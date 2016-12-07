package edu.osc.jobinfo.rest;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import edu.osc.jobinfo.api.JobInfo;
import edu.osc.jobinfo.api.JobInfoSvc;

@Singleton
public class JobInfoRESTImpl implements JobInfoREST {

   @Context
   private HttpHeaders headers;

   @EJB
   private JobInfoSvc jobInfoSvc;

   @Override
   public JobInfo getJobInfo(String id, String system) {
      String authentication = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
      JobInfo jobInfo = jobInfoSvc.getJobInfo(authentication, id, system);
      if (jobInfo == null) {
         throw new NotFoundException("No jobinfo for " + id + "." + system);
      }
      return jobInfo;
   }
}
