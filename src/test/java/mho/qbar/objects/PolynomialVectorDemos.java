package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mho.qbar.objects.PolynomialVector.*;
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
                v -> P.uniformSample(toList(range(0, v.dimension() - 1)))
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

    private void demoRead() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + nicePrint(s) + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        for (String s : take(LIMIT, P.strings(POLYNOMIAL_VECTOR_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + nicePrint(s) + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        for (String s : take(LIMIT, P.strings(POLYNOMIAL_VECTOR_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        for (PolynomialVector v : take(LIMIT, P.withScale(4).withSecondaryScale(4).polynomialVectors())) {
            System.out.println(v);
        }
    }
}
