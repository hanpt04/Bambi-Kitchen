package gr1.fpt.bambikitchen.Config;

import gr1.fpt.bambikitchen.model.enums.ResponseStatus;
import gr1.fpt.bambikitchen.service.DishService;
import gr1.fpt.bambikitchen.service.IngredientService;
import gr1.fpt.bambikitchen.service.impl.IngredientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/test")
public class TestController {

    @Autowired
    private IngredientService ingredientService;

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

    @DeleteMapping("/demo/delete-recipe")
    public void deleteRecipe(int DishId, int IngredientId)
    {
        dishService.deleteRecipeWithDishId(DishId);
    }

    @GetMapping("order/confirm/{id}")
    public String confirm(@PathVariable int id) {
        ingredientService.minusInventory(id);
        return "confirm";
    }

    @GetMapping("/order/cancel/{orderId}")
    public String canceled(@PathVariable int orderId) {
        ingredientService.resetReserve(orderId);
        return "canceled";
    }
}
