package org.agoncal.sample.javaee.monster;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
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
//        assertNotNull(ctx.lookup("java:global/classes/ItemEJB!org.agoncal.sample.arquilian.wytiwyr.ItemEJB"));
//        assertNotNull(ctx.lookup("java:global/jdbc/sampleArquilianWytiwyrDS"));

        // Looks up for the EJB
        Book bookEJB = (Book) ctx.lookup("java:global/classes/Book");

        // Finds all the scifi books
        String allBooks = bookEJB.listAllBooks("dummy name");

        System.out.println("############ " + allBooks);
    }
}
