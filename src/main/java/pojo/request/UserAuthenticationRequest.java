package pojo.request;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class UserAuthenticationRequest {
    private String email;
    private String password;

    public UserAuthenticationRequest() {}
    public UserAuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
