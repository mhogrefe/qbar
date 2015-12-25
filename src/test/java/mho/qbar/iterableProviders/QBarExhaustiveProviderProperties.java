package mho.qbar.iterableProviders;

import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import mho.qbar.objects.RationalVector;
import mho.qbar.testing.QBarTestProperties;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
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
        propertiesRationalVectors();
        propertiesReducedRationalVectors();
    }

    @Override
    protected void testBothModes() {
        propertiesRangeUp_Rational();
        propertiesRangeDown_Rational();
        propertiesRange_Rational_Rational();
        propertiesRationalsIn();
        propertiesRationalsNotIn();
        propertiesRationalVectors_int();
        propertiesRationalVectorsAtLeast();
        propertiesReducedRationalVectors_int();
        propertiesReducedRationalVectorsAtLeast();
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
        take(TINY_LIMIT, QEP.positiveRationals()).forEach(Rational::validate);
    }

    private void propertiesNegativeRationals() {
        initializeConstant("negativeRationals()");
        biggerTest(QEP, QEP.negativeRationals(), r -> r.signum() == -1);
        take(TINY_LIMIT, QEP.negativeRationals()).forEach(Rational::validate);
    }

    private void propertiesNonzeroRationals() {
        initializeConstant("nonzeroRationals()");
        biggerTest(QEP, QEP.nonzeroRationals(), r -> r != Rational.ZERO);
        take(TINY_LIMIT, QEP.nonzeroRationals()).forEach(Rational::validate);
    }

    private void propertiesRationals() {
        initializeConstant("rationals()");
        biggerTest(QEP, QEP.rationals(), r -> true);
        take(TINY_LIMIT, QEP.rationals()).forEach(Rational::validate);
    }

    private void propertiesNonNegativeRationalsLessThanOne() {
        initializeConstant("nonNegativeRationalsLessThanOne()");
        biggerTest(QEP, QEP.nonNegativeRationalsLessThanOne(), r -> r.signum() != -1 && lt(r, Rational.ONE));
        take(TINY_LIMIT, QEP.nonNegativeRationalsLessThanOne()).forEach(Rational::validate);
    }

    private void propertiesRangeUp_Rational() {
        initialize("rangeUp(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Iterable<Rational> rs = QEP.rangeUp(r);
            simpleTest(r, rs, s -> ge(s, r));
            take(TINY_LIMIT, rs).forEach(Rational::validate);
        }
    }

    private void propertiesRangeDown_Rational() {
        initialize("rangeDown(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Iterable<Rational> rs = QEP.rangeDown(r);
            simpleTest(r, rs, s -> le(s, r));
            take(TINY_LIMIT, rs).forEach(Rational::validate);
        }
    }

    private void propertiesRange_Rational_Rational() {
        initialize("range(Rational, Rational)");
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            Iterable<Rational> rs = QEP.range(p.a, p.b);
            simpleTest(p, rs, r -> ge(r, p.a) && le(r, p.b));
            assertEquals(p, gt(p.a, p.b), isEmpty(rs));
            take(TINY_LIMIT, rs).forEach(Rational::validate);
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
        take(TINY_LIMIT, QEP.finitelyBoundedIntervals()).forEach(Interval::validate);
    }

    private void propertiesIntervals() {
        initializeConstant("intervals()");
        biggerTest(QEP, QEP.intervals(), a -> true);
        take(TINY_LIMIT, QEP.intervals()).forEach(Interval::validate);
    }

    private void propertiesRationalsIn() {
        initialize("rationalsIn(Interval)");
        for (Interval a : take(LIMIT, P.intervals())) {
            Iterable<Rational> rs = QEP.rationalsIn(a);
            simpleTest(a, rs, a::contains);
            take(TINY_LIMIT, rs).forEach(Rational::validate);
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
            take(TINY_LIMIT, rs).forEach(Rational::validate);
        }
    }

    private void propertiesRationalVectors_int() {
        initialize("rationalVectors(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Iterable<RationalVector> vs = QEP.rationalVectors(i);
            simpleTest(i, vs, v -> v.dimension() == i);
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
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
        biggerTest(QEP, QEP.rationalVectors(), r -> true);
        take(TINY_LIMIT, QEP.rationalVectors()).forEach(RationalVector::validate);
    }

    private void propertiesRationalVectorsAtLeast() {
        initialize("rationalVectorsAtLeast(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Iterable<RationalVector> vs = QEP.rationalVectorsAtLeast(i);
            simpleTest(i, vs, v -> v.dimension() >= i);
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
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
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
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
        take(TINY_LIMIT, QEP.reducedRationalVectors()).forEach(RationalVector::validate);
    }

    private void propertiesReducedRationalVectorsAtLeast() {
        initialize("reducedRationalVectorsAtLeast(int)");
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            Iterable<RationalVector> vs = QEP.reducedRationalVectorsAtLeast(i);
            simpleTest(i, vs, v -> v.isReduced() && v.dimension() >= i);
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                QEP.reducedRationalVectorsAtLeast(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }
}
