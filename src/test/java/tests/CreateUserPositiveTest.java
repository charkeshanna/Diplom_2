package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.request.UserRegistrationRequest;
import pojo.response.UserRegistrationResponse;
import steps.CreateUserSteps;
import utils.DataGenerator;

import static io.restassured.RestAssured.given;

public class CreateUserPositiveTest extends BaseTest{
    private String email;
    private String password;
    private String accessToken;
    CreateUserSteps createUserSteps;

    @BeforeEach
    public void setUp() {
        createUserSteps = new CreateUserSteps();
    }


    @AfterEach
    public void tearDown() {
        createUserSteps.removeCreatedUser(accessToken);
        }



    @Test
    //to update
    @DisplayName("Check correct response code and text after adding new user with all fields filled")
    public void createUserReturnsValidResponse() {
        //генерирую логин и пароль
        email = DataGenerator.generateUserEmail();
        password = DataGenerator.generateUserPassword();
        //создаю user
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(email, password, "firstname" + email);
        //отправляю запрос на endpoint и получаю ответ
        Response response = createUserSteps.createUser(userRegistrationRequest);
        //проверка статус кода
        createUserSteps.checkStatusCode(response, 200);
        //проверка тела сообщения
        createUserSteps.checkResponseValue(response, "success", true);
        //десериализация ответа чтобы получить токен
        UserRegistrationResponse userRegistrationResponse = createUserSteps.userRegistrationResponse(response);
        accessToken = userRegistrationResponse.getAccessToken();
    }
}
