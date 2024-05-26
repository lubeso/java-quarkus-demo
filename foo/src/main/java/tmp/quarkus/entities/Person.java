package tmp.quarkus.entities;

import tmp.quarkus.enums.Status;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Person extends PanacheEntity {
    public String name;
    public LocalDate birth;
    public Status status;

    public String getName() {
        return name.toUpperCase();
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public static Person findByName(String name) {
        return find("name", name).firstResult();
    }

    public static List<Person> findAlive() {
        return list("status", Status.Alive);
    }

    public static void deleteStefs() {
        delete("name", "Stef");
    }
}
