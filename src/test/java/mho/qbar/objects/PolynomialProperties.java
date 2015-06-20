package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.Polynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PolynomialProperties {
    private static boolean USE_RANDOM;
    private static final @NotNull String POLYNOMIAL_CHARS = "*+-0123456789^x";
    private static int LIMIT;

    private static QBarIterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = QBarRandomProvider.example();
            LIMIT = 1000;
        } else {
            P = QBarExhaustiveProvider.INSTANCE;
            LIMIT = 10000;
        }
    }

    @Test
    public void testAllProperties() {
        System.out.println("Polynomial properties");
        for (boolean useRandom : Arrays.asList(false, true)) {
            System.out.println("\ttesting " + (useRandom ? "randomly" : "exhaustively"));
            USE_RANDOM = useRandom;
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
        System.out.println("Done");
    }

    private static void propertiesIterator() {
        initialize();
        System.out.println("\t\ttesting iterator() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            List<BigInteger> is = toList(p);
            assertTrue(p.toString(), all(r -> r != null, is));
            assertEquals(p.toString(), of(toList(p)), p);
            try {
                p.iterator().remove();
            } catch (UnsupportedOperationException ignored) {}
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            List<BigInteger> is = toList(p);
            assertTrue(p.toString(), !last(is).equals(BigInteger.ZERO));
        }
    }

    private static @NotNull BigInteger apply_BigInteger_naive(@NotNull Polynomial p, @NotNull BigInteger x) {
        return sumBigInteger(
                zipWith((c, i) -> c.equals(BigInteger.ZERO) ? BigInteger.ZERO : x.pow(i).multiply(c), p, rangeUp(0))
        );
    }

    private static void propertiesApply_BigInteger() {
        initialize();
        System.out.println("\t\ttesting apply(BigInteger) properties...");

        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            BigInteger y = p.a.apply(p.b);
            assertEquals(p.toString(), y, apply_BigInteger_naive(p.a, p.b));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), ZERO.apply(i), BigInteger.ZERO);
            assertEquals(i.toString(), X.apply(i), i);
            assertEquals(i.toString(), of(BigInteger.valueOf(-1), 1).apply(i), i.negate());
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p.toString(), p.apply(BigInteger.ZERO), p.coefficient(0));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p.toString(), p.apply(BigInteger.ONE), sumBigInteger(p));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            assertEquals(p.toString(), of(p.a).apply(p.b), p.a);
            assertEquals(p.toString(), of(Arrays.asList(p.a, BigInteger.ONE)).apply(p.b), p.b.add(p.a));
            assertEquals(p.toString(), of(Arrays.asList(p.a.negate(), BigInteger.ONE)).apply(p.b), p.b.subtract(p.a));
            assertEquals(p.toString(), of(p.a, 1).apply(p.b), p.b.multiply(p.a));
        }

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = P.withScale(20).naturalIntegersGeometric();
        }
        for (Pair<Integer, BigInteger> p : take(LIMIT, P.pairs(is, P.bigIntegers()))) {
            assertEquals(p.toString(), of(BigInteger.ONE, p.a).apply(p.b), p.b.pow(p.a));
        }
    }

    private static void compareImplementationsApply_BigInteger() {
        initialize();
        System.out.println("\t\tcomparing apply(BigInteger) implementations...");

        long totalTime = 0;
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            long time = System.nanoTime();
            apply_BigInteger_naive(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tnaïve: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            long time = System.nanoTime();
            p.a.apply(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Rational apply_Rational_naive(@NotNull Polynomial p, @NotNull Rational x) {
        return Rational.sum(
                zipWith((c, i) -> c.equals(BigInteger.ZERO) ? Rational.ZERO : x.pow(i).multiply(c), p, rangeUp(0))
        );
    }

    private static void propertiesApply_Rational() {
        initialize();
        System.out.println("\t\ttesting apply(Rational) properties...");

        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            Rational y = p.a.apply(p.b);
            assertEquals(p.toString(), y, apply_Rational_naive(p.a, p.b));
            assertEquals(p.toString(), y, p.a.toRationalPolynomial().apply(p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), ZERO.apply(r), Rational.ZERO);
            assertEquals(r.toString(), X.apply(r), r);
            assertEquals(r.toString(), of(BigInteger.valueOf(-1), 1).apply(r), r.negate());
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p.toString(), p.apply(Rational.ZERO), Rational.of(p.coefficient(0)));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p.toString(), p.apply(Rational.ONE), Rational.of(sumBigInteger(p)));
        }

        for (Pair<BigInteger, Rational> p : take(LIMIT, P.pairs(P.bigIntegers(), P.rationals()))) {
            assertEquals(p.toString(), of(p.a).apply(p.b), Rational.of(p.a));
            assertEquals(p.toString(), of(Arrays.asList(p.a, BigInteger.ONE)).apply(p.b), p.b.add(Rational.of(p.a)));
            assertEquals(
                    p.toString(),
                    of(Arrays.asList(p.a.negate(), BigInteger.ONE)).apply(p.b),
                    p.b.subtract(Rational.of(p.a))
            );
            assertEquals(p.toString(), of(p.a, 1).apply(p.b), p.b.multiply(p.a));
        }

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = P.withScale(20).naturalIntegersGeometric();
        }
        for (Pair<Integer, Rational> p : take(LIMIT, P.pairs(is, P.rationals()))) {
            assertEquals(p.toString(), of(BigInteger.ONE, p.a).apply(p.b), p.b.pow(p.a));
        }
    }

    private static void compareImplementationsApply_Rational() {
        initialize();
        System.out.println("\t\tcomparing apply(Rational) implementations...");

        long totalTime = 0;
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            RationalPolynomial rp = p.a.toRationalPolynomial();
            long time = System.nanoTime();
            rp.apply(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            long time = System.nanoTime();
            apply_Rational_naive(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tnaïve: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            long time = System.nanoTime();
            p.a.apply(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static void propertiesToRationalPolynomial() {
        initialize();
        System.out.println("\t\ttesting toRationalPolynomial() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            RationalPolynomial rp = p.toRationalPolynomial();
            assertEquals(p.toString(), p.toString(), rp.toString());
            assertEquals(p.toString(), p.degree(), rp.degree());
        }

        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            assertEquals(
                    p.toString(),
                    p.a.apply(p.b),
                    p.a.toRationalPolynomial().apply(Rational.of(p.b)).bigIntegerValueExact()
            );
        }

        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            assertEquals(p.toString(), p.a.apply(p.b), p.a.toRationalPolynomial().apply(p.b));
        }
    }

    private static void propertiesCoefficient() {
        initialize();
        System.out.println("\t\ttesting coefficient(int) properties...");

        Iterable<Pair<Polynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.polynomials(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.polynomials(), P.withScale(10).naturalIntegersGeometric());
        }
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            p.a.coefficient(p.b);
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, filter(q -> q.b <= q.a.degree(), ps))) {
            assertEquals(p.toString(), p.a.coefficient(p.b), toList(p.a).get(p.b));
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, filter(q -> q.b > q.a.degree(), ps))) {
            assertEquals(p.toString(), p.a.coefficient(p.b), BigInteger.ZERO);
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.coefficient(p.b);
                fail(p.toString());
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private static void propertiesOf_List_BigInteger() {
        initialize();
        System.out.println("\t\ttesting of(List<BigInteger>) properties");

        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            Polynomial p = of(is);
            p.validate();
            assertTrue(is.toString(), p.degree() < is.size());
        }

        Iterable<List<BigInteger>> iss = filter(
                is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                P.lists(P.bigIntegers())
        );
        for (List<BigInteger> is : take(LIMIT, iss)) {
            assertEquals(is.toString(), toList(of(is)), is);
        }

        for (List<BigInteger> is : take(LIMIT, P.listsWithElement(null, P.bigIntegers()))) {
            try {
                of(is);
                fail(is.toString());
            } catch (NullPointerException ignored) {}
        }
    }

    private static void propertiesOf_BigInteger() {
        initialize();
        System.out.println("\t\ttesting of(BigInteger) properties");

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Polynomial p = of(i);
            p.validate();
            assertTrue(i.toString(), p.degree() == 0 || p.degree() == -1);
        }

        for (BigInteger i : take(LIMIT, filter(j -> !j.equals(BigInteger.ZERO), P.bigIntegers()))) {
            assertEquals(i.toString(), of(i).coefficient(0), i);
        }
    }

    private static void propertiesOf_BigInteger_int() {
        initialize();
        System.out.println("\t\ttesting of(BigInteger, int) properties");

        Iterable<Pair<BigInteger, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.bigIntegers(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.bigIntegers(), P.withScale(20).naturalIntegersGeometric());
        }
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            Polynomial q = of(p.a, p.b);
            q.validate();
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, filter(q -> !q.a.equals(BigInteger.ZERO), ps))) {
            Polynomial q = of(p.a, p.b);
            List<BigInteger> coefficients = toList(q);
            assertEquals(p.toString(), length(filter(i -> !i.equals(BigInteger.ZERO), coefficients)), 1);
            assertEquals(p.toString(), q.degree(), p.b.intValue());
        }

        for (int i : take(LIMIT, P.naturalIntegers())) {
            assertTrue(of(BigInteger.ZERO, i) == ZERO);
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.negativeIntegers()))) {
            try {
                of(p.a, p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static void propertiesDegree() {
        initialize();
        System.out.println("\t\ttesting degree() properties");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            int degree = p.degree();
            assertTrue(p.toString(), degree >= -1);
        }
    }

    private static void propertiesLeading() {
        initialize();
        System.out.println("\t\ttesting leading() properties");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            p.leading();
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            BigInteger leading = p.leading().get();
            assertNotEquals(p.toString(), leading, BigInteger.ZERO);
        }

        for (BigInteger i : take(LIMIT, filter(j -> !j.equals(BigInteger.ZERO), P.bigIntegers()))) {
            Polynomial p = of(i);
            assertEquals(i.toString(), p.leading().get(), p.coefficient(0));
        }
    }

    private static void propertiesAdd() {
        initialize();
        System.out.println("\t\ttesting add(Polynomial) properties...");

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial sum = p.a.add(p.b);
            sum.validate();
            assertTrue(p.toString(), sum.degree() <= max(p.a.degree(), p.b.degree()));
            assertEquals(p.toString(), sum, p.b.add(p.a));
            assertEquals(p.toString(), sum.subtract(p.b), p.a);
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t.toString(), t.a.add(t.b).apply(t.c), t.a.apply(t.c).add(t.b.apply(t.c)));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            assertEquals(p.toString(), of(p.a).add(of(p.b)), of(p.a.add(p.b)));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p.toString(), ZERO.add(p), p);
            assertEquals(p.toString(), p.add(ZERO), p);
            assertTrue(p.toString(), p.add(p.negate()) == ZERO);
        }

        for (Triple<Polynomial, Polynomial, Polynomial> t : take(LIMIT, P.triples(P.polynomials()))) {
            Polynomial sum1 = t.a.add(t.b).add(t.c);
            Polynomial sum2 = t.a.add(t.b.add(t.c));
            assertEquals(t.toString(), sum1, sum2);
        }
    }

    private static void propertiesNegate() {
        initialize();
        System.out.println("\t\ttesting negate() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Polynomial negative = p.negate();
            negative.validate();
            assertEquals(p.toString(), negative.degree(), p.degree());
            assertEquals(p.toString(), p, negative.negate());
            assertTrue(p.toString(), p.add(negative) == ZERO);
        }

        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            assertEquals(p.toString(), p.a.negate().apply(p.b), p.a.apply(p.b).negate());
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).negate(), of(i.negate()));
        }

        for (Polynomial p : take(LIMIT, filter(q -> q != ZERO, P.polynomials()))) {
            Polynomial negative = p.negate();
            assertNotEquals(p.toString(), p, negative);
        }
    }

    private static void propertiesAbs() {
        initialize();
        System.out.println("\t\ttesting abs() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Polynomial abs = p.abs();
            abs.validate();
            assertEquals(p.toString(), abs.degree(), p.degree());
            assertEquals(p.toString(), abs, abs.abs());
            assertNotEquals(p.toString(), abs.signum(), -1);
            assertTrue(p.toString(), ge(abs, ZERO));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).abs(), of(i.abs()));
        }
    }

    private static void propertiesSignum() {
        initialize();
        System.out.println("\t\ttesting signum() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            int signum = p.signum();
            assertEquals(p.toString(), signum, compare(p, ZERO).toInt());
            assertTrue(p.toString(), signum == -1 || signum == 0 || signum == 1);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).signum(), i.signum());
        }
    }

    private static @NotNull Polynomial subtract_simplest(@NotNull Polynomial a, @NotNull Polynomial b) {
        return a.add(b.negate());
    }

    private static void propertiesSubtract() {
        initialize();
        System.out.println("\t\ttesting subtract(Polynomial) properties...");

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial difference = p.a.subtract(p.b);
            difference.validate();
            assertTrue(p.toString(), difference.degree() <= max(p.a.degree(), p.b.degree()));
            assertEquals(p.toString(), difference, subtract_simplest(p.a, p.b));
            assertEquals(p.toString(), difference, p.b.subtract(p.a).negate());
            assertEquals(p.toString(), p.a, difference.add(p.b));
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t.toString(), t.a.subtract(t.b).apply(t.c), t.a.apply(t.c).subtract(t.b.apply(t.c)));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            assertEquals(p.toString(), of(p.a).subtract(of(p.b)), of(p.a.subtract(p.b)));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p.toString(), ZERO.subtract(p), p.negate());
            assertEquals(p.toString(), p.subtract(ZERO), p);
            assertTrue(p.toString(), p.subtract(p) == ZERO);
        }
    }

    private static void compareImplementationsSubtract() {
        initialize();
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

    private static void propertiesMultiply_Polynomial() {
        initialize();
        System.out.println("\t\ttesting multiply(Polynomial) properties...");

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p.toString(), p.a == ZERO || p.b == ZERO || product.degree() == p.a.degree() + p.b.degree());
            assertEquals(p.toString(), product, p.b.multiply(p.a));
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t.toString(), t.a.add(t.b).apply(t.c), t.a.apply(t.c).add(t.b.apply(t.c)));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            assertEquals(p.toString(), of(p.a).multiply(of(p.b)), of(p.a.multiply(p.b)));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p.toString(), ONE.multiply(p), p);
            assertEquals(p.toString(), p.multiply(ONE), p);
            assertTrue(p.toString(), ZERO.multiply(p) == ZERO);
            assertTrue(p.toString(), p.multiply(ZERO) == ZERO);
        }

        for (Triple<Polynomial, Polynomial, Polynomial> t : take(LIMIT, P.triples(P.polynomials()))) {
            Polynomial product1 = t.a.multiply(t.b).multiply(t.c);
            Polynomial product2 = t.a.multiply(t.b.multiply(t.c));
            assertEquals(t.toString(), product1, product2);
            Polynomial expression1 = t.a.add(t.b).multiply(t.c);
            Polynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    private static void propertiesMultiply_BigInteger() {
        initialize();
        System.out.println("\t\ttesting multiply(BigInteger) properties...");

        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            Polynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p.toString(), p.b.equals(BigInteger.ZERO) || product.degree() == p.a.degree());
            assertEquals(p.toString(), product, p.a.multiply(of(p.b)));
            assertEquals(p.toString(), product, of(p.b).multiply(p.a));
        }

        Iterable<Triple<Polynomial, BigInteger, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.bigIntegers(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, BigInteger, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t.toString(), t.a.multiply(t.b).apply(t.c), t.a.apply(t.c).multiply(t.b));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            assertEquals(p.toString(), of(p.a).multiply(p.b), of(p.a.multiply(p.b)));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), ONE.multiply(i), of(i));
            assertTrue(i.toString(), ZERO.multiply(i) == ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p.toString(), p.multiply(BigInteger.ONE), p);
            assertTrue(p.toString(), p.multiply(BigInteger.ZERO) == ZERO);
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts2 = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts2)) {
            Polynomial expression1 = t.a.add(t.b).multiply(t.c);
            Polynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    private static void propertiesMultiply_int() {
        initialize();
        System.out.println("\t\ttesting multiply(int) properties...");

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.integers()))) {
            Polynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p.toString(), p.b == 0 || product.degree() == p.a.degree());
            assertEquals(p.toString(), product, p.a.multiply(of(BigInteger.valueOf(p.b))));
            assertEquals(p.toString(), product, of(BigInteger.valueOf(p.b)).multiply(p.a));
        }

        Iterable<Triple<Polynomial, Integer, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.integers(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Integer, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t.toString(), t.a.multiply(t.b).apply(t.c), t.a.apply(t.c).multiply(BigInteger.valueOf(t.b)));
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.integers()))) {
            assertEquals(p.toString(), of(p.a).multiply(p.b), of(p.a.multiply(BigInteger.valueOf(p.b))));
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(Integer.toString(i), ONE.multiply(i), of(BigInteger.valueOf(i)));
            assertTrue(Integer.toString(i), ZERO.multiply(i) == ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p.toString(), p.multiply(1), p);
            assertTrue(p.toString(), p.multiply(0) == ZERO);
        }

        Iterable<Triple<Polynomial, Polynomial, Integer>> ts2 = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.integers()
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts2)) {
            Polynomial expression1 = t.a.add(t.b).multiply(t.c);
            Polynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    private static @NotNull Polynomial shiftLeft_simplest(@NotNull Polynomial p, int bits) {
        return p.multiply(BigInteger.ONE.shiftLeft(bits));
    }

    private static void propertiesShiftLeft() {
        initialize();
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
            assertEquals(p.toString(), shifted.degree(), p.a.degree());
            assertEquals(p.toString(), shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p.toString(), map(BigInteger::signum, p.a), map(BigInteger::signum, shifted));
            assertEquals(p.toString(), p.a.degree(), shifted.degree());
            assertEquals(p.toString(), p.a.negate().shiftLeft(p.b), shifted.negate());
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p.toString(), p.shiftLeft(0), p);
        }

        Iterable<Triple<Polynomial, Integer, BigInteger>> ts = P.triples(P.polynomials(), is, P.bigIntegers());
        for (Triple<Polynomial, Integer, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t.toString(), t.a.shiftLeft(t.b).apply(t.c), t.a.apply(t.c).shiftLeft(t.b));
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), is))) {
            assertEquals(p.toString(), of(p.a).shiftLeft(p.b), of(p.a.shiftLeft(p.b)));
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), is))) {
            Polynomial shifted = p.a.shiftLeft(p.b);
            assertEquals(p.toString(), shifted, p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.shiftLeft(p.b);
                fail(p.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void compareImplementationsShiftLeft() {
        initialize();
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

    private static void propertiesSum() {
        initialize();
        System.out.println("\t\ttesting sum(Iterable<Polynomial>) properties...");

        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.polynomials(),
                Polynomial::add,
                Polynomial::sum,
                p -> {},
                true
        );

        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            Polynomial sum = sum(ps);
            assertTrue(ps.toString(), ps.isEmpty() || sum.degree() <= maximum(map(Polynomial::degree, ps)));
            assertEquals(ps.toString(), sum, sum_simplest(ps));
        }

        Iterable<Pair<List<Polynomial>, BigInteger>> ps = P.pairs(P.lists(P.polynomials()), P.bigIntegers());
        for (Pair<List<Polynomial>, BigInteger> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), sum(p.a).apply(p.b), sumBigInteger(map(q -> q.apply(p.b), p.a)));
        }

        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            assertEquals(is.toString(), sum(map(Polynomial::of, is)), of(sumBigInteger(is)));
        }
    }

    private static void compareImplementationsSum() {
        initialize();
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

    private static void propertiesProduct() {
        initialize();
        System.out.println("\t\ttesting product(Iterable<Polynomial>) properties...");

        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.polynomials(),
                Polynomial::multiply,
                Polynomial::product,
                p -> {},
                true
        );

        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            Polynomial product = product(ps);
            assertTrue(
                    ps.toString(),
                    any(p -> p == ZERO, ps) ||
                            product.degree() == IterableUtils.sumInteger(map(Polynomial::degree, ps))
            );
        }

        for (Pair<List<Polynomial>, BigInteger> p : take(LIMIT, P.pairs(P.lists(P.polynomials()), P.bigIntegers()))) {
            assertEquals(p.toString(), product(p.a).apply(p.b), productBigInteger(map(q -> q.apply(p.b), p.a)));
        }

        for (List<BigInteger> rs : take(LIMIT, P.lists(P.bigIntegers()))) {
            assertEquals(rs.toString(), product(map(Polynomial::of, rs)), of(productBigInteger(rs)));
        }
    }

    private static void propertiesDelta() {
        initialize();
        System.out.println("\t\ttesting delta(Iterable<Polynomial>) properties...");

        propertiesDeltaHelper(
                LIMIT,
                P.getWheelsProvider(),
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
    private static void propertiesPow() {
        initialize();
        System.out.println("\t\ttesting pow(int) properties...");

        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.polynomials(),
                P.withScale(5).naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            Polynomial q = p.a.pow(p.b);
            q.validate();
            assertTrue(p.toString(), p.a == ZERO || q.degree() == p.a.degree() * p.b);
            assertEquals(p.toString(), q, pow_simplest(p.a, p.b));
        }

        Iterable<Triple<Polynomial, Integer, BigInteger>> ts1 = P.triples(P.polynomials(), P.withScale(5).naturalIntegersGeometric(), P.bigIntegers());
        for (Triple<Polynomial, Integer, BigInteger> t : take(LIMIT, ts1)) {
            assertEquals(t.toString(), t.a.pow(t.b).apply(t.c), t.a.apply(t.c).pow(t.b));
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.withScale(5).naturalIntegersGeometric()))) {
            assertEquals(p.toString(), of(p.a).pow(p.b), of(p.a.pow(p.b)));
        }

        for (int i : take(LIMIT, P.withScale(20).positiveIntegersGeometric())) {
            assertTrue(Integer.toString(i), ZERO.pow(i) == ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertTrue(p.toString(), p.pow(0) == ONE);
            assertEquals(p.toString(), p.pow(1), p);
            assertEquals(p.toString(), p.pow(2), p.multiply(p));
        }

        for (Triple<Polynomial, Integer, Integer> t : take(LIMIT, P.triples(((QBarIterableProvider) P.withScale(5)).polynomials(), P.withScale(2).naturalIntegersGeometric(), P.withScale(2).naturalIntegersGeometric()))) {
            Polynomial expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            Polynomial expression2 = t.a.pow(t.b + t.c);
            assertEquals(t.toString(), expression1, expression2);
            Polynomial expression5 = t.a.pow(t.b).pow(t.c);
            Polynomial expression6 = t.a.pow(t.b * t.c);
            assertEquals(t.toString(), expression5, expression6);
        }

        Iterable<Triple<Polynomial, Polynomial, Integer>> ts2 = P.triples(P.polynomials(), P.polynomials(), P.withScale(2).naturalIntegersGeometric());
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts2)) {
            Polynomial expression1 = t.a.multiply(t.b).pow(t.c);
            Polynomial expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    private static void compareImplementationsPow() {
        initialize();
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

    private static void propertiesSubstitute() {
        initialize();
        System.out.println("\t\ttesting substitute(Polynomial) properties...");

        Iterable<Polynomial> ps = P.withScale(10).polynomials();
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(ps))) {
            Polynomial substituted = p.a.substitute(p.b);
            substituted.validate();
            assertTrue(
                    p.toString(),
                    p.b == ZERO || substituted == ZERO || substituted.degree() == p.a.degree() * p.b.degree()
            );
        }

        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, P.triples(ps, ps, P.bigIntegers()))) {
            assertEquals(t.toString(), t.a.substitute(t.b).apply(t.c), t.a.apply(t.b.apply(t.c)));
        }

        for (Pair<BigInteger, Polynomial> p : take(LIMIT, P.pairs(P.bigIntegers(), P.polynomials()))) {
            assertEquals(p.toString(), of(p.a).substitute(p.b), of(p.a));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p.toString(), p.substitute(ZERO), p == ZERO ? ZERO : of(p.coefficient(0)));
            assertEquals(p.toString(), p.substitute(ONE), of(IterableUtils.sumBigInteger(p)));
            assertEquals(p.toString(), p.substitute(X), p);
        }
    }

    private static void compareImplementationsSubstitute() {
        initialize();
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

    private static void propertiesIsMonic() {
        initialize();
        System.out.println("\t\ttesting isMonic() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            p.isMonic();
        }

        for (Polynomial p : take(LIMIT, filter(Polynomial::isMonic, P.polynomials()))) {
            assertEquals(p.toString(), p.leading().get(), BigInteger.ONE);
        }
    }

    private static void propertiesIsPrimitive() {
        initialize();
        System.out.println("\t\ttesting isPrimitive() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            p.isPrimitive();
        }

        for (Polynomial p : take(LIMIT, filter(Polynomial::isPrimitive, P.polynomials()))) {
            assertEquals(p.toString(), p.signum(), 1);
            assertEquals(p.toString(), foldl(BigInteger::gcd, BigInteger.ZERO, p), BigInteger.ONE);
        }
    }

    private static void propertiesContentAndPrimitive() {
        initialize();
        System.out.println("\t\ttesting contentAndPrimitive() properties...");

        for (Polynomial p : take(LIMIT, filter(q -> q != ZERO, P.polynomials()))) {
            Pair<BigInteger, Polynomial> contentAndPrimitive = p.contentAndPrimitive();
            BigInteger content = contentAndPrimitive.a;
            assertNotNull(p.toString(), content);
            Polynomial primitive = contentAndPrimitive.b;
            assertNotNull(p.toString(), primitive);
            primitive.validate();
            assertEquals(p.toString(), primitive.degree(), p.degree());
            assertNotEquals(p.toString(), content, BigInteger.ZERO);
            assertTrue(p.toString(), primitive.isPrimitive());
            assertEquals(p.toString(), primitive.multiply(content), p);

            Pair<BigInteger, Polynomial> primitiveCP = primitive.contentAndPrimitive();
            assertEquals(p.toString(), primitiveCP.a, BigInteger.ONE);
            assertEquals(p.toString(), primitive, primitiveCP.b);
        }
    }

    private static void propertiesEquals() {
        initialize();
        System.out.println("\t\ttesting equals(Object) properties...");

        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::polynomials);

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            assertEquals(p.toString(), of(p.a).equals(of(p.b)), p.a.equals(p.b));
        }
    }

    private static void propertiesHashCode() {
        initialize();
        System.out.println("\t\ttesting hashCode() properties...");

        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::polynomials);
    }

    private static void propertiesCompareTo() {
        initialize();
        System.out.println("\t\ttesting compareTo(Polynomial) properties...");

        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::polynomials);

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            assertEquals(p.toString(), p.a.compareTo(p.b), p.a.subtract(p.b).signum());
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            assertEquals(p.toString(), of(p.a).compareTo(of(p.b)), p.a.compareTo(p.b));
        }

        Iterable<Pair<Polynomial, Polynomial>> ps = filter(
                r -> r.a.degree() != r.b.degree(),
                P.pairs(filter(q -> q.signum() == 1, P.polynomials()))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), compare(p.a, p.b), compare(p.a.degree(), p.b.degree()));
        }
    }

    private static void propertiesRead() {
        initialize();
        System.out.println("\t\ttesting read(String) properties...");

        for (String s : take(LIMIT, P.strings())) {
            read(s);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Optional<Polynomial> op = read(p.toString());
            assertEquals(p.toString(), op.get(), p);
        }

        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(POLYNOMIAL_CHARS);
        } else {
            cs = P.uniformSample(POLYNOMIAL_CHARS);
        }
        Iterable<String> ss = filter(s -> read(s).isPresent(), P.strings(cs));
        for (String s : take(LIMIT, ss)) {
            Optional<Polynomial> op = read(s);
            op.get().validate();
        }
    }

    private static void propertiesFindIn() {
        initialize();
        System.out.println("\t\ttesting findIn(String) properties...");

        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.polynomials(),
                Polynomial::read,
                Polynomial::findIn,
                p -> {}
        );
    }

    private static void propertiesToString() {
        initialize();
        System.out.println("\t\ttesting toString() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            String s = p.toString();
            assertTrue(p.toString(), isSubsetOf(s, POLYNOMIAL_CHARS));
            Optional<Polynomial> readP = read(s);
            assertTrue(p.toString(), readP.isPresent());
            assertEquals(p.toString(), readP.get(), p);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).toString(), i.toString());
        }
    }
}
