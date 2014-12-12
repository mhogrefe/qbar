package mho.qbar.objects;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static mho.wheels.iterables.IterableUtils.*;

/**
 * A vector with {@link Rational} coordinates. May be zero-dimensional.
 *
 * There is only one instance of {@code ZERO_DIMENSIONAL}, so it may be compared with other {@code RationalVector}s
 * using {@code ==}.
 */
public class RationalVector {
    /**
     * []
     */
    public static final RationalVector ZERO_DIMENSIONAL = new RationalVector(new ArrayList<>());

    /**
     * The vector's coordinates
     */
    private @NotNull List<Rational> coordinates;

    /**
     * Private constructor for {@code RationalVector}; assumes arguments are valid
     *
     * <ul>
     *  <li>{@code coordinates} cannot have any null elements.</li>
     *  <li>Any {@code RationalVector} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param coordinates the vector's coordinates
     */
    private RationalVector(@NotNull List<Rational> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Creates a {@code RationalVector} from a list of {@code Rational}s. Throws an exception if any element is null.
     * Makes a defensive copy of {@code coordinates}.
     *
     * <ul>
     *  <li>{@code coordinates} cannot have any null elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param coordinates the vector's coordinates
     * @return the {@code RationalVector} with the specified coordinates
     */
    public static @NotNull RationalVector of(@NotNull List<Rational> coordinates) {
        if (coordinates.isEmpty()) return ZERO_DIMENSIONAL;
        if (any(a -> a == null, coordinates))
            throw new NullPointerException();
        return new RationalVector(toList(coordinates));
    }

    /**
     * Creates a one-dimensional {@code RationalVector} from a single {@code Rational} coordinate.
     *
     * <ul>
     *  <li>{@code a} must be non-null.</li>
     *  <li>The result is a one-dimensional vector.</li>
     * </ul>
     *
     * @param a the vector's coordinate
     * @return [a]
     */
    public static @NotNull RationalVector of(@NotNull Rational a) {
        return new RationalVector(Arrays.asList(a));
    }
}
