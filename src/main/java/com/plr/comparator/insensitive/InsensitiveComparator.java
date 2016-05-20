package com.plr.comparator.insensitive;

import java.util.Comparator;

import com.google.common.base.CharMatcher;

public class InsensitiveComparator implements Comparator<CharSequence> {

	final CharMatcher leftTrimer;
	final CharMatcher rightTrimer;
	final CharMatcher ignoreOn;
	final char replace;
	final private BeginEndFlexibleComparator beginEndFlexibleComparator;
	final CharacterComparator characterComparisonStrategy;

	public static InsensitiveComparator onNothing() {
		BeginEndFlexibleComparator beginEndFlexibleComparator = BeginEndFlexibleComparator.TRIMMABLE;
		return new InsensitiveComparator(beginEndFlexibleComparator, null);
	}

	public static InsensitiveComparator onWhiteSpaceRepetition() {
		CharMatcher ignoreOn = CharMatcher.whitespace();
		return onRepetition(ignoreOn);
	}

	public static InsensitiveComparator onRepetition(char toIgnore) {
		CharMatcher ignoreOn = CharMatcher.is(toIgnore);
		return onRepetition(ignoreOn);
	}

	public static InsensitiveComparator onRepetition(CharMatcher ignoreOn) {
		BeginEndFlexibleComparator beginEndFlexibleComparator = BeginEndFlexibleComparator.REPETITION_INSENSITIVE;
		return new InsensitiveComparator(beginEndFlexibleComparator, ignoreOn);
	}

	public static InsensitiveComparator onAllWhiteSpace() {
		CharMatcher ignoreOn = CharMatcher.whitespace();
		return onAll(ignoreOn);
	}

	public static InsensitiveComparator onAll(char toIgnore) {
		return onAll(CharMatcher.is(toIgnore));
	}

	public static InsensitiveComparator onAll(CharMatcher toIgnore) {
		BeginEndFlexibleComparator beginEndFlexibleComparator = BeginEndFlexibleComparator.CHARACTER_INSENSITIVE;
		return new InsensitiveComparator(beginEndFlexibleComparator, toIgnore);
	}

	public InsensitiveComparator trim() {
		CharMatcher trimOn = CharMatcher.whitespace();
		return trim(trimOn);
	}

	public InsensitiveComparator leftTrim() {
		CharMatcher trimOn = CharMatcher.whitespace();
		return leftTrim(trimOn);
	}

	public InsensitiveComparator rightTrim() {
		CharMatcher trimOn = CharMatcher.whitespace();
		return rightTrim(trimOn);
	}

	public InsensitiveComparator trim(CharMatcher trimOn) {
		return new InsensitiveComparator(ignoreOn, trimOn, trimOn, replace, beginEndFlexibleComparator,
				characterComparisonStrategy);
	}

	public InsensitiveComparator leftTrim(CharMatcher trimOn) {
		return new InsensitiveComparator(ignoreOn, trimOn, rightTrimer, replace, beginEndFlexibleComparator,
				characterComparisonStrategy);
	}

	public InsensitiveComparator rightTrim(CharMatcher trimOn) {
		return new InsensitiveComparator(ignoreOn, leftTrimer, trimOn, replace, beginEndFlexibleComparator,
				characterComparisonStrategy);
	}

	public InsensitiveComparator ignoreCase() {

		return new InsensitiveComparator(ignoreOn, leftTrimer, rightTrimer, replace, beginEndFlexibleComparator,
				CharacterComparatorIgnoreCase.getInstance());
	}

	private InsensitiveComparator(BeginEndFlexibleComparator beginEndFlexibleComparator) {
		this(beginEndFlexibleComparator, CharMatcher.NONE);
	}

	private InsensitiveComparator(BeginEndFlexibleComparator beginEndFlexibleComparator, CharMatcher ignoreOn) {
		this.leftTrimer = CharMatcher.NONE;
		this.rightTrimer = CharMatcher.NONE;
		this.ignoreOn = ignoreOn;
		this.replace = ' ';
		this.beginEndFlexibleComparator = beginEndFlexibleComparator;
		this.characterComparisonStrategy = CharacterComparatorAscii.getInstance();
	}

	private InsensitiveComparator(CharMatcher ignoreOn, CharMatcher leftTrimer, CharMatcher rightTrimer, char replace,
			BeginEndFlexibleComparator beginEndFlexibleComparator, CharacterComparator characterComparisonStrategy) {
		this.ignoreOn = ignoreOn;
		this.leftTrimer = leftTrimer;
		this.rightTrimer = rightTrimer;
		this.replace = replace;
		this.beginEndFlexibleComparator = beginEndFlexibleComparator;
		this.characterComparisonStrategy = characterComparisonStrategy;
	}

	@Override
	public int compare(CharSequence s1, CharSequence s2) {
		return beginEndFlexibleComparator.compare(s1, s2, this);
	}

}
