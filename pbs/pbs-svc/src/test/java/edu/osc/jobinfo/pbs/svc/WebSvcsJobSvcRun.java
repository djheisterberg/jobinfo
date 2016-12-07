package edu.osc.jobinfo.pbs.svc;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.openejb.api.LocalClient;

import edu.osc.jobinfo.pbs.api.JobSvc;

@LocalClient
public class WebSvcsJobSvcRun {

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
      String jobid = args[iArg++];
      String system = args[iArg++];

      WebSvcsJobSvcRun run = new WebSvcsJobSvcRun(authentication);
      Context ctxt = EJBContainer.createEJBContainer().getContext();
      ctxt.bind("inject", run);

      System.out.println(run.getJobHTML(jobid, system));

      ctxt.close();
   }

   private final String authentication;

   @EJB(name = "WebSvcsJobSvc")
   private JobSvc jobSvc;

   public WebSvcsJobSvcRun(String authentication) {
      this.authentication = authentication;
   }

   public String getJobHTML(String jobid, String system) {
      return jobSvc.getJobHTML(authentication, jobid, system);
   }
}
