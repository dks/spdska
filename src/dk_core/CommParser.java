/* vim:set tabstop=2: */
/* vim:set shiftwidth=2: */
package dk_core;

import dk_util.*;
import dk_gui.*;
import dk_data.*;
import dk_core.*;
import dk_core.CalcParser;
import java.io.*;
import java.util.*;

public class CommParser{
	static int mode = 0;
	public static HashMap<String,Var> variables = new HashMap<String,Var>();
	public static HashMap<Integer,Formula> formulae = new HashMap<Integer,Formula>();
	public static void parse(){
		try(BufferedReader br = new BufferedReader(new FileReader("cla/res/calc1.lec"))){
			for(String line; (line = br.readLine()) != null; ){
// обработать пустые строки
				String[] words = line.split("\\s");
				preSetMode(words[0]);
				switch(mode){
					case 0: pass(line); break;
					case 1: parseVariables(line); break;
					case 2: parseFormulae(line); break;
					case 3: setVariableValues(line); break;
					case 4: doCalculation(line); break;
				}
				postSetMode(words[0]);
			}
		} catch (Exception ex) {
			DBG.erep(ex);
		}
	}// EOM main
	
	public static void postSetMode(String newmode){
		switch(newmode){
			case "--НОП--": mode=1; break;
			case "--НОФ--": mode=2; break;
			case "--НОЗ--": mode=3; break;
			case "--НИР--": mode=4; break;
	}}
	public static void preSetMode(String newmode){
		switch(newmode){
			case "--КОП--":
			case "--КОФ--":
			case "--КОЗ--":
			case "--КИР--": mode=0;
	}}
	public static void parseVariables(String line){
		String[] vars = line.split("\\s");
		if (Character.isDigit(vars[0].charAt(0))) 
			throwParserError("Первым символом переменной не может быть цифра");
		String varName = vars[0].trim();
		String unit = correctOperators(vars[1]);
		String varDesc = ""; 
		for(int i = 2; i<vars.length; i++) varDesc+=" "+vars[i];
			variables.put(varName,new Var(varName,0.0,unit,varDesc,""));
	}
	public static void parseFormulae(String line){
		String[] vars = line.split("\\s");
		if (!Character.isDigit(vars[0].charAt(0))) 
			throwParserError("Первым указывается номер формулы");
		int formulaNumber = Integer.parseInt(vars[0]);
		String formulaFull = "";
		for(int i = 1; i<vars.length; i++) formulaFull+=vars[i];
		formulaFull = correctOperators(formulaFull);
		String[] formulaParts = formulaFull.split("=");
		//formulaName=formulaParts[0];
		//formulaText=formulaParts[1];
		formulae.put(formulaNumber,new Formula(formulaParts[0],formulaParts[1]));
	}
	public static void setVariableValues(String line){
		String[] vars = line.split("=");
		if (Character.isDigit(vars[0].charAt(0))) 
			throwParserError("Первой должна быть переменная");
		String varName = vars[0].trim();
		double val = Double.parseDouble( ((correctOperators(vars[1])).trim()).replace(",",".") );
		if (variables.containsKey(varName)){
			variables.get(varName).setValue(val);
		} else {
			DBG.erep("Необъявленная переменная");
		}
	}
	public static void pass(String s){
		Porter.msg(s);
	}
	public static String correctOperators(String s){
		return (s.replace("**","^")).replace("\\","/");
	}
	public static void throwParserError(String err){
		DBG.erep(err);
		//System.exit(125);
	}
	public static void doCalculation(String line){
//		for(Map.Entry<String,Var> entry : variables.entrySet()) 
//			out.println(entry.getKey()+"=> ["+entry.getValue()+"]");
//		for(Map.Entry<Integer,String> entry : formulae.entrySet()) 
//			out.println(entry.getKey()+"=> ["+entry.getValue()+"]");
		Porter.setVariables(variables);
		int formulaIndex = 1;
		try{
			formulaIndex = Integer.parseInt(line.substring(line.indexOf('(')+1,line.indexOf(')')));
		} catch (NumberFormatException e){
			DBG.erep("ошибка в номере формулы");
		}
		Formula formula = formulae.get(formulaIndex);
		String s = formula.getText();
		Porter.msg("Расчитываю выражение:"+s);
		Porter.msg(new CalcParser().parse(s));
	}
}
