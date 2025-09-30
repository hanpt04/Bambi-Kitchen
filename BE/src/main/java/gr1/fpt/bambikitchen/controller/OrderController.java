package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.dto.request.MakeOrderRequest;
import gr1.fpt.bambikitchen.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping
    public void createOrder(@RequestBody MakeOrderRequest makeOrderRequest) {
        System.out.println("Received order request: " + makeOrderRequest);
        orderService.makeOrder( makeOrderRequest);
    }
}
