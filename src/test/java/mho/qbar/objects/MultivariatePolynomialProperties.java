package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

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
