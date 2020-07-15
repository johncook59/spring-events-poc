package zarg.bank.service;

import java.math.BigDecimal;

public interface TellerService {
    void credit(String accountBid, BigDecimal amount);

    void debit(String accountBid, BigDecimal amount);

    BigDecimal balance(String accountBid);
}
