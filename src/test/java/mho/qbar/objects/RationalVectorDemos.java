package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;

import java.util.List;
import java.util.Random;

import static mho.qbar.objects.RationalVector.*;
import static mho.wheels.iterables.IterableUtils.*;

@SuppressWarnings("ConstantConditions")
public class RationalVectorDemos {
    private static final boolean USE_RANDOM = false;
    private static final String RATIONAL_VECTOR_CHARS = " ,-/0123456789[]";
    private static final int SMALL_LIMIT = 100;
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
        Iterable<RationalVector> vs = map(
                p -> RationalVector.of(toList(cons(p.a, p.b))),
                P.pairs(P.rationals(), P.rationalVectors())
        );
        for (RationalVector v : take(LIMIT, vs)) {
            System.out.println("x(" + v + ") = " + v.x());
        }
    }

    public static void demoY() {
        initialize();
        Iterable<RationalVector> vs = map(
                p -> RationalVector.of(toList(concat(p.a, p.b))),
                P.pairs(P.rationalVectors(2), P.rationalVectors())
        );
        for (RationalVector v : take(LIMIT, vs)) {
            System.out.println("y(" + v + ") = " + v.y());
        }
    }

    public static void demoZ() {
        initialize();
        Iterable<RationalVector> vs = map(
                p -> RationalVector.of(toList(concat(p.a, p.b))),
                P.pairs(P.rationalVectors(3), P.rationalVectors())
        );
        for (RationalVector v : take(LIMIT, vs)) {
            System.out.println("z(" + v + ") = " + v.z());
        }
    }

    public static void demoW() {
        initialize();
        Iterable<RationalVector> vs = map(
                p -> RationalVector.of(toList(concat(p.a, p.b))),
                P.pairs(P.rationalVectors(4), P.rationalVectors())
        );
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
            System.out.println("x(" + p.a + ", " + p.b + ") = " + p.a.x(p.b));
        }
    }

    public static void demoOf_List_Rational() {
        initialize();
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("of(" + listString + ") = " + of(rs));
        }
    }

    public static void demoOf_Rational() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("of(" + r + ") = " + of(r));
        }
    }

    public static void demoDimension() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("dim(" + v + ") = " + v.dimension());
        }
    }

    public static void demoZero() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof ExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = ((RandomProvider) P).naturalIntegersGeometric(20);
        }
        for (int i : take(SMALL_LIMIT, is)) {
            System.out.println("zero(" + i + ") = " + zero(i));
        }
    }

    public static void demoIdentity() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof ExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = ((RandomProvider) P).naturalIntegersGeometric(20);
        }
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, filter(q -> q.a > q.b, P.pairs(is)))) {
            System.out.println("identity(" + p.a + ", " + p.b + ") = " + identity(p.a, p.b));
        }
    }

    public static void demoIsZero() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println(v + " is " + (v.isZero() ? "" : " not ") + "zero");
        }
    }

    public static void demoNegate() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("-" + v + " = " + v.negate());
        }
    }

    public static void demoAdd() {
        initialize();
        Iterable<Pair<RationalVector, RationalVector>> ps;
        if (P instanceof ExhaustiveProvider) {
            ps = P.dependentPairs(
                    P.rationalVectors(),
                    v -> P.rationalVectors(v.dimension())
            );
        } else {
            ps = P.dependentPairs(
                    ((QBarRandomProvider) P).rationalVectorsBySize(32),
                    v -> ((QBarRandomProvider) P).rationalVectorsBySize(32, v.dimension())
            );
        }
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    public static void demoEquals_RationalVector() {
        initialize();
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, P.pairs(P.rationalVectors()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    public static void demoEquals_null() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            //noinspection ObjectEqualsNull
            System.out.println(v + (v.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    public static void demoHashCode() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("hashCode(" + v + ") = " + v.hashCode());
        }
    }

    public static void demoCompareTo() {
        initialize();
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, P.pairs(P.rationalVectors()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    public static void demoRead() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    public static void demoRead_targeted() {
        initialize();
        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(RATIONAL_VECTOR_CHARS);
        } else {
            cs = ((QBarRandomProvider) P).uniformSample(RATIONAL_VECTOR_CHARS);
        }
        for (String s : take(LIMIT, P.strings(cs))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    public static void demoFindIn() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    public static void demoFindIn_targeted() {
        initialize();
        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(RATIONAL_VECTOR_CHARS);
        } else {
            cs = ((QBarRandomProvider) P).uniformSample(RATIONAL_VECTOR_CHARS);
        }
        for (String s : take(LIMIT, P.strings(cs))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    public static void demoToString() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println(v);
        }
    }
}
