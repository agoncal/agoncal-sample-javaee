package org.agoncal.sample.javaee.monster;

import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class BookTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    public static final String creditCardXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><book><isbn>1234-5678-9010</isbn><nbOfPage>135</nbOfPage><illustrations>true</illustrations><contentLanguage>EN</contentLanguage><title>Dummy JAXB</title><price>10.5</price><description>JAXB Marshalling</description></book>";

    // ======================================
    // =              Unit tests            =
    // ======================================

    @Test
    public void shouldCheckJAXBMarshalling() throws Exception {

        Book book = new Book("Dummy JAXB", 10.5f, "JAXB Marshalling", "1234-5678-9010", 135, true);

        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(Book.class);
        Marshaller m = context.createMarshaller();
        m.marshal(book, writer);

        System.out.println(writer);

        assertEquals(creditCardXML, writer.toString());
    }
}
