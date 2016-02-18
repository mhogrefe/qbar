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
        propertiesGetExponents();
        propertiesExponent();
        propertiesSize();
        propertiesTerms();
        propertiesOf();
        propertiesFromTerms();
    }

    private void propertiesGetExponents() {
        initialize("getExponents()");
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            List<Integer> exponents = ev.getExponents();
            assertTrue(ev, all(i -> i >= 0, exponents));
            //noinspection Convert2MethodRef
            inverse(ExponentVector::getExponents, (List<Integer> is) -> of(is), ev);
        }

        for (ExponentVector ev : take(LIMIT, filterInfinite(f -> f != ONE, P.exponentVectors()))) {
            List<Integer> exponents = ev.getExponents();
            assertTrue(ev, last(exponents) != 0);
        }
    }

    private void propertiesExponent() {
        initialize("exponent(int)");
        Iterable<Pair<ExponentVector, Variable>> ps = P.pairsLogarithmicOrder(P.exponentVectors(), P.variables());
        for (Pair<ExponentVector, Variable> p : take(LIMIT, ps)) {
            int exponent = p.a.exponent(p.b);
            assertEquals(p, exponent, lookup(p.b, p.a.terms()).orElse(0));
        }
    }

    private void propertiesSize() {
        initialize("size()");
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            int size = ev.size();
            assertEquals(ev, size, ev.getExponents().size());
            assertTrue(ev, size >= 0);
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
            inverse(u -> toList(u.terms()), ExponentVector::fromTerms, ev);
        }
    }

    private void propertiesOf() {
        initialize("of(List<Integer>)");
        for (List<Integer> is : take(LIMIT, P.withScale(4).lists(P.naturalIntegersGeometric()))) {
            ExponentVector ev = of(is);
            ev.validate();
        }

        Iterable<List<Integer>> iss = filterInfinite(
                is -> is.isEmpty() || last(is) != 0,
                P.lists(P.naturalIntegersGeometric())
        );
        for (List<Integer> is : take(LIMIT, iss)) {
            fixedPoint(js -> of(js).getExponents(), is);
        }

        iss = P.listsWithSublists(
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

    private void propertiesFromTerms() {
        initialize("fromTerms(List<Pair<Variable, Integer>>)");
        Iterable<List<Pair<Variable, Integer>>> pss = P.withElement(
                Collections.emptyList(),
                map(
                        p -> toList(zip(p.a, p.b)),
                        P.dependentPairsInfinite(
                                P.subsetsAtLeast(1, P.variables()),
                                vs -> filterInfinite(
                                        is -> last(is) != 0,
                                        P.lists(vs.size(), P.positiveIntegersGeometric())
                                )
                        )
                )
        );
        for (List<Pair<Variable, Integer>> ps : take(LIMIT, pss)) {
            ExponentVector ev = fromTerms(ps);
            ev.validate();
            inverse(ExponentVector::fromTerms, u -> toList(u.terms()), ps);
        }

        Iterable<List<Pair<Variable, Integer>>> pssFail = map(
                p -> toList(zip(p.a, p.b)),
                P.dependentPairsInfinite(
                        P.subsetsAtLeast(1, P.variables()),
                        vs -> filterInfinite(
                                is -> last(is) == 0 || any(i -> i <= 0, is),
                                P.lists(vs.size(), P.integersGeometric())
                        )
                )
        );
        for (List<Pair<Variable, Integer>> ps : take(LIMIT, pssFail)) {
            try {
                fromTerms(ps);
                fail(ps);
            } catch (IllegalArgumentException ignored) {}
        }

        pssFail = map(
                p -> toList(zip(p.a, p.b)),
                P.dependentPairsInfinite(
                        filterInfinite(is -> !increasing(is), P.listsAtLeast(1, P.variables())),
                        vs -> filterInfinite(is -> last(is) != 0, P.lists(vs.size(), P.integersGeometric()))
                )
        );
        for (List<Pair<Variable, Integer>> ps : take(LIMIT, pssFail)) {
            try {
                fromTerms(ps);
                fail(ps);
            } catch (IllegalArgumentException ignored) {}
        }

        pssFail = P.listsWithElement(null, P.pairs(P.variables(), P.positiveIntegers()));
        for (List<Pair<Variable, Integer>> ps : take(LIMIT, pssFail)) {
            try {
                fromTerms(ps);
                fail(ps);
            } catch (NullPointerException ignored) {}
        }

        pssFail = filterInfinite(
                ps -> any(p -> p.a == null || p.b == null, ps),
                P.lists(P.pairs(P.withScale(4).withNull(P.variables()), P.withScale(4).withNull(P.positiveIntegers())))
        );
        for (List<Pair<Variable, Integer>> ps : take(LIMIT, pssFail)) {
            try {
                fromTerms(ps);
                fail(ps);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }
    }
}
