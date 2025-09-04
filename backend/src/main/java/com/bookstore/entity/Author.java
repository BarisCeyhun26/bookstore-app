package com.bookstore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "authors")
public class Author {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    @NotBlank
    @Column(name = "first_name", nullable = false)
    public String firstName;
    
    @NotBlank
    @Column(name = "last_name", nullable = false)
    public String lastName;
    
    @Column(name = "biography", columnDefinition = "TEXT")
    public String biography;
    
    @Column(name = "birth_date")
    public LocalDate birthDate;
    
    @Column(name = "email")
    public String email;
    
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
