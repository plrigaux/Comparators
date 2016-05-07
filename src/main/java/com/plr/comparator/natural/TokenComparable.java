package com.plr.comparator.natural;

import java.util.Comparator;

abstract public class TokenComparable implements Comparable<TokenComparable> {

	final Comparator<CharSequence> comparator;
	protected final NaturalComparator naturalComparator;

	TokenComparable(NaturalComparator naturalComparator) {
		this.comparator = naturalComparator.getAlphaComparator();
		this.naturalComparator = naturalComparator;
	}

//	@Override
//	public abstract int compareTo(TokenComparable other);

	
	public int compareTo(TokenComparable other) {
		CharSequence s1 = getStr();
		CharSequence s2 = other.getStr();
		return comparator.compare(s1, s2);
	}
	
	@Override
	public String toString() {
		return (isNumber() ? "N>" : "A>") + getStr().toString();
	}

	public abstract CharSequence getStr();
	
	abstract public boolean isNumber();
	
	abstract public int size();

	boolean isNegative() {
		return false;
	}

	public int compareLeadingZerosTo(TokenComparable other) {
		// if this method is called, it means that is was already equals 
		return 0;
	}

	public boolean isAllWhiteSpace() {
		return false;
	}
}
