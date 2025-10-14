package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.Orders;
import gr1.fpt.bambikitchen.model.dto.request.MakeOrderRequest;
import gr1.fpt.bambikitchen.model.dto.request.OrderUpdateDto;
import gr1.fpt.bambikitchen.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
