package zarg.bank.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
class LoggingTellerEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    void handleEvent(TellerEvent event) {
        log.info(event.toString());
    }
}