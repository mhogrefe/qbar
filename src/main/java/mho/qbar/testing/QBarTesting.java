package mho.qbar.testing;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.iterables.IterableUtils.zip;
import static mho.wheels.iterables.IterableUtils.zip3;
import static mho.wheels.ordering.Ordering.eq;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.testing.Testing.*;

public class QBarTesting {
    public static <T> void propertiesEqualsHelper(
            int limit,
            @NotNull QBarIterableProvider ip,
            @NotNull Function<QBarIterableProvider, Iterable<T>> fxs
    ) {
        QBarIterableProvider iq = ip.deepCopy();
        QBarIterableProvider ir = ip.deepCopy();
        for (Triple<T, T, T> t : take(limit, zip3(fxs.apply(ip), fxs.apply(iq), fxs.apply(ir)))) {
            //noinspection ObjectEqualsNull
            assertFalse(t, t.a.equals(null));
            assertTrue(t, t.a.equals(t.b));
            assertTrue(t, t.b.equals(t.c));
        }

        ip.reset();
        iq.reset();
        for (Pair<T, T> p : take(limit, ExhaustiveProvider.INSTANCE.pairsLex(fxs.apply(ip), fxs.apply(iq)))) {
            symmetric(Object::equals, p);
        }

        ip.reset();
        iq.reset();
        ir.reset();
        Iterable<Triple<T, T, T>> ts = ExhaustiveProvider.INSTANCE.triplesLex(
                fxs.apply(ip),
                fxs.apply(iq),
                fxs.apply(ir)
        );
        for (Triple<T, T, T> t : take(limit, ts)) {
            transitive(Object::equals, t);
        }
    }

    public static <T> void propertiesHashCodeHelper(
            int limit,
            @NotNull QBarIterableProvider ip,
            @NotNull Function<QBarIterableProvider, Iterable<T>> fxs
    ) {
        QBarIterableProvider iq = ip.deepCopy();
        for (Pair<T, T> p : take(limit, zip(fxs.apply(ip), fxs.apply(iq)))) {
            assertTrue(p, p.a.equals(p.b));
            assertEquals(p, p.a.hashCode(), p.b.hashCode());
        }
    }

    public static <T extends Comparable<T>> void propertiesCompareToHelper(
            int limit,
            @NotNull QBarIterableProvider ip,
            @NotNull Function<QBarIterableProvider, Iterable<T>> fxs
    ) {
        QBarIterableProvider iq = ip.deepCopy();
        QBarIterableProvider ir = ip.deepCopy();
        for (Pair<T, T> p : take(limit, zip(fxs.apply(ip), fxs.apply(iq)))) {
            assertTrue(p, eq(p.a, p.b));
        }

        ip.reset();
        iq.reset();
        for (Pair<T, T> p : take(limit, ExhaustiveProvider.INSTANCE.pairsLex(fxs.apply(ip), fxs.apply(iq)))) {
            int compare = p.a.compareTo(p.b);
            assertTrue(p, compare == 0 || compare == 1 || compare == -1);
            antiSymmetric(Ordering::le, p);
            assertTrue(p, le(p.a, p.b) || le(p.b, p.a));
            antiCommutative(Comparable::compareTo, c -> -c, p);
        }

        ip.reset();
        iq.reset();
        ir.reset();
        Iterable<Triple<T, T, T>> ts = ExhaustiveProvider.INSTANCE.triplesLex(
                fxs.apply(ip),
                fxs.apply(iq),
                fxs.apply(ir)
        );
        for (Triple<T, T, T> t : take(limit, ts)) {
            transitive(Ordering::le, t);
        }
    }
}
