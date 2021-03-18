# Cumulative Release Notes for rosette-common-java-api

## 37.1.0

Add language codes `BELARUSIAN`, `BURMESE`, `KHMER`, and `LAO`.

## 37.0.1

Consume parent pom 3.0.0

## 37.0.0

Use Guava 26.0-jre and Java 8.

## 36.2.0

Add language codes `NORTH_KOREAN` and `SOUTH_KOREAN`.

## 36.1.4

Add transliteration schemes `elot743`, `iso843_1997`, `iso259_1984`, `iso259_2_1994`, `ansiz39_25_1975`,
`iso11940_1998`, `iso11940_2_2007`, and `private_use`.

Deprecate transliteration schemes `iso_11940` and `iso_11940_2`.

## 36.1.3

Add transliteration schemes `iso_11940_2` and `rtgs`.

## 36.1.0

Fix EnumModule to call the base class in setupModule.

## 36.0.2

### ROS-196: Move to Jackson 2.7.3 and remove workaround.

Use a newer version of jackson and eliminate a complex workaround for
a Jackson issue.

## 36.0.1

### ROS-217: correct TextDomain to map null inputs to defaults.

null script is mapped `ISO15924#Zyyy`.
null language is mapped to `LanguageCode#UNKNOWN`.
null scheme is mapped to `TransliterationScheme#UNKNOWN`, not
`NATIVE`, as it was sometimes before this change.

## 36.0.0

### ROS-209: Split this into two jars.

Put the Jackson-dependent code into its own, independent, module.

We now build two artifacts:

```
com.basistech:common-api
com.basistech:common-api-jackson
```

Folks needing jackson serialization for LanguageCode (either within
ADM or outside of ADM) will need to depend on common-api-jackson.

## 35.6.0

### ROS-200: Add configuration marker interfaces.

Add `RosetteConfiguration` and `RosetteConfigurationKey` to avoid
the need for core SDK components to depend on an OSGi-specific library.

## 35.5.0

### COMN-212: Add/adjust common exceptions

Added RosetteUnsupportedLanguageException constructors that take a
``LanguageCode``.  Deprecated the others.  Added a new exception,
``RosetteIllegalArgumentException`` to be thrown when a known when an
invalid argument is passed specifically to a Rosette API.


## 35.4.0

### COMN-191: Add ZSM language code.

`LanguageCode.STANDARD_MALAY` was added.  iso639-3 code is "zsm".
iso639-1 code is "ms_sd".  This is a micro-language.  The existing
`LanguageCode.MALAY` ("msa", "ms") is a macro-language.

### RCB-237: Add enum for part of speech tag sets.

`PartOfSpeechTagSet` was added.
