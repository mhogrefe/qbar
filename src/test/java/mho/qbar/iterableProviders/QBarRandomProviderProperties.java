package mho.qbar.iterableProviders;

import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class QBarRandomProviderProperties {
    private static final int TINY_LIMIT = 20;
    private static int LIMIT;
    private static QBarIterableProvider P;

    private static void initialize(String name) {
        P.reset();
        System.out.println("\t\ttesting " + name + " properties...");
    }

    @Test
    public void testAllProperties() {
        List<Triple<QBarIterableProvider, Integer, String>> configs = new ArrayList<>();
        configs.add(new Triple<>(QBarExhaustiveProvider.INSTANCE, 10000, "exhaustively"));
        configs.add(new Triple<>(QBarRandomProvider.example(), 1000, "randomly"));
        System.out.println("QBarRandomProvider properties");
        for (Triple<QBarIterableProvider, Integer, String> config : configs) {
            P = config.a;
            LIMIT = config.b;
            System.out.println("\ttesting " + config.c);
            propertiesPositiveRationals();
            propertiesNegativeRationals();
            propertiesNonzeroRationals();
            propertiesRationals();
            propertiesNonNegativeRationalsLessThanOne();
            propertiesRangeUp_Rational();
            propertiesRangeDown_Rational();
            propertiesRange_Rational_Rational();
            propertiesFinitelyBoundedIntervals();
            propertiesIntervals();
            propertiesRationalsIn();
            propertiesRationalsNotIn();
        }
        System.out.println("Done");
    }

    private static <T> void simpleTestWithNulls(
            @NotNull QBarRandomProvider rp,
            @NotNull Iterable<T> xs,
            @NotNull Predicate<T> predicate
    ) {
        rp.reset();
        assertTrue(rp, all(predicate, take(TINY_LIMIT, xs)));
        rp.reset();
        testNoRemove(TINY_LIMIT, xs);
    }

    private static <T> void simpleTest(
            @NotNull QBarRandomProvider rp,
            @NotNull Iterable<T> xs,
            @NotNull Predicate<T> predicate
    ) {
        simpleTestWithNulls(rp, xs, x -> x != null && predicate.test(x));
    }

    private static void propertiesPositiveRationals() {
        initialize("positiveRationals()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Rational> rs = rp.positiveRationals();
            rp.reset();
            take(TINY_LIMIT, rs).forEach(Rational::validate);
            simpleTest(rp, rs, r -> r.signum() == 1);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.positiveRationals();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private static void propertiesNegativeRationals() {
        initialize("negativeRationals()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Rational> rs = rp.negativeRationals();
            rp.reset();
            take(TINY_LIMIT, rs).forEach(Rational::validate);
            simpleTest(rp, rs, r -> r.signum() == -1);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.negativeRationals();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private static void propertiesNonzeroRationals() {
        initialize("nonzeroRationals()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Rational> rs = rp.nonzeroRationals();
            rp.reset();
            take(TINY_LIMIT, rs).forEach(Rational::validate);
            simpleTest(rp, rs, r -> r != Rational.ZERO);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.nonzeroRationals();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private static void propertiesRationals() {
        initialize("rationals()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 3,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Rational> rs = rp.rationals();
            rp.reset();
            take(TINY_LIMIT, rs).forEach(Rational::validate);
            simpleTest(rp, rs, r -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 3,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationals();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private static void propertiesNonNegativeRationalsLessThanOne() {
        initialize("nonNegativeRationalsLessThanOne()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Rational> rs = rp.nonNegativeRationalsLessThanOne();
            rp.reset();
            take(TINY_LIMIT, rs).forEach(Rational::validate);
            simpleTest(rp, rs, r -> r.signum() != -1 && lt(r, Rational.ONE));
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.nonNegativeRationalsLessThanOne();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private static void propertiesRangeUp_Rational() {
        initialize("rangeUp(Rational)");
        Iterable<Pair<QBarRandomProvider, Rational>> ps = P.pairs(
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProviders()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(LIMIT, ps)) {
            Iterable<Rational> rs = p.a.rangeUp(p.b);
            simpleTest(p.a, rs, r -> ge(r, p.b));
        }

        Iterable<Pair<QBarRandomProvider, Rational>> psFail = P.pairs(
                filterInfinite(rp -> rp.getScale() < 4, P.qbarRandomProviders()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.rangeUp(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }
    }

    private static void propertiesRangeDown_Rational() {
        initialize("rangeDown(Rational)");
        Iterable<Pair<QBarRandomProvider, Rational>> ps = P.pairs(
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProviders()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(LIMIT, ps)) {
            Iterable<Rational> rs = p.a.rangeDown(p.b);
            simpleTest(p.a, rs, r -> le(r, p.b));
        }

        Iterable<Pair<QBarRandomProvider, Rational>> psFail = P.pairs(
                filterInfinite(rp -> rp.getScale() < 4, P.qbarRandomProviders()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.rangeDown(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }
    }

    private static void propertiesRange_Rational_Rational() {
        initialize("range(Rational, Rational)");
        Iterable<Triple<QBarRandomProvider, Rational, Rational>> ts = filterInfinite(
                t -> le(t.b, t.c),
                P.triples(
                        filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProviders()),
                        P.rationals(),
                        P.rationals()
                )
        );
        for (Triple<QBarRandomProvider, Rational, Rational> t : take(LIMIT, ts)) {
            Iterable<Rational> rs = t.a.range(t.b, t.c);
            simpleTest(t.a, rs, r -> ge(r, t.b) && le(r, t.c));
            assertEquals(t, gt(t.b, t.c), isEmpty(rs));
        }

        Iterable<Pair<QBarRandomProvider, Rational>> ps = P.pairs(
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProviders()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(LIMIT, ps)) {
            assertTrue(p, all(r -> eq(r, p.b), take(TINY_LIMIT, p.a.range(p.b, p.b))));
        }

        Iterable<Triple<QBarRandomProvider, Rational, Rational>> tsFail = filterInfinite(
                t -> le(t.b, t.c),
                P.triples(
                        filterInfinite(rp -> rp.getScale() < 4, P.qbarRandomProviders()),
                        P.rationals(),
                        P.rationals()
                )
        );
        for (Triple<QBarRandomProvider, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.range(t.b, t.c);
                fail(t);
            } catch (IllegalStateException ignored) {}
        }

        tsFail = filterInfinite(
                t -> gt(t.b, t.c),
                P.triples(
                        filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProviders()),
                        P.rationals(),
                        P.rationals()
                )
        );
        for (Triple<QBarRandomProvider, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.range(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static void propertiesFinitelyBoundedIntervals() {
        initialize("finitelyBoundedIntervals()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 6,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Interval> as = rp.finitelyBoundedIntervals();
            rp.reset();
            take(TINY_LIMIT, as).forEach(Interval::validate);
            simpleTest(rp, as, Interval::isFinitelyBounded);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 6,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.finitelyBoundedIntervals();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private static void propertiesIntervals() {
        initialize("intervals()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 6,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Interval> as = rp.intervals();
            rp.reset();
            take(TINY_LIMIT, as).forEach(Interval::validate);
            simpleTest(rp, as, a -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 6,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.intervals();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private static void propertiesRationalsIn() {
        initialize("rationalsIn()");
        Iterable<Pair<QBarRandomProvider, Interval>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() >= 4,
                        P.qbarRandomProvidersDefaultSecondaryScale()
                ),
                P.intervals()
        );
        for (Pair<QBarRandomProvider, Interval> p : take(LIMIT, ps)) {
            Iterable<Rational> rs = p.a.rationalsIn(p.b);
            take(TINY_LIMIT, rs).forEach(Rational::validate);
            simpleTest(p.a, rs, p.b::contains);
        }

        Iterable<Pair<QBarRandomProvider, Interval>> psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() < 4,
                        P.qbarRandomProvidersDefaultSecondaryScale()
                ),
                P.intervals()
        );
        for (Pair<QBarRandomProvider, Interval> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalsIn(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }
    }

    private static void propertiesRationalsNotIn() {
        initialize("rationalsNotIn()");
        Iterable<Pair<QBarRandomProvider, Interval>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() >= 4,
                        P.qbarRandomProvidersDefaultSecondaryScale()
                ),
                filterInfinite(a -> !a.equals(Interval.ALL), P.intervals())
        );
        for (Pair<QBarRandomProvider, Interval> p : take(LIMIT, ps)) {
            Iterable<Rational> rs = p.a.rationalsNotIn(p.b);
            take(TINY_LIMIT, rs).forEach(Rational::validate);
            simpleTest(p.a, rs, r -> !p.b.contains(r));
        }

        Iterable<Pair<QBarRandomProvider, Interval>> psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() < 4,
                        P.qbarRandomProvidersDefaultSecondaryScale()
                ),
                filterInfinite(a -> !a.equals(Interval.ALL), P.intervals())
        );
        for (Pair<QBarRandomProvider, Interval> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalsNotIn(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            try {
                rp.rationalsNotIn(Interval.ALL);
                fail(rp);
            } catch (IllegalArgumentException ignored) {}
        }
    }
}
