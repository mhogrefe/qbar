package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.math.Combinatorics;
import mho.wheels.misc.BigDecimalUtils;
import mho.wheels.misc.FloatingPointUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static mho.qbar.objects.Interval.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("ConstantConditions")
public class IntervalProperties {
    private static boolean USE_RANDOM;
    private static final String INTERVAL_CHARS = " (),-/0123456789I[]finty";
    private static final int TINY_LIMIT = 10;
    private static int LIMIT;

    private static QBarIterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = new QBarRandomProvider(new Random(0x6af477d9a7e54fcaL));
            LIMIT = 1000;
        } else {
            P = QBarExhaustiveProvider.INSTANCE;
            LIMIT = 10000;
        }
    }

    @Test
    public void testAllProperties() {
        System.out.println("Interval properties");
        for (boolean useRandom : Arrays.asList(false, true)) {
            System.out.println("\ttesting " + (useRandom ? "randomly" : "exhaustively"));
            USE_RANDOM = useRandom;
            propertiesGetLower();
            propertiesGetUpper();
            propertiesOf_Rational_Rational();
            propertiesLessThanOrEqualTo();
            propertiesGreaterThanOrEqualTo();
            propertiesOf_Rational();
            propertiesIsFinitelyBounded();
            propertiesContains_Rational();
            propertiesContains_Interval();
            propertiesDiameter();
            propertiesConvexHull_Interval();
            propertiesConvexHull_List_Interval();
            compareImplementationsConvexHull_List_Interval();
            propertiesIntersection();
            propertiesDisjoint();
            propertiesMakeDisjoint();
            propertiesComplement();
            propertiesMidpoint();
            propertiesSplit();
            propertiesBisect();
            propertiesRoundingPreimage_float();
            propertiesRoundingPreimage_double();
            propertiesRoundingPreimage_BigDecimal();
            propertiesFloatRange();
            propertiesDoubleRange();
            propertiesAdd();
            propertiesNegate();
            propertiesAbs();
            propertiesSignum();
            propertiesSubtract();
            propertiesMultiply_Interval();
            propertiesEquals();
            propertiesHashCode();
            propertiesCompareTo();
            propertiesRead();
            propertiesFindIn();
            propertiesToString();
        }
        System.out.println("Done");
    }

    private static void propertiesGetLower() {
        initialize();
        System.out.println("\t\ttesting getLower() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            a.getLower();
        }

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            assertEquals(a.toString(), of(a.getLower().get(), a.getUpper().get()), a);
        }
    }

    private static void propertiesGetUpper() {
        initialize();
        System.out.println("\t\ttesting getUpper() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            a.getUpper();
        }
    }

    private static void propertiesOf_Rational_Rational() {
        initialize();
        System.out.println("\t\ttesting of(Rational, Rational) properties...");

        Iterable<Pair<Rational, Rational>> ps = filter(q -> le(q.a, q.b), P.pairs(P.rationals()));
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            Interval a = of(p.a, p.b);
            validate(a);
            assertTrue(a.toString(), a.isFinitelyBounded());
        }
    }

    private static void propertiesLessThanOrEqualTo() {
        initialize();
        System.out.println("\t\ttesting lessThanOrEqualTo(Rational) properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            Interval a = lessThanOrEqualTo(r);
            validate(a);
            assertFalse(a.toString(), a.getLower().isPresent());
            assertTrue(a.toString(), a.getUpper().isPresent());
            for (Rational s : take(TINY_LIMIT, P.rationals(a))) {
                assertTrue(r.toString(), le(s, r));
            }
        }
    }

    private static void propertiesGreaterThanOrEqualTo() {
        initialize();
        System.out.println("\t\ttesting greaterThanOrEqualTo(Rational) properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            Interval a = greaterThanOrEqualTo(r);
            validate(a);
            assertTrue(a.toString(), a.getLower().isPresent());
            assertFalse(a.toString(), a.getUpper().isPresent());
            for (Rational s : take(TINY_LIMIT, P.rationals(a))) {
                assertTrue(r.toString(), ge(s, r));
            }
        }
    }

    private static void propertiesOf_Rational() {
        initialize();
        System.out.println("\t\ttesting of(Rational) properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            Interval a = of(r);
            validate(a);
            assertTrue(a.toString(), a.isFinitelyBounded());
            assertEquals(a.toString(), a.diameter().get(), Rational.ZERO);
        }
    }

    private static void propertiesIsFinitelyBounded() {
        initialize();
        System.out.println("\t\ttesting isFinitelyBounded() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            a.isFinitelyBounded();
        }

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            assertTrue(a.toString(), a.isFinitelyBounded());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertFalse(r.toString(), lessThanOrEqualTo(r).isFinitelyBounded());
            assertFalse(r.toString(), greaterThanOrEqualTo(r).isFinitelyBounded());
        }
    }

    private static void propertiesContains_Rational() {
        initialize();
        System.out.println("\t\ttesting contains(Rational) properties...");

        for (Pair<Interval, Rational> p : take(LIMIT, P.pairs(P.intervals(), P.rationals()))) {
            p.a.contains(p.b);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(r.toString(), ALL.contains(r));
            assertTrue(r.toString(), of(r).contains(r));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, filter(q -> !q.a.equals(q.b), P.pairs(P.rationals())))) {
            assertFalse(p.toString(), of(p.a).contains(p.b));
        }
    }

    private static void propertiesContains_Interval() {
        initialize();
        System.out.println("\t\ttesting contains(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            p.a.contains(p.b);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a.toString(), ALL.contains(a));
            assertTrue(a.toString(), a.contains(a));
        }

        for (Pair<Interval, Interval> p : take(LIMIT, filter(q -> q.a.contains(q.b), P.pairs(P.intervals())))) {
            Optional<Rational> ad = p.a.diameter();
            Optional<Rational> bd = p.b.diameter();
            assertTrue(p.toString(), !ad.isPresent() || le(bd.get(), ad.get()));
            assertTrue(p.toString(), p.a.convexHull(p.b).equals(p.a));
        }

        Iterable<Pair<Rational, Interval>> ps = filter(
                q -> !q.b.equals(of(q.a)),
                P.pairs(P.rationals(), P.intervals())
        );
        for (Pair<Rational, Interval> p : take(LIMIT, ps)) {
            assertFalse(p.toString(), of(p.a).contains(p.b));
        }
    }

    private static void propertiesDiameter() {
        initialize();
        System.out.println("\t\ttesting diameter() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            a.diameter();
        }

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            assertTrue(a.toString(), a.diameter().get().signum() != -1);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertFalse(r.toString(), lessThanOrEqualTo(r).diameter().isPresent());
            assertFalse(r.toString(), greaterThanOrEqualTo(r).diameter().isPresent());
        }
    }

    private static void propertiesConvexHull_Interval() {
        initialize();
        System.out.println("\t\ttesting convexHull(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Interval c = p.a.convexHull(p.b);
            validate(c);
            assertEquals(p.toString(), c, p.b.convexHull(p.a));
            //Given an interval c whose endpoints are in each of two intervals a and b, any Rational in c lies in the
            //convex hull of a and b
            for (Pair<Rational, Rational> q : take(TINY_LIMIT, P.pairs(P.rationals(p.a), P.rationals(p.b)))) {
                for (Rational r : take(TINY_LIMIT, P.rationals(of(min(q.a, q.b), max(q.a, q.b))))) {
                    assertTrue(p.toString(), c.contains(r));
                }
            }
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.finitelyBoundedIntervals()))) {
            Interval c = p.a.convexHull(p.b);
            assertTrue(p.toString(), ge(c.diameter().get(), p.a.diameter().get()));
            assertTrue(p.toString(), ge(c.diameter().get(), p.b.diameter().get()));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a.toString(), a.convexHull(a).equals(a));
            assertTrue(a.toString(), ALL.convexHull(a).equals(ALL));
        }
    }

    private static @NotNull Pair<Rational, Rational> convexHull_List_Interval_alt(@NotNull List<Interval> as) {
        if (isEmpty(as))
            throw new IllegalArgumentException("cannot take convex hull of empty set");
        Rational lower = minimum(
                (x, y) -> {
                    if (x == null) return -1;
                    if (y == null) return 1;
                    return x.compareTo(y);
                },
                map(a -> a.getLower().isPresent() ? a.getLower().get() : null, as)
        );
        Rational upper = maximum(
                (x, y) -> {
                    if (x == null) return 1;
                    if (y == null) return -1;
                    return x.compareTo(y);
                },
                map(a -> a.getUpper().isPresent() ? a.getUpper().get() : null, as)
        );
        return new Pair<>(lower, upper);
    }

    private static void propertiesConvexHull_List_Interval() {
        initialize();
        System.out.println("\t\ttesting convexHull(List<Interval>) properties...");

        for (List<Interval> as : take(LIMIT, P.listsAtLeast(1, P.intervals()))) {
            Interval c = convexHull(as);
            Pair<Rational, Rational> altC = convexHull_List_Interval_alt(as);
            Interval alt;
            if (altC.a == null && altC.b == null) {
                alt = ALL;
            } else if (altC.a == null) {
                alt = lessThanOrEqualTo(altC.b);
            } else if (altC.b == null) {
                alt = greaterThanOrEqualTo(altC.a);
            } else {
                alt = of(altC.a, altC.b);
            }
            assertEquals(as.toString(), alt, c);
            validate(c);
        }

        for (List<Interval> as : take(LIMIT, P.listsAtLeast(1, P.finitelyBoundedIntervals()))) {
            Interval c = convexHull(as);
            Rational cd = c.diameter().get();
            assertTrue(as.toString(), all(a -> ge(cd, a.diameter().get()), as));
        }

        Iterable<Pair<List<Interval>, List<Interval>>> ps = P.dependentPairsLogarithmic(
                P.listsAtLeast(1, P.intervals()),
                Combinatorics::permutationsIncreasing
        );
        for (Pair<List<Interval>, List<Interval>> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), convexHull(p.a), convexHull(p.b));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a.toString(), convexHull(Arrays.asList(a)), a);
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            assertEquals(p.toString(), convexHull(Arrays.asList(p.a, p.b)), p.a.convexHull(p.b));
        }

        Iterable<Pair<Interval, Integer>> ps2;
        if (P instanceof ExhaustiveProvider) {
            ps2 = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.intervals(), P.positiveIntegers());
        } else {
            ps2 = P.pairs(P.intervals(), ((RandomProvider) P).positiveIntegersGeometric(20));
        }
        for (Pair<Interval, Integer> p : take(LIMIT, ps2)) {
            assertEquals(p.toString(), convexHull(toList(replicate(p.b, p.a))), p.a);
        }

        Iterable<Pair<List<Interval>, Integer>> ps3 = P.dependentPairsLogarithmic(
                P.lists(P.intervals()),
                as -> P.range(0, as.size())
        );
        for (Pair<List<Interval>, Integer> p : take(LIMIT, ps3)) {
            List<Interval> as = toList(insert(p.a, p.b, ALL));
            assertEquals(p.toString(), convexHull(as), ALL);
        }

        for (Pair<List<Interval>, Integer> p : take(LIMIT, ps3)) {
            List<Interval> as = toList(insert(p.a, p.b, null));
            try {
                convexHull(as);
                fail(p.toString());
            } catch (NullPointerException ignored) {}
        }
    }

    private static void compareImplementationsConvexHull_List_Interval() {
        initialize();
        System.out.println("\t\tcomparing convexHull(List<Interval>) implementations...");

        long totalTime = 0;
        for (List<Interval> as : take(LIMIT, P.listsAtLeast(1, P.intervals()))) {
            long time = System.nanoTime();
            convexHull_List_Interval_alt(as);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\talt: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (List<Interval> as : take(LIMIT, P.listsAtLeast(1, P.intervals()))) {
            long time = System.nanoTime();
            convexHull(as);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static void propertiesIntersection() {
        initialize();
        System.out.println("\t\ttesting intersection(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Optional<Interval> oi = p.a.intersection(p.b);
            if (oi.isPresent()) {
                validate(oi.get());
            }
            assertEquals(p.toString(), oi, p.b.intersection(p.a));
            assertEquals(p.toString(), oi.isPresent(), !p.a.disjoint(p.b));
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.finitelyBoundedIntervals()))) {
            Optional<Interval> oi = p.a.intersection(p.b);
            if (oi.isPresent()) {
                assertTrue(p.toString(), le(oi.get().diameter().get(), p.a.diameter().get()));
                assertTrue(p.toString(), le(oi.get().diameter().get(), p.b.diameter().get()));
            }
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a.toString(), ALL.intersection(a).get(), a);
            assertEquals(a.toString(), a.intersection(a).get(), a);
        }

        for (Pair<Interval, Interval> p : take(LIMIT, filter(q -> !q.a.disjoint(q.b), P.pairs(P.intervals())))) {
            Interval intersection = p.a.intersection(p.b).get();
            for (Rational r : take(TINY_LIMIT, P.rationals(intersection))) {
                assertTrue(p.toString(), p.a.contains(r));
                assertTrue(p.toString(), p.b.contains(r));
            }
        }
    }

    private static void propertiesDisjoint() {
        initialize();
        System.out.println("\t\ttesting disjoint(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            boolean disjoint = p.a.disjoint(p.b);
            assertEquals(p.toString(), p.b.disjoint(p.a), disjoint);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertFalse(ALL.disjoint(a));
            assertFalse(a.disjoint(a));
        }

        for (Pair<Interval, Interval> p : take(LIMIT, filter(q -> q.a.disjoint(q.b), P.pairs(P.intervals())))) {
            for (Rational r : take(TINY_LIMIT, P.rationals(p.a))) {
                assertFalse(p.toString(), p.b.contains(r));
            }
        }
    }

    private static void propertiesMakeDisjoint() {
        initialize();
        System.out.println("\t\ttesting makeDisjoint(List<Interval>) properties...");

        for (List<Interval> as : take(LIMIT, P.lists(P.intervals()))) {
            List<Interval> disjoint = makeDisjoint(as);
            disjoint.forEach(mho.qbar.objects.IntervalProperties::validate);
            assertTrue(as.toString(), nondecreasing(disjoint));
            assertTrue(as.toString(), and(map(p -> p.a.disjoint(p.b), Combinatorics.distinctPairs(disjoint))));
            for (Rational r : take(TINY_LIMIT, mux(toList(map(P::rationals, as))))) {
                assertTrue(as.toString(), or(map(a -> a.contains(r), disjoint)));
            }
        }

        Iterable<Pair<List<Interval>, List<Interval>>> ps = P.dependentPairsLogarithmic(
                P.lists(P.intervals()),
                Combinatorics::permutationsIncreasing
        );
        for (Pair<List<Interval>, List<Interval>> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), makeDisjoint(p.a), makeDisjoint(p.b));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a.toString(), makeDisjoint(Arrays.asList(a)), Arrays.asList(a));
        }

        for (Pair<Interval, Interval> p : take(LIMIT, filter(q -> q.a.disjoint(q.b), P.pairs(P.intervals())))) {
            assertEquals(p.toString(), makeDisjoint(Pair.toList(p)), sort(Pair.toList(p)));
        }

        for (Pair<Interval, Interval> p : take(LIMIT, filter(q -> !q.a.disjoint(q.b), P.pairs(P.intervals())))) {
            assertEquals(p.toString(), makeDisjoint(Pair.toList(p)), Arrays.asList(convexHull(Pair.toList(p))));
        }

        Iterable<Pair<List<Interval>, Integer>> ps2 = P.dependentPairsLogarithmic(
                P.lists(P.intervals()),
                as -> P.range(0, as.size())
        );
        for (Pair<List<Interval>, Integer> p : take(LIMIT, ps2)) {
            List<Interval> as = toList(insert(p.a, p.b, ALL));
            assertEquals(p.toString(), makeDisjoint(as), Arrays.asList(ALL));
        }

        for (Pair<List<Interval>, Integer> p : take(LIMIT, ps2)) {
            List<Interval> as = toList(insert(p.a, p.b, null));
            try {
                convexHull(as);
                fail(p.toString());
            } catch (NullPointerException ignored) {}
        }
    }

    private static void propertiesComplement() {
        initialize();
        System.out.println("\t\ttesting complement() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            List<Interval> complement = a.complement();
            complement.forEach(mho.qbar.objects.IntervalProperties::validate);
            assertEquals(a.toString(), makeDisjoint(complement), complement);
            List<Rational> endpoints = new ArrayList<>();
            if (a.getLower().isPresent()) endpoints.add(a.getLower().get());
            if (a.getUpper().isPresent()) endpoints.add(a.getUpper().get());
            for (Rational endpoint : endpoints) {
                any(b -> b.contains(endpoint), complement);
            }
            for (Rational r : take(TINY_LIMIT, filter(s -> !endpoints.contains(s), P.rationals(a)))) {
                assertFalse(a.toString(), any(b -> b.contains(r), complement));
            }
            for (Rational r : take(TINY_LIMIT, P.rationalsNotIn(a))) {
                assertTrue(a.toString(), any(b -> b.contains(r), complement));
            }
        }

        for (Interval a : take(LIMIT, map(Interval::lessThanOrEqualTo, P.rationals()))) {
            List<Interval> complement = a.complement();
            assertEquals(a.toString(), complement.size(), 1);
            Interval x = complement.get(0);
            assertTrue(a.toString(), x.getLower().isPresent());
            assertFalse(a.toString(), x.getUpper().isPresent());
        }

        for (Interval a : take(LIMIT, map(Interval::greaterThanOrEqualTo, P.rationals()))) {
            List<Interval> complement = a.complement();
            assertEquals(a.toString(), complement.size(), 1);
            Interval x = complement.get(0);
            assertFalse(a.toString(), x.getLower().isPresent());
            assertTrue(a.toString(), x.getUpper().isPresent());
        }

        for (Interval a : take(LIMIT, map(Interval::of, P.rationals()))) {
            assertEquals(a.toString(), a.complement(), Arrays.asList(ALL));
        }

        Iterable<Interval> as = filter(b -> b.diameter().get() != Rational.ZERO, P.finitelyBoundedIntervals());
        for (Interval a : take(LIMIT, as)) {
            List<Interval> complement = a.complement();
            assertEquals(a.toString(), complement.size(), 2);
            Interval x = complement.get(0);
            Interval y = complement.get(1);
            assertFalse(a.toString(), x.getLower().isPresent());
            assertTrue(a.toString(), x.getUpper().isPresent());
            assertTrue(a.toString(), y.getLower().isPresent());
            assertFalse(a.toString(), y.getUpper().isPresent());
        }
    }

    private static void propertiesMidpoint() {
        initialize();
        System.out.println("\t\ttesting midpoint() properties...");

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            Rational midpoint = a.midpoint();
            assertEquals(a.toString(), midpoint.subtract(a.getLower().get()), a.getUpper().get().subtract(midpoint));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), of(r).midpoint(), r);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            try {
                lessThanOrEqualTo(r).midpoint();
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
            try {
                greaterThanOrEqualTo(r).midpoint();
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesSplit() {
        initialize();
        System.out.println("\t\ttesting split(Rational) properties...");

        Iterable<Pair<Interval, Rational>> ps = filter(q -> q.a.contains(q.b), P.pairs(P.intervals(), P.rationals()));
        for (Pair<Interval, Rational> p : take(LIMIT, ps)) {
            Pair<Interval, Interval> split = p.a.split(p.b);
            validate(split.a);
            validate(split.b);
            assertEquals(p.toString(), split.a.getUpper().get(), p.b);
            assertEquals(p.toString(), split.b.getLower().get(), p.b);
            for (Rational r : take(TINY_LIMIT, P.rationals(p.a))) {
                assertTrue(p.toString(), split.a.contains(r) || split.b.contains(r));
            }
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            Interval a = of(r);
            assertEquals(r.toString(), a.split(r), new Pair<>(a, a));

            Pair<Interval, Interval> p = ALL.split(r);
            assertFalse(r.toString(), p.a.getLower().isPresent());
            assertTrue(r.toString(), p.a.getUpper().isPresent());
            assertTrue(r.toString(), p.b.getLower().isPresent());
            assertFalse(r.toString(), p.b.getUpper().isPresent());
        }

        for (Pair<Rational, Rational> p : take(LIMIT, filter(q -> le(q.a, q.b), P.pairs(P.rationals())))) {
            Interval a = lessThanOrEqualTo(p.b);
            Pair<Interval, Interval> q = a.split(p.a);
            assertFalse(p.toString(), q.a.getLower().isPresent());
            assertTrue(p.toString(), q.a.getUpper().isPresent());
            assertTrue(p.toString(), q.b.isFinitelyBounded());
        }

        for (Pair<Rational, Rational> p : take(LIMIT, filter(q -> le(q.a, q.b), P.pairs(P.rationals())))) {
            Interval a = greaterThanOrEqualTo(p.a);
            Pair<Interval, Interval> q = a.split(p.b);
            assertTrue(p.toString(), q.a.isFinitelyBounded());
            assertTrue(p.toString(), q.b.getLower().isPresent());
            assertFalse(p.toString(), q.b.getUpper().isPresent());
        }

        Iterable<Pair<Interval, Rational>> psFail = filter(
                q -> !q.a.contains(q.b),
                P.pairs(P.intervals(), P.rationals())
        );
        for (Pair<Interval, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.split(p.b);
                fail(p.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesBisect() {
        initialize();
        System.out.println("\t\ttesting bisect() properties...");

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            Pair<Interval, Interval> bisection = a.bisect();
            validate(bisection.a);
            validate(bisection.b);
            assertTrue(a.toString(), bisection.a.isFinitelyBounded());
            assertTrue(a.toString(), bisection.b.isFinitelyBounded());
            assertEquals(a.toString(), bisection.a.diameter().get(), bisection.b.diameter().get());
            assertEquals(a.toString(), bisection.a.getUpper().get(), bisection.b.getLower().get());
            for (Rational r : take(TINY_LIMIT, P.rationals(a))) {
                assertTrue(a.toString(), bisection.a.contains(r) || bisection.b.contains(r));
            }
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            Interval a = of(r);
            assertEquals(r.toString(), a.bisect(), new Pair<>(a, a));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            try {
                lessThanOrEqualTo(r).bisect();
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
            try {
                greaterThanOrEqualTo(r).bisect();
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesRoundingPreimage_float() {
        initialize();
        System.out.println("\t\ttesting roundingPreimage(float) properties...");

        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g), P.floats()))) {
            Interval a = roundingPreimage(f);
            validate(a);
            assertEquals(Float.toString(f), roundingPreimage(-f), a.negate());
        }

        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g) && Math.abs(g) < Float.MAX_VALUE, P.floats()))) {
            Interval a = roundingPreimage(f);
            Rational central = Rational.ofExact(f);
            Rational pred = Rational.ofExact(FloatingPointUtils.predecessor(f));
            Rational succ = Rational.ofExact(FloatingPointUtils.successor(f));
            for (Rational r : take(TINY_LIMIT, P.rationals(a))) {
                Rational centralDistance = central.subtract(r).abs();
                Rational predDistance = pred.subtract(r).abs();
                Rational succDistance = succ.subtract(r).abs();
                assertTrue(Float.toString(f), le(centralDistance, predDistance));
                assertTrue(Float.toString(f), le(centralDistance, succDistance));
            }
            for (Rational r : take(TINY_LIMIT, P.rationalsNotIn(a))) {
                Rational centralDistance = central.subtract(r).abs();
                Rational predDistance = pred.subtract(r).abs();
                Rational succDistance = succ.subtract(r).abs();
                assertTrue(Float.toString(f), gt(centralDistance, predDistance) || gt(centralDistance, succDistance));
            }

            Rational x = a.getLower().get();
            Rational y = a.getUpper().get();
            for (Rational r : take(TINY_LIMIT, filter(s -> !s.equals(x) && !s.equals(y), P.rationals(a)))) {
                float g = r.floatValue();
                float h = f;
                //get rid of negative zero
                if (g == 0.0f) g = Math.abs(g);
                if (h == 0.0f) h = Math.abs(h);
                aeq(Float.toString(f), g, h);
            }
            for (Rational r : take(TINY_LIMIT, filter(s -> !s.equals(x) && !s.equals(y), P.rationalsNotIn(a)))) {
                aneq(Float.toString(f), r.floatValue(), f);
            }
        }
    }

    private static void propertiesRoundingPreimage_double() {
        initialize();
        System.out.println("\t\ttesting roundingPreimage(double) properties...");

        for (double d : take(LIMIT, filter(e -> !Double.isNaN(e), P.doubles()))) {
            Interval a = roundingPreimage(d);
            validate(a);
            assertEquals(Double.toString(d), roundingPreimage(-d), a.negate());
        }

        for (double d : take(LIMIT, filter(e -> !Double.isNaN(e) && Math.abs(e) < Double.MAX_VALUE, P.doubles()))) {
            Interval a = roundingPreimage(d);
            Rational central = Rational.ofExact(d);
            Rational pred = Rational.ofExact(FloatingPointUtils.predecessor(d));
            Rational succ = Rational.ofExact(FloatingPointUtils.successor(d));
            for (Rational r : take(TINY_LIMIT, P.rationals(a))) {
                Rational centralDistance = central.subtract(r).abs();
                Rational predDistance = pred.subtract(r).abs();
                Rational succDistance = succ.subtract(r).abs();
                assertTrue(Double.toString(d), le(centralDistance, predDistance));
                assertTrue(Double.toString(d), le(centralDistance, succDistance));
            }
            for (Rational r : take(TINY_LIMIT, P.rationalsNotIn(a))) {
                Rational centralDistance = central.subtract(r).abs();
                Rational predDistance = pred.subtract(r).abs();
                Rational succDistance = succ.subtract(r).abs();
                assertTrue(Double.toString(d), gt(centralDistance, predDistance) || gt(centralDistance, succDistance));
            }

            Rational x = a.getLower().get();
            Rational y = a.getUpper().get();
            for (Rational r : take(TINY_LIMIT, filter(s -> !s.equals(x) && !s.equals(y), P.rationals(a)))) {
                double g = r.doubleValue();
                double h = d;
                //get rid of negative zero
                if (g == 0.0) g = Math.abs(g);
                if (h == 0.0) h = Math.abs(h);
                aeq(Double.toString(d), g, h);
            }
            for (Rational r : take(TINY_LIMIT, filter(s -> !s.equals(x) && !s.equals(y), P.rationalsNotIn(a)))) {
                aneq(Double.toString(d), r.doubleValue(), d);
            }
        }
    }

    private static void propertiesRoundingPreimage_BigDecimal() {
        initialize();
        System.out.println("\t\ttesting roundingPreimage(BigDecimal) properties...");

        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            Interval a = roundingPreimage(bd);
            validate(a);
            assertEquals(bd.toString(), roundingPreimage(bd.negate()), a.negate());
            Rational central = Rational.of(bd);
            Rational pred = Rational.of(BigDecimalUtils.predecessor(bd));
            Rational succ = Rational.of(BigDecimalUtils.successor(bd));
            for (Rational r : take(TINY_LIMIT, P.rationals(a))) {
                Rational centralDistance = central.subtract(r).abs();
                Rational predDistance = pred.subtract(r).abs();
                Rational succDistance = succ.subtract(r).abs();
                assertTrue(bd.toString(), le(centralDistance, predDistance));
                assertTrue(bd.toString(), le(centralDistance, succDistance));
            }
            for (Rational r : take(TINY_LIMIT, P.rationalsNotIn(a))) {
                Rational centralDistance = central.subtract(r).abs();
                Rational predDistance = pred.subtract(r).abs();
                Rational succDistance = succ.subtract(r).abs();
                assertTrue(bd.toString(), gt(centralDistance, predDistance) || gt(centralDistance, succDistance));
            }
        }
    }

    private static void propertiesFloatRange() {
        initialize();
        System.out.println("\t\ttesting floatRange() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            Pair<Float, Float> range = a.floatRange();
            assertTrue(a.toString(), range.a != null);
            assertTrue(a.toString(), range.b != null);
            assertFalse(a.toString(), range.a.isNaN());
            assertFalse(a.toString(), range.b.isNaN());
            assertTrue(a.toString(), range.b >= range.a);
            assertFalse(a.toString(), range.a > 0 && range.a.isInfinite());
            assertFalse(a.toString(), range.a.equals(-0.0f));
            assertFalse(a.toString(), range.b < 0 && range.b.isInfinite());
            assertFalse(a.toString(), range.b.equals(-0.0f) && range.a.equals(0.0f));

            Pair<Float, Float> negRange = a.negate().floatRange();
            negRange = new Pair<>(-negRange.b, -negRange.a);
            float x = range.a.equals(-0.0f) ? 0.0f : range.a;
            float y = range.b.equals(-0.0f) ? 0.0f : range.b;
            float xn = negRange.a.equals(-0.0f) ? 0.0f : negRange.a;
            float yn = negRange.b.equals(-0.0f) ? 0.0f : negRange.b;
            aeq(a.toString(), x, xn);
            //noinspection SuspiciousNameCombination
            aeq(a.toString(), y, yn);

            Interval b;
            if (range.a.isInfinite() && range.b.isInfinite()) {
                b = ALL;
            } else if (range.a.isInfinite()) {
                b = lessThanOrEqualTo(Rational.ofExact(range.b));
            } else if (range.b.isInfinite()) {
                b = greaterThanOrEqualTo(Rational.ofExact(range.a));
            } else {
                b = of(Rational.ofExact(range.a), Rational.ofExact(range.b));
            }
            assertTrue(a.toString(), b.contains(a));
        }

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            Pair<Float, Float> range = a.floatRange();
            assertTrue(a.toString(), le(a.getLower().get(), Rational.ofExact(FloatingPointUtils.successor(range.a))));
            assertTrue(
                    a.toString(),
                    ge(a.getUpper().get(), Rational.ofExact(FloatingPointUtils.predecessor(range.b)))
            );
        }
    }

    private static void propertiesDoubleRange() {
        initialize();
        System.out.println("\t\ttesting doubleRange() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            Pair<Double, Double> range = a.doubleRange();
            assertTrue(a.toString(), range.a != null);
            assertTrue(a.toString(), range.b != null);
            assertFalse(a.toString(), range.a.isNaN());
            assertFalse(a.toString(), range.b.isNaN());
            assertTrue(a.toString(), range.b >= range.a);
            assertFalse(a.toString(), range.a > 0 && range.a.isInfinite());
            assertFalse(a.toString(), range.a.equals(-0.0));
            assertFalse(a.toString(), range.b < 0 && range.b.isInfinite());
            assertFalse(a.toString(), range.b.equals(-0.0) && range.a.equals(0.0));

            Pair<Double, Double> negRange = a.negate().doubleRange();
            negRange = new Pair<>(-negRange.b, -negRange.a);
            double x = range.a.equals(-0.0) ? 0.0 : range.a;
            double y = range.b.equals(-0.0) ? 0.0 : range.b;
            double xn = negRange.a.equals(-0.0) ? 0.0 : negRange.a;
            double yn = negRange.b.equals(-0.0) ? 0.0 : negRange.b;
            aeq(a.toString(), x, xn);
            //noinspection SuspiciousNameCombination
            aeq(a.toString(), y, yn);

            Interval b;
            if (range.a.isInfinite() && range.b.isInfinite()) {
                b = ALL;
            } else if (range.a.isInfinite()) {
                b = lessThanOrEqualTo(Rational.ofExact(range.b));
            } else if (range.b.isInfinite()) {
                b = greaterThanOrEqualTo(Rational.ofExact(range.a));
            } else {
                b = of(Rational.ofExact(range.a), Rational.ofExact(range.b));
            }
            assertTrue(a.toString(), b.contains(a));
        }

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            Pair<Float, Float> range = a.floatRange();
            assertTrue(a.toString(), le(a.getLower().get(), Rational.ofExact(FloatingPointUtils.successor(range.a))));
            assertTrue(
                    a.toString(),
                    ge(a.getUpper().get(), Rational.ofExact(FloatingPointUtils.predecessor(range.b)))
            );
        }
    }

    private static void propertiesAdd() {
        initialize();
        System.out.println("\t\ttesting add(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Interval sum = p.a.add(p.b);
            validate(sum);
            assertEquals(p.toString(), sum, p.b.add(p.a));
            assertTrue(p.toString(), sum.subtract(p.b).contains(p.a));
            for (Pair<Rational, Rational> q : take(TINY_LIMIT, P.pairs(P.rationals(p.a), P.rationals(p.b)))) {
                assertTrue(p.toString(), sum.contains(q.a.add(q.b)));
            }
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.finitelyBoundedIntervals()))) {
            Interval sum = p.a.add(p.b);
            Rational diameter = sum.diameter().get();
            assertTrue(p.toString(), ge(diameter, p.a.diameter().get()));
            assertTrue(p.toString(), ge(diameter, p.b.diameter().get()));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a.toString(), ZERO.add(a), a);
            assertEquals(a.toString(), a.add(ZERO), a);
            assertTrue(a.toString(), a.subtract(a).contains(ZERO));
            assertEquals(a.toString(), ALL.add(a), ALL);
            assertEquals(a.toString(), a.add(ALL), ALL);
        }

        for (Triple<Interval, Interval, Interval> t : take(LIMIT, P.triples(P.intervals()))) {
            Interval sum1 = t.a.add(t.b).add(t.c);
            Interval sum2 = t.a.add(t.b.add(t.c));
            assertEquals(t.toString(), sum1, sum2);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assertEquals(p.toString(), of(p.a).add(of(p.b)), of(p.a.add(p.b)));
        }
    }

    private static void propertiesNegate() {
        initialize();
        System.out.println("\t\ttesting negate() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            Interval negativeA = a.negate();
            validate(negativeA);
            assertEquals(a.toString(), a, negativeA.negate());
            assertTrue(a.add(negativeA).contains(ZERO));
            assertEquals(a.toString(), a.diameter(), negativeA.diameter());
            for (Rational r : take(TINY_LIMIT, P.rationals(a))) {
                assertTrue(a.toString(), negativeA.contains(r.negate()));
            }
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), of(r).negate(), of(r.negate()));
        }
    }

    private static void propertiesAbs() {
        initialize();
        System.out.println("\t\ttesting abs() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            Interval absA = a.abs();
            validate(absA);
            assertEquals(a.toString(), absA, absA.abs());
            Optional<Interval> negativeIntersection = absA.intersection(lessThanOrEqualTo(Rational.ZERO));
            assertTrue(a.toString(), !negativeIntersection.isPresent() || negativeIntersection.get().equals(ZERO));
            for (Rational r : take(TINY_LIMIT, P.rationals(a))) {
                assertTrue(a.toString(), absA.contains(r.abs()));
            }
        }

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            Rational diameter = a.diameter().get();
            Rational absDiameter = a.abs().diameter().get();
            assertTrue(a.toString(), le(absDiameter, diameter));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), of(r).abs(), of(r.abs()));
        }
    }

    private static void propertiesSignum() {
        initialize();
        System.out.println("\t\ttesting signum() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            Optional<Integer> signumA = a.signum();
            assertEquals(a.toString(), signumA, a.elementCompare(ZERO).map(Ordering::toInt));
            if (signumA.isPresent()) {
                Integer s = signumA.get();
                assertTrue(a.toString(), s == -1 || s == 0 || s == 1);
                for (Rational r : take(TINY_LIMIT, P.rationals(a))) {
                    assertTrue(a.toString(), r.signum() == s);
                }
            } else {
                assertTrue(a.toString(), !a.equals(ZERO));
                assertTrue(a.toString(), a.contains(ZERO));
            }
        }
    }

    private static void propertiesSubtract() {
        initialize();
        System.out.println("\t\ttesting subtract(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Interval difference = p.a.subtract(p.b);
            validate(difference);
            assertEquals(p.toString(), difference, p.b.subtract(p.a).negate());
            assertTrue(p.toString(), difference.add(p.b).contains(p.a));
            for (Pair<Rational, Rational> q : take(TINY_LIMIT, P.pairs(P.rationals(p.a), P.rationals(p.b)))) {
                assertTrue(p.toString(), difference.contains(q.a.subtract(q.b)));
            }
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.finitelyBoundedIntervals()))) {
            Interval difference = p.a.subtract(p.b);
            Rational diameter = difference.diameter().get();
            assertTrue(p.toString(), ge(diameter, p.a.diameter().get()));
            assertTrue(p.toString(), ge(diameter, p.b.diameter().get()));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a.toString(), ZERO.subtract(a), a.negate());
            assertEquals(a.toString(), a.subtract(ZERO), a);
            assertTrue(a.toString(), a.subtract(a).contains(ZERO));
            assertEquals(a.toString(), ALL.subtract(a), ALL);
            assertEquals(a.toString(), a.subtract(ALL), ALL);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assertEquals(p.toString(), of(p.a).subtract(of(p.b)), of(p.a.subtract(p.b)));
        }
    }

    private static void propertiesMultiply_Interval() {
        initialize();
        System.out.println("\t\ttesting multiply(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Interval product = p.a.multiply(p.b);
            validate(product);
            assertEquals(p.toString(), product, p.b.multiply(p.a));
            for (Pair<Rational, Rational> q : take(TINY_LIMIT, P.pairs(P.rationals(p.a), P.rationals(p.b)))) {
                assertTrue(p.toString(), product.contains(q.a.multiply(q.b)));
            }
        }

//        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), filter(r -> r != ZERO, P.rationals())))) {
//            assertEquals(p.toString(), p.a.multiply(p.b).divide(p.b), p.a);
//        }
//
//        Iterable<Pair<Rational, Rational>> ps = P.pairs(P.rationals(), filter(r -> r != Rational.ZERO, P.rationals()));
//        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
//            assertEquals(p.toString(), p.a.multiply(p.b), p.a.divide(p.b.invert()));
//        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a.toString(), ONE.multiply(a), a);
            assertEquals(a.toString(), a.multiply(ONE), a);
            assertTrue(a.toString(), ZERO.multiply(a).equals(ZERO));
            assertTrue(a.toString(), a.multiply(ZERO).equals(ZERO));
        }

        for (Interval a : take(LIMIT, filter(b -> !b.equals(ZERO), P.intervals()))) {
            assertEquals(a.toString(), ALL.multiply(a), ALL);
            assertEquals(a.toString(), a.multiply(ALL), ALL);
//            assertTrue(r.toString(), r.multiply(r.invert()) == ONE);
        }

        for (Triple<Interval, Interval, Interval> t : take(LIMIT, P.triples(P.intervals()))) {
            Interval product1 = t.a.multiply(t.b).multiply(t.c);
            Interval product2 = t.a.multiply(t.b.multiply(t.c));
            assertEquals(t.toString(), product1, product2);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assertEquals(p.toString(), of(p.a).multiply(of(p.b)), of(p.a.multiply(p.b)));
        }
    }

    private static void propertiesEquals() {
        initialize();
        System.out.println("\t\ttesting equals(Object) properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            //noinspection EqualsWithItself
            assertTrue(a.toString(), a.equals(a));
            //noinspection ObjectEqualsNull
            assertFalse(a.toString(), a.equals(null));
        }
    }

    private static void propertiesHashCode() {
        initialize();
        System.out.println("\t\ttesting hashCode() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a.toString(), a.hashCode(), a.hashCode());
        }
    }

    private static void propertiesCompareTo() {
        initialize();
        System.out.println("\t\ttesting compareTo(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            int compare = p.a.compareTo(p.b);
            assertTrue(p.toString(), compare == -1 || compare == 0 || compare == 1);
            assertEquals(p.toString(), p.b.compareTo(p.a), -compare);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a.toString(), a.compareTo(a), 0);
        }

        Iterable<Triple<Interval, Interval, Interval>> ts = filter(
                t -> lt(t.a, t.b) && lt(t.b, t.c),
                P.triples(P.intervals())
        );
        for (Triple<Interval, Interval, Interval> t : take(LIMIT, ts)) {
            assertEquals(t.toString(), t.a.compareTo(t.c), -1);
        }
    }

    private static void propertiesRead() {
        initialize();
        System.out.println("\t\ttesting read(String) properties...");

        for (String s : take(LIMIT, P.strings())) {
            read(s);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            Optional<Interval> oi = read(a.toString());
            assertEquals(a.toString(), oi.get(), a);
        }
    }

    private static void propertiesFindIn() {
        initialize();
        System.out.println("\t\ttesting findIn(String) properties...");

        for (String s : take(LIMIT, P.strings())) {
            findIn(s);
        }

        Iterable<Pair<String, Integer>> ps = P.dependentPairsLogarithmic(P.strings(), s -> range(0, s.length()));
        Iterable<String> ss = map(p -> take(p.a.b, p.a.a) + p.b + drop(p.a.b, p.a.a), P.pairs(ps, P.intervals()));
        for (String s : take(LIMIT, ss)) {
            Optional<Pair<Interval, Integer>> op = findIn(s);
            Pair<Interval, Integer> p = op.get();
            assertNotNull(s, p.a);
            assertNotNull(s, p.b);
            assertTrue(s, p.b >= 0 && p.b < s.length());
            String before = take(p.b, s);
            assertFalse(s, findIn(before).isPresent());
            String during = p.a.toString();
            assertTrue(s, s.substring(p.b).startsWith(during));
            String after = drop(p.b + during.length(), s);
            assertTrue(s, after.isEmpty() || !read(during + head(after)).isPresent());
        }
    }

    private static void propertiesToString() {
        initialize();
        System.out.println("\t\ttesting toString() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            String s = a.toString();
            assertTrue(isSubsetOf(s, INTERVAL_CHARS));
            Optional<Interval> readI = read(s);
            assertTrue(a.toString(), readI.isPresent());
            assertEquals(a.toString(), readI.get(), a);
        }
    }

    private static void validate(@NotNull Interval a) {
        if (a.getLower().isPresent() && a.getUpper().isPresent()) {
            assertTrue(a.toString(), le(a.getLower().get(), a.getUpper().get()));
        }
    }

    private static void aeq(String message, float x, float y) {
        assertEquals(message, Float.toString(x), Float.toString(y));
    }

    private static void aeq(String message, double x, double y) {
        assertEquals(message, Double.toString(x), Double.toString(y));
    }

    private static void aneq(String message, float x, float y) {
        assertNotEquals(message, Float.toString(x), Float.toString(y));
    }

    private static void aneq(String message, double x, double y) {
        assertNotEquals(message, Double.toString(x), Double.toString(y));
    }

    private static void aeq(String message, BigDecimal x, BigDecimal y) {
        assertEquals(message, x.stripTrailingZeros(), y.stripTrailingZeros());
    }
}
