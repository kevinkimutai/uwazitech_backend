package io.ctrla.claims.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class PreAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pre_auth_id")
    private Long preAuthId;

    private Boolean isApproved = false;

    @ManyToOne
    @JoinColumn(name = "policyholder_id_fkey", referencedColumnName = "policy_holder_id", nullable = false)
    private PolicyHolder policyHolder;


    // Many-to-one relationship with Insurance
    @ManyToOne
    @JoinColumn(name = "insurance_id_fkey", referencedColumnName = "insurance_id", nullable = false)
    private Insurance insurance;

    // Many-to-one relationship with Hospital
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
