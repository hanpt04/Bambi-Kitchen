package gr1.fpt.bambikitchen.service;


import gr1.fpt.bambikitchen.Factory.PaymentFactory;
import gr1.fpt.bambikitchen.Payment.PaymentMethod;
import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.Orders;
import gr1.fpt.bambikitchen.model.dto.request.DishCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.MakeOrderRequest;
import gr1.fpt.bambikitchen.model.dto.request.OrderItemDTO;
import gr1.fpt.bambikitchen.model.dto.request.RecipeItemDTO;
import gr1.fpt.bambikitchen.model.enums.DishType;
import gr1.fpt.bambikitchen.model.enums.OrderStatus;
import gr1.fpt.bambikitchen.model.enums.SourceType;
import gr1.fpt.bambikitchen.repository.OrderRepository;
import gr1.fpt.bambikitchen.service.impl.DishTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import gr1.fpt.bambikitchen.service.impl.IngredientServiceImpl;

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
    @Autowired
    AccountService accountService;
    @Autowired
    private IngredientServiceImpl ingredientServiceImpl;



    /**
     * Flow of the makeOrder method:
     * 1. Tính xem cần bao nhiêu nguyên liệu
     * 2. Tạo order với trạng thái pending
     * 3. Gọi kho check xem có đủ nguyên liệu không nếu không đủ thì báo lỗi
     * 4. Nếu đủ thì  gọi payment
     * 5. Nếu payment ok thì confirm order, confirm kho, lưu dish, tạo orderDetail
     * 6. Nếu payment fail thì unlock kho, đổi state order thành cancelled
     * 7. Frontend giữ screen hiện tại, không chuyển trang, chờ user bấm lại nút thanh toán thì quay lại bước 1
     */
    // nếu nó lưu lại dish custom, thì phải chỉnh lại recipe, gỡ base on ra
    public void makeOrder (MakeOrderRequest makeOrderRequest) throws Exception {
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

        // lưu dish custom vô table dish
        saveCustomDish( TongMonAn, makeOrderRequest.getAccountId());
        // Tạo order Detail


        PaymentMethod payment = paymentFactory.getPaymentMethod(makeOrderRequest.getPaymentMethod());
       // String url = payment.createPaymentRequest(Integer.parseInt(makeOrderRequest.getTotalPrice().toString()), savedOrder.getId());

    }

    // Hàm lưu dish Cusom/Adjust vào DB
    void saveCustomDish ( List<OrderItemDTO> TongMonAn, int accountId)
    {
        for ( OrderItemDTO monAn: TongMonAn) {
            if (monAn.getBasedOnId()== null && monAn.getDishId() ==null) // món custom 100%
            {
                DishCreateRequest dishRequest = new DishCreateRequest();
                dishRequest.setName(monAn.getName()+" (Custom)+"+"Của khách ID:  " + accountId);
                dishRequest.setDescription("Món custom của khách");
                dishRequest.setPrice(0); // Món custom không có giá
                dishRequest.setImageUrl("");
                dishRequest.setDishType(DishType.CUSTOM);
                dishRequest.setPublic(false);
                dishRequest.setActive(true);
                dishRequest.setAccount(accountService.findById(accountId));
                Map<Integer, Integer> ingredients = new HashMap<>();
                for (RecipeItemDTO congThuc : monAn.getRecipe())
                {
                    ingredients.put(congThuc.getIngredientId(), congThuc.getQuantity());
                }
                dishRequest.setIngredients(ingredients);
                 dishService.save(dishRequest);
                System.out.println("Dish custom 100% saved detail: " + dishRequest.getName() + " with ingredients: " + dishRequest.getIngredients());
            }
            else if (monAn.getBasedOnId() != null) // món preset có chỉnh sửa
            {
                DishCreateRequest dishRequest = new DishCreateRequest();
                dishRequest.setName(monAn.getName()+" (Custom based on ID: " + monAn.getBasedOnId()+")"+"Của khách ID:  " + accountId);
                dishRequest.setDescription("Món custom dựa trên preset của khách");
                dishRequest.setPrice(0); // Món custom không có giá
                dishRequest.setImageUrl("");
                dishRequest.setDishType(DishType.CUSTOM);
                dishRequest.setPublic(false);
                dishRequest.setActive(true);
                dishRequest.setAccount(accountService.findById(accountId));
                Map<Integer, Integer> ingredients = new HashMap<>();
                // Lấy công thức món base
                Map<Integer, Integer> baseIngredients = dishService.getIngredientsByDishId(monAn.getBasedOnId());
                for (Map.Entry<Integer, Integer> entry : baseIngredients.entrySet()) {
                    Integer ingredientId = entry.getKey();
                    Integer quantity = entry.getValue();
                    ingredients.put(ingredientId, quantity);
                }
                // Cộng trừ công thức món custom
                for (RecipeItemDTO congThuc : monAn.getRecipe())
                {
                    if (congThuc.getSourceType().equals(SourceType.ADDON))
                    {
                        ingredients.put(congThuc.getIngredientId(), ingredients.getOrDefault(congThuc.getIngredientId(), 0) + congThuc.getQuantity());
                    }
                    else if (congThuc.getSourceType().equals(SourceType.REMOVED))
                    {
                        ingredients.put(congThuc.getIngredientId(), ingredients.getOrDefault(congThuc.getIngredientId(), 0) - congThuc.getQuantity());
                    }

                }



                dishRequest.setIngredients(ingredients);
                 dishService.save(dishRequest);
                System.out.println("Dish custom based on preset saved detail: " + dishRequest.getName() + " with ingredients: " + dishRequest.getIngredients());
            }
            else // món preset 100%
            {
                System.out.println("Dish preset 100% detail: " + monAn.getName() + " with ID: " + monAn.getDishId());
            }
    }
        }




    boolean checkInventory (Map< Integer, Double> ingredientMap, int orderId) {
        return ingredientServiceImpl.checkAvailable(ingredientMap,orderId);
    }



    void confirmInventory ( int orderId)
    {

    }


    /**
     * Với dish preset 100% ( không có baseId, chỉ trỏ về dishId gốc )
     * - Chỉ tính recipe nhân với số lượng và Size để ra được quantity
     * <p>
     * Với dish preset có add/remove topping ( tạo 1 dishId mới, có baseId trỏ về dishId gốc )
     * - Tính recipe nhân với số lượng và trừ đi topping remove và add thêm topping addon
     * <p>
     * Với dish custom hoàn toàn ( tạo 1 dishId mới, baseId null )
     * - Chỉ tính recipe nhân với số lượng
     */
                //IngreId, Quantity
    public Map< Integer, Double> calculateNeededIngredients (List<OrderItemDTO> TongMonAn) {
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

                for (RecipeItemDTO congThuc : congThucMonAn) {
                    if (congThuc.getSourceType().equals(SourceType.ADDON)) {
                        Integer ingredientId = congThuc.getIngredientId();
                        Integer quantity = congThuc.getQuantity() * monAn.getQuantity(); // Nhân với số lượng món
                        ingredientMap.put(ingredientId, ingredientMap.getOrDefault(ingredientId, 0.0) + quantity);
                    } else if (congThuc.getSourceType().equals(SourceType.REMOVED)) {
                        Integer ingredientId = congThuc.getIngredientId();
                        Double quantity = (congThuc.getQuantity() * monAn.getQuantity() * monAn.getDishTemplate().getQuantityRatio()); // Nhân với số lượng món
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
