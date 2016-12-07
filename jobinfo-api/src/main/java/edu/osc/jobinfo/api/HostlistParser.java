package edu.osc.jobinfo.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HostlistParser {

   public static List<String> parseNames(String hostlist) throws ParseException {

      List<Host> hosts = parseHosts(hostlist);
      List<String> hostNames = new ArrayList<>(hosts.size());
      for (Host host : hosts) {
         hostNames.add(host.name);
      }

      return hostNames;
   }

   public static List<Host> parseHosts(String hostlist) throws ParseException {

      if (hostlist == null)
         return Collections.emptyList();

      hostlist = hostlist.trim();
      if (hostlist.isEmpty())
         return Collections.emptyList();

      int len = hostlist.length();
      List<Host> hosts = new ArrayList<>(len / 8 + 1);

      int ix = 0;
      while (ix < len) {
         int iy = hostlist.indexOf('+', ix);
         if (iy == -1)
            iy = len;
         String fullHost = hostlist.substring(ix, iy);

         int cpuIX = fullHost.indexOf('/');
         if (cpuIX == -1) {
            throw new ParseException("No '/' in host " + hosts.size() + " at " + ix + " of " + hostlist, ix);
         }
         String name = fullHost.substring(0, cpuIX).trim();

         // TODO cpu specifications

         hosts.add(new Host(name, null));
         ix = iy + 1;
      }

      return hosts;
   }
}
