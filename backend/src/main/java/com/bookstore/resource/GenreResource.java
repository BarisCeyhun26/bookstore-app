package com.bookstore.resource;

import com.bookstore.entity.Genre;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/genres")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Genre", description = "Genre management operations")
@ApplicationScoped
public class GenreResource {

    @PersistenceContext
    EntityManager entityManager;

    @GET
    @Operation(summary = "Get all genres", description = "Retrieve a list of all genres")
    public Response getAllGenres() {
        try {
            List<Genre> genres = entityManager.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
            return Response.ok(genres).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching genres: " + e.getMessage())
                .build();
        }
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get genre by ID", description = "Retrieve a specific genre by ID")
    public Response getGenreById(@PathParam("id") Long id) {
        try {
            Genre genre = entityManager.find(Genre.class, id);
            if (genre == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Genre not found")
                    .build();
            }
            return Response.ok(genre).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching genre: " + e.getMessage())
                .build();
        }
    }
}
