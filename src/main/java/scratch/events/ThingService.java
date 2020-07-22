package scratch.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ThingService {

    private final ThingDao thingDao;
    private final ApplicationEventPublisher publisher;

    public ThingService(ThingDao thingDao, ApplicationEventPublisher publisher) {
        this.thingDao = thingDao;
        this.publisher = publisher;
    }

    public void create(String name) {
        Thing thing = new Thing();

        thing.setName(name);
        thingDao.save(thing);

        publisher.publishEvent(new ThingEvent(thing));
    }

    public void update(String name, String newName) {
        Thing thing = thingDao.findByName(name);

        thing.setName(newName);
        publisher.publishEvent(new ThingEvent(thing));
    }
}
