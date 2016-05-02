package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

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
        Iterable<Pair<MultivariatePolynomial, Monomial>> ps = P.pairs(
                P.withScale(4).multivariatePolynomials(),
                P.withScale(4).monomials()
        );
        for (Pair<MultivariatePolynomial, Monomial> p : take(LIMIT, ps)) {
            System.out.println("coefficient(" + p.a + ", " + p.b + ") = " + p.a.coefficient(p.b));
        }
    }

    private void demoOf_List_Pair_Monomial_BigInteger() {
        Iterable<List<Pair<Monomial, BigInteger>>> pss = P.withScale(4).lists(
                P.pairs(P.withScale(4).monomials(), P.bigIntegers())
        );
        for (List<Pair<Monomial, BigInteger>> ps : take(LIMIT, pss)) {
            String listString = tail(init(ps.toString()));
            System.out.println("of(" + listString + ") = " + of(ps));
        }
    }

    private void demoOf_Monomial_BigInteger() {
        Iterable<Pair<Monomial, BigInteger>> ps = P.pairs(P.withScale(4).monomials(), P.bigIntegers());
        for (Pair<Monomial, BigInteger> p : take(LIMIT, ps)) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private void demoOf_BigInteger() {
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoOf_int() {
        for (int i : take(LIMIT, P.integers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoOf_Polynomial_Variable() {
        for (Pair<Polynomial, Variable> p : take(LIMIT, P.pairs(P.withScale(4).polynomials(), P.variables()))) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private void demoToPolynomial() {
        Iterable<MultivariatePolynomial> ps = P.withElement(
                ZERO,
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.optionals(P.variables()),
                                v -> v.isPresent() ?
                                        filterInfinite(
                                                q -> q.degree() > 0,
                                                P.withScale(4).multivariatePolynomials(
                                                        Collections.singletonList(v.get())
                                                )
                                        ) :
                                        map(q -> of(q, Variable.of(0)), P.withScale(4).polynomials(0))
                        )
                )
        );
        for (MultivariatePolynomial p : take(LIMIT, ps)) {
            System.out.println("toPolynomial(" + p + ") = " + p.toPolynomial());
        }
    }

    private void demoVariables() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println("variables(" + p + ") = " + p.variables());
        }
    }

    private void demoVariableCount() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println("variableCount(" + p + ") = " + p.variableCount());
        }
    }

    private void demoTermCount() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println("termCount(" + p + ") = " + p.termCount());
        }
    }

    private void demoMaxCoefficientBitLength() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println("maxCoefficientBitLength(" + p + ") = " + p.maxCoefficientBitLength());
        }
    }

    private void demoDegree() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println("degree(" + p + ") = " + p.degree());
        }
    }

    private void demoCoefficientsOfVariable() {
        Iterable<Pair<MultivariatePolynomial, Variable>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).multivariatePolynomials(),
                P.withScale(4).variables()
        );
        for (Pair<MultivariatePolynomial, Variable> p : take(LIMIT, ps)) {
            System.out.println("coefficientsOfVariable(" + p.a + ", " + p.b + ") = " +
                    p.a.coefficientsOfVariable(p.b));
        }
    }

    private void demoAdd() {
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(
                P.withScale(4).multivariatePolynomials()
        );
        for (Pair<MultivariatePolynomial, MultivariatePolynomial> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") + (" + p.b + ") = " + p.a.add(p.b));
        }
    }

    private void demoNegate() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println("-(" + p + ") = " + p.negate());
        }
    }

    private void demoSubtract() {
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(
                P.withScale(4).multivariatePolynomials()
        );
        for (Pair<MultivariatePolynomial, MultivariatePolynomial> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") - (" + p.b + ") = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_int() {
        Iterable<Pair<MultivariatePolynomial, Integer>> ps = P.pairs(
                P.withScale(4).multivariatePolynomials(),
                P.integers()
        );
        for (Pair<MultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        Iterable<Pair<MultivariatePolynomial, BigInteger>> ps = P.pairs(
                P.withScale(4).multivariatePolynomials(),
                P.bigIntegers()
        );
        for (Pair<MultivariatePolynomial, BigInteger> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Monomial_BigInteger() {
        Iterable<Triple<MultivariatePolynomial, Monomial, BigInteger>> ts = P.triples(
                P.withScale(4).multivariatePolynomials(),
                P.withScale(4).monomials(),
                P.bigIntegers()
        );
        for (Triple<MultivariatePolynomial, Monomial, BigInteger> t : take(LIMIT, ts)) {
            System.out.println("(" + t.a + ") * (" + t.b + ") * " + t.c + " = " + t.a.multiply(t.b, t.c));
        }
    }

    private void demoMultiply_MultivariatePolynomial() {
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(
                P.withScale(4).multivariatePolynomials()
        );
        for (Pair<MultivariatePolynomial, MultivariatePolynomial> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") * (" + p.b + ") = " + p.a.multiply(p.b));
        }
    }

    private void demoShiftLeft() {
        Iterable<Pair<MultivariatePolynomial, Integer>> ps = P.pairs(
                P.withScale(4).multivariatePolynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<MultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoResultant() {
        Iterable<Triple<MultivariatePolynomial, MultivariatePolynomial, Variable>> ts;
        if (P instanceof QBarExhaustiveProvider) {
            ts = nub(
                    map(
                            q -> new Triple<>(q.b.a, q.b.b, q.a.a),
                            P.dependentPairsInfiniteLogarithmicOrder(
                                    P.subsetPairs(P.variables()),
                                    p -> P.pairs(
                                            filterInfinite(r -> r != ZERO, P.multivariatePolynomials(Pair.toList(p)))
                                    )
                            )
                    )
            );
        } else {
            ts = map(
                    q -> new Triple<>(q.b.a, q.b.b, q.a.a),
                    P.dependentPairsInfinite(
                            P.subsetPairs(P.withScale(4).variables()),
                            p -> P.pairs(
                                    filterInfinite(
                                            r -> r != ZERO,
                                            P.withScale(4).withSecondaryScale(4)
                                                    .multivariatePolynomials(Pair.toList(p))
                                    )
                            )
                    )
            );
        }
        for (Triple<MultivariatePolynomial, MultivariatePolynomial, Variable> t : take(LIMIT, ts)) {
            System.out.println("resultant(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.resultant(t.b, t.c));
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

    private void demoReadStrict() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_targeted() {
        for (String s : take(LIMIT, P.strings(MULTIVARIATE_POLYNOMIAL_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoToString() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println(p);
        }
    }
}
