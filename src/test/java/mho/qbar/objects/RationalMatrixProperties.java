package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static mho.qbar.objects.RationalMatrix.*;
import static mho.qbar.objects.RationalVector.ZERO_DIMENSIONAL;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.compare;
import static mho.wheels.testing.Testing.*;

public class RationalMatrixProperties extends QBarTestProperties {
    private static final @NotNull String RATIONAL_MATRIX_CHARS = " #,-/0123456789[]";

    public RationalMatrixProperties() {
        super("RationalMatrix");
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
        propertiesMultiply_Rational();
        propertiesMultiply_BigInteger();
        propertiesMultiply_int();
        propertiesDivide_Rational();
        propertiesDivide_BigInteger();
        propertiesDivide_int();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead();
        propertiesFindIn();
        propertiesToString();
    }

    private void propertiesRows() {
        initialize("rows()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            List<RationalVector> rows = toList(m.rows());
            assertTrue(m, all(v -> v.dimension() == m.width(), rows));
            assertEquals(m, rows, toList(m.transpose().columns()));
            testNoRemove(m.rows());
            testHasNext(m.rows());
        }

        for (RationalMatrix m : take(LIMIT, filterInfinite(n -> n.height() > 0, P.rationalMatrices()))) {
            inverse(n -> toList(n.rows()), RationalMatrix::fromRows, m);
        }
    }

    private void propertiesColumns() {
        initialize("columns()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            List<RationalVector> columns = toList(m.columns());
            assertTrue(m, all(v -> v.dimension() == m.height(), columns));
            assertEquals(m, columns, toList(m.transpose().rows()));
            testNoRemove(m.columns());
            testHasNext(m.columns());
        }

        for (RationalMatrix m : take(LIMIT, filterInfinite(n -> n.width() > 0, P.rationalMatrices()))) {
            inverse(n -> toList(n.columns()), RationalMatrix::fromColumns, m);
        }
    }

