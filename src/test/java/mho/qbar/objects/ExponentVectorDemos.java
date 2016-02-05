package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mho.qbar.objects.ExponentVector.*;
import static mho.wheels.iterables.IterableUtils.*;

@SuppressWarnings("UnusedDeclaration")
public class ExponentVectorDemos extends QBarDemos {
    private static final @NotNull String EXPONENT_VECTOR_CHARS = "*0123456789^abcdefghijklmnopqrstuvwxyz";

    public ExponentVectorDemos(boolean useRandom) {
        super(useRandom);
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

    private void demoToString() {
        for (ExponentVector ev : take(LIMIT, P.withScale(4).exponentVectors())) {
            System.out.println(ev);
        }
    }
}
