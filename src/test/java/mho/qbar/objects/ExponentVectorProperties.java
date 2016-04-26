package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

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
        propertiesRemoveVariable();
        propertiesRemoveVariables();
        compareImplementationsRemoveVariables();
        propertiesMultiply();
        propertiesProduct();
        compareImplementationsProduct();
        propertiesPow();
        compareImplementationsPow();
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

    private void propertiesRemoveVariable() {
        initialize("removeVariable(Variable)");
        Iterable<Pair<ExponentVector, Variable>> ps = P.pairsLogarithmicOrder(P.exponentVectors(), P.variables());
        for (Pair<ExponentVector, Variable> p : take(LIMIT, ps)) {
            ExponentVector removed = p.a.removeVariable(p.b);
            removed.validate();
        }

        for (Variable v : take(LIMIT, P.variables())) {
            fixedPoint(e -> e.removeVariable(v), ONE);
        }
    }

    private static @NotNull ExponentVector removeVariables_alt(
            @NotNull ExponentVector ev,
            @NotNull List<Variable> vs
    ) {
        if (any(v -> v == null, vs)) {
            throw new NullPointerException();
        }
        if (ev == ONE) return ev;
        vs = toList(filter(v -> ev.exponent(v) != 0, vs));
        if (vs.isEmpty()) return ev;
        List<Integer> removedExponents = ev.getExponents();
        for (Variable v : vs) {
            removedExponents.set(v.getIndex(), 0);
        }
        return of(removedExponents);
    }

    private void propertiesRemoveVariables() {
        initialize("removeVariables(List<Variable>)");
        Iterable<Pair<ExponentVector, List<Variable>>> ps = P.pairsLogarithmicOrder(
                P.exponentVectors(),
                P.lists(P.variables())
        );
        for (Pair<ExponentVector, List<Variable>> p : take(LIMIT, ps)) {
            ExponentVector removed = p.a.removeVariables(p.b);
            removed.validate();
            assertEquals(p, removeVariables_alt(p.a, p.b), removed);
        }

        for (List<Variable> vs : take(LIMIT, P.lists(P.variables()))) {
            fixedPoint(e -> e.removeVariables(vs), ONE);
        }

        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            assertEquals(ev, ev.removeVariables(ev.variables()), ONE);
        }

        Iterable<Pair<ExponentVector, List<Variable>>> psFail = P.pairs(
                P.exponentVectors(),
                P.listsWithElement(null, P.variables())
        );
        for (Pair<ExponentVector, List<Variable>> p : take(LIMIT, psFail)) {
            try {
                p.a.removeVariables(p.b);
                fail(p);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsRemoveVariables() {
        Map<String, Function<Pair<ExponentVector, List<Variable>>, ExponentVector>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> removeVariables_alt(p.a, p.b));
        functions.put("standard", p -> p.a.removeVariables(p.b));
        Iterable<Pair<ExponentVector, List<Variable>>> ps = P.pairsLogarithmicOrder(
                P.exponentVectors(),
                P.lists(P.variables())
        );
        compareImplementations("removeVariables(List<Variable>)", take(LIMIT, ps), functions);
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

    private static @NotNull ExponentVector product_simplest(@NotNull Iterable<ExponentVector> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        return foldl(ExponentVector::multiply, ONE, xs);
    }

    private static @NotNull ExponentVector product_alt(@NotNull Iterable<ExponentVector> xs) {
        return of(toList(map(IterableUtils::sumInteger, transpose(map(ExponentVector::getExponents, xs)))));
    }

    private void propertiesProduct() {
        initialize("product(List<ExponentVector>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.exponentVectors(),
                ExponentVector::multiply,
                ExponentVector::product,
                ExponentVector::validate,
                true,
                true
        );

        for (List<ExponentVector> evs : take(LIMIT, P.withScale(1).lists(P.exponentVectors()))) {
            ExponentVector product = product(evs);
            assertEquals(evs, product, product_simplest(evs));
            assertEquals(evs, product, product_alt(evs));
        }
    }

    private void compareImplementationsProduct() {
        Map<String, Function<List<ExponentVector>, ExponentVector>> functions = new LinkedHashMap<>();
        functions.put("simplest", ExponentVectorProperties::product_simplest);
        functions.put("alt", ExponentVectorProperties::product_alt);
        functions.put("standard", ExponentVector::product);
        compareImplementations("product(List<ExponentVector>)", take(LIMIT, P.lists(P.exponentVectors())), functions);
    }

    private static @NotNull ExponentVector pow_simplest(@NotNull ExponentVector ev, int p) {
        return product(toList(replicate(p, ev)));
    }

    private static @NotNull ExponentVector pow_alt(@NotNull ExponentVector ev, int p) {
        if (p < 0) {
            throw new ArithmeticException("p cannot be negative. Invalid p: " + p);
        }
        if (p == 0 || ev == ONE) return ONE;
        if (p == 1) return ev;
        ExponentVector powerPower = null; // p^2^i
        List<ExponentVector> factors = new ArrayList<>();
        for (boolean bit : IntegerUtils.bits(p)) {
            powerPower = powerPower == null ? ev : powerPower.multiply(powerPower);
            if (bit) factors.add(powerPower);
        }
        return product(factors);
    }

    private void propertiesPow() {
        initialize("pow(int)");
        Iterable<Pair<ExponentVector, Integer>> ps = P.pairsLogarithmicOrder(
                P.exponentVectors(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<ExponentVector, Integer> p : take(LIMIT, ps)) {
            ExponentVector ev = p.a.pow(p.b);
            ev.validate();
            assertEquals(p, ev, pow_simplest(p.a, p.b));
            assertEquals(p, ev, pow_alt(p.a, p.b));
        }

        for (ExponentVector ev : take(LIMIT, P.exponentVectors())) {
            assertTrue(ev, ev.pow(0) == ONE);
            fixedPoint(f -> f.pow(1), ev);
            assertEquals(ev, ev.pow(2), ev.multiply(ev));
        }

        Iterable<Triple<ExponentVector, Integer, Integer>> ts2 = P.triples(
                P.exponentVectors(),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<ExponentVector, Integer, Integer> t : take(LIMIT, ts2)) {
            ExponentVector expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            ExponentVector expression2 = t.a.pow(t.b + t.c);
            assertEquals(t, expression1, expression2);
            ExponentVector expression5 = t.a.pow(t.b).pow(t.c);
            ExponentVector expression6 = t.a.pow(t.b * t.c);
            assertEquals(t, expression5, expression6);
        }

        Iterable<Triple<ExponentVector, ExponentVector, Integer>> ts3 = P.triples(
                P.exponentVectors(),
                P.exponentVectors(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<ExponentVector, ExponentVector, Integer> t : take(LIMIT, ts3)) {
            ExponentVector expression1 = t.a.multiply(t.b).pow(t.c);
            ExponentVector expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        for (Pair<ExponentVector, Integer> p : take(LIMIT, P.pairs(P.exponentVectors(), P.negativeIntegers()))) {
            try {
                p.a.pow(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsPow() {
        Map<String, Function<Pair<ExponentVector, Integer>, ExponentVector>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> pow_simplest(p.a, p.b));
        functions.put("alt", p -> pow_alt(p.a, p.b));
        functions.put("standard", p -> p.a.pow(p.b));
        Iterable<Pair<ExponentVector, Integer>> ps = P.pairsLogarithmicOrder(
                P.exponentVectors(),
                P.withScale(4).naturalIntegersGeometric()
        );
        compareImplementations("pow(int)", take(LIMIT, ps), functions);
    }

    //todo continue props

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
                false,
                true
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, EXPONENT_VECTOR_CHARS, P.exponentVectors(), ExponentVector::readStrict);
    }
}
