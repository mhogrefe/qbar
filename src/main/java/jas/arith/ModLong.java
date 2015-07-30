package jas.arith;

import jas.structure.RingElem;

import java.math.BigInteger;

/**
 * ModLong class with RingElem interface. Objects of this class are immutable.
 *
 * @author Heinz Kredel
 * @see ModInteger
 */

public final class ModLong implements RingElem<ModLong>, Modular {
    /**
     * ModLongRing reference.
     */
    public final ModLongRing ring;

    /**
     * Value part of the element data structure.
     */
    public final long val;

    /**
     * The constructor creates a ModLong object from a ModLongRing and a value
     * part.
     *
     * @param m ModLongRing.
     * @param a math.JasBigInteger.
     */
    public ModLong(ModLongRing m, BigInteger a) {
        this(m, a.mod(m.getModul()).longValue());
    }

    /**
     * The constructor creates a ModLong object from a ModLongRing and a long
     * value part.
     *
     * @param m ModLongRing.
     * @param a long.
     */
    public ModLong(ModLongRing m, long a) {
        ring = m;
        long v = a % ring.modul;
        val = (v >= 0L ? v : v + ring.modul);
    }

    /**
     * The constructor creates a ModLong object from a ModLongRing and a Long
     * value part.
     *
     * @param m ModLongRing.
     * @param a Long.
     */
    private ModLong(ModLongRing m, Long a) {
        this(m, a.longValue());
    }

    /**
     * The constructor creates a ModLong object from a ModLongRing and a String
     * value part.
     *
     * @param m ModLongRing.
     * @param s String.
     */
    public ModLong(ModLongRing m, String s) {
        this(m, new Long(s.trim()));
    }

    /**
     * Get the corresponding element factory.
     *
     * @return factory for this Element.
     */
    public ModLongRing factory() {
        return ring;
    }

    /**
     * Return a symmetric JasBigInteger from this Element.
     *
     * @return a symmetric JasBigInteger of this.
     */
    public JasBigInteger getSymmetricInteger() {
        long v = val;
        if ((val + val) > ring.modul) {
            // val > m/2 as 2*val > m, make symmetric to 0
            v = val - ring.modul;
        }
        return new JasBigInteger(v);
    }

    /**
     * Is ModLong number zero.
     *
     * @return If this is 0 then true is returned, else false.
     * @see jas.structure.RingElem#isZERO()
     */


    /**
     * Is ModLong number one.
     *
     * @return If this is 1 then true is returned, else false.
     * @see jas.structure.RingElem#isONE()
     */
    public boolean isONE() {
        return val == 1L;
    }

    /**
     * Is ModLong number a unit.
     *
     * @return If this is a unit then true is returned, else false.
     * @see jas.structure.RingElem#isUnit()
     */
    public boolean isUnit() {
        if (isZERO()) {
            return false;
        }
        if (ring.isField()) {
            return true;
        }
        long g = gcd(ring.modul, val);
        return (g == 1L || g == -1L);
    }

    @Override
    public String toString() {
        return Long.toString(val);
    }

    @Override
    public int compareTo(ModLong b) {
        long v = b.val;
        if (ring != b.ring) {
            v = v % ring.modul;
        }
        if (val > v) {
            return 1;
        }
        return (val < v ? -1 : 0);
    }

    @Override
    public boolean equals(Object b) {
        return b instanceof ModLong && (0 == compareTo((ModLong) b));
    }

    /**
     * Hash code for this ModLong.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (int) val;
    }

    /**
     * ModLong absolute value.
     *
     * @return the absolute value of this.
     * @see jas.structure.RingElem#abs()
     */
    public ModLong abs() {
        return new ModLong(ring, (val < 0 ? -val : val));
    }

    /**
     * ModLong negative.
     *
     * @return -this.
     * @see jas.structure.RingElem#negate()
     */
    public ModLong negate() {
        return new ModLong(ring, -val);
    }


    /**
     * ModLong signum.
     *
     * @return signum(this).
     * @see jas.structure.RingElem#signum()
     */
    public int signum() {
        if (val > 0L) {
            return 1;
        }
        return (val < 0L ? -1 : 0);
    }

    /**
     * ModLong subtraction.
     *
     * @param S ModLong.
     * @return this-S.
     */
    public ModLong subtract(ModLong S) {
        return new ModLong(ring, val - S.val);
    }

    /**
     * ModLong divide.
     *
     * @param S ModLong.
     * @return this/S.
     */
    public ModLong divide(ModLong S) {
        try {
            return multiply(S.inverse());
        } catch (ArithmeticException e) {
            try {
                if ((val % S.val) == 0L) {
                    return new ModLong(ring, val / S.val);
                }
                throw new ArithmeticException(e.getCause().toString());
            } catch (ArithmeticException a) {
                throw new ArithmeticException(a.getCause().toString());
            }
        }
    }

    /**
     * ModLong inverse.
     *
     * @return S with S=1/this if defined.
     * @see jas.structure.RingElem#inverse()
     */
    public ModLong inverse() /*throws NotInvertibleException*/ {
        try {
            return new ModLong(ring, modInverse(val, ring.modul));
        } catch (ArithmeticException e) {
            gcd(val, ring.modul);
            throw e;
        }
    }

    /**
     * ModLong remainder.
     *
     * @param S ModLong.
     * @return remainder(this, S).
     */
    public ModLong remainder(ModLong S) {
        if (S == null || S.isZERO()) {
            throw new ArithmeticException("division by zero");
        }
        if (S.isONE()) {
            return ring.getZERO();
        }
        if (S.isUnit()) {
            return ring.getZERO();
        }
        return new ModLong(ring, val % S.val);
    }

