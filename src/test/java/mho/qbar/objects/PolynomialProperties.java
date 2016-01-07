package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
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
        propertiesMultiply_BigInteger();
        propertiesMultiply_int();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesSum();
        compareImplementationsSum();
        propertiesProduct();
        propertiesDelta();
        propertiesPow();
        compareImplementationsPow();
        propertiesSubstitute();
        compareImplementationsSubstitute();
        propertiesIsMonic();
        propertiesIsPrimitive();
        propertiesContentAndPrimitive();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead();
        propertiesFindIn();
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
        initialize("");
        System.out.println("\t\ttesting add(Polynomial) properties...");

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial sum = p.a.add(p.b);
            sum.validate();
            assertTrue(p, sum.degree() <= max(p.a.degree(), p.b.degree()));
            assertEquals(p, sum, p.b.add(p.a));
            assertEquals(p, sum.subtract(p.b), p.a);
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
            assertEquals(p, of(p.a).add(of(p.b)), of(p.a.add(p.b)));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, ZERO.add(p), p);
            assertEquals(p, p.add(ZERO), p);
            assertTrue(p, p.add(p.negate()) == ZERO);
        }

        for (Triple<Polynomial, Polynomial, Polynomial> t : take(LIMIT, P.triples(P.polynomials()))) {
            Polynomial sum1 = t.a.add(t.b).add(t.c);
            Polynomial sum2 = t.a.add(t.b.add(t.c));
            assertEquals(t, sum1, sum2);
        }
    }

    private void propertiesNegate() {
        initialize("");
        System.out.println("\t\ttesting negate() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Polynomial negative = p.negate();
            negative.validate();
            assertEquals(p, negative.degree(), p.degree());
            assertEquals(p, p, negative.negate());
            assertTrue(p, p.add(negative) == ZERO);
        }

        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            assertEquals(p, p.a.negate().apply(p.b), p.a.apply(p.b).negate());
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).negate(), of(i.negate()));
        }

        for (Polynomial p : take(LIMIT, filter(q -> q != ZERO, P.polynomials()))) {
            Polynomial negative = p.negate();
            assertNotEquals(p, p, negative);
        }
    }

    private void propertiesAbs() {
        initialize("");
        System.out.println("\t\ttesting abs() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Polynomial abs = p.abs();
            abs.validate();
            assertEquals(p, abs.degree(), p.degree());
            assertEquals(p, abs, abs.abs());
            assertNotEquals(p, abs.signum(), -1);
            assertTrue(p, ge(abs, ZERO));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).abs(), of(i.abs()));
        }
    }

    private void propertiesSignum() {
        initialize("");
        System.out.println("\t\ttesting signum() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            int signum = p.signum();
            assertEquals(p, signum, compare(p, ZERO).toInt());
            assertTrue(p, signum == -1 || signum == 0 || signum == 1);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).signum(), i.signum());
        }
    }

    private static @NotNull Polynomial subtract_simplest(@NotNull Polynomial a, @NotNull Polynomial b) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("");
        System.out.println("\t\ttesting subtract(Polynomial) properties...");

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial difference = p.a.subtract(p.b);
            difference.validate();
            assertTrue(p, difference.degree() <= max(p.a.degree(), p.b.degree()));
            assertEquals(p, difference, subtract_simplest(p.a, p.b));
            assertEquals(p, difference, p.b.subtract(p.a).negate());
            assertEquals(p, p.a, difference.add(p.b));
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
            assertEquals(p, of(p.a).subtract(of(p.b)), of(p.a.subtract(p.b)));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, ZERO.subtract(p), p.negate());
            assertEquals(p, p.subtract(ZERO), p);
            assertTrue(p, p.subtract(p) == ZERO);
        }
    }

    private void compareImplementationsSubtract() {
        initialize("");
        System.out.println("\t\tcomparing subtract(Polynomial) implementations...");

        long totalTime = 0;
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            long time = System.nanoTime();
            subtract_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            long time = System.nanoTime();
            p.a.subtract(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private void propertiesMultiply_Polynomial() {
        initialize("");
        System.out.println("\t\ttesting multiply(Polynomial) properties...");

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.a == ZERO || p.b == ZERO || product.degree() == p.a.degree() + p.b.degree());
            assertEquals(p, product, p.b.multiply(p.a));
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
            assertEquals(p, of(p.a).multiply(of(p.b)), of(p.a.multiply(p.b)));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, ONE.multiply(p), p);
            assertEquals(p, p.multiply(ONE), p);
            assertTrue(p, ZERO.multiply(p) == ZERO);
            assertTrue(p, p.multiply(ZERO) == ZERO);
        }

        for (Triple<Polynomial, Polynomial, Polynomial> t : take(LIMIT, P.triples(P.polynomials()))) {
            Polynomial product1 = t.a.multiply(t.b).multiply(t.c);
            Polynomial product2 = t.a.multiply(t.b.multiply(t.c));
            assertEquals(t, product1, product2);
            Polynomial expression1 = t.a.add(t.b).multiply(t.c);
            Polynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("");
        System.out.println("\t\ttesting multiply(BigInteger) properties...");

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
            assertEquals(p, of(p.a).multiply(p.b), of(p.a.multiply(p.b)));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ONE.multiply(i), of(i));
            assertTrue(i, ZERO.multiply(i) == ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, p.multiply(BigInteger.ONE), p);
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
        initialize("");
        System.out.println("\t\ttesting multiply(int) properties...");

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
            assertEquals(p, of(p.a).multiply(p.b), of(p.a.multiply(BigInteger.valueOf(p.b))));
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(i, ONE.multiply(i), of(BigInteger.valueOf(i)));
            assertTrue(i, ZERO.multiply(i) == ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, p.multiply(1), p);
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
        initialize("");
        System.out.println("\t\ttesting shiftLeft(int) properties...");

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = P.withScale(10).naturalIntegersGeometric();
        }
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), is))) {
            Polynomial shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted.degree(), p.a.degree());
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p.toString(), map(BigInteger::signum, p.a), map(BigInteger::signum, shifted));
            assertEquals(p, p.a.degree(), shifted.degree());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, p.shiftLeft(0), p);
        }

        Iterable<Triple<Polynomial, Integer, BigInteger>> ts = P.triples(P.polynomials(), is, P.bigIntegers());
        for (Triple<Polynomial, Integer, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.shiftLeft(t.b).apply(t.c), t.a.apply(t.c).shiftLeft(t.b));
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), is))) {
            assertEquals(p, of(p.a).shiftLeft(p.b), of(p.a.shiftLeft(p.b)));
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), is))) {
            Polynomial shifted = p.a.shiftLeft(p.b);
            assertEquals(p, shifted, p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.shiftLeft(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsShiftLeft() {
        initialize("");
        System.out.println("\t\tcomparing shiftLeft(int) implementations...");

        long totalTime = 0;
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = P.withScale(10).naturalIntegersGeometric();
        }
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), is))) {
            long time = System.nanoTime();
            shiftLeft_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), is))) {
            long time = System.nanoTime();
            p.a.shiftLeft(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Polynomial sum_simplest(@NotNull Iterable<Polynomial> xs) {
        return foldl(Polynomial::add, ZERO, xs);
    }

    private void propertiesSum() {
        initialize("");
        System.out.println("\t\ttesting sum(Iterable<Polynomial>) properties...");

        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.polynomials(),
                Polynomial::add,
                Polynomial::sum,
                p -> {},
                true,
                true
        );

        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            Polynomial sum = sum(ps);
            assertTrue(ps, ps.isEmpty() || sum.degree() <= maximum(map(Polynomial::degree, ps)));
            assertEquals(ps, sum, sum_simplest(ps));
        }

        Iterable<Pair<List<Polynomial>, BigInteger>> ps = P.pairs(P.lists(P.polynomials()), P.bigIntegers());
        for (Pair<List<Polynomial>, BigInteger> p : take(LIMIT, ps)) {
            assertEquals(p, sum(p.a).apply(p.b), sumBigInteger(map(q -> q.apply(p.b), p.a)));
        }

        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            assertEquals(is, sum(map(Polynomial::of, is)), of(sumBigInteger(is)));
        }
    }

    private void compareImplementationsSum() {
        initialize("");
        System.out.println("\t\tcomparing sum(Iterable<Polynomial) implementations...");

        long totalTime = 0;
        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            long time = System.nanoTime();
            sum_simplest(ps);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            long time = System.nanoTime();
            sum(ps);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private void propertiesProduct() {
        initialize("");
        System.out.println("\t\ttesting product(Iterable<Polynomial>) properties...");

        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.polynomials(),
                Polynomial::multiply,
                Polynomial::product,
                p -> {},
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

        for (Pair<List<Polynomial>, BigInteger> p : take(LIMIT, P.pairs(P.lists(P.polynomials()), P.bigIntegers()))) {
            assertEquals(p, product(p.a).apply(p.b), productBigInteger(map(q -> q.apply(p.b), p.a)));
        }

        for (List<BigInteger> rs : take(LIMIT, P.lists(P.bigIntegers()))) {
            assertEquals(rs, product(map(Polynomial::of, rs)), of(productBigInteger(rs)));
        }
    }

    private void propertiesDelta() {
        initialize("");
        System.out.println("\t\ttesting delta(Iterable<Polynomial>) properties...");

        propertiesDeltaHelper(
                LIMIT,
                P.getWheelsProvider(),
                QEP.polynomials(),
                P.polynomials(),
                Polynomial::negate,
                Polynomial::subtract,
                Polynomial::delta,
                p -> {}
        );

        Iterable<Pair<List<Polynomial>, BigInteger>> ps = P.pairs(P.listsAtLeast(1, P.polynomials()), P.bigIntegers());
        for (Pair<List<Polynomial>, BigInteger> p : take(LIMIT, ps)) {
            aeqit(p.toString(), map(q -> q.apply(p.b), delta(p.a)), deltaBigInteger(map(q -> q.apply(p.b), p.a)));
        }

        for (List<BigInteger> is : take(LIMIT, P.listsAtLeast(1, P.bigIntegers()))) {
            aeqit(is.toString(), delta(map(Polynomial::of, is)), map(Polynomial::of, deltaBigInteger(is)));
        }
    }

    private static @NotNull Polynomial pow_simplest(@NotNull Polynomial a, int p) {
        return product(replicate(p, a));
    }

    //todo clean
    private void propertiesPow() {
        initialize("");
        System.out.println("\t\ttesting pow(int) properties...");

        Iterable<Pair<Polynomial, Integer>> ps1 = P.pairsLogarithmicOrder(
                P.polynomials(),
                P.withScale(5).naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps1)) {
            Polynomial q = p.a.pow(p.b);
            q.validate();
            assertTrue(p, p.a == ZERO || q.degree() == p.a.degree() * p.b);
            assertEquals(p, q, pow_simplest(p.a, p.b));
        }

        Iterable<Triple<Polynomial, Integer, BigInteger>> ts1 = P.triples(
                P.polynomials(),
                P.withScale(5).naturalIntegersGeometric(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Integer, BigInteger> t : take(LIMIT, ts1)) {
            assertEquals(t, t.a.pow(t.b).apply(t.c), t.a.apply(t.c).pow(t.b));
        }

        Iterable<Pair<BigInteger, Integer>> ps2 = P.pairs(P.bigIntegers(), P.withScale(5).naturalIntegersGeometric());
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps2)) {
            assertEquals(p, of(p.a).pow(p.b), of(p.a.pow(p.b)));
        }

        for (int i : take(LIMIT, P.withScale(20).positiveIntegersGeometric())) {
            assertTrue(i, ZERO.pow(i) == ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertTrue(p, p.pow(0) == ONE);
            assertEquals(p, p.pow(1), p);
            assertEquals(p, p.pow(2), p.multiply(p));
        }

        Iterable<Triple<Polynomial, Integer, Integer>> ts2 = P.triples(
                P.withScale(5).polynomials(),
                P.withScale(2).naturalIntegersGeometric(),
                P.withScale(2).naturalIntegersGeometric()
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
                P.polynomials(),
                P.polynomials(),
                P.withScale(2).naturalIntegersGeometric()
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts3)) {
            Polynomial expression1 = t.a.multiply(t.b).pow(t.c);
            Polynomial expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }
    }

    private void compareImplementationsPow() {
        initialize("");
        System.out.println("\t\tcomparing pow(int) implementations...");

        long totalTime = 0;
        Iterable<Integer> exps;
        Iterable<Pair<Polynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            exps = P.naturalIntegers();
            ps = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.polynomials(), exps);
        } else {
            exps = P.withScale(5).naturalIntegersGeometric();
            ps = P.pairs(P.polynomials(), exps);
        }
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            pow_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            p.a.pow(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Polynomial substitute_naive(@NotNull Polynomial a, @NotNull Polynomial b) {
        return sum(zipWith((c, i) -> c.equals(BigInteger.ZERO) ? ZERO : b.pow(i).multiply(c), a, rangeUp(0)));
    }

    private void propertiesSubstitute() {
        initialize("");
        System.out.println("\t\ttesting substitute(Polynomial) properties...");

        Iterable<Polynomial> ps = P.withScale(10).polynomials();
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(ps))) {
            Polynomial substituted = p.a.substitute(p.b);
            substituted.validate();
            assertTrue(p, p.b == ZERO || substituted == ZERO || substituted.degree() == p.a.degree() * p.b.degree());
        }

        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, P.triples(ps, ps, P.bigIntegers()))) {
            assertEquals(t, t.a.substitute(t.b).apply(t.c), t.a.apply(t.b.apply(t.c)));
        }

        for (Pair<BigInteger, Polynomial> p : take(LIMIT, P.pairs(P.bigIntegers(), P.polynomials()))) {
            assertEquals(p, of(p.a).substitute(p.b), of(p.a));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, p.substitute(ZERO), p == ZERO ? ZERO : of(p.coefficient(0)));
            assertEquals(p, p.substitute(ONE), of(IterableUtils.sumBigInteger(p)));
            assertEquals(p, p.substitute(X), p);
        }
    }

    private void compareImplementationsSubstitute() {
        initialize("");
        System.out.println("\t\tcomparing substitute(Polynomial) implementations...");

        long totalTime = 0;
        Iterable<Polynomial> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.polynomials();
        } else {
            ps = P.withScale(16).polynomials();
        }
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(ps))) {
            long time = System.nanoTime();
            substitute_naive(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tnaïve: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(ps))) {
            long time = System.nanoTime();
            p.a.substitute(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private void propertiesIsMonic() {
        initialize("");
        System.out.println("\t\ttesting isMonic() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            p.isMonic();
        }

        for (Polynomial p : take(LIMIT, filter(Polynomial::isMonic, P.polynomials()))) {
            assertEquals(p, p.leading().get(), BigInteger.ONE);
        }
    }

    private void propertiesIsPrimitive() {
        initialize("");
        System.out.println("\t\ttesting isPrimitive() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            p.isPrimitive();
        }

        for (Polynomial p : take(LIMIT, filter(Polynomial::isPrimitive, P.polynomials()))) {
            assertEquals(p, p.signum(), 1);
            assertEquals(p, foldl(BigInteger::gcd, BigInteger.ZERO, p), BigInteger.ONE);
        }
    }

    private void propertiesContentAndPrimitive() {
        initialize("");
        System.out.println("\t\ttesting contentAndPrimitive() properties...");

        for (Polynomial p : take(LIMIT, filter(q -> q != ZERO, P.polynomials()))) {
            Pair<BigInteger, Polynomial> contentAndPrimitive = p.contentAndPrimitive();
            BigInteger content = contentAndPrimitive.a;
            assertNotNull(p.toString(), content);
            Polynomial primitive = contentAndPrimitive.b;
            assertNotNull(p.toString(), primitive);
            primitive.validate();
            assertEquals(p, primitive.degree(), p.degree());
            assertNotEquals(p, content, BigInteger.ZERO);
            assertTrue(p, primitive.isPrimitive());
            assertEquals(p, primitive.multiply(content), p);

            Pair<BigInteger, Polynomial> primitiveCP = primitive.contentAndPrimitive();
            assertEquals(p, primitiveCP.a, BigInteger.ONE);
            assertEquals(p, primitive, primitiveCP.b);
        }
    }

    private void propertiesEquals() {
        initialize("");
        System.out.println("\t\ttesting equals(Object) properties...");

        propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::polynomials);

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            assertEquals(p, of(p.a).equals(of(p.b)), p.a.equals(p.b));
        }
    }

    private void propertiesHashCode() {
        initialize("");
        System.out.println("\t\ttesting hashCode() properties...");

        propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::polynomials);
    }

    private void propertiesCompareTo() {
        initialize("");
        System.out.println("\t\ttesting compareTo(Polynomial) properties...");

        propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::polynomials);

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            assertEquals(p, p.a.compareTo(p.b), p.a.subtract(p.b).signum());
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            assertEquals(p, of(p.a).compareTo(of(p.b)), p.a.compareTo(p.b));
        }

        Iterable<Pair<Polynomial, Polynomial>> ps = filter(
                r -> r.a.degree() != r.b.degree(),
                P.pairs(filter(q -> q.signum() == 1, P.polynomials()))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.degree(), p.b.degree()));
        }
    }

    private void propertiesRead() {
        initialize("");
        System.out.println("\t\ttesting read(String) properties...");

        for (String s : take(LIMIT, P.strings())) {
            read(s);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Optional<Polynomial> op = read(p.toString());
            assertEquals(p, op.get(), p);
        }

        for (String s : take(LIMIT, filter(t -> read(MEDIUM_LIMIT, t).isPresent(), P.strings(POLYNOMIAL_CHARS)))) {
            Optional<Polynomial> op = read(s);
            op.get().validate();
        }
    }

    private void propertiesFindIn() {
        initialize("");
        System.out.println("\t\ttesting findIn(String) properties...");

        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.polynomials(),
                s -> read(MEDIUM_LIMIT, s),
                s -> findIn(MEDIUM_LIMIT, s),
                p -> {}
        );
    }

    private void propertiesToString() {
        initialize("");
        System.out.println("\t\ttesting toString() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            String s = p.toString();
            assertTrue(p, isSubsetOf(s, POLYNOMIAL_CHARS));
            Optional<Polynomial> readP = read(s);
            assertTrue(p, readP.isPresent());
            assertEquals(p, readP.get(), p);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).toString(), i.toString());
        }
    }
}
