package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

/**
 * <p>The {@code Algebraic} class uniquely represents real algebraic numbers.</p>
 *
 * <p>There is only one instance of {@code ZERO} and one instance of {@code ONE}, so these may be compared with other
 * {@code Algebraic}s using {@code ==}.</p>
 *
 * <p>This class is immutable.</p>
 */
public class Algebraic implements Comparable<Algebraic> {
    /**
     * 0
     */
    public static final @NotNull Algebraic ZERO = new Algebraic(Rational.ZERO);

    /**
     * 1
     */
    public static final @NotNull Algebraic ONE = new Algebraic(Rational.ONE);

    /**
     * 10
     */
    public static final @NotNull Algebraic TEN = new Algebraic(Rational.TEN);

    /**
     * 2
     */
    public static final @NotNull Algebraic TWO = new Algebraic(Rational.TWO);

    /**
     * -1
     */
    public static final @NotNull Algebraic NEGATIVE_ONE = new Algebraic(Rational.NEGATIVE_ONE);

    /**
     * -1
     */
    public static final @NotNull Algebraic ONE_HALF = new Algebraic(Rational.ONE_HALF);

    /**
     * φ, the golden ratio
     */
    public static final @NotNull Algebraic PHI = of(Polynomial.read("x^2-x-1").get(), 1);

    private final int rootIndex;
    private final @NotNull Polynomial minimalPolynomial;
    private final @NotNull Optional<Rational> rational;

    private final @NotNull  Interval isolatingInterval;
    private final int mpRootCount;

    private Algebraic(
            @NotNull Polynomial minimalPolynomial,
            int rootIndex,
            @NotNull Interval isolatingInterval,
            int mpRootCount
    ) {
        this.rootIndex = rootIndex;
        this.minimalPolynomial = minimalPolynomial;
        rational = Optional.empty();
        this.isolatingInterval = isolatingInterval;
        this.mpRootCount = mpRootCount;
    }

    private Algebraic(@NotNull Rational rational) {
        rootIndex = 0;
        minimalPolynomial = Polynomial.fromRoot(rational);
        this.rational = Optional.of(rational);
        isolatingInterval = Interval.of(rational);
        mpRootCount = 1;
    }

    public static @NotNull Algebraic of(@NotNull Polynomial polynomial, int rootIndex) {
        if (rootIndex < 0) {
            throw new IllegalArgumentException();
        }
        if (polynomial == Polynomial.ZERO) {
            throw new IllegalArgumentException();
        }
        if (polynomial.degree() == 1) {
            if (rootIndex != 0) {
                throw new IllegalArgumentException();
            }
            Rational r = Rational.of(polynomial.coefficient(0).negate(), polynomial.coefficient(1));
            if (r == Rational.ZERO) return ZERO;
            if (r == Rational.ONE) return ONE;
            return new Algebraic(r);
        }
        polynomial = polynomial.squareFreePart();
        if (polynomial == Polynomial.ONE) {
            throw new IllegalArgumentException();
        }
        int rootCount = polynomial.rootCount();
        if (rootIndex >= rootCount) {
            throw new IllegalArgumentException();
        }
        List<Polynomial> factors = polynomial.factor();
        if (factors.size() == 1) {
            return new Algebraic(polynomial, rootIndex, polynomial.powerOfTwoIsolatingInterval(rootIndex), rootCount);
        }
        List<Pair<Polynomial, Integer>> polyRootPairs = new ArrayList<>();
        List<Real> realRoots = new ArrayList<>();
        List<Interval> isolatingIntervals = new ArrayList<>();
        List<Integer> rootCounts = new ArrayList<>();
        for (Polynomial factor : factors) {
            List<Interval> factorIsolatingIntervals = factor.powerOfTwoIsolatingIntervals();
            int factorRootCount = factor.rootCount();
            for (int i = 0; i < factorRootCount; i++) {
                polyRootPairs.add(new Pair<>(factor, i));
                realRoots.add(Real.root(factor::signum, factorIsolatingIntervals.get(i)));
                isolatingIntervals.add(factorIsolatingIntervals.get(i));
                rootCounts.add(factorRootCount);
            }
        }
        Interval isolatingInterval = polynomial.isolatingInterval(rootIndex);
        int matchIndex = Real.root(polynomial::signum, isolatingInterval).match(realRoots);
        Pair<Polynomial, Integer> pair = polyRootPairs.get(matchIndex);
        polynomial = pair.a;
        rootIndex = pair.b;
        if (polynomial.degree() == 1) {
            if (rootIndex != 0) {
                throw new IllegalArgumentException();
            }
            Rational r = Rational.of(polynomial.coefficient(0).negate(), polynomial.coefficient(1));
            if (r == Rational.ZERO) return ZERO;
            if (r == Rational.ONE) return ONE;
            return new Algebraic(r);
        }
        return new Algebraic(pair.a, pair.b, isolatingIntervals.get(matchIndex), rootCounts.get(matchIndex));
    }

