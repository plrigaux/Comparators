package com.plr.comparator.whitespace;

import java.util.Comparator;

import com.plr.comparator.Utils;

public class SpaceInsensitiveComparator implements Comparator<CharSequence> {
	
	static private final SpaceInsensitiveComparator instance = new SpaceInsensitiveComparator();
	
	public static SpaceInsensitiveComparator getInstance() {
		return instance;
	}
	
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
			return Utils.evaluateLength(s1, len1, k, 1);
		} else if (llen) {
			return Utils.evaluateLength(s2, len2, l, -1);
		}

		return 0;
	}
}
