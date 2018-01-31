package org.processmining.AlphaLoop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.processmining.Gather.Trid;

public class LoopN extends ArrayList<Trid> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name ;
	private HashSet<String> preset;
    private HashSet<String> postset;
    public LoopN()
    {
    	name = "";
    	preset = new HashSet<String>();
    	postset = new HashSet<String>();
    }
	public LoopN(Set<String> pre , Set<String> post)
	{
		this();
		preset.addAll(pre);
		postset.addAll(post);
	}
	public LoopN(String n,Set<String> pre , Set<String> post)
	{
		this(pre,post);
		name = n;
	}
	public LoopN(String n)
	{
		this();
		name = n;
	}
	public void addPreSet(Set<String> s)
	{
		preset.addAll(s);
	}
	public void addPostSet(Set<String> s)
	{
		postset.addAll(s);
	}
	
	public void addPreSet(String s)
	{
		preset.add(s);
	}
	public void addPostSet(String s)
	{
		postset.add(s);
	}
	
	public Set<String> getPreSet()
	{
		return preset;
	}
	
	public Set<String> getPostSet()
	{
		return postset;
	}
	
	public String getName()
	{
		return name; 
	}
	public ArrayList<String> getLoopNToStringList()
	{
		ArrayList<String> strlist = new ArrayList<String>();
		for(int i = 0 ; i < size() ; i ++)
		{
			strlist.add(get(i).getName());
		}
		return strlist;
	}

}