    /**
     * ModLong multiply.
     *
     * @param S ModLong.
     * @return this*S.
     */
    public ModLong multiply(ModLong S) {
        return new ModLong(ring, val * S.val);
    }

    /**
     * ModLong summation.
     *
     * @param S ModLong.
     * @return this+S.
     */
    public ModLong sum(ModLong S) {
        return new ModLong(ring, val + S.val);
    }

    /**
     * ModInteger greatest common divisor.
     *
     * @param S ModInteger.
     * @return [ gcd(this,S), a, b ] with a*this + b*S = gcd(this,S).
     */
    public ModLong gcd(ModLong S) {
        if (S.isZERO()) {
            return this;
        }
        if (isZERO()) {
            return S;
        }
        if (isUnit() || S.isUnit()) {
            return ring.getONE();
        }
        return new ModLong(ring, gcd(val, S.val));
    }

    /**
     * ModInteger extended greatest common divisor.
     *
     * @param S ModInteger.
     * @return [ gcd(this,S), a, b ] with a*this + b*S = gcd(this,S).
     */
    public ModLong[] egcd(ModLong S) {
        ModLong[] ret = new ModLong[3];
        ret[0] = null;
        ret[1] = null;
        ret[2] = null;
        if (S == null || S.isZERO()) {
            ret[0] = this;
            return ret;
        }
        if (isZERO()) {
            ret[0] = S;
            return ret;
        }
        if (isUnit() || S.isUnit()) {
            ret[0] = ring.getONE();
            if (isUnit() && S.isUnit()) {
                //ModLong half = (new ModLong(ring, 2L)).inverse();
                //ret[1] = this.inverse().multiply(half);
                //ret[2] = S.inverse().multiply(half);
                // (1-1*this)/S
                ret[1] = ring.getONE();
                ModLong x = ret[0].subtract(ret[1].multiply(this));
                ret[2] = x.divide(S);
                return ret;
            }
            if (isUnit()) {
                // oder inverse(S-1)?
                ret[1] = this.inverse();
                ret[2] = ring.getZERO();
                return ret;
            }
            // if ( s.isUnit() ) {
            // oder inverse(this-1)?
            ret[1] = ring.getZERO();
            ret[2] = S.inverse();
            return ret;
            //}
        }
        //System.out.println("this = " + this + ", S = " + S);
        long q = this.val;
        long r = S.val;
        long c1 = 1L; // JasBigInteger.ONE.val;
        long d1 = 0L; // JasBigInteger.ZERO.val;
        long c2 = 0L; // JasBigInteger.ZERO.val;
        long d2 = 1L; // JasBigInteger.ONE.val;
        long x1;
        long x2;
        while (r != 0L) {
            //qr = q.divideAndRemainder(r);
            long a = q / r;
            long b = q % r;
            q = a;
            x1 = c1 - q * d1;
            x2 = c2 - q * d2;
            c1 = d1;
            c2 = d2;
            d1 = x1;
            d2 = x2;
            q = r;
            r = b;
        }
        //System.out.println("q = " + q + "\n c1 = " + c1 + "\n c2 = " + c2);
        ret[0] = new ModLong(ring, q);
        ret[1] = new ModLong(ring, c1);
        ret[2] = new ModLong(ring, c2);
        return ret;
    }

    /**
     * Long greatest common divisor.
     *
     * @param T long.
     * @param S long.
     * @return gcd(T, S).
     */
    long gcd(long T, long S) {
        if (S == 0L) {
            return T;
        }
        if (T == 0L) {
            return S;
        }
        long a = T;
        long b = S;
        while (b != 0L) {
            long r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    /**
     * Long half extended greatest common divisor.
     *
     * @param T long.
     * @param S long.
     * @return [ gcd(T,S), a ] with a*T + b*S = gcd(T,S).
     */
    long[] hegcd(long T, long S) {
        long[] ret = new long[2];
        if (S == 0L) {
            ret[0] = T;
            ret[1] = 1L;
            return ret;
        }
        if (T == 0L) {
            ret[0] = S;
            ret[1] = 0L;
            return ret;
        }
        //System.out.println("hegcd, T = " + T + ", S = " + S);
        long a = T;
        long b = S;
        long a1 = 1L;
        long b1 = 0L;
        while (b != 0L) {
            long q = a / b;
            long r = a % b;
            a = b;
            b = r;
            long r1 = a1 - q * b1;
            a1 = b1;
            b1 = r1;
        }
        if (a1 < 0L) {
            a1 += S;
        }
        ret[0] = a;
        ret[1] = a1;
        return ret;
    }

    /**
     * Long modular inverse.
     *
     * @param T long.
     * @param m long.
     * @return a with with a*T = 1 mod m.
     */
    long modInverse(long T, long m) {
        if (T == 0L) {
            throw new ArithmeticException("zero is not invertible");
        }
        long[] hegcd = hegcd(T, m);
        long a = hegcd[0];
        if (!(a == 1L || a == -1L)) { // gcd != 1
            throw new ArithmeticException("element not invertible, gcd != 1");
        }
        long b = hegcd[1];
        if (b == 0L) { // when m divides this, e.g. m.isUnit()
            throw new ArithmeticException("element not invertible, divisible by modul");
        }
        if (b < 0L) {
            b += m;
        }
        return b;
    }
}
