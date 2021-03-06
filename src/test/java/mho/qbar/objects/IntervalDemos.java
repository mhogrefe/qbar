package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Either;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.Interval.*;
import static mho.qbar.objects.Interval.sum;
import static mho.qbar.testing.QBarTesting.QEP;
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

    private void demoContains_Algebraic() {
        for (Pair<Interval, Algebraic> p : take(LIMIT, P.pairs(P.intervals(), P.withScale(4).algebraics()))) {
            System.out.println(p.a + (p.a.contains(p.b) ? " contains " : " does not contain ") + p.b);
        }
    }

    private void demoContainsUnsafe() {
        Iterable<Rational> rs = P.rationals();
        //noinspection RedundantCast,Convert2MethodRef
        Iterable<Pair<Interval, Real>> ps = map(
                q -> {
                    Either<Algebraic, Pair<Rational, Integer>> e = q.b;
                    switch (e.whichSlot()) {
                        case A:
                            return new Pair<>(q.a, e.a().realValue());
                        case B:
                            Pair<Rational, Integer> r = e.b();
                            switch (r.b) {
                                case -1:
                                    return new Pair<>(q.a, Real.leftFuzzyRepresentation(r.a));
                                case 1:
                                    return new Pair<>(q.a, Real.rightFuzzyRepresentation(r.a));
                                case 0:
                                    return new Pair<>(q.a, Real.fuzzyRepresentation(r.a));
                                default:
                                    throw new IllegalStateException();
                            }
                        default:
                            throw new IllegalStateException();
                    }
                },
                filterInfinite(
                        p -> {
                            if (p.b.whichSlot() == Either.Slot.A) {
                                return true;
                            }
                            Pair<Rational, Integer> q = p.b.b();
                            Optional<Rational> lower = p.a.getLower();
                            if (lower.isPresent() && lower.get().equals(q.a) && q.b < 1) {
                                return false;
                            }
                            Optional<Rational> upper = p.a.getUpper();
                            //noinspection RedundantIfStatement
                            if (upper.isPresent() && upper.get().equals(q.a) && q.b > -1) {
                                return false;
                            }
                            return true;
                        },
                        P.pairs(
                                P.intervals(),
                                (Iterable<Either<Algebraic, Pair<Rational, Integer>>>) P.withScale(1).choose(
                                        (Iterable<Either<Algebraic, Pair<Rational, Integer>>>)
                                                map(
                                                        x -> Either.<Algebraic, Pair<Rational, Integer>>ofA(x),
                                                        P.withScale(4).algebraics()
                                                ),
                                        P.choose(
                                                Arrays.asList(
                                                        map(r -> Either.ofB(new Pair<>(r, -1)), rs),
                                                        map(r -> Either.ofB(new Pair<>(r, 1)), rs),
                                                        map(r -> Either.ofB(new Pair<>(r, 0)), rs)
                                                )
                                        )
                                )
                        )
                )
        );
        for (Pair<Interval, Real> p : take(LIMIT, ps)) {
            System.out.println(p.a + (p.a.containsUnsafe(p.b) ? " contains " : " does not contain ") + p.b);
        }
    }

    private void demoContains_Real_Rational() {
        Iterable<Triple<Interval, Real, Rational>> ts = P.triples(
                P.intervals(),
                P.withScale(4).reals(),
                P.positiveRationals()
        );
        for (Triple<Interval, Real, Rational> t : take(LIMIT, ts)) {
            System.out.println("contains(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.contains(t.b, t.c));
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
            System.out.println("convexHull(" + middle(as.toString()) + ") = " + convexHull(as));
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
            System.out.println("⋃(" + middle(as.toString()) + ") = " + union(as));
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
            System.out.println("Σ(" + middle(rs.toString()) + ") = " + sum(rs));
        }
    }

    private void demoProduct() {
        for (List<Interval> rs : take(LIMIT, P.withScale(4).lists(P.intervals()))) {
            System.out.println("Π(" + middle(rs.toString()) + ") = " + product(rs));
        }
    }

    private void demoDelta_finite() {
        for (List<Interval> rs : take(LIMIT, P.withScale(4).listsAtLeast(1, P.intervals()))) {
            System.out.println("Δ(" + middle(rs.toString()) + ") = " + its(delta(rs)));
        }
    }

    private void demoDelta_infinite() {
        for (Iterable<Interval> as : take(MEDIUM_LIMIT, P.prefixPermutations(QEP.intervals()))) {
            System.out.println("Δ(" + middle(its(as)) + ") = " + its(delta(as)));
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
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b) + " " + p.b);
        }
    }

    private void demoReadStrict() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_targeted() {
        for (String s : take(LIMIT, P.strings(INTERVAL_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoToString() {
        for (Interval a : take(LIMIT, P.intervals())) {
            System.out.println(a);
        }
    }
}
