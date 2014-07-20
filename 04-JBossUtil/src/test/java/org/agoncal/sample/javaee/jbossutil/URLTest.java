package org.agoncal.sample.javaee.jbossutil;

import org.junit.Assume;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class URLTest {

	// ======================================
	// =              Unit tests            =
	// ======================================

	@Test
	public void checksTheJBossAdminConsoleIsUp() throws Exception {
		Assume.assumeTrue(JBossUtil.isJBossUpAndRunning());

		Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:9990/console/App.html");
		assertEquals(Response.Status.OK.getStatusCode(), target.request(MediaType.TEXT_PLAIN).get().getStatus());
	}

    @Test
    public void checksTheJBossAdminConsoleIsNotOnTheRoot() throws Exception {
        Assume.assumeTrue(JBossUtil.isJBossUpAndRunning());

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:9990");
        assertEquals(Response.Status.MOVED_PERMANENTLY.getStatusCode(), target.request(MediaType.TEXT_PLAIN).get().getStatus());
    }

    @Test
	public void checksTheURLDoesNotExist() throws Exception {
		Assume.assumeTrue(JBossUtil.isJBossUpAndRunning());

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080").path("dummy");
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), target.request(MediaType.TEXT_PLAIN).get().getStatus());
	}

	@Test
	public void checksTheWelcomePageIsDeployed() throws Exception {
		Assume.assumeTrue(JBossUtil.isWebappDeployed("sampleJavaEEJBossUtil.war"));

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/sampleJavaEEJBossUtil").path("index.html");
		assertEquals(Response.Status.OK.getStatusCode(), target.request(MediaType.TEXT_PLAIN).get().getStatus());
	}

	@Test
	public void checksTheServletIsDeployed() throws Exception {
		Assume.assumeTrue(JBossUtil.isWebappDeployed("sampleJavaEEJBossUtil.war"));

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/sampleJavaEEJBossUtil").path("myservlet");
		assertEquals(Response.Status.OK.getStatusCode(), target.request(MediaType.TEXT_PLAIN).get().getStatus());
	}

	@Test
	public void checksTheRESTEndpointIsDeployed() throws Exception {
		Assume.assumeTrue(JBossUtil.isWebappDeployed("sampleJavaEEJBossUtil.war"));

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/sampleJavaEEJBossUtil").path("rest/myendpoint");
		assertEquals(Response.Status.OK.getStatusCode(), target.request(MediaType.TEXT_PLAIN).get().getStatus());
	}
}
