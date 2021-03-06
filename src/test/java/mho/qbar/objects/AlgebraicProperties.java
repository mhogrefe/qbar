package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.concurrency.ConcurrencyUtils;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.math.BinaryFraction;
import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.BigDecimalUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static mho.qbar.objects.Algebraic.*;
import static mho.qbar.objects.Algebraic.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.numberUtils.FloatingPointUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class AlgebraicProperties extends QBarTestProperties {
    private static final @NotNull String ALGEBRAIC_CHARS = " ()*+-/0123456789^foqrstx";
    private static final @NotNull BigInteger ASCII_ALPHANUMERIC_COUNT = BigInteger.valueOf(36);

    public AlgebraicProperties() {
        super("Algebraic");
    }

    @Override
    protected void testBothModes() {
        propertiesOf_Polynomial_int();
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
        propertiesIsInteger();
        propertiesBigIntegerValue_RoundingMode();
        propertiesBigIntegerValue();
        propertiesFloor();
        propertiesCeiling();
        propertiesBigIntegerValueExact();
        propertiesByteValueExact();
        propertiesShortValueExact();
        propertiesIntValueExact();
        propertiesLongValueExact();
        propertiesIsIntegerPowerOfTwo();
        propertiesRoundUpToIntegerPowerOfTwo();
        propertiesIsBinaryFraction();
        propertiesBinaryFractionValueExact();
        propertiesIsRational();
        propertiesIsAlgebraicInteger();
        propertiesRationalValueExact();
        propertiesRealValue();
        propertiesBinaryExponent();
        propertiesIsEqualToFloat();
        compareImplementationsIsEqualToFloat();
        propertiesIsEqualToDouble();
        compareImplementationsIsEqualToDouble();
        propertiesFloatValue_RoundingMode();
        propertiesFloatValue();
        propertiesFloatValueExact();
        propertiesDoubleValue_RoundingMode();
        propertiesDoubleValue();
        propertiesDoubleValueExact();
        propertiesHasTerminatingBaseExpansion();
        propertiesBigDecimalValueByPrecision_int_RoundingMode();
        propertiesBigDecimalValueByScale_int_RoundingMode();
        propertiesBigDecimalValueByPrecision_int();
        propertiesBigDecimalValueByScale_int();
        propertiesBigDecimalValueExact();
        propertiesMinimalPolynomial();
        propertiesRootIndex();
        propertiesDegree();
        propertiesIsolatingInterval();
        propertiesMinimalPolynomialRootCount();
        propertiesIntervalExtension();
        propertiesNegate();
        propertiesAbs();
        propertiesSignum();
        propertiesAdd_BigInteger();
        propertiesAdd_Rational();
        propertiesAdd_Algebraic();
        propertiesSubtract_BigInteger();
        propertiesSubtract_Rational();
        propertiesSubtract_Algebraic();
        propertiesMultiply_int();
        propertiesMultiply_BigInteger();
        propertiesMultiply_Rational();
        propertiesMultiply_Algebraic();
        propertiesInvert();
        propertiesDivide_int();
        propertiesDivide_BigInteger();
        propertiesDivide_Rational();
        propertiesDivide_Algebraic();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesShiftRight();
        compareImplementationsShiftRight();
        propertiesSum();
        compareImplementationsSum();
        propertiesProduct();
        compareImplementationsProduct();
        propertiesSumSign();
        compareImplementationsSumSign();
        propertiesDelta();
        propertiesPow_int();
        compareImplementationsPow_int();
        propertiesRootOfRational();
        propertiesSqrtOfRational();
        propertiesCbrtOfRational();
        propertiesRoot();
        propertiesSqrt();
        propertiesCbrt();
        propertiesPow_Rational();
        compareImplementationsPow_Rational();
        propertiesFractionalPart();
        propertiesRoundToDenominator();
        propertiesRealConjugates();
        propertiesIsReducedSurd();
        propertiesContinuedFraction();
        compareImplementationsIsContinuedFraction();
        propertiesRepeatedContinuedFraction();
        propertiesFromContinuedFraction();
        propertiesConvergents();
        propertiesDigits();
        propertiesCommonLeadingDigits();
        propertiesToStringBase();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesReadStrict();
        propertiesToString();
    }

    private void propertiesOf_Polynomial_int() {
        initialize("of(Polynomial, int)");
        Iterable<Pair<Polynomial, Integer>> ps = P.dependentPairs(
                filterInfinite(p -> p.rootCount() > 0, P.withScale(4).polynomialsAtLeast(1)),
                q -> P.range(0, q.rootCount() - 1)
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            Algebraic x = of(p.a, p.b);
            x.validate();
            assertTrue(p, x.degree() <= p.a.degree());
            assertTrue(p, x.rootIndex() <= p.b);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            assertEquals(x, x, of(x.minimalPolynomial(), x.rootIndex()));
        }

        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            try {
                of(Polynomial.ZERO, i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegersGeometric()))) {
            try {
                of(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(0), P.naturalIntegersGeometric()))) {
            try {
                of(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Polynomial, Integer>> psFail = P.pairs(
                filterInfinite(p -> p.rootCount() == 0, P.withScale(4).squareFreePolynomials()),
                P.naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                of(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesOf_Rational() {
        initialize("of(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Algebraic x = of(r);
            x.validate();
            assertTrue(r, x.isRational());
        }
    }

    private void propertiesOf_BigInteger() {
        initialize("of(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Algebraic x = of(i);
            x.validate();
            assertTrue(i, x.isInteger());
        }
    }

    private void propertiesOf_long() {
        initialize("of(long)");
        Algebraic lowerLimit = of(Long.MIN_VALUE);
        Algebraic upperLimit = of(Long.MAX_VALUE);
        for (long l : take(LIMIT, P.longs())) {
            Algebraic x = of(l);
            x.validate();
            assertTrue(l, x.isInteger());
            assertTrue(l, ge(x, lowerLimit));
            assertTrue(l, le(x, upperLimit));
        }
    }

    private void propertiesOf_int() {
        initialize("of(int)");
        Algebraic lowerLimit = of(Integer.MIN_VALUE);
        Algebraic upperLimit = of(Integer.MAX_VALUE);
        for (int i : take(LIMIT, P.integers())) {
            Algebraic x = of(i);
            x.validate();
            assertTrue(i, x.isInteger());
            assertTrue(i, ge(x, lowerLimit));
            assertTrue(i, le(x, upperLimit));
        }
    }

    private void propertiesOf_BinaryFraction() {
        initialize("of(BinaryFraction)");
        for (BinaryFraction bf : take(LIMIT, P.binaryFractions())) {
            Algebraic x = of(bf);
            x.validate();
            assertEquals(bf, of(bf.getMantissa()).multiply(ONE.shiftLeft(bf.getExponent())), x);
            assertTrue(bf, x.isBinaryFraction());
            inverse(Algebraic::of, Algebraic::binaryFractionValueExact, bf);
        }
    }

    private void propertiesOf_float() {
        initialize("of(float)");
        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            Algebraic x = of(f).get();
            x.validate();
            assertTrue(f, x.hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            Algebraic x = of(f).get();
            aeqf(f, f, x.floatValue());
            assertTrue(f, eq(new BigDecimal(Float.toString(f)), x.bigDecimalValueExact()));
        }
    }

    private void propertiesOf_double() {
        initialize("of(double)");
        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            Algebraic x = of(d).get();
            x.validate();
            assertTrue(d, x.hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            Algebraic x = of(d).get();
            aeqd(d, d, x.doubleValue());
            assertTrue(d, eq(new BigDecimal(Double.toString(d)), x.bigDecimalValueExact()));
        }
    }

    private void propertiesOfExact_float() {
        initialize("ofExact(float)");
        for (float f : take(LIMIT, P.floats())) {
            Optional<Algebraic> ox = ofExact(f);
            assertEquals(f, Float.isFinite(f), ox.isPresent());
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            inverse(g -> ofExact(g).get(), Algebraic::floatValueExact, f);
        }

        int x = 1 << (FLOAT_EXPONENT_WIDTH - 1);
        BigInteger y = BigInteger.ONE.shiftLeft(-MIN_SUBNORMAL_FLOAT_EXPONENT);
        BigInteger z = BigInteger.ONE.shiftLeft(x).subtract(BigInteger.ONE.shiftLeft(x - FLOAT_FRACTION_WIDTH - 1));
        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            Algebraic a = ofExact(f).get();
            a.validate();
            Rational r = a.rationalValueExact();
            assertTrue(f, IntegerUtils.isPowerOfTwo(r.getDenominator()));
            assertTrue(f, le(r.getDenominator(), y));
            assertTrue(f, le(r.getNumerator(), z));
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            Algebraic a = ofExact(f).get();
            aeqf(f, f, a.floatValue());
        }
    }

    private void propertiesOfExact_double() {
        initialize("ofExact(double)");
        for (double d : take(LIMIT, P.doubles())) {
            Optional<Algebraic> ox = ofExact(d);
            assertEquals(d, Double.isFinite(d), ox.isPresent());
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            inverse(e -> ofExact(e).get(), Algebraic::doubleValueExact, d);
        }

        int x = 1 << (DOUBLE_EXPONENT_WIDTH - 1);
        BigInteger y = BigInteger.ONE.shiftLeft(-MIN_SUBNORMAL_DOUBLE_EXPONENT);
        BigInteger z = BigInteger.ONE.shiftLeft(x).subtract(BigInteger.ONE.shiftLeft(x - DOUBLE_FRACTION_WIDTH - 1));
        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            Algebraic a = ofExact(d).get();
            a.validate();
            Rational r = a.rationalValueExact();
            assertTrue(d, IntegerUtils.isPowerOfTwo(r.getDenominator()));
            assertTrue(d, le(r.getDenominator(), y));
            assertTrue(d, le(r.getNumerator(), z));
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            Algebraic a = ofExact(d).get();
            aeqd(d, d, a.doubleValue());
        }
    }

    private void propertiesOf_BigDecimal() {
        initialize("of(BigDecimal)");
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            Algebraic x = of(bd);
            x.validate();
            assertTrue(bd, eq(bd, x.bigDecimalValueExact()));
            assertTrue(bd, x.hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (BigDecimal bd : take(LIMIT, P.canonicalBigDecimals())) {
            inverse(Algebraic::of, Algebraic::bigDecimalValueExact, bd);
        }
    }

    private void propertiesIsInteger() {
        initialize("isInteger()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            assertEquals(x, x.isInteger(), of(x.floor()).equals(x));
            homomorphic(Algebraic::negate, Function.identity(), Algebraic::isInteger, Algebraic::isInteger, x);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertTrue(i, of(i).isInteger());
        }
    }

    private void propertiesBigIntegerValue_RoundingMode() {
        initialize("bigIntegerValue(RoundingMode)");
        Iterable<Pair<Algebraic, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isInteger(),
                P.pairs(P.algebraics(), P.roundingModes())
        );
        for (Pair<Algebraic, RoundingMode> p : take(LIMIT, ps)) {
            BigInteger rounded = p.a.bigIntegerValue(p.b);
            assertTrue(p, rounded.equals(BigInteger.ZERO) || rounded.signum() == p.a.signum());
            assertTrue(p, lt(p.a.subtract(of(rounded)).abs(), ONE));
            assertEquals(p, p.a.realValue().bigIntegerValueUnsafe(p.b), rounded);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).bigIntegerValue(RoundingMode.UNNECESSARY), i);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            assertEquals(x, x.bigIntegerValue(RoundingMode.FLOOR), x.floor());
            assertEquals(x, x.bigIntegerValue(RoundingMode.CEILING), x.ceiling());
            assertTrue(x, le(of(x.bigIntegerValue(RoundingMode.DOWN)).abs(), x.abs()));
            assertTrue(x, ge(of(x.bigIntegerValue(RoundingMode.UP)).abs(), x.abs()));
            assertTrue(x, le(x.subtract(of(x.bigIntegerValue(RoundingMode.HALF_DOWN))).abs(), ONE_HALF));
            assertTrue(x, le(x.subtract(of(x.bigIntegerValue(RoundingMode.HALF_UP))).abs(), ONE_HALF));
            assertTrue(x, le(x.subtract(of(x.bigIntegerValue(RoundingMode.HALF_EVEN))).abs(), ONE_HALF));
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> lt(s.abs().fractionalPart(), ONE_HALF), P.algebraics()))) {
            assertEquals(x, x.bigIntegerValue(RoundingMode.HALF_DOWN), x.bigIntegerValue(RoundingMode.DOWN));
            assertEquals(x, x.bigIntegerValue(RoundingMode.HALF_UP), x.bigIntegerValue(RoundingMode.DOWN));
            assertEquals(x, x.bigIntegerValue(RoundingMode.HALF_EVEN), x.bigIntegerValue(RoundingMode.DOWN));
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> gt(s.abs().fractionalPart(), ONE_HALF), P.algebraics()))) {
            assertEquals(x, x.bigIntegerValue(RoundingMode.HALF_DOWN), x.bigIntegerValue(RoundingMode.UP));
            assertEquals(x, x.bigIntegerValue(RoundingMode.HALF_UP), x.bigIntegerValue(RoundingMode.UP));
            assertEquals(x, x.bigIntegerValue(RoundingMode.HALF_EVEN), x.bigIntegerValue(RoundingMode.UP));
        }

        //odd multiples of 1/2
        Iterable<Algebraic> xs = map(
                i -> of(Rational.of(i.shiftLeft(1).add(BigInteger.ONE), IntegerUtils.TWO)),
                P.bigIntegers()
        );
        for (Algebraic x : take(LIMIT, xs)) {
            assertEquals(x, x.bigIntegerValue(RoundingMode.HALF_DOWN), x.bigIntegerValue(RoundingMode.DOWN));
            assertEquals(x, x.bigIntegerValue(RoundingMode.HALF_UP), x.bigIntegerValue(RoundingMode.UP));
            assertFalse(x, x.bigIntegerValue(RoundingMode.HALF_EVEN).testBit(0));
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.algebraics()))) {
            try {
                x.bigIntegerValue(RoundingMode.UNNECESSARY);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesBigIntegerValue() {
        initialize("bigIntegerValue()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            BigInteger rounded = x.bigIntegerValue();
            assertTrue(x, rounded.equals(BigInteger.ZERO) || rounded.signum() == x.signum());
            assertTrue(x, le(x.subtract(of(x.bigIntegerValue())).abs(), ONE_HALF));
            assertEquals(x, x.realValue().bigIntegerValueUnsafe(), rounded);
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> lt(s.abs().fractionalPart(), ONE_HALF), P.algebraics()))) {
            assertEquals(x, x.bigIntegerValue(), x.bigIntegerValue(RoundingMode.DOWN));
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> gt(s.abs().fractionalPart(), ONE_HALF), P.algebraics()))) {
            assertEquals(x, x.bigIntegerValue(), x.bigIntegerValue(RoundingMode.UP));
        }

        //odd multiples of 1/2
        Iterable<Algebraic> xs = map(
                i -> of(Rational.of(i.shiftLeft(1).add(BigInteger.ONE), IntegerUtils.TWO)),
                P.bigIntegers()
        );
        for (Algebraic x : take(LIMIT, xs)) {
            assertFalse(x, x.bigIntegerValue().testBit(0));
        }
    }

    private void propertiesFloor() {
        initialize("floor()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            BigInteger floor = x.floor();
            assertTrue(x, le(of(floor), x));
            assertTrue(x, le(x.subtract(of(floor)), ONE));
            assertEquals(x, x.realValue().floorUnsafe(), floor);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).floor(), i);
        }
    }

    private void propertiesCeiling() {
        initialize("ceiling()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            BigInteger ceiling = x.ceiling();
            assertTrue(x, ge(of(ceiling), x));
            assertTrue(x, le(of(ceiling).subtract(x), ONE));
            assertEquals(x, x.realValue().ceilingUnsafe(), ceiling);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).ceiling(), i);
        }
    }

    private void propertiesBigIntegerValueExact() {
        initialize("bigIntegerValueExact()");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Algebraic x = of(i);
            BigInteger j = x.bigIntegerValueExact();
            assertEquals(i, j, i);
            inverse(Algebraic::bigIntegerValueExact, Algebraic::of, x);
            homomorphic(
                    Algebraic::negate,
                    BigInteger::negate,
                    Algebraic::bigIntegerValueExact,
                    Algebraic::bigIntegerValueExact,
                    x
            );
            assertEquals(x, x.realValue().bigIntegerValueExact(), j);
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.algebraics()))) {
            try {
                x.bigIntegerValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesByteValueExact() {
        initialize("byteValueExact()");
        for (byte b : take(LIMIT, P.bytes())) {
            Algebraic x = of(b);
            byte c = x.byteValueExact();
            assertEquals(b, x.byteValueExact(), b);
            inverse(Algebraic::byteValueExact, d -> of((int) d), x);
            assertEquals(x, x.realValue().byteValueExact(), c);
        }

        for (byte b : take(LIMIT, filter(c -> c != Byte.MIN_VALUE, P.bytes()))) {
            Algebraic x = of(b);
            homomorphic(Algebraic::negate, c -> (byte) -c, Algebraic::byteValueExact, Algebraic::byteValueExact, x);
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.algebraics()))) {
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
            Algebraic x = of(s);
            short t = x.shortValueExact();
            assertEquals(s, t, s);
            inverse(Algebraic::shortValueExact, u -> of((int) u), x);
            assertEquals(s, x.realValue().shortValueExact(), t);
        }

        for (short s : take(LIMIT, filter(t -> t != Short.MIN_VALUE, P.shorts()))) {
            Algebraic x = of(s);
            homomorphic(Algebraic::negate, t -> (short) -t, Algebraic::shortValueExact, Algebraic::shortValueExact, x);
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.algebraics()))) {
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
            Algebraic x = of(i);
            int j = x.intValueExact();
            assertEquals(i, j, i);
            inverse(Algebraic::intValueExact, Algebraic::of, x);
            assertEquals(i, x.realValue().intValueExact(), j);
        }

        for (int i : take(LIMIT, filter(j -> j != Integer.MIN_VALUE, P.integers()))) {
            Algebraic x = of(i);
            homomorphic(Algebraic::negate, j -> -j, Algebraic::intValueExact, Algebraic::intValueExact, x);
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.algebraics()))) {
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
            Algebraic x = of(l);
            long m = x.longValueExact();
            assertEquals(l, m, l);
            inverse(Algebraic::longValueExact, Algebraic::of, x);
            assertEquals(l, x.realValue().longValueExact(), m);
        }

        for (long l : take(LIMIT, filter(m -> m != Long.MIN_VALUE, P.longs()))) {
            Algebraic x = of(l);
            homomorphic(Algebraic::negate, m -> -m, Algebraic::longValueExact, Algebraic::longValueExact, x);
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.algebraics()))) {
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

    private void propertiesIsIntegerPowerOfTwo() {
        initialize("isPowerOfTwo()");
        for (Algebraic x : take(LIMIT, P.positiveAlgebraics())) {
            boolean b = x.isIntegerPowerOfTwo();
            assertEquals(x, x.isIntegerPowerOfTwo(), ONE.shiftLeft(x.binaryExponent()).equals(x));
            assertEquals(x, x.realValue().isExactIntegerPowerOfTwo(), b);
        }

        for (Algebraic x : take(LIMIT, P.rangeDown(ZERO))) {
            try {
                x.isIntegerPowerOfTwo();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesRoundUpToIntegerPowerOfTwo() {
        initialize("roundUpToIntegerPowerOfTwo()");
        for (Algebraic x : take(LIMIT, P.positiveAlgebraics())) {
            BinaryFraction powerOfTwo = x.roundUpToIntegerPowerOfTwo();
            assertTrue(x, powerOfTwo.isPowerOfTwo());
            assertTrue(x, le(x, of(powerOfTwo)));
            assertTrue(x, lt(of(powerOfTwo.shiftRight(1)), x));
            assertEquals(x, x.realValue().roundUpToIntegerPowerOfTwoUnsafe(), powerOfTwo);
        }

        for (Algebraic x : take(LIMIT, P.rangeDown(ZERO))) {
            try {
                x.roundUpToIntegerPowerOfTwo();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesIsBinaryFraction() {
        initialize("isBinaryFraction()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            boolean b = x.isBinaryFraction();
            homomorphic(
                    Algebraic::negate,
                    Function.identity(),
                    Algebraic::isBinaryFraction,
                    Algebraic::isBinaryFraction,
                    x
            );
            assertEquals(x, x.realValue().isExactBinaryFraction(), b);
        }
    }

    private void propertiesBinaryFractionValueExact() {
        initialize("binaryFractionValueExact()");
        for (Algebraic x : take(LIMIT, map(Algebraic::of, P.binaryFractions()))) {
            inverse(Algebraic::binaryFractionValueExact, Algebraic::of, x);
            homomorphic(
                    Algebraic::negate,
                    BinaryFraction::negate,
                    Algebraic::binaryFractionValueExact,
                    Algebraic::binaryFractionValueExact,
                    x
            );
            assertEquals(x, x.realValue().binaryFractionValueExact(), x.binaryFractionValueExact());
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> !s.isBinaryFraction(), P.algebraics()))) {
            try {
                x.binaryFractionValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesIsRational() {
        initialize("isRational()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            boolean b = x.isRational();
            homomorphic(
                    Algebraic::negate,
                    Function.identity(),
                    Algebraic::isRational,
                    Algebraic::isRational,
                    x
            );
            assertEquals(x, x.realValue().isExact(), b);
        }
    }

    private void propertiesIsAlgebraicInteger() {
        initialize("isAlgebraicInteger()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            x.isAlgebraicInteger();
            homomorphic(
                    Algebraic::negate,
                    Function.identity(),
                    Algebraic::isAlgebraicInteger,
                    Algebraic::isAlgebraicInteger,
                    x
            );
        }
    }

    private void propertiesRationalValueExact() {
        initialize("rationalValueExact()");
        for (Algebraic x : take(LIMIT, P.algebraics(1))) {
            inverse(Algebraic::rationalValueExact, Algebraic::of, x);
            homomorphic(
                    Algebraic::negate,
                    Rational::negate,
                    Algebraic::rationalValueExact,
                    Algebraic::rationalValueExact,
                    x
            );
            assertEquals(x, x.realValue().rationalValueExact().get(), x.rationalValueExact());
        }

        for (Algebraic x : take(LIMIT, filterInfinite(y -> !y.isRational(), P.algebraics()))) {
            try {
                x.rationalValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesRealValue() {
        initialize("realValue()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            x.realValue();
        }
    }

    private void propertiesBinaryExponent() {
        initialize("binaryExponent()");
        for (Algebraic x : take(LIMIT, P.positiveAlgebraics())) {
            int exponent = x.binaryExponent();
            Algebraic power = ONE.shiftLeft(exponent);
            assertTrue(x, le(power, x));
            assertTrue(x, le(x, power.shiftLeft(1)));
            assertEquals(x, x.realValue().binaryExponentUnsafe(), exponent);
        }

        for (Algebraic x : take(LIMIT, P.rangeDown(ZERO))) {
            try {
                x.binaryExponent();
                fail(x);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static boolean isEqualToFloat_simplest(@NotNull Algebraic x) {
        Optional<Algebraic> ox = ofExact(x.floatValue(RoundingMode.FLOOR));
        return ox.isPresent() && ox.get().equals(x);
    }

    private void propertiesIsEqualToFloat() {
        initialize("isEqualToFloat()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            boolean ietf = x.isEqualToFloat();
            assertTrue(x, !ietf || x.isBinaryFraction());
            homomorphic(
                    Algebraic::negate,
                    Function.identity(),
                    Algebraic::isEqualToFloat,
                    Algebraic::isEqualToFloat,
                    x
            );
            assertEquals(x, isEqualToFloat_simplest(x), ietf);
            assertEquals(x, x.realValue().isExactAndEqualToFloat(), ietf);
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            assertTrue(f, ofExact(f).get().isEqualToFloat());
        }
    }

    private void compareImplementationsIsEqualToFloat() {
        Map<String, Function<Algebraic, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", AlgebraicProperties::isEqualToFloat_simplest);
        functions.put("standard", Algebraic::isEqualToFloat);
        compareImplementations("isEqualToFloat()", take(LIMIT, P.algebraics()), functions, v -> P.reset());
    }

    private static boolean isEqualToDouble_simplest(@NotNull Algebraic x) {
        Optional<Algebraic> ox = ofExact(x.doubleValue(RoundingMode.FLOOR));
        return ox.isPresent() && ox.get().equals(x);
    }

    private void propertiesIsEqualToDouble() {
        initialize("isEqualToDouble()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            boolean ietd = x.isEqualToDouble();
            assertTrue(x, !ietd || x.isBinaryFraction());
            homomorphic(
                    Algebraic::negate,
                    Function.identity(),
                    Algebraic::isEqualToDouble,
                    Algebraic::isEqualToDouble,
                    x
            );
            assertEquals(x, isEqualToDouble_simplest(x), ietd);
            assertEquals(x, x.realValue().isExactAndEqualToDouble(), ietd);
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            assertTrue(d, ofExact(d).get().isEqualToDouble());
        }
    }

    private void compareImplementationsIsEqualToDouble() {
        Map<String, Function<Algebraic, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", AlgebraicProperties::isEqualToDouble_simplest);
        functions.put("standard", Algebraic::isEqualToDouble);
        compareImplementations("isEqualToDouble()", take(LIMIT, P.algebraics()), functions, v -> P.reset());
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

    private void propertiesFloatValue_RoundingMode() {
        initialize("floatValue(RoundingMode)");
        Iterable<Pair<Algebraic, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isEqualToFloat(),
                P.pairs(P.withScale(1).algebraics(), P.roundingModes())
        );
        for (Pair<Algebraic, RoundingMode> p : take(SMALL_LIMIT, ps)) {
            float rounded = p.a.floatValue(p.b);
            assertTrue(p, !Float.isNaN(rounded));
            assertTrue(p, rounded == 0.0f || Math.signum(rounded) == p.a.signum());
            assertEquals(p, p.a.realValue().floatValueUnsafe(p.b), rounded);
        }

        Iterable<Algebraic> xs = map(
                f -> ofExact(f).get(),
                filter(f -> Float.isFinite(f) && !isNegativeZero(f), P.floats())
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValue(RoundingMode.UNNECESSARY);
            assertEquals(x, x, ofExact(rounded).get());
            assertTrue(x, Float.isFinite(rounded));
            assertTrue(x, !isNegativeZero(rounded));
            inverse(s -> s.floatValue(RoundingMode.UNNECESSARY), (Float f) -> ofExact(f).get(), x);
        }

        Algebraic largestFloat = of(Rational.LARGEST_FLOAT);
        xs = filterInfinite(
                x -> !x.equals(largestFloat),
                P.withScale(2).algebraicsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValue(RoundingMode.FLOOR);
            float successor = successor(rounded);
            assertTrue(x, le(ofExact(rounded).get(), x));
            assertTrue(x, gt(ofExact(successor).get(), x));
            assertTrue(x, rounded < 0 || Float.isFinite(rounded));
            assertTrue(x, !isNegativeZero(rounded));
        }

        xs = filterInfinite(
                x -> !x.equals(of(Rational.LARGEST_FLOAT.negate())),
                P.withScale(2).algebraicsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValue(RoundingMode.CEILING);
            float predecessor = predecessor(rounded);
            assertTrue(x, ge(ofExact(rounded).get(), x));
            assertTrue(x, lt(ofExact(predecessor).get(), x));
            assertTrue(x, rounded > 0 || Float.isFinite(rounded));
        }

        xs = P.withScale(2).algebraicsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT));
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValue(RoundingMode.DOWN);
            assertTrue(x, le(ofExact(rounded).get().abs(), x.abs()));
            assertTrue(x, Float.isFinite(rounded));
        }

        xs = filterInfinite(
                r -> r != ZERO,
                P.withScale(2).algebraicsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValue(RoundingMode.DOWN);
            float successor = successor(rounded);
            float predecessor = predecessor(rounded);
            float down = x.signum() == -1 ? successor : predecessor;
            assertTrue(x, lt(ofExact(down).get().abs(), x.abs()));
        }

        xs = filterInfinite(
                x -> !x.abs().equals(of(Rational.LARGEST_FLOAT)),
                P.withScale(2).algebraicsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            assertTrue(x, !isNegativeZero(x.floatValue(RoundingMode.UP)));
        }

        xs = filterInfinite(
                r -> !r.equals(of(Rational.SMALLEST_FLOAT)),
                P.withScale(2).algebraicsIn(Interval.of(Rational.ZERO, Rational.SMALLEST_FLOAT))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValue(RoundingMode.FLOOR), 0.0f);
            aeqf(x, x.floatValue(RoundingMode.DOWN), 0.0f);
            float rounded = x.floatValue(RoundingMode.UP);
            float successor = successor(rounded);
            float predecessor = predecessor(rounded);
            float up = x.signum() == -1 ? predecessor : successor;
            assertTrue(x, gt(ofExact(up).get().abs(), x.abs()));
        }

        xs = filterInfinite(
                x -> !x.equals(of(Rational.LARGEST_FLOAT.negate())),
                P.withScale(2).algebraicsIn(Interval.lessThanOrEqualTo(Rational.LARGEST_FLOAT.negate()))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            float floor = x.floatValue(RoundingMode.FLOOR);
            aeqf(x, floor, Float.NEGATIVE_INFINITY);

            float up = x.floatValue(RoundingMode.UP);
            aeqf(x, up, Float.NEGATIVE_INFINITY);

            float halfUp = x.floatValue(RoundingMode.HALF_UP);
            aeqf(x, halfUp, Float.NEGATIVE_INFINITY);

            float halfEven = x.floatValue(RoundingMode.HALF_EVEN);
            aeqf(x, halfEven, Float.NEGATIVE_INFINITY);
        }

        xs = P.withScale(2).algebraicsIn(Interval.greaterThanOrEqualTo(Rational.LARGEST_FLOAT));
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValue(RoundingMode.FLOOR), Float.MAX_VALUE);
            aeqf(x, x.floatValue(RoundingMode.DOWN), Float.MAX_VALUE);
            aeqf(x, x.floatValue(RoundingMode.HALF_DOWN), Float.MAX_VALUE);
        }

        xs = filterInfinite(
                x -> x != ZERO && !x.equals(of(Rational.SMALLEST_FLOAT.negate())),
                P.withScale(2).algebraicsIn(Interval.of(Rational.SMALLEST_FLOAT.negate(), Rational.ZERO))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValue(RoundingMode.CEILING), -0.0f);
            aeqf(x, x.floatValue(RoundingMode.DOWN), -0.0f);
        }

        xs = filterInfinite(
                x -> !x.equals(of(Rational.LARGEST_FLOAT)),
                P.withScale(2).algebraicsIn(Interval.greaterThanOrEqualTo(Rational.LARGEST_FLOAT))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            float ceiling = x.floatValue(RoundingMode.CEILING);
            aeqf(x, ceiling, Float.POSITIVE_INFINITY);

            float up = x.floatValue(RoundingMode.UP);
            aeqf(x, up, Float.POSITIVE_INFINITY);

            float halfUp = x.floatValue(RoundingMode.HALF_UP);
            aeqf(x, halfUp, Float.POSITIVE_INFINITY);

            float halfEven = x.floatValue(RoundingMode.HALF_EVEN);
            aeqf(x, halfEven, Float.POSITIVE_INFINITY);
        }

        xs = P.withScale(2).algebraicsIn(Interval.lessThanOrEqualTo(Rational.LARGEST_FLOAT.negate()));
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValue(RoundingMode.CEILING), -Float.MAX_VALUE);
            aeqf(x, x.floatValue(RoundingMode.DOWN), -Float.MAX_VALUE);
            aeqf(x, x.floatValue(RoundingMode.HALF_DOWN), -Float.MAX_VALUE);
        }

        Iterable<Algebraic> midpoints = map(
                f -> {
                    Rational lo = Rational.ofExact(f).get();
                    Rational hi = Rational.ofExact(successor(f)).get();
                    return of(lo.add(hi).shiftRight(1));
                },
                filter(f -> Float.isFinite(f) && !isNegativeZero(f) && f != Float.MAX_VALUE, P.floats())
        );
        for (Algebraic x : take(SMALL_LIMIT, midpoints)) {
            float down = x.floatValue(RoundingMode.DOWN);
            float up = x.floatValue(RoundingMode.UP);
            aeqf(x, x.floatValue(RoundingMode.HALF_DOWN), down);
            aeqf(x, x.floatValue(RoundingMode.HALF_UP), up);
            float halfEven = x.floatValue(RoundingMode.HALF_EVEN);
            assertTrue(x, ((Float.floatToIntBits(down) & 1) == 0 ? down : up) == halfEven);
        }

        Iterable<Algebraic> notMidpoints = filterInfinite(
                x -> !floatEquidistant(x),
                P.withScale(2).algebraicsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT))
        );
        for (Algebraic x : take(SMALL_LIMIT, notMidpoints)) {
            float below = x.floatValue(RoundingMode.FLOOR);
            float above = x.floatValue(RoundingMode.CEILING);
            Algebraic belowDistance = x.subtract(ofExact(below).get());
            Algebraic aboveDistance = ofExact(above).get().subtract(x);
            float closest = lt(belowDistance, aboveDistance) ? below : above;
            aeqf(x, x.floatValue(RoundingMode.HALF_DOWN), closest);
            aeqf(x, x.floatValue(RoundingMode.HALF_UP), closest);
            aeqf(x, x.floatValue(RoundingMode.HALF_EVEN), closest);
        }

        xs = P.withScale(2).algebraicsIn(Interval.of(Rational.ZERO, Rational.SMALLEST_FLOAT.shiftRight(1)));
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValue(RoundingMode.HALF_DOWN), 0.0f);
            aeqf(x, x.floatValue(RoundingMode.HALF_EVEN), 0.0f);
        }

        xs = filterInfinite(
                r -> r != ZERO,
                P.withScale(2).algebraicsIn(Interval.of(Rational.SMALLEST_FLOAT.shiftRight(1).negate(), Rational.ZERO))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValue(RoundingMode.HALF_DOWN), -0.0f);
            aeqf(x, x.floatValue(RoundingMode.HALF_EVEN), -0.0f);
        }

        xs = filterInfinite(
                x -> !x.equals(of(Rational.SMALLEST_FLOAT.shiftRight(1))),
                P.withScale(2).algebraicsIn(Interval.of(Rational.ZERO, Rational.SMALLEST_FLOAT.shiftRight(1)))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValue(RoundingMode.HALF_UP), 0.0f);
        }

        xs = filterInfinite(
                x -> x != ZERO && !x.equals(of(Rational.SMALLEST_FLOAT.shiftRight(1).negate())),
                P.withScale(2).algebraicsIn(Interval.of(Rational.SMALLEST_FLOAT.shiftRight(1).negate(), Rational.ZERO))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValue(RoundingMode.HALF_UP), -0.0f);
        }

        for (Algebraic x : take(SMALL_LIMIT, P.withScale(2).algebraics())) {
            float floor = x.floatValue(RoundingMode.FLOOR);
            assertFalse(x, isNegativeZero(floor));
            assertFalse(x, floor == Float.POSITIVE_INFINITY);
            float ceiling = x.floatValue(RoundingMode.CEILING);
            assertFalse(x, ceiling == Float.NEGATIVE_INFINITY);
            float down = x.floatValue(RoundingMode.DOWN);
            assertTrue(x, Float.isFinite(down));
            float up = x.floatValue(RoundingMode.UP);
            assertFalse(x, isNegativeZero(up));
            float halfDown = x.floatValue(RoundingMode.HALF_DOWN);
            assertTrue(x, Float.isFinite(halfDown));
        }

        for (Algebraic x : take(LIMIT, filterInfinite(y -> !y.isEqualToFloat(), P.withScale(2).algebraics()))) {
            try {
                x.floatValue(RoundingMode.UNNECESSARY);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesFloatValue() {
        initialize("floatValue()");
        for (Algebraic x : take(SMALL_LIMIT, P.withScale(2).algebraics())) {
            float rounded = x.floatValue();
            aeqf(x, rounded, x.floatValue(RoundingMode.HALF_EVEN));
            assertTrue(x, !Float.isNaN(rounded));
            assertTrue(x, rounded == 0.0f || Math.signum(rounded) == x.signum());
            assertEquals(x, x.realValue().floatValueUnsafe(), rounded);
        }

        Iterable<Algebraic> xs = filterInfinite(
                x -> !x.equals(of(Rational.LARGEST_FLOAT.negate())),
                P.withScale(2).algebraicsIn(Interval.lessThanOrEqualTo(Rational.LARGEST_FLOAT.negate()))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValue();
            aeqf(x, rounded, Float.NEGATIVE_INFINITY);
        }

        xs = filterInfinite(
                x -> !x.equals(of(Rational.LARGEST_FLOAT)),
                P.withScale(2).algebraicsIn(Interval.greaterThanOrEqualTo(Rational.LARGEST_FLOAT))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            float rounded = x.floatValue();
            aeqf(x, rounded, Float.POSITIVE_INFINITY);
        }

        Iterable<Algebraic> midpoints = map(
                f -> {
                    Rational lo = Rational.ofExact(f).get();
                    Rational hi = Rational.ofExact(successor(f)).get();
                    return of(lo.add(hi).shiftRight(1));
                },
                filter(f -> Float.isFinite(f) && f != Float.MAX_VALUE, P.floats())
        );
        for (Algebraic x : take(SMALL_LIMIT, midpoints)) {
            float down = x.floatValue(RoundingMode.DOWN);
            float up = x.floatValue(RoundingMode.UP);
            float rounded = x.floatValue();
            assertTrue(x, ((Float.floatToIntBits(down) & 1) == 0 ? down : up) == rounded);
        }

        Iterable<Algebraic> notMidpoints = filterInfinite(
                x -> !floatEquidistant(x),
                P.withScale(2).algebraicsIn(Interval.of(Rational.LARGEST_FLOAT.negate(), Rational.LARGEST_FLOAT))
        );
        for (Algebraic x : take(SMALL_LIMIT, notMidpoints)) {
            float below = x.floatValue(RoundingMode.FLOOR);
            float above = x.floatValue(RoundingMode.CEILING);
            Algebraic belowDistance = x.subtract(ofExact(below).get());
            Algebraic aboveDistance = ofExact(above).get().subtract(x);
            float closest = lt(belowDistance, aboveDistance) ? below : above;
            aeqf(x, x.floatValue(), closest);
        }

        xs = P.withScale(2).algebraicsIn(Interval.of(Rational.ZERO, Rational.SMALLEST_FLOAT.shiftRight(1)));
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValue(), 0.0f);
        }

        xs = filterInfinite(
                x -> x != ZERO,
                P.withScale(2).algebraicsIn(Interval.of(Rational.SMALLEST_FLOAT.shiftRight(1).negate(), Rational.ZERO))
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            aeqf(x, x.floatValue(), -0.0f);
        }
    }

    private void propertiesFloatValueExact() {
        initialize("floatValueExact()");
        for (Algebraic x : take(SMALL_LIMIT, filterInfinite(Algebraic::isEqualToFloat, P.withScale(2).algebraics()))) {
            float f = x.floatValueExact();
            assertTrue(x, !Float.isNaN(f));
            assertTrue(x, f == 0.0f || Math.signum(f) == x.signum());
            homomorphic(
                    Algebraic::negate,
                    g -> absNegativeZeros(-g),
                    Algebraic::floatValueExact,
                    Algebraic::floatValueExact,
                    x
            );
            assertEquals(x, x.realValue().floatValueExact(), f);
        }

        Iterable<Algebraic> xs = map(
                f -> ofExact(f).get(),
                filter(f -> Float.isFinite(f) && !isNegativeZero(f), P.floats())
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            float f = x.floatValueExact();
            assertEquals(x, x, ofExact(f).get());
            assertTrue(x, Float.isFinite(f));
            assertTrue(x, !isNegativeZero(f));
        }

        for (Algebraic x : take(LIMIT, filterInfinite(y -> !y.isEqualToFloat(), P.withScale(2).algebraics()))) {
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

    private void propertiesDoubleValue_RoundingMode() {
        initialize("doubleValue(RoundingMode)");
        Iterable<Pair<Algebraic, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isEqualToDouble(),
                P.pairs(P.withScale(1).algebraics(), P.roundingModes())
        );
        for (Pair<Algebraic, RoundingMode> p : take(SMALL_LIMIT, ps)) {
            double rounded = p.a.doubleValue(p.b);
            assertTrue(p, !Double.isNaN(rounded));
            assertTrue(p, rounded == 0.0 || Math.signum(rounded) == p.a.signum());
            assertEquals(p, p.a.realValue().doubleValueUnsafe(p.b), rounded);
        }

        Iterable<Algebraic> xs = map(
                d -> ofExact(d).get(),
                filter(d -> Double.isFinite(d) && !isNegativeZero(d), P.doubles())
        );
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            double rounded = x.doubleValue(RoundingMode.UNNECESSARY);
            assertEquals(x, x, ofExact(rounded).get());
            assertTrue(x, Double.isFinite(rounded));
            assertTrue(x, !isNegativeZero(rounded));
            inverse(s -> s.doubleValue(RoundingMode.UNNECESSARY), (Double d) -> ofExact(d).get(), x);
        }

        Algebraic largestDouble = of(Rational.LARGEST_DOUBLE);
        xs = filterInfinite(
                x -> !x.equals(largestDouble),
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE)
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            double rounded = x.doubleValue(RoundingMode.FLOOR);
            double successor = successor(rounded);
            assertTrue(x, le(ofExact(rounded).get(), x));
            assertTrue(x, gt(ofExact(successor).get(), x));
            assertTrue(x, rounded < 0 || Double.isFinite(rounded));
            assertTrue(x, !isNegativeZero(rounded));
        }

        xs = filterInfinite(
                x -> !x.equals(of(Rational.LARGEST_DOUBLE.negate())),
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE)
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            double rounded = x.doubleValue(RoundingMode.CEILING);
            double predecessor = predecessor(rounded);
            assertTrue(x, ge(ofExact(rounded).get(), x));
            assertTrue(x, lt(ofExact(predecessor).get(), x));
            assertTrue(x, rounded > 0 || Double.isFinite(rounded));
        }

        xs = P.withScale(2).withSecondaryScale(4).algebraicsIn(
                Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE)
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            double rounded = x.doubleValue(RoundingMode.DOWN);
            assertTrue(x, le(ofExact(rounded).get().abs(), x.abs()));
            assertTrue(x, Double.isFinite(rounded));
        }

        xs = filterInfinite(
                r -> r != ZERO,
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE)
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            double rounded = x.doubleValue(RoundingMode.DOWN);
            double successor = successor(rounded);
            double predecessor = predecessor(rounded);
            double down = x.signum() == -1 ? successor : predecessor;
            assertTrue(x, lt(ofExact(down).get().abs(), x.abs()));
        }

        xs = filterInfinite(
                x -> !x.abs().equals(of(Rational.LARGEST_DOUBLE)),
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE)
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            assertTrue(x, !isNegativeZero(x.doubleValue(RoundingMode.UP)));
        }

        xs = filterInfinite(
                r -> !r.equals(of(Rational.SMALLEST_DOUBLE)),
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.of(Rational.ZERO, Rational.SMALLEST_DOUBLE)
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            aeqd(x, x.doubleValue(RoundingMode.FLOOR), 0.0);
            aeqd(x, x.doubleValue(RoundingMode.DOWN), 0.0);
            double rounded = x.doubleValue(RoundingMode.UP);
            double successor = successor(rounded);
            double predecessor = predecessor(rounded);
            double up = x.signum() == -1 ? predecessor : successor;
            assertTrue(x, gt(ofExact(up).get().abs(), x.abs()));
        }

        xs = filterInfinite(
                x -> !x.equals(of(Rational.LARGEST_DOUBLE.negate())),
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.lessThanOrEqualTo(Rational.LARGEST_DOUBLE.negate())
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            double floor = x.doubleValue(RoundingMode.FLOOR);
            aeqd(x, floor, Double.NEGATIVE_INFINITY);

            double up = x.doubleValue(RoundingMode.UP);
            aeqd(x, up, Double.NEGATIVE_INFINITY);

            double halfUp = x.doubleValue(RoundingMode.HALF_UP);
            aeqd(x, halfUp, Double.NEGATIVE_INFINITY);

            double halfEven = x.doubleValue(RoundingMode.HALF_EVEN);
            aeqd(x, halfEven, Double.NEGATIVE_INFINITY);
        }

        xs = P.withScale(2).withSecondaryScale(4).algebraicsIn(
                Interval.greaterThanOrEqualTo(Rational.LARGEST_DOUBLE)
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            aeqd(x, x.doubleValue(RoundingMode.FLOOR), Double.MAX_VALUE);
            aeqd(x, x.doubleValue(RoundingMode.DOWN), Double.MAX_VALUE);
            aeqd(x, x.doubleValue(RoundingMode.HALF_DOWN), Double.MAX_VALUE);
        }

        xs = filterInfinite(
                x -> x != ZERO && !x.equals(of(Rational.SMALLEST_DOUBLE.negate())),
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.of(Rational.SMALLEST_DOUBLE.negate(), Rational.ZERO)
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            aeqd(x, x.doubleValue(RoundingMode.CEILING), -0.0);
            aeqd(x, x.doubleValue(RoundingMode.DOWN), -0.0);
        }

        xs = filterInfinite(
                x -> !x.equals(of(Rational.LARGEST_DOUBLE)),
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.greaterThanOrEqualTo(Rational.LARGEST_DOUBLE)
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            double ceiling = x.doubleValue(RoundingMode.CEILING);
            aeqd(x, ceiling, Double.POSITIVE_INFINITY);

            double up = x.doubleValue(RoundingMode.UP);
            aeqd(x, up, Double.POSITIVE_INFINITY);

            double halfUp = x.doubleValue(RoundingMode.HALF_UP);
            aeqd(x, halfUp, Double.POSITIVE_INFINITY);

            double halfEven = x.doubleValue(RoundingMode.HALF_EVEN);
            aeqd(x, halfEven, Double.POSITIVE_INFINITY);
        }

        xs = P.withScale(2).withSecondaryScale(4).algebraicsIn(
                Interval.lessThanOrEqualTo(Rational.LARGEST_DOUBLE.negate())
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            aeqd(x, x.doubleValue(RoundingMode.CEILING), -Double.MAX_VALUE);
            aeqd(x, x.doubleValue(RoundingMode.DOWN), -Double.MAX_VALUE);
            aeqd(x, x.doubleValue(RoundingMode.HALF_DOWN), -Double.MAX_VALUE);
        }

        Iterable<Algebraic> midpoints = map(
                d -> {
                    Rational lo = Rational.ofExact(d).get();
                    Rational hi = Rational.ofExact(successor(d)).get();
                    return of(lo.add(hi).shiftRight(1));
                },
                filter(d -> Double.isFinite(d) && !isNegativeZero(d) && d != Double.MAX_VALUE, P.doubles())
        );
        for (Algebraic x : take(SMALL_LIMIT, midpoints)) {
            double down = x.doubleValue(RoundingMode.DOWN);
            double up = x.doubleValue(RoundingMode.UP);
            aeqd(x, x.doubleValue(RoundingMode.HALF_DOWN), down);
            aeqd(x, x.doubleValue(RoundingMode.HALF_UP), up);
            double halfEven = x.doubleValue(RoundingMode.HALF_EVEN);
            assertTrue(x, ((Double.doubleToLongBits(down) & 1L) == 0L ? down : up) == halfEven);
        }

        Iterable<Algebraic> notMidpoints = filterInfinite(
                x -> !doubleEquidistant(x),
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE)
                )
        );
        for (Algebraic x : take(TINY_LIMIT, notMidpoints)) {
            double below = x.doubleValue(RoundingMode.FLOOR);
            double above = x.doubleValue(RoundingMode.CEILING);
            Algebraic belowDistance = x.subtract(ofExact(below).get());
            Algebraic aboveDistance = ofExact(above).get().subtract(x);
            double closest = lt(belowDistance, aboveDistance) ? below : above;
            aeqd(x, x.doubleValue(RoundingMode.HALF_DOWN), closest);
            aeqd(x, x.doubleValue(RoundingMode.HALF_UP), closest);
            aeqd(x, x.doubleValue(RoundingMode.HALF_EVEN), closest);
        }

        xs = P.withScale(2).withSecondaryScale(4).algebraicsIn(
                Interval.of(Rational.ZERO, Rational.SMALLEST_DOUBLE.shiftRight(1))
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            aeqd(x, x.doubleValue(RoundingMode.HALF_DOWN), 0.0);
            aeqd(x, x.doubleValue(RoundingMode.HALF_EVEN), 0.0);
        }

        xs = filterInfinite(
                r -> r != ZERO,
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.of(Rational.SMALLEST_DOUBLE.shiftRight(1).negate(), Rational.ZERO)
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            aeqd(x, x.doubleValue(RoundingMode.HALF_DOWN), -0.0);
            aeqd(x, x.doubleValue(RoundingMode.HALF_EVEN), -0.0);
        }

        xs = filterInfinite(
                x -> !x.equals(of(Rational.SMALLEST_DOUBLE.shiftRight(1))),
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.of(Rational.ZERO, Rational.SMALLEST_DOUBLE.shiftRight(1))
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            aeqd(x, x.doubleValue(RoundingMode.HALF_UP), 0.0);
        }

        xs = filterInfinite(
                x -> x != ZERO && !x.equals(of(Rational.SMALLEST_DOUBLE.shiftRight(1).negate())),
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.of(Rational.SMALLEST_DOUBLE.shiftRight(1).negate(), Rational.ZERO)
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            aeqd(x, x.doubleValue(RoundingMode.HALF_UP), -0.0);
        }

        for (Algebraic x : take(SMALL_LIMIT, P.withScale(2).withSecondaryScale(4).algebraics())) {
            double floor = x.doubleValue(RoundingMode.FLOOR);
            assertFalse(x, isNegativeZero(floor));
            assertFalse(x, floor == Double.POSITIVE_INFINITY);
            double ceiling = x.doubleValue(RoundingMode.CEILING);
            assertFalse(x, ceiling == Double.NEGATIVE_INFINITY);
            double down = x.doubleValue(RoundingMode.DOWN);
            assertTrue(x, Double.isFinite(down));
            double up = x.doubleValue(RoundingMode.UP);
            assertFalse(x, isNegativeZero(up));
            double halfDown = x.doubleValue(RoundingMode.HALF_DOWN);
            assertTrue(x, Double.isFinite(halfDown));
        }

        for (Algebraic x : take(LIMIT, filterInfinite(y -> !y.isEqualToDouble(), P.withScale(2).algebraics()))) {
            try {
                x.doubleValue(RoundingMode.UNNECESSARY);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDoubleValue() {
        initialize("doubleValue()");
        for (Algebraic x : take(SMALL_LIMIT, P.withScale(2).algebraics())) {
            double rounded = x.doubleValue();
            aeqd(x, rounded, x.doubleValue(RoundingMode.HALF_EVEN));
            assertTrue(x, !Double.isNaN(rounded));
            assertTrue(x, rounded == 0.0 || Math.signum(rounded) == x.signum());
            assertEquals(x, x.realValue().doubleValueUnsafe(), rounded);
        }

        Iterable<Algebraic> xs = filterInfinite(
                x -> !x.equals(of(Rational.LARGEST_DOUBLE.negate())),
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.lessThanOrEqualTo(Rational.LARGEST_DOUBLE.negate())
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            double rounded = x.doubleValue();
            aeqd(x, rounded, Double.NEGATIVE_INFINITY);
        }

        xs = filterInfinite(
                x -> !x.equals(of(Rational.LARGEST_DOUBLE)),
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.greaterThanOrEqualTo(Rational.LARGEST_DOUBLE)
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            double rounded = x.doubleValue();
            aeqd(x, rounded, Double.POSITIVE_INFINITY);
        }

        Iterable<Algebraic> midpoints = map(
                d -> {
                    Rational lo = Rational.ofExact(d).get();
                    Rational hi = Rational.ofExact(successor(d)).get();
                    return of(lo.add(hi).shiftRight(1));
                },
                filter(d -> Double.isFinite(d) && d != Double.MAX_VALUE, P.doubles())
        );
        for (Algebraic x : take(SMALL_LIMIT, midpoints)) {
            double down = x.doubleValue(RoundingMode.DOWN);
            double up = x.doubleValue(RoundingMode.UP);
            double rounded = x.doubleValue();
            assertTrue(x, ((Double.doubleToLongBits(down) & 1L) == 0L ? down : up) == rounded);
        }

        Iterable<Algebraic> notMidpoints = filterInfinite(
                x -> !doubleEquidistant(x),
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.of(Rational.LARGEST_DOUBLE.negate(), Rational.LARGEST_DOUBLE)
                )
        );
        for (Algebraic x : take(TINY_LIMIT, notMidpoints)) {
            double below = x.doubleValue(RoundingMode.FLOOR);
            double above = x.doubleValue(RoundingMode.CEILING);
            Algebraic belowDistance = x.subtract(ofExact(below).get());
            Algebraic aboveDistance = ofExact(above).get().subtract(x);
            double closest = lt(belowDistance, aboveDistance) ? below : above;
            aeqd(x, x.doubleValue(), closest);
        }

        xs = P.withScale(2).withSecondaryScale(4).algebraicsIn(
                Interval.of(Rational.ZERO, Rational.SMALLEST_DOUBLE.shiftRight(1))
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            aeqd(x, x.doubleValue(), 0.0);
        }

        xs = filterInfinite(
                x -> x != ZERO,
                P.withScale(2).withSecondaryScale(4).algebraicsIn(
                        Interval.of(Rational.SMALLEST_DOUBLE.shiftRight(1).negate(), Rational.ZERO)
                )
        );
        for (Algebraic x : take(TINY_LIMIT, xs)) {
            aeqd(x, x.doubleValue(), -0.0);
        }
    }

    private void propertiesDoubleValueExact() {
        initialize("doubleValueExact()");
        Iterable<Algebraic> xs = filterInfinite(Algebraic::isEqualToDouble, P.withScale(2).algebraics());
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            double d = x.doubleValueExact();
            assertTrue(x, !Double.isNaN(d));
            assertTrue(x, d == 0.0 || Math.signum(d) == x.signum());
            homomorphic(
                    Algebraic::negate,
                    e -> absNegativeZeros(-e),
                    Algebraic::doubleValueExact,
                    Algebraic::doubleValueExact,
                    x
            );
            assertEquals(x, x.realValue().doubleValueExact(), d);
        }

        xs = map(d -> ofExact(d).get(), filter(d -> Double.isFinite(d) && !isNegativeZero(d), P.doubles()));
        for (Algebraic x : take(SMALL_LIMIT, xs)) {
            double d = x.doubleValueExact();
            assertEquals(x, x, ofExact(d).get());
            assertTrue(x, Double.isFinite(d));
            assertTrue(x, !isNegativeZero(d));
        }

        for (Algebraic x : take(LIMIT, filterInfinite(y -> !y.isEqualToDouble(), P.withScale(2).algebraics()))) {
            try {
                x.doubleValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesHasTerminatingBaseExpansion() {
        initialize("hasTerminatingBaseExpansion(BigInteger)");
        //noinspection Convert2MethodRef
        Iterable<Pair<Algebraic, BigInteger>> ps = P.pairs(
                P.withScale(4).algebraics(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
        );
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, ps)) {
            boolean result = p.a.hasTerminatingBaseExpansion(p.b);
            if (result) {
                Rational r = p.a.rationalValueExact();
                Iterable<BigInteger> dPrimeFactors = nub(MathUtils.primeFactors(r.getDenominator()));
                Iterable<BigInteger> bPrimeFactors = nub(MathUtils.primeFactors(p.b));
                assertTrue(p, isSubsetOf(dPrimeFactors, bPrimeFactors));
                Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>> pn = r.abs().positionalNotation(p.b);
                assertTrue(p, pn.c.equals(Collections.singletonList(BigInteger.ZERO)));
            }
        }

        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.algebraics(), P.rangeDown(BigInteger.ONE)))) {
            try {
                p.a.hasTerminatingBaseExpansion(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesBigDecimalValueByPrecision_int_RoundingMode() {
        initialize("bigDecimalValueByPrecision(int, RoundingMode)");
        Predicate<Triple<Algebraic, Integer, RoundingMode>> valid = t -> {
            try {
                t.a.bigDecimalValueByPrecision(t.b, t.c);
                return true;
            } catch (ArithmeticException e) {
                return false;
            }
        };
        Iterable<Triple<Algebraic, Integer, RoundingMode>> ts = filterInfinite(
                valid,
                P.triples(P.algebraics(), P.naturalIntegersGeometric(), P.roundingModes())
        );
        for (Triple<Algebraic, Integer, RoundingMode> t : take(LIMIT, ts)) {
            BigDecimal bd = t.a.bigDecimalValueByPrecision(t.b, t.c);
            assertTrue(t, eq(bd, BigDecimal.ZERO) || bd.signum() == t.a.signum());
            assertEquals(t, t.a.realValue().bigDecimalValueByPrecisionUnsafe(t.b, t.c), bd);
        }

        ts = filterInfinite(valid, P.triples(P.nonzeroAlgebraics(), P.positiveIntegersGeometric(), P.roundingModes()));
        for (Triple<Algebraic, Integer, RoundingMode> t : take(LIMIT, ts)) {
            BigDecimal bd = t.a.bigDecimalValueByPrecision(t.b, t.c);
            assertTrue(t, bd.precision() == t.b);
        }

        Iterable<Pair<Algebraic, Integer>> ps = filterInfinite(
                q -> valid.test(new Triple<>(q.a, q.b, RoundingMode.UNNECESSARY)),
                P.pairsSquareRootOrder(P.algebraics(1), P.naturalIntegersGeometric())
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.UNNECESSARY);
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.FLOOR));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.CEILING));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.DOWN));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.UP));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.HALF_DOWN));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.HALF_UP));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.HALF_EVEN));
        }

        ps = filterInfinite(
                q -> valid.test(new Triple<>(q.a, q.b, RoundingMode.FLOOR)) &&
                        !of(q.a.bigDecimalValueByPrecision(q.b)).equals(q.a),
                P.pairsSquareRootOrder(P.algebraics(), P.naturalIntegersGeometric())
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, ps)) {
            BigDecimal low = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.FLOOR);
            BigDecimal high = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.CEILING);
            assertTrue(p, lt(low, high));
        }

        ps = filterInfinite(
                q -> valid.test(new Triple<>(q.a, q.b, RoundingMode.FLOOR)) &&
                        !of(q.a.bigDecimalValueByPrecision(q.b)).equals(q.a),
                P.pairsSquareRootOrder(P.positiveAlgebraics(), P.naturalIntegersGeometric())
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, ps)) {
            BigDecimal floor = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.FLOOR);
            BigDecimal down = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.DOWN);
            BigDecimal ceiling = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.CEILING);
            BigDecimal up = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.UP);
            assertEquals(p, floor, down);
            assertEquals(p, ceiling, up);
        }

        ps = filterInfinite(
                q -> valid.test(new Triple<>(q.a, q.b, RoundingMode.FLOOR)) &&
                        !of(q.a.bigDecimalValueByPrecision(q.b)).equals(q.a),
                P.pairsSquareRootOrder(P.negativeAlgebraics(), P.naturalIntegersGeometric())
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, ps)) {
            BigDecimal floor = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.FLOOR);
            BigDecimal down = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.DOWN);
            BigDecimal ceiling = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.CEILING);
            BigDecimal up = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.UP);
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
            Algebraic x = of(bd);
            BigDecimal down = x.bigDecimalValueByPrecision(precision, RoundingMode.DOWN);
            BigDecimal up = x.bigDecimalValueByPrecision(precision, RoundingMode.UP);
            BigDecimal halfDown = x.bigDecimalValueByPrecision(precision, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = x.bigDecimalValueByPrecision(precision, RoundingMode.HALF_UP);
            BigDecimal halfEven = x.bigDecimalValueByPrecision(precision, RoundingMode.HALF_EVEN);
            BigDecimal closest = lt(x.subtract(of(down)).abs(), x.subtract(of(up)).abs()) ? down : up;
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
            Algebraic x = of(bd);
            BigDecimal down = x.bigDecimalValueByPrecision(precision, RoundingMode.DOWN);
            BigDecimal up = x.bigDecimalValueByPrecision(precision, RoundingMode.UP);
            BigDecimal halfDown = x.bigDecimalValueByPrecision(precision, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = x.bigDecimalValueByPrecision(precision, RoundingMode.HALF_UP);
            BigDecimal halfEven = x.bigDecimalValueByPrecision(precision, RoundingMode.HALF_EVEN);
            assertEquals(bd, down, halfDown);
            assertEquals(bd, up, halfUp);
            assertTrue(bd, bd.scale() != halfEven.scale() + 1 || !halfEven.unscaledValue().testBit(0));
        }

        Iterable<Triple<Algebraic, Integer, RoundingMode>> tsFail = P.triples(
                P.nonzeroAlgebraics(),
                P.negativeIntegers(),
                P.roundingModes()
        );
        for (Triple<Algebraic, Integer, RoundingMode> t : take(LIMIT, tsFail)) {
            try {
                t.a.bigDecimalValueByPrecision(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException | ArithmeticException ignored) {}
        }

        Iterable<Pair<Algebraic, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(x -> !x.hasTerminatingBaseExpansion(BigInteger.TEN), P.algebraics()),
                P.naturalIntegersGeometric()
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.bigDecimalValueByPrecision(p.b, RoundingMode.UNNECESSARY);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesBigDecimalValueByScale_int_RoundingMode() {
        initialize("bigDecimalValueByScale(int, RoundingMode)");
        Predicate<Triple<Algebraic, Integer, RoundingMode>> valid =
                t -> t.c != RoundingMode.UNNECESSARY ||
                        t.a.isRational() && t.a.multiply(Rational.TEN.pow(t.b)).isInteger();
        Iterable<Triple<Algebraic, Integer, RoundingMode>> ts = filterInfinite(
                valid,
                P.triples(P.algebraics(), P.integersGeometric(), P.roundingModes())
        );
        for (Triple<Algebraic, Integer, RoundingMode> t : take(LIMIT, ts)) {
            BigDecimal bd = t.a.bigDecimalValueByScale(t.b, t.c);
            assertTrue(t, bd.scale() == t.b);
            assertTrue(t, eq(bd, BigDecimal.ZERO) || bd.signum() == t.a.signum());
            assertEquals(t, t.a.realValue().bigDecimalValueByScaleUnsafe(t.b, t.c), bd);
        }

        Iterable<Pair<Algebraic, Integer>> ps = filterInfinite(
                q -> q.a.multiply(Rational.TEN.pow(q.b)).isInteger(),
                P.pairsSquareRootOrder(P.algebraics(1), P.integersGeometric())
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValueByScale(p.b, RoundingMode.UNNECESSARY);
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.FLOOR));
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.CEILING));
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.DOWN));
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.UP));
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.HALF_DOWN));
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.HALF_UP));
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.HALF_EVEN));
        }

        ps = filterInfinite(
                q -> !q.a.isRational() || !q.a.multiply(Rational.TEN.pow(q.b)).isInteger(),
                P.pairsSquareRootOrder(P.algebraics(), P.integersGeometric())
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, ps)) {
            BigDecimal low = p.a.bigDecimalValueByScale(p.b, RoundingMode.FLOOR);
            BigDecimal high = p.a.bigDecimalValueByScale(p.b, RoundingMode.CEILING);
            Rational lowR = Rational.of(low);
            Rational highR = Rational.of(high);
            assertTrue(p, gt(p.a, of(lowR)));
            assertTrue(p, lt(p.a, of(highR)));
            assertEquals(p, highR.subtract(lowR), Rational.TEN.pow(-p.b));
        }

        ps = filterInfinite(
                q -> !q.a.isRational() || !q.a.multiply(Rational.TEN.pow(q.b)).isInteger(),
                P.pairsSquareRootOrder(P.positiveAlgebraics(), P.integersGeometric())
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, ps)) {
            BigDecimal floor = p.a.bigDecimalValueByScale(p.b, RoundingMode.FLOOR);
            BigDecimal down = p.a.bigDecimalValueByScale(p.b, RoundingMode.DOWN);
            BigDecimal ceiling = p.a.bigDecimalValueByScale(p.b, RoundingMode.CEILING);
            BigDecimal up = p.a.bigDecimalValueByScale(p.b, RoundingMode.UP);
            assertEquals(p, floor, down);
            assertEquals(p, ceiling, up);
        }

        ps = filterInfinite(
                q -> !q.a.isRational() || !q.a.multiply(Rational.TEN.pow(q.b)).isInteger(),
                P.pairsSquareRootOrder(P.negativeAlgebraics(), P.integersGeometric())
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, ps)) {
            BigDecimal floor = p.a.bigDecimalValueByScale(p.b, RoundingMode.FLOOR);
            BigDecimal down = p.a.bigDecimalValueByScale(p.b, RoundingMode.DOWN);
            BigDecimal ceiling = p.a.bigDecimalValueByScale(p.b, RoundingMode.CEILING);
            BigDecimal up = p.a.bigDecimalValueByScale(p.b, RoundingMode.UP);
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
            Algebraic x = of(bd);
            BigDecimal down = x.bigDecimalValueByScale(scale, RoundingMode.DOWN);
            BigDecimal up = x.bigDecimalValueByScale(scale, RoundingMode.UP);
            BigDecimal halfDown = x.bigDecimalValueByScale(scale, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = x.bigDecimalValueByScale(scale, RoundingMode.HALF_UP);
            BigDecimal halfEven = x.bigDecimalValueByScale(scale, RoundingMode.HALF_EVEN);
            BigDecimal closest = lt(x.subtract(of(down)).abs(), x.subtract(of(up)).abs()) ? down : up;
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
            Algebraic x = of(bd);
            BigDecimal down = x.bigDecimalValueByScale(scale, RoundingMode.DOWN);
            BigDecimal up = x.bigDecimalValueByScale(scale, RoundingMode.UP);
            BigDecimal halfDown = x.bigDecimalValueByScale(scale, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = x.bigDecimalValueByScale(scale, RoundingMode.HALF_UP);
            BigDecimal halfEven = x.bigDecimalValueByScale(scale, RoundingMode.HALF_EVEN);
            assertEquals(bd, down, halfDown);
            assertEquals(bd, up, halfUp);
            assertTrue(bd, !halfEven.unscaledValue().testBit(0));
        }

        Iterable<Pair<Algebraic, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(x -> !x.hasTerminatingBaseExpansion(BigInteger.TEN), P.algebraics()),
                P.naturalIntegersGeometric()
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.bigDecimalValueByScale(p.b, RoundingMode.UNNECESSARY);
                fail(p);
            } catch (IllegalArgumentException | ArithmeticException ignored) {}
        }
    }

    private void propertiesBigDecimalValueByPrecision_int() {
        initialize("bigDecimalValueByPrecision(int)");
        Predicate<Pair<Algebraic, Integer>> valid = p -> {
            try {
                p.a.bigDecimalValueByPrecision(p.b);
                return true;
            } catch (ArithmeticException e) {
                return false;
            }
        };
        Iterable<Pair<Algebraic, Integer>> ps = filterInfinite(
                valid,
                P.pairsSquareRootOrder(P.algebraics(), P.naturalIntegersGeometric())
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValueByPrecision(p.b);
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.HALF_EVEN));
            assertTrue(p, eq(bd, BigDecimal.ZERO) || bd.signum() == p.a.signum());
            assertEquals(p, p.a.realValue().bigDecimalValueByPrecisionUnsafe(p.b), bd);
        }

        ps = filterInfinite(valid, P.pairsSquareRootOrder(P.nonzeroAlgebraics(), P.positiveIntegersGeometric()));
        for (Pair<Algebraic, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValueByPrecision(p.b);
            assertTrue(p, bd.precision() == p.b);
        }

        BigInteger five = BigInteger.valueOf(5);
        Iterable<BigDecimal> bds = filterInfinite(
                bd -> bd.precision() > 1 && !bd.abs().unscaledValue().mod(BigInteger.TEN).equals(five),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int precision = bd.precision() - 1;
            Algebraic x = of(bd);
            BigDecimal down = x.bigDecimalValueByPrecision(precision, RoundingMode.DOWN);
            BigDecimal up = x.bigDecimalValueByPrecision(precision, RoundingMode.UP);
            BigDecimal halfEven = x.bigDecimalValueByPrecision(precision);
            boolean closerToDown = lt(x.subtract(of(down)).abs(), x.subtract(of(up)).abs());
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
            Algebraic x = of(bd);
            BigDecimal halfEven = x.bigDecimalValueByPrecision(precision);
            assertTrue(bd, bd.scale() != halfEven.scale() + 1 || !halfEven.unscaledValue().testBit(0));
        }

        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairs(P.algebraics(), P.negativeIntegers()))) {
            try {
                p.a.bigDecimalValueByPrecision(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesBigDecimalValueByScale_int() {
        initialize("bigDecimalValueByScale(int)");
        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairsSquareRootOrder(P.algebraics(), P.integersGeometric()))) {
            BigDecimal bd = p.a.bigDecimalValueByScale(p.b);
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.HALF_EVEN));
            assertTrue(p, eq(bd, BigDecimal.ZERO) || bd.signum() == p.a.signum());
            assertTrue(p, bd.scale() == p.b);
            assertEquals(p, p.a.realValue().bigDecimalValueByScaleUnsafe(p.b), bd);
        }

        BigInteger five = BigInteger.valueOf(5);
        Iterable<BigDecimal> bds = filterInfinite(
                bd -> !bd.abs().unscaledValue().mod(BigInteger.TEN).equals(five),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int scale = bd.scale() - 1;
            Algebraic x = of(bd);
            BigDecimal down = x.bigDecimalValueByScale(scale, RoundingMode.DOWN);
            BigDecimal up = x.bigDecimalValueByScale(scale, RoundingMode.UP);
            BigDecimal halfEven = x.bigDecimalValueByScale(scale);
            BigDecimal closer = lt(x.subtract(of(down)).abs(), x.subtract(of(up)).abs()) ? down : up;
            assertEquals(bd, halfEven, closer);
        }

        bds = map(
                bd -> new BigDecimal(bd.unscaledValue().multiply(BigInteger.TEN).add(five), bd.scale()),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int scale = bd.scale() - 1;
            Algebraic x = of(bd);
            BigDecimal halfEven = x.bigDecimalValueByScale(scale);
            assertTrue(bd, !halfEven.unscaledValue().testBit(0));
        }
    }

    private void propertiesBigDecimalValueExact() {
        initialize("bigDecimalValueExact()");
        Iterable<Algebraic> xs = map(
                Algebraic::of,
                filterInfinite(r -> r.hasTerminatingBaseExpansion(BigInteger.TEN), P.rationals())
        );
        for (Algebraic x : take(LIMIT, xs)) {
            BigDecimal bd = x.bigDecimalValueExact();
            assertTrue(bd, BigDecimalUtils.isCanonical(bd));
            assertEquals(x, bd, x.bigDecimalValueByPrecision(0, RoundingMode.UNNECESSARY));
            assertEquals(x, bd.signum(), x.signum());
            inverse(Algebraic::bigDecimalValueExact, Algebraic::of, x);
            homomorphic(
                    Algebraic::negate,
                    BigDecimal::negate,
                    Algebraic::bigDecimalValueExact,
                    Algebraic::bigDecimalValueExact,
                    x
            );
            assertEquals(x, x.realValue().bigDecimalValueExact(), bd);
        }

        Iterable<Algebraic> xsFail = filterInfinite(
                x -> !x.hasTerminatingBaseExpansion(BigInteger.TEN),
                P.algebraics()
        );
        for (Algebraic x : take(LIMIT, xsFail)) {
            try {
                x.bigDecimalValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesMinimalPolynomial() {
        initialize("minimalPolynomial()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            Polynomial p = x.minimalPolynomial();
            assertNotEquals(x, p, ZERO);
            assertTrue(x, p.isIrreducible());
        }
    }

    private void propertiesRootIndex() {
        initialize("rootIndex()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            int rootIndex = x.rootIndex();
            assertTrue(x, rootIndex >= 0);
            assertTrue(x, rootIndex < x.minimalPolynomialRootCount());
        }
    }

    private void propertiesDegree() {
        initialize("degree()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            int degree = x.degree();
            assertTrue(x, degree > 0);
        }
    }

    private void propertiesIsolatingInterval() {
        initialize("isolatingInterval()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            Interval a = x.isolatingInterval();
            assertTrue(x, a.isFinitelyBounded());
        }

        for (Algebraic x : take(LIMIT, filterInfinite(y -> !y.isRational(), P.algebraics()))) {
            Interval a = x.isolatingInterval();
            assertTrue(x, a.getLower().get().isBinaryFraction());
            assertTrue(x, a.getUpper().get().isBinaryFraction());
            assertEquals(x, a, x.minimalPolynomial().powerOfTwoIsolatingInterval(x.rootIndex()));
        }
    }

    private void propertiesMinimalPolynomialRootCount() {
        initialize("minimalPolynomialRootCount()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            int rootCount = x.minimalPolynomialRootCount();
            assertTrue(x, rootCount > 0);
            assertTrue(x, rootCount <= x.degree());
        }
    }

    private void propertiesIntervalExtension() {
        initialize("intervalExtension(Algebraic, Algebraic)");
        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.subsetPairs(P.algebraics()))) {
            Interval extension = intervalExtension(p.a, p.b);
            assertTrue(p, extension.isFinitelyBounded());
            assertTrue(
                    p,
                    p.b.realValue().subtract(p.a.realValue()).geUnsafe(extension.diameter().get().shiftRight(1))
            );
            assertEquals(p, Real.intervalExtensionUnsafe(p.a.realValue(), p.b.realValue()), extension);
        }

        Iterable<Pair<Algebraic, Algebraic>> ps = P.subsetPairs(filterInfinite(y -> !y.isRational(), P.algebraics()));
        for (Pair<Algebraic, Algebraic> p : take(LIMIT, ps)) {
            Interval extension = intervalExtension(p.a, p.b);
            assertTrue(p, extension.getLower().get().isBinaryFraction());
            assertTrue(p, extension.getUpper().get().isBinaryFraction());
        }

        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.bagPairs(P.algebraics()))) {
            try {
                intervalExtension(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            Algebraic negative = x.negate();
            negative.validate();
            involution(Algebraic::negate, x);
            assertTrue(x, x.add(negative) == ZERO);
            assertTrue(x, x.realValue().negate().eq(negative.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        for (Algebraic x : take(LIMIT, P.nonzeroAlgebraics())) {
            assertNotEquals(x, x, x.negate());
        }
    }

    private void propertiesAbs() {
        initialize("abs()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            Algebraic abs = x.abs();
            abs.validate();
            idempotent(Algebraic::abs, x);
            assertNotEquals(x, abs.signum(), -1);
            assertTrue(x, ge(abs, ZERO));
            assertTrue(x, x.realValue().abs().eq(abs.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        for (Algebraic x : take(LIMIT, P.positiveAlgebraics())) {
            fixedPoint(Algebraic::abs, x);
        }
    }

    private void propertiesSignum() {
        initialize("testing signum()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            int signum = x.signum();
            assertEquals(x, signum, Ordering.compare(x, ZERO).toInt());
            assertTrue(x, signum == -1 || signum == 0 || signum == 1);
            assertEquals(x, x.realValue().signumUnsafe(), signum);
        }
    }

    private void propertiesAdd_BigInteger() {
        initialize("add(BigInteger)");
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.algebraics(), P.bigIntegers()))) {
            Algebraic sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum.degree(), p.a.degree());
            inverse(x -> x.add(p.b), (Algebraic x) -> x.subtract(p.b), p.a);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(y -> x.add(BigInteger.ZERO), x);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            fixedPoint(j -> ZERO.add(j).bigIntegerValueExact(), i);
        }
    }

    private void propertiesAdd_Rational() {
        initialize("add(Rational)");
        for (Pair<Algebraic, Rational> p : take(LIMIT, P.pairs(P.algebraics(), P.rationals()))) {
            Algebraic sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum.degree(), p.a.degree());
            inverse(x -> x.add(p.b), (Algebraic x) -> x.subtract(p.b), p.a);
            assertTrue(p, p.a.realValue().add(p.b).eq(sum.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(y -> x.add(Rational.ZERO), x);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            fixedPoint(s -> ZERO.add(s).rationalValueExact(), r);
        }
    }

    private void propertiesAdd_Algebraic() {
        initialize("add(Algebraic)");
        Iterable<Pair<Algebraic, Algebraic>> ps = P.pairs(P.withScale(1).withSecondaryScale(4).algebraics());
        for (Pair<Algebraic, Algebraic> p : take(SMALL_LIMIT, ps)) {
            Algebraic sum = p.a.add(p.b);
            sum.validate();
            assertTrue(p, sum.degree() <= p.a.degree() * p.b.degree());
            commutative(Algebraic::add, p);
            inverse(x -> x.add(p.b), (Algebraic x) -> x.subtract(p.b), p.a);
            assertTrue(
                    p,
                    p.a.realValue().add(p.b.realValue()).eq(sum.realValue(), Real.DEFAULT_RESOLUTION).orElse(true)
            );
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(ZERO::add, x);
            fixedPoint(y -> x.add(ZERO), x);
            assertEquals(x, x.add(x.negate()), ZERO);
        }

        Iterable<Triple<Algebraic, Algebraic, Algebraic>> ts = P.triples(
                P.withScale(1).withSecondaryScale(4).algebraics()
        );
        for (Triple<Algebraic, Algebraic, Algebraic> t : take(SMALL_LIMIT, ts)) {
            associative(Algebraic::add, t);
        }
    }

    private void propertiesSubtract_BigInteger() {
        initialize("subtract(BigInteger)");
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.algebraics(), P.bigIntegers()))) {
            Algebraic difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, difference.degree(), p.a.degree());
            inverse(x -> x.subtract(p.b), (Algebraic x) -> x.add(p.b), p.a);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(y -> x.subtract(BigInteger.ZERO), x);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ZERO.subtract(i).bigIntegerValueExact(), i.negate());
        }
    }

    private void propertiesSubtract_Rational() {
        initialize("subtract(Rational)");
        for (Pair<Algebraic, Rational> p : take(LIMIT, P.pairs(P.algebraics(), P.rationals()))) {
            Algebraic difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, difference.degree(), p.a.degree());
            inverse(x -> x.subtract(p.b), (Algebraic x) -> x.add(p.b), p.a);
            assertTrue(
                    p,
                    p.a.realValue().subtract(p.b).eq(difference.realValue(), Real.DEFAULT_RESOLUTION).orElse(true)
            );
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(y -> x.subtract(Rational.ZERO), x);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ZERO.subtract(r).rationalValueExact(), r.negate());
        }
    }

    private void propertiesSubtract_Algebraic() {
        initialize("subtract(Algebraic)");
        Iterable<Pair<Algebraic, Algebraic>> ps = P.pairs(P.withScale(1).withSecondaryScale(4).algebraics());
        for (Pair<Algebraic, Algebraic> p : take(SMALL_LIMIT, ps)) {
            Algebraic difference = p.a.subtract(p.b);
            difference.validate();
            assertTrue(p, difference.degree() <= p.a.degree() * p.b.degree());
            antiCommutative(Algebraic::subtract, Algebraic::negate, p);
            inverse(x -> x.subtract(p.b), (Algebraic x) -> x.add(p.b), p.a);
            assertTrue(
                    p,
                    p.a.realValue().subtract(p.b.realValue()).eq(difference.realValue(), Real.DEFAULT_RESOLUTION)
                            .orElse(true)
            );
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            assertEquals(x, ZERO.subtract(x), x.negate());
            fixedPoint(y -> x.subtract(ZERO), x);
            assertEquals(x, x.subtract(x), ZERO);
        }
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairs(P.algebraics(), P.integers()))) {
            Algebraic product = p.a.multiply(p.b);
            product.validate();
            assertEquals(p, product, p.a.multiply(of(p.b)));
            assertEquals(p, product, of(p.b).multiply(p.a));
            assertTrue(p, p.a.realValue().multiply(p.b).eq(product.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairs(P.algebraics(), P.nonzeroIntegers()))) {
            inverse(x -> x.multiply(p.b), (Algebraic x) -> x.divide(p.b), p.a);
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(i, ONE.multiply(i), of(i));
            fixedPoint(r -> r.multiply(i), ZERO);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(y -> y.multiply(1), x);
            assertEquals(x, x.multiply(0), ZERO);
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            assertEquals(i, of(i).invert().multiply(i), ONE);
        }

        Iterable<Triple<Algebraic, Algebraic, Integer>> ts = P.triples(
                P.withScale(1).withSecondaryScale(4).algebraics(),
                P.withScale(1).withSecondaryScale(4).algebraics(),
                P.integers()
        );
        for (Triple<Algebraic, Algebraic, Integer> t : take(SMALL_LIMIT, ts)) {
            rightDistributive(Algebraic::add, Algebraic::multiply, new Triple<>(t.c, t.a, t.b));
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.algebraics(), P.bigIntegers()))) {
            Algebraic product = p.a.multiply(p.b);
            product.validate();
            assertEquals(p, product, p.a.multiply(of(p.b)));
            assertEquals(p, product, of(p.b).multiply(p.a));
            assertTrue(p, p.a.realValue().multiply(p.b).eq(product.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.algebraics(), P.nonzeroBigIntegers()))) {
            inverse(x -> x.multiply(p.b), (Algebraic x) -> x.divide(p.b), p.a);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ONE.multiply(i), of(i));
            fixedPoint(x -> x.multiply(i), ZERO);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(y -> y.multiply(BigInteger.ONE), x);
            assertEquals(x, x.multiply(BigInteger.ZERO), ZERO);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            assertEquals(i, of(i).invert().multiply(i), ONE);
        }

        Iterable<Triple<Algebraic, Algebraic, BigInteger>> ts = P.triples(
                P.withScale(1).withSecondaryScale(4).algebraics(),
                P.withScale(1).withSecondaryScale(4).algebraics(),
                P.bigIntegers()
        );
        for (Triple<Algebraic, Algebraic, BigInteger> t : take(SMALL_LIMIT, ts)) {
            rightDistributive(Algebraic::add, Algebraic::multiply, new Triple<>(t.c, t.a, t.b));
        }
    }

    private void propertiesMultiply_Rational() {
        initialize("multiply(Rational)");
        for (Pair<Algebraic, Rational> p : take(LIMIT, P.pairs(P.algebraics(), P.rationals()))) {
            Algebraic product = p.a.multiply(p.b);
            product.validate();
            assertEquals(p, product, p.a.multiply(of(p.b)));
            assertEquals(p, product, of(p.b).multiply(p.a));
            assertTrue(p, p.a.realValue().multiply(p.b).eq(product.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Algebraic, Rational> p : take(LIMIT, P.pairs(P.algebraics(), P.nonzeroRationals()))) {
            inverse(x -> x.multiply(p.b), (Algebraic x) -> x.divide(p.b), p.a);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ONE.multiply(r), of(r));
            fixedPoint(x -> x.multiply(r), ZERO);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(y -> y.multiply(Rational.ONE), x);
            assertEquals(x, x.multiply(Rational.ZERO), ZERO);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            assertEquals(r, of(r).invert().multiply(r), ONE);
        }

        Iterable<Triple<Algebraic, Algebraic, Rational>> ts = P.triples(
                P.withScale(1).withSecondaryScale(4).algebraics(),
                P.withScale(1).withSecondaryScale(4).algebraics(),
                P.rationals()
        );
        for (Triple<Algebraic, Algebraic, Rational> t : take(SMALL_LIMIT, ts)) {
            rightDistributive(Algebraic::add, Algebraic::multiply, new Triple<>(t.c, t.a, t.b));
        }
    }

    private void propertiesMultiply_Algebraic() {
        initialize("multiply(Algebraic)");
        Iterable<Pair<Algebraic, Algebraic>> ps = P.pairs(P.withScale(1).withSecondaryScale(4).algebraics());
        for (Pair<Algebraic, Algebraic> p : take(SMALL_LIMIT, ps)) {
            Algebraic product = p.a.multiply(p.b);
            product.validate();
            commutative(Algebraic::multiply, p);
            assertTrue(
                    p,
                    p.a.realValue().multiply(p.b.realValue()).eq(product.realValue(), Real.DEFAULT_RESOLUTION)
                            .orElse(true)
            );
        }

        ps = P.pairs(
                filterInfinite(x -> x.degree() < 5, P.withScale(1).withSecondaryScale(4).algebraics()),
                filterInfinite(x -> x.degree() < 5, P.withScale(1).withSecondaryScale(4).nonzeroAlgebraics())
        );
        for (Pair<Algebraic, Algebraic> p : take(TINY_LIMIT, ps)) {
            inverse(x -> x.multiply(p.b), (Algebraic x) -> x.divide(p.b), p.a);
            assertEquals(p, p.a.multiply(p.b), p.a.divide(p.b.invert()));
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(x::multiply, ZERO);
            fixedPoint(y -> y.multiply(x), ZERO);
            fixedPoint(ONE::multiply, x);
            fixedPoint(y -> y.multiply(ONE), x);
        }

        for (Algebraic x : take(LIMIT, P.nonzeroAlgebraics())) {
            assertTrue(x, x.multiply(x.invert()) == ONE);
        }

        Iterable<Triple<Algebraic, Algebraic, Algebraic>> ts = P.triples(
                filterInfinite(x -> x.degree() < 5, P.withScale(1).withSecondaryScale(4).algebraics())
        );
        for (Triple<Algebraic, Algebraic, Algebraic> t : take(TINY_LIMIT, ts)) {
            associative(Algebraic::multiply, t);
            leftDistributive(Algebraic::add, Algebraic::multiply, t);
            rightDistributive(Algebraic::add, Algebraic::multiply, t);
        }
    }

    private void propertiesInvert() {
        initialize("invert()");
        for (Algebraic x : take(LIMIT, P.nonzeroAlgebraics())) {
            Algebraic inverse = x.invert();
            inverse.validate();
            involution(Algebraic::invert, x);
            assertTrue(x, x.multiply(inverse) == ONE);
            assertTrue(x, inverse != ZERO);
            assertTrue(x, x.realValue().invertUnsafe().eq(inverse.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> s.abs() != ONE, P.nonzeroAlgebraics()))) {
            assertTrue(x, !x.equals(x.invert()));
        }
    }

    private void propertiesDivide_int() {
        initialize("divide(int)");
        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairs(P.algebraics(), P.nonzeroIntegers()))) {
            Algebraic quotient = p.a.divide(p.b);
            quotient.validate();
            inverse(x -> x.divide(p.b), (Algebraic x) -> x.multiply(p.b), p.a);
            assertTrue(p, p.a.realValue().divide(p.b).eq(quotient.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairs(P.nonzeroAlgebraics(), P.nonzeroIntegers()))) {
            assertEquals(p, p.a.divide(p.b), of(p.b).divide(p.a).invert());
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            assertEquals(i, ONE.divide(i), of(i).invert());
            assertEquals(i, of(i).divide(i), ONE);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(y -> y.divide(1), x);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            try {
                x.divide(0);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_BigInteger() {
        initialize("divide(BigInteger)");
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.algebraics(), P.nonzeroBigIntegers()))) {
            Algebraic quotient = p.a.divide(p.b);
            quotient.validate();
            inverse(x -> x.divide(p.b), (Algebraic x) -> x.multiply(p.b), p.a);
            assertTrue(p, p.a.realValue().divide(p.b).eq(quotient.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.nonzeroAlgebraics(), P.nonzeroBigIntegers()))) {
            assertEquals(p, p.a.divide(p.b), of(p.b).divide(p.a).invert());
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            assertEquals(i, ONE.divide(i), of(i).invert());
            assertEquals(i, of(i).divide(i), ONE);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(y -> y.divide(BigInteger.ONE), x);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            try {
                x.divide(BigInteger.ZERO);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_Rational() {
        initialize("divide(Rational)");
        for (Pair<Algebraic, Rational> p : take(LIMIT, P.pairs(P.algebraics(), P.nonzeroRationals()))) {
            Algebraic quotient = p.a.divide(p.b);
            quotient.validate();
            inverse(x -> x.divide(p.b), (Algebraic x) -> x.multiply(p.b), p.a);
            assertTrue(p, p.a.realValue().divide(p.b).eq(quotient.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Algebraic, Rational> p : take(LIMIT, P.pairs(P.nonzeroAlgebraics(), P.nonzeroRationals()))) {
            assertEquals(p, p.a.divide(p.b), of(p.b).divide(p.a).invert());
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            assertEquals(r, ONE.divide(r), of(r).invert());
            assertEquals(r, of(r).divide(r), ONE);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(y -> y.divide(Rational.ONE), x);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            try {
                x.divide(Rational.ZERO);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_Algebraic() {
        initialize("divide(Algebraic)");
        Iterable<Pair<Algebraic, Algebraic>> ps = P.pairs(
                P.withScale(1).withSecondaryScale(4).algebraics(),
                P.withScale(1).withSecondaryScale(4).nonzeroAlgebraics()
        );
        for (Pair<Algebraic, Algebraic> p : take(SMALL_LIMIT, ps)) {
            Algebraic quotient = p.a.divide(p.b);
            quotient.validate();
            assertEquals(p, quotient, p.a.multiply(p.b.invert()));
            inverse(x -> x.divide(p.b), (Algebraic x) -> x.multiply(p.b), p.a);
            assertTrue(
                    p,
                    p.a.realValue().divideUnsafe(p.b.realValue()).eq(quotient.realValue(), Real.DEFAULT_RESOLUTION)
                            .orElse(true)
            );
        }

        ps = P.pairs(P.withScale(1).withSecondaryScale(4).nonzeroAlgebraics());
        for (Pair<Algebraic, Algebraic> p : take(SMALL_LIMIT, ps)) {
            assertEquals(p, p.a.divide(p.b), p.b.divide(p.a).invert());
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(y -> y.divide(ONE), x);
        }

        for (Algebraic x : take(LIMIT, P.nonzeroAlgebraics())) {
            assertEquals(x, ONE.divide(x), x.invert());
            assertTrue(x, x.divide(x) == ONE);
            fixedPoint(y -> y.divide(x), ZERO);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            try {
                x.divide(ZERO);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull Algebraic shiftLeft_simplest(@NotNull Algebraic x, int bits) {
        if (bits < 0) {
            return x.divide(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return x.multiply(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairs(P.algebraics(), P.integersGeometric()))) {
            homomorphic(
                    Algebraic::negate,
                    Function.identity(),
                    Algebraic::negate,
                    Algebraic::shiftLeft,
                    Algebraic::shiftLeft,
                    p
            );
            Algebraic shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            inverse(x -> x.shiftLeft(p.b), (Algebraic x) -> x.shiftRight(p.b), p.a);
            assertEquals(p, shifted, p.a.shiftRight(-p.b));
            assertTrue(
                    p,
                    p.a.realValue().shiftLeft(p.b).eq(shifted.realValue(), Real.DEFAULT_RESOLUTION).orElse(true)
            );
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(y -> y.shiftLeft(0), x);
        }

        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairs(P.algebraics(), P.naturalIntegersGeometric()))) {
            assertEquals(p, p.a.shiftLeft(p.b), p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<Algebraic, Integer>, Algebraic>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        compareImplementations(
                "shiftLeft(int)",
                take(LIMIT, P.pairs(P.algebraics(), P.integersGeometric())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull Algebraic shiftRight_simplest(@NotNull Algebraic x, int bits) {
        if (bits < 0) {
            return x.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return x.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftRight() {
        initialize("shiftRight(int)");
        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairs(P.algebraics(), P.integersGeometric()))) {
            homomorphic(
                    Algebraic::negate,
                    Function.identity(),
                    Algebraic::negate,
                    Algebraic::shiftRight,
                    Algebraic::shiftRight,
                    p
            );
            Algebraic shifted = p.a.shiftRight(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftRight_simplest(p.a, p.b));
            inverse(x -> x.shiftRight(p.b), (Algebraic x) -> x.shiftLeft(p.b), p.a);
            assertEquals(p, shifted, p.a.shiftLeft(-p.b));
            assertTrue(
                    p,
                    p.a.realValue().shiftRight(p.b).eq(shifted.realValue(), Real.DEFAULT_RESOLUTION).orElse(true)
            );
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            fixedPoint(y -> y.shiftRight(0), x);
        }

        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairs(P.algebraics(), P.naturalIntegersGeometric()))) {
            assertEquals(p, p.a.shiftRight(p.b), p.a.divide(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private void compareImplementationsShiftRight() {
        Map<String, Function<Pair<Algebraic, Integer>, Algebraic>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftRight_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        compareImplementations(
                "shiftRight(int)",
                take(LIMIT, P.pairs(P.algebraics(), P.integersGeometric())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull Algebraic sum_simplest(@NotNull List<Algebraic> xs) {
        return foldl(Algebraic::add, ZERO, xs);
    }

    private void propertiesSum() {
        initialize("sum(List<Algebraic>)");
        Iterable<List<Algebraic>> xss = filterInfinite(
                yss -> productInteger(toList(map(Algebraic::degree, yss))) <= 10,
                P.withScale(1).lists(P.withScale(1).withSecondaryScale(4).algebraics())
        );
        for (List<Algebraic> xs : take(SMALL_LIMIT, xss)) {
            Algebraic sum = sum(xs);
            sum.validate();
            assertEquals(xs, sum(xs), sum_simplest(xs));
            assertTrue(
                    xs,
                    Real.sum(toList(map(Algebraic::realValue, xs))).eq(sum.realValue(), Real.DEFAULT_RESOLUTION)
                            .orElse(true)
            );
        }

        Iterable<Pair<List<Algebraic>, List<Algebraic>>> ps = filterInfinite(
                q -> !q.a.equals(q.b),
                P.dependentPairs(xss, P::permutationsFinite)
        );
        for (Pair<List<Algebraic>, List<Algebraic>> p : take(SMALL_LIMIT, ps)) {
            assertEquals(p, sum(p.a), sum(p.b));
        }

        for (Algebraic x : take(LIMIT, P.withScale(1).withSecondaryScale(4).algebraics())) {
            assertEquals(x, sum(Collections.singletonList(x)), x);
        }

        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.pairs(P.withScale(1).withSecondaryScale(4).algebraics()))) {
            assertEquals(p, sum(Arrays.asList(p.a, p.b)), p.a.add(p.b));
        }

        Iterable<List<Algebraic>> xssFail = filterInfinite(
                ys -> !ys.isEmpty(),
                P.listsWithElement(null, P.withScale(1).algebraics())
        );
        for (List<Algebraic> xs : take(LIMIT, xssFail)) {
            try {
                sum(xs);
                fail(xs);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsSum() {
        Polynomial.USE_FACTOR_CACHE = false;
        Algebraic.USE_SUM_CACHE = false;
        Algebraic.USE_PRODUCT_CACHE = false;
        Map<String, Function<List<Algebraic>, Algebraic>> functions = new LinkedHashMap<>();
        functions.put("simplest", AlgebraicProperties::sum_simplest);
        functions.put("standard", Algebraic::sum);
        Iterable<List<Algebraic>> xss = P.withScale(1).lists(P.withScale(1).withSecondaryScale(4).algebraics());
        compareImplementations("sum(Iterable<Algebraic>)", take(SMALL_LIMIT, xss), functions, v -> P.reset());
        Polynomial.USE_FACTOR_CACHE = true;
        Algebraic.USE_SUM_CACHE = true;
        Algebraic.USE_PRODUCT_CACHE = true;
    }

    private static @NotNull Algebraic product_simplest(@NotNull List<Algebraic> xs) {
        return foldl(Algebraic::multiply, ONE, xs);
    }

    private void propertiesProduct() {
        initialize("product(List<Algebraic>)");
        Iterable<List<Algebraic>> xss = filterInfinite(
                yss -> productInteger(toList(map(Algebraic::degree, yss))) <= 10,
                P.withScale(1).lists(P.withScale(1).withSecondaryScale(4).algebraics())
        );
        for (List<Algebraic> xs : take(SMALL_LIMIT, xss)) {
            Algebraic product = product(xs);
            product.validate();
            assertEquals(xs, product(xs), product_simplest(xs));
            assertTrue(
                    xs,
                    Real.product(toList(map(Algebraic::realValue, xs)))
                            .eq(product.realValue(), Real.DEFAULT_RESOLUTION).orElse(true)
            );
        }

        Iterable<Pair<List<Algebraic>, List<Algebraic>>> ps = filterInfinite(
                q -> !q.a.equals(q.b),
                P.dependentPairs(xss, P::permutationsFinite)
        );
        for (Pair<List<Algebraic>, List<Algebraic>> p : take(SMALL_LIMIT, ps)) {
            assertEquals(p, product_simplest(p.a), product(p.b));
        }

        for (Algebraic x : take(LIMIT, P.withScale(1).withSecondaryScale(4).algebraics())) {
            assertEquals(x, product(Collections.singletonList(x)), x);
        }

        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.pairs(P.withScale(1).withSecondaryScale(4).algebraics()))) {
            assertEquals(p, product(Arrays.asList(p.a, p.b)), p.a.multiply(p.b));
        }

        Iterable<List<Algebraic>> xssFail = filterInfinite(
                ys -> !ys.isEmpty(),
                P.listsWithElement(null, P.withScale(1).algebraics())
        );
        for (List<Algebraic> xs : take(LIMIT, xssFail)) {
            try {
                product(xs);
                fail(xs);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsProduct() {
        Polynomial.USE_FACTOR_CACHE = false;
        Algebraic.USE_SUM_CACHE = false;
        Algebraic.USE_PRODUCT_CACHE = false;
        Map<String, Function<List<Algebraic>, Algebraic>> functions = new LinkedHashMap<>();
        functions.put("simplest", AlgebraicProperties::sum_simplest);
        functions.put("standard", Algebraic::sum);
        Iterable<List<Algebraic>> xss = P.withScale(1).lists(P.withScale(1).withSecondaryScale(4).algebraics());
        compareImplementations("product(List<Algebraic>)", take(SMALL_LIMIT, xss), functions, v -> P.reset());
        Polynomial.USE_FACTOR_CACHE = true;
        Algebraic.USE_SUM_CACHE = true;
        Algebraic.USE_PRODUCT_CACHE = true;
    }

    private static int sumSign_simplest(@NotNull List<Algebraic> xs) {
        return sum(xs).signum();
    }

    private static int sumSign_alt(@NotNull List<Algebraic> xs) {
        switch (xs.size()) {
            case 0:
                return 0;
            case 1:
                return xs.get(0).signum();
            default:
                return Integer.signum(sum(toList(tail(xs))).compareTo(head(xs).negate()));
        }
    }

    public static int sumSign_alt2(@NotNull List<Algebraic> xs) {
        if (any(Objects::isNull, xs)) {
            throw new NullPointerException();
        }
        switch (xs.size()) {
            case 0:
                return 0;
            case 1:
                return xs.get(0).signum();
            default:
                Map<String, Function<Void, Integer>> implementations = new HashMap<>();
                implementations.put("real", v -> Real.sum(toList(map(Algebraic::realValue, xs))).signumUnsafe());
                implementations.put("algebraic", v -> {
                    List<Algebraic> positives = new ArrayList<>();
                    List<Algebraic> negatives = new ArrayList<>();
                    for (Algebraic x : xs) {
                        int signum = x.signum();
                        if (signum == 1) {
                            positives.add(x);
                        } else if (signum == -1) {
                            negatives.add(x);
                        }
                    }
                    int positiveSize = positives.size();
                    int negativeSize = negatives.size();
                    if (positiveSize == 0 && negativeSize == 0) {
                        return 0;
                    } else if (positiveSize == 0) {
                        return -1;
                    } else if (negativeSize == 0) {
                        return 1;
                    } else if (positiveSize < negativeSize) {
                        Algebraic positiveSum = sum(positives).negate();
                        Algebraic negativeSum = ZERO;
                        for (Algebraic negative : negatives) {
                            negativeSum = negativeSum.add(negative);
                            if (lt(negativeSum, positiveSum)) return -1;
                        }
                        return negativeSum.equals(positiveSum) ? 0 : 1;
                    } else {
                        Algebraic negativeSum = sum(negatives).negate();
                        Algebraic positiveSum = ZERO;
                        for (Algebraic positive : positives) {
                            positiveSum = positiveSum.add(positive);
                            if (gt(positiveSum, negativeSum)) return 1;
                        }
                        return negativeSum.equals(positiveSum) ? 0 : -1;
                    }
                });
            return ConcurrencyUtils.evaluateFastest(implementations, null).b;
        }
    }

    private void propertiesSumSign() {
        initialize("sumSign(List<Algebraic>)");
        Iterable<List<Algebraic>> xss = P.withScale(1).lists(P.withScale(1).withSecondaryScale(4).algebraics());
        for (List<Algebraic> xs : take(SMALL_LIMIT, xss)) {
            int sumSign = sumSign(xs);
            assertEquals(xs, sumSign_simplest(xs), sumSign);
            assertEquals(xs, sumSign_alt(xs), sumSign);
            assertEquals(xs, sumSign_alt2(xs), sumSign);
            assertTrue(xs, sumSign == 0 || sumSign == 1 || sumSign == -1);
            assertEquals(xs, sumSign(toList(map(Algebraic::negate, xs))), -sumSign);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            assertEquals(x, sumSign(Collections.singletonList(x)), x.signum());
        }

        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.pairs(P.withScale(1).withSecondaryScale(4).algebraics()))) {
            assertEquals(p, sumSign(Pair.toList(p)), Integer.signum(p.a.compareTo(p.b.negate())));
        }

        for (List<Algebraic> xs : take(LIMIT, P.listsWithElement(null, P.algebraics()))) {
            try {
                sumSign(xs);
                fail(xs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void compareImplementationsSumSign() {
        Polynomial.USE_FACTOR_CACHE = false;
        Algebraic.USE_SUM_CACHE = false;
        Algebraic.USE_PRODUCT_CACHE = false;
        Map<String, Function<List<Algebraic>, Integer>> functions = new LinkedHashMap<>();
        functions.put("simplest", AlgebraicProperties::sumSign_simplest);
        functions.put("alt", AlgebraicProperties::sumSign_alt);
        functions.put("alt2", AlgebraicProperties::sumSign_alt2);
        functions.put("standard", Algebraic::sumSign);
        Iterable<List<Algebraic>> xss = filterInfinite(
                yss -> productInteger(toList(map(Algebraic::degree, yss))) <= 20,
                P.withScale(1).lists(P.withScale(1).withSecondaryScale(4).algebraics())
        );
        compareImplementations("sumSign(List<Algebraic>)", take(MEDIUM_LIMIT, xss), functions, v -> P.reset());
        Polynomial.USE_FACTOR_CACHE = true;
        Algebraic.USE_SUM_CACHE = true;
        Algebraic.USE_PRODUCT_CACHE = true;
    }

    private void propertiesDelta() {
        initialize("delta(Iterable<Algebraic>)");
        Iterable<List<Algebraic>> xss = P.withScale(2).listsAtLeast(
                1,
                P.withScale(1).withSecondaryScale(4).algebraics()
        );
        for (List<Algebraic> xs : take(SMALL_LIMIT, xss)) {
            Iterable<Algebraic> deltas = delta(xs);
            deltas.forEach(Algebraic::validate);
            aeq(xs, length(deltas), length(xs) - 1);
            Iterable<Algebraic> reversed = reverse(map(Algebraic::negate, delta(reverse(xs))));
            aeqit(xs, deltas, reversed);
            testNoRemove(TINY_LIMIT, deltas);
            testHasNext(deltas);
            assertTrue(
                    xs,
                    and(
                            zipWith(
                                    (x, y) -> x.eq(y.realValue(), Real.DEFAULT_RESOLUTION).orElse(true),
                                    Real.delta(map(Algebraic::realValue, xs)),
                                    deltas
                            )
                    )
            );
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            assertTrue(x, isEmpty(delta(Collections.singletonList(x))));
        }

        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.pairs(P.withScale(1).withSecondaryScale(4).algebraics()))) {
            aeqit(p, delta(Pair.toList(p)), Collections.singletonList(p.b.subtract(p.a)));
        }

        for (Iterable<Algebraic> xs : take(SMALL_LIMIT, P.prefixPermutations(QBarTesting.QEP.algebraics()))) {
            Iterable<Algebraic> deltas = delta(xs);
            List<Algebraic> deltaPrefix = toList(take(TINY_LIMIT, deltas));
            deltaPrefix.forEach(Algebraic::validate);
            aeq(IterableUtils.toString(TINY_LIMIT, xs), length(deltaPrefix), TINY_LIMIT);
            testNoRemove(TINY_LIMIT, deltas);
        }

        Iterable<List<Algebraic>> xssFail = P.withScale(3).listsWithElement(
                null,
                P.withScale(1).withSecondaryScale(4).algebraics()
        );
        for (List<Algebraic> xs : take(LIMIT, xssFail)) {
            try {
                toList(delta(xs));
                fail(xs);
            } catch (NullPointerException ignored) {}
        }
    }

    private static @NotNull Algebraic pow_int_alt(@NotNull Algebraic x, int p) {
        if (p == 0 || x == ONE) return ONE;
        if (p == 1) return x;
        if (p < 0) return pow_int_alt(x.invert(), -p);
        if (x == ZERO) return x;
        if (p % 2 == 0 && x.equals(NEGATIVE_ONE)) return ONE;
        if (x.isRational()) {
            return of(x.rationalValueExact().pow(p));
        }
        Optional<Polynomial> oPowerMP = x.minimalPolynomial().undoRootRoots(p);
        if (oPowerMP.isPresent()) {
            Polynomial powerMp = oPowerMP.get();
            int powerRootIndex = x.rootIndex();
            if ((p & 1) == 0) {
                int negativeRootCount = x.minimalPolynomial().rootCount(Interval.lessThanOrEqualTo(Rational.ZERO));
                int powerNegativeRootCount = powerMp.rootCount(Interval.lessThanOrEqualTo(Rational.ZERO));
                if (x.signum() == 1) {
                    powerRootIndex = powerNegativeRootCount - negativeRootCount + x.rootIndex();
                } else {
                    powerRootIndex = negativeRootCount + powerNegativeRootCount - x.rootIndex() - 1;
                }
            }
            return of(powerMp, powerRootIndex);
        }

        Polynomial mp = x.minimalPolynomial();
        if (mp.isMonic()) {
            return mp.rootPower(p).apply(x);
        } else {
            return mp.toRationalPolynomial().makeMonic().rootPower(p).apply(x);
        }
    }

    private static @NotNull Algebraic pow_int_alt2(@NotNull Algebraic x, int p) {
        if (p == 0 || x == ONE) return ONE;
        if (p == 1) return x;
        if (p < 0) return pow_int_alt2(x.invert(), -p);
        if (x == ZERO) return x;
        if (p % 2 == 0 && x.equals(NEGATIVE_ONE)) return ONE;
        if (x.isRational()) {
            return of(x.rationalValueExact().pow(p));
        }
        Optional<Polynomial> oPowerMP = x.minimalPolynomial().undoRootRoots(p);
        if (oPowerMP.isPresent()) {
            Polynomial powerMp = oPowerMP.get();
            int powerRootIndex = x.rootIndex();
            if ((p & 1) == 0) {
                int negativeRootCount = x.minimalPolynomial().rootCount(Interval.lessThanOrEqualTo(Rational.ZERO));
                int powerNegativeRootCount = powerMp.rootCount(Interval.lessThanOrEqualTo(Rational.ZERO));
                if (x.signum() == 1) {
                    powerRootIndex = powerNegativeRootCount - negativeRootCount + x.rootIndex();
                } else {
                    powerRootIndex = negativeRootCount + powerNegativeRootCount - x.rootIndex() - 1;
                }
            }
            return of(powerMp, powerRootIndex);
        }

        Map<String, Function<Pair<Algebraic, Integer>, Algebraic>> implementations = new HashMap<>();
        implementations.put("reducing", q -> q.a.pow(q.b));
        implementations.put("halving", q -> pow_int_alt(q.a, q.b));
        return ConcurrencyUtils.evaluateFastest(implementations, new Pair<>(x, p)).b;
    }

    private void propertiesPow_int() {
        initialize("pow(int)");
        Iterable<Pair<Algebraic, Integer>> ps = filterInfinite(
                p -> p.a != ZERO || p.b >= 0,
                P.pairsSquareRootOrder(
                        P.withScale(1).withSecondaryScale(4).algebraics(),
                        P.withScale(1).integersGeometric()
                )
        );
        for (Pair<Algebraic, Integer> p : take(MEDIUM_LIMIT, ps)) {
            Algebraic x = p.a.pow(p.b);
            x.validate();
            if (p.b != 0 && ((p.b & 1) == 1 || p.a.signum() != -1)) {
                inverse(y -> y.pow(p.b), (Algebraic y) -> y.root(p.b), p.a);
            }
            assertEquals(p, x, pow_int_alt(p.a, p.b));
            assertEquals(p, x, pow_int_alt2(p.a, p.b));
            assertTrue(p, p.a.realValue().powUnsafe(p.b).eq(x.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        ps = P.pairs(P.withScale(1).withSecondaryScale(4).nonzeroAlgebraics(), P.withScale(1).integersGeometric());
        for (Pair<Algebraic, Integer> p : take(SMALL_LIMIT, ps)) {
            homomorphic(Function.identity(), i -> -i, Algebraic::invert, Algebraic::pow, Algebraic::pow, p);
            homomorphic(Algebraic::invert, i -> -i, Function.identity(), Algebraic::pow, Algebraic::pow, p);
        }

        for (int i : take(LIMIT, P.positiveIntegersGeometric())) {
            fixedPoint(j -> j.pow(i), ZERO);
        }

        for (Algebraic x : take(MEDIUM_LIMIT, P.withScale(1).withSecondaryScale(4).algebraics())) {
            assertTrue(x, x.pow(0) == ONE);
            fixedPoint(y -> y.pow(1), x);
            assertEquals(x, x.pow(2), x.multiply(x));
        }

        for (Algebraic x : take(LIMIT, P.nonzeroAlgebraics())) {
            assertEquals(x, x.pow(-1), x.invert());
        }

        Iterable<Triple<Algebraic, Integer, Integer>> ts = filterInfinite(
                t -> t.a != ZERO || (t.b >= 0 && t.c >= 0),
                P.triples(
                        P.withScale(1).withSecondaryScale(4).algebraics(),
                        P.withScale(1).integersGeometric(),
                        P.withScale(1).integersGeometric()
                )
        );
        for (Triple<Algebraic, Integer, Integer> t : take(SMALL_LIMIT, ts)) {
            Algebraic expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            Algebraic expression2 = t.a.pow(t.b + t.c);
            assertEquals(t, expression1, expression2);
            Algebraic expression3 = t.a.pow(t.b).pow(t.c);
            Algebraic expression4 = t.a.pow(t.b * t.c);
            assertEquals(t, expression3, expression4);
        }

        ts = filterInfinite(
                t -> t.a != ZERO || (t.c == 0 && t.b >= 0),
                P.triples(
                        P.withScale(1).withSecondaryScale(4).algebraics(),
                        P.withScale(1).integersGeometric(),
                        P.withScale(1).integersGeometric()
                )
        );
        for (Triple<Algebraic, Integer, Integer> t : take(SMALL_LIMIT, ts)) {
            Algebraic expression1 = t.a.pow(t.b).divide(t.a.pow(t.c));
            Algebraic expression2 = t.a.pow(t.b - t.c);
            assertEquals(t, expression1, expression2);
        }

        Iterable<Triple<Algebraic, Algebraic, Integer>> ts2 = filter(
                t -> (t.a != ZERO && t.b != ZERO) || t.c >= 0,
                P.triples(
                        P.withScale(1).withSecondaryScale(4).algebraics(),
                        P.withScale(1).withSecondaryScale(4).algebraics(),
                        P.withScale(2).positiveIntegersGeometric()
                )
        );
        for (Triple<Algebraic, Algebraic, Integer> t : take(TINY_LIMIT, ts2)) {
            Algebraic expression1 = t.a.multiply(t.b).pow(t.c);
            Algebraic expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        ts2 = filterInfinite(
                t -> t.a != ZERO || t.c >= 0,
                P.triples(
                        filterInfinite(x -> x.degree() < 5, P.withScale(1).withSecondaryScale(4).algebraics()),
                        filterInfinite(x -> x.degree() < 5, P.withScale(1).withSecondaryScale(4).nonzeroAlgebraics()),
                        P.withScale(2).positiveIntegersGeometric()
                )
        );
        for (Triple<Algebraic, Algebraic, Integer> t : take(TINY_LIMIT, ts2)) {
            Algebraic expression1 = t.a.divide(t.b).pow(t.c);
            Algebraic expression2 = t.a.pow(t.c).divide(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                ZERO.pow(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsPow_int() {
        Polynomial.USE_FACTOR_CACHE = false;
        Algebraic.USE_SUM_CACHE = false;
        Algebraic.USE_PRODUCT_CACHE = false;
        Map<String, Function<Pair<Algebraic, Integer>, Algebraic>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> pow_int_alt(p.a, p.b));
        functions.put("alt2", p -> pow_int_alt2(p.a, p.b));
        functions.put("standard", p -> p.a.pow(p.b));
        Iterable<Pair<Algebraic, Integer>> ps = filterInfinite(
                p -> p.a != ZERO || p.b >= 0,
                P.pairsSquareRootOrder(
                        P.withScale(1).withSecondaryScale(4).algebraics(),
                        P.withScale(1).integersGeometric()
                )
        );
        compareImplementations("pow(int)", take(MEDIUM_LIMIT, ps), functions, v -> P.reset());
        Polynomial.USE_FACTOR_CACHE = true;
        Algebraic.USE_SUM_CACHE = true;
        Algebraic.USE_PRODUCT_CACHE = true;
    }

    private void propertiesRootOfRational() {
        initialize("rootOfRational(Rational, int)");
        Iterable<Pair<Rational, Integer>> ps = filterInfinite(
                p -> (p.a != Rational.ZERO || p.b >= 0) && ((p.b & 1) != 0 || p.a.signum() != -1),
                P.pairsSquareRootOrder(P.rationals(), P.withScale(2).nonzeroIntegersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            Algebraic x = rootOfRational(p.a, p.b);
            x.validate();
            //noinspection SuspiciousNameCombination
            inverse(y -> rootOfRational(y, p.b), (Algebraic y) -> y.pow(p.b).rationalValueExact(), p.a);
            if ((p.b & 1) == 0) {
                assertNotEquals(p, x.signum(), -1);
            } else {
                assertEquals(p, x.signum(), p.a.signum());
            }
            assertTrue(p, Real.rootOfRational(p.a, p.b).eq(x.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        ps = filterInfinite(
                p -> (p.b & 1) != 0 || p.a.signum() != -1,
                P.pairsSquareRootOrder(P.nonzeroRationals(), P.nonzeroIntegersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            homomorphic(
                    Function.identity(),
                    i -> -i,
                    Algebraic::invert,
                    Algebraic::rootOfRational,
                    Algebraic::rootOfRational,
                    p
            );
            homomorphic(
                    Rational::invert,
                    i -> -i,
                    Function.identity(),
                    Algebraic::rootOfRational,
                    Algebraic::rootOfRational,
                    p
            );
        }

        for (int i : take(LIMIT, P.positiveIntegersGeometric())) {
            fixedPoint(j -> rootOfRational(j, i).rationalValueExact(), Rational.ZERO);
        }

        for (Rational x : take(LIMIT, P.rationals())) {
            //noinspection SuspiciousNameCombination
            fixedPoint(y -> rootOfRational(y, 1).rationalValueExact(), x);
        }

        for (Rational x : take(LIMIT, P.nonzeroRationals())) {
            assertEquals(x, rootOfRational(x, -1).rationalValueExact(), x.invert());
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
            Algebraic y = sqrtOfRational(x);
            x.validate();
            inverse(Algebraic::sqrtOfRational, (Algebraic z) -> z.pow(2).rationalValueExact(), x);
            assertNotEquals(x, y.signum(), -1);
            assertTrue(x, Real.sqrtOfRational(x).eq(y.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
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
            Algebraic y = cbrtOfRational(x);
            x.validate();
            inverse(Algebraic::cbrtOfRational, (Algebraic z) -> z.pow(3).rationalValueExact(), x);
            assertEquals(x, y.signum(), x.signum());
            assertTrue(x, Real.cbrtOfRational(x).eq(y.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }
    }

    private void propertiesRoot() {
        initialize("root(int)");
        Iterable<Pair<Algebraic, Integer>> ps = filterInfinite(
                p -> (p.a != ZERO || p.b >= 0) && ((p.b & 1) != 0 || p.a.signum() != -1),
                P.pairsSquareRootOrder(P.withScale(4).algebraics(), P.withScale(2).nonzeroIntegersGeometric())
        );
        for (Pair<Algebraic, Integer> p : take(MEDIUM_LIMIT, ps)) {
            Algebraic x = p.a.root(p.b);
            x.validate();
            inverse(y -> y.root(p.b), (Algebraic y) -> y.pow(p.b), p.a);
            if ((p.b & 1) == 0) {
                assertNotEquals(p, x.signum(), -1);
            } else {
                assertEquals(p, x.signum(), p.a.signum());
            }
            assertTrue(p, p.a.realValue().rootUnsafe(p.b).eq(x.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        ps = filterInfinite(
                p -> (p.b & 1) != 0 || p.a.signum() != -1,
                P.pairsSquareRootOrder(P.withScale(4).nonzeroAlgebraics(), P.withScale(2).nonzeroIntegersGeometric())
        );
        for (Pair<Algebraic, Integer> p : take(MEDIUM_LIMIT, ps)) {
            homomorphic(Function.identity(), i -> -i, Algebraic::invert, Algebraic::root, Algebraic::root, p);
            homomorphic(Algebraic::invert, i -> -i, Function.identity(), Algebraic::root, Algebraic::root, p);
        }

        for (int i : take(LIMIT, P.positiveIntegersGeometric())) {
            fixedPoint(j -> j.root(i), ZERO);
        }

        for (Algebraic x : take(MEDIUM_LIMIT, P.withScale(4).algebraics())) {
            fixedPoint(y -> y.root(1), x);
        }

        for (Algebraic x : take(LIMIT, P.nonzeroAlgebraics())) {
            assertEquals(x, x.root(-1), x.invert());
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            try {
                x.root(0);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                ZERO.root(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Algebraic, Integer>> psFail = P.pairs(
                P.negativeAlgebraics(),
                map(i -> i << 1, P.integersGeometric())
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.root(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesSqrt() {
        initialize("sqrt()");
        for (Algebraic x : take(LIMIT, P.rangeUp(ZERO))) {
            Algebraic y = x.sqrt();
            y.validate();
            inverse(Algebraic::sqrt, (Algebraic z) -> z.pow(2), x);
            assertNotEquals(x, x.signum(), -1);
            assertTrue(x, x.realValue().sqrtUnsafe().eq(y.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.pairs(P.withScale(4).rangeUp(ZERO)))) {
            assertEquals(p, p.a.compareTo(p.b), p.a.sqrt().compareTo(p.b.sqrt()));
        }

        for (Algebraic x : take(LIMIT, P.negativeAlgebraics())) {
            try {
                x.sqrt();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesCbrt() {
        initialize("cbrt()");
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            Algebraic y = x.cbrt();
            y.validate();
            inverse(Algebraic::cbrt, (Algebraic z) -> z.pow(3), x);
            assertEquals(x, y.signum(), x.signum());
            assertTrue(x, x.realValue().cbrt().eq(y.realValue(), Real.DEFAULT_RESOLUTION).orElse(true));
        }

        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.pairs(P.withScale(4).algebraics()))) {
            assertEquals(p, p.a.compareTo(p.b), p.a.cbrt().compareTo(p.b.cbrt()));
        }
    }

    private static @NotNull Algebraic pow_Rational_alt(@NotNull Algebraic x, @NotNull Rational p) {
        return x.root(p.getDenominator().intValueExact()).pow(p.getNumerator().intValueExact());
    }

    private void propertiesPow_Rational() {
        initialize("pow(Rational)");
        BigInteger lower = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger upper = BigInteger.valueOf(Integer.MAX_VALUE);
        Iterable<Rational> rs = filterInfinite(
                r -> ge(r.getNumerator(), lower) && le(r.getNumerator(), upper) && le(r.getDenominator(), upper),
                P.withScale(3).rationals()
        );
        Iterable<Pair<Algebraic, Rational>> ps = filterInfinite(
                p -> (p.a != ZERO || p.b.signum() != -1) && (p.a.signum() != -1 || p.b.getDenominator().testBit(0)),
                P.pairsSquareRootOrder(P.withScale(1).withSecondaryScale(4).algebraics(), rs)
        );
        for (Pair<Algebraic, Rational> p : take(SMALL_LIMIT, ps)) {
            Algebraic x = p.a.pow(p.b);
            x.validate();
            assertTrue(
                    p,
                    p.a.realValue().powUnsafe(Real.of(p.b)).eq(x.realValue(), Real.DEFAULT_RESOLUTION).orElse(true)
            );
        }

        BigInteger five = BigInteger.valueOf(5);
        Iterable<Rational> simpleRs = filterInfinite(
                r -> le(r.getNumerator().abs(), five) && le(r.getDenominator(), five),
                P.withScale(3).rationals()
        );
        ps = filterInfinite(
                p -> (p.a != ZERO || p.b.signum() != -1) && (p.a.signum() != -1 || p.b.getDenominator().testBit(0)),
                P.pairsSquareRootOrder(P.withScale(1).withSecondaryScale(4).algebraics(), simpleRs)
        );
        for (Pair<Algebraic, Rational> p : take(SMALL_LIMIT, ps)) {
            assertEquals(p, p.a.pow(p.b), pow_Rational_alt(p.a, p.b));
            if (p.b != Rational.ZERO && (p.b.getNumerator().testBit(0) || p.a.signum() != -1)) {
                inverse(y -> y.pow(p.b), (Algebraic y) -> y.pow(p.b.invert()), p.a);
            }
        }

        ps = filterInfinite(
                p -> p.b.getDenominator().testBit(0) || p.a.signum() == 1,
                P.pairs(P.withScale(1).withSecondaryScale(4).nonzeroAlgebraics(), simpleRs)
        );
        for (Pair<Algebraic, Rational> p : take(SMALL_LIMIT, ps)) {
            homomorphic(Function.identity(), Rational::negate, Algebraic::invert, Algebraic::pow, Algebraic::pow, p);
            homomorphic(Algebraic::invert, Rational::negate, Function.identity(), Algebraic::pow, Algebraic::pow, p);
        }

        Iterable<Rational> positiveRs = filterInfinite(
                r -> ge(r.getNumerator(), lower) && le(r.getNumerator(), upper) && le(r.getDenominator(), upper),
                P.withScale(4).positiveRationals()
        );
        for (Rational r : take(LIMIT, positiveRs)) {
            fixedPoint(j -> j.pow(r), ZERO);
        }

        for (Algebraic x : take(MEDIUM_LIMIT, P.withScale(1).withSecondaryScale(4).algebraics())) {
            assertTrue(x, x.pow(Rational.ZERO) == ONE);
            fixedPoint(y -> y.pow(Rational.ONE), x);
            assertEquals(x, x.pow(Rational.TWO), x.multiply(x));
        }

        for (Algebraic x : take(LIMIT, P.nonzeroAlgebraics())) {
            assertEquals(x, x.pow(Rational.NEGATIVE_ONE), x.invert());
        }

        Iterable<Triple<Algebraic, Rational, Rational>> ts = filterInfinite(
                t -> (t.a != ZERO || (t.b.signum() != -1 && t.c.signum() != -1)),
                P.triples(
                        filterInfinite(x -> x.degree() < 5, P.withScale(1).withSecondaryScale(4).positiveAlgebraics()),
                        simpleRs,
                        simpleRs
                )
        );
        for (Triple<Algebraic, Rational, Rational> t : take(SMALL_LIMIT, ts)) {
            Algebraic expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            Algebraic expression2 = t.a.pow(t.b.add(t.c));
            assertEquals(t, expression1, expression2);
            Algebraic expression3 = t.a.pow(t.b).pow(t.c);
            Algebraic expression4 = t.a.pow(t.b.multiply(t.c));
            assertEquals(t, expression3, expression4);
        }

        ts = filterInfinite(
                t -> t.a != ZERO || (t.c == Rational.ZERO && t.b.signum() != -1),
                P.triples(
                        filterInfinite(x -> x.degree() < 5, P.withScale(1).withSecondaryScale(4).positiveAlgebraics()),
                        simpleRs,
                        simpleRs
                )
        );
        for (Triple<Algebraic, Rational, Rational> t : take(SMALL_LIMIT, ts)) {
            Algebraic expression1 = t.a.pow(t.b).divide(t.a.pow(t.c));
            Algebraic expression2 = t.a.pow(t.b.subtract(t.c));
            assertEquals(t, expression1, expression2);
        }

        Iterable<Triple<Algebraic, Algebraic, Rational>> ts2 = P.triples(
                filterInfinite(x -> x.degree() < 5, P.withScale(1).withSecondaryScale(4).positiveAlgebraics()),
                filterInfinite(x -> x.degree() < 5, P.withScale(1).withSecondaryScale(4).positiveAlgebraics()),
                simpleRs
        );
        for (Triple<Algebraic, Algebraic, Rational> t : take(TINY_LIMIT, ts2)) {
            Algebraic expression1 = t.a.multiply(t.b).pow(t.c);
            Algebraic expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        ts2 = P.triples(
                filterInfinite(x -> x.degree() < 5, P.withScale(1).withSecondaryScale(4).positiveAlgebraics()),
                filterInfinite(x -> x.degree() < 5, P.withScale(1).withSecondaryScale(4).positiveAlgebraics()),
                simpleRs
        );
        for (Triple<Algebraic, Algebraic, Rational> t : take(SMALL_LIMIT, ts2)) {
            Algebraic expression1 = t.a.divide(t.b).pow(t.c);
            Algebraic expression2 = t.a.pow(t.c).divide(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                ZERO.pow(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Algebraic, Rational>> psFail = P.pairs(
                P.negativeAlgebraics(),
                filterInfinite(r -> !r.getDenominator().testBit(0), rs)
        );
        for (Pair<Algebraic, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.pow(p.b);
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
        for (Pair<Algebraic, Rational> p : take(LIMIT, P.pairs(P.algebraics(), rsFail))) {
            try {
                p.a.pow(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsPow_Rational() {
        Polynomial.USE_FACTOR_CACHE = false;
        Algebraic.USE_SUM_CACHE = false;
        Algebraic.USE_PRODUCT_CACHE = false;
        Map<String, Function<Pair<Algebraic, Rational>, Algebraic>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> pow_Rational_alt(p.a, p.b));
        functions.put("standard", p -> p.a.pow(p.b));
        BigInteger five = BigInteger.valueOf(5);
        Iterable<Rational> simpleRs = filterInfinite(
                r -> le(r.getNumerator().abs(), five) && le(r.getDenominator(), five),
                P.withScale(3).rationals()
        );
        Iterable<Pair<Algebraic, Rational>> ps = filterInfinite(
                p -> (p.a != ZERO || p.b.signum() != -1) && (p.a.signum() != -1 || p.b.getDenominator().testBit(0)),
                P.pairsSquareRootOrder(P.withScale(1).withSecondaryScale(4).algebraics(), simpleRs)
        );
        compareImplementations("pow(Rational)", take(SMALL_LIMIT, ps), functions, v -> P.reset());
        Polynomial.USE_FACTOR_CACHE = true;
        Algebraic.USE_SUM_CACHE = true;
        Algebraic.USE_PRODUCT_CACHE = true;
    }

    private void propertiesFractionalPart() {
        initialize("fractionalPart()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            Algebraic fractionalPart = x.fractionalPart();
            fractionalPart.validate();
            assertTrue(x, ge(fractionalPart, ZERO));
            assertTrue(x, lt(fractionalPart, ONE));
            assertEquals(x, of(x.floor()).add(fractionalPart), x);
            assertTrue(
                    x,
                    x.realValue().fractionalPartUnsafe().eq(fractionalPart.realValue(), Real.DEFAULT_RESOLUTION)
                            .orElse(true)
            );
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).fractionalPart(), ZERO);
        }
    }

    private void propertiesRoundToDenominator() {
        initialize("roundToDenominator(BigInteger, RoundingMode)");
        Iterable<Triple<Algebraic, BigInteger, RoundingMode>> ts = filter(
                p -> p.c != RoundingMode.UNNECESSARY ||
                        p.a.isRational() && p.b.mod(p.a.rationalValueExact().getDenominator()).equals(BigInteger.ZERO),
                P.triples(P.withScale(4).algebraics(), P.positiveBigIntegers(), P.roundingModes())
        );
        for (Triple<Algebraic, BigInteger, RoundingMode> t : take(LIMIT, ts)) {
            Rational rounded = t.a.roundToDenominator(t.b, t.c);
            rounded.validate();
            assertEquals(t, t.b.mod(rounded.getDenominator()), BigInteger.ZERO);
            assertTrue(t, rounded == Rational.ZERO || rounded.signum() == t.a.signum());
            assertTrue(t, lt(t.a.subtract(rounded).abs(), of(Rational.of(BigInteger.ONE, t.b))));
            assertEquals(t, t.a.realValue().roundToDenominatorUnsafe(t.b, t.c), rounded);
        }

        Iterable<Pair<Algebraic, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isInteger(),
                P.pairs(P.algebraics(), P.roundingModes())
        );
        for (Pair<Algebraic, RoundingMode> p : take(LIMIT, ps)) {
            Rational rounded = p.a.roundToDenominator(BigInteger.ONE, p.b);
            assertEquals(p, rounded.getNumerator(), p.a.bigIntegerValue(p.b));
            assertEquals(p, rounded.getDenominator(), BigInteger.ONE);
        }

        Iterable<Pair<Algebraic, BigInteger>> ps2 = filterInfinite(
                p -> p.b.mod(p.a.rationalValueExact().getDenominator()).equals(BigInteger.ZERO),
                P.pairs(P.algebraics(1), P.positiveBigIntegers())
        );
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, ps2)) {
            assertTrue(p, p.a.roundToDenominator(p.b, RoundingMode.UNNECESSARY).equals(p.a.rationalValueExact()));
        }

        ps2 = P.pairs(P.withScale(4).algebraics(), P.positiveBigIntegers());
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, ps2)) {
            assertTrue(p, le(of(p.a.roundToDenominator(p.b, RoundingMode.FLOOR)), p.a));
            assertTrue(p, ge(of(p.a.roundToDenominator(p.b, RoundingMode.CEILING)), p.a));
            assertTrue(p, le(of(p.a.roundToDenominator(p.b, RoundingMode.DOWN).abs()), p.a.abs()));
            assertTrue(p, ge(of(p.a.roundToDenominator(p.b, RoundingMode.UP).abs()), p.a.abs()));
            Algebraic resolution = of(Rational.of(p.b).shiftLeft(1).invert());
            assertTrue(p, le(p.a.subtract(p.a.roundToDenominator(p.b, RoundingMode.HALF_DOWN)).abs(), resolution));
            assertTrue(p, le(p.a.subtract(p.a.roundToDenominator(p.b, RoundingMode.HALF_UP)).abs(), resolution));
            assertTrue(p, le(p.a.subtract(p.a.roundToDenominator(p.b, RoundingMode.HALF_EVEN)).abs(), resolution));
        }

        ps2 = filterInfinite(
                p -> lt(p.a.abs().multiply(p.b).fractionalPart(), ONE_HALF),
                P.pairs(P.withScale(4).algebraics(), P.positiveBigIntegers())
        );
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, ps2)) {
            Rational down = p.a.roundToDenominator(p.b, RoundingMode.DOWN);
            assertEquals(p, p.a.roundToDenominator(p.b, RoundingMode.HALF_DOWN), down);
            assertEquals(p, p.a.roundToDenominator(p.b, RoundingMode.HALF_UP), down);
            assertEquals(p, p.a.roundToDenominator(p.b, RoundingMode.HALF_EVEN), down);
        }

        ps2 = filterInfinite(
                p -> gt(p.a.abs().multiply(p.b).fractionalPart(), ONE_HALF),
                P.pairs(P.withScale(4).algebraics(), P.positiveBigIntegers())
        );
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, ps2)) {
            Rational up = p.a.roundToDenominator(p.b, RoundingMode.UP);
            assertEquals(p, p.a.roundToDenominator(p.b, RoundingMode.HALF_DOWN), up);
            assertEquals(p, p.a.roundToDenominator(p.b, RoundingMode.HALF_UP), up);
            assertEquals(p, p.a.roundToDenominator(p.b, RoundingMode.HALF_EVEN), up);
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.getDenominator().testBit(0), P.rationals()))) {
            Algebraic x = of(r);
            BigInteger denominator = r.getDenominator().shiftRight(1);
            assertEquals(x, x.abs().multiply(denominator).fractionalPart(), ONE_HALF);
            Rational hd = x.roundToDenominator(denominator, RoundingMode.HALF_DOWN);
            assertEquals(x, hd, x.roundToDenominator(denominator, RoundingMode.DOWN));
            Rational hu = x.roundToDenominator(denominator, RoundingMode.HALF_UP);
            assertEquals(x, hu, x.roundToDenominator(denominator, RoundingMode.UP));
            Rational he = x.roundToDenominator(denominator, RoundingMode.HALF_EVEN);
            assertFalse(r, he.multiply(denominator).getNumerator().testBit(0));
        }

        Iterable<Triple<Algebraic, BigInteger, RoundingMode>> tsFail = P.triples(
                P.algebraics(),
                P.rangeDown(BigInteger.ZERO),
                P.roundingModes()
        );
        for (Triple<Algebraic, BigInteger, RoundingMode> t : take(LIMIT, tsFail)) {
            try {
                t.a.roundToDenominator(t.b, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Algebraic, BigInteger>> psFail = filterInfinite(
                p -> !p.a.isRational() || !p.b.mod(p.a.rationalValueExact().getDenominator()).equals(BigInteger.ZERO),
                P.pairs(P.algebraics(), P.positiveBigIntegers())
        );
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, psFail)) {
            try {
                p.a.roundToDenominator(p.b, RoundingMode.UNNECESSARY);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesRealConjugates() {
        initialize("realConjugates()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            List<Algebraic> realConjugates = x.realConjugates();
            assertEquals(x, realConjugates.size(), x.minimalPolynomialRootCount() - 1);
            Polynomial mp = x.minimalPolynomial();
            int rootIndex = x.rootIndex();
            for (Algebraic realConjugate : realConjugates) {
                realConjugate.validate();
                assertEquals(x, realConjugate.minimalPolynomial(), mp);
                assertNotEquals(x, realConjugate.rootIndex(), rootIndex);
            }
        }
    }

    private void propertiesIsReducedSurd() {
        initialize("isReducedSurd()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            x.isReducedSurd();
        }

        Iterable<Algebraic> xs = P.withScale(1).choose(P.withScale(4).algebraics(1), P.withScale(1).algebraics(2));
        for (Algebraic x : take(LIMIT, xs)) {
            assertEquals(x, x.isReducedSurd(), x.repeatedContinuedFraction().a.isEmpty());
        }
    }

    public static @NotNull Iterable<BigInteger> continuedFraction_alt(@NotNull Algebraic x) {
        if (x.isRational()) {
            return x.rationalValueExact().continuedFraction();
        } else {
            return x.realValue().continuedFractionUnsafe();
        }
    }

    private void propertiesContinuedFraction() {
        initialize("continuedFraction()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            List<BigInteger> continuedFraction = toList(take(TINY_LIMIT, x.continuedFraction()));
            assertEquals(x, toList(take(TINY_LIMIT, continuedFraction_alt(x))), continuedFraction);
            assertFalse(x, continuedFraction.isEmpty());
            assertTrue(x, all(Objects::nonNull, continuedFraction));
            assertTrue(x, all(i -> i.signum() == 1, tail(continuedFraction)));
        }
    }

    private void compareImplementationsIsContinuedFraction() {
        Map<String, Function<Algebraic, Iterable<BigInteger>>> functions = new LinkedHashMap<>();
        functions.put("alt", AlgebraicProperties::continuedFraction_alt);
        functions.put("standard", Algebraic::continuedFraction);
        compareImplementations("continuedFraction()", take(LIMIT, P.algebraics()), functions, v -> P.reset());
    }

    private void propertiesRepeatedContinuedFraction() {
        initialize("repeatedContinuedFraction()");
        Iterable<Algebraic> xs = P.withScale(1).choose(P.withScale(4).algebraics(1), P.withScale(1).algebraics(2));
        for (Algebraic x : take(LIMIT, xs)) {
            Pair<List<BigInteger>, List<BigInteger>> cf = x.repeatedContinuedFraction();
            assertNotNull(x, cf.a);
            assertNotNull(x, cf.b);
            assertTrue(x, all(Objects::nonNull, cf.a));
            if (!cf.a.isEmpty()) {
                assertTrue(x, all(i -> i.signum() == 1, tail(cf.a)));
            }
            assertTrue(x, all(i -> i.signum() == 1, cf.b));
            assertTrue(x, !cf.a.isEmpty() || !cf.b.isEmpty());
            Pair<List<BigInteger>, List<BigInteger>> minimized = minimize(cf.a, cf.b);
            assertEquals(x, minimized.a, cf.a);
            assertEquals(x, minimized.b, cf.b);
            assertEquals(x, cf.b.isEmpty(), x.isRational());
            inverse(Algebraic::repeatedContinuedFraction, p -> fromContinuedFraction(p.a, p.b), x);
        }

        for (Algebraic x : take(LIMIT, filterInfinite(y -> y.degree() >= 3, P.algebraics()))) {
            try {
                x.repeatedContinuedFraction();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesFromContinuedFraction() {
        initialize("fromContinuedFraction(List<BigInteger>, List<BigInteger>)");
        Iterable<Pair<List<BigInteger>, List<BigInteger>>> ps = filterInfinite(
                p -> !p.a.isEmpty() || !p.b.isEmpty(),
                P.pairs(
                        P.withElement(
                                Collections.emptyList(),
                                zipWith(
                                        (i, is) -> toList(cons(i, is)),
                                        P.withScale(1).bigIntegers(),
                                        P.withScale(4).lists(P.withScale(2).positiveBigIntegers())
                                )
                        ),
                        P.withScale(4).lists(P.withScale(2).positiveBigIntegers())
                )
        );
        for (Pair<List<BigInteger>, List<BigInteger>> p : take(LIMIT, ps)) {
            Algebraic x = fromContinuedFraction(p.a, p.b);
            x.validate();
            assertEquals(p, x, fromContinuedFraction(p.a, toList(concat(p.b, p.b))));
            if (!p.b.isEmpty()) {
                assertEquals(
                        p,
                        x,
                        fromContinuedFraction(
                                toList(concat(p.a, Collections.singletonList(head(p.b)))), toList(rotateLeft(1, p.b))
                        )
                );
            }
        }

        Iterable<Pair<List<BigInteger>, List<BigInteger>>> psFail = filterInfinite(
                q -> (!q.a.isEmpty() && any(i -> i.signum() != 1, tail(q.a))) || any(i -> i.signum() != 1, q.b),
                P.pairs(P.withScale(4).lists(P.withScale(1).bigIntegers()))
        );
        for (Pair<List<BigInteger>, List<BigInteger>> p : take(LIMIT, psFail)) {
            try {
                fromContinuedFraction(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = filterInfinite(
                p -> any(Objects::isNull, p.a) || any(Objects::isNull, p.b),
                P.pairs(
                        P.withElement(
                                Collections.emptyList(),
                                zipWith(
                                        (i, is) -> toList(cons(i, is)),
                                        P.withScale(1).bigIntegers(),
                                        P.withScale(4)
                                                .lists(P.withScale(4).withNull(P.withScale(2).positiveBigIntegers()))
                                )
                        ),
                        P.withScale(4).lists(P.withScale(2).positiveBigIntegers())
                )
        );
        for (Pair<List<BigInteger>, List<BigInteger>> p : take(LIMIT, psFail)) {
            try {
                fromContinuedFraction(p.a, p.b);
                fail(p);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesConvergents() {
        initialize("convergents()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            List<Rational> convergents = toList(take(TINY_LIMIT, x.convergents()));
            assertFalse(x, convergents.isEmpty());
            assertTrue(x, all(Objects::nonNull, convergents));
            assertEquals(x, head(convergents), Rational.of(x.floor()));
            if (x.isRational()) {
                assertEquals(x, last(x.convergents()), x.rationalValueExact());
            }
            assertTrue(x, zigzagging(convergents));
            if (convergents.size() > 1) {
                assertTrue(x, lt(convergents.get(0), convergents.get(1)));
            }
        }
    }

    private void propertiesDigits() {
        initialize("digits(BigInteger)");
        //noinspection Convert2MethodRef
        Iterable<Pair<Algebraic, BigInteger>> ps = P.pairsSquareRootOrder(
                P.rangeUp(ZERO),
                P.rangeUp(IntegerUtils.TWO)
        );
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, ps)) {
            Pair<List<BigInteger>, Iterable<BigInteger>> digits = p.a.digits(p.b);
            assertTrue(p, digits.a.isEmpty() || !head(digits.a).equals(BigInteger.ZERO));
            assertTrue(p, all(x -> x.signum() != -1 && lt(x, p.b), digits.a));
            assertEquals(p, IntegerUtils.fromBigEndianDigits(p.b, digits.a), p.a.floor());
        }

        ps = filterInfinite(
                q -> q.a.hasTerminatingBaseExpansion(q.b),
                P.pairsSquareRootOrder(P.withScale(2).rangeUp(ZERO), P.withScale(4).rangeUp(IntegerUtils.TWO))
        );
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, ps)) {
            List<BigInteger> afterDecimal = toList(p.a.digits(p.b).b);
            assertTrue(p, afterDecimal.isEmpty() || !last(afterDecimal).equals(BigInteger.ZERO));
        }

        Iterable<Pair<Algebraic, BigInteger>> psFail = P.pairs(P.negativeAlgebraics(), P.rangeUp(IntegerUtils.TWO));
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, psFail)) {
            try {
                p.a.digits(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.algebraics(), P.rangeDown(BigInteger.ONE)))) {
            try {
                p.a.digits(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesCommonLeadingDigits() {
        initialize("commonLeadingDigits(BigInteger, Algebraic, Algebraic)");
        //noinspection Convert2MethodRef
        Iterable<Triple<BigInteger, Algebraic, Algebraic>> ts = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairsSquareRootOrder(
                        filterInfinite(p -> p.a != p.b, P.pairs(P.rangeUp(ZERO))),
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
                )
        );
        for (Triple<BigInteger, Algebraic, Algebraic> t : take(LIMIT, ts)) {
            Pair<List<BigInteger>, Integer> cld = commonLeadingDigits(t.a, t.b, t.c);
            assertNotEquals(t, cld.a, null);
            assertNotEquals(t, cld.b, null);
            assertTrue(t, cld.a.isEmpty() || !head(cld.a).equals(BigInteger.ZERO));
            BigInteger approx = IntegerUtils.fromBigEndianDigits(t.a, cld.a);
            Rational pow = Rational.of(t.a).pow(cld.b);
            assertTrue(t, le(of(pow.multiply(approx)), t.b));
            assertTrue(t, le(of(pow.multiply(approx)), t.c));
            assertTrue(t, gt(of(pow.multiply(approx.add(BigInteger.ONE))), t.b));
            assertTrue(t, gt(of(pow.multiply(approx.add(BigInteger.ONE))), t.c));
        }

        Iterable<Triple<BigInteger, Algebraic, Algebraic>> tsFail = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairsSquareRootOrder(
                        filterInfinite(p -> p.a != p.b, P.pairs(P.rangeUp(ZERO))),
                        P.rangeDown(BigInteger.valueOf(-2))
                )
        );
        for (Triple<BigInteger, Algebraic, Algebraic> t : take(LIMIT, tsFail)) {
            try {
                commonLeadingDigits(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        //noinspection Convert2MethodRef
        tsFail = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairsSquareRootOrder(
                        P.pairs(P.rangeUp(ZERO), P.negativeAlgebraics()),
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
                )
        );
        for (Triple<BigInteger, Algebraic, Algebraic> t : take(LIMIT, tsFail)) {
            try {
                commonLeadingDigits(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        //noinspection Convert2MethodRef
        tsFail = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairsSquareRootOrder(
                        P.pairs(P.negativeAlgebraics(), P.rangeUp(ZERO)),
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
                )
        );
        for (Triple<BigInteger, Algebraic, Algebraic> t : take(LIMIT, tsFail)) {
            try {
                commonLeadingDigits(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        //noinspection Convert2MethodRef
        Iterable<Pair<Algebraic, BigInteger>> psFail = P.pairsSquareRootOrder(
                P.rangeUp(ZERO),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
        );
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, psFail)) {
            try {
                commonLeadingDigits(p.b, p.a, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesToStringBase() {
        initialize("toStringBase(BigInteger, int)");
        //noinspection Convert2MethodRef
        Iterable<Triple<Algebraic, BigInteger, Integer>> ts = P.triples(
                P.withScale(4).algebraics(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                P.withScale(16).integersGeometric()
        );
        for (Triple<Algebraic, BigInteger, Integer> t : take(LIMIT, ts)) {
            String s = t.a.toStringBase(t.b, t.c);
            boolean ellipsis = s.endsWith("...");
            if (ellipsis) s = take(s.length() - 3, s);
            Algebraic error = t.a.subtract(Rational.fromStringBase(s, t.b)).abs();
            assertTrue(t, lt(error, of(t.b).pow(-t.c)));
            if (ellipsis) {
                assertTrue(t, error != ZERO);
            }
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
        ts = P.triples(
                P.withScale(4).algebraics(),
                P.range(IntegerUtils.TWO, ASCII_ALPHANUMERIC_COUNT),
                P.withScale(16).integersGeometric()
        );
        for (Triple<Algebraic, BigInteger, Integer> t : take(LIMIT, ts)) {
            String s = t.a.toStringBase(t.b, t.c);
            assertTrue(t, all(c -> elem(c, smallBaseChars), s));
        }

        String largeBaseChars = charsToString(
                concat(fromString("-.()"), ExhaustiveProvider.INSTANCE.rangeIncreasing('0', '9'))
        );
        //noinspection Convert2MethodRef
        ts = P.triples(
                P.withScale(4).algebraics(),
                map(
                        i -> BigInteger.valueOf(i),
                        P.withScale(64).rangeUpGeometric(ASCII_ALPHANUMERIC_COUNT.intValueExact() + 1)
                ),
                P.withScale(16).integersGeometric()
        );
        for (Triple<Algebraic, BigInteger, Integer> t : take(LIMIT, ts)) {
            String s = t.a.toStringBase(t.b, t.c);
            assertTrue(t, all(c -> elem(c, largeBaseChars), s));
        }

        Iterable<Triple<Algebraic, BigInteger, Integer>> tsFail = P.triples(
                P.withScale(4).algebraics(),
                P.rangeDown(BigInteger.ONE),
                P.withScale(16).integers()
        );
        for (Triple<Algebraic, BigInteger, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.toStringBase(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::algebraics);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::algebraics);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(Algebraic)");
        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::algebraics);
        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.subsetPairs(P.algebraics()))) {
            assertEquals(p, p.a.compareTo(p.b), p.a.realValue().compareToUnsafe(p.b.realValue()));
        }
    }

    private void propertiesReadStrict() {
        initialize("readStrict(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                ALGEBRAIC_CHARS,
                P.algebraics(),
                Algebraic::readStrict,
                Algebraic::validate,
                false,
                true
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, ALGEBRAIC_CHARS, P.algebraics(), Algebraic::readStrict);
    }
}
