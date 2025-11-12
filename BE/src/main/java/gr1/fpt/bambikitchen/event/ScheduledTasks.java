package gr1.fpt.bambikitchen.event;

import gr1.fpt.bambikitchen.firebase.service.FCMService;
import gr1.fpt.bambikitchen.model.*;
import gr1.fpt.bambikitchen.model.enums.Role;
import gr1.fpt.bambikitchen.model.enums.Unit;
import gr1.fpt.bambikitchen.repository.*;
import gr1.fpt.bambikitchen.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final DishRepository dishRepository;
    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    // Task này để kiếm tra kho nguyên liệu xem còn không và thông báo cho nhân viên cũng như disable nguyên liệu và dishes liên quan nó
    private static final Map<Unit, Double> THRESHOLDS = Map.of(
            Unit.KILOGRAM, 1.0,
            Unit.GRAM, 100.0,
            Unit.LITER, 0.3,
            Unit.PCS, 3.0
    );


    // status = true nếu muốn active đống dishes
    private void toggleActiveDishes(int ingredientId, boolean status) {
        List<Recipe> recipes = recipeRepository.getIngredientsByIngredient_Id((ingredientId));
        List<Dish> dishes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            dishes.add(recipe.getDish());
        }
        for (Dish dish : dishes) {
            dish.setActive(status);
        }
        dishRepository.saveAll(dishes);
    }

    @Async
    protected void alertStaffs(List<Ingredient> ingredientsOutOfStocks) {
        List<Account> staffs = accountRepository.findAllByRole(Role.STAFF);
        List<Notification> notifications = new ArrayList<>();
        for (Account staff : staffs) {
            Notification noti = new Notification();
            noti.setTitle("Các nguyện liệu đã hết, hãy kiểm tra kho hàng");
            StringBuilder message = new StringBuilder("Các nguyên liệu sau đã hết hoặc gần hết:\n");
            for (Ingredient ingredient : ingredientsOutOfStocks) {
                message.append("- ").append(ingredient.getName())
                        .append(" (còn lại: ").append(ingredient.getAvailable())
                        .append(" ").append(ingredient.getUnit().name()).append(")\n");
            }
            noti.setMessage(message.toString());
            noti.setAccount(staff);
            notifications.add(noti);
        }

        applicationEventPublisher.publishEvent(new EventListenerSystem.SendNotificationToListUserEvent(
                staffs.stream().map(Account::getId).toList(),
                notifications.get(0).getTitle(),
                notifications.get(0).getMessage()
        ));

        notificationRepository.saveAll(notifications);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void ingredientsStockChecking() {
        System.out.println("Checking stocks");

        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<Ingredient> ingredientsOutOfStocks = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            double threshold = THRESHOLDS.getOrDefault(ingredient.getUnit(), 3.0);
            boolean isActive = ingredient.getAvailable() >= threshold;

            if (!isActive) ingredientsOutOfStocks.add(ingredient);

            ingredient.setActive(isActive);
            toggleActiveDishes(ingredient.getId(), isActive);
        }

        if (!ingredientsOutOfStocks.isEmpty()) {
            alertStaffs(ingredientsOutOfStocks);
        }
    }
    // End of the task

}
