package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.iterables.NoRemoveIterable;
import mho.wheels.ordering.comparators.LexComparator;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>A matrix with {@link java.math.BigInteger} elements. The number of rows is the height, and the number of rows is
 * the width. Either the height or the width, or both, may be 0; 0-height matrices are written {@code "[]#w"}, where
 * {@code w} is the width. When referring to elements, the (<i>i</i>, <i>j</i>)th element is the element in row
 * <i>i</i> and column <i>j</i>.</p>
 *
 * <p>This class is immutable.</p>
 */
public final class Matrix implements Comparable<Matrix> {
    /**
     * Used by {@link mho.qbar.objects.Matrix#compareTo}
     */
    private static final Comparator<Iterable<Vector>> VECTOR_ITERABLE_COMPARATOR = new LexComparator<>();

    /**
     * The matrix's rows
     */
    private final @NotNull List<Vector> rows;

    /**
     * The matrix's width. The width can usually be inferred from {@code rows}, except when the height is 0, in which
     * case {@code rows} is empty and this field is the only way to determine the width.
     */
    private final int width;

    /**
     * Private constructor for {@code Matrix}; assumes arguments are valid
     *
     * <ul>
     *  <li>{@code rows} cannot have any null elements.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The dimension of every {@code Vector} in rows must be {@code width}.</li>
     *  <li>Any {@code Matrix} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param rows the matrix's rows
     * @param width the matrix's width
     */
    private Matrix(@NotNull List<Vector> rows, int width) {
        this.rows = rows;
        this.width = width;
    }

    /**
     * Returns an {@code Iterable} over this {@code Matrix}'s rows. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result is finite, contains no nulls, and every element has the same dimension.</li>
     * </ul>
     *
     * Length is height({@code this})
     *
     * @return an {@code Iterable} over this {@code Matrix}'s rows
     */
    public @NotNull Iterable<Vector> rows() {
        return new NoRemoveIterable<>(rows);
    }

