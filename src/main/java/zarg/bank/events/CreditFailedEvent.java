package zarg.bank.events;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString(callSuper = true)
public class CreditFailedEvent extends TellerEvent {
    private final BigDecimal amount;

    public CreditFailedEvent(String accountBid, String message, BigDecimal amount) {
        super(accountBid, message);
        this.amount = amount;
    }
}
