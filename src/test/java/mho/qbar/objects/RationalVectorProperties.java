package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.math.Combinatorics;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.*;

import static mho.qbar.objects.RationalVector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.compare;
import static mho.wheels.ordering.Ordering.lt;
import static mho.wheels.testing.Testing.findInProperties;
import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class RationalVectorProperties {
    private static boolean USE_RANDOM;
    private static final String RATIONAL_VECTOR_CHARS = " ,-/0123456789[]";
    private static int LIMIT;

    private static QBarIterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = QBarRandomProvider.example();
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
            propertiesStandard();
            propertiesIsZero();
            propertiesAdd();
            propertiesNegate();
            propertiesSubtract();
            compareImplementationsSubtract();
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
//            propertiesSum();
//            compareImplementationsSum();
//            propertiesDelta();
            propertiesDot();
            propertiesRightAngleCompare();
            propertiesSquaredLength();
            propertiesCancelDenominators();
            propertiesPivot();
            propertiesReduce();
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

        Iterable<Pair<RationalVector, Integer>> ps = P.dependentPairs(
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

        for (List<Rational> rs : take(LIMIT, P.listsWithElement(null, P.rationals()))) {
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
            assertEquals(r.toString(), v.x(), r);
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

        for (int i : take(LIMIT, P.withScale(20).naturalIntegersGeometric())) {
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

    private static void propertiesStandard() {
        initialize();
        System.out.println("\t\ttesting standard(int, int) properties");

        Iterable<Pair<Integer, Integer>> ps = filter(
                q -> q.a > q.b,
                P.pairs(P.withScale(20).naturalIntegersGeometric())
        );
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            RationalVector standard = standard(p.a, p.b);
            validate(standard);
            assertEquals(p.toString(), standard.dimension(), p.a.intValue());
            List<Rational> sortedCoordinates = reverse(sort(standard));
            assertTrue(p.toString(), head(sortedCoordinates) == Rational.ONE);
            assertTrue(p.toString(), all(x -> x == Rational.ZERO, tail(sortedCoordinates)));
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.rangeDown(0), P.integers()))) {
            try {
                standard(p.a, p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.integers()))) {
            try {
                standard(p.a, p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, filter(q -> q.a <= q.b, P.pairs(P.naturalIntegers())))) {
            try {
                standard(p.a, p.b);
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

    private static void propertiesAdd() {
        initialize();
        System.out.println("\t\ttesting add(RationalVector) properties...");

        Iterable<Pair<RationalVector, RationalVector>> ps = P.dependentPairs(
                P.rationalVectors(),
                v -> P.rationalVectors(v.dimension())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            RationalVector sum = p.a.add(p.b);
            validate(sum);
            assertEquals(p.toString(), sum.dimension(), p.a.dimension());
            assertEquals(p.toString(), sum, p.b.add(p.a));
            assertEquals(p.toString(), sum.subtract(p.b), p.a);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), zero(v.dimension()).add(v), v);
            assertEquals(v.toString(), v.add(zero(v.dimension())), v);
            assertTrue(v.toString(), v.add(v.negate()).isZero());
        }

        Iterable<Triple<RationalVector, RationalVector, RationalVector>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.rationalVectors(),
                        v -> P.pairs(P.rationalVectors(v.dimension()))
                )
        );
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

    private static void propertiesNegate() {
        initialize();
        System.out.println("\t\ttesting negate() properties");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            RationalVector negativeV = v.negate();
            validate(negativeV);
            assertEquals(v.toString(), v.dimension(), negativeV.dimension());
            assertEquals(v.toString(), v, negativeV.negate());
            assertTrue(v.toString(), v.add(negativeV).isZero());
        }

        for (RationalVector v : take(LIMIT, filter(w -> any(x -> x != Rational.ZERO, w), P.rationalVectors()))) {
            RationalVector negativeV = v.negate();
            assertNotEquals(v.toString(), v, negativeV);
        }
    }

    private static @NotNull RationalVector subtract_simplest(@NotNull RationalVector a, @NotNull RationalVector b) {
        return a.add(b.negate());
    }

    private static void propertiesSubtract() {
        initialize();
        System.out.println("\t\ttesting subtract(RationalVector) properties...");

        Iterable<Pair<RationalVector, RationalVector>> ps = P.dependentPairs(
                P.rationalVectors(),
                v -> P.rationalVectors(v.dimension())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            RationalVector difference = p.a.subtract(p.b);
            validate(difference);
            assertEquals(p.toString(), difference.dimension(), p.a.dimension());
            assertEquals(p.toString(), difference, p.b.subtract(p.a).negate());
            assertEquals(p.toString(), p.a, difference.add(p.b));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), zero(v.dimension()).subtract(v), v.negate());
            assertEquals(v.toString(), v.subtract(zero(v.dimension())), v);
            assertTrue(v.toString(), v.subtract(v).isZero());
        }
    }

    private static void compareImplementationsSubtract() {
        initialize();
        System.out.println("\t\tcomparing subtract(RationalVector) implementations...");

        long totalTime = 0;
        Iterable<Pair<RationalVector, RationalVector>> ps = P.dependentPairs(
                P.rationalVectors(),
                v -> P.rationalVectors(v.dimension())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            subtract_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            p.a.subtract(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
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
            assertTrue(r.toString(), ZERO_DIMENSIONAL.multiply(r) == ZERO_DIMENSIONAL);
        }

        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(1), P.rationals()))) {
            assertEquals(p.toString(), p.a.multiply(p.b), of(p.b).multiply(p.a.x()));
        }

        Iterable<Pair<Rational, Integer>> ps2;
        if (P instanceof QBarExhaustiveProvider) {
            ps2 = P.pairsLogarithmicOrder(P.rationals(), P.naturalIntegers());
        } else {
            ps2 = P.pairs(P.rationals(), P.withScale(20).naturalIntegersGeometric());
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
            assertTrue(i.toString(), ZERO_DIMENSIONAL.multiply(i) == ZERO_DIMENSIONAL);
        }

        for (Pair<RationalVector, BigInteger> p : take(LIMIT, P.pairs(P.rationalVectors(1), P.bigIntegers()))) {
            assertEquals(p.toString(), p.a.multiply(p.b), of(Rational.of(p.b)).multiply(p.a.x()));
        }

        Iterable<Pair<BigInteger, Integer>> ps2;
        if (P instanceof QBarExhaustiveProvider) {
            ps2 = P.pairsLogarithmicOrder(P.bigIntegers(), P.naturalIntegers());
        } else {
            ps2 = P.pairs(P.bigIntegers(), P.withScale(20).naturalIntegersGeometric());
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
            assertTrue(Integer.toString(i), ZERO_DIMENSIONAL.multiply(i) == ZERO_DIMENSIONAL);
        }

        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(1), P.integers()))) {
            assertEquals(p.toString(), p.a.multiply(p.b), of(Rational.of(p.b)).multiply(p.a.x()));
        }

        Iterable<Pair<Integer, Integer>> ps2;
        if (P instanceof QBarExhaustiveProvider) {
            ps2 = P.pairsLogarithmicOrder(P.integers(), P.naturalIntegers());
        } else {
            ps2 = P.pairs(P.integers(), P.withScale(20).naturalIntegersGeometric());
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
            assertTrue(r.toString(), ZERO_DIMENSIONAL.divide(r) == ZERO_DIMENSIONAL);
        }

        ps = P.pairs(
                filter(v -> v.x() != Rational.ZERO, P.rationalVectors(1)),
                filter(r -> r != Rational.ZERO, P.rationals())
        );
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.divide(p.b).x(), of(p.b).divide(p.a.x()).x().invert());
        }

        Iterable<Pair<Rational, Integer>> ps2;
        if (P instanceof QBarExhaustiveProvider) {
            ps2 = P.pairsLogarithmicOrder(
                    filter(r -> r != Rational.ZERO, P.rationals()),
                    P.naturalIntegers()
            );
        } else {
            ps2 = P.pairs(
                    filter(r -> r != Rational.ZERO, P.rationals()),
                    P.withScale(20).naturalIntegersGeometric()
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
            assertTrue(i.toString(), ZERO_DIMENSIONAL.divide(i) == ZERO_DIMENSIONAL);
        }

        ps = P.pairs(
                filter(v -> v.x() != Rational.ZERO, P.rationalVectors(1)),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.divide(p.b).x(), of(Rational.of(p.b)).divide(p.a.x()).x().invert());
        }

        Iterable<Pair<BigInteger, Integer>> ps2;
        if (P instanceof QBarExhaustiveProvider) {
            ps2 = P.pairsLogarithmicOrder(
                    filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers()),
                    P.naturalIntegers()
            );
        } else {
            ps2 = P.pairs(
                    filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers()),
                    P.withScale(20).naturalIntegersGeometric()
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
            assertTrue(Integer.toString(i), ZERO_DIMENSIONAL.divide(i) == ZERO_DIMENSIONAL);
        }

        ps = P.pairs(
                filter(v -> v.x() != Rational.ZERO, P.rationalVectors(1)),
                filter(i -> i != 0, P.integers())
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.divide(p.b).x(), of(Rational.of(p.b)).divide(p.a.x()).x().invert());
        }

        Iterable<Pair<Integer, Integer>> ps2;
        if (P instanceof QBarExhaustiveProvider) {
            ps2 = P.pairsLogarithmicOrder(
                    filter(i -> i != 0, P.integers()),
                    P.naturalIntegers()
            );
        } else {
            ps2 = P.pairs(filter(i -> i != 0, P.integers()), P.withScale(20).naturalIntegersGeometric());
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
            is = P.integersGeometric();
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
            is = P.naturalIntegersGeometric();
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
            is  = P.integersGeometric();
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
            is  = P.integersGeometric();
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
            is  = P.naturalIntegersGeometric();
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
            is  = P.integersGeometric();
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
        return foldl1(RationalVector::add, xs);
    }

