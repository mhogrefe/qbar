package jas.arith;

import java.math.BigInteger;
import java.util.Random;

/**
 * ModIntegerRing factory with RingFactory interface. Effectively immutable.
 *
 * @author Heinz Kredel
 */
public final class ModIntegerRing implements ModularRingFactory<ModInteger> {
    /**
     * Module part of the factory data structure.
     */
    public final BigInteger modul;

    /**
     * Indicator if this ring is a field.
     */
    private int isField = -1; // initially unknown

    /*
     * Certainty if module is probable prime.
     */
    //private int certainty = 10;

    /**
     * The constructor creates a ModIntegerRing object from a JasBigInteger object
     * as module part.
     *
     * @param m math.JasBigInteger.
     */
    public ModIntegerRing(BigInteger m) {
        modul = m;
    }

    /**
     * The constructor creates a ModIntegerRing object from a JasBigInteger object
     * as module part.
     *
     * @param m       math.JasBigInteger.
     * @param isField indicator if m is prime.
     */
    public ModIntegerRing(BigInteger m, boolean isField) {
        modul = m;
        this.isField = 1;
    }

    /**
     * Get the module part as JasBigInteger.
     *
     * @return modul.
     */
    public JasBigInteger getIntegerModul() {
        return new JasBigInteger(modul);
    }

    /**
     * Get the zero element.
     *
     * @return 0 as ModInteger.
     */
    public ModInteger getZERO() {
        return new ModInteger(this, BigInteger.ZERO);
    }

    /**
     * Get the one element.
     *
     * @return 1 as ModInteger.
     */
    public ModInteger getONE() {
        return new ModInteger(this, BigInteger.ONE);
    }

    /**
     * Is this structure finite or infinite.
     *
     * @return true if this structure is finite, else false.
     * @see jas.structure.ElemFactory#isFinite()
     */
    public boolean isFinite() {
        return true;
    }

    /**
     * Query if this ring is a field.
     *
     * @return true if module is prime, else false.
     */
    public boolean isField() {
        if (isField > 0) {
            return true;
        }
        if (isField == 0) {
            return false;
        }
        //System.out.println("isProbablePrime " + modul + " = " + modul.isProbablePrime(certainty));
        // if ( modul.isProbablePrime(certainty) ) {
        if (modul.isProbablePrime(modul.bitLength())) {
            isField = 1;
            return true;
        }
        isField = 0;
        return false;
    }

    /**
     * Characteristic of this ring.
     *
     * @return characteristic of this ring.
     */
    public BigInteger characteristic() {
        return modul;
    }

    /**
     * Get a ModInteger element from a JasBigInteger value.
     *
     * @param a JasBigInteger.
     * @return a ModInteger.
     */
    public ModInteger fromInteger(BigInteger a) {
        return new ModInteger(this, a);
    }

    /**
     * Get a ModInteger element from a long value.
     *
     * @param a long.
     * @return a ModInteger.
     */
    public ModInteger fromInteger(long a) {
        return new ModInteger(this, a);
    }

    /**
     * Comparison with any other object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object b) {
        if (!(b instanceof ModIntegerRing)) {
            return false;
        }
        ModIntegerRing m = (ModIntegerRing) b;
        return (0 == modul.compareTo(m.modul));
    }

    /**
     * Hash code for this ModIntegerRing.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return modul.hashCode();
    }

    /**
     * ModInteger random.
     *
     * @param n   such that 0 &le; v &le; (2<sup>n</sup>-1).
     * @param rnd is a source for random bits.
     * @return a random integer mod modul.
     */
    public ModInteger random(int n, Random rnd) {
        BigInteger v = new BigInteger(n, rnd);
        return new ModInteger(this, v);
    }
}
