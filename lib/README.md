# The Common Library #

This directory contains common code used across all Basis Java assets.
We minimize incompatible changes to avoid the need to make coordinated
changes across our products, and to make it easier for customers to mix and match products. 

# Data #

This project contains reference data for ISO-639 language codes,
ISO-15924 script codes, and text domains. The reference data is stored
in `src/main/data`.
[http://www.unicode.org/iso15924/iso15924-text.html](http://www.unicode.org/iso15924/iso15924-text.html)
is the source of `src/main/data/iso15924-utf8.txt`. A python3 script,
run by the Makefile, generates `iso15924-utf8.xml` from the text file.
The Maven build generates the Java enums from the XML files.



