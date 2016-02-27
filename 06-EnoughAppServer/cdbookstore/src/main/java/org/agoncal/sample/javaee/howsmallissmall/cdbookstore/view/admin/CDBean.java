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

import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.CD;
import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.Genre;
import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.Label;

/**
 * Backing bean for CD entities.
 * <p/>
 * This class provides CRUD functionality for all CD entities. It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base class.
 */

@Named("CDBean")
@Stateful
@ConversationScoped
public class CDBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving CD entities
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

   private org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.CD CD;

   public CD getCD()
   {
      return this.CD;
   }

   public void setCD(CD CD)
   {
      this.CD = CD;
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
         this.CD = this.example;
      }
      else
      {
         this.CD = findById(getId());
      }
   }

   public CD findById(Long id)
   {

      return this.entityManager.find(CD.class, id);
   }

   /*
    * Support updating and deleting CD entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.CD);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.CD);
            return "view?faces-redirect=true&id=" + this.CD.getId();
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
         CD deletableEntity = findById(getId());

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
    * Support searching CD entities with pagination
    */

   private int page;
   private long count;
   private List<CD> pageItems;

   private CD example = new CD();

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

   public CD getExample()
   {
      return this.example;
   }

   public void setExample(CD example)
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
      Root<CD> root = countCriteria.from(CD.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
               getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
               .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<CD> criteria = builder.createQuery(CD.class);
      root = criteria.from(CD.class);
      TypedQuery<CD> query = this.entityManager.createQuery(criteria.select(
               root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
               getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<CD> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<>();

      String title = this.example.getTitle();
      if (title != null && !"".equals(title))
      {
         predicatesList.add(builder.like(
                  builder.lower(root.<String> get("title")),
                  '%' + title.toLowerCase() + '%'));
      }
      String description = this.example.getDescription();
      if (description != null && !"".equals(description))
      {
         predicatesList.add(builder.like(
                  builder.lower(root.<String> get("description")),
                  '%' + description.toLowerCase() + '%'));
      }
      Label label = this.example.getLabel();
      if (label != null)
      {
         predicatesList.add(builder.equal(root.get("label"), label));
      }
      Genre genre = this.example.getGenre();
      if (genre != null)
      {
         predicatesList.add(builder.equal(root.get("genre"), genre));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<CD> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back CD entities (e.g. from inside an HtmlSelectOneMenu)
    */

   public List<CD> getAll()
   {

      CriteriaQuery<CD> criteria = this.entityManager.getCriteriaBuilder()
               .createQuery(CD.class);
      return this.entityManager.createQuery(
               criteria.select(criteria.from(CD.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final CDBean ejbProxy = this.sessionContext
               .getBusinessObject(CDBean.class);

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

            return String.valueOf(((CD) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private CD add = new CD();

   public CD getAdd()
   {
      return this.add;
   }

   public CD getAdded()
   {
      CD added = this.add;
      this.add = new CD();
      return added;
   }
}
