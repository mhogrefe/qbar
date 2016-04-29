package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static mho.qbar.objects.Algebraic.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.numberUtils.FloatingPointUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class AlgebraicProperties extends QBarTestProperties {
    private static final @NotNull String ALGEBRAIC_CHARS = " ()*+-/0123456789^foqrstx";

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
            } catch (IllegalArgumentException ignored) {}
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
            } catch (IllegalArgumentException ignored) {}
        }

        Iterable<Pair<Polynomial, Integer>> psFail = P.pairs(
                filterInfinite(p -> p.rootCount() == 0, P.withScale(4).squareFreePolynomials()),
                P.naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                of(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
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
            //todo assertEquals(bf, of(bf.getMantissa()).multiply(ONE.shiftLeft(bf.getExponent())), x);
            assertTrue(bf, IntegerUtils.isPowerOfTwo(x.rationalValueExact().getDenominator()));
            inverse(Algebraic::of, Algebraic::binaryFractionValueExact, bf);
        }
    }

    private void propertiesOf_float() {
        initialize("of(float)");
        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            Algebraic x = of(f).get();
            x.validate();
            //todo assertTrue(f, x.hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            Algebraic x = of(f).get();
            aeqf(f, f, x.floatValue());
            //todo assertTrue(f, eq(new BigDecimal(Float.toString(f)), x.bigDecimalValueExact()));
        }
    }

    private void propertiesOf_double() {
        initialize("of(double)");
        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            Algebraic x = of(d).get();
            x.validate();
            //todo assertTrue(d, x.hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            Algebraic x = of(d).get();
            aeqd(d, d, x.doubleValue());
            //todo assertTrue(d, eq(new BigDecimal(Double.toString(d)), x.bigDecimalValueExact()));
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
            //todo assertTrue(bd, eq(bd, x.bigDecimalValueExact()));
            //todo assertTrue(bd, x.hasTerminatingBaseExpansion(BigInteger.TEN));
        }

//        for (BigDecimal bd : take(LIMIT, P.canonicalBigDecimals())) {
//            inverse(Algebraic::of, Algebraic::bigDecimalValueExact, bd);
//        }
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

        //todo
//        for (Algebraic x : take(LIMIT, filterInfinite(s -> lt(s.abs().fractionalPart(), ONE_HALF), P.algebraics()))) {
//            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_DOWN), r.bigIntegerValue(RoundingMode.DOWN));
//            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_UP), r.bigIntegerValue(RoundingMode.DOWN));
//            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_EVEN), r.bigIntegerValue(RoundingMode.DOWN));
//        }
//
//        for (Rational r : take(LIMIT, filterInfinite(s -> gt(s.abs().fractionalPart(), ONE_HALF), P.rationals()))) {
//            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_DOWN), r.bigIntegerValue(RoundingMode.UP));
//            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_UP), r.bigIntegerValue(RoundingMode.UP));
//            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_EVEN), r.bigIntegerValue(RoundingMode.UP));
//        }

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
        }

