package org.agoncal.sample.javaee.tierarchitecture.ejbcentric.service;

import org.agoncal.sample.javaee.tierarchitecture.ejbcentric.model.Book;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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

    public void persist(Book entity) {
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
}
