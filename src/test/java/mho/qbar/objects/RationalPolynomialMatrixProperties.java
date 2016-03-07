package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mho.qbar.objects.RationalPolynomialMatrix.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.compare;
import static mho.wheels.testing.Testing.*;

public class RationalPolynomialMatrixProperties extends QBarTestProperties {
    private static final @NotNull String RATIONAL_POLYNOMIAL_MATRIX_CHARS = " #*+,-/0123456789[]^x";

    public RationalPolynomialMatrixProperties() {
        super("RationalPolynomialMatrix");
    }

    @Override
    protected void testBothModes() {
        propertiesRows();
        propertiesColumns();
        propertiesRow();
        propertiesColumn();
        propertiesHasIntegralElements();
        propertiesToPolynomialMatrix();
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
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead();
        propertiesFindIn();
        propertiesToString();
    }

    private void propertiesRows() {
        initialize("rows()");
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            List<RationalPolynomialVector> rows = toList(m.rows());
            assertTrue(m, all(v -> v.dimension() == m.width(), rows));
            assertEquals(m, rows, toList(m.transpose().columns()));
            testNoRemove(m.rows());
            testHasNext(m.rows());
        }

        Iterable<RationalPolynomialMatrix> ms = filterInfinite(n -> n.height() > 0, P.rationalPolynomialMatrices());
        for (RationalPolynomialMatrix m : take(LIMIT, ms)) {
            inverse(n -> toList(n.rows()), RationalPolynomialMatrix::fromRows, m);
        }
    }

    private void propertiesColumns() {
        initialize("columns()");
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            List<RationalPolynomialVector> columns = toList(m.columns());
            assertTrue(m, all(v -> v.dimension() == m.height(), columns));
            assertEquals(m, columns, toList(m.transpose().rows()));
            testNoRemove(m.columns());
            testHasNext(m.columns());
        }

        Iterable<RationalPolynomialMatrix> ms = filterInfinite(n -> n.width() > 0, P.rationalPolynomialMatrices());
        for (RationalPolynomialMatrix m : take(LIMIT, ms)) {
            inverse(n -> toList(n.columns()), RationalPolynomialMatrix::fromColumns, m);
        }
    }

    private void propertiesRow() {
        initialize("row(int)");
        Iterable<Pair<RationalPolynomialMatrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.height() > 0, P.rationalPolynomialMatrices()),
                m -> P.uniformSample(toList(range(0, m.height() - 1)))
        );
        for (Pair<RationalPolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            RationalPolynomialVector row = p.a.row(p.b);
            assertEquals(p, row, toList(p.a.rows()).get(p.b));
        }

        Iterable<Pair<RationalPolynomialMatrix, Integer>> psFail = filterInfinite(
                p -> p.b < 0 || p.b >= p.a.height(),
                P.pairs(P.rationalPolynomialMatrices(), P.integers())
        );
        for (Pair<RationalPolynomialMatrix, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.row(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesColumn() {
        initialize("column(int)");
        Iterable<Pair<RationalPolynomialMatrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.width() > 0, P.rationalPolynomialMatrices()),
                m -> P.uniformSample(toList(range(0, m.width() - 1)))
        );
        for (Pair<RationalPolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            RationalPolynomialVector column = p.a.column(p.b);
            assertEquals(p, column, toList(p.a.columns()).get(p.b));
        }

        Iterable<Pair<RationalPolynomialMatrix, Integer>> psFail = filterInfinite(
                p -> p.b < 0 || p.b >= p.a.width(),
                P.pairs(P.rationalPolynomialMatrices(), P.integers())
        );
        for (Pair<RationalPolynomialMatrix, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.column(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesHasIntegralElements() {
        initialize("hasIntegralElements()");
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            m.onlyHasIntegralElements();
        }

        Iterable<RationalPolynomialMatrix> ms = map(
                PolynomialMatrix::toRationalPolynomialMatrix,
                P.polynomialMatrices()
        );
        for (RationalPolynomialMatrix m : take(LIMIT, ms)) {
            assertTrue(m, m.onlyHasIntegralElements());
        }
    }

    private void propertiesToPolynomialMatrix() {
        initialize("toPolynomialMatrix()");
        Iterable<RationalPolynomialMatrix> ms = map(
                PolynomialMatrix::toRationalPolynomialMatrix,
                P.polynomialMatrices()
        );
        for (RationalPolynomialMatrix m : take(LIMIT, ms)) {
            PolynomialMatrix n = m.toPolynomialMatrix();
            assertEquals(m, m.toString(), n.toString());
            assertEquals(m, m.height(), n.height());
            assertEquals(m, m.width(), n.width());
            inverse(RationalPolynomialMatrix::toPolynomialMatrix, PolynomialMatrix::toRationalPolynomialMatrix, m);
        }

        Iterable<RationalPolynomialMatrix> msFail = filterInfinite(
                m -> any(r -> !r.onlyHasIntegralCoordinates(),
                m.rows()), P.rationalPolynomialMatrices()
        );
        for (RationalPolynomialMatrix m : take(LIMIT, msFail)) {
            try {
                m.toPolynomialMatrix();
                fail(m);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesGet() {
        initialize("get(int, int)");
        Iterable<Triple<RationalPolynomialMatrix, Integer, Integer>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        filterInfinite(m -> m.height() > 0 && m.width() > 0, P.rationalPolynomialMatrices()),
                        m -> P.uniformSample(
                                toList(EP.pairsLex(range(0, m.height() - 1), toList(range(0, m.width() - 1))))
                        )
                )
        );
        for (Triple<RationalPolynomialMatrix, Integer, Integer> t : take(LIMIT, ts)) {
            RationalPolynomial element = t.a.get(t.b, t.c);
            assertEquals(t, element, t.a.row(t.b).get(t.c));
            assertEquals(t, element, t.a.column(t.c).get(t.b));
        }

        Iterable<Triple<RationalPolynomialMatrix, Integer, Integer>> tsFail = filterInfinite(
                t -> t.b < 0 || t.b >= t.a.height() || t.c < 0 || t.c >= t.a.width(),
                P.triples(P.rationalPolynomialMatrices(), P.integers(), P.integers())
        );
        for (Triple<RationalPolynomialMatrix, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.get(t.b, t.c);
                fail(t);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesFromRows() {
        initialize("fromRows(List<RationalPolynomialVector>)");
        Iterable<List<RationalPolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.rationalPolynomialVectors(p.b))
                        )
                ),
                map(
                        i -> toList(replicate(i, RationalPolynomialVector.ZERO_DIMENSIONAL)),
                        P.positiveIntegersGeometric()
                )
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vss)) {
            RationalPolynomialMatrix m = fromRows(vs);
            m.validate();
            assertEquals(vs, m, fromColumns(vs).transpose());
            inverse(RationalPolynomialMatrix::fromRows, n -> toList(n.rows()), vs);
            assertEquals(vs, m.height(), vs.size());
            if (vs.size() != 0) {
                assertEquals(vs, m.width(), head(vs).dimension());
            }
        }

        Iterable<List<RationalPolynomialVector>> vssFail = filterInfinite(
                us -> !same(map(RationalPolynomialVector::dimension, us)),
                P.listsAtLeast(1, P.rationalPolynomialVectors())
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                fromRows(vs);
                fail(vs);
            } catch (IllegalArgumentException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.rationalPolynomialVectors(i))
                )
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                fromRows(vs);
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesFromColumns() {
        initialize("fromColumns(List<RationalPolynomialVector>)");
        Iterable<List<RationalPolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.rationalPolynomialVectors(p.b))
                        )
                ),
                map(
                        i -> toList(replicate(i, RationalPolynomialVector.ZERO_DIMENSIONAL)),
                        P.positiveIntegersGeometric()
                )
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vss)) {
            RationalPolynomialMatrix m = fromColumns(vs);
            m.validate();
            assertEquals(vs, m, fromRows(vs).transpose());
            inverse(RationalPolynomialMatrix::fromColumns, n -> toList(n.columns()), vs);
            assertEquals(vs, m.width(), vs.size());
            if (vs.size() != 0) {
                assertEquals(vs, m.height(), head(vs).dimension());
            }
        }

        Iterable<List<RationalPolynomialVector>> vssFail = filterInfinite(
                us -> !same(map(RationalPolynomialVector::dimension, us)),
                P.listsAtLeast(1, P.rationalPolynomialVectors())
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                fromColumns(vs);
                fail(vs);
            } catch (IllegalArgumentException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.rationalPolynomialVectors(i))
                )
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                fromColumns(vs);
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesMaxElementBitLength() {
        initialize("maxElementBitLength()");
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            assertTrue(m, m.maxElementBitLength() >= 0);
        }
    }

    private void propertiesHeight() {
        initialize("height()");
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            int height = m.height();
            assertTrue(m, height >= 0);
            assertEquals(m, height, length(m.rows()));
        }
    }

    private void propertiesWidth() {
        initialize("width()");
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            int width = m.width();
            assertTrue(m, width >= 0);
            assertEquals(m, width, length(m.columns()));
        }
    }

    private void propertiesIsSquare() {
        initialize("isSquare()");
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            m.isSquare();
        }
    }

    private void propertiesIsZero() {
        initialize("isZero()");
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            assertEquals(m, m.isZero(), zero(m.height(), m.width()).equals(m));
        }
    }

    private void propertiesZero() {
        initialize("zero(int, int)");
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            RationalPolynomialMatrix zero = zero(p.a, p.b);
            zero.validate();
            assertTrue(p, zero.isZero());
            assertEquals(p, zero.height(), p.a);
            assertEquals(p, zero.width(), p.b);
            inverse(q -> zero(q.a, q.b), (RationalPolynomialMatrix m) -> new Pair<>(m.height(), m.width()), p);
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
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            m.isIdentity();
        }
    }

    private void propertiesIdentity() {
        initialize("identity(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            RationalPolynomialMatrix identity = identity(i);
            identity.validate();
            assertTrue(i, identity.isSquare());
            assertTrue(i, identity.isIdentity());
            inverse(RationalPolynomialMatrix::identity, RationalPolynomialMatrix::height, i);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                identity(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::rationalPolynomialMatrices);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::rationalPolynomialMatrices);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(RationalPolynomialMatrix)");
        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::rationalPolynomialMatrices);

        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = filterInfinite(
                p -> p.a.height() != p.b.height(),
                P.pairs(P.rationalPolynomialMatrices())
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.height(), p.b.height()));
        }

        ps = filterInfinite(
                p -> p.a.height() == p.b.height() && p.a.width() != p.b.width(),
                P.pairs(P.rationalPolynomialMatrices())
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.width(), p.b.width()));
        }
    }

    private void propertiesRead() {
        initialize("read(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                RATIONAL_POLYNOMIAL_MATRIX_CHARS,
                P.rationalPolynomialMatrices(),
                RationalPolynomialMatrix::read,
                RationalPolynomialMatrix::validate,
                false
        );
    }

    private void propertiesFindIn() {
        initialize("findIn(String)");
        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.rationalPolynomialMatrices(),
                RationalPolynomialMatrix::read,
                RationalPolynomialMatrix::findIn,
                RationalPolynomialMatrix::validate
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(
                LIMIT,
                RATIONAL_POLYNOMIAL_MATRIX_CHARS,
                P.rationalPolynomialMatrices(),
                RationalPolynomialMatrix::read
        );
    }
}
