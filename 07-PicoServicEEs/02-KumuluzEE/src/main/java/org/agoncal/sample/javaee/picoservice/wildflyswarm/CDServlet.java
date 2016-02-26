package org.agoncal.sample.javaee.howsmallissmall.topcds.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/topcds", displayName = "Top CDs")
public class CDServlet extends HttpServlet
{

   private Logger logger = Logger.getLogger(CDServlet.class.getName());

   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      resp.setContentType("application/json");
      PrintWriter out = resp.getWriter();
      out.print(getJsonObject());
      out.flush();
   }

   private String getJsonObject()
   {
      StringJoiner sj = new StringJoiner(", ");

      List<Integer> randomCDs = getRandomNumbers();
      for (Integer randomCD : randomCDs)
      {
         sj.add("{\"id\":" + randomCD.toString() + "}");
      }
      return "[" + sj.toString() + "]";
   }

   private List<Integer> getRandomNumbers()
   {
      List<Integer> randomCDs = new ArrayList<>();
      Random r = new Random();
      randomCDs.add(r.nextInt(100) + 1101);
      randomCDs.add(r.nextInt(100) + 1101);
      randomCDs.add(r.nextInt(100) + 1101);

      logger.info("Top CDs are " + randomCDs);

      return randomCDs;
   }
}
