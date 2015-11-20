package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.view.util;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

@CatchException
@Interceptor
public class CatchExceptionInterceptor
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