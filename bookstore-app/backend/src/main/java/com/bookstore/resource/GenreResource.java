package com.bookstore.resource;

import com.bookstore.entity.Genre;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/genres")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GenreResource {
    
    @GET
    public Response getAllGenres() {
        List<Genre> genres = Genre.listAll();
        return Response.ok(genres).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getGenreById(@PathParam("id") Long id) {
        Genre genre = Genre.findById(id);
        if (genre == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(genre).build();
    }
    
    @POST
    @Transactional
    public Response createGenre(Genre genre) {
        genre.persist();
        return Response.status(Response.Status.CREATED).entity(genre).build();
    }
    
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateGenre(@PathParam("id") Long id, Genre genre) {
        Genre existingGenre = Genre.findById(id);
        if (existingGenre == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        existingGenre.name = genre.name;
        existingGenre.description = genre.description;
        
        return Response.ok(existingGenre).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteGenre(@PathParam("id") Long id) {
        Genre genre = Genre.findById(id);
        if (genre == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
