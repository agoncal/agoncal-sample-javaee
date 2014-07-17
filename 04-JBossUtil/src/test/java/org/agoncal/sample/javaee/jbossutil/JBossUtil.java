package org.agoncal.sample.javaee.jbossutil;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class allows to "dialog" with the JBoss administration API through HTTP. To administer JBoss we can either,
 * use the CLI ($JBOSS_HOME/bin/jboss-cli.sh), a browser or curl. For example, to know the status of the JBoss server
 * JBoss (running) we can :
 * <ul>
 * <li>Cli : :read-attribute(name=server-state)</li>
 * <li>Browser :
 * http://user:password@localhost:9990/management?operation=attribute&name=server-state</li>
 * <li>cUrl : curl --digest
 * 'http://user:password@localhost:9990/management' --header "Content-Type: application/json" -d
 * '{"operation":"read-attribute","name":"server-state","json.pretty":1}'</li>
 * </ul>
 * <p/>
 * This utility class sends HTTP requests to the JBoss administration REST APIs and to know if JBoss is up and running, if the web application (war/ear) is
 * deployed, if the server is listening on an HTTP port.... For this class to work, it needs to connect to a JBoss in localhost on
 * port 9990 in DIGEST mode. User and password are set on variables JBOSS_ADMIN_USER and JBOSS_ADMIN_PASSWORD.
 */
public class JBossUtil {

	public static final String JBOSS_ADMIN_USER = "admin";
	public static final String JBOSS_ADMIN_PASSWORD = "admin";
	public static final String JBOSS_MANAGEMENT_URL = "http://localhost:9990/management";

	/**
	 * This method returns a connected HTTP client (user/password). This HTTP client is then used in all the following methods.
	 *
	 * @return A RestEasy HTTP client
	 */
	private static ResteasyClient getClient() {
		// Setting digest credentials
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(JBOSS_ADMIN_USER, JBOSS_ADMIN_PASSWORD);
		credentialsProvider.setCredentials(AuthScope.ANY, credentials);
		HttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
		ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpclient, true);

