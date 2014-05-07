/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2010-2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5build;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OneArgumentOptionHandler;
import org.kohsuke.args4j.spi.Setter;

import java.nio.ByteOrder;

/**
* Created by benson on 5/7/14.
*/
public class ByteOrderOptionHandler extends OneArgumentOptionHandler<ByteOrder> {
    public ByteOrderOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super ByteOrder> setter) {
        super(parser, option, setter);
    }

    @Override
    protected ByteOrder parse(String argument) throws NumberFormatException, CmdLineException {
        if ("LE".equalsIgnoreCase(argument)) {
            return ByteOrder.LITTLE_ENDIAN;
        } else if ("BE".equalsIgnoreCase(argument)) {
            return ByteOrder.BIG_ENDIAN;
        } else {
            throw new CmdLineException(owner, "Invalid byte order " + argument);
        }
    }
}
