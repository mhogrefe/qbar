package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@SuppressWarnings("ConstantConditions")
public class RationalPolynomialProperties {
    private static boolean USE_RANDOM;
    private static final String RATIONAL_POLYNOMIAL_CHARS = "*+-/0123456789^x";
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
        System.out.println("RationalPolynomial properties");
        for (boolean useRandom : Arrays.asList(false, true)) {
            System.out.println("\ttesting " + (useRandom ? "randomly" : "exhaustively"));
            USE_RANDOM = useRandom;
            propertiesIterator();
            propertiesCoefficient();
            propertiesOf_List_Rational();
        }
        System.out.println("Done");
    }

    private static void propertiesIterator() {
        initialize();
        System.out.println("\t\ttesting iterator() properties...");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            List<Rational> rs = toList(p);
            assertTrue(p.toString(), all(r -> r != null, rs));
            assertEquals(p.toString(), of(toList(p)), p);
            try {
                p.iterator().remove();
            } catch (UnsupportedOperationException ignored) {}
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            List<Rational> rs = toList(p);
            assertTrue(p.toString(), last(rs) != Rational.ZERO);
        }
    }

    private static void propertiesCoefficient() {
        initialize();
        System.out.println("\t\ttesting coefficient(int) properties...");

        Iterable<Pair<RationalPolynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.rationalPolynomials(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.rationalPolynomials(), ((RandomProvider) P).naturalIntegersGeometric(10));
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            p.a.coefficient(p.b);
        }

        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, filter(q -> q.b <= q.a.degree(), ps))) {
            assertEquals(p.toString(), p.a.coefficient(p.b), toList(p.a).get(p.b));
        }

        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, filter(q -> q.b > q.a.degree(), ps))) {
            assertEquals(p.toString(), p.a.coefficient(p.b), Rational.ZERO);
        }

        Iterable<Pair<RationalPolynomial, Integer>> psFail = P.pairs(P.rationalPolynomials(), P.negativeIntegers());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.coefficient(p.b);
                fail(p.toString());
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }
    }

    private static void propertiesOf_List_Rational() {
        initialize();
        System.out.println("\t\ttesting of(List<Rational>) properties");

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            RationalPolynomial p = of(rs);
            validate(p);
            assertTrue(rs.toString(), length(p) <= rs.size());
        }

        Iterable<List<Rational>> rss = filter(rs -> rs.isEmpty() || last(rs) != Rational.ZERO, P.lists(P.rationals()));
        for (List<Rational> rs : take(LIMIT, rss)) {
            assertEquals(rs.toString(), toList(of(rs)), rs);
        }

        Iterable<List<Rational>> failRss = map(
                p -> toList(insert(p.a, p.b, null)),
                (Iterable<Pair<List<Rational>, Integer>>) P.dependentPairsLogarithmic(
                        P.lists(P.rationals()),
                        rs -> range(0, rs.size())
                )
        );
        for (List<Rational> rs : take(LIMIT, failRss)) {
            try {
                of(rs);
                fail(rs.toString());
            } catch (NullPointerException ignored) {}
        }
    }

    private static void validate(@NotNull RationalPolynomial p) {
        List<Rational> coefficients = toList(p);
        if (!coefficients.isEmpty()) {
            assertTrue(p.toString(), last(coefficients) != Rational.ZERO);
        }
        if (p.equals(ZERO)) assertTrue(p.toString(), p == ZERO);
        if (p.equals(ONE)) assertTrue(p.toString(), p == ONE);
    }
}
