package scratch.events;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class LoggingEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommittedEvent(ThingEvent event) {
        System.out.printf("Committed change to %s", event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleRolledBackEvent(ThingEvent event) {
        System.out.printf("Rolled back change to %s", event);
    }
}