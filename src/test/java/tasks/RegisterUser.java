package tasks;

import conf.Endpoinds;
import io.restassured.response.ValidatableResponse;
import models.CreateUserRequest;

import static io.restassured.RestAssured.given;

public class RegisterUser {
    public ValidatableResponse withInfo(CreateUserRequest createUserRequest) {
        return given()
                .body(createUserRequest)
                .post(Endpoinds.REGISTER.path())
                .then();
    }
}
