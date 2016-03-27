package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static mho.qbar.objects.MultivariatePolynomial.ZERO;
import static mho.qbar.objects.MultivariatePolynomial.of;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.compare;
import static mho.wheels.testing.Testing.*;

public class MultivariatePolynomialProperties extends QBarTestProperties {
    private static final @NotNull String MULTIVARIATE_POLYNOMIAL_CHARS = "*+-0123456789^abcdefghijklmnopqrstuvwxyz";

    public MultivariatePolynomialProperties() {
        super("MultivariatePolynomial");
    }

    @Override
    protected void testBothModes() {
        propertiesIterator();
        propertiesCoefficient();
        propertiesOf_List_Pair_ExponentVector_BigInteger();
        propertiesOf_ExponentVector_BigInteger();
        compareImplementationsOf_ExponentVector_BigInteger();
        propertiesOf_BigInteger();
        propertiesOf_int();
        propertiesOf_Polynomial_Variable();
        propertiesToPolynomial();
        propertiesVariables();
        propertiesVariableCount();
        propertiesTermCount();
        propertiesMaxCoefficientBitLength();
        propertiesDegree();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead();
        propertiesFindIn();
        propertiesToString();
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            List<Pair<ExponentVector, BigInteger>> terms = toList(p);
            assertFalse(p, any(t -> t == null || t.a == null || t.b == null, terms));
            //noinspection RedundantCast
            assertTrue(p, increasing((Iterable<ExponentVector>) map(t -> t.a, terms)));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<Pair<ExponentVector, BigInteger>> ts) -> of(ts), p);
            testNoRemove(p);
            testHasNext(p);
        }
    }

    private void propertiesCoefficient() {
        initialize("coefficient(ExponentVector)");
        Iterable<Pair<MultivariatePolynomial, ExponentVector>> ps = P.pairs(
                P.multivariatePolynomials(),
                P.exponentVectors()
        );
        for (Pair<MultivariatePolynomial, ExponentVector> p : take(LIMIT, ps)) {
            p.a.coefficient(p.b);
        }
    }

    private void propertiesOf_List_Pair_ExponentVector_BigInteger() {
        initialize("of(List<Pair<ExponentVector, BigInteger>>)");
        Iterable<List<Pair<ExponentVector, BigInteger>>> pss = P.lists(P.pairs(P.exponentVectors(), P.bigIntegers()));
        for (List<Pair<ExponentVector, BigInteger>> ps : take(LIMIT, pss)) {
            MultivariatePolynomial p = of(ps);
            p.validate();
            for (List<Pair<ExponentVector, BigInteger>> qs : take(TINY_LIMIT, P.permutationsFinite(ps))) {
                assertEquals(ps, of(qs), p);
            }
        }

        Iterable<List<Pair<ExponentVector, BigInteger>>> pssFail = filterInfinite(
                ps -> any(p -> p == null || p.a == null || p.b == null, ps),
                P.lists(P.withNull(P.pairs(P.withNull(P.exponentVectors()), P.withNull(P.bigIntegers()))))
        );
        for (List<Pair<ExponentVector, BigInteger>> ps : take(LIMIT, pssFail)) {
            try {
                of(ps);
                fail(ps);
            } catch (NullPointerException ignored) {}
        }
    }

    private static @NotNull MultivariatePolynomial of_ExponentVector_BigInteger_simplest(
            @NotNull ExponentVector ev,
            @NotNull BigInteger c
    ) {
        return of(Collections.singletonList(new Pair<>(ev, c)));
    }

    private void propertiesOf_ExponentVector_BigInteger() {
        initialize("of(ExponentVector, BigInteger)");
        for (Pair<ExponentVector, BigInteger> p : take(LIMIT, P.pairs(P.exponentVectors(), P.bigIntegers()))) {
            MultivariatePolynomial q = of(p.a, p.b);
            q.validate();
            assertEquals(p, of_ExponentVector_BigInteger_simplest(p.a, p.b), q);
            assertTrue(p, q.termCount() < 2);
        }
    }

    private void compareImplementationsOf_ExponentVector_BigInteger() {
        Map<String, Function<Pair<ExponentVector, BigInteger>, MultivariatePolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> of_ExponentVector_BigInteger_simplest(p.a, p.b));
        functions.put("standard", p -> of(p.a, p.b));
        Iterable<Pair<ExponentVector, BigInteger>> ps = P.pairs(P.exponentVectors(), P.bigIntegers());
        compareImplementations("of(ExponentVector, BigInteger)", take(LIMIT, ps), functions);
    }

    private void propertiesOf_BigInteger() {
        initialize("of(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            MultivariatePolynomial p = of(i);
            p.validate();
            assertTrue(i, p.degree() <= 0);
            assertTrue(i, p.termCount() <= 1);
            assertEquals(i, p.toString(), i.toString());
            inverse(MultivariatePolynomial::of, (MultivariatePolynomial q) -> q.coefficient(ExponentVector.ONE), i);
        }
    }

    private void propertiesOf_int() {
        initialize("of(int)");
        for (int i : take(LIMIT, P.integers())) {
            MultivariatePolynomial p = of(i);
            p.validate();
            assertTrue(i, p.degree() <= 0);
            assertTrue(i, p.termCount() <= 1);
            assertEquals(i, p.toString(), Integer.toString(i));
            inverse(
                    MultivariatePolynomial::of,
                    (MultivariatePolynomial q) -> q.coefficient(ExponentVector.ONE).intValueExact(),
                    i
            );
        }
    }

    private void propertiesOf_Polynomial_Variable() {
        initialize("of(Polynomial, Variable)");
        for (Pair<Polynomial, Variable> p : take(LIMIT, P.pairs(P.polynomials(), P.variables()))) {
            MultivariatePolynomial q = of(p.a, p.b);
            q.validate();
            assertTrue(p, q.variableCount() <= 1);
            assertEquals(p, q.toPolynomial(), p.a);
            assertEquals(p, p.a.degree(), q.degree());
        }
    }

    private void propertiesToPolynomial() {
        initialize("toPolynomial()");
        Iterable<MultivariatePolynomial> ps = P.withElement(
                ZERO,
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteLogarithmicOrder(
                                P.optionals(P.variables()),
                                v -> v.isPresent() ?
                                        filterInfinite(
                                                q -> q.degree() > 0,
                                                P.withScale(4).multivariatePolynomials(
                                                        Collections.singletonList(v.get())
                                                )
                                        ) :
                                        map(q -> of(q, Variable.of(0)), P.withScale(4).polynomials(0))
                        )
                )
        );
        for (MultivariatePolynomial p : take(LIMIT, ps)) {
            p.toPolynomial();
            List<Variable> vs = p.variables();
            if (vs.size() == 1) {
                Variable v = head(vs);
                inverse(MultivariatePolynomial::toPolynomial, r -> of(r, v), p);
            }
        }

        Iterable<MultivariatePolynomial> psFail = filterInfinite(
                q -> q.variableCount() > 1,
                P.multivariatePolynomials()
        );
        for (MultivariatePolynomial p : take(LIMIT, psFail)) {
            try {
                p.toPolynomial();
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesVariables() {
        initialize("variables()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            List<Variable> vs = p.variables();
            assertTrue(p, increasing(vs));
        }
    }

    private void propertiesVariableCount() {
        initialize("variableCount()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            int count = p.variableCount();
            assertTrue(p, count >= 0);
        }
    }

    private void propertiesTermCount() {
        initialize("termCount()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            int count = p.termCount();
            assertTrue(p, count >= 0);
        }
    }

    private void propertiesMaxCoefficientBitLength() {
        initialize("maxCoefficientBitLength()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            int bitLength = p.maxCoefficientBitLength();
            assertTrue(p, bitLength >= 0);
        }
    }

    private void propertiesDegree() {
        initialize("degree()");
        for (MultivariatePolynomial p : take(LIMIT, P.multivariatePolynomials())) {
            int degree = p.degree();
            assertTrue(p, degree >= -1);
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::multivariatePolynomials);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::multivariatePolynomials);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(MultivariatePolynomial)");
        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::multivariatePolynomials);

        Iterable<Pair<MultivariatePolynomial, MultivariatePolynomial>> ps = filterInfinite(
                p -> p.a.degree() != p.b.degree(),
                P.pairs(P.multivariatePolynomials())
        );
        for (Pair<MultivariatePolynomial, MultivariatePolynomial> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.degree(), p.b.degree()));
        }
    }

    private void propertiesRead() {
        initialize("read(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                MULTIVARIATE_POLYNOMIAL_CHARS,
                P.multivariatePolynomials(),
                MultivariatePolynomial::read,
                MultivariatePolynomial::validate,
                false
        );
    }

    private void propertiesFindIn() {
        initialize("findIn(String)");
        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.multivariatePolynomials(),
                MultivariatePolynomial::read,
                MultivariatePolynomial::findIn,
                MultivariatePolynomial::validate
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(
                LIMIT,
                MULTIVARIATE_POLYNOMIAL_CHARS,
                P.multivariatePolynomials(),
                MultivariatePolynomial::read
        );
    }
}
