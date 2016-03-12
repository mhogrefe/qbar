package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    private void propertiesTrace() {
        initialize("trace()");
        for (PolynomialMatrix m : take(LIMIT, P.squarePolynomialMatrices())) {
            Polynomial trace = m.trace();
            assertEquals(m, trace, m.transpose().trace());
        }

        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> ps = P.withElement(
                new Pair<>(zero(0, 0), zero(0, 0)),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).withSecondaryScale(4).polynomialMatrices(i, i))
                        )
                )
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.add(p.b).trace(), p.a.trace().add(p.b.trace()));
            commutative((a, b) -> a.multiply(b).trace(), p);
            Polynomial productTrace = Polynomial.ZERO;
            for (int i = 0; i < p.a.height(); i++) {
                productTrace = productTrace.add(p.a.row(i).dot(p.b.column(i)));
            }
            assertEquals(p, p.a.multiply(p.b).trace(), productTrace);
        }

        Iterable<Pair<PolynomialMatrix, Polynomial>> ps2 =  P.pairs(
                P.withScale(4).squarePolynomialMatrices(),
                P.polynomials()
        );
        for (Pair<PolynomialMatrix, Polynomial> p : take(LIMIT, ps2)) {
            assertEquals(p, p.a.multiply(p.b).trace(), p.a.trace().multiply(p.b));
        }
    }

    private void propertiesSubmatrix() {
        initialize("submatrix(List<Integer>, List<Integer>)");
        Iterable<Triple<PolynomialMatrix, List<Integer>, List<Integer>>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.polynomialMatrices(),
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
        for (Triple<PolynomialMatrix, List<Integer>, List<Integer>> t : take(LIMIT, ts)) {
            PolynomialMatrix submatrix = t.a.submatrix(t.b, t.c);
            submatrix.validate();
            assertEquals(t, submatrix.height(), t.b.size());
            assertEquals(t, submatrix.width(), t.c.size());
        }

        PolynomialMatrix zero = zero(0, 0);
        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            assertEquals(m, m.submatrix(Collections.emptyList(), Collections.emptyList()), zero);
            assertEquals(m, m.submatrix(toList(range(0, m.height() - 1)), toList(range(0, m.width() - 1))), m);
        }

        Iterable<Triple<PolynomialMatrix, List<Integer>, List<Integer>>> tsFail = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.polynomialMatrices(),
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
        for (Triple<PolynomialMatrix, List<Integer>, List<Integer>> t : take(LIMIT, tsFail)) {
            try {
                t.a.submatrix(t.b, t.c);
                fail(t);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }

        tsFail = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.polynomialMatrices(),
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
        for (Triple<PolynomialMatrix, List<Integer>, List<Integer>> t : take(LIMIT, tsFail)) {
            try {
                t.a.submatrix(t.b, t.c);
                fail(t);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }
    }

    private static @NotNull PolynomialMatrix transpose_alt(@NotNull PolynomialMatrix m) {
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
                                    PolynomialVector::of,
                                    (Iterable<List<Polynomial>>) IterableUtils.transpose(
                                            map(r -> (Iterable<Polynomial>) r, m.rows())
                                    )
                            )
                    )
            );
        }
    }

    private void propertiesTranspose() {
        initialize("transpose()");
        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            PolynomialMatrix transposed = m.transpose();
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
            involution(PolynomialMatrix::transpose, m);
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            PolynomialMatrix identity = identity(i);
            assertEquals(i, identity, identity.transpose());
        }
    }

    private void compareImplementationsTranspose() {
        Map<String, Function<PolynomialMatrix, PolynomialMatrix>> functions = new LinkedHashMap<>();
        functions.put("alt", PolynomialMatrixProperties::transpose_alt);
        functions.put("standard", PolynomialMatrix::transpose);
        compareImplementations("transpose()", take(LIMIT, P.polynomialMatrices()), functions);
    }

    private void propertiesConcat() {
        initialize("concat(PolynomialMatrix)");
        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> ps = P.chooseLogarithmicOrder(
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
                                t -> P.pairs(P.polynomialMatrices(t.a, t.c), P.polynomialMatrices(t.b, t.c))
                        )
                ),
                P.choose(
                        map(
                                p -> new Pair<>(zero(p.a, 0), zero(p.b, 0)),
                                P.pairs(P.naturalIntegersGeometric())
                        ),
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(LIMIT, ps)) {
            PolynomialMatrix concatenated = p.a.concat(p.b);
            concatenated.validate();
            assertEquals(p, concatenated.height(), p.a.height() + p.b.height());
            assertEquals(p, concatenated.width(), p.a.width());
            assertEquals(p, concatenated, p.a.transpose().augment(p.b.transpose()).transpose());
        }

        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            assertEquals(m, m, m.concat(zero(0, m.width())));
            assertEquals(m, m, zero(0, m.width()).concat(m));
        }

        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> psFail = filterInfinite(
                q -> q.a.width() != q.b.width(),
                P.pairs(P.polynomialMatrices())
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(LIMIT, psFail)) {
            try {
                p.a.concat(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAugment() {
        initialize("augment(PolynomialMatrix)");
        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> ps = P.chooseLogarithmicOrder(
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
                                t -> P.pairs(P.polynomialMatrices(t.a, t.b), P.polynomialMatrices(t.a, t.c))
                        )
                ),
                P.choose(
                        map(p -> new Pair<>(zero(0, p.a), zero(0, p.b)), P.pairs(P.naturalIntegersGeometric())),
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(LIMIT, ps)) {
            PolynomialMatrix augmented = p.a.augment(p.b);
            augmented.validate();
            assertEquals(p, augmented.height(), p.a.height());
            assertEquals(p, augmented.width(), p.a.width() + p.b.width());
            assertEquals(p, augmented, p.a.transpose().concat(p.b.transpose()).transpose());
        }

        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            assertEquals(m, m, m.augment(zero(m.height(), 0)));
            assertEquals(m, m, zero(m.height(), 0).augment(m));
        }

        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> psFail = filterInfinite(
                q -> q.a.height() != q.b.height(),
                P.pairs(P.polynomialMatrices())
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(LIMIT, psFail)) {
            try {
                p.a.augment(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAdd() {
        initialize("add(PolynomialMatrix)");
        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.polynomialMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(LIMIT, ps)) {
            PolynomialMatrix sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum.height(), p.a.height());
            assertEquals(p, sum.width(), p.a.width());
            commutative(PolynomialMatrix::add, p);
            inverse(m -> m.add(p.b), (PolynomialMatrix m) -> m.subtract(p.b), p.a);
            assertTrue(
                    p,
                    sum.maxElementBitLength() <= Ordering.max(p.a.maxElementBitLength(), p.b.maxElementBitLength()) + 1
            );
        }

        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            fixedPoint(n -> zero(n.height(), n.width()).add(n), m);
            fixedPoint(n -> n.add(zero(n.height(), n.width())), m);
            assertTrue(m, m.add(m.negate()).isZero());
        }

        Iterable<Triple<PolynomialMatrix, PolynomialMatrix, PolynomialMatrix>> ts = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.triples(P.polynomialMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(0, i);
                                    return new Triple<>(m, m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(i, 0);
                                    return new Triple<>(m, m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Triple<PolynomialMatrix, PolynomialMatrix, PolynomialMatrix> t : take(LIMIT, ts)) {
            associative(PolynomialMatrix::add, t);
        }

        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> psFail = filterInfinite(
                p -> p.a.height() != p.b.height() || p.a.width() != p.b.width(),
                P.pairs(P.polynomialMatrices())
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(LIMIT, psFail)) {
            try {
                p.a.add(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            PolynomialMatrix negative = m.negate();
            negative.validate();
            assertEquals(m, m.height(), negative.height());
            assertEquals(m, m.width(), negative.width());
            involution(PolynomialMatrix::negate, m);
            assertTrue(m, m.add(negative).isZero());
        }

        for (PolynomialMatrix m : take(LIMIT, filterInfinite(n -> !n.isZero(), P.polynomialMatrices()))) {
            assertNotEquals(m, m, m.negate());
        }
    }

    private static @NotNull PolynomialMatrix subtract_simplest(
            @NotNull PolynomialMatrix a,
            @NotNull PolynomialMatrix b
    ) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(PolynomialMatrix)");
        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.polynomialMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(LIMIT, ps)) {
            PolynomialMatrix difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, difference, subtract_simplest(p.a, p.b));
            assertEquals(p, difference.height(), p.a.height());
            assertEquals(p, difference.width(), p.a.width());
            antiCommutative(PolynomialMatrix::subtract, PolynomialMatrix::negate, p);
            inverse(m -> m.subtract(p.b), (PolynomialMatrix m) -> m.add(p.b), p.a);
        }

        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            assertEquals(m, zero(m.height(), m.width()).subtract(m), m.negate());
            fixedPoint(n -> n.subtract(zero(n.height(), n.width())), m);
            assertTrue(m, m.subtract(m).isZero());
        }

        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> psFail = filterInfinite(
                p -> p.a.height() != p.b.height() || p.a.width() != p.b.width(),
                P.pairs(P.polynomialMatrices())
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(LIMIT, psFail)) {
            try {
                p.a.subtract(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<PolynomialMatrix, PolynomialMatrix>, PolynomialMatrix>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.polynomialMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        compareImplementations("subtract(PolynomialMatrix)", take(LIMIT, ps), functions);
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
