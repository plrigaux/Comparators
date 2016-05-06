package com.plr.comparator;

import java.util.Comparator;

public class AsciiComparator implements Comparator<CharSequence> {

	final private static AsciiComparator instance = new AsciiComparator();

	public static AsciiComparator getInstance() {
		return instance;
	}

	@Override
	public int compare(CharSequence o1, CharSequence o2) {

		int len1 = o1.length();
		int len2 = o2.length();
		int lim = Math.min(len1, len2);

		int k = 0;
		int result = 0;
		while (k < lim) {
			char ss1 = o1.charAt(k);
			char ss2 = o2.charAt(k);

			result = ss1 - ss2;

			if (result != 0) {
				return result;
			}
			k++;
		}

		return result;
	}

}
