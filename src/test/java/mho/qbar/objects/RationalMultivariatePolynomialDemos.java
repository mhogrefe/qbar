package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static mho.qbar.objects.RationalMultivariatePolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.MEDIUM_LIMIT;

@SuppressWarnings("UnusedDeclaration")
public class RationalMultivariatePolynomialDemos  extends QBarDemos {
    private static final @NotNull String RATIONAL_MULTIVARIATE_POLYNOMIAL_CHARS =
            "*+-/0123456789^abcdefghijklmnopqrstuvwxyz";

    public RationalMultivariatePolynomialDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoIterable() {
        Iterable<Pair<RationalMultivariatePolynomial, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.monomialOrders()
        );
        for (Pair<RationalMultivariatePolynomial, MonomialOrder> p : take(LIMIT, ps)) {
            System.out.println("iterable(" + p.a + ", " + p.b + ") = " + toList(p.a.iterable(p.b)));
        }
    }

    private void demoIterator() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.withScale(4).rationalMultivariatePolynomials())) {
            System.out.println("toList(" + p + ") = " + toList(p));
        }
    }

    private void demoOnlyHasIntegralCoefficients() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.withScale(4).rationalMultivariatePolynomials())) {
            System.out.println(p + (p.onlyHasIntegralCoefficients() ? " only has " : " doesn't only have ") +
                    "integral coefficients");
        }
    }

    private void demoToMultivariatePolynomial() {
        Iterable<RationalMultivariatePolynomial> ps = map(
                MultivariatePolynomial::toRationalMultivariatePolynomial,
                P.withScale(4).multivariatePolynomials()
        );
        for (RationalMultivariatePolynomial p : take(LIMIT, ps)) {
            System.out.println("toRationalMultivariatePolynomial(" + p + ") = " +
                    p.toMultivariatePolynomial());
        }
    }

    private void demoCoefficient() {
        Iterable<Pair<RationalMultivariatePolynomial, Monomial>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.withScale(4).monomials()
        );
        for (Pair<RationalMultivariatePolynomial, Monomial> p : take(LIMIT, ps)) {
            System.out.println("coefficient(" + p.a + ", " + p.b + ") = " + p.a.coefficient(p.b));
        }
    }

    private void demoOf_List_Pair_Monomial_Rational() {
        Iterable<List<Pair<Monomial, Rational>>> pss = P.withScale(4).lists(
                P.pairs(P.withScale(4).monomials(), P.rationals())
        );
        for (List<Pair<Monomial, Rational>> ps : take(LIMIT, pss)) {
            System.out.println("of(" + middle(ps.toString()) + ") = " + of(ps));
        }
    }

    private void demoOf_Monomial_Rational() {
        Iterable<Pair<Monomial, Rational>> ps = P.pairs(P.withScale(4).monomials(), P.rationals());
        for (Pair<Monomial, Rational> p : take(LIMIT, ps)) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private void demoOf_Rational() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("of(" + r + ") = " + of(r));
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

    private void demoOf_Variable() {
        for (Variable v : take(MEDIUM_LIMIT, P.variables())) {
            System.out.println("of(" + v + ") = " + of(v));
        }
    }

    private void demoOf_RationalPolynomial_Variable() {
        Iterable<Pair<RationalPolynomial, Variable>> ps = P.pairs(P.withScale(4).rationalPolynomials(), P.variables());
        for (Pair<RationalPolynomial, Variable> p : take(LIMIT, ps)) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private void demoToRationalPolynomial() {
        Iterable<RationalMultivariatePolynomial> ps = P.withElement(
                ZERO,
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.optionals(P.variables()),
                                v -> v.isPresent() ?
                                        filterInfinite(
                                                q -> q.degree() > 0,
                                                P.withScale(4).rationalMultivariatePolynomials(
                                                        Collections.singletonList(v.get())
                                                )
                                        ) :
                                        map(q -> of(q, Variable.of(0)), P.withScale(4).rationalPolynomials(0))
                        )
                )
        );
        for (RationalMultivariatePolynomial p : take(LIMIT, ps)) {
            System.out.println("toRationalPolynomial(" + p + ") = " + p.toRationalPolynomial());
        }
    }

    private void demoVariables() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.withScale(4).rationalMultivariatePolynomials())) {
            System.out.println("variables(" + p + ") = " + p.variables());
        }
    }

    private void demoVariableCount() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.withScale(4).rationalMultivariatePolynomials())) {
            System.out.println("variableCount(" + p + ") = " + p.variableCount());
        }
    }

    private void demoTermCount() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.withScale(4).rationalMultivariatePolynomials())) {
            System.out.println("termCount(" + p + ") = " + p.termCount());
        }
    }

    private void demoMaxCoefficientBitLength() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.withScale(4).rationalMultivariatePolynomials())) {
            System.out.println("maxCoefficientBitLength(" + p + ") = " + p.maxCoefficientBitLength());
        }
    }

    private void demoDegree() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.withScale(4).rationalMultivariatePolynomials())) {
            System.out.println("degree(" + p + ") = " + p.degree());
        }
    }

    private void demoDegree_Variable() {
        Iterable<Pair<RationalMultivariatePolynomial, Variable>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.withScale(4).variables()
        );
        for (Pair<RationalMultivariatePolynomial, Variable> p : take(LIMIT, ps)) {
            System.out.println("degree(" + p.a + ", " + p.b + ") = " + p.a.degree(p.b));
        }
    }

    private void demoIsHomogeneous() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.withScale(4).rationalMultivariatePolynomials())) {
            System.out.println(p + " is " + (p.isHomogeneous() ? "" : "not ") + "homogeneous");
        }
    }

    private void demoCoefficientsOfVariable() {
        Iterable<Pair<RationalMultivariatePolynomial, Variable>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.withScale(4).variables()
        );
        for (Pair<RationalMultivariatePolynomial, Variable> p : take(LIMIT, ps)) {
            System.out.println("coefficientsOfVariable(" + p.a + ", " + p.b + ") = " +
                    p.a.coefficientsOfVariable(p.b));
        }
    }

    private void demoGroupVariables_List_Variable_MonomialOrder() {
        Iterable<Triple<RationalMultivariatePolynomial, List<Variable>, MonomialOrder>> ts = P.triples(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.withScale(4).lists(P.withScale(4).variables()),
                P.monomialOrders()
        );
        for (Triple<RationalMultivariatePolynomial, List<Variable>, MonomialOrder> t : take(LIMIT, ts)) {
            System.out.println("groupVariables(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.groupVariables(t.b, t.c));
        }
    }

    private void demoGroupVariables_List_Variable() {
        Iterable<Pair<RationalMultivariatePolynomial, List<Variable>>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.withScale(4).lists(P.withScale(4).variables())
        );
        for (Pair<RationalMultivariatePolynomial, List<Variable>> p : take(LIMIT, ps)) {
            System.out.println("groupVariables(" + p.a + ", " + p.b + ") = " + p.a.groupVariables(p.b));
        }
    }

    private void demoToString() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.withScale(4).rationalMultivariatePolynomials())) {
            System.out.println(p);
        }
    }
}
