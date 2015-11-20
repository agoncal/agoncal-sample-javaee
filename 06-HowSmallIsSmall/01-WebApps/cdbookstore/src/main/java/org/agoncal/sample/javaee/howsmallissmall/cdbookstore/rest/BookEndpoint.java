package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.Book;

/**
 * 
 */
@Stateless
@Path("/books")
public class BookEndpoint
{
   @PersistenceContext(unitName = "hsisCDBookStorePU")
   private EntityManager em;

   @POST
   @Consumes({ "application/xml", "application/json" })
   public Response create(Book entity)
   {
      em.persist(entity);
      return Response.created(
               UriBuilder.fromResource(BookEndpoint.class)
                        .path(String.valueOf(entity.getId())).build())
               .build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Book entity = em.find(Book.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces({ "application/xml", "application/json" })
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<Book> findByIdQuery = em
               .createQuery(
                        "SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.category LEFT JOIN FETCH b.authors LEFT JOIN FETCH b.publisher WHERE b.id = :entityId ORDER BY b.id",
                        Book.class);
      findByIdQuery.setParameter("entityId", id);
      Book entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      return Response.ok(entity).build();
   }

   @GET
   @Produces({ "application/xml", "application/json" })
   public List<Book> listAll(@QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult)
   {
      TypedQuery<Book> findAllQuery = em
               .createQuery(
                        "SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.category LEFT JOIN FETCH b.authors LEFT JOIN FETCH b.publisher ORDER BY b.id",
                        Book.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<Book> results = findAllQuery.getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes({ "application/xml", "application/json" })
   public Response update(@PathParam("id") Long id, Book entity)
   {
      if (entity == null)
      {
         return Response.status(Status.BAD_REQUEST).build();
      }
      if (id == null)
      {
         return Response.status(Status.BAD_REQUEST).build();
      }
      if (!id.equals(entity.getId()))
      {
         return Response.status(Status.CONFLICT).entity(entity).build();
      }
      if (em.find(Book.class, id) == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      try
      {
         entity = em.merge(entity);
      }
      catch (OptimisticLockException e)
      {
         return Response.status(Response.Status.CONFLICT)
                  .entity(e.getEntity()).build();
      }

      return Response.noContent().build();
   }
}
