package mho.qbar.objects;

import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>A multivariate polynomial with {@link BigInteger} coefficients.</p>
 *
 * <p>There is only one instance of {@code ZERO}, and one instance of {@code ONE}, so these may be compared with other
 * {@code MultivariatePolynomial}s using {@code ==}.</p>
 *
 * <p></p>
 *
 * <p>This class is immutable.</p>
 */
public class MultivariatePolynomial {
    private final @NotNull List<Pair<BigInteger, Monomial>> terms;

    private MultivariatePolynomial(@NotNull List<Pair<BigInteger, Monomial>> terms) {
        this.terms = terms;
    }

    @Override
    public boolean equals(Object that) {
        return this == that ||
                that != null && getClass() == that.getClass() && terms.equals(((MultivariatePolynomial) that).terms);

    }

    @Override
    public int hashCode() {
        return terms.hashCode();
    }
}
