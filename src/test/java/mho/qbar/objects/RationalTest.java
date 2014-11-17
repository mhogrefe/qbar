package mho.qbar.objects;

import mho.wheels.iterables.IterableUtils;
import mho.wheels.misc.FloatUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static mho.wheels.ordering.Ordering.*;
import static mho.qbar.objects.Rational.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class RationalTest {
    @Test
    public void testConstants() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
        aeq(SMALLEST_FLOAT, "1/713623846352979940529142984724747568191373312");
        aeq(LARGEST_SUBNORMAL_FLOAT, "8388607/713623846352979940529142984724747568191373312");
        aeq(SMALLEST_NORMAL_FLOAT, "1/85070591730234615865843651857942052864");
        aeq(LARGEST_FLOAT, "340282346638528859811704183484516925440");
        aeq(SMALLEST_DOUBLE,
                "1/2024022533073106183524953467189173070495566497641421183569013580274303395679953468919603837014371" +
                "244951870778643168119113898087373857934768670133999407385099215174242765663613644669077420932163412" +
                "397676784727450685620074834246926986181033556491595563408100565123587695523334146152305025321863275" +
                "08646006263307707741093494784");
        aeq(LARGEST_SUBNORMAL_DOUBLE,
                "4503599627370495/2024022533073106183524953467189173070495566497641421183569013580274303395679953468" +
                "919603837014371244951870778643168119113898087373857934768670133999407385099215174242765663613644669" +
                "077420932163412397676784727450685620074834246926986181033556491595563408100565123587695523334146152" +
                "30502532186327508646006263307707741093494784");
        aeq(SMALLEST_NORMAL_DOUBLE,
                "1/4494232837155789769323262976972561834044942447355766431835752028943316895137524078317711933060188" +
                "400528002846996784833941469744220360415562321185765986853109444197335621637131907555490031152352986" +
                "327073802125144220953767058561572036847827763520680929083762767114657455998681148461992907620883908" +
                "2406056034304");
        aeq(LARGEST_DOUBLE,
                "179769313486231570814527423731704356798070567525844996598917476803157260780028538760589558632766878" +
                "171540458953514382464234321326889464182768467546703537516986049910576551282076245490090389328944075" +
                "868508455133942304583236903222948165808559332123348274797826204144723168738177180919299881250404026" +
                "184124858368");
    }

    @Test
    public void testGetNumerator() {
        aeq(ZERO.getNumerator(), "0");
        aeq(ONE.getNumerator(), "1");
        aeq(read("2").get().getNumerator(), "2");
        aeq(read("-2").get().getNumerator(), "-2");
        aeq(read("5/3").get().getNumerator(), "5");
        aeq(read("-5/3").get().getNumerator(), "-5");
    }

    @Test
    public void testGetDenominator() {
        aeq(ZERO.getDenominator(), "1");
        aeq(ONE.getDenominator(), "1");
        aeq(read("2").get().getDenominator(), "1");
        aeq(read("-2").get().getDenominator(), "1");
        aeq(read("5/3").get().getDenominator(), "3");
        aeq(read("-5/3").get().getDenominator(), "3");
    }

    @Test
    public void testOf_BigInteger_BigInteger() {
        aeq(of(BigInteger.valueOf(2), BigInteger.valueOf(3)), "2/3");
        aeq(of(BigInteger.valueOf(4), BigInteger.valueOf(6)), "2/3");
        aeq(of(BigInteger.valueOf(-4), BigInteger.valueOf(-6)), "2/3");
        aeq(of(BigInteger.valueOf(4), BigInteger.valueOf(-6)), "-2/3");
        assertTrue(of(BigInteger.valueOf(4), BigInteger.valueOf(4)) == ONE);
        aeq(of(BigInteger.valueOf(4), BigInteger.valueOf(1)), "4");
        assertTrue(of(BigInteger.valueOf(0), BigInteger.valueOf(1)) == ZERO);
        try {
            of(BigInteger.valueOf(1), BigInteger.ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testOf_int_int() {
        aeq(of(2, 3), "2/3");
        aeq(of(4, 6), "2/3");
        aeq(of(-4, -6), "2/3");
        aeq(of(4, -6), "-2/3");
        assertTrue(of(4, 4) == ONE);
        aeq(of(4, 1), "4");
        assertTrue(of(0, 1) == ZERO);
        try {
            of(1, 0);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testOf_BigInteger() {
        aeq(of(BigInteger.valueOf(23)), "23");
        aeq(of(BigInteger.valueOf(-23)), "-23");
        assertTrue(of(BigInteger.valueOf(0)) == ZERO);
        assertTrue(of(BigInteger.valueOf(1)) == ONE);
    }

    @Test
    public void testOf_int() {
        aeq(of(23), "23");
        aeq(of(-23), "-23");
        assertTrue(of(0) == ZERO);
        assertTrue(of(1) == ONE);
    }

    @Test
    public void testOf_float() {
        assertTrue(of(0.0f) == ZERO);
        assertTrue(of(1.0f) == ONE);
        aeq(of(13.0f), "13");
        aeq(of(-5.0f), "-5");
        aeq(of(1.5f), "3/2");
        aeq(of(0.15625f), "5/32");
        aeq(of(0.1f), "13421773/134217728");
        aeq(of(1.0f / 3.0f), "11184811/33554432");
        aeq(of(1e10f), "10000000000");
        aeq(of(1e30f), "1000000015047466219876688855040");
        aeq(of((float) Math.PI), "13176795/4194304");
        aeq(of((float) Math.E), "2850325/1048576");
        aeq(of((float) Math.sqrt(2)), "11863283/8388608");
        aeq(of(Float.MIN_VALUE), SMALLEST_FLOAT);
        aeq(of(-Float.MIN_VALUE), SMALLEST_FLOAT.negate());
        aeq(of(Float.MIN_NORMAL), SMALLEST_NORMAL_FLOAT);
        aeq(of(-Float.MIN_NORMAL), SMALLEST_NORMAL_FLOAT.negate());
        aeq(of(Float.MAX_VALUE), LARGEST_FLOAT);
        aeq(of(-Float.MAX_VALUE), LARGEST_FLOAT.negate());
        assertNull(of(Float.POSITIVE_INFINITY));
        assertNull(of(Float.NEGATIVE_INFINITY));
        assertNull(of(Float.NaN));
    }

    @Test
    public void testOf_double() {
        assertTrue(of(0.0) == ZERO);
        assertTrue(of(1.0) == ONE);
        aeq(of(13.0), "13");
        aeq(of(-5.0), "-5");
        aeq(of(1.5), "3/2");
        aeq(of(0.15625), "5/32");
        aeq(of(0.1), "3602879701896397/36028797018963968");
        aeq(of(1.0 / 3.0), "6004799503160661/18014398509481984");
        aeq(of(1e10), "10000000000");
        aeq(of(1e30), "1000000000000000019884624838656");
        aeq(of(Math.PI), "884279719003555/281474976710656");
        aeq(of(Math.E), "6121026514868073/2251799813685248");
        aeq(of(Math.sqrt(2)), "6369051672525773/4503599627370496");
        aeq(of(Double.MIN_VALUE), SMALLEST_DOUBLE);
        aeq(of(-Double.MIN_VALUE), SMALLEST_DOUBLE.negate());
        aeq(of(Double.MIN_NORMAL), SMALLEST_NORMAL_DOUBLE);
        aeq(of(-Double.MIN_NORMAL), SMALLEST_NORMAL_DOUBLE.negate());
        aeq(of(Double.MAX_VALUE), LARGEST_DOUBLE);
        aeq(of(-Double.MAX_VALUE), LARGEST_DOUBLE.negate());
        assertNull(of(Double.POSITIVE_INFINITY));
        assertNull(of(Double.NEGATIVE_INFINITY));
        assertNull(of(Double.NaN));
    }

    @Test
    public void testOf_BigDecimal() {
        assertTrue(of(BigDecimal.ZERO) == ZERO);
        assertTrue(of(BigDecimal.ONE) == ONE);
        aeq(of(new BigDecimal("3")), "3");
        aeq(of(new BigDecimal("-5")), "-5");
        aeq(of(new BigDecimal("0.1")), "1/10");
        aeq(of(new BigDecimal("3.14159")), "314159/100000");
        aeq(of(new BigDecimal("-2.718281828459045")), "-543656365691809/200000000000000");
        aeq(of(new BigDecimal("0.00000000000001")), "1/100000000000000");
        aeq(of(new BigDecimal("1000000000000000")), "1000000000000000");
    }

    @Test
    public void testNegate() {
        aeq(read("2/3").get().negate(), "-2/3");
        aeq(read("-2/3").get().negate(), "2/3");
        aeq(read("4").get().negate(), "-4");
        aeq(read("-4").get().negate(), "4");
        assertTrue(ZERO.negate() == ZERO);
        aeq(ONE.negate(), "-1");
        assertTrue(read("-1").get().negate() == ONE);
    }

    @Test
    public void testInvert() {
        aeq(read("2/3").get().invert(), "3/2");
        aeq(read("-2/3").get().invert(), "-3/2");
        aeq(read("4").get().invert(), "1/4");
        aeq(read("1/4").get().invert(), "4");
        assertTrue(ONE.invert() == ONE);
        try {
            ZERO.invert();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testAbs() {
        aeq(read("2/3").get().abs(), "2/3");
        aeq(read("-2/3").get().abs(), "2/3");
        aeq(read("4").get().abs(), "4");
        aeq(read("-4").get().abs(), "4");
        aeq(ZERO.abs(), "0");
        aeq(ONE.abs(), "1");
    }

    @Test
    public void testSignum() {
        aeq(read("2/3").get().signum(), 1);
        aeq(read("-2/3").get().signum(), -1);
        aeq(read("4").get().signum(), 1);
        aeq(read("-4").get().signum(), -1);
        aeq(ZERO.signum(), 0);
        aeq(ONE.signum(), 1);
    }

    @Test
    public void testAdd() {
        aeq(add(read("1/2").get(), read("1/3").get()), "5/6");
        aeq(add(read("1/2").get(), read("-1/3").get()), "1/6");
        aeq(add(read("-1/2").get(), read("1/3").get()), "-1/6");
        aeq(add(read("-1/2").get(), read("-1/3").get()), "-5/6");
        aeq(add(read("2").get(), read("1/5").get()), "11/5");
        aeq(add(read("2").get(), read("-1/5").get()), "9/5");
        aeq(add(read("-2").get(), read("1/5").get()), "-9/5");
        aeq(add(read("-2").get(), read("-1/5").get()), "-11/5");
        aeq(add(read("2").get(), read("5").get()), "7");
        aeq(add(read("2").get(), read("-5").get()), "-3");
        aeq(add(read("-2").get(), read("5").get()), "3");
        aeq(add(read("-2").get(), read("-5").get()), "-7");
        assertTrue(add(read("6/7").get(), read("1/7").get()) == ONE);
        assertTrue(add(read("6/7").get(), read("-6/7").get()) == ZERO);
        aeq(add(read("1/2").get(), ZERO), "1/2");
        aeq(add(read("-1/2").get(), ZERO), "-1/2");
        aeq(add(read("1/2").get(), ONE), "3/2");
        aeq(add(read("-1/2").get(), ONE), "1/2");
        assertTrue(add(ZERO, ZERO) == ZERO);
        assertTrue(add(ZERO, ONE) == ONE);
        assertTrue(add(ONE, ZERO) == ONE);
        aeq(add(ONE, ONE), "2");
    }

    @Test
    public void testSubtract() {
        aeq(subtract(read("1/2").get(), read("1/3").get()), "1/6");
        aeq(subtract(read("1/2").get(), read("-1/3").get()), "5/6");
        aeq(subtract(read("-1/2").get(), read("1/3").get()), "-5/6");
        aeq(subtract(read("-1/2").get(), read("-1/3").get()), "-1/6");
        aeq(subtract(read("2").get(), read("1/5").get()), "9/5");
        aeq(subtract(read("2").get(), read("-1/5").get()), "11/5");
        aeq(subtract(read("-2").get(), read("1/5").get()), "-11/5");
        aeq(subtract(read("-2").get(), read("-1/5").get()), "-9/5");
        aeq(subtract(read("2").get(), read("5").get()), "-3");
        aeq(subtract(read("2").get(), read("-5").get()), "7");
        aeq(subtract(read("-2").get(), read("5").get()), "-7");
        aeq(subtract(read("-2").get(), read("-5").get()), "3");
        assertTrue(subtract(read("8/7").get(), read("1/7").get()) == ONE);
        assertTrue(subtract(read("6/7").get(), read("6/7").get()) == ZERO);
        aeq(subtract(read("1/2").get(), ZERO), "1/2");
        aeq(subtract(read("-1/2").get(), ZERO), "-1/2");
        aeq(subtract(read("1/2").get(), ONE), "-1/2");
        aeq(subtract(read("-1/2").get(), ONE), "-3/2");
        assertTrue(subtract(ZERO, ZERO) == ZERO);
        aeq(subtract(ZERO, ONE), "-1");
        assertTrue(subtract(ONE, ZERO) == ONE);
        assertTrue(subtract(ONE, ONE) == ZERO);
    }

    @Test
    public void testMultiply_Rational_Rational() {
        aeq(multiply(read("2/3").get(), read("6/7").get()), "4/7");
        aeq(multiply(read("2/3").get(), read("-6/7").get()), "-4/7");
        aeq(multiply(read("-2/3").get(), read("6/7").get()), "-4/7");
        aeq(multiply(read("-2/3").get(), read("-6/7").get()), "4/7");
        aeq(multiply(read("2/3").get(), read("4").get()), "8/3");
        aeq(multiply(read("2/3").get(), read("-4").get()), "-8/3");
        aeq(multiply(read("-2/3").get(), read("4").get()), "-8/3");
        aeq(multiply(read("-2/3").get(), read("-4").get()), "8/3");
        aeq(multiply(read("3").get(), read("5").get()), "15");
        aeq(multiply(read("3").get(), read("-5").get()), "-15");
        aeq(multiply(read("-3").get(), read("5").get()), "-15");
        aeq(multiply(read("-3").get(), read("-5").get()), "15");
        assertTrue(multiply(read("1/4").get(), read("4").get()) == ONE);
        assertTrue(multiply(read("-1/4").get(), read("-4").get()) == ONE);
        assertTrue(multiply(read("2/3").get(), ZERO) == ZERO);
        assertTrue(multiply(read("-2/3").get(), ZERO) == ZERO);
        aeq(multiply(read("2/3").get(), ONE), "2/3");
        aeq(multiply(read("-2/3").get(), ONE), "-2/3");
        assertTrue(multiply(ZERO, ZERO) == ZERO);
        assertTrue(multiply(ZERO, ONE) == ZERO);
        assertTrue(multiply(ONE, ZERO) == ZERO);
        assertTrue(multiply(ONE, ONE) == ONE);
    }

    @Test
    public void multiply_BigInteger() {
        aeq(read("2/3").get().multiply(BigInteger.valueOf(4)), "8/3");
        aeq(read("2/3").get().multiply(BigInteger.valueOf(-4)), "-8/3");
        aeq(read("-2/3").get().multiply(BigInteger.valueOf(4)), "-8/3");
        aeq(read("-2/3").get().multiply(BigInteger.valueOf(-4)), "8/3");
        aeq(read("2/3").get().multiply(BigInteger.valueOf(3)), "2");
        aeq(read("2/3").get().multiply(BigInteger.valueOf(-3)), "-2");
        aeq(read("-2/3").get().multiply(BigInteger.valueOf(3)), "-2");
        aeq(read("-2/3").get().multiply(BigInteger.valueOf(-3)), "2");
        assertTrue(read("2/3").get().multiply(BigInteger.ZERO) == ZERO);
        aeq(read("2/3").get().multiply(BigInteger.ONE), "2/3");
    }

    @Test
    public void multiply_int() {
        aeq(read("2/3").get().multiply(4), "8/3");
        aeq(read("2/3").get().multiply(-4), "-8/3");
        aeq(read("-2/3").get().multiply(4), "-8/3");
        aeq(read("-2/3").get().multiply(-4), "8/3");
        aeq(read("2/3").get().multiply(3), "2");
        aeq(read("2/3").get().multiply(-3), "-2");
        aeq(read("-2/3").get().multiply(3), "-2");
        aeq(read("-2/3").get().multiply(-3), "2");
        assertTrue(read("2/3").get().multiply(0) == ZERO);
        assertTrue(read("1/3").get().multiply(3) == ONE);
        assertTrue(read("-1/3").get().multiply(-3) == ONE);
        aeq(read("2/3").get().multiply(1), "2/3");
    }

    @Test
    public void testDivide_Rational_Rational() {
        aeq(divide(read("2/3").get(), read("6/7").get()), "7/9");
        aeq(divide(read("2/3").get(), read("-6/7").get()), "-7/9");
        aeq(divide(read("-2/3").get(), read("6/7").get()), "-7/9");
        aeq(divide(read("-2/3").get(), read("-6/7").get()), "7/9");
        aeq(divide(read("2/3").get(), read("4").get()), "1/6");
        aeq(divide(read("2/3").get(), read("-4").get()), "-1/6");
        aeq(divide(read("-2/3").get(), read("4").get()), "-1/6");
        aeq(divide(read("-2/3").get(), read("-4").get()), "1/6");
        aeq(divide(read("3").get(), read("5").get()), "3/5");
        aeq(divide(read("3").get(), read("-5").get()), "-3/5");
        aeq(divide(read("-3").get(), read("5").get()), "-3/5");
        aeq(divide(read("-3").get(), read("-5").get()), "3/5");
        aeq(divide(read("1/4").get(), read("4").get()), "1/16");
        assertTrue(divide(read("2/3").get(), read("2/3").get()) == ONE);
        assertTrue(divide(read("-2/3").get(), read("-2/3").get()) == ONE);
        aeq(divide(read("2/3").get(), ONE), "2/3");
        aeq(divide(read("-2/3").get(), ONE), "-2/3");
        assertTrue(divide(ZERO, ONE) == ZERO);
        assertTrue(divide(ONE, ONE) == ONE);
        try {
            divide(read("2/3").get(), ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            divide(read("3").get(), ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            divide(ONE, ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            divide(ZERO, ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void divide_BigInteger() {
        aeq(read("2/3").get().divide(BigInteger.valueOf(4)), "1/6");
        aeq(read("2/3").get().divide(BigInteger.valueOf(-4)), "-1/6");
        aeq(read("-2/3").get().divide(BigInteger.valueOf(4)), "-1/6");
        aeq(read("-2/3").get().divide(BigInteger.valueOf(-4)), "1/6");
        aeq(read("2/3").get().divide(BigInteger.valueOf(3)), "2/9");
        aeq(read("2/3").get().divide(BigInteger.valueOf(-3)), "-2/9");
        aeq(read("-2/3").get().divide(BigInteger.valueOf(3)), "-2/9");
        aeq(read("-2/3").get().divide(BigInteger.valueOf(-3)), "2/9");
        aeq(read("2/3").get().divide(BigInteger.ONE), "2/3");
        assertTrue(ZERO.divide(BigInteger.valueOf(3)) == ZERO);
        assertTrue(read("5").get().divide(BigInteger.valueOf(5)) == ONE);
        assertTrue(read("-5").get().divide(BigInteger.valueOf(-5)) == ONE);
        try {
            read("2/3").get().divide(BigInteger.ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void divide_int() {
        aeq(read("2/3").get().divide(4), "1/6");
        aeq(read("2/3").get().divide(-4), "-1/6");
        aeq(read("-2/3").get().divide(4), "-1/6");
        aeq(read("-2/3").get().divide(-4), "1/6");
        aeq(read("2/3").get().divide(3), "2/9");
        aeq(read("2/3").get().divide(-3), "-2/9");
        aeq(read("-2/3").get().divide(3), "-2/9");
        aeq(read("-2/3").get().divide(-3), "2/9");
        aeq(read("2/3").get().divide(1), "2/3");
        assertTrue(ZERO.divide(3) == ZERO);
        assertTrue(read("5").get().divide(5) == ONE);
        assertTrue(read("-5").get().divide(-5) == ONE);
        try {
            read("2/3").get().divide(0);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPow() {
        assertTrue(read("2/3").get().pow(0) == ONE);
        aeq(read("2/3").get().pow(1), "2/3");
        aeq(read("2/3").get().pow(2), "4/9");
        aeq(read("2/3").get().pow(3), "8/27");
        aeq(read("2/3").get().pow(-1), "3/2");
        aeq(read("2/3").get().pow(-2), "9/4");
        aeq(read("2/3").get().pow(-3), "27/8");
        aeq(read("-2/3").get().pow(0), "1");
        aeq(read("-2/3").get().pow(1), "-2/3");
        aeq(read("-2/3").get().pow(2), "4/9");
        aeq(read("-2/3").get().pow(3), "-8/27");
        aeq(read("-2/3").get().pow(-1), "-3/2");
        aeq(read("-2/3").get().pow(-2), "9/4");
        aeq(read("-2/3").get().pow(-3), "-27/8");
        assertTrue(read("2").get().pow(0) == ONE);
        aeq(read("2").get().pow(1), "2");
        aeq(read("2").get().pow(2), "4");
        aeq(read("2").get().pow(3), "8");
        aeq(read("2").get().pow(-1), "1/2");
        aeq(read("2").get().pow(-2), "1/4");
        aeq(read("2").get().pow(-3), "1/8");
        assertTrue(read("-2").get().pow(0) == ONE);
        aeq(read("-2").get().pow(1), "-2");
        aeq(read("-2").get().pow(2), "4");
        aeq(read("-2").get().pow(3), "-8");
        aeq(read("-2").get().pow(-1), "-1/2");
        aeq(read("-2").get().pow(-2), "1/4");
        aeq(read("-2").get().pow(-3), "-1/8");
        assertTrue(ZERO.pow(0) == ONE);
        assertTrue(ZERO.pow(1) == ZERO);
        assertTrue(ZERO.pow(2) == ZERO);
        assertTrue(ZERO.pow(3) == ZERO);
        assertTrue(ONE.pow(0) == ONE);
        assertTrue(ONE.pow(1) == ONE);
        assertTrue(ONE.pow(2) == ONE);
        assertTrue(ONE.pow(3) == ONE);
        assertTrue(ONE.pow(-1) == ONE);
        assertTrue(ONE.pow(-2) == ONE);
        assertTrue(ONE.pow(-3) == ONE);
        try {
            ZERO.pow(-1);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            ZERO.pow(-2);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            ZERO.pow(-3);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testFloor() {
        aeq(read("7/3").get().floor(), 2);
        aeq(read("5/3").get().floor(), 1);
        aeq(read("2/3").get().floor(), 0);
        aeq(read("-1/3").get().floor(), -1);
        aeq(read("-4/3").get().floor(), -2);
        aeq(read("4").get().floor(), 4);
        aeq(read("-2").get().floor(), -2);
        aeq(ZERO.floor(), 0);
        aeq(ONE.floor(), 1);
    }

    @Test
    public void testCeiling() {
        aeq(read("7/3").get().ceiling(), 3);
        aeq(read("5/3").get().ceiling(), 2);
        aeq(read("2/3").get().ceiling(), 1);
        aeq(read("-1/3").get().ceiling(), 0);
        aeq(read("-4/3").get().ceiling(), -1);
        aeq(read("4").get().ceiling(), 4);
        aeq(read("-2").get().ceiling(), -2);
        aeq(ZERO.ceiling(), 0);
        aeq(ONE.ceiling(), 1);
    }

    @Test
    public void testFractionalPart() {
        aeq(read("7/3").get().fractionalPart(), "1/3");
        aeq(read("5/3").get().fractionalPart(), "2/3");
        aeq(read("2/3").get().fractionalPart(), "2/3");
        aeq(read("-1/3").get().fractionalPart(), "2/3");
        aeq(read("-4/3").get().fractionalPart(), "2/3");
        assertTrue(read("4").get().fractionalPart() == ZERO);
        assertTrue(read("-2").get().fractionalPart() == ZERO);
        assertTrue(ZERO.fractionalPart() == ZERO);
        assertTrue(ONE.fractionalPart() == ZERO);
    }

    @Test
    public void testRound() {
        aeq(read("11/2").get().round(RoundingMode.UP), 6);
        aeq(read("5/2").get().round(RoundingMode.UP), 3);
        aeq(read("8/5").get().round(RoundingMode.UP), 2);
        aeq(read("11/10").get().round(RoundingMode.UP), 2);
        aeq(ONE.round(RoundingMode.UP), 1);
        aeq(ZERO.round(RoundingMode.UP), 0);
        aeq(read("-1").get().round(RoundingMode.UP), -1);
        aeq(read("-11/10").get().round(RoundingMode.UP), -2);
        aeq(read("-8/5").get().round(RoundingMode.UP), -2);
        aeq(read("-5/2").get().round(RoundingMode.UP), -3);
        aeq(read("-11/2").get().round(RoundingMode.UP), -6);

        aeq(read("11/2").get().round(RoundingMode.DOWN), 5);
        aeq(read("5/2").get().round(RoundingMode.DOWN), 2);
        aeq(read("8/5").get().round(RoundingMode.DOWN), 1);
        aeq(read("11/10").get().round(RoundingMode.DOWN), 1);
        aeq(ONE.round(RoundingMode.DOWN), 1);
        aeq(ZERO.round(RoundingMode.DOWN), 0);
        aeq(read("-1").get().round(RoundingMode.DOWN), -1);
        aeq(read("-11/10").get().round(RoundingMode.DOWN), -1);
        aeq(read("-8/5").get().round(RoundingMode.DOWN), -1);
        aeq(read("-5/2").get().round(RoundingMode.DOWN), -2);
        aeq(read("-11/2").get().round(RoundingMode.DOWN), -5);

        aeq(read("11/2").get().round(RoundingMode.CEILING), 6);
        aeq(read("5/2").get().round(RoundingMode.CEILING), 3);
        aeq(read("8/5").get().round(RoundingMode.CEILING), 2);
        aeq(read("11/10").get().round(RoundingMode.CEILING), 2);
        aeq(ONE.round(RoundingMode.CEILING), 1);
        aeq(ZERO.round(RoundingMode.CEILING), 0);
        aeq(read("-1").get().round(RoundingMode.CEILING), -1);
        aeq(read("-11/10").get().round(RoundingMode.CEILING), -1);
        aeq(read("-8/5").get().round(RoundingMode.CEILING), -1);
        aeq(read("-5/2").get().round(RoundingMode.CEILING), -2);
        aeq(read("-11/2").get().round(RoundingMode.CEILING), -5);

        aeq(read("11/2").get().round(RoundingMode.FLOOR), 5);
        aeq(read("5/2").get().round(RoundingMode.FLOOR), 2);
        aeq(read("8/5").get().round(RoundingMode.FLOOR), 1);
        aeq(read("11/10").get().round(RoundingMode.FLOOR), 1);
        aeq(ONE.round(RoundingMode.FLOOR), 1);
        aeq(ZERO.round(RoundingMode.FLOOR), 0);
        aeq(read("-1").get().round(RoundingMode.FLOOR), -1);
        aeq(read("-11/10").get().round(RoundingMode.FLOOR), -2);
        aeq(read("-8/5").get().round(RoundingMode.FLOOR), -2);
        aeq(read("-5/2").get().round(RoundingMode.FLOOR), -3);
        aeq(read("-11/2").get().round(RoundingMode.FLOOR), -6);

        aeq(read("11/2").get().round(RoundingMode.HALF_UP), 6);
        aeq(read("5/2").get().round(RoundingMode.HALF_UP), 3);
        aeq(read("8/5").get().round(RoundingMode.HALF_UP), 2);
        aeq(read("11/10").get().round(RoundingMode.HALF_UP), 1);
        aeq(ONE.round(RoundingMode.HALF_UP), 1);
        aeq(ZERO.round(RoundingMode.HALF_UP), 0);
        aeq(read("-1").get().round(RoundingMode.HALF_UP), -1);
        aeq(read("-11/10").get().round(RoundingMode.HALF_UP), -1);
        aeq(read("-8/5").get().round(RoundingMode.HALF_UP), -2);
        aeq(read("-5/2").get().round(RoundingMode.HALF_UP), -3);
        aeq(read("-11/2").get().round(RoundingMode.HALF_UP), -6);

        aeq(read("11/2").get().round(RoundingMode.HALF_DOWN), 5);
        aeq(read("5/2").get().round(RoundingMode.HALF_DOWN), 2);
        aeq(read("8/5").get().round(RoundingMode.HALF_DOWN), 2);
        aeq(read("11/10").get().round(RoundingMode.HALF_DOWN), 1);
        aeq(ONE.round(RoundingMode.HALF_DOWN), 1);
        aeq(ZERO.round(RoundingMode.HALF_DOWN), 0);
        aeq(read("-1").get().round(RoundingMode.HALF_DOWN), -1);
        aeq(read("-11/10").get().round(RoundingMode.HALF_DOWN), -1);
        aeq(read("-8/5").get().round(RoundingMode.HALF_DOWN), -2);
        aeq(read("-5/2").get().round(RoundingMode.HALF_DOWN), -2);
        aeq(read("-11/2").get().round(RoundingMode.HALF_DOWN), -5);

        aeq(read("11/2").get().round(RoundingMode.HALF_EVEN), 6);
        aeq(read("5/2").get().round(RoundingMode.HALF_EVEN), 2);
        aeq(read("8/5").get().round(RoundingMode.HALF_EVEN), 2);
        aeq(read("11/10").get().round(RoundingMode.HALF_EVEN), 1);
        aeq(ONE.round(RoundingMode.HALF_EVEN), 1);
        aeq(ZERO.round(RoundingMode.HALF_EVEN), 0);
        aeq(read("-1").get().round(RoundingMode.HALF_EVEN), -1);
        aeq(read("-11/10").get().round(RoundingMode.HALF_EVEN), -1);
        aeq(read("-8/5").get().round(RoundingMode.HALF_EVEN), -2);
        aeq(read("-5/2").get().round(RoundingMode.HALF_EVEN), -2);
        aeq(read("-11/2").get().round(RoundingMode.HALF_EVEN), -6);

        try {
            read("11/2").get().round(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("5/2").get().round(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("8/5").get().round(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("11/10").get().round(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(ONE.round(RoundingMode.UNNECESSARY), 1);
        aeq(ZERO.round(RoundingMode.UNNECESSARY), 0);
        aeq(read("-1").get().round(RoundingMode.UNNECESSARY), -1);
        try {
            System.out.println(read("-11/10").get().round(RoundingMode.UNNECESSARY));
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            System.out.println(read("-8/5").get().round(RoundingMode.UNNECESSARY));
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            System.out.println(read("-5/2").get().round(RoundingMode.UNNECESSARY));
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            System.out.println(read("-11/2").get().round(RoundingMode.UNNECESSARY));
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRoundToDenominator() {
        Rational doublePi = of(Math.PI);
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
        aeq(read("3/10").get().roundToDenominator(BigInteger.valueOf(30), RoundingMode.UNNECESSARY), "3/10");
        try {
            doublePi.roundToDenominator(BigInteger.ZERO, RoundingMode.HALF_EVEN);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            doublePi.roundToDenominator(BigInteger.valueOf(-1), RoundingMode.HALF_EVEN);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            doublePi.roundToDenominator(BigInteger.valueOf(7), RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShiftLeft() {
        aeq(read("7/12").get().shiftLeft(0), "7/12");
        aeq(read("7/12").get().shiftLeft(1), "7/6");
        aeq(read("7/12").get().shiftLeft(2), "7/3");
        aeq(read("7/12").get().shiftLeft(3), "14/3");
        aeq(read("7/12").get().shiftLeft(4), "28/3");
        aeq(read("7/12").get().shiftLeft(-1), "7/24");
        aeq(read("7/12").get().shiftLeft(-2), "7/48");
        aeq(read("7/12").get().shiftLeft(-3), "7/96");
        aeq(read("7/12").get().shiftLeft(-4), "7/192");
        aeq(read("4/5").get().shiftLeft(0), "4/5");
        aeq(read("4/5").get().shiftLeft(1), "8/5");
        aeq(read("4/5").get().shiftLeft(2), "16/5");
        aeq(read("4/5").get().shiftLeft(3), "32/5");
        aeq(read("4/5").get().shiftLeft(4), "64/5");
        aeq(read("4/5").get().shiftLeft(-1), "2/5");
        aeq(read("4/5").get().shiftLeft(-2), "1/5");
        aeq(read("4/5").get().shiftLeft(-3), "1/10");
        aeq(read("4/5").get().shiftLeft(-4), "1/20");
        assertTrue(ZERO.shiftLeft(4) == ZERO);
        assertTrue(ONE.shiftLeft(0) == ONE);
        aeq(ONE.shiftLeft(1), "2");
        aeq(ONE.shiftLeft(2), "4");
        aeq(ONE.shiftLeft(3), "8");
        aeq(ONE.shiftLeft(4), "16");
        aeq(ONE.shiftLeft(-1), "1/2");
        aeq(ONE.shiftLeft(-2), "1/4");
        aeq(ONE.shiftLeft(-3), "1/8");
        aeq(ONE.shiftLeft(-4), "1/16");
        aeq(read("-4/5").get().shiftLeft(0), "-4/5");
        aeq(read("-4/5").get().shiftLeft(1), "-8/5");
        aeq(read("-4/5").get().shiftLeft(2), "-16/5");
        aeq(read("-4/5").get().shiftLeft(3), "-32/5");
        aeq(read("-4/5").get().shiftLeft(4), "-64/5");
        aeq(read("-4/5").get().shiftLeft(-1), "-2/5");
        aeq(read("-4/5").get().shiftLeft(-2), "-1/5");
        aeq(read("-4/5").get().shiftLeft(-3), "-1/10");
        aeq(read("-4/5").get().shiftLeft(-4), "-1/20");
        aeq(read("-1").get().shiftLeft(0), "-1");
        aeq(read("-1").get().shiftLeft(1), "-2");
        aeq(read("-1").get().shiftLeft(2), "-4");
        aeq(read("-1").get().shiftLeft(3), "-8");
        aeq(read("-1").get().shiftLeft(4), "-16");
        aeq(read("-1").get().shiftLeft(-1), "-1/2");
        aeq(read("-1").get().shiftLeft(-2), "-1/4");
        aeq(read("-1").get().shiftLeft(-3), "-1/8");
        aeq(read("-1").get().shiftLeft(-4), "-1/16");
    }

    @Test
    public void testShiftRight() {
        aeq(read("7/12").get().shiftRight(0), "7/12");
        aeq(read("7/12").get().shiftRight(1), "7/24");
        aeq(read("7/12").get().shiftRight(2), "7/48");
        aeq(read("7/12").get().shiftRight(3), "7/96");
        aeq(read("7/12").get().shiftRight(4), "7/192");
        aeq(read("7/12").get().shiftRight(-1), "7/6");
        aeq(read("7/12").get().shiftRight(-2), "7/3");
        aeq(read("7/12").get().shiftRight(-3), "14/3");
        aeq(read("7/12").get().shiftRight(-4), "28/3");
        aeq(read("4/5").get().shiftRight(0), "4/5");
        aeq(read("4/5").get().shiftRight(1), "2/5");
        aeq(read("4/5").get().shiftRight(2), "1/5");
        aeq(read("4/5").get().shiftRight(3), "1/10");
        aeq(read("4/5").get().shiftRight(4), "1/20");
        aeq(read("4/5").get().shiftRight(-1), "8/5");
        aeq(read("4/5").get().shiftRight(-2), "16/5");
        aeq(read("4/5").get().shiftRight(-3), "32/5");
        aeq(read("4/5").get().shiftRight(-4), "64/5");
        assertTrue(ZERO.shiftRight(4) == ZERO);
        assertTrue(ONE.shiftRight(0) == ONE);
        aeq(ONE.shiftRight(1), "1/2");
        aeq(ONE.shiftRight(2), "1/4");
        aeq(ONE.shiftRight(3), "1/8");
        aeq(ONE.shiftRight(4), "1/16");
        aeq(ONE.shiftRight(-1), "2");
        aeq(ONE.shiftRight(-2), "4");
        aeq(ONE.shiftRight(-3), "8");
        aeq(ONE.shiftRight(-4), "16");
        aeq(read("-4/5").get().shiftRight(0), "-4/5");
        aeq(read("-4/5").get().shiftRight(1), "-2/5");
        aeq(read("-4/5").get().shiftRight(2), "-1/5");
        aeq(read("-4/5").get().shiftRight(3), "-1/10");
        aeq(read("-4/5").get().shiftRight(4), "-1/20");
        aeq(read("-4/5").get().shiftRight(-1), "-8/5");
        aeq(read("-4/5").get().shiftRight(-2), "-16/5");
        aeq(read("-4/5").get().shiftRight(-3), "-32/5");
        aeq(read("-4/5").get().shiftRight(-4), "-64/5");
        aeq(read("-1").get().shiftRight(0), "-1");
        aeq(read("-1").get().shiftRight(1), "-1/2");
        aeq(read("-1").get().shiftRight(2), "-1/4");
        aeq(read("-1").get().shiftRight(3), "-1/8");
        aeq(read("-1").get().shiftRight(4), "-1/16");
        aeq(read("-1").get().shiftRight(-1), "-2");
        aeq(read("-1").get().shiftRight(-2), "-4");
        aeq(read("-1").get().shiftRight(-3), "-8");
        aeq(read("-1").get().shiftRight(-4), "-16");
    }

    @Test
    public void testBinaryExponent() {
        aeq(read("1/3").get().binaryExponent(), -2);
        aeq(read("100").get().binaryExponent(), 6);
        aeq(read("127").get().binaryExponent(), 6);
        aeq(read("128").get().binaryExponent(), 7);
        aeq(read("129").get().binaryExponent(), 7);
        aeq(read("1/127").get().binaryExponent(), -7);
        aeq(read("1/128").get().binaryExponent(), -7);
        aeq(read("1/129").get().binaryExponent(), -8);
        aeq(ONE.binaryExponent(), 0);
    }

    @Test
    public void testToFloat() {
        aeq(ZERO.toFloat(), 0.0);
        aeq(ONE.toFloat(), 1.0);
        aeq(read("1/2").get().toFloat(), 0.5);
        aeq(read("1/3").get().toFloat(), 0.33333334);
        aeq(read("-1/3").get().toFloat(), -0.33333334);
        Rational almostOne = of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
        aeq(almostOne.toFloat(), 1.0);
        Rational floatPi = of((float) Math.PI);
        if (floatPi == null) {
            fail();
        }
        aeq(floatPi.toFloat(), 3.1415927);
        Rational trillion = of(BigInteger.TEN.pow(12));
        aeq(trillion.toFloat(), 1.0E12);
        Rational piSuccessor = of(FloatUtils.successor((float) Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = of(FloatUtils.predecessor((float) Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = add(floatPi, piSuccessor).divide(2);
        Rational halfBelowPi = add(floatPi, piPredecessor).divide(2);
        Rational justAbovePi = add(floatPi.multiply(2), piSuccessor).divide(3);
        Rational justBelowPi = add(floatPi.multiply(2), piPredecessor).divide(3);
        aeq(halfAbovePi.toFloat(), 3.141593);
        aeq(halfBelowPi.toFloat(), 3.1415925);
        aeq(justAbovePi.toFloat(), 3.1415927);
        aeq(justBelowPi.toFloat(), 3.1415927);
        Rational subnormal = of(1e-40f);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = of(FloatUtils.successor(1e-40f));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = of(FloatUtils.predecessor(1e-40f));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = add(subnormal, subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = add(subnormal, subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = add(subnormal.multiply(2), subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = add(subnormal.multiply(2), subnormalPredecessor).divide(3);
        aeq(subnormal.toFloat(), 1.0E-40);
        aeq(halfAboveSubnormal.toFloat(), 1.0E-40);
        aeq(halfBelowSubnormal.toFloat(), 1.0E-40);
        aeq(justAboveSubnormal.toFloat(), 1.0E-40);
        aeq(justBelowSubnormal.toFloat(), 1.0E-40);
        Rational belowNegativeMax = subtract(LARGEST_FLOAT.negate(), ONE);
        Rational negativeMaxSuccessor = of(FloatUtils.successor(-Float.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = add(LARGEST_FLOAT.negate(), negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = add(
                LARGEST_FLOAT.negate().multiply(2),
                negativeMaxSuccessor
        ).divide(3);
        aeq(belowNegativeMax.toFloat(), Float.NEGATIVE_INFINITY);
        aeq(halfAboveNegativeMax.toFloat(), -3.4028233E38);
        aeq(justAboveNegativeMax.toFloat(), -3.4028235E38);
        Rational aboveMax = add(LARGEST_FLOAT, ONE);
        Rational maxPredecessor = of(FloatUtils.predecessor(Float.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = add(LARGEST_FLOAT, maxPredecessor).divide(2);
        Rational justBelowMax = add(LARGEST_FLOAT.multiply(2), maxPredecessor).divide(3);
        aeq(aboveMax.toFloat(), Float.POSITIVE_INFINITY);
        aeq(halfBelowMax.toFloat(), 3.4028233E38);
        aeq(justBelowMax.toFloat(), 3.4028235E38);
        Rational halfAboveZero = SMALLEST_FLOAT.shiftRight(1);
        Rational justAboveZero = SMALLEST_FLOAT.divide(3);
        Rational halfBelowZero = halfAboveZero.negate();
        Rational justBelowZero = justAboveZero.negate();
        aeq(halfBelowZero.toFloat(), -0.0);
        aeq(justBelowZero.toFloat(), -0.0);
        aeq(halfAboveZero.toFloat(), 0.0);
        aeq(justAboveZero.toFloat(), 0.0);
        Rational boundary = add(
                LARGEST_SUBNORMAL_FLOAT,
                SMALLEST_NORMAL_FLOAT
        ).shiftRight(1);
        Rational halfBelowBoundary = add(LARGEST_SUBNORMAL_FLOAT, boundary).shiftRight(1);
        Rational halfAboveBoundary = add(SMALLEST_NORMAL_FLOAT, boundary).shiftRight(1);
        Rational justBelowBoundary = add(LARGEST_SUBNORMAL_FLOAT, boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = add(SMALLEST_NORMAL_FLOAT, boundary.shiftLeft(1)).divide(3);
        aeq(boundary.toFloat(), 1.17549435E-38);
        aeq(halfBelowBoundary.toFloat(), 1.1754942E-38);
        aeq(justBelowBoundary.toFloat(), 1.1754942E-38);
        aeq(halfAboveBoundary.toFloat(), 1.17549435E-38);
        aeq(justAboveBoundary.toFloat(), 1.17549435E-38);
    }

    @Test
    public void testToFloat_RoundingMode() {
        aeq(ZERO.toFloat(RoundingMode.FLOOR), 0.0);
        aeq(ZERO.toFloat(RoundingMode.CEILING), 0.0);
        aeq(ZERO.toFloat(RoundingMode.DOWN), 0.0);
        aeq(ZERO.toFloat(RoundingMode.UP), 0.0);
        aeq(ZERO.toFloat(RoundingMode.HALF_DOWN), 0.0);
        aeq(ZERO.toFloat(RoundingMode.HALF_UP), 0.0);
        aeq(ZERO.toFloat(RoundingMode.HALF_EVEN), 0.0);
        aeq(ZERO.toFloat(RoundingMode.UNNECESSARY), 0.0);
        aeq(ONE.toFloat(RoundingMode.FLOOR), 1.0);
        aeq(ONE.toFloat(RoundingMode.CEILING), 1.0);
        aeq(ONE.toFloat(RoundingMode.DOWN), 1.0);
        aeq(ONE.toFloat(RoundingMode.UP), 1.0);
        aeq(ONE.toFloat(RoundingMode.HALF_DOWN), 1.0);
        aeq(ONE.toFloat(RoundingMode.HALF_UP), 1.0);
        aeq(ONE.toFloat(RoundingMode.HALF_EVEN), 1.0);
        aeq(ONE.toFloat(RoundingMode.UNNECESSARY), 1.0);
        aeq(read("1/2").get().toFloat(RoundingMode.FLOOR), 0.5);
        aeq(read("1/2").get().toFloat(RoundingMode.CEILING), 0.5);
        aeq(read("1/2").get().toFloat(RoundingMode.DOWN), 0.5);
        aeq(read("1/2").get().toFloat(RoundingMode.UP), 0.5);
        aeq(read("1/2").get().toFloat(RoundingMode.HALF_DOWN), 0.5);
        aeq(read("1/2").get().toFloat(RoundingMode.HALF_UP), 0.5);
        aeq(read("1/2").get().toFloat(RoundingMode.HALF_EVEN), 0.5);
        aeq(read("1/2").get().toFloat(RoundingMode.UNNECESSARY), 0.5);
        aeq(read("1/3").get().toFloat(RoundingMode.FLOOR), 0.3333333);
        aeq(read("1/3").get().toFloat(RoundingMode.CEILING), 0.33333334);
        aeq(read("1/3").get().toFloat(RoundingMode.DOWN), 0.3333333);
        aeq(read("1/3").get().toFloat(RoundingMode.UP), 0.33333334);
        aeq(read("1/3").get().toFloat(RoundingMode.HALF_DOWN), 0.33333334);
        aeq(read("1/3").get().toFloat(RoundingMode.HALF_UP), 0.33333334);
        aeq(read("1/3").get().toFloat(RoundingMode.HALF_EVEN), 0.33333334);
        try {
            read("1/3").get().toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("-1/3").get().toFloat(RoundingMode.FLOOR), -0.33333334);
        aeq(read("-1/3").get().toFloat(RoundingMode.CEILING), -0.3333333);
        aeq(read("-1/3").get().toFloat(RoundingMode.DOWN), -0.3333333);
        aeq(read("-1/3").get().toFloat(RoundingMode.UP), -0.33333334);
        aeq(read("-1/3").get().toFloat(RoundingMode.HALF_DOWN), -0.33333334);
        aeq(read("-1/3").get().toFloat(RoundingMode.HALF_UP), -0.33333334);
        aeq(read("-1/3").get().toFloat(RoundingMode.HALF_EVEN), -0.33333334);
        try {
            read("-1/3").get().toFloat(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational almostOne = of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
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
        } catch (ArithmeticException ignored) {}
        Rational floatPi = of((float) Math.PI);
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
        Rational trillion = of(BigInteger.TEN.pow(12));
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
        } catch (ArithmeticException ignored) {}
        Rational piSuccessor = of(FloatUtils.successor((float) Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = of(FloatUtils.predecessor((float) Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = add(floatPi, piSuccessor).divide(2);
        Rational halfBelowPi = add(floatPi, piPredecessor).divide(2);
        Rational justAbovePi = add(floatPi.multiply(2), piSuccessor).divide(3);
        Rational justBelowPi = add(floatPi.multiply(2), piPredecessor).divide(3);
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
        Rational subnormal = of(1e-40f);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = of(FloatUtils.successor(1e-40f));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = of(FloatUtils.predecessor(1e-40f));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = add(subnormal, subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = add(subnormal, subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = add(subnormal.multiply(2), subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = add(subnormal.multiply(2), subnormalPredecessor).divide(3);
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
        Rational belowNegativeMax = subtract(LARGEST_FLOAT.negate(), ONE);
        Rational negativeMaxSuccessor = of(FloatUtils.successor(-Float.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = add(LARGEST_FLOAT.negate(), negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = add(
                LARGEST_FLOAT.negate().multiply(2),
                negativeMaxSuccessor
        ).divide(3);
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
        Rational aboveMax = add(LARGEST_FLOAT, ONE);
        Rational maxPredecessor = of(FloatUtils.predecessor(Float.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = add(LARGEST_FLOAT, maxPredecessor).divide(2);
        Rational justBelowMax = add(LARGEST_FLOAT.multiply(2), maxPredecessor).divide(3);
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
        Rational halfAboveZero = SMALLEST_FLOAT.shiftRight(1);
        Rational justAboveZero = SMALLEST_FLOAT.divide(3);
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
        Rational boundary = add(
                LARGEST_SUBNORMAL_FLOAT,
                SMALLEST_NORMAL_FLOAT
        ).shiftRight(1);
        Rational halfBelowBoundary = add(LARGEST_SUBNORMAL_FLOAT, boundary).shiftRight(1);
        Rational halfAboveBoundary = add(SMALLEST_NORMAL_FLOAT, boundary).shiftRight(1);
        Rational justBelowBoundary = add(LARGEST_SUBNORMAL_FLOAT, boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = add(SMALLEST_NORMAL_FLOAT, boundary.shiftLeft(1)).divide(3);
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testToDouble() {
        aeq(ZERO.toDouble(), 0.0);
        aeq(ONE.toDouble(), 1.0);
        aeq(read("1/2").get().toDouble(), 0.5);
        aeq(read("1/3").get().toDouble(), 0.3333333333333333);
        aeq(read("-1/3").get().toDouble(), -0.3333333333333333);
        Rational almostOne = of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
        aeq(almostOne.toDouble(), 1.0);
        Rational pi = of(Math.PI);
        if (pi == null) {
            fail();
        }
        aeq(pi.toDouble(), 3.141592653589793);
        Rational googol = of(BigInteger.TEN.pow(100));
        aeq(googol.toDouble(), 1.0E100);
        Rational piSuccessor = of(FloatUtils.successor(Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = of(FloatUtils.predecessor(Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = add(pi, piSuccessor).divide(2);
        Rational halfBelowPi = add(pi, piPredecessor).divide(2);
        Rational justAbovePi = add(pi.multiply(2), piSuccessor).divide(3);
        Rational justBelowPi = add(pi.multiply(2), piPredecessor).divide(3);
        aeq(halfAbovePi.toDouble(), 3.141592653589793);
        aeq(halfBelowPi.toDouble(), 3.141592653589793);
        aeq(justAbovePi.toDouble(), 3.141592653589793);
        aeq(justBelowPi.toDouble(), 3.141592653589793);
        Rational subnormal = of(1e-310);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = of(FloatUtils.successor(1e-310));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = of(FloatUtils.predecessor(1e-310));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = add(subnormal, subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = add(subnormal, subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = add(subnormal.multiply(2), subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = add(subnormal.multiply(2), subnormalPredecessor).divide(3);
        aeq(subnormal.toDouble(), 1.0E-310);
        aeq(halfAboveSubnormal.toDouble(), 1.00000000000005E-310);
        aeq(halfBelowSubnormal.toDouble(), 9.9999999999995E-311);
        aeq(justAboveSubnormal.toDouble(), 1.0E-310);
        aeq(justBelowSubnormal.toDouble(), 1.0E-310);
        Rational belowNegativeMax = subtract(LARGEST_DOUBLE.negate(), ONE);
        Rational negativeMaxSuccessor = of(FloatUtils.successor(-Double.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = add(LARGEST_DOUBLE.negate(), negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = add(
                LARGEST_DOUBLE.negate().multiply(2),
                negativeMaxSuccessor
        ).divide(3);
        aeq(belowNegativeMax.toDouble(), Double.NEGATIVE_INFINITY);
        aeq(halfAboveNegativeMax.toDouble(), -1.7976931348623155E308);
        aeq(justAboveNegativeMax.toDouble(), -1.7976931348623157E308);
        Rational aboveMax = add(LARGEST_DOUBLE, ONE);
        Rational maxPredecessor = of(FloatUtils.predecessor(Double.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = add(LARGEST_DOUBLE, maxPredecessor).divide(2);
        Rational justBelowMax = add(LARGEST_DOUBLE.multiply(2), maxPredecessor).divide(3);
        aeq(aboveMax.toDouble(), Double.POSITIVE_INFINITY);
        aeq(halfBelowMax.toDouble(), 1.7976931348623155E308);
        aeq(justBelowMax.toDouble(), 1.7976931348623157E308);
        Rational halfAboveZero = SMALLEST_DOUBLE.shiftRight(1);
        Rational justAboveZero = SMALLEST_DOUBLE.divide(3);
        Rational halfBelowZero = halfAboveZero.negate();
        Rational justBelowZero = justAboveZero.negate();
        aeq(halfBelowZero.toDouble(), -0.0);
        aeq(justBelowZero.toDouble(), -0.0);
        aeq(halfAboveZero.toDouble(), 0.0);
        aeq(justAboveZero.toDouble(), 0.0);
        Rational boundary = add(
                LARGEST_SUBNORMAL_DOUBLE,
                SMALLEST_NORMAL_DOUBLE
        ).shiftRight(1);
        Rational halfBelowBoundary = add(LARGEST_SUBNORMAL_DOUBLE, boundary).shiftRight(1);
        Rational halfAboveBoundary = add(SMALLEST_NORMAL_DOUBLE, boundary).shiftRight(1);
        Rational justBelowBoundary = add(LARGEST_SUBNORMAL_DOUBLE, boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = add(SMALLEST_NORMAL_DOUBLE, boundary.shiftLeft(1)).divide(3);
        aeq(boundary.toDouble(), 2.2250738585072014E-308);
        aeq(halfBelowBoundary.toDouble(), 2.225073858507201E-308);
        aeq(justBelowBoundary.toDouble(), 2.225073858507201E-308);
        aeq(halfAboveBoundary.toDouble(), 2.2250738585072014E-308);
        aeq(justAboveBoundary.toDouble(), 2.2250738585072014E-308);
    }

    @Test
    public void testToDouble_RoundingMode() {
        aeq(ZERO.toDouble(RoundingMode.FLOOR), 0.0);
        aeq(ZERO.toDouble(RoundingMode.CEILING), 0.0);
        aeq(ZERO.toDouble(RoundingMode.DOWN), 0.0);
        aeq(ZERO.toDouble(RoundingMode.UP), 0.0);
        aeq(ZERO.toDouble(RoundingMode.HALF_DOWN), 0.0);
        aeq(ZERO.toDouble(RoundingMode.HALF_UP), 0.0);
        aeq(ZERO.toDouble(RoundingMode.HALF_EVEN), 0.0);
        aeq(ZERO.toDouble(RoundingMode.UNNECESSARY), 0.0);
        aeq(ONE.toDouble(RoundingMode.FLOOR), 1.0);
        aeq(ONE.toDouble(RoundingMode.CEILING), 1.0);
        aeq(ONE.toDouble(RoundingMode.DOWN), 1.0);
        aeq(ONE.toDouble(RoundingMode.UP), 1.0);
        aeq(ONE.toDouble(RoundingMode.HALF_DOWN), 1.0);
        aeq(ONE.toDouble(RoundingMode.HALF_UP), 1.0);
        aeq(ONE.toDouble(RoundingMode.HALF_EVEN), 1.0);
        aeq(ONE.toDouble(RoundingMode.UNNECESSARY), 1.0);
        aeq(read("1/2").get().toDouble(RoundingMode.FLOOR), 0.5);
        aeq(read("1/2").get().toDouble(RoundingMode.CEILING), 0.5);
        aeq(read("1/2").get().toDouble(RoundingMode.DOWN), 0.5);
        aeq(read("1/2").get().toDouble(RoundingMode.UP), 0.5);
        aeq(read("1/2").get().toDouble(RoundingMode.HALF_DOWN), 0.5);
        aeq(read("1/2").get().toDouble(RoundingMode.HALF_UP), 0.5);
        aeq(read("1/2").get().toDouble(RoundingMode.HALF_EVEN), 0.5);
        aeq(read("1/2").get().toDouble(RoundingMode.UNNECESSARY), 0.5);
        aeq(read("1/3").get().toDouble(RoundingMode.FLOOR), 0.3333333333333333);
        aeq(read("1/3").get().toDouble(RoundingMode.CEILING), 0.33333333333333337);
        aeq(read("1/3").get().toDouble(RoundingMode.DOWN), 0.3333333333333333);
        aeq(read("1/3").get().toDouble(RoundingMode.UP), 0.33333333333333337);
        aeq(read("1/3").get().toDouble(RoundingMode.HALF_DOWN), 0.3333333333333333);
        aeq(read("1/3").get().toDouble(RoundingMode.HALF_UP), 0.3333333333333333);
        aeq(read("1/3").get().toDouble(RoundingMode.HALF_EVEN), 0.3333333333333333);
        try {
            read("1/3").get().toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("-1/3").get().toDouble(RoundingMode.FLOOR), -0.33333333333333337);
        aeq(read("-1/3").get().toDouble(RoundingMode.CEILING), -0.3333333333333333);
        aeq(read("-1/3").get().toDouble(RoundingMode.DOWN), -0.3333333333333333);
        aeq(read("-1/3").get().toDouble(RoundingMode.UP), -0.33333333333333337);
        aeq(read("-1/3").get().toDouble(RoundingMode.HALF_DOWN), -0.3333333333333333);
        aeq(read("-1/3").get().toDouble(RoundingMode.HALF_UP), -0.3333333333333333);
        aeq(read("-1/3").get().toDouble(RoundingMode.HALF_EVEN), -0.3333333333333333);
        try {
            read("-1/3").get().toDouble(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational almostOne = of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
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
        } catch (ArithmeticException ignored) {}
        Rational pi = of(Math.PI);
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
        Rational googol = of(BigInteger.TEN.pow(100));
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
        } catch (ArithmeticException ignored) {}
        Rational piSuccessor = of(FloatUtils.successor(Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = of(FloatUtils.predecessor(Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = add(pi, piSuccessor).divide(2);
        Rational halfBelowPi = add(pi, piPredecessor).divide(2);
        Rational justAbovePi = add(pi.multiply(2), piSuccessor).divide(3);
        Rational justBelowPi = add(pi.multiply(2), piPredecessor).divide(3);
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
        Rational subnormal = of(1e-310);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = of(FloatUtils.successor(1e-310));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = of(FloatUtils.predecessor(1e-310));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = add(subnormal, subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = add(subnormal, subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = add(subnormal.multiply(2), subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = add(subnormal.multiply(2), subnormalPredecessor).divide(3);
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
        Rational belowNegativeMax = subtract(LARGEST_DOUBLE.negate(), ONE);
        Rational negativeMaxSuccessor = of(FloatUtils.successor(-Double.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = add(LARGEST_DOUBLE.negate(), negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = add(
                LARGEST_DOUBLE.negate().multiply(2),
                negativeMaxSuccessor
        ).divide(3);
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
        Rational aboveMax = add(LARGEST_DOUBLE, ONE);
        Rational maxPredecessor = of(FloatUtils.predecessor(Double.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = add(LARGEST_DOUBLE, maxPredecessor).divide(2);
        Rational justBelowMax = add(LARGEST_DOUBLE.multiply(2), maxPredecessor).divide(3);
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
        Rational halfAboveZero = SMALLEST_DOUBLE.shiftRight(1);
        Rational justAboveZero = SMALLEST_DOUBLE.divide(3);
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
        Rational boundary = add(
                LARGEST_SUBNORMAL_DOUBLE,
                SMALLEST_NORMAL_DOUBLE
        ).shiftRight(1);
        Rational halfBelowBoundary = add(LARGEST_SUBNORMAL_DOUBLE, boundary).shiftRight(1);
        Rational halfAboveBoundary = add(SMALLEST_NORMAL_DOUBLE, boundary).shiftRight(1);
        Rational justBelowBoundary = add(LARGEST_SUBNORMAL_DOUBLE, boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = add(SMALLEST_NORMAL_DOUBLE, boundary.shiftLeft(1)).divide(3);
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
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
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testHasTerminatingDecimalExpansion() {
        assertTrue(ZERO.hasTerminatingDecimalExpansion());
        assertTrue(ONE.hasTerminatingDecimalExpansion());
        assertTrue(read("60").get().hasTerminatingDecimalExpansion());
        assertTrue(read("1/2").get().hasTerminatingDecimalExpansion());
        assertTrue(read("1/5").get().hasTerminatingDecimalExpansion());
        assertTrue(read("-7/100").get().hasTerminatingDecimalExpansion());
        assertTrue(read("-3/640").get().hasTerminatingDecimalExpansion());
        assertFalse(read("1/3").get().hasTerminatingDecimalExpansion());
        assertFalse(read("-1/15").get().hasTerminatingDecimalExpansion());
        assertFalse(read("-2/9").get().hasTerminatingDecimalExpansion());
    }

    @Test
    public void testToBigDecimal() {
        aeq(ZERO.toBigDecimal(), "0");
        aeq(ONE.toBigDecimal(), "1");
        aeq(read("-23").get().toBigDecimal(), "-23");
        aeq(read("4/5").get().toBigDecimal(), "0.8");
        aeq(read("1/64").get().toBigDecimal(), "0.015625");
        aeq(read("1234").get().toBigDecimal(), "1234");
        try {
            read("1/3").get().toBigDecimal();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testToBigDecimal_Precision() {
        aeq(ZERO.toBigDecimal(4), "0");
        aeq(ZERO.toBigDecimal(0), "0");
        aeq(ONE.toBigDecimal(4), "1");
        aeq(ONE.toBigDecimal(0), "1");
        aeq(read("1/2").get().toBigDecimal(0), "0.5");
        aeq(read("1/2").get().toBigDecimal(1), "0.5");
        aeq(read("1/2").get().toBigDecimal(4), "0.5");
        aeq(read("1/64").get().toBigDecimal(0), "0.015625");
        aeq(read("1/64").get().toBigDecimal(1), "0.02");
        aeq(read("1/64").get().toBigDecimal(2), "0.016");
        aeq(read("1/64").get().toBigDecimal(3), "0.0156");
        aeq(read("1/64").get().toBigDecimal(4), "0.01563");
        aeq(read("1/64").get().toBigDecimal(5), "0.015625");
        aeq(read("1/64").get().toBigDecimal(6), "0.015625");
        aeq(read("-1/3").get().toBigDecimal(1), "-0.3");
        aeq(read("-1/3").get().toBigDecimal(2), "-0.33");
        aeq(read("-1/3").get().toBigDecimal(3), "-0.333");
        aeq(read("-1/3").get().toBigDecimal(4), "-0.3333");
        aeq(read("6789").get().toBigDecimal(0), "6789");
        aeq(read("6789").get().toBigDecimal(1), "7E+3");
        aeq(read("6789").get().toBigDecimal(2), "6.8E+3");
        aeq(read("6789").get().toBigDecimal(3), "6.79E+3");
        aeq(read("6789").get().toBigDecimal(4), "6789");
        aeq(read("6789").get().toBigDecimal(5), "6789");
        try {
            read("-1/3").get().toBigDecimal(0);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("5").get().toBigDecimal(-1);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testToBigDecimal_Precision_RoundingMode() {
        aeq(ZERO.toBigDecimal(0, RoundingMode.UNNECESSARY), "0");
        aeq(ZERO.toBigDecimal(0, RoundingMode.FLOOR), "0");
        aeq(ZERO.toBigDecimal(0, RoundingMode.CEILING), "0");
        aeq(ZERO.toBigDecimal(0, RoundingMode.DOWN), "0");
        aeq(ZERO.toBigDecimal(0, RoundingMode.UP), "0");
        aeq(ZERO.toBigDecimal(0, RoundingMode.HALF_DOWN), "0");
        aeq(ZERO.toBigDecimal(0, RoundingMode.HALF_UP), "0");
        aeq(ZERO.toBigDecimal(0, RoundingMode.HALF_EVEN), "0");
        aeq(ZERO.toBigDecimal(4, RoundingMode.UNNECESSARY), "0");
        aeq(ZERO.toBigDecimal(4, RoundingMode.FLOOR), "0");
        aeq(ZERO.toBigDecimal(4, RoundingMode.CEILING), "0");
        aeq(ZERO.toBigDecimal(4, RoundingMode.DOWN), "0");
        aeq(ZERO.toBigDecimal(4, RoundingMode.UP), "0");
        aeq(ZERO.toBigDecimal(4, RoundingMode.HALF_DOWN), "0");
        aeq(ZERO.toBigDecimal(4, RoundingMode.HALF_UP), "0");
        aeq(ZERO.toBigDecimal(4, RoundingMode.HALF_EVEN), "0");
        aeq(ONE.toBigDecimal(0, RoundingMode.UNNECESSARY), "1");
        aeq(ONE.toBigDecimal(0, RoundingMode.FLOOR), "1");
        aeq(ONE.toBigDecimal(0, RoundingMode.CEILING), "1");
        aeq(ONE.toBigDecimal(0, RoundingMode.DOWN), "1");
        aeq(ONE.toBigDecimal(0, RoundingMode.UP), "1");
        aeq(ONE.toBigDecimal(0, RoundingMode.HALF_DOWN), "1");
        aeq(ONE.toBigDecimal(0, RoundingMode.HALF_UP), "1");
        aeq(ONE.toBigDecimal(0, RoundingMode.HALF_EVEN), "1");
        aeq(ONE.toBigDecimal(4, RoundingMode.UNNECESSARY), "1");
        aeq(ONE.toBigDecimal(4, RoundingMode.FLOOR), "1");
        aeq(ONE.toBigDecimal(4, RoundingMode.CEILING), "1");
        aeq(ONE.toBigDecimal(4, RoundingMode.DOWN), "1");
        aeq(ONE.toBigDecimal(4, RoundingMode.UP), "1");
        aeq(ONE.toBigDecimal(4, RoundingMode.HALF_DOWN), "1");
        aeq(ONE.toBigDecimal(4, RoundingMode.HALF_UP), "1");
        aeq(ONE.toBigDecimal(4, RoundingMode.HALF_EVEN), "1");
        aeq(read("1/2").get().toBigDecimal(0, RoundingMode.UNNECESSARY), "0.5");
        aeq(read("1/2").get().toBigDecimal(0, RoundingMode.FLOOR), "0.5");
        aeq(read("1/2").get().toBigDecimal(0, RoundingMode.CEILING), "0.5");
        aeq(read("1/2").get().toBigDecimal(0, RoundingMode.DOWN), "0.5");
        aeq(read("1/2").get().toBigDecimal(0, RoundingMode.UP), "0.5");
        aeq(read("1/2").get().toBigDecimal(0, RoundingMode.HALF_DOWN), "0.5");
        aeq(read("1/2").get().toBigDecimal(0, RoundingMode.HALF_UP), "0.5");
        aeq(read("1/2").get().toBigDecimal(0, RoundingMode.HALF_EVEN), "0.5");
        aeq(read("1/2").get().toBigDecimal(1, RoundingMode.UNNECESSARY), "0.5");
        aeq(read("1/2").get().toBigDecimal(1, RoundingMode.FLOOR), "0.5");
        aeq(read("1/2").get().toBigDecimal(1, RoundingMode.CEILING), "0.5");
        aeq(read("1/2").get().toBigDecimal(1, RoundingMode.DOWN), "0.5");
        aeq(read("1/2").get().toBigDecimal(1, RoundingMode.UP), "0.5");
        aeq(read("1/2").get().toBigDecimal(1, RoundingMode.HALF_DOWN), "0.5");
        aeq(read("1/2").get().toBigDecimal(1, RoundingMode.HALF_UP), "0.5");
        aeq(read("1/2").get().toBigDecimal(1, RoundingMode.HALF_EVEN), "0.5");
        aeq(read("1/2").get().toBigDecimal(4, RoundingMode.UNNECESSARY), "0.5");
        aeq(read("1/2").get().toBigDecimal(4, RoundingMode.FLOOR), "0.5");
        aeq(read("1/2").get().toBigDecimal(4, RoundingMode.CEILING), "0.5");
        aeq(read("1/2").get().toBigDecimal(4, RoundingMode.DOWN), "0.5");
        aeq(read("1/2").get().toBigDecimal(4, RoundingMode.UP), "0.5");
        aeq(read("1/2").get().toBigDecimal(4, RoundingMode.HALF_DOWN), "0.5");
        aeq(read("1/2").get().toBigDecimal(4, RoundingMode.HALF_UP), "0.5");
        aeq(read("1/2").get().toBigDecimal(4, RoundingMode.HALF_EVEN), "0.5");
        aeq(read("1/64").get().toBigDecimal(0, RoundingMode.UNNECESSARY), "0.015625");
        aeq(read("1/64").get().toBigDecimal(0, RoundingMode.FLOOR), "0.015625");
        aeq(read("1/64").get().toBigDecimal(0, RoundingMode.CEILING), "0.015625");
        aeq(read("1/64").get().toBigDecimal(0, RoundingMode.DOWN), "0.015625");
        aeq(read("1/64").get().toBigDecimal(0, RoundingMode.UP), "0.015625");
        aeq(read("1/64").get().toBigDecimal(0, RoundingMode.HALF_DOWN), "0.015625");
        aeq(read("1/64").get().toBigDecimal(0, RoundingMode.HALF_UP), "0.015625");
        aeq(read("1/64").get().toBigDecimal(0, RoundingMode.HALF_EVEN), "0.015625");
        try {
            read("1/64").get().toBigDecimal(1, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("1/64").get().toBigDecimal(1, RoundingMode.FLOOR), "0.01");
        aeq(read("1/64").get().toBigDecimal(1, RoundingMode.CEILING), "0.02");
        aeq(read("1/64").get().toBigDecimal(1, RoundingMode.DOWN), "0.01");
        aeq(read("1/64").get().toBigDecimal(1, RoundingMode.UP), "0.02");
        aeq(read("1/64").get().toBigDecimal(1, RoundingMode.HALF_DOWN), "0.02");
        aeq(read("1/64").get().toBigDecimal(1, RoundingMode.HALF_UP), "0.02");
        aeq(read("1/64").get().toBigDecimal(1, RoundingMode.HALF_EVEN), "0.02");
        try {
            read("1/64").get().toBigDecimal(2, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("1/64").get().toBigDecimal(2, RoundingMode.FLOOR), "0.015");
        aeq(read("1/64").get().toBigDecimal(2, RoundingMode.CEILING), "0.016");
        aeq(read("1/64").get().toBigDecimal(2, RoundingMode.DOWN), "0.015");
        aeq(read("1/64").get().toBigDecimal(2, RoundingMode.UP), "0.016");
        aeq(read("1/64").get().toBigDecimal(2, RoundingMode.HALF_DOWN), "0.016");
        aeq(read("1/64").get().toBigDecimal(2, RoundingMode.HALF_UP), "0.016");
        aeq(read("1/64").get().toBigDecimal(2, RoundingMode.HALF_EVEN), "0.016");
        try {
            read("1/64").get().toBigDecimal(3, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("1/64").get().toBigDecimal(3, RoundingMode.FLOOR), "0.0156");
        aeq(read("1/64").get().toBigDecimal(3, RoundingMode.CEILING), "0.0157");
        aeq(read("1/64").get().toBigDecimal(3, RoundingMode.DOWN), "0.0156");
        aeq(read("1/64").get().toBigDecimal(3, RoundingMode.UP), "0.0157");
        aeq(read("1/64").get().toBigDecimal(3, RoundingMode.HALF_DOWN), "0.0156");
        aeq(read("1/64").get().toBigDecimal(3, RoundingMode.HALF_UP), "0.0156");
        aeq(read("1/64").get().toBigDecimal(3, RoundingMode.HALF_EVEN), "0.0156");
        try {
            read("1/64").get().toBigDecimal(4, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("1/64").get().toBigDecimal(4, RoundingMode.FLOOR), "0.01562");
        aeq(read("1/64").get().toBigDecimal(4, RoundingMode.CEILING), "0.01563");
        aeq(read("1/64").get().toBigDecimal(4, RoundingMode.DOWN), "0.01562");
        aeq(read("1/64").get().toBigDecimal(4, RoundingMode.UP), "0.01563");
        aeq(read("1/64").get().toBigDecimal(4, RoundingMode.HALF_DOWN), "0.01562");
        aeq(read("1/64").get().toBigDecimal(4, RoundingMode.HALF_UP), "0.01563");
        aeq(read("1/64").get().toBigDecimal(4, RoundingMode.HALF_EVEN), "0.01562");
        aeq(read("1/64").get().toBigDecimal(5, RoundingMode.UNNECESSARY), "0.015625");
        aeq(read("1/64").get().toBigDecimal(5, RoundingMode.FLOOR), "0.015625");
        aeq(read("1/64").get().toBigDecimal(5, RoundingMode.CEILING), "0.015625");
        aeq(read("1/64").get().toBigDecimal(5, RoundingMode.DOWN), "0.015625");
        aeq(read("1/64").get().toBigDecimal(5, RoundingMode.UP), "0.015625");
        aeq(read("1/64").get().toBigDecimal(5, RoundingMode.HALF_DOWN), "0.015625");
        aeq(read("1/64").get().toBigDecimal(5, RoundingMode.HALF_UP), "0.015625");
        aeq(read("1/64").get().toBigDecimal(5, RoundingMode.HALF_EVEN), "0.015625");
        aeq(read("1/64").get().toBigDecimal(6, RoundingMode.UNNECESSARY), "0.015625");
        aeq(read("1/64").get().toBigDecimal(6, RoundingMode.FLOOR), "0.015625");
        aeq(read("1/64").get().toBigDecimal(6, RoundingMode.CEILING), "0.015625");
        aeq(read("1/64").get().toBigDecimal(6, RoundingMode.DOWN), "0.015625");
        aeq(read("1/64").get().toBigDecimal(6, RoundingMode.UP), "0.015625");
        aeq(read("1/64").get().toBigDecimal(6, RoundingMode.HALF_DOWN), "0.015625");
        aeq(read("1/64").get().toBigDecimal(6, RoundingMode.HALF_UP), "0.015625");
        aeq(read("1/64").get().toBigDecimal(6, RoundingMode.HALF_EVEN), "0.015625");
        try {
            read("-1/3").get().toBigDecimal(1, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("-1/3").get().toBigDecimal(1, RoundingMode.FLOOR), "-0.4");
        aeq(read("-1/3").get().toBigDecimal(1, RoundingMode.CEILING), "-0.3");
        aeq(read("-1/3").get().toBigDecimal(1, RoundingMode.DOWN), "-0.3");
        aeq(read("-1/3").get().toBigDecimal(1, RoundingMode.UP), "-0.4");
        aeq(read("-1/3").get().toBigDecimal(1, RoundingMode.HALF_DOWN), "-0.3");
        aeq(read("-1/3").get().toBigDecimal(1, RoundingMode.HALF_UP), "-0.3");
        aeq(read("-1/3").get().toBigDecimal(1, RoundingMode.HALF_EVEN), "-0.3");
        try {
            read("-1/3").get().toBigDecimal(2, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("-1/3").get().toBigDecimal(2, RoundingMode.FLOOR), "-0.34");
        aeq(read("-1/3").get().toBigDecimal(2, RoundingMode.CEILING), "-0.33");
        aeq(read("-1/3").get().toBigDecimal(2, RoundingMode.DOWN), "-0.33");
        aeq(read("-1/3").get().toBigDecimal(2, RoundingMode.UP), "-0.34");
        aeq(read("-1/3").get().toBigDecimal(2, RoundingMode.HALF_DOWN), "-0.33");
        aeq(read("-1/3").get().toBigDecimal(2, RoundingMode.HALF_UP), "-0.33");
        aeq(read("-1/3").get().toBigDecimal(2, RoundingMode.HALF_EVEN), "-0.33");
        try {
            read("-1/3").get().toBigDecimal(3, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("-1/3").get().toBigDecimal(3, RoundingMode.FLOOR), "-0.334");
        aeq(read("-1/3").get().toBigDecimal(3, RoundingMode.CEILING), "-0.333");
        aeq(read("-1/3").get().toBigDecimal(3, RoundingMode.DOWN), "-0.333");
        aeq(read("-1/3").get().toBigDecimal(3, RoundingMode.UP), "-0.334");
        aeq(read("-1/3").get().toBigDecimal(3, RoundingMode.HALF_DOWN), "-0.333");
        aeq(read("-1/3").get().toBigDecimal(3, RoundingMode.HALF_UP), "-0.333");
        aeq(read("-1/3").get().toBigDecimal(3, RoundingMode.HALF_EVEN), "-0.333");
        try {
            read("-1/3").get().toBigDecimal(4, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("-1/3").get().toBigDecimal(4, RoundingMode.FLOOR), "-0.3334");
        aeq(read("-1/3").get().toBigDecimal(4, RoundingMode.CEILING), "-0.3333");
        aeq(read("-1/3").get().toBigDecimal(4, RoundingMode.DOWN), "-0.3333");
        aeq(read("-1/3").get().toBigDecimal(4, RoundingMode.UP), "-0.3334");
        aeq(read("-1/3").get().toBigDecimal(4, RoundingMode.HALF_DOWN), "-0.3333");
        aeq(read("-1/3").get().toBigDecimal(4, RoundingMode.HALF_UP), "-0.3333");
        aeq(read("-1/3").get().toBigDecimal(4, RoundingMode.HALF_EVEN), "-0.3333");
        aeq(read("6789").get().toBigDecimal(0, RoundingMode.FLOOR), "6789");
        aeq(read("6789").get().toBigDecimal(0, RoundingMode.CEILING), "6789");
        aeq(read("6789").get().toBigDecimal(0, RoundingMode.DOWN), "6789");
        aeq(read("6789").get().toBigDecimal(0, RoundingMode.UP), "6789");
        aeq(read("6789").get().toBigDecimal(0, RoundingMode.HALF_DOWN), "6789");
        aeq(read("6789").get().toBigDecimal(0, RoundingMode.HALF_UP), "6789");
        aeq(read("6789").get().toBigDecimal(0, RoundingMode.HALF_EVEN), "6789");
        try {
            read("6789").get().toBigDecimal(1, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("6789").get().toBigDecimal(1, RoundingMode.FLOOR), "6E+3");
        aeq(read("6789").get().toBigDecimal(1, RoundingMode.CEILING), "7E+3");
        aeq(read("6789").get().toBigDecimal(1, RoundingMode.DOWN), "6E+3");
        aeq(read("6789").get().toBigDecimal(1, RoundingMode.UP), "7E+3");
        aeq(read("6789").get().toBigDecimal(1, RoundingMode.HALF_DOWN), "7E+3");
        aeq(read("6789").get().toBigDecimal(1, RoundingMode.HALF_UP), "7E+3");
        aeq(read("6789").get().toBigDecimal(1, RoundingMode.HALF_EVEN), "7E+3");
        try {
            read("6789").get().toBigDecimal(2, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("6789").get().toBigDecimal(2, RoundingMode.FLOOR), "6.7E+3");
        aeq(read("6789").get().toBigDecimal(2, RoundingMode.CEILING), "6.8E+3");
        aeq(read("6789").get().toBigDecimal(2, RoundingMode.DOWN), "6.7E+3");
        aeq(read("6789").get().toBigDecimal(2, RoundingMode.UP), "6.8E+3");
        aeq(read("6789").get().toBigDecimal(2, RoundingMode.HALF_DOWN), "6.8E+3");
        aeq(read("6789").get().toBigDecimal(2, RoundingMode.HALF_UP), "6.8E+3");
        aeq(read("6789").get().toBigDecimal(2, RoundingMode.HALF_EVEN), "6.8E+3");
        try {
            read("6789").get().toBigDecimal(3, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("6789").get().toBigDecimal(3, RoundingMode.FLOOR), "6.78E+3");
        aeq(read("6789").get().toBigDecimal(3, RoundingMode.CEILING), "6.79E+3");
        aeq(read("6789").get().toBigDecimal(3, RoundingMode.DOWN), "6.78E+3");
        aeq(read("6789").get().toBigDecimal(3, RoundingMode.UP), "6.79E+3");
        aeq(read("6789").get().toBigDecimal(3, RoundingMode.HALF_DOWN), "6.79E+3");
        aeq(read("6789").get().toBigDecimal(3, RoundingMode.HALF_UP), "6.79E+3");
        aeq(read("6789").get().toBigDecimal(3, RoundingMode.HALF_EVEN), "6.79E+3");
        aeq(read("6789").get().toBigDecimal(4, RoundingMode.UNNECESSARY), "6789");
        aeq(read("6789").get().toBigDecimal(4, RoundingMode.FLOOR), "6789");
        aeq(read("6789").get().toBigDecimal(4, RoundingMode.CEILING), "6789");
        aeq(read("6789").get().toBigDecimal(4, RoundingMode.DOWN), "6789");
        aeq(read("6789").get().toBigDecimal(4, RoundingMode.UP), "6789");
        aeq(read("6789").get().toBigDecimal(4, RoundingMode.HALF_DOWN), "6789");
        aeq(read("6789").get().toBigDecimal(4, RoundingMode.HALF_UP), "6789");
        aeq(read("6789").get().toBigDecimal(4, RoundingMode.HALF_EVEN), "6789");
        aeq(read("6789").get().toBigDecimal(5, RoundingMode.UNNECESSARY), "6789");
        aeq(read("6789").get().toBigDecimal(5, RoundingMode.FLOOR), "6789");
        aeq(read("6789").get().toBigDecimal(5, RoundingMode.CEILING), "6789");
        aeq(read("6789").get().toBigDecimal(5, RoundingMode.DOWN), "6789");
        aeq(read("6789").get().toBigDecimal(5, RoundingMode.UP), "6789");
        aeq(read("6789").get().toBigDecimal(5, RoundingMode.HALF_DOWN), "6789");
        aeq(read("6789").get().toBigDecimal(5, RoundingMode.HALF_UP), "6789");
        aeq(read("6789").get().toBigDecimal(5, RoundingMode.HALF_EVEN), "6789");
        try {
            read("5").get().toBigDecimal(-1, RoundingMode.UNNECESSARY);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().toBigDecimal(-1, RoundingMode.FLOOR);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().toBigDecimal(-1, RoundingMode.CEILING);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().toBigDecimal(-1, RoundingMode.DOWN);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().toBigDecimal(-1, RoundingMode.UP);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().toBigDecimal(-1, RoundingMode.HALF_DOWN);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().toBigDecimal(-1, RoundingMode.HALF_UP);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().toBigDecimal(-1, RoundingMode.HALF_EVEN);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testEquals() {
        assertTrue(ZERO.equals(ZERO));
        assertTrue(ONE.equals(ONE));
        assertTrue(read("4").get().equals(read("4").get()));
        assertTrue(read("-4").get().equals(read("-4").get()));
        assertTrue(read("5/12").get().equals(read("5/12").get()));
        assertTrue(read("-5/12").get().equals(read("-5/12").get()));
        assertFalse(ZERO.equals(ONE));
        assertFalse(ONE.equals(ZERO));
        assertFalse(ZERO.equals(read("4").get()));
        assertFalse(ZERO.equals(read("-4").get()));
        assertFalse(ZERO.equals(read("5/12").get()));
        assertFalse(ZERO.equals(read("-5/12").get()));
        assertFalse(ONE.equals(read("4").get()));
        assertFalse(ONE.equals(read("-4").get()));
        assertFalse(ONE.equals(read("5/12").get()));
        assertFalse(ONE.equals(read("-5/12").get()));
        assertFalse(read("4").get().equals(ZERO));
        assertFalse(read("-4").get().equals(ZERO));
        assertFalse(read("5/12").get().equals(ZERO));
        assertFalse(read("-5/12").get().equals(ZERO));
        assertFalse(read("4").get().equals(ONE));
        assertFalse(read("-4").get().equals(ONE));
        assertFalse(read("5/12").get().equals(ONE));
        assertFalse(read("-5/12").get().equals(ONE));
        assertFalse(read("4").equals(read("-4")));
        assertFalse(read("4").equals(read("5/12")));
        assertFalse(read("4").equals(read("-5/12")));
        assertFalse(read("-4").equals(read("4")));
        assertFalse(read("-4").equals(read("5/12")));
        assertFalse(read("-4").equals(read("-5/12")));
        assertFalse(read("5/12").equals(read("4")));
        assertFalse(read("5/12").equals(read("-4")));
        assertFalse(read("5/12").equals(read("-5/12")));
        assertFalse(read("-5/12").equals(read("4")));
        assertFalse(read("-5/12").equals(read("-4")));
        assertFalse(read("-5/12").equals(read("5/12")));
        assertTrue(read("4").isPresent());
        assertTrue(read("-4").isPresent());
        assertTrue(read("5/12").isPresent());
        assertTrue(read("-5/12").isPresent());
        assertTrue(read("-5/12").isPresent());
        assertTrue(read("-5/12").isPresent());
        assertTrue(read("-5/12").isPresent());
    }

    @Test
    public void testHashCode() {
        aeq(ZERO.hashCode(), 1);
        aeq(ONE.hashCode(), 32);
        aeq(read("4").hashCode(), 125);
        aeq(read("-4").hashCode(), -123);
        aeq(read("5/12").hashCode(), 167);
        aeq(read("-5/12").hashCode(), -143);
    }

    @Test
    public void testCompareTo() {
        assertTrue(eq(ZERO, ZERO));
        assertTrue(eq(ONE, ONE));
        assertTrue(eq(read("4").get(), read("4").get()));
        assertTrue(eq(read("-4").get(), read("-4").get()));
        assertTrue(eq(read("5/12").get(), read("5/12").get()));
        assertTrue(eq(read("-5/12").get(), read("-5/12").get()));
        assertTrue(lt(ZERO, ONE));
        assertTrue(gt(ONE, ZERO));
        assertTrue(lt(ZERO, read("4").get()));
        assertTrue(gt(ZERO, read("-4").get()));
        assertTrue(lt(ZERO, read("5/12").get()));
        assertTrue(gt(ZERO, read("-5/12").get()));
        assertTrue(lt(ONE, read("4").get()));
        assertTrue(gt(ONE, read("-4").get()));
        assertTrue(gt(ONE, read("5/12").get()));
        assertTrue(gt(ONE, read("-5/12").get()));
        assertTrue(gt(read("4").get(), ZERO));
        assertTrue(lt(read("-4").get(), ZERO));
        assertTrue(gt(read("5/12").get(), ZERO));
        assertTrue(lt(read("-5/12").get(), ZERO));
        assertTrue(gt(read("4").get(), ONE));
        assertTrue(lt(read("-4").get(), ONE));
        assertTrue(lt(read("5/12").get(), ONE));
        assertTrue(lt(read("-5/12").get(), ONE));
        assertTrue(gt(read("4").get(), read("-4").get()));
        assertTrue(gt(read("4").get(), read("5/12").get()));
        assertTrue(gt(read("4").get(), read("-5/12").get()));
        assertTrue(lt(read("-4").get(), read("4").get()));
        assertTrue(lt(read("-4").get(), read("5/12").get()));
        assertTrue(lt(read("-4").get(), read("-5/12").get()));
        assertTrue(lt(read("5/12").get(), read("4").get()));
        assertTrue(gt(read("5/12").get(), read("-4").get()));
        assertTrue(gt(read("5/12").get(), read("-5/12").get()));
        assertTrue(lt(read("-5/12").get(), read("4").get()));
        assertTrue(gt(read("-5/12").get(), read("-4").get()));
        assertTrue(lt(read("-5/12").get(), read("5/12").get()));
    }

    @Test
    public void testRead() {
        assertTrue(read("0").get() == ZERO);
        assertTrue(read("1").get() == ONE);
        aeq(read("3").get(), Rational.of(3));
        aeq(read("-3").get(), Rational.of(-3));
        aeq(read("5/12").get(), Rational.of(5, 12));
        aeq(read("-5/12").get(), Rational.of(-5, 12));
        aeq(read("12/6").get(), Rational.of(2));
        aeq(read("12/-6").get(), Rational.of(-2));
        aeq(read("6/8").get(), Rational.of(3, 4));
        aeq(read("-4/1").get(), Rational.of(-4));
        try {
            read("2/0");
            fail();
        } catch (ArithmeticException ignored) {}
        assertFalse(read("").isPresent());
        assertFalse(read(" ").isPresent());
        assertFalse(read("1 ").isPresent());
        assertFalse(read("01").isPresent());
        assertFalse(read("-").isPresent());
        assertFalse(read("-0").isPresent());
        assertFalse(read("--5").isPresent());
        assertFalse(read("0.1").isPresent());
        assertFalse(read("/1").isPresent());
        assertFalse(read("/").isPresent());
        assertFalse(read("2//3").isPresent());
        assertFalse(read("2/").isPresent());
        assertFalse(read("2 /3").isPresent());
        assertFalse(read("2/ 3").isPresent());
        assertFalse(read("2 / 3").isPresent());
        assertFalse(read("a").isPresent());
        assertFalse(read("2-3").isPresent());
        assertFalse(read("0x12").isPresent());
        assertFalse(read("12/3a").isPresent());
    }

    @Test
    public void testToString() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
        aeq(Rational.of(4), "4");
        aeq(Rational.of(-4), "-4");
        aeq(Rational.of(2, 5), "2/5");
        aeq(Rational.of(-2, 5), "-2/5");
    }

    private static void aeq(Iterable<?> a, Object b) {
        assertEquals(IterableUtils.toString(a), b.toString());
    }

    private static void aeq(Object a, Object b) {
        assertEquals(a.toString(), b.toString());
    }
}
