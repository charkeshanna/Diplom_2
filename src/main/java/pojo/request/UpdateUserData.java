package pojo.request;
import lombok.Setter;
@Setter
public class UpdateUserData {
    private String email;
    private String password;
    private String firstName;

    public UpdateUserData() {}
}
