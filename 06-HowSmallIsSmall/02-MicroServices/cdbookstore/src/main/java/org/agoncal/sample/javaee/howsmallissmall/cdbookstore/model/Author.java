package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

@Entity
public class Author extends Artist
{

   @Enumerated
   @Column(name = "preferred_language")
   private Language preferredLanguage;

   public Language getPreferredLanguage()
   {
      return preferredLanguage;
   }

   public void setPreferredLanguage(Language preferredLanguage)
   {
      this.preferredLanguage = preferredLanguage;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (!(obj instanceof Author))
      {
         return false;
      }
      Author other = (Author) obj;
      if (id != null)
      {
         if (!id.equals(other.id))
         {
            return false;
         }
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (id != null)
         result += "id: " + id;
      result += ", version: " + version;
      if (firstName != null && !firstName.trim().isEmpty())
         result += ", firstName: " + firstName;
      if (lastName != null && !lastName.trim().isEmpty())
         result += ", lastName: " + lastName;
      if (bio != null && !bio.trim().isEmpty())
         result += ", bio: " + bio;
      if (dateOfBirth != null)
         result += ", dateOfBirth: " + dateOfBirth;
      if (age != null)
         result += ", age: " + age;
      if (preferredLanguage != null)
         result += ", preferredLanguage: " + preferredLanguage;
      return result;
   }
}