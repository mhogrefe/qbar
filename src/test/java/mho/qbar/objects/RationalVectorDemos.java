package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.RationalVector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class RationalVectorDemos extends QBarDemos {
    private static final @NotNull String RATIONAL_VECTOR_CHARS = " ,-/0123456789[]";

    public RationalVectorDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoIterator() {
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("toList(" + v + ") = " + toList(v));
        }
    }

    private void demoGet() {
        Iterable<Pair<RationalVector, Integer>> ps = P.dependentPairs(
                P.rationalVectorsAtLeast(1),
                v -> P.uniformSample(toList(range(0, v.dimension() - 1)))
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            System.out.println("get(" + p.a + ", " + p.b + ") = " + p.a.get(p.b));
        }
    }

    private void demoOf_List_Rational() {
        for (List<Rational> rs : take(LIMIT, P.withScale(4).lists(P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("of(" + listString + ") = " + of(rs));
        }
    }

    private void demoOf_Rational() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("of(" + r + ") = " + of(r));
        }
    }

    private void demoDimension() {
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("dim(" + v + ") = " + v.dimension());
        }
    }

    private void demoIsZero() {
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println(v + " is " + (v.isZero() ? "" : " not ") + "zero");
        }
    }

    private void demoZero() {
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            System.out.println("zero(" + i + ") = " + zero(i));
        }
    }

    private void demoStandard() {
        for (Pair<Integer, Integer> p : take(MEDIUM_LIMIT, P.subsetPairs(P.naturalIntegersGeometric()))) {
            System.out.println("standard(" + p.b + ", " + p.a + ") = " + standard(p.b, p.a));
        }
    }

    private void demoAdd() {
        Iterable<Pair<RationalVector, RationalVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.rationalVectors(i))
                        )
                )
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private void demoNegate() {
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("-" + v + " = " + v.negate());
        }
    }

    private void demoSubtract() {
        Iterable<Pair<RationalVector, RationalVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.rationalVectors(i))
                        )
                )
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_Rational() {
        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(), P.rationals()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, P.pairs(P.rationalVectors(), P.bigIntegers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), P.integers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoDivide_Rational() {
        Iterable<Pair<RationalVector, Rational>> ps = filter(
                p -> p.b != Rational.ZERO,
                P.pairs(P.rationalVectors(), P.rationals())
        );
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_BigInteger() {
        Iterable<Pair<RationalVector, BigInteger>> ps = P.pairs(
                P.rationalVectors(),
                filter(bi -> !bi.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_int() {
        Iterable<Pair<RationalVector, Integer>> ps = P.pairs(P.rationalVectors(), filter(i -> i != 0, P.integers()));
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoShiftLeft() {
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

    private void demoShiftRight() {
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

//    private void demoSum() {
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

//    private void demoDelta() {
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

    private void demoDot() {
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

    private void demoRightAngleCompare() {
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

    private void demoSquaredLength() {
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("squaredLength(" + v + ") = " + v.squaredLength());
        }
    }

    private void demoCancelDenominators() {
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("cancelDenominators(" + v + ") = " + v.cancelDenominators());
        }
    }

    private void demoPivot() {
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("pivot(" + v + ") = " + v.pivot());
        }
    }

    private void demoReduce() {
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("reduce(" + v + ") = " + v.reduce());
        }
    }

    private void demoEquals_RationalVector() {
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, P.pairs(P.rationalVectors()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            //noinspection ObjectEqualsNull
            System.out.println(v + (v.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println("hashCode(" + v + ") = " + v.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, P.pairs(P.rationalVectors()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_VECTOR_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_VECTOR_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            System.out.println(v);
        }
    }
}
