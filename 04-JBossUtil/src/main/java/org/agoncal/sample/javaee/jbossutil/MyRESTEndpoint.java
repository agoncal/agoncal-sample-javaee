package org.agoncal.sample.javaee.jbossutil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Path("/myendpoint")
public class MyRESTEndpoint {

	// ======================================
	// =          Business methods          =
	// ======================================

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello() {
		return "Hello from the REST endpoint";
	}
}
