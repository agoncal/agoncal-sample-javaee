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

import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.Book;
import org.agoncal.sample.javaee.howsmallissmall.cdbookstore.model.Language;

/**
 * Backing bean for Book entities.
 * <p/>
 * This class provides CRUD functionality for all Book entities. It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class BookBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Book entities
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

   private Book book;

   public Book getBook()
   {
      return this.book;
   }

   public void setBook(Book book)
   {
      this.book = book;
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
         this.book = this.example;
      }
      else
      {
         this.book = findById(getId());
      }
   }

   public Book findById(Long id)
   {

      return this.entityManager.find(Book.class, id);
   }

   /*
    * Support updating and deleting Book entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.book);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.book);
            return "view?faces-redirect=true&id=" + this.book.getId();
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
         Book deletableEntity = findById(getId());

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
    * Support searching Book entities with pagination
    */

   private int page;
   private long count;
   private List<Book> pageItems;

   private Book example = new Book();

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

   public Book getExample()
   {
      return this.example;
   }

   public void setExample(Book example)
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
      Root<Book> root = countCriteria.from(Book.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
               getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
               .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
      root = criteria.from(Book.class);
      TypedQuery<Book> query = this.entityManager.createQuery(criteria
               .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
               getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Book> root)
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
      String isbn = this.example.getIsbn();
      if (isbn != null && !"".equals(isbn))
      {
         predicatesList.add(builder.like(
                  builder.lower(root.<String> get("isbn")),
                  '%' + isbn.toLowerCase() + '%'));
      }
      Integer nbOfPage = this.example.getNbOfPage();
      if (nbOfPage != null && nbOfPage.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("nbOfPage"), nbOfPage));
      }
      Language language = this.example.getLanguage();
      if (language != null)
      {
         predicatesList.add(builder.equal(root.get("language"), language));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Book> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Book entities (e.g. from inside an HtmlSelectOneMenu)
    */

   public List<Book> getAll()
   {

      CriteriaQuery<Book> criteria = this.entityManager.getCriteriaBuilder()
               .createQuery(Book.class);
      return this.entityManager.createQuery(
               criteria.select(criteria.from(Book.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final BookBean ejbProxy = this.sessionContext
               .getBusinessObject(BookBean.class);

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

            return String.valueOf(((Book) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Book add = new Book();

   public Book getAdd()
   {
      return this.add;
   }

   public Book getAdded()
   {
      Book added = this.add;
      this.add = new Book();
      return added;
   }
}
