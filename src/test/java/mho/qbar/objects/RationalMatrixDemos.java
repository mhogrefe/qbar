package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import org.jetbrains.annotations.NotNull;

import static mho.wheels.iterables.IterableUtils.*;

public class RationalMatrixDemos extends QBarDemos {
    private static final @NotNull String RATIONAL_MATRIX_CHARS = " #,-/0123456789[]";

    public RationalMatrixDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoRows() {
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            System.out.println("rows(" + m + ") = " + toList(m.rows()));
        }
    }

    private void demoColumns() {
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            System.out.println("columns(" + m + ") = " + toList(m.columns()));
        }
    }

    private void demoToString() {
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            System.out.println(m);
        }
    }
}
