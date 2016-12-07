package edu.osc.jobinfo.api;

import java.time.LocalDateTime;

import org.junit.Test;
import org.meanbean.test.BeanTester;

import edu.osc.jobinfo.testsupport.LocalDateTimeFactory;

public class JobInfoTest {

   @Test
   public void testBean() {
      BeanTester beanTester = new BeanTester();
      beanTester.getFactoryCollection().addFactory(LocalDateTime.class, new LocalDateTimeFactory());
      beanTester.testBean(JobInfo.class);
   }
}
