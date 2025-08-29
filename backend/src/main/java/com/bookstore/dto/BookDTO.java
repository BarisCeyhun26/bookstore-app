package com.bookstore.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookDTO {
    public Long bookId;
    public String title;
    public String isbn;
    public String authorName;
    public String genreName;
    public String publisherName;
    public LocalDate publicationDate;
    public BigDecimal price;
    public String format;
    public String description;
    public String coverImageUrl;
    public LocalDateTime createdAt;
    
    public BookDTO() {}
    
    public BookDTO(Long bookId, String title, String isbn, String authorName, 
                   String genreName, String publisherName, LocalDate publicationDate, 
                   BigDecimal price, String format, String description, 
                   String coverImageUrl, LocalDateTime createdAt) {
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.authorName = authorName;
        this.genreName = genreName;
        this.publisherName = publisherName;
        this.publicationDate = publicationDate;
        this.price = price;
        this.format = format;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.createdAt = createdAt;
    }
}
