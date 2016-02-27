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

import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.Author;
import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.Language;

/**
 * Backing bean for Author entities.
 * <p/>
 * This class provides CRUD functionality for all Author entities. It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class AuthorBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Author entities
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

   private Author author;

   public Author getAuthor()
   {
      return this.author;
   }

   public void setAuthor(Author author)
   {
      this.author = author;
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
         this.author = this.example;
      }
      else
      {
         this.author = findById(getId());
      }
   }

   public Author findById(Long id)
   {

      return this.entityManager.find(Author.class, id);
   }

   /*
    * Support updating and deleting Author entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.author);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.author);
            return "view?faces-redirect=true&id=" + this.author.getId();
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
         Author deletableEntity = findById(getId());

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
    * Support searching Author entities with pagination
    */

   private int page;
   private long count;
   private List<Author> pageItems;

   private Author example = new Author();

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

   public Author getExample()
   {
      return this.example;
   }

   public void setExample(Author example)
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
      Root<Author> root = countCriteria.from(Author.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
               getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
               .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Author> criteria = builder.createQuery(Author.class);
      root = criteria.from(Author.class);
      TypedQuery<Author> query = this.entityManager.createQuery(criteria
               .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
               getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Author> root)
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
      Language preferredLanguage = this.example.getPreferredLanguage();
      if (preferredLanguage != null)
      {
         predicatesList.add(builder.equal(root.get("preferredLanguage"),
                  preferredLanguage));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Author> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Author entities (e.g. from inside an HtmlSelectOneMenu)
    */

   public List<Author> getAll()
   {

      CriteriaQuery<Author> criteria = this.entityManager
               .getCriteriaBuilder().createQuery(Author.class);
      return this.entityManager.createQuery(
               criteria.select(criteria.from(Author.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final AuthorBean ejbProxy = this.sessionContext
               .getBusinessObject(AuthorBean.class);

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

            return String.valueOf(((Author) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Author add = new Author();

   public Author getAdd()
   {
      return this.add;
   }

   public Author getAdded()
   {
      Author added = this.add;
      this.add = new Author();
      return added;
   }
}
