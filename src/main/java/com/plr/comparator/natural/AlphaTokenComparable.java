package com.plr.comparator.natural;

import com.plr.comparator.whitespace.InsensitiveComparator;

public class AlphaTokenComparable extends TokenComparable {

	private final CharSequence str;;

	private final InsensitiveComparator REPETITION_INSENSITIVE = InsensitiveComparator.onRepetitionWhiteSpace(); 
	private final InsensitiveComparator SPACE_INSENSITIVE = InsensitiveComparator.onAllWhiteSpace(); 
	
	AlphaTokenComparable(CharSequence str, NaturalComparator naturalComparator) {
		super(naturalComparator);
		this.str = str;
	}

	@Override
	public boolean isNumber() {
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

	@Override
	public int compareTo(TokenComparable other) {
		CharSequence s1 = getStr();
		CharSequence s2 = other.getStr();

		if (naturalComparator.isSpaceInsensitve()) {
			return SPACE_INSENSITIVE.compare(s1, s2);
		} else if (naturalComparator.isSpaceCollapseInsensitve()) {
			return REPETITION_INSENSITIVE.compare(s1, s2);
		}

		return comparator.compare(s1, s2);
	}
	
	@Override
	public int size() {
		return str.length();
	}

}
