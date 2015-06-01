package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.junit.Assert;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.RationalVector.*;
import static mho.wheels.iterables.IterableUtils.*;

@SuppressWarnings({"ConstantConditions", "UnusedDeclaration"})
public class RationalVectorDemos {
    private static final boolean USE_RANDOM = false;
    private static final String RATIONAL_VECTOR_CHARS = " ,-/0123456789[]";
    private static final int SMALL_LIMIT = 100;
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

    private static void demoIterator() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("toList(" + v + ") = " + toList(v));
        }
    }

    private static void demoX() {
        initialize();
        Iterable<RationalVector> vs = map(
                p -> RationalVector.of(toList(cons(p.a, p.b))),
                P.pairs(P.rationals(), P.rationalVectors())
        );
        for (RationalVector v : take(LIMIT, vs)) {
            System.out.println("x(" + v + ") = " + v.x());
        }
    }

    private static void demoY() {
        initialize();
        Iterable<RationalVector> vs = map(
                p -> RationalVector.of(toList(concat(p.a, p.b))),
                P.pairs(P.rationalVectors(2), P.rationalVectors())
        );
        for (RationalVector v : take(LIMIT, vs)) {
            System.out.println("y(" + v + ") = " + v.y());
        }
    }

    private static void demoZ() {
        initialize();
        Iterable<RationalVector> vs = map(
                p -> RationalVector.of(toList(concat(p.a, p.b))),
                P.pairs(P.rationalVectors(3), P.rationalVectors())
        );
        for (RationalVector v : take(LIMIT, vs)) {
            System.out.println("z(" + v + ") = " + v.z());
        }
    }

    private static void demoW() {
        initialize();
        Iterable<RationalVector> vs = map(
                p -> RationalVector.of(toList(concat(p.a, p.b))),
                P.pairs(P.rationalVectors(4), P.rationalVectors())
        );
        for (RationalVector v : take(LIMIT, vs)) {
            System.out.println("w(" + v + ") = " + v.w());
        }
    }

    private static void demoX_int() {
        initialize();
        Iterable<Pair<RationalVector, Integer>> ps = P.dependentPairs(
                P.rationalVectors(),
                v -> range(0, v.dimension() - 1)
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            System.out.println("x(" + p.a + ", " + p.b + ") = " + p.a.x(p.b));
        }
    }

    private static void demoOf_List_Rational() {
        initialize();
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("of(" + listString + ") = " + of(rs));
        }
    }

    private static void demoOf_Rational() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("of(" + r + ") = " + of(r));
        }
    }

    private static void demoDimension() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("dim(" + v + ") = " + v.dimension());
        }
    }

    private static void demoZero() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = P.withScale(20).naturalIntegersGeometric();
        }
        for (int i : take(SMALL_LIMIT, is)) {
            System.out.println("zero(" + i + ") = " + zero(i));
        }
    }

    private static void demoStandard() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = P.withScale(20).naturalIntegersGeometric();
        }
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, filter(q -> q.a > q.b, P.pairs(is)))) {
            System.out.println("standard(" + p.a + ", " + p.b + ") = " + standard(p.a, p.b));
        }
    }

    private static void demoIsZero() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println(v + " is " + (v.isZero() ? "" : " not ") + "zero");
        }
    }

    private static void demoAdd() {
        initialize();
        Iterable<Pair<RationalVector, RationalVector>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.dependentPairs(P.rationalVectors(), v -> P.rationalVectors(v.dimension()));
        } else {
            ps = P.dependentPairs(
                    P.withScale(8).rationalVectors(),
                    v -> P.withScale(8).rationalVectors(v.dimension())
            );
        }
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private static void demoNegate() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("-" + v + " = " + v.negate());
        }
    }

    private static void demoSubtract() {
        initialize();
        Iterable<Pair<RationalVector, RationalVector>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.dependentPairs(P.rationalVectors(), v -> P.rationalVectors(v.dimension()));
        } else {
            ps = P.dependentPairs(
                    P.withScale(8).rationalVectors(),
                    v -> P.withScale(8).rationalVectors(v.dimension())
            );
        }
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private static void demoMultiply_Rational() {
        initialize();
        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(), P.rationals()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private static void demoMultiply_BigInteger() {
        initialize();
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, P.pairs(P.rationalVectors(), P.bigIntegers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private static void demoMultiply_int() {
        initialize();
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), P.integers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private static void demoDivide_Rational() {
        initialize();
        Iterable<Pair<RationalVector, Rational>> ps = filter(
                p -> p.b != Rational.ZERO,
                P.pairs(P.rationalVectors(), P.rationals())
        );
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private static void demoDivide_BigInteger() {
        initialize();
        Iterable<Pair<RationalVector, BigInteger>> ps = P.pairs(
                P.rationalVectors(),
                filter(bi -> !bi.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private static void demoDivide_int() {
        initialize();
        Iterable<Pair<RationalVector, Integer>> ps = P.pairs(P.rationalVectors(), filter(i -> i != 0, P.integers()));
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private static void demoShiftLeft() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = P.integersGeometric();
        }
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), is))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private static void demoShiftRight() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = P.integersGeometric();
        }
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), is))) {
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

//    private static void demoSum() {
//        initialize();
//        Iterable<Pair<Integer, Integer>> ps;
//        if (P instanceof QBarExhaustiveProvider) {
//            ps = P.pairs(P.positiveIntegers(), P.naturalIntegers());
//        } else {
//            ps = P.pairs(
//                    P.withScale(5).positiveIntegersGeometric(),
//                    P.withScale(5).naturalIntegersGeometric()
//            );
//        }
//        Iterable<List<RationalVector>> vss = map(
//                q -> q.b,
//                P.dependentPairsSquare(ps, p -> P.lists(p.a, P.rationalVectors(p.b)))
//        );
//        for (List<RationalVector> vs : take(LIMIT, vss)) {
//            String listString = tail(init(vs.toString()));
//            System.out.println("Σ(" + listString + ") = " + sum(vs));
//        }
//    }

//    private static void demoDelta() {
//        initialize();
//        Iterable<Pair<Integer, Integer>> ps;
//        if (P instanceof QBarExhaustiveProvider) {
//            ps = P.pairs(P.positiveIntegers(), P.naturalIntegers());
//        } else {
//            ps = P.pairs(
//                    P.withScale(5).positiveIntegersGeometric(),
//                    P.withScale(5).naturalIntegersGeometric()
//            );
//        }
//        Iterable<List<RationalVector>> vss = map(
//                q -> q.b,
//                P.dependentPairsSquare(ps, p -> P.lists(p.a, P.rationalVectors(p.b)))
//        );
//        for (List<RationalVector> vs : take(LIMIT, vss)) {
//            String listString = tail(init(vs.toString()));
//            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(delta(vs)));
//        }
//    }

    private static void demoDot() {
        initialize();
        Iterable<Pair<RationalVector, RationalVector>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.dependentPairs(P.rationalVectors(), v -> P.rationalVectors(v.dimension()));
        } else {
            ps = P.dependentPairs(
                    P.withScale(8).rationalVectors(),
                    v -> P.withScale(8).rationalVectors(v.dimension())
            );
        }
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            System.out.println("dot(" + p.a + ", " + p.b + ") = " + p.a.dot(p.b));
        }
    }

    private static void demoRightAngleCompare() {
        initialize();
        Iterable<Pair<RationalVector, RationalVector>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.dependentPairs(P.rationalVectors(), v -> P.rationalVectors(v.dimension()));
        } else {
            ps = P.dependentPairs(
                    P.withScale(8).rationalVectors(),
                    v -> P.withScale(8).rationalVectors(v.dimension())
            );
        }
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            String angleType;
            switch (p.a.rightAngleCompare(p.b)) {
                case LT:
                    angleType = "an acute";
                    break;
                case EQ:
                    angleType = "a right";
                    break;
                case GT:
                    angleType = "an obtuse";
                    break;
                default:
                    Assert.fail();
                    return;
            }
            System.out.println(p.a + " and " + p.b + " make " + angleType + " angle");
        }
    }

    private static void demoSquaredLength() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("squaredLength(" + v + ") = " + v.squaredLength());
        }
    }

    private static void demoCancelDenominators() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("cancelDenominators(" + v + ") = " + v.cancelDenominators());
        }
    }

    private static void demoPivot() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("pivot(" + v + ") = " + v.pivot());
        }
    }

    private static void demoReduce() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("reduce(" + v + ") = " + v.reduce());
        }
    }

    private static void demoEquals_RationalVector() {
        initialize();
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, P.pairs(P.rationalVectors()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private static void demoEquals_null() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            //noinspection ObjectEqualsNull
            System.out.println(v + (v.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private static void demoHashCode() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("hashCode(" + v + ") = " + v.hashCode());
        }
    }

    private static void demoCompareTo() {
        initialize();
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, P.pairs(P.rationalVectors()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private static void demoRead() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private static void demoRead_targeted() {
        initialize();
        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(RATIONAL_VECTOR_CHARS);
        } else {
            cs = P.uniformSample(RATIONAL_VECTOR_CHARS);
        }
        for (String s : take(LIMIT, P.strings(cs))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private static void demoFindIn() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private static void demoFindIn_targeted() {
        initialize();
        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(RATIONAL_VECTOR_CHARS);
        } else {
            cs = P.uniformSample(RATIONAL_VECTOR_CHARS);
        }
        for (String s : take(LIMIT, P.strings(cs))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private static void demoToString() {
        initialize();
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println(v);
        }
    }
}
