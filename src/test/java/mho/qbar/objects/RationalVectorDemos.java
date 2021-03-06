package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.RationalVector.*;
import static mho.qbar.objects.RationalVector.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class RationalVectorDemos extends QBarDemos {
    private static final @NotNull String RATIONAL_VECTOR_CHARS = " ,-/0123456789[]";

    public RationalVectorDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoIterator() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            System.out.println("toList(" + v + ") = " + toList(v));
        }
    }

    private void demoOnlyHasIntegralCoordinates() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            System.out.println(v + (v.onlyHasIntegralCoordinates() ? " only has " : " doesn't only have ") +
                    "integral coordinates");
        }
    }

    private void demoToVector() {
        for (RationalVector v : take(LIMIT, map(Vector::toRationalVector, P.withScale(4).vectors()))) {
            System.out.println("toVector(" + v + ") = " + v.toVector());
        }
    }

    private void demoGet() {
        Iterable<Pair<RationalVector, Integer>> ps = P.dependentPairs(
                P.withScale(4).rationalVectorsAtLeast(1),
                v -> P.uniformSample(toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, v.dimension() - 1)))
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            System.out.println("get(" + p.a + ", " + p.b + ") = " + p.a.get(p.b));
        }
    }

    private void demoOf_List_Rational() {
        for (List<Rational> rs : take(LIMIT, P.withScale(4).lists(P.rationals()))) {
            System.out.println("of(" + middle(rs.toString()) + ") = " + of(rs));
        }
    }

    private void demoOf_Rational() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("of(" + r + ") = " + of(r));
        }
    }

    private void demoMaxCoordinateBitLength() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            System.out.println("maxCoordinateBitLength(" + v + ") = " + v.maxCoordinateBitLength());
        }
    }

    private void demoDimension() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            System.out.println("dim(" + v + ") = " + v.dimension());
        }
    }

    private void demoIsZero() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            System.out.println(v + " is " + (v.isZero() ? "" : "not ") + "zero");
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
                                i -> P.pairs(P.withScale(4).rationalVectors(i))
                        )
                )
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private void demoNegate() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
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
                                i -> P.pairs(P.withScale(4).rationalVectors(i))
                        )
                )
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_Rational() {
        Iterable<Pair<RationalVector, Rational>> ps = P.pairs(P.withScale(4).rationalVectors(), P.rationals());
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        Iterable<Pair<RationalVector, BigInteger>> ps = P.pairs(P.withScale(4).rationalVectors(), P.bigIntegers());
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.withScale(4).rationalVectors(), P.integers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoDivide_Rational() {
        Iterable<Pair<RationalVector, Rational>> ps = P.pairs(P.withScale(4).rationalVectors(), P.nonzeroRationals());
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_BigInteger() {
        Iterable<Pair<RationalVector, BigInteger>> ps = P.pairs(
                P.withScale(4).rationalVectors(),
                P.nonzeroBigIntegers()
        );
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_int() {
        Iterable<Pair<RationalVector, Integer>> ps = P.pairs(P.withScale(4).rationalVectors(), P.nonzeroIntegers());
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoShiftLeft() {
        Iterable<Pair<RationalVector, Integer>> ps = P.pairs(P.withScale(4).rationalVectors(), P.integersGeometric());
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoShiftRight() {
        Iterable<Pair<RationalVector, Integer>> ps = P.pairs(P.withScale(4).rationalVectors(), P.integersGeometric());
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    private void demoSum() {
        Iterable<List<RationalVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.withScale(4).lists(p.a, P.withScale(4).rationalVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.withScale(4).positiveIntegersGeometric())
        );
        for (List<RationalVector> vs : take(LIMIT, vss)) {
            System.out.println("Σ(" + middle(vs.toString()) + ") = " + sum(vs));
        }
    }

    private void demoDelta() {
        Iterable<List<RationalVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.withScale(4).lists(p.a, P.withScale(4).rationalVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.withScale(4).positiveIntegersGeometric())
        );
        for (List<RationalVector> vs : take(LIMIT, vss)) {
            System.out.println("Δ(" + middle(vs.toString()) + ") = " + its(delta(vs)));
        }
    }

    private void demoDot() {
        Iterable<Pair<RationalVector, RationalVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).rationalVectors(i))
                        )
                )
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " ⋅ " + p.b + " = " + p.a.dot(p.b));
        }
    }

    private void demoRightAngleCompare() {
        Iterable<Pair<RationalVector, RationalVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).rationalVectors(i))
                        )
                )
        );
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
                    throw new IllegalStateException("unreachable");
            }
            System.out.println(p.a + " and " + p.b + " make " + angleType + " angle");
        }
    }

    private void demoSquaredLength() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            System.out.println("squaredLength(" + v + ") = " + v.squaredLength());
        }
    }

    private void demoCancelDenominators() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            System.out.println("cancelDenominators(" + v + ") = " + v.cancelDenominators());
        }
    }

    private void demoPivot() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            System.out.println("pivot(" + v + ") = " + v.pivot());
        }
    }

    private void demoIsReduced() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            System.out.println(v + " is " + (v.isReduced() ? "" : "not ") + "reduced");
        }
    }

    private void demoReduce() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            System.out.println("reduce(" + v + ") = " + v.reduce());
        }
    }

    private void demoEquals_RationalVector() {
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, P.pairs(P.withScale(4).rationalVectors()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            //noinspection ObjectEqualsNull
            System.out.println(v + (v.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            System.out.println("hashCode(" + v + ") = " + v.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, P.pairs(P.withScale(4).rationalVectors()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b) + " " + p.b);
        }
    }

    private void demoReadStrict() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_VECTOR_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoToString() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            System.out.println(v);
        }
    }
}
