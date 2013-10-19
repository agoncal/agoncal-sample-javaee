package org.agoncal.sample.javaee.tierarchitecture.ejbcentric.view;

import org.agoncal.sample.javaee.tierarchitecture.ejbcentric.model.Book;

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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Named
@Stateful
@ConversationScoped
public class BookBean implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    private Long id;
    private Book book;

    // Support searching Book entities with pagination
    private int page;
    private long count;
    private List<Book> pageItems;
    private Book example = new Book();

    // Support adding children to bidirectional, one-to-many tables
    private Book add = new Book();

    @Inject
    private Conversation conversation;

    @PersistenceContext(unitName = "sampleJavaEEEJBCentricPU", type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Resource
    private SessionContext sessionContext;

    // ======================================
    // =          Business Methods          =
    // ======================================

    public String create() {

        conversation.begin();
        return "create?faces-redirect=true";
    }

    public void retrieve() {

        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        if (conversation.isTransient()) {
            conversation.begin();
        }

        if (id == null) {
            book = example;
        } else {
            book = findById(getId());
        }
    }

    public Book findById(Long id) {

        return em.find(Book.class, id);
    }

    // Support updating and deleting Book entities
    public String update() {
        conversation.end();

        try {
            if (id == null) {
                em.persist(book);
                return "search?faces-redirect=true";
            } else {
                em.merge(book);
                return "view?faces-redirect=true&id=" + book.getId();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        conversation.end();

        try {
            Book deletableEntity = findById(getId());

            em.remove(deletableEntity);
            em.flush();
            return "search?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public void search() {
        page = 0;
    }

    public void paginate() {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        // Populate count

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Book> root = countCriteria.from(Book.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        count = em.createQuery(countCriteria).getSingleResult();

        // Populate pageItems

        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        root = criteria.from(Book.class);
        TypedQuery<Book> query = em.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(page * getPageSize()).setMaxResults(getPageSize());
        pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(Root<Book> root) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        String isbn = example.getIsbn();
        if (isbn != null && !"".equals(isbn)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("isbn")), '%' + isbn.toLowerCase() + '%'));
        }
        String title = example.getTitle();
        if (title != null && !"".equals(title)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("title")), '%' + title.toLowerCase() + '%'));
        }
        String description = example.getDescription();
        if (description != null && !"".equals(description)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("description")), '%' + description.toLowerCase() + '%'));
        }
        String publisher = example.getPublisher();
        if (publisher != null && !"".equals(publisher)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("publisher")), '%' + publisher.toLowerCase() + '%'));
        }
        Integer nbOfPages = example.getNbOfPages();
        if (nbOfPages != null && nbOfPages != 0) {
            predicatesList.add(builder.equal(root.get("nbOfPages"), nbOfPages));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    public List<Book> getAll() {

        CriteriaQuery<Book> criteria = em.getCriteriaBuilder().createQuery(Book.class);
        return em.createQuery(criteria.select(criteria.from(Book.class))).getResultList();
    }

    public Converter getConverter() {

        final BookBean ejbProxy = sessionContext.getBusinessObject(BookBean.class);

        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {

                return ejbProxy.findById(Long.valueOf(value));
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {

                if (value == null) {
                    return "";
                }

                return String.valueOf(((Book) value).getId());
            }
        };
    }

    // ======================================
    // =          Getters & Setters         =
    // ======================================

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return this.book;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return 10;
    }

    public Book getExample() {
        return this.example;
    }

    public void setExample(Book example) {
        this.example = example;
    }

    public List<Book> getPageItems() {
        return this.pageItems;
    }

    public long getCount() {
        return this.count;
    }

    public Book getAdd() {
        return this.add;
    }

    public Book getAdded() {
        Book added = this.add;
        this.add = new Book();
        return added;
    }
}