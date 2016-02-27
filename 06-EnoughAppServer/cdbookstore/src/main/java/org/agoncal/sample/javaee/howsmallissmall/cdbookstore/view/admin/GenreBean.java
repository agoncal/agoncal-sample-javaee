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

import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.Genre;

/**
 * Backing bean for Genre entities.
 * <p/>
 * This class provides CRUD functionality for all Genre entities. It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class GenreBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Genre entities
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

   private Genre genre;

   public Genre getGenre()
   {
      return this.genre;
   }

   public void setGenre(Genre genre)
   {
      this.genre = genre;
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
         this.genre = this.example;
      }
      else
      {
         this.genre = findById(getId());
      }
   }

   public Genre findById(Long id)
   {

      return this.entityManager.find(Genre.class, id);
   }

   /*
    * Support updating and deleting Genre entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.genre);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.genre);
            return "view?faces-redirect=true&id=" + this.genre.getId();
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
         Genre deletableEntity = findById(getId());

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
    * Support searching Genre entities with pagination
    */

   private int page;
   private long count;
   private List<Genre> pageItems;

   private Genre example = new Genre();

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

   public Genre getExample()
   {
      return this.example;
   }

   public void setExample(Genre example)
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
      Root<Genre> root = countCriteria.from(Genre.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
               getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
               .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Genre> criteria = builder.createQuery(Genre.class);
      root = criteria.from(Genre.class);
      TypedQuery<Genre> query = this.entityManager.createQuery(criteria
               .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
               getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Genre> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<>();

      String name = this.example.getName();
      if (name != null && !"".equals(name))
      {
         predicatesList.add(builder.like(
                  builder.lower(root.<String> get("name")),
                  '%' + name.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Genre> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Genre entities (e.g. from inside an HtmlSelectOneMenu)
    */

   public List<Genre> getAll()
   {

      CriteriaQuery<Genre> criteria = this.entityManager.getCriteriaBuilder()
               .createQuery(Genre.class);
      return this.entityManager.createQuery(
               criteria.select(criteria.from(Genre.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final GenreBean ejbProxy = this.sessionContext
               .getBusinessObject(GenreBean.class);

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

            return String.valueOf(((Genre) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Genre add = new Genre();

   public Genre getAdd()
   {
      return this.add;
   }

   public Genre getAdded()
   {
      Genre added = this.add;
      this.add = new Genre();
      return added;
   }
}
