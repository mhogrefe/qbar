package jas.arith;

import jas.structure.Power;
import jas.structure.RingElem;
import jas.structure.RingFactory;

import java.math.BigInteger;
import java.util.Random;

public final class BigRational implements RingElem<BigRational>, RingFactory<BigRational> {
    private final BigInteger num;

    private final BigInteger den;

    private final static BigRational ZERO = new BigRational(BigInteger.ZERO);

    private final static BigRational ONE = new BigRational(BigInteger.ONE);

    private BigRational(BigInteger n, BigInteger d) {
        num = n;
        den = d;
    }

    private BigRational(BigInteger n) {
        num = n;
        den = BigInteger.ONE;
    }

    private static BigRational unit(int d) {
        BigInteger nu = BigInteger.valueOf(1);
        BigInteger de = BigInteger.valueOf(d);
        return RNRED(nu, de);
    }

    public BigRational(long n) {
        num = BigInteger.valueOf(n);
        den = BigInteger.ONE;
    }

    private BigRational(String s) throws NumberFormatException {
        if (s == null) {
            num = BigInteger.ZERO;
            den = BigInteger.ONE;
            return;
        }
        if (s.length() == 0) {
            num = BigInteger.ZERO;
            den = BigInteger.ONE;
            return;
        }
        BigInteger n;
        BigInteger d;
        s = s.trim();
        int i = s.indexOf('/');
        if (i < 0) {
            i = s.indexOf('.');
            if (i < 0) {
                num = new BigInteger(s);
                den = BigInteger.ONE;
                return;
            }
            if (s.charAt(0) == '-') { // case -0.11111
                n = new BigInteger(s.substring(1, i));
            } else {
                n = new BigInteger(s.substring(0, i));
            }
            BigRational r = new BigRational(n);
            d = new BigInteger(s.substring(i + 1, s.length()));
            int j = s.length() - i - 1;
            BigRational z = BigRational.unit(10);
            z = Power.positivePower(z, j);
            BigRational f = new BigRational(d);
            f = f.multiply(z);
            r = r.sum(f);
            if (s.charAt(0) == '-') {
                num = r.num.negate();
            } else {
                num = r.num;
            }
            den = r.den;
        } else {
            n = new BigInteger(s.substring(0, i));
            d = new BigInteger(s.substring(i + 1, s.length()));
            BigRational r = RNRED(n, d);
            num = r.num;
            den = r.den;
        }
    }

    /**
     * Get the corresponding element factory.
     *
     * @return factory for this Element.
     */
    public BigRational factory() {
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
     * Get the string representation.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(num);
        if (!den.equals(BigInteger.ONE)) {
            s.append("/").append(den);
        }
        return s.toString();
    }

    /**
     * Get the zero element.
     *
     * @return 0 as BigRational.
     */
    public BigRational getZERO() {
        return ZERO;
    }

    /**
     * Get the one element.
     *
     * @return 1 as BigRational.
     */
    public BigRational getONE() {
        return ONE;
    }

