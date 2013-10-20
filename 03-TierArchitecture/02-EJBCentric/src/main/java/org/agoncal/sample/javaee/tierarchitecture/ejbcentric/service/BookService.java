package org.agoncal.sample.javaee.tierarchitecture.ejbcentric.service;

import org.agoncal.sample.javaee.tierarchitecture.ejbcentric.model.Book;

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
public class BookService {

    // ======================================
    // =             Attributes             =
    // ======================================

    @PersistenceContext(unitName = "sampleJavaEEEJBCentricPU")
    private EntityManager em;

    // ======================================
    // =          Business Methods          =
    // ======================================

    public void create(Book entity) {
        em.persist(entity);
    }

    public Book findById(Long id) {
        return em.find(Book.class, id);
    }

    public void remove(Book deletableEntity) {
        em.remove(em.merge(deletableEntity));
    }

    public Book findByIdWithRelations(Long id) {
        TypedQuery<Book> findByIdQuery = em.createQuery("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors WHERE b.id = :entityId ORDER BY b.id", Book.class);
        findByIdQuery.setParameter("entityId", id);
        Book entity = findByIdQuery.getSingleResult();
        return entity;
    }

    public List<Book> findAll() {
        return em.createQuery("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors ORDER BY b.id", Book.class).getResultList();
    }

    public void update(Book entity) {
        em.merge(entity);
    }

    public long count(Book example) {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        // Populate count

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Book> root = countCriteria.from(Book.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, example));
        long count = em.createQuery(countCriteria).getSingleResult();
        return count;
    }

    public List<Book> page(Book example, int page, int pageSize) {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        // Populate pageItems

        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        TypedQuery<Book> query = em.createQuery(criteria.select(root).where(getSearchPredicates(root, example)));
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
