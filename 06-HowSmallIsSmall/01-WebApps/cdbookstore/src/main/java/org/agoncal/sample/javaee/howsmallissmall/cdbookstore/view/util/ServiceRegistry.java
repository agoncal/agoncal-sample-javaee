package org.agoncal.sample.javaee.howsmallissmall.cdbookstore.view.util;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class ServiceRegistry {

    private static Logger logger = Logger.getLogger(ServiceRegistry.class.getName());

    private static String[] topBooksServiceURLs = {
            "http://localhost:8081/hsisTopBooks/topbooks",
            "http://localhost:8082/hsisTopBooks/topbooks",
            "http://localhost:8080/hsisTopBooks/topbooks",
            "http://localhost:7001/hsisTopBooks/topbooks",
            "http://localhost:7002/hsisTopBooks/topbooks"};

    private static String[] topCDsServiceURLs = {
            "http://localhost:8081/hsisTopCDs/topcds",
            "http://localhost:8082/hsisTopCDs/topcds",
            "http://localhost:8080/hsisTopCDs/topcds",
            "http://localhost:7001/hsisTopCDs/topcds",
            "http://localhost:7002/hsisTopCDs/topcds"};

    public static String getTopBooksServiceURL() {
        return pingWorkingURL(topBooksServiceURLs);
    }

    public static String getTopCDsServiceURL() {
        return pingWorkingURL(topCDsServiceURLs);
    }

    private static String pingWorkingURL(String[] urls) {
        try {
            for (String url : urls) {
                Response response = ClientBuilder.newClient().target(url).request().get();
                if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                    logger.info("Pinged URL : " + url);
                    return url;
                }
            }
        } catch (Exception e){
            return null;
        }
        return null;
    }
}
