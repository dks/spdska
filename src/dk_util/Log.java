/* vim: set tabstop=2: */
/* vim: set shiftwidth=2: */

package dk_util;

import java.util.*;
import javax.swing.*;

public class Log{
	private static Log instance;
	private static StringBuffer lg = new StringBuffer("<html>");
	private static JLabel statWin = new JLabel();

	protected Log(){ }
	protected static void update(){ statWin.setText(lg.toString()+"</html>"); }

	public static void m(String s){ 
    System.out.println(s);
    lg.append(s).append("<br />"); update();
  }
	public static void m(Exception e){ 
		lg.append(e.getMessage()).append("<br />"); 
		update(); 
	}
	public static void m(Object s){ 
		lg.append(s.toString()).append("<br />"); 
		update(); 
	}
	public static String getLog(){ return lg.toString()+"</html>"; }
	public static JLabel getStatusWindow(){ return statWin; }
}
