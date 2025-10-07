package gr1.fpt.bambikitchen.Config;

import gr1.fpt.bambikitchen.model.enums.ResponseStatus;
import gr1.fpt.bambikitchen.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    public record UserDTO (int id, String name, int age) {}
    @GetMapping("/demo")
    public ResponseEntity<WarpResponse<UserDTO>>  demo (@RequestBody UserDTO user) {
        UserDTO user1 = new  UserDTO(1, "John Doe", 30);
        UserDTO user2 = new  UserDTO(2, "Jane Smith", 25);
        List<UserDTO> users = List.of(user1, user2, user);
        WarpResponse<UserDTO> body = WarpResponse.of(ResponseStatus.NOT_FOUND, user);
         return ResponseEntity.status(body.getStatusCode()).body(body);

    }

    @Autowired
    DishService dishService;

    @DeleteMapping("demo")
    public void deleteRecipe(int DishId, int IngredientId)
    {
        dishService.deleteRecipeWithDishId(DishId);
    }

}
