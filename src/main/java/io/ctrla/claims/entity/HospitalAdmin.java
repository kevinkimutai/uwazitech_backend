package io.ctrla.claims.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
public class HospitalAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_admin_id")
    private Long hospitalAdminId;

    // One-to-one relationship with User entity
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "user_id_fkey", referencedColumnName = "user_id", nullable = false)
    private User user;

    // Many-to-one relationship with Hospital entity
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "hospital_id_fkey", referencedColumnName = "hospital_id", nullable = false)
    private Hospital hospital;

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
