package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.qbar.testing.QBarTestProperties;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static mho.qbar.testing.QBarTesting.QEP;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class QBarExhaustiveProviderProperties extends QBarTestProperties {
    public QBarExhaustiveProviderProperties() {
        super("QBarExhaustiveProvider");
    }

    @Override
    protected void testConstant() {
        propertiesPositiveRationals();
        propertiesNegativeRationals();
        propertiesNonzeroRationals();
        propertiesRationals();
        propertiesNonNegativeRationalsLessThanOne();
        propertiesFinitelyBoundedIntervals();
        propertiesIntervals();
        propertiesVectors();
        propertiesRationalVectors();
        propertiesReducedRationalVectors();
        propertiesPolynomialVectors();
        propertiesRationalPolynomialVectors();
        propertiesMatrices();
        propertiesSquareMatrices();
        propertiesInvertibleMatrices();
        propertiesRationalMatrices();
        propertiesSquareRationalMatrices();
        propertiesPolynomialMatrices();
        propertiesSquarePolynomialMatrices();
        propertiesInvertibleRationalMatrices();
        propertiesRationalPolynomialMatrices();
        propertiesSquareRationalPolynomialMatrices();
        propertiesPolynomials();
        propertiesPrimitivePolynomials();
        propertiesPositivePrimitivePolynomials();
        propertiesMonicPolynomials();
        propertiesSquareFreePolynomials();
        propertiesPositivePrimitiveSquareFreePolynomials();
        propertiesIrreduciblePolynomials();
        propertiesRationalPolynomials();
        propertiesMonicRationalPolynomials();
        propertiesVariables();
        propertiesMonomialOrders();
        propertiesExponentVectors();
        propertiesMultivariatePolynomials();
        propertiesPositiveAlgebraics();
        propertiesNegativeAlgebraics();
        propertiesNonzeroAlgebraics();
        propertiesAlgebraics();
        propertiesNonNegativeAlgebraicsLessThanOne();
    }

    @Override
    protected void testBothModes() {
        propertiesRangeUp_Rational();
        propertiesRangeDown_Rational();
        propertiesRange_Rational_Rational();
        propertiesRationalsIn();
        propertiesRationalsNotIn();
        propertiesVectors_int();
        propertiesVectorsAtLeast();
        propertiesRationalVectors_int();
        propertiesRationalVectorsAtLeast();
        propertiesReducedRationalVectors_int();
        propertiesReducedRationalVectorsAtLeast();
        propertiesPolynomialVectors_int();
        propertiesPolynomialVectorsAtLeast();
        propertiesRationalPolynomialVectors_int();
        propertiesRationalPolynomialVectorsAtLeast();
        propertiesMatrices_int_int();
        propertiesRationalMatrices_int_int();
        propertiesPolynomialMatrices_int_int();
        propertiesRationalPolynomialMatrices_int_int();
        propertiesPolynomials_int();
        propertiesPolynomialsAtLeast();
        propertiesPrimitivePolynomials_int();
        propertiesPrimitivePolynomialsAtLeast();
        propertiesPositivePrimitivePolynomials_int();
        propertiesPositivePrimitivePolynomialsAtLeast();
        propertiesMonicPolynomials_int();
        propertiesMonicPolynomialsAtLeast();
        propertiesSquareFreePolynomials_int();
        propertiesSquareFreePolynomialsAtLeast();
        propertiesPositivePrimitiveSquareFreePolynomials_int();
        propertiesPositivePrimitiveSquareFreePolynomialsAtLeast();
        propertiesIrreduciblePolynomials_int();
        propertiesIrreduciblePolynomialsAtLeast();
        propertiesRationalPolynomials_int();
        propertiesRationalPolynomialsAtLeast();
        propertiesMonicRationalPolynomials_int();
        propertiesMonicRationalPolynomialsAtLeast();
        propertiesExponentVectors_List_Variable();
        propertiesMultivariatePolynomials_List_Variable();
        propertiesPositiveAlgebraics_int();
        propertiesNegativeAlgebraics_int();
        propertiesNonzeroAlgebraics_int();
        propertiesAlgebraics_int();
        propertiesNonNegativeAlgebraicsLessThanOne_int();
        propertiesRangeUp_int_Algebraic();
        propertiesRangeUp_Algebraic();
        propertiesRangeDown_int_Algebraic();
        propertiesRangeDown_Algebraic();
    }

    private static <T> void test_helper(
            int limit,
            @NotNull Object message,
            @NotNull Iterable<T> xs,
            @NotNull Predicate<T> predicate
    ) {
        Iterable<T> txs = take(limit, xs);
        assertTrue(message, all(x -> x != null && predicate.test(x), txs));
        testNoRemove(limit, txs);
        assertTrue(message, unique(txs));
    }

    private static <T> void simpleTest(
            @NotNull Object message,
            @NotNull Iterable<T> xs,
            @NotNull Predicate<T> predicate
    ) {
        test_helper(TINY_LIMIT, message, xs, predicate);
    }

    private static <T> void biggerTest(
            @NotNull Object message,
            @NotNull Iterable<T> xs,
            @NotNull Predicate<T> predicate
    ) {
        test_helper(LARGE_LIMIT, message, xs, predicate);
    }

    private void propertiesPositiveRationals() {
        initializeConstant("positiveRationals()");
        biggerTest(QEP, QEP.positiveRationals(), r -> r.signum() == 1);
    }

    private void propertiesNegativeRationals() {
        initializeConstant("negativeRationals()");
        biggerTest(QEP, QEP.negativeRationals(), r -> r.signum() == -1);
    }

    private void propertiesNonzeroRationals() {
        initializeConstant("nonzeroRationals()");
        biggerTest(QEP, QEP.nonzeroRationals(), r -> r != Rational.ZERO);
    }

    private void propertiesRationals() {
        initializeConstant("rationals()");
        biggerTest(QEP, QEP.rationals(), r -> true);
    }

    private void propertiesNonNegativeRationalsLessThanOne() {
        initializeConstant("nonNegativeRationalsLessThanOne()");
        biggerTest(QEP, QEP.nonNegativeRationalsLessThanOne(), r -> r.signum() != -1 && lt(r, Rational.ONE));
    }

    private void propertiesRangeUp_Rational() {
        initialize("rangeUp(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Iterable<Rational> rs = QEP.rangeUp(r);
            simpleTest(r, rs, s -> ge(s, r));
        }
    }

    private void propertiesRangeDown_Rational() {
        initialize("rangeDown(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Iterable<Rational> rs = QEP.rangeDown(r);
            simpleTest(r, rs, s -> le(s, r));
        }
    }

    private void propertiesRange_Rational_Rational() {
        initialize("range(Rational, Rational)");
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            Iterable<Rational> rs = QEP.range(p.a, p.b);
            simpleTest(p, rs, r -> ge(r, p.a) && le(r, p.b));
            assertEquals(p, gt(p.a, p.b), isEmpty(rs));
            if (ge(p.a, p.b)) {
                testHasNext(rs);
            }
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            aeqit(r, QEP.range(r, r), Collections.singletonList(r));
        }
    }

    private void propertiesFinitelyBoundedIntervals() {
        initializeConstant("finitelyBoundedIntervals()");
        biggerTest(QEP, QEP.finitelyBoundedIntervals(), Interval::isFinitelyBounded);
    }

    private void propertiesIntervals() {
        initializeConstant("intervals()");
        biggerTest(QEP, QEP.intervals(), a -> true);
    }

    private void propertiesRationalsIn() {
        initialize("rationalsIn(Interval)");
        for (Interval a : take(LIMIT, P.intervals())) {
            Iterable<Rational> rs = QEP.rationalsIn(a);
            simpleTest(a, rs, a::contains);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            Iterable<Rational> rs = QEP.rationalsIn(Interval.of(r, r));
            aeqit(r, rs, Collections.singletonList(r));
            testHasNext(rs);
        }
    }

    private void propertiesRationalsNotIn() {
        initialize("rationalsNotIn(Interval)");
        for (Interval a : take(LIMIT, P.intervals())) {
            Iterable<Rational> rs = QEP.rationalsNotIn(a);
            simpleTest(a, rs, r -> !a.contains(r));
        }
    }

    private void propertiesVectors_int() {
        initialize("vectors(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Iterable<Vector> vs = QEP.vectors(i);
            simpleTest(i, vs, v -> v.dimension() == i);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                QEP.vectors(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesVectors() {
        initializeConstant("vectors()");
        biggerTest(QEP, QEP.vectors(), v -> true);
    }

    private void propertiesVectorsAtLeast() {
        initialize("vectorsAtLeast(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Iterable<Vector> vs = QEP.vectorsAtLeast(i);
            simpleTest(i, vs, v -> v.dimension() >= i);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                QEP.vectorsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalVectors_int() {
        initialize("rationalVectors(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Iterable<RationalVector> vs = QEP.rationalVectors(i);
            simpleTest(i, vs, v -> v.dimension() == i);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                QEP.rationalVectors(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalVectors() {
        initializeConstant("rationalVectors()");
        biggerTest(QEP, QEP.rationalVectors(), v -> true);
    }

    private void propertiesRationalVectorsAtLeast() {
        initialize("rationalVectorsAtLeast(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Iterable<RationalVector> vs = QEP.rationalVectorsAtLeast(i);
            simpleTest(i, vs, v -> v.dimension() >= i);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                QEP.rationalVectorsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesReducedRationalVectors_int() {
        initialize("reducedRationalVectors(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Iterable<RationalVector> vs = QEP.reducedRationalVectors(i);
            simpleTest(i, vs, v -> v.isReduced() && v.dimension() == i);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                QEP.reducedRationalVectors(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesReducedRationalVectors() {
        initializeConstant("reducedRationalVectors()");
        biggerTest(QEP, QEP.reducedRationalVectors(), RationalVector::isReduced);
    }

    private void propertiesReducedRationalVectorsAtLeast() {
        initialize("reducedRationalVectorsAtLeast(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Iterable<RationalVector> vs = QEP.reducedRationalVectorsAtLeast(i);
            simpleTest(i, vs, v -> v.isReduced() && v.dimension() >= i);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                QEP.reducedRationalVectorsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPolynomialVectors_int() {
        initialize("polynomialVectors(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Iterable<PolynomialVector> vs = QEP.polynomialVectors(i);
            simpleTest(i, vs, v -> v.dimension() == i);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                QEP.polynomialVectors(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPolynomialVectors() {
        initializeConstant("polynomialVectors()");
        biggerTest(QEP, QEP.polynomialVectors(), v -> true);
    }

    private void propertiesPolynomialVectorsAtLeast() {
        initialize("polynomialVectorsAtLeast(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Iterable<PolynomialVector> vs = QEP.polynomialVectorsAtLeast(i);
            simpleTest(i, vs, v -> v.dimension() >= i);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                QEP.polynomialVectorsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalPolynomialVectors_int() {
        initialize("rationalPolynomialVectors(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Iterable<RationalPolynomialVector> vs = QEP.rationalPolynomialVectors(i);
            simpleTest(i, vs, v -> v.dimension() == i);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                QEP.rationalPolynomialVectors(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalPolynomialVectors() {
        initializeConstant("rationalPolynomialVectors()");
        biggerTest(QEP, QEP.rationalPolynomialVectors(), v -> true);
    }

    private void propertiesRationalPolynomialVectorsAtLeast() {
        initialize("rationalPolynomialVectorsAtLeast(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Iterable<RationalPolynomialVector> vs = QEP.rationalPolynomialVectorsAtLeast(i);
            simpleTest(i, vs, v -> v.dimension() >= i);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                QEP.rationalPolynomialVectorsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMatrices_int_int() {
        initialize("matrices(int, int)");
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            Iterable<Matrix> ms = QEP.matrices(p.a, p.b);
            simpleTest(p, ms, n -> n.height() == p.a && n.width() == p.b);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.positiveIntegers()))) {
            try {
                QEP.matrices(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.positiveIntegers(), P.negativeIntegers()))) {
            try {
                QEP.matrices(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMatrices() {
        initializeConstant("matrices()");
        biggerTest(QEP, QEP.matrices(), m -> true);
    }

    private void propertiesSquareMatrices() {
        initializeConstant("squareMatrices()");
        biggerTest(QEP, QEP.squareMatrices(), Matrix::isSquare);
    }

    private void propertiesInvertibleMatrices() {
        initializeConstant("invertibleMatrices()");
        biggerTest(QEP, QEP.invertibleMatrices(), Matrix::isInvertible);
    }

    private void propertiesRationalMatrices_int_int() {
        initialize("rationalMatrices(int, int)");
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            Iterable<RationalMatrix> ms = QEP.rationalMatrices(p.a, p.b);
            simpleTest(p, ms, n -> n.height() == p.a && n.width() == p.b);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.positiveIntegers()))) {
            try {
                QEP.rationalMatrices(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.positiveIntegers(), P.negativeIntegers()))) {
            try {
                QEP.rationalMatrices(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalMatrices() {
        initializeConstant("rationalMatrices()");
        biggerTest(QEP, QEP.rationalMatrices(), m -> true);
    }

    private void propertiesSquareRationalMatrices() {
        initializeConstant("squareRationalMatrices()");
        biggerTest(QEP, QEP.squareRationalMatrices(), RationalMatrix::isSquare);
    }

    private void propertiesInvertibleRationalMatrices() {
        initializeConstant("invertibleRationalMatrices()");
        biggerTest(QEP, QEP.invertibleRationalMatrices(), RationalMatrix::isInvertible);
    }

    private void propertiesPolynomialMatrices_int_int() {
        initialize("polynomialMatrices(int, int)");
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            Iterable<PolynomialMatrix> ms = QEP.polynomialMatrices(p.a, p.b);
            simpleTest(p, ms, n -> n.height() == p.a && n.width() == p.b);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.positiveIntegers()))) {
            try {
                QEP.polynomialMatrices(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.positiveIntegers(), P.negativeIntegers()))) {
            try {
                QEP.polynomialMatrices(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPolynomialMatrices() {
        initializeConstant("polynomialMatrices()");
        biggerTest(QEP, QEP.polynomialMatrices(), m -> true);
    }

    private void propertiesSquarePolynomialMatrices() {
        initializeConstant("squarePolynomialMatrices()");
        biggerTest(QEP, QEP.squarePolynomialMatrices(), PolynomialMatrix::isSquare);
    }

    private void propertiesRationalPolynomialMatrices_int_int() {
        initialize("rationalPolynomialMatrices(int, int)");
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.naturalIntegersGeometric()))) {
            Iterable<RationalPolynomialMatrix> ms = QEP.rationalPolynomialMatrices(p.a, p.b);
            simpleTest(p, ms, n -> n.height() == p.a && n.width() == p.b);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.positiveIntegers()))) {
            try {
                QEP.rationalPolynomialMatrices(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.positiveIntegers(), P.negativeIntegers()))) {
            try {
                QEP.rationalPolynomialMatrices(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalPolynomialMatrices() {
        initializeConstant("rationalPolynomialMatrices()");
        biggerTest(QEP, QEP.rationalPolynomialMatrices(), m -> true);
    }

    private void propertiesSquareRationalPolynomialMatrices() {
        initializeConstant("squareRationalPolynomialMatrices()");
        biggerTest(QEP, QEP.squareRationalPolynomialMatrices(), RationalPolynomialMatrix::isSquare);
    }

    private void propertiesPolynomials_int() {
        initialize("polynomials(int)");
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.polynomials(i);
            simpleTest(i, ps, p -> p.degree() == i);
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.polynomials(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPolynomials() {
        initializeConstant("polynomials()");
        biggerTest(QEP, QEP.polynomials(), p -> true);
    }

    private void propertiesPolynomialsAtLeast() {
        initialize("polynomialsAtLeast(int)");
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.polynomialsAtLeast(i);
            simpleTest(i, ps, p -> p.degree() >= i);
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.polynomialsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPrimitivePolynomials_int() {
        initialize("primitivePolynomials(int)");
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.primitivePolynomials(i);
            simpleTest(i, ps, p -> p.degree() == i && p.isPrimitive());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.primitivePolynomials(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPrimitivePolynomials() {
        initializeConstant("primitivePolynomials()");
        biggerTest(QEP, QEP.primitivePolynomials(), Polynomial::isPrimitive);
    }

    private void propertiesPrimitivePolynomialsAtLeast() {
        initialize("primitivePolynomialsAtLeast(int)");
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.primitivePolynomialsAtLeast(i);
            simpleTest(i, ps, p -> p.degree() >= i && p.isPrimitive());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.primitivePolynomialsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPositivePrimitivePolynomials_int() {
        initialize("positivePrimitivePolynomials(int)");
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.positivePrimitivePolynomials(i);
            simpleTest(i, ps, p -> p.degree() == i && p.signum() == 1 && p.isPrimitive());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.positivePrimitivePolynomials(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPositivePrimitivePolynomials() {
        initializeConstant("positivePrimitivePolynomials()");
        biggerTest(QEP, QEP.positivePrimitivePolynomials(), p -> p.signum() == 1 && p.isPrimitive());
    }

    private void propertiesPositivePrimitivePolynomialsAtLeast() {
        initialize("positivePrimitivePolynomialsAtLeast(int)");
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.positivePrimitivePolynomialsAtLeast(i);
            simpleTest(i, ps, p -> p.degree() >= i && p.signum() == 1 && p.isPrimitive());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.positivePrimitivePolynomialsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMonicPolynomials_int() {
        initialize("monicPolynomials(int)");
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.monicPolynomials(i);
            simpleTest(i, ps, p -> p.degree() == i && p.isMonic());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.monicPolynomials(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMonicPolynomials() {
        initializeConstant("monicPolynomials()");
        biggerTest(QEP, QEP.monicPolynomials(), Polynomial::isMonic);
    }

    private void propertiesMonicPolynomialsAtLeast() {
        initialize("monicPolynomialsAtLeast(int)");
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.monicPolynomialsAtLeast(i);
            simpleTest(i, ps, p -> p.degree() >= i && p.isMonic());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.monicPolynomialsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesSquareFreePolynomials_int() {
        initialize("squareFreePolynomials(int)");
        for (int i : take(TINY_LIMIT, P.withScale(4).rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.squareFreePolynomials(i);
            simpleTest(i, ps, p -> p.degree() == i && p.isSquareFree());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.squareFreePolynomials(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesSquareFreePolynomials() {
        initializeConstant("squareFreePolynomials()");
        biggerTest(QEP, QEP.withScale(4).squareFreePolynomials(), Polynomial::isSquareFree);
    }

    private void propertiesSquareFreePolynomialsAtLeast() {
        initialize("squareFreePolynomialsAtLeast(int)");
        for (int i : take(TINY_LIMIT, P.withScale(4).rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.squareFreePolynomialsAtLeast(i);
            simpleTest(i, ps, p -> p.degree() >= i && p.isSquareFree());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.squareFreePolynomialsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPositivePrimitiveSquareFreePolynomials_int() {
        initialize("positivePrimitiveSquareFreePolynomials(int)");
        for (int i : take(TINY_LIMIT, P.withScale(4).rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.positivePrimitiveSquareFreePolynomials(i);
            simpleTest(i, ps, p -> p.degree() == i && p.signum() == 1 && p.isPrimitive() && p.isSquareFree());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.positivePrimitiveSquareFreePolynomials(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPositivePrimitiveSquareFreePolynomials() {
        initializeConstant("positivePrimitiveSquareFreePolynomials()");
        biggerTest(
                QEP,
                QEP.withScale(4).positivePrimitiveSquareFreePolynomials(),
                p -> p.signum() == 1 && p.isPrimitive() && p.isSquareFree()
        );
    }

    private void propertiesPositivePrimitiveSquareFreePolynomialsAtLeast() {
        initialize("positivePrimitiveSquareFreePolynomialsAtLeast(int)");
        for (int i : take(TINY_LIMIT, P.withScale(4).rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.positivePrimitiveSquareFreePolynomialsAtLeast(i);
            simpleTest(i, ps, p -> p.degree() >= i && p.signum() == 1 && p.isPrimitive() && p.isSquareFree());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.positivePrimitiveSquareFreePolynomialsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesIrreduciblePolynomials_int() {
        initialize("irreduciblePolynomials(int)");
        for (int i : take(TINY_LIMIT, P.withScale(4).rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.irreduciblePolynomials(i);
            simpleTest(i, ps, p -> p.degree() == i && p.isIrreducible());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.irreduciblePolynomials(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesIrreduciblePolynomials() {
        initializeConstant("irreduciblePolynomials()");
        biggerTest(QEP, QEP.withScale(4).irreduciblePolynomials(), Polynomial::isIrreducible);
    }

    private void propertiesIrreduciblePolynomialsAtLeast() {
        initialize("irreduciblePolynomialsAtLeast(int)");
        for (int i : take(TINY_LIMIT, P.withScale(1).rangeUpGeometric(-1))) {
            Iterable<Polynomial> ps = QEP.irreduciblePolynomialsAtLeast(i);
            simpleTest(i, ps, p -> p.degree() >= i && p.isIrreducible());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.irreduciblePolynomialsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalPolynomials_int() {
        initialize("rationalPolynomials(int)");
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            Iterable<RationalPolynomial> ps = QEP.rationalPolynomials(i);
            simpleTest(i, ps, p -> p.degree() == i);
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.rationalPolynomials(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalPolynomials() {
        initializeConstant("rationalPolynomials()");
        biggerTest(QEP, QEP.rationalPolynomials(), p -> true);
    }

    private void propertiesRationalPolynomialsAtLeast() {
        initialize("rationalPolynomialsAtLeast(int)");
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            Iterable<RationalPolynomial> ps = QEP.rationalPolynomialsAtLeast(i);
            simpleTest(i, ps, p -> p.degree() >= i);
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.rationalPolynomialsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMonicRationalPolynomials_int() {
        initialize("monicRationalPolynomials(int)");
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            Iterable<RationalPolynomial> ps = QEP.monicRationalPolynomials(i);
            simpleTest(i, ps, p -> p.degree() == i && p.isMonic());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.monicRationalPolynomials(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMonicRationalPolynomials() {
        initializeConstant("monicRationalPolynomials()");
        biggerTest(QEP, QEP.monicRationalPolynomials(), RationalPolynomial::isMonic);
    }

    private void propertiesMonicRationalPolynomialsAtLeast() {
        initialize("monicRationalPolynomialsAtLeast(int)");
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            Iterable<RationalPolynomial> ps = QEP.monicRationalPolynomialsAtLeast(i);
            simpleTest(i, ps, p -> p.degree() >= i && p.isMonic());
        }

        for (int i : take(LIMIT, P.rangeDown(-2))) {
            try {
                QEP.monicRationalPolynomialsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesVariables() {
        initializeConstant("variables()");
        biggerTest(QEP, QEP.variables(), v -> true);
    }

    private void propertiesMonomialOrders() {
        initializeConstant("monomialOrders()");
        biggerTest(QEP, QEP.monomialOrders(), o -> true);
    }

    private void propertiesExponentVectors() {
        initializeConstant("exponentVectors()");
        biggerTest(QEP, QEP.exponentVectors(), ev -> true);
    }

    private void propertiesExponentVectors_List_Variable() {
        initialize("exponentVectors(List<Variable>)");
        for (List<Variable> vs : take(LIMIT, P.subsets(P.variables()))) {
            Iterable<ExponentVector> evs = QEP.exponentVectors(vs);
            simpleTest(vs, evs, ev -> isSubsetOf(ev.variables(), vs));
        }

        for (List<Variable> vs : take(LIMIT, filterInfinite(us -> !increasing(us), P.lists(P.variables())))) {
            try {
                QEP.exponentVectors(vs);
                fail(vs);
            } catch (IllegalArgumentException ignored) {}
        }

        Iterable<List<Variable>> vsFail = filterInfinite(
                us -> increasing(filter(u -> u != null, us)),
                P.listsWithElement(null, P.variables())
        );
        for (List<Variable> vs : take(LIMIT, vsFail)) {
            try {
                QEP.exponentVectors(vs);
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesMultivariatePolynomials() {
        initializeConstant("multivariatePolynomials()");
        biggerTest(QEP, QEP.multivariatePolynomials(), p -> true);
    }

    private void propertiesMultivariatePolynomials_List_Variable() {
        initialize("multivariatePolynomials(List<Variable>)");
        for (List<Variable> vs : take(LIMIT, P.subsets(P.variables()))) {
            Iterable<MultivariatePolynomial> ps = QEP.multivariatePolynomials(vs);
            simpleTest(vs, ps, p -> isSubsetOf(p.variables(), vs));
        }

        for (List<Variable> vs : take(LIMIT, filterInfinite(us -> !increasing(us), P.lists(P.variables())))) {
            try {
                QEP.multivariatePolynomials(vs);
                fail(vs);
            } catch (IllegalArgumentException ignored) {}
        }

        Iterable<List<Variable>> vsFail = filterInfinite(
                us -> increasing(filter(u -> u != null, us)),
                P.listsWithElement(null, P.variables())
        );
        for (List<Variable> vs : take(LIMIT, vsFail)) {
            try {
                QEP.multivariatePolynomials(vs);
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesPositiveAlgebraics_int() {
        initialize("positiveAlgebraics(int)");
        for (int i : take(TINY_LIMIT, P.withScale(2).positiveIntegersGeometric())) {
            Iterable<Algebraic> xs = QEP.positiveAlgebraics(i);
            simpleTest(i, xs, x -> x.degree() == i && x.signum() == 1);
        }

        for (int i : take(LIMIT, P.withElement(0, P.negativeIntegers()))) {
            try {
                QEP.positiveAlgebraics(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPositiveAlgebraics() {
        initializeConstant("positiveAlgebraics()");
        simpleTest(QEP, QEP.positiveAlgebraics(), x -> x.signum() == 1);
    }

    private void propertiesNegativeAlgebraics_int() {
        initialize("negativeAlgebraics(int)");
        for (int i : take(TINY_LIMIT, P.withScale(2).positiveIntegersGeometric())) {
            Iterable<Algebraic> xs = QEP.negativeAlgebraics(i);
            simpleTest(i, xs, x -> x.degree() == i && x.signum() == -1);
        }

        for (int i : take(LIMIT, P.withElement(0, P.negativeIntegers()))) {
            try {
                QEP.negativeAlgebraics(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesNegativeAlgebraics() {
        initializeConstant("negativeAlgebraics()");
        simpleTest(QEP, QEP.negativeAlgebraics(), x -> x.signum() == -1);
    }

    private void propertiesNonzeroAlgebraics_int() {
        initialize("nonzeroAlgebraics(int)");
        for (int i : take(TINY_LIMIT, P.withScale(2).positiveIntegersGeometric())) {
            Iterable<Algebraic> xs = QEP.nonzeroAlgebraics(i);
            simpleTest(i, xs, x -> x.degree() == i && x != Algebraic.ZERO);
        }

        for (int i : take(LIMIT, P.withElement(0, P.negativeIntegers()))) {
            try {
                QEP.nonzeroAlgebraics(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesNonzeroAlgebraics() {
        initializeConstant("nonzeroAlgebraics()");
        simpleTest(QEP, QEP.nonzeroAlgebraics(), x -> x != Algebraic.ZERO);
    }

    private void propertiesAlgebraics_int() {
        initialize("algebraics(int)");
        for (int i : take(TINY_LIMIT, P.withScale(2).positiveIntegersGeometric())) {
            Iterable<Algebraic> xs = QEP.nonzeroAlgebraics(i);
            simpleTest(i, xs, x -> x.degree() == i);
        }

        for (int i : take(LIMIT, P.withElement(0, P.negativeIntegers()))) {
            try {
                QEP.algebraics(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAlgebraics() {
        initializeConstant("algebraics()");
        simpleTest(QEP, QEP.algebraics(), x -> true);
    }

    private void propertiesNonNegativeAlgebraicsLessThanOne_int() {
        initialize("nonNegativeAlgebraicsLessThanOne(int)");
        for (int i : take(TINY_LIMIT / 2, P.withScale(2).positiveIntegersGeometric())) {
            Iterable<Algebraic> xs = QEP.nonNegativeAlgebraicsLessThanOne(i);
            simpleTest(i, xs, x -> x.degree() == i && x.signum() != -1 && lt(x, Algebraic.ONE));
        }

        for (int i : take(LIMIT, P.withElement(0, P.negativeIntegers()))) {
            try {
                QEP.nonNegativeAlgebraicsLessThanOne(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesNonNegativeAlgebraicsLessThanOne() {
        initializeConstant("nonNegativeAlgebraicsLessThanOne()");
        simpleTest(QEP, QEP.nonNegativeAlgebraicsLessThanOne(), x -> x.signum() != -1 && lt(x, Algebraic.ONE));
    }

    private void propertiesRangeUp_int_Algebraic() {
        initialize("rangeUp(int, Algebraic)");
        Iterable<Pair<Algebraic, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).algebraics(),
                P.withScale(2).positiveIntegersGeometric()
        );
        for (Pair<Algebraic, Integer> p : take(MEDIUM_LIMIT, ps)) {
            Iterable<Algebraic> xs = QEP.rangeUp(p.b, p.a);
            simpleTest(p, xs, y -> y.degree() == p.b && ge(y, p.a));
        }

        for (Pair<Integer, Algebraic> p : take(LIMIT, P.pairs(P.rangeDown(0), P.algebraics()))) {
            try {
                QEP.rangeUp(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRangeUp_Algebraic() {
        initialize("rangeUp(Algebraic)");
        for (Algebraic x : take(MEDIUM_LIMIT, P.withScale(4).algebraics())) {
            Iterable<Algebraic> xs = QEP.rangeUp(x);
            simpleTest(x, xs, y -> ge(y, x));
        }
    }

    private void propertiesRangeDown_int_Algebraic() {
        initialize("rangeDown(int, Algebraic)");
        Iterable<Pair<Algebraic, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).algebraics(),
                P.withScale(2).positiveIntegersGeometric()
        );
        for (Pair<Algebraic, Integer> p : take(MEDIUM_LIMIT, ps)) {
            Iterable<Algebraic> xs = QEP.rangeDown(p.b, p.a);
            simpleTest(p, xs, y -> y.degree() == p.b && le(y, p.a));
        }

        for (Pair<Integer, Algebraic> p : take(LIMIT, P.pairs(P.rangeDown(0), P.algebraics()))) {
            try {
                QEP.rangeDown(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRangeDown_Algebraic() {
        initialize("rangeDown(Algebraic)");
        for (Algebraic x : take(MEDIUM_LIMIT, P.withScale(4).algebraics())) {
            Iterable<Algebraic> xs = QEP.rangeDown(x);
            simpleTest(x, xs, y -> le(y, x));
        }
    }

    //todo
    private void propertiesRange_Algebraic_Algebraic() {
        initialize("range(Algebraic, Algebraic)");
        for (Pair<Algebraic, Algebraic> p : take(MEDIUM_LIMIT, P.bagPairs(P.withScale(4).algebraics()))) {
            Iterable<Algebraic> xs = QEP.range(p.a, p.b);
            simpleTest(p, xs, x -> ge(x, p.a) && le(x, p.b));
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            aeqit(x, QEP.range(x, x), Collections.singletonList(x));
        }
    }
}
