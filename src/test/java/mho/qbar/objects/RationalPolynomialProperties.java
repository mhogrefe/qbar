package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
}
