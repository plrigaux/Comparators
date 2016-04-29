package com.plr.comparator.alphanum;

import java.util.Comparator;

public class AlphaTokenComparable extends TokenComparable {

	private final String str;

	AlphaTokenComparable(String str, Comparator<String> comparator) {
		super(comparator);
		this.str = str;
	}

	@Override
	boolean isNumber() {
		return false;
	}

	@Override
	public String getStr() {
		return str;
	}

	@Override
	public boolean isAllWhiteSpace() {
		return str.trim().length() == 0;
	}
}
