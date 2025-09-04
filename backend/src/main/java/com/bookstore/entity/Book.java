package com.bookstore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    @NotBlank
    @Column(name = "title", nullable = false)
    public String title;
    
    @Column(name = "isbn", unique = true)
    public String isbn;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    public Author author;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    public Publisher publisher;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    public Genre genre;
    
    @Column(name = "publication_date")
    public LocalDate publicationDate;
    
    @NotNull
    @Positive
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    public BigDecimal price;
    
    @NotBlank
    @Column(name = "format", nullable = false)
    public String format;
    
    @Column(name = "description", columnDefinition = "TEXT")
    public String description;
    
    @Column(name = "cover_image_url")
    public String coverImageUrl;
    
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
