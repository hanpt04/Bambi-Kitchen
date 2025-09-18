package gr1.fpt.bambikitchen.Factory;

import gr1.fpt.bambikitchen.Payment.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentFactory {
    @Autowired
    private Map<String, PaymentMethod> paymentMethods;

    public PaymentMethod getPaymentMethod(String paymentMethodName) {
        return paymentMethods.get(paymentMethodName);
    }
}
