/* vim: set tabstop=2: */
package dk_util;

import java.sql.*;

/**
 * This class implements project database. 
 * Tables structure as follows:
 *
 * Table that stores all posible object types
 * +-----------------------------------------------------------+
 * | Types                                                     |
 * +-----------------------------------------------------------+
 * | tid smallint unsigned NOT NULL AUTO_INCREMENT primary key |
 * | desc VARCHAR(255)                                         |
 * +-----------------------------------------------------------+
 *
 * Table that stores all objects in this projects
 * +------------------------------------------------------+
 * | Objects                                              |
 * +------------------------------------------------------+
 * | oid int unsigned NOT NULL AUTO_INCREMENT primary key | 
 * | txt VARCHAR(255) NOT NULL                            |
 * | tid smallint unsigned NOT NULL,                      |
 * |  foreign key (tid) references Types(tid)             |
 * +------------------------------------------------------+
 * 
 * Table that links objects
 * +-----------------------------+
 * | Links                       |
 * +-----------------------------+
 * | objid int unsigned NOT NULL |
 * | refid ind unsigned NOT NULL |
 * +-----------------------------+ 
 *
 */
public class H2DB{
	private static H2DB instance;
	protected static Connection conn;
	public static void init(){
		if (instance == null) { instance = new H2DB(); } 
		try{
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:res/test","sa","");
      createDB();
      fillDB();
  //TODO:Delete
      reportDB();
		}catch(Exception e){
			Log.m("ERROR(3): Ошибка инициализации базы данных. "+e);
		}
	}
  public static void finish(){
		try{
      conn.close();
		}catch(Exception e){
			Log.m("ERROR(4): Ошибка завершения работы базы данных. "+e);
		}
  }
  private static void createDB(){
    try{
      Statement s = conn.createStatement();
      s.execute("DROP TABLE Types IF EXISTS;"); //TODO: turn off on production
      s.execute("DROP TABLE Objects IF EXISTS;"); //TODO: turn off on prodution
      s.execute("DROP TABLE Links IF EXISTS;"); //TODO: turn off on prodution
      s.execute("CREATE TABLE IF NOT EXISTS Types(tid smallint unsigned NOT NULL AUTO_INCREMENT primary key,"
          +" txt VARCHAR(255));");
      s.execute("CREATE TABLE IF NOT EXISTS Objects(oid int unsigned NOT NULL AUTO_INCREMENT primary key,"
          +" txt VARCHAR(255) NOT NULL, tid smallint unsigned NOT NULL, "
          +" foreign key (tid) references Types(tid));");
      s.execute("CREATE TABLE IF NOT EXISTS Links(objid int unsigned NOT NULL, refid int unsigned NOT NULL);");
    }catch(Exception e){
			Log.m("ERROR(5): Ошибка создания базы данных "+e);
		}
  }
  public static void fillDB(){
    int lastparent=0;
    int lastchild=0;
		try{
			Statement s = conn.createStatement();
      ResultSet rs;
      // Fill data types - Start
			s.executeUpdate("INSERT INTO Types VALUES (1,'Здание');");
			s.executeUpdate("INSERT INTO Types VALUES (2,'Стена');");
			s.executeUpdate("INSERT INTO Types VALUES (3,'Перекрытие');");
			s.executeUpdate("INSERT INTO Types VALUES (4,'Объем');");
			s.executeUpdate("INSERT INTO Types VALUES (5,'Окно');");
      // Fill data types - End
			s.executeUpdate("INSERT INTO Objects VALUES (null,'Дом Пупкина',1);");
			rs = s.executeQuery("SELECT LAST_INSERT_ID();");
			while(rs.next()){ lastparent = rs.getInt(1); }
			rs.close();
      insertObj(lastparent,"Западная стена",2);
      insertObj(lastparent,"Южная стена",2);
      insertObj(lastparent,"Восточная стена",2);
      insertObj(lastparent,"Северная стена",2);
      insertObj(lastparent,"Перекрытие над подвалом",3);
      insertObj(lastparent,"Перекрытие чердачное",3);
      insertObj(lastparent,"Комната единственная",4);
      linkObj(8,2);
      linkObj(8,3);
      linkObj(8,4);
      linkObj(8,5);
		}catch(Exception e){
			Log.m("ERROR(6): Ошибка заполнения базы данных. "+e);
		}
  }

  public static void reportDB(){
		try{
      Statement s = conn.createStatement();
      //ResultSet rs = s.executeQuery("SELECT * FROM Objects;");
      ResultSet rs = s.executeQuery("SELECT t1.oid,t1.txt,t2.txt FROM Objects AS t1"
        +" JOIN Types AS t2 ON t1.tid=t2.tid;");
			while(rs.next()){
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3));
			}
      System.out.println("----------------");
			rs = s.executeQuery("SELECT * FROM links;");
			while(rs.next()){
				System.out.println(rs.getInt(1)+" "+rs.getInt(2));
			}
      System.out.println("----------------");
			rs = s.executeQuery("SELECT o.txt,p.txt FROM links AS l JOIN objects AS o "
        +" ON l.objid=o.oid JOIN objects AS p ON l.refid=p.oid WHERE l.refid='8';");
			while(rs.next()){
				System.out.println(rs.getString(1)+"\t "+rs.getString(2));
			}
			rs.close();
		}catch(Exception e){
			Log.m("ERROR(7): Ошибка чтения базы данных. "+e);
		}
  }

  public static int insertObj(int ref, String obj, int objType){
    int lastrec=0;
    try{
      int objid=0;
      Statement s = conn.createStatement();
      s.executeUpdate("INSERT INTO Objects VALUES (null,'"+obj+"',"+objType+");");
      ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID();");
      while(rs.next()){ objid = rs.getInt(1); }
      lastrec=objid;
      s.executeUpdate("INSERT INTO Links VALUES ("+objid+","+ref+");");
			rs.close();
    }catch(Exception e){
      System.out.println("ERROR(): "+e);
			//Log.m("ERROR(3): "+e);
		}
    return lastrec;
  }

  public static void linkObj(int ref, int obj){
    try{
      Statement s = conn.createStatement();
      s.executeUpdate("INSERT INTO Links VALUES ("+obj+","+ref+");");
    }catch(Exception e){
      System.out.println("ERROR(): "+e);
			//Log.m("ERROR(3): "+e);
		}
  }
}
