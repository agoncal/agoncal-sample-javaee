package org.agoncal.sample.javaee.monster;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.*;
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
@Path("/MonsterRest")
//@WebService
@Stateless
@WebServlet(urlPatterns = "/MonsterServlet")
@Entity
@Table(name = "MonsterEntity")
@XmlRootElement(name = "MonsterXML")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = "findAll", query = "SELECT c FROM Book c")
public class Book extends HttpServlet {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    @GeneratedValue
    private Long id;
    private String isbn;
    private Integer nbOfPage;
    private Boolean illustrations;
    private String contentLanguage;
    @Column(nullable = false)
    @Size(min = 5, max = 50)
    @NotNull
    @XmlElement(nillable = false)
    private String title;
    private Float price;
    @Column(length = 2000)
    @Size(max = 2000)
    private String description;
    @ElementCollection
    @CollectionTable(name = "tags")
    private List<String> tags = new ArrayList<>();


    @XmlTransient
    @Transient
    @EJB
    Book monsterEJB;

    @XmlTransient
    @Transient
    @PersistenceContext(unitName = "monsterPU")
    private EntityManager em;

    // ======================================
    // =        Servlet Entry Point         =
    // ======================================

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");    // if null, validation will fail
        try {
            response.getWriter().println("In Servlet calling the EJB side " + monsterEJB.listAllBooks(title));
        } catch (EJBException ee) {
            response.getWriter().println(ee.getCausedByException().getMessage());
            ee.printStackTrace();
        }
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Book> listAllBooks(String title) {
        // Sets data
        this.title = title;
        this.price = new Float(0.01);
        this.description = "The hard-coded description";
        this.isbn = "978-1-4302-1954-5";
        this.nbOfPage = 210;
        this.illustrations = Boolean.TRUE;
        List<String> tags = new ArrayList<>();
        tags.add("Monster");
        tags.add("Component");
        this.tags = tags;

        // Persists the book
        em.persist(this);

        // Returns all books
        TypedQuery<Book> query = em.createNamedQuery("findAll", Book.class);
        List<Book> allBooks = query.getResultList();
        return allBooks;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getContentLanguage() {
        return contentLanguage;
    }

    public void setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTagsAsString() {
        String s = "";
        for (String tag : tags) {
            s += tag + ", ";
        }
        if (s.length() > 2)
            return s.substring(0, s.length() - 2);
        else
            return s;
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