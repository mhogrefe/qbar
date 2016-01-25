package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.math.MathUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import static mho.wheels.iterables.IterableUtils.*;

@SuppressWarnings("UnusedDeclaration")
public class ExponentVectorDemos extends QBarDemos {
    private static final @NotNull String EXPONENT_VECTOR_CHARS = "*0123456789^abcdefghijklmnopqrstuvwxyz";

    public ExponentVectorDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoExponent() {
        Iterable<Pair<ExponentVector, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).exponentVectors(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<ExponentVector, Integer> p : take(LIMIT, ps)) {
            System.out.println("exponent(" + p.a + ", " + MathUtils.variableIndexToString(p.b) + ") = " +
                    p.a.exponent(p.b));
        }
    }

    private void demoToString() {
        for (ExponentVector ev : take(LIMIT, P.withScale(4).exponentVectors())) {
            System.out.println(ev);
        }
    }
}
