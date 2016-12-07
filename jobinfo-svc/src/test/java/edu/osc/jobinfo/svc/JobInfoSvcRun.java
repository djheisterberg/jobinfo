package edu.osc.jobinfo.svc;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.openejb.api.LocalClient;

import edu.osc.jobinfo.api.JobInfo;
import edu.osc.jobinfo.api.JobInfoSvc;

@LocalClient
public class JobInfoSvcRun {

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

      JobInfoSvcRun run = new JobInfoSvcRun(authentication);
      Context ctxt = EJBContainer.createEJBContainer().getContext();
      ctxt.bind("inject", run);

      JobInfo jobInfo = run.getJobInfo(jobid, system);
      for (String gangliaURL : jobInfo.getGangliaURLs()) {
         System.out.println(gangliaURL);
      }

      ctxt.close();
   }

   private final String authentication;

   @EJB(name = "JobInfoSvcImpl")
   private JobInfoSvc jobInfoSvc;

   public JobInfoSvcRun(String authentication) {
      this.authentication = authentication;
   }

   public JobInfo getJobInfo(String jobid, String system) {
      return jobInfoSvc.getJobInfo(authentication, jobid, system);
   }
}
