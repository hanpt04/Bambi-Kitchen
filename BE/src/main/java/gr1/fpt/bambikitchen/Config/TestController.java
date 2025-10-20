package gr1.fpt.bambikitchen.Config;

import gr1.fpt.bambikitchen.model.enums.ResponseStatus;
import gr1.fpt.bambikitchen.service.impl.DishService;
import gr1.fpt.bambikitchen.service.IngredientService;
import gr1.fpt.bambikitchen.service.impl.IngredientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/test")
public class TestController {

    @Autowired
    private IngredientServiceImpl  ingredientServiceImpl;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    DishService dishService;

    @GetMapping("testinventory")
    public boolean testinventory () {
        Map< Integer, Double> ingredientMap = new HashMap<>();
        int orderId =100;
        ingredientMap.put(17, 400.0);
        ingredientMap.put(2, 0.0);
        ingredientMap.put(3, 400.0);
        ingredientMap.put(25, 260.0);
        ingredientMap.put(11, 200.0);
        ingredientMap.put(12, 100.0);
        ingredientMap.put(28, 195.0);
        ingredientMap.put(15, 200.0);
        return ingredientServiceImpl.checkAvailable( ingredientMap, orderId);
    }

    public record UserDTO (int id, String name, int age) {}
    @GetMapping("/demo")
    public ResponseEntity<WarpResponse<UserDTO>>  demo (@RequestBody UserDTO user) {
        UserDTO user1 = new  UserDTO(1, "John Doe", 30);
        UserDTO user2 = new  UserDTO(2, "Jane Smith", 25);
        List<UserDTO> users = List.of(user1, user2, user);
        WarpResponse<UserDTO> body = WarpResponse.of(ResponseStatus.NOT_FOUND, user);
         return ResponseEntity.status(body.getStatusCode()).body(body);

    }

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
