package org.processmining.AlphaLoop;

import java.util.HashSet;
import java.util.Set;

public class LoopC {
	private HashSet<String> perset;
	private HashSet<String> postset;
	public LoopC()
	{
		perset = new HashSet<String>();
		postset = new HashSet<String>();
	}
	public LoopC(Set<String> per ,Set<String> post)
	{
		this();
		perset.addAll(per);
		postset.addAll(post);
	}
	public HashSet<String> getPerSet()
	{
		return perset;
	}
	public HashSet<String> getPostSet()
	{
		return postset;
	}
	public void addPerSet(String s)
	{
		perset.add(s);
	}
	public void addPerSet(Set<String> s)
	{
		perset.addAll(s);
	}
	public void addPostSet(String s)
	{
		postset.add(s);
	}
	public void addPostSet(Set<String> s)
	{
	    postset.addAll(s);
	}
	public String toString()
	{
		return "perset:  "+perset.toString()+'\n'+"postset:  "+postset.toString();
	}

}
