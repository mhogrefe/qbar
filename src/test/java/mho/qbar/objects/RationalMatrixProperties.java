package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Quadruple;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.qbar.objects.RationalMatrix.*;
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
        propertiesHasIntegralElements();
        propertiesToMatrix();
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
        propertiesMultiply_Rational();
        propertiesMultiply_BigInteger();
        propertiesMultiply_int();
        propertiesDivide_Rational();
        propertiesDivide_BigInteger();
        propertiesDivide_int();
        propertiesMultiply_RationalVector();
        propertiesMultiply_RationalMatrix();
        compareImplementationsMultiply_RationalMatrix();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesShiftRight();
        compareImplementationsShiftRight();
        propertiesIsInRowEchelonForm();
        propertiesRowEchelonForm();
        propertiesRank();
        propertiesIsInvertible();
        propertiesIsInReducedRowEchelonForm();
        propertiesReducedRowEchelonForm();
        propertiesSolveLinearSystem();
        propertiesSolveLinearSystemPermissive();
        propertiesInvert();
        propertiesDeterminant();
        compareImplementationsDeterminant();
        propertiesCharacteristicPolynomial();
        compareImplementationsCharacteristicPolynomial();
        propertiesKroneckerMultiply();
        propertiesKroneckerAdd();
        propertiesRealEigenvalues();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesReadStrict();
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

    private void propertiesHasIntegralElements() {
        initialize("hasIntegralElements()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            m.onlyHasIntegralElements();
        }

        for (RationalMatrix m : take(LIMIT, map(Matrix::toRationalMatrix, P.matrices()))) {
            assertTrue(m, m.onlyHasIntegralElements());
        }
    }

    private void propertiesToMatrix() {
        initialize("toMatrix()");
        for (RationalMatrix m : take(LIMIT, map(Matrix::toRationalMatrix, P.matrices()))) {
            Matrix n = m.toMatrix();
            assertEquals(m, m.toString(), n.toString());
            assertEquals(m, m.height(), n.height());
            assertEquals(m, m.width(), n.width());
            inverse(RationalMatrix::toMatrix, Matrix::toRationalMatrix, m);
        }

        Iterable<RationalMatrix> msFail = filterInfinite(
                m -> any(r -> !r.onlyHasIntegralCoordinates(),
                m.rows()), P.rationalMatrices()
        );
        for (RationalMatrix m : take(LIMIT, msFail)) {
            try {
                m.toMatrix();
                fail(m);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesGet() {
        initialize("get(int, int)");
        Iterable<Triple<RationalMatrix, Integer, Integer>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        filterInfinite(m -> m.height() > 0 && m.width() > 0, P.rationalMatrices()),
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
                map(i -> toList(replicate(i, RationalVector.ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
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
                map(i -> toList(replicate(i, RationalVector.ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
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

    private void propertiesMaxElementBitLength() {
        initialize("maxElementBitLength()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            assertTrue(m, m.maxElementBitLength() >= 0);
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
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            RationalMatrix identity = identity(i);
            identity.validate();
            assertTrue(i, identity.isSquare());
            assertTrue(i, identity.isIdentity());
            inverse(RationalMatrix::identity, RationalMatrix::height, i);
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
        for (RationalMatrix m : take(LIMIT, P.squareRationalMatrices())) {
            Rational trace = m.trace();
            assertEquals(m, trace, m.transpose().trace());
        }

        Iterable<Pair<RationalMatrix, RationalMatrix>> ps = P.withElement(
                new Pair<>(zero(0, 0), zero(0, 0)),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.rationalMatrices(i, i))
                        )
                )
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.add(p.b).trace(), p.a.trace().add(p.b.trace()));
            commutative((a, b) -> a.multiply(b).trace(), p);
            Rational productTrace = Rational.ZERO;
            for (int i = 0; i < p.a.height(); i++) {
                productTrace = productTrace.add(p.a.row(i).dot(p.b.column(i)));
            }
            assertEquals(p, p.a.multiply(p.b).trace(), productTrace);
        }

        Iterable<Pair<RationalMatrix, Rational>> qs = P.pairs(P.withScale(4).squareRationalMatrices(), P.rationals());
        for (Pair<RationalMatrix, Rational> p : take(LIMIT, qs)) {
            assertEquals(p, p.a.multiply(p.b).trace(), p.a.trace().multiply(p.b));
        }
    }

    private void propertiesSubmatrix() {
        initialize("submatrix(List<Integer>, List<Integer>)");
        Iterable<Triple<RationalMatrix, List<Integer>, List<Integer>>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.rationalMatrices(),
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
                        P.rationalMatrices(),
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
                        P.rationalMatrices(),
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

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            RationalMatrix identity = identity(i);
            assertEquals(i, identity, identity.transpose());
        }
    }

    private void compareImplementationsTranspose() {
        Map<String, Function<RationalMatrix, RationalMatrix>> functions = new LinkedHashMap<>();
        functions.put("alt", RationalMatrixProperties::transpose_alt);
        functions.put("standard", RationalMatrix::transpose);
        compareImplementations("transpose()", take(LIMIT, P.rationalMatrices()), functions, v -> P.reset());
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
                                    RationalMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
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
                                    RationalMatrix m = zero(0, i);
                                    return new Triple<>(m, m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    RationalMatrix m = zero(i, 0);
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
                                    RationalMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
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
                                    RationalMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    RationalMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.positiveIntegersGeometric()
                        )
                )
        );
        compareImplementations("subtract(RationalMatrix)", take(LIMIT, ps), functions, v -> P.reset());
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

    private void propertiesMultiply_RationalVector() {
        initialize("multiply(RationalVector)");
        Iterable<Pair<RationalMatrix, RationalVector>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.rationalMatrices(p.a, p.b), P.rationalVectors(p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> new Pair<>(zero(i, 0), RationalVector.ZERO_DIMENSIONAL),
                                P.naturalIntegersGeometric()
                        ),
                        map(v -> new Pair<>(zero(0, v.dimension()), v), P.rationalVectorsAtLeast(1))
                )
        );
        for (Pair<RationalMatrix, RationalVector> p : take(LIMIT, ps)) {
            RationalVector v = p.a.multiply(p.b);
            assertEquals(p, v.dimension(), p.a.height());
        }

        Iterable<Pair<RationalVector, RationalVector>> ps2 = P.withElement(
                new Pair<>(RationalVector.ZERO_DIMENSIONAL, RationalVector.ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.rationalVectors(i))
                        )
                )
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps2)) {
            Rational r = fromRows(Collections.singletonList(p.a)).multiply(p.b).get(0);
            assertEquals(p, r, p.a.dot(p.b));
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            assertEquals(m, m.multiply(RationalVector.zero(m.width())), RationalVector.zero(m.height()));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, identity(v.dimension()).multiply(v), v);
        }

        ps = map(
                q -> new Pair<>(zero(q.a, q.b.dimension()), q.b),
                P.pairs(P.naturalIntegersGeometric(), P.rationalVectors())
        );
        for (Pair<RationalMatrix, RationalVector> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b), RationalVector.zero(p.a.height()));
        }

        Iterable<Pair<RationalMatrix, RationalVector>> psFail = filterInfinite(
                p -> p.a.width() != p.b.dimension(),
                P.pairs(P.rationalMatrices(), P.rationalVectors())
        );
        for (Pair<RationalMatrix, RationalVector> p : take(LIMIT, psFail)) {
            try {
                p.a.multiply(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull RationalMatrix multiply_RationalMatrix_alt(
            @NotNull RationalMatrix a,
            @NotNull RationalMatrix b
    ) {
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

    private void propertiesMultiply_RationalMatrix() {
        initialize("multiply(RationalMatrix)");
        Iterable<Pair<RationalMatrix, RationalMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.triples(P.positiveIntegersGeometric()),
                                t -> P.pairs(
                                        P.withScale(4).rationalMatrices(t.a, t.b),
                                        P.withScale(4).rationalMatrices(t.b, t.c)
                                )
                        )
                ),
                P.choose(
                    P.choose(
                            map(
                                    m -> new Pair<>(m, zero(m.width(), 0)),
                                    filterInfinite(
                                            m -> m.height() != 0 && m.width() != 0,
                                            P.withScale(4).rationalMatrices()
                                    )
                            ),
                            map(
                                    m -> new Pair<>(zero(0, m.height()), m),
                                    filterInfinite(
                                            m -> m.height() != 0 && m.width() != 0,
                                            P.withScale(4).rationalMatrices()
                                    )
                            )
                    ),
                    map(
                            p -> new Pair<>(zero(p.a, 0), zero(0, p.b)),
                            P.pairs(P.positiveIntegersGeometric())
                    )
                )
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, ps)) {
            RationalMatrix product = p.a.multiply(p.b);
            assertEquals(p, multiply_RationalMatrix_alt(p.a, p.b), product);
            assertEquals(p, product.height(), p.a.height());
            assertEquals(p, product.width(), p.b.width());
        }

        Iterable<Pair<RationalVector, RationalVector>> ps2 = P.withElement(
                new Pair<>(RationalVector.ZERO_DIMENSIONAL, RationalVector.ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.rationalVectors(i))
                        )
                )
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps2)) {
            Rational r = fromRows(Collections.singletonList(p.a))
                    .multiply(fromColumns(Collections.singletonList(p.b))).get(0, 0);
            assertEquals(p, r, p.a.dot(p.b));
        }

        Iterable<Pair<RationalMatrix, RationalVector>> ps3 = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.pairs(P.rationalMatrices(p.a, p.b), P.rationalVectors(p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> new Pair<>(zero(i, 0), RationalVector.ZERO_DIMENSIONAL),
                                P.naturalIntegersGeometric()
                        ),
                        map(v -> new Pair<>(zero(0, v.dimension()), v), P.rationalVectorsAtLeast(1))
                )
        );
        for (Pair<RationalMatrix, RationalVector> p : take(LIMIT, ps3)) {
            assertEquals(p, p.a.multiply(p.b), p.a.multiply(fromColumns(Collections.singletonList(p.b))).column(0));
        }

        Iterable<Pair<RationalMatrix, Integer>> ps4 = P.pairs(P.rationalMatrices(), P.naturalIntegersGeometric());
        for (Pair<RationalMatrix, Integer> p : take(LIMIT, ps4)) {
            assertEquals(p, p.a.multiply(zero(p.a.width(), p.b)), zero(p.a.height(), p.b));
            assertEquals(p, zero(p.b, p.a.height()).multiply(p.a), zero(p.b, p.a.width()));
        }

        for (RationalMatrix m : take(LIMIT, P.squareRationalMatrices())) {
            assertEquals(m, m.multiply(identity(m.width())), m);
            assertEquals(m, identity(m.width()).multiply(m), m);
        }

        Iterable<Pair<RationalMatrix, RationalMatrix>> psFail = filterInfinite(
                p -> p.a.width() != p.b.height(),
                P.pairs(P.rationalMatrices())
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, psFail)) {
            try {
                p.a.multiply(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsMultiply_RationalMatrix() {
        Map<String, Function<Pair<RationalMatrix, RationalMatrix>, RationalMatrix>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> multiply_RationalMatrix_alt(p.a, p.b));
        functions.put("standard", p -> p.a.multiply(p.b));
        Iterable<Pair<RationalMatrix, RationalMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.triples(P.positiveIntegersGeometric()),
                                t -> P.pairs(
                                        P.withScale(4).rationalMatrices(t.a, t.b),
                                        P.withScale(4).rationalMatrices(t.b, t.c)
                                )
                        )
                ),
                P.choose(
                        P.choose(
                                map(
                                        m -> new Pair<>(m, zero(m.width(), 0)),
                                        filterInfinite(
                                                m -> m.height() != 0 && m.width() != 0,
                                                P.withScale(4).rationalMatrices()
                                        )
                                ),
                                map(
                                        m -> new Pair<>(zero(0, m.height()), m),
                                        filterInfinite(
                                                m -> m.height() != 0 && m.width() != 0,
                                                P.withScale(4).rationalMatrices()
                                        )
                                )
                        ),
                        map(
                                p -> new Pair<>(zero(p.a, 0), zero(0, p.b)),
                                P.pairs(P.positiveIntegersGeometric())
                        )
                )
        );
        compareImplementations("multiply(RationalMatrix)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull RationalMatrix shiftLeft_simplest(@NotNull RationalMatrix m, int bits) {
        if (bits < 0) {
            return m.divide(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return m.multiply(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        for (Pair<RationalMatrix, Integer> p : take(LIMIT, P.pairs(P.rationalMatrices(), P.integersGeometric()))) {
            RationalMatrix shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            assertEquals(p, p.a.height(), shifted.height());
            assertEquals(p, p.a.width(), shifted.width());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
            inverse(a -> a.shiftLeft(p.b), (RationalMatrix m) -> m.shiftRight(p.b), p.a);
            assertEquals(p, shifted, p.a.shiftRight(-p.b));
            homomorphic(
                    RationalMatrix::negate,
                    Function.identity(),
                    RationalMatrix::negate,
                    RationalMatrix::shiftLeft,
                    RationalMatrix::shiftLeft,
                    p
            );
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            fixedPoint(n -> n.shiftLeft(0), m);
        }

        Iterable<Pair<RationalMatrix, Integer>> ps = P.pairs(P.rationalMatrices(), P.naturalIntegersGeometric());
        for (Pair<RationalMatrix, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.shiftLeft(p.b), p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<RationalMatrix, Integer>, RationalMatrix>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        Iterable<Pair<RationalMatrix, Integer>> ps = P.pairs(P.rationalMatrices(), P.integersGeometric());
        compareImplementations("shiftLeft(int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull RationalMatrix shiftRight_simplest(@NotNull RationalMatrix m, int bits) {
        if (bits < 0) {
            return m.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return m.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftRight() {
        initialize("shiftRight(int)");
        for (Pair<RationalMatrix, Integer> p : take(LIMIT, P.pairs(P.rationalMatrices(), P.integersGeometric()))) {
            RationalMatrix shifted = p.a.shiftRight(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftRight_simplest(p.a, p.b));
            assertEquals(p, p.a.height(), shifted.height());
            assertEquals(p, p.a.width(), shifted.width());
            assertEquals(p, p.a.negate().shiftRight(p.b), shifted.negate());
            inverse(a -> a.shiftRight(p.b), (RationalMatrix m) -> m.shiftLeft(p.b), p.a);
            assertEquals(p, shifted, p.a.shiftLeft(-p.b));
            homomorphic(
                    RationalMatrix::negate,
                    Function.identity(),
                    RationalMatrix::negate,
                    RationalMatrix::shiftRight,
                    RationalMatrix::shiftRight,
                    p
            );
        }

        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            assertEquals(m, m.shiftRight(0), m);
        }

        Iterable<Pair<RationalMatrix, Integer>> ps = P.pairs(P.rationalMatrices(), P.naturalIntegersGeometric());
        for (Pair<RationalMatrix, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.shiftRight(p.b), p.a.divide(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private void compareImplementationsShiftRight() {
        Map<String, Function<Pair<RationalMatrix, Integer>, RationalMatrix>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftRight_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftRight(p.b));
        Iterable<Pair<RationalMatrix, Integer>> ps = P.pairs(P.rationalMatrices(), P.integersGeometric());
        compareImplementations("shiftRight(int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesIsInRowEchelonForm() {
        initialize("isInRowEchelonForm()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            assertEquals(m, m.isInRowEchelonForm(), m.equals(m.rowEchelonForm()));
        }

        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            RationalMatrix zero = zero(p.a, p.b);
            assertTrue(p, zero.isInRowEchelonForm());
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            RationalMatrix identity = identity(i);
            assertTrue(i, identity.isInRowEchelonForm());
        }
    }

    private void propertiesRowEchelonForm() {
        initialize("rowEchelonForm()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            RationalMatrix ref = m.rowEchelonForm();
            ref.validate();
            assertTrue(m, ref.isInRowEchelonForm());
            idempotent(RationalMatrix::rowEchelonForm, m);
        }
    }

    private void propertiesRank() {
        initialize("rank()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            int rank = m.rank();
            assertTrue(m, rank >= 0);
            assertTrue(m, rank <= m.height());
            assertTrue(m, rank <= m.width());
            assertEquals(m, m.transpose().rank(), rank);
        }

        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            RationalMatrix zero = zero(p.a, p.b);
            assertEquals(p, zero.rank(), 0);
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            RationalMatrix identity = identity(i);
            assertEquals(i, identity.rank(), i);
        }
    }

    private void propertiesIsInvertible() {
        initialize("isInvertible()");
        for (RationalMatrix m : take(LIMIT, P.squareRationalMatrices())) {
            boolean isInvertible = m.isInvertible();
            assertEquals(m, m.transpose().isInvertible(), isInvertible);
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            RationalMatrix identity = identity(i);
            assertTrue(i, identity.isInvertible());
        }
    }

    private void propertiesIsInReducedRowEchelonForm() {
        initialize("isInReducedRowEchelonForm()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            boolean inRref = m.isInReducedRowEchelonForm();
            assertEquals(m, inRref, m.equals(m.reducedRowEchelonForm()));
            if (inRref) {
                assertTrue(m, m.isInRowEchelonForm());
            }
        }

        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            RationalMatrix zero = zero(p.a, p.b);
            assertTrue(p, zero.isInReducedRowEchelonForm());
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            RationalMatrix identity = identity(i);
            assertTrue(i, identity.isInReducedRowEchelonForm());
        }
    }

    private void propertiesReducedRowEchelonForm() {
        initialize("reducedRowEchelonForm()");
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            RationalMatrix rref = m.reducedRowEchelonForm();
            rref.validate();
            assertTrue(m, rref.isInReducedRowEchelonForm());
            idempotent(RationalMatrix::reducedRowEchelonForm, m);
        }
    }

    private void propertiesSolveLinearSystem() {
        initialize("solveLinearSystem(RationalVector)");
        Iterable<Pair<RationalMatrix, RationalVector>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(P.rationalMatrices(p.a, p.b), P.rationalVectors(p.a))
                        )
                ),
                P.choose(
                        map(
                                i -> new Pair<>(zero(0, i), RationalVector.ZERO_DIMENSIONAL),
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(v -> new Pair<>(zero(v.dimension(), 0), v), P.withScale(4).rationalVectorsAtLeast(1))
                )
        );
        for (Pair<RationalMatrix, RationalVector> p : take(LIMIT, ps)) {
            Optional<RationalVector> solution = p.a.solveLinearSystem(p.b);
            if (solution.isPresent()) {
                assertTrue(p, p.a.height() >= p.a.width());
                RationalVector v = solution.get();
                assertEquals(p, p.a.multiply(v), p.b);
                assertEquals(p, solution, p.a.solveLinearSystemPermissive(p.b));
            }
        }

        Iterable<Pair<RationalMatrix, RationalVector>> psFail = filterInfinite(
                q -> q.b.dimension() != q.a.height(),
                P.pairs(P.rationalMatrices(), P.rationalVectors())
        );
        for (Pair<RationalMatrix, RationalVector> p : take(LIMIT, psFail)) {
            try {
                p.a.solveLinearSystem(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesSolveLinearSystemPermissive() {
        initialize("solveLinearSystemPermissive(RationalVector)");
        Iterable<Pair<RationalMatrix, RationalVector>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(
                                        P.withScale(4).rationalMatrices(p.a, p.b),
                                        P.withScale(4).rationalVectors(p.a)
                                )
                        )
                ),
                P.choose(
                        map(
                                i -> new Pair<>(zero(0, i), RationalVector.ZERO_DIMENSIONAL),
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(v -> new Pair<>(zero(v.dimension(), 0), v), P.withScale(4).rationalVectorsAtLeast(1))
                )
        );
        for (Pair<RationalMatrix, RationalVector> p : take(LIMIT, ps)) {
            Optional<RationalVector> solution = p.a.solveLinearSystemPermissive(p.b);
            if (solution.isPresent()) {
                RationalVector v = solution.get();
                assertEquals(p, p.a.multiply(v), p.b);
            }
        }

        Iterable<Pair<RationalMatrix, RationalVector>> psFail = filterInfinite(
                q -> q.b.dimension() != q.a.height(),
                P.pairs(P.rationalMatrices(), P.rationalVectors())
        );
        for (Pair<RationalMatrix, RationalVector> p : take(LIMIT, psFail)) {
            try {
                p.a.solveLinearSystemPermissive(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesInvert() {
        initialize("invert()");
        for (RationalMatrix m : take(LIMIT, P.withScale(4).squareRationalMatrices())) {
            Optional<RationalMatrix> oInverse = m.invert();
            assertEquals(m, oInverse.isPresent(), m.isInvertible());
            if (oInverse.isPresent()) {
                RationalMatrix inverse = oInverse.get();
                inverse.validate();
                assertTrue(m, m.multiply(inverse).isIdentity());
                assertTrue(m, inverse.multiply(m).isIdentity());
                involution(n -> n.invert().get(), m);
            }
        }

        for (int i : take(SMALL_LIMIT, P.positiveIntegersGeometric())) {
            RationalMatrix zero = zero(i, i);
            assertFalse(i, zero.invert().isPresent());
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            RationalMatrix identity = identity(i);
            idempotent(m -> m.invert().get(), identity);
        }
    }

    private static @NotNull Rational determinant_Laplace(@NotNull RationalMatrix m) {
        if (m.width() == 0) return Rational.ONE;
        if (m.width() == 1) return m.get(0, 0);
        Rational determinant = Rational.ZERO;
        RationalVector firstRow = m.row(0);
        List<Integer> rowIndices = toList(range(1, m.width() - 1));
        boolean sign = true;
        for (int i = 0; i < m.width(); i++) {
            Rational factor = firstRow.get(i);
            if (!sign) factor = factor.negate();
            sign = !sign;
            if (factor == Rational.ZERO) continue;
            Rational minor = m.submatrix(
                    rowIndices,
                    toList(concat(range(0, i - 1), range(i + 1, m.width() - 1)))
            ).determinant();
            determinant = determinant.add(factor.multiply(minor));
        }
        return determinant;
    }

    private static @NotNull Rational determinant_Gauss(@NotNull RationalMatrix m) {
        int n = m.width();
        if (n != m.height()) {
            throw new IllegalArgumentException("m must be square. Invalid m: " + m);
        }
        Rational det = Rational.ONE;
        List<RationalVector> refRows = toList(m.rows());
        int i = 0;
        outer:
        for (int j = 0; i < n && j < n; j++) {
            int nonzeroRowIndex = i;
            Rational pivot = refRows.get(i).get(j);
            while (pivot == Rational.ZERO) {
                nonzeroRowIndex++;
                if (nonzeroRowIndex == n) continue outer;
                pivot = refRows.get(nonzeroRowIndex).get(j);
            }
            if (nonzeroRowIndex != i) {
                Collections.swap(refRows, i, nonzeroRowIndex);
                det = det.negate();
            }
            RationalVector nonzeroRow = refRows.get(i);
            if (pivot != Rational.ONE) {
                nonzeroRow = nonzeroRow.divide(pivot);
                refRows.set(i, nonzeroRow);
                det = det.multiply(pivot);
            }
            for (int k = i + 1; k < n; k++) {
                RationalVector row = refRows.get(k);
                if (row.get(j) != Rational.ZERO) {
                    refRows.set(k, row.subtract(nonzeroRow.multiply(row.get(j))));
                }
            }
            i++;
        }
        for (int k = n - 1; k >= 0; k--) {
            if (refRows.get(k).get(k) == Rational.ZERO) {
                return Rational.ZERO;
            }
        }
        return det;
    }

    private void propertiesDeterminant() {
        initialize("determinant()");
        for (RationalMatrix m : take(LIMIT, P.withScale(4).squareRationalMatrices())) {
            Rational determinant = m.determinant();
            assertEquals(m, determinant, determinant_Laplace(m));
            assertEquals(m, determinant, determinant_Gauss(m));
            assertEquals(m, determinant, m.transpose().determinant());
            assertEquals(m, determinant != Rational.ZERO, m.isInvertible());
            if (determinant != Rational.ZERO) {
                assertEquals(m, m.invert().get().determinant(), determinant.invert());
            }
        }

        Iterable<Pair<RationalMatrix, Pair<Integer, Integer>>> ps = P.dependentPairs(
                filterInfinite(m -> m.width() > 1, P.withScale(4).squareRationalMatrices()),
                m -> P.subsetPairs(P.range(0, m.height() - 1))
        );
        for (Pair<RationalMatrix, Pair<Integer, Integer>> p : take(LIMIT, ps)) {
            List<RationalVector> rows = toList(p.a.rows());
            Collections.swap(rows, p.b.a, p.b.b);
            RationalMatrix swapped = fromRows(rows);
            assertEquals(p, swapped.determinant(), p.a.determinant().negate());
        }

        Iterable<Pair<Pair<RationalMatrix, Rational>, Integer>> ps2 = P.dependentPairs(
                P.pairs(
                        filterInfinite(m -> m.width() > 0, P.withScale(4).squareRationalMatrices()),
                        P.withScale(4).rationals()
                ),
                p -> P.range(0, p.a.height() - 1)
        );
        for (Pair<Pair<RationalMatrix, Rational>, Integer> p : take(LIMIT, ps2)) {
            List<RationalVector> rows = toList(p.a.a.rows());
            rows.set(p.b, rows.get(p.b).multiply(p.a.b));
            RationalMatrix rowScaled = fromRows(rows);
            assertEquals(p, rowScaled.determinant(), p.a.a.determinant().multiply(p.a.b));
        }

        for (int i : take(SMALL_LIMIT, P.positiveIntegersGeometric())) {
            RationalMatrix zero = zero(i, i);
            assertEquals(i, zero.determinant(), Rational.ZERO);
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            RationalMatrix identity = identity(i);
            assertEquals(i, identity.determinant(), Rational.ONE);
        }
    }

    private void compareImplementationsDeterminant() {
        Map<String, Function<RationalMatrix, Rational>> functions = new LinkedHashMap<>();
        functions.put("Laplace", RationalMatrixProperties::determinant_Laplace);
        functions.put("Gauss", RationalMatrixProperties::determinant_Gauss);
        functions.put("standard", RationalMatrix::determinant);
        compareImplementations(
                "determinant()",
                take(LIMIT, P.withScale(4).squareRationalMatrices()),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull RationalPolynomial characteristicPolynomial_alt(@NotNull RationalMatrix a) {
        return RationalPolynomialMatrix.identity(a.width()).multiply(RationalPolynomial.X)
                .subtract(RationalPolynomialMatrix.of(a)).determinant();
    }

    private void propertiesCharacteristicPolynomial() {
        initialize("characteristicPolynomial()");
        for (RationalMatrix m : take(LIMIT, P.withScale(4).squareRationalMatrices())) {
            RationalPolynomial p = m.characteristicPolynomial();
            assertEquals(m, characteristicPolynomial_alt(m), p);
            assertTrue(m, p.isMonic());
            Rational det = m.determinant();
            assertEquals(m, p.coefficient(0), m.height() % 2 == 0 ? det : det.negate());
            if (m.height() > 0) {
                assertEquals(m, p.coefficient(m.height() - 1), m.trace().negate());
            }
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            RationalMatrix zero = zero(i, i);
            assertEquals(i, zero.characteristicPolynomial(), RationalPolynomial.of(Rational.ONE, i));
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            RationalMatrix identity = identity(i);
            assertEquals(
                    i,
                    identity.characteristicPolynomial(),
                    RationalPolynomial.of(Arrays.asList(Rational.NEGATIVE_ONE, Rational.ONE)).pow(i)
            );
        }
    }

    private void compareImplementationsCharacteristicPolynomial() {
        Map<String, Function<RationalMatrix, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("alt", RationalMatrixProperties::characteristicPolynomial_alt);
        functions.put("standard", RationalMatrix::characteristicPolynomial);
        Iterable<RationalMatrix> ms = P.withScale(4).squareRationalMatrices();
        compareImplementations("characteristicPolynomial()", take(LIMIT, ms), functions, v -> P.reset());
    }

    private void propertiesKroneckerMultiply() {
        initialize("kroneckerMultiply(RationalMatrix)");
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, P.pairs(P.rationalMatrices()))) {
            RationalMatrix product = p.a.kroneckerMultiply(p.b);
            assertEquals(p, product.height(), p.a.height() * p.b.height());
            assertEquals(p, product.width(), p.a.width() * p.b.width());
        }

        Iterable<Triple<RationalMatrix, RationalMatrix, Rational>> ts = P.triples(
                P.rationalMatrices(),
                P.rationalMatrices(),
                P.rationals()
        );
        for (Triple<RationalMatrix, RationalMatrix, Rational> t : take(LIMIT, ts)) {
            RationalMatrix product = t.a.multiply(t.c).kroneckerMultiply(t.b);
            assertEquals(t, product, t.a.kroneckerMultiply(t.b.multiply(t.c)));
            assertEquals(t, product, t.a.kroneckerMultiply(t.b).multiply(t.c));
        }

        for (Triple<RationalMatrix, RationalMatrix, RationalMatrix> t : take(LIMIT, P.triples(P.rationalMatrices()))) {
            associative(RationalMatrix::kroneckerMultiply, t);
        }

        Iterable<Pair<RationalMatrix, RationalMatrix>> psAdd = P.chooseLogarithmicOrder(
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
        Iterable<Triple<RationalMatrix, RationalMatrix, RationalMatrix>> ts2 = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairs(psAdd, P.rationalMatrices())
        );
        for (Triple<RationalMatrix, RationalMatrix, RationalMatrix> t : take(LIMIT, ts2)) {
            leftDistributive(RationalMatrix::add, RationalMatrix::kroneckerMultiply, t);
            rightDistributive(RationalMatrix::add, RationalMatrix::kroneckerMultiply, t);
        }

        Iterable<Pair<RationalMatrix, RationalMatrix>> psMult = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.triples(P.withScale(4).positiveIntegersGeometric()),
                                t -> P.pairs(
                                        P.withScale(4).withSecondaryScale(4).rationalMatrices(t.a, t.b),
                                        P.withScale(4).withSecondaryScale(4).rationalMatrices(t.b, t.c)
                                )
                        )
                ),
                P.choose(
                    P.choose(
                            map(
                                    m -> new Pair<>(m, zero(m.width(), 0)),
                                    filterInfinite(
                                            m -> m.height() != 0 && m.width() != 0,
                                            P.withScale(4).withSecondaryScale(4).rationalMatrices()
                                    )
                            ),
                            map(
                                    m -> new Pair<>(zero(0, m.height()), m),
                                    filterInfinite(
                                            m -> m.height() != 0 && m.width() != 0,
                                            P.withScale(4).withSecondaryScale(4).rationalMatrices()
                                    )
                            )
                    ),
                    map(
                            p -> new Pair<>(zero(p.a, 0), zero(0, p.b)),
                            P.pairs(P.withScale(4).positiveIntegersGeometric())
                    )
                )
        );
        Iterable<Quadruple<RationalMatrix, RationalMatrix, RationalMatrix, RationalMatrix>> qs = map(
                p -> new Quadruple<>(p.a.a, p.b.a, p.a.b, p.b.b),
                P.pairs(psMult)
        );
        for (Quadruple<RationalMatrix, RationalMatrix, RationalMatrix, RationalMatrix> q : take(LIMIT, qs)) {
            assertEquals(
                    q,
                    q.a.kroneckerMultiply(q.b).multiply(q.c.kroneckerMultiply(q.d)),
                    q.a.multiply(q.c).kroneckerMultiply(q.b.multiply(q.d))
            );
        }

        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, P.pairs(P.rationalMatrices()))) {
            assertEquals(
                    p,
                    p.a.kroneckerMultiply(p.b).transpose(),
                    p.a.transpose().kroneckerMultiply(p.b.transpose())
            );
        }

        Iterable<Pair<RationalMatrix, RationalMatrix>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(4).squareRationalMatrices()
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, ps)) {
            assertEquals(
                    p,
                    p.a.kroneckerMultiply(p.b).determinant(),
                    p.a.determinant().pow(p.b.width()).multiply(p.b.determinant().pow(p.a.width()))
            );
        }

        ps = P.pairs(P.withScale(3).withSecondaryScale(2).invertibleRationalMatrices());
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, ps)) {
            assertEquals(
                    p,
                    p.a.kroneckerMultiply(p.b).invert().get(),
                    p.a.invert().get().kroneckerMultiply(p.b.invert().get())
            );
        }

        for (Pair<RationalMatrix, Rational> p : take(LIMIT, P.pairs(P.rationalMatrices(), P.rationals()))) {
            assertEquals(
                    p,
                    p.a.kroneckerMultiply(fromRows(Collections.singletonList(RationalVector.of(p.b)))),
                    p.a.multiply(p.b)
            );
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            RationalMatrix product = fromRows(Collections.singletonList(RationalVector.of(p.a)))
                    .kroneckerMultiply(fromRows(Collections.singletonList(RationalVector.of(p.b))));
            assertEquals(p, product.height(), 1);
            assertEquals(p, product.width(), 1);
            assertEquals(p, product.get(0, 0), p.a.multiply(p.b));
        }
    }

    private void propertiesKroneckerAdd() {
        initialize("kroneckerAdd(RationalMatrix)");
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, P.pairs(P.squareRationalMatrices()))) {
            RationalMatrix sum = p.a.kroneckerAdd(p.b);
            assertEquals(p, sum.height(), p.a.height() * p.b.height());
            assertEquals(p, sum.width(), p.a.width() * p.b.width());
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            RationalMatrix sum = fromRows(Collections.singletonList(RationalVector.of(p.a)))
                    .kroneckerAdd(fromRows(Collections.singletonList(RationalVector.of(p.b))));
            assertEquals(p, sum.height(), 1);
            assertEquals(p, sum.width(), 1);
            assertEquals(p, sum.get(0, 0), p.a.add(p.b));
        }
    }

    private void propertiesRealEigenvalues() {
        initialize("realEigenvalues()");
        for (RationalMatrix m : take(LIMIT, P.withScale(4).squareRationalMatrices())) {
            List<Algebraic> realEigenvalues = m.realEigenvalues();
            realEigenvalues.forEach(Algebraic::validate);
            assertTrue(m, increasing(realEigenvalues));
            assertEquals(m, realEigenvalues.size(), m.characteristicPolynomial().constantFactor().b.rootCount());
        }

        for (RationalMatrix m : take(LIMIT, filterInfinite(n -> !n.isSquare(), P.rationalMatrices()))) {
            try {
                m.realEigenvalues();
                fail(m);
            } catch (IllegalArgumentException ignored) {}
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

    private void propertiesReadStrict() {
        initialize("readStrict(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                RATIONAL_MATRIX_CHARS,
                P.rationalMatrices(),
                RationalMatrix::readStrict,
                RationalMatrix::validate,
                false,
                true
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, RATIONAL_MATRIX_CHARS, P.rationalMatrices(), RationalMatrix::readStrict);
    }
}
