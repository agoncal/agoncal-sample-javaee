package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.view.util;

import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

public class ServiceRegistry
{

   private static Logger logger = Logger.getLogger(ServiceRegistry.class.getName());

   private static String[] topBooksServiceURLs = {
            "http://localhost:8081/hsisTopBooks/topbooks",
            "http://localhost:8082/hsisTopBooks/topbooks",
            "http://localhost:8080/hsisTopBooks/topbooks",
            "http://localhost:7001/hsisTopBooks/topbooks",
            "http://localhost:7002/hsisTopBooks/topbooks" };

   private static String[] topCDsServiceURLs = {
            "http://localhost:8081/hsisTopCDs/topcds",
            "http://localhost:8082/hsisTopCDs/topcds",
            "http://localhost:8080/hsisTopCDs/topcds",
            "http://localhost:7001/hsisTopCDs/topcds",
            "http://localhost:7002/hsisTopCDs/topcds" };

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
            Response response = ClientBuilder.newClient().target(url).request().get();
            if (response.getStatus() == Response.Status.OK.getStatusCode())
            {
               logger.info("OK URL : " + url);
               return url;
            }
         }
         catch (Exception e)
         {
            logger.finer("KO URL : " + url);
         }
      }
      return null;
   }
}
