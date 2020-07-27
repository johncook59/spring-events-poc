package scratch.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemService {

    private final ItemDao itemDao;
    private final ApplicationEventPublisher publisher;

    public ItemService(ItemDao itemDao, ApplicationEventPublisher publisher) {
        this.itemDao = itemDao;
        this.publisher = publisher;
    }

    public void create(String name) {
        Item item = new Item();

        item.setName(name);
        itemDao.save(item);

        publisher.publishEvent(new ItemCreatedEvent(item));
    }

    public void update(String name, String newName) {
        Item item = itemDao.findByName(name);

        item.setName(newName);
        publisher.publishEvent(new ItemUpdatedEvent(item));
    }
}
