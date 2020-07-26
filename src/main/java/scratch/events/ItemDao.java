package scratch.events;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDao extends JpaRepository<Item, Integer> {
    Item findByName(String name);
}
