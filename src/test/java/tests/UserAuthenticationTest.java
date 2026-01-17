package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.request.UserAuthenticationRequest;
import pojo.response.UserRegistrationResponse;
import steps.CreateUserSteps;
import utils.DataGenerator;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserAuthenticationTest extends BaseTest{
    private boolean isUserCreated;
    private String email;
    private String password;
    private String firstName;
    private String accessToken;
    CreateUserSteps createUserSteps;

    @BeforeEach
    public void setUp() {
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
    public void deleteUser() {
        if(isUserCreated) {
            createUserSteps.removeCreatedUser(accessToken);
        }
    }

    @Test
    @DisplayName("Authentication with valid email and password")
    public void loginWithValidCredentialsReturnsSuccessResponse() {
        UserAuthenticationRequest userAuthenticationRequest = new UserAuthenticationRequest(email, password);
        Response response = createUserSteps.loginAsUser(userAuthenticationRequest);
        // проверю статус-код
        createUserSteps.checkStatusCode(response, 200);
        //проверю успешное сообщение
        createUserSteps.checkResponseValue(response, "success", true);
        //проверю что нужный email возвращает
        createUserSteps.checkResponseValue(response, "user.email", email);
        //проверю что accessToken
        String loginToken = response.jsonPath().getString("accessToken");
        assertNotNull(loginToken, "accessToken should not be null");
        assertFalse(loginToken.isEmpty(), "accessToken should not be empty");
    }

    @Test
    @DisplayName("Login with wrong login and password")
    public void loginWithWrongEmailAndPasswordReturnsUnathorized() {
        //сгенерим новую пару логин-пароль
        email = DataGenerator.generateUserEmail();
        password = DataGenerator.generateUserPassword();
        //что-то там создадим
        UserAuthenticationRequest userAuthenticationRequest = new UserAuthenticationRequest(email, password);
        Response response = createUserSteps.loginAsUser(userAuthenticationRequest);
        // проверю статус-код
        createUserSteps.checkStatusCode(response, 401);
        //проверю успешное сообщение
        createUserSteps.checkResponseValue(response, "success", false);
        //проверю что нужный текст сообщения возвращает
        createUserSteps.checkResponseValue(response, "message", "email or password are incorrect");
    }
}
