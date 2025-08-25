package com.bookstore.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "publishers")
public class Publisher extends PanacheEntity {
    
    @NotBlank
    @Column(name = "name", nullable = false)
    public String name;
    
    @Column(name = "address")
    public String address;
    
    @Column(name = "phone")
    public String phone;
    
    @Email
    @Column(name = "email")
    public String email;
    
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Book> books;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
