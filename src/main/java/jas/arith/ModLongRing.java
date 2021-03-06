package jas.arith;

import java.math.BigInteger;
import java.util.Random;

/**
 * ModLongRing factory with RingFactory interface. Effectively immutable.
 *
 * @author Heinz Kredel
 */
public final class ModLongRing implements ModularRingFactory<ModLong> {
    /**
     * Module part of the factory data structure.
     */
    public final long modul;

    /**
     * Indicator if this ring is a field.
     */
    private int isField = -1; // initially unknown

    /*
     * Certainty if module is probable prime.
     */
    //private final int certainty = 10;

    /**
     * maximal representable integer.
     */
    public final static BigInteger MAX_LONG = new BigInteger(
            String.valueOf(Integer.MAX_VALUE)); // not larger!

    /**
     * The constructor creates a ModLongRing object from a long integer as
     * module part.
     *
     * @param m       long integer.
     */
    public ModLongRing(long m) {
        modul = m;
        this.isField = 1;
    }

    /**
     * The constructor creates a ModLongRing object from a JasBigInteger converted
     * to long as module part.
     *
     * @param m       java.math.JasBigInteger.
     */
    public ModLongRing(BigInteger m) {
        this(m.longValue());
        if (MAX_LONG.compareTo(m) < 0) { // m > max
            throw new IllegalArgumentException("modul to large for long " + m);
        }
    }

    /**
     * Get the module part as JasBigInteger.
     *
     * @return modul.
     */
    public BigInteger getModul() {
        return new BigInteger(Long.toString(modul));
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
     * @return 0 as ModLong.
     */
    public ModLong getZERO() {
        return new ModLong(this, 0L);
    }

    /**
     * Get the one element.
     *
     * @return 1 as ModLong.
     */
    public ModLong getONE() {
        return new ModLong(this, 1L);
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
        BigInteger m = new BigInteger(Long.toString(modul));
        if (m.isProbablePrime(m.bitLength())) {
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
        return new BigInteger(Long.toString(modul));
    }

    /**
     * Get a ModLong element from a JasBigInteger value.
     *
     * @param a JasBigInteger.
     * @return a ModLong.
     */
    public ModLong fromInteger(BigInteger a) {
        return new ModLong(this, a);
    }

    /**
     * Get a ModLong element from a long value.
     *
     * @param a long.
     * @return a ModLong.
     */
    public ModLong fromInteger(long a) {
        return new ModLong(this, a);
    }

    /**
     * Comparison with any other object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object b) {
        if (!(b instanceof ModLongRing)) {
            return false;
        }
        ModLongRing m = (ModLongRing) b;
        return (modul == m.modul);
    }

    /**
     * Hash code for this ModLongRing.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (int) modul;
    }

    /**
     * ModLong random.
     *
     * @param n   such that 0 &le; v &le; (2<sup>n</sup>-1).
     * @param rnd is a source for random bits.
     * @return a random integer mod modul.
     */
    public ModLong random(int n, Random rnd) {
        BigInteger v = new BigInteger(n, rnd);
        return new ModLong(this, v); // rnd.nextLong() not ok
    }
}
