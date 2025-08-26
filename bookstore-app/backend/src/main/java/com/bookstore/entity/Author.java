package com.bookstore.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "authors")
public class Author extends PanacheEntity {
    
    @NotBlank
    @Column(name = "name", nullable = false)
    public String name;
    
    @Column(name = "biography", columnDefinition = "TEXT")
    public String biography;
    
    @Column(name = "birth_date")
    public String birthDate;
    
    @Column(name = "nationality")
    public String nationality;
    
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
