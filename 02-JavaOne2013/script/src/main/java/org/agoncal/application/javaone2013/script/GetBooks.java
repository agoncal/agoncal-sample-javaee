package org.agoncal.application.javaone2013.script;

import sun.awt.resources.awt_sv;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 *         Using the Amazon WebServices to get book informations
 *         https://affiliate-program.amazon.com/gp/advertising/api/detail/main.html
 */
public class GetBooks {

    public static void main(String[] args) {

        URI uriAmazon = UriBuilder.fromUri("http://free.apisigning.com/onca/xml")
                .queryParam("Service", "AWSECommerceService")
                .queryParam("AWSAccessKeyId", "AKIAIYNLC7WME6YSY66A")
                .queryParam("Operation", "ItemSearch")
                .queryParam("SearchIndex", "Books")
                .queryParam("Keywords", "Java EE 7")
                .queryParam("Condition", "New")
                .queryParam("ResponseGroup", "Large").build();

        Client client = ClientBuilder.newClient();
        Response response = client.target(uriAmazon).request().get();
        String javaee7books = response.readEntity(String.class);

        System.out.println(uriAmazon.toString());
        System.out.println(response.getStatus());
        System.out.println(javaee7books);
    }
}
