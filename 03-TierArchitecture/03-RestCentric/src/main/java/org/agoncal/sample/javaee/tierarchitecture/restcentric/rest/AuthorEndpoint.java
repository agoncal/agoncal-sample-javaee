package org.agoncal.sample.javaee.tierarchitecture.restcentric.rest;

import org.agoncal.sample.javaee.tierarchitecture.restcentric.model.Author;

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
@Path("/authors")
public class AuthorEndpoint {

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
    public Response create(Author entity) {
        em.persist(entity);
        return Response.created(UriBuilder.fromResource(AuthorEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") Long id) {
        Author deletableEntity = em.find(Author.class, id);
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
        TypedQuery<Author> findByIdQuery = em.createQuery("SELECT DISTINCT a FROM Author a WHERE a.id = :entityId ORDER BY a.id", Author.class);
        findByIdQuery.setParameter("entityId", id);
        Author entity = findByIdQuery.getSingleResult();
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }

    @GET
    @Produces("application/xml")
    // TODO Create Authors class and return Response.ok(entity).build(); or no content
    public List<Author> findAll() {
        final List<Author> results = em.createQuery("SELECT DISTINCT a FROM Author a ORDER BY a.id", Author.class).getResultList();
        return results;
    }

    @PUT
    @Consumes("application/xml")
    public Response update(Author entity) {
        em.merge(entity);
        return Response.noContent().build();
    }

    @GET
    @Path("/count/{example}")
    public long count(@PathParam("example") Author example) {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        // Populate count

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Author> root = countCriteria.from(Author.class);
        if (example == null)
            countCriteria = countCriteria.select(builder.count(root));
        else
            countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, example));

        long count = em.createQuery(countCriteria).getSingleResult();
        return count;
    }

    public List<Author> paginate(Author example, int page, int pageSize) {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        // Populate pageItems

        CriteriaQuery<Author> criteria = builder.createQuery(Author.class);
        Root<Author> root = criteria.from(Author.class);
        TypedQuery<Author> query = em.createQuery(criteria.select(root).where(getSearchPredicates(root, example)));
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        List<Author> pageItems = query.getResultList();
        return pageItems;
    }

    private Predicate[] getSearchPredicates(Root<Author> root, Author example) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        String firstname = example.getFirstname();
        if (firstname != null && !"".equals(firstname)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("firstname")), '%' + firstname.toLowerCase() + '%'));
        }
        String surname = example.getSurname();
        if (surname != null && !"".equals(surname)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("surname")), '%' + surname.toLowerCase() + '%'));
        }
        String bio = example.getBio();
        if (bio != null && !"".equals(bio)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("bio")), '%' + bio.toLowerCase() + '%'));
        }
        String twitter = example.getTwitter();
        if (twitter != null && !"".equals(twitter)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("twitter")), '%' + twitter.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
}