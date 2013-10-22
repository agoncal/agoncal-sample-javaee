package org.agoncal.sample.javaee.tierarchitecture.restcentric.view;

import org.agoncal.sample.javaee.tierarchitecture.restcentric.model.Author;

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
public class AuthorBean implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    private Long id;
    private Author author;

    // Support searching Book entities with pagination
    private int page;
    private long count;
    private List<Author> pageItems;
    private Author example = new Author();

    // Support adding children to bidirectional, one-to-many tables
    private Author add = new Author();

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
        String restEndointURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/rest/authors/";
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
            author = example;
        } else {
            author = findById(getId());
        }
    }

    public Author findById(Long id) {

        return target.path("{id}").resolveTemplate("id", id).request(MediaType.APPLICATION_XML).get(Author.class);
    }

    // Support updating and deleting Book entities
    public String update() {
        conversation.end();

        try {
            if (id == null) {
                target.request().post(Entity.entity(author, MediaType.APPLICATION_XML));
                return "search?faces-redirect=true";
            } else {
                target.request().put(Entity.entity(author, MediaType.APPLICATION_XML));
                return "view?faces-redirect=true&id=" + author.getId();
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

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Author> root = countCriteria.from(Author.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        count = em.createQuery(countCriteria).getSingleResult();

        // Populate pageItems

        CriteriaQuery<Author> criteria = builder.createQuery(Author.class);
        root = criteria.from(Author.class);
        TypedQuery<Author> query = em.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(page * getPageSize()).setMaxResults(getPageSize());
        pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(Root<Author> root) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        String firstname = example.getFirstname();
        if (firstname != null && !"".equals(firstname)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("firstname")), '%' + firstname.toLowerCase() + '%'));
        }
        String surname = example.getSurname();
        if (surname != null && !"".equals(surname)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("surname")), '%' + surname.toLowerCase() + '%'));
        }
        String bio = example.getBio();
        if (bio != null && !"".equals(bio)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("bio")), '%' + bio.toLowerCase() + '%'));
        }
        String twitter = example.getTwitter();
        if (twitter != null && !"".equals(twitter)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("twitter")), '%' + twitter.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    public List<Author> getAll() {

        return target.request(MediaType.APPLICATION_XML).get(List.class);
    }

    public Converter getConverter() {

        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {

                return target.path("{id}").resolveTemplate("id", Long.valueOf(value)).request(MediaType.APPLICATION_XML).get(Author.class);
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {

                if (value == null) {
                    return "";
                }

                return String.valueOf(((Author) value).getId());
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

    public Author getAuthor() {
        return this.author;
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

    public Author getExample() {
        return this.example;
    }

    public void setExample(Author example) {
        this.example = example;
    }

    public List<Author> getPageItems() {
        return this.pageItems;
    }

    public long getCount() {
        return this.count;
    }

    public Author getAdd() {
        return this.add;
    }

    public Author getAdded() {
        Author added = this.add;
        this.add = new Author();
        return added;
    }
}