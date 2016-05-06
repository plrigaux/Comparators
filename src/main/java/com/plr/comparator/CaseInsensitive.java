package com.plr.comparator;

import java.util.Comparator;

public class CaseInsensitive implements Comparator<CharSequence> {

	private final static CaseInsensitive instance = new CaseInsensitive();

	public int compare(CharSequence s1, CharSequence s2) {
		int n1 = s1.length();
		int n2 = s2.length();
		int min = Math.min(n1, n2);
		for (int i = 0; i < min; i++) {
			char c1 = s1.charAt(i);
			char c2 = s2.charAt(i);
			if (c1 != c2) {
				c1 = Character.toUpperCase(c1);
				c2 = Character.toUpperCase(c2);
				if (c1 != c2) {
					c1 = Character.toLowerCase(c1);
					c2 = Character.toLowerCase(c2);
					if (c1 != c2) {
						// No overflow because of numeric promotion
						return c1 - c2;
					}
				}
			}
		}
		return n1 - n2;
	}

	public static CaseInsensitive getInstance() {
		return instance;
	}

}