import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static io.restassured.path.json.JsonPath.from;
@Tag("Regresion")
public class ReqResTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(ReqResTest.class);


    @Test
    public void loginTest() {
        String nameOfUser = "Natalia";
        logger.info("Running login test by {}", nameOfUser);
        given()
                .body("""
                        {
                            "email": "eve.holt@reqres.in",
                            "password": "cityslicka"
                        }""")
                .post("/login")
                .then()
                .body("token", notNullValue())


        ;

    }

    @Test
    public void getSingleUserTest() {
        given()
                .get("/users/2")
                .then().
                statusCode(HttpStatus.SC_OK)
                .body("data.id", equalTo(2))
                .body("data.email", containsStringIgnoringCase("JANET"))
                .body("data.email", equalToIgnoringCase("janet.weaver@reqres.in"))
                .body("data.id", greaterThan(1))
        ;

    }

    @Test
    public void deleteUserTest() {
        given()
                .delete("/users/2")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

    }

    @Test
    @Tag("Authentication")
    public void patchUserTest() {
        String nameUpdated = given()
                .when()
                .body("""
                        {
                            "name": "morpheus",
                            "job": "zion resident"
                        }""")
                .patch("/users/2")

                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getString("name");
        assertThat(nameUpdated, equalTo("morpheus"));

    }

    @Test
    @Tag("Authentication")
    public void putUserTest() {
        String jobUpdated = given()
                .when()
                .body("""
                        {
                            "name": "morpheus",
                            "job": "zion resident"
                        }""")
                .put("/users/2")

                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getString("job");
        assertThat(jobUpdated, equalTo("zion resident"));

    }

    @Test
    public void getAllUsersTest() {
        Response response = given().get("/users?page=2");
        Headers headers = response.getHeaders();
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        String contentType = response.getContentType();

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        System.out.println("body" + body);
        System.out.println("contentType" + contentType);
        System.out.println("Headers" + headers);
        System.out.println("********");
        System.out.println(headers.get("Content-Type"));
        System.out.println(headers.get("Transfer-Encoding"));
    }

    @Test
    public void getAllUsersTest2() {
        String response = given().when()
                .get("/users?page=2").then().extract().body().asString();
        int page = from(response).get("page");
        int totalPages = from(response).get("total_pages");
        int idFirsUser = from(response).get("data[0].id");
        System.out.println("page: " + page);
        System.out.println("totalpages: " + totalPages);
        System.out.println("Id Usuario: " + idFirsUser);


        List<Map> usersWithIDGreaterThan10 = from(response).get("data.findAll {user -> user.id >10}");
        String email = usersWithIDGreaterThan10.get(0).get("email").toString();
        System.out.println("Email: " + email);
        System.out.println("Lista de usuarios" + usersWithIDGreaterThan10);

        List<Map> user = from(response).get("data.findAll {user -> user.id >10 && user.last_name=='Howell'}");
        int id = Integer.valueOf(user.get(0).get("id").toString());
        System.out.println("User: " + user);
        System.out.println("id: " + id);


    }

    @Test
    public void createUserTest() {
        String response = given().when()
                .body("""
                        {
                            "name": "morpheus",
                            "job": "leader"
                        }""")
                .post("/users").then().extract().body().asString();
        User user = from(response).getObject("", User.class);

        System.out.println("ID de usuario: " + user.getId());
        System.out.println("Job de usuario: " + user.getJob());
    }

    @Test
    public void serializationTest() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
        var user = new User();
        user.setId("2");
        user.setCreatedAt("15 Agosto 550");
        user.setName("Natalia");
        user.setJob("Software Engineer");

        var serializad = mapper.writeValueAsString(user);
        System.out.println(serializad);

    }

    @Test
    public void desserializationTest() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
        var jsonString = """
                {
                  "createdAt" : "15 Agosto 550",
                  "name" : "Natalia",
                  "id" : "2",
                  "job" : "Software Engineer"
                }""";


        var deserializad = mapper.readValue(jsonString, User.class);
        System.out.println(deserializad.getId());
        System.out.println(deserializad.getName());
        System.out.println(deserializad.getCreatedAt());


    }


    @Test
    public void registerUserTest() {
        CreateUserRequest createUserRequest = new CreateUserRequest();

        createUserRequest.setEmail("eve.holt@reqres.in");
        createUserRequest.setPassword("pistol");

        CreateUserResponse userResponse = given().when()
                .body(createUserRequest)
                .post("/register")
                .then()
                .extract()
                .body()
                .as(CreateUserResponse.class);

        assertThat(userResponse.getId(), equalTo(4));
        assertThat(userResponse.getToken(), equalTo("QpwL5tke4Pnpja7X4"));
        System.out.println("id: " + userResponse.getId());
        System.out.println("token: " + userResponse.getToken());


    }

}
