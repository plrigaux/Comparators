package com.plr.comparator.natural


import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName

class PLRTest {

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
		
		def trims = ["Doc5.doc " , " Doc5.doc ", " Doc5.doc"]

		displayComparingListElement(trims, NaturalComparator.primary().leftTrim())
		displayComparingListElement(trims, NaturalComparator.primary().rightTrim())
		displayComparingListElement(trims, NaturalComparator.primary().trim())
	}
	
	private displayComparingListElement(List list, NaturalComparator naturalComparator) {

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
