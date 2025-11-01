package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.Dish;
import gr1.fpt.bambikitchen.model.dto.request.DishCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishUpdateRequest;
import gr1.fpt.bambikitchen.service.impl.DishService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/dish")
@CrossOrigin(origins = "*")
public class DishController {

    private final DishService dishService;

    @Operation(summary = "Sài chung cho create & update, nếu update thì gửi id, còn create thì ko cần", description = "Tạo món ăn mới kèm nguyên liệu và thông tin chi tiết, gửi kèm 1 map chứa Id Ingredient và số lượng. Account chỉ cần gửi Id, mấy field khác để trống")
    @PostMapping
    public ResponseEntity<Dish> save(@ModelAttribute DishCreateRequest request) throws IOException {
        System.out.println(request);
        return ResponseEntity.ok(dishService.saveMenu(request));
    }
    @PutMapping
    public ResponseEntity<Dish> update(@ModelAttribute DishUpdateRequest request) throws IOException {
        return ResponseEntity.ok(dishService.update(request));
    }
    @GetMapping("/toggle-public/{id}")
    public ResponseEntity<Boolean> toggleActive(@PathVariable int id) {
        return ResponseEntity.ok(dishService.togglePublic(id));
    }

    @GetMapping("/toggle-active/{id}")
    public ResponseEntity<Boolean> toggleInactive(@PathVariable int id) {
        return ResponseEntity.ok(dishService.toggleActive(id));
    }

    @PutMapping("/save-custom-dish")
    public void saveCustomDish(@RequestParam int id, @RequestParam boolean isPublic) {
        dishService.customToPreset(id, isPublic);
    }

    @GetMapping("{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable int id) {
        return ResponseEntity.ok(dishService.getDishById(id));
    }

    @Operation(summary = "Menu", description = "Cái endpoint này hiển thị bên menu, chỉ bao gồm những dish active và public")
    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAll());
    }

    @GetMapping("account/{accountId}")
    public ResponseEntity<List<Dish>> getDishesByAccountId(@PathVariable int accountId) {
        return ResponseEntity.ok(dishService.getDishedByAccount(accountId));}

    @Operation(summary = "Get Tất cả các dish", description = "Cái endpoint này hiển thị bên admin, bao gồm cả những dish không active và public")
    @GetMapping("get-all")
    public ResponseEntity<List<Dish>> getAllDishAdmin() {
        return ResponseEntity.ok(dishService.getAllDish());
    }
}
