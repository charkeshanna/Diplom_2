package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.request.UserRegistrationRequest;
import steps.CreateUserSteps;
import utils.DataGenerator;

public class CreateUserWithoutRequiredFieldsTest extends BaseTest{
    private String email;
    private String password;
    private String accessToken;
    CreateUserSteps createUserSteps;

    @BeforeEach
    public void setUp() {
        createUserSteps = new CreateUserSteps();
    }


    @Test
    //to update
    @DisplayName("Create user without email")
    public void createUserWithoutEmailForbidden() {
        //генерирую пароль
        password = DataGenerator.generateUserPassword();
        //создаю user
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(null, password, "firstname" + email);
        //отправляю запрос на endpoint и получаю ответ
        Response response = createUserSteps.createUser(userRegistrationRequest);
        //проверка статус кода
        createUserSteps.checkStatusCode(response, 403);
        //проверка тела сообщения
        createUserSteps.checkNegativeResponseValueForSkippedRequiredFieldsCreateRequest(response);
    }

    @Test
    //to update
    @DisplayName("Create user without email")
    public void createUserWithoutPasswordForbidden() {
        //генерирую email
        email = DataGenerator.generateUserEmail();
        //создаю user
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(email, null, "firstname" + email);
        //отправляю запрос на endpoint и получаю ответ
        Response response = createUserSteps.createUser(userRegistrationRequest);
        //проверка статус кода
        createUserSteps.checkStatusCode(response, 403);
        //проверка тела сообщения
        createUserSteps.checkNegativeResponseValueForSkippedRequiredFieldsCreateRequest(response);
    }

    @Test
    //to update
    @DisplayName("Create user without email")
    public void createUserWithoutFirstNameForbidden() {
        //генерирую email and password
        email = DataGenerator.generateUserEmail();
        password = DataGenerator.generateUserPassword();
        //создаю user
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(email, password, null);
        //отправляю запрос на endpoint и получаю ответ
        Response response = createUserSteps.createUser(userRegistrationRequest);
        //проверка статус кода
        createUserSteps.checkStatusCode(response, 403);
        //проверка тела сообщения
        createUserSteps.checkNegativeResponseValueForSkippedRequiredFieldsCreateRequest(response);
    }

}
