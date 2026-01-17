package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.request.CreateOrderRequest;
import pojo.response.GetOrdersResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrdersSteps {

    @Step("Get ingredients list")
    //получим список ингредиентов
    public List<String> getAllIngredientsList() {
        return given()
                .when()
                .get("/api/ingredients")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data._id", String.class);
    }

    @Step("Method to choose random ingredients")
    public List<String> getRandomIngredients(List<String> allIngredients, int count) {
        List<String> copy = new ArrayList<>(allIngredients);
        Collections.shuffle(copy);
        return copy.subList(0, count);
    }

    @Step("Send request to create an order with authorization")
    public Response sendCreateOrderRequest(String accessToken, CreateOrderRequest createOrderRequest) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(createOrderRequest)
                .when()
                .post("/api/orders");
    }

    @Step("Send request to create an order without token")
    public Response sendCreateOrderRequestWithoutToken(CreateOrderRequest createOrderRequest) {
        return given()
                .header("Content-type", "application/json")
                .body(createOrderRequest)
                .when()
                .post("/api/orders");
    }

    @Step("Check status code {expectedCode} of the response")
    public  void checkStatusCode(Response response, int expectedCode) {
        int actualCode = response.getStatusCode();
        assertEquals(expectedCode, actualCode, "Статус-код не совпадает!");
    }

    @Step("Check message body: value '{key}' equals '{expectedValue}'")
    public  void checkResponseValue(Response response, String key, Object expectedValue) {
        Object actualValue = response.jsonPath().get(key);
        assertEquals(expectedValue, actualValue, "Значение по ключу '" + key + "' не совпадает");
    }

    @Step("Generate not existing hash of valid format")
    public String generateInvalidIngredientHash() {
        return "ffffffffffffffffffffffff";
    }

    @Step("Send getOrders request for Authorized user")
    public GetOrdersResponse sendGetOrdersRequestForAuthorizedUser(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders")
                .body().as(GetOrdersResponse.class);
    }

    @Step("Send request to get orders for unauthorized user")
    public Response sendGetOrdersRequestForNonAuthorizedUser() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/orders");
    }


}
