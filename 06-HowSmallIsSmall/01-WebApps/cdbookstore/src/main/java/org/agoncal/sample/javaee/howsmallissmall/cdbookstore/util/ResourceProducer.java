package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.util;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

public class ResourceProducer
{

   @Produces
   @RequestScoped
   private FacesContext produceFacesContext()
   {
      return FacesContext.getCurrentInstance();
   }

   @Produces
   @RequestScoped
   private HttpServletResponse produceHttpServletResponse()
   {
      return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
   }

   @Produces
   public Logger produceLogger(InjectionPoint injectionPoint)
   {
      return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
   }
}