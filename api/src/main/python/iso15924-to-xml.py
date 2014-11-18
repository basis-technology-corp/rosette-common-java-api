import csv
import codecs
import sys


def convert(input_path, output_path):
    with codecs.open(output_path, 'w', 'utf-8') as output:
        output.write("""<?xml version='1.0' encoding='utf-8'?>\n""")
        output.write("""<!-- Generated with iso15924-to-xml.py from {input}-->\n""".format(input=input_path))
        output.write("""<iso15924definitions>\n""")

        with codecs.open(input_path, 'r', 'utf-8') as datafile:
            reader = csv.reader(datafile, delimiter=';')
            for row in reader:
                if len(row) == 0 or row[0].startswith('#'):
                    continue
                char4 = row[0]
                numeric = row[1]
                text_eng = row[2]
                text_fra = row[3]
                name = row[4]
                date = row[5]
                output.write("""    <code>
      <char4>{char4}</char4>
      <numeric>{numeric}</numeric>
      <name>{name}</name>
      <PVA>{pva}</PVA>
      <date>{date}</date>
    </code>
    """.format(char4=char4, name=text_eng, numeric=numeric,
                           pva=name,
                           date=date))
        output.write("""
</iso15924definitions>\n""")

if __name__ == '__main__':
    convert(sys.argv[1], sys.argv[2])


