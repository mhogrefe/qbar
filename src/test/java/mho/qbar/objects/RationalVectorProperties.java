package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.math.Combinatorics;
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
            propertiesDivide_Rational();
            propertiesDivide_BigInteger();
            propertiesDivide_int();
            propertiesShiftLeft();
            compareImplementationsShiftLeft();
            propertiesShiftRight();
            compareImplementationsShiftRight();
            propertiesSum();
            compareImplementationsSum();
            propertiesDelta();
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
            assertEquals(p.toString(), sum.subtract(p.b), p.a);
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
            assertEquals(p.toString(), p.a.multiply(p.b).divide(p.b), p.a);
        }

        ps = P.pairs(P.rationalVectors(), filter(r -> r != Rational.ZERO, P.rationals()));
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

        Iterable<Pair<RationalVector, BigInteger>> ps = P.pairs(
                P.rationalVectors(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.multiply(p.b).divide(p.b), p.a);
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

        Iterable<Pair<RationalVector, Integer>> ps = P.pairs(P.rationalVectors(), filter(i -> i != 0, P.integers()));
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.multiply(p.b).divide(p.b), p.a);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), v, v.multiply(1));
            assertTrue(v.toString(), v.multiply(0).isZero());
        }

        for (int i : take(LIMIT, P.integers())) {
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

    private static void propertiesDivide_Rational() {
        initialize();
        System.out.println("\t\ttesting divide(Rational) properties...");

        Iterable<Pair<RationalVector, Rational>> ps = filter(
                p -> p.b != Rational.ZERO,
                P.pairs(P.rationalVectors(), P.rationals())
        );
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            RationalVector v = p.a.divide(p.b);
            validate(v);
            assertEquals(p.toString(), v.dimension(), p.a.dimension());
            assertEquals(p.toString(), p.a, v.multiply(p.b));
            assertEquals(p.toString(), v, p.a.multiply(p.b.invert()));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), v.divide(Rational.ONE), v);
        }

        for (Rational r : take(LIMIT, filter(s -> s != Rational.ZERO, P.rationals()))) {
            assertTrue(ZERO_DIMENSIONAL.divide(r) == ZERO_DIMENSIONAL);
        }

        ps = P.pairs(
                filter(v -> v.x() != Rational.ZERO, P.rationalVectors(1)),
                filter(r -> r != Rational.ZERO, P.rationals())
        );
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.divide(p.b).x(), of(p.b).divide(p.a.x()).x().invert());
        }

        Iterable<Pair<Rational, Integer>> ps2;
        if (P instanceof ExhaustiveProvider) {
            ps2 = ((ExhaustiveProvider) P).pairsLogarithmicOrder(
                    filter(r -> r != Rational.ZERO, P.rationals()),
                    P.naturalIntegers()
            );
        } else {
            ps2 = P.pairs(
                    filter(r -> r != Rational.ZERO, P.rationals()),
                    ((RandomProvider) P).naturalIntegersGeometric(20)
            );
        }
        for (Pair<Rational, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p.toString(), zero(p.b).divide(p.a).isZero());
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            try {
                v.divide(Rational.ZERO);
                fail(v.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesDivide_BigInteger() {
        initialize();
        System.out.println("\t\ttesting divide(BigInteger) properties...");

        Iterable<Pair<RationalVector, BigInteger>> ps = filter(
                p -> !p.b.equals(BigInteger.ZERO),
                P.pairs(P.rationalVectors(), P.bigIntegers())
        );
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, ps)) {
            RationalVector v = p.a.divide(p.b);
            validate(v);
            assertEquals(p.toString(), v.dimension(), p.a.dimension());
            assertEquals(p.toString(), p.a, v.multiply(p.b));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), v.divide(BigInteger.ONE), v);
        }

        for (BigInteger i : take(LIMIT, filter(j -> !j.equals(BigInteger.ZERO), P.bigIntegers()))) {
            assertTrue(ZERO_DIMENSIONAL.divide(i) == ZERO_DIMENSIONAL);
        }

        ps = P.pairs(
                filter(v -> v.x() != Rational.ZERO, P.rationalVectors(1)),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.divide(p.b).x(), of(Rational.of(p.b)).divide(p.a.x()).x().invert());
        }

        Iterable<Pair<BigInteger, Integer>> ps2;
        if (P instanceof ExhaustiveProvider) {
            ps2 = ((ExhaustiveProvider) P).pairsLogarithmicOrder(
                    filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers()),
                    P.naturalIntegers()
            );
        } else {
            ps2 = P.pairs(
                    filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers()),
                    ((RandomProvider) P).naturalIntegersGeometric(20)
            );
        }
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p.toString(), zero(p.b).divide(p.a).isZero());
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            try {
                v.divide(BigInteger.ZERO);
                fail(v.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesDivide_int() {
        initialize();
        System.out.println("\t\ttesting divide(int) properties...");

        Iterable<Pair<RationalVector, Integer>> ps = filter(
                p -> !p.b.equals(0),
                P.pairs(P.rationalVectors(), P.integers())
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            RationalVector v = p.a.divide(p.b);
            validate(v);
            assertEquals(p.toString(), v.dimension(), p.a.dimension());
            assertEquals(p.toString(), p.a, v.multiply(p.b));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), v.divide(1), v);
        }

        for (int i : take(LIMIT, filter(j -> j != 0, P.integers()))) {
            assertTrue(ZERO_DIMENSIONAL.divide(i) == ZERO_DIMENSIONAL);
        }

        ps = P.pairs(
                filter(v -> v.x() != Rational.ZERO, P.rationalVectors(1)),
                filter(i -> i != 0, P.integers())
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.divide(p.b).x(), of(Rational.of(p.b)).divide(p.a.x()).x().invert());
        }

        Iterable<Pair<Integer, Integer>> ps2;
        if (P instanceof ExhaustiveProvider) {
            ps2 = ((ExhaustiveProvider) P).pairsLogarithmicOrder(
                    filter(i -> i != 0, P.integers()),
                    P.naturalIntegers()
            );
        } else {
            ps2 = P.pairs(filter(i -> i != 0, P.integers()), ((RandomProvider) P).naturalIntegersGeometric(20));
        }
        for (Pair<Integer, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p.toString(), zero(p.b).divide(p.a).isZero());
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            try {
                v.divide(0);
                fail(v.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull RationalVector shiftLeft_simplest(@NotNull RationalVector v, int bits) {
        if (bits < 0) {
            return v.divide(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return v.multiply(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private static void propertiesShiftLeft() {
        initialize();
        System.out.println("\t\ttesting shiftLeft(int) properties...");

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric(50);
        }
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), is))) {
            RationalVector shifted = p.a.shiftLeft(p.b);
            validate(shifted);
            assertEquals(p.toString(), shifted, shiftLeft_simplest(p.a, p.b));
            aeq(p.toString(), map(Rational::signum, p.a), map(Rational::signum, shifted));
            assertEquals(p.toString(), p.a.dimension(), shifted.dimension());
            assertEquals(p.toString(), p.a.negate().shiftLeft(p.b), shifted.negate());
            assertEquals(p.toString(), shifted, p.a.shiftRight(-p.b));
        }

        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = ((QBarRandomProvider) P).naturalIntegersGeometric(50);
        }
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), is))) {
            RationalVector shifted = p.a.shiftLeft(p.b);
            assertEquals(p.toString(), shifted, p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private static void compareImplementationsShiftLeft() {
        initialize();
        System.out.println("\t\tcomparing shiftLeft(int) implementations...");

        long totalTime = 0;
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric(50);
        }
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), is))) {
            long time = System.nanoTime();
            shiftLeft_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), is))) {
            long time = System.nanoTime();
            p.a.shiftLeft(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull RationalVector shiftRight_simplest(@NotNull RationalVector v, int bits) {
        if (bits < 0) {
            return v.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return v.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private static void propertiesShiftRight() {
        initialize();
        System.out.println("\t\ttesting shiftRight(int) properties...");

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric(50);
        }
        Iterable<Pair<RationalVector, Integer>> ps = P.pairs(P.rationalVectors(), is);
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            RationalVector shifted = p.a.shiftRight(p.b);
            validate(shifted);
            assertEquals(p.toString(), shifted, shiftRight_simplest(p.a, p.b));
            aeq(p.toString(), map(Rational::signum, p.a), map(Rational::signum, shifted));
            assertEquals(p.toString(), p.a.dimension(), shifted.dimension());
            assertEquals(p.toString(), p.a.negate().shiftRight(p.b), shifted.negate());
            assertEquals(p.toString(), shifted, p.a.shiftLeft(-p.b));
        }

        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = ((QBarRandomProvider) P).naturalIntegersGeometric(50);
        }
        ps = P.pairs(P.rationalVectors(), is);
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            RationalVector shifted = p.a.shiftRight(p.b);
            assertEquals(p.toString(), shifted, p.a.divide(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private static void compareImplementationsShiftRight() {
        initialize();
        System.out.println("\t\tcomparing shiftRight(int) implementations...");

        long totalTime = 0;
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric(50);
        }
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), is))) {
            long time = System.nanoTime();
            shiftRight_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), is))) {
            long time = System.nanoTime();
            p.a.shiftRight(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull RationalVector sum_simplest(@NotNull Iterable<RationalVector> xs) {
        return foldl1(p -> p.a.add(p.b), xs);
    }

    private static void propertiesSum() {
        initialize();
        System.out.println("\t\ttesting sum(Iterable<RationalVector>) properties...");

        Iterable<Pair<Integer, Integer>> ps0;
        if (P instanceof ExhaustiveProvider) {
            ps0 = P.pairs(P.positiveIntegers(), P.naturalIntegers());
        } else {
            ps0 = P.pairs(
                    ((RandomProvider) P).positiveIntegersGeometric(5),
                    ((RandomProvider) P).naturalIntegersGeometric(5)
            );
        }
        Iterable<List<RationalVector>> vss = map(
                q -> q.b,
                P.dependentPairsSquare(ps0, p -> P.lists(p.a, P.rationalVectors(p.b)))
        );
        for (List<RationalVector> vs : take(LIMIT, vss)) {
            RationalVector sum = sum(vs);
            validate(sum);
            assertEquals(vs.toString(), sum, sum_simplest(vs));
            assertEquals(vs.toString(), sum.dimension(), head(vs).dimension());
        }

        Iterable<Pair<List<RationalVector>, List<RationalVector>>> ps = filter(
                q -> !q.a.equals(q.b),
                P.dependentPairsLogarithmic(vss, Combinatorics::permutationsIncreasing)
        );

        for (Pair<List<RationalVector>, List<RationalVector>> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), sum(p.a), sum(p.b));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), sum(Arrays.asList(v)), v);
        }

        Iterable<Pair<RationalVector, RationalVector>> ps2 = filter(
                q -> q.a.dimension() == q.b.dimension(),
                P.pairs(P.rationalVectors())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps2)) {
            assertEquals(p.toString(), sum(Arrays.asList(p.a, p.b)), p.a.add(p.b));
        }

        Iterable<List<RationalVector>> failVss = map(
                p -> toList(insert(p.a, p.b, null)),
                (Iterable<Pair<List<RationalVector>, Integer>>) P.dependentPairsLogarithmic(
                        vss,
                        rs -> range(0, rs.size())
                )
        );
        for (List<RationalVector> vs : take(LIMIT, failVss)) {
            try {
                sum(vs);
                fail(vs.toString());
            } catch (NullPointerException ignored) {}
        }
    }

    private static void compareImplementationsSum() {
        initialize();
        System.out.println("\t\tcomparing sum(Iterable<RationalVector>) implementations...");

        long totalTime = 0;
        Iterable<Pair<Integer, Integer>> ps;
        if (P instanceof ExhaustiveProvider) {
            ps = P.pairs(P.positiveIntegers(), P.naturalIntegers());
        } else {
            ps = P.pairs(
                    ((RandomProvider) P).positiveIntegersGeometric(5),
                    ((RandomProvider) P).naturalIntegersGeometric(5)
            );
        }
        Iterable<List<RationalVector>> vss = map(
                q -> q.b,
                P.dependentPairsSquare(ps, p -> P.lists(p.a, P.rationalVectors(p.b)))
        );
        for (List<RationalVector> vs : take(LIMIT, vss)) {
            long time = System.nanoTime();
            sum_simplest(vs);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\talt: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (List<RationalVector> vs : take(LIMIT, vss)) {
            long time = System.nanoTime();
            sum(vs);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static void propertiesDelta() {
        initialize();
        System.out.println("\t\ttesting delta(Iterable<RationalVector>) properties...");

        Iterable<Pair<Integer, Integer>> ps0;
        if (P instanceof ExhaustiveProvider) {
            ps0 = P.pairs(P.positiveIntegers(), P.naturalIntegers());
        } else {
            ps0 = P.pairs(
                    ((RandomProvider) P).positiveIntegersGeometric(5),
                    ((RandomProvider) P).naturalIntegersGeometric(5)
            );
        }
        Iterable<List<RationalVector>> vss = map(
                q -> q.b,
                P.dependentPairsSquare(ps0, p -> P.lists(p.a, P.rationalVectors(p.b)))
        );
        for (List<RationalVector> vs : take(LIMIT, vss)) {
            Iterable<RationalVector> deltas = delta(vs);
            deltas.forEach(mho.qbar.objects.RationalVectorProperties::validate);
            assertTrue(vs.toString(), all(v -> v.dimension() == head(vs).dimension(), deltas));
            assertEquals(vs.toString(), length(deltas), length(vs) - 1);
            List<RationalVector> reversed = reverse(map(RationalVector::negate, delta(reverse(vs))));
            aeq(vs.toString(), deltas, reversed);
            try {
                deltas.iterator().remove();
            } catch (UnsupportedOperationException ignored) {}
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertTrue(v.toString(), isEmpty(delta(Arrays.asList(v))));
        }

        Iterable<Pair<RationalVector, RationalVector>> ps = filter(
                q -> q.a.dimension() == q.b.dimension(),
                P.pairs(P.rationalVectors())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            aeq(p.toString(), delta(Arrays.asList(p.a, p.b)), Arrays.asList(p.b.subtract(p.a)));
        }

        Iterable<List<RationalVector>> failVss = map(
                p -> toList(insert(p.a, p.b, null)),
                (Iterable<Pair<List<RationalVector>, Integer>>) P.dependentPairsLogarithmic(
                        vss,
                        rs -> range(0, rs.size())
                )
        );
        for (List<RationalVector> vs : take(LIMIT, failVss)) {
            try {
                toList(delta(vs));
                fail(vs.toString());
            } catch (NullPointerException ignored) {}
        }
    }

    private static void propertiesDot() {
        Iterable<Pair<RationalVector, RationalVector>> ps;
        if (P instanceof ExhaustiveProvider) {
            ps = P.dependentPairs(P.rationalVectors(), v -> P.rationalVectors(v.dimension()));
        } else {
            ps = P.dependentPairs(
                    ((QBarRandomProvider) P).rationalVectorsBySize(8),
                    v -> ((QBarRandomProvider) P).rationalVectorsBySize(8, v.dimension())
            );
        }
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            Rational dot = p.a.dot(p.b);
            assertEquals(p.toString(), dot, p.b.dot(p.a));
            assertEquals(p.toString(), p.a.negate().dot(p.b), dot.negate());
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

    private static <T> void aeq(String message, Iterable<T> xs, Iterable<T> ys) {
        assertTrue(message, equal(xs, ys));
    }
}
