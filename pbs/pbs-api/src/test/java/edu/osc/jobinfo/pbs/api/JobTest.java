package edu.osc.jobinfo.pbs.api;

import java.time.LocalDateTime;

import org.junit.Test;
import org.meanbean.test.BeanTester;

import edu.osc.jobinfo.testsupport.LocalDateTimeFactory;

public class JobTest {

   @Test
   public void testBean() {
      BeanTester beanTester = new BeanTester();
      beanTester.getFactoryCollection().addFactory(LocalDateTime.class, new LocalDateTimeFactory());
      beanTester.testBean(Job.class);
   }
}
