package org.agoncal.sample.javaee.jbossutil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@WebServlet(urlPatterns = "/myservlet")
public class MyServlet extends HttpServlet {

	// ======================================
	// =        Servlet Entry Point         =
	// ======================================

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.getWriter().println("Hello from the REST endpoint");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
