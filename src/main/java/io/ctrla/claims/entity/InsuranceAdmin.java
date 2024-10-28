package io.ctrla.claims.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
public class InsuranceAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "insurance_admin_id")
    private Long insuranceAdminId;

    // One-to-one relationship with User entity
    @OneToOne
    @JoinColumn(name = "user_id_fkey", referencedColumnName = "user_id", nullable = false)
    private User user;

    // Many-to-one relationship with Insurance entity
    @ManyToOne
    @JoinColumn(name = "insurance_id_fkey", referencedColumnName = "insurance_id", nullable = false)
    private Insurance insurance;

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
