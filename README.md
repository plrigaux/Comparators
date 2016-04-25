# AlphaNumComparator

Perform 'natural order' comparisons of strings in Java.

The Alphanum Algorithm is an improved sorting algorithm for strings
containing numbers.  Instead of sorting numbers in ASCII order like
a standard sort, this algorithm sorts numbers in numeric order.

 	
"Natural sort" is the widely-used term for sorting "image9.jpg" as being less than "image10.jpg". it is "natural" because it is how a human being would sort them, as opposed to the unnatural "ascii-betical" sorting that computers do by default. codinghorror.com/blog/archives/001018.html – Kip Aug 11 '09 at 19:28


I'd like some kind of string comparison function that preserves natural sort order1. Is there anything like this built into Java? I can't find anything in the String class, and the Comparator class only knows of two implementations.

I can roll my own (it's not a very hard problem), but I'd rather not re-invent the wheel if I don't have to.

In my specific case, I have software version strings that I want to sort. So I want "1.2.10.5" to be considered greater than "1.2.9.1".

1 By "natural" sort order, I mean it compares strings the way a human would compare them, as opposed to "ascii-betical" sort ordering that only makes sense to programmers. In other words, "image9.jpg" is less than "image10.jpg", and "album1set2page9photo1.jpg" is less than "album1set2page10photo5.jpg", and "1.2.9.1" is less than "1.2.10.5"

1. Negative number
2. Space insensitive
1. Switch betwwen collator

Sometimes they accept and skip whitespace, skip leading zeros and most importantly places shorter strings before longer strings when they are equivalent. The string 1.020 will then be placed after 1.20. If you're using it for determining if two versions are equal you can get a false negative in this case. I.e. when checking that compareTo() returns 0.

http://blog.codinghorror.com/sorting-for-humans-natural-sort-order/

https://github.com/paour/natorder
