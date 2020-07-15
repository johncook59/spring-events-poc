package zarg.bank.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import zarg.bank.dao.AccountDao;
import zarg.bank.domain.Account;
import zarg.bank.events.CreditEvent;
import zarg.bank.events.CreditFailedEvent;
import zarg.bank.events.DebitEvent;
import zarg.bank.events.DebitFailedEvent;

import java.math.BigDecimal;

@Service
@Transactional
class DefaultTellerService implements TellerService {

    private final AccountDao accountDao;
    private final ApplicationEventPublisher publisher;

    DefaultTellerService(AccountDao accountDao, ApplicationEventPublisher publisher) {
        this.accountDao = accountDao;
        this.publisher = publisher;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void credit(String accountBid, BigDecimal amount) {
        try {
            Assert.isTrue(amount.doubleValue() > 0, "Invalid amount");

            updateBalance(accountBid, amount);
            publisher.publishEvent(new CreditEvent(accountBid, amount));

        } catch (Exception e) {
            publisher.publishEvent(new CreditFailedEvent(accountBid, e.getMessage(), amount));
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void debit(String accountBid, BigDecimal amount) {
        try {
            Assert.isTrue(amount.doubleValue() > 0, "Invalid amount");

            updateBalance(accountBid, amount.negate());
            publisher.publishEvent(new DebitEvent(accountBid, amount));

        } catch (Exception e) {
            publisher.publishEvent(new DebitFailedEvent(accountBid, e.getMessage(), amount));
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public BigDecimal balance(String accountBid) {
        return findAccount(accountBid).getBalance();
    }

    private Account findAccount(String accountBid) {
        return accountDao.findByBid(accountBid);
    }

    private void updateBalance(String accountBid, BigDecimal amount) {
        Account account = findAccount(accountBid);
        account.setBalance(account.getBalance().add(amount));
        accountDao.save(account);
    }
}
