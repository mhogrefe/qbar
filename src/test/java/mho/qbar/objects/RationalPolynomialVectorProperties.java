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

import static mho.qbar.objects.RationalPolynomialVector.*;
import static mho.qbar.objects.RationalPolynomialVector.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.compare;
import static mho.wheels.testing.Testing.*;

public class RationalPolynomialVectorProperties extends QBarTestProperties {
    private static final @NotNull String RATIONAL_POLYNOMIAL_VECTOR_CHARS = " *+,-/0123456789[]^x";

    public RationalPolynomialVectorProperties() {
        super("RationalPolynomialVector");
    }

    @Override
    protected void testBothModes() {
        propertiesIterator();
        propertiesOnlyHasIntegralCoordinates();
        propertiesToPolynomialVector();
        propertiesGet();
        propertiesOf_List_RationalPolynomial();
        propertiesOf_RationalPolynomial();
        propertiesOf_RationalVector();
        propertiesMaxCoordinateBitLength();
        propertiesDimension();
        propertiesIsZero();
        propertiesZero();
        propertiesStandard();
        propertiesAdd();
        propertiesNegate();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesMultiply_RationalPolynomial();
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
        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            List<RationalPolynomial> ps = toList(v);
            assertTrue(v, all(Objects::nonNull, ps));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<RationalPolynomial> qs) -> of(qs), v);
            testNoRemove(v);
            testHasNext(v);
        }
    }

    private void propertiesOnlyHasIntegralCoordinates() {
        initialize("onlyHasIntegralCoordinates()");
        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            v.onlyHasIntegralCoordinates();
        }

        Iterable<RationalPolynomialVector> vs = map(
                PolynomialVector::toRationalPolynomialVector,
                P.polynomialVectors()
        );
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
            assertTrue(v, v.onlyHasIntegralCoordinates());
        }
    }

    private void propertiesToPolynomialVector() {
        initialize("toPolynomialVector()");
        Iterable<RationalPolynomialVector> vs = map(
                PolynomialVector::toRationalPolynomialVector,
                P.polynomialVectors()
        );
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
            PolynomialVector u = v.toPolynomialVector();
            assertEquals(v, v.toString(), u.toString());
            assertEquals(v, v.dimension(), u.dimension());
            inverse(RationalPolynomialVector::toPolynomialVector, PolynomialVector::toRationalPolynomialVector, v);
        }

        Iterable<RationalPolynomialVector> psFail = filterInfinite(
                p -> any(c -> !c.onlyHasIntegralCoefficients(), p),
                P.rationalPolynomialVectors()
        );
        for (RationalPolynomialVector v : take(LIMIT, psFail)) {
            try {
                v.toPolynomialVector();
                fail(v);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesGet() {
        initialize("get(int)");
        Iterable<Pair<RationalPolynomialVector, Integer>> ps = P.dependentPairs(
                P.rationalPolynomialVectorsAtLeast(1),
                v -> P.uniformSample(toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, v.dimension() - 1)))
        );
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial x = p.a.get(p.b);
            assertEquals(p, x, toList(p.a).get(p.b));
        }

        Iterable<Pair<RationalPolynomialVector, Integer>> psFail = filterInfinite(
                p -> p.b < 0 || p.b >= p.a.dimension(),
                P.pairs(P.rationalPolynomialVectors(), P.integers())
        );
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.get(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesOf_List_RationalPolynomial() {
        initialize("of(List<RationalPolynomial>)");
        for (List<RationalPolynomial> ps : take(LIMIT, P.lists(P.rationalPolynomials()))) {
            RationalPolynomialVector v = of(ps);
            v.validate();
            //noinspection Convert2MethodRef
            inverse(RationalPolynomialVector::of, (RationalPolynomialVector u) -> toList(u), ps);
            assertEquals(ps, v.dimension(), ps.size());
        }

        for (List<RationalPolynomial> ps : take(LIMIT, P.listsWithElement(null, P.rationalPolynomials()))) {
            try {
                of(ps);
                fail(ps);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesOf_RationalPolynomial() {
        initialize("of(RationalPolynomial)");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            RationalPolynomialVector v = of(p);
            v.validate();
            assertEquals(p, v.dimension(), 1);
            assertEquals(p, v.get(0), p);
            inverse(RationalPolynomialVector::of, (RationalPolynomialVector u) -> u.get(0), p);
        }
    }

    private void propertiesOf_RationalVector() {
        initialize("of(RationalVector)");
        for (RationalVector v : take(LIMIT, P.rationalVectors())) {
            RationalPolynomialVector pv = of(v);
            pv.validate();
            assertEquals(v, pv.dimension(), v.dimension());
            assertEquals(v, pv.toString(), pv.toString());
        }
    }

    private void propertiesMaxCoordinateBitLength() {
        initialize("maxCoordinateBitLength()");
        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            assertTrue(v, v.maxCoordinateBitLength() >= 0);
            homomorphic(
                    RationalPolynomialVector::negate,
                    Function.identity(),
                    RationalPolynomialVector::maxCoordinateBitLength,
                    RationalPolynomialVector::maxCoordinateBitLength,
                    v
            );
        }
    }

    private void propertiesDimension() {
        initialize("dimension()");
        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            int dimension = v.dimension();
            assertTrue(v, dimension >= 0);
            assertEquals(v, length(v), dimension);
        }
    }

    private void propertiesIsZero() {
        initialize("isZero()");
        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            assertEquals(v, v.isZero(), zero(v.dimension()).equals(v));
        }
    }

    private void propertiesZero() {
        initialize("zero(int)");
        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            RationalPolynomialVector zero = zero(i);
            zero.validate();
            assertEquals(i, zero.dimension(), i);
            inverse(RationalPolynomialVector::zero, RationalPolynomialVector::dimension, i);
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
            RationalPolynomialVector standard = standard(p.b, p.a);
            standard.validate();
            assertEquals(p, standard.dimension(), p.b);
            for (int i = 0; i < p.b; i++) {
                assertEquals(p, standard.get(i), i == p.a ? RationalPolynomial.ONE : RationalPolynomial.ZERO);
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
        initialize("add(RationalPolynomialVector)");
        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.rationalPolynomialVectors(i))
                        )
                )
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, ps)) {
            RationalPolynomialVector sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum.dimension(), p.a.dimension());
            commutative(RationalPolynomialVector::add, p);
            inverse(v -> v.add(p.b), (RationalPolynomialVector v) -> v.subtract(p.b), p.a);
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            fixedPoint(u -> zero(u.dimension()).add(u), v);
            fixedPoint(u -> u.add(zero(u.dimension())), v);
            assertTrue(v, v.add(v.negate()).isZero());
        }

        Iterable<
                Triple<RationalPolynomialVector, RationalPolynomialVector, RationalPolynomialVector>
        > ts = P.withElement(
                new Triple<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        t -> t.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.triples(P.rationalPolynomialVectors(i))
                        )
                )
        );
        for (Triple<
                RationalPolynomialVector,
                RationalPolynomialVector,
                RationalPolynomialVector
        > t : take(LIMIT, ts)) {
            associative(RationalPolynomialVector::add, t);
        }

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            homomorphic(
                    RationalPolynomialVector::of,
                    RationalPolynomialVector::of,
                    RationalPolynomialVector::of,
                    RationalPolynomial::add,
                    RationalPolynomialVector::add,
                    p
            );
        }

        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> psFail = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.rationalPolynomialVectors())
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, psFail)) {
            try {
                p.a.add(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            RationalPolynomialVector negative = v.negate();
            negative.validate();
            assertEquals(v, v.dimension(), negative.dimension());
            involution(RationalPolynomialVector::negate, v);
            assertTrue(v, v.add(negative).isZero());
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            homomorphic(
                    RationalPolynomialVector::of,
                    RationalPolynomialVector::of,
                    RationalPolynomial::negate,
                    RationalPolynomialVector::negate,
                    p
            );
        }

        Iterable<RationalPolynomialVector> vs = filterInfinite(u -> !u.isZero(), P.rationalPolynomialVectors());
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
            assertNotEquals(v, v, v.negate());
        }
    }

    private static @NotNull RationalPolynomialVector subtract_simplest(
            @NotNull RationalPolynomialVector a,
            @NotNull RationalPolynomialVector b
    ) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(RationalPolynomialVector)");
        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.rationalPolynomialVectors(i))
                        )
                )
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, ps)) {
            RationalPolynomialVector difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, subtract_simplest(p.a, p.b), difference);
            assertEquals(p, difference.dimension(), p.a.dimension());
            antiCommutative(RationalPolynomialVector::subtract, RationalPolynomialVector::negate, p);
            inverse(v -> v.subtract(p.b), (RationalPolynomialVector v) -> v.add(p.b), p.a);
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            assertEquals(v, zero(v.dimension()).subtract(v), v.negate());
            fixedPoint(u -> u.subtract(zero(u.dimension())), v);
            assertTrue(v, v.subtract(v).isZero());
        }

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            homomorphic(
                    RationalPolynomialVector::of,
                    RationalPolynomialVector::of,
                    RationalPolynomialVector::of,
                    RationalPolynomial::subtract,
                    RationalPolynomialVector::subtract,
                    p
            );
        }

        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> psFail = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.rationalPolynomialVectors())
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, psFail)) {
            try {
                p.a.subtract(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsSubtract() {
        Map<
                String,
                Function<Pair<RationalPolynomialVector, RationalPolynomialVector>, RationalPolynomialVector>
        > functions = new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.rationalPolynomialVectors(i))
                        )
                )
        );
        compareImplementations("subtract(RationalPolynomialVector)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesMultiply_RationalPolynomial() {
        initialize("multiply(RationalPolynomial)");
        Iterable<Pair<RationalPolynomialVector, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomialVectors(),
                P.rationalPolynomials()
        );
        for (Pair<RationalPolynomialVector, RationalPolynomial> p : take(LIMIT, ps)) {
            RationalPolynomialVector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            fixedPoint(u -> u.multiply(RationalPolynomial.ONE), v);
            assertTrue(v, v.multiply(RationalPolynomial.ZERO).isZero());
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(v -> v.multiply(p), ZERO_DIMENSIONAL);
        }

        ps = P.pairs(P.rationalPolynomialVectors(1), P.rationalPolynomials());
        for (Pair<RationalPolynomialVector, RationalPolynomial> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b), of(p.b).multiply(p.a.get(0)));
        }

        Iterable<Pair<RationalPolynomial, Integer>> ps2 = P.pairsLogarithmicOrder(
                P.rationalPolynomials(),
                P.naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            homomorphic(
                    RationalPolynomialVector::of,
                    Function.identity(),
                    RationalPolynomialVector::of,
                    RationalPolynomial::multiply,
                    RationalPolynomialVector::multiply,
                    p
            );
        }
    }

    private void propertiesMultiply_Rational() {
        initialize("multiply(Rational)");
        Iterable<Pair<RationalPolynomialVector, Rational>> ps = P.pairs(P.rationalPolynomialVectors(), P.rationals());
        for (Pair<RationalPolynomialVector, Rational> p : take(LIMIT, ps)) {
            RationalPolynomialVector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        ps = P.pairs(P.rationalPolynomialVectors(), P.nonzeroRationals());
        for (Pair<RationalPolynomialVector, Rational> p : take(LIMIT, ps)) {
            inverse(v -> v.multiply(p.b), (RationalPolynomialVector v) -> v.divide(p.b), p.a);
            assertEquals(p, p.a.multiply(p.b), p.a.divide(p.b.invert()));
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            fixedPoint(u -> u.multiply(Rational.ONE), v);
            assertTrue(v, v.multiply(Rational.ZERO).isZero());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            fixedPoint(v -> v.multiply(r), ZERO_DIMENSIONAL);
        }

        ps = P.pairs(P.rationalPolynomialVectors(1), P.rationals());
        for (Pair<RationalPolynomialVector, Rational> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b), of(RationalPolynomial.of(p.b)).multiply(p.a.get(0)));
        }

        Iterable<Pair<Rational, Integer>> ps2 = P.pairsLogarithmicOrder(P.rationals(), P.naturalIntegersGeometric());
        for (Pair<Rational, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }

        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            homomorphic(
                    RationalPolynomialVector::of,
                    Function.identity(),
                    RationalPolynomialVector::of,
                    RationalPolynomial::multiply,
                    RationalPolynomialVector::multiply,
                    p
            );
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        Iterable<Pair<RationalPolynomialVector, BigInteger>> ps = P.pairs(
                P.rationalPolynomialVectors(),
                P.bigIntegers()
        );
        for (Pair<RationalPolynomialVector, BigInteger> p : take(LIMIT, ps)) {
            RationalPolynomialVector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        ps = P.pairs(P.rationalPolynomialVectors(), P.nonzeroBigIntegers());
        for (Pair<RationalPolynomialVector, BigInteger> p : take(LIMIT, ps)) {
            inverse(v -> v.multiply(p.b), (RationalPolynomialVector v) -> v.divide(p.b), p.a);
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            fixedPoint(u -> u.multiply(BigInteger.ONE), v);
            assertTrue(v, v.multiply(BigInteger.ZERO).isZero());
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            fixedPoint(v -> v.multiply(i), ZERO_DIMENSIONAL);
        }

        ps = P.pairs(P.rationalPolynomialVectors(1), P.bigIntegers());
        for (Pair<RationalPolynomialVector, BigInteger> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b), of(RationalPolynomial.of(Rational.of(p.b))).multiply(p.a.get(0)));
        }

        Iterable<Pair<BigInteger, Integer>> ps2 = P.pairsLogarithmicOrder(
                P.bigIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }

        for (Pair<RationalPolynomial, BigInteger> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.bigIntegers()))) {
            homomorphic(
                    RationalPolynomialVector::of,
                    Function.identity(),
                    RationalPolynomialVector::of,
                    RationalPolynomial::multiply,
                    RationalPolynomialVector::multiply,
                    p
            );
        }
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        Iterable<Pair<RationalPolynomialVector, Integer>> ps = P.pairs(P.rationalPolynomialVectors(), P.integers());
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            RationalPolynomialVector v = p.a.multiply(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
        }

        ps = P.pairs(P.rationalPolynomialVectors(), P.nonzeroIntegers());
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            inverse(v -> v.multiply(p.b), (RationalPolynomialVector v) -> v.divide(p.b), p.a);
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            fixedPoint(u -> u.multiply(1), v);
            assertTrue(v, v.multiply(0).isZero());
        }

        for (int i : take(LIMIT, P.integers())) {
            fixedPoint(v -> v.multiply(i), ZERO_DIMENSIONAL);
        }

        ps = P.pairs(P.rationalPolynomialVectors(1), P.integers());
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b), of(RationalPolynomial.of(Rational.of(p.b))).multiply(p.a.get(0)));
        }

        Iterable<Pair<Integer, Integer>> ps2 = P.pairsLogarithmicOrder(P.integers(), P.naturalIntegersGeometric());
        for (Pair<Integer, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p, zero(p.b).multiply(p.a).isZero());
        }

        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.integers()))) {
            homomorphic(
                    RationalPolynomialVector::of,
                    Function.identity(),
                    RationalPolynomialVector::of,
                    RationalPolynomial::multiply,
                    RationalPolynomialVector::multiply,
                    p
            );
        }
    }

    private void propertiesDivide_Rational() {
        initialize("divide(Rational)");
        Iterable<Pair<RationalPolynomialVector, Rational>> ps = P.pairs(
                P.rationalPolynomialVectors(),
                P.nonzeroRationals()
        );
        for (Pair<RationalPolynomialVector, Rational> p : take(LIMIT, ps)) {
            RationalPolynomialVector v = p.a.divide(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
            inverse(u -> u.divide(p.b), (RationalPolynomialVector u) -> u.multiply(p.b), p.a);
            assertEquals(p, v, p.a.multiply(p.b.invert()));
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            fixedPoint(u -> u.divide(Rational.ONE), v);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            fixedPoint(v -> v.divide(r), ZERO_DIMENSIONAL);
        }

        Iterable<Pair<Rational, Integer>> ps2 = P.pairsLogarithmicOrder(
                P.nonzeroRationals(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p, zero(p.b).divide(p.a).isZero());
        }

        Iterable<Pair<RationalPolynomial, Rational>> ps3 = P.pairs(P.rationalPolynomials(), P.nonzeroRationals());
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps3)) {
            homomorphic(
                    RationalPolynomialVector::of,
                    Function.identity(),
                    RationalPolynomialVector::of,
                    RationalPolynomial::divide,
                    RationalPolynomialVector::divide,
                    p
            );
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            try {
                v.divide(Rational.ZERO);
                fail(v);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_BigInteger() {
        initialize("divide(BigInteger)");
        Iterable<Pair<RationalPolynomialVector, BigInteger>> ps = P.pairs(
                P.rationalPolynomialVectors(),
                P.nonzeroBigIntegers()
        );
        for (Pair<RationalPolynomialVector, BigInteger> p : take(LIMIT, ps)) {
            RationalPolynomialVector v = p.a.divide(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
            inverse(u -> u.divide(p.b), (RationalPolynomialVector u) -> u.multiply(p.b), p.a);
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            assertEquals(v, v.divide(BigInteger.ONE), v);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            fixedPoint(v -> v.divide(i), ZERO_DIMENSIONAL);
        }

        Iterable<Pair<BigInteger, Integer>> ps2 = P.pairsLogarithmicOrder(
                P.nonzeroBigIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p, zero(p.b).divide(p.a).isZero());
        }

        Iterable<Pair<RationalPolynomial, BigInteger>> ps3 = P.pairs(P.rationalPolynomials(), P.nonzeroBigIntegers());
        for (Pair<RationalPolynomial, BigInteger> p : take(LIMIT, ps3)) {
            homomorphic(
                    RationalPolynomialVector::of,
                    Function.identity(),
                    RationalPolynomialVector::of,
                    RationalPolynomial::divide,
                    RationalPolynomialVector::divide,
                    p
            );
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            try {
                v.divide(BigInteger.ZERO);
                fail(v);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_int() {
        initialize("divide(int)");
        Iterable<Pair<RationalPolynomialVector, Integer>> ps = P.pairs(
                P.rationalPolynomialVectors(),
                P.nonzeroIntegers()
        );
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            RationalPolynomialVector v = p.a.divide(p.b);
            v.validate();
            assertEquals(p, v.dimension(), p.a.dimension());
            inverse(u -> u.divide(p.b), (RationalPolynomialVector u) -> u.multiply(p.b), p.a);
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            assertEquals(v, v.divide(1), v);
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            fixedPoint(v -> v.divide(i), ZERO_DIMENSIONAL);
        }

        Iterable<Pair<Integer, Integer>> ps2 = P.pairsLogarithmicOrder(
                P.nonzeroIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Integer, Integer> p : take(LIMIT, ps2)) {
            assertTrue(p, zero(p.b).divide(p.a).isZero());
        }

        Iterable<Pair<RationalPolynomial, Integer>> ps3 = P.pairs(P.rationalPolynomials(), P.nonzeroIntegers());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps3)) {
            homomorphic(
                    RationalPolynomialVector::of,
                    Function.identity(),
                    RationalPolynomialVector::of,
                    RationalPolynomial::divide,
                    RationalPolynomialVector::divide,
                    p
            );
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            try {
                v.divide(0);
                fail(v);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull RationalPolynomialVector shiftLeft_simplest(
            @NotNull RationalPolynomialVector v,
            int bits
    ) {
        if (bits < 0) {
            return v.divide(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return v.multiply(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        Iterable<Pair<RationalPolynomialVector, Integer>> ps = P.pairs(
                P.rationalPolynomialVectors(),
                P.integersGeometric()
        );
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            RationalPolynomialVector shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p, map(RationalPolynomial::signum, p.a), map(RationalPolynomial::signum, shifted));
            assertEquals(p, p.a.dimension(), shifted.dimension());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
            inverse(a -> a.shiftLeft(p.b), (RationalPolynomialVector v) -> v.shiftRight(p.b), p.a);
            assertEquals(p, shifted, p.a.shiftRight(-p.b));
            homomorphic(
                    RationalPolynomialVector::negate,
                    Function.identity(),
                    RationalPolynomialVector::negate,
                    RationalPolynomialVector::shiftLeft,
                    RationalPolynomialVector::shiftLeft,
                    p
            );
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            fixedPoint(u -> u.shiftLeft(0), v);
        }

        ps = P.pairs(P.rationalPolynomialVectors(), P.naturalIntegersGeometric());
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.shiftLeft(p.b), p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
        }

        Iterable<Pair<RationalPolynomial, Integer>> ps2 = P.pairs(P.rationalPolynomials(), P.integersGeometric());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps2)) {
            homomorphic(
                    RationalPolynomialVector::of,
                    Function.identity(),
                    RationalPolynomialVector::of,
                    RationalPolynomial::shiftLeft,
                    RationalPolynomialVector::shiftLeft,
                    p
            );
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<RationalPolynomialVector, Integer>, RationalPolynomialVector>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        Iterable<Pair<RationalPolynomialVector, Integer>> ps = P.pairs(
                P.rationalPolynomialVectors(),
                P.integersGeometric()
        );
        compareImplementations("shiftLeft(int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull RationalPolynomialVector shiftRight_simplest(
            @NotNull RationalPolynomialVector v,
            int bits
    ) {
        if (bits < 0) {
            return v.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return v.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftRight() {
        initialize("shiftRight(int)");
        Iterable<Pair<RationalPolynomialVector, Integer>> ps = P.pairs(
                P.rationalPolynomialVectors(),
                P.integersGeometric()
        );
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            RationalPolynomialVector shifted = p.a.shiftRight(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftRight_simplest(p.a, p.b));
            aeqit(p, map(RationalPolynomial::signum, p.a), map(RationalPolynomial::signum, shifted));
            assertEquals(p, p.a.dimension(), shifted.dimension());
            assertEquals(p, p.a.negate().shiftRight(p.b), shifted.negate());
            inverse(a -> a.shiftRight(p.b), (RationalPolynomialVector v) -> v.shiftLeft(p.b), p.a);
            assertEquals(p, shifted, p.a.shiftLeft(-p.b));
            homomorphic(
                    RationalPolynomialVector::negate,
                    Function.identity(),
                    RationalPolynomialVector::negate,
                    RationalPolynomialVector::shiftRight,
                    RationalPolynomialVector::shiftRight,
                    p
            );
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            assertEquals(v, v.shiftRight(0), v);
        }

        ps = P.pairs(P.rationalPolynomialVectors(), P.naturalIntegersGeometric());
        for (Pair<RationalPolynomialVector, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.shiftRight(p.b), p.a.divide(BigInteger.ONE.shiftLeft(p.b)));
        }

        Iterable<Pair<RationalPolynomial, Integer>> ps2 = P.pairs(P.rationalPolynomials(), P.integersGeometric());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps2)) {
            homomorphic(
                    RationalPolynomialVector::of,
                    Function.identity(),
                    RationalPolynomialVector::of,
                    RationalPolynomial::shiftRight,
                    RationalPolynomialVector::shiftRight,
                    p
            );
        }
    }

    private void compareImplementationsShiftRight() {
        Map<String, Function<Pair<RationalPolynomialVector, Integer>, RationalPolynomialVector>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> shiftRight_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftRight(p.b));
        Iterable<Pair<RationalPolynomialVector, Integer>> ps = P.pairs(
                P.rationalPolynomialVectors(),
                P.integersGeometric()
        );
        compareImplementations("shiftRight(int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull RationalPolynomialVector sum_alt(@NotNull Iterable<RationalPolynomialVector> xs) {
        List<RationalPolynomial> coordinates = toList(map(RationalPolynomial::sum, transpose(map(v -> v, xs))));
        return coordinates.isEmpty() ? ZERO_DIMENSIONAL : of(coordinates);
    }

    private void propertiesSum() {
        initialize("sum(Iterable<RationalPolynomialVector>)");
        Iterable<List<RationalPolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.withScale(4).rationalPolynomialVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vss)) {
            RationalPolynomialVector sum = sum(vs);
            sum.validate();
            assertEquals(vs, sum, sum_alt(vs));
            assertEquals(vs, sum.dimension(), head(vs).dimension());
        }

        Iterable<Pair<List<RationalPolynomialVector>, List<RationalPolynomialVector>>> ps = filterInfinite(
                q -> !q.a.equals(q.b),
                P.dependentPairs(vss, P::permutationsFinite)
        );
        for (Pair<List<RationalPolynomialVector>, List<RationalPolynomialVector>> p : take(LIMIT, ps)) {
            assertEquals(p, sum(p.a), sum(p.b));
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            assertEquals(v, sum(Collections.singletonList(v)), v);
        }

        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> ps2 = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.rationalPolynomialVectors(i))
                        )
                )
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, ps2)) {
            assertEquals(p, sum(Arrays.asList(p.a, p.b)), p.a.add(p.b));
        }

        for (List<RationalPolynomial> rs : take(LIMIT, P.listsAtLeast(1, P.rationalPolynomials()))) {
            homomorphic(
                    ss -> toList(map(RationalPolynomialVector::of, ss)),
                    RationalPolynomialVector::of,
                    RationalPolynomial::sum,
                    RationalPolynomialVector::sum,
                    rs
            );
        }

        Iterable<List<RationalPolynomialVector>> vssFail = filterInfinite(
                us -> !same(map(RationalPolynomialVector::dimension, us)),
                P.listsAtLeast(1, P.rationalPolynomialVectors())
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                sum(vs);
                fail(vs);
            } catch (ArithmeticException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.rationalPolynomialVectors(i))
                )
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                sum(vs);
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void compareImplementationsSum() {
        Map<String, Function<List<RationalPolynomialVector>, RationalPolynomialVector>> functions =
                new LinkedHashMap<>();
        functions.put("alt", RationalPolynomialVectorProperties::sum_alt);
        functions.put("standard", RationalPolynomialVector::sum);
        Iterable<List<RationalPolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.withScale(4).rationalPolynomialVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        compareImplementations("sum(Iterable<RationalPolynomialVector>)", take(LIMIT, vss), functions, v -> P.reset());
    }

    private void propertiesDelta() {
        initialize("delta(Iterable<RationalPolynomialVector>)");
        Iterable<List<RationalPolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.withScale(4).rationalPolynomialVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.positiveIntegersGeometric())
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vss)) {
            Iterable<RationalPolynomialVector> deltas = delta(vs);
            deltas.forEach(RationalPolynomialVector::validate);
            assertTrue(vs, all(v -> v.dimension() == head(vs).dimension(), deltas));
            assertEquals(vs, length(deltas), length(vs) - 1);
            List<RationalPolynomialVector> reversed = reverse(
                    map(RationalPolynomialVector::negate, delta(reverse(vs)))
            );
            aeqit(vs, deltas, reversed);
            testNoRemove(TINY_LIMIT, deltas);
            testHasNext(deltas);
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            assertTrue(v, isEmpty(delta(Collections.singletonList(v))));
        }

        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.rationalPolynomialVectors(i))
                        )
                )
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, ps)) {
            aeqit(p, delta(Arrays.asList(p.a, p.b)), Collections.singletonList(p.b.subtract(p.a)));
        }

        Iterable<Iterable<RationalPolynomialVector>> vss2 = P.withElement(
                repeat(ZERO_DIMENSIONAL),
                map(
                        p -> p.b, P.dependentPairsInfiniteSquareRootOrder(
                                P.positiveIntegersGeometric(),
                                i -> EP.prefixPermutations(QBarTesting.QEP.rationalPolynomialVectors(i))
                        )
                )
        );
        for (Iterable<RationalPolynomialVector> vs : take(LIMIT, vss2)) {
            Iterable<RationalPolynomialVector> deltas = delta(vs);
            List<RationalPolynomialVector> deltaPrefix = toList(take(TINY_LIMIT, deltas));
            deltaPrefix.forEach(RationalPolynomialVector::validate);
            assertEquals(vs, length(deltaPrefix), TINY_LIMIT);
            testNoRemove(TINY_LIMIT, deltas);
        }

        for (List<RationalPolynomial> rs : take(LIMIT, P.listsAtLeast(1, P.rationalPolynomials()))) {
            homomorphic(
                    ss -> toList(map(RationalPolynomialVector::of, ss)),
                    ss -> toList(map(RationalPolynomialVector::of, ss)),
                    ss -> toList(RationalPolynomial.delta(ss)),
                    vs -> toList(delta(vs)),
                    rs
            );
        }

        Iterable<List<RationalPolynomialVector>> vssFail = filterInfinite(
                us -> !same(map(RationalPolynomialVector::dimension, us)),
                P.listsAtLeast(1, P.rationalPolynomialVectors())
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                toList(delta(vs));
                fail(vs);
            } catch (ArithmeticException ignored) {}
        }

        vssFail = map(
                p -> p.b,
                P.dependentPairsInfiniteSquareRootOrder(
                        P.positiveIntegersGeometric(),
                        i -> P.listsWithElement(null, P.rationalPolynomialVectors(i))
                )
        );
        for (List<RationalPolynomialVector> vs : take(LIMIT, vssFail)) {
            try {
                toList(delta(vs));
                fail(vs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesDot() {
        initialize("dot(RationalPolynomialVector)");
        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> ps = P.withElement(
                new Pair<>(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL),
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.positiveIntegersGeometric(),
                                i -> P.pairs(P.withScale(4).rationalPolynomialVectors(i))
                        )
                )
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, ps)) {
            commutative(RationalPolynomialVector::dot, p);
            homomorphic(
                    RationalPolynomialVector::negate,
                    Function.identity(),
                    RationalPolynomial::negate,
                    RationalPolynomialVector::dot,
                    RationalPolynomialVector::dot,
                    p
            );
            homomorphic(
                    Function.identity(),
                    RationalPolynomialVector::negate,
                    RationalPolynomial::negate,
                    RationalPolynomialVector::dot,
                    RationalPolynomialVector::dot,
                    p
            );
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomialVector, RationalPolynomialVector>> ts = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairs(ps, P.withScale(4).rationalPolynomials())
        );
        for (Triple<RationalPolynomial, RationalPolynomialVector, RationalPolynomialVector> t : take(LIMIT, ts)) {
            assertEquals(t, t.b.dot(t.c).multiply(t.a), t.b.multiply(t.a).dot(t.c));
        }

        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            assertEquals(v, v.dot(zero(v.dimension())), RationalPolynomial.ZERO);
            for (int i = 0; i < v.dimension(); i++) {
                assertEquals(v, v.dot(standard(v.dimension(), i)), v.get(i));
            }
        }

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            homomorphic(
                    RationalPolynomialVector::of,
                    RationalPolynomialVector::of,
                    Function.identity(),
                    RationalPolynomial::multiply,
                    RationalPolynomialVector::dot,
                    p
            );
        }

        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> psFail = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.rationalPolynomialVectors())
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, psFail)) {
            try {
                p.a.dot(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull RationalPolynomial squaredLength_simplest(@NotNull RationalPolynomialVector v) {
        return v.dot(v);
    }

    private void propertiesSquaredLength() {
        initialize("squaredLength()");
        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            RationalPolynomial squaredLength = v.squaredLength();
            assertEquals(v, squaredLength_simplest(v), squaredLength);
            assertNotEquals(v, squaredLength.signum(), -1);
            assertEquals(v, v.negate().squaredLength(), squaredLength);
        }

        Iterable<Pair<RationalPolynomialVector, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomialVectors(),
                P.rationalPolynomials()
        );
        for (Pair<RationalPolynomialVector, RationalPolynomial> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiply(p.b).squaredLength(), p.a.squaredLength().multiply(p.b.pow(2)));
        }

        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            assertEquals(i, zero(i).squaredLength(), RationalPolynomial.ZERO);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.subsetPairs(P.naturalIntegersGeometric()))) {
            assertEquals(p, standard(p.b, p.a).squaredLength(), RationalPolynomial.ONE);
        }
    }

    private void compareImplementationsSquaredLength() {
        Map<String, Function<RationalPolynomialVector, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", RationalPolynomialVectorProperties::squaredLength_simplest);
        functions.put("standard", RationalPolynomialVector::squaredLength);
        compareImplementations(
                "squaredLength()",
                take(LIMIT, P.rationalPolynomialVectors()),
                functions,
                v -> P.reset()
        );
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::rationalPolynomialVectors);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::rationalPolynomialVectors);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(RationalPolynomialVector)");
        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::rationalPolynomialVectors);

        Iterable<Pair<RationalPolynomialVector, RationalPolynomialVector>> ps = filterInfinite(
                p -> p.a.dimension() != p.b.dimension(),
                P.pairs(P.rationalPolynomialVectors())
        );
        for (Pair<RationalPolynomialVector, RationalPolynomialVector> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.dimension(), p.b.dimension()));
        }
    }

    private void propertiesReadStrict() {
        initialize("readStrict(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                RATIONAL_POLYNOMIAL_VECTOR_CHARS,
                P.rationalPolynomialVectors(),
                RationalPolynomialVector::readStrict,
                RationalPolynomialVector::validate,
                false,
                true
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(
                LIMIT,
                RATIONAL_POLYNOMIAL_VECTOR_CHARS,
                P.rationalPolynomialVectors(),
                RationalPolynomialVector::readStrict
        );
    }
}
