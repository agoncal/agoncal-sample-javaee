package org.agoncal.application.javaone2013.script;

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

    /*
    http://webservices.amazon.com/onca/xml?
Service=AWSECommerceService&
AWSAccessKeyId=[AWS Access Key ID]&
AssociateTag=[Associate ID]&
Operation=ItemLookup&
IdType=ASIN&
ItemId=B00008OE6I&
ResponseGroup=Accessories&
Version=2011-08-01
&Timestamp=[YYYY-MM-DDThh:mm:ssZ]
&Signature=[Request Signature]
     */

    public static void main(String[] args) {

//        URI uriAmazon = UriBuilder.fromUri("http://webservices.amazon.com/onca/xml").queryParam("Service", "AWSECommerceService").queryParam("AWSAccessKeyId", "AKIAIYNLC7WME6YSY66A").queryParam("AssociateTag", "antgonblo-20").build();
        URI uriAmazon = UriBuilder.fromUri("http://free.apisigning.com/onca/xml").queryParam("Service", "AWSECommerceService").queryParam("AWSAccessKeyId", "AKIAIYNLC7WME6YSY66A").build();
        URI uriSearch = UriBuilder.fromUri(uriAmazon).queryParam("Operation","ItemSearch").build();
        URI uriSearchBooks = UriBuilder.fromUri(uriSearch).queryParam("SearchIndex","Books").build();
        Client client = ClientBuilder.newClient();

        URI uriSearchBooksByKeyword = UriBuilder.fromUri(uriSearchBooks).queryParam("Keywords","Java EE 7").build();
        URI uriSearchBooksWithResponse = UriBuilder.fromUri(uriSearchBooksByKeyword).queryParam("Condition","New").queryParam("ResponseGroup","Large").build();
//        URI uriSearchBooksWithImages = UriBuilder.fromUri(uriSearchBooks).queryParam("Condition","All").queryParam("ResponseGroup","Images").queryParam("Title","Java EE 7").build();

        System.out.println(uriSearchBooksByKeyword.toString());
        System.out.println(uriSearchBooksWithResponse.toString());

//        Response response = client.target(uriSearchBooksByKeyword).request().get();
//        System.out.println(response.getStatus());
//        System.out.println(response.readEntity(String.class));
        Response response = client.target(uriSearchBooksWithResponse).request().get();
        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
    }
}
