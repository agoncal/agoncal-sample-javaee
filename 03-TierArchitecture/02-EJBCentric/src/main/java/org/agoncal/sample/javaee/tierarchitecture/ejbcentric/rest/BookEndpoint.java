package org.agoncal.sample.javaee.tierarchitecture.ejbcentric.rest;

import org.agoncal.sample.javaee.tierarchitecture.ejbcentric.model.Book;
import org.agoncal.sample.javaee.tierarchitecture.ejbcentric.service.BookService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Path("/books")
public class BookEndpoint {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private BookService bookService;

    // ======================================
    // =          Business Methods          =
    // ======================================

    @POST
    @Consumes("application/xml")
    public Response create(Book entity) {
        bookService.create(entity);
        return Response.created(UriBuilder.fromResource(BookEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") Long id) {
        Book deletableEntity = bookService.findById(id);
        if (deletableEntity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        bookService.delete(deletableEntity);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/xml")
    public Response findById(@PathParam("id") Long id) {
        Book entity = bookService.findByIdWithRelations(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }

    @GET
    @Produces("application/xml")
    public List<Book> findAll() {
        final List<Book> results = bookService.findAll();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/xml")
    public Response update(Book entity) {
        bookService.update(entity);
        return Response.noContent().build();
    }
}