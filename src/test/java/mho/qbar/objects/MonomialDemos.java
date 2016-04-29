package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.qbar.objects.Monomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.nicePrint;

@SuppressWarnings("UnusedDeclaration")
public class MonomialDemos extends QBarDemos {
    private static final @NotNull String MONOMIAL_CHARS = "*0123456789^abcdefghijklmnopqrstuvwxyz";

    public MonomialDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoGetExponents() {
        for (Monomial m : take(LIMIT, P.monomials())) {
            System.out.println("getExponents(" + m + ") = " + m.getExponents());
        }
    }

    private void demoExponent() {
        Iterable<Pair<Monomial, Variable>> ps = P.pairsLogarithmicOrder(P.monomials(), P.variables());
        for (Pair<Monomial, Variable> p : take(LIMIT, ps)) {
            System.out.println("exponent(" + p.a + ", " + p.b + ") = " + p.a.exponent(p.b));
        }
    }

    private void demoSize() {
        for (Monomial m : take(LIMIT, P.monomials())) {
            System.out.println("size(" + m + ") = " + m.size());
        }
    }

    private void demoTerms() {
        for (Monomial m : take(LIMIT, P.monomials())) {
            System.out.println("terms(" + m + ") = " + toList(m.terms()));
        }
    }

    private void demoOf() {
        for (List<Integer> is : take(LIMIT, P.lists(P.naturalIntegersGeometric()))) {
            String listString = tail(init(is.toString()));
            System.out.println("of(" + listString + ") = " + of(is));
        }
    }

    private void demoFromTerms() {
        Iterable<List<Pair<Variable, Integer>>> pss = P.withElement(
                Collections.emptyList(),
                map(
                        p -> toList(zip(p.a, p.b)),
                        P.dependentPairsInfinite(
                                P.listsAtLeast(1, P.variables()),
                                vs -> P.lists(vs.size(), P.positiveIntegersGeometric())
                        )
                )
        );
        for (List<Pair<Variable, Integer>> ps : take(LIMIT, pss)) {
            String listString = tail(init(ps.toString()));
            System.out.println("fromTerms(" + listString + ") = " + fromTerms(ps));
        }
    }

    private void demoDegree() {
        for (Monomial m : take(LIMIT, P.monomials())) {
            System.out.println("degree(" + m + ") = " + m.degree());
        }
    }

    private void demoVariables() {
        for (Monomial m : take(LIMIT, P.monomials())) {
            System.out.println("variables(" + m + ") = " + m.variables());
        }
    }

    private void demoRemoveVariable() {
        Iterable<Pair<Monomial, Variable>> ps = P.pairsLogarithmicOrder(
                P.monomials(),
                P.withScale(4).variables()
        );
        for (Pair<Monomial, Variable> p : take(LIMIT, ps)) {
            System.out.println("removeVariable(" + p.a + ", " + p.b + ") = " + p.a.removeVariable(p.b));
        }
    }

    private void demoRemoveVariables() {
        Iterable<Pair<Monomial, List<Variable>>> ps = P.pairsLogarithmicOrder(
                P.monomials(),
                P.withScale(4).lists(P.withScale(4).variables())
        );
        for (Pair<Monomial, List<Variable>> p : take(LIMIT, ps)) {
            System.out.println("removeVariables(" + p.a + ", " + p.b + ") = " + p.a.removeVariables(p.b));
        }
    }

    private void demoMultiply() {
        for (Pair<Monomial, Monomial> p : take(LIMIT, P.pairs(P.monomials()))) {
            System.out.println("(" + p.a + ") * (" + p.b + ") = " + p.a.multiply(p.b));
        }
    }

