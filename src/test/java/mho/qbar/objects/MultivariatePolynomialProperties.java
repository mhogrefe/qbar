package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
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

    public MultivariatePolynomialProperties() {
        super("MultivariatePolynomial");
    }

    @Override
    protected void testBothModes() {
        propertiesIterable();
        propertiesIterator();
        propertiesCoefficient();
        propertiesOf_List_Pair_Monomial_BigInteger();
        propertiesOf_Monomial_BigInteger();
        compareImplementationsOf_Monomial_BigInteger();
        propertiesOf_BigInteger();
        propertiesOf_int();
        propertiesOf_Polynomial_Variable();
        propertiesToPolynomial();
        propertiesVariables();
        propertiesVariableCount();
        propertiesTermCount();
        propertiesMaxCoefficientBitLength();
        propertiesDegree();
        propertiesIsHomogeneous();
        propertiesCoefficientsOfVariable();
        propertiesAdd();
        compareImplementationsAdd();
        propertiesNegate();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesMultiply_int();
        propertiesMultiply_BigInteger();
        propertiesMultiply_Monomial_BigInteger();
        propertiesMultiply_MultivariatePolynomial();
        compareImplementationsMultiply_MultivariatePolynomial();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
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
        compareImplementations("of(Monomial, BigInteger)", take(LIMIT, ps), functions);
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
                                            rangeUp(0)
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
    }

    private void compareImplementationsAdd() {
        Map<String, Function<Pair<MultivariatePolynomial, MultivariatePolynomial>, MultivariatePolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> add_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.add(p.b));
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(P.multivariatePolynomials());
        compareImplementations("add(MultivariatePolynomial)", take(LIMIT, ps), functions);
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
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<MultivariatePolynomial, MultivariatePolynomial>, MultivariatePolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(P.multivariatePolynomials());
        compareImplementations("subtract(MultivariatePolynomial)", take(LIMIT, ps), functions);
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
    }

    private void compareImplementationsMultiply_MultivariatePolynomial() {
        Map<String, Function<Pair<MultivariatePolynomial, MultivariatePolynomial>, MultivariatePolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("alt", p -> multiply_MultivariatePolynomial_alt(p.a, p.b));
        functions.put("standard", p -> p.a.multiply(p.b));
        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = P.pairs(P.multivariatePolynomials());
        compareImplementations("multiply(MultivariatePolynomial)", take(LIMIT, ps), functions);
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
                functions
        );
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