    /**
     * Query if this ring is a field.
     *
     * @return true.
     */
    public boolean isField() {
        return true;
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
     * Get a BigRational element from a math.JasBigInteger.
     *
     * @param a math.JasBigInteger.
     * @return BigRational from a.
     */
    public BigRational fromInteger(BigInteger a) {
        return new BigRational(a);
    }

    /**
     * Get a BigRational element from a long.
     *
     * @param a long.
     * @return BigRational from a.
     */
    public BigRational fromInteger(long a) {
        return new BigRational(a);
    }

    /**
     * Is BigRational zero.
     *
     * @return If this is 0 then true is returned, else false.
     * @see jas.structure.RingElem#isZERO()
     */
    public boolean isZERO() {
        return num.equals(BigInteger.ZERO);
    }

    /**
     * Is BigRational one.
     *
     * @return If this is 1 then true is returned, else false.
     * @see jas.structure.RingElem#isONE()
     */
    public boolean isONE() {
        return num.equals(den);
    }

    /**
     * Is BigRational unit.
     *
     * @return If this is a unit then true is returned, else false.
     * @see jas.structure.RingElem#isUnit()
     */
    public boolean isUnit() {
        return (!isZERO());
    }

    /**
     * Comparison with any other object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object b) {
        if (!(b instanceof BigRational)) {
            return false;
        }
        BigRational br = (BigRational) b;
        return num.equals(br.num) && den.equals(br.den);
    }

    @Override
    public int hashCode() {
        return 37 * num.hashCode() + den.hashCode();
    }

    private static BigRational RNRED(BigInteger n, BigInteger d) {
        BigInteger num;
        BigInteger den;
        if (n.equals(BigInteger.ZERO)) {
            num = n;
            den = BigInteger.ONE;
            return new BigRational(num, den);
        }
        BigInteger C = n.gcd(d);
        num = n.divide(C);
        den = d.divide(C);
        if (den.signum() < 0) {
            num = num.negate();
            den = den.negate();
        }
        return new BigRational(num, den);
    }

    public BigRational abs() {
        if (this.signum() >= 0) {
            return this;
        }
        return this.negate();
    }

    @Override
    public int compareTo(BigRational S) {
        BigInteger J2Y;
        BigInteger J3Y;
        BigInteger R1;
        BigInteger R2;
        BigInteger S1;
        BigInteger S2;
        int J1Y;
        int SL;
        int TL;
        int RL;
        if (this.equals(ZERO)) {
            return -S.signum();
        }
        if (S.equals(ZERO)) {
            return this.signum();
        }
        R1 = num; //this.numerator();
        R2 = den; //this.denominator();
        S1 = S.num;
        S2 = S.den;
        RL = R1.signum();
        SL = S1.signum();
        J1Y = (RL - SL);
        TL = (J1Y / 2);
        if (TL != 0) {
            return TL;
        }
        J3Y = R1.multiply(S2);
        J2Y = R2.multiply(S1);
        TL = J3Y.compareTo(J2Y);
        return TL;
    }

    /**
     * Rational number difference.
     *
     * @param S BigRational.
     * @return this-S.
     */
    public BigRational subtract(BigRational S) {
        return this.sum(S.negate());
    }

    /**
     * Rational number inverse.
     *
     * @return 1/this.
     * @see jas.structure.RingElem#inverse()
     */
    public BigRational inverse() {
        BigInteger R1 = num;
        BigInteger R2 = den;
        BigInteger S1;
        BigInteger S2;
        if (R1.signum() >= 0) {
            S1 = R2;
            S2 = R1;
        } else {
            S1 = R2.negate();
            S2 = R1.negate();
        }
        return new BigRational(S1, S2);
    }

    /**
     * Rational number negative.
     *
     * @return -this.
     * @see jas.structure.RingElem#negate()
     */
    public BigRational negate() {
        BigInteger n = num.negate();
        return new BigRational(n, den);
    }

    /**
     * Rational number product.
     *
     * @param S BigRational.
     * @return this*S.
     */
    public BigRational multiply(BigRational S) {
        BigInteger D1;
        BigInteger D2;
        BigInteger R1;
        BigInteger R2;
        BigInteger RB1;
        BigInteger RB2;
        BigInteger S1;
        BigInteger S2;
        BigInteger SB1;
        BigInteger SB2;
        BigRational T;
        BigInteger T1;
        BigInteger T2;
        if (this.equals(ZERO) || S.equals(ZERO)) {
            T = ZERO;
            return T;
        }
        R1 = num; //this.numerator();
        R2 = den; //this.denominator();
        S1 = S.num;
        S2 = S.den;
        if (R2.equals(BigInteger.ONE) && S2.equals(BigInteger.ONE)) {
            T1 = R1.multiply(S1);
            T = new BigRational(T1, BigInteger.ONE);
            return T;
        }
        if (R2.equals(BigInteger.ONE)) {
            D1 = R1.gcd(S2);
            RB1 = R1.divide(D1);
            SB2 = S2.divide(D1);
            T1 = RB1.multiply(S1);
            T = new BigRational(T1, SB2);
            return T;
        }
        if (S2.equals(BigInteger.ONE)) {
            D2 = S1.gcd(R2);
            SB1 = S1.divide(D2);
            RB2 = R2.divide(D2);
            T1 = SB1.multiply(R1);
            T = new BigRational(T1, RB2);
            return T;
        }
        D1 = R1.gcd(S2);
        RB1 = R1.divide(D1);
        SB2 = S2.divide(D1);
        D2 = S1.gcd(R2);
        SB1 = S1.divide(D2);
        RB2 = R2.divide(D2);
        T1 = RB1.multiply(SB1);
        T2 = RB2.multiply(SB2);
        T = new BigRational(T1, T2);
        return T;
    }

    /**
     * Rational number quotient.
     *
     * @param S BigRational.
     * @return this/S.
     */
    public BigRational divide(BigRational S) {
        return multiply(S.inverse());
    }

    /**
     * Rational number remainder.
     *
     * @param S BigRational.
     * @return this-(this/S)*S
     */
    public BigRational remainder(BigRational S) {
        if (S.isZERO()) {
            throw new ArithmeticException("division by zero");
        }
        return ZERO;
    }

    public BigRational random(int n, Random rnd) {
        BigInteger A;
        BigInteger B;
        A = new BigInteger(n, rnd); // always positive
        if (rnd.nextBoolean()) {
            A = A.negate();
        }
        B = new BigInteger(n, rnd); // always positive
        B = B.add(BigInteger.ONE);
        return RNRED(A, B);
    }

    /**
     * Rational number sign.
     *
     * @see jas.structure.RingElem#signum()
     */
    public int signum() {
        return num.signum();
    }

    /**
     * Rational number sum.
     *
     * @param S BigRational.
     * @return this+S.
     */
    public BigRational sum(BigRational S) {
        BigInteger D;
        BigInteger E;
        BigInteger J1Y;
        BigInteger J2Y;
        BigInteger R1;
        BigInteger R2;
        BigInteger RB2;
        BigInteger S1;
        BigInteger S2;
        BigInteger SB2;
        BigRational T;
        BigInteger T1;
        BigInteger T2;
        if (this.equals(ZERO)) {
            T = S;
            return T;
        }
        if (S.equals(ZERO)) {
            T = this;
            return T;
        }
        R1 = num; //this.numerator();
        R2 = den; //this.denominator();
        S1 = S.num;
        S2 = S.den;
        if (R2.equals(BigInteger.ONE) && S2.equals(BigInteger.ONE)) {
            T1 = R1.add(S1);
            T = new BigRational(T1, BigInteger.ONE);
            return T;
        }
        if (R2.equals(BigInteger.ONE)) {
            T1 = R1.multiply(S2);
            T1 = T1.add(S1);
            T = new BigRational(T1, S2);
            return T;
        }
        if (S2.equals(BigInteger.ONE)) {
            T1 = R2.multiply(S1);
            T1 = T1.add(R1);
            T = new BigRational(T1, R2);
            return T;
        }
        D = R2.gcd(S2);
        RB2 = R2.divide(D);
        SB2 = S2.divide(D);
        J1Y = R1.multiply(SB2);
        J2Y = RB2.multiply(S1);
        T1 = J1Y.add(J2Y);
        if (T1.equals(BigInteger.ZERO)) {
            T = ZERO;
            return T;
        }
        if (!D.equals(BigInteger.ONE)) {
            E = T1.gcd(D);
            if (!E.equals(BigInteger.ONE)) {
                T1 = T1.divide(E);
                R2 = R2.divide(E);
            }
        }
        T2 = R2.multiply(SB2);
        T = new BigRational(T1, T2);
        return T;
    }

    /**
     * Rational number greatest common divisor.
     *
     * @param S BigRational.
     * @return gcd(this, S).
     */
    public BigRational gcd(BigRational S) {
        if (S == null || S.isZERO()) {
            return this;
        }
        if (this.isZERO()) {
            return S;
        }
        return ONE;
    }

    /**
     * BigRational extended greatest common divisor.
     *
     * @param S BigRational.
     * @return [ gcd(this,S), a, b ] with a*this + b*S = gcd(this,S).
     */
    public BigRational[] egcd(BigRational S) {
        BigRational[] ret = new BigRational[3];
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
        BigRational half = BigRational.unit(2);
        ret[0] = ONE;
        ret[1] = this.inverse().multiply(half);
        ret[2] = S.inverse().multiply(half);
        return ret;
    }
}
