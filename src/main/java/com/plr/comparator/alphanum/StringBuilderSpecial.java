package com.plr.comparator.alphanum;

public class StringBuilderSpecial implements CharSequence, Comparable<StringBuilderSpecial> {
    /**
     * The value is used for character storage.
     */
	private char[] value;

    /**
     * The count is the number of characters used.
     */
	private int count;
    
    StringBuilderSpecial(int size, int adf) {
    	value = new char[size];
    	count = 0;
    }
    
	@Override
	public int length() {
		return count;
	}

	@Override
	public char charAt(int index) {
		return value[index];
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return new String(value, start, end);
	}

	@Override
	public int compareTo(StringBuilderSpecial o) {

		int len1 = length();
		int len2 = o.length();
		int lim = Math.min(len1, len2);

		int k = 0;
		while (k < lim) {
			char c1 = value[k];
			char c2 = o.value[k];
			if (c1 != c2) {
				return c1 - c2;
			}
			k++;
		}
		return len1 - len2;
	}

	public void append(char c) {
		value[count++] = c;
	}
	
	@Override
	public String toString() {
		return new String(value, 0, count);
	}

}
