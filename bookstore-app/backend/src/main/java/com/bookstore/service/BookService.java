package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Author;
import com.bookstore.entity.Genre;
import com.bookstore.entity.Publisher;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class BookService {
    
    public List<Book> getBooks(int page, int size, String title, String author, String genre, String format) {
        StringBuilder query = new StringBuilder("SELECT b FROM Book b JOIN FETCH b.author JOIN FETCH b.publisher JOIN FETCH b.genre");
        Parameters params = new Parameters();
        
        if (title != null && !title.trim().isEmpty()) {
            query.append(" WHERE LOWER(b.title) LIKE LOWER(:title)");
            params.and("title", "%" + title + "%");
        }
        
        if (author != null && !author.trim().isEmpty()) {
            if (params.map().isEmpty()) {
                query.append(" WHERE LOWER(b.author.lastName) LIKE LOWER(:author) OR LOWER(b.author.firstName) LIKE LOWER(:author)");
            } else {
                query.append(" AND (LOWER(b.author.lastName) LIKE LOWER(:author) OR LOWER(b.author.firstName) LIKE LOWER(:author))");
            }
            params.and("author", "%" + author + "%");
        }
        
        if (genre != null && !genre.trim().isEmpty()) {
            if (params.map().isEmpty()) {
                query.append(" WHERE LOWER(b.genre.name) LIKE LOWER(:genre)");
            } else {
                query.append(" AND LOWER(b.genre.name) LIKE LOWER(:genre)");
            }
            params.and("genre", "%" + genre + "%");
        }
        
        if (format != null && !format.trim().isEmpty()) {
            if (params.map().isEmpty()) {
                query.append(" WHERE b.format = :format");
            } else {
                query.append(" AND b.format = :format");
            }
            params.and("format", Book.BookFormat.valueOf(format.toUpperCase()));
        }
        
        query.append(" ORDER BY b.title");
        
        PanacheQuery<Book> panacheQuery = Book.find(query.toString(), params);
        return panacheQuery.page(Page.of(page, size)).list();
    }
    
    public Book getBookById(Long id) {
        return Book.findById(id);
    }
    
    public List<Book> getBooksByAuthor(Long authorId) {
        return Book.find("author.id = ?1 ORDER BY title", authorId).list();
    }
    
    public List<Book> getBooksByGenre(Long genreId) {
        return Book.find("genre.id = ?1 ORDER BY title", genreId).list();
    }
    
    public List<Book> getBooksByPublisher(Long publisherId) {
        return Book.find("publisher.id = ?1 ORDER BY title", publisherId).list();
    }
    
    public List<Book> getBooksByFormat(String format) {
        return Book.find("format = ?1 ORDER BY title", Book.BookFormat.valueOf(format.toUpperCase())).list();
    }
    
    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Book.listAll();
        }
        
        String searchQuery = "%" + query.toLowerCase() + "%";
        return Book.find(
            "LOWER(title) LIKE ?1 OR LOWER(isbn) LIKE ?1 OR LOWER(author.firstName) LIKE ?1 OR LOWER(author.lastName) LIKE ?1 ORDER BY title",
            searchQuery
        ).list();
    }
    
    public List<Book> getBestsellers(int limit) {
        // This is a simplified implementation. In a real application, you would calculate bestsellers based on sales data
        return Book.find("ORDER BY id DESC").page(Page.of(0, limit)).list();
    }
    
    @Transactional
    public Book createBook(Book book) {
        book.persist();
        return book;
    }
    
    @Transactional
    public Book updateBook(Long id, Book bookDetails) {
        Book book = Book.findById(id);
        if (book == null) {
            return null;
        }
        
        book.title = bookDetails.title;
        book.isbn = bookDetails.isbn;
        book.author = bookDetails.author;
        book.publisher = bookDetails.publisher;
        book.genre = bookDetails.genre;
        book.publicationDate = bookDetails.publicationDate;
        book.price = bookDetails.price;
        book.format = bookDetails.format;
        book.description = bookDetails.description;
        book.coverImageUrl = bookDetails.coverImageUrl;
        
        return book;
    }
    
    @Transactional
    public boolean deleteBook(Long id) {
        Book book = Book.findById(id);
        if (book == null) {
            return false;
        }
        
        book.delete();
        return true;
    }
}
