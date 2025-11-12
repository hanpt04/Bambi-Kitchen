package gr1.fpt.bambikitchen.service.impl;


import gr1.fpt.bambikitchen.Factory.PaymentFactory;
import gr1.fpt.bambikitchen.Payment.PaymentMethod;
import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.*;
import gr1.fpt.bambikitchen.model.dto.request.*;
import gr1.fpt.bambikitchen.model.dto.response.FeedbackDto;
import gr1.fpt.bambikitchen.model.enums.DishType;
import gr1.fpt.bambikitchen.model.enums.OrderStatus;
import gr1.fpt.bambikitchen.model.enums.SizeCode;
import gr1.fpt.bambikitchen.model.enums.SourceType;
import gr1.fpt.bambikitchen.repository.DishRepository;
import gr1.fpt.bambikitchen.repository.NutritionRepository;
import gr1.fpt.bambikitchen.repository.OrderDetailRepository;
import gr1.fpt.bambikitchen.repository.OrderRepository;
import gr1.fpt.bambikitchen.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
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
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    PaymentService paymentService;
    @Autowired
    private NutritionRepository nutritionRepository;

    public Orders findById(int id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new CustomException("Order not found!", HttpStatus.NOT_FOUND));
    }


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
    @Transactional
    public String makeOrder (MakeOrderRequest makeOrderRequest) throws Exception {
        List<OrderItemDTO> TongMonAn = makeOrderRequest.getItems();

        Orders savedOrder =  makeOrder( makeOrderRequest.getAccountId(), makeOrderRequest.getTotalPrice().longValue(), makeOrderRequest.getNote());
        boolean isEnough = checkInventory(calculateNeededIngredients(TongMonAn), savedOrder.getId());
        calculateNeededIngredients(TongMonAn);
        if ( !isEnough ) {
            throw new CustomException("Not enough inventory to fulfill the order", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // lưu dish custom vô table dish & tạo order Detail
        saveCustomDish( TongMonAn, makeOrderRequest.getAccountId(), savedOrder);
       Payment paymentSaved= makePayment( makeOrderRequest.getTotalPrice().longValue(), savedOrder.getId(), makeOrderRequest.getPaymentMethod(), makeOrderRequest.getAccountId() );

        PaymentMethod payment = paymentFactory.getPaymentMethod(makeOrderRequest.getPaymentMethod());
        Long price = makeOrderRequest.getTotalPrice().longValue();
        String url = payment.createPaymentRequest(price, savedOrder.getId(), paymentSaved.getOrderId());
        return url;
    }

    // Hàm save order
    Orders makeOrder(int userId, Long totalPrice, String note) {
        Orders newOrder = new Orders();
        newOrder.setUserId(userId);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setNote(note);
        return orderRepository.save(newOrder);
    }

    // Hàm save payment
    Payment makePayment( Long amount, int orderId, String method, int accountId ) {
       return paymentService .savePayment(new Payment(orderId, accountId, method, amount));
    }

    boolean checkInventory (Map< Integer, Double> ingredientMap, int orderId) {
        return ingredientServiceImpl.checkAvailable(ingredientMap,orderId);
    }

    // Hàm lưu dish Cusom/Adjust vào DB
    void saveCustomDish ( List<OrderItemDTO> TongMonAn, int accountId, Orders order) throws IOException {
        for ( OrderItemDTO monAn: TongMonAn) {
            if (monAn.getBasedOnId()== null && monAn.getDishId() ==null) // món custom 100%
            {
                DishCreateRequest dishRequest = new DishCreateRequest();
                dishRequest.setName(monAn.getName()+" (Custom)+"+"Của khách ID:  " + accountId);
                dishRequest.setDescription("Món custom của khách");
                dishRequest.setPrice(0); // Món custom không có giá
                dishRequest.setDishType(DishType.CUSTOM);
                dishRequest.setPublic(false);
                dishRequest.setActive(true);
                dishRequest.setFile(null);
                dishRequest.setAccount(accountService.findById(accountId));
                Map<Integer, Integer> ingredients = new HashMap<>();
                for (RecipeItemDTO congThuc : monAn.getRecipe())
                {
                    ingredients.put(congThuc.getIngredientId(), congThuc.getQuantity());
                }
                dishRequest.setIngredients(ingredients);
                Dish savedDish =  dishService.save(dishRequest);

                createOrderDetail(savedDish, order, monAn.getNote(), monAn.getDishTemplate().getSize().toString());
            }
            else if (monAn.getBasedOnId() != null) // món preset có chỉnh sửa
            {
                DishCreateRequest dishRequest = new DishCreateRequest();
                dishRequest.setName(monAn.getName()+" (Custom based on ID: " + monAn.getBasedOnId()+")"+"Của khách ID:  " + accountId);
                dishRequest.setDescription("Món custom dựa trên preset của khách");
                dishRequest.setPrice(0); // Món custom không có giá
                dishRequest.setFile(null);
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
               Dish savedDish = dishService.save(dishRequest);
                createOrderDetail(savedDish, order, monAn.getNote(), monAn.getDishTemplate().getSize().toString());
            }
            else // món preset 100%
            {
                Dish presetDish = dishRepository.findById(monAn.getDishId()).orElseThrow(() -> new CustomException("Dish not found with ID: " + monAn.getDishId(), HttpStatus.NOT_FOUND));
                createOrderDetail( presetDish, order, monAn.getNote(), monAn.getDishTemplate().getSize().toString());
            }
    }
        }

    void createOrderDetail (Dish dish, Orders order,String note, String size )
    {

        //last update 3/11: tính calo tổng dựa trên công thức món ăn
        int totalCalo = 0;
        Map<Integer,Integer> ingredientMap = dishService.getIngredientsByDishId(dish.getId());
        for(Map.Entry<Integer, Integer> entry : ingredientMap.entrySet()){
            int quantity = entry.getValue();
            totalCalo += (int) (quantity * dishTemplateService.findBySizeCode(SizeCode.valueOf(size)).getQuantityRatio());
        }
        orderDetailRepository.save(new OrderDetail(dish,order, note, size,totalCalo) );
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

        DishTemplate sizeM = dishTemplateService.findBySizeCode(SizeCode.M);
        DishTemplate sizeL = dishTemplateService.findBySizeCode(SizeCode.L);
        DishTemplate sizeS = dishTemplateService.findBySizeCode(SizeCode.S);

        for (OrderItemDTO monAn: TongMonAn) {

            switch (monAn.getDishTemplate().getSize().toString())
            {
                case "S":
                    monAn.setDishTemplate(sizeS);
                    break;
                case "M":
                    monAn.setDishTemplate(sizeM);
                    break;
                case "L":
                    monAn.setDishTemplate(sizeL);
                    break;
                default:
                    throw new CustomException("Size not found", HttpStatus.NOT_FOUND);
            }


            //Ingreid, Quantity
            Map<Integer, Integer> baseIngredients = monAn.getBasedOnId() != null ? dishService.getIngredientsByDishId(monAn.getBasedOnId()) : null;

            if (monAn.getBasedOnId() != null ) // món preset có chỉnh sửa
            {
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
            else if ( monAn.getBasedOnId() == null && monAn.getDishId() == null ) // món custom 100%
            {
                for (RecipeItemDTO congThuc : monAn.getRecipe())
                {
                    Integer ingredientId = congThuc.getIngredientId();
                    Integer quantity = congThuc.getQuantity() * monAn.getQuantity(); // Nhân với số lượng món
                    ingredientMap.put(ingredientId, ingredientMap.getOrDefault(ingredientId, 0.0) + quantity);
                }
            }
            // nếu là preset 100%
            else if ( monAn.getDishId() != null ){
                baseIngredients = dishService.getIngredientsByDishId(monAn.getDishId());
                for (Map.Entry<Integer, Integer> entry : baseIngredients.entrySet()) {
                    Integer ingredientId = entry.getKey();
                    Double quantity = (entry.getValue() * monAn.getQuantity()*monAn.getDishTemplate().getQuantityRatio()); // Nhân với số lượng món
                    ingredientMap.put(ingredientId, ingredientMap.getOrDefault(ingredientId, 0.0) + quantity);
                }
            }
        }
        return ingredientMap;
    }

    public Orders feedbackOrder(OrderUpdateDto dto) {
        Orders orders = orderRepository.findById(dto.getOrderId()).orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        if(orders.getStatus().equals(OrderStatus.PAID)){
            orders.setComment(dto.getComment());
            orders.setRanking(dto.getRanking());
            return orderRepository.save(orders);
        }

           throw new CustomException("Order not paid", HttpStatus.BAD_REQUEST);
    }


    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Orders> getOrderByUser(int userId) {
        return orderRepository.findByUserId(userId);
    }

    public Orders getOrderById(int orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
    }

    public List<FeedbackDto> getFeedback(){
        List<FeedbackDto> feedbacks = new ArrayList<>();
        for( Orders order : orderRepository.findAll()){
            FeedbackDto feedback = new FeedbackDto();
            Account account = accountService.findById(order.getUserId());
            feedback.setAccountId(account.getId());
            feedback.setAccountName(account.getName());
            feedback.setOrderId(order.getId());
            feedback.setRanking(order.getRanking());
            feedback.setComment(order.getComment());
            feedbacks.add(feedback);
        }
        return feedbacks;
    }
}
