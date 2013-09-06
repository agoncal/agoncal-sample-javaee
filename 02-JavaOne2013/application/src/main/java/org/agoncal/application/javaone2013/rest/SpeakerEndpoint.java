package org.agoncal.application.javaone2013.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.agoncal.application.javaone2013.model.Speaker;

/**
 * 
 */
@Stateless
@Path("/speakers")
public class SpeakerEndpoint
{
   @PersistenceContext(unitName = "javaone2013PU")
   private EntityManager em;

   @POST
   @Consumes("application/xml")
   public Response create(Speaker entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(SpeakerEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Speaker entity = em.find(Speaker.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/xml")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<Speaker> findByIdQuery = em.createQuery("SELECT DISTINCT s FROM Speaker s WHERE s.id = :entityId ORDER BY s.id", Speaker.class);
      findByIdQuery.setParameter("entityId", id);
      Speaker entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      return Response.ok(entity).build();
   }

   @GET
   @Produces("application/xml")
   public List<Speaker> listAll()
   {
      final List<Speaker> results = em.createQuery("SELECT DISTINCT s FROM Speaker s ORDER BY s.id", Speaker.class).getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/xml")
   public Response update(Speaker entity)
   {
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}