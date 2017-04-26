package personnel;

import java.util.*;

public class Bund{
 private static Bund instance;
 private static Locale myloc;
 private static ResourceBundle rb;

 protected Bund(){
  myloc = Locale.getDefault();
  rb = ResourceBundle.getBundle("i18n/MB", myloc);  
 }

 public static void init(){
  if (instance == null) { instance = new Bund(); } 
 }

 public static String p(String s){
  return rb.getString(s);
 }

 public static void setLocale(){
 }
}