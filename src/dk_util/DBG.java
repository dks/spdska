package dk_util;

/**
 * DBG stands for "Debug" is a class providing shorthands for tracing and debugging.
 */
public class DBG{
	/**
	 * When V set to true - verbose output, keep silent otherwise.
	 */
	public static boolean V = true;
	/**
	 * Shorthand for sending text to STDOUT.
	 * Always send output, ignores verbosity setting.
	 *
	 * @param s text to send to STDOUT.
	 */ 
 	public static void say(String s){
		System.out.println(s);
	}
 	public static void say(Object s){
		System.out.println(s);
	}
	/**
	 * Sends array contents to STDOUT.
	 *
	 * @param array int array to be dumped.
	 */
	public static void dump(int[] array){
		StringBuilder s = new StringBuilder();
		for(int i=0;i<array.length;i++) s.append("["+i+"]="+array[i]+" ");
		if (V==true) System.out.println("CLASS:["
			+new Throwable().getStackTrace()[1].getClassName()
			+"] METHOD:["
			+new Throwable().getStackTrace()[1].getMethodName()
			+"] INT ARRAY DUMP:\t"+s);
	}
	/**
	 * Sends array contents to STDOUT.
	 *
	 * @param array int array to be dumped.
	 * @param message message to output together with debugging info.
	 */
	public static void dump(String message,int[] array){
		StringBuilder s = new StringBuilder();
		for(int i=0;i<array.length;i++) s.append("["+i+"]="+array[i]+" ");
		if (V==true) System.out.println("CLASS:["
			+new Throwable().getStackTrace()[1].getClassName()
			+"] METHOD:["
			+new Throwable().getStackTrace()[1].getMethodName()
			+"] INT ARRAY DUMP:\t<"+message+"> "+s);
	}
	/**
	 * Sends object string representation to STDOUT.
	 *
	 * @param obj Object to be dumped.
	 */
	public static void dump(Object obj, Object... objs){
		if (V==true) { 
			System.out.println("CLASS:["
			+new Throwable().getStackTrace()[1].getClassName()
			+"] METHOD:["
			+new Throwable().getStackTrace()[1].getMethodName()
			+"] OBJECT DUMP:\t"+obj);
			if (objs.length != 0){
				for(Object o : objs){
					System.out.println("CLASS:["
					+new Throwable().getStackTrace()[1].getClassName()
					+"] METHOD:["
					+new Throwable().getStackTrace()[1].getMethodName()
					+"] OBJECT DUMP:\t"+o);
	}}}}
	/**
	 * Sends object string representation to STDOUT.
	 *
	 * @param message message to output together with debugging info.
	 * @param obj Object to be dumped.
	 */
	public static void dump(String message,Object obj){
		if (V==true) System.out.println("CLASS:["
			+new Throwable().getStackTrace()[1].getClassName()
			+"] METHOD:["
			+new Throwable().getStackTrace()[1].getMethodName()
			+"] OBJECT DUMP:\t<"+message+"> "+obj);
	}
	/**
	 * Sends debugging info to STDOUT.
	 *
	 * @param s message to output together with debugging info.
	 */
	public static void info(String s){
		if (V==true) System.out.println("CLASS:["
			+new Throwable().getStackTrace()[1].getClassName()
			+"] METHOD:["
			+new Throwable().getStackTrace()[1].getMethodName()
			+"] INFO:\t"+s);
	}
	/**
	 * Sends caller debugging info to STDOUT.
	 */
 	public static void getCaller(){
		if (V==true) System.out.println("CLASS:["
			+new Throwable().getStackTrace()[2].getClassName()
			+"] METHOD:["
			+new Throwable().getStackTrace()[2].getMethodName()
			+"] <--- CALLER OF:"
			+" "+new Throwable().getStackTrace()[1].getClassName()
			+"."+new Throwable().getStackTrace()[1].getMethodName()
			+"()");
	}
	/**
	 * Error report - sends debugging info to STDERR.
	 *
	 * @param s message to output together with error report.
	 */
	public static void erep(String s){
		if (V==true) System.err.println("CLASS:["
			+new Throwable().getStackTrace()[1].getClassName()
			+"] METHOD:["
			+new Throwable().getStackTrace()[1].getMethodName()
			+"] ERROR:\t"+s);
	}
	/**
	 * Error report - sends debugging info to STDERR.
	 *
	 * @param e exception that contains message to output together with error report.
	 */
	public static void erep(Exception e){
		if (V==true) System.err.println("CLASS:["
			+new Throwable().getStackTrace()[1].getClassName()
			+"] METHOD:["
			+new Throwable().getStackTrace()[1].getMethodName()
			+"] ERROR:\t"+e.getMessage());
	}
}//EOC DBG

/* vim: set tabstop=2: */
