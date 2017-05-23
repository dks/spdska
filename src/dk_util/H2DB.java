/* vim: set tabstop=2: */
package dk_util;

import java.sql.*;

public class H2DB{
	private static H2DB instance;
	public static void init(){
		if (instance == null) { instance = new H2DB(); } 
		try{
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection("jdbc:h2:../res/test","sa","");
			Statement s = conn.createStatement();
//			s.execute("DROP TABLE data;");
			s.execute("CREATE TABLE IF NOT EXISTS data(id int AUTO_INCREMENT primary key,txt VARCHAR(5));");
			s.executeUpdate("INSERT INTO data VALUES (null,'TEST');");
			ResultSet rs = s.executeQuery("SELECT * FROM data;");
			while(rs.next()){
				System.out.println(rs.getInt(1)+" "+rs.getString(2));
			}
			rs.close();
			conn.close();
		}catch(Exception e){
			Log.m("ERROR(3): "+e);
		}
	}
}
