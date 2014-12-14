package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;

import java.util.Random;

import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.iterables.IterableUtils.toList;

public class RationalVectorDemos {
    private static final boolean USE_RANDOM = false;
    private static final String RATIONAL_VECTOR_CHARS = " ,-/0123456789[]";
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
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("toList(" + v + ") = " + toList(v));
        }
    }

    public static void demoToString() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println(v);
        }
    }
}
