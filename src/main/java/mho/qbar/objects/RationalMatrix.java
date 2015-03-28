package mho.qbar.objects;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
        if (rows.isEmpty()) {
            return replicate(width, RationalVector.ZERO_DIMENSIONAL);
        } else {
            return map(RationalVector::of, transpose(map(r -> r, rows)));
        }
    }

    /**
     * Returns one of {@code this}'s row vectors. 0-indexed.
     *
     * <ul>
     *  <li>{@code this} must be have at least one row.</li>
     *  <li>{@code i} must be non-negative.</li>
     *  <li>{@code i} must be less than the height of {@code this}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param i the 0-based row index
     * @return the {@code i}th row of {@code this}
     */
    public @NotNull RationalVector row(int i) {
        return rows.get(i);
    }

    /**
     * Returns one of {@code this}'s column vectors. 0-indexed.
     *
     * <ul>
     *  <li>{@code this} must have at least one column.</li>
     *  <li>{@code j} must be non-negative.</li>
     *  <li>{@code j} must be less than the width of {@code this}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param j the 0-based column index
     * @return the {@code j}th column of {@code this}
     */
    public @NotNull RationalVector column(int j) {
        if (rows.isEmpty()) {
            if (j >= width)
                throw new ArrayIndexOutOfBoundsException();
            return RationalVector.ZERO_DIMENSIONAL;
        } else {
            return RationalVector.of(toList(map(r -> r.x(j), rows)));
        }
    }

    /**
     * Returns one of {@code this}'s elements. 0-indexed.
     *
     * <ul>
     *  <li>{@code this} must have at least one row and at least one column.</li>
     *  <li>{@code i} must be non-negative.</li>
     *  <li>{@code j} must be non-negative.</li>
     *  <li>{@code i} must be less than the height of {@code this}.</li>
     *  <li>{@code j} must be less than the width of {@code this}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param i the 0-based row index
     * @param j the 0-based column index
     * @return the element of {@code this} in the {@code i}th row and {@code j}th column
     */
    public @NotNull Rational element(int i, int j) {
        return rows.get(i).x(j);
    }

    /**
     * Creates a {@code RationalMatrix} from its row vectors. If an empty list is given, the matrix with height 0 and
     * width 0 is returned. This method cannot be used to create matrices with 0 height and nonzero width.
     *
     * <ul>
     *  <li>{@code this} cannot be null, and every element must have the same dimension.</li>
     *  <li>The result either has nonzero height, or 0 width and 0 height.</li>
     * </ul>
     *
     * @param rows the matrix's rows
     * @return a {@code RationalMatrix} with the given rows
     */
    public static @NotNull RationalMatrix fromRows(@NotNull List<RationalVector> rows) {
        if (any(a -> a == null, rows))
            throw new NullPointerException();
        if (!same(map(RationalVector::dimension, rows)))
            throw new IllegalArgumentException("rows must have same dimension");
        return new RationalMatrix(toList(rows), rows.isEmpty() ? 0 : rows.get(0).dimension());
    }

    /**
     * Creates a {@code RationalMatrix} from its column vectors. If an empty list is given, the matrix with height 0
     * and width 0 is returned. This method cannot be used to create matrices with 0 width and nonzero height.
     *
     * <ul>
     *  <li>{@code this} cannot be null, and every element must have the same dimension.</li>
     *  <li>The result either has nonzero width, or 0 width and 0 height.</li>
     * </ul>
     *
     * @param columns the matrix's columns
     * @return a {@code RationalMatrix} with the given columns
     */
    public static @NotNull RationalMatrix fromColumns(@NotNull List<RationalVector> columns) {
        if (any(a -> a == null, columns))
            throw new NullPointerException();
        if (!same(map(RationalVector::dimension, columns)))
            throw new IllegalArgumentException("columns must have same dimension");
        if (columns.isEmpty()) {
            return new RationalMatrix(new ArrayList<>(), 0);
        } else {
            return new RationalMatrix(
                    toList(map(RationalVector::of, transpose(map(c -> c, columns)))),
                    columns.size()
            );
        }
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
