package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.OrderDetail;
import gr1.fpt.bambikitchen.service.impl.OrderDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order-detail")
public class OrderDetailController {
    @Autowired
    private OrderDetailServiceImpl orderDetailService;

    @GetMapping
    public ResponseEntity<List<OrderDetail>> getAllOrderDetails() {
        return ResponseEntity.ok(orderDetailService.getAll());
    }

    @GetMapping("/by-order/{orderId}")
    public ResponseEntity<List<OrderDetail>> findByOrder(@PathVariable int orderId) {
        return ResponseEntity.ok(orderDetailService.getByOrderId(orderId));
    }

    @GetMapping("/{detailId}")
    public ResponseEntity<OrderDetail> getOrderDetailById(@PathVariable int detailId){
        return ResponseEntity.ok(orderDetailService.getById(detailId));}

}
