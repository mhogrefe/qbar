package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Quadruple;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.qbar.objects.MultivariatePolynomial.*;
import static mho.qbar.objects.MultivariatePolynomial.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.compare;
import static mho.wheels.ordering.Ordering.max;
import static mho.wheels.testing.Testing.*;

public class MultivariatePolynomialProperties extends QBarTestProperties {
    private static final @NotNull String MULTIVARIATE_POLYNOMIAL_CHARS = "*+-0123456789^abcdefghijklmnopqrstuvwxyz";

    private static final @NotNull Comparator<MultivariatePolynomial> TERM_SHORTLEX_COMPARATOR = (p, q) -> {
        int c = Integer.compare(p.termCount(), q.termCount());
        if (c != 0) return c;
        return p.compareTo(q);
    };

    public MultivariatePolynomialProperties() {
        super("MultivariatePolynomial");
    }

    @Override
    protected void testBothModes() {
        propertiesIterable();
        propertiesIterator();
        propertiesToRationalMultivariatePolynomial();
        propertiesCoefficient();
        propertiesOf_List_Pair_Monomial_BigInteger();
        propertiesOf_Monomial_BigInteger();
        compareImplementationsOf_Monomial_BigInteger();
        propertiesOf_BigInteger();
        propertiesOf_int();
        propertiesOf_Variable();
        propertiesOf_Polynomial_Variable();
        propertiesToPolynomial();
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
        propertiesDivideExact_BigInteger();
        propertiesDivideExact_int();
        propertiesMultiply_Monomial_BigInteger();
        propertiesMultiply_MultivariatePolynomial();
        compareImplementationsMultiply_MultivariatePolynomial();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesSum();
        compareImplementationsSum();
        propertiesProduct();
        compareImplementationsProduct();
        propertiesDelta();
        propertiesPow();
        compareImplementationsPow();
        propertiesBinomialPower();
        compareImplementationsBinomialPower();
        propertiesApplyBigInteger();
        propertiesApplyRational();
        propertiesSubstituteMonomial();
        propertiesSubstitute();
        propertiesSylvesterMatrix();
        propertiesResultant();
        propertiesPowerReduce();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesReadStrict_String_MonomialOrder();
        propertiesReadStrict_String();
        propertiesToString_MonomialOrder();
        propertiesToString();
    }

