package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.CachedIterator;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.FloatingPointUtils;
import mho.wheels.numberUtils.IntegerUtils;
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
        Pair<List<BigInteger>, Iterable<BigInteger>> digits = c.digitsUnsafe(BigInteger.TEN);
        String beforeDecimal =
                IntegerUtils.toStringBase(BigInteger.TEN, IntegerUtils.fromBigEndianDigits(BigInteger.TEN, digits.a));
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

    private void demoPrimeConstant() {
        constant_helper(PRIME_CONSTANT);
    }

    private void demoThueMorse() {
        constant_helper(THUE_MORSE);
    }

    private void demoKolakoski() {
        constant_helper(KOLAKOSKI);
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
            System.out.println("ltUnsafe(" + p.a + ", " + p.b + ") = " + p.a.ltUnsafe(p.b));
        }
    }

    private void demoGtUnsafe_Rational() {
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
}
