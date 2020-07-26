package scratch.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class LoggingEventListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommittedEvent(ItemEvent event) {
        logger.info("Committed change to {}", event.getItem());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleRolledBackEvent(ItemEvent event) {
        logger.warn("Rolled back change to {}", event.getItem());
    }
}