    /**
     * Returns an {@code Iterable} over this {@code Matrix}'s columns. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result is finite, contains no nulls, and every element has the same dimension.</li>
     * </ul>
     *
     * Length is width({@code this})
     *
     * @return an {@code Iterable} over this {@code Matrix}'s columns
     */
    public @NotNull Iterable<Vector> columns() {
        if (rows.isEmpty()) {
            return replicate(width, Vector.ZERO_DIMENSIONAL);
        } else {
            //noinspection RedundantCast
            return map(
                    Vector::of,
                    IterableUtils.transpose((Iterable<Iterable<BigInteger>>) map(i -> (Iterable<BigInteger>) i, rows))
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
    public @NotNull Vector row(int i) {
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
    public @NotNull Vector column(int j) {
        if (rows.isEmpty()) {
            if (j < 0) {
                throw new ArrayIndexOutOfBoundsException("j cannot be negative. Invalid j: " + j);
            }
            if (j >= width) {
                throw new ArrayIndexOutOfBoundsException("j must be less than the width of this. j: " +
                        j + ", this: " + this);
            }
            return Vector.ZERO_DIMENSIONAL;
        } else {
            return Vector.of(toList(map(i -> i.get(j), rows)));
        }
    }

    /**
     * Converts {@code this} to a {@code RationalMatrix}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result is a {@code RationalMatrix} with only integral elements.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return a {@code RationalMatrix} with the same value as {@code this}
     */
    public @NotNull RationalMatrix toRationalMatrix() {
        if (rows.isEmpty()) {
            return RationalMatrix.zero(0, width);
        } else {
            return RationalMatrix.fromRows(toList(map(Vector::toRationalVector, rows)));
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
    public @NotNull BigInteger get(int i, int j) {
        return rows.get(i).get(j);
    }

    /**
     * Creates a {@code Matrix} from its row vectors. If an empty list is given, the matrix with height 0 and width 0
     * is returned. This method cannot be used to create matrices with 0 height and nonzero width.
     *
     * <ul>
     *  <li>{@code this} cannot be null, and every element must have the same dimension.</li>
     *  <li>The result either has nonzero height, or 0 width and 0 height.</li>
     * </ul>
     *
     * Size is |{@code rows}|×0 if {@code rows} is empty, |{@code rows}|×|{@code rows.get(0)}| otherwise
     *
     * @param rows the matrix's rows
     * @return a {@code Matrix} with the given rows
     */
    public static @NotNull Matrix fromRows(@NotNull List<Vector> rows) {
        if (any(a -> a == null, rows)) {
            throw new NullPointerException();
        } else if (!same(map(Vector::dimension, rows))) {
            throw new IllegalArgumentException("Every element of rows must have the same dimension. Invalid rows: " +
                    rows);
        } else {
            return new Matrix(toList(rows), rows.isEmpty() ? 0 : rows.get(0).dimension());
        }
    }

    /**
     * Creates a {@code Matrix} from its column vectors. If an empty list is given, the matrix with height 0 and width
     * 0 is returned. This method cannot be used to create matrices with 0 width and nonzero height.
     *
     * <ul>
     *  <li>{@code this} cannot be null, and every element must have the same dimension.</li>
     *  <li>The result either has nonzero width, or 0 width and 0 height.</li>
     * </ul>
     *
     * Size is 0×|{@code columns}| if {@code columns} is empty, |{@code columns.get(0)}|×|{@code columns}| otherwise
     *
     * @param columns the matrix's columns
     * @return a {@code Matrix} with the given columns
     */
    public static @NotNull Matrix fromColumns(@NotNull List<Vector> columns) {
        if (any(a -> a == null, columns)) {
            throw new NullPointerException();
        } else if (!same(map(Vector::dimension, columns))) {
            throw new IllegalArgumentException("Every element of columns must have the same dimension." +
                    " Invalid columns: " + columns);
        } else if (columns.isEmpty()) {
            return new Matrix(Collections.emptyList(), 0);
        } else {
            //noinspection RedundantCast
            return new Matrix(
                    toList(
                            map(
                                    Vector::of,
                                    IterableUtils.transpose(
                                            (Iterable<Iterable<BigInteger>>)
                                                    map(c -> (Iterable<BigInteger>) c, columns)
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
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the maximum coordinate bit length
     */
    public int maxElementBitLength() {
        if (isZero()) return 0;
        return maximum(map(Vector::maxCoordinateBitLength, rows));
    }

    /**
     * Returns this {@code Matrix}'s height.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the number of rows in {@code this}
     */
    public int height() {
        return rows.size();
    }

    /**
     * Returns this {@code Matrix}'s width.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
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
     *  <li>{@code this} may be any {@code Matrix}.</li>
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
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether the elements of {@code this} are all 0.
     */
    public boolean isZero() {
        return all(Vector::isZero, rows);
    }

    /**
     * Creates a zero matrix with a given height and width.
     *
     * <ul>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is a {@code Matrix} all of whose coordinates are 0.</li>
     * </ul>
     *
     * Size is {@code height}×{@code width}
     *
     * @param height the zero matrix's height
     * @param width the zero matrix's width
     * @return 0<sub>{@code height}, {@code width}</sub>
     */
    public static @NotNull Matrix zero(int height, int width) {
        if (height < 0) {
            throw new IllegalArgumentException("height cannot be negative. Invalid height: " + height);
        }
        if (width < 0) {
            throw new IllegalArgumentException("width cannot be negative. Invalid width: " + width);
        }
        return new Matrix(toList(replicate(height, Vector.zero(width))), width);
    }

    /**
     * Determines whether {@code this} is an identity matrix.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result may be either boolean.</li>
     * </ul>
     *
     * @return whether {@code this} is an identity
     */
    public boolean isIdentity() {
        if (!isSquare()) return false;
        for (int i = 0; i < width; i++) {
            Vector row = row(i);
            for (int j = 0; j < width; j++) {
                if (!row.get(j).equals(i == j ? BigInteger.ONE : BigInteger.ZERO)) return false;
            }
        }
        return true;
    }

    /**
     * Creates an identity matrix.
     *
     * <ul>
     *  <li>{@code dimension} cannot be negative.</li>
     *  <li>The result is a square {@code Matrix}, with height and width at least 1, with ones on the diagonal and
     *  zeros everywhere else.</li>
     * </ul>
     *
     * Size is {@code dimension}×{@code dimension}
     *
     * @param dimension the matrix's dimension (width and height)
     * @return I<sub>{@code dimension}</sub>
     */
    public static @NotNull Matrix identity(int dimension) {
        if (dimension < 0) {
            throw new IllegalArgumentException("dimension cannot be negative. Invalid dimension: " + dimension);
        }
        return new Matrix(toList(map(i -> Vector.standard(dimension, i), range(0, dimension - 1))), dimension);
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
    public @NotNull BigInteger trace() {
        if (!isSquare()) {
            throw new IllegalArgumentException("this must be square. Invalid this: " + this);
        }
        BigInteger sum = BigInteger.ZERO;
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
     *  <li>{@code this} may be any {@code Matrix}.</li>
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
    public @NotNull Matrix submatrix(@NotNull List<Integer> rowIndices, @NotNull List<Integer> columnIndices) {
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
            return new Matrix(toList(map(rows::get, rowIndices)), width);
        } else {
            List<Vector> submatrixRows = new ArrayList<>();
            for (int i : rowIndices) {
                Vector row = rows.get(i);
                submatrixRows.add(Vector.of(toList(map(row::get, columnIndices))));
            }
            return new Matrix(submatrixRows, columnIndices.size());
        }
    }

    /**
     * Returns the transpose of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is width({@code this})×height({@code this})
     *
     * @return {@code this}<sup>T</sup>
     */
    public @NotNull Matrix transpose() {
        int height = height();
        if (height == 0 || width == 0) {
            //noinspection SuspiciousNameCombination
            return zero(width, height);
        }
        BigInteger[][] elements = new BigInteger[width][height];
        for (int i = 0; i < height; i++) {
            Vector row = rows.get(i);
            for (int j = 0; j < width; j++) {
                elements[j][i] = row.get(j);
            }
        }
        //noinspection SuspiciousNameCombination
        return new Matrix(toList(map(i -> Vector.of(Arrays.asList(elements[i])), range(0, width - 1))), height);
    }

    /**
     * Returns the {@code Matrix} produced by concatenating the rows of {@code this} with the rows of {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same width.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is (height({@code this}+height({@code that}))×width({@code this})
     *
     * @param that the {@code Matrix} that {@code this} is concatenated with
     * @return {@code this} concatenated with {@code that}
     */
    public @NotNull Matrix concat(@NotNull Matrix that) {
        if (width != that.width) {
            throw new IllegalArgumentException("this and that must have the same width. this: " + this + ", that: " +
                    that);
        }
        if (height() == 0) return that;
        if (that.height() == 0) return this;
        return new Matrix(toList(IterableUtils.concat(rows, that.rows)), width);
    }

    /**
     * Returns {@code this} augmented with {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same height.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×(width({@code this}+width({@code that}))
     *
     * @param that the {@code Matrix} that {@code this} is augmented by
     * @return {@code this}|{@code that}
     */
    public @NotNull Matrix augment(@NotNull Matrix that) {
        if (height() != that.height()) {
            throw new IllegalArgumentException("this and that must have the same height. this: " + this + ", that: " +
                    that);
        }
        if (width == 0) return that;
        if (that.width == 0) return this;
        return new Matrix(
                toList(zipWith((r, s) -> Vector.of(toList(IterableUtils.concat(r, s))), rows, that.rows)),
                width + that.width
        );
    }

    /**
     * Returns the sum of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same height and the same width.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code Matrix} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull Matrix add(@NotNull Matrix that) {
        int height = height();
        if (width != that.width || height != that.height()) {
            throw new ArithmeticException("this and that must have the same width and height. this: " +
                    this + ", that: " + that);
        }
        if (height == 0 || width == 0) return this;
        return new Matrix(toList(zipWith(Vector::add, rows, that.rows)), width);
    }

    /**
     * Returns the negative of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return –{@code this}
     */
    public @NotNull Matrix negate() {
        int height = height();
        if (height == 0 || width == 0) return this;
        return new Matrix(toList(map(Vector::negate, rows)), width);
    }

    /**
     * Returns the difference of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same height and the same width.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code Matrix} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull Matrix subtract(@NotNull Matrix that) {
        int height = height();
        if (width != that.width || height != that.height()) {
            throw new ArithmeticException("this and that must have the same width and height. this: " +
                    this + ", that: " + that);
        }
        if (height == 0 || width == 0) return this;
        return new Matrix(toList(zipWith(Vector::subtract, rows, that.rows)), width);
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Matrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code BigInteger} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Matrix multiply(@NotNull BigInteger that) {
        if (height() == 0 || width == 0 || that.equals(BigInteger.ONE)) return this;
        return new Matrix(toList(map(r -> r.multiply(that), rows)), width);
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Matrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param that the {@code int} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Matrix multiply(int that) {
        if (height() == 0 || width == 0 || that == 1) return this;
        return new Matrix(toList(map(r -> r.multiply(that), rows)), width);
    }

    /**
     * Returns the product of {@code this} and {@code that}. The result is {@code that} after a linear transformation
     * whose basis vectors are the columns of {@code this}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Matrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The width of {@code this} must equal the dimension of {@code that}.</li>
     * </ul>
     *
     * Length is height({@code this})
     *
     * @param that the {@code Vector} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Vector multiply(@NotNull Vector that) {
        if (width != that.dimension()) {
            throw new ArithmeticException("The width of this must equal the dimension of that. this: " +
                    this + ", that: " + that);
        }
        return Vector.of(toList(map(r -> r.dot(that), rows)));
    }

    /**
     * Returns the matrix product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Matrix}</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The width of {@code this} must equal the height of {@code that}.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code that})
     *
     * @param that the {@code Matrix} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Matrix multiply(@NotNull Matrix that) {
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
        List<Vector> rows = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Vector row = row(i);
            List<BigInteger> productRow = new ArrayList<>();
            for (int k = 0; k < l; k++) {
                BigInteger sum = BigInteger.ZERO;
                for (int j = 0; j < m; j++) {
                    sum = sum.add(row.get(j).multiply(that.get(j, k)));
                }
                productRow.add(sum);
            }
            rows.add(Vector.of(productRow));
        }
        return fromRows(rows);
    }

    /**
     * Returns the left shift of {@code this} by {@code bits}; {@code this}×2<sup>{@code bits}</sup>.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Matrix}.</li>
     *  <li>{@code bits} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @param bits the number of bits to left-shift by
     * @return {@code this}≪{@code bits}
     */
    public @NotNull Matrix shiftLeft(int bits) {
        if (bits < 0) {
            throw new ArithmeticException("bits cannot be negative. Invalid bits: " + bits);
        }
        if (isZero() || bits == 0) return this;
        return new Matrix(toList(map(r -> r.shiftLeft(bits), rows)), width);
    }

    /**
     * Determines whether {@code this} is in row echelon form; whether all zero rows are at the bottom, and the first
     * nonzero element of every row is strictly to the right of the first nonzero element of the row above it. Note
     * that contrary to some definitions of row echelon form, the first nonzero element of a row is not necessarily 1.
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
        for (Vector row : rows()) {
            Optional<Integer> oi = findIndex(x -> !x.equals(BigInteger.ZERO), row);
            if (!oi.isPresent()) break;
            int pivotIndex = oi.get();
            if (pivotIndex <= lastPivotIndex) return false;
            lastPivotIndex = pivotIndex;
        }
        return true;
    }

    /**
     * Returns a row echelon form of {@code this}. In other words, all zero rows are at the bottom, and the first
     * nonzero element of every row is strictly to the right of the first nonzero element of the row above it. Note
     * that contrary to some definitions of row echelon form, the first nonzero element of a row is not necessarily 1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result is in row echelon form.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return a row echelon form of {@code this}
     */
    public @NotNull Matrix rowEchelonForm() {
        int height = height();
        boolean changed = false;
        List<Vector> refRows = rows;
        int i = 0;
        outer:
        for (int j = 0; i < height && j < width; j++) {
            int nonzeroRowIndex = i;
            BigInteger pivot = refRows.get(i).get(j);
            while (pivot.equals(BigInteger.ZERO)) {
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
            Vector nonzeroRow = refRows.get(i);
            for (int k = i + 1; k < height; k++) {
                Vector row = refRows.get(k);
                BigInteger belowPivot = row.get(j);
                if (!belowPivot.equals(BigInteger.ZERO)) {
                    if (!changed) {
                        changed = true;
                        refRows = toList(rows);
                    }
                    refRows.set(k, row.multiply(pivot).subtract(nonzeroRow.multiply(belowPivot)));
                }
            }
            i++;
        }
        return changed ? new Matrix(refRows, width) : this;
    }

    /**
     * Returns a row echelon form of {@code this}. In other words, all zero rows are at the bottom, and the first
     * nonzero element of every row is strictly to the right of the first nonzero element of the row above it. Note
     * that contrary to some definitions of row echelon form, the first nonzero element of a row is not necessarily 1.
     * This method differs from {@link Matrix#rowEchelonForm()} in that each row is primitive (see
     * {@link Vector#isPrimitive()}).
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result is in row echelon form and each row is primitive.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return a row echelon form of {@code this} with all rows primitive
     */
    public @NotNull Matrix primitiveRowEchelonForm() {
        int height = height();
        boolean changed = false;
        List<Vector> refRows = toList(rows());
        int i = 0;
        outer:
        for (int j = 0; i < height && j < width; j++) {
            int nonzeroRowIndex = i;
            BigInteger pivot = refRows.get(i).get(j);
            while (pivot.equals(BigInteger.ZERO)) {
                nonzeroRowIndex++;
                if (nonzeroRowIndex == height) continue outer;
                pivot = refRows.get(nonzeroRowIndex).get(j);
            }
            if (nonzeroRowIndex != i) {
                if (!changed) {
                    changed = true;
                    refRows = toList(rows());
                }
                Collections.swap(refRows, i, nonzeroRowIndex);
            }
            Vector nonzeroRow = refRows.get(i);
            for (int k = i + 1; k < height; k++) {
                Vector row = refRows.get(k);
                BigInteger belowPivot = row.get(j);
                if (!belowPivot.equals(BigInteger.ZERO)) {
                    if (!changed) {
                        changed = true;
                        refRows = toList(rows());
                    }
                    BigInteger gcd = pivot.gcd(belowPivot);
                    refRows.set(
                            k,
                            row.multiply(pivot.divide(gcd)).subtract(nonzeroRow.multiply(row.get(j).divide(gcd)))
                    );
                }
            }
            i++;
        }
        for (i = 0; i < height; i++) {
            Vector row = refRows.get(i);
            Vector primitiveRow = row.makePrimitive();
            if (row != primitiveRow) {
                if (!changed) {
                    changed = true;
                    refRows = toList(rows());
                }
                refRows.set(i, primitiveRow);
            }
        }
        return changed ? fromRows(refRows) : this;
    }

    /**
     * Returns the rank of {@code this}, or the dimension of the row space (which equals the dimension of the column
     * space).
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result is not negative.</li>
     * </ul>
     *
     * @return rank({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public int rank() {
        Matrix ref = rowEchelonForm();
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
     * {@link Matrix#isInRowEchelonForm()}) and every leading element is the only nonzero element in its column. Note
     * that contrary to the usual definitions of reduced row echelon form, the first nonzero element of a row is not
     * necessarily 1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is in reduced row echelon form
     */
    public boolean isInReducedRowEchelonForm() {
        if (!isInRowEchelonForm()) return false;
        for (int i = 0; i < height(); i++) {
            Vector row = row(i);
            Optional<Integer> oi = findIndex(x -> !x.equals(BigInteger.ZERO), row);
            if (!oi.isPresent()) break;
            int pivotIndex = oi.get();
            for (int j = 0; j < i; j++) {
                if (!get(j, pivotIndex).equals(BigInteger.ZERO)) return false;
            }
        }
        return true;
    }

    /**
     * Returns the reduced row echelon form of {@code this}. In other words, the result is in row echelon form (see
     * {@link Matrix#isInRowEchelonForm()}) and every leading element is the only nonzero element in its column. Note
     * that contrary to the usual definitions of reduced row echelon form, the first nonzero element of a row is not
     * necessarily 1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result is in reduced row echelon form.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return the reduced row echelon form of {@code this}
     */
    public @NotNull Matrix reducedRowEchelonForm() {
        int height = height();
        if (width == 0 || height == 0) return this;
        Matrix ref = rowEchelonForm();
        if (width <= height && !ref.row(width - 1).isZero()) {
            Matrix identity = identity(width);
            return width == height ? identity : identity.concat(zero(height - width, width));
        }
        boolean changed = false;
        List<Vector> rrefRows = ref.rows;
        for (int i = 0; i < height(); i++) {
            Vector row = rrefRows.get(i);
            Optional<Integer> pivotIndex = findIndex(x -> !x.equals(BigInteger.ZERO), row);
            if (!pivotIndex.isPresent()) break;
            int j = pivotIndex.get();
            BigInteger pivot = row.get(j);
            for (int k = i - 1; k >= 0; k--) {
                Vector above = rrefRows.get(k);
                BigInteger abovePivot = above.get(j);
                if (!abovePivot.equals(BigInteger.ZERO)) {
                    if (!changed) {
                        changed = true;
                        rrefRows = toList(ref.rows);
                    }
                    rrefRows.set(k, above.multiply(pivot).subtract(row.multiply(abovePivot)));
                }
            }
        }
        return changed ? new Matrix(rrefRows, width) : ref;
    }

    /**
     * Returns the reduced row echelon form of {@code this}. In other words, the result is in row echelon form (see
     * {@link Matrix#isInRowEchelonForm()}) and every leading element is the only nonzero element in its column. Note
     * that contrary to the usual definitions of reduced row echelon form, the first nonzero element of a row is not
     * necessarily 1. This method differs from {@link Matrix#reducedRowEchelonForm()} in that each row is primitive
     * (see {@link Vector#isPrimitive()}).
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>The result is in reduced row echelon form.</li>
     * </ul>
     *
     * Size is height({@code this})×width({@code this})
     *
     * @return the reduced row echelon form of {@code this}
     */
    public @NotNull Matrix primitiveReducedRowEchelonForm() {
        int height = height();
        if (width == 0 || height == 0) return this;
        Matrix ref = primitiveRowEchelonForm();
        if (width <= height && !ref.row(width - 1).isZero()) {
            Matrix identity = identity(width);
            return width == height ? identity : identity.concat(zero(height - width, width));
        }
        boolean changed = false;
        List<Vector> rrefRows = ref.rows;
        for (int i = 0; i < height(); i++) {
            Vector row = rrefRows.get(i);
            Optional<Integer> pivotIndex = findIndex(x -> !x.equals(BigInteger.ZERO), row);
            if (!pivotIndex.isPresent()) break;
            int j = pivotIndex.get();
            BigInteger pivot = row.get(j);
            for (int k = i - 1; k >= 0; k--) {
                Vector above = rrefRows.get(k);
                BigInteger abovePivot = above.get(j);
                if (!abovePivot.equals(BigInteger.ZERO)) {
                    if (!changed) {
                        changed = true;
                        rrefRows = toList(ref.rows);
                    }
                    BigInteger gcd = pivot.gcd(abovePivot);
                    rrefRows.set(k, above.multiply(pivot.divide(gcd)).subtract(row.multiply(abovePivot.divide(gcd))));
                }
            }
        }
        for (int k = 0; k < height; k++) {
            Vector row = rrefRows.get(k);
            Vector primitiveRow = row.makePrimitive();
            if (row != primitiveRow) {
                if (!changed) {
                    changed = true;
                    rrefRows = toList(rows());
                }
                rrefRows.set(k, primitiveRow);
            }
        }
        return changed ? new Matrix(rrefRows, width) : ref;
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
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>{@code rhs} may be any {@code Vector}.</li>
     *  <li>The dimension of {@code rhs} must equal the height of {@code this}.</li>
     *  <li>The result may be empty or any {@code RationalVector}.</li>
     * </ul>
     *
     * Length is width({@code this})
     *
     * @param rhs the right-hand side of the system of equations
     * @return The assignments to the variables in the equations
     */
    public @NotNull Optional<RationalVector> solveLinearSystem(@NotNull Vector rhs) {
        if (height() != rhs.dimension()) {
            throw new IllegalArgumentException("The dimension of rhs must equal the height of this. rhs: " +
                    rhs + ", this: " + this);
        }
        if (width > height()) return Optional.empty();
        Matrix rref = augment(fromColumns(Collections.singletonList(rhs))).reducedRowEchelonForm();
        Matrix bottom = rref.submatrix(toList(range(width, height() - 1)), toList(range(0, width)));
        if (!bottom.isZero()) return Optional.empty();
        List<Rational> result = new ArrayList<>();
        int lastColumnIndex = rref.width - 1;
        for (int i = 0; i < width; i++) {
            Vector row = rref.row(i);
            for (int j = 0; j < width; j++) {
                if ((i == j) == row.get(j).equals(BigInteger.ZERO)) return Optional.empty();
            }
            result.add(Rational.of(row.get(lastColumnIndex), row.get(i)));
        }
        return Optional.of(RationalVector.of(result));
    }

    /**
     * Returns the inverse of {@code this}, or an empty {@code Optional} if {@code this} is not invertible.
     *
     * <ul>
     *  <li>{@code this} must be square.</li>
     *  <li>The result is either empty or an invertible {@code RationalMatrix} whose inverse contains only integral
     *  elements.</li>
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
        Matrix rref = augment(identity(width)).reducedRowEchelonForm();
        List<RationalVector> resultRows = new ArrayList<>();
        for (int i = 0; i < height(); i++) {
            Vector row = rref.row(i);
            for (int j = 0; j < width; j++) {
                if ((i == j) == row.get(j).equals(BigInteger.ZERO)) return Optional.empty();
            }
            BigInteger denominator = row.get(i);
            List<Rational> resultRow = new ArrayList<>();
            for (int j = width; j < 2 * width; j++) {
                resultRow.add(Rational.of(row.get(j), denominator));
            }
            resultRows.add(RationalVector.of(resultRow));
        }
        return Optional.of(RationalMatrix.fromRows(resultRows));
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
    public @NotNull BigInteger determinant() {
        if (width != height()) {
            throw new IllegalArgumentException("this must be square. Invalid this: " + this);
        }
        int n = width;
        if (n == 0) return BigInteger.ONE;
        if (n == 1) return get(0, 0);
        BigInteger[][] arrayA = new BigInteger[n][n];
        for (int i = 0; i < n; i++) {
            Vector row = row(i);
            arrayA[i] = new BigInteger[n];
            for (int j = 0; j < n; j++) {
                arrayA[i][j] = row.get(j);
            }
        }
        BigInteger[][] arrayB = new BigInteger[n - 1][n - 1];
        arrayB[0][0] = BigInteger.ONE;
        boolean swapSign = true;
        for (int k = 0; k < n - 1; k++) {
            int firstNonzeroColumnIndex = -1;
            for (int j = 0; j < n - k; j++) {
                if (!arrayA[0][j].equals(BigInteger.ZERO)) {
                    firstNonzeroColumnIndex = j;
                    break;
                }
            }
            if (firstNonzeroColumnIndex == -1) {
                return BigInteger.ZERO;
            }
            if (firstNonzeroColumnIndex != 0) {
                for (BigInteger[] row : arrayA) {
                    BigInteger temp = row[firstNonzeroColumnIndex];
                    row[firstNonzeroColumnIndex] = row[0];
                    row[0] = temp;
                }
                swapSign = !swapSign;
            }
            BigInteger denominator = arrayB[0][0];
            for (int i = 1; i < n - k; i++) {
                arrayB[i - 1][0] = BigInteger.ZERO;
                for (int j = 1; j < n - k; j++) {
                    BigInteger numerator = arrayA[0][0].multiply(arrayA[i][j])
                            .subtract(arrayA[i][0].multiply(arrayA[0][j]));
                    arrayB[i - 1][j - 1] = numerator.divide(denominator);
                }
            }
            BigInteger[][] temp = arrayA;
            arrayA = arrayB;
            arrayB = temp;
        }
        BigInteger determinant = arrayA[0][0];
        return swapSign ? determinant : determinant.negate();
    }

    /**
     * Given the 0th to dth power sums of a d-degree monic polynomial, where the ith power sum is the sum of the ith
     * powers of the roots, returns the polynomial. This method is of limited use, because it only works when the power
     * sums are all integers and it is known ahead of time that the result polynomial has integer coefficients.
     *
     * <ul>
     *  <li>{@code powerSums} cannot be empty, and its first element must be one less than its length.</li>
     *  <li>The result is monic.</li>
     * </ul>
     *
     * @param powerSums the Newton sums of a monic {@code Polynomial}
     * @return the monic {@code Polynomial} whose Newton sums are {@code powerSums}
     */
    private static @NotNull Polynomial fromPowerSums(@NotNull List<BigInteger> powerSums) {
        List<BigInteger> coefficients = new ArrayList<>();
        coefficients.add(BigInteger.ONE);
        for (int i = 1; i < powerSums.size(); i++) {
            List<BigInteger> terms = new ArrayList<>();
            for (int j = 1; j <= i; j++) {
                terms.add(powerSums.get(j).multiply(coefficients.get(i - j)));
            }
            coefficients.add(sumBigInteger(terms).negate().divide(BigInteger.valueOf(i)));
        }
        return Polynomial.of(reverse(coefficients));
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
    public @NotNull Polynomial characteristicPolynomial() {
        if (width != height()) {
            throw new IllegalArgumentException("this must be square. Invalid this: " + this);
        }
        int n = width;
        if (n == 0) return Polynomial.ONE;
        int r = 1;
        while (r * r <= n) r++;
        List<Matrix> powers = new ArrayList<>();
        Matrix previousPower = identity(n);
        powers.add(previousPower);
        List<BigInteger> powerSums = toList(replicate(r * r, BigInteger.ZERO));
        powerSums.set(0, BigInteger.valueOf(n));
        for (int i = 1; i < r; i++) {
            Matrix nextPower = multiply(previousPower);
            powers.add(nextPower);
            powerSums.set(i, nextPower.trace());
            previousPower = nextPower;
        }
        List<Matrix> cs = new ArrayList<>();
        Matrix firstC = multiply(powers.get(r - 1));
        cs.add(firstC);
        Matrix previousC = firstC;
        powerSums.set(r, firstC.trace());
        for (int j = 2; j < r; j++) {
            Matrix nextC = firstC.multiply(previousC);
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
        return fromPowerSums(toList(take(n + 1, powerSums)));
    }

    /**
     * Returns the Kronecker product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is the Kronecker product of two matrices.</li>
     * </ul>
     *
     * Size is (height({@code this})height({@code that}))×(width({@code this})width({@code that}))
     *
     * @param that the {@code Matrix} that {@code this} is multiplied by.
     * @return {@code this}⊗{@code that}
     */
    public @NotNull Matrix kroneckerMultiply(@NotNull Matrix that) {
        List<Vector> productRows = new ArrayList<>();
        for (Vector thisRow : rows) {
            //noinspection Convert2streamapi
            for (Vector thatRow : that.rows) {
                //noinspection Convert2MethodRef
                productRows.add(Vector.of(toList(concatMap(x -> map(y -> x.multiply(y), thatRow), thisRow))));
            }
        }
        return new Matrix(productRows, width * that.width);
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
     * @param that the {@code Matrix} added to {@code this}
     * @return {@code this}⊕{@code that}
     */
    public @NotNull Matrix kroneckerAdd(@NotNull Matrix that) {
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
     *  <li>{@code this} may be any {@code Matrix}.</li>
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
        Matrix matrix = (Matrix) that;
        return width == matrix.width && rows.equals(matrix.rows);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
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
     * "equal to", respectively. Shortlex ordering is used; {@code Matrix}es are compared first by height, then by
     * width, and then top-to-bottom, left-to-right, element by element.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that The {@code Matrix} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull Matrix that) {
        if (this == that) return 0;
        if (rows.size() > that.rows.size()) return 1;
        if (rows.size() < that.rows.size()) return -1;
        if (width > that.width) return 1;
        if (width < that.width) return -1;
        return VECTOR_ITERABLE_COMPARATOR.compare(rows, that.rows);
    }

    /**
     * Creates an {@code Matrix} from a {@code String}. Valid input takes the form of a {@code String} that could have
     * been returned by {@link mho.qbar.objects.Matrix#toString}. See that method's tests and demos for examples of
     * valid input. Note that {@code "[]"} is not a valid input; use {@code "[]#0"} instead.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<Matrix>}.</li>
     * </ul>
     *
     * @param s a string representation of a {@code Matrix}.
     * @return the wrapped {@code Matrix} represented by {@code s}, or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<Matrix> readStrict(@NotNull String s) {
        if (s.startsWith("[]#")) {
            Optional<Integer> oWidth = Readers.readIntegerStrict(s.substring(3));
            if (!oWidth.isPresent()) return Optional.empty();
            int width = oWidth.get();
            if (width < 0) return Optional.empty();
            return Optional.of(new Matrix(Collections.emptyList(), width));
        } else {
            Optional<List<Vector>> ors = Readers.readListStrict(Vector::readStrict).apply(s);
            if (!ors.isPresent()) return Optional.empty();
            List<Vector> rs = ors.get();
            if (rs.isEmpty() || !same(map(Vector::dimension, rs))) return Optional.empty();
            return Optional.of(new Matrix(rs, rs.get(0).dimension()));
        }
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Matrix}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        return rows.isEmpty() ? "[]#" + width : rows.toString();
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code Matrix} used outside this
     * class.
     */
    public void validate() {
        assertTrue(this, width >= 0);
        assertTrue(this, all(r -> r.dimension() == width, rows));
    }
}
