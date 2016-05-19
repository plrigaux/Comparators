package com.plr.comparator.insensitive


import java.text.Collator;

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName

import com.plr.comparator.natural.WikiOutputTest;

class InsensitiveWikiOutputTest {

	@Rule
	public TestName testName = new TestName();

	@Before
	public void before() {
		println "Doing test: " + testName.getMethodName();
	}

	List l1 = [
		"Doc  5.doc",
		"Doc 5.doc",
		"Doc5.doc",
		"abc",
		"a b c",
		"a  b   c",
		" a b c "
	]

	@Test
	public void testOnAllWhiteSpace() {

		InsensitiveComparator insensitiveComparator = InsensitiveComparator.onAllWhiteSpace();

		WikiOutputTest.displayComparingListElement(l1, insensitiveComparator)
	}
	
	@Test
	public void testOnAllWhiteSpaced() {

		InsensitiveComparator insensitiveComparator = InsensitiveComparator.onRepetitionWhiteSpace();

		WikiOutputTest.displayComparingListElement(l1, insensitiveComparator)
	}
	
	@Test
	public void testIgnoreCase() {
		def words = [
			"Albert Einstein" ,
			"Max Planck",
			"albert einstein"
		]
		
		InsensitiveComparator insensitiveComparator = InsensitiveComparator.ignoreCase();

		WikiOutputTest.displayComparingListElement(words, insensitiveComparator)
	}
}
