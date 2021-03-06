package mho.qbar.objects;

import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.CachedIterator;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.BigDecimalUtils;
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
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static mho.qbar.objects.Real.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.numberUtils.FloatingPointUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;
import static mho.wheels.testing.Testing.assertTrue;

public class RealProperties extends QBarTestProperties {
    private static final @NotNull String REAL_CHARS = "-.0123456789~";
    private static final @NotNull BigInteger ASCII_ALPHANUMERIC_COUNT = BigInteger.valueOf(36);
    private static final @NotNull Rational SMALL_RESOLUTION = Rational.of(1, 256);

    public RealProperties() {
        super("Real");
    }

    @Override
    protected void testBothModes() {
        propertiesOf_Rational();
        propertiesOf_BigInteger();
        propertiesOf_long();
        propertiesOf_int();
        propertiesOf_BinaryFraction();
        propertiesOf_float();
        propertiesOf_double();
        propertiesOfExact_float();
        propertiesOfExact_double();
        propertiesOf_BigDecimal();
        propertiesFuzzyRepresentation();
        propertiesLeftFuzzyRepresentation();
        propertiesRightFuzzyRepresentation();
        propertiesIterator();
        propertiesMatch();
        propertiesIsExactInteger();
        propertiesBigIntegerValueUnsafe_RoundingMode();
        propertiesBigIntegerValue_RoundingMode_Rational();
        propertiesBigIntegerValueUnsafe();
        propertiesBigIntegerValue_Rational();
        propertiesFloorUnsafe();
        propertiesFloor();
        propertiesCeilingUnsafe();
        propertiesCeiling();
        propertiesBigIntegerValueExact();
        propertiesByteValueExact();
        propertiesShortValueExact();
        propertiesIntValueExact();
        propertiesLongValueExact();
        propertiesIsExactIntegerPowerOfTwo();
        propertiesRoundUpToIntegerPowerOfTwoUnsafe();
        propertiesRoundUpToIntegerPowerOfTwo();
        propertiesIsExactBinaryFraction();
        propertiesBinaryFractionValueExact();
        propertiesIsExact();
        propertiesRationalValueExact();
        propertiesBinaryExponentUnsafe();
        propertiesBinaryExponent();
        propertiesIsExactAndEqualToFloat();
        propertiesIsExactAndEqualToDouble();
        propertiesFloatValueUnsafe_RoundingMode();
        propertiesFloatValue_RoundingMode_Rational();
        propertiesFloatValueUnsafe();
        propertiesFloatValue_Rational();
        propertiesFloatValueExact();
        propertiesDoubleValueUnsafe_RoundingMode();
        propertiesDoubleValue_RoundingMode_Rational();
        propertiesDoubleValueUnsafe();
        propertiesDoubleValue_Rational();
        propertiesDoubleValueExact();
        propertiesBigDecimalValueByPrecisionUnsafe_int_RoundingMode();
        propertiesBigDecimalValueByPrecision_int_RoundingMode_Rational();
        propertiesBigDecimalValueByScaleUnsafe_int_RoundingMode();
        propertiesBigDecimalValueByScale_int_RoundingMode_Rational();
        propertiesBigDecimalValueByPrecisionUnsafe_int();
        propertiesBigDecimalValueByPrecision_int_Rational();
        propertiesBigDecimalValueByScaleUnsafe_int();
        propertiesBigDecimalValueByScale_int_Rational();
        propertiesBigDecimalValueExact();
        propertiesNegate();
        propertiesAbs();
        propertiesSignumUnsafe();
        propertiesSignum();
        propertiesAdd_Rational();
        propertiesAdd_Real();
        propertiesSubtract_Rational();
        propertiesSubtract_Real();
        propertiesMultiply_int();
        propertiesMultiply_BigInteger();
        propertiesMultiply_Rational();
        propertiesMultiply_Real();
        propertiesInvertUnsafe();
        propertiesInvert();
        propertiesDivide_int();
        propertiesDivide_BigInteger();
        propertiesDivide_Rational();
        propertiesDivideUnsafe_Real();
        propertiesDivide_Real_Rational();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesShiftRight();
        compareImplementationsShiftRight();
        propertiesSum();
        compareImplementationsSum();
        propertiesProduct();
        compareImplementationsProduct();
        propertiesDelta();
        propertiesPowUnsafe_int();
        propertiesPow_int_Rational();
        propertiesRootOfRational();
        propertiesSqrtOfRational();
        propertiesCbrtOfRational();
        propertiesRootUnsafe();
        propertiesRoot();
        propertiesSqrtUnsafe();
        propertiesSqrt();
        propertiesCbrt();
        propertiesExpOfRational();
        propertiesExp();
        propertiesLogOfRational_Rational();
        propertiesLogUnsafe();
        propertiesLog_Rational();
        propertiesLogOfRational_Rational_Rational();
        propertiesLogUnsafe_Rational();
        propertiesLog_Rational_Rational();
        propertiesLogUnsafe_Real();
        propertiesLog_Real_Rational();
        propertiesPowUnsafe_Real();
        propertiesPow_Real_Rational();
        propertiesSinOfRational();
        propertiesCosOfRational();
        propertiesSin();
        propertiesCos();
        propertiesTanOfRational();
        propertiesCotOfRational();
        propertiesTanUnsafe();
        propertiesTan();
        propertiesCotUnsafe();
        propertiesCot();
        propertiesSecOfRational();
        propertiesCscOfRational();
        propertiesSecUnsafe();
        propertiesSec();
        propertiesCscUnsafe();
        propertiesCsc();
        propertiesArctanOfRational();
        propertiesArccotOfRational();
        propertiesArctan();
        propertiesArccot();
        propertiesArcsinOfRational();
        propertiesArccosOfRational();
        propertiesArcsinUnsafe();
        propertiesArcsin();
        propertiesArccosUnsafe();
        propertiesArccos();
        propertiesArcsecOfRational();
        propertiesArccscOfRational();
        propertiesArcsecUnsafe();
        propertiesArcsec();
        propertiesArccscUnsafe();
        propertiesArccsc();
        propertiesAtan2OfRational();
        propertiesAtan2Unsafe();
        propertiesAtan2();
        propertiesIntervalExtensionUnsafe();
        propertiesFractionalPartUnsafe();
        propertiesFractionalPart();
        propertiesRoundToDenominatorUnsafe();
        propertiesRoundToDenominator();
        propertiesContinuedFractionUnsafe();
        propertiesFromContinuedFraction();
        propertiesConvergentsUnsafe();
        propertiesDigitsUnsafe();
        propertiesDigits();
        propertiesFromDigits();
        propertiesLiouville();
        propertiesChampernowne();
        propertiesCopelandErdos();
        propertiesGreedyNormal();
        propertiesToStringBaseUnsafe();
        propertiesToStringBase();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareToUnsafe_Rational();
        propertiesCompareTo_Rational_Rational();
        propertiesEqUnsafe_Rational();
        propertiesNeUnsafe_Rational();
        propertiesLtUnsafe_Rational();
        propertiesGtUnsafe_Rational();
        propertiesLeUnsafe_Rational();
        propertiesGeUnsafe_Rational();
        propertiesEq_Rational_Rational();
        propertiesNe_Rational_Rational();
        propertiesLt_Rational_Rational();
        propertiesGt_Rational_Rational();
        propertiesLe_Rational_Rational();
        propertiesGe_Rational_Rational();
        propertiesCompareToUnsafe_Real();
        propertiesCompareTo_Real_Rational();
        propertiesEqUnsafe_Real();
        propertiesNeUnsafe_Real();
        propertiesLtUnsafe_Real();
        propertiesGtUnsafe_Real();
        propertiesLeUnsafe_Real();
        propertiesGeUnsafe_Real();
        propertiesEq_Real_Rational();
        propertiesNe_Real_Rational();
        propertiesLt_Real_Rational();
        propertiesGt_Real_Rational();
        propertiesLe_Real_Rational();
        propertiesGe_Real_Rational();
        propertiesToString();
    }

    private void propertiesOf_Rational() {
        initialize("of(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Real x = of(r);
            x.validate();
            assertTrue(r, x.isExact());
            inverse(Real::of, (Real y) -> y.rationalValueExact().get(), r);
        }
    }

    private void propertiesOf_BigInteger() {
        initialize("of(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Real x = of(i);
            x.validate();
            assertTrue(i, x.isExactInteger());
        }
    }

    private void propertiesOf_long() {
        initialize("of(long)");
        Rational lowerLimit = Rational.of(Long.MIN_VALUE);
        Rational upperLimit = Rational.of(Long.MAX_VALUE);
        for (long l : take(LIMIT, P.longs())) {
            Real x = of(l);
            x.validate();
            assertTrue(l, x.isExactInteger());
            assertTrue(l, x.geUnsafe(lowerLimit));
            assertTrue(l, x.leUnsafe(upperLimit));
        }
    }

    private void propertiesOf_int() {
        initialize("of(int)");
        Rational lowerLimit = Rational.of(Integer.MIN_VALUE);
        Rational upperLimit = Rational.of(Integer.MAX_VALUE);
        for (int i : take(LIMIT, P.integers())) {
            Real x = of(i);
            x.validate();
            assertTrue(i, x.isExactInteger());
            assertTrue(i, x.geUnsafe(lowerLimit));
            assertTrue(i, x.leUnsafe(upperLimit));
        }
    }

    private void propertiesOf_BinaryFraction() {
        initialize("of(BinaryFraction)");
        for (BinaryFraction bf : take(LIMIT, P.binaryFractions())) {
            Real x = of(bf);
            x.validate();
            assertEquals(
                    bf,
                    of(bf.getMantissa()).multiply(Rational.ONE.shiftLeft(bf.getExponent())).rationalValueExact().get(),
                    x.rationalValueExact().get()
            );
            assertTrue(bf, x.isExactBinaryFraction());
            inverse(Real::of, Real::binaryFractionValueExact, bf);
        }
    }

    private void propertiesOf_float() {
        initialize("of(float)");
        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            Real x = of(f).get();
            x.validate();
            assertTrue(f, x.rationalValueExact().get().hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            Real x = of(f).get();
            aeqf(f, f, x.floatValueUnsafe());
            assertTrue(f, eq(new BigDecimal(Float.toString(f)), x.rationalValueExact().get().bigDecimalValueExact()));
        }
    }

    private void propertiesOf_double() {
        initialize("of(double)");
        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            Real x = of(d).get();
            x.validate();
            assertTrue(d, x.rationalValueExact().get().hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            Real x = of(d).get();
            aeqd(d, d, x.doubleValueUnsafe());
            assertTrue(d, eq(new BigDecimal(Double.toString(d)), x.rationalValueExact().get().bigDecimalValueExact()));
        }
    }

