package mho.qbar;

import mho.haskellesque.numbers.Numbers;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static mho.haskellesque.ordering.Ordering.*;
import static org.junit.Assert.*;

public class RationalTest {
    @Test
    public void testConstants() {
        aeq(Rational.ZERO, "0");
        aeq(Rational.ONE, "1");
        aeq(Rational.SMALLEST_FLOAT, "1/713623846352979940529142984724747568191373312");
        aeq(Rational.LARGEST_SUBNORMAL_FLOAT, "8388607/713623846352979940529142984724747568191373312");
        aeq(Rational.SMALLEST_NORMAL_FLOAT, "1/85070591730234615865843651857942052864");
        aeq(Rational.LARGEST_FLOAT, "340282346638528859811704183484516925440");
        aeq(Rational.SMALLEST_DOUBLE, "1/202402253307310618352495346718917307049556649764142118356901358027430339567995346891960383701437124495187077864316811911389808737385793476867013399940738509921517424276566361364466907742093216341239767678472745068562007483424692698618103355649159556340810056512358769552333414615230502532186327508646006263307707741093494784");
        aeq(Rational.LARGEST_SUBNORMAL_DOUBLE, "4503599627370495/202402253307310618352495346718917307049556649764142118356901358027430339567995346891960383701437124495187077864316811911389808737385793476867013399940738509921517424276566361364466907742093216341239767678472745068562007483424692698618103355649159556340810056512358769552333414615230502532186327508646006263307707741093494784");
        aeq(Rational.SMALLEST_NORMAL_DOUBLE, "1/44942328371557897693232629769725618340449424473557664318357520289433168951375240783177119330601884005280028469967848339414697442203604155623211857659868531094441973356216371319075554900311523529863270738021251442209537670585615720368478277635206809290837627671146574559986811484619929076208839082406056034304");
        aeq(Rational.LARGEST_DOUBLE, "179769313486231570814527423731704356798070567525844996598917476803157260780028538760589558632766878171540458953514382464234321326889464182768467546703537516986049910576551282076245490090389328944075868508455133942304583236903222948165808559332123348274797826204144723168738177180919299881250404026184124858368");
    }

    @Test
    public void testGetNumerator() {
        aeq(Rational.ZERO.getNumerator(), "0");
        aeq(Rational.ONE.getNumerator(), "1");
        aeq(Rational.read("2").get().getNumerator(), "2");
        aeq(Rational.read("-2").get().getNumerator(), "-2");
        aeq(Rational.read("5/3").get().getNumerator(), "5");
        aeq(Rational.read("-5/3").get().getNumerator(), "-5");
    }

    @Test
    public void testGetDenominator() {
        aeq(Rational.ZERO.getDenominator(), "1");
        aeq(Rational.ONE.getDenominator(), "1");
        aeq(Rational.read("2").get().getDenominator(), "1");
        aeq(Rational.read("-2").get().getDenominator(), "1");
        aeq(Rational.read("5/3").get().getDenominator(), "3");
        aeq(Rational.read("-5/3").get().getDenominator(), "3");
    }

    @Test
    public void testOfBigIntegerBigInteger() {
        aeq(Rational.of(BigInteger.valueOf(2), BigInteger.valueOf(3)), "2/3");
        aeq(Rational.of(BigInteger.valueOf(4), BigInteger.valueOf(6)), "2/3");
        aeq(Rational.of(BigInteger.valueOf(-4), BigInteger.valueOf(-6)), "2/3");
        aeq(Rational.of(BigInteger.valueOf(4), BigInteger.valueOf(-6)), "-2/3");
        assertTrue(Rational.of(BigInteger.valueOf(4), BigInteger.valueOf(4)) == Rational.ONE);
        aeq(Rational.of(BigInteger.valueOf(4), BigInteger.valueOf(1)), "4");
        assertTrue(Rational.of(BigInteger.valueOf(0), BigInteger.valueOf(1)) == Rational.ZERO);
        try {
            Rational.of(BigInteger.valueOf(1), BigInteger.ZERO);
            fail();
        } catch (ArithmeticException e) {}
    }

    @Test
    public void testOfIntInt() {
        aeq(Rational.of(2, 3), "2/3");
        aeq(Rational.of(4, 6), "2/3");
        aeq(Rational.of(-4, -6), "2/3");
        aeq(Rational.of(4, -6), "-2/3");
        assertTrue(Rational.of(4, 4) == Rational.ONE);
        aeq(Rational.of(4, 1), "4");
        assertTrue(Rational.of(0, 1) == Rational.ZERO);
        try {
            Rational.of(1, 0);
            fail();
        } catch (ArithmeticException e) {}
    }

    @Test
    public void testOfBigInteger() {
        aeq(Rational.of(BigInteger.valueOf(23)), "23");
        aeq(Rational.of(BigInteger.valueOf(-23)), "-23");
        assertTrue(Rational.of(BigInteger.valueOf(0)) == Rational.ZERO);
        assertTrue(Rational.of(BigInteger.valueOf(1)) == Rational.ONE);
    }

    @Test
    public void testOfInt() {
        aeq(Rational.of(23), "23");
        aeq(Rational.of(-23), "-23");
        assertTrue(Rational.of(0) == Rational.ZERO);
        assertTrue(Rational.of(1) == Rational.ONE);
    }

    @Test
    public void testOfFloat() {
        assertTrue(Rational.of(0.0f) == Rational.ZERO);
        assertTrue(Rational.of(1.0f) == Rational.ONE);
        aeq(Rational.of(13.0f), "13");
        aeq(Rational.of(-5.0f), "-5");
        aeq(Rational.of(1.5f), "3/2");
        aeq(Rational.of(0.15625f), "5/32");
        aeq(Rational.of(0.1f), "13421773/134217728");
        aeq(Rational.of(1.0f / 3.0f), "11184811/33554432");
        aeq(Rational.of(1e10f), "10000000000");
        aeq(Rational.of(1e30f), "1000000015047466219876688855040");
        aeq(Rational.of((float) Math.PI), "13176795/4194304");
        aeq(Rational.of((float) Math.E), "2850325/1048576");
        aeq(Rational.of((float) Math.sqrt(2)), "11863283/8388608");
        aeq(Rational.of(Float.MIN_VALUE), Rational.SMALLEST_FLOAT);
        aeq(Rational.of(-Float.MIN_VALUE), Rational.SMALLEST_FLOAT.negate());
        aeq(Rational.of(Float.MIN_NORMAL), Rational.SMALLEST_NORMAL_FLOAT);
        aeq(Rational.of(-Float.MIN_NORMAL), Rational.SMALLEST_NORMAL_FLOAT.negate());
        aeq(Rational.of(Float.MAX_VALUE), Rational.LARGEST_FLOAT);
        aeq(Rational.of(-Float.MAX_VALUE), Rational.LARGEST_FLOAT.negate());
        assertNull(Rational.of(Float.POSITIVE_INFINITY));
        assertNull(Rational.of(Float.NEGATIVE_INFINITY));
        assertNull(Rational.of(Float.NaN));
    }

    @Test
    public void testOfDouble() {
        assertTrue(Rational.of(0.0) == Rational.ZERO);
        assertTrue(Rational.of(1.0) == Rational.ONE);
        aeq(Rational.of(13.0), "13");
        aeq(Rational.of(-5.0), "-5");
        aeq(Rational.of(1.5), "3/2");
        aeq(Rational.of(0.15625), "5/32");
        aeq(Rational.of(0.1), "3602879701896397/36028797018963968");
        aeq(Rational.of(1.0/3.0), "6004799503160661/18014398509481984");
        aeq(Rational.of(1e10), "10000000000");
        aeq(Rational.of(1e30), "1000000000000000019884624838656");
        aeq(Rational.of(Math.PI), "884279719003555/281474976710656");
        aeq(Rational.of(Math.E), "6121026514868073/2251799813685248");
        aeq(Rational.of(Math.sqrt(2)), "6369051672525773/4503599627370496");
        aeq(Rational.of(Double.MIN_VALUE), Rational.SMALLEST_DOUBLE);
        aeq(Rational.of(-Double.MIN_VALUE), Rational.SMALLEST_DOUBLE.negate());
        aeq(Rational.of(Double.MIN_NORMAL), Rational.SMALLEST_NORMAL_DOUBLE);
        aeq(Rational.of(-Double.MIN_NORMAL), Rational.SMALLEST_NORMAL_DOUBLE.negate());
        aeq(Rational.of(Double.MAX_VALUE), Rational.LARGEST_DOUBLE);
        aeq(Rational.of(-Double.MAX_VALUE), Rational.LARGEST_DOUBLE.negate());
        assertNull(Rational.of(Double.POSITIVE_INFINITY));
        assertNull(Rational.of(Double.NEGATIVE_INFINITY));
        assertNull(Rational.of(Double.NaN));
    }

    @Test
    public void testOfBigDecimal() {
        assertTrue(Rational.of(BigDecimal.ZERO) == Rational.ZERO);
        assertTrue(Rational.of(BigDecimal.ONE) == Rational.ONE);
        aeq(Rational.of(new BigDecimal("3")), "3");
        aeq(Rational.of(new BigDecimal("-5")), "-5");
        aeq(Rational.of(new BigDecimal("0.1")), "1/10");
        aeq(Rational.of(new BigDecimal("3.14159")), "314159/100000");
        aeq(Rational.of(new BigDecimal("-2.718281828459045")), "-543656365691809/200000000000000");
        aeq(Rational.of(new BigDecimal("0.00000000000001")), "1/100000000000000");
        aeq(Rational.of(new BigDecimal("1000000000000000")), "1000000000000000");
    }

    @Test
    public void testNegate() {
        aeq(Rational.read("2/3").get().negate(), "-2/3");
        aeq(Rational.read("-2/3").get().negate(), "2/3");
        aeq(Rational.read("4").get().negate(), "-4");
        aeq(Rational.read("-4").get().negate(), "4");
        assertTrue(Rational.ZERO.negate() == Rational.ZERO);
        aeq(Rational.ONE.negate(), "-1");
        assertTrue(Rational.read("-1").get().negate() == Rational.ONE);
    }

    @Test
    public void testInvert() {
        aeq(Rational.read("2/3").get().invert(), "3/2");
        aeq(Rational.read("-2/3").get().invert(), "-3/2");
        aeq(Rational.read("4").get().invert(), "1/4");
        aeq(Rational.read("1/4").get().invert(), "4");
        assertTrue(Rational.ONE.invert() == Rational.ONE);
        try {
            Rational.ZERO.invert();
            fail();
        } catch (ArithmeticException e) {}
    }

    @Test
    public void testAbs() {
        aeq(Rational.read("2/3").get().abs(), "2/3");
        aeq(Rational.read("-2/3").get().abs(), "2/3");
        aeq(Rational.read("4").get().abs(), "4");
        aeq(Rational.read("-4").get().abs(), "4");
        aeq(Rational.ZERO.abs(), "0");
        aeq(Rational.ONE.abs(), "1");
    }

    @Test
    public void testSignum() {
        aeq(Rational.read("2/3").get().signum(), 1);
        aeq(Rational.read("-2/3").get().signum(), -1);
        aeq(Rational.read("4").get().signum(), 1);
        aeq(Rational.read("-4").get().signum(), -1);
        aeq(Rational.ZERO.signum(), 0);
        aeq(Rational.ONE.signum(), 1);
    }

    @Test
    public void testAdd() {
        aeq(Rational.add(Rational.read("1/2").get(), Rational.read("1/3").get()), "5/6");
        aeq(Rational.add(Rational.read("1/2").get(), Rational.read("-1/3").get()), "1/6");
        aeq(Rational.add(Rational.read("-1/2").get(), Rational.read("1/3").get()), "-1/6");
        aeq(Rational.add(Rational.read("-1/2").get(), Rational.read("-1/3").get()), "-5/6");
        aeq(Rational.add(Rational.read("2").get(), Rational.read("1/5").get()), "11/5");
        aeq(Rational.add(Rational.read("2").get(), Rational.read("-1/5").get()), "9/5");
        aeq(Rational.add(Rational.read("-2").get(), Rational.read("1/5").get()), "-9/5");
        aeq(Rational.add(Rational.read("-2").get(), Rational.read("-1/5").get()), "-11/5");
        aeq(Rational.add(Rational.read("2").get(), Rational.read("5").get()), "7");
        aeq(Rational.add(Rational.read("2").get(), Rational.read("-5").get()), "-3");
        aeq(Rational.add(Rational.read("-2").get(), Rational.read("5").get()), "3");
        aeq(Rational.add(Rational.read("-2").get(), Rational.read("-5").get()), "-7");
        assertTrue(Rational.add(Rational.read("6/7").get(), Rational.read("1/7").get()) == Rational.ONE);
        assertTrue(Rational.add(Rational.read("6/7").get(), Rational.read("-6/7").get()) == Rational.ZERO);
        aeq(Rational.add(Rational.read("1/2").get(), Rational.ZERO), "1/2");
        aeq(Rational.add(Rational.read("-1/2").get(), Rational.ZERO), "-1/2");
        aeq(Rational.add(Rational.read("1/2").get(), Rational.ONE), "3/2");
        aeq(Rational.add(Rational.read("-1/2").get(), Rational.ONE), "1/2");
        assertTrue(Rational.add(Rational.ZERO, Rational.ZERO) == Rational.ZERO);
        assertTrue(Rational.add(Rational.ZERO, Rational.ONE) == Rational.ONE);
        assertTrue(Rational.add(Rational.ONE, Rational.ZERO) == Rational.ONE);
        aeq(Rational.add(Rational.ONE, Rational.ONE), "2");
    }

    @Test
    public void testSubtract() {
        aeq(Rational.subtract(Rational.read("1/2").get(), Rational.read("1/3").get()), "1/6");
        aeq(Rational.subtract(Rational.read("1/2").get(), Rational.read("-1/3").get()), "5/6");
        aeq(Rational.subtract(Rational.read("-1/2").get(), Rational.read("1/3").get()), "-5/6");
        aeq(Rational.subtract(Rational.read("-1/2").get(), Rational.read("-1/3").get()), "-1/6");
        aeq(Rational.subtract(Rational.read("2").get(), Rational.read("1/5").get()), "9/5");
        aeq(Rational.subtract(Rational.read("2").get(), Rational.read("-1/5").get()), "11/5");
        aeq(Rational.subtract(Rational.read("-2").get(), Rational.read("1/5").get()), "-11/5");
        aeq(Rational.subtract(Rational.read("-2").get(), Rational.read("-1/5").get()), "-9/5");
        aeq(Rational.subtract(Rational.read("2").get(), Rational.read("5").get()), "-3");
        aeq(Rational.subtract(Rational.read("2").get(), Rational.read("-5").get()), "7");
        aeq(Rational.subtract(Rational.read("-2").get(), Rational.read("5").get()), "-7");
        aeq(Rational.subtract(Rational.read("-2").get(), Rational.read("-5").get()), "3");
        assertTrue(Rational.subtract(Rational.read("8/7").get(), Rational.read("1/7").get()) == Rational.ONE);
        assertTrue(Rational.subtract(Rational.read("6/7").get(), Rational.read("6/7").get()) == Rational.ZERO);
        aeq(Rational.subtract(Rational.read("1/2").get(), Rational.ZERO), "1/2");
        aeq(Rational.subtract(Rational.read("-1/2").get(), Rational.ZERO), "-1/2");
        aeq(Rational.subtract(Rational.read("1/2").get(), Rational.ONE), "-1/2");
        aeq(Rational.subtract(Rational.read("-1/2").get(), Rational.ONE), "-3/2");
        assertTrue(Rational.subtract(Rational.ZERO, Rational.ZERO) == Rational.ZERO);
        aeq(Rational.subtract(Rational.ZERO, Rational.ONE), "-1");
        assertTrue(Rational.subtract(Rational.ONE, Rational.ZERO) == Rational.ONE);
        assertTrue(Rational.subtract(Rational.ONE, Rational.ONE) == Rational.ZERO);
    }

    @Test
    public void testMultiplyRationalRational() {
        aeq(Rational.multiply(Rational.read("2/3").get(), Rational.read("6/7").get()), "4/7");
        aeq(Rational.multiply(Rational.read("2/3").get(), Rational.read("-6/7").get()), "-4/7");
        aeq(Rational.multiply(Rational.read("-2/3").get(), Rational.read("6/7").get()), "-4/7");
        aeq(Rational.multiply(Rational.read("-2/3").get(), Rational.read("-6/7").get()), "4/7");
        aeq(Rational.multiply(Rational.read("2/3").get(), Rational.read("4").get()), "8/3");
        aeq(Rational.multiply(Rational.read("2/3").get(), Rational.read("-4").get()), "-8/3");
        aeq(Rational.multiply(Rational.read("-2/3").get(), Rational.read("4").get()), "-8/3");
        aeq(Rational.multiply(Rational.read("-2/3").get(), Rational.read("-4").get()), "8/3");
        aeq(Rational.multiply(Rational.read("3").get(), Rational.read("5").get()), "15");
        aeq(Rational.multiply(Rational.read("3").get(), Rational.read("-5").get()), "-15");
        aeq(Rational.multiply(Rational.read("-3").get(), Rational.read("5").get()), "-15");
        aeq(Rational.multiply(Rational.read("-3").get(), Rational.read("-5").get()), "15");
        assertTrue(Rational.multiply(Rational.read("1/4").get(), Rational.read("4").get()) == Rational.ONE);
        assertTrue(Rational.multiply(Rational.read("-1/4").get(), Rational.read("-4").get()) == Rational.ONE);
        assertTrue(Rational.multiply(Rational.read("2/3").get(), Rational.ZERO) == Rational.ZERO);
        assertTrue(Rational.multiply(Rational.read("-2/3").get(), Rational.ZERO) == Rational.ZERO);
        aeq(Rational.multiply(Rational.read("2/3").get(), Rational.ONE), "2/3");
        aeq(Rational.multiply(Rational.read("-2/3").get(), Rational.ONE), "-2/3");
        assertTrue(Rational.multiply(Rational.ZERO, Rational.ZERO) == Rational.ZERO);
        assertTrue(Rational.multiply(Rational.ZERO, Rational.ONE) == Rational.ZERO);
        assertTrue(Rational.multiply(Rational.ONE, Rational.ZERO) == Rational.ZERO);
        assertTrue(Rational.multiply(Rational.ONE, Rational.ONE) == Rational.ONE);
    }

