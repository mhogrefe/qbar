package mho.qbar.objects;

import org.jetbrains.annotations.NotNull;

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
    private final Rational rational;

    private Algebraic(@NotNull Polynomial minimalPolynomial, int rootIndex) {
        this.rootIndex = rootIndex;
        this.minimalPolynomial = minimalPolynomial;
        rational = null;
    }

    private Algebraic(@NotNull Rational rational) {
        rootIndex = 0;
        minimalPolynomial = null;
        this.rational = rational;
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code Algebraic} used outside this
     * class.
     */
    public void validate() {
        if (minimalPolynomial == null) {
            assertEquals(this, rootIndex, 0);
            assertNotNull(this, rational);
        } else {
            assertNull(this, rational);
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
