package gr1.fpt.bambikitchen.model.dto.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class NotificationSendingRequest {
    @NonNull
    private String title;
    @NonNull
    private String message;

    private String deviceToken;
    private Integer userId;
}
