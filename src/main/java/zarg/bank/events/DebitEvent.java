package zarg.bank.events;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString(callSuper = true)
public class DebitEvent extends TellerEvent {
    private final BigDecimal amount;

    public DebitEvent(String accountBid, BigDecimal amount) {
        super(accountBid, "");
        this.amount = amount;
    }
}
