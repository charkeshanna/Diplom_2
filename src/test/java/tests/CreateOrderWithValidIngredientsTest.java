package tests;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pojo.request.CreateOrderRequest;
import pojo.response.UserRegistrationResponse;
import steps.CreateUserSteps;
import steps.OrdersSteps;
import utils.DataGenerator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateOrderWithValidIngredientsTest extends BaseTest{
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
        //получу список заказов
        ordersSteps = new OrdersSteps();
        allIngredients = ordersSteps.getAllIngredientsList();
        //создадим юзера
        createUserSteps = new CreateUserSteps();
        //сгенерируем email & password
        email = DataGenerator.generateUserEmail();
        password = DataGenerator.generateUserPassword();
        firstName = email + "firstName";
        //создадим юзера
        UserRegistrationResponse userRegistrationResponse = createUserSteps.createNewUserReturnsSuccessAndResponse(email, password, firstName);
        isUserCreated = true;
        accessToken = userRegistrationResponse.getAccessToken();

    }

    @AfterEach
    public void tearDown() {
        if(isUserCreated) {
            createUserSteps.removeCreatedUser(accessToken);
        }

    }

    @Test
    @DisplayName("Create order for Authorized user with ingredients")
    public void OrderWithIngredientsForAuthorizedUserCanBeAdded() {
        //получим здесь список рандомный ингредиентов
        List<String> random = ordersSteps.getRandomIngredients(allIngredients, 3);
        //создадим объект с заказом
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(random);
        //будем передавать наш заказ
        Response response = ordersSteps.sendCreateOrderRequest(accessToken, createOrderRequest);
        //проверим код ответа
        ordersSteps.checkStatusCode(response,200);
        //проверим успешность сообщения
        ordersSteps.checkResponseValue(response, "success", true);
        //сохраним номер заказа который возвращается в ответе
        int orderNumber = response.jsonPath().getInt("order.number");
        assertNotNull(orderNumber, "OrderNumber should not be null");
        assertTrue(orderNumber>0, "Order Number should be greater than 0");

    }

    @Test
    @DisplayName("Create order for Non-Authorized user with ingredients - without token")
    public void OrderWithIngredientsForNonAuthorizedUserCanBeAdded() {
        //получим здесь список рандомный ингредиентов
        List<String> random = ordersSteps.getRandomIngredients(allIngredients, 3);
        //создадим объект с заказом
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(random);
        //будем передавать наш заказ
        Response response = ordersSteps.sendCreateOrderRequestWithoutToken(createOrderRequest);
        //проверим код ответа
        ordersSteps.checkStatusCode(response,200);
        //проверим успешность сообщения
        ordersSteps.checkResponseValue(response, "success", true);
        //сохраним номер заказа который возвращается в ответе
        int orderNumber = response.jsonPath().getInt("order.number");
        assertNotNull(orderNumber, "OrderNumber should not be null");
        assertTrue(orderNumber>0, "Order Number should be greater than 0");
    }

    @Test
    @DisplayName("Create order for Non-Authorized user with ingredients - with wrong token")
    public void OrderWithIngredientsForNonAuthorizedUserWithWrongTokenCanBeAdded() {
        //получим здесь список рандомный ингредиентов
        List<String> random = ordersSteps.getRandomIngredients(allIngredients, 3);
        //создадим объект с заказом
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(random);
        //обновим токен
        String accessTokenUpdated = accessToken + "1";
        //будем передавать наш заказ
        Response response = ordersSteps.sendCreateOrderRequest(accessTokenUpdated, createOrderRequest);
        //проверим код ответа
        ordersSteps.checkStatusCode(response,403);
        //проверим успешность сообщения
        ordersSteps.checkResponseValue(response, "success", false);
        ordersSteps.checkResponseValue(response, "message", "invalid signature");

    }




}
