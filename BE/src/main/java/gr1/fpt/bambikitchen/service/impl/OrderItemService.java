package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.model.OrderItem;
import gr1.fpt.bambikitchen.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }
    public void save (OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }
    public List<OrderItem> findByOrderId(int id) {
        return orderItemRepository.findByOrder_OrderId(id);
    }
    public void deleteAllByOrderId(int orderId){
        orderItemRepository.deleteAllByOrOrder_OrderId(orderId);
    }

}
