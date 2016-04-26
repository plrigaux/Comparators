package com.plr.comparator.alphanum;

import java.util.Comparator;

public class AlphaTokenComparable extends TokenComparable {

	AlphaTokenComparable(String s, Comparator<String> comparator) {
		super(s, comparator);
	}

	@Override
	boolean isNumber() {
		return false;
	}

}
