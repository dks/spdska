/* vim: set tabstop=2: */
package dk_util;

import java.io.*;
import dk_util.*;

/**
 * MuLTRU - Multi Line Text Reader Utility
 */
public class Multru{
	public static String getMLS(String fn){
		Reader input = null;
		try{
			input = new InputStreamReader(
				Multru.class.getResourceAsStream("../"+fn),"UTF-8");
		}catch(UnsupportedEncodingException uee){
			System.out.println(uee);
		}catch(NullPointerException npe){
			DBG.erep("Resource is missing!");
		}
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[4096];
		int charsRead;
		try{
			if (input != null) {
				while((charsRead = input.read(buffer)) >= 0){
					sb.append(buffer,0,charsRead);
				}
				input.close();
			} else {
				DBG.erep("Can't get input from empty data!!!");
			}
		}catch(IOException ioe){
			System.out.println(ioe);
		}
		return sb.toString();
	}
}
