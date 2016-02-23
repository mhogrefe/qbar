package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.iterables.NoRemoveIterable;
import mho.wheels.ordering.comparators.LexComparator;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>A matrix with {@link Polynomial} elements. The number of rows is the height, and the number of rows is the width.
 * Either the height or the width, or both, may be 0; 0-height matrices are written {@code "[]#w"}, where {@code w} is
 * the width. When referring to elements, the (<i>i</i>, <i>j</i>)th element is the element in row <i>i</i> and column
 * <i>j</i>.</p>
 *
 * <p>This class is immutable.</p>
 */
public class PolynomialMatrix implements Comparable<PolynomialMatrix> {
    /**
     * Used by {@link PolynomialMatrix#compareTo}
     */
    private static final Comparator<Iterable<PolynomialVector>> POLYNOMIAL_VECTOR_ITERABLE_COMPARATOR =
            new LexComparator<>();

    /**
     * The matrix's rows
     */
    private final @NotNull List<PolynomialVector> rows;

    /**
     * The matrix's width. The width can usually be inferred from {@code rows}, except when the height is 0, in which
     * case {@code rows} is empty and this field is the only way to determine the width.
     */
    private final int width;

    /**
     * Private constructor for {@code PolynomialMatrix}; assumes arguments are valid
     *
     * <ul>
     *  <li>{@code rows} cannot have any null elements.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The dimension of every {@code PolynomialVector} in rows must be {@code width}.</li>
     *  <li>Any {@code PolynomialMatrix} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param rows the matrix's rows
     * @param width the matrix's width
     */
    private PolynomialMatrix(@NotNull List<PolynomialVector> rows, int width) {
        this.rows = rows;
        this.width = width;
    }

    /**
     * Returns an {@code Iterable} over this {@code PolynomialMatrix}'s rows. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialMatrix}.</li>
     *  <li>The result is finite, contains no nulls, and every element has the same dimension.</li>
     * </ul>
     *
     * Length is height({@code this})
     *
     * @return an {@code Iterable} over this {@code PolynomialMatrix}'s rows
     */
    public @NotNull Iterable<PolynomialVector> rows() {
        return new NoRemoveIterable<>(rows);
    }

    /**
     * Returns an {@code Iterable} over this {@code PolynomialMatrix}'s columns. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialMatrix}.</li>
     *  <li>The result is finite, contains no nulls, and every element has the same dimension.</li>
     * </ul>
     *
     * Length is width({@code this})
     *
     * @return an {@code Iterable} over this {@code PolynomialMatrix}'s columns
     */
    public @NotNull Iterable<PolynomialVector> columns() {
        if (rows.isEmpty()) {
            return replicate(width, PolynomialVector.ZERO_DIMENSIONAL);
        } else {
            //noinspection RedundantCast
            return map(
                    PolynomialVector::of,
                    IterableUtils.transpose((Iterable<Iterable<Polynomial>>) map(p -> (Iterable<Polynomial>) p, rows))
            );
        }
    }

    /**
     * Returns one of {@code this}'s row vectors. 0-indexed.
     *
     * <ul>
     *  <li>{@code this} must be have at least one row.</li>
     *  <li>{@code i} cannot be negative.</li>
     *  <li>{@code i} must be less than the height of {@code this}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Length is height({@code this})
     *
     * @param i the 0-based row index
     * @return the {@code i}th row of {@code this}
     */
    public @NotNull PolynomialVector row(int i) {
        return rows.get(i);
    }

    /**
     * Returns one of {@code this}'s column vectors. 0-indexed.
     *
     * <ul>
     *  <li>{@code this} must have at least one column.</li>
     *  <li>{@code j} cannot be negative.</li>
     *  <li>{@code j} must be less than the width of {@code this}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Length is width({@code this})
     *
     * @param j the 0-based column index
     * @return the {@code j}th column of {@code this}
     */
    public @NotNull PolynomialVector column(int j) {
        if (rows.isEmpty()) {
            if (j < 0) {
                throw new ArrayIndexOutOfBoundsException("j cannot be negative. Invalid j: " + j);
            }
            if (j >= width) {
                throw new ArrayIndexOutOfBoundsException("j must be less than the width of this. j: " +
                        j + ", this: " + this);
            }
            return PolynomialVector.ZERO_DIMENSIONAL;
        } else {
            return PolynomialVector.of(toList(map(i -> i.get(j), rows)));
        }
    }

    /**
     * Converts {@code this} to a {@code RationalPolynomialMatrix}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialMatrix}.</li>
     *  <li>The result is a {@code RationalPolynomialMatrix} with integral elements.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return a {@code RationalPolynomialMatrix} with the same value as {@code this}
     */
    public @NotNull RationalPolynomialMatrix toRationalPolynomialMatrix() {
        if (rows.isEmpty()) {
            return RationalPolynomialMatrix.zero(0, width);
        } else {
            return RationalPolynomialMatrix.fromRows(toList(map(PolynomialVector::toRationalPolynomialVector, rows)));
        }
    }

