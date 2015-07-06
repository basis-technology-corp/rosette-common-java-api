/******************************************************************************
 * * This data and information is proprietary to, and a valuable trade secret
 * * of, Basis Technology Corp.  It is given in confidence by Basis Technology
 * * and may only be used as permitted under the license agreement under which
 * * it has been distributed, and in no other way.
 * *
 * * Copyright (c) 2010 Basis Technology Corporation All rights reserved.
 * *
 * * The technical data and information provided herein are provided with
 * * `limited rights', and the computer software provided herein is provided
 * * with `restricted rights' as those terms are defined in DAR and ASPR
 * * 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5build;

import com.google.common.base.Charsets;

import java.nio.ByteBuffer;

final class Utils {
    private Utils() {
    }

    static int gcd(int n, int m) {
        assert n > 0 && m > 0;
        int r = n % m;
        while (r != 0) {
            n = m;
            m = r;
            r = n % m;
        }
        return m;
    }

    static int lcm(int n, int m) {
        return n * (m / gcd(n, m));
    }

    static int alignUp(int addr, int align) {
        addr += align - 1;
        addr -= addr % align;
        return addr;
    }

    static int alignDown(int addr, int align) {
        addr -= addr % align;
        return addr;
    }

    // 2^msb(x) <= x < 2^(1 + msb(x))
    // (or alternatively 1 << msb(x) = Integer.highestOneBit(x))
    static int msb(int x) {
        int rv = 0;
        assert x > 0;
        if (x >= (1 << 16)) {
            rv += 16;
            x >>= 16;
        }
        if (x >= (1 << 8)) {
            rv += 8;
            x >>= 8;
        }
        if (x >= (1 << 4)) {
            rv += 4;
            x >>= 4;
        }
        if (x >= (1 << 2)) {
            rv += 2;
            x >>= 2;
        }
        if (x >= (1 << 1)) {
            rv += 1;
        }
        return rv;
    }

    static int hashStep(int hash, int x) {
        return (hash * 0x55555565) + x; // random prime = 5 (mod 8)
    }

    static int hashBytes(byte[] bytes, int offset, int length) {
        int hash = length;
        while (length > 0) {
            hash = hashStep(hash, bytes[offset]);
            offset++;
            length--;
        }
        return hash;
    }

    static int hashBytes(ByteBuffer bytes) {
        int length = bytes.capacity();
        int hash = length;
        bytes.position(0);
        while (length > 0) {
            hash = hashStep(hash, bytes.get());
            length--;
        }
        bytes.position(0);
        return hash;
    }

    // There isn't a library routine that does this as far as I can see...
    static boolean equalBytes(byte[] bytes1, int offset1,
                              byte[] bytes2, int offset2,
                              int length) {
        while (length > 0) {
            if (bytes1[offset1] != bytes2[offset2]) {
                return false;
            }
            offset1++;
            offset2++;
            length--;
        }
        return true;
    }

    // Convert a string to ASCII bytes.
    static byte[] toAscii(String data) {
        return data.getBytes(Charsets.US_ASCII);
    }

    static String charString(char c) {
        if (c > '\u00FF') {
            return String.format("%04X", (int) c);
        } else if (c < '!' || c > '~') {
            return String.format("%02X", (int) c);
        } else {
            return Character.toString(c);
        }
    }

    // The index is measured in ints, not bytes.
    static void putNetworkInt(ByteBuffer buf, int index, int value) {
        index *= 4;
        buf.put(index + 0, (byte) (value >> 24));
        buf.put(index + 1, (byte) (value >> 16));
        buf.put(index + 2, (byte) (value >> 8));
        buf.put(index + 3, (byte) (value >> 0));
    }
}
