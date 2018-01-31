package org.processmining.AlphaLoop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.processmining.Gather.Trid;

public class Loop2P {
	private HashSet<Set<String>> first;
	private HashSet<Set<String>> second;

	public Loop2P() {
		first = new HashSet<Set<String>>();
		second = new HashSet<Set<String>>();
	}

	public Loop2P(Set<String> f, Set<String> s) {
		this();
		first.add(f);
		second.add(s);
	}

	public void addFirst(Set<String> f) {
		first.add(f);
	}

	public void addSecond(Set<String> s) {
		second.add(s);
	}


	public Set<Set<String>> getFirst() {
		return first;
	}

	public Set<Set<String>> getSecond() {
		return second;
	}

	public Set<String> L2TSET(ArrayList<String> l2tlist, ArrayList<Trid> tridlist) {
		Set<String> l2t = new HashSet<String>();

			String tstr = l2tlist.remove(0);
			Set<String> f1perset = new HashSet<String>();
			Set<String> f1postset = new HashSet<String>();
			for (int i = 0; i < tridlist.size(); i++) {
				Trid trid1 = tridlist.get(i);
				if (trid1.getName() == tstr) {
					f1perset = trid1.getPreSet();
					f1postset = trid1.getPostSet();
				}
			}
			
			l2t.add(tstr);
			for (int i = 0; i < tridlist.size(); i++) {
				Trid trid2 = tridlist.get(i);
				Set<String> f2perset = new HashSet<String>();
				Set<String> f2postset = new HashSet<String>();
				f2perset = trid2.getPreSet();
				f2postset = trid2.getPostSet();
				if ((f1perset.containsAll(f2perset) && f2perset.containsAll(f1perset))
						|| (f1postset.containsAll(f2postset) && f2postset.containsAll(f1postset))) {
					l2t.add(trid2.getName());
				}
			}
		return l2t;
	}

	public String toString() {
		return "firstset:" + first.toString() + '\n' + "secondset:" + second.toString();
	}
}
