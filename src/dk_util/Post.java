package dk_util;

import java.util.*;
import dk_core.*;

public class Post{
 private static Post instance;
 private static HashMap<String,Var> hm = new HashMap<String,Var>();
 private static StringBuffer lg = new StringBuffer("<html>");
 private static StringBuffer sb = new StringBuffer("<html>");
 private static StringBuffer eb = new StringBuffer();
 private static StringBuffer transit = new StringBuffer();

 protected Post(){ }

 public static void setVariables(HashMap<String,Var> newmap) { hm = newmap; }
 public static HashMap<String,Var> getVariables() { return hm;}

 public static void msg(String s){ sb.append(s).append("<br />"); }
 public static void msg(Object s){ sb.append(s.toString()).append("<br />"); }
 public static String getMessages(){ return sb.toString()+"</html>"; }

 public static void log(String s){ lg.append(s).append("<br />"); }
 public static void log(Object s){ lg.append(s.toString()).append("<br />"); }
 public static String getLog(){ return lg.toString()+"</html>"; }

 public static void setTransit(StringBuffer t){ transit = t; }
 public static StringBuffer getTransit(){ return transit; }

 public static void err(Exception e){ eb.append(e.getMessage()).append("<br />"); }
 public static void err(String e){ eb.append(e).append("<br />"); }
 public static void err(Object e){ eb.append(e.toString()).append("<br />"); }
 public static String getErrors() { return eb.toString(); }
}
