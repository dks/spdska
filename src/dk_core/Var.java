/* vim: set tabstop=2: */
package dk_core;

public class Var{
	String name;				// Название переменной
	double value;				// Значение переменной
	String unit;				// Единица измерения
	String explanation;	// Пояснение, что это за переменная
	String origin;			// Пояснение, как определяется величина переменной (источник или формула)
	public void setName(String name) { this.name = name; }
	public void setValue(double val) { this.value = val; }
	public void setUnit(String unit) { this.unit = unit; }
	public void setExplanation(String exp) { this.explanation = exp; }
	public void setOrigin(String origin) { this.origin = origin; }
	public String getName() { return name; }
	public double getValue(){ return value; }
	public String getUnit() { return unit; }
	public String getExplanation() { return explanation; }
	public String getOrigin() { return origin; }
	public String toString() { return name+" "+value+" "+unit+" "+explanation+" "+origin; }
	public Var(String name,double value,String unit,String explanation,String origin){
		setName(name);
		setValue(value);
		setUnit(unit);
		setExplanation(explanation);
		setOrigin(origin);
	}
}