    public static @NotNull Algebraic of(@NotNull Rational rational) {
        if (rational == Rational.ZERO) return ZERO;
        if (rational == Rational.ONE) return ONE;
        return new Algebraic(rational);
    }

    public @NotNull Real realValue() {
        if (rational.isPresent()) {
            return Real.of(rational.get());
        } else {
            return Real.root(minimalPolynomial::signum, isolatingInterval);
        }
    }

    public int signum() {
        if (rational.isPresent()) {
            return rational.get().signum();
        } else {
            return realValue().signum();
        }
    }

    public @NotNull Algebraic negate() {
        if (this == ZERO) return ZERO;
        if (rational.isPresent()) {
            Rational r = rational.get();
            if (r.equals(Rational.NEGATIVE_ONE)) return ONE;
            return new Algebraic(r.negate());
        } else {
            return new Algebraic(
                    minimalPolynomial.reflect(),
                    mpRootCount - rootIndex - 1,
                    isolatingInterval.negate(),
                    mpRootCount
            );
        }
    }

    public @NotNull Algebraic invert() {
        if (this == ZERO) {
            throw new ArithmeticException();
        }
        if (this == ONE) return ONE;
        if (rational.isPresent()) {
            return new Algebraic(rational.get().invert());
        } else {
            Polynomial inverseMP = minimalPolynomial.invertRoots();
            int negativeRootCount = minimalPolynomial.rootCount(Interval.lessThanOrEqualTo(Rational.ZERO));
            int positiveRootCount = mpRootCount - negativeRootCount;
            int inverseRootIndex;
            if (signum() == 1) {
                inverseRootIndex = 2 * negativeRootCount + positiveRootCount - rootIndex - 1;
            } else {
                inverseRootIndex = negativeRootCount - rootIndex - 1;
            }
            Interval inverseIsolatingInterval = inverseMP.isolatingInterval(inverseRootIndex);
            return new Algebraic(inverseMP, inverseRootIndex, inverseIsolatingInterval, mpRootCount);
        }
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        Algebraic algebraic = (Algebraic) that;
        if (rational.isPresent() != algebraic.rational.isPresent()) return false;
        if (rational.isPresent()) {
            return rational.get().equals(algebraic.rational.get());
        } else {
            return rootIndex == algebraic.rootIndex && minimalPolynomial.equals(algebraic.minimalPolynomial);
        }
    }

    @Override
    public int hashCode() {
        if (rational.isPresent()) {
            return 31 + rational.get().hashCode();
        } else {
            return 31 * rootIndex + minimalPolynomial.hashCode();
        }
    }

    @Override
    public int compareTo(@NotNull Algebraic that) {
        if (this == that) return 0;
        if (rational.isPresent() && that.rational.isPresent()) {
            return rational.get().compareTo(that.rational.get());
        } else if (rational.isPresent()) {
            return -that.realValue().compareTo(rational.get());
        } else if (that.rational.isPresent()) {
            return realValue().compareTo(that.rational.get());
        } else {
            return realValue().compareTo(that.realValue());
        }
    }

