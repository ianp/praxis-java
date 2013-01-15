// Copyright © 2013 Ian Phillips.
// See the accompanying LICENSE.md file for further information.
package org.ianp.praxis;

import java.util.BitSet;

/**
 * The Sieve of Eratosthenes.
 * <p>
 * A solution to <a href="http://programmingpraxis.com/2009/02/19/sieve-of-eratosthenes/">this problem</a>.
 */
public class SieveOfEratosthenes {

    public static BitSet sieve(int target) {
        BitSet primes = new BitSet(target);
        if (target < 2) { return primes; }
        primes.set(2);
        if (target < 3) { return primes; }
        for (int i = 3; i <= target; i += 2) {
            primes.set(i);
        }
        for (int prime = 3; prime * prime < target; prime = primes.nextSetBit(prime + 1)) {
            for (int i = prime + prime; i <= target; i += prime) {
                primes.clear(i);
            }
        }
        return primes;
    }

    public static void main(String[] args) {
        long target = Long.parseLong(args[0]);
        if (target < 1) {
            System.err.println("target too small");
            System.exit(1);
        }
        if (target > Integer.MAX_VALUE) {
            System.err.println("target too big");
            System.exit(2);
        }
        System.console().format("%s%n", sieve((int) target).cardinality());
    }

}

