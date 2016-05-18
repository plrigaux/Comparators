package com.plr.comparator.insensitive;

import java.util.Comparator;

import com.google.common.base.CharMatcher;

public class InsensitiveComparator implements Comparator<CharSequence> {

	public static InsensitiveComparator trimOn(CharMatcher ignoreOn) {
		BeginEndFlexibleComparator beginEndFlexibleComparator = BeginEndFlexibleComparator.SPACE_TRIM;
		return new InsensitiveComparator(beginEndFlexibleComparator, ignoreOn).trim();
	}

	public static InsensitiveComparator trimOnWhiteSpace() {
		return trimOn(CharMatcher.whitespace());
	}

	public static InsensitiveComparator onRepetitionWhiteSpace() {
		CharMatcher ignoreOn = CharMatcher.whitespace();
		return onRepetition(ignoreOn);
	}

	public static InsensitiveComparator onRepetition(char ignoreOn) {
		return onRepetition(CharMatcher.is(ignoreOn));
	}

	public static InsensitiveComparator onRepetition(CharMatcher ignoreOn) {
		BeginEndFlexibleComparator beginEndFlexibleComparator = BeginEndFlexibleComparator.REPETITION_INSENSITIVE;
		return new InsensitiveComparator(beginEndFlexibleComparator, ignoreOn);
	}

	public static InsensitiveComparator onAllWhiteSpace() {
		CharMatcher ignoreOn = CharMatcher.whitespace();
		return onAll(ignoreOn);
	}

	public static InsensitiveComparator onAll(char ignoreOn) {
		return onAll(CharMatcher.is(ignoreOn));
	}

	public static InsensitiveComparator onAll(CharMatcher ignoreOn) {
		BeginEndFlexibleComparator beginEndFlexibleComparator = BeginEndFlexibleComparator.CHARACTER_INSENSITIVE;
		return new InsensitiveComparator(beginEndFlexibleComparator, ignoreOn);
	}

	public InsensitiveComparator trim() {
		return new InsensitiveComparator(ignoreOn, ignoreOn, ignoreOn, replace, beginEndFlexibleComparator,
				characterComparisonStrategy);
	}

	public InsensitiveComparator leftTrim() {
		return new InsensitiveComparator(ignoreOn, ignoreOn, rightTrimer, replace, beginEndFlexibleComparator,
				characterComparisonStrategy);
	}

	public InsensitiveComparator rightTrim() {
		return new InsensitiveComparator(ignoreOn, leftTrimer, ignoreOn, replace, beginEndFlexibleComparator,
				characterComparisonStrategy);
	}

	public InsensitiveComparator trim(CharMatcher trim) {
		return new InsensitiveComparator(ignoreOn, trim, trim, replace, beginEndFlexibleComparator,
				characterComparisonStrategy);
	}

	public InsensitiveComparator leftTrim(CharMatcher trim) {
		return new InsensitiveComparator(ignoreOn, trim, rightTrimer, replace, beginEndFlexibleComparator,
				characterComparisonStrategy);
	}

	public InsensitiveComparator rightTrim(CharMatcher trim) {
		return new InsensitiveComparator(ignoreOn, leftTrimer, trim, replace, beginEndFlexibleComparator,
				characterComparisonStrategy);
	}

	public InsensitiveComparator ignoreCase() {
		return new InsensitiveComparator(ignoreOn, leftTrimer, rightTrimer, replace, beginEndFlexibleComparator,
				CharacterComparatorIgnoreCase.getInstance());
	}

	final CharMatcher leftTrimer;
	final CharMatcher rightTrimer;
	final CharMatcher ignoreOn;
	final char replace;
	final private BeginEndFlexibleComparator beginEndFlexibleComparator;
	final CharacterComparator characterComparisonStrategy;

	private InsensitiveComparator(BeginEndFlexibleComparator beginEndFlexibleComparator, CharMatcher ignoreOn) {
		this.leftTrimer = CharMatcher.NONE;
		this.rightTrimer = CharMatcher.NONE;
		this.ignoreOn = ignoreOn;
		this.replace = ' ';
		this.beginEndFlexibleComparator = beginEndFlexibleComparator;
		this.characterComparisonStrategy = CharacterComparatorAscii.getInstance();
	}

	private InsensitiveComparator(CharMatcher ignoreOn, CharMatcher leftTrimer, CharMatcher rightTrimer, char replace,
			BeginEndFlexibleComparator beginEndFlexibleComparator,
			CharacterComparator characterComparisonStrategy) {
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
