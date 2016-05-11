package com.plr.comparator.whitespace;

import com.plr.comparator.Utils;

public class SpaceRightTrimComparator extends SpaceTrimComparator {

	static private final SpaceRightTrimComparator instance = new SpaceRightTrimComparator();

	public static SpaceRightTrimComparator getInstance() {
		return instance;
	}

	@Override
	public int compare(CharSequence s1, CharSequence s2) {

		int len1 = Utils.rightTrim(s1);
		int len2 = Utils.rightTrim(s2);

		int k = 0;
		int l = 0;

		return compare(s1, s2, len1, len2, k, l);
	}
	
	
}
