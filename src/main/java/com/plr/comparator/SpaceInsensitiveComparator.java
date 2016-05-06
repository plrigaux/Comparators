package com.plr.comparator;

import java.util.Comparator;

public class SpaceInsensitiveComparator implements Comparator<CharSequence> {
	
	static private final SpaceInsensitiveComparator instance = new SpaceInsensitiveComparator();
	
	@Override
	public int compare(CharSequence s1, CharSequence s2) {

		int len1 = s1.length();
		int len2 = s2.length();

		int k = 0;
		int l = 0;

		boolean klen = k < len1;
		boolean llen = l < len2;

		outerloop: while (klen && llen) {
			char c1 = s1.charAt(k);
			char c2 = s2.charAt(l);

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

	 static int evaluateLength(CharSequence string, int length, int index, int result) {
		do {
			char ch = string.charAt(index);
			if (!Character.isWhitespace(ch)) {
				return result;
			}
		} while (++index < length);

		return 0;
	}

	public static SpaceInsensitiveComparator getInstance() {
		return instance;
	}

	
}
