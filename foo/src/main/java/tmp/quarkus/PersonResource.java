package tmp.quarkus;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tmp.quarkus.entities.Person;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    @GET
    public List<Person> listAll() {
        return Person.listAll();
    }

    @GET
    @Path("/{id}")
    public Person findById(@PathParam("id") Long id) {
        Person person = Person.findById(id);

        if (person == null) {
            throw new WebApplicationException(404);
        }

        return person;
    }

    @POST
    @Transactional
    public Response create(Person person) {
        person.persist();
        return Response
            .created(URI.create("/persons/" + person.id))
            .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Person update(@PathParam("id") Long id, Person person) {
        Person _person = Person.findById(id);

        if (_person == null) {
            throw new WebApplicationException(404);
        }

        _person.name = person.name;

        return _person;
    }


    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Person _person = Person.findById(id);

        if (_person == null) {
            throw new WebApplicationException(404);
        }

        _person.delete();
    }

    @GET
    @Path("/search")
    public Person search(@QueryParam("name") Optional<String> name) {
        if (name.isEmpty()) {
            throw new WebApplicationException(400);
        }

        Person person = Person.findByName(name.get());

        if (person == null) {
            throw new WebApplicationException(404);
        }

        return person;
    }

    @GET
    @Path("/count")
    public Long count() {
        return Person.count();
    }
}
