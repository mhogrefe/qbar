package mho.qbar.objects;

import mho.wheels.ordering.comparators.ShortlexComparator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static mho.wheels.iterables.IterableUtils.*;

/**
 * <p>A vector with {@link Rational} coordinates. May be zero-dimensional.
 *
 * <p>There is only one instance of {@code ZERO_DIMENSIONAL}, so it may be compared with other {@code RationalVector}s
 * using {@code ==}.
 *
 * <p>This class is immutable.
 */
public class RationalVector implements Comparable<RationalVector> {
    /**
     * []
     */
    public static final RationalVector ZERO_DIMENSIONAL = new RationalVector(new ArrayList<>());

    /**
     * Used by {@link mho.qbar.objects.RationalVector#compareTo}
     */
    private static Comparator<Iterable<Rational>> RATIONAL_ITERABLE_COMPARATOR = new ShortlexComparator<>();

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

    /**
     * Returns a defensive copy of this {@code RationalVector}'s coordinates
     *
     * <ul>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return the numerator
     */
    public @NotNull List<Rational> getCoordinates() {
        return toList(coordinates);
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively. Shortlex ordering is used; shorter {@code RationalVector}s come before longer ones,
     * and two {@code RationalVector}s of the same length are compared left-to-right, element by element.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that The {@code RationalVector} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull RationalVector that) {
        return RATIONAL_ITERABLE_COMPARATOR.compare(coordinates, that.coordinates);
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code RationalVector} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        return coordinates.equals(((RationalVector) that).coordinates);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>The result is a {@code String} which begins with '[' and ends with ']', and the rest is a possibly-empty
     *  list of comma-separated {@code String}s, each of which validly represents a {@code Rational} (see
     *  {@link Rational#toString}.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        return coordinates.toString();
    }
}
