package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@DiscriminatorValue("C")
public class CD extends Item
{

   @Column(name = "nb_of_discs")
   private Integer nbOfDiscs;

   @ManyToOne
   private Label label;

   @ManyToMany
   private Set<Musician> musicians = new HashSet<>();

   @ManyToOne
   private Genre genre;

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

   public Integer getNbOfDiscs() {
      return nbOfDiscs;
   }

   public void setNbOfDiscs(Integer nbOfDiscs) {
      this.nbOfDiscs = nbOfDiscs;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (!(obj instanceof CD))
      {
         return false;
      }
      CD other = (CD) obj;
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
      if (nbOfDiscs != null)
         result += ", nbOfDiscs: " + nbOfDiscs;
      return result;
   }

   public Label getLabel()
   {
      return this.label;
   }

   public void setLabel(final Label label)
   {
      this.label = label;
   }

   public Set<Musician> getMusicians()
   {
      return this.musicians;
   }

   public void setMusicians(final Set<Musician> musicians)
   {
      this.musicians = musicians;
   }

   public Genre getGenre()
   {
      return this.genre;
   }

   public void setGenre(final Genre genre)
   {
      this.genre = genre;
   }
}