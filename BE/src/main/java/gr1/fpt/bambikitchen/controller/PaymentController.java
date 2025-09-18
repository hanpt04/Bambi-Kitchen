package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.Factory.PaymentFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("api/payment")
public class PaymentController {

    @Autowired
    PaymentFactory paymentFactory;

    @GetMapping("/testpayment")
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
        }
        else
        {
            System.out.println("Thanh toan cancle");
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
}
