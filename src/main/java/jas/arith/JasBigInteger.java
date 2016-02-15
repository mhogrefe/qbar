package jas.arith;

import jas.structure.RingElem;
import jas.structure.RingFactory;

import java.math.BigInteger;
import java.util.Random;

public final class JasBigInteger implements RingElem<JasBigInteger>, RingFactory<JasBigInteger> {
    /**
     * The data structure.
     */
    public final BigInteger val;

    /**
     * The constant 0.
     */
    public final static JasBigInteger ZERO = new JasBigInteger(BigInteger.ZERO);

    /**
     * The constant 1.
     */
    public final static JasBigInteger ONE = new JasBigInteger(BigInteger.ONE);

    /**
     * Constructor for JasBigInteger from math.JasBigInteger.
     *
     * @param a java.math.JasBigInteger.
     */
    public JasBigInteger(BigInteger a) {
        val = a;
    }

    /**
     * Constructor for JasBigInteger from long.
     *
     * @param a long.
     */
    public JasBigInteger(long a) {
        val = new BigInteger(String.valueOf(a));
    }

    /**
     * Constructor for JasBigInteger from String.
     *
     * @param s String.
     */
    private JasBigInteger(String s) {
        val = new BigInteger(s.trim());
    }

    /**
     * Constructor for JasBigInteger without parameters.
     */
    public JasBigInteger() {
        val = BigInteger.ZERO;
    }

    /**
     * Get the value.
     *
     * @return val java.math.JasBigInteger.
     */
    public BigInteger getVal() {
        return val;
    }

    /**
     * Get the value as long.
     *
     * @return val as long.
     */
    public long longValue() {
        return val.longValue();
    }

    /**
     * Get the corresponding element factory.
     *
     * @return factory for this Element.
     */
    public JasBigInteger factory() {
        return this;
    }

    /**
     * Is this structure finite or infinite.
     *
     * @return true if this structure is finite, else false.
     * @see jas.structure.ElemFactory#isFinite()
     */
    public boolean isFinite() {
        return false;
    }

    /**
     * Get the zero element.
     *
     * @return 0.
     */
    public JasBigInteger getZERO() {
        return ZERO;
    }

    /**
     * Get the one element.
     *
     * @return 1.
     */
    public JasBigInteger getONE() {
        return ONE;
    }

    /**
     * Query if this ring is a field.
     *
     * @return false.
     */
    public boolean isField() {
        return false;
    }

    /**
     * Characteristic of this ring.
     *
     * @return characteristic of this ring.
     */
    public BigInteger characteristic() {
        return BigInteger.ZERO;
    }

    /**
     * Get a JasBigInteger element from a math.JasBigInteger.
     *
     * @param a math.JasBigInteger.
     * @return a as JasBigInteger.
     */
    public JasBigInteger fromInteger(BigInteger a) {
        return new JasBigInteger(a);
    }

    /**
     * Get a JasBigInteger element from long.
     *
     * @param a long.
     * @return a as JasBigInteger.
     */
    public JasBigInteger fromInteger(long a) {
        return new JasBigInteger(a);
    }

    /**
     * Is JasBigInteger number zero.
     *
     * @return If this is 0 then true is returned, else false.
     * @see jas.structure.RingElem#isZERO()
     */
    public boolean isZERO() {
        return val.equals(BigInteger.ZERO);
    }

    /**
     * Is JasBigInteger number one.
     *
     * @see jas.structure.RingElem#isONE()
     */
    public boolean isONE() {
        return val.equals(BigInteger.ONE);
    }

    /**
     * Is JasBigInteger number unit.
     *
     * @see jas.structure.RingElem#isUnit()
     */
    public boolean isUnit() {
        return (this.isONE() || this.negate().isONE());
    }

    //
    //Compare to JasBigInteger b.
    //
    //@param b JasBigInteger.
    //@return 0 if this == b, 1 if this > b, -1 if this < b.
    //
    @Override
    public int compareTo(JasBigInteger b) {
        return val.compareTo(b.val);
    }

    /**
     * Comparison with any other object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object b) {
        if (!(b instanceof JasBigInteger)) {
            return false;
        }
        JasBigInteger bi = (JasBigInteger) b;
        return val.equals(bi.val);
    }

    /**
     * Hash code for this JasBigInteger.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return val.hashCode();
    }

    /**
     * Absolute value of this.
     *
     * @see jas.structure.RingElem#abs()
     */
    public JasBigInteger abs() {
        return new JasBigInteger(val.abs());
    }

    /* Negative value of this.
     * @see jas.structure.RingElem#negate()
     */
    public JasBigInteger negate() {
        return new JasBigInteger(val.negate());
    }

