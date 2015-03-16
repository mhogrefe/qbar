package jas.arith;

import jas.structure.RingElem;

import java.math.BigInteger;

public final class ModInteger implements RingElem<ModInteger>, Modular {
    public final ModIntegerRing ring;

    public final BigInteger val;

    public ModInteger(ModIntegerRing m, BigInteger a) {
        ring = m;
        val = a.mod(ring.modul);
    }

    public ModInteger(ModIntegerRing m, long a) {
        this(m, new BigInteger(String.valueOf(a)));
    }

    public ModInteger(ModIntegerRing m, String s) {
        this(m, new BigInteger(s.trim()));
    }

    public ModIntegerRing factory() {
        return ring;
    }

    public JasBigInteger getSymmetricInteger() {
        BigInteger v = val;
        if (val.add(val).compareTo(ring.modul) > 0) {
            v = val.subtract(ring.modul);
        }
        return new JasBigInteger(v);
    }

    /**
     * Is ModInteger number zero.
     *
     * @return If this is 0 then true is returned, else false.
     * @see jas.structure.RingElem#isZERO()
     */
    public boolean isZERO() {
        return val.equals(BigInteger.ZERO);
    }


    /**
     * Is ModInteger number one.
     *
     * @return If this is 1 then true is returned, else false.
     * @see jas.structure.RingElem#isONE()
     */
    public boolean isONE() {
        return val.equals(BigInteger.ONE);
    }


    /**
     * Is ModInteger number a unit.
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
        BigInteger g = ring.modul.gcd(val).abs();
        return (g.equals(BigInteger.ONE));
    }


    /**
     * Get the String representation.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public int compareTo(ModInteger b) {
        BigInteger v = b.val;
        if (ring != b.ring) {
            v = v.mod(ring.modul);
        }
        return val.compareTo(v);
    }

    @Override
    public boolean equals(Object b) {
        return b instanceof ModInteger && (0 == compareTo((ModInteger) b));
    }


    /**
     * Hash code for this ModInteger.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        //return 37 * val.hashCode();
        return val.hashCode();
    }


    /**
     * ModInteger absolute value.
     *
     * @return the absolute value of this.
     * @see jas.structure.RingElem#abs()
     */
    public ModInteger abs() {
        return new ModInteger(ring, val.abs());
    }

    /**
     * ModInteger negative.
     *
     * @return -this.
     * @see jas.structure.RingElem#negate()
     */
    public ModInteger negate() {
        return new ModInteger(ring, val.negate());
    }

    /**
     * ModInteger signum.
     *
     * @return signum(this).
     * @see jas.structure.RingElem#signum()
     */
    public int signum() {
        return val.signum();
    }

    /**
     * ModInteger subtraction.
     *
     * @param S ModInteger.
     * @return this-S.
     */
    public ModInteger subtract(ModInteger S) {
        return new ModInteger(ring, val.subtract(S.val));
    }

    /**
     * ModInteger divide.
     *
     * @param S ModInteger.
     * @return this/S.
     */
    public ModInteger divide(ModInteger S) {
        try {
            return multiply(S.inverse());
        } catch (ArithmeticException e) {
            try {
                if (val.remainder(S.val).equals(BigInteger.ZERO)) {
                    return new ModInteger(ring, val.divide(S.val));
                }
                throw new ArithmeticException(e.toString());
            } catch (ArithmeticException a) {
                throw new ArithmeticException(a.toString());
            }
        }
    }

    /**
     * ModInteger inverse.
     *
     * @return S with S=1/this if defined.
     * @see jas.structure.RingElem#inverse()
     */
    public ModInteger inverse() /*throws NotInvertibleException*/ {
        try {
            return new ModInteger(ring, val.modInverse(ring.modul));
        } catch (ArithmeticException e) {
            BigInteger g = val.gcd(ring.modul);
            ring.modul.divide(g);
            throw e;
        }
    }

    /**
     * ModInteger remainder.
     *
     * @param S ModInteger.
     * @return remainder(this, S).
     */
    public ModInteger remainder(ModInteger S) {
        if (S == null || S.isZERO()) {
            throw new ArithmeticException("division by zero");
        }
        if (S.isONE()) {
            return ring.getZERO();
        }
        if (S.isUnit()) {
            return ring.getZERO();
        }
        return new ModInteger(ring, val.remainder(S.val));
    }

    /**
     * ModInteger multiply.
     *
     * @param S ModInteger.
     * @return this*S.
     */
    public ModInteger multiply(ModInteger S) {
        return new ModInteger(ring, val.multiply(S.val));
    }

    /**
     * ModInteger summation.
     *
     * @param S ModInteger.
     * @return this+S.
     */
    public ModInteger sum(ModInteger S) {
        return new ModInteger(ring, val.add(S.val));
    }

    /**
     * ModInteger greatest common divisor.
     *
     * @param S ModInteger.
     * @return gcd(this, S).
     */
    public ModInteger gcd(ModInteger S) {
        if (S.isZERO()) {
            return this;
        }
        if (isZERO()) {
            return S;
        }
        if (isUnit() || S.isUnit()) {
            return ring.getONE();
        }
        return new ModInteger(ring, val.gcd(S.val));
    }

    /**
     * ModInteger extended greatest common divisor.
     *
     * @param S ModInteger.
     * @return [ gcd(this,S), a, b ] with a*this + b*S = gcd(this,S).
     */
    public ModInteger[] egcd(ModInteger S) {
        ModInteger[] ret = new ModInteger[3];
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
        if (this.isUnit() || S.isUnit()) {
            ret[0] = ring.getONE();
            if (this.isUnit() && S.isUnit()) {
                ret[1] = ring.getONE();
                ModInteger x = ret[0].subtract(ret[1].multiply(this));
                ret[2] = x.divide(S);
                return ret;
            }
            if (this.isUnit()) {
                ret[1] = this.inverse();
                ret[2] = ring.getZERO();
                return ret;
            }
            ret[1] = ring.getZERO();
            ret[2] = S.inverse();
            return ret;
        }
        BigInteger[] qr;
        BigInteger q = this.val;
        BigInteger r = S.val;
        BigInteger c1 = JasBigInteger.ONE.val;
        BigInteger d1 = JasBigInteger.ZERO.val;
        BigInteger c2 = JasBigInteger.ZERO.val;
        BigInteger d2 = JasBigInteger.ONE.val;
        BigInteger x1;
        BigInteger x2;
        while (!r.equals(BigInteger.ZERO)) {
            qr = q.divideAndRemainder(r);
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
        ret[0] = new ModInteger(ring, q);
        ret[1] = new ModInteger(ring, c1);
        ret[2] = new ModInteger(ring, c2);
        return ret;
    }

}
