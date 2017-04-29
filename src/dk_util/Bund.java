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
		String req = "?";
		try {
			req = rb.getString(s);
		} catch (NullPointerException e){
			Post.msg("ERROR(2): Попытка получить значение из несуществующего набора ресурсов!");
		}
		return req;
	}

	public static void resetBundle(){
		try{
			rb = ResourceBundle.getBundle("i18n/MB", myloc);  
		} catch (MissingResourceException e){
			Post.msg("ERROR(1): Отсутствует файл набора ресурсов для локализации!");
		}
	}

	public static void setLocale(String l){
		myloc = new Locale(l);
		resetBundle();
	}
	
	public static Locale getLocale(){
		return myloc;
	}  
}
