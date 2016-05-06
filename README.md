# Natural Comparator


This Java library performs 'natural order' comparisons of strings in Java. Copyright (C) 2016 by Pierre-Luc Rigaux plrigaux@gmail.com.


It is based on the work of : 

* Pierre-Luc Paour <https://github.com/paour/natorder>
* David Koelle <http://www.davekoelle.com/alphanum.html>

This [article](http://blog.codinghorror.com/sorting-for-humans-natural-sort-order/) by Jeff Atwood on Coding Horror explains the problematic of the need of a natural ordering library.

## Description

As humans we sort strings diffrently than computers. This librairy helps to sort string in natural order. The natural order is the way humans would sort elements contrary as the default computer sorting order wich follow the "unnatural" ASCII order.

"Natural sort" is the widely-used term for sorting "image9.jpg" as being less than "image10.jpg". it is "natural" because it is how a human being would sort them, as opposed to the unnatural "ascii-betical" sorting that computers do by default. codinghorror.com/blog/archives/001018.html – Kip Aug 11 '09 at 19:28


I'd like some kind of string comparison function that preserves natural sort order1. Is there anything like this built into Java? I can't find anything in the String class, and the Comparator class only knows of two implementations.

The Alphanum Algorithm is an improved sorting algorithm for strings
containing numbers.  Instead of sorting numbers in ASCII order like
a standard sort, this algorithm sorts numbers in numeric order.

<table>
<tr><th>ASCII Order<th>Natural Order</tr>
<tr><td>
     z101.doc<br>     
     z4.doc<br>
     z102.doc<br>     
     z5.doc<br>    
     z1.doc<br>
     z11.doc<br>    
     z6.doc<br>    
     z2.doc<br>
     z12.doc<br>
     </td>
     <td>
     z101.doc<br>     
     z4.doc<br>
     z102.doc<br>     
     z5.doc<br>    
     z1.doc<br>
     z11.doc<br>    
     z6.doc<br>    
     z2.doc<br>
     z12.doc<br>
     </tr>
</table>


1. Negative number
2. Space insensitive
1. Switch betwwen collator

## Numbers

The compatator compares numbers in the natural form order. Basically the order that we learnt at school.

```

 0 < 1 < 3 < 10 < 23 < 134 < 123454634563472345345653456347
  
```

Unlike some one liner scripts, the comparator doesn't transform the numeric string into a numeric value. This have 2 benefits:

* Allows numbers beyong the limits of the language native num type
* Increases calculation performance 

### Negative numbers

The comparator takes case of strings representing negative numbers. Which means that it considers that "-10" is smaller than "-1".

It is consider a negative number if the string starts with an hyphen ('-') or there is a whitespace before the hyphen. 

Negative: "-456", " -43", "test  -467"

Not negative: "Something-45" 

### Decimal numbers

The comparator takes also care of the decimal part of the number. Honestly, I don't know in real life when it can be usefull never the less for sake of consistency I decide to include this feature.

```

 0 < 1 < 3 < 10 < 23 < 134 < 123454634563472345345653456347
 
 
```

### Leading and tailing zeros

As you know the leading and tailing zeros have no numerical significance, but it may have an aestetic one. The comparator will ignore leading and tailing zeros if mode ASDF chosssen.

## Space insensitive

The comparator doesn't take care of white spaces in front of numbers.

all leading and trailing whitespace of both the expectedString and the examined string are ignored
any remaining whitespace, appearing within either string, is collapsed to a single space before comparison
For example:

assertThat("   my\tfoo  bar ", equalToIgnoringWhiteSpace(" my  foo bar"))

## Heading zeros incensitive
The comparator doesn't take care of zeros in front of numbers.

## Custom comparator

### Internationalisation I18n
The comparator can be ajusted to compare compare strings lexicographically, according to a specific locale.



The Collator class performs locale-sensitive String comparison. You use this class to build searching and sorting routines for natural language text.

### Case insensitive

The comparator can be ajusted to compare strings ignoring case difference.

Note that this method does not take locale into account, and will result in an unsatisfactory ordering for certain locales. The java.text package provides collators to allow locale-sensitive ordering.


## Lists table order
In my specific case, I have software version strings that I want to sort. So I want "1.2.10.5" to be considered greater than "1.2.9.1".




1 By "natural" sort order, I mean it compares strings the way a human would compare them, as opposed to "ascii-betical" sort ordering that only makes sense to programmers. In other words, "image9.jpg" is less than "image10.jpg", and "album1set2page9photo1.jpg" is less than "album1set2page10photo5.jpg", and "1.2.9.1" is less than "1.2.10.5"



Sometimes they accept and skip whitespace, skip leading zeros and most importantly places shorter strings before longer strings when they are equivalent. The string 1.020 will then be placed after 1.20. If you're using it for determining if two versions are equal you can get a false negative in this case. I.e. when checking that compareTo() returns 0.



https://github.com/paour/natorder

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
<code>'Doc&nbsp;5.doc'&nbsp;=&nbsp;'Doc5.doc'</code><br>
<code>'Doc&nbsp;5.doc'&nbsp;=&nbsp;'Doc05.doc'</code><br>
<code>'Doc&nbsp;5.doc'&nbsp;&lt;&nbsp;'Doc05.2.doc'</code><br>
<tr><td>SECONDARY
<td>If the string are <i>PRIMARY</i> equal, the comparator looks white spaces and leading and trailing zeros around numbers to differentiate them.<br>
<br>
This a can be useful is you want to sort in a definitive order similar string.
<td>
<code>'Doc&nbsp;5.doc'&nbsp;&gt;&nbsp;'Doc5.doc'</code><br>
<code>'Doc&nbsp;5.doc'&nbsp;&lt;&nbsp;'Doc05.doc'</code><br>
<code>'Doc&nbsp;5.doc'&nbsp;&lt;&nbsp;'Doc05.2.doc'"</code><br>
<tr><td>LTRIM
<td>Ignore leading white spaces at the beginning and the end of the string.
<td> 
<code>'Doc5.doc'&nbsp;=&nbsp;'&nbsp;&nbsp;&nbsp;Doc5.doc'</code><br>
<tr><td>RTRIM
<td>Ignore trailing white spaces at the beginning and the end of the string.
<td> <code>'Doc5.doc'&nbsp;=&nbsp;'Doc5.doc&nbsp;&nbsp;&nbsp;&nbsp;'</code><br>
<tr><td>TRIM

<td>Ignore leading and trailing white spaces at the beginning and the end of the string. 
<br><br>
<i>Note</i>: Combination of LTRIM and RTRIM
<td>
<code>'Doc5.doc'&nbsp;=&nbsp;'&nbsp;&nbsp;Doc5.doc&nbsp;&nbsp;&nbsp;&nbsp;'</code><br>
<tr><td>NEGATIVE_NUMBER
<td>Treat the hyphen before the number as negative.
<td>
<code>'-5'&nbsp;&lt;&nbsp;'-4'</code><br>
<tr><td>RATIONAL_NUMBER
<td>Handle the portion after the dot '.' as decimal.
<td>
<code>'10.4'&nbsp;&lt;&nbsp;'10.45'</code><br>
<tr><td>REAL_NUMBER
<td>Combination of the NEGATIVE_NUMBER and RATIONAL_NUMBER flag.
<td>
<code>'-10.4'&nbsp;&gt;&nbsp;'-10.45'</code><br>
<tr><td>SPACE_INSENSITVE
<td>Ignore  white spaces in string.
<td><code>"abc"	= " a b&nbsp;&nbsp;c "</code><br>
		<code>"  \n   abc  \n"	= " a b   c "</code><br>
<tr><td>	SPACE_REPETITION_INSENSITVE	
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
