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
 * <p>A matrix with {@link Rational} elements. The number of rows is the height, and the number of rows is the width.
 * Either the height or the width, or both, may be 0; 0-height matrices are written {@code "[]#w"}, where {@code w} is
 * the width. When referring to elements, the (<i>i</i>, <i>j</i>)th element is the element in row <i>i</i> and column
 * <i>j</i>.</p>
 *
 * <p>This class is immutable.</p>
 */
public final class RationalMatrix implements Comparable<RationalMatrix> {
    /**
     * Used by {@link mho.qbar.objects.RationalMatrix#compareTo}
     */
    private static final Comparator<Iterable<RationalVector>> RATIONAL_VECTOR_ITERABLE_COMPARATOR =
            new LexComparator<>();

    /**
     * The matrix's rows
     */
    private final @NotNull List<RationalVector> rows;

    /**
     * The matrix's width. The width can usually be inferred from {@code rows}, except when the height is 0, in which
     * case {@code rows} is empty and this field is the only way to determine the width.
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
    public @NotNull Iterable<RationalVector> rows() {
        return new NoRemoveIterable<>(rows);
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
    public @NotNull Iterable<RationalVector> columns() {
        if (rows.isEmpty()) {
            return replicate(width, RationalVector.ZERO_DIMENSIONAL);
        } else {
            //noinspection RedundantCast
            return map(
                    RationalVector::of,
                    IterableUtils.transpose((Iterable<Iterable<Rational>>) map(r -> (Iterable<Rational>) r, rows))
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
    public @NotNull RationalVector row(int i) {
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
    public @NotNull RationalVector column(int j) {
        if (rows.isEmpty()) {
            if (j < 0) {
                throw new ArrayIndexOutOfBoundsException("j cannot be negative. Invalid j: " + j);
            }
            if (j >= width) {
                throw new ArrayIndexOutOfBoundsException("j must be less than the width of this. j: " +
                        j + ", this: " + this);
            }
            return RationalVector.ZERO_DIMENSIONAL;
        } else {
            return RationalVector.of(toList(map(r -> r.get(j), rows)));
        }
    }

    /**
     * Determines whether the elements of {@code this} are all integers.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} only has integral elements.
     */
    public boolean onlyHasIntegralElements() {
        return all(RationalVector::onlyHasIntegralCoordinates, rows);
    }

