package org.agoncal.sample.javaee.monster;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class BookIT {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static EJBContainer ec;
    private static Context ctx;

    // ======================================
    // =          Lifecycle Methods         =
    // ======================================

    @BeforeClass
    public static void initContainer() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put(EJBContainer.MODULES, new File[]{new File("target/classes"), new File("target/test-classes")});
        ec = EJBContainer.createEJBContainer(properties);
        ctx = ec.getContext();
    }

    @AfterClass
    public static void closeContainer() throws Exception {
        if (ec != null) {
            ec.close();
        }
    }

    // ======================================
    // =              Unit tests            =
    // ======================================

    @Test
    public void shouldFindAllBooksWithEJB() throws Exception {

        // Check JNDI dependencies
        assertNotNull(ctx.lookup("java:global/classes/Book"));
        assertNotNull(ctx.lookup("java:global/classes/Book!org.agoncal.sample.javaee.monster.Book"));
        assertNotNull(ctx.lookup("java:comp/DefaultDataSource"));

        // Looks up for the EJB
        Book bookEJB = (Book) ctx.lookup("java:global/classes/Book");

        // Creates and Finds all the books
        List<Book> allBooks = bookEJB.listAllBooks("TitleFromEJB1");

        int initSize = allBooks.size();

        // Creates and Finds all the books a second time
        allBooks = bookEJB.listAllBooks("TitleFromEJB1");

        assertEquals(initSize + 1, allBooks.size());

        System.out.println("############ " + allBooks);
    }
}
