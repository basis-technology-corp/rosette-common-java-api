
TEXT = 1
DEFINE = 'define'
SCOPE = 2
EVAL = 'eval'

functions = set(['for','if'])
end_functions = set(['end-for', 'end-if'])
ifs = set(['elif', 'else'])

def classify(symbol):
    if symbol in (u'[%', u'%]'):
        return DEFINE
    elif symbol in (u'[<', u'>]'):
        return SCOPE
    elif symbol in (u'[=', u'=]'):
        return EVAL
    else:
        return TEXT

def tokenize(input):
    line = 1
    token = u''
    while len(input) > 1:
        type = classify(input[0:2])
        if type > TEXT:
            if len(token) > 0:
                yield (TEXT, token, line)
                token = u''
            yield (type, input[0:2], line)
            input = input[2:]
        else:
            if input[0] == u'\n':
                line += 1
            token += input[0]
            input = input[1:]
    token += input
    if len(token) > 0:
        yield (TEXT, token, line)

class ParserException(Exception):
    def __init__(self, msg):
        Exception.__init__(self, msg)

def parse(input):
    tokens = tokenize(input)

    stack = [(0, [])]

    for type, token, line in tokens:
        top_type, top_list = stack[-1][0:2]
        if type is TEXT:
            top_list.append(token)
            continue

        ctype, content, line = tokens.next()
        content = content.strip()
        if ctype is not TEXT or content == "":
            raise ParserException("line %d: Empty declaration." % line)
        ttype, terminal, line = tokens.next()
        if ttype != type:
            raise ParserException("line %d: Mismatched bracket type '%s'." % (line, terminal))
                
        if type in (DEFINE, EVAL):
            top_list.append((type, [content]))
        elif type is SCOPE:
            contents = content.split(' ')
            if contents[0] in functions:
                new = (contents[0], [' '.join(contents[1:])], [])
                top_list.append(new)
                stack.append(new)
            elif contents[0] in end_functions:
                if top_type in ifs:
                    top_type = 'if'
                if content != ("end-" + top_type):
                    raise ParserException("line %d: Type mismatch, expected 'end-%s', found '%s'." % (line, top_type, contents[0]))
                if top_type == 'if':
                    while stack.pop()[0] != 'if': pass
                else:
                    stack.pop()
            elif contents[0] in ifs:
                if top_type not in ('if', 'elif'):
                    raise ParserException("line %d: '%s' cannot follow '%s'." % (line, contents[0], top_type))
                new = (contents[0], [' '.join(contents[1:])], [])
                stack[-1][2].append(new)
                stack.append(new)                                        
            else:
                raise ParserException("line %d: Unknown function '%s'." % (line, contents[0]))
                                      
    if len(stack) != 1:
        raise ParserException("Unexpected end of input, missing [< end-%s >]." % stack[-1][0])
    return stack[0][1]

if __name__ == '__main__':
    import sys

    f = open(sys.argv[1])
    input = f.read()

    print parse(input)
