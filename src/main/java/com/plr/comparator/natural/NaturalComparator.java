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

import static com.plr.comparator.natural.NaturalComparator.Flags.*;

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
import com.plr.comparator.insensitive.InsensitiveComparator;

public final class NaturalComparator implements Comparator<CharSequence> {

	private static final Logger logger = LoggerFactory.getLogger(NaturalComparator.class);

	final private Comparator<CharSequence> alphaComparator;
	final private boolean isHandlingDecimal;
	final private boolean isNegativeNumber;
	final private boolean isPrimary;
	final private boolean isRTrim;
	final private boolean isLTrim;
	final private boolean isSpaceInsensitve;
	final private boolean isSpaceCollapseInsensitve;
	final private boolean isNoNumberHeadingSpace;

	final private static String NEG_REGEX = "(?:((?<=^|\\s)[-])?";
	final private static String DEC_REGEX = "((\\.\\d++)(?!\\.\\d))?";
	final private static String NUM_REGEX = "0*([1-9]\\d*|0)";

	final Pattern pattern;

	enum Flags {
		PRIMARY, SECONDARY, LTRIM, RTRIM, TRIM, SPACE_INSENSITVE, SPACE_REPETITION_INSENSITVE, REAL_NUMBER, NEGATIVE, DECIMAL, NO_NUMBER_HEADING_SPACE
	};

	private final EnumSet<NaturalComparator.Flags> flagSet = EnumSet.noneOf(Flags.class);

	private NaturalComparator() {
		// Throw an exception if this ever *is* called
		throw new AssertionError("Instantiating utility class.");
	}

	public NaturalComparator caseInsensitive() {

		Comparator<CharSequence> comp;

		if (alphaComparator instanceof InsensitiveComparator) {
			InsensitiveComparator insensitiveComparator = (InsensitiveComparator) alphaComparator;

			comp = insensitiveComparator.ignoreCase();
		} else {
			comp = CaseInsensitiveComparator.getInstance();
		}

		return alphaComparator(comp);
	}

	public NaturalComparator ascii() {
		return alphaComparator(AsciiComparator.getInstance());
	}

	public NaturalComparator alphaComparator(Comparator<CharSequence> alphaComparator) {
		return new NaturalComparator(alphaComparator, flagSet.toArray(new Flags[flagSet.size()]));
	}

