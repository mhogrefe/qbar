package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.structures.Pair;

import java.util.Random;

import static mho.qbar.objects.RationalVector.*;
import static mho.wheels.iterables.IterableUtils.*;

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

    public static void demoX() {
        initialize();
        Iterable<RationalVector> vs = map(p -> {
            assert p.b != null;
            return RationalVector.of(toList(cons(p.a, p.b)));
        }, P.pairs(P.rationals(), P.rationalVectors()));
        for (RationalVector v : take(LIMIT, vs)) {
            System.out.println("x(" + v + ") = " + v.x());
        }
    }

    public static void demoY() {
        initialize();
        Iterable<RationalVector> vs = map(p -> {
            assert p.b != null;
            return RationalVector.of(toList(concat(p.a, p.b)));
        }, P.pairs(P.rationalVectors(2), P.rationalVectors()));
        for (RationalVector v : take(LIMIT, vs)) {
            System.out.println("y(" + v + ") = " + v.y());
        }
    }

    public static void demoZ() {
        initialize();
        Iterable<RationalVector> vs = map(p -> {
            assert p.b != null;
            return RationalVector.of(toList(concat(p.a, p.b)));
        }, P.pairs(P.rationalVectors(3), P.rationalVectors()));
        for (RationalVector v : take(LIMIT, vs)) {
            System.out.println("z(" + v + ") = " + v.z());
        }
    }

    public static void demoW() {
        initialize();
        Iterable<RationalVector> vs = map(p -> {
            assert p.b != null;
            return RationalVector.of(toList(concat(p.a, p.b)));
        }, P.pairs(P.rationalVectors(4), P.rationalVectors()));
        for (RationalVector v : take(LIMIT, vs)) {
            System.out.println("w(" + v + ") = " + v.w());
        }
    }

    public static void demoX_int() {
        initialize();
        Iterable<Pair<RationalVector, Integer>> ps = P.dependentPairsLogarithmic(
                P.rationalVectors(),
                v -> range(0, v.dimension() - 1)
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("x(" + p.a + ", " + p.b + ") = " + p.a.x(p.b));
        }
    }

    public static void demoToString() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println(v);
        }
    }
}
