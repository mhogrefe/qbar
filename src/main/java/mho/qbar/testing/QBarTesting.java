package mho.qbar.testing;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.wheels.io.TextInput;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.numberUtils.IntegerUtils;
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
        List<String> list = null;
        Map<String, String> map = null;
        String key = null;
        boolean quoted = false;
        for (String line : new TextInput(QBarTesting.class, "testOutput.txt")) {
            switch (state) {
                case NONE:
                    if (line.isEmpty()) break;
                    String[] tokens = line.split(" ");
                    if (tokens.length != 3 && tokens.length != 4) {
                        throw new IllegalStateException("Bad data header: " + line);
                    }
                    if (tokens.length == 4) {
                        if (!tokens[3].equals("q")) {
                            throw new IllegalStateException("Bad 4th token: " + tokens[3]);
                        }
                        quoted = true;
                    } else {
                        quoted = false;
                    }
                    name = tokens[0];
                    counter = Integer.parseInt(tokens[2]);
                    if (counter < 0) {
                        throw new IllegalStateException("Bad counter: " + counter);
                    }
                    switch (tokens[1]) {
                        case "list":
                            if (testingLists.containsKey(name)) {
                                throw new IllegalStateException("Duplicate list name: " + name);
                            }
                            state = ReadState.LIST;
                            list = new ArrayList<>(counter);
                            break;
                        case "map":
                            if (testingMaps.containsKey(name)) {
                                throw new IllegalStateException("Duplicate map name: " + name);
                            }
                            state = ReadState.MAP;
                            map = new HashMap<>(counter);
                            break;
                        default:
                            throw new IllegalStateException("Bad data type: " + tokens[1]);
                    }
                    if (counter == 0) {
                        if (state == ReadState.LIST) {
                            testingLists.put(name, list);
                            state = ReadState.NONE;
                        } else {
                            testingMaps.put(name, map);
                            state = ReadState.NONE;
                        }
                    }
                    break;
                case LIST:
                    list.add(readTestOutput(line, quoted));
                    counter--;
                    if (counter == 0) {
                        testingLists.put(name, list);
                        state = ReadState.NONE;
                    }
                    break;
                case MAP:
                    int colonIndex = line.indexOf(':');
                    String value = colonIndex == line.length() - 1 ?
                            "" :
                            readTestOutput(line.substring(colonIndex + 2), quoted);
                    map.put(value, line.substring(0, colonIndex));
                    counter--;
                    if (counter == 0) {
                        testingMaps.put(name, map);
                        state = ReadState.NONE;
                    }
            }
        }
        list = null;
        map = null;
    }

    private static @NotNull String readTestOutput(@NotNull String s, boolean quoted) {
        if (quoted) {
            if (s.length() < 2 || head(s) != '\'' || last(s) != '\'') {
                throw new IllegalStateException("line should be quoted: " + s);
            }
            s = s.substring(1, s.length() - 1);
        }
        if (!elem('\\', s)) return s;
        StringBuilder sb = new StringBuilder();
        boolean sawBackslash = false;
        int counter4 = -1;
        int cAcc = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (sawBackslash) {
                if (c == '\\') {
                    sb.append('\\');
                    sawBackslash = false;
                    continue;
                } else if (c == 'u') {
                    counter4 = 4;
                    sawBackslash = false;
                    continue;
                } else {
                    throw new IllegalStateException("Improperly escaped backslash: " + s);
                }
            }
            if (counter4 != -1) {
                cAcc = cAcc * 16 + IntegerUtils.fromDigit(Character.toUpperCase(c));
                counter4--;
                if (counter4 == 0) {
                    sb.append((char) cAcc);
                    counter4 = -1;
                    cAcc = 0;
                }
            } else if (c != '\\') {
                sb.append(c);
            }
            sawBackslash = c == '\\';
        }
        return sb.toString();
    }

    public static void aeqitQBarLog(Iterable<?> a, String b) {
        if (testingLists == null) {
            initializeTestData();
        }
        List<String> list = testingLists.get(b);
        List<String> actual = toList(map(Objects::toString, a));
        if (!Objects.equals(list, actual)) {
            boolean quote = containsTrailingSpaces(actual);
            System.out.println();
            System.out.print(b + " list " + actual.size());
            if (quote) {
                System.out.println(" q");
            } else {
                System.out.println();
            }
            for (String s : actual) {
                if (quote) System.out.print('\'');
                System.out.print(escape(s));
                if (quote) {
                    System.out.println('\'');
                } else {
                    System.out.println();
                }
            }
            fail("No match for " + b);
        }
    }

    public static void aeqitLimitQBarLog(int limit, Iterable<?> a, String b) {
        if (testingLists == null) {
            initializeTestData();
        }
        List<String> list = testingLists.get(b);
        List<String> actual = itsList(limit, a);
        if (!Objects.equals(list, actual)) {
            boolean quote = containsTrailingSpaces(actual);
            System.out.println();
            System.out.print(b + " list " + actual.size());
            if (quote) {
                System.out.println(" q");
            } else {
                System.out.println();
            }
            for (String s : actual) {
                if (quote) System.out.print('\'');
                System.out.print(escape(s));
                if (quote) {
                    System.out.println('\'');
                } else {
                    System.out.println();
                }
            }
            fail("No match for " + b);
        }
    }

    public static void aeqMapQBarLog(Map<?, ?> a, String b) {
        if (testingLists == null) {
            initializeTestData();
        }
        Map<String, String> map = testingMaps.get(b);
        Map<String, String> actual = itsMap(a);
        if (!Objects.equals(map, actual)) {
            boolean quote = containsTrailingSpaces(actual);
            System.out.println();
            System.out.print(b + " map " + actual.size());
            if (quote) {
                System.out.println(" q");
            } else {
                System.out.println();
            }
            Map<Integer, Set<String>> sortedActual = new TreeMap<>(Comparator.reverseOrder());
            for (Map.Entry<String, String> entry : actual.entrySet()) {
                int frequency = Integer.parseInt(entry.getValue());
                Set<String> values = sortedActual.get(frequency);
                if (values == null) {
                    values = new TreeSet<>();
                    sortedActual.put(frequency, values);
                }
                values.add(entry.getKey());
            }
            for (Map.Entry<Integer, Set<String>> entry : sortedActual.entrySet()) {
                int frequency = entry.getKey();
                for (String value : entry.getValue()) {
                    System.out.print(frequency);
                    System.out.print(": ");
                    if (quote) System.out.print('\'');
                    System.out.print(escape(value));
                    if (quote) {
                        System.out.println('\'');
                    } else {
                        System.out.println();
                    }
                }
            }
            fail("No match for " + b);
        }
    }

    private static boolean containsTrailingSpaces(@NotNull List<String> strings) {
        for (String s : strings) {
            if (!s.isEmpty() && last(s) == ' ') return true;
        }
        return false;
    }

    private static boolean containsTrailingSpaces(@NotNull Map<String, String> strings) {
        for (Map.Entry<String, String> entry : strings.entrySet()) {
            if (!entry.getKey().isEmpty() && last(entry.getKey()) == ' ') return true;
            if (!entry.getValue().isEmpty() && last(entry.getValue()) == ' ') return true;
        }
        return false;
    }

    private static @NotNull String escape(@NotNull String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < ' ' || c > '~') {
                sb.append("\\u").append(String.format("%4s", Integer.toHexString(c)).replace(' ', '0'));
            } else {
                if (c == '\\') {
                    sb.append('\\');
                }
                sb.append(c);
            }
        }
        return sb.toString();
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
            boolean denseInUsedCharString,
            boolean strict
    ) {
        for (String s : take(limit, P.strings())) {
            read.apply(s);
        }

        if (strict) {
            for (T x : take(limit, xs)) {
                Optional<T> ox = read.apply(x.toString());
                T y = ox.get();
                validate.accept(y);
                assertEquals(x, y, x);
            }
        }

        if (denseInUsedCharString) {
            for (String s : take(limit, filterInfinite(t -> read.apply(t).isPresent(), P.strings(usedChars)))) {
                inverse(t -> read.apply(t).get(), Object::toString, s);
                validate.accept(read.apply(s).get());
            }
        }
    }
}
