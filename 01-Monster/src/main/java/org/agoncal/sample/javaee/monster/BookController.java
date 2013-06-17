package org.agoncal.sample.javaee.monster;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Named
@RequestScoped
public class BookController {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private Book book;

    private List<Book> books;

    // ======================================
    // =              Public Methods        =
    // ======================================

    public String doInvokeEJB() {
        books = book.listAllBooks("EJB " + new Date());
        return "showbooks.faces";
    }

    // ======================================
    // =         Getters & setters          =
    // ======================================

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}