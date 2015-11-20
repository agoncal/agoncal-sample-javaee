package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@DiscriminatorValue("B")
public class Book extends Item
{

   @Column(length = 15)
   @NotNull
   @Size(max = 15)
   private String isbn;

   @Column(name = "nb_of_pages")
   @Min(1)
   private Integer nbOfPage;

   @Column(name = "publication_date")
   @Temporal(TemporalType.DATE)
   private Date publicationDate;

   @Enumerated
   private Language language;

   @ManyToOne
   private Category category;

   @OneToMany
   private Set<Author> authors = new HashSet<>();

   @ManyToOne
   private Publisher publisher;

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

   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public Float getUnitCost()
   {
      return unitCost;
   }

   public void setUnitCost(Float unitCost)
   {
      this.unitCost = unitCost;
   }

   public String getIsbn()
   {
      return isbn;
   }

   public void setIsbn(String isbn)
   {
      this.isbn = isbn;
   }

   public Integer getNbOfPage()
   {
      return nbOfPage;
   }

   public void setNbOfPage(Integer nbOfPage)
   {
      this.nbOfPage = nbOfPage;
   }

   public Date getPublicationDate()
   {
      return publicationDate;
   }

   public void setPublicationDate(Date publicationDate)
   {
      this.publicationDate = publicationDate;
   }

   public Language getLanguage()
   {
      return language;
   }

   public void setLanguage(Language language)
   {
      this.language = language;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (!(obj instanceof Book))
      {
         return false;
      }
      Book other = (Book) obj;
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
      if (title != null && !title.trim().isEmpty())
         result += ", title: " + title;
      if (description != null && !description.trim().isEmpty())
         result += ", description: " + description;
      if (unitCost != null)
         result += ", unitCost: " + unitCost;
      if (isbn != null && !isbn.trim().isEmpty())
         result += ", isbn: " + isbn;
      if (nbOfPage != null)
         result += ", nbOfPage: " + nbOfPage;
      if (publicationDate != null)
         result += ", publicationDate: " + publicationDate;
      if (language != null)
         result += ", language: " + language;
      return result;
   }

   public Category getCategory()
   {
      return this.category;
   }

   public void setCategory(final Category category)
   {
      this.category = category;
   }

   public Set<Author> getAuthors()
   {
      return this.authors;
   }

   public void setAuthors(final Set<Author> authors)
   {
      this.authors = authors;
   }

   public Publisher getPublisher()
   {
      return this.publisher;
   }

   public void setPublisher(final Publisher publisher)
   {
      this.publisher = publisher;
   }
}