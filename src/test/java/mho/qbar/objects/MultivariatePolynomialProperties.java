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

import static mho.qbar.objects.MultivariatePolynomial.*;
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