    /**
     * signum.
     *
     * @see jas.structure.RingElem#signum()
     */
    public int signum() {
        return val.signum();
    }

    /**
     * JasBigInteger subtract.
     *
     * @param S JasBigInteger.
     * @return this-S.
     */
    public JasBigInteger subtract(JasBigInteger S) {
        return new JasBigInteger(val.subtract(S.val));
    }

    /**
     * JasBigInteger divide.
     *
     * @param S JasBigInteger.
     * @return this/S.
     */
    public JasBigInteger divide(JasBigInteger S) {
        return new JasBigInteger(val.divide(S.val));
    }

    /**
     * Integer inverse. R is a non-zero integer. S=1/R if defined else
     * throws not invertible exception.
     *
     * @see jas.structure.RingElem#inverse()
     */
    public JasBigInteger inverse() {
        if (this.isONE() || this.negate().isONE()) {
            return this;
        }
        //return ZERO;
        throw new ArithmeticException("element not invertible " + this + " :: JasBigInteger");
    }

    /**
     * JasBigInteger remainder.
     *
     * @param S JasBigInteger.
     * @return this - (this/S)*S.
     */
    public JasBigInteger remainder(JasBigInteger S) {
        return new JasBigInteger(val.remainder(S.val));
    }

    /**
     * JasBigInteger compute quotient and remainder. Throws an exception, if S ==
     * 0.
     *
     * @param S JasBigInteger.
     * @return JasBigInteger[] { q, r } with this = q S + r and 0 &le; r &lt; |S|.
     */
    //@Override
    JasBigInteger[] quotientRemainder(JasBigInteger S) {
        JasBigInteger[] qr = new JasBigInteger[2];
        BigInteger[] C = val.divideAndRemainder(S.val);
        qr[0] = new JasBigInteger(C[0]);
        qr[1] = new JasBigInteger(C[1]);
        return qr;
    }

    /**
     * JasBigInteger greatest common divisor.
     *
     * @param S JasBigInteger.
     * @return gcd(this, S).
     */
    public JasBigInteger gcd(JasBigInteger S) {
        return new JasBigInteger(val.gcd(S.val));
    }

    /**
     * JasBigInteger extended greatest common divisor.
     *
     * @param S JasBigInteger.
     * @return [ gcd(this,S), a, b ] with a*this + b*S = gcd(this,S).
     */
    public JasBigInteger[] egcd(JasBigInteger S) {
        JasBigInteger[] ret = new JasBigInteger[3];
        ret[0] = null;
        ret[1] = null;
        ret[2] = null;
        if (S == null || S.isZERO()) {
            ret[0] = this;
            return ret;
        }
        if (this.isZERO()) {
            ret[0] = S;
            return ret;
        }
        //System.out.println("this = " + this + ", S = " + S);
        JasBigInteger[] qr;
        JasBigInteger q = this;
        JasBigInteger r = S;
        JasBigInteger c1 = ONE;
        JasBigInteger d1 = ZERO;
        JasBigInteger c2 = ZERO;
        JasBigInteger d2 = ONE;
        JasBigInteger x1;
        JasBigInteger x2;
        while (!r.isZERO()) {
            qr = q.quotientRemainder(r);
            q = qr[0];
            x1 = c1.subtract(q.multiply(d1));
            x2 = c2.subtract(q.multiply(d2));
            c1 = d1;
            c2 = d2;
            d1 = x1;
            d2 = x2;
            q = r;
            r = qr[1];
        }
        //System.out.println("q = " + q + "\n c1 = " + c1 + "\n c2 = " + c2);
        if (q.signum() < 0) {
            q = q.negate();
            c1 = c1.negate();
            c2 = c2.negate();
        }
        ret[0] = q;
        ret[1] = c1;
        ret[2] = c2;
        return ret;
    }

    /**
     * JasBigInteger random.
     *
     * @param n   such that 0 &le; r &le; (2<sup>n</sup>-1).
     * @param rnd is a source for random bits.
     * @return r, a random JasBigInteger.
     */
    public JasBigInteger random(int n, Random rnd) {
        BigInteger r = new BigInteger(n, rnd);
        if (rnd.nextBoolean()) {
            r = r.negate();
        }
        return new JasBigInteger(r);
    }

    /**
     * JasBigInteger multiply.
     *
     * @param S JasBigInteger.
     * @return this*S.
     */
    public JasBigInteger multiply(JasBigInteger S) {
        return new JasBigInteger(val.multiply(S.val));
    }

    /**
     * JasBigInteger summation.
     *
     * @param S JasBigInteger.
     * @return this+S.
     */
    public JasBigInteger sum(JasBigInteger S) {
        return new JasBigInteger(val.add(S.val));
    }
}
