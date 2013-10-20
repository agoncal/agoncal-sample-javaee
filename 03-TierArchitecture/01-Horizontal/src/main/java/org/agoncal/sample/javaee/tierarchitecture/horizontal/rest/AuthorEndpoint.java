package org.agoncal.sample.javaee.tierarchitecture.horizontal.rest;

import org.agoncal.sample.javaee.tierarchitecture.horizontal.model.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
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
@Transactional
@Path("/authors")
public class AuthorEndpoint {

    // ======================================
    // =             Attributes             =
    // ======================================

    @PersistenceContext(unitName = "sampleJavaEEHorizontalPU")
    private EntityManager em;

    // ======================================
    // =          Business Methods          =
    // ======================================

    @POST
    @Consumes("application/xml")
    public Response create(Author entity) {
        em.persist(entity);
        return Response.created(UriBuilder.fromResource(AuthorEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") Long id) {
        Author deletableEntity = em.find(Author.class, id);
        if (deletableEntity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        em.remove(deletableEntity);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/xml")
    public Response findById(@PathParam("id") Long id) {
        TypedQuery<Author> findByIdQuery = em.createQuery("SELECT DISTINCT a FROM Author a WHERE a.id = :entityId ORDER BY a.id", Author.class);
        findByIdQuery.setParameter("entityId", id);
        Author entity = findByIdQuery.getSingleResult();
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }

    @GET
    @Produces("application/xml")
    public List<Author> findAll() {
        final List<Author> results = em.createQuery("SELECT DISTINCT a FROM Author a ORDER BY a.id", Author.class).getResultList();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/xml")
    public Response update(Author entity) {
        em.merge(entity);
        return Response.noContent().build();
    }
}