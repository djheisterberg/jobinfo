package edu.osc.jobinfo.pbs.api;

import javax.ejb.Local;

@Local
public interface JobSvc {

   public final static String JOBID = "jobid";
   public final static String SYSTEM = "system";
   public final static String USERNAME = "username";
   public final static String GROUPNAME = "groupname";
   public final static String ACCOUNT = "account";
   public final static String JOBNAME = "jobname";
   public final static String NPROC = "nproc";
   public final static String MPPE = "mppe";
   public final static String MPPSSP = "mppssp";
   public final static String NODES = "nodes";
   public final static String FEATURE = "feature";
   public final static String GRES = "gres";
   public final static String GATTR = "gattr";
   public final static String QUEUE = "queue";
   public final static String QOS = "qos";
   public final static String SUBMIT = "submit_ts";
   public final static String START = "start_ts";
   public final static String END = "end_ts";
   public final static String CPU_REQ = "cput_req";
   public final static String CPU = "cput";
   public final static String WALLTIME_REQ = "walltime_req";
   public final static String WALLTIME = "walltime";
   public final static String MEM_REQ = "mem_req";
   public final static String MEM = "mem_kb";
   public final static String VMEM_REQ = "vmem_req";
   public final static String VMEM = "vmem_kb";
   public final static String SOFTWARE = "software";
   public final static String SUBMITHOST = "submithost";
   public final static String HOSTLIST = "hostlist";
   public final static String EXITSTATUS = "exit_status";
   public final static String SCRIPT = "script";

   public String getJobHTML(String authentication, String id, String system);

   public Job getJob(String authentication, String id, String system);
}
