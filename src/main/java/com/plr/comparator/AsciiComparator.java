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
		while (k < lim) {
			char c1 = o1.charAt(k);
			char c2 = o2.charAt(k);

            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }
        return len1 - len2;
	}

}
