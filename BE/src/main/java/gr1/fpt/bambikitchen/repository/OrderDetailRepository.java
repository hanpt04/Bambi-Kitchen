package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.OrderDetail;
import gr1.fpt.bambikitchen.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrders_Id(int orders_id);
}
