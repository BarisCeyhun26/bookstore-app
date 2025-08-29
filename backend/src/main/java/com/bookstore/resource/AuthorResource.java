package com.bookstore.resource;

import com.bookstore.entity.Author;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Authors", description = "Author management operations")
@ApplicationScoped
public class AuthorResource {

    @PersistenceContext
    EntityManager entityManager;

    @GET
    @Operation(summary = "Get all authors", description = "Retrieve a list of all authors")
    public Response getAllAuthors() {
        try {
            List<Author> authors = entityManager.createQuery("SELECT a FROM Author a", Author.class).getResultList();
            return Response.ok(authors).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching authors: " + e.getMessage())
                .build();
        }
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get author by ID", description = "Retrieve a specific author by ID")
    public Response getAuthorById(@PathParam("id") Long id) {
        try {
            Author author = entityManager.find(Author.class, id);
            if (author == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Author not found")
                    .build();
            }
            return Response.ok(author).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching author: " + e.getMessage())
                .build();
        }
    }
}
