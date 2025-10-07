package gr1.fpt.bambikitchen.service;


import gr1.fpt.bambikitchen.Factory.PaymentFactory;
import gr1.fpt.bambikitchen.Payment.PaymentMethod;
import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.Orders;
import gr1.fpt.bambikitchen.model.dto.request.MakeOrderRequest;
import gr1.fpt.bambikitchen.model.dto.request.OrderItemDTO;
import gr1.fpt.bambikitchen.model.dto.request.RecipeItemDTO;
import gr1.fpt.bambikitchen.model.enums.OrderStatus;
import gr1.fpt.bambikitchen.model.enums.SourceType;
import gr1.fpt.bambikitchen.repository.OrderRepository;
import gr1.fpt.bambikitchen.service.impl.DishTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    DishTemplateService dishTemplateService;
    @Autowired
    PaymentFactory paymentFactory;




    public void makeOrder (MakeOrderRequest makeOrderRequest) {

        //check kho
        List<OrderItemDTO> TongMonAn = makeOrderRequest.getItems();

        Orders newOrder = new Orders();

        newOrder.setUserId(makeOrderRequest.getAccountId());
        newOrder.setTotalPrice( makeOrderRequest.getTotalPrice());
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setNote(makeOrderRequest.getNote());
        Orders savedOrder = orderRepository.save(newOrder);
        boolean isEnough = checkInventory(calculateNeededIngredients(TongMonAn), savedOrder.getId());

        if ( !isEnough ) {
            throw new CustomException("Not enough inventory to fulfill the order", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // nếu đủ thì lock kho và payment, nếu payment ok thì confirm kho và LƯU DISH VÔ DATABASE
        // nếu payment fail thì unlock kho


        PaymentMethod payment = paymentFactory.getPaymentMethod(makeOrderRequest.getPaymentMethod());


        // nếu kho ok thì lưu order




    }


    boolean checkInventory (Map< Integer, Double> ingredientMap, int orderId) {

        //Inventoryservice.check(ingredientMap)

        return false;
    }

    void confirmInventory ( int orderId)
    {

    }



                //IngreId, Quantity
    public Map< Integer, Double> calculateNeededIngredients (List<OrderItemDTO> TongMonAn) {

        // nếu là dish custom thì lấy base + recipe addon remove này nọ để check kho
        // nếu là dish preset thì lấy recipe của dish preset để check kho
        // Làm 1 cái map<ingredientId, totalQuantityNeeded> để check kho 1 lần


        //Lấy recipe từ preset và base trước
        /**
         * Với dish preset 100% ( không có baseId, chỉ trỏ về dishId gốc )
         * - Chỉ tính recipe nhân với số lượng và Size để ra được quantity
         *
         * Với dish preset có add/remove topping ( tạo 1 dishId mới, có baseId trỏ về dishId gốc )
         * - Tính recipe nhân với số lượng và trừ đi topping remove và add thêm topping addon
         *
         * Với dish custom hoàn toàn ( tạo 1 dishId mới, baseId null )
         * - Chỉ tính recipe nhân với số lượng
         */
        Map<Integer, Double > ingredientMap = new HashMap<>();

        for (OrderItemDTO monAn: TongMonAn) {
            monAn.setDishTemplate(dishTemplateService.findBySizeCode(monAn.getDishTemplate().getSize()));

            if (monAn.getBasedOnId() != null)
            {
                //Ingreid, Quantity
                Map<Integer, Integer> baseIngredients = dishService.getIngredientsByDishId(monAn.getBasedOnId());
                for (Map.Entry<Integer, Integer> entry : baseIngredients.entrySet()) {
                    Integer ingredientId = entry.getKey();
                    Double quantity =  (entry.getValue() * monAn.getQuantity()*monAn.getDishTemplate().getQuantityRatio()); // Nhân với số lượng món
                    ingredientMap.put(ingredientId, ingredientMap.getOrDefault(ingredientId, 0.0) + quantity);
                }
                List<RecipeItemDTO> congThucMonAn = monAn.getRecipe();
                for (RecipeItemDTO congThuc : congThucMonAn)
                {
                    if (congThuc.getSourceType().equals(SourceType.ADDON))
                    {
                        Integer ingredientId = congThuc.getIngredientId();
                        Integer quantity = congThuc.getQuantity() * monAn.getQuantity(); // Nhân với số lượng món
                        ingredientMap.put(ingredientId, ingredientMap.getOrDefault(ingredientId, 0.0) + quantity);
                    }
                    else if (congThuc.getSourceType().equals(SourceType.REMOVED))
                    {
                        Integer ingredientId = congThuc.getIngredientId();
                        Double quantity =  (congThuc.getQuantity() * monAn.getQuantity()*monAn.getDishTemplate().getQuantityRatio()); // Nhân với số lượng món
                        ingredientMap.put(ingredientId, ingredientMap.getOrDefault(ingredientId, 0.0) - quantity);
                    }

                }
            }
            // Nếu là món custom hoàn toàn không có base
            else if ( monAn.getBasedOnId() == null)
            {
                for (RecipeItemDTO congThuc : monAn.getRecipe())
                {
                    Integer ingredientId = congThuc.getIngredientId();
                    Integer quantity = congThuc.getQuantity() * monAn.getQuantity(); // Nhân với số lượng món
                    ingredientMap.put(ingredientId, ingredientMap.getOrDefault(ingredientId, 0.0) + quantity);
                }
            }
        }

        ingredientMap.forEach( (ingredientId, quantity) -> {
            System.out.println("Ingredient ID: " + ingredientId + ", Total Quantity Needed: " + quantity);
        });

        return ingredientMap;
    }


}
