package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.qbar.testing.QBarTestProperties;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
        propertiesVectors_int();
        propertiesVectors();
        propertiesVectorsAtLeast();
        propertiesRationalVectors_int();
        propertiesRationalVectors();
        propertiesRationalVectorsAtLeast();
        propertiesReducedRationalVectors_int();
        propertiesReducedRationalVectors();
        propertiesReducedRationalVectorsAtLeast();
        propertiesPolynomialVectors_int();
        propertiesPolynomialVectors();
        propertiesPolynomialVectorsAtLeast();
        propertiesRationalPolynomialVectors_int();
        propertiesRationalPolynomialVectors();
        propertiesRationalPolynomialVectorsAtLeast();
        propertiesMatrices_int_int();
        propertiesMatrices();
        propertiesSquareMatrices();
        propertiesInvertibleMatrices();
        propertiesRationalMatrices_int_int();
        propertiesRationalMatrices();
        propertiesSquareRationalMatrices();
        propertiesInvertibleRationalMatrices();
        propertiesPolynomialMatrices_int_int();
        propertiesPolynomialMatrices();
        propertiesSquarePolynomialMatrices();
        propertiesRationalPolynomialMatrices_int_int();
        propertiesRationalPolynomialMatrices();
        propertiesSquareRationalPolynomialMatrices();
        propertiesPolynomials_int();
        propertiesPolynomials();
        propertiesPolynomialsAtLeast();
        propertiesPrimitivePolynomials_int();
        propertiesPrimitivePolynomials();
        propertiesPrimitivePolynomialsAtLeast();
        propertiesPositivePrimitivePolynomials_int();
        propertiesPositivePrimitivePolynomials();
        propertiesPositivePrimitivePolynomialsAtLeast();
        propertiesSquareFreePolynomials_int();
        propertiesSquareFreePolynomials();
        propertiesSquareFreePolynomialsAtLeast();
        propertiesPositivePrimitiveSquareFreePolynomials_int();
        propertiesPositivePrimitiveSquareFreePolynomials();
        propertiesPositivePrimitiveSquareFreePolynomialsAtLeast();
        propertiesIrreduciblePolynomials_int();
        propertiesIrreduciblePolynomials();
        propertiesIrreduciblePolynomialsAtLeast();
        propertiesRationalPolynomials_int();
        propertiesRationalPolynomials();
        propertiesRationalPolynomialsAtLeast();
        propertiesMonicRationalPolynomials_int();
        propertiesMonicRationalPolynomials();
        propertiesMonicRationalPolynomialsAtLeast();
        propertiesVariables();
        propertiesMonomialOrders();
        propertiesExponentVectors();
        propertiesExponentVectors_List_Variable();
        propertiesMultivariatePolynomials();
        propertiesMultivariatePolynomials_List_Variable();
        propertiesPositiveAlgebraics_int();
        propertiesPositiveAlgebraics();
        propertiesNegativeAlgebraics_int();
        propertiesNegativeAlgebraics();
        propertiesNonzeroAlgebraics_int();
        propertiesNonzeroAlgebraics();
        propertiesAlgebraics_int();
        propertiesAlgebraics();
        propertiesNonNegativeAlgebraicsLessThanOne_int();
        propertiesNonNegativeAlgebraicsLessThanOne();
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
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Rational> rs = rp.positiveRationals();
            rp.reset();
            take(TINY_LIMIT, rs).forEach(Rational::validate);
            simpleTest(rp, rs, r -> r.signum() == 1);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 4,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
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
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Rational> rs = rp.negativeRationals();
            rp.reset();
            take(TINY_LIMIT, rs).forEach(Rational::validate);
            simpleTest(rp, rs, r -> r.signum() == -1);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 4,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
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
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Rational> rs = rp.nonzeroRationals();
            rp.reset();
            take(TINY_LIMIT, rs).forEach(Rational::validate);
            simpleTest(rp, rs, r -> r != Rational.ZERO);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 4,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
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
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Rational> rs = rp.rationals();
            rp.reset();
            take(TINY_LIMIT, rs).forEach(Rational::validate);
            simpleTest(rp, rs, r -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 3,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
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
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Rational> rs = rp.nonNegativeRationalsLessThanOne();
            rp.reset();
            take(TINY_LIMIT, rs).forEach(Rational::validate);
            simpleTest(rp, rs, r -> r.signum() != -1 && lt(r, Rational.ONE));
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 4,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
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
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultTertiaryScale()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(LIMIT, ps)) {
            Iterable<Rational> rs = p.a.rangeUp(p.b);
            simpleTest(p.a, rs, r -> ge(r, p.b));
        }

        Iterable<Pair<QBarRandomProvider, Rational>> psFail = P.pairs(
                filterInfinite(rp -> rp.getScale() < 4, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultTertiaryScale()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(LIMIT, ps)) {
            Iterable<Rational> rs = p.a.rangeDown(p.b);
            simpleTest(p.a, rs, r -> le(r, p.b));
        }

        Iterable<Pair<QBarRandomProvider, Rational>> psFail = P.pairs(
                filterInfinite(rp -> rp.getScale() < 4, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultTertiaryScale()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(LIMIT, ps)) {
            assertTrue(p, all(r -> eq(r, p.b), take(TINY_LIMIT, p.a.range(p.b, p.b))));
        }

        Iterable<Triple<QBarRandomProvider, Rational, Rational>> tsFail = filterInfinite(
                t -> le(t.b, t.c),
                P.triples(
                        filterInfinite(rp -> rp.getScale() < 4, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Interval> as = rp.finitelyBoundedIntervals();
            rp.reset();
            take(TINY_LIMIT, as).forEach(Interval::validate);
            simpleTest(rp, as, Interval::isFinitelyBounded);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 6,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
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
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Interval> as = rp.intervals();
            rp.reset();
            take(TINY_LIMIT, as).forEach(Interval::validate);
            simpleTest(rp, as, a -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 6,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
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
                        P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
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
                        P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
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
                        P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
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
                        P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
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
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            try {
                rp.rationalsNotIn(Interval.ALL);
                fail(rp);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesVectors_int() {
        initialize("vectors(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<Vector> vs = p.a.vectors(p.b);
            p.a.reset();
            take(TINY_LIMIT, vs).forEach(Vector::validate);
            simpleTest(p.a, vs, v -> v.dimension() == p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.vectors(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.negativeIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.vectors(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesVectors() {
        initialize("vectors()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Vector> vs = rp.vectors();
            rp.reset();
            take(TINY_LIMIT, vs).forEach(Vector::validate);
            simpleTest(rp, vs, v -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.vectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() <= 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.vectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesVectorsAtLeast() {
        initialize("vectorsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<Vector> vs = p.a.vectorsAtLeast(p.b);
            p.a.reset();
            take(TINY_LIMIT, vs).forEach(Vector::validate);
            simpleTest(p.a, vs, v -> v.dimension() >= p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultTertiaryScale()),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.vectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.vectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
                        P.negativeIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.vectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalVectors_int() {
        initialize("rationalVectors(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalVector> vs = p.a.rationalVectors(p.b);
            p.a.reset();
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
            simpleTest(p.a, vs, v -> v.dimension() == p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() < 3, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalVectors(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
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
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalVector> vs = rp.rationalVectors();
            rp.reset();
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
            simpleTest(rp, vs, v -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 3 && rp.getSecondaryScale() > 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() <= 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesRationalVectorsAtLeast() {
        initialize("rationalVectorsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() < 3, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalVector> vs = p.a.reducedRationalVectors(p.b);
            p.a.reset();
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
            simpleTest(p.a, vs, v -> v.isReduced() && v.dimension() == p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.reducedRationalVectors(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
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
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalVector> vs = rp.reducedRationalVectors();
            rp.reset();
            take(TINY_LIMIT, vs).forEach(RationalVector::validate);
            simpleTest(rp, vs, RationalVector::isReduced);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.reducedRationalVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() <= 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.reducedRationalVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesReducedRationalVectorsAtLeast() {
        initialize("reducedRationalVectorsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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

    private void propertiesPolynomialVectors_int() {
        initialize("polynomialVectors(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<PolynomialVector> vs = p.a.polynomialVectors(p.b);
            p.a.reset();
            take(TINY_LIMIT, vs).forEach(PolynomialVector::validate);
            simpleTest(p.a, vs, v -> v.dimension() == p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() <= 0 && rp.getSecondaryScale() >= 0,
                        P.qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.polynomialVectors(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0 && rp.getSecondaryScale() < 0,
                        P.qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.polynomialVectors(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                        P.qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.negativeIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.polynomialVectors(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPolynomialVectors() {
        initialize("polynomialVectors()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() > 0,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<PolynomialVector> vs = rp.polynomialVectors();
            rp.reset();
            take(TINY_LIMIT, vs).forEach(PolynomialVector::validate);
            simpleTest(rp, vs, v -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.polynomialVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() < 0 && rp.getTertiaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.polynomialVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() <= 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.polynomialVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesPolynomialVectorsAtLeast() {
        initialize("vectorsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getTertiaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                                P.withScale(4).qbarRandomProviders()
                        ),
                        P.withScale(4).naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<PolynomialVector> vs = p.a.polynomialVectorsAtLeast(p.b);
            p.a.reset();
            take(TINY_LIMIT, vs).forEach(PolynomialVector::validate);
            simpleTest(p.a, vs, v -> v.dimension() >= p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getTertiaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() >= 0,
                                P.qbarRandomProviders()
                        ),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.polynomialVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getTertiaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0 && rp.getSecondaryScale() < 0,
                                P.qbarRandomProviders()
                        ),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.polynomialVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getTertiaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                                P.qbarRandomProviders()
                        ),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.polynomialVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getTertiaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                                P.qbarRandomProviders()
                        ),
                        P.negativeIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.polynomialVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalPolynomialVectors_int() {
        initialize("rationalPolynomialVectors(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalPolynomialVector> vs = p.a.rationalPolynomialVectors(p.b);
            p.a.reset();
            take(TINY_LIMIT, vs).forEach(RationalPolynomialVector::validate);
            simpleTest(p.a, vs, v -> v.dimension() == p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() < 3 && rp.getSecondaryScale() >= 0,
                        P.qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalPolynomialVectors(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() >= 3 && rp.getSecondaryScale() < 0,
                        P.qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalPolynomialVectors(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0,
                        P.qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.negativeIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalPolynomialVectors(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalPolynomialVectors() {
        initialize("rationalPolynomialVectors()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() > 0,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalPolynomialVector> vs = rp.rationalPolynomialVectors();
            rp.reset();
            take(TINY_LIMIT, vs).forEach(RationalPolynomialVector::validate);
            simpleTest(rp, vs, v -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 3 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalPolynomialVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() < 0 && rp.getTertiaryScale() > 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalPolynomialVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() <= 0,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalPolynomialVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesRationalPolynomialVectorsAtLeast() {
        initialize("rationalPolynomialVectorsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getTertiaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0,
                                P.withScale(4).qbarRandomProviders()
                        ),
                        P.withScale(4).naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalPolynomialVector> vs = p.a.rationalPolynomialVectorsAtLeast(p.b);
            p.a.reset();
            take(TINY_LIMIT, vs).forEach(RationalPolynomialVector::validate);
            simpleTest(p.a, vs, v -> v.dimension() >= p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getTertiaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() < 3 && rp.getSecondaryScale() >= 0,
                                P.qbarRandomProviders()
                        ),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalPolynomialVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getTertiaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() < 0,
                                P.qbarRandomProviders()
                        ),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalPolynomialVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getTertiaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0,
                                P.qbarRandomProviders()
                        ),
                        P.naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalPolynomialVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getTertiaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0,
                                P.qbarRandomProviders()
                        ),
                        P.negativeIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalPolynomialVectorsAtLeast(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMatrices_int_int() {
        initialize("matrices(int, int)");
        Iterable<Triple<QBarRandomProvider, Integer, Integer>> ts = P.triples(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, ts)) {
            Iterable<Matrix> ms = t.a.matrices(t.b, t.c);
            t.a.reset();
            take(TINY_LIMIT, ms).forEach(Matrix::validate);
            simpleTest(t.a, ms, m -> m.height() == t.b && m.width() == t.c);
        }

        Iterable<Triple<QBarRandomProvider, Integer, Integer>> tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() <= 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.matrices(t.b, t.c);
                fail(t);
            } catch (IllegalStateException ignored) {}
        }

        tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).negativeIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.matrices(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).negativeIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.matrices(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMatrices() {
        initialize("matrices()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 2,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Matrix> ms = rp.matrices();
            rp.reset();
            take(TINY_LIMIT, ms).forEach(Matrix::validate);
            simpleTest(rp, ms, m -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() >= 2,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.matrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() < 2,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.matrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesSquareMatrices() {
        initialize("squareMatrices()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 2,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Matrix> ms = rp.squareMatrices();
            rp.reset();
            take(TINY_LIMIT, ms).forEach(Matrix::validate);
            simpleTest(rp, ms, Matrix::isSquare);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 2 && rp.getSecondaryScale() >= 2,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squareMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() < 2,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squareMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesInvertibleMatrices() {
        initialize("invertibleMatrices()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 2,
                P.withScale(2).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            Iterable<Matrix> ms = rp.invertibleMatrices();
            rp.reset();
            take(TINY_LIMIT, ms).forEach(Matrix::validate);
            simpleTest(rp, ms, Matrix::isInvertible);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 2 && rp.getSecondaryScale() >= 2,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.invertibleMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() < 2,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.invertibleMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesRationalMatrices_int_int() {
        initialize("rationalMatrices(int, int)");
        Iterable<Triple<QBarRandomProvider, Integer, Integer>> ts = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 3,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
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
                filterInfinite(
                        rp -> rp.getScale() < 3,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
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
                filterInfinite(
                        rp -> rp.getScale() >= 3,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
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
                filterInfinite(
                        rp -> rp.getScale() >= 3,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
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
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalMatrix> ms = rp.rationalMatrices();
            rp.reset();
            take(TINY_LIMIT, ms).forEach(RationalMatrix::validate);
            simpleTest(rp, ms, m -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 3 && rp.getSecondaryScale() >= 2,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() < 2,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
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
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalMatrix> ms = rp.squareRationalMatrices();
            rp.reset();
            take(TINY_LIMIT, ms).forEach(RationalMatrix::validate);
            simpleTest(rp, ms, RationalMatrix::isSquare);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 3 && rp.getSecondaryScale() >= 2,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squareRationalMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() < 2,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squareRationalMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesInvertibleRationalMatrices() {
        initialize("invertibleRationalMatrices()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 2,
                P.withScale(2).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            Iterable<RationalMatrix> ms = rp.invertibleRationalMatrices();
            rp.reset();
            take(TINY_LIMIT, ms).forEach(RationalMatrix::validate);
            simpleTest(rp, ms, RationalMatrix::isInvertible);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 3 && rp.getSecondaryScale() >= 2,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.invertibleRationalMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() < 2,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.invertibleRationalMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesPolynomialMatrices_int_int() {
        initialize("polynomialMatrices(int, int)");
        Iterable<Triple<QBarRandomProvider, Integer, Integer>> ts = P.triples(
                filterInfinite(
                        rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, ts)) {
            Iterable<PolynomialMatrix> ms = t.a.polynomialMatrices(t.b, t.c);
            t.a.reset();
            take(TINY_LIMIT, ms).forEach(PolynomialMatrix::validate);
            simpleTest(t.a, ms, m -> m.height() == t.b && m.width() == t.c);
        }

        Iterable<Triple<QBarRandomProvider, Integer, Integer>> tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() <= 0 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.polynomialMatrices(t.b, t.c);
                fail(t);
            } catch (IllegalStateException ignored) {}
        }

        tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() > 0 && rp.getSecondaryScale() < 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.polynomialMatrices(t.b, t.c);
                fail(t);
            } catch (IllegalStateException ignored) {}
        }

        tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).negativeIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.polynomialMatrices(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).negativeIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.polynomialMatrices(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPolynomialMatrices() {
        initialize("polynomialMatrices()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<PolynomialMatrix> ms = rp.polynomialMatrices();
            rp.reset();
            take(TINY_LIMIT, ms).forEach(PolynomialMatrix::validate);
            simpleTest(rp, ms, m -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.polynomialMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() < 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.polynomialMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() < 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.polynomialMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesSquarePolynomialMatrices() {
        initialize("squarePolynomialMatrices()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<PolynomialMatrix> ms = rp.squarePolynomialMatrices();
            rp.reset();
            take(TINY_LIMIT, ms).forEach(PolynomialMatrix::validate);
            simpleTest(rp, ms, PolynomialMatrix::isSquare);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 2 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squarePolynomialMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() < 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squarePolynomialMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() < 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squarePolynomialMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesRationalPolynomialMatrices_int_int() {
        initialize("rationalPolynomialMatrices(int, int)");
        Iterable<Triple<QBarRandomProvider, Integer, Integer>> ts = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, ts)) {
            Iterable<RationalPolynomialMatrix> ms = t.a.rationalPolynomialMatrices(t.b, t.c);
            t.a.reset();
            take(TINY_LIMIT, ms).forEach(RationalPolynomialMatrix::validate);
            simpleTest(t.a, ms, m -> m.height() == t.b && m.width() == t.c);
        }

        Iterable<Triple<QBarRandomProvider, Integer, Integer>> tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() < 3 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.rationalPolynomialMatrices(t.b, t.c);
                fail(t);
            } catch (IllegalStateException ignored) {}
        }

        tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 3 && rp.getSecondaryScale() < 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.rationalPolynomialMatrices(t.b, t.c);
                fail(t);
            } catch (IllegalStateException ignored) {}
        }

        tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).negativeIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.rationalPolynomialMatrices(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).negativeIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.rationalPolynomialMatrices(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalPolynomialMatrices() {
        initialize("rationalPolynomialMatrices()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalPolynomialMatrix> ms = rp.rationalPolynomialMatrices();
            rp.reset();
            take(TINY_LIMIT, ms).forEach(RationalPolynomialMatrix::validate);
            simpleTest(rp, ms, m -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 3 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalPolynomialMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() < 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalPolynomialMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() < 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalPolynomialMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesSquareRationalPolynomialMatrices() {
        initialize("squareRationalPolynomialMatrices()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalPolynomialMatrix> ms = rp.squareRationalPolynomialMatrices();
            rp.reset();
            take(TINY_LIMIT, ms).forEach(RationalPolynomialMatrix::validate);
            simpleTest(rp, ms, RationalPolynomialMatrix::isSquare);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 3 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squareRationalPolynomialMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() < 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squareRationalPolynomialMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() < 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squareRationalPolynomialMatrices();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesPolynomials_int() {
        initialize("polynomials(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.rangeUpGeometric(-1)
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.polynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() == p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.rangeUpGeometric(-1)
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.polynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
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
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Polynomial> ps = rp.polynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(Polynomial::validate);
            simpleTest(rp, ps, p -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() >= 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.polynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() < 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.polynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesPolynomialsAtLeast() {
        initialize("polynomialsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.primitivePolynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() == p.b && q.isPrimitive());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.primitivePolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
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
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Polynomial> ps = rp.primitivePolynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(Polynomial::validate);
            simpleTest(rp, ps, Polynomial::isPrimitive);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.primitivePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() <= 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.primitivePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesPrimitivePolynomialsAtLeast() {
        initialize("primitivePolynomialsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.positivePrimitivePolynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() == p.b && q.signum() == 1 && q.isPrimitive());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.positivePrimitivePolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
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
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Polynomial> ps = rp.positivePrimitivePolynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(Polynomial::validate);
            simpleTest(rp, ps, p -> p.signum() == 1 && p.isPrimitive());
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.positivePrimitivePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() <= 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.positivePrimitivePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesPositivePrimitivePolynomialsAtLeast() {
        initialize("positivePrimitivePolynomialsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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

    private void propertiesSquareFreePolynomials_int() {
        initialize("squareFreePolynomials(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.squareFreePolynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() == p.b && q.isSquareFree());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.squareFreePolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.negativeIntegers()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.squareFreePolynomials(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesSquareFreePolynomials() {
        initialize("squareFreePolynomials()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            Iterable<Polynomial> ps = rp.squareFreePolynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(Polynomial::validate);
            simpleTest(rp, ps, Polynomial::isSquareFree);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() >= 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squareFreePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() < 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.squareFreePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesSquareFreePolynomialsAtLeast() {
        initialize("squareFreePolynomialsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() >= 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0,
                                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.squareFreePolynomialsAtLeast(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() >= p.b && q.isSquareFree());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getSecondaryScale() >= 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultTertiaryScale()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.squareFreePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() < 0 || p.a.getSecondaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.squareFreePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() >= 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
                        P.rangeDown(-2)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.squareFreePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPositivePrimitiveSquareFreePolynomials_int() {
        initialize("positivePrimitiveSquareFreePolynomials(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.positivePrimitiveSquareFreePolynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() == p.b && q.signum() == 1 && q.isPrimitive() && q.isSquareFree());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.positivePrimitiveSquareFreePolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.negativeIntegers()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.positivePrimitiveSquareFreePolynomials(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPositivePrimitiveSquareFreePolynomials() {
        initialize("positivePrimitiveSquareFreePolynomials()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            Iterable<Polynomial> ps = rp.positivePrimitiveSquareFreePolynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(Polynomial::validate);
            simpleTest(rp, ps, p -> p.signum() == 1 && p.isPrimitive() && p.isSquareFree());
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.positivePrimitiveSquareFreePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() <= 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.positivePrimitiveSquareFreePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesPositivePrimitiveSquareFreePolynomialsAtLeast() {
        initialize("positivePrimitiveSquareFreePolynomialsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0,
                                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.positivePrimitiveSquareFreePolynomialsAtLeast(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() >= p.b && q.signum() == 1 && q.isPrimitive() && q.isSquareFree());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultTertiaryScale()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.positivePrimitiveSquareFreePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() <= 0 || p.a.getSecondaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.positivePrimitiveSquareFreePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
                        P.rangeDown(-2)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.positivePrimitiveSquareFreePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesIrreduciblePolynomials_int() {
        initialize("irreduciblePolynomials(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.irreduciblePolynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() == p.b && q.isIrreducible());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.irreduciblePolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.negativeIntegers()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.irreduciblePolynomials(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesIrreduciblePolynomials() {
        initialize("irreduciblePolynomials()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 2,
                P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            Iterable<Polynomial> ps = rp.irreduciblePolynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(Polynomial::validate);
            simpleTest(rp, ps, Polynomial::isIrreducible);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 2 && rp.getSecondaryScale() >= 2,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.irreduciblePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() < 2,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.irreduciblePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesIrreduciblePolynomialsAtLeast() {
        initialize("irreduciblePolynomialsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() >= 2 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() >= 2,
                                P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.irreduciblePolynomialsAtLeast(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(Polynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() >= p.b && q.isIrreducible());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getSecondaryScale() >= 2 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() < 2, P.qbarRandomProvidersDefaultTertiaryScale()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.irreduciblePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() < 2 || p.a.getSecondaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() >= 2, P.qbarRandomProvidersDefaultTertiaryScale()),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.irreduciblePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() >= 2 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() >= 2, P.qbarRandomProvidersDefaultTertiaryScale()),
                        P.rangeDown(-2)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.irreduciblePolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRationalPolynomials_int() {
        initialize("rationalPolynomials(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.rangeUpGeometric(-1)
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalPolynomial> qs = p.a.rationalPolynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(RationalPolynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() == p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() < 3, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.rangeUpGeometric(-1)
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rationalPolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
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
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalPolynomial> ps = rp.rationalPolynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(RationalPolynomial::validate);
            simpleTest(rp, ps, p -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() < 3 && rp.getSecondaryScale() >= 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalPolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() < 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.rationalPolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesRationalPolynomialsAtLeast() {
        initialize("rationalPolynomialsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() < 3, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() >= 3, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<RationalPolynomial> qs = p.a.monicRationalPolynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(RationalPolynomial::validate);
            simpleTest(p.a, qs, q -> q.degree() == p.b && q.isMonic());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.monicRationalPolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
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
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<RationalPolynomial> ps = rp.monicRationalPolynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(RationalPolynomial::validate);
            simpleTest(rp, ps, RationalPolynomial::isMonic);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.monicRationalPolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() <= 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.monicRationalPolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesMonicRationalPolynomialsAtLeast() {
        initialize("positivePrimitivePolynomialsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > 0 && p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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
                        filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultTertiaryScale()),
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

    private void propertiesVariables() {
        initialize("variables()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Variable> vs = rp.variables();
            rp.reset();
            take(TINY_LIMIT, vs).forEach(Variable::validate);
            simpleTest(rp, vs, v -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() <= 0,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.variables();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesMonomialOrders() {
        initialize("monomialOrders()");
        for (QBarRandomProvider rp : take(LIMIT, P.qbarRandomProvidersDefault())) {
            Iterable<MonomialOrder> os = rp.monomialOrders();
            rp.reset();
            simpleTest(rp, os, o -> true);
        }
    }

    private void propertiesExponentVectors() {
        initialize("exponentVectors()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<ExponentVector> evs = rp.exponentVectors();
            rp.reset();
            take(TINY_LIMIT, evs).forEach(ExponentVector::validate);
            simpleTest(rp, evs, ev -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() <= 0,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.exponentVectors();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesExponentVectors_List_Variable() {
        initialize("exponentVectors(List<Variable>)");
        Iterable<Pair<QBarRandomProvider, List<Variable>>> ps = P.pairs(
                filterInfinite(s -> s.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.subsets(P.variables())
        );
        for (Pair<QBarRandomProvider, List<Variable>> p : take(LIMIT, ps)) {
            Iterable<ExponentVector> evs = p.a.exponentVectors(p.b);
            p.a.reset();
            take(TINY_LIMIT, evs).forEach(ExponentVector::validate);
            simpleTest(p.a, evs, ev -> isSubsetOf(ev.variables(), p.b));
        }

        Iterable<Pair<QBarRandomProvider, List<Variable>>> psFail = P.pairs(
                filterInfinite(s -> s.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.subsets(P.variables())
        );
        for (Pair<QBarRandomProvider, List<Variable>> p : take(LIMIT, psFail)) {
            try {
                p.a.exponentVectors(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairs(
                filterInfinite(s -> s.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                filterInfinite(vs -> !increasing(vs), P.lists(P.variables()))
        );
        for (Pair<QBarRandomProvider, List<Variable>> p : take(LIMIT, psFail)) {
            try {
                p.a.exponentVectors(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMultivariatePolynomials() {
        initialize("multivariatePolynomials()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 2 && s.getSecondaryScale() > 0 && s.getTertiaryScale() >= 2,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<MultivariatePolynomial> ps = rp.multivariatePolynomials();
            rp.reset();
            take(TINY_LIMIT, ps).forEach(MultivariatePolynomial::validate);
            simpleTest(rp, ps, p -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() < 2 && s.getSecondaryScale() > 0 && s.getTertiaryScale() >= 2,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.multivariatePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                s -> s.getScale() >= 0 && s.getSecondaryScale() <= 0 && s.getTertiaryScale() >= 2,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.multivariatePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                s -> s.getScale() >= 0 && s.getSecondaryScale() > 0 && s.getTertiaryScale() < 2,
                P.qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.multivariatePolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesMultivariatePolynomials_List_Variable() {
        initialize("multivariatePolynomials(List<Variable>)");
        Iterable<Pair<QBarRandomProvider, List<Variable>>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() >= 2 && s.getSecondaryScale() > 0 && s.getTertiaryScale() >= 2,
                        P.qbarRandomProviders()
                ),
                P.subsets(P.variables())
        );
        for (Pair<QBarRandomProvider, List<Variable>> p : take(LIMIT, ps)) {
            Iterable<MultivariatePolynomial> qs = p.a.multivariatePolynomials(p.b);
            p.a.reset();
            take(TINY_LIMIT, qs).forEach(MultivariatePolynomial::validate);
            simpleTest(p.a, qs, q -> isSubsetOf(q.variables(), p.b));
        }

        Iterable<Pair<QBarRandomProvider, List<Variable>>> psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() < 2 && s.getSecondaryScale() > 0 && s.getTertiaryScale() >= 2,
                        P.qbarRandomProviders()
                ),
                P.subsets(P.variables())
        );
        for (Pair<QBarRandomProvider, List<Variable>> p : take(LIMIT, psFail)) {
            try {
                p.a.multivariatePolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() >= 2 && s.getSecondaryScale() <= 0 && s.getTertiaryScale() >= 2,
                        P.qbarRandomProviders()
                ),
                P.subsets(P.variables())
        );
        for (Pair<QBarRandomProvider, List<Variable>> p : take(LIMIT, psFail)) {
            try {
                p.a.multivariatePolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() >= 2 && s.getSecondaryScale() > 0 && s.getTertiaryScale() < 2,
                        P.qbarRandomProviders()
                ),
                P.subsets(P.variables())
        );
        for (Pair<QBarRandomProvider, List<Variable>> p : take(LIMIT, psFail)) {
            try {
                p.a.multivariatePolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() >= 2 && s.getSecondaryScale() > 0 && s.getTertiaryScale() >= 2,
                        P.qbarRandomProviders()
                ),
                filterInfinite(vs -> !increasing(vs), P.lists(P.variables()))
        );
        for (Pair<QBarRandomProvider, List<Variable>> p : take(LIMIT, psFail)) {
            try {
                p.a.multivariatePolynomials(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPositiveAlgebraics_int() {
        initialize("positiveAlgebraics(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(TINY_LIMIT, ps)) {
            Iterable<Algebraic> xs = p.a.positiveAlgebraics(p.b);
            p.a.reset();
            take(TINY_LIMIT, xs).forEach(Algebraic::validate);
            simpleTest(p.a, xs, x -> x.degree() == p.b && x.signum() == 1);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() <= 0,
                        P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.positiveAlgebraics(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withElement(0, P.negativeIntegersGeometric())
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.positiveAlgebraics(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPositiveAlgebraics() {
        initialize("positiveAlgebraics()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() >= 4,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(TINY_LIMIT, rps)) {
            Iterable<Algebraic> xs = rp.positiveAlgebraics();
            rp.reset();
            take(TINY_LIMIT, xs).forEach(Algebraic::validate);
            simpleTest(rp, xs, x -> x.signum() == 1);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() <= 0 && s.getSecondaryScale() >= 4,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.positiveAlgebraics();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() < 4,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.positiveAlgebraics();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesNegativeAlgebraics_int() {
        initialize("negativeAlgebraics(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(TINY_LIMIT, ps)) {
            Iterable<Algebraic> xs = p.a.negativeAlgebraics(p.b);
            p.a.reset();
            take(TINY_LIMIT, xs).forEach(Algebraic::validate);
            simpleTest(p.a, xs, x -> x.degree() == p.b && x.signum() == -1);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() <= 0,
                        P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.negativeAlgebraics(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withElement(0, P.negativeIntegersGeometric())
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.negativeAlgebraics(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesNegativeAlgebraics() {
        initialize("negativeAlgebraics()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() >= 4,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(TINY_LIMIT, rps)) {
            Iterable<Algebraic> xs = rp.negativeAlgebraics();
            rp.reset();
            take(TINY_LIMIT, xs).forEach(Algebraic::validate);
            simpleTest(rp, xs, x -> x.signum() == -1);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() <= 0 && s.getSecondaryScale() >= 4,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.negativeAlgebraics();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() < 4,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.negativeAlgebraics();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesNonzeroAlgebraics_int() {
        initialize("nonzeroAlgebraics(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(TINY_LIMIT, ps)) {
            Iterable<Algebraic> xs = p.a.nonzeroAlgebraics(p.b);
            p.a.reset();
            take(TINY_LIMIT, xs).forEach(Algebraic::validate);
            simpleTest(p.a, xs, x -> x.degree() == p.b && x != Algebraic.ZERO);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() <= 0,
                        P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.nonzeroAlgebraics(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withElement(0, P.negativeIntegersGeometric())
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.nonzeroAlgebraics(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesNonzeroAlgebraics() {
        initialize("nonzeroAlgebraics()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() >= 4,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(TINY_LIMIT, rps)) {
            Iterable<Algebraic> xs = rp.nonzeroAlgebraics();
            rp.reset();
            take(TINY_LIMIT, xs).forEach(Algebraic::validate);
            simpleTest(rp, xs, x -> x != Algebraic.ZERO);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() <= 0 && s.getSecondaryScale() >= 4,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.nonzeroAlgebraics();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() < 4,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.nonzeroAlgebraics();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesAlgebraics_int() {
        initialize("algebraics(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(TINY_LIMIT, ps)) {
            Iterable<Algebraic> xs = p.a.algebraics(p.b);
            p.a.reset();
            take(TINY_LIMIT, xs).forEach(Algebraic::validate);
            simpleTest(p.a, xs, x -> x.degree() == p.b);
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() <= 0,
                        P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.algebraics(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withElement(0, P.negativeIntegersGeometric())
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.algebraics(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAlgebraics() {
        initialize("algebraics()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() >= 4,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(TINY_LIMIT, rps)) {
            Iterable<Algebraic> xs = rp.algebraics();
            rp.reset();
            take(TINY_LIMIT, xs).forEach(Algebraic::validate);
            simpleTest(rp, xs, x -> true);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() <= 0 && s.getSecondaryScale() >= 4,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.algebraics();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() < 4,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.algebraics();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesNonNegativeAlgebraicsLessThanOne_int() {
        initialize("nonNegativeAlgebraicsLessThanOne(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(TINY_LIMIT, ps)) {
            Iterable<Algebraic> xs = p.a.nonNegativeAlgebraicsLessThanOne(p.b);
            p.a.reset();
            take(TINY_LIMIT, xs).forEach(Algebraic::validate);
            simpleTest(p.a, xs, x -> x.degree() == p.b && x.signum() != -1 && lt(x, Algebraic.ONE));
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() <= 0,
                        P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.nonNegativeAlgebraicsLessThanOne(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withElement(0, P.negativeIntegersGeometric())
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.nonNegativeAlgebraicsLessThanOne(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesNonNegativeAlgebraicsLessThanOne() {
        initialize("nonNegativeAlgebraicsLessThanOne()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() >= 4,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(TINY_LIMIT, rps)) {
            Iterable<Algebraic> xs = rp.nonNegativeAlgebraicsLessThanOne();
            rp.reset();
            take(TINY_LIMIT, xs).forEach(Algebraic::validate);
            simpleTest(rp, xs, x -> x.signum() != -1 && lt(x, Algebraic.ONE));
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                s -> s.getScale() <= 0 && s.getSecondaryScale() >= 4,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.nonNegativeAlgebraicsLessThanOne();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() < 4,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.nonNegativeAlgebraicsLessThanOne();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }
}
