package com.plr.comparator.alphanum;

import java.util.Iterator;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tokenizer implements Iterator<TokenComparable> {

	private static final Logger logger = LoggerFactory.getLogger(Tokenizer.class);

	final private NaturalComparator naturalComparator;

	private final CharSequence toSplit;
	final private Matcher matcher;

	private int from = 0;
	private int to = -1;
	private int start = 0;

	private boolean matcherFind = false;

	Tokenizer(NaturalComparator naturalComparator, CharSequence toSplit) {
		this.naturalComparator = naturalComparator;
		this.toSplit = toSplit;

		matcher = naturalComparator.pattern.matcher(toSplit);

		trimMatcher();
	}

	private int counter = 0;
	private boolean hasPrevious = false;

	TokenComparable getNext() {
		if (!hasPrevious && !matcherFind) {
			matcherFind = matcher.find();

			if (logger.isDebugEnabled() && matcherFind) {
				logger.debug("toSplit:'{}'", toSplit);
				for (int i = 0; i < matcher.groupCount(); i++) {
					logger.debug("find {} Gr{}: '{}'", counter, i, matcher.group(i));
				}
				counter++;
				logger.debug("");
			}

			hasPrevious = true;
		}

		TokenComparable token = null;

		if (matcherFind) {
			if (hasPrevious) {
				if (start != matcher.start()) {
					token = grabAlphaToken(start, matcher.start());
				} else {
					token = grabNumToken();
				}

				hasPrevious = false;
			} else {
				token = grabNumToken();
			}
		} else {
			// No match at all
			if (from == start) {
				token = new AlphaTokenComparable(toSplit, naturalComparator);
				counter++;
				start = to;
			}
			// The last part
			else if (start != to) {
				token = grabAlphaToken(start, to);
				start = to;
			}
		}

		return token;
	}

	private TokenComparable grabAlphaToken(int beg, int end) {
		CharSequence prev = new StringBuilderSpecial2(toSplit, beg, end);
		return grabAlphaToken(prev);
	}

	private TokenComparable grabAlphaToken(CharSequence prev) {
		TokenComparable token;
		token = new AlphaTokenComparable(prev, naturalComparator);
		counter++;
		return token;
	}

	private TokenComparable grabNumToken() {
		TokenComparable token = null;

		CharSequence wholeStr = new StringBuilderSpecial2(toSplit, matcher.start(0), matcher.end(0));

		// TODO remove the group for neg
		boolean isNegative = false;
		int groupIndex = 1;

		if (naturalComparator.isNegativeNumber()) {
			isNegative = matcher.start(groupIndex++) != -1;
		}

		CharSequence number = new StringBuilderSpecial2(toSplit, matcher.start(groupIndex), matcher.end(groupIndex));

		groupIndex++;
		// TODO try to find why there is a dot at front (+1)
		CharSequence decimal = null;

		if (matcher.groupCount() >= groupIndex) {
			int end3 = matcher.end(groupIndex);
			if (end3 != -1) {
				decimal = new StringBuilderSpecial2(toSplit, matcher.start(3) + 1, end3);
			}
		}

		token = new NumberTokenComparable(isNegative, number, decimal, wholeStr, naturalComparator);

		counter++;
		start = matcher.end();
		matcherFind = false;

		return token;
	}

	private void trimMatcher() {

		if (naturalComparator.isTrim() || naturalComparator.isLTrim()) {
			from = _ltrim(toSplit);
		}

		to = toSplit.length();
		if (naturalComparator.isTrim() || naturalComparator.isRTrim()) {
			to = _rtrim(toSplit);
		}

		matcher.region(from, to);

		start = from;
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

	TokenComparable tokenComparableTemp = null;

	@Override
	public boolean hasNext() {
		if (tokenComparableTemp != null) {
			return true;
		} else {
			tokenComparableTemp = getNext();
		}
		return tokenComparableTemp != null;
	}

	@Override
	public TokenComparable next() {

		if (tokenComparableTemp != null) {
			TokenComparable tmp = tokenComparableTemp;
			tokenComparableTemp = null;
			return tmp;
		} else {
			return getNext();
		}
	}
}
