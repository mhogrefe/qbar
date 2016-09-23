package mho.qbar.objects;

import mho.qbar.testing.QBarTestProperties;
import mho.wheels.iterables.CachedIterator;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.BigDecimalUtils;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
            assertTrue(l, geUnsafe(x, lowerLimit));
            assertTrue(l, leUnsafe(x, upperLimit));
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
            assertTrue(i, geUnsafe(x, lowerLimit));
            assertTrue(i, leUnsafe(x, upperLimit));
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
            inverse(Algebraic::of, Algebraic::binaryFractionValueExact, bf);
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
            inverse(Algebraic::of, Algebraic::bigDecimalValueExact, bd);
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
                P.dependentPairs(
                        P.distinctListsAtLeast(1, P.naturalIntegersGeometric()),
                        P::uniformSample
                )
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
            assertTrue(p, ltUnsafe(p.a.subtract(of(rounded)).abs(), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).bigIntegerValueUnsafe(RoundingMode.UNNECESSARY), i);
        }

        for (Real x : take(LIMIT, P.cleanReals())) {
            assertEquals(x, x.bigIntegerValueUnsafe(RoundingMode.FLOOR), x.floorUnsafe());
            assertEquals(x, x.bigIntegerValueUnsafe(RoundingMode.CEILING), x.ceilingUnsafe());
            assertTrue(x, leUnsafe(of(x.bigIntegerValueUnsafe(RoundingMode.DOWN)).abs(), x.abs()));
            assertTrue(x, geUnsafe(of(x.bigIntegerValueUnsafe(RoundingMode.UP)).abs(), x.abs()));
            assertTrue(x, leUnsafe(x.subtract(of(x.bigIntegerValueUnsafe(RoundingMode.HALF_DOWN))).abs(), ONE_HALF));
            assertTrue(x, leUnsafe(x.subtract(of(x.bigIntegerValueUnsafe(RoundingMode.HALF_UP))).abs(), ONE_HALF));
            assertTrue(x, leUnsafe(x.subtract(of(x.bigIntegerValueUnsafe(RoundingMode.HALF_EVEN))).abs(), ONE_HALF));
        }

        Iterable<Real> xs = filterInfinite(s -> ltUnsafe(s.abs().fractionalPartUnsafe(), ONE_HALF), P.cleanReals());
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

        xs = filterInfinite(s -> gtUnsafe(s.abs().fractionalPartUnsafe(), ONE_HALF), P.cleanReals());
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
            if (oi.isPresent()) {
                assertEquals(t, t.a.bigIntegerValueUnsafe(t.b), oi.get());
            }
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
                P.triples(
                        P.withScale(4).reals(),
                        P.roundingModes(),
                        P.withElement(Rational.ZERO, P.withScale(4).negativeRationals())
                )
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
            assertTrue(x, ltUnsafe(x.subtract(of(rounded)).abs(), ONE));
            assertTrue(x, leUnsafe(x.subtract(of(rounded)).abs(), ONE_HALF));
        }

        xs = filterInfinite(s -> ltUnsafe(s.abs().fractionalPartUnsafe(), ONE_HALF), P.cleanReals());
        for (Real x : take(LIMIT, xs)) {
            assertEquals(
                    x,
                    x.bigIntegerValueUnsafe(),
                    x.bigIntegerValueUnsafe(RoundingMode.DOWN)
            );
        }

        xs = filterInfinite(s -> gtUnsafe(s.abs().fractionalPartUnsafe(), ONE_HALF), P.cleanReals());
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
            if (oi.isPresent()) {
                assertEquals(p, p.a.bigIntegerValueUnsafe(), oi.get());
            }
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withScale(4).reals(),
                P.withElement(Rational.ZERO, P.withScale(4).negativeRationals())
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
            assertTrue(x, ltUnsafe(x.subtract(of(rounded)).abs(), ONE));
        }
    }

    private void propertiesFloor() {
        initialize("floor(Rational)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(P.withScale(4).reals(), P.withScale(4).positiveRationals());
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            Optional<BigInteger> oi = p.a.floor(p.b);
            if (oi.isPresent()) {
                assertEquals(p, p.a.floorUnsafe(), oi.get());
            }
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withScale(4).reals(),
                P.withElement(Rational.ZERO, P.withScale(4).negativeRationals())
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
            assertTrue(x, ltUnsafe(x.subtract(of(rounded)).abs(), ONE));
        }
    }

    private void propertiesCeiling() {
        initialize("ceiling(Rational)");
        Iterable<Pair<Real, Rational>> ps = P.pairs(P.withScale(4).reals(), P.withScale(4).positiveRationals());
        for (Pair<Real, Rational> p : take(LIMIT, ps)) {
            Optional<BigInteger> oi = p.a.ceiling(p.b);
            if (oi.isPresent()) {
                assertEquals(p, p.a.ceilingUnsafe(), oi.get());
            }
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withScale(4).reals(),
                P.withElement(Rational.ZERO, P.withScale(4).negativeRationals())
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
            assertTrue(x, leUnsafe(x, of(powerOfTwo)));
            //noinspection SuspiciousNameCombination
            assertTrue(x, ltUnsafe(of(powerOfTwo.shiftRight(1)), x));
        }

        for (Real x : take(LIMIT, P.withElement(ZERO, P.negativeCleanReals()))) {
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
            if (obf.isPresent()) {
                assertEquals(p, p.a.roundUpToIntegerPowerOfTwoUnsafe(), obf.get());
            }
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withScale(4).positiveReals(),
                P.withElement(Rational.ZERO, P.withScale(4).negativeRationals())
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
            Real power = ONE.shiftLeft(exponent);
            //noinspection SuspiciousNameCombination
            assertTrue(x, leUnsafe(power, x));
            assertTrue(x, leUnsafe(x, power.shiftLeft(1)));
        }

        for (Real x : take(LIMIT, P.withElement(ZERO, P.negativeCleanReals()))) {
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
            if (oi.isPresent()) {
                assertEquals(p, p.a.binaryExponentUnsafe(), oi.get());
            }
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withScale(4).positiveReals(),
                P.withElement(Rational.ZERO, P.withScale(4).negativeRationals())
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
            //noinspection SuspiciousNameCombination
            assertTrue(x, leUnsafe(ofExact(rounded).get(), x));
            //noinspection SuspiciousNameCombination
            assertTrue(x, gtUnsafe(ofExact(successor).get(), x));
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
            //noinspection SuspiciousNameCombination
            assertTrue(x, geUnsafe(ofExact(rounded).get(), x));
            //noinspection SuspiciousNameCombination
            assertTrue(x, ltUnsafe(ofExact(predecessor).get(), x));
            assertTrue(x, rounded > 0 || Float.isFinite(rounded));
        }

        xs = P.withScale(2).cleanRealsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT));
        for (Real x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValueUnsafe(RoundingMode.DOWN);
            assertTrue(x, leUnsafe(ofExact(rounded).get().abs(), x.abs()));
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
            assertTrue(x, ltUnsafe(ofExact(down).get().abs(), x.abs()));
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
            assertTrue(x, gtUnsafe(ofExact(up).get().abs(), x.abs()));
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
            float closest = ltUnsafe(belowDistance, aboveDistance) ? below : above;
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
            if (of.isPresent()) {
                assertEquals(t, t.a.floatValueUnsafe(t.b), of.get());
            }
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
                P.triples(
                        P.withScale(4).reals(),
                        P.roundingModes(),
                        P.withElement(Rational.ZERO, P.negativeRationals())
                )
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
            float closest = ltUnsafe(belowDistance, aboveDistance) ? below : above;
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
            if (of.isPresent()) {
                assertEquals(p, p.a.floatValueUnsafe(), of.get());
            }
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withScale(4).reals(),
                P.withElement(Rational.ZERO, P.negativeRationals())
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
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
            //noinspection SuspiciousNameCombination
            assertTrue(x, leUnsafe(ofExact(rounded).get(), x));
            //noinspection SuspiciousNameCombination
            assertTrue(x, gtUnsafe(ofExact(successor).get(), x));
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
            //noinspection SuspiciousNameCombination
            assertTrue(x, geUnsafe(ofExact(rounded).get(), x));
            //noinspection SuspiciousNameCombination
            assertTrue(x, ltUnsafe(ofExact(predecessor).get(), x));
            assertTrue(x, rounded > 0 || Double.isFinite(rounded));
        }

        xs = P.withScale(2).withSecondaryScale(4)
                .cleanRealsIn(Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE));
        for (Real x : take(TINY_LIMIT, xs)) {
            double rounded = x.doubleValueUnsafe(RoundingMode.DOWN);
            assertTrue(x, leUnsafe(ofExact(rounded).get().abs(), x.abs()));
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
            assertTrue(x, ltUnsafe(ofExact(down).get().abs(), x.abs()));
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
            assertTrue(x, gtUnsafe(ofExact(up).get().abs(), x.abs()));
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
            double closest = ltUnsafe(belowDistance, aboveDistance) ? below : above;
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
            if (od.isPresent()) {
                assertEquals(t, t.a.doubleValueUnsafe(t.b), od.get());
            }
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
                P.triples(
                        P.withScale(4).reals(),
                        P.roundingModes(),
                        P.withElement(Rational.ZERO, P.negativeRationals())
                )
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
            double closest = ltUnsafe(belowDistance, aboveDistance) ? below : above;
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
            if (od.isPresent()) {
                assertEquals(p, p.a.doubleValueUnsafe(), od.get());
            }
        }

        Iterable<Pair<Real, Rational>> psFail = P.pairs(
                P.withScale(4).reals(),
                P.withElement(Rational.ZERO, P.negativeRationals())
        );
        for (Pair<Real, Rational> p : take(LIMIT, psFail)) {
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
            BigDecimal closest = ltUnsafe(x.subtract(of(down)).abs(), x.subtract(of(up)).abs()) ? down : up;
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
            if (obd.isPresent()) {
                assertEquals(q, q.a.bigDecimalValueByPrecisionUnsafe(q.b, q.c), obd.get());
            }
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
                P.withElement(Rational.ZERO, P.negativeRationals())
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
            assertTrue(p, gtUnsafe(p.a, of(lowR)));
            assertTrue(p, ltUnsafe(p.a, of(highR)));
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
            BigDecimal closest = ltUnsafe(x.subtract(of(down)).abs(), x.subtract(of(up)).abs()) ? down : up;
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
            if (obd.isPresent()) {
                assertEquals(q, q.a.bigDecimalValueByScaleUnsafe(q.b, q.c), obd.get());
            }
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
                P.withElement(Rational.ZERO, P.negativeRationals())
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

        ps = filterInfinite(valid::test, P.pairsSquareRootOrder(P.nonzeroCleanReals(), P.positiveIntegersGeometric()));
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
            boolean closerToDown = ltUnsafe(x.subtract(of(down)).abs(), x.subtract(of(up)).abs());
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
            if (obd.isPresent()) {
                assertEquals(t, t.a.bigDecimalValueByPrecisionUnsafe(t.b), obd.get());
            }
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
                P.withElement(Rational.ZERO, P.negativeRationals())
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
            BigDecimal closer = ltUnsafe(x.subtract(of(down)).abs(), x.subtract(of(up)).abs()) ? down : up;
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
            if (obd.isPresent()) {
                assertEquals(t, t.a.bigDecimalValueByScaleUnsafe(t.b), obd.get());
            }
        }

        Iterable<Triple<Real, Integer, Rational>> tsFail = P.triples(
                P.withScale(4).reals(),
                P.integersGeometric(),
                P.withElement(Rational.ZERO, P.negativeRationals())
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
            Optional<Boolean> involution = eq(negative.negate(), x, DEFAULT_RESOLUTION);
            assertTrue(x, !involution.isPresent() || involution.get());
            Optional<Boolean> inverse = eq(x.add(negative), Rational.ZERO, DEFAULT_RESOLUTION);
            assertTrue(x, !inverse.isPresent() || inverse.get());
        }

        for (Real x : take(LIMIT, P.nonzeroReals())) {
            Optional<Boolean> unequal = ne(x, x.negate(), DEFAULT_RESOLUTION);
            assertTrue(x, !unequal.isPresent() || unequal.get());
        }
    }

    private void propertiesAbs() {
        initialize("abs()");
        for (Real x : take(LIMIT, P.reals())) {
            Real abs = x.abs();
            abs.validate();
            Optional<Boolean> idempotent = eq(abs, abs.abs(), DEFAULT_RESOLUTION);
            assertTrue(x, !idempotent.isPresent() || idempotent.get());
            Optional<Integer> signum = abs.signum(DEFAULT_RESOLUTION);
            assertTrue(x, !signum.isPresent() || signum.get() != -1);
            Optional<Boolean> nonNegative = ge(abs, Rational.ZERO, DEFAULT_RESOLUTION);
            assertTrue(x, !nonNegative.isPresent() || nonNegative.get());
        }

        for (Real x : take(LIMIT, P.positiveReals())) {
            //noinspection SuspiciousNameCombination
            Optional<Boolean> fixedPoint = eq(x.abs(), x, DEFAULT_RESOLUTION);
            assertTrue(x, !fixedPoint.isPresent() || fixedPoint.get());
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
            if (oi.isPresent()) {
                assertEquals(p, p.a.signumUnsafe(), oi.get());
            }
        }
    }

    private void propertiesAdd_Rational() {
        initialize("add(Rational)");
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.rationals()))) {
            Real sum = p.a.add(p.b);
            sum.validate();
            Optional<Boolean> inverse = eq(sum.subtract(p.b), p.a, DEFAULT_RESOLUTION);
            assertTrue(p, !inverse.isPresent() || inverse.get());
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
            Optional<Boolean> commutative = eq(p.b.add(p.a), sum, DEFAULT_RESOLUTION);
            assertTrue(p, !commutative.isPresent() || commutative.get());
            Optional<Boolean> inverse = eq(sum.subtract(p.b), p.a, DEFAULT_RESOLUTION);
            assertTrue(p, !inverse.isPresent() || inverse.get());
        }

        for (Real x : take(LIMIT, P.reals())) {
            assertTrue(x, x.add(ZERO) == x);
            assertTrue(x, ZERO.add(x) == x);
            Optional<Boolean> zero = eq(x.add(x.negate()), ZERO, DEFAULT_RESOLUTION);
            assertTrue(x, !zero.isPresent() || zero.get());
        }

        for (Triple<Real, Real, Real> t : take(SMALL_LIMIT, P.triples(P.reals()))) {
            Optional<Boolean> associative = eq(t.a.add(t.b).add(t.c), t.a.add(t.b.add(t.c)), DEFAULT_RESOLUTION);
            assertTrue(t, !associative.isPresent() || associative.get());
        }
    }

    private void propertiesSubtract_Rational() {
        initialize("subtract(Rational)");
        for (Pair<Real, Rational> p : take(LIMIT, P.pairs(P.reals(), P.rationals()))) {
            Real difference = p.a.subtract(p.b);
            difference.validate();
            Optional<Boolean> inverse = eq(difference.add(p.b), p.a, DEFAULT_RESOLUTION);
            assertTrue(p, !inverse.isPresent() || inverse.get());
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
            Optional<Boolean> anticommutative = eq(p.b.subtract(p.a), difference.negate(), DEFAULT_RESOLUTION);
            assertTrue(p, !anticommutative.isPresent() || anticommutative.get());
            Optional<Boolean> inverse = eq(difference.add(p.b), p.a, DEFAULT_RESOLUTION);
            assertTrue(p, !inverse.isPresent() || inverse.get());
        }

        for (Real x : take(LIMIT, P.reals())) {
            Optional<Boolean> negate = eq(ZERO.subtract(x), x.negate(), DEFAULT_RESOLUTION);
            assertTrue(x, !negate.isPresent() || negate.get());
            assertTrue(x, x.subtract(ZERO) == x);
            assertTrue(x, x.subtract(x) == ZERO);
        }
    }
}
