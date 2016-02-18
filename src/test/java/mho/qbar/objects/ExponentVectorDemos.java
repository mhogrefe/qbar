package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static mho.qbar.objects.ExponentVector.*;
import static mho.wheels.iterables.IterableUtils.*;

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
        Iterable<Pair<ExponentVector, Variable>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).exponentVectors(),
                P.withScale(4).variables()
        );
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
        for (List<Integer> is : take(LIMIT, P.withScale(4).lists(P.naturalIntegersGeometric()))) {
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
                                P.withScale(4).subsetsAtLeast(1, P.withScale(4).variables()),
                                vs -> filterInfinite(
                                        is -> last(is) != 0,
                                        P.lists(vs.size(), P.withScale(4).positiveIntegersGeometric())
                                )
                        )
                )
        );
        for (List<Pair<Variable, Integer>> ps : take(LIMIT, pss)) {
            String listString = tail(init(ps.toString()));
            System.out.println("fromTerms(" + listString + ") = " + fromTerms(ps));
        }
    }

    private void demoToString() {
        for (ExponentVector ev : take(LIMIT, P.withScale(4).exponentVectors())) {
            System.out.println(ev);
        }
    }
}
