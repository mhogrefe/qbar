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

import java.math.BigInteger;
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
            propertiesSubtract();
            propertiesMultiply_Rational();
            propertiesMultiply_BigInteger();
            propertiesMultiply_int();
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
            try {
                v.iterator().remove();
            } catch (UnsupportedOperationException ignored) {}
        }
    }

    private static void propertiesX() {
        initialize();
        System.out.println("\t\ttesting x() properties");

        Iterable<RationalVector> vs = map(
                p -> RationalVector.of(toList(cons(p.a, p.b))),
                P.pairs(P.rationals(), P.rationalVectors())
        );
        for (RationalVector v : take(LIMIT, vs)) {
            Rational x = v.x();
            assertEquals(v.toString(), x, toList(v).get(0));
        }
    }

    private static void propertiesY() {
        initialize();
        System.out.println("\t\ttesting y() properties");

        Iterable<RationalVector> vs = map(
                p -> RationalVector.of(toList(concat(p.a, p.b))),
                P.pairs(P.rationalVectors(2), P.rationalVectors())
        );
        for (RationalVector v : take(LIMIT, vs)) {
            Rational y = v.y();
            assertEquals(v.toString(), y, toList(v).get(1));
        }
    }

    private static void propertiesZ() {
        initialize();
        System.out.println("\t\ttesting z() properties");

        Iterable<RationalVector> vs = map(
                p -> RationalVector.of(toList(concat(p.a, p.b))),
                P.pairs(P.rationalVectors(3), P.rationalVectors())
        );
        for (RationalVector v : take(LIMIT, vs)) {
            Rational z = v.z();
            assertEquals(v.toString(), z, toList(v).get(2));
        }
    }

    private static void propertiesW() {
        initialize();
        System.out.println("\t\ttesting w() properties");

        Iterable<RationalVector> vs = map(
                p -> RationalVector.of(toList(concat(p.a, p.b))),
                P.pairs(P.rationalVectors(4), P.rationalVectors())
        );
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
            Rational x = p.a.x(p.b);
            assertEquals(p.toString(), x, toList(p.a).get(p.b));
        }

        Iterable<Pair<RationalVector, Integer>> psFail = filter(
                p -> p.b < 0 || p.b >= p.a.dimension(),
                P.pairs(P.rationalVectors(), P.integers())
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, psFail)) {
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
            RationalVector identity = identity(p.a, p.b);
            validate(identity);
            assertEquals(p.toString(), identity.dimension(), p.a.intValue());
            List<Rational> sortedCoordinates = reverse(sort(identity));
            assertTrue(p.toString(), head(sortedCoordinates) == Rational.ONE);
            assertTrue(p.toString(), all(x -> x == Rational.ZERO, tail(sortedCoordinates)));
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.rangeDown(0), P.integers()))) {
            try {
                identity(p.a, p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.integers()))) {
            try {
                identity(p.a, p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, filter(q -> q.a <= q.b, P.pairs(P.naturalIntegers())))) {
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

        for (RationalVector v : take(LIMIT, filter(w -> any(x -> x != Rational.ZERO, w), P.rationalVectors()))) {
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
            assertEquals(v.toString(), zero(v.dimension()).add(v), v);
            assertEquals(v.toString(), v.add(zero(v.dimension())), v);
            assertTrue(v.add(v.negate()).isZero());
        }

        Iterable<Triple<RationalVector, RationalVector, RationalVector>> ts;
        if (P instanceof ExhaustiveProvider) {
            ts = map(
                    p -> new Triple<>(p.a, p.b.a, p.b.b),
                    P.dependentPairs(P.rationalVectors(), v -> P.pairs(P.rationalVectors(v.dimension())))
            );
        } else {
            ts = map(
                    p -> new Triple<>(p.a, p.b.a, p.b.b),
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

        Iterable<Pair<RationalVector, RationalVector>> psFail = filter(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.rationalVectors())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, psFail)) {
            try {
                p.a.add(p.b);
                fail(p.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesSubtract() {
        initialize();
        System.out.println("\t\ttesting subtract(RationalVector) properties...");

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
            RationalVector difference = p.a.subtract(p.b);
            validate(difference);
            assertEquals(p.toString(), difference, p.b.subtract(p.a).negate());
            assertEquals(p.toString(), p.a, difference.add(p.b));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), zero(v.dimension()).subtract(v), v.negate());
            assertEquals(v.toString(), v.subtract(zero(v.dimension())), v);
            assertTrue(v.toString(), v.subtract(v).isZero());
        }
    }

    private static void propertiesMultiply_Rational() {
        initialize();
        System.out.println("\t\ttesting multiply(Rational) properties...");

        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(), P.rationals()))) {
            RationalVector v = p.a.multiply(p.b);
            validate(v);
            assertEquals(p.toString(), v.dimension(), p.a.dimension());
        }

        Iterable<Pair<RationalVector, Rational>> ps = P.pairs(
                P.rationalVectors(),
                filter(r -> r != Rational.ZERO, P.rationals())
        );
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.multiply(p.b), p.a.divide(p.b.invert()));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), v, v.multiply(Rational.ONE));
            assertTrue(v.toString(), v.multiply(Rational.ZERO).isZero());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(ZERO_DIMENSIONAL.multiply(r) == ZERO_DIMENSIONAL);
        }

        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(1), P.rationals()))) {
            assertEquals(p.toString(), p.a.multiply(p.b), of(p.b).multiply(p.a.x()));
        }

        Iterable<Pair<Rational, Integer>> ps2;
        if (P instanceof ExhaustiveProvider) {
            ps2 = ((ExhaustiveProvider) P).pairsLogarithmicOrder(P.rationals(), P.naturalIntegers());
        } else {
            ps2 = P.pairs(P.rationals(), ((RandomProvider) P).naturalIntegersGeometric(20));
        }
        for (Pair<Rational, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p.toString(), zero(p.b).multiply(p.a).isZero());
        }
    }

    private static void propertiesMultiply_BigInteger() {
        initialize();
        System.out.println("\t\ttesting multiply(BigInteger) properties...");

        for (Pair<RationalVector, BigInteger> p : take(LIMIT, P.pairs(P.rationalVectors(), P.bigIntegers()))) {
            RationalVector v = p.a.multiply(p.b);
            validate(v);
            assertEquals(p.toString(), v.dimension(), p.a.dimension());
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), v, v.multiply(BigInteger.ONE));
            assertTrue(v.toString(), v.multiply(BigInteger.ZERO).isZero());
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertTrue(ZERO_DIMENSIONAL.multiply(i) == ZERO_DIMENSIONAL);
        }

        for (Pair<RationalVector, BigInteger> p : take(LIMIT, P.pairs(P.rationalVectors(1), P.bigIntegers()))) {
            assertEquals(p.toString(), p.a.multiply(p.b), of(Rational.of(p.b)).multiply(p.a.x()));
        }

        Iterable<Pair<BigInteger, Integer>> ps2;
        if (P instanceof ExhaustiveProvider) {
            ps2 = ((ExhaustiveProvider) P).pairsLogarithmicOrder(P.bigIntegers(), P.naturalIntegers());
        } else {
            ps2 = P.pairs(P.bigIntegers(), ((RandomProvider) P).naturalIntegersGeometric(20));
        }
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p.toString(), zero(p.b).multiply(p.a).isZero());
        }
    }

    private static void propertiesMultiply_int() {
        initialize();
        System.out.println("\t\ttesting multiply(int) properties...");

        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), P.integers()))) {
            RationalVector v = p.a.multiply(p.b);
            validate(v);
            assertEquals(p.toString(), v.dimension(), p.a.dimension());
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), v, v.multiply(1));
            assertTrue(v.toString(), v.multiply(0).isZero());
        }

        for (Integer i : take(LIMIT, P.integers())) {
            assertTrue(ZERO_DIMENSIONAL.multiply(i) == ZERO_DIMENSIONAL);
        }

        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(1), P.integers()))) {
            assertEquals(p.toString(), p.a.multiply(p.b), of(Rational.of(p.b)).multiply(p.a.x()));
        }

        Iterable<Pair<Integer, Integer>> ps2;
        if (P instanceof ExhaustiveProvider) {
            ps2 = ((ExhaustiveProvider) P).pairsLogarithmicOrder(P.integers(), P.naturalIntegers());
        } else {
            ps2 = P.pairs(P.integers(), ((RandomProvider) P).naturalIntegersGeometric(20));
        }
        for (Pair<Integer, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p.toString(), zero(p.b).multiply(p.a).isZero());
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
            int compare = p.a.compareTo(p.b);
            assertTrue(p.toString(), compare == -1 || compare == 0 || compare == 1);
            assertEquals(p.toString(), p.b.compareTo(p.a), -compare);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), v.compareTo(v), 0);
        }

        Iterable<Triple<RationalVector, RationalVector, RationalVector>> ts = filter(
                t -> lt(t.a, t.b) && lt(t.b, t.c),
                P.triples(P.rationalVectors())
        );
        for (Triple<RationalVector, RationalVector, RationalVector> t : take(LIMIT, ts)) {
            assertEquals(t.toString(), t.a.compareTo(t.c), -1);
        }

        Iterable<Pair<RationalVector, RationalVector>> ps = filter(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.rationalVectors())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
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
                p -> take(p.a.b, p.a.a) + p.b + drop(p.a.b, p.a.a),
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
