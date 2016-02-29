package org.agoncal.sample.javaee.enoughappserver.cdbookstore.view.util;

import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ServiceRegistry
{

   private static Logger logger = Logger.getLogger(ServiceRegistry.class.getName());

   private static String[] topBooksServiceURLs = {
            "http://localhost:8080/topbooks",
            "http://localhost:8081/topbooks",
            "http://localhost:8082/topbooks",
            "http://localhost:7001/topbooks",
            "http://localhost:7002/topbooks" };

   private static String[] topCDsServiceURLs = {
            "http://localhost:8080/topcds",
            "http://localhost:8081/topcds",
            "http://localhost:8082/topcds",
            "http://localhost:7001/topcds",
            "http://localhost:7002/topcds" };

   public static String getTopBooksServiceURL()
   {
      return pingWorkingURL(topBooksServiceURLs);
   }

   public static String getTopCDsServiceURL()
   {
      return pingWorkingURL(topCDsServiceURLs);
   }

   private static String pingWorkingURL(String[] urls)
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
