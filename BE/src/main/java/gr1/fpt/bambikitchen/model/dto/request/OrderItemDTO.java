package gr1.fpt.bambikitchen.model.dto.request;

import gr1.fpt.bambikitchen.model.DishTemplate;
import lombok.Data;

import java.util.List;

@Data
public class OrderItemDTO {

    private Integer dishId; // ID của dish (PRESET hoặc CUSTOM, nếu đặt lại món cũ)
    private Integer basedOnId; // ID của dish PRESET (nếu dựa trên preset), null nếu custom từ đầu
    private String name; // Tên món (VD: "Cơm Gà + Cà chua + Trứng")
    private Integer quantity; // Số lượng món

    private DishTemplate dishTemplate;
    private List<RecipeItemDTO> recipe; // Danh sách nguyên liệu
}
