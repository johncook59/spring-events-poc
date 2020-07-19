package zarg.debitcredit.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zarg.debitcredit.dao.AccountDao;
import zarg.debitcredit.domain.Account;
import zarg.debitcredit.events.CreditEvent;
import zarg.debitcredit.events.CreditFailedEvent;
import zarg.debitcredit.events.DebitEvent;
import zarg.debitcredit.events.DebitFailedEvent;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.math.BigDecimal;

@Service("PessimisticLockingTellerService")
@ConditionalOnProperty(prefix = "transaction", name = "lock-mode", havingValue = "pessimistic")
@Transactional
class PessimisticLockingTellerService implements TellerService {

    private final AccountDao accountDao;
    private final ApplicationEventPublisher publisher;

    PessimisticLockingTellerService(AccountDao accountDao, ApplicationEventPublisher publisher) {
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
        try {
            Account account =accountDao.findAndLockByBid(accountBid)
                    .orElseThrow(() -> new EntityNotFoundException(accountBid));

            account.setBalance(account.getBalance().add(amount));
            accountDao.save(account);
        } catch (NoResultException e) {
            throw new EntityNotFoundException(accountBid);
        }
    }
}
