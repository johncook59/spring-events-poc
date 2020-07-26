package scratch.events;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private int version = 0;

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @Override
    public String toString() {
        return new StringBuilder("Item{")
                .append("name='").append(name).append('\'')
                .append('}')
                .toString();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }
}
