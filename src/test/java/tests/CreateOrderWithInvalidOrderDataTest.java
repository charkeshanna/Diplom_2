package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.request.CreateOrderRequest;
import pojo.response.UserRegistrationResponse;
import steps.CreateUserSteps;
import steps.OrdersSteps;
import utils.DataGenerator;

import java.util.List;

public class CreateOrderWithInvalidOrderDataTest extends BaseTest{
    OrdersSteps ordersSteps;
    private String accessToken;
    CreateUserSteps createUserSteps;
    private boolean isUserCreated;
    private  String email;
    private  String password;
    private  String firstName;


    @BeforeEach
    public  void setUp() {
        ordersSteps = new OrdersSteps();
        //создадим юзера
        createUserSteps = new CreateUserSteps();
        //сгенерируем email & password
        email = DataGenerator.generateUserEmail();
        password = DataGenerator.generateUserPassword();
        firstName = email + "firstName";
        //создадим юзера
        UserRegistrationResponse userRegistrationResponse = createUserSteps.createNewUserReturnsSuccessAndResponse(email, password, firstName);
        isUserCreated = true;
        //сохраним токен
        accessToken = userRegistrationResponse.getAccessToken();

    }

    @AfterEach
    public void tearDown() {
        if(isUserCreated) {
            createUserSteps.removeCreatedUser(accessToken);
        }

    }

    @Test
    @DisplayName("check that order without ingredients cannot be created")
    public void createOrderWithoutIngredientsReturnsError() {
        //заказ с пустым списком
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of());
        //получим ответ после отправки запроса на добавление пустого заказа
        Response response = ordersSteps.sendCreateOrderRequest(accessToken, createOrderRequest);
        //проверим код ответ
        ordersSteps.checkStatusCode(response, 400);
        //проверим тело сообщения
        ordersSteps.checkResponseValue(response, "success", false);
        ordersSteps.checkResponseValue(response, "message", "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("create order with not existing hash ingredient of valid format")
    public void createOrderWithNotExistingHashIngredientHashReturnsFail(){
        //generate invalid ingredient hash
        String notExistingHash = ordersSteps.generateInvalidIngredientHash();
        //добавим в заказ невалидный айди ингредиента
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(notExistingHash));
        //получим ответ после отправки запроса на добавление такого заказа
        Response response = ordersSteps.sendCreateOrderRequest(accessToken, createOrderRequest);
        //проверим код ответ
        ordersSteps.checkStatusCode(response, 400);
        //проверим тело сообщения
        ordersSteps.checkResponseValue(response, "success", false);
        ordersSteps.checkResponseValue(response, "message", "One or more ids provided are incorrect");
    }

    @Test
    @DisplayName("create order with not valid hash ingredient")
    public void createOrderWithInvalidIngredientHashReturnsFail(){
        //generate invalid ingredient hash
        String invalidIngredientHash = "123invalidHash";
        //добавим в заказ невалидный айди ингредиента
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(invalidIngredientHash));
        //получим ответ после отправки запроса на добавление такого заказа
        Response response = ordersSteps.sendCreateOrderRequest(accessToken, createOrderRequest);
        //проверим код ответ
        ordersSteps.checkStatusCode(response, 500);
    }


}
