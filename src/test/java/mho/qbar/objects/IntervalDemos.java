package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import static mho.qbar.objects.Interval.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;

@SuppressWarnings({"ConstantConditions", "UnusedDeclaration"})
public class IntervalDemos {
    private static final boolean USE_RANDOM = false;
    private static final String INTERVAL_CHARS = " (),-/0123456789I[]finty";
    private static final int SMALL_LIMIT = 1000;
    private static int LIMIT;

    private static QBarIterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = new QBarRandomProvider(new Random(0x6af477d9a7e54fcaL));
            LIMIT = 1000;
        } else {
            P = QBarExhaustiveProvider.INSTANCE;
            LIMIT = 10000;
        }
    }

    public static void demoGetLower() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("getLower(" + a + ") = " + a.getLower());
        }
    }

    public static void demoGetUpper() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("getUpper(" + a + ") = " + a.getUpper());
        }
    }

    public static void demoOf_Rational_Rational() {
        initialize();
        Iterable<Pair<Rational, Rational>> rs = filter(p -> le(p.a, p.b), P.pairs(P.rationals()));
        for (Pair<Rational, Rational> p : take(LIMIT, rs)) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    public static void demoLessThanOrEqualTo() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("lessThanOrEqualTo(" + r + ") = " + lessThanOrEqualTo(r));
        }
    }

    public static void demoGreaterThanOrEqualTo() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("greaterThanOrEqualTo(" + r + ") = " + greaterThanOrEqualTo(r));
        }
    }

    public static void demoOf_Rational() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("of(" + r + ") = " + Interval.of(r));
        }
    }

    public static void demoIsFinitelyBounded() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println(a + " is " + (a.isFinitelyBounded() ? "" : "not ") + "finitely bounded");
        }
    }

    public static void demoContains_Rational() {
        initialize();
        for (Pair<Interval, Rational> p : take(LIMIT, P.pairs(P.intervals(), P.rationals()))) {
            System.out.println(p.a + (p.a.contains(p.b) ? " contains " : " does not contain ") + p.b);
        }
    }

    public static void demoContains_Interval() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + (p.a.contains(p.b) ? " contains " : " does not contain ") + p.b);
        }
    }

    public static void demoDiameter() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("diameter(" + a + ") = " + a.diameter());
        }
    }

    public static void demoConvexHull_Interval() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println("convexHull(" + p.a + ", " + p.b + ") = " + p.a.convexHull(p.b));
        }
    }

    public static void demoConvexHull_List_Interval() {
        initialize();
        for (List<Interval> as : take(LIMIT, P.listsAtLeast(1, P.intervals()))) {
            String listString = tail(init(as.toString()));
            System.out.println("convexHull(" + listString + ") = " + convexHull(as));
        }
    }

    public static void demoIntersection() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println("intersection(" + p.a + ", " + p.b + ") = " + p.a.intersection(p.b));
        }
    }

    public static void demoDisjoint() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " and " + p.b + " are " + (p.a.disjoint(p.b) ? "" : "not ") + "disjoint");
        }
    }

    public static void demoMakeDisjoint() {
        initialize();
        for (List<Interval> as : take(LIMIT, P.lists(P.intervals()))) {
            String listString = tail(init(as.toString()));
            System.out.println("makeDisjoint(" + listString + ") = " + makeDisjoint(as));
        }
    }

    public static void demoComplement() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("complement(" + a + ") = " + a.complement());
        }
    }

    public static void demoMidpoint() {
        initialize();
        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            System.out.println("midpoint(" + a + ") = " + a.midpoint());
        }
    }

    public static void demoSplit() {
        initialize();
        Iterable<Pair<Interval, Rational>> ps = filter(q -> q.a.contains(q.b), P.pairs(P.intervals(), P.rationals()));
        for (Pair<Interval, Rational> p : take(LIMIT, ps)) {
            System.out.println("split(" + p.a + ", " + p.b + ") = " + p.a.split(p.b));
        }
    }

    public static void demoBisect() {
        initialize();
        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            System.out.println("bisect(" + a + ") = " + a.bisect());
        }
    }

    public static void demoRoundingPreimage_float() {
        initialize();
        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g), P.floats()))) {
            System.out.println("roundingPreimage(" + f + ") = " + roundingPreimage(f));
        }
    }

    public static void demoRoundingPreimage_double() {
        initialize();
        for (double d : take(SMALL_LIMIT, filter(e -> !Double.isNaN(e), P.doubles()))) {
            System.out.println("roundingPreimage(" + d + ") = " + roundingPreimage(d));
        }
    }

    public static void demoRoundingPreimage_BigDecimal() {
        initialize();
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            System.out.println("roundingPreimage(" + bd + ") = " + roundingPreimage(bd));
        }
    }

    public static void demoFloatRange() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("floatRange(" + a + ") = " + a.floatRange());
        }
    }

    public static void demoDoubleRange() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("doubleRange(" + a + ") = " + a.doubleRange());
        }
    }

    public static void demoAdd() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    public static void demoNegate() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("-" + a + " = " + a.negate());
        }
    }

    public static void demoAbs() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("|" + a + "| = " + a.abs());
        }
    }

    public static void demoSignum() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("sgn(" + a + ") = " + a.signum());
        }
    }

    public static void demoSubtract() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    public static void demoMultiply_Interval() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    public static void demoMultiply_Rational() {
        initialize();
        for (Pair<Interval, Rational> p : take(LIMIT, P.pairs(P.intervals(), P.rationals()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    public static void demoMultiply_BigInteger() {
        initialize();
        for (Pair<Interval, BigInteger> p : take(LIMIT, P.pairs(P.intervals(), P.bigIntegers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    public static void demoMultiply_int() {
        initialize();
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.integers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    public static void demoInvert() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("1/" + a + " = " + a.invert());
        }
    }

    public static void demoInvertHull() {
        initialize();
        for (Interval a : take(LIMIT, filter(b -> !b.equals(ZERO), P.intervals()))) {
            System.out.println("invertHull(" + a + ") = " + a.invertHull());
        }
    }

    public static void demoDivide_Interval() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    public static void demoDivideHull() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, filter(q -> !q.b.equals(ZERO), P.pairs(P.intervals())))) {
            System.out.println("divideHull(" + p.a + ", " + p.b + ") = " + p.a.divideHull(p.b));
        }
    }

    public static void demoDivide_Rational() {
        initialize();
        Iterable<Pair<Interval, Rational>> ps = P.pairs(P.intervals(), filter(r -> r != Rational.ZERO, P.rationals()));
        for (Pair<Interval, Rational> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    public static void demoDivide_BigInteger() {
        initialize();
        Iterable<Pair<Interval, BigInteger>> ps = P.pairs(
                P.intervals(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Interval, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    public static void demoDivide_int() {
        initialize();
        Iterable<Pair<Interval, Integer>> ps = P.pairs(P.intervals(), filter(i -> i != 0, P.integers()));
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    public static void demoShiftLeft() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric(50);
        }
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), is))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    public static void demoShiftRight() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric(50);
        }
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), is))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    public static void demoSum() {
        initialize();
        for (List<Interval> rs : take(LIMIT, P.lists(P.intervals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(rs));
        }
    }

    public static void demoProduct() {
        initialize();
        for (List<Interval> rs : take(LIMIT, P.lists(P.intervals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Π(" + listString + ") = " + product(rs));
        }
    }

    public static void demoDelta() {
        initialize();
        for (List<Interval> rs : take(LIMIT, P.listsAtLeast(1, P.intervals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(delta(rs)));
        }
    }

    public static void demoPow() {
        initialize();
        Iterable<Integer> exps;
        if (P instanceof QBarExhaustiveProvider) {
            exps = P.integers();
        } else {
            exps = ((QBarRandomProvider) P).integersGeometric(50);
        }
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), exps))) {
            System.out.println(p.a + " ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    public static void demoPowHull() {
        initialize();
        Iterable<Integer> exps;
        if (P instanceof QBarExhaustiveProvider) {
            exps = P.integers();
        } else {
            exps = ((QBarRandomProvider) P).integersGeometric(50);
        }
        Iterable<Pair<Interval, Integer>> ps = filter(
                p -> p.b >= 0 || !p.a.equals(ZERO),
                P.pairs(P.intervals(), exps)
        );
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " ^ " + p.b + " = " + p.a.powHull(p.b));
        }
    }

    public static void demoElementCompare() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println("elementCompare(" + p.a + ", " + p.b + ") = " + p.a.elementCompare(p.b));
        }
    }

    public static void demoEquals_Interval() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    public static void demoEquals_null() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            //noinspection ObjectEqualsNull
            System.out.println(a + (a.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    public static void demoHashCode() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("hashCode(" + a + ") = " + a.hashCode());
        }
    }

    public static void demoCompareTo() {
        initialize();
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    public static void demoRead() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    public static void demoRead_targeted() {
        initialize();
        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(INTERVAL_CHARS);
        } else {
            cs = ((QBarRandomProvider) P).uniformSample(INTERVAL_CHARS);
        }
        for (String s : take(LIMIT, P.strings(cs))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    public static void demoFindIn() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    public static void demoFindIn_targeted() {
        initialize();
        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(INTERVAL_CHARS);
        } else {
            cs = ((QBarRandomProvider) P).uniformSample(INTERVAL_CHARS);
        }
        for (String s : take(LIMIT, P.strings(cs))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    public static void demoToString() {
        initialize();
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println(a);
        }
    }
}
