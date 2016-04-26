package com.plr.comparator.alphanum;

import java.util.Comparator;

abstract public class TokenComparable implements Comparable<TokenComparable> {


	final Comparator<String> comparator;

	TokenComparable( Comparator<String> comparator) {
		this.comparator = comparator;
	}

	@Override
	public int compareTo(TokenComparable o) {
		return comparator.compare(getStr(), o.getStr());
	}

	@Override
	public String toString() {
		return getStr().toString() + "(num? " + isNumber() + ")";
	}

	public abstract String getStr();
	
	abstract boolean isNumber();

	boolean isNegative() {
		return false;
	}
}
