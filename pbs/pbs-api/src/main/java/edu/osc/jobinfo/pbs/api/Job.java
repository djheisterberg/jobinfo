package edu.osc.jobinfo.pbs.api;

public class Job {

   public enum State {
      A("abort"), C("checkpoint"), D("delete"), E("exit"), Q("queue"), R("rerun"), S("start"), T("restart");

      public final String description;

      private State(String description) {
         this.description = description;
      }
   }

   private String id;
   private String system;
   private State state;
   private String username;
   private String groupname;
   private String account;
   private String jobname;
   private Integer nproc;
   private String mppe;
   private String mppssp;
   private String nodes;
   private String feature;
   private String gres;
   private String gattr;
   private String queue;
   private String qos;
   private String submit;
   private String start;
   private String end;
   private String cputReq;
   private String cput;
   private String walltimeReq;
   private String walltime;
   private Integer memKBReq;
   private Integer memKB;
   private Integer vmemKBReq;
   private Integer vmemKB;
   private String software;
   private String submithost;
   private String hostlist;
   private Integer exitStatus;
   private String script;

   public Job() {
   }

   public Job(String id, String system, State state) {
      this.id = id;
      this.system = system;
      this.state = state;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getSystem() {
      return system;
   }

   public void setSystem(String system) {
      this.system = system;
   }

   public State getState() {
      return state;
   }

   public void setState(State state) {
      this.state = state;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getGroupname() {
      return groupname;
   }

   public void setGroupname(String groupname) {
      this.groupname = groupname;
   }

   public String getAccount() {
      return account;
   }

   public void setAccount(String account) {
      this.account = account;
   }

   public String getJobname() {
      return jobname;
   }

   public void setJobname(String jobname) {
      this.jobname = jobname;
   }

   public Integer getNproc() {
      return nproc;
   }

   public void setNproc(Integer nproc) {
      this.nproc = nproc;
   }

   public String getMppe() {
      return mppe;
   }

   public void setMppe(String mppe) {
      this.mppe = mppe;
   }

   public String getMppssp() {
      return mppssp;
   }

   public void setMppssp(String mppssp) {
      this.mppssp = mppssp;
   }

   public String getNodes() {
      return nodes;
   }

   public void setNodes(String nodes) {
      this.nodes = nodes;
   }

   public String getFeature() {
      return feature;
   }

   public void setFeature(String feature) {
      this.feature = feature;
   }

   public String getGres() {
      return gres;
   }

   public void setGres(String gres) {
      this.gres = gres;
   }

   public String getGattr() {
      return gattr;
   }

   public void setGattr(String gattr) {
      this.gattr = gattr;
   }

   public String getQueue() {
      return queue;
   }

   public void setQueue(String queue) {
      this.queue = queue;
   }

   public String getQos() {
      return qos;
   }

   public void setQos(String qos) {
      this.qos = qos;
   }

   public String getSubmit() {
      return submit;
   }

   public void setSubmit(String submit) {
      this.submit = submit;
   }

   public String getStart() {
      return start;
   }

   public void setStart(String start) {
      this.start = start;
   }

   public String getEnd() {
      return end;
   }

   public void setEnd(String end) {
      this.end = end;
   }

   public String getCputReq() {
      return cputReq;
   }

   public void setCputReq(String cputReq) {
      this.cputReq = cputReq;
   }

   public String getCput() {
      return cput;
   }

   public void setCput(String cput) {
      this.cput = cput;
   }

   public String getWalltimeReq() {
      return walltimeReq;
   }

   public void setWalltimeReq(String walltimeReq) {
      this.walltimeReq = walltimeReq;
   }

   public String getWalltime() {
      return walltime;
   }

   public void setWalltime(String walltime) {
      this.walltime = walltime;
   }

   public Integer getMemKBReq() {
      return memKBReq;
   }

   public void setMemKBReq(Integer memKBReq) {
      this.memKBReq = memKBReq;
   }

   public Integer getMemKB() {
      return memKB;
   }

   public void setMemKB(Integer memKB) {
      this.memKB = memKB;
   }

   public Integer getVmemKBReq() {
      return vmemKBReq;
   }

   public void setVmemKBReq(Integer vmemKBReq) {
      this.vmemKBReq = vmemKBReq;
   }

   public Integer getVmemKB() {
      return vmemKB;
   }

   public void setVmemKB(Integer vmemKB) {
      this.vmemKB = vmemKB;
   }

   public String getSoftware() {
      return software;
   }

   public void setSoftware(String software) {
      this.software = software;
   }

   public String getSubmithost() {
      return submithost;
   }

   public void setSubmithost(String submithost) {
      this.submithost = submithost;
   }

   public String getHostlist() {
      return hostlist;
   }

   public void setHostlist(String hostlist) {
      this.hostlist = hostlist;
   }

   public Integer getExitStatus() {
      return exitStatus;
   }

   public void setExitStatus(Integer exitStatus) {
      this.exitStatus = exitStatus;
   }

   public String getScript() {
      return script;
   }

   public void setScript(String script) {
      this.script = script;
   }
}
