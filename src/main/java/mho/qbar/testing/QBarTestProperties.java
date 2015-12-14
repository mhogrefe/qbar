package mho.qbar.testing;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static mho.wheels.testing.Testing.LARGE_LIMIT;
import static mho.wheels.testing.Testing.MEDIUM_LIMIT;

public abstract class QBarTestProperties {
    protected int LIMIT;
    protected QBarIterableProvider P;
    private @NotNull
    String name;

    public QBarTestProperties(@NotNull String name) {
        this.name = name;
    }

    protected void initializeConstant(@NotNull String methodName) {
        System.out.println("\ttesting " + methodName + " properties...");
    }

    protected void initialize(@NotNull String methodName) {
        P.reset();
        System.out.print('\t');
        initializeConstant(methodName);
    }

    protected void testConstant() {}

    protected abstract void testBothModes();

    @Test
    public void testAllProperties() {
        System.out.println(name + " properties");
        testConstant();
        List<Triple<QBarIterableProvider, Integer, String>> configs = new ArrayList<>();
        configs.add(new Triple<>(QBarExhaustiveProvider.INSTANCE, LARGE_LIMIT, "exhaustively"));
        configs.add(new Triple<>(QBarRandomProvider.example(), MEDIUM_LIMIT, "randomly"));
        for (Triple<QBarIterableProvider, Integer, String> config : configs) {
            P = config.a;
            LIMIT = config.b;
            System.out.println("\ttesting " + config.c);
            testBothModes();
        }
        System.out.println("Done");
    }
}