    private void propertiesRow() {
        initialize("row(int)");
        Iterable<Pair<RationalMatrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.height() > 0, P.rationalMatrices()),
                m -> P.uniformSample(toList(range(0, m.height() - 1)))
        );
        for (Pair<RationalMatrix, Integer> p : take(LIMIT, ps)) {
            RationalVector row = p.a.row(p.b);
            assertEquals(p, row, toList(p.a.rows()).get(p.b));
        }

        Iterable<Pair<RationalMatrix, Integer>> psFail = filterInfinite(
                p -> p.b < 0 || p.b >= p.a.height(),
                P.pairs(P.rationalMatrices(), P.integers())
        );
        for (Pair<RationalMatrix, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.row(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesColumn() {
        initialize("column(int)");
        Iterable<Pair<RationalMatrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.width() > 0, P.rationalMatrices()),
                m -> P.uniformSample(toList(range(0, m.width() - 1)))
        );
        for (Pair<RationalMatrix, Integer> p : take(LIMIT, ps)) {
            RationalVector column = p.a.column(p.b);
            assertEquals(p, column, toList(p.a.columns()).get(p.b));
        }

        Iterable<Pair<RationalMatrix, Integer>> psFail = filterInfinite(
                p -> p.b < 0 || p.b >= p.a.width(),
                P.pairs(P.rationalMatrices(), P.integers())
        );
        for (Pair<RationalMatrix, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.column(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesGet() {
        initialize("get(int, int)");
        Iterable<Triple<RationalMatrix, Integer, Integer>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        filterInfinite(m -> m.height() > 0 && m.width() > 0, P.withScale(4).rationalMatrices()),
                        m -> P.uniformSample(
                                toList(EP.pairsLex(range(0, m.height() - 1), toList(range(0, m.width() - 1))))
                        )
                )
        );
        for (Triple<RationalMatrix, Integer, Integer> t : take(LIMIT, ts)) {
            Rational element = t.a.get(t.b, t.c);
            assertEquals(t, element, t.a.row(t.b).get(t.c));
            assertEquals(t, element, t.a.column(t.c).get(t.b));
        }

        Iterable<Triple<RationalMatrix, Integer, Integer>> tsFail = filterInfinite(
                t -> t.b < 0 || t.b >= t.a.height() || t.c < 0 || t.c >= t.a.width(),
                P.triples(P.rationalMatrices(), P.integers(), P.integers())
        );
        for (Triple<RationalMatrix, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.get(t.b, t.c);
                fail(t);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesFromRows() {
        initialize("fromRows(List<RationalVector>)");
        Iterable<List<RationalVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.rationalVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        for (List<RationalVector> vs : take(LIMIT, vss)) {
            RationalMatrix m = fromRows(vs);
            m.validate();
            assertEquals(vs, m, fromColumns(vs).transpose());
            inverse(RationalMatrix::fromRows, n -> toList(n.rows()), vs);
            assertEquals(vs, m.height(), vs.size());
            if (vs.size() != 0) {
                assertEquals(vs, m.width(), head(vs).dimension());
            }
        }

        Iterable<List<RationalVector>> vssFail = filterInfinite(
                us -> !same(map(RationalVector::dimension, us)),
                P.listsAtLeast(1, P.rationalVectors())
        );
        for (List<RationalVector> vs : take(LIMIT, vssFail)) {
            try {
                fromRows(vs);
                fail(vs);
            } catch (IllegalArgumentException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.rationalVectors(i))
                )
        );
        for (List<RationalVector> vs : take(LIMIT, vssFail)) {
            try {
                fromRows(vs);
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesFromColumns() {
        initialize("fromColumns(List<RationalVector>)");
        Iterable<List<RationalVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.rationalVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        for (List<RationalVector> vs : take(LIMIT, vss)) {
            RationalMatrix m = fromColumns(vs);
            m.validate();
            assertEquals(vs, m, fromRows(vs).transpose());
            inverse(RationalMatrix::fromColumns, n -> toList(n.columns()), vs);
            assertEquals(vs, m.width(), vs.size());
            if (vs.size() != 0) {
                assertEquals(vs, m.height(), head(vs).dimension());
            }
        }

        Iterable<List<RationalVector>> vssFail = filterInfinite(
                us -> !same(map(RationalVector::dimension, us)),
                P.listsAtLeast(1, P.rationalVectors())
        );
        for (List<RationalVector> vs : take(LIMIT, vssFail)) {
            try {
                fromColumns(vs);
                fail(vs);
            } catch (IllegalArgumentException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.rationalVectors(i))
                )
        );
        for (List<RationalVector> vs : take(LIMIT, vssFail)) {
            try {
                fromColumns(vs);
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesHeight() {
        initialize("height()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            int height = m.height();
            assertTrue(m, height >= 0);
            assertEquals(m, height, length(m.rows()));
        }
    }

    private void propertiesWidth() {
        initialize("width()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            int width = m.width();
            assertTrue(m, width >= 0);
            assertEquals(m, width, length(m.columns()));
        }
    }

    private void propertiesIsSquare() {
        initialize("isSquare()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            m.isSquare();
        }
    }

    private void propertiesIsZero() {
        initialize("isZero()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            assertEquals(m, m.isZero(), zero(m.height(), m.width()).equals(m));
        }
    }

    private void propertiesZero() {
        initialize("zero(int, int)");
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            RationalMatrix zero = zero(p.a, p.b);
            zero.validate();
            assertTrue(p, zero.isZero());
            assertEquals(p, zero.height(), p.a);
            assertEquals(p, zero.width(), p.b);
            inverse(q -> zero(q.a, q.b), (RationalMatrix m) -> new Pair<>(m.height(), m.width()), p);
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
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            m.isIdentity();
        }
    }

    private void propertiesIdentity() {
        initialize("identity(int)");
        for (int i : take(SMALL_LIMIT, P.positiveIntegersGeometric())) {
            RationalMatrix identity = identity(i);
            identity.validate();
            assertTrue(i, identity.isSquare());
            assertTrue(i, identity.isIdentity());
            inverse(RationalMatrix::identity, RationalMatrix::height, i);
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
        Iterable<Triple<RationalMatrix, List<Integer>, List<Integer>>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.withScale(4).rationalMatrices(),
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
        for (Triple<RationalMatrix, List<Integer>, List<Integer>> t : take(LIMIT, ts)) {
            RationalMatrix submatrix = t.a.submatrix(t.b, t.c);
            submatrix.validate();
            assertEquals(t, submatrix.height(), t.b.size());
            assertEquals(t, submatrix.width(), t.c.size());
        }

        RationalMatrix zero = zero(0, 0);
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            assertEquals(m, m.submatrix(Collections.emptyList(), Collections.emptyList()), zero);
            assertEquals(m, m.submatrix(toList(range(0, m.height() - 1)), toList(range(0, m.width() - 1))), m);
        }

        Iterable<Triple<RationalMatrix, List<Integer>, List<Integer>>> tsFail = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.withScale(4).rationalMatrices(),
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
        for (Triple<RationalMatrix, List<Integer>, List<Integer>> t : take(LIMIT, tsFail)) {
            try {
                t.a.submatrix(t.b, t.c);
                fail(t);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }

        tsFail = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.withScale(4).rationalMatrices(),
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
        for (Triple<RationalMatrix, List<Integer>, List<Integer>> t : take(LIMIT, tsFail)) {
            try {
                t.a.submatrix(t.b, t.c);
                fail(t);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }
    }

    private static @NotNull RationalMatrix transpose_alt(@NotNull RationalMatrix m) {
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
                                    RationalVector::of,
                                    (Iterable<List<Rational>>) IterableUtils.transpose(
                                            map(r -> (Iterable<Rational>) r, m.rows())
                                    )
                            )
                    )
            );
        }
    }

    private void propertiesTranspose() {
        initialize("transpose()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            RationalMatrix transposed = m.transpose();
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
            involution(RationalMatrix::transpose, m);
        }

        for (int i : take(SMALL_LIMIT, P.positiveIntegersGeometric())) {
            RationalMatrix identity = identity(i);
            assertEquals(i, identity, identity.transpose());
        }
    }

    private void compareImplementationsTranspose() {
        Map<String, Function<RationalMatrix, RationalMatrix>> functions = new LinkedHashMap<>();
        functions.put("alt", RationalMatrixProperties::transpose_alt);
        functions.put("standard", RationalMatrix::transpose);
        compareImplementations("transpose()", take(LIMIT, P.rationalMatrices()), functions);
    }

    private void propertiesConcat() {
        initialize("concat(RationalMatrix)");
        Iterable<Pair<RationalMatrix, RationalMatrix>> ps = P.chooseLogarithmicOrder(
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
                                t -> P.pairs(
                                        P.rationalMatrices(t.a, t.c),
                                        P.rationalMatrices(t.b, t.c)
                                )
                        )
                ),
                P.choose(
                        map(
                                p -> new Pair<>(zero(p.a, 0), zero(p.b, 0)),
                                P.pairs(P.naturalIntegersGeometric())
                        ),
                        map(
                                i -> {
                                    RationalMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, ps)) {
            RationalMatrix concatenated = p.a.concat(p.b);
            concatenated.validate();
            assertEquals(p, concatenated.height(), p.a.height() + p.b.height());
            assertEquals(p, concatenated.width(), p.a.width());
            assertEquals(p, concatenated, p.a.transpose().augment(p.b.transpose()).transpose());
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            assertEquals(m, m, m.concat(zero(0, m.width())));
            assertEquals(m, m, zero(0, m.width()).concat(m));
        }

        Iterable<Pair<RationalMatrix, RationalMatrix>> psFail = filterInfinite(
                q -> q.a.width() != q.b.width(),
                P.pairs(P.rationalMatrices())
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, psFail)) {
            try {
                p.a.concat(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAugment() {
        initialize("augment(RationalMatrix)");
        Iterable<Pair<RationalMatrix, RationalMatrix>> ps = P.chooseLogarithmicOrder(
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
                                t -> P.pairs(P.rationalMatrices(t.a, t.b), P.rationalMatrices(t.a, t.c))
                        )
                ),
                P.choose(
                        map(p -> new Pair<>(zero(0, p.a), zero(0, p.b)), P.pairs(P.naturalIntegersGeometric())),
                        map(
                                i -> {
                                    RationalMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, ps)) {
            RationalMatrix augmented = p.a.augment(p.b);
            augmented.validate();
            assertEquals(p, augmented.height(), p.a.height());
            assertEquals(p, augmented.width(), p.a.width() + p.b.width());
            assertEquals(p, augmented, p.a.transpose().concat(p.b.transpose()).transpose());
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            assertEquals(m, m, m.augment(zero(m.height(), 0)));
            assertEquals(m, m, zero(m.height(), 0).augment(m));
        }

        Iterable<Pair<RationalMatrix, RationalMatrix>> psFail = filterInfinite(
                q -> q.a.height() != q.b.height(),
                P.pairs(P.rationalMatrices())
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, psFail)) {
            try {
                p.a.augment(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAdd() {
        initialize("add(RationalMatrix)");
        Iterable<Pair<RationalMatrix, RationalMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.rationalMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    RationalMatrix m = RationalMatrix.zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    RationalMatrix m = RationalMatrix.zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, ps)) {
            RationalMatrix sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum.height(), p.a.height());
            assertEquals(p, sum.width(), p.a.width());
            commutative(RationalMatrix::add, p);
            inverse(m -> m.add(p.b), (RationalMatrix m) -> m.subtract(p.b), p.a);
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            fixedPoint(n -> zero(n.height(), n.width()).add(n), m);
            fixedPoint(n -> n.add(zero(n.height(), n.width())), m);
            assertTrue(m, m.add(m.negate()).isZero());
        }

        Iterable<Triple<RationalMatrix, RationalMatrix, RationalMatrix>> ts = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.triples(P.rationalMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    RationalMatrix m = RationalMatrix.zero(0, i);
                                    return new Triple<>(m, m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    RationalMatrix m = RationalMatrix.zero(i, 0);
                                    return new Triple<>(m, m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Triple<RationalMatrix, RationalMatrix, RationalMatrix> t : take(LIMIT, ts)) {
            associative(RationalMatrix::add, t);
        }

        Iterable<Pair<RationalMatrix, RationalMatrix>> psFail = filterInfinite(
                p -> p.a.height() != p.b.height() || p.a.width() != p.b.width(),
                P.pairs(P.rationalMatrices())
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, psFail)) {
            try {
                p.a.add(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            RationalMatrix negative = m.negate();
            negative.validate();
            assertEquals(m, m.height(), negative.height());
            assertEquals(m, m.width(), negative.width());
            involution(RationalMatrix::negate, m);
            assertTrue(m, m.add(negative).isZero());
        }

        for (RationalMatrix m : take(LIMIT, filterInfinite(n -> !n.isZero(), P.rationalMatrices()))) {
            assertNotEquals(m, m, m.negate());
        }
    }

    private static @NotNull RationalMatrix subtract_simplest(@NotNull RationalMatrix a, @NotNull RationalMatrix b) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(RationalMatrix)");
        Iterable<Pair<RationalMatrix, RationalMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.rationalMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    RationalMatrix m = RationalMatrix.zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    RationalMatrix m = RationalMatrix.zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, ps)) {
            RationalMatrix difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, difference, subtract_simplest(p.a, p.b));
            assertEquals(p, difference.height(), p.a.height());
            assertEquals(p, difference.width(), p.a.width());
            antiCommutative(RationalMatrix::subtract, RationalMatrix::negate, p);
            inverse(m -> m.subtract(p.b), (RationalMatrix m) -> m.add(p.b), p.a);
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            assertEquals(m, zero(m.height(), m.width()).subtract(m), m.negate());
            fixedPoint(n -> n.subtract(zero(n.height(), n.width())), m);
            assertTrue(m, m.subtract(m).isZero());
        }

        Iterable<Pair<RationalMatrix, RationalMatrix>> psFail = filterInfinite(
                p -> p.a.height() != p.b.height() || p.a.width() != p.b.width(),
                P.pairs(P.rationalMatrices())
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, psFail)) {
            try {
                p.a.subtract(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<RationalMatrix, RationalMatrix>, RationalMatrix>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        Iterable<Pair<RationalMatrix, RationalMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.rationalMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    RationalMatrix m = RationalMatrix.zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    RationalMatrix m = RationalMatrix.zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        compareImplementations("subtract(RationalMatrix)", take(LIMIT, ps), functions);
    }

    private void propertiesMultiply_Rational() {
        initialize("multiply(Rational)");
        for (Pair<RationalMatrix, Rational> p : take(LIMIT, P.pairs(P.rationalMatrices(), P.rationals()))) {
            RationalMatrix m = p.a.multiply(p.b);
            m.validate();
            assertEquals(p, m.height(), p.a.height());
            assertEquals(p, m.width(), p.a.width());
        }

        for (Pair<RationalMatrix, Rational> p : take(LIMIT, P.pairs(P.rationalMatrices(), P.nonzeroRationals()))) {
            inverse(m -> m.multiply(p.b), (RationalMatrix m) -> m.divide(p.b), p.a);
            assertEquals(p, p.a.multiply(p.b), p.a.divide(p.b.invert()));
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            fixedPoint(n -> n.multiply(Rational.ONE), m);
            assertTrue(m, m.multiply(Rational.ZERO).isZero());
        }

        Iterable<Triple<Integer, Integer, Rational>> ts = map(
                p -> new Triple<>(p.b.a, p.b.b, p.a),
                P.pairsSquareRootOrder(P.rationals(), P.pairs(P.naturalIntegersGeometric()))
        );
        for (Triple<Integer, Integer, Rational> t : take(LIMIT, ts)) {
            assertTrue(t, zero(t.a, t.b).multiply(t.c).isZero());
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        for (Pair<RationalMatrix, BigInteger> p : take(LIMIT, P.pairs(P.rationalMatrices(), P.bigIntegers()))) {
            RationalMatrix m = p.a.multiply(p.b);
            m.validate();
            assertEquals(p, m.height(), p.a.height());
            assertEquals(p, m.width(), p.a.width());
        }

        for (Pair<RationalMatrix, BigInteger> p : take(LIMIT, P.pairs(P.rationalMatrices(), P.nonzeroBigIntegers()))) {
            inverse(m -> m.multiply(p.b), (RationalMatrix m) -> m.divide(p.b), p.a);
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
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
        for (Pair<RationalMatrix, Integer> p : take(LIMIT, P.pairs(P.rationalMatrices(), P.integers()))) {
            RationalMatrix m = p.a.multiply(p.b);
            m.validate();
            assertEquals(p, m.height(), p.a.height());
            assertEquals(p, m.width(), p.a.width());
        }

        for (Pair<RationalMatrix, Integer> p : take(LIMIT, P.pairs(P.rationalMatrices(), P.nonzeroIntegers()))) {
            inverse(m -> m.multiply(p.b), (RationalMatrix m) -> m.divide(p.b), p.a);
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
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

    private void propertiesDivide_Rational() {
        initialize("divide(Rational)");
        for (Pair<RationalMatrix, Rational> p : take(LIMIT, P.pairs(P.rationalMatrices(), P.nonzeroRationals()))) {
            RationalMatrix m = p.a.divide(p.b);
            m.validate();
            assertEquals(p, m.height(), p.a.height());
            assertEquals(p, m.width(), p.a.width());
            inverse(n -> n.divide(p.b), (RationalMatrix n) -> n.multiply(p.b), p.a);
            assertEquals(p, m, p.a.multiply(p.b.invert()));
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            fixedPoint(u -> u.divide(Rational.ONE), m);
        }

        Iterable<Triple<Integer, Integer, Rational>> ts = map(
                p -> new Triple<>(p.b.a, p.b.b, p.a),
                P.pairsSquareRootOrder(P.nonzeroRationals(), P.pairs(P.naturalIntegersGeometric()))
        );
        for (Triple<Integer, Integer, Rational> t : take(LIMIT, ts)) {
            assertTrue(t, zero(t.a, t.b).divide(t.c).isZero());
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            try {
                m.divide(Rational.ZERO);
                fail(m);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_BigInteger() {
        initialize("divide(BigInteger)");
        for (Pair<RationalMatrix, BigInteger> p : take(LIMIT, P.pairs(P.rationalMatrices(), P.nonzeroBigIntegers()))) {
            RationalMatrix m = p.a.divide(p.b);
            m.validate();
            assertEquals(p, m.height(), p.a.height());
            assertEquals(p, m.width(), p.a.width());
            inverse(n -> n.divide(p.b), (RationalMatrix n) -> n.multiply(p.b), p.a);
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            fixedPoint(u -> u.divide(BigInteger.ONE), m);
        }

        Iterable<Triple<Integer, Integer, BigInteger>> ts = map(
                p -> new Triple<>(p.b.a, p.b.b, p.a),
                P.pairsSquareRootOrder(P.nonzeroBigIntegers(), P.pairs(P.naturalIntegersGeometric()))
        );
        for (Triple<Integer, Integer, BigInteger> t : take(LIMIT, ts)) {
            assertTrue(t, zero(t.a, t.b).divide(t.c).isZero());
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            try {
                m.divide(BigInteger.ZERO);
                fail(m);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_int() {
        initialize("divide(int)");
        for (Pair<RationalMatrix, Integer> p : take(LIMIT, P.pairs(P.rationalMatrices(), P.nonzeroIntegers()))) {
            RationalMatrix m = p.a.divide(p.b);
            m.validate();
            assertEquals(p, m.height(), p.a.height());
            assertEquals(p, m.width(), p.a.width());
            inverse(n -> n.divide(p.b), (RationalMatrix n) -> n.multiply(p.b), p.a);
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            fixedPoint(u -> u.divide(1), m);
        }

        Iterable<Triple<Integer, Integer, Integer>> ts = map(
                p -> new Triple<>(p.b.a, p.b.b, p.a),
                P.pairsSquareRootOrder(P.nonzeroIntegers(), P.pairs(P.naturalIntegersGeometric()))
        );
        for (Triple<Integer, Integer, Integer> t : take(LIMIT, ts)) {
            assertTrue(t, zero(t.a, t.b).divide(t.c).isZero());
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            try {
                m.divide(0);
                fail(m);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::rationalMatrices);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::rationalMatrices);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(RationalMatrix)");
        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::rationalMatrices);

        Iterable<Pair<RationalMatrix, RationalMatrix>> ps = filterInfinite(
                p -> p.a.height() != p.b.height(),
                P.pairs(P.rationalMatrices())
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.height(), p.b.height()));
        }

        ps = filterInfinite(
                p -> p.a.height() == p.b.height() && p.a.width() != p.b.width(),
                P.pairs(P.rationalMatrices())
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.width(), p.b.width()));
        }
    }

    private void propertiesRead() {
        initialize("read(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                RATIONAL_MATRIX_CHARS,
                P.rationalMatrices(),
                RationalMatrix::read,
                RationalMatrix::validate,
                false
        );
    }

    private void propertiesFindIn() {
        initialize("findIn(String)");
        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.rationalMatrices(),
                RationalMatrix::read,
                RationalMatrix::findIn,
                RationalMatrix::validate
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, RATIONAL_MATRIX_CHARS, P.rationalMatrices(), RationalMatrix::read);
    }
}
