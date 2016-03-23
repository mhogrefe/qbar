package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.RationalPolynomialVector.*;
import static mho.qbar.objects.RationalPolynomialVector.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class RationalPolynomialVectorDemos extends QBarDemos {
    private static final @NotNull String RATIONAL_POLYNOMIAL_VECTOR_CHARS = " *+,-/0123456789[]^x";

    public RationalPolynomialVectorDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoIterator() {
        Iterable<RationalPolynomialVector> vs = P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors();
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
            System.out.println("toList(" + v + ") = " + toList(v));
        }
    }

    private void demoOnlyHasIntegralCoordinates() {
        Iterable<RationalPolynomialVector> vs = P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors();
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
            System.out.println(v + (v.onlyHasIntegralCoordinates() ? " only has " : " doesn't only have ") +
                    "integral coordinates");
        }
    }

    private void demoToPolynomialVector() {
        Iterable<RationalPolynomialVector> vs = map(
                PolynomialVector::toRationalPolynomialVector,
                P.withScale(4).withSecondaryScale(4).polynomialVectors()
        );
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
            System.out.println("toPolynomialVector(" + v + ") = " + v.toPolynomialVector());
        }
    }

    private void demoGet() {
        Iterable<Pair<RationalPolynomialVector, Integer>> ps = P.dependentPairs(
                P.withScale(4).withSecondaryScale(4).rationalPolynomialVectorsAtLeast(1),
                v -> P.uniformSample(toList(range(0, v.dimension() - 1)))
        );
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            System.out.println("get(" + p.a + ", " + p.b + ") = " + p.a.get(p.b));
        }
    }

    private void demoOf_List_RationalPolynomial() {
        for (List<RationalPolynomial> rs : take(LIMIT, P.withScale(4).lists(P.withScale(4).rationalPolynomials()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("of(" + listString + ") = " + of(rs));
        }
    }

    private void demoOf_RationalPolynomial() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println("of(" + p + ") = " + of(p));
        }
    }

    private void demoOf_RationalVector() {
        for (RationalVector v : take(LIMIT, P.withScale(4).rationalVectors())) {
            System.out.println("of(" + v + ") = " + of(v));
        }
    }

    private void demoMaxCoordinateBitLength() {
        Iterable<RationalPolynomialVector> vs = P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors();
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
            System.out.println("maxCoordinateBitLength(" + v + ") = " + v.maxCoordinateBitLength());
        }
    }

    private void demoDimension() {
        Iterable<RationalPolynomialVector> vs = P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors();
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
            System.out.println("dim(" + v + ") = " + v.dimension());
        }
    }

    private void demoIsZero() {
        Iterable<RationalPolynomialVector> vs = P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors();
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
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
        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).rationalPolynomialVectors(i))
                        )
                )
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private void demoNegate() {
        for (RationalPolynomialVector v : take(LIMIT, P.withScale(4).rationalPolynomialVectors())) {
            System.out.println("-" + v + " = " + v.negate());
        }
    }

    private void demoSubtract() {
        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).rationalPolynomialVectors(i))
                        )
                )
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_RationalPolynomial() {
        Iterable<Pair<RationalPolynomialVector, RationalPolynomial>> ps = P.pairs(
                P.withScale(4).rationalPolynomialVectors(),
                P.withScale(4).rationalPolynomials()
        );
        for (Pair<RationalPolynomialVector, RationalPolynomial> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Rational() {
        Iterable<Pair<RationalPolynomialVector, Rational>> ps = P.pairs(
                P.withScale(4).rationalPolynomialVectors(),
                P.rationals()
        );
        for (Pair<RationalPolynomialVector, Rational> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        Iterable<Pair<RationalPolynomialVector, BigInteger>> ps = P.pairs(
                P.withScale(4).rationalPolynomialVectors(),
                P.bigIntegers()
        );
        for (Pair<RationalPolynomialVector, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        Iterable<Pair<RationalPolynomialVector, Integer>> ps = P.pairs(
                P.withScale(4).rationalPolynomialVectors(),
                P.integers()
        );
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoDivide_Rational() {
        Iterable<Pair<RationalPolynomialVector, Rational>> ps = P.pairs(
                P.withScale(4).rationalPolynomialVectors(),
                P.nonzeroRationals()
        );
        for (Pair<RationalPolynomialVector, Rational> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_BigInteger() {
        Iterable<Pair<RationalPolynomialVector, BigInteger>> ps = P.pairs(
                P.withScale(4).rationalPolynomialVectors(),
                P.nonzeroBigIntegers()
        );
        for (Pair<RationalPolynomialVector, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_int() {
        Iterable<Pair<RationalPolynomialVector, Integer>> ps = P.pairs(
                P.withScale(4).rationalPolynomialVectors(),
                P.nonzeroIntegers()
        );
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoShiftLeft() {
        Iterable<Pair<RationalPolynomialVector, Integer>> ps = P.pairs(
                P.withScale(4).rationalPolynomialVectors(),
                P.integersGeometric()
        );
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoShiftRight() {
        Iterable<Pair<RationalPolynomialVector, Integer>> ps = P.pairs(
                P.withScale(4).rationalPolynomialVectors(),
                P.integersGeometric()
        );
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    private void demoSum() {
        Iterable<List<RationalPolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.withScale(4).lists(
                                        p.a,
                                        P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors(p.b)
                                )
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.withScale(4).positiveIntegersGeometric())
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vss)) {
            String listString = tail(init(vs.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(vs));
        }
    }

    private void demoDelta() {
        Iterable<List<RationalPolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.withScale(4).lists(
                                        p.a,
                                        P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors(p.b)
                                )
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.withScale(4).positiveIntegersGeometric())
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vss)) {
            String listString = tail(init(vs.toString()));
            System.out.println("Δ(" + listString + ") = " + its(delta(vs)));
        }
    }

    private void demoDot() {
        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).rationalPolynomialVectors(i))
                        )
                )
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " ⋅ " + p.b + " = " + p.a.dot(p.b));
        }
    }

    private void demoSquaredLength() {
        for (RationalPolynomialVector v : take(LIMIT, P.withScale(4).rationalPolynomialVectors())) {
            System.out.println("squaredLength(" + v + ") = " + v.squaredLength());
        }
    }

    private void demoEquals_RationalPolynomialVector() {
        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors()
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        Iterable<RationalPolynomialVector> vs = P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors();
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
            //noinspection ObjectEqualsNull
            System.out.println(v + (v.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        Iterable<RationalPolynomialVector> vs = P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors();
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
            System.out.println("hashCode(" + v + ") = " + v.hashCode());
        }
    }

    private void demoCompareTo() {
        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors()
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + nicePrint(s) + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_POLYNOMIAL_VECTOR_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + nicePrint(s) + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_POLYNOMIAL_VECTOR_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        Iterable<RationalPolynomialVector> vs = P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors();
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
            System.out.println(v);
        }
    }
}
