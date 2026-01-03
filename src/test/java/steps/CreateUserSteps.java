package steps;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.request.UpdateUserData;
import pojo.request.UserAuthenticationRequest;
import pojo.request.UserRegistrationRequest;
import pojo.response.UserRegistrationResponse;

import static org.hamcrest.Matchers.equalTo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static io.restassured.RestAssured.given;
public class CreateUserSteps {



    @Step("Create new User")
    public Response createUser(UserRegistrationRequest userRegistrationRequest) {
        return given()
                .header("Content-type", "application/json")
                .body(userRegistrationRequest)
                .when()
                .post("/api/auth/register");
    }

    @Step("Check status code {expectedCode} of the response")
    public  void checkStatusCode(Response response, int expectedCode) {
        int actualCode = response.getStatusCode();
        assertEquals(expectedCode, actualCode, "Статус-код не совпадает!");
    }

    @Step("Deserialization of the response")
    public UserRegistrationResponse userRegistrationResponse(Response response) {
        return response.then().extract().as(UserRegistrationResponse.class);
    }


    @Step("Check message body: value '{key}' equals '{expectedValue}'")
    public  void checkResponseValue(Response response, String key, Object expectedValue) {
        Object actualValue = response.jsonPath().get(key);
        assertEquals(expectedValue, actualValue, "Значение по ключу '" + key + "' не совпадает");
    }

    @Step("Check message body for negative testing")
    public void checkNegativeResponseValueForSkippedRequiredFieldsCreateRequest(Response response) {
        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Check status code and messages")
    public void checkAfterAttemptToCreateAlreadyExistingUserReturnMessage(Response response) {
        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Step("Remove user")
    public void removeCreatedUser(String accessToken) {
        given()
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(202);
    }

    @Step("Create a new user (the whole flow)")
    public UserRegistrationResponse createNewUserReturnsSuccessAndResponse(String email, String password, String firstName) {

        //создаю user
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(email, password, firstName);
        return given()
                .header("Content-Type", "application/json")
                .body(userRegistrationRequest)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(200)
                .extract()
                .as(UserRegistrationResponse.class);
    }

    @Step("Login as user")
    public  Response loginAsUser(UserAuthenticationRequest userAuthenticationRequest) {
        return given()
                .header("Content-type", "application/json")
                .body(userAuthenticationRequest)
                .when()
                .post("/api/auth/login");
    }

    @Step("Update user's data")
    public Response updateDataForAuthorizedUser(String accessToken, UpdateUserData updateUserData) {
        return given()
                .header("Authorization", accessToken)
                .header("Content-Type", "application/json")
                .body(updateUserData)
                .when()
                .patch("/api/auth/user");
      }

      @Step("Update User's Data for Non-Authorized User - no header with authorization")
    public Response updateDataForNonAuthorizedUser(UpdateUserData updateUserData) {
          return given()
                  .header("Content-Type", "application/json")
                  .body(updateUserData)
                  .when()
                  .patch("/api/auth/user");
      }


}
