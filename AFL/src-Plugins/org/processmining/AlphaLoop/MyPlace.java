package org.processmining.AlphaLoop;

import java.util.HashSet;

import org.processmining.Gather.Tuple;

public class MyPlace {
	private HashSet<String> perset;
	private HashSet<String> postset;
	public MyPlace()
	{
		perset = new HashSet<String>();
		postset = new HashSet<String>();
	}
	public MyPlace(String s1,String s2)
	{
		this();
		this.addPerSet(s1);
		this.addPostSet(s2);
	}
	public MyPlace(Tuple tuple)
	{
		this(tuple.getFirst(),tuple.getSecond());
	}
	public MyPlace(HashSet<String> list1 , HashSet<String> list2 )
	{
		this();
		this.addPerSet(list1);
		this.addPostSet(list2);
	}
	public  HashSet<String>  getPerSet()
	{
		return perset;
	}
	public  HashSet<String>  getPostSet()
	{
		return postset;
	}
	public void addPerSet(String s)
	{
		perset.add(s);
	}
	public void addPostSet(String s)
	{
		postset.add(s);
	}
	public void addPerSet( HashSet<String> s)
	{
		perset.addAll(s);
	}
	public void addPostSet( HashSet<String> s)
	{
		postset.addAll(s);
	}
    public String toString()
    {
    	return perset.toString()+ "->" +postset.toString();
    }
    public MyPlace combine(MyPlace p)
    {
    	this.addPerSet(p.getPerSet());
    	this.addPostSet(p.getPostSet());
    	return this;
    }

    public boolean equals(Object obj)
	{
		boolean bool = false;
	   if (obj instanceof MyPlace)
	   {
		   MyPlace place = (MyPlace) obj;
		   if(place.getPerSet().containsAll(perset)&&perset.containsAll(place.getPerSet())&&place.getPostSet().containsAll(postset)&&postset.containsAll(place.getPostSet()))
		   {
			   bool =true;
		   }
		}
		return bool;
	}
    
    
    
}
