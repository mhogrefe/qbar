package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static mho.qbar.objects.Rational.ONE;
import static mho.qbar.objects.Rational.ZERO;
import static mho.qbar.objects.RationalVector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("ConstantConditions")
public class RationalVectorProperties {
    private static boolean USE_RANDOM;
    private static final String RATIONAL_VECTOR_CHARS = " ,-/0123456789[]";
    private static final int SMALL_LIMIT = 100;
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
            propertiesDimension();
            propertiesZero();
            propertiesIdentity();
            propertiesIsZero();
            propertiesNegate();
            propertiesAdd();
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
            assertEquals(rs.toString(), v.dimension(), rs.size());
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

    private static void propertiesDimension() {
        initialize();
        System.out.println("\t\ttesting dimension() properties");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            int dimension = v.dimension();
            assertTrue(v.toString(), dimension >= 0);
            assertEquals(v.toString(), length(v), dimension);
        }
    }

    private static void propertiesZero() {
        initialize();
        System.out.println("\t\ttesting zero(int) properties");

        Iterable<Integer> is;
        if (P instanceof ExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = ((RandomProvider) P).naturalIntegersGeometric(20);
        }
        for (int i : take(SMALL_LIMIT, is)) {
            RationalVector zero = zero(i);
            validate(zero);
            assertEquals(Integer.toString(i), zero.dimension(), i);
            assertTrue(Integer.toString(i), zero.isZero());
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                zero(i);
                fail(Integer.toString(i));
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static void propertiesIdentity() {
        initialize();
        System.out.println("\t\ttesting identity(int, int) properties");

        Iterable<Integer> is;
        if (P instanceof ExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = ((RandomProvider) P).naturalIntegersGeometric(20);
        }
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, filter(q -> q.a > q.b, P.pairs(is)))) {
            assert p.a != null;
            assert p.b != null;
            RationalVector identity = identity(p.a, p.b);
            validate(identity);
            assertEquals(p.toString(), identity.dimension(), p.a.intValue());
            List<Rational> sortedCoordinates = reverse(sort(identity));
            assertTrue(p.toString(), head(sortedCoordinates) == ONE);
            assertTrue(p.toString(), all(x -> x == ZERO, tail(sortedCoordinates)));
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.rangeDown(0), P.integers()))) {
            assert p.a != null;
            assert p.b != null;
            try {
                identity(p.a, p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.integers()))) {
            assert p.a != null;
            assert p.b != null;
            try {
                identity(p.a, p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, filter(q -> q.a <= q.b, P.pairs(P.naturalIntegers())))) {
            assert p.a != null;
            assert p.b != null;
            try {
                identity(p.a, p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static void propertiesIsZero() {
        initialize();
        System.out.println("\t\ttesting isZero properties");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            v.isZero();
        }
    }

    private static void propertiesNegate() {
        initialize();
        System.out.println("\t\ttesting negate() properties");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            RationalVector negativeV = v.negate();
            validate(negativeV);
            assertEquals(v.toString(), v.dimension(), negativeV.dimension());
            assertEquals(v.toString(), v, negativeV.negate());
            assertTrue(v.add(negativeV).isZero());
        }

        for (RationalVector v : take(LIMIT, filter(w -> any(x -> x != ZERO, w), P.rationalVectors()))) {
            RationalVector negativeV = v.negate();
            assertNotEquals(v.toString(), v, negativeV);
        }
    }

    private static void propertiesAdd() {
        initialize();
        System.out.println("\t\ttesting add(RationalVector) properties...");

        Iterable<Pair<RationalVector, RationalVector>> ps;
        if (P instanceof ExhaustiveProvider) {
            ps = P.dependentPairs(P.rationalVectors(), v -> P.rationalVectors(v.dimension()));
        } else {
            ps = P.dependentPairs(
                    ((QBarRandomProvider) P).rationalVectorsBySize(32),
                    v -> ((QBarRandomProvider) P).rationalVectorsBySize(32, v.dimension())
            );
        }
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            RationalVector sum = p.a.add(p.b);
            validate(sum);
            assertEquals(p.toString(), sum, p.b.add(p.a));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertTrue(v.add(v.negate()).isZero());
        }

        Iterable<Triple<RationalVector, RationalVector, RationalVector>> ts;
        if (P instanceof ExhaustiveProvider) {
            ts = map(
                    p -> new Triple<RationalVector, RationalVector, RationalVector>(p.a, p.b.a, p.b.b),
                    P.dependentPairs(P.rationalVectors(), v -> P.pairs(P.rationalVectors(v.dimension())))
            );
        } else {
            ts = map(
                    p -> new Triple<RationalVector, RationalVector, RationalVector>(p.a, p.b.a, p.b.b),
                    P.dependentPairs(
                            ((QBarRandomProvider) P).rationalVectorsBySize(32),
                            v -> P.pairs(((QBarRandomProvider) P).rationalVectorsBySize(32, v.dimension()))
                    )
            );
        }
        for (Triple<RationalVector, RationalVector, RationalVector> t : take(LIMIT, ts)) {
            RationalVector sum1 = t.a.add(t.b).add(t.c);
            RationalVector sum2 = t.a.add(t.b.add(t.c));
            assertEquals(t.toString(), sum1, sum2);
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
