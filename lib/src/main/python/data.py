# This code does something which turned out to be really ugly.  The idea
# here is to fit an XML document into a series of nested Python dictionaries
# and lists.  Then autogen can stick the dictionaries in the local scope
# of the template's code snippets.  Idealy the syntax of a template will
# then be nice and clean and obvious.
#
# Both problems with this plan come from the fact that while XML elements
# can contain multiple children with the same name a dictionary can't.
#
# First, don't put single instances of a child in the same element with
# multiple instances.  (i.e. <a><b/><c/><c/></a>)  This will cause problems.
#
# Second, there's going to be one more level of scope than you'd like.
#
# The XML code:
#   <list>
#     <item>
#       <key>value</key>
#     </item>
#     <item>
#       <key>value</key>
#     </item>
#   <list>
#
# Will become: {'list' : [ {'item' : {'key' : 'value'}},
#                          {'item' : {'key' : 'value'}} ] }
#
# Thus, when you iterate over 'list', the successive 'item's will be scoped,
# not 'key'.  'key' can then be accessed as item['key'] or by placing the
# code in a [< local item >] block.

from xml.parsers.expat import ParserCreate

class ParserException(Exception):
    def __init__(self, msg):
        Exception.__init__(self, msg)

class Parser(object):
    def __init__(self):
        self.p = ParserCreate()
        self.p.StartElementHandler = self.StartElementHandler
        self.p.EndElementHandler = self.EndElementHandler
        self.p.CharacterDataHandler = self.CharacterDataHandler

    def StartElementHandler(self, name, attrs):
        if len(attrs) > 0:
            new_element = {name : attrs}
        else:
            new_element = {name : None}

        top = self.stack[-1]
        data_top = self.data_stack[-1]
        if data_top[top] is None:
            data_top[top] = new_element
        elif isinstance(data_top[top], list):
            data_top[top].append(new_element)
        elif isinstance(data_top[top], dict):
            if name in data_top[top]:
                data_top[top] = [{k:v} for k, v in data_top[top].iteritems()]
                data_top[top].append(new_element)
            else:
                new_element = data_top[top]
                new_element[name] = None
        else:
            raise ParserException("line %d: Element already contains text." % self.p.CurrentLineNumber)

        self.data_stack.append(new_element)
        self.stack.append(name)

    def EndElementHandler(self, name):
        self.data_stack.pop()
        self.stack.pop()
    
    def CharacterDataHandler(self, data):
        if len(data.strip()) == 0:
            return

        top = self.stack[-1]
        data_top = self.data_stack[-1]
        if data_top[top] == None:
            data_top[top] = data
        elif isinstance(data_top[top], unicode):
            data_top[top] += data
        else:
            raise ParserException("line %d: Stray text." % self.p.CurrentLineNumber)

    def parse(self, file):
        self.stack = ['top']
        self.data_stack = [{'top': None}]
        self.p.ParseFile(file)
        return self.data_stack[0]['top']

if __name__ == '__main__':
    import sys
    p = Parser()
    print p.parse(open(sys.argv[1]))
