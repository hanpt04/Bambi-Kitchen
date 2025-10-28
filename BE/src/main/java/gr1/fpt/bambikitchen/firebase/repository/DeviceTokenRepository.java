package gr1.fpt.bambikitchen.firebase.repository;

import gr1.fpt.bambikitchen.firebase.model.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Integer> {
    List<DeviceToken> findByUserId(Integer userId);

    Optional<DeviceToken> findByToken(String token);

    Optional<DeviceToken> findByUserIdAndToken(Integer userId, String token);
}
