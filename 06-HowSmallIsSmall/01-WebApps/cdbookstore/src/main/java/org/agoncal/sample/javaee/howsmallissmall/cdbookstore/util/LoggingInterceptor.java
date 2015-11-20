package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.util;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

@Loggable
@Interceptor
public class LoggingInterceptor
{

   @Inject
   private Logger logger;

   @AroundInvoke
   private Object intercept(InvocationContext ic) throws Exception
   {
      try
      {
         return ic.proceed();
      }
      finally
      {
      }
   }
}