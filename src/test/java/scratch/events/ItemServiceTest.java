package scratch.events;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceTest {

    private static final String NAME = "Nelson";
    private static final String WORK_NAME = "Madiba";

    @Autowired
    private ItemService underTest;

    @Autowired
    private ItemDao itemDao;

    @MockBean
    private MockingEventListener eventListener;

    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    @Before
    public void setUp() {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @After
    public void teardown() {
        itemDao.deleteAll();
    }

    @Test
    public void shouldHandleCommitEventOnCreate() {
        transactionTemplate.execute(status -> {
            underTest.create(NAME);
            return null;
        });

        verify(eventListener).handleCommittedEvent(any(ItemCreatedEvent.class));
        verify(eventListener, never()).handleCommittedEvent(any(ItemUpdatedEvent.class));
        verify(eventListener, never()).handleRolledBackEvent(any(ItemCreatedEvent.class));
        verify(eventListener, never()).handleRolledBackEvent(any(ItemUpdatedEvent.class));
    }

    @Test
    public void shouldHandleCommitEventOnUpdateToNewName() {
        transactionTemplate.execute(status -> {
            underTest.create(NAME);
            return null;
        });

        Mockito.reset(eventListener);

        transactionTemplate.execute(status -> {
            underTest.update(NAME, WORK_NAME);
            return null;
        });

        verify(eventListener, never()).handleCommittedEvent(any(ItemCreatedEvent.class));
        verify(eventListener).handleCommittedEvent(any(ItemUpdatedEvent.class));
        verify(eventListener, never()).handleRolledBackEvent(any(ItemCreatedEvent.class));
        verify(eventListener, never()).handleRolledBackEvent(any(ItemUpdatedEvent.class));
    }

    @Test
    public void shouldHandleRollbackEventOnCreateWithDuplicateName() {
        transactionTemplate.execute(status -> {
            underTest.create(NAME);
            return null;
        });

        Mockito.reset(eventListener);

        try {
            transactionTemplate.execute(status -> {
                underTest.create(NAME);
                return null;
            });
        } catch (Exception ignore) {
        }

        verify(eventListener, never()).handleCommittedEvent(any(ItemCreatedEvent.class));
        verify(eventListener, never()).handleCommittedEvent(any(ItemUpdatedEvent.class));
        verify(eventListener).handleRolledBackEvent(any(ItemCreatedEvent.class));
        verify(eventListener, never()).handleRolledBackEvent(any(ItemUpdatedEvent.class));
    }

    @Test
    public void shouldHandleRollbackEventOnUpdateToExistingName() {
        transactionTemplate.execute(status -> {
            underTest.create(NAME);
            underTest.create(WORK_NAME);
            return null;
        });

        Mockito.reset(eventListener);

        try {
            transactionTemplate.execute(status -> {
                underTest.update(NAME, WORK_NAME);
                return null;
            });
        } catch (Exception ignore) {
        }

        verify(eventListener, never()).handleCommittedEvent(any(ItemCreatedEvent.class));
        verify(eventListener, never()).handleCommittedEvent(any(ItemUpdatedEvent.class));
        verify(eventListener, never()).handleRolledBackEvent(any(ItemCreatedEvent.class));
        verify(eventListener).handleRolledBackEvent(any(ItemUpdatedEvent.class));
    }

    @Component
    static class MockingEventListener {

        @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
        public void handleCommittedEvent(ItemCreatedEvent event) {
        }

        @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
        public void handleCommittedEvent(ItemUpdatedEvent event) {
        }

        @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
        public void handleRolledBackEvent(ItemCreatedEvent event) {
        }

        @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
        public void handleRolledBackEvent(ItemUpdatedEvent event) {
        }
    }
}