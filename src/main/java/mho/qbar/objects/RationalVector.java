package mho.qbar.objects;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A vector with {@link Rational} coordinates. May be zero-dimensional.
 */
public class RationalVector {
    /**
     * The vector's coordinates
     */
    private @NotNull List<Rational> coordinates;

    /**
     * Private cosntructor for {@code RationalVector}
     *
     * @param coordinates the vector's coordinates
     */
    private RationalVector(@NotNull List<Rational> coordinates) {
        this.coordinates = coordinates;
    }
}
