package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.Factory.PaymentFactory;
import gr1.fpt.bambikitchen.service.IngredientService;
import gr1.fpt.bambikitchen.service.impl.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    PaymentFactory paymentFactory;
    @Autowired
    PaymentService paymentService;
    @Autowired
    IngredientService ingredientService;

    @GetMapping("/test-payment")
    public String testPayment(@RequestParam String paymentMethodName) throws Exception {

        paymentFactory.getPaymentMethod(paymentMethodName).pay(20);
      return  paymentFactory.getPaymentMethod(paymentMethodName).createPaymentRequest(198000,1);

    }


    @GetMapping("momo-return")
    public ResponseEntity<Map<String, Object>> handleMomoReturn(@RequestParam Map<String, String> params, HttpServletResponse httpResponse) throws Exception {
        Map<String, Object> response = new HashMap<>(params);

        // Trích xuất các thuộc tính từ params
        String partnerCode = params.get("partnerCode");
        String orderId = params.get("orderId");
        String requestId = params.get("requestId");
        String amount = params.get("amount");
        String orderInfo = params.get("orderInfo");
        String orderType = params.get("orderType");
        String transId = params.get("transId");
        String resultCode = params.get("resultCode");
        String message = params.get("message");
        String payType = params.get("payType");
        String responseTime = params.get("responseTime");
        String extraData = params.get("extraData");
        String signature = params.get("signature");

        // Lấy paymentId từ vnp_OrderInfo
        String[] parts = orderInfo.split(":");
        String paymentId = parts[1];

        System.out.println("paymentId: " + paymentId);

        if (message.equals("Successful.")) {
            System.out.println("thanh toan thanh cong");
            // nếu sucess thì set lại state là confirm và gửi mail và lock kho
            paymentService.paymentSucess( Integer.parseInt(paymentId), "MOMO", transId);

        }
        else
        {
            System.out.println("Thanh toan cancle");
            paymentService.paymentFail( Integer.parseInt(paymentId), "MOMO");
        }

//        String redirectUrl = "http://localhost:3000/order/status?"
//                + "orderId=" + extraData
//                + "&amount=" + amount
//                + "&status=" + (message.equals("Successful.") ? "SUCCESS" : "FAILED")
//                + "&method=MOMO";
//
//        httpResponse.sendRedirect(redirectUrl);

        //o day redirect ve FE voi parameter de hien thi hoa don
        return ResponseEntity.ok(response);
    }


    @GetMapping("/vnpay-return")
    public String handleVnPayReturn(@RequestParam Map<String, String> params, HttpServletResponse httpResponse) throws Exception {
        Map<String, Object> response = new HashMap<>();

        // Lấy các thông tin quan trọng từ VNPay
        String vnp_Amount = params.get("vnp_Amount");
        String vnp_BankCode = params.get("vnp_BankCode");
        String vnp_BankTranNo = params.get("vnp_BankTranNo");
        String vnp_CardType = params.get("vnp_CardType");
        String vnp_OrderInfo = params.get("vnp_OrderInfo");
        String vnp_PayDate = params.get("vnp_PayDate");
        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        String vnp_TransactionNo = params.get("vnp_TransactionNo");
        String vnp_TransactionStatus = params.get("vnp_TransactionStatus");
        String vnp_TxnRef = params.get("vnp_TxnRef");
        String vnp_SecureHash = params.get("vnp_SecureHash");

        // Kiểm tra mã phản hồi để xác định thanh toán có thành công hay không
        boolean isSuccess = "00".equals(vnp_ResponseCode) && "00".equals(vnp_TransactionStatus);

        // Tạo dữ liệu để gửi về frontend
        response.put("orderId", vnp_TxnRef);
        response.put("amount", Integer.parseInt(vnp_Amount) / 100);
        response.put("bankCode", vnp_BankCode);
        response.put("bankTransactionNo", vnp_BankTranNo);
        response.put("cardType", vnp_CardType);
        response.put("orderInfo", vnp_OrderInfo);
        response.put("payDate", vnp_PayDate);
        response.put("transactionNo", vnp_TransactionNo);
        response.put("status", isSuccess ? "SUCCESS" : "FAILED");

        // Lấy paymentId từ vnp_OrderInfo
        String[] parts = vnp_OrderInfo.split(":");
        String paymentId = parts[1];

        System.out.println("paymentId: " + paymentId);

        // Cập nhật trạng thái thanh toán vào database + gửi hoóa đơn vào mail + tạo notification + dissable url
        if (isSuccess) {
            System.out.println("thanh toan thanh cong VNPAY");
            paymentService.paymentSucess( Integer.parseInt(paymentId), "VNPAY", vnp_TransactionNo);

        } else {
            System.out.println("Thanh toan cancle");
            paymentService.paymentFail( Integer.parseInt(paymentId), "VNPAY");
        }

//        Payment payment = paymentService.getPaymentById(Integer.parseInt(paymentId));
//        // Redirect về frontend với thông tin giao dịch qua query parameters
//        String redirectUrl = "http://localhost:3000/order/status?"
//                + "orderId=" + payment.getOrderId()
//                + "&amount=" + (Integer.parseInt(vnp_Amount) / 100)
//                + "&status=" + (isSuccess ? "SUCCESS" : "FAILED")
//                + "&method=VNPAY";

//        httpResponse.sendRedirect(redirectUrl);
        return response.toString();
    }
}
