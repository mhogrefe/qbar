package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.Vector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class VectorDemos extends QBarDemos {
    private static final @NotNull String VECTOR_CHARS = " ,-0123456789[]";

    public VectorDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoIterator() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            System.out.println("toList(" + v + ") = " + toList(v));
        }
    }

    private void demoToRationalVector() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            System.out.println("toRationalVector(" + v + ") = " + v.toRationalVector());
        }
    }

    private void demoGet() {
        Iterable<Pair<Vector, Integer>> ps = P.dependentPairs(
                P.withScale(4).vectorsAtLeast(1),
                v -> P.uniformSample(toList(range(0, v.dimension() - 1)))
        );
        for (Pair<Vector, Integer> p : take(LIMIT, ps)) {
            System.out.println("get(" + p.a + ", " + p.b + ") = " + p.a.get(p.b));
        }
    }

    private void demoOf_List_BigInteger() {
        for (List<BigInteger> is : take(LIMIT, P.withScale(4).lists(P.bigIntegers()))) {
            String listString = tail(init(is.toString()));
            System.out.println("of(" + listString + ") = " + of(is));
        }
    }

    private void demoOf_BigInteger() {
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoMaxCoordinateBitLength() {
        for (Vector v : take(LIMIT, P.vectors())) {
            System.out.println("maxCoordinateBitLength(" + v + ") = " + v.maxCoordinateBitLength());
        }
    }

    private void demoDimension() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            System.out.println("dim(" + v + ") = " + v.dimension());
        }
    }

    private void demoIsZero() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
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
        Iterable<Pair<Vector, Vector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).vectors(i))
                        )
                )
        );
        for (Pair<Vector, Vector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private void demoNegate() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            System.out.println("-" + v + " = " + v.negate());
        }
    }

    private void demoSubtract() {
        Iterable<Pair<Vector, Vector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).vectors(i))
                        )
                )
        );
        for (Pair<Vector, Vector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        Iterable<Pair<Vector, BigInteger>> ps = P.pairs(P.withScale(4).vectors(), P.bigIntegers());
        for (Pair<Vector, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        for (Pair<Vector, Integer> p : take(LIMIT, P.pairs(P.withScale(4).vectors(), P.integers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoShiftLeft() {
        Iterable<Pair<Vector, Integer>> ps = P.pairs(P.withScale(4).vectors(), P.naturalIntegersGeometric());
        for (Pair<Vector, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoSum() {
        Iterable<List<Vector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.withScale(4).lists(p.a, P.withScale(4).vectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.withScale(4).positiveIntegersGeometric())
        );
        for (List<Vector> vs : take(LIMIT, vss)) {
            String listString = tail(init(vs.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(vs));
        }
    }

    private void demoDelta() {
        Iterable<List<Vector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.withScale(4).lists(p.a, P.withScale(4).vectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.withScale(4).positiveIntegersGeometric())
        );
        for (List<Vector> vs : take(LIMIT, vss)) {
            String listString = tail(init(vs.toString()));
            System.out.println("Δ(" + listString + ") = " + its(delta(vs)));
        }
    }

    private void demoDot() {
        Iterable<Pair<Vector, Vector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).vectors(i))
                        )
                )
        );
        for (Pair<Vector, Vector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " ⋅ " + p.b + " = " + p.a.dot(p.b));
        }
    }

    private void demoRightAngleCompare() {
        Iterable<Pair<Vector, Vector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).vectors(i))
                        )
                )
        );
        for (Pair<Vector, Vector> p : take(LIMIT, ps)) {
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
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            System.out.println("squaredLength(" + v + ") = " + v.squaredLength());
        }
    }

    private void demoPivot() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            System.out.println("pivot(" + v + ") = " + v.pivot());
        }
    }

    private void demoIsReduced() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            System.out.println(v + " is " + (v.isReduced() ? "" : "not ") + "reduced");
        }
    }

    private void demoEquals_Vector() {
        for (Pair<Vector, Vector> p : take(LIMIT, P.pairs(P.withScale(4).vectors()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            //noinspection ObjectEqualsNull
            System.out.println(v + (v.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            System.out.println("hashCode(" + v + ") = " + v.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<Vector, Vector> p : take(LIMIT, P.pairs(P.withScale(4).vectors()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + nicePrint(s) + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        for (String s : take(LIMIT, P.strings(VECTOR_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + nicePrint(s) + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        for (String s : take(LIMIT, P.strings(VECTOR_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        for (Vector v : take(LIMIT, P.vectors())) {
            System.out.println(v);
        }
    }
}
