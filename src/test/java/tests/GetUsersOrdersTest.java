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

import static org.junit.jupiter.api.Assertions.*;

public class GetUsersOrdersTest extends BaseTest {

    OrdersSteps ordersSteps;
    List<String> allIngredients;
    private String accessToken;
    CreateUserSteps createUserSteps;
    private boolean isUserCreated;
    private  String email;
    private  String password;
    private  String firstName;


    @BeforeEach
    public  void setUp() {
        //сгенерируем email & password
        email = DataGenerator.generateUserEmail();
        password = DataGenerator.generateUserPassword();
        firstName = email + "firstName";
        //создадим юзера
        createUserSteps = new CreateUserSteps();
        UserRegistrationResponse userRegistrationResponse = createUserSteps.createNewUserReturnsSuccessAndResponse(email, password, firstName);
        isUserCreated = true;
        accessToken = userRegistrationResponse.getAccessToken();

        //получу список заказов чтобы потом вытянуть хэши и сформировать свой заказ
        ordersSteps = new OrdersSteps();
        allIngredients = ordersSteps.getAllIngredientsList();
        int successOrders = 0;
        while (successOrders < 2) {
            List<String> random = ordersSteps.getRandomIngredients(allIngredients, 3);
            CreateOrderRequest createOrderRequest = new CreateOrderRequest(random);
            Response response = ordersSteps.sendCreateOrderRequest(accessToken, createOrderRequest);

            if (response.getStatusCode() == 200) {
                successOrders++;
            }
        }


    }

    @AfterEach
    public void tearDown() {
        if(isUserCreated) {
            createUserSteps.removeCreatedUser(accessToken);
        }

    }

    @Test
    @DisplayName("get users orders")
    public void getUsersOrdersRequestReturnsSomething() {
        Response response = ordersSteps.sendGetOrdersRequestForAuthorizedUser(accessToken);

        ordersSteps.checkStatusCode(response,200);
        //проверим успешность сообщения
        ordersSteps.checkResponseValue(response, "success", true);
        //сохраним номер заказа который возвращается в ответе
        int totalToday = response.jsonPath().getInt("totalToday");
        System.out.println(totalToday);
        assertEquals(2, totalToday);
        //assertNotNull(orderNumber, "OrderNumber should not be null");
        //assertTrue(totalToday==2, "Order Number should be greater than 0");

    }


}
