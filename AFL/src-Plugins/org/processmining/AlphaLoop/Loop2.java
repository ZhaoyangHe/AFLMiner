package org.processmining.AlphaLoop;

public class Loop2 {
	private String first;
	private String second;
	public Loop2()
	{
		first = new String();
		second = new String();
	}
	public Loop2(String f,String s)
	{
		this();
		first = f;
		second = s;
	}
	public void setFirst(String s)
	{
		first = s;
	}
	public void setSecond(String s)
	{
		second = s;
	}
	public String getFirst()
	{
		return first;
	}
	public String getSecond()
	{
		return second;
	}
	public String toString()
	{
		return first+"->"+second;
	}

}
