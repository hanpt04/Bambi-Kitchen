package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.Dish;
import gr1.fpt.bambikitchen.model.dto.request.DishCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishUpdateRequest;
import gr1.fpt.bambikitchen.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
}
