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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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


    //update img url sau khi hoàn tất tạo ingredient
    @EventListener
    @Async
    public void handleIngredientCreatedEvent(IngredientDtoRequest dto) throws IOException {
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredient().getId()).orElse(null);
        if(ingredient != null) {
            if(ingredient.getPublicId() == null){
            try {
                uploadAndSave(ingredient,dto.getFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            }
            else{
                //xóa trước rồi mới lưu mới
                Map result = cloudinaryService.deleteImage(ingredient.getPublicId());
                //nếu xóa thành công thì lưu ảnh mới upload lên
                if(result.get("result").equals("ok")) {
                    uploadAndSave(ingredient,dto.getFile());
                }
            }
        }
    }

    @EventListener
    @Async
    public void handleDishCreate(DishDtoRequest dto) throws IOException {
        Dish dish = dishRepository.findById(dto.getDish().getId()).orElse(null);
        if(dto != null){
            if(dish.getPublicId() == null){
                try{
                    uploadAndSaveDish(dish,dto.getFile());
                }
                catch(IOException e){
                    throw new RuntimeException(e);
                }
            }
            else{
                Map result = cloudinaryService.deleteImage(dish.getPublicId());
                if(result.get("result").equals("ok")) {
                    uploadAndSaveDish(dish,dto.getFile());
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

    public static record PaymentCancelEvent(int paymentId) {}

    @EventListener
    @Async
    public void canclePaymentAndOrderEvent (PaymentCancelEvent paymentCancelEvent)
    {
        Payment payment = paymentRepository.findById(paymentCancelEvent.paymentId).orElse(null);
        if(payment != null)
        {
            payment.setStatus("CANCELLED");
            payment.setNote("Payment timeout");
            paymentRepository.save(payment);
        }
        Orders order = orderRepository.findById(paymentCancelEvent.paymentId).orElse(null);
        if(order != null)
        {
            order.setStatus(OrderStatus.CANCELLED);
            order.setNote("Order cancelled due to payment timeout");
            orderRepository.save(order);
        }
    }

    public static record CreateItemAndInventory(int ingredientId,double quantity, int orderId){}

    @EventListener
    @Async
    public void createItemAndInventory(CreateItemAndInventory createItemAndInventory){
        OrderItem orderItem = new OrderItem();
        orderItem.setIngredientId(createItemAndInventory.ingredientId);
        orderItem.setQuantity(createItemAndInventory.quantity);
        ingredientService.saveOrder(createItemAndInventory.orderId);
        InventoryOrder inventoryOrder = inventoryOrderService.findByOrderId(createItemAndInventory.orderId);
        orderItem.setOrder(inventoryOrder);
        orderItemService.save(orderItem);
    }


}
