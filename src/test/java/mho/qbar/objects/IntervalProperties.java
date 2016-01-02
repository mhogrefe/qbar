package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
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
        compareImplementationsDisjoint();
        propertiesUnion();
        compareImplementationsUnion();
        propertiesComplement();
        propertiesMidpoint();
        propertiesSplit();
        propertiesBisect();
        propertiesRoundingPreimage_float();
        propertiesRoundingPreimage_double();
        propertiesRoundingPreimage_BigDecimal();
        propertiesAdd();
        propertiesNegate();
        propertiesAbs();
        propertiesSignum();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesMultiply_Interval();
        propertiesMultiply_Rational();
        propertiesMultiply_BigInteger();
        propertiesMultiply_int();
        propertiesInvert();
        propertiesInvertHull();
        compareImplementationsInvertHull();
        propertiesDivide_Interval();
        propertiesDivideHull();
        compareImplementationsDivideHull();
        propertiesDivide_Rational();
        propertiesDivide_BigInteger();
        propertiesDivide_int();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesShiftRight();
        compareImplementationsShiftRight();
        propertiesSum();
        compareImplementationsSum();
        propertiesProduct();
        propertiesDelta();
        propertiesPow();
        propertiesPowHull();
        compareImplementationsPowHull();
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
        for (Pair<Rational, Rational> p : take(LIMIT, P.bagPairs(P.rationals()))) {
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
        initialize("contains(Rational)");
        for (Pair<Interval, Rational> p : take(LIMIT, P.pairs(P.intervals(), P.rationals()))) {
            p.a.contains(p.b);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(r, ALL.contains(r));
            assertTrue(r, of(r).contains(r));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.distinctPairs(P.rationals()))) {
            assertFalse(p, of(p.a).contains(p.b));
        }
    }

    private void propertiesContains_Interval() {
        initialize("contains(Interval)");
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            p.a.contains(p.b);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a, ALL.contains(a));
            assertTrue(a, a.contains(a));
        }

        Iterable<Pair<Interval, Interval>> ps = filterInfinite(q -> q.a.contains(q.b), P.pairs(P.intervals()));
        for (Pair<Interval, Interval> p : take(LIMIT, ps)) {
            Optional<Rational> ad = p.a.diameter();
            Optional<Rational> bd = p.b.diameter();
            assertTrue(p, !ad.isPresent() || le(bd.get(), ad.get()));
            assertTrue(p, p.a.convexHull(p.b).equals(p.a));
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.b))) {
                assertTrue(p, p.a.contains(r));
            }
        }

        Iterable<Pair<Rational, Interval>> ps2 = filterInfinite(
                q -> !q.b.equals(of(q.a)),
                P.pairs(P.rationals(), P.intervals())
        );
        for (Pair<Rational, Interval> p : take(LIMIT, ps2)) {
            assertFalse(p, of(p.a).contains(p.b));
        }
    }

    private void propertiesDiameter() {
        initialize("diameter()");
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
        initialize("convexHull(Interval)");
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Interval c = p.a.convexHull(p.b);
            c.validate();
            commutative(Interval::convexHull, p);
            //Given an interval c whose endpoints are in each of two intervals a and b, any Rational in c lies in the
            //convex hull of a and b
            for (Pair<Rational, Rational> q : take(TINY_LIMIT, P.pairs(P.rationalsIn(p.a), P.rationalsIn(p.b)))) {
                Pair<Rational, Rational> minMax = minMax(q.a, q.b);
                for (Rational r : take(TINY_LIMIT, P.rationalsIn(of(minMax.a, minMax.b)))) {
                    assertTrue(p, c.contains(r));
                }
            }
        }

        for (Triple<Interval, Interval, Interval> t : take(LIMIT, P.triples(P.intervals()))) {
            associative(Interval::convexHull, t);
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.finitelyBoundedIntervals()))) {
            Interval c = p.a.convexHull(p.b);
            assertTrue(p, ge(c.diameter().get(), p.a.diameter().get()));
            assertTrue(p, ge(c.diameter().get(), p.b.diameter().get()));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(b -> b.convexHull(b), a);
            fixedPoint(a::convexHull, ALL);
            fixedPoint(b -> b.convexHull(a), ALL);
        }
    }

    private static @NotNull Pair<Rational, Rational> convexHull_List_Interval_alt(@NotNull List<Interval> as) {
        if (isEmpty(as)) {
            throw new IllegalArgumentException("as cannot be empty.");
        }
        List<Optional<Rational>> lowers = toList(map(Interval::getLower, as));
        List<Optional<Rational>> uppers = toList(map(Interval::getUpper, as));
        //noinspection RedundantCast
        Rational lower = any(a -> !a.isPresent(), lowers) ?
                null :
                minimum((Iterable<Rational>) map(Optional::get, lowers));
        //noinspection RedundantCast
        Rational upper = any(a -> !a.isPresent(), uppers) ?
                null :
                maximum((Iterable<Rational>) map(Optional::get, uppers));
        return new Pair<>(lower, upper);
    }

    private void propertiesConvexHull_List_Interval() {
        initialize("convexHull(List<Interval>)");
        for (List<Interval> as : take(LIMIT, P.listsAtLeast(1, P.intervals()))) {
            Interval c = convexHull(as);
            assertEquals(
                    as,
                    convexHull_List_Interval_alt(as),
                    new Pair<>(c.getLower().orElse(null), c.getUpper().orElse(null))
            );
            c.validate();
        }

        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.intervals(),
                Interval::convexHull,
                Interval::convexHull,
                Interval::validate,
                false,
                true
        );

        for (List<Interval> as : take(LIMIT, P.listsAtLeast(1, P.finitelyBoundedIntervals()))) {
            Interval c = convexHull(as);
            Rational cd = c.diameter().get();
            assertTrue(as, all(a -> ge(cd, a.diameter().get()), as));
        }

        Iterable<Pair<Interval, Integer>> ps2 = P.pairsLogarithmicOrder(P.intervals(), P.positiveIntegersGeometric());
        for (Pair<Interval, Integer> p : take(LIMIT, ps2)) {
            assertEquals(p, convexHull(toList(replicate(p.b, p.a))), p.a);
        }

        for (List<Interval> as : take(LIMIT, P.listsWithElement(ALL, P.intervals()))) {
            assertEquals(as, convexHull(as), ALL);
        }
    }

    private void compareImplementationsConvexHull_List_Interval() {
        Map<String, Function<List<Interval>, Pair<Rational, Rational>>> functions = new LinkedHashMap<>();
        functions.put("alt", IntervalProperties::convexHull_List_Interval_alt);
        functions.put(
                "standard",
                as -> {
                    Interval a = convexHull(as);
                    return new Pair<>(a.getLower().orElse(null), a.getUpper().orElse(null));
                }
        );
        compareImplementations("convexHull(List<Interval>)", take(LIMIT, P.listsAtLeast(1, P.intervals())), functions);
    }

    private void propertiesIntersection() {
        initialize("intersection(Interval)");
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Optional<Interval> oi = p.a.intersection(p.b);
            if (oi.isPresent()) {
                oi.get().validate();
            }
            commutative(Interval::intersection, p);
            assertEquals(p, oi.isPresent(), !p.a.disjoint(p.b));
        }

        Iterable<Triple<Optional<Interval>, Optional<Interval>, Optional<Interval>>> ts =
                P.triples(P.nonEmptyOptionals(P.intervals()));
        for (Triple<Optional<Interval>, Optional<Interval>, Optional<Interval>> t : take(LIMIT, ts)) {
            associative(
                    (a, b) -> !a.isPresent() || !b.isPresent() ? Optional.empty() : a.get().intersection(b.get()),
                    t
            );
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.finitelyBoundedIntervals()))) {
            Optional<Interval> oi = p.a.intersection(p.b);
            if (oi.isPresent()) {
                assertTrue(p, le(oi.get().diameter().get(), p.a.diameter().get()));
                assertTrue(p, le(oi.get().diameter().get(), p.b.diameter().get()));
            }
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(b -> ALL.intersection(b).get(), a);
            fixedPoint(b -> b.intersection(ALL).get(), a);
            fixedPoint(b -> b.intersection(b).get(), a);
        }

        Iterable<Pair<Interval, Interval>> ps = filterInfinite(q -> !q.a.disjoint(q.b), P.pairs(P.intervals()));
        for (Pair<Interval, Interval> p : take(LIMIT, ps)) {
            Interval intersection = p.a.intersection(p.b).get();
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(intersection))) {
                assertTrue(p, p.a.contains(r));
                assertTrue(p, p.b.contains(r));
            }
        }
    }

    private static boolean disjoint_simplest(@NotNull Interval a, @NotNull Interval b) {
        return !a.intersection(b).isPresent();
    }

    private void propertiesDisjoint() {
        initialize("disjoint(Interval)");
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            assertEquals(p, p.a.disjoint(p.b), disjoint_simplest(p.a, p.b));
            commutative(Interval::disjoint, p);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertFalse(a, ALL.disjoint(a));
            assertFalse(a, a.disjoint(a));
        }

        Iterable<Pair<Interval, Interval>> ps = filterInfinite(q -> q.a.disjoint(q.b), P.pairs(P.intervals()));
        for (Pair<Interval, Interval> p : take(LIMIT, ps)) {
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertFalse(p, p.b.contains(r));
            }
        }
    }

    private void compareImplementationsDisjoint() {
        Map<String, Function<Pair<Interval, Interval>, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> disjoint_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.disjoint(p.b));
        compareImplementations("disjoint(Interval)", take(LIMIT, P.pairs(P.intervals())), functions);
    }

    private static @NotNull  List<Interval> union_alt(@NotNull List<Interval> as) {
        if (as.size() < 2) return as;
        List<Interval> simplified = new ArrayList<>();
        Interval acc = null;
        for (Interval a : sort(as)) {
            if (acc == null) {
                acc = a;
            } else if (acc.disjoint(a)) {
                simplified.add(acc);
                acc = a;
            } else {
                acc = acc.convexHull(a);
            }
        }
        if (acc != null) simplified.add(acc);
        return simplified;
    }

    private void propertiesUnion() {
        initialize("union(List<Interval>)");
        for (List<Interval> as : take(LIMIT, P.lists(P.intervals()))) {
            List<Interval> union = union(as);
            union.forEach(Interval::validate);
            assertEquals(as, union, union_alt(as));
            assertTrue(as, increasing(union));
            assertTrue(as.toString(), and(map(p -> p.a.disjoint(p.b), EP.distinctPairs(union))));
            for (Rational r : take(TINY_LIMIT, mux(toList(map(P::rationalsIn, as))))) { //todo use choose
                assertTrue(as, or(map(a -> a.contains(r), union)));
            }
        }

        Iterable<Pair<List<Interval>, List<Interval>>> ps = P.dependentPairs(
                P.lists(P.intervals()),
                P::permutationsFinite
        );
        for (Pair<List<Interval>, List<Interval>> p : take(LIMIT, ps)) {
            assertEquals(p, union(p.a), union(p.b));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(Interval::union, Collections.singletonList(a));
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            commutative((a, b) -> union(Arrays.asList(a, b)), p);
        }

        Iterable<Pair<Interval, Interval>> ps2 = filterInfinite(q -> q.a.disjoint(q.b), P.pairs(P.intervals()));
        for (Pair<Interval, Interval> p : take(LIMIT, ps2)) {
            assertEquals(p, union(Pair.toList(p)), sort(Pair.toList(p)));
        }

        ps2 = filterInfinite(q -> !q.a.disjoint(q.b), P.pairs(P.intervals()));
        for (Pair<Interval, Interval> p : take(LIMIT, ps2)) {
            assertEquals(p, union(Pair.toList(p)), Collections.singletonList(convexHull(Pair.toList(p))));
        }

        for (List<Interval> as : take(LIMIT, P.listsWithElement(ALL, P.intervals()))) {
            assertEquals(as, union(as), Collections.singletonList(ALL));
        }

        for (List<Interval> as : take(LIMIT, P.listsWithElement(null, P.intervals()))) {
            try {
                union(as);
                fail(as);
            } catch (NullPointerException ignored) {}
        }
    }

    private void compareImplementationsUnion() {
        Map<String, Function<List<Interval>, List<Interval>>> functions = new LinkedHashMap<>();
        functions.put("alt", IntervalProperties::union_alt);
        functions.put("standard", Interval::union);
        compareImplementations("union(List<Interval>)", take(LIMIT, P.lists(P.intervals())), functions);
    }

    private void propertiesComplement() {
        initialize("complement()");
        for (Interval a : take(LIMIT, P.intervals())) {
            List<Interval> complement = a.complement();
            complement.forEach(Interval::validate);
            assertEquals(a, union(complement), complement);
            List<Rational> endpoints = new ArrayList<>();
            if (a.getLower().isPresent()) endpoints.add(a.getLower().get());
            if (a.getUpper().isPresent()) endpoints.add(a.getUpper().get());
            for (Rational endpoint : endpoints) {
                any(b -> b.contains(endpoint), complement);
            }
        }

        Iterable<Interval> as = filterInfinite(a -> !a.diameter().equals(Optional.of(Rational.ZERO)), P.intervals());
        for (Interval a : take(LIMIT, as)) {
            List<Interval> complement = a.complement();
            List<Rational> endpoints = new ArrayList<>();
            if (a.getLower().isPresent()) endpoints.add(a.getLower().get());
            if (a.getUpper().isPresent()) endpoints.add(a.getUpper().get());
            for (Rational r : take(TINY_LIMIT, filterInfinite(s -> !endpoints.contains(s), P.rationalsIn(a)))) {
                assertFalse(a, any(b -> b.contains(r), complement));
            }
        }

        for (Interval a : take(LIMIT, filterInfinite(b -> !b.equals(ALL), P.intervals()))) {
            List<Interval> complement = a.complement();
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

        as = filterInfinite(b -> b.diameter().get() != Rational.ZERO, P.finitelyBoundedIntervals());
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
        initialize("midpoint()");
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
        initialize("split(Rational)");
        Iterable<Pair<Interval, Rational>> ps = filterInfinite(
                q -> q.a.contains(q.b),
                P.pairs(P.intervals(), P.rationals())
        );
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

        for (Pair<Rational, Rational> p : take(LIMIT, P.bagPairs(P.rationals()))) {
            Interval a = lessThanOrEqualTo(p.b);
            Pair<Interval, Interval> q = a.split(p.a);
            assertFalse(p, q.a.getLower().isPresent());
            assertTrue(p, q.a.getUpper().isPresent());
            assertTrue(p, q.b.isFinitelyBounded());

            a = greaterThanOrEqualTo(p.a);
            q = a.split(p.b);
            assertTrue(p, q.a.isFinitelyBounded());
            assertTrue(p, q.b.getLower().isPresent());
            assertFalse(p, q.b.getUpper().isPresent());
        }

        Iterable<Pair<Interval, Rational>> psFail = filterInfinite(
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
        initialize("bisect()");
        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            Pair<Interval, Interval> bisection = a.bisect();
            bisection.a.validate();
            bisection.b.validate();
            assertTrue(a, bisection.a.isFinitelyBounded());
            assertTrue(a, bisection.b.isFinitelyBounded());
            assertEquals(a, bisection.a.diameter(), bisection.b.diameter());
            assertEquals(a, bisection.a.getUpper(), bisection.b.getLower());
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
        initialize("roundingPreimage(float)");
        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g), P.floats()))) {
            Interval a = roundingPreimage(f);
            a.validate();
            assertEquals(f, roundingPreimage(-f), a.negate());
        }

        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g) && Math.abs(g) < Float.MAX_VALUE, P.floats()))) {
            Interval a = roundingPreimage(f);
            Rational center = Rational.ofExact(f).get();
            Rational predecessor = Rational.ofExact(FloatingPointUtils.predecessor(f)).get();
            Rational successor = Rational.ofExact(FloatingPointUtils.successor(f)).get();
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(a))) {
                Rational centerDistance = center.subtract(r).abs();
                Rational predecessorDistance = predecessor.subtract(r).abs();
                Rational successorDistance = successor.subtract(r).abs();
                assertTrue(f, le(centerDistance, predecessorDistance));
                assertTrue(f, le(centerDistance, successorDistance));
            }
            for (Rational r : take(TINY_LIMIT, P.rationalsNotIn(a))) {
                Rational centralDistance = center.subtract(r).abs();
                Rational predecessorDistance = predecessor.subtract(r).abs();
                Rational successorDistance = successor.subtract(r).abs();
                assertTrue(f, gt(centralDistance, predecessorDistance) || gt(centralDistance, successorDistance));
            }

            Rational x = a.getLower().get();
            Rational y = a.getUpper().get();
            for (Rational r : take(TINY_LIMIT, filterInfinite(s -> !s.equals(x) && !s.equals(y), P.rationalsIn(a)))) {
                assertEquals(
                        f,
                        FloatingPointUtils.absNegativeZeros(r.floatValue()),
                        FloatingPointUtils.absNegativeZeros(f)
                );
            }
            Iterable<Rational> rs = filterInfinite(s -> !s.equals(x) && !s.equals(y), P.rationalsNotIn(a));
            for (Rational r : take(TINY_LIMIT, rs)) {
                assertNotEquals(f, r.floatValue(), f);
            }
        }
    }

    private void propertiesRoundingPreimage_double() {
        initialize("roundingPreimage(double)");
        for (double d : take(LIMIT, filter(e -> !Double.isNaN(e), P.doubles()))) {
            Interval a = roundingPreimage(d);
            a.validate();
            assertEquals(d, roundingPreimage(-d), a.negate());
        }

        for (double d : take(LIMIT, filter(e -> !Double.isNaN(e) && Math.abs(e) < Double.MAX_VALUE, P.doubles()))) {
            Interval a = roundingPreimage(d);
            Rational center = Rational.ofExact(d).get();
            Rational predecessor = Rational.ofExact(FloatingPointUtils.predecessor(d)).get();
            Rational successor = Rational.ofExact(FloatingPointUtils.successor(d)).get();
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(a))) {
                Rational centerDistance = center.subtract(r).abs();
                Rational predecessorDistance = predecessor.subtract(r).abs();
                Rational successorDistance = successor.subtract(r).abs();
                assertTrue(d, le(centerDistance, predecessorDistance));
                assertTrue(d, le(centerDistance, successorDistance));
            }
            for (Rational r : take(TINY_LIMIT, P.rationalsNotIn(a))) {
                Rational centerDistance = center.subtract(r).abs();
                Rational predecessorDistance = predecessor.subtract(r).abs();
                Rational successorDistance = successor.subtract(r).abs();
                assertTrue(d, gt(centerDistance, predecessorDistance) || gt(centerDistance, successorDistance));
            }

            Rational x = a.getLower().get();
            Rational y = a.getUpper().get();
            for (Rational r : take(TINY_LIMIT, filterInfinite(s -> !s.equals(x) && !s.equals(y), P.rationalsIn(a)))) {
                assertEquals(
                        d,
                        FloatingPointUtils.absNegativeZeros(r.doubleValue()),
                        FloatingPointUtils.absNegativeZeros(d)
                );
            }
            Iterable<Rational> rs = filterInfinite(s -> !s.equals(x) && !s.equals(y), P.rationalsNotIn(a));
            for (Rational r : take(TINY_LIMIT, rs)) {
                assertNotEquals(d, r.doubleValue(), d);
            }
        }
    }

    private void propertiesRoundingPreimage_BigDecimal() {
        initialize("roundingPreimage(BigDecimal)");
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            Interval a = roundingPreimage(bd);
            a.validate();
            assertEquals(bd, roundingPreimage(bd.negate()), a.negate());
            Rational center = Rational.of(bd);
            Rational predecessor = Rational.of(BigDecimalUtils.predecessor(bd));
            Rational successor = Rational.of(BigDecimalUtils.successor(bd));
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(a))) {
                Rational centerDistance = center.subtract(r).abs();
                Rational predecessorDistance = predecessor.subtract(r).abs();
                Rational successorDistance = successor.subtract(r).abs();
                assertTrue(bd, le(centerDistance, predecessorDistance));
                assertTrue(bd, le(centerDistance, successorDistance));
            }
            for (Rational r : take(TINY_LIMIT, P.rationalsNotIn(a))) {
                Rational centerDistance = center.subtract(r).abs();
                Rational predecessorDistance = predecessor.subtract(r).abs();
                Rational successorDistance = successor.subtract(r).abs();
                assertTrue(bd, gt(centerDistance, predecessorDistance) || gt(centerDistance, successorDistance));
            }

            Rational x = a.getLower().get();
            Rational y = a.getUpper().get();
            for (Rational r : take(TINY_LIMIT, filterInfinite(s -> !s.equals(x) && !s.equals(y), P.rationalsIn(a)))) {
                assertEquals(bd, r.bigDecimalValueByScale(bd.scale()), bd);
            }
            Iterable<Rational> rs = filterInfinite(s -> !s.equals(x) && !s.equals(y), P.rationalsNotIn(a));
            for (Rational r : take(TINY_LIMIT, rs)) {
                assertNotEquals(bd, r.bigDecimalValueByScale(bd.scale()), bd);
            }
        }
    }

    private void propertiesAdd() {
        initialize("add(Interval)");
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Interval sum = p.a.add(p.b);
            sum.validate();
            commutative(Interval::add, p);
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
            fixedPoint(ZERO::add, a);
            fixedPoint(b -> b.add(ZERO), a);
            fixedPoint(ALL::add, ALL);
            fixedPoint(b -> b.add(ALL), ALL);
            assertTrue(a, a.subtract(a).contains(ZERO));
        }

        for (Triple<Interval, Interval, Interval> t : take(LIMIT, P.triples(P.intervals()))) {
            associative(Interval::add, t);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(Interval::of, Interval::of, Interval::of, Rational::add, Interval::add, p);
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (Interval a : take(LIMIT, P.intervals())) {
            Interval negative = a.negate();
            negative.validate();
            involution(Interval::negate, a);
            assertTrue(a, a.add(negative).contains(ZERO));
            assertEquals(a, a.diameter(), negative.diameter());
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(a))) {
                assertTrue(a, negative.contains(r.negate()));
            }
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            homomorphic(Interval::of, Interval::of, Rational::negate, Interval::negate, r);
        }
    }

    private void propertiesAbs() {
        initialize("abs()");
        Interval nonPositive = lessThanOrEqualTo(Rational.ZERO);
        for (Interval a : take(LIMIT, P.intervals())) {
            Interval abs = a.abs();
            abs.validate();
            idempotent(Interval::abs, a);
            Optional<Interval> nonPositiveIntersection = abs.intersection(nonPositive);
            assertTrue(a, !nonPositiveIntersection.isPresent() || nonPositiveIntersection.get().equals(ZERO));
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
            homomorphic(Interval::of, Interval::of, Rational::abs, Interval::abs, r);
        }
    }

    private void propertiesSignum() {
        initialize("signum()");
        for (Interval a : take(LIMIT, P.intervals())) {
            Optional<Integer> signum = a.signum();
            assertEquals(a, signum, a.elementCompare(ZERO).map(Ordering::toInt));
            if (signum.isPresent()) {
                int s = signum.get();
                assertTrue(a, s == -1 || s == 0 || s == 1);
                for (Rational r : take(TINY_LIMIT, P.rationalsIn(a))) {
                    assertTrue(a, r.signum() == s);
                }
            } else {
                assertTrue(a, !a.equals(ZERO));
                assertTrue(a, a.contains(ZERO));
            }
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            homomorphic(Interval::of, Optional::of, Rational::signum, Interval::signum, r);
        }
    }

    private static @NotNull Interval subtract_simplest(@NotNull Interval a, @NotNull Interval b) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(Interval)");
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Interval difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, subtract_simplest(p.a, p.b), difference);
            antiCommutative(Interval::subtract, Interval::negate, p);
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
            assertTrue(a, a.subtract(a).contains(ZERO));
            fixedPoint(b -> b.subtract(ZERO), a);
            fixedPoint(b -> b.subtract(a), ALL);
            fixedPoint(a::subtract, ALL);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(Interval::of, Interval::of, Interval::of, Rational::subtract, Interval::subtract, p);
        }
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<Interval, Interval>, Interval>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        compareImplementations("subtract(Interval)", take(LIMIT, P.pairs(P.intervals())), functions);
    }

    private void propertiesMultiply_Interval() {
        initialize("multiply(Interval)");
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            Interval product = p.a.multiply(p.b);
            product.validate();
            commutative(Interval::multiply, p);
            for (Pair<Rational, Rational> q : take(TINY_LIMIT, P.pairs(P.rationalsIn(p.a), P.rationalsIn(p.b)))) {
                assertTrue(p, product.contains(q.a.multiply(q.b)));
            }
        }

        Iterable<Pair<Interval, Interval>> ps = P.pairs(
                P.intervals(),
                filterInfinite(a -> !a.equals(ZERO), P.intervals())
        );
        for (Pair<Interval, Interval> p : take(LIMIT, ps)) {
            Interval product = p.a.multiply(p.b);
            List<Interval> quotient = product.divide(p.b);
            assertTrue(p, any(a -> a.contains(p.a), quotient));
            quotient = union(toList(concatMap(p.a::divide, p.b.invert())));
            assertTrue(p, quotient.contains(product));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(b -> b.multiply(ONE), a);
            fixedPoint(ONE::multiply, a);
            fixedPoint(a::multiply, ZERO);
            fixedPoint(b -> b.multiply(a), ZERO);
        }

        for (Interval a : take(LIMIT, filterInfinite(b -> !b.equals(ZERO), P.intervals()))) {
            fixedPoint(b -> b.multiply(a), ALL);
            fixedPoint(a::multiply, ALL);
            List<Interval> inverse = a.invert();
            List<Interval> back = union(toList(map(a::multiply, inverse)));
            assertTrue(a, any(b -> b.contains(ONE), back));
        }

        for (Triple<Interval, Interval, Interval> t : take(LIMIT, P.triples(P.intervals()))) {
            associative(Interval::multiply, t);
            Interval expression1 = t.a.multiply(t.b.add(t.c));
            Interval expression2 = t.a.multiply(t.b).add(t.a.multiply(t.c));
            assertTrue(t, expression2.contains(expression1));
            Interval expression3 = t.a.add(t.b).multiply(t.c);
            Interval expression4 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertTrue(t, expression4.contains(expression3));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(Interval::of, Interval::of, Interval::of, Rational::multiply, Interval::multiply, p);
        }
    }

    private void propertiesMultiply_Rational() {
        initialize("multiply(Rational)");
        for (Pair<Interval, Rational> p : take(LIMIT, P.pairs(P.intervals(), P.rationals()))) {
            Interval a = p.a.multiply(p.b);
            a.validate();
            assertEquals(p, a, p.a.multiply(of(p.b)));
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, a.contains(r.multiply(p.b)));
            }
        }

        Iterable<Pair<Interval, Rational>> ps = P.pairs(P.intervals(), P.nonzeroRationals());
        for (Pair<Interval, Rational> p : take(LIMIT, ps)) {
            inverse(a -> a.multiply(p.b), (Interval a) -> a.divide(p.b), p.a);
            assertEquals(p, p.a.multiply(p.b), p.a.divide(p.b.invert()));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(b -> b.multiply(Rational.ONE), a);
            assertEquals(a, a.multiply(Rational.ZERO), ZERO);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            fixedPoint(a -> a.multiply(r), ZERO);
            assertEquals(r, ONE.multiply(r), of(r));
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            fixedPoint(a -> a.multiply(r), ALL);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(Interval::of, Function.identity(), Interval::of, Rational::multiply, Interval::multiply, p);
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        for (Pair<Interval, BigInteger> p : take(LIMIT, P.pairs(P.intervals(), P.bigIntegers()))) {
            Interval a = p.a.multiply(p.b);
            a.validate();
            assertEquals(p, a, p.a.multiply(Rational.of(p.b)));
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, a.contains(r.multiply(p.b)));
            }
        }

        for (Pair<Interval, BigInteger> p : take(LIMIT, P.pairs(P.intervals(), P.nonzeroBigIntegers()))) {
            inverse(a -> a.multiply(p.b), (Interval a) -> a.divide(p.b), p.a);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(b -> b.multiply(BigInteger.ONE), a);
            assertEquals(a, a.multiply(BigInteger.ZERO), ZERO);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            fixedPoint(a -> a.multiply(i), ZERO);
            assertEquals(i.toString(), ONE.multiply(i), of(Rational.of(i)));
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            fixedPoint(a -> a.multiply(i), ALL);
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.bigIntegers()))) {
            homomorphic(Interval::of, Function.identity(), Interval::of, Rational::multiply, Interval::multiply, p);
        }
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.integers()))) {
            Interval a = p.a.multiply(p.b);
            a.validate();
            assertEquals(p, a, p.a.multiply(Rational.of(p.b)));
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, a.contains(r.multiply(p.b)));
            }
        }

        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.nonzeroIntegers()))) {
            inverse(a -> a.multiply(p.b), (Interval a) -> a.divide(p.b), p.a);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(b -> b.multiply(1), a);
            assertEquals(a, a.multiply(0), ZERO);
        }

        for (int i : take(LIMIT, P.integers())) {
            fixedPoint(a -> a.multiply(i), ZERO);
            assertEquals(i, ONE.multiply(i), of(Rational.of(i)));
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            fixedPoint(a -> a.multiply(i), ALL);
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integers()))) {
            homomorphic(Interval::of, Function.identity(), Interval::of, Rational::multiply, Interval::multiply, p);
        }
    }

    private void propertiesInvert() {
        initialize("invert()");
        for (Interval a : take(LIMIT, P.intervals())) {
            List<Interval> inverse = a.invert();
            inverse.forEach(Interval::validate);
            int size = inverse.size();
            assertTrue(a, size >= 0 && size <= 2);
            if (size == 1) {
                Interval i = inverse.get(0);
                Optional<Rational> x = i.getLower();
                Optional<Rational> y = i.getUpper();
                if (x.isPresent() && y.isPresent()) {
                    if (x.get().equals(y.get())) {
                        assertNotEquals(a, x.get(), Rational.ZERO);
                    } else {
                        int ps = x.get().signum();
                        int qs = y.get().signum();
                        if (qs == 1) {
                            assertTrue(a, ps != -1);
                        }
                        if (ps == -1) {
                            assertTrue(a, qs != 1);
                        }
                    }
                } else if (y.isPresent()) {
                    assertTrue(a, le(y.get(), Rational.ZERO));
                } else if (x.isPresent()) {
                    assertTrue(a, ge(x.get(), Rational.ZERO));
                }
            } else if (size == 2) {
                Interval i = inverse.get(0);
                Interval j = inverse.get(1);
                Optional<Rational> ix = i.getLower();
                Optional<Rational> iy = i.getUpper();
                Optional<Rational> jx = j.getLower();
                Optional<Rational> jy = j.getUpper();
                assertFalse(a, ix.isPresent());
                assertTrue(a, iy.isPresent());
                assertTrue(a, jx.isPresent());
                assertFalse(a, jy.isPresent());
                if (jx.get() == Rational.ZERO) {
                    assertTrue(a, iy.get().signum() == -1);
                } else if (iy.get() == Rational.ZERO) {
                    assertTrue(a, jx.get().signum() == 1);
                } else {
                    assertTrue(a, iy.get().signum() == -1);
                    assertTrue(a, jx.get().signum() == 1);
                }
            }
        }

        Iterable<Interval> as = filterInfinite(a -> !a.diameter().equals(Optional.of(Rational.ZERO)), P.intervals());
        for (Interval a : take(LIMIT, as)) {
            List<Interval> inverse = a.invert();
            for (Rational r : take(TINY_LIMIT, filterInfinite(s -> s != Rational.ZERO, P.rationalsIn(a)))) {
                assertTrue(a, any(b -> b.contains(r.invert()), inverse));
            }
        }
    }

    private static @NotNull Interval invertHull_simplest(@NotNull Interval a) {
        List<Interval> inverse = a.invert();
        switch (inverse.size()) {
            case 0:
                throw new ArithmeticException("division by zero");
            case 1:
                return inverse.get(0);
            default:
                return ALL;
        }
    }

    private void propertiesInvertHull() {
        initialize("invertHull()");
        for (Interval a : take(LIMIT, filterInfinite(b -> !b.equals(ZERO), P.intervals()))) {
            Interval inverse = a.invertHull();
            inverse.validate();
            assertEquals(a, invertHull_simplest(a), inverse);
            Optional<Rational> x = inverse.getLower();
            Optional<Rational> y = inverse.getUpper();
            if (x.isPresent() && y.isPresent()) {
                if (x.equals(y)) {
                    assertNotEquals(a, x.get(), ZERO);
                } else {
                    int ps = x.get().signum();
                    int qs = y.get().signum();
                    if (qs == 1) {
                        assertTrue(a, ps != -1);
                    }
                    if (ps == -1) {
                        assertTrue(a, qs != 1);
                    }
                }
            } else if (y.isPresent()) {
                assertTrue(a, le(y.get(), Rational.ZERO));
            } else if (x.isPresent()) {
                assertTrue(a, ge(x.get(), Rational.ZERO));
            }
            assertTrue(a, all(inverse::contains, a.invert()));
            Interval back = inverse.invertHull();
            assertTrue(a, back.contains(a));
            assertTrue(a, a.multiply(inverse).contains(ONE));
            for (Rational r : take(TINY_LIMIT, filter(s -> s != Rational.ZERO, P.rationalsIn(a)))) {
                assertTrue(a, inverse.contains(r.invert()));
            }
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            homomorphic(Interval::of, Interval::of, Rational::invert, Interval::invertHull, r);
        }
    }

    private void compareImplementationsInvertHull() {
        Map<String, Function<Interval, Interval>> functions = new LinkedHashMap<>();
        functions.put("simplest", IntervalProperties::invertHull_simplest);
        functions.put("standard", Interval::invertHull);
        Iterable<Interval> as = filterInfinite(a -> !a.equals(ZERO), P.intervals());
        compareImplementations("invertHull()", take(LIMIT, as), functions);
    }

    private void propertiesDivide_Interval() {
        initialize("divide(Interval)");
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            List<Interval> quotient = p.a.divide(p.b);
            quotient.forEach(Interval::validate);
            int size = quotient.size();
            assertTrue(p, size >= 0 && size <= 2);
            if (size == 2) {
                Interval i = quotient.get(0);
                Interval j = quotient.get(1);
                Optional<Rational> ix = i.getLower();
                Optional<Rational> iy = i.getUpper();
                Optional<Rational> jx = j.getLower();
                Optional<Rational> jy = j.getUpper();
                assertFalse(p, ix.isPresent());
                assertTrue(p, iy.isPresent());
                assertTrue(p, jx.isPresent());
                assertFalse(p, jy.isPresent());
                if (jx.get() == Rational.ZERO) {
                    assertTrue(p, iy.get().signum() == -1);
                } else if (iy.get() == Rational.ZERO) {
                    assertTrue(p, jx.get().signum() == 1);
                } else {
                    assertTrue(p, iy.get().signum() == -1);
                    assertTrue(p, jx.get().signum() == 1);
                }
            }
            Iterable<Pair<Rational, Rational>> ps = P.pairs(
                    P.rationalsIn(p.a),
                    filter(r -> r != Rational.ZERO, P.rationalsIn(p.b))
            );
            for (Pair<Rational, Rational> q : take(TINY_LIMIT, ps)) {
                assertTrue(p, any(b -> b.contains(q.a.divide(q.b)), quotient));
            }
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(b -> b.divide(ONE).get(0), a);
        }

        for (Interval a : take(LIMIT, filter(b -> !b.equals(ZERO), P.intervals()))) {
            assertEquals(a, ONE.divide(a), a.invert());
            assertTrue(a, any(b -> b.contains(ONE), a.divide(a)));
        }
    }

    private static @NotNull Interval divideHull_alt(@NotNull Interval a, @NotNull Interval b) {
        List<Interval> quotient = a.divide(b);
        switch (quotient.size()) {
            case 0:
                throw new ArithmeticException("division by zero");
            case 1:
                return quotient.get(0);
            default:
                return ALL;
        }
    }

    private void propertiesDivideHull() {
        initialize("divideHull(Interval)");
        Iterable<Pair<Interval, Interval>> ps = P.pairs(
                P.intervals(),
                filterInfinite(a -> !a.equals(ZERO), P.intervals())
        );
        for (Pair<Interval, Interval> p : take(LIMIT, ps)) {
            Interval quotient = p.a.divideHull(p.b);
            quotient.validate();
            assertEquals(p, divideHull_alt(p.a, p.b), quotient);
            assertTrue(p, quotient.multiply(p.b).contains(p.a));
            Iterable<Pair<Rational, Rational>> qs = P.pairs(
                    P.rationalsIn(p.a),
                    filter(r -> r != Rational.ZERO, P.rationalsIn(p.b))
            );
            for (Pair<Rational, Rational> q : take(TINY_LIMIT, qs)) {
                assertTrue(p, quotient.contains(q.a.divide(q.b)));
            }
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(filterInfinite(a -> !a.equals(ZERO), P.intervals())))) {
            assertTrue(p, p.b.divideHull(p.a).invertHull().contains(p.a.divideHull(p.b)));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(b -> b.divideHull(ONE), a);
        }

        for (Interval a : take(LIMIT, filterInfinite(b -> !b.equals(ZERO), P.intervals()))) {
            assertEquals(a, ONE.divideHull(a), a.invertHull());
            assertTrue(a, a.divideHull(a).contains(ONE));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroRationals()))) {
            homomorphic(Interval::of, Interval::of, Interval::of, Rational::divide, Interval::divideHull, p);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            try {
                a.divideHull(ZERO);
                fail(a);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsDivideHull() {
        Map<String, Function<Pair<Interval, Interval>, Interval>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> divideHull_alt(p.a, p.b));
        functions.put("standard", p -> p.a.divideHull(p.b));
        Iterable<Pair<Interval, Interval>> ps = P.pairs(
                P.intervals(),
                filterInfinite(a -> !a.equals(ZERO), P.intervals())
        );
        compareImplementations("divideHull(Interval)", take(LIMIT, ps), functions);
    }

    private void propertiesDivide_Rational() {
        initialize("divide(Rational)");
        for (Pair<Interval, Rational> p : take(LIMIT, P.pairs(P.intervals(), P.nonzeroRationals()))) {
            Interval quotient = p.a.divide(p.b);
            quotient.validate();
            assertEquals(p, p.a.multiply(p.b.invert()), quotient);
            inverse(a -> a.divide(p.b), (Interval a) -> a.multiply(p.b), p.a);
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, quotient.contains(r.divide(p.b)));
            }
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(b -> b.divide(Rational.ONE), a);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            assertEquals(r, ONE.divide(r), of(r.invert()));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroRationals()))) {
            homomorphic(Interval::of, Function.identity(), Interval::of, Rational::divide, Interval::divide, p);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            try {
                a.divide(Rational.ZERO);
                fail(a);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_BigInteger() {
        initialize("divide(BigInteger)");
        for (Pair<Interval, BigInteger> p : take(LIMIT, P.pairs(P.intervals(), P.nonzeroBigIntegers()))) {
            Interval quotient = p.a.divide(p.b);
            quotient.validate();
            assertEquals(p, p.a.multiply(Rational.of(p.b).invert()), quotient);
            inverse(a -> a.divide(p.b), (Interval a) -> a.multiply(p.b), p.a);
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, quotient.contains(r.divide(p.b)));
            }
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(b -> b.divide(BigInteger.ONE), a);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            assertEquals(i, ONE.divide(i), of(Rational.of(i).invert()));
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroBigIntegers()))) {
            homomorphic(Interval::of, Function.identity(), Interval::of, Rational::divide, Interval::divide, p);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            try {
                a.divide(BigInteger.ZERO);
                fail(a);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_int() {
        initialize("divide(int)");
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.nonzeroIntegers()))) {
            Interval quotient = p.a.divide(p.b);
            quotient.validate();
            assertEquals(p, p.a.multiply(Rational.of(p.b).invert()), quotient);
            inverse(a -> a.divide(p.b), (Interval a) -> a.multiply(p.b), p.a);
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, quotient.contains(r.divide(p.b)));
            }
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(b -> b.divide(1), a);
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            assertEquals(i, ONE.divide(i), of(Rational.of(i).invert()));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroIntegers()))) {
            homomorphic(Interval::of, Function.identity(), Interval::of, Rational::divide, Interval::divide, p);
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
        initialize("shiftLeft(int)");
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.integersGeometric()))) {
            homomorphic(
                    Interval::negate,
                    Function.identity(),
                    Interval::negate,
                    Interval::shiftLeft,
                    Interval::shiftLeft,
                    p
            );
            Interval shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            assertEquals(p, p.a.signum(), shifted.signum());
            assertEquals(p, p.a.isFinitelyBounded(), shifted.isFinitelyBounded());
            inverse(a -> a.shiftLeft(p.b), (Interval a) -> a.shiftRight(p.b), p.a);
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, shifted.contains(r.shiftLeft(p.b)));
            }
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(b -> b.shiftLeft(0), a);
        }

        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.naturalIntegersGeometric()))) {
            assertEquals(p, p.a.shiftLeft(p.b), p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            homomorphic(Interval::of, Function.identity(), Interval::of, Rational::shiftLeft, Interval::shiftLeft, p);
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<Interval, Integer>, Interval>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        Iterable<Pair<Interval, Integer>> ps = P.pairs(P.intervals(), P.integersGeometric());
        compareImplementations("shiftLeft(int)", take(LIMIT, ps), functions);
    }

    private static @NotNull Interval shiftRight_simplest(@NotNull Interval a, int bits) {
        if (bits < 0) {
            return a.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return a.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftRight() {
        initialize("shiftRight(int)");
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.integersGeometric()))) {
            homomorphic(
                    Interval::negate,
                    Function.identity(),
                    Interval::negate,
                    Interval::shiftRight,
                    Interval::shiftRight,
                    p
            );
            Interval shifted = p.a.shiftRight(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftRight_simplest(p.a, p.b));
            assertEquals(p, p.a.signum(), shifted.signum());
            assertEquals(p, p.a.isFinitelyBounded(), shifted.isFinitelyBounded());
            inverse(a -> a.shiftRight(p.b), (Interval a) -> a.shiftLeft(p.b), p.a);
            for (Rational r : take(TINY_LIMIT, P.rationalsIn(p.a))) {
                assertTrue(p, shifted.contains(r.shiftRight(p.b)));
            }
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            fixedPoint(b -> b.shiftRight(0), a);
        }

        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.naturalIntegersGeometric()))) {
            assertEquals(p, p.a.shiftRight(p.b), p.a.divide(BigInteger.ONE.shiftLeft(p.b)));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            homomorphic(
                    Interval::of,
                    Function.identity(),
                    Interval::of,
                    Rational::shiftRight,
                    Interval::shiftRight,
                    p
            );
        }
    }

    private void compareImplementationsShiftRight() {
        Map<String, Function<Pair<Interval, Integer>, Interval>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftRight_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftRight(p.b));
        Iterable<Pair<Interval, Integer>> ps = P.pairs(P.intervals(), P.integersGeometric());
        compareImplementations("shiftRight(int)", take(LIMIT, ps), functions);
    }

    private static @NotNull Interval sum_simplest(@NotNull List<Interval> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        return foldl(Interval::add, ONE, xs);
    }

    private void propertiesSum() {
        initialize("sum(Iterable<Interval>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.intervals(),
                Interval::add,
                Interval::sum,
                a -> {},
                true,
                true
        );

        for (List<Interval> is : take(LIMIT, P.lists(P.intervals()))) {
            Interval sum = sum(is);
            for (List<Rational> rs : take(TINY_LIMIT, transposeTruncating(map(P::rationalsIn, is)))) {
                assertTrue(is, sum.contains(Rational.sum(rs)));
            }
            assertEquals(is, sum.isFinitelyBounded(), is.isEmpty() || all(Interval::isFinitelyBounded, is));
        }

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            homomorphic(ss -> toList(map(Interval::of, rs)), Interval::of, Rational::sum, Interval::sum, rs);
        }
    }

    private void compareImplementationsSum() {
        Map<String, Function<List<Interval>, Interval>> functions = new LinkedHashMap<>();
        functions.put("simplest", IntervalProperties::sum_simplest);
        functions.put("standard", Interval::sum);
        compareImplementations("sum(Iterable<Interval>)", take(LIMIT, P.lists(P.intervals())), functions);
    }

    private void propertiesProduct() {
        initialize("product(Iterable<Interval>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.intervals(),
                Interval::multiply,
                Interval::product,
                a -> {},
                true,
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

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            homomorphic(ss -> toList(map(Interval::of, rs)), Interval::of, Rational::product, Interval::product, rs);
        }
    }

    private void propertiesDelta() {
        initialize("delta(Iterable<Interval>)");
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

        for (List<Interval> as : take(LIMIT, P.listsAtLeast(1, P.intervals()))) {
            Iterable<Interval> deltas = delta(as);
            for (List<Rational> rs : take(TINY_LIMIT, transposeTruncating(map(P::rationalsIn, as)))) {
                assertTrue(as, and(zipWith(Interval::contains, deltas, Rational.delta(rs))));
            }
        }

        for (List<Rational> rs : take(LIMIT, P.listsAtLeast(1, P.rationals()))) {
            homomorphic(
                    ss -> toList(map(Interval::of, ss)),
                    ss -> toList(map(Interval::of, ss)),
                    ss -> toList(Rational.delta(ss)),
                    as -> toList(delta(as)),
                    rs
            );
        }
    }

    private void propertiesPow() {
        initialize("pow(int)");
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.integersGeometric()))) {
            List<Interval> pow = p.a.pow(p.b);
            pow.forEach(Interval::validate);
            int size = pow.size();
            assertTrue(p, size >= 0 && size <= 2);
            if (size == 2) {
                Interval i = pow.get(0);
                Interval j = pow.get(1);
                Optional<Rational> ix = i.getLower();
                Optional<Rational> iy = i.getUpper();
                Optional<Rational> jx = j.getLower();
                Optional<Rational> jy = j.getUpper();
                assertFalse(p, ix.isPresent());
                assertTrue(p, iy.isPresent());
                assertTrue(p, jx.isPresent());
                assertFalse(p, jy.isPresent());
                if (jx.get() == Rational.ZERO) {
                    assertTrue(p, iy.get().signum() == -1);
                } else if (iy.get() == Rational.ZERO) {
                    assertTrue(p, jx.get().signum() == 1);
                } else {
                    assertTrue(p, iy.get().signum() == -1);
                    assertTrue(p, jx.get().signum() == 1);
                }
            }
        }

        Iterable<Pair<Interval, Integer>> ps = filterInfinite(
                p -> p.b >= 0 || !p.a.equals(ZERO),
                P.pairs(P.intervals(), P.integersGeometric())
        );
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            List<Interval> pow = p.a.pow(p.b);
            Interval x = product(replicate(Math.abs(p.b), p.a));
            Interval product = p.b < 0 ? x.invertHull() : x;
            assertTrue(p, all(product::contains, pow));
        }

        ps = filterInfinite(
                p -> p.b >= 0 || !p.a.equals(ZERO),
                P.pairs(
                        filterInfinite(a -> !a.diameter().equals(Optional.of(Rational.ZERO)), P.intervals()),
                        P.integersGeometric()
                )
        );
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            Iterable<Rational> rs = p.b < 0 ?
                    filterInfinite(r -> r != Rational.ZERO, P.rationalsIn(p.a)) :
                    P.rationalsIn(p.a);
            for (Rational r : take(TINY_LIMIT, rs)) {
                List<Interval> pow = p.a.pow(p.b);
                assertTrue(p, any(s -> s.contains(r.pow(p.b)), pow));
            }
        }

        ps = P.pairs(filter(a -> !a.equals(ZERO), P.intervals()), P.integersGeometric());
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            List<Interval> a = p.a.pow(p.b);
            assertTrue(p, any(c -> convexHull(toList(concatMap(Interval::invert, p.a.pow(-p.b)))).contains(c), a));
            assertTrue(p, any(c -> convexHull(toList(concatMap(b -> b.pow(-p.b), p.a.invert()))).contains(c), a));
        }

        for (int i : take(LIMIT, P.positiveIntegersGeometric())) {
            assertTrue(i, ZERO.pow(i).equals(Collections.singletonList(ZERO)));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a, a.pow(0).equals(Collections.singletonList(ONE)));
            assertEquals(a, a.pow(1), Collections.singletonList(a));
            Interval product = a.multiply(a);
            assertTrue(a, all(product::contains, a.pow(2)));
            assertEquals(a, a.pow(-1), a.invert());
        }
    }

    private static @NotNull Interval powHull_simplest(@NotNull Interval a, int p) {
        if (p < 0 && a.equals(ZERO)) {
            throw new ArithmeticException();
        }
        return convexHull(a.pow(p));
    }

    private void propertiesPowHull() {
        initialize("powHull(int)");
        Iterable<Pair<Interval, Integer>> ps = filterInfinite(
                p -> p.b >= 0 || !p.a.equals(ZERO),
                P.pairs(P.intervals(), P.integersGeometric())
        );
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            Interval powHull = p.a.powHull(p.b);
            powHull.validate();
            assertEquals(p, powHull_simplest(p.a, p.b), powHull);
            Interval product = product(replicate(Math.abs(p.b), p.a));
            if (p.b < 0) product = product.invertHull();
            assertTrue(p, product.contains(powHull));
        }

        ps = P.pairs(filterInfinite(a -> !a.equals(ZERO), P.intervals()), P.integersGeometric());
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            Interval a = p.a.powHull(p.b);
            assertTrue(p, p.a.powHull(-p.b).invertHull().contains(a));
            assertTrue(p, p.a.invertHull().powHull(-p.b).contains(a));
        }

        ps = filterInfinite(
                p -> p.b >= 0 || !p.a.equals(ZERO),
                P.pairs(
                        filterInfinite(a -> !a.diameter().equals(Optional.of(Rational.ZERO)), P.intervals()),
                        P.integersGeometric()
                )
        );
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            Iterable<Rational> rs = p.b < 0 ?
                    filterInfinite(r -> r != Rational.ZERO, P.rationalsIn(p.a)) :
                    P.rationalsIn(p.a);
            for (Rational r : take(TINY_LIMIT, rs)) {
                Interval powHull = p.a.powHull(p.b);
                assertTrue(p, powHull.contains(r.pow(p.b)));
            }
        }

        for (int i : take(LIMIT, P.positiveIntegersGeometric())) {
            assertTrue(Integer.toString(i), ZERO.powHull(i).equals(ZERO));
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a, a.powHull(0).equals(ONE));
            fixedPoint(b -> b.powHull(1), a);
            assertTrue(a, a.multiply(a).contains(a.powHull(2)));
        }

        for (Interval a : take(LIMIT, filterInfinite(a -> !a.equals(ZERO), P.intervals()))) {
            assertEquals(a, a.powHull(-1), a.invertHull());
        }

        Iterable<Triple<Interval, Integer, Integer>> ts1 = filterInfinite(
                p -> p.b >= 0 && p.c >= 0 || !p.a.equals(ZERO),
                P.triples(P.intervals(), P.integersGeometric(), P.integersGeometric())
        );
        for (Triple<Interval, Integer, Integer> t : take(LIMIT, ts1)) {
            Interval expression1 = t.a.powHull(t.b).multiply(t.a.powHull(t.c));
            Interval expression2 = t.a.powHull(t.b + t.c);
            assertTrue(t, expression1.contains(expression2));
        }

        ts1 = filterInfinite(
                t -> !t.a.equals(ZERO) || t.c == 0 && t.b >= 0,
                P.triples(P.intervals(), P.integersGeometric(), P.integersGeometric())
        );
        for (Triple<Interval, Integer, Integer> t : take(LIMIT, ts1)) {
            Interval expression1 = t.a.powHull(t.b).divideHull(t.a.powHull(t.c));
            Interval expression2 = t.a.powHull(t.b - t.c);
            assertTrue(t, expression1.contains(expression2));
        }

        ts1 = filterInfinite(
                t -> !t.a.equals(ZERO) || t.b >= 0 && t.c >= 0,
                P.triples(P.intervals(), P.integersGeometric(), P.integersGeometric())
        );
        for (Triple<Interval, Integer, Integer> t : take(LIMIT, ts1)) {
            Interval expression5 = t.a.powHull(t.b).powHull(t.c);
            Interval expression6 = t.a.powHull(t.b * t.c);
            assertTrue(t, expression5.contains(expression6));
        }

        Iterable<Triple<Interval, Interval, Integer>> ts2 = filterInfinite(
                t -> !t.a.equals(ZERO) && !t.b.equals(ZERO) || t.c >= 0,
                P.triples(P.intervals(), P.intervals(), P.integersGeometric())
        );
        for (Triple<Interval, Interval, Integer> t : take(LIMIT, ts2)) {
            Interval expression1 = t.a.multiply(t.b).powHull(t.c);
            Interval expression2 = t.a.powHull(t.c).multiply(t.b.powHull(t.c));
            assertEquals(t, expression1, expression2);
        }

        ts2 = filterInfinite(
                t -> !t.a.equals(ZERO) || t.c >= 0,
                P.triples(P.intervals(), filterInfinite(a -> !a.equals(ZERO), P.intervals()), P.integersGeometric())
        );
        for (Triple<Interval, Interval, Integer> t : take(LIMIT, ts2)) {
            Interval expression1 = t.a.divideHull(t.b).powHull(t.c);
            Interval expression2 = t.a.powHull(t.c).divideHull(t.b.powHull(t.c));
            assertTrue(t, expression1.contains(expression2));
        }

        Iterable<Pair<Rational, Integer>> ps2 = filterInfinite(
                p -> p.b >= 0 || p.a != Rational.ZERO,
                P.pairs(P.rationals(), P.integersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps2)) {
            homomorphic(Interval::of, Function.identity(), Interval::of, Rational::pow, Interval::powHull, p);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                ZERO.powHull(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsPowHull() {
        Map<String, Function<Pair<Interval, Integer>, Interval>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> powHull_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.powHull(p.b));
        Iterable<Pair<Interval, Integer>> ps = filterInfinite(
                p -> p.b >= 0 || !p.a.equals(ZERO),
                P.pairs(P.intervals(), P.integersGeometric())
        );
        compareImplementations("powHull(int)", take(LIMIT, ps), functions);
    }

    private void propertiesElementCompare() {
        initialize("elementCompare(Interval)");
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            p.a.elementCompare(p.b);
            antiCommutative(Interval::elementCompare, oc -> oc.map(Ordering::invert), p);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            Optional<Ordering> compare = a.elementCompare(a);
            assertTrue(a, !compare.isPresent() || compare.get() == EQ);
            assertEquals(a, a.elementCompare(ZERO).map(Ordering::toInt), a.signum());
        }

        for (Triple<Interval, Interval, Interval> t : take(LIMIT, P.triples(P.intervals()))) {
            transitive((a, b) -> a.elementCompare(b).equals(Optional.of(LT)), t);
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::intervals);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::intervals);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(Interval)");
        propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::intervals);
    }

    private void propertiesRead() {
        initialize("read(String)");
        propertiesReadHelper(LIMIT, P, INTERVAL_CHARS, P.intervals(), Interval::read, Interval::validate, false);
    }

    private void propertiesFindIn() {
        initialize("findIn(String)");
        propertiesFindInHelper(LIMIT, P.getWheelsProvider(), P.intervals(), Interval::read, Interval::findIn, a -> {});
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, INTERVAL_CHARS, P.intervals(), Interval::read);
    }
}
