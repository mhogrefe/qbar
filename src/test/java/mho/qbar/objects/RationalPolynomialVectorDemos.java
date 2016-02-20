package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mho.qbar.objects.RationalPolynomialVector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.MEDIUM_LIMIT;
import static mho.wheels.testing.Testing.SMALL_LIMIT;
import static mho.wheels.testing.Testing.nicePrint;

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

    private void demoHasIntegralCoordinates() {
        Iterable<RationalPolynomialVector> vs = P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors();
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
            System.out.println(v + (v.hasIntegralCoordinates() ? " has " : " doesn't have ") + "integral coordinates");
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
