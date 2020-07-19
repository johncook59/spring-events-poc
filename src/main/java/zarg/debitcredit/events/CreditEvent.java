package zarg.debitcredit.events;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString(callSuper = true)
public class CreditEvent extends TellerEvent {
    private final BigDecimal amount;

    public CreditEvent(String accountBid, BigDecimal amount) {
        super(accountBid, "");
        this.amount = amount;
    }
}
