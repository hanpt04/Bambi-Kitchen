package gr1.fpt.bambikitchen.schedule;

import gr1.fpt.bambikitchen.event.EventListenerSystem;
import gr1.fpt.bambikitchen.model.InventoryOrder;
import gr1.fpt.bambikitchen.security.OTP.OTP;
import gr1.fpt.bambikitchen.security.OTP.OTPRepository;
import gr1.fpt.bambikitchen.service.IngredientService;
import gr1.fpt.bambikitchen.service.impl.InventoryOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class Scheduler  {

    @Autowired
    OTPRepository otpRepository;
    @Autowired
    InventoryOrderService inventoryOrderService;
    @Autowired
    IngredientService ingredientService;
    @Autowired
    ApplicationEventPublisher eventPublisher;


    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void terminateExpiredOTPs() {
        List<OTP> expiredOTPs = otpRepository.findAllByCreatedAtBefore(LocalDateTime.now().minusMinutes(5));
        for (OTP otp : expiredOTPs) {
            otp.setExpired(true);
            otpRepository.save(otp);
        }
    }

    //hàm schedule để chạy nếu sau 5' vẫn còn giữ chỗ chưa phản hôì thì sẽ cập nhật lại reserve và available quantity
    @Scheduled(fixedRate = 60000)
    public void reserve(){
        List<InventoryOrder> inventoryOrder = inventoryOrderService.findAll();
        for(InventoryOrder order : inventoryOrder) {
            if(order.getReceivedAt().before(Timestamp.valueOf(LocalDateTime.now().minusMinutes(3)))) {
                ingredientService.resetReserve(order.getOrderId());
                eventPublisher.publishEvent(new EventListenerSystem.PaymentCancelEvent(order.getOrderId()));
            }
        }
    }
}
