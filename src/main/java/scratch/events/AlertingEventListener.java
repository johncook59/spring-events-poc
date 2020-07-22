package scratch.events;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AlertingEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleRolledBackEvent(ThingEvent event) {
        System.out.println("Increment Datadog counter scratch.thing.operation_failure");
    }
}