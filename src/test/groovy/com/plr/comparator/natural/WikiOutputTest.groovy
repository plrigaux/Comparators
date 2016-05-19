package com.plr.comparator.natural


import java.text.Collator;

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName

class WikiOutputTest {

	@Rule
	public TestName testName = new TestName();

	@Before
	public void before() {
		println "Doing test: " + testName.getMethodName();
	}

	def l1 = [
		"Doc 5.doc",
		"Doc5.doc",
		"Doc05.doc",
		"Doc05.2.doc",
		"Doc05.02.doc"
	]

	@Test
	public void testPrimary() {

		NaturalComparator naturalComparator = NaturalComparator.primary();

		displayComparingListElement(l1, naturalComparator)
	}

	@Test
	public void testSecondary() {

		NaturalComparator naturalComparator = NaturalComparator.secondary();

		displayComparingListElement(l1, naturalComparator)
	}


	def numbers = [
		"-123",
		"-12",
		"-1.1532456",
		"-1.15",
		"-1.1" ,
		"-1",
		"-0.99999999999",
		"-0.99",
		"-0.15",
		"-0.1",
		"0",
		"0.1",
		"0.15",
		"1",
		"1.1",
		"1.15",
		"11.1",
		"11.67",
		"11.6756745674",
		"11.68",
		"12",
		"123"
	]

	def rationalNumbers = [
		"0.3" ,
		"0.33" ,
		"3.456456345634563423467" ,
		"10"
	]



	@Test
	public void testNumber() {

		displayComparingListElement(numbers, NaturalComparator.primary())
		displayComparingListElement(numbers, NaturalComparator.secondary())
		displayComparingListElement(numbers, NaturalComparator.primary().negative())
		displayComparingListElement(numbers, NaturalComparator.primary().decimal())
		displayComparingListElement(numbers, NaturalComparator.primary().real())
		displayComparingListElement(numbers, NaturalComparator.secondary().real())
	}

	@Test
	public void testRationalNumber() {

		displayComparingListElement(rationalNumbers, NaturalComparator.primary().decimal())
	}


	List<String> negativesNumbers = [
		"-456" ,
		"-43" ,
		"3",
		"-3",
		"test -467",
		"test 467"
	]
	@Test
	public void testNegativeNumber() {

		Comparator<CharSequence> comp =  NaturalComparator.primary().negative()

		displayComparingListElement(negativesNumbers, comp)
	}

	@Test
	public void testRealNumber() {

		displayComparingListElement(rationalNumbers, NaturalComparator.primary().real())
	}

	@Test
	public void testTrims() {

		def trims = [
			"Doc5.doc " ,
			" Doc5.doc ",
			" Doc5.doc"
		]

		displayComparingListElement(trims, NaturalComparator.primary().leftTrim())
		displayComparingListElement(trims, NaturalComparator.primary().rightTrim())
		displayComparingListElement(trims, NaturalComparator.primary().trim())
	}

	@Test
	public void testIgnoreCase() {

		def trims = [
			"Albert Einstein" ,
			"Max Planck",
			"albert einstein"
		]

		displayComparingListElement(trims, NaturalComparator.primary().ignoreCase())
	}

	def localWords = [
		"peach",
		"pêche",
		"péché",
		"sin"
	]

	@Test
	public void testCollatorFR() {



		Collator collator = Collator.getInstance(new Locale("fr","FR"));


		Comparator<CharSequence> comp = NaturalComparator.primary().collator(collator);

		displayComparingListElement(localWords, comp)
	}
	
	@Test
	public void testCollatorEN() {
		Collator collator = Collator.getInstance(new Locale("en","EN"));
		
		Comparator<CharSequence> comp = NaturalComparator.primary().collator(collator);

		displayComparingListElement(localWords, comp)
	}
	
	@Test
	public void testCustomComparator() {
		def words = ["Doc1", "Doc2", "Doc3.doc", "Alpha1", "Beta2", "1Number"]
		
		Comparator<CharSequence> compText = new Comparator<CharSequence>() {
			public int compare(CharSequence s1, CharSequence s2) {
				return 0;
			}
		};
		
		Comparator<CharSequence> comp = NaturalComparator.primary().comparator(compText);

		displayComparingListElement(words, comp)
	}


	public static displayComparingListElement(List list, Comparator<CharSequence> naturalComparator) {

		assert list.size() > 0

		Collections.sort(list, naturalComparator)

		def a = list[0]

		print "\"$a\""
		for (int j = 1; j < list.size(); j++) {

			def b = list[j]

			int res = naturalComparator.compare(a,b)

			def comp = res < 0 ? "<" : res > 0 ? ">" : "=="

			print " $comp \"$b\""

			a = b
		}

		println ""
	}
}
