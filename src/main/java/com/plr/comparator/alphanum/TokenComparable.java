package com.plr.comparator.alphanum;

abstract public class TokenComparable implements Comparable<TokenComparable> {

	String s;

	TokenComparable(String s) {
		this.s = s;
	}

	@Override
	public int compareTo(TokenComparable o) {
		return s.compareTo(o.s);
	}

	@Override
	public String toString() {
		return s.toString() + "(num? " + isNumber() + ")";
	}

	abstract boolean isNumber();

	boolean isNegative() {
		return false;
	}
}
