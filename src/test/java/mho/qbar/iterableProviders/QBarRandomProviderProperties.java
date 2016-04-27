package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.qbar.testing.QBarTestProperties;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Quadruple;
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
        propertiesMonicPolynomials_int();
        propertiesMonicPolynomials();
        propertiesMonicPolynomialsAtLeast();
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
        propertiesRangeUp_int_Algebraic();
        propertiesRangeUp_Algebraic();
        propertiesRangeDown_int_Algebraic();
        propertiesRangeDown_Algebraic();
        propertiesRange_int_Algebraic_Algebraic();
        propertiesRange_Algebraic_Algebraic();
        propertiesAlgebraicsIn_int_Interval();
        propertiesAlgebraicsIn_Interval();
        propertiesAlgebraicsNotIn_int_Interval();
        propertiesAlgebraicsNotIn_Interval();
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

    private void propertiesMonicPolynomials_int() {
        initialize("monicPolynomials(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.monicPolynomials(p.b);
            p.a.reset();
            simpleTest(p.a, qs, q -> q.degree() == p.b && q.isMonic());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() <= 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.monicPolynomials(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.negativeIntegers()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.monicPolynomials(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMonicPolynomials() {
        initialize("monicPolynomials()");
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            Iterable<Polynomial> ps = rp.monicPolynomials();
            rp.reset();
            simpleTest(rp, ps, Polynomial::isMonic);
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() <= 0 && rp.getSecondaryScale() > 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.monicPolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }

        rpsFail = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() <= 0,
                P.qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.monicPolynomials();
                fail(rp);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesMonicPolynomialsAtLeast() {
        initialize("monicPolynomialsAtLeast(int)");
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                                P.qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            Iterable<Polynomial> qs = p.a.monicPolynomialsAtLeast(p.b);
            p.a.reset();
            simpleTest(p.a, qs, q -> q.degree() >= p.b && q.isMonic());
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() <= 0 || rp.getSecondaryScale() <= 0,
                                P.qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.monicPolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() <= p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                                P.qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.monicPolynomialsAtLeast(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                                P.qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.rangeDown(-2)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.monicPolynomialsAtLeast(p.b);
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

    private void propertiesRangeUp_int_Algebraic() {
        initialize("rangeUp(int, Algebraic)");
        Iterable<Triple<QBarRandomProvider, Integer, Algebraic>> ts = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 2,
                        P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).positiveIntegersGeometric(),
                P.withScale(1).withSecondaryScale(4).algebraics()
        );
        for (Triple<QBarRandomProvider, Integer, Algebraic> t : take(SMALL_LIMIT, ts)) {
            Iterable<Algebraic> xs = t.a.rangeUp(t.b, t.c);
            simpleTest(t.a, xs, x -> x.degree() == t.b && ge(x, t.c));
        }

        Iterable<Triple<QBarRandomProvider, Integer, Algebraic>> tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 2,
                        P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.rangeDown(0),
                P.algebraics()
        );
        for (Triple<QBarRandomProvider, Integer, Algebraic> t : take(LIMIT, tsFail)) {
            try {
                t.a.rangeUp(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() < 2,
                        P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.positiveIntegers(),
                P.algebraics()
        );
        for (Triple<QBarRandomProvider, Integer, Algebraic> t : take(LIMIT, tsFail)) {
            try {
                t.a.rangeUp(t.b, t.c);
                fail(t);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesRangeUp_Algebraic() {
        initialize("rangeUp(Algebraic)");
        Iterable<Pair<QBarRandomProvider, Algebraic>> ps = P.pairs(
                filterInfinite(
                        rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 4,
                        P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(1).withSecondaryScale(4).algebraics()
        );
        for (Pair<QBarRandomProvider, Algebraic> p : take(MEDIUM_LIMIT, ps)) {
            Iterable<Algebraic> xs = p.a.rangeUp(p.b);
            simpleTest(p.a, xs, x -> ge(x, p.b));
        }

        Iterable<Pair<QBarRandomProvider, Algebraic>> psFail = P.pairs(
                filterInfinite(
                        rp -> rp.getScale() < 2 && rp.getSecondaryScale() >= 4,
                        P.qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.algebraics()
        );
        for (Pair<QBarRandomProvider, Algebraic> p : take(LIMIT, psFail)) {
            try {
                p.a.rangeUp(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairs(
                filterInfinite(
                        rp -> rp.getScale() >= 2 && rp.getSecondaryScale() < 4,
                        P.qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.algebraics()
        );
        for (Pair<QBarRandomProvider, Algebraic> p : take(LIMIT, psFail)) {
            try {
                p.a.rangeUp(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesRangeDown_int_Algebraic() {
        initialize("rangeDown(int, Algebraic)");
        Iterable<Triple<QBarRandomProvider, Integer, Algebraic>> ts = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 2,
                        P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).positiveIntegersGeometric(),
                P.withScale(1).withSecondaryScale(4).algebraics()
        );
        for (Triple<QBarRandomProvider, Integer, Algebraic> t : take(SMALL_LIMIT, ts)) {
            Iterable<Algebraic> xs = t.a.rangeDown(t.b, t.c);
            simpleTest(t.a, xs, x -> x.degree() == t.b && le(x, t.c));
        }

        Iterable<Triple<QBarRandomProvider, Integer, Algebraic>> tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 2,
                        P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.rangeDown(0),
                P.algebraics()
        );
        for (Triple<QBarRandomProvider, Integer, Algebraic> t : take(LIMIT, tsFail)) {
            try {
                t.a.rangeDown(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() < 2,
                        P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.positiveIntegers(),
                P.algebraics()
        );
        for (Triple<QBarRandomProvider, Integer, Algebraic> t : take(LIMIT, tsFail)) {
            try {
                t.a.rangeDown(t.b, t.c);
                fail(t);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesRangeDown_Algebraic() {
        initialize("rangeDown(Algebraic)");
        Iterable<Pair<QBarRandomProvider, Algebraic>> ps = P.pairs(
                filterInfinite(
                        rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 4,
                        P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(1).withSecondaryScale(4).algebraics()
        );
        for (Pair<QBarRandomProvider, Algebraic> p : take(MEDIUM_LIMIT, ps)) {
            Iterable<Algebraic> xs = p.a.rangeDown(p.b);
            simpleTest(p.a, xs, x -> le(x, p.b));
        }

        Iterable<Pair<QBarRandomProvider, Algebraic>> psFail = P.pairs(
                filterInfinite(
                        rp -> rp.getScale() < 2 && rp.getSecondaryScale() >= 4,
                        P.qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.algebraics()
        );
        for (Pair<QBarRandomProvider, Algebraic> p : take(LIMIT, psFail)) {
            try {
                p.a.rangeDown(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairs(
                filterInfinite(
                        rp -> rp.getScale() >= 2 && rp.getSecondaryScale() < 4,
                        P.qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.algebraics()
        );
        for (Pair<QBarRandomProvider, Algebraic> p : take(LIMIT, psFail)) {
            try {
                p.a.rangeDown(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesRange_int_Algebraic_Algebraic() {
        initialize("range(int, Algebraic, Algebraic)");
        Iterable<Quadruple<QBarRandomProvider, Integer, Algebraic, Algebraic>> qs = filterInfinite(
                q -> q.b == q.c.degree() && q.c.equals(q.d) || lt(q.c, q.d),
                P.quadruples(
                        filterInfinite(
                                rp -> rp.getScale() >= 2,
                                P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                        ),
                        P.withScale(4).positiveIntegersGeometric(),
                        P.withScale(1).withSecondaryScale(4).algebraics(),
                        P.withScale(1).withSecondaryScale(4).algebraics()
                )
        );
        for (Quadruple<QBarRandomProvider, Integer, Algebraic, Algebraic> q : take(SMALL_LIMIT, qs)) {
            Iterable<Algebraic> xs = q.a.range(q.b, q.c, q.d);
            simpleTest(q.a, xs, x -> x.degree() == q.b && ge(x, q.c) && le(x, q.d));
        }

        Iterable<Quadruple<QBarRandomProvider, Integer, Algebraic, Algebraic>> qsFail = filterInfinite(
                q -> q.b == q.c.degree() && q.c.equals(q.d) || lt(q.c, q.d),
                P.quadruples(
                        filterInfinite(
                                rp -> rp.getScale() >= 2,
                                P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                        ),
                        P.rangeDown(0),
                        P.algebraics(),
                        P.algebraics()
                )
        );
        for (Quadruple<QBarRandomProvider, Integer, Algebraic, Algebraic> q : take(LIMIT, qsFail)) {
            try {
                q.a.range(q.b, q.c, q.d);
                fail(q);
            } catch (IllegalArgumentException ignored) {}
        }

        qsFail = filterInfinite(
                q -> q.b == q.c.degree() && q.c.equals(q.d) || lt(q.c, q.d),
                P.quadruples(
                        filterInfinite(
                                rp -> rp.getScale() < 2,
                                P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                        ),
                        P.withScale(4).positiveIntegersGeometric(),
                        P.withScale(1).withSecondaryScale(4).algebraics(),
                        P.withScale(1).withSecondaryScale(4).algebraics()
                )
        );
        for (Quadruple<QBarRandomProvider, Integer, Algebraic, Algebraic> q : take(LIMIT, qsFail)) {
            try {
                q.a.range(q.b, q.c, q.d);
                fail(q);
            } catch (IllegalStateException ignored) {}
        }

        Iterable<Triple<QBarRandomProvider, Integer, Algebraic>> tsFail = filterInfinite(
                t -> t.b != t.c.degree(),
                P.triples(
                        filterInfinite(
                                rp -> rp.getScale() >= 2,
                                P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                        ),
                        P.withScale(4).positiveIntegersGeometric(),
                        P.withScale(1).withSecondaryScale(4).algebraics()
                )
        );
        for (Triple<QBarRandomProvider, Integer, Algebraic> t : take(LIMIT, tsFail)) {
            try {
                t.a.range(t.b, t.c, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRange_Algebraic_Algebraic() {
        initialize("range(Algebraic, Algebraic)");
        Iterable<Triple<QBarRandomProvider, Algebraic, Algebraic>> ts = filterInfinite(
                t -> le(t.b, t.c),
                P.triples(
                        filterInfinite(
                                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 4,
                                P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(1).withSecondaryScale(4).algebraics(),
                        P.withScale(1).withSecondaryScale(4).algebraics()
                )
        );
        for (Triple<QBarRandomProvider, Algebraic, Algebraic> t : take(MEDIUM_LIMIT, ts)) {
            Iterable<Algebraic> xs = t.a.range(t.b, t.c);
            simpleTest(t.a, xs, x -> ge(x, t.b) && le(x, t.c));
        }

        Iterable<Triple<QBarRandomProvider, Algebraic, Algebraic>> tsFail = filterInfinite(
                t -> gt(t.b, t.c),
                P.triples(
                        filterInfinite(
                                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 4,
                                P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.algebraics(),
                        P.algebraics()
                )
        );
        for (Triple<QBarRandomProvider, Algebraic, Algebraic> t : take(LIMIT, tsFail)) {
            try {
                t.a.range(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = filterInfinite(
                t -> le(t.b, t.c),
                P.triples(
                        filterInfinite(
                                rp -> rp.getScale() < 2 && rp.getSecondaryScale() >= 4,
                                P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(1).withSecondaryScale(4).algebraics(),
                        P.withScale(1).withSecondaryScale(4).algebraics()
                )
        );
        for (Triple<QBarRandomProvider, Algebraic, Algebraic> t : take(LIMIT, tsFail)) {
            try {
                t.a.range(t.b, t.c);
                fail(t);
            } catch (IllegalStateException ignored) {}
        }

        tsFail = filterInfinite(
                t -> le(t.b, t.c),
                P.triples(
                        filterInfinite(
                                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() < 4,
                                P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(1).withSecondaryScale(4).algebraics(),
                        P.withScale(1).withSecondaryScale(4).algebraics()
                )
        );
        for (Triple<QBarRandomProvider, Algebraic, Algebraic> t : take(LIMIT, tsFail)) {
            try {
                t.a.range(t.b, t.c);
                fail(t);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesAlgebraicsIn_int_Interval() {
        initialize("algebraicsIn(int, Interval)");
        Iterable<Triple<QBarRandomProvider, Integer, Interval>> ts = filterInfinite(
                t -> t.b == 1 || !t.c.isFinitelyBounded() || !t.c.getLower().get().equals(t.c.getUpper().get()),
                        P.triples(
                        filterInfinite(
                                rp -> rp.getScale() >= 2,
                                P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                        ),
                        P.withScale(4).positiveIntegersGeometric(),
                        P.withScale(6).intervals()
                )
        );
        for (Triple<QBarRandomProvider, Integer, Interval> t : take(SMALL_LIMIT, ts)) {
            Iterable<Algebraic> xs = t.a.algebraicsIn(t.b, t.c);
            simpleTest(t.a, xs, x -> x.degree() == t.b && t.c.contains(x));
        }

        Iterable<Triple<QBarRandomProvider, Integer, Interval>> tsFail = filterInfinite(
                t -> t.b == 1 || !t.c.isFinitelyBounded() || !t.c.getLower().get().equals(t.c.getUpper().get()),
                        P.triples(
                        filterInfinite(
                                rp -> rp.getScale() >= 2,
                                P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                        ),
                        P.withScale(4).rangeDown(0),
                        P.intervals()
                )
        );
        for (Triple<QBarRandomProvider, Integer, Interval> t : take(LIMIT, tsFail)) {
            try {
                t.a.algebraicsIn(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = filterInfinite(
                t -> t.b == 1 || !t.c.isFinitelyBounded() || !t.c.getLower().get().equals(t.c.getUpper().get()),
                        P.triples(
                        filterInfinite(
                                rp -> rp.getScale() < 2,
                                P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                        ),
                        P.positiveIntegers(),
                        P.intervals()
                )
        );
        for (Triple<QBarRandomProvider, Integer, Interval> t : take(LIMIT, tsFail)) {
            try {
                t.a.algebraicsIn(t.b, t.c);
                fail(t);
            } catch (IllegalStateException ignored) {}
        }

        Iterable<Triple<QBarRandomProvider, Integer, Rational>> tsFail2 = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 2,
                        P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.rangeUp(2),
                P.rationals()
        );
        for (Triple<QBarRandomProvider, Integer, Rational> t : take(LIMIT, tsFail2)) {
            try {
                t.a.algebraicsIn(t.b, Interval.of(t.c));
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAlgebraicsIn_Interval() {
        initialize("algebraicsIn(Interval)");
        Iterable<Pair<QBarRandomProvider, Interval>> ps = P.pairs(
                filterInfinite(
                        rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 4,
                        P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(6).intervals()
        );
        for (Pair<QBarRandomProvider, Interval> p : take(MEDIUM_LIMIT, ps)) {
            Iterable<Algebraic> xs = p.a.algebraicsIn(p.b);
            simpleTest(p.a, xs, p.b::contains);
        }

        Iterable<Pair<QBarRandomProvider, Interval>> psFail = P.pairs(
                filterInfinite(
                        rp -> rp.getScale() < 2 && rp.getSecondaryScale() >= 4,
                        P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.intervals()
        );
        for (Pair<QBarRandomProvider, Interval> p : take(LIMIT, psFail)) {
            try {
                p.a.algebraicsIn(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairs(
                filterInfinite(
                        rp -> rp.getScale() >= 2 && rp.getSecondaryScale() < 4,
                        P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.intervals()
        );
        for (Pair<QBarRandomProvider, Interval> p : take(LIMIT, psFail)) {
            try {
                p.a.algebraicsIn(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }
    }

    private void propertiesAlgebraicsNotIn_int_Interval() {
        initialize("algebraicsNotIn(int, Interval)");
        Iterable<Triple<QBarRandomProvider, Integer, Interval>> ts = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 2,
                        P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).positiveIntegersGeometric(),
                filterInfinite(a -> !a.equals(Interval.ALL), P.withScale(6).intervals())
        );
        for (Triple<QBarRandomProvider, Integer, Interval> t : take(SMALL_LIMIT, ts)) {
            Iterable<Algebraic> xs = t.a.algebraicsNotIn(t.b, t.c);
            simpleTest(t.a, xs, x -> x.degree() == t.b && !t.c.contains(x));
        }

        Iterable<Triple<QBarRandomProvider, Integer, Interval>> tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 2,
                        P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.rangeDown(0),
                filterInfinite(a -> !a.equals(Interval.ALL), P.withScale(6).intervals())
        );
        for (Triple<QBarRandomProvider, Integer, Interval> t : take(LIMIT, tsFail)) {
            try {
                t.a.algebraicsNotIn(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = P.triples(
                filterInfinite(
                        rp -> rp.getScale() < 2,
                        P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.positiveIntegers(),
                filterInfinite(a -> !a.equals(Interval.ALL), P.withScale(6).intervals())
        );
        for (Triple<QBarRandomProvider, Integer, Interval> t : take(LIMIT, tsFail)) {
            try {
                t.a.algebraicsNotIn(t.b, t.c);
                fail(t);
            } catch (IllegalStateException ignored) {}
        }

        Iterable<Pair<QBarRandomProvider, Integer>> psFail = P.pairs(
                filterInfinite(
                        rp -> rp.getScale() >= 2,
                        P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.positiveIntegers()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.algebraicsNotIn(p.b, Interval.ALL);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAlgebraicsNotIn_Interval() {
        initialize("algebraicsNotIn(Interval)");
        Iterable<Pair<QBarRandomProvider, Interval>> ps = P.pairs(
                filterInfinite(
                        rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 4,
                        P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                ),
                filterInfinite(a -> !a.equals(Interval.ALL), P.withScale(6).intervals())
        );
        for (Pair<QBarRandomProvider, Interval> p : take(MEDIUM_LIMIT, ps)) {
            Iterable<Algebraic> xs = p.a.algebraicsNotIn(p.b);
            simpleTest(p.a, xs, x -> !p.b.contains(x));
        }

        Iterable<Pair<QBarRandomProvider, Interval>> psFail = P.pairs(
                filterInfinite(
                        rp -> rp.getScale() < 2 && rp.getSecondaryScale() >= 4,
                        P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                ),
                filterInfinite(a -> !a.equals(Interval.ALL), P.withScale(6).intervals())
        );
        for (Pair<QBarRandomProvider, Interval> p : take(LIMIT, psFail)) {
            try {
                p.a.algebraicsNotIn(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        psFail = P.pairs(
                filterInfinite(
                        rp -> rp.getScale() >= 2 && rp.getSecondaryScale() < 4,
                        P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                ),
                filterInfinite(a -> !a.equals(Interval.ALL), P.withScale(6).intervals())
        );
        for (Pair<QBarRandomProvider, Interval> p : take(LIMIT, psFail)) {
            try {
                p.a.algebraicsNotIn(p.b);
                fail(p);
            } catch (IllegalStateException ignored) {}
        }

        Iterable<QBarRandomProvider> rpsFail = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 4,
                P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(LIMIT, rpsFail)) {
            try {
                rp.algebraicsNotIn(Interval.ALL);
                fail(rp);
            } catch (IllegalArgumentException ignored) {}
        }
    }
}