    private void propertiesOfExact_float() {
        initialize("ofExact(float)");
        for (float f : take(LIMIT, P.floats())) {
            Optional<Real> ox = ofExact(f);
            assertEquals(f, Float.isFinite(f), ox.isPresent());
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            //noinspection Convert2MethodRef
            inverse(g -> ofExact(g).get(), (Real x) -> x.floatValueExact(), f);
        }

        int x = 1 << (FLOAT_EXPONENT_WIDTH - 1);
        BigInteger y = BigInteger.ONE.shiftLeft(-MIN_SUBNORMAL_FLOAT_EXPONENT);
        BigInteger z = BigInteger.ONE.shiftLeft(x).subtract(BigInteger.ONE.shiftLeft(x - FLOAT_FRACTION_WIDTH - 1));
        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            Real a = ofExact(f).get();
            a.validate();
            Rational r = a.rationalValueExact().get();
            assertTrue(f, IntegerUtils.isPowerOfTwo(r.getDenominator()));
            assertTrue(f, le(r.getDenominator(), y));
            assertTrue(f, le(r.getNumerator(), z));
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            Real a = ofExact(f).get();
            aeqf(f, f, a.floatValueExact());
        }
    }

    private void propertiesOfExact_double() {
        initialize("ofExact(double)");
        for (double d : take(LIMIT, P.doubles())) {
            Optional<Real> ox = ofExact(d);
            assertEquals(d, Double.isFinite(d), ox.isPresent());
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            inverse(e -> ofExact(e).get(), Real::doubleValueExact, d);
        }

        int x = 1 << (DOUBLE_EXPONENT_WIDTH - 1);
        BigInteger y = BigInteger.ONE.shiftLeft(-MIN_SUBNORMAL_DOUBLE_EXPONENT);
        BigInteger z = BigInteger.ONE.shiftLeft(x).subtract(BigInteger.ONE.shiftLeft(x - DOUBLE_FRACTION_WIDTH - 1));
        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            Real a = ofExact(d).get();
            a.validate();
            Rational r = a.rationalValueExact().get();
            assertTrue(d, IntegerUtils.isPowerOfTwo(r.getDenominator()));
            assertTrue(d, le(r.getDenominator(), y));
            assertTrue(d, le(r.getNumerator(), z));
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            Real a = ofExact(d).get();
            aeqd(d, d, a.doubleValueExact());
        }
    }

    private void propertiesOf_BigDecimal() {
        initialize("of(BigDecimal)");
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            Real x = of(bd);
            x.validate();
            assertTrue(bd, eq(bd, x.rationalValueExact().get().bigDecimalValueExact()));
            assertTrue(bd, x.rationalValueExact().get().hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (BigDecimal bd : take(LIMIT, P.canonicalBigDecimals())) {
            inverse(Real::of, Real::bigDecimalValueExact, bd);
        }
    }

    private void propertiesFuzzyRepresentation() {
        initialize("fuzzyRepresentation(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Real x = fuzzyRepresentation(r);
            x.validate();
            assertFalse(r, x.isExact());
        }
    }

    private void propertiesLeftFuzzyRepresentation() {
        initialize("leftFuzzyRepresentation(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Real x = leftFuzzyRepresentation(r);
            x.validate();
            assertFalse(r, x.isExact());
        }
    }

    private void propertiesRightFuzzyRepresentation() {
        initialize("rightFuzzyRepresentation(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Real x = rightFuzzyRepresentation(r);
            x.validate();
            assertFalse(r, x.isExact());
        }
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (Real x : take(LIMIT, P.reals())) {
            List<Interval> intervals = toList(take(TINY_LIMIT, x));
            for (int i = 1; i < TINY_LIMIT; i++) {
                assertTrue(x, intervals.get(i - 1).contains(intervals.get(i)));
            }
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            List<Interval> intervals = toList(take(TINY_LIMIT, of(r)));
            assertEquals(r, intervals, toList(replicate(TINY_LIMIT, Interval.of(r))));
        }
    }

    private void propertiesMatch() {
        initialize("match(List<Real>)");
        CachedIterator<Real> reals = new CachedIterator<>(P.cleanReals());
        Iterable<Pair<Real, List<Real>>> ps = map(
                p -> new Pair<>(reals.get(p.b).get(), toList(map(i -> reals.get(i).get(), p.a))),
                P.dependentPairs(P.distinctListsAtLeast(1, P.naturalIntegersGeometric()), P::uniformSample)
        );
        for (Pair<Real, List<Real>> p : take(LIMIT, ps)) {
            int index = p.a.match(p.b);
            assertTrue(p, p.b.get(index) == p.a);
        }

        for (Real r : take(LIMIT, P.reals())) {
            try {
                r.match(Collections.emptyList());
                fail(r);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesIsExactInteger() {
        initialize("isExactInteger()");
        for (Real x : take(LIMIT, P.reals())) {
            homomorphic(Real::negate, Function.identity(), Real::isExactInteger, Real::isExactInteger, x);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertTrue(i, of(i).isExactInteger());
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

    private void propertiesBigIntegerValueUnsafe_RoundingMode() {
        initialize("bigIntegerValueUnsafe(RoundingMode)");
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
            p.a.bigIntegerValueUnsafe(p.b);
        }

        ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isExactInteger(),
                P.pairs(P.cleanReals(), P.roundingModes())
        );
        for (Pair<Real, RoundingMode> p : take(LIMIT, ps)) {
            BigInteger rounded = p.a.bigIntegerValueUnsafe(p.b);
            assertTrue(p, rounded.equals(BigInteger.ZERO) || rounded.signum() == p.a.signumUnsafe());
            assertTrue(p, p.a.subtract(of(rounded)).abs().ltUnsafe(Rational.ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).bigIntegerValueUnsafe(RoundingMode.UNNECESSARY), i);
        }

        for (Real x : take(LIMIT, P.cleanReals())) {
            assertEquals(x, x.bigIntegerValueUnsafe(RoundingMode.FLOOR), x.floorUnsafe());
            assertEquals(x, x.bigIntegerValueUnsafe(RoundingMode.CEILING), x.ceilingUnsafe());
            assertTrue(x, of(x.bigIntegerValueUnsafe(RoundingMode.DOWN)).abs().leUnsafe(x.abs()));
            assertTrue(x, of(x.bigIntegerValueUnsafe(RoundingMode.UP)).abs().geUnsafe(x.abs()));
            assertTrue(
                    x,
                    x.subtract(of(x.bigIntegerValueUnsafe(RoundingMode.HALF_DOWN))).abs().leUnsafe(Rational.ONE_HALF)
            );
            assertTrue(
                    x,
                    x.subtract(of(x.bigIntegerValueUnsafe(RoundingMode.HALF_UP))).abs().leUnsafe(Rational.ONE_HALF)
            );
            assertTrue(
                    x,
                    x.subtract(of(x.bigIntegerValueUnsafe(RoundingMode.HALF_EVEN))).abs().leUnsafe(Rational.ONE_HALF)
            );
        }

        Iterable<Real> xs = filterInfinite(
                s -> s.abs().fractionalPartUnsafe().ltUnsafe(Rational.ONE_HALF),
                P.cleanReals()
        );
        for (Real x : take(LIMIT, xs)) {
            assertEquals(
                    x,
                    x.bigIntegerValueUnsafe(RoundingMode.HALF_DOWN),
                    x.bigIntegerValueUnsafe(RoundingMode.DOWN)
            );
            assertEquals(x, x.bigIntegerValueUnsafe(RoundingMode.HALF_UP), x.bigIntegerValueUnsafe(RoundingMode.DOWN));
            assertEquals(
                    x,
                    x.bigIntegerValueUnsafe(RoundingMode.HALF_EVEN),
                    x.bigIntegerValueUnsafe(RoundingMode.DOWN)
            );
        }

        xs = filterInfinite(s -> s.abs().fractionalPartUnsafe().gtUnsafe(Rational.ONE_HALF), P.cleanReals());
        for (Real x : take(LIMIT, xs)) {
            assertEquals(x, x.bigIntegerValueUnsafe(RoundingMode.HALF_DOWN), x.bigIntegerValueUnsafe(RoundingMode.UP));
            assertEquals(x, x.bigIntegerValueUnsafe(RoundingMode.HALF_UP), x.bigIntegerValueUnsafe(RoundingMode.UP));
            assertEquals(x, x.bigIntegerValueUnsafe(RoundingMode.HALF_EVEN), x.bigIntegerValueUnsafe(RoundingMode.UP));
        }

        //odd multiples of 1/2
        xs = map(i -> of(Rational.of(i.shiftLeft(1).add(BigInteger.ONE), IntegerUtils.TWO)), P.bigIntegers());
        for (Real x : take(LIMIT, xs)) {
            assertEquals(
                    x,
                    x.bigIntegerValueUnsafe(RoundingMode.HALF_DOWN),
                    x.bigIntegerValueUnsafe(RoundingMode.DOWN)
            );
            assertEquals(x, x.bigIntegerValueUnsafe(RoundingMode.HALF_UP), x.bigIntegerValueUnsafe(RoundingMode.UP));
            assertFalse(x, x.bigIntegerValueUnsafe(RoundingMode.HALF_EVEN).testBit(0));
        }

        for (Real x : take(LIMIT, filterInfinite(s -> !s.isExactInteger(), P.cleanReals()))) {
            try {
                x.bigIntegerValueUnsafe(RoundingMode.UNNECESSARY);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesBigIntegerValue_RoundingMode_Rational() {
        initialize("bigIntegerValue(RoundingMode, Rational)");
        Iterable<Triple<Real, RoundingMode, Rational>> ts = filterInfinite(
                s -> s.b != RoundingMode.UNNECESSARY || s.a.isExactInteger(),
                P.triples(P.withScale(4).reals(), P.roundingModes(), P.withScale(4).positiveRationals())
        );
        for (Triple<Real, RoundingMode, Rational> t : take(LIMIT, ts)) {
            Optional<BigInteger> oi = t.a.bigIntegerValue(t.b, t.c);
            oi.ifPresent(bigInteger -> assertEquals(t, t.a.bigIntegerValueUnsafe(t.b), bigInteger));
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                filterInfinite(x -> !x.isExactInteger(), P.withScale(4).reals()),
                P.withScale(4).positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.bigIntegerValue(RoundingMode.UNNECESSARY, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Triple<Real, RoundingMode, Rational>> tsFail = filterInfinite(
                s -> s.b != RoundingMode.UNNECESSARY || s.a.isExactInteger(),
                P.triples(P.withScale(4).reals(), P.roundingModes(), P.withScale(4).rangeDown(Rational.ZERO))
        );
        for (Triple<Real, RoundingMode, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.bigIntegerValue(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesBigIntegerValueUnsafe() {
        initialize("bigIntegerValueUnsafe()");
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
            x.bigIntegerValueUnsafe();
        }

        for (Real x : take(LIMIT, P.cleanReals())) {
            BigInteger rounded = x.bigIntegerValueUnsafe();
            assertTrue(x, rounded.equals(BigInteger.ZERO) || rounded.signum() == x.signumUnsafe());
            assertTrue(x, x.subtract(of(rounded)).abs().ltUnsafe(Rational.ONE));
            assertTrue(x, x.subtract(of(rounded)).abs().leUnsafe(Rational.ONE_HALF));
        }

        xs = filterInfinite(s -> s.abs().fractionalPartUnsafe().ltUnsafe(Rational.ONE_HALF), P.cleanReals());
        for (Real x : take(LIMIT, xs)) {
            assertEquals(
                    x,
                    x.bigIntegerValueUnsafe(),
                    x.bigIntegerValueUnsafe(RoundingMode.DOWN)
            );
        }

        xs = filterInfinite(s -> s.abs().fractionalPartUnsafe().gtUnsafe(Rational.ONE_HALF), P.cleanReals());
        for (Real x : take(LIMIT, xs)) {
            assertEquals(x, x.bigIntegerValueUnsafe(), x.bigIntegerValueUnsafe(RoundingMode.UP));
        }

        //odd multiples of 1/2
        xs = map(i -> of(Rational.of(i.shiftLeft(1).add(BigInteger.ONE), IntegerUtils.TWO)), P.bigIntegers());
        for (Real x : take(LIMIT, xs)) {
            assertFalse(x, x.bigIntegerValueUnsafe().testBit(0));
        }
    }

    private void propertiesBigIntegerValue_Rational() {
        initialize("bigIntegerValue(RoundingMode)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(P.withScale(4).reals(), P.withScale(4).positiveRationals());
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            Optional<BigInteger> oi = p.a.bigIntegerValue(p.b);
            oi.ifPresent(bigInteger -> assertEquals(p, p.a.bigIntegerValueUnsafe(), bigInteger));
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withScale(4).reals(),
                P.withScale(4).rangeDown(Rational.ZERO)
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.bigIntegerValue(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesFloorUnsafe() {
        initialize("floorUnsafe()");
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
            x.floorUnsafe();
        }

        for (Real x : take(LIMIT, P.cleanReals())) {
            BigInteger rounded = x.floorUnsafe();
            assertTrue(x, rounded.equals(BigInteger.ZERO) || rounded.signum() == x.signumUnsafe());
            assertTrue(x, x.subtract(of(rounded)).abs().ltUnsafe(Rational.ONE));
        }
    }

    private void propertiesFloor() {
        initialize("floor(Rational)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(P.withScale(4).reals(), P.withScale(4).positiveRationals());
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            Optional<BigInteger> oi = p.a.floor(p.b);
            oi.ifPresent(bigInteger -> assertEquals(p, p.a.floorUnsafe(), bigInteger));
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withScale(4).reals(),
                P.withScale(4).rangeDown(Rational.ZERO)
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.floor(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesCeilingUnsafe() {
        initialize("ceilingUnsafe()");
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
            x.ceilingUnsafe();
        }

        for (Real x : take(LIMIT, P.cleanReals())) {
            BigInteger rounded = x.ceilingUnsafe();
            assertTrue(x, rounded.equals(BigInteger.ZERO) || rounded.signum() == x.signumUnsafe());
            assertTrue(x, x.subtract(of(rounded)).abs().ltUnsafe(Rational.ONE));
        }
    }

    private void propertiesCeiling() {
        initialize("ceiling(Rational)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(P.withScale(4).reals(), P.withScale(4).positiveRationals());
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            Optional<BigInteger> oi = p.a.ceiling(p.b);
            oi.ifPresent(bigInteger -> assertEquals(p, p.a.ceilingUnsafe(), bigInteger));
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withScale(4).reals(),
                P.withScale(4).rangeDown(Rational.ZERO)
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.ceiling(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesBigIntegerValueExact() {
        initialize("bigIntegerValueExact()");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Real x = of(i);
            assertEquals(i, x.bigIntegerValueExact(), i);
            homomorphic(Real::negate, BigInteger::negate, Real::bigIntegerValueExact, Real::bigIntegerValueExact, x);
        }

        for (Real x : take(LIMIT, filterInfinite(s -> !s.isExactInteger(), P.reals()))) {
            try {
                x.bigIntegerValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesByteValueExact() {
        initialize("byteValueExact()");
        for (byte b : take(LIMIT, P.bytes())) {
            Real x = of(b);
            assertEquals(b, x.byteValueExact(), b);
        }

        for (byte b : take(LIMIT, filter(c -> c != Byte.MIN_VALUE, P.bytes()))) {
            Real x = of(b);
            homomorphic(Real::negate, c -> (byte) -c, Real::byteValueExact, Real::byteValueExact, x);
        }

        for (Real x : take(LIMIT, filterInfinite(s -> !s.isExactInteger(), P.reals()))) {
            try {
                x.byteValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, P.rangeUp(BigInteger.valueOf(Byte.MAX_VALUE).add(BigInteger.ONE)))) {
            try {
                of(i).byteValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, P.rangeDown(BigInteger.valueOf(Byte.MIN_VALUE).subtract(BigInteger.ONE)))) {
            try {
                of(i).byteValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesShortValueExact() {
        initialize("shortValueExact()");
        for (short s : take(LIMIT, P.shorts())) {
            Real x = of(s);
            assertEquals(s, x.shortValueExact(), s);
        }

        for (short s : take(LIMIT, filter(t -> t != Short.MIN_VALUE, P.shorts()))) {
            Real x = of(s);
            homomorphic(Real::negate, t -> (short) -t, Real::shortValueExact, Real::shortValueExact, x);
        }

        for (Real x : take(LIMIT, filterInfinite(s -> !s.isExactInteger(), P.reals()))) {
            try {
                x.shortValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, P.rangeUp(BigInteger.valueOf(Short.MAX_VALUE).add(BigInteger.ONE)))) {
            try {
                of(i).shortValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, P.rangeDown(BigInteger.valueOf(Short.MIN_VALUE).subtract(BigInteger.ONE)))) {
            try {
                of(i).shortValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesIntValueExact() {
        initialize("intValueExact()");
        for (int i : take(LIMIT, P.integers())) {
            Real x = of(i);
            assertEquals(i, x.intValueExact(), i);
        }

        for (int i : take(LIMIT, filter(j -> j != Integer.MIN_VALUE, P.integers()))) {
            Real x = of(i);
            homomorphic(Real::negate, j -> -j, Real::intValueExact, Real::intValueExact, x);
        }

        for (Real x : take(LIMIT, filterInfinite(s -> !s.isExactInteger(), P.reals()))) {
            try {
                x.intValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> isFail = P.withScale(33)
                .rangeUp(BigInteger.valueOf(Integer.MAX_VALUE).add(BigInteger.ONE));
        for (BigInteger i : take(LIMIT, isFail)) {
            try {
                of(i).intValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> isFail2 = P.withScale(33)
                .rangeDown(BigInteger.valueOf(Integer.MIN_VALUE).subtract(BigInteger.ONE));
        for (BigInteger i : take(LIMIT, isFail2)) {
            try {
                of(i).intValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesLongValueExact() {
        initialize("longValueExact()");
        for (long l : take(LIMIT, P.longs())) {
            Real x = of(l);
            assertEquals(l, x.longValueExact(), l);
        }

        for (long l : take(LIMIT, filter(m -> m != Long.MIN_VALUE, P.longs()))) {
            Real x = of(l);
            homomorphic(Real::negate, m -> -m, Real::longValueExact, Real::longValueExact, x);
        }

        for (Real x : take(LIMIT, filterInfinite(s -> !s.isExactInteger(), P.reals()))) {
            try {
                x.longValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> isFail = P.withScale(65).rangeUp(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE));
        for (BigInteger i : take(LIMIT, isFail)) {
            try {
                of(i).longValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> isFail2 = P.withScale(65)
                .rangeDown(BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE));
        for (BigInteger i : take(LIMIT, isFail2)) {
            try {
                of(i).longValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesIsExactIntegerPowerOfTwo() {
        initialize("isExactIntegerPowerOfTwo()");
        for (Real x : take(LIMIT, P.reals())) {
            x.isExactIntegerPowerOfTwo();
        }

        for (int i : take(MEDIUM_LIMIT, P.withScale(4).integersGeometric())) {
            assertTrue(i, of(Rational.ONE.shiftLeft(i)).isExactIntegerPowerOfTwo());
        }
    }

    //x must be positive
    private static boolean pow2Check(@NotNull Algebraic x, @NotNull FuzzinessType ft) {
        boolean right = ft == FuzzinessType.RIGHT || ft == FuzzinessType.BOTH;
        return !x.isIntegerPowerOfTwo() || !right;
    }

    private void propertiesRoundUpToIntegerPowerOfTwoUnsafe() {
        initialize("roundUpToIntegerPowerOfTwoUnsafe()");
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
            x.roundUpToIntegerPowerOfTwoUnsafe();
        }

        for (Real x : take(LIMIT, P.positiveCleanReals())) {
            BinaryFraction powerOfTwo = x.roundUpToIntegerPowerOfTwoUnsafe();
            assertTrue(x, powerOfTwo.isPowerOfTwo());
            assertTrue(x, x.leUnsafe(Rational.of(powerOfTwo)));
            assertTrue(x, of(powerOfTwo.shiftRight(1)).ltUnsafe(x));
        }

        for (Real x : take(LIMIT, P.cleanRealRangeDown(Algebraic.ZERO))) {
            try {
                x.roundUpToIntegerPowerOfTwoUnsafe();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesRoundUpToIntegerPowerOfTwo() {
        initialize("roundUpToIntegerPowerOfTwo(Rational)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                P.withScale(4).positiveReals(),
                P.withScale(4).positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            Optional<BinaryFraction> obf = p.a.roundUpToIntegerPowerOfTwo(p.b);
            obf.ifPresent(binaryFraction -> assertEquals(p, p.a.roundUpToIntegerPowerOfTwoUnsafe(), binaryFraction));
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withScale(4).positiveReals(),
                P.withScale(4).rangeDown(Rational.ZERO)
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.roundUpToIntegerPowerOfTwo(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesIsExactBinaryFraction() {
        initialize("isExactBinaryFraction()");
        for (Real x : take(LIMIT, P.reals())) {
            x.isExactBinaryFraction();
            homomorphic(
                    Real::negate,
                    Function.identity(),
                    Real::isExactBinaryFraction,
                    Real::isExactBinaryFraction,
                    x
            );
        }
    }

    private void propertiesBinaryFractionValueExact() {
        initialize("binaryFractionValueExact()");
        for (Real x : take(LIMIT, map(Real::of, P.binaryFractions()))) {
            homomorphic(
                    Real::negate,
                    BinaryFraction::negate,
                    Real::binaryFractionValueExact,
                    Real::binaryFractionValueExact,
                    x
            );
        }

        for (Real x : take(LIMIT, filterInfinite(s -> !s.isExactBinaryFraction(), P.reals()))) {
            try {
                x.binaryFractionValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesIsExact() {
        initialize("isExact()");
        for (Real x : take(LIMIT, P.reals())) {
            x.isExact();
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(r, of(r).isExact());
        }
    }

    private void propertiesRationalValueExact() {
        initialize("rationalValueExact()");
        for (Real x : take(LIMIT, P.reals())) {
            x.rationalValueExact();
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            Real x = of(r);
            assertTrue(r, x.rationalValueExact().isPresent());
        }
    }

    //x must be positive
    private static boolean binaryExponentCheck(@NotNull Algebraic x, @NotNull FuzzinessType ft) {
        boolean left = ft == FuzzinessType.LEFT || ft == FuzzinessType.BOTH;
        return !x.isIntegerPowerOfTwo() || !left;
    }

    private void propertiesBinaryExponentUnsafe() {
        initialize("binaryExponentUnsafe()");
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
            x.binaryExponentUnsafe();
        }

        for (Real x : take(LIMIT, P.positiveCleanReals())) {
            int exponent = x.binaryExponentUnsafe();
            Rational power = Rational.ONE.shiftLeft(exponent);
            assertTrue(x, x.geUnsafe(power));
            assertTrue(x, x.leUnsafe(power.shiftLeft(1)));
        }

        for (Real x : take(LIMIT, P.cleanRealRangeDown(Algebraic.ZERO))) {
            try {
                x.binaryExponentUnsafe();
                fail(x);
            } catch (ArithmeticException | IllegalArgumentException ignored) {}
        }
    }

    private void propertiesBinaryExponent() {
        initialize("binaryExponent(Rational)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                P.withScale(4).positiveReals(),
                P.withScale(4).positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            Optional<Integer> oi = p.a.binaryExponent(p.b);
            oi.ifPresent(integer -> assertEquals(p, p.a.binaryExponentUnsafe(), integer));
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withScale(4).positiveReals(),
                P.withScale(4).rangeDown(Rational.ZERO)
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.binaryExponent(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesIsExactAndEqualToFloat() {
        initialize("isExactAndEqualToFloat()");
        for (Real x : take(LIMIT, P.reals())) {
            boolean ietf = x.isExactAndEqualToFloat();
            assertTrue(x, !ietf || x.isExactBinaryFraction());
            homomorphic(
                    Real::negate,
                    Function.identity(),
                    Real::isExactAndEqualToFloat,
                    Real::isExactAndEqualToFloat,
                    x
            );
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            assertTrue(f, ofExact(f).get().isExactAndEqualToFloat());
        }
    }

    private void propertiesIsExactAndEqualToDouble() {
        initialize("isExactAndEqualToDouble()");
        for (Real x : take(LIMIT, P.reals())) {
            boolean ietf = x.isExactAndEqualToDouble();
            assertTrue(x, !ietf || x.isExactBinaryFraction());
            homomorphic(
                    Real::negate,
                    Function.identity(),
                    Real::isExactAndEqualToDouble,
                    Real::isExactAndEqualToDouble,
                    x
            );
        }

        for (double d : take(LIMIT, filter(g -> Double.isFinite(g) && !isNegativeZero(g), P.doubles()))) {
            assertTrue(d, ofExact(d).get().isExactAndEqualToDouble());
        }
    }

    private static boolean floatEquidistant(@NotNull Algebraic x) {
        if (!x.isRational()) return false;
        Rational r = x.rationalValueExact();
        float below = r.floatValue(RoundingMode.FLOOR);
        float above = r.floatValue(RoundingMode.CEILING);
        if (below == above || Float.isInfinite(below) || Float.isInfinite(above)) return false;
        Rational belowDistance = r.subtract(Rational.ofExact(below).get());
        Rational aboveDistance = Rational.ofExact(above).get().subtract(r);
        return belowDistance.equals(aboveDistance);
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

    private void propertiesFloatValueUnsafe_RoundingMode() {
        initialize("floatValueUnsafe(RoundingMode)");
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
            p.a.floatValueUnsafe(p.b);
        }

        ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isExactAndEqualToFloat(),
                P.pairs(P.withScale(1).cleanReals(), P.roundingModes())
        );
        for (Pair<Real, RoundingMode> p : take(SMALL_LIMIT, ps)) {
            float rounded = p.a.floatValueUnsafe(p.b);
            assertTrue(p, !Float.isNaN(rounded));
            assertTrue(p, rounded == 0.0f || Math.signum(rounded) == p.a.signumUnsafe());
        }

        Iterable<Real> xs = map(
                f -> ofExact(f).get(),
                filter(f -> Float.isFinite(f) && !isNegativeZero(f), P.floats())
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValueUnsafe(RoundingMode.UNNECESSARY);
            assertEquals(x, x.rationalValueExact(), ofExact(rounded).get().rationalValueExact());
            assertTrue(x, Float.isFinite(rounded));
            assertTrue(x, !isNegativeZero(rounded));
        }

        Algebraic largestFloat = Algebraic.of(Rational.LARGEST_FLOAT);
        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.equals(largestFloat),
                        P.withScale(2)
                                .algebraicsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValueUnsafe(RoundingMode.FLOOR);
            float successor = successor(rounded);
            assertTrue(x, x.geUnsafe(Rational.ofExact(rounded).get()));
            assertTrue(x, x.ltUnsafe(Rational.ofExact(successor).get()));
            assertTrue(x, rounded < 0 || Float.isFinite(rounded));
            assertTrue(x, !isNegativeZero(rounded));
        }

        xs = map(
                Algebraic::realValue, filterInfinite(
                        x -> !x.equals(Algebraic.of(Rational.LARGEST_FLOAT.negate())),
                        P.withScale(2)
                                .algebraicsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValueUnsafe(RoundingMode.CEILING);
            float predecessor = predecessor(rounded);
            assertTrue(x, x.leUnsafe(Rational.ofExact(rounded).get()));
            assertTrue(x, x.gtUnsafe(Rational.ofExact(predecessor).get()));
            assertTrue(x, rounded > 0 || Float.isFinite(rounded));
        }

        xs = P.withScale(2).cleanRealsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT));
        for (Real x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValueUnsafe(RoundingMode.DOWN);
            assertTrue(x, x.abs().geUnsafe(Rational.ofExact(rounded).get().abs()));
            assertTrue(x, Float.isFinite(rounded));
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        r -> r != Algebraic.ZERO,
                        P.withScale(2)
                                .algebraicsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValueUnsafe(RoundingMode.DOWN);
            float successor = successor(rounded);
            float predecessor = predecessor(rounded);
            float down = x.signumUnsafe() == -1 ? successor : predecessor;
            assertTrue(x, x.abs().gtUnsafe(Rational.ofExact(down).get().abs()));
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.abs().equals(Algebraic.of(Rational.LARGEST_FLOAT)),
                        P.withScale(2)
                                .algebraicsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            assertTrue(x, !isNegativeZero(x.floatValueUnsafe(RoundingMode.UP)));
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        r -> !r.equals(Algebraic.of(Rational.SMALLEST_FLOAT)),
                        P.withScale(2).algebraicsIn(Interval.of(Rational.ZERO, Rational.SMALLEST_FLOAT))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValueUnsafe(RoundingMode.FLOOR), 0.0f);
            aeqf(x, x.floatValueUnsafe(RoundingMode.DOWN), 0.0f);
            float rounded = x.floatValueUnsafe(RoundingMode.UP);
            float successor = successor(rounded);
            float predecessor = predecessor(rounded);
            float up = x.signumUnsafe() == -1 ? predecessor : successor;
            assertTrue(x, x.abs().ltUnsafe(Rational.ofExact(up).get().abs()));
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.equals(Algebraic.of(Rational.LARGEST_FLOAT.negate())),
                        P.withScale(2).algebraicsIn(Interval.lessThanOrEqualTo(Rational.LARGEST_FLOAT.negate()))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            float floor = x.floatValueUnsafe(RoundingMode.FLOOR);
            aeqf(x, floor, Float.NEGATIVE_INFINITY);

            float up = x.floatValueUnsafe(RoundingMode.UP);
            aeqf(x, up, Float.NEGATIVE_INFINITY);

            float halfUp = x.floatValueUnsafe(RoundingMode.HALF_UP);
            aeqf(x, halfUp, Float.NEGATIVE_INFINITY);

            float halfEven = x.floatValueUnsafe(RoundingMode.HALF_EVEN);
            aeqf(x, halfEven, Float.NEGATIVE_INFINITY);
        }

        xs = P.withScale(2).cleanRealsIn(Interval.greaterThanOrEqualTo(Rational.LARGEST_FLOAT));
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValueUnsafe(RoundingMode.FLOOR), Float.MAX_VALUE);
            aeqf(x, x.floatValueUnsafe(RoundingMode.DOWN), Float.MAX_VALUE);
            aeqf(x, x.floatValueUnsafe(RoundingMode.HALF_DOWN), Float.MAX_VALUE);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> x != Algebraic.ZERO && !x.equals(Algebraic.of(Rational.SMALLEST_FLOAT.negate())),
                        P.withScale(2).algebraicsIn(Interval.of(Rational.SMALLEST_FLOAT.negate(), Rational.ZERO))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValueUnsafe(RoundingMode.CEILING), -0.0f);
            aeqf(x, x.floatValueUnsafe(RoundingMode.DOWN), -0.0f);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.equals(Algebraic.of(Rational.LARGEST_FLOAT)),
                        P.withScale(2).algebraicsIn(Interval.greaterThanOrEqualTo(Rational.LARGEST_FLOAT))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            float ceiling = x.floatValueUnsafe(RoundingMode.CEILING);
            aeqf(x, ceiling, Float.POSITIVE_INFINITY);

            float up = x.floatValueUnsafe(RoundingMode.UP);
            aeqf(x, up, Float.POSITIVE_INFINITY);

            float halfUp = x.floatValueUnsafe(RoundingMode.HALF_UP);
            aeqf(x, halfUp, Float.POSITIVE_INFINITY);

            float halfEven = x.floatValueUnsafe(RoundingMode.HALF_EVEN);
            aeqf(x, halfEven, Float.POSITIVE_INFINITY);
        }

        xs = P.withScale(2).cleanRealsIn(Interval.lessThanOrEqualTo(Rational.LARGEST_FLOAT.negate()));
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValueUnsafe(RoundingMode.CEILING), -Float.MAX_VALUE);
            aeqf(x, x.floatValueUnsafe(RoundingMode.DOWN), -Float.MAX_VALUE);
            aeqf(x, x.floatValueUnsafe(RoundingMode.HALF_DOWN), -Float.MAX_VALUE);
        }

        Iterable<Real> midpoints = map(
                f -> {
                    Rational lo = Rational.ofExact(f).get();
                    Rational hi = Rational.ofExact(successor(f)).get();
                    return of(lo.add(hi).shiftRight(1));
                },
                filter(f -> Float.isFinite(f) && !isNegativeZero(f) && f != Float.MAX_VALUE, P.floats())
        );
        for (Real x : take(SMALL_LIMIT, midpoints)) {
            float down = x.floatValueUnsafe(RoundingMode.DOWN);
            float up = x.floatValueUnsafe(RoundingMode.UP);
            aeqf(x, x.floatValueUnsafe(RoundingMode.HALF_DOWN), down);
            aeqf(x, x.floatValueUnsafe(RoundingMode.HALF_UP), up);
            float halfEven = x.floatValueUnsafe(RoundingMode.HALF_EVEN);
            assertTrue(x, ((Float.floatToIntBits(down) & 1) == 0 ? down : up) == halfEven);
        }

        Iterable<Real> notMidpoints = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !floatEquidistant(x),
                        P.withScale(2)
                                .algebraicsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT))
                )
        );
        for (Real x : take(SMALL_LIMIT, notMidpoints)) {
            float below = x.floatValueUnsafe(RoundingMode.FLOOR);
            float above = x.floatValueUnsafe(RoundingMode.CEILING);
            Real belowDistance = x.subtract(ofExact(below).get());
            Real aboveDistance = ofExact(above).get().subtract(x);
            float closest = belowDistance.ltUnsafe(aboveDistance) ? below : above;
            aeqf(x, x.floatValueUnsafe(RoundingMode.HALF_DOWN), closest);
            aeqf(x, x.floatValueUnsafe(RoundingMode.HALF_UP), closest);
            aeqf(x, x.floatValueUnsafe(RoundingMode.HALF_EVEN), closest);
        }

        xs = P.withScale(2).cleanRealsIn(Interval.of(Rational.ZERO, Rational.SMALLEST_FLOAT.shiftRight(1)));
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValueUnsafe(RoundingMode.HALF_DOWN), 0.0f);
            aeqf(x, x.floatValueUnsafe(RoundingMode.HALF_EVEN), 0.0f);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        r -> r != Algebraic.ZERO,
                        P.withScale(2).algebraicsIn(
                                Interval.of(Rational.SMALLEST_FLOAT.shiftRight(1).negate(), Rational.ZERO)
                        )
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValueUnsafe(RoundingMode.HALF_DOWN), -0.0f);
            aeqf(x, x.floatValueUnsafe(RoundingMode.HALF_EVEN), -0.0f);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.equals(Algebraic.of(Rational.SMALLEST_FLOAT.shiftRight(1))),
                        P.withScale(2).algebraicsIn(Interval.of(Rational.ZERO, Rational.SMALLEST_FLOAT.shiftRight(1)))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValueUnsafe(RoundingMode.HALF_UP), 0.0f);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> x != Algebraic.ZERO &&
                                !x.equals(Algebraic.of(Rational.SMALLEST_FLOAT.shiftRight(1).negate())),
                        P.withScale(2).algebraicsIn(
                                Interval.of(Rational.SMALLEST_FLOAT.shiftRight(1).negate(), Rational.ZERO)
                        )
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValueUnsafe(RoundingMode.HALF_UP), -0.0f);
        }

        for (Real x : take(SMALL_LIMIT, P.withScale(2).cleanReals())) {
            float floor = x.floatValueUnsafe(RoundingMode.FLOOR);
            assertFalse(x, isNegativeZero(floor));
            assertFalse(x, floor == Float.POSITIVE_INFINITY);
            float ceiling = x.floatValueUnsafe(RoundingMode.CEILING);
            assertFalse(x, ceiling == Float.NEGATIVE_INFINITY);
            float down = x.floatValueUnsafe(RoundingMode.DOWN);
            assertTrue(x, Float.isFinite(down));
            float up = x.floatValueUnsafe(RoundingMode.UP);
            assertFalse(x, isNegativeZero(up));
            float halfDown = x.floatValueUnsafe(RoundingMode.HALF_DOWN);
            assertTrue(x, Float.isFinite(halfDown));
        }

        Iterable<Real> xsFail = map(
                Algebraic::realValue,
                filterInfinite(y -> !y.isEqualToFloat(), P.withScale(2).algebraics())
        );
        for (Real x : take(LIMIT, xsFail)) {
            try {
                x.floatValueUnsafe(RoundingMode.UNNECESSARY);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesFloatValue_RoundingMode_Rational() {
        initialize("floatValue(RoundingMode, Rational)");
        Iterable<Triple<Real, RoundingMode, Rational>> ts = filterInfinite(
                s -> s.b != RoundingMode.UNNECESSARY || s.a.isExactAndEqualToFloat(),
                P.triples(P.withScale(4).reals(), P.roundingModes(), P.positiveRationals())
        );
        for (Triple<Real, RoundingMode, Rational> t : take(LIMIT, ts)) {
            Optional<Float> of = t.a.floatValue(t.b, t.c);
            of.ifPresent(aFloat -> assertEquals(t, t.a.floatValueUnsafe(t.b), aFloat));
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                filterInfinite(x -> !x.isExactAndEqualToFloat(), P.withScale(4).reals()),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.floatValue(RoundingMode.UNNECESSARY, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Triple<Real, RoundingMode, Rational>> tsFail = filterInfinite(
                s -> s.b != RoundingMode.UNNECESSARY || s.a.isExactAndEqualToFloat(),
                P.triples(P.withScale(4).reals(), P.roundingModes(), P.rangeDown(Rational.ZERO))
        );
        for (Triple<Real, RoundingMode, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.floatValue(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesFloatValueUnsafe() {
        initialize("floatValueUnsafe()");
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
            x.floatValueUnsafe();
        }

        for (Real x : take(SMALL_LIMIT, P.withScale(2).cleanReals())) {
            float rounded = x.floatValueUnsafe();
            aeqf(x, rounded, x.floatValueUnsafe(RoundingMode.HALF_EVEN));
            assertTrue(x, !Float.isNaN(rounded));
            assertTrue(x, rounded == 0.0f || Math.signum(rounded) == x.signumUnsafe());
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.equals(Algebraic.of(Rational.LARGEST_FLOAT.negate())),
                        P.withScale(2).algebraicsIn(Interval.lessThanOrEqualTo(Rational.LARGEST_FLOAT.negate()))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValueUnsafe();
            aeqf(x, rounded, Float.NEGATIVE_INFINITY);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.equals(Algebraic.of(Rational.LARGEST_FLOAT)),
                        P.withScale(2).algebraicsIn(Interval.greaterThanOrEqualTo(Rational.LARGEST_FLOAT))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValueUnsafe();
            aeqf(x, rounded, Float.POSITIVE_INFINITY);
        }

        Iterable<Real> midpoints = map(
                f -> {
                    Rational lo = Rational.ofExact(f).get();
                    Rational hi = Rational.ofExact(successor(f)).get();
                    return of(lo.add(hi).shiftRight(1));
                },
                filter(f -> Float.isFinite(f) && f != Float.MAX_VALUE, P.floats())
        );
        for (Real x : take(SMALL_LIMIT, midpoints)) {
            float down = x.floatValueUnsafe(RoundingMode.DOWN);
            float up = x.floatValueUnsafe(RoundingMode.UP);
            float rounded = x.floatValueUnsafe();
            assertTrue(x, ((Float.floatToIntBits(down) & 1) == 0 ? down : up) == rounded);
        }

        Iterable<Real> notMidpoints = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !floatEquidistant(x),
                        P.withScale(2)
                                .algebraicsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT))
                )
        );
        for (Real x : take(SMALL_LIMIT, notMidpoints)) {
            float below = x.floatValueUnsafe(RoundingMode.FLOOR);
            float above = x.floatValueUnsafe(RoundingMode.CEILING);
            Real belowDistance = x.subtract(ofExact(below).get());
            Real aboveDistance = ofExact(above).get().subtract(x);
            float closest = belowDistance.ltUnsafe(aboveDistance) ? below : above;
            aeqf(x, x.floatValueUnsafe(), closest);
        }

        xs = P.withScale(2).cleanRealsIn(Interval.of(Rational.ZERO, Rational.SMALLEST_FLOAT.shiftRight(1)));
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValueUnsafe(), 0.0f);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> x != Algebraic.ZERO,
                        P.withScale(2).algebraicsIn(
                                Interval.of(Rational.SMALLEST_FLOAT.shiftRight(1).negate(), Rational.ZERO)
                        )
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValueUnsafe(), -0.0f);
        }
    }

    private void propertiesFloatValue_Rational() {
        initialize("floatValue(RoundingMode)");
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.positiveRationals()))) {
            Optional<Float> of = p.a.floatValue(p.b);
            of.ifPresent(aFloat -> assertEquals(p, p.a.floatValueUnsafe(), aFloat));
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.rangeDown(Rational.ZERO)))) {
            try {
                p.a.floatValue(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesFloatValueExact() {
        initialize("floatValueExact()");
        for (Real x : take(SMALL_LIMIT, filterInfinite(Real::isExactAndEqualToFloat, P.withScale(2).cleanReals()))) {
            float f = x.floatValueExact();
            assertTrue(x, !Float.isNaN(f));
            assertTrue(x, f == 0.0f || Math.signum(f) == x.signumUnsafe());
            homomorphic(Real::negate, g -> absNegativeZeros(-g), Real::floatValueExact, Real::floatValueExact, x);
        }

        Iterable<Real> xs = map(
                f -> ofExact(f).get(),
                filter(f -> Float.isFinite(f) && !isNegativeZero(f), P.floats())
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            float f = x.floatValueExact();
            assertEquals(x, x.rationalValueExact().get(), Rational.ofExact(f).get());
            assertTrue(x, Float.isFinite(f));
            assertTrue(x, !isNegativeZero(f));
        }

        for (Real x : take(LIMIT, filterInfinite(y -> !y.isExactAndEqualToFloat(), P.withScale(2).cleanReals()))) {
            try {
                x.floatValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static boolean doubleEquidistant(@NotNull Algebraic x) {
        if (!x.isRational()) return false;
        Rational r = x.rationalValueExact();
        double below = r.doubleValue(RoundingMode.FLOOR);
        double above = r.doubleValue(RoundingMode.CEILING);
        if (below == above || Double.isInfinite(below) || Double.isInfinite(above)) return false;
        Rational belowDistance = r.subtract(Rational.ofExact(below).get());
        Rational aboveDistance = Rational.ofExact(above).get().subtract(r);
        return belowDistance.equals(aboveDistance);
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
                        return (Double.doubleToLongBits(predecessor) & 1L) == 0L ? !right : !left;
                    case UNNECESSARY:
                        return false;
                    default:
                        return true;
                }
            }
        }
        return rm != RoundingMode.UNNECESSARY;
    }

    private void propertiesDoubleValueUnsafe_RoundingMode() {
        initialize("doubleValueUnsafe(RoundingMode)");
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
            p.a.doubleValueUnsafe(p.b);
        }

        ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isExactAndEqualToDouble(),
                P.pairs(P.withScale(1).cleanReals(), P.roundingModes())
        );
        for (Pair<Real, RoundingMode> p : take(SMALL_LIMIT, ps)) {
            double rounded = p.a.doubleValueUnsafe(p.b);
            assertTrue(p, !Double.isNaN(rounded));
            assertTrue(p, rounded == 0.0 || Math.signum(rounded) == p.a.signumUnsafe());
        }

        Iterable<Real> xs = map(
                d -> ofExact(d).get(),
                filter(d -> Double.isFinite(d) && !isNegativeZero(d), P.doubles())
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            double rounded = x.doubleValueUnsafe(RoundingMode.UNNECESSARY);
            assertEquals(x, x.rationalValueExact(), ofExact(rounded).get().rationalValueExact());
            assertTrue(x, Double.isFinite(rounded));
            assertTrue(x, !isNegativeZero(rounded));
        }

        Algebraic largestDouble = Algebraic.of(Rational.LARGEST_DOUBLE);
        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.equals(largestDouble),
                        P.withScale(2).withScale(4)
                                .algebraicsIn(Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE))
                )
        );
        for (Real x : take(TINY_LIMIT, xs)) {
            double rounded = x.doubleValueUnsafe(RoundingMode.FLOOR);
            double successor = successor(rounded);
            assertTrue(x, x.geUnsafe(Rational.ofExact(rounded).get()));
            assertTrue(x, x.ltUnsafe(Rational.ofExact(successor).get()));
            assertTrue(x, rounded < 0 || Double.isFinite(rounded));
            assertTrue(x, !isNegativeZero(rounded));
        }

        xs = map(
                Algebraic::realValue, filterInfinite(
                        x -> !x.equals(Algebraic.of(Rational.LARGEST_DOUBLE.negate())),
                        P.withScale(2).withScale(4)
                                .algebraicsIn(Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE))
                )
        );
        for (Real x : take(TINY_LIMIT, xs)) {
            double rounded = x.doubleValueUnsafe(RoundingMode.CEILING);
            double predecessor = predecessor(rounded);
            assertTrue(x, x.leUnsafe(Rational.ofExact(rounded).get()));
            assertTrue(x, x.gtUnsafe(Rational.ofExact(predecessor).get()));
            assertTrue(x, rounded > 0 || Double.isFinite(rounded));
        }

        xs = P.withScale(2).withSecondaryScale(4)
                .cleanRealsIn(Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE));
        for (Real x : take(TINY_LIMIT, xs)) {
            double rounded = x.doubleValueUnsafe(RoundingMode.DOWN);
            assertTrue(x, x.abs().geUnsafe(ofExact(rounded).get().abs()));
            assertTrue(x, Double.isFinite(rounded));
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        r -> r != Algebraic.ZERO,
                        P.withScale(2).withSecondaryScale(4)
                                .algebraicsIn(Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE))
                )
        );
        for (Real x : take(TINY_LIMIT, xs)) {
            double rounded = x.doubleValueUnsafe(RoundingMode.DOWN);
            double successor = successor(rounded);
            double predecessor = predecessor(rounded);
            double down = x.signumUnsafe() == -1 ? successor : predecessor;
            assertTrue(x, x.abs().gtUnsafe(ofExact(down).get().abs()));
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.abs().equals(Algebraic.of(Rational.LARGEST_DOUBLE)),
                        P.withScale(2).withSecondaryScale(4)
                                .algebraicsIn(Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE))
                )
        );
        for (Real x : take(TINY_LIMIT, xs)) {
            assertTrue(x, !isNegativeZero(x.doubleValueUnsafe(RoundingMode.UP)));
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        r -> !r.equals(Algebraic.of(Rational.SMALLEST_DOUBLE)),
                        P.withScale(2).algebraicsIn(Interval.of(Rational.ZERO, Rational.SMALLEST_DOUBLE))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqd(x, x.doubleValueUnsafe(RoundingMode.FLOOR), 0.0);
            aeqd(x, x.doubleValueUnsafe(RoundingMode.DOWN), 0.0);
            double rounded = x.doubleValueUnsafe(RoundingMode.UP);
            double successor = successor(rounded);
            double predecessor = predecessor(rounded);
            double up = x.signumUnsafe() == -1 ? predecessor : successor;
            assertTrue(x, x.abs().ltUnsafe(ofExact(up).get().abs()));
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.equals(Algebraic.of(Rational.LARGEST_DOUBLE.negate())),
                        P.withScale(2).withSecondaryScale(4)
                                .algebraicsIn(Interval.lessThanOrEqualTo(Rational.LARGEST_DOUBLE.negate()))
                )
        );
        for (Real x : take(TINY_LIMIT, xs)) {
            double floor = x.doubleValueUnsafe(RoundingMode.FLOOR);
            aeqd(x, floor, Double.NEGATIVE_INFINITY);

            double up = x.doubleValueUnsafe(RoundingMode.UP);
            aeqd(x, up, Double.NEGATIVE_INFINITY);

            double halfUp = x.doubleValueUnsafe(RoundingMode.HALF_UP);
            aeqd(x, halfUp, Double.NEGATIVE_INFINITY);

            double halfEven = x.doubleValueUnsafe(RoundingMode.HALF_EVEN);
            aeqd(x, halfEven, Double.NEGATIVE_INFINITY);
        }

        xs = P.withScale(2).withSecondaryScale(4).cleanRealsIn(Interval.greaterThanOrEqualTo(Rational.LARGEST_DOUBLE));
        for (Real x : take(TINY_LIMIT, xs)) {
            aeqd(x, x.doubleValueUnsafe(RoundingMode.FLOOR), Double.MAX_VALUE);
            aeqd(x, x.doubleValueUnsafe(RoundingMode.DOWN), Double.MAX_VALUE);
            aeqd(x, x.doubleValueUnsafe(RoundingMode.HALF_DOWN), Double.MAX_VALUE);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> x != Algebraic.ZERO && !x.equals(Algebraic.of(Rational.SMALLEST_DOUBLE.negate())),
                        P.withScale(2).algebraicsIn(Interval.of(Rational.SMALLEST_DOUBLE.negate(), Rational.ZERO))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqd(x, x.doubleValueUnsafe(RoundingMode.CEILING), -0.0);
            aeqd(x, x.doubleValueUnsafe(RoundingMode.DOWN), -0.0);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.equals(Algebraic.of(Rational.LARGEST_DOUBLE)),
                        P.withScale(2).withSecondaryScale(4)
                                .algebraicsIn(Interval.greaterThanOrEqualTo(Rational.LARGEST_DOUBLE))
                )
        );
        for (Real x : take(TINY_LIMIT, xs)) {
            double ceiling = x.doubleValueUnsafe(RoundingMode.CEILING);
            aeqd(x, ceiling, Double.POSITIVE_INFINITY);

            double up = x.doubleValueUnsafe(RoundingMode.UP);
            aeqd(x, up, Double.POSITIVE_INFINITY);

            double halfUp = x.doubleValueUnsafe(RoundingMode.HALF_UP);
            aeqd(x, halfUp, Double.POSITIVE_INFINITY);

            double halfEven = x.doubleValueUnsafe(RoundingMode.HALF_EVEN);
            aeqd(x, halfEven, Double.POSITIVE_INFINITY);
        }

        xs = P.withScale(2).withSecondaryScale(4)
                .cleanRealsIn(Interval.lessThanOrEqualTo(Rational.LARGEST_DOUBLE.negate()));
        for (Real x : take(TINY_LIMIT, xs)) {
            aeqd(x, x.doubleValueUnsafe(RoundingMode.CEILING), -Double.MAX_VALUE);
            aeqd(x, x.doubleValueUnsafe(RoundingMode.DOWN), -Double.MAX_VALUE);
            aeqd(x, x.doubleValueUnsafe(RoundingMode.HALF_DOWN), -Double.MAX_VALUE);
        }

        Iterable<Real> midpoints = map(
                d -> {
                    Rational lo = Rational.ofExact(d).get();
                    Rational hi = Rational.ofExact(successor(d)).get();
                    return of(lo.add(hi).shiftRight(1));
                },
                filter(d -> Double.isFinite(d) && !isNegativeZero(d) && d != Double.MAX_VALUE, P.doubles())
        );
        for (Real x : take(SMALL_LIMIT, midpoints)) {
            double down = x.doubleValueUnsafe(RoundingMode.DOWN);
            double up = x.doubleValueUnsafe(RoundingMode.UP);
            aeqd(x, x.doubleValueUnsafe(RoundingMode.HALF_DOWN), down);
            aeqd(x, x.doubleValueUnsafe(RoundingMode.HALF_UP), up);
            double halfEven = x.doubleValueUnsafe(RoundingMode.HALF_EVEN);
            assertTrue(x, ((Double.doubleToLongBits(down) & 1L) == 0L ? down : up) == halfEven);
        }

        Iterable<Real> notMidpoints = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !doubleEquidistant(x),
                        P.withScale(2).withSecondaryScale(4)
                                .algebraicsIn(Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE))
                )
        );
        for (Real x : take(TINY_LIMIT, notMidpoints)) {
            double below = x.doubleValueUnsafe(RoundingMode.FLOOR);
            double above = x.doubleValueUnsafe(RoundingMode.CEILING);
            Real belowDistance = x.subtract(ofExact(below).get());
            Real aboveDistance = ofExact(above).get().subtract(x);
            double closest = belowDistance.ltUnsafe(aboveDistance) ? below : above;
            aeqd(x, x.doubleValueUnsafe(RoundingMode.HALF_DOWN), closest);
            aeqd(x, x.doubleValueUnsafe(RoundingMode.HALF_UP), closest);
            aeqd(x, x.doubleValueUnsafe(RoundingMode.HALF_EVEN), closest);
        }

        xs = P.withScale(2).cleanRealsIn(Interval.of(Rational.ZERO, Rational.SMALLEST_DOUBLE.shiftRight(1)));
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqd(x, x.doubleValueUnsafe(RoundingMode.HALF_DOWN), 0.0);
            aeqd(x, x.doubleValueUnsafe(RoundingMode.HALF_EVEN), 0.0);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        r -> r != Algebraic.ZERO,
                        P.withScale(2).algebraicsIn(
                                Interval.of(Rational.SMALLEST_DOUBLE.shiftRight(1).negate(), Rational.ZERO)
                        )
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqd(x, x.doubleValueUnsafe(RoundingMode.HALF_DOWN), -0.0);
            aeqd(x, x.doubleValueUnsafe(RoundingMode.HALF_EVEN), -0.0);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.equals(Algebraic.of(Rational.SMALLEST_DOUBLE.shiftRight(1))),
                        P.withScale(2).algebraicsIn(Interval.of(Rational.ZERO, Rational.SMALLEST_DOUBLE.shiftRight(1)))
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqd(x, x.doubleValueUnsafe(RoundingMode.HALF_UP), 0.0);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> x != Algebraic.ZERO &&
                                !x.equals(Algebraic.of(Rational.SMALLEST_DOUBLE.shiftRight(1).negate())),
                        P.withScale(2).algebraicsIn(
                                Interval.of(Rational.SMALLEST_DOUBLE.shiftRight(1).negate(), Rational.ZERO)
                        )
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqd(x, x.doubleValueUnsafe(RoundingMode.HALF_UP), -0.0);
        }

        for (Real x : take(SMALL_LIMIT, P.withScale(2).cleanReals())) {
            double floor = x.doubleValueUnsafe(RoundingMode.FLOOR);
            assertFalse(x, isNegativeZero(floor));
            assertFalse(x, floor == Double.POSITIVE_INFINITY);
            double ceiling = x.doubleValueUnsafe(RoundingMode.CEILING);
            assertFalse(x, ceiling == Double.NEGATIVE_INFINITY);
            double down = x.doubleValueUnsafe(RoundingMode.DOWN);
            assertTrue(x, Double.isFinite(down));
            double up = x.doubleValueUnsafe(RoundingMode.UP);
            assertFalse(x, isNegativeZero(up));
            double halfDown = x.doubleValueUnsafe(RoundingMode.HALF_DOWN);
            assertTrue(x, Double.isFinite(halfDown));
        }

        Iterable<Real> xsFail = map(
                Algebraic::realValue,
                filterInfinite(y -> !y.isEqualToDouble(), P.withScale(2).algebraics())
        );
        for (Real x : take(LIMIT, xsFail)) {
            try {
                x.doubleValueUnsafe(RoundingMode.UNNECESSARY);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDoubleValue_RoundingMode_Rational() {
        initialize("doubleValue(RoundingMode, Rational)");
        Iterable<Triple<Real, RoundingMode, Rational>> ts = filterInfinite(
                s -> s.b != RoundingMode.UNNECESSARY || s.a.isExactAndEqualToDouble(),
                P.triples(P.withScale(4).reals(), P.roundingModes(), P.positiveRationals())
        );
        for (Triple<Real, RoundingMode, Rational> t : take(LIMIT, ts)) {
            Optional<Double> od = t.a.doubleValue(t.b, t.c);
            od.ifPresent(aDouble -> assertEquals(t, t.a.doubleValueUnsafe(t.b), aDouble));
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                filterInfinite(x -> !x.isExactAndEqualToDouble(), P.withScale(4).reals()),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.doubleValue(RoundingMode.UNNECESSARY, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Triple<Real, RoundingMode, Rational>> tsFail = filterInfinite(
                s -> s.b != RoundingMode.UNNECESSARY || s.a.isExactAndEqualToDouble(),
                P.triples(P.withScale(4).reals(), P.roundingModes(), P.rangeDown(Rational.ZERO))
        );
        for (Triple<Real, RoundingMode, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.doubleValue(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesDoubleValueUnsafe() {
        initialize("doubleValueUnsafe()");
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
            x.doubleValueUnsafe();
        }

        for (Real x : take(SMALL_LIMIT, P.withScale(2).cleanReals())) {
            double rounded = x.doubleValueUnsafe();
            aeqd(x, rounded, x.doubleValueUnsafe(RoundingMode.HALF_EVEN));
            assertTrue(x, !Double.isNaN(rounded));
            assertTrue(x, rounded == 0.0 || Math.signum(rounded) == x.signumUnsafe());
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.equals(Algebraic.of(Rational.LARGEST_DOUBLE.negate())),
                        P.withScale(2).withSecondaryScale(4)
                                .algebraicsIn(Interval.lessThanOrEqualTo(Rational.LARGEST_DOUBLE.negate()))
                )
        );
        for (Real x : take(TINY_LIMIT, xs)) {
            double rounded = x.doubleValueUnsafe();
            aeqd(x, rounded, Double.NEGATIVE_INFINITY);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !x.equals(Algebraic.of(Rational.LARGEST_DOUBLE)),
                        P.withScale(2).withSecondaryScale(4)
                                .algebraicsIn(Interval.greaterThanOrEqualTo(Rational.LARGEST_DOUBLE))
                )
        );
        for (Real x : take(TINY_LIMIT, xs)) {
            double rounded = x.doubleValueUnsafe();
            aeqd(x, rounded, Double.POSITIVE_INFINITY);
        }

        Iterable<Real> midpoints = map(
                d -> {
                    Rational lo = Rational.ofExact(d).get();
                    Rational hi = Rational.ofExact(successor(d)).get();
                    return of(lo.add(hi).shiftRight(1));
                },
                filter(d -> Double.isFinite(d) && d != Double.MAX_VALUE, P.doubles())
        );
        for (Real x : take(SMALL_LIMIT, midpoints)) {
            double down = x.doubleValueUnsafe(RoundingMode.DOWN);
            double up = x.doubleValueUnsafe(RoundingMode.UP);
            double rounded = x.doubleValueUnsafe();
            assertTrue(x, ((Double.doubleToLongBits(down) & 1L) == 0L ? down : up) == rounded);
        }

        Iterable<Real> notMidpoints = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> !doubleEquidistant(x),
                        P.withScale(2).withSecondaryScale(4)
                                .algebraicsIn(Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE))
                )
        );
        for (Real x : take(TINY_LIMIT, notMidpoints)) {
            double below = x.doubleValueUnsafe(RoundingMode.FLOOR);
            double above = x.doubleValueUnsafe(RoundingMode.CEILING);
            Real belowDistance = x.subtract(ofExact(below).get());
            Real aboveDistance = ofExact(above).get().subtract(x);
            double closest = belowDistance.ltUnsafe(aboveDistance) ? below : above;
            aeqd(x, x.doubleValueUnsafe(), closest);
        }

        xs = P.withScale(2).cleanRealsIn(Interval.of(Rational.ZERO, Rational.SMALLEST_DOUBLE.shiftRight(1)));
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqd(x, x.doubleValueUnsafe(), 0.0);
        }

        xs = map(
                Algebraic::realValue,
                filterInfinite(
                        x -> x != Algebraic.ZERO,
                        P.withScale(2).algebraicsIn(
                                Interval.of(Rational.SMALLEST_DOUBLE.shiftRight(1).negate(), Rational.ZERO)
                        )
                )
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            aeqd(x, x.doubleValueUnsafe(), -0.0);
        }
    }

    private void propertiesDoubleValue_Rational() {
        initialize("doubleValue(RoundingMode)");
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.positiveRationals()))) {
            Optional<Double> od = p.a.doubleValue(p.b);
            od.ifPresent(aDouble -> assertEquals(p, p.a.doubleValueUnsafe(), aDouble));
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.withScale(4).reals(), P.rangeDown(Rational.ZERO)))) {
            try {
                p.a.doubleValue(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesDoubleValueExact() {
        initialize("doubleValueExact()");
        for (Real x : take(SMALL_LIMIT, filterInfinite(Real::isExactAndEqualToDouble, P.withScale(2).cleanReals()))) {
            double d = x.doubleValueExact();
            assertTrue(x, !Double.isNaN(d));
            assertTrue(x, d == 0.0 || Math.signum(d) == x.signumUnsafe());
            homomorphic(Real::negate, g -> absNegativeZeros(-g), Real::doubleValueExact, Real::doubleValueExact, x);
        }

        Iterable<Real> xs = map(
                d -> ofExact(d).get(),
                filter(d -> Double.isFinite(d) && !isNegativeZero(d), P.doubles())
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            double d = x.doubleValueExact();
            assertEquals(x, x.rationalValueExact().get(), Rational.ofExact(d).get());
            assertTrue(x, Double.isFinite(d));
            assertTrue(x, !isNegativeZero(d));
        }

        for (Real x : take(LIMIT, filterInfinite(y -> !y.isExactAndEqualToDouble(), P.withScale(2).cleanReals()))) {
            try {
                x.doubleValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
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
                    boolean lowerEven = !x.bigDecimalValueByPrecision(precision, RoundingMode.FLOOR).unscaledValue()
                            .testBit(0);
                    return !(lowerEven ? right : left);
                case UNNECESSARY:
                    return false;
                default:
                    return true;
            }
        }
        return rm != RoundingMode.UNNECESSARY;
    }

    private void propertiesBigDecimalValueByPrecisionUnsafe_int_RoundingMode() {
        initialize("bigDecimalValueByPrecisionUnsafe(int, RoundingMode)");
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
            BigDecimal bd = t.a.bigDecimalValueByPrecisionUnsafe(t.b, t.c);
            if (ne(bd, BigDecimal.ZERO)) {
                assertTrue(t, t.b == 0 || bd.precision() == t.b);
            }
        }

        Predicate<Triple<Real, Integer, RoundingMode>> valid = t -> {
            try {
                t.a.bigDecimalValueByPrecisionUnsafe(t.b, t.c);
                return true;
            } catch (ArithmeticException e) {
                return false;
            }
        };
        ts = filterInfinite(valid, P.triples(P.cleanReals(), P.naturalIntegersGeometric(), P.roundingModes()));
        for (Triple<Real, Integer, RoundingMode> t : take(LIMIT, ts)) {
            BigDecimal bd = t.a.bigDecimalValueByPrecisionUnsafe(t.b, t.c);
            assertTrue(t, eq(bd, BigDecimal.ZERO) || bd.signum() == t.a.signumUnsafe());
        }

        ts = filterInfinite(valid, P.triples(P.nonzeroCleanReals(), P.positiveIntegersGeometric(), P.roundingModes()));
        for (Triple<Real, Integer, RoundingMode> t : take(LIMIT, ts)) {
            BigDecimal bd = t.a.bigDecimalValueByPrecisionUnsafe(t.b, t.c);
            assertTrue(t, bd.precision() == t.b);
        }

        Iterable<Pair<Real, Integer>> ps = filterInfinite(
                q -> valid.test(new Triple<>(q.a, q.b, RoundingMode.UNNECESSARY)),
                P.pairsSquareRootOrder(map(Algebraic::realValue, P.algebraics(1)), P.naturalIntegersGeometric())
        );
        for (Pair<Real, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.UNNECESSARY);
            assertEquals(p, bd, p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.FLOOR));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.CEILING));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.DOWN));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.UP));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.HALF_DOWN));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.HALF_UP));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.HALF_EVEN));
        }

        ps = filterInfinite(
                q -> valid.test(new Triple<>(q.a, q.b, RoundingMode.FLOOR)) &&
                        (!q.a.isExact() ||
                                !Rational.of(q.a.bigDecimalValueByPrecisionUnsafe(q.b))
                                        .equals(q.a.rationalValueExact().get())),
                P.pairsSquareRootOrder(P.cleanReals(), P.naturalIntegersGeometric())
        );
        for (Pair<Real, Integer> p : take(LIMIT, ps)) {
            BigDecimal low = p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.FLOOR);
            BigDecimal high = p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.CEILING);
            assertTrue(p, lt(low, high));
        }

        ps = filterInfinite(
                q -> valid.test(new Triple<>(q.a, q.b, RoundingMode.FLOOR)) &&
                        (!q.a.isExact() ||
                                !Rational.of(q.a.bigDecimalValueByPrecisionUnsafe(q.b))
                                        .equals(q.a.rationalValueExact().get())),
                P.pairsSquareRootOrder(P.positiveCleanReals(), P.naturalIntegersGeometric())
        );
        for (Pair<Real, Integer> p : take(LIMIT, ps)) {
            BigDecimal floor = p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.FLOOR);
            BigDecimal down = p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.DOWN);
            BigDecimal ceiling = p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.CEILING);
            BigDecimal up = p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.UP);
            assertEquals(p, floor, down);
            assertEquals(p, ceiling, up);
        }

        ps = filterInfinite(
                q -> valid.test(new Triple<>(q.a, q.b, RoundingMode.FLOOR)) &&
                        (!q.a.isExact() ||
                                !Rational.of(q.a.bigDecimalValueByPrecisionUnsafe(q.b))
                                        .equals(q.a.rationalValueExact().get())),
                P.pairsSquareRootOrder(P.negativeCleanReals(), P.naturalIntegersGeometric())
        );
        for (Pair<Real, Integer> p : take(LIMIT, ps)) {
            BigDecimal floor = p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.FLOOR);
            BigDecimal down = p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.DOWN);
            BigDecimal ceiling = p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.CEILING);
            BigDecimal up = p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.UP);
            assertEquals(p, floor, up);
            assertEquals(p, ceiling, down);
        }

        BigInteger five = BigInteger.valueOf(5);
        Iterable<BigDecimal> bds = filterInfinite(
                bd -> bd.precision() > 1 && !bd.abs().unscaledValue().mod(BigInteger.TEN).equals(five),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int precision = bd.precision() - 1;
            Real x = of(bd);
            BigDecimal down = x.bigDecimalValueByPrecisionUnsafe(precision, RoundingMode.DOWN);
            BigDecimal up = x.bigDecimalValueByPrecisionUnsafe(precision, RoundingMode.UP);
            BigDecimal halfDown = x.bigDecimalValueByPrecisionUnsafe(precision, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = x.bigDecimalValueByPrecisionUnsafe(precision, RoundingMode.HALF_UP);
            BigDecimal halfEven = x.bigDecimalValueByPrecisionUnsafe(precision, RoundingMode.HALF_EVEN);
            BigDecimal closest = x.subtract(of(down)).abs().ltUnsafe(x.subtract(of(up)).abs()) ? down : up;
            assertEquals(bd, halfDown, closest);
            assertEquals(bd, halfUp, closest);
            assertEquals(bd, halfEven, closest);
        }

        bds = filterInfinite(
                bd -> bd.precision() > 1,
                map(
                        bd -> new BigDecimal(bd.unscaledValue().multiply(BigInteger.TEN).add(five), bd.scale()),
                        P.bigDecimals()
                )
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int precision = bd.precision() - 1;
            Real x = of(bd);
            BigDecimal down = x.bigDecimalValueByPrecisionUnsafe(precision, RoundingMode.DOWN);
            BigDecimal up = x.bigDecimalValueByPrecisionUnsafe(precision, RoundingMode.UP);
            BigDecimal halfDown = x.bigDecimalValueByPrecisionUnsafe(precision, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = x.bigDecimalValueByPrecisionUnsafe(precision, RoundingMode.HALF_UP);
            BigDecimal halfEven = x.bigDecimalValueByPrecisionUnsafe(precision, RoundingMode.HALF_EVEN);
            assertEquals(bd, down, halfDown);
            assertEquals(bd, up, halfUp);
            assertTrue(bd, bd.scale() != halfEven.scale() + 1 || !halfEven.unscaledValue().testBit(0));
        }

        Iterable<Triple<Real, Integer, RoundingMode>> tsFail = P.triples(
                P.nonzeroCleanReals(),
                P.negativeIntegers(),
                P.roundingModes()
        );
        for (Triple<Real, Integer, RoundingMode> t : take(LIMIT, tsFail)) {
            try {
                t.a.bigDecimalValueByPrecisionUnsafe(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException | ArithmeticException ignored) {}
        }

        Iterable<Pair<Real, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(
                        x -> !x.isExact() || !x.rationalValueExact().get().hasTerminatingBaseExpansion(BigInteger.TEN),
                        P.cleanReals()
                ),
                P.naturalIntegersGeometric()
        );
        for (Pair<Real, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.UNNECESSARY);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesBigDecimalValueByPrecision_int_RoundingMode_Rational() {
        initialize("bigDecimalValueByPrecision(int, RoundingMode, Rational)");
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
            Optional<BigDecimal> obd = q.a.bigDecimalValueByPrecision(q.b, q.c, q.d);
            obd.ifPresent(bigDecimal -> assertEquals(q, q.a.bigDecimalValueByPrecisionUnsafe(q.b, q.c), bigDecimal));
        }

        Iterable<Triple<Real, Integer, Rational>> tsFail = filterInfinite(
                t -> {
                    if (!t.a.isExact()) {
                        return true;
                    }
                    Rational r = t.a.rationalValueExact().get();
                    return !r.hasTerminatingBaseExpansion(BigInteger.TEN) ||
                            (r != Rational.ZERO && t.b != 0 && r.bigDecimalValueExact().precision() > t.b);
                },
                P.triples(P.withScale(4).reals(), P.naturalIntegersGeometric(), P.positiveRationals())
        );
        for (Triple<Real, Integer, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.bigDecimalValueByPrecision(t.b, RoundingMode.UNNECESSARY, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Triple<Real, RoundingMode, Rational>> tsFail2 = filterInfinite(
                t -> {
                    if (!t.a.isExact()) {
                        return true;
                    }
                    Rational r = t.a.rationalValueExact().get();
                    try {
                        r.bigDecimalValueByPrecision(0, t.b);
                        return false;
                    } catch (ArithmeticException ignored) {
                        return true;
                    }
                },
                P.triples(P.withScale(4).reals(), P.roundingModes(), P.positiveRationals())
        );
        for (Triple<Real, RoundingMode, Rational> t : take(LIMIT, tsFail2)) {
            try {
                t.a.bigDecimalValueByPrecision(0, t.b, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Quadruple<Real, Integer, RoundingMode, Rational>> qsFail = P.quadruples(
                P.withScale(4).reals(),
                P.naturalIntegersGeometric(),
                P.roundingModes(),
                P.rangeDown(Rational.ZERO)
        );
        for (Quadruple<Real, Integer, RoundingMode, Rational> q : take(LIMIT, qsFail)) {
            try {
                q.a.bigDecimalValueByPrecision(q.b, q.c, q.d);
                fail(q);
            } catch (IllegalArgumentException ignored) {}
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

    private void propertiesBigDecimalValueByScaleUnsafe_int_RoundingMode() {
        initialize("bigDecimalValueByScaleUnsafe(int, RoundingMode)");
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
            BigDecimal bd = t.a.bigDecimalValueByScaleUnsafe(t.b, t.c);
            assertEquals(t, bd.scale(), t.b);
        }

        Predicate<Triple<Real, Integer, RoundingMode>> valid =
                t -> t.c != RoundingMode.UNNECESSARY ||
                        t.a.isExact() && t.a.rationalValueExact().get().multiply(Rational.TEN.pow(t.b)).isInteger();
        ts = filterInfinite(valid, P.triples(P.cleanReals(), P.integersGeometric(), P.roundingModes()));
        for (Triple<Real, Integer, RoundingMode> t : take(LIMIT, ts)) {
            BigDecimal bd = t.a.bigDecimalValueByScaleUnsafe(t.b, t.c);
            assertTrue(t, eq(bd, BigDecimal.ZERO) || bd.signum() == t.a.signumUnsafe());
        }

        Iterable<Pair<Real, Integer>> ps = filterInfinite(
                q -> q.a.isExact() && q.a.rationalValueExact().get().multiply(Rational.TEN.pow(q.b)).isInteger(),
                P.pairsSquareRootOrder(map(Algebraic::realValue, P.algebraics(1)), P.integersGeometric())
        );
        for (Pair<Real, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.UNNECESSARY);
            assertEquals(p, bd, p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.FLOOR));
            assertEquals(p, bd, p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.CEILING));
            assertEquals(p, bd, p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.DOWN));
            assertEquals(p, bd, p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.UP));
            assertEquals(p, bd, p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.HALF_DOWN));
            assertEquals(p, bd, p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.HALF_UP));
            assertEquals(p, bd, p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.HALF_EVEN));
        }

        ps = filterInfinite(
                q -> !q.a.isExact() || !q.a.rationalValueExact().get().multiply(Rational.TEN.pow(q.b)).isInteger(),
                P.pairsSquareRootOrder(P.cleanReals(), P.integersGeometric())
        );
        for (Pair<Real, Integer> p : take(LIMIT, ps)) {
            BigDecimal low = p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.FLOOR);
            BigDecimal high = p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.CEILING);
            Rational lowR = Rational.of(low);
            Rational highR = Rational.of(high);
            assertTrue(p, p.a.gtUnsafe(lowR));
            assertTrue(p, p.a.ltUnsafe(highR));
            assertEquals(p, highR.subtract(lowR), Rational.TEN.pow(-p.b));
        }

        ps = filterInfinite(
                q -> !q.a.isExact() || !q.a.rationalValueExact().get().multiply(Rational.TEN.pow(q.b)).isInteger(),
                P.pairsSquareRootOrder(P.positiveCleanReals(), P.integersGeometric())
        );
        for (Pair<Real, Integer> p : take(LIMIT, ps)) {
            BigDecimal floor = p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.FLOOR);
            BigDecimal down = p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.DOWN);
            BigDecimal ceiling = p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.CEILING);
            BigDecimal up = p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.UP);
            assertEquals(p, floor, down);
            assertEquals(p, ceiling, up);
        }

        ps = filterInfinite(
                q -> !q.a.isExact() || !q.a.rationalValueExact().get().multiply(Rational.TEN.pow(q.b)).isInteger(),
                P.pairsSquareRootOrder(P.negativeCleanReals(), P.integersGeometric())
        );
        for (Pair<Real, Integer> p : take(LIMIT, ps)) {
            BigDecimal floor = p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.FLOOR);
            BigDecimal down = p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.DOWN);
            BigDecimal ceiling = p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.CEILING);
            BigDecimal up = p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.UP);
            assertEquals(p, floor, up);
            assertEquals(p, ceiling, down);
        }

        BigInteger five = BigInteger.valueOf(5);
        Iterable<BigDecimal> bds = filterInfinite(
                bd -> !bd.abs().unscaledValue().mod(BigInteger.TEN).equals(five),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int scale = bd.scale() - 1;
            Real x = of(bd);
            BigDecimal down = x.bigDecimalValueByScaleUnsafe(scale, RoundingMode.DOWN);
            BigDecimal up = x.bigDecimalValueByScaleUnsafe(scale, RoundingMode.UP);
            BigDecimal halfDown = x.bigDecimalValueByScaleUnsafe(scale, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = x.bigDecimalValueByScaleUnsafe(scale, RoundingMode.HALF_UP);
            BigDecimal halfEven = x.bigDecimalValueByScaleUnsafe(scale, RoundingMode.HALF_EVEN);
            BigDecimal closest = x.subtract(of(down)).abs().ltUnsafe(x.subtract(of(up)).abs()) ? down : up;
            assertEquals(bd, halfDown, closest);
            assertEquals(bd, halfUp, closest);
            assertEquals(bd, halfEven, closest);
        }

        bds = map(
                bd -> new BigDecimal(bd.unscaledValue().multiply(BigInteger.TEN).add(five), bd.scale()),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int scale = bd.scale() - 1;
            Real x = of(bd);
            BigDecimal down = x.bigDecimalValueByScaleUnsafe(scale, RoundingMode.DOWN);
            BigDecimal up = x.bigDecimalValueByScaleUnsafe(scale, RoundingMode.UP);
            BigDecimal halfDown = x.bigDecimalValueByScaleUnsafe(scale, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = x.bigDecimalValueByScaleUnsafe(scale, RoundingMode.HALF_UP);
            BigDecimal halfEven = x.bigDecimalValueByScaleUnsafe(scale, RoundingMode.HALF_EVEN);
            assertEquals(bd, down, halfDown);
            assertEquals(bd, up, halfUp);
            assertTrue(bd, !halfEven.unscaledValue().testBit(0));
        }

        Iterable<Pair<Real, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(x -> !x.isExact() ||
                        !x.rationalValueExact().get().hasTerminatingBaseExpansion(BigInteger.TEN), P.cleanReals()),
                P.naturalIntegersGeometric()
        );
        for (Pair<Real, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.UNNECESSARY);
                fail(p);
            } catch (IllegalArgumentException | ArithmeticException ignored) {}
        }
    }

    private void propertiesBigDecimalValueByScale_int_RoundingMode_Rational() {
        initialize("bigDecimalValueByScale(int, RoundingMode, Rational)");
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
            Optional<BigDecimal> obd = q.a.bigDecimalValueByScale(q.b, q.c, q.d);
            obd.ifPresent(bigDecimal -> assertEquals(q, q.a.bigDecimalValueByScaleUnsafe(q.b, q.c), bigDecimal));
        }

        Iterable<Triple<Real, Integer, Rational>> tsFail = filterInfinite(
                t -> {
                    if (!t.a.isExact()) {
                        return true;
                    }
                    Rational r = t.a.rationalValueExact().get();
                    return !r.hasTerminatingBaseExpansion(BigInteger.TEN) ||
                            (r != Rational.ZERO && t.b != 0 && r.bigDecimalValueExact().precision() > t.b);
                },
                P.triples(P.withScale(4).reals(), P.integersGeometric(), P.positiveRationals())
        );
        for (Triple<Real, Integer, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.bigDecimalValueByScale(t.b, RoundingMode.UNNECESSARY, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Quadruple<Real, Integer, RoundingMode, Rational>> qsFail = P.quadruples(
                P.withScale(4).reals(),
                P.integersGeometric(),
                P.roundingModes(),
                P.rangeDown(Rational.ZERO)
        );
        for (Quadruple<Real, Integer, RoundingMode, Rational> q : take(LIMIT, qsFail)) {
            try {
                q.a.bigDecimalValueByScale(q.b, q.c, q.d);
                fail(q);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesBigDecimalValueByPrecisionUnsafe_int() {
        initialize("bigDecimalValueByPrecisionUnsafe(int)");
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
            BigDecimal bd = p.a.bigDecimalValueByPrecisionUnsafe(p.b);
            if (ne(bd, BigDecimal.ZERO)) {
                assertTrue(p, p.b == 0 || bd.precision() == p.b);
            }
        }

        Predicate<Pair<Real, Integer>> valid = p -> {
            try {
                p.a.bigDecimalValueByPrecisionUnsafe(p.b);
                return true;
            } catch (ArithmeticException e) {
                return false;
            }
        };
        ps = filterInfinite(
                valid,
                P.pairsSquareRootOrder(P.cleanReals(), P.naturalIntegersGeometric())
        );
        for (Pair<Real, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValueByPrecisionUnsafe(p.b);
            assertEquals(p, bd, p.a.bigDecimalValueByPrecisionUnsafe(p.b, RoundingMode.HALF_EVEN));
            assertTrue(p, eq(bd, BigDecimal.ZERO) || bd.signum() == p.a.signumUnsafe());
        }

        ps = filterInfinite(valid, P.pairsSquareRootOrder(P.nonzeroCleanReals(), P.positiveIntegersGeometric()));
        for (Pair<Real, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValueByPrecisionUnsafe(p.b);
            assertTrue(p, bd.precision() == p.b);
        }

        BigInteger five = BigInteger.valueOf(5);
        Iterable<BigDecimal> bds = filterInfinite(
                bd -> bd.precision() > 1 && !bd.abs().unscaledValue().mod(BigInteger.TEN).equals(five),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int precision = bd.precision() - 1;
            Real x = of(bd);
            BigDecimal down = x.bigDecimalValueByPrecisionUnsafe(precision, RoundingMode.DOWN);
            BigDecimal up = x.bigDecimalValueByPrecisionUnsafe(precision, RoundingMode.UP);
            BigDecimal halfEven = x.bigDecimalValueByPrecisionUnsafe(precision);
            boolean closerToDown = x.subtract(of(down)).abs().ltUnsafe(x.subtract(of(up)).abs());
            assertEquals(bd, halfEven, closerToDown ? down : up);
        }

        bds = filterInfinite(
                bd -> bd.precision() > 1,
                map(
                        bd -> new BigDecimal(bd.unscaledValue().multiply(BigInteger.TEN).add(five), bd.scale()),
                        P.bigDecimals()
                )
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int precision = bd.precision() - 1;
            Real x = of(bd);
            BigDecimal halfEven = x.bigDecimalValueByPrecisionUnsafe(precision);
            assertTrue(bd, bd.scale() != halfEven.scale() + 1 || !halfEven.unscaledValue().testBit(0));
        }

        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.cleanReals(), P.negativeIntegers()))) {
            try {
                p.a.bigDecimalValueByPrecisionUnsafe(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesBigDecimalValueByPrecision_int_Rational() {
        initialize("bigDecimalValueByPrecision(int, Rational)");
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
            Optional<BigDecimal> obd = t.a.bigDecimalValueByPrecision(t.b, t.c);
            obd.ifPresent(bigDecimal -> assertEquals(t, t.a.bigDecimalValueByPrecisionUnsafe(t.b), bigDecimal));
        }

        Iterable<Pair<Real, Rational>> psFail2 = filterInfinite(
                p -> {
                    if (!p.a.isExact()) {
                        return true;
                    }
                    Rational r = p.a.rationalValueExact().get();
                    try {
                        r.bigDecimalValueByPrecision(0);
                        return false;
                    } catch (ArithmeticException ignored) {
                        return true;
                    }
                },
                P.pairs(P.withScale(4).reals(), P.positiveRationals())
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail2)) {
            try {
                p.a.bigDecimalValueByPrecision(0, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Triple<Real, Integer, Rational>> tsFail = P.triples(
                P.withScale(4).reals(),
                P.naturalIntegersGeometric(),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, Integer, Rational> q : take(LIMIT, tsFail)) {
            try {
                q.a.bigDecimalValueByPrecision(q.b, q.c);
                fail(q);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesBigDecimalValueByScaleUnsafe_int() {
        initialize("bigDecimalValueByScaleUnsafe(int)");
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
            BigDecimal bd = p.a.bigDecimalValueByScaleUnsafe(p.b);
            assertEquals(p, bd.scale(), p.b);
        }

        for (Pair<Real, Integer> p : take(LIMIT, P.pairsSquareRootOrder(P.cleanReals(), P.integersGeometric()))) {
            BigDecimal bd = p.a.bigDecimalValueByScaleUnsafe(p.b);
            assertEquals(p, bd, p.a.bigDecimalValueByScaleUnsafe(p.b, RoundingMode.HALF_EVEN));
            assertTrue(p, eq(bd, BigDecimal.ZERO) || bd.signum() == p.a.signumUnsafe());
            assertTrue(p, bd.scale() == p.b);
        }

        BigInteger five = BigInteger.valueOf(5);
        Iterable<BigDecimal> bds = filterInfinite(
                bd -> !bd.abs().unscaledValue().mod(BigInteger.TEN).equals(five),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int scale = bd.scale() - 1;
            Real x = of(bd);
            BigDecimal down = x.bigDecimalValueByScaleUnsafe(scale, RoundingMode.DOWN);
            BigDecimal up = x.bigDecimalValueByScaleUnsafe(scale, RoundingMode.UP);
            BigDecimal halfEven = x.bigDecimalValueByScaleUnsafe(scale);
            BigDecimal closer = x.subtract(of(down)).abs().ltUnsafe(x.subtract(of(up)).abs()) ? down : up;
            assertEquals(bd, halfEven, closer);
        }

        bds = map(
                bd -> new BigDecimal(bd.unscaledValue().multiply(BigInteger.TEN).add(five), bd.scale()),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int scale = bd.scale() - 1;
            Real x = of(bd);
            BigDecimal halfEven = x.bigDecimalValueByScaleUnsafe(scale);
            assertTrue(bd, !halfEven.unscaledValue().testBit(0));
        }
    }

    private void propertiesBigDecimalValueByScale_int_Rational() {
        initialize("bigDecimalValueByScale(int, Rational)");
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
            Optional<BigDecimal> obd = t.a.bigDecimalValueByScale(t.b, t.c);
            obd.ifPresent(bigDecimal -> assertEquals(t, t.a.bigDecimalValueByScaleUnsafe(t.b), bigDecimal));
        }

        Iterable<Triple<Real, Integer, Rational>> tsFail = P.triples(
                P.withScale(4).reals(),
                P.integersGeometric(),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, Integer, Rational> q : take(LIMIT, tsFail)) {
            try {
                q.a.bigDecimalValueByScale(q.b, q.c);
                fail(q);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesBigDecimalValueExact() {
        initialize("bigDecimalValueExact()");
        Iterable<Real> xs = map(
                Real::of,
                filterInfinite(r -> r.hasTerminatingBaseExpansion(BigInteger.TEN), P.rationals())
        );
        for (Real x : take(LIMIT, xs)) {
            BigDecimal bd = x.bigDecimalValueExact();
            assertTrue(bd, BigDecimalUtils.isCanonical(bd));
            assertEquals(x, bd, x.bigDecimalValueByPrecisionUnsafe(0, RoundingMode.UNNECESSARY));
            assertEquals(x, bd.signum(), x.signumUnsafe());
            homomorphic(Real::negate, BigDecimal::negate, Real::bigDecimalValueExact, Real::bigDecimalValueExact, x);
        }

        Iterable<Real> xsFail = filterInfinite(
                x -> !x.isExact() || !x.rationalValueExact().get().hasTerminatingBaseExpansion(BigInteger.TEN),
                P.reals()
        );
        for (Real x : take(LIMIT, xsFail)) {
            try {
                x.bigDecimalValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (Real x : take(LIMIT, P.reals())) {
            Real negative = x.negate();
            negative.validate();
            //noinspection SuspiciousNameCombination
            assertTrue(x, negative.negate().eq(x, DEFAULT_RESOLUTION).orElse(true));
            assertTrue(x, x.add(negative).eq(Rational.ZERO, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.nonzeroReals())) {
            assertTrue(x, x.ne(x.negate(), DEFAULT_RESOLUTION).orElse(true));
        }
    }

    private void propertiesAbs() {
        initialize("abs()");
        for (Real x : take(LIMIT, P.reals())) {
            Real abs = x.abs();
            abs.validate();
            assertTrue(x, abs.eq(abs.abs(), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(x, abs.signum(DEFAULT_RESOLUTION).map(s -> s != -1).orElse(true));
            assertTrue(x, abs.ge(Rational.ZERO, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.positiveReals())) {
            assertTrue(x, x.abs().eq(x, DEFAULT_RESOLUTION).orElse(true));
        }
    }

    private void propertiesSignumUnsafe() {
        initialize("signumUnsafe()");
        Iterable<Real> xs = P.withScale(1).choose(
                map(Algebraic::realValue, P.algebraics()),
                P.choose(
                        Arrays.asList(
                                map(Real::leftFuzzyRepresentation, P.nonzeroRationals()),
                                map(Real::rightFuzzyRepresentation, P.nonzeroRationals()),
                                map(Real::fuzzyRepresentation, P.nonzeroRationals())
                        )
                )
        );
        for (Real x : take(LIMIT, xs)) {
            int signum = x.signumUnsafe();
            assertEquals(x, signum, x.compareToUnsafe(Rational.ZERO));
            assertTrue(x, signum == -1 || signum == 0 || signum == 1);
        }
    }

    private void propertiesSignum() {
        initialize("signum(Rational)");
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.positiveRationals()))) {
            Optional<Integer> oi = p.a.signum(p.b);
            oi.ifPresent(integer -> assertEquals(p, p.a.signumUnsafe(), integer));
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.rangeDown(Rational.ZERO)))) {
            try {
                p.a.signum(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAdd_Rational() {
        initialize("add(Rational)");
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.rationals()))) {
            Real sum = p.a.add(p.b);
            sum.validate();
            assertTrue(p, sum.subtract(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.add(Rational.ZERO) == x);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ZERO.add(r).rationalValueExact().get(), r);
        }
    }

    private void propertiesAdd_Real() {
        initialize("add(Real)");
        for (Pair<Real, Real> p : take(LIMIT, P.pairs(P.reals()))) {
            Real sum = p.a.add(p.b);
            sum.validate();
            assertTrue(p, p.b.add(p.a).eq(sum, DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, sum.subtract(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.add(ZERO) == x);
            assertTrue(x, ZERO.add(x) == x);
            assertTrue(x, x.add(x.negate()).eq(Rational.ZERO, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Triple<Real, Real, Real> t : take(SMALL_LIMIT, P.triples(P.reals()))) {
            assertTrue(t, t.a.add(t.b).add(t.c).eq(t.a.add(t.b.add(t.c)), DEFAULT_RESOLUTION).orElse(true));
        }
    }

    private void propertiesSubtract_Rational() {
        initialize("subtract(Rational)");
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.rationals()))) {
            Real difference = p.a.subtract(p.b);
            difference.validate();
            assertTrue(p, difference.add(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.subtract(Rational.ZERO) == x);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ZERO.subtract(r).rationalValueExact().get(), r.negate());
        }
    }

    private void propertiesSubtract_Real() {
        initialize("subtract(Real)");
        for (Pair<Real, Real> p : take(SMALL_LIMIT, P.pairs(P.reals()))) {
            Real difference = p.a.subtract(p.b);
            difference.validate();
            assertTrue(p, p.b.subtract(p.a).eq(difference.negate(), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, difference.add(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, ZERO.subtract(x).eq(x.negate(), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(x, x.subtract(ZERO) == x);
            assertTrue(x, x.subtract(x) == ZERO);
        }
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.reals(), P.integers()))) {
            Real product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.a.multiply(of(p.b)).eq(product, DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, of(p.b).multiply(p.a).eq(product, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.reals(), P.nonzeroIntegers()))) {
            assertTrue(p, p.a.multiply(p.b).divide(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
        }

        for (int i : take(LIMIT, P.integers())) {
            assertTrue(i, ONE.multiply(i).eq(Rational.of(i), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(i, ZERO.multiply(i) == ZERO);
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.multiply(1) == x);
            assertTrue(x, x.multiply(0) == ZERO);
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            assertTrue(i, of(i).invertUnsafe().multiply(i).eq(Rational.ONE, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Triple<Real, Real, Integer> t : take(SMALL_LIMIT, P.triples(P.reals(), P.reals(), P.integers()))) {
            assertTrue(t, t.a.add(t.b).multiply(t.c).eq(t.a.multiply(t.c).add(t.b.multiply(t.c)), DEFAULT_RESOLUTION)
                    .orElse(true));
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        for (Pair<Real, BigInteger> p : take(LIMIT, P.pairs(P.reals(), P.bigIntegers()))) {
            Real product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.a.multiply(of(p.b)).eq(product, DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, of(p.b).multiply(p.a).eq(product, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Real, BigInteger> p : take(LIMIT, P.pairs(P.reals(), P.nonzeroBigIntegers()))) {
            assertTrue(p, p.a.multiply(p.b).divide(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertTrue(i, ONE.multiply(i).eq(Rational.of(i), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(i, ZERO.multiply(i) == ZERO);
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.multiply(BigInteger.ONE) == x);
            assertTrue(x, x.multiply(BigInteger.ZERO) == ZERO);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            assertTrue(i, of(i).invertUnsafe().multiply(i).eq(Rational.ONE, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Triple<Real, Real, BigInteger> t : take(SMALL_LIMIT, P.triples(P.reals(), P.reals(), P.bigIntegers()))) {
            assertTrue(t, t.a.add(t.b).multiply(t.c).eq(t.a.multiply(t.c).add(t.b.multiply(t.c)), DEFAULT_RESOLUTION)
                    .orElse(true));
        }
    }

    private void propertiesMultiply_Rational() {
        initialize("multiply(Rational)");
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.rationals()))) {
            Real product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.a.multiply(of(p.b)).eq(product, DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, of(p.b).multiply(p.a).eq(product, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.nonzeroRationals()))) {
            Real product = p.a.multiply(p.b);
            assertTrue(p, product.divide(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, product.eq(p.a.divide(p.b.invert()), DEFAULT_RESOLUTION).orElse(true));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(r, ONE.multiply(r).eq(r, DEFAULT_RESOLUTION).orElse(true));
            assertTrue(r, ZERO.multiply(r) == ZERO);
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.multiply(Rational.ONE) == x);
            assertTrue(x, x.multiply(Rational.ZERO) == ZERO);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            assertTrue(r, of(r).invertUnsafe().multiply(r).eq(Rational.ONE, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Triple<Real, Real, Rational> t : take(SMALL_LIMIT, P.triples(P.reals(), P.reals(), P.rationals()))) {
            assertTrue(t, t.a.add(t.b).multiply(t.c).eq(t.a.multiply(t.c).add(t.b.multiply(t.c)), DEFAULT_RESOLUTION)
                    .orElse(true));
        }
    }

    private void propertiesMultiply_Real() {
        initialize("multiply(Real)");
        for (Pair<Real, Real> p : take(SMALL_LIMIT, P.pairs(P.reals()))) {
            Real product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b.multiply(p.a).eq(product, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Real, Real> p : take(TINY_LIMIT, P.pairs(P.reals(), P.nonzeroReals()))) {
            Real product = p.a.multiply(p.b);
            assertTrue(p, product.divideUnsafe(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, product.eq(p.a.divideUnsafe(p.b.invertUnsafe()), DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.multiply(Rational.ONE) == x);
            assertTrue(x, x.multiply(Rational.ZERO) == ZERO);
        }

        for (Real x : take(LIMIT, P.nonzeroReals())) {
            assertTrue(x, x.invertUnsafe().multiply(x).eq(Rational.ONE, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Triple<Real, Real, Real> t : take(TINY_LIMIT, P.triples(P.reals()))) {
            assertTrue(t, t.a.multiply(t.b).multiply(t.c).eq(t.a.multiply(t.b.multiply(t.c)), DEFAULT_RESOLUTION)
                    .orElse(true));
            assertTrue(t, t.c.multiply(t.a.add(t.b)).eq(t.c.multiply(t.a).add(t.c.multiply(t.b)), DEFAULT_RESOLUTION)
                    .orElse(true));
            assertTrue(t, t.a.add(t.b).multiply(t.c).eq(t.a.multiply(t.c).add(t.b.multiply(t.c)), DEFAULT_RESOLUTION)
                    .orElse(true));
        }
    }

    private void propertiesInvertUnsafe() {
        initialize("invertUnsafe()");
        for (Real x : take(LIMIT, P.nonzeroReals())) {
            Real y = x.invertUnsafe();
            y.validate();
            assertTrue(x, y.invertUnsafe().eq(x, DEFAULT_RESOLUTION).orElse(true));
            assertTrue(x, x.invertUnsafe().multiply(x).eq(Rational.ONE, DEFAULT_RESOLUTION).orElse(true));
            assertTrue(x, y.signum(DEFAULT_RESOLUTION).map(s -> s != 0).orElse(true));
        }

        Iterable<Rational> rs = filterInfinite(r -> r.abs() != Rational.ONE, P.withScale(4).nonzeroRationals());
        Iterable<Real> xs = P.withScale(1).choose(
                map(
                        Algebraic::realValue,
                        filterInfinite(x -> x.abs() != Algebraic.ONE, P.withScale(4).nonzeroAlgebraics())
                ),
                P.choose(
                        Arrays.asList(
                                map(Real::leftFuzzyRepresentation, rs),
                                map(Real::rightFuzzyRepresentation, rs),
                                map(Real::fuzzyRepresentation, rs)
                        )
                )
        );
        for (Real x : take(LIMIT, xs)) {
            assertTrue(x, x.ne(x.invertUnsafe(), DEFAULT_RESOLUTION).orElse(true));
        }
    }

    private void propertiesInvert() {
        initialize("invert(Rational)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                P.withScale(1).choose(
                        map(Algebraic::realValue, P.withScale(4).nonzeroAlgebraics()),
                        P.choose(
                                Arrays.asList(
                                        map(Real::leftFuzzyRepresentation, P.withScale(4).rationals()),
                                        map(Real::rightFuzzyRepresentation, P.withScale(4).rationals()),
                                        map(Real::fuzzyRepresentation, P.withScale(4).rationals())
                                )
                        )
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            Optional<Real> ox = p.a.invert(p.b);
            if (ox.isPresent()) {
                ox.get().validate();
                assertTrue(p, p.a.invertUnsafe().eq(ox.get(), DEFAULT_RESOLUTION).orElse(true));
            }
        }

        for (Rational r : take(LIMIT, P.positiveRationals())) {
            try {
                ZERO.invert(r);
                fail(r);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withScale(1).choose(
                        map(Algebraic::realValue, P.withScale(4).nonzeroAlgebraics()),
                        P.choose(
                                Arrays.asList(
                                        map(Real::leftFuzzyRepresentation, P.withScale(4).rationals()),
                                        map(Real::rightFuzzyRepresentation, P.withScale(4).rationals()),
                                        map(Real::fuzzyRepresentation, P.withScale(4).rationals())
                                )
                        )
                ),
                P.rangeDown(Rational.ZERO)
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.invert(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesDivide_int() {
        initialize("divide(int)");
        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.reals(), P.nonzeroIntegers()))) {
            Real quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.multiply(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.nonzeroReals(), P.nonzeroIntegers()))) {
            assertTrue(p, p.a.divide(p.b).eq(of(p.b).divideUnsafe(p.a).invertUnsafe(), DEFAULT_RESOLUTION)
                    .orElse(true));
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            assertTrue(i, ONE.divide(i).eq(of(i).invertUnsafe(), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(i, of(i).divide(i).eq(Rational.ONE, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.divide(1) == x);
        }

        for (Real x : take(LIMIT, P.reals())) {
            try {
                x.divide(0);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_BigInteger() {
        initialize("divide(BigInteger)");
        for (Pair<Real, BigInteger> p : take(LIMIT, P.pairs(P.reals(), P.nonzeroBigIntegers()))) {
            Real quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.multiply(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Real, BigInteger> p : take(LIMIT, P.pairs(P.nonzeroReals(), P.nonzeroBigIntegers()))) {
            assertTrue(p, p.a.divide(p.b).eq(of(p.b).divideUnsafe(p.a).invertUnsafe(), DEFAULT_RESOLUTION)
                    .orElse(true));
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            assertTrue(i, ONE.divide(i).eq(of(i).invertUnsafe(), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(i, of(i).divide(i).eq(Rational.ONE, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.divide(BigInteger.ONE) == x);
        }

        for (Real x : take(LIMIT, P.reals())) {
            try {
                x.divide(BigInteger.ZERO);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_Rational() {
        initialize("divide(Rational)");
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.nonzeroRationals()))) {
            Real quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.multiply(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.nonzeroReals(), P.nonzeroRationals()))) {
            Real quotient = p.a.divide(p.b);
            assertTrue(p, quotient.eq(of(p.b).divideUnsafe(p.a).invertUnsafe(), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, quotient.eq(p.a.multiply(p.b.invert()), DEFAULT_RESOLUTION).orElse(true));
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            assertTrue(r, ONE.divide(r).eq(of(r).invertUnsafe(), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(r, of(r).divide(r).eq(Rational.ONE, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.divide(Rational.ONE) == x);
        }

        for (Real x : take(LIMIT, P.reals())) {
            try {
                x.divide(Rational.ZERO);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivideUnsafe_Real() {
        initialize("divideUnsafe(Real)");
        for (Pair<Real, Real> p : take(LIMIT, P.pairs(P.reals(), P.nonzeroReals()))) {
            Real quotient = p.a.divideUnsafe(p.b);
            quotient.validate();
            assertTrue(p, quotient.eq(p.a.multiply(p.b.invertUnsafe()), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, quotient.multiply(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Real, Real> p : take(LIMIT, P.pairs(P.nonzeroReals()))) {
            assertTrue(p, p.a.divideUnsafe(p.b).eq(p.b.divideUnsafe(p.a).invertUnsafe(), DEFAULT_RESOLUTION)
                    .orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.divideUnsafe(ONE) == x);
        }

        for (Real x : take(LIMIT, P.nonzeroReals())) {
            assertTrue(x, ONE.divideUnsafe(x).eq(x.invertUnsafe(), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(x, x.divideUnsafe(x) == ONE);
            assertTrue(x, ZERO.divideUnsafe(x) == ZERO);
        }

        for (Real x : take(LIMIT, P.reals())) {
            try {
                x.divideUnsafe(ZERO);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_Real_Rational() {
        initialize("divide(Real, Rational)");
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(
                P.withScale(4).reals(),
                P.withScale(1).choose(
                        map(Algebraic::realValue, P.withScale(4).nonzeroAlgebraics()),
                        P.choose(
                                Arrays.asList(
                                        map(Real::leftFuzzyRepresentation, P.withScale(4).rationals()),
                                        map(Real::rightFuzzyRepresentation, P.withScale(4).rationals()),
                                        map(Real::fuzzyRepresentation, P.withScale(4).rationals())
                                )
                        )
                ),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            Optional<Real> ox = t.a.divide(t.b, t.c);
            if (ox.isPresent()) {
                ox.get().validate();
                assertTrue(t, t.a.divideUnsafe(t.b).eq(ox.get(), DEFAULT_RESOLUTION).orElse(true));
            }
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.positiveRationals()))) {
            try {
                p.a.divide(ZERO, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Triple<Real, Real, Rational>> tsFail = P.triples(
                P.withScale(4).reals(),
                P.withScale(1).choose(
                        map(Algebraic::realValue, P.withScale(4).nonzeroAlgebraics()),
                        P.choose(
                                Arrays.asList(
                                        map(Real::leftFuzzyRepresentation, P.withScale(4).rationals()),
                                        map(Real::rightFuzzyRepresentation, P.withScale(4).rationals()),
                                        map(Real::fuzzyRepresentation, P.withScale(4).rationals())
                                )
                        )
                ),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.divide(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static @NotNull Real shiftLeft_simplest(@NotNull Real x, int bits) {
        if (bits < 0) {
            return x.divide(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return x.multiply(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.reals(), P.integersGeometric()))) {
            Real shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertTrue(p, shifted.eq(shiftLeft_simplest(p.a, p.b), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, shifted.shiftRight(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, shifted.eq(p.a.shiftRight(-p.b), DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.shiftLeft(0) == x);
        }

        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.reals(), P.naturalIntegersGeometric()))) {
            assertTrue(p, p.a.shiftLeft(p.b).eq(p.a.multiply(BigInteger.ONE.shiftLeft(p.b)), DEFAULT_RESOLUTION)
                    .orElse(true));
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<Real, Integer>, Real>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        compareImplementations(
                "shiftLeft(int)",
                take(LIMIT, P.pairs(P.reals(), P.integersGeometric())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull Real shiftRight_simplest(@NotNull Real x, int bits) {
        if (bits < 0) {
            return x.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return x.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftRight() {
        initialize("shiftRight(int)");
        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.reals(), P.integersGeometric()))) {
            Real shifted = p.a.shiftRight(p.b);
            shifted.validate();
            assertTrue(p, shifted.eq(shiftRight_simplest(p.a, p.b), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, shifted.shiftLeft(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, shifted.eq(p.a.shiftLeft(-p.b), DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.shiftRight(0) == x);
        }

        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.reals(), P.naturalIntegersGeometric()))) {
            assertTrue(p, p.a.shiftRight(p.b).eq(p.a.divide(BigInteger.ONE.shiftLeft(p.b)), DEFAULT_RESOLUTION)
                    .orElse(true));
        }
    }

    private void compareImplementationsShiftRight() {
        Map<String, Function<Pair<Real, Integer>, Real>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftRight_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftRight(p.b));
        compareImplementations(
                "shiftRight(int)",
                take(LIMIT, P.pairs(P.reals(), P.integersGeometric())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull Real sum_simplest(@NotNull List<Real> xs) {
        return foldl(Real::add, ZERO, xs);
    }

    private void propertiesSum() {
        initialize("sum(List<Real>)");
        for (List<Real> xs : take(LIMIT, P.withScale(4).lists(P.reals()))) {
            Real sum = sum(xs);
            sum.validate();
            assertTrue(xs, sum.eq(sum_simplest(xs), DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, sum(Collections.singletonList(x)).eq(x, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Real, Real> p : take(LIMIT, P.pairs(P.reals()))) {
            assertTrue(p, sum(Arrays.asList(p.a, p.b)).eq(p.a.add(p.b), DEFAULT_RESOLUTION).orElse(true));
        }

        Iterable<List<Real>> xssFail = map(
                p -> toList(insert(p.a, p.b, null)),
                P.dependentPairsIdentityHash(P.withScale(4).lists(P.reals()), xs -> P.range(0, xs.size()))
        );
        for (List<Real> xs : take(LIMIT, xssFail)) {
            try {
                sum(xs);
                fail(xs);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsSum() {
        Map<String, Function<List<Real>, Real>> functions = new LinkedHashMap<>();
        functions.put("simplest", RealProperties::sum_simplest);
        functions.put("standard", Real::sum);
        Iterable<List<Real>> xss = P.lists(P.reals());
        compareImplementations("sum(Iterable<Real>)", take(SMALL_LIMIT, xss), functions, v -> P.reset());
    }

    private static @NotNull Real product_simplest(@NotNull List<Real> xs) {
        return foldl(Real::multiply, ONE, xs);
    }

    private void propertiesProduct() {
        initialize("product(List<Real>)");
        for (List<Real> xs : take(LIMIT, P.withScale(1).lists(P.reals()))) {
            Real product = product(xs);
            product.validate();
            assertTrue(xs, product.eq(product_simplest(xs), DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, product(Collections.singletonList(x)).eq(x, DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Real, Real> p : take(LIMIT, P.pairs(P.reals()))) {
            assertTrue(p, product(Arrays.asList(p.a, p.b)).eq(p.a.multiply(p.b), DEFAULT_RESOLUTION).orElse(true));
        }

        Iterable<List<Real>> xssFail = map(
                p -> toList(insert(p.a, p.b, null)),
                P.dependentPairsIdentityHash(P.withScale(4).lists(P.reals()), xs -> P.range(0, xs.size()))
        );
        for (List<Real> xs : take(LIMIT, xssFail)) {
            try {
                product(xs);
                fail(xs);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsProduct() {
        Map<String, Function<List<Real>, Real>> functions = new LinkedHashMap<>();
        functions.put("simplest", RealProperties::product_simplest);
        functions.put("standard", Real::product);
        Iterable<List<Real>> xss = P.withScale(1).lists(P.reals());
        compareImplementations("product(Iterable<Real>)", take(LIMIT, xss), functions, v -> P.reset());
    }

    private void propertiesDelta() {
        initialize("delta(Iterable<Real>)");
        for (List<Real> xs : take(LIMIT, P.listsAtLeast(1, P.reals()))) {
            Iterable<Real> deltas = delta(xs);
            List<Real> deltaList = toList(deltas);
            deltaList.forEach(Real::validate);
            aeq(xs, deltaList.size(), length(xs) - 1);
            List<Real> reversed = reverse(map(Real::negate, delta(reverse(xs))));
            for (int i = 0; i < deltaList.size(); i++) {
                assertTrue(xs, deltaList.get(i).eq(reversed.get(i), DEFAULT_RESOLUTION).orElse(true));
            }
            testNoRemove(TINY_LIMIT, deltas);
            testHasNext(deltas);
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, isEmpty(delta(Collections.singletonList(x))));
        }

        for (Pair<Real, Real> p : take(LIMIT, P.pairs(P.reals()))) {
            List<Real> deltas = toList(delta(Pair.toList(p)));
            assertEquals(p, deltas.size(), 1);
            assertTrue(p, deltas.get(0).eq(p.b.subtract(p.a), DEFAULT_RESOLUTION).orElse(true));
        }

        for (Iterable<Real> xs : take(SMALL_LIMIT, P.prefixPermutations(QBarTesting.QEP.reals()))) {
            Iterable<Real> deltas = delta(xs);
            List<Real> deltaPrefix = toList(take(TINY_LIMIT, deltas));
            deltaPrefix.forEach(Real::validate);
            aeq(IterableUtils.toString(TINY_LIMIT, xs), length(deltaPrefix), TINY_LIMIT);
            testNoRemove(TINY_LIMIT, deltas);
        }

        Iterable<List<Real>> xssFail = map(
                p -> toList(insert(p.a, p.b, null)),
                P.dependentPairsIdentityHash(P.withScale(4).lists(P.reals()), xs -> P.range(0, xs.size()))
        );
        for (List<Real> xs : take(LIMIT, xssFail)) {
            try {
                toList(delta(xs));
                fail(xs);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPowUnsafe_int() {
        initialize("powUnsafe(int)");
        Iterable<Pair<Rational, Integer>> rs = filterInfinite(
                p -> p.a != Rational.ZERO || p.b >= 0,
                P.pairsSquareRootOrder(P.rationals(), P.withScale(4).integersGeometric())
        );
        Iterable<Pair<Real, Integer>> ps = P.withScale(1).choose(
                map(
                        q -> new Pair<>(q.a.realValue(), q.b),
                        filterInfinite(
                                p -> p.a != Algebraic.ZERO || p.b >= 0,
                                P.pairsSquareRootOrder(P.algebraics(), P.withScale(4).integersGeometric())
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
            Real x = p.a.powUnsafe(p.b);
            x.validate();
        }

        Iterable<Pair<Rational, Integer>> nonzeroRs = filterInfinite(
                p -> p.a != Rational.ZERO || p.b >= 0,
                P.pairsSquareRootOrder(P.withScale(4).rationals(), P.withScale(2).nonzeroIntegersGeometric())
        );
        ps = P.withScale(1).choose(
                map(
                        q -> new Pair<>(q.a.realValue(), q.b),
                        filterInfinite(
                                p -> p.a != Algebraic.ZERO || p.b >= 0,
                                P.pairsSquareRootOrder(
                                        P.withScale(4).algebraics(),
                                        P.withScale(2).nonzeroIntegersGeometric()
                                )
                        )
                ),
                P.choose(
                        Arrays.asList(
                                map(p -> new Pair<>(leftFuzzyRepresentation(p.a), p.b), nonzeroRs),
                                map(p -> new Pair<>(rightFuzzyRepresentation(p.a), p.b), nonzeroRs),
                                map(p -> new Pair<>(fuzzyRepresentation(p.a), p.b), nonzeroRs)
                        )
                )
        );
        for (Pair<Real, Integer> p : take(SMALL_LIMIT, ps)) {
            Real x = p.a.powUnsafe(p.b);
            assertTrue(p, x.rootUnsafe(p.b).eq(((p.b & 1) == 0 ? p.a.abs() : p.a), DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Real, Integer> p : take(LIMIT, P.pairs(P.nonzeroReals(), P.withScale(4).integersGeometric()))) {
            Real x = p.a.powUnsafe(p.b);
            assertTrue(p, p.a.powUnsafe(-p.b).eq(x.invertUnsafe(), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(p, p.a.invertUnsafe().powUnsafe(-p.b).eq(x, DEFAULT_RESOLUTION).orElse(true));
        }

        for (int i : take(LIMIT, P.positiveIntegersGeometric())) {
            assertTrue(i, ZERO.powUnsafe(i) == ZERO);
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.powUnsafe(0) == ONE);
            assertTrue(x, x.powUnsafe(1) == x);
            assertTrue(x, x.powUnsafe(2).eq(x.multiply(x), DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.nonzeroReals())) {
            assertTrue(x, x.powUnsafe(-1).eq(x.invertUnsafe(), DEFAULT_RESOLUTION).orElse(true));
        }

        Iterable<Triple<Real, Integer, Integer>> ts = filterInfinite(
                t -> !t.a.isExact() || t.a.rationalValueExact().get() != Rational.ZERO || (t.b >= 0 && t.c >= 0),
                P.triples(
                        P.withScale(4).cleanReals(),
                        P.withScale(4).integersGeometric(),
                        P.withScale(4).integersGeometric()
                )
        );
        for (Triple<Real, Integer, Integer> t : take(SMALL_LIMIT, ts)) {
            Real expression1 = t.a.powUnsafe(t.b).multiply(t.a.powUnsafe(t.c));
            Real expression2 = t.a.powUnsafe(t.b + t.c);
            assertTrue(t, expression1.eq(expression2, DEFAULT_RESOLUTION).orElse(true));
            Real expression3 = t.a.powUnsafe(t.b).powUnsafe(t.c);
            Real expression4 = t.a.powUnsafe(t.b * t.c);
            assertTrue(t, expression3.eq(expression4, DEFAULT_RESOLUTION).orElse(true));
        }

        ts = filterInfinite(
                t -> !t.a.isExact() || t.a.rationalValueExact().get() != Rational.ZERO || (t.c == 0 && t.b >= 0),
                P.triples(
                        P.withScale(4).cleanReals(),
                        P.withScale(4).integersGeometric(),
                        P.withScale(4).integersGeometric()
                )
        );
        for (Triple<Real, Integer, Integer> t : take(SMALL_LIMIT, ts)) {
            Real expression1 = t.a.powUnsafe(t.b).divideUnsafe(t.a.powUnsafe(t.c));
            Real expression2 = t.a.powUnsafe(t.b - t.c);
            assertTrue(t, expression1.eq(expression2, DEFAULT_RESOLUTION).orElse(true));
        }

        Iterable<Triple<Real, Real, Integer>> ts2 = filter(
                t -> ((!t.a.isExact() || t.a.rationalValueExact().get() != Rational.ZERO) &&
                        (!t.b.isExact() || t.b.rationalValueExact().get() != Rational.ZERO))
                        || t.c >= 0,
                P.triples(
                        P.withScale(4).cleanReals(),
                        P.withScale(4).cleanReals(),
                        P.withScale(4).positiveIntegersGeometric()
                )
        );
        for (Triple<Real, Real, Integer> t : take(TINY_LIMIT, ts2)) {
            Real expression1 = t.a.multiply(t.b).powUnsafe(t.c);
            Real expression2 = t.a.powUnsafe(t.c).multiply(t.b.powUnsafe(t.c));
            assertTrue(t, expression1.eq(expression2, DEFAULT_RESOLUTION).orElse(true));
        }

        ts2 = filterInfinite(
                t -> !t.a.isExact() || t.a.rationalValueExact().get() != Rational.ZERO || t.c >= 0,
                P.triples(
                        P.withScale(4).cleanReals(),
                        P.withScale(4).nonzeroCleanReals(),
                        P.withScale(4).positiveIntegersGeometric()
                )
        );
        for (Triple<Real, Real, Integer> t : take(TINY_LIMIT, ts2)) {
            Real expression1 = t.a.divideUnsafe(t.b).powUnsafe(t.c);
            Real expression2 = t.a.powUnsafe(t.c).divideUnsafe(t.b.powUnsafe(t.c));
            assertTrue(t, expression1.eq(expression2, DEFAULT_RESOLUTION).orElse(true));
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                ZERO.powUnsafe(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesPow_int_Rational() {
        initialize("pow(Real, Rational)");
        Iterable<Triple<Real, Integer, Rational>> ts = filterInfinite(
                t -> (!t.a.isExact() || t.a.rationalValueExact().get() != Rational.ZERO) || t.b >= 0,
                P.triples(P.withScale(4).reals(), P.withScale(4).integersGeometric(), P.positiveRationals())
        );
        for (Triple<Real, Integer, Rational> t : take(LIMIT, ts)) {
            Optional<Real> ox = t.a.pow(t.b, t.c);
            if (ox.isPresent()) {
                ox.get().validate();
                assertTrue(t, t.a.powUnsafe(t.b).eq(ox.get(), DEFAULT_RESOLUTION).orElse(true));
            }
        }

        for (Pair<Integer, Rational> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.positiveRationals()))) {
            try {
                ZERO.pow(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Triple<Real, Integer, Rational>> tsFail = filterInfinite(
                t -> (!t.a.isExact() || t.a.rationalValueExact().get() != Rational.ZERO) || t.b >= 0,
                P.triples(P.withScale(4).reals(), P.withScale(4).integersGeometric(), P.rangeDown(Rational.ZERO))
        );
        for (Triple<Real, Integer, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.pow(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesRootOfRational() {
        initialize("rootOfRational(Rational, int)");
        Iterable<Pair<Rational, Integer>> ps = filterInfinite(
                p -> (p.a != Rational.ZERO || p.b >= 0) && ((p.b & 1) != 0 || p.a.signum() != -1),
                P.pairsSquareRootOrder(P.rationals(), P.withScale(2).nonzeroIntegersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            Real x = rootOfRational(p.a, p.b);
            x.validate();
            assertTrue(p, x.powUnsafe(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
            if ((p.b & 1) == 0) {
                assertNotEquals(p, x.signumUnsafe(), -1);
            }
        }

        ps = filterInfinite(
                p -> (p.b & 1) != 0 || p.a.signum() != -1,
                P.pairsSquareRootOrder(P.nonzeroRationals(), P.nonzeroIntegersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, rootOfRational(p.a, p.b).eq(rootOfRational(p.a, -p.b).invertUnsafe(), DEFAULT_RESOLUTION)
                    .orElse(true));
            assertTrue(p, rootOfRational(p.a, p.b).eq(rootOfRational(p.a.invert(), -p.b), DEFAULT_RESOLUTION)
                    .orElse(true));
        }

        for (int i : take(LIMIT, P.positiveIntegersGeometric())) {
            assertTrue(i, rootOfRational(Rational.ZERO, i) == ZERO);
        }

        for (Rational x : take(LIMIT, P.rationals())) {
            assertEquals(x, rootOfRational(x, 1).rationalValueExact().get(), x);
        }

        for (Rational x : take(LIMIT, P.nonzeroRationals())) {
            assertEquals(x, rootOfRational(x, -1).rationalValueExact().get(), x.invert());
        }

        for (Rational x : take(LIMIT, P.rationals())) {
            try {
                rootOfRational(x, 0);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                rootOfRational(Rational.ZERO, i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Rational, Integer>> psFail = P.pairs(
                P.negativeRationals(),
                map(i -> i * 2, P.integersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, psFail)) {
            try {
                rootOfRational(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesSqrtOfRational() {
        initialize("sqrtOfRational(Rational)");
        for (Rational x : take(LIMIT, P.rangeUp(Rational.ZERO))) {
            Real y = sqrtOfRational(x);
            x.validate();
            assertTrue(x, y.powUnsafe(2).eq(x, DEFAULT_RESOLUTION).orElse(true));
            assertNotEquals(x, y.signumUnsafe(), -1);
        }

        for (Rational x : take(LIMIT, P.negativeRationals())) {
            try {
                sqrtOfRational(x);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesCbrtOfRational() {
        initialize("cbrtOfRational(Rational)");
        for (Rational x : take(LIMIT, P.rangeUp(Rational.ZERO))) {
            Real y = cbrtOfRational(x);
            x.validate();
            assertTrue(x, y.powUnsafe(3).eq(x, DEFAULT_RESOLUTION).orElse(true));
            assertEquals(x, y.signumUnsafe(), x.signum());
        }
    }

    private void propertiesRootUnsafe() {
        initialize("rootUnsafe(int)");
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
                P.pairsSquareRootOrder(P.withScale(4).reals(), P.withScale(4).nonzeroIntegersGeometric())
        );
        for (Pair<Real, Integer> p : take(MEDIUM_LIMIT, ps)) {
            Real x = p.a.rootUnsafe(p.b);
            x.validate();
            assertTrue(p, x.powUnsafe(p.b).eq(p.a, DEFAULT_RESOLUTION).orElse(true));
            if ((p.b & 1) == 0) {
                assertTrue(p, x.geUnsafe(Rational.ZERO));
            }
        }

        ps = filterInfinite(
                p -> {
                    int signum = p.a.signumUnsafe();
                    return (p.b >= 0 || signum != 0) && ((p.b & 1) != 0 || signum != -1);
                },
                P.pairsSquareRootOrder(P.withScale(4).nonzeroCleanReals(), P.withScale(4).nonzeroIntegersGeometric())
        );
        for (Pair<Real, Integer> p : take(MEDIUM_LIMIT, ps)) {
            assertTrue(p, p.a.rootUnsafe(p.b).eq(p.a.rootUnsafe(-p.b).invertUnsafe(), DEFAULT_RESOLUTION)
                    .orElse(true));
            assertTrue(p, p.a.rootUnsafe(p.b).eq(p.a.invertUnsafe().rootUnsafe(-p.b), DEFAULT_RESOLUTION)
                    .orElse(true));
        }

        for (int i : take(LIMIT, P.positiveIntegersGeometric())) {
            assertTrue(i, ZERO.rootUnsafe(i) == ZERO);
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.rootUnsafe(1) == x);
        }

        for (Real x : take(LIMIT, P.nonzeroCleanReals())) {
            assertTrue(x, x.rootUnsafe(-1).eq(x.invertUnsafe(), DEFAULT_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.reals())) {
            try {
                x.rootUnsafe(0);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                ZERO.rootUnsafe(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Real, Integer>> psFail = P.pairs(
                P.negativeCleanReals(),
                map(i -> i << 1, P.integersGeometric())
        );
        for (Pair<Real, Integer> p : take(LIMIT, psFail)) {
            try {
                //noinspection ResultOfMethodCallIgnored
                IterableUtils.toString(p.a.rootUnsafe(p.b));
                fail(p);
            } catch (ArithmeticException ignored) {}
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

    private void propertiesRoot() {
        initialize("root(int, Rational)");
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
                                P.withScale(4).nonzeroIntegersGeometric(),
                                P.positiveRationals()
                        )
                )
        );
        for (Triple<Real, Integer, Rational> t : take(MEDIUM_LIMIT, ts)) {
            Optional<Real> ox = t.a.root(t.b, t.c);
            if (ox.isPresent()) {
                ox.get().validate();
                assertTrue(t, ox.get().eq(t.a.rootUnsafe(t.b), DEFAULT_RESOLUTION).orElse(true));
            }
        }

        for (Pair<Integer, Rational> p : take(LIMIT, P.pairs(P.negativeIntegersGeometric(), P.positiveRationals()))) {
            try {
                ZERO.root(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Triple<Real, Integer, Rational>> tsFail = P.triples(
                P.reals(),
                P.integersGeometric(),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, Integer, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.root(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesSqrtUnsafe() {
        initialize("sqrtUnsafe()");
        Iterable<Real> xs = filterInfinite(
                x -> x.ge(Rational.ZERO, DEFAULT_RESOLUTION).orElse(false),
                P.withScale(4).reals()
        );
        for (Real x : take(MEDIUM_LIMIT, xs)) {
            Real y = x.sqrtUnsafe();
            y.validate();
            assertTrue(x, y.powUnsafe(2).eq(x, DEFAULT_RESOLUTION).orElse(true));
            assertTrue(x, y.geUnsafe(Rational.ZERO));
        }

        for (Real x : take(LIMIT, P.negativeReals())) {
            try {
                //noinspection ResultOfMethodCallIgnored
                x.sqrtUnsafe().toString();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesSqrt() {
        initialize("sqrt()");
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
        for (Pair<Real, Rational> p : take(MEDIUM_LIMIT, ps)) {
            Optional<Real> ox = p.a.sqrt(p.b);
            if (ox.isPresent()) {
                ox.get().validate();
                assertTrue(p, ox.get().eq(p.a.sqrtUnsafe(), DEFAULT_RESOLUTION).orElse(true));
            }
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.rangeDown(Rational.ZERO)))) {
            try {
                p.a.sqrt(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesCbrt() {
        initialize("cbrt()");
        for (Real x : take(MEDIUM_LIMIT, P.withScale(4).reals())) {
            Real y = x.cbrt();
            y.validate();
            assertTrue(x, y.powUnsafe(3).eq(x, DEFAULT_RESOLUTION).orElse(true));
        }
    }

    private void propertiesExpOfRational() {
        initialize("expOfRational(Rational)");
        BigInteger limit = BigInteger.valueOf(SMALL_LIMIT);
        Iterable<Rational> xs = filterInfinite(
                x -> Ordering.le(x.bigIntegerValue().abs(), limit),
                P.withScale(4).rationals()
        );
        for (Rational x : take(LIMIT, xs)) {
            Real y = expOfRational(x);
            y.validate();
            assertEquals(x, y.signumUnsafe(), 1);
        }

        xs = filterInfinite(r -> Ordering.le(r.abs(), Rational.TEN), P.withScale(4).rationals());
        for (Rational x : take(SMALL_LIMIT, xs)) {
            assertTrue(x, expOfRational(x).logUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesExp() {
        initialize("exp()");
        BigInteger limit = BigInteger.valueOf(SMALL_LIMIT);
        Iterable<Real> xs = filterInfinite(
                x -> {
                    Optional<BigInteger> i = x.bigIntegerValue(DEFAULT_RESOLUTION);
                    return i.isPresent() && Ordering.le(i.get().abs(), limit);
                },
                P.withScale(4).reals()
        );
        for (Real x : take(TINY_LIMIT, xs)) {
            Real y = x.exp();
            y.validate();
            assertEquals(x, y.signumUnsafe(), 1);
            assertTrue(x, y.logUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesLogOfRational_Rational() {
        initialize("logOfRational(Rational)");
        for (Rational x : take(LIMIT, P.withScale(4).positiveRationals())) {
            Real y = logOfRational(x);
            y.validate();
        }

        for (Rational x : take(SMALL_LIMIT, P.withScale(4).positiveRationals())) {
            assertTrue(x, logOfRational(x).exp().eq(x, SMALL_RESOLUTION).orElse(true));
        }

        for (Rational x : take(LIMIT, P.rangeDown(Rational.ZERO))) {
            try {
                logOfRational(x);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesLogUnsafe() {
        initialize("logUnsafe()");
        for (Real x : take(TINY_LIMIT, P.withScale(4).positiveReals())) {
            Real y = x.logUnsafe();
            y.validate();
            assertTrue(x, y.exp().eq(x, SMALL_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.negativeReals())) {
            try {
                toList(x.logUnsafe());
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesLog_Rational() {
        initialize("log(Rational)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                P.withElement(rightFuzzyRepresentation(Rational.ZERO), P.withScale(4).positiveReals()),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(TINY_LIMIT, ps)) {
            Optional<Real> ox = p.a.log(p.b);
            if (ox.isPresent()) {
                Real x = ox.get();
                x.validate();
                assertTrue(p, x.eq(p.a.logUnsafe(), SMALL_RESOLUTION).orElse(true));
            }
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.negativeReals(), P.positiveRationals()))) {
            try {
                Optional<Real> ox = p.a.log(p.b);
                ox.ifPresent(IterableUtils::toList);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withElement(rightFuzzyRepresentation(Rational.ZERO), P.withScale(4).positiveReals()),
                P.rangeDown(Rational.ZERO)
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.log(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesLogOfRational_Rational_Rational() {
        initialize("logOfRational(Rational, Rational)");
        Iterable<Pair<Rational, Rational>> ps = P.pairs(
                P.positiveRationals(),
                filterInfinite(r -> r != Rational.ONE, P.positiveRationals())
        );
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            Real x = logOfRational(p.a, p.b);
            x.validate();
        }

        ps = P.pairs(
                P.withScale(4).positiveRationals(),
                filterInfinite(r -> r != Rational.ONE, P.withScale(4).positiveRationals())
        );
        for (Pair<Rational, Rational> p : take(TINY_LIMIT, ps)) {
            Real x = logOfRational(p.a, p.b);
            assertTrue(p, of(p.b).powUnsafe(x).eq(p.a, SMALL_RESOLUTION).orElse(true));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rangeDown(Rational.ZERO), P.positiveRationals()))) {
            try {
                logOfRational(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.positiveRationals(), P.rangeDown(Rational.ZERO)))) {
            try {
                logOfRational(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Rational r : take(LIMIT, P.positiveRationals())) {
            try {
                logOfRational(r, Rational.ONE);
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesLogUnsafe_Rational() {
        initialize("logUnsafe(Rational)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                P.withScale(4).positiveReals(),
                filterInfinite(r -> r != Rational.ONE, P.withScale(4).positiveRationals())
        );
        for (Pair<Real, Rational> p : take(TINY_LIMIT, ps)) {
            Real x = p.a.logUnsafe(p.b);
            x.validate();
            assertTrue(p, of(p.b).powUnsafe(x).eq(p.a, SMALL_RESOLUTION).orElse(true));
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.negativeReals(), P.positiveRationals()))) {
            try {
                toList(p.a.logUnsafe(p.b));
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.positiveReals(), P.rangeDown(Rational.ZERO)))) {
            try {
                p.a.logUnsafe(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Real x : take(LIMIT, P.positiveReals())) {
            try {
                x.logUnsafe(Rational.ONE);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesLog_Rational_Rational() {
        initialize("log(Rational, Rational)");
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(
                P.withElement(rightFuzzyRepresentation(Rational.ZERO), P.withScale(4).positiveReals()),
                filterInfinite(r -> r != Rational.ONE, P.withScale(4).positiveRationals()),
                P.positiveRationals()
        );
        for (Triple<Real, Rational, Rational> t : take(TINY_LIMIT, ts)) {
            Optional<Real> ox = t.a.log(t.b, t.c);
            if (ox.isPresent()) {
                Real x = ox.get();
                x.validate();
                assertTrue(t, x.eq(t.a.logUnsafe(t.b), SMALL_RESOLUTION).orElse(true));
            }
        }

        Iterable<Triple<Real, Rational, Rational>> tsFail = P.triples(
                P.withElement(rightFuzzyRepresentation(Rational.ZERO), P.withScale(4).positiveReals()),
                P.rangeDown(Rational.ZERO),
                P.positiveRationals()
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.log(t.b, t.c);
            } catch (ArithmeticException ignored) {}
        }

        tsFail = P.triples(
                P.negativeReals(),
                filterInfinite(r -> r != Rational.ONE, P.withScale(4).positiveRationals()),
                P.positiveRationals()
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                Optional<Real> ox = t.a.log(t.b, t.c);
                ox.ifPresent(IterableUtils::toList);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withElement(rightFuzzyRepresentation(Rational.ZERO), P.withScale(4).positiveReals()),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.log(Rational.ONE, p.b);
            } catch (ArithmeticException ignored) {}
        }

        tsFail = P.triples(
                P.withElement(rightFuzzyRepresentation(Rational.ZERO), P.withScale(4).positiveReals()),
                filterInfinite(r -> r != Rational.ONE, P.withScale(4).positiveRationals()),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.log(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesLogUnsafe_Real() {
        initialize("logUnsafe(Real)");
        Iterable<Pair<Real, Real>> ps = P.pairs(
                P.withScale(4).positiveReals(),
                filterInfinite(
                        x -> x.ne(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                        P.withScale(4).positiveReals()
                )
        );
        for (Pair<Real, Real> p : take(TINY_LIMIT, ps)) {
            Real x = p.a.logUnsafe(p.b);
            x.validate();
            assertTrue(p, p.b.powUnsafe(x).eq(p.a, SMALL_RESOLUTION).orElse(true));
        }

        Iterable<Pair<Real, Real>> psFail = P.pairs(
                P.withScale(4).negativeReals(),
                filterInfinite(
                        x -> x.ne(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                        P.withScale(4).positiveReals()
                )
        );
        for (Pair<Real, Real> p : take(LIMIT, psFail)) {
            try {
                toList(p.a.logUnsafe(p.b));
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Pair<Real, Real> p : take(LIMIT, P.pairs(P.positiveReals(), P.negativeReals()))) {
            try {
                p.a.logUnsafe(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Real x : take(LIMIT, P.positiveReals())) {
            try {
                x.logUnsafe(Real.ONE);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesLog_Real_Rational() {
        initialize("log(Real, Rational)");
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
            Optional<Real> ox = t.a.log(t.b, t.c);
            if (ox.isPresent()) {
                Real x = ox.get();
                x.validate();
                assertTrue(t, x.eq(t.a.logUnsafe(t.b), SMALL_RESOLUTION).orElse(true));
            }
        }

        Iterable<Triple<Real, Real, Rational>> tsFail = P.triples(
                P.withElement(rightFuzzyRepresentation(Rational.ZERO), P.withScale(4).positiveReals()),
                P.negativeReals(),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.log(t.b, t.c);
            } catch (ArithmeticException ignored) {}
        }

        tsFail = P.triples(
                P.negativeReals(),
                P.withElement(
                        rightFuzzyRepresentation(Rational.ZERO),
                        filterInfinite(
                                x -> x.ne(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                                P.withScale(4).positiveReals()
                        )
                ),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                Optional<Real> ox = t.a.log(t.b, t.c);
                ox.ifPresent(IterableUtils::toList);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withElement(rightFuzzyRepresentation(Rational.ZERO), P.withScale(4).positiveReals()),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.log(Rational.ONE, p.b);
            } catch (ArithmeticException ignored) {}
        }

        tsFail = P.triples(
                P.withElement(rightFuzzyRepresentation(Rational.ZERO), P.withScale(4).positiveReals()),
                P.withElement(
                        rightFuzzyRepresentation(Rational.ZERO),
                        filterInfinite(
                                x -> x.ne(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                                P.withScale(4).positiveReals()
                        )
                ),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.log(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesPowUnsafe_Real() {
        initialize("powUnsafe(Real)");
        BigInteger lower = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger upper = BigInteger.valueOf(Integer.MAX_VALUE);
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
            p.a.powUnsafe(p.b).validate();
        }

        ps = filterInfinite(
                p -> {
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
                P.pairs(
                        P.withScale(4).withSecondaryScale(4).nonzeroReals(),
                        filterInfinite(
                                x -> x.abs().lt(Rational.TEN, DEFAULT_RESOLUTION).orElse(true),
                                P.withScale(3).withSecondaryScale(4).reals()
                        )
                )
        );
        for (Pair<Real, Real> p : take(TINY_LIMIT, ps)) {
            Real x = p.a.powUnsafe(p.b);
            assertTrue(p, p.a.powUnsafe(p.b.negate()).eq(x.invertUnsafe(), SMALL_RESOLUTION).orElse(true));
            assertTrue(p, p.a.invertUnsafe().powUnsafe(p.b.negate()).eq(x, SMALL_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.positiveReals())) {
            assertTrue(x, ZERO.powUnsafe(x) == ZERO);
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.powUnsafe(ZERO) == ONE);
            assertTrue(x, x.powUnsafe(ONE) == x);
            assertTrue(x, x.powUnsafe(TWO).eq(x.multiply(x), SMALL_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.nonzeroReals())) {
            assertTrue(x, x.powUnsafe(NEGATIVE_ONE).eq(x.invertUnsafe(), SMALL_RESOLUTION).orElse(true));
        }

        for (Real x : take(LIMIT, P.negativeReals())) {
            try {
                ZERO.powUnsafe(x);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Real, Real>> psFail = P.pairs(P.negativeReals(), filterInfinite(x -> !x.isExact(), P.reals()));
        for (Pair<Real, Real> p : take(LIMIT, psFail)) {
            try {
                p.a.powUnsafe(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        psFail = P.pairs(
                P.negativeReals(),
                map(Real::of, filterInfinite(x -> !x.getDenominator().testBit(0), P.withScale(3).rationals()))
        );
        for (Pair<Real, Real> p : take(LIMIT, psFail)) {
            try {
                p.a.powUnsafe(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Rational> rsFail = filterInfinite(
                r -> lt(r.getNumerator(), lower) || gt(r.getNumerator(), upper) || gt(r.getDenominator(), upper),
                map(
                        p -> Rational.of(p.a, p.b),
                        P.choose(
                                P.pairs(
                                        P.withScale(1).choose(
                                                P.withScale(33).rangeDown(lower.subtract(BigInteger.ONE)),
                                                P.withScale(33).rangeUp(upper.add(BigInteger.ONE))
                                        ),
                                        P.positiveBigIntegers()
                                ),
                                P.pairs(P.bigIntegers(), P.withScale(33).rangeUp(upper.add(BigInteger.ONE)))
                        )
                )
        );
        psFail = P.pairs(filterInfinite(x -> !x.isExact(), P.positiveReals()), map(Real::of, rsFail));
        for (Pair<Real, Real> p : take(LIMIT, psFail)) {
            try {
                p.a.powUnsafe(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesPow_Real_Rational() {
        initialize("pow(Real, Rational)");
        BigInteger lower = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger upper = BigInteger.valueOf(Integer.MAX_VALUE);
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
                        filterInfinite(
                                x -> x.abs().lt(Rational.TEN, DEFAULT_RESOLUTION).orElse(true),
                                P.withScale(3).withSecondaryScale(4).reals()
                        ),
                        filterInfinite(
                                x -> x.abs().lt(Rational.TEN, DEFAULT_RESOLUTION).orElse(true),
                                P.withScale(3).withSecondaryScale(4).reals()
                        ),
                        P.positiveRationals()
                )
        );
        for (Triple<Real, Real, Rational> t : take(TINY_LIMIT, ts)) {
            Optional<Real> ox = t.a.pow(t.b, t.c);
            if (ox.isPresent()) {
                Real x = ox.get();
                x.validate();
                assertTrue(t, x.eq(t.a.powUnsafe(t.b), SMALL_RESOLUTION).orElse(true));
            }
        }

        Iterable<Triple<Real, Real, Rational>> tsFail = P.triples(
                P.negativeReals(),
                map(Real::of, filterInfinite(x -> !x.getDenominator().testBit(0), P.withScale(3).rationals())),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.powUnsafe(t.b);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Rational> rsFail = filterInfinite(
                r -> lt(r.getNumerator(), lower) || gt(r.getNumerator(), upper) || gt(r.getDenominator(), upper),
                map(
                        p -> Rational.of(p.a, p.b),
                        P.choose(
                                P.pairs(
                                        P.withScale(1).choose(
                                                P.withScale(33).rangeDown(lower.subtract(BigInteger.ONE)),
                                                P.withScale(33).rangeUp(upper.add(BigInteger.ONE))
                                        ),
                                        P.positiveBigIntegers()
                                ),
                                P.pairs(P.bigIntegers(), P.withScale(33).rangeUp(upper.add(BigInteger.ONE)))
                        )
                )
        );
        tsFail = P.triples(
                filterInfinite(x -> !x.isExact(), P.positiveReals()),
                map(Real::of, rsFail),
                P.positiveRationals()
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.pow(t.b, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }

        tsFail = filterInfinite(
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
                        P.rangeDown(Rational.ZERO)
                )
        );
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.pow(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesSinOfRational() {
        initialize("sinOfRational(Rational)");
        Rational max = Rational.of(100);
        Iterable<Rational> xs = filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).rationals());
        for (Rational x : take(LIMIT, xs)) {
            Real y = sinOfRational(x);
            y.validate();
            assertTrue(x, y.leUnsafe(Rational.ONE));
            assertTrue(x, y.geUnsafe(Rational.NEGATIVE_ONE));
            assertTrue(x, x == Rational.ZERO || !y.isExact());
            assertTrue(x, of(x).sin().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        for (Rational x : take(TINY_LIMIT, xs)) {
            Real y = sinOfRational(x);
            assertTrue(x, sinOfRational(x.negate()).eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftRight(1).subtract(x).cos().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftLeft(1).add(x).sin().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(
                    x,
                    y.powUnsafe(2).add(cosOfRational(x).powUnsafe(2)).eq(Rational.ONE, SMALL_RESOLUTION).orElse(true)
            );
        }

        xs = filterInfinite(y -> PI.shiftRight(1).gtUnsafe(y.abs()), P.range(Rational.TWO.negate(), Rational.TWO));
        for (Rational x : take(TINY_LIMIT, xs)) {
            assertTrue(x, sinOfRational(x).arcsinUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesCosOfRational() {
        initialize("cosOfRational(Rational)");
        Rational max = Rational.of(100);
        Iterable<Rational> xs = filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).rationals());
        for (Rational x : take(LIMIT, xs)) {
            Real y = cosOfRational(x);
            y.validate();
            assertTrue(x, y.leUnsafe(Rational.ONE));
            assertTrue(x, y.geUnsafe(Rational.NEGATIVE_ONE));
            assertTrue(x, x == Rational.ZERO || !y.isExact());
            assertTrue(x, of(x).cos().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        for (Rational x : take(TINY_LIMIT, xs)) {
            Real y = cosOfRational(x);
            assertTrue(x, cosOfRational(x.negate()).eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftRight(1).subtract(x).sin().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftLeft(1).add(x).cos().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        xs = filterInfinite(PI::gtUnsafe, P.range(Rational.ZERO, Rational.of(4)));
        for (Rational x : take(TINY_LIMIT, xs)) {
            assertTrue(x, cosOfRational(x).arccosUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesSin() {
        initialize("sin()");
        Rational max = Rational.of(100);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                P.withScale(4).reals()
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            Real y = x.sin();
            y.validate();
            assertTrue(x, y.leUnsafe(Rational.ONE));
            assertTrue(x, y.geUnsafe(Rational.NEGATIVE_ONE));
            assertTrue(x, x.isExact() && x.rationalValueExact().get() == Rational.ZERO || !y.isExact());
        }

        for (Real x : take(TINY_LIMIT, xs)) {
            Real y = x.sin();
            assertTrue(x, x.negate().sin().eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftRight(1).subtract(x).cos().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, x.add(PI.shiftLeft(1)).sin().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(
                    x,
                    y.powUnsafe(2).add(x.cos().powUnsafe(2)).eq(Rational.ONE, SMALL_RESOLUTION).orElse(true)
            );
        }

        xs = filterInfinite(
                y -> y.abs().ltUnsafe(PI.shiftRight(1)),
                P.realRange(Algebraic.TWO.negate(), Algebraic.TWO)
        );
        for (Real x : take(TINY_LIMIT, xs)) {
            assertTrue(x, x.sin().arcsinUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesCos() {
        initialize("cos()");
        Rational max = Rational.of(100);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                P.withScale(4).reals()
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            Real y = x.cos();
            y.validate();
            assertTrue(x, y.leUnsafe(Rational.ONE));
            assertTrue(x, y.geUnsafe(Rational.NEGATIVE_ONE));
            assertTrue(x, x.isExact() && x.rationalValueExact().get() == Rational.ZERO || !y.isExact());
        }

        for (Real x : take(TINY_LIMIT, xs)) {
            Real y = x.cos();
            assertTrue(x, x.negate().cos().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftRight(1).subtract(x).sin().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, x.add(PI.shiftLeft(1)).cos().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        xs = filterInfinite(y -> y.ltUnsafe(PI), P.realRange(Algebraic.ZERO, Algebraic.of(4)));
        for (Real x : take(TINY_LIMIT, xs)) {
            assertTrue(x, x.cos().arccosUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesTanOfRational() {
        initialize("tanOfRational(Rational)");
        Rational max = Rational.of(100);
        Iterable<Rational> xs = filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).rationals());
        for (Rational x : take(LIMIT, xs)) {
            Real y = tanOfRational(x);
            y.validate();
            assertTrue(x, x == Rational.ZERO || !y.isExact());
            assertTrue(x, of(x).tanUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        for (Rational x : take(TINY_LIMIT, xs)) {
            Real y = tanOfRational(x);
            assertTrue(x, tanOfRational(x.negate()).eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftRight(1).subtract(x).cotUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftLeft(1).add(x).tanUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        xs = filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).nonzeroRationals());
        for (Rational x : take(TINY_LIMIT, xs)) {
            assertTrue(x, tanOfRational(x).eq(cotOfRational(x).invertUnsafe(), DEFAULT_RESOLUTION).orElse(true));
        }

        xs = filterInfinite(y -> PI.shiftRight(1).gtUnsafe(y.abs()), P.range(Rational.TWO.negate(), Rational.TWO));
        for (Rational x : take(TINY_LIMIT, xs)) {
            assertTrue(x, tanOfRational(x).arctan().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesCotOfRational() {
        initialize("cotOfRational(Rational)");
        Rational max = Rational.of(100);
        Iterable<Rational> xs = filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).nonzeroRationals());
        for (Rational x : take(LIMIT, xs)) {
            Real y = cotOfRational(x);
            y.validate();
            assertFalse(x, y.isExact());
            assertTrue(x, of(x).cotUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        for (Rational x : take(TINY_LIMIT, xs)) {
            Real y = cotOfRational(x);
            assertTrue(x, cotOfRational(x.negate()).eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftRight(1).subtract(x).tanUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.eq(tanOfRational(x).invertUnsafe(), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftLeft(1).add(x).cotUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        xs = filterInfinite(
                y -> y != Rational.ZERO && PI.gtUnsafe(y),
                P.range(Rational.ZERO, Rational.ONE.shiftLeft(2))
        );
        for (Rational x : take(TINY_LIMIT, xs)) {
            assertTrue(x, cotOfRational(x).arccot().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesTanUnsafe() {
        initialize("tanUnsafe()");
        Rational max = Rational.of(100);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                P.withScale(4).reals()
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            Real y = x.tanUnsafe();
            y.validate();
            assertTrue(x, x.isExact() && x.rationalValueExact().get() == Rational.ZERO || !y.isExact());
        }

        for (Real x : take(TINY_LIMIT, xs)) {
            Real y = x.tanUnsafe();
            assertTrue(x, x.negate().tanUnsafe().eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftRight(1).subtract(x).cotUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, x.add(PI.shiftLeft(1)).tanUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        xs = filterInfinite(x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true), P.withScale(4).nonzeroReals());
        for (Real x : take(TINY_LIMIT, xs)) {
            Real y = x.tanUnsafe();
            assertTrue(x, x.cotUnsafe().invertUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        xs = filterInfinite(
                y -> y.abs().ltUnsafe(PI.shiftRight(1)),
                P.realRange(Algebraic.TWO.negate(), Algebraic.TWO)
        );
        for (Real x : take(TINY_LIMIT, xs)) {
            assertTrue(x, x.tanUnsafe().arctan().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesTan() {
        initialize("tan(Rational)");
        Rational max = Rational.of(100);
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                filterInfinite(
                        x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(SMALL_LIMIT, ps)) {
            Optional<Real> oy = p.a.tan(p.b);
            if (oy.isPresent()) {
                Real y = oy.get();
                y.validate();
                assertTrue(p, y.eq(p.a.tanUnsafe(), SMALL_RESOLUTION).orElse(true));
            }
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.rangeDown(Rational.ZERO)))) {
            try {
                p.a.tan(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesCotUnsafe() {
        initialize("cotUnsafe()");
        Rational max = Rational.of(100);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                P.withScale(4).nonzeroReals()
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            Real y = x.cotUnsafe();
            y.validate();
        }

        for (Real x : take(TINY_LIMIT, xs)) {
            Real y = x.cotUnsafe();
            assertTrue(x, x.negate().cotUnsafe().eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftRight(1).subtract(x).tanUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, x.tanUnsafe().invertUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, x.add(PI.shiftLeft(1)).cotUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        xs = filterInfinite(
                y -> y.gt(Rational.ZERO, DEFAULT_RESOLUTION).orElse(false) && y.ltUnsafe(PI),
                P.realRange(Algebraic.ZERO, Algebraic.ONE.shiftLeft(2))
        );
        for (Real x : take(TINY_LIMIT, xs)) {
            assertTrue(x, x.cotUnsafe().arccot().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesCot() {
        initialize("cot(Rational)");
        Rational max = Rational.of(100);
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                filterInfinite(
                        x -> (!x.isExact() || x.rationalValueExact().get() != Rational.ZERO) &&
                                x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(SMALL_LIMIT, ps)) {
            Optional<Real> oy = p.a.cot(p.b);
            if (oy.isPresent()) {
                Real y = oy.get();
                y.validate();
                assertTrue(p, y.eq(p.a.cotUnsafe(), SMALL_RESOLUTION).orElse(true));
            }
        }

        for (Rational r : take(LIMIT, P.positiveRationals())) {
            try {
                ZERO.cot(r);
                fail(r);
            } catch (ArithmeticException ignored) {}
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.nonzeroReals(), P.rangeDown(Rational.ZERO)))) {
            try {
                p.a.cot(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesSecOfRational() {
        initialize("secOfRational(Rational)");
        Rational max = Rational.of(100);
        Iterable<Rational> xs = filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).rationals());
        for (Rational x : take(LIMIT, xs)) {
            Real y = secOfRational(x);
            y.validate();
            assertTrue(x, y.geUnsafe(Rational.ONE) || y.leUnsafe(Rational.NEGATIVE_ONE));
            assertTrue(x, x == Rational.ZERO || !y.isExact());
            assertTrue(x, of(x).secUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        for (Rational x : take(TINY_LIMIT, xs)) {
            Real y = secOfRational(x);
            assertTrue(x, secOfRational(x.negate()).eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftRight(1).subtract(x).cscUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftLeft(1).add(x).secUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        xs = filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).nonzeroRationals());
        for (Rational x : take(TINY_LIMIT, xs)) {
            assertTrue(x, secOfRational(x).eq(cosOfRational(x).invertUnsafe(), DEFAULT_RESOLUTION).orElse(true));
        }

        Iterable<Real> xs2 = filterInfinite(y -> y.ltUnsafe(PI), P.realRange(Algebraic.ZERO, Algebraic.of(4)));
        for (Real x : take(TINY_LIMIT, xs2)) {
            assertTrue(x, x.secUnsafe().arcsecUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesCscOfRational() {
        initialize("cscOfRational(Rational)");
        Rational max = Rational.of(100);
        Iterable<Rational> xs = filterInfinite(x -> Ordering.le(x.abs(), max), P.withScale(4).nonzeroRationals());
        for (Rational x : take(LIMIT, xs)) {
            Real y = cscOfRational(x);
            y.validate();
            assertTrue(x, y.geUnsafe(Rational.ONE) || y.leUnsafe(Rational.NEGATIVE_ONE));
            assertFalse(x, y.isExact());
            assertTrue(x, of(x).cscUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        for (Rational x : take(TINY_LIMIT, xs)) {
            Real y = cscOfRational(x);
            assertTrue(x, cscOfRational(x.negate()).eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftRight(1).subtract(x).secUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.eq(sinOfRational(x).invertUnsafe(), DEFAULT_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftLeft(1).add(x).cscUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        xs = filterInfinite(
                y -> y != Rational.ZERO && PI.shiftRight(1).gtUnsafe(y.abs()),
                P.range(Rational.TWO.negate(), Rational.TWO)
        );
        for (Rational x : take(TINY_LIMIT, xs)) {
            assertTrue(x, cscOfRational(x).arccscUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesSecUnsafe() {
        initialize("secUnsafe()");
        Rational max = Rational.of(100);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                P.withScale(4).reals()
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            Real y = x.secUnsafe();
            y.validate();
            assertTrue(x, y.geUnsafe(Rational.ONE) || y.leUnsafe(Rational.NEGATIVE_ONE));
            assertTrue(x, x.isExact() && x.rationalValueExact().get() == Rational.ZERO || !y.isExact());
        }

        for (Real x : take(TINY_LIMIT, xs)) {
            Real y = x.secUnsafe();
            assertTrue(x, x.negate().secUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftRight(1).subtract(x).cscUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, x.add(PI.shiftLeft(1)).secUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        xs = filterInfinite(x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true), P.withScale(4).nonzeroReals());
        for (Real x : take(TINY_LIMIT, xs)) {
            Real y = x.secUnsafe();
            assertTrue(x, x.cos().invertUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        xs = filterInfinite(y -> y.ltUnsafe(PI), P.realRange(Algebraic.ZERO, Algebraic.of(4)));
        for (Real x : take(TINY_LIMIT, xs)) {
            assertTrue(x, x.secUnsafe().arcsecUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesSec() {
        initialize("sec(Rational)");
        Rational max = Rational.of(100);
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                filterInfinite(
                        x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(SMALL_LIMIT, ps)) {
            Optional<Real> oy = p.a.sec(p.b);
            if (oy.isPresent()) {
                Real y = oy.get();
                y.validate();
                assertTrue(p, y.eq(p.a.secUnsafe(), SMALL_RESOLUTION).orElse(true));
            }
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.rangeDown(Rational.ZERO)))) {
            try {
                p.a.sec(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesCscUnsafe() {
        initialize("cscUnsafe()");
        Rational max = Rational.of(100);
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                P.withScale(4).nonzeroReals()
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            Real y = x.cscUnsafe();
            y.validate();
            assertTrue(x, y.geUnsafe(Rational.ONE) || y.leUnsafe(Rational.NEGATIVE_ONE));
        }

        for (Real x : take(TINY_LIMIT, xs)) {
            Real y = x.cscUnsafe();
            assertTrue(x, x.negate().cscUnsafe().eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, PI.shiftRight(1).subtract(x).secUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, x.sin().invertUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, x.add(PI.shiftLeft(1)).cscUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        xs = filterInfinite(
                y -> y.ne(Rational.ZERO, SMALL_RESOLUTION).orElse(false) && y.abs().ltUnsafe(PI.shiftRight(1)),
                P.realRange(Algebraic.TWO.negate(), Algebraic.TWO)
        );
        for (Real x : take(TINY_LIMIT, xs)) {
            assertTrue(x, x.cscUnsafe().arccscUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesCsc() {
        initialize("csc(Rational)");
        Rational max = Rational.of(100);
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                filterInfinite(
                        x -> (!x.isExact() || x.rationalValueExact().get() != Rational.ZERO) &&
                                x.abs().le(max, DEFAULT_RESOLUTION).orElse(true),
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(SMALL_LIMIT, ps)) {
            Optional<Real> oy = p.a.csc(p.b);
            if (oy.isPresent()) {
                Real y = oy.get();
                y.validate();
                assertTrue(p, y.eq(p.a.cscUnsafe(), SMALL_RESOLUTION).orElse(true));
            }
        }

        for (Rational r : take(LIMIT, P.positiveRationals())) {
            try {
                ZERO.csc(r);
                fail(r);
            } catch (ArithmeticException ignored) {}
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.nonzeroReals(), P.rangeDown(Rational.ZERO)))) {
            try {
                p.a.csc(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesArctanOfRational() {
        initialize("arctanOfRational(Rational)");
        for (Rational x : take(LIMIT, P.withScale(4).rationals())) {
            Real y = arctanOfRational(x);
            y.validate();
            assertTrue(x, y.ltUnsafe(PI.shiftRight(1)));
            assertTrue(x, y.gtUnsafe(PI.shiftRight(1).negate()));
            assertTrue(x, x == Rational.ZERO || !y.isExact());
            assertTrue(x, of(x).arctan().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        for (Rational x : take(TINY_LIMIT, P.withScale(4).rationals())) {
            Real y = arctanOfRational(x);
            assertTrue(x, arctanOfRational(x.negate()).eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.tanUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesArccotOfRational() {
        initialize("arccotOfRational(Rational)");
        for (Rational x : take(LIMIT, P.withScale(4).rationals())) {
            Real y = arccotOfRational(x);
            y.validate();
            assertTrue(x, y.ltUnsafe(PI));
            assertTrue(x, y.gtUnsafe(Rational.ZERO));
            assertFalse(x, y.isExact());
            assertTrue(x, of(x).arccot().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        for (Rational x : take(TINY_LIMIT, P.withScale(4).rationals())) {
            Real y = arccotOfRational(x);
            assertTrue(x, y.cotUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesArctan() {
        initialize("arctan()");
        for (Real x : take(SMALL_LIMIT, P.withScale(4).reals())) {
            Real y = x.arctan();
            y.validate();
            assertTrue(x, y.ltUnsafe(PI.shiftRight(1)));
            assertTrue(x, y.gtUnsafe(PI.shiftRight(1).negate()));
            assertTrue(x, x.isExact() && x.rationalValueExact().get() == Rational.ZERO || !y.isExact());
        }

        for (Real x : take(TINY_LIMIT, P.withScale(4).reals())) {
            Real y = x.arctan();
            assertTrue(x, x.negate().arctan().eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.tanUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesArccot() {
        initialize("arccot()");
        for (Real x : take(SMALL_LIMIT, P.withScale(4).reals())) {
            Real y = x.arccot();
            y.validate();
            assertTrue(x, y.ltUnsafe(PI));
            assertTrue(x, y.gtUnsafe(Rational.ZERO));
            assertFalse(x, y.isExact());
        }

        for (Real x : take(TINY_LIMIT, P.withScale(4).reals())) {
            Real y = x.arccot();
            assertTrue(x, y.cotUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }
    }

    private void propertiesArcsinOfRational() {
        initialize("arcsinOfRational(Rational)");
        for (Rational x : take(LIMIT, P.withScale(4).range(Rational.NEGATIVE_ONE, Rational.ONE))) {
            Real y = arcsinOfRational(x);
            y.validate();
            assertTrue(x, y.le(PI.shiftRight(1), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.ge(PI.shiftRight(1).negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, x == Rational.ZERO || !y.isExact());
            assertTrue(x, of(x).arcsinUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        for (Rational x : take(TINY_LIMIT, P.withScale(4).range(Rational.NEGATIVE_ONE, Rational.ONE))) {
            Real y = arcsinOfRational(x);
            assertTrue(x, arcsinOfRational(x.negate()).eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.sin().eq(x, SMALL_RESOLUTION).orElse(true));
        }

        Iterable<Rational> xsFail = filterInfinite(
                x -> Ordering.gt(x.abs(), Rational.ONE),
                P.withScale(4).rationals()
        );
        for (Rational x : take(LIMIT, xsFail)) {
            try {
                arcsinOfRational(x);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesArccosOfRational() {
        initialize("arccosOfRational(Rational)");
        for (Rational x : take(LIMIT, P.withScale(4).range(Rational.NEGATIVE_ONE, Rational.ONE))) {
            Real y = arccosOfRational(x);
            y.validate();
            assertTrue(x, y.le(PI, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.geUnsafe(Rational.ZERO));
            assertTrue(x, x == Rational.ONE || !y.isExact());
            assertTrue(x, of(x).arccosUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        for (Rational x : take(TINY_LIMIT, P.withScale(4).range(Rational.NEGATIVE_ONE, Rational.ONE))) {
            Real y = arccosOfRational(x);
            assertTrue(x, y.cos().eq(x, SMALL_RESOLUTION).orElse(true));
        }

        Iterable<Rational> xsFail = filterInfinite(
                x -> Ordering.gt(x.abs(), Rational.ONE),
                P.withScale(4).rationals()
        );
        for (Rational x : take(LIMIT, xsFail)) {
            try {
                arccosOfRational(x);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesArcsinUnsafe() {
        initialize("arcsinUnsafe()");
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                P.withScale(4).realRange(Algebraic.NEGATIVE_ONE, Algebraic.ONE)
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            Real y = x.arcsinUnsafe();
            y.validate();
            assertTrue(x, y.le(PI.shiftRight(1), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.ge(PI.shiftRight(1).negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, x.isExact() && x.rationalValueExact().get() == Rational.ZERO || !y.isExact());
        }

        for (Real x : take(TINY_LIMIT, xs)) {
            Real y = x.arcsinUnsafe();
            assertTrue(x, x.negate().arcsinUnsafe().eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.sin().eq(x, SMALL_RESOLUTION).orElse(true));
        }

        Iterable<Real> xsFail = filterInfinite(
                x -> x.abs().gt(Rational.ONE, SMALL_RESOLUTION).orElse(false),
                P.withScale(4).reals()
        );
        for (Real x : take(LIMIT, xsFail)) {
            try {
                toList(x.arcsinUnsafe());
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesArcsin() {
        initialize("arcsin(Rational)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                P.realRange(Algebraic.NEGATIVE_ONE, Algebraic.ONE),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(MEDIUM_LIMIT, ps)) {
            Optional<Real> oy = p.a.arcsin(p.b);
            if (oy.isPresent()) {
                Real y = oy.get();
                y.validate();
                assertTrue(p, y.eq(p.a.arcsinUnsafe(), SMALL_RESOLUTION).orElse(true));
            }
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.nonzeroReals(), P.rangeDown(Rational.ZERO)))) {
            try {
                p.a.arcsin(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesArccosUnsafe() {
        initialize("arccosUnsafe()");
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().le(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                P.withScale(4).realRange(Algebraic.NEGATIVE_ONE, Algebraic.ONE)
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            Real y = x.arccosUnsafe();
            y.validate();
            assertTrue(x, y.le(PI, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.ge(Rational.ZERO, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, x.isExact() && x.rationalValueExact().get() == Rational.ONE || !y.isExact());
        }

        for (Real x : take(TINY_LIMIT, xs)) {
            Real y = x.arccosUnsafe();
            assertTrue(x, y.cos().eq(x, SMALL_RESOLUTION).orElse(true));
        }

        Iterable<Real> xsFail = filterInfinite(
                x -> x.abs().gt(Rational.ONE, SMALL_RESOLUTION).orElse(false),
                P.withScale(4).reals()
        );
        for (Real x : take(LIMIT, xsFail)) {
            try {
                toList(x.arccosUnsafe());
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesArccos() {
        initialize("arccos(Rational)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                P.realRange(Algebraic.NEGATIVE_ONE, Algebraic.ONE),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(MEDIUM_LIMIT, ps)) {
            Optional<Real> oy = p.a.arccos(p.b);
            if (oy.isPresent()) {
                Real y = oy.get();
                y.validate();
                assertTrue(p, y.eq(p.a.arccosUnsafe(), SMALL_RESOLUTION).orElse(true));
            }
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.nonzeroReals(), P.rangeDown(Rational.ZERO)))) {
            try {
                p.a.arccos(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesArcsecOfRational() {
        initialize("arcsecOfRational(Rational)");
        Iterable<Rational> xs = filterInfinite(x -> Ordering.ge(x.abs(), Rational.ONE), P.withScale(4).rationals());
        for (Rational x : take(LIMIT, xs)) {
            Real y = arcsecOfRational(x);
            y.validate();
            assertTrue(x, y.le(PI, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.geUnsafe(Rational.ZERO));
            assertTrue(x, x == Rational.ONE || !y.isExact());
            assertTrue(x, of(x).arcsecUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        for (Rational x : take(TINY_LIMIT, xs)) {
            Real y = arcsecOfRational(x);
            assertTrue(x, y.secUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }

        Iterable<Rational> xsFail = filterInfinite(
                x -> x.abs() != Rational.ONE,
                P.withScale(4).range(Rational.NEGATIVE_ONE, Rational.ONE)
        );
        for (Rational x : take(LIMIT, xsFail)) {
            try {
                arcsecOfRational(x);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesArccscOfRational() {
        initialize("arccscOfRational(Rational)");
        Iterable<Rational> xs = filterInfinite(x -> Ordering.ge(x.abs(), Rational.ONE), P.withScale(4).rationals());
        for (Rational x : take(LIMIT, xs)) {
            Real y = arccscOfRational(x);
            y.validate();
            assertTrue(x, y.le(PI.shiftRight(1), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.ge(PI.shiftRight(1).negate(), SMALL_RESOLUTION).orElse(true));
            assertFalse(x, y.isExact());
            assertTrue(x, of(x).arccscUnsafe().eq(y, SMALL_RESOLUTION).orElse(true));
        }

        for (Rational x : take(TINY_LIMIT, xs)) {
            Real y = arccscOfRational(x);
            assertTrue(x, arccscOfRational(x.negate()).eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.cscUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }

        Iterable<Rational> xsFail = filterInfinite(
                x -> x.abs() != Rational.ONE,
                P.withScale(4).range(Rational.NEGATIVE_ONE, Rational.ONE)
        );
        for (Rational x : take(LIMIT, xsFail)) {
            try {
                arccscOfRational(x);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesArcsecUnsafe() {
        initialize("arcsecUnsafe()");
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().ge(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                P.withScale(4).reals()
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            Real y = x.arcsecUnsafe();
            y.validate();
            assertTrue(x, y.le(PI, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.ge(Rational.ZERO, SMALL_RESOLUTION).orElse(true));
            assertTrue(x, x.isExact() && x.rationalValueExact().get() == Rational.ONE || !y.isExact());
        }

        for (Real x : take(TINY_LIMIT, xs)) {
            Real y = x.arcsecUnsafe();
            assertTrue(x, y.secUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }

        Iterable<Real> xsFail = filterInfinite(
                x -> x.ne(Rational.ZERO, DEFAULT_RESOLUTION).orElse(false) &&
                        x.abs().ne(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                P.withScale(4).realRange(Algebraic.NEGATIVE_ONE, Algebraic.ONE)
        );
        for (Real x : take(SMALL_LIMIT, xsFail)) {
            try {
                toList(x.arcsecUnsafe());
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesArcsec() {
        initialize("arcsec(Rational)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                filterInfinite(
                        x -> x.abs().ge(Rational.ONE, DEFAULT_RESOLUTION).orElse(true),
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(MEDIUM_LIMIT, ps)) {
            Optional<Real> oy = p.a.arcsec(p.b);
            if (oy.isPresent()) {
                Real y = oy.get();
                y.validate();
                assertTrue(p, y.eq(p.a.arcsecUnsafe(), SMALL_RESOLUTION).orElse(true));
            }
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                filterInfinite(x -> x.abs().ge(Rational.ONE, SMALL_RESOLUTION).orElse(true), P.withScale(4).reals()),
                P.rangeDown(Rational.ZERO)
        );
        for (Pair<Real, Rational> p : take(SMALL_LIMIT, psFail)) {
            try {
                p.a.arcsec(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesArccscUnsafe() {
        initialize("arccscUnsafe()");
        Iterable<Real> xs = filterInfinite(
                x -> x.abs().ge(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                P.withScale(4).reals()
        );
        for (Real x : take(SMALL_LIMIT, xs)) {
            Real y = x.arccscUnsafe();
            y.validate();
            assertTrue(x, y.le(PI.shiftRight(1), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.ge(PI.shiftRight(1).negate(), SMALL_RESOLUTION).orElse(true));
            assertFalse(x, y.isExact());
        }

        for (Real x : take(TINY_LIMIT, xs)) {
            Real y = x.arccscUnsafe();
            assertTrue(x, x.negate().arccscUnsafe().eq(y.negate(), SMALL_RESOLUTION).orElse(true));
            assertTrue(x, y.cscUnsafe().eq(x, SMALL_RESOLUTION).orElse(true));
        }

        Iterable<Real> xsFail = filterInfinite(
                x -> x.ne(Rational.ZERO, DEFAULT_RESOLUTION).orElse(false) &&
                        x.abs().ne(Rational.ONE, DEFAULT_RESOLUTION).orElse(false),
                P.withScale(4).realRange(Algebraic.NEGATIVE_ONE, Algebraic.ONE)
        );
        for (Real x : take(SMALL_LIMIT, xsFail)) {
            try {
                toList(x.arccscUnsafe());
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesArccsc() {
        initialize("arccsc(Rational)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(
                filterInfinite(
                        x -> x.abs().ge(Rational.ONE, DEFAULT_RESOLUTION).orElse(true),
                        P.withScale(4).reals()
                ),
                P.positiveRationals()
        );
        for (Pair<Real, Rational> p : take(MEDIUM_LIMIT, ps)) {
            Optional<Real> oy = p.a.arccsc(p.b);
            if (oy.isPresent()) {
                Real y = oy.get();
                y.validate();
                assertTrue(p, y.eq(p.a.arccscUnsafe(), SMALL_RESOLUTION).orElse(true));
            }
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                filterInfinite(x -> x.abs().ge(Rational.ONE, SMALL_RESOLUTION).orElse(true), P.withScale(4).reals()),
                P.rangeDown(Rational.ZERO)
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.arccsc(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAtan2OfRational() {
        initialize("atan2OfRational(Rational, Rational)");
        Iterable<Pair<Rational, Rational>> ps = filterInfinite(
                p -> p.a != Rational.ZERO || p.b != Rational.ZERO,
                P.pairs(P.withScale(4).rationals())
        );
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            Real theta = atan2OfRational(p.a, p.b);
            theta.validate();
        }

        for (Pair<Rational, Rational> p : take(TINY_LIMIT, ps)) {
            Real r = sqrtOfRational(p.a.pow(2).add(p.b.pow(2)));
            Real theta = atan2OfRational(p.a, p.b);
            assertTrue(p, theta.sin().multiply(r).eq(p.a, SMALL_RESOLUTION).orElse(true));
            assertTrue(p, theta.cos().multiply(r).eq(p.b, SMALL_RESOLUTION).orElse(true));
        }

        ps = P.pairs(P.withScale(4).rationals(), P.withScale(4).positiveRationals());
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            assertTrue(
                    p,
                    atan2OfRational(p.a, p.b).eq(arctanOfRational(p.a.divide(p.b)), SMALL_RESOLUTION).orElse(true)
            );
        }

        for (Rational r : take(LIMIT, P.positiveRationals())) {
            assertTrue(r, atan2OfRational(Rational.ZERO, r) == ZERO);
            assertTrue(r, atan2OfRational(Rational.ZERO, r.negate()).eq(PI, SMALL_RESOLUTION).orElse(true));
            assertTrue(r, atan2OfRational(r, Rational.ZERO).eq(PI.shiftRight(1), SMALL_RESOLUTION).orElse(true));
            assertTrue(
                    r,
                    atan2OfRational(r.negate(), Rational.ZERO).eq(PI.shiftRight(1).negate(), SMALL_RESOLUTION)
                            .orElse(true)
            );
        }
    }

    private void propertiesAtan2Unsafe() {
        initialize("atan2Unsafe(Real, Real)");
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> (p.a.isExact() && p.b.ne(Rational.ZERO, DEFAULT_RESOLUTION).orElse(false)) ||
                        p.a.ne(Rational.ZERO, DEFAULT_RESOLUTION).orElse(false) ||
                        p.a.gt(Rational.ZERO, DEFAULT_RESOLUTION).orElse(false),
                P.pairs(P.withScale(4).reals())
        );
        for (Pair<Real, Real> p : take(MEDIUM_LIMIT, ps)) {
            Real theta = atan2Unsafe(p.a, p.b);
            theta.validate();
        }

        for (Pair<Real, Real> p : take(TINY_LIMIT, ps)) {
            Real r = p.a.powUnsafe(2).add(p.b.powUnsafe(2)).sqrtUnsafe();
            Real theta = atan2Unsafe(p.a, p.b);
            assertTrue(p, theta.sin().multiply(r).eq(p.a, SMALL_RESOLUTION).orElse(true));
            assertTrue(p, theta.cos().multiply(r).eq(p.b, SMALL_RESOLUTION).orElse(true));
        }

        ps = P.pairs(P.withScale(4).reals(), P.withScale(4).positiveReals());
        for (Pair<Real, Real> p : take(TINY_LIMIT, ps)) {
            assertTrue(p, atan2Unsafe(p.a, p.b).eq(p.a.divideUnsafe(p.b).arctan(), SMALL_RESOLUTION).orElse(true));
        }

        for (Real r : take(TINY_LIMIT, P.positiveReals())) {
            assertTrue(r, atan2Unsafe(Real.ZERO, r) == ZERO);
            assertTrue(r, atan2Unsafe(Real.ZERO, r.negate()).eq(PI, SMALL_RESOLUTION).orElse(true));
            assertTrue(r, atan2Unsafe(r, Real.ZERO).eq(PI.shiftRight(1), SMALL_RESOLUTION).orElse(true));
            assertTrue(
                    r,
                    atan2Unsafe(r.negate(), Real.ZERO).eq(PI.shiftRight(1).negate(), SMALL_RESOLUTION).orElse(true)
            );
        }
    }

    private void propertiesAtan2() {
        initialize("atan2(Real, Real, Rational)");
        Iterable<Triple<Real, Real, Rational>> ts = filterInfinite(
                t -> !t.a.isExact() || !t.b.isExact() || t.a.rationalValueExact().get() != Rational.ZERO ||
                        t.b.rationalValueExact().get() != Rational.ZERO,
                P.triples(P.withScale(4).reals(), P.withScale(4).reals(), P.positiveRationals())
        );
        for (Triple<Real, Real, Rational> t : take(MEDIUM_LIMIT, ts)) {
            Optional<Real> oTheta = atan2(t.a, t.b, t.c);
            if (oTheta.isPresent()) {
                Real theta = oTheta.get();
                theta.validate();
                assertTrue(t, theta.eq(atan2Unsafe(t.a, t.b), SMALL_RESOLUTION).orElse(true));
            }
        }

        for (Rational r : take(LIMIT, P.positiveRationals())) {
            try {
                atan2(ZERO, ZERO, r);
                fail(r);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Triple<Real, Real, Rational>> tsFail = filterInfinite(
                t -> !t.a.isExact() || !t.b.isExact() || t.a.rationalValueExact().get() != Rational.ZERO ||
                        t.b.rationalValueExact().get() != Rational.ZERO,
                P.triples(P.withScale(4).reals(), P.withScale(4).reals(), P.rangeDown(Rational.ZERO))
        );
        for (Triple<Real, Real, Rational> t : take(MEDIUM_LIMIT, tsFail)) {
            try {
                atan2(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesIntervalExtensionUnsafe() {
        initialize("intervalExtensionUnsafe(Real, Real)");
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.lt(p.b, DEFAULT_RESOLUTION).orElse(false),
                P.pairs(P.withScale(4).reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            Interval extension = intervalExtensionUnsafe(p.a, p.b);
            assertTrue(p, extension.isFinitelyBounded());
            assertTrue(p, p.b.subtract(p.a).geUnsafe(extension.diameter().get().shiftRight(1)));
        }

        for (Real x : take(LIMIT, P.reals())) {
            try {
                intervalExtensionUnsafe(x, x);
                fail(x);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesFractionalPartUnsafe() {
        initialize("fractionalPartUnsafe()");
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
            Real fractionalPart = x.fractionalPartUnsafe();
            fractionalPart.validate();
            assertTrue(x, fractionalPart.geUnsafe(ZERO));
            assertTrue(x, fractionalPart.ltUnsafe(ONE));
            assertTrue(x, of(x.floorUnsafe()).add(fractionalPart).eq(x, DEFAULT_RESOLUTION).orElse(true));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).fractionalPartUnsafe().rationalValueExact().get(), Rational.ZERO);
        }
    }

    private void propertiesFractionalPart() {
        initialize("fractionalPart(Rational)");
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.positiveRationals()))) {
            Optional<Real> ox = p.a.fractionalPart(p.b);
            if (ox.isPresent()) {
                ox.get().validate();
                assertTrue(p, p.a.fractionalPartUnsafe().eq(ox.get(), DEFAULT_RESOLUTION).orElse(true));
            }
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertFalse(i, leftFuzzyRepresentation(Rational.of(i)).fractionalPart(DEFAULT_RESOLUTION).isPresent());
        }

        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.rangeDown(Rational.ZERO)))) {
            try {
                p.a.fractionalPart(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
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

    private void propertiesRoundToDenominatorUnsafe() {
        initialize("roundToDenominatorUnsafe(BigInteger, RoundingMode)");
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
            Rational rounded = t.a.roundToDenominatorUnsafe(t.b, t.c);
            rounded.validate();
            assertEquals(t, t.b.mod(rounded.getDenominator()), BigInteger.ZERO);
            assertTrue(t, rounded == Rational.ZERO || rounded.signum() == t.a.signumUnsafe());
            assertTrue(t, t.a.subtract(rounded).abs().lt(of(Rational.of(BigInteger.ONE, t.b)), DEFAULT_RESOLUTION)
                    .orElse(true));
        }

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
                        p -> rmdCheck(p.a.a, BigInteger.ONE, p.b, p.a.b),
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
            Rational rounded = p.a.roundToDenominatorUnsafe(BigInteger.ONE, p.b);
            assertEquals(p, rounded.getNumerator(), p.a.bigIntegerValueUnsafe(p.b));
            assertEquals(p, rounded.getDenominator(), BigInteger.ONE);
        }

        Iterable<Pair<Real, BigInteger>> ps2 = map(
                q -> new Pair<>(of(q.a), q.b),
                filterInfinite(
                        p -> p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO),
                        P.pairs(P.rationals(), P.positiveBigIntegers())
                )
        );
        for (Pair<Real, BigInteger> p : take(LIMIT, ps2)) {
            assertEquals(
                    p,
                    p.a.roundToDenominatorUnsafe(p.b, RoundingMode.UNNECESSARY),
                    p.a.rationalValueExact().get()
            );
        }

        for (Pair<Real, BigInteger> p : take(LIMIT, P.pairs(P.cleanReals(), P.positiveBigIntegers()))) {
            assertTrue(p, p.a.geUnsafe(p.a.roundToDenominatorUnsafe(p.b, RoundingMode.FLOOR)));
            assertTrue(p, p.a.leUnsafe(p.a.roundToDenominatorUnsafe(p.b, RoundingMode.CEILING)));
            assertTrue(p, p.a.abs().geUnsafe(p.a.roundToDenominatorUnsafe(p.b, RoundingMode.DOWN).abs()));
            assertTrue(p, p.a.abs().leUnsafe(p.a.roundToDenominatorUnsafe(p.b, RoundingMode.UP).abs()));
            Rational resolution = Rational.of(p.b).shiftLeft(1).invert();
            assertTrue(
                    p,
                    p.a.subtract(p.a.roundToDenominatorUnsafe(p.b, RoundingMode.HALF_DOWN)).abs()
                            .leUnsafe(resolution));
            assertTrue(
                    p,
                    p.a.subtract(p.a.roundToDenominatorUnsafe(p.b, RoundingMode.HALF_UP)).abs().leUnsafe(resolution));
            assertTrue(
                    p,
                    p.a.subtract(p.a.roundToDenominatorUnsafe(p.b, RoundingMode.HALF_EVEN)).abs()
                            .leUnsafe(resolution));
        }

        ps2 = filterInfinite(
                p -> p.a.abs().multiply(p.b).fractionalPartUnsafe().ltUnsafe(Rational.ONE_HALF),
                P.pairs(P.cleanReals(), P.positiveBigIntegers())
        );
        for (Pair<Real, BigInteger> p : take(LIMIT, ps2)) {
            Rational down = p.a.roundToDenominatorUnsafe(p.b, RoundingMode.DOWN);
            assertEquals(p, p.a.roundToDenominatorUnsafe(p.b, RoundingMode.HALF_DOWN), down);
            assertEquals(p, p.a.roundToDenominatorUnsafe(p.b, RoundingMode.HALF_UP), down);
            assertEquals(p, p.a.roundToDenominatorUnsafe(p.b, RoundingMode.HALF_EVEN), down);
        }

        ps2 = filterInfinite(
                p -> p.a.abs().multiply(p.b).fractionalPartUnsafe().gtUnsafe(ONE_HALF),
                P.pairs(P.cleanReals(), P.positiveBigIntegers())
        );
        for (Pair<Real, BigInteger> p : take(LIMIT, ps2)) {
            Rational up = p.a.roundToDenominatorUnsafe(p.b, RoundingMode.UP);
            assertEquals(p, p.a.roundToDenominatorUnsafe(p.b, RoundingMode.HALF_DOWN), up);
            assertEquals(p, p.a.roundToDenominatorUnsafe(p.b, RoundingMode.HALF_UP), up);
            assertEquals(p, p.a.roundToDenominatorUnsafe(p.b, RoundingMode.HALF_EVEN), up);
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.getDenominator().testBit(0), P.rationals()))) {
            Real x = of(r);
            BigInteger denominator = r.getDenominator().shiftRight(1);
            assertEquals(
                    x,
                    x.abs().multiply(denominator).fractionalPartUnsafe().rationalValueExact().get(),
                    Rational.ONE_HALF
            );
            Rational hd = x.roundToDenominatorUnsafe(denominator, RoundingMode.HALF_DOWN);
            assertEquals(x, hd, x.roundToDenominatorUnsafe(denominator, RoundingMode.DOWN));
            Rational hu = x.roundToDenominatorUnsafe(denominator, RoundingMode.HALF_UP);
            assertEquals(x, hu, x.roundToDenominatorUnsafe(denominator, RoundingMode.UP));
            Rational he = x.roundToDenominatorUnsafe(denominator, RoundingMode.HALF_EVEN);
            assertFalse(r, he.multiply(denominator).getNumerator().testBit(0));
        }

        //noinspection RedundantCast
        Iterable<Triple<Real, BigInteger, RoundingMode>> tsFail = map(
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
                                P.rangeDown(BigInteger.ZERO),
                                P.roundingModes()
                        )
                )
        );
        for (Triple<Real, BigInteger, RoundingMode> t : take(LIMIT, tsFail)) {
            try {
                t.a.roundToDenominatorUnsafe(t.b, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Real, BigInteger>> psFail = filterInfinite(
                p -> !p.a.isExact() ||
                        !p.b.mod(p.a.rationalValueExact().get().getDenominator()).equals(BigInteger.ZERO),
                P.pairs(P.cleanReals(), P.positiveBigIntegers())
        );
        for (Pair<Real, BigInteger> p : take(LIMIT, psFail)) {
            try {
                p.a.roundToDenominatorUnsafe(p.b, RoundingMode.UNNECESSARY);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesRoundToDenominator() {
        initialize("roundToDenominator(BigInteger, RoundingMode, Rational)");
        Iterable<Quadruple<Real, BigInteger, RoundingMode, Rational>> qs = filterInfinite(
                r -> r.c != RoundingMode.UNNECESSARY || r.a.multiply(r.b).isExactInteger(),
                P.quadruples(
                        P.reals(),
                        P.positiveBigIntegers(),
                        P.roundingModes(),
                        P.positiveRationals()
                )
        );
        for (Quadruple<Real, BigInteger, RoundingMode, Rational> q : take(LIMIT, qs)) {
            Optional<Rational> or = q.a.roundToDenominator(q.b, q.c, q.d);
            or.ifPresent(rational -> assertEquals(q, q.a.roundToDenominatorUnsafe(q.b, q.c), rational));
        }

        Iterable<Quadruple<Real, BigInteger, RoundingMode, Rational>> qsFail = filterInfinite(
                r -> r.c != RoundingMode.UNNECESSARY || r.a.multiply(r.b).isExactInteger(),
                P.quadruples(
                        P.reals(),
                        P.rangeDown(BigInteger.ZERO),
                        P.roundingModes(),
                        P.positiveRationals()
                )
        );
        for (Quadruple<Real, BigInteger, RoundingMode, Rational> q : take(LIMIT, qsFail)) {
            try {
                q.a.roundToDenominator(q.b, q.c, q.d);
                fail(q);
            } catch (ArithmeticException ignored) {}
        }

        qsFail = filterInfinite(
                r -> r.c != RoundingMode.UNNECESSARY || r.a.multiply(r.b).isExactInteger(),
                P.quadruples(
                        P.reals(),
                        P.positiveBigIntegers(),
                        P.roundingModes(),
                        P.rangeDown(Rational.ZERO)
                )
        );
        for (Quadruple<Real, BigInteger, RoundingMode, Rational> q : take(LIMIT, qsFail)) {
            try {
                q.a.roundToDenominator(q.b, q.c, q.d);
                fail(q);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesContinuedFractionUnsafe() {
        initialize("continuedFractionUnsafe()");
        for (Real x : take(LIMIT, P.cleanReals())) {
            List<BigInteger> continuedFraction = toList(take(TINY_LIMIT, x.continuedFractionUnsafe()));
            assertFalse(x, continuedFraction.isEmpty());
            assertTrue(x, all(Objects::nonNull, continuedFraction));
            assertTrue(x, all(i -> i.signum() == 1, tail(continuedFraction)));
        }
    }

    private void propertiesFromContinuedFraction() {
        initialize("fromContinuedFraction()");
        Iterable<List<BigInteger>> iss = map(
                p -> toList(cons(p.a, p.b)),
                P.pairs(P.bigIntegers(), P.lists(P.positiveBigIntegers()))
        );
        for (List<BigInteger> is : take(LIMIT, iss)) {
            Real x = fromContinuedFraction(is);
            x.validate();
            assertTrue(is, x.isExact());
            if (!is.isEmpty() && !last(is).equals(BigInteger.ONE)) {
                assertEquals(is, toList(x.continuedFractionUnsafe()), is);
            }
            assertEquals(is, x.rationalValueExact().get(), Rational.fromContinuedFraction(is));
        }

        Iterable<Iterable<BigInteger>> iss2 = map(
                p -> cons(p.a, p.b),
                P.pairs(P.bigIntegers(), P.prefixPermutations(EP.positiveBigIntegers()))
        );
        for (Iterable<BigInteger> is : take(LIMIT, iss2)) {
            Real x = fromContinuedFraction(is);
            x.validate();
            assertEquals(is, toList(take(TINY_LIMIT, x.continuedFractionUnsafe())), toList(take(TINY_LIMIT, is)));
        }

        Iterable<List<BigInteger>> issFail = filterInfinite(
                is -> any(Objects::isNull, is),
                map(
                        p -> toList(cons(p.a, p.b)),
                        P.pairs(P.bigIntegers(), P.lists(P.withNull(P.positiveBigIntegers())))
                )
        );
        for (List<BigInteger> is : take(LIMIT, issFail)) {
            try {
                toList(fromContinuedFraction(is));
                fail(is);
            } catch (NullPointerException ignored) {}
        }

        issFail = filterInfinite(is -> any(i -> i.signum() != 1, tail(is)), P.listsAtLeast(1, P.bigIntegers()));
        for (List<BigInteger> is : take(LIMIT, issFail)) {
            try {
                toList(fromContinuedFraction(is));
                fail(is);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesConvergentsUnsafe() {
        initialize("convergentsUnsafe()");
        for (Real x : take(LIMIT, P.cleanReals())) {
            List<Rational> convergents = toList(take(TINY_LIMIT, x.convergentsUnsafe()));
            assertFalse(x, convergents.isEmpty());
            assertTrue(x, all(Objects::nonNull, convergents));
            assertEquals(x, head(convergents), Rational.of(x.floorUnsafe()));
            if (x.isExact()) {
                assertEquals(x, last(x.convergentsUnsafe()), x.rationalValueExact().get());
            }
            assertTrue(x, zigzagging(convergents));
            if (convergents.size() > 1) {
                assertTrue(x, lt(convergents.get(0), convergents.get(1)));
            }
        }
    }

    private void propertiesDigitsUnsafe() {
        initialize("digitsUnsafe(BigInteger)");
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
        for (Pair<Real, BigInteger> p : take(LIMIT, ps)) {
            Pair<List<BigInteger>, Iterable<BigInteger>> digits = p.a.digitsUnsafe(p.b);
            assertTrue(p, digits.a.isEmpty() || !head(digits.a).equals(BigInteger.ZERO));
            assertTrue(p, all(x -> x.signum() != -1 && lt(x, p.b), digits.a));
            assertEquals(p, IntegerUtils.fromBigEndianDigits(p.b, digits.a), p.a.floorUnsafe());
        }

        ps = map(
                p -> new Pair<>(Real.of(p.a), p.b),
                filterInfinite(
                        q -> q.a.hasTerminatingBaseExpansion(q.b),
                        P.pairsSquareRootOrder(
                                P.withScale(41).rangeUp(Rational.ZERO),
                                P.withScale(4).rangeUp(IntegerUtils.TWO)
                        )
                )
        );
        for (Pair<Real, BigInteger> p : take(LIMIT, ps)) {
            List<BigInteger> afterDecimal = toList(p.a.digitsUnsafe(p.b).b);
            assertTrue(p, afterDecimal.isEmpty() || !last(afterDecimal).equals(BigInteger.ZERO));
        }

        Iterable<Pair<Real, BigInteger>> psFail = P.pairs(P.negativeCleanReals(), P.rangeUp(IntegerUtils.TWO));
        for (Pair<Real, BigInteger> p : take(LIMIT, psFail)) {
            try {
                p.a.digitsUnsafe(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Real, BigInteger> p : take(LIMIT, P.pairs(P.reals(), P.rangeDown(BigInteger.ONE)))) {
            try {
                p.a.digitsUnsafe(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesDigits() {
        initialize("digits(BigInteger, Rational)");
        //noinspection Convert2MethodRef
        Iterable<Triple<Real, BigInteger, Rational>> ts = P.triples(
                P.realRangeUp(Algebraic.ZERO),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                P.positiveRationals()
        );
        for (Triple<Real, BigInteger, Rational> t : take(LIMIT, ts)) {
            Optional<Pair<List<BigInteger>, Iterable<BigInteger>>> op = t.a.digits(t.b, t.c);
            if (op.isPresent()) {
                Pair<List<BigInteger>, Iterable<BigInteger>> digitsUnsafe = t.a.digitsUnsafe(t.b);
                List<BigInteger> abbreviatedDigits = toList(take(TINY_LIMIT, op.get().b));
                if (!abbreviatedDigits.isEmpty() && last(abbreviatedDigits).equals(BigInteger.ZERO)) {
                    abbreviatedDigits.remove(abbreviatedDigits.size() - 1);
                }
                List<BigInteger> abbreviatedDigitsUnsafe = toList(take(abbreviatedDigits.size(), digitsUnsafe.b));
                assertEquals(t, op.get().a, digitsUnsafe.a);
                assertEquals(t, abbreviatedDigits, abbreviatedDigitsUnsafe);
            }
        }

        //noinspection Convert2MethodRef
        Iterable<Triple<Real, BigInteger, Rational>> tsFail = P.triples(
                P.realRangeUp(Algebraic.ZERO),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, BigInteger, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.digits(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        //noinspection Convert2MethodRef
        tsFail = P.triples(
                P.realRangeUp(Algebraic.ZERO),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, BigInteger, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.digits(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesFromDigits() {
        initialize("fromDigits(BigInteger, List<BigInteger>, Iterable<BigInteger>");
        //noinspection Convert2MethodRef
        Iterable<Triple<BigInteger, List<BigInteger>, List<BigInteger>>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairsInfinite(
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                        b -> P.pairs(P.lists(P.range(BigInteger.ZERO, b.subtract(BigInteger.ONE))))
                )
        );
        for (Triple<BigInteger, List<BigInteger>, List<BigInteger>> t : take(LIMIT, ts)) {
            Real x = fromDigits(t.a, t.b, t.c);
            x.validate();
            Pair<List<BigInteger>, Iterable<BigInteger>> digits = x.digitsUnsafe(t.a);
            assertEquals(t, digits.a, toList(dropWhile(i -> i.equals(BigInteger.ZERO), t.b)));
            assertEquals(t, toList(take(t.c.size(), digits.b)), t.c);
        }

        //noinspection Convert2MethodRef
        Iterable<Triple<BigInteger, List<BigInteger>, Iterable<BigInteger>>> ts2 = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairsInfinite(
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                        b -> P.pairs(
                                P.lists(P.range(BigInteger.ZERO, b.subtract(BigInteger.ONE))),
                                map(
                                        IterableUtils::cycle,
                                        P.listsAtLeast(1, P.range(BigInteger.ZERO, b.subtract(BigInteger.ONE)))
                                )
                        )
                )
        );
        for (Triple<BigInteger, List<BigInteger>, Iterable<BigInteger>> t : take(LIMIT, ts2)) {
            Real x = fromDigits(t.a, t.b, t.c);
            x.validate();
            Optional<Pair<List<BigInteger>, Iterable<BigInteger>>> oDigits = x.digits(t.a, DEFAULT_RESOLUTION);
            if (oDigits.isPresent()) {
                Pair<List<BigInteger>, Iterable<BigInteger>> digits = oDigits.get();
                assertEquals(t, digits.a, toList(dropWhile(i -> i.equals(BigInteger.ZERO), t.b)));
                assertEquals(t, toList(take(TINY_LIMIT, digits.b)), toList(take(TINY_LIMIT, t.c)));
            }
        }

        //noinspection Convert2MethodRef
        Iterable<Triple<BigInteger, List<BigInteger>, List<BigInteger>>> tsFail = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                filterInfinite(
                        q -> any(i -> i == null || i.signum() == -1 || ge(i, q.a), q.b.a) ||
                                any(i -> i == null || i.signum() == -1 || ge(i, q.a), q.b.b),
                        P.dependentPairsInfinite(
                                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                                i -> P.pairs(P.lists(P.withNull(P.bigIntegers())))
                        )
                )
        );
        for (Triple<BigInteger, List<BigInteger>, List<BigInteger>> t : take(LIMIT, tsFail)) {
            try {
                toList(fromDigits(t.a, t.b, t.c));
                fail(t);
            } catch (IllegalArgumentException | NullPointerException ignored) {}
        }

        //noinspection Convert2MethodRef
        Iterable<Triple<BigInteger, List<BigInteger>, Iterable<BigInteger>>> tsFail2 = map(
                p -> new Triple<>(p.a, p.b.a, cycle(p.b.b)),
                filterInfinite(
                        q -> any(i -> i == null || i.signum() == -1 || ge(i, q.a), q.b.a) ||
                                any(i -> i == null || i.signum() == -1 || ge(i, q.a), q.b.b),
                        P.dependentPairsInfinite(
                                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                                b -> P.pairs(
                                        P.lists(P.withNull(P.bigIntegers())),
                                        P.listsAtLeast(1, P.withNull(P.bigIntegers()))
                                )
                        )
                )
        );
        for (Triple<BigInteger, List<BigInteger>, Iterable<BigInteger>> t : take(LIMIT, tsFail2)) {
            try {
                toList(fromDigits(t.a, t.b, t.c));
                fail(t);
            } catch (IllegalArgumentException | NullPointerException ignored) {}
        }
    }

    private void propertiesLiouville() {
        initialize("liouville(BigInteger)");
        for (BigInteger i : take(LIMIT, P.withScale(8).rangeUp(IntegerUtils.TWO))) {
            Real x = liouville(i);
            x.validate();
            assertFalse(i, x.isExact());
            assertEquals(i, x.signumUnsafe(), 1);
            assertTrue(i, x.ltUnsafe(Rational.ONE));
        }
    }

    private void propertiesChampernowne() {
        initialize("champernowne(BigInteger)");
        for (BigInteger i : take(LIMIT, P.withScale(8).rangeUp(IntegerUtils.TWO))) {
            Real x = champernowne(i);
            x.validate();
            assertFalse(i, x.isExact());
            assertEquals(i, x.signumUnsafe(), 1);
            assertTrue(i, x.ltUnsafe(Rational.ONE));
        }
    }

    private void propertiesCopelandErdos() {
        initialize("copelandErdos(BigInteger)");
        for (BigInteger i : take(LIMIT, P.withScale(8).rangeUp(IntegerUtils.TWO))) {
            Real x = copelandErdos(i);
            x.validate();
            assertFalse(i, x.isExact());
            assertEquals(i, x.signumUnsafe(), 1);
            assertTrue(i, x.ltUnsafe(Rational.ONE));
        }
    }

    private void propertiesGreedyNormal() {
        initialize("greedyNormal(int)");
        for (int i : take(MEDIUM_LIMIT, P.rangeUpGeometric(2))) {
            Real x = greedyNormal(i);
            x.validate();
            assertFalse(i, x.isExact());
            assertEquals(i, x.signumUnsafe(), 1);
            assertTrue(i, x.ltUnsafe(Rational.ONE));
        }
    }

    private void propertiesToStringBaseUnsafe() {
        initialize("toStringBaseUnsafe(BigInteger, int)");
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
                                    P.rationals()
                            );
                            return P.withScale(1).choose(
                                    map(Algebraic::realValue, P.withElement(Algebraic.ZERO, P.positiveAlgebraics())),
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
            String s = t.a.toStringBaseUnsafe(t.b, t.c);
            String safeS = t.a.toStringBase(
                    t.b,
                    t.c,
                    min(DEFAULT_RESOLUTION, Rational.of(t.b).pow(-t.c).shiftRight(10))
            );
            if (head(safeS) == '~') {
                safeS = tail(safeS);
            }
            if (s.endsWith("...")) {
                s = take(s.length() - 3, s);
            }
            if (safeS.endsWith("...")) {
                safeS = take(safeS.length() - 3, safeS);
            }
            assertEquals(t, safeS, s);
            Real error = t.a.subtract(Rational.fromStringBase(s, t.b)).abs();
            assertTrue(t, error.ltUnsafe(of(t.b).powUnsafe(-t.c)));
        }

        String smallBaseChars = charsToString(
                concat(
                        Arrays.asList(
                                fromString("-."),
                                ExhaustiveProvider.INSTANCE.rangeIncreasing('0', '9'),
                                ExhaustiveProvider.INSTANCE.rangeIncreasing('A', 'Z')
                        )
                )
        );
        ts = map(
                (Pair<Pair<BigInteger, Integer>, Real> p) -> new Triple<>(p.b, p.a.a, p.a.b),
                P.dependentPairsInfinite(
                        P.pairs(
                                P.range(IntegerUtils.TWO, ASCII_ALPHANUMERIC_COUNT),
                                P.withScale(16).integersGeometric()
                        ),
                        q -> {
                            Rational multiplier = Rational.of(q.a).pow(q.b);
                            Iterable<Rational> rs = filterInfinite(
                                    r -> !r.multiply(multiplier).isInteger(),
                                    P.rationals()
                            );
                            return P.withScale(1).choose(
                                    map(Algebraic::realValue, P.withElement(Algebraic.ZERO, P.positiveAlgebraics())),
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
            String s = t.a.toStringBaseUnsafe(t.b, t.c);
            assertTrue(t, all(c -> elem(c, smallBaseChars), s));
        }

        String largeBaseChars = charsToString(
                concat(fromString("-.()"), ExhaustiveProvider.INSTANCE.rangeIncreasing('0', '9'))
        );
        //noinspection Convert2MethodRef
        ts = map(
                (Pair<Pair<BigInteger, Integer>, Real> p) -> new Triple<>(p.b, p.a.a, p.a.b),
                P.dependentPairsInfinite(
                        P.pairs(
                                map(
                                        i -> BigInteger.valueOf(i),
                                        P.withScale(64).rangeUpGeometric(ASCII_ALPHANUMERIC_COUNT.intValueExact() + 1)
                                ),
                                P.withScale(16).integersGeometric()
                        ),
                        q -> {
                            Rational multiplier = Rational.of(q.a).pow(q.b);
                            Iterable<Rational> rs = filterInfinite(
                                    r -> !r.multiply(multiplier).isInteger(),
                                    P.rationals()
                            );
                            return P.withScale(1).choose(
                                    map(Algebraic::realValue, P.withElement(Algebraic.ZERO, P.positiveAlgebraics())),
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
            String s = t.a.toStringBaseUnsafe(t.b, t.c);
            assertTrue(t, all(c -> elem(c, largeBaseChars), s));
        }

        Iterable<Triple<Real, BigInteger, Integer>> tsFail = map(
                (Pair<Pair<BigInteger, Integer>, Real> p) -> new Triple<>(p.b, p.a.a, p.a.b),
                P.dependentPairsInfinite(
                        P.pairs(P.rangeDown(BigInteger.ONE), P.withScale(16).integersGeometric()),
                        q -> P.cleanReals()
                )
        );
        for (Triple<Real, BigInteger, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.toStringBaseUnsafe(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesToStringBase() {
        initialize("toStringBase(BigInteger, int, Rational)");
        //noinspection Convert2MethodRef
        Iterable<Quadruple<Real, BigInteger, Integer, Rational>> qs = map(
                p -> new Quadruple<>(p.a.a, p.a.b, p.a.c, p.b),
                P.dependentPairsInfiniteIdentityHash(
                        P.triples(
                                P.reals(),
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
            q.a.toStringBase(q.b, q.c, q.d);
        }

        //noinspection Convert2MethodRef
        Iterable<Quadruple<Real, BigInteger, Integer, Rational>> qsFail = map(
                p -> new Quadruple<>(p.a.a, p.a.b, p.a.c, p.b),
                P.dependentPairsInfiniteIdentityHash(
                        P.triples(P.reals(), P.rangeDown(BigInteger.ONE), P.withScale(16).integersGeometric()),
                        t -> t.b.signum() != 1 ?
                                P.rationals() :
                                filterInfinite(
                                        x -> x != Rational.ZERO,
                                        P.range(Rational.ZERO, Rational.of(t.b).pow(-t.c).shiftRight(1))
                                )
                )
        );
        for (Quadruple<Real, BigInteger, Integer, Rational> q : take(LIMIT, qsFail)) {
            try {
                q.a.toStringBase(q.b, q.c, q.d);
                fail(q);
            } catch (IllegalArgumentException ignored) {}
        }

        //noinspection Convert2MethodRef
        qsFail = filterInfinite(
                q -> q.d.signum() != 1 || Ordering.gt(q.d, Rational.of(q.b).pow(-q.c).shiftRight(1)),
                P.quadruples(
                        P.reals(),
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                        P.withScale(16).integersGeometric(),
                        P.rationals()
                )
        );
        for (Quadruple<Real, BigInteger, Integer, Rational> q : take(LIMIT, qsFail)) {
            try {
                q.a.toStringBase(q.b, q.c, q.d);
                fail(q);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        for (Pair<Real, Real> p : take(LIMIT, P.pairs(P.reals()))) {
            try {
                //noinspection ResultOfMethodCallIgnored
                p.a.equals(p.b);
                fail(p);
            } catch (UnsupportedOperationException ignored) {}
        }

        for (Real x : take(LIMIT, P.reals())) {
            try {
                //noinspection ObjectEqualsNull,ResultOfMethodCallIgnored
                x.equals(null);
                fail(x);
            } catch (UnsupportedOperationException ignored) {}
        }
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        for (Real x : take(LIMIT, P.reals())) {
            try {
                //noinspection ResultOfMethodCallIgnored
                x.hashCode();
                fail(x);
            } catch (UnsupportedOperationException ignored) {}
        }
    }

    private void propertiesCompareToUnsafe_Rational() {
        initialize("compareToUnsafe(Rational)");
        //noinspection RedundantCast
        Iterable<Pair<Real, Rational>> ps = map(
                p -> new Pair<>(p.b, p.a),
                (Iterable<Pair<Rational, Real>>) P.dependentPairsInfinite(
                        P.rationals(),
                        r -> {
                            Iterable<Rational> rs = filterInfinite(s -> !r.equals(s), P.rationals());
                            return P.withScale(1).choose(
                                    map(Algebraic::realValue, P.algebraics()),
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
            int c = p.a.compareToUnsafe(p.b);
            assertTrue(p, c == -1 || c == 0 || c == 1);
            assertEquals(c, p.a.compareToUnsafe(of(p.b)), c);
        }
    }

    private void propertiesCompareTo_Rational_Rational() {
        initialize("compareTo(Rational, Rational)");
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(P.reals(), P.rationals(), P.positiveRationals());
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            Optional<Integer> oc = t.a.compareTo(t.b, t.c);
            assertEquals(t, t.a.compareTo(of(t.b), t.c), oc);
            oc.ifPresent(integer -> assertEquals(t, t.a.compareToUnsafe(t.b), integer));
        }

        Iterable<Triple<Real, Rational, Rational>> tsFail = P.triples(
                P.reals(),
                P.rationals(),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.compareTo(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesEqUnsafe_Rational() {
        initialize("eqUnsafe(Rational)");
        //noinspection RedundantCast
        Iterable<Pair<Real, Rational>> ps = map(
                p -> new Pair<>(p.b, p.a),
                (Iterable<Pair<Rational, Real>>) P.dependentPairsInfinite(
                        P.rationals(),
                        r -> {
                            Iterable<Rational> rs = filterInfinite(s -> !r.equals(s), P.rationals());
                            return P.withScale(1).choose(
                                    map(Algebraic::realValue, P.algebraics()),
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
            boolean b = p.a.eqUnsafe(p.b);
            assertEquals(p, p.a.eqUnsafe(of(p.b)), b);
        }
    }

    private void propertiesNeUnsafe_Rational() {
        initialize("neUnsafe(Rational)");
        //noinspection RedundantCast
        Iterable<Pair<Real, Rational>> ps = map(
                p -> new Pair<>(p.b, p.a),
                (Iterable<Pair<Rational, Real>>) P.dependentPairsInfinite(
                        P.rationals(),
                        r -> {
                            Iterable<Rational> rs = filterInfinite(s -> !r.equals(s), P.rationals());
                            return P.withScale(1).choose(
                                    map(Algebraic::realValue, P.algebraics()),
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
            boolean b = p.a.neUnsafe(p.b);
            assertEquals(p, p.a.neUnsafe(of(p.b)), b);
        }
    }

    private void propertiesLtUnsafe_Rational() {
        initialize("ltUnsafe(Rational)");
        Iterable<Pair<Real, Rational>> ps = filterInfinite(
                p -> p.a.lt(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.withScale(4).reals(), P.withScale(4).rationals())
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            boolean b = p.a.ltUnsafe(p.b);
            assertEquals(p, p.a.ltUnsafe(of(p.b)), b);
        }
    }

    private void propertiesGtUnsafe_Rational() {
        initialize("gtUnsafe(Rational)");
        Iterable<Pair<Real, Rational>> ps = filterInfinite(
                p -> p.a.gt(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.withScale(4).reals(), P.withScale(4).rationals())
        );
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            boolean b = p.a.gtUnsafe(p.b);
            assertEquals(p, p.a.gtUnsafe(of(p.b)), b);
        }
    }

    private void propertiesLeUnsafe_Rational() {
        initialize("leUnsafe(Rational)");
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
            boolean b = p.a.leUnsafe(p.b);
            assertEquals(p, p.a.leUnsafe(of(p.b)), b);
        }
    }

    private void propertiesGeUnsafe_Rational() {
        initialize("geUnsafe(Rational)");
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
            boolean b = p.a.geUnsafe(p.b);
            assertEquals(p, p.a.geUnsafe(of(p.b)), b);
        }
    }

    private void propertiesEq_Rational_Rational() {
        initialize("eq(Rational, Rational)");
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(P.reals(), P.rationals(), P.positiveRationals());
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            Optional<Boolean> ob = t.a.eq(t.b, t.c);
            assertEquals(t, t.a.eq(of(t.b), t.c), ob);
            ob.ifPresent(aBoolean -> assertEquals(t, t.a.eqUnsafe(t.b), aBoolean));
        }

        Iterable<Triple<Real, Rational, Rational>> tsFail = P.triples(
                P.reals(),
                P.rationals(),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.eq(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesNe_Rational_Rational() {
        initialize("ne(Rational, Rational)");
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(P.reals(), P.rationals(), P.positiveRationals());
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            Optional<Boolean> ob = t.a.ne(t.b, t.c);
            assertEquals(t, t.a.ne(of(t.b), t.c), ob);
            ob.ifPresent(aBoolean -> assertEquals(t, t.a.neUnsafe(t.b), aBoolean));
        }

        Iterable<Triple<Real, Rational, Rational>> tsFail = P.triples(
                P.reals(),
                P.rationals(),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.ne(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesLt_Rational_Rational() {
        initialize("lt(Rational, Rational)");
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(P.reals(), P.rationals(), P.positiveRationals());
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            Optional<Boolean> ob = t.a.lt(t.b, t.c);
            assertEquals(t, t.a.lt(of(t.b), t.c), ob);
            ob.ifPresent(aBoolean -> assertEquals(t, t.a.ltUnsafe(t.b), aBoolean));
        }

        Iterable<Triple<Real, Rational, Rational>> tsFail = P.triples(
                P.reals(),
                P.rationals(),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.lt(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesGt_Rational_Rational() {
        initialize("gt(Rational, Rational)");
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(P.reals(), P.rationals(), P.positiveRationals());
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            Optional<Boolean> ob = t.a.gt(t.b, t.c);
            assertEquals(t, t.a.gt(of(t.b), t.c), ob);
            ob.ifPresent(aBoolean -> assertEquals(t, t.a.gtUnsafe(t.b), aBoolean));
        }

        Iterable<Triple<Real, Rational, Rational>> tsFail = P.triples(
                P.reals(),
                P.rationals(),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.gt(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesLe_Rational_Rational() {
        initialize("le(Rational, Rational)");
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(P.reals(), P.rationals(), P.positiveRationals());
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            Optional<Boolean> ob = t.a.le(t.b, t.c);
            assertEquals(t, t.a.le(of(t.b), t.c), ob);
            ob.ifPresent(aBoolean -> assertEquals(t, t.a.leUnsafe(t.b), aBoolean));
        }

        Iterable<Triple<Real, Rational, Rational>> tsFail = P.triples(
                P.reals(),
                P.rationals(),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.le(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesGe_Rational_Rational() {
        initialize("ge(Rational, Rational)");
        Iterable<Triple<Real, Rational, Rational>> ts = P.triples(P.reals(), P.rationals(), P.positiveRationals());
        for (Triple<Real, Rational, Rational> t : take(LIMIT, ts)) {
            Optional<Boolean> ob = t.a.ge(t.b, t.c);
            assertEquals(t, t.a.ge(of(t.b), t.c), ob);
            ob.ifPresent(aBoolean -> assertEquals(t, t.a.geUnsafe(t.b), aBoolean));
        }

        Iterable<Triple<Real, Rational, Rational>> tsFail = P.triples(
                P.reals(),
                P.rationals(),
                P.rangeDown(Rational.ZERO)
        );
        for (Triple<Real, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.ge(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesCompareToUnsafe_Real() {
        initialize("compareToUnsafe(Real)");
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.eq(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            int compare = p.a.compareToUnsafe(p.b);
            assertTrue(p, compare == 0 || compare == 1 || compare == -1);
            assertEquals(p, p.b.compareToUnsafe(p.a), -compare);
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.compareToUnsafe(x) == 0);
        }

        Iterable<Triple<Real, Real, Real>> ts = filterInfinite(
                t -> t.a.eq(t.b, DEFAULT_RESOLUTION).isPresent() && t.b.eq(t.c, DEFAULT_RESOLUTION).isPresent(),
                P.triples(P.reals())
        );
        for (Triple<Real, Real, Real> t : take(LIMIT, ts)) {
            if (t.a.compareToUnsafe(t.b) <= 0 && t.b.compareToUnsafe(t.c) <= 0) {
                assertTrue(t, t.a.compareToUnsafe(t.c) <= 0);
            }
        }
    }

    private void propertiesCompareTo_Real_Rational() {
        initialize("compareTo(Real, Rational)");
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(P.reals(), P.reals(), P.positiveRationals());
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            Optional<Integer> oc = t.a.compareTo(t.b, t.c);
            oc.ifPresent(integer -> assertEquals(t, t.a.compareToUnsafe(t.b), integer));
        }

        Iterable<Triple<Real, Real, Rational>> tsFail = P.triples(P.reals(), P.reals(), P.rangeDown(Rational.ZERO));
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.compareTo(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesEqUnsafe_Real() {
        initialize("eqUnsafe(Real)");
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.eq(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            boolean b = p.a.eqUnsafe(p.b);
            assertEquals(p, b, !p.a.ltUnsafe(p.b) && !p.a.gtUnsafe(p.b));
            commutative(Real::eqUnsafe, p);
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.eqUnsafe(x));
        }
    }

    private void propertiesNeUnsafe_Real() {
        initialize("neUnsafe(Real)");
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.eq(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            boolean b = p.a.neUnsafe(p.b);
            assertEquals(p, b, p.a.ltUnsafe(p.b) || p.a.gtUnsafe(p.b));
            commutative(Real::neUnsafe, p);
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertFalse(x, x.neUnsafe(x));
        }
    }

    private void propertiesLtUnsafe_Real() {
        initialize("ltUnsafe(Real)");
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.eq(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            boolean b = p.a.ltUnsafe(p.b);
            assertEquals(p, b, !p.a.eqUnsafe(p.b) && !p.a.gtUnsafe(p.b));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertFalse(x, x.ltUnsafe(x));
        }
    }

    private void propertiesGtUnsafe_Real() {
        initialize("gtUnsafe(Real)");
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.eq(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            boolean b = p.a.gtUnsafe(p.b);
            assertEquals(p, b, !p.a.eqUnsafe(p.b) && !p.a.ltUnsafe(p.b));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertFalse(x, x.gtUnsafe(x));
        }
    }

    private void propertiesLeUnsafe_Real() {
        initialize("leUnsafe(Real)");
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.le(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            boolean b = p.a.leUnsafe(p.b);
            assertEquals(p, b, p.b.geUnsafe(p.a));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.leUnsafe(x));
        }
    }

    private void propertiesGeUnsafe_Real() {
        initialize("geUnsafe(Real)");
        Iterable<Pair<Real, Real>> ps = filterInfinite(
                p -> p.a.ge(p.b, DEFAULT_RESOLUTION).isPresent(),
                P.pairs(P.reals())
        );
        for (Pair<Real, Real> p : take(LIMIT, ps)) {
            boolean b = p.a.geUnsafe(p.b);
            assertEquals(p, b, p.b.leUnsafe(p.a));
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.geUnsafe(x));
        }
    }

    private void propertiesEq_Real_Rational() {
        initialize("eq(Real, Rational)");
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(P.reals(), P.reals(), P.positiveRationals());
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            Optional<Boolean> oc = t.a.eq(t.b, t.c);
            oc.ifPresent(aBoolean -> assertEquals(t, t.a.eqUnsafe(t.b), aBoolean));
        }

        Iterable<Triple<Real, Real, Rational>> tsFail = P.triples(P.reals(), P.reals(), P.rangeDown(Rational.ZERO));
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.eq(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesNe_Real_Rational() {
        initialize("ne(Real, Rational)");
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(P.reals(), P.reals(), P.positiveRationals());
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            Optional<Boolean> oc = t.a.ne(t.b, t.c);
            oc.ifPresent(aBoolean -> assertEquals(t, t.a.neUnsafe(t.b), aBoolean));
        }

        Iterable<Triple<Real, Real, Rational>> tsFail = P.triples(P.reals(), P.reals(), P.rangeDown(Rational.ZERO));
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.ne(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesLt_Real_Rational() {
        initialize("lt(Real, Rational)");
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(P.reals(), P.reals(), P.positiveRationals());
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            Optional<Boolean> oc = t.a.lt(t.b, t.c);
            oc.ifPresent(aBoolean -> assertEquals(t, t.a.ltUnsafe(t.b), aBoolean));
        }

        Iterable<Triple<Real, Real, Rational>> tsFail = P.triples(P.reals(), P.reals(), P.rangeDown(Rational.ZERO));
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.lt(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesGt_Real_Rational() {
        initialize("gt(Real, Rational)");
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(P.reals(), P.reals(), P.positiveRationals());
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            Optional<Boolean> oc = t.a.gt(t.b, t.c);
            oc.ifPresent(aBoolean -> assertEquals(t, t.a.gtUnsafe(t.b), aBoolean));
        }

        Iterable<Triple<Real, Real, Rational>> tsFail = P.triples(P.reals(), P.reals(), P.rangeDown(Rational.ZERO));
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.gt(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesLe_Real_Rational() {
        initialize("le(Real, Rational)");
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(P.reals(), P.reals(), P.positiveRationals());
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            Optional<Boolean> oc = t.a.le(t.b, t.c);
            oc.ifPresent(aBoolean -> assertEquals(t, t.a.leUnsafe(t.b), aBoolean));
        }

        Iterable<Triple<Real, Real, Rational>> tsFail = P.triples(P.reals(), P.reals(), P.rangeDown(Rational.ZERO));
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.le(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesGe_Real_Rational() {
        initialize("ge(Real, Rational)");
        Iterable<Triple<Real, Real, Rational>> ts = P.triples(P.reals(), P.reals(), P.positiveRationals());
        for (Triple<Real, Real, Rational> t : take(LIMIT, ts)) {
            Optional<Boolean> oc = t.a.ge(t.b, t.c);
            oc.ifPresent(aBoolean -> assertEquals(t, t.a.geUnsafe(t.b), aBoolean));
        }

        Iterable<Triple<Real, Real, Rational>> tsFail = P.triples(P.reals(), P.reals(), P.rangeDown(Rational.ZERO));
        for (Triple<Real, Real, Rational> t : take(LIMIT, tsFail)) {
            try {
                t.a.ge(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesToString() {
        initialize("toString()");
        for (Real x : take(LIMIT, P.reals())) {
            String s = x.toString();
            assertTrue(x, isSubsetOf(s, REAL_CHARS));
        }
    }
}
