package gr1.fpt.bambikitchen.model.dto.request;

import gr1.fpt.bambikitchen.model.Dish;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class DishDtoRequest {
    Dish dish;
    MultipartFile file;
}
