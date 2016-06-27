package com.xiruan.controlm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by chen on 2016/6/26.
 */
public class PropertiesInit {

//  static String fileName = "E:\\data-xml-jar\\job_init.properties";
  static String fileName = "job_init.properties";

  public static String dbUrl;
  public static String dbUser;
  public static String dbPassword;
  public static String dbDriver;

  public static String source_sql;
  public static String job_field;
  public static String pre_job_field;
  public static String aft_job_field;

  public static final  String jobParentKey="JOB.";
  public static Map<String, String> jobMap = new HashMap<String, String>();
  public static final  String folderParentKey="FOLDER.";
  public static Map<String, String> folderMap = new HashMap<String, String>();



  static {
    FileInputStream inputStream = null;
    ResourceBundle resourceBundle = null;
    try {
      inputStream = new FileInputStream(fileName);
      resourceBundle = new PropertyResourceBundle(inputStream);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    dbUrl = resourceBundle.getString("datasource.url");
    dbUser = resourceBundle.getString("datasource.username");
    dbPassword = resourceBundle.getString("datasource.password");
    dbDriver = resourceBundle.getString("datasource.driver-class-name");

    source_sql = resourceBundle.getString("source_sql");
    job_field = resourceBundle.getString("job_field");
    pre_job_field = resourceBundle.getString("pre_job_field");
    aft_job_field = resourceBundle.getString("aft_job_field");



    Enumeration<String> keys = resourceBundle.getKeys();
    while (keys.hasMoreElements()) {
      String key =keys.nextElement();
      if(key!=null && !"".equals(key) && key.startsWith(jobParentKey)){
        jobMap.put(key.replaceFirst(jobParentKey,""),resourceBundle.getString(key));
      }else   if(key!=null && !"".equals(key) && key.startsWith(folderParentKey)){
        folderMap.put(key.replaceFirst(folderParentKey,""),resourceBundle.getString(key));
      }
    }

  }

}
