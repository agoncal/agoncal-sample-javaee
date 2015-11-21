package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.view.shopping;

import static org.agoncal.sample.javaee.howsmallissmall.cdbookstore.view.util.ServiceRegistry.getTopBooksServiceURL;
import static org.agoncal.sample.javaee.howsmallissmall.cdbookstore.view.util.ServiceRegistry.getTopCDsServiceURL;

import java.io.StringReader;
import java.util.*;

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

import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.Item;

@Named
@RequestScoped
@Transactional
public class RatedItemsBean
{

   @Inject
   private FacesContext facesContext;

   @PersistenceContext(unitName = "hsisCDBookStorePU")
   private EntityManager em;

   List<Item> topRatedItems = new ArrayList<>();
   Set<Item> randomItems = new HashSet<>();

   @PostConstruct
   private void init()
   {
      doFindTopRated();
      doFindRandomThree();

   }

   private void doFindRandomThree()
   {
      int min = em.createQuery("select min (i.id) from Item i", Long.class).getSingleResult().intValue();
      int max = em.createQuery("select max (i.id) from Item i", Long.class).getSingleResult().intValue();

      while (randomItems.size() < 3)
      {
         long id = new Random().nextInt((max - min) + 1) + min;
         Item item = em.find(Item.class, id);
         if (item != null)
            randomItems.add(item);
      }
   }

   private void doFindTopRated()
   {
      List<Long> topTopRatedIds = new ArrayList<>();

      List<Long> topRatedBookIds = getTopBooks();
      if (topRatedBookIds != null)
         topTopRatedIds.addAll(getTopBooks());
      List<Long> topRatedCDIds = getTopCDs();
      if (topRatedCDIds != null)
         topTopRatedIds.addAll(getTopCDs());

      TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id in :ids", Item.class);
      query.setParameter("ids", topTopRatedIds);
      topRatedItems = query.getResultList();
   }

   private List<Long> getTopBooks()
   {
      String topBooksServiceURL = getTopBooksServiceURL();
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
      String topCDsServiceURL = getTopCDsServiceURL();
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

   public List<Item> getTopRatedItems()
   {
      return topRatedItems;
   }

   public void setTopRatedItems(List<Item> topRatedItems)
   {
      this.topRatedItems = topRatedItems;
   }

   public List<Item> getRandomItems()
   {
      return new ArrayList<>(randomItems);
   }

}