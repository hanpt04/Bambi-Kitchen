package gr1.fpt.bambikitchen.model.dto.request;

import gr1.fpt.bambikitchen.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Data
public class IngredientDtoRequest {
    private Ingredient ingredient;
    private MultipartFile file;
}
