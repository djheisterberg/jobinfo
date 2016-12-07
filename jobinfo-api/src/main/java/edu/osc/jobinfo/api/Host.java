package edu.osc.jobinfo.api;

import java.util.List;

public class Host {

   public final String name;
   public final List<Integer> cpus;

   public Host(String name, List<Integer> cpus) {
      this.name = name;
      this.cpus = cpus;
   }
}
