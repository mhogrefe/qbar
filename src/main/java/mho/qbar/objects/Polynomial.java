package mho.qbar.objects;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
