package org.agoncal.sample.javaee.howsmallissmall.topbooks.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.agoncal.sample.javaee.howsmallissmall.topbooks.model.Book;

@Path("/topbooks")
public class BookEndpoint
{
   @Inject
   private EntityManager em;

   @Inject
   private Logger logger;

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public List<Book> getTopBooks()
   {
      List<Book> results = new ArrayList<>();

      int min = em.createQuery("select min (b.id) from Book b", Long.class).getSingleResult().intValue();
      int max = em.createQuery("select max (b.id) from Book b", Long.class).getSingleResult().intValue();

      while (results.size() < 3)
      {
         long id = new Random().nextInt((max - min) + 1) + min;
         Book item = em.find(Book.class, id);
         if (item != null)
            results.add(item);
      }

      logger.info("Top Books are " + results);

      return results;
   }
}
