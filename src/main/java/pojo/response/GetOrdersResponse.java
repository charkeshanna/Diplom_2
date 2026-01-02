package pojo.response;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetOrdersResponse {
    private boolean success;
    private List<Order> orders;
    private int total;
    private int totalToday;
}
