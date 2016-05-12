package com.plr.comparator.insensitive;

import com.plr.comparator.Utils;

abstract class BeginEndFlexibleComparator {

	abstract int compare(CharSequence string1, CharSequence string2, int length1, int length2, int index1, int index2,
			InsensitiveComparator insensitiveComparator);
	
	int compare(CharSequence s1, CharSequence s2, InsensitiveComparator insensitiveComparator) {
		int length1 = s1.length();
		int length2 = s2.length();

		int index1 = 0;
		int index2 = 0;

		return compare(s1, s2, length1, length2, index1, index2, insensitiveComparator);
	}
	
	static abstract class TrimableComparator extends BeginEndFlexibleComparator {
		public int compare(CharSequence s1, CharSequence s2, InsensitiveComparator insensitiveComparator) {
			int length1 = Utils.rightTrim(s1, insensitiveComparator.rightTrimer);
			int length2 = Utils.rightTrim(s2, insensitiveComparator.rightTrimer);

			int index1 = Utils.leftTrim(s1, insensitiveComparator.leftTrimer);
			int index2 = Utils.leftTrim(s2, insensitiveComparator.leftTrimer);

			return compare(s1, s2, length1, length2, index1, index2, insensitiveComparator);
		}
	}
	
	static BeginEndFlexibleComparator REPETITION_INSENSITIVE = new RepetitionInsensitiveComparator();
	
	static final class RepetitionInsensitiveComparator extends TrimableComparator {

		public int compare(CharSequence s1, CharSequence s2, int length1, int length2, int index1, int index2,
				InsensitiveComparator insensitiveComparator) {
			char c1;
			char c2;

			char cc1 = 0;
			char cc2 = 0;

			boolean klen = index1 < length1;
			boolean llen = index2 < length2;

			outerloop: while (klen && llen) {
				c1 = s1.charAt(index1);
				c2 = s2.charAt(index2);

				while (insensitiveComparator.ignoreOn.matches(cc1) && insensitiveComparator.ignoreOn.matches(c1)) {
					if (klen = ++index1 < length1) {
						c1 = s1.charAt(index1);
					} else {
						break outerloop;
					}
				}

				while (insensitiveComparator.ignoreOn.matches(cc2) && insensitiveComparator.ignoreOn.matches(c2)) {
					if (llen = ++index2 < length2) {
						c2 = s2.charAt(index2);
					} else {
						break outerloop;
					}
				}

				if (insensitiveComparator.ignoreOn.matches(c1)) {
					c1 = insensitiveComparator.replace;
				}

				if (insensitiveComparator.ignoreOn.matches(c2)) {
					c2 = insensitiveComparator.replace;
				}

				int result = insensitiveComparator.characterComparisonStrategy.compare(c1, c2);;
				if (result != 0) {
					return result;
				}

				klen = ++index1 < length1;
				llen = ++index2 < length2;

				cc1 = c1;
				cc2 = c2;
			}

			if (klen) {
				return Character.isWhitespace(cc1)
						? Utils.evaluateLength(s1, insensitiveComparator.ignoreOn, length1, index2, 1) : 1;
			} else if (llen) {
				return Character.isWhitespace(cc2)
						? Utils.evaluateLength(s2, insensitiveComparator.ignoreOn, length2, index2, -1) : -1;
			}

			return 0;
		}
	}

	static BeginEndFlexibleComparator SPACE_TRIM = new SpaceTrimComparator();
	
	static final class SpaceTrimComparator extends TrimableComparator {

		public int compare(CharSequence s1, CharSequence s2, int length1, int length2, int index1, int index2,
				InsensitiveComparator insensitiveComparator) {
			boolean klen = index1 < length1;
			boolean llen = index2 < length2;

			while (klen && llen) {
				char c1 = s1.charAt(index1);
				char c2 = s2.charAt(index2);

				int result = insensitiveComparator.characterComparisonStrategy.compare(c1, c2);;
				if (result != 0) {
					return result;
				}

				klen = ++index1 < length1;
				llen = ++index2 < length2;
			}

			return klen ? 1 : (llen ? -1 : 0);
		}
	}
	
	static BeginEndFlexibleComparator CHARACTER_INSENSITIVE = new SpaceInsensitiveComparator();
	
	private static class SpaceInsensitiveComparator extends BeginEndFlexibleComparator {

		public int compare(CharSequence s1, CharSequence s2, int length1, int length2, int index1, int index2,
				InsensitiveComparator insensitiveComparator) {

			boolean klen = index1 < length1;
			boolean llen = index2 < length2;

			outerloop: while (klen && llen) {
				char c1 = s1.charAt(index1);
				char c2 = s2.charAt(index2);

				while (insensitiveComparator.ignoreOn.matches(c1)) {
					if (klen = ++index1 < length1) {
						c1 = s1.charAt(index1);
					} else {
						break outerloop;
					}
				}

				while (insensitiveComparator.ignoreOn.matches(c2)) {
					if (llen = ++index2 < length2) {
						c2 = s2.charAt(index2);
					} else {
						break outerloop;
					}
				}

				int result = insensitiveComparator.characterComparisonStrategy.compare(c1, c2);;
				if (result != 0) {
					return result;
				}

				klen = ++index1 < length1;
				llen = ++index2 < length2;

			}

			if (klen) {
				return Utils.evaluateLength(s1, insensitiveComparator.ignoreOn, length1, index1, 1);
			} else if (llen) {
				return Utils.evaluateLength(s2, insensitiveComparator.ignoreOn, length2, index2, -1);
			}

			return 0;
		}
	}
}