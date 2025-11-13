package gr1.fpt.bambikitchen.model.dto.response;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DishNutritionResponse<T> {
    private int dishId;
    private T response; // response về là kiểu Json
}
