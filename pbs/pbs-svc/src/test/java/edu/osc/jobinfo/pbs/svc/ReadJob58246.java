package edu.osc.jobinfo.pbs.svc;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ReadJob58246 {

   private final static String URL = "https://" + WebSvcsJobSvc.DOMAIN + WebSvcsJobSvc.PATH;
   private final static String ID = "58246";
   private final static String SYSTEM = "owens";

   public static void main(String[] args) throws Exception {

      if (args.length != 2) {
         System.err.println("USAGE: ReadJob58246 username password");
         System.exit(1);
      }

      CredentialsProvider credProv = new BasicCredentialsProvider();
      credProv.setCredentials(new AuthScope(WebSvcsJobSvc.DOMAIN, AuthScope.ANY_PORT),
            new UsernamePasswordCredentials(args[0], args[1]));

      CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credProv).build();

      RequestBuilder getBuilder = RequestBuilder.get(URL);
      getBuilder.addParameter("jobid", ID);
      getBuilder.addParameter("system", SYSTEM);
      for (String field : WebSvcsJobSvc.FIELDS) {
         getBuilder.addParameter(field, "1");
      }
      HttpGet httpGet = new HttpGet(getBuilder.build().getURI());
      System.out.println("GET: " + httpGet);

      CloseableHttpResponse response = httpClient.execute(httpGet);
      StatusLine statusLine = response.getStatusLine();
      int statusCode = statusLine.getStatusCode();
      HttpEntity entity = response.getEntity();
      if (statusCode == 200) {
         String content = EntityUtils.toString(entity);
         OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(ID + "." + SYSTEM + ".html"), "UTF-8");
         osw.write(content);
         osw.close();
      } else {
         System.out.println(statusLine);
      }
      EntityUtils.consume(entity);
      response.close();

      httpClient.close();
   }
}
