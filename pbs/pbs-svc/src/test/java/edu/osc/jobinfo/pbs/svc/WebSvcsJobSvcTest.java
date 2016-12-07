package edu.osc.jobinfo.pbs.svc;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import edu.osc.jobinfo.pbs.api.Job;
import edu.osc.jobinfo.pbs.api.JobSvc;

public class WebSvcsJobSvcTest {

   private final static String HTML0 = "<html><body><table><tr><td><table>";
   private final static String HTML1 = "</table></td></tr></table></body></html>";

   private _JobSvc jobSvc = new _JobSvc();

   @Test
   public void testBadHTML() {
      jobSvc.noData = false;
      Job job = jobSvc.parseJobHTML("id", "system", "html");
      Assert.assertNull(job);
      Assert.assertTrue(jobSvc.noData);

      jobSvc.noData = false;
      job = jobSvc.parseJobHTML("id", "system", "<html><body><table></table></body></html>");
      Assert.assertNull(job);
      Assert.assertTrue(jobSvc.noData);

      jobSvc.noData = false;
      job = jobSvc.parseJobHTML("id", "system",
            "<html><body><table><tr><td><table></table></td></tr></table></body></html>");
      Assert.assertNull(job);
      Assert.assertTrue(jobSvc.noData);
   }

   @Test
   public void testBadID() {
      StringBuilder sb = new StringBuilder(100);
      sb.append(HTML0);
      addRow(sb, JobSvc.JOBID, "some job id");
      sb.append(HTML1);

      jobSvc.badId = false;
      Job job = jobSvc.parseJobHTML("id", "system", sb.toString());
      Assert.assertNull(job);
      Assert.assertTrue(jobSvc.badId);
   }

   @Test
   public void testParseNulls() {
      Set<String> unparsedFields = jobSvc.unparsedFields;
      unparsedFields.clear();

      Assert.assertNull(jobSvc.parseInt("id", "system", "int field", null));
      Assert.assertTrue(unparsedFields.isEmpty());
   }

   @Test
   public void testLogParseException() {
      final String id = "1234";
      final String system = "oakley";
      final String jobid = id + "." + system;
      final String nproc = "junk nproc";
      final String memKBReq = "junk memKBReq";
      final String memKB = "junk memKB";
      final String vmemKBReq = "junk vmemKBReq";
      final String vmemKB = "junk vmemKB";
      final String exitstatus = "junk exitstatus";

      StringBuilder sb = new StringBuilder(1000);
      sb.append(HTML0);
      addRow(sb, JobSvc.JOBID, jobid);
      addRow(sb, JobSvc.SYSTEM, system);
      addRow(sb, JobSvc.NPROC, nproc);
      addRow(sb, JobSvc.MEM_REQ, memKBReq);
      addRow(sb, JobSvc.MEM, memKB);
      addRow(sb, JobSvc.VMEM_REQ, vmemKBReq);
      addRow(sb, JobSvc.VMEM, vmemKB);
      addRow(sb, JobSvc.EXITSTATUS, exitstatus);
      sb.append(HTML1);

      Set<String> unparsedFields = jobSvc.unparsedFields;
      unparsedFields.clear();
      Job job = jobSvc.parseJobHTML(id, system, sb.toString());

      Assert.assertNotNull(job);
      Assert.assertTrue(unparsedFields.contains(JobSvc.NPROC));
      Assert.assertTrue(unparsedFields.contains(JobSvc.MEM_REQ));
      Assert.assertTrue(unparsedFields.contains(JobSvc.MEM));
      Assert.assertTrue(unparsedFields.contains(JobSvc.VMEM_REQ));
      Assert.assertTrue(unparsedFields.contains(JobSvc.VMEM));
      Assert.assertTrue(unparsedFields.contains(JobSvc.EXITSTATUS));
   }

