# This makefile documents how you run any non-automatic procedures in here.
# It does nothing by default

error:
	echo "Pick a target"
	exit 1

iso15924: src/main/data/iso15924-utf8.xml

# The iso15924-utf8.txt, which is checked in, comes from:
#   http://www.unicode.org/iso15924/iso15924.txt.zip
PYTHON3=python3
src/main/data/iso15924-utf8.xml: src/main/data/iso15924-utf8.txt
	$(PYTHON3) src/main/python/iso15924-to-xml.py src/main/data/iso15924-utf8.txt src/main/data/iso15924-utf8.xml
