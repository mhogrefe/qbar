package mho.qbar.objects;

import mho.qbar.testing.QBarTestProperties;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.qbar.objects.RationalMultivariatePolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

public class RationalMultivariatePolynomialProperties extends QBarTestProperties {
    private static final @NotNull String RATIONAL_MULTIVARIATE_POLYNOMIAL_CHARS =
            "*+-/0123456789^abcdefghijklmnopqrstuvwxyz";

    private static final @NotNull Comparator<RationalMultivariatePolynomial> TERM_SHORTLEX_COMPARATOR = (p, q) -> {
        int c = Integer.compare(p.termCount(), q.termCount());
        if (c != 0) return c;
        return p.compareTo(q);
    };

    public RationalMultivariatePolynomialProperties() {
        super("RationalMultivariatePolynomial");
    }

    @Override
    protected void testBothModes() {
        propertiesIterable();
        propertiesIterator();
        propertiesOnlyHasIntegralCoefficients();
        propertiesToMultivariatePolynomial();
        propertiesCoefficient();
        propertiesOf_List_Pair_Monomial_Rational();
        propertiesOf_Monomial_Rational();
        compareImplementationsOf_Monomial_Rational();
        propertiesOf_Rational();
        propertiesOf_BigInteger();
        propertiesOf_int();
        propertiesOf_Variable();
        propertiesOf_RationalPolynomial_Variable();
        propertiesToRationalPolynomial();
        propertiesVariables();
        propertiesVariableCount();
        propertiesTermCount();
        propertiesMaxCoefficientBitLength();
        propertiesDegree();
        propertiesDegree_Variable();
        propertiesIsHomogeneous();
        propertiesCoefficientsOfVariable();
        propertiesGroupVariables_List_Variable_MonomialOrder();
        propertiesGroupVariables_List_Variable();
    }

