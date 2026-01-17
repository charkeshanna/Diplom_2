package pojo.request;

import java.util.List;

public class CreateOrderRequest {
    private List<String> ingredients;

    public CreateOrderRequest() {}
    public CreateOrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public List<String> getIngredients() {
        return ingredients;
    }

}
