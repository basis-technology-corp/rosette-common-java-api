# Cumulative Release Notes for rosette-common-java #

# 33.1.1 #

## Script guessing changes ([COMN-93](http://jira.basistech.net/browse/COMN-93)) ##

We changed ISO15924Utils to stop using a Unicode block-based scheme
for mapping characters to scripts, and switched to the official
Unicode Consortium property-based method. There is a dependency on
ICU4J to implement this; if we could move to Java 1.7, we could just
use the built-in UnicodeScript class, instead.  This means that maven
users of the common jar now pull in an additional dependency on icu4j.
Products that use the ISO15924Utils that don't already bundle an icu4j
must now do so.

Here are some examples of how this is different:

* Latin-alphabet characters 'marooned' in other blocks are now
  labelled 'Latn', such as the full-width forms.

* Non-alphabetic characters such as numbers and punctuation are Zyyy,
  even if they reside in the Latin block.

* Miscellaneous characters, such as the BOM, are correctly
  characterized regardless of their block
  
## Changes to Take5Dictionary  ([COMN-136](http://jira.basistech.net/browse/COMN-136)) ##
  
*  This version deprecates the existing constructors for
   `Take5Dictionary` and provides a fluent `Take5DictionaryBuilder` in
   their place. 
* This version allows applications to control the questionable
  optimization of reading the memory of the FSA into the heap. By
  default, the data is not copied. `Take5DictionaryBuilder` provides a
  method to request the copy; the request is only respected for
  'version 56' and above dictionaries, to avoid problem of reading
  unreasonable amounts of data.
