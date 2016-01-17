package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.io.Readers;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.qbar.objects.Polynomial.*;
import static mho.qbar.objects.Polynomial.sum;
import static mho.qbar.testing.QBarTesting.*;
import static mho.qbar.testing.QBarTesting.propertiesCompareToHelper;
import static mho.qbar.testing.QBarTesting.propertiesEqualsHelper;
import static mho.qbar.testing.QBarTesting.propertiesHashCodeHelper;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class PolynomialProperties extends QBarTestProperties {
    private static final @NotNull String POLYNOMIAL_CHARS = "*+-0123456789^x";

    public PolynomialProperties() {
        super("Polynomial");
    }

    @Override
    protected void testBothModes() {
        propertiesIterator();
        propertiesApply_BigInteger();
        compareImplementationsApply_BigInteger();
        propertiesApply_Rational();
        compareImplementationsApply_Rational();
        propertiesToRationalPolynomial();
        propertiesCoefficient();
        propertiesOf_List_BigInteger();
        propertiesOf_BigInteger();
        propertiesOf_BigInteger_int();
        propertiesDegree();
        propertiesLeading();
        propertiesAdd();
        propertiesNegate();
        propertiesAbs();
        propertiesSignum();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesMultiply_Polynomial();
        compareImplementationsMultiply_Polynomial();
        propertiesMultiply_BigInteger();
        propertiesMultiply_int();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
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
        propertiesIsPrimitive();
        propertiesContentAndPrimitive();
        propertiesConstantFactor();
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
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            List<BigInteger> is = toList(p);
            assertTrue(p, all(r -> r != null, is));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<BigInteger> js) -> of(js), p);
            testNoRemove(p);
            testHasNext(p);
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            List<BigInteger> is = toList(p);
            assertTrue(p, !last(is).equals(BigInteger.ZERO));
        }
    }

    private static @NotNull BigInteger apply_BigInteger_naive(@NotNull Polynomial p, @NotNull BigInteger x) {
        return sumBigInteger(
                zipWith((c, i) -> c.equals(BigInteger.ZERO) ? BigInteger.ZERO : x.pow(i).multiply(c), p, rangeUp(0))
        );
    }

    private void propertiesApply_BigInteger() {
        initialize("apply(BigInteger)");
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            BigInteger y = p.a.apply(p.b);
            assertEquals(p, y, apply_BigInteger_naive(p.a, p.b));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ZERO.apply(i), BigInteger.ZERO);
            fixedPoint(X::apply, i);
            assertEquals(i, of(IntegerUtils.NEGATIVE_ONE, 1).apply(i), i.negate());
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.apply(BigInteger.ZERO), p.coefficient(0));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, p.apply(BigInteger.ONE), sumBigInteger(p));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            assertEquals(p, of(p.a).apply(p.b), p.a);
            assertEquals(p, of(Arrays.asList(p.a, BigInteger.ONE)).apply(p.b), p.b.add(p.a));
            assertEquals(p, of(Arrays.asList(p.a.negate(), BigInteger.ONE)).apply(p.b), p.b.subtract(p.a));
            assertEquals(p, of(p.a, 1).apply(p.b), p.b.multiply(p.a));
        }

        for (Pair<Integer, BigInteger> p : take(LIMIT, P.pairs(P.naturalIntegersGeometric(), P.bigIntegers()))) {
            assertEquals(p, of(BigInteger.ONE, p.a).apply(p.b), p.b.pow(p.a));
        }
    }

    private void compareImplementationsApply_BigInteger() {
        Map<String, Function<Pair<Polynomial, BigInteger>, BigInteger>> functions = new LinkedHashMap<>();
        functions.put("naïve", p -> apply_BigInteger_naive(p.a, p.b));
        functions.put("standard", p -> p.a.apply(p.b));
        compareImplementations("apply(BigInteger)", take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers())), functions);
    }

    private static @NotNull Rational apply_Rational_simplest(@NotNull Polynomial p, @NotNull Rational x) {
        return p.toRationalPolynomial().apply(x);
    }

    private static @NotNull Rational apply_Rational_naive(@NotNull Polynomial p, @NotNull Rational x) {
        return Rational.sum(
                zipWith((c, i) -> c.equals(BigInteger.ZERO) ? Rational.ZERO : x.pow(i).multiply(c), p, rangeUp(0))
        );
    }

    private static @NotNull Rational apply_Rational_alt(@NotNull Polynomial p, @NotNull Rational x) {
        if (x.isInteger()) {
            return Rational.of(p.apply(x.bigIntegerValueExact()));
        } else {
            return foldr((c, y) -> x.multiply(y).add(Rational.of(c)), Rational.ZERO, p);
        }
    }

    private void propertiesApply_Rational() {
        initialize("apply(Rational)");
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            Rational y = p.a.apply(p.b);
            assertEquals(p, y, apply_Rational_simplest(p.a, p.b));
            assertEquals(p, y, apply_Rational_naive(p.a, p.b));
            assertEquals(p, y, apply_Rational_alt(p.a, p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ZERO.apply(r), Rational.ZERO);
            fixedPoint(X::apply, r);
            assertEquals(r, of(IntegerUtils.NEGATIVE_ONE, 1).apply(r), r.negate());
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.apply(Rational.ZERO), Rational.of(p.coefficient(0)));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, p.apply(Rational.ONE), Rational.of(sumBigInteger(p)));
        }

        for (Pair<BigInteger, Rational> p : take(LIMIT, P.pairs(P.bigIntegers(), P.rationals()))) {
            assertEquals(p, of(p.a).apply(p.b), Rational.of(p.a));
            assertEquals(p, of(Arrays.asList(p.a, BigInteger.ONE)).apply(p.b), p.b.add(Rational.of(p.a)));
            assertEquals(
                    p,
                    of(Arrays.asList(p.a.negate(), BigInteger.ONE)).apply(p.b),
                    p.b.subtract(Rational.of(p.a))
            );
            assertEquals(p, of(p.a, 1).apply(p.b), p.b.multiply(p.a));
        }

        for (Pair<Integer, Rational> p : take(LIMIT, P.pairs(P.naturalIntegersGeometric(), P.rationals()))) {
            assertEquals(p, of(BigInteger.ONE, p.a).apply(p.b), p.b.pow(p.a));
        }
    }

    private void compareImplementationsApply_Rational() {
        Map<String, Function<Pair<Polynomial, Rational>, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> apply_Rational_simplest(p.a, p.b));
        functions.put("naïve", p -> apply_Rational_naive(p.a, p.b));
        functions.put("alt", p -> apply_Rational_alt(p.a, p.b));
        functions.put("standard", p -> p.a.apply(p.b));
        compareImplementations("apply(Rational)", take(LIMIT, P.pairs(P.polynomials(), P.rationals())), functions);
    }

    private void propertiesToRationalPolynomial() {
        initialize("toRationalPolynomial()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            RationalPolynomial rp = p.toRationalPolynomial();
            assertEquals(p, p.toString(), rp.toString());
            assertEquals(p, p.degree(), rp.degree());
        }

        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            assertEquals(p, p.a.apply(p.b), p.a.toRationalPolynomial().apply(Rational.of(p.b)).bigIntegerValueExact());
        }

        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            assertEquals(p, p.a.apply(p.b), p.a.toRationalPolynomial().apply(p.b));
        }
    }

    private void propertiesCoefficient() {
        initialize("coefficient(int)");
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.polynomials(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            p.a.coefficient(p.b);
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, filterInfinite(q -> q.b <= q.a.degree(), ps))) {
            assertEquals(p, p.a.coefficient(p.b), toList(p.a).get(p.b));
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, filterInfinite(q -> q.b > q.a.degree(), ps))) {
            assertEquals(p, p.a.coefficient(p.b), BigInteger.ZERO);
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.coefficient(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesOf_List_BigInteger() {
        initialize("of(List<BigInteger>)");
        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            Polynomial p = of(is);
            p.validate();
            assertTrue(is, p.degree() < is.size());
        }

        Iterable<List<BigInteger>> iss = filterInfinite(
                is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                P.lists(P.bigIntegers())
        );
        for (List<BigInteger> is : take(LIMIT, iss)) {
            fixedPoint(js -> toList(of(js)), is);
        }

        for (List<BigInteger> is : take(LIMIT, P.listsWithElement(null, P.bigIntegers()))) {
            try {
                of(is);
                fail(is);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesOf_BigInteger() {
        initialize("of(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Polynomial p = of(i);
            p.validate();
            assertTrue(i, p.degree() == 0 || p.degree() == -1);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            assertEquals(i, of(i).coefficient(0), i);
        }
    }

    private void propertiesOf_BigInteger_int() {
        initialize("of(BigInteger, int)");
        Iterable<Pair<BigInteger, Integer>> ps = P.pairsLogarithmicOrder(
                P.bigIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            Polynomial q = of(p.a, p.b);
            q.validate();
        }

        ps = P.pairsLogarithmicOrder(P.nonzeroBigIntegers(), P.naturalIntegersGeometric());
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            Polynomial q = of(p.a, p.b);
            assertTrue(p, all(c -> c.equals(BigInteger.ZERO), init(q)));
            assertEquals(p, q.degree(), p.b);
        }

        for (int i : take(LIMIT, P.naturalIntegers())) {
            assertTrue(i, of(BigInteger.ZERO, i) == ZERO);
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.negativeIntegers()))) {
            try {
                of(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesDegree() {
        initialize("degree()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            int degree = p.degree();
            assertTrue(p, degree >= -1);
        }
    }

    private void propertiesLeading() {
        initialize("leading()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            p.leading();
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            BigInteger leading = p.leading().get();
            assertNotEquals(p, leading, BigInteger.ZERO);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            Polynomial p = of(i);
            assertEquals(i, p.leading().get(), p.coefficient(0));
        }
    }

    private void propertiesAdd() {
        initialize("add(Polynomial)");
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial sum = p.a.add(p.b);
            sum.validate();
            assertTrue(p, sum.degree() <= max(p.a.degree(), p.b.degree()));
            commutative(Polynomial::add, p);
            inverse(q -> q.add(p.b), (Polynomial q) -> q.subtract(p.b), p.a);
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.add(t.b).apply(t.c), t.a.apply(t.c).add(t.b.apply(t.c)));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(Polynomial::of, Polynomial::of, Polynomial::of, BigInteger::add, Polynomial::add, p);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, ZERO.add(p), p);
            assertEquals(p, p.add(ZERO), p);
            assertTrue(p, p.add(p.negate()) == ZERO);
        }

        for (Triple<Polynomial, Polynomial, Polynomial> t : take(LIMIT, P.triples(P.polynomials()))) {
            associative(Polynomial::add, t);
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Polynomial negative = p.negate();
            negative.validate();
            assertEquals(p, negative.degree(), p.degree());
            involution(Polynomial::negate, p);
            assertTrue(p, p.add(negative) == ZERO);
        }

        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            assertEquals(p, p.a.negate().apply(p.b), p.a.apply(p.b).negate());
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            homomorphic(Polynomial::of, Polynomial::of, BigInteger::negate, Polynomial::negate, i);
        }

        for (Polynomial p : take(LIMIT, filterInfinite(q -> q != ZERO, P.polynomials()))) {
            Polynomial negative = p.negate();
            assertNotEquals(p, p, negative);
        }
    }

    private void propertiesAbs() {
        initialize("abs()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Polynomial abs = p.abs();
            abs.validate();
            assertEquals(p, abs.degree(), p.degree());
            idempotent(Polynomial::abs, p);
            assertNotEquals(p, abs.signum(), -1);
            assertTrue(p, ge(abs, ZERO));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            homomorphic(Polynomial::of, Polynomial::of, BigInteger::abs, Polynomial::abs, i);
        }
    }

    private void propertiesSignum() {
        initialize("signum()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            int signum = p.signum();
            assertEquals(p, signum, compare(p, ZERO).toInt());
            assertTrue(p, signum == -1 || signum == 0 || signum == 1);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            homomorphic(Polynomial::of, Function.identity(), BigInteger::signum, Polynomial::signum, i);
        }
    }

    private static @NotNull Polynomial subtract_simplest(@NotNull Polynomial a, @NotNull Polynomial b) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(Polynomial)");
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial difference = p.a.subtract(p.b);
            difference.validate();
            assertTrue(p, difference.degree() <= max(p.a.degree(), p.b.degree()));
            assertEquals(p, difference, subtract_simplest(p.a, p.b));
            antiCommutative(Polynomial::subtract, Polynomial::negate, p);
            inverse(q -> q.subtract(p.b), (Polynomial q) -> q.add(p.b), p.a);
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.subtract(t.b).apply(t.c), t.a.apply(t.c).subtract(t.b.apply(t.c)));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(Polynomial::of, Polynomial::of, Polynomial::of, BigInteger::subtract, Polynomial::subtract, p);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, ZERO.subtract(p), p.negate());
            fixedPoint(q -> q.subtract(ZERO), p);
            assertTrue(p, p.subtract(p) == ZERO);
        }
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        compareImplementations("subtract(Polynomial)", take(LIMIT, P.pairs(P.polynomials())), functions);
    }

    private static @NotNull Polynomial multiply_Polynomial_alt(@NotNull Polynomial a, @NotNull Polynomial b) {
        if (a == ZERO || b == ZERO) return ZERO;
        if (a == ONE) return b;
        if (b == ONE) return a;
        int p = a.degree();
        int q = b.degree();
        if (p < q) {
            return multiply_Polynomial_alt(b, a);
        }
        int r = p + q;
        List<BigInteger> productCoefficients = new ArrayList<>();
        int k = 0;
        for (; k <= q; k++) {
            BigInteger coefficient = BigInteger.ZERO;
            for (int i = 0; i <= k; i++) {
                coefficient = coefficient.add(a.coefficient(k - i).multiply(b.coefficient(i)));
            }
            productCoefficients.add(coefficient);
        }
        for (; k < p; k++) {
            BigInteger coefficient = BigInteger.ZERO;
            for (int i = 0; i <= q; i++) {
                coefficient = coefficient.add(a.coefficient(k - i).multiply(b.coefficient(i)));
            }
            productCoefficients.add(coefficient);
        }
        for (; k <= r; k++) {
            BigInteger coefficient = BigInteger.ZERO;
            for (int i = k - p; i <= q; i++) {
                coefficient = coefficient.add(a.coefficient(k - i).multiply(b.coefficient(i)));
            }
            productCoefficients.add(coefficient);
        }
        return of(productCoefficients);
    }

    private void propertiesMultiply_Polynomial() {
        initialize("multiply(Polynomial)");
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial product = p.a.multiply(p.b);
            product.validate();
            assertEquals(p, product, multiply_Polynomial_alt(p.a, p.b));
            assertTrue(p, p.a == ZERO || p.b == ZERO || product.degree() == p.a.degree() + p.b.degree());
            commutative(Polynomial::multiply, p);
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.add(t.b).apply(t.c), t.a.apply(t.c).add(t.b.apply(t.c)));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(Polynomial::of, Polynomial::of, Polynomial::of, BigInteger::multiply, Polynomial::multiply, p);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            fixedPoint(ONE::multiply, p);
            fixedPoint(q -> q.multiply(ONE), p);
            fixedPoint(q -> q.multiply(p), ZERO);
            fixedPoint(p::multiply, ZERO);
        }

        for (Triple<Polynomial, Polynomial, Polynomial> t : take(LIMIT, P.triples(P.polynomials()))) {
            associative(Polynomial::multiply, t);
            leftDistributive(Polynomial::add, Polynomial::multiply, t);
            rightDistributive(Polynomial::add, Polynomial::multiply, t);
        }
    }

    private void compareImplementationsMultiply_Polynomial() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> multiply_Polynomial_alt(p.a, p.b));
        functions.put("standard", p -> p.a.multiply(p.b));
        compareImplementations("multiply(Polynomial)", take(LIMIT, P.pairs(P.polynomials())), functions);
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            Polynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b.equals(BigInteger.ZERO) || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(of(p.b)));
            assertEquals(p, product, of(p.b).multiply(p.a));
        }

        Iterable<Triple<Polynomial, BigInteger, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.bigIntegers(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, BigInteger, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.multiply(t.b).apply(t.c), t.a.apply(t.c).multiply(t.b));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(
                    Polynomial::of,
                    Function.identity(),
                    Polynomial::of,
                    BigInteger::multiply,
                    Polynomial::multiply,
                    p
            );
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ONE.multiply(i), of(i));
            fixedPoint(j -> j.multiply(i), ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            fixedPoint(q -> q.multiply(BigInteger.ONE), p);
            assertTrue(p, p.multiply(BigInteger.ZERO) == ZERO);
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts2 = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts2)) {
            Polynomial expression1 = t.a.add(t.b).multiply(t.c);
            Polynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.integers()))) {
            Polynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b == 0 || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(of(BigInteger.valueOf(p.b))));
            assertEquals(p, product, of(BigInteger.valueOf(p.b)).multiply(p.a));
        }

        Iterable<Triple<Polynomial, Integer, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.integers(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Integer, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.multiply(t.b).apply(t.c), t.a.apply(t.c).multiply(BigInteger.valueOf(t.b)));
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.integers()))) {
            homomorphic(
                    Polynomial::of,
                    Function.identity(),
                    Polynomial::of,
                    (i, j) -> i.multiply(BigInteger.valueOf(j)),
                    Polynomial::multiply,
                    p
            );
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(i, ONE.multiply(i), of(BigInteger.valueOf(i)));
            fixedPoint(j -> j.multiply(i), ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            fixedPoint(q -> q.multiply(1), p);
            assertTrue(p, p.multiply(0) == ZERO);
        }

        Iterable<Triple<Polynomial, Polynomial, Integer>> ts2 = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.integers()
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts2)) {
            Polynomial expression1 = t.a.add(t.b).multiply(t.c);
            Polynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }
    }

    private static @NotNull Polynomial shiftLeft_simplest(@NotNull Polynomial p, int bits) {
        return p.multiply(BigInteger.ONE.shiftLeft(bits));
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.naturalIntegersGeometric()))) {
            Polynomial shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted.degree(), p.a.degree());
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p.toString(), map(BigInteger::signum, p.a), map(BigInteger::signum, shifted));
            assertEquals(p, p.a.degree(), shifted.degree());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            fixedPoint(q -> q.shiftLeft(0), p);
        }

        Iterable<Triple<Polynomial, Integer, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.naturalIntegersGeometric(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Integer, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.shiftLeft(t.b).apply(t.c), t.a.apply(t.c).shiftLeft(t.b));
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.naturalIntegersGeometric()))) {
            homomorphic(
                    Polynomial::of,
                    Function.identity(),
                    Polynomial::of,
                    BigInteger::shiftLeft,
                    Polynomial::shiftLeft,
                    p
            );
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.shiftLeft(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<Polynomial, Integer>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        compareImplementations(
                "shiftLeft(int)",
                take(LIMIT, P.pairs(P.polynomials(), P.naturalIntegersGeometric())),
                functions
        );
    }

    private static @NotNull Polynomial sum_simplest(@NotNull Iterable<Polynomial> xs) {
        return foldl(Polynomial::add, ZERO, xs);
    }

    private void propertiesSum() {
        initialize("sum(Iterable<Polynomial>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.polynomials(),
                Polynomial::add,
                Polynomial::sum,
                Polynomial::validate,
                true,
                true
        );

        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            Polynomial sum = sum(ps);
            sum.validate();
            assertEquals(ps, sum, sum_simplest(ps));
            assertTrue(ps, ps.isEmpty() || sum.degree() <= maximum(map(Polynomial::degree, ps)));
        }

        for (Pair<List<Polynomial>, BigInteger> p : take(LIMIT, P.pairs(P.lists(P.polynomials()), P.bigIntegers()))) {
            assertEquals(p, sum(p.a).apply(p.b), sumBigInteger(map(q -> q.apply(p.b), p.a)));
        }

        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            homomorphic(
                    js -> toList(map(Polynomial::of, js)),
                    Polynomial::of,
                    IterableUtils::sumBigInteger,
                    Polynomial::sum,
                    is
            );
        }
    }

    private void compareImplementationsSum() {
        Map<String, Function<List<Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", PolynomialProperties::sum_simplest);
        functions.put("standard", Polynomial::sum);
        compareImplementations("sum(Iterable<Polynomial>)", take(LIMIT, P.lists(P.polynomials())), functions);
    }

    private static @NotNull Polynomial product_simplest(@NotNull Iterable<Polynomial> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        if (any(x -> x == ZERO, xs)) {
            return ZERO;
        }
        return foldl(Polynomial::multiply, ONE, xs);
    }

    private static @NotNull Polynomial product_alt(@NotNull List<Polynomial> xs) {
        if (any(x -> x == ZERO, xs)) return ZERO;
        List<BigInteger> productCoefficients =
                toList(replicate(sumInteger(map(Polynomial::degree, xs)) + 1, BigInteger.ZERO));
        List<List<Pair<BigInteger, Integer>>> selections = toList(map(p -> toList(zip(p, rangeUp(0))), xs));
        outer:
        for (List<Pair<BigInteger, Integer>> selection : EP.cartesianProduct(selections)) {
            BigInteger coefficient = BigInteger.ONE;
            int exponent = 0;
            for (Pair<BigInteger, Integer> p : selection) {
                if (p.a.equals(BigInteger.ZERO)) continue outer;
                coefficient = coefficient.multiply(p.a);
                exponent += p.b;
            }
            productCoefficients.set(exponent, productCoefficients.get(exponent).add(coefficient));
        }
        return of(productCoefficients);
    }

    private void propertiesProduct() {
        initialize("product(Iterable<Polynomial>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.polynomials(),
                Polynomial::multiply,
                Polynomial::product,
                Polynomial::validate,
                true,
                true
        );

        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            Polynomial product = product(ps);
            assertTrue(
                    ps,
                    any(p -> p == ZERO, ps) ||
                            product.degree() == IterableUtils.sumInteger(map(Polynomial::degree, ps))
            );
        }

        for (List<Polynomial> ps : take(LIMIT, P.withScale(1).lists(P.withSecondaryScale(1).polynomials()))) {
            Polynomial product = product(ps);
            assertEquals(ps, product, product_simplest(ps));
            assertEquals(ps, product, product_alt(ps));
        }

        for (Pair<List<Polynomial>, BigInteger> p : take(LIMIT, P.pairs(P.lists(P.polynomials()), P.bigIntegers()))) {
            assertEquals(p, product(p.a).apply(p.b), productBigInteger(map(q -> q.apply(p.b), p.a)));
        }

        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            homomorphic(
                    js -> toList(map(Polynomial::of, js)),
                    Polynomial::of,
                    IterableUtils::productBigInteger,
                    Polynomial::product,
                    is
            );
        }
    }

    private void compareImplementationsProduct() {
        Map<String, Function<List<Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", PolynomialProperties::product_simplest);
        functions.put("alt", PolynomialProperties::product_alt);
        functions.put("standard", Polynomial::product);
        compareImplementations(
                "product(Iterable<Polynomial>)",
                take(LIMIT, P.withScale(1).lists(P.withSecondaryScale(1).polynomials())),
                functions
        );
    }

    private void propertiesDelta() {
        initialize("delta(Iterable<Polynomial>)");
        propertiesDeltaHelper(
                LIMIT,
                P.getWheelsProvider(),
                QEP.polynomials(),
                P.polynomials(),
                Polynomial::negate,
                Polynomial::subtract,
                Polynomial::delta,
                Polynomial::validate
        );

        Iterable<Pair<List<Polynomial>, BigInteger>> ps = P.pairs(P.listsAtLeast(1, P.polynomials()), P.bigIntegers());
        for (Pair<List<Polynomial>, BigInteger> p : take(LIMIT, ps)) {
            aeqit(p.toString(), map(q -> q.apply(p.b), delta(p.a)), deltaBigInteger(map(q -> q.apply(p.b), p.a)));
        }

        for (List<BigInteger> is : take(LIMIT, P.listsAtLeast(1, P.bigIntegers()))) {
            homomorphic(
                    js -> toList(map(Polynomial::of, js)),
                    js -> toList(map(Polynomial::of, js)),
                    js -> toList(deltaBigInteger(js)),
                    qs -> toList(delta(qs)),
                    is
            );
        }
    }

    private static @NotNull Polynomial pow_simplest(@NotNull Polynomial a, int p) {
        return product(replicate(p, a));
    }

    private void propertiesPow() {
        initialize("pow(int)");
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            Polynomial q = p.a.pow(p.b);
            q.validate();
            assertTrue(p, p.a == ZERO || q.degree() == p.a.degree() * p.b);
            assertEquals(p, q, pow_simplest(p.a, p.b));
        }

        Iterable<Triple<Polynomial, Integer, BigInteger>> ts = P.triples(
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Integer, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.pow(t.b).apply(t.c), t.a.apply(t.c).pow(t.b));
        }

        Iterable<Pair<BigInteger, Integer>> ps2 = P.pairsLogarithmicOrder(
                P.bigIntegers(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps2)) {
            homomorphic(Polynomial::of, Function.identity(), Polynomial::of, BigInteger::pow, Polynomial::pow, p);
        }

        for (int i : take(LIMIT, P.withScale(4).positiveIntegersGeometric())) {
            fixedPoint(p -> p.pow(i), ZERO);
        }

        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            assertTrue(p, p.pow(0) == ONE);
            fixedPoint(q -> q.pow(1), p);
            assertEquals(p, p.pow(2), p.multiply(p));
        }

        Iterable<Triple<Polynomial, Integer, Integer>> ts2 = P.triples(
                P.withScale(4).withSecondaryScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<Polynomial, Integer, Integer> t : take(LIMIT, ts2)) {
            Polynomial expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            Polynomial expression2 = t.a.pow(t.b + t.c);
            assertEquals(t, expression1, expression2);
            Polynomial expression5 = t.a.pow(t.b).pow(t.c);
            Polynomial expression6 = t.a.pow(t.b * t.c);
            assertEquals(t, expression5, expression6);
        }

        Iterable<Triple<Polynomial, Polynomial, Integer>> ts3 = P.triples(
                P.withScale(4).polynomials(),
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts3)) {
            Polynomial expression1 = t.a.multiply(t.b).pow(t.c);
            Polynomial expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.pow(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsPow() {
        Map<String, Function<Pair<Polynomial, Integer>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> pow_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.pow(p.b));
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        compareImplementations("pow(int)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial substitute_naive(@NotNull Polynomial a, @NotNull Polynomial b) {
        return sum(zipWith((c, i) -> c.equals(BigInteger.ZERO) ? ZERO : b.pow(i).multiply(c), a, rangeUp(0)));
    }

    private void propertiesSubstitute() {
        initialize("substitute(Polynomial)");
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial substituted = p.a.substitute(p.b);
            substituted.validate();
            assertTrue(p, p.b == ZERO || substituted == ZERO || substituted.degree() == p.a.degree() * p.b.degree());
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.substitute(t.b).apply(t.c), t.a.apply(t.b.apply(t.c)));
        }

        for (Pair<BigInteger, Polynomial> p : take(LIMIT, P.pairs(P.bigIntegers(), P.polynomials()))) {
            Polynomial q = of(p.a);
            assertEquals(p, q.substitute(p.b), q);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, p.substitute(ZERO), p == ZERO ? ZERO : of(p.coefficient(0)));
            assertEquals(p, p.substitute(ONE), of(sumBigInteger(p)));
            fixedPoint(q -> q.substitute(X), p);
        }
    }

    private void compareImplementationsSubstitute() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("naïve", p -> substitute_naive(p.a, p.b));
        functions.put("standard", p -> p.a.substitute(p.b));
        compareImplementations(
                "substitute(Polynomial)",
                take(LIMIT, P.pairs(P.withScale(4).polynomials())),
                functions
        );
    }

    private void propertiesDifferentiate() {
        initialize("differentiate()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Polynomial derivative = p.differentiate();
            derivative.validate();
            assertEquals(p, last(take(p.degree() + 2, iterate(Polynomial::differentiate, p))), ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials(0))) {
            assertEquals(p, p.differentiate(), ZERO);
        }
    }

    private void propertiesIsMonic() {
        initialize("isMonic()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            boolean isMonic = p.isMonic();
            Optional<BigInteger> leading = p.leading();
            assertEquals(p, isMonic, leading.isPresent() && leading.get().equals(BigInteger.ONE));
        }
    }

    private void propertiesIsPrimitive() {
        initialize("isPrimitive()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            boolean primitive = p.isPrimitive();
            assertEquals(p, primitive, foldl(BigInteger::gcd, BigInteger.ZERO, p).equals(BigInteger.ONE));
            assertEquals(p, primitive, p.negate().isPrimitive());
        }
    }

    private void propertiesContentAndPrimitive() {
        initialize("contentAndPrimitive()");
        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            Pair<BigInteger, Polynomial> contentAndPrimitive = p.contentAndPrimitive();
            BigInteger content = contentAndPrimitive.a;
            assertNotNull(p, content);
            Polynomial primitive = contentAndPrimitive.b;
            assertNotNull(p, primitive);
            primitive.validate();
            assertEquals(p, primitive.degree(), p.degree());
            assertTrue(p, primitive.isPrimitive());
            assertEquals(p, primitive.multiply(content), p);
            assertEquals(p, content.signum(), 1);
            idempotent(q -> q.contentAndPrimitive().b, p);
            assertEquals(p, content, p.constantFactor().a.abs());
        }
    }

    private void propertiesConstantFactor() {
        initialize("constantFactor()");
        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            Pair<BigInteger, Polynomial> contentAndPrimitive = p.constantFactor();
            BigInteger constantFactor = contentAndPrimitive.a;
            assertNotNull(p, constantFactor);
            Polynomial absolutePrimitive = contentAndPrimitive.b;
            assertNotNull(p, absolutePrimitive);
            absolutePrimitive.validate();
            assertEquals(p, absolutePrimitive.degree(), p.degree());
            assertNotEquals(p, constantFactor, BigInteger.ZERO);
            assertTrue(p, absolutePrimitive.isPrimitive());
            assertEquals(p, absolutePrimitive.multiply(constantFactor), p);
            assertEquals(p, absolutePrimitive.signum(), 1);
            idempotent(q -> q.constantFactor().b, p);
            assertEquals(p, absolutePrimitive, p.contentAndPrimitive().b.abs());
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::polynomials);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::polynomials);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(Polynomial)");
        propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::polynomials);

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            assertEquals(p, p.a.compareTo(p.b), p.a.subtract(p.b).signum());
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(
                    Polynomial::of,
                    Polynomial::of,
                    Function.identity(),
                    BigInteger::compareTo,
                    Polynomial::compareTo,
                    p
            );
        }

        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                r -> r.a.degree() != r.b.degree(),
                P.pairs(filter(q -> q.signum() == 1, P.polynomials()))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            homomorphic(
                    Polynomial::degree,
                    Polynomial::degree,
                    Function.identity(),
                    Polynomial::compareTo,
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
                POLYNOMIAL_CHARS,
                P.polynomials(),
                Polynomial::read,
                Polynomial::validate,
                false
        );
    }

    private void propertiesRead_int_String() {
        initialize("read(int, String)");

        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(P.strings(), P.positiveIntegersGeometric());
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            read(p.b, p.a);
        }

        Iterable<Pair<Polynomial, Integer>> ps2 = filterInfinite(
                p -> p.a.degree() <= p.b,
                P.pairsLogarithmicOrder(P.polynomials(), P.positiveIntegersGeometric())
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps2)) {
            Optional<Polynomial> op = read(p.b, p.a.toString());
            Polynomial q = op.get();
            q.validate();
            assertEquals(p, q, p.a);
        }

        ps2 = filterInfinite(
                p -> p.a.degree() > p.b,
                P.pairsLogarithmicOrder(P.polynomials(), P.positiveIntegersGeometric())
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps2)) {
            Optional<Polynomial> op = read(p.b, p.a.toString());
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
                P.polynomials(),
                s -> badString(s).isPresent() ? Optional.empty() : read(s),
                s -> badString(s).isPresent() ? Optional.empty() : findIn(s),
                Polynomial::validate
        );
    }

    private void propertiesFindIn_int_String() {
        initialize("findIn(int, String)");
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                filterInfinite(
                        s -> !Readers.genericFindIn(PolynomialProperties::badString).apply(s).isPresent(),
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
                        map(Object::toString, filterInfinite(p -> p.degree() <= i, P.polynomials()))
                )
        );
        for (Pair<Integer, String> p : take(LIMIT, ps2)) {
            Optional<Pair<Polynomial, Integer>> op = findIn(p.a, p.b);
            Pair<Polynomial, Integer> q = op.get();
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
        propertiesToStringHelper(LIMIT, POLYNOMIAL_CHARS, P.polynomials(), Polynomial::read);

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            homomorphic(Polynomial::of, Function.identity(), BigInteger::toString, Polynomial::toString, i);
        }
    }
}
