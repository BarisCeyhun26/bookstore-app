package com.bookstore.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "authors")
public class Author extends PanacheEntity {
    
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
    
    @Email
    @Column(name = "email")
    public String email;
    
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Book> books;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
