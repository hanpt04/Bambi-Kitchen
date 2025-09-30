package gr1.fpt.bambikitchen.service;


import gr1.fpt.bambikitchen.model.dto.request.MakeOrderRequest;
import gr1.fpt.bambikitchen.model.dto.request.OrderItemDTO;
import gr1.fpt.bambikitchen.model.dto.request.RecipeItemDTO;
import gr1.fpt.bambikitchen.model.enums.SourceType;
import gr1.fpt.bambikitchen.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    DishService dishService;


    public void makeOrder (MakeOrderRequest makeOrderRequest) {

        //check kho
        List<OrderItemDTO> TongMonAn = makeOrderRequest.getItems();
        // nếu là dish custom thì lấy base + recipe addon remove này nọ để check kho
        // nếu là dish preset thì lấy recipe của dish preset để check kho
        // Làm 1 cái map<ingredientId, totalQuantityNeeded> để check kho 1 lần
            //IngreId, Quantity
        Map< Integer, Integer> ingredientMap = new HashMap<>();
        //Lấy recipe từ preset và base trước

        for (OrderItemDTO monAn: TongMonAn) {

            if (monAn.getBasedOnId() != null)
            {
                //Ingreid, Quantity
                Map<Integer, Integer> baseIngredients = dishService.getIngredientsByDishId(monAn.getBasedOnId());
                for (Map.Entry<Integer, Integer> entry : baseIngredients.entrySet()) {
                    Integer ingredientId = entry.getKey();
                    Integer quantity = entry.getValue() * monAn.getQuantity(); // Nhân với số lượng món
                    ingredientMap.put(ingredientId, ingredientMap.getOrDefault(ingredientId, 0) + quantity);
                }
                List<RecipeItemDTO> congThucMonAn = monAn.getRecipe();
                for (RecipeItemDTO congThuc : congThucMonAn)
                {
                    if (congThuc.getSourceType().equals(SourceType.ADDON))
                    {
                        Integer ingredientId = congThuc.getIngredientId();
                        Integer quantity = congThuc.getQuantity() * monAn.getQuantity(); // Nhân với số lượng món
                        ingredientMap.put(ingredientId, ingredientMap.getOrDefault(ingredientId, 0) + quantity);
                    }
                    else if (congThuc.getSourceType().equals(SourceType.REMOVED))
                    {
                        Integer ingredientId = congThuc.getIngredientId();
                        Integer quantity = congThuc.getQuantity() * monAn.getQuantity(); // Nhân với số lượng món
                        ingredientMap.put(ingredientId, ingredientMap.getOrDefault(ingredientId, 0) - quantity);
                    }

                }
            }
        }

        ingredientMap.forEach( (ingredientId, quantity) -> {
            System.out.println("Ingredient ID: " + ingredientId + ", Total Quantity Needed: " + quantity);
        });

//        for ( OrderItemDTO monAn : TongMonAn) {
//            if (monAn.getBasedOnId()!=null)
//            {
//
//            }
//
//            for (RecipeItemDTO congThucMonAn : monAn.getRecipe() )
//            {
//                if (congThucMonAn.getSourceType().equals(SourceType.ADDON))
//                {
//                    ingredientMap.put(congThucMonAn.getIngredientId(), congThucMonAn.getQuantity());
//                }
//
//            }
//
//        }



    }

    public Map< Integer, Integer> calculateNeededIngredients (List<OrderItemDTO> items) {
        Map<Integer, Integer> ingredientMap = new HashMap<>();



        return ingredientMap;
    }


}
