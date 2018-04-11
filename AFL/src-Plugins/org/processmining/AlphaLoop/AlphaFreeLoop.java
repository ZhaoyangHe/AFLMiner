package org.processmining.AlphaLoop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

public class AlphaFreeLoop {
	@Plugin(name = "AlphaFreeLoopMiner", parameterLabels = { "XLog" }, returnLabels = { "Petrinet" }, returnTypes = { Petrinet.class }, help = "The ¦Á-FL algorithm is proposed as mining free loop structures from incomplete event logs.")
	@UITopiaVariant(affiliation = "Shandong University of Science and Technology", author = "Zhaoyang He", email = "yz1022918@163.com")
	public Petrinet doMining(PluginContext context, XLog log) {
		String pnname = "AlphaFreeLoop";
		PetrinetImpl pn = new PetrinetImpl(pnname);
		MyLog mylog = new MyLog(log);
		FootMatrix footmatrix = mylog.createFootMatrix();
		System.out.println("log£º" + mylog);
		System.out.println("LOOPTRID:" + mylog.createLoopTrid());
		footmatrix.ShowMatrix();
		ArrayList<Trid> tridlist = new ArrayList<Trid>(mylog.createLoopTrid());

		Loop loop = new Loop();
		Set<Trid> otherloop = new HashSet<Trid>();
		for (int i = 0; i < tridlist.size(); i++) {
			Trid trid = tridlist.get(i);
			Set<String> perset = trid.getPreSet();
			Set<String> postset = trid.getPostSet();
			if (perset.containsAll(postset)) {
				loop.addFirstSet(trid.getName());
			} else if (postset.containsAll(perset)) {
				loop.addSecondSet(trid.getName());
			} else {
				otherloop.add(trid);
			}
		}
		//System.out.println("loop£º" + loop);
		//System.out.println("otherloop£º" + otherloop);

		ArrayList<String> firstlist = new ArrayList<String>(loop.getFirstSet());
		ArrayList<String> secondlist = new ArrayList<String>(loop.getSecondSet());
		Loop2P loop2p = new Loop2P();
		while (firstlist.size() > 0) {
			Set<String> first = new HashSet<String>();
			first = loop2p.L2TSET(firstlist, tridlist);
			loop2p.addFirst(first);
		}
		while (secondlist.size() > 0) {
			Set<String> second = new HashSet<String>();
			second = loop2p.L2TSET(secondlist, tridlist);
			loop2p.addSecond(second);
		}
		//System.out.println("loop2p£º" + loop2p);
//		for (int len = 0; len < mylog.size(); len++) {
//			MyTrace mytrace = mylog.get(len);
//			//System.out.println("trace(" + len + ")£º" + mytrace);
//		}
		ArrayList<Set<String>> loop2pfirstlist = new ArrayList<Set<String>>(loop2p.getFirst());
		ArrayList<Set<String>> loop2psecondlist = new ArrayList<Set<String>>(loop2p.getSecond());
		ArrayList<Loop2D> loop2list = new ArrayList<Loop2D>();

		for (int i = 0; i < loop2pfirstlist.size(); i++) {
			Loop2D loop2d = new Loop2D();
			Set<String> flp = new HashSet<String>();
			flp.addAll(loop2pfirstlist.get(i));
			//System.out.println("flp£º" + flp);
			Set<String> slp = new HashSet<String>();
			for (int j = 0; j < loop2psecondlist.size(); j++) {
				slp.addAll(loop2psecondlist.get(j));
				//System.out.println("slp£º" + slp);
				int lenflag = 0;
				for (int len = 0; len < mylog.size(); len++) {
					MyTrace mytrace = mylog.get(len);
					ArrayList<Integer> f = new ArrayList<Integer>();
					for (String t : flp) {
						if (mytrace.getposlist(t) != null)
							f.addAll(mytrace.getposlist(t));
					}
					Collections.sort(f);
					ArrayList<Integer> s = new ArrayList<Integer>();
					for (String t : slp) {
						if (mytrace.getposlist(t) != null)
							s.addAll(mytrace.getposlist(t));
					}
					Collections.sort(s);
					if ((f.size() == s.size())) {
						if (f.size() == 0) {
							lenflag++;
							continue;
						} else {
							int loop2flag = 0;
							for (int k = 0; k < f.size(); k++) {
								if (f.get(k) < s.get(k))
									loop2flag++;
							}
							if (loop2flag == f.size()) {
								lenflag++;
							} else
								break;
						}
					} else
						break;
				}
				if (lenflag == mylog.size()) {
					loop2d.addFirst(flp);
					loop2d.addSecond(slp);
					loop2list.add(loop2d);
					//System.out.println("flp->slp:" + flp + "->" + slp);
				} else
					//System.out.println(flp + "and" + slp + "are not in a freeloop");
				slp.clear();
			}
		}
		//System.out.println("loop2list:" + loop2list);

		ArrayList<Trid> otherlooplist = new ArrayList<Trid>(otherloop); 
		ArrayList<Trid> loop1plist = new ArrayList<Trid>();
		//System.out.println("otherlooplist£º" + otherlooplist);
		for (int i = 0; i < otherlooplist.size(); i++) {
			Trid trid0 = otherlooplist.get(i);
			int l1pflag = 0;
			for (int j = 0; j < otherlooplist.size(); j++) {
				Trid trid1 = otherlooplist.get(j);
				Set<String> prset = new HashSet<String>();
				Set<String> poset = new HashSet<String>();
				prset.addAll(trid0.getPreSet());
				poset.addAll(trid0.getPostSet());
				prset.retainAll(trid1.getPreSet());
				poset.retainAll(trid1.getPostSet());
				if (!poset.containsAll(prset) || !prset.containsAll(poset) || prset.isEmpty() || prset.isEmpty()) {
					l1pflag++;
				}
			}
			if (l1pflag == otherlooplist.size())
				loop1plist.add(trid0);
		}
		//System.out.println("loop1plist£º" + loop1plist);
		ArrayList<Trid> loop1list = new ArrayList<Trid>();
		for (int i = 0; i < loop1plist.size(); i++) {
			Trid l1 = loop1plist.get(i);
			String loop1a = l1.getName();
			ArrayList<String> apr = new ArrayList<String>();
			ArrayList<String> apo = new ArrayList<String>();
			apr.addAll(l1.getPreSet());
			apr.removeAll(l1.getPostSet());
			apo.addAll(l1.getPostSet());
			apo.removeAll(l1.getPreSet());
			//System.out.println("loop1 activity name:" + loop1a + "   loop1a apr:" + apr + "   loop1a apo:" + apo);
			for (int len = 0; len < mylog.size(); len++) {
				//System.out.println("len:" + len);
				MyTrace mytrace = mylog.get(len);
				ArrayList<Integer> l1apos = new ArrayList<Integer>();
				ArrayList<Integer> l1aprpos = new ArrayList<Integer>();
				ArrayList<Integer> l1apopos = new ArrayList<Integer>();
				if (mytrace.getposlist(loop1a) != null)
					l1apos.addAll(mytrace.getposlist(loop1a));
				//System.out.println("loop1 activity poslist:" + l1apos);
				for (String t : apr) {
					if (mytrace.getposlist(t) != null)
						l1aprpos.addAll(mytrace.getposlist(t));
				}
				Collections.sort(l1aprpos);
				for (String t : apo) {
					if (mytrace.getposlist(t) != null)
						l1apopos.addAll(mytrace.getposlist(t));
				}
				Collections.sort(l1apopos);
				//System.out.println("loop1 prea poslist:" + l1aprpos);
				//System.out.println("loop1 posta poslist:" + l1apopos);
				if (l1apos.size() > 1) {
					for (int j = 0; j < l1aprpos.size(); j++) {
						for (int k = 0; k < l1apos.size() - 1; k++) {
							if (l1aprpos.get(j) < l1apos.get(k) && l1apos.get(k + 1) < l1apopos.get(j)) {
								if (!loop1list.contains(l1))
									loop1list.add(l1);
								break;
							}
						}
						if (loop1list.contains(l1))
							break;
					}
				}
			}
		}
		//System.out.println("loop1list" + loop1list);
		ArrayList<LoopOrder> loop1order = new ArrayList<LoopOrder>();
		for (int i = 0; i < loop1list.size(); i++) {
			LoopOrder looporder = new LoopOrder();
			looporder.add(loop1list.get(i).getName());
			loop1order.add(looporder);
		}
		//System.out.println("loop1order" + loop1order);
		otherlooplist.removeAll(loop1list);
		ArrayList<LoopN> loopnlist = new ArrayList<LoopN>();
		while (otherlooplist.size() > 0) {
			Trid trid0 = otherlooplist.remove(0);
			LoopN loopn = new LoopN();
			loopn.add(trid0);
			for (int i = 0; i < otherlooplist.size(); i++) {
				Trid trid1 = otherlooplist.get(i);
				Set<String> prset = new HashSet<String>();
				Set<String> poset = new HashSet<String>();
				prset.addAll(trid1.getPreSet());
				poset.addAll(trid1.getPostSet());
				prset.retainAll(trid0.getPreSet());
				poset.retainAll(trid0.getPostSet());
				if (poset.containsAll(prset) && prset.containsAll(poset)&&poset.size()!=0 && prset.size()!=0) {
					loopn.add(trid1);
					otherlooplist.remove(i);
					i--;
				}
			}
			loopnlist.add(loopn);
		}
		//System.out.println("loopnlist" + loopnlist);
		ArrayList<LoopOrder> loopnorder = new ArrayList<LoopOrder>();
		for (int i = 0; i < loopnlist.size(); i++) {
			LoopN loopn = loopnlist.get(i);

			LoopOrder looporder = new LoopOrder();
			ArrayList<String> strlist1 = loopn.getLoopNToStringList();
			//System.out.println("strlist1" + strlist1);
			for (int l = 0; l < mylog.size(); l++) {
				MyTrace mytrace = mylog.get(l);
				ArrayList<String> strlist2 = mytrace.getTraceToStringList();
				//System.out.println("strlist2" + strlist2);
				if (strlist2.containsAll(strlist1)) {
					for (int k = 0; k < strlist2.size(); k++) {
						if (strlist1.contains(strlist2.get(k))) {
							looporder.add(strlist2.get(k));
						}
					}
					break;
				}
			}
			loopnorder.add(looporder);
		}
		//System.out.println("loopnorder" + loopnorder);
		for (int i = 0; i < loopnorder.size(); i++) {
			ArrayList<String> ordern = loopnorder.get(i);
			
			if (ordern.size() == 2) {
				footmatrix.loopntoCasual(ordern.get(0), ordern.get(1));
				footmatrix.loopntoCasual(ordern.get(1), ordern.get(0));
			}
		}
		ArrayList<String> transtrlist = new ArrayList<String>(footmatrix.getItoSmap().values());
		Map<String, Transition> StoTmap = new HashMap<String, Transition>();
		for (int i = 0; i < transtrlist.size(); i++) {
			String str = transtrlist.get(i);
			Transition transition = pn.addTransition(str);
			StoTmap.put(str, transition);
		}

		for (int i = 0; i < loop2list.size(); i++) {
			Loop2D loop2d = loop2list.get(i);
			for (String l2df : loop2d.getFirst()) {
				for (String l2ds : loop2d.getSecond())
					footmatrix.changeParalleltoCasual(l2df, l2ds);

			}
		}

		ArrayList<Tuple> tuplelist = new ArrayList<Tuple>(footmatrix.getCasualSet());
		//System.out.println("casual_relation   " + tuplelist);
		ArrayList<MyPlace> myplacelist = new ArrayList<MyPlace>();
		for (int i = 0; i < tuplelist.size(); i++) {
			myplacelist.add(new MyPlace(tuplelist.get(i)));
		}
		for (int i = 0; i < myplacelist.size(); i++) {
			MyPlace myplace1 = myplacelist.get(i);
			ArrayList<String> perstr1 = new ArrayList<String>(myplace1.getPerSet());
			ArrayList<String> poststr1 = new ArrayList<String>(myplace1.getPostSet());
			for (int j = 0; j < myplacelist.size(); j++) {
				if (i != j) {
					MyPlace myplace2 = myplacelist.get(j);
					ArrayList<String> perstr2 = new ArrayList<String>(myplace2.getPerSet());
					ArrayList<String> poststr2 = new ArrayList<String>(myplace2.getPostSet());
					if (perstr1.containsAll(perstr2)) {
						if (norel(footmatrix, poststr1, poststr2) && casual(footmatrix, perstr1, poststr2)
								&& casual(footmatrix, perstr2, poststr1)) {
							MyPlace newplace = new MyPlace();
							newplace.combine(myplace1);
							newplace.combine(myplace2);
							if (!myplacelist.contains(newplace)) {
								myplacelist.add(newplace);
							}
						}
					} else if (poststr1.containsAll(poststr2) && casual(footmatrix, perstr1, poststr2)
							&& casual(footmatrix, perstr2, poststr1)) {
						if (norel(footmatrix, perstr1, perstr2)) {
							MyPlace newplace = new MyPlace();
							newplace.combine(myplace1);
							newplace.combine(myplace2);
							if (!myplacelist.contains(newplace)) {
								myplacelist.add(newplace);
							}
						}
					} else if (perstr2.containsAll(perstr1) && casual(footmatrix, perstr1, poststr2)
							&& casual(footmatrix, perstr2, poststr1)) {
						if (norel(footmatrix, poststr1, poststr2)) {
							MyPlace newplace = new MyPlace();
							newplace.combine(myplace1);
							newplace.combine(myplace2);
							if (!myplacelist.contains(newplace)) {
								myplacelist.add(newplace);
							}
						}
					} else if (poststr2.containsAll(poststr1) && casual(footmatrix, perstr1, poststr2)
							&& casual(footmatrix, perstr2, poststr1)) {
						if (norel(footmatrix, perstr1, perstr2)) {
							MyPlace newplace = new MyPlace();
							newplace.combine(myplace1);
							newplace.combine(myplace2);
							if (!myplacelist.contains(newplace)) {
								myplacelist.add(newplace);
							}
						}
					}
				}
			}
		}
		//System.out.println("combine place1: " + myplacelist);
		ArrayList<MyPlace> myplacelist2 = new ArrayList<MyPlace>();
		for (int i = 0; i < myplacelist.size(); i++) {
			MyPlace myplace1 = myplacelist.get(i);
			boolean contains = false;
			ArrayList<String> perstr1 = new ArrayList<String>(myplace1.getPerSet());
			ArrayList<String> poststr1 = new ArrayList<String>(myplace1.getPostSet());
			for (int j = 0; j < myplacelist.size(); j++) {
				if (i != j) {
					MyPlace myplace2 = myplacelist.get(j);
					ArrayList<String> perstr2 = new ArrayList<String>(myplace2.getPerSet());
					ArrayList<String> poststr2 = new ArrayList<String>(myplace2.getPostSet());
					if (perstr2.containsAll(perstr1) && poststr2.containsAll(poststr1)) {
						contains = true;
					}
				}
			}
			if (!contains) {
				myplacelist2.add(myplace1);
			}
		}
		myplacelist.clear();
		myplacelist.addAll(myplacelist2);
		//System.out.println("combine place2: " + myplacelist);
		ArrayList<MyPlace> loop1place = new ArrayList<MyPlace>();
		for (int i = 0; i < loop1order.size(); i++) {
			LoopOrder looporder = loop1order.get(i);
			for (int j = 0; j < myplacelist.size(); j++) {
				MyPlace myplace = myplacelist.get(j);
				if (myplace.getPerSet().containsAll(looporder) || myplace.getPostSet().containsAll(looporder)) {
					loop1place.add(myplace);
					myplacelist.remove(myplace);
					j--;
				}
			}
			//System.out.println("loop1 place: " + loop1place);
			if (loop1place.size() > 0) {
				MyPlace pp = loop1place.get(0);
				for (int j = 1; j < loop1place.size(); j++) {
					pp.combine(loop1place.get(j));
				}
				myplacelist.add(pp);
			}
			loop1place.clear();
		}
		//System.out.println("add loop1 place: " + myplacelist);
		for (int i = 0; i < myplacelist.size(); i++) {
			MyPlace myplace1 = myplacelist.get(i);
			for (int j = 0; j < myplacelist.size(); j++) {
				if (i != j) {
					MyPlace myplace2 = myplacelist.get(j);
					ArrayList<String> perstr1 = new ArrayList<String>(myplace1.getPerSet());
					ArrayList<String> poststr1 = new ArrayList<String>(myplace1.getPostSet());
					ArrayList<String> perstr2 = new ArrayList<String>(myplace2.getPerSet());
					ArrayList<String> poststr2 = new ArrayList<String>(myplace2.getPostSet());

					if (perstr1.containsAll(perstr2) && (poststr1.containsAll(poststr2))) {
						myplace1.combine(myplace2);
						myplacelist.remove(myplace2);
						j--;
					} else if (perstr2.containsAll(perstr1) && poststr2.containsAll(poststr1)) {
						myplace2.combine(myplace1);
						myplacelist.remove(myplace1);
						i--;
					}
				}
			}
		}
		//System.out.println("combine place3: " + myplacelist);

		ArrayList<Place> placelist = new ArrayList<Place>();
		int number = 0;
		for (int i = 0; i < myplacelist.size(); i++) {
			String label = "p" + number++;
			placelist.add(pn.addPlace(label));
		}

		for (int i = 0; i < myplacelist.size(); i++) {
			MyPlace myplace = myplacelist.get(i);
			ArrayList<String> myperlist = new ArrayList<String>(myplace.getPerSet());
			ArrayList<String> mypostlist = new ArrayList<String>(myplace.getPostSet());

			for (int j = 0; j < myperlist.size(); j++) {
				Place p = placelist.get(i);
				Transition t = StoTmap.get(myperlist.get(j));
				pn.addArc(t, p);
			}
			for (int j = 0; j < mypostlist.size(); j++) {
				Place p = placelist.get(i);
				Transition t = StoTmap.get(mypostlist.get(j));
				pn.addArc(p, t);
			}
		}

		ArrayList<String> startt = new ArrayList<String>();
		ArrayList<String> endt = new ArrayList<String>();
		for (int i = 0; i < mylog.size(); i++) {
			String start = mylog.getTrace(i).getFirstEvent().getName();
			String end = mylog.getTrace(i).getLastEvent().getName();
			if (!startt.contains(start))
				startt.add(start);
			if (!endt.contains(end))
				endt.add(end);
		}
		
		//System.out.println("start t: " + startt + "   end t: " + endt);

		Place stareplace = pn.addPlace("Start");
		Place endplace = pn.addPlace("end");
		for (String s : startt) {
			Transition starttra = StoTmap.get(s);
			pn.addArc(stareplace, starttra);
		}
		for (String e : endt) {
			Transition endtra = StoTmap.get(e);
			pn.addArc(endtra, endplace);
		}

		return pn;

	}

	public static boolean norel(FootMatrix footmatrix, ArrayList<String> per, ArrayList<String> post) {
		boolean bool = true;

		for (int i = 0; i < per.size(); i++) {
			for (int j = 0; j < post.size(); j++) {
				bool = bool && (footmatrix.getRelation(per.get(i), post.get(j)).equals(Relation.NoRel));
			}
		}
		return bool;
	}

	public static boolean casual(FootMatrix footmatrix, ArrayList<String> per, ArrayList<String> post) {
		boolean bool = true;

		for (int i = 0; i < per.size(); i++) {
			for (int j = 0; j < post.size(); j++) {
				bool = bool && (footmatrix.getRelation(per.get(i), post.get(j)).equals(Relation.DirectCasually));
			}
		}
		return bool;
	}
}
