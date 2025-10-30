package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.Utils.FileUtil;
import gr1.fpt.bambikitchen.event.EventListenerSystem;
import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.mapper.IngredientMapper;
import gr1.fpt.bambikitchen.model.*;
import gr1.fpt.bambikitchen.model.dto.request.*;
import gr1.fpt.bambikitchen.repository.IngredientCategoryRepository;
import gr1.fpt.bambikitchen.repository.IngredientRepository;
import gr1.fpt.bambikitchen.service.IngredientService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    final ApplicationEventPublisher eventPublisher;
    final InventoryOrderService inventoryOrderService;
    final OrderItemService orderItemService;

    @PersistenceContext
    private EntityManager entityManager;

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
        newIngredient.setQuantity(ingredient.getQuantity());
        newIngredient.setAvailable(ingredient.getQuantity());
        newIngredient.setPricePerUnit(ingredient.getPricePerUnit());
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
        newIngredient.setQuantity(ingredient.getQuantity());
        newIngredient.setAvailable(ingredient.getAvailable());
        newIngredient.setReserve(ingredient.getReserve());
        newIngredient.setLastReserveAt(oldIngredient.getLastReserveAt());

        //kiểm tra xem nếu đã có img rồi mà ko update lại img thì set lại img cũ
        if (oldIngredient.getImgUrl() != null) {
            newIngredient.setImgUrl(oldIngredient.getImgUrl());
            newIngredient.setPublicId(oldIngredient.getPublicId());
        }
        Ingredient ingredientUpdate = ingredientRepository.save(newIngredient);
        //publisher ( sẽ kiểm tra xem có file gửi về hay ko rồi mới update file
        //nếu có thay đổi ảnh: xóa ảnh hiện tại trên cloud -> cập lại lại url và public_id
        if (!ingredient.getFile().isEmpty()) {
            //luư lại file tạm trong project và giữ đường dẫn đến file
            String path = FileUtil.saveFile(ingredient.getFile());
            //lấy ra file từ path và convert qua multipartFile để upload lên cloudinary
            File file = FileUtil.getFileByPath(path);
            MultipartFile multipartFile = FileUtil.convertFileToMultipart(file);
            file.delete();
            eventPublisher.publishEvent(new IngredientDtoRequest(ingredientUpdate, multipartFile));
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

    //hàm check kho coi đủ nguyên liệu không
    @Override
    public boolean isEnoughIngredient(Map<Integer, Double> ingredientMap, int orderId) {
        for (Map.Entry<Integer, Double> entry : ingredientMap.entrySet()) {
            int ingredientId = entry.getKey();
            double quantity = entry.getValue();
            //entityManager.clear();
            entityManager.getEntityManagerFactory().getCache().evict(Ingredient.class);
            Ingredient locked = ingredientRepository.lockById(ingredientId);
            if (locked.availableIngredient() < quantity) {
                return false;
            }
        }
        reserveIngredient(ingredientMap, orderId);
        return true;
    }


    @Override
    public boolean checkAvailable(Map<Integer, Double> ingredientMap, int orderId) {
        if (!isEnoughIngredient(ingredientMap, orderId)) {
            return false;
        } else {
            //lưu lại các orderitem ( luư nguyên liệu + quantity để sau này trừ kho và gỡ kho
            //giữ chỗ ingredient
            return true;
        }
    }

    //lưu bảng này để check xem create lúc nào để nếu sau 5' chưa có trừ kho thì sẽ trả lại reserve
    public void saveOrder(int orderId) {
        InventoryOrder inventory = new InventoryOrder();
        inventory.setOrderId(orderId);
        inventory.setReceivedAt(Timestamp.valueOf(LocalDateTime.now()));
        inventoryOrderService.save(inventory);
    }

    //giữ chỗ nguyên liệu
    public void reserveIngredient(Map<Integer, Double> ingredientMap, int orderId) {
        for (Map.Entry<Integer, Double> entry : ingredientMap.entrySet()) {
            int ingredientId = entry.getKey();
            double quantity = entry.getValue();
            //set reserve và lưu thêm bảng tạm inventory order để xem order đó giữ chỗ hồi nào
            //thêm bảng orderitem đi theo để truy vấn cái ingredientId+quantity để sau này lấy ra trừ kho hoặc trả chỗ
            Ingredient locked = ingredientRepository.lockById(ingredientId);
            double newReserve = locked.getReserve() + quantity;
            double newAvailable = locked.getQuantity() - newReserve;
            locked.setReserve(newReserve);
            locked.setLastReserveAt(Date.valueOf(LocalDate.now()));
            locked.setAvailable(newAvailable);
            ingredientRepository.saveAndFlush(locked);
            entityManager.clear();
            eventPublisher.publishEvent(new EventListenerSystem.CreateItemAndInventory(ingredientId, quantity, orderId));
        }
    }


    @Override
    //hàm confirm: set lại reserve và trừ kho ( cập nhật lại quantity)
    //xóa inventoryOrder, orderItem
    public void minusInventory(int orderId) {
        List<OrderItem> items = orderItemService.findByOrderId(orderId);
        for (OrderItem item : items) {
            Ingredient ingredient = ingredientRepository.findById(item.getIngredientId()).orElseThrow();
            double newQuantity = ingredient.getQuantity() - item.getQuantity();
            double newReserve = ingredient.getReserve() - item.getQuantity();
            ingredient.setReserve(newReserve);
            ingredient.setQuantity(newQuantity);
            ingredient.setAvailable(newQuantity - newReserve);
            ingredientRepository.save(ingredient);
        }
        orderItemService.deleteAllByOrderId(orderId);
        inventoryOrderService.delete(orderId);
    }

    @Override
    public void resetReserve(int orderId) {
        entityManager.clear();
        for (OrderItem item : orderItemService.findByOrderId(orderId)) {
            Ingredient ingredient = ingredientRepository.findById(item.getIngredientId()).orElseThrow();
            double newReserve = ingredient.getReserve() - item.getQuantity();
            double newAvailable = ingredient.getAvailable() + item.getQuantity();
            ingredient.setReserve(newReserve);
            ingredient.setAvailable(newAvailable);
            ingredientRepository.save(ingredient);
            orderItemService.deleteAllByOrderId(orderId);
            inventoryOrderService.delete(orderId);
        }
    }

}
