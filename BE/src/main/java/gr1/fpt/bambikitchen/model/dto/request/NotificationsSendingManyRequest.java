package gr1.fpt.bambikitchen.model.dto.request;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class NotificationsSendingManyRequest {
    @NonNull
    private String title;
    @NonNull
    private String message;
    private List<Integer> userIds;
}
