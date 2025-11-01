package gr1.fpt.bambikitchen.model.dto.response;

import gr1.fpt.bambikitchen.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
    int orderId;
    int ranking;
    String comment;
    String accountName;
    int accountId;

}
