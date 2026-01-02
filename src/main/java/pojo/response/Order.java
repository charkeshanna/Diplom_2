package pojo.response;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Order {
    private String _id;
    private List<String> ingredients;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private int number;

}
