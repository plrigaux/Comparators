package com.plr.comparator.whitespace;

import java.util.Comparator;

import com.google.common.base.CharMatcher;

public class InsensitiveComparator implements Comparator<CharSequence> {

	
	public static InsensitiveComparator on(CharMatcher ignoreOn) {
		BeginEndFlexibleComparator beginEndFlexibleComparator = BeginEndFlexibleComparator.SPACE_TRIM;
		return new InsensitiveComparator(beginEndFlexibleComparator, ignoreOn);
	}
	
	public static InsensitiveComparator onWhiteSpace() {
		return on(CharMatcher.WHITESPACE);
	}

	public static InsensitiveComparator onRepetitionWhiteSpace() {
		CharMatcher ignoreOn = CharMatcher.WHITESPACE;
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
		CharMatcher ignoreOn = CharMatcher.WHITESPACE;
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
		return new InsensitiveComparator(ignoreOn, ignoreOn, ignoreOn, replace, beginEndFlexibleComparator);
	}

	public InsensitiveComparator leftTrim() {
		return new InsensitiveComparator(ignoreOn, ignoreOn, rightTrimer, replace, beginEndFlexibleComparator);
	}

	public InsensitiveComparator rightTrim() {
		return new InsensitiveComparator(ignoreOn, leftTrimer, ignoreOn, replace, beginEndFlexibleComparator);
	}
	
	public InsensitiveComparator trim(CharMatcher trim) {
		return new InsensitiveComparator(ignoreOn, trim, trim, replace, beginEndFlexibleComparator);
	}

	public InsensitiveComparator leftTrim(CharMatcher trim) {
		return new InsensitiveComparator(ignoreOn, trim, rightTrimer, replace, beginEndFlexibleComparator);
	}

	public InsensitiveComparator rightTrim(CharMatcher trim) {
		return new InsensitiveComparator(ignoreOn, leftTrimer, trim, replace, beginEndFlexibleComparator);
	}

	final CharMatcher leftTrimer;
	final CharMatcher rightTrimer;
	final CharMatcher ignoreOn;
	final char replace;
	final private BeginEndFlexibleComparator beginEndFlexibleComparator;

	private InsensitiveComparator(BeginEndFlexibleComparator beginEndFlexibleComparator, CharMatcher ignoreOn) {
		this.leftTrimer = CharMatcher.NONE;
		this.rightTrimer = CharMatcher.NONE;
		this.ignoreOn = ignoreOn;
		this.replace = ' ';
		this.beginEndFlexibleComparator = beginEndFlexibleComparator;
	}

	private InsensitiveComparator(CharMatcher ignoreOn, CharMatcher leftTrimer, CharMatcher rightTrimer, char replace,
			BeginEndFlexibleComparator beginEndFlexibleComparator) {
		this.ignoreOn = ignoreOn;
		this.leftTrimer = leftTrimer;
		this.rightTrimer = rightTrimer;
		this.replace = replace;
		this.beginEndFlexibleComparator = beginEndFlexibleComparator;
	}

	@Override
	public int compare(CharSequence s1, CharSequence s2) {
		return beginEndFlexibleComparator.compare(s1, s2, this);
	}

}
