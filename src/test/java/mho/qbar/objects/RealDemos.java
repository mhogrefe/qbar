package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.iterables.CachedIterator;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import static mho.qbar.objects.Real.*;
import static mho.wheels.iterables.IterableUtils.map;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.iterables.IterableUtils.toList;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class RealDemos extends QBarDemos {
    public RealDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoOf_Rational() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("of(" + r + ") = " + of(r));
        }
    }

    private void demoOf_BigInteger() {
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoOf_long() {
        for (long l : take(LIMIT, P.longs())) {
            System.out.println("of(" + l + ") = " + of(l));
        }
    }

    private void demoOf_int() {
        for (int i : take(LIMIT, P.integers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoOf_BinaryFraction() {
        for (BinaryFraction bf : take(LIMIT, P.binaryFractions())) {
            System.out.println("of(" + bf + ") = " + of(bf));
        }
    }

    private void demoOf_float() {
        for (float f : take(LIMIT, P.floats())) {
            System.out.println("of(" + f + ") = " + of(f));
        }
    }

    private void demoOf_double() {
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println("of(" + d + ") = " + of(d));
        }
    }

    private void demoOfExact_float() {
        for (float f : take(LIMIT, P.floats())) {
            System.out.println("ofExact(" + f + ") = " + ofExact(f));
        }
    }

    private void demoOfExact_double() {
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println("ofExact(" + d + ") = " + ofExact(d));
        }
    }

    private void demoOf_BigDecimal() {
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            System.out.println("of(" + bd + ") = " + of(bd));
        }
    }

    private void demoFuzzyRepresentation() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("fuzzyRepresentation(" + r + ") = " + fuzzyRepresentation(r));
        }
    }

    private void demoLeftFuzzyRepresentation() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("leftFuzzyRepresentation(" + r + ") = " + leftFuzzyRepresentation(r));
        }
    }

    private void demoRightFuzzyRepresentation() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("rightFuzzyRepresentation(" + r + ") = " + rightFuzzyRepresentation(r));
        }
    }

    private void demoIterator() {
        for (Real r : take(MEDIUM_LIMIT, P.withScale(4).reals())) {
            System.out.println(r + ": " + its(r));
        }
    }

    private void demoIsExact() {
        for (Real r : take(LIMIT, P.withScale(4).reals())) {
            System.out.println(r + " is " + (r.isExact() ? "" : "not ") + "exact");
        }
    }

    private void demoRationalValue() {
        for (Real r : take(LIMIT, P.withScale(4).reals())) {
            System.out.println("rationalValue(" + r +") = " + r.rationalValue());
        }
    }

    private void demoMatch() {
        CachedIterator<Real> reals = new CachedIterator<>(P.withScale(4).cleanReals());
        Iterable<Pair<Real, List<Real>>> ps = map(
                p -> new Pair<>(reals.get(p.b).get(), toList(map(i -> reals.get(i).get(), p.a))),
                P.dependentPairs(
                        P.withScale(4).distinctListsAtLeast(1, P.withScale(4).naturalIntegersGeometric()),
                        P::uniformSample
                )
        );
        for (Pair<Real, List<Real>> p : take(LIMIT, ps)) {
            System.out.println("match(" + p.a + ", " + p.b + ") = " + p.a.match(p.b));
        }
    }

    private enum FuzzinessType {
        LEFT, RIGHT, BOTH
    }

    private static boolean fuzzyCheck(@NotNull Rational r, @NotNull RoundingMode rm, @NotNull FuzzinessType ft) {
        boolean left = ft == FuzzinessType.LEFT || ft == FuzzinessType.BOTH;
        boolean right = ft == FuzzinessType.RIGHT || ft == FuzzinessType.BOTH;
        if (r.isInteger()) {
            int sign = r.signum();
            switch (rm) {
                case UP:
                    return sign != 0 && (sign != 1 || right) && (sign != -1 || left);
                case DOWN:
                    return (sign != 1 || left) && (sign != -1 || right);
                case CEILING:
                    return right;
                case FLOOR:
                    return left;
                default:
                    return true;
            }
        } else if (r.getDenominator().equals(IntegerUtils.TWO)) {
            int sign = r.signum();
            switch (rm) {
                case HALF_UP:
                    return (sign != 1 || right) && (sign != -1 || left);
                case HALF_DOWN:
                    return (sign != 1 || left) && (sign != -1 || right);
                case HALF_EVEN:
                    BigInteger mod4 = r.getNumerator().and(BigInteger.valueOf(3));
                    return (!mod4.equals(BigInteger.ONE) && right) || (!mod4.equals(BigInteger.valueOf(3)) && left);
                default:
                    return true;
            }
        } else {
            return true;
        }
    }
}
