package org.agoncal.sample.javaee.enoughappserver.registry;

public interface Registry
{
   void registerService(String name, String uri);

   void unregisterService(String name, String uri);

   String discoverTopBooksService();

   String discoverTopCDsService();
}
