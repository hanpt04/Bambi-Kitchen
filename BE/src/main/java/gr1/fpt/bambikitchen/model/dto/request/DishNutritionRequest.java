package gr1.fpt.bambikitchen.model.dto.request;



import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishNutritionRequest {

    @NotBlank(message = "Tên tô không được để trống nha quý zị!")
    @Size(max = 100)
    private String name;

    @NotEmpty(message = "Phải có ít nhất 1 nguyên liệu chứ bro!")
    @Size(max = 20, message = "Tô nhiều quá, ăn không hết đâu!")
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
        private Double amount;

        @NotBlank
        @Pattern(regexp = "g|kg|ml|l|pcs", message = "Unit chỉ được: g, kg, ml, l, pcs")
        private String unit;

        @NotBlank
        @Pattern(regexp = "100g|1 pcs", message = "per chỉ được: 100g hoặc 1 pcs")
        private String per;

        @NotNull
        @PositiveOrZero
        private Double cal;

        @NotNull
        @PositiveOrZero
        private Double pro;

        @NotNull
        @PositiveOrZero
        private Double carb;

        @NotNull
        @PositiveOrZero
        private Double fat;

        @NotNull
        @PositiveOrZero
        private Double fiber;
    }
}