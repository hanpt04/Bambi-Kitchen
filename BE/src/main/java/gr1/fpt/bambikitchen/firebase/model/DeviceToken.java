package gr1.fpt.bambikitchen.firebase.model;

import gr1.fpt.bambikitchen.firebase.model.constraints.Platform;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Version;

import java.sql.Timestamp;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceToken {

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Platform platform = Platform.ANDROID;

    @Column(nullable = false)
    private Integer userId;

    @UpdateTimestamp
    private Timestamp lastTimestamp;

    @Column(nullable = false)
    private boolean active = true;
}
