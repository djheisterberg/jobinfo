package edu.osc.jobinfo.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class HostlistParserTest {

   @Test
   public void testNull() throws Exception {
      Assert.assertTrue(HostlistParser.parseNames(null).isEmpty());
   }

   @Test
   public void testEmpty() throws Exception {
      Assert.assertTrue(HostlistParser.parseNames("   ").isEmpty());
   }

   @Test
   public void testSingleCPU() throws Exception {
      final String hostlist = "o0218/22";
      final List<String> expectedHosts = Collections.singletonList("o0218");

      testParse(hostlist, expectedHosts);
   }

   @Test
   public void testSingleRange() throws Exception {
      final String hostlist = "o0120/0-27";
      final List<String> expectedHosts = Collections.singletonList("o0120");

      testParse(hostlist, expectedHosts);
   }

   @Test
   public void testSingleList() throws Exception {
      final String hostlist = "o0207/5,12-18";
      final List<String> expectedHosts = Collections.singletonList("o0207");

      testParse(hostlist, expectedHosts);
   }

   @Test
   public void testMultiCPU() throws Exception {
      final String hostlist = "o0218/22+o0219/13";
      final List<String> expectedHosts = new ArrayList<>(2);
      expectedHosts.add("o0218");
      expectedHosts.add("o0219");

      testParse(hostlist, expectedHosts);
   }

   @Test
   public void testMultiRange() throws Exception {
      final String hostlist = "o0249/0-11+o0248/0-11+o0247/0-11+o0246/0-11";
      final List<String> expectedHosts = new ArrayList<>(4);
      expectedHosts.add("o0249");
      expectedHosts.add("o0248");
      expectedHosts.add("o0247");
      expectedHosts.add("o0246");

      testParse(hostlist, expectedHosts);
   }

   @Test
   public void testMultiList() throws Exception {
      final String hostlist = "o0249/0-11+o0248/0,2-12+o0247/0-11+o0246/8,10,12-21";
      final List<String> expectedHosts = new ArrayList<>(4);
      expectedHosts.add("o0249");
      expectedHosts.add("o0248");
      expectedHosts.add("o0247");
      expectedHosts.add("o0246");

      testParse(hostlist, expectedHosts);
   }

   private void testParse(String hostlist, List<String> expectedHosts) throws Exception {
      List<String> hosts = HostlistParser.parseNames(hostlist);
      Assert.assertEquals(expectedHosts, hosts);
   }
}
