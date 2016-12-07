package edu.osc.jobinfo.svc;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.osc.jobinfo.api.JobInfo;
import edu.osc.jobinfo.pbs.api.Job;
import edu.osc.jobinfo.pbs.api.JobSvc;

public class JobSvcImplTest {

   private final MockJobSvc jobSvc;
   private final _JobInfoSvc jobInfoSvc;

   public JobSvcImplTest() throws Exception {
      jobSvc = new MockJobSvc();
      jobInfoSvc = new _JobInfoSvc(jobSvc);
      jobInfoSvc.postConstruct();
   }

   @Before
   public void before() {
      jobInfoSvc.unparsedFields.clear();
   }

   @Test
   public void testNullJob() {
      jobSvc.setJob(null);

      Assert.assertNull(jobInfoSvc.getJobInfo(null, null, null));
      Assert.assertTrue(jobInfoSvc.unparsedFields.isEmpty());
   }

   @Test
   public void testNoHosts() {
      Job job = new Job();
      job.setId("id");
      jobSvc.setJob(job);

      JobInfo jobInfo = jobInfoSvc.getJobInfo(null, null, null);
      Assert.assertNotNull(jobInfo);
      Assert.assertSame(job, jobInfo.getJob());
      Assert.assertNotNull(jobInfo.getNodes());
      Assert.assertTrue(jobInfo.getNodes().isEmpty());
      Assert.assertNotNull(jobInfo.getGangliaURLs());
      Assert.assertTrue(jobInfo.getGangliaURLs().isEmpty());
      Assert.assertTrue(jobInfoSvc.unparsedFields.isEmpty());
   }

   @Test
   public void testBadHosts() {
      Job job = new Job();
      job.setId("id");
      job.setHostlist("unparseable");
      jobSvc.setJob(job);

      JobInfo jobInfo = jobInfoSvc.getJobInfo(null, null, null);
      Assert.assertNotNull(jobInfo);
      Assert.assertSame(job, jobInfo.getJob());
      Assert.assertNotNull(jobInfo.getNodes());
      Assert.assertTrue(jobInfo.getNodes().isEmpty());
      Assert.assertNotNull(jobInfo.getGangliaURLs());
      Assert.assertTrue(jobInfo.getGangliaURLs().isEmpty());
      Assert.assertTrue(jobInfoSvc.unparsedFields.contains("hostlist"));
   }

   @Test
   public void testNoStart() {
      Job job = new Job();
      job.setId("id");
      job.setHostlist("n0100/0");
      jobSvc.setJob(job);

      final List<String> expectedNodes = Collections.singletonList("n0100");

      JobInfo jobInfo = jobInfoSvc.getJobInfo(null, null, null);
      Assert.assertNotNull(jobInfo);
      Assert.assertSame(job, jobInfo.getJob());
      Assert.assertNotNull(jobInfo.getNodes());
      Assert.assertEquals(expectedNodes, jobInfo.getNodes());
      Assert.assertNotNull(jobInfo.getGangliaURLs());
      Assert.assertTrue(jobInfo.getGangliaURLs().isEmpty());
      Assert.assertTrue(jobInfoSvc.unparsedFields.isEmpty());
   }

   @Test
   public void testBadStart() {
      Job job = new Job();
      job.setId("id");
      job.setHostlist("n0100/0");
      job.setStart("unparseable");
      jobSvc.setJob(job);

      final List<String> expectedNodes = Collections.singletonList("n0100");

      JobInfo jobInfo = jobInfoSvc.getJobInfo(null, null, null);
      Assert.assertNotNull(jobInfo);
      Assert.assertSame(job, jobInfo.getJob());
      Assert.assertNotNull(jobInfo.getNodes());
      Assert.assertEquals(expectedNodes, jobInfo.getNodes());
      Assert.assertNotNull(jobInfo.getGangliaURLs());
      Assert.assertTrue(jobInfo.getGangliaURLs().isEmpty());
      Assert.assertTrue(jobInfoSvc.unparsedFields.contains("start"));
   }

   @Test
   public void testNoEnd() {
      Job job = new Job();
      job.setId("id");
      job.setSystem("oakley");
      job.setHostlist("n0100/0");
      job.setStart("2016-01-01 00:00:01");
      jobSvc.setJob(job);

      final List<String> expectedNodes = Collections.singletonList("n0100");

      JobInfo jobInfo = jobInfoSvc.getJobInfo(null, null, null);
      Assert.assertNotNull(jobInfo);
      Assert.assertSame(job, jobInfo.getJob());
      Assert.assertNotNull(jobInfo.getNodes());
      Assert.assertEquals(expectedNodes, jobInfo.getNodes());
      Assert.assertNotNull(jobInfo.getGangliaURLs());
      Assert.assertEquals(1, jobInfo.getGangliaURLs().size());
      Assert.assertTrue(jobInfoSvc.unparsedFields.isEmpty());
   }

   @Test
   public void testBadEnd() {
      Job job = new Job();
      job.setId("id");
      job.setSystem("oakley");
      job.setHostlist("n0100/0");
      job.setStart("2016-01-01 00:00:01");
      job.setEnd("unparseable");
      jobSvc.setJob(job);

      final List<String> expectedNodes = Collections.singletonList("n0100");

      JobInfo jobInfo = jobInfoSvc.getJobInfo(null, null, null);
      Assert.assertNotNull(jobInfo);
      Assert.assertSame(job, jobInfo.getJob());
      Assert.assertNotNull(jobInfo.getNodes());
      Assert.assertEquals(expectedNodes, jobInfo.getNodes());
      Assert.assertNotNull(jobInfo.getGangliaURLs());
      Assert.assertEquals(1, jobInfo.getGangliaURLs().size());
      Assert.assertTrue(jobInfoSvc.unparsedFields.contains("end"));
   }

   @Test
   public void testOK() {
      Job job = new Job();
      job.setId("id");
      job.setSystem("oakley");
      job.setHostlist("n0101/0");
      job.setStart("2016-01-01 00:00:01");
      job.setEnd("2016-01-02 23:59:59");
      jobSvc.setJob(job);

      final List<String> expectedNodes = Collections.singletonList("n0101");

      JobInfo jobInfo = jobInfoSvc.getJobInfo(null, null, null);
      Assert.assertNotNull(jobInfo);
      Assert.assertSame(job, jobInfo.getJob());
      Assert.assertNotNull(jobInfo.getNodes());
      Assert.assertEquals(expectedNodes, jobInfo.getNodes());
      Assert.assertNotNull(jobInfo.getGangliaURLs());
      Assert.assertEquals(1, jobInfo.getGangliaURLs().size());
      Assert.assertFalse(jobInfo.getGangliaURLs().get(0).isEmpty());
      Assert.assertTrue(jobInfoSvc.unparsedFields.isEmpty());
   }

   static class _JobInfoSvc extends JobInfoSvcImpl {

      public final Set<String> unparsedFields = new HashSet<>();

      public _JobInfoSvc(JobSvc jobSvc) throws Exception {
         Field jobSvcField = JobInfoSvcImpl.class.getDeclaredField("jobSvc");
         jobSvcField.setAccessible(true);
         jobSvcField.set(this, jobSvc);
      }

      @Override
      protected void logParseException(String id, String field, String value, Exception e) {
         unparsedFields.add(field);
      }
   }

   static class MockJobSvc implements JobSvc {

      private Job job;

      public void setJob(Job job) {
         this.job = job;
      }

      @Override
      public String getJobHTML(String authentication, String id, String system) {
         return null;
      }

      @Override
      public Job getJob(String authentication, String id, String system) {
         return job;
      }
   }
}
