package io.ctrla.claims.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "insurance_id")
    private Long insuranceId;

    @Column(unique = true, name = "insurance_name")
    @NotNull(message = "insurance_name cannot be null")
    private String insuranceName;

    @Column(unique = true, name = "address")
    @NotNull(message = "insurance_address cannot be null")
    private String insuranceAddress;

    @Column(unique = true, name = "contact_phone_number")
    @NotNull(message = "phone_number cannot be null")
    private String phoneNumber;

    // One-to-many relationship with Insurance entity
    @OneToMany(mappedBy = "insurance")
    private List<InsuranceAdmin> insuranceAdmins;

    @OneToMany(mappedBy = "insurance")
    private List<PreAuth> preAuths;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
