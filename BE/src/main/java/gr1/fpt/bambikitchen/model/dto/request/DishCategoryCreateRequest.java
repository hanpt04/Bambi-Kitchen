package gr1.fpt.bambikitchen.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

@Data
public class DishCategoryCreateRequest {
    @NotBlank(message = "Name must not be blank")
    @Nationalized
    private String name;

    @Nationalized
    private String description;
}
