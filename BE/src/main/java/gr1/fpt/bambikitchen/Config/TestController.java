package gr1.fpt.bambikitchen.Config;

import gr1.fpt.bambikitchen.model.enums.ResponseStatus;
import gr1.fpt.bambikitchen.service.DishService;
import gr1.fpt.bambikitchen.service.impl.IngredientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class TestController {

    @Autowired
    private IngredientServiceImpl ingredientServiceImpl;

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

    @GetMapping("/confirm/{id}")
    public String confirm(@PathVariable int id) {
        ingredientServiceImpl.minusInventory(id);
        return "confirm";
    }
}
