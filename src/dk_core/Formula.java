/* vim: set tabstop=2: */
package dk_core;

public class Formula{
	String formText;
	String formName;
	public Formula(String formName,String formText){
		this.formName=formName.trim();
		this.formText=formText.trim();
	}	
	public String getName(){ return formName; }
	public String getText(){ return formText; }
	public void setName(String newName) { this.formName=newName; }
	public void setText(String newText) { this.formText=newText; }
	public String toString() { return formName+" = "+formText; }
}
