# Cumulative Release Notes for rosette-common-java #

# 35.0.0 #

## Script guessing changes ##

We changed ISO15924Utils to stop using a Unicode block-based scheme for mapping characters to scripts, and switched to the official Unicode Consortium property-based method. There is a dependency on ICU4J to implement this; if we could move to Java 1.7, we could just use the built-in UnicodeScript class, instead.

Here are some examples of how this is different:

* Latin-alphabet characters 'marooned' in other blocks are now labelled 'Latn', such as the full-width forms.
* Non-alphabetic characters such as numbers and punctuation are Zyyy, even if they reside in the Latin block.
* Miscellaneous characters, such as the BOM, are correctly characterized regardless of their block
