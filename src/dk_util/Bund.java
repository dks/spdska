/* vim: set tabstop=2: */
package dk_util;

import java.util.*;

public class Bund{
	private static Bund instance;
	private static Locale myloc;
	private static ResourceBundle rb;

	protected Bund(){
		//myloc = Locale.JAPANESE; 
		//myloc = Locale.getDefault();
		myloc = new Locale("RU");
		resetBundle();
	}

	public static void init(){
		if (instance == null) { instance = new Bund(); } 
	}

	public static String p(String s){
		return rb.getString(s);
	}

	public static void resetBundle(){
		rb = ResourceBundle.getBundle("i18n/MB", myloc);  
	}

	public static void setLocale(String l){
		myloc = new Locale(l);
		resetBundle();
	}
	
	public static Locale getLocale(){
		return myloc;
	}  
}
