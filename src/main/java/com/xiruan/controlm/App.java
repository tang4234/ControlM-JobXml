package com.xiruan.controlm;


import org.dom4j.Document;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class App {


  public static void main(String[] args) {
    String filePath = "./job.xml";
    if (args != null && args.length > 0) {
      filePath = args[0];
    }
    try {
      Connection con = DBUtil.openConnection(PropertiesInit.dbDriver, PropertiesInit.dbUrl,
              PropertiesInit.dbUser, PropertiesInit.dbPassword);
      try {
        List<Map<String, Object>> list = DBUtil.queryMapList(con, PropertiesInit.source_sql);
        Document document = XmlConverUtil.getDocument(list);
        XmlConverUtil.docXml(document,filePath);
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        DBUtil.closeConnection();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

}
