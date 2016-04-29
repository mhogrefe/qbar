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

import static mho.qbar.objects.Monomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

public class MonomialProperties extends QBarTestProperties {
    private static final @NotNull String MONOMIAL_CHARS = "*0123456789^abcdefghijklmnopqrstuvwxyz";

    public MonomialProperties() {
        super("Monomial");
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
        for (Monomial m : take(LIMIT, P.monomials())) {
            List<Integer> exponents = m.getExponents();
            assertTrue(m, all(i -> i >= 0, exponents));
            //noinspection Convert2MethodRef
            inverse(Monomial::getExponents, (List<Integer> is) -> of(is), m);
        }

        for (Monomial m : take(LIMIT, filterInfinite(f -> f != ONE, P.monomials()))) {
            List<Integer> exponents = m.getExponents();
            assertTrue(m, last(exponents) != 0);
        }
    }

    private void propertiesExponent() {
        initialize("exponent(int)");
        Iterable<Pair<Monomial, Variable>> ps = P.pairsLogarithmicOrder(P.monomials(), P.variables());
        for (Pair<Monomial, Variable> p : take(LIMIT, ps)) {
            int exponent = p.a.exponent(p.b);
            assertEquals(p, exponent, lookup(p.b, p.a.terms()).orElse(0));
        }
    }

    private void propertiesSize() {
        initialize("size()");
        for (Monomial m : take(LIMIT, P.monomials())) {
            int size = m.size();
            assertEquals(m, size, m.getExponents().size());
            assertTrue(m, size >= 0);
        }
    }

    private void propertiesTerms() {
        initialize("terms()");
        for (Monomial m : take(LIMIT, P.monomials())) {
            Iterable<Pair<Variable, Integer>> termsIterable = m.terms();
            testNoRemove(termsIterable);
            testHasNext(termsIterable);
            List<Pair<Variable, Integer>> terms = toList(termsIterable);
            assertTrue(m, all(t -> t.b > 0, terms));
            //noinspection RedundantCast
            assertTrue(m, increasing((Iterable<Variable>) map(t -> t.a, terms)));
            inverse(u -> toList(u.terms()), Monomial::fromTerms, m);
        }
    }

