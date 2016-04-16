package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.iterables.IterableUtils.zip;
import static mho.wheels.ordering.Ordering.eq;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.testing.Testing.*;

public class MonomialOrderProperties extends QBarTestProperties {
    private static final @NotNull String MONOMIAL_ORDER_CHARS = "EGLRVX";

    public MonomialOrderProperties() {
        super("MonomialOrder");
    }

    @Override
    protected void testBothModes() {
        propertiesCompare();
        propertiesReadStrict();
    }

    private void propertiesCompare() {
        initialize("compareTo(Variable)");
        for (MonomialOrder o : QBarExhaustiveProvider.INSTANCE.monomialOrders()) {
            QBarIterableProvider Q = P.deepCopy();
            QBarIterableProvider R = P.deepCopy();
            Iterable<Pair<ExponentVector, ExponentVector>> ps = zip(P.exponentVectors(), Q.exponentVectors());
            for (Pair<ExponentVector, ExponentVector> p : IterableUtils.take(LIMIT, ps)) {
                assertTrue(p, eq(o, p.a, p.b));
            }

            P.reset();
            Q.reset();
            ps = zip(P.exponentVectors(), Q.exponentVectors());
            for (Pair<ExponentVector, ExponentVector> p : take(LIMIT, ps)) {
                int compare = o.compare(p.a, p.b);
                antiSymmetric((x, y) -> le(o, x, y), p);
                assertTrue(p, compare == 0 || compare == 1 || compare == -1);
                assertTrue(p, le(o, p.a, p.b) || le(o, p.b, p.a));
                antiCommutative(o::compare, c -> -c, p);
            }

            P.reset();
            Q.reset();
            R.reset();
            Iterable<Triple<ExponentVector, ExponentVector, ExponentVector>> ts =  EP.triples(
                    P.exponentVectors(),
                    Q.exponentVectors(),
                    R.exponentVectors()
            );
            for (Triple<ExponentVector, ExponentVector, ExponentVector> t : take(LIMIT, ts)) {
                transitive((x, y) -> le(o, x, y), t);
            }
        }
    }

    private void propertiesReadStrict() {
        initialize("readStrict(String)");
        QBarTesting.propertiesReadHelper(
            LIMIT,
            P,
            MONOMIAL_ORDER_CHARS,
            P.monomialOrders(),
            MonomialOrder::readStrict,
            o -> {},
            false
        );
    }
}
