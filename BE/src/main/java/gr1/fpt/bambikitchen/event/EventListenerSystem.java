package gr1.fpt.bambikitchen.event;

import gr1.fpt.bambikitchen.cloudinary.CloudinaryService;
import gr1.fpt.bambikitchen.model.*;
import gr1.fpt.bambikitchen.model.dto.request.DishDtoRequest;
import gr1.fpt.bambikitchen.model.dto.request.IngredientDtoRequest;
import gr1.fpt.bambikitchen.model.enums.OrderStatus;
import gr1.fpt.bambikitchen.repository.DishRepository;
import gr1.fpt.bambikitchen.repository.IngredientRepository;
import gr1.fpt.bambikitchen.repository.OrderRepository;
import gr1.fpt.bambikitchen.repository.PaymentRepository;
import gr1.fpt.bambikitchen.service.IngredientService;
import gr1.fpt.bambikitchen.service.impl.InventoryOrderService;
import gr1.fpt.bambikitchen.service.impl.OrderItemService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class EventListenerSystem {
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    IngredientService ingredientService;
    @Autowired
    private InventoryOrderService inventoryOrderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private JavaMailSender mailSender;


    //update img url sau khi hoàn tất tạo ingredient
    @EventListener
    @Async
    public void handleIngredientCreatedEvent(IngredientDtoRequest dto) throws IOException {
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredient().getId()).orElse(null);
        if (ingredient != null) {
            if (ingredient.getPublicId() == null) {
                try {
                    uploadAndSave(ingredient, dto.getFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                //xóa trước rồi mới lưu mới
                Map result = cloudinaryService.deleteImage(ingredient.getPublicId());
                //nếu xóa thành công thì lưu ảnh mới upload lên
                if (result.get("result").equals("ok")) {
                    uploadAndSave(ingredient, dto.getFile());
                }
            }
        }
    }

    @EventListener
    @Async
    public void handleDishCreate(DishDtoRequest dto) throws IOException {
        Dish dish = dishRepository.findById(dto.getDish().getId()).orElse(null);
        if (dto != null) {
            if (dish.getPublicId() == null) {
                try {
                    uploadAndSaveDish(dish, dto.getFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Map result = cloudinaryService.deleteImage(dish.getPublicId());
                if (result.get("result").equals("ok")) {
                    uploadAndSaveDish(dish, dto.getFile());
                }
            }
        }
    }

    //tách hàm cho gọn
    private void uploadAndSave(Ingredient ingredient, MultipartFile file) throws IOException {
        Map<String, String> map = cloudinaryService.uploadImg(file);
        ingredient.setImgUrl(map.get("secure_url"));
        ingredient.setPublicId(map.get("public_id"));
        ingredientRepository.save(ingredient);
    }

    private void uploadAndSaveDish(Dish dish, MultipartFile file) throws IOException {
        Map<String, String> map = cloudinaryService.uploadImg(file);
        dish.setImageUrl(map.get("secure_url"));
        dish.setPublicId(map.get("public_id"));
        dishRepository.save(dish);
    }

    public static record PaymentCancelEvent(int paymentId) {
    }

    @EventListener
    @Async
    public void canclePaymentAndOrderEvent(PaymentCancelEvent paymentCancelEvent) {
        Payment payment = paymentRepository.findById(paymentCancelEvent.paymentId).orElse(null);
        if (payment != null) {
            payment.setStatus("CANCELLED");
            payment.setNote("Payment timeout");
            paymentRepository.save(payment);
        }
        Orders order = orderRepository.findById(paymentCancelEvent.paymentId).orElse(null);
        if (order != null) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setNote("Order cancelled due to payment timeout");
            orderRepository.save(order);
        }
    }

    public static record CreateItemAndInventory(int ingredientId, double quantity, int orderId) {
    }

    @EventListener
    @Async
    public void createItemAndInventory(CreateItemAndInventory createItemAndInventory) {
        OrderItem orderItem = new OrderItem();
        orderItem.setIngredientId(createItemAndInventory.ingredientId);
        orderItem.setQuantity(createItemAndInventory.quantity);
        ingredientService.saveOrder(createItemAndInventory.orderId);
        InventoryOrder inventoryOrder = inventoryOrderService.findByOrderId(createItemAndInventory.orderId);
        orderItem.setOrder(inventoryOrder);
        orderItemService.save(orderItem);
    }

    public static record SendOTPEvent(String email, String otp) {
    }

    @EventListener
    @Async
    public void sendOTP(SendOTPEvent sendOTPEvent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(sendOTPEvent.email);
            helper.setSubject("🔑 Your OTP Code (valid 5 minutes)");

            // HTML body
            String htmlContent = """
                    <div style="font-family: Arial, sans-serif; max-width: 480px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;">
                      <h2 style="color: #2c3e50; text-align: center;">🔐 One-Time Password</h2>
                      <p style="font-size: 16px; color: #555;">
                        Please use the following OTP to complete your action:
                      </p>
                      <div style="text-align: center; margin: 20px 0;">
                        <span style="display: inline-block; font-size: 28px; font-weight: bold; letter-spacing: 6px; color: #e74c3c; padding: 10px 20px; border: 2px dashed #e74c3c; border-radius: 8px;">
                          %s
                        </span>
                      </div>
                      <p style="font-size: 14px; color: #888; text-align: center;">
                        This code is valid for <strong>5 minutes</strong>.<br/>
                        If you didn’t request it, please ignore this email.
                      </p>
                    </div>
                    """.formatted(sendOTPEvent.otp);

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    /**
     *
     * @param email
     * @param dishes
     */
    public record SendOrderEvent(String email, List<DishInfo> dishes) {
    }

    @Builder
    public record DishInfo(
            String name,
            Integer price,
            Map<String, Integer> ingredients
    ) {
    }

    @EventListener
    @Async
    public void sendOrder(SendOrderEvent event) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(event.email);
        helper.setSubject("🔑 Your Bill");


        String htmlContent = buildBillHtml(event.dishes);

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    // a helper function to build the HTML content for the bill
    public String buildBillHtml(List<DishInfo> dishes) {
        StringBuilder html = new StringBuilder();

        html.append("""
                    <html>
                    <head>
                        <style>
                            body {
                                font-family: 'Segoe UI', sans-serif;
                                margin: 20px;
                                color: #333;
                                background-color: #fff;
                            }
                            .container {
                                max-width: 650px;
                                margin: auto;
                                border: 1px solid #f0f0f0;
                                border-radius: 12px;
                                box-shadow: 0 2px 10px rgba(0,0,0,0.05);
                                padding: 25px;
                                background: #fff;
                            }
                            h2 {
                                text-align: center;
                                color: #ff7a00;
                                margin-bottom: 30px;
                            }
                            .dish-card {
                                border: 1px solid #ffe3cc;
                                background: #fff7f0;
                                padding: 15px 20px;
                                border-radius: 10px;
                                margin-bottom: 20px;
                            }
                            .dish-name {
                                font-weight: bold;
                                font-size: 1.05rem;
                                margin-bottom: 10px;
                            }
                            .price-tag {
                                color: #ff7a00;
                                font-style: italic;
                                margin-bottom: 10px;
                                display: block;
                            }
                            ul {
                                list-style-type: none;
                                padding-left: 0;
                                margin-left: 0;
                            }
                            ul li {
                                display: flex;
                                justify-content: space-between;
                                padding: 3px 0;
                                border-bottom: 1px dashed rgba(255,122,0,0.3);
                                font-size: 0.95em;
                            }
                            .total {
                                text-align: right;
                                font-weight: bold;
                                padding: 10px;
                                margin-top: 25px;
                                border-radius: 8px;
                                background: #ff7a00;
                                color: white;
                                font-size: 1.05rem;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <h2>🧾 Hóa đơn nguyên liệu</h2>
                """);


        int grandTotal = 0;
        int dishIndex = 1;

        for (DishInfo dish : dishes) {
            html.append(String.format("<h3>%d. %s</h3>\n", dishIndex++, dish.name()));

            // show base price
            html.append(String.format("""
                        <p class="dish-price">Giá gốc món: <b>%,d₫</b></p>
                        <ul>
                    """, dish.price()));

            for (Map.Entry<String, Integer> ing : dish.ingredients().entrySet()) {
                html.append(String.format("""
                            <li>+ %s <span class="price">(Số lượng: %d)</span></li>
                        """, ing.getKey(), ing.getValue()));
            }
            grandTotal += dish.price();
        }

        html.append(String.format("""
                    <p class="total">Tổng chi phí tất cả món:
                        <span style="color:#4CAF50">%,d₫</span>
                    </p>
                        </div>
                    </body>
                    </html>
                """, grandTotal));

        return html.toString();
    }

    public record SendResetPasswordMessageEvent(String email) {
    }

    @EventListener
    @Async
    public void sendResetPasswordMessage(SendResetPasswordMessageEvent event) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(event.email);
        helper.setSubject("🔑 Your Password has been reset");

        String htmlContent = """
                 <div style="font-family: Arial, sans-serif; max-width: 480px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;">
                   <h2 style="color: #2c3e50; text-align: center;">🔑 Reset Your Password</h2>
                   <p style="font-size: 16px; color: #555;">
                     We received a request to reset the password for your account. If it was you, please ignore this email.
                   </p>
                   <h3 style="color: red; text-align: center;">But if wasn't you, click the button below to prevent your password from changing</h3>
                   <div style="text-align: center; margin: 20px 30px;">
                     <a href="%s" style="display: inline-block; background-color: #3498db; color: #fff; text-decoration: none;\s
                         font-size: 16px; font-weight: bold; padding: 12px 24px; border-radius: 6px;">
                       Reset Password
                     </a>
                   </div>
                   <p style="font-size: 14px; color: #888; text-align: center;">
                     This link will expire in <strong>15 minutes</strong> for your security.<br/>
                     If you didn’t request a password reset, you can safely ignore this email.
                   </p>
                   <hr style="margin: 20px 0; border: none; border-top: 1px solid #eee;"/>
                   <p style="font-size: 12px; color: #aaa; text-align: center;">
                     If the button above doesn’t work, copy and paste this URL into your browser:<br/>
                     <span style="color: #3498db;">%s</span>
                   </p>
                 </div>
                \s""";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
