package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Past;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder implements Serializable
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id;
   @Version
   @Column(name = "version")
   private int version;

   @Column(name = "order_date", updatable = false)
   @Temporal(TemporalType.DATE)
   @Past
   private Date orderDate;

   @Column
   private Float totalWithoutVat;

   @Column(name = "vat_rate")
   private Float vatRate;

   @Column
   private Float vat;

   @Column
   private Float totalWithVat;

   @Column
   private Float total;

   @ManyToOne
   private User customer;

   @OneToMany
   private Set<OrderLine> orderLines = new HashSet<>();

   @Embedded
   private Address address = new Address();

   @Embedded
   private CreditCard creditCard = new CreditCard();

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

   public Date getOrderDate()
   {
      return orderDate;
   }

   public void setOrderDate(Date orderDate)
   {
      this.orderDate = orderDate;
   }

   public Float getTotalWithoutVat()
   {
      return totalWithoutVat;
   }

   public void setTotalWithoutVat(Float totalWithoutVat)
   {
      this.totalWithoutVat = totalWithoutVat;
   }

   public Float getVatRate()
   {
      return vatRate;
   }

   public void setVatRate(Float vatRate)
   {
      this.vatRate = vatRate;
   }

   public Float getVat()
   {
      return vat;
   }

   public void setVat(Float vat)
   {
      this.vat = vat;
   }

   public Float getTotalWithVat()
   {
      return totalWithVat;
   }

   public void setTotalWithVat(Float totalWithVat)
   {
      this.totalWithVat = totalWithVat;
   }

   public Float getTotal()
   {
      return total;
   }

   public void setTotal(Float total)
   {
      this.total = total;
   }

   public User getCustomer()
   {
      return this.customer;
   }

   public void setCustomer(final User customer)
   {
      this.customer = customer;
   }

   public Set<OrderLine> getOrderLines()
   {
      return this.orderLines;
   }

   public void setOrderLines(final Set<OrderLine> orderLines)
   {
      this.orderLines = orderLines;
   }

   public Address getAddress()
   {
      return address;
   }

   public void setAddress(Address address)
   {
      this.address = address;
   }

   public String getStreet1()
   {
      return address.getStreet1();
   }

   public void setStreet1(String street1)
   {
      this.address.setStreet1(street1);
   }

   public String getStreet2()
   {
      return address.getStreet2();
   }

   public void setStreet2(String street2)
   {
      this.address.setStreet2(street2);
   }

   public String getCity()
   {
      return address.getCity();
   }

   public void setCity(String city)
   {
      this.address.setCity(city);
   }

   public String getState()
   {
      return address.getState();
   }

   public void setState(String state)
   {
      this.address.setState(state);
   }

   public String getZipcode()
   {
      return address.getZipcode();
   }

   public void setZipcode(String zipcode)
   {
      this.address.setZipcode(zipcode);
   }

   public CreditCard getCreditCard()
   {
      return creditCard;
   }

   public void setCreditCard(CreditCard creditCard)
   {
      this.creditCard = creditCard;
   }

   public String getCreditCardNumber()
   {
      return creditCard.getCreditCardNumber();
   }

   public void setCreditCardNumber(String creditCardNumber)
   {
      this.creditCard.setCreditCardNumber(creditCardNumber);
   }

   public CreditCardType getCreditCardType()
   {
      return creditCard.getCreditCardType();
   }

   public void setCreditCardType(CreditCardType creditCardType)
   {
      this.creditCard.setCreditCardType(creditCardType);
   }

   public String getCreditCardExpDate()
   {
      return creditCard.getCreditCardExpDate();
   }

   public void setCreditCardExpDate(String creditCardExpDate)
   {
      this.creditCard.setCreditCardExpDate(creditCardExpDate);
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (!(obj instanceof PurchaseOrder))
      {
         return false;
      }
      PurchaseOrder other = (PurchaseOrder) obj;
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
      if (orderDate != null)
         result += ", orderDate: " + orderDate;
      if (totalWithoutVat != null)
         result += ", totalWithoutVat: " + totalWithoutVat;
      if (vatRate != null)
         result += ", vatRate: " + vatRate;
      if (vat != null)
         result += ", vat: " + vat;
      if (totalWithVat != null)
         result += ", totalWithVat: " + totalWithVat;
      if (total != null)
         result += ", total: " + total;
      if (customer != null)
         result += ", customer: " + customer;
      if (orderLines != null)
         result += ", orderLines: " + orderLines;
      if (getStreet1() != null && !getStreet1().trim().isEmpty())
         result += ", street1: " + getStreet1();
      if (getStreet2() != null && !getStreet2().trim().isEmpty())
         result += ", street2: " + getStreet2();
      if (getCity() != null && !getCity().trim().isEmpty())
         result += ", city: " + getCity();
      if (getState() != null && !getState().trim().isEmpty())
         result += ", state: " + getState();
      if (getZipcode() != null && !getZipcode().trim().isEmpty())
         result += ", zipcode: " + getZipcode();
      if (getCreditCardNumber() != null && !getCreditCardNumber().trim().isEmpty())
         result += ", creditCardNumber: " + getCreditCardNumber();
      if (getCreditCardType() != null)
         result += ", creditCardType: " + getCreditCardType();
      if (getCreditCardExpDate() != null && !getCreditCardExpDate().trim().isEmpty())
         result += ", creditCardExpDate: " + getCreditCardExpDate();
      return result;
   }
}