package io.ctrla.claims.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Long hospitalId;

    @Column(name = "hospital_address")
    private String hospitalAddress;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "hospital_branch")
    private String hospitalBranch;

    // One-to-many relationship with HospitalAdmin entity
    @ToString.Exclude
    @OneToMany(mappedBy = "hospital")
    private List<HospitalAdmin> hospitalAdmins;

    @ToString.Exclude
    @OneToMany(mappedBy = "hospital")
    private List<PreAuth> preAuths;

    @ToString.Exclude
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Invoice> invoices;

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
