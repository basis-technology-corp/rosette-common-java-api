# COMN-18: This rule used to live in rlp/utilities/source/code/Makefile

# iso15924-utf8.txt comes from here:
#   http://www.unicode.org/iso15924/iso15924.txt.zip
# iso15924-utf8.xml is produced from the txt file with:
#   //depot/tip/basis_tools/java/src/ISO15924ToXML.java
iso15924-utf8.xml: iso15924-utf8.txt
	$(JAVA_HOME)/bin/javac ../../../../basis_tools/java/src/ISO15924ToXML.java
	$(JAVA_HOME)/bin/java -classpath ../../../../basis_tools/java/src ISO15924ToXML $< $@
