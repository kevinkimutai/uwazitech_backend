package io.ctrla.claims.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "invoice_url")
    private String invoiceUrl;

    // Many-to-One relationship with PolicyHolder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_holder_id", referencedColumnName = "policy_holder_id")
    private PolicyHolder policyHolder;

    // Many-to-One relationship with Insurance
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_id", referencedColumnName = "insurance_id")
    private Insurance insurance;

    @Column(name = "invoice_items")
    // One-to-many relationship with InvoiceItem
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id") // Maps invoice_id in InvoiceItem
    private List<InvoiceItem> invoiceItems;

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
