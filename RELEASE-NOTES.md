# Cumulative Release Notes for rosette-common-java

# 36.0.0

### ROS-209: Split this into two jars.

Put the Jackson-dependent code into its own, independent, module.

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

### RCB-237: Add enum for part of speech tag sets.
