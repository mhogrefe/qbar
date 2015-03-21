package mho.qbar.objects;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private final @NotNull List<Rational> coefficients;

    private RationalPolynomial(@NotNull List<Rational> coefficients) {
        this.coefficients = coefficients;
    }
}
