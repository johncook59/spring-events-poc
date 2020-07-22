package scratch.events;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "thing")
public class Thing {

    @Id
    @GeneratedValue
    private int id;

    @Version
    private int version = 0;

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Thing{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Thing setName(String name) {
        this.name = name;
        return this;
    }
}
