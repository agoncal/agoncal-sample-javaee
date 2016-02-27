package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.util;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class NumberProducer
{

   @Produces
   @ThirteenDigits
   private String prefix1;
   @Produces
   @ThirteenDigits
   private int postfix1;
   @Produces
   @EightDigits
   private String prefix2;
   @Produces
   @EightDigits
   private int postfix2;
   @Produces
   @ThirteenDigits
   @EightDigits
   @Alternative
   private String prefix3;
   @Produces
   @ThirteenDigits
   @EightDigits
   @Alternative
   private int postfix3;
   @Produces
   @Vat
   @Named
   private Float vatRate;
   @Produces
   @Discount
   @Named
   private Float discountRate;
}