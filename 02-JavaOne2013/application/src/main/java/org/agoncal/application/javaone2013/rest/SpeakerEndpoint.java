package org.agoncal.application.javaone2013.rest;

import org.agoncal.application.javaone2013.model.Speaker;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
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
@Path("/speakers")
public class SpeakerEndpoint {

    // ======================================
    // =             Attributes             =
    // ======================================

    @PersistenceContext(unitName = "javaone2013PU")
    private EntityManager em;

    // ======================================
    // =          Business Methods          =
    // ======================================

    @POST
    @Consumes("application/xml")
    public Response create(Speaker entity) {
        em.persist(entity);
        return Response.created(UriBuilder.fromResource(SpeakerEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") @NotNull Long id) {
        Speaker entity = em.find(Speaker.class, id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        em.remove(entity);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/xml")
    public Response findById(@PathParam("id") @NotNull Long id) {
        TypedQuery<Speaker> findByIdQuery = em.createQuery("SELECT DISTINCT s FROM Speaker s WHERE s.id = :entityId ORDER BY s.id", Speaker.class);
        findByIdQuery.setParameter("entityId", id);
        Speaker entity = findByIdQuery.getSingleResult();
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }

    @GET
    @Produces("application/xml")
    public List<Speaker> listAll() {
        final List<Speaker> results = em.createQuery("SELECT DISTINCT s FROM Speaker s ORDER BY s.id", Speaker.class).getResultList();
        return results;
    }

    @PUT
    @Consumes("application/xml")
    public Response update(Speaker entity) {
        em.merge(entity);
        return Response.noContent().build();
    }
}