//    private static void propertiesSum() {
//        initialize();
//        System.out.println("\t\ttesting sum(Iterable<RationalVector>) properties...");
//
//        Iterable<Pair<Integer, Integer>> ps0;
//        if (P instanceof QBarExhaustiveProvider) {
//            ps0 = P.pairs(P.positiveIntegers(), P.naturalIntegers());
//        } else {
//            ps0 = P.pairs(
//                    P.withScale(5).positiveIntegersGeometric(),
//                    P.withScale(3).naturalIntegersGeometric()
//            );
//        }
//        Iterable<List<RationalVector>> vss = map(
//                q -> q.b,
//                P.dependentPairs(ps0, p -> P.lists(p.a, P.rationalVectors(p.b)))
//        );
//        for (List<RationalVector> vs : take(LIMIT, vss)) {
//            RationalVector sum = sum(vs);
//            validate(sum);
//            assertEquals(vs.toString(), sum, sum_simplest(vs));
//            assertEquals(vs.toString(), sum.dimension(), head(vs).dimension());
//        }
//
//        Iterable<Pair<List<RationalVector>, List<RationalVector>>> ps = filter(
//                q -> !q.a.equals(q.b),
//                P.dependentPairs(vss, P::permutations)
//        );
//        for (Pair<List<RationalVector>, List<RationalVector>> p : take(LIMIT, ps)) {
//            assertEquals(p.toString(), sum(p.a), sum(p.b));
//        }
//
//        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
//            assertEquals(v.toString(), sum(Collections.singletonList(v)), v);
//        }
//
//        Iterable<Pair<RationalVector, RationalVector>> ps2 = filter(
//                q -> q.a.dimension() == q.b.dimension(),
//                P.pairs(P.rationalVectors())
//        );
//        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps2)) {
//            assertEquals(p.toString(), sum(Arrays.asList(p.a, p.b)), p.a.add(p.b));
//        }
//
//        Iterable<List<RationalVector>> failVss = map(
//                p -> toList(insert(p.a, p.b, null)),
//                (Iterable<Pair<List<RationalVector>, Integer>>) P.dependentPairs(
//                        vss,
//                        rs -> range(0, rs.size())
//                )
//        );
//        for (List<RationalVector> vs : take(LIMIT, failVss)) {
//            try {
//                sum(vs);
//                fail(vs.toString());
//            } catch (NullPointerException ignored) {}
//        }
//    }

