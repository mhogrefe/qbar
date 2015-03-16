package jas.arith;

import jas.structure.Power;

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


    /**
     * Range of probable primes.
     */
    public static enum Range {
        small, low, medium, large, mersenne
    }


    /**
     * Cache the val list for different size
     */
    private volatile static List<BigInteger> SMALL_LIST = null;


    private volatile static List<BigInteger> LOW_LIST = null;


    private volatile static List<BigInteger> MEDIUM_LIST = null;


    private volatile static List<BigInteger> LARGE_LIST = null;


    private volatile static List<BigInteger> MERSENNE_LIST = null;


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
        this(Range.medium);
    }


    /**
     * Constructor for PrimeList.
     *
     * @param r size range for primes.
     */
    public PrimeList(Range r) {
        // initialize with some known primes, see knuth (2,390)
        switch (r) {
            case small:
                if (SMALL_LIST != null) {
                    val = SMALL_LIST;
                } else {
                    val = new ArrayList<>(50);
                    addSmall();
                    SMALL_LIST = val;
                }
                break;
            case low:
                if (LOW_LIST != null) {
                    val = LOW_LIST;
                } else {
                    val = new ArrayList<>(50);
                    addLow();
                    LOW_LIST = val;
                }
                break;
            default:
            case medium:
                if (MEDIUM_LIST != null) {
                    val = MEDIUM_LIST;
                } else {
                    val = new ArrayList<>(50);
                    addMedium();
                    MEDIUM_LIST = val;
                }
                break;
            case large:
                if (LARGE_LIST != null) {
                    val = LARGE_LIST;
                } else {
                    val = new ArrayList<>(50);
                    addLarge();
                    LARGE_LIST = val;
                }
                break;
            case mersenne:
                if (MERSENNE_LIST != null) {
                    val = MERSENNE_LIST;
                } else {
                    val = new ArrayList<>(50);
                    addMersenne();
                    MERSENNE_LIST = val;
                }
                break;
        }
        last = get(size() - 1);
    }


    /**
     * Add small primes.
     */
    private void addSmall() {
        // really small
        val.add(BigInteger.valueOf(2L));
        val.add(BigInteger.valueOf(3L));
        val.add(BigInteger.valueOf(5L));
        val.add(BigInteger.valueOf(7L));
        val.add(BigInteger.valueOf(11L));
        val.add(BigInteger.valueOf(13L));
        val.add(BigInteger.valueOf(17L));
        val.add(BigInteger.valueOf(19L));
        val.add(BigInteger.valueOf(23L));
        val.add(BigInteger.valueOf(29L));
    }


    /**
     * Add low sized primes.
     */
    private void addLow() {
        // 2^15-x
        val.add(getLongPrime(15, 19));
        val.add(getLongPrime(15, 49));
        val.add(getLongPrime(15, 51));
        val.add(getLongPrime(15, 55));
        val.add(getLongPrime(15, 61));
        val.add(getLongPrime(15, 75));
        val.add(getLongPrime(15, 81));
        val.add(getLongPrime(15, 115));
        val.add(getLongPrime(15, 121));
        val.add(getLongPrime(15, 135));
        // 2^16-x
        val.add(getLongPrime(16, 15));
        val.add(getLongPrime(16, 17));
        val.add(getLongPrime(16, 39));
        val.add(getLongPrime(16, 57));
        val.add(getLongPrime(16, 87));
        val.add(getLongPrime(16, 89));
        val.add(getLongPrime(16, 99));
        val.add(getLongPrime(16, 113));
        val.add(getLongPrime(16, 117));
        val.add(getLongPrime(16, 123));
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


    /**
     * Add large sized primes.
     */
    private void addLarge() {
        // 2^59-x
        val.add(getLongPrime(59, 55));
        val.add(getLongPrime(59, 99));
        val.add(getLongPrime(59, 225));
        val.add(getLongPrime(59, 427));
        val.add(getLongPrime(59, 517));
        val.add(getLongPrime(59, 607));
        val.add(getLongPrime(59, 649));
        val.add(getLongPrime(59, 687));
        val.add(getLongPrime(59, 861));
        val.add(getLongPrime(59, 871));
        // 2^60-x
        val.add(getLongPrime(60, 93));
        val.add(getLongPrime(60, 107));
        val.add(getLongPrime(60, 173));
        val.add(getLongPrime(60, 179));
        val.add(getLongPrime(60, 257));
        val.add(getLongPrime(60, 279));
        val.add(getLongPrime(60, 369));
        val.add(getLongPrime(60, 395));
        val.add(getLongPrime(60, 399));
        val.add(getLongPrime(60, 453));
        // 2^63-x
        val.add(getLongPrime(63, 25));
        val.add(getLongPrime(63, 165));
        val.add(getLongPrime(63, 259));
        val.add(getLongPrime(63, 301));
        val.add(getLongPrime(63, 375));
        val.add(getLongPrime(63, 387));
        val.add(getLongPrime(63, 391));
        val.add(getLongPrime(63, 409));
        val.add(getLongPrime(63, 457));
        val.add(getLongPrime(63, 471));
        // 2^64-x not possible
    }


    /**
     * Add Mersenne sized primes.
     */
    private void addMersenne() {
        // 2^n-1
        val.add(getMersennePrime(2));
        val.add(getMersennePrime(3));
        val.add(getMersennePrime(5));
        val.add(getMersennePrime(7));
        val.add(getMersennePrime(13));
        val.add(getMersennePrime(17));
        val.add(getMersennePrime(19));
        val.add(getMersennePrime(31));
        val.add(getMersennePrime(61));
        val.add(getMersennePrime(89));
        val.add(getMersennePrime(107));
        val.add(getMersennePrime(127));
        val.add(getMersennePrime(521));
        val.add(getMersennePrime(607));
        val.add(getMersennePrime(1279));
        val.add(getMersennePrime(2203));
        val.add(getMersennePrime(2281));
        val.add(getMersennePrime(3217));
        val.add(getMersennePrime(4253));
        val.add(getMersennePrime(4423));
        val.add(getMersennePrime(9689));
        val.add(getMersennePrime(9941));
        val.add(getMersennePrime(11213));
        val.add(getMersennePrime(19937));
    }

    private static BigInteger getLongPrime(int n, int m) {
        long prime = 2; // knuth (2,390)
        for (int i = 1; i < n; i++) {
            prime *= 2;
        }
        prime -= m;
        return BigInteger.valueOf(prime);
    }

    private static BigInteger getMersennePrime(int n) {
        JasBigInteger t = new JasBigInteger(2);
        JasBigInteger p = Power.positivePower(t, n);
        p = p.subtract(new JasBigInteger(1));
        return p.getVal();
    }

    /**
     * toString.
     */
    @Override
    public String toString() {
        return val.toString();
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
        return new Iterator<BigInteger>() {


            int index = -1;


            public boolean hasNext() {
                return true;
            }


            public void remove() {
                throw new UnsupportedOperationException("remove not implemented");
            }


            public BigInteger next() {
                index++;
                return get(index);
            }
        };
    }

}
