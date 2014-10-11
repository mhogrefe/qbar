package mho.qbar;

import mho.haskellesque.iterables.Exhaustive;
import mho.haskellesque.math.BasicMath;
import mho.haskellesque.numbers.Numbers;
import mho.haskellesque.ordering.Ordering;
import mho.haskellesque.tuples.Pair;
import mho.haskellesque.tuples.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

import static mho.haskellesque.iterables.IterableUtils.*;
import static mho.haskellesque.math.Combinatorics.*;
import static mho.haskellesque.ordering.Ordering.*;

/**
 * The <tt>Rational</tt> class uniquely represents rational numbers. <tt>denominator</tt> is the smallest positive
 * integer d such that <tt>this</tt>&#x00D7;d is an integer. <tt>numerator</tt> is the smallest integer n such that
 * n/d equals <tt>this</tt>. This means that n and d have no positive common factor greater than 1, and d is always
 * positive. Arithmetic algorithms taken from Knuth.
 *
 * There is only one instance of ZERO and one instance of ONE, so these may be compared with other <tt>Rational</tt>s
 * using ==.
 */
public final class Rational implements Comparable<Rational> {
    /**
     * 0
     */
    public static final @NotNull Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);
    /**
     * 1
     */
    public static final @NotNull Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);

    /**
     * The smallest positive float value, or 2<sup>&#x2212;149</sup>
     */
    public static final @NotNull Rational SMALLEST_FLOAT = new Rational(BigInteger.ONE, BigInteger.ONE.shiftLeft(149));
    /**
     * The largest subnormal float value, or (2<sup>23</sup>&#x2212;1)/2<sup>149</sup>
     */
    public static final @NotNull Rational LARGEST_SUBNORMAL_FLOAT = new Rational(BigInteger.ONE.shiftLeft(23).subtract(BigInteger.ONE), BigInteger.ONE.shiftLeft(149));
    /**
     * The smallest positive normal float value, or 2<sup>&#x2212;126</sup>
     */
    public static final @NotNull Rational SMALLEST_NORMAL_FLOAT = new Rational(BigInteger.ONE, BigInteger.ONE.shiftLeft(126));
    /**
     * The largest finite float value, or 2<sup>128</sup>&#x2212;2<sup>104</sup>
     */
    public static final @NotNull Rational LARGEST_FLOAT = new Rational(BigInteger.ONE.shiftLeft(128).subtract(BigInteger.ONE.shiftLeft(104)), BigInteger.ONE);
    /**
     * The smallest positive double value, or 2<sup>&#x2212;1074</sup>
     */
    public static final @NotNull Rational SMALLEST_DOUBLE = new Rational(BigInteger.ONE, BigInteger.ONE.shiftLeft(1074));
    /**
     * The largest subnormal double value, or (2<sup>52</sup>&#x2212;1)/2<sup>1074</sup>
     */
    public static final @NotNull Rational LARGEST_SUBNORMAL_DOUBLE = new Rational(BigInteger.ONE.shiftLeft(52).subtract(BigInteger.ONE), BigInteger.ONE.shiftLeft(1074));
    /**
     * The smallest positive normal double value, or 2<sup>&#x2212;1022</sup>
     */
    public static final @NotNull Rational SMALLEST_NORMAL_DOUBLE = new Rational(BigInteger.ONE, BigInteger.ONE.shiftLeft(1022));
    /**
     * The largest finite double value, or 2<sup>1024</sup>&#x2212;2<sup>971</sup>
     */
    public static final @NotNull Rational LARGEST_DOUBLE = new Rational(BigInteger.ONE.shiftLeft(1024).subtract(BigInteger.ONE.shiftLeft(971)), BigInteger.ONE);

    /**
     * <tt>this</tt> times <tt>denominator</tt>
     */
    private final @NotNull BigInteger numerator;
    /**
     * The smallest positive <tt>BigInteger</tt> d such that <tt>this</tt>&#x00D7;d is an integer
     */
    private final @NotNull BigInteger denominator;

    /**
     * Private constructor from <tt>BigInteger</tt>s; assumes arguments are valid
     *
     * <ul>
     *  <li><tt>numerator</tt> cannot be null.</li>
     *  <li><tt>denominator</tt> cannot be null or equal to 0.</li>
     *  <li><tt>numerator</tt> and <tt>denominator</tt> cannot have a positive common factor greater than 1.</li>
     *  <li>Any <tt>Rational</tt> may be constructed with this constructor.</li>
     * </ul>
     *
     * @param numerator the numerator
     * @param denominator the denominator
     */
    private Rational(@NotNull BigInteger numerator, @NotNull BigInteger denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Returns this <tt>Rational</tt>'s numerator
     *
     * <ul>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return the numerator
     */
    public @NotNull BigInteger getNumerator() {
        return numerator;
    }

    /**
     * Returns this <tt>Rational</tt>'s denominator
     *
     * <ul>
     *  <li>The result is positive.</li>
     * </ul>
     *
     * @return the denominator
     */
    public @NotNull BigInteger getDenominator() {
        return denominator;
    }

    /**
     * Creates a <tt>Rational</tt> from <tt>BigInteger</tt>s. Throws an exception if <tt>denominator</tt> is zero.
     * Reduces args and negates denominator if necessary.
     *
     * <ul>
     *  <li><tt>numerator</tt> cannot be null.</li>
     *  <li><tt>denominator</tt> cannot be null or equal to 0.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param numerator the numerator
     * @param denominator the denominator
     * @return the <tt>Rational</tt> corresponding to <tt>numerator</tt>/<tt>denominator</tt>
     * @throws java.lang.ArithmeticException if denominator is zero
     */
    public static @NotNull Rational of(@NotNull BigInteger numerator, @NotNull BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO))
            throw new ArithmeticException("division by zero");
        if (numerator.equals(BigInteger.ZERO)) return ZERO;
        if (numerator.equals(denominator)) return ONE;
        BigInteger gcd = numerator.gcd(denominator);
        if (denominator.signum() < 0) gcd = gcd.negate();
        return new Rational(numerator.divide(gcd), denominator.divide(gcd));
    }

    /**
     * Creates a <tt>Rational</tt> from <tt>ints</tt>. Throws an exception if any args are null or <tt>denominator</tt>
     * is zero. Reduces args and negates denominator if necessary.
     *
     * <ul>
     *  <li><tt>numerator</tt> can be any <tt>int</tt>.</li>
     *  <li><tt>denominator</tt> cannot be equal to 0.</li>
     *  <li>The result is a <tt>Rational</tt> whose numerator and denominator both satisfy
     *  &#x2212;2<sup>31</sup>&#x2264;x&lt;2<sup>31</sup>.</li>
     * </ul>
     *
     * @param numerator the numerator
     * @param denominator the denominator
     * @return the <tt>Rational</tt> corresponding to <tt>numerator</tt>/<tt>denominator</tt>
     */
    public static @NotNull Rational of(int numerator, int denominator) {
        if (denominator == 0)
            throw new ArithmeticException("division by zero");
        if (numerator == 0) return ZERO;
        if (numerator == denominator) return ONE;
        int gcd = BasicMath.gcd(numerator, denominator);
        if (denominator < 0) gcd = -gcd;
        return new Rational(BigInteger.valueOf(numerator / gcd), BigInteger.valueOf(denominator / gcd));
    }

    /**
     * Creates a <tt>Rational</tt> from a <tt>BigInteger</tt>.
     *
     * <ul>
     *  <li><tt>n</tt> cannot be null.</li>
     *  <li>The result is an integral <tt>Rational</tt>.</li>
     * </ul>
     *
     * @param n the integer
     * @return the <tt>Rational</tt> corresponding to <tt>n</tt>
     */
    public static @NotNull Rational of(@NotNull BigInteger n) {
        if (n.equals(BigInteger.ZERO)) return ZERO;
        if (n.equals(BigInteger.ONE)) return ONE;
        return new Rational(n, BigInteger.ONE);
    }

    /**
     * Creates a <tt>Rational</tt> from an <tt>int</tt>.
     *
     * <ul>
     *  <li><tt>n</tt> can be any <tt>int</tt>.</li>
     *  <li>The result is an integral <tt>Rational</tt> satisfying
     *  &#x2212;2<sup>31</sup>&#x2264;x&lt;2<sup>31</sup>.</li>
     * </ul>
     *
     * @param n the integer
     * @return the <tt>Rational</tt> corresponding to <tt>n</tt>
     */
    public static @NotNull Rational of(int n) {
        if (n == 0) return ZERO;
        if (n == 1) return ONE;
        return new Rational(BigInteger.valueOf(n), BigInteger.ONE);
    }

    /**
     * Creates a <tt>Rational</tt> from a <tt>float</tt>. No rounding occurs; the <tt>Rational</tt> has exactly the
     * same value as the <tt>float</tt>. For example, of(1.0f/3.0f) yields 11184811/33554432, not 1/3. Returns
     * <tt>null</tt> if the <tt>float</tt> is +Infinity, &#x2212;Infinity, or NaN
     *
     * <ul>
     *  <li><tt>f</tt> may be any <tt>float</tt>.</li>
     *  <li>
     *   The result is null or a <tt>Rational</tt> that may be exactly represented as a float. Here are some, but not
     *   all, of the conditions on the result:
     *   <ul>
     *    <li>The denominator is a power of 2 less than or equal to 2<sup>149</sup>.</li>
     *    <li>The numerator is less than or equal to 2<sup>128</sup>&#x2212;2<sup>104</sup>.</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param f the float
     * @return the <tt>Rational</tt> corresponding to <tt>f</tt>, or null if <tt>f</tt> is Inf, &#x2212;Inf, or NaN
     */
    public static @Nullable Rational of(float f) {
        if (f == 0.0f) return ZERO;
        if (f == 1.0f) return ONE;
        if (Float.isInfinite(f) || Float.isNaN(f)) return null;
        int bits = Float.floatToIntBits(f);
        int exponent = bits >> 23 & ((1 << 8) - 1);
        int mantissa = bits & ((1 << 23) - 1);
        Rational rational;
        if (exponent == 0) {
            rational = of(mantissa).shiftRight(149);
        } else {
            rational = of(mantissa + (1 << 23), 1 << 23).shiftLeft(exponent - 127);
        }
        if (bits < 0) rational = rational.negate();
        return rational;
    }

    /**
     * Creates a <tt>Rational</tt> from a <tt>double</tt>. No rounding occurs; the <tt>Rational</tt> has exactly the
     * same value as the <tt>double</tt>. For example, of(1.0/3.0) yields 6004799503160661/18014398509481984, not 1/3.
     * Returns <tt>null</tt> if the <tt>double</tt> is +Infinity, &#x2212;Infinity, or NaN
     *
     * <ul>
     *  <li><tt>f</tt> may be any <tt>double</tt>.</li>
     *  <li>
     *   The result is a null or a <tt>Rational</tt> that may be exactly represented as a double. Here are some, but
     *   not all, of the conditions on the result:
     *   <ul>
     *    <li>The denominator is a power of 2 less than or equal to 2<sup>1074</sup>.</li>
     *    <li>The numerator is less than or equal to 2<sup>1024</sup>&#x2212;2<sup>971</sup>.</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param d the double
     * @return the <tt>Rational</tt> corresponding to <tt>d</tt>, or null if <tt>d</tt> is Inf, &#x2212;Inf, or NaN
     */
    public static @Nullable Rational of(double d) {
        if (d == 0.0) return ZERO;
        if (d == 1.0) return ONE;
        if (Double.isInfinite(d) || Double.isNaN(d)) return null;
        long bits = Double.doubleToLongBits(d);
        int exponent = (int) (bits >> 52) & ((1 << 11) - 1);
        long mantissa = bits & ((1L << 52) - 1);
        Rational rational;
        if (exponent == 0) {
            rational = of(BigInteger.valueOf(mantissa)).shiftRight(1074);
        } else {
            Rational significand = of(BigInteger.valueOf(mantissa)).shiftRight(52);
            rational = add(significand, ONE).shiftLeft(exponent - 1023);
        }
        if (bits < 0) rational = rational.negate();
        return rational;
    }

    /**
     * Creates a <tt>Rational</tt> from a <tt>BigDecimal</tt>.
     *
     * <ul>
     *  <li><tt>d</tt> may not be null.</li>
     *  <li>The result is a <tt>Rational</tt> whose denominator may be written as 2<sup>m</sup>5<sup>n</sup>, with
     *  m,n&#x2265;0.</li>
     * </ul>
     *
     * @param d the <tt>BigDecimal</tt>
     * @return the <tt>Rational</tt> corresponding to <tt>d</tt>
     */
    public static @NotNull Rational of(@NotNull BigDecimal d) {
        return divide(of(d.unscaledValue()), of(10).pow(d.scale()));
    }

    /**
     * Returns the negative of <tt>this</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return &#x2212;<tt>this</tt>.
     */
    public @NotNull Rational negate() {
        if (this == ZERO) return ZERO;
        BigInteger negativeNumerator = numerator.negate();
        if (negativeNumerator.equals(denominator)) return ONE;
        return new Rational(negativeNumerator, denominator);
    }

    /**
     * Returns the multiplicative inverse of <tt>this</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any non-zero <tt>Rational</tt>.</li>
     *  <li>The result is a non-zero <tt>Rational</tt>.</li>
     * </ul>
     *
     * @return 1/<tt>this</tt>.
     */
    public @NotNull Rational invert() {
        if (this == ZERO)
            throw new ArithmeticException("division by zero");
        if (equals(ONE)) return ONE;
        if (numerator.signum() == -1) {
            return new Rational(denominator.negate(), numerator.negate());
        } else {
            return new Rational(denominator, numerator);
        }
    }

    /**
     * Returns the absolute value of <tt>this</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational.</tt></li>
     *  <li>The result is a non-negative <tt>Rational</tt>.</li>
     * </ul>
     *
     * @return |<tt>this</tt>|.
     */
    public @NotNull Rational abs() {
        return numerator.signum() == -1 ? negate() : this;
    }

    /**
     * Returns the sign of <tt>this</tt>: 1 if positive, &#x2212;1 if negative, 0 if equal to 0.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result is &#x2212;1, 0, or 1.</li>
     * </ul>
     *
     * @return sgn(<tt>this</tt>)
     */
    public int signum() {
        return numerator.signum();
    }

    /**
     * Returns the sum of <tt>a</tt> and <tt>b</tt>.
     *
     * <ul>
     *  <li><tt>a</tt> cannot be null.</li>
     *  <li><tt>b</tt> cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param a the first <tt>Rational</tt>
     * @param b the second <tt>Rational</tt>
     * @return <tt>a</tt>+<tt>b</tt>
     */
    public static @NotNull Rational add(@NotNull Rational a, @NotNull Rational b) {
        if (a == ZERO) return b;
        if (b == ZERO) return a;
        BigInteger d1 = a.denominator.gcd(b.denominator);
        if (d1.equals(BigInteger.ONE)) {
            BigInteger sn = a.numerator.multiply(b.denominator).add(a.denominator.multiply(b.numerator));
            if (sn.equals(BigInteger.ZERO)) return ZERO;
            BigInteger sd = a.denominator.multiply(b.denominator);
            if (sn.equals(sd)) return ONE;
            return new Rational(sn, sd);
        } else {
            BigInteger t = a.numerator.multiply(b.denominator.divide(d1)).add(b.numerator.multiply(a.denominator.divide(d1)));
            if (t.equals(BigInteger.ZERO)) return ZERO;
            BigInteger d2 = t.gcd(d1);
            BigInteger sn = t.divide(d2);
            BigInteger sd = a.denominator.divide(d1).multiply(b.denominator.divide(d2));
            if (sn.equals(sd)) return ONE;
            return new Rational(sn, sd);
        }
    }

    /**
     * Returns the difference of <tt>a</tt> and <tt>b</tt>.
     *
     * <ul>
     *  <li><tt>a</tt> cannot be null.</li>
     *  <li><tt>b</tt> cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param a the first <tt>Rational</tt>
     * @param b the second <tt>Rational</tt>
     * @return <tt>a</tt>&#x2212;<tt>b</tt>
     */
    public static @NotNull Rational subtract(@NotNull Rational a, @NotNull Rational b) {
        return add(a, b.negate());
    }

    /**
     * Returns the product of <tt>a</tt> and <tt>b</tt>.
     *
     * <ul>
     *  <li><tt>a</tt> cannot be null.</li>
     *  <li><tt>b</tt> cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param a the first <tt>Rational</tt>
     * @param b the second <tt>Rational</tt>
     * @return <tt>a</tt>&#x00D7;<tt>b</tt>
     */
    public static @NotNull Rational multiply(@NotNull Rational a, @NotNull Rational b) {
        if (a == ZERO || b == ZERO) return ZERO;
        if (a == ONE) return b;
        if (b == ONE) return a;
        BigInteger g1 = a.numerator.gcd(b.denominator);
        BigInteger g2 = b.numerator.gcd(a.denominator);
        BigInteger mn = a.numerator.divide(g1).multiply(b.numerator.divide(g2));
        BigInteger md = a.denominator.divide(g2).multiply(b.denominator.divide(g1));
        if (mn.equals(md)) return ONE;
        return new Rational(mn, md);
    }

    /**
     * Returns the product of <tt>this</tt> and <tt>that</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li><tt>that</tt> cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the multiplier
     * @return <tt>this</tt>&#x00D7;<tt>that</tt>
     */
    public @NotNull Rational multiply(@NotNull BigInteger that) {
        if (this == ZERO || that.equals(BigInteger.ZERO)) return ZERO;
        if (numerator.equals(BigInteger.ONE) && denominator.equals(that) ||
                numerator.equals(BigInteger.ONE.negate()) && denominator.equals(that.negate())) return ONE;
        BigInteger g = denominator.gcd(that);
        return new Rational(numerator.multiply(that.divide(g)), denominator.divide(g));
    }

    /**
     * Returns the product of <tt>this</tt> and <tt>that</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the multiplier
     * @return <tt>this</tt>&#x00D7;<tt>that</tt>
     */
    public @NotNull Rational multiply(int that) {
        if (this == ZERO || that == 0) return ZERO;
        if (numerator.equals(BigInteger.ONE) && denominator.equals(BigInteger.valueOf(that))) return ONE;
        if (numerator.equals(BigInteger.ONE.negate()) && denominator.equals(BigInteger.valueOf(that).negate())) return ONE;
        BigInteger g = denominator.gcd(BigInteger.valueOf(that));
        return new Rational(numerator.multiply(BigInteger.valueOf(that).divide(g)), denominator.divide(g));
    }

    /**
     * Returns the quotient of <tt>a</tt> and <tt>b</tt>.
     *
     * <ul>
     *  <li><tt>a</tt> cannot be null.</li>
     *  <li><tt>b</tt> cannot be null or zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param a the first <tt>Rational</tt>
     * @param b the second <tt>Rational</tt>
     * @return <tt>a</tt>/<tt>b</tt>
     */
    public static @NotNull Rational divide(@NotNull Rational a, @NotNull Rational b) {
        if (b == ZERO)
            throw new ArithmeticException("division by zero");
        return multiply(a, b.invert());
    }

    /**
     * Returns the quotient of <tt>this</tt> and <tt>that</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li><tt>that</tt> cannot be null or zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the divisor
     * @return <tt>this</tt>/<tt>that</tt>
     */
    public @NotNull Rational divide(@NotNull BigInteger that) {
        if (that.equals(BigInteger.ZERO))
            throw new ArithmeticException("division by zero");
        if (this == ZERO) return ZERO;
        if (denominator.equals(BigInteger.ONE) && numerator.equals(that)) return ONE;
        BigInteger g = numerator.gcd(that);
        if (that.signum() == -1) g = g.negate();
        return new Rational(numerator.divide(g), denominator.multiply(that.divide(g)));
    }

    /**
     * Returns the quotient of <tt>this</tt> and <tt>that</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li><tt>that</tt> cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the divisor
     * @return <tt>this</tt>/<tt>that</tt>
     */
    public @NotNull Rational divide(int that) {
        if (that == 0)
            throw new ArithmeticException("division by zero");
        if (this == ZERO) return ZERO;
        if (denominator.equals(BigInteger.ONE) && numerator.equals(BigInteger.valueOf(that))) return ONE;
        BigInteger g = numerator.gcd(BigInteger.valueOf(that));
        if (that < 0) g = g.negate();
        return new Rational(numerator.divide(g), denominator.multiply(BigInteger.valueOf(that).divide(g)));
    }

    /**
     * Returns <tt>this</tt> raised to the power of <tt>p</tt>. 0<sup>0</sup> yields 1.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li><tt>p</tt> may be any <tt>int</tt>.</li>
     *  <li>If <tt>p</tt>&lt;0, <tt>this</tt> cannot be 0.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param p the power
     * @return <tt>this</tt><sup><tt>p</tt></sup>
     */
    public @NotNull Rational pow(int p) {
        if (p == 0) return ONE;
        if (p == 1) return this;
        if (p < 0) {
            if (this == ZERO)
                throw new ArithmeticException("division by zero");
            return invert().pow(-p);
        }
        if (this == ZERO || this == ONE) return this;
        if (this.equals(ONE.negate()) && p % 2 == 0) return ONE;
        BigInteger pNumerator = numerator.pow(p);
        BigInteger pDenominator = denominator.pow(p);
        if (pDenominator.signum() == -1) {
            pNumerator = pNumerator.negate();
            pDenominator = pDenominator.negate();
        }
        return new Rational(pNumerator, pDenominator);
    }

    /**
     * Returns the floor of <tt>this</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return &#x230A;<tt>this</tt>&#x230B;
     */
    public @NotNull BigInteger floor() {
        if (numerator.signum() < 0) {
            return negate().ceiling().negate();
        } else {
            return numerator.divide(denominator);
        }
    }

    /**
     * Returns the ceiling of <tt>this</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return &#x2308;<tt>this</tt>&#x2309;
     */
    public @NotNull BigInteger ceiling() {
        if (numerator.signum() < 0) {
            return negate().floor().negate();
        } else {
            if (numerator.mod(denominator).equals(BigInteger.ZERO)) {
                return numerator.divide(denominator);
            } else {
                return numerator.divide(denominator).add(BigInteger.ONE);
            }
        }
    }

    /**
     * Returns the fractional part of <tt>this</tt>; <tt>this</tt>&#x2212;&#x230A;<tt>this</tt>&#x230B;.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result is a <tt>Rational</tt> x such that 0&#x2264;x&lt;1.</li>
     * </ul>
     *
     * @return the fractional part of <tt>this</tt>
     */
    public @NotNull Rational fractionalPart() {
        if (denominator.equals(BigInteger.ONE)) return ZERO;
        return subtract(this, of(floor()));
    }

    /**
     * Rounds <tt>this</tt> to an integer according to <tt>roundingMode</tt>; see documentation for
     * <tt>java.math.RoundingMode</tt> for details.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li><tt>roundingMode</tt> may be any <tt>RoundingMode</tt>.</li>
     *  <li>If <tt>roundingMode</tt> is <tt>UNNECESSARY</tt>, <tt>this</tt> must be an integer.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param roundingMode determines the way in which <tt>this</tt> is rounded. Options are <tt>UP</tt>,
     *                     <tt>DOWN</tt>, <tt>CEILING</tt>, <tt>FLOOR</tt>, <tt>HALF_UP</tt>, <tt>HALF_DOWN</tt>,
     *                     <tt>HALF_EVEN</tt>, and <tt>UNNECESSARY</tt>.
     * @return <tt>this</tt>, rounded
     */
    public @NotNull BigInteger round(@NotNull RoundingMode roundingMode) {
        Ordering halfCompare = compare(fractionalPart(), of(1, 2));
        if (signum() == -1) halfCompare = halfCompare.invert();
        switch (roundingMode) {
            case UNNECESSARY:
                if (denominator.equals(BigInteger.ONE)) {
                    return numerator;
                } else {
                    throw new ArithmeticException("Rational not an integer. Use a different rounding mode");
                }
            case FLOOR:
                return floor();
            case CEILING:
                return ceiling();
            case DOWN:
                return numerator.divide(denominator);
            case UP:
                BigInteger down = numerator.divide(denominator);
                if (numerator.mod(denominator).equals(BigInteger.ZERO)) {
                    return down;
                } else {
                    if (numerator.signum() == 1) {
                        return down.add(BigInteger.ONE);
                    } else {
                        return down.subtract(BigInteger.ONE);
                    }
                }
            case HALF_DOWN:
                if (halfCompare == GT) {
                    return round(RoundingMode.UP);
                } else {
                    return round(RoundingMode.DOWN);
                }
            case HALF_UP:
                if (halfCompare == LT) {
                    return round(RoundingMode.DOWN);
                } else {
                    return round(RoundingMode.UP);
                }
            case HALF_EVEN:
                if (halfCompare == LT) return round(RoundingMode.DOWN);
                if (halfCompare == GT) return round(RoundingMode.UP);
                BigInteger floor = floor();
                return floor.testBit(0) ? floor.add(BigInteger.ONE) : floor;
        }
        return null; //never happens
    }

    /**
     * Rounds <tt>this</tt> a rational number that is an integer multiple of 1/<tt>denominator</tt> according to
     * <tt>roundingMode</tt>; see documentation for <tt>java.math.RoundingMode</tt> for details.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li><tt>denominator</tt> must be positive.</li>
     *  <li>If <tt>roundingMode</tt> is <tt>UNNECESSARY</tt>, <tt>this</tt>'s denominator must divide
     *  <tt>denominator</tt>.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param denominator the denominator which represents the precision that <tt>this</tt> is rounded to.
     * @param roundingMode determines the way in which <tt>this</tt> is rounded. Options are <tt>UP</tt>,
     *                     <tt>DOWN</tt>, <tt>CEILING</tt>, <tt>FLOOR</tt>, <tt>HALF_UP</tt>, <tt>HALF_DOWN</tt>,
     *                     <tt>HALF_EVEN</tt>, and <tt>UNNECESSARY</tt>.
     * @return <tt>this</tt>, rounded to an integer multiple of 1/<tt>denominator</tt>
     */
    public @NotNull Rational roundToDenominator(@NotNull BigInteger denominator, @NotNull RoundingMode roundingMode) {
        if (denominator.signum() != 1)
            throw new ArithmeticException("must round to a positive denominator");
        return of(multiply(denominator).round(roundingMode)).divide(denominator);
    }

    /**
     * Returns the left shift of <tt>this</tt> by <tt>bits</tt>;
     * <tt>this</tt>&#x00D7;2<sup><tt>bits</tt></sup>. Negative <tt>bits</tt> corresponds to a right shift.
     *
     * <ul>
     *  <li><tt>this</tt> can be any <tt>Rational</tt>.</li>
     *  <li><tt>bits</tt> may be any <tt>int</tt>.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param bits the number of bits to left-shift by
     * @return <tt>this</tt>&lt;&lt;<tt>bits</tt>
     */
    public @NotNull Rational shiftLeft(int bits) {
        if (this == ZERO) return ZERO;
        if (bits == 0) return this;
        if (bits < 0) return shiftRight(-bits);
        int denominatorTwos = denominator.getLowestSetBit();
        if (bits <= denominatorTwos) {
            BigInteger shifted = denominator.shiftRight(bits);
            if (numerator.equals(BigInteger.ONE) && shifted.equals(BigInteger.ONE)) return ONE;
            return new Rational(numerator, shifted);
        } else {
            BigInteger shiftedNumerator = numerator.shiftLeft(bits - denominatorTwos);
            BigInteger shiftedDenominator = denominator.shiftRight(denominatorTwos);
            if (shiftedNumerator.equals(BigInteger.ONE) && shiftedDenominator.equals(BigInteger.ONE)) return ONE;
            return new Rational(shiftedNumerator, shiftedDenominator);
        }
    }

    /**
     * Returns the right shift of <tt>this</tt> by <tt>bits</tt>;
     * <tt>this</tt>&#x00D7;2<sup>&#x2212;<tt>bits</tt></sup>. Negative <tt>bits</tt> corresponds to a left shift.
     *
     * <ul>
     *  <li><tt>this</tt> can be any <tt>Rational</tt>.</li>
     *  <li><tt>bits</tt> may be any <tt>int</tt>.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param bits the number of bits to right-shift by
     * @return <tt>this</tt>&gt;&gt;<tt>bits</tt>
     */
    public @NotNull Rational shiftRight(int bits) {
        if (this == ZERO) return ZERO;
        if (bits == 0) return this;
        if (bits < 0) return shiftLeft(-bits);
        int numeratorTwos = numerator.getLowestSetBit();
        if (bits <= numeratorTwos) {
            BigInteger shifted = numerator.shiftRight(bits);
            if (shifted.equals(BigInteger.ONE) && denominator.equals(BigInteger.ONE)) return ONE;
            return new Rational(shifted, denominator);
        } else {
            BigInteger shiftedNumerator = numerator.shiftRight(numeratorTwos);
            BigInteger shiftedDenominator = denominator.shiftLeft(bits - numeratorTwos);
            if (shiftedNumerator.equals(BigInteger.ONE) && shiftedDenominator.equals(BigInteger.ONE)) return ONE;
            return new Rational(shiftedNumerator, shiftedDenominator);
        }
    }

    /**
     * This method returns the floor of the base-2 logarithm of <tt>this</tt>. In other words, every positive
     * <tt>Rational</tt> may be written as a&#x00D7;2<sup>b</sup>, where a is a <tt>Rational</tt> such that
     * 1&#x2264;a&lt;2 and b is an integer; this method returns b.
     *
     * <ul>
     *  <li><tt>this</tt> must be positive.</li>
     *  <li>The result could be any integer.</li>
     * </ul>
     *
     * @return &#x230A;<tt>log<sub>2</sub>this</tt>&#x230B;
     */
    public int binaryExponent() {
        if (this == ONE) return 0;
        Rational adjusted = this;
        int exponent = 0;
        if (lt(numerator, denominator)) {
            while (lt(adjusted.numerator, adjusted.denominator)) {
                adjusted = adjusted.shiftLeft(1);
                exponent--;
            }
        } else {
            while (ge(adjusted.numerator, adjusted.denominator)) {
                adjusted = adjusted.shiftRight(1);
                exponent++;
            }
            exponent--;
        }
        return exponent;
    }

    /**
     * Every <tt>Rational</tt>has a <i>left-neighboring <tt>float</tt></i>, or the largest <tt>float</tt> that is less
     * than or equal to the <tt>Rational</tt>; this <tt>float</tt> may be -Infinity. Likewise, every <tt>Rational</tt>
     * has a <i>right-neighboring <tt>float</tt></i>: the smallest <tt>float</tt> greater than or equal to the
     * <tt>Rational</tt>. This float may be Infinity. If <tt>this</tt> is exactly equal to some <tt>float</tt>, the
     * left- and right-neighboring <tt>float</tt>s will both be equal to that <tt>float</tt> and to each other. This
     * method returns the pair made up of the left- and right-neighboring <tt>float</tt>s. If the left-neighboring
     * <tt>float</tt> is a zero, it is a positive zero; if the right-neighboring <tt>float</tt> is a zero, it is a
     * negative zero. The exception is when <tt>this</tt> is equal to zero; then both neighbors are positive zeroes.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result is a pair of <tt>float</tt>s that are either equal, or the second is the next-largest
     *  <tt>float</tt> after the first. Negative zero may not appear in the first slot of the pair, and positive zero
     *  may only appear in the second slot if the first slot also contains a positive zero. Neither slot may contain a
     *  NaN.</li>
     * </ul>
     *
     * @return The pair of left- and right-neighboring <tt>float</tt>s.
     */
    private @NotNull Pair<Float, Float> floatRange() {
        if (this == ZERO) return new Pair<>(0f, 0f);
        if (numerator.signum() == -1) {
            Pair<Float, Float> negativeRange = negate().floatRange();
            assert negativeRange.a != null;
            assert negativeRange.b != null;
            return new Pair<>(-negativeRange.b, -negativeRange.a);
        }
        int exponent = binaryExponent();
        if (exponent > 127 || exponent == 127 && gt(this, LARGEST_FLOAT)) {
            return new Pair<>(Float.MAX_VALUE, Float.POSITIVE_INFINITY);
        }
        Rational fraction;
        int adjustedExponent;
        if (exponent < -126) {
            fraction = shiftLeft(149);
            adjustedExponent = 0;
        } else {
            fraction = subtract(shiftRight(exponent), ONE).shiftLeft(23);
            adjustedExponent = exponent + 127;
        }
        float loFloat = Float.intBitsToFloat((adjustedExponent << 23) + fraction.floor().intValue());
        float hiFloat = fraction.denominator.equals(BigInteger.ONE) ? loFloat : Numbers.successor(loFloat);
        return new Pair<>(loFloat, hiFloat);
    }

    /**
     * Every <tt>Rational</tt>has a <i>left-neighboring <tt>double</tt></i>, or the largest <tt>double</tt> that is
     * less than or equal to the <tt>Rational</tt>; this <tt>double</tt> may be -Infinity. Likewise, every
     * <tt>Rational</tt> has a <i>right-neighboring <tt>double</tt></i>: the smallest <tt>double</tt> greater than or
     * equal to the <tt>Rational</tt>. This double may be Infinity. If <tt>this</tt> is exactly equal to some
     * <tt>double</tt>, the left- and right-neighboring <tt>double</tt>s will both be equal to that <tt>double</tt> and
     * to each other. This method returns the pair made up of the left- and right-neighboring <tt>double</tt>s. If the
     * left-neighboring <tt>double</tt> is a zero, it is a positive zero; if the right-neighboring <tt>double</tt> is a
     * zero, it is a negative zero. The exception is when <tt>this</tt> is equal to zero; then both neighbors are
     * positive zeroes.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result is a pair of <tt>double</tt>s that are either equal, or the second is the next-largest
     *  <tt>double</tt> after the first. Negative zero may not appear in the first slot of the pair, and positive zero
     *  may only appear in the second slot if the first slot also contains a positive zero. Neither slot may contain a
     *  NaN.</li>
     * </ul>
     *
     * @return The pair of left- and right-neighboring <tt>double</tt>s.
     */
    private @NotNull Pair<Double, Double> doubleRange() {
        if (this == ZERO) return new Pair<>(0.0, 0.0);
        if (numerator.signum() == -1) {
            Pair<Double, Double> negativeRange = negate().doubleRange();
            assert negativeRange.a != null;
            assert negativeRange.b != null;
            return new Pair<>(-negativeRange.b, -negativeRange.a);
        }
        int exponent = binaryExponent();
        if (exponent > 1023 || exponent == 1023 && gt(this, LARGEST_DOUBLE)) {
            return new Pair<>(Double.MAX_VALUE, Double.POSITIVE_INFINITY);
        }
        Rational fraction;
        int adjustedExponent;
        if (exponent < -1022) {
            fraction = shiftLeft(1074);
            adjustedExponent = 0;
        } else {
            fraction = subtract(shiftRight(exponent), ONE).shiftLeft(52);
            adjustedExponent = exponent + 1023;
        }
        double loDouble = Double.longBitsToDouble(((long) adjustedExponent << 52) + fraction.floor().longValue());
        double hiDouble = fraction.denominator.equals(BigInteger.ONE) ? loDouble : Numbers.successor(loDouble);
        return new Pair<>(loDouble, hiDouble);
    }

    /**
     * Rounds <tt>this</tt> to a <tt>float</tt> using <tt>RoundingMode.HALF_EVEN</tt>. If <tt>this</tt> is closest to
     * one <tt>float</tt>, that <tt>float</tt> is returned. If there are two closest <tt>float</tt>s, the one with the
     * unset lowest-order bit is returned. If <tt>this</tt> is greater than or equal to zero and less than or equal to
     * <tt>Float.MIN_VALUE</tt>/2, positive zero is returned. If <tt>this</tt> is greater than or equal to
     * &#x2212;<tt>Float.MIN_VALUE</tt>/2 and less than zero, negative zero is returned. If <tt>this</tt> is greater
     * than <tt>Float.MAX_VALUE</tt>, Infinity is returned. If <tt>this</tt> is less than
     * &#x2212;<tt>Float.MAX_VALUE</tt>, &#x2212;Infinity is returned.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result may be any <tt>float</tt> except NaN.</li>
     * </ul>
     *
     * @return <tt>this</tt>, rounded
     */
    public float toFloat() {
        return toFloat(RoundingMode.HALF_EVEN);
    }

    /**
     * Rounds <tt>this</tt> to a <tt>float</tt>. The details of the rounding are specified by <tt>roundingMode</tt>.
     * <ul>
     *  <li><tt>UNNECESSARY</tt>: If <tt>this</tt> is exactly equal to a <tt>float</tt>, that <tt>float</tt> is
     *  returned. Otherwise, an <tt>ArithmeticException</tt> is thrown. If <tt>this</tt> is zero, positive zero is
     *  returned. Negative zero, Infinity, and &#x2212;Infinity cannot be returned.</li>
     *  <li><tt>FLOOR</tt>: The largest <tt>float</tt> less than or equal to <tt>this</tt> is returned. If
     *  <tt>this</tt> is greater than or equal to zero and less than <tt>Float.MIN_VALUE</tt>, positive zero is
     *  returned. If <tt>this</tt> is less than &#x2212;<tt>Float.MAX_VALUE</tt>, &#x2212;Infinity is returned. If
     *  <tt>this</tt> is greater than or equal to <tt>Float.MAX_VALUE</tt>, <tt>Float.MAX_VALUE</tt> is returned.
     *  Negative zero and Infinity cannot be returned.</li>
     *  <li><tt>CEILING</tt>: The smallest <tt>float</tt> greater than or equal to <tt>this</tt> is returned. If
     *  <tt>this</tt> is equal to zero, positive zero is returned. If <tt>this</tt> is less than zero and greater than
     *  &#x2212;<tt>Float.MIN_VALUE</tt>, negative zero is returned. If <tt>this</tt> is greater than
     *  <tt>Float.MAX_VALUE</tt>, Infinity is returned. If <tt>this</tt> is less than or equal to
     *  &#x2212;<tt>Float.MAX_VALUE</tt>, &#x2212;<tt>Float.MAX_VALUE</tt> is returned. &#x2212;Infinity cannot be
     *  returned.</li>
     *  <li><tt>DOWN</tt>: The first <tt>float</tt> going from <tt>this</tt> to 0 (possibly equal to <tt>this</tt>) is
     *  returned. If <tt>this</tt> is greater than or equal to zero and less than <tt>Float.MIN_VALUE</tt>, positive
     *  zero is returned. If <tt>this</tt> is less than zero and greater than &#x2212;<tt>Float.MIN_VALUE</tt>,
     *  negative zero is returned. If <tt>this</tt> is greater than or equal to <tt>Float.MAX_VALUE</tt>,
     *  <tt>Float.MAX_VALUE</tt> is returned. If <tt>this</tt> is less than or equal to
     *  &#x2212;<tt>Float.MAX_VALUE</tt>, &#x2212;<tt>Float.MAX_VALUE</tt> is returned. Infinity and &#x2212;Infinity
     *  cannot be returned.</li>
     *  <li><tt>UP</tt>: The first <tt>float</tt> going from <tt>this</tt> to the Infinity of the same sign (possibly
     *  equal to <tt>this</tt>) is returned. If <tt>this</tt> is equal to zero, positive zero is returned. If
     *  <tt>this</tt> is greater than <tt>Float.MAX_VALUE</tt>, Infinity is returned. If <tt>this</tt> is less than
     *  <tt>Float.MIN_VALUE</tt>, &#x2212;Infinity is returned. Negative zero cannot be returned.</li>
     *  <li><tt>HALF_DOWN</tt>: If <tt>this</tt> is closest to one <tt>float</tt>, that <tt>float</tt> is returned. If
     *  there are two closest <tt>float</tt>s, the one with the lower absolute value is returned. If <tt>this</tt> is
     *  greater than or equal to zero and less than or equal to <tt>Float.MIN_VALUE</tt>/2, positive zero is returned.
     *  If <tt>this</tt> is greater than or equal to &#x2212;<tt>Float.MIN_VALUE</tt>/2 and less than zero, negative
     *  zero is returned. If <tt>this</tt> is greater than or equal to <tt>Float.MAX_VALUE</tt>,
     *  <tt>Float.MAX_VALUE</tt> is returned. If <tt>this</tt> is less than or equal to
     *  &#x2212;<tt>Float.MAX_VALUE</tt>, &#x2212;<tt>Float.MAX_VALUE</tt> is returned. Infinity and &#x2212;Infinity
     *  cannot be returned.</li>
     *  <li><tt>HALF_UP</tt>: If <tt>this</tt> is closest to one <tt>float</tt>, that <tt>float</tt> is returned. If
     *  there are two closest <tt>float</tt>s, the one with the higher absolute value is returned. If <tt>this</tt> is
     *  greater than or equal to zero and less than <tt>Float.MIN_VALUE</tt>/2, positive zero is returned. If
     *  <tt>this</tt> is greater than &#x2212;<tt>Float.MIN_VALUE</tt>/2 and less than zero, negative zero is returned.
     *  If <tt>this</tt> is greater than <tt>Float.MAX_VALUE</tt>, Infinity is returned. If <tt>this</tt> is less than
     *  &#x2212;<tt>Float.MAX_VALUE</tt>, &#x2212;Infinity is returned.</li>
     *  <li><tt>HALF_EVEN</tt>: If <tt>this</tt> is closest to one <tt>float</tt>, that <tt>float</tt> is returned. If
     *  there are two closest <tt>float</tt>s, the one with the unset lowest-order bit is returned. If <tt>this</tt> is
     *  greater than or equal to zero and less than or equal to <tt>Float.MIN_VALUE</tt>/2, positive zero is returned.
     *  If <tt>this</tt> is greater than or equal to &#x2212;<tt>Float.MIN_VALUE</tt>/2 and less than zero, negative
     *  zero is returned. If <tt>this</tt> is greater than <tt>Float.MAX_VALUE</tt>, Infinity is returned. If
     *  <tt>this</tt> is less than &#x2212;<tt>Float.MAX_VALUE</tt>, &#x2212;Infinity is returned.</li>
     * </ul>
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li><tt>roundingMode</tt> cannot be null.</li>
     *  <li>If <tt>roundingMode</tt> is <tt>UNNECESSARY</tt>, <tt>this</tt> must be exactly equal to a
     *  <tt>float</tt>.</li>
     *  <li>The result may be any <tt>float</tt> except NaN.</li>
     * </ul>
     *
     * @param roundingMode specifies the details of how to round <tt>this</tt>.
     * @return <tt>this</tt>, rounded
     */
    public float toFloat(@NotNull RoundingMode roundingMode) {
        Pair<Float, Float> floatRange = floatRange();
        assert floatRange.a != null;
        assert floatRange.b != null;
        if (floatRange.a.equals(floatRange.b)) return floatRange.a;
        Rational loFloat = of(floatRange.a);
        Rational hiFloat = of(floatRange.b);
        if ((loFloat == null || hiFloat == null) && roundingMode == RoundingMode.UNNECESSARY) {
            throw new ArithmeticException("Rational not exactly equal to a float. Use a different rounding mode");
        }
        if (loFloat == null) {
            if (roundingMode == RoundingMode.FLOOR || roundingMode == RoundingMode.UP ||
                    roundingMode == RoundingMode.HALF_UP || roundingMode == RoundingMode.HALF_EVEN) {
                return Float.NEGATIVE_INFINITY;
            } else {
                return -Float.MAX_VALUE;
            }
        }
        if (hiFloat == null) {
            if (roundingMode == RoundingMode.CEILING || roundingMode == RoundingMode.UP ||
                    roundingMode == RoundingMode.HALF_UP || roundingMode == RoundingMode.HALF_EVEN) {
                return Float.POSITIVE_INFINITY;
            } else {
                return Float.MAX_VALUE;
            }
        }
        Rational midway = add(loFloat, hiFloat).shiftRight(1);
        Ordering midwayCompare = compare(this, midway);
        switch (roundingMode) {
            case UNNECESSARY:
                throw new ArithmeticException("Rational not exactly equal to a float. Use a different rounding mode");
            case FLOOR:
                return floatRange.a;
            case CEILING:
                return floatRange.b;
            case DOWN:
                return floatRange.a < 0 ? floatRange.b : floatRange.a;
            case UP:
                return floatRange.a < 0 ? floatRange.a : floatRange.b;
            case HALF_DOWN:
                if (midwayCompare == EQ) return signum() == 1 ? floatRange.a : floatRange.b;
                return midwayCompare == LT ? floatRange.a : floatRange.b;
            case HALF_UP:
                if (midwayCompare == EQ) return signum() == 1 ? floatRange.b : floatRange.a;
                return midwayCompare == LT ? floatRange.a : floatRange.b;
            case HALF_EVEN:
                if (midwayCompare == LT) return floatRange.a;
                if (midwayCompare == GT) return floatRange.b;
                return (Float.floatToIntBits(floatRange.a) & 1) == 0 ? floatRange.a : floatRange.b;
        }
        return 0; //never happens
    }

    /**
     * Rounds <tt>this</tt> to a <tt>double</tt> using <tt>RoundingMode.HALF_EVEN</tt>. If <tt>this</tt> is closest to
     * one <tt>double</tt>, that <tt>double</tt> is returned. If there are two closest <tt>double</tt>s, the one with
     * the unset lowest-order bit is returned. If <tt>this</tt> is greater than or equal to zero and less than or equal
     * to <tt>Double.MIN_VALUE</tt>/2, positive zero is returned. If <tt>this</tt> is greater than or equal to
     * &#x2212;<tt>Double.MIN_VALUE</tt>/2 and less than zero, negative zero is returned. If <tt>this</tt> is greater
     * than <tt>Double.MAX_VALUE</tt>, Infinity is returned. If <tt>this</tt> is less than
     * &#x2212;<tt>Double.MAX_VALUE</tt>, &#x2212;Infinity is returned.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result may be any <tt>double</tt> except NaN.</li>
     * </ul>
     *
     * @return <tt>this</tt>, rounded
     */
    public double toDouble() {
        return toDouble(RoundingMode.HALF_EVEN);
    }

    /**
     * Rounds <tt>this</tt> to a <tt>double</tt>. The details of the rounding are specified by <tt>roundingMode</tt>.
     * <ul>
     *  <li><tt>UNNECESSARY</tt>: If <tt>this</tt> is exactly equal to a <tt>double</tt>, that <tt>double</tt> is
     *  returned. Otherwise, an <tt>ArithmeticException</tt> is thrown. If <tt>this</tt> is zero, positive zero is
     *  returned. Negative zero, Infinity, and &#x2212;Infinity cannot be returned.</li>
     *  <li><tt>FLOOR</tt>: The largest <tt>double</tt> less than or equal to <tt>this</tt> is returned. If
     *  <tt>this</tt> is greater than or equal to zero and less than <tt>Double.MIN_VALUE</tt>, positive zero is
     *  returned. If <tt>this</tt> is less than &#x2212;<tt>Double.MAX_VALUE</tt>, &#x2212;Infinity is returned. If
     *  <tt>this</tt> is greater than or equal to <tt>Double.MAX_VALUE</tt>, <tt>Double.MAX_VALUE</tt> is returned.
     *  Negative zero and Infinity cannot be returned.</li>
     *  <li><tt>CEILING</tt>: The smallest <tt>double</tt> greater than or equal to <tt>this</tt> is returned. If
     *  <tt>this</tt> is equal to zero, positive zero is returned. If <tt>this</tt> is less than zero and greater than
     *  &#x2212;<tt>Double.MIN_VALUE</tt>, negative zero is returned. If <tt>this</tt> is greater than
     *  <tt>Double.MAX_VALUE</tt>, Infinity is returned. If <tt>this</tt> is less than or equal to
     *  &#x2212;<tt>Double.MAX_VALUE</tt>, &#x2212;<tt>Double.MAX_VALUE</tt> is returned. &#x2212;Infinity cannot be
     *  returned.</li>
     *  <li><tt>DOWN</tt>: The first <tt>double</tt> going from <tt>this</tt> to 0 (possibly equal to <tt>this</tt>) is
     *  returned. If <tt>this</tt> is greater than or equal to zero and less than <tt>Double.MIN_VALUE</tt>, positive
     *  zero is returned. If <tt>this</tt> is greater than &#x2212;<tt>Double.MIN_VALUE</tt> and less than zero,
     *  negative zero is returned. If <tt>this</tt> is greater than or equal to <tt>Double.MAX_VALUE</tt>,
     *  <tt>Double.MAX_VALUE</tt> is returned. If <tt>this</tt> is less than or equal to
     *  &#x2212;<tt>Double.MAX_VALUE</tt>, &#x2212;<tt>Double.MAX_VALUE</tt> is returned. Infinity and &#x2212;Infinity
     *  cannot be returned.</li>
     *  <li><tt>UP</tt>: The first <tt>double</tt> going from <tt>this</tt> to the Infinity of the same sign (possibly
     *  equal to <tt>this</tt>) is returned. If <tt>this</tt> is equal to zero, positive zero is returned. If
     *  <tt>this</tt> is greater than <tt>Double.MAX_VALUE</tt>, Infinity is returned. If <tt>this</tt> is less than
     *  <tt>Double.MIN_VALUE</tt>, &#x2212;Infinity is returned. Negative zero cannot be returned.</li>
     *  <li><tt>HALF_DOWN</tt>: If <tt>this</tt> is closest to one <tt>double</tt>, that <tt>double</tt> is returned.
     *  If there are two closest <tt>double</tt>s, the one with the lower absolute value is returned. If <tt>this</tt>
     *  is greater than or equal to zero and less than or equal to <tt>Double.MIN_VALUE</tt>/2, positive zero is
     *  returned. If <tt>this</tt> is greater than or equal to &#x2212;<tt>Double.MIN_VALUE</tt>/2 and less than zero,
     *  negative zero is returned. If <tt>this</tt> is greater than or equal to <tt>Double.MAX_VALUE</tt>,
     *  <tt>Double.MAX_VALUE</tt> is returned. If <tt>this</tt> is less than or equal to
     *  &#x2212;<tt>Double.MAX_VALUE</tt>, &#x2212;<tt>Double.MAX_VALUE</tt> is returned. Infinity and &#x2212;Infinity
     *  cannot be returned.</li>
     *  <li><tt>HALF_UP</tt>: If <tt>this</tt> is closest to one <tt>double</tt>, that <tt>double</tt> is returned. If
     *  there are two closest <tt>double</tt>s, the one with the higher absolute value is returned. If <tt>this</tt> is
     *  greater than or equal to zero and less than <tt>Double.MIN_VALUE</tt>/2, positive zero is returned. If
     *  <tt>this</tt> is greater than &#x2212;<tt>Double.MIN_VALUE</tt>/2 and less than zero, negative zero is returned.
     *  If <tt>this</tt> is greater than <tt>Double.MAX_VALUE</tt>, Infinity is returned. If <tt>this</tt> is less than
     *  &#x2212;<tt>Double.MAX_VALUE</tt>, &#x2212;Infinity is returned.</li>
     *  <li><tt>HALF_EVEN</tt>: If <tt>this</tt> is closest to one <tt>double</tt>, that <tt>double</tt> is returned.
     *  If there are two closest <tt>double</tt>s, the one with the unset lowest-order bit is returned. If
     *  <tt>this</tt> is greater than or equal to zero and less than or equal to <tt>Double.MIN_VALUE</tt>/2, positive
     *  zero is returned. If <tt>this</tt> is greater than or equal to &#x2212;<tt>Double.MIN_VALUE</tt>/2 and less
     *  than zero, negative zero is returned. If <tt>this</tt> is greater than <tt>Double.MAX_VALUE</tt>, Infinity is
     *  returned. If <tt>this</tt> is less than &#x2212;<tt>Double.MAX_VALUE</tt>, &#x2212;Infinity is returned.</li>
     * </ul>
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li><tt>roundingMode</tt> cannot be null.</li>
     *  <li>If <tt>roundingMode</tt> is <tt>UNNECESSARY</tt>, <tt>this</tt> must be exactly equal to a
     *  <tt>double</tt>.</li>
     *  <li>The result may be any <tt>double</tt> except NaN.</li>
     * </ul>
     *
     * @param roundingMode specifies the details of how to round <tt>this</tt>.
     * @return <tt>this</tt>, rounded
     */
    public double toDouble(@NotNull RoundingMode roundingMode) {
        Pair<Double, Double> doubleRange = doubleRange();
        assert doubleRange.a != null;
        assert doubleRange.b != null;
        if (doubleRange.a.equals(doubleRange.b)) return doubleRange.a;
        Rational loDouble = of(doubleRange.a);
        Rational hiDouble = of(doubleRange.b);
        if ((loDouble == null || hiDouble == null) && roundingMode == RoundingMode.UNNECESSARY) {
            throw new ArithmeticException("Rational not exactly equal to a double. Use a different rounding mode");
        }
        if (loDouble == null) {
            if (roundingMode == RoundingMode.FLOOR || roundingMode == RoundingMode.UP ||
                    roundingMode == RoundingMode.HALF_UP || roundingMode == RoundingMode.HALF_EVEN) {
                return Double.NEGATIVE_INFINITY;
            } else {
                return -Double.MAX_VALUE;
            }
        }
        if (hiDouble == null) {
            if (roundingMode == RoundingMode.CEILING || roundingMode == RoundingMode.UP ||
                    roundingMode == RoundingMode.HALF_UP || roundingMode == RoundingMode.HALF_EVEN) {
                return Double.POSITIVE_INFINITY;
            } else {
                return Double.MAX_VALUE;
            }
        }
        Rational midway = add(loDouble, hiDouble).shiftRight(1);
        Ordering midwayCompare = compare(this, midway);
        switch (roundingMode) {
            case UNNECESSARY:
                throw new ArithmeticException("Rational not exactly equal to a double. Use a different rounding mode");
            case FLOOR:
                return doubleRange.a;
            case CEILING:
                return doubleRange.b;
            case DOWN:
                return doubleRange.a < 0 ? doubleRange.b : doubleRange.a;
            case UP:
                return doubleRange.a < 0 ? doubleRange.a : doubleRange.b;
            case HALF_DOWN:
                if (midwayCompare == EQ) return signum() == 1 ? doubleRange.a : doubleRange.b;
                return midwayCompare == LT ? doubleRange.a : doubleRange.b;
            case HALF_UP:
                if (midwayCompare == EQ) return signum() == 1 ? doubleRange.b : doubleRange.a;
                return midwayCompare == LT ? doubleRange.a : doubleRange.b;
            case HALF_EVEN:
                if (midwayCompare == LT) return doubleRange.a;
                if (midwayCompare == GT) return doubleRange.b;
                return (Double.doubleToLongBits(doubleRange.a) & 1) == 0 ? doubleRange.a : doubleRange.b;
        }
        return 0; //never happens
    }

    /**
     * Determines whether <tt>this</tt> has a terminating decimal expansion (that is, whether the denominator has no
     * prime factors other than 2 or 5).
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result may be either <tt>boolean</tt>.</li>
     * </ul>
     *
     * @return whether <tt>this</tt> has a terminating decimal expansion
     */
    public boolean hasTerminatingDecimalExpansion() {
        BigInteger denominatorResidue = denominator.shiftRight(denominator.getLowestSetBit());
        BigInteger five = BigInteger.valueOf(5);
        while (denominatorResidue.mod(five).equals(BigInteger.ZERO)) {
            denominatorResidue = denominatorResidue.divide(five);
        }
        return denominatorResidue.equals(BigInteger.ONE);
    }

    /**
     * Returns a BigDecimal exactly equal to <tt>this</tt>. Throws an <tt>ArithmeticException</tt> if <tt>this</tt>
     * cannot be represented as a terminating decimal.
     *
     * <ul>
     *  <li><tt>this</tt> must be a <tt>Rational</tt> whose decimal expansion is terminating; that is, its denominator
     *  must only have 2 or 5 as prime factors.</li>
     *  <li>The result is a <tt>BigDecimal</tt> with minimal scale. That is, the scale is the smallest non-negative n
     *  such that <tt>this</tt>&#x00D7;10<sup>n</sup> is an integer.</li>
     * </ul>
     *
     * @return <tt>this</tt>, in <tt>BigDecimal</tt> form
     */
    public @NotNull BigDecimal toBigDecimal() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator));
    }

    /**
     * Rounds <tt>this</tt> to a <tt>BigDecimal</tt> with a specified precision (number of significant digits), or to
     * full precision if <tt>precision</tt> is 0.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li><tt>precision</tt> must be non-negative.</li>
     *  <li>If <tt>precision</tt> is 0, then <tt>this</tt> must be a <tt>Rational</tt> whose decimal expansion is
     *  terminating; that is, its denominator must only have 2 or 5 as prime factors.</li>
     *  <li>The result is a <tt>BigDecimal</tt> x such that x's scale is greater than or equal to zero and less than or
     *  equal to n, where n is the smallest non-negative integer such that x&#x00D7;10<sup>n</sup> is an integer.</li>
     * </ul>
     *
     * @param precision the precision with which to round <tt>this</tt>. 0 indicates full precision.
     * @return <tt>this</tt>, in <tt>BigDecimal</tt> form
     */
    public @NotNull BigDecimal toBigDecimal(int precision) {
        MathContext context = new MathContext(precision);
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), context);
    }

    /**
     * Rounds <tt>this</tt> to a <tt>BigDecimal</tt> with a specified rounding mode (see documentation for
     * <tt>java.math.RoundingMode</tt> for details) and with a specified precision (number of significant digits), or
     * to full precision if <tt>precision</tt> is 0.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li><tt>precision</tt> must be non-negative.</li>
     *  <li><tt>roundingMode</tt> may be any <tt>RoundingMode</tt>.</li>
     *  <li>If <tt>precision</tt> is 0, then <tt>this</tt> must be a <tt>Rational</tt> whose decimal expansion is
     *  terminating; that is, its denominator must only have 2 or 5 as prime factors.</li>
     *  <li>If <tt>roundingMode</tt> is <tt>UNNECESSARY</tt>, then <tt>precision</tt> must be at least as large as the
     *  number of digits in <tt>this</tt>'s decimal expansion.</li>
     *  <li>The result is a <tt>BigDecimal</tt> x such that x's scale is greater than or equal to zero and less than or
     *  equal to n, where n is the smallest non-negative integer such x&#x00D7;10<sup>n</sup> is an integer.</li>
     * </ul>
     *
     * @param precision the precision with which to round <tt>this</tt>. 0 indicates full precision.
     * @param roundingMode specifies the details of how to round <tt>this</tt>.
     * @return <tt>this</tt>, in <tt>BigDecimal</tt> form
     */
    public @NotNull BigDecimal toBigDecimal(int precision, @NotNull RoundingMode roundingMode) {
        MathContext context = new MathContext(precision, roundingMode);
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), context);
    }

    /**
     * Determines whether <tt>this</tt> is equal to <tt>that</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li><tt>that</tt> may be any <tt>Object</tt>.</li>
     *  <li>The result may be either <tt>boolean</tt>.</li>
     * </ul>
     *
     * @param that The <tt>Rational</tt> to be compared with <tt>this</tt>
     * @return <tt>this</tt>=<tt>that</tt>
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || Rational.class != that.getClass()) return false;
        Rational r = (Rational) that;
        return denominator.equals(r.denominator) && numerator.equals(r.numerator);
    }

    /**
     * Calculates the hash code of <tt>this</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>(conjecture) The result may be any <tt>int</tt>.</li>
     * </ul>
     *
     * @return <tt>this</tt>'s hash code.
     */
    @Override
    public int hashCode() {
        return 31 * numerator.hashCode() + denominator.hashCode();
    }

    /**
     * Compares <tt>this</tt> to <tt>that</tt>, returning 1, &#x2212;1, or 0 if the answer is "greater than", "less
     * than", or "equal to", respectively.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li><tt>that</tt> cannot be null.</li>
     *  <li>The result may be &#x2212;1, 0, or 1.</li>
     * </ul>
     *
     * @param that The <tt>Rational</tt> to be compared with <tt>this</tt>
     * @return <tt>this</tt> compared to <tt>that</tt>
     */
    @Override
    public int compareTo(@NotNull Rational that) {
        return numerator.multiply(that.denominator).compareTo(that.numerator.multiply(denominator));
    }

    /**
     * Finds the continued fraction of <tt>this</tt>. If we pretend that the result is an array called a of length n,
     * then <tt>this</tt>=a[0]+1/(a[1]+1/(a[2]+...+1/a[n-1]...)). Every rational number has two such representations;
     * this method returns the shortest one.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result is non-null and non-empty. The first element may be any <tt>BigInteger</tt>; the remaining
     *  elements, if any, are all positive. If the result has more than one element, the last element is greater than
     *  1.</li>
     * </ul>
     *
     * @return the continued-fraction-representation of <tt>this</tt>
     */
    public @NotNull List<BigInteger> continuedFraction() {
        List<BigInteger> continuedFraction = new ArrayList<>();
        Rational remainder = this;
        while (true) {
            BigInteger floor = remainder.floor();
            continuedFraction.add(floor);
            remainder = subtract(remainder, of(floor));
            if (remainder == ZERO) break;
            remainder = remainder.invert();
        }
        return continuedFraction;
    }

    /**
     * Returns the <tt>Rational</tt> corresponding to a continued fraction. Every rational number has two continued-
     * fraction representations; either is accepted.
     *
     * <ul>
     *  <li><tt>continuedFraction</tt> must be non-null and non-empty. All elements but the first must be
     *  positive.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param continuedFraction a continued fraction
     * @return a[0]+1/(a[1]+1/(a[2]+...+1/a[n-1]...))
     */
    public static @NotNull Rational fromContinuedFraction(@NotNull List<BigInteger> continuedFraction) {
        Rational x = of(continuedFraction.get(continuedFraction.size() - 1));
        for (int i = continuedFraction.size() - 2; i >= 0; i--) {
            if (i != 0 && continuedFraction.get(i).signum() != 1)
                throw new IllegalArgumentException("all continued fraction elements but the first must be positive");
            x = add(x.invert(), of(continuedFraction.get(i)));
        }
        return x;
    }

    /**
     * Returns the convergents, or rational approximations of <tt>this</tt> formed by truncating its continued fraction
     * at various points. The first element of the result is the floor of <tt>this</tt>, and the last element is
     * <tt>this</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result is a non-null, non-empty list that consists of the convergents of its last element.</li>
     * </ul>
     *
     * @return the convergents of <tt>this</tt>.
     */
    public @NotNull List<Rational> convergents() {
        List<Rational> approximations = new ArrayList<>();
        List<BigInteger> continuedFraction = continuedFraction();
        for (int i = 0; i < continuedFraction.size(); i++) {
            List<BigInteger> truncatedContinuedFraction = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                truncatedContinuedFraction.add(continuedFraction.get(j));
            }
            approximations.add(fromContinuedFraction(truncatedContinuedFraction));
        }
        return approximations;
    }

    /**
     * Returns the positional expansion of (non-negative) <tt>this</tt> in a given base, in the form of a triple of
     * <tt>BigInteger</tt> lists. The first list contains the digits before the decimal point, the second list contains
     * the non-repeating part of the digits after the decimal point, and the third list contains the repeating digits.
     * The digits are given in the usual order: most-significant first. The first two lists may be empty, but the third
     * is always non-empty (if the digits terminate, the third list contains a single zero). The sign of <tt>this</tt>
     * is ignored.
     *
     * <ul>
     *  <li><tt>this</tt> must be non-negative.</li>
     *  <li><tt>base</tt> must be greater than 1.</li>
     *  <li>The elements of the result are all non-null. The elements of the elements are all non-negative. The first
     *  element does not begin with a zero. The last element is non-empty. The second and third lists are minimal; that
     *  is, the sequence (second)(third)(third)(third)... cannot be represented in a more compact way. The lists [1, 2]
     *  and [3, 1, 2] are not minimal, because the sequence [1, 2, 3, 1, 2, 3, 1, 2, ...] can be represented by [] and
     *  [1, 2, 3]. The lists [] and [1, 2, 1, 2] are not minimal either, because the sequence
     *  [1, 2, 1, 2, 1, 2, ...] can be represented by [] and [1, 2].</li>
     * </ul>
     *
     * @param base the base of the positional expansion
     * @return a triple containing the digits before the decimal point, the non-repeating digits after the decimal
     * point, and the repeating digits.
     */
    public @NotNull Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>> positionalNotation(@NotNull BigInteger base) {
        if (signum() == -1)
            throw new IllegalArgumentException("this cannot be negative");
        BigInteger floor = floor();
        List<BigInteger> beforeDecimal = toList(BasicMath.digits(base, floor));
        Rational fractionalPart = subtract(this, of(floor));
        BigInteger numerator = fractionalPart.numerator;
        BigInteger denominator = fractionalPart.denominator;
        BigInteger remainder = numerator.multiply(base);
        int index = 0;
        Integer repeatingIndex;
        List<BigInteger> digits = new ArrayList<>();
        Map<BigInteger, Integer> remainders = new HashMap<>();
        while (true) {
            remainders.put(remainder, index);
            BigInteger digit = remainder.divide(denominator);
            digits.add(digit);
            remainder = remainder.subtract(denominator.multiply(digit)).multiply(base);
            repeatingIndex = remainders.get(remainder);
            if (repeatingIndex != null) break;
            index++;
        }
        List<BigInteger> nonrepeating = new ArrayList<>();
        List<BigInteger> repeating = new ArrayList<>();
        for (int i = 0; i < repeatingIndex; i++) {
            nonrepeating.add(digits.get(i));
        }
        for (int i = repeatingIndex; i < digits.size(); i++) {
            repeating.add(digits.get(i));
        }
        return new Triple<>(beforeDecimal, nonrepeating, repeating);
    }

    /**
     * Creates a <tt>Rational</tt> from a base expansion.
     *
     * <ul>
     *  <li><tt>base</tt> must be greater than 1.</li>
     *  <li><tt>beforeDecimalPoint</tt> must only contain elements greater than or equal to zero and less than
     *  <tt>base</tt>.</li>
     *  <li><tt>nonRepeating</tt> must only contain elements greater than or equal to zero and less than
     *  <tt>base</tt>.</li>
     *  <li><tt>repeating</tt> must only contain elements greater than or equal to zero and less than
     *  <tt>base</tt>. It must also be non-empty; to input a terminating expansion, use one (or more) zeros.</li>
     * </ul>
     *
     * @param base the base of the positional expansion
     * @param beforeDecimalPoint the digits before the decimal point
     * @param nonRepeating the non-repeating portion of the digits after the decimal point
     * @param repeating the repeating portion of the digits after the decimal point
     * @return (beforeDecimalPoint).(nonRepeating)(repeating)(repeating)(repeating)..._(base)
     */
    public static @NotNull Rational fromPositionalNotation(@NotNull BigInteger base, @NotNull List<BigInteger> beforeDecimalPoint,
                                                           @NotNull List<BigInteger> nonRepeating, @NotNull List<BigInteger> repeating) {
        BigInteger floor = BasicMath.fromDigits(base, beforeDecimalPoint);
        BigInteger nonRepeatingInteger = BasicMath.fromDigits(base, nonRepeating);
        BigInteger repeatingInteger = BasicMath.fromDigits(base, repeating);
        Rational nonRepeatingPart = of(nonRepeatingInteger, base.pow(nonRepeating.size()));
        Rational repeatingPart = of(repeatingInteger, base.pow(repeating.size()).subtract(BigInteger.ONE)).divide(base.pow(nonRepeating.size()));
        return add(add(of(floor), nonRepeatingPart), repeatingPart);
    }

    /**
     * Returns the digits of (non-negative) <tt>this</tt> in a given base. The return value is a pair consisting of the
     * digits before the decimal point (in a list) and the digits after the decimal point (in a possibly-infinite
     * iterable). Trailing zeroes are not included.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li><tt>base</tt> must be greater than 1.</li>
     *  <li>the result </li>
     * </ul>
     *
     * @param base the base of the digits
     * @return a pair consisting of the digits before the decimal point and the digits after
     */
    public @NotNull Pair<List<BigInteger>, Iterable<BigInteger>> digits(BigInteger base) {
        Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>> positionalNotation = positionalNotation(base);
        Iterable<BigInteger> afterDecimal;
        assert positionalNotation.c != null;
        if (positionalNotation.c.equals(Arrays.asList(BigInteger.ZERO))) {
            afterDecimal = positionalNotation.b;
        } else {
            afterDecimal = concat(positionalNotation.b, cycle(positionalNotation.c));
        }
        return new Pair<>(positionalNotation.a, afterDecimal);
    }

