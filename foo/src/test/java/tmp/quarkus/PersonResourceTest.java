package tmp.quarkus;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.restassured.http.ContentType;
import io.vertx.core.json.JsonObject;
import tmp.quarkus.enums.Status;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static io.restassured.RestAssured.given;


@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(OrderAnnotation.class)
class PersonResourceTest {

    private static String basePath = "/persons";

    @Test
    @Order(1)
    void testCount() {
        given()
            .when()
                .get(basePath + "/count")
            .then()
                .statusCode(200)
                .body(is("" + 1));
    }


    @Test
    @Order(2)
    void testFindAll() {
        given()
            .when()
                .get(basePath)
            .then()
                .statusCode(200)
                .body(
                    containsString("EMILY BROWN"),
                    containsString("1995-09-12"),
                    containsString("" + Status.Alive)
                );
    }

    @Test
    @Order(3)
    void testCreate() {
        JsonObject person = new JsonObject();
        person.put("name", "Jennifer Kertzmann");
        person.put("birth", "1975-04-23");
        person.put("status", Status.Alive);

        given()
            .contentType(ContentType.JSON)
            .body(person.toString())
            .when()
                .post("/persons")
            .then()
                .statusCode(201);
    }

    @Test
    @Order(4)
    void testFindById() {
        given()
            .pathParam("id", 1)
            .accept(ContentType.JSON)
            .when()
                .get(basePath + "/{id}")
            .then()
                .statusCode(200)
                .body(
                        containsString("EMILY BROWN"),
                        containsString("1995-09-12"),
                        containsString("" + Status.Alive)
                );
        ;
    }

}