    private void propertiesOf() {
        initialize("of(List<Integer>)");
        for (List<Integer> is : take(LIMIT, P.withScale(4).lists(P.naturalIntegersGeometric()))) {
            Monomial m = of(is);
            m.validate();
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
            Monomial m = fromTerms(ps);
            m.validate();
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
            inverse(Monomial::fromTerms, u -> toList(u.terms()), ps);
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
        for (Monomial m : take(LIMIT, P.monomials())) {
            int degree = m.degree();
            assertTrue(m, degree >= 0);
        }
    }

    private void propertiesVariables() {
        initialize("variables()");
        for (Monomial m : take(LIMIT, P.monomials())) {
            List<Variable> variables = m.variables();
            assertTrue(m, increasing(variables));
            String s = m.toString();
            for (Variable v : variables) {
                assertTrue(m, s.contains(v.toString()));
            }
        }
    }

    private void propertiesRemoveVariable() {
        initialize("removeVariable(Variable)");
        Iterable<Pair<Monomial, Variable>> ps = P.pairsLogarithmicOrder(P.monomials(), P.variables());
        for (Pair<Monomial, Variable> p : take(LIMIT, ps)) {
            Monomial removed = p.a.removeVariable(p.b);
            removed.validate();
        }

        for (Variable v : take(LIMIT, P.variables())) {
            fixedPoint(e -> e.removeVariable(v), ONE);
        }
    }

    private static @NotNull
    Monomial removeVariables_alt(
            @NotNull Monomial m,
            @NotNull List<Variable> vs
    ) {
        if (any(v -> v == null, vs)) {
            throw new NullPointerException();
        }
        if (m == ONE) return m;
        vs = toList(filter(v -> m.exponent(v) != 0, vs));
        if (vs.isEmpty()) return m;
        List<Integer> removedExponents = m.getExponents();
        for (Variable v : vs) {
            removedExponents.set(v.getIndex(), 0);
        }
        return of(removedExponents);
    }

    private void propertiesRemoveVariables() {
        initialize("removeVariables(List<Variable>)");
        Iterable<Pair<Monomial, List<Variable>>> ps = P.pairsLogarithmicOrder(
                P.monomials(),
                P.lists(P.variables())
        );
        for (Pair<Monomial, List<Variable>> p : take(LIMIT, ps)) {
            Monomial removed = p.a.removeVariables(p.b);
            removed.validate();
            assertEquals(p, removeVariables_alt(p.a, p.b), removed);
        }

        for (List<Variable> vs : take(LIMIT, P.lists(P.variables()))) {
            fixedPoint(e -> e.removeVariables(vs), ONE);
        }

        for (Monomial m : take(LIMIT, P.monomials())) {
            assertEquals(m, m.removeVariables(m.variables()), ONE);
        }

        Iterable<Pair<Monomial, List<Variable>>> psFail = P.pairs(
                P.monomials(),
                P.listsWithElement(null, P.variables())
        );
        for (Pair<Monomial, List<Variable>> p : take(LIMIT, psFail)) {
            try {
                p.a.removeVariables(p.b);
                fail(p);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsRemoveVariables() {
        Map<String, Function<Pair<Monomial, List<Variable>>, Monomial>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> removeVariables_alt(p.a, p.b));
        functions.put("standard", p -> p.a.removeVariables(p.b));
        Iterable<Pair<Monomial, List<Variable>>> ps = P.pairsLogarithmicOrder(
                P.monomials(),
                P.lists(P.variables())
        );
        compareImplementations("removeVariables(List<Variable>)", take(LIMIT, ps), functions);
    }

    private void propertiesMultiply() {
        initialize("multiply(Monomial)");
        for (Pair<Monomial, Monomial> p : take(LIMIT, P.pairs(P.monomials()))) {
            Monomial product = p.a.multiply(p.b);
            product.validate();
            commutative(Monomial::multiply, p);
        }

        for (Monomial m : take(LIMIT, P.monomials())) {
            fixedPoint(ONE::multiply, m);
            fixedPoint(f -> f.multiply(ONE), m);
        }

        for (Triple<Monomial, Monomial, Monomial> t : take(LIMIT, P.triples(P.monomials()))) {
            associative(Monomial::multiply, t);
        }
    }

    private static @NotNull
    Monomial product_simplest(@NotNull Iterable<Monomial> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        return foldl(Monomial::multiply, ONE, xs);
    }

    private static @NotNull
    Monomial product_alt(@NotNull Iterable<Monomial> xs) {
        return of(toList(map(IterableUtils::sumInteger, transpose(map(Monomial::getExponents, xs)))));
    }

    private void propertiesProduct() {
        initialize("product(List<Monomial>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.monomials(),
                Monomial::multiply,
                Monomial::product,
                Monomial::validate,
                true,
                true
        );

        for (List<Monomial> evs : take(LIMIT, P.withScale(1).lists(P.monomials()))) {
            Monomial product = product(evs);
            assertEquals(evs, product, product_simplest(evs));
            assertEquals(evs, product, product_alt(evs));
        }
    }

    private void compareImplementationsProduct() {
        Map<String, Function<List<Monomial>, Monomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", MonomialProperties::product_simplest);
        functions.put("alt", MonomialProperties::product_alt);
        functions.put("standard", Monomial::product);
        compareImplementations("product(List<Monomial>)", take(LIMIT, P.lists(P.monomials())), functions);
    }

    private static @NotNull
    Monomial pow_simplest(@NotNull Monomial ev, int p) {
        return product(toList(replicate(p, ev)));
    }

    private static @NotNull
    Monomial pow_alt(@NotNull Monomial m, int p) {
        if (p < 0) {
            throw new ArithmeticException("p cannot be negative. Invalid p: " + p);
        }
        if (p == 0 || m == ONE) return ONE;
        if (p == 1) return m;
        Monomial powerPower = null; // p^2^i
        List<Monomial> factors = new ArrayList<>();
        for (boolean bit : IntegerUtils.bits(p)) {
            powerPower = powerPower == null ? m : powerPower.multiply(powerPower);
            if (bit) factors.add(powerPower);
        }
        return product(factors);
    }

    private void propertiesPow() {
        initialize("pow(int)");
        Iterable<Pair<Monomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.monomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<Monomial, Integer> p : take(LIMIT, ps)) {
            Monomial m = p.a.pow(p.b);
            m.validate();
            assertEquals(p, m, pow_simplest(p.a, p.b));
            assertEquals(p, m, pow_alt(p.a, p.b));
        }

        for (Monomial m : take(LIMIT, P.monomials())) {
            assertTrue(m, m.pow(0) == ONE);
            fixedPoint(f -> f.pow(1), m);
            assertEquals(m, m.pow(2), m.multiply(m));
        }

        Iterable<Triple<Monomial, Integer, Integer>> ts2 = P.triples(
                P.monomials(),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<Monomial, Integer, Integer> t : take(LIMIT, ts2)) {
            Monomial expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            Monomial expression2 = t.a.pow(t.b + t.c);
            assertEquals(t, expression1, expression2);
            Monomial expression5 = t.a.pow(t.b).pow(t.c);
            Monomial expression6 = t.a.pow(t.b * t.c);
            assertEquals(t, expression5, expression6);
        }

        Iterable<Triple<Monomial, Monomial, Integer>> ts3 = P.triples(
                P.monomials(),
                P.monomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<Monomial, Monomial, Integer> t : take(LIMIT, ts3)) {
            Monomial expression1 = t.a.multiply(t.b).pow(t.c);
            Monomial expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        for (Pair<Monomial, Integer> p : take(LIMIT, P.pairs(P.monomials(), P.negativeIntegers()))) {
            try {
                p.a.pow(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsPow() {
        Map<String, Function<Pair<Monomial, Integer>, Monomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> pow_simplest(p.a, p.b));
        functions.put("alt", p -> pow_alt(p.a, p.b));
        functions.put("standard", p -> p.a.pow(p.b));
        Iterable<Pair<Monomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.monomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        compareImplementations("pow(int)", take(LIMIT, ps), functions);
    }

    //todo continue props

    private void propertiesEquals() {
        initialize("equals(Object)");
        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::monomials);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::monomials);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(Monomial)");
        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::monomials);
    }

    private void propertiesReadStrict() {
        initialize("readStrict(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                MONOMIAL_CHARS,
                P.monomials(),
                Monomial::readStrict,
                Monomial::validate,
                false,
                true
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, MONOMIAL_CHARS, P.monomials(), Monomial::readStrict);
    }
}
