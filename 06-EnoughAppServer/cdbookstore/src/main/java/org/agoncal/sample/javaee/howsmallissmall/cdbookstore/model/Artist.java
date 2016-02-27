package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@MappedSuperclass
public class Artist
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   protected Long id;
   @Version
   @Column(name = "version")
   protected int version;

   @Column(length = 50, name = "first_name", nullable = false)
   @NotNull
   @Size(min = 2, max = 50)
   protected String firstName;

   @Column(length = 50, name = "last_name", nullable = false)
   @NotNull
   @Size(min = 2, max = 50)
   protected String lastName;

   @Column(length = 5000)
   @Size(max = 5000)
   protected String bio;

   @Column(name = "date_of_birth")
   @Temporal(TemporalType.DATE)
   @Past
   protected Date dateOfBirth;

   @Transient
   protected Integer age;

   @PostLoad
   @PostPersist
   @PostUpdate
   private void calculateAge()
   {
      if (dateOfBirth == null)
      {
         age = null;
         return;
      }

      Calendar birth = new GregorianCalendar();
      birth.setTime(dateOfBirth);
      Calendar now = new GregorianCalendar();
      now.setTime(new Date());
      int adjust = 0;
      if (now.get(Calendar.DAY_OF_YEAR) - birth.get(Calendar.DAY_OF_YEAR) < 0)
      {
         adjust = -1;
      }
      age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + adjust;
   }

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   public String getFirstName()
   {
      return firstName;
   }

   public void setFirstName(String firstName)
   {
      this.firstName = firstName;
   }

   public String getLastName()
   {
      return lastName;
   }

   public void setLastName(String lastName)
   {
      this.lastName = lastName;
   }

   public String getBio()
   {
      return bio;
   }

   public void setBio(String bio)
   {
      this.bio = bio;
   }

   public Date getDateOfBirth()
   {
      return dateOfBirth;
   }

   public void setDateOfBirth(Date dateOfBirth)
   {
      this.dateOfBirth = dateOfBirth;
   }

   public Integer getAge()
   {
      return age;
   }

   public void setAge(Integer age)
   {
      this.age = age;
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (firstName != null && !firstName.trim().isEmpty())
         result += "firstName: " + firstName;
      if (lastName != null && !lastName.trim().isEmpty())
         result += ", lastName: " + lastName;
      if (bio != null && !bio.trim().isEmpty())
         result += ", bio: " + bio;
      if (dateOfBirth != null)
         result += ", dateOfBirth: " + dateOfBirth;
      if (age != null)
         result += ", age: " + age;
      return result;
   }
}