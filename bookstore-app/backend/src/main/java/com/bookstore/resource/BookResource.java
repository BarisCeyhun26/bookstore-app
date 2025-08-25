package com.bookstore.resource;

import com.bookstore.entity.Book;
import com.bookstore.entity.Author;
import com.bookstore.entity.Genre;
import com.bookstore.entity.Publisher;
import com.bookstore.service.BookService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    
    @Inject
    BookService bookService;
    
    @GET
    public Response getAllBooks(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("title") String title,
            @QueryParam("author") String author,
            @QueryParam("genre") String genre,
            @QueryParam("format") String format) {
        
        List<Book> books = bookService.getBooks(page, size, title, author, genre, format);
        return Response.ok(books).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") Long id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book).build();
    }
    
    @GET
    @Path("/author/{authorId}")
    public Response getBooksByAuthor(@PathParam("authorId") Long authorId) {
        List<Book> books = bookService.getBooksByAuthor(authorId);
        return Response.ok(books).build();
    }
    
    @GET
    @Path("/genre/{genreId}")
    public Response getBooksByGenre(@PathParam("genreId") Long genreId) {
        List<Book> books = bookService.getBooksByGenre(genreId);
        return Response.ok(books).build();
    }
    
    @GET
    @Path("/publisher/{publisherId}")
    public Response getBooksByPublisher(@PathParam("publisherId") Long publisherId) {
        List<Book> books = bookService.getBooksByPublisher(publisherId);
        return Response.ok(books).build();
    }
    
    @GET
    @Path("/format/{format}")
    public Response getBooksByFormat(@PathParam("format") String format) {
        List<Book> books = bookService.getBooksByFormat(format);
        return Response.ok(books).build();
    }
    
    @GET
    @Path("/search")
    public Response searchBooks(@QueryParam("q") String query) {
        List<Book> books = bookService.searchBooks(query);
        return Response.ok(books).build();
    }
    
    @GET
    @Path("/bestsellers")
    public Response getBestsellers(@QueryParam("limit") @DefaultValue("5") int limit) {
        List<Book> books = bookService.getBestsellers(limit);
        return Response.ok(books).build();
    }
    
    @POST
    public Response createBook(@Valid Book book) {
        Book createdBook = bookService.createBook(book);
        return Response.status(Response.Status.CREATED).entity(createdBook).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") Long id, @Valid Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        if (updatedBook == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedBook).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        boolean deleted = bookService.deleteBook(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
