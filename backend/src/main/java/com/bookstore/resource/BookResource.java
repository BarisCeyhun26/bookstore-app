package com.bookstore.resource;

import com.bookstore.dto.BookDTO;
import com.bookstore.entity.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Books", description = "Book management operations")
@ApplicationScoped
public class BookResource {

    @PersistenceContext
    EntityManager entityManager;

    @GET
    @Operation(summary = "Get all books", description = "Retrieve a list of all books with author, genre, and publisher information")
    public Response getAllBooks() {
        try {
            String jpql = """
                SELECT new com.bookstore.dto.BookDTO(
                    b.id, b.title, b.isbn, 
                    CONCAT(a.firstName, ' ', a.lastName), 
                    g.name, p.name, 
                    b.publicationDate, b.price, b.format, 
                    b.description, b.coverImageUrl, b.createdAt
                )
                FROM Book b
                LEFT JOIN b.author a
                LEFT JOIN b.genre g
                LEFT JOIN b.publisher p
                ORDER BY b.title
                """;
            
            List<BookDTO> books = entityManager.createQuery(jpql, BookDTO.class)
                .getResultList();
            
            return Response.ok(books).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching books: " + e.getMessage())
                .build();
        }
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a specific book by its ID")
    public Response getBookById(@PathParam("id") Long id) {
        try {
            Book book = entityManager.find(Book.class, id);
            if (book == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Book not found")
                    .build();
            }
            
            String jpql = """
                SELECT new com.bookstore.dto.BookDTO(
                    b.id, b.title, b.isbn, 
                    CONCAT(a.firstName, ' ', a.lastName), 
                    g.name, p.name, 
                    b.publicationDate, b.price, b.format, 
                    b.description, b.coverImageUrl, b.createdAt
                )
                FROM Book b
                LEFT JOIN b.author a
                LEFT JOIN b.genre g
                LEFT JOIN b.publisher p
                WHERE b.id = :id
                """;
            
            BookDTO bookDTO = entityManager.createQuery(jpql, BookDTO.class)
                .setParameter("id", id)
                .getSingleResult();
            
            return Response.ok(bookDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching book: " + e.getMessage())
                .build();
        }
    }

    @GET
    @Path("/search")
    @Operation(summary = "Search books", description = "Search books by title, author, or genre")
    public Response searchBooks(@QueryParam("q") String query) {
        if (query == null || query.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Search query is required")
                .build();
        }
        
        try {
            String jpql = """
                SELECT new com.bookstore.dto.BookDTO(
                    b.id, b.title, b.isbn, 
                    CONCAT(a.firstName, ' ', a.lastName), 
                    g.name, p.name, 
                    b.publicationDate, b.price, b.format, 
                    b.description, b.coverImageUrl, b.createdAt
                )
                FROM Book b
                LEFT JOIN b.author a
                LEFT JOIN b.genre g
                LEFT JOIN b.publisher p
                WHERE LOWER(b.title) LIKE LOWER(:query) 
                   OR LOWER(a.firstName) LIKE LOWER(:query) 
                   OR LOWER(a.lastName) LIKE LOWER(:query)
                   OR LOWER(g.name) LIKE LOWER(:query)
                ORDER BY b.title
                """;
            
            List<BookDTO> books = entityManager.createQuery(jpql, BookDTO.class)
                .setParameter("query", "%" + query + "%")
                .getResultList();
            
            return Response.ok(books).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error searching books: " + e.getMessage())
                .build();
        }
    }
}