    /**
     * Converts {@code this} to a {@code Matrix}.
     *
     * <ul>
     *  <li>{@code this} must only have integral elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return a {@code Matrix} with the same value as {@code this}
     */
    public @NotNull Matrix toMatrix() {
        if (rows.isEmpty()) {
            return Matrix.zero(0, width);
        } else {
            return Matrix.fromRows(toList(map(RationalVector::toVector, rows)));
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
    public @NotNull Rational get(int i, int j) {
        return rows.get(i).get(j);
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
     * Size is |{@code rows}|×0 if {@code rows} is empty, |{@code rows}|×|{@code rows.get(0)}| otherwise
     *
     * @param rows the matrix's rows
     * @return a {@code RationalMatrix} with the given rows
     */
    public static @NotNull RationalMatrix fromRows(@NotNull List<RationalVector> rows) {
        if (any(Objects::isNull, rows)) {
            throw new NullPointerException();
        } else if (!same(map(RationalVector::dimension, rows))) {
            throw new IllegalArgumentException("Every element of rows must have the same dimension. Invalid rows: " +
                    rows);
        } else {
            return new RationalMatrix(toList(rows), rows.isEmpty() ? 0 : rows.get(0).dimension());
        }
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
     * Size is 0×|{@code columns}| if {@code columns} is empty, |{@code columns.get(0)}|×|{@code columns}| otherwise
     *
     * @param columns the matrix's columns
     * @return a {@code RationalMatrix} with the given columns
     */
    public static @NotNull RationalMatrix fromColumns(@NotNull List<RationalVector> columns) {
        if (any(Objects::isNull, columns)) {
            throw new NullPointerException();
        } else if (!same(map(RationalVector::dimension, columns))) {
            throw new IllegalArgumentException("Every element of columns must have the same dimension." +
                    " Invalid columns: " + columns);
        } else if (columns.isEmpty()) {
            return new RationalMatrix(Collections.emptyList(), 0);
        } else {
            //noinspection RedundantCast
            return new RationalMatrix(
                    toList(
                            map(
                                    RationalVector::of,
                                    IterableUtils.transpose(
                                            (Iterable<Iterable<Rational>>) map(c -> (Iterable<Rational>) c, columns)
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
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the maximum coordinate bit length
     */
    public int maxElementBitLength() {
        if (isZero()) return 0;
        return Ordering.maximum(map(RationalVector::maxCoordinateBitLength, rows));
    }

    /**
     * Returns this {@code RationalMatrix}'s height.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the number of rows in {@code this}
     */
    public int height() {
        return rows.size();
    }

    /**
     * Returns this {@code RationalMatrix}'s width.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
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
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
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
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether the elements of {@code this} are all 0.
     */
    public boolean isZero() {
        return all(RationalVector::isZero, rows);
    }

    /**
     * Creates a zero matrix with a given height and width.
     *
     * <ul>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is a {@code RationalMatrix} all of whose coordinates are 0.</li>
     * </ul>
     *
     * Size is {@code height}×{@code width}
     *
     * @param height the zero matrix's height
     * @param width the zero matrix's width
     * @return 0<sub>{@code height}, {@code width}</sub>
     */
    public static @NotNull RationalMatrix zero(int height, int width) {
        if (height < 0) {
            throw new IllegalArgumentException("height cannot be negative. Invalid height: " + height);
        }
        if (width < 0) {
            throw new IllegalArgumentException("width cannot be negative. Invalid width: " + width);
        }
        return new RationalMatrix(toList(replicate(height, RationalVector.zero(width))), width);
    }

    /**
     * Determines whether {@code this} is an identity matrix.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result may be either boolean.</li>
     * </ul>
     *
     * @return whether {@code this} is an identity
     */
    public boolean isIdentity() {
        if (!isSquare()) return false;
        for (int i = 0; i < width; i++) {
            RationalVector row = row(i);
            for (int j = 0; j < width; j++) {
                if (row.get(j) != (i == j ? Rational.ONE : Rational.ZERO)) return false;
            }
        }
        return true;
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
    public @NotNull Rational trace() {
        if (!isSquare()) {
            throw new IllegalArgumentException("this must be square. Invalid this: " + this);
        }
        Rational sum = Rational.ZERO;
        for (int i = 0; i < width; i++) {
            sum = sum.add(get(i, i));
        }
        return sum;
    }

    /**
     * Creates an identity matrix. The dimension must be positive.
     *
     * <ul>
     *  <li>{@code dimension} cannot be negative.</li>
     *  <li>The result is a square {@code RationalMatrix}, with height and width at least 1, with ones on the diagonal
     *  and zeros everywhere else.</li>
     * </ul>
     *
     * Size is {@code dimension}×{@code dimension}
     *
     * @param dimension the matrix's dimension (width and height)
     * @return I<sub>{@code dimension}</sub>
     */
    public static @NotNull RationalMatrix identity(int dimension) {
        if (dimension < 0) {
            throw new IllegalArgumentException("dimension cannot be negative. Invalid dimension: " + dimension);
        }
        if (dimension == 0) {
            return zero(0, 0);
        }
        return new RationalMatrix(
                toList(
                        map(
                                i -> RationalVector.standard(dimension, i),
                                ExhaustiveProvider.INSTANCE.rangeIncreasing(0, dimension - 1)
                        )
                ),
                dimension
        );
    }

    /**
     * Returns a submatrix of {@code this} containing the rows and columns selected by a list of row indices and column
     * indices (both 0-based, in ascending order, with no repetitions).
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
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
    public @NotNull RationalMatrix submatrix(@NotNull List<Integer> rowIndices, @NotNull List<Integer> columnIndices) {
        if (!Ordering.increasing(rowIndices)) {
            throw new IllegalArgumentException("rowIndices must be in ascending order and cannot have any" +
                    " repetitions. Invalid rowIndices: " + rowIndices);
        } else if (!Ordering.increasing(columnIndices)) {
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
            return new RationalMatrix(toList(map(rows::get, rowIndices)), width);
        } else {
            List<RationalVector> submatrixRows = new ArrayList<>();
            for (int i : rowIndices) {
                RationalVector row = rows.get(i);
                submatrixRows.add(RationalVector.of(toList(map(row::get, columnIndices))));
            }
            return new RationalMatrix(submatrixRows, columnIndices.size());
        }
    }

    /**
     * Returns the transpose of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is width({@code this})×height({@code this})
     *
     * @return {@code this}<sup>T</sup>
     */
    public @NotNull RationalMatrix transpose() {
        int height = height();
        if (height == 0 || width == 0) {
            //noinspection SuspiciousNameCombination
            return zero(width, height);
        }
        Rational[][] elements = new Rational[width][height];
        for (int i = 0; i < height; i++) {
            RationalVector row = rows.get(i);
            for (int j = 0; j < width; j++) {
                elements[j][i] = row.get(j);
            }
        }
        //noinspection SuspiciousNameCombination
        return new RationalMatrix(
                toList(
                        map(i -> RationalVector.of(Arrays.asList(elements[i])),
                        ExhaustiveProvider.INSTANCE.rangeIncreasing(0, width - 1))
                ),
                height
        );
    }

    /**
     * Returns the {@code RationalMatrix} produced by concatenating the rows of {@code this} with the rows of
     * {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same width.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is (height({@code this}+height({@code that}))×width({@code this})
     *
     * @param that the {@code RationalMatrix} that {@code this} is concatenated with
     * @return {@code this} concatenated with {@code that}
     */
    public @NotNull RationalMatrix concat(@NotNull RationalMatrix that) {
        if (width != that.width) {
            throw new IllegalArgumentException("this and that must have the same width. this: " + this + ", that: " +
                    that);
        }
        if (height() == 0) return that;
        if (that.height() == 0) return this;
        return new RationalMatrix(toList(IterableUtils.concat(rows, that.rows)), width);
    }

    /**
     * Returns {@code this} augmented with {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same height.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×(width({@code this}+width({@code that}))
     *
     * @param that the {@code RationalMatrix} that {@code this} is augmented by
     * @return {@code this}|{@code that}
     */
    public @NotNull RationalMatrix augment(@NotNull RationalMatrix that) {
        if (height() != that.height()) {
            throw new IllegalArgumentException("this and that must have the same height. this: " + this + ", that: " +
                    that);
        }
        if (width == 0) return that;
        if (that.width == 0) return this;
        return new RationalMatrix(
                toList(zipWith((r, s) -> RationalVector.of(toList(IterableUtils.concat(r, s))), rows, that.rows)),
                width + that.width
        );
    }

    /**
     * Returns the sum of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same height and the same width.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code RationalMatrix} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull RationalMatrix add(@NotNull RationalMatrix that) {
        int height = height();
        if (width != that.width || height != that.height()) {
            throw new ArithmeticException("this and that must have the same width and height. this: " +
                    this + ", that: " + that);
        }
        if (height == 0 || width == 0) return this;
        return new RationalMatrix(toList(zipWith(RationalVector::add, rows, that.rows)), width);
    }

    /**
     * Returns the negative of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return –{@code this}
     */
    public @NotNull RationalMatrix negate() {
        int height = height();
        if (height == 0 || width == 0) return this;
        return new RationalMatrix(toList(map(RationalVector::negate, rows)), width);
    }

    /**
     * Returns the difference of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same height and the same width.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code RationalMatrix} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull RationalMatrix subtract(@NotNull RationalMatrix that) {
        int height = height();
        if (width != that.width || height != that.height()) {
            throw new ArithmeticException("this and that must have the same width and height. this: " +
                    this + ", that: " + that);
        }
        if (height == 0 || width == 0) return this;
        return new RationalMatrix(toList(zipWith(RationalVector::subtract, rows, that.rows)), width);
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code Rational} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalMatrix multiply(@NotNull Rational that) {
        if (height() == 0 || width == 0 || that == Rational.ONE) return this;
        return new RationalMatrix(toList(map(r -> r.multiply(that), rows)), width);
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code BigInteger} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalMatrix multiply(@NotNull BigInteger that) {
        if (height() == 0 || width == 0 || that.equals(BigInteger.ONE)) return this;
        return new RationalMatrix(toList(map(r -> r.multiply(that), rows)), width);
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code int} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalMatrix multiply(int that) {
        if (height() == 0 || width == 0 || that == 1) return this;
        return new RationalMatrix(toList(map(r -> r.multiply(that), rows)), width);
    }

    /**
     * Returns the scalar product of {@code this} and the inverse of {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalMatrix}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code Rational} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalMatrix divide(@NotNull Rational that) {
        return multiply(that.invert());
    }

    /**
     * Returns the scalar product of {@code this} and the inverse of {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalMatrix}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code BigInteger} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalMatrix divide(@NotNull BigInteger that) {
        return multiply(Rational.of(BigInteger.ONE, that));
    }

    /**
     * Returns the scalar product of {@code this} and the inverse of {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalMatrix}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code int} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalMatrix divide(int that) {
        return multiply(Rational.of(1, that));
    }

    /**
     * Returns the product of {@code this} and {@code that}. The result is {@code that} after a linear transformation
     * whose basis vectors are the columns of {@code this}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The width of {@code this} must equal the dimension of {@code that}.</li>
     * </ul>
     *
     * Length is height({@code this})
     *
     * @param that the {@code RationalVector} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalVector multiply(@NotNull RationalVector that) {
        if (width != that.dimension()) {
            throw new ArithmeticException("The width of this must equal the dimension of that. this: " +
                    this + ", that: " + that);
        }
        return RationalVector.of(toList(map(r -> r.dot(that), rows)));
    }

    /**
     * Returns the matrix product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalMatrix}</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The width of {@code this} must equal the height of {@code that}.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code that})
     *
     * @param that the {@code RationalMatrix} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalMatrix multiply(@NotNull RationalMatrix that) {
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
        List<RationalVector> rows = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            RationalVector row = row(i);
            List<Rational> productRow = new ArrayList<>();
            for (int k = 0; k < l; k++) {
                Rational sum = Rational.ZERO;
                for (int j = 0; j < m; j++) {
                    sum = sum.add(row.get(j).multiply(that.get(j, k)));
                }
                productRow.add(sum);
            }
            rows.add(RationalVector.of(productRow));
        }
        return fromRows(rows);
    }

    /**
     * Returns the left shift of {@code this} by {@code bits}; {@code this}×2<sup>{@code bits}</sup>. Negative
     * {@code bits} corresponds to a right shift.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalMatrix}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param bits the number of bits to left-shift by
     * @return {@code this}≪{@code bits}
     */
    public @NotNull RationalMatrix shiftLeft(int bits) {
        if (isZero() || bits == 0) return this;
        return new RationalMatrix(toList(map(r -> r.shiftLeft(bits), rows)), width);
    }

    /**
     * Returns the right shift of {@code this} by {@code bits}; {@code this}×2<sup>–{@code bits}</sup>. Negative
     * {@code bits} corresponds to a left shift.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalMatrix}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param bits the number of bits to right-shift by
     * @return {@code this}≫{@code bits}
     */
    public @NotNull RationalMatrix shiftRight(int bits) {
        if (isZero() || bits == 0) return this;
        return new RationalMatrix(toList(map(r -> r.shiftRight(bits), rows)), width);
    }

    /**
     * Determines whether {@code this} is in row echelon form; whether all zero rows are at the bottom, the first
     * nonzero element of every row is 1, and the first nonzero element of every row is strictly to the right of the
     * first nonzero element of the row above it.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is in row echelon form
     */
    public boolean isInRowEchelonForm() {
        boolean seenNonzero = false;
        for (int i = height() - 1; i >= 0; i--) {
            boolean zero = row(i).isZero();
            if (zero) {
                if (seenNonzero) return false;
            } else {
                seenNonzero = true;
            }
        }
        int lastPivotIndex = -1;
        for (RationalVector row : rows()) {
            Optional<Integer> oi = findIndex(x -> x != Rational.ZERO, row);
            if (!oi.isPresent()) break;
            int pivotIndex = oi.get();
            if (row.get(pivotIndex) != Rational.ONE || pivotIndex <= lastPivotIndex) return false;
            lastPivotIndex = pivotIndex;
        }
        return true;
    }

    /**
     * Returns a row echelon form of {@code this}. In other words, all zero rows are at the bottom, the first nonzero
     * element of every row is 1, and the first nonzero element of every row is strictly to the right of the first
     * nonzero element of the row above it.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result is in row echelon form.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return a row echelon form of {@code this}
     */
    public @NotNull RationalMatrix rowEchelonForm() {
        int height = height();
        if (width == 0 || height == 0) return this;
        boolean changed = false;
        List<RationalVector> refRows = rows;
        int i = 0;
        outer:
        for (int j = 0; i < height && j < width; j++) {
            int nonzeroRowIndex = i;
            Rational pivot = refRows.get(i).get(j);
            while (pivot == Rational.ZERO) {
                nonzeroRowIndex++;
                if (nonzeroRowIndex == height) continue outer;
                pivot = refRows.get(nonzeroRowIndex).get(j);
            }
            if (nonzeroRowIndex != i) {
                if (!changed) {
                    changed = true;
                    refRows = toList(rows);
                }
                Collections.swap(refRows, i, nonzeroRowIndex);
            }
            RationalVector nonzeroRow = refRows.get(i);
            if (pivot != Rational.ONE) {
                if (!changed) {
                    changed = true;
                    refRows = toList(rows);
                }
                nonzeroRow = nonzeroRow.divide(pivot);
                refRows.set(i, nonzeroRow);
            }
            for (int k = i + 1; k < height; k++) {
                RationalVector row = refRows.get(k);
                if (row.get(j) != Rational.ZERO) {
                    if (!changed) {
                        changed = true;
                        refRows = toList(rows);
                    }
                    refRows.set(k, row.subtract(nonzeroRow.multiply(row.get(j))));
                }
            }
            i++;
        }
        return changed ? new RationalMatrix(refRows, width) : this;
    }

    /**
     * Returns the rank of {@code this}, or the dimension of the row space (which equals the dimension of the column
     * space).
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result is not negative.</li>
     * </ul>
     *
     * @return rank({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public int rank() {
        RationalMatrix ref = rowEchelonForm();
        for (int rank = height(); rank > 0; rank--) {
            if (!ref.row(rank - 1).isZero()) {
                return rank;
            }
        }
        return 0;
    }

    /**
     * Determines whether {@code this} is invertible.
     *
     * <ul>
     *  <li>{@code this} must be square.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is invertible
     */
    public boolean isInvertible() {
        if (!isSquare()) {
            throw new IllegalArgumentException("this must be square. Invalid this: " + this);
        }
        return rank() == width;
    }

    /**
     * Determines whether {@code this} is in reduced row echelon form; whether it is in row echelon form (see
     * {@link RationalMatrix#isInRowEchelonForm()}) and every leading element is the only nonzero element in its
     * column.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is in reduced row echelon form
     */
    public boolean isInReducedRowEchelonForm() {
        if (!isInRowEchelonForm()) return false;
        for (int i = 0; i < height(); i++) {
            RationalVector row = row(i);
            Optional<Integer> oi = findIndex(x -> x != Rational.ZERO, row);
            if (!oi.isPresent()) break;
            int pivotIndex = oi.get();
            for (int j = 0; j < i; j++) {
                if (get(j, pivotIndex) != Rational.ZERO) return false;
            }
        }
        return true;
    }

    /**
     * Returns the reduced row echelon form of {@code this}. In other words, the result is in row echelon form (see
     * {@link RationalMatrix#isInRowEchelonForm()}) and every leading element is the only nonzero element in its
     * column.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>The result is in reduced row echelon form.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return the reduced row echelon form of {@code this}
     */
    public @NotNull RationalMatrix reducedRowEchelonForm() {
        int height = height();
        if (width == 0 || height == 0) return this;
        RationalMatrix ref = rowEchelonForm();
        if (width <= height && !ref.row(width - 1).isZero()) {
            RationalMatrix identity = identity(width);
            return width == height ? identity : identity.concat(zero(height - width, width));
        }
        boolean changed = false;
        List<RationalVector> rrefRows = ref.rows;
        for (int i = 0; i < height(); i++) {
            RationalVector row = rrefRows.get(i);
            Optional<Integer> pivotIndex = findIndex(x -> x != Rational.ZERO, row);
            if (!pivotIndex.isPresent()) break;
            int j = pivotIndex.get();
            for (int k = i - 1; k >= 0; k--) {
                RationalVector above = rrefRows.get(k);
                if (above.get(j) != Rational.ZERO) {
                    if (!changed) {
                        changed = true;
                        rrefRows = toList(ref.rows);
                    }
                    rrefRows.set(k, above.subtract(row.multiply(above.get(j))));
                }
            }
        }
        return changed ? new RationalMatrix(rrefRows, width) : ref;
    }

    /**
     * Solves a linear system of equations. If this is a matrix with <i>n</i> rows and <i>m</i> columns, then the
     * system contains <i>n</i> equations in <i>m</i> variables, and the entry (<i>i</i>, <i>j</i>) is the coefficient
     * of the <i>j</i>th variable in the <i>i</i>th equation. The vector {@code rhs} represents the right-hand side of
     * the equations, and its <i>i</i>th entry is the right-hand side of the <i>i</i>th equation. If the system is
     * inconsistent or underdetermined, the result is empty; otherwise, it is non-empty. Note that if the system is
     * overdetermined, it may still be consistent. If the result is non-empty, it represents the values of the <i>m</i>
     * variables, and its <i>j</i>th element is the value of the <i>j</i>th variable.
     *
     * This method differs from {@link RationalMatrix#solveLinearSystemPermissive(RationalVector)} in that it returns
     * empty on underdetermined systems (systems with infinitely many solutions).
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>{@code rhs} may be any {@code RationalVector}.</li>
     *  <li>The dimension of {@code rhs} must equal the height of {@code this}.</li>
     *  <li>The result may be empty or any {@code RationalVector}.</li>
     * </ul>
     *
     * Length is width({@code this})
     *
     * @param rhs the right-hand side of the system of equations
     * @return The assignments to the variables in the equations
     */
    public @NotNull Optional<RationalVector> solveLinearSystem(@NotNull RationalVector rhs) {
        if (height() != rhs.dimension()) {
            throw new IllegalArgumentException("The dimension of rhs must equal the height of this. rhs: " +
                    rhs + ", this: " + this);
        }
        if (width > height()) return Optional.empty();
        RationalMatrix rref = augment(fromColumns(Collections.singletonList(rhs))).reducedRowEchelonForm();
        RationalMatrix bottom;
        if (width == height()) {
            bottom = zero(0, width + 1);
        } else {
            bottom = rref.submatrix(
                    toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(width, height() - 1)),
                    toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, width))
            );
        }
        if (!bottom.isZero()) return Optional.empty();
        if (width == 0) {
            return Optional.of(RationalVector.ZERO_DIMENSIONAL);
        }
        RationalMatrix left = rref.submatrix(
                toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, width - 1)),
                toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, width - 1))
        );
        if (!left.isIdentity()) return Optional.empty();
        return Optional.of(
                rref.submatrix(
                        toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, width - 1)),
                        Collections.singletonList(width)
                ).column(0)
        );
    }

    /**
     * Solves a linear system of equations. If this is a matrix with <i>n</i> rows and <i>m</i> columns, then the
     * system contains <i>n</i> equations in <i>m</i> variables, and the entry (<i>i</i>, <i>j</i>) is the coefficient
     * of the <i>j</i>th variable in the <i>i</i>th equation. The vector {@code rhs} represents the right-hand side of
     * the equations, and its <i>i</i>th entry is the right-hand side of the <i>i</i>th equation. If the system is
     * inconsistent, the result is empty; otherwise, it is non-empty. If the system is underdetermined, some solution
     * is returned, usually with some variables set to zero. If the result is non-empty, it represents the values of
     * the <i>m</i> variables, and its <i>j</i>th element is the value of the <i>j</i>th variable.
     *
     * This method differs from {@link RationalMatrix#solveLinearSystem(RationalVector)} in that when the system is
     * underdetermined (has infinitely many solutions) one solution is returned.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>{@code rhs} may be any {@code RationalVector}.</li>
     *  <li>The dimension of {@code rhs} must equal the height of {@code this}.</li>
     *  <li>The result may be empty or any {@code RationalVector}.</li>
     * </ul>
     *
     * Length is width({@code this})
     *
     * @param rhs the right-hand side of the system of equations
     * @return The assignments to the variables in the equations
     */
    public @NotNull Optional<RationalVector> solveLinearSystemPermissive(@NotNull RationalVector rhs) {
        if (height() != rhs.dimension()) {
            throw new IllegalArgumentException("The dimension of rhs must equal the height of this. rhs: " +
                    rhs + ", this: " + this);
        }
        RationalMatrix rref = augment(fromColumns(Collections.singletonList(rhs))).reducedRowEchelonForm();
        RationalMatrix bottom;
        if (width >= height()) {
            bottom = zero(0, width + 1);
        } else {
            bottom = rref.submatrix(
                    toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(width, height() - 1)),
                    toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, width))
            );
        }
        if (!bottom.isZero()) return Optional.empty();
        List<Rational> result = toList(replicate(width, Rational.ZERO));
        for (RationalVector row : rref.rows) {
            Rational last = row.get(row.dimension() - 1);
            Optional<Integer> firstIndex = findIndex(r -> r != Rational.ZERO, init(row));
            if (firstIndex.isPresent()) {
                result.set(firstIndex.get(), last);
            } else if (last != Rational.ZERO) {
                return Optional.empty();
            }
        }
        return Optional.of(RationalVector.of(result));
    }

    /**
     * Returns the inverse of {@code this}, or an empty {@code Optional} if {@code this} is not invertible.
     *
     * <ul>
     *  <li>{@code this} must be square.</li>
     *  <li>The result is either empty or an invertible {@code RationalMatrix}.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return {@code this}<sup>–1</sup>
     */
    public @NotNull Optional<RationalMatrix> invert() {
        if (!isSquare()) {
            throw new IllegalArgumentException("this must be square. Invalid this: " + this);
        }
        RationalMatrix rref = augment(identity(width)).reducedRowEchelonForm();
        List<Integer> range = width == 0 ?
                Collections.emptyList() :
                toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, width - 1));
        RationalMatrix left = rref.submatrix(range, range);
        if (!left.isIdentity()) return Optional.empty();
        return Optional.of(
                rref.submatrix(
                        range,
                        width == 0 ?
                                Collections.emptyList() :
                                toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(width, 2 * width - 1))
                )
        );
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
    public @NotNull Rational determinant() {
        if (width != height()) {
            throw new IllegalArgumentException("this must be square. Invalid this: " + this);
        }
        int n = width;
        if (n == 0) return Rational.ONE;
        if (n == 1) return get(0, 0);
        Rational[][] arrayA = new Rational[n][n];
        for (int i = 0; i < n; i++) {
            RationalVector row = row(i);
            arrayA[i] = new Rational[n];
            for (int j = 0; j < n; j++) {
                arrayA[i][j] = row.get(j);
            }
        }
        Rational[][] arrayB = new Rational[n - 1][n - 1];
        arrayB[0][0] = Rational.ONE;
        boolean swapSign = true;
        for (int k = 0; k < n - 1; k++) {
            int firstNonzeroColumnIndex = -1;
            for (int j = 0; j < n - k; j++) {
                if (arrayA[0][j] != Rational.ZERO) {
                    firstNonzeroColumnIndex = j;
                    break;
                }
            }
            if (firstNonzeroColumnIndex == -1) {
                return Rational.ZERO;
            }
            if (firstNonzeroColumnIndex != 0) {
                for (Rational[] row : arrayA) {
                    Rational temp = row[firstNonzeroColumnIndex];
                    row[firstNonzeroColumnIndex] = row[0];
                    row[0] = temp;
                }
                swapSign = !swapSign;
            }
            Rational denominator = arrayB[0][0];
            for (int i = 1; i < n - k; i++) {
                arrayB[i - 1][0] = Rational.ZERO;
                for (int j = 1; j < n - k; j++) {
                    Rational numerator = arrayA[0][0].multiply(arrayA[i][j])
                            .subtract(arrayA[i][0].multiply(arrayA[0][j]));
                    arrayB[i - 1][j - 1] = numerator.divide(denominator);
                }
            }
            Rational[][] temp = arrayA;
            arrayA = arrayB;
            arrayB = temp;
        }
        Rational determinant = arrayA[0][0];
        return swapSign ? determinant : determinant.negate();
    }

    /**
     * Computes the characteristic polynomial of {@code this} using the Faddeev-Leverrier algorithm.
     *
     * <ul>{@code this} must be square.</ul>
     * <ul>The result is monic.</ul>
     *
     * Length is height({@code this})+1
     *
     * @return det(Ix–{@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull RationalPolynomial characteristicPolynomial() {
        if (width != height()) {
            throw new IllegalArgumentException("this must be square. Invalid this: " + this);
        }
        int n = width;
        if (n == 0) return RationalPolynomial.ONE;
        int r = 1;
        while (r * r <= n) r++;
        List<RationalMatrix> powers = new ArrayList<>();
        RationalMatrix previousPower = identity(n);
        powers.add(previousPower);
        List<Rational> powerSums = toList(replicate(r * r, Rational.ZERO));
        powerSums.set(0, Rational.of(n));
        for (int i = 1; i < r; i++) {
            RationalMatrix nextPower = multiply(previousPower);
            powers.add(nextPower);
            powerSums.set(i, nextPower.trace());
            previousPower = nextPower;
        }
        List<RationalMatrix> cs = new ArrayList<>();
        RationalMatrix firstC = multiply(powers.get(r - 1));
        cs.add(firstC);
        RationalMatrix previousC = firstC;
        powerSums.set(r, firstC.trace());
        for (int j = 2; j < r; j++) {
            RationalMatrix nextC = firstC.multiply(previousC);
            powerSums.set(j * r, nextC.trace());
            cs.add(nextC);
            previousC = nextC;
        }
        for (int i = 1; i < r; i++) {
            for (int j = 1; j < r; j++) {
                int index = j * r + i;
                if (index <= n) {
                    powerSums.set(index, powers.get(i).multiply(cs.get(j - 1)).trace());
                }
            }
        }
        return RationalPolynomial.fromPowerSums(toList(take(n + 1, powerSums)));
    }

    /**
     * Returns the Kronecker product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is the Kronecker product of two matrices.</li>
     * </ul>
     *
     * Size is (height({@code this})height({@code that}))×(width({@code this})width({@code that}))
     *
     * @param that the {@code RationalMatrix} that {@code this} is multiplied by.
     * @return {@code this}⊗{@code that}
     */
    public @NotNull RationalMatrix kroneckerMultiply(@NotNull RationalMatrix that) {
        List<RationalVector> productRows = new ArrayList<>();
        for (RationalVector thisRow : rows) {
            //noinspection Convert2streamapi
            for (RationalVector thatRow : that.rows) {
                //noinspection Convert2MethodRef
                productRows.add(RationalVector.of(toList(concatMap(x -> map(y -> x.multiply(y), thatRow), thisRow))));
            }
        }
        return new RationalMatrix(productRows, width * that.width);
    }

    /**
     * Returns the Kronecker sum of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} must be square.</li>
     *  <li>{@code that} must be square.</li>
     *  <li>The result is the Kronecker sum of two matrices.</li>
     * </ul>
     *
     * Size is (height({@code this})height({@code that}))×(width({@code this})width({@code that}))
     *
     * @param that the {@code RationalMatrix} added to {@code this}
     * @return {@code this}⊕{@code that}
     */
    public @NotNull RationalMatrix kroneckerAdd(@NotNull RationalMatrix that) {
        if (!isSquare()) {
            throw new IllegalArgumentException("this must be square. Invalid this: " + this);
        }
        if (!that.isSquare()) {
            throw new IllegalArgumentException("that must be square. Invalid that: " + that);
        }
        return kroneckerMultiply(identity(that.width)).add(identity(width).kroneckerMultiply(that));
    }

    /**
     * The real eigenvalues of {@code this} in increasing order.
     *
     * <ul>
     *  <li>{@code this} must be square.</li>
     *  <li>The result is increasing and has no duplicates.</li>
     * </ul>
     *
     * @return the eigenvalues of {@code this}
     */
    public @NotNull List<Algebraic> realEigenvalues() {
        return characteristicPolynomial().realRoots();
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
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
        RationalMatrix matrix = (RationalMatrix) that;
        return width == matrix.width && rows.equals(matrix.rows);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
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
     * "equal to", respectively. Shortlex ordering is used; {@code RationalMatrix}es are compared first by height, then
     * by width, and then top-to-bottom, left-to-right, element by element.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMatrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that The {@code RationalMatrix} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull RationalMatrix that) {
        if (this == that) return 0;
        if (rows.size() > that.rows.size()) return 1;
        if (rows.size() < that.rows.size()) return -1;
        if (width > that.width) return 1;
        if (width < that.width) return -1;
        return RATIONAL_VECTOR_ITERABLE_COMPARATOR.compare(rows, that.rows);
    }

    /**
     * Creates an {@code RationalMatrix} from a {@code String}. Valid input takes the form of a {@code String} that
     * could have been returned by {@link mho.qbar.objects.RationalMatrix#toString}. See that method's tests and demos
     * for examples of valid input. Note that {@code "[]"} is not a valid input; use {@code "[]#0"} instead.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<RationalMatrix>}.</li>
     * </ul>
     *
     * @param s a string representation of a {@code RationalMatrix}.
     * @return the wrapped {@code RationalMatrix} represented by {@code s}, or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<RationalMatrix> readStrict(@NotNull String s) {
        if (s.startsWith("[]#")) {
            Optional<Integer> oWidth = Readers.readIntegerStrict(s.substring(3));
            if (!oWidth.isPresent()) return Optional.empty();
            int width = oWidth.get();
            if (width < 0) return Optional.empty();
            return Optional.of(new RationalMatrix(Collections.emptyList(), width));
        } else {
            Optional<List<RationalVector>> ors = Readers.readListStrict(RationalVector::readStrict).apply(s);
            if (!ors.isPresent()) return Optional.empty();
            List<RationalVector> rs = ors.get();
            if (rs.isEmpty() || !same(map(RationalVector::dimension, rs))) return Optional.empty();
            return Optional.of(new RationalMatrix(rs, rs.get(0).dimension()));
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

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code RationalMatrix} used outside
     * this class.
     */
    public void validate() {
        assertTrue(this, width >= 0);
        assertTrue(this, all(r -> r.dimension() == width, rows));
    }
}