    private void propertiesIterable() {
        initialize("iterable(MonomialOrder)");
        Iterable<Pair<MultivariatePolynomial, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                P.multivariatePolynomials(),
                P.monomialOrders()
        );
        for (Pair<MultivariatePolynomial, MonomialOrder> p : take(LIMIT, ps)) {
            Iterable<Pair<Monomial, BigInteger>> termIterable = p.a.iterable(p.b);
            List<Pair<Monomial, BigInteger>> terms = toList(termIterable);
            assertFalse(p, any(t -> t == null || t.a == null || t.b == null, terms));
            //noinspection RedundantCast
            assertTrue(p, increasing(p.b, (Iterable<Monomial>) map(t -> t.a, terms)));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<Pair<Monomial, BigInteger>> ts) -> of(ts), p.a);
            testNoRemove(termIterable);
            testHasNext(termIterable);
        }
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            List<Pair<Monomial, BigInteger>> terms = toList(p);
            assertFalse(p, any(t -> t == null || t.a == null || t.b == null, terms));
            //noinspection RedundantCast
            assertTrue(p, increasing((Iterable<Monomial>) map(t -> t.a, terms)));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<Pair<Monomial, BigInteger>> ts) -> of(ts), p);
            testNoRemove(p);
            testHasNext(p);
        }
    }

    private void propertiesToRationalMultivariatePolynomial() {
        initialize("toRationalMultivariatePolynomial()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            RationalMultivariatePolynomial rp = p.toRationalMultivariatePolynomial();
            assertEquals(p, p.toString(), rp.toString());
            assertEquals(p, p.degree(), rp.degree());
            inverse(
                    MultivariatePolynomial::toRationalMultivariatePolynomial,
                    RationalMultivariatePolynomial::toMultivariatePolynomial,
                    p
            );
        }

        Iterable<Pair<MultivariatePolynomial, Map<Variable, Rational>>> ps = P.dependentPairsInfinite(
                filterInfinite(q -> q.degree() > 0, P.multivariatePolynomials()),
                p -> P.maps(p.variables(), P.rationals())
        );
        for (Pair<MultivariatePolynomial, Map<Variable, Rational>> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.applyRational(p.b), p.a.toRationalMultivariatePolynomial().applyRational(p.b));
        }
    }

    private void propertiesCoefficient() {
        initialize("coefficient(Monomial)");
        Iterable<Pair<MultivariatePolynomial, Monomial>> ps = P.pairs(
                P.multivariatePolynomials(),
                P.monomials()
        );
        for (Pair<MultivariatePolynomial, Monomial> p : take(LIMIT, ps)) {
            p.a.coefficient(p.b);
        }
    }

    private void propertiesOf_List_Pair_Monomial_BigInteger() {
        initialize("of(List<Pair<Monomial, BigInteger>>)");
        Iterable<List<Pair<Monomial, BigInteger>>> pss = P.lists(P.pairs(P.monomials(), P.bigIntegers()));
        for (List<Pair<Monomial, BigInteger>> ps : take(LIMIT, pss)) {
            MultivariatePolynomial p = of(ps);
            p.validate();
            for (List<Pair<Monomial, BigInteger>> qs : take(TINY_LIMIT, P.permutationsFinite(ps))) {
                assertEquals(ps, of(qs), p);
            }
        }

        Iterable<List<Pair<Monomial, BigInteger>>> pssFail = filterInfinite(
                ps -> any(p -> p == null || p.a == null || p.b == null, ps),
                P.lists(P.withNull(P.pairs(P.withNull(P.monomials()), P.withNull(P.bigIntegers()))))
        );
        for (List<Pair<Monomial, BigInteger>> ps : take(LIMIT, pssFail)) {
            try {
                of(ps);
                fail(ps);
            } catch (NullPointerException ignored) {}
        }
    }

    private static @NotNull MultivariatePolynomial of_Monomial_BigInteger_simplest(
            @NotNull Monomial ev,
            @NotNull BigInteger c
    ) {
        return of(Collections.singletonList(new Pair<>(ev, c)));
    }

    private void propertiesOf_Monomial_BigInteger() {
        initialize("of(Monomial, BigInteger)");
        for (Pair<Monomial, BigInteger> p : take(LIMIT, P.pairs(P.monomials(), P.bigIntegers()))) {
            MultivariatePolynomial q = of(p.a, p.b);
            q.validate();
            assertEquals(p, of_Monomial_BigInteger_simplest(p.a, p.b), q);
            assertTrue(p, q.termCount() < 2);
        }
    }

    private void compareImplementationsOf_Monomial_BigInteger() {
        Map<String, Function<Pair<Monomial, BigInteger>, MultivariatePolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> of_Monomial_BigInteger_simplest(p.a, p.b));
        functions.put("standard", p -> of(p.a, p.b));
        Iterable<Pair<Monomial, BigInteger>> ps = P.pairs(P.monomials(), P.bigIntegers());
        compareImplementations("of(Monomial, BigInteger)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesOf_BigInteger() {
        initialize("of(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            MultivariatePolynomial p = of(i);
            p.validate();
            assertTrue(i, p.degree() <= 0);
            assertTrue(i, p.termCount() <= 1);
            assertEquals(i, p.toString(), i.toString());
            inverse(MultivariatePolynomial::of, (MultivariatePolynomial q) -> q.coefficient(Monomial.ONE), i);
        }
    }

    private void propertiesOf_int() {
        initialize("of(int)");
        for (int i : take(LIMIT, P.integers())) {
            MultivariatePolynomial p = of(i);
            p.validate();
            assertTrue(i, p.degree() <= 0);
            assertTrue(i, p.termCount() <= 1);
            assertEquals(i, p.toString(), Integer.toString(i));
            inverse(
                    MultivariatePolynomial::of,
                    (MultivariatePolynomial q) -> q.coefficient(Monomial.ONE).intValueExact(),
                    i
            );
        }
    }

    private void propertiesOf_Variable() {
        initialize("of(Variable)");
        for (Variable v : take(LIMIT, P.variables())) {
            MultivariatePolynomial p = of(v);
            p.validate();
            assertEquals(v, p.termCount(), 1);
            assertEquals(v, p.degree(), 1);
        }
    }

    private void propertiesOf_Polynomial_Variable() {
        initialize("of(Polynomial, Variable)");
        for (Pair<Polynomial, Variable> p : take(LIMIT, P.pairs(P.polynomials(), P.variables()))) {
            MultivariatePolynomial q = of(p.a, p.b);
            q.validate();
            assertTrue(p, q.variableCount() <= 1);
            assertEquals(p, q.toPolynomial(), p.a);
            assertEquals(p, p.a.degree(), q.degree());
        }
    }

    private void propertiesToPolynomial() {
        initialize("toPolynomial()");
        Iterable<MultivariatePolynomial> ps = P.withElement(
                ZERO,
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.optionals(P.variables()),
                                v -> v.isPresent() ?
                                        filterInfinite(
                                                q -> q.degree() > 0,
                                                P.withScale(4).multivariatePolynomials(
                                                        Collections.singletonList(v.get())
                                                )
                                        ) :
                                        map(q -> of(q, Variable.of(0)), P.withScale(4).polynomials(0))
                        )
                )
        );
        for (MultivariatePolynomial p : take(LIMIT, ps)) {
            p.toPolynomial();
            List<Variable> vs = p.variables();
            if (vs.size() == 1) {
                Variable v = head(vs);
                inverse(MultivariatePolynomial::toPolynomial, r -> of(r, v), p);
            }
        }

        Iterable<MultivariatePolynomial> psFail = filterInfinite(
                q -> q.variableCount() > 1,
                P.multivariatePolynomials()
        );
        for (MultivariatePolynomial p : take(LIMIT, psFail)) {
            try {
                p.toPolynomial();
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesVariables() {
        initialize("variables()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            List<Variable> vs = p.variables();
            assertTrue(p, increasing(vs));
        }
    }

    private void propertiesVariableCount() {
        initialize("variableCount()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            int count = p.variableCount();
            assertTrue(p, count >= 0);
        }
    }

    private void propertiesTermCount() {
        initialize("termCount()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
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
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            int degree = p.degree();
            assertTrue(p, degree >= -1);
        }

        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials(Collections.emptyList()))) {
            assertTrue(p, p.degree() < 1);
        }

        Variable a = Variable.of(0);
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials(Collections.singletonList(a)))) {
            assertEquals(p, p.degree(), p.toPolynomial().degree());
        }
    }

    private void propertiesDegree_Variable() {
        initialize("degree(Variable)");
        Iterable<Pair<MultivariatePolynomial, Variable>> ps = P.pairsLogarithmicOrder(
                P.multivariatePolynomials(),
                P.variables()
        );
        for (Pair<MultivariatePolynomial, Variable> p : take(LIMIT, ps)) {
            int degree = p.a.degree(p.b);
            assertTrue(p, degree >= -1);
        }

        ps = P.pairsLogarithmicOrder(P.multivariatePolynomials(Collections.emptyList()), P.variables());
        for (Pair<MultivariatePolynomial, Variable> p : take(LIMIT, ps)) {
            assertTrue(p, p.a.degree(p.b) < 1);
        }

        Variable a = Variable.of(0);
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials(Collections.singletonList(a)))) {
            assertEquals(p, p.degree(a), p.toPolynomial().degree());
        }
    }

    private void propertiesIsHomogeneous() {
        initialize("isHomogeneous()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            p.isHomogeneous();
        }
    }

    private void propertiesCoefficientsOfVariable() {
        initialize("coefficientsOfVariable(Variable)");
        Iterable<Pair<MultivariatePolynomial, Variable>> ps = P.pairsLogarithmicOrder(
                P.multivariatePolynomials(),
                P.variables()
        );
        for (Pair<MultivariatePolynomial, Variable> p : take(LIMIT, ps)) {
            List<MultivariatePolynomial> coefficients = p.a.coefficientsOfVariable(p.b);
            coefficients.forEach(MultivariatePolynomial::validate);
            inverse(
                    q -> q.coefficientsOfVariable(p.b),
                    (List<MultivariatePolynomial> cs) -> sum(
                            toList(
                                    zipWith(
                                            (c, i) -> c.multiply(
                                                    i == 0 ?
                                                            Monomial.ONE :
                                                            Monomial.fromTerms(
                                                                    Collections.singletonList(new Pair<>(p.b, i))
                                                            ),
                                                    BigInteger.ONE
                                            ),
                                            cs,
                                            ExhaustiveProvider.INSTANCE.naturalIntegers()
                                    )
                            )
                    ),
                    p.a
            );
        }

        Iterable<Pair<Variable, MultivariatePolynomial>> ps2 = P.dependentPairsInfiniteLogarithmicOrder(
                P.variables(),
                v -> P.multivariatePolynomials(Collections.singletonList(v))
        );
        for (Pair<Variable, MultivariatePolynomial> p : take(LIMIT, ps2)) {
            List<MultivariatePolynomial> coefficients = p.b.coefficientsOfVariable(p.a);
            //noinspection Convert2streamapi
            for (MultivariatePolynomial coefficient : coefficients) {
                if (coefficient.degree() > 0) {
                    fail(p);
                }
            }
            assertEquals(
                    p,
                    toList(map(c -> c.coefficient(Monomial.ONE), coefficients)),
                    toList(p.b.toPolynomial())
            );
        }

        for (Variable v : take(LIMIT, P.variables())) {
            assertEquals(v, ZERO.coefficientsOfVariable(v), Collections.emptyList());
        }

        ps = filterInfinite(
                r -> !r.a.variables().contains(r.b),
                P.pairs(filterInfinite(q -> q != ZERO, P.multivariatePolynomials()), P.variables())
        );
        for (Pair<MultivariatePolynomial, Variable> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.coefficientsOfVariable(p.b), Collections.singletonList(p.a));
        }
    }

    private void propertiesGroupVariables_List_Variable_MonomialOrder() {
        initialize("groupVariables(List<Variable>, MonomialOrder)");
        Iterable<Triple<MultivariatePolynomial, List<Variable>, MonomialOrder>> ts = P.triples(
                P.multivariatePolynomials(),
                P.lists(P.variables()),
                P.monomialOrders()
        );
        for (Triple<MultivariatePolynomial, List<Variable>, MonomialOrder> t : take(LIMIT, ts)) {
            List<Pair<Monomial, MultivariatePolynomial>> groups = t.a.groupVariables(t.b, t.c);
            groups.forEach(q -> q.b.validate());
            //noinspection RedundantCast
            assertTrue(t, increasing(t.c, (Iterable<Monomial>) map(g -> g.a, groups)));
            assertTrue(t, all(g -> t.b.containsAll(g.a.variables()), groups));
            assertTrue(t, all(g -> !any(t.b::contains, g.b.variables()), groups));
            assertTrue(t, !any(g -> g.b == ZERO, groups));
            inverse(
                    q -> q.groupVariables(t.b),
                    (List<Pair<Monomial, MultivariatePolynomial>> gs) -> sum(
                            toList(map(g -> g.b.multiply(g.a, BigInteger.ONE), gs))
                    ),
                    t.a
            );
        }

        Iterable<Triple<MultivariatePolynomial, Variable, MonomialOrder>> ts2 = P.triples(
                P.multivariatePolynomials(),
                P.variables(),
                P.monomialOrders()
        );
        for (Triple<MultivariatePolynomial, Variable, MonomialOrder> t : take(LIMIT, ts2)) {
            assertEquals(
                    t,
                    toList(map(g -> g.b, t.a.groupVariables(Collections.singletonList(t.b), t.c))),
                    toList(filter(c -> c != ZERO, t.a.coefficientsOfVariable(t.b)))
            );
        }

        Iterable<Triple<MultivariatePolynomial, List<Variable>, MonomialOrder>> tsFail = P.triples(
                P.multivariatePolynomials(),
                P.listsWithElement(null, P.variables()),
                P.monomialOrders()
        );
        for (Triple<MultivariatePolynomial, List<Variable>, MonomialOrder> t : take(LIMIT, tsFail)) {
            try {
                t.a.groupVariables(t.b, t.c);
                fail(t);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesGroupVariables_List_Variable() {
        initialize("groupVariables(List<Variable>)");
        Iterable<Pair<MultivariatePolynomial, List<Variable>>> ps = P.pairs(
                P.multivariatePolynomials(),
                P.lists(P.variables())
        );
        for (Pair<MultivariatePolynomial, List<Variable>> p : take(LIMIT, ps)) {
            List<Pair<Monomial, MultivariatePolynomial>> groups = p.a.groupVariables(p.b);
            groups.forEach(q -> q.b.validate());
            //noinspection RedundantCast
            assertTrue(p, increasing((Iterable<Monomial>) map(g -> g.a, groups)));
            assertTrue(p, all(g -> p.b.containsAll(g.a.variables()), groups));
            assertTrue(p, all(g -> !any(p.b::contains, g.b.variables()), groups));
            assertTrue(p, !any(g -> g.b == ZERO, groups));
            inverse(
                    q -> q.groupVariables(p.b),
                    (List<Pair<Monomial, MultivariatePolynomial>> gs) -> sum(
                            toList(map(g -> g.b.multiply(g.a, BigInteger.ONE), gs))
                    ),
                    p.a
            );
        }

        Iterable<Pair<MultivariatePolynomial, Variable>> ps2 = P.pairsLogarithmicOrder(
                P.multivariatePolynomials(),
                P.variables()
        );
        for (Pair<MultivariatePolynomial, Variable> p : take(LIMIT, ps2)) {
            assertEquals(
                    p,
                    toList(map(g -> g.b, p.a.groupVariables(Collections.singletonList(p.b)))),
                    toList(filter(c -> c != ZERO, p.a.coefficientsOfVariable(p.b)))
            );
        }

        Iterable<Pair<MultivariatePolynomial, List<Variable>>> psFail = P.pairs(
                P.multivariatePolynomials(),
                P.listsWithElement(null, P.variables())
        );
        for (Pair<MultivariatePolynomial, List<Variable>> p : take(LIMIT, psFail)) {
            try {
                p.a.groupVariables(p.b);
                fail(p);
            } catch (NullPointerException ignored) {}
        }
    }

    private static @NotNull MultivariatePolynomial add_simplest(
            @NotNull MultivariatePolynomial a,
            @NotNull MultivariatePolynomial b
    ) {
        return of(toList(concat(a, b)));
    }

    private void propertiesAdd() {
        initialize("add(MultivariatePolynomial)");
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(P.multivariatePolynomials());
        for (Pair<MultivariatePolynomial, MultivariatePolynomial> p : take(LIMIT, ps)) {
            MultivariatePolynomial sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum, add_simplest(p.a, p.b));
            commutative(MultivariatePolynomial::add, p);
            inverse(r -> r.add(p.b), (MultivariatePolynomial q) -> q.subtract(p.b), p.a);
        }

        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            fixedPoint(ZERO::add, p);
            fixedPoint(s -> p.add(ZERO), p);
            assertTrue(p, p.add(p.negate()) == ZERO);
        }

        Iterable<Triple<MultivariatePolynomial, MultivariatePolynomial, MultivariatePolynomial>> ts =
                P.triples(P.multivariatePolynomials());
        for (Triple<MultivariatePolynomial, MultivariatePolynomial, MultivariatePolynomial> t : take(LIMIT, ts)) {
            associative(MultivariatePolynomial::add, t);
        }

        Iterable<Triple<MultivariatePolynomial, MultivariatePolynomial, Map<Variable, BigInteger>>> ts2 = map(
                r -> new Triple<>(r.a.a, r.a.b, r.b),
                P.dependentPairsInfinite(
                        filterInfinite(
                                q -> q.a.degree() > 0 || q.b.degree() > 0,
                                P.pairs(P.multivariatePolynomials())
                        ),
                        p -> P.maps(sort(nub(concat(p.a.variables(), p.b.variables()))), P.bigIntegers())
                )
        );
        for (Triple<MultivariatePolynomial, MultivariatePolynomial, Map<Variable, BigInteger>> t : take(LIMIT, ts2)) {
            MultivariatePolynomial sum = t.a.add(t.b);
            assertEquals(t, t.a.applyBigInteger(t.c).add(t.b.applyBigInteger(t.c)), sum.applyBigInteger(t.c));
        }
    }

    private void compareImplementationsAdd() {
        Map<String, Function<Pair<MultivariatePolynomial, MultivariatePolynomial>, MultivariatePolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> add_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.add(p.b));
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(P.multivariatePolynomials());
        compareImplementations("add(MultivariatePolynomial)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            MultivariatePolynomial negative = p.negate();
            negative.validate();
            involution(MultivariatePolynomial::negate, p);
            assertTrue(p, p.add(negative) == ZERO);
        }

        for (MultivariatePolynomial p : take(LIMIT, filterInfinite(q -> q != ZERO, P.multivariatePolynomials()))) {
            assertNotEquals(p, p, p.negate());
        }

        Iterable<Pair<MultivariatePolynomial, Map<Variable, BigInteger>>> ps = P.dependentPairsInfinite(
                filterInfinite(q -> q.degree() > 0, P.multivariatePolynomials()),
                p -> P.maps(p.variables(), P.bigIntegers())
        );
        for (Pair<MultivariatePolynomial, Map<Variable, BigInteger>> p : take(LIMIT, ps)) {
            MultivariatePolynomial negative = p.a.negate();
            assertEquals(p, p.a.applyBigInteger(p.b).negate(), negative.applyBigInteger(p.b));
        }
    }

    private static @NotNull MultivariatePolynomial subtract_simplest(
            @NotNull MultivariatePolynomial a,
            @NotNull MultivariatePolynomial b
    ) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(MultivariatePolynomial)");
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(P.multivariatePolynomials());
        for (Pair<MultivariatePolynomial, MultivariatePolynomial> p : take(LIMIT, ps)) {
            MultivariatePolynomial difference = p.a.subtract(p.b);
            difference.validate();
            assertTrue(p, difference.degree() <= max(p.a.degree(), p.b.degree()));
            assertEquals(p, difference, subtract_simplest(p.a, p.b));
            antiCommutative(MultivariatePolynomial::subtract, MultivariatePolynomial::negate, p);
            inverse(q -> q.subtract(p.b), (MultivariatePolynomial q) -> q.add(p.b), p.a);
        }

        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            assertEquals(p, ZERO.subtract(p), p.negate());
            fixedPoint(q -> q.subtract(ZERO), p);
            assertTrue(p, p.subtract(p) == ZERO);
        }

        Iterable<Triple<MultivariatePolynomial, MultivariatePolynomial, Map<Variable, BigInteger>>> ts2 = map(
                r -> new Triple<>(r.a.a, r.a.b, r.b),
                P.dependentPairsInfinite(
                        filterInfinite(
                                q -> q.a.degree() > 0 || q.b.degree() > 0,
                                P.pairs(P.multivariatePolynomials())
                        ),
                        p -> P.maps(sort(nub(concat(p.a.variables(), p.b.variables()))), P.bigIntegers())
                )
        );
        for (Triple<MultivariatePolynomial, MultivariatePolynomial, Map<Variable, BigInteger>> t : take(LIMIT, ts2)) {
            MultivariatePolynomial difference = t.a.subtract(t.b);
            assertEquals(
                    t,
                    t.a.applyBigInteger(t.c).subtract(t.b.applyBigInteger(t.c)),
                    difference.applyBigInteger(t.c)
            );
        }
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<MultivariatePolynomial, MultivariatePolynomial>, MultivariatePolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(P.multivariatePolynomials());
        compareImplementations("subtract(MultivariatePolynomial)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        Iterable<Pair<MultivariatePolynomial, Integer>> ps = P.pairs(P.multivariatePolynomials(), P.integers());
        for (Pair<MultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            MultivariatePolynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b == 0 || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(of(BigInteger.valueOf(p.b))));
            assertEquals(p, product, of(BigInteger.valueOf(p.b)).multiply(p.a));
        }

        ps = P.pairs(P.multivariatePolynomials(), P.nonzeroIntegers());
        for (Pair<MultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            inverse(q -> q.multiply(p.b), (MultivariatePolynomial q) -> q.divideExact(p.b), p.a);
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.integers()))) {
            homomorphic(
                    MultivariatePolynomial::of,
                    Function.identity(),
                    MultivariatePolynomial::of,
                    (i, j) -> i.multiply(BigInteger.valueOf(j)),
                    MultivariatePolynomial::multiply,
                    p
            );
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(i, ONE.multiply(i), of(BigInteger.valueOf(i)));
            fixedPoint(j -> j.multiply(i), ZERO);
        }

        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            fixedPoint(q -> q.multiply(1), p);
            assertTrue(p, p.multiply(0) == ZERO);
        }

        Iterable<Triple<MultivariatePolynomial, MultivariatePolynomial, Integer>> ts2 = P.triples(
                P.multivariatePolynomials(),
                P.multivariatePolynomials(),
                P.integers()
        );
        for (Triple<MultivariatePolynomial, MultivariatePolynomial, Integer> t : take(LIMIT, ts2)) {
            MultivariatePolynomial expression1 = t.a.add(t.b).multiply(t.c);
            MultivariatePolynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }

        Iterable<Triple<MultivariatePolynomial, Integer, Map<Variable, BigInteger>>> ts3 = map(
                r -> new Triple<>(r.a.a, r.b, r.a.b),
                P.pairs(
                        P.dependentPairsInfinite(
                                filterInfinite(q -> q.degree() > 0, P.multivariatePolynomials()),
                                p -> P.maps(p.variables(), P.bigIntegers())
                        ),
                        P.integers()
                )
        );
        for (Triple<MultivariatePolynomial, Integer, Map<Variable, BigInteger>> t : take(LIMIT, ts3)) {
            MultivariatePolynomial product = t.a.multiply(t.b);
            assertEquals(t, t.a.applyBigInteger(t.c).multiply(BigInteger.valueOf(t.b)), product.applyBigInteger(t.c));
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        Iterable<Pair<MultivariatePolynomial, BigInteger>> ps = P.pairs(P.multivariatePolynomials(), P.bigIntegers());
        for (Pair<MultivariatePolynomial, BigInteger> p : take(LIMIT, ps)) {
            MultivariatePolynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b.equals(BigInteger.ZERO) || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(of(p.b)));
            assertEquals(p, product, of(p.b).multiply(p.a));
        }

        ps = P.pairs(P.multivariatePolynomials(), P.nonzeroBigIntegers());
        for (Pair<MultivariatePolynomial, BigInteger> p : take(LIMIT, ps)) {
            inverse(q -> q.multiply(p.b), (MultivariatePolynomial q) -> q.divideExact(p.b), p.a);
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            //noinspection Convert2MethodRef
            homomorphic(
                    MultivariatePolynomial::of,
                    Function.identity(),
                    MultivariatePolynomial::of,
                    (i, j) -> i.multiply(j),
                    MultivariatePolynomial::multiply,
                    p
            );
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ONE.multiply(i), of(i));
            fixedPoint(j -> j.multiply(i), ZERO);
        }

        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            fixedPoint(q -> q.multiply(BigInteger.ONE), p);
            assertTrue(p, p.multiply(BigInteger.ZERO) == ZERO);
        }

        Iterable<Triple<MultivariatePolynomial, MultivariatePolynomial, BigInteger>> ts2 = P.triples(
                P.multivariatePolynomials(),
                P.multivariatePolynomials(),
                P.bigIntegers()
        );
        for (Triple<MultivariatePolynomial, MultivariatePolynomial, BigInteger> t : take(LIMIT, ts2)) {
            MultivariatePolynomial expression1 = t.a.add(t.b).multiply(t.c);
            MultivariatePolynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }

        Iterable<Triple<MultivariatePolynomial, BigInteger, Map<Variable, BigInteger>>> ts3 = map(
                r -> new Triple<>(r.a.a, r.b, r.a.b),
                P.pairs(
                        P.dependentPairsInfinite(
                                filterInfinite(q -> q.degree() > 0, P.multivariatePolynomials()),
                                p -> P.maps(p.variables(), P.bigIntegers())
                        ),
                        P.bigIntegers()
                )
        );
        for (Triple<MultivariatePolynomial, BigInteger, Map<Variable, BigInteger>> t : take(LIMIT, ts3)) {
            MultivariatePolynomial product = t.a.multiply(t.b);
            assertEquals(t, t.a.applyBigInteger(t.c).multiply(t.b), product.applyBigInteger(t.c));
        }
    }

    private void propertiesDivideExact_BigInteger() {
        initialize("divideExact(BigInteger)");
        Iterable<Pair<MultivariatePolynomial, BigInteger>> ps = map(
                p -> new Pair<>(p.a.multiply(p.b), p.b),
                P.pairs(P.multivariatePolynomials(), P.nonzeroBigIntegers())
        );
        for (Pair<MultivariatePolynomial, BigInteger> p : take(LIMIT, ps)) {
            MultivariatePolynomial quotient = p.a.divideExact(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            inverse(q -> q.divideExact(p.b), (MultivariatePolynomial q) -> q.multiply(p.b), p.a);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            fixedPoint(j -> j.divideExact(i), ZERO);
        }

        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            fixedPoint(q -> q.divideExact(BigInteger.ONE), p);
        }

        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            try {
                p.divideExact(BigInteger.ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivideExact_int() {
        initialize("divideExact(int)");
        Iterable<Pair<MultivariatePolynomial, Integer>> ps = map(
                p -> new Pair<>(p.a.multiply(p.b), p.b),
                P.pairs(P.multivariatePolynomials(), P.nonzeroIntegers())
        );
        for (Pair<MultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            MultivariatePolynomial quotient = p.a.divideExact(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            inverse(q -> q.divideExact(p.b), (MultivariatePolynomial q) -> q.multiply(p.b), p.a);
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            fixedPoint(j -> j.divideExact(i), ZERO);
        }

        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            fixedPoint(q -> q.divideExact(1), p);
        }

        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            try {
                p.divideExact(0);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesMultiply_Monomial_BigInteger() {
        initialize("multiply(Monomial, BigInteger)");
        Iterable<Triple<MultivariatePolynomial, Monomial, BigInteger>> ts = P.triples(
                P.multivariatePolynomials(),
                P.monomials(),
                P.bigIntegers()
        );
        for (Triple<MultivariatePolynomial, Monomial, BigInteger> t : take(LIMIT, ts)) {
            MultivariatePolynomial product = t.a.multiply(t.b, t.c);
            product.validate();
            assertTrue(
                    t,
                    t.a == ZERO || t.c.equals(BigInteger.ZERO) || product.degree() == t.a.degree() + t.b.degree()
            );
            assertEquals(t, t.a.multiply(of(t.b, t.c)), product);
        }

        Iterable<Pair<MultivariatePolynomial, Monomial>> ps = P.pairs(
                P.multivariatePolynomials(),
                P.monomials()
        );
        for (Pair<MultivariatePolynomial, Monomial> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b, BigInteger.ZERO), ZERO);
        }

        Iterable<Pair<MultivariatePolynomial, BigInteger>> ps2 = P.pairs(P.multivariatePolynomials(), P.bigIntegers());
        for (Pair<MultivariatePolynomial, BigInteger> p : take(LIMIT, ps2)) {
            assertEquals(p, p.a.multiply(Monomial.ONE, p.b), p.a.multiply(p.b));
        }

        Iterable<Quadruple<MultivariatePolynomial, Monomial, BigInteger, Map<Variable, BigInteger>>> qs = map(
                r -> new Quadruple<>(r.a.a.a, r.a.a.b, r.b, r.a.b),
                P.pairs(
                    P.dependentPairsInfinite(
                            filterInfinite(
                                    q -> q.a.degree() > 0 || q.b.degree() > 0,
                                    P.pairs(P.multivariatePolynomials(), P.monomials())
                            ),
                            p -> P.maps(sort(nub(concat(p.a.variables(), p.b.variables()))), P.bigIntegers())
                    ),
                    P.bigIntegers()
                )
        );
        for (Quadruple<MultivariatePolynomial, Monomial, BigInteger, Map<Variable, BigInteger>> q : take(LIMIT, qs)) {
            MultivariatePolynomial product = q.a.multiply(q.b, q.c);
            assertEquals(
                    q,
                    q.a.applyBigInteger(q.d).multiply(q.b.applyBigInteger(q.d)).multiply(q.c),
                    product.applyBigInteger(q.d)
            );
        }
    }

    private static @NotNull MultivariatePolynomial multiply_MultivariatePolynomial_alt(
            @NotNull MultivariatePolynomial a,
            @NotNull MultivariatePolynomial b
    ) {
        if (a == ZERO || b == ZERO) return ZERO;
        if (a == ONE) return b;
        if (b == ONE) return a;
        Map<Monomial, BigInteger> terms = new HashMap<>();
        for (Pair<Monomial, BigInteger> aTerm : a) {
            for (Pair<Monomial, BigInteger> bTerm : b) {
                Monomial evProduct = aTerm.a.multiply(bTerm.a);
                BigInteger cProduct = aTerm.b.multiply(bTerm.b);
                BigInteger coefficient = terms.get(evProduct);
                if (coefficient == null) coefficient = BigInteger.ZERO;
                coefficient = coefficient.add(cProduct);
                if (coefficient.equals(BigInteger.ZERO)) {
                    terms.remove(evProduct);
                } else {
                    terms.put(evProduct, coefficient);
                }
            }
        }
        return of(toList(fromMap(terms)));
    }

    private void propertiesMultiply_MultivariatePolynomial() {
        initialize("multiply(MultivariatePolynomial)");
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(P.multivariatePolynomials());
        for (Pair<MultivariatePolynomial, MultivariatePolynomial> p : take(LIMIT, ps)) {
            MultivariatePolynomial product = p.a.multiply(p.b);
            product.validate();
            assertEquals(p, product, multiply_MultivariatePolynomial_alt(p.a, p.b));
            assertTrue(p, p.a == ZERO || p.b == ZERO || product.degree() == p.a.degree() + p.b.degree());
            commutative(MultivariatePolynomial::multiply, p);
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(
                    MultivariatePolynomial::of,
                    MultivariatePolynomial::of,
                    MultivariatePolynomial::of,
                    BigInteger::multiply,
                    MultivariatePolynomial::multiply,
                    p
            );
        }

        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            fixedPoint(ONE::multiply, p);
            fixedPoint(q -> q.multiply(ONE), p);
            fixedPoint(q -> q.multiply(p), ZERO);
            fixedPoint(p::multiply, ZERO);
        }

        Iterable<Triple<MultivariatePolynomial, MultivariatePolynomial, MultivariatePolynomial>> ts = P.triples(
                P.multivariatePolynomials()
        );
        for (Triple<MultivariatePolynomial, MultivariatePolynomial, MultivariatePolynomial> t : take(LIMIT, ts)) {
            associative(MultivariatePolynomial::multiply, t);
            leftDistributive(MultivariatePolynomial::add, MultivariatePolynomial::multiply, t);
            rightDistributive(MultivariatePolynomial::add, MultivariatePolynomial::multiply, t);
        }

        Iterable<Triple<MultivariatePolynomial, MultivariatePolynomial, Map<Variable, BigInteger>>> ts2 = map(
                r -> new Triple<>(r.a.a, r.a.b, r.b),
                P.dependentPairsInfinite(
                        filterInfinite(
                                q -> q.a.degree() > 0 || q.b.degree() > 0,
                                P.pairs(P.multivariatePolynomials())
                        ),
                        p -> P.maps(sort(nub(concat(p.a.variables(), p.b.variables()))), P.bigIntegers())
                )
        );
        for (Triple<MultivariatePolynomial, MultivariatePolynomial, Map<Variable, BigInteger>> t : take(LIMIT, ts2)) {
            MultivariatePolynomial product = t.a.multiply(t.b);
            assertEquals(t, t.a.applyBigInteger(t.c).multiply(t.b.applyBigInteger(t.c)), product.applyBigInteger(t.c));
        }
    }

    private void compareImplementationsMultiply_MultivariatePolynomial() {
        Map<String, Function<Pair<MultivariatePolynomial, MultivariatePolynomial>, MultivariatePolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("alt", p -> multiply_MultivariatePolynomial_alt(p.a, p.b));
        functions.put("standard", p -> p.a.multiply(p.b));
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(P.multivariatePolynomials());
        compareImplementations("multiply(MultivariatePolynomial)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull MultivariatePolynomial shiftLeft_simplest(@NotNull MultivariatePolynomial p, int bits) {
        return p.multiply(BigInteger.ONE.shiftLeft(bits));
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        Iterable<Pair<MultivariatePolynomial, Integer>> ps = P.pairs(
                P.multivariatePolynomials(),
                P.naturalIntegersGeometric()
        );
        for (Pair<MultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            MultivariatePolynomial shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted.degree(), p.a.degree());
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p.toString(), map(t -> t.b.signum(), p.a), map(t -> t.b.signum(), shifted));
            assertEquals(p, p.a.degree(), shifted.degree());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
        }

        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            fixedPoint(q -> q.shiftLeft(0), p);
        }

        Iterable<Pair<MultivariatePolynomial, Integer>> psFail = P.pairs(
                P.multivariatePolynomials(),
                P.negativeIntegers()
        );
        for (Pair<MultivariatePolynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.shiftLeft(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<MultivariatePolynomial, Integer>, MultivariatePolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        compareImplementations(
                "shiftLeft(int)",
                take(LIMIT, P.pairs(P.multivariatePolynomials(), P.naturalIntegersGeometric())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull MultivariatePolynomial sum_simplest(@NotNull List<MultivariatePolynomial> xs) {
        return of(toList(concat(map(p -> (Iterable<Pair<Monomial, BigInteger>>) p, xs))));
    }

    private static @NotNull MultivariatePolynomial sum_alt(@NotNull List<MultivariatePolynomial> xs) {
        List<Pair<Monomial, BigInteger>> sumTerms = new ArrayList<>();
        List<Iterator<Pair<Monomial, BigInteger>>> inputTermsIterators =
                toList(map(MultivariatePolynomial::iterator, xs));
        List<Pair<Monomial, BigInteger>> inputTerms =
                toList(map(it -> it.hasNext() ? it.next() : null, inputTermsIterators));
        while (true) {
            Monomial lowestMonomial = null;
            List<Integer> lowestIndices = new ArrayList<>();
            for (int i = 0; i < inputTerms.size(); i++) {
                Pair<Monomial, BigInteger> p = inputTerms.get(i);
                if (p == null) continue;
                if (lowestMonomial == null || Ordering.lt(p.a, lowestMonomial)) {
                    lowestMonomial = p.a;
                    lowestIndices.clear();
                    lowestIndices.add(i);
                } else if (p.a.equals(lowestMonomial)) {
                    lowestIndices.add(i);
                }
            }
            if (lowestMonomial == null) {
                break;
            }
            BigInteger coefficient = BigInteger.ZERO;
            for (int index : lowestIndices) {
                coefficient = coefficient.add(inputTerms.get(index).b);
                Iterator<Pair<Monomial, BigInteger>> it = inputTermsIterators.get(index);
                inputTerms.set(index, it.hasNext() ? it.next() : null);
            }
            if (!coefficient.equals(BigInteger.ZERO)) {
                sumTerms.add(new Pair<>(lowestMonomial, coefficient));
            }
        }
        if (sumTerms.size() == 0) return ZERO;
        if (sumTerms.size() == 1) {
            Pair<Monomial, BigInteger> term = sumTerms.get(0);
            if (term.a == Monomial.ONE && term.b.equals(BigInteger.ONE)) return ONE;
        }
        return of(sumTerms);
    }

    private void propertiesSum() {
        initialize("sum(List<MultivariatePolynomial>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.multivariatePolynomials(),
                MultivariatePolynomial::add,
                MultivariatePolynomial::sum,
                MultivariatePolynomial::validate,
                true,
                true
        );

        for (List<MultivariatePolynomial> ps : take(LIMIT, P.lists(P.multivariatePolynomials()))) {
            MultivariatePolynomial sum = sum(ps);
            sum.validate();
            assertEquals(ps, sum, sum_simplest(ps));
            assertEquals(ps, sum, sum_alt(ps));
            //noinspection RedundantCast
            assertTrue(
                    ps,
                    ps.isEmpty() ||
                            sum.degree() <= maximum((Iterable<Integer>) map(MultivariatePolynomial::degree, ps))
            );
        }

        Iterable<Pair<List<MultivariatePolynomial>, Map<Variable, BigInteger>>> ps = P.dependentPairsInfinite(
                filterInfinite(qs -> any(q -> q.degree() > 0, qs), P.withScale(1).lists(P.multivariatePolynomials())),
                ns -> P.maps(sort(nub(concat(map(MultivariatePolynomial::variables, ns)))), P.bigIntegers())
        );
        for (Pair<List<MultivariatePolynomial>, Map<Variable, BigInteger>> p : take(LIMIT, ps)) {
            MultivariatePolynomial sum = sum(p.a);
            assertEquals(p, sumBigInteger(toList(map(m -> m.applyBigInteger(p.b), p.a))), sum.applyBigInteger(p.b));
        }
    }

    private void compareImplementationsSum() {
        Map<String, Function<List<MultivariatePolynomial>, MultivariatePolynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", MultivariatePolynomialProperties::sum_simplest);
        functions.put("alt", MultivariatePolynomialProperties::sum_alt);
        functions.put("standard", MultivariatePolynomial::sum);
        Iterable<List<MultivariatePolynomial>> pss = P.lists(P.multivariatePolynomials());
        compareImplementations("sum(List<Polynomial>)", take(LIMIT, pss), functions, v -> P.reset());
    }

    private static @NotNull MultivariatePolynomial product_alt(@NotNull List<MultivariatePolynomial> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        if (any(x -> x == ZERO, xs)) {
            return ZERO;
        }
        return foldl(MultivariatePolynomial::multiply, ONE, sort(TERM_SHORTLEX_COMPARATOR, xs));
    }

    private static @NotNull MultivariatePolynomial product_alt2(@NotNull List<MultivariatePolynomial> xs) {
        if (any(x -> x == ZERO, xs)) return ZERO;
        SortedMap<Monomial, BigInteger> termMap = new TreeMap<>();
        for (List<Pair<Monomial, BigInteger>> terms : EP.cartesianProduct(toList(map(IterableUtils::toList, xs)))) {
            Monomial termProduct = Monomial.product(toList(map(t -> t.a, terms)));
            BigInteger coefficientProduct = productBigInteger(toList(map(t -> t.b, terms)));
            BigInteger coefficient = termMap.get(termProduct);
            if (coefficient == null) {
                coefficient = BigInteger.ZERO;
            }
            coefficient = coefficient.add(coefficientProduct);
            termMap.put(termProduct, coefficient);
        }
        return of(toList(fromMap(termMap)));
    }

    private void propertiesProduct() {
        initialize("product(Iterable<MultivariatePolynomial>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.withScale(4).multivariatePolynomials(),
                MultivariatePolynomial::multiply,
                MultivariatePolynomial::product,
                MultivariatePolynomial::validate,
                true,
                true
        );

        for (List<MultivariatePolynomial> ps : take(LIMIT, P.withScale(1).lists(P.multivariatePolynomials()))) {
            MultivariatePolynomial product = product(ps);
            assertTrue(
                    ps,
                    any(p -> p == ZERO, ps) ||
                            product.degree() ==
                                    IterableUtils.sumInteger(toList(map(MultivariatePolynomial::degree, ps)))
            );
        }

        for (List<MultivariatePolynomial> ps : take(LIMIT, P.withScale(1).lists(P.multivariatePolynomials()))) {
            MultivariatePolynomial product = product(ps);
            assertEquals(ps, product, product_alt(ps));
            assertEquals(ps, product, product_alt2(ps));
        }

        Iterable<Pair<List<MultivariatePolynomial>, Map<Variable, BigInteger>>> ps = P.dependentPairsInfinite(
                filterInfinite(qs -> any(q -> q.degree() > 0, qs), P.withScale(1).lists(P.multivariatePolynomials())),
                ns -> P.maps(sort(nub(concat(map(MultivariatePolynomial::variables, ns)))), P.bigIntegers())
        );
        for (Pair<List<MultivariatePolynomial>, Map<Variable, BigInteger>> p : take(LIMIT, ps)) {
            MultivariatePolynomial product = product(p.a);
            assertEquals(
                    p,
                    productBigInteger(toList(map(m -> m.applyBigInteger(p.b), p.a))),
                    product.applyBigInteger(p.b)
            );
        }
    }

    private void compareImplementationsProduct() {
        Map<String, Function<List<MultivariatePolynomial>, MultivariatePolynomial>> functions = new LinkedHashMap<>();
        functions.put("alt", MultivariatePolynomialProperties::product_alt);
        functions.put("alt2", MultivariatePolynomialProperties::product_alt2);
        functions.put("standard", MultivariatePolynomial::product);
        compareImplementations(
                "product(Iterable<MultivariatePolynomial>)",
                take(LIMIT, P.withScale(1).lists(P.multivariatePolynomials())),
                functions,
                v -> P.reset()
        );
    }

    private void propertiesDelta() {
        initialize("delta(Iterable<MultivariatePolynomial>)");
        propertiesDeltaHelper(
                LIMIT,
                P.getWheelsProvider(),
                QBarTesting.QEP.multivariatePolynomials(),
                P.multivariatePolynomials(),
                MultivariatePolynomial::negate,
                MultivariatePolynomial::subtract,
                MultivariatePolynomial::delta,
                MultivariatePolynomial::validate
        );

        Iterable<Pair<List<MultivariatePolynomial>, Map<Variable, BigInteger>>> ps = P.dependentPairsInfinite(
                filterInfinite(qs -> any(q -> q.degree() > 0, qs), P.withScale(1).lists(P.multivariatePolynomials())),
                ns -> P.maps(sort(nub(concat(map(MultivariatePolynomial::variables, ns)))), P.bigIntegers())
        );
        for (Pair<List<MultivariatePolynomial>, Map<Variable, BigInteger>> p : take(LIMIT, ps)) {
            List<MultivariatePolynomial> delta = toList(delta(p.a));
            assertEquals(
                    p,
                    toList(deltaBigInteger(map(m -> m.applyBigInteger(p.b), p.a))),
                    toList(map(q -> q.applyBigInteger(p.b), delta))
            );
        }
    }

    private static @NotNull MultivariatePolynomial pow_simplest(@NotNull MultivariatePolynomial a, int p) {
        return product(toList(replicate(p, a)));
    }

    private void propertiesPow() {
        initialize("pow(int)");
        Iterable<Pair<MultivariatePolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).multivariatePolynomials(),
                P.withScale(1).naturalIntegersGeometric()
        );
        for (Pair<MultivariatePolynomial, Integer> p : take(LIMIT, ps)) {
            MultivariatePolynomial q = p.a.pow(p.b);
            q.validate();
            assertTrue(p, p.a == ZERO || q.degree() == p.a.degree() * p.b);
            assertEquals(p, q, pow_simplest(p.a, p.b));
        }

        for (int i : take(LIMIT, P.withScale(4).positiveIntegersGeometric())) {
            fixedPoint(p -> p.pow(i), ZERO);
        }

        for (MultivariatePolynomial p : take(LIMIT, P.withScale(4).multivariatePolynomials())) {
            assertTrue(p, p.pow(0) == ONE);
            fixedPoint(q -> q.pow(1), p);
            assertEquals(p, p.pow(2), p.multiply(p));
        }

        Iterable<Triple<MultivariatePolynomial, Integer, Integer>> ts2 = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.pairsSquareRootOrder(
                        P.withScale(4).withSecondaryScale(4).multivariatePolynomials(),
                        P.pairs(P.withScale(1).naturalIntegersGeometric())
                )
        );
        for (Triple<MultivariatePolynomial, Integer, Integer> t : take(LIMIT, ts2)) {
            MultivariatePolynomial expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            MultivariatePolynomial expression2 = t.a.pow(t.b + t.c);
            assertEquals(t, expression1, expression2);
            MultivariatePolynomial expression5 = t.a.pow(t.b).pow(t.c);
            MultivariatePolynomial expression6 = t.a.pow(t.b * t.c);
            assertEquals(t, expression5, expression6);
        }

        Iterable<Triple<MultivariatePolynomial, MultivariatePolynomial, Integer>> ts3 = map(
                p -> new Triple<>(p.a.a, p.a.b, p.b),
                P.pairsLogarithmicOrder(
                        P.pairs(P.withScale(4).withSecondaryScale(4).multivariatePolynomials()),
                        P.withScale(1).naturalIntegersGeometric()
                )
        );
        for (Triple<MultivariatePolynomial, MultivariatePolynomial, Integer> t : take(LIMIT, ts3)) {
            MultivariatePolynomial expression1 = t.a.multiply(t.b).pow(t.c);
            MultivariatePolynomial expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        Iterable<Triple<MultivariatePolynomial, Integer, Map<Variable, BigInteger>>> ts4 = map(
                p -> new Triple<>(p.a.a, p.b, p.a.b),
                P.pairsLogarithmicOrder(
                        P.dependentPairsInfinite(
                                filterInfinite(p -> p.degree() > 0, P.withScale(4).multivariatePolynomials()),
                                n -> P.maps(n.variables(), P.bigIntegers())
                        ),
                        P.withScale(1).naturalIntegersGeometric()
                )
        );
        for (Triple<MultivariatePolynomial, Integer, Map<Variable, BigInteger>> t : take(LIMIT, ts4)) {
            MultivariatePolynomial p = t.a.pow(t.b);
            assertEquals(t, t.a.applyBigInteger(t.c).pow(t.b), p.applyBigInteger(t.c));
        }

        Iterable<Pair<MultivariatePolynomial, Integer>> psFail = P.pairs(
                P.multivariatePolynomials(),
                P.negativeIntegers()
        );
        for (Pair<MultivariatePolynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.pow(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsPow() {
        Map<String, Function<Pair<MultivariatePolynomial, Integer>, MultivariatePolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> pow_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.pow(p.b));
        Iterable<Pair<MultivariatePolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).multivariatePolynomials(),
                P.withScale(1).naturalIntegersGeometric()
        );
        compareImplementations("pow(int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull MultivariatePolynomial binomialPower_simplest(
            @NotNull Variable a,
            @NotNull Variable b,
            int p
    ) {
        return of(
                Arrays.asList(new Pair<>(Monomial.of(a), BigInteger.ONE), new Pair<>(Monomial.of(b), BigInteger.ONE))
        ).pow(p);
    }

    private void propertiesBinomialPower() {
        initialize("binomialPower(Variable, Variable, int)");
        Iterable<Triple<Variable, Variable, Integer>> ts = map(
                p -> new Triple<>(p.a.a, p.a.b, p.b),
                P.pairsLogarithmicOrder(P.distinctPairs(P.variables()), P.naturalIntegersGeometric())
        );
        for (Triple<Variable, Variable, Integer> t : take(LIMIT, ts)) {
            MultivariatePolynomial p = binomialPower(t.a, t.b, t.c);
            p.validate();
            assertEquals(t, p, binomialPower_simplest(t.a, t.b, t.c));
            assertEquals(t, p, binomialPower(t.b, t.a, t.c));
            assertTrue(t, p.isHomogeneous());
        }

        ts = map(
                p -> new Triple<>(p.a.a, p.a.b, p.b),
                P.pairsLogarithmicOrder(P.distinctPairs(P.variables()), P.positiveIntegersGeometric())
        );
        for (Triple<Variable, Variable, Integer> t : take(LIMIT, ts)) {
            MultivariatePolynomial p = binomialPower(t.a, t.b, t.c);
            assertEquals(t, p.variables(), Ordering.lt(t.a, t.b) ? Arrays.asList(t.a, t.b) : Arrays.asList(t.b, t.a));
        }

        for (Pair<Variable, Variable> p : take(LIMIT, P.distinctPairs(P.variables()))) {
            assertEquals(p, binomialPower(p.a, p.b, 0), ONE);
            assertEquals(
                    p,
                    binomialPower(p.a, p.b, 1),
                    of(
                            Arrays.asList(
                                    new Pair<>(Monomial.of(p.a), BigInteger.ONE),
                                    new Pair<>(Monomial.of(p.b), BigInteger.ONE)
                            )
                    )
            );
        }

        for (Pair<Variable, Integer> p : take(LIMIT, P.pairs(P.variables(), P.naturalIntegers()))) {
            try {
                binomialPower(p.a, p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        Iterable<Triple<Variable, Variable, Integer>> tsFail = map(
                p -> new Triple<>(p.a.a, p.a.b, p.b),
                P.pairs(P.distinctPairs(P.variables()), P.negativeIntegersGeometric())
        );
        for (Triple<Variable, Variable, Integer> t : take(LIMIT, tsFail)) {
            try {
                binomialPower(t.a, t.b, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsBinomialPower() {
        Map<String, Function<Triple<Variable, Variable, Integer>, MultivariatePolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", t -> binomialPower_simplest(t.a, t.b, t.c));
        functions.put("standard", t -> binomialPower(t.a, t.b, t.c));
        Iterable<Triple<Variable, Variable, Integer>> ts = map(
                p -> new Triple<>(p.a.a, p.a.b, p.b),
                P.pairsLogarithmicOrder(P.distinctPairs(P.variables()), P.naturalIntegersGeometric())
        );
        compareImplementations("binomialPower(Variable, Variable, int)", take(LIMIT, ts), functions, v -> P.reset());
    }

    private void propertiesApplyBigInteger() {
        initialize("applyBigInteger(Map<Variable, BigInteger>)");
        Iterable<Pair<MultivariatePolynomial, Map<Variable, BigInteger>>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.choose(
                    map(i -> new Pair<>(of(i), new TreeMap<>()), P.withScale(4).bigIntegers()),
                    P.dependentPairsInfiniteSquareRootOrder(
                            filterInfinite(r -> r.degree() > 0, P.multivariatePolynomials()),
                            q -> {
                                List<Variable> us = toList(q.variables());
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
            ps = P.choose(
                    P.dependentPairsInfinite(
                            filterInfinite(r -> r.degree() > 0, P.withScale(4).multivariatePolynomials()),
                            q -> {
                                List<Variable> us = toList(q.variables());
                                return map(
                                        p -> p.b,
                                        P.dependentPairsInfiniteLogarithmicOrder(
                                                map(
                                                        vs -> sort(nub(concat(vs, us))),
                                                        P.withScale(4).subsets(P.variables())
                                                ),
                                                ws -> P.maps(ws, P.bigIntegers())
                                        )
                                );
                            }
                    ),
                    map(i -> new Pair<>(of(i), new TreeMap<>()), P.withScale(4).bigIntegers())
            );
        }
        for (Pair<MultivariatePolynomial, Map<Variable, BigInteger>> p : take(LIMIT, ps)) {
            p.a.applyBigInteger(p.b);
        }

        Iterable<Pair<MultivariatePolynomial, Map<Variable, BigInteger>>> psFail =
                P.dependentPairsInfiniteSquareRootOrder(
                        filterInfinite(p -> p.degree() > 0, P.multivariatePolynomials()),
                        m -> {
                            List<Variable> us = toList(m.variables());
                            return map(
                                    p -> p.b,
                                    P.dependentPairsInfiniteLogarithmicOrder(
                                            filterInfinite(
                                                    vs -> !isSubsetOf(us, vs),
                                                    P.subsetsAtLeast(1, P.variables())
                                            ),
                                            ws -> P.maps(ws, P.bigIntegers())
                                    )
                            );
                        }
                );
        for (Pair<MultivariatePolynomial, Map<Variable, BigInteger>> p : take(LIMIT, psFail)) {
            try {
                p.a.applyBigInteger(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesApplyRational() {
        initialize("applyRational(Map<Variable, Rational>)");
        Iterable<Pair<MultivariatePolynomial, Map<Variable, Rational>>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.choose(
                    map(i -> new Pair<>(of(i), new TreeMap<>()), P.withScale(4).bigIntegers()),
                    P.dependentPairsInfiniteSquareRootOrder(
                            filterInfinite(r -> r.degree() > 0, P.multivariatePolynomials()),
                            q -> {
                                List<Variable> us = toList(q.variables());
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
            ps = P.choose(
                    P.dependentPairsInfinite(
                            filterInfinite(r -> r.degree() > 0, P.withScale(4).multivariatePolynomials()),
                            q -> {
                                List<Variable> us = toList(q.variables());
                                return map(
                                        p -> p.b,
                                        P.dependentPairsInfiniteLogarithmicOrder(
                                                map(
                                                        vs -> sort(nub(concat(vs, us))),
                                                        P.withScale(4).subsets(P.variables())
                                                ),
                                                ws -> P.maps(ws, P.rationals())
                                        )
                                );
                            }
                    ),
                    map(i -> new Pair<>(of(i), new TreeMap<>()), P.withScale(4).bigIntegers())
            );
        }
        for (Pair<MultivariatePolynomial, Map<Variable, Rational>> p : take(LIMIT, ps)) {
            p.a.applyRational(p.b);
        }

        Iterable<Pair<MultivariatePolynomial, Map<Variable, Rational>>> psFail =
                P.dependentPairsInfiniteSquareRootOrder(
                        filterInfinite(p -> p.degree() > 0, P.multivariatePolynomials()),
                        m -> {
                            List<Variable> us = toList(m.variables());
                            return map(
                                    p -> p.b,
                                    P.dependentPairsInfiniteLogarithmicOrder(
                                            filterInfinite(
                                                    vs -> !isSubsetOf(us, vs),
                                                    P.subsetsAtLeast(1, P.variables())
                                            ),
                                            ws -> P.maps(ws, P.rationals())
                                    )
                            );
                        }
                );
        for (Pair<MultivariatePolynomial, Map<Variable, Rational>> p : take(LIMIT, psFail)) {
            try {
                p.a.applyRational(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesSubstituteMonomial() {
        initialize("substituteMonomial(Map<Variable, Monomial>)");
        Iterable<Pair<MultivariatePolynomial, Map<Variable, Monomial>>> ps = P.pairsSquareRootOrder(
                P.withScale(4).multivariatePolynomials(),
                P.withElement(
                        new TreeMap<>(),
                        map(
                                p -> p.b,
                                P.dependentPairsInfiniteLogarithmicOrder(
                                        P.withScale(4).subsetsAtLeast(1, P.withScale(4).variables()),
                                        vs -> P.maps(vs, P.withScale(4).monomials())
                                )
                        )
                )
        );
        for (Pair<MultivariatePolynomial, Map<Variable, Monomial>> p : take(LIMIT, ps)) {
            MultivariatePolynomial q = p.a.substituteMonomial(p.b);
            q.validate();
        }
    }

    private void propertiesSubstitute() {
        initialize("substitute(Map<Variable, MultivariatePolynomial>)");
        Iterable<Pair<MultivariatePolynomial, Map<Variable, MultivariatePolynomial>>> ps = P.pairsSquareRootOrder(
                P.withScale(2).withSecondaryScale(1).multivariatePolynomials(),
                P.withElement(
                        new TreeMap<>(),
                        map(
                                p -> p.b,
                                P.dependentPairsInfiniteLogarithmicOrder(
                                        P.withScale(4).subsetsAtLeast(1, P.withScale(4).variables()),
                                        vs -> P.maps(
                                                vs,
                                                P.withScale(2).withSecondaryScale(1).multivariatePolynomials()
                                        )
                                )
                        )
                )
        );
        for (Pair<MultivariatePolynomial, Map<Variable, MultivariatePolynomial>> p : take(LIMIT, ps)) {
            MultivariatePolynomial q = p.a.substitute(p.b);
            q.validate();
        }
    }

    private void propertiesSylvesterMatrix() {
        initialize("sylvesterMatrix(MultivariatePolynomial, Variable)");
        Iterable<Triple<MultivariatePolynomial, MultivariatePolynomial, Variable>> ts;
        if (P instanceof QBarExhaustiveProvider) {
            ts = nub(
                    map(
                            q -> new Triple<>(q.b.a, q.b.b, q.a.a),
                            P.dependentPairsInfiniteLogarithmicOrder(
                                    P.subsetPairs(P.variables()),
                                    p -> P.pairs(
                                            filterInfinite(r -> r != ZERO, P.multivariatePolynomials(Pair.toList(p)))
                                    )
                            )
                    )
            );
        } else {
            ts = map(
                    q -> new Triple<>(q.b.a, q.b.b, q.a.a),
                    P.dependentPairsInfinite(
                            P.subsetPairs(P.withScale(4).variables()),
                            p -> P.pairs(
                                    filterInfinite(
                                            r -> r != ZERO,
                                            P.withScale(4).withSecondaryScale(4)
                                                    .multivariatePolynomials(Pair.toList(p))
                                    )
                            )
                    )
            );
        }
        for (Triple<MultivariatePolynomial, MultivariatePolynomial, Variable> t : take(LIMIT, ts)) {
            PolynomialMatrix m = t.a.sylvesterMatrix(t.b, t.c);
            assertTrue(t, m.isSquare());
            assertEquals(t, m.width(), t.a.degree(t.c) + t.b.degree(t.c));
        }

        Variable a = Variable.of(0);
        Variable b = Variable.of(1);
        Iterable<MultivariatePolynomial> ps = filterInfinite(
                q -> q != ZERO,
                P.multivariatePolynomials(Arrays.asList(a, b))
        );
        for (MultivariatePolynomial p : take(LIMIT, ps)) {
            assertTrue(p, p.sylvesterMatrix(ONE, a).isIdentity());
            assertTrue(p, ONE.sylvesterMatrix(p, a).isIdentity());

            try {
                p.sylvesterMatrix(ZERO, a);
                fail(p);
            } catch (ArithmeticException ignored) {}

            try {
                ZERO.sylvesterMatrix(ZERO, a);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Triple<MultivariatePolynomial, MultivariatePolynomial, Variable>> tsFail = filterInfinite(
                t -> {
                    List<Variable> aVariables = t.a.variables();
                    aVariables.remove(t.c);
                    if (aVariables.size() > 1) {
                        return false;
                    }
                    List<Variable> bVariables = t.b.variables();
                    bVariables.remove(t.c);
                    return bVariables.size() > 1;
                },
                P.triples(
                        P.withScale(4).withSecondaryScale(4).multivariatePolynomials(),
                        P.withScale(4).withSecondaryScale(4).multivariatePolynomials(),
                        P.withScale(4).variables()
                )
        );
        for (Triple<MultivariatePolynomial, MultivariatePolynomial, Variable> t : take(LIMIT, tsFail)) {
            try {
                t.a.sylvesterMatrix(t.b, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesResultant() {
        initialize("resultant(MultivariatePolynomial, Variable)");
        Iterable<Triple<MultivariatePolynomial, MultivariatePolynomial, Variable>> ts;
        if (P instanceof QBarExhaustiveProvider) {
            ts = nub(
                    map(
                            q -> new Triple<>(q.b.a, q.b.b, q.a.a),
                            P.dependentPairsInfiniteLogarithmicOrder(
                                    P.subsetPairs(P.variables()),
                                    p -> P.pairs(
                                            filterInfinite(r -> r != ZERO, P.multivariatePolynomials(Pair.toList(p)))
                                    )
                            )
                    )
            );
        } else {
            ts = map(
                    q -> new Triple<>(q.b.a, q.b.b, q.a.a),
                    P.dependentPairsInfinite(
                            P.subsetPairs(P.withScale(4).variables()),
                            p -> P.pairs(
                                    filterInfinite(
                                            r -> r != ZERO,
                                            P.withScale(4).withSecondaryScale(4)
                                                    .multivariatePolynomials(Pair.toList(p))
                                    )
                            )
                    )
            );
        }
        for (Triple<MultivariatePolynomial, MultivariatePolynomial, Variable> t : take(LIMIT, ts)) {
            t.a.resultant(t.b, t.c);
        }

        Variable a = Variable.of(0);
        Variable b = Variable.of(1);
        Iterable<MultivariatePolynomial> ps = filterInfinite(
                q -> q != ZERO,
                P.withScale(4).withSecondaryScale(4).multivariatePolynomials(Arrays.asList(a, b))
        );
        for (MultivariatePolynomial p : take(LIMIT, ps)) {
            assertEquals(p, p.resultant(ONE, a), Polynomial.ONE);
            assertEquals(p, ONE.resultant(p, a), Polynomial.ONE);

            Polynomial selfResultant = p.resultant(p, a);
            assertEquals(p, selfResultant, p.degree(a) > 0 ? Polynomial.ZERO : Polynomial.ONE);

            try {
                p.resultant(ZERO, a);
                fail(p);
            } catch (ArithmeticException ignored) {}

            try {
                ZERO.resultant(ZERO, a);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Triple<MultivariatePolynomial, MultivariatePolynomial, Variable>> tsFail = filterInfinite(
                t -> {
                    List<Variable> aVariables = t.a.variables();
                    aVariables.remove(t.c);
                    if (aVariables.size() > 1) {
                        return false;
                    }
                    List<Variable> bVariables = t.b.variables();
                    bVariables.remove(t.c);
                    return bVariables.size() > 1;
                },
                P.triples(
                        P.withScale(4).withSecondaryScale(4).multivariatePolynomials(),
                        P.withScale(4).withSecondaryScale(4).multivariatePolynomials(),
                        P.withScale(4).variables()
                )
        );
        for (Triple<MultivariatePolynomial, MultivariatePolynomial, Variable> t : take(LIMIT, tsFail)) {
            try {
                t.a.resultant(t.b, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesPowerReduce() {
        initialize("powerReduce(Map<Variable, Polynomial>");
        Iterable<Pair<MultivariatePolynomial, Map<Variable, Polynomial>>> ps = P.pairsSquareRootOrder(
                P.withScale(4).multivariatePolynomials(),
                P.withElement(
                        new TreeMap<>(),
                        map(
                                p -> p.b,
                                P.dependentPairsInfiniteLogarithmicOrder(
                                        P.withScale(4).subsetsAtLeast(1, P.withScale(4).variables()),
                                        vs -> P.maps(vs, P.withScale(4).monicPolynomialsAtLeast(1))
                                )
                        )
                )
        );
        for (Pair<MultivariatePolynomial, Map<Variable, Polynomial>> p : take(LIMIT, ps)) {
            MultivariatePolynomial q = p.a.powerReduce(p.b);
            q.validate();
            //noinspection Convert2streamapi
            for (Variable v : p.a.variables()) {
                if (p.b.containsKey(v)) {
                    assertTrue(p, q.degree(v) < p.b.get(v).degree());
                }
            }
        }

        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            fixedPoint(q -> q.powerReduce(Collections.emptyMap()), p);
        }

        Iterable<Pair<BigInteger, Map<Variable, Polynomial>>> ps2 = P.pairs(
                P.bigIntegers(),
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).subsetsAtLeast(1, P.withScale(4).variables()),
                                vs -> P.maps(vs, P.withScale(4).monicPolynomialsAtLeast(1))
                        )
                )
        );
        for (Pair<BigInteger, Map<Variable, Polynomial>> p : take(LIMIT, ps2)) {
            MultivariatePolynomial q = of(p.a);
            fixedPoint(r -> r.powerReduce(p.b), q);
        }

        Iterable<Pair<MultivariatePolynomial, Map<Variable, Polynomial>>> psFail = P.pairsSquareRootOrder(
                P.withScale(4).multivariatePolynomials(),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.withScale(4).subsetsAtLeast(1, P.withScale(4).variables()),
                                vs -> filterInfinite(
                                        m -> any(q -> q.degree() < 1 || !q.isMonic(), m.values()),
                                        P.maps(vs, P.withScale(4).polynomials())
                                )
                        )
                )
        );
        for (Pair<MultivariatePolynomial, Map<Variable, Polynomial>> p : take(LIMIT, psFail)) {
            try {
                p.a.powerReduce(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::multivariatePolynomials);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::multivariatePolynomials);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(MultivariatePolynomial)");
        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::multivariatePolynomials);

        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = filterInfinite(
                p -> p.a.degree() != p.b.degree(),
                P.pairs(P.multivariatePolynomials())
        );
        for (Pair<MultivariatePolynomial, MultivariatePolynomial> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.degree(), p.b.degree()));
        }
    }

    private void propertiesReadStrict_String_MonomialOrder() {
        initialize("readStrict(String, MonomialOrder)");
        for (Pair<String, MonomialOrder> p : take(LIMIT, P.pairsLogarithmicOrder(P.strings(), P.monomialOrders()))) {
            readStrict(p.a, p.b);
        }

        Iterable<Pair<MultivariatePolynomial, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                P.multivariatePolynomials(),
                P.monomialOrders()
        );
        for (Pair<MultivariatePolynomial, MonomialOrder> p : take(LIMIT, ps)) {
            Optional<MultivariatePolynomial> op = readStrict(p.a.toString(p.b), p.b);
            MultivariatePolynomial q = op.get();
            q.validate();
            assertEquals(p, p.a, q);
        }
    }

    private void propertiesReadStrict_String() {
        initialize("readStrict(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                MULTIVARIATE_POLYNOMIAL_CHARS,
                P.multivariatePolynomials(),
                MultivariatePolynomial::readStrict,
                MultivariatePolynomial::validate,
                false,
                true
        );
    }

    private void propertiesToString_MonomialOrder() {
        initialize("toString(MonomialOrder)");
        Iterable<Pair<MultivariatePolynomial, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                P.multivariatePolynomials(),
                P.monomialOrders()
        );
        for (Pair<MultivariatePolynomial, MonomialOrder> p : take(LIMIT, ps)) {
            String s = p.a.toString(p.b);
            assertTrue(p, isSubsetOf(s, MULTIVARIATE_POLYNOMIAL_CHARS));
            Optional<MultivariatePolynomial> op = readStrict(s, p.b);
            assertTrue(p, op.isPresent());
            assertEquals(p, op.get(), p.a);
        }
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(
                LIMIT,
                MULTIVARIATE_POLYNOMIAL_CHARS,
                P.multivariatePolynomials(),
                MultivariatePolynomial::readStrict
        );
    }
}