   @Test
   public void testParse() {
      final String id = "1234";
      final String system = "oakley";
      final String jobid = id + "." + system;
      final String username = "username";
      final String groupname = "groupname";
      final String account = "account";
      final String jobname = "jobname";
      final Integer nproc = Integer.valueOf(10);
      final String mppe = "mppe";
      final String mppssp = "mppssp";
      final String nodes = "nodes";
      final String feature = "feature";
      final String gres = "gres";
      final String gattr = "gattr";
      final String queue = "queue";
      final String qos = "qos";
      final String submit = "2014-01-01 10:11:12";
      final String start = "2015-06-10 13:14:15";
      final String end = "2016-12-20 16:17:18";
      final String cputReq = "1:02:03";
      final String cput = "4:05:06";
      final String walltimeReq = "7:08:09";
      final String walltime = "10:11:12";
      final Integer memKBReq = Integer.valueOf(456);
      final Integer memKB = Integer.valueOf(567);
      final Integer vmemKBReq = Integer.valueOf(678);
      final Integer vmemKB = Integer.valueOf(789);
      final String software = "software";
      final String submithost = "submithost";
      final String hostlist = "hostlist";
      final Integer exitstatus = Integer.valueOf(-11);
      final String script = "script";

      StringBuilder sb = new StringBuilder(1000);
      sb.append(HTML0);
      addRow(sb, JobSvc.JOBID, jobid);
      addRow(sb, JobSvc.SYSTEM, system);
      addRow(sb, JobSvc.USERNAME, username);
      addRow(sb, JobSvc.GROUPNAME, groupname);
      addRow(sb, JobSvc.ACCOUNT, account);
      addRow(sb, JobSvc.JOBNAME, jobname);
      addRow(sb, JobSvc.NPROC, nproc.toString());
      addRow(sb, JobSvc.MPPE, mppe);
      addRow(sb, JobSvc.MPPSSP, mppssp);
      addRow(sb, JobSvc.NODES, nodes);
      addRow(sb, JobSvc.FEATURE, feature);
      addRow(sb, JobSvc.GRES, gres);
      addRow(sb, JobSvc.GATTR, gattr);
      addRow(sb, JobSvc.QUEUE, queue);
      addRow(sb, JobSvc.QOS, qos);
      addRow(sb, JobSvc.SUBMIT, submit);
      addRow(sb, JobSvc.START, start);
      addRow(sb, JobSvc.END, end);
      addRow(sb, JobSvc.CPU_REQ, cputReq);
      addRow(sb, JobSvc.CPU, cput);
      addRow(sb, JobSvc.WALLTIME_REQ, walltimeReq);
      addRow(sb, JobSvc.WALLTIME, walltime);
      addRow(sb, JobSvc.MEM_REQ, memKBReq.toString());
      addRow(sb, JobSvc.MEM, memKB.toString());
      addRow(sb, JobSvc.VMEM_REQ, vmemKBReq.toString());
      addRow(sb, JobSvc.VMEM, vmemKB.toString());
      addRow(sb, JobSvc.SOFTWARE, software);
      addRow(sb, JobSvc.SUBMITHOST, submithost);
      addRow(sb, JobSvc.HOSTLIST, hostlist);
      addRow(sb, JobSvc.EXITSTATUS, exitstatus.toString());
      addRow(sb, JobSvc.SCRIPT, script);
      sb.append(HTML1);

      Job job = jobSvc.parseJobHTML(id, system, sb.toString());

      Assert.assertNotNull(job);
      Assert.assertEquals(jobid, job.getId());
      Assert.assertEquals(system, job.getSystem());
      Assert.assertEquals(username, job.getUsername());
      Assert.assertEquals(groupname, job.getGroupname());
      Assert.assertEquals(account, job.getAccount());
      Assert.assertEquals(jobname, job.getJobname());
      Assert.assertEquals(nproc, job.getNproc());
      Assert.assertEquals(mppe, job.getMppe());
      Assert.assertEquals(mppssp, job.getMppssp());
      Assert.assertEquals(nodes, job.getNodes());
      Assert.assertEquals(feature, job.getFeature());
      Assert.assertEquals(gres, job.getGres());
      Assert.assertEquals(gattr, job.getGattr());
      Assert.assertEquals(queue, job.getQueue());
      Assert.assertEquals(qos, job.getQos());
      Assert.assertEquals(submit, job.getSubmit());
      Assert.assertEquals(start, job.getStart());
      Assert.assertEquals(end, job.getEnd());
      Assert.assertEquals(cputReq, job.getCputReq());
      Assert.assertEquals(cput, job.getCput());
      Assert.assertEquals(walltimeReq, job.getWalltimeReq());
      Assert.assertEquals(walltime, job.getWalltime());
      Assert.assertEquals(memKBReq, job.getMemKBReq());
      Assert.assertEquals(memKB, job.getMemKB());
      Assert.assertEquals(vmemKBReq, job.getVmemKBReq());
      Assert.assertEquals(vmemKB, job.getVmemKB());
      Assert.assertEquals(software, job.getSoftware());
      Assert.assertEquals(submithost, job.getSubmithost());
      Assert.assertEquals(hostlist, job.getHostlist());
      Assert.assertEquals(exitstatus, job.getExitStatus());
      Assert.assertEquals(script, job.getScript());
   }

