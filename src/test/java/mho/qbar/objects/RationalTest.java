package mho.qbar.objects;

import mho.wheels.iterables.IterableUtils;
import mho.wheels.misc.FloatUtils;
import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static mho.wheels.iterables.IterableUtils.toList;
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
    public void testOf_long_long() {
        aeq(of(2L, 3L), "2/3");
        aeq(of(4L, 6L), "2/3");
        aeq(of(-4L, -6L), "2/3");
        aeq(of(4L, -6L), "-2/3");
        assertTrue(of(4L, 4L) == ONE);
        aeq(of(4L, 1L), "4");
        assertTrue(of(0L, 1L) == ZERO);
        try {
            of(1L, 0L);
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
    public void testOf_long() {
        aeq(of(23L), "23");
        aeq(of(-23L), "-23");
        assertTrue(of(0L) == ZERO);
        assertTrue(of(1L) == ONE);
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
        aeq(of(0.1f), "1/10");
        aeq(of(1.0f / 3.0f), "16666667/50000000");
        aeq(of(1e10f), "10000000000");
        aeq(of(1e30f), "1000000000000000000000000000000");
        aeq(of((float) Math.PI), "31415927/10000000");
        aeq(of((float) Math.E), "27182817/10000000");
        aeq(of((float) Math.sqrt(2)), "2828427/2000000");
        aeq(of(Float.MIN_VALUE), "7/5000000000000000000000000000000000000000000000");
        aeq(of(-Float.MIN_VALUE), "-7/5000000000000000000000000000000000000000000000");
        aeq(of(Float.MIN_NORMAL), "23509887/2000000000000000000000000000000000000000000000");
        aeq(of(-Float.MIN_NORMAL), "-23509887/2000000000000000000000000000000000000000000000");
        aeq(of(Float.MAX_VALUE), "340282350000000000000000000000000000000");
        aeq(of(-Float.MAX_VALUE), "-340282350000000000000000000000000000000");
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
        aeq(of(0.1), "1/10");
        aeq(of(1.0 / 3.0), "3333333333333333/10000000000000000");
        aeq(of(1e10), "10000000000");
        aeq(of(1e30), "1000000000000000000000000000000");
        aeq(of(Math.PI), "3141592653589793/1000000000000000");
        aeq(of(Math.E), "543656365691809/200000000000000");
        aeq(of(Math.sqrt(2)), "14142135623730951/10000000000000000");
        aeq(of(Double.MIN_VALUE),
                "49/100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000");
        aeq(of(-Double.MIN_VALUE),
                "-49/10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000");
        aeq(of(Double.MIN_NORMAL),
                "11125369292536007/500000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000");
        aeq(of(-Double.MIN_NORMAL),
                "-11125369292536007/50000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000");
        aeq(of(Double.MAX_VALUE),
                "179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000");
        aeq(of(-Double.MAX_VALUE),
                "-17976931348623157000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000");
        assertNull(of(Double.POSITIVE_INFINITY));
        assertNull(of(Double.NEGATIVE_INFINITY));
        assertNull(of(Double.NaN));
    }

    @Test
    public void testOfExact_float() {
        assertTrue(ofExact(0.0f) == ZERO);
        assertTrue(ofExact(1.0f) == ONE);
        aeq(ofExact(13.0f), "13");
        aeq(ofExact(-5.0f), "-5");
        aeq(ofExact(1.5f), "3/2");
        aeq(ofExact(0.15625f), "5/32");
        aeq(ofExact(0.1f), "13421773/134217728");
        aeq(ofExact(1.0f / 3.0f), "11184811/33554432");
        aeq(ofExact(1e10f), "10000000000");
        aeq(ofExact(1e30f), "1000000015047466219876688855040");
        aeq(ofExact((float) Math.PI), "13176795/4194304");
        aeq(ofExact((float) Math.E), "2850325/1048576");
        aeq(ofExact((float) Math.sqrt(2)), "11863283/8388608");
        aeq(ofExact(Float.MIN_VALUE), SMALLEST_FLOAT);
        aeq(ofExact(-Float.MIN_VALUE), SMALLEST_FLOAT.negate());
        aeq(ofExact(Float.MIN_NORMAL), SMALLEST_NORMAL_FLOAT);
        aeq(ofExact(-Float.MIN_NORMAL), SMALLEST_NORMAL_FLOAT.negate());
        aeq(ofExact(Float.MAX_VALUE), LARGEST_FLOAT);
        aeq(ofExact(-Float.MAX_VALUE), LARGEST_FLOAT.negate());
        assertNull(ofExact(Float.POSITIVE_INFINITY));
        assertNull(ofExact(Float.NEGATIVE_INFINITY));
        assertNull(ofExact(Float.NaN));
    }

    @Test
    public void testOfExact_double() {
        assertTrue(ofExact(0.0) == ZERO);
        assertTrue(ofExact(1.0) == ONE);
        aeq(ofExact(13.0), "13");
        aeq(ofExact(-5.0), "-5");
        aeq(ofExact(1.5), "3/2");
        aeq(ofExact(0.15625), "5/32");
        aeq(ofExact(0.1), "3602879701896397/36028797018963968");
        aeq(ofExact(1.0 / 3.0), "6004799503160661/18014398509481984");
        aeq(ofExact(1e10), "10000000000");
        aeq(ofExact(1e30), "1000000000000000019884624838656");
        aeq(ofExact(Math.PI), "884279719003555/281474976710656");
        aeq(ofExact(Math.E), "6121026514868073/2251799813685248");
        aeq(ofExact(Math.sqrt(2)), "6369051672525773/4503599627370496");
        aeq(ofExact(Double.MIN_VALUE), SMALLEST_DOUBLE);
        aeq(ofExact(-Double.MIN_VALUE), SMALLEST_DOUBLE.negate());
        aeq(ofExact(Double.MIN_NORMAL), SMALLEST_NORMAL_DOUBLE);
        aeq(ofExact(-Double.MIN_NORMAL), SMALLEST_NORMAL_DOUBLE.negate());
        aeq(ofExact(Double.MAX_VALUE), LARGEST_DOUBLE);
        aeq(ofExact(-Double.MAX_VALUE), LARGEST_DOUBLE.negate());
        assertNull(ofExact(Double.POSITIVE_INFINITY));
        assertNull(ofExact(Double.NEGATIVE_INFINITY));
        assertNull(ofExact(Double.NaN));
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
    public void testBigIntegerValue_RoundingMode() {
        aeq(read("11/2").get().bigIntegerValue(RoundingMode.UP), 6);
        aeq(read("5/2").get().bigIntegerValue(RoundingMode.UP), 3);
        aeq(read("8/5").get().bigIntegerValue(RoundingMode.UP), 2);
        aeq(read("11/10").get().bigIntegerValue(RoundingMode.UP), 2);
        aeq(ONE.bigIntegerValue(RoundingMode.UP), 1);
        aeq(ZERO.bigIntegerValue(RoundingMode.UP), 0);
        aeq(read("-1").get().bigIntegerValue(RoundingMode.UP), -1);
        aeq(read("-11/10").get().bigIntegerValue(RoundingMode.UP), -2);
        aeq(read("-8/5").get().bigIntegerValue(RoundingMode.UP), -2);
        aeq(read("-5/2").get().bigIntegerValue(RoundingMode.UP), -3);
        aeq(read("-11/2").get().bigIntegerValue(RoundingMode.UP), -6);

        aeq(read("11/2").get().bigIntegerValue(RoundingMode.DOWN), 5);
        aeq(read("5/2").get().bigIntegerValue(RoundingMode.DOWN), 2);
        aeq(read("8/5").get().bigIntegerValue(RoundingMode.DOWN), 1);
        aeq(read("11/10").get().bigIntegerValue(RoundingMode.DOWN), 1);
        aeq(ONE.bigIntegerValue(RoundingMode.DOWN), 1);
        aeq(ZERO.bigIntegerValue(RoundingMode.DOWN), 0);
        aeq(read("-1").get().bigIntegerValue(RoundingMode.DOWN), -1);
        aeq(read("-11/10").get().bigIntegerValue(RoundingMode.DOWN), -1);
        aeq(read("-8/5").get().bigIntegerValue(RoundingMode.DOWN), -1);
        aeq(read("-5/2").get().bigIntegerValue(RoundingMode.DOWN), -2);
        aeq(read("-11/2").get().bigIntegerValue(RoundingMode.DOWN), -5);

        aeq(read("11/2").get().bigIntegerValue(RoundingMode.CEILING), 6);
        aeq(read("5/2").get().bigIntegerValue(RoundingMode.CEILING), 3);
        aeq(read("8/5").get().bigIntegerValue(RoundingMode.CEILING), 2);
        aeq(read("11/10").get().bigIntegerValue(RoundingMode.CEILING), 2);
        aeq(ONE.bigIntegerValue(RoundingMode.CEILING), 1);
        aeq(ZERO.bigIntegerValue(RoundingMode.CEILING), 0);
        aeq(read("-1").get().bigIntegerValue(RoundingMode.CEILING), -1);
        aeq(read("-11/10").get().bigIntegerValue(RoundingMode.CEILING), -1);
        aeq(read("-8/5").get().bigIntegerValue(RoundingMode.CEILING), -1);
        aeq(read("-5/2").get().bigIntegerValue(RoundingMode.CEILING), -2);
        aeq(read("-11/2").get().bigIntegerValue(RoundingMode.CEILING), -5);

        aeq(read("11/2").get().bigIntegerValue(RoundingMode.FLOOR), 5);
        aeq(read("5/2").get().bigIntegerValue(RoundingMode.FLOOR), 2);
        aeq(read("8/5").get().bigIntegerValue(RoundingMode.FLOOR), 1);
        aeq(read("11/10").get().bigIntegerValue(RoundingMode.FLOOR), 1);
        aeq(ONE.bigIntegerValue(RoundingMode.FLOOR), 1);
        aeq(ZERO.bigIntegerValue(RoundingMode.FLOOR), 0);
        aeq(read("-1").get().bigIntegerValue(RoundingMode.FLOOR), -1);
        aeq(read("-11/10").get().bigIntegerValue(RoundingMode.FLOOR), -2);
        aeq(read("-8/5").get().bigIntegerValue(RoundingMode.FLOOR), -2);
        aeq(read("-5/2").get().bigIntegerValue(RoundingMode.FLOOR), -3);
        aeq(read("-11/2").get().bigIntegerValue(RoundingMode.FLOOR), -6);

        aeq(read("11/2").get().bigIntegerValue(RoundingMode.HALF_UP), 6);
        aeq(read("5/2").get().bigIntegerValue(RoundingMode.HALF_UP), 3);
        aeq(read("8/5").get().bigIntegerValue(RoundingMode.HALF_UP), 2);
        aeq(read("11/10").get().bigIntegerValue(RoundingMode.HALF_UP), 1);
        aeq(ONE.bigIntegerValue(RoundingMode.HALF_UP), 1);
        aeq(ZERO.bigIntegerValue(RoundingMode.HALF_UP), 0);
        aeq(read("-1").get().bigIntegerValue(RoundingMode.HALF_UP), -1);
        aeq(read("-11/10").get().bigIntegerValue(RoundingMode.HALF_UP), -1);
        aeq(read("-8/5").get().bigIntegerValue(RoundingMode.HALF_UP), -2);
        aeq(read("-5/2").get().bigIntegerValue(RoundingMode.HALF_UP), -3);
        aeq(read("-11/2").get().bigIntegerValue(RoundingMode.HALF_UP), -6);

        aeq(read("11/2").get().bigIntegerValue(RoundingMode.HALF_DOWN), 5);
        aeq(read("5/2").get().bigIntegerValue(RoundingMode.HALF_DOWN), 2);
        aeq(read("8/5").get().bigIntegerValue(RoundingMode.HALF_DOWN), 2);
        aeq(read("11/10").get().bigIntegerValue(RoundingMode.HALF_DOWN), 1);
        aeq(ONE.bigIntegerValue(RoundingMode.HALF_DOWN), 1);
        aeq(ZERO.bigIntegerValue(RoundingMode.HALF_DOWN), 0);
        aeq(read("-1").get().bigIntegerValue(RoundingMode.HALF_DOWN), -1);
        aeq(read("-11/10").get().bigIntegerValue(RoundingMode.HALF_DOWN), -1);
        aeq(read("-8/5").get().bigIntegerValue(RoundingMode.HALF_DOWN), -2);
        aeq(read("-5/2").get().bigIntegerValue(RoundingMode.HALF_DOWN), -2);
        aeq(read("-11/2").get().bigIntegerValue(RoundingMode.HALF_DOWN), -5);

        aeq(read("11/2").get().bigIntegerValue(RoundingMode.HALF_EVEN), 6);
        aeq(read("5/2").get().bigIntegerValue(RoundingMode.HALF_EVEN), 2);
        aeq(read("8/5").get().bigIntegerValue(RoundingMode.HALF_EVEN), 2);
        aeq(read("11/10").get().bigIntegerValue(RoundingMode.HALF_EVEN), 1);
        aeq(ONE.bigIntegerValue(RoundingMode.HALF_EVEN), 1);
        aeq(ZERO.bigIntegerValue(RoundingMode.HALF_EVEN), 0);
        aeq(read("-1").get().bigIntegerValue(RoundingMode.HALF_EVEN), -1);
        aeq(read("-11/10").get().bigIntegerValue(RoundingMode.HALF_EVEN), -1);
        aeq(read("-8/5").get().bigIntegerValue(RoundingMode.HALF_EVEN), -2);
        aeq(read("-5/2").get().bigIntegerValue(RoundingMode.HALF_EVEN), -2);
        aeq(read("-11/2").get().bigIntegerValue(RoundingMode.HALF_EVEN), -6);

        try {
            read("11/2").get().bigIntegerValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("5/2").get().bigIntegerValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("8/5").get().bigIntegerValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("11/10").get().bigIntegerValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(ONE.bigIntegerValue(RoundingMode.UNNECESSARY), 1);
        aeq(ZERO.bigIntegerValue(RoundingMode.UNNECESSARY), 0);
        aeq(read("-1").get().bigIntegerValue(RoundingMode.UNNECESSARY), -1);
        try {
            read("-11/10").get().bigIntegerValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("-8/5").get().bigIntegerValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("-5/2").get().bigIntegerValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("-11/2").get().bigIntegerValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBigIntegerValue() {
        aeq(read("11/2").get().bigIntegerValue(), 6);
        aeq(read("5/2").get().bigIntegerValue(), 2);
        aeq(read("8/5").get().bigIntegerValue(), 2);
        aeq(read("11/10").get().bigIntegerValue(), 1);
        aeq(ONE.bigIntegerValue(), 1);
        aeq(ZERO.bigIntegerValue(), 0);
        aeq(read("-1").get().bigIntegerValue(), -1);
        aeq(read("-11/10").get().bigIntegerValue(), -1);
        aeq(read("-8/5").get().bigIntegerValue(), -2);
        aeq(read("-5/2").get().bigIntegerValue(), -2);
        aeq(read("-11/2").get().bigIntegerValue(), -6);
    }

    @Test
     public void testBigIntegerValueExact() {
        aeq(ONE.bigIntegerValueExact(), 1);
        aeq(ZERO.bigIntegerValueExact(), 0);
        aeq(read("-1").get().bigIntegerValueExact(), -1);
        aeq(read("23").get().bigIntegerValueExact(), 23);
        aeq(read("-8").get().bigIntegerValueExact(), -8);
        try {
            read("11/2").get().bigIntegerValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("-8/5").get().bigIntegerValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testByteValueExact() {
        aeq(ONE.byteValueExact(), 1);
        aeq(ZERO.byteValueExact(), 0);
        aeq(read("-1").get().byteValueExact(), -1);
        aeq(read("23").get().byteValueExact(), 23);
        aeq(read("-8").get().byteValueExact(), -8);
        try {
            read("11/2").get().byteValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("-8/5").get().byteValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("1000").get().byteValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShortValueExact() {
        aeq(ONE.shortValueExact(), 1);
        aeq(ZERO.shortValueExact(), 0);
        aeq(read("-1").get().shortValueExact(), -1);
        aeq(read("23").get().shortValueExact(), 23);
        aeq(read("-8").get().shortValueExact(), -8);
        try {
            read("11/2").get().shortValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("-8/5").get().shortValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("100000").get().shortValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIntValueExact() {
        aeq(ONE.intValueExact(), 1);
        aeq(ZERO.intValueExact(), 0);
        aeq(read("-1").get().intValueExact(), -1);
        aeq(read("23").get().intValueExact(), 23);
        aeq(read("-8").get().intValueExact(), -8);
        try {
            read("11/2").get().intValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("-8/5").get().intValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("10000000000").get().intValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testLongValueExact() {
        aeq(ONE.longValueExact(), 1);
        aeq(ZERO.longValueExact(), 0);
        aeq(read("-1").get().longValueExact(), -1);
        aeq(read("23").get().longValueExact(), 23);
        aeq(read("-8").get().longValueExact(), -8);
        try {
            read("11/2").get().longValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("-8/5").get().longValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("10000000000000000000").get().longValueExact();
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
    public void testBigDecimalValue_int_RoundingMode() {
        aeq(ZERO.bigDecimalValue(0, RoundingMode.UNNECESSARY), "0");
        aeq(ZERO.bigDecimalValue(0, RoundingMode.FLOOR), "0");
        aeq(ZERO.bigDecimalValue(0, RoundingMode.CEILING), "0");
        aeq(ZERO.bigDecimalValue(0, RoundingMode.DOWN), "0");
        aeq(ZERO.bigDecimalValue(0, RoundingMode.UP), "0");
        aeq(ZERO.bigDecimalValue(0, RoundingMode.HALF_DOWN), "0");
        aeq(ZERO.bigDecimalValue(0, RoundingMode.HALF_UP), "0");
        aeq(ZERO.bigDecimalValue(0, RoundingMode.HALF_EVEN), "0");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.UNNECESSARY), "0");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.FLOOR), "0");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.CEILING), "0");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.DOWN), "0");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.UP), "0");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.HALF_DOWN), "0");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.HALF_UP), "0");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.HALF_EVEN), "0");
        aeq(ONE.bigDecimalValue(0, RoundingMode.UNNECESSARY), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.FLOOR), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.CEILING), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.DOWN), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.UP), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.HALF_DOWN), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.HALF_UP), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.HALF_EVEN), "1");
        aeq(ONE.bigDecimalValue(4, RoundingMode.UNNECESSARY), "1");
        aeq(ONE.bigDecimalValue(4, RoundingMode.FLOOR), "1");
        aeq(ONE.bigDecimalValue(4, RoundingMode.CEILING), "1");
        aeq(ONE.bigDecimalValue(4, RoundingMode.DOWN), "1");
        aeq(ONE.bigDecimalValue(4, RoundingMode.UP), "1");
        aeq(ONE.bigDecimalValue(4, RoundingMode.HALF_DOWN), "1");
        aeq(ONE.bigDecimalValue(4, RoundingMode.HALF_UP), "1");
        aeq(ONE.bigDecimalValue(4, RoundingMode.HALF_EVEN), "1");
        aeq(read("1/2").get().bigDecimalValue(0, RoundingMode.UNNECESSARY), "0.5");
        aeq(read("1/2").get().bigDecimalValue(0, RoundingMode.FLOOR), "0.5");
        aeq(read("1/2").get().bigDecimalValue(0, RoundingMode.CEILING), "0.5");
        aeq(read("1/2").get().bigDecimalValue(0, RoundingMode.DOWN), "0.5");
        aeq(read("1/2").get().bigDecimalValue(0, RoundingMode.UP), "0.5");
        aeq(read("1/2").get().bigDecimalValue(0, RoundingMode.HALF_DOWN), "0.5");
        aeq(read("1/2").get().bigDecimalValue(0, RoundingMode.HALF_UP), "0.5");
        aeq(read("1/2").get().bigDecimalValue(0, RoundingMode.HALF_EVEN), "0.5");
        aeq(read("1/2").get().bigDecimalValue(1, RoundingMode.UNNECESSARY), "0.5");
        aeq(read("1/2").get().bigDecimalValue(1, RoundingMode.FLOOR), "0.5");
        aeq(read("1/2").get().bigDecimalValue(1, RoundingMode.CEILING), "0.5");
        aeq(read("1/2").get().bigDecimalValue(1, RoundingMode.DOWN), "0.5");
        aeq(read("1/2").get().bigDecimalValue(1, RoundingMode.UP), "0.5");
        aeq(read("1/2").get().bigDecimalValue(1, RoundingMode.HALF_DOWN), "0.5");
        aeq(read("1/2").get().bigDecimalValue(1, RoundingMode.HALF_UP), "0.5");
        aeq(read("1/2").get().bigDecimalValue(1, RoundingMode.HALF_EVEN), "0.5");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.UNNECESSARY), "0.5");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.FLOOR), "0.5");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.CEILING), "0.5");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.DOWN), "0.5");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.UP), "0.5");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.HALF_DOWN), "0.5");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.HALF_UP), "0.5");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.HALF_EVEN), "0.5");
        aeq(read("1/64").get().bigDecimalValue(0, RoundingMode.UNNECESSARY), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(0, RoundingMode.FLOOR), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(0, RoundingMode.CEILING), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(0, RoundingMode.DOWN), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(0, RoundingMode.UP), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(0, RoundingMode.HALF_DOWN), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(0, RoundingMode.HALF_UP), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(0, RoundingMode.HALF_EVEN), "0.015625");
        try {
            read("1/64").get().bigDecimalValue(1, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("1/64").get().bigDecimalValue(1, RoundingMode.FLOOR), "0.01");
        aeq(read("1/64").get().bigDecimalValue(1, RoundingMode.CEILING), "0.02");
        aeq(read("1/64").get().bigDecimalValue(1, RoundingMode.DOWN), "0.01");
        aeq(read("1/64").get().bigDecimalValue(1, RoundingMode.UP), "0.02");
        aeq(read("1/64").get().bigDecimalValue(1, RoundingMode.HALF_DOWN), "0.02");
        aeq(read("1/64").get().bigDecimalValue(1, RoundingMode.HALF_UP), "0.02");
        aeq(read("1/64").get().bigDecimalValue(1, RoundingMode.HALF_EVEN), "0.02");
        try {
            read("1/64").get().bigDecimalValue(2, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("1/64").get().bigDecimalValue(2, RoundingMode.FLOOR), "0.015");
        aeq(read("1/64").get().bigDecimalValue(2, RoundingMode.CEILING), "0.016");
        aeq(read("1/64").get().bigDecimalValue(2, RoundingMode.DOWN), "0.015");
        aeq(read("1/64").get().bigDecimalValue(2, RoundingMode.UP), "0.016");
        aeq(read("1/64").get().bigDecimalValue(2, RoundingMode.HALF_DOWN), "0.016");
        aeq(read("1/64").get().bigDecimalValue(2, RoundingMode.HALF_UP), "0.016");
        aeq(read("1/64").get().bigDecimalValue(2, RoundingMode.HALF_EVEN), "0.016");
        try {
            read("1/64").get().bigDecimalValue(3, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("1/64").get().bigDecimalValue(3, RoundingMode.FLOOR), "0.0156");
        aeq(read("1/64").get().bigDecimalValue(3, RoundingMode.CEILING), "0.0157");
        aeq(read("1/64").get().bigDecimalValue(3, RoundingMode.DOWN), "0.0156");
        aeq(read("1/64").get().bigDecimalValue(3, RoundingMode.UP), "0.0157");
        aeq(read("1/64").get().bigDecimalValue(3, RoundingMode.HALF_DOWN), "0.0156");
        aeq(read("1/64").get().bigDecimalValue(3, RoundingMode.HALF_UP), "0.0156");
        aeq(read("1/64").get().bigDecimalValue(3, RoundingMode.HALF_EVEN), "0.0156");
        try {
            read("1/64").get().bigDecimalValue(4, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("1/64").get().bigDecimalValue(4, RoundingMode.FLOOR), "0.01562");
        aeq(read("1/64").get().bigDecimalValue(4, RoundingMode.CEILING), "0.01563");
        aeq(read("1/64").get().bigDecimalValue(4, RoundingMode.DOWN), "0.01562");
        aeq(read("1/64").get().bigDecimalValue(4, RoundingMode.UP), "0.01563");
        aeq(read("1/64").get().bigDecimalValue(4, RoundingMode.HALF_DOWN), "0.01562");
        aeq(read("1/64").get().bigDecimalValue(4, RoundingMode.HALF_UP), "0.01563");
        aeq(read("1/64").get().bigDecimalValue(4, RoundingMode.HALF_EVEN), "0.01562");
        aeq(read("1/64").get().bigDecimalValue(5, RoundingMode.UNNECESSARY), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(5, RoundingMode.FLOOR), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(5, RoundingMode.CEILING), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(5, RoundingMode.DOWN), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(5, RoundingMode.UP), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(5, RoundingMode.HALF_DOWN), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(5, RoundingMode.HALF_UP), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(5, RoundingMode.HALF_EVEN), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.UNNECESSARY), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.FLOOR), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.CEILING), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.DOWN), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.UP), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.HALF_DOWN), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.HALF_UP), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.HALF_EVEN), "0.015625");
        try {
            read("-1/3").get().bigDecimalValue(1, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("-1/3").get().bigDecimalValue(1, RoundingMode.FLOOR), "-0.4");
        aeq(read("-1/3").get().bigDecimalValue(1, RoundingMode.CEILING), "-0.3");
        aeq(read("-1/3").get().bigDecimalValue(1, RoundingMode.DOWN), "-0.3");
        aeq(read("-1/3").get().bigDecimalValue(1, RoundingMode.UP), "-0.4");
        aeq(read("-1/3").get().bigDecimalValue(1, RoundingMode.HALF_DOWN), "-0.3");
        aeq(read("-1/3").get().bigDecimalValue(1, RoundingMode.HALF_UP), "-0.3");
        aeq(read("-1/3").get().bigDecimalValue(1, RoundingMode.HALF_EVEN), "-0.3");
        try {
            read("-1/3").get().bigDecimalValue(2, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("-1/3").get().bigDecimalValue(2, RoundingMode.FLOOR), "-0.34");
        aeq(read("-1/3").get().bigDecimalValue(2, RoundingMode.CEILING), "-0.33");
        aeq(read("-1/3").get().bigDecimalValue(2, RoundingMode.DOWN), "-0.33");
        aeq(read("-1/3").get().bigDecimalValue(2, RoundingMode.UP), "-0.34");
        aeq(read("-1/3").get().bigDecimalValue(2, RoundingMode.HALF_DOWN), "-0.33");
        aeq(read("-1/3").get().bigDecimalValue(2, RoundingMode.HALF_UP), "-0.33");
        aeq(read("-1/3").get().bigDecimalValue(2, RoundingMode.HALF_EVEN), "-0.33");
        try {
            read("-1/3").get().bigDecimalValue(3, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("-1/3").get().bigDecimalValue(3, RoundingMode.FLOOR), "-0.334");
        aeq(read("-1/3").get().bigDecimalValue(3, RoundingMode.CEILING), "-0.333");
        aeq(read("-1/3").get().bigDecimalValue(3, RoundingMode.DOWN), "-0.333");
        aeq(read("-1/3").get().bigDecimalValue(3, RoundingMode.UP), "-0.334");
        aeq(read("-1/3").get().bigDecimalValue(3, RoundingMode.HALF_DOWN), "-0.333");
        aeq(read("-1/3").get().bigDecimalValue(3, RoundingMode.HALF_UP), "-0.333");
        aeq(read("-1/3").get().bigDecimalValue(3, RoundingMode.HALF_EVEN), "-0.333");
        try {
            read("-1/3").get().bigDecimalValue(4, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("-1/3").get().bigDecimalValue(4, RoundingMode.FLOOR), "-0.3334");
        aeq(read("-1/3").get().bigDecimalValue(4, RoundingMode.CEILING), "-0.3333");
        aeq(read("-1/3").get().bigDecimalValue(4, RoundingMode.DOWN), "-0.3333");
        aeq(read("-1/3").get().bigDecimalValue(4, RoundingMode.UP), "-0.3334");
        aeq(read("-1/3").get().bigDecimalValue(4, RoundingMode.HALF_DOWN), "-0.3333");
        aeq(read("-1/3").get().bigDecimalValue(4, RoundingMode.HALF_UP), "-0.3333");
        aeq(read("-1/3").get().bigDecimalValue(4, RoundingMode.HALF_EVEN), "-0.3333");
        aeq(read("6789").get().bigDecimalValue(0, RoundingMode.FLOOR), "6789");
        aeq(read("6789").get().bigDecimalValue(0, RoundingMode.CEILING), "6789");
        aeq(read("6789").get().bigDecimalValue(0, RoundingMode.DOWN), "6789");
        aeq(read("6789").get().bigDecimalValue(0, RoundingMode.UP), "6789");
        aeq(read("6789").get().bigDecimalValue(0, RoundingMode.HALF_DOWN), "6789");
        aeq(read("6789").get().bigDecimalValue(0, RoundingMode.HALF_UP), "6789");
        aeq(read("6789").get().bigDecimalValue(0, RoundingMode.HALF_EVEN), "6789");
        try {
            read("6789").get().bigDecimalValue(1, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("6789").get().bigDecimalValue(1, RoundingMode.FLOOR), "6E+3");
        aeq(read("6789").get().bigDecimalValue(1, RoundingMode.CEILING), "7E+3");
        aeq(read("6789").get().bigDecimalValue(1, RoundingMode.DOWN), "6E+3");
        aeq(read("6789").get().bigDecimalValue(1, RoundingMode.UP), "7E+3");
        aeq(read("6789").get().bigDecimalValue(1, RoundingMode.HALF_DOWN), "7E+3");
        aeq(read("6789").get().bigDecimalValue(1, RoundingMode.HALF_UP), "7E+3");
        aeq(read("6789").get().bigDecimalValue(1, RoundingMode.HALF_EVEN), "7E+3");
        try {
            read("6789").get().bigDecimalValue(2, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("6789").get().bigDecimalValue(2, RoundingMode.FLOOR), "6.7E+3");
        aeq(read("6789").get().bigDecimalValue(2, RoundingMode.CEILING), "6.8E+3");
        aeq(read("6789").get().bigDecimalValue(2, RoundingMode.DOWN), "6.7E+3");
        aeq(read("6789").get().bigDecimalValue(2, RoundingMode.UP), "6.8E+3");
        aeq(read("6789").get().bigDecimalValue(2, RoundingMode.HALF_DOWN), "6.8E+3");
        aeq(read("6789").get().bigDecimalValue(2, RoundingMode.HALF_UP), "6.8E+3");
        aeq(read("6789").get().bigDecimalValue(2, RoundingMode.HALF_EVEN), "6.8E+3");
        try {
            read("6789").get().bigDecimalValue(3, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("6789").get().bigDecimalValue(3, RoundingMode.FLOOR), "6.78E+3");
        aeq(read("6789").get().bigDecimalValue(3, RoundingMode.CEILING), "6.79E+3");
        aeq(read("6789").get().bigDecimalValue(3, RoundingMode.DOWN), "6.78E+3");
        aeq(read("6789").get().bigDecimalValue(3, RoundingMode.UP), "6.79E+3");
        aeq(read("6789").get().bigDecimalValue(3, RoundingMode.HALF_DOWN), "6.79E+3");
        aeq(read("6789").get().bigDecimalValue(3, RoundingMode.HALF_UP), "6.79E+3");
        aeq(read("6789").get().bigDecimalValue(3, RoundingMode.HALF_EVEN), "6.79E+3");
        aeq(read("6789").get().bigDecimalValue(4, RoundingMode.UNNECESSARY), "6789");
        aeq(read("6789").get().bigDecimalValue(4, RoundingMode.FLOOR), "6789");
        aeq(read("6789").get().bigDecimalValue(4, RoundingMode.CEILING), "6789");
        aeq(read("6789").get().bigDecimalValue(4, RoundingMode.DOWN), "6789");
        aeq(read("6789").get().bigDecimalValue(4, RoundingMode.UP), "6789");
        aeq(read("6789").get().bigDecimalValue(4, RoundingMode.HALF_DOWN), "6789");
        aeq(read("6789").get().bigDecimalValue(4, RoundingMode.HALF_UP), "6789");
        aeq(read("6789").get().bigDecimalValue(4, RoundingMode.HALF_EVEN), "6789");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.UNNECESSARY), "6789");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.FLOOR), "6789");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.CEILING), "6789");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.DOWN), "6789");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.UP), "6789");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.HALF_DOWN), "6789");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.HALF_UP), "6789");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.HALF_EVEN), "6789");
        try {
            read("5").get().bigDecimalValue(-1, RoundingMode.UNNECESSARY);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().bigDecimalValue(-1, RoundingMode.FLOOR);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().bigDecimalValue(-1, RoundingMode.CEILING);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().bigDecimalValue(-1, RoundingMode.DOWN);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().bigDecimalValue(-1, RoundingMode.UP);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().bigDecimalValue(-1, RoundingMode.HALF_DOWN);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().bigDecimalValue(-1, RoundingMode.HALF_UP);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("5").get().bigDecimalValue(-1, RoundingMode.HALF_EVEN);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigDecimalValue_int() {
        aeq(ZERO.bigDecimalValue(4), "0");
        aeq(ZERO.bigDecimalValue(0), "0");
        aeq(ONE.bigDecimalValue(4), "1");
        aeq(ONE.bigDecimalValue(0), "1");
        aeq(read("1/2").get().bigDecimalValue(0), "0.5");
        aeq(read("1/2").get().bigDecimalValue(1), "0.5");
        aeq(read("1/2").get().bigDecimalValue(4), "0.5");
        aeq(read("1/64").get().bigDecimalValue(0), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(1), "0.02");
        aeq(read("1/64").get().bigDecimalValue(2), "0.016");
        aeq(read("1/64").get().bigDecimalValue(3), "0.0156");
        aeq(read("1/64").get().bigDecimalValue(4), "0.01563");
        aeq(read("1/64").get().bigDecimalValue(5), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(6), "0.015625");
        aeq(read("-1/3").get().bigDecimalValue(1), "-0.3");
        aeq(read("-1/3").get().bigDecimalValue(2), "-0.33");
        aeq(read("-1/3").get().bigDecimalValue(3), "-0.333");
        aeq(read("-1/3").get().bigDecimalValue(4), "-0.3333");
        aeq(read("6789").get().bigDecimalValue(0), "6789");
        aeq(read("6789").get().bigDecimalValue(1), "7E+3");
        aeq(read("6789").get().bigDecimalValue(2), "6.8E+3");
        aeq(read("6789").get().bigDecimalValue(3), "6.79E+3");
        aeq(read("6789").get().bigDecimalValue(4), "6789");
        aeq(read("6789").get().bigDecimalValue(5), "6789");
        try {
            read("-1/3").get().bigDecimalValue(0);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("5").get().bigDecimalValue(-1);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigDecimalValueExact() {
        aeq(ZERO.bigDecimalValueExact(), "0");
        aeq(ONE.bigDecimalValueExact(), "1");
        aeq(read("-23").get().bigDecimalValueExact(), "-23");
        aeq(read("4/5").get().bigDecimalValueExact(), "0.8");
        aeq(read("1/64").get().bigDecimalValueExact(), "0.015625");
        aeq(read("1234").get().bigDecimalValueExact(), "1234");
        try {
            read("1/3").get().bigDecimalValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
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
    public void testSum() {
        aeq(sum(readRationalList("[10, 21/2, 11]").get()), "63/2");
        aeq(sum(readRationalList("[-4, 6, -8]").get()), -6);
        aeq(sum(new ArrayList<>()), 0);
        try {
            sum(readRationalListWithNulls("[10, null, 11]").get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testProduct() {
        aeq(product(readRationalList("[10, 21/2, 11]").get()), 1155);
        aeq(product(readRationalList("[-4, 6, -8]").get()), 192);
        aeq(product(new ArrayList<>()), 1);
        try {
            product(readRationalListWithNulls("[10, null, 11]").get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testDelta() {
        aeq(delta(readRationalList("[31/10, 41/10, 59/10, 23/10]").get()), "[1, 9/5, -18/5]");
        aeq(delta(Arrays.asList(Rational.of(3))), "[]");
        try {
            delta(new ArrayList<>());
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(delta(readRationalListWithNulls("[10, null, 12]").get()));
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testHarmonicNumber() {
        aeq(harmonicNumber(1), "1");
        aeq(harmonicNumber(5), "137/60");
        aeq(harmonicNumber(10), "7381/2520");
        try {
            harmonicNumber(0);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            harmonicNumber(-5);
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
    public void testRoundToDenominator() {
        Rational doublePi = ofExact(Math.PI);
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
        aeq(ZERO.floatValue(), 0.0);
        aeq(ONE.floatValue(), 1.0);
        aeq(read("1/2").get().floatValue(), 0.5);
        aeq(read("1/3").get().floatValue(), 0.33333334);
        aeq(read("-1/3").get().floatValue(), -0.33333334);
        Rational almostOne = of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
        aeq(almostOne.floatValue(), 1.0);
        Rational floatPi = ofExact((float) Math.PI);
        if (floatPi == null) {
            fail();
        }
        aeq(floatPi.floatValue(), 3.1415927);
        Rational trillion = of(BigInteger.TEN.pow(12));
        aeq(trillion.floatValue(), 1.0E12);
        Rational piSuccessor = ofExact(FloatUtils.successor((float) Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = ofExact(FloatUtils.predecessor((float) Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = add(floatPi, piSuccessor).divide(2);
        Rational halfBelowPi = add(floatPi, piPredecessor).divide(2);
        Rational justAbovePi = add(floatPi.multiply(2), piSuccessor).divide(3);
        Rational justBelowPi = add(floatPi.multiply(2), piPredecessor).divide(3);
        aeq(halfAbovePi.floatValue(), 3.141593);
        aeq(halfBelowPi.floatValue(), 3.1415925);
        aeq(justAbovePi.floatValue(), 3.1415927);
        aeq(justBelowPi.floatValue(), 3.1415927);
        Rational subnormal = ofExact(1e-40f);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = ofExact(FloatUtils.successor(1e-40f));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = ofExact(FloatUtils.predecessor(1e-40f));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = add(subnormal, subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = add(subnormal, subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = add(subnormal.multiply(2), subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = add(subnormal.multiply(2), subnormalPredecessor).divide(3);
        aeq(subnormal.floatValue(), 1.0E-40);
        aeq(halfAboveSubnormal.floatValue(), 1.0E-40);
        aeq(halfBelowSubnormal.floatValue(), 1.0E-40);
        aeq(justAboveSubnormal.floatValue(), 1.0E-40);
        aeq(justBelowSubnormal.floatValue(), 1.0E-40);
        Rational belowNegativeMax = subtract(LARGEST_FLOAT.negate(), ONE);
        Rational negativeMaxSuccessor = ofExact(FloatUtils.successor(-Float.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = add(LARGEST_FLOAT.negate(), negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = add(
                LARGEST_FLOAT.negate().multiply(2),
                negativeMaxSuccessor
        ).divide(3);
        aeq(belowNegativeMax.floatValue(), Float.NEGATIVE_INFINITY);
        aeq(halfAboveNegativeMax.floatValue(), -3.4028233E38);
        aeq(justAboveNegativeMax.floatValue(), -3.4028235E38);
        Rational aboveMax = add(LARGEST_FLOAT, ONE);
        Rational maxPredecessor = ofExact(FloatUtils.predecessor(Float.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = add(LARGEST_FLOAT, maxPredecessor).divide(2);
        Rational justBelowMax = add(LARGEST_FLOAT.multiply(2), maxPredecessor).divide(3);
        aeq(aboveMax.floatValue(), Float.POSITIVE_INFINITY);
        aeq(halfBelowMax.floatValue(), 3.4028233E38);
        aeq(justBelowMax.floatValue(), 3.4028235E38);
        Rational halfAboveZero = SMALLEST_FLOAT.shiftRight(1);
        Rational justAboveZero = SMALLEST_FLOAT.divide(3);
        Rational halfBelowZero = halfAboveZero.negate();
        Rational justBelowZero = justAboveZero.negate();
        aeq(halfBelowZero.floatValue(), -0.0);
        aeq(justBelowZero.floatValue(), -0.0);
        aeq(halfAboveZero.floatValue(), 0.0);
        aeq(justAboveZero.floatValue(), 0.0);
        Rational boundary = add(
                LARGEST_SUBNORMAL_FLOAT,
                SMALLEST_NORMAL_FLOAT
        ).shiftRight(1);
        Rational halfBelowBoundary = add(LARGEST_SUBNORMAL_FLOAT, boundary).shiftRight(1);
        Rational halfAboveBoundary = add(SMALLEST_NORMAL_FLOAT, boundary).shiftRight(1);
        Rational justBelowBoundary = add(LARGEST_SUBNORMAL_FLOAT, boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = add(SMALLEST_NORMAL_FLOAT, boundary.shiftLeft(1)).divide(3);
        aeq(boundary.floatValue(), 1.17549435E-38);
        aeq(halfBelowBoundary.floatValue(), 1.1754942E-38);
        aeq(justBelowBoundary.floatValue(), 1.1754942E-38);
        aeq(halfAboveBoundary.floatValue(), 1.17549435E-38);
        aeq(justAboveBoundary.floatValue(), 1.17549435E-38);
    }

    @Test
    public void testToFloat_RoundingMode() {
        aeq(ZERO.floatValue(RoundingMode.FLOOR), 0.0);
        aeq(ZERO.floatValue(RoundingMode.CEILING), 0.0);
        aeq(ZERO.floatValue(RoundingMode.DOWN), 0.0);
        aeq(ZERO.floatValue(RoundingMode.UP), 0.0);
        aeq(ZERO.floatValue(RoundingMode.HALF_DOWN), 0.0);
        aeq(ZERO.floatValue(RoundingMode.HALF_UP), 0.0);
        aeq(ZERO.floatValue(RoundingMode.HALF_EVEN), 0.0);
        aeq(ZERO.floatValue(RoundingMode.UNNECESSARY), 0.0);
        aeq(ONE.floatValue(RoundingMode.FLOOR), 1.0);
        aeq(ONE.floatValue(RoundingMode.CEILING), 1.0);
        aeq(ONE.floatValue(RoundingMode.DOWN), 1.0);
        aeq(ONE.floatValue(RoundingMode.UP), 1.0);
        aeq(ONE.floatValue(RoundingMode.HALF_DOWN), 1.0);
        aeq(ONE.floatValue(RoundingMode.HALF_UP), 1.0);
        aeq(ONE.floatValue(RoundingMode.HALF_EVEN), 1.0);
        aeq(ONE.floatValue(RoundingMode.UNNECESSARY), 1.0);
        aeq(read("1/2").get().floatValue(RoundingMode.FLOOR), 0.5);
        aeq(read("1/2").get().floatValue(RoundingMode.CEILING), 0.5);
        aeq(read("1/2").get().floatValue(RoundingMode.DOWN), 0.5);
        aeq(read("1/2").get().floatValue(RoundingMode.UP), 0.5);
        aeq(read("1/2").get().floatValue(RoundingMode.HALF_DOWN), 0.5);
        aeq(read("1/2").get().floatValue(RoundingMode.HALF_UP), 0.5);
        aeq(read("1/2").get().floatValue(RoundingMode.HALF_EVEN), 0.5);
        aeq(read("1/2").get().floatValue(RoundingMode.UNNECESSARY), 0.5);
        aeq(read("1/3").get().floatValue(RoundingMode.FLOOR), 0.3333333);
        aeq(read("1/3").get().floatValue(RoundingMode.CEILING), 0.33333334);
        aeq(read("1/3").get().floatValue(RoundingMode.DOWN), 0.3333333);
        aeq(read("1/3").get().floatValue(RoundingMode.UP), 0.33333334);
        aeq(read("1/3").get().floatValue(RoundingMode.HALF_DOWN), 0.33333334);
        aeq(read("1/3").get().floatValue(RoundingMode.HALF_UP), 0.33333334);
        aeq(read("1/3").get().floatValue(RoundingMode.HALF_EVEN), 0.33333334);
        try {
            read("1/3").get().floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("-1/3").get().floatValue(RoundingMode.FLOOR), -0.33333334);
        aeq(read("-1/3").get().floatValue(RoundingMode.CEILING), -0.3333333);
        aeq(read("-1/3").get().floatValue(RoundingMode.DOWN), -0.3333333);
        aeq(read("-1/3").get().floatValue(RoundingMode.UP), -0.33333334);
        aeq(read("-1/3").get().floatValue(RoundingMode.HALF_DOWN), -0.33333334);
        aeq(read("-1/3").get().floatValue(RoundingMode.HALF_UP), -0.33333334);
        aeq(read("-1/3").get().floatValue(RoundingMode.HALF_EVEN), -0.33333334);
        try {
            read("-1/3").get().floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational almostOne = of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
        aeq(almostOne.floatValue(RoundingMode.FLOOR), 0.99999994);
        aeq(almostOne.floatValue(RoundingMode.CEILING), 1.0);
        aeq(almostOne.floatValue(RoundingMode.DOWN), 0.99999994);
        aeq(almostOne.floatValue(RoundingMode.UP), 1.0);
        aeq(almostOne.floatValue(RoundingMode.HALF_DOWN), 1.0);
        aeq(almostOne.floatValue(RoundingMode.HALF_UP), 1.0);
        aeq(almostOne.floatValue(RoundingMode.HALF_EVEN), 1.0);
        try {
            almostOne.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational floatPi = ofExact((float) Math.PI);
        if (floatPi == null) {
            fail();
        }
        aeq(floatPi.floatValue(RoundingMode.FLOOR), 3.1415927);
        aeq(floatPi.floatValue(RoundingMode.CEILING), 3.1415927);
        aeq(floatPi.floatValue(RoundingMode.DOWN), 3.1415927);
        aeq(floatPi.floatValue(RoundingMode.UP), 3.1415927);
        aeq(floatPi.floatValue(RoundingMode.HALF_DOWN), 3.1415927);
        aeq(floatPi.floatValue(RoundingMode.HALF_UP), 3.1415927);
        aeq(floatPi.floatValue(RoundingMode.HALF_EVEN), 3.1415927);
        aeq(floatPi.floatValue(RoundingMode.UNNECESSARY), 3.1415927);
        Rational trillion = of(BigInteger.TEN.pow(12));
        aeq(trillion.floatValue(RoundingMode.FLOOR), 1.0E12);
        aeq(trillion.floatValue(RoundingMode.CEILING), 1.00000006E12);
        aeq(trillion.floatValue(RoundingMode.DOWN), 1.0E12);
        aeq(trillion.floatValue(RoundingMode.UP), 1.00000006E12);
        aeq(trillion.floatValue(RoundingMode.HALF_DOWN), 1.0E12);
        aeq(trillion.floatValue(RoundingMode.HALF_UP), 1.0E12);
        aeq(trillion.floatValue(RoundingMode.HALF_EVEN), 1.0E12);
        try {
            trillion.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational piSuccessor = ofExact(FloatUtils.successor((float) Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = ofExact(FloatUtils.predecessor((float) Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = add(floatPi, piSuccessor).divide(2);
        Rational halfBelowPi = add(floatPi, piPredecessor).divide(2);
        Rational justAbovePi = add(floatPi.multiply(2), piSuccessor).divide(3);
        Rational justBelowPi = add(floatPi.multiply(2), piPredecessor).divide(3);
        aeq(halfAbovePi.floatValue(RoundingMode.FLOOR), 3.1415927);
        aeq(halfAbovePi.floatValue(RoundingMode.CEILING), 3.141593);
        aeq(halfAbovePi.floatValue(RoundingMode.DOWN), 3.1415927);
        aeq(halfAbovePi.floatValue(RoundingMode.UP), 3.141593);
        aeq(halfAbovePi.floatValue(RoundingMode.HALF_DOWN), 3.1415927);
        aeq(halfAbovePi.floatValue(RoundingMode.HALF_UP), 3.141593);
        aeq(halfAbovePi.floatValue(RoundingMode.HALF_EVEN), 3.141593);
        try {
            halfAbovePi.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfBelowPi.floatValue(RoundingMode.FLOOR), 3.1415925);
        aeq(halfBelowPi.floatValue(RoundingMode.CEILING), 3.1415927);
        aeq(halfBelowPi.floatValue(RoundingMode.DOWN), 3.1415925);
        aeq(halfBelowPi.floatValue(RoundingMode.UP), 3.1415927);
        aeq(halfBelowPi.floatValue(RoundingMode.HALF_DOWN), 3.1415925);
        aeq(halfBelowPi.floatValue(RoundingMode.HALF_UP), 3.1415927);
        aeq(halfBelowPi.floatValue(RoundingMode.HALF_EVEN), 3.1415925);
        try {
            halfBelowPi.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justAbovePi.floatValue(RoundingMode.FLOOR), 3.1415927);
        aeq(justAbovePi.floatValue(RoundingMode.CEILING), 3.141593);
        aeq(justAbovePi.floatValue(RoundingMode.DOWN), 3.1415927);
        aeq(justAbovePi.floatValue(RoundingMode.UP), 3.141593);
        aeq(justAbovePi.floatValue(RoundingMode.HALF_DOWN), 3.1415927);
        aeq(justAbovePi.floatValue(RoundingMode.HALF_UP), 3.1415927);
        aeq(justAbovePi.floatValue(RoundingMode.HALF_EVEN), 3.1415927);
        try {
            justAbovePi.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justBelowPi.floatValue(RoundingMode.FLOOR), 3.1415925);
        aeq(justBelowPi.floatValue(RoundingMode.CEILING), 3.1415927);
        aeq(justBelowPi.floatValue(RoundingMode.DOWN), 3.1415925);
        aeq(justBelowPi.floatValue(RoundingMode.UP), 3.1415927);
        aeq(justBelowPi.floatValue(RoundingMode.HALF_DOWN), 3.1415927);
        aeq(justBelowPi.floatValue(RoundingMode.HALF_UP), 3.1415927);
        aeq(justBelowPi.floatValue(RoundingMode.HALF_EVEN), 3.1415927);
        try {
            justBelowPi.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational subnormal = ofExact(1e-40f);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = ofExact(FloatUtils.successor(1e-40f));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = ofExact(FloatUtils.predecessor(1e-40f));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = add(subnormal, subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = add(subnormal, subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = add(subnormal.multiply(2), subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = add(subnormal.multiply(2), subnormalPredecessor).divide(3);
        aeq(subnormal.floatValue(RoundingMode.FLOOR), 1.0E-40);
        aeq(subnormal.floatValue(RoundingMode.CEILING), 1.0E-40);
        aeq(subnormal.floatValue(RoundingMode.DOWN), 1.0E-40);
        aeq(subnormal.floatValue(RoundingMode.UP), 1.0E-40);
        aeq(subnormal.floatValue(RoundingMode.HALF_DOWN), 1.0E-40);
        aeq(subnormal.floatValue(RoundingMode.HALF_UP), 1.0E-40);
        aeq(subnormal.floatValue(RoundingMode.HALF_EVEN), 1.0E-40);
        aeq(subnormal.floatValue(RoundingMode.UNNECESSARY), 1.0E-40);
        aeq(halfAboveSubnormal.floatValue(RoundingMode.FLOOR), 1.0E-40);
        aeq(halfAboveSubnormal.floatValue(RoundingMode.CEILING), 1.00001E-40);
        aeq(halfAboveSubnormal.floatValue(RoundingMode.DOWN), 1.0E-40);
        aeq(halfAboveSubnormal.floatValue(RoundingMode.UP), 1.00001E-40);
        aeq(halfAboveSubnormal.floatValue(RoundingMode.HALF_DOWN), 1.0E-40);
        aeq(halfAboveSubnormal.floatValue(RoundingMode.HALF_UP), 1.00001E-40);
        aeq(halfAboveSubnormal.floatValue(RoundingMode.HALF_EVEN), 1.0E-40);
        try {
            halfAboveSubnormal.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfBelowSubnormal.floatValue(RoundingMode.FLOOR), 9.9998E-41);
        aeq(halfBelowSubnormal.floatValue(RoundingMode.CEILING), 1.0E-40);
        aeq(halfBelowSubnormal.floatValue(RoundingMode.DOWN), 9.9998E-41);
        aeq(halfBelowSubnormal.floatValue(RoundingMode.UP), 1.0E-40);
        aeq(halfBelowSubnormal.floatValue(RoundingMode.HALF_DOWN), 9.9998E-41);
        aeq(halfBelowSubnormal.floatValue(RoundingMode.HALF_UP), 1.0E-40);
        aeq(halfBelowSubnormal.floatValue(RoundingMode.HALF_EVEN), 1.0E-40);
        try {
            halfBelowSubnormal.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justAboveSubnormal.floatValue(RoundingMode.FLOOR), 1.0E-40);
        aeq(justAboveSubnormal.floatValue(RoundingMode.CEILING), 1.00001E-40);
        aeq(justAboveSubnormal.floatValue(RoundingMode.DOWN), 1.0E-40);
        aeq(justAboveSubnormal.floatValue(RoundingMode.UP), 1.00001E-40);
        aeq(justAboveSubnormal.floatValue(RoundingMode.HALF_DOWN), 1.0E-40);
        aeq(justAboveSubnormal.floatValue(RoundingMode.HALF_UP), 1.0E-40);
        aeq(justAboveSubnormal.floatValue(RoundingMode.HALF_EVEN), 1.0E-40);
        try {
            justAboveSubnormal.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justBelowSubnormal.floatValue(RoundingMode.FLOOR), 9.9998E-41);
        aeq(justBelowSubnormal.floatValue(RoundingMode.CEILING), 1.0E-40);
        aeq(justBelowSubnormal.floatValue(RoundingMode.DOWN), 9.9998E-41);
        aeq(justBelowSubnormal.floatValue(RoundingMode.UP), 1.0E-40);
        aeq(justBelowSubnormal.floatValue(RoundingMode.HALF_DOWN), 1.0E-40);
        aeq(justBelowSubnormal.floatValue(RoundingMode.HALF_UP), 1.0E-40);
        aeq(justBelowSubnormal.floatValue(RoundingMode.HALF_EVEN), 1.0E-40);
        try {
            justBelowSubnormal.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational belowNegativeMax = subtract(LARGEST_FLOAT.negate(), ONE);
        Rational negativeMaxSuccessor = ofExact(FloatUtils.successor(-Float.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = add(LARGEST_FLOAT.negate(), negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = add(
                LARGEST_FLOAT.negate().multiply(2),
                negativeMaxSuccessor
        ).divide(3);
        aeq(belowNegativeMax.floatValue(RoundingMode.FLOOR), Float.NEGATIVE_INFINITY);
        aeq(belowNegativeMax.floatValue(RoundingMode.CEILING), -3.4028235E38);
        aeq(belowNegativeMax.floatValue(RoundingMode.DOWN), -3.4028235E38);
        aeq(belowNegativeMax.floatValue(RoundingMode.UP), Float.NEGATIVE_INFINITY);
        aeq(belowNegativeMax.floatValue(RoundingMode.HALF_DOWN), -3.4028235E38);
        aeq(belowNegativeMax.floatValue(RoundingMode.HALF_UP), Float.NEGATIVE_INFINITY);
        aeq(belowNegativeMax.floatValue(RoundingMode.HALF_EVEN), Float.NEGATIVE_INFINITY);
        try {
            belowNegativeMax.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfAboveNegativeMax.floatValue(RoundingMode.FLOOR), -3.4028235E38);
        aeq(halfAboveNegativeMax.floatValue(RoundingMode.CEILING), -3.4028233E38);
        aeq(halfAboveNegativeMax.floatValue(RoundingMode.DOWN), -3.4028233E38);
        aeq(halfAboveNegativeMax.floatValue(RoundingMode.UP), -3.4028235E38);
        aeq(halfAboveNegativeMax.floatValue(RoundingMode.HALF_DOWN), -3.4028233E38);
        aeq(halfAboveNegativeMax.floatValue(RoundingMode.HALF_UP), -3.4028235E38);
        aeq(halfAboveNegativeMax.floatValue(RoundingMode.HALF_EVEN), -3.4028233E38);
        try {
            halfAboveNegativeMax.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justAboveNegativeMax.floatValue(RoundingMode.FLOOR), -3.4028235E38);
        aeq(justAboveNegativeMax.floatValue(RoundingMode.CEILING), -3.4028233E38);
        aeq(justAboveNegativeMax.floatValue(RoundingMode.DOWN), -3.4028233E38);
        aeq(justAboveNegativeMax.floatValue(RoundingMode.UP), -3.4028235E38);
        aeq(justAboveNegativeMax.floatValue(RoundingMode.HALF_DOWN), -3.4028235E38);
        aeq(justAboveNegativeMax.floatValue(RoundingMode.HALF_UP), -3.4028235E38);
        aeq(justAboveNegativeMax.floatValue(RoundingMode.HALF_EVEN), -3.4028235E38);
        try {
            justAboveNegativeMax.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational aboveMax = add(LARGEST_FLOAT, ONE);
        Rational maxPredecessor = ofExact(FloatUtils.predecessor(Float.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = add(LARGEST_FLOAT, maxPredecessor).divide(2);
        Rational justBelowMax = add(LARGEST_FLOAT.multiply(2), maxPredecessor).divide(3);
        aeq(aboveMax.floatValue(RoundingMode.FLOOR), 3.4028235E38);
        aeq(aboveMax.floatValue(RoundingMode.CEILING), Float.POSITIVE_INFINITY);
        aeq(aboveMax.floatValue(RoundingMode.DOWN), 3.4028235E38);
        aeq(aboveMax.floatValue(RoundingMode.UP), Float.POSITIVE_INFINITY);
        aeq(aboveMax.floatValue(RoundingMode.HALF_DOWN), 3.4028235E38);
        aeq(aboveMax.floatValue(RoundingMode.HALF_UP), Float.POSITIVE_INFINITY);
        aeq(aboveMax.floatValue(RoundingMode.HALF_EVEN), Float.POSITIVE_INFINITY);
        try {
            aboveMax.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfBelowMax.floatValue(RoundingMode.FLOOR), 3.4028233E38);
        aeq(halfBelowMax.floatValue(RoundingMode.CEILING), 3.4028235E38);
        aeq(halfBelowMax.floatValue(RoundingMode.DOWN), 3.4028233E38);
        aeq(halfBelowMax.floatValue(RoundingMode.UP), 3.4028235E38);
        aeq(halfBelowMax.floatValue(RoundingMode.HALF_DOWN), 3.4028233E38);
        aeq(halfBelowMax.floatValue(RoundingMode.HALF_UP), 3.4028235E38);
        aeq(halfBelowMax.floatValue(RoundingMode.HALF_EVEN), 3.4028233E38);
        try {
            halfBelowMax.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justBelowMax.floatValue(RoundingMode.FLOOR), 3.4028233E38);
        aeq(justBelowMax.floatValue(RoundingMode.CEILING), 3.4028235E38);
        aeq(justBelowMax.floatValue(RoundingMode.DOWN), 3.4028233E38);
        aeq(justBelowMax.floatValue(RoundingMode.UP), 3.4028235E38);
        aeq(justBelowMax.floatValue(RoundingMode.HALF_DOWN), 3.4028235E38);
        aeq(justBelowMax.floatValue(RoundingMode.HALF_UP), 3.4028235E38);
        aeq(justBelowMax.floatValue(RoundingMode.HALF_EVEN), 3.4028235E38);
        try {
            justBelowMax.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational halfAboveZero = SMALLEST_FLOAT.shiftRight(1);
        Rational justAboveZero = SMALLEST_FLOAT.divide(3);
        Rational halfBelowZero = halfAboveZero.negate();
        Rational justBelowZero = justAboveZero.negate();
        aeq(halfBelowZero.floatValue(RoundingMode.FLOOR), -1.4E-45);
        aeq(halfBelowZero.floatValue(RoundingMode.CEILING), -0.0);
        aeq(halfBelowZero.floatValue(RoundingMode.DOWN), -0.0);
        aeq(halfBelowZero.floatValue(RoundingMode.UP), -1.4E-45);
        aeq(halfBelowZero.floatValue(RoundingMode.HALF_DOWN), -0.0);
        aeq(halfBelowZero.floatValue(RoundingMode.HALF_UP), -1.4E-45);
        aeq(halfBelowZero.floatValue(RoundingMode.HALF_EVEN), -0.0);
        try {
            halfBelowZero.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justBelowZero.floatValue(RoundingMode.FLOOR), -1.4E-45);
        aeq(justBelowZero.floatValue(RoundingMode.CEILING), -0.0);
        aeq(justBelowZero.floatValue(RoundingMode.DOWN), -0.0);
        aeq(justBelowZero.floatValue(RoundingMode.UP), -1.4E-45);
        aeq(justBelowZero.floatValue(RoundingMode.HALF_DOWN), -0.0);
        aeq(justBelowZero.floatValue(RoundingMode.HALF_UP), -0.0);
        aeq(justBelowZero.floatValue(RoundingMode.HALF_EVEN), -0.0);
        try {
            justBelowZero.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfAboveZero.floatValue(RoundingMode.FLOOR), 0.0);
        aeq(halfAboveZero.floatValue(RoundingMode.CEILING), 1.4E-45);
        aeq(halfAboveZero.floatValue(RoundingMode.DOWN), 0.0);
        aeq(halfAboveZero.floatValue(RoundingMode.UP), 1.4E-45);
        aeq(halfAboveZero.floatValue(RoundingMode.HALF_DOWN), 0.0);
        aeq(halfAboveZero.floatValue(RoundingMode.HALF_UP), 1.4E-45);
        aeq(halfAboveZero.floatValue(RoundingMode.HALF_EVEN), 0.0);
        try {
            halfAboveZero.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justAboveZero.floatValue(RoundingMode.FLOOR), 0.0);
        aeq(justAboveZero.floatValue(RoundingMode.CEILING), 1.4E-45);
        aeq(justAboveZero.floatValue(RoundingMode.DOWN), 0.0);
        aeq(justAboveZero.floatValue(RoundingMode.UP), 1.4E-45);
        aeq(justAboveZero.floatValue(RoundingMode.HALF_DOWN), 0.0);
        aeq(justAboveZero.floatValue(RoundingMode.HALF_UP), 0.0);
        aeq(justAboveZero.floatValue(RoundingMode.HALF_EVEN), 0.0);
        try {
            justAboveZero.floatValue(RoundingMode.UNNECESSARY);
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
        aeq(boundary.floatValue(RoundingMode.FLOOR), 1.1754942E-38);
        aeq(boundary.floatValue(RoundingMode.CEILING), 1.17549435E-38);
        aeq(boundary.floatValue(RoundingMode.DOWN), 1.1754942E-38);
        aeq(boundary.floatValue(RoundingMode.UP), 1.17549435E-38);
        aeq(boundary.floatValue(RoundingMode.HALF_DOWN), 1.1754942E-38);
        aeq(boundary.floatValue(RoundingMode.HALF_UP), 1.17549435E-38);
        aeq(boundary.floatValue(RoundingMode.HALF_EVEN), 1.17549435E-38);
        try {
            boundary.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfBelowBoundary.floatValue(RoundingMode.FLOOR), 1.1754942E-38);
        aeq(halfBelowBoundary.floatValue(RoundingMode.CEILING), 1.17549435E-38);
        aeq(halfBelowBoundary.floatValue(RoundingMode.DOWN), 1.1754942E-38);
        aeq(halfBelowBoundary.floatValue(RoundingMode.UP), 1.17549435E-38);
        aeq(halfBelowBoundary.floatValue(RoundingMode.HALF_DOWN), 1.1754942E-38);
        aeq(halfBelowBoundary.floatValue(RoundingMode.HALF_UP), 1.1754942E-38);
        aeq(halfBelowBoundary.floatValue(RoundingMode.HALF_EVEN), 1.1754942E-38);
        try {
            halfBelowBoundary.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justBelowBoundary.floatValue(RoundingMode.FLOOR), 1.1754942E-38);
        aeq(justBelowBoundary.floatValue(RoundingMode.CEILING), 1.17549435E-38);
        aeq(justBelowBoundary.floatValue(RoundingMode.DOWN), 1.1754942E-38);
        aeq(justBelowBoundary.floatValue(RoundingMode.UP), 1.17549435E-38);
        aeq(justBelowBoundary.floatValue(RoundingMode.HALF_DOWN), 1.1754942E-38);
        aeq(justBelowBoundary.floatValue(RoundingMode.HALF_UP), 1.1754942E-38);
        aeq(justBelowBoundary.floatValue(RoundingMode.HALF_EVEN), 1.1754942E-38);
        try {
            justBelowBoundary.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfAboveBoundary.floatValue(RoundingMode.FLOOR), 1.1754942E-38);
        aeq(halfAboveBoundary.floatValue(RoundingMode.CEILING), 1.17549435E-38);
        aeq(halfAboveBoundary.floatValue(RoundingMode.DOWN), 1.1754942E-38);
        aeq(halfAboveBoundary.floatValue(RoundingMode.UP), 1.17549435E-38);
        aeq(halfAboveBoundary.floatValue(RoundingMode.HALF_DOWN), 1.17549435E-38);
        aeq(halfAboveBoundary.floatValue(RoundingMode.HALF_UP), 1.17549435E-38);
        aeq(halfAboveBoundary.floatValue(RoundingMode.HALF_EVEN), 1.17549435E-38);
        try {
            halfAboveBoundary.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justAboveBoundary.floatValue(RoundingMode.FLOOR), 1.1754942E-38);
        aeq(justAboveBoundary.floatValue(RoundingMode.CEILING), 1.17549435E-38);
        aeq(justAboveBoundary.floatValue(RoundingMode.DOWN), 1.1754942E-38);
        aeq(justAboveBoundary.floatValue(RoundingMode.UP), 1.17549435E-38);
        aeq(justAboveBoundary.floatValue(RoundingMode.HALF_DOWN), 1.17549435E-38);
        aeq(justAboveBoundary.floatValue(RoundingMode.HALF_UP), 1.17549435E-38);
        aeq(justAboveBoundary.floatValue(RoundingMode.HALF_EVEN), 1.17549435E-38);
        try {
            justAboveBoundary.floatValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testToDouble() {
        aeq(ZERO.doubleValue(), 0.0);
        aeq(ONE.doubleValue(), 1.0);
        aeq(read("1/2").get().doubleValue(), 0.5);
        aeq(read("1/3").get().doubleValue(), 0.3333333333333333);
        aeq(read("-1/3").get().doubleValue(), -0.3333333333333333);
        Rational almostOne = of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
        aeq(almostOne.doubleValue(), 1.0);
        Rational pi = ofExact(Math.PI);
        if (pi == null) {
            fail();
        }
        aeq(pi.doubleValue(), 3.141592653589793);
        Rational googol = of(BigInteger.TEN.pow(100));
        aeq(googol.doubleValue(), 1.0E100);
        Rational piSuccessor = ofExact(FloatUtils.successor(Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = ofExact(FloatUtils.predecessor(Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = add(pi, piSuccessor).divide(2);
        Rational halfBelowPi = add(pi, piPredecessor).divide(2);
        Rational justAbovePi = add(pi.multiply(2), piSuccessor).divide(3);
        Rational justBelowPi = add(pi.multiply(2), piPredecessor).divide(3);
        aeq(halfAbovePi.doubleValue(), 3.141592653589793);
        aeq(halfBelowPi.doubleValue(), 3.141592653589793);
        aeq(justAbovePi.doubleValue(), 3.141592653589793);
        aeq(justBelowPi.doubleValue(), 3.141592653589793);
        Rational subnormal = ofExact(1e-310);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = ofExact(FloatUtils.successor(1e-310));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = ofExact(FloatUtils.predecessor(1e-310));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = add(subnormal, subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = add(subnormal, subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = add(subnormal.multiply(2), subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = add(subnormal.multiply(2), subnormalPredecessor).divide(3);
        aeq(subnormal.doubleValue(), 1.0E-310);
        aeq(halfAboveSubnormal.doubleValue(), 1.00000000000005E-310);
        aeq(halfBelowSubnormal.doubleValue(), 9.9999999999995E-311);
        aeq(justAboveSubnormal.doubleValue(), 1.0E-310);
        aeq(justBelowSubnormal.doubleValue(), 1.0E-310);
        Rational belowNegativeMax = subtract(LARGEST_DOUBLE.negate(), ONE);
        Rational negativeMaxSuccessor = ofExact(FloatUtils.successor(-Double.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = add(LARGEST_DOUBLE.negate(), negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = add(
                LARGEST_DOUBLE.negate().multiply(2),
                negativeMaxSuccessor
        ).divide(3);
        aeq(belowNegativeMax.doubleValue(), Double.NEGATIVE_INFINITY);
        aeq(halfAboveNegativeMax.doubleValue(), -1.7976931348623155E308);
        aeq(justAboveNegativeMax.doubleValue(), -1.7976931348623157E308);
        Rational aboveMax = add(LARGEST_DOUBLE, ONE);
        Rational maxPredecessor = ofExact(FloatUtils.predecessor(Double.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = add(LARGEST_DOUBLE, maxPredecessor).divide(2);
        Rational justBelowMax = add(LARGEST_DOUBLE.multiply(2), maxPredecessor).divide(3);
        aeq(aboveMax.doubleValue(), Double.POSITIVE_INFINITY);
        aeq(halfBelowMax.doubleValue(), 1.7976931348623155E308);
        aeq(justBelowMax.doubleValue(), 1.7976931348623157E308);
        Rational halfAboveZero = SMALLEST_DOUBLE.shiftRight(1);
        Rational justAboveZero = SMALLEST_DOUBLE.divide(3);
        Rational halfBelowZero = halfAboveZero.negate();
        Rational justBelowZero = justAboveZero.negate();
        aeq(halfBelowZero.doubleValue(), -0.0);
        aeq(justBelowZero.doubleValue(), -0.0);
        aeq(halfAboveZero.doubleValue(), 0.0);
        aeq(justAboveZero.doubleValue(), 0.0);
        Rational boundary = add(
                LARGEST_SUBNORMAL_DOUBLE,
                SMALLEST_NORMAL_DOUBLE
        ).shiftRight(1);
        Rational halfBelowBoundary = add(LARGEST_SUBNORMAL_DOUBLE, boundary).shiftRight(1);
        Rational halfAboveBoundary = add(SMALLEST_NORMAL_DOUBLE, boundary).shiftRight(1);
        Rational justBelowBoundary = add(LARGEST_SUBNORMAL_DOUBLE, boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = add(SMALLEST_NORMAL_DOUBLE, boundary.shiftLeft(1)).divide(3);
        aeq(boundary.doubleValue(), 2.2250738585072014E-308);
        aeq(halfBelowBoundary.doubleValue(), 2.225073858507201E-308);
        aeq(justBelowBoundary.doubleValue(), 2.225073858507201E-308);
        aeq(halfAboveBoundary.doubleValue(), 2.2250738585072014E-308);
        aeq(justAboveBoundary.doubleValue(), 2.2250738585072014E-308);
    }

    @Test
    public void testToDouble_RoundingMode() {
        aeq(ZERO.doubleValue(RoundingMode.FLOOR), 0.0);
        aeq(ZERO.doubleValue(RoundingMode.CEILING), 0.0);
        aeq(ZERO.doubleValue(RoundingMode.DOWN), 0.0);
        aeq(ZERO.doubleValue(RoundingMode.UP), 0.0);
        aeq(ZERO.doubleValue(RoundingMode.HALF_DOWN), 0.0);
        aeq(ZERO.doubleValue(RoundingMode.HALF_UP), 0.0);
        aeq(ZERO.doubleValue(RoundingMode.HALF_EVEN), 0.0);
        aeq(ZERO.doubleValue(RoundingMode.UNNECESSARY), 0.0);
        aeq(ONE.doubleValue(RoundingMode.FLOOR), 1.0);
        aeq(ONE.doubleValue(RoundingMode.CEILING), 1.0);
        aeq(ONE.doubleValue(RoundingMode.DOWN), 1.0);
        aeq(ONE.doubleValue(RoundingMode.UP), 1.0);
        aeq(ONE.doubleValue(RoundingMode.HALF_DOWN), 1.0);
        aeq(ONE.doubleValue(RoundingMode.HALF_UP), 1.0);
        aeq(ONE.doubleValue(RoundingMode.HALF_EVEN), 1.0);
        aeq(ONE.doubleValue(RoundingMode.UNNECESSARY), 1.0);
        aeq(read("1/2").get().doubleValue(RoundingMode.FLOOR), 0.5);
        aeq(read("1/2").get().doubleValue(RoundingMode.CEILING), 0.5);
        aeq(read("1/2").get().doubleValue(RoundingMode.DOWN), 0.5);
        aeq(read("1/2").get().doubleValue(RoundingMode.UP), 0.5);
        aeq(read("1/2").get().doubleValue(RoundingMode.HALF_DOWN), 0.5);
        aeq(read("1/2").get().doubleValue(RoundingMode.HALF_UP), 0.5);
        aeq(read("1/2").get().doubleValue(RoundingMode.HALF_EVEN), 0.5);
        aeq(read("1/2").get().doubleValue(RoundingMode.UNNECESSARY), 0.5);
        aeq(read("1/3").get().doubleValue(RoundingMode.FLOOR), 0.3333333333333333);
        aeq(read("1/3").get().doubleValue(RoundingMode.CEILING), 0.33333333333333337);
        aeq(read("1/3").get().doubleValue(RoundingMode.DOWN), 0.3333333333333333);
        aeq(read("1/3").get().doubleValue(RoundingMode.UP), 0.33333333333333337);
        aeq(read("1/3").get().doubleValue(RoundingMode.HALF_DOWN), 0.3333333333333333);
        aeq(read("1/3").get().doubleValue(RoundingMode.HALF_UP), 0.3333333333333333);
        aeq(read("1/3").get().doubleValue(RoundingMode.HALF_EVEN), 0.3333333333333333);
        try {
            read("1/3").get().doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("-1/3").get().doubleValue(RoundingMode.FLOOR), -0.33333333333333337);
        aeq(read("-1/3").get().doubleValue(RoundingMode.CEILING), -0.3333333333333333);
        aeq(read("-1/3").get().doubleValue(RoundingMode.DOWN), -0.3333333333333333);
        aeq(read("-1/3").get().doubleValue(RoundingMode.UP), -0.33333333333333337);
        aeq(read("-1/3").get().doubleValue(RoundingMode.HALF_DOWN), -0.3333333333333333);
        aeq(read("-1/3").get().doubleValue(RoundingMode.HALF_UP), -0.3333333333333333);
        aeq(read("-1/3").get().doubleValue(RoundingMode.HALF_EVEN), -0.3333333333333333);
        try {
            read("-1/3").get().doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational almostOne = of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
        aeq(almostOne.doubleValue(RoundingMode.FLOOR), 0.9999999999999999);
        aeq(almostOne.doubleValue(RoundingMode.CEILING), 1.0);
        aeq(almostOne.doubleValue(RoundingMode.DOWN), 0.9999999999999999);
        aeq(almostOne.doubleValue(RoundingMode.UP), 1.0);
        aeq(almostOne.doubleValue(RoundingMode.HALF_DOWN), 1.0);
        aeq(almostOne.doubleValue(RoundingMode.HALF_UP), 1.0);
        aeq(almostOne.doubleValue(RoundingMode.HALF_EVEN), 1.0);
        try {
            almostOne.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational pi = ofExact(Math.PI);
        if (pi == null) {
            fail();
        }
        aeq(pi.doubleValue(RoundingMode.FLOOR), 3.141592653589793);
        aeq(pi.doubleValue(RoundingMode.CEILING), 3.141592653589793);
        aeq(pi.doubleValue(RoundingMode.DOWN), 3.141592653589793);
        aeq(pi.doubleValue(RoundingMode.UP), 3.141592653589793);
        aeq(pi.doubleValue(RoundingMode.HALF_DOWN), 3.141592653589793);
        aeq(pi.doubleValue(RoundingMode.HALF_UP), 3.141592653589793);
        aeq(pi.doubleValue(RoundingMode.HALF_EVEN), 3.141592653589793);
        aeq(pi.doubleValue(RoundingMode.UNNECESSARY), 3.141592653589793);
        Rational googol = of(BigInteger.TEN.pow(100));
        aeq(googol.doubleValue(RoundingMode.FLOOR), 9.999999999999998E99);
        aeq(googol.doubleValue(RoundingMode.CEILING), 1.0E100);
        aeq(googol.doubleValue(RoundingMode.DOWN), 9.999999999999998E99);
        aeq(googol.doubleValue(RoundingMode.UP), 1.0E100);
        aeq(googol.doubleValue(RoundingMode.HALF_DOWN), 1.0E100);
        aeq(googol.doubleValue(RoundingMode.HALF_UP), 1.0E100);
        aeq(googol.doubleValue(RoundingMode.HALF_EVEN), 1.0E100);
        try {
            googol.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational piSuccessor = ofExact(FloatUtils.successor(Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = ofExact(FloatUtils.predecessor(Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = add(pi, piSuccessor).divide(2);
        Rational halfBelowPi = add(pi, piPredecessor).divide(2);
        Rational justAbovePi = add(pi.multiply(2), piSuccessor).divide(3);
        Rational justBelowPi = add(pi.multiply(2), piPredecessor).divide(3);
        aeq(halfAbovePi.doubleValue(RoundingMode.FLOOR), 3.141592653589793);
        aeq(halfAbovePi.doubleValue(RoundingMode.CEILING), 3.1415926535897936);
        aeq(halfAbovePi.doubleValue(RoundingMode.DOWN), 3.141592653589793);
        aeq(halfAbovePi.doubleValue(RoundingMode.UP), 3.1415926535897936);
        aeq(halfAbovePi.doubleValue(RoundingMode.HALF_DOWN), 3.141592653589793);
        aeq(halfAbovePi.doubleValue(RoundingMode.HALF_UP), 3.1415926535897936);
        aeq(halfAbovePi.doubleValue(RoundingMode.HALF_EVEN), 3.141592653589793);
        try {
            halfAbovePi.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfBelowPi.doubleValue(RoundingMode.FLOOR), 3.1415926535897927);
        aeq(halfBelowPi.doubleValue(RoundingMode.CEILING), 3.141592653589793);
        aeq(halfBelowPi.doubleValue(RoundingMode.DOWN), 3.1415926535897927);
        aeq(halfBelowPi.doubleValue(RoundingMode.UP), 3.141592653589793);
        aeq(halfBelowPi.doubleValue(RoundingMode.HALF_DOWN), 3.1415926535897927);
        aeq(halfBelowPi.doubleValue(RoundingMode.HALF_UP), 3.141592653589793);
        aeq(halfBelowPi.doubleValue(RoundingMode.HALF_EVEN), 3.141592653589793);
        try {
            halfBelowPi.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justAbovePi.doubleValue(RoundingMode.FLOOR), 3.141592653589793);
        aeq(justAbovePi.doubleValue(RoundingMode.CEILING), 3.1415926535897936);
        aeq(justAbovePi.doubleValue(RoundingMode.DOWN), 3.141592653589793);
        aeq(justAbovePi.doubleValue(RoundingMode.UP), 3.1415926535897936);
        aeq(justAbovePi.doubleValue(RoundingMode.HALF_DOWN), 3.141592653589793);
        aeq(justAbovePi.doubleValue(RoundingMode.HALF_UP), 3.141592653589793);
        aeq(justAbovePi.doubleValue(RoundingMode.HALF_EVEN), 3.141592653589793);
        try {
            justAbovePi.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justBelowPi.doubleValue(RoundingMode.FLOOR), 3.1415926535897927);
        aeq(justBelowPi.doubleValue(RoundingMode.CEILING), 3.141592653589793);
        aeq(justBelowPi.doubleValue(RoundingMode.DOWN), 3.1415926535897927);
        aeq(justBelowPi.doubleValue(RoundingMode.UP), 3.141592653589793);
        aeq(justBelowPi.doubleValue(RoundingMode.HALF_DOWN), 3.141592653589793);
        aeq(justBelowPi.doubleValue(RoundingMode.HALF_UP), 3.141592653589793);
        aeq(justBelowPi.doubleValue(RoundingMode.HALF_EVEN), 3.141592653589793);
        try {
            justBelowPi.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational subnormal = ofExact(1e-310);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = ofExact(FloatUtils.successor(1e-310));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = ofExact(FloatUtils.predecessor(1e-310));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = add(subnormal, subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = add(subnormal, subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = add(subnormal.multiply(2), subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = add(subnormal.multiply(2), subnormalPredecessor).divide(3);
        aeq(subnormal.doubleValue(RoundingMode.FLOOR), 1.0E-310);
        aeq(subnormal.doubleValue(RoundingMode.CEILING), 1.0E-310);
        aeq(subnormal.doubleValue(RoundingMode.DOWN), 1.0E-310);
        aeq(subnormal.doubleValue(RoundingMode.UP), 1.0E-310);
        aeq(subnormal.doubleValue(RoundingMode.HALF_DOWN), 1.0E-310);
        aeq(subnormal.doubleValue(RoundingMode.HALF_UP), 1.0E-310);
        aeq(subnormal.doubleValue(RoundingMode.HALF_EVEN), 1.0E-310);
        aeq(subnormal.doubleValue(RoundingMode.UNNECESSARY), 1.0E-310);
        aeq(halfAboveSubnormal.doubleValue(RoundingMode.FLOOR), 1.0E-310);
        aeq(halfAboveSubnormal.doubleValue(RoundingMode.CEILING), 1.00000000000005E-310);
        aeq(halfAboveSubnormal.doubleValue(RoundingMode.DOWN), 1.0E-310);
        aeq(halfAboveSubnormal.doubleValue(RoundingMode.UP), 1.00000000000005E-310);
        aeq(halfAboveSubnormal.doubleValue(RoundingMode.HALF_DOWN), 1.0E-310);
        aeq(halfAboveSubnormal.doubleValue(RoundingMode.HALF_UP), 1.00000000000005E-310);
        aeq(halfAboveSubnormal.doubleValue(RoundingMode.HALF_EVEN), 1.00000000000005E-310);
        try {
            halfAboveSubnormal.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfBelowSubnormal.doubleValue(RoundingMode.FLOOR), 9.9999999999995E-311);
        aeq(halfBelowSubnormal.doubleValue(RoundingMode.CEILING), 1.0E-310);
        aeq(halfBelowSubnormal.doubleValue(RoundingMode.DOWN), 9.9999999999995E-311);
        aeq(halfBelowSubnormal.doubleValue(RoundingMode.UP), 1.0E-310);
        aeq(halfBelowSubnormal.doubleValue(RoundingMode.HALF_DOWN), 9.9999999999995E-311);
        aeq(halfBelowSubnormal.doubleValue(RoundingMode.HALF_UP), 1.0E-310);
        aeq(halfBelowSubnormal.doubleValue(RoundingMode.HALF_EVEN), 9.9999999999995E-311);
        try {
            halfBelowSubnormal.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justAboveSubnormal.doubleValue(RoundingMode.FLOOR), 1.0E-310);
        aeq(justAboveSubnormal.doubleValue(RoundingMode.CEILING), 1.00000000000005E-310);
        aeq(justAboveSubnormal.doubleValue(RoundingMode.DOWN), 1.0E-310);
        aeq(justAboveSubnormal.doubleValue(RoundingMode.UP), 1.00000000000005E-310);
        aeq(justAboveSubnormal.doubleValue(RoundingMode.HALF_DOWN), 1.0E-310);
        aeq(justAboveSubnormal.doubleValue(RoundingMode.HALF_UP), 1.0E-310);
        aeq(justAboveSubnormal.doubleValue(RoundingMode.HALF_EVEN), 1.0E-310);
        try {
            justAboveSubnormal.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justBelowSubnormal.doubleValue(RoundingMode.FLOOR), 9.9999999999995E-311);
        aeq(justBelowSubnormal.doubleValue(RoundingMode.CEILING), 1.0E-310);
        aeq(justBelowSubnormal.doubleValue(RoundingMode.DOWN), 9.9999999999995E-311);
        aeq(justBelowSubnormal.doubleValue(RoundingMode.UP), 1.0E-310);
        aeq(justBelowSubnormal.doubleValue(RoundingMode.HALF_DOWN), 1.0E-310);
        aeq(justBelowSubnormal.doubleValue(RoundingMode.HALF_UP), 1.0E-310);
        aeq(justBelowSubnormal.doubleValue(RoundingMode.HALF_EVEN), 1.0E-310);
        try {
            justBelowSubnormal.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational belowNegativeMax = subtract(LARGEST_DOUBLE.negate(), ONE);
        Rational negativeMaxSuccessor = ofExact(FloatUtils.successor(-Double.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = add(LARGEST_DOUBLE.negate(), negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = add(
                LARGEST_DOUBLE.negate().multiply(2),
                negativeMaxSuccessor
        ).divide(3);
        aeq(belowNegativeMax.doubleValue(RoundingMode.FLOOR), Double.NEGATIVE_INFINITY);
        aeq(belowNegativeMax.doubleValue(RoundingMode.CEILING), -1.7976931348623157E308);
        aeq(belowNegativeMax.doubleValue(RoundingMode.DOWN), -1.7976931348623157E308);
        aeq(belowNegativeMax.doubleValue(RoundingMode.UP), Double.NEGATIVE_INFINITY);
        aeq(belowNegativeMax.doubleValue(RoundingMode.HALF_DOWN), -1.7976931348623157E308);
        aeq(belowNegativeMax.doubleValue(RoundingMode.HALF_UP), Double.NEGATIVE_INFINITY);
        aeq(belowNegativeMax.doubleValue(RoundingMode.HALF_EVEN), Double.NEGATIVE_INFINITY);
        try {
            belowNegativeMax.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfAboveNegativeMax.doubleValue(RoundingMode.FLOOR), -1.7976931348623157E308);
        aeq(halfAboveNegativeMax.doubleValue(RoundingMode.CEILING), -1.7976931348623155E308);
        aeq(halfAboveNegativeMax.doubleValue(RoundingMode.DOWN), -1.7976931348623155E308);
        aeq(halfAboveNegativeMax.doubleValue(RoundingMode.UP), -1.7976931348623157E308);
        aeq(halfAboveNegativeMax.doubleValue(RoundingMode.HALF_DOWN), -1.7976931348623155E308);
        aeq(halfAboveNegativeMax.doubleValue(RoundingMode.HALF_UP), -1.7976931348623157E308);
        aeq(halfAboveNegativeMax.doubleValue(RoundingMode.HALF_EVEN), -1.7976931348623155E308);
        try {
            halfAboveNegativeMax.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justAboveNegativeMax.doubleValue(RoundingMode.FLOOR), -1.7976931348623157E308);
        aeq(justAboveNegativeMax.doubleValue(RoundingMode.CEILING), -1.7976931348623155E308);
        aeq(justAboveNegativeMax.doubleValue(RoundingMode.DOWN), -1.7976931348623155E308);
        aeq(justAboveNegativeMax.doubleValue(RoundingMode.UP), -1.7976931348623157E308);
        aeq(justAboveNegativeMax.doubleValue(RoundingMode.HALF_DOWN), -1.7976931348623157E308);
        aeq(justAboveNegativeMax.doubleValue(RoundingMode.HALF_UP), -1.7976931348623157E308);
        aeq(justAboveNegativeMax.doubleValue(RoundingMode.HALF_EVEN), -1.7976931348623157E308);
        try {
            justAboveNegativeMax.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational aboveMax = add(LARGEST_DOUBLE, ONE);
        Rational maxPredecessor = ofExact(FloatUtils.predecessor(Double.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = add(LARGEST_DOUBLE, maxPredecessor).divide(2);
        Rational justBelowMax = add(LARGEST_DOUBLE.multiply(2), maxPredecessor).divide(3);
        aeq(aboveMax.doubleValue(RoundingMode.FLOOR), 1.7976931348623157E308);
        aeq(aboveMax.doubleValue(RoundingMode.CEILING), Double.POSITIVE_INFINITY);
        aeq(aboveMax.doubleValue(RoundingMode.DOWN), 1.7976931348623157E308);
        aeq(aboveMax.doubleValue(RoundingMode.UP), Double.POSITIVE_INFINITY);
        aeq(aboveMax.doubleValue(RoundingMode.HALF_DOWN), 1.7976931348623157E308);
        aeq(aboveMax.doubleValue(RoundingMode.HALF_UP), Double.POSITIVE_INFINITY);
        aeq(aboveMax.doubleValue(RoundingMode.HALF_EVEN), Double.POSITIVE_INFINITY);
        try {
            aboveMax.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfBelowMax.doubleValue(RoundingMode.FLOOR), 1.7976931348623155E308);
        aeq(halfBelowMax.doubleValue(RoundingMode.CEILING), 1.7976931348623157E308);
        aeq(halfBelowMax.doubleValue(RoundingMode.DOWN), 1.7976931348623155E308);
        aeq(halfBelowMax.doubleValue(RoundingMode.UP), 1.7976931348623157E308);
        aeq(halfBelowMax.doubleValue(RoundingMode.HALF_DOWN), 1.7976931348623155E308);
        aeq(halfBelowMax.doubleValue(RoundingMode.HALF_UP), 1.7976931348623157E308);
        aeq(halfBelowMax.doubleValue(RoundingMode.HALF_EVEN), 1.7976931348623155E308);
        try {
            halfBelowMax.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justBelowMax.doubleValue(RoundingMode.FLOOR), 1.7976931348623155E308);
        aeq(justBelowMax.doubleValue(RoundingMode.CEILING), 1.7976931348623157E308);
        aeq(justBelowMax.doubleValue(RoundingMode.DOWN), 1.7976931348623155E308);
        aeq(justBelowMax.doubleValue(RoundingMode.UP), 1.7976931348623157E308);
        aeq(justBelowMax.doubleValue(RoundingMode.HALF_DOWN), 1.7976931348623157E308);
        aeq(justBelowMax.doubleValue(RoundingMode.HALF_UP), 1.7976931348623157E308);
        aeq(justBelowMax.doubleValue(RoundingMode.HALF_EVEN), 1.7976931348623157E308);
        try {
            justBelowMax.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        Rational halfAboveZero = SMALLEST_DOUBLE.shiftRight(1);
        Rational justAboveZero = SMALLEST_DOUBLE.divide(3);
        Rational halfBelowZero = halfAboveZero.negate();
        Rational justBelowZero = justAboveZero.negate();
        aeq(halfBelowZero.doubleValue(RoundingMode.FLOOR), -4.9E-324);
        aeq(halfBelowZero.doubleValue(RoundingMode.CEILING), -0.0);
        aeq(halfBelowZero.doubleValue(RoundingMode.DOWN), -0.0);
        aeq(halfBelowZero.doubleValue(RoundingMode.UP), -4.9E-324);
        aeq(halfBelowZero.doubleValue(RoundingMode.HALF_DOWN), -0.0);
        aeq(halfBelowZero.doubleValue(RoundingMode.HALF_UP), -4.9E-324);
        aeq(halfBelowZero.doubleValue(RoundingMode.HALF_EVEN), -0.0);
        try {
            halfBelowZero.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justBelowZero.doubleValue(RoundingMode.FLOOR), -4.9E-324);
        aeq(justBelowZero.doubleValue(RoundingMode.CEILING), -0.0);
        aeq(justBelowZero.doubleValue(RoundingMode.DOWN), -0.0);
        aeq(justBelowZero.doubleValue(RoundingMode.UP), -4.9E-324);
        aeq(justBelowZero.doubleValue(RoundingMode.HALF_DOWN), -0.0);
        aeq(justBelowZero.doubleValue(RoundingMode.HALF_UP), -0.0);
        aeq(justBelowZero.doubleValue(RoundingMode.HALF_EVEN), -0.0);
        try {
            justBelowZero.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfAboveZero.doubleValue(RoundingMode.FLOOR), 0.0);
        aeq(halfAboveZero.doubleValue(RoundingMode.CEILING), 4.9E-324);
        aeq(halfAboveZero.doubleValue(RoundingMode.DOWN), 0.0);
        aeq(halfAboveZero.doubleValue(RoundingMode.UP), 4.9E-324);
        aeq(halfAboveZero.doubleValue(RoundingMode.HALF_DOWN), 0.0);
        aeq(halfAboveZero.doubleValue(RoundingMode.HALF_UP), 4.9E-324);
        aeq(halfAboveZero.doubleValue(RoundingMode.HALF_EVEN), 0.0);
        try {
            halfAboveZero.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justAboveZero.doubleValue(RoundingMode.FLOOR), 0.0);
        aeq(justAboveZero.doubleValue(RoundingMode.CEILING), 4.9E-324);
        aeq(justAboveZero.doubleValue(RoundingMode.DOWN), 0.0);
        aeq(justAboveZero.doubleValue(RoundingMode.UP), 4.9E-324);
        aeq(justAboveZero.doubleValue(RoundingMode.HALF_DOWN), 0.0);
        aeq(justAboveZero.doubleValue(RoundingMode.HALF_UP), 0.0);
        aeq(justAboveZero.doubleValue(RoundingMode.HALF_EVEN), 0.0);
        try {
            justAboveZero.doubleValue(RoundingMode.UNNECESSARY);
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
        aeq(boundary.doubleValue(RoundingMode.FLOOR), 2.225073858507201E-308);
        aeq(boundary.doubleValue(RoundingMode.CEILING), 2.2250738585072014E-308);
        aeq(boundary.doubleValue(RoundingMode.DOWN), 2.225073858507201E-308);
        aeq(boundary.doubleValue(RoundingMode.UP), 2.2250738585072014E-308);
        aeq(boundary.doubleValue(RoundingMode.HALF_DOWN), 2.225073858507201E-308);
        aeq(boundary.doubleValue(RoundingMode.HALF_UP), 2.2250738585072014E-308);
        aeq(boundary.doubleValue(RoundingMode.HALF_EVEN), 2.2250738585072014E-308);
        try {
            boundary.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfBelowBoundary.doubleValue(RoundingMode.FLOOR), 2.225073858507201E-308);
        aeq(halfBelowBoundary.doubleValue(RoundingMode.CEILING), 2.2250738585072014E-308);
        aeq(halfBelowBoundary.doubleValue(RoundingMode.DOWN), 2.225073858507201E-308);
        aeq(halfBelowBoundary.doubleValue(RoundingMode.UP), 2.2250738585072014E-308);
        aeq(halfBelowBoundary.doubleValue(RoundingMode.HALF_DOWN), 2.225073858507201E-308);
        aeq(halfBelowBoundary.doubleValue(RoundingMode.HALF_UP), 2.225073858507201E-308);
        aeq(halfBelowBoundary.doubleValue(RoundingMode.HALF_EVEN), 2.225073858507201E-308);
        try {
            halfBelowBoundary.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justBelowBoundary.doubleValue(RoundingMode.FLOOR), 2.225073858507201E-308);
        aeq(justBelowBoundary.doubleValue(RoundingMode.CEILING), 2.2250738585072014E-308);
        aeq(justBelowBoundary.doubleValue(RoundingMode.DOWN), 2.225073858507201E-308);
        aeq(justBelowBoundary.doubleValue(RoundingMode.UP), 2.2250738585072014E-308);
        aeq(justBelowBoundary.doubleValue(RoundingMode.HALF_DOWN), 2.225073858507201E-308);
        aeq(justBelowBoundary.doubleValue(RoundingMode.HALF_UP), 2.225073858507201E-308);
        aeq(justBelowBoundary.doubleValue(RoundingMode.HALF_EVEN), 2.225073858507201E-308);
        try {
            justBelowBoundary.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(halfAboveBoundary.doubleValue(RoundingMode.FLOOR), 2.225073858507201E-308);
        aeq(halfAboveBoundary.doubleValue(RoundingMode.CEILING), 2.2250738585072014E-308);
        aeq(halfAboveBoundary.doubleValue(RoundingMode.DOWN), 2.225073858507201E-308);
        aeq(halfAboveBoundary.doubleValue(RoundingMode.UP), 2.2250738585072014E-308);
        aeq(halfAboveBoundary.doubleValue(RoundingMode.HALF_DOWN), 2.2250738585072014E-308);
        aeq(halfAboveBoundary.doubleValue(RoundingMode.HALF_UP), 2.2250738585072014E-308);
        aeq(halfAboveBoundary.doubleValue(RoundingMode.HALF_EVEN), 2.2250738585072014E-308);
        try {
            halfAboveBoundary.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(justAboveBoundary.doubleValue(RoundingMode.FLOOR), 2.225073858507201E-308);
        aeq(justAboveBoundary.doubleValue(RoundingMode.CEILING), 2.2250738585072014E-308);
        aeq(justAboveBoundary.doubleValue(RoundingMode.DOWN), 2.225073858507201E-308);
        aeq(justAboveBoundary.doubleValue(RoundingMode.UP), 2.2250738585072014E-308);
        aeq(justAboveBoundary.doubleValue(RoundingMode.HALF_DOWN), 2.2250738585072014E-308);
        aeq(justAboveBoundary.doubleValue(RoundingMode.HALF_UP), 2.2250738585072014E-308);
        aeq(justAboveBoundary.doubleValue(RoundingMode.HALF_EVEN), 2.2250738585072014E-308);
        try {
            justAboveBoundary.doubleValue(RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
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
        assertFalse(read("2/0").isPresent());
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

    private static @NotNull Optional<List<Rational>> readRationalList(@NotNull String s) {
        return Readers.readList(Rational::read, s);
    }

    private static @NotNull Optional<List<Rational>> readRationalListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Rational::read, s);
    }
}
