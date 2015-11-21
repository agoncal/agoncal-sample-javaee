package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.view.util;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

public class ServiceRegistry {

    private static String[] topBooksServiceURLs = {
            "http://localhost:8080/hsisTopBooks/topbooks",
            "http://localhost:7001/hsisTopBooks/topbooks",
            "http://localhost:7002/hsisTopBooks/topbooks"};

    private static String[] topCDsServiceURLs = {
            "http://localhost:8080/hsisTopCDs/topcds",
            "http://localhost:7001/hsisTopCDs/topcds",
            "http://localhost:7002/hsisTopCDs/topcds"};

    public static String getTopBooksServiceURL() {
        try {
            for (String topBooksServiceURL : topBooksServiceURLs) {
                Response response = ClientBuilder.newClient().target(topBooksServiceURL).request().get();
                if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                    return topBooksServiceURL;
                }
            }
        } catch (Exception e){
            return null;
        }
        return null;
    }

    public static String getTopCDsServiceURL() {
        try {
            for (String topCDsServiceURL : topCDsServiceURLs) {
                Response response = ClientBuilder.newClient().target(topCDsServiceURL).request().get();
                if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                    return topCDsServiceURL;
                }
            }
        } catch (Exception e){
            return null;
        }
        return null;
    }
}
