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
import static mho.wheels.ordering.Ordering.max;
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
        propertiesAdd();
        compareImplementationsAdd();
        propertiesNegate();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesMultiply_int();
        propertiesMultiply_BigInteger();
        propertiesMultiply_Rational();
        propertiesDivide_int();
        propertiesDivide_BigInteger();
        propertiesDivide_Rational();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesShiftRight();
        compareImplementationsShiftRight();
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

    private static @NotNull RationalMultivariatePolynomial add_simplest(
            @NotNull RationalMultivariatePolynomial a,
            @NotNull RationalMultivariatePolynomial b
    ) {
        return of(toList(concat(a, b)));
    }

    private void propertiesAdd() {
        initialize("add(RationalMultivariatePolynomial)");
        Iterable<Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial>> ps = P.pairs(
                P.rationalMultivariatePolynomials()
        );
        for (Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial> p : take(LIMIT, ps)) {
            RationalMultivariatePolynomial sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum, add_simplest(p.a, p.b));
            commutative(RationalMultivariatePolynomial::add, p);
            inverse(r -> r.add(p.b), (RationalMultivariatePolynomial q) -> q.subtract(p.b), p.a);
        }

        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            fixedPoint(ZERO::add, p);
            fixedPoint(s -> p.add(ZERO), p);
            assertTrue(p, p.add(p.negate()) == ZERO);
        }

        Iterable<
                Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, RationalMultivariatePolynomial>
        > ts = P.triples(P.rationalMultivariatePolynomials());
        for (Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, RationalMultivariatePolynomial> t :
                take(LIMIT, ts)) {
            associative(RationalMultivariatePolynomial::add, t);
        }

        Iterable<
                Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, Map<Variable, Rational>>
        > ts2 = map(
                r -> new Triple<>(r.a.a, r.a.b, r.b),
                P.dependentPairsInfinite(
                        filterInfinite(
                                q -> q.a.degree() > 0 || q.b.degree() > 0,
                                P.pairs(P.rationalMultivariatePolynomials())
                        ),
                        p -> P.maps(sort(nub(concat(p.a.variables(), p.b.variables()))), P.rationals())
                )
        );
        for (Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, Map<Variable, Rational>> t :
                take(LIMIT, ts2)) {
            RationalMultivariatePolynomial sum = t.a.add(t.b);
            assertEquals(t, t.a.applyRational(t.c).add(t.b.applyRational(t.c)), sum.applyRational(t.c));
        }
    }

    private void compareImplementationsAdd() {
        Map<
                String,
                Function<
                        Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial>,
                        RationalMultivariatePolynomial
                >
        > functions = new LinkedHashMap<>();
        functions.put("simplest", p -> add_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.add(p.b));
        Iterable<Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial>> ps = P.pairs(
                P.rationalMultivariatePolynomials()
        );
        compareImplementations("add(RationalMultivariatePolynomial)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            RationalMultivariatePolynomial negative = p.negate();
            negative.validate();
            involution(RationalMultivariatePolynomial::negate, p);
            assertTrue(p, p.add(negative) == ZERO);
        }

        Iterable<RationalMultivariatePolynomial> ps = filterInfinite(
                q -> q != ZERO,
                P.rationalMultivariatePolynomials()
        );
        for (RationalMultivariatePolynomial p : take(LIMIT, ps)) {
            assertNotEquals(p, p, p.negate());
        }

        Iterable<Pair<RationalMultivariatePolynomial, Map<Variable, Rational>>> ps2 = P.dependentPairsInfinite(
                filterInfinite(q -> q.degree() > 0, P.rationalMultivariatePolynomials()),
                p -> P.maps(p.variables(), P.rationals())
        );
        for (Pair<RationalMultivariatePolynomial, Map<Variable, Rational>> p : take(LIMIT, ps2)) {
            RationalMultivariatePolynomial negative = p.a.negate();
            assertEquals(p, p.a.applyRational(p.b).negate(), negative.applyRational(p.b));
        }
    }

    private static @NotNull RationalMultivariatePolynomial subtract_simplest(
            @NotNull RationalMultivariatePolynomial a,
            @NotNull RationalMultivariatePolynomial b
    ) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(RationalMultivariatePolynomial)");
        Iterable<Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial>> ps = P.pairs(
                P.rationalMultivariatePolynomials()
        );
        for (Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial> p : take(LIMIT, ps)) {
            RationalMultivariatePolynomial difference = p.a.subtract(p.b);
            difference.validate();
            assertTrue(p, difference.degree() <= max(p.a.degree(), p.b.degree()));
            assertEquals(p, difference, subtract_simplest(p.a, p.b));
            antiCommutative(RationalMultivariatePolynomial::subtract, RationalMultivariatePolynomial::negate, p);
            inverse(q -> q.subtract(p.b), (RationalMultivariatePolynomial q) -> q.add(p.b), p.a);
        }

        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            assertEquals(p, ZERO.subtract(p), p.negate());
            fixedPoint(q -> q.subtract(ZERO), p);
            assertTrue(p, p.subtract(p) == ZERO);
        }

        Iterable<Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, Map<Variable, Rational>>> ts2 =
                map(
                        r -> new Triple<>(r.a.a, r.a.b, r.b),
                        P.dependentPairsInfinite(
                                filterInfinite(
                                        q -> q.a.degree() > 0 || q.b.degree() > 0,
                                        P.pairs(P.rationalMultivariatePolynomials())
                                ),
                                p -> P.maps(sort(nub(concat(p.a.variables(), p.b.variables()))), P.rationals())
                        )
                );
        for (Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, Map<Variable, Rational>> t :
                take(LIMIT, ts2)) {
            RationalMultivariatePolynomial difference = t.a.subtract(t.b);
            assertEquals(t, t.a.applyRational(t.c).subtract(t.b.applyRational(t.c)), difference.applyRational(t.c));
        }
    }

    private void compareImplementationsSubtract() {
        Map<
                String,
                Function<
                        Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial>,
                        RationalMultivariatePolynomial
                >
        > functions = new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        Iterable<Pair<RationalMultivariatePolynomial, RationalMultivariatePolynomial>> ps = P.pairs(
                P.rationalMultivariatePolynomials()
        );
        compareImplementations("subtract(RationalMultivariatePolynomial)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        Iterable<Pair<RationalMultivariatePolynomial, Integer>> ps = P.pairs(
                P.rationalMultivariatePolynomials(),
                P.integers()
        );
        for (Pair<RationalMultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            RationalMultivariatePolynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b == 0 || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(of(Rational.of(p.b))));
            assertEquals(p, product, of(p.b).multiply(p.a));
        }

        ps = P.pairs(P.rationalMultivariatePolynomials(), P.nonzeroIntegers());
        for (Pair<RationalMultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            inverse(q -> q.multiply(p.b), (RationalMultivariatePolynomial q) -> q.divide(p.b), p.a);
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.integers()))) {
            homomorphic(
                    RationalMultivariatePolynomial::of,
                    Function.identity(),
                    RationalMultivariatePolynomial::of,
                    (i, j) -> i.multiply(BigInteger.valueOf(j)),
                    RationalMultivariatePolynomial::multiply,
                    p
            );
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(i, ONE.multiply(i), of(BigInteger.valueOf(i)));
            fixedPoint(j -> j.multiply(i), ZERO);
        }

        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            fixedPoint(q -> q.multiply(1), p);
            assertTrue(p, p.multiply(0) == ZERO);
        }

        Iterable<Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, Integer>> ts2 = P.triples(
                P.rationalMultivariatePolynomials(),
                P.rationalMultivariatePolynomials(),
                P.integers()
        );
        for (Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, Integer> t : take(LIMIT, ts2)) {
            RationalMultivariatePolynomial expression1 = t.a.add(t.b).multiply(t.c);
            RationalMultivariatePolynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }

        Iterable<Triple<RationalMultivariatePolynomial, Integer, Map<Variable, Rational>>> ts3 = map(
                r -> new Triple<>(r.a.a, r.b, r.a.b),
                P.pairs(
                        P.dependentPairsInfinite(
                                filterInfinite(q -> q.degree() > 0, P.rationalMultivariatePolynomials()),
                                p -> P.maps(p.variables(), P.rationals())
                        ),
                        P.integers()
                )
        );
        for (Triple<RationalMultivariatePolynomial, Integer, Map<Variable, Rational>> t : take(LIMIT, ts3)) {
            RationalMultivariatePolynomial product = t.a.multiply(t.b);
            assertEquals(t, t.a.applyRational(t.c).multiply(t.b), product.applyRational(t.c));
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        Iterable<Pair<RationalMultivariatePolynomial, BigInteger>> ps = P.pairs(
                P.rationalMultivariatePolynomials(),
                P.bigIntegers()
        );
        for (Pair<RationalMultivariatePolynomial, BigInteger> p : take(LIMIT, ps)) {
            RationalMultivariatePolynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b.equals(BigInteger.ZERO) || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(of(p.b)));
            assertEquals(p, product, of(p.b).multiply(p.a));
        }

        ps = P.pairs(P.rationalMultivariatePolynomials(), P.nonzeroBigIntegers());
        for (Pair<RationalMultivariatePolynomial, BigInteger> p : take(LIMIT, ps)) {
            inverse(q -> q.multiply(p.b), (RationalMultivariatePolynomial q) -> q.divide(p.b), p.a);
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            //noinspection Convert2MethodRef
            homomorphic(
                    RationalMultivariatePolynomial::of,
                    Function.identity(),
                    RationalMultivariatePolynomial::of,
                    (i, j) -> i.multiply(j),
                    RationalMultivariatePolynomial::multiply,
                    p
            );
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ONE.multiply(i), of(i));
            fixedPoint(j -> j.multiply(i), ZERO);
        }

        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            fixedPoint(q -> q.multiply(BigInteger.ONE), p);
            assertTrue(p, p.multiply(BigInteger.ZERO) == ZERO);
        }

        Iterable<Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, BigInteger>> ts2 = P.triples(
                P.rationalMultivariatePolynomials(),
                P.rationalMultivariatePolynomials(),
                P.bigIntegers()
        );
        for (Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, BigInteger> t : take(LIMIT, ts2)) {
            RationalMultivariatePolynomial expression1 = t.a.add(t.b).multiply(t.c);
            RationalMultivariatePolynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }

        Iterable<Triple<RationalMultivariatePolynomial, BigInteger, Map<Variable, Rational>>> ts3 = map(
                r -> new Triple<>(r.a.a, r.b, r.a.b),
                P.pairs(
                        P.dependentPairsInfinite(
                                filterInfinite(q -> q.degree() > 0, P.rationalMultivariatePolynomials()),
                                p -> P.maps(p.variables(), P.rationals())
                        ),
                        P.bigIntegers()
                )
        );
        for (Triple<RationalMultivariatePolynomial, BigInteger, Map<Variable, Rational>> t : take(LIMIT, ts3)) {
            RationalMultivariatePolynomial product = t.a.multiply(t.b);
            assertEquals(t, t.a.applyRational(t.c).multiply(t.b), product.applyRational(t.c));
        }
    }

    private void propertiesMultiply_Rational() {
        initialize("multiply(Rational)");
        Iterable<Pair<RationalMultivariatePolynomial, Rational>> ps = P.pairs(
                P.rationalMultivariatePolynomials(),
                P.rationals()
        );
        for (Pair<RationalMultivariatePolynomial, Rational> p : take(LIMIT, ps)) {
            RationalMultivariatePolynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b == Rational.ZERO || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(of(p.b)));
            assertEquals(p, product, of(p.b).multiply(p.a));
        }

        ps = P.pairs(P.rationalMultivariatePolynomials(), P.nonzeroRationals());
        for (Pair<RationalMultivariatePolynomial, Rational> p : take(LIMIT, ps)) {
            inverse(q -> q.multiply(p.b), (RationalMultivariatePolynomial q) -> q.divide(p.b), p.a);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            //noinspection Convert2MethodRef
            homomorphic(
                    RationalMultivariatePolynomial::of,
                    Function.identity(),
                    RationalMultivariatePolynomial::of,
                    (i, j) -> i.multiply(j),
                    RationalMultivariatePolynomial::multiply,
                    p
            );
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ONE.multiply(r), of(r));
            fixedPoint(s -> s.multiply(r), ZERO);
        }

        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            fixedPoint(q -> q.multiply(Rational.ONE), p);
            assertTrue(p, p.multiply(Rational.ZERO) == ZERO);
        }

        Iterable<Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, Rational>> ts2 = P.triples(
                P.rationalMultivariatePolynomials(),
                P.rationalMultivariatePolynomials(),
                P.rationals()
        );
        for (Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, Rational> t : take(LIMIT, ts2)) {
            RationalMultivariatePolynomial expression1 = t.a.add(t.b).multiply(t.c);
            RationalMultivariatePolynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }

        Iterable<Triple<RationalMultivariatePolynomial, Rational, Map<Variable, Rational>>> ts3 = map(
                r -> new Triple<>(r.a.a, r.b, r.a.b),
                P.pairs(
                        P.dependentPairsInfinite(
                                filterInfinite(q -> q.degree() > 0, P.rationalMultivariatePolynomials()),
                                p -> P.maps(p.variables(), P.rationals())
                        ),
                        P.rationals()
                )
        );
        for (Triple<RationalMultivariatePolynomial, Rational, Map<Variable, Rational>> t : take(LIMIT, ts3)) {
            RationalMultivariatePolynomial product = t.a.multiply(t.b);
            assertEquals(t, t.a.applyRational(t.c).multiply(t.b), product.applyRational(t.c));
        }
    }

    private void propertiesDivide_int() {
        initialize("divide(int)");
        Iterable<Pair<RationalMultivariatePolynomial, Integer>> ps = P.pairs(
                P.rationalMultivariatePolynomials(),
                P.nonzeroIntegers()
        );
        for (Pair<RationalMultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            RationalMultivariatePolynomial quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            inverse(q -> q.divide(p.b), (RationalMultivariatePolynomial q) -> q.multiply(p.b), p.a);
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.nonzeroIntegers(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.divide(t.b).apply(t.c), t.a.apply(t.c).divide(t.b));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroIntegers()))) {
            homomorphic(
                    RationalMultivariatePolynomial::of,
                    Function.identity(),
                    RationalMultivariatePolynomial::of,
                    Rational::divide,
                    RationalMultivariatePolynomial::divide,
                    p
            );
        }

        Iterable<Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, Integer>> ts2 = P.triples(
                P.rationalMultivariatePolynomials(),
                P.rationalMultivariatePolynomials(),
                P.nonzeroIntegers()
        );
        for (Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, Integer> t : take(LIMIT, ts2)) {
            RationalMultivariatePolynomial expression1 = t.a.add(t.b).divide(t.c);
            RationalMultivariatePolynomial expression2 = t.a.divide(t.c).add(t.b.divide(t.c));
            assertEquals(t, expression1, expression2);
        }

        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            fixedPoint(q -> q.divide(1), p);
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            fixedPoint(p -> p.divide(i), ZERO);
        }

        Iterable<Triple<RationalMultivariatePolynomial, Integer, Map<Variable, Rational>>> ts3 = map(
                r -> new Triple<>(r.a.a, r.b, r.a.b),
                P.pairs(
                        P.dependentPairsInfinite(
                                filterInfinite(q -> q.degree() > 0, P.rationalMultivariatePolynomials()),
                                p -> P.maps(p.variables(), P.rationals())
                        ),
                        P.nonzeroIntegers()
                )
        );
        for (Triple<RationalMultivariatePolynomial, Integer, Map<Variable, Rational>> t : take(LIMIT, ts3)) {
            RationalMultivariatePolynomial quotient = t.a.divide(t.b);
            assertEquals(t, t.a.applyRational(t.c).divide(t.b), quotient.applyRational(t.c));
        }

        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            try {
                p.divide(0);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_BigInteger() {
        initialize("divide(BigInteger)");
        Iterable<Pair<RationalMultivariatePolynomial, BigInteger>> ps = P.pairs(
                P.rationalMultivariatePolynomials(),
                P.nonzeroBigIntegers()
        );
        for (Pair<RationalMultivariatePolynomial, BigInteger> p : take(LIMIT, ps)) {
            RationalMultivariatePolynomial quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            inverse(q -> q.divide(p.b), (RationalMultivariatePolynomial q) -> q.multiply(p.b), p.a);
        }

        Iterable<Triple<RationalPolynomial, BigInteger, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.nonzeroBigIntegers(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, BigInteger, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.divide(t.b).apply(t.c), t.a.apply(t.c).divide(t.b));
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroBigIntegers()))) {
            homomorphic(
                    RationalMultivariatePolynomial::of,
                    Function.identity(),
                    RationalMultivariatePolynomial::of,
                    Rational::divide,
                    RationalMultivariatePolynomial::divide,
                    p
            );
        }

        Iterable<Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, BigInteger>> ts2 = P.triples(
                P.rationalMultivariatePolynomials(),
                P.rationalMultivariatePolynomials(),
                P.nonzeroBigIntegers()
        );
        for (Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, BigInteger> t : take(LIMIT, ts2)) {
            RationalMultivariatePolynomial expression1 = t.a.add(t.b).divide(t.c);
            RationalMultivariatePolynomial expression2 = t.a.divide(t.c).add(t.b.divide(t.c));
            assertEquals(t, expression1, expression2);
        }

        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            fixedPoint(q -> q.divide(BigInteger.ONE), p);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            fixedPoint(p -> p.divide(i), ZERO);
        }

        Iterable<Triple<RationalMultivariatePolynomial, BigInteger, Map<Variable, Rational>>> ts3 = map(
                r -> new Triple<>(r.a.a, r.b, r.a.b),
                P.pairs(
                        P.dependentPairsInfinite(
                                filterInfinite(q -> q.degree() > 0, P.rationalMultivariatePolynomials()),
                                p -> P.maps(p.variables(), P.rationals())
                        ),
                        P.nonzeroBigIntegers()
                )
        );
        for (Triple<RationalMultivariatePolynomial, BigInteger, Map<Variable, Rational>> t : take(LIMIT, ts3)) {
            RationalMultivariatePolynomial quotient = t.a.divide(t.b);
            assertEquals(t, t.a.applyRational(t.c).divide(t.b), quotient.applyRational(t.c));
        }

        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            try {
                p.divide(BigInteger.ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_Rational() {
        initialize("divide(Rational)");
        Iterable<Pair<RationalMultivariatePolynomial, Rational>> ps = P.pairs(
                P.rationalMultivariatePolynomials(),
                P.nonzeroRationals()
        );
        for (Pair<RationalMultivariatePolynomial, Rational> p : take(LIMIT, ps)) {
            RationalMultivariatePolynomial quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            inverse(q -> q.divide(p.b), (RationalMultivariatePolynomial q) -> q.multiply(p.b), p.a);
        }

        Iterable<Triple<RationalPolynomial, Rational, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.nonzeroRationals(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Rational, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.divide(t.b).apply(t.c), t.a.apply(t.c).divide(t.b));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroRationals()))) {
            homomorphic(
                    RationalMultivariatePolynomial::of,
                    Function.identity(),
                    RationalMultivariatePolynomial::of,
                    Rational::divide,
                    RationalMultivariatePolynomial::divide,
                    p
            );
        }

        Iterable<Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, Rational>> ts2 = P.triples(
                P.rationalMultivariatePolynomials(),
                P.rationalMultivariatePolynomials(),
                P.nonzeroRationals()
        );
        for (Triple<RationalMultivariatePolynomial, RationalMultivariatePolynomial, Rational> t : take(LIMIT, ts2)) {
            RationalMultivariatePolynomial expression1 = t.a.add(t.b).divide(t.c);
            RationalMultivariatePolynomial expression2 = t.a.divide(t.c).add(t.b.divide(t.c));
            assertEquals(t, expression1, expression2);
        }

        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            fixedPoint(q -> q.divide(Rational.ONE), p);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            fixedPoint(p -> p.divide(r), ZERO);
        }

        Iterable<Triple<RationalMultivariatePolynomial, Rational, Map<Variable, Rational>>> ts3 = map(
                r -> new Triple<>(r.a.a, r.b, r.a.b),
                P.pairs(
                        P.dependentPairsInfinite(
                                filterInfinite(q -> q.degree() > 0, P.rationalMultivariatePolynomials()),
                                p -> P.maps(p.variables(), P.rationals())
                        ),
                        P.nonzeroRationals()
                )
        );
        for (Triple<RationalMultivariatePolynomial, Rational, Map<Variable, Rational>> t : take(LIMIT, ts3)) {
            RationalMultivariatePolynomial quotient = t.a.divide(t.b);
            assertEquals(t, t.a.applyRational(t.c).divide(t.b), quotient.applyRational(t.c));
        }

        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            try {
                p.divide(Rational.ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull RationalMultivariatePolynomial shiftLeft_simplest(
            @NotNull RationalMultivariatePolynomial p,
            int bits
    ) {
        if (bits >= 0) {
            return p.multiply(BigInteger.ONE.shiftLeft(bits));
        } else {
            return p.divide(BigInteger.ONE.shiftLeft(-bits));
        }
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        Iterable<Pair<RationalMultivariatePolynomial, Integer>> ps = P.pairs(
                P.rationalMultivariatePolynomials(),
                P.integersGeometric()
        );
        for (Pair<RationalMultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            RationalMultivariatePolynomial shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted.degree(), p.a.degree());
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p.toString(), map(t -> t.b.signum(), p.a), map(t -> t.b.signum(), shifted));
            assertEquals(p, p.a.degree(), shifted.degree());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
            assertEquals(p, p.a.shiftRight(-p.b), shifted);
        }

        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            fixedPoint(q -> q.shiftLeft(0), p);
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<
                String,
                Function<Pair<RationalMultivariatePolynomial, Integer>, RationalMultivariatePolynomial>
        > functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        compareImplementations(
                "shiftLeft(int)",
                take(LIMIT, P.pairs(P.rationalMultivariatePolynomials(), P.integersGeometric())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull RationalMultivariatePolynomial shiftRight_simplest(
            @NotNull RationalMultivariatePolynomial p,
            int bits
    ) {
        if (bits >= 0) {
            return p.divide(BigInteger.ONE.shiftLeft(bits));
        } else {
            return p.multiply(BigInteger.ONE.shiftLeft(-bits));
        }
    }

    private void propertiesShiftRight() {
        initialize("shiftRight(int)");
        Iterable<Pair<RationalMultivariatePolynomial, Integer>> ps = P.pairs(
                P.rationalMultivariatePolynomials(),
                P.integersGeometric()
        );
        for (Pair<RationalMultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            RationalMultivariatePolynomial shifted = p.a.shiftRight(p.b);
            shifted.validate();
            assertEquals(p, shifted.degree(), p.a.degree());
            assertEquals(p, shifted, shiftRight_simplest(p.a, p.b));
            aeqit(p.toString(), map(t -> t.b.signum(), p.a), map(t -> t.b.signum(), shifted));
            assertEquals(p, p.a.degree(), shifted.degree());
            assertEquals(p, p.a.negate().shiftRight(p.b), shifted.negate());
            assertEquals(p, p.a.shiftLeft(-p.b), shifted);
        }

        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            fixedPoint(q -> q.shiftRight(0), p);
        }
    }

    private void compareImplementationsShiftRight() {
        Map<
                String,
                Function<Pair<RationalMultivariatePolynomial, Integer>, RationalMultivariatePolynomial>
        > functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftRight_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftRight(p.b));
        compareImplementations(
                "shiftRight(int)",
                take(LIMIT, P.pairs(P.rationalMultivariatePolynomials(), P.integersGeometric())),
                functions,
                v -> P.reset()
        );
    }
}