//        for (Rational r : take(LIMIT, filterInfinite(s -> lt(s.abs().fractionalPart(), ONE_HALF), P.rationals()))) {
//            assertEquals(r, r.bigIntegerValue(), r.bigIntegerValue(RoundingMode.DOWN));
//        }
//
//        for (Rational r : take(LIMIT, filterInfinite(s -> gt(s.abs().fractionalPart(), ONE_HALF), P.rationals()))) {
//            assertEquals(r, r.bigIntegerValue(), r.bigIntegerValue(RoundingMode.UP));
//        }

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
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).ceiling(), i);
        }
    }

    private void propertiesBigIntegerValueExact() {
        initialize("bigIntegerValueExact()");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Algebraic x = of(i);
            assertEquals(i, x.bigIntegerValueExact(), i);
            inverse(Algebraic::bigIntegerValueExact, Algebraic::of, x);
            homomorphic(
                    Algebraic::negate,
                    BigInteger::negate,
                    Algebraic::bigIntegerValueExact,
                    Algebraic::bigIntegerValueExact,
                    x
            );
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
            assertEquals(b, x.byteValueExact(), b);
            inverse(Algebraic::byteValueExact, c -> of((int) c), x);
        }

        for (byte b : take(LIMIT, filter(c -> c != Byte.MIN_VALUE, P.bytes()))) {
            Algebraic x = of(b);
            homomorphic(Algebraic::negate, c -> (byte) -c, Algebraic::byteValueExact, Algebraic::byteValueExact, x);
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.rationals()))) {
            try {
                r.byteValueExact();
                fail(r);
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
            assertEquals(s, x.shortValueExact(), s);
            inverse(Algebraic::shortValueExact, t -> of((int) t), x);
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
            assertEquals(i, x.intValueExact(), i);
            inverse(Algebraic::intValueExact, Algebraic::of, x);
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
            assertEquals(l, x.longValueExact(), l);
            inverse(Algebraic::longValueExact, Algebraic::of, x);
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
            //todo assertEquals(r, r.isPowerOfTwo(), ONE.shiftLeft(r.binaryExponent()).equals(r));
        }

        for (Algebraic x : take(LIMIT, P.withElement(ZERO, P.negativeAlgebraics()))) {
            try {
                x.isIntegerPowerOfTwo();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesRoundUpToIntegerPowerOfTwo() {
        initialize("roundUpToIntegerPowerOfTwo()");
        for (Algebraic x : take(LIMIT, P.positiveAlgebraics())) {
            Rational powerOfTwo = x.roundUpToIntegerPowerOfTwo();
            assertTrue(x, powerOfTwo.isPowerOfTwo());
            assertTrue(x, le(x, of(powerOfTwo)));
            assertTrue(x, lt(of(powerOfTwo.shiftRight(1)), x));
        }

        for (Algebraic x : take(LIMIT, P.withElement(ZERO, P.negativeAlgebraics()))) {
            try {
                x.roundUpToIntegerPowerOfTwo();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesIsBinaryFraction() {
        initialize("isBinaryFraction()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            x.isBinaryFraction();
            homomorphic(
                    Algebraic::negate,
                    Function.identity(),
                    Algebraic::isBinaryFraction,
                    Algebraic::isBinaryFraction,
                    x
            );
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
            x.isRational();
            homomorphic(
                    Algebraic::negate,
                    Function.identity(),
                    Algebraic::isRational,
                    Algebraic::isRational,
                    x
            );
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
            //todo
//            Rational power = ONE.shiftLeft(exponent);
//            assertTrue(r, le(power, r));
//            assertTrue(r, le(r, power.shiftLeft(1)));
        }

        for (Algebraic x : take(LIMIT, P.withElement(ZERO, P.negativeAlgebraics()))) {
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
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            assertTrue(f, ofExact(f).get().isEqualToFloat());
        }
    }

    private void compareImplementationsIsEqualToFloat() {
        Map<String, Function<Algebraic, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", AlgebraicProperties::isEqualToFloat_simplest);
        functions.put("standard", Algebraic::isEqualToFloat);
        compareImplementations("isEqualToFloat()", take(LIMIT, P.algebraics()), functions);
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
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            assertTrue(d, ofExact(d).get().isEqualToDouble());
        }
    }

    private void compareImplementationsIsEqualToDouble() {
        Map<String, Function<Algebraic, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", AlgebraicProperties::isEqualToDouble_simplest);
        functions.put("standard", Algebraic::isEqualToDouble);
        compareImplementations("isEqualToDouble()", take(LIMIT, P.algebraics()), functions);
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
                P.withScale(2).algebraicsIn(Interval.greaterThanOrEqualTo(Rational.LARGEST_FLOAT)
        ));
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
            //todo length
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
        }

        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.algebraics(), P.nonzeroBigIntegers()))) {
            assertEquals(p, p.a.multiply(p.b).divide(p.b), p.a);
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
        }

        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.algebraics(), P.nonzeroBigIntegers()))) {
            assertEquals(p, p.a.multiply(p.b).divide(p.b), p.a);
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
        }

        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.algebraics(), P.nonzeroBigIntegers()))) {
            assertEquals(p, p.a.multiply(p.b).divide(p.b), p.a);
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
