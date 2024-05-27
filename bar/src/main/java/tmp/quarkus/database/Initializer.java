package tmp.quarkus.database;

import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class Initializer {

    private final PgPool connection;
    private final boolean createSchema;

    public Initializer(
            PgPool connection,
            @ConfigProperty(name = "custom.datasource.create-schema", defaultValue = "true") boolean createSchema
    ) {
        this.connection = connection;
        this.createSchema = createSchema;
    }

    void onStart(@Observes StartupEvent ev) {
        if (this.createSchema) {
            createSchema();
        }
    }

    private void createSchema() {
        connection
            .query("DROP TABLE IF EXISTS fruits").execute()
            .flatMap(r -> connection.query("CREATE TABLE fruits (id SERIAL PRIMARY KEY, name TEXT NOT NULL)").execute())
            .flatMap(r -> connection.query("INSERT INTO fruits (name) VALUES ('Kiwi')").execute())
            .flatMap(r -> connection.query("INSERT INTO fruits (name) VALUES ('Durian')").execute())
            .flatMap(r -> connection.query("INSERT INTO fruits (name) VALUES ('Pomelo')").execute())
            .flatMap(r -> connection.query("INSERT INTO fruits (name) VALUES ('Lychee')").execute())
            .await().indefinitely();
    }
}
