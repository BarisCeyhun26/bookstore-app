package com.bookstore.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer extends PanacheEntity {
    
    @NotBlank
    @Column(name = "first_name", nullable = false)
    public String firstName;
    
    @NotBlank
    @Column(name = "last_name", nullable = false)
    public String lastName;
    
    @NotBlank
    @Email
    @Column(name = "email", nullable = false, unique = true)
    public String email;
    
    @NotBlank
    @Column(name = "password_hash", nullable = false)
    public String passwordHash;
    
    @Column(name = "phone")
    public String phone;
    
    @Column(name = "address", columnDefinition = "TEXT")
    public String address;
    
    @NotNull
    @Column(name = "is_member", nullable = false)
    public Boolean isMember = false;
    
    @NotNull
    @Column(name = "membership_discount", nullable = false, precision = 3, scale = 2)
    public BigDecimal membershipDiscount = BigDecimal.ZERO;
    
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Order> orders;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<ShoppingCart> shoppingCart;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
