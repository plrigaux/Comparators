package com.plr.comparator.natural;

public class StringBuilderSpecial2 implements CharSequence, Comparable<StringBuilderSpecial2> {
    /**
     * The value is used for character storage.
     */
	private CharSequence value;

	private final int count;
	private final int start;
//	private final int end;
	
    StringBuilderSpecial2(CharSequence charSequence, int start, int end) {
    	value = charSequence;
    	this.start = start;
//    	this.end = end;
    	this.count = end - start;
    }
    
	@Override
	public int length() {
		return count;
	}

	@Override
	public char charAt(int index) {
		return value.charAt(start + index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return new StringBuilderSpecial2(value, start, end);
	}

	@Override
	public int compareTo(StringBuilderSpecial2 o) {

		int len1 = length();
		int len2 = o.length();
		int lim = Math.min(len1, len2);

		int k = 0;
		while (k < lim) {
			char c1 = this.charAt(k);
			char c2 = o.charAt(k);
			if (c1 != c2) {
				return c1 - c2;
			}
			k++;
		}
		return len1 - len2;
	}

	
	@Override
	public String toString() {
		return new StringBuilder(this).toString();
	}

}
