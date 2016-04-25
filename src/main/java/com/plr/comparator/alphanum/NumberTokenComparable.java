package com.plr.comparator.alphanum;

import com.google.common.base.CharMatcher;

public class NumberTokenComparable extends TokenComparable {

	boolean negative;
	
	NumberTokenComparable(String s) {
		super(s);
		
		
		

		cleanZeros();
		
		
	}
	
	private void cleanNeg() {
		
		
		
	}
	
	private void cleanZeros() {

		int len = s.length();

		if (len == 1) {
			return;
		}

		int first;
		int last = len - 1;

		for (first = 0; first < len; first++) {
			if (s.charAt(first) == '0') {
				break;
			}
		}
		/*
		 * for (last = len - 1; last > first; last--) { if (s.charAt(last) ==
		 * '0') { break; } }
		 */
		s = s.substring(first, last + 1).toString();
	}

	@Override
	boolean isNumber() {
		return true;
	}

	@Override
	public int compareTo(TokenComparable o) {
		if (!o.isNumber()) {
			return super.compareTo(o);
		}

		if (isNegative()) {
			if (!o.isNegative()) {
				return -1;
			}

			return -1 * comapreNum(s, o.s, 1);
		} else {
			if (o.isNegative()) {
				return 1;
			}

			return comapreNum(s, o.s, 0);
		}

	}

	private int comapreNum(String s1, String s2, int start) {

		int result = s1.length() - s2.length();

		// If equal, the first different number counts
		if (result == 0) {
			for (int i = start; i < s.length(); i++) {
				result = s1.charAt(i) - s2.charAt(i);
				if (result != 0) {
					return result;
				}
			}
		}

		return result;
	}

	boolean isNegative() {
		return negative;
	}

}
