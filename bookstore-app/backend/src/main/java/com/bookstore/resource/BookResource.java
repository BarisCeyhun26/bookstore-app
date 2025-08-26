package com.bookstore.resource;

import com.bookstore.entity.Book;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    
    @GET
    public Response getAllBooks() {
        List<Book> books = Book.listAll();
        return Response.ok(books).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") Long id) {
        Book book = Book.findById(id);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book).build();
    }
    
    @POST
    @Transactional
    public Response createBook(Book book) {
        book.persist();
        return Response.status(Response.Status.CREATED).entity(book).build();
    }
    
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateBook(@PathParam("id") Long id, Book book) {
        Book existingBook = Book.findById(id);
        if (existingBook == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        existingBook.title = book.title;
        existingBook.isbn = book.isbn;
        existingBook.publicationDate = book.publicationDate;
        existingBook.price = book.price;
        existingBook.format = book.format;
        existingBook.description = book.description;
        existingBook.coverImageUrl = book.coverImageUrl;
        
        return Response.ok(existingBook).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteBook(@PathParam("id") Long id) {
        Book book = Book.findById(id);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        book.delete();
        return Response.noContent().build();
    }
}
