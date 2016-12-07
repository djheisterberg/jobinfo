package edu.osc.jobinfo.api;

import javax.ejb.Local;

import edu.osc.jobinfo.pbs.api.Job;

@Local
public interface JobInfoSvc {

   public JobInfo getJobInfo(Job job);

   public JobInfo getJobInfo(String authentication, String id, String system);
}
