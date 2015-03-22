package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static mho.qbar.objects.Polynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    private static void validate(@NotNull Polynomial p) {
        List<BigInteger> coefficients = toList(p);
        if (!coefficients.isEmpty()) {
            assertTrue(p.toString(), !last(coefficients).equals(BigInteger.ZERO));
        }
        if (p.equals(ZERO)) assertTrue(p.toString(), p == ZERO);
        if (p.equals(ONE)) assertTrue(p.toString(), p == ONE);
    }
}
