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

	final private Comparator<String> comparator;

	//TODO make the regex not capture the point 
	final static String NUM_PAT = "(?:\\s)*((?:^|\\s)[-])?0*([1-9]\\d*|0)((\\.\\d++)(?!\\.\\d))?";
	
	final static public Comparator<String> ASCII = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
	};

	public NaturalComparator() {
		comparator = ASCII;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public NaturalComparator(Collator collator) {
		this.comparator = (Comparator) collator;
	}

	public NaturalComparator(Comparator<String> collator) {
		this.comparator = collator;
	}

	final static Pattern PATTERN = Pattern.compile(NUM_PAT);

	List<TokenComparable> split(String toSplit) {

		List<TokenComparable> list = new ArrayList<>();

		Matcher matcher = PATTERN.matcher(toSplit);

		int start = 0;
		while (matcher.find()) {
			if (start != matcher.start()) {
				String prev = toSplit.substring(start, matcher.start());
				list.add(new AlphaTokenComparable(prev, comparator));
			}

//			for (int i = 0; i < matcher.groupCount(); i++) {
//				System.out.println("Gr" + i + ": " + matcher.group(i));
//			}

			boolean isNegative = "-".equals(matcher.group(1));
			String number = matcher.group(2);
			String decimal = matcher.group(3);
			String wholeStr = matcher.group(0);

			//TODO make the regex not capture the point 
			if (decimal != null) {
				decimal = decimal.substring(1);
			}
			
			list.add(new NumberTokenComparable(isNegative, number, decimal, wholeStr, comparator));
			
			start = matcher.end();
		}

		if (start != toSplit.length()) {
			String last = toSplit.substring(start, toSplit.length());
			list.add(new AlphaTokenComparable(last, comparator));
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
		int result;
		while (k < lim) {
			TokenComparable ss1 = list1.get(k);
			TokenComparable ss2 = list2.get(k);

			result = ss1.compareTo(ss2);

			if (result != 0) {
				return result;
			}
			k++;
		}
		
		result =  len1 - len2;
		
		if (result == 0) {
			return s1.compareTo(s2);
		}
		
		return result;
	}
}