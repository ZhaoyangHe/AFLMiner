package org.processmining.FootMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.processmining.Data.MyEvent;
import org.processmining.Data.MyLog;
import org.processmining.Gather.Tuple;
import org.processmining.Relation.Relation;

public class FootMatrix {
	private MyLog mylog;
	private Relation[][] relation;
	private HashMap<String,Integer> StoImap ;
	private HashMap<Integer,String> ItoSmap;
	public FootMatrix(MyLog log)
	{
		mylog = log ;
		
		StoImap = new HashMap<String , Integer>();
		ItoSmap = new HashMap<Integer ,String>();
		initial();
		createFootMatrix();
	}
	public HashMap<String, Integer> getStoImap()
	{
		return StoImap;
	}
	public HashMap<Integer , String> getItoSmap()
	{
		return ItoSmap;
	}
	private void initial()
	{
		ArrayList<MyEvent> eventlist = new ArrayList<MyEvent>();
		eventlist.addAll(mylog.getMyEventSet());
		
		int len = eventlist.size();
		relation = new Relation[len][len];
		
		for(int x = 0 ; x < len ; x++)
		{
			for(int y = 0 ; y < len ; y++)
			{
				relation[x][y] = Relation.NoRel;
			}
		}
		
		for(int i = 0  ; i < eventlist.size() ; i ++)
		{
			MyEvent event = eventlist.get(i);
			String s = event.getName();
			StoImap.put(s, i);
			ItoSmap.put(i, s);
		}
	}
	private void createFootMatrix()
	{
		ArrayList<Tuple> tuplelist = new ArrayList<Tuple>();
		tuplelist.addAll(mylog.getAllTuple());
		ArrayList<Tuple> parallellist = new ArrayList<Tuple>();
		ArrayList<Tuple> casuallist = new ArrayList<Tuple>();
		while(tuplelist.size()>0)
		{
			Tuple tuple = tuplelist.get(0);
			Tuple retuple = new Tuple(tuple.getSecond(),tuple.getFirst());
			if(tuplelist.contains(retuple))
			{
				tuplelist.remove(retuple);
				tuplelist.remove(tuple);
				parallellist.add(tuple);
			}
			else
			{
				tuplelist.remove(tuple);
				casuallist.add(tuple);
			}
		}
		for(int i = 0 ; i < parallellist.size() ; i ++)
		{
			Tuple t = parallellist.get(i);
			String f = t.getFirst();
			String s = t.getSecond();
			int x = StoImap.get(f);
			int y = StoImap.get(s);
			relation[x][y] = Relation.Parallel;
			relation[y][x] = Relation.Parallel;
		}
		for(int i = 0 ; i < casuallist.size() ; i ++)
		{
			Tuple t = casuallist.get(i);
			String f = t.getFirst();
			String s = t.getSecond();
			int x = StoImap.get(f);
			int y = StoImap.get(s);
			relation[x][y] = Relation.DirectCasually;
			relation[y][x] = Relation.reDirectCasually;
		}
	}
	public void changeParalleltoCasual(String s1,String s2)
	{
		int x = StoImap.get(s1);
		int y = StoImap.get(s2);
		relation[x][y] = Relation.DirectCasually;
		relation[y][x] = Relation.DirectCasually;
	}
	public void loopntoCasual(String s1,String s2)
	{
		int x = StoImap.get(s1);
		int y = StoImap.get(s2);
		relation[x][y] = Relation.DirectCasually;
		relation[y][x] = Relation.DirectCasually;
	}
	public Set<Tuple> getCasualSet()
	{
		Set<Tuple> tupleset = new HashSet<Tuple>();
		for(int i = 0 ; i < relation.length ; i++)
		{
			for(int j = 0 ; j < relation.length ; j++)
			{
				if(relation[i][j].equals(Relation.DirectCasually))					
				{
					tupleset.add(new Tuple(ItoSmap.get(i), ItoSmap.get(j)));
				}
			}
		}
		return tupleset;
	}
	
	public Set<Tuple> getParallelSet()
	{
		Set<Tuple> tupleset = new HashSet<Tuple>();
		for(int i = 0 ; i < relation.length ; i++)
		{
			for(int j = 0 ; j < relation.length ; j++)
			{
				if(relation[i][j].equals(Relation.Parallel))					
				{
					tupleset.add(new Tuple(ItoSmap.get(i), ItoSmap.get(j)));
				}
			}
		}
		return tupleset;
	}
	
	public Relation getRelation(String s1, String s2)
	{
		return relation[StoImap.get(s1)][StoImap.get(s2)];
	}
	
	
    public void ShowMatrix()
    {
    	int length = relation.length;
    	String [] [] tostr = new String[length+1][length+1];
    	tostr[0][0] = " ";
    	for(int i = 1 ; i <= length ; i ++ )
    	{
    		tostr[0][i] = ItoSmap.get(i-1);
    		tostr[i][0] = ItoSmap.get(i-1);
    	}
    	for(int i = 0 ; i < length ; i++)
    	{
    		for(int j = 0 ; j <length ; j++)
    		{
    			tostr[i+1][j+1] = relation[i][j].toString();
    		}
    	}
    	for(int i = 0 ; i <= length ; i ++)
    	{
    		for(int  j = 0 ; j <= length ; j++)
    		{
    			System.out.printf("%4s",tostr[i][j]);
    		}
    		System.out.println();
    	}
    	
    }
	public Relation[][] getRelations()
	{
		return relation;
	}
	
	
	
	
	
}
