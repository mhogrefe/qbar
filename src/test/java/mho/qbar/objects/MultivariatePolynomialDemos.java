package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import static mho.qbar.objects.MultivariatePolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.nicePrint;

@SuppressWarnings("UnusedDeclaration")
public class MultivariatePolynomialDemos extends QBarDemos {
    private static final @NotNull String MULTIVARIATE_POLYNOMIAL_CHARS = "*+-0123456789^abcdefghijklmnopqrstuvwxyz";

    public MultivariatePolynomialDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoIterator() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println("toList(" + p + ") = " + toList(p));
        }
    }

    private void demoCoefficient() {
        Iterable<Pair<MultivariatePolynomial, ExponentVector>> ps = P.pairs(
                P.withScale(4).multivariatePolynomials(),
                P.withScale(4).exponentVectors()
        );
        for (Pair<MultivariatePolynomial, ExponentVector> p : take(LIMIT, ps)) {
            System.out.println("coefficient(" + p.a + ", " + p.b + ") = " + p.a.coefficient(p.b));
        }
    }

    private void demoEquals_MultivariatePolynomial() {
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(
                P.withScale(4).multivariatePolynomials()
        );
        for (Pair<MultivariatePolynomial, MultivariatePolynomial> p : take(LIMIT, ps)) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            //noinspection ObjectEqualsNull
            System.out.println(p + (p.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println("hashCode(" + p + ") = " + p.hashCode());
        }
    }

    private void demoCompareTo() {
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(
                P.withScale(4).multivariatePolynomials()
        );
        for (Pair<MultivariatePolynomial, MultivariatePolynomial> p : take(LIMIT, ps)) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + nicePrint(s) + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        for (String s : take(LIMIT, P.strings(MULTIVARIATE_POLYNOMIAL_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + nicePrint(s) + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        for (String s : take(LIMIT, P.strings(MULTIVARIATE_POLYNOMIAL_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println(p);
        }
    }
}