    private void propertiesIterable() {
        initialize("iterable(MonomialOrder)");
        Iterable<Pair<RationalMultivariatePolynomial, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                P.rationalMultivariatePolynomials(),
                P.monomialOrders()
        );
        for (Pair<RationalMultivariatePolynomial, MonomialOrder> p : take(LIMIT, ps)) {
            Iterable<Pair<Monomial, Rational>> termIterable = p.a.iterable(p.b);
            List<Pair<Monomial, Rational>> terms = toList(termIterable);
            assertFalse(p, any(t -> t == null || t.a == null || t.b == null, terms));
            //noinspection RedundantCast
            assertTrue(p, increasing(p.b, (Iterable<Monomial>) map(t -> t.a, terms)));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<Pair<Monomial, Rational>> ts) -> of(ts), p.a);
            testNoRemove(termIterable);
            testHasNext(termIterable);
        }
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            List<Pair<Monomial, Rational>> terms = toList(p);
            assertFalse(p, any(t -> t == null || t.a == null || t.b == null, terms));
            //noinspection RedundantCast
            assertTrue(p, increasing((Iterable<Monomial>) map(t -> t.a, terms)));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<Pair<Monomial, Rational>> ts) -> of(ts), p);
            testNoRemove(p);
            testHasNext(p);
        }
    }

    private void propertiesOnlyHasIntegralCoefficients() {
        initialize("onlyHasIntegralCoefficients()");
        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            p.onlyHasIntegralCoefficients();
        }

        Iterable<RationalMultivariatePolynomial> ps = map(
                MultivariatePolynomial::toRationalMultivariatePolynomial,
                P.multivariatePolynomials()
        );
        for (RationalMultivariatePolynomial p : take(LIMIT, ps)) {
            assertTrue(p, p.onlyHasIntegralCoefficients());
        }
    }

    private void propertiesToMultivariatePolynomial() {
        initialize("toMultivariatePolynomial()");
        Iterable<RationalMultivariatePolynomial> ps = map(
                MultivariatePolynomial::toRationalMultivariatePolynomial,
                P.multivariatePolynomials()
        );
        for (RationalMultivariatePolynomial p : take(LIMIT, ps)) {
            MultivariatePolynomial rp = p.toMultivariatePolynomial();
            assertEquals(p, p.toString(), rp.toString());
            assertEquals(p, p.degree(), rp.degree());
            inverse(
                    RationalMultivariatePolynomial::toMultivariatePolynomial,
                    MultivariatePolynomial::toRationalMultivariatePolynomial,
                    p
            );
        }

        Iterable<Pair<RationalMultivariatePolynomial, Map<Variable, Rational>>> ps2 = P.dependentPairsInfinite(
                filterInfinite(q -> q.degree() > 0, ps),
                p -> P.maps(p.variables(), P.rationals())
        );
        for (Pair<RationalMultivariatePolynomial, Map<Variable, Rational>> p : take(LIMIT, ps2)) {
            assertEquals(p, p.a.applyRational(p.b), p.a.toMultivariatePolynomial().applyRational(p.b));
        }

        Iterable<RationalMultivariatePolynomial> psFail = filterInfinite(
                p -> !p.onlyHasIntegralCoefficients(),
                P.rationalMultivariatePolynomials()
        );
        for (RationalMultivariatePolynomial p : take(LIMIT, psFail)) {
            try {
                p.toMultivariatePolynomial();
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesCoefficient() {
        initialize("coefficient(Monomial)");
        Iterable<Pair<RationalMultivariatePolynomial, Monomial>> ps = P.pairs(
                P.rationalMultivariatePolynomials(),
                P.monomials()
        );
        for (Pair<RationalMultivariatePolynomial, Monomial> p : take(LIMIT, ps)) {
            p.a.coefficient(p.b);
        }
    }

    private void propertiesOf_List_Pair_Monomial_Rational() {
        initialize("of(List<Pair<Monomial, Rational>>)");
        Iterable<List<Pair<Monomial, Rational>>> pss = P.lists(P.pairs(P.monomials(), P.rationals()));
        for (List<Pair<Monomial, Rational>> ps : take(LIMIT, pss)) {
            RationalMultivariatePolynomial p = of(ps);
            p.validate();
            for (List<Pair<Monomial, Rational>> qs : take(TINY_LIMIT, P.permutationsFinite(ps))) {
                assertEquals(ps, of(qs), p);
            }
        }

        Iterable<List<Pair<Monomial, Rational>>> pssFail = filterInfinite(
                ps -> any(p -> p == null || p.a == null || p.b == null, ps),
                P.lists(P.withNull(P.pairs(P.withNull(P.monomials()), P.withNull(P.rationals()))))
        );
        for (List<Pair<Monomial, Rational>> ps : take(LIMIT, pssFail)) {
            try {
                of(ps);
                fail(ps);
            } catch (NullPointerException ignored) {}
        }
    }

    private static @NotNull RationalMultivariatePolynomial of_Monomial_Rational_simplest(
            @NotNull Monomial ev,
            @NotNull Rational c
    ) {
        return of(Collections.singletonList(new Pair<>(ev, c)));
    }

    private void propertiesOf_Monomial_Rational() {
        initialize("of(Monomial, Rational)");
        for (Pair<Monomial, Rational> p : take(LIMIT, P.pairs(P.monomials(), P.rationals()))) {
            RationalMultivariatePolynomial q = of(p.a, p.b);
            q.validate();
            assertEquals(p, of_Monomial_Rational_simplest(p.a, p.b), q);
            assertTrue(p, q.termCount() < 2);
        }
    }

    private void compareImplementationsOf_Monomial_Rational() {
        Map<String, Function<Pair<Monomial, Rational>, RationalMultivariatePolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> of_Monomial_Rational_simplest(p.a, p.b));
        functions.put("standard", p -> of(p.a, p.b));
        Iterable<Pair<Monomial, Rational>> ps = P.pairs(P.monomials(), P.rationals());
        compareImplementations("of(Monomial, Rational)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesOf_Rational() {
        initialize("of(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            RationalMultivariatePolynomial p = of(r);
            p.validate();
            assertTrue(r, p.degree() <= 0);
            assertTrue(r, p.termCount() <= 1);
            assertEquals(r, p.toString(), r.toString());
            inverse(
                    RationalMultivariatePolynomial::of,
                    (RationalMultivariatePolynomial q) -> q.coefficient(Monomial.ONE),
                    r
            );
        }
    }

    private void propertiesOf_BigInteger() {
        initialize("of(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            RationalMultivariatePolynomial p = of(i);
            p.validate();
            assertTrue(i, p.degree() <= 0);
            assertTrue(i, p.termCount() <= 1);
            assertEquals(i, p.toString(), i.toString());
            inverse(
                    RationalMultivariatePolynomial::of,
                    (RationalMultivariatePolynomial q) -> q.coefficient(Monomial.ONE).bigIntegerValueExact(),
                    i
            );
        }
    }

    private void propertiesOf_int() {
        initialize("of(int)");
        for (int i : take(LIMIT, P.integers())) {
            RationalMultivariatePolynomial p = of(i);
            p.validate();
            assertTrue(i, p.degree() <= 0);
            assertTrue(i, p.termCount() <= 1);
            assertEquals(i, p.toString(), Integer.toString(i));
            inverse(
                    RationalMultivariatePolynomial::of,
                    (RationalMultivariatePolynomial q) -> q.coefficient(Monomial.ONE).intValueExact(),
                    i
            );
        }
    }

    private void propertiesOf_Variable() {
        initialize("of(Variable)");
        for (Variable v : take(LIMIT, P.variables())) {
            RationalMultivariatePolynomial p = of(v);
            p.validate();
            assertEquals(v, p.termCount(), 1);
            assertEquals(v, p.degree(), 1);
        }
    }

    private void propertiesOf_RationalPolynomial_Variable() {
        initialize("of(RationalPolynomial, Variable)");
        for (Pair<RationalPolynomial, Variable> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.variables()))) {
            RationalMultivariatePolynomial q = of(p.a, p.b);
            q.validate();
            assertTrue(p, q.variableCount() <= 1);
            assertEquals(p, q.toRationalPolynomial(), p.a);
            assertEquals(p, p.a.degree(), q.degree());
        }
    }

    private void propertiesToRationalPolynomial() {
        initialize("toRationalPolynomial()");
        Iterable<RationalMultivariatePolynomial> ps = P.withElement(
                ZERO,
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.optionals(P.variables()),
                                v -> v.isPresent() ?
                                        filterInfinite(
                                                q -> q.degree() > 0,
                                                P.withScale(4).rationalMultivariatePolynomials(
                                                        Collections.singletonList(v.get())
                                                )
                                        ) :
                                        map(q -> of(q, Variable.of(0)), P.withScale(4).rationalPolynomials(0))
                        )
                )
        );
        for (RationalMultivariatePolynomial p : take(LIMIT, ps)) {
            p.toRationalPolynomial();
            List<Variable> vs = p.variables();
            if (vs.size() == 1) {
                Variable v = head(vs);
                inverse(RationalMultivariatePolynomial::toRationalPolynomial, r -> of(r, v), p);
            }
        }

        Iterable<RationalMultivariatePolynomial> psFail = filterInfinite(
                q -> q.variableCount() > 1,
                P.rationalMultivariatePolynomials()
        );
        for (RationalMultivariatePolynomial p : take(LIMIT, psFail)) {
            try {
                p.toRationalPolynomial();
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesVariables() {
        initialize("variables()");
        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            List<Variable> vs = p.variables();
            assertTrue(p, increasing(vs));
        }
    }

    private void propertiesVariableCount() {
        initialize("variableCount()");
        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            int count = p.variableCount();
            assertTrue(p, count >= 0);
        }
    }

    private void propertiesTermCount() {
        initialize("termCount()");
        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            int count = p.termCount();
            assertTrue(p, count >= 0);
        }
    }

    private void propertiesMaxCoefficientBitLength() {
        initialize("maxCoefficientBitLength()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            int bitLength = p.maxCoefficientBitLength();
            assertTrue(p, bitLength >= 0);
        }
    }

    private void propertiesDegree() {
        initialize("degree()");
        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            int degree = p.degree();
            assertTrue(p, degree >= -1);
        }

        Iterable<RationalMultivariatePolynomial> ps = P.rationalMultivariatePolynomials(Collections.emptyList());
        for (RationalMultivariatePolynomial p : take(LIMIT, ps)) {
            assertTrue(p, p.degree() < 1);
        }

        Iterable<RationalMultivariatePolynomial> ps2 = P.rationalMultivariatePolynomials(
                Collections.singletonList(Variable.of(0))
        );
        for (RationalMultivariatePolynomial p : take(LIMIT, ps2)) {
            assertEquals(p, p.degree(), p.toRationalPolynomial().degree());
        }
    }

    private void propertiesDegree_Variable() {
        initialize("degree(Variable)");
        Iterable<Pair<RationalMultivariatePolynomial, Variable>> ps = P.pairsLogarithmicOrder(
                P.rationalMultivariatePolynomials(),
                P.variables()
        );
        for (Pair<RationalMultivariatePolynomial, Variable> p : take(LIMIT, ps)) {
            int degree = p.a.degree(p.b);
            assertTrue(p, degree >= -1);
        }

        ps = P.pairsLogarithmicOrder(P.rationalMultivariatePolynomials(Collections.emptyList()), P.variables());
        for (Pair<RationalMultivariatePolynomial, Variable> p : take(LIMIT, ps)) {
            assertTrue(p, p.a.degree(p.b) < 1);
        }

        Variable a = Variable.of(0);
        Iterable<RationalMultivariatePolynomial> ps2 = P.rationalMultivariatePolynomials(Collections.singletonList(a));
        for (RationalMultivariatePolynomial p : take(LIMIT, ps2)) {
            assertEquals(p, p.degree(a), p.toRationalPolynomial().degree());
        }
    }

    private void propertiesIsHomogeneous() {
        initialize("isHomogeneous()");
        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            p.isHomogeneous();
        }
    }

    private void propertiesCoefficientsOfVariable() {
        initialize("coefficientsOfVariable(Variable)");
        Iterable<Pair<RationalMultivariatePolynomial, Variable>> ps = P.pairsLogarithmicOrder(
                P.rationalMultivariatePolynomials(),
                P.variables()
        );
        for (Pair<RationalMultivariatePolynomial, Variable> p : take(LIMIT, ps)) {
            List<RationalMultivariatePolynomial> coefficients = p.a.coefficientsOfVariable(p.b);
            coefficients.forEach(RationalMultivariatePolynomial::validate);
            inverse(
                    q -> q.coefficientsOfVariable(p.b),
                    (List<RationalMultivariatePolynomial> cs) -> sum(
                            toList(
                                    zipWith(
                                            (c, i) -> c.multiply(
                                                    i == 0 ?
                                                            Monomial.ONE :
                                                            Monomial.fromTerms(
                                                                    Collections.singletonList(new Pair<>(p.b, i))
                                                            ),
                                                    Rational.ONE
                                            ),
                                            cs,
                                            ExhaustiveProvider.INSTANCE.naturalIntegers()
                                    )
                            )
                    ),
                    p.a
            );
        }

        Iterable<Pair<Variable, RationalMultivariatePolynomial>> ps2 = P.dependentPairsInfiniteLogarithmicOrder(
                P.variables(),
                v -> P.rationalMultivariatePolynomials(Collections.singletonList(v))
        );
        for (Pair<Variable, RationalMultivariatePolynomial> p : take(LIMIT, ps2)) {
            List<RationalMultivariatePolynomial> coefficients = p.b.coefficientsOfVariable(p.a);
            //noinspection Convert2streamapi
            for (RationalMultivariatePolynomial coefficient : coefficients) {
                if (coefficient.degree() > 0) {
                    fail(p);
                }
            }
            assertEquals(
                    p,
                    toList(map(c -> c.coefficient(Monomial.ONE), coefficients)),
                    toList(p.b.toRationalPolynomial())
            );
        }

        for (Variable v : take(LIMIT, P.variables())) {
            assertEquals(v, ZERO.coefficientsOfVariable(v), Collections.emptyList());
        }

        ps = filterInfinite(
                r -> !r.a.variables().contains(r.b),
                P.pairs(filterInfinite(q -> q != ZERO, P.rationalMultivariatePolynomials()), P.variables())
        );
        for (Pair<RationalMultivariatePolynomial, Variable> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.coefficientsOfVariable(p.b), Collections.singletonList(p.a));
        }
    }

    private void propertiesGroupVariables_List_Variable_MonomialOrder() {
        initialize("groupVariables(List<Variable>, MonomialOrder)");
        Iterable<Triple<RationalMultivariatePolynomial, List<Variable>, MonomialOrder>> ts = P.triples(
                P.rationalMultivariatePolynomials(),
                P.lists(P.variables()),
                P.monomialOrders()
        );
        for (Triple<RationalMultivariatePolynomial, List<Variable>, MonomialOrder> t : take(LIMIT, ts)) {
            List<Pair<Monomial, RationalMultivariatePolynomial>> groups = t.a.groupVariables(t.b, t.c);
            groups.forEach(q -> q.b.validate());
            //noinspection RedundantCast
            assertTrue(t, increasing(t.c, (Iterable<Monomial>) map(g -> g.a, groups)));
            assertTrue(t, all(g -> t.b.containsAll(g.a.variables()), groups));
            assertTrue(t, all(g -> !any(t.b::contains, g.b.variables()), groups));
            assertTrue(t, !any(g -> g.b == ZERO, groups));
            inverse(
                    q -> q.groupVariables(t.b),
                    (List<Pair<Monomial, RationalMultivariatePolynomial>> gs) -> sum(
                            toList(map(g -> g.b.multiply(g.a, Rational.ONE), gs))
                    ),
                    t.a
            );
        }

        Iterable<Triple<RationalMultivariatePolynomial, Variable, MonomialOrder>> ts2 = P.triples(
                P.rationalMultivariatePolynomials(),
                P.variables(),
                P.monomialOrders()
        );
        for (Triple<RationalMultivariatePolynomial, Variable, MonomialOrder> t : take(LIMIT, ts2)) {
            assertEquals(
                    t,
                    toList(map(g -> g.b, t.a.groupVariables(Collections.singletonList(t.b), t.c))),
                    toList(filter(c -> c != ZERO, t.a.coefficientsOfVariable(t.b)))
            );
        }

        Iterable<Triple<RationalMultivariatePolynomial, List<Variable>, MonomialOrder>> tsFail = P.triples(
                P.rationalMultivariatePolynomials(),
                P.listsWithElement(null, P.variables()),
                P.monomialOrders()
        );
        for (Triple<RationalMultivariatePolynomial, List<Variable>, MonomialOrder> t : take(LIMIT, tsFail)) {
            try {
                t.a.groupVariables(t.b, t.c);
                fail(t);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesGroupVariables_List_Variable() {
        initialize("groupVariables(List<Variable>)");
        Iterable<Pair<RationalMultivariatePolynomial, List<Variable>>> ps = P.pairs(
                P.rationalMultivariatePolynomials(),
                P.lists(P.variables())
        );
        for (Pair<RationalMultivariatePolynomial, List<Variable>> p : take(LIMIT, ps)) {
            List<Pair<Monomial, RationalMultivariatePolynomial>> groups = p.a.groupVariables(p.b);
            groups.forEach(q -> q.b.validate());
            //noinspection RedundantCast
            assertTrue(p, increasing((Iterable<Monomial>) map(g -> g.a, groups)));
            assertTrue(p, all(g -> p.b.containsAll(g.a.variables()), groups));
            assertTrue(p, all(g -> !any(p.b::contains, g.b.variables()), groups));
            assertTrue(p, !any(g -> g.b == ZERO, groups));
            inverse(
                    q -> q.groupVariables(p.b),
                    (List<Pair<Monomial, RationalMultivariatePolynomial>> gs) -> sum(
                            toList(map(g -> g.b.multiply(g.a, Rational.ONE), gs))
                    ),
                    p.a
            );
        }

        Iterable<Pair<RationalMultivariatePolynomial, Variable>> ps2 = P.pairsLogarithmicOrder(
                P.rationalMultivariatePolynomials(),
                P.variables()
        );
        for (Pair<RationalMultivariatePolynomial, Variable> p : take(LIMIT, ps2)) {
            assertEquals(
                    p,
                    toList(map(g -> g.b, p.a.groupVariables(Collections.singletonList(p.b)))),
                    toList(filter(c -> c != ZERO, p.a.coefficientsOfVariable(p.b)))
            );
        }

        Iterable<Pair<RationalMultivariatePolynomial, List<Variable>>> psFail = P.pairs(
                P.rationalMultivariatePolynomials(),
                P.listsWithElement(null, P.variables())
        );
        for (Pair<RationalMultivariatePolynomial, List<Variable>> p : take(LIMIT, psFail)) {
            try {
                p.a.groupVariables(p.b);
                fail(p);
            } catch (NullPointerException ignored) {}
        }
    }
}
