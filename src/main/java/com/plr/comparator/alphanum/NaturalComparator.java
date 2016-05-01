package com.plr.comparator.alphanum;

import static com.plr.comparator.alphanum.NaturalComparator.Flags.LTRIM;
import static com.plr.comparator.alphanum.NaturalComparator.Flags.RTRIM;
import static com.plr.comparator.alphanum.NaturalComparator.Flags.SPACE_INSENSITVE;
import static com.plr.comparator.alphanum.NaturalComparator.Flags.SPACE_INSENSITVE2;
import static com.plr.comparator.alphanum.NaturalComparator.Flags.TRIM;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NaturalComparator implements Comparator<CharSequence> {

	private static final Logger logger = LoggerFactory.getLogger(NaturalComparator.class);

	final private Comparator<CharSequence> alphaComparator;

	// TODO make the regex not capture the point
	// final static String NUM_PAT =
	// "(?:\\s)*((?:^|\\s)[-])?0*([1-9]\\d*|0)((\\.\\d++)(?!\\.\\d))?";

	// Compare without space
	// final static String NUM_PAT =
	// "\\s+|(?:(?:(?<=^|\\s)[-])?0*([1-9]\\d*|0))((\\.\\d++)(?!\\.\\d))?";

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

	private final EnumSet<NaturalComparator.Flags> flagSet = EnumSet.noneOf(Flags.class);

	public NaturalComparator(Comparator<CharSequence> alphaComparator, Flags... flags) {
		this.alphaComparator = alphaComparator;

		flagSet.addAll(Arrays.asList(flags));

		pureNumbers = flagSet.contains(Flags.PRIMARY);

	}

	// Just for tests
	List<TokenComparable> split(CharSequence s1) {
		Tokenizer splitter1 = new Tokenizer(this, s1);
		return splitter1.split();
	}

	public int compare(CharSequence s1, CharSequence s2) {

		Tokenizer splitter1 = new Tokenizer(this, s1);
		Tokenizer splitter2 = new Tokenizer(this, s2);

		// List<TokenComparable> list1 = splitter1.split();
		// List<TokenComparable> list2 = splitter2.split();

		// if (logger.isDebugEnabled()) {
		// logger.debug("list1: {}", list1);
		// logger.debug("list2: {}", list2);
		// }

		List<TokenComparable> list1 = new ArrayList<>();
		List<TokenComparable> list2 = new ArrayList<>();

		int result = 0;
		// boolean hasNext1;
		// boolean hasNext2 = false;

		while (splitter1.hasNext() && splitter2.hasNext()) {
			TokenComparable ss1 = splitter1.next();
			TokenComparable ss2 = splitter2.next();

			list1.add(ss1);
			list2.add(ss2);

			result = ss1.compareTo(ss2);

			if (result != 0) {
				return result;
			}
		}

		boolean oneGreater = splitter1.hasNext() ^ splitter2.hasNext();

		if (oneGreater) {
			return splitter1.hasNext() ? 1 : -1;
		}

		if (pureNumbers && (splitter1.hasNext() || splitter2.hasNext())) {

			// System.out.println(splitter1.hasNext());
			// System.out.println(splitter2.hasNext());
			// List<TokenComparable> listMax;
			Tokenizer tokenizerMax = splitter1.hasNext() ? splitter1 : splitter2;

			TokenComparable ss = tokenizerMax.next();

			boolean isAllWhiteSpace = ss.isAllWhiteSpace();

			// means that is tailing white space
			if (isAllWhiteSpace && !tokenizerMax.hasNext()) {
				result = 0;
			}

			return result;
		}

		// For now equals, now compare leading zeros
		if (!pureNumbers) {
			int k = 0;
			int lim = list1.size();
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

				// if (flagSet.contains(Flags.TRIM)) {
				// st1 = trim(s1);
				// st2 = trim(s1);
				// } else if (flagSet.contains(Flags.LTRIM)) {
				// st1 = ltrim(s1);
				// st2 = ltrim(s2);
				// } else if (flagSet.contains(Flags.TRIM)) {
				// st1 = rtrim(s1);
				// st2 = rtrim(s2);
				// }

				result = ASCII.compare(st1, st2);
			}
		}

		return result;

	}

	public boolean equals(String s1, String s2) {
		return compare(s1, s2) == 0;
	}

	public Comparator<CharSequence> getAlphaComparator() {
		return alphaComparator;
	}

	public boolean isTrim() {
		return flagSet.contains(TRIM);
	}

	public boolean isRTrim() {
		return flagSet.contains(RTRIM);
	}

	public boolean isLTrim() {
		return flagSet.contains(LTRIM);
	}

	public boolean isSpaceInsensitve() {
		return flagSet.contains(SPACE_INSENSITVE);
	}

	public boolean isSpaceInsensitve2() {
		return flagSet.contains(SPACE_INSENSITVE2);
	}

}