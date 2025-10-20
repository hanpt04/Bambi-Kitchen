package gr1.fpt.bambikitchen.model.dto.request;

import gr1.fpt.bambikitchen.model.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountUpdateRequest {
    @NotNull
    private int id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    private Role role;

    private boolean isActive;

    private String mail;

    private String phone;

    private String password;
}
