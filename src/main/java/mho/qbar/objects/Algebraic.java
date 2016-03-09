package mho.qbar.objects;

import mho.wheels.iterables.IterableUtils;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.math.MathUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.testing.Testing.*;

/**
 * <p>The {@code Algebraic} class uniquely represents real algebraic numbers.</p>
 *
 * <p>There is only one instance of {@code ZERO} and one instance of {@code ONE}, so these may be compared with other
 * {@code Algebraic}s using {@code ==}.</p>
 *
 * <p>This class is immutable.</p>
 */
public class Algebraic {
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
            return new Algebraic(Rational.of(polynomial.coefficient(0).negate(), polynomial.coefficient(1)));
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
            return new Algebraic(Rational.of(polynomial.coefficient(0).negate(), polynomial.coefficient(1)));
        }
        return new Algebraic(pair.a, pair.b, isolatingIntervals.get(matchIndex), rootCounts.get(matchIndex));
    }

    public @NotNull Algebraic of(@NotNull Rational rational) {
        return new Algebraic(rational);
    }

    public @NotNull Real realValue() {
        if (rational.isPresent()) {
            return Real.of(rational.get());
        } else {
            return Real.root(minimalPolynomial::signum, isolatingInterval);
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
            assertTrue(this, rootIndex < minimalPolynomial.rootCount());
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
