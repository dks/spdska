/* vim: set tabstop=2: */
import java.sql.*;

public class h2t{
	public static void main(String args[]){
		try{
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection("jdbc:h2:test","sa","");
			Statement s = conn.createStatement();
			//s.execute("DROP TABLE data;");
			//s.execute("CREATE TABLE data(id int AUTO_INCREMENT primary key,txt VARCHAR(5));");
			s.executeUpdate("INSERT INTO data VALUES (null,'TEST');");
			ResultSet rs = s.executeQuery("SELECT * FROM data;");
			while(rs.next()){
				System.out.println(rs.getInt(1)+" "+rs.getString(2));
			}
			rs.close();
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
