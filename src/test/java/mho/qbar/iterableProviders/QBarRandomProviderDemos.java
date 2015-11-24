package mho.qbar.iterableProviders;

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
}