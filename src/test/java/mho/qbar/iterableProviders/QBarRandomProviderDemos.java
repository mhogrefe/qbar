package mho.qbar.iterableProviders;

import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import mho.qbar.testing.QBarDemos;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;

import static mho.wheels.iterables.IterableUtils.filterInfinite;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class QBarRandomProviderDemos extends QBarDemos {
    public QBarRandomProviderDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoPositiveRationals() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("positiveRationals(" + rp + ") = " + its(rp.positiveRationals()));
        }
    }

    private void demoNegativeRationals() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("negativeRationals(" + rp + ") = " + its(rp.negativeRationals()));
        }
    }

    private void demoNonzeroRationals() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("nonzeroRationals(" + rp + ") = " + its(rp.nonzeroRationals()));
        }
    }

    private void demoRationals() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 3,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("rationals(" + rp + ") = " + its(rp.rationals()));
        }
    }

    private void demoNonNegativeRationalsLessThanOne() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("nonNegativeRationalsLessThanOne(" + rp + ") = " +
                    its(rp.nonNegativeRationalsLessThanOne()));
        }
    }

    private void demoRangeUp_Rational() {
        Iterable<Pair<QBarRandomProvider, Rational>> ps = P.pairs(
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rangeUp(" + p.a + ", " + p.b + ") = " + its(p.a.rangeUp(p.b)));
        }
    }

    private void demoRangeDown_Rational() {
        Iterable<Pair<QBarRandomProvider, Rational>> ps = P.pairs(
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rangeDown(" + p.a + ", " + p.b + ") = " + its(p.a.rangeDown(p.b)));
        }
    }

    private void demoRange_Rational_Rational() {
        Iterable<Triple<QBarRandomProvider, Rational, Rational>> ts = filterInfinite(
                t -> le(t.b, t.c),
                P.triples(
                        filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultSecondaryScale()),
                        P.rationals(),
                        P.rationals()
                )
        );
        for (Triple<QBarRandomProvider, Rational, Rational> t : take(MEDIUM_LIMIT, ts)) {
            System.out.println("range(" + t.a + ", " + t.b + ", " + t.c + ") = " + its(t.a.range(t.b, t.c)));
        }
    }

    private void demoFinitelyBoundedIntervals() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 6,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("finitelyBoundedIntervals(" + rp + ") = " + its(rp.finitelyBoundedIntervals()));
        }
    }

    private void demoIntervals() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 6,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("intervals(" + rp + ") = " + its(rp.intervals()));
        }
    }

    private void demoRationalsIn() {
        Iterable<Pair<QBarRandomProvider, Interval>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() >= 4,
                        P.qbarRandomProvidersDefaultSecondaryScale()
                ),
                P.intervals()
        );
        for (Pair<QBarRandomProvider, Interval> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rationalsIn(" + p.a + ", " + p.b + ") = " + its(p.a.rationalsIn(p.b)));
        }
    }

    private void demoRationalsNotIn() {
        Iterable<Pair<QBarRandomProvider, Interval>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() >= 4,
                        P.qbarRandomProvidersDefaultSecondaryScale()
                ),
                filterInfinite(a -> !a.equals(Interval.ALL), P.intervals())
        );
        for (Pair<QBarRandomProvider, Interval> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rationalsNotIn(" + p.a + ", " + p.b + ") = " + its(p.a.rationalsNotIn(p.b)));
        }
    }

    private void demoRationalVectors_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() >= 3, P.withScale(4).qbarRandomProvidersDefaultSecondaryScale()),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            System.out.println("rationalVectors(" + p.a + ", " + p.b + ") = " + its(p.a.rationalVectors(p.b)));
        }
    }

    private void demoRationalVectors() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() > 0,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            System.out.println("rationalVectors(" + rp + ") = " + its(rp.rationalVectors()));
        }
    }

    private void demoRationalVectorsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() >= 3, P.withScale(4).qbarRandomProviders()),
                        P.withScale(4).naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            System.out.println("rationalVectorsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.rationalVectorsAtLeast(p.b)));
        }
    }

    private void demoReducedRationalVectors_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(rp -> rp.getScale() > 0, P.withScale(4).qbarRandomProvidersDefaultSecondaryScale()),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            System.out.println("reducedRationalVectors(" + p.a + ", " + p.b + ") = " +
                    its(p.a.reducedRationalVectors(p.b)));
        }
    }

    private void demoReducedRationalVectors() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(LIMIT, rps)) {
            System.out.println("reducedRationalVectors(" + rp + ") = " + its(rp.reducedRationalVectors()));
        }
    }

    private void demoReducedRationalVectorsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(rp -> rp.getScale() > 0, P.withScale(4).qbarRandomProviders()),
                        P.withScale(4).naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(LIMIT, ps)) {
            System.out.println("reducedRationalVectorsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.reducedRationalVectorsAtLeast(p.b)));
        }
    }
}