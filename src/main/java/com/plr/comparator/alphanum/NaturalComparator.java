package com.plr.comparator.alphanum;

import java.text.Collator;
import java.util.ArrayList;

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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NaturalComparator implements Comparator<String> {

	private final Collator collator;

	public NaturalComparator() {
		collator = null;
	}

	public NaturalComparator(Collator collator) {
		this.collator = collator;
	}

	//final static String NUM_PAT = "(:?(?<!\\d)[-+])?\\d+(?:(?:\\.\\d++)(?!\\.\\d))?";
	final static String NUM_PAT = "(:?^[-+])?\\d+(?:(?:\\.\\d++)(?!\\.\\d))?";

	final static Pattern PATTERN = Pattern.compile(NUM_PAT);

	List<TokenComparable> split(String toSplit) {

		List<TokenComparable> list = new ArrayList<>();

		Matcher matcher = PATTERN.matcher(toSplit);

		int start = 0;
		while (matcher.find()) {
			if (start != matcher.start()) {
				String prev = toSplit.substring(start, matcher.start());
				list.add(new AlphaTokenComparable(prev));
			}

			String num = toSplit.substring(matcher.start(), matcher.end());
			list.add(new NumberTokenComparable(num));

			start = matcher.end();
		}

		if (start != toSplit.length()) {
			String last = toSplit.substring(start, toSplit.length());
			list.add(new AlphaTokenComparable(last));
		}

		return list;
	}

	public int compare(String s1, String s2) {

		List<TokenComparable> list1 = split(s1);
		List<TokenComparable> list2 = split(s2);

		int len1 = list1.size();
		int len2 = list2.size();
		int lim = Math.min(len1, len2);

		int k = 0;
		while (k < lim) {
			TokenComparable ss1 = list1.get(k);
			TokenComparable ss2 = list2.get(k);

			int result = ss1.compareTo(ss2);

			if (result != 0) {
				return result;
			}
			k++;
		}
		return len1 - len2;
	}
}