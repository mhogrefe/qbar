package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import static mho.qbar.objects.AlgebraicAngle.*;
import static mho.wheels.iterables.IterableUtils.filterInfinite;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.ordering.Ordering.compare;
import static mho.wheels.testing.Testing.MEDIUM_LIMIT;
import static mho.wheels.testing.Testing.nicePrint;

@SuppressWarnings("UnusedDeclaration")
public class AlgebraicAngleDemos extends QBarDemos {
    private static final @NotNull String ALGEBRAIC_ANGLE_CHARS = " ()*+-/0123456789^acfiopqrstx";

    public AlgebraicAngleDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoFromTurns() {
        for (Rational r : take(LIMIT, filterInfinite(x -> x != Rational.ONE, P.range(Rational.ZERO, Rational.ONE)))) {
            System.out.println("fromTurns(" + r + ") = " + fromTurns(r));
        }
    }

    private void demoFromDegrees() {
        Rational limit = Rational.of(360);
        for (Rational r : take(LIMIT, filterInfinite(x -> !x.equals(limit), P.range(Rational.ZERO, limit)))) {
            System.out.println("fromDegrees(" + r + ") = " + fromDegrees(r));
        }
    }

    private void demoIsRationalMultipleOfPi() {
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            System.out.println(t + " is " + (t.isRationalMultipleOfPi() ? "" : "not ") + "a rational multiple of pi");
        }
    }

    private void demoRationalTurns() {
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            System.out.println("rationalTurns(" + t + ") = " + t.rationalTurns());
        }
    }

    private void demoEquals_AlgebraicAngle() {
        for (Pair<AlgebraicAngle, AlgebraicAngle> p : take(LIMIT, P.pairs(P.withScale(4).algebraicAngles()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            //noinspection ObjectEqualsNull
            System.out.println(t + (t.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            System.out.println("hashCode(" + t + ") = " + t.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<AlgebraicAngle, AlgebraicAngle> p : take(LIMIT, P.pairs(P.withScale(4).algebraicAngles()))) {
            System.out.println(p.a + " " + compare(p.a, p.b) + " " + p.b);
        }
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
