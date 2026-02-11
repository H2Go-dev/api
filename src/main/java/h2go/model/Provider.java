package h2go.model;

import h2go.model.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Provider {
    @Id
    @Column(nullable = false)
    private String id;

    @OneToOne
    @MapsId
    private User user;

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private String serviceCity;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus;

    private LocalDateTime deletedAt;
}
