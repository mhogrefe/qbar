package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static mho.qbar.objects.Polynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class PolynomialProperties {
    private static boolean USE_RANDOM;
    private static final String POLYNOMIAL_CHARS = "*+-0123456789^x";
    private static int LIMIT;

    private static QBarIterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = new QBarRandomProvider(new Random(0x6af477d9a7e54fcaL));
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
            propertiesCoefficient();
            propertiesOf_List_BigInteger();
            propertiesOf_BigInteger();
            propertiesOf_BigInteger_int();
            propertiesDegree();
            propertiesLeading();
            propertiesNegate();
            propertiesAbs();
            propertiesSignum();
            propertiesEquals();
            propertiesHashCode();
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

    private static void propertiesCoefficient() {
        initialize();
        System.out.println("\t\ttesting coefficient(int) properties...");

        Iterable<Pair<Polynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.polynomials(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.polynomials(), ((RandomProvider) P).naturalIntegersGeometric(10));
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
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }
    }

    private static void propertiesOf_List_BigInteger() {
        initialize();
        System.out.println("\t\ttesting of(List<BigInteger>) properties");

        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            Polynomial p = of(is);
            validate(p);
            assertTrue(is.toString(), length(p) <= is.size());
        }

        Iterable<List<BigInteger>> iss = filter(
                is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                P.lists(P.bigIntegers())
        );
        for (List<BigInteger> is : take(LIMIT, iss)) {
            assertEquals(is.toString(), toList(of(is)), is);
        }

        Iterable<List<BigInteger>> failIss = map(
                p -> toList(insert(p.a, p.b, null)),
                (Iterable<Pair<List<BigInteger>, Integer>>) P.dependentPairsLogarithmic(
                        P.lists(P.bigIntegers()),
                        rs -> range(0, rs.size())
                )
        );
        for (List<BigInteger> is : take(LIMIT, failIss)) {
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
            validate(p);
            assertTrue(i.toString(), p.degree() == 0 || p.degree() == -1);
            assertTrue(i.toString(), length(p) == 1 || length(p) == 0);
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
            ps = P.pairs(P.bigIntegers(), ((RandomProvider) P).naturalIntegersGeometric(20));
        }
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            Polynomial q = of(p.a, p.b);
            validate(q);
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, filter(q -> !q.a.equals(BigInteger.ZERO), ps))) {
            Polynomial q = of(p.a, p.b);
            List<BigInteger> coefficients = toList(q);
            assertEquals(p.toString(), length(filter(i -> !i.equals(BigInteger.ZERO), coefficients)), 1);
            assertEquals(p.toString(), q.degree(), p.b.intValue());
            assertEquals(p.toString(), length(q), p.b + 1);
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

    private static void propertiesNegate() {
        initialize();
        System.out.println("\t\ttesting negate() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Polynomial negative = p.negate();
            validate(negative);
            assertEquals(p.toString(), p, negative.negate());
            //todo assertTrue(p.toString(), p.add(negative) == ZERO);
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
            validate(abs);
            assertEquals(p.toString(), abs, abs.abs());
            assertNotEquals(p.toString(), abs.signum(), -1);
            //todo assertTrue(p.toString(), ge(abs, ZERO));
        }
    }

    private static void propertiesSignum() {
        initialize();
        System.out.println("\t\ttesting signum() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            int signum = p.signum();
            assertEquals(p.toString(), signum, Ordering.compare(p, ZERO).toInt());
            assertTrue(p.toString(), signum == -1 || signum == 0 || signum == 1);
        }
    }

    private static void propertiesEquals() {
        initialize();
        System.out.println("\t\ttesting equals(Object) properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            //noinspection EqualsWithItself
            assertTrue(p.toString(), p.equals(p));
            //noinspection ObjectEqualsNull
            assertFalse(p.toString(), p.equals(null));
        }
    }

    private static void propertiesHashCode() {
        initialize();
        System.out.println("\t\ttesting hashCode() properties...");

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p.toString(), p.hashCode(), p.hashCode());
        }
    }

    private static void validate(@NotNull Polynomial p) {
        List<BigInteger> coefficients = toList(p);
        if (!coefficients.isEmpty()) {
            assertTrue(p.toString(), !last(coefficients).equals(BigInteger.ZERO));
        }
        if (p.equals(ZERO)) assertTrue(p.toString(), p == ZERO);
        if (p.equals(ONE)) assertTrue(p.toString(), p == ONE);
    }
}
