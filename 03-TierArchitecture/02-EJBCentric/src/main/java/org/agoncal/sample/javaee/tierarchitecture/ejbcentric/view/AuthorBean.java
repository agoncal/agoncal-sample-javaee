package org.agoncal.sample.javaee.tierarchitecture.ejbcentric.view;

import org.agoncal.sample.javaee.tierarchitecture.ejbcentric.model.Author;
import org.agoncal.sample.javaee.tierarchitecture.ejbcentric.service.AuthorService;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Named
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

    @Inject
    private AuthorService authorService;

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

        return authorService.findById(id);
    }

    // Support updating and deleting Book entities
    public String update() {
        conversation.end();

        try {
            if (id == null) {
                authorService.create(author);
                return "search?faces-redirect=true";
            } else {
                authorService.update(author);
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
            Author deletableEntity = authorService.findById(getId());

            authorService.remove(deletableEntity);
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

        count = authorService.count(example);
        pageItems = authorService.page(example, page, getPageSize());

    }

    public List<Author> getAll() {

        return authorService.findAll();
    }

    public Converter getConverter() {

        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {

                return authorService.findById(Long.valueOf(value));
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