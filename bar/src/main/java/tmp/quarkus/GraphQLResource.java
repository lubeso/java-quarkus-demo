package tmp.quarkus;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;
import jakarta.inject.Inject;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import tmp.quarkus.entities.Fruit;

@GraphQLApi
public class GraphQLResource {

    @Inject
    private PgPool connection;

    public GraphQLResource(PgPool connection) {
        this.connection = connection;
    }

    @Query("allFruits") 
    @Description("Get all fruits")
    public Uni<List<Fruit>> getAllFruits() {
        return Fruit.findAll(connection).collect().asList();
    }

}