    public static @NotNull Optional<Algebraic> genericRead(
            @NotNull String s,
            @NotNull Function<String, Optional<Polynomial>> polynomialHandler
    ) {
        if (s.startsWith("root ")) {
            s = s.substring(5);
            int ofIndex = s.indexOf(" of ");
            if (ofIndex == -1) return Optional.empty();
            Optional<Integer> oRootIndex = Readers.readInteger(s.substring(0, ofIndex));
            if (!oRootIndex.isPresent()) return Optional.empty();
            int rootIndex = oRootIndex.get();
            if (rootIndex < 0) return Optional.empty();
            Optional<Polynomial> oMinimalPolynomial = polynomialHandler.apply(s.substring(ofIndex + 4));
            if (!oMinimalPolynomial.isPresent()) return Optional.empty();
            Algebraic x;
            try {
                x = of(oMinimalPolynomial.get(), rootIndex);
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
            if (x.toString().equals(s)) {
                return Optional.of(x);
            } else {
                return Optional.empty();
            }
        } else if (s.contains("sqrt(")) {
            BigInteger denominator;
            int slashIndex = s.indexOf('/');
            if (slashIndex != -1) {
                if (head(s) != '(') return Optional.empty();
                if (s.charAt(slashIndex - 1) != ')') return Optional.empty();
                Optional<BigInteger> oDenominator = Readers.readBigInteger(s.substring(slashIndex + 1));
                if (!oDenominator.isPresent()) return Optional.empty();
                denominator = oDenominator.get();
                if (denominator.signum() != 1) return Optional.empty();
                s = s.substring(1, slashIndex - 1);
            } else {
                denominator = BigInteger.ONE;
            }
            int plusIndex = s.indexOf('+');
            int minusIndex = s.indexOf('-', 1);
            BigInteger constant;
            if (plusIndex != -1) {
                Optional<BigInteger> oConstant = Readers.readBigInteger(s.substring(0, plusIndex));
                if (!oConstant.isPresent()) return Optional.empty();
                constant = oConstant.get();
                if (constant.equals(BigInteger.ZERO)) return Optional.empty();
                s = s.substring(plusIndex + 1);
            } else if (minusIndex != -1) {
                Optional<BigInteger> oConstant = Readers.readBigInteger(s.substring(0, minusIndex));
                if (oConstant.isPresent()) return Optional.empty();
                constant = oConstant.get();
                if (constant.equals(BigInteger.ZERO)) return Optional.empty();
                s = s.substring(minusIndex);
            } else {
                constant = BigInteger.ZERO;
            }
            BigInteger beforeRadical;
            int starIndex = s.indexOf('*');
            if (starIndex == -1) {
                if (s.isEmpty()) return Optional.empty();
                if (head(s) == '-') {
                    beforeRadical = IntegerUtils.NEGATIVE_ONE;
                    s = tail(s);
                } else {
                    beforeRadical = BigInteger.ONE;
                }
            } else {
                Optional<BigInteger> oBeforeRadical = Readers.readBigInteger(s.substring(0, starIndex));
                if (!oBeforeRadical.isPresent()) return Optional.empty();
                beforeRadical = oBeforeRadical.get();
                if (beforeRadical.equals(BigInteger.ZERO)) return Optional.empty();
                if (beforeRadical.abs().equals(BigInteger.ONE)) return Optional.empty();
                s = s.substring(starIndex + 1);
            }
            if (!s.startsWith("sqrt(")) return Optional.empty();
            if (last(s) != ')') return Optional.empty();
            Optional<BigInteger> oUnderRadical = Readers.readBigInteger(s.substring(5, s.length() - 1));
            if (!oUnderRadical.isPresent()) return Optional.empty();
            BigInteger underRadical = oUnderRadical.get();
            if (underRadical.signum() != 1) return Optional.empty();
            Optional<Algebraic> ox = fromQEForm(constant, beforeRadical, underRadical, denominator);
            if (!ox.isPresent()) return Optional.empty();
            return ox;
        } else {
            Optional<Rational> or = Rational.read(s);
            if (!or.isPresent()) return Optional.empty();
            return Optional.of(of(or.get()));
        }
    }

    private static @NotNull Optional<Algebraic> fromQEForm(
            @NotNull BigInteger constant,
            @NotNull BigInteger beforeRadical,
            @NotNull BigInteger underRadical,
            @NotNull BigInteger denominator
    ) {
        if (!MathUtils.gcd(Arrays.asList(constant, beforeRadical, denominator)).equals(BigInteger.ONE)) {
            return Optional.empty();
        }
        Rational a = Rational.of(denominator).shiftRight(1);
        Rational b = Rational.of(constant.negate());
        int rootIndex = beforeRadical.signum() == 1 ? 1 : 0;
        BigInteger discriminant = underRadical.multiply(beforeRadical.abs().pow(2));
        if (discriminant.bitLength() >= 32) return Optional.empty();
        if (!MathUtils.largestPerfectPowerFactor(2, discriminant).equals(beforeRadical.abs())) return Optional.empty();
        Rational c = b.pow(2).subtract(Rational.of(discriminant)).divide(a).shiftRight(2);
        Polynomial mp = RationalPolynomial.of(Arrays.asList(c, b, a)).constantFactor().b;
        return Optional.of(of(mp, rootIndex));
    }

    public static @NotNull Optional<Algebraic> read(@NotNull String s) {
        return genericRead(s, Polynomial::read);
    }

    public static @NotNull Optional<Algebraic> read(int maxExponent, @NotNull String s) {
        return genericRead(s, t -> Polynomial.read(maxExponent, t));
    }

    public @NotNull String toString() {
        if (rational.isPresent()) {
            return rational.get().toString();
        } else {
            if (minimalPolynomial.degree() == 2) {
                BigInteger a = minimalPolynomial.coefficient(2);
                BigInteger b = minimalPolynomial.coefficient(1);
                BigInteger c = minimalPolynomial.coefficient(0);
                BigInteger discriminant = b.pow(2).subtract(a.multiply(c).shiftLeft(2));
                if (discriminant.bitLength() < 32) {
                    BigInteger beforeRadical = MathUtils.largestPerfectPowerFactor(2, discriminant);
                    BigInteger underRadical = discriminant.divide(beforeRadical.pow(2));
                    BigInteger denominator = a.shiftLeft(1);
                    BigInteger gcd = MathUtils.gcd(Arrays.asList(beforeRadical, denominator, b));
                    beforeRadical = beforeRadical.divide(gcd);
                    BigInteger constant = b.divide(gcd).negate();
                    denominator = denominator.divide(gcd);
                    boolean nonTrivialBeforeRadical = !beforeRadical.equals(BigInteger.ONE);
                    boolean nonTrivialConstant = !constant.equals(BigInteger.ZERO);
                    boolean nonTrivialDenominator = !denominator.equals(BigInteger.ONE);

                    StringBuilder sb = new StringBuilder();
                    if (nonTrivialDenominator) {
                        sb.append('(');
                    }
                    if (nonTrivialConstant) {
                        sb.append(constant);
                    }
                    if (rootIndex == 0) {
                        sb.append('-');
                    } else if (nonTrivialConstant) {
                        sb.append('+');
                    }
                    if (nonTrivialBeforeRadical) {
                        sb.append(beforeRadical).append('*');
                    }
                    sb.append("sqrt(").append(underRadical).append(')');
                    if (nonTrivialDenominator) {
                        sb.append(")/").append(denominator);
                    }
                    return sb.toString();
                }
            }
            return "root " + rootIndex + " of " + minimalPolynomial;
        }
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code Algebraic} used outside this
     * class.
     */
    public void validate() {
        if (rational.isPresent()) {
            assertEquals(this, rootIndex, 0);
            assertEquals(this, minimalPolynomial, Polynomial.fromRoot(rational.get()));
            assertEquals(this, isolatingInterval, Interval.of(rational.get()));
            assertEquals(this, mpRootCount, 0);
        } else {
            assertTrue(this, minimalPolynomial.degree() > 1);
            assertTrue(this, minimalPolynomial.isIrreducible());
            assertTrue(this, rootIndex >= 0);
            assertTrue(this, rootIndex < mpRootCount);
            assertEquals(this, isolatingInterval, minimalPolynomial.powerOfTwoIsolatingInterval(rootIndex));
            assertEquals(this, mpRootCount, minimalPolynomial.rootCount());
        }
        if (equals(ZERO)) {
            assertTrue(this, this == ZERO);
        }
        if (equals(ONE)) {
            assertTrue(this, this == ONE);
        }
    }
}
