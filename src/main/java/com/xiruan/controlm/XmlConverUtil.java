package com.xiruan.controlm;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class XmlConverUtil {


  public static Document getDocument(List<Map<String, Object>> list) {
    assert list!=null;
    Document document = DocumentHelper.createDocument();
    Element deftable = document.addElement("DEFTABLE ");

//    deftable.addAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
//    deftable.addAttribute("xsi:noNamespaceSchemaLocation","Folder.xsd");
    Namespace nt = new Namespace("xsi", "http://www.worg/2001/XMLSchema-instance");
    deftable.add(nt);
    deftable.addAttribute("xsi:noNamespaceSchemaLocation", "Folder.xsd");

    Element folderElement = deftable.addElement("FOLDER");
    //添加properties中FOLDER节点固定属性
    for(String folderKey :PropertiesInit.folderMap.keySet()){
      folderElement.addAttribute(folderKey,PropertiesInit.folderMap.get(folderKey));
    }
    for (Map<String, Object> data : list) {
      Element job = folderElement.addElement("JOB");
      //添加properties中job节点固定属性
      for(String jobKey :PropertiesInit.jobMap.keySet()){
        job.addAttribute(jobKey,PropertiesInit.jobMap.get(jobKey));
      }
      //将sql查询出来的数据作为属性插入
      for (String key : data.keySet()) {
        if (data.get(key) != null) {
          job.addAttribute(key, data.get(key).toString());
        }else{
          job.addAttribute(key, "");
        }
      }
      String jobName ="";
      if(data.get(PropertiesInit.job_field)!=null){
        jobName=data.get(PropertiesInit.job_field).toString();
      }
      for (Map<String, Object> processData : list) {
        //添加IN
        if (processData.get(PropertiesInit.aft_job_field) != null && processData.get(PropertiesInit.aft_job_field).equals(jobName)) {
          Element incondElemnt = job.addElement("INCOND");
          incondElemnt.addAttribute("NAME", processData.get(PropertiesInit.job_field) + "-TO-" + jobName);
          incondElemnt.addAttribute("ODATE", "ODAT");
          incondElemnt.addAttribute("AND_OR", "A");
        }
      }
      for (Map<String, Object> processData : list) {
        //in必须要有个与之配对的
        if(processData.get(PropertiesInit.aft_job_field)!=null && processData.get(PropertiesInit.aft_job_field).equals(jobName)){
          Element outElemnt =job.addElement("OUTCOND");
          outElemnt.addAttribute("NAME",processData.get(PropertiesInit.job_field)+"-TO-"+jobName);
          outElemnt.addAttribute("ODATE","ODAT");
          outElemnt.addAttribute("SIGN","-");
        }
      }
      for (Map<String, Object> processData : list) {
        //添加out
        if (processData.get(PropertiesInit.pre_job_field) != null && processData.get(PropertiesInit.pre_job_field).equals(jobName)) {
          Element outElemnt = job.addElement("OUTCOND");
          outElemnt.addAttribute("NAME", jobName + "-TO-" + processData.get(PropertiesInit.job_field));
          outElemnt.addAttribute("ODATE", "ODAT");
          outElemnt.addAttribute("SIGN", "+");
        }
      }


    }
    return document;
  }

  public static String doc2String(Object document) {
    String s = "";
    try {
      // 使用输出流来进行转化
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      // 使用UTF-8编码
      OutputFormat format = new OutputFormat("   ", true, "UTF-8");
      XMLWriter writer = new XMLWriter(out, format);
      writer.write(document);
      s = out.toString("UTF-8");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return s;
  }

  public static  void docXml(Object document,String filePath) {
    Writer writer = null;
    OutputFormat format = null;
    XMLWriter xmlwriter = null;
    //将定义好的内容写入xml文件中
    try {
      //进行格式化
      format = OutputFormat.createPrettyPrint();
      //设定编码
      format.setEncoding("UTF-8");
      xmlwriter = new XMLWriter(new FileOutputStream(filePath), format);
      xmlwriter.write(document);
      xmlwriter.flush();
      xmlwriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}