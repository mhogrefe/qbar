package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Quadruple;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.qbar.objects.Matrix.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.compare;
import static mho.wheels.testing.Testing.*;

public class MatrixProperties extends QBarTestProperties {
    private static final @NotNull String MATRIX_CHARS = " #,-0123456789[]";

    public MatrixProperties() {
        super("Matrix");
    }

    @Override
    protected void testBothModes() {
        propertiesRows();
        propertiesColumns();
        propertiesRow();
        propertiesColumn();
        propertiesToRationalMatrix();
        propertiesGet();
        propertiesFromRows();
        propertiesFromColumns();
        propertiesMaxElementBitLength();
        propertiesHeight();
        propertiesWidth();
        propertiesIsSquare();
        propertiesIsZero();
        propertiesZero();
        propertiesIsIdentity();
        propertiesIdentity();
        propertiesTrace();
        propertiesSubmatrix();
        propertiesTranspose();
        compareImplementationsTranspose();
        propertiesConcat();
        propertiesAugment();
        propertiesAdd();
        propertiesNegate();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesMultiply_BigInteger();
        propertiesMultiply_int();
        propertiesMultiply_Vector();
        propertiesMultiply_Matrix();
        compareImplementationsMultiply_Matrix();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesIsInRowEchelonForm();
        propertiesRowEchelonForm();
        propertiesPrimitiveRowEchelonForm();
        compareImplementationsPrimitiveRowEchelonForm();
        propertiesRank();
        compareImplementationsRank();
        propertiesIsInvertible();
        compareImplementationsIsInvertible();
        propertiesIsInReducedRowEchelonForm();
        propertiesReducedRowEchelonForm();
        propertiesPrimitiveReducedRowEchelonForm();
        compareImplementationsPrimitiveReducedRowEchelonForm();
        propertiesSolveLinearSystem();
        compareImplementationsSolveLinearSystem();
        propertiesInvert();
        compareImplementationsInvert();
        propertiesDeterminant();
        compareImplementationsDeterminant();
        propertiesCharacteristicPolynomial();
        compareImplementationsCharacteristicPolynomial();
        propertiesKroneckerMultiply();
        propertiesKroneckerAdd();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesReadStrict();
        propertiesToString();
    }

    private void propertiesRows() {
        initialize("rows()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            List<Vector> rows = toList(m.rows());
            assertTrue(m, all(v -> v.dimension() == m.width(), rows));
            assertEquals(m, rows, toList(m.transpose().columns()));
            testNoRemove(m.rows());
            testHasNext(m.rows());
        }

        for (Matrix m : take(LIMIT, filterInfinite(n -> n.height() > 0, P.matrices()))) {
            inverse(n -> toList(n.rows()), Matrix::fromRows, m);
        }
    }

    private void propertiesColumns() {
        initialize("columns()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            List<Vector> columns = toList(m.columns());
            assertTrue(m, all(v -> v.dimension() == m.height(), columns));
            assertEquals(m, columns, toList(m.transpose().rows()));
            testNoRemove(m.columns());
            testHasNext(m.columns());
        }

        for (Matrix m : take(LIMIT, filterInfinite(n -> n.width() > 0, P.matrices()))) {
            inverse(n -> toList(n.columns()), Matrix::fromColumns, m);
        }
    }

