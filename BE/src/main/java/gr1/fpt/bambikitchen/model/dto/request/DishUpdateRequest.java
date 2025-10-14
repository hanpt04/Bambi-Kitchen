package gr1.fpt.bambikitchen.model.dto.request;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.enums.DishType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Nationalized;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Data
public class DishUpdateRequest {
    private int id;
    @Nationalized
    private String name;
    @Nationalized
    private String description;
    @Min(value = 0,message = "Must be equal or greater than 0 !!")
    private int price;
    @ManyToOne
    @JoinColumn(name = "AccountId")
    private Account account;
    @Enumerated(EnumType.STRING)
    private DishType dishType;
    private boolean isActive;
    private boolean isPublic;
    @Min(value = 0,message = "Must be equal greater than 0 !!")
    private int usedQuantity;
    @Nullable
    private MultipartFile file;
}
