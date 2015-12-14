package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.qbar.testing.QBarDemos;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.Interval.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.testing.Testing.MEDIUM_LIMIT;

@SuppressWarnings("UnusedDeclaration")
public class IntervalDemos extends QBarDemos {
    private static final @NotNull String INTERVAL_CHARS = " (),-/0123456789I[]finty";

    public IntervalDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoGetLower() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("getLower(" + a + ") = " + a.getLower());
        }
    }

    private void demoGetUpper() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("getUpper(" + a + ") = " + a.getUpper());
        }
    }

    private void demoOf_Rational_Rational() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, filterInfinite(q -> le(q.a, q.b), P.pairs(P.rationals())))) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private void demoLessThanOrEqualTo() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("lessThanOrEqualTo(" + r + ") = " + lessThanOrEqualTo(r));
        }
    }

    private void demoGreaterThanOrEqualTo() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("greaterThanOrEqualTo(" + r + ") = " + greaterThanOrEqualTo(r));
        }
    }

    private void demoOf_Rational() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("of(" + r + ") = " + Interval.of(r));
        }
    }

    private void demoIsFinitelyBounded() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println(a + " is " + (a.isFinitelyBounded() ? "" : "not ") + "finitely bounded");
        }
    }

    private void demoContains_Rational() {
        initialize();
        for (Pair<Interval, Rational> p : take(LIMIT, P.pairs(P.intervals(), P.rationals()))) {
            System.out.println(p.a + (p.a.contains(p.b) ? " contains " : " does not contain ") + p.b);
        }
    }

    private void demoContains_Interval() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + (p.a.contains(p.b) ? " contains " : " does not contain ") + p.b);
        }
    }

    private void demoDiameter() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("diameter(" + a + ") = " + a.diameter());
        }
    }

    private void demoConvexHull_Interval() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println("convexHull(" + p.a + ", " + p.b + ") = " + p.a.convexHull(p.b));
        }
    }

    private void demoConvexHull_List_Interval() {
        initialize();
        for (List<Interval> as : take(LIMIT, P.listsAtLeast(1, P.intervals()))) {
            String listString = tail(init(as.toString()));
            System.out.println("convexHull(" + listString + ") = " + convexHull(as));
        }
    }

    private void demoIntersection() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println("intersection(" + p.a + ", " + p.b + ") = " + p.a.intersection(p.b));
        }
    }

    private void demoDisjoint() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " and " + p.b + " are " + (p.a.disjoint(p.b) ? "" : "not ") + "disjoint");
        }
    }

    private void demoMakeDisjoint() {
        initialize();
        for (List<Interval> as : take(LIMIT, P.lists(P.intervals()))) {
            String listString = tail(init(as.toString()));
            System.out.println("makeDisjoint(" + listString + ") = " + makeDisjoint(as));
        }
    }

    private void demoComplement() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("complement(" + a + ") = " + a.complement());
        }
    }

    private void demoMidpoint() {
        initialize();
        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            System.out.println("midpoint(" + a + ") = " + a.midpoint());
        }
    }

    private void demoSplit() {
        initialize();
        Iterable<Pair<Interval, Rational>> ps = filter(q -> q.a.contains(q.b), P.pairs(P.intervals(), P.rationals()));
        for (Pair<Interval, Rational> p : take(LIMIT, ps)) {
            System.out.println("split(" + p.a + ", " + p.b + ") = " + p.a.split(p.b));
        }
    }

    private void demoBisect() {
        initialize();
        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            System.out.println("bisect(" + a + ") = " + a.bisect());
        }
    }

    private void demoRoundingPreimage_float() {
        initialize();
        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g), P.floats()))) {
            System.out.println("roundingPreimage(" + f + ") = " + roundingPreimage(f));
        }
    }

    private void demoRoundingPreimage_double() {
        initialize();
        for (double d : take(MEDIUM_LIMIT, filter(e -> !Double.isNaN(e), P.doubles()))) {
            System.out.println("roundingPreimage(" + d + ") = " + roundingPreimage(d));
        }
    }

    private void demoRoundingPreimage_BigDecimal() {
        initialize();
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            System.out.println("roundingPreimage(" + bd + ") = " + roundingPreimage(bd));
        }
    }

    private void demoFloatRange() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("floatRange(" + a + ") = " + a.floatRange());
        }
    }

    private void demoDoubleRange() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("doubleRange(" + a + ") = " + a.doubleRange());
        }
    }

    private void demoAdd() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private void demoNegate() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("-" + a + " = " + a.negate());
        }
    }

    private void demoAbs() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("|" + a + "| = " + a.abs());
        }
    }

    private void demoSignum() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("sgn(" + a + ") = " + a.signum());
        }
    }

    private void demoSubtract() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_Interval() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Rational() {
        initialize();
        for (Pair<Interval, Rational> p : take(LIMIT, P.pairs(P.intervals(), P.rationals()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        initialize();
        for (Pair<Interval, BigInteger> p : take(LIMIT, P.pairs(P.intervals(), P.bigIntegers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        initialize();
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.integers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoInvert() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("1/" + a + " = " + a.invert());
        }
    }

    private void demoInvertHull() {
        initialize();
        for (Interval a : take(LIMIT, filter(b -> !b.equals(ZERO), P.intervals()))) {
            System.out.println("invertHull(" + a + ") = " + a.invertHull());
        }
    }

    private void demoDivide_Interval() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivideHull() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, filter(q -> !q.b.equals(ZERO), P.pairs(P.intervals())))) {
            System.out.println("divideHull(" + p.a + ", " + p.b + ") = " + p.a.divideHull(p.b));
        }
    }

    private void demoDivide_Rational() {
        initialize();
        Iterable<Pair<Interval, Rational>> ps = P.pairs(P.intervals(), filter(r -> r != Rational.ZERO, P.rationals()));
        for (Pair<Interval, Rational> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_BigInteger() {
        initialize();
        Iterable<Pair<Interval, BigInteger>> ps = P.pairs(
                P.intervals(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Interval, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_int() {
        initialize();
        Iterable<Pair<Interval, Integer>> ps = P.pairs(P.intervals(), filter(i -> i != 0, P.integers()));
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoShiftLeft() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric();
        }
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), is))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoShiftRight() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric();
        }
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), is))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    private void demoSum() {
        initialize();
        for (List<Interval> rs : take(LIMIT, P.lists(P.intervals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(rs));
        }
    }

    private void demoProduct() {
        initialize();
        for (List<Interval> rs : take(LIMIT, P.lists(P.intervals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Π(" + listString + ") = " + product(rs));
        }
    }

    private void demoDelta() {
        initialize();
        for (List<Interval> rs : take(LIMIT, P.listsAtLeast(1, P.intervals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(delta(rs)));
        }
    }

    private void demoPow() {
        initialize();
        Iterable<Integer> exps;
        if (P instanceof QBarExhaustiveProvider) {
            exps = P.integers();
        } else {
            exps = ((QBarRandomProvider) P).integersGeometric();
        }
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), exps))) {
            System.out.println(p.a + " ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private void demoPowHull() {
        initialize();
        Iterable<Integer> exps;
        if (P instanceof QBarExhaustiveProvider) {
            exps = P.integers();
        } else {
            exps = ((QBarRandomProvider) P).integersGeometric();
        }
        Iterable<Pair<Interval, Integer>> ps = filter(
                p -> p.b >= 0 || !p.a.equals(ZERO),
                P.pairs(P.intervals(), exps)
        );
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " ^ " + p.b + " = " + p.a.powHull(p.b));
        }
    }

    private void demoElementCompare() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println("elementCompare(" + p.a + ", " + p.b + ") = " + p.a.elementCompare(p.b));
        }
    }

    private void demoEquals_Interval() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            //noinspection ObjectEqualsNull
            System.out.println(a + (a.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("hashCode(" + a + ") = " + a.hashCode());
        }
    }

    private void demoCompareTo() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        initialize();
        for (String s : take(LIMIT, P.strings(INTERVAL_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        initialize();
        for (String s : take(LIMIT, P.strings(INTERVAL_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println(a);
        }
    }
}
