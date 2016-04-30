package com.plr.comparator.alphanum;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * The Alphanum Algorithm is an improved sorting algorithm for strings
 * containing numbers.  Instead of sorting numbers in ASCII order like
 * a standard sort, this algorithm sorts numbers in numeric order.
 *
 * The Alphanum Algorithm is discussed at http://www.DaveKoelle.com
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.plr.comparator.alphanum.NaturalComparator.Flags.*;

public class NaturalComparator implements Comparator<CharSequence> {

	private static final Logger logger = LoggerFactory.getLogger(NaturalComparator.class);

	final private Comparator<CharSequence> alphaComparator;

	// TODO make the regex not capture the point
	// final static String NUM_PAT =
	// "(?:\\s)*((?:^|\\s)[-])?0*([1-9]\\d*|0)((\\.\\d++)(?!\\.\\d))?";

	// Compare without space
	// final static String NUM_PAT =
	// "\\s+|(?:(?:(?<=^|\\s)[-])?0*([1-9]\\d*|0))((\\.\\d++)(?!\\.\\d))?";

	final static String NUM_PAT = "(?:\\s)*(?:((?<=^|\\s)[-])?0*([1-9]\\d*|0))((\\.\\d++)(?!\\.\\d))?";

	final static public Comparator<CharSequence> ASCII = new Comparator<CharSequence>() {
		@Override
		public int compare(CharSequence o1, CharSequence o2) {

			int len1 = o1.length();
			int len2 = o2.length();
			int lim = Math.min(len1, len2);

			int k = 0;
			int result = 0;
			while (k < lim) {
				char ss1 = o1.charAt(k);
				char ss2 = o2.charAt(k);

				result = ss1 - ss2;

				if (result != 0) {
					return result;
				}
				k++;
			}

			return result;
		}
	};

	public enum Flags {
		PRIMARY, SECONDARY, LTRIM, RTRIM, TRIM, SPACE_INSENSITVE, SPACE_INSENSITVE2
	};

	public NaturalComparator(Flags... flags) {
		this(ASCII, flags);
	}

	private boolean pureNumbers;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public NaturalComparator(Collator collator, Flags... flags) {
		this((Comparator) collator, flags);

	}

	final private Pattern pattern;

	private final EnumSet<NaturalComparator.Flags> flagSet = EnumSet.noneOf(Flags.class);

	public NaturalComparator(Comparator<CharSequence> alphaComparator, Flags... flags) {
		this.alphaComparator = alphaComparator;

		flagSet.addAll(Arrays.asList(flags));

		pureNumbers = flagSet.contains(Flags.PRIMARY);

		pattern = Pattern.compile(NUM_PAT);
	}

	List<TokenComparable> split(CharSequence toSplit) {

		List<TokenComparable> list = new ArrayList<>();

		if (flagSet.contains(SPACE_INSENSITVE)) {
			toSplit = spaceInsensible(toSplit);
		} else if (flagSet.contains(SPACE_INSENSITVE2)) {
			toSplit = spaceInsensible2(toSplit);
		}

		Matcher matcher = pattern.matcher(toSplit);

		trim(toSplit, matcher);

		int start = matcher.regionStart();

		int j = 0;
		while (matcher.find()) {

			if (logger.isDebugEnabled()) {
				logger.debug("toSplit:'{}'", toSplit);
				for (int i = 0; i < matcher.groupCount(); i++) {
					logger.debug("find {} Gr{}: '{}'", j, i, matcher.group(i));
				}
				j++;
				logger.debug("");
			}

			if (start != matcher.start()) {
//				CharSequence prev = toSplit.subSequence(start, matcher.start());
				CharSequence prev = new StringBuilderSpecial2(toSplit, start, matcher.start());
				list.add(new AlphaTokenComparable(prev, alphaComparator));
			}

			CharSequence wholeStr = new StringBuilderSpecial2(toSplit, matcher.start(0), matcher.end(0));
			

			CharSequence number = new StringBuilderSpecial2(toSplit, matcher.start(2), matcher.end(2));
			
			// TODO remove the group for neg
			boolean isNegative = matcher.group(1) != null;

			// TODO try to find why there is a dot at front (+1)
			CharSequence decimal = null;
			
			int end3 = matcher.end(3);
			if (end3 >= 0) {
				decimal = new StringBuilderSpecial2(toSplit, matcher.start(3) + 1, end3);
			}
			
			list.add(new NumberTokenComparable(isNegative, number, decimal, wholeStr, alphaComparator));

			start = matcher.end();
		}

		if (start != matcher.regionEnd()) {
			CharSequence last = toSplit.subSequence(start, matcher.regionEnd());
			list.add(new AlphaTokenComparable(last, alphaComparator));
		}

		return list;
	}