	public NaturalComparator alphaCollator(Collator alphaComparator) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Comparator<CharSequence> comp = (Comparator) alphaComparator;
		return alphaComparator(comp);
	}

	public NaturalComparator real() {
		flagSet.add(Flags.REAL_NUMBER);
		return new NaturalComparator(alphaComparator, flagSet.toArray(new Flags[flagSet.size()]));
	}

	public NaturalComparator trim() {
		flagSet.add(Flags.TRIM);
		return new NaturalComparator(alphaComparator, flagSet.toArray(new Flags[flagSet.size()]));
	}
	
	public NaturalComparator leftTrim() {
		flagSet.add(Flags.LTRIM);
		return new NaturalComparator(alphaComparator, flagSet.toArray(new Flags[flagSet.size()]));
	}
	
	public NaturalComparator rightTrim() {
		flagSet.add(Flags.RTRIM);
		return new NaturalComparator(alphaComparator, flagSet.toArray(new Flags[flagSet.size()]));
	}

	public NaturalComparator decimal() {
		flagSet.add(Flags.DECIMAL);
		return new NaturalComparator(alphaComparator, flagSet.toArray(new Flags[flagSet.size()]));
	}

	public NaturalComparator negative() {
		flagSet.add(Flags.NEGATIVE);
		return new NaturalComparator(alphaComparator, flagSet.toArray(new Flags[flagSet.size()]));
	}

	public NaturalComparator noNumberHeadingSpace() {
		flagSet.add(Flags.NO_NUMBER_HEADING_SPACE);
		return new NaturalComparator(alphaComparator, flagSet.toArray(new Flags[flagSet.size()]));
	}

	/**
	 * Provide a comparator where only <b>primary</b> differences are considered
	 * significant during comparison.
	 * 
	 * @return NaturalComparator
	 */
	public static NaturalComparator primary() {
		return new NaturalComparator(AsciiComparator.getInstance(), PRIMARY);
	}

	/**
	 * Provide a comparator where only <b>secondary</b> differences and
	 * <u>above</u> are considered significant during comparison.
	 * 
	 * @return NaturalComparator
	 */
	public static NaturalComparator secondary() {
		return new NaturalComparator(AsciiComparator.getInstance(), SECONDARY);
	}

	public NaturalComparator whiteSpaceInsensitive() {

		InsensitiveComparator comparator = InsensitiveComparator.onAllWhiteSpace();

		if (alphaComparator instanceof CaseInsensitiveComparator) {
			comparator = comparator.ignoreCase();
		}

		flagSet.add(Flags.SPACE_INSENSITVE);
		return new NaturalComparator(comparator, flagSet.toArray(new Flags[flagSet.size()]));
	}

	public NaturalComparator whiteSpaceRepetitionInsensitive() {

		InsensitiveComparator comparator = InsensitiveComparator.onRepetitionWhiteSpace();

		if (alphaComparator instanceof CaseInsensitiveComparator) {
			comparator = comparator.ignoreCase();
		}

		flagSet.add(Flags.SPACE_REPETITION_INSENSITVE);
		return new NaturalComparator(comparator, flagSet.toArray(new Flags[flagSet.size()]));
	}

	private NaturalComparator(Comparator<CharSequence> alphaComparator, Flags... flags) {
		this.alphaComparator = alphaComparator;

		flagSet.addAll(Arrays.asList(flags));

		isPrimary = flagSet.contains(Flags.PRIMARY);
		isHandlingDecimal = flagSet.contains(Flags.DECIMAL) || flagSet.contains(Flags.REAL_NUMBER);
		isNegativeNumber = flagSet.contains(Flags.NEGATIVE) || flagSet.contains(Flags.REAL_NUMBER);
		isLTrim = flagSet.contains(TRIM) || flagSet.contains(LTRIM);
		isRTrim = flagSet.contains(TRIM) || flagSet.contains(RTRIM);
		isSpaceCollapseInsensitve = flagSet.contains(SPACE_REPETITION_INSENSITVE);
		isSpaceInsensitve = flagSet.contains(SPACE_INSENSITVE);
		isNoNumberHeadingSpace = flagSet.contains(NO_NUMBER_HEADING_SPACE);

		pattern = buildRegEx();
	}

	private Pattern buildRegEx() {
		StringBuilder regex = new StringBuilder(200);

		if (!isNoNumberHeadingSpace) {
			regex.append("\\s*");
		}

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

		return Pattern.compile(regex.toString());
	}

	public int compare(CharSequence s1, CharSequence s2) {

		logger.debug("s1: '{}' s2: '{}'", s1, s2);

		Tokenizer splitter1 = new Tokenizer(this, s1);
		Tokenizer splitter2 = new Tokenizer(this, s2);

		List<TokenComparable> list = isPrimary ? null : new ArrayList<>();

		int result = 0;

		while (splitter1.hasNext() && splitter2.hasNext()) {
			TokenComparable ss1 = splitter1.next();
			TokenComparable ss2 = splitter2.next();

			if (!isPrimary) {
				list.add(ss1);
				list.add(ss2);
			}

			result = ss1.compareTo(ss2);
			
			logger.debug("ss1: {} ss2: {} result:{}", ss1, ss2, result);

			if (result != 0) {
				return result;
			}
		}

		boolean oneGreater = splitter1.hasNext() ^ splitter2.hasNext();

		if (oneGreater) {
			return splitter1.hasNext() ? 1 : -1;
		}

		if (!isPrimary) {

			// For now equals, now compare leading zeros or spaces
			int k = 0;
			int lim = list.size();
			while (k < lim) {
				TokenComparable ss1 = list.get(k++);
				TokenComparable ss2 = list.get(k++);

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
	
	class PrimaryStrategy {
		
	}

	public boolean equals(String s1, String s2) {
		return compare(s1, s2) == 0;
	}

	public Comparator<CharSequence> getAlphaComparator() {
		return alphaComparator;
	}

	public boolean isTrim() {
		return isRTrim & isLTrim;
	}

	public boolean isRTrim() {
		return isRTrim;
	}

	public boolean isLTrim() {
		return isLTrim;
	}

	public boolean isSpaceInsensitve() {
		return isSpaceInsensitve;
	}

	public boolean isSpaceCollapseInsensitve() {
		return isSpaceCollapseInsensitve;
	}

	public boolean isRationalNumber() {
		return isHandlingDecimal;
	}

	public boolean isNegativeNumber() {
		return isNegativeNumber;
	}

}