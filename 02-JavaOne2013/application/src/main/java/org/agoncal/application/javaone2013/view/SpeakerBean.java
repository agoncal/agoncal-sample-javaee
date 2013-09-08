package org.agoncal.application.javaone2013.view;

import org.agoncal.application.javaone2013.model.Speaker;

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
public class SpeakerBean implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    private Long id;
    private Speaker speaker;

    // Support searching Book entities with pagination
    private int page;
    private long count;
    private List<Speaker> pageItems;
    private Speaker example = new Speaker();

    // Support adding children to bidirectional, one-to-many tables
    private Speaker add = new Speaker();

    @Inject
    private Conversation conversation;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @Resource
    private SessionContext sessionContext;

    // ======================================
    // =          Business Methods          =
    // ======================================

    public String create() {

        this.conversation.begin();
        return "create?faces-redirect=true";
    }

    public void retrieve() {

        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        if (this.conversation.isTransient()) {
            this.conversation.begin();
        }

        if (this.id == null) {
            this.speaker = this.example;
        } else {
            this.speaker = findById(getId());
        }
    }

    public Speaker findById(Long id) {

        return this.entityManager.find(Speaker.class, id);
    }

    // Support updating and deleting Book entities
    public String update() {
        this.conversation.end();

        try {
            if (this.id == null) {
                this.entityManager.persist(this.speaker);
                return "search?faces-redirect=true";
            } else {
                this.entityManager.merge(this.speaker);
                return "view?faces-redirect=true&id=" + this.speaker.getId();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        this.conversation.end();

        try {
            Speaker deletableEntity = findById(getId());

            this.entityManager.remove(deletableEntity);
            this.entityManager.flush();
            return "search?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public void search() {
        this.page = 0;
    }

    public void paginate() {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

        // Populate this.count

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Speaker> root = countCriteria.from(Speaker.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        this.count = this.entityManager.createQuery(countCriteria).getSingleResult();

        // Populate this.pageItems

        CriteriaQuery<Speaker> criteria = builder.createQuery(Speaker.class);
        root = criteria.from(Speaker.class);
        TypedQuery<Speaker> query = this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
        this.pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(Root<Speaker> root) {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        String firstname = this.example.getFirstname();
        if (firstname != null && !"".equals(firstname)) {
            predicatesList.add(builder.like(root.<String>get("firstname"), '%' + firstname + '%'));
        }
        String surname = this.example.getSurname();
        if (surname != null && !"".equals(surname)) {
            predicatesList.add(builder.like(root.<String>get("surname"), '%' + surname + '%'));
        }
        String bio = this.example.getBio();
        if (bio != null && !"".equals(bio)) {
            predicatesList.add(builder.like(root.<String>get("bio"), '%' + bio + '%'));
        }
        String twitter = this.example.getTwitter();
        if (twitter != null && !"".equals(twitter)) {
            predicatesList.add(builder.like(root.<String>get("twitter"), '%' + twitter + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

   /*
    * Support listing and POSTing back Book entities (e.g. from inside an HtmlSelectOneMenu)
    */

    public List<Speaker> getAll() {

        CriteriaQuery<Speaker> criteria = this.entityManager.getCriteriaBuilder().createQuery(Speaker.class);
        return this.entityManager.createQuery(criteria.select(criteria.from(Speaker.class))).getResultList();
    }

    public Converter getConverter() {

        final SpeakerBean ejbProxy = this.sessionContext.getBusinessObject(SpeakerBean.class);

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

                return String.valueOf(((Speaker) value).getId());
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

    public Speaker getSpeaker() {
        return this.speaker;
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

    public Speaker getExample() {
        return this.example;
    }

    public void setExample(Speaker example) {
        this.example = example;
    }

    public List<Speaker> getPageItems() {
        return this.pageItems;
    }

    public long getCount() {
        return this.count;
    }

    public Speaker getAdd() {
        return this.add;
    }

    public Speaker getAdded() {
        Speaker added = this.add;
        this.add = new Speaker();
        return added;
    }
}