package com.plr.comparator.natural;

import static com.plr.comparator.natural.NaturalComparator.Flags.LTRIM;
import static com.plr.comparator.natural.NaturalComparator.Flags.PRIMARY;
import static com.plr.comparator.natural.NaturalComparator.Flags.RTRIM;
import static com.plr.comparator.natural.NaturalComparator.Flags.SPACE_INSENSITVE;
import static com.plr.comparator.natural.NaturalComparator.Flags.SPACE_INSENSITVE2;
import static com.plr.comparator.natural.NaturalComparator.Flags.TRIM;

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
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.plr.comparator.AsciiComparator;
import com.plr.comparator.CaseInsensitiveComparator;

public final class NaturalComparator implements Comparator<CharSequence> {

	private static final Logger logger = LoggerFactory.getLogger(NaturalComparator.class);

	final private Comparator<CharSequence> alphaComparator;

	final static public Comparator<CharSequence> ASCII = AsciiComparator.getInstance();
	final static public Comparator<CharSequence> CASE_INSENSITIVE = CaseInsensitiveComparator.getInstance();

	public enum Flags {
		/**
		 * Look only at the number numeric value. Treat leading and tailling
		 * zeros as non sinificant.
		 */
		PRIMARY, SECONDARY,

		/** Ignore white spaces at the begining the string */
		LTRIM,

		/** Ignore white spaces at the end of the string */
		RTRIM,

		/** Ignore white spaces at the begining and the end of the string */
		TRIM, SPACE_INSENSITVE, SPACE_INSENSITVE2,

		/** Combination of the NEGATIVE_NUMBER and RATIONAL_NUMBER flag */
		REAL_NUMBER,

		/** Treat the hyphen before the number as negative. */
		NEGATIVE_NUMBER,

		/** Handle the portion after the dot '.' as decimal. */
		RATIONAL_NUMBER
	};

	private boolean pureNumbers;

	private final EnumSet<NaturalComparator.Flags> flagSet = EnumSet.noneOf(Flags.class);

	// final private static String NUM_PAT =
	// "(?:\\s)*(?:((?<=^|\\s)[-])?0*([1-9]\\d*|0))((\\.\\d++)(?!\\.\\d))?";

	final private static String NEG_REGEX = "(?:((?<=^|\\s)[-])?";
	final private static String DEC_REGEX = "((\\.\\d++)(?!\\.\\d))?";
	final private static String NUM_REGEX = "0*([1-9]\\d*|0)";

	final Pattern pattern;

	private NaturalComparator() {
		// Throw an exception if this ever *is* called
		throw new AssertionError("Instantiating utility class.");
	}

	private NaturalComparator(Comparator<CharSequence> alphaComparator, Flags... flags) {
		this.alphaComparator = alphaComparator;

		flagSet.addAll(Arrays.asList(flags));

		if (flagSet.contains(Flags.SECONDARY)) {
			flagSet.remove(PRIMARY);
		} else {
			flagSet.add(PRIMARY);
		}

		pureNumbers = flagSet.contains(Flags.PRIMARY);

		StringBuilder regex = new StringBuilder(200);

		if (isNegativeNumber()) {
			regex.append(NEG_REGEX);
		}

		regex.append(NUM_REGEX);

		if (isNegativeNumber()) {
			regex.append(")");
		}

		if (isRationalNumber()) {
			regex.append(DEC_REGEX);
		}

		logger.debug("Regex: \"{}\"", regex);

		pattern = Pattern.compile(regex.toString());
	}

	public static NaturalComparator getComparator(Flags... flags) {
		return getComparator(ASCII, flags);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static NaturalComparator getComparator(Collator collator, Flags... flags) {
		return getComparator((Comparator) collator, flags);
	}

	public static NaturalComparator getComparator(Comparator<CharSequence> alphaComparator, Flags... flags) {
		return new NaturalComparator(alphaComparator, flags);
	}

	public int compare(CharSequence s1, CharSequence s2) {

		logger.debug("s1: '{}' s2: '{}'", s1, s2);

		Tokenizer splitter1 = new Tokenizer(this, s1);
		Tokenizer splitter2 = new Tokenizer(this, s2);

		List<TokenComparable> list = new ArrayList<>();

		int result = 0;

		while (splitter1.hasNext() && splitter2.hasNext()) {
			TokenComparable ss1 = splitter1.next();
			TokenComparable ss2 = splitter2.next();

			list.add(ss1);
			list.add(ss2);

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
			int lim = list.size();
			while (k < lim) {
				TokenComparable ss1 = list.get(k++);
				TokenComparable ss2 = list.get(k++);

				result = ss1.compareLeadingZerosTo(ss2);

				if (result != 0) {
					return result;
				}
			}

			if (result == 0 && !isSpaceInsensitve() && !isSpaceCollapseInsensitve()) {
				result = alphaComparator.compare(s1, s2);
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
		return flagSet.contains(TRIM) || flagSet.contains(PRIMARY);
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

	public boolean isSpaceCollapseInsensitve() {
		return flagSet.contains(SPACE_INSENSITVE2);
	}

	public boolean isRationalNumber() {
		return flagSet.contains(Flags.RATIONAL_NUMBER) || flagSet.contains(Flags.REAL_NUMBER);
	}

	public boolean isNegativeNumber() {
		return flagSet.contains(Flags.NEGATIVE_NUMBER) || flagSet.contains(Flags.REAL_NUMBER);
	}

}