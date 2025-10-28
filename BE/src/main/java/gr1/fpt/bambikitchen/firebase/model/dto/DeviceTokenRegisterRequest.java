package gr1.fpt.bambikitchen.firebase.model.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import gr1.fpt.bambikitchen.firebase.model.constraints.Platform;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTokenRegisterRequest {
    private String token;
    @JsonValue
    private Platform platform;
    private Integer userId;
}
