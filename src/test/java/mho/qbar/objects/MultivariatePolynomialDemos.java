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

import static mho.qbar.objects.MultivariatePolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static mho.wheels.testing.Testing.nicePrint;

@SuppressWarnings("UnusedDeclaration")
public class MultivariatePolynomialDemos extends QBarDemos {
    private static final @NotNull String MULTIVARIATE_POLYNOMIAL_CHARS = "*+-0123456789^abcdefghijklmnopqrstuvwxyz";

    public MultivariatePolynomialDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoIterable() {
        Iterable<Pair<MultivariatePolynomial, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).multivariatePolynomials(),
                P.monomialOrders()
        );
        for (Pair<MultivariatePolynomial, MonomialOrder> p : take(LIMIT, ps)) {
            System.out.println("iterable(" + p.a + ", " + p.b + ") = " + toList(p.a.iterable(p.b)));
        }
    }

    private void demoIterator() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println("toList(" + p + ") = " + toList(p));
        }
    }

    private void demoToRationalMultivariatePolynomial() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println("toRationalMultivariatePolynomial(" + p + ") = " +
                    p.toRationalMultivariatePolynomial());
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
            System.out.println("of(" + middle(ps.toString()) + ") = " + of(ps));
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

    private void demoOf_Variable() {
        for (Variable v : take(MEDIUM_LIMIT, P.variables())) {
            System.out.println("of(" + v + ") = " + of(v));
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

    private void demoDegree_Variable() {
        Iterable<Pair<MultivariatePolynomial, Variable>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).multivariatePolynomials(),
                P.withScale(4).variables()
        );
        for (Pair<MultivariatePolynomial, Variable> p : take(LIMIT, ps)) {
            System.out.println("degree(" + p.a + ", " + p.b + ") = " + p.a.degree(p.b));
        }
    }

    private void demoIsHomogeneous() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println(p + " is " + (p.isHomogeneous() ? "" : "not ") + "homogeneous");
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

    private void demoGroupVariables_List_Variable_MonomialOrder() {
        Iterable<Triple<MultivariatePolynomial, List<Variable>, MonomialOrder>> ts = P.triples(
                P.withScale(4).multivariatePolynomials(),
                P.withScale(4).lists(P.withScale(4).variables()),
                P.monomialOrders()
        );
        for (Triple<MultivariatePolynomial, List<Variable>, MonomialOrder> t : take(LIMIT, ts)) {
            System.out.println("groupVariables(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.groupVariables(t.b, t.c));
        }
    }

    private void demoGroupVariables_List_Variable() {
        Iterable<Pair<MultivariatePolynomial, List<Variable>>> ps = P.pairs(
                P.withScale(4).multivariatePolynomials(),
                P.withScale(4).lists(P.withScale(4).variables())
        );
        for (Pair<MultivariatePolynomial, List<Variable>> p : take(LIMIT, ps)) {
            System.out.println("groupVariables(" + p.a + ", " + p.b + ") = " + p.a.groupVariables(p.b));
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

    private void demoDivideExact_BigInteger() {
        Iterable<Pair<MultivariatePolynomial, BigInteger>> ps = map(
                p -> new Pair<>(p.a.multiply(p.b), p.b),
                P.pairs(P.withScale(4).multivariatePolynomials(), P.nonzeroBigIntegers())
        );
        for (Pair<MultivariatePolynomial, BigInteger> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divideExact(p.b));
        }
    }

    private void demoDivideExact_int() {
        Iterable<Pair<MultivariatePolynomial, Integer>> ps = map(
                p -> new Pair<>(p.a.multiply(p.b), p.b),
                P.pairs(P.withScale(4).multivariatePolynomials(), P.nonzeroIntegers())
        );
        for (Pair<MultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divideExact(p.b));
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

    private void demoSum() {
        Iterable<List<MultivariatePolynomial>> pss = P.withScale(4).lists(P.withScale(4).multivariatePolynomials());
        for (List<MultivariatePolynomial> ps : take(LIMIT, pss)) {
            System.out.println("Σ(" + middle(ps.toString()) + ") = " + sum(ps));
        }
    }

    private void demoProduct() {
        Iterable<List<MultivariatePolynomial>> pss = P.withScale(4).lists(P.withScale(4).multivariatePolynomials());
        for (List<MultivariatePolynomial> ps : take(LIMIT, pss)) {
            System.out.println("Π(" + middle(ps.toString()) + ") = " + product(ps));
        }
    }

    private void demoDelta() {
        Iterable<List<MultivariatePolynomial>> pss =
                P.withScale(4).listsAtLeast(1, P.withScale(4).multivariatePolynomials());
        for (List<MultivariatePolynomial> ps : take(LIMIT, pss)) {
            System.out.println("Δ(" + middle(ps.toString()) + ") = " + its(delta(ps)));
        }
    }

    private void demoPow() {
        Iterable<Pair<MultivariatePolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).multivariatePolynomials(),
                P.withScale(1).naturalIntegersGeometric()
        );
        for (Pair<MultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private void demoBinomialPower() {
        Iterable<Triple<Variable, Variable, Integer>> ts = map(
                p -> new Triple<>(p.a.a, p.a.b, p.b),
                P.pairsLogarithmicOrder(P.distinctPairs(P.variables()), P.naturalIntegersGeometric())
        );
        for (Triple<Variable, Variable, Integer> t : take(LIMIT, ts)) {
            System.out.println("binomialPower(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    binomialPower(t.a, t.b, t.c));
        }
    }

    private void demoApplyBigInteger() {
        Iterable<Pair<MultivariatePolynomial, Map<Variable, BigInteger>>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.choose(
                    map(i -> new Pair<>(of(i), new TreeMap<>()), P.withScale(4).bigIntegers()),
                    P.dependentPairsInfiniteSquareRootOrder(
                            filterInfinite(r -> r.degree() > 0, P.multivariatePolynomials()),
                            q -> {
                                List<Variable> us = toList(q.variables());
                                return map(
                                        p -> p.b,
                                        P.dependentPairsInfiniteLogarithmicOrder(
                                                nub(map(vs -> sort(nub(concat(vs, us))), P.subsets(P.variables()))),
                                                ws -> P.maps(ws, P.bigIntegers())
                                        )
                                );
                            }
                    )
            );
        } else {
            ps = P.choose(
                    P.dependentPairsInfinite(
                            filterInfinite(r -> r.degree() > 0, P.withScale(4).multivariatePolynomials()),
                            q -> {
                                List<Variable> us = toList(q.variables());
                                return map(
                                        p -> p.b,
                                        P.dependentPairsInfiniteLogarithmicOrder(
                                                map(
                                                        vs -> sort(nub(concat(vs, us))),
                                                        P.withScale(4).subsets(P.variables())
                                                ),
                                                ws -> P.maps(ws, P.bigIntegers())
                                        )
                                );
                            }
                    ),
                    map(i -> new Pair<>(of(i), new TreeMap<>()), P.withScale(4).bigIntegers())
            );
        }
        for (Pair<MultivariatePolynomial, Map<Variable, BigInteger>> p : take(LIMIT, ps)) {
            System.out.println("applyBigInteger(" + p.a + ", " + p.b + ") = " + p.a.applyBigInteger(p.b));
        }
    }

    private void demoApplyRational() {
        Iterable<Pair<MultivariatePolynomial, Map<Variable, Rational>>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.choose(
                    map(i -> new Pair<>(of(i), new TreeMap<>()), P.withScale(4).bigIntegers()),
                    P.dependentPairsInfiniteSquareRootOrder(
                            filterInfinite(r -> r.degree() > 0, P.multivariatePolynomials()),
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
                    )
            );
        } else {
            ps = P.choose(
                    P.dependentPairsInfinite(
                            filterInfinite(r -> r.degree() > 0, P.withScale(4).multivariatePolynomials()),
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
        for (Pair<MultivariatePolynomial, Map<Variable, Rational>> p : take(LIMIT, ps)) {
            System.out.println("applyBigInteger(" + p.a + ", " + p.b + ") = " + p.a.applyRational(p.b));
        }
    }

    private void demoSubstituteMonomial() {
        Iterable<Pair<MultivariatePolynomial, Map<Variable, Monomial>>> ps = P.pairsSquareRootOrder(
                P.withScale(4).multivariatePolynomials(),
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
        for (Pair<MultivariatePolynomial, Map<Variable, Monomial>> p : take(LIMIT, ps)) {
            System.out.println("substituteMonomial(" + p.a + ", " + p.b + ") = " + p.a.substituteMonomial(p.b));
        }
    }

    private void demoSubstitute() {
        Iterable<Pair<MultivariatePolynomial, Map<Variable, MultivariatePolynomial>>> ps = P.pairsSquareRootOrder(
                P.withScale(2).withSecondaryScale(1).multivariatePolynomials(),
                P.withElement(
                        new TreeMap<>(),
                        map(
                                p -> p.b,
                                P.dependentPairsInfiniteLogarithmicOrder(
                                        P.withScale(4).subsetsAtLeast(1, P.withScale(4).variables()),
                                        vs -> P.maps(
                                                vs,
                                                P.withScale(2).withSecondaryScale(1).multivariatePolynomials()
                                        )
                                )
                        )
                )
        );
        for (Pair<MultivariatePolynomial, Map<Variable, MultivariatePolynomial>> p : take(LIMIT, ps)) {
            System.out.println("substitute(" + p.a + ", " + p.b + ") = " + p.a.substitute(p.b));
        }
    }

    private void demoSylvesterMatrix() {
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
            System.out.println("sylvesterMatrix(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.sylvesterMatrix(t.b, t.c));
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

    private void demoPowerReduce() {
        Iterable<Pair<MultivariatePolynomial, Map<Variable, Polynomial>>> ps = P.pairsSquareRootOrder(
                P.withScale(4).multivariatePolynomials(),
                P.withElement(
                        new TreeMap<>(),
                        map(
                                p -> p.b,
                                P.dependentPairsInfiniteLogarithmicOrder(
                                        P.withScale(4).subsetsAtLeast(1, P.withScale(4).variables()),
                                        vs -> P.maps(vs, P.withScale(4).monicPolynomialsAtLeast(1))
                                )
                        )
                )
        );
        for (Pair<MultivariatePolynomial, Map<Variable, Polynomial>> p : take(LIMIT, ps)) {
            System.out.println("powerReduce(" + p.a + ", " + p.b + ") = " + p.a.powerReduce(p.b));
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
                P.strings(MULTIVARIATE_POLYNOMIAL_CHARS),
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

    private void demoReadStrict_String_targeted() {
        for (String s : take(LIMIT, P.strings(MULTIVARIATE_POLYNOMIAL_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoToString_MonomialOrder() {
        Iterable<Pair<MultivariatePolynomial, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).multivariatePolynomials(),
                P.monomialOrders()
        );
        for (Pair<MultivariatePolynomial, MonomialOrder> p : take(LIMIT, ps)) {
            System.out.println("toString(" + p.a + ", " + p.b + ") = " + p.a.toString(p.b));
        }
    }

    private void demoToString() {
        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            System.out.println(p);
        }
    }
}
