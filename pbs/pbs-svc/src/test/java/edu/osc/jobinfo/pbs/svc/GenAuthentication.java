package edu.osc.jobinfo.pbs.svc;

import java.util.Base64;

public class GenAuthentication {

   public static void main(String[] args) throws Exception {
      if (args.length != 2) {
         System.err.println("USAGE: GenAuthentication username password");
         System.exit(1);
      }
      System.out.println(Base64.getEncoder().encodeToString((args[0] + ":" + args[1]).getBytes("UTF-8")));
   }
}
