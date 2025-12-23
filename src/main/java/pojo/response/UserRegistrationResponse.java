package pojo.response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationResponse {
    //define all the fields of the response
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private UserDataResponse userData;
}
