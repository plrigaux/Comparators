package com.plr.comparator.insensitive;

public class CharacterComparisonStrategy {

	int compare (char c1, char c2) {
		return c1 - c2;
	}
	
	static private CharacterComparisonStrategy instance = new CharacterComparisonStrategy();

	public static CharacterComparisonStrategy getInstance() {
		return instance;
	}
}
