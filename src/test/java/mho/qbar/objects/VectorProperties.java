package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.Vector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class VectorProperties extends QBarTestProperties {
    private static final @NotNull String VECTOR_CHARS = " ,-0123456789[]";

    public VectorProperties() {
        super("Vector");
    }

    @Override
    protected void testBothModes() {
        propertiesIterator();
        propertiesGet();
        propertiesOf_List_BigInteger();
        propertiesOf_BigInteger();
        propertiesDimension();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead();
        propertiesFindIn();
        propertiesToString();
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (Vector v : take(LIMIT, P.vectors())) {
            List<BigInteger> is = toList(v);
            assertTrue(v, all(r -> r != null, is));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<BigInteger> js) -> of(js), v);
            testNoRemove(v);
            testHasNext(v);
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

    private void propertiesDimension() {
        initialize("dimension()");
        for (Vector v : take(LIMIT, P.vectors())) {
            int dimension = v.dimension();
            assertTrue(v, dimension >= 0);
            assertEquals(v, length(v), dimension);
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

    private void propertiesRead() {
        initialize("read(String)");
        QBarTesting.propertiesReadHelper(LIMIT, P, VECTOR_CHARS, P.vectors(), Vector::read, Vector::validate, false);
    }

    private void propertiesFindIn() {
        initialize("findIn(String)");
        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.vectors(),
                Vector::read,
                Vector::findIn,
                Vector::validate
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, VECTOR_CHARS, P.vectors(), Vector::read);
    }
}
