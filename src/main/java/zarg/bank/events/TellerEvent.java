package zarg.bank.events;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
public class TellerEvent {
    private final String accountBid;
    private final String message;
    private final Instant instant;

    public TellerEvent(String accountBid, String message) {
        this.accountBid = accountBid;
        this.message = message;
        this.instant = Instant.now();
    }
}
