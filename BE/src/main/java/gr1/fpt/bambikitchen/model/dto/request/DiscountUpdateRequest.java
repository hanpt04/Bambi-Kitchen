package gr1.fpt.bambikitchen.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class DiscountUpdateRequest {
    @NotNull
    private Integer id;

    private String name;

    @Min(value = 0, message = "Discount Percent must be greater than 0 !!")
    @Max(value = 100, message = "Discount Percent must be less than 100 !!")
    private Integer discountPercent;

    @Min(value = 0, message = "Quantity must be greater than 0 !!")
    private Integer quantity;

    private Date startTime;

    private Date endTime;

    private String description;
}
