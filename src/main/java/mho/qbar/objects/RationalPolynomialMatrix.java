package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.iterables.NoRemoveIterable;
import mho.wheels.ordering.Ordering;
import mho.wheels.ordering.comparators.LexComparator;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>A matrix with {@link RationalPolynomial} elements. The number of rows is the height, and the number of rows is
 * the width. Either the height or the width, or both, may be 0; 0-height matrices are written {@code "[]#w"}, where
 * {@code w} is the width. When referring to elements, the (<i>i</i>, <i>j</i>)th element is the element in row
 * <i>i</i> and column <i>j</i>.</p>
 *
 * <p>This class is immutable.</p>
 */
public final class RationalPolynomialMatrix implements Comparable<RationalPolynomialMatrix> {
    /**
     * Used by {@link PolynomialMatrix#compareTo}
     */
    private static final Comparator<
            Iterable<RationalPolynomialVector>
    > RATIONAL_POLYNOMIAL_VECTOR_ITERABLE_COMPARATOR = new LexComparator<>();

    /**
     * The matrix's rows
     */
    private final @NotNull List<RationalPolynomialVector> rows;

    /**
     * The matrix's width. The width can usually be inferred from {@code rows}, except when the height is 0, in which
     * case {@code rows} is empty and this field is the only way to determine the width.
     */
    private final int width;

    /**
     * Private constructor for {@code RationalPolynomialMatrix}; assumes arguments are valid
     *
     * <ul>
     *  <li>{@code rows} cannot have any null elements.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The dimension of every {@code RationalPolynomialVector} in rows must be {@code width}.</li>
     *  <li>Any {@code RationalPolynomialMatrix} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param rows the matrix's rows
     * @param width the matrix's width
     */
    private RationalPolynomialMatrix(@NotNull List<RationalPolynomialVector> rows, int width) {
        this.rows = rows;
        this.width = width;
    }

    /**
     * Returns an {@code Iterable} over this {@code RationalPolynomialMatrix}'s rows. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>The result is finite, contains no nulls, and every element has the same dimension.</li>
     * </ul>
     *
     * Length is height({@code this})
     *
     * @return an {@code Iterable} over this {@code RationalPolynomialMatrix}'s rows
     */
    public @NotNull Iterable<RationalPolynomialVector> rows() {
        return new NoRemoveIterable<>(rows);
    }

