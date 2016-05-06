package com.plr.comparator.alphanum;

public class AlphaTokenComparable extends TokenComparable {

	private final CharSequence str;;

	AlphaTokenComparable(CharSequence str, NaturalComparator naturalComparator) {
		super(naturalComparator);
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

	@Override
	public int compareTo(TokenComparable other) {
		CharSequence s1 = getStr();
		CharSequence s2 = other.getStr();

		if (naturalComparator.isSpaceInsensitve()) {
			return compareSpaceInsensitve(s1, s2);
		} else if (naturalComparator.isSpaceInsensitve2()) {
			// return compareSpaceInsensitve2(s1, s2);
		}

		return comparator.compare(s1, s2);
	}

	static int compareSpaceInsensitve(CharSequence s1, CharSequence s2) {

		int len1 = s1.length();
		int len2 = s2.length();

		int k = 0;
		int l = 0;

		char c1;
		char c2;

		boolean klen = k < len1;
		boolean llen = l < len2;

		outerloop: while (klen && llen) {
			c1 = s1.charAt(k);
			c2 = s2.charAt(l);

			while (Character.isWhitespace(c1)) {
				if (klen = ++k < len1) {
					c1 = s1.charAt(k);
				} else {
					break outerloop;
				}
			}

			while (Character.isWhitespace(c2)) {
				if (llen = ++l < len2) {
					c2 = s2.charAt(l);
				} else {
					break outerloop;
				}
			}

			if (c1 != c2) {
				return c1 - c2;
			}

			klen = ++k < len1;
			llen = ++l < len2;

		}

		if (klen) {
			return evaluateLength(s1, len1, k, 1);
		} else if (llen) {
			return evaluateLength(s2, len2, l, -1);
		}

		return 0;
	}

	private static int evaluateLength(CharSequence string, int length, int index, int result) {
		do {
			char ch = string.charAt(index);
			if (!Character.isWhitespace(ch)) {
				int v = ch;
				System.out.println("c:'"+ ch + "' v: " + v);		
				return result;
			}
		} while (++index < length);

		return 0;
	}

	static int compareSpaceInsensitve2(CharSequence s1, CharSequence s2) {

		int len1 = s1.length();
		int len2 = s2.length();

		int k = 0;
		int l = 0;

		char c1;
		char c2;

		char cc1 = 0;
		char cc2 = 0;

		boolean klen = k < len1;
		boolean llen = l < len2;

		outerloop: while (klen && llen) {
			c1 = s1.charAt(k);
			c2 = s2.charAt(l);

			while (Character.isWhitespace(cc1) && Character.isWhitespace(c1)) {
				if (klen = ++k < len1) {
					c1 = s1.charAt(k);
				} else {
					break outerloop;
				}
			}

			while (Character.isWhitespace(cc2) && Character.isWhitespace(c2)) {
				if (llen = ++l < len2) {
					c2 = s2.charAt(l);
				} else {
					break outerloop;
				}
			}

			if (Character.isWhitespace(c1)) {
				c1 = ' ';
			}

			if (Character.isWhitespace(c2)) {
				c2 = ' ';
			}

			if (c1 != c2) {
				return c1 - c2;
			}

			klen = ++k < len1;
			llen = ++l < len2;

			cc1 = c1;
			cc2 = c2;
		}

		if (klen) {
			return Character.isWhitespace(cc1) ? evaluateLength(s1, len1, l, 1) : 1;
		} else if (llen) {
			return Character.isWhitespace(cc2) ? evaluateLength(s2, len2, l, -1) : -1;
		}

		return 0;
	}
}
