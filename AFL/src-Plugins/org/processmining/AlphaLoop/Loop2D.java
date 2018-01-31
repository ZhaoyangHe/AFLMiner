package org.processmining.AlphaLoop;

import java.util.HashSet;
import java.util.Set;

public class Loop2D {
	private HashSet<String> first;
	private HashSet<String> second;

	public Loop2D() {
		first = new HashSet<String>();
		second = new HashSet<String>();
	}

	public Loop2D(Set<String> f, Set<String> s) {
		this();
		first.addAll(f);
		second.addAll(s);
	}

	public void addFirst(Set<String> f) {
		first.addAll(f);
	}

	public void addSecond(Set<String> s) {
		second.addAll(s);
	}


	public Set<String> getFirst() {
		return first;
	}

	public Set<String> getSecond() {
		return second;
	}

	

	public String toString() {
		return "firstset:" + first.toString() + '\n' + "secondset" + second.toString();
	}
}
