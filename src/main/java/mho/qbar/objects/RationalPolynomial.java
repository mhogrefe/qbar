package mho.qbar.objects;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static mho.wheels.iterables.IterableUtils.*;

/**
 * <p>A univariate polynomial in x with {@code Rational} coefficients.
 *
 * <p>There is only one instance of {@code ZERO}, and one instance of {@code ONE}, so these may be compared with other
 * {@code RationalPolynomial}s using {@code ==}. This is not true for {@code X}.
 *
 * <p>This class uses a little-endian dense representation; in other words, the coefficient of x<sup>i</sup> is located
 * at the ith position of the coefficient list. The list contains no trailing zeros. Zero is represented by the empty
 * list.
 *
 * <p>This class is immutable.
 */
public class RationalPolynomial {
    /**
     * 0
     */
    public static final @NotNull RationalPolynomial ZERO = new RationalPolynomial(new ArrayList<>());

    /**
     * 1
     */
    public static final @NotNull RationalPolynomial ONE = new RationalPolynomial(Arrays.asList(Rational.ONE));

    /**
     * x
     */
    public static final @NotNull RationalPolynomial X = new RationalPolynomial(
            Arrays.asList(Rational.ZERO, Rational.ONE)
    );

    /**
     * The polynomial's coefficients. The coefficient of x<sup>i</sup> is at the ith position.
     */
    private final @NotNull List<Rational> coefficients;

    /**
     * Private constructor for {@code Polynomial}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code coefficients} cannot have any null elements and cannot end in a 0.</li>
     *  <li>Any {@code RationalPolynomial} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param coefficients the polynomial's coefficients
     */
    private RationalPolynomial(@NotNull List<Rational> coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * Creates a {@code RationalPolynomial} from a list of {@code Rational} coefficients. Throws an exception if any
     * coefficient is null. Makes a defensive copy of {@code coefficients}. Throws out any trailing zeros.
     *
     * <ul>
     *  <li>{@code coefficients} cannot have any null elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is at most |{@code coefficients}|
     *
     * @param coefficients the polynomial's coefficients, from least to most significant
     * @return the {@code RationalPolynomial} with the specified coefficients
     */
    public static @NotNull RationalPolynomial of(@NotNull List<Rational> coefficients) {
        if (any(i -> i == null, coefficients))
            throw new NullPointerException();
        int actualSize = coefficients.size();
        for (int i = coefficients.size() - 1; i >= 0; i--) {
            if (coefficients.get(i) == Rational.ZERO) {
                actualSize--;
            } else {
                break;
            }
        }
        if (actualSize == 0) return ZERO;
        if (actualSize == 1 && coefficients.get(0) == Rational.ONE) return ONE;
        return new RationalPolynomial(toList(take(actualSize, coefficients)));
    }
}
