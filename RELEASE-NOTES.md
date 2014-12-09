# Cumulative Release Notes for rosette-common-java

## 34.1.0


### Turning on  [Nexus release staging](https://confluence.basistech.net/display/ENG/Nexus#Nexus-StagingRepositoryUsage)
    
### ([COMN-156](http://jira.basistech.net/browse/COMN-156)) license manager improvements.

## 34.0.0

### ([COMN-148](http://jira.basistech.net/browse/COMN-148)) Split between public API and private common classes

We split this library from two to three jars: common-api, common-lib,
and common-t5builder.  Typical clients that used to depend on just
common will now need to depend on both common-api and common-lib.

To make the jar files play well as OSGi bundles, we changed some of
the package structure of some of the older internal classes and
removed the redundant extra implementation of the Take5 runtime.

Some classes were shuffled incompatibly to different package
locations.  Some classes that used to live in:

    com/basistech/util/internal/
    com/basistech/rosette/util/

now live here:

    com/basistech/internal/util/ArabicCodePointStrings
    com/basistech/internal/util/ArabicCodePoints
    com/basistech/internal/util/Base64
    com/basistech/internal/util/BeanConfigurationException
    com/basistech/internal/util/DataFileConfiguration
    com/basistech/internal/util/Endian
    com/basistech/internal/util/ISO15924Utils
    com/basistech/internal/util/InterruptibleCharSequence
    com/basistech/internal/util/NumericalPrecision
    com/basistech/internal/util/Openable
    com/basistech/internal/util/TabReader
    com/basistech/internal/util/UnicodeHelper
    com/basistech/internal/util/bitvector/BitVector
    com/basistech/internal/util/bitvector/IntProcedure
    com/basistech/internal/util/bitvector/QuickBitVector

The OSGi bundling requires some significant java heap.  If you see
this error when building:

```
[INFO] --- maven-bundle-plugin:2.5.3:bundle (default-bundle) @ t5builder ---
[ERROR] Java heap space -> [Help 1]
```

you will need to adjust your heap settings via MAVEN_OPTS, e.g.

```
$ MAVEN_OPTS=-Xmx2g mvn clean install
```

## 33.1.1

### ([COMN-93](http://jira.basistech.net/browse/COMN-93)) Script guessing changes

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
  
### ([COMN-136](http://jira.basistech.net/browse/COMN-136)) Changes to Take5Dictionary
  
*  This version deprecates the existing constructors for
   `Take5Dictionary` and provides a fluent `Take5DictionaryBuilder` in
   their place. 
* This version removes the questionable optimization of reading the memory of the FSA into the heap.
