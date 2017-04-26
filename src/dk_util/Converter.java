package dk_util;

import java.util.Vector;

public class Converter{
	public static Object[] convertVectorToArray(Vector<?> v) {  
		Object[] o = v.toArray();  
		for(int i = 0; i < o.length; i++)  
			if(o[i] instanceof Vector)  
				o[i] = convertVectorToArray((Vector<?>)o[i]);  
		return o;  
	}  
}
/* vim: set tabstop=2: */
