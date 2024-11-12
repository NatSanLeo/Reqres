import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTest  extends  BaseTest{
    private static final Logger logger = LogManager.getLogger(LoginTest.class);

    @Test
    @Tag("Login")
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
}
