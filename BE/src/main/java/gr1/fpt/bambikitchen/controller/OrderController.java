package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.Orders;
import gr1.fpt.bambikitchen.model.dto.request.MakeOrderRequest;
import gr1.fpt.bambikitchen.model.dto.request.OrderUpdateDto;
import gr1.fpt.bambikitchen.model.dto.response.FeedbackDto;
import gr1.fpt.bambikitchen.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping
    public String createOrder(@RequestBody MakeOrderRequest makeOrderRequest) throws Exception {
        System.out.println("Received order request: " + makeOrderRequest);
        return orderService.makeOrder( makeOrderRequest);

    }

   @PutMapping
    public ResponseEntity<Orders> updateOrder(@RequestBody OrderUpdateDto dto) {
        return ResponseEntity.ok(orderService.feedbackOrder(dto));
   }

   @PutMapping("/feedback")
   public ResponseEntity<Orders> feedback(@RequestBody OrderUpdateDto request) {
       return ResponseEntity.ok(orderService.feedbackOrder(request));
   }

   @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());}

    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable int id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Orders>> getOrdersByUserId(@PathVariable int userId) {
        return ResponseEntity.ok(orderService.getOrderByUser(userId));}

    @GetMapping("getFeedbacks")
    public ResponseEntity<List<FeedbackDto>> getFeedbacks() {
        return ResponseEntity.ok(orderService.getFeedback());
    }
}
