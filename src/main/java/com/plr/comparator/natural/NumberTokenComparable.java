package com.plr.comparator.natural;

public class NumberTokenComparable extends TokenComparable {

	private final boolean isNegative;
	private final CharSequence units;
	private final CharSequence decimal;
	private final CharSequence str;

	NumberTokenComparable(String str, NaturalComparator naturalComparator) {
		super(naturalComparator);
		this.str = str;

		int len = str.length();
		int i = 0;
		char c = 0;

		for (; i < len; i++) {
			c = str.charAt(i);

			if (!Character.isWhitespace(c)) {
				break;
			}
		}

		boolean isNegative = false;

		if (c == '-') {
			isNegative = true;
			c = str.charAt(++i);
		}

		this.isNegative = isNegative;

		int start = i;
		do {
			if (!Character.isDigit(c)) {
				c = str.charAt(i++);
				break;
			}

			c = str.charAt(i++);
		} while (i < len);

		this.units = str.substring(start, i);

		String decimal = null;
		start = i;

		while (Character.isDigit(c) && i < len) {
			c = str.charAt(i++);
		}

		if (start != i) {
			decimal = str.substring(start, i);
		}

		this.decimal = decimal;
	}

	NumberTokenComparable(boolean isNegative, CharSequence units, CharSequence decimal, CharSequence str,
			 NaturalComparator naturalComparator) {
		super(naturalComparator);
		this.isNegative = isNegative;
		this.decimal = decimal;
		this.units = units;
		this.str = str;
	}

	@Override
	public boolean isNumber() {
		return true;
	}

	@Override
	public int compareTo(TokenComparable other) {
		if (!other.isNumber()) {
			return -other.compareTo(this);
		}

		NumberTokenComparable otherNTC = (NumberTokenComparable) other;

		if (isNegative()) {
			if (!other.isNegative()) {
				return -1;
			}

			return -comapreNum(units, otherNTC.units, otherNTC);
		} else {
			if (other.isNegative()) {
				return 1;
			}

			return comapreNum(units, otherNTC.units, otherNTC);
		}

	}

	private int comapreNum(CharSequence s1, CharSequence s2, NumberTokenComparable other) {

		int beg1 = 0;
		int beg2 = 0;

		int len1 = s1.length();
		int len2 = s2.length();

		int result = len1 - len2;

		// If equal size, the first different number counts
		if (result == 0) {
			for (int i = beg1, j = beg2; i < len1; i++, j++) {
				result = s1.charAt(i) - s2.charAt(j);
				if (result != 0) {
					return result;
				}
			}

			result = comapreDecimals(other);

			if (result != 0) {
				return result;
			}
		}

		return result;
	}

	// The one that have less leading zeros or space is the smallest
	@Override
	public int compareLeadingZerosTo(TokenComparable other) {
		if (!other.isNumber()) {
			return -other.compareTo(this);
		}

		CharSequence thisStr = this.getStr();
		CharSequence otherStr = other.getStr();

		int len1 = thisStr.length();
		int len2 = otherStr.length();

		int result = len1 - len2;

		return result;
	}

	private int comapreDecimals(NumberTokenComparable o) {

		CharSequence s1 = this.decimal;
		CharSequence s2 = o.decimal;

		if (s1 == null) {
			if (s2 != null) {
				return -1;
			} else {
				return 0;
			}
		} else {
			if (s2 == null) {
				return 1;
			}
		}

		int len1 = s1.length();
		int len2 = s2.length();

		// clean tailling zeros
		for (; len1 >= 0; len1--) {
			if (s1.charAt(len1 - 1) != '0') {
				break;
			}
		}

		for (; len2 >= 0; len2--) {
			if (s2.charAt(len2 - 1) != '0') {
				break;
			}
		}

		int lim = Math.min(len1, len2);

		for (int k = 0; k < lim; k++) {
			char c1 = s1.charAt(k);
			char c2 = s2.charAt(k);

			int result = c1 - c2;

			if (result != 0) {
				return result;
			}

		}

		return len1 - len2;

	}

	boolean isNegative() {
		return isNegative;
	}

	@Override
	public CharSequence getStr() {
		return str;
	}

	public CharSequence getDecimal() {
		return decimal;
	}

	public CharSequence getUnit() {
		return units;
	}

	@Override
	public int size() {
		return str.length();
	}

}
