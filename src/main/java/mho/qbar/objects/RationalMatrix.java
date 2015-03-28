package mho.qbar.objects;

import mho.wheels.iterables.IterableUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

import static mho.wheels.iterables.IterableUtils.*;

/**
 * <p>A matrix with {@link Rational} elements. The number of rows is the height, and the number of rows is the width.
 * Either the height or the width, or both, may be 0; 0-height matrices are written {@code "[]#w"}, where {@code w} is
 * the width. When referring to elements, the (i, j)th element is the element in row i and column j.
 *
 * <p>This class is immutable.
 */
public final class RationalMatrix {
    /**
     * The matrix's rows
     */
    private final @NotNull List<RationalVector> rows;

    /**
     * The matrix's width. The width can usually be inferred from {@code rows}, except when the height is 0, in which
     * case {@code rows} is empty.
     */
    private final int width;

    /**
     * Private constructor for {@code RationalMatrix}; assumes arguments are valid
     *
     * <ul>
     *  <li>{@code rows} cannot have any null elements.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The dimension of every {@code RationalVector} in rows must be {@code width}.</li>
     *  <li>Any {@code RationalMatrix} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param rows the matrix's rows
     * @param width the matrix's width
     */
    private RationalMatrix(@NotNull List<RationalVector> rows, int width) {
        this.rows = rows;
        this.width = width;
    }

    /**
     * Returns an {@code Iterable} over this {@code RationalMatrix}'s rows. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result is finite, contains no nulls, and every element has the same dimension.</li>
     * </ul>
     *
     * Length is height({@code this})
     *
     * @return an {@code Iterable} over this {@code RationalMatrix}'s rows
     */
    public @NotNull Iterable<RationalVector> rowIterable() {
        return () -> new Iterator<RationalVector>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < rows.size();
            }

            @Override
            public RationalVector next() {
                return rows.get(i++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * Returns an {@code Iterable} over this {@code RationalMatrix}'s columns. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result is finite, contains no nulls, and every element has the same dimension.</li>
     * </ul>
     *
     * Length is width({@code this})
     *
     * @return an {@code Iterable} over this {@code RationalMatrix}'s columns
     */
    public @NotNull Iterable<RationalVector> columnIterable() {
        return map(RationalVector::of, transpose(map(r -> r, rows)));
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        return rows.isEmpty() ? "[]#" + width : rows.toString();
    }
}