//    public @NotNull String baseString(@NotNull BigInteger base, int precision, @NotNull RoundingMode roundingMode) {
//        if (signum() == -1) return "-" + negate().baseString(base, precision, roundingMode);
//        Pair<List<BigInteger>, Iterable<BigInteger>> digits = digits(base);
//        int beforeDecimalSize = digits.fst.size();
//        if (precision != 0 && precision < beforeDecimalSize) {
//            int exp = beforeDecimalSize - 1;
//            return divide(base.pow(exp)).baseString(base, precision, roundingMode) + "E+" + MathUtils.baseString(base, BigInteger.valueOf(exp));
//        }
//        List<BigInteger> roundedDigits = new ArrayList<>();
//        if (precision == 0) {
//            for (BigInteger digit : digits.snd) {
//                roundedDigits.add(digit);
//            }
//        } else {
//            int remainingPrecision = precision == 0 ? 0 : precision - beforeDecimalSize;
//            return null;
//        }
//        StringBuilder sb = new StringBuilder();
//        sb.append(MathUtils.baseString(base, floor()));
//        sb.append('.');
//        if (base.compareTo(BigInteger.valueOf(36)) <= 0) {
//            for (BigInteger digit : roundedDigits) {
//                sb.append(digit.compareTo(BigInteger.valueOf(10)) < 0 ? digit : (char) (digit.intValue() - 10 + 'a'));
//            }
//        } else {
//            for (BigInteger digit : roundedDigits) {
//                sb.append('(');
//                sb.append(digit);
//                sb.append(')');
//            }
//        }
//        return sb.toString();
//    }

    /**
     * Creates a <tt>Rational</tt> from a <tt>String</tt>. Valid strings are of the form <tt>a.toString()</tt> or
     * <tt>a.toString() + "/" + b.toString()</tt>, where <tt>a</tt> and <tt>b</tt> are some <tt>BigInteger</tt>s. If
     * the <tt>String</tt> is invalid, the method returns Optional.empty() without throwing an exception; this aids
     * composability.
     *
     * <ul>
     *  <li><tt>s</tt> cannot be null and cannot be of the form <tt>n.toString() + "/0"</tt>, where <tt>n</tt> is some
     *  <tt>BigInteger</tt>.</li>
     *  <li>The result may contain any <tt>Rational</tt>, or be empty.</li>
     * </ul>
     *
     * @param s a string representation of a <tt>Rational</tt>.
     * @return the <tt>Rational</tt> represented by <tt>s</tt>, or null if <tt>s</tt> is invalid.
     */
    public static @NotNull Optional<Rational> read(@NotNull String s) {
        if (s.isEmpty()) return Optional.empty();
        int slashIndex = s.indexOf("/");
        if (slashIndex == -1) {
            Optional<BigInteger> n = Numbers.readBigInteger(s);
            return n.map(Rational::of);
        } else {
            Optional<BigInteger> numerator = Numbers.readBigInteger(s.substring(0, slashIndex));
            if (!numerator.isPresent()) return Optional.empty();
            Optional<BigInteger> denominator = Numbers.readBigInteger(s.substring(slashIndex + 1));
            if (!denominator.isPresent()) return Optional.empty();
            return Optional.of(of(numerator.get(), denominator.get()));
        }
    }

    /**
     * Creates a string representation of <tt>this</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Rational</tt>.</li>
     *  <li>The result is a string in one of two forms: <tt>a.toString()</tt> or <tt>a.toString() + "/" +
     *  b.toString()</tt>, where <tt>a</tt> and <tt>b</tt> are some <tt>BigInteger</tt>s such that <tt>b</tt> is
     *  positive and <tt>a</tt> and <tt>b</tt> have no positive common factors greater than 1.</li>
     * </ul>
     *
     * @return a string representation of <tt>this</tt>.
     */
    public @NotNull String toString() {
        if (denominator.equals(BigInteger.ONE)) {
            return numerator.toString();
        } else {
            return numerator.toString() + "/" + denominator.toString();
        }
    }

    /**
     * Creates a <tt>Rational</tt> from a pair of the numerator and denominator.
     *
     * <ul>
     *  <li><tt>pair</tt> cannot be null, neither element can be null, the second element must be positive, and the two
     *  elements cannot have any positive common factor greater than one.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param pair a <tt>Pair</tt> of the numerator and denominator
     * @return the <tt>Rational</tt> with the given numerator and denominator
     */
    private static @NotNull Rational fromPair(@NotNull Pair<BigInteger, BigInteger> pair) {
        assert pair.a != null;
        assert pair.b != null;
        if (pair.a.equals(BigInteger.ZERO)) return ZERO;
        if (pair.a.equals(BigInteger.ONE) && pair.b.equals(BigInteger.ONE)) return ONE;
        return new Rational(pair.a, pair.b);
    }

    /**
     * @return an <tt>Iterable</tt> that contains every <tt>Rational</tt>.
     */
    public static final @NotNull Iterable<Rational> RATIONALS =
        map(
                Rational::fromPair,
                filter(
                        p -> p.a.gcd(p.b).equals(BigInteger.ONE),
                        pairs(Exhaustive.BIG_INTEGERS, Exhaustive.POSITIVE_BIG_INTEGERS)
                )
        );

    /**
     * @return an <tt>Iterable</tt> that contains every non-negative <tt>Rational</tt>.
     */
    public static @NotNull Iterable<Rational> NONNEGATIVE_RATIONALS =
        map(
                Rational::fromPair,
                filter(
                        p -> p.a.gcd(p.b).equals(BigInteger.ONE),
                        pairs(Exhaustive.NATURAL_BIG_INTEGERS, Exhaustive.POSITIVE_BIG_INTEGERS)
                )
        );

    /**
     * @return an <tt>Iterable</tt> that contains every positive <tt>Rational</tt>.
     */
    public static @NotNull Iterable<Rational> POSITIVE_RATIONALS =
        map(
                Rational::fromPair,
                filter(
                        p -> p.a.gcd(p.b).equals(BigInteger.ONE),
                        map(list -> new Pair<>(list.get(0), list.get(1)), lists(2, Exhaustive.POSITIVE_BIG_INTEGERS))
                )
        );

    /**
     * @return an <tt>Iterable</tt> that contains every negative <tt>Rational</tt>.
     */
    public static @NotNull Iterable<Rational> NEGATIVE_RATIONALS =
        map(
                Rational::fromPair,
                filter(
                        p -> p.a.gcd(p.b).equals(BigInteger.ONE),
                        pairs(Exhaustive.NEGATIVE_BIG_INTEGERS, Exhaustive.POSITIVE_BIG_INTEGERS)
                )
        );

    /**
     * @return an <tt>Iterable</tt> that contains every non-negative <tt>Rational</tt> less than one.
     */
    public static @NotNull Iterable<Rational> NONNEGATIVE_RATIONALS_LESS_THAN_ONE =
        map(
                Rational::fromPair,
                filter(
                        p -> lt(p.a, p.b) && p.a.gcd(p.b).equals(BigInteger.ONE),
                        pairs(Exhaustive.NATURAL_BIG_INTEGERS, Exhaustive.POSITIVE_BIG_INTEGERS)
                )
        );
}
