package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.request.UpdateUserData;
import pojo.request.UserAuthenticationRequest;
import pojo.response.UserRegistrationResponse;
import steps.CreateUserSteps;
import utils.DataGenerator;

public class UpdateUsersDataForNonAuthorizedUserTest extends BaseTest{
    private boolean isUserCreated;
    private String email;
    private String password;
    private String firstName;
    private String name;
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
    @DisplayName("Check that firstName of the user cannot be updated for non-Authorized user with wrong accessToken")
    public void updateNameOfNonAuthorizedUserWithWrongAccessTokenReturnsFalse() {
        //create new firstname
        name = firstName + "updated";
        //создадим объект c обновленными данными
        UpdateUserData updateUserData = new UpdateUserData();
        updateUserData.setName(name);
        //set wrong accessToken
        String accessTokenUpd = accessToken + "False";
        //отправим запрос сначала просто проверим респонс
        Response response = createUserSteps.updateDataForAuthorizedUser(accessTokenUpd, updateUserData);
        //проверим код ответа
        createUserSteps.checkStatusCode(response, 403);
        //проверим успешность
        createUserSteps.checkResponseValue(response, "success", false);
        //залогинимся и проверим что данные остались прежними
        UserAuthenticationRequest userAuthenticationRequest =  new UserAuthenticationRequest(email, password);
        Response responseAfterLogin = createUserSteps.loginAsUser(userAuthenticationRequest);
        //проверим код ответа
        createUserSteps.checkStatusCode(responseAfterLogin, 200);
        //проверим успешность
        createUserSteps.checkResponseValue(responseAfterLogin, "success", true);
        //проверим что  firstname старый
        createUserSteps.checkResponseValue(responseAfterLogin, "user.name", firstName);
    }

    @Test
    @DisplayName("Check that email of the user cannot be updated for non-Authorized user without in accessToken field")
    public void updateEmailForNonAuthorizedUserWithoutAccessTokenReturnsFalse() {
        //create new email
        String emailUpdated = DataGenerator.generateUserEmail();
        //создадим объект c обновленными данными
        UpdateUserData updateUserData = new UpdateUserData();
        updateUserData.setEmail(emailUpdated);
        //отправим запрос сначала просто проверим респонс
        Response response = createUserSteps.updateDataForNonAuthorizedUser(updateUserData);
        //проверим код ответа
        createUserSteps.checkStatusCode(response, 401);
        //проверим успешность
        createUserSteps.checkResponseValue(response, "success", false);
        //залогинимся с новым email и проверим что данные остались прежними
        createUserSteps.checkResponseValue(response, "message", "You should be authorised");
        UserAuthenticationRequest userAuthenticationRequest =  new UserAuthenticationRequest(emailUpdated, password);
        Response responseAfterLogin = createUserSteps.loginAsUser(userAuthenticationRequest);
        // проверю статус-код
        createUserSteps.checkStatusCode(responseAfterLogin, 401);
        //проверю успешное сообщение
        createUserSteps.checkResponseValue(responseAfterLogin, "success", false);
        //проверю что нужный текст сообщения возвращает
        createUserSteps.checkResponseValue(responseAfterLogin, "message", "email or password are incorrect");;
    }

    @Test
    @DisplayName("Password should not be updated for user with wrong accessToken")
    public void updatePasswordOfNonAuthorizedUserWithWrongAccessTokenReturnsFalse() {
        //create new password
        String passwordUpdated = DataGenerator.generateUserPassword();
        //создадим объект c обновленными данными
        UpdateUserData updateUserData = new UpdateUserData();
        updateUserData.setPassword(passwordUpdated);
        //set wrong accessToken
        String accessTokenUpdated = accessToken + "False";
        //отправим запрос на обновление данных с неправильным токеном
        Response response = createUserSteps.updateDataForAuthorizedUser(accessTokenUpdated, updateUserData);
        //проверим код ответа
        createUserSteps.checkStatusCode(response, 403);
        //проверим успешность
        createUserSteps.checkResponseValue(response, "success", false);
        //проверим что нельзя залогиниться с неверным паролем
        UserAuthenticationRequest userAuthenticationRequest =  new UserAuthenticationRequest(email, passwordUpdated);
        Response responseAfterLogin = createUserSteps.loginAsUser(userAuthenticationRequest);
        //проверим код ответа
        createUserSteps.checkStatusCode(responseAfterLogin, 401);
        //проверим успешность
        createUserSteps.checkResponseValue(responseAfterLogin, "success", false);
        //проверю что нужный текст сообщения возвращает
        createUserSteps.checkResponseValue(responseAfterLogin, "message", "email or password are incorrect");
    }

}
