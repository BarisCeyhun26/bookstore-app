package com.bookstore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "publishers")
public class Publisher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    @NotBlank
    @Column(name = "name", nullable = false)
    public String name;
    
    @Column(name = "address", columnDefinition = "TEXT")
    public String address;
    
    @Column(name = "phone")
    public String phone;
    
    @Column(name = "email")
    public String email;
    
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
