# Cumulative Release Notes for rosette-common-java

## 35.2.1

### Update to current parent (57.2.9), make it build with Java8.

## 35.2.0

### [ROS-183](http://jira.basistech.net/browse/RD-1239): Remove  opencsv dependency
    
This picks up a new parent that uses a new version of the
maven-bundle-plugin to avoid propagating embedded jars as
dependencies, in addition to removing all embedded dependencies from
the 5builder.

## 35.1.4

### [RD-1239](http://jira.basistech.net/browse/RD-1239) Workaround split package

utilities.jar, required by RES (because RNI requires it) has files in
the same package used in common-api.  This change is a workaround to
enable OSGi for RES, etc. by *copying* those file from utilities.jar
into common-api:

```
com/basistech/util/BasisRootBean.java
com/basistech/util/NoRootDirectoryException.java
com/basistech/util/Pathnames.java
```

## 35.1.2

### No Jira: move to parent version 57.1.2, and thus Jackson 2.6.2.

## 35.1.1

### No jira: added an annotation class used in the OSGi system.
    `RosetteSystemBundlePackage`

## 35.1.0

### [ROS-88](http://jira.basistech.net/browse/ROS-88) Use parent 57.1.0

## 35.0.0

### [COMN-196](http://jira.basistech.net/browse/COMN-196) Build with   Java 1.7
    
Adopt parent 57.0.0 and build to require Java 1.7.    
    

### No jira: Removed joda-time dependency in Take5Build command line

Removed optional joda-time dependency from t5builder jar.  If you want
to use the Take5Build command line, you no longer need to provide
this, but you still need to provide args4j.

## 34.1.0

### No jira: Turning on [Nexus release staging](https://confluence.basistech.net/display/ENG/Nexus#Nexus-StagingRepositoryUsage)

See [752fb8ba](https://git.basistech.net/textanalytics/rosette-common-java/commit/752fb8ba3dacce4adbe4d3136c7aa481134d4d08).
    
### [COMN-156](http://jira.basistech.net/browse/COMN-156) License manager improvements

LManager can now be given a secret to enable components to bypass
license checks when we're calling between components, e.g. REX using
RBL without an RBL license.

## 34.0.0

### [COMN-148](http://jira.basistech.net/browse/COMN-148) Split between public API and private common classes

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

### [COMN-93](http://jira.basistech.net/browse/COMN-93) Script guessing changes

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
  
### [COMN-136](http://jira.basistech.net/browse/COMN-136) Changes to Take5Dictionary
  
*  This version deprecates the existing constructors for
   `Take5Dictionary` and provides a fluent `Take5DictionaryBuilder` in
   their place. 
* This version removes the questionable optimization of reading the memory of the FSA into the heap.