    @Test
    public void multiplyBigInteger() {
        aeq(Rational.read("2/3").get().multiply(BigInteger.valueOf(4)), "8/3");
        aeq(Rational.read("2/3").get().multiply(BigInteger.valueOf(-4)), "-8/3");
        aeq(Rational.read("-2/3").get().multiply(BigInteger.valueOf(4)), "-8/3");
        aeq(Rational.read("-2/3").get().multiply(BigInteger.valueOf(-4)), "8/3");
        aeq(Rational.read("2/3").get().multiply(BigInteger.valueOf(3)), "2");
        aeq(Rational.read("2/3").get().multiply(BigInteger.valueOf(-3)), "-2");
        aeq(Rational.read("-2/3").get().multiply(BigInteger.valueOf(3)), "-2");
        aeq(Rational.read("-2/3").get().multiply(BigInteger.valueOf(-3)), "2");
        assertTrue(Rational.read("2/3").get().multiply(BigInteger.ZERO) == Rational.ZERO);
        aeq(Rational.read("2/3").get().multiply(BigInteger.ONE), "2/3");
    }

    @Test
    public void multiplyInt() {
        aeq(Rational.read("2/3").get().multiply(4), "8/3");
        aeq(Rational.read("2/3").get().multiply(-4), "-8/3");
        aeq(Rational.read("-2/3").get().multiply(4), "-8/3");
        aeq(Rational.read("-2/3").get().multiply(-4), "8/3");
        aeq(Rational.read("2/3").get().multiply(3), "2");
        aeq(Rational.read("2/3").get().multiply(-3), "-2");
        aeq(Rational.read("-2/3").get().multiply(3), "-2");
        aeq(Rational.read("-2/3").get().multiply(-3), "2");
        assertTrue(Rational.read("2/3").get().multiply(0) == Rational.ZERO);
        assertTrue(Rational.read("1/3").get().multiply(3) == Rational.ONE);
        assertTrue(Rational.read("-1/3").get().multiply(-3) == Rational.ONE);
        aeq(Rational.read("2/3").get().multiply(1), "2/3");
    }

    @Test
    public void testDivideRationalRational() {
        aeq(Rational.divide(Rational.read("2/3").get(), Rational.read("6/7").get()), "7/9");
        aeq(Rational.divide(Rational.read("2/3").get(), Rational.read("-6/7").get()), "-7/9");
        aeq(Rational.divide(Rational.read("-2/3").get(), Rational.read("6/7").get()), "-7/9");
        aeq(Rational.divide(Rational.read("-2/3").get(), Rational.read("-6/7").get()), "7/9");
        aeq(Rational.divide(Rational.read("2/3").get(), Rational.read("4").get()), "1/6");
        aeq(Rational.divide(Rational.read("2/3").get(), Rational.read("-4").get()), "-1/6");
        aeq(Rational.divide(Rational.read("-2/3").get(), Rational.read("4").get()), "-1/6");
        aeq(Rational.divide(Rational.read("-2/3").get(), Rational.read("-4").get()), "1/6");
        aeq(Rational.divide(Rational.read("3").get(), Rational.read("5").get()), "3/5");
        aeq(Rational.divide(Rational.read("3").get(), Rational.read("-5").get()), "-3/5");
        aeq(Rational.divide(Rational.read("-3").get(), Rational.read("5").get()), "-3/5");
        aeq(Rational.divide(Rational.read("-3").get(), Rational.read("-5").get()), "3/5");
        aeq(Rational.divide(Rational.read("1/4").get(), Rational.read("4").get()), "1/16");
        assertTrue(Rational.divide(Rational.read("2/3").get(), Rational.read("2/3").get()) == Rational.ONE);
        assertTrue(Rational.divide(Rational.read("-2/3").get(), Rational.read("-2/3").get()) == Rational.ONE);
        aeq(Rational.divide(Rational.read("2/3").get(), Rational.ONE), "2/3");
        aeq(Rational.divide(Rational.read("-2/3").get(), Rational.ONE), "-2/3");
        assertTrue(Rational.divide(Rational.ZERO, Rational.ONE) == Rational.ZERO);
        assertTrue(Rational.divide(Rational.ONE, Rational.ONE) == Rational.ONE);
        try {
            Rational.divide(Rational.read("2/3").get(), Rational.ZERO);
            fail();
        } catch (ArithmeticException e) {}
        try {
            Rational.divide(Rational.read("3").get(), Rational.ZERO);
            fail();
        } catch (ArithmeticException e) {}
        try {
            Rational.divide(Rational.ONE, Rational.ZERO);
            fail();
        } catch (ArithmeticException e) {}
        try {
            Rational.divide(Rational.ZERO, Rational.ZERO);
            fail();
        } catch (ArithmeticException e) {}
    }

    @Test
    public void divideBigInteger() {
        aeq(Rational.read("2/3").get().divide(BigInteger.valueOf(4)), "1/6");
        aeq(Rational.read("2/3").get().divide(BigInteger.valueOf(-4)), "-1/6");
        aeq(Rational.read("-2/3").get().divide(BigInteger.valueOf(4)), "-1/6");
        aeq(Rational.read("-2/3").get().divide(BigInteger.valueOf(-4)), "1/6");
        aeq(Rational.read("2/3").get().divide(BigInteger.valueOf(3)), "2/9");
        aeq(Rational.read("2/3").get().divide(BigInteger.valueOf(-3)), "-2/9");
        aeq(Rational.read("-2/3").get().divide(BigInteger.valueOf(3)), "-2/9");
        aeq(Rational.read("-2/3").get().divide(BigInteger.valueOf(-3)), "2/9");
        aeq(Rational.read("2/3").get().divide(BigInteger.ONE), "2/3");
        assertTrue(Rational.ZERO.divide(BigInteger.valueOf(3)) == Rational.ZERO);
        assertTrue(Rational.read("5").get().divide(BigInteger.valueOf(5)) == Rational.ONE);
        assertTrue(Rational.read("-5").get().divide(BigInteger.valueOf(-5)) == Rational.ONE);
        try {
            Rational.read("2/3").get().divide(BigInteger.ZERO);
            fail();
        } catch (ArithmeticException e) {}
    }

    @Test
    public void divideInt() {
        aeq(Rational.read("2/3").get().divide(4), "1/6");
        aeq(Rational.read("2/3").get().divide(-4), "-1/6");
        aeq(Rational.read("-2/3").get().divide(4), "-1/6");
        aeq(Rational.read("-2/3").get().divide(-4), "1/6");
        aeq(Rational.read("2/3").get().divide(3), "2/9");
        aeq(Rational.read("2/3").get().divide(-3), "-2/9");
        aeq(Rational.read("-2/3").get().divide(3), "-2/9");
        aeq(Rational.read("-2/3").get().divide(-3), "2/9");
        aeq(Rational.read("2/3").get().divide(1), "2/3");
        assertTrue(Rational.ZERO.divide(3) == Rational.ZERO);
        assertTrue(Rational.read("5").get().divide(5) == Rational.ONE);
        assertTrue(Rational.read("-5").get().divide(-5) == Rational.ONE);
        try {
            Rational.read("2/3").get().divide(0);
            fail();
        } catch (ArithmeticException e) {}
    }

    @Test
    public void testPow() {
        assertTrue(Rational.read("2/3").get().pow(0) == Rational.ONE);
        aeq(Rational.read("2/3").get().pow(1), "2/3");
        aeq(Rational.read("2/3").get().pow(2), "4/9");
        aeq(Rational.read("2/3").get().pow(3), "8/27");
        aeq(Rational.read("2/3").get().pow(-1), "3/2");
        aeq(Rational.read("2/3").get().pow(-2), "9/4");
        aeq(Rational.read("2/3").get().pow(-3), "27/8");
        aeq(Rational.read("-2/3").get().pow(0), "1");
        aeq(Rational.read("-2/3").get().pow(1), "-2/3");
        aeq(Rational.read("-2/3").get().pow(2), "4/9");
        aeq(Rational.read("-2/3").get().pow(3), "-8/27");
        aeq(Rational.read("-2/3").get().pow(-1), "-3/2");
        aeq(Rational.read("-2/3").get().pow(-2), "9/4");
        aeq(Rational.read("-2/3").get().pow(-3), "-27/8");
        assertTrue(Rational.read("2").get().pow(0) == Rational.ONE);
        aeq(Rational.read("2").get().pow(1), "2");
        aeq(Rational.read("2").get().pow(2), "4");
        aeq(Rational.read("2").get().pow(3), "8");
        aeq(Rational.read("2").get().pow(-1), "1/2");
        aeq(Rational.read("2").get().pow(-2), "1/4");
        aeq(Rational.read("2").get().pow(-3), "1/8");
        assertTrue(Rational.read("-2").get().pow(0) == Rational.ONE);
        aeq(Rational.read("-2").get().pow(1), "-2");
        aeq(Rational.read("-2").get().pow(2), "4");
        aeq(Rational.read("-2").get().pow(3), "-8");
        aeq(Rational.read("-2").get().pow(-1), "-1/2");
        aeq(Rational.read("-2").get().pow(-2), "1/4");
        aeq(Rational.read("-2").get().pow(-3), "-1/8");
        assertTrue(Rational.ZERO.pow(0) == Rational.ONE);
        assertTrue(Rational.ZERO.pow(1) == Rational.ZERO);
        assertTrue(Rational.ZERO.pow(2) == Rational.ZERO);
        assertTrue(Rational.ZERO.pow(3) == Rational.ZERO);
        assertTrue(Rational.ONE.pow(0) == Rational.ONE);
        assertTrue(Rational.ONE.pow(1) == Rational.ONE);
        assertTrue(Rational.ONE.pow(2) == Rational.ONE);
        assertTrue(Rational.ONE.pow(3) == Rational.ONE);
        assertTrue(Rational.ONE.pow(-1) == Rational.ONE);
        assertTrue(Rational.ONE.pow(-2) == Rational.ONE);
        assertTrue(Rational.ONE.pow(-3) == Rational.ONE);
        try {
            Rational.ZERO.pow(-1);
            fail();
        } catch (ArithmeticException e) {}
        try {
            Rational.ZERO.pow(-2);
            fail();
        } catch (ArithmeticException e) {}
        try {
            Rational.ZERO.pow(-3);
            fail();
        } catch (ArithmeticException e) {}
    }

    @Test
    public void testFloor() {
        aeq(Rational.read("7/3").get().floor(), 2);
        aeq(Rational.read("5/3").get().floor(), 1);
        aeq(Rational.read("2/3").get().floor(), 0);
        aeq(Rational.read("-1/3").get().floor(), -1);
        aeq(Rational.read("-4/3").get().floor(), -2);
        aeq(Rational.read("4").get().floor(), 4);
        aeq(Rational.read("-2").get().floor(), -2);
        aeq(Rational.ZERO.floor(), 0);
        aeq(Rational.ONE.floor(), 1);
    }

    @Test
    public void testCeiling() {
        aeq(Rational.read("7/3").get().ceiling(), 3);
        aeq(Rational.read("5/3").get().ceiling(), 2);
        aeq(Rational.read("2/3").get().ceiling(), 1);
        aeq(Rational.read("-1/3").get().ceiling(), 0);
        aeq(Rational.read("-4/3").get().ceiling(), -1);
        aeq(Rational.read("4").get().ceiling(), 4);
        aeq(Rational.read("-2").get().ceiling(), -2);
        aeq(Rational.ZERO.ceiling(), 0);
        aeq(Rational.ONE.ceiling(), 1);
    }

    @Test
    public void testFractionalPart() {
        aeq(Rational.read("7/3").get().fractionalPart(), "1/3");
        aeq(Rational.read("5/3").get().fractionalPart(), "2/3");
        aeq(Rational.read("2/3").get().fractionalPart(), "2/3");
        aeq(Rational.read("-1/3").get().fractionalPart(), "2/3");
        aeq(Rational.read("-4/3").get().fractionalPart(), "2/3");
        assertTrue(Rational.read("4").get().fractionalPart() == Rational.ZERO);
        assertTrue(Rational.read("-2").get().fractionalPart() == Rational.ZERO);
        assertTrue(Rational.ZERO.fractionalPart() == Rational.ZERO);
        assertTrue(Rational.ONE.fractionalPart() == Rational.ZERO);
    }

