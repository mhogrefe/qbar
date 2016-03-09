package mho.qbar.objects;

import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

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
    public static final @NotNull Algebraic PHI = new Algebraic(Polynomial.read("x^2-x-1").get(), 1);

    private final int rootIndex;
    private final Polynomial minimalPolynomial;
    private final @NotNull Optional<Rational> rational;

    private Algebraic(@NotNull Polynomial minimalPolynomial, int rootIndex) {
        this.rootIndex = rootIndex;
        this.minimalPolynomial = minimalPolynomial;
        rational = Optional.empty();
    }

    private Algebraic(@NotNull Rational rational) {
        rootIndex = 0;
        minimalPolynomial = Polynomial.fromRoot(rational);
        this.rational = Optional.of(rational);
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
        if (rootIndex >= polynomial.rootCount()) {
            throw new IllegalArgumentException();
        }
        List<Polynomial> factors = polynomial.factor();
        if (factors.size() == 1) {
            return new Algebraic(polynomial, rootIndex);
        }
        List<Pair<Polynomial, Integer>> polyRootPairs = new ArrayList<>();
        List<Real> realRoots = new ArrayList<>();
        for (Polynomial factor : factors) {
            for (int i = 0; i < factor.rootCount(); i++) {
                polyRootPairs.add(new Pair<>(factor, i));
                realRoots.add(Real.root(factor, i));
            }
        }
        Pair<Polynomial, Integer> pair = polyRootPairs.get(Real.root(polynomial, rootIndex).match(realRoots));
        polynomial = pair.a;
        rootIndex = pair.b;
        if (polynomial.degree() == 1) {
            if (rootIndex != 0) {
                throw new IllegalArgumentException();
            }
            return new Algebraic(Rational.of(polynomial.coefficient(0).negate(), polynomial.coefficient(1)));
        }
        return new Algebraic(pair.a, pair.b);
    }

    public @NotNull Algebraic of(@NotNull Rational rational) {
        return new Algebraic(rational);
    }

    public @NotNull Real realValue() {
        if (rational.isPresent()) {
            return Real.of(rational.get());
        } else {
            return Real.root(minimalPolynomial::signum, minimalPolynomial.powerOfTwoIsolatingInterval(rootIndex));
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
        } else {
            assertTrue(this, minimalPolynomial.degree() > 1);
            assertTrue(this, minimalPolynomial.isIrreducible());
            assertTrue(this, rootIndex >= 0);
            assertTrue(this, rootIndex < minimalPolynomial.rootCount());
        }
        if (equals(ZERO)) {
            assertTrue(this, this == ZERO);
        }
        if (equals(ONE)) {
            assertTrue(this, this == ONE);
        }
    }
}
