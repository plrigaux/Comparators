package com.plr.comparator.insensitive;

public class CharacterComparatorAscii implements CharacterComparator {

	public int compare (char c1, char c2) {
		return c1 - c2;
	}
	
	static private CharacterComparator instance = new CharacterComparatorAscii();

	public static CharacterComparator getInstance() {
		return instance;
	}
}
