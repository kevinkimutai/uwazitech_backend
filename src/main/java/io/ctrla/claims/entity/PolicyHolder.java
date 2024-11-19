package io.ctrla.claims.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class PolicyHolder {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "policy_holder_id")
   private Long policyHolderId;

   @Column(name="policy_number", unique = true)
   private String policyNumber;

   @ManyToOne
   @JoinColumn(name = "insurance_id_fkey", referencedColumnName = "insurance_id", nullable = false)
   private Insurance insurance;

   @ManyToOne
   @JoinColumn(name = "user_id_fkey", referencedColumnName = "user_id", nullable = false)
   private User user;

   // One-to-many relationship with Invoice
   @OneToMany(mappedBy = "policyHolder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<Invoice> invoices; // Add this to get invoices directly from PolicyHolder
}