    /**
     * Returns an {@code Iterable} over this {@code RationalPolynomialMatrix}'s columns. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>The result is finite, contains no nulls, and every element has the same dimension.</li>
     * </ul>
     *
     * Length is width({@code this})
     *
     * @return an {@code Iterable} over this {@code RationalPolynomialMatrix}'s columns
     */
    public @NotNull Iterable<RationalPolynomialVector> columns() {
        if (rows.isEmpty()) {
            return replicate(width, RationalPolynomialVector.ZERO_DIMENSIONAL);
        } else {
            //noinspection RedundantCast
            return map(
                    RationalPolynomialVector::of,
                    IterableUtils.transpose(
                            (Iterable<Iterable<RationalPolynomial>>) map(r -> (Iterable<RationalPolynomial>) r, rows)
                    )
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
    public @NotNull RationalPolynomialVector row(int i) {
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
    public @NotNull RationalPolynomialVector column(int j) {
        if (rows.isEmpty()) {
            if (j < 0) {
                throw new ArrayIndexOutOfBoundsException("j cannot be negative. Invalid j: " + j);
            }
            if (j >= width) {
                throw new ArrayIndexOutOfBoundsException("j must be less than the width of this. j: " +
                        j + ", this: " + this);
            }
            return RationalPolynomialVector.ZERO_DIMENSIONAL;
        } else {
            return RationalPolynomialVector.of(toList(map(r -> r.get(j), rows)));
        }
    }

    /**
     * Determines whether the elements of {@code this} are all integers.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} only has integral elements.
     */
    public boolean onlyHasIntegralElements() {
        return all(RationalPolynomialVector::onlyHasIntegralCoordinates, rows);
    }

    /**
     * Converts {@code this} to a {@code PolynomialMatrix}.
     *
     * <ul>
     *  <li>{@code this} must only have integral elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return a {@code PolynomialMatrix} with the same value as {@code this}
     */
    public @NotNull PolynomialMatrix toPolynomialMatrix() {
        if (rows.isEmpty()) {
            return PolynomialMatrix.zero(0, width);
        } else {
            return PolynomialMatrix.fromRows(toList(map(RationalPolynomialVector::toPolynomialVector, rows)));
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
    public @NotNull RationalPolynomial get(int i, int j) {
        return rows.get(i).get(j);
    }

    /**
     * Creates a {@code RationalPolynomialMatrix} from its row vectors. If an empty list is given, the matrix with
     * height 0 and width 0 is returned. This method cannot be used to create matrices with 0 height and nonzero width.
     *
     * <ul>
     *  <li>{@code this} cannot be null, and every element must have the same dimension.</li>
     *  <li>The result either has nonzero height, or 0 width and 0 height.</li>
     * </ul>
     *
     * Size is |{@code rows}|×0 if {@code rows} is empty, |{@code rows}|×|{@code rows.get(0)}| otherwise
     *
     * @param rows the matrix's rows
     * @return a {@code RationalPolynomialMatrix} with the given rows
     */
    public static @NotNull RationalPolynomialMatrix fromRows(@NotNull List<RationalPolynomialVector> rows) {
        if (any(a -> a == null, rows)) {
            throw new NullPointerException();
        } else if (!same(map(RationalPolynomialVector::dimension, rows))) {
            throw new IllegalArgumentException("Every element of rows must have the same dimension. Invalid rows: " +
                    rows);
        } else {
            return new RationalPolynomialMatrix(toList(rows), rows.isEmpty() ? 0 : rows.get(0).dimension());
        }
    }

    /**
     * Creates a {@code RationalPolynomialMatrix} from its column vectors. If an empty list is given, the matrix with
     * height 0 and width 0 is returned. This method cannot be used to create matrices with 0 width and nonzero height.
     *
     * <ul>
     *  <li>{@code this} cannot be null, and every element must have the same dimension.</li>
     *  <li>The result either has nonzero width, or 0 width and 0 height.</li>
     * </ul>
     *
     * Size is 0×|{@code columns}| if {@code columns} is empty, |{@code columns.get(0)}|×|{@code columns}| otherwise
     *
     * @param columns the matrix's columns
     * @return a {@code RationalPolynomialMatrix} with the given columns
     */
    public static @NotNull RationalPolynomialMatrix fromColumns(@NotNull List<RationalPolynomialVector> columns) {
        if (any(a -> a == null, columns)) {
            throw new NullPointerException();
        } else if (!same(map(RationalPolynomialVector::dimension, columns))) {
            throw new IllegalArgumentException("Every element of columns must have the same dimension." +
                    " Invalid columns: " + columns);
        } else if (columns.isEmpty()) {
            return new RationalPolynomialMatrix(Collections.emptyList(), 0);
        } else {
            //noinspection RedundantCast
            return new RationalPolynomialMatrix(
                    toList(
                            map(
                                    RationalPolynomialVector::of,
                                    IterableUtils.transpose(
                                            (Iterable<Iterable<RationalPolynomial>>)
                                                    map(c -> (Iterable<RationalPolynomial>) c, columns)
                                    )
                            )
                    ),
                    columns.size()
            );
        }
    }

    /**
     * Creates a {@code RationalPolynomialMatrix} from a {@code RationalMatrix}.
     *
     * <ul>
     *  <li>{@code m} cannot be null.</li>
     *  <li>The result has constant elements.</li>
     * </ul>
     *
     * Length is height({@code m})×width({@code m})
     *
     * @param m a {@code RationalMatrix}
     * @return the {@code RationalPolynomialMatrix} with the same elements as {@code m}
     */
    public static @NotNull RationalPolynomialMatrix of(@NotNull RationalMatrix m) {
        if (m.height() == 0 || m.width() == 0) return zero(m.height(), m.width());
        return fromRows(toList(map(RationalPolynomialVector::of, m.rows())));
    }

    /**
     * Returns the maximum bit length of any element, or 0 if {@code this} has no elements.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the maximum coordinate bit length
     */
    public int maxElementBitLength() {
        if (isZero()) return 0;
        return Ordering.maximum(map(RationalPolynomialVector::maxCoordinateBitLength, rows));
    }

    /**
     * Returns this {@code RationalPolynomialMatrix}'s height.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the number of rows in {@code this}
     */
    public int height() {
        return rows.size();
    }

    /**
     * Returns this {@code RationalPolynomialMatrix}'s width.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
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
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
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
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether the elements of {@code this} are all 0.
     */
    public boolean isZero() {
        return all(RationalPolynomialVector::isZero, rows);
    }

    /**
     * Creates a zero matrix with a given height and width.
     *
     * <ul>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is a {@code RationalPolynomialMatrix} all of whose coordinates are 0.</li>
     * </ul>
     *
     * Size is {@code height}×{@code width}
     *
     * @param height the zero matrix's height
     * @param width the zero matrix's width
     * @return 0<sub>{@code height}, {@code width}</sub>
     */
    public static @NotNull RationalPolynomialMatrix zero(int height, int width) {
        if (height < 0) {
            throw new IllegalArgumentException("height cannot be negative. Invalid height: " + height);
        }
        if (width < 0) {
            throw new IllegalArgumentException("width cannot be negative. Invalid width: " + width);
        }
        return new RationalPolynomialMatrix(toList(replicate(height, RationalPolynomialVector.zero(width))), width);
    }

    /**
     * Determines whether {@code this} is an identity matrix.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>The result may be either boolean.</li>
     * </ul>
     *
     * @return whether {@code this} is an identity
     */
    public boolean isIdentity() {
        if (!isSquare()) return false;
        for (int i = 0; i < width; i++) {
            RationalPolynomialVector row = row(i);
            for (int j = 0; j < width; j++) {
                if (row.get(j) != (i == j ? RationalPolynomial.ONE : RationalPolynomial.ZERO)) return false;
            }
        }
        return true;
    }

    /**
     * Creates an identity matrix. The dimension must be positive.
     *
     * <ul>
     *  <li>{@code dimension} cannot be negative.</li>
     *  <li>The result is a square {@code RationalPolynomialMatrix}, with height and width at least 1, with ones on the
     *  diagonal and zeros everywhere else.</li>
     * </ul>
     *
     * Size is {@code dimension}×{@code dimension}
     *
     * @param dimension the matrix's dimension (width and height)
     * @return I<sub>{@code dimension}</sub>
     */
    public static @NotNull RationalPolynomialMatrix identity(int dimension) {
        if (dimension < 0) {
            throw new IllegalArgumentException("dimension cannot be negative. Invalid dimension: " + dimension);
        }
        if (dimension == 0) {
            return zero(0, 0);
        }
        return new RationalPolynomialMatrix(
                toList(
                        map(i -> RationalPolynomialVector.standard(dimension, i),
                        ExhaustiveProvider.INSTANCE.rangeIncreasing(0, dimension - 1))
                ),
                dimension
        );
    }

    /**
     * Returns the trace of {@code this}.
     *
     * <ul>
     *  <li>{@code this} must be square.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return tr({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull RationalPolynomial trace() {
        if (!isSquare()) {
            throw new IllegalArgumentException("this must be square. Invalid this: " + this);
        }
        RationalPolynomial sum = RationalPolynomial.ZERO;
        for (int i = 0; i < width; i++) {
            sum = sum.add(get(i, i));
        }
        return sum;
    }

    /**
     * Returns a submatrix of {@code this} containing the rows and columns selected by a list of row indices and column
     * indices (both 0-based, in ascending order, with no repetitions).
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code rowIndices} must be in ascending order, cannot have any repetitions, and cannot contain negative
     *  numbers.</li>
     *  <li>{@code columnIndices} must be in ascending order, cannot have any repetitions, and cannot contain negative
     *  numbers.</li>
     *  <li>The elements of {@code rowIndices} must all be less than height({@code this}).</li>
     *  <li>The elements of {@code columnIndices} must all be less than width({@code this}).</li>
     * </ul>
     *
     * Size is |{@code rowIndices}|×|{@code columnIndices}|
     *
     * @param rowIndices the indices of the rows in the result
     * @param columnIndices the indices of the columns in the result
     * @return a submatrix of {@code this}
     */
    public @NotNull RationalPolynomialMatrix submatrix(
            @NotNull List<Integer> rowIndices,
            @NotNull List<Integer> columnIndices
    ) {
        if (!increasing(rowIndices)) {
            throw new IllegalArgumentException("rowIndices must be in ascending order and cannot have any" +
                    " repetitions. Invalid rowIndices: " + rowIndices);
        } else if (!increasing(columnIndices)) {
            throw new IllegalArgumentException("columnIndices must be in ascending order and cannot have any" +
                    " repetitions. Invalid columnIndices: " + columnIndices);
        } else if (!rowIndices.isEmpty() && (head(rowIndices) < 0 || last(rowIndices) >= height())) {
            throw new IllegalArgumentException("rowIndices cannot contain negative numbers or any elements greater" +
                    " than or equal to height(this). rowIndices: " + rowIndices + ", height(this): " + height());
        } else if (!columnIndices.isEmpty() && (head(columnIndices) < 0 || last(columnIndices) >= width)) {
            throw new IllegalArgumentException("columnIndices cannot contain negative numbers or any elements" +
                    " greater than or equal to width(this). columnIndices: " + columnIndices + ", width(this): " +
                    width());
        } else if (rowIndices.isEmpty() || columnIndices.isEmpty()) {
            return zero(rowIndices.size(), columnIndices.size());
        } else if (rowIndices.size() == height() && columnIndices.size() == width) {
            return this;
        } else if (columnIndices.size() == width) {
            return new RationalPolynomialMatrix(toList(map(rows::get, rowIndices)), width);
        } else {
            List<RationalPolynomialVector> submatrixRows = new ArrayList<>();
            for (int i : rowIndices) {
                RationalPolynomialVector row = rows.get(i);
                submatrixRows.add(RationalPolynomialVector.of(toList(map(row::get, columnIndices))));
            }
            return new RationalPolynomialMatrix(submatrixRows, columnIndices.size());
        }
    }

    /**
     * Returns the transpose of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is width({@code this})×height({@code this})
     *
     * @return {@code this}<sup>T</sup>
     */
    public @NotNull RationalPolynomialMatrix transpose() {
        int height = height();
        if (height == 0 || width == 0) {
            //noinspection SuspiciousNameCombination
            return zero(width, height);
        }
        RationalPolynomial[][] elements = new RationalPolynomial[width][height];
        for (int i = 0; i < height; i++) {
            RationalPolynomialVector row = rows.get(i);
            for (int j = 0; j < width; j++) {
                elements[j][i] = row.get(j);
            }
        }
        //noinspection SuspiciousNameCombination
        return new RationalPolynomialMatrix(
                toList(
                        map(i -> RationalPolynomialVector.of(Arrays.asList(elements[i])),
                        ExhaustiveProvider.INSTANCE.rangeIncreasing(0, width - 1))
                ),
                height
        );
    }

    /**
     * Returns the {@code RationalPolynomialMatrix} produced by concatenating the rows of {@code this} with the rows of
     * {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same width.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is (height({@code this}+height({@code that}))×width({@code this})
     *
     * @param that the {@code RationalPolynomialMatrix} that {@code this} is concatenated with
     * @return {@code this} concatenated with {@code that}
     */
    public @NotNull RationalPolynomialMatrix concat(@NotNull RationalPolynomialMatrix that) {
        if (width != that.width) {
            throw new IllegalArgumentException("this and that must have the same width. this: " + this + ", that: " +
                    that);
        }
        if (height() == 0) return that;
        if (that.height() == 0) return this;
        return new RationalPolynomialMatrix(toList(IterableUtils.concat(rows, that.rows)), width);
    }

    /**
     * Returns {@code this} augmented with {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same height.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×(width({@code this}+width({@code that}))
     *
     * @param that the {@code RationalPolynomialMatrix} that {@code this} is augmented by
     * @return {@code this}|{@code that}
     */
    public @NotNull RationalPolynomialMatrix augment(@NotNull RationalPolynomialMatrix that) {
        if (height() != that.height()) {
            throw new IllegalArgumentException("this and that must have the same height. this: " + this + ", that: " +
                    that);
        }
        if (width == 0) return that;
        if (that.width == 0) return this;
        return new RationalPolynomialMatrix(
                toList(
                        zipWith(
                                (r, s) -> RationalPolynomialVector.of(toList(IterableUtils.concat(r, s))),
                                rows,
                                that.rows
                        )
                ),
                width + that.width
        );
    }

    /**
     * Returns the sum of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same height and the same width.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code RationalPolynomialMatrix} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull RationalPolynomialMatrix add(@NotNull RationalPolynomialMatrix that) {
        int height = height();
        if (width != that.width || height != that.height()) {
            throw new ArithmeticException("this and that must have the same width and height. this: " +
                    this + ", that: " + that);
        }
        if (height == 0 || width == 0) return this;
        return new RationalPolynomialMatrix(toList(zipWith(RationalPolynomialVector::add, rows, that.rows)), width);
    }

    /**
     * Returns the negative of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return –{@code this}
     */
    public @NotNull RationalPolynomialMatrix negate() {
        int height = height();
        if (height == 0 || width == 0) return this;
        return new RationalPolynomialMatrix(toList(map(RationalPolynomialVector::negate, rows)), width);
    }

    /**
     * Returns the difference of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same height and the same width.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code RationalPolynomialMatrix} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull RationalPolynomialMatrix subtract(@NotNull RationalPolynomialMatrix that) {
        int height = height();
        if (width != that.width || height != that.height()) {
            throw new ArithmeticException("this and that must have the same width and height. this: " +
                    this + ", that: " + that);
        }
        if (height == 0 || width == 0) return this;
        return new RationalPolynomialMatrix(
                toList(zipWith(RationalPolynomialVector::subtract, rows, that.rows)),
                width
        );
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code RationalPolynomial} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomialMatrix multiply(@NotNull RationalPolynomial that) {
        if (height() == 0 || width == 0 || that == RationalPolynomial.ONE) return this;
        return new RationalPolynomialMatrix(toList(map(r -> r.multiply(that), rows)), width);
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code Rational} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomialMatrix multiply(@NotNull Rational that) {
        if (height() == 0 || width == 0 || that == Rational.ONE) return this;
        return new RationalPolynomialMatrix(toList(map(r -> r.multiply(that), rows)), width);
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code BigInteger} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomialMatrix multiply(@NotNull BigInteger that) {
        if (height() == 0 || width == 0 || that.equals(BigInteger.ONE)) return this;
        return new RationalPolynomialMatrix(toList(map(r -> r.multiply(that), rows)), width);
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code int} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomialMatrix multiply(int that) {
        if (height() == 0 || width == 0 || that == 1) return this;
        return new RationalPolynomialMatrix(toList(map(r -> r.multiply(that), rows)), width);
    }

    /**
     * Returns the scalar product of {@code this} and the inverse of {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code Rational} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalPolynomialMatrix divide(@NotNull Rational that) {
        return multiply(that.invert());
    }

    /**
     * Returns the scalar product of {@code this} and the inverse of {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code BigInteger} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalPolynomialMatrix divide(@NotNull BigInteger that) {
        return multiply(Rational.of(BigInteger.ONE, that));
    }

    /**
     * Returns the scalar product of {@code this} and the inverse of {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code int} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalPolynomialMatrix divide(int that) {
        return multiply(Rational.of(1, that));
    }

    /**
     * Returns the product of {@code this} and {@code that}. The result is {@code that} after a linear transformation
     * whose basis vectors are the columns of {@code this}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The width of {@code this} must equal the dimension of {@code that}.</li>
     * </ul>
     *
     * Length is height({@code this})
     *
     * @param that the {@code RationalPolynomialVector} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomialVector multiply(@NotNull RationalPolynomialVector that) {
        if (width != that.dimension()) {
            throw new ArithmeticException("The width of this must equal the dimension of that. this: " +
                    this + ", that: " + that);
        }
        return RationalPolynomialVector.of(toList(map(r -> r.dot(that), rows)));
    }

    /**
     * Returns the matrix product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialMatrix}</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The width of {@code this} must equal the height of {@code that}.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code that})
     *
     * @param that the {@code RationalPolynomialMatrix} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomialMatrix multiply(@NotNull RationalPolynomialMatrix that) {
        int n = height();
        int m = width;
        if (m != that.height()) {
            throw new ArithmeticException("the width of this must equal the height of that. this: " +
                    this + ", that: " + that);
        }
        int l = that.width;
        if (n == 0) {
            return zero(0, l);
        }
        List<RationalPolynomialVector> rows = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            RationalPolynomialVector row = row(i);
            List<RationalPolynomial> productRow = new ArrayList<>();
            for (int k = 0; k < l; k++) {
                RationalPolynomial sum = RationalPolynomial.ZERO;
                for (int j = 0; j < m; j++) {
                    sum = sum.add(row.get(j).multiply(that.get(j, k)));
                }
                productRow.add(sum);
            }
            rows.add(RationalPolynomialVector.of(productRow));
        }
        return fromRows(rows);
    }

    /**
     * Returns the left shift of {@code this} by {@code bits}; {@code this}×2<sup>{@code bits}</sup>. Negative
     * {@code bits} corresponds to a right shift.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param bits the number of bits to left-shift by
     * @return {@code this}≪{@code bits}
     */
    public @NotNull RationalPolynomialMatrix shiftLeft(int bits) {
        if (isZero() || bits == 0) return this;
        return new RationalPolynomialMatrix(toList(map(r -> r.shiftLeft(bits), rows)), width);
    }

    /**
     * Returns the right shift of {@code this} by {@code bits}; {@code this}×2<sup>–{@code bits}</sup>. Negative
     * {@code bits} corresponds to a left shift.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param bits the number of bits to right-shift by
     * @return {@code this}≫{@code bits}
     */
    public @NotNull RationalPolynomialMatrix shiftRight(int bits) {
        if (isZero() || bits == 0) return this;
        return new RationalPolynomialMatrix(toList(map(r -> r.shiftRight(bits), rows)), width);
    }

    /**
     * Computes the determinant of {@code this} using the Dogdson-Jordan-Gauss algorithm (Basu, Pollack, and Roy 2006).
     *
     * <ul>
     *  <li>{@code this} must be square.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return |{@code this}|
     */
    public @NotNull RationalPolynomial determinant() {
        if (width != height()) {
            throw new IllegalArgumentException("this must be square. Invalid this: " + this);
        }
        int n = width;
        if (n == 0) return RationalPolynomial.ONE;
        if (n == 1) return get(0, 0);
        RationalPolynomial[][] arrayA = new RationalPolynomial[n][n];
        for (int i = 0; i < n; i++) {
            RationalPolynomialVector row = row(i);
            arrayA[i] = new RationalPolynomial[n];
            for (int j = 0; j < n; j++) {
                arrayA[i][j] = row.get(j);
            }
        }
        RationalPolynomial[][] arrayB = new RationalPolynomial[n - 1][n - 1];
        arrayB[0][0] = RationalPolynomial.ONE;
        boolean swapSign = true;
        for (int k = 0; k < n - 1; k++) {
            int firstNonzeroColumnIndex = -1;
            for (int j = 0; j < n - k; j++) {
                if (arrayA[0][j] != RationalPolynomial.ZERO) {
                    firstNonzeroColumnIndex = j;
                    break;
                }
            }
            if (firstNonzeroColumnIndex == -1) {
                return RationalPolynomial.ZERO;
            }
            if (firstNonzeroColumnIndex != 0) {
                for (RationalPolynomial[] row : arrayA) {
                    RationalPolynomial temp = row[firstNonzeroColumnIndex];
                    row[firstNonzeroColumnIndex] = row[0];
                    row[0] = temp;
                }
                swapSign = !swapSign;
            }
            RationalPolynomial denominator = arrayB[0][0];
            for (int i = 1; i < n - k; i++) {
                arrayB[i - 1][0] = RationalPolynomial.ZERO;
                for (int j = 1; j < n - k; j++) {
                    RationalPolynomial numerator = arrayA[0][0].multiply(arrayA[i][j])
                            .subtract(arrayA[i][0].multiply(arrayA[0][j]));
                    arrayB[i - 1][j - 1] = numerator.divide(denominator).a;
                }
            }
            RationalPolynomial[][] temp = arrayA;
            arrayA = arrayB;
            arrayB = temp;
        }
        RationalPolynomial determinant = arrayA[0][0];
        return swapSign ? determinant : determinant.negate();
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
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
        RationalPolynomialMatrix matrix = (RationalPolynomialMatrix) that;
        return width == matrix.width && rows.equals(matrix.rows);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
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
     * "equal to", respectively. Shortlex ordering is used; {@code RationalPolynomialMatrix}es are compared first by
     * height, then by width, and then top-to-bottom, left-to-right, element by element.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that The {@code RationalPolynomialMatrix} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull RationalPolynomialMatrix that) {
        if (this == that) return 0;
        if (rows.size() > that.rows.size()) return 1;
        if (rows.size() < that.rows.size()) return -1;
        if (width > that.width) return 1;
        if (width < that.width) return -1;
        return RATIONAL_POLYNOMIAL_VECTOR_ITERABLE_COMPARATOR.compare(rows, that.rows);
    }

    /**
     * Creates an {@code RationalPolynomialMatrix} from a {@code String}. Valid input takes the form of a
     * {@code String} that could have been returned by {@link mho.qbar.objects.RationalPolynomialMatrix#toString}. See
     * that method's tests and demos for examples of valid input. Note that {@code "[]"} is not a valid input; use
     * {@code "[]#0"} instead.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<RationalPolynomialMatrix>}.</li>
     * </ul>
     *
     * @param s a string representation of a {@code RationalPolynomialMatrix}.
     * @return the wrapped {@code RationalPolynomialMatrix} represented by {@code s}, or {@code empty} if {@code s} is
     * invalid.
     */
    public static @NotNull Optional<RationalPolynomialMatrix> readStrict(@NotNull String s) {
        if (s.startsWith("[]#")) {
            Optional<Integer> oWidth = Readers.readIntegerStrict(s.substring(3));
            if (!oWidth.isPresent()) return Optional.empty();
            int width = oWidth.get();
            if (width < 0) return Optional.empty();
            return Optional.of(new RationalPolynomialMatrix(Collections.emptyList(), width));
        } else {
            Optional<List<RationalPolynomialVector>> ors =
                    Readers.readListStrict(RationalPolynomialVector::readStrict).apply(s);
            if (!ors.isPresent()) return Optional.empty();
            List<RationalPolynomialVector> rs = ors.get();
            if (rs.isEmpty() || !same(map(RationalPolynomialVector::dimension, rs))) return Optional.empty();
            return Optional.of(new RationalPolynomialMatrix(rs, rs.get(0).dimension()));
        }
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialMatrix}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        return rows.isEmpty() ? "[]#" + width : rows.toString();
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code RationalPolynomialMatrix} used
     * outside this class.
     */
    public void validate() {
        assertTrue(this, width >= 0);
        assertTrue(this, all(r -> r.dimension() == width, rows));
    }
}
