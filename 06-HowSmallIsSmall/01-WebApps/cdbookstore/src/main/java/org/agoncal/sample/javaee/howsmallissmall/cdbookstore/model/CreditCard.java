package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class CreditCard implements Serializable
{

   @Column(length = 30, name = "credit_card_number", nullable = false)
   @NotNull
   @Size(min = 1, max = 30)
   private String creditCardNumber;

   @Enumerated
   @Column(name = "credit_card_type")
   @NotNull
   private CreditCardType creditCardType;

   @Column(length = 5, name = "credit_card_expiry_date", nullable = false)
   @NotNull
   @Size(min = 1, max = 5)
   private String creditCardExpDate;

   public String getCreditCardNumber()
   {
      return creditCardNumber;
   }

   public void setCreditCardNumber(String creditCardNumber)
   {
      this.creditCardNumber = creditCardNumber;
   }

   public CreditCardType getCreditCardType()
   {
      return creditCardType;
   }

   public void setCreditCardType(CreditCardType creditCardType)
   {
      this.creditCardType = creditCardType;
   }

   public String getCreditCardExpDate()
   {
      return creditCardExpDate;
   }

   public void setCreditCardExpDate(String creditCardExpDate)
   {
      this.creditCardExpDate = creditCardExpDate;
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (creditCardNumber != null && !creditCardNumber.trim().isEmpty())
         result += "creditCardNumber: " + creditCardNumber;
      if (creditCardType != null)
         result += ", creditCardType: " + creditCardType;
      if (creditCardExpDate != null && !creditCardExpDate.trim().isEmpty())
         result += ", creditCardExpDate: " + creditCardExpDate;
      return result;
   }
}