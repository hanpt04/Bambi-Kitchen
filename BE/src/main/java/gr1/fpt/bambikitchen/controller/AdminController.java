package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.Dish;
import gr1.fpt.bambikitchen.model.Orders;
import gr1.fpt.bambikitchen.model.Payment;
import gr1.fpt.bambikitchen.service.IngredientService;
import gr1.fpt.bambikitchen.service.impl.DishService;
import gr1.fpt.bambikitchen.service.impl.OrderService;
import gr1.fpt.bambikitchen.service.impl.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService ordersService;
    @Autowired
    private DishService dishService;
    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/payments/total-revenue")
    public ResponseEntity<List<Payment>> getAll(){
        List<Payment> payments = paymentService.getAll();
        return ResponseEntity.ok(payments);
    }
    @GetMapping("/order")
    public ResponseEntity<List<Orders>> getAllOrder(){
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @GetMapping("/dishes/most-popular")
    public ResponseEntity<List<Dish>> getMostPopularDishes(){
        List<Dish> dishes = dishService.getTop5Dish();
        return ResponseEntity.ok(dishes);   }

    @GetMapping("/ingredients/low-stock")
    public ResponseEntity<List<?>> getLowStockIngredients(){
        return ResponseEntity.ok(ingredientService.getLowInventoryIngredients());
    }
}