//    private static void compareImplementationsSum() {
//        initialize();
//        System.out.println("\t\tcomparing sum(Iterable<RationalVector>) implementations...");
//
//        long totalTime = 0;
//        Iterable<Pair<Integer, Integer>> ps;
//        if (P instanceof QBarExhaustiveProvider) {
//            ps = P.pairs(P.positiveIntegers(), P.naturalIntegers());
//        } else {
//            ps = P.pairs(
//                    P.withScale(5).positiveIntegersGeometric(),
//                    P.withScale(3).naturalIntegersGeometric()
//            );
//        }
//        Iterable<List<RationalVector>> vss = map(
//                q -> q.b,
//                P.dependentPairsSquare(ps, p -> P.lists(p.a, P.rationalVectors(p.b)))
//        );
//        for (List<RationalVector> vs : take(LIMIT, vss)) {
//            long time = System.nanoTime();
//            sum_simplest(vs);
//            totalTime += (System.nanoTime() - time);
//        }
//        System.out.println("\t\t\talt: " + ((double) totalTime) / 1e9 + " s");
//
//        totalTime = 0;
//        for (List<RationalVector> vs : take(LIMIT, vss)) {
//            long time = System.nanoTime();
//            sum(vs);
//            totalTime += (System.nanoTime() - time);
//        }
//        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
//    }
//
//    private static void propertiesDelta() {
//        initialize();
//        System.out.println("\t\ttesting delta(Iterable<RationalVector>) properties...");
//
//        Iterable<Pair<Integer, Integer>> ps0;
//        if (P instanceof QBarExhaustiveProvider) {
//            ps0 = P.pairs(P.positiveIntegers(), P.naturalIntegers());
//        } else {
//            ps0 = P.pairs(
//                    P.withScale(5).positiveIntegersGeometric(),
//                    P.withScale(3).naturalIntegersGeometric()
//            );
//        }
//        Iterable<List<RationalVector>> vss = map(
//                q -> q.b,
//                P.dependentPairsSquare(ps0, p -> P.lists(p.a, P.rationalVectors(p.b)))
//        );
//        for (List<RationalVector> vs : take(LIMIT, vss)) {
//            Iterable<RationalVector> deltas = delta(vs);
//            deltas.forEach(mho.qbar.objects.RationalVectorProperties::validate);
//            assertTrue(vs.toString(), all(v -> v.dimension() == head(vs).dimension(), deltas));
//            assertEquals(vs.toString(), length(deltas), length(vs) - 1);
//            List<RationalVector> reversed = reverse(map(RationalVector::negate, delta(reverse(vs))));
//            aeq(vs.toString(), deltas, reversed);
//            try {
//                deltas.iterator().remove();
//            } catch (UnsupportedOperationException ignored) {}
//        }
//
//        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
//            assertTrue(v.toString(), isEmpty(delta(Collections.singletonList(v))));
//        }
//
//        Iterable<Pair<RationalVector, RationalVector>> ps = filter(
//                q -> q.a.dimension() == q.b.dimension(),
//                P.pairs(P.rationalVectors())
//        );
//        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
//            aeq(p.toString(), delta(Arrays.asList(p.a, p.b)), Collections.singletonList(p.b.subtract(p.a)));
//        }
//
//        Iterable<List<RationalVector>> failVss = map(
//                p -> toList(insert(p.a, p.b, null)),
//                (Iterable<Pair<List<RationalVector>, Integer>>) P.dependentPairsLogarithmic(
//                        vss,
//                        rs -> range(0, rs.size())
//                )
//        );
//        for (List<RationalVector> vs : take(LIMIT, failVss)) {
//            try {
//                toList(delta(vs));
//                fail(vs.toString());
//            } catch (NullPointerException ignored) {}
//        }
//    }

    private static void propertiesDot() {
        initialize();
        System.out.println("\t\ttesting dot(Rational) properties...");

        Iterable<Pair<RationalVector, RationalVector>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.dependentPairs(P.rationalVectors(), v -> P.rationalVectors(v.dimension()));
        } else {
            ps = P.dependentPairs(
                    P.withScale(8).rationalVectors(),
                    v -> P.withScale(8).rationalVectors(v.dimension())
            );
        }
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            Rational dot = p.a.dot(p.b);
            assertEquals(p.toString(), dot, p.b.dot(p.a));
            assertEquals(p.toString(), p.a.negate().dot(p.b), dot.negate());
        }

        Iterable<Triple<Rational, RationalVector, RationalVector>> ts = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairs(ps, P.rationals())
        );
        for (Triple<Rational, RationalVector, RationalVector> t : take(LIMIT, ts)) {
            assertEquals(t.toString(), t.b.dot(t.c).multiply(t.a), t.b.multiply(t.a).dot(t.c));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), v.dot(zero(v.dimension())), Rational.ZERO);
            for (int i = 0; i < v.dimension(); i++) {
                assertEquals(v.toString(), v.dot(standard(v.dimension(), i)), v.x(i));
            }
        }

        Iterable<Pair<RationalVector, RationalVector>> psFail = filter(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.rationalVectors())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, psFail)) {
            try {
                p.a.dot(p.b);
                fail(p.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesRightAngleCompare() {
        initialize();
        System.out.println("\t\ttesting rightAngleCompare(RationalVector) properties...");

        Iterable<Pair<RationalVector, RationalVector>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.dependentPairs(P.rationalVectors(), v -> P.rationalVectors(v.dimension()));
        } else {
            ps = P.dependentPairs(
                    P.withScale(8).rationalVectors(),
                    v -> P.withScale(8).rationalVectors(v.dimension())
            );
        }
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            Ordering rightAngleCompare = p.a.rightAngleCompare(p.b);
            assertEquals(p.toString(), rightAngleCompare, p.b.rightAngleCompare(p.a));
            assertEquals(p.toString(), p.a.negate().rightAngleCompare(p.b), rightAngleCompare.invert());
        }

        Iterable<Triple<Rational, RationalVector, RationalVector>> ts = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairs(ps, P.positiveRationals())
        );
        for (Triple<Rational, RationalVector, RationalVector> t : take(LIMIT, ts)) {
            assertEquals(t.toString(), t.b.multiply(t.a).rightAngleCompare(t.c), t.b.rightAngleCompare(t.c));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v.toString(), v.dot(zero(v.dimension())), Rational.ZERO);
            for (int i = 0; i < v.dimension(); i++) {
                assertEquals(
                        v.toString(),
                        v.rightAngleCompare(standard(v.dimension(), i)),
                        compare(v.x(i), Rational.ZERO).invert()
                );
            }
        }

        Iterable<Pair<RationalVector, RationalVector>> psFail = filter(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.rationalVectors())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, psFail)) {
            try {
                p.a.rightAngleCompare(p.b);
                fail(p.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesSquaredLength() {
        initialize();
        System.out.println("\t\ttesting squaredLength() properties...");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            Rational squaredLength = v.squaredLength();
            assertEquals(v.toString(), squaredLength, v.dot(v));
            assertNotEquals(v.toString(), squaredLength.signum(), -1);
            assertEquals(v.toString(), v.negate().squaredLength(), squaredLength);
        }

        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(), P.rationals()))) {
            assertEquals(p.toString(), p.a.multiply(p.b).squaredLength(), p.a.squaredLength().multiply(p.b.pow(2)));
        }

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = P.withScale(20).naturalIntegersGeometric();
        }
        for (int i : take(LIMIT, is)) {
            assertEquals(Integer.toString(i), zero(i).squaredLength(), Rational.ZERO);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, filter(q -> q.a > q.b, P.pairs(is)))) {
            assertEquals(p.toString(), standard(p.a, p.b).squaredLength(), Rational.ONE);
        }
    }

    private static void propertiesCancelDenominators() {
        initialize();
        System.out.println("\t\ttesting cancelDenominators() properties...");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            RationalVector canceled = v.cancelDenominators();
            validate(canceled);

            assertTrue(v.toString(), all(r -> r.getDenominator().equals(BigInteger.ONE), canceled));
            BigInteger gcd = foldl((x, y) -> x.gcd(y.getNumerator()), BigInteger.ZERO, canceled);
            assertTrue(v.toString(), gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE));
            assertEquals(v.toString(), canceled.cancelDenominators(), canceled);
            assertEquals(v.toString(), canceled.dimension(), v.dimension());
            assertTrue(v.toString(), equal(map(Rational::signum, v), map(Rational::signum, canceled)));
            assertTrue(
                    v.toString(),
                    same(
                            zipWith(
                                    Rational::divide,
                                    filter(r -> r != Rational.ZERO, v),
                                    filter(r -> r != Rational.ZERO, canceled)
                            )
                    )
            );
        }

        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(), P.positiveRationals()))) {
            assertEquals(p.toString(), p.a.cancelDenominators(), p.a.multiply(p.b).cancelDenominators());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            Rational canceled = head(of(r).cancelDenominators()).abs();
            assertTrue(r.toString(), canceled == Rational.ZERO || canceled == Rational.ONE);
        }

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = P.withScale(20).naturalIntegersGeometric();
        }
        for (int i : take(LIMIT, is)) {
            RationalVector zero = zero(i);
            assertEquals(Integer.toString(i), zero.cancelDenominators(), zero);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, filter(q -> q.a > q.b, P.pairs(is)))) {
            RationalVector standard = standard(p.a, p.b);
            assertEquals(p.toString(), standard.cancelDenominators(), standard);
        }
    }

    private static void propertiesPivot() {
        initialize();
        System.out.println("\t\ttesting pivot() properties...");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            v.pivot();
        }

        for (RationalVector v : take(LIMIT, filter(w -> !w.isZero(), P.rationalVectors()))) {
            Rational pivot = v.pivot().get();
            assertTrue(v.toString(), pivot != Rational.ZERO);
            assertTrue(v.toString(), elem(pivot, v));
        }

        Iterable<Pair<RationalVector, Rational>> ps = P.pairs(
                P.rationalVectors(),
                filter(r -> r != Rational.ZERO, P.rationals())
        );
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.multiply(p.b).pivot(), p.a.pivot().map(r -> r.multiply(p.b)));
        }
    }

    private static void propertiesReduce() {
        initialize();
        System.out.println("\t\ttesting reduce() properties...");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            RationalVector reduced = v.reduce();
            validate(reduced);

            Optional<Rational> pivot = reduced.pivot();
            assertTrue(v.toString(), !pivot.isPresent() || pivot.get() == Rational.ONE);
            assertEquals(v.toString(), reduced.reduce(), reduced);
            assertEquals(v.toString(), reduced.dimension(), v.dimension());
            pivot = v.pivot();
            RationalVector abs = !pivot.isPresent() || pivot.get().signum() != -1 ? v : v.negate();
            assertTrue(v.toString(), equal(map(Rational::signum, abs), map(Rational::signum, reduced)));
            assertTrue(
                    v.toString(),
                    same(
                            zipWith(
                                    Rational::divide,
                                    filter(r -> r != Rational.ZERO, v),
                                    filter(r -> r != Rational.ZERO, reduced)
                            )
                    )
            );
        }

        Iterable<Pair<RationalVector, Rational>> ps = P.pairs(
                P.rationalVectors(),
                filter(r -> r != Rational.ZERO, P.rationals())
        );
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.reduce(), p.a.multiply(p.b).reduce());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            Rational reduced = head(of(r).reduce());
            assertTrue(r.toString(), reduced == Rational.ZERO || reduced == Rational.ONE);
        }

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = P.withScale(20).naturalIntegersGeometric();
        }
        for (int i : take(LIMIT, is)) {
            RationalVector zero = zero(i);
            assertEquals(Integer.toString(i), zero.reduce(), zero);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, filter(q -> q.a > q.b, P.pairs(is)))) {
            RationalVector standard = standard(p.a, p.b);
            assertEquals(p.toString(), standard.reduce(), standard);
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

        findInProperties(
                LIMIT,
                P.getWheelsProvider(),
                P.rationalVectors(),
                RationalVector::read,
                RationalVector::findIn
        );
    }

    private static void propertiesToString() {
        initialize();
        System.out.println("\t\ttesting toString() properties...");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            String s = v.toString();
            assertTrue(v.toString(), isSubsetOf(s, RATIONAL_VECTOR_CHARS));
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
