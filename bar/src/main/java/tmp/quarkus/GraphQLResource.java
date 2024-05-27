package tmp.quarkus;

import java.util.List;
import java.util.ArrayList;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import tmp.quarkus.entities.Film;

@GraphQLApi
public class GraphQLResource {
    @Query("allFilms") 
    @Description("Get all Films from a galaxy far far away") 
    public List<Film> getAllFilms() {
        return new ArrayList<>();
    }
}
