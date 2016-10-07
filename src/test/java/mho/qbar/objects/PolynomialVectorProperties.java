package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.qbar.objects.PolynomialVector.*;
import static mho.qbar.objects.PolynomialVector.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.compare;
import static mho.wheels.testing.Testing.*;

public class PolynomialVectorProperties extends QBarTestProperties {
    private static final @NotNull String POLYNOMIAL_VECTOR_CHARS = " *+,-0123456789[]^x";

    public PolynomialVectorProperties() {
        super("PolynomialVector");
    }

    @Override
    protected void testBothModes() {
        propertiesIterator();
        propertiesToRationalPolynomialVector();
        propertiesGet();
        propertiesOf_List_Polynomial();
        propertiesOf_Polynomial();
        propertiesOf_Vector();
        propertiesMaxCoordinateBitLength();
        propertiesDimension();
        propertiesIsZero();
        propertiesZero();
        propertiesStandard();
        propertiesAdd();
        propertiesNegate();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesMultiply_Polynomial();
        propertiesMultiply_BigInteger();
        propertiesMultiply_int();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesSum();
        compareImplementationsSum();
        propertiesDelta();
        propertiesDot();
        propertiesSquaredLength();
        compareImplementationsSquaredLength();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesReadStrict();
        propertiesToString();
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            List<Polynomial> ps = toList(v);
            assertTrue(v, all(p -> p != null, ps));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<Polynomial> qs) -> of(qs), v);
            testNoRemove(v);
            testHasNext(v);
        }
    }

    private void propertiesToRationalPolynomialVector() {
        initialize("toRationalPolynomialVector()");
        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            RationalPolynomialVector rv = v.toRationalPolynomialVector();
            assertEquals(v, v.toString(), rv.toString());
            assertEquals(v, v.dimension(), rv.dimension());
            inverse(PolynomialVector::toRationalPolynomialVector, RationalPolynomialVector::toPolynomialVector, v);
        }
    }

    private void propertiesGet() {
        initialize("get(int)");
        Iterable<Pair<PolynomialVector, Integer>> ps = P.dependentPairs(
                P.polynomialVectorsAtLeast(1),
                v -> P.uniformSample(toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, v.dimension() - 1)))
        );
        for (Pair<PolynomialVector, Integer> p : take(LIMIT, ps)) {
            Polynomial x = p.a.get(p.b);
            assertEquals(p, x, toList(p.a).get(p.b));
        }

        Iterable<Pair<PolynomialVector, Integer>> psFail = filterInfinite(
                p -> p.b < 0 || p.b >= p.a.dimension(),
                P.pairs(P.polynomialVectors(), P.integers())
        );
        for (Pair<PolynomialVector, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.get(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
    }

    private void propertiesOf_List_Polynomial() {
        initialize("of(List<Polynomial>)");
        for (List<Polynomial> is : take(LIMIT, P.lists(P.polynomials()))) {
            PolynomialVector v = of(is);
            v.validate();
            //noinspection Convert2MethodRef
            inverse(PolynomialVector::of, (PolynomialVector u) -> toList(u), is);
            assertEquals(is, v.dimension(), is.size());
        }

        for (List<Polynomial> ps : take(LIMIT, P.listsWithElement(null, P.polynomials()))) {
            try {
                of(ps);
                fail(ps);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesOf_Polynomial() {
        initialize("of(Polynomial)");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            PolynomialVector v = of(p);
            v.validate();
            assertEquals(p, v.dimension(), 1);
            assertEquals(p, v.get(0), p);
            inverse(PolynomialVector::of, (PolynomialVector u) -> u.get(0), p);
        }
    }

    private void propertiesOf_Vector() {
        initialize("of(Vector)");
        for (Vector v : take(LIMIT, P.vectors())) {
            PolynomialVector pv = of(v);
            pv.validate();
            assertEquals(v, pv.dimension(), v.dimension());
            assertEquals(v, pv.toString(), pv.toString());
        }
    }

    private void propertiesMaxCoordinateBitLength() {
        initialize("maxCoordinateBitLength()");
        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            assertTrue(v, v.maxCoordinateBitLength() >= 0);
            homomorphic(
                    PolynomialVector::negate,
                    Function.identity(),
                    PolynomialVector::maxCoordinateBitLength,
                    PolynomialVector::maxCoordinateBitLength,
                    v
            );
        }
    }

    private void propertiesDimension() {
        initialize("dimension()");
        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            int dimension = v.dimension();
            assertTrue(v, dimension >= 0);
            assertEquals(v, length(v), dimension);
        }
    }

    private void propertiesIsZero() {
        initialize("isZero()");
        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            assertEquals(v, v.isZero(), zero(v.dimension()).equals(v));
        }
    }

    private void propertiesZero() {
        initialize("zero(int)");
        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            PolynomialVector zero = zero(i);
            zero.validate();
            assertEquals(i, zero.dimension(), i);
            inverse(PolynomialVector::zero, PolynomialVector::dimension, i);
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
            PolynomialVector standard = standard(p.b, p.a);
            standard.validate();
            assertEquals(p, standard.dimension(), p.b);
            for (int i = 0; i < p.b; i++) {
                assertEquals(p, standard.get(i), i == p.a ? Polynomial.ONE : Polynomial.ZERO);
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
        initialize("add(PolynomialVector)");
        Iterable<Pair<PolynomialVector, PolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.polynomialVectors(i))
                        )
                )
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, ps)) {
            PolynomialVector sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum.dimension(), p.a.dimension());
            commutative(PolynomialVector::add, p);
            inverse(v -> v.add(p.b), (PolynomialVector v) -> v.subtract(p.b), p.a);
        }

        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            fixedPoint(u -> zero(u.dimension()).add(u), v);
            fixedPoint(u -> u.add(zero(u.dimension())), v);
            assertTrue(v, v.add(v.negate()).isZero());
        }

        Iterable<Triple<PolynomialVector, PolynomialVector, PolynomialVector>> ts = P.withElement(
                new Triple<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        t -> t.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.triples(P.polynomialVectors(i))
                        )
                )
        );
        for (Triple<PolynomialVector, PolynomialVector, PolynomialVector> t : take(LIMIT, ts)) {
            associative(PolynomialVector::add, t);
        }

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            homomorphic(
                    PolynomialVector::of,
                    PolynomialVector::of,
                    PolynomialVector::of,
                    Polynomial::add,
                    PolynomialVector::add,
                    p
            );
        }

        Iterable<Pair<PolynomialVector, PolynomialVector>> psFail = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.polynomialVectors())
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, psFail)) {
            try {
                p.a.add(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            PolynomialVector negative = v.negate();
            negative.validate();
            assertEquals(v, v.dimension(), negative.dimension());
            involution(PolynomialVector::negate, v);
            assertTrue(v, v.add(negative).isZero());
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            homomorphic(PolynomialVector::of, PolynomialVector::of, Polynomial::negate, PolynomialVector::negate, p);
        }

        for (PolynomialVector v : take(LIMIT, filterInfinite(u -> !u.isZero(), P.polynomialVectors()))) {
            assertNotEquals(v, v, v.negate());
        }
    }

    private static @NotNull PolynomialVector subtract_simplest(
            @NotNull PolynomialVector a,
            @NotNull PolynomialVector b
    ) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(PolynomialVector)");
        Iterable<Pair<PolynomialVector, PolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.polynomialVectors(i))
                        )
                )
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, ps)) {
            PolynomialVector difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, subtract_simplest(p.a, p.b), difference);
            assertEquals(p, difference.dimension(), p.a.dimension());
            antiCommutative(PolynomialVector::subtract, PolynomialVector::negate, p);
            inverse(v -> v.subtract(p.b), (PolynomialVector v) -> v.add(p.b), p.a);
        }

        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            assertEquals(v, zero(v.dimension()).subtract(v), v.negate());
            fixedPoint(u -> u.subtract(zero(u.dimension())), v);
            assertTrue(v, v.subtract(v).isZero());
        }

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            homomorphic(
                    PolynomialVector::of,
                    PolynomialVector::of,
                    PolynomialVector::of,
                    Polynomial::subtract,
                    PolynomialVector::subtract,
                    p
            );
        }

        Iterable<Pair<PolynomialVector, PolynomialVector>> psFail = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.polynomialVectors())
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, psFail)) {
            try {
                p.a.subtract(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<PolynomialVector, PolynomialVector>, PolynomialVector>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        Iterable<Pair<PolynomialVector, PolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.polynomialVectors(i))
                        )
                )
        );
        compareImplementations("subtract(PolynomialVector)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesMultiply_Polynomial() {
        initialize("multiply(Polynomial)");
        for (Pair<PolynomialVector, Polynomial> p : take(LIMIT, P.pairs(P.polynomialVectors(), P.polynomials()))) {
            PolynomialVector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            fixedPoint(u -> u.multiply(Polynomial.ONE), v);
            assertTrue(v, v.multiply(Polynomial.ZERO).isZero());
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            fixedPoint(v -> v.multiply(p), ZERO_DIMENSIONAL);
        }

        for (Pair<PolynomialVector, Polynomial> p : take(LIMIT, P.pairs(P.polynomialVectors(1), P.polynomials()))) {
            assertEquals(p, p.a.multiply(p.b), of(p.b).multiply(p.a.get(0)));
        }

        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.polynomials(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            homomorphic(
                    PolynomialVector::of,
                    Function.identity(),
                    PolynomialVector::of,
                    Polynomial::multiply,
                    PolynomialVector::multiply,
                    p
            );
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        for (Pair<PolynomialVector, BigInteger> p : take(LIMIT, P.pairs(P.polynomialVectors(), P.bigIntegers()))) {
            PolynomialVector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            fixedPoint(u -> u.multiply(BigInteger.ONE), v);
            assertTrue(v, v.multiply(BigInteger.ZERO).isZero());
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            fixedPoint(v -> v.multiply(i), ZERO_DIMENSIONAL);
        }

        for (Pair<PolynomialVector, BigInteger> p : take(LIMIT, P.pairs(P.polynomialVectors(1), P.bigIntegers()))) {
            assertEquals(p, p.a.multiply(p.b), of(Polynomial.of(p.b)).multiply(p.a.get(0)));
        }

        Iterable<Pair<BigInteger, Integer>> ps = P.pairsLogarithmicOrder(
                P.bigIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }

        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            homomorphic(
                    PolynomialVector::of,
                    Function.identity(),
                    PolynomialVector::of,
                    Polynomial::multiply,
                    PolynomialVector::multiply,
                    p
            );
        }
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        for (Pair<PolynomialVector, Integer> p : take(LIMIT, P.pairs(P.polynomialVectors(), P.integers()))) {
            PolynomialVector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            fixedPoint(u -> u.multiply(1), v);
            assertTrue(v, v.multiply(0).isZero());
        }

        for (int i : take(LIMIT, P.integers())) {
            fixedPoint(v -> v.multiply(i), ZERO_DIMENSIONAL);
        }

        for (Pair<PolynomialVector, Integer> p : take(LIMIT, P.pairs(P.polynomialVectors(1), P.integers()))) {
            assertEquals(p, p.a.multiply(p.b), of(Polynomial.of(BigInteger.valueOf(p.b))).multiply(p.a.get(0)));
        }

        Iterable<Pair<Integer, Integer>> ps = P.pairsLogarithmicOrder(P.integers(), P.naturalIntegersGeometric());
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.integers()))) {
            homomorphic(
                    PolynomialVector::of,
                    Function.identity(),
                    PolynomialVector::of,
                    Polynomial::multiply,
                    PolynomialVector::multiply,
                    p
            );
        }
    }

    private static @NotNull PolynomialVector shiftLeft_simplest(@NotNull PolynomialVector v, int bits) {
        if (bits < 0) {
            throw new ArithmeticException("bits cannot be negative. Invalid bits: " + bits);
        }
        return v.multiply(BigInteger.ONE.shiftLeft(bits));
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        Iterable<Pair<PolynomialVector, Integer>> ps = P.pairs(P.polynomialVectors(), P.naturalIntegersGeometric());
        for (Pair<PolynomialVector, Integer> p : take(LIMIT, ps)) {
            PolynomialVector shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p, map(Polynomial::signum, p.a), map(Polynomial::signum, shifted));
            assertEquals(p, p.a.dimension(), shifted.dimension());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
            homomorphic(
                    PolynomialVector::negate,
                    Function.identity(),
                    PolynomialVector::negate,
                    PolynomialVector::shiftLeft,
                    PolynomialVector::shiftLeft,
                    p
            );
        }

        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            fixedPoint(u -> u.shiftLeft(0), v);
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.naturalIntegersGeometric()))) {
            homomorphic(
                    PolynomialVector::of,
                    Function.identity(),
                    PolynomialVector::of,
                    Polynomial::shiftLeft,
                    PolynomialVector::shiftLeft,
                    p
            );
        }

        Iterable<Pair<PolynomialVector, Integer>> psFail = P.pairs(
                P.polynomialVectors(),
                P.negativeIntegersGeometric()
        );
        for (Pair<PolynomialVector, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.shiftLeft(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<PolynomialVector, Integer>, PolynomialVector>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        Iterable<Pair<PolynomialVector, Integer>> ps = P.pairs(P.polynomialVectors(), P.naturalIntegersGeometric());
        compareImplementations("shiftLeft(int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull PolynomialVector sum_alt(@NotNull Iterable<PolynomialVector> xs) {
        List<Polynomial> coordinates = toList(map(Polynomial::sum, transpose(map(v -> v, xs))));
        return coordinates.isEmpty() ? ZERO_DIMENSIONAL : of(coordinates);
    }

    private void propertiesSum() {
        initialize("sum(Iterable<PolynomialVector>)");
        Iterable<List<PolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.withScale(4).polynomialVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        for (List<PolynomialVector> vs : take(LIMIT, vss)) {
            PolynomialVector sum = sum(vs);
            sum.validate();
            assertEquals(vs, sum, sum_alt(vs));
            assertEquals(vs, sum.dimension(), head(vs).dimension());
        }

        Iterable<Pair<List<PolynomialVector>, List<PolynomialVector>>> ps = filterInfinite(
                q -> !q.a.equals(q.b),
                P.dependentPairs(vss, P::permutationsFinite)
        );
        for (Pair<List<PolynomialVector>, List<PolynomialVector>> p : take(LIMIT, ps)) {
            assertEquals(p, sum(p.a), sum(p.b));
        }

        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            assertEquals(v, sum(Collections.singletonList(v)), v);
        }

        Iterable<Pair<PolynomialVector, PolynomialVector>> ps2 = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.polynomialVectors(i))
                        )
                )
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, ps2)) {
            assertEquals(p, sum(Arrays.asList(p.a, p.b)), p.a.add(p.b));
        }

        for (List<Polynomial> xs : take(LIMIT, P.listsAtLeast(1, P.polynomials()))) {
            homomorphic(
                    ss -> toList(map(PolynomialVector::of, ss)),
                    PolynomialVector::of,
                    Polynomial::sum,
                    PolynomialVector::sum,
                    xs
            );
        }

        Iterable<List<PolynomialVector>> vssFail = filterInfinite(
                us -> !same(map(PolynomialVector::dimension, us)),
                P.listsAtLeast(1, P.polynomialVectors())
        );
        for (List<PolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                sum(vs);
                fail(vs);
            } catch (ArithmeticException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.polynomialVectors(i))
                )
        );
        for (List<PolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                sum(vs);
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void compareImplementationsSum() {
        Map<String, Function<List<PolynomialVector>, PolynomialVector>> functions = new LinkedHashMap<>();
        functions.put("alt", PolynomialVectorProperties::sum_alt);
        functions.put("standard", PolynomialVector::sum);
        Iterable<List<PolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.polynomialVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        compareImplementations("sum(Iterable<PolynomialVector>)", take(LIMIT, vss), functions, v -> P.reset());
    }

    private void propertiesDelta() {
        initialize("delta(Iterable<PolynomialVector>)");
        Iterable<List<PolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.withScale(4).polynomialVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        for (List<PolynomialVector> vs : take(LIMIT, vss)) {
            Iterable<PolynomialVector> deltas = delta(vs);
            deltas.forEach(PolynomialVector::validate);
            assertTrue(vs, all(v -> v.dimension() == head(vs).dimension(), deltas));
            assertEquals(vs, length(deltas), length(vs) - 1);
            List<PolynomialVector> reversed = reverse(map(PolynomialVector::negate, delta(reverse(vs))));
            aeqit(vs, deltas, reversed);
            testNoRemove(TINY_LIMIT, deltas);
            testHasNext(deltas);
        }

        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            assertTrue(v, isEmpty(delta(Collections.singletonList(v))));
        }

        Iterable<Pair<PolynomialVector, PolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.polynomialVectors(i))
                        )
                )
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, ps)) {
            aeqit(p, delta(Arrays.asList(p.a, p.b)), Collections.singletonList(p.b.subtract(p.a)));
        }

        Iterable<Iterable<PolynomialVector>> vss2 = P.withElement(
                repeat(ZERO_DIMENSIONAL),
                map(
                        p -> p.b, P.dependentPairsInfiniteSquareRootOrder(
                                P.positiveIntegersGeometric(),
                                i -> EP.prefixPermutations(QBarTesting.QEP.polynomialVectors(i))
                        )
                )
        );
        for (Iterable<PolynomialVector> vs : take(LIMIT, vss2)) {
            Iterable<PolynomialVector> deltas = delta(vs);
            List<PolynomialVector> deltaPrefix = toList(take(TINY_LIMIT, deltas));
            deltaPrefix.forEach(PolynomialVector::validate);
            assertEquals(vs, length(deltaPrefix), TINY_LIMIT);
            testNoRemove(TINY_LIMIT, deltas);
        }

        for (List<Polynomial> xs : take(LIMIT, P.listsAtLeast(1, P.polynomials()))) {
            homomorphic(
                    ss -> toList(map(PolynomialVector::of, ss)),
                    ss -> toList(map(PolynomialVector::of, ss)),
                    ss -> toList(Polynomial.delta(ss)),
                    vs -> toList(delta(vs)),
                    xs
            );
        }

        Iterable<List<PolynomialVector>> vssFail = filterInfinite(
                us -> !same(map(PolynomialVector::dimension, us)),
                P.listsAtLeast(1, P.polynomialVectors())
        );
        for (List<PolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                toList(delta(vs));
                fail(vs);
            } catch (ArithmeticException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.polynomialVectors(i))
                )
        );
        for (List<PolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                toList(delta(vs));
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesDot() {
        initialize("dot(PolynomialVector)");
        Iterable<Pair<PolynomialVector, PolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.polynomialVectors(i))
                        )
                )
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, ps)) {
            commutative(PolynomialVector::dot, p);
            homomorphic(
                    PolynomialVector::negate,
                    Function.identity(),
                    Polynomial::negate,
                    PolynomialVector::dot,
                    PolynomialVector::dot,
                    p
            );
            homomorphic(
                    Function.identity(),
                    PolynomialVector::negate,
                    Polynomial::negate,
                    PolynomialVector::dot,
                    PolynomialVector::dot,
                    p
            );
        }

        Iterable<Triple<Polynomial, PolynomialVector, PolynomialVector>> ts = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairs(ps, P.polynomials())
        );
        for (Triple<Polynomial, PolynomialVector, PolynomialVector> t : take(LIMIT, ts)) {
            assertEquals(t, t.b.dot(t.c).multiply(t.a), t.b.multiply(t.a).dot(t.c));
        }

        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            assertEquals(v, v.dot(zero(v.dimension())), Polynomial.ZERO);
            for (int i = 0; i < v.dimension(); i++) {
                assertEquals(v, v.dot(standard(v.dimension(), i)), v.get(i));
            }
        }

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            homomorphic(
                    PolynomialVector::of,
                    PolynomialVector::of,
                    Function.identity(),
                    Polynomial::multiply,
                    PolynomialVector::dot,
                    p
            );
        }

        Iterable<Pair<PolynomialVector, PolynomialVector>> psFail = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.polynomialVectors())
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, psFail)) {
            try {
                p.a.dot(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull Polynomial squaredLength_simplest(@NotNull PolynomialVector v) {
        return v.dot(v);
    }

    private void propertiesSquaredLength() {
        initialize("squaredLength()");
        for (PolynomialVector v : take(LIMIT, P.polynomialVectors())) {
            Polynomial squaredLength = v.squaredLength();
            assertEquals(v, squaredLength_simplest(v), squaredLength);
            assertNotEquals(v, squaredLength.signum(), -1);
            assertEquals(v, v.negate().squaredLength(), squaredLength);
        }

        for (Pair<PolynomialVector, Polynomial> p : take(LIMIT, P.pairs(P.polynomialVectors(), P.polynomials()))) {
            assertEquals(p, p.a.multiply(p.b).squaredLength(), p.a.squaredLength().multiply(p.b.pow(2)));
        }

        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            assertEquals(i, zero(i).squaredLength(), Polynomial.ZERO);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.subsetPairs(P.naturalIntegersGeometric()))) {
            assertEquals(p, standard(p.b, p.a).squaredLength(), Polynomial.ONE);
        }
    }

    private void compareImplementationsSquaredLength() {
        Map<String, Function<PolynomialVector, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", PolynomialVectorProperties::squaredLength_simplest);
        functions.put("standard", PolynomialVector::squaredLength);
        compareImplementations("squaredLength()", take(LIMIT, P.polynomialVectors()), functions, v -> P.reset());
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::polynomialVectors);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::polynomialVectors);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(PolynomialVector)");
        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::polynomialVectors);

        Iterable<Pair<PolynomialVector, PolynomialVector>> ps = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.polynomialVectors())
        );
        for (Pair<PolynomialVector, PolynomialVector> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.dimension(), p.b.dimension()));
        }
    }

    private void propertiesReadStrict() {
        initialize("readStrict(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                POLYNOMIAL_VECTOR_CHARS,
                P.polynomialVectors(),
                PolynomialVector::readStrict,
                PolynomialVector::validate,
                false,
                true
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, POLYNOMIAL_VECTOR_CHARS, P.polynomialVectors(), PolynomialVector::readStrict);
    }
}
