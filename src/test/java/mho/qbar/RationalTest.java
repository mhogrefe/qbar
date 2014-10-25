package mho.qbar;

import mho.haskellesque.iterables.IterableUtils;
import mho.haskellesque.iterables.RandomProvider;
import mho.haskellesque.numbers.Numbers;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;

import static mho.haskellesque.iterables.IterableUtils.take;
import static mho.haskellesque.ordering.Ordering.*;
import static mho.qbar.Rational.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class RationalTest {
    private @NotNull RandomProvider P;

    @Before
    public void initialize() {
        P = new RandomProvider(new Random(-1781048559808000493L));
    }

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
    public void testOf_Int_Int() {
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
    public void testOf_Int() {
        aeq(of(23), "23");
        aeq(of(-23), "-23");
        assertTrue(of(0) == ZERO);
        assertTrue(of(1) == ONE);
    }

    @Test
    public void testOf_Float() {
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
    public void testOf_Double() {
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
    public void multiply_Int() {
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
    public void divide_Int() {
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
        Rational piSuccessor = of(Numbers.successor((float) Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = of(Numbers.predecessor((float) Math.PI));
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
        Rational subnormalSuccessor = of(Numbers.successor(1e-40f));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = of(Numbers.predecessor(1e-40f));
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
        Rational negativeMaxSuccessor = of(Numbers.successor(-Float.MAX_VALUE));
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
        Rational maxPredecessor = of(Numbers.predecessor(Float.MAX_VALUE));
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
        Rational piSuccessor = of(Numbers.successor((float) Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = of(Numbers.predecessor((float) Math.PI));
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
        Rational subnormalSuccessor = of(Numbers.successor(1e-40f));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = of(Numbers.predecessor(1e-40f));
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
        Rational negativeMaxSuccessor = of(Numbers.successor(-Float.MAX_VALUE));
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
        Rational maxPredecessor = of(Numbers.predecessor(Float.MAX_VALUE));
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
        Rational piSuccessor = of(Numbers.successor(Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = of(Numbers.predecessor(Math.PI));
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
        Rational subnormalSuccessor = of(Numbers.successor(1e-310));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = of(Numbers.predecessor(1e-310));
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
        Rational negativeMaxSuccessor = of(Numbers.successor(-Double.MAX_VALUE));
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
        Rational maxPredecessor = of(Numbers.predecessor(Double.MAX_VALUE));
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
        Rational piSuccessor = of(Numbers.successor(Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = of(Numbers.predecessor(Math.PI));
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
        Rational subnormalSuccessor = of(Numbers.successor(1e-310));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = of(Numbers.predecessor(1e-310));
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
        Rational negativeMaxSuccessor = of(Numbers.successor(-Double.MAX_VALUE));
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
        Rational maxPredecessor = of(Numbers.predecessor(Double.MAX_VALUE));
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
        aeq(ZERO.compareTo(ZERO), 0);
        aeq(ONE.compareTo(ONE), 0);
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
        aeq(read("3").get(), "3");
        aeq(read("-3").get(), "-3");
        aeq(read("5/12").get(), "5/12");
        aeq(read("-5/12").get(), "-5/12");
        aeq(read("12/6").get(), "2");
        aeq(read("12/-6").get(), "-2");
        aeq(read("6/8").get(), "3/4");
        aeq(read("-4/1").get(), "-4");
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
        aeq(read("4").get(), "4");
        aeq(read("-4").get(), "-4");
        aeq(read("2/5").get(), "2/5");
        aeq(read("-2/5").get(), "-2/5");
    }

    @Test
    public void testRationals() {
        aeq(take(50, RATIONALS),
                "[0, 1, 1/2, 1/3, 1/4, -1, -1/2, 2, -1/3, -1/4, 2/3, 1/5, 1/6, 1/7, 1/8, -1/5, -1/6, 2/5, -1/7," +
                " -1/8, 2/7, -2, 3, 3/2, -2/3, 3/4, -3, -3/2, 4, -3/4, 4/3, -2/5, 3/5, -2/7, 3/7, 3/8, -3/5, 4/5," +
                " -3/7, -3/8, 4/7, 1/9, 1/10, 1/11, 1/12, -1/9, -1/10, 2/9, -1/11, -1/12]");
    }

    @Test
    public void testNonNegativeRationals() {
        aeq(take(50, NON_NEGATIVE_RATIONALS),
                "[0, 1, 1/2, 1/3, 1/4, 2, 3, 3/2, 2/3, 3/4, 1/5, 1/6, 1/7, 1/8, 2/5, 3/5, 2/7, 3/7, 3/8, 4, 5, 5/2," +
                " 4/3, 5/3, 5/4, 6, 7, 7/2, 7/3, 7/4, 4/5, 5/6, 4/7, 5/7, 5/8, 6/5, 7/5, 7/6, 6/7, 7/8, 1/9, 1/10," +
                " 1/11, 1/12, 2/9, 3/10, 2/11, 3/11, 1/13, 1/14]");
    }

    @Test
    public void testPositiveRationals() {
        aeq(take(50, POSITIVE_RATIONALS),
                "[1, 1/2, 2, 1/3, 1/4, 2/3, 3, 3/2, 4, 3/4, 4/3, 1/5, 1/6, 2/5, 1/7, 1/8, 2/7, 3/5, 4/5, 3/7, 3/8," +
                " 4/7, 5, 5/2, 6, 5/3, 5/4, 7, 7/2, 8, 7/3, 7/4, 8/3, 5/6, 6/5, 5/7, 5/8, 6/7, 7/5, 7/6, 8/5, 7/8," +
                " 8/7, 1/9, 1/10, 2/9, 1/11, 1/12, 2/11, 3/10]");
    }

    @Test
    public void testNegativeRationals() {
        aeq(take(50, NEGATIVE_RATIONALS),
                "[-1, -1/2, -2, -1/3, -1/4, -2/3, -3, -3/2, -4, -3/4, -4/3, -1/5, -1/6, -2/5, -1/7, -1/8, -2/7," +
                " -3/5, -4/5, -3/7, -3/8, -4/7, -5, -5/2, -6, -5/3, -5/4, -7, -7/2, -8, -7/3, -7/4, -8/3, -5/6," +
                " -6/5, -5/7, -5/8, -6/7, -7/5, -7/6, -8/5, -7/8, -8/7, -1/9, -1/10, -2/9, -1/11, -1/12, -2/11," +
                " -3/10]");
    }

    @Test
    public void testNonNegativeRationalsLessThanOne() {
        aeq(take(50, NONNEGATIVE_RATIONALS_LESS_THAN_ONE),
                "[0, 1/2, 1/3, 1/4, 2/3, 3/4, 1/5, 1/6, 1/7, 1/8, 2/5, 3/5, 2/7, 3/7, 3/8, 4/5, 5/6, 4/7, 5/7, 5/8," +
                " 6/7, 7/8, 1/9, 1/10, 1/11, 1/12, 2/9, 3/10, 2/11, 3/11, 1/13, 1/14, 1/15, 1/16, 2/13, 3/13, 3/14," +
                " 2/15, 3/16, 4/9, 5/9, 4/11, 5/11, 5/12, 7/9, 7/10, 6/11, 7/11, 7/12, 4/13]");
    }

    @Test
    public void testRandomRationals_Int() {
        aeq(take(20, randomRationals(P, 3)),
                "[7, 1, 0, -1/8, -1, -2/3, -1, -5, 0, 1/3, 1, -2/3, -1/29, 4, -1/2, -12, -1, 0, -8/13, -2/7]");
        aeq(take(20, randomRationals(P, 4)),
                "[0, 13, 12/37, -1/14, 30, -2, 1/5, -887/21, 120/43, -1," +
                " -1, -1, -1/2, -1/8, 17/3, -1/14, 2/3, 0, -2, -1/3]");
        aeq(take(20, randomRationals(P, 5)),
                "[-1/6, -1/7, 27, 0, 23/3, -42, 58/9, -1, -1/100, -1/7," +
                " -1/36, -18/479, -1/2, -1, -1/7, 1/71, 0, -3029/228, 12/5, -1/2]");
        aeq(take(20, randomRationals(P, 10)),
                "[20/1327, -1/4964, -89/3, 11/12896222, -37/3257784, 1042026, -1/55, 1192/255, -5/16," +
                " -165510/454753, -1/4, -3752/3, 5/141, 36991, -27338/1397, 47/17546, -2727, -3/5987, 3, 1/7827]");
        aeq(take(20, randomRationals(P, 100)),
                "[7794647805636643454127718981794986731065179168717528319/804103074140120138245329048569086589871948" +
                "685327066372930328248598," +
                " 221570096366670650938512248490/21354527, -15110369239612982353183412694720788925915864165626940238" +
                "48857461438/16606612020383430259242013252751366334858153234716250267454912171," +
                " 50527/1079392977429," +
                " 31098440355702495324052966597287045395071806778002185888479039114438825272794203116481846428077989" +
                "28518611169350444709/178844821276309668541370465341732933396848349320," +
                " -9/73095306738151210803875896447948946641716559617846137522," +
                " -861237387630774251/188814504643444287, 99118670653/1730624552886962413266034936090230188," +
                " -5073913952727835324043438123/16218, 7119112/1377480333757833001641511607," +
                " -16/1460134460914060035822172621127485600334216863307," +
                " -354095404790544114789/36605356388967961637585244419199865807175472282808753867414877," +
                " 6017145870466733640000745183718128/486969463948452063949193282045755," +
                " 22896954610199180446177012264928613415538771406340054726801714578110220887130293718310994198432749" +
                "59386456/281102221," +
                " -72477291133881/45383442686067190747676017901898072429241146226181812379453000," +
                " 17108/89022452291343406294138495824117618001145271085483969981853006421436737731802415768883651282" +
                "93982357069650740378985170712419350184447069, -21540993935236376131660978535829829577/2794118," +
                " -97572928275367678961/581577743664668876842, 24544331/580844203," +
                " -15110091853/4049564061150907586767959329048598]");
        try {
            randomRationals(P, 2);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            randomRationals(P, 0);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            randomRationals(P, -4);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRandomRationals() {
        aeq(take(20, randomRationals(P)),
                "[28316475562344188383645903995477392912/58352411," +
                " 115026539360322396321904458709367553356949577477/768205638868, 55/181711270441813673567119," +
                " 685659147139743/566, 475/16, -56/3, 127, 535187/776729408316270611599, 76, 154855009939/10228," +
                " -436368197737914966032172233/10899, -10573303/483531694924," +
                " 805947693312703966800490172698/5567844249301368238890239," +
                " 2446984376637910448639982840999/50743492065178372411080063589," +
                " -3749/121117, -5319560589/145481335091470221366332575477, 2/21354527," +
                " 10294640032517187746918631766295895126520452494595336350816413314257010652780512939995800380190739" +
                "165048270259576489244435929214037970052773/1026518, -7996/34448306433199594463," +
                " 829/6760252159714696926820448969102461]");
    }

    @Test
    public void testRandomNonNegativeRationals_Int() {
        aeq(take(20, randomNonNegativeRationals(P, 3)),
                "[7, 0, 0, 0, 0, 1, 0, 0, 1, 1/10, 0, 1/6, 0, 0, 1/3, 1, 19, 1/3, 0, 0]");
        aeq(take(20, randomNonNegativeRationals(P, 4)),
                "[1, 5, 1, 0, 2/3, 1, 1/15, 6/5, 3, 7, 3, 1/4, 2/7, 1/16, 3, 9, 0, 0, 0, 120]");
        aeq(take(20, randomNonNegativeRationals(P, 5)),
                "[3, 378, 2, 27, 1/137, 1/10, 23, 16, 3, 4, 1, 0, 0, 2/1275, 22/21, 1, 3/2, 12, 1/1690, 23/4]");
        aeq(take(20, randomNonNegativeRationals(P, 10)),
                "[4/3, 28/7691, 32/127, 5/63789, 6391/13858742509, 38/749, 5/723, 47/3, 12847/1341, 3/5," +
                " 1/102527, 8, 2726/5, 39/3659, 13/2, 377/2, 2, 0, 163/438702, 4895/2]");
        aeq(take(20, randomNonNegativeRationals(P, 100)),
                "[130930763182270660228663614013295761916501829255314527169021191457514666359558387588238964808959/7" +
                "517586777550828054626795662503, 216821/101419744017795180979313623318," +
                " 8432222209/3472916303802927696667141042973436153514," +
                " 109075443415234998413675268968903162899292281522067008545634886614273778365/1409662300984247073," +
                " 40244563659976491035220893845859613086/183875243," +
                " 50527/99244512490630600984484746207488440272815326551723319543270638504972182417331169219578973600" +
                "20502286025542015027338969477985265317," +
                " 7965040635321302351304013292694477500015437/183750625465175779505276265349101297108607903180002057" +
                "183, 41370335581718261747544169636254603412992954953943640640, 9471485831/15159019072," +
                " 99118670653/739522645111015134336927852406, 3/24608," +
                " 128272421474007609228256640954861882877744/1410063030826718427074900563293759788273051472528258478" +
                "927711, 7767480419577776911791358616454980/5140793647567668921712781661254915271921807," +
                " 31625195462219/1285299460546232934466888651629061955446184873," +
                " 10439/74629226064088438771793747293, 6017145870466733640000745183718128/18592538082321198897469070" +
                "784418778765776524023525522415240483488251998617817033644198190671797926285692061979350263343," +
                " 2657758105757474127110498134402601344575637909949350767497984158488861189/446798755076486851094948" +
                "636315026049797920379417013479720792361972424256512886438489492145430803428272665687896," +
                " 3400089547/20678688052, 6274844981898338/6901207, 110608108733832818689476407123450600294030423224" +
                "6152191886001886094311131611639562713878468940952365683622402882942520413/3618233608561748395950355" +
                "85967598041032]");
        try {
            randomNonNegativeRationals(P, 2);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            randomNonNegativeRationals(P, 0);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            randomNonNegativeRationals(P, -4);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRandomNonNegativeRationals() {
        aeq(take(20, randomNonNegativeRationals(P)),
                "[703427/108680047959250986, 3210428970808626123440551/78549137, 10887264/2133810949," +
                " 266470418240311/4742738, 535187/316463874199, 2/447122575, 76/1254366637715," +
                " 519429375/7517586777550828054626795662503, 216821/101419744017795180979313623318," +
                " 8432222209/3472916303802927696667141042973436153514," +
                " 641087837740224095520163807112308682/289510968873788833838064960525124056654829674246248923937," +
                " 40244563659976491035220893845859613086/183875243," +
                " 50527/99244512490630600984484746207488440272815326551723319543270638504972182417331169219578973600" +
                "20502286025542015027338969477985265317," +
                " 7965040635321302351304013292694477500015437/4980570683122845847909980567802027835," +
                " 2776031/25244033548712370072449616190670858743797881193, 5154044922/43831224199," +
                " 6569084480/648874484541, 341/192, 254461635208187120283751549/3432930313, 3/24608]");
    }

    @Test
    public void testRandomPositiveRationals_Int() {
        aeq(take(20, randomPositiveRationals(P, 3)),
                "[15, 7/3, 1/2, 8, 13/5, 1, 1, 1, 3, 1, 1/3, 1, 3, 1/4, 1, 1/2, 1/3, 2/3, 1, 1]");
        aeq(take(20, randomPositiveRationals(P, 4)),
                "[7, 703/393, 12, 1, 1/3, 12, 13/3, 1/13, 1/17, 1, 1/6, 1/3, 15/2, 5/7, 1, 3, 1, 248, 2/11, 1/3]");
        aeq(take(20, randomPositiveRationals(P, 5)),
                "[16, 4, 4/3, 1/890, 1/6, 1, 4/283, 1/3, 137/3, 10/87, 1/2, 62, 186, 6, 1, 2/19, 1, 1/3, 1, 264]");
        aeq(take(20, randomPositiveRationals(P, 10)),
                "[879/69217, 10/7, 11/795956, 13/7, 31/2, 57/21520, 39, 19979421/3998054, 7691/96, 127/2014, 292/21," +
                " 63789/2, 2468/14583, 13858742509/294, 749/79, 411/13, 723/175, 3/2, 48807/29231, 1341/11]");
        aeq(take(20, randomPositiveRationals(P, 100)),
                "[102527/17312403, 316463874199/6, 447122575/1176," +
                " 704610823827/3143033119300834198644069310133308879517329503534595129160065507604060983844672124072" +
                "322565195350251261283498014102904063, 7517586777550828054626795662503/741109," +
                " 3472916303802927696667141042973436153514/335231867706868192600337349063996732925210220322146235185" +
                "200480379729109693, 1409662300984247073/82779859525093798968142719774830639518, 183875243/312671," +
                " 99244512490630600984484746207488440272815326551723319543270638504972182417331169219578973600205022" +
                "86025542015027338969477985265317/6298430515363845415357007470, 1/43831224199," +
                " 15159019072/648874484541," +
                " 1410063030826718427074900563293759788273051472528258478927711/28536667853717087425913343933335364," +
                " 5140793647567668921712781661254915271921807/477347200," +
                " 1285299460546232934466888651629061955446184873/1784646922309452955045374686002717796," +
                " 74629226064088438771793747293/172170645343581217752976627718761200," +
                " 18592538082321198897469070784418778765776524023525522415240483488251998617817033644198190671797926" +
                "285692061979350263343/9725146364871011445443688137374275407885573497451826599984408963659340293," +
                " 44679875507648685109494863631502604979792037941701347972079236197242425651288643848949214543080342" +
                "8272665687896/7695056843, 6901207/176694600747696211068473992298355492424," +
                " 448/3688330965425236776550683243237517877270010025075375704716661242634958753628480757343523822221" +
                "090197119525574855690013789," +
                " 8470734525663105536958803543893487063052840666166897116597224/326811157579]");
        try {
            randomPositiveRationals(P, 2);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            randomPositiveRationals(P, 0);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            randomPositiveRationals(P, -4);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRandomPositiveRationals() {
        aeq(take(20, randomPositiveRationals(P)),
                "[65649474733/50, 8046132249267142822265255/78549137, 547758176/2133810949, 27183283269/1631119," +
                " 1811559053982367/595931, 13367/20607, 17312403/316463874199, 6/447122575, 588/1254366637715," +
                " 1056300287/7517586777550828054626795662503, 741109/101419744017795180979313623318," +
                " 25612091393/3472916303802927696667141042973436153514," +
                " 1970315833525139968423970867392653258/289510968873788833838064960525124056654829674246248923937," +
                " 82779859525093798968142719774830639518/183875243," +
                " 312671/9924451249063060098448474620748844027281532655172331954327063850497218241733116921957897360" +
                "020502286025542015027338969477985265317, 42485/62307903," +
                " 11164639/25244033548712370072449616190670858743797881193, 15159019072/648874484541, 2389/192," +
                " 10157981949491229319476745341/3432930313]");
    }

    @Test
    public void testRandomNegativeRationals_Int() {
        aeq(take(20, randomNegativeRationals(P, 3)),
                "[-15, -7/3, -1/2, -8, -13/5, -1, -1, -1, -3, -1, -1/3, -1, -3, -1/4, -1, -1/2, -1/3, -2/3, -1, -1]");
        aeq(take(20, randomNegativeRationals(P, 4)),
                "[-7, -703/393, -12, -1, -1/3, -12, -13/3, -1/13, -1/17, -1," +
                " -1/6, -1/3, -15/2, -5/7, -1, -3, -1, -248, -2/11, -1/3]");
        aeq(take(20, randomNegativeRationals(P, 5)),
                "[-16, -4, -4/3, -1/890, -1/6, -1, -4/283, -1/3, -137/3, -10/87," +
                " -1/2, -62, -186, -6, -1, -2/19, -1, -1/3, -1, -264]");
        aeq(take(20, randomNegativeRationals(P, 10)),
                "[-879/69217, -10/7, -11/795956, -13/7, -31/2, -57/21520, -39, -19979421/3998054, -7691/96," +
                " -127/2014, -292/21, -63789/2, -2468/14583, -13858742509/294, -749/79, -411/13, -723/175, -3/2," +
                " -48807/29231, -1341/11]");
        aeq(take(20, randomNegativeRationals(P, 100)),
                "[-102527/17312403, -316463874199/6, -447122575/1176," +
                " -704610823827/314303311930083419864406931013330887951732950353459512916006550760406098384467212407" +
                "2322565195350251261283498014102904063, -7517586777550828054626795662503/741109," +
                " -3472916303802927696667141042973436153514/33523186770686819260033734906399673292521022032214623518" +
                "5200480379729109693, -1409662300984247073/82779859525093798968142719774830639518," +
                " -183875243/312671," +
                " -9924451249063060098448474620748844027281532655172331954327063850497218241733116921957897360020502" +
                "286025542015027338969477985265317/6298430515363845415357007470, -1/43831224199," +
                " -15159019072/648874484541," +
                " -1410063030826718427074900563293759788273051472528258478927711/2853666785371708742591334393333536" +
                "4, -5140793647567668921712781661254915271921807/477347200," +
                " -1285299460546232934466888651629061955446184873/1784646922309452955045374686002717796," +
                " -74629226064088438771793747293/172170645343581217752976627718761200," +
                " -1859253808232119889746907078441877876577652402352552241524048348825199861781703364419819067179792" +
                "6285692061979350263343/9725146364871011445443688137374275407885573497451826599984408963659340293," +
                " -4467987550764868510949486363150260497979203794170134797207923619724242565128864384894921454308034" +
                "28272665687896/7695056843, -6901207/176694600747696211068473992298355492424," +
                " -448/368833096542523677655068324323751787727001002507537570471666124263495875362848075734352382222" +
                "1090197119525574855690013789," +
                " -8470734525663105536958803543893487063052840666166897116597224/326811157579]");
        try {
            randomNegativeRationals(P, 2);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            randomNegativeRationals(P, 0);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            randomNegativeRationals(P, -4);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRandomNegativeRationals() {
        aeq(take(20, randomNegativeRationals(P)),
                "[-65649474733/50, -8046132249267142822265255/78549137, -547758176/2133810949, -27183283269/1631119," +
                " -1811559053982367/595931, -13367/20607, -17312403/316463874199, -6/447122575, -588/1254366637715," +
                " -1056300287/7517586777550828054626795662503, -741109/101419744017795180979313623318," +
                " -25612091393/3472916303802927696667141042973436153514," +
                " -1970315833525139968423970867392653258/289510968873788833838064960525124056654829674246248923937," +
                " -82779859525093798968142719774830639518/183875243," +
                " -312671/992445124906306009844847462074884402728153265517233195432706385049721824173311692195789736" +
                "0020502286025542015027338969477985265317, -42485/62307903," +
                " -11164639/25244033548712370072449616190670858743797881193, -15159019072/648874484541, -2389/192," +
                " -10157981949491229319476745341/3432930313]");
    }

    @Test
    public void testRandomNonNegativeRationalsLessThanOne_Int() {
        aeq(take(20, randomNonNegativeRationalsLessThanOne(P, 3)),
                "[0, 0, 0, 0, 0, 0, 1/10, 0, 1/6, 0, 0, 1/3, 1/3, 0, 0, 1/12, 0, 1/3, 0, 0]");
        aeq(take(20, randomNonNegativeRationalsLessThanOne(P, 4)),
                "[0, 0, 0, 0, 1/248, 0, 2/55, 0, 0, 1/3, 74/87, 0, 0, 4/5, 1/21, 0, 0, 1/151, 8/13, 1/8]");
        aeq(take(20, randomNonNegativeRationalsLessThanOne(P, 5)),
                "[0, 1/181, 84/1769, 3/61, 1/7, 13/97, 2/7, 0, 3/8, 1/25," +
                " 1/22, 7/264, 1/7, 0, 2/3, 1/52, 1/3, 1/4, 3/125, 0]");
        aeq(take(20, randomNonNegativeRationalsLessThanOne(P, 10)),
                "[1/3, 1/22935, 43/318, 0, 1/6, 1/288537, 8/209, 1/581, 1/14, 0," +
                " 12/8843, 715/16519, 1/52, 7/74, 19/15165, 5/264, 4/389547, 62/77, 1/4, 1/824896]");
        aeq(take(20, randomNonNegativeRationalsLessThanOne(P, 100)),
                "[8/19115413234586613922071872429018658253005645," +
                " 85670910849758892570342055611481509357008599360251517919/43368919404338580948728100858673375441939" +
                "0170232945797696, 6569084480/648874484541, 2706903/176694600747696211068473992298355492424," +
                " 192/3688330965425236776550683243237517877270010025075375704716661242634958753628480757343523822221" +
                "090197119525574855690013789, 173897/275223825703365147091279783," +
                " 951732525077793218487970312495/71925553759405163093812054518105585024068553436," +
                " 4259067536175257580045/367522450405311323573981133621122, 50886674322/16952077774158324824975," +
                " 239767048199442843065659531849756404472293137585348/8855310663888228009861873107670577595111802900" +
                "408961035557172423321370127080848108208785, 90489965462802886/1815951617228423251," +
                " 498744531555493424/1135656064374618341045686916689830462556625046850998820065417222556321999653934" +
                "8445004402171746369665878612993060279663, 527702914570/8228044561649665419461," +
                " 2297640569482110469993/3146347802622966119593060140756024878108882373217055364573," +
                " 225176190354577958893/8391847397286266028663628628121949," +
                " 16463773253254/4460786916521119836043153499810635802022499595724107621041," +
                " 1810679170864420561862755191/126456984828725371321103379756615440565966281542317113182463400903649" +
                "68491127122, 859081323/143284950272832419208508," +
                " 682593906260878534705/151290350198800408473876763059169102633462652," +
                " 86984647404509122/160285187112559912264544581772315655]");
        try {
            randomNonNegativeRationalsLessThanOne(P, 2);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            randomNonNegativeRationalsLessThanOne(P, 0);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            randomNonNegativeRationalsLessThanOne(P, -4);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRandomNonNegativeRationalsLessThanOne() {
        aeq(take(20, randomNonNegativeRationalsLessThanOne(P)),
                "[703427/108680047959250986, 10887264/2133810949, 535187/316463874199, 2/447122575," +
                " 76/1254366637715, 519429375/7517586777550828054626795662503," +
                " 216821/101419744017795180979313623318, 8432222209/3472916303802927696667141042973436153514," +
                " 641087837740224095520163807112308682/289510968873788833838064960525124056654829674246248923937," +
                " 50527/99244512490630600984484746207488440272815326551723319543270638504972182417331169219578973600" +
                "20502286025542015027338969477985265317, 2776031/25244033548712370072449616190670858743797881193," +
                " 5154044922/43831224199, 6569084480/648874484541, 3/24608, 24125/8100308256502729982," +
                " 50383/16403535952228346146649733488193995, 7119112/2297953243," +
                " 9973812979224303732889/1323815223207487473860070151744750239917398103680780742568603429145," +
                " 1343/926409115776928504673415276737736, 3/7035601853855406784]");
    }

    private static void aeq(Iterable<?> a, Object b) {
        assertEquals(IterableUtils.toString(a), b.toString());
    }

    private static void aeq(Object a, Object b) {
        assertEquals(a.toString(), b.toString());
    }
}
