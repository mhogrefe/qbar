package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static mho.qbar.objects.RationalVector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RationalVectorProperties {
    private static boolean USE_RANDOM;
    private static final String RATIONAL_VECTOR_CHARS = " ,-/0123456789[]";
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
        System.out.println("RationalVector properties");
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

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            List<Rational> rs = toList(v);
            assertTrue(v.toString(), all(r -> r != null, rs));
            assertEquals(v.toString(), of(toList(v)), v);
        }
    }

    private static void validate(@NotNull RationalVector v) {
        assertTrue(v.toString(), all(r -> r != null, v));
        if (v.equals(ZERO_DIMENSIONAL)) assertTrue(v.toString(), v == ZERO_DIMENSIONAL);
    }
}
