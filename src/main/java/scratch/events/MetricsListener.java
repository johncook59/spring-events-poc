package scratch.events;

import com.timgroup.statsd.StatsDClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MetricsListener {

    private static final String ITEM_METRIC = "item";
    private static final String OPERATION_UPDATE = ITEM_METRIC + ".update";
    private static final String OPERATION_INSERT = ITEM_METRIC + ".insert";
    private static final String SUCCESS = ".success";
    private static final String ROLLEDBACK = ".rolledback";

    private final StatsDClient statsd;

    public MetricsListener(StatsDClient statsd) {
        this.statsd = statsd;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommittedEvent(ItemCreatedEvent event) {
        statsd.incrementCounter(OPERATION_INSERT + SUCCESS);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommittedEvent(ItemUpdatedEvent event) {
        statsd.incrementCounter(OPERATION_UPDATE + SUCCESS);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleRolledBackEvent(ItemCreatedEvent event) {
        statsd.incrementCounter(OPERATION_INSERT + ROLLEDBACK);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleRolledBackEvent(ItemUpdatedEvent event) {
        statsd.incrementCounter(OPERATION_UPDATE + ROLLEDBACK);
    }
}