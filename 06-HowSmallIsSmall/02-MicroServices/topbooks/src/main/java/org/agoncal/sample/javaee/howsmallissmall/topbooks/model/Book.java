package org.agoncal.sample.javaee.howsmallissmall.topbooks.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

@Entity
@NamedQueries({
         @NamedQuery(name = Book.FIND_TOP_RATED, query = "SELECT b FROM Book b")
})
public class Book implements Serializable
{

   public static final String FIND_TOP_RATED = "Item.findTopRated";

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   protected Long id;

   private String isbn;

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public String getIsbn()
   {
      return isbn;
   }

   public void setIsbn(String isbn)
   {
      this.isbn = isbn;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      Book book = (Book) o;
      return Objects.equals(isbn, book.isbn);
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(isbn);
   }

   @Override
   public String toString()
   {
      return "Book{" +
               "id=" + id +
               ", isbn='" + isbn + '\'' +
               '}';
   }
}