package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mho.qbar.objects.PolynomialMatrix.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.compare;
import static mho.wheels.testing.Testing.*;

public class PolynomialMatrixProperties extends QBarTestProperties {
    private static final @NotNull String POLYNOMIAL_MATRIX_CHARS = " #*+,-0123456789[]^x";

    public PolynomialMatrixProperties() {
        super("PolynomialMatrix");
    }

    @Override
    protected void testBothModes() {
        propertiesRows();
        propertiesColumns();
        propertiesRow();
        propertiesColumn();
        propertiesToRationalPolynomialMatrix();
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
        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            List<PolynomialVector> rows = toList(m.rows());
            assertTrue(m, all(v -> v.dimension() == m.width(), rows));
            assertEquals(m, rows, toList(m.transpose().columns()));
            testNoRemove(m.rows());
            testHasNext(m.rows());
        }

        for (PolynomialMatrix m : take(LIMIT, filterInfinite(n -> n.height() > 0, P.polynomialMatrices()))) {
            inverse(n -> toList(n.rows()), PolynomialMatrix::fromRows, m);
        }
    }

    private void propertiesColumns() {
        initialize("columns()");
        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            List<PolynomialVector> columns = toList(m.columns());
            assertTrue(m, all(v -> v.dimension() == m.height(), columns));
            assertEquals(m, columns, toList(m.transpose().rows()));
            testNoRemove(m.columns());
            testHasNext(m.columns());
        }

        for (PolynomialMatrix m : take(LIMIT, filterInfinite(n -> n.width() > 0, P.polynomialMatrices()))) {
            inverse(n -> toList(n.columns()), PolynomialMatrix::fromColumns, m);
        }
    }

    private void propertiesRow() {
        initialize("row(int)");
        Iterable<Pair<PolynomialMatrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.height() > 0, P.polynomialMatrices()),
                m -> P.uniformSample(toList(range(0, m.height() - 1)))
        );
        for (Pair<PolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            PolynomialVector row = p.a.row(p.b);
            assertEquals(p, row, toList(p.a.rows()).get(p.b));
        }

        Iterable<Pair<PolynomialMatrix, Integer>> psFail = filterInfinite(
                p -> p.b < 0 || p.b >= p.a.height(),
                P.pairs(P.polynomialMatrices(), P.integers())
        );
        for (Pair<PolynomialMatrix, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.row(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesColumn() {
        initialize("column(int)");
        Iterable<Pair<PolynomialMatrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.width() > 0, P.polynomialMatrices()),
                m -> P.uniformSample(toList(range(0, m.width() - 1)))
        );
        for (Pair<PolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            PolynomialVector column = p.a.column(p.b);
            assertEquals(p, column, toList(p.a.columns()).get(p.b));
        }

        Iterable<Pair<PolynomialMatrix, Integer>> psFail = filterInfinite(
                p -> p.b < 0 || p.b >= p.a.width(),
                P.pairs(P.polynomialMatrices(), P.integers())
        );
        for (Pair<PolynomialMatrix, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.column(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesToRationalPolynomialMatrix() {
        initialize("toRationalPolynomialMatrix()");
        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            RationalPolynomialMatrix rm = m.toRationalPolynomialMatrix();
            assertEquals(m, m.toString(), m.toString());
            assertEquals(m, m.height(), rm.height());
            assertEquals(m, m.width(), rm.width());
            inverse(PolynomialMatrix::toRationalPolynomialMatrix, RationalPolynomialMatrix::toPolynomialMatrix, m);
        }
    }

    private void propertiesGet() {
        initialize("get(int, int)");
        Iterable<Triple<PolynomialMatrix, Integer, Integer>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        filterInfinite(m -> m.height() > 0 && m.width() > 0, P.polynomialMatrices()),
                        m -> P.uniformSample(
                                toList(EP.pairsLex(range(0, m.height() - 1), toList(range(0, m.width() - 1))))
                        )
                )
        );
        for (Triple<PolynomialMatrix, Integer, Integer> t : take(LIMIT, ts)) {
            Polynomial element = t.a.get(t.b, t.c);
            assertEquals(t, element, t.a.row(t.b).get(t.c));
            assertEquals(t, element, t.a.column(t.c).get(t.b));
        }

        Iterable<Triple<PolynomialMatrix, Integer, Integer>> tsFail = filterInfinite(
                t -> t.b < 0 || t.b >= t.a.height() || t.c < 0 || t.c >= t.a.width(),
                P.triples(P.polynomialMatrices(), P.integers(), P.integers())
        );
        for (Triple<PolynomialMatrix, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.get(t.b, t.c);
                fail(t);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesFromRows() {
        initialize("fromRows(List<PolynomialVector>)");
        Iterable<List<PolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.polynomialVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, PolynomialVector.ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        for (List<PolynomialVector> vs : take(LIMIT, vss)) {
            PolynomialMatrix m = fromRows(vs);
            m.validate();
            assertEquals(vs, m, fromColumns(vs).transpose());
            inverse(PolynomialMatrix::fromRows, n -> toList(n.rows()), vs);
            assertEquals(vs, m.height(), vs.size());
            if (vs.size() != 0) {
                assertEquals(vs, m.width(), head(vs).dimension());
            }
        }

        Iterable<List<PolynomialVector>> vssFail = filterInfinite(
                us -> !same(map(PolynomialVector::dimension, us)),
                P.listsAtLeast(1, P.polynomialVectors())
        );
        for (List<PolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                fromRows(vs);
                fail(vs);
            } catch (IllegalArgumentException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.polynomialVectors(i))
                )
        );
        for (List<PolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                fromRows(vs);
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesFromColumns() {
        initialize("fromColumns(List<PolynomialVector>)");
        Iterable<List<PolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.polynomialVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, PolynomialVector.ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        for (List<PolynomialVector> vs : take(LIMIT, vss)) {
            PolynomialMatrix m = fromColumns(vs);
            m.validate();
            assertEquals(vs, m, fromRows(vs).transpose());
            inverse(PolynomialMatrix::fromColumns, n -> toList(n.columns()), vs);
            assertEquals(vs, m.width(), vs.size());
            if (vs.size() != 0) {
                assertEquals(vs, m.height(), head(vs).dimension());
            }
        }

        Iterable<List<PolynomialVector>> vssFail = filterInfinite(
                us -> !same(map(PolynomialVector::dimension, us)),
                P.listsAtLeast(1, P.polynomialVectors())
        );
        for (List<PolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                fromColumns(vs);
                fail(vs);
            } catch (IllegalArgumentException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.polynomialVectors(i))
                )
        );
        for (List<PolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                fromColumns(vs);
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesMaxElementBitLength() {
        initialize("maxElementBitLength()");
        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            assertTrue(m, m.maxElementBitLength() >= 0);
        }
    }

    private void propertiesHeight() {
        initialize("height()");
        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            int height = m.height();
            assertTrue(m, height >= 0);
            assertEquals(m, height, length(m.rows()));
        }
    }

    private void propertiesWidth() {
        initialize("width()");
        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            int width = m.width();
            assertTrue(m, width >= 0);
            assertEquals(m, width, length(m.columns()));
        }
    }

    private void propertiesIsSquare() {
        initialize("isSquare()");
        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            m.isSquare();
        }
    }

    private void propertiesIsZero() {
        initialize("isZero()");
        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            assertEquals(m, m.isZero(), zero(m.height(), m.width()).equals(m));
        }
    }

    private void propertiesZero() {
        initialize("zero(int, int)");
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            PolynomialMatrix zero = zero(p.a, p.b);
            zero.validate();
            assertTrue(p, zero.isZero());
            assertEquals(p, zero.height(), p.a);
            assertEquals(p, zero.width(), p.b);
            inverse(q -> zero(q.a, q.b), (PolynomialMatrix m) -> new Pair<>(m.height(), m.width()), p);
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
        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            m.isIdentity();
        }
    }

    private void propertiesIdentity() {
        initialize("identity(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            PolynomialMatrix identity = identity(i);
            identity.validate();
            assertTrue(i, identity.isSquare());
            assertTrue(i, identity.isIdentity());
            inverse(PolynomialMatrix::identity, PolynomialMatrix::height, i);
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
        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::matrices);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::matrices);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(PolynomialMatrix)");
        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::matrices);

        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> ps = filterInfinite(
                p -> p.a.height() != p.b.height(),
                P.pairs(P.polynomialMatrices())
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.height(), p.b.height()));
        }

        ps = filterInfinite(
                p -> p.a.height() == p.b.height() && p.a.width() != p.b.width(),
                P.pairs(P.polynomialMatrices())
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.width(), p.b.width()));
        }
    }

    private void propertiesRead() {
        initialize("read(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                POLYNOMIAL_MATRIX_CHARS,
                P.polynomialMatrices(),
                PolynomialMatrix::read,
                PolynomialMatrix::validate,
                false
        );
    }

    private void propertiesFindIn() {
        initialize("findIn(String)");
        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.polynomialMatrices(),
                PolynomialMatrix::read,
                PolynomialMatrix::findIn,
                PolynomialMatrix::validate
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, POLYNOMIAL_MATRIX_CHARS, P.polynomialMatrices(), PolynomialMatrix::read);
    }
}
