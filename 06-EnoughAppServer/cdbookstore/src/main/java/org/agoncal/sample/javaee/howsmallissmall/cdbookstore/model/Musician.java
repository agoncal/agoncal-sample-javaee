package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class Musician extends Artist
{

   @Column(name = "preferred_instrument")
   private String preferredInstrument;

   @ManyToMany
   private Set<CD> cds = new HashSet<>();

   public String getPreferredInstrument()
   {
      return preferredInstrument;
   }

   public void setPreferredInstrument(String preferredInstrument)
   {
      this.preferredInstrument = preferredInstrument;
   }

   public Set<CD> getCds()
   {
      return this.cds;
   }

   public void setCds(final Set<CD> cds)
   {
      this.cds = cds;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (!(obj instanceof Musician))
      {
         return false;
      }
      Musician other = (Musician) obj;
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
      if (preferredInstrument != null
               && !preferredInstrument.trim().isEmpty())
         result += ", preferredInstrument: " + preferredInstrument;
      return result;
   }
}