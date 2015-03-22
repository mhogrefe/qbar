package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.structures.Pair;

import java.util.Random;

import static mho.wheels.iterables.IterableUtils.*;

@SuppressWarnings({"ConstantConditions", "UnusedDeclaration"})
public class RationalPolynomialDemos {
    private static final boolean USE_RANDOM = false;
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

    public static void demoIterator() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println("toList(" + p + ") = " + toList(p));
        }
    }

    public static void demoCoefficient() {
        initialize();
        Iterable<Pair<RationalPolynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.rationalPolynomials(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.rationalPolynomials(), ((RandomProvider) P).naturalIntegersGeometric(10));
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("coefficient(" + p.a + ", " + p.b + ") = " + p.a.coefficient(p.b));
        }
    }

    public static void demoToString() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println(p);
        }
    }
}