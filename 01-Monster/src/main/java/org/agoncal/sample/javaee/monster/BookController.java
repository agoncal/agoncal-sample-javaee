package org.agoncal.sample.javaee.monster;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

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

    private String books;

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

    public String getBooks() {
        return books;
    }

    public void setBooks(String books) {
        this.books = books;
    }
}