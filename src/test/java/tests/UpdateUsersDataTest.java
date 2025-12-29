package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.request.UpdateUserData;
import pojo.request.UserRegistrationRequest;
import pojo.response.UserRegistrationResponse;
import steps.CreateUserSteps;
import utils.DataGenerator;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateUsersDataTest extends BaseTest{
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
    @DisplayName("Check that firstName of the authorized user can be updated")
    public void updateFirstNameOfAuthorizedUserReturnsSuccess() {
        //create new firstname
        firstName = email + "updated";
        //создадим объект c обновленными данными
        UpdateUserData updateUserData = new UpdateUserData();
        updateUserData.setFirstName(firstName);
        //отправим запрос сначала просто проверим респонс
        Response response = createUserSteps.updateUsersData(accessToken, updateUserData);
        //проверим код ответа
        createUserSteps.checkStatusCode(response, 200);
        //проверим успешность
        createUserSteps.checkResponseValue(response, "success", true);
        //проверим что обновился firstname
        createUserSteps.checkResponseValue(response, "user.name", firstName);
    }

    @Test
    @DisplayName("Check that email can be updated for authorized user")
    public void updateEmailOfAuthorizedUserReturnsSuccess() {
        //create new email
        email = DataGenerator.generateUserEmail();
        //создадим объект c обновленными данными

        UpdateUserData updateUserData = new UpdateUserData();
        updateUserData.setEmail(email);
        //отправим запрос сначала просто проверим респонс
        Response response = createUserSteps.updateUsersData(accessToken, updateUserData);
        //проверим код ответа
        createUserSteps.checkStatusCode(response, 200);
        //проверим успешность
        createUserSteps.checkResponseValue(response, "success", true);
        //проверим что обновился firstname
        createUserSteps.checkResponseValue(response, "user.email", email);
    }
}
