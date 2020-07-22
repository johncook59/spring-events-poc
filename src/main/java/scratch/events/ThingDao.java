package scratch.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingDao extends JpaRepository<Thing, Integer> {
    Thing findByName(String name);
}
