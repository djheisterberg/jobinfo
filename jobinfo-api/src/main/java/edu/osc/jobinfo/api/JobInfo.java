package edu.osc.jobinfo.api;

import java.util.List;

import edu.osc.jobinfo.pbs.api.Job;

public class JobInfo {

   private Job job;
   private List<String> nodes;
   private List<String> gangliaURLs;

   public JobInfo() {
   }

   public JobInfo(Job job) {
      this.job = job;
   }

   public Job getJob() {
      return job;
   }

   public void setJob(Job job) {
      this.job = job;
   }

   public List<String> getNodes() {
      return nodes;
   }

   public void setNodes(List<String> nodes) {
      this.nodes = nodes;
   }

   public List<String> getGangliaURLs() {
      return gangliaURLs;
   }

   public void setGangliaURLs(List<String> gangliaURLs) {
      this.gangliaURLs = gangliaURLs;
   }
}
