package com.plr.comparator.alphanum;

import java.util.Comparator;

public class NumberTokenComparable extends TokenComparable {

	private final boolean isNegative;
	private final String units;
	private final String decimal;
	private final String str;

	NumberTokenComparable(boolean isNegative, String units, String decimal, Comparator<String> comparator) {
		super(comparator);
		this.isNegative = isNegative;
		this.decimal = decimal;
		this.units = units;

		String str = isNegative ? "-" : "";
		str += units;
		if (decimal != null) {
			str += "." + decimal;
		}
		this.str = str;
	}

	@Override
	boolean isNumber() {
		return true;
	}

	@Override
	public int compareTo(TokenComparable other) {
		if (!other.isNumber()) {
			return super.compareTo(other);
		}

		NumberTokenComparable otherNTC = (NumberTokenComparable) other;

		if (isNegative()) {
			if (!other.isNegative()) {
				return -1;
			}

			return -1 * comapreNum(units, otherNTC.units, otherNTC);
		} else {
			if (other.isNegative()) {
				return 1;
			}

			return comapreNum(units, otherNTC.units, otherNTC);
		}

	}

	private int comapreNum(String s1, String s2, NumberTokenComparable o) {

		int beg1 = 0;
		int beg2 = 0;

		int last1 = s1.length();
		int last2 = s2.length();

		// clean front space
		for (; beg1 < last1; beg1++) {
			if (!Character.isWhitespace(s1.charAt(beg1))) {
				break;
			}
		}

		for (; beg2 < last2; beg2++) {
			if (!Character.isWhitespace(s2.charAt(beg2))) {
				break;
			}
		}

		// clean zeros
		for (; beg1 < last1 - 1; beg1++) {
			if (s1.charAt(beg1) != '0') {
				break;
			}
		}

		for (; beg2 < last2 - 1; beg2++) {
			if (s2.charAt(beg2) != '0') {
				break;
			}
		}

		int result = (last1 - beg1) - (last2 - beg2);

		// If equal size, the first different number counts
		if (result == 0) {
			for (int i = beg1, j = beg2; i < last1; i++, j++) {
				result = s1.charAt(i) - s2.charAt(j);
				if (result != 0) {
					return result;
				}
			}

			result = comapreDecimals(o);

			// look at zeros difference
			if (result != 0) {
				return result;
			}

			return beg1 - beg2;
			// if (beg1 != start || beg2 != start) {
			// result = super.compareTo(o);
			// }
		}

		return result;
	}

	private int comapreDecimals(NumberTokenComparable o) {

		String s1 = this.decimal;
		String s2 = o.decimal;

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

		int beg1 = 0;
		int beg2 = 0;

		int last1 = s1.length();
		int last2 = s2.length();


		// clean zeros
		for (; beg1 < last1 - 1; beg1++) {
			if (s1.charAt(beg1) != '0') {
				break;
			}
		}

		for (; beg2 < last2 - 1; beg2++) {
			if (s2.charAt(beg2) != '0') {
				break;
			}
		}

		int result = (last1 - beg1) - (last2 - beg2);

		// If equal size, the first different number counts
		if (result == 0) {
			for (int i = beg1, j = beg2; i < last1; i++, j++) {
				result = s1.charAt(i) - s2.charAt(j);
				if (result != 0) {
					return result;
				}
			}

			// look at zeros difference

			return beg1 - beg2;
			// if (beg1 != start || beg2 != start) {
			// result = super.compareTo(o);
			// }
		}

		return result;
	}

	boolean isNegative() {
		return isNegative;
	}

	@Override
	public String getStr() {
		return str;
	}

	public String getDecimal() {
		return decimal;
	}

	public String getUnit() {
		return units;
	}

}
