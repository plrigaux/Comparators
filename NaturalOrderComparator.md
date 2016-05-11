<h1 class="gh-header-title instapaper_title">Natural Comparator</h1>

# Introduction


This Java library performs "Natural Order" comparisons of strings in Java. Copyright (C) 2016 by Pierre-Luc Rigaux plrigaux@gmail.com.


It is based on the work of : 

* Pierre-Luc Paour <https://github.com/paour/natorder>
* David Koelle <http://www.davekoelle.com/alphanum.html>

This [article](http://blog.codinghorror.com/sorting-for-humans-natural-sort-order/) by Jeff Atwood on Coding Horror explains the problematic of the need of a natural ordering library.


# Description

As humans we sort strings differently than computers. This library helps to sort string in natural order. The natural order is the way humans would sort elements contrary as the default computer sorting order which follow the _unnatural_ ASCII order.

We call it "natural order" because it is how a human being would sort normally string, as opposed to the unnatural "ascii-betical" sorting that computers do by default. For example in "natural order" the file "image9.jpg" before "image10.jpg" or software version strings "1.2.10.5" is considered greater than "1.2.9.1".

See table below for the difference between the "ASCII Order" and the "Natural Order".

|ASCII Order   | Natural Order   |
|--------------|-----------------|
|     z101.doc |     z101.doc    |
|     z4.doc   |     z4.doc      |
|     z102.doc |     z102.doc    |
|     z5.doc   |     z5.doc      |
|     z1.doc   |     z1.doc      |
|     z11.doc  |     z11.doc     |
|     z6.doc   |     z6.doc      |
|     z2.doc   |     z2.doc      |
|     z12.doc  |     z12.doc     |


It's easy to find on the Web different algorithms to compare in "Natural Order", but I wanted to have a solution that encompass different use cases, such as :
 
* skip whitespace
* skip leading zeros 
* define an order when the string are equivalent `Example: 4-05 < 4-050`
* I18n
* ...

# Example
 
```java

Comparator<Charsequece> comparator = NaturalComparator.getComparator();

String[] array = {"z101.doc", "z4.doc", "z102.doc"};

Arrays.sort(array, comparator);

> Gives: "z4.doc", "z101.doc", "z102.doc"

```
> Gives: "z4.doc", "z101.doc", "z102.doc"

## Numbers

The comparator compares numbers in the natural form order. Basically the order that we learned at school.

```
 0 < 1 < 3 < 10 < 23 < 134 < 123454634563472345345653456347
  
```

Unlike some one liner scripts, the comparator doesn't transform the numeric string into a numeric value. 
This have 2 benefits:

* Allows numbers beyond the limits of the language native numeric type
* Increases calculation performance 

### Negative numbers

The comparator takes case of strings representing negative numbers. Which means that it 
considers that "-10" is smaller than "-1".

It considers a negative number if an hyphen ('-') is ahead a digit and if the hyphen:

1. is at the begin of the string OR
1. there is a whitespace before the hyphen. 

```
"-456", " -43", "test  -467"
```

If those conditions aren't met, we can consider the hyphen as a mere separator.

```
"Something-45", "123-456" 
```

### Decimal numbers

The comparator takes also care of the decimal part of the number. Honestly, I don't know in real life when it can be useful, never the less for sake of consistency I decided to include this feature.

```
"0.3" < "0.33" < "3.456456345634563423467" < "10" 
```

### Leading and tailing zeros

As you know the leading and tailing zeros have no numerical significance, but it may have an aesthetic one. Zero padding can also be used to sort numerical value in natural order. The comparator will ignore leading and tailing zeros if PRIMARY mode chosen.

### Space handling

The comparator has different ways to handle white spaces.

Note: for now the comparator only handle white spaces and NOT horizontal spaces the nonbraking space \u00A0.

#### Space insensitive

The comparator doesn't take in account any white spaces in the string.


#### Space repetition insensitive

The comparator collapse series of continuous white spaces and treat then as one space.

#### Space trimming

The comparator can ignore white space at the beginning, at the end or at both end of the string.



### Custom comparator

#### Internationalization (I18n)
The comparator can be adjusted to compare compare strings lexicographically, according to a specific locale.



The Collator class performs locale-sensitive String comparison. You use this class to build searching and sorting routines for natural language text.

#### Case insensitive

The comparator can be adjusted to compare strings ignoring case difference.

Note that this method does not take locale into account, and will result in an unsatisfactory ordering for certain locale. The java.text package provides collators to allow locale-sensitive ordering.







## Reverse order
If you want to sort in the reverse order you just have to call the  reverse() method. It leaverage the Java&nbsp;8 utility method.

```java
NaturalComparator naturalComparator = new NaturalComparator();

Comparator<String> comparator = naturalComparator.reverse();

```

# Summary

Here list of summaries




<table>
<tr><th>Case<th>Description<th>Example

<tr><td>PRIMARY
<td>Look only at the number numeric value. Treat leading and trailing zeros as non significant.
<td>
<code>"Doc&nbsp;5.doc"&nbsp;=&nbsp;"Doc5.doc"</code><br>
<code>"Doc&nbsp;5.doc"&nbsp;=&nbsp;"Doc05.doc"</code><br>
<code>"Doc&nbsp;5.doc"&nbsp;&lt;&nbsp;"Doc05.2.doc"</code><br>

<tr><td>SECONDARY
<td>If the string are <i>PRIMARY</i> equal, the comparator looks white spaces and leading and trailing zeros around numbers to differentiate them.<br>
<br>
This a can be useful is you want to sort in a definitive order similar string.
<td>
<code>"Doc&nbsp;5.doc"&nbsp;&gt;&nbsp;"Doc5.doc"</code><br>
<code>"Doc&nbsp;5.doc"&nbsp;&lt;&nbsp;"Doc05.doc"</code><br>
<code>"Doc&nbsp;5.doc"&nbsp;&lt;&nbsp;"Doc05.2.doc""</code><br>

<tr><td>LTRIM
<td>Ignore leading white spaces at the beginning and the end of the string.
<td> 
<code>"Doc5.doc"&nbsp;=&nbsp;"&nbsp;&nbsp;&nbsp;Doc5.doc"</code><br>

<tr><td>RTRIM
<td>Ignore trailing white spaces at the beginning and the end of the string.
<td> <code>"Doc5.doc"&nbsp;=&nbsp;"Doc5.doc&nbsp;&nbsp;&nbsp;&nbsp;"</code><br>

<tr><td>TRIM
<td>Ignore leading and trailing white spaces at the beginning and the end of the string. 
<br><br>
<i>Note</i>: Combination of LTRIM and RTRIM
<td>
<code>"Doc5.doc"&nbsp;=&nbsp;"&nbsp;&nbsp;Doc5.doc&nbsp;&nbsp;&nbsp;&nbsp;"</code><br>

<tr><td>NEGATIVE_NUMBER
<td>Treat the hyphen before the number as negative.
<td>
<code>"-5"&nbsp;&lt;&nbsp;"-4"</code><br>

<tr><td>RATIONAL_NUMBER
<td>Handle the portion after the dot "." as decimal.
<td>
<code>"10.4"&nbsp;&lt;&nbsp;"10.45"</code><br>

<tr><td>REAL_NUMBER
<td>Combination of the NEGATIVE_NUMBER and RATIONAL_NUMBER flag.
<td>
<code>"-10.4"&nbsp;&gt;&nbsp;"-10.45"</code><br>

<tr><td>SPACE_INSENSITVE
<td>Ignore  white spaces in string.
<td><code>"abc"	= " a b&nbsp;&nbsp;c "</code><br>
		<code>"  \n   abc  \n"	= " a b   c "</code><br>

<tr><td>SPACE_REPETITION_INSENSITVE	
<td> Ignore  white spaces repetition in string.
<td><code>"ab c"	= "ab   c"</code><br>
<code>"abc"	!= "ab   c"</code><br>
</table>

## Diclaimer
This software is provided 'as-is', without any express or implied warranty. In no event will the authors be held liable for any damages arising from the use of this software.

Permission is granted to anyone to use this software for any purpose, including commercial applications, and to alter it and redistribute it freely, subject to the following restrictions:

The origin of this software must not be misrepresented; you must not claim that you wrote the original software. If you use this software in a product, an acknowledgment in the product documentation would be appreciated but is not required.
Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software.
This notice may not be removed or altered from any source distribution.