    private void demoProduct() {
        for (List<Monomial> ps : take(LIMIT, P.withScale(4).lists(P.withScale(4).monomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Π(" + listString + ") = " + product(ps));
        }
    }

    private void demoPow() {
        Iterable<Pair<Monomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).monomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<Monomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private void demoApplyBigInteger() {
        Iterable<Pair<Monomial, Map<Variable, BigInteger>>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = cons(
                    new Pair<>(ONE, new TreeMap<>()),
                    P.dependentPairsInfiniteSquareRootOrder(
                            filterInfinite(f -> f != ONE, P.monomials()),
                            m -> {
                                List<Variable> us = toList(m.variables());
                                return map(
                                        qs -> toSortedMap(zip(qs.a, qs.b)),
                                        P.dependentPairsInfiniteLogarithmicOrder(
                                                nub(map(vs -> sort(nub(concat(vs, us))), P.subsets(P.variables()))),
                                                ws -> P.lists(ws.size(), P.bigIntegers())
                                        )
                                );
                            }
                    )
            );
        } else {
            ps = P.withElement(
                    new Pair<>(ONE, new TreeMap<>()),
                    P.dependentPairsInfinite(
                            filterInfinite(f -> f != ONE, P.withScale(4).monomials()),
                            m -> {
                                List<Variable> us = toList(m.variables());
                                return map(
                                        qs -> toSortedMap(zip(qs.a, qs.b)),
                                        P.dependentPairsInfinite(
                                                map(
                                                        vs -> sort(nub(concat(vs, us))),
                                                        P.withScale(4).subsets(P.variables())
                                                ),
                                                ws -> P.lists(ws.size(), P.withScale(4).bigIntegers())
                                        )
                                );
                            }
                    )
            );
        }
        for (Pair<Monomial, Map<Variable, BigInteger>> p : take(LIMIT, ps)) {
            System.out.println("applyBigInteger(" + p.a + ", " + p.b + ") = " + p.a.applyBigInteger(p.b));
        }
    }

    private void demoApplyRational() {
        Iterable<Pair<Monomial, Map<Variable, Rational>>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = cons(
                    new Pair<>(ONE, new TreeMap<>()),
                    P.dependentPairsInfiniteSquareRootOrder(
                            filterInfinite(f -> f != ONE, P.monomials()),
                            m -> {
                                List<Variable> us = toList(m.variables());
                                return map(
                                        qs -> toSortedMap(zip(qs.a, qs.b)),
                                        P.dependentPairsInfiniteLogarithmicOrder(
                                                nub(map(vs -> sort(nub(concat(vs, us))), P.subsets(P.variables()))),
                                                ws -> P.lists(ws.size(), P.rationals())
                                        )
                                );
                            }
                    )
            );
        } else {
            ps = P.withElement(
                    new Pair<>(ONE, new TreeMap<>()),
                    P.dependentPairsInfinite(
                            filterInfinite(f -> f != ONE, P.withScale(4).monomials()),
                            m -> {
                                List<Variable> us = toList(m.variables());
                                return map(
                                        qs -> toSortedMap(zip(qs.a, qs.b)),
                                        P.dependentPairsInfinite(
                                                map(
                                                        vs -> sort(nub(concat(vs, us))),
                                                        P.withScale(4).subsets(P.variables())
                                                ),
                                                ws -> P.lists(ws.size(), P.withScale(4).rationals())
                                        )
                                );
                            }
                    )
            );
        }
        for (Pair<Monomial, Map<Variable, Rational>> p : take(LIMIT, ps)) {
            System.out.println("applyRational(" + p.a + ", " + p.b + ") = " + p.a.applyRational(p.b));
        }
    }

    private void demoSubstitute() {
        Iterable<Pair<Monomial, Map<Variable, Monomial>>> ps = P.pairsSquareRootOrder(
                P.withScale(4).monomials(),
                P.withElement(
                        new TreeMap<>(),
                        map(
                                p -> toMap(zip(p.a, p.b)),
                                P.dependentPairsInfiniteLogarithmicOrder(
                                        P.withScale(4).subsetsAtLeast(1, P.variables()),
                                        vs -> P.lists(vs.size(), P.withScale(4).monomials())
                                )
                        )
                )
        );
        for (Pair<Monomial, Map<Variable, Monomial>> p : take(LIMIT, ps)) {
            System.out.println("substitute(" + p.a + ", " + p.b + ") = " + p.a.substitute(p.b));
        }
    }

    private void demoEquals_Monomial() {
        for (Pair<Monomial, Monomial> p : take(LIMIT, P.pairs(P.monomials()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (Monomial m : take(LIMIT, P.monomials())) {
            //noinspection ObjectEqualsNull
            System.out.println(m + (m.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (Monomial m : take(LIMIT, P.monomials())) {
            System.out.println("hashCode(" + m + ") = " + m.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<Monomial, Monomial> p : take(LIMIT, P.pairs(P.monomials()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoReadStrict() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_targeted() {
        for (String s : take(LIMIT, P.strings(MONOMIAL_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoToString() {
        for (Monomial m : take(LIMIT, P.monomials())) {
            System.out.println(m);
        }
    }
}
