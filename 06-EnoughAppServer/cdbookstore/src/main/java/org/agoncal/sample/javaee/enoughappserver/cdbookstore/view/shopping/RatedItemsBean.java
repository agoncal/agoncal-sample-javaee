package org.agoncal.sample.javaee.enoughappserver.cdbookstore.view.shopping;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.agoncal.sample.javaee.enoughappserver.cdbookstore.model.Book;
import org.agoncal.sample.javaee.enoughappserver.cdbookstore.model.CD;
import org.agoncal.sample.javaee.enoughappserver.cdbookstore.registry.Registry;

@Named
@RequestScoped
@Transactional
public class RatedItemsBean
{

   @Inject
   private FacesContext facesContext;

   @Inject
   private Logger logger;

   @Inject
   private Registry registry;

   @PersistenceContext(unitName = "hsisCDBookStorePU")
   private EntityManager em;

   List<Book> topRatedBooks = new ArrayList<>();
   List<CD> topRatedCDs = new ArrayList<>();

   @PostConstruct
   private void init()
   {
      // Top Rated Books
      List<Long> topRatedBookIds = getTopBooks();
      if (topRatedBookIds != null)
      {
         logger.fine("Top rated books ids " + topRatedBookIds);
         TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.id in :ids", Book.class);
         query.setParameter("ids", topRatedBookIds);
         topRatedBooks = query.getResultList();
      }

      // Top CRated Ds
      List<Long> topRatedCDIds = getTopCDs();
      if (topRatedCDIds != null)
      {
         logger.fine("Top rated CD ids " + topRatedCDIds);
         TypedQuery<CD> query = em.createQuery("SELECT c FROM CD c WHERE c.id in :ids", CD.class);
         query.setParameter("ids", topRatedCDIds);
         topRatedCDs = query.getResultList();
      }
   }

   private List<Long> getTopBooks()
   {
      String topBooksServiceURL = registry.discoverTopBooksService();
      if (topBooksServiceURL == null)
         return null;

      List<Long> topBookIds = new ArrayList<>();

      Response response = ClientBuilder.newClient().target(topBooksServiceURL).request().get();
      String body = response.readEntity(String.class);

      try (JsonReader reader = Json.createReader(new StringReader(body)))
      {
         JsonArray array = reader.readArray();
         for (int i = 0; i < array.size(); i++)
         {
            topBookIds.add((long) array.getJsonObject(i).getInt("id"));
         }
      }

      return topBookIds;
   }

   private List<Long> getTopCDs()
   {
      String topCDsServiceURL = registry.discoverTopCDsService();
      if (topCDsServiceURL == null)
         return null;

      List<Long> topCDIds = new ArrayList<>();

      Response response = ClientBuilder.newClient().target(topCDsServiceURL).request().get();
      String body = response.readEntity(String.class);

      try (JsonReader reader = Json.createReader(new StringReader(body)))
      {
         JsonArray array = reader.readArray();
         for (int i = 0; i < array.size(); i++)
         {
            topCDIds.add((long) array.getJsonObject(i).getInt("id"));
         }
      }

      return topCDIds;
   }

   public List<CD> getTopRatedCDs()
   {
      return topRatedCDs;
   }

   public void setTopRatedCDs(List<CD> topRatedCDs)
   {
      this.topRatedCDs = topRatedCDs;
   }

   public List<Book> getTopRatedBooks()
   {
      return topRatedBooks;
   }

   public void setTopRatedBooks(List<Book> topRatedBooks)
   {
      this.topRatedBooks = topRatedBooks;
   }
}