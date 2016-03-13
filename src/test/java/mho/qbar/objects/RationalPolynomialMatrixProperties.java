package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    private void propertiesTrace() {
        initialize("trace()");
        for (RationalPolynomialMatrix m : take(LIMIT, P.squareRationalPolynomialMatrices())) {
            RationalPolynomial trace = m.trace();
            assertEquals(m, trace, m.transpose().trace());
        }

        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = P.withElement(
                new Pair<>(zero(0, 0), zero(0, 0)),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).withSecondaryScale(4).rationalPolynomialMatrices(i, i))
                        )
                )
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.add(p.b).trace(), p.a.trace().add(p.b.trace()));
            commutative((a, b) -> a.multiply(b).trace(), p);
            RationalPolynomial productTrace = RationalPolynomial.ZERO;
            for (int i = 0; i < p.a.height(); i++) {
                productTrace = productTrace.add(p.a.row(i).dot(p.b.column(i)));
            }
            assertEquals(p, p.a.multiply(p.b).trace(), productTrace);
        }

        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomial>> qs = P.pairs(
                P.withScale(4).squareRationalPolynomialMatrices(),
                P.rationalPolynomials()
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomial> p : take(LIMIT, qs)) {
            assertEquals(p, p.a.multiply(p.b).trace(), p.a.trace().multiply(p.b));
        }
    }

    private void propertiesSubmatrix() {
        initialize("submatrix(List<Integer>, List<Integer>)");
        Iterable<Triple<RationalPolynomialMatrix, List<Integer>, List<Integer>>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.rationalPolynomialMatrices(),
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
        for (Triple<RationalPolynomialMatrix, List<Integer>, List<Integer>> t : take(LIMIT, ts)) {
            RationalPolynomialMatrix submatrix = t.a.submatrix(t.b, t.c);
            submatrix.validate();
            assertEquals(t, submatrix.height(), t.b.size());
            assertEquals(t, submatrix.width(), t.c.size());
        }

        RationalPolynomialMatrix zero = zero(0, 0);
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            assertEquals(m, m.submatrix(Collections.emptyList(), Collections.emptyList()), zero);
            assertEquals(m, m.submatrix(toList(range(0, m.height() - 1)), toList(range(0, m.width() - 1))), m);
        }

        Iterable<Triple<RationalPolynomialMatrix, List<Integer>, List<Integer>>> tsFail = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.rationalPolynomialMatrices(),
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
        for (Triple<RationalPolynomialMatrix, List<Integer>, List<Integer>> t : take(LIMIT, tsFail)) {
            try {
                t.a.submatrix(t.b, t.c);
                fail(t);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }

        tsFail = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.rationalPolynomialMatrices(),
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
        for (Triple<RationalPolynomialMatrix, List<Integer>, List<Integer>> t : take(LIMIT, tsFail)) {
            try {
                t.a.submatrix(t.b, t.c);
                fail(t);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }
    }

    private static @NotNull RationalPolynomialMatrix transpose_alt(@NotNull RationalPolynomialMatrix m) {
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
                                    RationalPolynomialVector::of,
                                    (Iterable<List<RationalPolynomial>>) IterableUtils.transpose(
                                            map(r -> (Iterable<RationalPolynomial>) r, m.rows())
                                    )
                            )
                    )
            );
        }
    }

    private void propertiesTranspose() {
        initialize("transpose()");
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            RationalPolynomialMatrix transposed = m.transpose();
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
            involution(RationalPolynomialMatrix::transpose, m);
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            RationalPolynomialMatrix identity = identity(i);
            assertEquals(i, identity, identity.transpose());
        }
    }

    private void compareImplementationsTranspose() {
        Map<String, Function<RationalPolynomialMatrix, RationalPolynomialMatrix>> functions = new LinkedHashMap<>();
        functions.put("alt", RationalPolynomialMatrixProperties::transpose_alt);
        functions.put("standard", RationalPolynomialMatrix::transpose);
        compareImplementations("transpose()", take(LIMIT, P.rationalPolynomialMatrices()), functions);
    }

    private void propertiesConcat() {
        initialize("concat(RationalMatrix)");
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = P.chooseLogarithmicOrder(
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
                                        P.rationalPolynomialMatrices(t.a, t.c),
                                        P.rationalPolynomialMatrices(t.b, t.c)
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
                                    RationalPolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(LIMIT, ps)) {
            RationalPolynomialMatrix concatenated = p.a.concat(p.b);
            concatenated.validate();
            assertEquals(p, concatenated.height(), p.a.height() + p.b.height());
            assertEquals(p, concatenated.width(), p.a.width());
            assertEquals(p, concatenated, p.a.transpose().augment(p.b.transpose()).transpose());
        }

        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            assertEquals(m, m, m.concat(zero(0, m.width())));
            assertEquals(m, m, zero(0, m.width()).concat(m));
        }

        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> psFail = filterInfinite(
                q -> q.a.width() != q.b.width(),
                P.pairs(P.rationalPolynomialMatrices())
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(LIMIT, psFail)) {
            try {
                p.a.concat(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAugment() {
        initialize("augment(RationalPolynomialMatrix)");
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = P.chooseLogarithmicOrder(
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
                                t -> P.pairs(
                                        P.rationalPolynomialMatrices(t.a, t.b),
                                        P.rationalPolynomialMatrices(t.a, t.c)
                                )
                        )
                ),
                P.choose(
                        map(p -> new Pair<>(zero(0, p.a), zero(0, p.b)), P.pairs(P.naturalIntegersGeometric())),
                        map(
                                i -> {
                                    RationalPolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(LIMIT, ps)) {
            RationalPolynomialMatrix augmented = p.a.augment(p.b);
            augmented.validate();
            assertEquals(p, augmented.height(), p.a.height());
            assertEquals(p, augmented.width(), p.a.width() + p.b.width());
            assertEquals(p, augmented, p.a.transpose().concat(p.b.transpose()).transpose());
        }

        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            assertEquals(m, m, m.augment(zero(m.height(), 0)));
            assertEquals(m, m, zero(m.height(), 0).augment(m));
        }

        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> psFail = filterInfinite(
                q -> q.a.height() != q.b.height(),
                P.pairs(P.rationalPolynomialMatrices())
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(LIMIT, psFail)) {
            try {
                p.a.augment(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAdd() {
        initialize("add(RationalPolynomialMatrix)");
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.rationalPolynomialMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    RationalPolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    RationalPolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(LIMIT, ps)) {
            RationalPolynomialMatrix sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum.height(), p.a.height());
            assertEquals(p, sum.width(), p.a.width());
            commutative(RationalPolynomialMatrix::add, p);
            inverse(m -> m.add(p.b), (RationalPolynomialMatrix m) -> m.subtract(p.b), p.a);
        }

        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            fixedPoint(n -> zero(n.height(), n.width()).add(n), m);
            fixedPoint(n -> n.add(zero(n.height(), n.width())), m);
            assertTrue(m, m.add(m.negate()).isZero());
        }

        Iterable<Triple<RationalPolynomialMatrix, RationalPolynomialMatrix, RationalPolynomialMatrix>> ts =
                P.chooseLogarithmicOrder(
                        map(
                                q -> q.b,
                                P.dependentPairsInfiniteSquareRootOrder(
                                        P.pairs(P.positiveIntegersGeometric()),
                                        p -> P.triples(P.rationalPolynomialMatrices(p.a, p.b))
                                )
                        ),
                        P.choose(
                                map(
                                        i -> {
                                            RationalPolynomialMatrix m = zero(0, i);
                                            return new Triple<>(m, m, m);
                                        },
                                        P.naturalIntegersGeometric()
                                ),
                                map(
                                        i -> {
                                            RationalPolynomialMatrix m = zero(i, 0);
                                            return new Triple<>(m, m, m);
                                        },
                                        P.positiveIntegersGeometric()
                                )
                        )
                );
        for (Triple<
                RationalPolynomialMatrix,
                RationalPolynomialMatrix,
                RationalPolynomialMatrix
        > t : take(LIMIT, ts)) {
            associative(RationalPolynomialMatrix::add, t);
        }

        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> psFail = filterInfinite(
                p -> p.a.height() != p.b.height() || p.a.width() != p.b.width(),
                P.pairs(P.rationalPolynomialMatrices())
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(LIMIT, psFail)) {
            try {
                p.a.add(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            RationalPolynomialMatrix negative = m.negate();
            negative.validate();
            assertEquals(m, m.height(), negative.height());
            assertEquals(m, m.width(), negative.width());
            involution(RationalPolynomialMatrix::negate, m);
            assertTrue(m, m.add(negative).isZero());
        }

        Iterable<RationalPolynomialMatrix> ms = filterInfinite(n -> !n.isZero(), P.rationalPolynomialMatrices());
        for (RationalPolynomialMatrix m : take(LIMIT, ms)) {
            assertNotEquals(m, m, m.negate());
        }
    }

    private static @NotNull RationalPolynomialMatrix subtract_simplest(
            @NotNull RationalPolynomialMatrix a,
            @NotNull RationalPolynomialMatrix b
    ) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(RationalPolynomialMatrix)");
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.rationalPolynomialMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    RationalPolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    RationalPolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(LIMIT, ps)) {
            RationalPolynomialMatrix difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, difference, subtract_simplest(p.a, p.b));
            assertEquals(p, difference.height(), p.a.height());
            assertEquals(p, difference.width(), p.a.width());
            antiCommutative(RationalPolynomialMatrix::subtract, RationalPolynomialMatrix::negate, p);
            inverse(m -> m.subtract(p.b), (RationalPolynomialMatrix m) -> m.add(p.b), p.a);
        }

        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            assertEquals(m, zero(m.height(), m.width()).subtract(m), m.negate());
            fixedPoint(n -> n.subtract(zero(n.height(), n.width())), m);
            assertTrue(m, m.subtract(m).isZero());
        }

        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> psFail = filterInfinite(
                p -> p.a.height() != p.b.height() || p.a.width() != p.b.width(),
                P.pairs(P.rationalPolynomialMatrices())
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(LIMIT, psFail)) {
            try {
                p.a.subtract(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsSubtract() {
        Map<
                String,
                Function<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>, RationalPolynomialMatrix>
        > functions = new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.rationalPolynomialMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    RationalPolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    RationalPolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        compareImplementations("subtract(RationalPolynomialMatrix)", take(LIMIT, ps), functions);
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
