package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
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
        propertiesOf_List_Integer();
        propertiesOf_Variable();
        propertiesFromTerms();
        propertiesDegree();
        propertiesVariables();
        propertiesVariableCount();
        compareImplementationsVariableCount();
        propertiesRemoveVariable();
        propertiesRemoveVariables();
        compareImplementationsRemoveVariables();
        propertiesRetainVariables();
        compareImplementationsRetainVariables();
        propertiesMultiply();
        propertiesProduct();
        compareImplementationsProduct();
        propertiesPow();
        compareImplementationsPow();
        propertiesApplyBigInteger();
        propertiesApplyRational();
        propertiesSubstitute();
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

    private void propertiesOf_List_Integer() {
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

    private void propertiesOf_Variable() {
        initialize("of(Variable)");
        for (Variable v : take(LIMIT, P.variables())) {
            Monomial m = of(v);
            m.validate();
            assertEquals(v, m.variableCount(), 1);
            assertEquals(v, m.degree(), 1);
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

    private static int variableCount_simplest(@NotNull Monomial m) {
        return m.variables().size();
    }

    private void propertiesVariableCount() {
        initialize("variableCount()");
        for (Monomial m : take(LIMIT, P.monomials())) {
            int count = m.variableCount();
            assertEquals(m, count, variableCount_simplest(m));
            assertTrue(m, count >= 0);
        }
    }

    private void compareImplementationsVariableCount() {
        Map<String, Function<Monomial, Integer>> functions = new LinkedHashMap<>();
        functions.put("simplest", MonomialProperties::variableCount_simplest);
        functions.put("standard", Monomial::variableCount);
        compareImplementations("variableCount()", take(LIMIT, P.monomials()), functions);
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

    private static @NotNull Monomial removeVariables_alt(@NotNull Monomial m, @NotNull List<Variable> vs) {
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

    private static @NotNull Monomial removeVariables_alt2(@NotNull Monomial m, @NotNull List<Variable> vs) {
        List<Variable> us = m.variables();
        us.removeAll(vs);
        return m.retainVariables(us);
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
            assertEquals(p, removeVariables_alt2(p.a, p.b), removed);
        }

        for (List<Variable> vs : take(LIMIT, P.lists(P.variables()))) {
            fixedPoint(e -> e.removeVariables(vs), ONE);
        }

        for (Monomial m : take(LIMIT, P.monomials())) {
            fixedPoint(n -> n.removeVariables(Collections.emptyList()), m);
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
        functions.put("alt2", p -> removeVariables_alt2(p.a, p.b));
        functions.put("standard", p -> p.a.removeVariables(p.b));
        Iterable<Pair<Monomial, List<Variable>>> ps = P.pairsLogarithmicOrder(
                P.monomials(),
                P.lists(P.variables())
        );
        compareImplementations("removeVariables(List<Variable>)", take(LIMIT, ps), functions);
    }

    private static @NotNull Monomial retainVariables_alt(@NotNull Monomial m, @NotNull List<Variable> vs) {
        List<Variable> us = m.variables();
        us.removeAll(vs);
        return m.removeVariables(us);
    }

    private void propertiesRetainVariables() {
        initialize("retainVariables(List<Variable>)");
        Iterable<Pair<Monomial, List<Variable>>> ps = P.pairsLogarithmicOrder(
                P.monomials(),
                P.lists(P.variables())
        );
        for (Pair<Monomial, List<Variable>> p : take(LIMIT, ps)) {
            Monomial retained = p.a.retainVariables(p.b);
            retained.validate();
            assertEquals(p, retainVariables_alt(p.a, p.b), retained);
        }

        for (List<Variable> vs : take(LIMIT, P.lists(P.variables()))) {
            fixedPoint(e -> e.retainVariables(vs), ONE);
        }

        for (Monomial m : take(LIMIT, P.monomials())) {
            fixedPoint(n -> n.retainVariables(n.variables()), m);
            assertEquals(m, m.retainVariables(Collections.emptyList()), ONE);
        }

        Iterable<Pair<Monomial, List<Variable>>> psFail = P.pairs(
                P.monomials(),
                P.listsWithElement(null, P.variables())
        );
        for (Pair<Monomial, List<Variable>> p : take(LIMIT, psFail)) {
            try {
                p.a.retainVariables(p.b);
                fail(p);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsRetainVariables() {
        Map<String, Function<Pair<Monomial, List<Variable>>, Monomial>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> retainVariables_alt(p.a, p.b));
        functions.put("standard", p -> p.a.retainVariables(p.b));
        Iterable<Pair<Monomial, List<Variable>>> ps = P.pairsLogarithmicOrder(
                P.monomials(),
                P.lists(P.variables())
        );
        compareImplementations("retainVariables(List<Variable>)", take(LIMIT, ps), functions);
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

        Iterable<Triple<Monomial, Monomial, Map<Variable, BigInteger>>> ts = map(
                r -> new Triple<>(r.a.a, r.a.b, r.b),
                P.dependentPairsInfinite(
                        filterInfinite(q -> q.a != ONE || q.b != ONE, P.pairs(P.monomials())),
                        p -> P.maps(sort(nub(concat(p.a.variables(), p.b.variables()))), P.bigIntegers())
                )
        );
        for (Triple<Monomial, Monomial, Map<Variable, BigInteger>> t : take(LIMIT, ts)) {
            Monomial product = t.a.multiply(t.b);
            assertEquals(t, t.a.applyBigInteger(t.c).multiply(t.b.applyBigInteger(t.c)), product.applyBigInteger(t.c));
        }
    }

    private static @NotNull Monomial product_simplest(@NotNull Iterable<Monomial> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        return foldl(Monomial::multiply, ONE, xs);
    }

    private static @NotNull Monomial product_alt(@NotNull Iterable<Monomial> xs) {
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

        Iterable<Pair<List<Monomial>, Map<Variable, BigInteger>>> ps = P.dependentPairsInfinite(
                filterInfinite(ms -> any(m -> m != ONE, ms), P.withScale(1).lists(P.monomials())),
                ns -> P.maps(sort(nub(concat(map(Monomial::variables, ns)))), P.bigIntegers())
        );
        for (Pair<List<Monomial>, Map<Variable, BigInteger>> p : take(LIMIT, ps)) {
            Monomial product = product(p.a);
            assertEquals(
                    p,
                    productBigInteger(toList(map(m -> m.applyBigInteger(p.b), p.a))),
                    product.applyBigInteger(p.b)
            );
        }
    }

    private void compareImplementationsProduct() {
        Map<String, Function<List<Monomial>, Monomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", MonomialProperties::product_simplest);
        functions.put("alt", MonomialProperties::product_alt);
        functions.put("standard", Monomial::product);
        compareImplementations("product(List<Monomial>)", take(LIMIT, P.lists(P.monomials())), functions);
    }

    private static @NotNull Monomial pow_simplest(@NotNull Monomial ev, int p) {
        return product(toList(replicate(p, ev)));
    }

    private static @NotNull Monomial pow_alt(@NotNull Monomial m, int p) {
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

        Iterable<Triple<Monomial, Integer, Integer>> ts = P.triples(
                P.monomials(),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<Monomial, Integer, Integer> t : take(LIMIT, ts)) {
            Monomial expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            Monomial expression2 = t.a.pow(t.b + t.c);
            assertEquals(t, expression1, expression2);
            Monomial expression5 = t.a.pow(t.b).pow(t.c);
            Monomial expression6 = t.a.pow(t.b * t.c);
            assertEquals(t, expression5, expression6);
        }

        Iterable<Triple<Monomial, Monomial, Integer>> ts2 = P.triples(
                P.monomials(),
                P.monomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<Monomial, Monomial, Integer> t : take(LIMIT, ts2)) {
            Monomial expression1 = t.a.multiply(t.b).pow(t.c);
            Monomial expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        Iterable<Triple<Monomial, Integer, Map<Variable, BigInteger>>> ts3 = map(
                p -> new Triple<>(p.a.a, p.b, p.a.b),
                P.pairsLogarithmicOrder(
                        P.dependentPairsInfinite(
                                filterInfinite(m -> m != ONE, P.monomials()),
                                n -> P.maps(n.variables(), P.bigIntegers())
                        ),
                        P.withScale(4).naturalIntegersGeometric()
                )
        );
        for (Triple<Monomial, Integer, Map<Variable, BigInteger>> t : take(LIMIT, ts3)) {
            Monomial m = t.a.pow(t.b);
            assertEquals(t, t.a.applyBigInteger(t.c).pow(t.b), m.applyBigInteger(t.c));
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

    private void propertiesApplyBigInteger() {
        initialize("applyBigInteger(Map<Variable, BigInteger>)");
        Iterable<Pair<Monomial, Map<Variable, BigInteger>>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = cons(
                    new Pair<>(ONE, new TreeMap<>()),
                    P.dependentPairsInfiniteSquareRootOrder(
                            filterInfinite(n -> n != ONE, P.monomials()),
                            m -> {
                                List<Variable> us = toList(m.variables());
                                return map(
                                        p -> p.b,
                                        P.dependentPairsInfiniteLogarithmicOrder(
                                                nub(map(vs -> sort(nub(concat(vs, us))), P.subsets(P.variables()))),
                                                ws -> P.maps(ws, P.bigIntegers())
                                        )
                                );
                            }
                    )
            );
        } else {
            ps = P.withElement(
                    new Pair<>(ONE, new TreeMap<>()),
                    P.dependentPairsInfinite(
                            filterInfinite(n -> n != ONE, P.monomials()),
                            m -> {
                                List<Variable> us = toList(m.variables());
                                return map(
                                        p -> p.b,
                                        P.dependentPairsInfiniteLogarithmicOrder(
                                                map(vs -> sort(nub(concat(vs, us))), P.subsets(P.variables())),
                                                ws -> P.maps(ws, P.bigIntegers())
                                        )
                                );
                            }
                    )
            );
        }
        for (Pair<Monomial, Map<Variable, BigInteger>> p : take(LIMIT, ps)) {
            p.a.applyBigInteger(p.b);
        }

        Iterable<Pair<Monomial, Map<Variable, BigInteger>>> psFail = P.dependentPairsInfiniteSquareRootOrder(
                filterInfinite(n -> n != ONE, P.monomials()),
                m -> {
                    List<Variable> us = toList(m.variables());
                    return map(
                            p -> p.b,
                            P.dependentPairsInfiniteLogarithmicOrder(
                                    filterInfinite(vs -> !isSubsetOf(us, vs), P.subsetsAtLeast(1, P.variables())),
                                    ws -> P.maps(ws, P.bigIntegers())
                            )
                    );
                }
        );
        for (Pair<Monomial, Map<Variable, BigInteger>> p : take(LIMIT, psFail)) {
            try {
                p.a.applyBigInteger(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesApplyRational() {
        initialize("applyRational(Map<Variable, Rational>)");
        Iterable<Pair<Monomial, Map<Variable, Rational>>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = cons(
                    new Pair<>(ONE, new TreeMap<>()),
                    P.dependentPairsInfiniteSquareRootOrder(
                            filterInfinite(n -> n != ONE, P.monomials()),
                            m -> {
                                List<Variable> us = toList(m.variables());
                                return map(
                                        p -> p.b,
                                        P.dependentPairsInfiniteLogarithmicOrder(
                                                nub(map(vs -> sort(nub(concat(vs, us))), P.subsets(P.variables()))),
                                                ws -> P.maps(ws, P.rationals())
                                        )
                                );
                            }
                    )
            );
        } else {
            ps = P.withElement(
                    new Pair<>(ONE, new TreeMap<>()),
                    P.dependentPairsInfinite(
                            filterInfinite(n -> n != ONE, P.monomials()),
                            m -> {
                                List<Variable> us = toList(m.variables());
                                return map(
                                        p -> p.b,
                                        P.dependentPairsInfiniteLogarithmicOrder(
                                                map(vs -> sort(nub(concat(vs, us))), P.subsets(P.variables())),
                                                ws -> P.maps(ws, P.rationals())
                                        )
                                );
                            }
                    )
            );
        }
        for (Pair<Monomial, Map<Variable, Rational>> p : take(LIMIT, ps)) {
            p.a.applyRational(p.b);
        }

        Iterable<Pair<Monomial, Map<Variable, Rational>>> psFail = P.dependentPairsInfiniteSquareRootOrder(
                filterInfinite(n -> n != ONE, P.monomials()),
                m -> {
                    List<Variable> us = toList(m.variables());
                    return map(
                            p -> p.b,
                            P.dependentPairsInfiniteLogarithmicOrder(
                                    filterInfinite(vs -> !isSubsetOf(us, vs), P.subsetsAtLeast(1, P.variables())),
                                    ws -> P.maps(ws, P.rationals())
                            )
                    );
                }
        );
        for (Pair<Monomial, Map<Variable, Rational>> p : take(LIMIT, psFail)) {
            try {
                p.a.applyRational(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesSubstitute() {
        initialize("substitute(Map<Variable, Monomial>)");
        Iterable<Pair<Monomial, Map<Variable, Monomial>>> ps = P.pairsSquareRootOrder(
                P.withScale(4).monomials(),
                P.withElement(
                        new TreeMap<>(),
                        map(
                                p -> p.b,
                                P.dependentPairsInfiniteLogarithmicOrder(
                                        P.withScale(4).subsetsAtLeast(1, P.variables()),
                                        vs -> P.maps(vs, P.withScale(4).monomials())
                                )
                        )
                )
        );
        for (Pair<Monomial, Map<Variable, Monomial>> p : take(LIMIT, ps)) {
            Monomial m = p.a.substitute(p.b);
            m.validate();
        }
    }

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
