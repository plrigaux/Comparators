package com.plr.comparator.alphanum;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tokenizer implements Iterator<TokenComparable> {

	private static final Logger logger = LoggerFactory.getLogger(Tokenizer.class);

	final private NaturalComparator naturalComparator;

	final static String NUM_PAT = "(?:\\s)*(?:((?<=^|\\s)[-])?0*([1-9]\\d*|0))((\\.\\d++)(?!\\.\\d))?";

	final static Pattern pattern = Pattern.compile(NUM_PAT);

	private final CharSequence toSplit;
	private final CharSequence original;
	final private Matcher matcher;

	private int from = 0;
	private int to = -1;
	private int start = 0;

	private boolean matcherFind = false;

	Tokenizer(NaturalComparator naturalComparator, CharSequence toSplit) {
		this.naturalComparator = naturalComparator;

		this.original = toSplit;

		if (naturalComparator.isSpaceInsensitve()) {
			this.toSplit = spaceInsensible(toSplit);
		} else if (naturalComparator.isSpaceInsensitve2()) {
			this.toSplit = spaceInsensible2(toSplit);
		} else {
			this.toSplit = toSplit;
		}

		matcher = pattern.matcher(toSplit);

		trimMatcher();
	}

//	List<TokenComparable> split() {
//
//		List<TokenComparable> list = new ArrayList<>();
//
//		matcher.reset();
//		matcher.region(from, to);
//
//		int start = matcher.regionStart();
//
//		int j = 0;
//		while (matcher.find()) {
//
//			if (logger.isDebugEnabled()) {
//				logger.debug("toSplit:'{}'", toSplit);
//				for (int i = 0; i < matcher.groupCount(); i++) {
//					logger.debug("find {} Gr{}: '{}'", j, i, matcher.group(i));
//				}
//				j++;
//				logger.debug("");
//			}
//
//			if (start != matcher.start()) {
//				CharSequence prev = new StringBuilderSpecial2(toSplit, start, matcher.start());
//				list.add(new AlphaTokenComparable(prev, naturalComparator.getAlphaComparator()));
//			}
//
//			CharSequence wholeStr = new StringBuilderSpecial2(toSplit, matcher.start(0), matcher.end(0));
//			CharSequence number = new StringBuilderSpecial2(toSplit, matcher.start(2), matcher.end(2));
//
//			// TODO remove the group for neg
//			boolean isNegative = matcher.start(1) != -1;
//
//			// TODO try to find why there is a dot at front (+1)
//			CharSequence decimal = null;
//
//			int end3 = matcher.end(3);
//			if (end3 != -1) {
//				decimal = new StringBuilderSpecial2(toSplit, matcher.start(3) + 1, end3);
//			}
//
//			list.add(new NumberTokenComparable(isNegative, number, decimal, wholeStr,
//					naturalComparator.getAlphaComparator()));
//
//			start = matcher.end();
//		}
//
//		if (start != matcher.regionEnd()) {
//			CharSequence last = toSplit.subSequence(start, matcher.regionEnd());
//			list.add(new AlphaTokenComparable(last, naturalComparator.getAlphaComparator()));
//		}
//
//		return list;
//	}

	private int counter = 0;
	private boolean hasPrevious = false;

	TokenComparable getNext() {
		TokenComparable token = null;

		if (logger.isDebugEnabled()) {
			logger.debug("toSplit:'{}'", toSplit);
			for (int i = 0; i < matcher.groupCount(); i++) {
				logger.debug("find {} Gr{}: '{}'", counter, i, matcher.group(i));
			}
			logger.debug("");
		}

		if (!hasPrevious && !matcherFind) {
			matcherFind = matcher.find();
			hasPrevious = true;
		}

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
				token = new AlphaTokenComparable(toSplit, naturalComparator.getAlphaComparator());
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
		token = new AlphaTokenComparable(prev, naturalComparator.getAlphaComparator());
		counter++;
		return token;
	}

	private TokenComparable grabNumToken() {
		TokenComparable token;
		CharSequence wholeStr = new StringBuilderSpecial2(toSplit, matcher.start(0), matcher.end(0));
		CharSequence number = new StringBuilderSpecial2(toSplit, matcher.start(2), matcher.end(2));

		// TODO remove the group for neg
		boolean isNegative = matcher.start(1) != -1;

		// TODO try to find why there is a dot at front (+1)
		CharSequence decimal = null;

		int end3 = matcher.end(3);
		if (end3 != -1) {
			decimal = new StringBuilderSpecial2(toSplit, matcher.start(3) + 1, end3);
		}

		token = new NumberTokenComparable(isNegative, number, decimal, wholeStr,
				naturalComparator.getAlphaComparator());
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
		return sb;
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
		return sb;
	}

	public CharSequence getOriginal() {
		return original;
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
