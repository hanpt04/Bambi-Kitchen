package gr1.fpt.bambikitchen.event;

import gr1.fpt.bambikitchen.cloudinary.CloudinaryService;
import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.Payment;
import gr1.fpt.bambikitchen.model.dto.request.IngredientDtoRequest;
import gr1.fpt.bambikitchen.repository.IngredientRepository;
import gr1.fpt.bambikitchen.repository.PaymentRepository;
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
                System.out.println(result+"result");
                //nếu xóa thành công thì lưu ảnh mới upload lên
                if(result.get("result").equals("ok")) {
                    uploadAndSave(ingredient,dto.getFile());
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

    public static record PaymentCancelEvent(int paymentId) {}

    @EventListener
    @Async
    public void canclePaymentEvent (PaymentCancelEvent paymentCancelEvent)
    {
        Payment payment = paymentRepository.findById(paymentCancelEvent.paymentId).orElse(null);
        if(payment != null)
        {
            payment.setStatus("CANCELLED");
            payment.setNote("Payment timeout");
            paymentRepository.save(payment);
        }
    }



}
