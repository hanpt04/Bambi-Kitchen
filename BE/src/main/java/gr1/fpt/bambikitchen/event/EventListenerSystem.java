package gr1.fpt.bambikitchen.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr1.fpt.bambikitchen.cloudinary.CloudinaryService;
import gr1.fpt.bambikitchen.firebase.service.FCMService;
import gr1.fpt.bambikitchen.model.*;
import gr1.fpt.bambikitchen.model.dto.request.DishDtoRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishNutritionRequest;
import gr1.fpt.bambikitchen.model.dto.request.IngredientDtoRequest;
import gr1.fpt.bambikitchen.model.enums.OrderStatus;
import gr1.fpt.bambikitchen.repository.*;
import gr1.fpt.bambikitchen.service.IngredientService;
import gr1.fpt.bambikitchen.service.impl.InventoryOrderService;
import gr1.fpt.bambikitchen.service.impl.OrderItemService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
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
    @Autowired
    private FCMService fcmService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private NutritionRepository nutritionRepository;

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${server.url:http://localhost}")
    private String serverUrl;

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

    public record MailDetails(String email, int orderId) {
    }

    @EventListener
    @Async
    public void mailBillToUser(MailDetails mailDetails) throws MessagingException {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrders_Id(mailDetails.orderId);
        sendOrder(new SendOrderEvent(mailDetails.email, toOrderDetailsMap(orderDetails)));
    }

    private List<DishInfo> toOrderDetailsMap(List<OrderDetail> orderDetails) {
        return orderDetails.parallelStream()
                .flatMap(orderDetail -> Stream.of(orderDetail.getDish()))
                .distinct()
                .flatMap(dish -> Stream.of(
                                DishInfo.builder()
                                        .name(dish.getName())
                                        .price(dish.getPrice())
                                        .ingredients(fromDishToIngredientsInfo(dish))
                                        .build()
                        )
                ).collect(Collectors.toList());
    }

    private Map<EventListenerSystem.IngredientInfo, Integer> fromDishToIngredientsInfo(Dish dish) {
        return recipeRepository.getIngredientsByDish_Id(dish.getId())
                .parallelStream()
                .collect(Collectors.toMap(
                        recipe -> {
                            return EventListenerSystem.IngredientInfo.builder()
                                    .ingredient(recipe.getIngredient())
                                    .nutrition(nutritionRepository.findByIngredient_Id(recipe.getIngredient().getId()))
                                    .build();
                        },
                        Recipe::getQuantity
                ));
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
            // Ingredient, Used Quantity
            Map<IngredientInfo, Integer> ingredients
    ) {
    }

    @Builder
    public record IngredientInfo(
            Ingredient ingredient,
            Nutrition nutrition
    ) {
    }

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
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="margin:0;padding:0;font-family:'Segoe UI',Tahoma,Geneva,Verdana,sans-serif;background:#ff8c42;padding:40px 20px;">
                    <table width="100%" cellpadding="0" cellspacing="0" border="0" style="max-width:700px;margin:0 auto;background:#ffffff;border-radius:20px;overflow:hidden;">
                        <!-- Header -->
                        <tr>
                            <td style="background:linear-gradient(135deg, #ff8c42 0%, #ff6b35 100%);background-color:#ff8c42;padding:40px 30px;text-align:center;color:white;">
                                <h1 style="font-size:32px;margin:0 0 10px 0;text-shadow:2px 2px 4px rgba(0,0,0,0.2);">🧾 Hóa Đơn Nguyên Liệu</h1>
                                <p style="font-size:16px;margin:0;opacity:0.95;">Chi tiết các món ăn và nguyên liệu</p>
                            </td>
                        </tr>
                        <!-- Content -->
                        <tr>
                            <td style="padding:40px 30px;">
            """);

        int grandTotal = 0;
        int dishIndex = 1;

        for (DishInfo dish : dishes) {
            DishNutritionRequest requestForDish = null;
            String viewLink = "#";

            try {
                var ingredients = new ArrayList<DishNutritionRequest.Ingredient>();
                for (Map.Entry<IngredientInfo, Integer> ing : dish.ingredients().entrySet()) {
                    var ingredient = DishNutritionRequest.Ingredient.builder()
                            .name(ing.getKey().ingredient().getName())
                            .amount(Double.parseDouble(ing.getValue().toString()))
                            .unit(ing.getKey().ingredient().getUnit().getName())
                            .per(ing.getKey().nutrition().getPer_unit())
                            .cal(ing.getKey().nutrition().getCalcium())
                            .pro(ing.getKey().nutrition().getProtein())
                            .carb(ing.getKey().nutrition().getCarb())
                            .fat(ing.getKey().nutrition().getSat_fat())
                            .fiber(ing.getKey().nutrition().getFiber())
                            .build();
                    ingredients.add(ingredient);
                }
                requestForDish = DishNutritionRequest.builder()
                        .name(dish.name())
                        .ingredients(ingredients)
                        .build();

                var mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(requestForDish);
                String encoded = Base64.getUrlEncoder().withoutPadding()
                        .encodeToString(json.getBytes(StandardCharsets.UTF_8));

                String appBaseUrl = serverUrl + ":" + serverPort;
                viewLink = appBaseUrl + "/api/mail/calculate-calories?q=" + encoded;

                log.info(viewLink);

            } catch (Exception e) {
                System.err.println("Error creating nutrition link: " + e.getMessage());
            }

            // Using TABLE layout for email compatibility
            html.append(String.format("""
                                <!-- Dish Card -->
                                <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="background:linear-gradient(135deg, #fff5e6 0%%, #ffe6cc 100%%);background-color:#fff5e6;border-radius:15px;margin-bottom:25px;border:2px solid #ffc299;">
                                    <tr>
                                        <td style="padding:25px;">
                                            <!-- Dish Header -->
                                            <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="margin-bottom:20px;padding-bottom:15px;border-bottom:2px solid #ff8c42;">
                                                <tr>
                                                    <td style="vertical-align:middle;">
                                                        <table cellpadding="0" cellspacing="0" border="0">
                                                            <tr>
                                                                <td style="width:35px;height:35px;background:#ff8c42;color:white;border-radius:50%%;font-size:16px;font-weight:bold;text-align:center;vertical-align:middle;">%d</td>
                                                                <td style="padding-left:12px;font-size:22px;font-weight:bold;color:#ff6b35;">%s</td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                    <td style="text-align:right;vertical-align:middle;">
                                                        <table cellpadding="0" cellspacing="0" border="0" align="right">
                                                            <tr>
                                                                <td style="background:#ff6b35;color:white;padding:8px 20px;border-radius:25px;font-weight:bold;font-size:18px;margin-bottom:10px;">%,d₫</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-top:10px;">
                                                                    <!-- Email-safe button -->
                                                                    <a href="%s" style="display:inline-block;background:#ff6b35;color:#ffffff;text-decoration:none;padding:10px 16px;border-radius:20px;font-weight:600;font-size:14px;font-family:'Segoe UI',Tahoma,Geneva,Verdana,sans-serif;" target="_blank">🔎 Xem dinh dưỡng</a>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                            <!-- Ingredients Section -->
                                            <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="background:white;border-radius:12px;overflow:hidden;">
                                                <tr>
                                                    <td style="background:#ff8c42;color:white;padding:12px 20px;font-size:16px;font-weight:600;">📋 Danh sách nguyên liệu</td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <table width="100%%" cellpadding="0" cellspacing="0" border="0">
                                                            <thead>
                                                                <tr style="background:#fff5e6;">
                                                                    <th style="padding:12px 15px;text-align:left;font-weight:600;color:#ff6b35;border-bottom:2px solid #ff8c42;font-size:14px;">Tên Nguyên Liệu</th>
                                                                    <th style="padding:12px 15px;text-align:center;font-weight:600;color:#ff6b35;border-bottom:2px solid #ff8c42;font-size:14px;width:120px;">Số Lượng</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                """, dishIndex++, dish.name(), dish.price(), viewLink));

            for (Map.Entry<IngredientInfo, Integer> ing : dish.ingredients().entrySet()) {
                html.append(String.format("""
                                                                <tr>
                                                                    <td style="padding:12px 15px;border-bottom:1px solid #ffe6cc;color:#374151;font-size:15px;">▪ %s</td>
                                                                    <td style="padding:12px 15px;border-bottom:1px solid #ffe6cc;text-align:center;">
                                                                        <span style="background:#ff8c42;color:white;padding:4px 12px;border-radius:12px;font-size:13px;font-weight:600;display:inline-block;">%d</span>
                                                                    </td>
                                                                </tr>
                    """, ing.getKey().ingredient().getName(), ing.getValue()));
            }

            html.append("""
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                """);

            grandTotal += dish.price();
        }

        html.append(String.format("""
                                <!-- Total Section -->
                                <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="background:#ff8c42;border-radius:15px;margin-top:30px;">
                                    <tr>
                                        <td style="padding:30px;text-align:center;color:white;">
                                            <div style="font-size:18px;margin-bottom:12px;opacity:0.95;font-weight:500;">💰 Tổng Chi Phí Tất Cả Món</div>
                                            <div style="font-size:36px;font-weight:bold;text-shadow:2px 2px 4px rgba(0,0,0,0.2);letter-spacing:1px;">%,d₫</div>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <!-- Footer -->
                        <tr>
                            <td style="text-align:center;padding:30px;color:#6b7280;font-size:14px;border-top:1px solid #e5e7eb;">
                                <div style="font-size:24px;margin-bottom:10px;">🍽️</div>
                                <p style="margin:5px 0;"><strong>Bambi Kitchen</strong></p>
                                <p style="margin:5px 0;">Cảm ơn quý khách đã sử dụng dịch vụ!</p>
                            </td>
                        </tr>
                    </table>
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
                 <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 600px; margin: auto; padding: 0; background: linear-gradient(135deg, #fff5e6 0%, #ffe0b3 100%); border-radius: 15px; overflow: hidden;">
                   <div style="background: linear-gradient(135deg, #ff8c42 0%, #ff6b35 100%); padding: 30px 20px; text-align: center;">
                     <h2 style="color: white; margin: 0; font-size: 28px; text-shadow: 2px 2px 4px rgba(0,0,0,0.2);">🔐 Reset Your Password</h2>
                   </div>
                
                   <div style="padding: 30px; background: white; margin: 20px; border-radius: 10px; box-shadow: 0 4px 12px rgba(255, 140, 66, 0.2);">
                     <div style="background: #fff5e6; border-left: 4px solid #ff8c42; padding: 15px; border-radius: 6px; margin-bottom: 20px;">
                       <p style="margin: 0; font-size: 16px; color: #555; line-height: 1.6;">
                     We received a request to reset the password for your account. If it was you, please ignore this email.
                   </p>
                     </div>
                
                     <div style="background: linear-gradient(135deg, #fff5e6 0%, #ffe6cc 100%); border: 2px solid #ff8c42; border-radius: 10px; padding: 20px; margin: 25px 0; text-align: center;">
                       <p style="margin: 0 0 15px 0; font-size: 18px; color: #ff6b35; font-weight: bold;">⚠️ Security Alert</p>
                       <p style="margin: 0 0 20px 0; font-size: 15px; color: #d9534f; font-weight: 600;">
                         If this wasn't you, click the button below to prevent your password from changing
                       </p>
                       <a href="%s" style="display: inline-block; background: linear-gradient(135deg, #ff8c42 0%, #ff6b35 100%); color: white; text-decoration: none; font-size: 16px; font-weight: bold; padding: 14px 32px; border-radius: 8px; box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3); transition: all 0.3s;">
                         🔒 Prevent Password Reset
                     </a>
                   </div>
                
                     <div style="background: #fffaf5; padding: 15px; border-radius: 8px; border-left: 3px solid #ffc299; margin-top: 20px;">
                       <p style="margin: 0; font-size: 14px; color: #666; text-align: center; line-height: 1.6;">
                         ⏰ This link will expire in <strong style="color: #ff6b35;">15 minutes</strong> for your security.<br/>
                         If you didn't request a password reset, you can safely ignore this email.
                   </p>
                     </div>
                
                     <hr style="margin: 25px 0; border: none; border-top: 2px solid #ffe6cc;"/>
                
                     <div style="background: #f8f9fa; padding: 15px; border-radius: 6px;">
                       <p style="margin: 0 0 8px 0; font-size: 12px; color: #888; text-align: center;">
                         If the button above doesn't work, copy and paste this URL into your browser:
                   </p>
                       <p style="margin: 0; font-size: 11px; color: #ff8c42; text-align: center; word-break: break-all; font-family: monospace;">
                         %s
                       </p>
                     </div>
                   </div>
                
                   <div style="text-align: center; padding: 20px; color: #999; font-size: 12px;">
                     <p style="margin: 0;">© 2024 Bambi Kitchen. All rights reserved.</p>
                   </div>
                 </div>
                \s""";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public record SendNotificationToListUserEvent(List<Integer> userIds, String title, String body) {
    }

    @EventListener
    @Async
    public void sendNotificationToListUsers(SendNotificationToListUserEvent event) {
        fcmService.sendNotificationToListUsers(event.title, event.body, event.userIds);
    }

    public record SendNotificationToUserEvent(Integer userId, String title, String body) {
    }

    @EventListener
    @Async
    public void sendNotificationToUsers(SendNotificationToUserEvent event) {
        fcmService.sendNotificationToUser(event.userId, event.title, event.body);
    }
}
