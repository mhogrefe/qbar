package mho.qbar.iterableProviders;

import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import mho.qbar.testing.QBarDemos;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;

import static mho.wheels.iterables.IterableUtils.filterInfinite;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.testing.Testing.MEDIUM_LIMIT;
import static mho.wheels.testing.Testing.its;

@SuppressWarnings("UnusedDeclaration")
public class QBarRandomProviderDemos extends QBarDemos {
    public QBarRandomProviderDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoPositiveRationals() {
        initialize();
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("positiveRationals(" + rp + ") = " + its(rp.positiveRationals()));
        }
    }

    private void demoNegativeRationals() {
        initialize();
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("negativeRationals(" + rp + ") = " + its(rp.negativeRationals()));
        }
    }

    private void demoNonzeroRationals() {
        initialize();
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("nonzeroRationals(" + rp + ") = " + its(rp.nonzeroRationals()));
        }
    }

    private void demoRationals() {
        initialize();
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 3,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("rationals(" + rp + ") = " + its(rp.rationals()));
        }
    }

    private void demoNonNegativeRationalsLessThanOne() {
        initialize();
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
        initialize();
        Iterable<Pair<QBarRandomProvider, Rational>> ps = P.pairs(
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rangeUp(" + p.a + ", " + p.b + ") = " + its(p.a.rangeUp(p.b)));
        }
    }

    private void demoRangeDown_Rational() {
        initialize();
        Iterable<Pair<QBarRandomProvider, Rational>> ps = P.pairs(
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rangeDown(" + p.a + ", " + p.b + ") = " + its(p.a.rangeDown(p.b)));
        }
    }

    private void demoRange_Rational_Rational() {
        initialize();
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
        initialize();
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 6,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("finitelyBoundedIntervals(" + rp + ") = " + its(rp.finitelyBoundedIntervals()));
        }
    }

    private void demoIntervals() {
        initialize();
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 6,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("intervals(" + rp + ") = " + its(rp.intervals()));
        }
    }

    private void demoRationalsIn() {
        initialize();
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
        initialize();
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
}