   private void addRow(StringBuilder sb, String key, String value) {
      sb.append("<tr><td>" + key + "</td><td>" + value + "</td></tr>");
   }

   @Test
   public void testParse58246() throws Exception {
      Reader r = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("58246.owens.html"), "UTF-8");
      char[] buffer = new char[8000];
      int nChar = r.read(buffer);
      r.close();
      String html = new String(buffer, 0, nChar);
      System.out.println(html);

      Job job = jobSvc.parseJobHTML("58246", "owens", html);

      Assert.assertNotNull(job);
      Assert.assertEquals("58246.owens-batch.ten.osc.edu", job.getId());
      Assert.assertEquals("owens", job.getSystem());
      Assert.assertEquals("osu5946", job.getUsername());
      Assert.assertEquals("PAS0503", job.getGroupname());
      Assert.assertEquals("PAS0503", job.getAccount());
      Assert.assertEquals("my_job", job.getJobname());
      Assert.assertEquals(Integer.valueOf(48), job.getNproc());
      Assert.assertNull(job.getMppe());
      Assert.assertNull(job.getMppssp());
      Assert.assertEquals("4:ppn=12", job.getNodes());
      Assert.assertNull(job.getFeature());
      Assert.assertNull(job.getGres());
      Assert.assertEquals("PAS0503", job.getGattr());
      Assert.assertEquals("parallel", job.getQueue());
      Assert.assertNull(job.getQos());
      Assert.assertEquals("2016-11-08 10:37:42", job.getSubmit());
      Assert.assertEquals("2016-11-08 10:37:51", job.getStart());
      Assert.assertEquals("2016-11-08 10:38:07", job.getEnd());
      Assert.assertEquals("00:00:00", job.getCputReq());
      Assert.assertEquals("00:00:40", job.getCput());
      Assert.assertEquals("03:00:00", job.getWalltimeReq());
      Assert.assertEquals("00:00:12", job.getWalltime());
      Assert.assertNull(job.getMemKBReq());
      Assert.assertEquals(Integer.valueOf(897232), job.getMemKB());
      Assert.assertNull(job.getVmemKBReq());
      Assert.assertEquals(Integer.valueOf(3352600), job.getVmemKB());
      Assert.assertNull(job.getSoftware());
      Assert.assertEquals("owens-login02.hpc.osc.edu", job.getSubmithost());
      Assert.assertEquals("o0249/0-11+o0248/0-11+o0247/0-11+o0246/0-11", job.getHostlist());
      Assert.assertEquals(Integer.valueOf(1), job.getExitStatus());
      String script = job.getScript();
      Assert.assertNotNull(script);
      Assert.assertTrue(script.startsWith("#!/bin/bash -l"));
      System.out.println(script);
   }

   private static class _JobSvc extends WebSvcsJobSvc {

      public boolean noData = false;
      public boolean badId = false;
      public final Set<String> unparsedFields = new HashSet<>();

      @Override
      protected void logNoData(String id, String system) {
         noData = true;
      }

      @Override
      protected void logParseException(String id, String system, String field, String value, Exception e) {
         unparsedFields.add(field);
      }

      @Override
      protected void logBadId(String id, String system, String badId) {
         this.badId = true;
      }
   }
}
