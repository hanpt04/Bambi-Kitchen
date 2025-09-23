package gr1.fpt.bambikitchen.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class IngredientsGetCountRequest {
    List<Integer> dishes;
}
