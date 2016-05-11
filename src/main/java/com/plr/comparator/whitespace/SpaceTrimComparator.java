package com.plr.comparator.whitespace;

import java.util.Comparator;

import com.plr.comparator.Utils;

public class SpaceTrimComparator implements Comparator<CharSequence> {

	static private final SpaceTrimComparator instance = new SpaceTrimComparator();

	public static SpaceTrimComparator getInstance() {
		return instance;
	}

	@Override
	public int compare(CharSequence s1, CharSequence s2) {

		int len1 = Utils.rightTrim(s1);
		int len2 = Utils.rightTrim(s2);

		int k = Utils.leftTrim(s1);
		int l = Utils.leftTrim(s2);

		return compare(s1, s2, len1, len2, k, l);
	}

	int compare(CharSequence s1, CharSequence s2, int len1, int len2, int k, int l) {
		boolean klen = k < len1;
		boolean llen = l < len2;

		while (klen && llen) {
			char c1 = s1.charAt(k);
			char c2 = s2.charAt(l);

			int result = c1 - c2;
			if (result != 0) {
				return result;
			}

			klen = ++k < len1;
			llen = ++l < len2;
		}

		return klen ? 1 : (llen ? -1 : 0);
	}
}
