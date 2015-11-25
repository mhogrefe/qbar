package mho.qbar.iterableProviders;

import mho.qbar.objects.Rational;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class QBarExhaustiveProviderDemos {
    private static final boolean USE_RANDOM = false;
    private static final @NotNull QBarExhaustiveProvider EP = QBarExhaustiveProvider.INSTANCE;
    private static final int SMALL_LIMIT = 1000;
    private static final int TINY_LIMIT = 100;
    private static int LIMIT;
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

    private static void demoRangeUp_Rational() {
        initialize();
        for (Rational r : take(SMALL_LIMIT, P.rationals())) {
            System.out.println("rangeUp(" + r + ") = " + its(EP.rangeUp(r)));
        }
    }

    private static void demoRangeDown_Rational() {
        initialize();
        for (Rational r : take(SMALL_LIMIT, P.rationals())) {
            System.out.println("rangeDown(" + r + ") = " + its(EP.rangeDown(r)));
        }
    }

    private static void demoRange_Rational_Rational() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            System.out.println("range(" + p.a + ", " + p.b + ") = " + its(EP.range(p.a, p.b)));
        }
    }
}
