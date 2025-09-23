package gr1.fpt.bambikitchen.event;

import gr1.fpt.bambikitchen.cloudinary.CloudinaryService;
import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.dto.request.IngredientDtoRequest;
import gr1.fpt.bambikitchen.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class IngredientEventListener {
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    //update img url sau khi hoàn tất tạo ingredient
    @EventListener
    @Async
    public void handleIngredientCreatedEvent(IngredientDtoRequest dto) {
        System.out.println("Sự kiện");
        System.out.println(dto.toString());
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredient().getId()).orElse(null);
        if(ingredient != null){
            try {
                String url = cloudinaryService.uploadImage(dto.getFile());
                ingredient.setImgUrl(url);
                ingredientRepository.save(ingredient);
                System.out.println("Image uploaded to Cloudinary: " + url);
                System.out.println(ingredient.toString());
                System.out.println(LocalDateTime.now()+"CLOUDINARY");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
