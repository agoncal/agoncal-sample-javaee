package org.agoncal.sample.javaee.tierarchitecture.ejbcentric.view;

import org.agoncal.sample.javaee.tierarchitecture.ejbcentric.model.Book;
import org.agoncal.sample.javaee.tierarchitecture.ejbcentric.service.BookService;

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

    @Inject
    private BookService bookService;

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

        return bookService.findById(id);
    }

    // Support updating and deleting Book entities
    public String update() {
        conversation.end();

        try {
            if (id == null) {
                bookService.create(book);
                return "search?faces-redirect=true";
            } else {
                bookService.update(book);
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

            bookService.delete(deletableEntity);
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

        count = bookService.count(example);
        pageItems = bookService.page(example, page, getPageSize());

    }

    public List<Book> getAll() {

        return bookService.findAll();
    }

    public Converter getConverter() {

        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {

                return bookService.findById(Long.valueOf(value));
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