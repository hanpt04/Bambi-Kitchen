package gr1.fpt.bambikitchen.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishNutritionRequest {

    private int id;

    @Size(max = 100)
    private String name;

    private List<Ingredient> ingredients;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ingredient {

        @NotBlank
        @Size(max = 50)
        private String name;

        @NotNull
        @Positive(message = "Số lượng phải > 0 nha!")
        private Integer amount;

        @NotBlank
        @Pattern(regexp = "g|kg|ml|l|pcs", message = "Unit chỉ được: g, kg, ml, l, pcs")
        private String unit;

    }
}