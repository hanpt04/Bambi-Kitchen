package gr1.fpt.bambikitchen.model.dto.request;

import gr1.fpt.bambikitchen.model.enums.Unit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Data
public class IngredientCreateRequest {
    @NotBlank(message = "Name can not blank !")
    @Length(min = 10, message = "Name must greater than 10 chars")
    private String name;

    @NotNull(message = "Category id is required")
    private Integer categoryId;

    @NotNull(message = "Unit is required")
    private Unit unit;

    @Nullable
    MultipartFile file;
}
