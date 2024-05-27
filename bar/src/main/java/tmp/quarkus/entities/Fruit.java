package tmp.quarkus.entities;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

public class Fruit {

    public Long id;
    public String name;

    public Fruit() {}  // default

    public Fruit(String name) {
        this.name = name;
    }

    public Fruit(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Multi<Fruit> findAll(PgPool connection) {
        return connection.query("SELECT id, name FROM fruits ORDER BY name ASC").execute()
            .onItem().transformToMulti(set -> Multi.createFrom().iterable(set)) 
            .onItem().transform(Fruit::from); 
    }

    public static Uni<Fruit> findById(PgPool connection, Long id) {
        return connection.preparedQuery("SELECT id, name FROM fruits WHERE id = $1").execute(Tuple.of(id)) 
            .onItem().transform(RowSet::iterator) 
            .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null); 
    }

    public Uni<Long> save(PgPool connection) {
        return connection.preparedQuery("INSERT INTO fruits (name) VALUES ($1) RETURNING id").execute(Tuple.of(name))
            .onItem().transform(rowSet -> rowSet.iterator().next().getLong("id"));
    }

    public static Uni<Boolean> delete(PgPool connection, Long id) {
        return connection.preparedQuery("DELETE FROM fruits WHERE id = $1").execute(Tuple.of(id))
            .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1); 
    }

    private static Fruit from(Row row) {
        return new Fruit(row.getLong("id"), row.getString("name"));
    }
}
