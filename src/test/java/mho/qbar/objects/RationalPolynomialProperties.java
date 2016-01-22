package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.io.Readers;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Quadruple;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.qbar.objects.RationalPolynomial.sum;
import static mho.qbar.testing.QBarTesting.*;
import static mho.qbar.testing.QBarTesting.propertiesCompareToHelper;
import static mho.qbar.testing.QBarTesting.propertiesEqualsHelper;
import static mho.qbar.testing.QBarTesting.propertiesHashCodeHelper;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.ge;
import static mho.wheels.ordering.Ordering.max;
import static mho.wheels.testing.Testing.*;

public class RationalPolynomialProperties extends QBarTestProperties {
    private static final @NotNull String RATIONAL_POLYNOMIAL_CHARS = "*+-/0123456789^x";

    public RationalPolynomialProperties() {
        super("RationalPolynomial");
    }

    @Override
    protected void testBothModes() {
        propertiesIterator();
        propertiesApply();
        compareImplementationsApply();
        propertiesHasIntegralCoefficients();
        propertiesToPolynomial();
        propertiesCoefficient();
        propertiesOf_List_Rational();
        propertiesOf_Rational();
        propertiesOf_Rational_int();
        propertiesDegree();
        propertiesLeading();
        propertiesAdd();
        propertiesNegate();
        propertiesAbs();
        propertiesSignum();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesMultiply_RationalPolynomial();
        compareImplementationsMultiply_RationalPolynomial();
        propertiesMultiply_Rational();
        propertiesMultiply_BigInteger();
        propertiesMultiply_int();
        propertiesDivide_Rational();
        propertiesDivide_BigInteger();
        propertiesDivide_int();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesShiftRight();
        compareImplementationsShiftRight();
        propertiesSum();
        compareImplementationsSum();
        propertiesProduct();
        compareImplementationsProduct();
        propertiesDelta();
        propertiesPow();
        compareImplementationsPow();
        propertiesSubstitute();
        compareImplementationsSubstitute();
        propertiesDifferentiate();
        propertiesIsMonic();
        propertiesMakeMonic();
        propertiesConstantFactor();
        propertiesDivide_RationalPolynomial();
        compareImplementationsDivide_RationalPolynomial();
        propertiesIsDivisibleBy();
        compareImplementationsIsDivisibleBy();
        propertiesSignedRemainderSequence();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead_String();
        propertiesRead_int_String();
        propertiesFindIn_String();
        propertiesFindIn_int_String();
        propertiesToString();
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            List<Rational> rs = toList(p);
            assertTrue(p, all(r -> r != null, rs));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<Rational> ss) -> of(ss), p);
            testNoRemove(p);
            testHasNext(p);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            List<Rational> rs = toList(p);
            assertTrue(p, last(rs) != Rational.ZERO);
        }
    }

    private static @NotNull Rational apply_naive(@NotNull RationalPolynomial p, @NotNull Rational x) {
        return Rational.sum(
                zipWith((c, i) -> c == Rational.ZERO ? Rational.ZERO : x.pow(i).multiply(c), p, rangeUp(0))
        );
    }

    private void propertiesApply() {
        initialize("apply(Rational)");
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            Rational y = p.a.apply(p.b);
            assertEquals(p, y, apply_naive(p.a, p.b));
        }

        for (Rational i : take(LIMIT, P.rationals())) {
            assertEquals(i, ZERO.apply(i), Rational.ZERO);
            fixedPoint(X::apply, i);
            assertEquals(i, of(Rational.NEGATIVE_ONE, 1).apply(i), i.negate());
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            assertEquals(p, p.apply(Rational.ZERO), p.coefficient(0));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.apply(Rational.ONE), Rational.sum(p));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assertEquals(p, of(p.a).apply(p.b), p.a);
            assertEquals(p, of(Arrays.asList(p.a, Rational.ONE)).apply(p.b), p.b.add(p.a));
            assertEquals(p, of(Arrays.asList(p.a.negate(), Rational.ONE)).apply(p.b), p.b.subtract(p.a));
            assertEquals(p, of(p.a, 1).apply(p.b), p.b.multiply(p.a));
        }

        for (Pair<Integer, Rational> p : take(LIMIT, P.pairs(P.naturalIntegersGeometric(), P.rationals()))) {
            assertEquals(p, of(Rational.ONE, p.a).apply(p.b), p.b.pow(p.a));
        }
    }

    private void compareImplementationsApply() {
        Map<String, Function<Pair<RationalPolynomial, Rational>, Rational>> functions = new LinkedHashMap<>();
        functions.put("naïve", p -> apply_naive(p.a, p.b));
        functions.put("standard", p -> p.a.apply(p.b));
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(P.rationalPolynomials(), P.rationals());
        compareImplementations("apply(Rational)", take(LIMIT, ps), functions);
    }

    private void propertiesHasIntegralCoefficients() {
        initialize("hasIntegralCoefficients");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            p.hasIntegralCoefficients();
        }

        for (RationalPolynomial p : take(LIMIT, map(Polynomial::toRationalPolynomial, P.polynomials()))) {
            assertTrue(p, p.hasIntegralCoefficients());
        }
    }

    private void propertiesToPolynomial() {
        initialize("toPolynomial()");
        for (RationalPolynomial p : take(LIMIT, map(Polynomial::toRationalPolynomial, P.polynomials()))) {
            Polynomial q = p.toPolynomial();
            assertEquals(p, p.toString(), q.toString());
            assertEquals(p, p.degree(), q.degree());
            inverse(RationalPolynomial::toPolynomial, Polynomial::toRationalPolynomial, p);
        }

        Iterable<Pair<RationalPolynomial, BigInteger>> ps = P.pairs(
                map(Polynomial::toRationalPolynomial, P.polynomials()),
                P.bigIntegers()
        );
        for (Pair<RationalPolynomial, BigInteger> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.apply(Rational.of(p.b)).bigIntegerValueExact(), p.a.toPolynomial().apply(p.b));
        }

        Iterable<RationalPolynomial> psFail = filterInfinite(
                p -> any(c -> !c.isInteger(), p),
                P.rationalPolynomials()
        );
        for (RationalPolynomial p : take(LIMIT, psFail)) {
            try {
                p.toPolynomial();
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesCoefficient() {
        initialize("coefficient(int)");
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.rationalPolynomials(),
                P.naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            p.a.coefficient(p.b);
        }

        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, filterInfinite(q -> q.b <= q.a.degree(), ps))) {
            assertEquals(p, p.a.coefficient(p.b), toList(p.a).get(p.b));
        }

        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, filterInfinite(q -> q.b > q.a.degree(), ps))) {
            assertEquals(p, p.a.coefficient(p.b), Rational.ZERO);
        }

        Iterable<Pair<RationalPolynomial, Integer>> psFail = P.pairs(P.rationalPolynomials(), P.negativeIntegers());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.coefficient(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesOf_List_Rational() {
        initialize("of(List<Rational>)");
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            RationalPolynomial p = of(rs);
            p.validate();
            assertTrue(rs, p.degree() < rs.size());
        }

        Iterable<List<Rational>> rss = filterInfinite(
                rs -> rs.isEmpty() || last(rs) != Rational.ZERO,
                P.lists(P.rationals())
        );
        for (List<Rational> rs : take(LIMIT, rss)) {
            fixedPoint(ss -> toList(of(ss)), rs);
        }

        for (List<Rational> rs : take(LIMIT, P.listsWithElement(null, P.rationals()))) {
            try {
                of(rs);
                fail(rs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesOf_Rational() {
        initialize("of(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            RationalPolynomial p = of(r);
            p.validate();
            assertTrue(r, p.degree() == 0 || p.degree() == -1);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            assertEquals(r, of(r).coefficient(0), r);
        }
    }

    private void propertiesOf_Rational_int() {
        initialize("of(Rational, int)");
        Iterable<Pair<Rational, Integer>> ps = P.pairsLogarithmicOrder(
                P.rationals(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial q = of(p.a, p.b);
            q.validate();
        }

        ps = P.pairsLogarithmicOrder(P.nonzeroRationals(), P.naturalIntegersGeometric());
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial q = of(p.a, p.b);
            assertTrue(p, all(c -> c == Rational.ZERO, init(q)));
            assertEquals(p, q.degree(), p.b);
        }

        for (int i : take(LIMIT, P.naturalIntegers())) {
            assertTrue(i, of(Rational.ZERO, i) == ZERO);
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.negativeIntegers()))) {
            try {
                of(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesDegree() {
        initialize("degree()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            int degree = p.degree();
            assertTrue(p, degree >= -1);
        }
    }

    private void propertiesLeading() {
        initialize("leading()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            p.leading();
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            Rational leading = p.leading().get();
            assertNotEquals(p, leading, Rational.ZERO);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            RationalPolynomial p = of(r);
            assertEquals(r, p.leading().get(), p.coefficient(0));
        }
    }

    private void propertiesAdd() {
        initialize("add(RationalPolynomial)");
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            RationalPolynomial sum = p.a.add(p.b);
            sum.validate();
            assertTrue(p, sum.degree() <= max(p.a.degree(), p.b.degree()));
            commutative(RationalPolynomial::add, p);
            inverse(q -> q.add(p.b), (RationalPolynomial q) -> q.subtract(p.b), p.a);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomials(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.add(t.b).apply(t.c), t.a.apply(t.c).add(t.b.apply(t.c)));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    Rational::add,
                    RationalPolynomial::add,
                    p
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, ZERO.add(p), p);
            assertEquals(p, p.add(ZERO), p);
            assertTrue(p, p.add(p.negate()) == ZERO);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial>> ts2 = P.triples(
                P.rationalPolynomials()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial> t : take(LIMIT, ts2)) {
            associative(RationalPolynomial::add, t);
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            RationalPolynomial negative = p.negate();
            negative.validate();
            assertEquals(p, negative.degree(), p.degree());
            involution(RationalPolynomial::negate, p);
            assertTrue(p, p.add(negative) == ZERO);
        }

        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            assertEquals(p, p.a.negate().apply(p.b), p.a.apply(p.b).negate());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            homomorphic(
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    Rational::negate,
                    RationalPolynomial::negate,
                    r
            );
        }

        for (RationalPolynomial p : take(LIMIT, filterInfinite(q -> q != ZERO, P.rationalPolynomials()))) {
            RationalPolynomial negative = p.negate();
            assertNotEquals(p, p, negative);
        }
    }

    private void propertiesAbs() {
        initialize("abs()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            RationalPolynomial abs = p.abs();
            abs.validate();
            assertEquals(p, abs.degree(), p.degree());
            idempotent(RationalPolynomial::abs, p);
            assertNotEquals(p, abs.signum(), -1);
            assertTrue(p, ge(abs, ZERO));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            homomorphic(RationalPolynomial::of, RationalPolynomial::of, Rational::abs, RationalPolynomial::abs, r);
            assertEquals(r, of(r).abs(), of(r.abs()));
        }
    }

    private void propertiesSignum() {
        initialize("signum()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            int signum = p.signum();
            assertEquals(p, signum, Ordering.compare(p, ZERO).toInt());
            assertTrue(p, signum == -1 || signum == 0 || signum == 1);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            homomorphic(RationalPolynomial::of, Function.identity(), Rational::signum, RationalPolynomial::signum, r);
        }
    }

    private static @NotNull RationalPolynomial subtract_simplest(
            @NotNull RationalPolynomial a,
            @NotNull RationalPolynomial b
    ) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(RationalPolynomial)");
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            RationalPolynomial difference = p.a.subtract(p.b);
            difference.validate();
            assertTrue(p, difference.degree() <= max(p.a.degree(), p.b.degree()));
            assertEquals(p, difference, subtract_simplest(p.a, p.b));
            antiCommutative(RationalPolynomial::subtract, RationalPolynomial::negate, p);
            inverse(q -> q.subtract(p.b), (RationalPolynomial q) -> q.add(p.b), p.a);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomials(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.subtract(t.b).apply(t.c), t.a.apply(t.c).subtract(t.b.apply(t.c)));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    Rational::subtract,
                    RationalPolynomial::subtract,
                    p
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, ZERO.subtract(p), p.negate());
            fixedPoint(q -> q.subtract(ZERO), p);
            assertTrue(p, p.subtract(p) == ZERO);
        }
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<RationalPolynomial, RationalPolynomial>, RationalPolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        compareImplementations(
                "subtract(RationalPolynomial)",
                take(LIMIT, P.pairs(P.rationalPolynomials())),
                functions
        );
    }

    private static @NotNull RationalPolynomial multiply_RationalPolynomial_alt(
            @NotNull RationalPolynomial a,
            @NotNull RationalPolynomial b
    ) {
        if (a == ZERO || b == ZERO) return ZERO;
        if (a == ONE) return b;
        if (b == ONE) return a;
        int p = a.degree();
        int q = b.degree();
        if (p < q) {
            return multiply_RationalPolynomial_alt(b, a);
        }
        int r = p + q;
        List<Rational> productCoefficients = new ArrayList<>();
        int k = 0;
        for (; k <= q; k++) {
            Rational coefficient = Rational.ZERO;
            for (int i = 0; i <= k; i++) {
                coefficient = coefficient.add(a.coefficient(k - i).multiply(b.coefficient(i)));
            }
            productCoefficients.add(coefficient);
        }
        for (; k < p; k++) {
            Rational coefficient = Rational.ZERO;
            for (int i = 0; i <= q; i++) {
                coefficient = coefficient.add(a.coefficient(k - i).multiply(b.coefficient(i)));
            }
            productCoefficients.add(coefficient);
        }
        for (; k <= r; k++) {
            Rational coefficient = Rational.ZERO;
            for (int i = k - p; i <= q; i++) {
                coefficient = coefficient.add(a.coefficient(k - i).multiply(b.coefficient(i)));
            }
            productCoefficients.add(coefficient);
        }
        return of(productCoefficients);
    }

    private void propertiesMultiply_RationalPolynomial() {
        initialize("multiply(RationalPolynomial)");
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            RationalPolynomial product = p.a.multiply(p.b);
            product.validate();
            assertEquals(p, product, multiply_RationalPolynomial_alt(p.a, p.b));
            assertTrue(p, p.a == ZERO || p.b == ZERO || product.degree() == p.a.degree() + p.b.degree());
            commutative(RationalPolynomial::multiply, p);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomials(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.add(t.b).apply(t.c), t.a.apply(t.c).add(t.b.apply(t.c)));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    Rational::multiply,
                    RationalPolynomial::multiply,
                    p
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(ONE::multiply, p);
            fixedPoint(q -> q.multiply(ONE), p);
            fixedPoint(q -> q.multiply(p), ZERO);
            fixedPoint(p::multiply, ZERO);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial>> ts2 = P.triples(
                P.rationalPolynomials()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial> t : take(LIMIT, ts2)) {
            associative(RationalPolynomial::multiply, t);
            leftDistributive(RationalPolynomial::add, RationalPolynomial::multiply, t);
            rightDistributive(RationalPolynomial::add, RationalPolynomial::multiply, t);
        }

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.monicRationalPolynomials()))) {
            RationalPolynomial product = p.a.multiply(p.b);
            assertTrue(p, product.isMonic());
        }
    }

    private void compareImplementationsMultiply_RationalPolynomial() {
        Map<String, Function<Pair<RationalPolynomial, RationalPolynomial>, RationalPolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("alt", p -> multiply_RationalPolynomial_alt(p.a, p.b));
        functions.put("standard", p -> p.a.multiply(p.b));
        compareImplementations(
                "multiply(RationalPolynomial)",
                take(LIMIT, P.pairs(P.rationalPolynomials())),
                functions
        );
    }

    private void propertiesMultiply_Rational() {
        initialize("multiply(Rational)");
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            RationalPolynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b == Rational.ZERO || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(of(p.b)));
            assertEquals(p, product, of(p.b).multiply(p.a));
        }

        Iterable<Triple<RationalPolynomial, Rational, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.rationals(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Rational, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.multiply(t.b).apply(t.c), t.a.apply(t.c).multiply(t.b));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::multiply,
                    RationalPolynomial::multiply,
                    p
            );
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ONE.multiply(r), of(r));
            fixedPoint(j -> j.multiply(r), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.multiply(Rational.ONE), p);
            assertTrue(p, p.multiply(Rational.ZERO) == ZERO);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts2 = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomials(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts2)) {
            RationalPolynomial expression1 = t.a.add(t.b).multiply(t.c);
            RationalPolynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        for (Pair<RationalPolynomial, BigInteger> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.bigIntegers()))) {
            RationalPolynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b.equals(BigInteger.ZERO) || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(of(Rational.of(p.b))));
            assertEquals(p, product, of(Rational.of(p.b)).multiply(p.a));
        }

        Iterable<Triple<RationalPolynomial, BigInteger, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.bigIntegers(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, BigInteger, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.multiply(t.b).apply(t.c), t.a.apply(t.c).multiply(t.b));
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.bigIntegers()))) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::multiply,
                    RationalPolynomial::multiply,
                    p
            );
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ONE.multiply(i), of(Rational.of(i)));
            fixedPoint(j -> j.multiply(i), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.multiply(BigInteger.ONE), p);
            assertTrue(p, p.multiply(BigInteger.ZERO) == ZERO);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, BigInteger>> ts2 = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomials(),
                P.bigIntegers()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, BigInteger> t : take(LIMIT, ts2)) {
            RationalPolynomial expression1 = t.a.add(t.b).multiply(t.c);
            RationalPolynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.integers()))) {
            RationalPolynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b == 0 || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(Rational.of(p.b)));
            assertEquals(p, product, of(Rational.of(p.b)).multiply(p.a));
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.integers(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.multiply(t.b).apply(t.c), t.a.apply(t.c).multiply(BigInteger.valueOf(t.b)));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integers()))) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::multiply,
                    RationalPolynomial::multiply,
                    p
            );
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(i, ONE.multiply(i), of(Rational.of(i)));
            fixedPoint(j -> j.multiply(i), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.multiply(1), p);
            assertTrue(p, p.multiply(0) == ZERO);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Integer>> ts2 = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomials(),
                P.integers()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Integer> t : take(LIMIT, ts2)) {
            RationalPolynomial expression1 = t.a.add(t.b).multiply(t.c);
            RationalPolynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }
    }

    private void propertiesDivide_Rational() {
        initialize("divide(Rational)");
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(P.rationalPolynomials(), P.nonzeroRationals());
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            RationalPolynomial quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            inverse(q -> q.divide(p.b), (RationalPolynomial q) -> q.multiply(p.b), p.a);
            assertEquals(p, quotient, p.a.multiply(p.b.invert()));
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
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::divide,
                    RationalPolynomial::divide,
                    p
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.divide(Rational.ONE), p);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            fixedPoint(p -> p.divide(r), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            try {
                p.divide(Rational.ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_BigInteger() {
        initialize("divide(BigInteger)");
        Iterable<Pair<RationalPolynomial, BigInteger>> ps = P.pairs(P.rationalPolynomials(), P.nonzeroBigIntegers());
        for (Pair<RationalPolynomial, BigInteger> p : take(LIMIT, ps)) {
            RationalPolynomial quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            inverse(q -> q.divide(p.b), (RationalPolynomial q) -> q.multiply(p.b), p.a);
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
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::divide,
                    RationalPolynomial::divide,
                    p
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.divide(BigInteger.ONE), p);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            fixedPoint(p -> p.divide(i), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            try {
                p.divide(BigInteger.ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_int() {
        initialize("divide(int)");
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(P.rationalPolynomials(), P.nonzeroIntegers());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            inverse(q -> q.divide(p.b), (RationalPolynomial q) -> q.multiply(p.b), p.a);
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
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::divide,
                    RationalPolynomial::divide,
                    p
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.divide(1), p);
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            fixedPoint(p -> p.divide(i), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            try {
                p.divide(0);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull RationalPolynomial shiftLeft_simplest(@NotNull RationalPolynomial p, int bits) {
        if (bits < 0) {
            return p.divide(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return p.multiply(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(P.rationalPolynomials(), P.integersGeometric());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted.degree(), p.a.degree());
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p.toString(), map(Rational::signum, p.a), map(Rational::signum, shifted));
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
            assertEquals(p, shifted, p.a.shiftRight(-p.b));
            inverse(q -> q.shiftLeft(p.b), (RationalPolynomial q) -> q.shiftRight(p.b), p.a);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.shiftLeft(0), p);
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.integersGeometric(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.shiftLeft(t.b).apply(t.c), t.a.apply(t.c).shiftLeft(t.b));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::shiftLeft,
                    RationalPolynomial::shiftLeft,
                    p
            );
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<RationalPolynomial, Integer>, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        compareImplementations(
                "shiftLeft(int)",
                take(LIMIT, P.pairs(P.rationalPolynomials(), P.integersGeometric())),
                functions
        );
    }

    private static @NotNull RationalPolynomial shiftRight_simplest(@NotNull RationalPolynomial p, int bits) {
        if (bits < 0) {
            return p.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return p.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftRight() {
        initialize("shiftRight(int)");
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(P.rationalPolynomials(), P.integersGeometric());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial shifted = p.a.shiftRight(p.b);
            shifted.validate();
            assertEquals(p, shifted.degree(), p.a.degree());
            assertEquals(p, shifted, shiftRight_simplest(p.a, p.b));
            aeqit(p.toString(), map(Rational::signum, p.a), map(Rational::signum, shifted));
            assertEquals(p, p.a.negate().shiftRight(p.b), shifted.negate());
            assertEquals(p, shifted, p.a.shiftLeft(-p.b));
            inverse(q -> q.shiftRight(p.b), (RationalPolynomial q) -> q.shiftLeft(p.b), p.a);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.shiftRight(0), p);
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.integersGeometric(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.shiftRight(t.b).apply(t.c), t.a.apply(t.c).shiftRight(t.b));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::shiftRight,
                    RationalPolynomial::shiftRight,
                    p
            );
        }
    }

    private void compareImplementationsShiftRight() {
        Map<String, Function<Pair<RationalPolynomial, Integer>, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftRight_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftRight(p.b));
        compareImplementations(
                "shiftRight(int)",
                take(LIMIT, P.pairs(P.rationalPolynomials(), P.integersGeometric())),
                functions
        );
    }

    private static @NotNull RationalPolynomial sum_simplest(@NotNull Iterable<RationalPolynomial> xs) {
        return foldl(RationalPolynomial::add, ZERO, xs);
    }

    private void propertiesSum() {
        initialize("sum(Iterable<RationalPolynomial>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.rationalPolynomials(),
                RationalPolynomial::add,
                RationalPolynomial::sum,
                RationalPolynomial::validate,
                true,
                true
        );

        for (List<RationalPolynomial> ps : take(LIMIT, P.lists(P.rationalPolynomials()))) {
            RationalPolynomial sum = sum(ps);
            assertEquals(ps, sum, sum_simplest(ps));
            assertTrue(ps, ps.isEmpty() || sum.degree() <= maximum(map(RationalPolynomial::degree, ps)));
        }

        Iterable<Pair<List<RationalPolynomial>, Rational>> ps = P.pairs(
                P.lists(P.rationalPolynomials()),
                P.rationals()
        );
        for (Pair<List<RationalPolynomial>, Rational> p : take(LIMIT, ps)) {
            assertEquals(p, sum(p.a).apply(p.b), Rational.sum(map(q -> q.apply(p.b), p.a)));
        }

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            homomorphic(
                    ss -> toList(map(RationalPolynomial::of, ss)),
                    RationalPolynomial::of,
                    Rational::sum,
                    RationalPolynomial::sum,
                    rs
            );
        }
    }

    private void compareImplementationsSum() {
        Map<String, Function<List<RationalPolynomial>, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", RationalPolynomialProperties::sum_simplest);
        functions.put("standard", RationalPolynomial::sum);
        compareImplementations(
                "sum(Iterable<RationalPolynomial>)",
                take(LIMIT, P.lists(P.rationalPolynomials())),
                functions
        );
    }

    private static @NotNull RationalPolynomial product_simplest(@NotNull Iterable<RationalPolynomial> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        if (any(x -> x == ZERO, xs)) {
            return ZERO;
        }
        return foldl(RationalPolynomial::multiply, ONE, xs);
    }

    private static @NotNull RationalPolynomial product_alt(@NotNull List<RationalPolynomial> xs) {
        if (any(x -> x == ZERO, xs)) return ZERO;
        List<Rational> productCoefficients =
                toList(replicate(sumInteger(map(RationalPolynomial::degree, xs)) + 1, Rational.ZERO));
        List<List<Pair<Rational, Integer>>> selections = toList(map(p -> toList(zip(p, rangeUp(0))), xs));
        outer:
        for (List<Pair<Rational, Integer>> selection : EP.cartesianProduct(selections)) {
            Rational coefficient = Rational.ONE;
            int exponent = 0;
            for (Pair<Rational, Integer> p : selection) {
                if (p.a.equals(Rational.ZERO)) continue outer;
                coefficient = coefficient.multiply(p.a);
                exponent += p.b;
            }
            productCoefficients.set(exponent, productCoefficients.get(exponent).add(coefficient));
        }
        return of(productCoefficients);
    }

    private void propertiesProduct() {
        initialize("product(Iterable<RationalPolynomial>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.withScale(4).rationalPolynomials(),
                RationalPolynomial::multiply,
                RationalPolynomial::product,
                RationalPolynomial::validate,
                true,
                true
        );

        for (List<RationalPolynomial> ps : take(LIMIT, P.withScale(4).lists(P.withScale(4).rationalPolynomials()))) {
            RationalPolynomial product = product(ps);
            assertTrue(
                    ps,
                    any(p -> p == ZERO, ps) ||
                            product.degree() == IterableUtils.sumInteger(map(RationalPolynomial::degree, ps))
            );
        }

        Iterable<List<RationalPolynomial>> pss = P.withScale(1).lists(P.withSecondaryScale(1).rationalPolynomials());
        for (List<RationalPolynomial> ps : take(LIMIT, pss)) {
            RationalPolynomial product = product(ps);
            assertEquals(ps, product, product_simplest(ps));
            assertEquals(ps, product, product_alt(ps));
        }

        Iterable<Pair<List<RationalPolynomial>, Rational>> ps2 = P.pairs(
                P.withScale(4).lists(P.withScale(4).rationalPolynomials()),
                P.rationals()
        );
        for (Pair<List<RationalPolynomial>, Rational> p : take(LIMIT, ps2)) {
            assertEquals(p, product(p.a).apply(p.b), Rational.product(map(q -> q.apply(p.b), p.a)));
        }

        for (List<Rational> rs : take(LIMIT, P.lists(P.withScale(4).rationals()))) {
            homomorphic(
                    ss -> toList(map(RationalPolynomial::of, ss)),
                    RationalPolynomial::of,
                    Rational::product,
                    RationalPolynomial::product,
                    rs
            );
        }
    }

    private void compareImplementationsProduct() {
        Map<String, Function<List<RationalPolynomial>, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", RationalPolynomialProperties::product_simplest);
        functions.put("alt", RationalPolynomialProperties::product_alt);
        functions.put("standard", RationalPolynomial::product);
        compareImplementations(
                "product(Iterable<RationalPolynomial>)",
                take(LIMIT, P.withScale(1).lists(P.withSecondaryScale(1).rationalPolynomials())),
                functions
        );
    }

    private void propertiesDelta() {
        initialize("delta(Iterable<RationalPolynomial>)");
        propertiesDeltaHelper(
                LIMIT,
                P.getWheelsProvider(),
                QEP.rationalPolynomials(),
                P.rationalPolynomials(),
                RationalPolynomial::negate,
                RationalPolynomial::subtract,
                RationalPolynomial::delta,
                RationalPolynomial::validate
        );

        Iterable<Pair<List<RationalPolynomial>, Rational>> ps = P.pairs(
                P.listsAtLeast(1, P.rationalPolynomials()),
                P.rationals()
        );
        for (Pair<List<RationalPolynomial>, Rational> p : take(LIMIT, ps)) {
            aeqit(p.toString(), map(q -> q.apply(p.b), delta(p.a)), Rational.delta(map(q -> q.apply(p.b), p.a)));
        }

        for (List<Rational> rs : take(LIMIT, P.listsAtLeast(1, P.rationals()))) {
            homomorphic(
                    ss -> toList(map(RationalPolynomial::of, ss)),
                    ss -> toList(map(RationalPolynomial::of, ss)),
                    ss -> toList(Rational.delta(ss)),
                    qs -> toList(delta(qs)),
                    rs
            );
        }
    }

    private static @NotNull RationalPolynomial pow_simplest(@NotNull RationalPolynomial a, int p) {
        return product(replicate(p, a));
    }

    private void propertiesPow() {
        initialize("pow(int)");
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).withSecondaryScale(4).rationalPolynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial q = p.a.pow(p.b);
            q.validate();
            assertTrue(p, p.a == ZERO || q.degree() == p.a.degree() * p.b);
            assertEquals(p, q, pow_simplest(p.a, p.b));
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts = P.triples(
                P.withScale(3).withSecondaryScale(1).rationalPolynomials(),
                P.withScale(1).naturalIntegersGeometric(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.pow(t.b).apply(t.c), t.a.apply(t.c).pow(t.b));
        }

        Iterable<Pair<Rational, Integer>> ps2 = P.pairsLogarithmicOrder(
                P.rationals(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps2)) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::pow,
                    RationalPolynomial::pow,
                    p
            );
        }

        for (int i : take(LIMIT, P.withScale(4).positiveIntegersGeometric())) {
            fixedPoint(p -> p.pow(i), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.withScale(4).withSecondaryScale(4).rationalPolynomials())) {
            assertTrue(p, p.pow(0) == ONE);
            fixedPoint(q -> q.pow(1), p);
            assertEquals(p, p.pow(2), p.multiply(p));
        }

        Iterable<Triple<RationalPolynomial, Integer, Integer>> ts2 = P.triples(
                P.withScale(3).withSecondaryScale(1).rationalPolynomials(),
                P.withScale(1).naturalIntegersGeometric(),
                P.withScale(1).naturalIntegersGeometric()
        );
        for (Triple<RationalPolynomial, Integer, Integer> t : take(LIMIT, ts2)) {
            RationalPolynomial expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            RationalPolynomial expression2 = t.a.pow(t.b + t.c);
            assertEquals(t, expression1, expression2);
            RationalPolynomial expression5 = t.a.pow(t.b).pow(t.c);
            RationalPolynomial expression6 = t.a.pow(t.b * t.c);
            assertEquals(t, expression5, expression6);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Integer>> ts3 = P.triples(
                P.withScale(3).withSecondaryScale(1).rationalPolynomials(),
                P.withScale(3).withSecondaryScale(1).rationalPolynomials(),
                P.withScale(1).naturalIntegersGeometric()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Integer> t : take(LIMIT, ts3)) {
            RationalPolynomial expression1 = t.a.multiply(t.b).pow(t.c);
            RationalPolynomial expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        Iterable<Pair<RationalPolynomial, Integer>> psFail = P.pairs(P.rationalPolynomials(), P.negativeIntegers());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.pow(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsPow() {
        Map<String, Function<Pair<RationalPolynomial, Integer>, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> pow_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.pow(p.b));
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalPolynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        compareImplementations("pow(int)", take(LIMIT, ps), functions);
    }

    private static @NotNull RationalPolynomial substitute_naive(
            @NotNull RationalPolynomial a,
            @NotNull RationalPolynomial b
    ) {
        return sum(zipWith((c, i) -> c == Rational.ZERO ? ZERO : b.pow(i).multiply(c), a, rangeUp(0)));
    }

    private void propertiesSubstitute() {
        initialize("substitute(RationalPolynomial)");
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(4).rationalPolynomials()
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            RationalPolynomial substituted = p.a.substitute(p.b);
            substituted.validate();
            assertTrue(p, p.b == ZERO || substituted == ZERO || substituted.degree() == p.a.degree() * p.b.degree());
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts = P.triples(
                P.withScale(4).withSecondaryScale(4).rationalPolynomials(),
                P.withScale(4).withSecondaryScale(4).rationalPolynomials(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.substitute(t.b).apply(t.c), t.a.apply(t.b.apply(t.c)));
        }

        for (Pair<Rational, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationals(), P.rationalPolynomials()))) {
            RationalPolynomial q = of(p.a);
            assertEquals(p, q.substitute(p.b), q);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.substitute(ZERO), p == ZERO ? ZERO : of(p.coefficient(0)));
            assertEquals(p, p.substitute(ONE), of(Rational.sum(p)));
            fixedPoint(q -> q.substitute(X), p);
        }
    }

    private void compareImplementationsSubstitute() {
        Map<String, Function<Pair<RationalPolynomial, RationalPolynomial>, RationalPolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("naïve", p -> substitute_naive(p.a, p.b));
        functions.put("standard", p -> p.a.substitute(p.b));
        compareImplementations(
                "substitute(RationalPolynomial)",
                take(LIMIT, P.pairs(P.withScale(4).withSecondaryScale(4).rationalPolynomials())),
                functions
        );
    }

    private void propertiesDifferentiate() {
        initialize("differentiate()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            RationalPolynomial derivative = p.differentiate();
            derivative.validate();
            assertEquals(p, last(take(p.degree() + 2, iterate(RationalPolynomial::differentiate, p))), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials(0))) {
            assertEquals(p, p.differentiate(), ZERO);
        }
    }

    private void propertiesIsMonic() {
        initialize("isMonic()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            boolean isMonic = p.isMonic();
            Optional<Rational> leading = p.leading();
            assertEquals(p, isMonic, leading.isPresent() && leading.get() == Rational.ONE);
        }
    }

    private void propertiesMakeMonic() {
        initialize("makeMonic()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            RationalPolynomial monic = p.makeMonic();
            monic.validate();
            assertEquals(p, monic.degree(), p.degree());
            assertTrue(p, monic.isMonic());
            idempotent(RationalPolynomial::makeMonic, p);
            assertEquals(p, p.negate().makeMonic(), monic);
            assertTrue(
                    p,
                    same(
                            zipWith(
                                    Rational::divide,
                                    filter(r -> r != Rational.ZERO, p),
                                    filter(r -> r != Rational.ZERO, monic)
                            )
                    )
            );
        }

        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(
                P.rationalPolynomialsAtLeast(0),
                P.nonzeroRationals()
        );
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            RationalPolynomial monic = p.a.makeMonic();
            assertEquals(p, p.a.multiply(p.b).makeMonic(), monic);
        }
    }

    private void propertiesConstantFactor() {
        initialize("constantFactor()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            Pair<Rational, Polynomial> contentAndPrimitive = p.constantFactor();
            Rational constantFactor = contentAndPrimitive.a;
            assertNotNull(p, constantFactor);
            Polynomial absolutePrimitive = contentAndPrimitive.b;
            assertEquals(p, absolutePrimitive.degree(), p.degree());
            assertNotNull(p, absolutePrimitive);
            assertTrue(p, absolutePrimitive.isPrimitive());
            assertEquals(p, absolutePrimitive.toRationalPolynomial().multiply(constantFactor), p);
            assertEquals(p, absolutePrimitive.signum(), 1);
            idempotent(q -> q.constantFactor().b.toRationalPolynomial(), p);
        }
    }

    private static @NotNull Pair<RationalPolynomial, RationalPolynomial> divide_RationalPolynomial_simplest(
            @NotNull RationalPolynomial a,
            @NotNull RationalPolynomial b
    ) {
        RationalPolynomial quotient = ZERO;
        RationalPolynomial remainder = a;
        while (remainder != ZERO && remainder.degree() >= b.degree()) {
            RationalPolynomial t = of(
                    remainder.leading().get().divide(b.leading().get()),
                    remainder.degree() - b.degree()
            );
            quotient = quotient.add(t);
            remainder = remainder.subtract(t.multiply(b));
        }
        return new Pair<>(quotient, remainder);
    }

    private void propertiesDivide_RationalPolynomial() {
        initialize("divide(RationalPolynomial)");
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomials(),
                P.rationalPolynomialsAtLeast(0)
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            Pair<RationalPolynomial, RationalPolynomial> quotRem = p.a.divide(p.b);
            assertEquals(p, quotRem, divide_RationalPolynomial_simplest(p.a, p.b));
            RationalPolynomial quotient = quotRem.a;
            assertNotNull(p, quotient);
            RationalPolynomial remainder = quotRem.b;
            assertNotNull(p, remainder);
            quotient.validate();
            remainder.validate();
            assertEquals(p, quotient.multiply(p.b).add(remainder), p.a);
            assertTrue(p, remainder.degree() < p.b.degree());
        }

        Iterable<Quadruple<RationalPolynomial, RationalPolynomial, Rational, Rational>> qs = P.quadruples(
                P.rationalPolynomials(),
                P.rationalPolynomialsAtLeast(0),
                P.rationals(),
                P.nonzeroRationals()
        );
        for (Quadruple<RationalPolynomial, RationalPolynomial, Rational, Rational> q : take(LIMIT, qs)) {
            assertEquals(q, q.a.multiply(q.c).divide(q.b.multiply(q.d)).b, q.a.divide(q.b).b.multiply(q.c));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroRationals()))) {
            Pair<RationalPolynomial, RationalPolynomial> quotRem = of(p.a).divide(of(p.b));
            assertEquals(p, quotRem.a, of(p.a.divide(p.b)));
            assertEquals(p, quotRem.b, ZERO);
        }

        ps = filterInfinite(
                q -> q.a.degree() < q.b.degree(),
                P.pairs(P.rationalPolynomials(), P.rationalPolynomialsAtLeast(0))
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            Pair<RationalPolynomial, RationalPolynomial> quotRem = p.a.divide(p.b);
            assertEquals(p, quotRem.a, ZERO);
            assertEquals(p, quotRem.b, p.a);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            try {
                p.divide(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsDivide_RationalPolynomial() {
        Map<
                String,
                Function<Pair<RationalPolynomial, RationalPolynomial>, Pair<RationalPolynomial, RationalPolynomial>>
        > functions = new LinkedHashMap<>();
        functions.put("simplest", p -> divide_RationalPolynomial_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.divide(p.b));
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomials(),
                P.rationalPolynomialsAtLeast(0)
        );
        compareImplementations("divide(RationalPolynomial)", take(LIMIT, ps), functions);
    }

    private static boolean isDivisibleBy_simplest(@NotNull RationalPolynomial a, @NotNull RationalPolynomial b) {
        return a.divide(b).b == ZERO;
    }

    private void propertiesIsDivisibleBy() {
        initialize("isDivisibleBy(RationalPolynomial)");
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomials(),
                P.rationalPolynomialsAtLeast(0)
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            boolean isDivisible = p.a.isDivisibleBy(p.b);
            assertEquals(p, isDivisible, isDivisibleBy_simplest(p.a, p.b));
            assertEquals(p, isDivisible, p.a.equals(p.a.divide(p.b).a.multiply(p.b)));
            assertTrue(p, p.a.multiply(p.b).isDivisibleBy(p.b));
        }

        ps = P.pairs(P.rationalPolynomials(), P.rationalPolynomials(0));
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            assertTrue(p, p.a.isDivisibleBy(p.b));
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomialsAtLeast(0),
                P.nonzeroRationals()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts)) {
            boolean isDivisible = t.a.isDivisibleBy(t.b);
            assertEquals(t, isDivisible, t.a.multiply(t.c).isDivisibleBy(t.b));
            assertEquals(t, isDivisible, t.a.isDivisibleBy(t.b.multiply(t.c)));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            try {
                p.isDivisibleBy(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsIsDivisibleBy() {
        Map<String, Function<Pair<RationalPolynomial, RationalPolynomial>, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> isDivisibleBy_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.isDivisibleBy(p.b));
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomials(),
                P.rationalPolynomialsAtLeast(0)
        );
        compareImplementations("isDivisibleBy(RationalPolynomial)", take(LIMIT, ps), functions);
    }

    private void propertiesSignedRemainderSequence() {
        initialize("signedRemainderSequence(RationalPolynomial)");
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = filterInfinite(
                p -> p.a != ZERO || p.b != ZERO,
                P.pairs(P.withScale(4).rationalPolynomials())
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            List<RationalPolynomial> sequence = p.a.signedRemainderSequence(p.b);
            assertFalse(p, sequence.isEmpty());
            assertNotEquals(p, last(sequence), ZERO);
            //todo GCD
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            assertEquals(p, p.signedRemainderSequence(ZERO), Collections.singletonList(p));
            assertEquals(p, ZERO.signedRemainderSequence(p), Arrays.asList(ZERO, p));
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::rationalPolynomials);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::rationalPolynomials);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(RationalPolynomial)");
        propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::rationalPolynomials);

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            assertEquals(p, p.a.compareTo(p.b), p.a.subtract(p.b).signum());
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    Function.identity(),
                    Rational::compareTo,
                    RationalPolynomial::compareTo,
                    p
            );
        }

        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = filterInfinite(
                r -> r.a.degree() != r.b.degree(),
                P.pairs(filter(q -> q.signum() == 1, P.rationalPolynomials()))
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            homomorphic(
                    RationalPolynomial::degree,
                    RationalPolynomial::degree,
                    Function.identity(),
                    RationalPolynomial::compareTo,
                    Integer::compareTo,
                    p
            );
        }
    }

    private void propertiesRead_String() {
        initialize("read(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                RATIONAL_POLYNOMIAL_CHARS,
                P.rationalPolynomials(),
                RationalPolynomial::read,
                RationalPolynomial::validate,
                false
        );
    }

    private void propertiesRead_int_String() {
        initialize("read(int, String)");

        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(P.strings(), P.positiveIntegersGeometric());
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            read(p.b, p.a);
        }

        Iterable<Pair<RationalPolynomial, Integer>> ps2 = filterInfinite(
                p -> p.a.degree() <= p.b,
                P.pairsLogarithmicOrder(P.rationalPolynomials(), P.positiveIntegersGeometric())
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps2)) {
            Optional<RationalPolynomial> op = read(p.b, p.a.toString());
            RationalPolynomial q = op.get();
            q.validate();
            assertEquals(p, q, p.a);
        }

        ps2 = filterInfinite(
                p -> p.a.degree() > p.b,
                P.pairsLogarithmicOrder(P.rationalPolynomials(), P.positiveIntegersGeometric())
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps2)) {
            Optional<RationalPolynomial> op = read(p.b, p.a.toString());
            assertFalse(p, op.isPresent());
        }
    }

    private static @NotNull Optional<String> badString(@NotNull String s) {
        boolean seenX = false;
        boolean seenXCaret = false;
        int exponentDigitCount = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == 'x') {
                seenX = true;
            } else if (seenX && c == '^') {
                seenXCaret = true;
            } else if (seenXCaret && c >= '0' && c <= '9') {
                exponentDigitCount++;
                if (exponentDigitCount > 3) return Optional.of("");
            } else {
                seenX = false;
                seenXCaret = false;
                exponentDigitCount = 0;
            }
        }
        return Optional.empty();
    }

    private void propertiesFindIn_String() {
        initialize("findIn(String)");
        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.rationalPolynomials(),
                s -> badString(s).isPresent() ? Optional.empty() : read(s),
                s -> badString(s).isPresent() ? Optional.empty() : findIn(s),
                RationalPolynomial::validate
        );
    }

    private void propertiesFindIn_int_String() {
        initialize("findIn(int, String)");
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                filterInfinite(
                        s -> !Readers.genericFindIn(RationalPolynomialProperties::badString).apply(s).isPresent(),
                        P.strings()
                ),
                P.positiveIntegersGeometric()
        );
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            findIn(p.b, p.a);
        }

        Iterable<Pair<Integer, String>> ps2 = P.dependentPairsInfiniteLogarithmicOrder(
                P.positiveIntegersGeometric(),
                i -> P.stringsWithSubstrings(
                        map(Object::toString, filterInfinite(p -> p.degree() <= i, P.rationalPolynomials()))
                )
        );
        for (Pair<Integer, String> p : take(LIMIT, ps2)) {
            Optional<Pair<RationalPolynomial, Integer>> op = findIn(p.a, p.b);
            Pair<RationalPolynomial, Integer> q = op.get();
            assertNotNull(p, q.a);
            assertNotNull(p, q.b);
            q.a.validate();
            assertTrue(p, q.b >= 0 && q.b < p.b.length());
            String before = take(q.b, p.b);
            assertFalse(p, findIn(before).isPresent());
            String during = q.a.toString();
            assertTrue(p, p.b.substring(q.b).startsWith(during));
            String after = drop(q.b + during.length(), p.b);
            assertTrue(p, after.isEmpty() || !read(during + head(after)).isPresent());
        }
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, RATIONAL_POLYNOMIAL_CHARS, P.rationalPolynomials(), RationalPolynomial::read);

        for (Rational r : take(LIMIT, P.rationals())) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    Rational::toString,
                    RationalPolynomial::toString,
                    r
            );
        }
    }
}
