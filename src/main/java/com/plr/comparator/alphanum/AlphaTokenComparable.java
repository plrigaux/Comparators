package com.plr.comparator.alphanum;

import java.util.Comparator;

public class AlphaTokenComparable extends TokenComparable {

	private final CharSequence str;

	AlphaTokenComparable(CharSequence str, Comparator<CharSequence> comparator) {
		super(comparator);
		this.str = str;
	}

	@Override
	boolean isNumber() {
		return false;
	}

	@Override
	public CharSequence getStr() {
		return str;
	}

	@Override
	public boolean isAllWhiteSpace() {
		return str.toString().trim().length() == 0;
	}
}
