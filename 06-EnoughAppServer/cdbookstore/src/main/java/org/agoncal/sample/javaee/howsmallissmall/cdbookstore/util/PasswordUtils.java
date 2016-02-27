package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.util;

import java.security.MessageDigest;
import java.util.Base64;

public class PasswordUtils
{

   public static String digestPassword(String plainTextPassword)
   {
      try
      {
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         md.update(plainTextPassword.getBytes("UTF-8"));
         byte[] passwordDigest = md.digest();
         return new String(Base64.getEncoder().encode(passwordDigest));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Exception encoding password", e);
      }
   }
}