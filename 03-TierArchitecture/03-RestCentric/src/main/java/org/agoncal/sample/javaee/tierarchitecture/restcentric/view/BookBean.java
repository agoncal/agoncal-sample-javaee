package org.agoncal.sample.javaee.tierarchitecture.restcentric.view;

import org.agoncal.sample.javaee.tierarchitecture.restcentric.model.Book;

import javax.annotation.PostConstruct;
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
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
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

    @PersistenceContext(unitName = "sampleJavaEERESTCentricPU", type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    private Client client = ClientBuilder.newClient();
    private WebTarget target;

    // ======================================
    // =         Lifecycle Methods          =
    // ======================================

    @PostConstruct
    private void setWebTarget() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String restEndointURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/rest/books/";
        target = client.target(restEndointURL);
    }

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

        return target.path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_XML).get(Book.class);
    }

    // Support updating and deleting Book entities
    public String update() {
        conversation.end();

        try {
            if (id == null) {
                target.request().post(Entity.entity(book, MediaType.APPLICATION_XML));
                return "search?faces-redirect=true";
            } else {
                target.request().put(Entity.entity(book, MediaType.APPLICATION_XML));
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

            target.path("{id}").resolveTemplate("id", getId()).request(MediaType.APPLICATION_XML).delete();

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
        count = 14;//TODO target.path("count").path("{example}").resolveTemplate("example", example).request().get(Long.class);

        // Populate pageItems

        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
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

        return target.request(MediaType.APPLICATION_XML).get(List.class);
    }

    public Converter getConverter() {

        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {

                return target.path("{id}").resolveTemplate("id", Long.valueOf(value)).request(MediaType.APPLICATION_XML).get(Book.class);
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