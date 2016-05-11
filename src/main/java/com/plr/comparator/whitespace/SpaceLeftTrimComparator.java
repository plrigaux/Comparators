package com.plr.comparator.whitespace;

import com.plr.comparator.Utils;

public class SpaceLeftTrimComparator extends SpaceTrimComparator {

	static private final SpaceLeftTrimComparator instance = new SpaceLeftTrimComparator();

	public static SpaceLeftTrimComparator getInstance() {
		return instance;
	}

	@Override
	public int compare(CharSequence s1, CharSequence s2) {

		int len1 = s1.length();
		int len2 = s2.length(); 

		int k = Utils.leftTrim(s1);
		int l = Utils.leftTrim(s2);
		
		return compare(s1, s2, len1, len2, k, l);
	}
}