	private CharSequence spaceInsensible(CharSequence toSplit) {
		int l = toSplit.length();
		StringBuilderSpecial sb = new StringBuilderSpecial(l);
		int i = 0;

		while (i < l) {
			char ch = toSplit.charAt(i);
			if (!Character.isWhitespace(ch)) {
				sb.append(ch);
			}
			i++;
		}
		toSplit = sb;
		return toSplit;
	}

	private CharSequence spaceInsensible2(CharSequence toSplit) {
		int l = toSplit.length();

		l = _rtrim(toSplit);

		StringBuilderSpecial sb = new StringBuilderSpecial(l);

		int i = _rtrim(toSplit);

		boolean isWhitespace = true;
		boolean wasWhitespace = false;
		while (i < l) {
			char ch = toSplit.charAt(i);

			isWhitespace = Character.isWhitespace(ch);
			if (!isWhitespace) {
				sb.append(ch);
			} else if (!wasWhitespace) {
				sb.append(' ');
			}
			wasWhitespace = isWhitespace;
			i++;
		}
		toSplit = sb;
		return toSplit;
	}

	private void trim(CharSequence toSplit, Matcher matcher) {
		int rstrat = 0;
		if (flagSet.contains(TRIM) || flagSet.contains(LTRIM)) {
			rstrat = _ltrim(toSplit);
		}

		int rend = toSplit.length();
		if (flagSet.contains(TRIM) || flagSet.contains(RTRIM)) {
			rend = _rtrim(toSplit);
		}

		matcher.region(rstrat, rend);
	}

	public int compare(CharSequence s1, CharSequence s2) {

		List<TokenComparable> list1 = split(s1);
		List<TokenComparable> list2 = split(s2);

		if (logger.isDebugEnabled()) {
			logger.debug("list1: {}", list1);
			logger.debug("list2: {}", list2);
		}

		int len1 = list1.size();
		int len2 = list2.size();
		int lim = Math.min(len1, len2);

		int k = 0;
		int result;
		while (k < lim) {
			TokenComparable ss1 = list1.get(k);
			TokenComparable ss2 = list2.get(k);

			result = ss1.compareTo(ss2);

			if (result != 0) {
				return result;
			}
			k++;
		}

		result = len1 - len2;

		if (pureNumbers && result != 0) {

			int limMax;
			List<TokenComparable> listMax;
			if (result > 0) {
				listMax = list1;
				limMax = len1;
			} else {
				listMax = list2;
				limMax = len2;
			}

			TokenComparable ss = listMax.get(k);

			boolean isAllWhiteSpace = ss.isAllWhiteSpace();

			// means that is tailing white space
			if (isAllWhiteSpace && limMax == (k + 1)) {
				result = 0;
			}
		}

		if (result != 0) {
			return result;
		}

		// For now equals, now compare leading zeros
		if (!pureNumbers) {
			k = 0;
			while (k < lim) {
				TokenComparable ss1 = list1.get(k);
				TokenComparable ss2 = list2.get(k);

				result = ss1.compareLeadingZerosTo(ss2);

				if (result != 0) {
					return result;
				}
				k++;
			}

			if (result == 0) {
				CharSequence st1 = s1;
				CharSequence st2 = s2;

				if (flagSet.contains(Flags.TRIM)) {
					st1 = trim(s1);
					st2 = trim(s1);
				} else if (flagSet.contains(Flags.LTRIM)) {
					st1 = ltrim(s1);
					st2 = ltrim(s2);
				} else if (flagSet.contains(Flags.TRIM)) {
					st1 = rtrim(s1);
					st2 = rtrim(s2);
				}

				result = ASCII.compare(st1, st2);
			}
		}

		return result;

	}

	public boolean equals(String s1, String s2) {
		return compare(s1, s2) == 0;
	}

	private static CharSequence ltrim(CharSequence s) {
		int i = _ltrim(s);
		return s.subSequence(i, -1);
	}

	private static CharSequence rtrim(CharSequence s) {
		int i = _rtrim(s);
		return s.subSequence(0, i + 1);
	}

	private static CharSequence trim(CharSequence s) {
		int i = _ltrim(s);

		int j = _rtrim(s);

		return s.subSequence(i, j);
	}

	private static int _ltrim(CharSequence s) {
		int i = 0;
		while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
			i++;
		}
		return i;
	}

	private static int _rtrim(CharSequence s) {
		int i = s.length() - 1;
		while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
			i--;
		}
		return i + 1;
	}

}