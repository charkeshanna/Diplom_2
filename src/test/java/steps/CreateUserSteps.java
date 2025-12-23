package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.request.UserRegistrationRequest;
import pojo.response.UserRegistrationResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static io.restassured.RestAssured.given;
public class CreateUserSteps {



    @Step("Create new User")
    public  UserRegistrationResponse createUser(UserRegistrationRequest userRegistrationRequest) {

        return given()
                .header("Content-type", "application/json")
                .body(userRegistrationRequest)
                .when()
                .post("/api/auth/register")
                .body().as(UserRegistrationResponse.class);
    }
/*
    @Step("Check status code {expectedCode} of the response")
    public  void checkStatusCode(Response response, int expectedCode) {
        int actualCode = response.getStatusCode();
        assertEquals(expectedCode, actualCode, "Статус-код не совпадает!");
    }*/
/*
    @Step("Check message body of the response {expectedValue}")
    public  void checkResponseValue(Response response, String key, Object expectedValue) {
        Object actualValue = response.jsonPath().get(key);
        assertEquals(expectedValue, actualValue, "Значение по ключу '" + key + "' не совпадает");
    }

    @Step("Login as a courier and get courierID")
    public  int getCourierIDafterSuccessLogin(CourierCredentials courierCredentials) {
        return given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }



    @Step("Login as courier (without checking the response)")
    public  Response loginWithLoginAndPassword(CourierCredentials courierCredentials) {
        return given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Get id after successful login")
    public  Integer getCourierIdAfterSuccessLogin(Response response) {
        return response.path("id");
    }

    @Step("Remove courier using courierId {id}")
    public  void deleteCourierById(int id) {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + String.valueOf(id))
                .then()
                .statusCode(200);

    }
    @Step("Add courier (without processing id)")
    public  Response addCourierReturnsSuccessResponse(String login, String password) {
        Courier courier = new Courier(login, password, null);
        Response response = createCourier(courier);
        checkStatusCode(response, 201);
        return response;
    }*/
}
