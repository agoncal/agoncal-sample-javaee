package org.agoncal.application.javaone2013.view;

import org.agoncal.application.javaone2013.model.Talk;

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
import javax.validation.constraints.NotNull;
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
public class TalkBean implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    private Long id;
    private Talk talk;

    // Support searching Book entities with pagination
    private int page;
    private long count;
    private List<Talk> pageItems;
    private Talk example = new Talk();

    // Support adding children to bidirectional, one-to-many tables
    private Talk add = new Talk();

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
            this.talk = this.example;
        } else {
            this.talk = findById(getId());
        }
    }

    public Talk findById(@NotNull Long id) {

        return this.entityManager.find(Talk.class, id);
    }

    // Support updating and deleting Book entities
    public String update() {
        this.conversation.end();

        try {
            if (this.id == null) {
                this.entityManager.persist(this.talk);
                return "search?faces-redirect=true";
            } else {
                this.entityManager.merge(this.talk);
                return "view?faces-redirect=true&id=" + this.talk.getId();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        this.conversation.end();

        try {
            Talk deletableEntity = findById(getId());

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
        Root<Talk> root = countCriteria.from(Talk.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        this.count = this.entityManager.createQuery(countCriteria).getSingleResult();

        // Populate this.pageItems

        CriteriaQuery<Talk> criteria = builder.createQuery(Talk.class);
        root = criteria.from(Talk.class);
        TypedQuery<Talk> query = this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
        this.pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(Root<Talk> root) {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        String title = this.example.getTitle();
        if (title != null && !"".equals(title)) {
            predicatesList.add(builder.like(root.<String>get("title"), '%' + title + '%'));
        }
        String description = this.example.getDescription();
        if (description != null && !"".equals(description)) {
            predicatesList.add(builder.like(root.<String>get("description"), '%' + description + '%'));
        }
        String room = this.example.getRoom();
        if (room != null && !"".equals(room)) {
            predicatesList.add(builder.like(root.<String>get("room"), '%' + room + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

   /*
    * Support listing and POSTing back Book entities (e.g. from inside an HtmlSelectOneMenu)
    */

    public List<Talk> getAll() {

        CriteriaQuery<Talk> criteria = this.entityManager.getCriteriaBuilder().createQuery(Talk.class);
        return this.entityManager.createQuery(criteria.select(criteria.from(Talk.class))).getResultList();
    }

    public Converter getConverter() {

        final TalkBean ejbProxy = this.sessionContext.getBusinessObject(TalkBean.class);

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

                return String.valueOf(((Talk) value).getId());
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

    public Talk getTalk() {
        return this.talk;
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

    public Talk getExample() {
        return this.example;
    }

    public void setExample(Talk example) {
        this.example = example;
    }

    public List<Talk> getPageItems() {
        return this.pageItems;
    }

    public long getCount() {
        return this.count;
    }

    public Talk getAdd() {
        return this.add;
    }

    public Talk getAdded() {
        Talk added = this.add;
        this.add = new Talk();
        return added;
    }
}