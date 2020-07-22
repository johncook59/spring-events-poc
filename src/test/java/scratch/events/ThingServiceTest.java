package scratch.events;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ThingServiceTest {

    private static final String NAME = "Nelson";
    private static final String WORK_NAME = "Madiba";

    @Autowired
    private ThingService underTest;

    @Autowired
    private ThingDao thingDao;

    @MockBean
    private LoggingEventListener loggingEventListener;

    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    @Before
    public void setUp() {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @After
    public void teardown() {
        thingDao.deleteAll();
    }

    @Test
    public void shouldHandleCommitEventOnCreate() {
        transactionTemplate.execute(status -> {
            underTest.create(NAME);
            return null;
        });

        verify(loggingEventListener).handleCommittedEvent(any());
        verify(loggingEventListener, never()).handleRolledBackEvent(any());
    }

    @Test
    public void shouldHandleCommitEventOnUpdateToNewName() {
        transactionTemplate.execute(status -> {
            underTest.create(NAME);
            return null;
        });

        Mockito.reset(loggingEventListener);

        transactionTemplate.execute(status -> {
            underTest.update(NAME, WORK_NAME);
            return null;
        });

        verify(loggingEventListener).handleCommittedEvent(any());
        verify(loggingEventListener, never()).handleRolledBackEvent(any());
    }

    @Test
    public void shouldHandleRollbackEventOnUpdateToExistingName() {
        transactionTemplate.execute(status -> {
            underTest.create(NAME);
            underTest.create(WORK_NAME);
            return null;
        });

        Mockito.reset(loggingEventListener);

        try {
            transactionTemplate.execute(status -> {
                underTest.update(NAME, WORK_NAME);
                return null;
            });
        } catch (Exception ignore) {
        }

        verify(loggingEventListener, never()).handleCommittedEvent(any());
        verify(loggingEventListener).handleRolledBackEvent(any());
    }

}