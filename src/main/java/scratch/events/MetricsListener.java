package scratch.events;

import com.timgroup.statsd.StatsDClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MetricsListener {

    private static final String ITEM_METRIC = "item";
    private static final String OPERATION_UPDATE = ".update";
    private static final String OPERATION_INSERT = ".insert";
    private static final String OPERATION_DELETE = ".insert";
    private static final String SUCCESS = ".success";
    private static final String ROLLEDBACK = ".rolledback";

    private final StatsDClient statsd;

    public MetricsListener(StatsDClient statsd) {
        this.statsd = statsd;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleItemChangeCommitEvent(ItemEvent event) {
        String metric = buildMetricName(event) + SUCCESS;
        statsd.incrementCounter(metric);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleItemChangeRolledBackEvent(ItemEvent event) {
        String metric = buildMetricName(event) + ROLLEDBACK;
        statsd.incrementCounter(metric);
    }

    private static String buildMetricName(ItemEvent event) {
        String name = ITEM_METRIC;
        switch (event.getEvent()) {
            case CREATED:
                return name + OPERATION_INSERT;
            case UPDATED:
                return name + OPERATION_UPDATE;
            case DELETED:
                return name + OPERATION_DELETE;
        }

        throw new IllegalArgumentException("Null Event value");
    }
}