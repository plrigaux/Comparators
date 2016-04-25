package com.plr.comparator.alphanum;

public class NumberTokenComparable extends TokenComparable {

	boolean negative;
	
	NumberTokenComparable(String s) {
		super(s);		
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

			return -1 * comapreNum(s, o.s, 1, o);
		} else {
			if (o.isNegative()) {
				return 1;
			}

			return comapreNum(s, o.s, 0, o);
		}

	}

	private int comapreNum(String s1, String s2, int start, TokenComparable o) {

		int beg1 = start;
		int beg2 = start;
		
		int last1 = s1.length();
		int last2 = s2.length();
		
		//clean zeros
		for (; beg1 < last1; beg1++) {
			if (s1.charAt(beg1) != '0') {
				break;
			}
		}
		
		for (; beg2 < last2; beg2++) {
			if (s2.charAt(beg2) != '0') {
				break;
			}
		}
		
		int result = (last1 - beg1) - (last2 - beg2);
		
		// If equal size, the first different number counts
		if (result == 0) {
			for (int i = beg1, j = beg2; i < last1; i++, j++) {
				result = s1.charAt(i) - s2.charAt(j);
				if (result != 0) {
					return result;
				}
			}
			
			//look at zeros difference
			
			return beg1 - beg2;
//			if (beg1 != start || beg2 != start) {
//				result = super.compareTo(o);
//			}
		}

		return result;
	}

	boolean isNegative() {
		return s.charAt(0) == '-';
	}

}
