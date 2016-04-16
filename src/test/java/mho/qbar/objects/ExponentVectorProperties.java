package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
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
        propertiesDegree();
        propertiesVariables();
        propertiesMultiply();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesReadStrict();
        propertiesToString();
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
                                P.listsAtLeast(1, P.variables()),
                                vs -> P.lists(vs.size(), P.positiveIntegersGeometric())
                        )
                )
        );
        for (List<Pair<Variable, Integer>> ps : take(LIMIT, pss)) {
            ExponentVector ev = fromTerms(ps);
            ev.validate();
        }

        pss = P.withElement(
                Collections.emptyList(),
                map(
                        p -> toList(zip(p.a, p.b)),
                        P.dependentPairsInfinite(
                                P.subsetsAtLeast(1, P.variables()),
                                vs -> P.lists(vs.size(), P.positiveIntegersGeometric())
                        )
                )
        );
        for (List<Pair<Variable, Integer>> ps : take(LIMIT, pss)) {
            inverse(ExponentVector::fromTerms, u -> toList(u.terms()), ps);
        }

        Iterable<List<Pair<Variable, Integer>>> pssFail = map(
                p -> toList(zip(p.a, p.b)),
                P.dependentPairsInfinite(
                        P.listsAtLeast(1, P.variables()),
                        vs -> filterInfinite(is -> any(i -> i <= 0, is), P.lists(vs.size(), P.integersGeometric()))
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

    private void propertiesDegree() {
        initialize("degree()");
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            int degree = ev.degree();
            assertTrue(ev, degree >= 0);
        }
    }

    private void propertiesVariables() {
        initialize("variables()");
        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            List<Variable> variables = ev.variables();
            assertTrue(ev, increasing(variables));
            String s = ev.toString();
            for (Variable v : variables) {
                assertTrue(ev, s.contains(v.toString()));
            }
        }
    }

    private void propertiesMultiply() {
        initialize("multiply(ExponentVector)");
        for (Pair<ExponentVector, ExponentVector> p : take(LIMIT, P.pairs(P.exponentVectors()))) {
            ExponentVector product = p.a.multiply(p.b);
            product.validate();
            commutative(ExponentVector::multiply, p);
        }

        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            fixedPoint(ONE::multiply, ev);
            fixedPoint(f -> f.multiply(ONE), ev);
        }

        for (Triple<ExponentVector, ExponentVector, ExponentVector> t : take(LIMIT, P.triples(P.exponentVectors()))) {
            associative(ExponentVector::multiply, t);
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::exponentVectors);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::exponentVectors);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(ExponentVector)");
        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::exponentVectors);
    }

    private void propertiesReadStrict() {
        initialize("readStrict(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                EXPONENT_VECTOR_CHARS,
                P.exponentVectors(),
                ExponentVector::readStrict,
                ExponentVector::validate,
                false
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, EXPONENT_VECTOR_CHARS, P.exponentVectors(), ExponentVector::readStrict);
    }
}
