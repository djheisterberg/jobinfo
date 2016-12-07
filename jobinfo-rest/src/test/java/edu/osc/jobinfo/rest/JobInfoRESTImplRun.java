package edu.osc.jobinfo.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.openejb.OpenEjbContainer;

public class JobInfoRESTImplRun {

   private final static String DOMAIN = "localhost";
   private final static int PORT = 4204;
   private final static String URL = "http://" + DOMAIN + ":" + PORT + "/jobinfo-rest/jobinfo/rest";

   public static void main(String[] args) throws Exception {
      int nArgs = args.length;
      if ((nArgs < 2) || (nArgs > 3)) {
         System.err.println("USAGE WebSvcsJobSvcRun [authentication] job-id system");
         System.exit(1);
      }
      int iArg = 0;
      String authentication = null;
      if (nArgs == 3) {
         authentication = args[iArg++];
      }
      String id = args[iArg++];
      String system = args[iArg++];

      Properties properties = new Properties();
      properties.setProperty(OpenEjbContainer.OPENEJB_EMBEDDED_REMOTABLE, "true");
      Context ctxt = EJBContainer.createEJBContainer(properties).getContext();

      try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
         RequestBuilder getBuilder = RequestBuilder.get(URL);
         getBuilder.addParameter("id", id);
         getBuilder.addParameter("system", system);
         HttpGet httpGet = new HttpGet(getBuilder.build().getURI());
         httpGet.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
         if (authentication != null) {
            httpGet.addHeader(HttpHeaders.AUTHORIZATION, authentication);
         }

         try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            System.out.println(statusLine);
            String html = EntityUtils.toString(entity);
            System.out.println(html);
         } catch (IOException ioe) {
            System.err.println("Exception reading job html id=" + id + ", system=" + system);
            ioe.printStackTrace();
         }

      } catch (IOException ioe) {
         System.err.println("Exception closing HttpClient id=" + id + ", system=" + system);
         ioe.printStackTrace();
      }

      ctxt.close();
   }

   protected static String decodeAuthentication(String authentication) {
      try {
         authentication = new String(Base64.getDecoder().decode(authentication), "UTF-8");
      } catch (UnsupportedEncodingException ignored) {
      }
      return authentication;
   }
}
