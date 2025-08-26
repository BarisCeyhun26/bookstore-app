package com.bookstore.resource;

import com.bookstore.entity.Author;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    
    @GET
    public Response getAllAuthors() {
        List<Author> authors = Author.listAll();
        return Response.ok(authors).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") Long id) {
        Author author = Author.findById(id);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(author).build();
    }
    
    @POST
    @Transactional
    public Response createAuthor(Author author) {
        author.persist();
        return Response.status(Response.Status.CREATED).entity(author).build();
    }
    
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateAuthor(@PathParam("id") Long id, Author author) {
        Author existingAuthor = Author.findById(id);
        if (existingAuthor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        existingAuthor.name = author.name;
        existingAuthor.biography = author.biography;
        existingAuthor.birthDate = author.birthDate;
        existingAuthor.nationality = author.nationality;
        
        return Response.ok(existingAuthor).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteAuthor(@PathParam("id") Long id) {
        Author author = Author.findById(id);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
