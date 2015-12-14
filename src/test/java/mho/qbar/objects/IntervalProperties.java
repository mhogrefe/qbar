package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.numberUtils.BigDecimalUtils;
import mho.wheels.numberUtils.FloatingPointUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.qbar.objects.Interval.*;
import static mho.qbar.objects.Interval.greaterThanOrEqualTo;
import static mho.qbar.objects.Interval.lessThanOrEqualTo;
import static mho.qbar.objects.Interval.sum;
import static mho.qbar.testing.QBarTesting.*;
import static mho.qbar.testing.QBarTesting.propertiesCompareToHelper;
import static mho.qbar.testing.QBarTesting.propertiesEqualsHelper;
import static mho.qbar.testing.QBarTesting.propertiesHashCodeHelper;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class IntervalProperties extends QBarTestProperties {
    private static final @NotNull String INTERVAL_CHARS = " (),-/0123456789I[]finty";

    public IntervalProperties() {
        super("Interval");
    }

    @Override
    protected void testBothModes() {
        propertiesGetLower();
        propertiesGetUpper();
        propertiesOf_Rational_Rational();
        propertiesLessThanOrEqualTo();
        propertiesGreaterThanOrEqualTo();
        propertiesOf_Rational();
        propertiesBitLength();
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
//        propertiesComplement();
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
        propertiesMultiply_Rational();
        propertiesMultiply_BigInteger();
        propertiesMultiply_int();
        propertiesInvert();
        propertiesInvertHull();
        propertiesDivide_Interval();
        propertiesDivideHull();
        propertiesDivide_Rational();
        propertiesDivide_BigInteger();
        propertiesDivide_int();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesShiftRight();
        compareImplementationsShiftRight();
        propertiesSum();
        propertiesProduct();
        propertiesDelta();
        propertiesPow();
        propertiesPowHull();
        propertiesElementCompare();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead();
        propertiesFindIn();
        propertiesToString();
    }

    private void propertiesGetLower() {
        initialize("getLower()");
        for (Interval a : take(LIMIT, P.intervals())) {
            a.getLower();
        }
    }

    private void propertiesGetUpper() {
        initialize("getUpper()");
        for (Interval a : take(LIMIT, P.intervals())) {
            a.getUpper();
        }
    }

    private void propertiesOf_Rational_Rational() {
        initialize("of(Rational, Rational)");
        Iterable<Pair<Rational, Rational>> ps = filterInfinite(q -> le(q.a, q.b), P.pairs(P.rationals()));
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            Interval a = of(p.a, p.b);
            a.validate();
            assertTrue(a, a.isFinitelyBounded());
        }

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            assertEquals(a, of(a.getLower().get(), a.getUpper().get()), a);
        }
    }

    private void propertiesLessThanOrEqualTo() {
        initialize("lessThanOrEqualTo(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Interval a = lessThanOrEqualTo(r);
            a.validate();
            assertFalse(a, a.getLower().isPresent());
            assertEquals(a, a.getUpper().get(), r);
            for (Rational s : take(TINY_LIMIT, P.rationalsIn(a))) {
                assertTrue(r, le(s, r));
            }
        }
    }

    private void propertiesGreaterThanOrEqualTo() {
        initialize("greaterThanOrEqualTo(Rational");
        for (Rational r : take(LIMIT, P.rationals())) {
            Interval a = greaterThanOrEqualTo(r);
            a.validate();
            assertEquals(a, a.getLower().get(), r);
            assertFalse(a, a.getUpper().isPresent());
            for (Rational s : take(TINY_LIMIT, P.rationalsIn(a))) {
                assertTrue(r, ge(s, r));
            }
        }
    }

    private void propertiesOf_Rational() {
        initialize("of(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Interval a = of(r);
            a.validate();
            assertTrue(a, a.isFinitelyBounded());
            assertEquals(a, a.diameter().get(), Rational.ZERO);
            for (Rational s : take(TINY_LIMIT, P.rationalsIn(a))) {
                assertEquals(r, s, r);
            }
        }
    }

    private void propertiesBitLength() {
        initialize("bitLength()");
        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a, a.bitLength() >= 0);
            homomorphic(Interval::negate, Function.identity(), Interval::bitLength, Interval::bitLength, a);
        }
    }

    private void propertiesIsFinitelyBounded() {
        initialize("isFinitelyBounded()");
        for (Interval a : take(LIMIT, P.intervals())) {
            assertNotEquals(a, a.isFinitelyBounded(), a.toString().contains("Infinity"));
        }

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            assertTrue(a, a.isFinitelyBounded());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertFalse(r, lessThanOrEqualTo(r).isFinitelyBounded());
            assertFalse(r, greaterThanOrEqualTo(r).isFinitelyBounded());
        }
    }

    private void propertiesContains_Rational() {
        initialize("");
        System.out.println("\t\ttesting contains(Rational) properties...");

        for (Pair<Interval, Rational> p : take(LIMIT, P.pairs(P.intervals(), P.rationals()))) {
            p.a.contains(p.b);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(r, ALL.contains(r));
            assertTrue(r, of(r).contains(r));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, filter(q -> !q.a.equals(q.b), P.pairs(P.rationals())))) {
            assertFalse(p, of(p.a).contains(p.b));
        }
    }

    private void propertiesContains_Interval() {
        initialize("");
        System.out.println("\t\ttesting contains(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            p.a.contains(p.b);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a, ALL.contains(a));
            assertTrue(a, a.contains(a));
        }

        for (Pair<Interval, Interval> p : take(LIMIT, filter(q -> q.a.contains(q.b), P.pairs(P.intervals())))) {
            Optional<Rational> ad = p.a.diameter();
            Optional<Rational> bd = p.b.diameter();
            assertTrue(p, !ad.isPresent() || le(bd.get(), ad.get()));
            assertTrue(p, p.a.convexHull(p.b).equals(p.a));
        }

        Iterable<Pair<Rational, Interval>> ps = filter(
                q -> !q.b.equals(of(q.a)),
                P.pairs(P.rationals(), P.intervals())
        );
        for (Pair<Rational, Interval> p : take(LIMIT, ps)) {
            assertFalse(p, of(p.a).contains(p.b));
        }
    }

    private void propertiesDiameter() {
        initialize("");
        System.out.println("\t\ttesting diameter() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            a.diameter();
        }

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            assertTrue(a, a.diameter().get().signum() != -1);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertFalse(r, lessThanOrEqualTo(r).diameter().isPresent());
            assertFalse(r, greaterThanOrEqualTo(r).diameter().isPresent());
        }
    }

    private void propertiesConvexHull_Interval() {
        initialize("");
        System.out.println("\t\ttesting convexHull(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Interval c = p.a.convexHull(p.b);
            c.validate();
            assertEquals(p, c, p.b.convexHull(p.a));
            //Given an interval c whose endpoints are in each of two intervals a and b, any Rational in c lies in the
            //convex hull of a and b
            for (Pair<Rational, Rational> q : take(TINY_LIMIT, P.pairs(P.rationalsIn(p.a), P.rationalsIn(p.b)))) {
                for (Rational r : take(TINY_LIMIT, P.rationalsIn(of(min(q.a, q.b), max(q.a, q.b))))) {
                    assertTrue(p, c.contains(r));
                }
            }
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.finitelyBoundedIntervals()))) {
            Interval c = p.a.convexHull(p.b);
            assertTrue(p, ge(c.diameter().get(), p.a.diameter().get()));
            assertTrue(p, ge(c.diameter().get(), p.b.diameter().get()));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a, a.convexHull(a).equals(a));
            assertTrue(a, ALL.convexHull(a).equals(ALL));
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

    private void propertiesConvexHull_List_Interval() {
        initialize("");
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
            assertEquals(as, alt, c);
            c.validate();
        }

        for (List<Interval> as : take(LIMIT, P.listsAtLeast(1, P.finitelyBoundedIntervals()))) {
            Interval c = convexHull(as);
            Rational cd = c.diameter().get();
            assertTrue(as, all(a -> ge(cd, a.diameter().get()), as));
        }

        Iterable<Pair<List<Interval>, List<Interval>>> ps = P.dependentPairs(
                P.listsAtLeast(1, P.intervals()),
                P::permutationsFinite
        );
        for (Pair<List<Interval>, List<Interval>> p : take(LIMIT, ps)) {
            assertEquals(p, convexHull(p.a), convexHull(p.b));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a, convexHull(Collections.singletonList(a)), a);
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            assertEquals(p, convexHull(Arrays.asList(p.a, p.b)), p.a.convexHull(p.b));
        }

        Iterable<Pair<Interval, Integer>> ps2;
        if (P instanceof QBarExhaustiveProvider) {
            ps2 = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.intervals(), P.positiveIntegers());
        } else {
            ps2 = P.pairs(P.intervals(), P.withScale(20).positiveIntegersGeometric());
        }
        for (Pair<Interval, Integer> p : take(LIMIT, ps2)) {
            assertEquals(p, convexHull(toList(replicate(p.b, p.a))), p.a);
        }

        for (List<Interval> as : take(LIMIT, P.listsWithElement(ALL, P.intervals()))) {
            assertEquals(as, convexHull(as), ALL);
        }

        for (List<Interval> as : take(LIMIT, P.listsWithElement(null, P.intervals()))) {
            try {
                convexHull(as);
                fail(as);
            } catch (NullPointerException ignored) {}
        }
    }

    private void compareImplementationsConvexHull_List_Interval() {
        initialize("");
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

    private void propertiesIntersection() {
        initialize("");
        System.out.println("\t\ttesting intersection(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Optional<Interval> oi = p.a.intersection(p.b);
            if (oi.isPresent()) {
                oi.get().validate();
            }
            assertEquals(p, oi, p.b.intersection(p.a));
            assertEquals(p, oi.isPresent(), !p.a.disjoint(p.b));
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.finitelyBoundedIntervals()))) {
            Optional<Interval> oi = p.a.intersection(p.b);
            if (oi.isPresent()) {
                assertTrue(p, le(oi.get().diameter().get(), p.a.diameter().get()));
                assertTrue(p, le(oi.get().diameter().get(), p.b.diameter().get()));
            }
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a, ALL.intersection(a).get(), a);
            assertEquals(a, a.intersection(a).get(), a);
        }

        for (Pair<Interval, Interval> p : take(LIMIT, filter(q -> !q.a.disjoint(q.b), P.pairs(P.intervals())))) {
            Interval intersection = p.a.intersection(p.b).get();
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(intersection))) {
                assertTrue(p, p.a.contains(r));
                assertTrue(p, p.b.contains(r));
            }
        }
    }

    private void propertiesDisjoint() {
        initialize("");
        System.out.println("\t\ttesting disjoint(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            boolean disjoint = p.a.disjoint(p.b);
            assertEquals(p, p.b.disjoint(p.a), disjoint);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertFalse(a, ALL.disjoint(a));
            assertFalse(a, a.disjoint(a));
        }

        for (Pair<Interval, Interval> p : take(LIMIT, filter(q -> q.a.disjoint(q.b), P.pairs(P.intervals())))) {
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertFalse(p, p.b.contains(r));
            }
        }
    }

    private void propertiesMakeDisjoint() {
        initialize("");
        System.out.println("\t\ttesting makeDisjoint(List<Interval>) properties...");

        for (List<Interval> as : take(LIMIT, P.lists(P.intervals()))) {
            List<Interval> disjoint = makeDisjoint(as);
            disjoint.forEach(Interval::validate);
            assertTrue(as, weaklyIncreasing(disjoint));
            assertTrue(
                    as.toString(),
                    and(map(p -> p.a.disjoint(p.b), ExhaustiveProvider.INSTANCE.distinctPairs(disjoint)))
            );
            for (Rational r : take(TINY_LIMIT, mux(toList(map(P::rationalsIn, as))))) {
                assertTrue(as, or(map(a -> a.contains(r), disjoint)));
            }
        }

        Iterable<Pair<List<Interval>, List<Interval>>> ps = P.dependentPairs(
                P.lists(P.intervals()),
                P::permutationsFinite
        );
        for (Pair<List<Interval>, List<Interval>> p : take(LIMIT, ps)) {
            assertEquals(p, makeDisjoint(p.a), makeDisjoint(p.b));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a, makeDisjoint(Collections.singletonList(a)), Collections.singletonList(a));
        }

        for (Pair<Interval, Interval> p : take(LIMIT, filter(q -> q.a.disjoint(q.b), P.pairs(P.intervals())))) {
            assertEquals(p, makeDisjoint(Pair.toList(p)), sort(Pair.toList(p)));
        }

        for (Pair<Interval, Interval> p : take(LIMIT, filter(q -> !q.a.disjoint(q.b), P.pairs(P.intervals())))) {
            assertEquals(p, makeDisjoint(Pair.toList(p)), Collections.singletonList(convexHull(Pair.toList(p))));
        }

        for (List<Interval> as : take(LIMIT, P.listsWithElement(ALL, P.intervals()))) {
            assertEquals(as, makeDisjoint(as), Collections.singletonList(ALL));
        }

        for (List<Interval> as : take(LIMIT, P.listsWithElement(null, P.intervals()))) {
            try {
                convexHull(as);
                fail(as);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesComplement() {
        initialize("");
        System.out.println("\t\ttesting complement() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            List<Interval> complement = a.complement();
            complement.forEach(Interval::validate);
            assertEquals(a, makeDisjoint(complement), complement);
            List<Rational> endpoints = new ArrayList<>();
            if (a.getLower().isPresent()) endpoints.add(a.getLower().get());
            if (a.getUpper().isPresent()) endpoints.add(a.getUpper().get());
            for (Rational endpoint : endpoints) {
                any(b -> b.contains(endpoint), complement);
            }
            //todo: fix hanging
//            for (Rational r : take(TINY_LIMIT, filter(s -> !endpoints.contains(s), P.rationals(a)))) {
//                assertFalse(a, any(b -> b.contains(r), complement));
//            }
            for (Rational r : take(TINY_LIMIT, P.rationalsNotIn(a))) {
                assertTrue(a, any(b -> b.contains(r), complement));
            }
        }

        for (Interval a : take(LIMIT, map(Interval::lessThanOrEqualTo, P.rationals()))) {
            List<Interval> complement = a.complement();
            assertEquals(a, complement.size(), 1);
            Interval x = complement.get(0);
            assertTrue(a, x.getLower().isPresent());
            assertFalse(a, x.getUpper().isPresent());
        }

        for (Interval a : take(LIMIT, map(Interval::greaterThanOrEqualTo, P.rationals()))) {
            List<Interval> complement = a.complement();
            assertEquals(a, complement.size(), 1);
            Interval x = complement.get(0);
            assertFalse(a, x.getLower().isPresent());
            assertTrue(a, x.getUpper().isPresent());
        }

        for (Interval a : take(LIMIT, map(Interval::of, P.rationals()))) {
            assertEquals(a, a.complement(), Collections.singletonList(ALL));
        }

        Iterable<Interval> as = filter(b -> b.diameter().get() != Rational.ZERO, P.finitelyBoundedIntervals());
        for (Interval a : take(LIMIT, as)) {
            List<Interval> complement = a.complement();
            assertEquals(a, complement.size(), 2);
            Interval x = complement.get(0);
            Interval y = complement.get(1);
            assertFalse(a, x.getLower().isPresent());
            assertTrue(a, x.getUpper().isPresent());
            assertTrue(a, y.getLower().isPresent());
            assertFalse(a, y.getUpper().isPresent());
        }
    }

    private void propertiesMidpoint() {
        initialize("");
        System.out.println("\t\ttesting midpoint() properties...");

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            Rational midpoint = a.midpoint();
            assertEquals(a, midpoint.subtract(a.getLower().get()), a.getUpper().get().subtract(midpoint));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, of(r).midpoint(), r);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            try {
                lessThanOrEqualTo(r).midpoint();
                fail(r);
            } catch (ArithmeticException ignored) {}
            try {
                greaterThanOrEqualTo(r).midpoint();
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesSplit() {
        initialize("");
        System.out.println("\t\ttesting split(Rational) properties...");

        Iterable<Pair<Interval, Rational>> ps = filter(q -> q.a.contains(q.b), P.pairs(P.intervals(), P.rationals()));
        for (Pair<Interval, Rational> p : take(LIMIT, ps)) {
            Pair<Interval, Interval> split = p.a.split(p.b);
            split.a.validate();
            split.b.validate();
            assertEquals(p, split.a.getUpper().get(), p.b);
            assertEquals(p, split.b.getLower().get(), p.b);
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, split.a.contains(r) || split.b.contains(r));
            }
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            Interval a = of(r);
            assertEquals(r, a.split(r), new Pair<>(a, a));

            Pair<Interval, Interval> p = ALL.split(r);
            assertFalse(r, p.a.getLower().isPresent());
            assertTrue(r, p.a.getUpper().isPresent());
            assertTrue(r, p.b.getLower().isPresent());
            assertFalse(r, p.b.getUpper().isPresent());
        }

        for (Pair<Rational, Rational> p : take(LIMIT, filter(q -> le(q.a, q.b), P.pairs(P.rationals())))) {
            Interval a = lessThanOrEqualTo(p.b);
            Pair<Interval, Interval> q = a.split(p.a);
            assertFalse(p, q.a.getLower().isPresent());
            assertTrue(p, q.a.getUpper().isPresent());
            assertTrue(p, q.b.isFinitelyBounded());
        }

        for (Pair<Rational, Rational> p : take(LIMIT, filter(q -> le(q.a, q.b), P.pairs(P.rationals())))) {
            Interval a = greaterThanOrEqualTo(p.a);
            Pair<Interval, Interval> q = a.split(p.b);
            assertTrue(p, q.a.isFinitelyBounded());
            assertTrue(p, q.b.getLower().isPresent());
            assertFalse(p, q.b.getUpper().isPresent());
        }

        Iterable<Pair<Interval, Rational>> psFail = filter(
                q -> !q.a.contains(q.b),
                P.pairs(P.intervals(), P.rationals())
        );
        for (Pair<Interval, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.split(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesBisect() {
        initialize("");
        System.out.println("\t\ttesting bisect() properties...");

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            Pair<Interval, Interval> bisection = a.bisect();
            bisection.a.validate();
            bisection.b.validate();
            assertTrue(a, bisection.a.isFinitelyBounded());
            assertTrue(a, bisection.b.isFinitelyBounded());
            assertEquals(a, bisection.a.diameter().get(), bisection.b.diameter().get());
            assertEquals(a, bisection.a.getUpper().get(), bisection.b.getLower().get());
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(a))) {
                assertTrue(a, bisection.a.contains(r) || bisection.b.contains(r));
            }
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            Interval a = of(r);
            assertEquals(r, a.bisect(), new Pair<>(a, a));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            try {
                lessThanOrEqualTo(r).bisect();
                fail(r);
            } catch (ArithmeticException ignored) {}
            try {
                greaterThanOrEqualTo(r).bisect();
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesRoundingPreimage_float() {
        initialize("");
        System.out.println("\t\ttesting roundingPreimage(float) properties...");

        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g), P.floats()))) {
            Interval a = roundingPreimage(f);
            a.validate();
            assertEquals(f, roundingPreimage(-f), a.negate());
        }

        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g) && Math.abs(g) < Float.MAX_VALUE, P.floats()))) {
            Interval a = roundingPreimage(f);
            Rational central = Rational.ofExact(f).get();
            Rational pred = Rational.ofExact(FloatingPointUtils.predecessor(f)).get();
            Rational succ = Rational.ofExact(FloatingPointUtils.successor(f)).get();
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(a))) {
                Rational centralDistance = central.subtract(r).abs();
                Rational predDistance = pred.subtract(r).abs();
                Rational succDistance = succ.subtract(r).abs();
                assertTrue(f, le(centralDistance, predDistance));
                assertTrue(f, le(centralDistance, succDistance));
            }
            for (Rational r : take(TINY_LIMIT, P.rationalsNotIn(a))) {
                Rational centralDistance = central.subtract(r).abs();
                Rational predDistance = pred.subtract(r).abs();
                Rational succDistance = succ.subtract(r).abs();
                assertTrue(f, gt(centralDistance, predDistance) || gt(centralDistance, succDistance));
            }

            Rational x = a.getLower().get();
            Rational y = a.getUpper().get();
            for (Rational r : take(TINY_LIMIT, filter(s -> !s.equals(x) && !s.equals(y), P.rationalsIn(a)))) {
                float g = r.floatValue();
                float h = f;
                //get rid of negative zero
                if (g == 0.0f) g = Math.abs(g);
                if (h == 0.0f) h = Math.abs(h);
                assertEquals(f, g, h);
            }
            for (Rational r : take(TINY_LIMIT, filter(s -> !s.equals(x) && !s.equals(y), P.rationalsNotIn(a)))) {
                assertNotEquals(f, r.floatValue(), f);
            }
        }
    }

    private void propertiesRoundingPreimage_double() {
        initialize("");
        System.out.println("\t\ttesting roundingPreimage(double) properties...");

        for (double d : take(LIMIT, filter(e -> !Double.isNaN(e), P.doubles()))) {
            Interval a = roundingPreimage(d);
            a.validate();
            assertEquals(d, roundingPreimage(-d), a.negate());
        }

        for (double d : take(LIMIT, filter(e -> !Double.isNaN(e) && Math.abs(e) < Double.MAX_VALUE, P.doubles()))) {
            Interval a = roundingPreimage(d);
            Rational central = Rational.ofExact(d).get();
            Rational pred = Rational.ofExact(FloatingPointUtils.predecessor(d)).get();
            Rational succ = Rational.ofExact(FloatingPointUtils.successor(d)).get();
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(a))) {
                Rational centralDistance = central.subtract(r).abs();
                Rational predDistance = pred.subtract(r).abs();
                Rational succDistance = succ.subtract(r).abs();
                assertTrue(d, le(centralDistance, predDistance));
                assertTrue(d, le(centralDistance, succDistance));
            }
            for (Rational r : take(TINY_LIMIT, P.rationalsNotIn(a))) {
                Rational centralDistance = central.subtract(r).abs();
                Rational predDistance = pred.subtract(r).abs();
                Rational succDistance = succ.subtract(r).abs();
                assertTrue(d, gt(centralDistance, predDistance) || gt(centralDistance, succDistance));
            }

            Rational x = a.getLower().get();
            Rational y = a.getUpper().get();
            for (Rational r : take(TINY_LIMIT, filter(s -> !s.equals(x) && !s.equals(y), P.rationalsIn(a)))) {
                double g = r.doubleValue();
                double h = d;
                //get rid of negative zero
                if (g == 0.0) g = Math.abs(g);
                if (h == 0.0) h = Math.abs(h);
                assertEquals(d, g, h);
            }
            for (Rational r : take(TINY_LIMIT, filter(s -> !s.equals(x) && !s.equals(y), P.rationalsNotIn(a)))) {
                assertNotEquals(d, r.doubleValue(), d);
            }
        }
    }

    private void propertiesRoundingPreimage_BigDecimal() {
        initialize("");
        System.out.println("\t\ttesting roundingPreimage(BigDecimal) properties...");

        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            Interval a = roundingPreimage(bd);
            a.validate();
            assertEquals(bd, roundingPreimage(bd.negate()), a.negate());
            Rational central = Rational.of(bd);
            Rational pred = Rational.of(BigDecimalUtils.predecessor(bd));
            Rational succ = Rational.of(BigDecimalUtils.successor(bd));
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(a))) {
                Rational centralDistance = central.subtract(r).abs();
                Rational predDistance = pred.subtract(r).abs();
                Rational succDistance = succ.subtract(r).abs();
                assertTrue(bd, le(centralDistance, predDistance));
                assertTrue(bd, le(centralDistance, succDistance));
            }
            for (Rational r : take(TINY_LIMIT, P.rationalsNotIn(a))) {
                Rational centralDistance = central.subtract(r).abs();
                Rational predDistance = pred.subtract(r).abs();
                Rational succDistance = succ.subtract(r).abs();
                assertTrue(bd, gt(centralDistance, predDistance) || gt(centralDistance, succDistance));
            }
        }
    }

    private void propertiesFloatRange() {
        initialize("");
        System.out.println("\t\ttesting floatRange() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            Pair<Float, Float> range = a.floatRange();
            assertTrue(a, range.a != null);
            assertTrue(a, range.b != null);
            assertFalse(a, range.a.isNaN());
            assertFalse(a, range.b.isNaN());
            assertTrue(a, range.b >= range.a);
            assertFalse(a, range.a > 0 && range.a.isInfinite());
            assertFalse(a, FloatingPointUtils.isNegativeZero(range.a));
            assertFalse(a, range.b < 0 && range.b.isInfinite());
            assertFalse(a, FloatingPointUtils.isNegativeZero(range.b) && FloatingPointUtils.isPositiveZero(range.a));

            Pair<Float, Float> negRange = a.negate().floatRange();
            negRange = new Pair<>(-negRange.b, -negRange.a);
            float x = FloatingPointUtils.absNegativeZeros(range.a);
            float y = FloatingPointUtils.absNegativeZeros(range.b);
            float xn = FloatingPointUtils.absNegativeZeros(negRange.a);
            float yn = FloatingPointUtils.absNegativeZeros(negRange.b);
            assertEquals(a, x, xn);
            assertEquals(a, y, yn);

            Interval b;
            if (range.a.isInfinite() && range.b.isInfinite()) {
                b = ALL;
            } else if (range.a.isInfinite()) {
                b = lessThanOrEqualTo(Rational.ofExact(range.b).get());
            } else if (range.b.isInfinite()) {
                b = greaterThanOrEqualTo(Rational.ofExact(range.a).get());
            } else {
                b = of(Rational.ofExact(range.a).get(), Rational.ofExact(range.b).get());
            }
            assertTrue(a, b.contains(a));
        }

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            Pair<Float, Float> range = a.floatRange();
            assertTrue(a, le(a.getLower().get(), Rational.ofExact(FloatingPointUtils.successor(range.a)).get()));
            assertTrue(a, ge(a.getUpper().get(), Rational.ofExact(FloatingPointUtils.predecessor(range.b)).get()));
        }
    }

    private void propertiesDoubleRange() {
        initialize("");
        System.out.println("\t\ttesting doubleRange() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            Pair<Double, Double> range = a.doubleRange();
            assertTrue(a, range.a != null);
            assertTrue(a, range.b != null);
            assertFalse(a, range.a.isNaN());
            assertFalse(a, range.b.isNaN());
            assertTrue(a, range.b >= range.a);
            assertFalse(a, range.a > 0 && range.a.isInfinite());
            assertFalse(a, FloatingPointUtils.isNegativeZero(range.a));
            assertFalse(a, range.b < 0 && range.b.isInfinite());
            assertFalse(a, FloatingPointUtils.isNegativeZero(range.b) && FloatingPointUtils.isPositiveZero(range.a));

            Pair<Double, Double> negRange = a.negate().doubleRange();
            negRange = new Pair<>(-negRange.b, -negRange.a);
            double x = FloatingPointUtils.absNegativeZeros(range.a);
            double y = FloatingPointUtils.absNegativeZeros(range.b);
            double xn = FloatingPointUtils.absNegativeZeros(negRange.a);
            double yn = FloatingPointUtils.absNegativeZeros(negRange.b);
            assertEquals(a, x, xn);
            assertEquals(a, y, yn);

            Interval b;
            if (range.a.isInfinite() && range.b.isInfinite()) {
                b = ALL;
            } else if (range.a.isInfinite()) {
                b = lessThanOrEqualTo(Rational.ofExact(range.b).get());
            } else if (range.b.isInfinite()) {
                b = greaterThanOrEqualTo(Rational.ofExact(range.a).get());
            } else {
                b = of(Rational.ofExact(range.a).get(), Rational.ofExact(range.b).get());
            }
            assertTrue(a, b.contains(a));
        }

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            Pair<Float, Float> range = a.floatRange();
            assertTrue(a, le(a.getLower().get(), Rational.ofExact(FloatingPointUtils.successor(range.a)).get()));
            assertTrue(a, ge(a.getUpper().get(), Rational.ofExact(FloatingPointUtils.predecessor(range.b)).get()));
        }
    }

    private void propertiesAdd() {
        initialize("");
        System.out.println("\t\ttesting add(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Interval sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum, p.b.add(p.a));
            assertTrue(p, sum.subtract(p.b).contains(p.a));
            for (Pair<Rational, Rational> q : take(TINY_LIMIT, P.pairs(P.rationalsIn(p.a), P.rationalsIn(p.b)))) {
                assertTrue(p, sum.contains(q.a.add(q.b)));
            }
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.finitelyBoundedIntervals()))) {
            Interval sum = p.a.add(p.b);
            Rational diameter = sum.diameter().get();
            assertTrue(p, ge(diameter, p.a.diameter().get()));
            assertTrue(p, ge(diameter, p.b.diameter().get()));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a, ZERO.add(a), a);
            assertEquals(a, a.add(ZERO), a);
            assertTrue(a, a.subtract(a).contains(ZERO));
            assertEquals(a, ALL.add(a), ALL);
            assertEquals(a, a.add(ALL), ALL);
        }

        for (Triple<Interval, Interval, Interval> t : take(LIMIT, P.triples(P.intervals()))) {
            Interval sum1 = t.a.add(t.b).add(t.c);
            Interval sum2 = t.a.add(t.b.add(t.c));
            assertEquals(t, sum1, sum2);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assertEquals(p, of(p.a).add(of(p.b)), of(p.a.add(p.b)));
        }
    }

    private void propertiesNegate() {
        initialize("");
        System.out.println("\t\ttesting negate() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            Interval negative = a.negate();
            negative.validate();
            assertEquals(a, a, negative.negate());
            assertTrue(a, a.add(negative).contains(ZERO));
            assertEquals(a, a.diameter(), negative.diameter());
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(a))) {
                assertTrue(a, negative.contains(r.negate()));
            }
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, of(r).negate(), of(r.negate()));
        }
    }

    private void propertiesAbs() {
        initialize("");
        System.out.println("\t\ttesting abs() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            Interval abs = a.abs();
            abs.validate();
            assertEquals(a, abs, abs.abs());
            Optional<Interval> negativeIntersection = abs.intersection(lessThanOrEqualTo(Rational.ZERO));
            assertTrue(a, !negativeIntersection.isPresent() || negativeIntersection.get().equals(ZERO));
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(a))) {
                assertTrue(a, abs.contains(r.abs()));
            }
        }

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            Rational diameter = a.diameter().get();
            Rational absDiameter = a.abs().diameter().get();
            assertTrue(a, le(absDiameter, diameter));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, of(r).abs(), of(r.abs()));
        }
    }

    private void propertiesSignum() {
        initialize("");
        System.out.println("\t\ttesting signum() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            Optional<Integer> signumA = a.signum();
            assertEquals(a, signumA, a.elementCompare(ZERO).map(Ordering::toInt));
            if (signumA.isPresent()) {
                Integer s = signumA.get();
                assertTrue(a, s == -1 || s == 0 || s == 1);
                for (Rational r : take(TINY_LIMIT, P.rationalsIn(a))) {
                    assertTrue(a, r.signum() == s);
                }
            } else {
                assertTrue(a, !a.equals(ZERO));
                assertTrue(a, a.contains(ZERO));
            }
        }
    }

    private void propertiesSubtract() {
        initialize("");
        System.out.println("\t\ttesting subtract(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Interval difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, difference, p.b.subtract(p.a).negate());
            assertTrue(p, difference.add(p.b).contains(p.a));
            for (Pair<Rational, Rational> q : take(TINY_LIMIT, P.pairs(P.rationalsIn(p.a), P.rationalsIn(p.b)))) {
                assertTrue(p, difference.contains(q.a.subtract(q.b)));
            }
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.finitelyBoundedIntervals()))) {
            Interval difference = p.a.subtract(p.b);
            Rational diameter = difference.diameter().get();
            assertTrue(p, ge(diameter, p.a.diameter().get()));
            assertTrue(p, ge(diameter, p.b.diameter().get()));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a, ZERO.subtract(a), a.negate());
            assertEquals(a, a.subtract(ZERO), a);
            assertTrue(a, a.subtract(a).contains(ZERO));
            assertEquals(a, ALL.subtract(a), ALL);
            assertEquals(a, a.subtract(ALL), ALL);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assertEquals(p, of(p.a).subtract(of(p.b)), of(p.a.subtract(p.b)));
        }
    }

    private void propertiesMultiply_Interval() {
        initialize("");
        System.out.println("\t\ttesting multiply(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Interval product = p.a.multiply(p.b);
            product.validate();
            assertEquals(p, product, p.b.multiply(p.a));
            for (Pair<Rational, Rational> q : take(TINY_LIMIT, P.pairs(P.rationalsIn(p.a), P.rationalsIn(p.b)))) {
                assertTrue(p, product.contains(q.a.multiply(q.b)));
            }
        }

        Iterable<Pair<Interval, Interval>> ps = P.pairs(P.intervals(), filter(a -> !a.equals(ZERO), P.intervals()));
        for (Pair<Interval, Interval> p : take(LIMIT, ps)) {
            Interval product = p.a.multiply(p.b);
            List<Interval> quotient = product.divide(p.b);
            assertTrue(p, any(a -> a.contains(p.a), quotient));
            quotient = makeDisjoint(toList(concatMap(p.a::divide, p.b.invert())));
            assertTrue(p, quotient.contains(product));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a, ONE.multiply(a), a);
            assertEquals(a, a.multiply(ONE), a);
            assertTrue(a, ZERO.multiply(a).equals(ZERO));
            assertTrue(a, a.multiply(ZERO).equals(ZERO));
        }

        for (Interval a : take(LIMIT, filter(b -> !b.equals(ZERO), P.intervals()))) {
            assertEquals(a, ALL.multiply(a), ALL);
            assertEquals(a, a.multiply(ALL), ALL);
            List<Interval> inverse = a.invert();
            List<Interval> back = makeDisjoint(toList(map(a::multiply, inverse)));
            assertTrue(a, any(b -> b.contains(ONE), back));
        }

        for (Triple<Interval, Interval, Interval> t : take(LIMIT, P.triples(P.intervals()))) {
            Interval product1 = t.a.multiply(t.b).multiply(t.c);
            Interval product2 = t.a.multiply(t.b.multiply(t.c));
            assertEquals(t, product1, product2);
            Interval expression1 = t.a.add(t.b).multiply(t.c);
            Interval expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertTrue(t, expression2.contains(expression1));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assertEquals(p, of(p.a).multiply(of(p.b)), of(p.a.multiply(p.b)));
        }
    }

    private void propertiesMultiply_Rational() {
        initialize("");
        System.out.println("\t\ttesting multiply(Rational) properties...");

        for (Pair<Interval, Rational> p : take(LIMIT, P.pairs(P.intervals(), P.rationals()))) {
            Interval a = p.a.multiply(p.b);
            a.validate();
            assertEquals(p, a, p.a.multiply(of(p.b)));
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, a.contains(r.multiply(p.b)));
            }
        }

        Iterable<Pair<Interval, Rational>> ps = P.pairs(P.intervals(), filter(r -> r != Rational.ZERO, P.rationals()));
        for (Pair<Interval, Rational> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b).divide(p.b), p.a);
            assertEquals(p, p.a.multiply(p.b), p.a.divide(p.b.invert()));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a, a, a.multiply(Rational.ONE));
            assertEquals(a, a.multiply(Rational.ZERO), ZERO);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ZERO.multiply(r), ZERO);
            assertEquals(r, ONE.multiply(r), of(r));
        }

        for (Rational r : take(LIMIT, filter(s -> s != Rational.ZERO, P.rationals()))) {
            assertEquals(r, ALL.multiply(r), ALL);
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("");
        System.out.println("\t\ttesting multiply(BigInteger) properties...");

        for (Pair<Interval, BigInteger> p : take(LIMIT, P.pairs(P.intervals(), P.bigIntegers()))) {
            Interval a = p.a.multiply(p.b);
            a.validate();
            assertEquals(p, a, p.a.multiply(Rational.of(p.b)));
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, a.contains(r.multiply(p.b)));
            }
        }

        Iterable<Pair<Interval, BigInteger>> ps = P.pairs(
                P.intervals(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Interval, BigInteger> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b).divide(p.b), p.a);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a, a, a.multiply(BigInteger.ONE));
            assertEquals(a, a.multiply(BigInteger.ZERO), ZERO);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), ZERO.multiply(i), ZERO);
            assertEquals(i.toString(), ONE.multiply(i), of(Rational.of(i)));
        }

        for (BigInteger i : take(LIMIT, filter(j -> !j.equals(BigInteger.ZERO), P.bigIntegers()))) {
            assertEquals(i.toString(), ALL.multiply(i), ALL);
        }
    }

    private void propertiesMultiply_int() {
        initialize("");
        System.out.println("\t\ttesting multiply(int) properties...");

        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.integers()))) {
            Interval a = p.a.multiply(p.b);
            a.validate();
            assertEquals(p, a, p.a.multiply(Rational.of(p.b)));
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, a.contains(r.multiply(p.b)));
            }
        }

        Iterable<Pair<Interval, Integer>> ps = P.pairs(P.intervals(), filter(i -> i != 0, P.integers()));
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b).divide(p.b), p.a);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a, a, a.multiply(1));
            assertEquals(a, a.multiply(0), ZERO);
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(i, ZERO.multiply(i), ZERO);
            assertEquals(i, ONE.multiply(i), of(Rational.of(i)));
        }

        for (int i : take(LIMIT, filter(j -> j != 0, P.integers()))) {
            assertEquals(i, ALL.multiply(i), ALL);
        }
    }

    private void propertiesInvert() {
        initialize("");
        System.out.println("\t\ttesting invert() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            List<Interval> inverse = a.invert();
            inverse.forEach(Interval::validate);

            //todo fix hanging
//            for (Rational r : take(TINY_LIMIT, filter(s -> s != Rational.ZERO, P.rationals(a)))) {
//                assertTrue(a, any(b -> b.contains(r.invert()), inverse));
//            }

            int size = inverse.size();
            assertTrue(a, size == 0 || size == 1 || size == 2);
            if (size == 1) {
                Interval i1 = inverse.get(0);
                Rational p = i1.getLower().isPresent() ? i1.getLower().get() : null;
                Rational q = i1.getUpper().isPresent() ? i1.getUpper().get() : null;
                if (p == null && q != null) {
                    assertTrue(a, le(q, Rational.ZERO));
                } else if (p != null && q == null) {
                    assertTrue(a, ge(p, Rational.ZERO));
                } else if (i1.isFinitelyBounded()) {
                    if (p.equals(q)) {
                        assertNotEquals(a, p, Rational.ZERO);
                    } else {
                        int ps = p.signum();
                        int qs = q.signum();
                        if (qs == 1) {
                            assertTrue(a, ps != -1);
                        }
                        if (ps == -1) {
                            assertTrue(a, qs != 1);
                        }
                    }
                }
            } else if (size == 2) {
                Interval i1 = inverse.get(0);
                Interval i2 = inverse.get(1);
                Rational p1 = i1.getLower().isPresent() ? i1.getLower().get() : null;
                Rational q1 = i1.getUpper().isPresent() ? i1.getUpper().get() : null;
                Rational p2 = i2.getLower().isPresent() ? i2.getLower().get() : null;
                Rational q2 = i2.getUpper().isPresent() ? i2.getUpper().get() : null;
                assertTrue(a, p1 == null);
                assertTrue(a, q2 == null);
                if (p2 == Rational.ZERO) {
                    assertTrue(a, q1.signum() == -1);
                } else if (q1 == Rational.ZERO) {
                    assertTrue(a, p2.signum() == 1);
                } else {
                    assertTrue(a, q1.signum() == -1);
                    assertTrue(a, p2.signum() == 1);
                }
            }
        }

        for (Interval a : take(LIMIT, filter(b -> !b.equals(ZERO), P.intervals()))) {
            List<Interval> inverse = a.invert();
            List<Interval> back = makeDisjoint(toList(concatMap(Interval::invert, inverse)));
            assertTrue(a, convexHull(back).contains(a));
            assertTrue(a, convexHull(toList(map(a::multiply, inverse))).contains(ONE));
        }
    }

    private void propertiesInvertHull() {
        initialize("");
        System.out.println("\t\ttesting invertHull() properties...");

        for (Interval a : take(LIMIT, filter(b -> !b.equals(ZERO), P.intervals()))) {
            Interval inverse = a.invertHull();
            inverse.validate();

            for (Rational r : take(TINY_LIMIT, filter(s -> s != Rational.ZERO, P.rationalsIn(a)))) {
                assertTrue(a, inverse.contains(r.invert()));
            }

            Rational p = inverse.getLower().isPresent() ? inverse.getLower().get() : null;
            Rational q = inverse.getUpper().isPresent() ? inverse.getUpper().get() : null;
            if (p == null && q != null) {
                assertTrue(a, le(q, Rational.ZERO));
            } else if (p != null && q == null) {
                assertTrue(a, ge(p, Rational.ZERO));
            } else if (inverse.isFinitelyBounded()) {
                if (p.equals(q)) {
                    assertNotEquals(a, p, ZERO);
                } else {
                    int ps = p.signum();
                    int qs = q.signum();
                    if (qs == 1) {
                        assertTrue(a, ps != -1);
                    }
                    if (ps == -1) {
                        assertTrue(a, qs != 1);
                    }
                }
            }

            assertTrue(a, all(inverse::contains, a.invert()));
            Interval back = inverse.invertHull();
            assertTrue(a, back.contains(a));
            assertTrue(a, a.multiply(inverse).contains(ONE));
        }
    }

    private void propertiesDivide_Interval() {
        initialize("");
        System.out.println("\t\ttesting divide(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            List<Interval> quotient = p.a.divide(p.b);
            quotient.forEach(Interval::validate);

            //todo fix hanging
//            Iterable<Pair<Rational, Rational>> qs = P.pairs(
//                    P.rationals(p.a),
//                    filter(r -> r != Rational.ZERO, P.rationals(p.b))
//            );
//            for (Pair<Rational, Rational> q : take(TINY_LIMIT, qs)) {
//                assertTrue(p, any(b -> b.contains(q.a.divide(q.b)), quotient));
//            }

            int size = quotient.size();
            assertTrue(p, size == 0 || size == 1 || size == 2);
            if (size == 2) {
                Interval i1 = quotient.get(0);
                Interval i2 = quotient.get(1);
                Rational p1 = i1.getLower().isPresent() ? i1.getLower().get() : null;
                Rational q1 = i1.getUpper().isPresent() ? i1.getUpper().get() : null;
                Rational p2 = i2.getLower().isPresent() ? i2.getLower().get() : null;
                Rational q2 = i2.getUpper().isPresent() ? i2.getUpper().get() : null;
                assertTrue(p, p1 == null);
                assertTrue(p, q2 == null);
                if (p2 == Rational.ZERO) {
                    assertTrue(p, q1.signum() == -1);
                } else if (q1 == Rational.ZERO) {
                    assertTrue(p, p2.signum() == 1);
                } else {
                    assertTrue(p, q1.signum() == -1);
                    assertTrue(p, p2.signum() == 1);
                }
            }
        }

        Iterable<Pair<Interval, Interval>> ps = P.pairs(P.intervals(), filter(a -> !a.equals(ZERO), P.intervals()));
        for (Pair<Interval, Interval> p : take(LIMIT, ps)) {
            List<Interval> quotient = p.a.divide(p.b);

            assertTrue(p, convexHull(toList(map(a -> a.multiply(p.b), quotient))).contains(p.a));
            assertTrue(p, convexHull(toList(map(p.a::multiply, p.b.invert()))).contains(convexHull(quotient)));
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(filter(a -> !a.equals(ZERO), P.intervals())))) {
            assertTrue(
                    p,
                    convexHull(toList(concatMap(Interval::invert, p.b.divide(p.a))))
                            .contains(convexHull(p.a.divide(p.b)))
            );
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a, any(b -> b.contains(a), a.divide(ONE)));
        }

        for (Interval a : take(LIMIT, filter(b -> !b.equals(ZERO), P.intervals()))) {
            assertEquals(a, ONE.divide(a), a.invert());
            assertTrue(a, any(b -> b.contains(ONE), a.divide(a)));
        }
    }

    private void propertiesDivideHull() {
        initialize("");
        System.out.println("\t\ttesting divideHull(Interval) properties...");

        Iterable<Pair<Interval, Interval>> ps = P.pairs(P.intervals(), filter(a -> !a.equals(ZERO), P.intervals()));
        for (Pair<Interval, Interval> p : take(LIMIT, ps)) {
            Interval quotient = p.a.divideHull(p.b);
            quotient.validate();

            Iterable<Pair<Rational, Rational>> qs = P.pairs(
                    P.rationalsIn(p.a),
                    filter(r -> r != Rational.ZERO, P.rationalsIn(p.b))
            );
            for (Pair<Rational, Rational> q : take(TINY_LIMIT, qs)) {
                assertTrue(p, quotient.contains(q.a.divide(q.b)));
            }

            assertTrue(p, quotient.multiply(p.b).contains(p.a));
            assertTrue(p, p.a.multiply(p.b.invertHull()).contains(quotient));
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(filter(a -> !a.equals(ZERO), P.intervals())))) {
            assertTrue(p, p.b.divideHull(p.a).invertHull().contains(p.a.divideHull(p.b)));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a, a.divideHull(ONE).contains(a));
        }

        for (Interval a : take(LIMIT, filter(b -> !b.equals(ZERO), P.intervals()))) {
            assertEquals(a, ONE.divideHull(a), a.invertHull());
            assertTrue(a, a.divideHull(a).contains(ONE));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            try {
                a.divideHull(ZERO);
                fail(a);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_Rational() {
        initialize("");
        System.out.println("\t\ttesting divide(Rational) properties...");

        Iterable<Pair<Interval, Rational>> ps = P.pairs(P.intervals(), filter(r -> r != Rational.ZERO, P.rationals()));
        for (Pair<Interval, Rational> p : take(LIMIT, ps)) {
            Interval quotient = p.a.divide(p.b);
            quotient.validate();

            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, quotient.contains(r.divide(p.b)));
            }

            assertTrue(p, quotient.multiply(p.b).contains(p.a));
            assertTrue(p, p.a.multiply(p.b.invert()).contains(quotient));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a, a.divide(Rational.ONE).contains(a));
        }

        for (Rational r : take(LIMIT, filter(s -> s != Rational.ZERO, P.rationals()))) {
            assertEquals(r, ONE.divide(r), of(r.invert()));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            try {
                a.divide(Rational.ZERO);
                fail(a);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_BigInteger() {
        initialize("");
        System.out.println("\t\ttesting divide(BigInteger) properties...");

        Iterable<Pair<Interval, BigInteger>> ps = P.pairs(
                P.intervals(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Interval, BigInteger> p : take(LIMIT, ps)) {
            Interval quotient = p.a.divide(p.b);
            quotient.validate();

            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, quotient.contains(r.divide(p.b)));
            }

            assertTrue(p, quotient.multiply(p.b).contains(p.a));
            assertTrue(p, p.a.multiply(Rational.of(p.b).invert()).contains(quotient));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a, a.divide(BigInteger.ONE).contains(a));
        }

        for (BigInteger i : take(LIMIT, filter(j -> !j.equals(BigInteger.ZERO), P.bigIntegers()))) {
            assertEquals(i, ONE.divide(i), of(Rational.of(i).invert()));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            try {
                a.divide(BigInteger.ZERO);
                fail(a);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_int() {
        initialize("");
        System.out.println("\t\ttesting divide(int) properties...");

        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), filter(i -> i != 0, P.integers())))) {
            Interval quotient = p.a.divide(p.b);
            quotient.validate();

            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, quotient.contains(r.divide(p.b)));
            }

            assertTrue(p, quotient.multiply(p.b).contains(p.a));
            assertTrue(p, p.a.multiply(Rational.of(p.b).invert()).contains(quotient));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a, a.divide(1).contains(a));
        }

        for (int i : take(LIMIT, filter(j -> j != 0, P.integers()))) {
            assertEquals(i, ONE.divide(i), of(Rational.of(i).invert()));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            try {
                a.divide(0);
                fail(a);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull Interval shiftLeft_simplest(@NotNull Interval a, int bits) {
        if (bits < 0) {
            return a.divide(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return a.multiply(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftLeft() {
        initialize("");
        System.out.println("\t\ttesting shiftLeft(int) properties...");

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric();
        }
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), is))) {
            Interval shifted = p.a.shiftLeft(p.b);
            shifted.validate();

            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, shifted.contains(r.shiftLeft(p.b)));
            }

            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            assertEquals(p, p.a.signum(), shifted.signum());
            assertEquals(p, p.a.isFinitelyBounded(), shifted.isFinitelyBounded());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
            assertEquals(p, shifted, p.a.shiftRight(-p.b));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a, a.shiftLeft(0), a);
        }

        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = ((QBarRandomProvider) P).naturalIntegersGeometric();
        }
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), is))) {
            Interval shifted = p.a.shiftLeft(p.b);
            assertEquals(p, shifted, p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private void compareImplementationsShiftLeft() {
        initialize("");
        System.out.println("\t\tcomparing shiftLeft(int) implementations...");

        long totalTime = 0;
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric();
        }
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), is))) {
            long time = System.nanoTime();
            shiftLeft_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), is))) {
            long time = System.nanoTime();
            p.a.shiftLeft(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Interval shiftRight_simplest(@NotNull Interval a, int bits) {
        if (bits < 0) {
            return a.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return a.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftRight() {
        initialize("");
        System.out.println("\t\ttesting shiftRight(int) properties...");

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric();
        }
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), is))) {
            Interval shifted = p.a.shiftRight(p.b);
            shifted.validate();

            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, shifted.contains(r.shiftRight(p.b)));
            }

            assertEquals(p, shifted, shiftRight_simplest(p.a, p.b));
            assertEquals(p, p.a.signum(), shifted.signum());
            assertEquals(p, p.a.isFinitelyBounded(), shifted.isFinitelyBounded());
            assertEquals(p, p.a.negate().shiftRight(p.b), shifted.negate());
            assertEquals(p, shifted, p.a.shiftLeft(-p.b));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a, a.shiftRight(0), a);
        }

        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = ((QBarRandomProvider) P).naturalIntegersGeometric();
        }
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), is))) {
            Interval shifted = p.a.shiftRight(p.b);
            assertEquals(p, shifted, p.a.divide(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private void compareImplementationsShiftRight() {
        initialize("");
        System.out.println("\t\tcomparing shiftRight(int) implementations...");

        long totalTime = 0;
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric();
        }
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), is))) {
            long time = System.nanoTime();
            shiftRight_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), is))) {
            long time = System.nanoTime();
            p.a.shiftRight(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private void propertiesSum() {
        initialize("");
        System.out.println("\t\ttesting sum(Iterable<Interval>) properties...");

        propertiesFoldHelper(LIMIT, P.getWheelsProvider(), P.intervals(), Interval::add, Interval::sum, a -> {}, true);

        for (List<Interval> is : take(LIMIT, P.lists(P.intervals()))) {
            Interval sum = sum(is);
            for (List<Rational> rs : take(TINY_LIMIT, transposeTruncating(map(P::rationalsIn, is)))) {
                assertTrue(is, sum.contains(Rational.sum(rs)));
            }
            assertEquals(is, sum.isFinitelyBounded(), is.isEmpty() || all(Interval::isFinitelyBounded, is));
        }
    }

    private void propertiesProduct() {
        initialize("");
        System.out.println("\t\ttesting product(Iterable<Interval>) properties...");

        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.intervals(),
                Interval::multiply,
                Interval::product,
                a -> {},
                true
        );

        for (List<Interval> is : take(LIMIT, P.lists(P.intervals()))) {
            Interval product = product(is);
            for (List<Rational> rs : take(TINY_LIMIT, transposeTruncating(map(P::rationalsIn, is)))) {
                assertTrue(is, product.contains(Rational.product(rs)));
            }

            assertEquals(
                    is,
                    product.isFinitelyBounded(),
                    is.isEmpty() || is.contains(ZERO) || all(Interval::isFinitelyBounded, is)
            );
        }
    }

    private void propertiesDelta() {
        initialize("");
        System.out.println("\t\ttesting delta(Iterable<Interval>) properties...");

        propertiesDeltaHelper(
                LIMIT,
                P.getWheelsProvider(),
                QEP.intervals(),
                P.intervals(),
                Interval::negate,
                Interval::subtract,
                Interval::delta,
                a -> {}
        );

        for (List<Interval> is : take(LIMIT, P.listsAtLeast(1, P.intervals()))) {
            Iterable<Interval> deltas = delta(is);
            for (List<Rational> rs : take(TINY_LIMIT, transposeTruncating(map(P::rationalsIn, is)))) {
                assertTrue(is, and(zipWith(Interval::contains, deltas, Rational.delta(rs))));
            }
        }
    }

    private void propertiesPow() {
        initialize("");
        System.out.println("\t\ttesting pow(int) properties...");

        Iterable<Pair<Interval, Integer>> ps = P.pairs(P.intervals(), P.withScale(20).integersGeometric());
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            List<Interval> pow = p.a.pow(p.b);
            pow.forEach(Interval::validate);

            //todo fix hanging
//            Iterable<Rational> rs = p.b < 0 ? filter(r -> r != Rational.ZERO, P.rationals(p.a)) : P.rationals(p.a);
//            for (Rational r : take(TINY_LIMIT, rs)) {
//                assertTrue(p, any(s -> s.contains(r.pow(p.b)), pow));
//            }

            int size = pow.size();
            assertTrue(p, size == 0 || size == 1 || size == 2);
            if (size == 2) {
                Interval i1 = pow.get(0);
                Interval i2 = pow.get(1);
                Rational p1 = i1.getLower().isPresent() ? i1.getLower().get() : null;
                Rational q1 = i1.getUpper().isPresent() ? i1.getUpper().get() : null;
                Rational p2 = i2.getLower().isPresent() ? i2.getLower().get() : null;
                Rational q2 = i2.getUpper().isPresent() ? i2.getUpper().get() : null;
                assertTrue(p, p1 == null);
                assertTrue(p, q2 == null);
                if (p2 == Rational.ZERO) {
                    assertTrue(p, q1.signum() == -1);
                } else if (q1 == Rational.ZERO) {
                    assertTrue(p, p2.signum() == 1);
                } else {
                    assertTrue(p, q1.signum() == -1);
                    assertTrue(p, p2.signum() == 1);
                }
            }
        }

        ps = filter(
                p -> p.b >= 0 || !p.a.equals(ZERO),
                P.pairs(P.intervals(), P.withScale(20).integersGeometric())
        );
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            List<Interval> pow = p.a.pow(p.b);
            Interval x = product(replicate(Math.abs(p.b), p.a));
            Interval product = p.b < 0 ? x.invertHull() : x;
            assertTrue(p, all(product::contains, pow));
        }

        ps = P.pairs(filter(a -> !a.equals(ZERO), P.intervals()), P.withScale(20).integersGeometric());
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            List<Interval> a = p.a.pow(p.b);
            assertTrue(
                    p,
                    any(c -> convexHull(toList(concatMap(Interval::invert, p.a.pow(-p.b)))).contains(c), a)
            );
            assertTrue(
                    p,
                    any(c -> convexHull(toList(concatMap(b -> b.pow(-p.b), p.a.invert()))).contains(c), a)
            );
        }

        for (int i : take(LIMIT, P.withScale(20).positiveIntegersGeometric())) {
            assertTrue(i, ZERO.pow(i).equals(Collections.singletonList(ZERO)));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a, a.pow(0).equals(Collections.singletonList(ONE)));
            assertEquals(a, a.pow(1), Collections.singletonList(a));
            Interval product = a.multiply(a);
            assertTrue(a, all(product::contains, a.pow(2)));
            assertEquals(a, a.pow(-1), a.invert());
        }

        Iterable<Triple<Interval, Integer, Integer>> ts1 = filter(
                p -> p.b >= 0 && p.c >= 0 || !p.a.equals(ZERO),
                P.triples(P.intervals(), P.withScale(20).integersGeometric(), P.withScale(20).integersGeometric())
        );
        for (Triple<Interval, Integer, Integer> t : take(LIMIT, ts1)) {
            Interval expression1 = convexHull(toList(concatMap(a -> map(a::multiply, t.a.pow(t.c)), t.a.pow(t.b))));
            Interval expression2 = convexHull(t.a.pow(t.b + t.c));
            assertTrue(t, expression1.contains(expression2));
        }

        ts1 = filter(
                t -> !t.a.equals(ZERO) || t.c == 0 && t.b >= 0,
                P.triples(P.intervals(), P.withScale(20).integersGeometric(), P.withScale(20).integersGeometric())
        );
        for (Triple<Interval, Integer, Integer> t : take(LIMIT, ts1)) {
            Interval expression1 = convexHull(
                    toList(concatMap(a -> concatMap(a::divide, t.a.pow(t.c)), t.a.pow(t.b)))
            );
            Interval expression2 = convexHull(t.a.pow(t.b - t.c));
            assertTrue(t, expression1.contains(expression2));
        }

        ts1 = filter(
                t -> !t.a.equals(ZERO) || t.b >= 0 && t.c >= 0,
                P.triples(P.intervals(), P.withScale(20).integersGeometric(), P.withScale(20).integersGeometric())
        );
        for (Triple<Interval, Integer, Integer> t : take(LIMIT, ts1)) {
            Interval expression5 = convexHull(toList(concatMap(a -> a.pow(t.c), t.a.pow(t.b))));
            Interval expression6 = convexHull(t.a.pow(t.b * t.c));
            assertTrue(t, expression5.contains(expression6));
        }

        Iterable<Triple<Interval, Interval, Integer>> ts2 = filter(
                t -> !t.a.equals(ZERO) && !t.b.equals(ZERO) || t.c >= 0,
                P.triples(P.intervals(), P.intervals(), P.withScale(20).integersGeometric())
        );
        for (Triple<Interval, Interval, Integer> t : take(LIMIT, ts2)) {
            Interval expression1 = convexHull(t.a.multiply(t.b).pow(t.c));
            Interval expression2 = convexHull(toList(concatMap(a -> map(a::multiply, t.b.pow(t.c)), t.a.pow(t.c))));
            assertEquals(t, expression1, expression2);
        }

        ts2 = filter(
                t -> !t.a.equals(ZERO) || t.c >= 0,
                P.triples(
                        P.intervals(),
                        filter(a -> !a.equals(ZERO), P.intervals()),
                        P.withScale(20).integersGeometric()
                )
        );
        for (Triple<Interval, Interval, Integer> t : take(LIMIT, ts2)) {
            Interval expression1 = convexHull(toList(concatMap(a -> a.pow(t.c), t.a.divide(t.b))));
            Interval expression2 = convexHull(
                    toList(concatMap(a -> concatMap(a::divide, t.b.pow(t.c)), t.a.pow(t.c)))
            );
            assertTrue(t, expression1.contains(expression2));
        }
    }

    private void propertiesPowHull() {
        initialize("");
        System.out.println("\t\ttesting powHull(int) properties...");

        Iterable<Pair<Interval, Integer>> ps = filter(
                p -> p.b >= 0 || !p.a.equals(ZERO),
                P.pairs(P.intervals(), P.withScale(20).integersGeometric())
        );
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            Interval pow = p.a.powHull(p.b);
            pow.validate();

            //todo fix hanging
//            Iterable<Rational> rs = p.b < 0 ? filter(r -> r != Rational.ZERO, P.rationals(p.a)) : P.rationals(p.a);
//            for (Rational r : take(TINY_LIMIT, rs)) {
//                assertTrue(p, pow.contains(r.pow(p.b)));
//            }

            Interval product = product(replicate(Math.abs(p.b), p.a));
            if (p.b < 0) product = product.invertHull();
            assertTrue(p, product.contains(pow));
        }

        ps = P.pairs(filter(a -> !a.equals(ZERO), P.intervals()), P.withScale(20).integersGeometric());
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            Interval a = p.a.powHull(p.b);
            assertTrue(p, p.a.powHull(-p.b).invertHull().contains(a));
            assertTrue(p, p.a.invertHull().powHull(-p.b).contains(a));
        }

        for (int i : take(LIMIT, P.withScale(20).positiveIntegersGeometric())) {
            assertTrue(Integer.toString(i), ZERO.powHull(i).equals(ZERO));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a, a.powHull(0).equals(ONE));
            assertEquals(a, a.powHull(1), a);
            assertTrue(a, a.multiply(a).contains(a.powHull(2)));
        }

        Iterable<Interval> rs = filter(a -> !a.equals(ZERO), P.intervals());
        for (Interval a : take(LIMIT, rs)) {
            assertEquals(a, a.powHull(-1), a.invertHull());
        }

        Iterable<Triple<Interval, Integer, Integer>> ts1 = filter(
                p -> p.b >= 0 && p.c >= 0 || !p.a.equals(ZERO),
                P.triples(P.intervals(), P.withScale(20).integersGeometric(), P.withScale(20).integersGeometric())
        );
        for (Triple<Interval, Integer, Integer> t : take(LIMIT, ts1)) {
            Interval expression1 = t.a.powHull(t.b).multiply(t.a.powHull(t.c));
            Interval expression2 = t.a.powHull(t.b + t.c);
            assertTrue(t, expression1.contains(expression2));
        }

        ts1 = filter(
                t -> !t.a.equals(ZERO) || t.c == 0 && t.b >= 0,
                P.triples(P.intervals(), P.withScale(20).integersGeometric(), P.withScale(20).integersGeometric())
        );
        for (Triple<Interval, Integer, Integer> t : take(LIMIT, ts1)) {
            Interval expression1 = t.a.powHull(t.b).divideHull(t.a.powHull(t.c));
            Interval expression2 = t.a.powHull(t.b - t.c);
            assertTrue(t, expression1.contains(expression2));
        }

        ts1 = filter(
                t -> !t.a.equals(ZERO) || t.b >= 0 && t.c >= 0,
                P.triples(P.intervals(), P.withScale(20).integersGeometric(), P.withScale(20).integersGeometric())
        );
        for (Triple<Interval, Integer, Integer> t : take(LIMIT, ts1)) {
            Interval expression5 = t.a.powHull(t.b).powHull(t.c);
            Interval expression6 = t.a.powHull(t.b * t.c);
            assertTrue(t, expression5.contains(expression6));
        }

        Iterable<Triple<Interval, Interval, Integer>> ts2 = filter(
                t -> !t.a.equals(ZERO) && !t.b.equals(ZERO) || t.c >= 0,
                P.triples(P.intervals(), P.intervals(), P.withScale(20).integersGeometric())
        );
        for (Triple<Interval, Interval, Integer> t : take(LIMIT, ts2)) {
            Interval expression1 = t.a.multiply(t.b).powHull(t.c);
            Interval expression2 = t.a.powHull(t.c).multiply(t.b.powHull(t.c));
            assertEquals(t, expression1, expression2);
        }

        ts2 = filter(
                t -> !t.a.equals(ZERO) || t.c >= 0,
                P.triples(
                        P.intervals(),
                        filter(a -> !a.equals(ZERO), P.intervals()),
                        P.withScale(20).integersGeometric()
                )
        );
        for (Triple<Interval, Interval, Integer> t : take(LIMIT, ts2)) {
            Interval expression1 = t.a.divideHull(t.b).powHull(t.c);
            Interval expression2 = t.a.powHull(t.c).divideHull(t.b.powHull(t.c));
            assertTrue(t, expression1.contains(expression2));
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                ZERO.powHull(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesElementCompare() {
        initialize("");
        System.out.println("\t\ttesting elementCompare(Interval) properties...");

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            @SuppressWarnings("UnusedDeclaration")
            Optional<Ordering> compare = p.a.elementCompare(p.b);
        }

        Iterable<Pair<Interval, Interval>> ps = filter(
                q -> q.a.elementCompare(q.b).isPresent(),
                P.pairs(P.intervals())
        );
        for (Pair<Interval, Interval> p : take(LIMIT, ps)) {
            Ordering compare = p.a.elementCompare(p.b).get();
            assertEquals(p, p.b.elementCompare(p.a).get(), compare.invert());
        }

        ps = filter(
                q -> !q.a.elementCompare(q.b).isPresent(),
                P.pairs(P.intervals())
        );
        for (Pair<Interval, Interval> p : take(LIMIT, ps)) {
            assertFalse(p, p.b.elementCompare(p.a).isPresent());
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            Optional<Ordering> compare = a.elementCompare(a);
            assertTrue(a, !compare.isPresent() || compare.get() == EQ);
            assertEquals(a, a.elementCompare(ZERO).map(Ordering::toInt), a.signum());
        }

        Iterable<Triple<Interval, Interval, Interval>> ts = filter(
                t -> t.a.elementCompare(t.b).equals(Optional.of(LT)) &&
                     t.b.elementCompare(t.c).equals(Optional.of(LT)), ///
                P.triples(P.intervals())
        );
        for (Triple<Interval, Interval, Interval> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.elementCompare(t.c), Optional.of(LT));
        }
    }

    private void propertiesEquals() {
        initialize("");
        System.out.println("\t\ttesting equals(Object) properties...");

        propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::intervals);
    }

    private void propertiesHashCode() {
        initialize("");
        System.out.println("\t\ttesting hashCode() properties...");

        propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::intervals);
    }

    private void propertiesCompareTo() {
        initialize("");
        System.out.println("\t\ttesting compareTo(Interval) properties...");

        propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::intervals);
    }

    private void propertiesRead() {
        initialize("");
        System.out.println("\t\ttesting read(String) properties...");

        for (String s : take(LIMIT, P.strings())) {
            read(s);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            Optional<Interval> oi = read(a.toString());
            assertEquals(a, oi.get(), a);
        }
    }

    private void propertiesFindIn() {
        initialize("");
        System.out.println("\t\ttesting findIn(String) properties...");

        propertiesFindInHelper(LIMIT, P.getWheelsProvider(), P.intervals(), Interval::read, Interval::findIn, a -> {});
    }

    private void propertiesToString() {
        initialize("");
        System.out.println("\t\ttesting toString() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            String s = a.toString();
            assertTrue(a, isSubsetOf(s, INTERVAL_CHARS));
            Optional<Interval> readI = read(s);
            assertTrue(a, readI.isPresent());
            assertEquals(a, readI.get(), a);
        }
    }
}