    /**
     * Returns one of {@code this}'s elements. 0-indexed.
     *
     * <ul>
     *  <li>{@code this} must have at least one row and at least one column.</li>
     *  <li>{@code i} cannot be negative.</li>
     *  <li>{@code j} cannot be negative.</li>
     *  <li>{@code i} must be less than the height of {@code this}.</li>
     *  <li>{@code j} must be less than the width of {@code this}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param i the 0-based row index
     * @param j the 0-based column index
     * @return the element of {@code this} in the {@code i}th row and {@code j}th column
     */
    public @NotNull Polynomial get(int i, int j) {
        return rows.get(i).get(j);
    }

    /**
     * Creates a {@code PolynomialMatrix} from its row vectors. If an empty list is given, the matrix with height 0 and
     * width 0 is returned. This method cannot be used to create matrices with 0 height and nonzero width.
     *
     * <ul>
     *  <li>{@code this} cannot be null, and every element must have the same dimension.</li>
     *  <li>The result either has nonzero height, or 0 width and 0 height.</li>
     * </ul>
     *
     * Size is |{@code rows}|×0 if {@code rows} is empty, |{@code rows}|×|{@code rows.get(0)}| otherwise
     *
     * @param rows the matrix's rows
     * @return a {@code PolynomialMatrix} with the given rows
     */
    public static @NotNull PolynomialMatrix fromRows(@NotNull List<PolynomialVector> rows) {
        if (any(a -> a == null, rows)) {
            throw new NullPointerException();
        } else if (!same(map(PolynomialVector::dimension, rows))) {
            throw new IllegalArgumentException("Every element of rows must have the same dimension. Invalid rows: " +
                    rows);
        } else {
            return new PolynomialMatrix(toList(rows), rows.isEmpty() ? 0 : rows.get(0).dimension());
        }
    }

    /**
     * Creates a {@code PolynomialMatrix} from its column vectors. If an empty list is given, the matrix with height 0
     * and width 0 is returned. This method cannot be used to create matrices with 0 width and nonzero height.
     *
     * <ul>
     *  <li>{@code this} cannot be null, and every element must have the same dimension.</li>
     *  <li>The result either has nonzero width, or 0 width and 0 height.</li>
     * </ul>
     *
     * Size is 0×|{@code columns}| if {@code columns} is empty, |{@code columns.get(0)}|×|{@code columns}| otherwise
     *
     * @param columns the matrix's columns
     * @return a {@code PolynomialMatrix} with the given columns
     */
    public static @NotNull PolynomialMatrix fromColumns(@NotNull List<PolynomialVector> columns) {
        if (any(a -> a == null, columns)) {
            throw new NullPointerException();
        } else if (!same(map(PolynomialVector::dimension, columns))) {
            throw new IllegalArgumentException("Every element of columns must have the same dimension." +
                    " Invalid columns: " + columns);
        } else if (columns.isEmpty()) {
            return new PolynomialMatrix(Collections.emptyList(), 0);
        } else {
            //noinspection RedundantCast
            return new PolynomialMatrix(
                    toList(
                            map(
                                    PolynomialVector::of,
                                    transpose(
                                            (Iterable<Iterable<Polynomial>>)
                                                    map(c -> (Iterable<Polynomial>) c, columns)
                                    )
                            )
                    ),
                    columns.size()
            );
        }
    }

    /**
     * Returns the maximum bit length of any element, or 0 if {@code this} has no elements.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialMatrix}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the maximum coordinate bit length
     */
    public int maxElementBitLength() {
        if (isZero()) return 0;
        return maximum(map(PolynomialVector::maxCoordinateBitLength, rows));
    }

    /**
     * Returns this {@code PolynomialMatrix}'s height.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialMatrix}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the number of rows in {@code this}
     */
    public int height() {
        return rows.size();
    }

    /**
     * Returns this {@code PolynomialMatrix}'s width.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialMatrix}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the number of columns in {@code this}
     */
    public int width() {
        return width;
    }

    /**
     * Determines whether {@code this} is a square matrix.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialMatrix}.</li>
     *  <li>The result may be either boolean.</li>
     * </ul>
     *
     * @return whether {@code this} is square
     */
    public boolean isSquare() {
        return rows.size() == width;
    }

    /**
     * Determines whether this is a zero matrix.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialMatrix}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether the elements of {@code this} are all 0.
     */
    public boolean isZero() {
        return all(PolynomialVector::isZero, rows);
    }

