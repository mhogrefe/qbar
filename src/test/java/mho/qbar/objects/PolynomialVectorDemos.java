package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.PolynomialVector.*;
import static mho.qbar.objects.PolynomialVector.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class PolynomialVectorDemos extends QBarDemos {
    private static final @NotNull String POLYNOMIAL_VECTOR_CHARS = " *+,-0123456789[]^x";

    public PolynomialVectorDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoIterator() {
        for (PolynomialVector v : take(LIMIT, P.withScale(4).withSecondaryScale(4).polynomialVectors())) {
            System.out.println("toList(" + v + ") = " + toList(v));
        }
    }

    private void demoToRationalPolynomialVector() {
        for (PolynomialVector v : take(LIMIT, P.withScale(4).withSecondaryScale(4).polynomialVectors())) {
            System.out.println("toRationalPolynomialVector(" + v + ") = " + v.toRationalPolynomialVector());
        }
    }

    private void demoGet() {
        Iterable<Pair<PolynomialVector, Integer>> ps = P.dependentPairs(
                P.withScale(4).withSecondaryScale(4).polynomialVectorsAtLeast(1),
                v -> P.uniformSample(toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, v.dimension() - 1)))
        );
        for (Pair<PolynomialVector, Integer> p : take(LIMIT, ps)) {
            System.out.println("get(" + p.a + ", " + p.b + ") = " + p.a.get(p.b));
        }
    }

    private void demoOf_List_Polynomial() {
        for (List<Polynomial> is : take(LIMIT, P.withScale(4).lists(P.withScale(4).polynomials()))) {
            String listString = tail(init(is.toString()));
            System.out.println("of(" + listString + ") = " + of(is));
        }
    }

    private void demoOf_Polynomial() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("of(" + p + ") = " + of(p));
        }
    }

    private void demoOf_Vector() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            System.out.println("of(" + v + ") = " + of(v));
        }
    }

    private void demoMaxCoordinateBitLength() {
        for (PolynomialVector v : take(LIMIT, P.withScale(4).withSecondaryScale(4).polynomialVectors())) {
            System.out.println("maxCoordinateBitLength(" + v + ") = " + v.maxCoordinateBitLength());
        }
    }

    private void demoDimension() {
        for (PolynomialVector v : take(LIMIT, P.withScale(4).withSecondaryScale(4).polynomialVectors())) {
            System.out.println("dim(" + v + ") = " + v.dimension());
        }
    }

    private void demoIsZero() {
        for (PolynomialVector v : take(LIMIT, P.withScale(4).withSecondaryScale(4).polynomialVectors())) {
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
        Iterable<Pair<PolynomialVector, PolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).polynomialVectors(i))
                        )
                )
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private void demoNegate() {
        for (PolynomialVector v : take(LIMIT, P.withScale(4).polynomialVectors())) {
            System.out.println("-" + v + " = " + v.negate());
        }
    }

    private void demoSubtract() {
        Iterable<Pair<PolynomialVector, PolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).polynomialVectors(i))
                        )
                )
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_Polynomial() {
        Iterable<Pair<PolynomialVector, Polynomial>> ps = P.pairs(
                P.withScale(4).polynomialVectors(),
                P.withScale(4).polynomials()
        );
        for (Pair<PolynomialVector, Polynomial> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        Iterable<Pair<PolynomialVector, BigInteger>> ps = P.pairs(P.withScale(4).polynomialVectors(), P.bigIntegers());
        for (Pair<PolynomialVector, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        Iterable<Pair<PolynomialVector, Integer>> ps = P.pairs(P.withScale(4).polynomialVectors(), P.integers());
        for (Pair<PolynomialVector, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoShiftLeft() {
        Iterable<Pair<PolynomialVector, Integer>> ps = P.pairs(
                P.withScale(4).polynomialVectors(),
                P.naturalIntegersGeometric()
        );
        for (Pair<PolynomialVector, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoSum() {
        Iterable<List<PolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.withScale(4).lists(
                                        p.a,
                                        P.withScale(4).withSecondaryScale(4).polynomialVectors(p.b)
                                )
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.withScale(4).positiveIntegersGeometric())
        );
        for (List<PolynomialVector> vs : take(LIMIT, vss)) {
            String listString = tail(init(vs.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(vs));
        }
    }

    private void demoDelta() {
        Iterable<List<PolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.withScale(4).lists(
                                        p.a,
                                        P.withScale(4).withSecondaryScale(4).polynomialVectors(p.b)
                                )
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.withScale(4).positiveIntegersGeometric())
        );
        for (List<PolynomialVector> vs : take(LIMIT, vss)) {
            String listString = tail(init(vs.toString()));
            System.out.println("Δ(" + listString + ") = " + its(delta(vs)));
        }
    }

    private void demoDot() {
        Iterable<Pair<PolynomialVector, PolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).polynomialVectors(i))
                        )
                )
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " ⋅ " + p.b + " = " + p.a.dot(p.b));
        }
    }

    private void demoSquaredLength() {
        for (PolynomialVector v : take(LIMIT, P.withScale(4).polynomialVectors())) {
            System.out.println("squaredLength(" + v + ") = " + v.squaredLength());
        }
    }

    private void demoEquals_PolynomialVector() {
        Iterable<Pair<PolynomialVector, PolynomialVector>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(4).polynomialVectors()
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (PolynomialVector v : take(LIMIT, P.withScale(4).withSecondaryScale(4).polynomialVectors())) {
            //noinspection ObjectEqualsNull
            System.out.println(v + (v.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (PolynomialVector v : take(LIMIT, P.withScale(4).withSecondaryScale(4).polynomialVectors())) {
            System.out.println("hashCode(" + v + ") = " + v.hashCode());
        }
    }

    private void demoCompareTo() {
        Iterable<Pair<PolynomialVector, PolynomialVector>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(4).polynomialVectors()
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoReadStrict() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_targeted() {
        for (String s : take(LIMIT, P.strings(POLYNOMIAL_VECTOR_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoToString() {
        for (PolynomialVector v : take(LIMIT, P.withScale(4).withSecondaryScale(4).polynomialVectors())) {
            System.out.println(v);
        }
    }
}
