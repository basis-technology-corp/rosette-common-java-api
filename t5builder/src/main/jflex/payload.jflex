/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5build;
import java.io.StringReader;

%%

%class PayloadLexer
%extends PayloadLexerBase
%scanerror PayloadLexerException
%type PayloadToken
%final
%unicode
%column

%{
  StringBuilder string = new StringBuilder();

  PayloadLexer(String data) {
      this(new StringReader(data));
  }

  void reset(String data) {
      yyreset(new StringReader(data));
  }

  private PayloadToken
   token(PayloadTokenType type, String text) {
    return new PayloadToken(type, text, yycolumn);
  }

private PayloadToken token(PayloadTokenType type) {
    return new PayloadToken(type, yytext(), yycolumn);
  }
%}

WhiteSpace = [ \t\f]

mode = "#" [0-9]*[*!]?[:ifs]

intnum = [+-]?([0-9]+ | 0[bB][01]+ | 0[xX][0-9a-fA-F]+)
flonum = [+-]?("." [0-9]+ {exponent}? | [0-9]+ "." [0-9]+ {exponent}? | "inf" | "NaN" )
exponent = [eE][+-]?[0-9]+
bmp = \\u([0-9a-fA-F]){4}
notbmp = \\U([0-9a-fA-F]){8}
hexitem = [0-9a-fA-F]+

%state STRING,HEXDUMP

%%

<YYINITIAL> {
/* whitespace */
  {WhiteSpace}               { /* ignore */ }
  \"                         { string.setLength(0); yybegin(STRING); }
  {mode}                     { return token(PayloadTokenType.MODE); }
  {intnum}                   { return token(PayloadTokenType.INTNUM); }
  {flonum}                   { return token(PayloadTokenType.FLONUM); }
  \[                         { yybegin(HEXDUMP); return token(PayloadTokenType.HEXDUMPSTART, ""); }
  .                          { throw new PayloadLexerException("Invalid character <" + yytext() + "> at toplevel", yycolumn); }
  }

<STRING> {
  \"                         { yybegin(YYINITIAL); return token(PayloadTokenType.STRING, string.toString()); }
  \\t                        { string.append('\t'); }
  \\n                        { string.append('\n'); }
  \\z                        { string.append("\u0000"); }
  {bmp}                      { string.append(decodeBmpEscape(yytext())); }
  {notbmp}                   { string.appendCodePoint(decodeCodepointEscape(yytext())); }
  \\r                        { string.append('\r'); }
  \\\"                       { string.append('\"'); }
  \\\\                       { string.append('\\'); }
  .                          { string.append(yytext()); } /* this does it one-at-a-time... */
  <<EOF>>                    { throw new PayloadLexerException("End of input in the midst of a quoted string", yycolumn); }
}

<HEXDUMP> {
  {WhiteSpace}               { /* ignore */ }
  {hexitem}                  { return token(PayloadTokenType.HEXDUMPITEM); }
  \]                         { yybegin(YYINITIAL); return token(PayloadTokenType.HEXDUMPEND, ""); }
  .                          { throw new PayloadLexerException("Invalid character <" + yytext() + "> in hexdump.", yycolumn); }
}

 /* error fallback */
.                              { throw new PayloadLexerException("Invalid character <" + yytext() + ">", yycolumn); }
