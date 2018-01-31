package org.processmining.AlphaLoop;

import java.util.HashSet;
import java.util.Set;

public class Loop {
	private HashSet<String> firstset;
	private HashSet<String> secondset;
	public Loop()
	{
		firstset = new HashSet<String>();
		secondset = new HashSet<String>();
	}
	public Loop(Set<String> f ,Set<String> s)
	{
		this();
		firstset.addAll(f);
		secondset.addAll(s);
	}
	public HashSet<String> getFirstSet()
	{
		return firstset;
	}
	public HashSet<String> getSecondSet()
	{
		return secondset;
	}
	public void addFirstSet(String s)
	{
		firstset.add(s);
	}
	public void addFirstSet(Set<String> s)
	{
		firstset.addAll(s);
	}
	public void addSecondSet(String s)
	{
		secondset.add(s);
	}
	public void addSecondSet(Set<String> s)
	{
		secondset.addAll(s);
	}
	public String toString()
	{
		return "firstset:  "+firstset.toString()+'\n'+"secondset:  "+secondset.toString();
	}

}
