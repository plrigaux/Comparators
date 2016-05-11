/*
Copyright 2016 Pierre-Luc Rigaux <plrigaux@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.plr.comparator.natural;

import static com.plr.comparator.natural.NaturalComparator.Flags.LTRIM;
import static com.plr.comparator.natural.NaturalComparator.Flags.PRIMARY;
import static com.plr.comparator.natural.NaturalComparator.Flags.RTRIM;
import static com.plr.comparator.natural.NaturalComparator.Flags.SPACE_INSENSITVE;
import static com.plr.comparator.natural.NaturalComparator.Flags.SPACE_REPETITION_INSENSITVE;
import static com.plr.comparator.natural.NaturalComparator.Flags.TRIM;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;

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
		 * Look only at the number numeric value. Treat leading and tailing
		 * zeros as non significant.
		 */
		PRIMARY, SECONDARY,

		/** Ignore white spaces at the beginning the string */
		LTRIM,

		/** Ignore white spaces at the end of the string */
		RTRIM,

		/** Ignore white spaces at the begining and the end of the string */
		TRIM, 
		
		/** Ignore string white spaces */
		SPACE_INSENSITVE, 
		
		/** Ignore string white spaces repetition */
		SPACE_REPETITION_INSENSITVE,

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
		
//		if (pureNumbers) {
			regex.append("\\s*");
//		}

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

//		if (pureNumbers) {
//			regex.append("\\s*");
//		}
		
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

//				result = ss1.compareLeadingZerosTo(ss2);
				
				result = ss1.size() - ss2.size();


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
		return flagSet.contains(SPACE_REPETITION_INSENSITVE);
	}

	public boolean isRationalNumber() {
		return flagSet.contains(Flags.RATIONAL_NUMBER) || flagSet.contains(Flags.REAL_NUMBER);
	}

	public boolean isNegativeNumber() {
		return flagSet.contains(Flags.NEGATIVE_NUMBER) || flagSet.contains(Flags.REAL_NUMBER);
	}

}