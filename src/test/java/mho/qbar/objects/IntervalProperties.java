package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

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
            propertiesConvexHull_Interval();
            propertiesConvexHull_Iterable_Interval();
            propertiesDiameter();
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
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertTrue(a.toString(), a.convexHull(a).equals(a));
            assertTrue(a.toString(), ALL.convexHull(a).equals(ALL));
        }

        //Given an interval c whose endpoints are in each of two intervals a and b, any Rational in c lies in the
        //convex hull of a and b
        Iterable<Triple<Interval, Interval, Rational>> ts = map(
                s -> new Triple<>(s.a.a, s.a.b, s.b.b),
                (Iterable<Pair<Pair<Interval, Interval>, Pair<Interval, Rational>>>) P.dependentPairs(
                        P.pairs(P.intervals()),
                        p -> P.dependentPairs(
                                map(
                                        q -> of(min(q.a, q.b), max(q.a, q.b)),
                                        P.pairs(P.rationals(p.a), P.rationals(p.b))
                                ),
                                P::rationals
                        )
                )
        );
        for (Triple<Interval, Interval, Rational> t : take(LIMIT, ts)) {
            assertTrue(t.toString(), t.a.convexHull(t.b).contains(t.c));
        }
    }

    private static void propertiesConvexHull_Iterable_Interval() {
        initialize();
        System.out.println("\t\ttesting convexHull(Iterable<Interval>) properties...");

        for (List<Interval> as : take(LIMIT, P.listsAtLeast(1, P.intervals()))) {
            Interval c = convexHull(as);
            validate(c);
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            assertEquals(a.toString(), convexHull(Arrays.asList(a)), a);
        }

        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            assertEquals(p.toString(), convexHull(Arrays.asList(p.a, p.b)), p.a.convexHull(p.b));
        }

        //todo continue
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
}
