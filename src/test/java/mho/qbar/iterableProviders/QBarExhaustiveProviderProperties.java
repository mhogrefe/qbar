package mho.qbar.iterableProviders;

import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class QBarExhaustiveProviderProperties {
    private static final QBarExhaustiveProvider EP = QBarExhaustiveProvider.INSTANCE;
    private static final int LARGE_LIMIT = 10000;
    private static final int TINY_LIMIT = 20;
    private static int LIMIT;
    private static QBarIterableProvider P;

    private static void initializeConstant(String name) {
        System.out.println("\ttesting " + name + " properties...");
    }

    private static void initialize(String name) {
        P.reset();
        System.out.print('\t');
        initializeConstant(name);
    }

    @Test
    public void testAllProperties() {
        System.out.println("QBarExhaustiveProvider properties");
        propertiesPositiveRationals();
        propertiesNegativeRationals();
        propertiesNonzeroRationals();
        propertiesRationals();
        propertiesNonNegativeRationalsLessThanOne();
        propertiesFinitelyBoundedIntervals();
        propertiesIntervals();
        List<Triple<QBarIterableProvider, Integer, String>> configs = new ArrayList<>();
        configs.add(new Triple<>(QBarExhaustiveProvider.INSTANCE, 10000, "exhaustively"));
        configs.add(new Triple<>(QBarRandomProvider.example(), 1000, "randomly"));
        for (Triple<QBarIterableProvider, Integer, String> config : configs) {
            P = config.a;
            LIMIT = config.b;
            System.out.println("\ttesting " + config.c);
            propertiesRangeUp_Rational();
            propertiesRangeDown_Rational();
            propertiesRange_Rational_Rational();
        }
        System.out.println("Done");
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

    private static void propertiesPositiveRationals() {
        initializeConstant("positiveRationals()");
        biggerTest(EP, EP.positiveRationals(), r -> r.signum() == 1);
        take(TINY_LIMIT, EP.positiveRationals()).forEach(Rational::validate);
    }

    private static void propertiesNegativeRationals() {
        initializeConstant("negativeRationals()");
        biggerTest(EP, EP.negativeRationals(), r -> r.signum() == -1);
        take(TINY_LIMIT, EP.negativeRationals()).forEach(Rational::validate);
    }

    private static void propertiesNonzeroRationals() {
        initializeConstant("nonzeroRationals()");
        biggerTest(EP, EP.nonzeroRationals(), r -> r != Rational.ZERO);
        take(TINY_LIMIT, EP.nonzeroRationals()).forEach(Rational::validate);
    }

    private static void propertiesRationals() {
        initializeConstant("rationals()");
        biggerTest(EP, EP.rationals(), r -> true);
        take(TINY_LIMIT, EP.rationals()).forEach(Rational::validate);
    }

    private static void propertiesNonNegativeRationalsLessThanOne() {
        initializeConstant("nonNegativeRationalsLessThanOne()");
        biggerTest(EP, EP.nonNegativeRationalsLessThanOne(), r -> r.signum() != -1 && lt(r, Rational.ONE));
        take(TINY_LIMIT, EP.nonNegativeRationalsLessThanOne()).forEach(Rational::validate);
    }

    private static void propertiesRangeUp_Rational() {
        initialize("rangeUp(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Iterable<Rational> rs = EP.rangeUp(r);
            simpleTest(r, rs, s -> ge(s, r));
            take(TINY_LIMIT, rs).forEach(Rational::validate);
        }
    }

    private static void propertiesRangeDown_Rational() {
        initialize("rangeDown(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Iterable<Rational> rs = EP.rangeDown(r);
            simpleTest(r, rs, s -> le(s, r));
            take(TINY_LIMIT, rs).forEach(Rational::validate);
        }
    }

    private static void propertiesRange_Rational_Rational() {
        initialize("range(Rational, Rational)");
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            Iterable<Rational> rs = EP.range(p.a, p.b);
            simpleTest(p, rs, r -> ge(r, p.a) && le(r, p.b));
            assertEquals(p, gt(p.a, p.b), isEmpty(rs));
            take(TINY_LIMIT, rs).forEach(Rational::validate);
            if (ge(p.a, p.b)) {
                testHasNext(rs);
            }
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            aeqit(r, EP.range(r, r), Collections.singletonList(r));
        }
    }

    private static void propertiesFinitelyBoundedIntervals() {
        initializeConstant("finitelyBoundedIntervals()");
        biggerTest(EP, EP.finitelyBoundedIntervals(), Interval::isFinitelyBounded);
        take(TINY_LIMIT, EP.finitelyBoundedIntervals()).forEach(Interval::validate);
    }

    private static void propertiesIntervals() {
        initializeConstant("intervals()");
        biggerTest(EP, EP.intervals(), a -> true);
        take(TINY_LIMIT, EP.intervals()).forEach(Interval::validate);
    }
}
