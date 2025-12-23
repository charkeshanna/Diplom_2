package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.request.UserRegistrationRequest;
import pojo.response.UserRegistrationResponse;
import steps.CreateUserSteps;
import utils.DataGenerator;

public class CreateUserTest extends BaseTest{
    private String email;
    private String password;
    CreateUserSteps createUserSteps;

    @BeforeEach
    public void setUp() {
        createUserSteps = new CreateUserSteps();
    }


    @Test
    //to update
    @DisplayName("Check correct response code and text after adding a courier with all fields filled")
    public void createUserReturnsValidResponse() {
        //генерирую логин и пароль
        email = DataGenerator.generateUserEmail();
        password = DataGenerator.generateUserPassword();
        //создаю user
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(email, password, "firstname" + email);
        //отправляю запрос на endpoint и получаю ответ
        UserRegistrationResponse userRegistrationResponse = createUserSteps.createUser(userRegistrationRequest);
        String accessToken = userRegistrationResponse.getAccessToken();
        String refreshToken = userRegistrationResponse.getRefreshToken();

        //проверяю ответ
        //createUserSteps.checkStatusCode(userRegistrationResponse, 200);
        //createCourierSteps.checkResponseValue(response, "ok", true);

    }
}
