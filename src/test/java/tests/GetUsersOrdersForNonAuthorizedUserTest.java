package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import steps.OrdersSteps;

public class GetUsersOrdersForNonAuthorizedUserTest extends BaseTest{
    OrdersSteps ordersSteps;

    @Test
    public void getUsersOrdersRequestForNonAuthorizedUserReturnsFalseResponse() {
        ordersSteps = new OrdersSteps();
        Response response = ordersSteps.sendGetOrdersRequestForNonAuthorizedUser();
        //проверим код
        ordersSteps.checkStatusCode(response, 401);
        //проверим текст
        ordersSteps.checkResponseValue(response, "success", false);
        ordersSteps.checkResponseValue(response, "message", "You should be authorised");
    }

}
