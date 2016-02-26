package mho.qbar.testing;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.wheels.io.TextInput;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.eq;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.testing.Testing.*;

public class QBarTesting {
    public static final @NotNull QBarExhaustiveProvider QEP = QBarExhaustiveProvider.INSTANCE;
    private enum ReadState { NONE, LIST, MAP }
    private static Map<String, List<String>> testingLists = null;
    private static Map<String, Map<String, String>> testingMaps = null;

    private static void initializeTestData() {
        testingLists = new HashMap<>();
        testingMaps = new HashMap<>();
        ReadState state = ReadState.NONE;
        int counter = 0;
        String name = "";
        List<String> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        String key = null;
        for (String line : new TextInput(QBarTesting.class, "testOutput.txt")) {
            switch (state) {
                case NONE:
                    if (line.isEmpty()) break;
                    String[] tokens = line.split(" ");
                    if (tokens.length != 3) {
                        throw new IllegalStateException("Bad data header: " + line);
                    }
                    name = tokens[0];
                    counter = Integer.parseInt(tokens[2]);
                    if (counter < 0) {
                        throw new IllegalStateException("Bad counter: " + counter);
                    }
                    if (counter == 0) break;
                    switch (tokens[1]) {
                        case "list":
                            state = ReadState.LIST;
                            break;
                        case "map":
                            state = ReadState.MAP;
                            break;
                        default:
                            throw new IllegalStateException("Bad data type: " + tokens[1]);
                    }
                    break;
                case LIST:
                    list.add(line);
                    counter--;
                    if (counter == 0) {
                        testingLists.put(name, list);
                        list = new ArrayList<>();
                        state = ReadState.NONE;
                    }
                    break;
                case MAP:
                    if (key == null) {
                        key = line;
                    } else {
                        map.put(key, line);
                        key = null;
                        counter--;
                        if (counter == 0) {
                            testingMaps.put(name, map);
                            map = new HashMap<>();
                            state = ReadState.NONE;
                        }
                    }
            }
        }
        list = null;
        map = null;
    }

    public static void aeqitQBarLog(Iterable<?> a, String b) {
        if (testingLists == null) {
            initializeTestData();
        }
        List<String> list = testingLists.get(b);
        if (list == null) {
            list = new ArrayList<>();
        }
        List<String> actual = toList(map(Object::toString, a));
        if (!list.equals(actual)) {
            System.out.println("Error! No match for " + b);
            System.out.println();
            System.out.println(b + " list " + list.size());
            for (String s : list) {
                System.out.println(s);
            }
        }
    }

    public static void aeqitLimitQBarLog(int limit, Iterable<?> a, String b) {
        if (testingLists == null) {
            initializeTestData();
        }
        List<String> list = testingLists.get(b);
        if (list == null) {
            list = new ArrayList<>();
        }
        List<String> actual = itsList(limit, a);
        if (!list.equals(actual)) {
            System.out.println();
            System.out.println(b + " list " + actual.size());
            for (String s : actual) {
                System.out.println(s);
            }
            fail("No match for " + b);
        }
    }

    public static void aeqMapQBarLog(Map<?, ?> a, String b) {
        if (testingLists == null) {
            initializeTestData();
        }
        Map<String, String> map = testingMaps.get(b);
        if (map == null) {
            map = new HashMap<>();
        }
        Map<String, String> actual = itsMap(a);
        if (!map.equals(actual)) {
            System.out.println();
            System.out.println(b + " map " + actual.size());
            for (Map.Entry<String, String> entry : actual.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }
            fail("No match for " + b);
        }
    }

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
        for (Pair<T, T> p : take(limit, ExhaustiveProvider.INSTANCE.pairs(fxs.apply(ip), fxs.apply(iq)))) {
            symmetric(Object::equals, p);
        }

        ip.reset();
        iq.reset();
        ir.reset();
        Iterable<Triple<T, T, T>> ts = ExhaustiveProvider.INSTANCE.triples(
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
        for (Pair<T, T> p : take(limit, ExhaustiveProvider.INSTANCE.pairs(fxs.apply(ip), fxs.apply(iq)))) {
            int compare = p.a.compareTo(p.b);
            assertTrue(p, compare == 0 || compare == 1 || compare == -1);
            antiSymmetric(Ordering::le, p);
            assertTrue(p, le(p.a, p.b) || le(p.b, p.a));
            antiCommutative(Comparable::compareTo, c -> -c, p);
        }

        ip.reset();
        iq.reset();
        ir.reset();
        Iterable<Triple<T, T, T>> ts = ExhaustiveProvider.INSTANCE.triples(
                fxs.apply(ip),
                fxs.apply(iq),
                fxs.apply(ir)
        );
        for (Triple<T, T, T> t : take(limit, ts)) {
            transitive(Ordering::le, t);
        }
    }

    public static <T> void propertiesReadHelper(
            int limit,
            @NotNull QBarIterableProvider P,
            @NotNull String usedChars,
            @NotNull Iterable<T> xs,
            @NotNull Function<String, Optional<T>> read,
            @NotNull Consumer<T> validate,
            boolean denseInUsedCharString
    ) {
        for (String s : take(limit, P.strings())) {
            read.apply(s);
        }

        for (T x : take(limit, xs)) {
            Optional<T> ox = read.apply(x.toString());
            T y = ox.get();
            validate.accept(y);
            assertEquals(x, y, x);
        }

        if (denseInUsedCharString) {
            for (String s : take(limit, filterInfinite(t -> read.apply(t).isPresent(), P.strings(usedChars)))) {
                inverse(t -> read.apply(t).get(), Object::toString, s);
                validate.accept(read.apply(s).get());
            }
        }
    }
}