    /**
     * Creates a zero matrix with a given height and width.
     *
     * <ul>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is a {@code PolynomialMatrix} all of whose coordinates are 0.</li>
     * </ul>
     *
     * Size is {@code height}×{@code width}
     *
     * @param height the zero matrix's height
     * @param width the zero matrix's width
     * @return 0<sub>{@code height}, {@code width}</sub>
     */
    public static @NotNull PolynomialMatrix zero(int height, int width) {
        if (height < 0) {
            throw new IllegalArgumentException("height cannot be negative. Invalid height: " + height);
        }
        if (width < 0) {
            throw new IllegalArgumentException("width cannot be negative. Invalid width: " + width);
        }
        return new PolynomialMatrix(toList(replicate(height, PolynomialVector.zero(width))), width);
    }

    /**
     * Determines whether {@code this} is an identity matrix.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialMatrix}.</li>
     *  <li>The result may be either boolean.</li>
     * </ul>
     *
     * @return whether {@code this} is an identity
     */
    public boolean isIdentity() {
        if (!isSquare()) return false;
        for (int i = 0; i < width; i++) {
            PolynomialVector row = row(i);
            for (int j = 0; j < width; j++) {
                if (row.get(j) != (i == j ? Polynomial.ONE : Polynomial.ZERO)) return false;
            }
        }
        return true;
    }

    /**
     * Creates an identity matrix.
     *
     * <ul>
     *  <li>{@code dimension} cannot be negative.</li>
     *  <li>The result is a square {@code PolynomialMatrix}, with height and width at least 1, with ones on the
     *  diagonal and zeros everywhere else.</li>
     * </ul>
     *
     * Size is {@code dimension}×{@code dimension}
     *
     * @param dimension the matrix's dimension (width and height)
     * @return I<sub>{@code dimension}</sub>
     */
    public static @NotNull PolynomialMatrix identity(int dimension) {
        if (dimension < 0) {
            throw new IllegalArgumentException("dimension cannot be negative. Invalid dimension: " + dimension);
        }
        return new PolynomialMatrix(
                toList(map(i -> PolynomialVector.standard(dimension, i), range(0, dimension - 1))),
                dimension
        );
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialMatrix}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code Object} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        PolynomialMatrix matrix = (PolynomialMatrix) that;
        return width == matrix.width && rows.equals(matrix.rows);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialMatrix}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        return 31 * rows.hashCode() + width;
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively. Shortlex ordering is used; {@code PolynomialMatrix}es are compared first by height,
     * then by width, and then top-to-bottom, left-to-right, element by element.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that The {@code PolynomialMatrix} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull PolynomialMatrix that) {
        if (this == that) return 0;
        if (rows.size() > that.rows.size()) return 1;
        if (rows.size() < that.rows.size()) return -1;
        if (width > that.width) return 1;
        if (width < that.width) return -1;
        return POLYNOMIAL_VECTOR_ITERABLE_COMPARATOR.compare(rows, that.rows);
    }

    /**
     * Creates an {@code PolynomialMatrix} from a {@code String}. Valid input takes the form of a {@code String} that
     * could have been returned by {@link mho.qbar.objects.PolynomialMatrix#toString}. See that method's tests and
     * demos for examples of valid input. Note that {@code "[]"} is not a valid input; use {@code "[]#0"} instead.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<PolynomialMatrix>}.</li>
     * </ul>
     *
     * @param s a string representation of a {@code PolynomialMatrix}.
     * @return the wrapped {@code PolynomialMatrix} represented by {@code s}, or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<PolynomialMatrix> read(@NotNull String s) {
        if (s.startsWith("[]#")) {
            Optional<Integer> oWidth = Readers.readInteger(s.substring(3));
            if (!oWidth.isPresent()) return Optional.empty();
            int width = oWidth.get();
            if (width < 0) return Optional.empty();
            return Optional.of(new PolynomialMatrix(Collections.emptyList(), width));
        } else {
            Optional<List<PolynomialVector>> ors = Readers.readList(PolynomialVector::read).apply(s);
            if (!ors.isPresent()) return Optional.empty();
            List<PolynomialVector> rs = ors.get();
            if (rs.isEmpty() || !same(map(PolynomialVector::dimension, rs))) return Optional.empty();
            return Optional.of(new PolynomialMatrix(rs, rs.get(0).dimension()));
        }
    }

    /**
     * Finds the first occurrence of a {@code PolynomialMatrix} in a {@code String}. Returns the
     * {@code PolynomialMatrix} and the index at which it was found. Returns an empty {@code Optional} if no
     * {@code PolynomialMatrix} is found. Only {@code String}s which could have been emitted by
     * {@link mho.qbar.objects.PolynomialMatrix#toString} are recognized. The longest possible {@code PolynomialMatrix}
     * is parsed.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code PolynomialMatrix} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<PolynomialMatrix, Integer>> findIn(@NotNull String s) {
        return Readers.genericFindIn(PolynomialMatrix::read, " #+,-0123456789[]^x").apply(s);
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialMatrix}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        return rows.isEmpty() ? "[]#" + width : rows.toString();
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code PolynomialMatrix} used outside
     * this class.
     */
    public void validate() {
        assertTrue(this, width >= 0);
        assertTrue(this, all(r -> r.dimension() == width, rows));
    }
}
