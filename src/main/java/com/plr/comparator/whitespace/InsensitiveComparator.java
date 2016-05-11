package com.plr.comparator.whitespace;

import java.util.Comparator;

import com.google.common.base.CharMatcher;

public class InsensitiveComparator implements Comparator<CharSequence> {

	public static InsensitiveComparator onWhiteSpace() {
		BeginEndFlexibleComparator beginEndFlexibleComparator = BeginEndFlexibleComparator.SPACE_TRIM;
		CharMatcher ignoreOn = CharMatcher.WHITESPACE;
		return new InsensitiveComparator(beginEndFlexibleComparator, ignoreOn);
	}

	public static InsensitiveComparator onRepetition() {
		BeginEndFlexibleComparator beginEndFlexibleComparator = BeginEndFlexibleComparator.REPETITION_INSENSITIVE;
		CharMatcher ignoreOn = CharMatcher.WHITESPACE;
		return new InsensitiveComparator(beginEndFlexibleComparator, ignoreOn);
	}
	
	public static InsensitiveComparator onAllWhiteSpace() {
		BeginEndFlexibleComparator beginEndFlexibleComparator = BeginEndFlexibleComparator.CHARACTER_INSENSITIVE;
		CharMatcher ignoreOn = CharMatcher.WHITESPACE;
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
