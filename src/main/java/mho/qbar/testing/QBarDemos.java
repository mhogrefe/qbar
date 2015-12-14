package mho.qbar.testing;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;

import static mho.wheels.testing.Testing.LARGE_LIMIT;
import static mho.wheels.testing.Testing.MEDIUM_LIMIT;

public class QBarDemos {
    protected final boolean USE_RANDOM = false;
    protected int LIMIT;
    protected QBarIterableProvider P;

    protected void initialize() {
        //noinspection ConstantConditions
        if (USE_RANDOM) {
            P = QBarRandomProvider.example();
            LIMIT = MEDIUM_LIMIT;
        } else {
            P = QBarExhaustiveProvider.INSTANCE;
            LIMIT = LARGE_LIMIT;
        }
    }
}
