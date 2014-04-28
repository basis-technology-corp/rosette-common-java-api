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

package com.basistech.t5build;
import java.io.StringReader;

%%

%class KeyLexer
%scanerror PayloadLexerException
%type String
%final
%unicode
%column

%{
  StringBuilder string = new StringBuilder();

  KeyLexer(String data) {
      this(new StringReader(data));
  }

  void reset(String data) {
      yyreset(new StringReader(data));
  }
%}

bmp = \\u([0-9a-fA-F]){4}
notbmp = \\U([0-9a-fA-F]){8}

%%

<YYINITIAL> {
  \\t                        { string.append('\t'); }
  \\n                        { string.append('\n'); }
  \\z                        { string.append("\u0000"); }
  {bmp}                      { string.append(PayloadLexerBase.decodeBmpEscape(yytext())); }
  {notbmp}                   { string.appendCodePoint(PayloadLexerBase.decodeCodepointEscape(yytext())); }
  \\r                        { string.append('\r'); }
  \\                         { string.append('\\'); }
  .                          { string.append(yytext()); } /* this does it one-at-a-time... */
  <<EOF>>                    { String r = string.toString(); string.setLength(0); return r; }
}

 /* error fallback */
.                            { throw new PayloadLexerException("Invalid character <" + yytext() + ">", yycolumn); }
