package edu.osc.jobinfo.svc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.osc.jobinfo.api.HostlistParser;
import edu.osc.jobinfo.api.JobInfo;
import edu.osc.jobinfo.api.JobInfoSvc;
import edu.osc.jobinfo.pbs.api.Job;
import edu.osc.jobinfo.pbs.api.JobSvc;

@Singleton
public class JobInfoSvcImpl implements JobInfoSvc {

   // https://cts05.osc.edu/gweb/?r=hour&cs=11/03/2016+09:00&ce=11/04/2016+17:00&c=Oakley+nodes&h=n0004.ten.osc.edu&tab=m&mc=2&metric_group=ALLGROUPS

   private final static String BASE_URL = "https://cts05.osc.edu/gweb/";
   private final static String DEFAULT_DOMAIN = "ten.osc.edu";
   private static final String UTF8 = "UTF-8";

   private final static DateTimeFormatter jdtp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
   private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

   private final static Logger logger = LoggerFactory.getLogger(JobInfoSvcImpl.class);

   private final Map<String, String> systemMap = new HashMap<>(4);

   @Resource(name = "baseURL")
   private String baseURL;

   @EJB
   private JobSvc jobSvc;

   @PostConstruct
   protected void postConstruct() {
      systemMap.put("oak", "Oakley nodes");
      systemMap.put("owens", "Owens");
      systemMap.put("ruby", "Ruby");

      if (baseURL == null) {
         logger.warn("Using default base URL " + BASE_URL);
         baseURL = BASE_URL;
      }
      logger.info("Using base URL " + baseURL);
   }

   @Override
   public JobInfo getJobInfo(Job job) {
      if (job == null)
         return null;
      String id = job.getId();

      JobInfo jobInfo = new JobInfo(job);
      jobInfo.setNodes(Collections.emptyList());
      jobInfo.setGangliaURLs(Collections.emptyList());

      String hostlist = job.getHostlist();
      if (hostlist == null)
         return jobInfo;

      List<String> nodes = null;
      try {
         nodes = HostlistParser.parseNames(hostlist);
         jobInfo.setNodes(nodes);
      } catch (ParseException pe) {
         logParseException(id, "hostlist", hostlist, pe);
         return jobInfo;
      }

      LocalDateTime start = parseJobDateTime(id, "start", job.getStart());
      if (start == null)
         return jobInfo;
      start = start.minus(Duration.ofMinutes(15));
      String startParam = dtf.format(start);

      LocalDateTime end = parseJobDateTime(id, "end", job.getEnd());
      if (end == null)
         end = LocalDateTime.now();
      end = end.plus(Duration.ofMinutes(15));
      String endParam = dtf.format(end);

      String system = job.getSystem();
      String clusterParam = systemMap.get(system);
      if (clusterParam == null) {
         clusterParam = Character.toUpperCase(system.charAt(0)) + system.substring(1);
         logger.warn("Using unmapped system " + system);
      }

      List<String> gangliaURLs = new ArrayList<>(nodes.size());
      jobInfo.setGangliaURLs(gangliaURLs);
      for (String node : nodes) {
         StringBuilder url = new StringBuilder(baseURL + "?r=hour&tab=m&mc=2&metric_group=ALLGROUPS");
         try {
            url.append("&c=" + URLEncoder.encode(clusterParam, UTF8));
            url.append("&h=" + URLEncoder.encode(node + "." + DEFAULT_DOMAIN, UTF8));
            url.append("&cs=" + URLEncoder.encode(startParam, UTF8));
            url.append("&ce=" + URLEncoder.encode(endParam, UTF8));
         } catch (UnsupportedEncodingException ignored) {
            // can't happen with UTF-8
         }
         gangliaURLs.add(url.toString());
      }

      return jobInfo;
   }

   @Override
   public JobInfo getJobInfo(String authentication, String id, String system) {
      Job job = jobSvc.getJob(authentication, id, system);
      return getJobInfo(job);
   }

   protected LocalDateTime parseJobDateTime(String id, String field, String s) {
      if (s != null) {
         try {
            return LocalDateTime.parse(s, jdtp);
         } catch (DateTimeParseException dtpe) {
            logParseException(id, field, s, dtpe);
         }
      }
      return null;
   }

   protected void logParseException(String id, String field, String value, Exception e) {
      logger.error("Exception parsing " + field + " '" + value + "' " + id, e);
   }
}
