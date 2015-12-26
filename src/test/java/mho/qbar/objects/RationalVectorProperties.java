package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.RationalVector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.compare;
import static mho.wheels.testing.Testing.*;
import static mho.wheels.testing.Testing.propertiesFindInHelper;

public class RationalVectorProperties extends QBarTestProperties {
    private static final @NotNull String RATIONAL_VECTOR_CHARS = " ,-/0123456789[]";

    public RationalVectorProperties() {
        super("RationalVector");
    }

    @Override
    protected void testBothModes() {
        propertiesIterator();
        propertiesGet();
        propertiesOf_List_Rational();
        propertiesOf_Rational();
        propertiesDimension();
        propertiesIsZero();
        propertiesZero();
        propertiesStandard();
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
//        propertiesSum();
//        compareImplementationsSum();
//        propertiesDelta();
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

    private void propertiesIterator() {
        initialize("iterator()");
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            List<Rational> rs = toList(v);
            assertTrue(v, all(r -> r != null, rs));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<Rational> ss) -> of(ss), v);
            try {
                v.iterator().remove();
                fail(v);
            } catch (UnsupportedOperationException ignored) {}
        }
    }

    private void propertiesGet() {
        initialize("get(int)");
        Iterable<Pair<RationalVector, Integer>> ps = P.dependentPairs(
                P.rationalVectorsAtLeast(1),
                v -> P.uniformSample(toList(range(0, v.dimension() - 1)))
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            Rational x = p.a.get(p.b);
            assertEquals(p, x, toList(p.a).get(p.b));
        }

        Iterable<Pair<RationalVector, Integer>> psFail = filterInfinite(
                p -> p.b < 0 || p.b >= p.a.dimension(),
                P.pairs(P.rationalVectors(), P.integers())
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.get(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesOf_List_Rational() {
        initialize("of(List<Rational>)");
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            RationalVector v = of(rs);
            v.validate();
            //noinspection Convert2MethodRef
            inverse(RationalVector::of, (RationalVector u) -> toList(u), rs);
            assertEquals(rs, v.dimension(), rs.size());
        }

        for (List<Rational> rs : take(LIMIT, P.listsWithElement(null, P.rationals()))) {
            try {
                of(rs);
                fail(rs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesOf_Rational() {
        initialize("of(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            RationalVector v = of(r);
            v.validate();
            assertEquals(r, v.dimension(), 1);
            assertEquals(r, v.get(0), r);
            inverse(RationalVector::of, (RationalVector u) -> u.get(0), r);
        }
    }

    private void propertiesDimension() {
        initialize("dimension()");
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            int dimension = v.dimension();
            assertTrue(v, dimension >= 0);
            assertEquals(v, length(v), dimension);
        }
    }

    private void propertiesIsZero() {
        initialize("isZero()");
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v.isZero(), zero(v.dimension()).equals(v));
        }
    }

    private void propertiesZero() {
        initialize("zero(int)");
        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            RationalVector zero = zero(i);
            zero.validate();
            assertEquals(i, zero.dimension(), i);
            inverse(RationalVector::zero, RationalVector::dimension, i);
            assertTrue(i, zero.isZero());
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                zero(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesStandard() {
        initialize("standard(int, int)");
        for (Pair<Integer, Integer> p : take(LIMIT, P.subsetPairs(P.naturalIntegersGeometric()))) {
            RationalVector standard = standard(p.b, p.a);
            standard.validate();
            assertEquals(p, standard.dimension(), p.b);
            for (int i = 0; i < p.b; i++) {
                assertTrue(p, standard.get(i) == (i == p.a ? Rational.ONE : Rational.ZERO));
            }
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.rangeDown(0), P.integers()))) {
            try {
                standard(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.integers()))) {
            try {
                standard(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.bagPairs(P.naturalIntegers()))) {
            try {
                standard(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAdd() {
        initialize("");
        System.out.println("\t\ttesting add(RationalVector) properties...");

        Iterable<Pair<RationalVector, RationalVector>> ps = P.dependentPairs(
                P.rationalVectors(),
                v -> P.rationalVectors(v.dimension())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            RationalVector sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum.dimension(), p.a.dimension());
            assertEquals(p, sum, p.b.add(p.a));
            assertEquals(p, sum.subtract(p.b), p.a);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, zero(v.dimension()).add(v), v);
            assertEquals(v, v.add(zero(v.dimension())), v);
            assertTrue(v, v.add(v.negate()).isZero());
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
            assertEquals(t, sum1, sum2);
        }

        Iterable<Pair<RationalVector, RationalVector>> psFail = filter(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.rationalVectors())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, psFail)) {
            try {
                p.a.add(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesNegate() {
        initialize("");
        System.out.println("\t\ttesting negate() properties");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            RationalVector negative = v.negate();
            negative.validate();
            assertEquals(v, v.dimension(), negative.dimension());
            assertEquals(v, v, negative.negate());
            assertTrue(v, v.add(negative).isZero());
        }

        for (RationalVector v : take(LIMIT, filter(w -> any(x -> x != Rational.ZERO, w), P.rationalVectors()))) {
            RationalVector negativeV = v.negate();
            assertNotEquals(v, v, negativeV);
        }
    }

    private static @NotNull RationalVector subtract_simplest(@NotNull RationalVector a, @NotNull RationalVector b) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("");
        System.out.println("\t\ttesting subtract(RationalVector) properties...");

        Iterable<Pair<RationalVector, RationalVector>> ps = P.dependentPairs(
                P.rationalVectors(),
                v -> P.rationalVectors(v.dimension())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            RationalVector difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, difference.dimension(), p.a.dimension());
            assertEquals(p, difference, p.b.subtract(p.a).negate());
            assertEquals(p, p.a, difference.add(p.b));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, zero(v.dimension()).subtract(v), v.negate());
            assertEquals(v, v.subtract(zero(v.dimension())), v);
            assertTrue(v, v.subtract(v).isZero());
        }
    }

    private void compareImplementationsSubtract() {
        initialize("");
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

    private void propertiesMultiply_Rational() {
        initialize("");
        System.out.println("\t\ttesting multiply(Rational) properties...");

        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(), P.rationals()))) {
            RationalVector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        Iterable<Pair<RationalVector, Rational>> ps = P.pairs(
                P.rationalVectors(),
                filter(r -> r != Rational.ZERO, P.rationals())
        );
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b).divide(p.b), p.a);
        }

        ps = P.pairs(P.rationalVectors(), filter(r -> r != Rational.ZERO, P.rationals()));
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b), p.a.divide(p.b.invert()));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v, v.multiply(Rational.ONE));
            assertTrue(v, v.multiply(Rational.ZERO).isZero());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(r, ZERO_DIMENSIONAL.multiply(r) == ZERO_DIMENSIONAL);
        }

        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(1), P.rationals()))) {
            assertEquals(p, p.a.multiply(p.b), of(p.b).multiply(p.a.get(0)));
        }

        Iterable<Pair<Rational, Integer>> ps2;
        if (P instanceof QBarExhaustiveProvider) {
            ps2 = P.pairsLogarithmicOrder(P.rationals(), P.naturalIntegers());
        } else {
            ps2 = P.pairs(P.rationals(), P.withScale(20).naturalIntegersGeometric());
        }
        for (Pair<Rational, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("");
        System.out.println("\t\ttesting multiply(BigInteger) properties...");

        for (Pair<RationalVector, BigInteger> p : take(LIMIT, P.pairs(P.rationalVectors(), P.bigIntegers()))) {
            RationalVector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        Iterable<Pair<RationalVector, BigInteger>> ps = P.pairs(
                P.rationalVectors(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b).divide(p.b), p.a);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v, v.multiply(BigInteger.ONE));
            assertTrue(v, v.multiply(BigInteger.ZERO).isZero());
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertTrue(i, ZERO_DIMENSIONAL.multiply(i) == ZERO_DIMENSIONAL);
        }

        for (Pair<RationalVector, BigInteger> p : take(LIMIT, P.pairs(P.rationalVectors(1), P.bigIntegers()))) {
            assertEquals(p, p.a.multiply(p.b), of(Rational.of(p.b)).multiply(p.a.get(0)));
        }

        Iterable<Pair<BigInteger, Integer>> ps2;
        if (P instanceof QBarExhaustiveProvider) {
            ps2 = P.pairsLogarithmicOrder(P.bigIntegers(), P.naturalIntegers());
        } else {
            ps2 = P.pairs(P.bigIntegers(), P.withScale(20).naturalIntegersGeometric());
        }
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }
    }

    private void propertiesMultiply_int() {
        initialize("");
        System.out.println("\t\ttesting multiply(int) properties...");

        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), P.integers()))) {
            RationalVector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        Iterable<Pair<RationalVector, Integer>> ps = P.pairs(P.rationalVectors(), filter(i -> i != 0, P.integers()));
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b).divide(p.b), p.a);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v, v.multiply(1));
            assertTrue(v, v.multiply(0).isZero());
        }

        for (int i : take(LIMIT, P.integers())) {
            assertTrue(i, ZERO_DIMENSIONAL.multiply(i) == ZERO_DIMENSIONAL);
        }

        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(1), P.integers()))) {
            assertEquals(p, p.a.multiply(p.b), of(Rational.of(p.b)).multiply(p.a.get(0)));
        }

        Iterable<Pair<Integer, Integer>> ps2;
        if (P instanceof QBarExhaustiveProvider) {
            ps2 = P.pairsLogarithmicOrder(P.integers(), P.naturalIntegers());
        } else {
            ps2 = P.pairs(P.integers(), P.withScale(20).naturalIntegersGeometric());
        }
        for (Pair<Integer, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }
    }

    private void propertiesDivide_Rational() {
        initialize("");
        System.out.println("\t\ttesting divide(Rational) properties...");

        Iterable<Pair<RationalVector, Rational>> ps = filter(
                p -> p.b != Rational.ZERO,
                P.pairs(P.rationalVectors(), P.rationals())
        );
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            RationalVector v = p.a.divide(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
            assertEquals(p, p.a, v.multiply(p.b));
            assertEquals(p, v, p.a.multiply(p.b.invert()));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v.divide(Rational.ONE), v);
        }

        for (Rational r : take(LIMIT, filter(s -> s != Rational.ZERO, P.rationals()))) {
            assertTrue(r, ZERO_DIMENSIONAL.divide(r) == ZERO_DIMENSIONAL);
        }

        ps = P.pairs(
                filter(v -> v.get(0) != Rational.ZERO, P.rationalVectors(1)),
                filter(r -> r != Rational.ZERO, P.rationals())
        );
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.divide(p.b).get(0), of(p.b).divide(p.a.get(0)).get(0).invert());
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
            assertTrue(p, zero(p.b).divide(p.a).isZero());
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            try {
                v.divide(Rational.ZERO);
                fail(v);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_BigInteger() {
        initialize("");
        System.out.println("\t\ttesting divide(BigInteger) properties...");

        Iterable<Pair<RationalVector, BigInteger>> ps = filter(
                p -> !p.b.equals(BigInteger.ZERO),
                P.pairs(P.rationalVectors(), P.bigIntegers())
        );
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, ps)) {
            RationalVector v = p.a.divide(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
            assertEquals(p, p.a, v.multiply(p.b));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v.divide(BigInteger.ONE), v);
        }

        for (BigInteger i : take(LIMIT, filter(j -> !j.equals(BigInteger.ZERO), P.bigIntegers()))) {
            assertTrue(i, ZERO_DIMENSIONAL.divide(i) == ZERO_DIMENSIONAL);
        }

        ps = P.pairs(
                filter(v -> v.get(0) != Rational.ZERO, P.rationalVectors(1)),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.divide(p.b).get(0), of(Rational.of(p.b)).divide(p.a.get(0)).get(0).invert());
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
            assertTrue(p, zero(p.b).divide(p.a).isZero());
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            try {
                v.divide(BigInteger.ZERO);
                fail(v);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_int() {
        initialize("");
        System.out.println("\t\ttesting divide(int) properties...");

        Iterable<Pair<RationalVector, Integer>> ps = filter(
                p -> !p.b.equals(0),
                P.pairs(P.rationalVectors(), P.integers())
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            RationalVector v = p.a.divide(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
            assertEquals(p, p.a, v.multiply(p.b));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v.divide(1), v);
        }

        for (int i : take(LIMIT, filter(j -> j != 0, P.integers()))) {
            assertTrue(i, ZERO_DIMENSIONAL.divide(i) == ZERO_DIMENSIONAL);
        }

        ps = P.pairs(
                filter(v -> v.get(0) != Rational.ZERO, P.rationalVectors(1)),
                filter(i -> i != 0, P.integers())
        );
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.divide(p.b).get(0), of(Rational.of(p.b)).divide(p.a.get(0)).get(0).invert());
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
            assertTrue(p, zero(p.b).divide(p.a).isZero());
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            try {
                v.divide(0);
                fail(v);
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

    private void propertiesShiftLeft() {
        initialize("");
        System.out.println("\t\ttesting shiftLeft(int) properties...");

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is = P.integersGeometric();
        }
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), is))) {
            RationalVector shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p, map(Rational::signum, p.a), map(Rational::signum, shifted));
            assertEquals(p, p.a.dimension(), shifted.dimension());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
            assertEquals(p, shifted, p.a.shiftRight(-p.b));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v.shiftLeft(0), v);
        }

        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = P.naturalIntegersGeometric();
        }
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), is))) {
            RationalVector shifted = p.a.shiftLeft(p.b);
            assertEquals(p, shifted, p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private void compareImplementationsShiftLeft() {
        initialize("");
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

    private void propertiesShiftRight() {
        initialize("");
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
            shifted.validate();
            assertEquals(p, shifted, shiftRight_simplest(p.a, p.b));
            aeqit(p, map(Rational::signum, p.a), map(Rational::signum, shifted));
            assertEquals(p, p.a.dimension(), shifted.dimension());
            assertEquals(p, p.a.negate().shiftRight(p.b), shifted.negate());
            assertEquals(p, shifted, p.a.shiftLeft(-p.b));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v.shiftRight(0), v);
        }

        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = P.naturalIntegersGeometric();
        }
        ps = P.pairs(P.rationalVectors(), is);
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            RationalVector shifted = p.a.shiftRight(p.b);
            assertEquals(p, shifted, p.a.divide(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private void compareImplementationsShiftRight() {
        initialize("");
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

//    private void propertiesSum() {
//        initialize("");
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
//            assertEquals(vs, sum, sum_simplest(vs));
//            assertEquals(vs, sum.dimension(), head(vs).dimension());
//        }
//
//        Iterable<Pair<List<RationalVector>, List<RationalVector>>> ps = filter(
//                q -> !q.a.equals(q.b),
//                P.dependentPairs(vss, P::permutations)
//        );
//        for (Pair<List<RationalVector>, List<RationalVector>> p : take(LIMIT, ps)) {
//            assertEquals(p, sum(p.a), sum(p.b));
//        }
//
//        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
//            assertEquals(v, sum(Collections.singletonList(v)), v);
//        }
//
//        Iterable<Pair<RationalVector, RationalVector>> ps2 = filter(
//                q -> q.a.dimension() == q.b.dimension(),
//                P.pairs(P.rationalVectors())
//        );
//        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps2)) {
//            assertEquals(p, sum(Arrays.asList(p.a, p.b)), p.a.add(p.b));
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
//                fail(vs);
//            } catch (NullPointerException ignored) {}
//        }
//    }

//    private void compareImplementationsSum() {
//        initialize("");
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
//    private void propertiesDelta() {
//        initialize("");
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
//            assertTrue(vs, all(v -> v.dimension() == head(vs).dimension(), deltas));
//            assertEquals(vs, length(deltas), length(vs) - 1);
//            List<RationalVector> reversed = reverse(map(RationalVector::negate, delta(reverse(vs))));
//            aeq(vs, deltas, reversed);
//            try {
//                deltas.iterator().remove();
//            } catch (UnsupportedOperationException ignored) {}
//        }
//
//        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
//            assertTrue(v, isEmpty(delta(Collections.singletonList(v))));
//        }
//
//        Iterable<Pair<RationalVector, RationalVector>> ps = filter(
//                q -> q.a.dimension() == q.b.dimension(),
//                P.pairs(P.rationalVectors())
//        );
//        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
//            aeq(p, delta(Arrays.asList(p.a, p.b)), Collections.singletonList(p.b.subtract(p.a)));
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
//                fail(vs);
//            } catch (NullPointerException ignored) {}
//        }
//    }

    private void propertiesDot() {
        initialize("");
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
            assertEquals(p, dot, p.b.dot(p.a));
            assertEquals(p, p.a.negate().dot(p.b), dot.negate());
        }

        Iterable<Triple<Rational, RationalVector, RationalVector>> ts = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairs(ps, P.rationals())
        );
        for (Triple<Rational, RationalVector, RationalVector> t : take(LIMIT, ts)) {
            assertEquals(t, t.b.dot(t.c).multiply(t.a), t.b.multiply(t.a).dot(t.c));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v.dot(zero(v.dimension())), Rational.ZERO);
            for (int i = 0; i < v.dimension(); i++) {
                assertEquals(v, v.dot(standard(v.dimension(), i)), v.get(i));
            }
        }

        Iterable<Pair<RationalVector, RationalVector>> psFail = filter(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.rationalVectors())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, psFail)) {
            try {
                p.a.dot(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesRightAngleCompare() {
        initialize("");
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
            assertEquals(p, rightAngleCompare, p.b.rightAngleCompare(p.a));
            assertEquals(p, p.a.negate().rightAngleCompare(p.b), rightAngleCompare.invert());
        }

        Iterable<Triple<Rational, RationalVector, RationalVector>> ts = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairs(ps, P.positiveRationals())
        );
        for (Triple<Rational, RationalVector, RationalVector> t : take(LIMIT, ts)) {
            assertEquals(t, t.b.multiply(t.a).rightAngleCompare(t.c), t.b.rightAngleCompare(t.c));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v.dot(zero(v.dimension())), Rational.ZERO);
            for (int i = 0; i < v.dimension(); i++) {
                assertEquals(
                        v,
                        v.rightAngleCompare(standard(v.dimension(), i)),
                        compare(v.get(i), Rational.ZERO).invert()
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
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesSquaredLength() {
        initialize("");
        System.out.println("\t\ttesting squaredLength() properties...");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            Rational squaredLength = v.squaredLength();
            assertEquals(v, squaredLength, v.dot(v));
            assertNotEquals(v, squaredLength.signum(), -1);
            assertEquals(v, v.negate().squaredLength(), squaredLength);
        }

        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(), P.rationals()))) {
            assertEquals(p, p.a.multiply(p.b).squaredLength(), p.a.squaredLength().multiply(p.b.pow(2)));
        }

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = P.withScale(20).naturalIntegersGeometric();
        }
        for (int i : take(LIMIT, is)) {
            assertEquals(i, zero(i).squaredLength(), Rational.ZERO);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, filter(q -> q.a > q.b, P.pairs(is)))) {
            assertEquals(p, standard(p.a, p.b).squaredLength(), Rational.ONE);
        }
    }

    private void propertiesCancelDenominators() {
        initialize("");
        System.out.println("\t\ttesting cancelDenominators() properties...");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            RationalVector canceled = v.cancelDenominators();
            canceled.validate();

            assertTrue(v, all(r -> r.getDenominator().equals(BigInteger.ONE), canceled));
            BigInteger gcd = foldl((x, y) -> x.gcd(y.getNumerator()), BigInteger.ZERO, canceled);
            assertTrue(v, gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE));
            assertEquals(v, canceled.cancelDenominators(), canceled);
            assertEquals(v, canceled.dimension(), v.dimension());
            assertTrue(v, equal(map(Rational::signum, v), map(Rational::signum, canceled)));
            assertTrue(
                    v,
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
            assertEquals(p, p.a.cancelDenominators(), p.a.multiply(p.b).cancelDenominators());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            Rational canceled = head(of(r).cancelDenominators()).abs();
            assertTrue(r, canceled == Rational.ZERO || canceled == Rational.ONE);
        }

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = P.withScale(20).naturalIntegersGeometric();
        }
        for (int i : take(LIMIT, is)) {
            RationalVector zero = zero(i);
            assertEquals(i, zero.cancelDenominators(), zero);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, filter(q -> q.a > q.b, P.pairs(is)))) {
            RationalVector standard = standard(p.a, p.b);
            assertEquals(p, standard.cancelDenominators(), standard);
        }
    }

    private void propertiesPivot() {
        initialize("");
        System.out.println("\t\ttesting pivot() properties...");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            v.pivot();
        }

        for (RationalVector v : take(LIMIT, filter(w -> !w.isZero(), P.rationalVectors()))) {
            Rational pivot = v.pivot().get();
            assertTrue(v, pivot != Rational.ZERO);
            assertTrue(v, elem(pivot, v));
        }

        Iterable<Pair<RationalVector, Rational>> ps = P.pairs(
                P.rationalVectors(),
                filter(r -> r != Rational.ZERO, P.rationals())
        );
        for (Pair<RationalVector, Rational> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b).pivot(), p.a.pivot().map(r -> r.multiply(p.b)));
        }
    }

    private void propertiesReduce() {
        initialize("");
        System.out.println("\t\ttesting reduce() properties...");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            RationalVector reduced = v.reduce();
            reduced.validate();

            Optional<Rational> pivot = reduced.pivot();
            assertTrue(v, !pivot.isPresent() || pivot.get() == Rational.ONE);
            assertEquals(v, reduced.reduce(), reduced);
            assertEquals(v, reduced.dimension(), v.dimension());
            pivot = v.pivot();
            RationalVector abs = !pivot.isPresent() || pivot.get().signum() != -1 ? v : v.negate();
            assertTrue(v, equal(map(Rational::signum, abs), map(Rational::signum, reduced)));
            assertTrue(
                    v,
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
            assertEquals(p, p.a.reduce(), p.a.multiply(p.b).reduce());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            Rational reduced = head(of(r).reduce());
            assertTrue(r, reduced == Rational.ZERO || reduced == Rational.ONE);
        }

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = P.withScale(20).naturalIntegersGeometric();
        }
        for (int i : take(LIMIT, is)) {
            RationalVector zero = zero(i);
            assertEquals(i, zero.reduce(), zero);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, filter(q -> q.a > q.b, P.pairs(is)))) {
            RationalVector standard = standard(p.a, p.b);
            assertEquals(p, standard.reduce(), standard);
        }
    }

    private void propertiesEquals() {
        initialize("");
        System.out.println("\t\ttesting equals(Object) properties...");

        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::rationalVectors);
    }

    private void propertiesHashCode() {
        initialize("");
        System.out.println("\t\ttesting hashCode() properties...");

        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::rationalVectors);
    }

    private void propertiesCompareTo() {
        initialize("");
        System.out.println("\t\ttesting compareTo(RationalVector) properties...");

        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::rationalVectors);

        Iterable<Pair<RationalVector, RationalVector>> ps = filter(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.rationalVectors())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.dimension(), p.b.dimension()));
        }
    }

    private void propertiesRead() {
        initialize("");
        System.out.println("\t\ttesting read(String) properties...");

        for (String s : take(LIMIT, P.strings())) {
            read(s);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            Optional<RationalVector> ov = read(v.toString());
            assertEquals(v, ov.get(), v);
        }
    }

    private void propertiesFindIn() {
        initialize("");
        System.out.println("\t\ttesting findIn(String) properties...");

        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.rationalVectors(),
                RationalVector::read,
                RationalVector::findIn,
                rp -> {}
        );
    }

    private void propertiesToString() {
        initialize("");
        System.out.println("\t\ttesting toString() properties...");

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            String s = v.toString();
            assertTrue(v, isSubsetOf(s, RATIONAL_VECTOR_CHARS));
            Optional<RationalVector> readV = read(s);
            assertTrue(v, readV.isPresent());
            assertEquals(v, readV.get(), v);
        }
    }
}
