package org.agoncal.sample.javaee.tierarchitecture.ejbcentric.service;

import org.agoncal.sample.javaee.tierarchitecture.ejbcentric.model.Author;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Stateless
public class AuthorService {

    // ======================================
    // =             Attributes             =
    // ======================================

    @PersistenceContext(unitName = "sampleJavaEEEJBCentricPU")
    private EntityManager em;

    // ======================================
    // =          Business Methods          =
    // ======================================

    public void create(Author entity) {
        em.persist(entity);
    }

    public Author findById(Long id) {
        return em.find(Author.class, id);
    }

    public void remove(Author deletableEntity) {
        em.remove(em.merge(deletableEntity));
    }

    public Author findByIdWithRelations(Long id) {
        TypedQuery<Author> findByIdQuery = em.createQuery("SELECT DISTINCT a FROM Author a WHERE a.id = :entityId ORDER BY a.id", Author.class);
        findByIdQuery.setParameter("entityId", id);
        Author entity = findByIdQuery.getSingleResult();
        return entity;
    }

    public List<Author> findAll() {
        return em.createQuery("SELECT DISTINCT a FROM Author a ORDER BY a.id", Author.class).getResultList();
    }

    public void update(Author entity) {
        em.merge(entity);
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

    public long count(Author example) {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        // Populate count

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Author> root = countCriteria.from(Author.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, example));
        long count = em.createQuery(countCriteria).getSingleResult();
        return count;
    }

    public List<Author> page(Author example, int page, int pageSize) {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        // Populate pageItems

        CriteriaQuery<Author> criteria = builder.createQuery(Author.class);
        Root<Author> root = criteria.from(Author.class);
        TypedQuery<Author> query = em.createQuery(criteria.select(root).where(getSearchPredicates(root, example)));
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        List<Author> pageItems = query.getResultList();
        return pageItems;

    }
}
