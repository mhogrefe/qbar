package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead();
        propertiesFindIn();
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
            //todo assertTrue(p, lt(p.a.subtract(of(rounded)).abs(), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).bigIntegerValue(RoundingMode.UNNECESSARY), i);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            assertEquals(x, x.bigIntegerValue(RoundingMode.FLOOR), x.floor());
            assertEquals(x, x.bigIntegerValue(RoundingMode.CEILING), x.ceiling());
            //todo
//            assertTrue(x, le(of(x.bigIntegerValue(RoundingMode.DOWN)).abs(), x.abs()));
//            assertTrue(x, ge(of(x.bigIntegerValue(RoundingMode.UP)).abs(), x.abs()));
//            assertTrue(x, le(x.subtract(of(x.bigIntegerValue(RoundingMode.HALF_DOWN))).abs(), ONE_HALF));
//            assertTrue(x, le(x.subtract(of(x.bigIntegerValue(RoundingMode.HALF_UP))).abs(), ONE_HALF));
//            assertTrue(x, le(x.subtract(of(x.bigIntegerValue(RoundingMode.HALF_EVEN))).abs(), ONE_HALF));
        }

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
            //todo assertTrue(x, le(x.subtract(of(x.bigIntegerValue())).abs(), ONE_HALF));
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
            //todo assertTrue(x, le(x.subtract(of(floor)), ONE));
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
            //todo assertTrue(x, le(of(ceiling).subtract(x), ONE));
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

    private void propertiesRead() {
        initialize("read(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                ALGEBRAIC_CHARS,
                P.algebraics(),
                Algebraic::read,
                Algebraic::validate,
                false
        );
    }

    private void propertiesFindIn() {
        initialize("findIn(String)");
        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.algebraics(),
                Algebraic::read,
                Algebraic::findIn,
                Algebraic::validate
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, ALGEBRAIC_CHARS, P.algebraics(), Algebraic::read);
    }
}
