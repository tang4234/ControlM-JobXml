package com.xiruan.controlm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MainApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
		      Connection con = DBUtil.openConnection(PropertiesInit.dbDriver, PropertiesInit.dbUrl,
		              PropertiesInit.dbUser, PropertiesInit.dbPassword);
		      try {
		        List<Map<String, Object>> list = DBUtil.queryMapList(con, PropertiesInit.source_sql);
		        //Document document = XmlConverUtil.getDocument(list);
		        //XmlConverUtil.docXml(document,filePath);
		        Runctmcreate.run(list);
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
