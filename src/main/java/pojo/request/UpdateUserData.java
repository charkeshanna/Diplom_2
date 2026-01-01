package pojo.request;
import lombok.Setter;
@Setter
public class UpdateUserData {
    private String email;
    private String password;
    private String name;

    public UpdateUserData() {}
    public UpdateUserData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
