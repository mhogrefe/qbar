package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.qbar.objects.ExponentVector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.nicePrint;

@SuppressWarnings("UnusedDeclaration")
public class ExponentVectorDemos extends QBarDemos {
    private static final @NotNull String EXPONENT_VECTOR_CHARS = "*0123456789^abcdefghijklmnopqrstuvwxyz";

    public ExponentVectorDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoGetExponents() {
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            System.out.println("getExponents(" + ev + ") = " + ev.getExponents());
        }
    }

    private void demoExponent() {
        Iterable<Pair<ExponentVector, Variable>> ps = P.pairsLogarithmicOrder(P.exponentVectors(), P.variables());
        for (Pair<ExponentVector, Variable> p : take(LIMIT, ps)) {
            System.out.println("exponent(" + p.a + ", " + p.b + ") = " + p.a.exponent(p.b));
        }
    }

    private void demoSize() {
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            System.out.println("size(" + ev + ") = " + ev.size());
        }
    }

    private void demoTerms() {
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            System.out.println("terms(" + ev + ") = " + toList(ev.terms()));
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
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            System.out.println("degree(" + ev + ") = " + ev.degree());
        }
    }

    private void demoVariables() {
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            System.out.println("variables(" + ev + ") = " + ev.variables());
        }
    }

    private void demoRemoveVariable() {
        Iterable<Pair<ExponentVector, Variable>> ps = P.pairsLogarithmicOrder(
                P.exponentVectors(),
                P.withScale(4).variables()
        );
        for (Pair<ExponentVector, Variable> p : take(LIMIT, ps)) {
            System.out.println("removeVariable(" + p.a + ", " + p.b + ") = " + p.a.removeVariable(p.b));
        }
    }

    private void demoRemoveVariables() {
        Iterable<Pair<ExponentVector, List<Variable>>> ps = P.pairsLogarithmicOrder(
                P.exponentVectors(),
                P.withScale(4).lists(P.withScale(4).variables())
        );
        for (Pair<ExponentVector, List<Variable>> p : take(LIMIT, ps)) {
            System.out.println("removeVariables(" + p.a + ", " + p.b + ") = " + p.a.removeVariables(p.b));
        }
    }

    private void demoMultiply() {
        for (Pair<ExponentVector, ExponentVector> p : take(LIMIT, P.pairs(P.exponentVectors()))) {
            System.out.println("(" + p.a + ") * (" + p.b + ") = " + p.a.multiply(p.b));
        }
    }

    private void demoProduct() {
        for (List<ExponentVector> ps : take(LIMIT, P.withScale(4).lists(P.withScale(4).exponentVectors()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Π(" + listString + ") = " + product(ps));
        }
    }

    private void demoPow() {
        Iterable<Pair<ExponentVector, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).exponentVectors(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<ExponentVector, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private void demoApplyBigInteger() {
        Iterable<Pair<ExponentVector, Map<Variable, BigInteger>>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = cons(
                    new Pair<>(ONE, new TreeMap<>()),
                    P.dependentPairsInfiniteSquareRootOrder(
                            filterInfinite(f -> f != ONE, P.exponentVectors()),
                            ev -> {
                                List<Variable> us = toList(ev.variables());
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
                            filterInfinite(f -> f != ONE, P.withScale(4).exponentVectors()),
                            ev -> {
                                List<Variable> us = toList(ev.variables());
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
        for (Pair<ExponentVector, Map<Variable, BigInteger>> p : take(LIMIT, ps)) {
            System.out.println("applyBigInteger(" + p.a + ", " + p.b + ") = " + p.a.applyBigInteger(p.b));
        }
    }

    private void demoApplyRational() {
        Iterable<Pair<ExponentVector, Map<Variable, Rational>>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = cons(
                    new Pair<>(ONE, new TreeMap<>()),
                    P.dependentPairsInfiniteSquareRootOrder(
                            filterInfinite(f -> f != ONE, P.exponentVectors()),
                            ev -> {
                                List<Variable> us = toList(ev.variables());
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
                            filterInfinite(f -> f != ONE, P.withScale(4).exponentVectors()),
                            ev -> {
                                List<Variable> us = toList(ev.variables());
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
        for (Pair<ExponentVector, Map<Variable, Rational>> p : take(LIMIT, ps)) {
            System.out.println("applyRational(" + p.a + ", " + p.b + ") = " + p.a.applyRational(p.b));
        }
    }

    private void demoSubstitute() {
        Iterable<Pair<ExponentVector, Map<Variable, ExponentVector>>> ps = P.pairsSquareRootOrder(
                P.withScale(4).exponentVectors(),
                P.withElement(
                        new TreeMap<>(),
                        map(
                                p -> toMap(zip(p.a, p.b)),
                                P.dependentPairsInfiniteLogarithmicOrder(
                                        P.withScale(4).subsetsAtLeast(1, P.variables()),
                                        vs -> P.lists(vs.size(), P.withScale(4).exponentVectors())
                                )
                        )
                )
        );
        for (Pair<ExponentVector, Map<Variable, ExponentVector>> p : take(LIMIT, ps)) {
            System.out.println("substitute(" + p.a + ", " + p.b + ") = " + p.a.substitute(p.b));
        }
    }

    private void demoEquals_ExponentVector() {
        for (Pair<ExponentVector, ExponentVector> p : take(LIMIT, P.pairs(P.exponentVectors()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            //noinspection ObjectEqualsNull
            System.out.println(ev + (ev.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            System.out.println("hashCode(" + ev + ") = " + ev.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<ExponentVector, ExponentVector> p : take(LIMIT, P.pairs(P.exponentVectors()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoReadStrict() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_targeted() {
        for (String s : take(LIMIT, P.strings(EXPONENT_VECTOR_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoToString() {
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            System.out.println(ev);
        }
    }
}
