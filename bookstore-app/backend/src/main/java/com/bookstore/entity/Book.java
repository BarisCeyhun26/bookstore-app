package com.bookstore.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book extends PanacheEntity {
    
    @NotBlank
    @Column(name = "title", nullable = false)
    public String title;
    
    @Column(name = "isbn", unique = true)
    public String isbn;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    public Author author;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    public Publisher publisher;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    public Genre genre;
    
    @Column(name = "publication_date")
    public LocalDate publicationDate;
    
    @NotNull
    @Positive
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    public BigDecimal price;
    
    @NotBlank
    @Column(name = "format", nullable = false)
    @Enumerated(EnumType.STRING)
    public BookFormat format;
    
    @Column(name = "description", columnDefinition = "TEXT")
    public String description;
    
    @Column(name = "cover_image_url")
    public String coverImageUrl;
    
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    
    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Inventory inventory;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum BookFormat {
        PHYSICAL, E_BOOK, AUDIOBOOK
    }
}
