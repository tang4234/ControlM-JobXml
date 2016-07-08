package com.xiruan.controlm;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Runctmcreate {


  public static void run(final List<Map<String, Object>> list) {
    assert list != null;

    Thread myThread = new Thread() {
      public void run() {
        //System.out.println("myThead is running");


        String nowTime = "-" + System.currentTimeMillis();

        for (Map<String, Object> data : list) {
          String job = "";
          //添加properties中job节点固定属性
          for (String jobKey : PropertiesInit.jobMap.keySet()) {
            //job.addAttribute(jobKey,PropertiesInit.jobMap.get(jobKey));

            job += " -" + jobKey + " " + PropertiesInit.jobMap.get(jobKey);

          }
          //将sql查询出来的数据作为属性插入
          for (String key : data.keySet()) {
            if (data.get(key) != null && !key.equals("INCOND") && !key.equals("OUTCOND")) {
              //job.addAttribute(key, data.get(key).toString());
              if (key.equals("CMDLINE")) {
                job += " -" + key + " \"" + data.get(key) + "\"";
              } else {
                job += " -" + key + " " + data.get(key);
              }
            } else {
              //job.addAttribute(key, "");
            }
          }
          String jobName = "";
          //if(data.get(PropertiesInit.job_field)!=null){
          jobName = data.get(PropertiesInit.job_field).toString();
          // }
          for (Map<String, Object> processData : list) {
            //添加OUT
            String pre = (String) processData.get(PropertiesInit.pre_job_field);//split
            if (pre != null) {
              pre = pre.toString().trim();
              String[] pres = pre.split(";");
              for (String jobpre : pres) {
                if (jobpre != null && jobpre.equals(jobName)) {
                  job += " -OUTCOND" + " " + jobName + "-TO-" + processData.get(PropertiesInit.job_field) + nowTime + " ODAT ADD";
                }
              }

            }
          }

          for (Map<String, Object> processData : list) {
            //添加IN
            String aft = (String) processData.get(PropertiesInit.aft_job_field);//split
            if (aft != null) {
              aft = aft.toString().trim();
              String[] afts = aft.split(";");

              for (String jobaft : afts) {
                //System.out.println("前置作业："+jobaft+"-----当前作业"+jobName+"--get:"+PropertiesInit.job_field);
                if (jobaft != null && jobaft.equals(jobName)) {
                  job += " -INCOND" + " " + processData.get(PropertiesInit.job_field) + "-TO-" + jobName + nowTime + " ODAT AND";
                  job += " -OUTCOND" + " " + processData.get(PropertiesInit.job_field) + "-TO-" + jobName + nowTime + " ODAT DEL";

                }
              }

            }
          }

          startcmd(job);

          //sleep 100;
        }

      }
    };
    myThread.start();
    // startcmd("ls -lrt");
    //start ctm command
    // System.out.println(job);

  }

  public static void startcmd(String cmd) {

    cmd = "ctmcreate" + cmd;
    String[] command = {"/bin/csh", "-c", cmd};
    System.out.println(cmd);
    try {
      List<String> processList = new ArrayList<String>();
      Process process = Runtime.getRuntime().exec(command);
      BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line = "";
      while ((line = input.readLine()) != null) {
        processList.add(line);
      }
      input.close();
      for (String line1 : processList) {
        System.out.println(line1);
      }
      int exitValue = process.waitFor();
      if (0 != exitValue) {
        //log.error("call shell failed. error code is :" + exitValue);
        System.out.println("Add Job fail;Exit:" + exitValue);
      } else {
        System.out.println("Add Job success!;Exit:" + exitValue);
      }

    } catch (Throwable e) {
      //log.error("call shell failed. " + startcmd(job););
      System.out.println("Run Command Failed:" + e.toString());
    }
    System.out.println("---------------------------------------------------------------------------------------------");
    try {

      Thread.sleep(PropertiesInit.sleeptime);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}