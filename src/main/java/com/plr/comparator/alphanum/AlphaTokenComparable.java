package com.plr.comparator.alphanum;

public class AlphaTokenComparable extends TokenComparable {

	AlphaTokenComparable(String s) {
		super(s);
	}

	@Override
	boolean isNumber() {
		return false;
	}

}
