package mho.qbar.objects;

import mho.qbar.testing.QBarTestProperties;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

public class ExponentVectorProperties extends QBarTestProperties {
    private static final @NotNull String EXPONENT_VECTOR_CHARS = "*0123456789^abcdefghijklmnopqrstuvwxyz";

    public ExponentVectorProperties() {
        super("ExponentVector");
    }

    @Override
    protected void testBothModes() {
        propertiesExponent();
        propertiesTerms();
    }

    private void propertiesExponent() {
        initialize("exponent(int)");
        Iterable<Pair<ExponentVector, Integer>> ps = P.pairsLogarithmicOrder(
                P.exponentVectors(),
                P.naturalIntegersGeometric()
        );
        for (Pair<ExponentVector, Integer> p : take(LIMIT, ps)) {
            int exponent = p.a.exponent(p.b);
            assertEquals(p, exponent, lookup(p.b, p.a.terms()).orElse(0));
        }

        for (Pair<ExponentVector, Integer> p : take(LIMIT, P.pairs(P.exponentVectors(), P.negativeIntegers()))) {
            try {
                p.a.exponent(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesTerms() {
        initialize("terms()");
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            Iterable<Pair<Integer, Integer>> termsIterable = ev.terms();
            testNoRemove(termsIterable);
            testHasNext(termsIterable);
            List<Pair<Integer, Integer>> terms = toList(termsIterable);
            assertTrue(ev, all(t -> t.a >= 0, terms));
            assertTrue(ev, all(t -> t.b > 0, terms));
            //noinspection RedundantCast
            assertTrue(ev, increasing((Iterable<Integer>) map(t -> t.a, terms)));
        }
    }
}
