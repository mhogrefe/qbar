package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
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
        compareImplementationsRowEchelonForm();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead();
        propertiesFindIn();
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

    private void propertiesGet() {
        initialize("get(int, int)");
        Iterable<Triple<Matrix, Integer, Integer>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        filterInfinite(m -> m.height() > 0 && m.width() > 0, P.withScale(4).matrices()),
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
        for (int i : take(SMALL_LIMIT, P.positiveIntegersGeometric())) {
            Matrix identity = identity(i);
            identity.validate();
            assertTrue(i, identity.isSquare());
            assertTrue(i, identity.isIdentity());
            inverse(Matrix::identity, Matrix::height, i);
        }

        for (int i : take(LIMIT, P.rangeDown(0))) {
            try {
                identity(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesSubmatrix() {
        initialize("submatrix(List<Integer>, List<Integer>)");
        Iterable<Triple<Matrix, List<Integer>, List<Integer>>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.withScale(4).matrices(),
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
                        P.withScale(4).matrices(),
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
                        P.withScale(4).matrices(),
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

        for (int i : take(SMALL_LIMIT, P.positiveIntegersGeometric())) {
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
                                    Matrix m = Matrix.zero(0, i);
                                    return new Triple<>(m, m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    Matrix m = Matrix.zero(i, 0);
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

        for (Vector v : take(LIMIT, P.vectorsAtLeast(1))) {
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

        for (Matrix m : take(LIMIT, filterInfinite(n -> n.width() > 0, P.squareMatrices()))) {
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

        for (int i : take(SMALL_LIMIT, P.positiveIntegersGeometric())) {
            Matrix identity = identity(i);
            assertTrue(i, identity.isInRowEchelonForm());
        }
    }

    private static @NotNull Matrix rowEchelonForm_alt(@NotNull Matrix m) {
        int height = m.height();
        boolean changed = false;
        List<Vector> refRows = toList(m.rows());
        int i = 0;
        outer:
        for (int j = 0; i < height && j < m.width(); j++) {
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
                    refRows = toList(m.rows());
                }
                Collections.swap(refRows, i, nonzeroRowIndex);
            }
            Vector nonzeroRow = refRows.get(i);
            for (int k = i + 1; k < height; k++) {
                Vector row = refRows.get(k);
                if (!row.get(j).equals(BigInteger.ZERO)) {
                    if (!changed) {
                        changed = true;
                        refRows = toList(m.rows());
                    }
                    BigInteger leading = row.get(j);
                    BigInteger gcd = pivot.gcd(leading);
                    refRows.set(
                            k,
                            row.multiply(pivot.divide(gcd)).subtract(nonzeroRow.multiply(row.get(j).divide(gcd)))
                    );
                }
            }
            i++;
        }
        return changed ? fromRows(refRows) : m;
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
            assertEquals(m, toZeroOne.apply(ref), toZeroOne.apply(rowEchelonForm_alt(m)));
            assertTrue(m, ref.isInRowEchelonForm());
            idempotent(Matrix::rowEchelonForm, m);
        }
    }

    private void compareImplementationsRowEchelonForm() {
        Map<String, Function<Matrix, Matrix>> functions = new LinkedHashMap<>();
        functions.put("alt", MatrixProperties::rowEchelonForm_alt);
        functions.put("standard", Matrix::rowEchelonForm);
        compareImplementations("rowEchelonForm()", take(LIMIT, P.matrices()), functions);
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

    private void propertiesRead() {
        initialize("read(String)");
        QBarTesting.propertiesReadHelper(LIMIT, P, MATRIX_CHARS, P.matrices(), Matrix::read, Matrix::validate, false);
    }

    private void propertiesFindIn() {
        initialize("findIn(String)");
        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.matrices(),
                Matrix::read,
                Matrix::findIn,
                Matrix::validate
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, MATRIX_CHARS, P.matrices(), Matrix::read);
    }
}