package mho.qbar.objects;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RationalPolynomial {
    private final @NotNull List<Rational> coefficients;

    private RationalPolynomial(@NotNull List<Rational> coefficients) {
        this.coefficients = coefficients;
    }
}
