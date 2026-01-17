package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.request.CreateOrderRequest;
import pojo.response.GetOrdersResponse;
import pojo.response.Order;
import pojo.response.UserRegistrationResponse;
import steps.CreateUserSteps;
import steps.OrdersSteps;
import utils.DataGenerator;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetUsersOrdersForAuthorizedUserTest extends BaseTest {

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
        //создам больше 50 заказов
        ordersSteps = new OrdersSteps();
        allIngredients = ordersSteps.getAllIngredientsList();
        int successOrders = 0;
        while (successOrders < 53) {
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
    public void getUsersOrdersRequestReturns50ordersSortedByUpdatedDate() {
        GetOrdersResponse getOrdersResponse = ordersSteps.sendGetOrdersRequestForAuthorizedUser(accessToken);
        assertNotNull(getOrdersResponse.getOrders(), "Список заказов пустой");
        //Поменять потом число заказов которые возвращаются!
        //проверяю что возвращает только 50 последних заказов
        assertTrue(getOrdersResponse.getOrders().size() <= 50, "Сервер должен вернуть не больше  50 последних заказов, а вернул: " + getOrdersResponse.getOrders().size());
        //отдельно сохраняем список заказов
        List<Order> orders = getOrdersResponse.getOrders();
        //будем сравнивать что по датам сортировано правильно
        for (int i = 0; i < orders.size() - 1; i++) {
            Instant current = Instant.parse(orders.get(i).getUpdatedAt());
            Instant next = Instant.parse(orders.get(i + 1).getUpdatedAt());

            assertTrue(
                    current.isBefore(next),
                    "Заказы не отсортированы по updatedAt"
            );
        }

    }


}
