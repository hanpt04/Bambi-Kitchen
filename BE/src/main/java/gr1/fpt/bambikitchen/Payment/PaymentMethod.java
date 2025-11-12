package gr1.fpt.bambikitchen.Payment;

public interface PaymentMethod {
    void pay (double amount);
    String createPaymentRequest(long amount, int orderIdreal , int paymentId) throws Exception;
}
