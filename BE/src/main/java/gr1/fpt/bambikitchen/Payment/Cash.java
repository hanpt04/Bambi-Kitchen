package gr1.fpt.bambikitchen.Payment;

import gr1.fpt.bambikitchen.service.impl.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("CASH")
@RequiredArgsConstructor
public class Cash implements PaymentMethod {

    private final PaymentService paymentService;

    @Override
    public void pay(double amount) {

    }
    String generateTransactionId() {
        return "CASH-" + System.currentTimeMillis() ;
    }

    @Override
    public String createPaymentRequest(long amount, int orderIdreal , int paymentId) throws Exception {
        paymentService.paymentSucess( paymentId, "CASH", generateTransactionId() );
        return " Pay with Cash on Delivery ";
    }
}
