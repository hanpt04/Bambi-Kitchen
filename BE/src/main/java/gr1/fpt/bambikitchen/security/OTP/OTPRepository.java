package gr1.fpt.bambikitchen.security.OTP;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OTPRepository extends JpaRepository<OTP, String> {

    OTP findFirstByMailOrderByCreatedAtDesc(String mail);

    boolean existsByOtpAndMailAndIsActiveOrderByCreatedAtDesc(
            String otp,
            String mail,
            boolean isActive
    );

    List<OTP> findAllByCreatedAtBefore(LocalDateTime createdAtBefore);
}
