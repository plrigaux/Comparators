package com.plr.comparator.alphanum;

import java.util.Comparator;

abstract public class TokenComparable implements Comparable<TokenComparable> {

	final Comparator<String> comparator;

	TokenComparable( Comparator<String> comparator) {
		this.comparator = comparator;
	}

	@Override
	public int compareTo(TokenComparable other) {
	
		if (isAllWhiteSpace() && other.isAllWhiteSpace()) {
			return 0;
		}
		
		return comparator.compare(getStr(), other.getStr());
	}

	@Override
	public String toString() {
		return (isNumber() ? "N>" : "A>") +getStr().toString();
	}

	public abstract String getStr();
	
	abstract boolean isNumber();

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
