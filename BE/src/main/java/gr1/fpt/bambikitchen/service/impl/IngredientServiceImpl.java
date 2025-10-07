package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.Utils.FileUtil;
import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.mapper.IngredientMapper;
import gr1.fpt.bambikitchen.model.*;
import gr1.fpt.bambikitchen.model.dto.request.*;
import gr1.fpt.bambikitchen.repository.IngredientCategoryRepository;
import gr1.fpt.bambikitchen.repository.IngredientRepository;
import gr1.fpt.bambikitchen.repository.RecipeRepository;
import gr1.fpt.bambikitchen.service.IngredientService;
import gr1.fpt.bambikitchen.service.InventoryOrderService;
import gr1.fpt.bambikitchen.service.OrderItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final RecipeRepository recipeRepository;
    final ApplicationEventPublisher eventPublisher;
    final InventoryOrderService inventoryOrderService;
    final OrderItemService orderItemService;

    @Override
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient findById(int id) {
        return ingredientRepository.findById(id).orElseThrow(
                () -> new CustomException("Ingredient cannot be found " + id, HttpStatus.BAD_REQUEST)
        );
    }

    @Override
    public Ingredient findByName(String name) {
        return ingredientRepository.findByName(name).orElseThrow(
                () -> new CustomException("Ingredient cannot be found " + name, HttpStatus.BAD_REQUEST)
        );
    }

    @Override
    public Ingredient save(IngredientCreateRequest ingredient) throws IOException {

        IngredientCategory category = ingredientCategoryRepository.findById(ingredient.getCategoryId()).orElseThrow(
                () -> new CustomException("Ingredient category cannot be found " + ingredient.getCategoryId(), HttpStatus.BAD_REQUEST)
        );
        Ingredient newIngredient = ingredientMapper.toIngredient(ingredient);
        newIngredient.setCategory(category);
        Ingredient ingredientSave = ingredientRepository.save(newIngredient);

        // publisher
        if (!ingredient.getFile().isEmpty()) {
            String absolutePath = FileUtil.saveFile(ingredient.getFile());
            File file = FileUtil.getFileByPath(absolutePath);
            MultipartFile multipartFile = FileUtil.convertFileToMultipart(file);
            file.delete(); // xóa file gốc trong uploads/
            eventPublisher.publishEvent(new IngredientDtoRequest(ingredientSave, multipartFile));
        }
        return ingredientSave;
    }

    @Override
    public Ingredient update(IngredientUpdateRequest ingredient) throws IOException {
        Ingredient oldIngredient = ingredientRepository.findById(ingredient.getId()).orElseThrow(
                () -> new CustomException("Ingredient cannot be found " + ingredient.getId(), HttpStatus.BAD_REQUEST)
        );

        IngredientCategory category = ingredientCategoryRepository.findById(ingredient.getCategoryId()).orElseThrow(
                () -> new CustomException("Ingredient category cannot be found " + ingredient.getCategoryId(), HttpStatus.BAD_REQUEST)
        );
        //mới bỏ mapper set tay ( do ko hỉu )
        Ingredient newIngredient = new Ingredient();
        newIngredient.setId(oldIngredient.getId());
        newIngredient.setCategory(category);
        newIngredient.setName(ingredient.getName());
        newIngredient.setUnit(ingredient.getUnit());
        newIngredient.setActive(ingredient.getActive());
        //kiểm tra xem nếu đã có img rồi mà ko update lại img thì set lại img cũ
        if(oldIngredient.getImgUrl()!=null){
            newIngredient.setImgUrl(oldIngredient.getImgUrl());
            newIngredient.setPublicId(oldIngredient.getPublicId());
        }
        Ingredient ingredientUpdate = ingredientRepository.save(newIngredient);
        //publisher ( sẽ kiểm tra xem có file gửi về hay ko rồi mới update file
        //nếu có thay đổi ảnh: xóa ảnh hiện tại trên cloud -> cập lại lại url và public_id
        if(!ingredient.getFile().isEmpty()) {
            //luư lại file tạm trong project và giữ đường dẫn đến file
            String path = FileUtil.saveFile(ingredient.getFile());
            //lấy ra file từ path và convert qua multipartFile để upload lên cloudinary
            File file = FileUtil.getFileByPath(path);
            MultipartFile multipartFile = FileUtil.convertFileToMultipart(file);
            file.delete();
            eventPublisher.publishEvent(new IngredientDtoRequest(ingredientUpdate,multipartFile));
        }
        return ingredientUpdate;
    }

    @Override
    public String delete(int id) {
        Ingredient oldIngredient = ingredientRepository.findById(id).orElseThrow(
                () -> new CustomException("Ingredient cannot be found " + id, HttpStatus.BAD_REQUEST)
        );

        oldIngredient.setActive(false);
        ingredientRepository.save(oldIngredient);
        return "Deleted Ingredient with id: " + id;
    }


    /**
     * Aggregates ingredient quantities for the requested dishes.
     * <p>
     * Retrieves all recipes, filters them by dish IDs from {@code ingredientsGetCountRequest},
     * extracts unique ingredients, fetches their {@link IngredientDetail} records,
     * and sums the quantities for each ingredient.
     *
     * @return a map where keys are ingredient names and values are the total quantities
     * required for the requested dishes.
     */
    @Override
    public Map<Integer, Integer> getIngredientsCount(List<Integer> dishes) {
        Map<Integer, Integer> ingredientsCount = new HashMap<>();

        List<Recipe> recipes = recipeRepository.findAll();

        /**
         * Retrieves recipes and returns a map of ingredient names to total needed quantities to make requested dishes.
         */
        recipes.parallelStream()
                // Filter the needed ingredients for the requested dishes
                .filter(recipe -> dishes.contains(recipe.getDish().getId()))
                // Count required ingredient's quantity
                .forEach(recipe -> ingredientsCount.merge(
                        recipe.getIngredient().getId(),
                        recipe.getQuantity(),
                        Integer::sum
                ));

        return ingredientsCount;
    }

    //hàm check kho coi đủ nguyên liệu không
    @Override
    public boolean isEnoughIngredient(Map<Integer, Double> ingredientMap, int orderId) {
        for(Map.Entry<Integer, Double> entry : ingredientMap.entrySet()) {
            int ingredientId = entry.getKey();
            double quantity = entry.getValue();
            List<Ingredient> ingredients = ingredientRepository.findAll();
            for(Ingredient ingredient : ingredients) {
                if(ingredientId == ingredient.getId()) {
                    if(ingredient.availableIngredient()<quantity) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkAvailable(Map<Integer, Double> ingredientMap, int orderId) {
        if(!isEnoughIngredient(ingredientMap, orderId)) {
            return false;
        }
        else{
            //lưu lại các orderitem ( luư nguyên liệu + quantity để sau này trừ kho và gỡ kho
            saveOrder(orderId);
            System.out.println("Order: " + orderId);
            //giữ chỗ ingredient
            reserveIngredient(ingredientMap,orderId);
            return true;
        }
    }

    //lưu bảng này để check xem create lúc nào để nếu sau 5' chưa có trừ kho thì sẽ trả lại reserve
    public void saveOrder(int orderId){
        InventoryOrder inventory = new InventoryOrder();
        inventory.setOrderId(orderId);
        inventoryOrderService.save(inventory);
    }

    //giữ chỗ nguyên liệu
    public void reserveIngredient(Map<Integer,Double> ingredientMap, int orderId){
        for(Map.Entry<Integer,Double> entry : ingredientMap.entrySet()) {
            int ingredientId = entry.getKey();
            double quantity = entry.getValue();
            List<Ingredient> ingredients = ingredientRepository.findAll();
            for(Ingredient ingredient : ingredients) {
                //set reserve và lưu thêm bảng tạm inventory order để xem order đó giữ chỗ hồi nào
                //thêm bảng orderitem đi theo để truy vấn cái ingredientId+quantity để sau này lấy ra trừ kho hoặc trả chỗ
                if(ingredientId == ingredient.getId()) {
                    Ingredient locked = ingredientRepository.lockById(ingredientId);
                    double newReserve = locked.getReserve()+quantity;
                    double newAvailable = locked.getAvailable()-newReserve;
                    locked.setReserve(newReserve);
                    locked.setLastReserveAt(Date.valueOf(LocalDate.now()));
                    locked.setAvailable(newAvailable);
                    OrderItem item = new OrderItem();
                    item.setIngredientId(ingredientId);
                    item.setQuantity(quantity);
                    InventoryOrder inventoryOrder = inventoryOrderService.findByOrderId(orderId);
                    item.setOrder(inventoryOrder);
                    orderItemService.save(item);
                }
            }
        }
        ingredientRepository.flush();
        System.out.println(inventoryOrderService.findByOrderId(orderId));
        System.out.println(orderItemService.findByOrderId(orderId));
    }


    //hàm confirm: set lại reserve và trừ kho ( cập nhật lại quantity)
    //xóa inventoryOrder, orderItem
    public void minusInventory(int orderId){
        List<OrderItem> items = orderItemService.findByOrderId(orderId);
        for(OrderItem item : items) {
            Ingredient ingredient = ingredientRepository.findById(item.getIngredientId()).orElseThrow();
            double newQuantity = ingredient.getQuantity()-item.getQuantity();
            double newReserve = ingredient.getReserve() - item.getQuantity();
            ingredient.setReserve(newReserve);
            ingredient.setQuantity(newQuantity);
            ingredient.setAvailable(newQuantity - newReserve);
            ingredientRepository.save(ingredient);
        }
        orderItemService.deleteAllByOrderId(orderId);
        inventoryOrderService.delete(orderId);
    }


    //hàm schedule để chạy nếu sau 5' vẫn còn giữ chỗ chưa phản hôì thì sẽ cập nhật lại reserve và available quantity


}