    private void propertiesRow() {
        initialize("row(int)");
        Iterable<Pair<Matrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.height() > 0, P.matrices()),
                m -> P.uniformSample(toList(range(0, m.height() - 1)))
        );
        for (Pair<Matrix, Integer> p : take(LIMIT, ps)) {
            Vector row = p.a.row(p.b);
            assertEquals(p, row, toList(p.a.rows()).get(p.b));
        }

        Iterable<Pair<Matrix, Integer>> psFail = filterInfinite(
                p -> p.b < 0 || p.b >= p.a.height(),
                P.pairs(P.matrices(), P.integers())
        );
        for (Pair<Matrix, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.row(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesColumn() {
        initialize("column(int)");
        Iterable<Pair<Matrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.width() > 0, P.matrices()),
                m -> P.uniformSample(toList(range(0, m.width() - 1)))
        );
        for (Pair<Matrix, Integer> p : take(LIMIT, ps)) {
            Vector column = p.a.column(p.b);
            assertEquals(p, column, toList(p.a.columns()).get(p.b));
        }

        Iterable<Pair<Matrix, Integer>> psFail = filterInfinite(
                p -> p.b < 0 || p.b >= p.a.width(),
                P.pairs(P.matrices(), P.integers())
        );
        for (Pair<Matrix, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.column(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesToRationalMatrix() {
        initialize("toRationalMatrix()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            RationalMatrix rm = m.toRationalMatrix();
            assertEquals(m, m.toString(), m.toString());
            assertEquals(m, m.height(), rm.height());
            assertEquals(m, m.width(), rm.width());
            inverse(Matrix::toRationalMatrix, RationalMatrix::toMatrix, m);
        }
    }

    private void propertiesGet() {
        initialize("get(int, int)");
        Iterable<Triple<Matrix, Integer, Integer>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        filterInfinite(m -> m.height() > 0 && m.width() > 0, P.matrices()),
                        m -> P.uniformSample(
                                toList(EP.pairsLex(range(0, m.height() - 1), toList(range(0, m.width() - 1))))
                        )
                )
        );
        for (Triple<Matrix, Integer, Integer> t : take(LIMIT, ts)) {
            BigInteger element = t.a.get(t.b, t.c);
            assertEquals(t, element, t.a.row(t.b).get(t.c));
            assertEquals(t, element, t.a.column(t.c).get(t.b));
        }

        Iterable<Triple<Matrix, Integer, Integer>> tsFail = filterInfinite(
                t -> t.b < 0 || t.b >= t.a.height() || t.c < 0 || t.c >= t.a.width(),
                P.triples(P.matrices(), P.integers(), P.integers())
        );
        for (Triple<Matrix, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.get(t.b, t.c);
                fail(t);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesFromRows() {
        initialize("fromRows(List<Vector>)");
        Iterable<List<Vector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.vectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, Vector.ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        for (List<Vector> vs : take(LIMIT, vss)) {
            Matrix m = fromRows(vs);
            m.validate();
            assertEquals(vs, m, fromColumns(vs).transpose());
            inverse(Matrix::fromRows, n -> toList(n.rows()), vs);
            assertEquals(vs, m.height(), vs.size());
            if (vs.size() != 0) {
                assertEquals(vs, m.width(), head(vs).dimension());
            }
        }

        Iterable<List<Vector>> vssFail = filterInfinite(
                us -> !same(map(Vector::dimension, us)),
                P.listsAtLeast(1, P.vectors())
        );
        for (List<Vector> vs : take(LIMIT, vssFail)) {
            try {
                fromRows(vs);
                fail(vs);
            } catch (IllegalArgumentException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.vectors(i))
                )
        );
        for (List<Vector> vs : take(LIMIT, vssFail)) {
            try {
                fromRows(vs);
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesFromColumns() {
        initialize("fromColumns(List<Vector>)");
        Iterable<List<Vector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.vectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, Vector.ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        for (List<Vector> vs : take(LIMIT, vss)) {
            Matrix m = fromColumns(vs);
            m.validate();
            assertEquals(vs, m, fromRows(vs).transpose());
            inverse(Matrix::fromColumns, n -> toList(n.columns()), vs);
            assertEquals(vs, m.width(), vs.size());
            if (vs.size() != 0) {
                assertEquals(vs, m.height(), head(vs).dimension());
            }
        }

        Iterable<List<Vector>> vssFail = filterInfinite(
                us -> !same(map(Vector::dimension, us)),
                P.listsAtLeast(1, P.vectors())
        );
        for (List<Vector> vs : take(LIMIT, vssFail)) {
            try {
                fromColumns(vs);
                fail(vs);
            } catch (IllegalArgumentException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.vectors(i))
                )
        );
        for (List<Vector> vs : take(LIMIT, vssFail)) {
            try {
                fromColumns(vs);
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesMaxElementBitLength() {
        initialize("maxElementBitLength()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            assertTrue(m, m.maxElementBitLength() >= 0);
        }
    }

    private void propertiesHeight() {
        initialize("height()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            int height = m.height();
            assertTrue(m, height >= 0);
            assertEquals(m, height, length(m.rows()));
        }
    }

    private void propertiesWidth() {
        initialize("width()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            int width = m.width();
            assertTrue(m, width >= 0);
            assertEquals(m, width, length(m.columns()));
        }
    }

    private void propertiesIsSquare() {
        initialize("isSquare()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            m.isSquare();
        }
    }

    private void propertiesIsZero() {
        initialize("isZero()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            assertEquals(m, m.isZero(), zero(m.height(), m.width()).equals(m));
        }
    }

    private void propertiesZero() {
        initialize("zero(int, int)");
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            Matrix zero = zero(p.a, p.b);
            zero.validate();
            assertTrue(p, zero.isZero());
            assertEquals(p, zero.height(), p.a);
            assertEquals(p, zero.width(), p.b);
            inverse(q -> zero(q.a, q.b), (Matrix m) -> new Pair<>(m.height(), m.width()), p);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.naturalIntegers()))) {
            try {
                zero(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.naturalIntegers(), P.negativeIntegers()))) {
            try {
                zero(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesIsIdentity() {
        initialize("isIdentity()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            m.isIdentity();
        }
    }

    private void propertiesIdentity() {
        initialize("identity(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Matrix identity = identity(i);
            identity.validate();
            assertTrue(i, identity.isSquare());
            assertTrue(i, identity.isIdentity());
            inverse(Matrix::identity, Matrix::height, i);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                identity(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesTrace() {
        initialize("trace()");
        for (Matrix m : take(LIMIT, P.squareMatrices())) {
            BigInteger trace = m.trace();
            assertEquals(m, trace, m.transpose().trace());
        }

        Iterable<Pair<Matrix, Matrix>> ps = P.withElement(
                new Pair<>(zero(0, 0), zero(0, 0)),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).matrices(i, i))
                        )
                )
        );
        for (Pair<Matrix, Matrix> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.add(p.b).trace(), p.a.trace().add(p.b.trace()));
            commutative((a, b) -> a.multiply(b).trace(), p);
            BigInteger productTrace = BigInteger.ZERO;
            for (int i = 0; i < p.a.height(); i++) {
                productTrace = productTrace.add(p.a.row(i).dot(p.b.column(i)));
            }
            assertEquals(p, p.a.multiply(p.b).trace(), productTrace);
        }

        for (Pair<Matrix, BigInteger> p : take(LIMIT, P.pairs(P.withScale(4).squareMatrices(), P.bigIntegers()))) {
            assertEquals(p, p.a.multiply(p.b).trace(), p.a.trace().multiply(p.b));
        }
    }

    private void propertiesSubmatrix() {
        initialize("submatrix(List<Integer>, List<Integer>)");
        Iterable<Triple<Matrix, List<Integer>, List<Integer>>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.matrices(),
                        m -> {
                            List<Integer> allRows = toList(EP.range(0, m.height() - 1));
                            List<Integer> allColumns = toList(EP.range(0, m.width() - 1));
                            return P.pairs(
                                    map(bs -> toList(select(bs, allRows)), P.lists(m.height(), P.booleans())),
                                    map(bs -> toList(select(bs, allColumns)), P.lists(m.width(), P.booleans()))
                            );
                        }
                )
        );
        for (Triple<Matrix, List<Integer>, List<Integer>> t : take(LIMIT, ts)) {
            Matrix submatrix = t.a.submatrix(t.b, t.c);
            submatrix.validate();
            assertEquals(t, submatrix.height(), t.b.size());
            assertEquals(t, submatrix.width(), t.c.size());
        }

        Matrix zero = zero(0, 0);
        for (Matrix m : take(LIMIT, P.matrices())) {
            assertEquals(m, m.submatrix(Collections.emptyList(), Collections.emptyList()), zero);
            assertEquals(m, m.submatrix(toList(range(0, m.height() - 1)), toList(range(0, m.width() - 1))), m);
        }

        Iterable<Triple<Matrix, List<Integer>, List<Integer>>> tsFail = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.matrices(),
                        m -> {
                            int height = m.height();
                            List<Integer> allColumns = toList(EP.range(0, m.width() - 1));
                            return P.pairs(
                                    filterInfinite(
                                            is -> any(i -> i == null || i < 0 || i >= height, is) || !increasing(is),
                                            P.lists(P.withNull(P.integersGeometric()))
                                    ),
                                    map(bs -> toList(select(bs, allColumns)), P.lists(m.width(), P.booleans()))
                            );
                        }
                )
        );
        for (Triple<Matrix, List<Integer>, List<Integer>> t : take(LIMIT, tsFail)) {
            try {
                t.a.submatrix(t.b, t.c);
                fail(t);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }

        tsFail = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.matrices(),
                        m -> {
                            List<Integer> allRows = toList(EP.range(0, m.height() - 1));
                            int width = m.width();
                            return P.pairs(
                                    map(bs -> toList(select(bs, allRows)), P.lists(m.height(), P.booleans())),
                                    filterInfinite(
                                            is -> any(i -> i == null || i < 0 || i >= width, is) || !increasing(is),
                                            P.lists(P.withNull(P.integersGeometric()))
                                    )
                            );
                        }
                )
        );
        for (Triple<Matrix, List<Integer>, List<Integer>> t : take(LIMIT, tsFail)) {
            try {
                t.a.submatrix(t.b, t.c);
                fail(t);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }
    }

    private static @NotNull Matrix transpose_alt(@NotNull Matrix m) {
        int height = m.height();
        int width = m.width();
        if (height == 0 || width == 0) {
            //noinspection SuspiciousNameCombination
            return zero(width, height);
        } else {
            //noinspection SuspiciousNameCombination,RedundantCast
            return fromRows(
                    toList(
                            map(
                                    Vector::of,
                                    (Iterable<List<BigInteger>>) IterableUtils.transpose(
                                            map(r -> (Iterable<BigInteger>) r, m.rows())
                                    )
                            )
                    )
            );
        }
    }

    private void propertiesTranspose() {
        initialize("transpose()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            Matrix transposed = m.transpose();
            transposed.validate();
            assertEquals(m, transposed, transpose_alt(m));
            int height = m.height();
            int width = m.width();
            assertEquals(m, transposed.height(), width);
            assertEquals(m, transposed.width(), height);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    assertEquals(m, m.get(i, j), transposed.get(j, i));
                }
            }
            assertEquals(m, m.isSquare(), transposed.isSquare());
            assertEquals(m, m.isZero(), transposed.isZero());
            assertEquals(m, m.isIdentity(), transposed.isIdentity());
            involution(Matrix::transpose, m);
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Matrix identity = identity(i);
            assertEquals(i, identity, identity.transpose());
        }
    }

    private void compareImplementationsTranspose() {
        Map<String, Function<Matrix, Matrix>> functions = new LinkedHashMap<>();
        functions.put("alt", MatrixProperties::transpose_alt);
        functions.put("standard", Matrix::transpose);
        compareImplementations("transpose()", take(LIMIT, P.matrices()), functions);
    }

    private void propertiesConcat() {
        initialize("concat(Matrix)");
        Iterable<Pair<Matrix, Matrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                filterInfinite(
                                        t -> t.a != 0 || t.b != 0,
                                        P.triples(
                                                P.naturalIntegersGeometric(),
                                                P.naturalIntegersGeometric(),
                                                P.positiveIntegersGeometric()
                                        )
                                ),
                                t -> P.pairs(P.matrices(t.a, t.c), P.matrices(t.b, t.c))
                        )
                ),
                P.choose(
                        map(
                                p -> new Pair<>(zero(p.a, 0), zero(p.b, 0)),
                                P.pairs(P.naturalIntegersGeometric())
                        ),
                        map(
                                i -> {
                                    Matrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<Matrix, Matrix> p : take(LIMIT, ps)) {
            Matrix concatenated = p.a.concat(p.b);
            concatenated.validate();
            assertEquals(p, concatenated.height(), p.a.height() + p.b.height());
            assertEquals(p, concatenated.width(), p.a.width());
            assertEquals(p, concatenated, p.a.transpose().augment(p.b.transpose()).transpose());
        }

        for (Matrix m : take(LIMIT, P.matrices())) {
            assertEquals(m, m, m.concat(zero(0, m.width())));
            assertEquals(m, m, zero(0, m.width()).concat(m));
        }

        Iterable<Pair<Matrix, Matrix>> psFail = filterInfinite(q -> q.a.width() != q.b.width(), P.pairs(P.matrices()));
        for (Pair<Matrix, Matrix> p : take(LIMIT, psFail)) {
            try {
                p.a.concat(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAugment() {
        initialize("augment(Matrix)");
        Iterable<Pair<Matrix, Matrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                filterInfinite(
                                        t -> t.b != 0 || t.c != 0,
                                        P.triples(
                                                P.positiveIntegersGeometric(),
                                                P.naturalIntegersGeometric(),
                                                P.naturalIntegersGeometric()
                                        )
                                ),
                                t -> P.pairs(P.matrices(t.a, t.b), P.matrices(t.a, t.c))
                        )
                ),
                P.choose(
                        map(p -> new Pair<>(zero(0, p.a), zero(0, p.b)), P.pairs(P.naturalIntegersGeometric())),
                        map(
                                i -> {
                                    Matrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<Matrix, Matrix> p : take(LIMIT, ps)) {
            Matrix augmented = p.a.augment(p.b);
            augmented.validate();
            assertEquals(p, augmented.height(), p.a.height());
            assertEquals(p, augmented.width(), p.a.width() + p.b.width());
            assertEquals(p, augmented, p.a.transpose().concat(p.b.transpose()).transpose());
        }

        for (Matrix m : take(LIMIT, P.matrices())) {
            assertEquals(m, m, m.augment(zero(m.height(), 0)));
            assertEquals(m, m, zero(m.height(), 0).augment(m));
        }

        Iterable<Pair<Matrix, Matrix>> psFail = filterInfinite(
                q -> q.a.height() != q.b.height(),
                P.pairs(P.matrices())
        );
        for (Pair<Matrix, Matrix> p : take(LIMIT, psFail)) {
            try {
                p.a.augment(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAdd() {
        initialize("add(Matrix)");
        Iterable<Pair<Matrix, Matrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.matrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    Matrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    Matrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<Matrix, Matrix> p : take(LIMIT, ps)) {
            Matrix sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum.height(), p.a.height());
            assertEquals(p, sum.width(), p.a.width());
            commutative(Matrix::add, p);
            inverse(m -> m.add(p.b), (Matrix m) -> m.subtract(p.b), p.a);
            assertTrue(
                    p,
                    sum.maxElementBitLength() <= Ordering.max(p.a.maxElementBitLength(), p.b.maxElementBitLength()) + 1
            );
        }

        for (Matrix m : take(LIMIT, P.matrices())) {
            fixedPoint(n -> zero(n.height(), n.width()).add(n), m);
            fixedPoint(n -> n.add(zero(n.height(), n.width())), m);
            assertTrue(m, m.add(m.negate()).isZero());
        }

        Iterable<Triple<Matrix, Matrix, Matrix>> ts = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.triples(P.matrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    Matrix m = zero(0, i);
                                    return new Triple<>(m, m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    Matrix m = zero(i, 0);
                                    return new Triple<>(m, m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Triple<Matrix, Matrix, Matrix> t : take(LIMIT, ts)) {
            associative(Matrix::add, t);
        }

        Iterable<Pair<Matrix, Matrix>> psFail = filterInfinite(
                p -> p.a.height() != p.b.height() || p.a.width() != p.b.width(),
                P.pairs(P.matrices())
        );
        for (Pair<Matrix, Matrix> p : take(LIMIT, psFail)) {
            try {
                p.a.add(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            Matrix negative = m.negate();
            negative.validate();
            assertEquals(m, m.height(), negative.height());
            assertEquals(m, m.width(), negative.width());
            involution(Matrix::negate, m);
            assertTrue(m, m.add(negative).isZero());
        }

        for (Matrix m : take(LIMIT, filterInfinite(n -> !n.isZero(), P.matrices()))) {
            assertNotEquals(m, m, m.negate());
        }
    }

    private static @NotNull Matrix subtract_simplest(@NotNull Matrix a, @NotNull Matrix b) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(Matrix)");
        Iterable<Pair<Matrix, Matrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.matrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    Matrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    Matrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<Matrix, Matrix> p : take(LIMIT, ps)) {
            Matrix difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, difference, subtract_simplest(p.a, p.b));
            assertEquals(p, difference.height(), p.a.height());
            assertEquals(p, difference.width(), p.a.width());
            antiCommutative(Matrix::subtract, Matrix::negate, p);
            inverse(m -> m.subtract(p.b), (Matrix m) -> m.add(p.b), p.a);
        }

        for (Matrix m : take(LIMIT, P.matrices())) {
            assertEquals(m, zero(m.height(), m.width()).subtract(m), m.negate());
            fixedPoint(n -> n.subtract(zero(n.height(), n.width())), m);
            assertTrue(m, m.subtract(m).isZero());
        }

        Iterable<Pair<Matrix, Matrix>> psFail = filterInfinite(
                p -> p.a.height() != p.b.height() || p.a.width() != p.b.width(),
                P.pairs(P.matrices())
        );
        for (Pair<Matrix, Matrix> p : take(LIMIT, psFail)) {
            try {
                p.a.subtract(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<Matrix, Matrix>, Matrix>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        Iterable<Pair<Matrix, Matrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.matrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    Matrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    Matrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        compareImplementations("subtract(Matrix)", take(LIMIT, ps), functions);
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        for (Pair<Matrix, BigInteger> p : take(LIMIT, P.pairs(P.matrices(), P.bigIntegers()))) {
            Matrix m = p.a.multiply(p.b);
            m.validate();
            assertEquals(p, m.height(), p.a.height());
            assertEquals(p, m.width(), p.a.width());
        }

        for (Matrix m : take(LIMIT, P.matrices())) {
            fixedPoint(n -> n.multiply(BigInteger.ONE), m);
            assertTrue(m, m.multiply(BigInteger.ZERO).isZero());
        }

        Iterable<Triple<Integer, Integer, BigInteger>> ts = map(
                p -> new Triple<>(p.b.a, p.b.b, p.a),
                P.pairsSquareRootOrder(P.bigIntegers(), P.pairs(P.naturalIntegersGeometric()))
        );
        for (Triple<Integer, Integer, BigInteger> t : take(LIMIT, ts)) {
            assertTrue(t, zero(t.a, t.b).multiply(t.c).isZero());
        }
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        for (Pair<Matrix, Integer> p : take(LIMIT, P.pairs(P.matrices(), P.integers()))) {
            Matrix m = p.a.multiply(p.b);
            m.validate();
            assertEquals(p, m.height(), p.a.height());
            assertEquals(p, m.width(), p.a.width());
        }

        for (Matrix m : take(LIMIT, P.matrices())) {
            fixedPoint(n -> n.multiply(1), m);
            assertTrue(m, m.multiply(0).isZero());
        }

        Iterable<Triple<Integer, Integer, Integer>> ts = map(
                p -> new Triple<>(p.b.a, p.b.b, p.a),
                P.pairsSquareRootOrder(P.integers(), P.pairs(P.naturalIntegersGeometric()))
        );
        for (Triple<Integer, Integer, Integer> t : take(LIMIT, ts)) {
            assertTrue(t, zero(t.a, t.b).multiply(t.c).isZero());
        }
    }

    private void propertiesMultiply_Vector() {
        initialize("multiply(Vector)");
        Iterable<Pair<Matrix, Vector>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.matrices(p.a, p.b), P.vectors(p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> new Pair<>(zero(i, 0), Vector.ZERO_DIMENSIONAL),
                                P.naturalIntegersGeometric()
                        ),
                        map(v -> new Pair<>(zero(0, v.dimension()), v), P.vectorsAtLeast(1))
                )
        );
        for (Pair<Matrix, Vector> p : take(LIMIT, ps)) {
            Vector v = p.a.multiply(p.b);
            assertEquals(p, v.dimension(), p.a.height());
        }

        Iterable<Pair<Vector, Vector>> ps2 = P.withElement(
                new Pair<>(Vector.ZERO_DIMENSIONAL, Vector.ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.vectors(i))
                        )
                )
        );
        for (Pair<Vector, Vector> p : take(LIMIT, ps2)) {
            BigInteger i = fromRows(Collections.singletonList(p.a)).multiply(p.b).get(0);
            assertEquals(p, i, p.a.dot(p.b));
        }

        for (Matrix m : take(LIMIT, P.matrices())) {
            assertEquals(m, m.multiply(Vector.zero(m.width())), Vector.zero(m.height()));
        }

        for (Vector v : take(LIMIT, P.vectors())) {
            assertEquals(v, identity(v.dimension()).multiply(v), v);
        }

        ps = map(q -> new Pair<>(zero(q.a, q.b.dimension()), q.b), P.pairs(P.naturalIntegersGeometric(), P.vectors()));
        for (Pair<Matrix, Vector> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b), Vector.zero(p.a.height()));
        }

        Iterable<Pair<Matrix, Vector>> psFail = filterInfinite(
                p -> p.a.width() != p.b.dimension(),
                P.pairs(P.matrices(), P.vectors())
        );
        for (Pair<Matrix, Vector> p : take(LIMIT, psFail)) {
            try {
                p.a.multiply(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull Matrix multiply_Matrix_alt(@NotNull Matrix a, @NotNull Matrix b) {
        if (a.width() != b.height()) {
            throw new ArithmeticException("the width of this must equal the height of that. this: " + a + ", that: " +
                    b);
        }
        if (b.width() == 0) {
            return zero(a.height(), 0);
        } else {
            return fromColumns(toList(map(a::multiply, b.columns())));
        }
    }

    private void propertiesMultiply_Matrix() {
        initialize("multiply(Matrix)");
        Iterable<Pair<Matrix, Matrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.triples(P.positiveIntegersGeometric()),
                                t -> P.pairs(P.withScale(4).matrices(t.a, t.b), P.withScale(4).matrices(t.b, t.c))
                        )
                ),
                P.choose(
                    P.choose(
                            map(
                                    m -> new Pair<>(m, zero(m.width(), 0)),
                                    filterInfinite(
                                            m -> m.height() != 0 && m.width() != 0,
                                            P.withScale(4).matrices()
                                    )
                            ),
                            map(
                                    m -> new Pair<>(zero(0, m.height()), m),
                                    filterInfinite(
                                            m -> m.height() != 0 && m.width() != 0,
                                            P.withScale(4).matrices()
                                    )
                            )
                    ),
                    map(
                            p -> new Pair<>(zero(p.a, 0), zero(0, p.b)),
                            P.pairs(P.positiveIntegersGeometric())
                    )
                )
        );
        for (Pair<Matrix, Matrix> p : take(LIMIT, ps)) {
            Matrix product = p.a.multiply(p.b);
            assertEquals(p, multiply_Matrix_alt(p.a, p.b), product);
            assertEquals(p, product.height(), p.a.height());
            assertEquals(p, product.width(), p.b.width());
            assertTrue(
                    p,
                    p.a.multiply(p.b).maxElementBitLength() <=
                            p.a.maxElementBitLength() + p.b.maxElementBitLength() +
                            BigInteger.valueOf(p.a.width()).bitLength()
            );
        }

        Iterable<Pair<Vector, Vector>> ps2 = P.withElement(
                new Pair<>(Vector.ZERO_DIMENSIONAL, Vector.ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.vectors(i))
                        )
                )
        );
        for (Pair<Vector, Vector> p : take(LIMIT, ps2)) {
            BigInteger i = fromRows(Collections.singletonList(p.a))
                    .multiply(fromColumns(Collections.singletonList(p.b))).get(0, 0);
            assertEquals(p, i, p.a.dot(p.b));
        }

        Iterable<Pair<Matrix, Vector>> ps3 = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.matrices(p.a, p.b), P.vectors(p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> new Pair<>(zero(i, 0), Vector.ZERO_DIMENSIONAL),
                                P.naturalIntegersGeometric()
                        ),
                        map(v -> new Pair<>(zero(0, v.dimension()), v), P.vectorsAtLeast(1))
                )
        );
        for (Pair<Matrix, Vector> p : take(LIMIT, ps3)) {
            assertEquals(p, p.a.multiply(p.b), p.a.multiply(fromColumns(Collections.singletonList(p.b))).column(0));
        }

        Iterable<Pair<Matrix, Integer>> ps4 = P.pairs(P.matrices(), P.naturalIntegersGeometric());
        for (Pair<Matrix, Integer> p : take(LIMIT, ps4)) {
            assertEquals(p, p.a.multiply(zero(p.a.width(), p.b)), zero(p.a.height(), p.b));
            assertEquals(p, zero(p.b, p.a.height()).multiply(p.a), zero(p.b, p.a.width()));
        }

        for (Matrix m : take(LIMIT, P.squareMatrices())) {
            assertEquals(m, m.multiply(identity(m.width())), m);
            assertEquals(m, identity(m.width()).multiply(m), m);
        }

        Iterable<Pair<Matrix, Matrix>> psFail = filterInfinite(
                p -> p.a.width() != p.b.height(),
                P.pairs(P.matrices())
        );
        for (Pair<Matrix, Matrix> p : take(LIMIT, psFail)) {
            try {
                p.a.multiply(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsMultiply_Matrix() {
        Map<String, Function<Pair<Matrix, Matrix>, Matrix>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> multiply_Matrix_alt(p.a, p.b));
        functions.put("standard", p -> p.a.multiply(p.b));
        Iterable<Pair<Matrix, Matrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.triples(P.positiveIntegersGeometric()),
                                t -> P.pairs(P.withScale(4).matrices(t.a, t.b), P.withScale(4).matrices(t.b, t.c))
                        )
                ),
                P.choose(
                        P.choose(
                                map(
                                        m -> new Pair<>(m, zero(m.width(), 0)),
                                        filterInfinite(
                                                m -> m.height() != 0 && m.width() != 0,
                                                P.withScale(4).matrices()
                                        )
                                ),
                                map(
                                        m -> new Pair<>(zero(0, m.height()), m),
                                        filterInfinite(
                                                m -> m.height() != 0 && m.width() != 0,
                                                P.withScale(4).matrices()
                                        )
                                )
                        ),
                        map(
                                p -> new Pair<>(zero(p.a, 0), zero(0, p.b)),
                                P.pairs(P.positiveIntegersGeometric())
                        )
                )
        );
        compareImplementations("multiply(Matrix)", take(LIMIT, ps), functions);
    }

    private static @NotNull Matrix shiftLeft_simplest(@NotNull Matrix m, int bits) {
        if (bits < 0) {
            throw new ArithmeticException("bits cannot be negative. Invalid bits: " + bits);
        }
        return m.multiply(BigInteger.ONE.shiftLeft(bits));
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        for (Pair<Matrix, Integer> p : take(LIMIT, P.pairs(P.matrices(), P.naturalIntegersGeometric()))) {
            Matrix shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            assertEquals(p, p.a.height(), shifted.height());
            assertEquals(p, p.a.width(), shifted.width());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
            homomorphic(Matrix::negate, Function.identity(), Matrix::negate, Matrix::shiftLeft, Matrix::shiftLeft, p);
        }

        for (Matrix m : take(LIMIT, P.matrices())) {
            fixedPoint(n -> n.shiftLeft(0), m);
        }

        for (Pair<Matrix, Integer> p : take(LIMIT, P.pairs(P.matrices(), P.negativeIntegersGeometric()))) {
            try {
                p.a.shiftLeft(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<Matrix, Integer>, Matrix>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        Iterable<Pair<Matrix, Integer>> ps = P.pairs(P.matrices(), P.naturalIntegersGeometric());
        compareImplementations("shiftLeft(int)", take(LIMIT, ps), functions);
    }

    private void propertiesIsInRowEchelonForm() {
        initialize("isInRowEchelonForm()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            assertEquals(m, m.isInRowEchelonForm(), m.equals(m.rowEchelonForm()));
        }

        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            Matrix zero = zero(p.a, p.b);
            assertTrue(p, zero.isInRowEchelonForm());
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Matrix identity = identity(i);
            assertTrue(i, identity.isInRowEchelonForm());
        }
    }

    private void propertiesRowEchelonForm() {
        initialize("rowEchelonForm()");
        Function<Matrix, Matrix> toZeroOne = m -> {
            if (m.width() == 0 || m.height() == 0) return m;
            return fromRows(
                    toList(
                            map(
                                    r -> Vector.of(
                                            toList(
                                                    map(
                                                            x -> x.equals(BigInteger.ZERO) ?
                                                                    BigInteger.ZERO :
                                                                    BigInteger.ONE,
                                                            r
                                                    )
                                            )
                                    ),
                                    m.rows()
                            )
                    )
            );
        };
        for (Matrix m : take(LIMIT, P.matrices())) {
            Matrix ref = m.rowEchelonForm();
            ref.validate();
            assertEquals(m, toZeroOne.apply(ref), toZeroOne.apply(m.primitiveRowEchelonForm()));
            assertTrue(m, ref.isInRowEchelonForm());
            idempotent(Matrix::rowEchelonForm, m);
        }
    }

    private static @NotNull Matrix primitiveRowEchelonForm_simplest(@NotNull Matrix m) {
        Matrix ref = m.rowEchelonForm();
        List<Vector> refRows = toList(ref.rows());
        boolean changed = false;
        for (int i = 0; i < m.height(); i++) {
            Vector row = refRows.get(i);
            Vector primitiveRow = row.makePrimitive();
            if (row != primitiveRow) {
                if (!changed) {
                    changed = true;
                }
                refRows.set(i, primitiveRow);
            }
        }
        return changed ? fromRows(refRows) : ref;
    }

    private void propertiesPrimitiveRowEchelonForm() {
        initialize("primitiveRowEchelonForm()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            Matrix ref = m.primitiveRowEchelonForm();
            ref.validate();
            assertEquals(m, ref, primitiveRowEchelonForm_simplest(m));
            assertTrue(m, ref.isInRowEchelonForm());
            assertTrue(m, all(Vector::isPrimitive, ref.rows()));
            idempotent(Matrix::primitiveRowEchelonForm, m);
        }
    }

    private void compareImplementationsPrimitiveRowEchelonForm() {
        Map<String, Function<Matrix, Matrix>> functions = new LinkedHashMap<>();
        functions.put("simplest", MatrixProperties::primitiveRowEchelonForm_simplest);
        functions.put("standard", Matrix::primitiveRowEchelonForm);
        compareImplementations("primitiveRowEchelonForm()", take(LIMIT, P.matrices()), functions);
    }

    private static int rank_alt(@NotNull Matrix m) {
        Matrix ref = m.primitiveRowEchelonForm();
        for (int rank = m.height(); rank > 0; rank--) {
            if (!ref.row(rank - 1).isZero()) {
                return rank;
            }
        }
        return 0;
    }

    private static int rank_alt2(@NotNull Matrix m) {
        RationalMatrix ref = m.toRationalMatrix().rowEchelonForm();
        for (int rank = m.height(); rank > 0; rank--) {
            if (!ref.row(rank - 1).isZero()) {
                return rank;
            }
        }
        return 0;
    }

    private void propertiesRank() {
        initialize("rank()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            int rank = m.rank();
            assertEquals(m, rank_alt(m), rank);
            assertEquals(m, rank_alt2(m), rank);
            assertTrue(m, rank >= 0);
            assertTrue(m, rank <= m.height());
            assertTrue(m, rank <= m.width());
            assertEquals(m, m.transpose().rank(), rank);
        }

        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            Matrix zero = zero(p.a, p.b);
            assertEquals(p, zero.rank(), 0);
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Matrix identity = identity(i);
            assertEquals(i, identity.rank(), i);
        }
    }

    private void compareImplementationsRank() {
        Map<String, Function<Matrix, Integer>> functions = new LinkedHashMap<>();
        functions.put("alt", MatrixProperties::rank_alt);
        functions.put("alt2", MatrixProperties::rank_alt2);
        functions.put("standard", Matrix::rank);
        compareImplementations("rank()", take(LIMIT, P.matrices()), functions);
    }

    private static boolean isInvertible_alt(@NotNull Matrix m) {
        if (!m.isSquare()) {
            throw new IllegalArgumentException("this must be square. Invalid this: " + m);
        }
        return m.toRationalMatrix().rank() == m.width();
    }

    private void propertiesIsInvertible() {
        initialize("isInvertible()");
        for (Matrix m : take(LIMIT, P.withScale(4).squareMatrices())) {
            boolean isInvertible = m.isInvertible();
            assertEquals(m, isInvertible_alt(m), isInvertible);
            assertEquals(m, m.transpose().isInvertible(), isInvertible);
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Matrix identity = identity(i);
            assertTrue(i, identity.isInvertible());
        }
    }

    private void compareImplementationsIsInvertible() {
        Map<String, Function<Matrix, Boolean>> functions = new LinkedHashMap<>();
        functions.put("alt", MatrixProperties::isInvertible_alt);
        functions.put("standard", Matrix::isInvertible);
        compareImplementations("isInvertible()", take(LIMIT, P.withScale(4).squareMatrices()), functions);
    }

    private void propertiesIsInReducedRowEchelonForm() {
        initialize("isInReducedRowEchelonForm()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            boolean isInRref = m.isInReducedRowEchelonForm();
            assertTrue(m, !m.equals(m.reducedRowEchelonForm()) || isInRref);
        }

        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            Matrix zero = zero(p.a, p.b);
            assertTrue(p, zero.isInReducedRowEchelonForm());
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Matrix identity = identity(i);
            assertTrue(i, identity.isInReducedRowEchelonForm());
        }
    }

    private void propertiesReducedRowEchelonForm() {
        initialize("reducedRowEchelonForm()");
        Function<Matrix, Matrix> toZeroOne = m -> {
            if (m.width() == 0 || m.height() == 0) return m;
            return fromRows(
                    toList(
                            map(
                                    r -> Vector.of(
                                            toList(
                                                    map(
                                                            x -> x.equals(BigInteger.ZERO) ?
                                                                    BigInteger.ZERO :
                                                                    BigInteger.ONE,
                                                            r
                                                    )
                                            )
                                    ),
                                    m.rows()
                            )
                    )
            );
        };
        for (Matrix m : take(LIMIT, P.matrices())) {
            Matrix rref = m.reducedRowEchelonForm();
            rref.validate();
            assertEquals(m, toZeroOne.apply(rref), toZeroOne.apply(m.primitiveReducedRowEchelonForm()));
            assertTrue(m, rref.isInRowEchelonForm());
            assertTrue(m, rref.isInReducedRowEchelonForm());
            idempotent(Matrix::reducedRowEchelonForm, m);
        }
    }

    private static @NotNull Matrix primitiveReducedRowEchelonForm_simplest(@NotNull Matrix m) {
        Matrix ref = m.reducedRowEchelonForm();
        List<Vector> rrefRows = toList(ref.rows());
        boolean changed = false;
        for (int i = 0; i < m.height(); i++) {
            Vector row = rrefRows.get(i);
            Vector primitiveRow = row.makePrimitive();
            if (row != primitiveRow) {
                if (!changed) {
                    changed = true;
                }
                rrefRows.set(i, primitiveRow);
            }
        }
        return changed ? fromRows(rrefRows) : ref;
    }

    private void propertiesPrimitiveReducedRowEchelonForm() {
        initialize("primitiveReducedRowEchelonForm()");
        for (Matrix m : take(LIMIT, P.matrices())) {
            Matrix rref = m.primitiveReducedRowEchelonForm();
            rref.validate();
            assertEquals(m, rref, primitiveReducedRowEchelonForm_simplest(m));
            assertTrue(m, rref.isInReducedRowEchelonForm());
            assertTrue(m, all(Vector::isPrimitive, rref.rows()));
            idempotent(Matrix::primitiveReducedRowEchelonForm, m);
        }
    }

    private void compareImplementationsPrimitiveReducedRowEchelonForm() {
        Map<String, Function<Matrix, Matrix>> functions = new LinkedHashMap<>();
        functions.put("simplest", MatrixProperties::primitiveReducedRowEchelonForm_simplest);
        functions.put("standard", Matrix::primitiveReducedRowEchelonForm);
        compareImplementations("primitiveReducedRowEchelonForm()", take(LIMIT, P.matrices()), functions);
    }

    private static @NotNull Optional<RationalVector> solveLinearSystem_simplest(@NotNull Matrix m, @NotNull Vector v) {
        return m.toRationalMatrix().solveLinearSystem(v.toRationalVector());
    }

    private static @NotNull Optional<RationalVector> solveLinearSystem_alt(@NotNull Matrix m, @NotNull Vector v) {
        if (m.height() != v.dimension()) {
            throw new IllegalArgumentException("The dimension of v must equal the height of m. v: " + v + ", m: " + m);
        }
        if (m.width() > m.height()) return Optional.empty();
        Matrix rref = m.augment(fromColumns(Collections.singletonList(v))).primitiveReducedRowEchelonForm();
        Matrix bottom = rref.submatrix(toList(range(m.width(), m.height() - 1)), toList(range(0, m.width())));
        if (!bottom.isZero()) return Optional.empty();
        List<Rational> result = new ArrayList<>();
        int lastColumnIndex = rref.width() - 1;
        for (int i = 0; i < m.width(); i++) {
            Vector row = rref.row(i);
            for (int j = 0; j < m.width(); j++) {
                if ((i == j) == row.get(j).equals(BigInteger.ZERO)) return Optional.empty();
            }
            result.add(Rational.of(row.get(lastColumnIndex), row.get(i)));
        }
        return Optional.of(RationalVector.of(result));
    }

    private void propertiesSolveLinearSystem() {
        initialize("solveLinearSystem(Vector)");
        Iterable<Pair<Matrix, Vector>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(P.withScale(4).matrices(p.a, p.b), P.withScale(4).vectors(p.a))
                        )
                ),
                P.choose(
                        map(
                                i -> new Pair<>(zero(0, i), Vector.ZERO_DIMENSIONAL),
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(v -> new Pair<>(zero(v.dimension(), 0), v), P.withScale(4).vectorsAtLeast(1))
                )
        );
        for (Pair<Matrix, Vector> p : take(LIMIT, ps)) {
            Optional<RationalVector> solution = p.a.solveLinearSystem(p.b);
            assertEquals(p, solveLinearSystem_simplest(p.a, p.b), solution);
            assertEquals(p, solveLinearSystem_alt(p.a, p.b), solution);
            if (solution.isPresent()) {
                assertTrue(p, p.a.height() >= p.a.width());
                RationalVector v = solution.get();
                assertEquals(p, p.a.toRationalMatrix().multiply(v).toVector(), p.b);
            }
        }

        Iterable<Pair<Matrix, Vector>> psFail = filterInfinite(
                q -> q.b.dimension() != q.a.height(),
                P.pairs(P.matrices(), P.vectors())
        );
        for (Pair<Matrix, Vector> p : take(LIMIT, psFail)) {
            try {
                p.a.solveLinearSystem(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsSolveLinearSystem() {
        Map<String, Function<Pair<Matrix, Vector>, Optional<RationalVector>>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> solveLinearSystem_simplest(p.a, p.b));
        functions.put("alt", p -> solveLinearSystem_alt(p.a, p.b));
        functions.put("standard", p -> p.a.solveLinearSystem(p.b));
        Iterable<Pair<Matrix, Vector>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(P.withScale(4).matrices(p.a, p.b), P.withScale(4).vectors(p.a))
                        )
                ),
                P.choose(
                        map(
                                i -> new Pair<>(zero(0, i), Vector.ZERO_DIMENSIONAL),
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(v -> new Pair<>(zero(v.dimension(), 0), v), P.withScale(4).vectorsAtLeast(1))
                )
        );
        compareImplementations("solveLinearSystem(Vector)", take(LIMIT, ps), functions);
    }

    private static @NotNull Optional<RationalMatrix> invert_simplest(@NotNull Matrix m) {
        return m.toRationalMatrix().invert();
    }

    private static @NotNull Optional<RationalMatrix> invert_alt(@NotNull Matrix m) {
        if (!m.isSquare()) {
            throw new IllegalArgumentException("m must be square. Invalid m: " + m);
        }
        Matrix rref = m.augment(identity(m.width())).primitiveReducedRowEchelonForm();
        List<RationalVector> resultRows = new ArrayList<>();
        for (int i = 0; i < m.height(); i++) {
            Vector row = rref.row(i);
            for (int j = 0; j < m.width(); j++) {
                if ((i == j) == row.get(j).equals(BigInteger.ZERO)) return Optional.empty();
            }
            BigInteger denominator = row.get(i);
            List<Rational> resultRow = new ArrayList<>();
            for (int j = m.width(); j < 2 * m.width(); j++) {
                resultRow.add(Rational.of(row.get(j), denominator));
            }
            resultRows.add(RationalVector.of(resultRow));
        }
        return Optional.of(RationalMatrix.fromRows(resultRows));
    }

    private void propertiesInvert() {
        initialize("invert()");
        for (Matrix m : take(LIMIT, P.withScale(4).withSecondaryScale(4).squareMatrices())) {
            Optional<RationalMatrix> oInverse = m.invert();
            assertEquals(m, invert_simplest(m), oInverse);
            assertEquals(m, invert_alt(m), oInverse);
            assertEquals(m, oInverse.isPresent(), m.isInvertible());
            if (oInverse.isPresent()) {
                RationalMatrix inverse = oInverse.get();
                assertTrue(m, m.toRationalMatrix().multiply(inverse).isIdentity());
                assertTrue(m, inverse.multiply(m.toRationalMatrix()).isIdentity());
                assertEquals(m, inverse.invert().get().toMatrix(), m);
            }
        }

        for (int i : take(SMALL_LIMIT, P.positiveIntegersGeometric())) {
            Matrix zero = zero(i, i);
            assertFalse(i, zero.invert().isPresent());
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Matrix identity = identity(i);
            idempotent(m -> m.invert().get().toMatrix(), identity);
        }
    }

    private void compareImplementationsInvert() {
        Map<String, Function<Matrix, Optional<RationalMatrix>>> functions = new LinkedHashMap<>();
        functions.put("simplest", MatrixProperties::invert_simplest);
        functions.put("alt", MatrixProperties::invert_alt);
        functions.put("standard", Matrix::invert);
        Iterable<Matrix> ms = P.withScale(4).withSecondaryScale(4).squareMatrices();
        compareImplementations("invert()", take(LIMIT, ms), functions);
    }

    private static @NotNull BigInteger determinant_simplest(@NotNull Matrix m) {
        return m.toRationalMatrix().determinant().bigIntegerValueExact();
    }

    private static @NotNull BigInteger determinant_Laplace(@NotNull Matrix m) {
        if (m.width() == 0) return BigInteger.ONE;
        if (m.width() == 1) return m.get(0, 0);
        BigInteger determinant = BigInteger.ZERO;
        Vector firstRow = m.row(0);
        List<Integer> rowIndices = toList(range(1, m.width() - 1));
        boolean sign = true;
        for (int i = 0; i < m.width(); i++) {
            BigInteger factor = firstRow.get(i);
            if (!sign) factor = factor.negate();
            sign = !sign;
            if (factor.equals(BigInteger.ZERO)) continue;
            BigInteger minor = m.submatrix(
                    rowIndices,
                    toList(concat(range(0, i - 1), range(i + 1, m.width() - 1)))
            ).determinant();
            determinant = determinant.add(factor.multiply(minor));
        }
        return determinant;
    }

    private void propertiesDeterminant() {
        initialize("determinant()");
        for (Matrix m : take(LIMIT, P.withScale(4).withSecondaryScale(4).squareMatrices())) {
            BigInteger determinant = m.determinant();
            assertEquals(m, determinant, determinant_simplest(m));
            assertEquals(m, determinant, determinant_Laplace(m));
            assertEquals(m, determinant, m.transpose().determinant());
            assertNotEquals(m, determinant.equals(BigInteger.ZERO), m.isInvertible());
            if (!determinant.equals(BigInteger.ZERO)) {
                assertEquals(m, m.invert().get().determinant(), Rational.of(determinant).invert());
            }
        }

        Iterable<Pair<Matrix, Pair<Integer, Integer>>> ps = P.dependentPairs(
                filterInfinite(m -> m.width() > 1, P.withScale(4).squareMatrices()),
                m -> P.subsetPairs(P.range(0, m.height() - 1))
        );
        for (Pair<Matrix, Pair<Integer, Integer>> p : take(LIMIT, ps)) {
            List<Vector> rows = toList(p.a.rows());
            Collections.swap(rows, p.b.a, p.b.b);
            Matrix swapped = fromRows(rows);
            assertEquals(p, swapped.determinant(), p.a.determinant().negate());
        }

        Iterable<Pair<Pair<Matrix, BigInteger>, Integer>> ps2 = P.dependentPairs(
                P.pairs(
                        filterInfinite(m -> m.width() > 0, P.withScale(4).squareMatrices()),
                        P.withScale(4).bigIntegers()
                ),
                p -> P.range(0, p.a.height() - 1)
        );
        for (Pair<Pair<Matrix, BigInteger>, Integer> p : take(LIMIT, ps2)) {
            List<Vector> rows = toList(p.a.a.rows());
            rows.set(p.b, rows.get(p.b).multiply(p.a.b));
            Matrix rowScaled = fromRows(rows);
            assertEquals(p, rowScaled.determinant(), p.a.a.determinant().multiply(p.a.b));
        }

        for (int i : take(SMALL_LIMIT, P.positiveIntegersGeometric())) {
            Matrix zero = zero(i, i);
            assertEquals(i, zero.determinant(), BigInteger.ZERO);
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Matrix identity = identity(i);
            assertEquals(i, identity.determinant(), BigInteger.ONE);
        }
    }

    private void compareImplementationsDeterminant() {
        Map<String, Function<Matrix, BigInteger>> functions = new LinkedHashMap<>();
        functions.put("simplest", MatrixProperties::determinant_simplest);
        functions.put("Laplace", MatrixProperties::determinant_Laplace);
        functions.put("standard", Matrix::determinant);
        compareImplementations("determinant()", take(LIMIT, P.withScale(4).squareMatrices()), functions);
    }

    private static @NotNull Polynomial characteristicPolynomial_simplest(@NotNull Matrix m) {
        return m.toRationalMatrix().characteristicPolynomial().toPolynomial();
    }

    private static @NotNull Polynomial characteristicPolynomial_alt(@NotNull Matrix a) {
        return PolynomialMatrix.identity(a.width()).multiply(Polynomial.X).subtract(PolynomialMatrix.of(a))
                .determinant();
    }

    private void propertiesCharacteristicPolynomial() {
        initialize("characteristicPolynomial()");
        for (Matrix m : take(LIMIT, P.withScale(4).squareMatrices())) {
            Polynomial p = m.characteristicPolynomial();
            assertEquals(m, characteristicPolynomial_simplest(m), p);
            assertEquals(m, characteristicPolynomial_alt(m), p);
            assertTrue(m, p.isMonic());
            BigInteger det = m.determinant();
            assertEquals(m, p.coefficient(0), m.height() % 2 == 0 ? det : det.negate());
            if (m.height() > 0) {
                assertEquals(m, p.coefficient(m.height() - 1), m.trace().negate());
            }
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Matrix zero = zero(i, i);
            assertEquals(i, zero.characteristicPolynomial(), Polynomial.of(BigInteger.ONE, i));
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Matrix identity = identity(i);
            assertEquals(
                    i,
                    identity.characteristicPolynomial(),
                    Polynomial.of(Arrays.asList(IntegerUtils.NEGATIVE_ONE, BigInteger.ONE)).pow(i)
            );
        }
    }

    private void compareImplementationsCharacteristicPolynomial() {
        Map<String, Function<Matrix, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", MatrixProperties::characteristicPolynomial_simplest);
        functions.put("alt", MatrixProperties::characteristicPolynomial_alt);
        functions.put("standard", Matrix::characteristicPolynomial);
        compareImplementations("characteristicPolynomial()", take(LIMIT, P.withScale(4).squareMatrices()), functions);
    }

    private void propertiesKroneckerMultiply() {
        initialize("kroneckerMultiply(Matrix)");
        for (Pair<Matrix, Matrix> p : take(LIMIT, P.pairs(P.matrices()))) {
            Matrix product = p.a.kroneckerMultiply(p.b);
            assertEquals(p, product.height(), p.a.height() * p.b.height());
            assertEquals(p, product.width(), p.a.width() * p.b.width());
        }

        Iterable<Triple<Matrix, Matrix, BigInteger>> ts = P.triples(P.matrices(), P.matrices(), P.bigIntegers());
        for (Triple<Matrix, Matrix, BigInteger> t : take(LIMIT, ts)) {
            Matrix product = t.a.multiply(t.c).kroneckerMultiply(t.b);
            assertEquals(t, product, t.a.kroneckerMultiply(t.b.multiply(t.c)));
            assertEquals(t, product, t.a.kroneckerMultiply(t.b).multiply(t.c));
        }

        for (Triple<Matrix, Matrix, Matrix> t : take(LIMIT, P.triples(P.matrices()))) {
            associative(Matrix::kroneckerMultiply, t);
        }

        Iterable<Pair<Matrix, Matrix>> psAdd = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.matrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    Matrix m = Matrix.zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    Matrix m = Matrix.zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        Iterable<Triple<Matrix, Matrix, Matrix>> ts2 = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairs(psAdd, P.matrices())
        );
        for (Triple<Matrix, Matrix, Matrix> t : take(LIMIT, ts2)) {
            leftDistributive(Matrix::add, Matrix::kroneckerMultiply, t);
            rightDistributive(Matrix::add, Matrix::kroneckerMultiply, t);
        }

        Iterable<Pair<Matrix, Matrix>> psMult = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.triples(P.withScale(4).positiveIntegersGeometric()),
                                t -> P.pairs(
                                        P.withScale(4).withSecondaryScale(4).matrices(t.a, t.b),
                                        P.withScale(4).withSecondaryScale(4).matrices(t.b, t.c)
                                )
                        )
                ),
                P.choose(
                    P.choose(
                            map(
                                    m -> new Pair<>(m, zero(m.width(), 0)),
                                    filterInfinite(
                                            m -> m.height() != 0 && m.width() != 0,
                                            P.withScale(4).withSecondaryScale(4).matrices()
                                    )
                            ),
                            map(
                                    m -> new Pair<>(zero(0, m.height()), m),
                                    filterInfinite(
                                            m -> m.height() != 0 && m.width() != 0,
                                            P.withScale(4).withSecondaryScale(4).matrices()
                                    )
                            )
                    ),
                    map(
                            p -> new Pair<>(zero(p.a, 0), zero(0, p.b)),
                            P.pairs(P.withScale(4).positiveIntegersGeometric())
                    )
                )
        );
        Iterable<Quadruple<Matrix, Matrix, Matrix, Matrix>> qs = map(
                p -> new Quadruple<>(p.a.a, p.b.a, p.a.b, p.b.b),
                P.pairs(psMult)
        );
        for (Quadruple<Matrix, Matrix, Matrix, Matrix> q : take(LIMIT, qs)) {
            assertEquals(
                    q,
                    q.a.kroneckerMultiply(q.b).multiply(q.c.kroneckerMultiply(q.d)),
                    q.a.multiply(q.c).kroneckerMultiply(q.b.multiply(q.d))
            );
        }

        for (Pair<Matrix, Matrix> p : take(LIMIT, P.pairs(P.matrices()))) {
            assertEquals(
                    p,
                    p.a.kroneckerMultiply(p.b).transpose(),
                    p.a.transpose().kroneckerMultiply(p.b.transpose())
            );
        }

        for (Pair<Matrix, Matrix> p : take(LIMIT, P.pairs(P.squareMatrices()))) {
            assertEquals(
                    p,
                    p.a.kroneckerMultiply(p.b).determinant(),
                    p.a.determinant().pow(p.b.width()).multiply(p.b.determinant().pow(p.a.width()))
            );
        }

        Iterable<Pair<Matrix, Matrix>> ps = P.pairs(P.withScale(2).withSecondaryScale(2).invertibleMatrices());
        for (Pair<Matrix, Matrix> p : take(LIMIT, ps)) {
            assertEquals(
                    p,
                    p.a.kroneckerMultiply(p.b).invert().get(),
                    p.a.invert().get().kroneckerMultiply(p.b.invert().get())
            );
        }

        for (Pair<Matrix, BigInteger> p : take(LIMIT, P.pairs(P.matrices(), P.bigIntegers()))) {
            assertEquals(
                    p,
                    p.a.kroneckerMultiply(fromRows(Collections.singletonList(Vector.of(p.b)))),
                    p.a.multiply(p.b)
            );
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            Matrix product = fromRows(Collections.singletonList(Vector.of(p.a)))
                    .kroneckerMultiply(fromRows(Collections.singletonList(Vector.of(p.b))));
            assertEquals(p, product.height(), 1);
            assertEquals(p, product.width(), 1);
            assertEquals(p, product.get(0, 0), p.a.multiply(p.b));
        }
    }

    private void propertiesKroneckerAdd() {
        initialize("kroneckerAdd(Matrix)");
        for (Pair<Matrix, Matrix> p : take(LIMIT, P.pairs(P.squareMatrices()))) {
            Matrix sum = p.a.kroneckerAdd(p.b);
            assertEquals(p, sum.height(), p.a.height() * p.b.height());
            assertEquals(p, sum.width(), p.a.width() * p.b.width());
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            Matrix sum = fromRows(Collections.singletonList(Vector.of(p.a)))
                    .kroneckerAdd(fromRows(Collections.singletonList(Vector.of(p.b))));
            assertEquals(p, sum.height(), 1);
            assertEquals(p, sum.width(), 1);
            assertEquals(p, sum.get(0, 0), p.a.add(p.b));
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::matrices);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::matrices);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(Matrix)");
        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::matrices);

        Iterable<Pair<Matrix, Matrix>> ps = filterInfinite(p -> p.a.height() != p.b.height(), P.pairs(P.matrices()));
        for (Pair<Matrix, Matrix> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.height(), p.b.height()));
        }

        ps = filterInfinite(p -> p.a.height() == p.b.height() && p.a.width() != p.b.width(), P.pairs(P.matrices()));
        for (Pair<Matrix, Matrix> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.width(), p.b.width()));
        }
    }

    private void propertiesReadStrict() {
        initialize("readStrict(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                MATRIX_CHARS,
                P.matrices(),
                Matrix::readStrict,
                Matrix::validate,
                false,
                true
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, MATRIX_CHARS, P.matrices(), Matrix::readStrict);
    }
}
