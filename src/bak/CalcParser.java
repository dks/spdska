/* vim:set tabstop=2: */
/* vim:set shiftwidth=2: */
package dk_core;

import java.util.HashMap;
import java.math.BigDecimal;
import dk_util.*;

public class CalcParser {
	int pos = -1, c;
	String str = "";
	boolean itIsVar;
	HashMap<String,BigDecimal> hm = new HashMap<String,BigDecimal>();
	
	public CalcParser(){
		hm.put("икс",new BigDecimal(24));
	}

	void eatChar() { c = (++pos < str.length()) ? str.charAt(pos) : -1;	}
	void eatSpace() { while (Character.isWhitespace(c)) eatChar(); }
	public BigDecimal parse(String s) {
		this.str=s;
		eatChar();
		BigDecimal v = parseExpression();
		if (c != -1) throw new RuntimeException("Unexpected: " + (char)c);
		return v;
	}
	// уровень 1 - выражение
	BigDecimal parseExpression() {
		BigDecimal v = parseTerm();
		for (;;) {
			eatSpace();
			if (c == '+') { // addition
				eatChar();
				//v += parseTerm();
				v = v.add(parseTerm());
			} else if (c == '-') { // subtraction
				eatChar();
				//v -= parseTerm();
				v = v.subtract(parseTerm());
			} else {
				return v;
	}}}
	// уровень 2 - сложение и вычитание
	BigDecimal parseTerm() {
		BigDecimal v = parseFactor();
		for (;;) {
			eatSpace();
			if (c == '/') { // division
				eatChar();
				//v /= parseFactor();
				v = v.divide(parseFactor());
			} else if (c == '*' || c == '(') { // multiplication
				if (c == '*') eatChar();
				//v *= parseFactor();
				v = v.multiply(parseFactor());
			} else {
				return v;
	}}}
	// уровень 3 - умножение, деление, переменные, функции, степень
	BigDecimal parseFactor() {
		BigDecimal v;
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

			v = new BigDecimal(0);
			//формируем цифры
			while ((c >= '0' && c <= '9') || c == '.') {
				sb.append((char)c);
				eatChar();
			}
			if (sb.length() != 0) v = new BigDecimal(sb.toString());
////////////////////////////////////////
			//формируем переменные
			itIsVar = false;
			while (Character.isLetter(c)){
				sb.append((char)c);
				eatChar();
				itIsVar = true;
			}
			if (c == '(') { 
				eatChar();
////////////////////// ТУТ БУДУТ ВСЕ МАТЕМАТИЧЕСКИЕ ФУНКЦИИ /////////////////////
				if ((sb.toString()).equals("корень")) {
					//v = Math.sqrt(parseExpression());
					v = BigDecimalMath.sqrt(v);
				} else throw new RuntimeException("Unexpected command: " + sb);
				if (c == ')') eatChar();
/////////////////////////////////////////////////////////////////////
				return v;
			}
			if (itIsVar) v = hm.get(sb.toString());
			if (itIsVar) { 
				System.out.println(Porter.getVariables());
			}

			if (sb.length() == 0) throw new RuntimeException("Unexpected: " + (char)c);
///////////////////////////////////////////
		}
		eatSpace();
		if (c == '^') { // exponentiation
			eatChar();
			//v = Math.pow(v, parseFactor());
			v = v.pow(parseFactor());
		}
		if (negate) v = v.negate(); // exponentiation has higher priority than unary minus: -3^2=-9
		return v;
	}
}
