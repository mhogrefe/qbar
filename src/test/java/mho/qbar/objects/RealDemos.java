package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.CachedIterator;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.FloatingPointUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Quadruple;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.Real.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class RealDemos extends QBarDemos {
    public RealDemos(boolean useRandom) {
        super(useRandom);
    }

    // runs forever
    private void constant_helper(@NotNull Real c) {
        BigInteger base = BigInteger.TEN;
        Pair<List<BigInteger>, Iterable<BigInteger>> digits = c.digitsUnsafe(base);
        String beforeDecimal =
                IntegerUtils.toStringBase(base, IntegerUtils.fromBigEndianDigits(base, digits.a));
        System.out.print(beforeDecimal);
        System.out.print('.');
        int i = beforeDecimal.length() + 1;
        for (BigInteger d : digits.b) {
            System.out.print(d);
            i++;
            if (i % SMALL_LIMIT == 0) {
                System.out.println();
            }
        }
    }

    private void demoSqrt2() {
        constant_helper(SQRT_TWO);
    }

    private void demoPhi() {
        constant_helper(PHI);
    }

    private void demoE() {
        constant_helper(E);
    }

    private void demoPi() {
        constant_helper(PI);
    }

    private void demoLog2() {
        constant_helper(LOG_2);
    }

    private void demoPrimeConstant() {
        constant_helper(PRIME_CONSTANT);
    }

    private void demoThueMorse() {
        constant_helper(THUE_MORSE);
    }

    private void demoKolakoski() {
        constant_helper(KOLAKOSKI);
    }

    private void demoContinuedFractionConstant() {
        constant_helper(CONTINUED_FRACTION_CONSTANT);
    }

    private void demoCahen() {
        constant_helper(CAHEN);
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

    private void demoIsExactInteger() {
        for (Real x : take(LIMIT, P.withScale(4).reals())) {
            System.out.println(x + " is " + (x.isExactInteger() ? "" : "not ") + "an exact integer");
        }
    }

    private enum FuzzinessType {
        NONE, LEFT, RIGHT, BOTH
    }

    private static boolean rmIntCheck(@NotNull Algebraic x, @NotNull RoundingMode rm, @NotNull FuzzinessType ft) {
        boolean left = ft == FuzzinessType.LEFT || ft == FuzzinessType.BOTH;
        boolean right = ft == FuzzinessType.RIGHT || ft == FuzzinessType.BOTH;
        if (x.isInteger()) {
            int sign = x.signum();
            switch (rm) {
                case UP:
                    return sign != 0 && !(sign == 1 && right) && !(sign == -1 && left);
                case DOWN:
                    return !(sign == 1 && left) && !(sign == -1 && right);
                case CEILING:
                    return !right;
                case FLOOR:
                    return !left;
                case UNNECESSARY:
                    return ft == FuzzinessType.NONE;
                default:
                    return true;
            }
        } else if (x.isRational() && x.rationalValueExact().getDenominator().equals(IntegerUtils.TWO)) {
            int sign = x.signum();
            switch (rm) {
                case HALF_UP:
                    return !(sign == 1 && left) && !(sign == -1 && right);
                case HALF_DOWN:
                    return !(sign == 1 && right) && !(sign == -1 && left);
                case HALF_EVEN:
                    BigInteger mod4 = x.rationalValueExact().getNumerator().and(BigInteger.valueOf(3));
                    return !(mod4.equals(BigInteger.ONE) && right) && !(mod4.equals(BigInteger.valueOf(3)) && left);
                case UNNECESSARY:
                    return false;
                default:
                    return true;
            }
        } else {
            return rm != RoundingMode.UNNECESSARY;
        }
    }

    private void demoBigIntegerValueUnsafe_RoundingMode() {
        //noinspection RedundantCast
        Iterable<Pair<Real, RoundingMode>> ps = map(
                q -> {
                    Real r;
                    switch (q.a.b) {
                        case NONE:
                            r = q.a.a.realValue();
                            break;
                        case LEFT:
                            r = leftFuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        case RIGHT:
                            r = rightFuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        case BOTH:
                            r = fuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                    return new Pair<>(r, q.b);
                },
                filterInfinite(
                        p -> rmIntCheck(p.a.a, p.b, p.a.b),
                        P.pairsLogarithmicOrder(
                                P.withScale(1).choose(
                                        map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                        P.choose(
                                                (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                                P.withScale(4).rationals()
                                                        )
                                                )
                                        )
                                ),
                                P.roundingModes()
                        )
                )
        );
        for (Pair<Real, RoundingMode> p : take(LIMIT, ps)) {
            System.out.println("bigIntegerValueUnsafe(" + p.a + ", " + p.b + ") = " + p.a.bigIntegerValueUnsafe(p.b));
        }
    }

    private void demoBigIntegerValue_RoundingMode_Rational() {
        Iterable<Triple<Real, RoundingMode, Rational>> ts = filterInfinite(
                s -> s.b != RoundingMode.UNNECESSARY || s.a.isExactInteger(),
                P.triples(P.withScale(4).reals(), P.roundingModes(), P.withScale(4).positiveRationals())
        );
        for (Triple<Real, RoundingMode, Rational> t : take(LIMIT, ts)) {
            System.out.println("bigIntegerValue(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.bigIntegerValue(t.b, t.c));
        }
    }

    private void demoBigIntegerValueUnsafe() {
        //noinspection RedundantCast
        Iterable<Real> xs = map(
                q -> {
                    switch (q.b) {
                        case NONE:
                            return q.a.realValue();
                        case LEFT:
                            return leftFuzzyRepresentation(q.a.rationalValueExact());
                        case RIGHT:
                            return rightFuzzyRepresentation(q.a.rationalValueExact());
                        case BOTH:
                            return fuzzyRepresentation(q.a.rationalValueExact());
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                },
                filterInfinite(
                        p -> rmIntCheck(p.a, RoundingMode.HALF_EVEN, p.b),
                        P.withScale(1).choose(
                                map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                P.choose(
                                        (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                        P.withScale(4).rationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                        P.withScale(4).rationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                        P.withScale(4).rationals()
                                                )
                                        )
                                )
                        )
                )
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("bigIntegerValueUnsafe(" + x + ") = " + x.bigIntegerValueUnsafe());
        }
    }

    private void demoBigIntegerValue_Rational() {
        Iterable<Pair<Real, Rational>> ps = P.pairs(P.withScale(4).reals(), P.withScale(4).positiveRationals());
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("bigIntegerValue(" + p.a + ", " + p.b + ") = " + p.a.bigIntegerValue(p.b));
        }
    }

    private void demoFloorUnsafe() {
        //noinspection RedundantCast
        Iterable<Real> xs = map(
                q -> {
                    switch (q.b) {
                        case NONE:
                            return q.a.realValue();
                        case LEFT:
                            return leftFuzzyRepresentation(q.a.rationalValueExact());
                        case RIGHT:
                            return rightFuzzyRepresentation(q.a.rationalValueExact());
                        case BOTH:
                            return fuzzyRepresentation(q.a.rationalValueExact());
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                },
                filterInfinite(
                        p -> rmIntCheck(p.a, RoundingMode.FLOOR, p.b),
                        P.withScale(1).choose(
                                map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                P.choose(
                                        (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                        P.withScale(4).rationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                        P.withScale(4).rationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                        P.withScale(4).rationals()
                                                )
                                        )
                                )
                        )
                )
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("floorUnsafe(" + x + ") = " + x.floorUnsafe());
        }
    }

    private void demoFloor() {
        Iterable<Pair<Real, Rational>> ps = P.pairs(P.withScale(4).reals(), P.withScale(4).positiveRationals());
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("floor(" + p.a + ", " + p.b + ") = " + p.a.floor(p.b));
        }
    }

    private void demoCeilingUnsafe() {
        //noinspection RedundantCast
        Iterable<Real> xs = map(
                q -> {
                    switch (q.b) {
                        case NONE:
                            return q.a.realValue();
                        case LEFT:
                            return leftFuzzyRepresentation(q.a.rationalValueExact());
                        case RIGHT:
                            return rightFuzzyRepresentation(q.a.rationalValueExact());
                        case BOTH:
                            return fuzzyRepresentation(q.a.rationalValueExact());
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                },
                filterInfinite(
                        p -> rmIntCheck(p.a, RoundingMode.CEILING, p.b),
                        P.withScale(1).choose(
                                map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                P.choose(
                                        (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                        P.withScale(4).rationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                        P.withScale(4).rationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                        P.withScale(4).rationals()
                                                )
                                        )
                                )
                        )
                )
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("ceilingUnsafe(" + x + ") = " + x.ceilingUnsafe());
        }
    }

    private void demoCeiling() {
        Iterable<Pair<Real, Rational>> ps = P.pairs(P.withScale(4).reals(), P.withScale(4).positiveRationals());
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("ceiling(" + p.a + ", " + p.b + ") = " + p.a.ceiling(p.b));
        }
    }

    private void demoBigIntegerValueExact() {
        for (Real x : take(LIMIT, map(Real::of, P.bigIntegers()))) {
            System.out.println("bigIntegerValueExact(" + x + ") = " + x.bigIntegerValueExact());
        }
    }

    private void demoByteValueExact() {
        for (Real x : take(LIMIT, map(Real::of, P.bytes()))) {
            System.out.println("byteValueExact(" + x + ") = " + x.byteValueExact());
        }
    }

    private void demoShortValueExact() {
        for (Real x : take(LIMIT, map(Real::of, P.shorts()))) {
            System.out.println("shortValueExact(" + x + ") = " + x.shortValueExact());
        }
    }

    private void demoIntValueExact() {
        for (Real x : take(LIMIT, map(Real::of, P.integers()))) {
            System.out.println("intValueExact(" + x + ") = " + x.intValueExact());
        }
    }

    private void demoLongValueExact() {
        for (Real x : take(LIMIT, map(Real::of, P.longs()))) {
            System.out.println("longValueExact(" + x + ") = " + x.longValueExact());
        }
    }

    private void demoIsExactIntegerPowerOfTwo() {
        for (Real x : take(LIMIT, P.withScale(4).positiveReals())) {
            System.out.println(x + " is " + (x.isExactIntegerPowerOfTwo() ? "an" : "not an") +
                    " exact integer power of 2");
        }
    }

    //x must be positive
    private static boolean pow2Check(@NotNull Algebraic x, @NotNull FuzzinessType ft) {
        boolean right = ft == FuzzinessType.RIGHT || ft == FuzzinessType.BOTH;
        return !x.isIntegerPowerOfTwo() || !right;
    }

    private void demoRoundUpToIntegerPowerOfTwoUnsafe() {
        //noinspection RedundantCast
        Iterable<Real> xs = map(
                q -> {
                    switch (q.b) {
                        case NONE:
                            return q.a.realValue();
                        case LEFT:
                            return leftFuzzyRepresentation(q.a.rationalValueExact());
                        case RIGHT:
                            return rightFuzzyRepresentation(q.a.rationalValueExact());
                        case BOTH:
                            return fuzzyRepresentation(q.a.rationalValueExact());
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                },
                filterInfinite(
                        p -> pow2Check(p.a, p.b),
                        P.withScale(1).choose(
                                map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).positiveAlgebraics()),
                                P.choose(
                                        (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                        P.withScale(4).positiveRationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                        P.withScale(4).positiveRationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                        P.withScale(4).positiveRationals()
                                                )
                                        )
                                )
                        )
                )
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("roundUpToIntegerPowerOfTwoUnsafe(" + x + ") = " +
                    x.roundUpToIntegerPowerOfTwoUnsafe());
        }
    }

    private void demoRoundUpToIntegerPowerOfTwo() {
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                P.withScale(4).positiveReals(),
                P.withScale(4).positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("roundUpToIntegerPowerOfTwo(" + p.a + ", " + p.b + ") = " +
                    p.a.roundUpToIntegerPowerOfTwo(p.b));
        }
    }

    private void demoIsExactBinaryFraction() {
        for (Real x : take(LIMIT, P.withScale(4).positiveReals())) {
            System.out.println(x + " is " + (x.isExactBinaryFraction() ? "an" : "not an") + " exact binary fraction");
        }
    }

    private void demoIsExact() {
        for (Real r : take(LIMIT, P.withScale(4).reals())) {
            System.out.println(r + " is " + (r.isExact() ? "" : "not ") + "exact");
        }
    }

    private void demoRationalValueExact() {
        for (Real r : take(LIMIT, P.withScale(4).reals())) {
            System.out.println("rationalValueExact(" + r +") = " + r.rationalValueExact());
        }
    }

    //x must be positive
    private static boolean binaryExponentCheck(@NotNull Algebraic x, @NotNull FuzzinessType ft) {
        boolean left = ft == FuzzinessType.LEFT || ft == FuzzinessType.BOTH;
        return !x.isIntegerPowerOfTwo() || !left;
    }

    private void demoBinaryExponentUnsafe() {
        //noinspection RedundantCast
        Iterable<Real> xs = map(
                q -> {
                    switch (q.b) {
                        case NONE:
                            return q.a.realValue();
                        case LEFT:
                            return leftFuzzyRepresentation(q.a.rationalValueExact());
                        case RIGHT:
                            return rightFuzzyRepresentation(q.a.rationalValueExact());
                        case BOTH:
                            return fuzzyRepresentation(q.a.rationalValueExact());
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                },
                filterInfinite(
                        p -> binaryExponentCheck(p.a, p.b),
                        P.withScale(1).choose(
                                map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).positiveAlgebraics()),
                                P.choose(
                                        (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                        P.withScale(4).positiveRationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                        P.withScale(4).positiveRationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                        P.withScale(4).positiveRationals()
                                                )
                                        )
                                )
                        )
                )
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("binaryExponentUnsafe(" + x + ") = " + x.binaryExponentUnsafe());
        }
    }

    private void demoBinaryExponent() {
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                P.withScale(4).positiveReals(),
                P.withScale(4).positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("binaryExponent(" + p.a + ", " + p.b + ") = " + p.a.binaryExponent(p.b));
        }
    }

    private void demoIsExactAndEqualToFloat() {
        for (Real x : take(LIMIT, P.withScale(4).reals())) {
            System.out.println(x + " is " + (x.isExactAndEqualToFloat() ? "" : "not ") + "exact and equal to a float");
        }
    }

    private void demoIsExactAndEqualToDouble() {
        for (Real x : take(LIMIT, P.withScale(4).reals())) {
            System.out.println(x + " is " + (x.isExactAndEqualToDouble() ? "" : "not ") +
                    "exact and equal to a double");
        }
    }

    private static boolean rmFloatCheck(@NotNull Algebraic x, @NotNull RoundingMode rm, @NotNull FuzzinessType ft) {
        boolean left = ft == FuzzinessType.LEFT || ft == FuzzinessType.BOTH;
        boolean right = ft == FuzzinessType.RIGHT || ft == FuzzinessType.BOTH;
        if (x.isEqualToFloat()) {
            int sign = x.signum();
            switch (rm) {
                case UP:
                    return sign != 0 && !(sign == 1 && right) && !(sign == -1 && left);
                case DOWN:
                    return sign != 0 && !(sign == 1 && left) && !(sign == -1 && right);
                case CEILING:
                    return sign != 0 && !right;
                case FLOOR:
                    return !left;
                case UNNECESSARY:
                    return ft == FuzzinessType.NONE;
                default:
                    return !(sign == 0 && left);
            }
        } else if (x.isRational()) {
            Rational r = x.rationalValueExact();
            float predecessor = r.floatValue(RoundingMode.FLOOR);
            if (Float.isFinite(predecessor) && predecessor != Float.MAX_VALUE &&
                    r.equals(Rational.ofExact(predecessor).get()
                            .add(Rational.ofExact(FloatingPointUtils.successor(predecessor)).get()).shiftRight(1))) {
                int sign = x.signum();
                switch (rm) {
                    case HALF_UP:
                        return !(sign == 1 && left) && !(sign == -1 && right);
                    case HALF_DOWN:
                        return !(sign == 1 && right) && !(sign == -1 && left);
                    case HALF_EVEN:
                        return (Float.floatToIntBits(predecessor) & 1) == 0 ? !right : !left;
                    case UNNECESSARY:
                        return false;
                    default:
                        return true;
                }
            }
        }
        return rm != RoundingMode.UNNECESSARY;
    }

    private void demoFloatValueUnsafe_RoundingMode() {
        //noinspection RedundantCast
        Iterable<Pair<Real, RoundingMode>> ps = map(
                q -> {
                    Real r;
                    switch (q.a.b) {
                        case NONE:
                            r = q.a.a.realValue();
                            break;
                        case LEFT:
                            r = leftFuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        case RIGHT:
                            r = rightFuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        case BOTH:
                            r = fuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                    return new Pair<>(r, q.b);
                },
                filterInfinite(
                        p -> rmFloatCheck(p.a.a, p.b, p.a.b),
                        P.pairsLogarithmicOrder(
                                P.withScale(1).choose(
                                        map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                        P.choose(
                                                (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                                P.withScale(4).rationals()
                                                        )
                                                )
                                        )
                                ),
                                P.roundingModes()
                        )
                )
        );
        for (Pair<Real, RoundingMode> p : take(LIMIT, ps)) {
            System.out.println("floatValueUnsafe(" + p.a + ", " + p.b + ") = " + p.a.floatValueUnsafe(p.b));
        }
    }

    private void demoFloatValue_RoundingMode_Rational() {
        Iterable<Triple<Real, RoundingMode, Rational>> ts = filterInfinite(
                s -> s.b != RoundingMode.UNNECESSARY || s.a.isExactAndEqualToFloat(),
                P.triples(P.withScale(4).reals(), P.roundingModes(), P.positiveRationals())
        );
        for (Triple<Real, RoundingMode, Rational> t : take(LIMIT, ts)) {
            System.out.println("floatValue(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.floatValue(t.b, t.c));
        }
    }

    private void demoFloatValueUnsafe() {
        //noinspection RedundantCast
        Iterable<Real> xs = map(
                q -> {
                    switch (q.b) {
                        case NONE:
                            return q.a.realValue();
                        case LEFT:
                            return leftFuzzyRepresentation(q.a.rationalValueExact());
                        case RIGHT:
                            return rightFuzzyRepresentation(q.a.rationalValueExact());
                        case BOTH:
                            return fuzzyRepresentation(q.a.rationalValueExact());
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                },
                filterInfinite(
                        p -> rmFloatCheck(p.a, RoundingMode.HALF_EVEN, p.b),
                        P.withScale(1).choose(
                                map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                P.choose(
                                        (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                        P.withScale(4).rationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                        P.withScale(4).rationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                        P.withScale(4).rationals()
                                                )
                                        )
                                )
                        )
                )
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("floatValueUnsafe(" + x + ") = " + x.floatValueUnsafe());
        }
    }

    private void demoFloatValue_Rational() {
        Iterable<Pair<Real, Rational>> ps = P.pairs(P.withScale(4).reals(), P.withScale(4).positiveRationals());
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("floatValue(" + p.a + ", " + p.b + ") = " + p.a.floatValue(p.b));
        }
    }

    private void demoFloatValueExact() {
        Iterable<Real> xs = map(
                f -> ofExact(f).get(),
                filter(f -> Float.isFinite(f) && !FloatingPointUtils.isNegativeZero(f), P.floats())
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("floatValueExact(" + x + ") = " + x.floatValueExact());
        }
    }

    private static boolean rmDoubleCheck(@NotNull Algebraic x, @NotNull RoundingMode rm, @NotNull FuzzinessType ft) {
        boolean left = ft == FuzzinessType.LEFT || ft == FuzzinessType.BOTH;
        boolean right = ft == FuzzinessType.RIGHT || ft == FuzzinessType.BOTH;
        if (x.isEqualToDouble()) {
            int sign = x.signum();
            switch (rm) {
                case UP:
                    return sign != 0 && !(sign == 1 && right) && !(sign == -1 && left);
                case DOWN:
                    return sign != 0 && !(sign == 1 && left) && !(sign == -1 && right);
                case CEILING:
                    return sign != 0 && !right;
                case FLOOR:
                    return !left;
                case UNNECESSARY:
                    return ft == FuzzinessType.NONE;
                default:
                    return !(sign == 0 && left);
            }
        } else if (x.isRational()) {
            Rational r = x.rationalValueExact();
            double predecessor = r.doubleValue(RoundingMode.FLOOR);
            if (Double.isFinite(predecessor) && predecessor != Double.MAX_VALUE &&
                    r.equals(Rational.ofExact(predecessor).get()
                            .add(Rational.ofExact(FloatingPointUtils.successor(predecessor)).get()).shiftRight(1))) {
                int sign = x.signum();
                switch (rm) {
                    case HALF_UP:
                        return !(sign == 1 && left) && !(sign == -1 && right);
                    case HALF_DOWN:
                        return !(sign == 1 && right) && !(sign == -1 && left);
                    case HALF_EVEN:
                        return (Double.doubleToLongBits(predecessor) & 1) == 0 ? !right : !left;
                    case UNNECESSARY:
                        return false;
                    default:
                        return true;
                }
            }
        }
        return rm != RoundingMode.UNNECESSARY;
    }

    private void demoDoubleValueUnsafe_RoundingMode() {
        //noinspection RedundantCast
        Iterable<Pair<Real, RoundingMode>> ps = map(
                q -> {
                    Real r;
                    switch (q.a.b) {
                        case NONE:
                            r = q.a.a.realValue();
                            break;
                        case LEFT:
                            r = leftFuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        case RIGHT:
                            r = rightFuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        case BOTH:
                            r = fuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                    return new Pair<>(r, q.b);
                },
                filterInfinite(
                        p -> rmDoubleCheck(p.a.a, p.b, p.a.b),
                        P.pairsLogarithmicOrder(
                                P.withScale(1).choose(
                                        map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                        P.choose(
                                                (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                                P.withScale(4).rationals()
                                                        )
                                                )
                                        )
                                ),
                                P.roundingModes()
                        )
                )
        );
        for (Pair<Real, RoundingMode> p : take(LIMIT, ps)) {
            System.out.println("doubleValueUnsafe(" + p.a + ", " + p.b + ") = " + p.a.doubleValueUnsafe(p.b));
        }
    }

    private void demoDoubleValue_RoundingMode_Rational() {
        Iterable<Triple<Real, RoundingMode, Rational>> ts = filterInfinite(
                s -> s.b != RoundingMode.UNNECESSARY || s.a.isExactAndEqualToFloat(),
                P.triples(P.withScale(4).reals(), P.roundingModes(), P.positiveRationals())
        );
        for (Triple<Real, RoundingMode, Rational> t : take(LIMIT, ts)) {
            System.out.println("doubleValue(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.doubleValue(t.b, t.c));
        }
    }

    private void demoDoubleValueUnsafe() {
        //noinspection RedundantCast
        Iterable<Real> xs = map(
                q -> {
                    switch (q.b) {
                        case NONE:
                            return q.a.realValue();
                        case LEFT:
                            return leftFuzzyRepresentation(q.a.rationalValueExact());
                        case RIGHT:
                            return rightFuzzyRepresentation(q.a.rationalValueExact());
                        case BOTH:
                            return fuzzyRepresentation(q.a.rationalValueExact());
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                },
                filterInfinite(
                        p -> rmDoubleCheck(p.a, RoundingMode.HALF_EVEN, p.b),
                        P.withScale(1).choose(
                                map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                P.choose(
                                        (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                        P.withScale(4).rationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                        P.withScale(4).rationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                        P.withScale(4).rationals()
                                                )
                                        )
                                )
                        )
                )
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("doubleValueUnsafe(" + x + ") = " + x.doubleValueUnsafe());
        }
    }

    private void demoDoubleValue_Rational() {
        Iterable<Pair<Real, Rational>> ps = P.pairs(P.withScale(4).reals(), P.withScale(4).positiveRationals());
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("doubleValue(" + p.a + ", " + p.b + ") = " + p.a.doubleValue(p.b));
        }
    }

    private void demoDoubleValueExact() {
        Iterable<Real> xs = map(
                d -> ofExact(d).get(),
                filter(d -> Double.isFinite(d) && !FloatingPointUtils.isNegativeZero(d), P.doubles())
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("doubleValueExact(" + x + ") = " + x.doubleValueExact());
        }
    }

    private static boolean precisionAlphaBorderline(int n, @NotNull Rational x) {
        return x.hasTerminatingBaseExpansion(BigInteger.TEN) && x.bigDecimalValueExact().precision() <= n;
    }

    private static boolean precisionBetaBorderline(int n, @NotNull Rational x) {
        if (!x.hasTerminatingBaseExpansion(BigInteger.TEN)) {
            return false;
        }
        BigDecimal bd = x.bigDecimalValueExact();
        return bd.precision() == n + 1 && bd.unscaledValue().mod(BigInteger.TEN).intValueExact() == 5;
    }

    private static boolean rmBigDecimalPrecisionCheck(
            @NotNull Algebraic x,
            int precision,
            @NotNull RoundingMode rm,
            @NotNull FuzzinessType ft
    ) {
        int sign = x.signum();
        if (sign == 0) {
            return ft == FuzzinessType.NONE;
        }
        if (precision == 0) {
            return x.hasTerminatingBaseExpansion(BigInteger.TEN) && ft == FuzzinessType.NONE;
        }
        boolean left = ft == FuzzinessType.LEFT || ft == FuzzinessType.BOTH;
        boolean right = ft == FuzzinessType.RIGHT || ft == FuzzinessType.BOTH;
        if (x.isRational() && precisionAlphaBorderline(precision, x.rationalValueExact())) {
            switch (rm) {
                case UP:
                    return !(sign == 1 && right) && !(sign == -1 && left);
                case DOWN:
                    return !(sign == 1 && left) && !(sign == -1 && right);
                case CEILING:
                    return !right;
                case FLOOR:
                    return !left;
                case UNNECESSARY:
                    return ft == FuzzinessType.NONE;
                default:
                    return true;
            }
        } else if (x.isRational() && precisionBetaBorderline(precision, x.rationalValueExact())) {
            switch (rm) {
                case HALF_UP:
                    return !(sign == 1 && left) && !(sign == -1 && right);
                case HALF_DOWN:
                    return !(sign == 1 && right) && !(sign == -1 && left);
                case HALF_EVEN:
                    boolean lowerEven =
                            !x.bigDecimalValueByPrecision(precision, RoundingMode.FLOOR).unscaledValue().testBit(0);
                    return !(lowerEven ? right : left);
                case UNNECESSARY:
                    return false;
                default:
                    return true;
            }
        }
        return rm != RoundingMode.UNNECESSARY;
    }

    private void demoBigDecimalValueByPrecisionUnsafe_int_RoundingMode() {
        //noinspection RedundantCast
        Iterable<Triple<Real, Integer, RoundingMode>> ts = map(
                u -> {
                    Real r;
                    switch (u.a.b) {
                        case NONE:
                            r = u.a.a.realValue();
                            break;
                        case LEFT:
                            r = leftFuzzyRepresentation(u.a.a.rationalValueExact());
                            break;
                        case RIGHT:
                            r = rightFuzzyRepresentation(u.a.a.rationalValueExact());
                            break;
                        case BOTH:
                            r = fuzzyRepresentation(u.a.a.rationalValueExact());
                            break;
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                    return new Triple<>(r, u.b, u.c);
                },
                filterInfinite(
                        t -> rmBigDecimalPrecisionCheck(t.a.a, t.b, t.c, t.a.b),
                        P.triples(
                                P.withScale(1).choose(
                                        map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                        P.choose(
                                                (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                                P.withScale(4).rationals()
                                                        )
                                                )
                                        )
                                ),
                                P.naturalIntegersGeometric(),
                                P.roundingModes()
                        )
                )
        );
        for (Triple<Real, Integer, RoundingMode> t : take(LIMIT, ts)) {
            System.out.println("bigDecimalValueByPrecisionUnsafe(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.bigDecimalValueByPrecisionUnsafe(t.b, t.c));
        }
    }

    private void demoBigDecimalValueByPrecision_int_RoundingMode_Rational() {
        Iterable<Quadruple<Real, Integer, RoundingMode, Rational>> qs = filterInfinite(
                q -> {
                    try {
                        q.a.bigDecimalValueByPrecision(q.b, q.c, q.d);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                P.quadruples(
                        P.withScale(4).reals(),
                        P.naturalIntegersGeometric(),
                        P.roundingModes(),
                        P.positiveRationals()
                )
        );
        for (Quadruple<Real, Integer, RoundingMode, Rational> q : take(LIMIT, qs)) {
            System.out.println("bigDecimalValueByPrecision(" + q.a + ", " + q.b + ", " + q.c + ", " + q.d + ") = " +
                    q.a.bigDecimalValueByPrecision(q.b, q.c, q.d));
        }
    }

    private static boolean scaleAlphaBorderline(int n, @NotNull Rational x) {
        return x.multiply(Rational.TEN.pow(n)).isInteger();
    }

    private static boolean scaleBetaBorderline(int n, @NotNull Rational x) {
        Rational scaled = x.multiply(Rational.TEN.pow(n + 1));
        return scaled.isInteger() && scaled.bigIntegerValueExact().mod(BigInteger.TEN).equals(BigInteger.valueOf(5));
    }

    private static boolean rmBigDecimalScaleCheck(
            @NotNull Algebraic x,
            int scale,
            @NotNull RoundingMode rm,
            @NotNull FuzzinessType ft
    ) {
        int sign = x.signum();
        boolean left = ft == FuzzinessType.LEFT || ft == FuzzinessType.BOTH;
        boolean right = ft == FuzzinessType.RIGHT || ft == FuzzinessType.BOTH;
        if (x.isRational() && scaleAlphaBorderline(scale, x.rationalValueExact())) {
            switch (rm) {
                case UP:
                    return sign != 0 && !(sign == 1 && right) && !(sign == -1 && left);
                case DOWN:
                    return !(sign == 1 && left) && !(sign == -1 && right);
                case CEILING:
                    return !right;
                case FLOOR:
                    return !left;
                case UNNECESSARY:
                    return ft == FuzzinessType.NONE;
                default:
                    return true;
            }
        } else if (x.isRational() && scaleBetaBorderline(scale, x.rationalValueExact())) {
            switch (rm) {
                case HALF_UP:
                    return !(sign == 1 && left) && !(sign == -1 && right);
                case HALF_DOWN:
                    return !(sign == 1 && right) && !(sign == -1 && left);
                case HALF_EVEN:
                    boolean lowerEven =
                            !x.bigDecimalValueByScale(scale, RoundingMode.FLOOR).unscaledValue().testBit(0);
                    return !(lowerEven ? right : left);
                case UNNECESSARY:
                    return false;
                default:
                    return true;
            }
        }
        return rm != RoundingMode.UNNECESSARY;
    }

    private void demoBigDecimalValueByScaleUnsafe_int_RoundingMode() {
        //noinspection RedundantCast
        Iterable<Triple<Real, Integer, RoundingMode>> ts = map(
                u -> {
                    Real r;
                    switch (u.a.b) {
                        case NONE:
                            r = u.a.a.realValue();
                            break;
                        case LEFT:
                            r = leftFuzzyRepresentation(u.a.a.rationalValueExact());
                            break;
                        case RIGHT:
                            r = rightFuzzyRepresentation(u.a.a.rationalValueExact());
                            break;
                        case BOTH:
                            r = fuzzyRepresentation(u.a.a.rationalValueExact());
                            break;
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                    return new Triple<>(r, u.b, u.c);
                },
                filterInfinite(
                        t -> rmBigDecimalScaleCheck(t.a.a, t.b, t.c, t.a.b),
                        P.triples(
                                P.withScale(1).choose(
                                        map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                        P.choose(
                                                (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                                P.withScale(4).rationals()
                                                        )
                                                )
                                        )
                                ),
                                P.integersGeometric(),
                                P.roundingModes()
                        )
                )
        );
        for (Triple<Real, Integer, RoundingMode> t : take(LIMIT, ts)) {
            System.out.println("bigDecimalValueByScaleUnsafe(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.bigDecimalValueByScaleUnsafe(t.b, t.c));
        }
    }

    private void demoBigDecimalValueByScale_int_RoundingMode_Rational() {
        Iterable<Quadruple<Real, Integer, RoundingMode, Rational>> qs = filterInfinite(
                q -> {
                    try {
                        q.a.bigDecimalValueByScale(q.b, q.c, q.d);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                P.quadruples(P.withScale(4).reals(), P.integersGeometric(), P.roundingModes(), P.positiveRationals())
        );
        for (Quadruple<Real, Integer, RoundingMode, Rational> q : take(LIMIT, qs)) {
            System.out.println("bigDecimalValueByScale(" + q.a + ", " + q.b + ", " + q.c + ", " + q.d + ") = " +
                    q.a.bigDecimalValueByScale(q.b, q.c, q.d));
        }
    }

    private void demoBigDecimalValueByPrecisionUnsafe_int() {
        //noinspection RedundantCast
        Iterable<Pair<Real, Integer>> ps = map(
                q -> {
                    Real r;
                    switch (q.a.b) {
                        case NONE:
                            r = q.a.a.realValue();
                            break;
                        case LEFT:
                            r = leftFuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        case RIGHT:
                            r = rightFuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        case BOTH:
                            r = fuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                    return new Pair<>(r, q.b);
                },
                filterInfinite(
                        p -> rmBigDecimalPrecisionCheck(p.a.a, p.b, RoundingMode.HALF_EVEN, p.a.b),
                        P.pairsSquareRootOrder(
                                P.withScale(1).choose(
                                        map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                        P.choose(
                                                (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                                P.withScale(4).rationals()
                                                        )
                                                )
                                        )
                                ),
                                P.naturalIntegersGeometric()
                        )
                )
        );
        for (Pair<Real, Integer> p : take(LIMIT, ps)) {
            System.out.println("bigDecimalValueByPrecisionUnsafe(" + p.a + ", " + p.b + ") = " +
                    p.a.bigDecimalValueByPrecisionUnsafe(p.b));
        }
    }

    private void demoBigDecimalValueByPrecision_int_Rational() {
        Iterable<Triple<Real, Integer, Rational>> ts = filterInfinite(
                t -> {
                    try {
                        t.a.bigDecimalValueByPrecision(t.b, t.c);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                P.triples(P.withScale(4).reals(), P.naturalIntegersGeometric(), P.positiveRationals())
        );
        for (Triple<Real, Integer, Rational> t : take(LIMIT, ts)) {
            System.out.println("bigDecimalValueByPrecision(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.bigDecimalValueByPrecision(t.b, t.c));
        }
    }

    private void demoBigDecimalValueByScaleUnsafe_int() {
        //noinspection RedundantCast
        Iterable<Pair<Real, Integer>> ps = map(
                q -> {
                    Real r;
                    switch (q.a.b) {
                        case NONE:
                            r = q.a.a.realValue();
                            break;
                        case LEFT:
                            r = leftFuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        case RIGHT:
                            r = rightFuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        case BOTH:
                            r = fuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                    return new Pair<>(r, q.b);
                },
                filterInfinite(
                        p -> rmBigDecimalScaleCheck(p.a.a, p.b, RoundingMode.HALF_EVEN, p.a.b),
                        P.pairsSquareRootOrder(
                                P.withScale(1).choose(
                                        map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                        P.choose(
                                                (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                                P.withScale(4).rationals()
                                                        )
                                                )
                                        )
                                ),
                                P.integersGeometric()
                        )
                )
        );
        for (Pair<Real, Integer> p : take(LIMIT, ps)) {
            System.out.println("bigDecimalValueByScaleUnsafe(" + p.a + ", " + p.b + ") = " +
                    p.a.bigDecimalValueByScaleUnsafe(p.b));
        }
    }

    private void demoBigDecimalValueByScale_int_Rational() {
        Iterable<Triple<Real, Integer, Rational>> ts = filterInfinite(
                t -> {
                    try {
                        t.a.bigDecimalValueByScale(t.b, t.c);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                P.triples(P.withScale(4).reals(), P.integersGeometric(), P.positiveRationals())
        );
        for (Triple<Real, Integer, Rational> t : take(LIMIT, ts)) {
            System.out.println("bigDecimalValueByScale(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.bigDecimalValueByScale(t.b, t.c));
        }
    }

    private void demoBigDecimalValueExact() {
        Iterable<Real> xs = map(
                Real::of,
                filterInfinite(r -> r.hasTerminatingBaseExpansion(BigInteger.TEN), P.rationals())
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("bigDecimalValueExact(" + x + ") = " + x.bigDecimalValueExact());
        }
    }

    private void demoNegate() {
        for (Real x : take(LIMIT, P.withScale(4).reals())) {
            System.out.println("-(" + x + ") = " + x.negate());
        }
    }

    private void demoAbs() {
        for (Real x : take(LIMIT, P.withScale(4).reals())) {
            System.out.println("|" + x + "| = " + x.abs());
        }
    }

    private void demoSignumUnsafe() {
        Iterable<Real> xs = P.withScale(1).choose(
                map(Algebraic::realValue, P.withScale(4).algebraics()),
                P.choose(
                        Arrays.asList(
                                map(Real::leftFuzzyRepresentation, P.withScale(4).nonzeroRationals()),
                                map(Real::rightFuzzyRepresentation, P.withScale(4).nonzeroRationals()),
                                map(Real::fuzzyRepresentation, P.withScale(4).nonzeroRationals())
                        )
                )
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("signumUnsafe(" + x + ") = " + x.signumUnsafe());
        }
    }

    private void demoSignum() {
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.positiveRationals()))) {
            System.out.println("signum(" + p.a + ", " + p.b + ") = " + p.a.signum(p.b));
        }
    }

    private void demoAdd_Rational() {
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.rationals()))) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private void demoAdd_Real() {
        for (Pair<Real, Real> p : take(LIMIT, P.pairs(P.withScale(4).reals()))) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private void demoSubtract_Rational() {
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.rationals()))) {
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private void demoSubtract_Real() {
        for (Pair<Real, Real> p : take(LIMIT, P.pairs(P.withScale(4).reals()))) {
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_int() {
        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.integers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        for (Pair<Real, BigInteger> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.bigIntegers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Rational() {
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.rationals()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Real() {
        for (Pair<Real, Real> p : take(LIMIT, P.pairs(P.withScale(4).withSecondaryScale(4).reals()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoInvertUnsafe() {
        for (Real x : take(LIMIT, P.withScale(4).nonzeroReals())) {
            System.out.println("invertUnsafe(" + x + ") = " + x.invertUnsafe());
        }
    }

    private void demoInvert() {
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                filterInfinite(
                        x -> !x.isExact() || x.rationalValueExact().get() != Rational.ZERO,
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("invert(" + p.a + ", " + p.b + ") = " + p.a.invert(p.b));
        }
    }

    private void demoDivide_int() {
        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.nonzeroIntegers()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_BigInteger() {
        for (Pair<Real, BigInteger> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.nonzeroBigIntegers()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_Rational() {
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.nonzeroRationals()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivideUnsafe() {
        for (Pair<Real, Real> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.nonzeroReals()))) {
            System.out.println("divideUnsafe(" + p.a + ", " + p.b + ") = " + p.a.divideUnsafe(p.b));
        }
    }

    private void demoDivide_Real_Rational() {
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                filterInfinite(
                        x -> !x.isExact() || x.rationalValueExact().get() != Rational.ZERO,
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            System.out.println("divide(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.divide(t.b, t.c));
        }
    }

    private void demoShiftLeft() {
        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.integersGeometric()))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoShiftRight() {
        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.integersGeometric()))) {
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    private void demoSum() {
        for (List<Real> xs : take(LIMIT, P.withScale(1).lists(P.withScale(4).reals()))) {
            System.out.println("Σ(" + middle(xs.toString()) + ") = " + sum(xs));
        }
    }

    private void demoProduct() {
        for (List<Real> xs : take(LIMIT, P.withScale(1).lists(P.withScale(4).reals()))) {
            System.out.println("Π(" + middle(xs.toString()) + ") = " + product(xs));
        }
    }

    private void demoDelta_finite() {
        for (List<Real> xs : take(LIMIT, P.withScale(2).listsAtLeast(1, P.withScale(4).reals()))) {
            System.out.println("Δ(" + middle(xs.toString()) + ") = " + its(delta(xs)));
        }
    }

    private void demoDelta_infinite() {
        for (Iterable<Real> xs : take(MEDIUM_LIMIT, P.prefixPermutations(QBarTesting.QEP.reals()))) {
            System.out.println("Δ(" + middle(its(xs)) + ") = " + its(delta(xs)));
        }
    }

    private void demoPowUnsafe_int() {
        Iterable<Pair<Rational, Integer>> rs = filterInfinite(
                p -> p.a != Rational.ZERO || p.b >= 0,
                P.pairsSquareRootOrder(P.withScale(4).rationals(), P.withScale(4).integersGeometric())
        );
        Iterable<Pair<Real, Integer>> ps = P.withScale(1).choose(
                map(
                        q -> new Pair<>(q.a.realValue(), q.b),
                        filterInfinite(
                                p -> p.a != Algebraic.ZERO || p.b >= 0,
                                P.pairsSquareRootOrder(P.withScale(4).algebraics(), P.withScale(4).integersGeometric())
                        )
                ),
                P.choose(
                        Arrays.asList(
                                map(p -> new Pair<>(leftFuzzyRepresentation(p.a), p.b), rs),
                                map(p -> new Pair<>(rightFuzzyRepresentation(p.a), p.b), rs),
                                map(p -> new Pair<>(fuzzyRepresentation(p.a), p.b), rs)
                        )
                )
        );
        for (Pair<Real, Integer> p : take(LIMIT, ps)) {
            System.out.println("powUnsafe(" + p.a + ", " + p.b + ") = " + p.a.powUnsafe(p.b));
        }
    }

    private void demoPow_int_Rational() {
        Iterable<Triple<Real, Integer, Rational>> ts = filterInfinite(
                t -> (!t.a.isExact() || t.a.rationalValueExact().get() != Rational.ZERO) || t.b >= 0,
                P.triples(P.withScale(4).reals(), P.withScale(4).integersGeometric(), P.positiveRationals())
        );
        for (Triple<Real, Integer, Rational> t : take(LIMIT, ts)) {
            System.out.println("pow(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.pow(t.b, t.c));
        }
    }

    private void demoRootOfRational() {
        Iterable<Pair<Rational, Integer>> ps = filterInfinite(
                p -> (p.a != Rational.ZERO || p.b >= 0) && ((p.b & 1) != 0 || p.a.signum() != -1),
                P.pairsSquareRootOrder(P.withScale(4).rationals(), P.nonzeroIntegersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") ^ (1/" + p.b + ") = " + rootOfRational(p.a, p.b));
        }
    }

    private void demoSqrtOfRational() {
        for (Rational x : take(LIMIT, P.withScale(4).rangeUp(Rational.ZERO))) {
            System.out.println("sqrt(" + x + ") = " + sqrtOfRational(x));
        }
    }

    private void demoCbrtOfRational() {
        for (Rational x : take(LIMIT, P.withScale(4).rationals())) {
            System.out.println("cbrt(" + x + ") = " + cbrtOfRational(x));
        }
    }

    private void demoRootUnsafe() {
        Iterable<Pair<Real, Integer>> ps = filterInfinite(
                p -> {
                    if (p.b < 0) {
                        Optional<Boolean> equalsZero = p.a.eq(Rational.ZERO, DEFAULT_RESOLUTION);
                        if (!equalsZero.isPresent() || equalsZero.get()) {
                            return false;
                        }
                    }
                    if ((p.b & 1) == 0) {
                        Optional<Boolean> nonNegative = p.a.ge(Rational.ZERO, DEFAULT_RESOLUTION);
                        if (!nonNegative.isPresent() || !nonNegative.get()) {
                            return false;
                        }
                    }
                    if (Math.abs(p.b) > 6) {
                        // skip raising a fuzzy zero to a high power, since that is slow
                        Optional<Boolean> isZero = p.a.eq(Rational.ZERO, DEFAULT_RESOLUTION);
                        if (!isZero.isPresent()) {
                            return false;
                        }
                    }
                    return true;
                },
                P.pairsSquareRootOrder(P.withScale(4).reals(), P.nonzeroIntegersGeometric())
        );
        for (Pair<Real, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("(" + p.a + ") ^ (1/" + p.b + ") = " + p.a.rootUnsafe(p.b));
        }
    }

    private static boolean rootCheck(@NotNull Algebraic x, int r, @NotNull FuzzinessType ft) {
        if ((r & 1) == 0 && x.signum() == -1) {
            return false;
        }
        if (r < 0 && x == Algebraic.ZERO && ft == FuzzinessType.NONE) {
            return false;
        }
        if (Math.abs(r) > 6) {
            // skip raising a fuzzy zero to a high power, since that is slow
            if (x == Algebraic.ZERO && ft != FuzzinessType.NONE) {
                return false;
            }
        }
        return true;
    }

    private void demoRoot() {
        //noinspection RedundantCast
        Iterable<Triple<Real, Integer, Rational>> ts = map(
                u -> {
                    Real r;
                    switch (u.a.b) {
                        case NONE:
                            r = u.a.a.realValue();
                            break;
                        case LEFT:
                            r = leftFuzzyRepresentation(u.a.a.rationalValueExact());
                            break;
                        case RIGHT:
                            r = rightFuzzyRepresentation(u.a.a.rationalValueExact());
                            break;
                        case BOTH:
                            r = fuzzyRepresentation(u.a.a.rationalValueExact());
                            break;
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                    return new Triple<>(r, u.b, u.c);
                },
                filterInfinite(
                        t -> rootCheck(t.a.a, t.b, t.a.b),
                        P.triples(
                                P.withScale(1).choose(
                                        map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                        P.choose(
                                                (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                                P.withScale(4).rationals()
                                                        )
                                                )
                                        )
                                ),
                                P.nonzeroIntegersGeometric(),
                                P.positiveRationals()
                        )
                )
        );
        for (Triple<Real, Integer, Rational> t : take(MEDIUM_LIMIT, ts)) {
            System.out.println("root(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.root(t.b, t.c));
        }
    }

    private void demoSqrtUnsafe() {
        Iterable<Real> xs = filterInfinite(
                x -> x.ge(Rational.ZERO, DEFAULT_RESOLUTION).orElse(false),
                P.withScale(4).reals()
        );
        for (Real x : take(MEDIUM_LIMIT, xs)) {
            System.out.println("sqrtUnsafe(" + x + ") = " + x.sqrtUnsafe());
        }
    }

    private void demoSqrt() {
        //noinspection RedundantCast
        Iterable<Pair<Real, Rational>> ps = map(
                q -> {
                    Real r;
                    switch (q.a.b) {
                        case NONE:
                            r = q.a.a.realValue();
                            break;
                        case LEFT:
                            r = leftFuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        case RIGHT:
                            r = rightFuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        case BOTH:
                            r = fuzzyRepresentation(q.a.a.rationalValueExact());
                            break;
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                    return new Pair<>(r, q.b);
                },
                filterInfinite(
                        p -> rootCheck(p.a.a, 2, p.a.b),
                        P.pairs(
                                P.withScale(1).choose(
                                        map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                        P.choose(
                                                (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                                P.withScale(4).rationals()
                                                        )
                                                )
                                        )
                                ),
                                P.positiveRationals()
                        )
                )
        );
        for (Pair<Real, Rational> t : take(MEDIUM_LIMIT, ps)) {
            System.out.println("sqrt(" + t.a + ", " + t.b + ") = " + t.a.sqrt(t.b));
        }
    }

    private void demoCbrt() {
        for (Real x : take(MEDIUM_LIMIT, P.withScale(4).reals())) {
            System.out.println("cbrt(" + x + ") = " + x.cbrt());
        }
    }

    private void demoExpOfRational() {
        BigInteger limit = BigInteger.valueOf(SMALL_LIMIT);
        Iterable<Rational> xs = filterInfinite(
                x -> Ordering.le(x.bigIntegerValue().abs(), limit),
                P.withScale(4).rationals()
        );
        for (Rational x : take(LIMIT, xs)) {
            System.out.println("expOfRational(" + x + ") = " + expOfRational(x));
        }
    }

    private void demoExp() {
        BigInteger limit = BigInteger.valueOf(SMALL_LIMIT);
        Iterable<Real> xs = filterInfinite(
                x -> {
                    Optional<BigInteger> i = x.bigIntegerValue(DEFAULT_RESOLUTION);
                    return i.isPresent() && Ordering.le(i.get().abs(), limit);
                },
                P.withScale(4).reals()
        );
        long s = System.currentTimeMillis();
        for (Real x : take(TINY_LIMIT, xs)) {
            System.out.println("exp(" + x + ") = " + x.exp());
        }
        System.out.println("time: " + (System.currentTimeMillis() - s));
    }

    private void demoLogOfRational_Rational() {
        for (Rational x : take(MEDIUM_LIMIT, P.withScale(4).positiveRationals())) {
            System.out.println("logOfRational(" + x + ") = " + logOfRational(x));
        }
    }

    private void demoLogUnsafe() {
        for (Real x : take(TINY_LIMIT, P.withScale(4).positiveReals())) {
            System.out.println("logUnsafe(" + x + ") = " + x.logUnsafe());
        }
    }

    private void demoLog_Rational() {
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                P.withElement(rightFuzzyRepresentation(Rational.ZERO), P.withScale(4).positiveReals()),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(TINY_LIMIT, ps)) {
            System.out.println("log(" + p.a + ", " + p.b + ") = " + p.a.log(p.b));
        }
    }

    private void demoLogOfRational_Rational_Rational() {
        Iterable<Pair<Rational, Rational>> ps = P.pairs(
                P.withScale(4).positiveRationals(),
                filterInfinite(r -> r != Rational.ONE, P.withScale(4).positiveRationals())
        );
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            System.out.println("logOfRational(" + p.a + ", " + p.b + ") = " + logOfRational(p.a, p.b));
        }
    }

    private void demoLogUnsafe_Rational() {
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                P.withScale(4).positiveReals(),
                filterInfinite(r -> r != Rational.ONE, P.withScale(4).positiveRationals())
        );
        for (Pair<Real, Rational> p : take(TINY_LIMIT, ps)) {
            System.out.println("logUnsafe(" + p.a + ", " + p.b + ") = " + p.a.logUnsafe(p.b));
        }
    }

    private void demoLog_Rational_Rational() {
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(
                P.withElement(rightFuzzyRepresentation(Rational.ZERO), P.withScale(4).positiveReals()),
                filterInfinite(r -> r != Rational.ONE, P.withScale(4).positiveRationals()),
                P.positiveRationals()
        );
        for (Triple<Real, Rational, Rational> t : take(TINY_LIMIT, ts)) {
            System.out.println("log(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.log(t.b, t.c));
        }
    }

    private void demoLogUnsafe_Real() {
        Iterable<Pair<Real, Real>> ps = P.pairs(
                P.withScale(4).positiveReals(),
                filterInfinite(
                        x -> x.ne(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                        P.withScale(4).positiveReals()
                )
        );
        for (Pair<Real, Real> p : take(TINY_LIMIT, ps)) {
            System.out.println("logUnsafe(" + p.a + ", " + p.b + ") = " + p.a.logUnsafe(p.b));
        }
    }

    private void demoLog_Real_Rational() {
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(
                P.withElement(rightFuzzyRepresentation(Rational.ZERO), P.withScale(4).positiveReals()),
                P.withElement(
                        rightFuzzyRepresentation(Rational.ZERO),
                        filterInfinite(
                                x -> x.ne(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                                P.withScale(4).positiveReals()
                        )
                ),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(TINY_LIMIT, ts)) {
            System.out.println("log(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.log(t.b, t.c));
        }
    }

    private void demoPowUnsafe_Real() {
        BigInteger lower = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger upper = BigInteger.valueOf(Integer.MAX_VALUE);
        Rational smallResolution = Rational.of(1, 10000);
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> {
                    boolean xZero = p.a.eq(Rational.ZERO, DEFAULT_RESOLUTION).orElse(true);
                    if (xZero) {
                        Optional<Integer> xSign = p.b.signum(DEFAULT_RESOLUTION);
                        if (!xSign.isPresent() || xSign.get() != 0 && xSign.get() != 1) {
                            return false;
                        }
                    }
                    if (!p.a.eq(Rational.ZERO, DEFAULT_RESOLUTION).isPresent()) {
                        if (!p.b.isExact()) {
                            return false;
                        }
                    }
                    if (p.a.lt(Rational.ZERO, DEFAULT_RESOLUTION).orElse(true)) {
                        if (!p.b.isExact() || !p.b.rationalValueExact().get().getDenominator().testBit(0)) {
                            return false;
                        }
                    }
                    if (!p.a.isExact() && p.b.isExact()) {
                        Rational r = p.b.rationalValueExact().get();
                        if (!(Ordering.ge(r.getNumerator(), lower) && Ordering.le(r.getNumerator(), upper) &&
                                        Ordering.le(r.getDenominator(), upper))) {
                            return false;
                        }
                    }
                    return true;
                },
                P.pairs(P.withScale(3).withSecondaryScale(4).reals())
        );
        for (Pair<Real, Real> p : take(TINY_LIMIT, ps)) {
            System.out.println("powUnsafe(" + p.a + ", " + p.b + ") = " +
                    p.a.powUnsafe(p.b).toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoPow_Real_Rational() {
        BigInteger lower = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger upper = BigInteger.valueOf(Integer.MAX_VALUE);
        Rational smallResolution = Rational.of(1, 10000);
        Iterable<Triple<Real, Real, Rational>> ts = filterInfinite(
                t -> {
                    if (t.a.eq(Rational.ZERO, DEFAULT_RESOLUTION).orElse(true)) {
                        if (t.b.lt(Rational.ZERO, DEFAULT_RESOLUTION).orElse(true)) {
                            return false;
                        }
                    }
                    if (!t.a.eq(Rational.ZERO, DEFAULT_RESOLUTION).isPresent()) {
                        if (!t.b.isExact()) {
                            return false;
                        }
                    }
                    if (t.a.lt(Rational.ZERO, DEFAULT_RESOLUTION).orElse(true)) {
                        if (!t.b.isExact() || !t.b.rationalValueExact().get().getDenominator().testBit(0)) {
                            return false;
                        }
                    }
                    if (!t.a.isExact() && t.b.isExact()) {
                        Rational r = t.b.rationalValueExact().get();
                        if (!(Ordering.ge(r.getNumerator(), lower) && Ordering.le(r.getNumerator(), upper) &&
                                        Ordering.le(r.getDenominator(), upper))) {
                            return false;
                        }
                    }
                    return true;
                },
                P.triples(
                        P.withScale(3).withSecondaryScale(4).reals(),
                        filterInfinite(
                                x -> x.abs().lt(Rational.TEN, DEFAULT_RESOLUTION).orElse(true),
                                P.withScale(3).withSecondaryScale(4).reals()
                        ),
                        P.positiveRationals()
                )
        );
        for (Triple<Real, Real, Rational> t : take(TINY_LIMIT, ts)) {
            System.out.println("pow(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.pow(t.b, t.c).map(x -> x.toStringBase(BigInteger.TEN, 3, smallResolution)));
        }
    }

    private void demoSinOfRational() {
        Rational max = Rational.of(100);
        for (Rational x : take(LIMIT, filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).rationals()))) {
            System.out.println("sinOfRational(" + x + ") = " + sinOfRational(x));
        }
    }

    private void demoCosOfRational() {
        Rational max = Rational.of(100);
        for (Rational x : take(LIMIT, filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).rationals()))) {
            System.out.println("cosOfRational(" + x + ") = " + cosOfRational(x));
        }
    }

    private void demoSin() {
        Rational smallResolution = Rational.of(1, 10000);
        Rational max = Rational.of(100);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                P.withScale(4).reals()
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("sin(" + x + ") = " + x.sin().toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoCos() {
        Rational smallResolution = Rational.of(1, 10000);
        Rational max = Rational.of(100);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                P.withScale(4).reals()
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("cos(" + x + ") = " + x.cos().toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoTanOfRational() {
        Rational max = Rational.of(100);
        for (Rational x : take(LIMIT, filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).rationals()))) {
            System.out.println("tanOfRational(" + x + ") = " + tanOfRational(x));
        }
    }

    private void demoCotOfRational() {
        Rational max = Rational.of(100);
        Iterable<Rational> xs = filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).nonzeroRationals());
        for (Rational x : take(LIMIT, xs)) {
            System.out.println("cotOfRational(" + x + ") = " + cotOfRational(x));
        }
    }

    private void demoTanUnsafe() {
        Rational smallResolution = Rational.of(1, 10000);
        Rational max = Rational.of(100);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                P.withScale(4).reals()
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("tanUnsafe(" + x + ") = " +
                    x.tanUnsafe().toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoTan() {
        Rational smallResolution = Rational.of(1, 10000);
        Rational max = Rational.of(100);
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                filterInfinite(
                        x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("tan(" + p.a + ", " + p.b + ") = " +
                    p.a.tan(p.b).map(x -> x.toStringBase(BigInteger.TEN, 3, smallResolution)));
        }
    }

    private void demoCotUnsafe() {
        Rational smallResolution = Rational.of(1, 10000);
        Rational max = Rational.of(100);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                P.withScale(4).nonzeroReals()
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("cotUnsafe(" + x + ") = " +
                    x.cotUnsafe().toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoCot() {
        Rational smallResolution = Rational.of(1, 10000);
        Rational max = Rational.of(100);
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                filterInfinite(
                        x -> (!x.isExact() || x.rationalValueExact().get() != Rational.ZERO) &&
                                x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("cot(" + p.a + ", " + p.b + ") = " +
                    p.a.cot(p.b).map(x -> x.toStringBase(BigInteger.TEN, 3, smallResolution)));
        }
    }

    private void demoSecOfRational() {
        Rational max = Rational.of(100);
        for (Rational x : take(LIMIT, filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).rationals()))) {
            System.out.println("secOfRational(" + x + ") = " + secOfRational(x));
        }
    }

    private void demoCscOfRational() {
        Rational max = Rational.of(100);
        Iterable<Rational> xs = filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).nonzeroRationals());
        for (Rational x : take(LIMIT, xs)) {
            System.out.println("cscOfRational(" + x + ") = " + cscOfRational(x));
        }
    }

    private void demoSecUnsafe() {
        Rational smallResolution = Rational.of(1, 10000);
        Rational max = Rational.of(100);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                P.withScale(4).reals()
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("secUnsafe(" + x + ") = " +
                    x.secUnsafe().toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoSec() {
        Rational smallResolution = Rational.of(1, 10000);
        Rational max = Rational.of(100);
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                filterInfinite(
                        x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("sec(" + p.a + ", " + p.b + ") = " +
                    p.a.sec(p.b).map(x -> x.toStringBase(BigInteger.TEN, 3, smallResolution)));
        }
    }

    private void demoCscUnsafe() {
        Rational smallResolution = Rational.of(1, 10000);
        Rational max = Rational.of(100);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                P.withScale(4).nonzeroReals()
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("cscUnsafe(" + x + ") = " +
                    x.cscUnsafe().toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoCsc() {
        Rational smallResolution = Rational.of(1, 10000);
        Rational max = Rational.of(100);
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                filterInfinite(
                        x -> (!x.isExact() || x.rationalValueExact().get() != Rational.ZERO) &&
                                x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("csc(" + p.a + ", " + p.b + ") = " +
                    p.a.csc(p.b).map(x -> x.toStringBase(BigInteger.TEN, 3, smallResolution)));
        }
    }

    private void demoArctanOfRational() {
        for (Rational x : take(LIMIT, P.withScale(4).rationals())) {
            System.out.println("arctanOfRational(" + x + ") = " + arctanOfRational(x));
        }
    }

    private void demoArccotOfRational() {
        for (Rational x : take(LIMIT, P.withScale(4).rationals())) {
            System.out.println("arccotOfRational(" + x + ") = " + arccotOfRational(x));
        }
    }

    private void demoArctan() {
        Rational smallResolution = Rational.of(1, 10000);
        for (Real x : take(LIMIT, P.withScale(4).reals())) {
            System.out.println("arctan(" + x + ") = " + x.arctan().toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoArccot() {
        Rational smallResolution = Rational.of(1, 10000);
        for (Real x : take(LIMIT, P.withScale(4).reals())) {
            System.out.println("arccot(" + x + ") = " + x.arccot().toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoArcsinOfRational() {
        for (Rational x : take(LIMIT, P.withScale(4).range(Rational.NEGATIVE_ONE, Rational.ONE))) {
            System.out.println("arcsinOfRational(" + x + ") = " + arcsinOfRational(x));
        }
    }

    private void demoArccosOfRational() {
        for (Rational x : take(LIMIT, P.withScale(4).range(Rational.NEGATIVE_ONE, Rational.ONE))) {
            System.out.println("arccosOfRational(" + x + ") = " + arccosOfRational(x));
        }
    }

    private void demoArcsinUnsafe() {
        Rational smallResolution = Rational.of(1, 10000);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                P.withScale(4).realRange(Algebraic.NEGATIVE_ONE, Algebraic.ONE)
        );
        for (Real x : take(MEDIUM_LIMIT, xs)) {
            System.out.println("arcsinUnsafe(" + x + ") = " +
                    x.arcsinUnsafe().toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoArcsin() {
        Rational smallResolution = Rational.of(1, 10000);
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                P.realRange(Algebraic.NEGATIVE_ONE, Algebraic.ONE),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("arcsin(" + p.a + ", " + p.b + ") = " +
                    p.a.arcsin(p.b).map(x -> x.toStringBase(BigInteger.TEN, 3, smallResolution)));
        }
    }

    private void demoArccosUnsafe() {
        Rational smallResolution = Rational.of(1, 10000);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                P.withScale(4).realRange(Algebraic.NEGATIVE_ONE, Algebraic.ONE)
        );
        for (Real x : take(MEDIUM_LIMIT, xs)) {
            System.out.println("arccosUnsafe(" + x + ") = " +
                    x.arccosUnsafe().toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoArccos() {
        Rational smallResolution = Rational.of(1, 10000);
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                P.realRange(Algebraic.NEGATIVE_ONE, Algebraic.ONE),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("arccos(" + p.a + ", " + p.b + ") = " +
                    p.a.arccos(p.b).map(x -> x.toStringBase(BigInteger.TEN, 3, smallResolution)));
        }
    }

    private void demoArcsecOfRational() {
        Iterable<Rational> xs = filterInfinite(x -> Ordering.ge(x.abs(), Rational.ONE), P.withScale(4).rationals());
        for (Rational x : take(LIMIT, xs)) {
            System.out.println("arcsecOfRational(" + x + ") = " + arcsecOfRational(x));
        }
    }

    private void demoArccscOfRational() {
        Iterable<Rational> xs = filterInfinite(x -> Ordering.ge(x.abs(), Rational.ONE), P.withScale(4).rationals());
        for (Rational x : take(LIMIT, xs)) {
            System.out.println("arccscOfRational(" + x + ") = " + arccscOfRational(x));
        }
    }

    private void demoArcsecUnsafe() {
        Rational smallResolution = Rational.of(1, 10000);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().ge(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                P.withScale(4).reals()
        );
        for (Real x : take(MEDIUM_LIMIT, xs)) {
            System.out.println("arcsecUnsafe(" + x + ") = " +
                    x.arcsecUnsafe().toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoArcsec() {
        Rational smallResolution = Rational.of(1, 10000);
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                filterInfinite(
                        x -> x.abs().ge(Rational.ONE, DEFAULT_RESOLUTION).orElse(true),
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("arcsec(" + p.a + ", " + p.b + ") = " +
                    p.a.arcsec(p.b).map(x -> x.toStringBase(BigInteger.TEN, 3, smallResolution)));
        }
    }

    private void demoArccscUnsafe() {
        Rational smallResolution = Rational.of(1, 10000);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().ge(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                P.withScale(4).reals()
        );
        for (Real x : take(MEDIUM_LIMIT, xs)) {
            System.out.println("arccscUnsafe(" + x + ") = " +
                    x.arccscUnsafe().toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoArccsc() {
        Rational smallResolution = Rational.of(1, 10000);
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                filterInfinite(
                        x -> x.abs().ge(Rational.ONE, DEFAULT_RESOLUTION).orElse(true),
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("arccsc(" + p.a + ", " + p.b + ") = " +
                    p.a.arccsc(p.b).map(x -> x.toStringBase(BigInteger.TEN, 3, smallResolution)));
        }
    }

    private void demoAtan2OfRational() {
        Iterable<Pair<Rational, Rational>> ps = filterInfinite(
                p -> p.a != Rational.ZERO || p.b != Rational.ZERO,
                P.pairs(P.withScale(4).rationals())
        );
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            System.out.println("atan2(" + p.a + ", " + p.b + ") = " + atan2OfRational(p.a, p.b));
        }
    }

    private void demoAtan2Unsafe() {
        Rational smallResolution = Rational.of(1, 10000);
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> (p.a.isExact() && p.b.ne(Rational.ZERO, DEFAULT_RESOLUTION).orElse(false)) ||
                        p.a.ne(Rational.ZERO, DEFAULT_RESOLUTION).orElse(false) ||
                        p.a.gt(Rational.ZERO, DEFAULT_RESOLUTION).orElse(false),
                P.pairs(P.withScale(4).reals())
        );
        for (Pair<Real, Real> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("atan2Unsafe(" + p.a + ", " + p.b + ") = " +
                    atan2Unsafe(p.a, p.b).toStringBase(BigInteger.TEN, 3, smallResolution));
        }
    }

    private void demoAtan2() {
        Rational smallResolution = Rational.of(1, 10000);
        Iterable<Triple<Real, Real, Rational>> ts = filterInfinite(
                t -> !t.a.isExact() || !t.b.isExact() || t.a.rationalValueExact().get() != Rational.ZERO ||
                        t.b.rationalValueExact().get() != Rational.ZERO,
                P.triples(P.withScale(4).reals(), P.withScale(4).reals(), P.positiveRationals())
        );
        for (Triple<Real, Real, Rational> t : take(MEDIUM_LIMIT, ts)) {
            System.out.println("atan2(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    atan2(t.a, t.b, t.c).map(x -> x.toStringBase(BigInteger.TEN, 3, smallResolution)));
        }
    }

    private void demoIntervalExtensionUnsafe() {
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.lt(p.b, DEFAULT_RESOLUTION).orElse(false),
                P.pairs(P.withScale(4).reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            System.out.println("intervalExtensionUnsafe(" + p.a + ", " + p.b + ") = " +
                    intervalExtensionUnsafe(p.a, p.b));
        }
    }

    private void demoFractionalPartUnsafe() {
        //noinspection RedundantCast
        Iterable<Real> xs = map(
                q -> {
                    switch (q.b) {
                        case NONE:
                            return q.a.realValue();
                        case LEFT:
                            return leftFuzzyRepresentation(q.a.rationalValueExact());
                        case RIGHT:
                            return rightFuzzyRepresentation(q.a.rationalValueExact());
                        case BOTH:
                            return fuzzyRepresentation(q.a.rationalValueExact());
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                },
                filterInfinite(
                        p -> rmIntCheck(p.a, RoundingMode.FLOOR, p.b),
                        P.withScale(1).choose(
                                map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                P.choose(
                                        (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                        P.withScale(4).rationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                        P.withScale(4).rationals()
                                                ),
                                                map(
                                                        r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                        P.withScale(4).rationals()
                                                )
                                        )
                                )
                        )
                )
        );
        for (Real x : take(LIMIT, xs)) {
            System.out.println("fractionalPartUnsafe(" + x + ") = " + x.fractionalPartUnsafe());
        }
    }

    private void demoFractionalPart() {
        Iterable<Pair<Real, Rational>> ps = P.pairs(P.withScale(4).reals(), P.withScale(4).positiveRationals());
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("fractionalPart(" + p.a + ", " + p.b + ") = " + p.a.fractionalPart(p.b));
        }
    }

    private static boolean rmdCheck(
            @NotNull Algebraic x,
            @NotNull BigInteger denominator,
            @NotNull RoundingMode rm,
            @NotNull FuzzinessType ft
    ) {
        boolean left = ft == FuzzinessType.LEFT || ft == FuzzinessType.BOTH;
        boolean right = ft == FuzzinessType.RIGHT || ft == FuzzinessType.BOTH;
        if (x.isRational() && x.rationalValueExact().multiply(denominator).isInteger()) {
            int sign = x.signum();
            switch (rm) {
                case UP:
                    return sign != 0 && !(sign == 1 && right) && !(sign == -1 && left);
                case DOWN:
                    return !(sign == 1 && left) && !(sign == -1 && right);
                case CEILING:
                    return !right;
                case FLOOR:
                    return !left;
                case UNNECESSARY:
                    return ft == FuzzinessType.NONE;
                default:
                    return true;
            }
        } else if (x.isRational() &&
                x.rationalValueExact().multiply(denominator).getDenominator().equals(IntegerUtils.TWO)) {
            int sign = x.signum();
            switch (rm) {
                case HALF_UP:
                    return !(sign == 1 && left) && !(sign == -1 && right);
                case HALF_DOWN:
                    return !(sign == 1 && right) && !(sign == -1 && left);
                case HALF_EVEN:
                    BigInteger mod4 =
                            x.rationalValueExact().multiply(denominator).getNumerator().and(BigInteger.valueOf(3));
                    return !(mod4.equals(BigInteger.ONE) && right) && !(mod4.equals(BigInteger.valueOf(3)) && left);
                case UNNECESSARY:
                    return false;
                default:
                    return true;
            }
        } else {
            return rm != RoundingMode.UNNECESSARY;
        }
    }

    private void demoRoundToDenominatorUnsafe() {
        //noinspection RedundantCast
        Iterable<Triple<Real, BigInteger, RoundingMode>> ts = map(
                s -> {
                    Real r;
                    switch (s.a.b) {
                        case NONE:
                            r = s.a.a.realValue();
                            break;
                        case LEFT:
                            r = leftFuzzyRepresentation(s.a.a.rationalValueExact());
                            break;
                        case RIGHT:
                            r = rightFuzzyRepresentation(s.a.a.rationalValueExact());
                            break;
                        case BOTH:
                            r = fuzzyRepresentation(s.a.a.rationalValueExact());
                            break;
                        default:
                            throw new IllegalStateException("unreachable");
                    }
                    return new Triple<>(r, s.b, s.c);
                },
                filterInfinite(
                        t -> rmdCheck(t.a.a, t.b, t.c, t.a.b),
                        P.triples(
                                P.withScale(1).choose(
                                        map(x -> new Pair<>(x, FuzzinessType.NONE), P.withScale(4).algebraics()),
                                        P.choose(
                                                (List<Iterable<Pair<Algebraic, FuzzinessType>>>) Arrays.asList(
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.LEFT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.RIGHT),
                                                                P.withScale(4).rationals()
                                                        ),
                                                        map(
                                                                r -> new Pair<>(Algebraic.of(r), FuzzinessType.BOTH),
                                                                P.withScale(4).rationals()
                                                        )
                                                )
                                        )
                                ),
                                P.positiveBigIntegers(),
                                P.roundingModes()
                        )
                )
        );
        for (Triple<Real, BigInteger, RoundingMode> t : take(LIMIT, ts)) {
            System.out.println("roundToDenominatorUnsafe(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.roundToDenominatorUnsafe(t.b, t.c));
        }
    }

    private void demoRoundToDenominator() {
        Iterable<Quadruple<Real, BigInteger, RoundingMode, Rational>> qs = filterInfinite(
                r -> r.c != RoundingMode.UNNECESSARY || r.a.multiply(r.b).isExactInteger(),
                P.quadruples(
                        P.withScale(4).reals(),
                        P.positiveBigIntegers(),
                        P.roundingModes(),
                        P.withScale(4).positiveRationals()
                )
        );
        for (Quadruple<Real, BigInteger, RoundingMode, Rational> q : take(LIMIT, qs)) {
            System.out.println("roundToDenominator(" + q.a + ", " + q.b + ", " + q.c + ", " + q.d + ") = " +
                    q.a.roundToDenominator(q.b, q.c, q.d));
        }
    }

    private void demoContinuedFractionUnsafe() {
        for (Real x : take(LIMIT, P.withScale(4).cleanReals())) {
            System.out.println("continuedFractionUnsafe(" + x + ") = " + its(x.continuedFractionUnsafe()));
        }
    }

    private void demoFromContinuedFraction_finite() {
        Iterable<List<BigInteger>> iss = map(
                p -> toList(cons(p.a, p.b)),
                P.pairs(P.withScale(4).bigIntegers(), P.withScale(4).lists(P.withScale(4).positiveBigIntegers()))
        );
        for (List<BigInteger> is : take(LIMIT, iss)) {
            System.out.println("fromContinuedFraction(" + middle(is.toString()) + ") = " + fromContinuedFraction(is));
        }
    }

    private void demoFromContinuedFraction_infinite() {
        Iterable<Iterable<BigInteger>> iss = map(
                p -> cons(p.a, p.b),
                P.pairs(P.bigIntegers(), P.prefixPermutations(EP.positiveBigIntegers()))
        );
        for (Iterable<BigInteger> is : take(MEDIUM_LIMIT, iss)) {
            System.out.println("fromContinuedFraction(" + middle(its(is)) + ") = " + fromContinuedFraction(is));
        }
    }

    private void demoConvergentsUnsafe() {
        for (Real x : take(MEDIUM_LIMIT, P.withScale(4).cleanReals())) {
            System.out.println("convergentsUnsafe(" + x + ") = " + its(x.convergentsUnsafe()));
        }
    }

    private void demoDigitsUnsafe() {
        Iterable<Rational> nonNegativeRationals = P.withElement(Rational.ZERO, P.withScale(4).positiveRationals());
        //noinspection Convert2MethodRef
        Iterable<Pair<Real, BigInteger>> ps = filterInfinite(
                (Pair<Real, BigInteger> p) -> {
                    Optional<Pair<List<BigInteger>, Iterable<BigInteger>>> digits =
                            p.a.digits(p.b, DEFAULT_RESOLUTION);
                    if (!digits.isPresent() || !lengthAtLeast(TINY_LIMIT, digits.get().b)) {
                        return false;
                    } else {
                        for (BigInteger d : take(TINY_LIMIT, digits.get().b)) {
                            if (d.equals(IntegerUtils.NEGATIVE_ONE)) {
                                return false;
                            }
                        }
                    }
                    return true;
                },
                P.pairs(
                        P.withScale(1).choose(
                                map(
                                        Algebraic::realValue,
                                        P.withElement(Algebraic.ZERO, P.withScale(4).positiveAlgebraics())
                                ),
                                P.choose(
                                        Arrays.asList(
                                                map(Real::leftFuzzyRepresentation, nonNegativeRationals),
                                                map(Real::rightFuzzyRepresentation, nonNegativeRationals),
                                                map(Real::fuzzyRepresentation, nonNegativeRationals)
                                        )
                                )
                        ),
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
                )
        );
        for (Pair<Real, BigInteger> p : take(MEDIUM_LIMIT, ps)) {
            Pair<List<BigInteger>, Iterable<BigInteger>> digits = p.a.digitsUnsafe(p.b);
            System.out.println("digitsUnsafe(" + p.a + ", " + p.b + ") = " +
                    new Pair<>(digits.a, itsList(TINY_LIMIT, digits.b)));
        }
    }

    private void demoDigits() {
        //noinspection Convert2MethodRef
        Iterable<Triple<Real, BigInteger, Rational>> ts = P.triples(
                P.realRangeUp(Algebraic.ZERO),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                P.positiveRationals()
        );
        for (Triple<Real, BigInteger, Rational> t : take(LIMIT, ts)) {
            Optional<Pair<List<BigInteger>, List<String>>> digits =
                    t.a.digits(t.b, t.c).map(p -> new Pair<>(p.a, itsList(TINY_LIMIT, p.b)));
            System.out.println("digits(" + t.a + ", " + t.b + ", " + t.c + ") = " + digits);
        }
    }

    private void demoFromDigits_finite() {
        //noinspection Convert2MethodRef
        Iterable<Triple<BigInteger, List<BigInteger>, List<BigInteger>>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairsInfinite(
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                        b -> P.pairs(P.withScale(4).lists(P.range(BigInteger.ZERO, b.subtract(BigInteger.ONE))))
                )
        );
        for (Triple<BigInteger, List<BigInteger>, List<BigInteger>> t : take(LIMIT, ts)) {
            System.out.println("fromDigits(" + t.a + ", " + t.b + ", " + t.c + ") = " + fromDigits(t.a, t.b, t.c));
        }
    }

    private void demoFromDigits_infinite() {
        //noinspection Convert2MethodRef
        Iterable<Triple<BigInteger, List<BigInteger>, Iterable<BigInteger>>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairsInfinite(
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                        b -> P.pairs(
                                P.withScale(4).lists(P.range(BigInteger.ZERO, b.subtract(BigInteger.ONE))),
                                map(
                                        IterableUtils::cycle,
                                        P.withScale(4).listsAtLeast(
                                                1,
                                                P.range(BigInteger.ZERO, b.subtract(BigInteger.ONE))
                                        )
                                )
                        )
                )
        );
        for (Triple<BigInteger, List<BigInteger>, Iterable<BigInteger>> t : take(MEDIUM_LIMIT, ts)) {
            System.out.println("fromDigits(" + t.a + ", " + t.b + ", " + itsList(TINY_LIMIT, t.c) + ") = " +
                    fromDigits(t.a, t.b, t.c));
        }
    }

    private void demoLiouville() {
        for (BigInteger i : take(LIMIT, P.withScale(8).rangeUp(IntegerUtils.TWO))) {
            System.out.println("liouville(" + i + ") = " + liouville(i));
        }
    }

    private void demoChampernowne() {
        for (BigInteger i : take(LIMIT, P.withScale(8).rangeUp(IntegerUtils.TWO))) {
            System.out.println("champernowne(" + i + ") = " + champernowne(i));
        }
    }

    private void demoCopelandErdos() {
        for (BigInteger i : take(LIMIT, P.withScale(8).rangeUp(IntegerUtils.TWO))) {
            System.out.println("copelandErdos(" + i + ") = " + copelandErdos(i));
        }
    }

    private void demoGreedyNormal() {
        for (int i : take(MEDIUM_LIMIT, P.rangeUpGeometric(2))) {
            System.out.println("greedyNormal(" + i + ") = " + greedyNormal(i));
        }
    }

    private void demoToStringBaseUnsafe() {
        //noinspection Convert2MethodRef
        Iterable<Triple<Real, BigInteger, Integer>> ts = map(
                (Pair<Pair<BigInteger, Integer>, Real> p) -> new Triple<>(p.b, p.a.a, p.a.b),
                P.dependentPairsInfinite(
                        P.pairs(
                                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                                P.withScale(16).integersGeometric()
                        ),
                        q -> {
                            Rational multiplier = Rational.of(q.a).pow(q.b);
                            Iterable<Rational> rs = filterInfinite(
                                    r -> !r.multiply(multiplier).isInteger(),
                                    P.withScale(4).rationals()
                            );
                            return P.withScale(1).choose(
                                    map(
                                            Algebraic::realValue,
                                            P.withElement(Algebraic.ZERO, P.withScale(4).positiveAlgebraics())
                                    ),
                                    P.choose(
                                            Arrays.asList(
                                                    map(Real::leftFuzzyRepresentation, rs),
                                                    map(Real::rightFuzzyRepresentation, rs),
                                                    map(Real::fuzzyRepresentation, rs)
                                            )
                                    )
                            );
                        }
                )
        );
        for (Triple<Real, BigInteger, Integer> t : take(LIMIT, ts)) {
            System.out.println("toStringBaseUnsafe(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.toStringBaseUnsafe(t.b, t.c));
        }
    }

    private void demoToStringBase() {
        //noinspection Convert2MethodRef
        Iterable<Quadruple<Real, BigInteger, Integer, Rational>> qs = map(
                p -> new Quadruple<>(p.a.a, p.a.b, p.a.c, p.b),
                P.dependentPairsInfiniteIdentityHash(
                        P.triples(
                                P.withScale(4).reals(),
                                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                                P.withScale(16).integersGeometric()
                        ),
                        t -> filterInfinite(
                                x -> x != Rational.ZERO,
                                P.range(Rational.ZERO, Rational.of(t.b).pow(-t.c).shiftRight(1))
                        )
                )
        );
        for (Quadruple<Real, BigInteger, Integer, Rational> q : take(LIMIT, qs)) {
            System.out.println("toStringBase(" + q.a + ", " + q.b + ", " + q.c + ", " + q.d + ") = " +
                    q.a.toStringBase(q.b, q.c, q.d));
        }
    }

    private void demoCompareToUnsafe_Rational() {
        //noinspection RedundantCast
        Iterable<Pair<Real, Rational>> ps = map(
                p -> new Pair<>(p.b, p.a),
                (Iterable<Pair<Rational, Real>>) P.dependentPairsInfinite(
                        P.withScale(4).rationals(),
                        r -> {
                            Iterable<Rational> rs = filterInfinite(s -> !r.equals(s), P.withScale(4).rationals());
                            return P.withScale(1).choose(
                                    map(Algebraic::realValue, P.withScale(4).algebraics()),
                                    P.choose(
                                            Arrays.asList(
                                                    map(Real::leftFuzzyRepresentation, rs),
                                                    map(Real::rightFuzzyRepresentation, rs),
                                                    map(Real::fuzzyRepresentation, rs)
                                            )
                                    )
                            );
                        }
                )
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("compareToUnsafe(" + p.a + ", " + p.b + ") = " + p.a.compareToUnsafe(p.b));
        }
    }

    private void demoCompareTo_Rational_Rational() {
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).rationals(),
                P.positiveRationals()
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            System.out.println("compareTo(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.compareTo(t.b, t.c));
        }
    }

    private void demoEqUnsafe_Rational() {
        //noinspection RedundantCast
        Iterable<Pair<Real, Rational>> ps = map(
                p -> new Pair<>(p.b, p.a),
                (Iterable<Pair<Rational, Real>>) P.dependentPairsInfinite(
                        P.withScale(4).rationals(),
                        r -> {
                            Iterable<Rational> rs = filterInfinite(s -> !r.equals(s), P.withScale(4).rationals());
                            return P.withScale(1).choose(
                                    map(Algebraic::realValue, P.withScale(4).algebraics()),
                                    P.choose(
                                            Arrays.asList(
                                                    map(Real::leftFuzzyRepresentation, rs),
                                                    map(Real::rightFuzzyRepresentation, rs),
                                                    map(Real::fuzzyRepresentation, rs)
                                            )
                                    )
                            );
                        }
                )
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("eqUnsafe(" + p.a + ", " + p.b + ") = " + p.a.eqUnsafe(p.b));
        }
    }

    private void demoNeUnsafe_Rational() {
        //noinspection RedundantCast
        Iterable<Pair<Real, Rational>> ps = map(
                p -> new Pair<>(p.b, p.a),
                (Iterable<Pair<Rational, Real>>) P.dependentPairsInfinite(
                        P.withScale(4).rationals(),
                        r -> {
                            Iterable<Rational> rs = filterInfinite(s -> !r.equals(s), P.withScale(4).rationals());
                            return P.withScale(1).choose(
                                    map(Algebraic::realValue, P.withScale(4).algebraics()),
                                    P.choose(
                                            Arrays.asList(
                                                    map(Real::leftFuzzyRepresentation, rs),
                                                    map(Real::rightFuzzyRepresentation, rs),
                                                    map(Real::fuzzyRepresentation, rs)
                                            )
                                    )
                            );
                        }
                )
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("neUnsafe(" + p.a + ", " + p.b + ") = " + p.a.neUnsafe(p.b));
        }
    }

    private void demoLtUnsafe_Rational() {
        Iterable<Pair<Real, Rational>> ps = filterInfinite(
                p -> p.a.lt(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.withScale(4).reals(), P.withScale(4).rationals())
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("ltUnsafe(" + p.a + ", " + p.b + ") = " + p.a.ltUnsafe(p.b));
        }
    }

    private void demoGtUnsafe_Rational() {
        Iterable<Pair<Real, Rational>> ps = filterInfinite(
                p -> p.a.gt(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.withScale(4).reals(), P.withScale(4).rationals())
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("gtUnsafe(" + p.a + ", " + p.b + ") = " + p.a.gtUnsafe(p.b));
        }
    }

    private void demoLeUnsafe_Rational() {
        //noinspection RedundantCast
        Iterable<Pair<Real, Rational>> ps = map(
                p -> new Pair<>(p.b, p.a),
                (Iterable<Pair<Rational, Real>>) P.dependentPairsInfinite(
                        P.withScale(4).rationals(),
                        r -> {
                            Iterable<Rational> rs = filterInfinite(s -> !r.equals(s), P.withScale(4).rationals());
                            return P.withScale(1).choose(
                                    map(Algebraic::realValue, P.withScale(4).algebraics()),
                                    P.choose(
                                            Arrays.asList(
                                                    map(Real::leftFuzzyRepresentation, P.withScale(4).rationals()),
                                                    map(Real::rightFuzzyRepresentation, rs),
                                                    map(Real::fuzzyRepresentation, rs)
                                            )
                                    )
                            );
                        }
                )
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("leUnsafe(" + p.a + ", " + p.b + ") = " + p.a.leUnsafe(p.b));
        }
    }

    private void demoGeUnsafe_Rational() {
        //noinspection RedundantCast
        Iterable<Pair<Real, Rational>> ps = map(
                p -> new Pair<>(p.b, p.a),
                (Iterable<Pair<Rational, Real>>) P.dependentPairsInfinite(
                        P.withScale(4).rationals(),
                        r -> {
                            Iterable<Rational> rs = filterInfinite(s -> !r.equals(s), P.withScale(4).rationals());
                            return P.withScale(1).choose(
                                    map(Algebraic::realValue, P.withScale(4).algebraics()),
                                    P.choose(
                                            Arrays.asList(
                                                    map(Real::leftFuzzyRepresentation, rs),
                                                    map(Real::rightFuzzyRepresentation, P.withScale(4).rationals()),
                                                    map(Real::fuzzyRepresentation, rs)
                                            )
                                    )
                            );
                        }
                )
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            System.out.println("geUnsafe(" + p.a + ", " + p.b + ") = " + p.a.geUnsafe(p.b));
        }
    }

    private void demoEq_Rational_Rational() {
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).rationals(),
                P.positiveRationals()
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            System.out.println("eq(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.eq(t.b, t.c));
        }
    }

    private void demoNe_Rational_Rational() {
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).rationals(),
                P.positiveRationals()
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            System.out.println("ne(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.ne(t.b, t.c));
        }
    }

    private void demoLt_Rational_Rational() {
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).rationals(),
                P.positiveRationals()
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            System.out.println("lt(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.lt(t.b, t.c));
        }
    }

    private void demoGt_Rational_Rational() {
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).rationals(),
                P.positiveRationals()
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            System.out.println("gt(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.gt(t.b, t.c));
        }
    }

    private void demoLe_Rational_Rational() {
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).rationals(),
                P.positiveRationals()
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            System.out.println("le(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.le(t.b, t.c));
        }
    }

    private void demoGe_Rational_Rational() {
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).rationals(),
                P.positiveRationals()
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            System.out.println("ge(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.ge(t.b, t.c));
        }
    }

    private void demoCompareToUnsafe_Real() {
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.eq(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.withScale(4).reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            System.out.println("compareToUnsafe(" + p.a + ", " + p.b + ") = " + p.a.compareToUnsafe(p.b));
        }
    }

    private void demoCompareTo_Real_Rational() {
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).reals(),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            System.out.println("compareTo(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.compareTo(t.b, t.c));
        }
    }

    private void demoEqUnsafe_Real() {
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.eq(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.withScale(4).reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            System.out.println("eqUnsafe(" + p.a + ", " + p.b + ") = " + p.a.eqUnsafe(p.b));
        }
    }

    private void demoNeUnsafe_Real() {
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.eq(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.withScale(4).reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            System.out.println("neUnsafe(" + p.a + ", " + p.b + ") = " + p.a.neUnsafe(p.b));
        }
    }

    private void demoLtUnsafe_Real() {
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.eq(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.withScale(4).reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            System.out.println("ltUnsafe(" + p.a + ", " + p.b + ") = " + p.a.ltUnsafe(p.b));
        }
    }

    private void demoGtUnsafe_Real() {
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.eq(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.withScale(4).reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            System.out.println("gtUnsafe(" + p.a + ", " + p.b + ") = " + p.a.gtUnsafe(p.b));
        }
    }

    private void demoLeUnsafe_Real() {
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.le(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.withScale(4).reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            System.out.println("leUnsafe(" + p.a + ", " + p.b + ") = " + p.a.leUnsafe(p.b));
        }
    }

    private void demoGeUnsafe_Real() {
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.ge(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.withScale(4).reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            System.out.println("geUnsafe(" + p.a + ", " + p.b + ") = " + p.a.geUnsafe(p.b));
        }
    }

    private void demoEq_Real_Rational() {
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).reals(),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            System.out.println("eq(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.eq(t.b, t.c));
        }
    }

    private void demoNe_Real_Rational() {
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).reals(),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            System.out.println("ne(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.ne(t.b, t.c));
        }
    }

    private void demoLt_Real_Rational() {
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).reals(),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            System.out.println("lt(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.lt(t.b, t.c));
        }
    }

    private void demoGt_Real_Rational() {
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).reals(),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            System.out.println("gt(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.gt(t.b, t.c));
        }
    }

    private void demoLe_Real_Rational() {
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).reals(),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            System.out.println("le(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.le(t.b, t.c));
        }
    }

    private void demoGe_Real_Rational() {
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(4).reals(),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            System.out.println("ge(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.ge(t.b, t.c));
        }
    }

    private void demoToString() {
        for (Real x : take(LIMIT, P.reals())) {
            System.out.println(x);
        }
    }
}
