package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static mho.qbar.objects.RationalVector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

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
            propertiesX_int();
            propertiesOf_List_Rational();
            propertiesOf_Rational();
            propertiesEquals();
            propertiesHashCode();
            propertiesCompareTo();
            propertiesRead();
            propertiesFindIn();
            propertiesToString();
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

    private static void propertiesX_int() {
        initialize();
        System.out.println("\t\ttesting x(int) properties");

        Iterable<Pair<RationalVector, Integer>> ps = P.dependentPairsLogarithmic(
                P.rationalVectors(),
                v -> range(0, v.dimension() - 1)
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational x = p.a.x(p.b);
            assertEquals(p.toString(), x, toList(p.a).get(p.b));
        }

        Iterable<Pair<RationalVector, Integer>> psFail = filter(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return p.b < 0 || p.b >= p.a.dimension();
                },
                P.pairs(P.rationalVectors(), P.integers())
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, psFail)) {
            assert p.a != null;
            assert p.b != null;
            try {
                p.a.x(p.b);
                fail(p.toString());
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private static void propertiesOf_List_Rational() {
        initialize();
        System.out.println("\t\ttesting of(List<Rational>) properties");
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            RationalVector v = of(rs);
            validate(v);
            assertEquals(rs.toString(), toList(v), rs);
        }

        Iterable<List<Rational>> failRss = map(p -> {
            assert p.a != null;
            assert p.b != null;
            return toList(insert(p.a, p.b, null));
        }, (Iterable<Pair<List<Rational>, Integer>>) P.dependentPairsLogarithmic(
                P.lists(P.rationals()),
                rs -> range(0, rs.size())
        ));
        for (List<Rational> rs : take(LIMIT, failRss)) {
            try {
                of(rs);
                fail(rs.toString());
            } catch (NullPointerException ignored) {}
        }
    }

    private static void propertiesOf_Rational() {
        initialize();
        System.out.println("\t\ttesting of(Rational) properties");
        for (Rational r : take(LIMIT, P.rationals())) {
            RationalVector v = of(r);
            validate(v);
            assertEquals(r.toString(), v.dimension(), 1);
        }
    }

    private static void propertiesEquals() {
        initialize();
        System.out.println("\t\ttesting equals(Object) properties...");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            //noinspection EqualsWithItself
            assertTrue(v.toString(), v.equals(v));
            //noinspection ObjectEqualsNull
            assertFalse(v.toString(), v.equals(null));
        }
    }

    private static void propertiesHashCode() {
        initialize();
        System.out.println("\t\ttesting hashCode() properties...");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), v.hashCode(), v.hashCode());
        }
    }

    private static void propertiesCompareTo() {
        initialize();
        System.out.println("\t\ttesting compareTo(RationalVector) properties...");

        for (Pair<RationalVector, RationalVector> p : take(LIMIT, P.pairs(P.rationalVectors()))) {
            assert p.a != null;
            assert p.b != null;
            int compare = p.a.compareTo(p.b);
            assertTrue(p.toString(), compare == -1 || compare == 0 || compare == 1);
            assertEquals(p.toString(), p.b.compareTo(p.a), -compare);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), v.compareTo(v), 0);
        }

        Iterable<Triple<RationalVector, RationalVector, RationalVector>> ts = filter(
                t -> {
                    assert t.a != null;
                    assert t.b != null;
                    return lt(t.a, t.b) && lt(t.b, t.c);
                },
                P.triples(P.rationalVectors())
        );
        for (Triple<RationalVector, RationalVector, RationalVector> t : take(LIMIT, ts)) {
            assert t.a != null;
            assert t.c != null;
            assertEquals(t.toString(), t.a.compareTo(t.c), -1);
        }

        Iterable<Pair<RationalVector, RationalVector>> ps = filter(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return p.a.dimension() != p.b.dimension();
                },
                P.pairs(P.rationalVectors())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), compare(p.a, p.b), compare(p.a.dimension(), p.b.dimension()));
        }
    }

    private static void propertiesRead() {
        initialize();
        System.out.println("\t\ttesting read(String) properties...");

        for (String s : take(LIMIT, P.strings())) {
            read(s);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            Optional<RationalVector> ov = read(v.toString());
            assertEquals(v.toString(), ov.get(), v);
        }
    }

    private static void propertiesFindIn() {
        initialize();
        System.out.println("\t\ttesting findIn(String) properties...");

        for (String s : take(LIMIT, P.strings())) {
            findIn(s);
        }

        Iterable<Pair<String, Integer>> ps = P.dependentPairsLogarithmic(P.strings(), s -> range(0, s.length()));
        Iterable<String> ss = map(
                p -> {
                    assert p.a != null;
                    assert p.a.a != null;
                    assert p.a.b != null;
                    return take(p.a.b, p.a.a) + p.b + drop(p.a.b, p.a.a);
                },
                P.pairs(ps, P.rationalVectors())
        );
        for (String s : take(LIMIT, ss)) {
            Optional<Pair<RationalVector, Integer>> op = findIn(s);
            Pair<RationalVector, Integer> p = op.get();
            assertNotNull(s, p.a);
            assertNotNull(s, p.b);
            assertTrue(s, p.b >= 0 && p.b < s.length());
            String before = take(p.b, s);
            assertFalse(s, findIn(before).isPresent());
            String during = p.a.toString();
            assertTrue(s, s.substring(p.b).startsWith(during));
            String after = drop(p.b + during.length(), s);
            assertTrue(s, after.isEmpty() || !read(during + head(after)).isPresent());
        }
    }

    private static void propertiesToString() {
        initialize();
        System.out.println("\t\ttesting toString() properties...");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            String s = v.toString();
            assertTrue(isSubsetOf(s, RATIONAL_VECTOR_CHARS));
            Optional<RationalVector> readV = read(s);
            assertTrue(v.toString(), readV.isPresent());
            assertEquals(v.toString(), readV.get(), v);
        }
    }

    private static void validate(@NotNull RationalVector v) {
        assertTrue(v.toString(), all(r -> r != null, v));
        if (v.equals(ZERO_DIMENSIONAL)) assertTrue(v.toString(), v == ZERO_DIMENSIONAL);
    }
}