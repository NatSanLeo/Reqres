package tasks;

import models.CreateUserRequest;
import models.CreateUserResponse;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class RegisterSuccessfulUser {
    public CreateUserResponse withInfo(CreateUserRequest createUserRequest) {
        RegisterUser registerUser = new RegisterUser();
        var registerUserResponse = registerUser
                .withInfo(createUserRequest);

        registerUserResponse.statusCode(SC_OK);
        var registerUserResponseModel = registerUserResponse
                .extract().as(CreateUserResponse.class);


        assertThat(registerUserResponseModel.getToken(), notNullValue());

        return registerUserResponseModel;

    }
}
