package mho.qbar.objects;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>A univariate polynomial in x with {@code BigInteger} coefficients.
 *
 * <p>There is only one instance of {@code ZERO}, and one instance of {@code ONE}, so these may be compared with other
 * {@code Polynomial}s using {@code ==}. This is not true for {@code X}.
 *
 * <p>This class uses a little-endian dense representation; in other words, the coefficient of x<sup>i</sup> is located
 * at the ith position of the coefficient list. The list contains no trailing zeros.
 *
 * <p>This class is immutable.
 */
public class Polynomial {
    /**
     * 0
     */
    public static final @NotNull Polynomial ZERO = new Polynomial(new ArrayList<>());

    /**
     * 1
     */
    public static final @NotNull Polynomial ONE = new Polynomial(Arrays.asList(BigInteger.ONE));

    /**
     * x
     */
    public static final @NotNull Polynomial X = new Polynomial(Arrays.asList(BigInteger.ZERO, BigInteger.ONE));

    private final @NotNull List<BigInteger> coefficients;

    private Polynomial(@NotNull List<BigInteger> coefficients) {
        this.coefficients = coefficients;
    }
}