    @Test
    public void testRound() {
        aeq(Rational.read("11/2").get().round(RoundingMode.UP), 6);
        aeq(Rational.read("5/2").get().round(RoundingMode.UP), 3);
        aeq(Rational.read("8/5").get().round(RoundingMode.UP), 2);
        aeq(Rational.read("11/10").get().round(RoundingMode.UP), 2);
        aeq(Rational.ONE.round(RoundingMode.UP), 1);
        aeq(Rational.ZERO.round(RoundingMode.UP), 0);
        aeq(Rational.read("-1").get().round(RoundingMode.UP), -1);
        aeq(Rational.read("-11/10").get().round(RoundingMode.UP), -2);
        aeq(Rational.read("-8/5").get().round(RoundingMode.UP), -2);
        aeq(Rational.read("-5/2").get().round(RoundingMode.UP), -3);
        aeq(Rational.read("-11/2").get().round(RoundingMode.UP), -6);

        aeq(Rational.read("11/2").get().round(RoundingMode.DOWN), 5);
        aeq(Rational.read("5/2").get().round(RoundingMode.DOWN), 2);
        aeq(Rational.read("8/5").get().round(RoundingMode.DOWN), 1);
        aeq(Rational.read("11/10").get().round(RoundingMode.DOWN), 1);
        aeq(Rational.ONE.round(RoundingMode.DOWN), 1);
        aeq(Rational.ZERO.round(RoundingMode.DOWN), 0);
        aeq(Rational.read("-1").get().round(RoundingMode.DOWN), -1);
        aeq(Rational.read("-11/10").get().round(RoundingMode.DOWN), -1);
        aeq(Rational.read("-8/5").get().round(RoundingMode.DOWN), -1);
        aeq(Rational.read("-5/2").get().round(RoundingMode.DOWN), -2);
        aeq(Rational.read("-11/2").get().round(RoundingMode.DOWN), -5);

        aeq(Rational.read("11/2").get().round(RoundingMode.CEILING), 6);
        aeq(Rational.read("5/2").get().round(RoundingMode.CEILING), 3);
        aeq(Rational.read("8/5").get().round(RoundingMode.CEILING), 2);
        aeq(Rational.read("11/10").get().round(RoundingMode.CEILING), 2);
        aeq(Rational.ONE.round(RoundingMode.CEILING), 1);
        aeq(Rational.ZERO.round(RoundingMode.CEILING), 0);
        aeq(Rational.read("-1").get().round(RoundingMode.CEILING), -1);
        aeq(Rational.read("-11/10").get().round(RoundingMode.CEILING), -1);
        aeq(Rational.read("-8/5").get().round(RoundingMode.CEILING), -1);
        aeq(Rational.read("-5/2").get().round(RoundingMode.CEILING), -2);
        aeq(Rational.read("-11/2").get().round(RoundingMode.CEILING), -5);

        aeq(Rational.read("11/2").get().round(RoundingMode.FLOOR), 5);
        aeq(Rational.read("5/2").get().round(RoundingMode.FLOOR), 2);
        aeq(Rational.read("8/5").get().round(RoundingMode.FLOOR), 1);
        aeq(Rational.read("11/10").get().round(RoundingMode.FLOOR), 1);
        aeq(Rational.ONE.round(RoundingMode.FLOOR), 1);
        aeq(Rational.ZERO.round(RoundingMode.FLOOR), 0);
        aeq(Rational.read("-1").get().round(RoundingMode.FLOOR), -1);
        aeq(Rational.read("-11/10").get().round(RoundingMode.FLOOR), -2);
        aeq(Rational.read("-8/5").get().round(RoundingMode.FLOOR), -2);
        aeq(Rational.read("-5/2").get().round(RoundingMode.FLOOR), -3);
        aeq(Rational.read("-11/2").get().round(RoundingMode.FLOOR), -6);

        aeq(Rational.read("11/2").get().round(RoundingMode.HALF_UP), 6);
        aeq(Rational.read("5/2").get().round(RoundingMode.HALF_UP), 3);
        aeq(Rational.read("8/5").get().round(RoundingMode.HALF_UP), 2);
        aeq(Rational.read("11/10").get().round(RoundingMode.HALF_UP), 1);
        aeq(Rational.ONE.round(RoundingMode.HALF_UP), 1);
        aeq(Rational.ZERO.round(RoundingMode.HALF_UP), 0);
        aeq(Rational.read("-1").get().round(RoundingMode.HALF_UP), -1);
        aeq(Rational.read("-11/10").get().round(RoundingMode.HALF_UP), -1);
        aeq(Rational.read("-8/5").get().round(RoundingMode.HALF_UP), -2);
        aeq(Rational.read("-5/2").get().round(RoundingMode.HALF_UP), -3);
        aeq(Rational.read("-11/2").get().round(RoundingMode.HALF_UP), -6);

        aeq(Rational.read("11/2").get().round(RoundingMode.HALF_DOWN), 5);
        aeq(Rational.read("5/2").get().round(RoundingMode.HALF_DOWN), 2);
        aeq(Rational.read("8/5").get().round(RoundingMode.HALF_DOWN), 2);
        aeq(Rational.read("11/10").get().round(RoundingMode.HALF_DOWN), 1);
        aeq(Rational.ONE.round(RoundingMode.HALF_DOWN), 1);
        aeq(Rational.ZERO.round(RoundingMode.HALF_DOWN), 0);
        aeq(Rational.read("-1").get().round(RoundingMode.HALF_DOWN), -1);
        aeq(Rational.read("-11/10").get().round(RoundingMode.HALF_DOWN), -1);
        aeq(Rational.read("-8/5").get().round(RoundingMode.HALF_DOWN), -2);
        aeq(Rational.read("-5/2").get().round(RoundingMode.HALF_DOWN), -2);
        aeq(Rational.read("-11/2").get().round(RoundingMode.HALF_DOWN), -5);

        aeq(Rational.read("11/2").get().round(RoundingMode.HALF_EVEN), 6);
        aeq(Rational.read("5/2").get().round(RoundingMode.HALF_EVEN), 2);
        aeq(Rational.read("8/5").get().round(RoundingMode.HALF_EVEN), 2);
        aeq(Rational.read("11/10").get().round(RoundingMode.HALF_EVEN), 1);
        aeq(Rational.ONE.round(RoundingMode.HALF_EVEN), 1);
        aeq(Rational.ZERO.round(RoundingMode.HALF_EVEN), 0);
        aeq(Rational.read("-1").get().round(RoundingMode.HALF_EVEN), -1);
        aeq(Rational.read("-11/10").get().round(RoundingMode.HALF_EVEN), -1);
        aeq(Rational.read("-8/5").get().round(RoundingMode.HALF_EVEN), -2);
        aeq(Rational.read("-5/2").get().round(RoundingMode.HALF_EVEN), -2);
        aeq(Rational.read("-11/2").get().round(RoundingMode.HALF_EVEN), -6);

        try {
            Rational.read("11/2").get().round(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        try {
            Rational.read("5/2").get().round(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        try {
            Rational.read("8/5").get().round(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        try {
            Rational.read("11/10").get().round(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.ONE.round(RoundingMode.UNNECESSARY), 1);
        aeq(Rational.ZERO.round(RoundingMode.UNNECESSARY), 0);
        aeq(Rational.read("-1").get().round(RoundingMode.UNNECESSARY), -1);
        try {
            System.out.println(Rational.read("-11/10").get().round(RoundingMode.UNNECESSARY));
            fail();
        } catch (ArithmeticException e) {}
        try {
            System.out.println(Rational.read("-8/5").get().round(RoundingMode.UNNECESSARY));
            fail();
        } catch (ArithmeticException e) {}
        try {
            System.out.println(Rational.read("-5/2").get().round(RoundingMode.UNNECESSARY));
            fail();
        } catch (ArithmeticException e) {}
        try {
            System.out.println(Rational.read("-11/2").get().round(RoundingMode.UNNECESSARY));
            fail();
        } catch (ArithmeticException e) {}
    }

    @Test
    public void testRoundToDenominator() {
        Rational doublePi = Rational.of(Math.PI);
        if (doublePi == null) {
            fail();
        }
        aeq(doublePi.roundToDenominator(BigInteger.ONE, RoundingMode.HALF_EVEN), "3");
        aeq(doublePi.roundToDenominator(BigInteger.valueOf(2), RoundingMode.HALF_EVEN), "3");
        aeq(doublePi.roundToDenominator(BigInteger.valueOf(3), RoundingMode.HALF_EVEN), "3");
        aeq(doublePi.roundToDenominator(BigInteger.valueOf(4), RoundingMode.HALF_EVEN), "13/4");
        aeq(doublePi.roundToDenominator(BigInteger.valueOf(5), RoundingMode.HALF_EVEN), "16/5");
        aeq(doublePi.roundToDenominator(BigInteger.valueOf(6), RoundingMode.HALF_EVEN), "19/6");
        aeq(doublePi.roundToDenominator(BigInteger.valueOf(7), RoundingMode.HALF_EVEN), "22/7");
        aeq(doublePi.roundToDenominator(BigInteger.valueOf(8), RoundingMode.HALF_EVEN), "25/8");
        aeq(doublePi.roundToDenominator(BigInteger.valueOf(9), RoundingMode.HALF_EVEN), "28/9");
        aeq(doublePi.roundToDenominator(BigInteger.valueOf(10), RoundingMode.HALF_EVEN), "31/10");
        aeq(doublePi.roundToDenominator(BigInteger.valueOf(100), RoundingMode.HALF_EVEN), "157/50");
        aeq(doublePi.roundToDenominator(BigInteger.valueOf(1000), RoundingMode.HALF_EVEN), "1571/500");
        aeq(Rational.read("3/10").get().roundToDenominator(BigInteger.valueOf(30), RoundingMode.UNNECESSARY), "3/10");
        try {
            doublePi.roundToDenominator(BigInteger.ZERO, RoundingMode.HALF_EVEN);
            fail();
        } catch (ArithmeticException e) {}
        try {
            doublePi.roundToDenominator(BigInteger.valueOf(-1), RoundingMode.HALF_EVEN);
            fail();
        } catch (ArithmeticException e) {}
        try {
            doublePi.roundToDenominator(BigInteger.valueOf(7), RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
    }

    @Test
    public void testShiftLeft() {
        aeq(Rational.read("7/12").get().shiftLeft(0), "7/12");
        aeq(Rational.read("7/12").get().shiftLeft(1), "7/6");
        aeq(Rational.read("7/12").get().shiftLeft(2), "7/3");
        aeq(Rational.read("7/12").get().shiftLeft(3), "14/3");
        aeq(Rational.read("7/12").get().shiftLeft(4), "28/3");
        aeq(Rational.read("7/12").get().shiftLeft(-1), "7/24");
        aeq(Rational.read("7/12").get().shiftLeft(-2), "7/48");
        aeq(Rational.read("7/12").get().shiftLeft(-3), "7/96");
        aeq(Rational.read("7/12").get().shiftLeft(-4), "7/192");
        aeq(Rational.read("4/5").get().shiftLeft(0), "4/5");
        aeq(Rational.read("4/5").get().shiftLeft(1), "8/5");
        aeq(Rational.read("4/5").get().shiftLeft(2), "16/5");
        aeq(Rational.read("4/5").get().shiftLeft(3), "32/5");
        aeq(Rational.read("4/5").get().shiftLeft(4), "64/5");
        aeq(Rational.read("4/5").get().shiftLeft(-1), "2/5");
        aeq(Rational.read("4/5").get().shiftLeft(-2), "1/5");
        aeq(Rational.read("4/5").get().shiftLeft(-3), "1/10");
        aeq(Rational.read("4/5").get().shiftLeft(-4), "1/20");
        assertTrue(Rational.ZERO.shiftLeft(4) == Rational.ZERO);
        assertTrue(Rational.ONE.shiftLeft(0) == Rational.ONE);
        aeq(Rational.ONE.shiftLeft(1), "2");
        aeq(Rational.ONE.shiftLeft(2), "4");
        aeq(Rational.ONE.shiftLeft(3), "8");
        aeq(Rational.ONE.shiftLeft(4), "16");
        aeq(Rational.ONE.shiftLeft(-1), "1/2");
        aeq(Rational.ONE.shiftLeft(-2), "1/4");
        aeq(Rational.ONE.shiftLeft(-3), "1/8");
        aeq(Rational.ONE.shiftLeft(-4), "1/16");
        aeq(Rational.read("-4/5").get().shiftLeft(0), "-4/5");
        aeq(Rational.read("-4/5").get().shiftLeft(1), "-8/5");
        aeq(Rational.read("-4/5").get().shiftLeft(2), "-16/5");
        aeq(Rational.read("-4/5").get().shiftLeft(3), "-32/5");
        aeq(Rational.read("-4/5").get().shiftLeft(4), "-64/5");
        aeq(Rational.read("-4/5").get().shiftLeft(-1), "-2/5");
        aeq(Rational.read("-4/5").get().shiftLeft(-2), "-1/5");
        aeq(Rational.read("-4/5").get().shiftLeft(-3), "-1/10");
        aeq(Rational.read("-4/5").get().shiftLeft(-4), "-1/20");
        aeq(Rational.read("-1").get().shiftLeft(0), "-1");
        aeq(Rational.read("-1").get().shiftLeft(1), "-2");
        aeq(Rational.read("-1").get().shiftLeft(2), "-4");
        aeq(Rational.read("-1").get().shiftLeft(3), "-8");
        aeq(Rational.read("-1").get().shiftLeft(4), "-16");
        aeq(Rational.read("-1").get().shiftLeft(-1), "-1/2");
        aeq(Rational.read("-1").get().shiftLeft(-2), "-1/4");
        aeq(Rational.read("-1").get().shiftLeft(-3), "-1/8");
        aeq(Rational.read("-1").get().shiftLeft(-4), "-1/16");
    }

    @Test
    public void testShiftRight() {
        aeq(Rational.read("7/12").get().shiftRight(0), "7/12");
        aeq(Rational.read("7/12").get().shiftRight(1), "7/24");
        aeq(Rational.read("7/12").get().shiftRight(2), "7/48");
        aeq(Rational.read("7/12").get().shiftRight(3), "7/96");
        aeq(Rational.read("7/12").get().shiftRight(4), "7/192");
        aeq(Rational.read("7/12").get().shiftRight(-1), "7/6");
        aeq(Rational.read("7/12").get().shiftRight(-2), "7/3");
        aeq(Rational.read("7/12").get().shiftRight(-3), "14/3");
        aeq(Rational.read("7/12").get().shiftRight(-4), "28/3");
        aeq(Rational.read("4/5").get().shiftRight(0), "4/5");
        aeq(Rational.read("4/5").get().shiftRight(1), "2/5");
        aeq(Rational.read("4/5").get().shiftRight(2), "1/5");
        aeq(Rational.read("4/5").get().shiftRight(3), "1/10");
        aeq(Rational.read("4/5").get().shiftRight(4), "1/20");
        aeq(Rational.read("4/5").get().shiftRight(-1), "8/5");
        aeq(Rational.read("4/5").get().shiftRight(-2), "16/5");
        aeq(Rational.read("4/5").get().shiftRight(-3), "32/5");
        aeq(Rational.read("4/5").get().shiftRight(-4), "64/5");
        assertTrue(Rational.ZERO.shiftRight(4) == Rational.ZERO);
        assertTrue(Rational.ONE.shiftRight(0) == Rational.ONE);
        aeq(Rational.ONE.shiftRight(1), "1/2");
        aeq(Rational.ONE.shiftRight(2), "1/4");
        aeq(Rational.ONE.shiftRight(3), "1/8");
        aeq(Rational.ONE.shiftRight(4), "1/16");
        aeq(Rational.ONE.shiftRight(-1), "2");
        aeq(Rational.ONE.shiftRight(-2), "4");
        aeq(Rational.ONE.shiftRight(-3), "8");
        aeq(Rational.ONE.shiftRight(-4), "16");
        aeq(Rational.read("-4/5").get().shiftRight(0), "-4/5");
        aeq(Rational.read("-4/5").get().shiftRight(1), "-2/5");
        aeq(Rational.read("-4/5").get().shiftRight(2), "-1/5");
        aeq(Rational.read("-4/5").get().shiftRight(3), "-1/10");
        aeq(Rational.read("-4/5").get().shiftRight(4), "-1/20");
        aeq(Rational.read("-4/5").get().shiftRight(-1), "-8/5");
        aeq(Rational.read("-4/5").get().shiftRight(-2), "-16/5");
        aeq(Rational.read("-4/5").get().shiftRight(-3), "-32/5");
        aeq(Rational.read("-4/5").get().shiftRight(-4), "-64/5");
        aeq(Rational.read("-1").get().shiftRight(0), "-1");
        aeq(Rational.read("-1").get().shiftRight(1), "-1/2");
        aeq(Rational.read("-1").get().shiftRight(2), "-1/4");
        aeq(Rational.read("-1").get().shiftRight(3), "-1/8");
        aeq(Rational.read("-1").get().shiftRight(4), "-1/16");
        aeq(Rational.read("-1").get().shiftRight(-1), "-2");
        aeq(Rational.read("-1").get().shiftRight(-2), "-4");
        aeq(Rational.read("-1").get().shiftRight(-3), "-8");
        aeq(Rational.read("-1").get().shiftRight(-4), "-16");
    }

    @Test
    public void testBinaryExponent() {
        aeq(Rational.read("1/3").get().binaryExponent(), -2);
        aeq(Rational.read("100").get().binaryExponent(), 6);
        aeq(Rational.read("127").get().binaryExponent(), 6);
        aeq(Rational.read("128").get().binaryExponent(), 7);
        aeq(Rational.read("129").get().binaryExponent(), 7);
        aeq(Rational.read("1/127").get().binaryExponent(), -7);
        aeq(Rational.read("1/128").get().binaryExponent(), -7);
        aeq(Rational.read("1/129").get().binaryExponent(), -8);
        aeq(Rational.ONE.binaryExponent(), 0);
    }

    @Test
    public void testToFloat() {
        aeq(Rational.ZERO.toFloat(), 0.0);
        aeq(Rational.ONE.toFloat(), 1.0);
        aeq(Rational.read("1/2").get().toFloat(), 0.5);
        aeq(Rational.read("1/3").get().toFloat(), 0.33333334);
        aeq(Rational.read("-1/3").get().toFloat(), -0.33333334);
        Rational almostOne = Rational.of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
        aeq(almostOne.toFloat(), 1.0);
        Rational floatPi = Rational.of((float) Math.PI);
        if (floatPi == null) {
            fail();
        }
        aeq(floatPi.toFloat(), 3.1415927);
        Rational trillion = Rational.of(BigInteger.TEN.pow(12));
        aeq(trillion.toFloat(), 1.0E12);
        Rational piSuccessor = Rational.of(Numbers.successor((float) Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = Rational.of(Numbers.predecessor((float) Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = Rational.add(floatPi, piSuccessor).divide(2);
        Rational halfBelowPi = Rational.add(floatPi, piPredecessor).divide(2);
        Rational justAbovePi = Rational.add(floatPi.multiply(2), piSuccessor).divide(3);
        Rational justBelowPi = Rational.add(floatPi.multiply(2), piPredecessor).divide(3);
        aeq(halfAbovePi.toFloat(), 3.141593);
        aeq(halfBelowPi.toFloat(), 3.1415925);
        aeq(justAbovePi.toFloat(), 3.1415927);
        aeq(justBelowPi.toFloat(), 3.1415927);
        Rational subnormal = Rational.of(1e-40f);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = Rational.of(Numbers.successor(1e-40f));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = Rational.of(Numbers.predecessor(1e-40f));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = Rational.add(subnormal, subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = Rational.add(subnormal, subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = Rational.add(subnormal.multiply(2), subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = Rational.add(subnormal.multiply(2), subnormalPredecessor).divide(3);
        aeq(subnormal.toFloat(), 1.0E-40);
        aeq(halfAboveSubnormal.toFloat(), 1.0E-40);
        aeq(halfBelowSubnormal.toFloat(), 1.0E-40);
        aeq(justAboveSubnormal.toFloat(), 1.0E-40);
        aeq(justBelowSubnormal.toFloat(), 1.0E-40);
        Rational belowNegativeMax = Rational.subtract(Rational.LARGEST_FLOAT.negate(), Rational.ONE);
        Rational negativeMaxSuccessor = Rational.of(Numbers.successor(-Float.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = Rational.add(Rational.LARGEST_FLOAT.negate(), negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = Rational.add(Rational.LARGEST_FLOAT.negate().multiply(2), negativeMaxSuccessor).divide(3);
        aeq(belowNegativeMax.toFloat(), Float.NEGATIVE_INFINITY);
        aeq(halfAboveNegativeMax.toFloat(), -3.4028233E38);
        aeq(justAboveNegativeMax.toFloat(), -3.4028235E38);
        Rational aboveMax = Rational.add(Rational.LARGEST_FLOAT, Rational.ONE);
        Rational maxPredecessor = Rational.of(Numbers.predecessor(Float.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = Rational.add(Rational.LARGEST_FLOAT, maxPredecessor).divide(2);
        Rational justBelowMax = Rational.add(Rational.LARGEST_FLOAT.multiply(2), maxPredecessor).divide(3);
        aeq(aboveMax.toFloat(), Float.POSITIVE_INFINITY);
        aeq(halfBelowMax.toFloat(), 3.4028233E38);
        aeq(justBelowMax.toFloat(), 3.4028235E38);
        Rational halfAboveZero = Rational.SMALLEST_FLOAT.shiftRight(1);
        Rational justAboveZero = Rational.SMALLEST_FLOAT.divide(3);
        Rational halfBelowZero = halfAboveZero.negate();
        Rational justBelowZero = justAboveZero.negate();
        aeq(halfBelowZero.toFloat(), -0.0);
        aeq(justBelowZero.toFloat(), -0.0);
        aeq(halfAboveZero.toFloat(), 0.0);
        aeq(justAboveZero.toFloat(), 0.0);
        Rational boundary = Rational.add(Rational.LARGEST_SUBNORMAL_FLOAT, Rational.SMALLEST_NORMAL_FLOAT).shiftRight(1);
        Rational halfBelowBoundary = Rational.add(Rational.LARGEST_SUBNORMAL_FLOAT, boundary).shiftRight(1);
        Rational halfAboveBoundary = Rational.add(Rational.SMALLEST_NORMAL_FLOAT, boundary).shiftRight(1);
        Rational justBelowBoundary = Rational.add(Rational.LARGEST_SUBNORMAL_FLOAT, boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = Rational.add(Rational.SMALLEST_NORMAL_FLOAT, boundary.shiftLeft(1)).divide(3);
        aeq(boundary.toFloat(), 1.17549435E-38);
        aeq(halfBelowBoundary.toFloat(), 1.1754942E-38);
        aeq(justBelowBoundary.toFloat(), 1.1754942E-38);
        aeq(halfAboveBoundary.toFloat(), 1.17549435E-38);
        aeq(justAboveBoundary.toFloat(), 1.17549435E-38);
    }

    @Test
    public void testToFloatRoundingMode() {
        aeq(Rational.ZERO.toFloat(RoundingMode.FLOOR), 0.0);
        aeq(Rational.ZERO.toFloat(RoundingMode.CEILING), 0.0);
        aeq(Rational.ZERO.toFloat(RoundingMode.DOWN), 0.0);
        aeq(Rational.ZERO.toFloat(RoundingMode.UP), 0.0);
        aeq(Rational.ZERO.toFloat(RoundingMode.HALF_DOWN), 0.0);
        aeq(Rational.ZERO.toFloat(RoundingMode.HALF_UP), 0.0);
        aeq(Rational.ZERO.toFloat(RoundingMode.HALF_EVEN), 0.0);
        aeq(Rational.ZERO.toFloat(RoundingMode.UNNECESSARY), 0.0);
        aeq(Rational.ONE.toFloat(RoundingMode.FLOOR), 1.0);
        aeq(Rational.ONE.toFloat(RoundingMode.CEILING), 1.0);
        aeq(Rational.ONE.toFloat(RoundingMode.DOWN), 1.0);
        aeq(Rational.ONE.toFloat(RoundingMode.UP), 1.0);
        aeq(Rational.ONE.toFloat(RoundingMode.HALF_DOWN), 1.0);
        aeq(Rational.ONE.toFloat(RoundingMode.HALF_UP), 1.0);
        aeq(Rational.ONE.toFloat(RoundingMode.HALF_EVEN), 1.0);
        aeq(Rational.ONE.toFloat(RoundingMode.UNNECESSARY), 1.0);
        aeq(Rational.read("1/2").get().toFloat(RoundingMode.FLOOR), 0.5);
        aeq(Rational.read("1/2").get().toFloat(RoundingMode.CEILING), 0.5);
        aeq(Rational.read("1/2").get().toFloat(RoundingMode.DOWN), 0.5);
        aeq(Rational.read("1/2").get().toFloat(RoundingMode.UP), 0.5);
        aeq(Rational.read("1/2").get().toFloat(RoundingMode.HALF_DOWN), 0.5);
        aeq(Rational.read("1/2").get().toFloat(RoundingMode.HALF_UP), 0.5);
        aeq(Rational.read("1/2").get().toFloat(RoundingMode.HALF_EVEN), 0.5);
        aeq(Rational.read("1/2").get().toFloat(RoundingMode.UNNECESSARY), 0.5);
        aeq(Rational.read("1/3").get().toFloat(RoundingMode.FLOOR), 0.3333333);
        aeq(Rational.read("1/3").get().toFloat(RoundingMode.CEILING), 0.33333334);
        aeq(Rational.read("1/3").get().toFloat(RoundingMode.DOWN), 0.3333333);
        aeq(Rational.read("1/3").get().toFloat(RoundingMode.UP), 0.33333334);
        aeq(Rational.read("1/3").get().toFloat(RoundingMode.HALF_DOWN), 0.33333334);
        aeq(Rational.read("1/3").get().toFloat(RoundingMode.HALF_UP), 0.33333334);
        aeq(Rational.read("1/3").get().toFloat(RoundingMode.HALF_EVEN), 0.33333334);
        try {
            Rational.read("1/3").get().toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.read("-1/3").get().toFloat(RoundingMode.FLOOR), -0.33333334);
        aeq(Rational.read("-1/3").get().toFloat(RoundingMode.CEILING), -0.3333333);
        aeq(Rational.read("-1/3").get().toFloat(RoundingMode.DOWN), -0.3333333);
        aeq(Rational.read("-1/3").get().toFloat(RoundingMode.UP), -0.33333334);
        aeq(Rational.read("-1/3").get().toFloat(RoundingMode.HALF_DOWN), -0.33333334);
        aeq(Rational.read("-1/3").get().toFloat(RoundingMode.HALF_UP), -0.33333334);
        aeq(Rational.read("-1/3").get().toFloat(RoundingMode.HALF_EVEN), -0.33333334);
        try {
            Rational.read("-1/3").get().toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational almostOne = Rational.of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
        aeq(almostOne.toFloat(RoundingMode.FLOOR), 0.99999994);
        aeq(almostOne.toFloat(RoundingMode.CEILING), 1.0);
        aeq(almostOne.toFloat(RoundingMode.DOWN), 0.99999994);
        aeq(almostOne.toFloat(RoundingMode.UP), 1.0);
        aeq(almostOne.toFloat(RoundingMode.HALF_DOWN), 1.0);
        aeq(almostOne.toFloat(RoundingMode.HALF_UP), 1.0);
        aeq(almostOne.toFloat(RoundingMode.HALF_EVEN), 1.0);
        try {
            almostOne.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational floatPi = Rational.of((float) Math.PI);
        if (floatPi == null) {
            fail();
        }
        aeq(floatPi.toFloat(RoundingMode.FLOOR), 3.1415927);
        aeq(floatPi.toFloat(RoundingMode.CEILING), 3.1415927);
        aeq(floatPi.toFloat(RoundingMode.DOWN), 3.1415927);
        aeq(floatPi.toFloat(RoundingMode.UP), 3.1415927);
        aeq(floatPi.toFloat(RoundingMode.HALF_DOWN), 3.1415927);
        aeq(floatPi.toFloat(RoundingMode.HALF_UP), 3.1415927);
        aeq(floatPi.toFloat(RoundingMode.HALF_EVEN), 3.1415927);
        aeq(floatPi.toFloat(RoundingMode.UNNECESSARY), 3.1415927);
        Rational trillion = Rational.of(BigInteger.TEN.pow(12));
        aeq(trillion.toFloat(RoundingMode.FLOOR), 1.0E12);
        aeq(trillion.toFloat(RoundingMode.CEILING), 1.00000006E12);
        aeq(trillion.toFloat(RoundingMode.DOWN), 1.0E12);
        aeq(trillion.toFloat(RoundingMode.UP), 1.00000006E12);
        aeq(trillion.toFloat(RoundingMode.HALF_DOWN), 1.0E12);
        aeq(trillion.toFloat(RoundingMode.HALF_UP), 1.0E12);
        aeq(trillion.toFloat(RoundingMode.HALF_EVEN), 1.0E12);
        try {
            trillion.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational piSuccessor = Rational.of(Numbers.successor((float) Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = Rational.of(Numbers.predecessor((float) Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = Rational.add(floatPi, piSuccessor).divide(2);
        Rational halfBelowPi = Rational.add(floatPi, piPredecessor).divide(2);
        Rational justAbovePi = Rational.add(floatPi.multiply(2), piSuccessor).divide(3);
        Rational justBelowPi = Rational.add(floatPi.multiply(2), piPredecessor).divide(3);
        aeq(halfAbovePi.toFloat(RoundingMode.FLOOR), 3.1415927);
        aeq(halfAbovePi.toFloat(RoundingMode.CEILING), 3.141593);
        aeq(halfAbovePi.toFloat(RoundingMode.DOWN), 3.1415927);
        aeq(halfAbovePi.toFloat(RoundingMode.UP), 3.141593);
        aeq(halfAbovePi.toFloat(RoundingMode.HALF_DOWN), 3.1415927);
        aeq(halfAbovePi.toFloat(RoundingMode.HALF_UP), 3.141593);
        aeq(halfAbovePi.toFloat(RoundingMode.HALF_EVEN), 3.141593);
        try {
            halfAbovePi.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfBelowPi.toFloat(RoundingMode.FLOOR), 3.1415925);
        aeq(halfBelowPi.toFloat(RoundingMode.CEILING), 3.1415927);
        aeq(halfBelowPi.toFloat(RoundingMode.DOWN), 3.1415925);
        aeq(halfBelowPi.toFloat(RoundingMode.UP), 3.1415927);
        aeq(halfBelowPi.toFloat(RoundingMode.HALF_DOWN), 3.1415925);
        aeq(halfBelowPi.toFloat(RoundingMode.HALF_UP), 3.1415927);
        aeq(halfBelowPi.toFloat(RoundingMode.HALF_EVEN), 3.1415925);
        try {
            halfBelowPi.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justAbovePi.toFloat(RoundingMode.FLOOR), 3.1415927);
        aeq(justAbovePi.toFloat(RoundingMode.CEILING), 3.141593);
        aeq(justAbovePi.toFloat(RoundingMode.DOWN), 3.1415927);
        aeq(justAbovePi.toFloat(RoundingMode.UP), 3.141593);
        aeq(justAbovePi.toFloat(RoundingMode.HALF_DOWN), 3.1415927);
        aeq(justAbovePi.toFloat(RoundingMode.HALF_UP), 3.1415927);
        aeq(justAbovePi.toFloat(RoundingMode.HALF_EVEN), 3.1415927);
        try {
            justAbovePi.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justBelowPi.toFloat(RoundingMode.FLOOR), 3.1415925);
        aeq(justBelowPi.toFloat(RoundingMode.CEILING), 3.1415927);
        aeq(justBelowPi.toFloat(RoundingMode.DOWN), 3.1415925);
        aeq(justBelowPi.toFloat(RoundingMode.UP), 3.1415927);
        aeq(justBelowPi.toFloat(RoundingMode.HALF_DOWN), 3.1415927);
        aeq(justBelowPi.toFloat(RoundingMode.HALF_UP), 3.1415927);
        aeq(justBelowPi.toFloat(RoundingMode.HALF_EVEN), 3.1415927);
        try {
            justBelowPi.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational subnormal = Rational.of(1e-40f);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = Rational.of(Numbers.successor(1e-40f));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = Rational.of(Numbers.predecessor(1e-40f));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = Rational.add(subnormal, subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = Rational.add(subnormal, subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = Rational.add(subnormal.multiply(2), subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = Rational.add(subnormal.multiply(2), subnormalPredecessor).divide(3);
        aeq(subnormal.toFloat(RoundingMode.FLOOR), 1.0E-40);
        aeq(subnormal.toFloat(RoundingMode.CEILING), 1.0E-40);
        aeq(subnormal.toFloat(RoundingMode.DOWN), 1.0E-40);
        aeq(subnormal.toFloat(RoundingMode.UP), 1.0E-40);
        aeq(subnormal.toFloat(RoundingMode.HALF_DOWN), 1.0E-40);
        aeq(subnormal.toFloat(RoundingMode.HALF_UP), 1.0E-40);
        aeq(subnormal.toFloat(RoundingMode.HALF_EVEN), 1.0E-40);
        aeq(subnormal.toFloat(RoundingMode.UNNECESSARY), 1.0E-40);
        aeq(halfAboveSubnormal.toFloat(RoundingMode.FLOOR), 1.0E-40);
        aeq(halfAboveSubnormal.toFloat(RoundingMode.CEILING), 1.00001E-40);
        aeq(halfAboveSubnormal.toFloat(RoundingMode.DOWN), 1.0E-40);
        aeq(halfAboveSubnormal.toFloat(RoundingMode.UP), 1.00001E-40);
        aeq(halfAboveSubnormal.toFloat(RoundingMode.HALF_DOWN), 1.0E-40);
        aeq(halfAboveSubnormal.toFloat(RoundingMode.HALF_UP), 1.00001E-40);
        aeq(halfAboveSubnormal.toFloat(RoundingMode.HALF_EVEN), 1.0E-40);
        try {
            halfAboveSubnormal.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfBelowSubnormal.toFloat(RoundingMode.FLOOR), 9.9998E-41);
        aeq(halfBelowSubnormal.toFloat(RoundingMode.CEILING), 1.0E-40);
        aeq(halfBelowSubnormal.toFloat(RoundingMode.DOWN), 9.9998E-41);
        aeq(halfBelowSubnormal.toFloat(RoundingMode.UP), 1.0E-40);
        aeq(halfBelowSubnormal.toFloat(RoundingMode.HALF_DOWN), 9.9998E-41);
        aeq(halfBelowSubnormal.toFloat(RoundingMode.HALF_UP), 1.0E-40);
        aeq(halfBelowSubnormal.toFloat(RoundingMode.HALF_EVEN), 1.0E-40);
        try {
            halfBelowSubnormal.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justAboveSubnormal.toFloat(RoundingMode.FLOOR), 1.0E-40);
        aeq(justAboveSubnormal.toFloat(RoundingMode.CEILING), 1.00001E-40);
        aeq(justAboveSubnormal.toFloat(RoundingMode.DOWN), 1.0E-40);
        aeq(justAboveSubnormal.toFloat(RoundingMode.UP), 1.00001E-40);
        aeq(justAboveSubnormal.toFloat(RoundingMode.HALF_DOWN), 1.0E-40);
        aeq(justAboveSubnormal.toFloat(RoundingMode.HALF_UP), 1.0E-40);
        aeq(justAboveSubnormal.toFloat(RoundingMode.HALF_EVEN), 1.0E-40);
        try {
            justAboveSubnormal.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justBelowSubnormal.toFloat(RoundingMode.FLOOR), 9.9998E-41);
        aeq(justBelowSubnormal.toFloat(RoundingMode.CEILING), 1.0E-40);
        aeq(justBelowSubnormal.toFloat(RoundingMode.DOWN), 9.9998E-41);
        aeq(justBelowSubnormal.toFloat(RoundingMode.UP), 1.0E-40);
        aeq(justBelowSubnormal.toFloat(RoundingMode.HALF_DOWN), 1.0E-40);
        aeq(justBelowSubnormal.toFloat(RoundingMode.HALF_UP), 1.0E-40);
        aeq(justBelowSubnormal.toFloat(RoundingMode.HALF_EVEN), 1.0E-40);
        try {
            justBelowSubnormal.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational belowNegativeMax = Rational.subtract(Rational.LARGEST_FLOAT.negate(), Rational.ONE);
        Rational negativeMaxSuccessor = Rational.of(Numbers.successor(-Float.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = Rational.add(Rational.LARGEST_FLOAT.negate(), negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = Rational.add(Rational.LARGEST_FLOAT.negate().multiply(2), negativeMaxSuccessor).divide(3);
        aeq(belowNegativeMax.toFloat(RoundingMode.FLOOR), Float.NEGATIVE_INFINITY);
        aeq(belowNegativeMax.toFloat(RoundingMode.CEILING), -3.4028235E38);
        aeq(belowNegativeMax.toFloat(RoundingMode.DOWN), -3.4028235E38);
        aeq(belowNegativeMax.toFloat(RoundingMode.UP), Float.NEGATIVE_INFINITY);
        aeq(belowNegativeMax.toFloat(RoundingMode.HALF_DOWN), -3.4028235E38);
        aeq(belowNegativeMax.toFloat(RoundingMode.HALF_UP), Float.NEGATIVE_INFINITY);
        aeq(belowNegativeMax.toFloat(RoundingMode.HALF_EVEN), Float.NEGATIVE_INFINITY);
        try {
            belowNegativeMax.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfAboveNegativeMax.toFloat(RoundingMode.FLOOR), -3.4028235E38);
        aeq(halfAboveNegativeMax.toFloat(RoundingMode.CEILING), -3.4028233E38);
        aeq(halfAboveNegativeMax.toFloat(RoundingMode.DOWN), -3.4028233E38);
        aeq(halfAboveNegativeMax.toFloat(RoundingMode.UP), -3.4028235E38);
        aeq(halfAboveNegativeMax.toFloat(RoundingMode.HALF_DOWN), -3.4028233E38);
        aeq(halfAboveNegativeMax.toFloat(RoundingMode.HALF_UP), -3.4028235E38);
        aeq(halfAboveNegativeMax.toFloat(RoundingMode.HALF_EVEN), -3.4028233E38);
        try {
            halfAboveNegativeMax.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justAboveNegativeMax.toFloat(RoundingMode.FLOOR), -3.4028235E38);
        aeq(justAboveNegativeMax.toFloat(RoundingMode.CEILING), -3.4028233E38);
        aeq(justAboveNegativeMax.toFloat(RoundingMode.DOWN), -3.4028233E38);
        aeq(justAboveNegativeMax.toFloat(RoundingMode.UP), -3.4028235E38);
        aeq(justAboveNegativeMax.toFloat(RoundingMode.HALF_DOWN), -3.4028235E38);
        aeq(justAboveNegativeMax.toFloat(RoundingMode.HALF_UP), -3.4028235E38);
        aeq(justAboveNegativeMax.toFloat(RoundingMode.HALF_EVEN), -3.4028235E38);
        try {
            justAboveNegativeMax.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational aboveMax = Rational.add(Rational.LARGEST_FLOAT, Rational.ONE);
        Rational maxPredecessor = Rational.of(Numbers.predecessor(Float.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = Rational.add(Rational.LARGEST_FLOAT, maxPredecessor).divide(2);
        Rational justBelowMax = Rational.add(Rational.LARGEST_FLOAT.multiply(2), maxPredecessor).divide(3);
        aeq(aboveMax.toFloat(RoundingMode.FLOOR), 3.4028235E38);
        aeq(aboveMax.toFloat(RoundingMode.CEILING), Float.POSITIVE_INFINITY);
        aeq(aboveMax.toFloat(RoundingMode.DOWN), 3.4028235E38);
        aeq(aboveMax.toFloat(RoundingMode.UP), Float.POSITIVE_INFINITY);
        aeq(aboveMax.toFloat(RoundingMode.HALF_DOWN), 3.4028235E38);
        aeq(aboveMax.toFloat(RoundingMode.HALF_UP), Float.POSITIVE_INFINITY);
        aeq(aboveMax.toFloat(RoundingMode.HALF_EVEN), Float.POSITIVE_INFINITY);
        try {
            aboveMax.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfBelowMax.toFloat(RoundingMode.FLOOR), 3.4028233E38);
        aeq(halfBelowMax.toFloat(RoundingMode.CEILING), 3.4028235E38);
        aeq(halfBelowMax.toFloat(RoundingMode.DOWN), 3.4028233E38);
        aeq(halfBelowMax.toFloat(RoundingMode.UP), 3.4028235E38);
        aeq(halfBelowMax.toFloat(RoundingMode.HALF_DOWN), 3.4028233E38);
        aeq(halfBelowMax.toFloat(RoundingMode.HALF_UP), 3.4028235E38);
        aeq(halfBelowMax.toFloat(RoundingMode.HALF_EVEN), 3.4028233E38);
        try {
            halfBelowMax.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justBelowMax.toFloat(RoundingMode.FLOOR), 3.4028233E38);
        aeq(justBelowMax.toFloat(RoundingMode.CEILING), 3.4028235E38);
        aeq(justBelowMax.toFloat(RoundingMode.DOWN), 3.4028233E38);
        aeq(justBelowMax.toFloat(RoundingMode.UP), 3.4028235E38);
        aeq(justBelowMax.toFloat(RoundingMode.HALF_DOWN), 3.4028235E38);
        aeq(justBelowMax.toFloat(RoundingMode.HALF_UP), 3.4028235E38);
        aeq(justBelowMax.toFloat(RoundingMode.HALF_EVEN), 3.4028235E38);
        try {
            justBelowMax.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational halfAboveZero = Rational.SMALLEST_FLOAT.shiftRight(1);
        Rational justAboveZero = Rational.SMALLEST_FLOAT.divide(3);
        Rational halfBelowZero = halfAboveZero.negate();
        Rational justBelowZero = justAboveZero.negate();
        aeq(halfBelowZero.toFloat(RoundingMode.FLOOR), -1.4E-45);
        aeq(halfBelowZero.toFloat(RoundingMode.CEILING), -0.0);
        aeq(halfBelowZero.toFloat(RoundingMode.DOWN), -0.0);
        aeq(halfBelowZero.toFloat(RoundingMode.UP), -1.4E-45);
        aeq(halfBelowZero.toFloat(RoundingMode.HALF_DOWN), -0.0);
        aeq(halfBelowZero.toFloat(RoundingMode.HALF_UP), -1.4E-45);
        aeq(halfBelowZero.toFloat(RoundingMode.HALF_EVEN), -0.0);
        try {
            halfBelowZero.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justBelowZero.toFloat(RoundingMode.FLOOR), -1.4E-45);
        aeq(justBelowZero.toFloat(RoundingMode.CEILING), -0.0);
        aeq(justBelowZero.toFloat(RoundingMode.DOWN), -0.0);
        aeq(justBelowZero.toFloat(RoundingMode.UP), -1.4E-45);
        aeq(justBelowZero.toFloat(RoundingMode.HALF_DOWN), -0.0);
        aeq(justBelowZero.toFloat(RoundingMode.HALF_UP), -0.0);
        aeq(justBelowZero.toFloat(RoundingMode.HALF_EVEN), -0.0);
        try {
            justBelowZero.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfAboveZero.toFloat(RoundingMode.FLOOR), 0.0);
        aeq(halfAboveZero.toFloat(RoundingMode.CEILING), 1.4E-45);
        aeq(halfAboveZero.toFloat(RoundingMode.DOWN), 0.0);
        aeq(halfAboveZero.toFloat(RoundingMode.UP), 1.4E-45);
        aeq(halfAboveZero.toFloat(RoundingMode.HALF_DOWN), 0.0);
        aeq(halfAboveZero.toFloat(RoundingMode.HALF_UP), 1.4E-45);
        aeq(halfAboveZero.toFloat(RoundingMode.HALF_EVEN), 0.0);
        try {
            halfAboveZero.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justAboveZero.toFloat(RoundingMode.FLOOR), 0.0);
        aeq(justAboveZero.toFloat(RoundingMode.CEILING), 1.4E-45);
        aeq(justAboveZero.toFloat(RoundingMode.DOWN), 0.0);
        aeq(justAboveZero.toFloat(RoundingMode.UP), 1.4E-45);
        aeq(justAboveZero.toFloat(RoundingMode.HALF_DOWN), 0.0);
        aeq(justAboveZero.toFloat(RoundingMode.HALF_UP), 0.0);
        aeq(justAboveZero.toFloat(RoundingMode.HALF_EVEN), 0.0);
        try {
            justAboveZero.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational boundary = Rational.add(Rational.LARGEST_SUBNORMAL_FLOAT, Rational.SMALLEST_NORMAL_FLOAT).shiftRight(1);
        Rational halfBelowBoundary = Rational.add(Rational.LARGEST_SUBNORMAL_FLOAT, boundary).shiftRight(1);
        Rational halfAboveBoundary = Rational.add(Rational.SMALLEST_NORMAL_FLOAT, boundary).shiftRight(1);
        Rational justBelowBoundary = Rational.add(Rational.LARGEST_SUBNORMAL_FLOAT, boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = Rational.add(Rational.SMALLEST_NORMAL_FLOAT, boundary.shiftLeft(1)).divide(3);
        aeq(boundary.toFloat(RoundingMode.FLOOR), 1.1754942E-38);
        aeq(boundary.toFloat(RoundingMode.CEILING), 1.17549435E-38);
        aeq(boundary.toFloat(RoundingMode.DOWN), 1.1754942E-38);
        aeq(boundary.toFloat(RoundingMode.UP), 1.17549435E-38);
        aeq(boundary.toFloat(RoundingMode.HALF_DOWN), 1.1754942E-38);
        aeq(boundary.toFloat(RoundingMode.HALF_UP), 1.17549435E-38);
        aeq(boundary.toFloat(RoundingMode.HALF_EVEN), 1.17549435E-38);
        try {
            boundary.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfBelowBoundary.toFloat(RoundingMode.FLOOR), 1.1754942E-38);
        aeq(halfBelowBoundary.toFloat(RoundingMode.CEILING), 1.17549435E-38);
        aeq(halfBelowBoundary.toFloat(RoundingMode.DOWN), 1.1754942E-38);
        aeq(halfBelowBoundary.toFloat(RoundingMode.UP), 1.17549435E-38);
        aeq(halfBelowBoundary.toFloat(RoundingMode.HALF_DOWN), 1.1754942E-38);
        aeq(halfBelowBoundary.toFloat(RoundingMode.HALF_UP), 1.1754942E-38);
        aeq(halfBelowBoundary.toFloat(RoundingMode.HALF_EVEN), 1.1754942E-38);
        try {
            halfBelowBoundary.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justBelowBoundary.toFloat(RoundingMode.FLOOR), 1.1754942E-38);
        aeq(justBelowBoundary.toFloat(RoundingMode.CEILING), 1.17549435E-38);
        aeq(justBelowBoundary.toFloat(RoundingMode.DOWN), 1.1754942E-38);
        aeq(justBelowBoundary.toFloat(RoundingMode.UP), 1.17549435E-38);
        aeq(justBelowBoundary.toFloat(RoundingMode.HALF_DOWN), 1.1754942E-38);
        aeq(justBelowBoundary.toFloat(RoundingMode.HALF_UP), 1.1754942E-38);
        aeq(justBelowBoundary.toFloat(RoundingMode.HALF_EVEN), 1.1754942E-38);
        try {
            justBelowBoundary.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfAboveBoundary.toFloat(RoundingMode.FLOOR), 1.1754942E-38);
        aeq(halfAboveBoundary.toFloat(RoundingMode.CEILING), 1.17549435E-38);
        aeq(halfAboveBoundary.toFloat(RoundingMode.DOWN), 1.1754942E-38);
        aeq(halfAboveBoundary.toFloat(RoundingMode.UP), 1.17549435E-38);
        aeq(halfAboveBoundary.toFloat(RoundingMode.HALF_DOWN), 1.17549435E-38);
        aeq(halfAboveBoundary.toFloat(RoundingMode.HALF_UP), 1.17549435E-38);
        aeq(halfAboveBoundary.toFloat(RoundingMode.HALF_EVEN), 1.17549435E-38);
        try {
            halfAboveBoundary.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justAboveBoundary.toFloat(RoundingMode.FLOOR), 1.1754942E-38);
        aeq(justAboveBoundary.toFloat(RoundingMode.CEILING), 1.17549435E-38);
        aeq(justAboveBoundary.toFloat(RoundingMode.DOWN), 1.1754942E-38);
        aeq(justAboveBoundary.toFloat(RoundingMode.UP), 1.17549435E-38);
        aeq(justAboveBoundary.toFloat(RoundingMode.HALF_DOWN), 1.17549435E-38);
        aeq(justAboveBoundary.toFloat(RoundingMode.HALF_UP), 1.17549435E-38);
        aeq(justAboveBoundary.toFloat(RoundingMode.HALF_EVEN), 1.17549435E-38);
        try {
            justAboveBoundary.toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
    }

    @Test
    public void testToDouble() {
        aeq(Rational.ZERO.toDouble(), 0.0);
        aeq(Rational.ONE.toDouble(), 1.0);
        aeq(Rational.read("1/2").get().toDouble(), 0.5);
        aeq(Rational.read("1/3").get().toDouble(), 0.3333333333333333);
        aeq(Rational.read("-1/3").get().toDouble(), -0.3333333333333333);
        Rational almostOne = Rational.of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
        aeq(almostOne.toDouble(), 1.0);
        Rational pi = Rational.of(Math.PI);
        if (pi == null) {
            fail();
        }
        aeq(pi.toDouble(), 3.141592653589793);
        Rational googol = Rational.of(BigInteger.TEN.pow(100));
        aeq(googol.toDouble(), 1.0E100);
        Rational piSuccessor = Rational.of(Numbers.successor(Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = Rational.of(Numbers.predecessor(Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = Rational.add(pi, piSuccessor).divide(2);
        Rational halfBelowPi = Rational.add(pi, piPredecessor).divide(2);
        Rational justAbovePi = Rational.add(pi.multiply(2), piSuccessor).divide(3);
        Rational justBelowPi = Rational.add(pi.multiply(2), piPredecessor).divide(3);
        aeq(halfAbovePi.toDouble(), 3.141592653589793);
        aeq(halfBelowPi.toDouble(), 3.141592653589793);
        aeq(justAbovePi.toDouble(), 3.141592653589793);
        aeq(justBelowPi.toDouble(), 3.141592653589793);
        Rational subnormal = Rational.of(1e-310);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = Rational.of(Numbers.successor(1e-310));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = Rational.of(Numbers.predecessor(1e-310));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = Rational.add(subnormal, subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = Rational.add(subnormal, subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = Rational.add(subnormal.multiply(2), subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = Rational.add(subnormal.multiply(2), subnormalPredecessor).divide(3);
        aeq(subnormal.toDouble(), 1.0E-310);
        aeq(halfAboveSubnormal.toDouble(), 1.00000000000005E-310);
        aeq(halfBelowSubnormal.toDouble(), 9.9999999999995E-311);
        aeq(justAboveSubnormal.toDouble(), 1.0E-310);
        aeq(justBelowSubnormal.toDouble(), 1.0E-310);
        Rational belowNegativeMax = Rational.subtract(Rational.LARGEST_DOUBLE.negate(), Rational.ONE);
        Rational negativeMaxSuccessor = Rational.of(Numbers.successor(-Double.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = Rational.add(Rational.LARGEST_DOUBLE.negate(), negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = Rational.add(Rational.LARGEST_DOUBLE.negate().multiply(2), negativeMaxSuccessor).divide(3);
        aeq(belowNegativeMax.toDouble(), Double.NEGATIVE_INFINITY);
        aeq(halfAboveNegativeMax.toDouble(), -1.7976931348623155E308);
        aeq(justAboveNegativeMax.toDouble(), -1.7976931348623157E308);
        Rational aboveMax = Rational.add(Rational.LARGEST_DOUBLE, Rational.ONE);
        Rational maxPredecessor = Rational.of(Numbers.predecessor(Double.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = Rational.add(Rational.LARGEST_DOUBLE, maxPredecessor).divide(2);
        Rational justBelowMax = Rational.add(Rational.LARGEST_DOUBLE.multiply(2), maxPredecessor).divide(3);
        aeq(aboveMax.toDouble(), Double.POSITIVE_INFINITY);
        aeq(halfBelowMax.toDouble(), 1.7976931348623155E308);
        aeq(justBelowMax.toDouble(), 1.7976931348623157E308);
        Rational halfAboveZero = Rational.SMALLEST_DOUBLE.shiftRight(1);
        Rational justAboveZero = Rational.SMALLEST_DOUBLE.divide(3);
        Rational halfBelowZero = halfAboveZero.negate();
        Rational justBelowZero = justAboveZero.negate();
        aeq(halfBelowZero.toDouble(), -0.0);
        aeq(justBelowZero.toDouble(), -0.0);
        aeq(halfAboveZero.toDouble(), 0.0);
        aeq(justAboveZero.toDouble(), 0.0);
        Rational boundary = Rational.add(Rational.LARGEST_SUBNORMAL_DOUBLE, Rational.SMALLEST_NORMAL_DOUBLE).shiftRight(1);
        Rational halfBelowBoundary = Rational.add(Rational.LARGEST_SUBNORMAL_DOUBLE, boundary).shiftRight(1);
        Rational halfAboveBoundary = Rational.add(Rational.SMALLEST_NORMAL_DOUBLE, boundary).shiftRight(1);
        Rational justBelowBoundary = Rational.add(Rational.LARGEST_SUBNORMAL_DOUBLE, boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = Rational.add(Rational.SMALLEST_NORMAL_DOUBLE, boundary.shiftLeft(1)).divide(3);
        aeq(boundary.toDouble(), 2.2250738585072014E-308);
        aeq(halfBelowBoundary.toDouble(), 2.225073858507201E-308);
        aeq(justBelowBoundary.toDouble(), 2.225073858507201E-308);
        aeq(halfAboveBoundary.toDouble(), 2.2250738585072014E-308);
        aeq(justAboveBoundary.toDouble(), 2.2250738585072014E-308);
    }

    @Test
    public void testToDoubleRoundingMode() {
        aeq(Rational.ZERO.toDouble(RoundingMode.FLOOR), 0.0);
        aeq(Rational.ZERO.toDouble(RoundingMode.CEILING), 0.0);
        aeq(Rational.ZERO.toDouble(RoundingMode.DOWN), 0.0);
        aeq(Rational.ZERO.toDouble(RoundingMode.UP), 0.0);
        aeq(Rational.ZERO.toDouble(RoundingMode.HALF_DOWN), 0.0);
        aeq(Rational.ZERO.toDouble(RoundingMode.HALF_UP), 0.0);
        aeq(Rational.ZERO.toDouble(RoundingMode.HALF_EVEN), 0.0);
        aeq(Rational.ZERO.toDouble(RoundingMode.UNNECESSARY), 0.0);
        aeq(Rational.ONE.toDouble(RoundingMode.FLOOR), 1.0);
        aeq(Rational.ONE.toDouble(RoundingMode.CEILING), 1.0);
        aeq(Rational.ONE.toDouble(RoundingMode.DOWN), 1.0);
        aeq(Rational.ONE.toDouble(RoundingMode.UP), 1.0);
        aeq(Rational.ONE.toDouble(RoundingMode.HALF_DOWN), 1.0);
        aeq(Rational.ONE.toDouble(RoundingMode.HALF_UP), 1.0);
        aeq(Rational.ONE.toDouble(RoundingMode.HALF_EVEN), 1.0);
        aeq(Rational.ONE.toDouble(RoundingMode.UNNECESSARY), 1.0);
        aeq(Rational.read("1/2").get().toDouble(RoundingMode.FLOOR), 0.5);
        aeq(Rational.read("1/2").get().toDouble(RoundingMode.CEILING), 0.5);
        aeq(Rational.read("1/2").get().toDouble(RoundingMode.DOWN), 0.5);
        aeq(Rational.read("1/2").get().toDouble(RoundingMode.UP), 0.5);
        aeq(Rational.read("1/2").get().toDouble(RoundingMode.HALF_DOWN), 0.5);
        aeq(Rational.read("1/2").get().toDouble(RoundingMode.HALF_UP), 0.5);
        aeq(Rational.read("1/2").get().toDouble(RoundingMode.HALF_EVEN), 0.5);
        aeq(Rational.read("1/2").get().toDouble(RoundingMode.UNNECESSARY), 0.5);
        aeq(Rational.read("1/3").get().toDouble(RoundingMode.FLOOR), 0.3333333333333333);
        aeq(Rational.read("1/3").get().toDouble(RoundingMode.CEILING), 0.33333333333333337);
        aeq(Rational.read("1/3").get().toDouble(RoundingMode.DOWN), 0.3333333333333333);
        aeq(Rational.read("1/3").get().toDouble(RoundingMode.UP), 0.33333333333333337);
        aeq(Rational.read("1/3").get().toDouble(RoundingMode.HALF_DOWN), 0.3333333333333333);
        aeq(Rational.read("1/3").get().toDouble(RoundingMode.HALF_UP), 0.3333333333333333);
        aeq(Rational.read("1/3").get().toDouble(RoundingMode.HALF_EVEN), 0.3333333333333333);
        try {
            Rational.read("1/3").get().toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.read("-1/3").get().toDouble(RoundingMode.FLOOR), -0.33333333333333337);
        aeq(Rational.read("-1/3").get().toDouble(RoundingMode.CEILING), -0.3333333333333333);
        aeq(Rational.read("-1/3").get().toDouble(RoundingMode.DOWN), -0.3333333333333333);
        aeq(Rational.read("-1/3").get().toDouble(RoundingMode.UP), -0.33333333333333337);
        aeq(Rational.read("-1/3").get().toDouble(RoundingMode.HALF_DOWN), -0.3333333333333333);
        aeq(Rational.read("-1/3").get().toDouble(RoundingMode.HALF_UP), -0.3333333333333333);
        aeq(Rational.read("-1/3").get().toDouble(RoundingMode.HALF_EVEN), -0.3333333333333333);
        try {
            Rational.read("-1/3").get().toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational almostOne = Rational.of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
        aeq(almostOne.toDouble(RoundingMode.FLOOR), 0.9999999999999999);
        aeq(almostOne.toDouble(RoundingMode.CEILING), 1.0);
        aeq(almostOne.toDouble(RoundingMode.DOWN), 0.9999999999999999);
        aeq(almostOne.toDouble(RoundingMode.UP), 1.0);
        aeq(almostOne.toDouble(RoundingMode.HALF_DOWN), 1.0);
        aeq(almostOne.toDouble(RoundingMode.HALF_UP), 1.0);
        aeq(almostOne.toDouble(RoundingMode.HALF_EVEN), 1.0);
        try {
            almostOne.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational pi = Rational.of(Math.PI);
        if (pi == null) {
            fail();
        }
        aeq(pi.toDouble(RoundingMode.FLOOR), 3.141592653589793);
        aeq(pi.toDouble(RoundingMode.CEILING), 3.141592653589793);
        aeq(pi.toDouble(RoundingMode.DOWN), 3.141592653589793);
        aeq(pi.toDouble(RoundingMode.UP), 3.141592653589793);
        aeq(pi.toDouble(RoundingMode.HALF_DOWN), 3.141592653589793);
        aeq(pi.toDouble(RoundingMode.HALF_UP), 3.141592653589793);
        aeq(pi.toDouble(RoundingMode.HALF_EVEN), 3.141592653589793);
        aeq(pi.toDouble(RoundingMode.UNNECESSARY), 3.141592653589793);
        Rational googol = Rational.of(BigInteger.TEN.pow(100));
        aeq(googol.toDouble(RoundingMode.FLOOR), 9.999999999999998E99);
        aeq(googol.toDouble(RoundingMode.CEILING), 1.0E100);
        aeq(googol.toDouble(RoundingMode.DOWN), 9.999999999999998E99);
        aeq(googol.toDouble(RoundingMode.UP), 1.0E100);
        aeq(googol.toDouble(RoundingMode.HALF_DOWN), 1.0E100);
        aeq(googol.toDouble(RoundingMode.HALF_UP), 1.0E100);
        aeq(googol.toDouble(RoundingMode.HALF_EVEN), 1.0E100);
        try {
            googol.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational piSuccessor = Rational.of(Numbers.successor(Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = Rational.of(Numbers.predecessor(Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = Rational.add(pi, piSuccessor).divide(2);
        Rational halfBelowPi = Rational.add(pi, piPredecessor).divide(2);
        Rational justAbovePi = Rational.add(pi.multiply(2), piSuccessor).divide(3);
        Rational justBelowPi = Rational.add(pi.multiply(2), piPredecessor).divide(3);
        aeq(halfAbovePi.toDouble(RoundingMode.FLOOR), 3.141592653589793);
        aeq(halfAbovePi.toDouble(RoundingMode.CEILING), 3.1415926535897936);
        aeq(halfAbovePi.toDouble(RoundingMode.DOWN), 3.141592653589793);
        aeq(halfAbovePi.toDouble(RoundingMode.UP), 3.1415926535897936);
        aeq(halfAbovePi.toDouble(RoundingMode.HALF_DOWN), 3.141592653589793);
        aeq(halfAbovePi.toDouble(RoundingMode.HALF_UP), 3.1415926535897936);
        aeq(halfAbovePi.toDouble(RoundingMode.HALF_EVEN), 3.141592653589793);
        try {
            halfAbovePi.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfBelowPi.toDouble(RoundingMode.FLOOR), 3.1415926535897927);
        aeq(halfBelowPi.toDouble(RoundingMode.CEILING), 3.141592653589793);
        aeq(halfBelowPi.toDouble(RoundingMode.DOWN), 3.1415926535897927);
        aeq(halfBelowPi.toDouble(RoundingMode.UP), 3.141592653589793);
        aeq(halfBelowPi.toDouble(RoundingMode.HALF_DOWN), 3.1415926535897927);
        aeq(halfBelowPi.toDouble(RoundingMode.HALF_UP), 3.141592653589793);
        aeq(halfBelowPi.toDouble(RoundingMode.HALF_EVEN), 3.141592653589793);
        try {
            halfBelowPi.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justAbovePi.toDouble(RoundingMode.FLOOR), 3.141592653589793);
        aeq(justAbovePi.toDouble(RoundingMode.CEILING), 3.1415926535897936);
        aeq(justAbovePi.toDouble(RoundingMode.DOWN), 3.141592653589793);
        aeq(justAbovePi.toDouble(RoundingMode.UP), 3.1415926535897936);
        aeq(justAbovePi.toDouble(RoundingMode.HALF_DOWN), 3.141592653589793);
        aeq(justAbovePi.toDouble(RoundingMode.HALF_UP), 3.141592653589793);
        aeq(justAbovePi.toDouble(RoundingMode.HALF_EVEN), 3.141592653589793);
        try {
            justAbovePi.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justBelowPi.toDouble(RoundingMode.FLOOR), 3.1415926535897927);
        aeq(justBelowPi.toDouble(RoundingMode.CEILING), 3.141592653589793);
        aeq(justBelowPi.toDouble(RoundingMode.DOWN), 3.1415926535897927);
        aeq(justBelowPi.toDouble(RoundingMode.UP), 3.141592653589793);
        aeq(justBelowPi.toDouble(RoundingMode.HALF_DOWN), 3.141592653589793);
        aeq(justBelowPi.toDouble(RoundingMode.HALF_UP), 3.141592653589793);
        aeq(justBelowPi.toDouble(RoundingMode.HALF_EVEN), 3.141592653589793);
        try {
            justBelowPi.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational subnormal = Rational.of(1e-310);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = Rational.of(Numbers.successor(1e-310));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = Rational.of(Numbers.predecessor(1e-310));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = Rational.add(subnormal, subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = Rational.add(subnormal, subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = Rational.add(subnormal.multiply(2), subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = Rational.add(subnormal.multiply(2), subnormalPredecessor).divide(3);
        aeq(subnormal.toDouble(RoundingMode.FLOOR), 1.0E-310);
        aeq(subnormal.toDouble(RoundingMode.CEILING), 1.0E-310);
        aeq(subnormal.toDouble(RoundingMode.DOWN), 1.0E-310);
        aeq(subnormal.toDouble(RoundingMode.UP), 1.0E-310);
        aeq(subnormal.toDouble(RoundingMode.HALF_DOWN), 1.0E-310);
        aeq(subnormal.toDouble(RoundingMode.HALF_UP), 1.0E-310);
        aeq(subnormal.toDouble(RoundingMode.HALF_EVEN), 1.0E-310);
        aeq(subnormal.toDouble(RoundingMode.UNNECESSARY), 1.0E-310);
        aeq(halfAboveSubnormal.toDouble(RoundingMode.FLOOR), 1.0E-310);
        aeq(halfAboveSubnormal.toDouble(RoundingMode.CEILING), 1.00000000000005E-310);
        aeq(halfAboveSubnormal.toDouble(RoundingMode.DOWN), 1.0E-310);
        aeq(halfAboveSubnormal.toDouble(RoundingMode.UP), 1.00000000000005E-310);
        aeq(halfAboveSubnormal.toDouble(RoundingMode.HALF_DOWN), 1.0E-310);
        aeq(halfAboveSubnormal.toDouble(RoundingMode.HALF_UP), 1.00000000000005E-310);
        aeq(halfAboveSubnormal.toDouble(RoundingMode.HALF_EVEN), 1.00000000000005E-310);
        try {
            halfAboveSubnormal.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfBelowSubnormal.toDouble(RoundingMode.FLOOR), 9.9999999999995E-311);
        aeq(halfBelowSubnormal.toDouble(RoundingMode.CEILING), 1.0E-310);
        aeq(halfBelowSubnormal.toDouble(RoundingMode.DOWN), 9.9999999999995E-311);
        aeq(halfBelowSubnormal.toDouble(RoundingMode.UP), 1.0E-310);
        aeq(halfBelowSubnormal.toDouble(RoundingMode.HALF_DOWN), 9.9999999999995E-311);
        aeq(halfBelowSubnormal.toDouble(RoundingMode.HALF_UP), 1.0E-310);
        aeq(halfBelowSubnormal.toDouble(RoundingMode.HALF_EVEN), 9.9999999999995E-311);
        try {
            halfBelowSubnormal.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justAboveSubnormal.toDouble(RoundingMode.FLOOR), 1.0E-310);
        aeq(justAboveSubnormal.toDouble(RoundingMode.CEILING), 1.00000000000005E-310);
        aeq(justAboveSubnormal.toDouble(RoundingMode.DOWN), 1.0E-310);
        aeq(justAboveSubnormal.toDouble(RoundingMode.UP), 1.00000000000005E-310);
        aeq(justAboveSubnormal.toDouble(RoundingMode.HALF_DOWN), 1.0E-310);
        aeq(justAboveSubnormal.toDouble(RoundingMode.HALF_UP), 1.0E-310);
        aeq(justAboveSubnormal.toDouble(RoundingMode.HALF_EVEN), 1.0E-310);
        try {
            justAboveSubnormal.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justBelowSubnormal.toDouble(RoundingMode.FLOOR), 9.9999999999995E-311);
        aeq(justBelowSubnormal.toDouble(RoundingMode.CEILING), 1.0E-310);
        aeq(justBelowSubnormal.toDouble(RoundingMode.DOWN), 9.9999999999995E-311);
        aeq(justBelowSubnormal.toDouble(RoundingMode.UP), 1.0E-310);
        aeq(justBelowSubnormal.toDouble(RoundingMode.HALF_DOWN), 1.0E-310);
        aeq(justBelowSubnormal.toDouble(RoundingMode.HALF_UP), 1.0E-310);
        aeq(justBelowSubnormal.toDouble(RoundingMode.HALF_EVEN), 1.0E-310);
        try {
            justBelowSubnormal.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational belowNegativeMax = Rational.subtract(Rational.LARGEST_DOUBLE.negate(), Rational.ONE);
        Rational negativeMaxSuccessor = Rational.of(Numbers.successor(-Double.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = Rational.add(Rational.LARGEST_DOUBLE.negate(), negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = Rational.add(Rational.LARGEST_DOUBLE.negate().multiply(2), negativeMaxSuccessor).divide(3);
        aeq(belowNegativeMax.toDouble(RoundingMode.FLOOR), Double.NEGATIVE_INFINITY);
        aeq(belowNegativeMax.toDouble(RoundingMode.CEILING), -1.7976931348623157E308);
        aeq(belowNegativeMax.toDouble(RoundingMode.DOWN), -1.7976931348623157E308);
        aeq(belowNegativeMax.toDouble(RoundingMode.UP), Double.NEGATIVE_INFINITY);
        aeq(belowNegativeMax.toDouble(RoundingMode.HALF_DOWN), -1.7976931348623157E308);
        aeq(belowNegativeMax.toDouble(RoundingMode.HALF_UP), Double.NEGATIVE_INFINITY);
        aeq(belowNegativeMax.toDouble(RoundingMode.HALF_EVEN), Double.NEGATIVE_INFINITY);
        try {
            belowNegativeMax.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfAboveNegativeMax.toDouble(RoundingMode.FLOOR), -1.7976931348623157E308);
        aeq(halfAboveNegativeMax.toDouble(RoundingMode.CEILING), -1.7976931348623155E308);
        aeq(halfAboveNegativeMax.toDouble(RoundingMode.DOWN), -1.7976931348623155E308);
        aeq(halfAboveNegativeMax.toDouble(RoundingMode.UP), -1.7976931348623157E308);
        aeq(halfAboveNegativeMax.toDouble(RoundingMode.HALF_DOWN), -1.7976931348623155E308);
        aeq(halfAboveNegativeMax.toDouble(RoundingMode.HALF_UP), -1.7976931348623157E308);
        aeq(halfAboveNegativeMax.toDouble(RoundingMode.HALF_EVEN), -1.7976931348623155E308);
        try {
            halfAboveNegativeMax.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justAboveNegativeMax.toDouble(RoundingMode.FLOOR), -1.7976931348623157E308);
        aeq(justAboveNegativeMax.toDouble(RoundingMode.CEILING), -1.7976931348623155E308);
        aeq(justAboveNegativeMax.toDouble(RoundingMode.DOWN), -1.7976931348623155E308);
        aeq(justAboveNegativeMax.toDouble(RoundingMode.UP), -1.7976931348623157E308);
        aeq(justAboveNegativeMax.toDouble(RoundingMode.HALF_DOWN), -1.7976931348623157E308);
        aeq(justAboveNegativeMax.toDouble(RoundingMode.HALF_UP), -1.7976931348623157E308);
        aeq(justAboveNegativeMax.toDouble(RoundingMode.HALF_EVEN), -1.7976931348623157E308);
        try {
            justAboveNegativeMax.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational aboveMax = Rational.add(Rational.LARGEST_DOUBLE, Rational.ONE);
        Rational maxPredecessor = Rational.of(Numbers.predecessor(Double.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = Rational.add(Rational.LARGEST_DOUBLE, maxPredecessor).divide(2);
        Rational justBelowMax = Rational.add(Rational.LARGEST_DOUBLE.multiply(2), maxPredecessor).divide(3);
        aeq(aboveMax.toDouble(RoundingMode.FLOOR), 1.7976931348623157E308);
        aeq(aboveMax.toDouble(RoundingMode.CEILING), Double.POSITIVE_INFINITY);
        aeq(aboveMax.toDouble(RoundingMode.DOWN), 1.7976931348623157E308);
        aeq(aboveMax.toDouble(RoundingMode.UP), Double.POSITIVE_INFINITY);
        aeq(aboveMax.toDouble(RoundingMode.HALF_DOWN), 1.7976931348623157E308);
        aeq(aboveMax.toDouble(RoundingMode.HALF_UP), Double.POSITIVE_INFINITY);
        aeq(aboveMax.toDouble(RoundingMode.HALF_EVEN), Double.POSITIVE_INFINITY);
        try {
            aboveMax.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfBelowMax.toDouble(RoundingMode.FLOOR), 1.7976931348623155E308);
        aeq(halfBelowMax.toDouble(RoundingMode.CEILING), 1.7976931348623157E308);
        aeq(halfBelowMax.toDouble(RoundingMode.DOWN), 1.7976931348623155E308);
        aeq(halfBelowMax.toDouble(RoundingMode.UP), 1.7976931348623157E308);
        aeq(halfBelowMax.toDouble(RoundingMode.HALF_DOWN), 1.7976931348623155E308);
        aeq(halfBelowMax.toDouble(RoundingMode.HALF_UP), 1.7976931348623157E308);
        aeq(halfBelowMax.toDouble(RoundingMode.HALF_EVEN), 1.7976931348623155E308);
        try {
            halfBelowMax.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justBelowMax.toDouble(RoundingMode.FLOOR), 1.7976931348623155E308);
        aeq(justBelowMax.toDouble(RoundingMode.CEILING), 1.7976931348623157E308);
        aeq(justBelowMax.toDouble(RoundingMode.DOWN), 1.7976931348623155E308);
        aeq(justBelowMax.toDouble(RoundingMode.UP), 1.7976931348623157E308);
        aeq(justBelowMax.toDouble(RoundingMode.HALF_DOWN), 1.7976931348623157E308);
        aeq(justBelowMax.toDouble(RoundingMode.HALF_UP), 1.7976931348623157E308);
        aeq(justBelowMax.toDouble(RoundingMode.HALF_EVEN), 1.7976931348623157E308);
        try {
            justBelowMax.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational halfAboveZero = Rational.SMALLEST_DOUBLE.shiftRight(1);
        Rational justAboveZero = Rational.SMALLEST_DOUBLE.divide(3);
        Rational halfBelowZero = halfAboveZero.negate();
        Rational justBelowZero = justAboveZero.negate();
        aeq(halfBelowZero.toDouble(RoundingMode.FLOOR), -4.9E-324);
        aeq(halfBelowZero.toDouble(RoundingMode.CEILING), -0.0);
        aeq(halfBelowZero.toDouble(RoundingMode.DOWN), -0.0);
        aeq(halfBelowZero.toDouble(RoundingMode.UP), -4.9E-324);
        aeq(halfBelowZero.toDouble(RoundingMode.HALF_DOWN), -0.0);
        aeq(halfBelowZero.toDouble(RoundingMode.HALF_UP), -4.9E-324);
        aeq(halfBelowZero.toDouble(RoundingMode.HALF_EVEN), -0.0);
        try {
            halfBelowZero.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justBelowZero.toDouble(RoundingMode.FLOOR), -4.9E-324);
        aeq(justBelowZero.toDouble(RoundingMode.CEILING), -0.0);
        aeq(justBelowZero.toDouble(RoundingMode.DOWN), -0.0);
        aeq(justBelowZero.toDouble(RoundingMode.UP), -4.9E-324);
        aeq(justBelowZero.toDouble(RoundingMode.HALF_DOWN), -0.0);
        aeq(justBelowZero.toDouble(RoundingMode.HALF_UP), -0.0);
        aeq(justBelowZero.toDouble(RoundingMode.HALF_EVEN), -0.0);
        try {
            justBelowZero.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfAboveZero.toDouble(RoundingMode.FLOOR), 0.0);
        aeq(halfAboveZero.toDouble(RoundingMode.CEILING), 4.9E-324);
        aeq(halfAboveZero.toDouble(RoundingMode.DOWN), 0.0);
        aeq(halfAboveZero.toDouble(RoundingMode.UP), 4.9E-324);
        aeq(halfAboveZero.toDouble(RoundingMode.HALF_DOWN), 0.0);
        aeq(halfAboveZero.toDouble(RoundingMode.HALF_UP), 4.9E-324);
        aeq(halfAboveZero.toDouble(RoundingMode.HALF_EVEN), 0.0);
        try {
            halfAboveZero.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justAboveZero.toDouble(RoundingMode.FLOOR), 0.0);
        aeq(justAboveZero.toDouble(RoundingMode.CEILING), 4.9E-324);
        aeq(justAboveZero.toDouble(RoundingMode.DOWN), 0.0);
        aeq(justAboveZero.toDouble(RoundingMode.UP), 4.9E-324);
        aeq(justAboveZero.toDouble(RoundingMode.HALF_DOWN), 0.0);
        aeq(justAboveZero.toDouble(RoundingMode.HALF_UP), 0.0);
        aeq(justAboveZero.toDouble(RoundingMode.HALF_EVEN), 0.0);
        try {
            justAboveZero.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        Rational boundary = Rational.add(Rational.LARGEST_SUBNORMAL_DOUBLE, Rational.SMALLEST_NORMAL_DOUBLE).shiftRight(1);
        Rational halfBelowBoundary = Rational.add(Rational.LARGEST_SUBNORMAL_DOUBLE, boundary).shiftRight(1);
        Rational halfAboveBoundary = Rational.add(Rational.SMALLEST_NORMAL_DOUBLE, boundary).shiftRight(1);
        Rational justBelowBoundary = Rational.add(Rational.LARGEST_SUBNORMAL_DOUBLE, boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = Rational.add(Rational.SMALLEST_NORMAL_DOUBLE, boundary.shiftLeft(1)).divide(3);
        aeq(boundary.toDouble(RoundingMode.FLOOR), 2.225073858507201E-308);
        aeq(boundary.toDouble(RoundingMode.CEILING), 2.2250738585072014E-308);
        aeq(boundary.toDouble(RoundingMode.DOWN), 2.225073858507201E-308);
        aeq(boundary.toDouble(RoundingMode.UP), 2.2250738585072014E-308);
        aeq(boundary.toDouble(RoundingMode.HALF_DOWN), 2.225073858507201E-308);
        aeq(boundary.toDouble(RoundingMode.HALF_UP), 2.2250738585072014E-308);
        aeq(boundary.toDouble(RoundingMode.HALF_EVEN), 2.2250738585072014E-308);
        try {
            boundary.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfBelowBoundary.toDouble(RoundingMode.FLOOR), 2.225073858507201E-308);
        aeq(halfBelowBoundary.toDouble(RoundingMode.CEILING), 2.2250738585072014E-308);
        aeq(halfBelowBoundary.toDouble(RoundingMode.DOWN), 2.225073858507201E-308);
        aeq(halfBelowBoundary.toDouble(RoundingMode.UP), 2.2250738585072014E-308);
        aeq(halfBelowBoundary.toDouble(RoundingMode.HALF_DOWN), 2.225073858507201E-308);
        aeq(halfBelowBoundary.toDouble(RoundingMode.HALF_UP), 2.225073858507201E-308);
        aeq(halfBelowBoundary.toDouble(RoundingMode.HALF_EVEN), 2.225073858507201E-308);
        try {
            halfBelowBoundary.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justBelowBoundary.toDouble(RoundingMode.FLOOR), 2.225073858507201E-308);
        aeq(justBelowBoundary.toDouble(RoundingMode.CEILING), 2.2250738585072014E-308);
        aeq(justBelowBoundary.toDouble(RoundingMode.DOWN), 2.225073858507201E-308);
        aeq(justBelowBoundary.toDouble(RoundingMode.UP), 2.2250738585072014E-308);
        aeq(justBelowBoundary.toDouble(RoundingMode.HALF_DOWN), 2.225073858507201E-308);
        aeq(justBelowBoundary.toDouble(RoundingMode.HALF_UP), 2.225073858507201E-308);
        aeq(justBelowBoundary.toDouble(RoundingMode.HALF_EVEN), 2.225073858507201E-308);
        try {
            justBelowBoundary.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(halfAboveBoundary.toDouble(RoundingMode.FLOOR), 2.225073858507201E-308);
        aeq(halfAboveBoundary.toDouble(RoundingMode.CEILING), 2.2250738585072014E-308);
        aeq(halfAboveBoundary.toDouble(RoundingMode.DOWN), 2.225073858507201E-308);
        aeq(halfAboveBoundary.toDouble(RoundingMode.UP), 2.2250738585072014E-308);
        aeq(halfAboveBoundary.toDouble(RoundingMode.HALF_DOWN), 2.2250738585072014E-308);
        aeq(halfAboveBoundary.toDouble(RoundingMode.HALF_UP), 2.2250738585072014E-308);
        aeq(halfAboveBoundary.toDouble(RoundingMode.HALF_EVEN), 2.2250738585072014E-308);
        try {
            halfAboveBoundary.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(justAboveBoundary.toDouble(RoundingMode.FLOOR), 2.225073858507201E-308);
        aeq(justAboveBoundary.toDouble(RoundingMode.CEILING), 2.2250738585072014E-308);
        aeq(justAboveBoundary.toDouble(RoundingMode.DOWN), 2.225073858507201E-308);
        aeq(justAboveBoundary.toDouble(RoundingMode.UP), 2.2250738585072014E-308);
        aeq(justAboveBoundary.toDouble(RoundingMode.HALF_DOWN), 2.2250738585072014E-308);
        aeq(justAboveBoundary.toDouble(RoundingMode.HALF_UP), 2.2250738585072014E-308);
        aeq(justAboveBoundary.toDouble(RoundingMode.HALF_EVEN), 2.2250738585072014E-308);
        try {
            justAboveBoundary.toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
    }

    @Test
    public void hasTerminatingDecimalExpansionTest() {
        assertTrue(Rational.ZERO.hasTerminatingDecimalExpansion());
        assertTrue(Rational.ONE.hasTerminatingDecimalExpansion());
        assertTrue(Rational.read("60").get().hasTerminatingDecimalExpansion());
        assertTrue(Rational.read("1/2").get().hasTerminatingDecimalExpansion());
        assertTrue(Rational.read("1/5").get().hasTerminatingDecimalExpansion());
        assertTrue(Rational.read("-7/100").get().hasTerminatingDecimalExpansion());
        assertTrue(Rational.read("-3/640").get().hasTerminatingDecimalExpansion());
        assertFalse(Rational.read("1/3").get().hasTerminatingDecimalExpansion());
        assertFalse(Rational.read("-1/15").get().hasTerminatingDecimalExpansion());
        assertFalse(Rational.read("-2/9").get().hasTerminatingDecimalExpansion());
    }

    @Test
    public void numberOfDecimalDigitsTest() {
        aeq(Rational.ZERO.numberOfDecimalDigits(), 0);
        aeq(Rational.ONE.numberOfDecimalDigits(), 1);
        aeq(Rational.read("5").get().numberOfDecimalDigits(), 1);
        aeq(Rational.read("125").get().numberOfDecimalDigits(), 3);
        aeq(Rational.read("-125").get().numberOfDecimalDigits(), 3);
        aeq(Rational.read("3/5").get().numberOfDecimalDigits(), 1);
        aeq(Rational.read("-3/2").get().numberOfDecimalDigits(), 2);
        aeq(Rational.read("12000").get().numberOfDecimalDigits(), 2);
        aeq(Rational.read("7/1000").get().numberOfDecimalDigits(), 1);
    }

    @Test
    public void toBigDecimalTest() {
        aeq(Rational.ZERO.toBigDecimal(), "0");
        aeq(Rational.ONE.toBigDecimal(), "1");
        aeq(Rational.read("-23").get().toBigDecimal(), "-23");
        aeq(Rational.read("4/5").get().toBigDecimal(), "0.8");
        aeq(Rational.read("1/64").get().toBigDecimal(), "0.015625");
        aeq(Rational.read("1234").get().toBigDecimal(), "1234");
        try {
            Rational.read("1/3").get().toBigDecimal();
            fail();
        } catch (ArithmeticException e) {}
    }

    @Test
    public void toBigDecimalPrecisionTest() {
        aeq(Rational.ZERO.toBigDecimal(4), "0");
        aeq(Rational.ZERO.toBigDecimal(0), "0");
        aeq(Rational.ONE.toBigDecimal(4), "1");
        aeq(Rational.ONE.toBigDecimal(0), "1");
        aeq(Rational.read("1/2").get().toBigDecimal(0), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(1), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(4), "0.5");
        aeq(Rational.read("1/64").get().toBigDecimal(0), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(1), "0.02");
        aeq(Rational.read("1/64").get().toBigDecimal(2), "0.016");
        aeq(Rational.read("1/64").get().toBigDecimal(3), "0.0156");
        aeq(Rational.read("1/64").get().toBigDecimal(4), "0.01563");
        aeq(Rational.read("1/64").get().toBigDecimal(5), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(6), "0.015625");
        aeq(Rational.read("-1/3").get().toBigDecimal(1), "-0.3");
        aeq(Rational.read("-1/3").get().toBigDecimal(2), "-0.33");
        aeq(Rational.read("-1/3").get().toBigDecimal(3), "-0.333");
        aeq(Rational.read("-1/3").get().toBigDecimal(4), "-0.3333");
        aeq(Rational.read("6789").get().toBigDecimal(0), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(1), "7E+3");
        aeq(Rational.read("6789").get().toBigDecimal(2), "6.8E+3");
        aeq(Rational.read("6789").get().toBigDecimal(3), "6.79E+3");
        aeq(Rational.read("6789").get().toBigDecimal(4), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(5), "6789");
        try {
            Rational.read("-1/3").get().toBigDecimal(0);
            fail();
        } catch (ArithmeticException e) {}
        try {
            Rational.read("5").get().toBigDecimal(-1);
            fail();
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void toBigDecimalPrecisionRoundingModeTest() {
        aeq(Rational.ZERO.toBigDecimal(0, RoundingMode.UNNECESSARY), "0");
        aeq(Rational.ZERO.toBigDecimal(0, RoundingMode.FLOOR), "0");
        aeq(Rational.ZERO.toBigDecimal(0, RoundingMode.CEILING), "0");
        aeq(Rational.ZERO.toBigDecimal(0, RoundingMode.DOWN), "0");
        aeq(Rational.ZERO.toBigDecimal(0, RoundingMode.UP), "0");
        aeq(Rational.ZERO.toBigDecimal(0, RoundingMode.HALF_DOWN), "0");
        aeq(Rational.ZERO.toBigDecimal(0, RoundingMode.HALF_UP), "0");
        aeq(Rational.ZERO.toBigDecimal(0, RoundingMode.HALF_EVEN), "0");
        aeq(Rational.ZERO.toBigDecimal(4, RoundingMode.UNNECESSARY), "0");
        aeq(Rational.ZERO.toBigDecimal(4, RoundingMode.FLOOR), "0");
        aeq(Rational.ZERO.toBigDecimal(4, RoundingMode.CEILING), "0");
        aeq(Rational.ZERO.toBigDecimal(4, RoundingMode.DOWN), "0");
        aeq(Rational.ZERO.toBigDecimal(4, RoundingMode.UP), "0");
        aeq(Rational.ZERO.toBigDecimal(4, RoundingMode.HALF_DOWN), "0");
        aeq(Rational.ZERO.toBigDecimal(4, RoundingMode.HALF_UP), "0");
        aeq(Rational.ZERO.toBigDecimal(4, RoundingMode.HALF_EVEN), "0");
        aeq(Rational.ONE.toBigDecimal(0, RoundingMode.UNNECESSARY), "1");
        aeq(Rational.ONE.toBigDecimal(0, RoundingMode.FLOOR), "1");
        aeq(Rational.ONE.toBigDecimal(0, RoundingMode.CEILING), "1");
        aeq(Rational.ONE.toBigDecimal(0, RoundingMode.DOWN), "1");
        aeq(Rational.ONE.toBigDecimal(0, RoundingMode.UP), "1");
        aeq(Rational.ONE.toBigDecimal(0, RoundingMode.HALF_DOWN), "1");
        aeq(Rational.ONE.toBigDecimal(0, RoundingMode.HALF_UP), "1");
        aeq(Rational.ONE.toBigDecimal(0, RoundingMode.HALF_EVEN), "1");
        aeq(Rational.ONE.toBigDecimal(4, RoundingMode.UNNECESSARY), "1");
        aeq(Rational.ONE.toBigDecimal(4, RoundingMode.FLOOR), "1");
        aeq(Rational.ONE.toBigDecimal(4, RoundingMode.CEILING), "1");
        aeq(Rational.ONE.toBigDecimal(4, RoundingMode.DOWN), "1");
        aeq(Rational.ONE.toBigDecimal(4, RoundingMode.UP), "1");
        aeq(Rational.ONE.toBigDecimal(4, RoundingMode.HALF_DOWN), "1");
        aeq(Rational.ONE.toBigDecimal(4, RoundingMode.HALF_UP), "1");
        aeq(Rational.ONE.toBigDecimal(4, RoundingMode.HALF_EVEN), "1");
        aeq(Rational.read("1/2").get().toBigDecimal(0, RoundingMode.UNNECESSARY), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(0, RoundingMode.FLOOR), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(0, RoundingMode.CEILING), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(0, RoundingMode.DOWN), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(0, RoundingMode.UP), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(0, RoundingMode.HALF_DOWN), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(0, RoundingMode.HALF_UP), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(0, RoundingMode.HALF_EVEN), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(1, RoundingMode.UNNECESSARY), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(1, RoundingMode.FLOOR), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(1, RoundingMode.CEILING), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(1, RoundingMode.DOWN), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(1, RoundingMode.UP), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(1, RoundingMode.HALF_DOWN), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(1, RoundingMode.HALF_UP), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(1, RoundingMode.HALF_EVEN), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(4, RoundingMode.UNNECESSARY), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(4, RoundingMode.FLOOR), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(4, RoundingMode.CEILING), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(4, RoundingMode.DOWN), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(4, RoundingMode.UP), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(4, RoundingMode.HALF_DOWN), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(4, RoundingMode.HALF_UP), "0.5");
        aeq(Rational.read("1/2").get().toBigDecimal(4, RoundingMode.HALF_EVEN), "0.5");
        aeq(Rational.read("1/64").get().toBigDecimal(0, RoundingMode.UNNECESSARY), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(0, RoundingMode.FLOOR), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(0, RoundingMode.CEILING), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(0, RoundingMode.DOWN), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(0, RoundingMode.UP), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(0, RoundingMode.HALF_DOWN), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(0, RoundingMode.HALF_UP), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(0, RoundingMode.HALF_EVEN), "0.015625");
        try {
            Rational.read("1/64").get().toBigDecimal(1, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.read("1/64").get().toBigDecimal(1, RoundingMode.FLOOR), "0.01");
        aeq(Rational.read("1/64").get().toBigDecimal(1, RoundingMode.CEILING), "0.02");
        aeq(Rational.read("1/64").get().toBigDecimal(1, RoundingMode.DOWN), "0.01");
        aeq(Rational.read("1/64").get().toBigDecimal(1, RoundingMode.UP), "0.02");
        aeq(Rational.read("1/64").get().toBigDecimal(1, RoundingMode.HALF_DOWN), "0.02");
        aeq(Rational.read("1/64").get().toBigDecimal(1, RoundingMode.HALF_UP), "0.02");
        aeq(Rational.read("1/64").get().toBigDecimal(1, RoundingMode.HALF_EVEN), "0.02");
        try {
            Rational.read("1/64").get().toBigDecimal(2, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.read("1/64").get().toBigDecimal(2, RoundingMode.FLOOR), "0.015");
        aeq(Rational.read("1/64").get().toBigDecimal(2, RoundingMode.CEILING), "0.016");
        aeq(Rational.read("1/64").get().toBigDecimal(2, RoundingMode.DOWN), "0.015");
        aeq(Rational.read("1/64").get().toBigDecimal(2, RoundingMode.UP), "0.016");
        aeq(Rational.read("1/64").get().toBigDecimal(2, RoundingMode.HALF_DOWN), "0.016");
        aeq(Rational.read("1/64").get().toBigDecimal(2, RoundingMode.HALF_UP), "0.016");
        aeq(Rational.read("1/64").get().toBigDecimal(2, RoundingMode.HALF_EVEN), "0.016");
        try {
            Rational.read("1/64").get().toBigDecimal(3, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.read("1/64").get().toBigDecimal(3, RoundingMode.FLOOR), "0.0156");
        aeq(Rational.read("1/64").get().toBigDecimal(3, RoundingMode.CEILING), "0.0157");
        aeq(Rational.read("1/64").get().toBigDecimal(3, RoundingMode.DOWN), "0.0156");
        aeq(Rational.read("1/64").get().toBigDecimal(3, RoundingMode.UP), "0.0157");
        aeq(Rational.read("1/64").get().toBigDecimal(3, RoundingMode.HALF_DOWN), "0.0156");
        aeq(Rational.read("1/64").get().toBigDecimal(3, RoundingMode.HALF_UP), "0.0156");
        aeq(Rational.read("1/64").get().toBigDecimal(3, RoundingMode.HALF_EVEN), "0.0156");
        try {
            Rational.read("1/64").get().toBigDecimal(4, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.read("1/64").get().toBigDecimal(4, RoundingMode.FLOOR), "0.01562");
        aeq(Rational.read("1/64").get().toBigDecimal(4, RoundingMode.CEILING), "0.01563");
        aeq(Rational.read("1/64").get().toBigDecimal(4, RoundingMode.DOWN), "0.01562");
        aeq(Rational.read("1/64").get().toBigDecimal(4, RoundingMode.UP), "0.01563");
        aeq(Rational.read("1/64").get().toBigDecimal(4, RoundingMode.HALF_DOWN), "0.01562");
        aeq(Rational.read("1/64").get().toBigDecimal(4, RoundingMode.HALF_UP), "0.01563");
        aeq(Rational.read("1/64").get().toBigDecimal(4, RoundingMode.HALF_EVEN), "0.01562");
        aeq(Rational.read("1/64").get().toBigDecimal(5, RoundingMode.UNNECESSARY), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(5, RoundingMode.FLOOR), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(5, RoundingMode.CEILING), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(5, RoundingMode.DOWN), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(5, RoundingMode.UP), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(5, RoundingMode.HALF_DOWN), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(5, RoundingMode.HALF_UP), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(5, RoundingMode.HALF_EVEN), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(6, RoundingMode.UNNECESSARY), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(6, RoundingMode.FLOOR), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(6, RoundingMode.CEILING), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(6, RoundingMode.DOWN), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(6, RoundingMode.UP), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(6, RoundingMode.HALF_DOWN), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(6, RoundingMode.HALF_UP), "0.015625");
        aeq(Rational.read("1/64").get().toBigDecimal(6, RoundingMode.HALF_EVEN), "0.015625");
        try {
            Rational.read("-1/3").get().toBigDecimal(1, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.read("-1/3").get().toBigDecimal(1, RoundingMode.FLOOR), "-0.4");
        aeq(Rational.read("-1/3").get().toBigDecimal(1, RoundingMode.CEILING), "-0.3");
        aeq(Rational.read("-1/3").get().toBigDecimal(1, RoundingMode.DOWN), "-0.3");
        aeq(Rational.read("-1/3").get().toBigDecimal(1, RoundingMode.UP), "-0.4");
        aeq(Rational.read("-1/3").get().toBigDecimal(1, RoundingMode.HALF_DOWN), "-0.3");
        aeq(Rational.read("-1/3").get().toBigDecimal(1, RoundingMode.HALF_UP), "-0.3");
        aeq(Rational.read("-1/3").get().toBigDecimal(1, RoundingMode.HALF_EVEN), "-0.3");
        try {
            Rational.read("-1/3").get().toBigDecimal(2, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.read("-1/3").get().toBigDecimal(2, RoundingMode.FLOOR), "-0.34");
        aeq(Rational.read("-1/3").get().toBigDecimal(2, RoundingMode.CEILING), "-0.33");
        aeq(Rational.read("-1/3").get().toBigDecimal(2, RoundingMode.DOWN), "-0.33");
        aeq(Rational.read("-1/3").get().toBigDecimal(2, RoundingMode.UP), "-0.34");
        aeq(Rational.read("-1/3").get().toBigDecimal(2, RoundingMode.HALF_DOWN), "-0.33");
        aeq(Rational.read("-1/3").get().toBigDecimal(2, RoundingMode.HALF_UP), "-0.33");
        aeq(Rational.read("-1/3").get().toBigDecimal(2, RoundingMode.HALF_EVEN), "-0.33");
        try {
            Rational.read("-1/3").get().toBigDecimal(3, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.read("-1/3").get().toBigDecimal(3, RoundingMode.FLOOR), "-0.334");
        aeq(Rational.read("-1/3").get().toBigDecimal(3, RoundingMode.CEILING), "-0.333");
        aeq(Rational.read("-1/3").get().toBigDecimal(3, RoundingMode.DOWN), "-0.333");
        aeq(Rational.read("-1/3").get().toBigDecimal(3, RoundingMode.UP), "-0.334");
        aeq(Rational.read("-1/3").get().toBigDecimal(3, RoundingMode.HALF_DOWN), "-0.333");
        aeq(Rational.read("-1/3").get().toBigDecimal(3, RoundingMode.HALF_UP), "-0.333");
        aeq(Rational.read("-1/3").get().toBigDecimal(3, RoundingMode.HALF_EVEN), "-0.333");
        try {
            Rational.read("-1/3").get().toBigDecimal(4, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.read("-1/3").get().toBigDecimal(4, RoundingMode.FLOOR), "-0.3334");
        aeq(Rational.read("-1/3").get().toBigDecimal(4, RoundingMode.CEILING), "-0.3333");
        aeq(Rational.read("-1/3").get().toBigDecimal(4, RoundingMode.DOWN), "-0.3333");
        aeq(Rational.read("-1/3").get().toBigDecimal(4, RoundingMode.UP), "-0.3334");
        aeq(Rational.read("-1/3").get().toBigDecimal(4, RoundingMode.HALF_DOWN), "-0.3333");
        aeq(Rational.read("-1/3").get().toBigDecimal(4, RoundingMode.HALF_UP), "-0.3333");
        aeq(Rational.read("-1/3").get().toBigDecimal(4, RoundingMode.HALF_EVEN), "-0.3333");
        aeq(Rational.read("6789").get().toBigDecimal(0, RoundingMode.FLOOR), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(0, RoundingMode.CEILING), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(0, RoundingMode.DOWN), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(0, RoundingMode.UP), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(0, RoundingMode.HALF_DOWN), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(0, RoundingMode.HALF_UP), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(0, RoundingMode.HALF_EVEN), "6789");
        try {
            Rational.read("6789").get().toBigDecimal(1, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.read("6789").get().toBigDecimal(1, RoundingMode.FLOOR), "6E+3");
        aeq(Rational.read("6789").get().toBigDecimal(1, RoundingMode.CEILING), "7E+3");
        aeq(Rational.read("6789").get().toBigDecimal(1, RoundingMode.DOWN), "6E+3");
        aeq(Rational.read("6789").get().toBigDecimal(1, RoundingMode.UP), "7E+3");
        aeq(Rational.read("6789").get().toBigDecimal(1, RoundingMode.HALF_DOWN), "7E+3");
        aeq(Rational.read("6789").get().toBigDecimal(1, RoundingMode.HALF_UP), "7E+3");
        aeq(Rational.read("6789").get().toBigDecimal(1, RoundingMode.HALF_EVEN), "7E+3");
        try {
            Rational.read("6789").get().toBigDecimal(2, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.read("6789").get().toBigDecimal(2, RoundingMode.FLOOR), "6.7E+3");
        aeq(Rational.read("6789").get().toBigDecimal(2, RoundingMode.CEILING), "6.8E+3");
        aeq(Rational.read("6789").get().toBigDecimal(2, RoundingMode.DOWN), "6.7E+3");
        aeq(Rational.read("6789").get().toBigDecimal(2, RoundingMode.UP), "6.8E+3");
        aeq(Rational.read("6789").get().toBigDecimal(2, RoundingMode.HALF_DOWN), "6.8E+3");
        aeq(Rational.read("6789").get().toBigDecimal(2, RoundingMode.HALF_UP), "6.8E+3");
        aeq(Rational.read("6789").get().toBigDecimal(2, RoundingMode.HALF_EVEN), "6.8E+3");
        try {
            Rational.read("6789").get().toBigDecimal(3, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException e) {}
        aeq(Rational.read("6789").get().toBigDecimal(3, RoundingMode.FLOOR), "6.78E+3");
        aeq(Rational.read("6789").get().toBigDecimal(3, RoundingMode.CEILING), "6.79E+3");
        aeq(Rational.read("6789").get().toBigDecimal(3, RoundingMode.DOWN), "6.78E+3");
        aeq(Rational.read("6789").get().toBigDecimal(3, RoundingMode.UP), "6.79E+3");
        aeq(Rational.read("6789").get().toBigDecimal(3, RoundingMode.HALF_DOWN), "6.79E+3");
        aeq(Rational.read("6789").get().toBigDecimal(3, RoundingMode.HALF_UP), "6.79E+3");
        aeq(Rational.read("6789").get().toBigDecimal(3, RoundingMode.HALF_EVEN), "6.79E+3");
        aeq(Rational.read("6789").get().toBigDecimal(4, RoundingMode.UNNECESSARY), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(4, RoundingMode.FLOOR), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(4, RoundingMode.CEILING), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(4, RoundingMode.DOWN), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(4, RoundingMode.UP), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(4, RoundingMode.HALF_DOWN), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(4, RoundingMode.HALF_UP), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(4, RoundingMode.HALF_EVEN), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(5, RoundingMode.UNNECESSARY), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(5, RoundingMode.FLOOR), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(5, RoundingMode.CEILING), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(5, RoundingMode.DOWN), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(5, RoundingMode.UP), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(5, RoundingMode.HALF_DOWN), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(5, RoundingMode.HALF_UP), "6789");
        aeq(Rational.read("6789").get().toBigDecimal(5, RoundingMode.HALF_EVEN), "6789");
        try {
            Rational.read("5").get().toBigDecimal(-1, RoundingMode.UNNECESSARY);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            Rational.read("5").get().toBigDecimal(-1, RoundingMode.FLOOR);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            Rational.read("5").get().toBigDecimal(-1, RoundingMode.CEILING);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            Rational.read("5").get().toBigDecimal(-1, RoundingMode.DOWN);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            Rational.read("5").get().toBigDecimal(-1, RoundingMode.UP);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            Rational.read("5").get().toBigDecimal(-1, RoundingMode.HALF_DOWN);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            Rational.read("5").get().toBigDecimal(-1, RoundingMode.HALF_UP);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            Rational.read("5").get().toBigDecimal(-1, RoundingMode.HALF_EVEN);
            fail();
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void equalsTest() {
        assertTrue(Rational.ZERO.equals(Rational.ZERO));
        assertTrue(Rational.ONE.equals(Rational.ONE));
        assertTrue(Rational.read("4").equals(Rational.read("4")));
        assertTrue(Rational.read("-4").equals(Rational.read("-4")));
        assertTrue(Rational.read("5/12").equals(Rational.read("5/12")));
        assertTrue(Rational.read("-5/12").equals(Rational.read("-5/12")));
        assertFalse(Rational.ZERO.equals(Rational.ONE));
        assertFalse(Rational.ONE.equals(Rational.ZERO));
        assertFalse(Rational.ZERO.equals(Rational.read("4")));
        assertFalse(Rational.ZERO.equals(Rational.read("-4")));
        assertFalse(Rational.ZERO.equals(Rational.read("5/12")));
        assertFalse(Rational.ZERO.equals(Rational.read("-5/12")));
        assertFalse(Rational.ONE.equals(Rational.read("4")));
        assertFalse(Rational.ONE.equals(Rational.read("-4")));
        assertFalse(Rational.ONE.equals(Rational.read("5/12")));
        assertFalse(Rational.ONE.equals(Rational.read("-5/12")));
        assertFalse(Rational.read("4").equals(Rational.ZERO));
        assertFalse(Rational.read("-4").equals(Rational.ZERO));
        assertFalse(Rational.read("5/12").equals(Rational.ZERO));
        assertFalse(Rational.read("-5/12").equals(Rational.ZERO));
        assertFalse(Rational.read("4").equals(Rational.ONE));
        assertFalse(Rational.read("-4").equals(Rational.ONE));
        assertFalse(Rational.read("5/12").equals(Rational.ONE));
        assertFalse(Rational.read("-5/12").equals(Rational.ONE));
        assertFalse(Rational.read("4").equals(Rational.read("-4")));
        assertFalse(Rational.read("4").equals(Rational.read("5/12")));
        assertFalse(Rational.read("4").equals(Rational.read("-5/12")));
        assertFalse(Rational.read("-4").equals(Rational.read("4")));
        assertFalse(Rational.read("-4").equals(Rational.read("5/12")));
        assertFalse(Rational.read("-4").equals(Rational.read("-5/12")));
        assertFalse(Rational.read("5/12").equals(Rational.read("4")));
        assertFalse(Rational.read("5/12").equals(Rational.read("-4")));
        assertFalse(Rational.read("5/12").equals(Rational.read("-5/12")));
        assertFalse(Rational.read("-5/12").equals(Rational.read("4")));
        assertFalse(Rational.read("-5/12").equals(Rational.read("-4")));
        assertFalse(Rational.read("-5/12").equals(Rational.read("5/12")));
        assertFalse(Rational.ZERO.equals(null));
        assertFalse(Rational.ONE.equals(null));
        assertFalse(Rational.read("4").equals(null));
        assertFalse(Rational.read("-4").equals(null));
        assertFalse(Rational.read("5/12").equals(null));
        assertFalse(Rational.read("-5/12").equals(null));
        assertFalse(Rational.read("-5/12").equals(2));
        assertFalse(Rational.read("-5/12").equals("Hi"));
        assertFalse(Rational.read("-5/12").equals(1.2e10));
    }

    @Test
    public void hashCodeTest() {
        aeq(Rational.ZERO.hashCode(), 1);
        aeq(Rational.ONE.hashCode(), 32);
        aeq(Rational.read("4").hashCode(), 125);
        aeq(Rational.read("-4").hashCode(), -123);
        aeq(Rational.read("5/12").hashCode(), 167);
        aeq(Rational.read("-5/12").hashCode(), -143);
    }

    @Test
    public void compareToTest() {
        aeq(Rational.ZERO.compareTo(Rational.ZERO), 0);
        aeq(Rational.ONE.compareTo(Rational.ONE), 0);
        assertTrue(eq(Rational.read("4").get(), Rational.read("4").get()));
        assertTrue(eq(Rational.read("-4").get(), Rational.read("-4").get()));
        assertTrue(eq(Rational.read("5/12").get(), Rational.read("5/12").get()));
        assertTrue(eq(Rational.read("-5/12").get(), Rational.read("-5/12").get()));
        assertTrue(lt(Rational.ZERO, Rational.ONE));
        assertTrue(gt(Rational.ONE, Rational.ZERO));
        assertTrue(lt(Rational.ZERO, Rational.read("4").get()));
        assertTrue(gt(Rational.ZERO, Rational.read("-4").get()));
        assertTrue(lt(Rational.ZERO, Rational.read("5/12").get()));
        assertTrue(gt(Rational.ZERO, Rational.read("-5/12").get()));
        assertTrue(lt(Rational.ONE, Rational.read("4").get()));
        assertTrue(gt(Rational.ONE, Rational.read("-4").get()));
        assertTrue(gt(Rational.ONE, Rational.read("5/12").get()));
        assertTrue(gt(Rational.ONE, Rational.read("-5/12").get()));
        assertTrue(gt(Rational.read("4").get(), Rational.ZERO));
        assertTrue(lt(Rational.read("-4").get(), Rational.ZERO));
        assertTrue(gt(Rational.read("5/12").get(), Rational.ZERO));
        assertTrue(lt(Rational.read("-5/12").get(), Rational.ZERO));
        assertTrue(gt(Rational.read("4").get(), Rational.ONE));
        assertTrue(lt(Rational.read("-4").get(), Rational.ONE));
        assertTrue(lt(Rational.read("5/12").get(), Rational.ONE));
        assertTrue(lt(Rational.read("-5/12").get(), Rational.ONE));
        assertTrue(gt(Rational.read("4").get(), Rational.read("-4").get()));
        assertTrue(gt(Rational.read("4").get(), Rational.read("5/12").get()));
        assertTrue(gt(Rational.read("4").get(), Rational.read("-5/12").get()));
        assertTrue(lt(Rational.read("-4").get(), Rational.read("4").get()));
        assertTrue(lt(Rational.read("-4").get(), Rational.read("5/12").get()));
        assertTrue(lt(Rational.read("-4").get(), Rational.read("-5/12").get()));
        assertTrue(lt(Rational.read("5/12").get(), Rational.read("4").get()));
        assertTrue(gt(Rational.read("5/12").get(), Rational.read("-4").get()));
        assertTrue(gt(Rational.read("5/12").get(), Rational.read("-5/12").get()));
        assertTrue(lt(Rational.read("-5/12").get(), Rational.read("4").get()));
        assertTrue(gt(Rational.read("-5/12").get(), Rational.read("-4").get()));
        assertTrue(lt(Rational.read("-5/12").get(), Rational.read("5/12").get()));
    }

    @Test
    public void readTest() {
        assertTrue(Rational.read("0").get() == Rational.ZERO);
        assertTrue(Rational.read("1").get() == Rational.ONE);
        aeq(Rational.read("3").get(), "3");
        aeq(Rational.read("-3").get(), "-3");
        aeq(Rational.read("5/12").get(), "5/12");
        aeq(Rational.read("-5/12").get(), "-5/12");
        aeq(Rational.read("12/6").get(), "2");
        aeq(Rational.read("12/-6").get(), "-2");
        aeq(Rational.read("6/8").get(), "3/4");
        aeq(Rational.read("-4/1").get(), "-4");
        try {
            Rational.read("2/0");
            fail();
        } catch (ArithmeticException e) {}
        assertFalse(Rational.read("").isPresent());
        assertFalse(Rational.read(" ").isPresent());
        assertFalse(Rational.read("1 ").isPresent());
        assertFalse(Rational.read("01").isPresent());
        assertFalse(Rational.read("-").isPresent());
        assertFalse(Rational.read("-0").isPresent());
        assertFalse(Rational.read("--5").isPresent());
        assertFalse(Rational.read("0.1").isPresent());
        assertFalse(Rational.read("/1").isPresent());
        assertFalse(Rational.read("/").isPresent());
        assertFalse(Rational.read("2//3").isPresent());
        assertFalse(Rational.read("2/").isPresent());
        assertFalse(Rational.read("2 /3").isPresent());
        assertFalse(Rational.read("2/ 3").isPresent());
        assertFalse(Rational.read("2 / 3").isPresent());
        assertFalse(Rational.read("a").isPresent());
        assertFalse(Rational.read("2-3").isPresent());
        assertFalse(Rational.read("0x12").isPresent());
        assertFalse(Rational.read("12/3a").isPresent());
    }

    @Test
    public void toStringTest() {
        aeq(Rational.ZERO, "0");
        aeq(Rational.ONE, "1");
        aeq(Rational.read("4").get(), "4");
        aeq(Rational.read("-4").get(), "-4");
        aeq(Rational.read("2/5").get(), "2/5");
        aeq(Rational.read("-2/5").get(), "-2/5");
    }

    private static void aeq(Object a, Object b) {
        assertEquals(a.toString(), b.toString());
    }
}
