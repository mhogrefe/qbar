package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

import static mho.qbar.objects.RationalPolynomialVector.*;
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
        propertiesHasIntegralCoordinates();
        propertiesToPolynomialVector();
        propertiesGet();
        propertiesOf_List_RationalPolynomial();
        propertiesOf_RationalPolynomial();
        propertiesMaxCoordinateBitLength();
        propertiesDimension();
        propertiesIsZero();
        propertiesZero();
        propertiesStandard();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead();
        propertiesFindIn();
        propertiesToString();
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            List<RationalPolynomial> ps = toList(v);
            assertTrue(v, all(p -> p != null, ps));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<RationalPolynomial> qs) -> of(qs), v);
            testNoRemove(v);
            testHasNext(v);
        }
    }

    private void propertiesHasIntegralCoordinates() {
        initialize("hasIntegralCoordinates()");
        for (RationalPolynomialVector v : take(LIMIT, P.rationalPolynomialVectors())) {
            v.hasIntegralCoordinates();
        }

        Iterable<RationalPolynomialVector> vs = map(
                PolynomialVector::toRationalPolynomialVector,
                P.polynomialVectors()
        );
        for (RationalPolynomialVector v : take(LIMIT, vs)) {
            assertTrue(v, v.hasIntegralCoordinates());
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
                p -> any(c -> !c.hasIntegralCoefficients(), p),
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
                v -> P.uniformSample(toList(range(0, v.dimension() - 1)))
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

    private void propertiesRead() {
        initialize("read(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                RATIONAL_POLYNOMIAL_VECTOR_CHARS,
                P.rationalPolynomialVectors(),
                RationalPolynomialVector::read,
                RationalPolynomialVector::validate,
                false
        );
    }

    private void propertiesFindIn() {
        initialize("findIn(String)");
        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.rationalPolynomialVectors(),
                RationalPolynomialVector::read,
                RationalPolynomialVector::findIn,
                RationalPolynomialVector::validate
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(
                LIMIT,
                RATIONAL_POLYNOMIAL_VECTOR_CHARS,
                P.rationalPolynomialVectors(),
                RationalPolynomialVector::read
        );
    }
}
