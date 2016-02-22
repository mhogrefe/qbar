package jas.arith;

import mho.wheels.iterables.NoRemoveIterator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * List of big primes. Provides an Iterator for generating prime numbers.
 * Similar to ALDES/SAC2 SACPOL.PRIME list.
 *
 * @author Heinz Kredel See Knuth vol 2,page 390, for list of known primes. See
 *         also ALDES/SAC2 SACPOL.PRIME
 */
public final class PrimeList implements Iterable<BigInteger> {
    private volatile static List<BigInteger> MEDIUM_LIST = null;

    /**
     * The list of probable primes in requested range.
     */
    private List<BigInteger> val = null;

    /**
     * The last prime in the list.
     */
    private BigInteger last;

    /**
     * Constructor for PrimeList.
     */
    public PrimeList() {
        // initialize with some known primes, see knuth (2,390)
        if (MEDIUM_LIST != null) {
            val = MEDIUM_LIST;
        } else {
            val = new ArrayList<>(50);
            addMedium();
            MEDIUM_LIST = val;
        }
        last = get(size() - 1);
    }

    /**
     * Add medium sized primes.
     */
    private void addMedium() {
        // 2^28-x
        val.add(getLongPrime(28, 57));
        val.add(getLongPrime(28, 89));
        val.add(getLongPrime(28, 95));
        val.add(getLongPrime(28, 119));
        val.add(getLongPrime(28, 125));
        val.add(getLongPrime(28, 143));
        val.add(getLongPrime(28, 165));
        val.add(getLongPrime(28, 183));
        val.add(getLongPrime(28, 213));
        val.add(getLongPrime(28, 273));
        // 2^29-x
        val.add(getLongPrime(29, 3));
        val.add(getLongPrime(29, 33));
        val.add(getLongPrime(29, 43));
        val.add(getLongPrime(29, 63));
        val.add(getLongPrime(29, 73));
        val.add(getLongPrime(29, 75));
        val.add(getLongPrime(29, 93));
        val.add(getLongPrime(29, 99));
        val.add(getLongPrime(29, 121));
        val.add(getLongPrime(29, 133));
        // 2^32-x
        val.add(getLongPrime(32, 5));
        val.add(getLongPrime(32, 17));
        val.add(getLongPrime(32, 65));
        val.add(getLongPrime(32, 99));
        val.add(getLongPrime(32, 107));
        val.add(getLongPrime(32, 135));
        val.add(getLongPrime(32, 153));
        val.add(getLongPrime(32, 185));
        val.add(getLongPrime(32, 209));
        val.add(getLongPrime(32, 267));
    }

    private static BigInteger getLongPrime(int n, int m) {
        long prime = 2; // knuth (2,390)
        for (int i = 1; i < n; i++) {
            prime *= 2;
        }
        prime -= m;
        return BigInteger.valueOf(prime);
    }

    /**
     * size of current list.
     */
    int size() {
        return val.size();
    }

    /**
     * get prime at index i.
     */
    BigInteger get(int i) {
        BigInteger p;
        if (i < size()) {
            p = val.get(i);
        } else {
            p = last.nextProbablePrime();
            val.add(p);
            last = p;
        }
        return p;
    }

    /**
     * Iterator.
     */
    public Iterator<BigInteger> iterator() {
        return new NoRemoveIterator<BigInteger>() {
            int index = -1;

            public boolean hasNext() {
                return true;
            }

            public BigInteger next() {
                index++;
                return get(index);
            }
        };
    }
}
