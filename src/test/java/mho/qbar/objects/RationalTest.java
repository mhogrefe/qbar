package mho.qbar.objects;

import mho.wheels.iterables.IterableUtils;
import mho.wheels.misc.FloatingPointUtils;
import mho.wheels.misc.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.qbar.objects.Rational.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("ConstantConditions")
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
        aeq(take(20, HARMONIC_NUMBERS),
                "[1, 3/2, 11/6, 25/12, 137/60, 49/20, 363/140, 761/280, 7129/2520, 7381/2520, 83711/27720," +
                " 86021/27720, 1145993/360360, 1171733/360360, 1195757/360360, 2436559/720720, 42142223/12252240," +
                " 14274301/4084080, 275295799/77597520, 55835135/15519504]");
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
    public void testIsInteger() {
        assertTrue(ZERO.isInteger());
        assertTrue(ONE.isInteger());
        assertTrue(read("5").get().isInteger());
        assertTrue(read("-5").get().isInteger());
        assertFalse(read("1/2").get().isInteger());
        assertFalse(read("-1/2").get().isInteger());
        assertFalse(ofExact(Math.PI).isInteger());
        assertFalse(ofExact(-Math.PI).isInteger());
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
    public void testHasTerminatingBaseExpansion() {
        assertTrue(ZERO.hasTerminatingBaseExpansion(BigInteger.valueOf(2)));
        assertTrue(ONE.hasTerminatingBaseExpansion(BigInteger.valueOf(2)));
        assertTrue(read("60").get().hasTerminatingBaseExpansion(BigInteger.valueOf(2)));
        assertTrue(read("1/2").get().hasTerminatingBaseExpansion(BigInteger.valueOf(2)));
        assertFalse(read("1/5").get().hasTerminatingBaseExpansion(BigInteger.valueOf(2)));
        assertFalse(read("-7/100").get().hasTerminatingBaseExpansion(BigInteger.valueOf(2)));
        assertFalse(read("-3/640").get().hasTerminatingBaseExpansion(BigInteger.valueOf(2)));
        assertFalse(read("1/3").get().hasTerminatingBaseExpansion(BigInteger.valueOf(2)));
        assertFalse(read("-1/15").get().hasTerminatingBaseExpansion(BigInteger.valueOf(2)));
        assertFalse(read("-2/9").get().hasTerminatingBaseExpansion(BigInteger.valueOf(2)));
        assertTrue(ZERO.hasTerminatingBaseExpansion(BigInteger.valueOf(3)));
        assertTrue(ONE.hasTerminatingBaseExpansion(BigInteger.valueOf(3)));
        assertTrue(read("60").get().hasTerminatingBaseExpansion(BigInteger.valueOf(3)));
        assertFalse(read("1/2").get().hasTerminatingBaseExpansion(BigInteger.valueOf(3)));
        assertFalse(read("1/5").get().hasTerminatingBaseExpansion(BigInteger.valueOf(3)));
        assertFalse(read("-7/100").get().hasTerminatingBaseExpansion(BigInteger.valueOf(3)));
        assertFalse(read("-3/640").get().hasTerminatingBaseExpansion(BigInteger.valueOf(3)));
        assertTrue(read("1/3").get().hasTerminatingBaseExpansion(BigInteger.valueOf(3)));
        assertFalse(read("-1/15").get().hasTerminatingBaseExpansion(BigInteger.valueOf(3)));
        assertTrue(read("-2/9").get().hasTerminatingBaseExpansion(BigInteger.valueOf(3)));
        assertTrue(ZERO.hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertTrue(ONE.hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertTrue(read("60").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertTrue(read("1/2").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertFalse(read("1/5").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertFalse(read("-7/100").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertFalse(read("-3/640").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertFalse(read("1/3").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertFalse(read("-1/15").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertFalse(read("-2/9").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertTrue(ZERO.hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertTrue(ONE.hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertTrue(read("60").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertTrue(read("1/2").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertFalse(read("1/5").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertFalse(read("-7/100").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertFalse(read("-3/640").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertFalse(read("1/3").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertFalse(read("-1/15").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertFalse(read("-2/9").get().hasTerminatingBaseExpansion(BigInteger.valueOf(4)));
        assertTrue(ZERO.hasTerminatingBaseExpansion(BigInteger.valueOf(16)));
        assertTrue(ONE.hasTerminatingBaseExpansion(BigInteger.valueOf(16)));
        assertTrue(read("60").get().hasTerminatingBaseExpansion(BigInteger.valueOf(16)));
        assertTrue(read("1/2").get().hasTerminatingBaseExpansion(BigInteger.valueOf(16)));
        assertFalse(read("1/5").get().hasTerminatingBaseExpansion(BigInteger.valueOf(16)));
        assertFalse(read("-7/100").get().hasTerminatingBaseExpansion(BigInteger.valueOf(16)));
        assertFalse(read("-3/640").get().hasTerminatingBaseExpansion(BigInteger.valueOf(16)));
        assertFalse(read("1/3").get().hasTerminatingBaseExpansion(BigInteger.valueOf(16)));
        assertFalse(read("-1/15").get().hasTerminatingBaseExpansion(BigInteger.valueOf(16)));
        assertFalse(read("-2/9").get().hasTerminatingBaseExpansion(BigInteger.valueOf(16)));
        assertTrue(ZERO.hasTerminatingBaseExpansion(BigInteger.valueOf(83)));
        assertTrue(ONE.hasTerminatingBaseExpansion(BigInteger.valueOf(83)));
        assertTrue(read("60").get().hasTerminatingBaseExpansion(BigInteger.valueOf(83)));
        assertFalse(read("1/2").get().hasTerminatingBaseExpansion(BigInteger.valueOf(83)));
        assertFalse(read("1/5").get().hasTerminatingBaseExpansion(BigInteger.valueOf(83)));
        assertFalse(read("-7/100").get().hasTerminatingBaseExpansion(BigInteger.valueOf(83)));
        assertFalse(read("-3/640").get().hasTerminatingBaseExpansion(BigInteger.valueOf(83)));
        assertFalse(read("1/3").get().hasTerminatingBaseExpansion(BigInteger.valueOf(83)));
        assertFalse(read("-1/15").get().hasTerminatingBaseExpansion(BigInteger.valueOf(83)));
        assertFalse(read("-2/9").get().hasTerminatingBaseExpansion(BigInteger.valueOf(83)));
        assertTrue(ZERO.hasTerminatingBaseExpansion(BigInteger.valueOf(100)));
        assertTrue(ONE.hasTerminatingBaseExpansion(BigInteger.valueOf(100)));
        assertTrue(read("60").get().hasTerminatingBaseExpansion(BigInteger.valueOf(100)));
        assertTrue(read("1/2").get().hasTerminatingBaseExpansion(BigInteger.valueOf(100)));
        assertTrue(read("1/5").get().hasTerminatingBaseExpansion(BigInteger.valueOf(100)));
        assertTrue(read("-7/100").get().hasTerminatingBaseExpansion(BigInteger.valueOf(100)));
        assertTrue(read("-3/640").get().hasTerminatingBaseExpansion(BigInteger.valueOf(100)));
        assertFalse(read("1/3").get().hasTerminatingBaseExpansion(BigInteger.valueOf(100)));
        assertFalse(read("-1/15").get().hasTerminatingBaseExpansion(BigInteger.valueOf(100)));
        assertFalse(read("-2/9").get().hasTerminatingBaseExpansion(BigInteger.valueOf(100)));
        try {
            read("1/2").get().hasTerminatingBaseExpansion(BigInteger.ONE);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("1/2").get().hasTerminatingBaseExpansion(BigInteger.ZERO);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("1/2").get().hasTerminatingBaseExpansion(BigInteger.valueOf(-1));
            fail();
        } catch (IllegalArgumentException ignored) {}
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
        aeq(ZERO.bigDecimalValue(4, RoundingMode.UNNECESSARY), "0.000");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.FLOOR), "0.000");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.CEILING), "0.000");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.DOWN), "0.000");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.UP), "0.000");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.HALF_DOWN), "0.000");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.HALF_UP), "0.000");
        aeq(ZERO.bigDecimalValue(4, RoundingMode.HALF_EVEN), "0.000");
        aeq(ONE.bigDecimalValue(0, RoundingMode.UNNECESSARY), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.FLOOR), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.CEILING), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.DOWN), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.UP), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.HALF_DOWN), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.HALF_UP), "1");
        aeq(ONE.bigDecimalValue(0, RoundingMode.HALF_EVEN), "1");
        aeq(ONE.bigDecimalValue(4, RoundingMode.UNNECESSARY), "1.000");
        aeq(ONE.bigDecimalValue(4, RoundingMode.FLOOR), "1.000");
        aeq(ONE.bigDecimalValue(4, RoundingMode.CEILING), "1.000");
        aeq(ONE.bigDecimalValue(4, RoundingMode.DOWN), "1.000");
        aeq(ONE.bigDecimalValue(4, RoundingMode.UP), "1.000");
        aeq(ONE.bigDecimalValue(4, RoundingMode.HALF_DOWN), "1.000");
        aeq(ONE.bigDecimalValue(4, RoundingMode.HALF_UP), "1.000");
        aeq(ONE.bigDecimalValue(4, RoundingMode.HALF_EVEN), "1.000");
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
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.UNNECESSARY), "0.5000");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.FLOOR), "0.5000");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.CEILING), "0.5000");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.DOWN), "0.5000");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.UP), "0.5000");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.HALF_DOWN), "0.5000");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.HALF_UP), "0.5000");
        aeq(read("1/2").get().bigDecimalValue(4, RoundingMode.HALF_EVEN), "0.5000");
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
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.UNNECESSARY), "0.0156250");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.FLOOR), "0.0156250");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.CEILING), "0.0156250");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.DOWN), "0.0156250");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.UP), "0.0156250");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.HALF_DOWN), "0.0156250");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.HALF_UP), "0.0156250");
        aeq(read("1/64").get().bigDecimalValue(6, RoundingMode.HALF_EVEN), "0.0156250");
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
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.UNNECESSARY), "6789.0");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.FLOOR), "6789.0");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.CEILING), "6789.0");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.DOWN), "6789.0");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.UP), "6789.0");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.HALF_DOWN), "6789.0");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.HALF_UP), "6789.0");
        aeq(read("6789").get().bigDecimalValue(5, RoundingMode.HALF_EVEN), "6789.0");
        aeq(read("19/20").get().bigDecimalValue(0, RoundingMode.FLOOR), "0.95");
        aeq(read("19/20").get().bigDecimalValue(0, RoundingMode.CEILING), "0.95");
        aeq(read("19/20").get().bigDecimalValue(0, RoundingMode.DOWN), "0.95");
        aeq(read("19/20").get().bigDecimalValue(0, RoundingMode.UP), "0.95");
        aeq(read("19/20").get().bigDecimalValue(0, RoundingMode.HALF_DOWN), "0.95");
        aeq(read("19/20").get().bigDecimalValue(0, RoundingMode.HALF_UP), "0.95");
        aeq(read("19/20").get().bigDecimalValue(0, RoundingMode.HALF_EVEN), "0.95");
        try {
            read("19/20").get().bigDecimalValue(1, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("19/20").get().bigDecimalValue(1, RoundingMode.FLOOR), "0.9");
        aeq(read("19/20").get().bigDecimalValue(1, RoundingMode.CEILING), "1");
        aeq(read("19/20").get().bigDecimalValue(1, RoundingMode.DOWN), "0.9");
        aeq(read("19/20").get().bigDecimalValue(1, RoundingMode.UP), "1");
        aeq(read("19/20").get().bigDecimalValue(1, RoundingMode.HALF_DOWN), "0.9");
        aeq(read("19/20").get().bigDecimalValue(1, RoundingMode.HALF_UP), "1");
        aeq(read("19/20").get().bigDecimalValue(1, RoundingMode.HALF_EVEN), "1");
        aeq(read("19/20").get().bigDecimalValue(2, RoundingMode.UNNECESSARY), "0.95");
        aeq(read("19/20").get().bigDecimalValue(2, RoundingMode.FLOOR), "0.95");
        aeq(read("19/20").get().bigDecimalValue(2, RoundingMode.CEILING), "0.95");
        aeq(read("19/20").get().bigDecimalValue(2, RoundingMode.DOWN), "0.95");
        aeq(read("19/20").get().bigDecimalValue(2, RoundingMode.UP), "0.95");
        aeq(read("19/20").get().bigDecimalValue(2, RoundingMode.HALF_DOWN), "0.95");
        aeq(read("19/20").get().bigDecimalValue(2, RoundingMode.HALF_UP), "0.95");
        aeq(read("19/20").get().bigDecimalValue(2, RoundingMode.HALF_EVEN), "0.95");
        aeq(read("19/20").get().bigDecimalValue(3, RoundingMode.UNNECESSARY), "0.950");
        aeq(read("19/20").get().bigDecimalValue(3, RoundingMode.FLOOR), "0.950");
        aeq(read("19/20").get().bigDecimalValue(3, RoundingMode.CEILING), "0.950");
        aeq(read("19/20").get().bigDecimalValue(3, RoundingMode.DOWN), "0.950");
        aeq(read("19/20").get().bigDecimalValue(3, RoundingMode.UP), "0.950");
        aeq(read("19/20").get().bigDecimalValue(3, RoundingMode.HALF_DOWN), "0.950");
        aeq(read("19/20").get().bigDecimalValue(3, RoundingMode.HALF_UP), "0.950");
        aeq(read("19/20").get().bigDecimalValue(3, RoundingMode.HALF_EVEN), "0.950");
        aeq(read("19/20").get().bigDecimalValue(4, RoundingMode.UNNECESSARY), "0.9500");
        aeq(read("19/20").get().bigDecimalValue(4, RoundingMode.FLOOR), "0.9500");
        aeq(read("19/20").get().bigDecimalValue(4, RoundingMode.CEILING), "0.9500");
        aeq(read("19/20").get().bigDecimalValue(4, RoundingMode.DOWN), "0.9500");
        aeq(read("19/20").get().bigDecimalValue(4, RoundingMode.UP), "0.9500");
        aeq(read("19/20").get().bigDecimalValue(4, RoundingMode.HALF_DOWN), "0.9500");
        aeq(read("19/20").get().bigDecimalValue(4, RoundingMode.HALF_UP), "0.9500");
        aeq(read("19/20").get().bigDecimalValue(4, RoundingMode.HALF_EVEN), "0.9500");
        aeq(read("19/20").get().bigDecimalValue(5, RoundingMode.UNNECESSARY), "0.95000");
        aeq(read("19/20").get().bigDecimalValue(5, RoundingMode.FLOOR), "0.95000");
        aeq(read("19/20").get().bigDecimalValue(5, RoundingMode.CEILING), "0.95000");
        aeq(read("19/20").get().bigDecimalValue(5, RoundingMode.DOWN), "0.95000");
        aeq(read("19/20").get().bigDecimalValue(5, RoundingMode.UP), "0.95000");
        aeq(read("19/20").get().bigDecimalValue(5, RoundingMode.HALF_DOWN), "0.95000");
        aeq(read("19/20").get().bigDecimalValue(5, RoundingMode.HALF_UP), "0.95000");
        aeq(read("19/20").get().bigDecimalValue(5, RoundingMode.HALF_EVEN), "0.95000");
        aeq(read("199/200").get().bigDecimalValue(0, RoundingMode.FLOOR), "0.995");
        aeq(read("199/200").get().bigDecimalValue(0, RoundingMode.CEILING), "0.995");
        aeq(read("199/200").get().bigDecimalValue(0, RoundingMode.DOWN), "0.995");
        aeq(read("199/200").get().bigDecimalValue(0, RoundingMode.UP), "0.995");
        aeq(read("199/200").get().bigDecimalValue(0, RoundingMode.HALF_DOWN), "0.995");
        aeq(read("199/200").get().bigDecimalValue(0, RoundingMode.HALF_UP), "0.995");
        aeq(read("199/200").get().bigDecimalValue(0, RoundingMode.HALF_EVEN), "0.995");
        try {
            read("199/200").get().bigDecimalValue(1, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("199/200").get().bigDecimalValue(1, RoundingMode.FLOOR), "0.9");
        aeq(read("199/200").get().bigDecimalValue(1, RoundingMode.CEILING), "1");
        aeq(read("199/200").get().bigDecimalValue(1, RoundingMode.DOWN), "0.9");
        aeq(read("199/200").get().bigDecimalValue(1, RoundingMode.UP), "1");
        aeq(read("199/200").get().bigDecimalValue(1, RoundingMode.HALF_DOWN), "1");
        aeq(read("199/200").get().bigDecimalValue(1, RoundingMode.HALF_UP), "1");
        aeq(read("199/200").get().bigDecimalValue(1, RoundingMode.HALF_EVEN), "1");
        try {
            read("199/200").get().bigDecimalValue(2, RoundingMode.UNNECESSARY);
            fail();
        } catch (ArithmeticException ignored) {}
        aeq(read("199/200").get().bigDecimalValue(2, RoundingMode.FLOOR), "0.99");
        aeq(read("199/200").get().bigDecimalValue(2, RoundingMode.CEILING), "1.0");
        aeq(read("199/200").get().bigDecimalValue(2, RoundingMode.DOWN), "0.99");
        aeq(read("199/200").get().bigDecimalValue(2, RoundingMode.UP), "1.0");
        aeq(read("199/200").get().bigDecimalValue(2, RoundingMode.HALF_DOWN), "0.99");
        aeq(read("199/200").get().bigDecimalValue(2, RoundingMode.HALF_UP), "1.0");
        aeq(read("199/200").get().bigDecimalValue(2, RoundingMode.HALF_EVEN), "1.0");
        aeq(read("199/200").get().bigDecimalValue(3, RoundingMode.UNNECESSARY), "0.995");
        aeq(read("199/200").get().bigDecimalValue(3, RoundingMode.FLOOR), "0.995");
        aeq(read("199/200").get().bigDecimalValue(3, RoundingMode.CEILING), "0.995");
        aeq(read("199/200").get().bigDecimalValue(3, RoundingMode.DOWN), "0.995");
        aeq(read("199/200").get().bigDecimalValue(3, RoundingMode.UP), "0.995");
        aeq(read("199/200").get().bigDecimalValue(3, RoundingMode.HALF_DOWN), "0.995");
        aeq(read("199/200").get().bigDecimalValue(3, RoundingMode.HALF_UP), "0.995");
        aeq(read("199/200").get().bigDecimalValue(3, RoundingMode.HALF_EVEN), "0.995");
        aeq(read("199/200").get().bigDecimalValue(4, RoundingMode.UNNECESSARY), "0.9950");
        aeq(read("199/200").get().bigDecimalValue(4, RoundingMode.FLOOR), "0.9950");
        aeq(read("199/200").get().bigDecimalValue(4, RoundingMode.CEILING), "0.9950");
        aeq(read("199/200").get().bigDecimalValue(4, RoundingMode.DOWN), "0.9950");
        aeq(read("199/200").get().bigDecimalValue(4, RoundingMode.UP), "0.9950");
        aeq(read("199/200").get().bigDecimalValue(4, RoundingMode.HALF_DOWN), "0.9950");
        aeq(read("199/200").get().bigDecimalValue(4, RoundingMode.HALF_UP), "0.9950");
        aeq(read("199/200").get().bigDecimalValue(4, RoundingMode.HALF_EVEN), "0.9950");
        aeq(read("199/200").get().bigDecimalValue(5, RoundingMode.UNNECESSARY), "0.99500");
        aeq(read("199/200").get().bigDecimalValue(5, RoundingMode.FLOOR), "0.99500");
        aeq(read("199/200").get().bigDecimalValue(5, RoundingMode.CEILING), "0.99500");
        aeq(read("199/200").get().bigDecimalValue(5, RoundingMode.DOWN), "0.99500");
        aeq(read("199/200").get().bigDecimalValue(5, RoundingMode.UP), "0.99500");
        aeq(read("199/200").get().bigDecimalValue(5, RoundingMode.HALF_DOWN), "0.99500");
        aeq(read("199/200").get().bigDecimalValue(5, RoundingMode.HALF_UP), "0.99500");
        aeq(read("199/200").get().bigDecimalValue(5, RoundingMode.HALF_EVEN), "0.99500");
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
        aeq(ZERO.bigDecimalValue(4), "0.000");
        aeq(ZERO.bigDecimalValue(0), "0");
        aeq(ONE.bigDecimalValue(4), "1.000");
        aeq(ONE.bigDecimalValue(0), "1");
        aeq(read("1/2").get().bigDecimalValue(0), "0.5");
        aeq(read("1/2").get().bigDecimalValue(1), "0.5");
        aeq(read("1/2").get().bigDecimalValue(4), "0.5000");
        aeq(read("1/64").get().bigDecimalValue(0), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(1), "0.02");
        aeq(read("1/64").get().bigDecimalValue(2), "0.016");
        aeq(read("1/64").get().bigDecimalValue(3), "0.0156");
        aeq(read("1/64").get().bigDecimalValue(4), "0.01562");
        aeq(read("1/64").get().bigDecimalValue(5), "0.015625");
        aeq(read("1/64").get().bigDecimalValue(6), "0.0156250");
        aeq(read("-1/3").get().bigDecimalValue(1), "-0.3");
        aeq(read("-1/3").get().bigDecimalValue(2), "-0.33");
        aeq(read("-1/3").get().bigDecimalValue(3), "-0.333");
        aeq(read("-1/3").get().bigDecimalValue(4), "-0.3333");
        aeq(read("6789").get().bigDecimalValue(0), "6789");
        aeq(read("6789").get().bigDecimalValue(1), "7E+3");
        aeq(read("6789").get().bigDecimalValue(2), "6.8E+3");
        aeq(read("6789").get().bigDecimalValue(3), "6.79E+3");
        aeq(read("6789").get().bigDecimalValue(4), "6789");
        aeq(read("6789").get().bigDecimalValue(5), "6789.0");
        aeq(read("19/20").get().bigDecimalValue(0), "0.95");
        aeq(read("19/20").get().bigDecimalValue(1), "1");
        aeq(read("19/20").get().bigDecimalValue(2), "0.95");
        aeq(read("19/20").get().bigDecimalValue(3), "0.950");
        aeq(read("19/20").get().bigDecimalValue(4), "0.9500");
        aeq(read("19/20").get().bigDecimalValue(5), "0.95000");
        aeq(read("199/200").get().bigDecimalValue(0), "0.995");
        aeq(read("199/200").get().bigDecimalValue(1), "1");
        aeq(read("199/200").get().bigDecimalValue(2), "1.0");
        aeq(read("199/200").get().bigDecimalValue(3), "0.995");
        aeq(read("199/200").get().bigDecimalValue(4), "0.9950");
        aeq(read("199/200").get().bigDecimalValue(5), "0.99500");
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
        aeq(read("19/20").get().bigDecimalValueExact(), "0.95");
        aeq(read("199/200").get().bigDecimalValueExact(), "0.995");
        try {
            read("1/3").get().bigDecimalValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
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
        try {
            ZERO.binaryExponent();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("-2/3").get().binaryExponent();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFloatValue_RoundingMode() {
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
        Rational piSuccessor = ofExact(FloatingPointUtils.successor((float) Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = ofExact(FloatingPointUtils.predecessor((float) Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = floatPi.add(piSuccessor).divide(2);
        Rational halfBelowPi = floatPi.add(piPredecessor).divide(2);
        Rational justAbovePi = floatPi.multiply(2).add(piSuccessor).divide(3);
        Rational justBelowPi = floatPi.multiply(2).add(piPredecessor).divide(3);
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
        Rational subnormalSuccessor = ofExact(FloatingPointUtils.successor(1e-40f));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = ofExact(FloatingPointUtils.predecessor(1e-40f));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = subnormal.add(subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = subnormal.add(subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = subnormal.multiply(2).add(subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = subnormal.multiply(2).add(subnormalPredecessor).divide(3);
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
        Rational belowNegativeMax = LARGEST_FLOAT.negate().subtract(ONE);
        Rational negativeMaxSuccessor = ofExact(FloatingPointUtils.successor(-Float.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = LARGEST_FLOAT.negate().add(negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = LARGEST_FLOAT.negate().multiply(2).add(negativeMaxSuccessor).divide(3);
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
        Rational aboveMax = LARGEST_FLOAT.add(ONE);
        Rational maxPredecessor = ofExact(FloatingPointUtils.predecessor(Float.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = LARGEST_FLOAT.add(maxPredecessor).divide(2);
        Rational justBelowMax = LARGEST_FLOAT.multiply(2).add(maxPredecessor).divide(3);
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
        Rational boundary = LARGEST_SUBNORMAL_FLOAT.add(SMALLEST_NORMAL_FLOAT).shiftRight(1);
        Rational halfBelowBoundary = LARGEST_SUBNORMAL_FLOAT.add(boundary).shiftRight(1);
        Rational halfAboveBoundary = SMALLEST_NORMAL_FLOAT.add(boundary).shiftRight(1);
        Rational justBelowBoundary = LARGEST_SUBNORMAL_FLOAT.add(boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = SMALLEST_NORMAL_FLOAT.add(boundary.shiftLeft(1)).divide(3);
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
    public void testFloatValue() {
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
        Rational piSuccessor = ofExact(FloatingPointUtils.successor((float) Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = ofExact(FloatingPointUtils.predecessor((float) Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = floatPi.add(piSuccessor).divide(2);
        Rational halfBelowPi = floatPi.add(piPredecessor).divide(2);
        Rational justAbovePi = floatPi.multiply(2).add(piSuccessor).divide(3);
        Rational justBelowPi = floatPi.multiply(2).add(piPredecessor).divide(3);
        aeq(halfAbovePi.floatValue(), 3.141593);
        aeq(halfBelowPi.floatValue(), 3.1415925);
        aeq(justAbovePi.floatValue(), 3.1415927);
        aeq(justBelowPi.floatValue(), 3.1415927);
        Rational subnormal = ofExact(1e-40f);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = ofExact(FloatingPointUtils.successor(1e-40f));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = ofExact(FloatingPointUtils.predecessor(1e-40f));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = subnormal.add(subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = subnormal.add(subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = subnormal.multiply(2).add(subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = subnormal.multiply(2).add(subnormalPredecessor).divide(3);
        aeq(subnormal.floatValue(), 1.0E-40);
        aeq(halfAboveSubnormal.floatValue(), 1.0E-40);
        aeq(halfBelowSubnormal.floatValue(), 1.0E-40);
        aeq(justAboveSubnormal.floatValue(), 1.0E-40);
        aeq(justBelowSubnormal.floatValue(), 1.0E-40);
        Rational belowNegativeMax = LARGEST_FLOAT.negate().subtract(ONE);
        Rational negativeMaxSuccessor = ofExact(FloatingPointUtils.successor(-Float.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = LARGEST_FLOAT.negate().add(negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = LARGEST_FLOAT.negate().multiply(2).add(negativeMaxSuccessor).divide(3);
        aeq(belowNegativeMax.floatValue(), Float.NEGATIVE_INFINITY);
        aeq(halfAboveNegativeMax.floatValue(), -3.4028233E38);
        aeq(justAboveNegativeMax.floatValue(), -3.4028235E38);
        Rational aboveMax = LARGEST_FLOAT.add(ONE);
        Rational maxPredecessor = ofExact(FloatingPointUtils.predecessor(Float.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = LARGEST_FLOAT.add(maxPredecessor).divide(2);
        Rational justBelowMax = LARGEST_FLOAT.multiply(2).add(maxPredecessor).divide(3);
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
        Rational boundary = LARGEST_SUBNORMAL_FLOAT.add(SMALLEST_NORMAL_FLOAT).shiftRight(1);
        Rational halfBelowBoundary = LARGEST_SUBNORMAL_FLOAT.add(boundary).shiftRight(1);
        Rational halfAboveBoundary = SMALLEST_NORMAL_FLOAT.add(boundary).shiftRight(1);
        Rational justBelowBoundary = LARGEST_SUBNORMAL_FLOAT.add(boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = SMALLEST_NORMAL_FLOAT.add(boundary.shiftLeft(1)).divide(3);
        aeq(boundary.floatValue(), 1.17549435E-38);
        aeq(halfBelowBoundary.floatValue(), 1.1754942E-38);
        aeq(justBelowBoundary.floatValue(), 1.1754942E-38);
        aeq(halfAboveBoundary.floatValue(), 1.17549435E-38);
        aeq(justAboveBoundary.floatValue(), 1.17549435E-38);
    }

    @Test
    public void testFloatValueExact() {
        aeq(ZERO.floatValueExact(), 0.0);
        aeq(ONE.floatValueExact(), 1.0);
        aeq(read("1/2").get().floatValueExact(), 0.5);
        try {
            read("1/3").get().floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("-1/3").get().floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational almostOne = of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
        try {
            almostOne.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational floatPi = ofExact((float) Math.PI);
        if (floatPi == null) {
            fail();
        }
        aeq(floatPi.floatValueExact(), 3.1415927);
        Rational trillion = of(BigInteger.TEN.pow(12));
        try {
            trillion.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational piSuccessor = ofExact(FloatingPointUtils.successor((float) Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = ofExact(FloatingPointUtils.predecessor((float) Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = floatPi.add(piSuccessor).divide(2);
        Rational halfBelowPi = floatPi.add(piPredecessor).divide(2);
        Rational justAbovePi = floatPi.multiply(2).add(piSuccessor).divide(3);
        Rational justBelowPi = floatPi.multiply(2).add(piPredecessor).divide(3);
        try {
            halfAbovePi.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfBelowPi.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justAbovePi.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justBelowPi.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational subnormal = ofExact(1e-40f);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = ofExact(FloatingPointUtils.successor(1e-40f));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = ofExact(FloatingPointUtils.predecessor(1e-40f));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = subnormal.add(subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = subnormal.add(subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = subnormal.multiply(2).add(subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = subnormal.multiply(2).add(subnormalPredecessor).divide(3);
        try {
            halfAboveSubnormal.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfBelowSubnormal.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justAboveSubnormal.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justBelowSubnormal.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational belowNegativeMax = LARGEST_FLOAT.negate().subtract(ONE);
        Rational negativeMaxSuccessor = ofExact(FloatingPointUtils.successor(-Float.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = LARGEST_FLOAT.negate().add(negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = LARGEST_FLOAT.negate().multiply(2).add(negativeMaxSuccessor).divide(3);
        try {
            belowNegativeMax.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfAboveNegativeMax.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justAboveNegativeMax.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational aboveMax = LARGEST_FLOAT.add(ONE);
        Rational maxPredecessor = ofExact(FloatingPointUtils.predecessor(Float.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = LARGEST_FLOAT.add(maxPredecessor).divide(2);
        Rational justBelowMax = LARGEST_FLOAT.multiply(2).add(maxPredecessor).divide(3);
        try {
            aboveMax.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfBelowMax.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justBelowMax.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational halfAboveZero = SMALLEST_FLOAT.shiftRight(1);
        Rational justAboveZero = SMALLEST_FLOAT.divide(3);
        Rational halfBelowZero = halfAboveZero.negate();
        Rational justBelowZero = justAboveZero.negate();
        try {
            halfBelowZero.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justBelowZero.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfAboveZero.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justAboveZero.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational boundary = LARGEST_SUBNORMAL_FLOAT.add(SMALLEST_NORMAL_FLOAT).shiftRight(1);
        Rational halfBelowBoundary = LARGEST_SUBNORMAL_FLOAT.add(boundary).shiftRight(1);
        Rational halfAboveBoundary = SMALLEST_NORMAL_FLOAT.add(boundary).shiftRight(1);
        Rational justBelowBoundary = LARGEST_SUBNORMAL_FLOAT.add(boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = SMALLEST_NORMAL_FLOAT.add(boundary.shiftLeft(1)).divide(3);
        try {
            boundary.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfBelowBoundary.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justBelowBoundary.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfAboveBoundary.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justAboveBoundary.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDoubleValue_RoundingMode() {
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
        Rational piSuccessor = ofExact(FloatingPointUtils.successor(Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = ofExact(FloatingPointUtils.predecessor(Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = pi.add(piSuccessor).divide(2);
        Rational halfBelowPi = pi.add(piPredecessor).divide(2);
        Rational justAbovePi = pi.multiply(2).add(piSuccessor).divide(3);
        Rational justBelowPi = pi.multiply(2).add(piPredecessor).divide(3);
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
        Rational subnormalSuccessor = ofExact(FloatingPointUtils.successor(1e-310));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = ofExact(FloatingPointUtils.predecessor(1e-310));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = subnormal.add(subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = subnormal.add(subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = subnormal.multiply(2).add(subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = subnormal.multiply(2).add(subnormalPredecessor).divide(3);
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
        Rational belowNegativeMax = LARGEST_DOUBLE.negate().subtract(ONE);
        Rational negativeMaxSuccessor = ofExact(FloatingPointUtils.successor(-Double.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = LARGEST_DOUBLE.negate().add(negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = LARGEST_DOUBLE.negate().multiply(2).add(negativeMaxSuccessor).divide(3);
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
        Rational aboveMax = LARGEST_DOUBLE.add(ONE);
        Rational maxPredecessor = ofExact(FloatingPointUtils.predecessor(Double.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = LARGEST_DOUBLE.add(maxPredecessor).divide(2);
        Rational justBelowMax = LARGEST_DOUBLE.multiply(2).add(maxPredecessor).divide(3);
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
        Rational boundary = LARGEST_SUBNORMAL_DOUBLE.add(SMALLEST_NORMAL_DOUBLE).shiftRight(1);
        Rational halfBelowBoundary = LARGEST_SUBNORMAL_DOUBLE.add(boundary).shiftRight(1);
        Rational halfAboveBoundary = SMALLEST_NORMAL_DOUBLE.add(boundary).shiftRight(1);
        Rational justBelowBoundary = LARGEST_SUBNORMAL_DOUBLE.add(boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = SMALLEST_NORMAL_DOUBLE.add(boundary.shiftLeft(1)).divide(3);
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
    public void testDoubleValue() {
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
        Rational piSuccessor = ofExact(FloatingPointUtils.successor(Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = ofExact(FloatingPointUtils.predecessor(Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = pi.add(piSuccessor).divide(2);
        Rational halfBelowPi = pi.add(piPredecessor).divide(2);
        Rational justAbovePi = pi.multiply(2).add(piSuccessor).divide(3);
        Rational justBelowPi = pi.multiply(2).add(piPredecessor).divide(3);
        aeq(halfAbovePi.doubleValue(), 3.141592653589793);
        aeq(halfBelowPi.doubleValue(), 3.141592653589793);
        aeq(justAbovePi.doubleValue(), 3.141592653589793);
        aeq(justBelowPi.doubleValue(), 3.141592653589793);
        Rational subnormal = ofExact(1e-310);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = ofExact(FloatingPointUtils.successor(1e-310));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = ofExact(FloatingPointUtils.predecessor(1e-310));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = subnormal.add(subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = subnormal.add(subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = subnormal.multiply(2).add(subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = subnormal.multiply(2).add(subnormalPredecessor).divide(3);
        aeq(subnormal.doubleValue(), 1.0E-310);
        aeq(halfAboveSubnormal.doubleValue(), 1.00000000000005E-310);
        aeq(halfBelowSubnormal.doubleValue(), 9.9999999999995E-311);
        aeq(justAboveSubnormal.doubleValue(), 1.0E-310);
        aeq(justBelowSubnormal.doubleValue(), 1.0E-310);
        Rational belowNegativeMax = LARGEST_DOUBLE.negate().subtract(ONE);
        Rational negativeMaxSuccessor = ofExact(FloatingPointUtils.successor(-Double.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = LARGEST_DOUBLE.negate().add(negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = LARGEST_DOUBLE.negate().multiply(2).add(negativeMaxSuccessor).divide(3);
        aeq(belowNegativeMax.doubleValue(), Double.NEGATIVE_INFINITY);
        aeq(halfAboveNegativeMax.doubleValue(), -1.7976931348623155E308);
        aeq(justAboveNegativeMax.doubleValue(), -1.7976931348623157E308);
        Rational aboveMax = LARGEST_DOUBLE.add(ONE);
        Rational maxPredecessor = ofExact(FloatingPointUtils.predecessor(Double.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = LARGEST_DOUBLE.add(maxPredecessor).divide(2);
        Rational justBelowMax = LARGEST_DOUBLE.multiply(2).add(maxPredecessor).divide(3);
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
        Rational boundary = LARGEST_SUBNORMAL_DOUBLE.add(SMALLEST_NORMAL_DOUBLE).shiftRight(1);
        Rational halfBelowBoundary = LARGEST_SUBNORMAL_DOUBLE.add(boundary).shiftRight(1);
        Rational halfAboveBoundary = SMALLEST_NORMAL_DOUBLE.add(boundary).shiftRight(1);
        Rational justBelowBoundary = LARGEST_SUBNORMAL_DOUBLE.add(boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = SMALLEST_NORMAL_DOUBLE.add(boundary.shiftLeft(1)).divide(3);
        aeq(boundary.doubleValue(), 2.2250738585072014E-308);
        aeq(halfBelowBoundary.doubleValue(), 2.225073858507201E-308);
        aeq(justBelowBoundary.doubleValue(), 2.225073858507201E-308);
        aeq(halfAboveBoundary.doubleValue(), 2.2250738585072014E-308);
        aeq(justAboveBoundary.doubleValue(), 2.2250738585072014E-308);
    }

    @Test
    public void testDoubleValueExact() {
        aeq(ZERO.doubleValueExact(), 0.0);
        aeq(ONE.doubleValueExact(), 1.0);
        aeq(read("1/2").get().doubleValueExact(), 0.5);
        try {
            read("1/3").get().doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("-1/3").get().doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational almostOne = of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));
        try {
            almostOne.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational pi = ofExact(Math.PI);
        if (pi == null) {
            fail();
        }
        aeq(pi.doubleValueExact(), 3.141592653589793);
        Rational googol = of(BigInteger.TEN.pow(100));
        try {
            googol.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational piSuccessor = ofExact(FloatingPointUtils.successor(Math.PI));
        if (piSuccessor == null) {
            fail();
        }
        Rational piPredecessor = ofExact(FloatingPointUtils.predecessor(Math.PI));
        if (piPredecessor == null) {
            fail();
        }
        Rational halfAbovePi = pi.add(piSuccessor).divide(2);
        Rational halfBelowPi = pi.add(piPredecessor).divide(2);
        Rational justAbovePi = pi.multiply(2).add(piSuccessor).divide(3);
        Rational justBelowPi = pi.multiply(2).add(piPredecessor).divide(3);
        try {
            halfAbovePi.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfBelowPi.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justAbovePi.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justBelowPi.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational subnormal = ofExact(1e-310);
        if (subnormal == null) {
            fail();
        }
        Rational subnormalSuccessor = ofExact(FloatingPointUtils.successor(1e-310));
        if (subnormalSuccessor == null) {
            fail();
        }
        Rational subnormalPredecessor = ofExact(FloatingPointUtils.predecessor(1e-310));
        if (subnormalPredecessor == null) {
            fail();
        }
        Rational halfAboveSubnormal = subnormal.add(subnormalSuccessor).divide(2);
        Rational halfBelowSubnormal = subnormal.add(subnormalPredecessor).divide(2);
        Rational justAboveSubnormal = subnormal.multiply(2).add(subnormalSuccessor).divide(3);
        Rational justBelowSubnormal = subnormal.multiply(2).add(subnormalPredecessor).divide(3);
        aeq(subnormal.doubleValueExact(), 1.0E-310);
        try {
            halfAboveSubnormal.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfBelowSubnormal.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justAboveSubnormal.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justBelowSubnormal.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational belowNegativeMax = LARGEST_DOUBLE.negate().subtract(ONE);
        Rational negativeMaxSuccessor = ofExact(FloatingPointUtils.successor(-Double.MAX_VALUE));
        if (negativeMaxSuccessor == null) {
            fail();
        }
        Rational halfAboveNegativeMax = LARGEST_DOUBLE.negate().add(negativeMaxSuccessor).divide(2);
        Rational justAboveNegativeMax = LARGEST_DOUBLE.negate().multiply(2).add(negativeMaxSuccessor).divide(3);
        try {
            belowNegativeMax.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfAboveNegativeMax.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justAboveNegativeMax.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational aboveMax = LARGEST_DOUBLE.add(ONE);
        Rational maxPredecessor = ofExact(FloatingPointUtils.predecessor(Double.MAX_VALUE));
        if (maxPredecessor == null) {
            fail();
        }
        Rational halfBelowMax = LARGEST_DOUBLE.add(maxPredecessor).divide(2);
        Rational justBelowMax = LARGEST_DOUBLE.multiply(2).add(maxPredecessor).divide(3);
        try {
            aboveMax.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfBelowMax.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justBelowMax.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational halfAboveZero = SMALLEST_DOUBLE.shiftRight(1);
        Rational justAboveZero = SMALLEST_DOUBLE.divide(3);
        Rational halfBelowZero = halfAboveZero.negate();
        Rational justBelowZero = justAboveZero.negate();
        try {
            halfBelowZero.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justBelowZero.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfAboveZero.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justAboveZero.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        Rational boundary = LARGEST_SUBNORMAL_DOUBLE.add(SMALLEST_NORMAL_DOUBLE).shiftRight(1);
        Rational halfBelowBoundary = LARGEST_SUBNORMAL_DOUBLE.add(boundary).shiftRight(1);
        Rational halfAboveBoundary = SMALLEST_NORMAL_DOUBLE.add(boundary).shiftRight(1);
        Rational justBelowBoundary = LARGEST_SUBNORMAL_DOUBLE.add(boundary.shiftLeft(1)).divide(3);
        Rational justAboveBoundary = SMALLEST_NORMAL_DOUBLE.add(boundary.shiftLeft(1)).divide(3);
        try {
            boundary.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfBelowBoundary.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justBelowBoundary.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            halfAboveBoundary.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            justAboveBoundary.doubleValueExact();
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
        aeq(read("1/2").get().add(read("1/3").get()), "5/6");
        aeq(read("1/2").get().add(read("-1/3").get()), "1/6");
        aeq(read("-1/2").get().add(read("1/3").get()), "-1/6");
        aeq(read("-1/2").get().add(read("-1/3").get()), "-5/6");
        aeq(read("2").get().add(read("1/5").get()), "11/5");
        aeq(read("2").get().add(read("-1/5").get()), "9/5");
        aeq(read("-2").get().add(read("1/5").get()), "-9/5");
        aeq(read("-2").get().add(read("-1/5").get()), "-11/5");
        aeq(read("2").get().add(read("5").get()), "7");
        aeq(read("2").get().add(read("-5").get()), "-3");
        aeq(read("-2").get().add(read("5").get()), "3");
        aeq(read("-2").get().add(read("-5").get()), "-7");
        assertTrue(read("6/7").get().add(read("1/7").get()) == ONE);
        assertTrue(read("6/7").get().add(read("-6/7").get()) == ZERO);
        aeq(read("1/2").get().add(ZERO), "1/2");
        aeq(read("-1/2").get().add(ZERO), "-1/2");
        aeq(read("1/2").get().add(ONE), "3/2");
        aeq(read("-1/2").get().add(ONE), "1/2");
        assertTrue(ZERO.add(ZERO) == ZERO);
        assertTrue(ZERO.add(ONE) == ONE);
        assertTrue(ONE.add(ZERO) == ONE);
        aeq(ONE.add(ONE), "2");
    }

    @Test
    public void testSubtract() {
        aeq(read("1/2").get().subtract(read("1/3").get()), "1/6");
        aeq(read("1/2").get().subtract(read("-1/3").get()), "5/6");
        aeq(read("-1/2").get().subtract(read("1/3").get()), "-5/6");
        aeq(read("-1/2").get().subtract(read("-1/3").get()), "-1/6");
        aeq(read("2").get().subtract(read("1/5").get()), "9/5");
        aeq(read("2").get().subtract(read("-1/5").get()), "11/5");
        aeq(read("-2").get().subtract(read("1/5").get()), "-11/5");
        aeq(read("-2").get().subtract(read("-1/5").get()), "-9/5");
        aeq(read("2").get().subtract(read("5").get()), "-3");
        aeq(read("2").get().subtract(read("-5").get()), "7");
        aeq(read("-2").get().subtract(read("5").get()), "-7");
        aeq(read("-2").get().subtract(read("-5").get()), "3");
        assertTrue(read("8/7").get().subtract(read("1/7").get()) == ONE);
        assertTrue(read("6/7").get().subtract(read("6/7").get()) == ZERO);
        aeq(read("1/2").get().subtract(ZERO), "1/2");
        aeq(read("-1/2").get().subtract(ZERO), "-1/2");
        aeq(read("1/2").get().subtract(ONE), "-1/2");
        aeq(read("-1/2").get().subtract(ONE), "-3/2");
        assertTrue(ZERO.subtract(ZERO) == ZERO);
        aeq(ZERO.subtract(ONE), "-1");
        assertTrue(ONE.subtract(ZERO) == ONE);
        assertTrue(ONE.subtract(ONE) == ZERO);
    }

    @Test
    public void testMultiply_Rational() {
        aeq(read("2/3").get().multiply(read("6/7").get()), "4/7");
        aeq(read("2/3").get().multiply(read("-6/7").get()), "-4/7");
        aeq(read("-2/3").get().multiply(read("6/7").get()), "-4/7");
        aeq(read("-2/3").get().multiply(read("-6/7").get()), "4/7");
        aeq(read("2/3").get().multiply(read("4").get()), "8/3");
        aeq(read("2/3").get().multiply(read("-4").get()), "-8/3");
        aeq(read("-2/3").get().multiply(read("4").get()), "-8/3");
        aeq(read("-2/3").get().multiply(read("-4").get()), "8/3");
        aeq(read("3").get().multiply(read("5").get()), "15");
        aeq(read("3").get().multiply(read("-5").get()), "-15");
        aeq(read("-3").get().multiply(read("5").get()), "-15");
        aeq(read("-3").get().multiply(read("-5").get()), "15");
        assertTrue(read("1/4").get().multiply(read("4").get()) == ONE);
        assertTrue(read("-1/4").get().multiply(read("-4").get()) == ONE);
        assertTrue(read("2/3").get().multiply(ZERO) == ZERO);
        assertTrue(read("-2/3").get().multiply(ZERO) == ZERO);
        aeq(read("2/3").get().multiply(ONE), "2/3");
        aeq(read("-2/3").get().multiply(ONE), "-2/3");
        assertTrue(ZERO.multiply(ZERO) == ZERO);
        assertTrue(ZERO.multiply(ONE) == ZERO);
        assertTrue(ONE.multiply(ZERO) == ZERO);
        assertTrue(ONE.multiply(ONE) == ONE);
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
    public void testDivide_Rational() {
        aeq(read("2/3").get().divide(read("6/7").get()), "7/9");
        aeq(read("2/3").get().divide(read("-6/7").get()), "-7/9");
        aeq(read("-2/3").get().divide(read("6/7").get()), "-7/9");
        aeq(read("-2/3").get().divide(read("-6/7").get()), "7/9");
        aeq(read("2/3").get().divide(read("4").get()), "1/6");
        aeq(read("2/3").get().divide(read("-4").get()), "-1/6");
        aeq(read("-2/3").get().divide(read("4").get()), "-1/6");
        aeq(read("-2/3").get().divide(read("-4").get()), "1/6");
        aeq(read("3").get().divide(read("5").get()), "3/5");
        aeq(read("3").get().divide(read("-5").get()), "-3/5");
        aeq(read("-3").get().divide(read("5").get()), "-3/5");
        aeq(read("-3").get().divide(read("-5").get()), "3/5");
        aeq(read("1/4").get().divide(read("4").get()), "1/16");
        assertTrue(read("2/3").get().divide(read("2/3").get()) == ONE);
        assertTrue(read("-2/3").get().divide(read("-2/3").get()) == ONE);
        aeq(read("2/3").get().divide(ONE), "2/3");
        aeq(read("-2/3").get().divide(ONE), "-2/3");
        assertTrue(ZERO.divide(ONE) == ZERO);
        assertTrue(ONE.divide(ONE) == ONE);
        try {
            read("2/3").get().divide(ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("3").get().divide(ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            ONE.divide(ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            ZERO.divide(ZERO);
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
        } catch (NullPointerException ignored) {}
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
    public void testContinuedFraction() {
        aeq(ZERO.continuedFraction(), "[0]");
        aeq(ONE.continuedFraction(), "[1]");
        aeq(read("5").get().continuedFraction(), "[5]");
        aeq(read("-5").get().continuedFraction(), "[-5]");
        aeq(read("1/2").get().continuedFraction(), "[0, 2]");
        aeq(read("-1/2").get().continuedFraction(), "[-1, 2]");
        aeq(read("415/93").get().continuedFraction(), "[4, 2, 6, 7]");
        aeq(read("-415/93").get().continuedFraction(), "[-5, 1, 1, 6, 7]");
        aeq(ofExact(Math.sqrt(2)).continuedFraction(),
                "[1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 2, 7, 1, 2, 33, 2, 7, 5," +
                " 2, 1, 1, 16, 2]");
        aeq(ofExact(-Math.sqrt(2)).continuedFraction(),
                "[-2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 2, 7, 1, 2, 33, 2, 7," +
                " 5, 2, 1, 1, 16, 2]");
        aeq(ofExact(Math.PI).continuedFraction(),
                "[3, 7, 15, 1, 292, 1, 1, 1, 2, 1, 3, 1, 14, 3, 3, 2, 1, 3, 3, 7, 2, 1, 1, 3, 2, 42, 2]");
        aeq(ofExact(-Math.PI).continuedFraction(),
                "[-4, 1, 6, 15, 1, 292, 1, 1, 1, 2, 1, 3, 1, 14, 3, 3, 2, 1, 3, 3, 7, 2, 1, 1, 3, 2, 42, 2]");
        aeq(ofExact(Math.E).continuedFraction(),
                "[2, 1, 2, 1, 1, 4, 1, 1, 6, 1, 1, 8, 1, 1, 10, 1, 1, 12, 1, 1, 11, 1, 1, 1, 11, 5, 1, 1, 2, 1, 4," +
                " 2, 1, 1, 9, 17, 3]");
        aeq(ofExact(-Math.E).continuedFraction(),
                "[-3, 3, 1, 1, 4, 1, 1, 6, 1, 1, 8, 1, 1, 10, 1, 1, 12, 1, 1, 11, 1, 1, 1, 11, 5, 1, 1, 2, 1, 4, 2," +
                " 1, 1, 9, 17, 3]");
    }

    @Test
    public void testFromContinuedFraction() {
        aeq(fromContinuedFraction(readBigIntegerList("[1]").get()), "1");
        aeq(fromContinuedFraction(readBigIntegerList("[0, 2]").get()), "1/2");
        aeq(fromContinuedFraction(readBigIntegerList("[-1, 2]").get()), "-1/2");
        aeq(fromContinuedFraction(readBigIntegerList("[4, 2, 6, 7]").get()), "415/93");
        aeq(fromContinuedFraction(readBigIntegerList("[-5, 1, 1, 6, 7]").get()), "-415/93");
        aeq(fromContinuedFraction(readBigIntegerList("[0, 1, 2, 3, 4, 5, 6, 7, 8]").get()).floatValue(), "0.69777465");
        try {
            fromContinuedFraction(readBigIntegerList("[]").get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testConvergents() {
        aeq(ZERO.convergents(), "[0]");
        aeq(ONE.convergents(), "[1]");
        aeq(read("5").get().convergents(), "[5]");
        aeq(read("-5").get().convergents(), "[-5]");
        aeq(read("1/2").get().convergents(), "[0, 1/2]");
        aeq(read("-1/2").get().convergents(), "[-1, -1/2]");
        aeq(read("415/93").get().convergents(), "[4, 9/2, 58/13, 415/93]");
        aeq(read("-415/93").get().convergents(), "[-5, -4, -9/2, -58/13, -415/93]");
        aeq(ofExact(Math.sqrt(2)).convergents(),
                "[1, 3/2, 7/5, 17/12, 41/29, 99/70, 239/169, 577/408, 1393/985, 3363/2378, 8119/5741, 19601/13860," +
                " 47321/33461, 114243/80782, 275807/195025, 665857/470832, 1607521/1136689, 3880899/2744210," +
                " 9369319/6625109, 22619537/15994428, 54608393/38613965, 77227930/54608393, 131836323/93222358," +
                " 209064253/147830751, 549964829/388883860, 4058818056/2870017771, 4608782885/3258901631," +
                " 13276383826/9387821033, 442729449143/313056995720, 898735282112/635501812473," +
                " 6733876423927/4761569683031, 34568117401747/24443350227628, 75870111227421/53648270138287," +
                " 110438228629168/78091620365915, 186308339856589/131739890504202," +
                " 3091371666334592/2185929868433147, 6369051672525773/4503599627370496]");
        aeq(ofExact(-Math.sqrt(2)).convergents(),
                "[-2, -1, -3/2, -7/5, -17/12, -41/29, -99/70, -239/169, -577/408, -1393/985, -3363/2378, -8119/5741," +
                " -19601/13860, -47321/33461, -114243/80782, -275807/195025, -665857/470832, -1607521/1136689," +
                " -3880899/2744210, -9369319/6625109, -22619537/15994428, -54608393/38613965, -77227930/54608393," +
                " -131836323/93222358, -209064253/147830751, -549964829/388883860, -4058818056/2870017771," +
                " -4608782885/3258901631, -13276383826/9387821033, -442729449143/313056995720," +
                " -898735282112/635501812473, -6733876423927/4761569683031, -34568117401747/24443350227628," +
                " -75870111227421/53648270138287, -110438228629168/78091620365915, -186308339856589/131739890504202," +
                " -3091371666334592/2185929868433147, -6369051672525773/4503599627370496]");
        aeq(ofExact(Math.PI).convergents(),
                "[3, 22/7, 333/106, 355/113, 103993/33102, 104348/33215, 208341/66317, 312689/99532, 833719/265381," +
                " 1146408/364913, 4272943/1360120, 5419351/1725033, 80143857/25510582, 245850922/78256779," +
                " 817696623/260280919, 1881244168/598818617, 2698940791/859099536, 9978066541/3176117225," +
                " 32633140414/10387451211, 238410049439/75888275702, 509453239292/162164002615," +
                " 747863288731/238052278317, 1257316528023/400216280932, 4519812872800/1438701121113," +
                " 10296942273623/3277618523158, 436991388364966/139098679093749, 884279719003555/281474976710656]");
        aeq(ofExact(-Math.PI).convergents(),
                "[-4, -3, -22/7, -333/106, -355/113, -103993/33102, -104348/33215, -208341/66317, -312689/99532," +
                " -833719/265381, -1146408/364913, -4272943/1360120, -5419351/1725033, -80143857/25510582," +
                " -245850922/78256779, -817696623/260280919, -1881244168/598818617, -2698940791/859099536," +
                " -9978066541/3176117225, -32633140414/10387451211, -238410049439/75888275702," +
                " -509453239292/162164002615, -747863288731/238052278317, -1257316528023/400216280932," +
                " -4519812872800/1438701121113, -10296942273623/3277618523158, -436991388364966/139098679093749," +
                " -884279719003555/281474976710656]");
        aeq(ofExact(Math.E).convergents(),
                "[2, 3, 8/3, 11/4, 19/7, 87/32, 106/39, 193/71, 1264/465, 1457/536, 2721/1001, 23225/8544," +
                " 25946/9545, 49171/18089, 517656/190435, 566827/208524, 1084483/398959, 13580623/4996032," +
                " 14665106/5394991, 28245729/10391023, 325368125/119696244, 353613854/130087267," +
                " 678981979/249783511, 1032595833/379870778, 12037536142/4428362069, 61220276543/22521681123," +
                " 73257812685/26950043192, 134478089228/49471724315, 342213991141/125893491822," +
                " 476692080369/175365216137, 2248982312617/827354356370, 4974656705603/1830073928877," +
                " 7223639018220/2657428285247, 12198295723823/4487502214124, 117008300532627/43044948212363," +
                " 2001339404778482/736251621824295, 6121026514868073/2251799813685248]");
        aeq(ofExact(-Math.E).convergents(),
                "[-3, -8/3, -11/4, -19/7, -87/32, -106/39, -193/71, -1264/465, -1457/536, -2721/1001, -23225/8544," +
                " -25946/9545, -49171/18089, -517656/190435, -566827/208524, -1084483/398959, -13580623/4996032," +
                " -14665106/5394991, -28245729/10391023, -325368125/119696244, -353613854/130087267," +
                " -678981979/249783511, -1032595833/379870778, -12037536142/4428362069, -61220276543/22521681123," +
                " -73257812685/26950043192, -134478089228/49471724315, -342213991141/125893491822," +
                " -476692080369/175365216137, -2248982312617/827354356370, -4974656705603/1830073928877," +
                " -7223639018220/2657428285247, -12198295723823/4487502214124, -117008300532627/43044948212363," +
                " -2001339404778482/736251621824295, -6121026514868073/2251799813685248]");
    }

    @Test
    public void testPositionalNotation() {
        aeq(ZERO.positionalNotation(BigInteger.valueOf(2)), "([], [], [0])");
        aeq(ZERO.positionalNotation(BigInteger.valueOf(3)), "([], [], [0])");
        aeq(ZERO.positionalNotation(BigInteger.valueOf(4)), "([], [], [0])");
        aeq(ZERO.positionalNotation(BigInteger.valueOf(10)), "([], [], [0])");
        aeq(ZERO.positionalNotation(BigInteger.valueOf(16)), "([], [], [0])");
        aeq(ZERO.positionalNotation(BigInteger.valueOf(83)), "([], [], [0])");
        aeq(ZERO.positionalNotation(BigInteger.valueOf(100)), "([], [], [0])");
        aeq(ONE.positionalNotation(BigInteger.valueOf(2)), "([1], [], [0])");
        aeq(ONE.positionalNotation(BigInteger.valueOf(3)), "([1], [], [0])");
        aeq(ONE.positionalNotation(BigInteger.valueOf(4)), "([1], [], [0])");
        aeq(ONE.positionalNotation(BigInteger.valueOf(10)), "([1], [], [0])");
        aeq(ONE.positionalNotation(BigInteger.valueOf(16)), "([1], [], [0])");
        aeq(ONE.positionalNotation(BigInteger.valueOf(83)), "([1], [], [0])");
        aeq(ONE.positionalNotation(BigInteger.valueOf(100)), "([1], [], [0])");
        aeq(read("1/2").get().positionalNotation(BigInteger.valueOf(2)), "([], [1], [0])");
        aeq(read("1/2").get().positionalNotation(BigInteger.valueOf(3)), "([], [], [1])");
        aeq(read("1/2").get().positionalNotation(BigInteger.valueOf(4)), "([], [2], [0])");
        aeq(read("1/2").get().positionalNotation(BigInteger.valueOf(10)), "([], [5], [0])");
        aeq(read("1/2").get().positionalNotation(BigInteger.valueOf(16)), "([], [8], [0])");
        aeq(read("1/2").get().positionalNotation(BigInteger.valueOf(83)), "([], [], [41])");
        aeq(read("1/2").get().positionalNotation(BigInteger.valueOf(100)), "([], [50], [0])");
        aeq(read("1/3").get().positionalNotation(BigInteger.valueOf(2)), "([], [], [0, 1])");
        aeq(read("1/3").get().positionalNotation(BigInteger.valueOf(3)), "([], [1], [0])");
        aeq(read("1/3").get().positionalNotation(BigInteger.valueOf(4)), "([], [], [1])");
        aeq(read("1/3").get().positionalNotation(BigInteger.valueOf(10)), "([], [], [3])");
        aeq(read("1/3").get().positionalNotation(BigInteger.valueOf(16)), "([], [], [5])");
        aeq(read("1/3").get().positionalNotation(BigInteger.valueOf(83)), "([], [], [27, 55])");
        aeq(read("1/3").get().positionalNotation(BigInteger.valueOf(100)), "([], [], [33])");
        aeq(read("1/7").get().positionalNotation(BigInteger.valueOf(2)), "([], [], [0, 0, 1])");
        aeq(read("1/7").get().positionalNotation(BigInteger.valueOf(3)), "([], [], [0, 1, 0, 2, 1, 2])");
        aeq(read("1/7").get().positionalNotation(BigInteger.valueOf(4)), "([], [], [0, 2, 1])");
        aeq(read("1/7").get().positionalNotation(BigInteger.valueOf(10)), "([], [], [1, 4, 2, 8, 5, 7])");
        aeq(read("1/7").get().positionalNotation(BigInteger.valueOf(16)), "([], [], [2, 4, 9])");
        aeq(read("1/7").get().positionalNotation(BigInteger.valueOf(83)), "([], [], [11, 71])");
        aeq(read("1/7").get().positionalNotation(BigInteger.valueOf(100)), "([], [], [14, 28, 57])");
        aeq(read("415/93").get().positionalNotation(BigInteger.valueOf(2)),
                "([1, 0, 0], [], [0, 1, 1, 1, 0, 1, 1, 0, 0, 1])");
        aeq(read("415/93").get().positionalNotation(BigInteger.valueOf(3)),
                "([1, 1], [1]," +
                " [1, 0, 1, 1, 1, 0, 0, 1, 2, 0, 2, 0, 0, 0, 2, 1, 2, 1, 1, 1, 2, 2, 1, 0, 2, 0, 2, 2, 2, 0])");
        aeq(read("415/93").get().positionalNotation(BigInteger.valueOf(4)), "([1, 0], [], [1, 3, 1, 2, 1])");
        aeq(read("415/93").get().positionalNotation(BigInteger.valueOf(10)),
                "([4], [], [4, 6, 2, 3, 6, 5, 5, 9, 1, 3, 9, 7, 8, 4, 9])");
        aeq(read("415/93").get().positionalNotation(BigInteger.valueOf(16)), "([4], [], [7, 6, 5, 13, 9])");
        aeq(read("415/93").get().positionalNotation(BigInteger.valueOf(83)),
                "([4], []," +
                " [38, 31, 19, 52, 54, 36, 49, 7, 11, 49, 81, 17, 70, 41, 78, 44, 51, 63, 30, 28, 46, 33, 75, 71," +
                " 33, 1, 65, 12, 41, 4])");
        aeq(read("415/93").get().positionalNotation(BigInteger.valueOf(100)),
                "([4], [], [46, 23, 65, 59, 13, 97, 84, 94, 62, 36, 55, 91, 39, 78, 49])");
        Rational approxPi = ofExact(Math.PI);
        aeq(approxPi.positionalNotation(BigInteger.valueOf(2)),
                "([1, 1]," +
                " [0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0," +
                " 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1], [0])");
        aeq(approxPi.positionalNotation(BigInteger.valueOf(4)),
                "([3], [0, 2, 1, 0, 0, 3, 3, 3, 1, 2, 2, 2, 2, 0, 2, 0, 2, 0, 1, 1, 2, 2, 0, 3], [0])");
        aeq(approxPi.positionalNotation(BigInteger.valueOf(10)),
                "([3], [1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 1, 1, 5, 9, 9, 7, 9, 6, 3, 4, 6, 8, 5, 4, 4, 1," +
                " 8, 5, 1, 6, 1, 5, 9, 0, 5, 7, 6, 1, 7, 1, 8, 7, 5], [0])");
        aeq(approxPi.positionalNotation(BigInteger.valueOf(16)),
                "([3], [2, 4, 3, 15, 6, 10, 8, 8, 8, 5, 10, 3], [0])");
        aeq(approxPi.positionalNotation(BigInteger.valueOf(100)),
                "([3]," +
                " [14, 15, 92, 65, 35, 89, 79, 31, 15, 99, 79, 63, 46, 85, 44, 18, 51, 61, 59, 5, 76, 17, 18, 75]," +
                " [0])");
        aeq(read("299/56").get().positionalNotation(BigInteger.valueOf(2)), "([1, 0, 1], [0, 1, 0], [1, 0, 1])");
        aeq(read("405/26").get().positionalNotation(BigInteger.valueOf(3)), "([1, 2, 0], [], [1, 2, 0])");
        aeq(read("15613/576").get().positionalNotation(BigInteger.valueOf(4)), "([1, 2, 3], [0, 1, 2], [3, 0, 1])");
        aeq(
                read("41111111/333000").get().positionalNotation(BigInteger.valueOf(10)),
                "([1, 2, 3], [4, 5, 6], [7, 8, 9])"
        );
        aeq(
                read("1628508433/5591040").get().positionalNotation(BigInteger.valueOf(16)),
                "([1, 2, 3], [4, 5, 6], [7, 8, 9])"
        );
        aeq(
                read("1153778558235787/163469900791").get().positionalNotation(BigInteger.valueOf(83)),
                "([1, 2, 3], [4, 5, 6], [7, 8, 9])"
        );
        aeq(
                read("3401010101010101/333333000000").get().positionalNotation(BigInteger.valueOf(100)),
                "([1, 2, 3], [4, 5, 6], [7, 8, 9])"
        );
        try {
            read("1/2").get().positionalNotation(BigInteger.ONE);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("1/2").get().positionalNotation(BigInteger.ZERO);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            read("-1/2").get().positionalNotation(BigInteger.valueOf(2));
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromPositionalNotation() {
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(2),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                0
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(3),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                0
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(4),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                0
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(10),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                0
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(16),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                0
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(83),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                0
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(100),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                0
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(2),
                        readBigIntegerList("[1]").get(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                1
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(3),
                        readBigIntegerList("[1]").get(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                1
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(4),
                        readBigIntegerList("[1]").get(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                1
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(10),
                        readBigIntegerList("[1]").get(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                1
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(16),
                        readBigIntegerList("[1]").get(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                1
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(83),
                        readBigIntegerList("[1]").get(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                1
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(100),
                        readBigIntegerList("[1]").get(),
                        new ArrayList<>(),
                        readBigIntegerList("[0]").get()
                ),
                1
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(2),
                        new ArrayList<>(),
                        readBigIntegerList("[1]").get(),
                        readBigIntegerList("[0]").get()
                ),
                "1/2"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(3),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[1]").get()
                ),
                "1/2"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(4),
                        new ArrayList<>(),
                        readBigIntegerList("[2]").get(),
                        readBigIntegerList("[0]").get()
                ),
                "1/2"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(10),
                        new ArrayList<>(),
                        readBigIntegerList("[5]").get(),
                        readBigIntegerList("[0]").get()
                ),
                "1/2"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(16),
                        new ArrayList<>(),
                        readBigIntegerList("[8]").get(),
                        readBigIntegerList("[0]").get()
                ),
                "1/2"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(83),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[41]").get()
                ),
                "1/2"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(100),
                        new ArrayList<>(),
                        readBigIntegerList("[50]").get(),
                        readBigIntegerList("[0]").get()
                ),
                "1/2"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(2),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[0, 1]").get()
                ),
                "1/3"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(3),
                        new ArrayList<>(),
                        readBigIntegerList("[1]").get(),
                        readBigIntegerList("[0]").get()
                ),
                "1/3"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(4),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[1]").get()
                ),
                "1/3"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(10),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[3]").get()
                ),
                "1/3"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(16),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[5]").get()
                ),
                "1/3"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(83),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[27, 55]").get()
                ),
                "1/3"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(100),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[33]").get()
                ),
                "1/3"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(2),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[0, 0, 1]").get()
                ),
                "1/7"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(3),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[0, 1, 0, 2, 1, 2]").get()
                ),
                "1/7"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(4),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[0, 2, 1]").get()
                ),
                "1/7"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(10),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[1, 4, 2, 8, 5, 7]").get()
                ),
                "1/7"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(16),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[2, 4, 9]").get()
                ),
                "1/7"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(83),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[11, 71]").get()
                ),
                "1/7"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(100),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        readBigIntegerList("[14, 28, 57]").get()
                ),
                "1/7"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(2),
                        readBigIntegerList("[1, 0, 0]").get(),
                        new ArrayList<>(),
                        readBigIntegerList("[0, 1, 1, 1, 0, 1, 1, 0, 0, 1]").get()
                ),
                "415/93"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(3),
                        readBigIntegerList("[1, 1]").get(),
                        readBigIntegerList("[1]").get(),
                        readBigIntegerList(
                                "[1, 0, 1, 1, 1, 0, 0, 1, 2, 0, 2, 0, 0, 0, 2, 1, 2, 1, 1, 1, 2, 2, 1, 0, 2, 0, 2," +
                                " 2, 2, 0]").get()
                ),
                "415/93"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(4),
                        readBigIntegerList("[1, 0]").get(),
                        new ArrayList<>(),
                        readBigIntegerList("[1, 3, 1, 2, 1]").get()
                ),
                "415/93"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(10),
                        readBigIntegerList("[4]").get(),
                        new ArrayList<>(),
                        readBigIntegerList("[4, 6, 2, 3, 6, 5, 5, 9, 1, 3, 9, 7, 8, 4, 9]").get()
                ),
                "415/93"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(16),
                        readBigIntegerList("[4]").get(),
                        new ArrayList<>(),
                        readBigIntegerList("[7, 6, 5, 13, 9]").get()
                ),
                "415/93"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(83),
                        readBigIntegerList("[4]").get(),
                        new ArrayList<>(),
                        readBigIntegerList(
                                "[38, 31, 19, 52, 54, 36, 49, 7, 11, 49, 81, 17, 70, 41, 78, 44, 51, 63, 30, 28, 46," +
                                " 33, 75, 71, 33, 1, 65, 12, 41, 4]").get()
                ),
                "415/93"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(100),
                        readBigIntegerList("[4]").get(),
                        new ArrayList<>(),
                        readBigIntegerList("[46, 23, 65, 59, 13, 97, 84, 94, 62, 36, 55, 91, 39, 78, 49]").get()
                ),
                "415/93"
        );
        Rational approxPi = ofExact(Math.PI);
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(2),
                        readBigIntegerList("[1, 1]").get(),
                        readBigIntegerList(
                                "[0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0," +
                                " 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1]").get(),
                        readBigIntegerList("[0]").get()
                ),
                approxPi
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(4),
                        readBigIntegerList("[3]").get(),
                        readBigIntegerList(
                                "[0, 2, 1, 0, 0, 3, 3, 3, 1, 2, 2, 2, 2, 0, 2, 0, 2, 0, 1, 1, 2, 2, 0, 3]").get(),
                        readBigIntegerList("[0]").get()
                ),
                approxPi
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(10),
                        readBigIntegerList("[3]").get(),
                        readBigIntegerList(
                                "[1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 1, 1, 5, 9, 9, 7, 9, 6, 3, 4, 6, 8," +
                                " 5, 4, 4, 1, 8, 5, 1, 6, 1, 5, 9, 0, 5, 7, 6, 1, 7, 1, 8, 7, 5]").get(),
                        readBigIntegerList("[0]").get()
                ),
                approxPi
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(16),
                        readBigIntegerList("[3]").get(),
                        readBigIntegerList("[2, 4, 3, 15, 6, 10, 8, 8, 8, 5, 10, 3]").get(),
                        readBigIntegerList("[0]").get()
                ),
                approxPi
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(100),
                        readBigIntegerList("[3]").get(),
                        readBigIntegerList(
                                "[14, 15, 92, 65, 35, 89, 79, 31, 15, 99, 79, 63, 46, 85, 44, 18, 51, 61, 59, 5, 76," +
                                " 17, 18, 75]").get(),
                        readBigIntegerList("[0]").get()
                ),
                approxPi
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(2),
                        readBigIntegerList("[1, 0, 1]").get(),
                        readBigIntegerList("[0, 1, 0]").get(),
                        readBigIntegerList("[1, 0, 1]").get()
                ),
                "299/56"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(3),
                        readBigIntegerList("[1, 2, 0]").get(),
                        readBigIntegerList("[1, 2, 0]").get(),
                        readBigIntegerList("[1, 2, 0]").get()
                ),
                "405/26"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(4),
                        readBigIntegerList("[1, 2, 3]").get(),
                        readBigIntegerList("[0, 1, 2]").get(),
                        readBigIntegerList("[3, 0, 1]").get()
                ),
                "15613/576"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(10),
                        readBigIntegerList("[1, 2, 3]").get(),
                        readBigIntegerList("[4, 5, 6]").get(),
                        readBigIntegerList("[7, 8, 9]").get()
                ),
                "41111111/333000"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(16),
                        readBigIntegerList("[1, 2, 3]").get(),
                        readBigIntegerList("[4, 5, 6]").get(),
                        readBigIntegerList("[7, 8, 9]").get()
                ),
                "1628508433/5591040"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(83),
                        readBigIntegerList("[1, 2, 3]").get(),
                        readBigIntegerList("[4, 5, 6]").get(),
                        readBigIntegerList("[7, 8, 9]").get()
                ),
                "1153778558235787/163469900791"
        );
        aeq(
                fromPositionalNotation(
                        BigInteger.valueOf(100),
                        readBigIntegerList("[1, 2, 3]").get(),
                        readBigIntegerList("[4, 5, 6]").get(),
                        readBigIntegerList("[7, 8, 9]").get()
                ),
                "3401010101010101/333333000000"
        );
        try {
            fromPositionalNotation(BigInteger.valueOf(1),
                    readBigIntegerList("[1, 0, 1]").get(),
                    readBigIntegerList("[0, 1, 0]").get(),
                    readBigIntegerList("[1, 0, 1]").get()
            );
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            fromPositionalNotation(BigInteger.valueOf(0),
                    readBigIntegerList("[1, 0, 1]").get(),
                    readBigIntegerList("[0, 1, 0]").get(),
                    readBigIntegerList("[1, 0, 1]").get()
            );
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            fromPositionalNotation(BigInteger.valueOf(-1),
                    readBigIntegerList("[1, 0, 1]").get(),
                    readBigIntegerList("[0, 1, 0]").get(),
                    readBigIntegerList("[1, 0, 1]").get()
            );
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            fromPositionalNotation(BigInteger.valueOf(2),
                    readBigIntegerList("[-1, 0, 1]").get(),
                    readBigIntegerList("[0, 1, 0]").get(),
                    readBigIntegerList("[1, 0, 1]").get()
            );
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            fromPositionalNotation(BigInteger.valueOf(2),
                    readBigIntegerList("[2, 0, 1]").get(),
                    readBigIntegerList("[0, 1, 0]").get(),
                    readBigIntegerList("[1, 0, 1]").get()
            );
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            fromPositionalNotation(BigInteger.valueOf(2),
                    readBigIntegerList("[1, 0, 1]").get(),
                    readBigIntegerList("[0, -1, 0]").get(),
                    readBigIntegerList("[1, 0, 1]").get()
            );
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            fromPositionalNotation(BigInteger.valueOf(2),
                    readBigIntegerList("[1, 0, 1]").get(),
                    readBigIntegerList("[0, 2, 0]").get(),
                    readBigIntegerList("[1, 0, 1]").get()
            );
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            fromPositionalNotation(BigInteger.valueOf(2),
                    readBigIntegerList("[1, 0, 1]").get(),
                    readBigIntegerList("[0, 1, 0]").get(),
                    readBigIntegerList("[-1, 0, 1]").get()
            );
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            fromPositionalNotation(BigInteger.valueOf(2),
                    readBigIntegerList("[1, 0, 1]").get(),
                    readBigIntegerList("[0, 1, 0]").get(),
                    readBigIntegerList("[2, 0, 1]").get()
            );
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            fromPositionalNotation(BigInteger.valueOf(2),
                    readBigIntegerList("[1, 0, 1]").get(),
                    readBigIntegerList("[0, 1, 0]").get(),
                    new ArrayList<>()
            );
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testDigits() {
        Pair<List<BigInteger>, Iterable<BigInteger>> result;

        result = ZERO.digits(BigInteger.valueOf(2));
        aeq(result.a, "[]");
        aeq(result.b, "[]");

        result = ZERO.digits(BigInteger.valueOf(3));
        aeq(result.a, "[]");
        aeq(result.b, "[]");

        result = ZERO.digits(BigInteger.valueOf(4));
        aeq(result.a, "[]");
        aeq(result.b, "[]");

        result = ZERO.digits(BigInteger.valueOf(10));
        aeq(result.a, "[]");
        aeq(result.b, "[]");

        result = ZERO.digits(BigInteger.valueOf(16));
        aeq(result.a, "[]");
        aeq(result.b, "[]");

        result = ZERO.digits(BigInteger.valueOf(83));
        aeq(result.a, "[]");
        aeq(result.b, "[]");

        result = ZERO.digits(BigInteger.valueOf(100));
        aeq(result.a, "[]");
        aeq(result.b, "[]");

        result = ONE.digits(BigInteger.valueOf(2));
        aeq(result.a, "[1]");
        aeq(result.b, "[]");

        result = ONE.digits(BigInteger.valueOf(3));
        aeq(result.a, "[1]");
        aeq(result.b, "[]");

        result = ONE.digits(BigInteger.valueOf(4));
        aeq(result.a, "[1]");
        aeq(result.b, "[]");

        result = ONE.digits(BigInteger.valueOf(10));
        aeq(result.a, "[1]");
        aeq(result.b, "[]");

        result = ONE.digits(BigInteger.valueOf(16));
        aeq(result.a, "[1]");
        aeq(result.b, "[]");

        result = ONE.digits(BigInteger.valueOf(83));
        aeq(result.a, "[1]");
        aeq(result.b, "[]");

        result = ONE.digits(BigInteger.valueOf(100));
        aeq(result.a, "[1]");
        aeq(result.b, "[]");

        result = read("1/2").get().digits(BigInteger.valueOf(2));
        aeq(result.a, "[]");
        aeq(result.b, "[1]");

        result = read("1/2").get().digits(BigInteger.valueOf(3));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b), "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]");

        result = read("1/2").get().digits(BigInteger.valueOf(4));
        aeq(result.a, "[]");
        aeq(result.b, "[2]");

        result = read("1/2").get().digits(BigInteger.valueOf(10));
        aeq(result.a, "[]");
        aeq(result.b, "[5]");

        result = read("1/2").get().digits(BigInteger.valueOf(16));
        aeq(result.a, "[]");
        aeq(result.b, "[8]");

        result = read("1/2").get().digits(BigInteger.valueOf(83));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b),
                "[41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, ...]");

        result = read("1/2").get().digits(BigInteger.valueOf(100));
        aeq(result.a, "[]");
        aeq(result.b, "[50]");

        result = read("1/3").get().digits(BigInteger.valueOf(2));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b), "[0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, ...]");

        result = read("1/3").get().digits(BigInteger.valueOf(3));
        aeq(result.a, "[]");
        aeq(result.b, "[1]");

        result = read("1/3").get().digits(BigInteger.valueOf(4));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b), "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]");

        result = read("1/3").get().digits(BigInteger.valueOf(10));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b), "[3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, ...]");
        result = read("1/3").get().digits(BigInteger.valueOf(16));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b), "[5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, ...]");

        result = read("1/3").get().digits(BigInteger.valueOf(83));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b),
                "[27, 55, 27, 55, 27, 55, 27, 55, 27, 55, 27, 55, 27, 55, 27, 55, 27, 55, 27, 55, ...]");

        result = read("1/3").get().digits(BigInteger.valueOf(100));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b),
                "[33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, ...]");

        result = read("1/7").get().digits(BigInteger.valueOf(2));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b), "[0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, ...]");

        result = read("1/7").get().digits(BigInteger.valueOf(3));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b), "[0, 1, 0, 2, 1, 2, 0, 1, 0, 2, 1, 2, 0, 1, 0, 2, 1, 2, 0, 1, ...]");

        result = read("1/7").get().digits(BigInteger.valueOf(4));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b), "[0, 2, 1, 0, 2, 1, 0, 2, 1, 0, 2, 1, 0, 2, 1, 0, 2, 1, 0, 2, ...]");

        result = read("1/7").get().digits(BigInteger.valueOf(10));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b), "[1, 4, 2, 8, 5, 7, 1, 4, 2, 8, 5, 7, 1, 4, 2, 8, 5, 7, 1, 4, ...]");

        result = read("1/7").get().digits(BigInteger.valueOf(16));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b), "[2, 4, 9, 2, 4, 9, 2, 4, 9, 2, 4, 9, 2, 4, 9, 2, 4, 9, 2, 4, ...]");

        result = read("1/7").get().digits(BigInteger.valueOf(83));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b),
                "[11, 71, 11, 71, 11, 71, 11, 71, 11, 71, 11, 71, 11, 71, 11, 71, 11, 71, 11, 71, ...]");

        result = read("1/7").get().digits(BigInteger.valueOf(100));
        aeq(result.a, "[]");
        aeq(IterableUtils.toString(20, result.b),
                "[14, 28, 57, 14, 28, 57, 14, 28, 57, 14, 28, 57, 14, 28, 57, 14, 28, 57, 14, 28, ...]");

        result = read("415/93").get().digits(BigInteger.valueOf(2));
        aeq(result.a, "[1, 0, 0]");
        aeq(IterableUtils.toString(20, result.b), "[0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, ...]");

        result = read("415/93").get().digits(BigInteger.valueOf(3));
        aeq(result.a, "[1, 1]");
        aeq(IterableUtils.toString(20, result.b), "[1, 1, 0, 1, 1, 1, 0, 0, 1, 2, 0, 2, 0, 0, 0, 2, 1, 2, 1, 1, ...]");

        result = read("415/93").get().digits(BigInteger.valueOf(4));
        aeq(result.a, "[1, 0]");
        aeq(IterableUtils.toString(20, result.b), "[1, 3, 1, 2, 1, 1, 3, 1, 2, 1, 1, 3, 1, 2, 1, 1, 3, 1, 2, 1, ...]");

        result = read("415/93").get().digits(BigInteger.valueOf(10));
        aeq(result.a, "[4]");
        aeq(IterableUtils.toString(20, result.b), "[4, 6, 2, 3, 6, 5, 5, 9, 1, 3, 9, 7, 8, 4, 9, 4, 6, 2, 3, 6, ...]");

        result = read("415/93").get().digits(BigInteger.valueOf(16));
        aeq(result.a, "[4]");
        aeq(IterableUtils.toString(20, result.b),
                "[7, 6, 5, 13, 9, 7, 6, 5, 13, 9, 7, 6, 5, 13, 9, 7, 6, 5, 13, 9, ...]");

        result = read("415/93").get().digits(BigInteger.valueOf(83));
        aeq(result.a, "[4]");
        aeq(IterableUtils.toString(20, result.b),
                "[38, 31, 19, 52, 54, 36, 49, 7, 11, 49, 81, 17, 70, 41, 78, 44, 51, 63, 30, 28, ...]");

        result = read("415/93").get().digits(BigInteger.valueOf(100));
        aeq(result.a, "[4]");
        aeq(IterableUtils.toString(20, result.b),
                "[46, 23, 65, 59, 13, 97, 84, 94, 62, 36, 55, 91, 39, 78, 49, 46, 23, 65, 59, 13, ...]");

        Rational approxPi = ofExact(Math.PI);

        result = approxPi.digits(BigInteger.valueOf(2));
        aeq(result.a, "[1, 1]");
        aeq(result.b,
                "[0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1," +
                " 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1]");

        result = approxPi.digits(BigInteger.valueOf(3));
        aeq(result.a, "[1, 0]");
        aeq(IterableUtils.toString(20, result.b), "[0, 1, 0, 2, 1, 1, 0, 1, 2, 2, 2, 2, 0, 1, 0, 2, 1, 1, 0, 0, ...]");

        result = approxPi.digits(BigInteger.valueOf(4));
        aeq(result.a, "[3]");
        aeq(result.b,
                "[0, 2, 1, 0, 0, 3, 3, 3, 1, 2, 2, 2, 2, 0, 2, 0, 2, 0, 1, 1, 2, 2, 0, 3]");

        result = approxPi.digits(BigInteger.valueOf(10));
        aeq(result.a, "[3]");
        aeq(result.b,
                "[1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 1, 1, 5, 9, 9, 7, 9, 6, 3, 4, 6, 8, 5, 4, 4, 1, 8, 5," +
                " 1, 6, 1, 5, 9, 0, 5, 7, 6, 1, 7, 1, 8, 7, 5]");

        result = approxPi.digits(BigInteger.valueOf(16));
        aeq(result.a, "[3]");
        aeq(result.b, "[2, 4, 3, 15, 6, 10, 8, 8, 8, 5, 10, 3]");

        result = approxPi.digits(BigInteger.valueOf(83));
        aeq(result.a, "[3]");
        aeq(IterableUtils.toString(20, result.b),
                "[11, 62, 35, 69, 50, 19, 79, 18, 11, 8, 60, 35, 10, 62, 20, 58, 42, 14, 31, 34, ...]");

        result = approxPi.digits(BigInteger.valueOf(100));
        aeq(result.a, "[3]");
        aeq(result.b,
                "[14, 15, 92, 65, 35, 89, 79, 31, 15, 99, 79, 63, 46, 85, 44, 18, 51, 61, 59, 5, 76, 17, 18, 75]");

        result = read("299/56").get().digits(BigInteger.valueOf(2));
        aeq(result.a, "[1, 0, 1]");
        aeq(IterableUtils.toString(20, result.b), "[0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, ...]");

        result = read("405/26").get().digits(BigInteger.valueOf(3));
        aeq(result.a, "[1, 2, 0]");
        aeq(IterableUtils.toString(20, result.b), "[1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, ...]");

        result = read("15613/576").get().digits(BigInteger.valueOf(4));
        aeq(result.a, "[1, 2, 3]");
        aeq(IterableUtils.toString(20, result.b), "[0, 1, 2, 3, 0, 1, 3, 0, 1, 3, 0, 1, 3, 0, 1, 3, 0, 1, 3, 0, ...]");

        result = read("41111111/333000").get().digits(BigInteger.valueOf(10));
        aeq(result.a, "[1, 2, 3]");
        aeq(IterableUtils.toString(20, result.b), "[4, 5, 6, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, ...]");

        result = read("1628508433/5591040").get().digits(BigInteger.valueOf(16));
        aeq(result.a, "[1, 2, 3]");
        aeq(IterableUtils.toString(20, result.b), "[4, 5, 6, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, ...]");

        result = read("1153778558235787/163469900791").get().digits(BigInteger.valueOf(83));
        aeq(result.a, "[1, 2, 3]");
        aeq(IterableUtils.toString(20, result.b), "[4, 5, 6, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, ...]");

        result = read("3401010101010101/333333000000").get().digits(BigInteger.valueOf(100));
        aeq(result.a, "[1, 2, 3]");
        aeq(IterableUtils.toString(20, result.b), "[4, 5, 6, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, ...]");

        try {
            read("-1/2").get().digits(BigInteger.valueOf(2));
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            read("1/2").get().digits(BigInteger.valueOf(1));
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            read("1/2").get().digits(BigInteger.valueOf(0));
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            read("1/2").get().digits(BigInteger.valueOf(-1));
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testEquals() {
        //noinspection EqualsWithItself
        assertTrue(ZERO.equals(ZERO));
        //noinspection EqualsWithItself
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
        assertFalse(read("12/6").isPresent());
        assertFalse(read("12/-6").isPresent());
        assertFalse(read("6/8").isPresent());
        assertFalse(read("4/1").isPresent());
    }

    @Test
    public void testFindIn() {
        aeq(findIn("abcd1234xyz").get(), "(1234, 4)");
        aeq(findIn("0123").get(), "(0, 0)");
        assertTrue(findIn("0123").get().a == ZERO);
        aeq(findIn("1-23").get(), "(1, 0)");
        assertTrue(findIn("1-23").get().a == ONE);
        aeq(findIn("a-23").get(), "(-23, 1)");
        aeq(findIn("---34--4").get(), "(-34, 2)");
        aeq(findIn(" 20.1 ").get(), "(20, 1)");
        aeq(findIn("ads4/3d").get(), "(4/3, 3)");
        aeq(findIn("101/101").get(), "(101/10, 0)");
        aeq(findIn("5/0").get(), "(5, 0)");
        assertFalse(findIn("").isPresent());
        assertFalse(findIn("hello").isPresent());
        assertFalse(findIn("vdfsvfbf").isPresent());
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

    private static @NotNull Optional<List<BigInteger>> readBigIntegerList(@NotNull String s) {
        return Readers.readList(Readers::findBigIntegerIn, s);
    }

    private static @NotNull Optional<List<Rational>> readRationalList(@NotNull String s) {
        return Readers.readList(Rational::findIn, s);
    }

    private static @NotNull Optional<List<Rational>> readRationalListWithNulls(@NotNull String s) {
        return Readers.readList(t -> Readers.findInWithNulls(Rational::findIn, t), s);
    }
}
