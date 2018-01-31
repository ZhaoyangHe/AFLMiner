package org.processmining.AlphaLoop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.map.HashedMap;
import org.deckfour.xes.model.XLog;
import org.processmining.Data.MyLog;
import org.processmining.Data.MyTrace;
import org.processmining.FootMatrix.FootMatrix;
import org.processmining.Gather.Trid;
import org.processmining.Gather.Tuple;
import org.processmining.Relation.Relation;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetImpl;

public class AlphaLoop {
	@Plugin(
			name = "AlphaFreeSingleLoopMiner",
			parameterLabels = { "XLog"},
			returnLabels = { "Petrinet" },
			returnTypes = { Petrinet.class }
	)
	@UITopiaVariant(
	        affiliation = "SDUST", 
	        author = "Zhaoyang He", 
	        email = "yz1022918@163.com"
	)
	public Petrinet doMining(PluginContext context,XLog log)
	{
		String pnname = "AlphaLoop";
		PetrinetImpl pn = new PetrinetImpl(pnname);
		
		MyLog mylog = new MyLog(log);
		FootMatrix footmatrix = mylog.createFootMatrix();
		//System.out.println(mylog.createLoopTrid());
		footmatrix.ShowMatrix();
		ArrayList<Trid> tridlist = new ArrayList<Trid>(mylog.createLoopTrid());
		LoopC loop = new LoopC();
		Set<Trid> otherloop = new HashSet<Trid>();
		for(int i = 0 ; i < tridlist.size() ; i ++)
		{
			Trid trid = tridlist.get(i);
			Set<String> perset = trid.getPreSet();
			Set<String> postset = trid.getPostSet();
			if(perset.containsAll(postset))
			{
				loop.addPerSet(trid.getName());
			}
			else if(postset.containsAll(perset))
			{
				loop.addPostSet(trid.getName());
			}	
			else
			{
				otherloop.add(trid);
			}
		}
		
		//System.out.println("loop"+loop);
		//System.out.println("otherloop"+otherloop);
		ArrayList<String> perlist = new ArrayList<String>(loop.getPerSet());
		ArrayList<String> postlist = new ArrayList<String>(loop.getPostSet());
		ArrayList<Loop2> loop2list = new ArrayList<Loop2>();
		while(perlist.size() > 0 )
		{
			Loop2 loop2 = new Loop2();
			String firststr = perlist.remove(0);
			loop2.setFirst(firststr);
			for(int i = 0 ; i < postlist.size() ; i ++)
			{
				String secondstr = postlist.get(i);
				boolean flage = true;
				for(int len = 0 ; len < mylog.size() ; len ++)
			    {
					MyTrace mytrace = mylog.get(len);
					int firstnumber = mytrace.getEventnumber(firststr);
					int secondnumber = mytrace.getEventnumber(secondstr);
					flage = flage&(firstnumber == secondnumber);
					if(!flage)
						break;
			    }
				if(flage)
				{
					loop2.setSecond(secondstr);
					postlist.remove(i);
					break;
				}
			}	
			loop2list.add(loop2);
		}
		//System.out.println(loop2list);
		
		ArrayList<Trid> otherlooplist = new ArrayList<Trid>(otherloop); 
		ArrayList<LoopN> loopnlist = new ArrayList<LoopN>();
		while(otherlooplist.size() > 0 )
		{
			Trid trid0 = otherlooplist.remove(0);
			LoopN loopn = new LoopN();
			loopn.add(trid0);
			for(int i = 0 ; i < otherlooplist.size() ; i ++)
			{
				Trid trid1 = otherlooplist.get(i);
				Set<String> prset = new HashSet<String>();
				Set<String> poset = new HashSet<String>();
				prset.addAll(trid1.getPreSet());
				poset.addAll(trid1.getPostSet());
				prset.retainAll(trid0.getPreSet());
				poset.retainAll(trid0.getPostSet());
				if(poset.containsAll(prset)&&prset.containsAll(poset))
				{
					loopn.add(trid1);
					otherlooplist.remove(i);
					i--;
				}
			}
            loopnlist.add(loopn);
		}
		//System.out.println(loopnlist);
		ArrayList<LoopOrder> loop1order = new ArrayList<LoopOrder>();
		ArrayList<LoopOrder> loopnorder = new ArrayList<LoopOrder>();
		for(int i = 0 ; i < loopnlist.size() ; i ++)
		{
			LoopN loopn = loopnlist.get(i);
			if(loopn.size()<3)
			{
				LoopOrder looporder = new LoopOrder();
				looporder.add(loopn.getLoopNToStringList().get(0));
				loop1order.add(looporder);
			}
			else
			{
				LoopOrder looporder = new LoopOrder();
				ArrayList<String> strlist1 = loopn.getLoopNToStringList();
				
				for(int l = 0 ; l < mylog.size() ; l ++)
				{
					MyTrace mytrace = mylog.get(l);
					ArrayList<String> strlist2 = mytrace.getTraceToStringList();
					if(strlist2.containsAll(strlist1))
					{
						for(int k = 0 ; k < strlist2.size() ; k++)
						{
							if(strlist1.contains(strlist2.get(k)))
							{
								looporder.add(strlist2.get(k));
							}
						}
						
						break;
					}
				}
				loopnorder.add(looporder);
			}
		}
		
		//System.out.println(loop1order);
		
		//System.out.println(loopnorder);
		
		ArrayList<String> transtrlist = new ArrayList<String>(footmatrix.getItoSmap().values());
		Map<String , Transition> StoTmap = new HashedMap<String , Transition>();
		for(int i = 0 ; i < transtrlist.size() ; i++)
		{
			String str = transtrlist.get(i);
			Transition transition = pn.addTransition(str);
			StoTmap.put(str, transition);
		}
		
		for(int i = 0 ; i < loop2list.size() ; i ++)
		{
			Loop2 loop2 = loop2list.get(i);
			footmatrix.changeParalleltoCasual(loop2.getFirst(), loop2.getSecond());
		}
		
		ArrayList<Tuple> tuplelist = new ArrayList<Tuple>(footmatrix.getCasualSet());
		//System.out.println("casual_relation   "+tuplelist);
		
		ArrayList<MyPlace> myplacelist = new ArrayList<MyPlace>();
		for(int i = 0 ; i < tuplelist.size() ; i ++)
		{
			myplacelist.add(new MyPlace(tuplelist.get(i)));
		}
		
		for(int i = 0 ; i < myplacelist.size() ; i++)
		{
			MyPlace myplace1 = myplacelist.get(i);
			for(int j = 0 ; j < myplacelist.size() ; j++)
			{
				if(i!=j)
				{
					MyPlace myplace2 = myplacelist.get(j);
					ArrayList<String> perstr1 = new ArrayList<String>(myplace1.getPerSet());
					ArrayList<String> poststr1 = new ArrayList<String>(myplace1.getPostSet());
					ArrayList<String> perstr2 = new ArrayList<String>(myplace2.getPerSet());
					ArrayList<String> poststr2 = new ArrayList<String>(myplace2.getPostSet());
					
					if(perstr1.containsAll(perstr2))
					{
						if(norel(footmatrix, poststr1, poststr2))
						{
							myplace1.combine(myplace2);
							myplacelist.remove(myplace2);
							j--;
						}
					}
					else if(poststr1.containsAll(poststr2))
					{
						if(norel(footmatrix, perstr1, perstr2))
						{
							myplace1.combine(myplace2);
							myplacelist.remove(myplace2);
							j--;
						}
					}
					else if(perstr2.containsAll(perstr1))
					{
						if(norel(footmatrix, poststr1, poststr2))
						{
							myplace2.combine(myplace1);
							myplacelist.remove(myplace1);
							i--;
						}
					}
					else if(poststr2.containsAll(poststr1))
					{
						if(norel(footmatrix, perstr1, perstr2))
						{
							myplace2.combine(myplace1);
							myplacelist.remove(myplace1);
							i--;
						}
					}
				}
			}
		}
		//System.out.println(myplacelist);
		ArrayList<MyPlace> loop1place = new ArrayList<MyPlace>();
		for(int i = 0 ; i < loop1order.size() ; i ++)
		{
			LoopOrder looporder = loop1order.get(i);
			for(int j = 0 ; j < myplacelist.size() ; j++)
			{
				MyPlace myplace = myplacelist.get(j);
				if(myplace.getPerSet().containsAll(looporder)||myplace.getPostSet().containsAll(looporder))
				{
					loop1place.add(myplace);
					myplacelist.remove(myplace);
					j--;
				}
			}
		}
		if(loop1place.size()>0)
		{
			MyPlace pp = loop1place.get(0);
		for(int i = 1 ; i < loop1place.size() ; i ++)
		{
			pp.combine(loop1place.get(i));
		}
		myplacelist.add(pp);
		} 
		//System.out.println(myplacelist);
		for(int i = 0 ; i < myplacelist.size() ; i++)
		{
			MyPlace myplace1 = myplacelist.get(i);
			for(int j = 0 ; j < myplacelist.size() ; j++)
			{
				if(i!=j)
				{
					MyPlace myplace2 = myplacelist.get(j);
					ArrayList<String> perstr1 = new ArrayList<String>(myplace1.getPerSet());
					ArrayList<String> poststr1 = new ArrayList<String>(myplace1.getPostSet());
					ArrayList<String> perstr2 = new ArrayList<String>(myplace2.getPerSet());
					ArrayList<String> poststr2 = new ArrayList<String>(myplace2.getPostSet());
					
					if(perstr1.containsAll(perstr2)&&(poststr1.containsAll(poststr2)))
					{
						myplace1.combine(myplace2);
						myplacelist.remove(myplace2);
						j--;
					}
					else if(perstr2.containsAll(perstr1)&&poststr2.containsAll(poststr1))
					{
						myplace2.combine(myplace1);
						myplacelist.remove(myplace1);
						i--;
					}
				}
			}
		}
		//System.out.println(myplacelist);
		
		ArrayList<Place> placelist = new ArrayList<Place>();
		int number = 0 ;
		for(int i = 0 ; i < myplacelist.size() ; i ++)
		{
			String label = "p" + number++;
			placelist.add(pn.addPlace(label));
		}
		
		for(int i = 0 ; i < myplacelist.size() ; i++)
		{
			MyPlace myplace = myplacelist.get(i);
			ArrayList<String> myperlist = new ArrayList<String>(myplace.getPerSet());
			ArrayList<String> mypostlist = new ArrayList<String>(myplace.getPostSet());
			
			for(int j = 0 ; j <myperlist.size() ; j ++)
			{
				Place p = placelist.get(i);
				Transition t = StoTmap.get(myperlist.get(j));
				pn.addArc(t, p);
			}
			for(int j = 0 ; j <mypostlist.size() ; j ++)
			{
				Place p = placelist.get(i);
				Transition t = StoTmap.get(mypostlist.get(j));
				pn.addArc(p, t);
			}
 		}
		
		String start = mylog.getFirstTrace().getFirstEvent().getName();
		String end =  mylog.getFirstTrace().getLastEvent().getName();
		
		Transition starttra = StoTmap.get(start);
		Transition endtra = StoTmap.get(end);
		
		Place stareplace = pn.addPlace("Start");
		Place endplace = pn.addPlace("end");
		
		pn.addArc(stareplace, starttra);
		pn.addArc(endtra, endplace);
		
		
		
		
		
		return pn;
		
	}
	public static boolean norel(FootMatrix footmatrix,ArrayList<String> per,ArrayList<String> post)
	{
		boolean bool = true;
		
		for(int i = 0 ; i < per.size() ; i ++)
		{
			for(int j = 0 ; j < post.size() ; j ++)
			{
				bool = bool&(footmatrix.getRelation(per.get(i), post.get(j)).equals(Relation.NoRel));
			}	
		}	
		return bool;
	}

}