		// Creating HTTP client
		return new ResteasyClientBuilder().httpEngine(engine).build();
	}


	// Used to test this class
	public static void main(String[] args) {
		System.out.println(isJBossUpAndRunning());
		System.out.println(isJBoss620EAP());
		System.out.println(isWebappDeployed("myWay"));
		System.out.println(isDatasourceDeployed("myDS"));
		System.out.println(isHTTPListenerOk());
	}

	/**
	 * This method checks that JBoss is up and running by doing an HTTP call and checking the body returns "running". This method is equivalent to the following command :
	 * <p/>
	 * <ul>
	 * <li>Cli :   :read-attribute(name=server-state)</li>
	 * <li>Browser : http://localhost:9990/management?operation=attribute&name=server-state</li>
	 * <li>Curl    : curl --digest 'http://user:password@localhost:9990/management' --header "Content-Type:application/json" -d '{"operation":"read-attribute","name":"server-state","json.pretty":1}'</li>
	 * </ul>
	 */
	public static boolean isJBossUpAndRunning() {

		Response response;
		try {
			WebTarget target = getClient().target(JBOSS_MANAGEMENT_URL).queryParam("operation", "attribute").queryParam("name", "server-state");
			response = target.request(MediaType.APPLICATION_JSON).get();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return response.getStatus() == Response.Status.OK.getStatusCode() && response.readEntity(String.class).contains("running");

	}

	/**
	 * This method checks that JBoss is version "6.2.0.GA". This method is equivalent to the following command :
	 * <p/>
	 * <ul>
	 * <li>CLI :   :read-attribute(name=product-version)</li>
	 * <li>Browser : http://user:password@localhost:9990/management/?operation=attribute&name=product-version</li>
	 * <li>Curl    : curl --digest 'http://user:password@localhost:9990/management' --header "Content-Type:application/json" -d '{"operation":"read-attribute","name":"product-version","json.pretty":1}'</li>
	 * </ul>
	 */
	public static boolean isJBoss620EAP() {

		Response response;
		try {
			WebTarget target = getClient().target(JBOSS_MANAGEMENT_URL).queryParam("operation", "attribute").queryParam("name", "product-version");
			response = target.request(MediaType.APPLICATION_JSON).get();
		} catch (Exception e) {
			return false;
		}

		return response.getStatus() == Response.Status.OK.getStatusCode() && response.readEntity(String.class).contains("6.2.0.GA");
	}


	/**
	 * This method checks that the war/ear is deployed. This method is equivalent to the following command :
	 * <p/>
	 * <ul>
	 * <li>CLI     : /deployment=dsn-ihm-ear-1.0.0-m2-a4-SNAPSHOT.ear:read-attribute(name=status)</li>
	 * <li>Browser : http://user:password@localhost:9990/management/deployment/dsn-ihm-ear-1.0.0-m2-a4-SNAPSHOT.ear?operation=attribute&name=status</li>
	 * <li>Curl    : curl --digest 'http://user:password@localhost:9990/management' --header "Content-Type:application/json" -d '{"address":[{"deployment":"dsn-ihm-ear-1.0.0-m2-a4-SNAPSHOT.ear"}],"operation":"read-attribute","name": "enabled","json.pretty":1}'</li>
	 * </ul>
	 */
	public static boolean isWebappDeployed(String warName) {

		Response response;
		try {
			WebTarget target = getClient().target(JBOSS_MANAGEMENT_URL).path("deployment").path(warName).queryParam("operation", "attribute").queryParam("name", "status");
			response = target.request(MediaType.APPLICATION_JSON).get();
		} catch (Exception e) {
			return false;
		}

		return response.getStatus() == Response.Status.OK.getStatusCode() && response.readEntity(String.class).contains("OK");
	}

	/**
	 * This method checks that the datasource is enabled. This method is equivalent to the following command :
	 * <p/>
	 * <ul>
	 * <li>CLI     : /subsystem=datasources/data-source=dsnDS:read-attribute(name=enabled)</li>
	 * <li>Browser : http://user:password@localhost:9990/management/subsystem/datasources/data-source/dsnDS?operation=attribute&name=enabled</li>
	 * <li>Curl    : curl --digest 'http://user:password@localhost:9990/management' --header "Content-Type:application/json" -d '{"address":["subsystem","datasources","data-source","dsnDS"], "operation":"read-attribute","name": "enabled","json.pretty":1}'</li>
	 * </ul>
	 */
	public static boolean isDatasourceDeployed(String datasourceName) {

		Response response;
		try {

			WebTarget target = getClient().target(JBOSS_MANAGEMENT_URL).path("subsystem").path("datasources").path("data-source").path(datasourceName).queryParam("operation", "attribute").queryParam("name", "enabled");
			response = target.request(MediaType.APPLICATION_JSON).get();
		} catch (Exception e) {
			return false;
		}

		return response.getStatus() == Response.Status.OK.getStatusCode() && response.readEntity(String.class).contains("true");
	}

	/**
	 * This method checks that the HTTP listener is ok. This method is equivalent to the following command :
	 * <p/>
	 * <ul>
	 * <li>CLI     : /subsystem=web/connector=http:read-attribute(name=enabled)</li>
	 * <li>Browser : http://user:password@localhost:9990/management/subsystem/web/connector/http?operation=attribute&name=enabled</li>
	 * <li>Curl    : curl --digest 'http://user:password@localhost:9990/management' --header "Content-Type:application/json" -d '{"address":["subsystem","web","connector","http"], "operation":"read-attribute", "name":"enabled","json.pretty":1}'</li>
	 * </ul>
	 */
	public static boolean isHTTPListenerOk() {

		Response response;
		try {

			WebTarget target = getClient().target(JBOSS_MANAGEMENT_URL).path("subsystem").path("web").path("connector").path("http").queryParam("operation", "attribute").queryParam("name", "enabled");
			response = target.request(MediaType.APPLICATION_JSON).get();
		} catch (Exception e) {
			return false;
		}

		return response.getStatus() == Response.Status.OK.getStatusCode() && response.readEntity(String.class).contains("true");
	}
}
