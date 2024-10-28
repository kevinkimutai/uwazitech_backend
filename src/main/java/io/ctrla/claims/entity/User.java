package io.ctrla.claims.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotNull(message = "email cannot be null")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @Column(name = "is_verified")
    private Boolean isVerified = false;


    @Column(name = "first_name")
    @NotNull(message = "first_name cannot be null")
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = "last_name cannot be null`                                                                                                                                                                                                                                                                                                                                                                                                                                                     ````111111`st_name cannot be null")
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Roles role = Roles.ROLE_POLICYHOLDER;

    @NotNull(message = "password cannot be null")
    private String password;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<PolicyHolder> policyHolders;
}


