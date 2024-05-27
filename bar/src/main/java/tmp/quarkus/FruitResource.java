package tmp.quarkus;

import java.net.URI;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import tmp.quarkus.entities.Fruit;

@Path("/fruits")
public class FruitResource {

    @Inject
    private PgPool connection;

    public FruitResource(PgPool connection) {
        this.connection = connection;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Fruit> findAll() {
        return Fruit.findAll(connection);
    }

    @GET
    @Path("{id}")
    public Uni<Response> findById(Long id) {
        return Fruit.findById(connection, id)
            .onItem().transform(fruit -> fruit != null ? Response.ok(fruit) : Response.status(Status.NOT_FOUND)) 
            .onItem().transform(ResponseBuilder::build); 
    }

    @POST
    public Uni<Response> create(Fruit fruit) {
        return fruit.save(connection)
                .onItem().transform(id -> URI.create("/fruits/" + id))
                .onItem().transform(uri -> Response.created(uri).build());
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(Long id) {
        return Fruit.delete(connection, id)
            .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
            .onItem().transform(status -> Response.status(status).build());
    }
}
