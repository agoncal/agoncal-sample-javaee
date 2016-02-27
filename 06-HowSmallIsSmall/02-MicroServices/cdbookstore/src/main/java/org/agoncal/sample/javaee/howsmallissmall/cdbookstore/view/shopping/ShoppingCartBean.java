package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.view.shopping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.CreditCard;
import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.CreditCardType;
import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.Item;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Named
@SessionScoped
public class ShoppingCartBean implements Serializable
{

   // ======================================
   // = Attributes =
   // ======================================

   @Inject
   private FacesContext facesContext;

   @PersistenceContext(unitName = "hsisCDBookStorePU")
   private EntityManager em;

   private List<ShoppingCartItem> cartItems = new ArrayList<>();
   private CreditCard creditCard = new CreditCard();

   // ======================================
   // = Public Methods =
   // ======================================

   public String addItemToCart()
   {
      Item item = em.find(Item.class, getParamId("itemId"));

      boolean itemFound = false;
      for (ShoppingCartItem cartItem : cartItems)
      {
         // If item already exists in the shopping cart we just change the quantity
         if (cartItem.getItem().equals(item))
         {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            itemFound = true;
         }
      }
      if (!itemFound)
         // Otherwise it's added to the shopping cart
         cartItems.add(new ShoppingCartItem(item, 1));

      facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, item.getTitle() + " added to the shopping cart",
              "You can now add more stuff if you want"));

      return "/shopping/viewItem.xhtml?faces-redirect=true&includeViewParams=true";
   }

   public String removeItemFromCart()
   {
      Item item = em.find(Item.class, getParamId("itemId"));

      for (ShoppingCartItem cartItem : cartItems)
      {
         if (cartItem.getItem().equals(item))
         {
            cartItems.remove(cartItem);
            return null;
         }
      }

      return null;
   }

   public String updateQuantity()
   {
      return null;
   }

   public String checkout()
   {
      facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order created",
              "You will receive a confirmation email"));

      return "/main";
   }

   public List<ShoppingCartItem> getCartItems()
   {
      return cartItems;
   }

   public boolean shoppingCartIsEmpty()
   {
      return getCartItems() == null || getCartItems().size() == 0;
   }

   public Float getTotal()
   {
      if (cartItems == null || cartItems.isEmpty())
         return 0f;

      Float total = 0f;

      // Sum up the quantities
      for (ShoppingCartItem cartItem : cartItems)
      {
         total += (cartItem.getSubTotal());
      }
      return total;
   }

   protected Long getParamId(String param)
   {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      return Long.valueOf(map.get(param));
   }

   // ======================================
   // = Getters & setters =
   // ======================================

   public CreditCard getCreditCard()
   {
      return creditCard;
   }

   public void setCreditCard(CreditCard creditCard)
   {
      this.creditCard = creditCard;
   }

   public CreditCardType[] getCreditCardTypes()
   {
      return CreditCardType.values();
   }
}