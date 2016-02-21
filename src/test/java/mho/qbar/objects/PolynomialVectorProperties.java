package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static mho.qbar.objects.PolynomialVector.*;
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
        propertiesMaxCoordinateBitLength();
        propertiesDimension();
        propertiesIsZero();
        propertiesZero();
        propertiesStandard();
        propertiesAdd();
        propertiesNegate();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead();
        propertiesFindIn();
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
                v -> P.uniformSample(toList(range(0, v.dimension() - 1)))
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
            } catch (NullPointerException ignored) {
            }
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
        compareImplementations("subtract(PolynomialVector)", take(LIMIT, ps), functions);
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

    private void propertiesRead() {
        initialize("read(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                POLYNOMIAL_VECTOR_CHARS,
                P.polynomialVectors(),
                PolynomialVector::read,
                PolynomialVector::validate,
                false
        );
    }

    private void propertiesFindIn() {
        initialize("findIn(String)");
        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.polynomialVectors(),
                PolynomialVector::read,
                PolynomialVector::findIn,
                PolynomialVector::validate
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, POLYNOMIAL_VECTOR_CHARS, P.polynomialVectors(), PolynomialVector::read);
    }
}
