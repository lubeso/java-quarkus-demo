package tmp.quarkus;

import java.net.URI;
import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
    public Person findById(Long id) {
        return Person.findById(id);
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
    public Person update(Long id, Person person) {
        Person _person = Person.findById(id);

        if (_person == null) {
            throw new NotFoundException();
        }

        _person.name = person.name;

        return _person;
    }


    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(Long id) {
        Person _person = Person.findById(id);

        if (_person == null) {
            throw new NotFoundException();
        }

        _person.delete();
    }

    @GET
    @Path("/search/{name}")
    public Person findByName(String name) {
        return Person.findByName(name);
    }

    @GET
    @Path("/count")
    public Long count() {
        return Person.count();
    }
}
