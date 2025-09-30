package gr1.fpt.bambikitchen.model.dto.request;


import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MakeOrderRequest {


    //Order là order tổng
    //Order chứa list OrderItem là các dish mà khách order
    // OrderItem chứa list RecipeItem là các nguyên liệu trong dish đó vd addon,remove,base v.v


    int accountId;
    String paymentMethod;
    String note;
    List<OrderItemDTO> items;


}
