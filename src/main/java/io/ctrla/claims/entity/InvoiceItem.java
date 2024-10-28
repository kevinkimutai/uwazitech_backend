package io.ctrla.claims.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_item_id")
    private Integer invoiceItemId;

    @Column(name = "invoice_item_name")
    private String invoiceItemName;

    @Column(name = "invoice_item_quantity")
    private String invoiceItemQuantity;

    @Column(name = "invoice_item_price")
    private Double invoiceItemPrice;

    @Column(name = "total_price")
    private Double ItemTotalPrice;

    // Many-to-One relationship with Invoice
    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

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
