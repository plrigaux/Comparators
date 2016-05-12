package com.plr.comparator.insensitive;

public class IgnoreCase extends CharacterComparisonStrategy {
	
	static private IgnoreCase instance = new IgnoreCase();

	public static IgnoreCase getInstance() {
		return instance;
	}
	
	int compare (char c1, char c2) {
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
		return 0;
	}
}
