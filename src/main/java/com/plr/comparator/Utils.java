package com.plr.comparator;

import com.google.common.base.CharMatcher;

public class Utils {

	/**
	 * Returns the index within the passed string of the first occurrence of a
	 * non white space character starting form the string beginning.
	 *
	 * @param s
	 *            a character string.
	 * @return the index of the first occurrence of the character in the
	 *         character sequence represented by this object, or the string
	 *         length if a non white space character not occurred.
	 */
	static public int leftTrim(CharSequence s) {
		int i = 0;
		int length = s.length();
		while (i < length && Character.isWhitespace(s.charAt(i))) {
			i++;
		}
		return i;
	}

	/**
	 * Returns the length of the passed string for the last occurrence of a
	 * non white space character.
	 *
	 * @param s
	 *            a character string.
	 * @return the index of the first occurrence of the character in the
	 *         character sequence represented by this object, or {@code 0} if a
	 *         non white space character not occurred.
	 */
	static public int rightTrim(CharSequence s) {
		int i = s.length() - 1;
		while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
			i--;
		}
		return i + 1;
	}

	static public int rightTrim(CharSequence s, CharMatcher charMatcher) {
		int i = s.length() - 1;
		while (i >= 0 && charMatcher.matches(s.charAt(i))) {
			i--;
		}
		return i + 1;
	}
	
	static public int leftTrim(CharSequence s, CharMatcher charMatcher) {
		int i = 0;
		int length = s.length();
		while (i < length && charMatcher.matches(s.charAt(i))) {
			i++;
		}
		return i;
	}
	
	public static int evaluateLength(CharSequence string, int length, int index, int result) {
		do {
			char ch = string.charAt(index);
			if (!Character.isWhitespace(ch)) {
				return result;
			}
		} while (++index < length);
	
		return 0;
	}

	public static int evaluateLength(CharSequence string, CharMatcher ignoreOn, int length, int index, int result) {
		do {
			char ch = string.charAt(index);
			if (!Character.isWhitespace(ch)) {
				return result;
			}
		} while (++index < length);
	
		return 0;
	}
}
