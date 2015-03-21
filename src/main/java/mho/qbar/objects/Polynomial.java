package mho.qbar.objects;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

public class Polynomial {
    private final @NotNull List<BigInteger> coefficients;

    private Polynomial(@NotNull List<BigInteger> coefficients) {
        this.coefficients = coefficients;
    }
}
