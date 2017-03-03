package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import static mho.qbar.objects.AlgebraicAngle.*;
import static mho.wheels.iterables.IterableUtils.filterInfinite;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.ordering.Ordering.compare;
import static mho.wheels.ordering.Ordering.ge;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.testing.Testing.MEDIUM_LIMIT;
import static mho.wheels.testing.Testing.TINY_LIMIT;
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

    private void demoGetQuadrant() {
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            System.out.println("getQuadrant(" + t + ") = " + t.getQuadrant());
        }
    }

    private void demoRealTurns() {
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            System.out.println("realTurns(" + t + ") = " + t.realTurns());
        }
    }

    private void demoRadians() {
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            System.out.println("radians(" + t + ") = " + t.radians());
        }
    }

    private void demoDegrees() {
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            System.out.println("degrees(" + t + ") = " + t.degrees());
        }
    }

    private void demoNegate() {
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            System.out.println("-(" + t + ") = " + t.negate());
        }
    }

    private void demoSupplement() {
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            System.out.println("pi-(" + t + ") = " + t.supplement());
        }
    }

    private void demoAddPi() {
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            System.out.println("pi+(" + t + ") = " + t.addPi());
        }
    }

    private void demoSin() {
        BigInteger maxDenominator = BigInteger.valueOf(25);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, ts)) {
            System.out.println("sin(" + t + ") = " + t.sin());
        }
    }

    private void demoCos() {
        BigInteger maxDenominator = BigInteger.valueOf(1000);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, ts)) {
            System.out.println("cos(" + t + ") = " + t.cos());
        }
    }

    private void demoTan() {
        BigInteger maxDenominator = BigInteger.valueOf(15);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> !t.equals(PI_OVER_TWO) && !t.equals(THREE_PI_OVER_TWO) &&
                        (t.isRationalMultipleOfPi() || t.cos().degree() < 6) &&
                        t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, ts)) {
            System.out.println("tan(" + t + ") = " + t.tan());
        }
    }

    private void demoCot() {
        BigInteger maxDenominator = BigInteger.valueOf(15);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> !t.equals(ZERO) && !t.equals(PI) &&
                        (t.isRationalMultipleOfPi() || t.cos().degree() < 6) &&
                        t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, ts)) {
            System.out.println("cot(" + t + ") = " + t.cot());
        }
    }

    private void demoSec() {
        BigInteger maxDenominator = BigInteger.valueOf(100);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> !t.equals(PI_OVER_TWO) && !t.equals(THREE_PI_OVER_TWO) &&
                        t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, ts)) {
            System.out.println("sec(" + t + ") = " + t.sec());
        }
    }

    private void demoCsc() {
        BigInteger maxDenominator = BigInteger.valueOf(25);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> !t.equals(ZERO) && !t.equals(PI) &&
                        t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, ts)) {
            System.out.println("csc(" + t + ") = " + t.csc());
        }
    }

    private void demoComplement() {
        BigInteger maxDenominator = BigInteger.valueOf(25);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, ts)) {
            System.out.println("pi/2-(" + t + ") = " + t.complement());
        }
    }

    private void demoRegularPolygonArea() {
        for (int i : take(TINY_LIMIT, P.withScale(4).rangeUpGeometric(2))) {
            System.out.println("regularPolygonArea(" + i + ") = " + regularPolygonArea(i));
        }
    }

    private void demoAntiprismVolume() {
        for (int i : take(15, P.withScale(4).rangeUpGeometric(2))) {
            System.out.println("antiprismVolume(" + i + ") = " + antiprismVolume(i));
        }
    }

    private void demoArcsin() {
        Iterable<Algebraic> xs = P.withScale(4).withSecondaryScale(4).range(Algebraic.NEGATIVE_ONE, Algebraic.ONE);
        for (Algebraic x : take(MEDIUM_LIMIT, xs)) {
            System.out.println("arcsin(" + x + ") = " + arcsin(x));
        }
    }

    private void demoArccos() {
        Iterable<Algebraic> xs = P.withScale(4).withSecondaryScale(4).range(Algebraic.NEGATIVE_ONE, Algebraic.ONE);
        for (Algebraic x : take(MEDIUM_LIMIT, xs)) {
            System.out.println("arccos(" + x + ") = " + arccos(x));
        }
    }

    private void demoArctan() {
        for (Algebraic x : take(MEDIUM_LIMIT, P.withScale(4).withSecondaryScale(4).algebraics())) {
            System.out.println("arctan(" + x + ") = " + arctan(x));
        }
    }

    private void demoArccot() {
        for (Algebraic x : take(MEDIUM_LIMIT, P.withScale(4).withSecondaryScale(4).algebraics())) {
            System.out.println("arccot(" + x + ") = " + arccot(x));
        }
    }

    private void demoArcsec() {
        Iterable<Algebraic> xs = filterInfinite(
                y -> ge(y.abs(), Algebraic.ONE),
                P.withScale(4).withSecondaryScale(4).algebraics()
        );
        for (Algebraic x : take(MEDIUM_LIMIT, xs)) {
            System.out.println("arcsec(" + x + ") = " + arcsec(x));
        }
    }

    private void demoArccsc() {
        Iterable<Algebraic> xs = filterInfinite(
                y -> ge(y.abs(), Algebraic.ONE),
                P.withScale(4).withSecondaryScale(4).algebraics()
        );
        for (Algebraic x : take(MEDIUM_LIMIT, xs)) {
            System.out.println("arccsc(" + x + ") = " + arccsc(x));
        }
    }

    private void demoPolarAngle() {
        Iterable<Pair<Algebraic, Algebraic>> ps = filterInfinite(
                p -> p.a != Algebraic.ZERO || p.b != Algebraic.ZERO,
                P.pairs(filterInfinite(x -> x.degree() < 4, P.withScale(4).withSecondaryScale(4).algebraics()))
        );
        for (Pair<Algebraic, Algebraic> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("polarAngle(" + p.a + ", " + p.b + ") = " + polarAngle(p.a, p.b));
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
