package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static mho.qbar.objects.Interval.*;
import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntervalProperties {
    private static boolean USE_RANDOM;
    private static final String INTERVAL_CHARS = " (),-/0123456789I[]finty";
    private static final int SMALL_LIMIT = 1000;
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
        System.out.println("Interval properties");
        for (boolean useRandom : Arrays.asList(false, true)) {
            System.out.println("\ttesting " + (useRandom ? "randomly" : "exhaustively"));
            USE_RANDOM = useRandom;
            propertiesGetLower();
            propertiesGetUpper();
        }
        System.out.println("Done");
    }

    private static void propertiesGetLower() {
        initialize();
        System.out.println("\t\ttesting getLower() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            a.getLower();
        }

        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            assertEquals(a.toString(), of(a.getLower().get(), a.getUpper().get()), a);
        }
    }

    private static void propertiesGetUpper() {
        initialize();
        System.out.println("\t\ttesting getUpper() properties...");

        for (Interval a : take(LIMIT, P.intervals())) {
            a.getUpper();
        }
    }
}
