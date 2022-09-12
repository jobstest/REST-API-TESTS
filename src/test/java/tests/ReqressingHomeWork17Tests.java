package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ReqressingHomeWork17Tests extends TestBase {

    @Test
    void registerSuccesfulTest() {
        String body = "{ \"email\": \"eve.holt@reqres.in\"," +
                " \"password\": \"pistol\" }";

        given()
                .log().uri()
                .log().body()
                .body(body)
                .contentType(JSON)
                .when()
                .post("/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is(notNullValue()));
    }

    @Test
    void createTest() {
        String body = "{ \"name\": \"morpheus\"," +
                " \"job\": \"leader\" }";

        given()
                .log().uri()
                .log().body()
                .body(body)
                .contentType(JSON)
                .when()
                .post("/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"))
                .body("id", is(notNullValue()));
    }

    @Test
    void putTest() {
        String body = "{ \"name\": \"morpheus\", " +
                "\"job\": \"zion resident\" }";

        given()
                .log().uri()
                .log().body()
                .body(body)
                .contentType(JSON)
                .when()
                .post("/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"));
    }

    @Test
    void delayedResponseTest() {
        String body = "{ \"name\": \"morpheus\", " +
                "\"job\": \"zion resident\" }";

        given()
                .log().uri()
                .log().body()
                .body(body)
                .contentType(JSON)
                .when()
                .get("/api/users?delay=3")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("per_page", is(6))
                .body("total", is(12)).body("total_pages", is(2));
    }

    @Test
    void registerUnsuccesfulTest() {
        String body = "{ \"email\": \"sydney@fife\" }";

        given()
                .log().uri()
                .log().body()
                .body(body)
                .contentType(JSON)
                .when()
                .post("/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
