package org.agoncal.sample.javaee.tierarchitecture.restcentric.rest;

import org.agoncal.sample.javaee.tierarchitecture.restcentric.model.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Transactional
@Path("/books")
public class BookEndpoint {

    // ======================================
    // =             Attributes             =
    // ======================================

    @PersistenceContext(unitName = "sampleJavaEERESTCentricPU")
    private EntityManager em;

    // ======================================
    // =          Business Methods          =
    // ======================================

    @POST
    @Consumes("application/xml")
    public Response create(Book entity) {
        em.persist(entity);
        return Response.created(UriBuilder.fromResource(BookEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") Long id) {
        Book deletableEntity = em.find(Book.class, id);
        if (deletableEntity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        em.remove(deletableEntity);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/xml")
    public Response findById(@PathParam("id") Long id) {
        TypedQuery<Book> findByIdQuery = em.createQuery("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors WHERE b.id = :entityId ORDER BY b.id", Book.class);
        findByIdQuery.setParameter("entityId", id);
        Book entity = findByIdQuery.getSingleResult();
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build(); // TODO return no content instead of not found
        }
        return Response.ok(entity).build();
    }

    @GET
    @Produces("application/xml")
    // TODO Create Books class and return Response.ok(entity).build(); or no content
    public List<Book> findAll() {
        final List<Book> results = em.createQuery("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors ORDER BY b.id", Book.class).getResultList();
        return results;
    }

    @PUT
    @Consumes("application/xml")
    public Response update(Book entity) {
        entity = em.merge(entity);
        return Response.ok(entity).build();
    }

    @GET
    @Path("/count")
    @Consumes(MediaType.APPLICATION_XML)
    public long count(@QueryParam("example") Book example) {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        // Populate count

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Book> root = countCriteria.from(Book.class);

        if (example == null)
            countCriteria = countCriteria.select(builder.count(root));
        else
            countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, example));

        long count = em.createQuery(countCriteria).getSingleResult();
        return count;
    }

    @GET
    @Path("/query")
    @Consumes(MediaType.APPLICATION_XML)
    public List<Book> page(@QueryParam("example") Book example, @QueryParam("page") int page, @QueryParam("pageSize") int pageSize) {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        // Populate pageItems

        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        TypedQuery<Book> query;

        if (example == null)
            query = em.createQuery(criteria.select(root));
        else
            query = em.createQuery(criteria.select(root).where(getSearchPredicates(root, example)));

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        List<Book> pageItems = query.getResultList();
        return pageItems;
    }

    private Predicate[] getSearchPredicates(Root<Book> root, Book example) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        String isbn = example.getIsbn();
        if (isbn != null && !"".equals(isbn)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("isbn")), '%' + isbn.toLowerCase() + '%'));
        }
        String title = example.getTitle();
        if (title != null && !"".equals(title)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("title")), '%' + title.toLowerCase() + '%'));
        }
        String description = example.getDescription();
        if (description != null && !"".equals(description)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("description")), '%' + description.toLowerCase() + '%'));
        }
        String publisher = example.getPublisher();
        if (publisher != null && !"".equals(publisher)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("publisher")), '%' + publisher.toLowerCase() + '%'));
        }
        Integer nbOfPages = example.getNbOfPages();
        if (nbOfPages != null && nbOfPages != 0) {
            predicatesList.add(builder.equal(root.get("nbOfPages"), nbOfPages));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
}