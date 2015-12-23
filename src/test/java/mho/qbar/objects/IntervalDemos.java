package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.Interval.*;
import static mho.qbar.testing.QBarTesting.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class IntervalDemos extends QBarDemos {
    private static final @NotNull String INTERVAL_CHARS = " (),-/0123456789I[]finty";

    public IntervalDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoGetLower() {
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("getLower(" + a + ") = " + a.getLower());
        }
    }

    private void demoGetUpper() {
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("getUpper(" + a + ") = " + a.getUpper());
        }
    }

    private void demoOf_Rational_Rational() {
        for (Pair<Rational, Rational> p : take(LIMIT, P.bagPairs(P.rationals()))) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private void demoLessThanOrEqualTo() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("lessThanOrEqualTo(" + r + ") = " + lessThanOrEqualTo(r));
        }
    }

    private void demoGreaterThanOrEqualTo() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("greaterThanOrEqualTo(" + r + ") = " + greaterThanOrEqualTo(r));
        }
    }

    private void demoOf_Rational() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("of(" + r + ") = " + Interval.of(r));
        }
    }

    private void demoBitLength() {
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("bitLength(" + a + ") = " + a.bitLength());
        }
    }

    private void demoIsFinitelyBounded() {
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println(a + " is " + (a.isFinitelyBounded() ? "" : "not ") + "finitely bounded");
        }
    }

    private void demoContains_Rational() {
        for (Pair<Interval, Rational> p : take(LIMIT, P.pairs(P.intervals(), P.rationals()))) {
            System.out.println(p.a + (p.a.contains(p.b) ? " contains " : " does not contain ") + p.b);
        }
    }

    private void demoContains_Interval() {
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + (p.a.contains(p.b) ? " contains " : " does not contain ") + p.b);
        }
    }

    private void demoDiameter() {
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("diameter(" + a + ") = " + a.diameter());
        }
    }

    private void demoConvexHull_Interval() {
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println("convexHull(" + p.a + ", " + p.b + ") = " + p.a.convexHull(p.b));
        }
    }

    private void demoConvexHull_List_Interval() {
        for (List<Interval> as : take(LIMIT, P.withScale(4).listsAtLeast(1, P.intervals()))) {
            String listString = tail(init(as.toString()));
            System.out.println("convexHull(" + listString + ") = " + convexHull(as));
        }
    }

    private void demoIntersection() {
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " ∩ " + p.b + " = " + p.a.intersection(p.b));
        }
    }

    private void demoDisjoint() {
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " and " + p.b + " are " + (p.a.disjoint(p.b) ? "" : "not ") + "disjoint");
        }
    }

    private void demoUnion() {
        for (List<Interval> as : take(LIMIT, P.withScale(4).lists(P.intervals()))) {
            String listString = tail(init(as.toString()));
            System.out.println("⋃(" + listString + ") = " + union(as));
        }
    }

    private void demoComplement() {
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("complement(" + a + ") = " + a.complement());
        }
    }

    private void demoMidpoint() {
        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            System.out.println("midpoint(" + a + ") = " + a.midpoint());
        }
    }

    private void demoSplit() {
        Iterable<Pair<Interval, Rational>> ps = filterInfinite(
                q -> q.a.contains(q.b),
                P.pairs(P.intervals(), P.rationals())
        );
        for (Pair<Interval, Rational> p : take(LIMIT, ps)) {
            System.out.println("split(" + p.a + ", " + p.b + ") = " + p.a.split(p.b));
        }
    }

    private void demoBisect() {
        for (Interval a : take(LIMIT, P.finitelyBoundedIntervals())) {
            System.out.println("bisect(" + a + ") = " + a.bisect());
        }
    }

    private void demoRoundingPreimage_float() {
        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g), P.floats()))) {
            System.out.println("roundingPreimage(" + f + ") = " + roundingPreimage(f));
        }
    }

    private void demoRoundingPreimage_double() {
        for (double d : take(MEDIUM_LIMIT, filter(e -> !Double.isNaN(e), P.doubles()))) {
            System.out.println("roundingPreimage(" + d + ") = " + roundingPreimage(d));
        }
    }

    private void demoRoundingPreimage_BigDecimal() {
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            System.out.println("roundingPreimage(" + bd + ") = " + roundingPreimage(bd));
        }
    }

    private void demoAdd() {
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private void demoNegate() {
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("-" + a + " = " + a.negate());
        }
    }

    private void demoAbs() {
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("|" + a + "| = " + a.abs());
        }
    }

    private void demoSignum() {
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("signum(" + a + ") = " + a.signum());
        }
    }

    private void demoSubtract() {
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_Interval() {
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Rational() {
        for (Pair<Interval, Rational> p : take(LIMIT, P.pairs(P.intervals(), P.rationals()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        for (Pair<Interval, BigInteger> p : take(LIMIT, P.pairs(P.intervals(), P.bigIntegers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.integers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoInvert() {
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("1 / " + a + " = " + a.invert());
        }
    }

    private void demoInvertHull() {
        for (Interval a : take(LIMIT, filterInfinite(b -> !b.equals(ZERO), P.intervals()))) {
            System.out.println("invertHull(" + a + ") = " + a.invertHull());
        }
    }

    private void demoDivide_Interval() {
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivideHull() {
        Iterable<Pair<Interval, Interval>> ps = filterInfinite(q -> !q.b.equals(ZERO), P.pairs(P.intervals()));
        for (Pair<Interval, Interval> p : take(LIMIT, ps)) {
            System.out.println("divideHull(" + p.a + ", " + p.b + ") = " + p.a.divideHull(p.b));
        }
    }

    private void demoDivide_Rational() {
        for (Pair<Interval, Rational> p : take(LIMIT, P.pairs(P.intervals(), P.nonzeroRationals()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_BigInteger() {
        for (Pair<Interval, BigInteger> p : take(LIMIT, P.pairs(P.intervals(), P.nonzeroBigIntegers()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_int() {
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.nonzeroIntegers()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoShiftLeft() {
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.integersGeometric()))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoShiftRight() {
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.integersGeometric()))) {
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    private void demoSum() {
        for (List<Interval> rs : take(LIMIT, P.withScale(4).lists(P.intervals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(rs));
        }
    }

    private void demoProduct() {
        for (List<Interval> rs : take(LIMIT, P.withScale(4).lists(P.intervals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Π(" + listString + ") = " + product(rs));
        }
    }

    private void demoDelta_finite() {
        for (List<Interval> rs : take(LIMIT, P.withScale(4).listsAtLeast(1, P.intervals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Δ(" + listString + ") = " + its(delta(rs)));
        }
    }

    private void demoDelta_infinite() {
        for (Iterable<Interval> as : take(MEDIUM_LIMIT, P.prefixPermutations(QEP.intervals()))) {
            String listString = tail(init(its(as)));
            System.out.println("Δ(" + listString + ") = " + its(delta(as)));
        }
    }

    private void demoPow() {
        for (Pair<Interval, Integer> p : take(LIMIT, P.pairs(P.intervals(), P.integersGeometric()))) {
            System.out.println(p.a + " ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private void demoPowHull() {
        Iterable<Pair<Interval, Integer>> ps = filterInfinite(
                p -> p.b >= 0 || !p.a.equals(ZERO),
                P.pairs(P.intervals(), P.integersGeometric())
        );
        for (Pair<Interval, Integer> p : take(LIMIT, ps)) {
            System.out.println("powHull(" + p.a + ", " + p.b + ") = " + p.a.powHull(p.b));
        }
    }

    private void demoElementCompare() {
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println("elementCompare(" + p.a + ", " + p.b + ") = " + p.a.elementCompare(p.b));
        }
    }

    private void demoEquals_Interval() {
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (Interval a : take(LIMIT, P.intervals())) {
            //noinspection ObjectEqualsNull
            System.out.println(a + (a.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println("hashCode(" + a + ") = " + a.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<Interval, Interval> p : take(LIMIT, P.pairs(P.intervals()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        for (String s : take(LIMIT, P.strings(INTERVAL_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        for (String s : take(LIMIT, P.strings(INTERVAL_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println(a);
        }
    }
}
