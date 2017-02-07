package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import static mho.qbar.objects.AlgebraicAngle.*;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.testing.Testing.MEDIUM_LIMIT;
import static mho.wheels.testing.Testing.nicePrint;

@SuppressWarnings("UnusedDeclaration")
public class AlgebraicAngleDemos extends QBarDemos {
    private static final @NotNull String ALGEBRAIC_ANGLE_CHARS = " ()*+-/0123456789^acfiopqrstx";

    public AlgebraicAngleDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoReadStrict_String() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_String_targeted() {
        for (String s : take(LIMIT, P.strings(ALGEBRAIC_ANGLE_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_int_String() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(P.strings(), P.rangeUpGeometric(2));
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("readStrict(" + p.b + ", " + nicePrint(p.a) + ") = " + readStrict(p.b, p.a));
        }
    }

    private void demoReadStrict_int_String_targeted() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                P.strings(ALGEBRAIC_ANGLE_CHARS),
                P.rangeUpGeometric(2)
        );
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("readStrict(" + p.b + ", " + p.a + ") = " + readStrict(p.b, p.a));
        }
    }

    private void demoToString() {
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.algebraicAngles())) {
            System.out.println(t);
        }
    }
}
