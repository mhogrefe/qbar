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
            propertiesX();
            propertiesY();
            propertiesZ();
            propertiesW();
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

    private static void propertiesX() {
        initialize();
        System.out.println("\t\ttesting x() properties");

        Iterable<RationalVector> vs = map(p -> {
            assert p.b != null;
            return RationalVector.of(toList(cons(p.a, p.b)));
        }, P.pairs(P.rationals(), P.rationalVectors()));
        for (RationalVector v : take(LIMIT, vs)) {
            Rational x = v.x();
            assertEquals(v.toString(), x, toList(v).get(0));
        }
    }

    private static void propertiesY() {
        initialize();
        System.out.println("\t\ttesting y() properties");

        Iterable<RationalVector> vs = map(p -> {
            assert p.a != null;
            assert p.b != null;
            return RationalVector.of(toList(concat(p.a, p.b)));
        }, P.pairs(P.rationalVectors(2), P.rationalVectors()));
        for (RationalVector v : take(LIMIT, vs)) {
            Rational y = v.y();
            assertEquals(v.toString(), y, toList(v).get(1));
        }
    }

    private static void propertiesZ() {
        initialize();
        System.out.println("\t\ttesting z() properties");

        Iterable<RationalVector> vs = map(p -> {
            assert p.a != null;
            assert p.b != null;
            return RationalVector.of(toList(concat(p.a, p.b)));
        }, P.pairs(P.rationalVectors(3), P.rationalVectors()));
        for (RationalVector v : take(LIMIT, vs)) {
            Rational z = v.z();
            assertEquals(v.toString(), z, toList(v).get(2));
        }
    }

    private static void propertiesW() {
        initialize();
        System.out.println("\t\ttesting w() properties");

        Iterable<RationalVector> vs = map(p -> {
            assert p.a != null;
            assert p.b != null;
            return RationalVector.of(toList(concat(p.a, p.b)));
        }, P.pairs(P.rationalVectors(4), P.rationalVectors()));
        for (RationalVector v : take(LIMIT, vs)) {
            Rational w = v.w();
            assertEquals(v.toString(), w, toList(v).get(3));
        }
    }

    private static void validate(@NotNull RationalVector v) {
        assertTrue(v.toString(), all(r -> r != null, v));
        if (v.equals(ZERO_DIMENSIONAL)) assertTrue(v.toString(), v == ZERO_DIMENSIONAL);
    }
}
