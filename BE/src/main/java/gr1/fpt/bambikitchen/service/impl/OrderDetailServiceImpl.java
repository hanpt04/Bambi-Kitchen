package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.OrderDetail;
import gr1.fpt.bambikitchen.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getAll() {
        return orderDetailRepository.findAll();
    }

    public List<OrderDetail> getByOrderId(int orderId) {
        return orderDetailRepository.findByOrders_Id(orderId);
    }

    public OrderDetail getById(int detailId){
        return orderDetailRepository.findById(detailId).orElseThrow(()-> new CustomException("Order detail not found", HttpStatus.NOT_FOUND));
    }
}
