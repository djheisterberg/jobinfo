package edu.osc.jobinfo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import edu.osc.jobinfo.api.JobInfo;

@Path("/jobinfo/rest")
@Produces(MediaType.APPLICATION_JSON)
public interface JobInfoREST {

   @GET
   public JobInfo getJobInfo(@QueryParam("id") String id, @QueryParam("system") String system);
}
