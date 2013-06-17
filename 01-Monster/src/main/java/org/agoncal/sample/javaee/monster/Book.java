package org.agoncal.sample.javaee.monster;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jws.WebService;
import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         Alexis Moussine Pouchkine
 *         Ludovic Champenois
 *         http://www.antoniogoncalves.org
 *         --
 */
//@Path("/monster")
//@WebService
@Stateless
@WebServlet(urlPatterns = "/monster")
@Entity
@XmlRootElement
@NamedQuery(name = "findAll", query = "SELECT c FROM Book c")
public class Book extends HttpServlet {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Size(max = 50, min = 5, message = "more than {min}, less than {max}")
    @NotNull
    @Transient
    @XmlTransient
    private String name;

    @XmlTransient
    @Transient
    @EJB
    Book monsterEJB;

    private String isbn;
    private Integer nbOfPage;
    private Boolean illustrations;
    private String contentLanguage;

    @Column(nullable = false)
    @Size(min = 5, max = 50)
    @XmlElement(nillable = false)
    protected String title;
    protected Float price;
    @Column(length = 2000)
    @Size(max = 2000)
    protected String description;

    @ElementCollection
    @CollectionTable(name = "tags")
    private List<String> tags = new ArrayList<>();

    @Transient
    @PersistenceContext(unitName = "monsterPU")
    private EntityManager em;

    // ======================================
    // =        Servlet Entry Point         =
    // ======================================

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("name");    // if null, validation will fail
        try {
            response.getWriter().println("In Servlet calling the EJB side " + monsterEJB.listAllBooks(param));
        } catch (EJBException ee) {
            response.getWriter().println(ee.getCausedByException().getMessage());
            ee.printStackTrace();
        }
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    public String listAllBooks(String name) {
        this.name = name;
        hardcode(name);
        em.persist(this);
        TypedQuery<Book> query = em.createNamedQuery("findAll", Book.class);
        List<Book> allBooks = query.getResultList();
        return "BusinessMethod from EJB :" + allBooks.toString();
    }

    private void hardcode(String romain) {
        this.title = romain;
        this.price = new Float(0.01);
        this.description = "The hard-coded description";
        this.isbn = "978-1-4302-1954-5";
        this.nbOfPage = 210;
        this.illustrations = Boolean.TRUE;
    }

    // ======================================
    // =            Interceptor             =
    // ======================================

    @AroundInvoke
    public Object logMethod(InvocationContext ic) throws Exception {
        System.out.println("> " + ic.getTarget().getClass() + " - " + ic.getMethod().getName());
        try {
            return ic.proceed();
        } finally {
            System.out.println("< " + ic.getTarget().getClass() + " - " + ic.getMethod().getName());
        }
    }

    // ======================================
    // =            Constructors            =
    // ======================================

    public Book() {
    }

    public Book(String title, Float price, String description, String isbn, Integer nbOfPage, Boolean illustrations) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.isbn = isbn;
        this.nbOfPage = nbOfPage;
        this.contentLanguage = "EN";
        this.illustrations = illustrations;
    }

    // ======================================
    // =          Getters & Setters         =
    // ======================================

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getNbOfPage() {
        return nbOfPage;
    }

    public void setNbOfPage(Integer nbOfPage) {
        this.nbOfPage = nbOfPage;
    }

    public Boolean getIllustrations() {
        return illustrations;
    }

    public void setIllustrations(Boolean illustrations) {
        this.illustrations = illustrations;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getTagsAsString() {
        String s = new String();
        for (String tag : tags) {
            s += tag + ", ";
        }
        if (s.length() > 2)
            return s.substring(0, s.length() - 2);
        else
            return s;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getContentLanguage() {
        return contentLanguage;
    }

    public void setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
    }

    @PostConstruct
    private void prepare() {
        System.out.println("\n=> PostConstruct");
        System.out.println("================");
    }

    @PreDestroy
    private void release() {
        System.out.println("=============");
        System.out.println("=> PreDestroy");
    }

    // ======================================
    // =         hash, equals, toString     =
    // ======================================

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Book");
        sb.append("{id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", price=").append(price);
        sb.append(", description='").append(description).append('\'');
        sb.append(", isbn='").append(isbn).append('\'');
        sb.append(", nbOfPage=").append(nbOfPage);
        sb.append(", illustrations=").append(illustrations);
        sb.append(", contentLanguage=").append(contentLanguage);
        sb.append(", tags=").append(getTagsAsString());
        sb.append('}');
        return sb.toString();
    }
}