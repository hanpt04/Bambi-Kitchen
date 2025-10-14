package gr1.fpt.bambikitchen.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderUpdateDto {
    int orderId;
    String comment;
    int ranking;
}
