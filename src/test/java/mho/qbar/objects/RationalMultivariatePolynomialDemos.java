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
import java.util.Map;
import java.util.TreeMap;

import static mho.qbar.objects.RationalMultivariatePolynomial.*;
import static mho.qbar.objects.RationalMultivariatePolynomial.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

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

    private void demoLeadingTerm_MonomialOrder() {
        Iterable<Pair<RationalMultivariatePolynomial, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.monomialOrders()
        );
        for (Pair<RationalMultivariatePolynomial, MonomialOrder> p : take(LIMIT, ps)) {
            System.out.println("LT(" + p.a + ", " + p.b + ") = " + p.a.leadingTerm(p.b));
        }
    }

    private void demoLeadingTerm() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            System.out.println("LT(" + p + ") = " + p.leadingTerm());
        }
    }

    private void demoLeadingCoefficient_MonomialOrder() {
        Iterable<Pair<RationalMultivariatePolynomial, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.monomialOrders()
        );
        for (Pair<RationalMultivariatePolynomial, MonomialOrder> p : take(LIMIT, ps)) {
            System.out.println("LC(" + p.a + ", " + p.b + ") = " + p.a.leadingCoefficient(p.b));
        }
    }

    private void demoLeadingCoefficient() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            System.out.println("LC(" + p + ") = " + p.leadingCoefficient());
        }
    }

    private void demoLeadingMonomial_MonomialOrder() {
        Iterable<Pair<RationalMultivariatePolynomial, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.monomialOrders()
        );
        for (Pair<RationalMultivariatePolynomial, MonomialOrder> p : take(LIMIT, ps)) {
            System.out.println("LM(" + p.a + ", " + p.b + ") = " + p.a.leadingMonomial(p.b));
        }
    }

    private void demoLeadingMonomial() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            System.out.println("LM(" + p + ") = " + p.leadingMonomial());
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

    private void demoAdd() {
        Iterable<Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials()
        );
        for (Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") + (" + p.b + ") = " + p.a.add(p.b));
        }
    }

    private void demoNegate() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.withScale(4).rationalMultivariatePolynomials())) {
            System.out.println("-(" + p + ") = " + p.negate());
        }
    }

    private void demoSubtract() {
        Iterable<Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials()
        );
        for (Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") - (" + p.b + ") = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_int() {
        Iterable<Pair<RationalMultivariatePolynomial, Integer>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.integers()
        );
        for (Pair<RationalMultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        Iterable<Pair<RationalMultivariatePolynomial, BigInteger>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.bigIntegers()
        );
        for (Pair<RationalMultivariatePolynomial, BigInteger> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Rational() {
        Iterable<Pair<RationalMultivariatePolynomial, Rational>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.rationals()
        );
        for (Pair<RationalMultivariatePolynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Monomial_Rational() {
        Iterable<Triple<RationalMultivariatePolynomial, Monomial, Rational>> ts = P.triples(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.withScale(4).monomials(),
                P.rationals()
        );
        for (Triple<RationalMultivariatePolynomial, Monomial, Rational> t : take(LIMIT, ts)) {
            System.out.println("(" + t.a + ") * (" + t.b + ") * " + t.c + " = " + t.a.multiply(t.b, t.c));
        }
    }

    private void demoMultiply_RationalMultivariatePolynomial() {
        Iterable<Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials()
        );
        for (Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") * (" + p.b + ") = " + p.a.multiply(p.b));
        }
    }

    private void demoDivide_int() {
        Iterable<Pair<RationalMultivariatePolynomial, Integer>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.nonzeroIntegers()
        );
        for (Pair<RationalMultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_BigInteger() {
        Iterable<Pair<RationalMultivariatePolynomial, BigInteger>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.nonzeroBigIntegers()
        );
        for (Pair<RationalMultivariatePolynomial, BigInteger> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_Rational() {
        Iterable<Pair<RationalMultivariatePolynomial, Rational>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.nonzeroRationals()
        );
        for (Pair<RationalMultivariatePolynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoShiftLeft() {
        Iterable<Pair<RationalMultivariatePolynomial, Integer>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.withScale(4).integersGeometric()
        );
        for (Pair<RationalMultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoShiftRight() {
        Iterable<Pair<RationalMultivariatePolynomial, Integer>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.withScale(4).integersGeometric()
        );
        for (Pair<RationalMultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    private void demoSum() {
        Iterable<List<RationalMultivariatePolynomial>> pss = P.withScale(4).lists(
                P.withScale(4).rationalMultivariatePolynomials()
        );
        for (List<RationalMultivariatePolynomial> ps : take(LIMIT, pss)) {
            System.out.println("Σ(" + middle(ps.toString()) + ") = " + sum(ps));
        }
    }

    private void demoProduct() {
        Iterable<List<RationalMultivariatePolynomial>> pss = P.withScale(4).lists(
                P.withScale(4).rationalMultivariatePolynomials()
        );
        for (List<RationalMultivariatePolynomial> ps : take(LIMIT, pss)) {
            System.out.println("Π(" + middle(ps.toString()) + ") = " + product(ps));
        }
    }

    private void demoDelta() {
        Iterable<List<RationalMultivariatePolynomial>> pss =
                P.withScale(4).listsAtLeast(1, P.withScale(4).rationalMultivariatePolynomials());
        for (List<RationalMultivariatePolynomial> ps : take(LIMIT, pss)) {
            System.out.println("Δ(" + middle(ps.toString()) + ") = " + its(delta(ps)));
        }
    }

    private void demoPow() {
        Iterable<Pair<RationalMultivariatePolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.withScale(1).naturalIntegersGeometric()
        );
        for (Pair<RationalMultivariatePolynomial, Integer> p : take(SMALL_LIMIT, ps)) {
            System.out.println("(" + p.a + ") ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private void demoApplyRational() {
        Iterable<Pair<RationalMultivariatePolynomial, Map<Variable, Rational>>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.choose(
                    P.dependentPairsInfiniteSquareRootOrder(
                            filterInfinite(r -> r.degree() > 0, P.rationalMultivariatePolynomials()),
                            q -> {
                                List<Variable> us = toList(q.variables());
                                return map(
                                        p -> p.b,
                                        P.dependentPairsInfiniteLogarithmicOrder(
                                                nub(map(vs -> sort(nub(concat(vs, us))), P.subsets(P.variables()))),
                                                ws -> P.maps(ws, P.rationals())
                                        )
                                );
                            }
                    ),
                    map(i -> new Pair<>(of(i), new TreeMap<>()), P.withScale(4).bigIntegers())
            );
        } else {
            ps = P.choose(
                    P.dependentPairsInfinite(
                            filterInfinite(r -> r.degree() > 0, P.withScale(4).rationalMultivariatePolynomials()),
                            q -> {
                                List<Variable> us = toList(q.variables());
                                return map(
                                        p -> p.b,
                                        P.dependentPairsInfiniteLogarithmicOrder(
                                                map(
                                                        vs -> sort(nub(concat(vs, us))),
                                                        P.withScale(4).subsets(P.variables())
                                                ),
                                                ws -> P.maps(ws, P.rationals())
                                        )
                                );
                            }
                    ),
                    map(i -> new Pair<>(of(i), new TreeMap<>()), P.withScale(4).bigIntegers())
            );
        }
        for (Pair<RationalMultivariatePolynomial, Map<Variable, Rational>> p : take(LIMIT, ps)) {
            System.out.println("applyRational(" + p.a + ", " + p.b + ") = " + p.a.applyRational(p.b));
        }
    }

    private void demoSubstituteMonomial() {
        Iterable<Pair<RationalMultivariatePolynomial, Map<Variable, Monomial>>> ps = P.pairsSquareRootOrder(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.withElement(
                        new TreeMap<>(),
                        map(
                                p -> p.b,
                                P.dependentPairsInfiniteLogarithmicOrder(
                                        P.withScale(4).subsetsAtLeast(1, P.withScale(4).variables()),
                                        vs -> P.maps(vs, P.withScale(4).monomials())
                                )
                        )
                )
        );
        for (Pair<RationalMultivariatePolynomial, Map<Variable, Monomial>> p : take(LIMIT, ps)) {
            System.out.println("substituteMonomial(" + p.a + ", " + p.b + ") = " + p.a.substituteMonomial(p.b));
        }
    }

    private void demoSubstitute() {
        Iterable<Pair<RationalMultivariatePolynomial, Map<Variable, RationalMultivariatePolynomial>>> ps =
                P.pairsSquareRootOrder(
                        P.withScale(4).withSecondaryScale(1).rationalMultivariatePolynomials(),
                        P.withElement(
                                new TreeMap<>(),
                                map(
                                        p -> p.b,
                                        P.dependentPairsInfiniteLogarithmicOrder(
                                                P.withScale(4).subsetsAtLeast(1, P.withScale(4).variables()),
                                                vs -> P.maps(
                                                        vs,
                                                        P.withScale(4).withSecondaryScale(1)
                                                                .rationalMultivariatePolynomials()
                                                )
                                        )
                                )
                        )
                );
        for (Pair<RationalMultivariatePolynomial, Map<Variable, RationalMultivariatePolynomial>> p : take(LIMIT, ps)) {
            System.out.println("substitute(" + p.a + ", " + p.b + ") = " + p.a.substitute(p.b));
        }
    }

    private void demoConstantFactor_MonomialOrder() {
        Iterable<Pair<RationalMultivariatePolynomial, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                filterInfinite(p -> p != ZERO, P.withScale(4).rationalMultivariatePolynomials()),
                P.monomialOrders()
        );
        for (Pair<RationalMultivariatePolynomial, MonomialOrder> p : take(LIMIT, ps)) {
            System.out.println("constantFactor(" + p.a + ", " + p.b + ") = " + p.a.constantFactor(p.b));
        }
    }

    private void demoConstantFactor() {
        Iterable<RationalMultivariatePolynomial> ps = filterInfinite(
                q -> q != ZERO,
                P.withScale(4).rationalMultivariatePolynomials()
        );
        for (RationalMultivariatePolynomial p : take(LIMIT, ps)) {
            System.out.println("constantFactor(" + p + ") = " + p.constantFactor());
        }
    }

    private void demoPowerReduce() {
        Iterable<Pair<RationalMultivariatePolynomial, Map<Variable, RationalPolynomial>>> ps = P.pairsSquareRootOrder(
                P.withScale(4).withSecondaryScale(1).rationalMultivariatePolynomials(),
                P.withElement(
                        new TreeMap<>(),
                        map(
                                p -> p.b,
                                P.dependentPairsInfiniteLogarithmicOrder(
                                        P.withScale(4).subsetsAtLeast(1, P.withScale(4).variables()),
                                        vs -> P.maps(
                                                vs,
                                                P.withScale(4).withSecondaryScale(2).monicRationalPolynomialsAtLeast(1)
                                        )
                                )
                        )
                )
        );
        for (Pair<RationalMultivariatePolynomial, Map<Variable, RationalPolynomial>> p : take(LIMIT, ps)) {
            System.out.println("powerReduce(" + p.a + ", " + p.b + ") = " + p.a.powerReduce(p.b));
        }
    }

    private void demoEquals_RationalMultivariatePolynomial() {
        Iterable<Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials()
        );
        for (Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial> p : take(LIMIT, ps)) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.withScale(4).rationalMultivariatePolynomials())) {
            //noinspection ObjectEqualsNull
            System.out.println(p + (p.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.withScale(4).rationalMultivariatePolynomials())) {
            System.out.println("hashCode(" + p + ") = " + p.hashCode());
        }
    }

    private void demoCompareTo() {
        Iterable<Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial>> ps = P.pairs(
                P.withScale(4).rationalMultivariatePolynomials()
        );
        for (Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial> p : take(LIMIT, ps)) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b) + " " + p.b);
        }
    }

    private void demoReadStrict_String_MonomialOrder() {
        for (Pair<String, MonomialOrder> p : take(LIMIT, P.pairsLogarithmicOrder(P.strings(), P.monomialOrders()))) {
            System.out.println("readStrict(" + nicePrint(p.a) + ", " + p.b + ") = " + readStrict(p.a, p.b));
        }
    }

    private void demoReadStrict_String_MonomialOrder_targeted() {
        Iterable<Pair<String, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                P.strings(RATIONAL_MULTIVARIATE_POLYNOMIAL_CHARS),
                P.monomialOrders()
        );
        for (Pair<String, MonomialOrder> p : take(LIMIT, ps)) {
            System.out.println("readStrict(" + p.a + ", " + p.b + ") = " + readStrict(p.a, p.b));
        }
    }

    private void demoReadStrict_String() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private static boolean onlyNumeric(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c < '0' || c > '9') && c != '-' && c != '/') return false;
        }
        return true;
    }

    private void demoReadStrict_String_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_MULTIVARIATE_POLYNOMIAL_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoToString_MonomialOrder() {
        Iterable<Pair<RationalMultivariatePolynomial, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalMultivariatePolynomials(),
                P.monomialOrders()
        );
        for (Pair<RationalMultivariatePolynomial, MonomialOrder> p : take(LIMIT, ps)) {
            System.out.println("toString(" + p.a + ", " + p.b + ") = " + p.a.toString(p.b));
        }
    }

    private void demoToString() {
        for (RationalMultivariatePolynomial p : take(LIMIT, P.withScale(4).rationalMultivariatePolynomials())) {
            System.out.println(p);
        }
    }
}
