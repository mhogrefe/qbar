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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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
        initialize("add(RationalVector)");
        Iterable<Pair<RationalVector, RationalVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.rationalVectors(i))
                        )
                )
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            RationalVector sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum.dimension(), p.a.dimension());
            commutative(RationalVector::add, p);
            inverse(v -> v.add(p.b), (RationalVector v) -> v.subtract(p.b), p.a);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            fixedPoint(u -> zero(u.dimension()).add(u), v);
            fixedPoint(u -> u.add(zero(u.dimension())), v);
            assertTrue(v, v.add(v.negate()).isZero());
        }

        Iterable<Triple<RationalVector, RationalVector, RationalVector>> ts = P.withElement(
                new Triple<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        t -> t.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.triples(P.rationalVectors(i))
                        )
                )
        );
        for (Triple<RationalVector, RationalVector, RationalVector> t : take(LIMIT, ts)) {
            associative(RationalVector::add, t);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(
                    RationalVector::of,
                    RationalVector::of,
                    RationalVector::of,
                    Rational::add,
                    RationalVector::add,
                    p
            );
        }

        Iterable<Pair<RationalVector, RationalVector>> psFail = filterInfinite(
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
        initialize("negate()");
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            RationalVector negative = v.negate();
            negative.validate();
            assertEquals(v, v.dimension(), negative.dimension());
            involution(RationalVector::negate, v);
            assertTrue(v, v.add(negative).isZero());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            homomorphic(RationalVector::of, RationalVector::of, Rational::negate, RationalVector::negate, r);
        }

        Iterable<RationalVector> vsFail = filterInfinite(w -> any(x -> x != Rational.ZERO, w), P.rationalVectors());
        for (RationalVector v : take(LIMIT, vsFail)) {
            assertNotEquals(v, v, v.negate());
        }
    }

    private static @NotNull RationalVector subtract_simplest(@NotNull RationalVector a, @NotNull RationalVector b) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(RationalVector)");
        Iterable<Pair<RationalVector, RationalVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.rationalVectors(i))
                        )
                )
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, ps)) {
            RationalVector difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, subtract_simplest(p.a, p.b), difference);
            assertEquals(p, difference.dimension(), p.a.dimension());
            antiCommutative(RationalVector::subtract, RationalVector::negate, p);
            inverse(v -> v.subtract(p.b), (RationalVector v) -> v.add(p.b), p.a);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, zero(v.dimension()).subtract(v), v.negate());
            fixedPoint(u -> u.subtract(zero(u.dimension())), v);
            assertTrue(v, v.subtract(v).isZero());
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(
                    RationalVector::of,
                    RationalVector::of,
                    RationalVector::of,
                    Rational::subtract,
                    RationalVector::subtract,
                    p
            );
        }

        Iterable<Pair<RationalVector, RationalVector>> psFail = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.rationalVectors())
        );
        for (Pair<RationalVector, RationalVector> p : take(LIMIT, psFail)) {
            try {
                p.a.subtract(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<RationalVector, RationalVector>, RationalVector>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        Iterable<Pair<RationalVector, RationalVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.rationalVectors(i))
                        )
                )
        );
        compareImplementations("subtract(RationalVector)", take(LIMIT, ps), functions);
    }

    private void propertiesMultiply_Rational() {
        initialize("multiply(Rational)");
        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(), P.rationals()))) {
            RationalVector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(), P.nonzeroRationals()))) {
            inverse(v -> v.multiply(p.b), (RationalVector v) -> v.divide(p.b), p.a);
            assertEquals(p, p.a.multiply(p.b), p.a.divide(p.b.invert()));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            fixedPoint(u -> u.multiply(Rational.ONE), v);
            assertTrue(v, v.multiply(Rational.ZERO).isZero());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            fixedPoint(v -> v.multiply(r), ZERO_DIMENSIONAL);
        }

        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(1), P.rationals()))) {
            assertEquals(p, p.a.multiply(p.b), of(p.b).multiply(p.a.get(0)));
        }

        Iterable<Pair<Rational, Integer>> ps = P.pairsLogarithmicOrder(P.rationals(), P.naturalIntegersGeometric());
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(
                    RationalVector::of,
                    Function.identity(),
                    RationalVector::of,
                    Rational::multiply,
                    RationalVector::multiply,
                    p
            );
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, P.pairs(P.rationalVectors(), P.bigIntegers()))) {
            RationalVector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        for (Pair<RationalVector, BigInteger> p : take(LIMIT, P.pairs(P.rationalVectors(), P.nonzeroBigIntegers()))) {
            inverse(v -> v.multiply(p.b), (RationalVector v) -> v.divide(p.b), p.a);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            fixedPoint(u -> u.multiply(BigInteger.ONE), v);
            assertTrue(v, v.multiply(BigInteger.ZERO).isZero());
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            fixedPoint(v -> v.multiply(i), ZERO_DIMENSIONAL);
        }

        for (Pair<RationalVector, BigInteger> p : take(LIMIT, P.pairs(P.rationalVectors(1), P.bigIntegers()))) {
            assertEquals(p, p.a.multiply(p.b), of(Rational.of(p.b)).multiply(p.a.get(0)));
        }

        Iterable<Pair<BigInteger, Integer>> ps = P.pairsLogarithmicOrder(
                P.bigIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.bigIntegers()))) {
            homomorphic(
                    RationalVector::of,
                    Function.identity(),
                    RationalVector::of,
                    Rational::multiply,
                    RationalVector::multiply,
                    p
            );
        }
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), P.integers()))) {
            RationalVector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), P.nonzeroIntegers()))) {
            inverse(v -> v.multiply(p.b), (RationalVector v) -> v.divide(p.b), p.a);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            fixedPoint(u -> u.multiply(1), v);
            assertTrue(v, v.multiply(0).isZero());
        }

        for (int i : take(LIMIT, P.integers())) {
            fixedPoint(v -> v.multiply(i), ZERO_DIMENSIONAL);
        }

        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(1), P.integers()))) {
            assertEquals(p, p.a.multiply(p.b), of(Rational.of(p.b)).multiply(p.a.get(0)));
        }

        Iterable<Pair<Integer, Integer>> ps = P.pairsLogarithmicOrder(P.integers(), P.naturalIntegersGeometric());
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integers()))) {
            homomorphic(
                    RationalVector::of,
                    Function.identity(),
                    RationalVector::of,
                    Rational::multiply,
                    RationalVector::multiply,
                    p
            );
        }
    }

    private void propertiesDivide_Rational() {
        initialize("divide(Rational)");
        for (Pair<RationalVector, Rational> p : take(LIMIT, P.pairs(P.rationalVectors(), P.nonzeroRationals()))) {
            RationalVector v = p.a.divide(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
            inverse(u -> u.divide(p.b), (RationalVector u) -> u.multiply(p.b), p.a);
            assertEquals(p, v, p.a.multiply(p.b.invert()));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            fixedPoint(u -> u.divide(Rational.ONE), v);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            fixedPoint(v -> v.divide(r), ZERO_DIMENSIONAL);
        }

        Iterable<Pair<Rational, Integer>> ps = P.pairsLogarithmicOrder(
                P.nonzeroRationals(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, zero(p.b).divide(p.a).isZero());
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroRationals()))) {
            homomorphic(
                    RationalVector::of,
                    Function.identity(),
                    RationalVector::of,
                    Rational::divide,
                    RationalVector::divide,
                    p
            );
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            try {
                v.divide(Rational.ZERO);
                fail(v);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_BigInteger() {
        initialize("divide(BigInteger)");
        for (Pair<RationalVector, BigInteger> p : take(LIMIT, P.pairs(P.rationalVectors(), P.nonzeroBigIntegers()))) {
            RationalVector v = p.a.divide(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
            inverse(u -> u.divide(p.b), (RationalVector u) -> u.multiply(p.b), p.a);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v.divide(BigInteger.ONE), v);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            fixedPoint(v -> v.divide(i), ZERO_DIMENSIONAL);
        }

        Iterable<Pair<BigInteger, Integer>> ps = P.pairsLogarithmicOrder(
                P.nonzeroBigIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, zero(p.b).divide(p.a).isZero());
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroBigIntegers()))) {
            homomorphic(
                    RationalVector::of,
                    Function.identity(),
                    RationalVector::of,
                    Rational::divide,
                    RationalVector::divide,
                    p
            );
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            try {
                v.divide(BigInteger.ZERO);
                fail(v);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_int() {
        initialize("divide(int)");
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), P.nonzeroIntegers()))) {
            RationalVector v = p.a.divide(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
            inverse(u -> u.divide(p.b), (RationalVector u) -> u.multiply(p.b), p.a);
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v.divide(1), v);
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            fixedPoint(v -> v.divide(i), ZERO_DIMENSIONAL);
        }

        Iterable<Pair<Integer, Integer>> ps = P.pairsLogarithmicOrder(
                P.nonzeroIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, zero(p.b).divide(p.a).isZero());
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroIntegers()))) {
            homomorphic(
                    RationalVector::of,
                    Function.identity(),
                    RationalVector::of,
                    Rational::divide,
                    RationalVector::divide,
                    p
            );
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
        initialize("shiftLeft(int)");
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), P.integersGeometric()))) {
            homomorphic(
                    RationalVector::negate,
                    Function.identity(),
                    RationalVector::negate,
                    RationalVector::shiftLeft,
                    RationalVector::shiftLeft,
                    p
            );
            RationalVector shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p, map(Rational::signum, p.a), map(Rational::signum, shifted));
            assertEquals(p, p.a.dimension(), shifted.dimension());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
            inverse(a -> a.shiftLeft(p.b), (RationalVector v) -> v.shiftRight(p.b), p.a);
            assertEquals(p, shifted, p.a.shiftRight(-p.b));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            fixedPoint(u -> u.shiftLeft(0), v);
        }

        Iterable<Pair<RationalVector, Integer>> ps = P.pairs(P.rationalVectors(), P.naturalIntegersGeometric());
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.shiftLeft(p.b), p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            homomorphic(
                    RationalVector::of,
                    Function.identity(),
                    RationalVector::of,
                    Rational::shiftLeft,
                    RationalVector::shiftLeft,
                    p
            );
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<RationalVector, Integer>, RationalVector>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        Iterable<Pair<RationalVector, Integer>> ps = P.pairs(P.rationalVectors(), P.integersGeometric());
        compareImplementations("shiftLeft(int)", take(LIMIT, ps), functions);
    }

    private static @NotNull RationalVector shiftRight_simplest(@NotNull RationalVector v, int bits) {
        if (bits < 0) {
            return v.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return v.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftRight() {
        initialize("shiftRight(int)");
        for (Pair<RationalVector, Integer> p : take(LIMIT, P.pairs(P.rationalVectors(), P.integersGeometric()))) {
            homomorphic(
                    RationalVector::negate,
                    Function.identity(),
                    RationalVector::negate,
                    RationalVector::shiftRight,
                    RationalVector::shiftRight,
                    p
            );
            RationalVector shifted = p.a.shiftRight(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftRight_simplest(p.a, p.b));
            aeqit(p, map(Rational::signum, p.a), map(Rational::signum, shifted));
            assertEquals(p, p.a.dimension(), shifted.dimension());
            assertEquals(p, p.a.negate().shiftRight(p.b), shifted.negate());
            inverse(a -> a.shiftRight(p.b), (RationalVector v) -> v.shiftLeft(p.b), p.a);
            assertEquals(p, shifted, p.a.shiftLeft(-p.b));
        }

        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            assertEquals(v, v.shiftRight(0), v);
        }

        Iterable<Pair<RationalVector, Integer>> ps = P.pairs(P.rationalVectors(), P.naturalIntegersGeometric());
        for (Pair<RationalVector, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.shiftRight(p.b), p.a.divide(BigInteger.ONE.shiftLeft(p.b)));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            homomorphic(
                    RationalVector::of,
                    Function.identity(),
                    RationalVector::of,
                    Rational::shiftRight,
                    RationalVector::shiftRight,
                    p
            );
        }
    }

    private void compareImplementationsShiftRight() {
        Map<String, Function<Pair<RationalVector, Integer>, RationalVector>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftRight_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftRight(p.b));
        Iterable<Pair<RationalVector, Integer>> ps = P.pairs(P.rationalVectors(), P.integersGeometric());
        compareImplementations("shiftRight(int)", take(LIMIT, ps), functions);
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
