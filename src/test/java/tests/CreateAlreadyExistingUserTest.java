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

public class CreateAlreadyExistingUserTest extends BaseTest{
    private String email;
    private String password;
    private String firstName;
    private String accessToken;
    CreateUserSteps createUserSteps;

    @BeforeEach
    public void setUp() {
        createUserSteps = new CreateUserSteps();
        //generate password and email
        email = DataGenerator.generateUserEmail();
        password = DataGenerator.generateUserPassword();
        firstName = email + "firstName";
        //шаг по созданию юзера
        UserRegistrationResponse userRegistrationResponse = createUserSteps.createNewUserReturnsSuccessAndResponse(email, password, firstName);
        //получаем accessToken
        accessToken = userRegistrationResponse.getAccessToken();
    }


    @AfterEach
    public void tearDown() {
        createUserSteps.removeCreatedUser(accessToken);
    }



    @Test
    //to update
    //еще не понятно по каким параметрам считает уже существующего -
    @DisplayName("Negative test to check that user with all data the same  cannot be created twice")
    public void createUserWithTheSameDataReturnsForbiddenResponse() {
        //создаю user передавая повторно те же данные
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(email, password, firstName);
        //отправляю запрос на endpoint и получаю ответ
        Response response = createUserSteps.createUser(userRegistrationRequest);
        //проверка статус кода
        createUserSteps.checkStatusCode(response, 403);
        //проверка тела сообщения
        createUserSteps.checkAfterAttemptToCreateAlreadyExistingUserReturnMessage(response);
    }

    @Test
    //to update
    //еще не понятно по каким параметрам считает уже существующего -
    @DisplayName("Negative test to check that user with only email the same and other data differentcannot be created twice")
    public void createUserWithAlreadyExistingEmailAndDifferentPasswordAndFirstNameReturnsForbiddenResponse() {
        //генерирую заново пароль
        password = DataGenerator.generateUserPassword();
        //создаю user передавая повторно те же данные
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(email, password, firstName + email);
        //отправляю запрос на endpoint и получаю ответ
        Response response = createUserSteps.createUser(userRegistrationRequest);
        //проверка статус кода
        createUserSteps.checkStatusCode(response, 403);
        //проверка тела сообщения
        createUserSteps.checkAfterAttemptToCreateAlreadyExistingUserReturnMessage(response);
    }
    @Test
    //to update
    //еще не понятно по каким параметрам считает уже существующего -
    @DisplayName("Negative test to check that user with only email the same and other data different cannot be created twice")
    public void createUserWithAlreadyExistingEmailAndFirstNameAndDifferentPasswordReturnsForbiddenResponse() {
        //генерирую заново пароль
        password = DataGenerator.generateUserPassword();
        //создаю user передавая повторно те же данные
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(email, password, firstName);
        //отправляю запрос на endpoint и получаю ответ
        Response response = createUserSteps.createUser(userRegistrationRequest);
        //проверка статус кода
        createUserSteps.checkStatusCode(response, 403);
        //проверка тела сообщения
        createUserSteps.checkAfterAttemptToCreateAlreadyExistingUserReturnMessage(response);
    }
}
