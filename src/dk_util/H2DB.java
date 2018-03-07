/* vim: set tabstop=2: */
package dk_util;

import java.sql.*;

public class H2DB{
	private static H2DB instance;
	protected static Connection conn;
	public static void init(){
		if (instance == null) { instance = new H2DB(); } 
		try{
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection("jdbc:h2:res/test","sa","");
			Statement s = conn.createStatement();
			s.execute("DROP TABLE data IF EXISTS;");
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
  public static void sim(){
    int lastparent=0;
    int lastchild=0;

    System.out.println("TEST H2DB");
		if (instance == null) { instance = new H2DB(); } 
		try{
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:res/test","sa","");
			Statement s = conn.createStatement();
      ResultSet rs;

			s.execute("DROP TABLE data2 IF EXISTS;");
			s.execute("DROP TABLE dataref IF EXISTS;");
			s.execute("CREATE TABLE IF NOT EXISTS data2(id int AUTO_INCREMENT"
        +" primary key, txt VARCHAR(50), objtype smallint unsigned);");
			s.execute("CREATE TABLE IF NOT EXISTS dataref(chid int ,parid int);");
			s.executeUpdate("INSERT INTO data2 VALUES (null,'Здание',1);");
			rs = s.executeQuery("SELECT LAST_INSERT_ID();");
			while(rs.next()){ lastparent = rs.getInt(1); }
      
      insertObj(lastparent,"Стена1",2);
      insertObj(lastparent,"Стена2",2);
      insertObj(lastparent,"Стена3",2);
      insertObj(lastparent,"Стена4",2);
      insertObj(lastparent,"Перекрытие1",3);
      insertObj(lastparent,"Перекрытие2",3);
      insertObj(lastparent,"Объем1",4);
      linkObj(8,2);
      linkObj(8,3);
      linkObj(8,4);
      linkObj(8,5);

			rs = s.executeQuery("SELECT * FROM data2;");
			while(rs.next()){
				System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getInt(3));
			}
      System.out.println("----------------");
			rs = s.executeQuery("SELECT * FROM dataref;");
			while(rs.next()){
				System.out.println(rs.getInt(1)+" "+rs.getInt(2));
			}
			rs.close();
			conn.close();
		}catch(Exception e){
      System.out.println("ERROR(3): "+e);
			//Log.m("ERROR(3): "+e);
		}
  }
  public static int insertObj(int refparent, String obj, int objType){
    int lastrec=0;
    try{
      int lastchild=0;
      Statement s = conn.createStatement();
      s.executeUpdate("INSERT INTO data2 VALUES (null,'"+obj+"',"+objType+");");
      ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID();");
      while(rs.next()){ lastchild = rs.getInt(1); }
      lastrec=lastchild;
      s.executeUpdate("INSERT INTO dataref VALUES ("+lastchild+","+refparent+");");
    }catch(Exception e){
      System.out.println("ERROR(): "+e);
			//Log.m("ERROR(3): "+e);
		}
    return lastrec;
  }
  public static void linkObj(int refparent, int refchild){
    try{
      Statement s = conn.createStatement();
      s.executeUpdate("INSERT INTO dataref VALUES ("+refchild+","+refparent+");");
    }catch(Exception e){
      System.out.println("ERROR(): "+e);
			//Log.m("ERROR(3): "+e);
		}
  }
}
