package mho.qbar.iterableProviders;

import mho.qbar.objects.Rational;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.structures.Pair;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class QBarRandomProviderDemos {
    private static final boolean USE_RANDOM = false;
    private static int LIMIT;
    private static final int SMALL_LIMIT = 1000;
    private static final int TINY_LIMIT = 100;
    private static QBarIterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = QBarRandomProvider.example();
            LIMIT = 1000;
        } else {
            P = QBarExhaustiveProvider.INSTANCE;
            LIMIT = 10000;
        }
    }

    private static void demoPositiveRationals() {
        initialize();
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("positiveRationals(" + rp + ") = " + its(rp.positiveRationals()));
        }
    }

    private static void demoNegativeRationals() {
        initialize();
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("negativeRationals(" + rp + ") = " + its(rp.negativeRationals()));
        }
    }

    private static void demoNonzeroRationals() {
        initialize();
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("nonzeroRationals(" + rp + ") = " + its(rp.nonzeroRationals()));
        }
    }

    private static void demoRationals() {
        initialize();
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 3,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("rationals(" + rp + ") = " + its(rp.rationals()));
        }
    }

    private static void demoNonNegativeRationalsLessThanOne() {
        initialize();
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("nonNegativeRationalsLessThanOne(" + rp + ") = " +
                    its(rp.nonNegativeRationalsLessThanOne()));
        }
    }

    private static void demoRangeUp_Rational() {
        initialize();
        Iterable<Pair<QBarRandomProvider, Rational>> ps = P.pairs(
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(SMALL_LIMIT, ps)) {
            System.out.println("rangeUp(" + p.a + ", " + p.b + ") = " + its(p.a.rangeUp(p.b)));
        }
    }

    private static void demoRangeDown_Rational() {
        initialize();
        Iterable<Pair<QBarRandomProvider, Rational>> ps = P.pairs(
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultSecondaryScale()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(SMALL_LIMIT, ps)) {
            System.out.println("rangeDown(" + p.a + ", " + p.b + ") = " + its(p.a.rangeDown(p.b)));
        }
    }
}