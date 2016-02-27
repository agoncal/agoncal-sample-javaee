package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.util;

import javax.inject.Inject;
import java.util.logging.Logger;

@EightDigits
public class IssnGenerator
{

   @Inject
   private Logger logger;
   @Inject
   @EightDigits
   private String prefix;
   @Inject
   @EightDigits
   private int postfix;

   public String generateNumber()
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}