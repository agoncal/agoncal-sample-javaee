package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.view.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.Musician;

/**
 * Backing bean for Musician entities.
 * <p/>
 * This class provides CRUD functionality for all Musician entities. It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class MusicianBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Musician entities
    */

   private Long id;

   public Long getId()
   {
      return this.id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   private Musician musician;

   public Musician getMusician()
   {
      return this.musician;
   }

   public void setMusician(Musician musician)
   {
      this.musician = musician;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(unitName = "hsisCDBookStorePU", type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      this.conversation.setTimeout(1800000L);
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
         this.conversation.setTimeout(1800000L);
      }

      if (this.id == null)
      {
         this.musician = this.example;
      }
      else
      {
         this.musician = findById(getId());
      }
   }

   public Musician findById(Long id)
   {

      return this.entityManager.find(Musician.class, id);
   }

   /*
    * Support updating and deleting Musician entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.musician);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.musician);
            return "view?faces-redirect=true&id=" + this.musician.getId();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null,
                  new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         Musician deletableEntity = findById(getId());

         this.entityManager.remove(deletableEntity);
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null,
                  new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching Musician entities with pagination
    */

   private int page;
   private long count;
   private List<Musician> pageItems;

   private Musician example = new Musician();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public Musician getExample()
   {
      return this.example;
   }

   public void setExample(Musician example)
   {
      this.example = example;
   }

   public String search()
   {
      this.page = 0;
      return null;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<Musician> root = countCriteria.from(Musician.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
               getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
               .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Musician> criteria = builder.createQuery(Musician.class);
      root = criteria.from(Musician.class);
      TypedQuery<Musician> query = this.entityManager.createQuery(criteria
               .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
               getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Musician> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<>();

      String firstName = this.example.getFirstName();
      if (firstName != null && !"".equals(firstName))
      {
         predicatesList.add(builder.like(
                  builder.lower(root.<String> get("firstName")),
                  '%' + firstName.toLowerCase() + '%'));
      }
      String lastName = this.example.getLastName();
      if (lastName != null && !"".equals(lastName))
      {
         predicatesList.add(builder.like(
                  builder.lower(root.<String> get("lastName")),
                  '%' + lastName.toLowerCase() + '%'));
      }
      String bio = this.example.getBio();
      if (bio != null && !"".equals(bio))
      {
         predicatesList.add(builder.like(
                  builder.lower(root.<String> get("bio")),
                  '%' + bio.toLowerCase() + '%'));
      }
      Integer age = this.example.getAge();
      if (age != null && age.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("age"), age));
      }
      String preferredInstrument = this.example.getPreferredInstrument();
      if (preferredInstrument != null && !"".equals(preferredInstrument))
      {
         predicatesList.add(builder.like(
                  builder.lower(root.<String> get("preferredInstrument")),
                  '%' + preferredInstrument.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Musician> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Musician entities (e.g. from inside an HtmlSelectOneMenu)
    */

   public List<Musician> getAll()
   {

      CriteriaQuery<Musician> criteria = this.entityManager
               .getCriteriaBuilder().createQuery(Musician.class);
      return this.entityManager.createQuery(
               criteria.select(criteria.from(Musician.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final MusicianBean ejbProxy = this.sessionContext
               .getBusinessObject(MusicianBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
                  UIComponent component, String value)
         {

            return ejbProxy.findById(Long.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
                  UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((Musician) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Musician add = new Musician();

   public Musician getAdd()
   {
      return this.add;
   }

   public Musician getAdded()
   {
      Musician added = this.add;
      this.add = new Musician();
      return added;
   }
}
