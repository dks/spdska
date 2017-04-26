/* vim:set tabstop=2: */
/* vim:set shiftwidth=2: */
package dk_core;

import java.util.HashMap;
import dk_util.*;

public class CalcParser {
	int pos = -1, c;
	String str = "";
	boolean itIsVar;
	HashMap<String,Double> hm = new HashMap<String,Double>();
	static final String prefix = "<table border=1><tr><td>"; 
	static final String infix = "</td></tr><tr><td>"; 
	static final String suffix = "</td></tr></table>"; 
	StringBuffer commBuff = new StringBuffer();
	
	public CalcParser(){}

	public static boolean isNumeric(String str){
		if (str == null) return false;
		char[] data = str.toCharArray();
		if (data.length <=0) return false;
		int index = 0;
		if (data[0]=='-' && data.length>1) index=1;
		for(;index<data.length;index++)	if (!Character.isDigit(data[index])) return false;
		return true;
	}
	void eatChar() { c = (++pos < str.length()) ? str.charAt(pos) : -1;	}
	void eatSpace() { while (Character.isWhitespace(c)) eatChar(); }
	public double parse(String s) {
		this.str=s;
		eatChar();
DBG.info(str.substring(pos,str.length())+" вход в parseExpression");
		double v = parseExpression();
DBG.info(v+" | "+str.substring(pos,str.length())+" выход из parseExpression");
		if (c != -1) DBG.erep("Неожиданный знак \""+(char)c+"\"");
		return v;
	}
	// уровень 1 - сложение и вычитание
	double parseExpression() {
DBG.info(str.substring(pos,str.length())+" вход в parseTerm1");
		double v = parseTerm();
DBG.info(v+" | "+str.substring(pos,str.length())+" выход из parseTerm1");
		for (;;) {
			eatSpace();
			if (c == '+') { // addition
				eatChar();
DBG.info(str.substring(pos,str.length())+" вход в parseTerm2");
				//v += parseTerm();
				v = v + parseTerm();
DBG.info(v+" | "+str.substring(pos,str.length())+" выход из parseTerm2");
			} else if (c == '-') { // subtraction
				eatChar();
DBG.info(str.substring(pos,str.length())+" вход в parseTerm3");
				//v -= parseTerm();
				v = v - parseTerm();
DBG.info(v+" | "+str.substring(pos,str.length())+" выход из parseTerm3");
			} else {
				return v;
	}}}
	// уровень 2 - умножение и деление
	double parseTerm() {
		int pos1 = pos;
DBG.info(str.substring(pos,str.length())+" вход в parseFactor1 "+pos);
		double v = parseFactor();
		int pos2 = pos;
DBG.info(v+" | "+str.substring(pos,str.length())+" выход из parseFactor1");
		for (;;) {
			eatSpace();
			if (c == '/') { // division
				eatChar();
				int pos3=pos;
DBG.info(str.substring(pos,str.length())+" вход в parseFactor2");
				double v1 = parseFactor();
DBG.info(v+" | "+str.substring(pos,str.length())+" выход из parseFactor2");
				if (v1 != 0) {
					//v /= v1;
					v = v / v1;
					int pos4=pos;
					commBuff.append(prefix)
						.append(str.substring(pos1,pos2))
						.append(infix)
						.append(str.substring(pos3,pos4))
						.append(suffix);
					Porter.setTransit(commBuff);
				} else DBG.erep("Деление на ноль");
			} else if (c == '*' || c == '(') { // multiplication
				if (c == '*') eatChar();
DBG.info(str.substring(pos,str.length())+" вход в parseFactor3");
				//v *= parseFactor();
				v = v * parseFactor();
DBG.info(v+" | "+str.substring(pos,str.length())+" выход из parseFactor3");
			} else {
				return v;
	}}}
	// уровень 3 - умножение, деление, переменные, функции, степень
	double parseFactor() {
		double v;
		boolean negate = false;
		eatSpace();
		if (c == '(') { // brackets
			eatChar();
			v = parseExpression();
			if (c == ')') eatChar();
		} else { // numbers
			if (c == '+' || c == '-') { // unary plus & minus
				negate = c == '-';
				eatChar();
				eatSpace();
			}
			StringBuilder sb = new StringBuilder();
			v = new Double(0);
			//формируем цифры
			while ((c >= '0' && c <= '9') || c == '.') {
				sb.append((char)c);
				eatChar();
			}
			if (sb.length() != 0) v = Double.parseDouble(sb.toString());
////////////////////////////////////////
			//формируем переменные
			itIsVar = false;
			while (Character.isLetterOrDigit(c) 
				|| Character.getType(c)==Character.CONNECTOR_PUNCTUATION
				|| Character.getType(c)==Character.INITIAL_QUOTE_PUNCTUATION
				|| c=='\'' || c=='\"'){
					sb.append((char)c);
					eatChar();
					itIsVar = true;
			}
			if (c == '(') { 
				eatChar();
// Здесь будут функции 
// надо брать из списка
// через свитч
//				if ((sb.toString()).equals("корень")) {
//					v = Math.sqrt(parseExpression());
//				} else DBG.erep("Нераспознанная функция \""+sb+"\"");
				switch((sb.toString()).toLowerCase()) {
					case "корень": v = Math.sqrt(parseExpression()); break;
					default: {
						if (isNumeric(sb.toString())) v = v * parseExpression();
						else DBG.erep("Нераспознанная функция \""+sb+"\"");
					}
				}
				if (c == ')') eatChar();
				return v;
			}
			if (itIsVar) { 
				try{
					v = (Porter.getVariables()).get(sb.toString()).getValue();
					DBG.info("Найдена переменная "+sb.toString()+" присвоено значение "+v);
				} catch(NullPointerException e) {
					DBG.erep("Переменная не найдена");
				}
			}
			if (sb.length() == 0) DBG.erep("Неожиданный знак \""+(char)c+"\"");
///////////////////////////////////////////
		}
		eatSpace();
		if (c == '^') { // exponentiation
			eatChar();
			v = Math.pow(v, parseFactor());
		}
		if (negate) v = -v; // exponentiation has higher priority than unary minus: -3^2=-9
		return v;
	}
}
