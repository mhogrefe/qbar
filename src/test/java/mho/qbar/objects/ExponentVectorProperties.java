package mho.qbar.objects;

import mho.qbar.testing.QBarTestProperties;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static mho.qbar.objects.ExponentVector.*;
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
        propertiesOf();
    }

    private void propertiesExponent() {
        initialize("exponent(int)");
        Iterable<Pair<ExponentVector, Variable>> ps = P.pairsLogarithmicOrder(P.exponentVectors(), P.variables());
        for (Pair<ExponentVector, Variable> p : take(LIMIT, ps)) {
            int exponent = p.a.exponent(p.b);
            assertEquals(p, exponent, lookup(p.b, p.a.terms()).orElse(0));
        }
    }

    private void propertiesTerms() {
        initialize("terms()");
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            Iterable<Pair<Variable, Integer>> termsIterable = ev.terms();
            testNoRemove(termsIterable);
            testHasNext(termsIterable);
            List<Pair<Variable, Integer>> terms = toList(termsIterable);
            assertTrue(ev, all(t -> t.b > 0, terms));
            //noinspection RedundantCast
            assertTrue(ev, increasing((Iterable<Variable>) map(t -> t.a, terms)));
        }
    }

    private void propertiesOf() {
        initialize("of(List<Integer>)");
        for (List<Integer> is : take(LIMIT, P.withScale(4).lists(P.naturalIntegersGeometric()))) {
            ExponentVector ev = of(is);
            ev.validate();
        }

//        Iterable<List<Integer>> iss = filterInfinite(
//                is -> is.isEmpty() || last(is) != 0,
//                P.lists(P.naturalIntegersGeometric())
//        );
//        for (List<Integer> is : take(LIMIT, iss)) {
//            fixedPoint(js -> toList(of(js)), is);
//        }

        Iterable<List<Integer>> iss = P.listsWithSublists(
                map(Collections::singletonList, P.negativeIntegersGeometric()),
                P.naturalIntegersGeometric()
        );
        for (List<Integer> is : take(LIMIT, iss)) {
            try {
                of(is);
                fail(is);
            } catch (IllegalArgumentException ignored) {}
        }

        for (List<Integer> is : take(LIMIT, P.listsWithElement(null, P.naturalIntegersGeometric()))) {
            try {
                of(is);
                fail(is);
            } catch (NullPointerException ignored) {}
        }
    }
}
