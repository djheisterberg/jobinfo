package edu.osc.jobinfo.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.openejb.OpenEjbContainer;

public class JobInfoRESTServer {

   public static void main(String[] args) throws Exception {
      Properties properties = new Properties();
      properties.setProperty(OpenEjbContainer.OPENEJB_EMBEDDED_REMOTABLE, "true");
      Context ctxt = EJBContainer.createEJBContainer(properties).getContext();

      System.out.println("Ready...");
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
      String line = br.readLine();
      while (line != null) {
         line = line.trim();
         if (!line.isEmpty())
            break;
         line = br.readLine();
      }
      br.close();
      System.out.println("...Done");
      ctxt.close();
   }
}
