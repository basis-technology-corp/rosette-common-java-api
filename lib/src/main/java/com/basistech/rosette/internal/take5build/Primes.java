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

import com.google.common.primitives.UnsignedInts;

import java.util.Arrays;

/**
 * Some prime numbers.
 */
final class Primes {
    private static final int M = 2 * 3 * 5 * 7;

    /*
 * is_prime() and next_prime() need to have the first few answers built-in.
 * Not for speed, but for correctness!
 */
    private static final boolean[] IS_PRIME_TABLE = {
        false, false, true, true, false, true, false, true
    };

    private static final byte[] NEXT_PRIME_TABLE = {
        2, 2, 2, 3, 5, 5, 7, 7
    };

    /*
     * If n is not divisible by 2, 3, 5 or 7, then n + stride[n % M] is the
     * smallest number > n that is also not divisible by 2, 3, 5 or 7.  (The 0
     * entries are never read.)
     */
    private static final byte[] STRIDE = {
        0, 10, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 2, 0, 4, 0, 0, 0, 2, 0, 4,
        0, 0, 0, 6, 0, 0, 0, 0, 0, 2,
        0, 6, 0, 0, 0, 0, 0, 4, 0, 0,
        0, 2, 0, 4, 0, 0, 0, 6, 0, 0,
        0, 0, 0, 6, 0, 0, 0, 0, 0, 2,
        0, 6, 0, 0, 0, 0, 0, 4, 0, 0,
        0, 2, 0, 6, 0, 0, 0, 0, 0, 4,
        0, 0, 0, 6, 0, 0, 0, 0, 0, 8,
        0, 0, 0, 0, 0, 0, 0, 4, 0, 0,
        0, 2, 0, 4, 0, 0, 0, 2, 0, 4,
        0, 0, 0, 8, 0, 0, 0, 0, 0, 0,
        0, 6, 0, 0, 0, 0, 0, 4, 0, 0,
        0, 6, 0, 0, 0, 0, 0, 2, 0, 4,
        0, 0, 0, 6, 0, 0, 0, 0, 0, 2,
        0, 6, 0, 0, 0, 0, 0, 6, 0, 0,
        0, 0, 0, 4, 0, 0, 0, 2, 0, 4,
        0, 0, 0, 6, 0, 0, 0, 0, 0, 2,
        0, 6, 0, 0, 0, 0, 0, 4, 0, 0,
        0, 2, 0, 4, 0, 0, 0, 2, 0, 10,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 2
    };

    /*
     * n + next_prime_step[n % M], is the smallest number >= n that is not
     * divisible by 2, 3, 5 or 7.  It follows that next_prime_step[n % M] != 0
     * iff n is divisible by 2, 3, 5 or 7.
    */
    private static final byte[] NEXT_PRIME_STEP = {
        1, 0, 9, 8, 7, 6, 5, 4, 3, 2,
        1, 0, 1, 0, 3, 2, 1, 0, 1, 0,
        3, 2, 1, 0, 5, 4, 3, 2, 1, 0,
        1, 0, 5, 4, 3, 2, 1, 0, 3, 2,
        1, 0, 1, 0, 3, 2, 1, 0, 5, 4,
        3, 2, 1, 0, 5, 4, 3, 2, 1, 0,
        1, 0, 5, 4, 3, 2, 1, 0, 3, 2,
        1, 0, 1, 0, 5, 4, 3, 2, 1, 0,
        3, 2, 1, 0, 5, 4, 3, 2, 1, 0,
        7, 6, 5, 4, 3, 2, 1, 0, 3, 2,
        1, 0, 1, 0, 3, 2, 1, 0, 1, 0,
        3, 2, 1, 0, 7, 6, 5, 4, 3, 2,
        1, 0, 5, 4, 3, 2, 1, 0, 3, 2,
        1, 0, 5, 4, 3, 2, 1, 0, 1, 0,
        3, 2, 1, 0, 5, 4, 3, 2, 1, 0,
        1, 0, 5, 4, 3, 2, 1, 0, 5, 4,
        3, 2, 1, 0, 3, 2, 1, 0, 1, 0,
        3, 2, 1, 0, 5, 4, 3, 2, 1, 0,
        1, 0, 5, 4, 3, 2, 1, 0, 3, 2,
        1, 0, 1, 0, 3, 2, 1, 0, 1, 0,
        9, 8, 7, 6, 5, 4, 3, 2, 1, 0
    };

    private int[] primes = new int[0];
    private int top;
    private int next = 2;

    boolean isPrime11(int n) {
        int i;
        int p;

        i = 4;                                /* start testing from 11 */
        for (;;) {
            p = getPrime(i++);
            if (UnsignedInts.compare(p * p, n) > 0) {
                return true;
            }
            if (UnsignedInts.remainder(n, p) == 0) {
                return false;
            }
        }
    }

    int getPrime(int i) {
        if (i >= top) {
            if (i >= primes.length) {
                primes = Arrays.copyOf(primes, primes.length * 2 + 5);
                if (top < 5) {
                    primes[0] = 2;
                    primes[1] = 3;
                    primes[2] = 5;
                    primes[3] = 7;
                    primes[4] = 11;
                    top = 5;
                    next = 13;
                }
            }
            while (i >= top) {
                if (isPrime11(next)) {
                    primes[top++] = next;
                }
                next += STRIDE[next % M];
            }
        }
        return primes[i];
    }


    boolean isPrime(int n) {
  /* For n < BASE_TABLE_LENGTH, we just know the answer: */
        if (n < IS_PRIME_TABLE.length) {
            return IS_PRIME_TABLE[n];
        }
  /* then a quick test for divisibility by 2, 3, 5 or 7: */
        if (NEXT_PRIME_STEP[n % M] != 0) {
            return false;
        }
  /* and from here on, we test: */
        return isPrime11(n);
    }


    int nextPrime(int n) {
  /* For n < BASE_TABLE_LENGTH, we just know the answer: */
        if (n < NEXT_PRIME_TABLE.length) {
            return NEXT_PRIME_TABLE[n];
        }
  /* Otherwise we advance to the next possibility and start testing: */
        n += NEXT_PRIME_STEP[n % M];
        while (!isPrime11(n)) {
            n += STRIDE[n % M];
        }
        return n;
    }
}
