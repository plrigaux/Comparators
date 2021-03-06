package com.plr.comparator.natural;

public class AlphaTokenComparable extends TokenComparable {

	private final CharSequence str;

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

		return comparator.compare(s1, s2);
	}
	
	@Override
	public int size() {
		return str.length();
	}

}
