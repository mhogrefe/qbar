package mho.qbar.objects;

import mho.qbar.testing.QBarTestProperties;
import mho.wheels.iterables.CachedIterator;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
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
        propertiesIsExact();
        propertiesRationalValue();
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
    }

    private void propertiesOf_Rational() {
        initialize("of(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Real x = of(r);
            x.validate();
            assertTrue(r, x.isExact());
            inverse(Real::of, (Real y) -> y.rationalValue().get(), r);
        }
    }

    private void propertiesOf_BigInteger() {
        initialize("of(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Real x = of(i);
            x.validate();
            assertTrue(i, x.rationalValue().get().isInteger());
        }
    }

    private void propertiesOf_long() {
        initialize("of(long)");
        Rational lowerLimit = Rational.of(Long.MIN_VALUE);
        Rational upperLimit = Rational.of(Long.MAX_VALUE);
        for (long l : take(LIMIT, P.longs())) {
            Real x = of(l);
            x.validate();
            assertTrue(l, x.rationalValue().get().isInteger());
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
            assertTrue(i, x.rationalValue().get().isInteger());
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
                    of(bf.getMantissa()).multiply(Rational.ONE.shiftLeft(bf.getExponent())).rationalValue().get(),
                    x.rationalValue().get()
            );
            assertTrue(bf, x.rationalValue().get().isBinaryFraction());
            inverse(Algebraic::of, Algebraic::binaryFractionValueExact, bf);
        }
    }

    private void propertiesOf_float() {
        initialize("of(float)");
        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            Real x = of(f).get();
            x.validate();
            assertTrue(f, x.rationalValue().get().hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            Real x = of(f).get();
            aeqf(f, f, x.floatValue());
            assertTrue(f, eq(new BigDecimal(Float.toString(f)), x.rationalValue().get().bigDecimalValueExact()));
        }
    }

    private void propertiesOf_double() {
        initialize("of(double)");
        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            Real x = of(d).get();
            x.validate();
            assertTrue(d, x.rationalValue().get().hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            Real x = of(d).get();
            aeqd(d, d, x.doubleValue());
            assertTrue(d, eq(new BigDecimal(Double.toString(d)), x.rationalValue().get().bigDecimalValueExact()));
        }
    }

    private void propertiesOfExact_float() {
        initialize("ofExact(float)");
        for (float f : take(LIMIT, P.floats())) {
            Optional<Real> ox = ofExact(f);
            assertEquals(f, Float.isFinite(f), ox.isPresent());
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            inverse(g -> ofExact(g).get(), (Real x) -> x.rationalValue().get().floatValueExact(), f);
        }

        int x = 1 << (FLOAT_EXPONENT_WIDTH - 1);
        BigInteger y = BigInteger.ONE.shiftLeft(-MIN_SUBNORMAL_FLOAT_EXPONENT);
        BigInteger z = BigInteger.ONE.shiftLeft(x).subtract(BigInteger.ONE.shiftLeft(x - FLOAT_FRACTION_WIDTH - 1));
        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            Real a = ofExact(f).get();
            a.validate();
            Rational r = a.rationalValue().get();
            assertTrue(f, IntegerUtils.isPowerOfTwo(r.getDenominator()));
            assertTrue(f, le(r.getDenominator(), y));
            assertTrue(f, le(r.getNumerator(), z));
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            Real a = ofExact(f).get();
            aeqf(f, f, a.floatValue());
        }
    }

    private void propertiesOfExact_double() {
        initialize("ofExact(double)");
        for (double d : take(LIMIT, P.doubles())) {
            Optional<Real> ox = ofExact(d);
            assertEquals(d, Double.isFinite(d), ox.isPresent());
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            inverse(e -> ofExact(e).get(), (Real x) -> x.rationalValue().get().doubleValueExact(), d);
        }

        int x = 1 << (DOUBLE_EXPONENT_WIDTH - 1);
        BigInteger y = BigInteger.ONE.shiftLeft(-MIN_SUBNORMAL_DOUBLE_EXPONENT);
        BigInteger z = BigInteger.ONE.shiftLeft(x).subtract(BigInteger.ONE.shiftLeft(x - DOUBLE_FRACTION_WIDTH - 1));
        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            Real a = ofExact(d).get();
            a.validate();
            Rational r = a.rationalValue().get();
            assertTrue(d, IntegerUtils.isPowerOfTwo(r.getDenominator()));
            assertTrue(d, le(r.getDenominator(), y));
            assertTrue(d, le(r.getNumerator(), z));
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            Real a = ofExact(d).get();
            aeqd(d, d, a.doubleValue());
        }
    }

    private void propertiesOf_BigDecimal() {
        initialize("of(BigDecimal)");
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            Real x = of(bd);
            x.validate();
            assertTrue(bd, eq(bd, x.rationalValue().get().bigDecimalValueExact()));
            assertTrue(bd, x.rationalValue().get().hasTerminatingBaseExpansion(BigInteger.TEN));
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

    private void propertiesIsExact() {
        initialize("isExact()");
        for (Real x : take(LIMIT, P.reals())) {
            x.isExact();
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(r, of(r).isExact());
        }
    }

    private void propertiesRationalValue() {
        initialize("rationalValue()");
        for (Real x : take(LIMIT, P.reals())) {
            x.rationalValue();
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            Real x = of(r);
            assertTrue(r, x.rationalValue().isPresent());
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

    private static boolean rmCheck(@NotNull Algebraic x, @NotNull RoundingMode rm, @NotNull FuzzinessType ft) {
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
                        p -> rmCheck(p.a.a, p.b, p.a.b),
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
                        p -> rmCheck(p.a, RoundingMode.HALF_EVEN, p.b),
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
                        p -> rmCheck(p.a, RoundingMode.FLOOR, p.b),
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
                        p -> rmCheck(p.a, RoundingMode.CEILING, p.b),
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
}
