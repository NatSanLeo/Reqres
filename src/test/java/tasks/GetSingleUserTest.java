package tasks;

import conf.Endpoinds;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;


public class GetSingleUserTest {
    public ValidatableResponse byId(Integer id){

     return    given()
             .pathParam("userId",id)
             .get(Endpoinds.USERS.path())
                .then();
    }

}
