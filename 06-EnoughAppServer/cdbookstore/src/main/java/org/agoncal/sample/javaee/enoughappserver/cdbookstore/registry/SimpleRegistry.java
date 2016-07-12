package org.agoncal.sample.javaee.enoughappserver.cdbookstore.registry;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class SimpleRegistry implements Registry
{

   private static Logger logger = Logger.getLogger(SimpleRegistry.class.getName());

   @Override
   public String discoverTopBooksService()
   {
      String[] topBooksServiceURLs = { "http://localhost:8080/topbooks", "http://localhost:8082/topbooks" };
      return discoverService(topBooksServiceURLs);
   }

   @Override
   public String discoverTopCDsService()
   {
      String[] topCDsServiceURLs = { "http://localhost:8080/topcds", "http://localhost:8081/topcds" };
      return discoverService(topCDsServiceURLs);
   }

   private static String discoverService(String[] urls)
   {
      for (String url : urls)
      {
         try
         {
            Response response = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();
            if (response.getStatus() == Response.Status.OK.getStatusCode())
            {
               logger.info("OK URL : " + url);
               return url;
            }
            else
            {
               logger.warning("KO URL : " + url + "  -  Status code : " + response.getStatus());
            }
         }
         catch (Exception e)
         {
            logger.info("KO URL : " + url);
         }
      }
      return null;
   }
}
