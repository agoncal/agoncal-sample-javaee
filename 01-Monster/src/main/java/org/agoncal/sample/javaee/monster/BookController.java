package org.agoncal.sample.javaee.monster;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
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

    @EJB
    private Book bookEJB;

    private List<Book> books;

    // ======================================
    // =              Public Methods        =
    // ======================================

    public String doInvokeEJB() {
        books = bookEJB.createAndListBooks("TitleFromEJB");
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
