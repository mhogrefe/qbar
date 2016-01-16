package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.qbar.testing.QBarTestProperties;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class QBarRandomProviderProperties extends QBarTestProperties {
    public QBarRandomProviderProperties() {
        super("QBarRandomProvider");
    }

    @Override
    protected void testBothModes() {
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
        propertiesRationalVectors_int();
        propertiesRationalVectors();
        propertiesRationalVectorsAtLeast();
        propertiesReducedRationalVectors_int();
        propertiesReducedRationalVectors();
        propertiesReducedRationalVectorsAtLeast();
        propertiesRationalMatrices_int_int();
        propertiesRationalMatrices();
        propertiesSquareRationalMatrices();
        propertiesPolynomials_int();
        propertiesPolynomials();
        propertiesPolynomialsAtLeast();
        propertiesPrimitivePolynomials_int();
        propertiesPrimitivePolynomials();
        propertiesPrimitivePolynomialsAtLeast();
        propertiesPositivePrimitivePolynomials_int();
        propertiesPositivePrimitivePolynomials();
        propertiesPositivePrimitivePolynomialsAtLeast();
        propertiesRationalPolynomials_int();
        propertiesRationalPolynomials();
        propertiesRationalPolynomialsAtLeast();
        propertiesMonicRationalPolynomials_int();
        propertiesMonicRationalPolynomials();
        propertiesMonicRationalPolynomialsAtLeast();
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

    private void propertiesPositiveRationals() {
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

    private void propertiesNegativeRationals() {
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

    private void propertiesNonzeroRationals() {
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

    private void propertiesRationals() {
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

    private void propertiesNonNegativeRationalsLessThanOne() {
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

    private void propertiesRangeUp_Rational() {
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

    private void propertiesRangeDown_Rational() {
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

    private void propertiesRange_Rational_Rational() {
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

    private void propertiesFinitelyBoundedIntervals() {
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

    private void propertiesIntervals() {
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

    private void propertiesRationalsIn() {
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

    private void propertiesRationalsNotIn() {
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

    private void propertiesRationalVectors_int() {
        initialize("rationalVectors(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalVector> vs = p.a.rationalVectors(p.b);
            p.a.reset();
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
            simpleTest(p.a, vs, v -> v.dimension() == p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() < 3, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalVectors(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.negativeIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalVectors(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalVectors() {
        initialize("rationalVectors()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalVector> vs = rp.rationalVectors();
            rp.reset();
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
            simpleTest(rp, vs, v -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 3 && rp.getSecondaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(rp -> rp.getScale() >= 3 && rp.getSecondaryScale() <= 0, P.qbarRandomProviders());
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesRationalVectorsAtLeast() {
        initialize("rationalVectorsAtLeast()");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProviders()),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalVector> vs = p.a.rationalVectorsAtLeast(p.b);
            p.a.reset();
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
            simpleTest(p.a, vs, v -> v.dimension() >= p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() < 3, P.qbarRandomProviders()),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProviders()),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProviders()),
                        P.negativeIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesReducedRationalVectors_int() {
        initialize("reducedRationalVectors(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalVector> vs = p.a.reducedRationalVectors(p.b);
            p.a.reset();
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
            simpleTest(p.a, vs, v -> v.isReduced() && v.dimension() == p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.reducedRationalVectors(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.negativeIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.reducedRationalVectors(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesReducedRationalVectors() {
        initialize("reducedRationalVectors()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalVector> vs = rp.reducedRationalVectors();
            rp.reset();
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
            simpleTest(rp, vs, RationalVector::isReduced);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.reducedRationalVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(rp -> rp.getScale() > 0 && rp.getSecondaryScale() <= 0, P.qbarRandomProviders());
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.reducedRationalVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesReducedRationalVectorsAtLeast() {
        initialize("reducedRationalVectorsAtLeast()");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalVector> vs = p.a.reducedRationalVectorsAtLeast(p.b);
            p.a.reset();
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
            simpleTest(p.a, vs, v -> v.isReduced() && v.dimension() >= p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProviders()),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.reducedRationalVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.reducedRationalVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.negativeIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.reducedRationalVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalMatrices_int_int() {
        initialize("rationalMatrices(int, int)");
        Iterable<Triple<QBarRandomProvider, Integer, Integer>> ts = P.triples(
                filterInfinite(rp -> rp.getScale() >= 3, P.withScale(4).qbarRandomProvidersDefaultSecondaryScale()),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, ts)) {
            Iterable<RationalMatrix> ms = t.a.rationalMatrices(t.b, t.c);
            t.a.reset();
            take(TINY_LIMIT, ms).forEach(RationalMatrix::validate);
            simpleTest(t.a, ms, m -> m.height() == t.b && m.width() == t.c);
        }

        Iterable<Triple<QBarRandomProvider, Integer, Integer>> tsFail = P.triples(
                filterInfinite(rp -> rp.getScale() < 3, P.withScale(4).qbarRandomProvidersDefaultSecondaryScale()),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.rationalMatrices(t.b, t.c);
                fail(t);
            } catch (IllegalStateException ignored) {}
        }

        tsFail = P.triples(
                filterInfinite(rp -> rp.getScale() >= 3, P.withScale(4).qbarRandomProvidersDefaultSecondaryScale()),
                P.withScale(4).negativeIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.rationalMatrices(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = P.triples(
                filterInfinite(rp -> rp.getScale() >= 3, P.withScale(4).qbarRandomProvidersDefaultSecondaryScale()),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).negativeIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.rationalMatrices(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalMatrices() {
        initialize("rationalMatrices()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalMatrix> ms = rp.rationalMatrices();
            rp.reset();
            take(TINY_LIMIT, ms).forEach(RationalMatrix::validate);
            simpleTest(rp, ms, m -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 3 && rp.getSecondaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() < 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesSquareRationalMatrices() {
        initialize("squareRationalMatrices()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalMatrix> ms = rp.squareRationalMatrices();
            rp.reset();
            take(TINY_LIMIT, ms).forEach(RationalMatrix::validate);
            simpleTest(rp, ms, RationalMatrix::isSquare);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 3 && rp.getSecondaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squareRationalMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() < 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squareRationalMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesPolynomials_int() {
        initialize("polynomials(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.rangeUpGeometric(-1)
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.polynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() == p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.rangeUpGeometric(-1)
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.polynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.rangeDown(-2)
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.polynomials(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPolynomials() {
        initialize("polynomials()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Polynomial> ps = rp.polynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(Polynomial::validate);
            simpleTest(rp, ps, p -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() >= 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.polynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(rp -> rp.getScale() > 0 && rp.getSecondaryScale() < 0, P.qbarRandomProviders());
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.polynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesPolynomialsAtLeast() {
        initialize("polynomialsAtLeast()");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.polynomialsAtLeast(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() >= p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.polynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.polynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.rangeDown(-2)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.polynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPrimitivePolynomials_int() {
        initialize("primitivePolynomials(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.primitivePolynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() == p.b && q.isPrimitive());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.primitivePolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.negativeIntegers()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.primitivePolynomials(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPrimitivePolynomials() {
        initialize("primitivePolynomials()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Polynomial> ps = rp.primitivePolynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(Polynomial::validate);
            simpleTest(rp, ps, Polynomial::isPrimitive);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.primitivePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(rp -> rp.getScale() > 0 && rp.getSecondaryScale() <= 0, P.qbarRandomProviders());
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.primitivePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesPrimitivePolynomialsAtLeast() {
        initialize("primitivePolynomialsAtLeast()");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.primitivePolynomialsAtLeast(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() >= p.b && q.isPrimitive());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.primitivePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() <= 0 || p.a.getSecondaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.primitivePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.rangeDown(-2)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.primitivePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPositivePrimitivePolynomials_int() {
        initialize("positivePrimitivePolynomials(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.positivePrimitivePolynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() == p.b && q.signum() == 1 && q.isPrimitive());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.positivePrimitivePolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.negativeIntegers()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.positivePrimitivePolynomials(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPositivePrimitivePolynomials() {
        initialize("positivePrimitivePolynomials()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Polynomial> ps = rp.positivePrimitivePolynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(Polynomial::validate);
            simpleTest(rp, ps, p -> p.signum() == 1 && p.isPrimitive());
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.positivePrimitivePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(rp -> rp.getScale() > 0 && rp.getSecondaryScale() <= 0, P.qbarRandomProviders());
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.positivePrimitivePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesPositivePrimitivePolynomialsAtLeast() {
        initialize("positivePrimitivePolynomialsAtLeast()");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.positivePrimitivePolynomialsAtLeast(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() >= p.b && q.signum() == 1 && q.isPrimitive());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.positivePrimitivePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() <= 0 || p.a.getSecondaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.positivePrimitivePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.rangeDown(-2)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.positivePrimitivePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalPolynomials_int() {
        initialize("rationalPolynomials(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.rangeUpGeometric(-1)
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalPolynomial> qs = p.a.rationalPolynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(RationalPolynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() == p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() < 3, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.rangeUpGeometric(-1)
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalPolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.rangeDown(-2)
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalPolynomials(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalPolynomials() {
        initialize("rationalPolynomials()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalPolynomial> ps = rp.rationalPolynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(RationalPolynomial::validate);
            simpleTest(rp, ps, p -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 3 && rp.getSecondaryScale() >= 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalPolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(rp -> rp.getScale() >= 3 && rp.getSecondaryScale() < 0, P.qbarRandomProviders());
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalPolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesRationalPolynomialsAtLeast() {
        initialize("rationalPolynomialsAtLeast()");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalPolynomial> qs = p.a.rationalPolynomialsAtLeast(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(RationalPolynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() >= p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() < 3, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalPolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalPolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.rangeDown(-2)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalPolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMonicRationalPolynomials_int() {
        initialize("monicRationalPolynomials(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalPolynomial> qs = p.a.monicRationalPolynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(RationalPolynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() == p.b && q.isMonic());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.monicRationalPolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.negativeIntegers()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.monicRationalPolynomials(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMonicRationalPolynomials() {
        initialize("monicRationalPolynomials()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalPolynomial> ps = rp.monicRationalPolynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(RationalPolynomial::validate);
            simpleTest(rp, ps, RationalPolynomial::isMonic);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.monicRationalPolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(rp -> rp.getScale() > 0 && rp.getSecondaryScale() <= 0, P.qbarRandomProviders());
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.monicRationalPolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesMonicRationalPolynomialsAtLeast() {
        initialize("positivePrimitivePolynomialsAtLeast()");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalPolynomial> qs = p.a.monicRationalPolynomialsAtLeast(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(RationalPolynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() >= p.b && q.isMonic());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.monicRationalPolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() <= 0 || p.a.getSecondaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.monicRationalPolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProviders()),
                        P.rangeDown(-2)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.monicRationalPolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }
}
