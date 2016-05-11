# Insensitive Comparator

The insensitive comparator is called insensitive because it can compare strings while not being 
sensitive to chosen characters. The original intention was to be insensitive to white spaces, but
we felt fair to extends this capacity to all chosen characters. 

The comparator leverage the Guava's [CharMatcher](https://github.com/google/guava/wiki/StringsExplained#charmatcher) to perform the character matching.

```
Comparator<CharSequence> insensitiveComparator = InsensitiveComparator.onWhiteSpace().trim();
```

## Insensitive to all ...

The comparator can be insensitive to all selected characters.

## Insensitive to characters repetitions

The comparator can be insensitive to characters repetitions.

## Trim

It allows the possibility to compare strings by not considering both string ends.
