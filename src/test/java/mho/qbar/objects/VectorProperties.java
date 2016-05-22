package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.qbar.objects.Vector.*;
import static mho.qbar.objects.Vector.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.compare;
import static mho.wheels.testing.Testing.*;

public class VectorProperties extends QBarTestProperties {
    private static final @NotNull String VECTOR_CHARS = " ,-0123456789[]";

    public VectorProperties() {
        super("Vector");
    }

    @Override
    protected void testBothModes() {
        propertiesIterator();
        propertiesToRationalVector();
        propertiesGet();
        propertiesOf_List_BigInteger();
        propertiesOf_BigInteger();
        propertiesMaxCoordinateBitLength();
        propertiesDimension();
        propertiesIsZero();
        propertiesZero();
        propertiesStandard();
        propertiesAdd();
        propertiesNegate();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesMultiply_BigInteger();
        propertiesMultiply_int();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesSum();
        compareImplementationsSum();
        propertiesDelta();
        propertiesDot();
        propertiesRightAngleCompare();
        compareImplementationsRightAngleCompare();
        propertiesSquaredLength();
        compareImplementationsSquaredLength();
        propertiesPivot();
        propertiesIsReduced();
        propertiesIsPrimitive();
        propertiesMakePrimitive();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesReadStrict();
        propertiesToString();
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (Vector v : take(LIMIT, P.vectors())) {
            List<BigInteger> is = toList(v);
            assertTrue(v, all(i -> i != null, is));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<BigInteger> js) -> of(js), v);
            testNoRemove(v);
            testHasNext(v);
        }
    }

    private void propertiesToRationalVector() {
        initialize("toRationalVector()");
        for (Vector v : take(LIMIT, P.vectors())) {
            RationalVector rv = v.toRationalVector();
            assertEquals(v, v.toString(), rv.toString());
            assertEquals(v, v.dimension(), rv.dimension());
            inverse(Vector::toRationalVector, RationalVector::toVector, v);
        }
    }

    private void propertiesGet() {
        initialize("get(int)");
        Iterable<Pair<Vector, Integer>> ps = P.dependentPairs(
                P.vectorsAtLeast(1),
                v -> P.uniformSample(toList(range(0, v.dimension() - 1)))
        );
        for (Pair<Vector, Integer> p : take(LIMIT, ps)) {
            BigInteger x = p.a.get(p.b);
            assertEquals(p, x, toList(p.a).get(p.b));
        }

        Iterable<Pair<Vector, Integer>> psFail = filterInfinite(
                p -> p.b < 0 || p.b >= p.a.dimension(),
                P.pairs(P.vectors(), P.integers())
        );
        for (Pair<Vector, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.get(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
    }

    private void propertiesOf_List_BigInteger() {
        initialize("of(List<BigInteger>)");
        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            Vector v = of(is);
            v.validate();
            //noinspection Convert2MethodRef
            inverse(Vector::of, (Vector u) -> toList(u), is);
            assertEquals(is, v.dimension(), is.size());
        }

        for (List<BigInteger> is : take(LIMIT, P.listsWithElement(null, P.bigIntegers()))) {
            try {
                of(is);
                fail(is);
            } catch (NullPointerException ignored) {
            }
        }
    }

    private void propertiesOf_BigInteger() {
        initialize("of(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Vector v = of(i);
            v.validate();
            assertEquals(i, v.dimension(), 1);
            assertEquals(i, v.get(0), i);
            inverse(Vector::of, (Vector u) -> u.get(0), i);
        }
    }

    private void propertiesMaxCoordinateBitLength() {
        initialize("maxCoordinateBitLength()");
        for (Vector v : take(LIMIT, P.vectors())) {
            assertTrue(v, v.maxCoordinateBitLength() >= 0);
            homomorphic(
                    Vector::negate,
                    Function.identity(),
                    Vector::maxCoordinateBitLength,
                    Vector::maxCoordinateBitLength,
                    v
            );
        }
    }

    private void propertiesDimension() {
        initialize("dimension()");
        for (Vector v : take(LIMIT, P.vectors())) {
            int dimension = v.dimension();
            assertTrue(v, dimension >= 0);
            assertEquals(v, length(v), dimension);
        }
    }

    private void propertiesIsZero() {
        initialize("isZero()");
        for (Vector v : take(LIMIT, P.vectors())) {
            assertEquals(v, v.isZero(), zero(v.dimension()).equals(v));
        }
    }

    private void propertiesZero() {
        initialize("zero(int)");
        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            Vector zero = zero(i);
            zero.validate();
            assertEquals(i, zero.dimension(), i);
            inverse(Vector::zero, Vector::dimension, i);
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
            Vector standard = standard(p.b, p.a);
            standard.validate();
            assertEquals(p, standard.dimension(), p.b);
            for (int i = 0; i < p.b; i++) {
                assertEquals(p, standard.get(i), i == p.a ? BigInteger.ONE : BigInteger.ZERO);
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
        initialize("add(Vector)");
        Iterable<Pair<Vector, Vector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.vectors(i))
                        )
                )
        );
        for (Pair<Vector, Vector> p : take(LIMIT, ps)) {
            Vector sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum.dimension(), p.a.dimension());
            commutative(Vector::add, p);
            inverse(v -> v.add(p.b), (Vector v) -> v.subtract(p.b), p.a);
        }

        for (Vector v : take(LIMIT, P.vectors())) {
            fixedPoint(u -> zero(u.dimension()).add(u), v);
            fixedPoint(u -> u.add(zero(u.dimension())), v);
            assertTrue(v, v.add(v.negate()).isZero());
        }

        Iterable<Triple<Vector, Vector, Vector>> ts = P.withElement(
                new Triple<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        t -> t.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.triples(P.vectors(i))
                        )
                )
        );
        for (Triple<Vector, Vector, Vector> t : take(LIMIT, ts)) {
            associative(Vector::add, t);
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(Vector::of, Vector::of, Vector::of, BigInteger::add, Vector::add, p);
        }

        Iterable<Pair<Vector, Vector>> psFail = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.vectors())
        );
        for (Pair<Vector, Vector> p : take(LIMIT, psFail)) {
            try {
                p.a.add(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (Vector v : take(LIMIT, P.vectors())) {
            Vector negative = v.negate();
            negative.validate();
            assertEquals(v, v.dimension(), negative.dimension());
            involution(Vector::negate, v);
            assertTrue(v, v.add(negative).isZero());
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            homomorphic(Vector::of, Vector::of, BigInteger::negate, Vector::negate, i);
        }

        for (Vector v : take(LIMIT, filterInfinite(u -> !u.isZero(), P.vectors()))) {
            assertNotEquals(v, v, v.negate());
        }
    }

    private static @NotNull Vector subtract_simplest(@NotNull Vector a, @NotNull Vector b) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(Vector)");
        Iterable<Pair<Vector, Vector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.vectors(i))
                        )
                )
        );
        for (Pair<Vector, Vector> p : take(LIMIT, ps)) {
            Vector difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, subtract_simplest(p.a, p.b), difference);
            assertEquals(p, difference.dimension(), p.a.dimension());
            antiCommutative(Vector::subtract, Vector::negate, p);
            inverse(v -> v.subtract(p.b), (Vector v) -> v.add(p.b), p.a);
        }

        for (Vector v : take(LIMIT, P.vectors())) {
            assertEquals(v, zero(v.dimension()).subtract(v), v.negate());
            fixedPoint(u -> u.subtract(zero(u.dimension())), v);
            assertTrue(v, v.subtract(v).isZero());
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(Vector::of, Vector::of, Vector::of, BigInteger::subtract, Vector::subtract, p);
        }

        Iterable<Pair<Vector, Vector>> psFail = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.vectors())
        );
        for (Pair<Vector, Vector> p : take(LIMIT, psFail)) {
            try {
                p.a.subtract(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<Vector, Vector>, Vector>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        Iterable<Pair<Vector, Vector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.vectors(i))
                        )
                )
        );
        compareImplementations("subtract(Vector)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        for (Pair<Vector, BigInteger> p : take(LIMIT, P.pairs(P.vectors(), P.bigIntegers()))) {
            Vector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        for (Vector v : take(LIMIT, P.vectors())) {
            fixedPoint(u -> u.multiply(BigInteger.ONE), v);
            assertTrue(v, v.multiply(BigInteger.ZERO).isZero());
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            fixedPoint(v -> v.multiply(i), ZERO_DIMENSIONAL);
        }

        for (Pair<Vector, BigInteger> p : take(LIMIT, P.pairs(P.vectors(1), P.bigIntegers()))) {
            assertEquals(p, p.a.multiply(p.b), of(p.b).multiply(p.a.get(0)));
        }

        Iterable<Pair<BigInteger, Integer>> ps = P.pairsLogarithmicOrder(
                P.bigIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(Vector::of, Function.identity(), Vector::of, BigInteger::multiply, Vector::multiply, p);
        }
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        for (Pair<Vector, Integer> p : take(LIMIT, P.pairs(P.vectors(), P.integers()))) {
            Vector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        for (Vector v : take(LIMIT, P.vectors())) {
            fixedPoint(u -> u.multiply(1), v);
            assertTrue(v, v.multiply(0).isZero());
        }

        for (int i : take(LIMIT, P.integers())) {
            fixedPoint(v -> v.multiply(i), ZERO_DIMENSIONAL);
        }

        for (Pair<Vector, Integer> p : take(LIMIT, P.pairs(P.vectors(1), P.integers()))) {
            assertEquals(p, p.a.multiply(p.b), of(BigInteger.valueOf(p.b)).multiply(p.a.get(0)));
        }

        Iterable<Pair<Integer, Integer>> ps = P.pairsLogarithmicOrder(P.integers(), P.naturalIntegersGeometric());
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.integers()))) {
            homomorphic(
                    Vector::of,
                    Function.identity(),
                    Vector::of,
                    (i, j) -> i.multiply(BigInteger.valueOf(j)),
                    Vector::multiply,
                    p
            );
        }
    }

    private static @NotNull Vector shiftLeft_simplest(@NotNull Vector v, int bits) {
        if (bits < 0) {
            throw new ArithmeticException("bits cannot be negative. Invalid bits: " + bits);
        }
        return v.multiply(BigInteger.ONE.shiftLeft(bits));
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        for (Pair<Vector, Integer> p : take(LIMIT, P.pairs(P.vectors(), P.naturalIntegersGeometric()))) {
            Vector shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p, map(BigInteger::signum, p.a), map(BigInteger::signum, shifted));
            assertEquals(p, p.a.dimension(), shifted.dimension());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
            homomorphic(Vector::negate, Function.identity(), Vector::negate, Vector::shiftLeft, Vector::shiftLeft, p);
        }

        for (Vector v : take(LIMIT, P.vectors())) {
            fixedPoint(u -> u.shiftLeft(0), v);
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.naturalIntegersGeometric()))) {
            homomorphic(Vector::of, Function.identity(), Vector::of, BigInteger::shiftLeft, Vector::shiftLeft, p);
        }

        for (Pair<Vector, Integer> p : take(LIMIT, P.pairs(P.vectors(), P.negativeIntegersGeometric()))) {
            try {
                p.a.shiftLeft(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<Vector, Integer>, Vector>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        Iterable<Pair<Vector, Integer>> ps = P.pairs(P.vectors(), P.naturalIntegersGeometric());
        compareImplementations("shiftLeft(int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull Vector sum_alt(@NotNull Iterable<Vector> xs) {
        List<BigInteger> coordinates = toList(map(IterableUtils::sumBigInteger, transpose(map(v -> v, xs))));
        return coordinates.isEmpty() ? ZERO_DIMENSIONAL : of(coordinates);
    }

    private void propertiesSum() {
        initialize("sum(Iterable<Vector>)");
        Iterable<List<Vector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.vectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        for (List<Vector> vs : take(LIMIT, vss)) {
            Vector sum = sum(vs);
            sum.validate();
            assertEquals(vs, sum, sum_alt(vs));
            assertEquals(vs, sum.dimension(), head(vs).dimension());
        }

        Iterable<Pair<List<Vector>, List<Vector>>> ps = filterInfinite(
                q -> !q.a.equals(q.b),
                P.dependentPairs(vss, P::permutationsFinite)
        );
        for (Pair<List<Vector>, List<Vector>> p : take(LIMIT, ps)) {
            assertEquals(p, sum(p.a), sum(p.b));
        }

        for (Vector v : take(LIMIT, P.vectors())) {
            assertEquals(v, sum(Collections.singletonList(v)), v);
        }

        Iterable<Pair<Vector, Vector>> ps2 = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.vectors(i))
                        )
                )
        );
        for (Pair<Vector, Vector> p : take(LIMIT, ps2)) {
            assertEquals(p, sum(Arrays.asList(p.a, p.b)), p.a.add(p.b));
        }

        for (List<BigInteger> is : take(LIMIT, P.listsAtLeast(1, P.bigIntegers()))) {
            homomorphic(ss -> toList(map(Vector::of, ss)), Vector::of, IterableUtils::sumBigInteger, Vector::sum, is);
        }

        Iterable<List<Vector>> vssFail = filterInfinite(
                us -> !same(map(Vector::dimension, us)),
                P.listsAtLeast(1, P.vectors())
        );
        for (List<Vector> vs : take(LIMIT, vssFail)) {
            try {
                sum(vs);
                fail(vs);
            } catch (ArithmeticException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.vectors(i))
                )
        );
        for (List<Vector> vs : take(LIMIT, vssFail)) {
            try {
                sum(vs);
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void compareImplementationsSum() {
        Map<String, Function<List<Vector>, Vector>> functions = new LinkedHashMap<>();
        functions.put("alt", VectorProperties::sum_alt);
        functions.put("standard", Vector::sum);
        Iterable<List<Vector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.vectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        compareImplementations("sum(Iterable<Vector>)", take(LIMIT, vss), functions, v -> P.reset());
    }

    private void propertiesDelta() {
        initialize("delta(Iterable<Vector>)");
        Iterable<List<Vector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.vectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        for (List<Vector> vs : take(LIMIT, vss)) {
            Iterable<Vector> deltas = delta(vs);
            deltas.forEach(Vector::validate);
            assertTrue(vs, all(v -> v.dimension() == head(vs).dimension(), deltas));
            assertEquals(vs, length(deltas), length(vs) - 1);
            List<Vector> reversed = reverse(map(Vector::negate, delta(reverse(vs))));
            aeqit(vs, deltas, reversed);
            testNoRemove(TINY_LIMIT, deltas);
            testHasNext(deltas);
        }

        for (Vector v : take(LIMIT, P.vectors())) {
            assertTrue(v, isEmpty(delta(Collections.singletonList(v))));
        }

        Iterable<Pair<Vector, Vector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.vectors(i))
                        )
                )
        );
        for (Pair<Vector, Vector> p : take(LIMIT, ps)) {
            aeqit(p, delta(Arrays.asList(p.a, p.b)), Collections.singletonList(p.b.subtract(p.a)));
        }

        Iterable<Iterable<Vector>> vss2 = P.withElement(
                repeat(ZERO_DIMENSIONAL),
                map(
                        p -> p.b, P.dependentPairsInfiniteSquareRootOrder(
                                P.positiveIntegersGeometric(),
                                i -> EP.prefixPermutations(QBarTesting.QEP.vectors(i))
                        )
                )
        );
        for (Iterable<Vector> vs : take(LIMIT, vss2)) {
            Iterable<Vector> deltas = delta(vs);
            List<Vector> deltaPrefix = toList(take(TINY_LIMIT, deltas));
            deltaPrefix.forEach(Vector::validate);
            assertEquals(vs, length(deltaPrefix), TINY_LIMIT);
            testNoRemove(TINY_LIMIT, deltas);
        }

        for (List<BigInteger> is : take(LIMIT, P.listsAtLeast(1, P.bigIntegers()))) {
            homomorphic(
                    ss -> toList(map(Vector::of, ss)),
                    ss -> toList(map(Vector::of, ss)),
                    ss -> toList(deltaBigInteger(ss)),
                    vs -> toList(delta(vs)),
                    is
            );
        }

        Iterable<List<Vector>> vssFail = filterInfinite(
                us -> !same(map(Vector::dimension, us)),
                P.listsAtLeast(1, P.vectors())
        );
        for (List<Vector> vs : take(LIMIT, vssFail)) {
            try {
                toList(delta(vs));
                fail(vs);
            } catch (ArithmeticException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.vectors(i))
                )
        );
        for (List<Vector> vs : take(LIMIT, vssFail)) {
            try {
                toList(delta(vs));
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesDot() {
        initialize("dot(Vector)");
        Iterable<Pair<Vector, Vector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.vectors(i))
                        )
                )
        );
        for (Pair<Vector, Vector> p : take(LIMIT, ps)) {
            commutative(Vector::dot, p);
            homomorphic(Vector::negate, Function.identity(), BigInteger::negate, Vector::dot, Vector::dot, p);
            homomorphic(Function.identity(), Vector::negate, BigInteger::negate, Vector::dot, Vector::dot, p);
        }

        Iterable<Triple<BigInteger, Vector, Vector>> ts = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairs(ps, P.bigIntegers())
        );
        for (Triple<BigInteger, Vector, Vector> t : take(LIMIT, ts)) {
            assertEquals(t, t.b.dot(t.c).multiply(t.a), t.b.multiply(t.a).dot(t.c));
        }

        for (Vector v : take(LIMIT, P.vectors())) {
            assertEquals(v, v.dot(zero(v.dimension())), BigInteger.ZERO);
            for (int i = 0; i < v.dimension(); i++) {
                assertEquals(v, v.dot(standard(v.dimension(), i)), v.get(i));
            }
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(Vector::of, Vector::of, Function.identity(), BigInteger::multiply, Vector::dot, p);
        }

        Iterable<Pair<Vector, Vector>> psFail = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.vectors())
        );
        for (Pair<Vector, Vector> p : take(LIMIT, psFail)) {
            try {
                p.a.dot(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull Ordering rightAngleCompare_simplest(@NotNull Vector a, @NotNull Vector b) {
        return Ordering.compare(a.dot(b), BigInteger.ZERO).invert();
    }

    private void propertiesRightAngleCompare() {
        initialize("rightAngleCompare(Vector)");
        Iterable<Pair<Vector, Vector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.vectors(i))
                        )
                )
        );
        for (Pair<Vector, Vector> p : take(LIMIT, ps)) {
            Ordering compare = p.a.rightAngleCompare(p.b);
            assertEquals(p, rightAngleCompare_simplest(p.a, p.b), compare);
            commutative(Vector::rightAngleCompare, p);
            homomorphic(
                    Vector::negate,
                    Function.identity(),
                    Ordering::invert,
                    Vector::rightAngleCompare,
                    Vector::rightAngleCompare,
                    p
            );
            homomorphic(
                    Function.identity(),
                    Vector::negate,
                    Ordering::invert,
                    Vector::rightAngleCompare,
                    Vector::rightAngleCompare,
                    p
            );
        }

        Iterable<Triple<BigInteger, Vector, Vector>> ts = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairs(ps, P.positiveBigIntegers())
        );
        for (Triple<BigInteger, Vector, Vector> t : take(LIMIT, ts)) {
            assertEquals(t, t.b.multiply(t.a).rightAngleCompare(t.c), t.b.rightAngleCompare(t.c));
        }

        for (Vector v : take(LIMIT, P.vectors())) {
            assertEquals(v, v.dot(zero(v.dimension())), BigInteger.ZERO);
            for (int i = 0; i < v.dimension(); i++) {
                assertEquals(
                        v,
                        v.rightAngleCompare(standard(v.dimension(), i)),
                        compare(v.get(i), BigInteger.ZERO).invert()
                );
            }
        }

        Iterable<Pair<Vector, Vector>> psFail = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.vectors())
        );
        for (Pair<Vector, Vector> p : take(LIMIT, psFail)) {
            try {
                p.a.rightAngleCompare(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsRightAngleCompare() {
        Map<String, Function<Pair<Vector, Vector>, Ordering>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> rightAngleCompare_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.rightAngleCompare(p.b));
        Iterable<Pair<Vector, Vector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.vectors(i))
                        )
                )
        );
        compareImplementations("rightAngleCompare(Vector)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull BigInteger squaredLength_simplest(@NotNull Vector v) {
        return v.dot(v);
    }

    private void propertiesSquaredLength() {
        initialize("squaredLength()");
        for (Vector v : take(LIMIT, P.vectors())) {
            BigInteger squaredLength = v.squaredLength();
            assertEquals(v, squaredLength_simplest(v), squaredLength);
            assertNotEquals(v, squaredLength.signum(), -1);
            assertEquals(v, v.negate().squaredLength(), squaredLength);
        }

        for (Pair<Vector, BigInteger> p : take(LIMIT, P.pairs(P.vectors(), P.bigIntegers()))) {
            assertEquals(p, p.a.multiply(p.b).squaredLength(), p.a.squaredLength().multiply(p.b.pow(2)));
        }

        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            assertEquals(i, zero(i).squaredLength(), BigInteger.ZERO);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.subsetPairs(P.naturalIntegersGeometric()))) {
            assertEquals(p, standard(p.b, p.a).squaredLength(), BigInteger.ONE);
        }
    }

    private void compareImplementationsSquaredLength() {
        Map<String, Function<Vector, BigInteger>> functions = new LinkedHashMap<>();
        functions.put("simplest", VectorProperties::squaredLength_simplest);
        functions.put("standard", Vector::squaredLength);
        compareImplementations("squaredLength()", take(LIMIT, P.vectors()), functions, v -> P.reset());
    }

    private void propertiesPivot() {
        initialize("pivot()");
        for (Vector v : take(LIMIT, P.vectors())) {
            v.pivot();
        }

        for (Vector v : take(LIMIT, filterInfinite(u -> !u.isZero(), P.vectors()))) {
            BigInteger pivot = v.pivot().get();
            assertFalse(v, pivot.equals(BigInteger.ZERO));
            assertTrue(v, elem(pivot, v));
        }

        for (Pair<Vector, BigInteger> p : take(LIMIT, P.pairs(P.vectors(), P.nonzeroBigIntegers()))) {
            assertEquals(p, p.a.multiply(p.b).pivot(), p.a.pivot().map(r -> r.multiply(p.b)));
        }
    }

    private void propertiesIsReduced() {
        initialize("isReduced()");
        for (Vector v : take(LIMIT, P.vectors())) {
            boolean reduced = v.isReduced();
            if (reduced) {
                assertTrue(v, v.isPrimitive());
            }
        }

        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            Vector zero = zero(i);
            assertTrue(i, zero.isReduced());
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.subsetPairs(P.naturalIntegersGeometric()))) {
            Vector standard = standard(p.b, p.a);
            assertTrue(p, standard.isReduced());
        }
    }

    private void propertiesIsPrimitive() {
        initialize("isPrimitive()");
        for (Vector v : take(LIMIT, P.vectors())) {
            v.isPrimitive();
        }

        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            Vector zero = zero(i);
            assertTrue(i, zero.isPrimitive());
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.subsetPairs(P.naturalIntegersGeometric()))) {
            Vector standard = standard(p.b, p.a);
            assertTrue(p, standard.isPrimitive());
        }
    }

    private void propertiesMakePrimitive() {
        initialize("makePrimitive()");
        for (Vector v : take(LIMIT, P.vectors())) {
            Vector primitive = v.makePrimitive();
            primitive.validate();
            assertTrue(v, primitive.isPrimitive());
            idempotent(Vector::makePrimitive, v);
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::vectors);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::vectors);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(Vector)");
        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::vectors);

        Iterable<Pair<Vector, Vector>> ps = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.vectors())
        );
        for (Pair<Vector, Vector> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.dimension(), p.b.dimension()));
        }
    }

    private void propertiesReadStrict() {
        initialize("readStrict(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                VECTOR_CHARS,
                P.vectors(),
                Vector::readStrict,
                Vector::validate,
                false,
                true
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, VECTOR_CHARS, P.vectors(), Vector::readStrict);
    }
}
