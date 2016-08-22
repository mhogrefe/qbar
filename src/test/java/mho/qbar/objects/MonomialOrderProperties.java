package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import org.jetbrains.annotations.NotNull;

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
            QBarTesting.propertiesCompareToHelper(LIMIT, P, o, QBarIterableProvider::monomials);
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
            false,
            true
        );
    }
}
