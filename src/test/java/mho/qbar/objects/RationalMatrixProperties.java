package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mho.qbar.objects.RationalMatrix.*;
import static mho.qbar.objects.RationalVector.ZERO_DIMENSIONAL;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
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
        propertiesIsZero();
        propertiesZero();
        propertiesIdentity();
        propertiesAdd();
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

    private void propertiesIdentity() {
        initialize("identity(int)");
        for (int i : take(SMALL_LIMIT, P.positiveIntegersGeometric())) {
            RationalMatrix identity = identity(i);
            identity.validate();
            for (int r = 0; r < i; r++) {
                for (int c = 0; c < i; c++) {
                    assertEquals(i, identity.get(r, c), r == c ? Rational.ONE : Rational.ZERO);
                }
            }
            inverse(RationalMatrix::identity, RationalMatrix::height, i);
        }

        for (int i : take(LIMIT, P.rangeDown(0))) {
            try {
                identity(i);
                fail(i);
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
            //todo inverse(m -> m.add(p.b), (RationalMatrix m) -> m.subtract(p.b), p.a);
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            fixedPoint(n -> zero(n.height(), n.width()).add(n), m);
            fixedPoint(n -> n.add(zero(n.height(), n.width())), m);
            //todo assertTrue(m, m.add(m.negate()).isZero());
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
                rp -> {}
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, RATIONAL_MATRIX_CHARS, P.rationalMatrices(), RationalMatrix::read);
    }
}
