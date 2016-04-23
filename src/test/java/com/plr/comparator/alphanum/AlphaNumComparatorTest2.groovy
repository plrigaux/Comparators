package com.plr.comparator.alphanum


import spock.lang.Shared
import spock.lang.Specification

class AlphaNumComparatorTest2 extends Specification {


	@Shared AlphaNumComparator alphaNumComparator = null;


	def setupSpec() {
		alphaNumComparator = new AlphaNumComparator();
	}


	def "test sort list" () {
		given:

		def list = [
			"z1.doc",
			"z10.doc",
			"z100.doc",
			"z101.doc",
			"z102.doc",
			"z11.doc",
			"z12.doc",
			"z13.doc",
			"z14.doc",
			"z15.doc",
			"z16.doc",
			"z17.doc",
			"z18.doc",
			"z19.doc",
			"z2.doc",
			"z20.doc",
			"z3.doc",
			"z4.doc",
			"z5.doc",
			"z6.doc",
			"z7.doc",
			"z8.doc",
			"z9.doc",
		]

		def expected = [
			"z1.doc",
			"z2.doc",
			"z3.doc",
			"z4.doc",
			"z5.doc",
			"z6.doc",
			"z7.doc",
			"z8.doc",
			"z9.doc",
			"z10.doc",
			"z11.doc",
			"z12.doc",
			"z13.doc",
			"z14.doc",
			"z15.doc",
			"z16.doc",
			"z17.doc",
			"z18.doc",
			"z19.doc",
			"z20.doc",
			"z100.doc",
			"z101.doc",
			"z102.doc",
		]



		when: "Do nothing"

		then: "The list aren't equal"
		list != expected

		when: "Sorting list"
		Collections.sort(list, alphaNumComparator)

		then: "The list are equal"
		list == expected
	}

	def "product names sort"() {
		given:
		def list = [
			"1000X Radonius Maximus",
			"10X Radonius",
			"200X Radonius",
			"20X Radonius",
			"20X Radonius Prime",
			"30X Radonius",
			"40X Radonius",
			"Allegia 50 Clasteron",
			"Allegia 500 Clasteron",
			"Allegia 50B Clasteron",
			"Allegia 51 Clasteron",
			"Allegia 6R Clasteron",
			"Alpha 100",
			"Alpha 2",
			"Alpha 200",
			"Alpha 2A",
			"Alpha 2A-8000",
			"Alpha 2A-900",
			"Callisto Morphamax",
			"Callisto Morphamax 500",
			"Callisto Morphamax 5000",
			"Callisto Morphamax 600",
			"Callisto Morphamax 6000 SE",
			"Callisto Morphamax 6000 SE2",
			"Callisto Morphamax 700",
			"Callisto Morphamax 7000",
			"Xiph Xlater 10000",
			"Xiph Xlater 2000",
			"Xiph Xlater 300",
			"Xiph Xlater 40",
			"Xiph Xlater 5",
			"Xiph Xlater 50",
			"Xiph Xlater 500",
			"Xiph Xlater 5000",
			"Xiph Xlater 58"
		]

		def expected = [
			"10X Radonius",
			"20X Radonius",
			"20X Radonius Prime",
			"30X Radonius",
			"40X Radonius",
			"200X Radonius",
			"1000X Radonius Maximus",
			"Allegia 6R Clasteron",
			"Allegia 50 Clasteron",
			"Allegia 50B Clasteron",
			"Allegia 51 Clasteron",
			"Allegia 500 Clasteron",
			"Alpha 2",
			"Alpha 2A",
			"Alpha 2A-900",
			"Alpha 2A-8000",
			"Alpha 100",
			"Alpha 200",
			"Callisto Morphamax",
			"Callisto Morphamax 500",
			"Callisto Morphamax 600",
			"Callisto Morphamax 700",
			"Callisto Morphamax 5000",
			"Callisto Morphamax 6000 SE",
			"Callisto Morphamax 6000 SE2",
			"Callisto Morphamax 7000",
			"Xiph Xlater 5",
			"Xiph Xlater 40",
			"Xiph Xlater 50",
			"Xiph Xlater 58",
			"Xiph Xlater 300",
			"Xiph Xlater 500",
			"Xiph Xlater 2000",
			"Xiph Xlater 5000",
			"Xiph Xlater 10000"
		]

		when: "Do nothing"

		then: "The list aren't equal"
		list != expected

		when: "Sorting list"
		Collections.sort(list, alphaNumComparator)

		then: "The list are equal"
		list == expected
	}

	enum CompType {
		GREATER, LESS, EQUAL
	}

	boolean compToZero(CharSequence s1, CharSequence s2, CompType compType) {

		int val = alphaNumComparator.compare(s1, s2)
		
		switch (compType) {
			case CompType.GREATER:
				return val > 0
			case CompType.LESS:
				return val < 0
			case CompType.EQUAL:
				return val == 0
		}
	}

	def "Mutiple cases"() {

		expect:

		compToZero (a, b, c) == true

	

		where:

		a 				| b 			| c
		"doc20.doc" 	| "doc10.doc" 	| CompType.GREATER
		"doc10.doc"		| "doc20.doc" 	| CompType.LESS
		"doc2.doc"		| "doc10.doc" 	| CompType.LESS
		"doc2.1.doc"	| "doc2.2.doc"	| CompType.LESS
		"doc2.10.doc"	| "doc2.2.doc"	| CompType.GREATER
	}
}
