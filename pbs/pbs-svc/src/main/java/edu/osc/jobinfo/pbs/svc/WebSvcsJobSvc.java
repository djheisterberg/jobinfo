package edu.osc.jobinfo.pbs.svc;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.HttpHeaders;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.osc.jobinfo.pbs.api.Job;
import edu.osc.jobinfo.pbs.api.JobSvc;

@Singleton
public class WebSvcsJobSvc implements JobSvc {

   public final static String DOMAIN = "websvcs.osc.edu";
   public final static String PATH = "/pbsacct/jobinfo.php";

   public final static String[] FIELDS = { USERNAME, GROUPNAME, ACCOUNT, JOBNAME, NPROC, MPPE, MPPSSP, NODES, FEATURE,
         GRES, GATTR, QUEUE, QOS, SUBMIT, START, END, CPU_REQ, CPU, WALLTIME_REQ, WALLTIME, MEM_REQ, MEM, VMEM_REQ,
         VMEM, SOFTWARE, SUBMITHOST, HOSTLIST, EXITSTATUS, SCRIPT };

   public final static String DATE_0 = "1969-12-31 19:00:00";

   private final static Logger logger = LoggerFactory.getLogger(WebSvcsJobSvc.class);

   @Resource(name = "domain")
   private String domain;

   @Resource(name = "path")
   private String path;

   private String url;

   @PostConstruct
   protected void postConstruct() {
      if (domain == null) {
         logger.warn("Using default domain " + DOMAIN);
         domain = DOMAIN;
      }
      if (path == null) {
         logger.warn("Using default path " + PATH);
         path = PATH;
      }
      url = "https://" + domain + path;
      logger.info("Using URL " + url);
   }

   @Override
   public String getJobHTML(String authentication, String id, String system) {

      try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
         RequestBuilder getBuilder = RequestBuilder.get(url);
         getBuilder.addParameter(JOBID, id);
         getBuilder.addParameter(SYSTEM, system);
         for (String field : WebSvcsJobSvc.FIELDS) {
            getBuilder.addParameter(field, "1");
         }
         HttpGet httpGet = new HttpGet(getBuilder.build().getURI());
         if (authentication != null) {
            httpGet.addHeader(HttpHeaders.AUTHORIZATION, authentication);
         }

         try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity);
            if (statusCode == 200) {
               return html;
            } else if (statusCode == 401) {
               throw new NotAuthorizedException(html);
            } else {
               logger.error("Error GETting job html id=" + id + ", system=" + system + ", response=" + statusLine);
               EntityUtils.consume(entity);
            }
         } catch (IOException ioe) {
            logger.error("Exception reading job html id=" + id + ", system=" + system, ioe);
         }

      } catch (IOException ioe) {
         logger.warn("Exception closing HttpClient id=" + id + ", system=" + system, ioe);
      }

      return null;
   }

   @Override
   public Job getJob(String authentication, String id, String system) {

      String html = getJobHTML(authentication, id, system);
      if (html != null) {
         return parseJobHTML(id, system, html);
      }
      return null;
   }

   protected Job parseJobHTML(String id, String system, String html) {

      Document doc = Jsoup.parse(html);

      // Should have a top-level table (parser provides body if necessary).
      Element topTable = doc.select("table").first();
      if (topTable == null) {
         logNoData(id, system);
         return null;
      }
      Element topTableBody = topTable.select("tbody").first();
      if (topTableBody == null) {
         logNoData(id, system);
         return null;
      }

      // Should have a sub-table.
      Element table = topTableBody.select("table").first();
      if (table == null) {
         logNoData(id, system);
         return null;
      }
      Element tableBody = table.select("tbody").first();
      if (tableBody == null) {
         logNoData(id, system);
         return null;
      }

      Job job = new Job();
      for (Element row : tableBody.select("tr")) {
         Elements cols = row.select("td");
         if (cols.size() != 2)
            break;
         String key = cols.get(0).text();
         String value = cols.get(1).text();
         if (value.isEmpty())
            value = null;

         switch (key) {
         case JOBID:
            job.setId(value);
            break;
         case SYSTEM:
            job.setSystem(value);
            break;
         case USERNAME:
            job.setUsername(value);
            break;
         case GROUPNAME:
            job.setGroupname(value);
            break;
         case ACCOUNT:
            job.setAccount(value);
            break;
         case JOBNAME:
            job.setJobname(value);
            break;
         case NPROC:
            job.setNproc(parseInt(id, system, NPROC, value));
            break;
         case MPPE:
            job.setMppe(value);
            break;
         case MPPSSP:
            job.setMppssp(value);
            break;
         case NODES:
            job.setNodes(value);
            break;
         case FEATURE:
            job.setFeature(value);
            break;
         case GRES:
            job.setGres(value);
            break;
         case GATTR:
            job.setGattr(value);
            break;
         case QUEUE:
            job.setQueue(value);
            break;
         case QOS:
            job.setQos(value);
            break;
         case SUBMIT:
            if (!DATE_0.equals(value)) {
               job.setSubmit(value);
            }
            break;
         case START:
            if (!DATE_0.equals(value)) {
               job.setStart(value);
            }
            break;
         case END:
            if (!DATE_0.equals(value)) {
               job.setEnd(value);
            }
            break;
         case CPU_REQ:
            job.setCputReq(value);
            break;
         case CPU:
            job.setCput(value);
            break;
         case WALLTIME_REQ:
            job.setWalltimeReq(value);
            break;
         case WALLTIME:
            job.setWalltime(value);
            break;
         case MEM_REQ:
            job.setMemReq(value);
            break;
         case MEM:
            job.setMemKB(parseInt(id, system, MEM, value));
            break;
         case VMEM_REQ:
            job.setVmemReq(value);
            break;
         case VMEM:
            job.setVmemKB(parseInt(id, system, VMEM, value));
            break;
         case SOFTWARE:
            job.setSoftware(value);
            break;
         case SUBMITHOST:
            job.setSubmithost(value);
            break;
         case HOSTLIST:
            job.setHostlist(value);
            break;
         case EXITSTATUS:
            job.setExitStatus(parseInt(id, system, EXITSTATUS, value));
            break;
         case SCRIPT:
            job.setScript(value);
            break;
         default:
            logger.error("Unrecognized field '" + key + "' for " + id + "." + system);
         }
      }

      // Check id
      String tmpId = job.getId();
      if ((tmpId != null) && tmpId.startsWith(id)) {
         return job;
      } else {
         logBadId(id, system, tmpId);
      }

      return null;
   }

   protected Integer parseInt(String id, String system, String field, String s) {
      if (s != null) {
         try {
            return Integer.parseInt(s);
         } catch (Exception e) {
            logParseException(id, system, field, s, e);
         }
      }
      return null;
   }

   protected void logNoData(String id, String system) {
      logger.error("No data found for " + id + "." + system);
   }

   protected void logParseException(String id, String system, String field, String value, Exception e) {
      logger.error("Exception parsing " + field + " '" + value + "' " + id + "." + system, e);
   }

   protected void logBadId(String id, String system, String badId) {
      logger.error("Id mismatch '" + badId + "' != " + id + "." + system);
   }
}
