package dk_util;

// Number - String Entry for List Key-Value Pair
public class Nustriel{
	String s;
	Integer n;

	public Nustriel() 	{ n=0; s=""; }
	public Nustriel(Integer n, String s){ this.n=n; this.s=s; }

	public void setKey(Integer n) 	{ this.n=n; }
	public void setValue(String s)	{ this.s=s; }

	public Integer getKey()	{ return n; }
	public String getValue(){ return s; }
	public String toString(){ return s; }
}
/* vim: set tabstop=2: */
