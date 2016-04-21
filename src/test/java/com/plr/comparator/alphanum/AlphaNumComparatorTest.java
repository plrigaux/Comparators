package com.plr.comparator.alphanum;

import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AlphaNumComparatorTest {

	AlphaNumComparator alphaNumComparator = null;

	@Before
	public void before() {
		alphaNumComparator = new AlphaNumComparator();
	}

	@Test
	public void basic() {
		int ret = alphaNumComparator.compare("doc2.doc", "doc10.doc");

		Assert.assertThat(ret, lessThan(0));
	}
	
	@Test
	public void basic2() {
		int ret = alphaNumComparator.compare("doc20.doc", "doc10.doc");

		Assert.assertThat(ret, greaterThan(0));
	}

}
