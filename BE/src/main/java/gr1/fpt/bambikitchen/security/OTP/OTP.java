package gr1.fpt.bambikitchen.security.OTP;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "OTP")
public class OTP {
    @Id
    private String otp;

    @Column
    private String mail;

    @Column
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column
    private LocalDateTime updatedAt;

    @Column
    private boolean isActive = true;
}
