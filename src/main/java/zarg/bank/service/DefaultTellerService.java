package zarg.bank.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zarg.bank.dao.AccountDao;
import zarg.bank.domain.Account;
import zarg.bank.events.CreditEvent;
import zarg.bank.events.CreditFailedEvent;
import zarg.bank.events.DebitEvent;
import zarg.bank.events.DebitFailedEvent;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

@Service("DefaultTellerService")
@Transactional
class DefaultTellerService implements TellerService {

    private final AccountDao accountDao;
    private final ApplicationEventPublisher publisher;

    DefaultTellerService(AccountDao accountDao, ApplicationEventPublisher publisher) {
        this.accountDao = accountDao;
        this.publisher = publisher;
    }

    @Override
    public void credit(String accountBid, BigDecimal amount) {
        try {
            updateBalance(accountBid, amount);
            publisher.publishEvent(new CreditEvent(accountBid, amount));

        } catch (Exception e) {
            publisher.publishEvent(new CreditFailedEvent(accountBid, e.getMessage(), amount));
            throw e;
        }
    }

    @Override
    public void debit(String accountBid, BigDecimal amount) {
        try {
            updateBalance(accountBid, amount.negate());
            publisher.publishEvent(new DebitEvent(accountBid, amount));

        } catch (Exception e) {
            publisher.publishEvent(new DebitFailedEvent(accountBid, e.getMessage(), amount));
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal balance(String accountBid) {
        return accountDao.findByBid(accountBid)
                .orElseThrow(() -> new EntityNotFoundException(accountBid))
                .getBalance();
    }

    private void updateBalance(String accountBid, BigDecimal amount) {
        Account account = accountDao.findByBid(accountBid)
                .orElseThrow(() -> new EntityNotFoundException(accountBid));
        account.setBalance(account.getBalance().add(amount));
        accountDao.save(account);
    }
}
