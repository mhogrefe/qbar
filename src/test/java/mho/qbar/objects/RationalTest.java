package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.FloatingPointUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.Rational.*;
import static mho.qbar.objects.Rational.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RationalTest {
    private static final int TINY_LIMIT = 20;
    private static final @NotNull Rational ALMOST_ONE =
            of(BigInteger.TEN.pow(1000).subtract(BigInteger.ONE), BigInteger.TEN.pow(1000));

    private static final @NotNull Rational TRILLION = of(BigInteger.TEN.pow(12));
    private static final @NotNull Rational PI_FLOAT = ofExact((float) Math.PI).get();
    private static final @NotNull Rational PI_SUCCESSOR_FLOAT =
            ofExact(FloatingPointUtils.successor((float) Math.PI)).get();
    private static final @NotNull Rational PI_PREDECESSOR_FLOAT =
            ofExact(FloatingPointUtils.predecessor((float) Math.PI)).get();
    private static final @NotNull Rational HALF_ABOVE_PI_FLOAT = PI_FLOAT.add(PI_SUCCESSOR_FLOAT).shiftRight(1);
    private static final @NotNull Rational HALF_BELOW_PI_FLOAT = PI_FLOAT.add(PI_PREDECESSOR_FLOAT).shiftRight(1);
    private static final @NotNull Rational JUST_ABOVE_PI_FLOAT =
            PI_FLOAT.shiftLeft(1).add(PI_SUCCESSOR_FLOAT).divide(3);
    private static final @NotNull Rational JUST_BELOW_PI_FLOAT =
            PI_FLOAT.shiftLeft(1).add(PI_PREDECESSOR_FLOAT).divide(3);
    private static final @NotNull Rational SUBNORMAL_FLOAT = ofExact(1.0E-40f).get();
    private static final @NotNull Rational SUBNORMAL_SUCCESSOR_FLOAT =
            ofExact(FloatingPointUtils.successor(1.0E-40f)).get();
    private static final @NotNull Rational SUBNORMAL_PREDECESSOR_FLOAT =
            ofExact(FloatingPointUtils.predecessor(1.0E-40f)).get();
    private static final @NotNull Rational HALF_ABOVE_SUBNORMAL_FLOAT =
            SUBNORMAL_FLOAT.add(SUBNORMAL_SUCCESSOR_FLOAT).shiftRight(1);
    private static final @NotNull Rational HALF_BELOW_SUBNORMAL_FLOAT =
            SUBNORMAL_FLOAT.add(SUBNORMAL_PREDECESSOR_FLOAT).shiftRight(1);
    private static final @NotNull Rational JUST_ABOVE_SUBNORMAL_FLOAT =
            SUBNORMAL_FLOAT.shiftLeft(1).add(SUBNORMAL_SUCCESSOR_FLOAT).divide(3);
    private static final @NotNull Rational JUST_BELOW_SUBNORMAL_FLOAT =
            SUBNORMAL_FLOAT.shiftLeft(1).add(SUBNORMAL_PREDECESSOR_FLOAT).divide(3);
    private static final @NotNull Rational BELOW_NEGATIVE_MAX_FLOAT = LARGEST_FLOAT.negate().subtract(ONE);
    private static final @NotNull Rational ALMOST_MAX_FLOAT =
            ofExact(FloatingPointUtils.predecessor(Float.MAX_VALUE)).get();
    private static final @NotNull Rational HALF_ABOVE_NEGATIVE_MAX_FLOAT =
            LARGEST_FLOAT.negate().add(ALMOST_MAX_FLOAT.negate()).shiftRight(1);
    private static final @NotNull Rational JUST_ABOVE_NEGATIVE_MAX_FLOAT =
            LARGEST_FLOAT.negate().shiftLeft(1).add(ALMOST_MAX_FLOAT.negate()).divide(3);
    private static final @NotNull Rational ABOVE_MAX_FLOAT = LARGEST_FLOAT.add(ONE);
    private static final @NotNull Rational HALF_BELOW_MAX_FLOAT = LARGEST_FLOAT.add(ALMOST_MAX_FLOAT).shiftRight(1);
    private static final @NotNull Rational JUST_BELOW_MAX_FLOAT =
            LARGEST_FLOAT.shiftLeft(1).add(ALMOST_MAX_FLOAT).divide(3);
    private static final @NotNull Rational HALF_ABOVE_ZERO_FLOAT = SMALLEST_FLOAT.shiftRight(1);
    private static final @NotNull Rational JUST_ABOVE_ZERO_FLOAT = SMALLEST_FLOAT.divide(3);
    private static final @NotNull Rational HALF_BELOW_ZERO_FLOAT = HALF_ABOVE_ZERO_FLOAT.negate();
    private static final @NotNull Rational JUST_BELOW_ZERO_FLOAT = JUST_ABOVE_ZERO_FLOAT.negate();
    private static final @NotNull Rational BOUNDARY_FLOAT =
            LARGEST_SUBNORMAL_FLOAT.add(SMALLEST_NORMAL_FLOAT).shiftRight(1);
    private static final @NotNull Rational JUST_BELOW_BOUNDARY_FLOAT =
            LARGEST_SUBNORMAL_FLOAT.add(BOUNDARY_FLOAT.shiftLeft(1)).divide(3);
    private static final @NotNull Rational JUST_ABOVE_BOUNDARY_FLOAT =
            SMALLEST_NORMAL_FLOAT.add(BOUNDARY_FLOAT.shiftLeft(1)).divide(3);

    private static final @NotNull Rational GOOGOL = of(BigInteger.TEN.pow(100));
    private static final @NotNull Rational PI_DOUBLE = ofExact(Math.PI).get();
    private static final @NotNull Rational PI_SUCCESSOR_DOUBLE =
            ofExact(FloatingPointUtils.successor(Math.PI)).get();
    private static final @NotNull Rational PI_PREDECESSOR_DOUBLE =
            ofExact(FloatingPointUtils.predecessor(Math.PI)).get();
    private static final @NotNull Rational HALF_ABOVE_PI_DOUBLE = PI_DOUBLE.add(PI_SUCCESSOR_DOUBLE).shiftRight(1);
    private static final @NotNull Rational HALF_BELOW_PI_DOUBLE = PI_DOUBLE.add(PI_PREDECESSOR_DOUBLE).shiftRight(1);
    private static final @NotNull Rational JUST_ABOVE_PI_DOUBLE =
            PI_DOUBLE.shiftLeft(1).add(PI_SUCCESSOR_DOUBLE).divide(3);
    private static final @NotNull Rational JUST_BELOW_PI_DOUBLE =
            PI_DOUBLE.shiftLeft(1).add(PI_PREDECESSOR_DOUBLE).divide(3);
    private static final @NotNull Rational SUBNORMAL_DOUBLE = ofExact(1.0E-310).get();
    private static final @NotNull Rational SUBNORMAL_SUCCESSOR_DOUBLE =
            ofExact(FloatingPointUtils.successor(1.0E-310)).get();
    private static final @NotNull Rational SUBNORMAL_PREDECESSOR_DOUBLE =
            ofExact(FloatingPointUtils.predecessor(1.0E-310)).get();
    private static final @NotNull Rational HALF_ABOVE_SUBNORMAL_DOUBLE =
            SUBNORMAL_DOUBLE.add(SUBNORMAL_SUCCESSOR_DOUBLE).shiftRight(1);
    private static final @NotNull Rational HALF_BELOW_SUBNORMAL_DOUBLE =
            SUBNORMAL_DOUBLE.add(SUBNORMAL_PREDECESSOR_DOUBLE).shiftRight(1);
    private static final @NotNull Rational JUST_ABOVE_SUBNORMAL_DOUBLE =
            SUBNORMAL_DOUBLE.shiftLeft(1).add(SUBNORMAL_SUCCESSOR_DOUBLE).divide(3);
    private static final @NotNull Rational JUST_BELOW_SUBNORMAL_DOUBLE =
            SUBNORMAL_DOUBLE.shiftLeft(1).add(SUBNORMAL_PREDECESSOR_DOUBLE).divide(3);
    private static final @NotNull Rational BELOW_NEGATIVE_MAX_DOUBLE = LARGEST_DOUBLE.negate().subtract(ONE);
    private static final @NotNull Rational ALMOST_MAX_DOUBLE =
            ofExact(FloatingPointUtils.predecessor(Double.MAX_VALUE)).get();
    private static final @NotNull Rational HALF_ABOVE_NEGATIVE_MAX_DOUBLE =
            LARGEST_DOUBLE.negate().add(ALMOST_MAX_DOUBLE.negate()).shiftRight(1);
    private static final @NotNull Rational JUST_ABOVE_NEGATIVE_MAX_DOUBLE =
            LARGEST_DOUBLE.negate().shiftLeft(1).add(ALMOST_MAX_DOUBLE.negate()).divide(3);
    private static final @NotNull Rational ABOVE_MAX_DOUBLE = LARGEST_DOUBLE.add(ONE);
    private static final @NotNull Rational HALF_BELOW_MAX_DOUBLE = LARGEST_DOUBLE.add(ALMOST_MAX_DOUBLE).shiftRight(1);
    private static final @NotNull Rational JUST_BELOW_MAX_DOUBLE =
            LARGEST_DOUBLE.shiftLeft(1).add(ALMOST_MAX_DOUBLE).divide(3);
    private static final @NotNull Rational HALF_ABOVE_ZERO_DOUBLE = SMALLEST_DOUBLE.shiftRight(1);
    private static final @NotNull Rational JUST_ABOVE_ZERO_DOUBLE = SMALLEST_DOUBLE.divide(3);
    private static final @NotNull Rational HALF_BELOW_ZERO_DOUBLE = HALF_ABOVE_ZERO_DOUBLE.negate();
    private static final @NotNull Rational JUST_BELOW_ZERO_DOUBLE = JUST_ABOVE_ZERO_DOUBLE.negate();
    private static final @NotNull Rational BOUNDARY_DOUBLE =
            LARGEST_SUBNORMAL_DOUBLE.add(SMALLEST_NORMAL_DOUBLE).shiftRight(1);
    private static final @NotNull Rational JUST_BELOW_BOUNDARY_DOUBLE =
            LARGEST_SUBNORMAL_DOUBLE.add(BOUNDARY_DOUBLE.shiftLeft(1)).divide(3);
    private static final @NotNull Rational JUST_ABOVE_BOUNDARY_DOUBLE =
            SMALLEST_NORMAL_DOUBLE.add(BOUNDARY_DOUBLE.shiftLeft(1)).divide(3);

    @Test
    public void testConstants() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
        aeq(TEN, "10");
        aeq(TWO, "2");
        aeq(NEGATIVE_ONE, "-1");
        aeq(ONE_HALF, "1/2");
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
        aeqitLimit(TINY_LIMIT, HARMONIC_NUMBERS,
                "[1, 3/2, 11/6, 25/12, 137/60, 49/20, 363/140, 761/280, 7129/2520, 7381/2520, 83711/27720," +
                " 86021/27720, 1145993/360360, 1171733/360360, 1195757/360360, 2436559/720720, 42142223/12252240," +
                " 14274301/4084080, 275295799/77597520, 55835135/15519504, ...]");
    }

    private static void getNumerator_helper(@NotNull String x, @NotNull String output) {
        aeq(read(x).get().getNumerator(), output);
    }

    @Test
    public void testGetNumerator() {
        getNumerator_helper("0", "0");
        getNumerator_helper("1", "1");
        getNumerator_helper("2", "2");
        getNumerator_helper("-2", "-2");
        getNumerator_helper("5/3", "5");
        getNumerator_helper("-5/3", "-5");
    }

    private static void getDenominator_helper(@NotNull String x, @NotNull String output) {
        aeq(read(x).get().getDenominator(), output);
    }

    @Test
    public void testGetDenominator() {
        getDenominator_helper("0", "1");
        getDenominator_helper("1", "1");
        getDenominator_helper("2", "1");
        getDenominator_helper("-2", "1");
        getDenominator_helper("5/3", "3");
        getDenominator_helper("-5/3", "3");
    }

    private static void of_BigInteger_BigInteger_helper(int x, int y, @NotNull String output) {
        aeq(of(BigInteger.valueOf(x), BigInteger.valueOf(y)), output);
    }

    @Test
    public void testOf_BigInteger_BigInteger() {
        of_BigInteger_BigInteger_helper(2, 3, "2/3");
        of_BigInteger_BigInteger_helper(4, 6, "2/3");
        of_BigInteger_BigInteger_helper(-4, -6, "2/3");
        of_BigInteger_BigInteger_helper(4, -6, "-2/3");
        of_BigInteger_BigInteger_helper(4, 4, "1");
        of_BigInteger_BigInteger_helper(4, 1, "4");
        of_BigInteger_BigInteger_helper(0, 1, "0");
        try {
            of(BigInteger.ONE, BigInteger.ZERO);
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

    private static void of_BinaryFraction_helper(@NotNull String input, @NotNull String output) {
        aeq(of(BinaryFraction.read(input).get()), output);
    }

    @Test
    public void testOf_BinaryFraction() {
        of_BinaryFraction_helper("0", "0");
        of_BinaryFraction_helper("1", "1");
        of_BinaryFraction_helper("11", "11");
        of_BinaryFraction_helper("5 >> 20", "5/1048576");
        of_BinaryFraction_helper("5 << 20", "5242880");
        of_BinaryFraction_helper("-1", "-1");
        of_BinaryFraction_helper("-11", "-11");
        of_BinaryFraction_helper("-5 >> 20", "-5/1048576");
        of_BinaryFraction_helper("-5 << 20", "-5242880");
    }

    private static void of_float_helper(float f, @NotNull String output) {
        aeq(of(f).get(), output);
    }

    private static void of_float_empty_helper(float f) {
        assertFalse(of(f).isPresent());
    }

    @Test
    public void testOf_float() {
        of_float_helper(0.0f, "0");
        of_float_helper(1.0f, "1");
        of_float_helper(13.0f, "13");
        of_float_helper(-5.0f, "-5");
        of_float_helper(1.5f, "3/2");
        of_float_helper(0.15625f, "5/32");
        of_float_helper(0.1f, "1/10");
        of_float_helper(1.0f / 3.0f, "16666667/50000000");
        of_float_helper(1.0e10f, "10000000000");
        of_float_helper(1.0e30f, "1000000000000000000000000000000");
        of_float_helper((float) Math.PI, "31415927/10000000");
        of_float_helper((float) Math.E, "27182817/10000000");
        of_float_helper((float) Math.sqrt(2), "2828427/2000000");
        of_float_helper(Float.MIN_VALUE, "7/5000000000000000000000000000000000000000000000");
        of_float_helper(-Float.MIN_VALUE, "-7/5000000000000000000000000000000000000000000000");
        of_float_helper(Float.MIN_NORMAL, "23509887/2000000000000000000000000000000000000000000000");
        of_float_helper(-Float.MIN_NORMAL, "-23509887/2000000000000000000000000000000000000000000000");
        of_float_helper(Float.MAX_VALUE, "340282350000000000000000000000000000000");
        of_float_helper(-Float.MAX_VALUE, "-340282350000000000000000000000000000000");
        of_float_empty_helper(Float.POSITIVE_INFINITY);
        of_float_empty_helper(Float.NEGATIVE_INFINITY);
        of_float_empty_helper(Float.NaN);
    }

    private static void of_double_helper(double d, @NotNull String output) {
        aeq(of(d).get(), output);
    }

    private static void of_double_empty_helper(double d) {
        assertFalse(of(d).isPresent());
    }

    @Test
    public void testOf_double() {
        of_double_helper(0.0, "0");
        of_double_helper(1.0, "1");
        of_double_helper(13.0, "13");
        of_double_helper(-5.0, "-5");
        of_double_helper(1.5, "3/2");
        of_double_helper(0.15625, "5/32");
        of_double_helper(0.1, "1/10");
        of_double_helper(1.0 / 3.0, "3333333333333333/10000000000000000");
        of_double_helper(1.0e10, "10000000000");
        of_double_helper(1.0e30, "1000000000000000000000000000000");
        of_double_helper(Math.PI, "3141592653589793/1000000000000000");
        of_double_helper(Math.E, "543656365691809/200000000000000");
        of_double_helper(Math.sqrt(2), "14142135623730951/10000000000000000");
        of_double_helper(Double.MIN_VALUE,
                "49/100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000");
        of_double_helper(-Double.MIN_VALUE,
                "-49/10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000");
        of_double_helper(Double.MIN_NORMAL,
                "11125369292536007/500000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000");
        of_double_helper(-Double.MIN_NORMAL,
                "-11125369292536007/50000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000");
        of_double_helper(Double.MAX_VALUE,
                "179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000");
        of_double_helper(-Double.MAX_VALUE,
                "-17976931348623157000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000");
        of_double_empty_helper(Double.POSITIVE_INFINITY);
        of_double_empty_helper(Double.NEGATIVE_INFINITY);
        of_double_empty_helper(Double.NaN);
    }

    private static void ofExact_float_helper(float f, @NotNull Object output) {
        aeq(ofExact(f).get(), output);
    }

    private static void ofExact_float_empty_helper(float f) {
        assertFalse(ofExact(f).isPresent());
    }

    @Test
    public void testOfExact_float() {
        ofExact_float_helper(0.0f, "0");
        ofExact_float_helper(1.0f, "1");
        ofExact_float_helper(13.0f, "13");
        ofExact_float_helper(-5.0f, "-5");
        ofExact_float_helper(1.5f, "3/2");
        ofExact_float_helper(0.15625f, "5/32");
        ofExact_float_helper(0.1f, "13421773/134217728");
        ofExact_float_helper(1.0f / 3.0f, "11184811/33554432");
        ofExact_float_helper(1.0e10f, "10000000000");
        ofExact_float_helper(1.0e30f, "1000000015047466219876688855040");
        ofExact_float_helper((float) Math.PI, "13176795/4194304");
        ofExact_float_helper((float) Math.E, "2850325/1048576");
        ofExact_float_helper((float) Math.sqrt(2), "11863283/8388608");
        ofExact_float_helper(Float.MIN_VALUE, SMALLEST_FLOAT);
        ofExact_float_helper(-Float.MIN_VALUE, SMALLEST_FLOAT.negate());
        ofExact_float_helper(Float.MIN_NORMAL, SMALLEST_NORMAL_FLOAT);
        ofExact_float_helper(-Float.MIN_NORMAL, SMALLEST_NORMAL_FLOAT.negate());
        ofExact_float_helper(Float.MAX_VALUE, LARGEST_FLOAT);
        ofExact_float_helper(-Float.MAX_VALUE, LARGEST_FLOAT.negate());
        ofExact_float_empty_helper(Float.POSITIVE_INFINITY);
        ofExact_float_empty_helper(Float.NEGATIVE_INFINITY);
        ofExact_float_empty_helper(Float.NaN);
    }

    private static void ofExact_double_helper(double d, @NotNull Object output) {
        aeq(ofExact(d).get(), output);
    }

    private static void ofExact_double_empty_helper(double d) {
        assertFalse(ofExact(d).isPresent());
    }

    @Test
    public void testOfExact_double() {
        ofExact_double_helper(0.0, "0");
        ofExact_double_helper(1.0, "1");
        ofExact_double_helper(13.0, "13");
        ofExact_double_helper(-5.0, "-5");
        ofExact_double_helper(1.5, "3/2");
        ofExact_double_helper(0.15625, "5/32");
        ofExact_double_helper(0.1, "3602879701896397/36028797018963968");
        ofExact_double_helper(1.0 / 3.0, "6004799503160661/18014398509481984");
        ofExact_double_helper(1.0e10, "10000000000");
        ofExact_double_helper(1.0e30, "1000000000000000019884624838656");
        ofExact_double_helper(Math.PI, "884279719003555/281474976710656");
        ofExact_double_helper(Math.E, "6121026514868073/2251799813685248");
        ofExact_double_helper(Math.sqrt(2), "6369051672525773/4503599627370496");
        ofExact_double_helper(Double.MIN_VALUE, SMALLEST_DOUBLE);
        ofExact_double_helper(-Double.MIN_VALUE, SMALLEST_DOUBLE.negate());
        ofExact_double_helper(Double.MIN_NORMAL, SMALLEST_NORMAL_DOUBLE);
        ofExact_double_helper(-Double.MIN_NORMAL, SMALLEST_NORMAL_DOUBLE.negate());
        ofExact_double_helper(Double.MAX_VALUE, LARGEST_DOUBLE);
        ofExact_double_helper(-Double.MAX_VALUE, LARGEST_DOUBLE.negate());
        ofExact_double_empty_helper(Double.POSITIVE_INFINITY);
        ofExact_double_empty_helper(Double.NEGATIVE_INFINITY);
        ofExact_double_empty_helper(Double.NaN);
    }

    private static void of_BigDecimal_helper(@NotNull String input, @NotNull String output) {
        aeq(of(Readers.readBigDecimal(input).get()), output);
    }

    @Test
    public void testOf_BigDecimal() {
        of_BigDecimal_helper("0", "0");
        of_BigDecimal_helper("1", "1");
        of_BigDecimal_helper("3", "3");
        of_BigDecimal_helper("-5", "-5");
        of_BigDecimal_helper("0.1", "1/10");
        of_BigDecimal_helper("3.14159", "314159/100000");
        of_BigDecimal_helper("-2.718281828459045", "-543656365691809/200000000000000");
        of_BigDecimal_helper("1E-14", "1/100000000000000");
        of_BigDecimal_helper("1000000000000000", "1000000000000000");
        of_BigDecimal_helper("1E+15", "1000000000000000");
    }

    private static void isInteger_helper(@NotNull Rational input, boolean output) {
        aeq(input.isInteger(), output);
    }

    private static void isInteger_helper(@NotNull String input, boolean output) {
        isInteger_helper(read(input).get(), output);
    }

    @Test
    public void testIsInteger() {
        isInteger_helper("0", true);
        isInteger_helper("1", true);
        isInteger_helper("5", true);
        isInteger_helper("-5", true);
        isInteger_helper("1/2", false);
        isInteger_helper("-1/2", false);
        isInteger_helper("-1/2", false);
        isInteger_helper(PI_DOUBLE, false);
        isInteger_helper(PI_DOUBLE.negate(), false);
    }

    private static void bigIntegerValue_RoundingMode_helper(
            @NotNull String r,
            @NotNull String roundingMode,
            int output
    ) {
        aeq(read(r).get().bigIntegerValue(Readers.readRoundingMode(roundingMode).get()), output);
    }

    private static void bigIntegerValue_RoundingMode_fail_helper(@NotNull String r, @NotNull String roundingMode) {
        try {
            read(r).get().bigIntegerValue(Readers.readRoundingMode(roundingMode).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBigIntegerValue_RoundingMode() {
        bigIntegerValue_RoundingMode_helper("11/2", "UP", 6);
        bigIntegerValue_RoundingMode_helper("5/2", "UP", 3);
        bigIntegerValue_RoundingMode_helper("8/5", "UP", 2);
        bigIntegerValue_RoundingMode_helper("11/10", "UP", 2);
        bigIntegerValue_RoundingMode_helper("1", "UP", 1);
        bigIntegerValue_RoundingMode_helper("0", "UP", 0);
        bigIntegerValue_RoundingMode_helper("-1", "UP", -1);
        bigIntegerValue_RoundingMode_helper("-11/10", "UP", -2);
        bigIntegerValue_RoundingMode_helper("-8/5", "UP", -2);
        bigIntegerValue_RoundingMode_helper("-5/2", "UP", -3);
        bigIntegerValue_RoundingMode_helper("-11/2", "UP", -6);

        bigIntegerValue_RoundingMode_helper("11/2", "DOWN", 5);
        bigIntegerValue_RoundingMode_helper("5/2", "DOWN", 2);
        bigIntegerValue_RoundingMode_helper("8/5", "DOWN", 1);
        bigIntegerValue_RoundingMode_helper("11/10", "DOWN", 1);
        bigIntegerValue_RoundingMode_helper("1", "DOWN", 1);
        bigIntegerValue_RoundingMode_helper("0", "DOWN", 0);
        bigIntegerValue_RoundingMode_helper("-1", "DOWN", -1);
        bigIntegerValue_RoundingMode_helper("-11/10", "DOWN", -1);
        bigIntegerValue_RoundingMode_helper("-8/5", "DOWN", -1);
        bigIntegerValue_RoundingMode_helper("-5/2", "DOWN", -2);
        bigIntegerValue_RoundingMode_helper("-11/2", "DOWN", -5);

        bigIntegerValue_RoundingMode_helper("11/2", "CEILING", 6);
        bigIntegerValue_RoundingMode_helper("5/2", "CEILING", 3);
        bigIntegerValue_RoundingMode_helper("8/5", "CEILING", 2);
        bigIntegerValue_RoundingMode_helper("11/10", "CEILING", 2);
        bigIntegerValue_RoundingMode_helper("1", "CEILING", 1);
        bigIntegerValue_RoundingMode_helper("0", "CEILING", 0);
        bigIntegerValue_RoundingMode_helper("-1", "CEILING", -1);
        bigIntegerValue_RoundingMode_helper("-11/10", "CEILING", -1);
        bigIntegerValue_RoundingMode_helper("-8/5", "CEILING", -1);
        bigIntegerValue_RoundingMode_helper("-5/2", "CEILING", -2);
        bigIntegerValue_RoundingMode_helper("-11/2", "CEILING", -5);

        bigIntegerValue_RoundingMode_helper("11/2", "FLOOR", 5);
        bigIntegerValue_RoundingMode_helper("5/2", "FLOOR", 2);
        bigIntegerValue_RoundingMode_helper("8/5", "FLOOR", 1);
        bigIntegerValue_RoundingMode_helper("11/10", "FLOOR", 1);
        bigIntegerValue_RoundingMode_helper("1", "FLOOR", 1);
        bigIntegerValue_RoundingMode_helper("0", "FLOOR", 0);
        bigIntegerValue_RoundingMode_helper("-1", "FLOOR", -1);
        bigIntegerValue_RoundingMode_helper("-11/10", "FLOOR", -2);
        bigIntegerValue_RoundingMode_helper("-8/5", "FLOOR", -2);
        bigIntegerValue_RoundingMode_helper("-5/2", "FLOOR", -3);
        bigIntegerValue_RoundingMode_helper("-11/2", "FLOOR", -6);

        bigIntegerValue_RoundingMode_helper("11/2", "HALF_UP", 6);
        bigIntegerValue_RoundingMode_helper("5/2", "HALF_UP", 3);
        bigIntegerValue_RoundingMode_helper("8/5", "HALF_UP", 2);
        bigIntegerValue_RoundingMode_helper("11/10", "HALF_UP", 1);
        bigIntegerValue_RoundingMode_helper("1", "HALF_UP", 1);
        bigIntegerValue_RoundingMode_helper("0", "HALF_UP", 0);
        bigIntegerValue_RoundingMode_helper("-1", "HALF_UP", -1);
        bigIntegerValue_RoundingMode_helper("-11/10", "HALF_UP", -1);
        bigIntegerValue_RoundingMode_helper("-8/5", "HALF_UP", -2);
        bigIntegerValue_RoundingMode_helper("-5/2", "HALF_UP", -3);
        bigIntegerValue_RoundingMode_helper("-11/2", "HALF_UP", -6);

        bigIntegerValue_RoundingMode_helper("11/2", "HALF_DOWN", 5);
        bigIntegerValue_RoundingMode_helper("5/2", "HALF_DOWN", 2);
        bigIntegerValue_RoundingMode_helper("8/5", "HALF_DOWN", 2);
        bigIntegerValue_RoundingMode_helper("11/10", "HALF_DOWN", 1);
        bigIntegerValue_RoundingMode_helper("1", "HALF_DOWN", 1);
        bigIntegerValue_RoundingMode_helper("0", "HALF_DOWN", 0);
        bigIntegerValue_RoundingMode_helper("-1", "HALF_DOWN", -1);
        bigIntegerValue_RoundingMode_helper("-11/10", "HALF_DOWN", -1);
        bigIntegerValue_RoundingMode_helper("-8/5", "HALF_DOWN", -2);
        bigIntegerValue_RoundingMode_helper("-5/2", "HALF_DOWN", -2);
        bigIntegerValue_RoundingMode_helper("-11/2", "HALF_DOWN", -5);

        bigIntegerValue_RoundingMode_helper("11/2", "HALF_EVEN", 6);
        bigIntegerValue_RoundingMode_helper("5/2", "HALF_EVEN", 2);
        bigIntegerValue_RoundingMode_helper("8/5", "HALF_EVEN", 2);
        bigIntegerValue_RoundingMode_helper("11/10", "HALF_EVEN", 1);
        bigIntegerValue_RoundingMode_helper("1", "HALF_EVEN", 1);
        bigIntegerValue_RoundingMode_helper("0", "HALF_EVEN", 0);
        bigIntegerValue_RoundingMode_helper("-1", "HALF_EVEN", -1);
        bigIntegerValue_RoundingMode_helper("-11/10", "HALF_EVEN", -1);
        bigIntegerValue_RoundingMode_helper("-8/5", "HALF_EVEN", -2);
        bigIntegerValue_RoundingMode_helper("-5/2", "HALF_EVEN", -2);
        bigIntegerValue_RoundingMode_helper("-11/2", "HALF_EVEN", -6);

        bigIntegerValue_RoundingMode_fail_helper("11/2", "UNNECESSARY");
        bigIntegerValue_RoundingMode_fail_helper("5/2", "UNNECESSARY");
        bigIntegerValue_RoundingMode_fail_helper("8/5", "UNNECESSARY");
        bigIntegerValue_RoundingMode_fail_helper("11/10", "UNNECESSARY");
        bigIntegerValue_RoundingMode_helper("1", "UNNECESSARY", 1);
        bigIntegerValue_RoundingMode_helper("0", "UNNECESSARY", 0);
        bigIntegerValue_RoundingMode_helper("-1", "UNNECESSARY", -1);
        bigIntegerValue_RoundingMode_fail_helper("-11/10", "UNNECESSARY");
        bigIntegerValue_RoundingMode_fail_helper("-8/5", "UNNECESSARY");
        bigIntegerValue_RoundingMode_fail_helper("-5/2", "UNNECESSARY");
        bigIntegerValue_RoundingMode_fail_helper("-11/2", "UNNECESSARY");
    }

    private static void bigIntegerValue_helper(@NotNull String r, int output
    ) {
        aeq(read(r).get().bigIntegerValue(), output);
    }

    @Test
    public void testBigIntegerValue() {
        bigIntegerValue_helper("11/2", 6);
        bigIntegerValue_helper("5/2", 2);
        bigIntegerValue_helper("8/5", 2);
        bigIntegerValue_helper("11/10", 1);
        bigIntegerValue_helper("1", 1);
        bigIntegerValue_helper("0", 0);
        bigIntegerValue_helper("-1", -1);
        bigIntegerValue_helper("-11/10", -1);
        bigIntegerValue_helper("-8/5", -2);
        bigIntegerValue_helper("-5/2", -2);
        bigIntegerValue_helper("-11/2", -6);
    }

    private static void floor_helper(@NotNull String input, int output) {
        aeq(read(input).get().floor(), output);
    }

    @Test
    public void testFloor() {
        floor_helper("7/3", 2);
        floor_helper("5/3", 1);
        floor_helper("2/3", 0);
        floor_helper("-1/3", -1);
        floor_helper("-4/3", -2);
        floor_helper("4", 4);
        floor_helper("-2", -2);
        floor_helper("0", 0);
        floor_helper("1", 1);
    }

    private static void ceiling_helper(@NotNull String input, int output) {
        aeq(read(input).get().ceiling(), output);
    }

    @Test
    public void testCeiling() {
        ceiling_helper("7/3", 3);
        ceiling_helper("5/3", 2);
        ceiling_helper("2/3", 1);
        ceiling_helper("-1/3", 0);
        ceiling_helper("-4/3", -1);
        ceiling_helper("4", 4);
        ceiling_helper("-2", -2);
        ceiling_helper("0", 0);
        ceiling_helper("1", 1);
    }

    private static void bigIntegerValueExact_helper(@NotNull String r, int output) {
        aeq(read(r).get().bigIntegerValueExact(), output);
    }

    private static void bigIntegerValueExact_fail_helper(@NotNull String r) {
        try {
            read(r).get().bigIntegerValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBigIntegerValueExact() {
        bigIntegerValueExact_fail_helper("11/2");
        bigIntegerValueExact_fail_helper("5/2");
        bigIntegerValueExact_fail_helper("8/5");
        bigIntegerValueExact_fail_helper("11/10");
        bigIntegerValueExact_helper("1", 1);
        bigIntegerValueExact_helper("0", 0);
        bigIntegerValueExact_helper("-1", -1);
        bigIntegerValueExact_fail_helper("-11/10");
        bigIntegerValueExact_fail_helper("-8/5");
        bigIntegerValueExact_fail_helper("-5/2");
        bigIntegerValueExact_fail_helper("-11/2");
    }

    private static void byteValueExact_helper(@NotNull String r, @NotNull String output) {
        aeq(read(r).get().byteValueExact(), output);
    }

    private static void byteValueExact_fail_helper(@NotNull String r) {
        try {
            read(r).get().byteValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testByteValueExact() {
        byteValueExact_helper("1", "1");
        byteValueExact_helper("0", "0");
        byteValueExact_helper("-1", "-1");
        byteValueExact_helper("23", "23");
        byteValueExact_helper("8", "8");
        byteValueExact_fail_helper("11/2");
        byteValueExact_fail_helper("-8/5");
        byteValueExact_fail_helper("1000");
    }

    private static void shortValueExact_helper(@NotNull String r, @NotNull String output) {
        aeq(read(r).get().shortValueExact(), output);
    }

    private static void shortValueExact_fail_helper(@NotNull String r) {
        try {
            read(r).get().shortValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShortValueExact() {
        shortValueExact_helper("1", "1");
        shortValueExact_helper("0", "0");
        shortValueExact_helper("-1", "-1");
        shortValueExact_helper("23", "23");
        shortValueExact_helper("8", "8");
        shortValueExact_fail_helper("11/2");
        shortValueExact_fail_helper("-8/5");
        shortValueExact_fail_helper("100000");
    }

    private static void intValueExact_helper(@NotNull String r, @NotNull String output) {
        aeq(read(r).get().intValueExact(), output);
    }

    private static void intValueExact_fail_helper(@NotNull String r) {
        try {
            read(r).get().intValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIntValueExact() {
        intValueExact_helper("1", "1");
        intValueExact_helper("0", "0");
        intValueExact_helper("-1", "-1");
        intValueExact_helper("23", "23");
        intValueExact_helper("8", "8");
        intValueExact_fail_helper("11/2");
        intValueExact_fail_helper("-8/5");
        intValueExact_fail_helper("10000000000");
    }

    private static void longValueExact_helper(@NotNull String r, @NotNull String output) {
        aeq(read(r).get().longValueExact(), output);
    }

    private static void longValueExact_fail_helper(@NotNull String r) {
        try {
            read(r).get().longValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testLongValueExact() {
        longValueExact_helper("1", "1");
        longValueExact_helper("0", "0");
        longValueExact_helper("-1", "-1");
        longValueExact_helper("23", "23");
        longValueExact_helper("8", "8");
        longValueExact_fail_helper("11/2");
        longValueExact_fail_helper("-8/5");
        longValueExact_fail_helper("10000000000000000000");
    }

    private static void isPowerOfTwo_helper(@NotNull String input, boolean output) {
        aeq(read(input).get().isPowerOfTwo(), output);
    }

    private static void isPowerOfTwo_fail_helper(@NotNull String r) {
        try {
            read(r).get().isPowerOfTwo();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIsPowerOfTwo() {
        isPowerOfTwo_helper("1", true);
        isPowerOfTwo_helper("2", true);
        isPowerOfTwo_helper("256", true);
        isPowerOfTwo_helper("1/2", true);
        isPowerOfTwo_helper("1/256", true);
        isPowerOfTwo_helper("3", false);
        isPowerOfTwo_helper("1/3", false);
        isPowerOfTwo_fail_helper("0");
        isPowerOfTwo_fail_helper("-1");
        isPowerOfTwo_fail_helper("-1/2");
    }

    private static void isBinaryFraction_helper(@NotNull String input, boolean output) {
        aeq(read(input).get().isBinaryFraction(), output);
    }

    @Test
    public void testIsBinaryFraction() {
        isBinaryFraction_helper("0", true);
        isBinaryFraction_helper("1", true);
        isBinaryFraction_helper("2", true);
        isBinaryFraction_helper("3", true);
        isBinaryFraction_helper("-1", true);
        isBinaryFraction_helper("-2", true);
        isBinaryFraction_helper("-3", true);
        isBinaryFraction_helper("1/4", true);
        isBinaryFraction_helper("3/4", true);
        isBinaryFraction_helper("-1/4", true);
        isBinaryFraction_helper("-3/4", true);
        isBinaryFraction_helper("1/3", false);
        isBinaryFraction_helper("1/10", false);
        isBinaryFraction_helper("-1/3", false);
        isBinaryFraction_helper("-1/10", false);
    }

    private static void binaryFractionValueExact_helper(@NotNull String r, @NotNull String output) {
        aeq(read(r).get().binaryFractionValueExact(), output);
    }

    private static void binaryFractionValueExact_fail_helper(@NotNull String r) {
        try {
            read(r).get().binaryFractionValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBinaryFractionValueExact() {
        binaryFractionValueExact_helper("0", "0");
        binaryFractionValueExact_helper("1", "1");
        binaryFractionValueExact_helper("2", "1 << 1");
        binaryFractionValueExact_helper("3", "3");
        binaryFractionValueExact_helper("-1", "-1");
        binaryFractionValueExact_helper("-2", "-1 << 1");
        binaryFractionValueExact_helper("-3", "-3");
        binaryFractionValueExact_helper("1/4", "1 >> 2");
        binaryFractionValueExact_helper("3/4", "3 >> 2");
        binaryFractionValueExact_helper("-1/4", "-1 >> 2");
        binaryFractionValueExact_helper("-3/4", "-3 >> 2");
        binaryFractionValueExact_fail_helper("1/3");
        binaryFractionValueExact_fail_helper("1/10");
        binaryFractionValueExact_fail_helper("-1/3");
        binaryFractionValueExact_fail_helper("-1/10");
    }

    private static void binaryExponent_helper(@NotNull String input, int output) {
        aeq(read(input).get().binaryExponent(), output);
    }

    private static void binaryExponent_fail_helper(@NotNull String input) {
        try {
            read(input).get().binaryExponent();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testBinaryExponent() {
        binaryExponent_helper("1/3", -2);
        binaryExponent_helper("100", 6);
        binaryExponent_helper("127", 6);
        binaryExponent_helper("128", 7);
        binaryExponent_helper("129", 7);
        binaryExponent_helper("3/4", -1);
        binaryExponent_helper("1/127", -7);
        binaryExponent_helper("1/128", -7);
        binaryExponent_helper("1/129", -8);
        binaryExponent_helper("1", 0);
        binaryExponent_fail_helper("0");
        binaryExponent_fail_helper("-2/3");
    }

    private static void isEqualToFloat_helper(@NotNull Rational r, boolean output) {
        aeq(r.isEqualToFloat(), output);
    }

    private static void isEqualToFloat_helper(@NotNull String r, boolean output) {
        isEqualToFloat_helper(read(r).get(), output);
    }

    @Test
    public void testIsEqualToFloat() {
        isEqualToFloat_helper("0", true);
        isEqualToFloat_helper("1", true);
        isEqualToFloat_helper("1/2", true);
        isEqualToFloat_helper("-1/2", true);
        isEqualToFloat_helper(PI_FLOAT, true);
        isEqualToFloat_helper(SUBNORMAL_FLOAT, true);
        isEqualToFloat_helper(LARGEST_FLOAT, true);
        isEqualToFloat_helper(LARGEST_FLOAT.negate(), true);
        isEqualToFloat_helper(LARGEST_SUBNORMAL_FLOAT, true);
        isEqualToFloat_helper(LARGEST_SUBNORMAL_FLOAT.negate(), true);
        isEqualToFloat_helper(SMALLEST_FLOAT, true);
        isEqualToFloat_helper(SMALLEST_FLOAT.negate(), true);
        isEqualToFloat_helper(SMALLEST_NORMAL_FLOAT, true);
        isEqualToFloat_helper(SMALLEST_NORMAL_FLOAT.negate(), true);
        isEqualToFloat_helper("1/3", false);
        isEqualToFloat_helper("-1/3", false);
        isEqualToFloat_helper("1/10", false);
        isEqualToFloat_helper("-1/10", false);
        isEqualToFloat_helper(ALMOST_ONE, false);
        isEqualToFloat_helper(TRILLION, false);
        isEqualToFloat_helper(HALF_ABOVE_PI_FLOAT, false);
        isEqualToFloat_helper(HALF_BELOW_PI_FLOAT, false);
        isEqualToFloat_helper(HALF_ABOVE_SUBNORMAL_FLOAT, false);
        isEqualToFloat_helper(HALF_BELOW_SUBNORMAL_FLOAT, false);
        isEqualToFloat_helper(BELOW_NEGATIVE_MAX_FLOAT, false);
        isEqualToFloat_helper(HALF_ABOVE_NEGATIVE_MAX_FLOAT, false);
        isEqualToFloat_helper(ABOVE_MAX_FLOAT, false);
        isEqualToFloat_helper(HALF_BELOW_MAX_FLOAT, false);
        isEqualToFloat_helper(HALF_BELOW_ZERO_FLOAT, false);
        isEqualToFloat_helper(HALF_ABOVE_ZERO_FLOAT, false);
        isEqualToFloat_helper(BOUNDARY_FLOAT, false);
    }

    private static void isEqualToDouble_helper(@NotNull Rational r, boolean output) {
        aeq(r.isEqualToDouble(), output);
    }

    private static void isEqualToDouble_helper(@NotNull String r, boolean output) {
        isEqualToDouble_helper(read(r).get(), output);
    }

    @Test
    public void testIsEqualToDouble() {
        isEqualToDouble_helper("0", true);
        isEqualToDouble_helper("1", true);
        isEqualToDouble_helper("1/2", true);
        isEqualToDouble_helper("-1/2", true);
        isEqualToDouble_helper(PI_DOUBLE, true);
        isEqualToDouble_helper(SUBNORMAL_DOUBLE, true);
        isEqualToDouble_helper(LARGEST_DOUBLE, true);
        isEqualToDouble_helper(LARGEST_DOUBLE.negate(), true);
        isEqualToDouble_helper(LARGEST_SUBNORMAL_DOUBLE, true);
        isEqualToDouble_helper(LARGEST_SUBNORMAL_DOUBLE.negate(), true);
        isEqualToDouble_helper(SMALLEST_DOUBLE, true);
        isEqualToDouble_helper(SMALLEST_DOUBLE.negate(), true);
        isEqualToDouble_helper(SMALLEST_NORMAL_DOUBLE, true);
        isEqualToDouble_helper(SMALLEST_NORMAL_DOUBLE.negate(), true);
        isEqualToDouble_helper("1/3", false);
        isEqualToDouble_helper("-1/3", false);
        isEqualToDouble_helper("1/10", false);
        isEqualToDouble_helper("-1/10", false);
        isEqualToDouble_helper(ALMOST_ONE, false);
        isEqualToDouble_helper(GOOGOL, false);
        isEqualToDouble_helper(HALF_ABOVE_PI_DOUBLE, false);
        isEqualToDouble_helper(HALF_BELOW_PI_DOUBLE, false);
        isEqualToDouble_helper(HALF_ABOVE_SUBNORMAL_DOUBLE, false);
        isEqualToDouble_helper(HALF_BELOW_SUBNORMAL_DOUBLE, false);
        isEqualToDouble_helper(BELOW_NEGATIVE_MAX_DOUBLE, false);
        isEqualToDouble_helper(HALF_ABOVE_NEGATIVE_MAX_DOUBLE, false);
        isEqualToDouble_helper(ABOVE_MAX_DOUBLE, false);
        isEqualToDouble_helper(HALF_BELOW_MAX_DOUBLE, false);
        isEqualToDouble_helper(HALF_BELOW_ZERO_DOUBLE, false);
        isEqualToDouble_helper(HALF_ABOVE_ZERO_DOUBLE, false);
        isEqualToDouble_helper(BOUNDARY_DOUBLE, false);
    }

    private static void floatValue_RoundingMode_helper(
            @NotNull Rational r,
            @NotNull String roundingMode,
            float output
    ) {
        aeq(r.floatValue(Readers.readRoundingMode(roundingMode).get()), output);
    }

    private static void floatValue_RoundingMode_helper(@NotNull String r, @NotNull String roundingMode, float output) {
        floatValue_RoundingMode_helper(read(r).get(), roundingMode, output);
    }

    private static void floatValue_RoundingMode_fail_helper(@NotNull Rational r, @NotNull String roundingMode) {
        try {
            r.floatValue(Readers.readRoundingMode(roundingMode).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    private static void floatValue_RoundingMode_fail_helper(@NotNull String r, @NotNull String roundingMode) {
        floatValue_RoundingMode_fail_helper(read(r).get(), roundingMode);
    }

    @Test
    public void testFloatValue_RoundingMode() {
        floatValue_RoundingMode_helper("0", "FLOOR", 0.0f);
        floatValue_RoundingMode_helper("0", "CEILING", 0.0f);
        floatValue_RoundingMode_helper("0", "DOWN", 0.0f);
        floatValue_RoundingMode_helper("0", "UP", 0.0f);
        floatValue_RoundingMode_helper("0", "HALF_DOWN", 0.0f);
        floatValue_RoundingMode_helper("0", "HALF_UP", 0.0f);
        floatValue_RoundingMode_helper("0", "HALF_EVEN", 0.0f);
        floatValue_RoundingMode_helper("0", "UNNECESSARY", 0.0f);

        floatValue_RoundingMode_helper("1", "FLOOR", 1.0f);
        floatValue_RoundingMode_helper("1", "CEILING", 1.0f);
        floatValue_RoundingMode_helper("1", "DOWN", 1.0f);
        floatValue_RoundingMode_helper("1", "UP", 1.0f);
        floatValue_RoundingMode_helper("1", "HALF_DOWN", 1.0f);
        floatValue_RoundingMode_helper("1", "HALF_UP", 1.0f);
        floatValue_RoundingMode_helper("1", "HALF_EVEN", 1.0f);
        floatValue_RoundingMode_helper("1", "UNNECESSARY", 1.0f);

        floatValue_RoundingMode_helper("1/2", "FLOOR", 0.5f);
        floatValue_RoundingMode_helper("1/2", "CEILING", 0.5f);
        floatValue_RoundingMode_helper("1/2", "DOWN", 0.5f);
        floatValue_RoundingMode_helper("1/2", "UP", 0.5f);
        floatValue_RoundingMode_helper("1/2", "HALF_DOWN", 0.5f);
        floatValue_RoundingMode_helper("1/2", "HALF_UP", 0.5f);
        floatValue_RoundingMode_helper("1/2", "HALF_EVEN", 0.5f);
        floatValue_RoundingMode_helper("1/2", "UNNECESSARY", 0.5f);

        floatValue_RoundingMode_helper("1/3", "FLOOR", 0.3333333f);
        floatValue_RoundingMode_helper("1/3", "CEILING", 0.33333334f);
        floatValue_RoundingMode_helper("1/3", "DOWN", 0.3333333f);
        floatValue_RoundingMode_helper("1/3", "UP", 0.33333334f);
        floatValue_RoundingMode_helper("1/3", "HALF_DOWN", 0.33333334f);
        floatValue_RoundingMode_helper("1/3", "HALF_UP", 0.33333334f);
        floatValue_RoundingMode_helper("1/3", "HALF_EVEN", 0.33333334f);
        floatValue_RoundingMode_fail_helper("1/3", "UNNECESSARY");

        floatValue_RoundingMode_helper("-1/3", "FLOOR", -0.33333334f);
        floatValue_RoundingMode_helper("-1/3", "CEILING", -0.3333333f);
        floatValue_RoundingMode_helper("-1/3", "DOWN", -0.3333333f);
        floatValue_RoundingMode_helper("-1/3", "UP", -0.33333334f);
        floatValue_RoundingMode_helper("-1/3", "HALF_DOWN", -0.33333334f);
        floatValue_RoundingMode_helper("-1/3", "HALF_UP", -0.33333334f);
        floatValue_RoundingMode_helper("-1/3", "HALF_EVEN", -0.33333334f);
        floatValue_RoundingMode_fail_helper("-1/3", "UNNECESSARY");

        floatValue_RoundingMode_helper(ALMOST_ONE, "FLOOR", 0.99999994f);
        floatValue_RoundingMode_helper(ALMOST_ONE, "CEILING", 1.0f);
        floatValue_RoundingMode_helper(ALMOST_ONE, "DOWN", 0.99999994f);
        floatValue_RoundingMode_helper(ALMOST_ONE, "UP", 1.0f);
        floatValue_RoundingMode_helper(ALMOST_ONE, "HALF_DOWN", 1.0f);
        floatValue_RoundingMode_helper(ALMOST_ONE, "HALF_UP", 1.0f);
        floatValue_RoundingMode_helper(ALMOST_ONE, "HALF_EVEN", 1.0f);
        floatValue_RoundingMode_fail_helper(ALMOST_ONE, "UNNECESSARY");

        floatValue_RoundingMode_helper(TRILLION, "FLOOR", 1.0E12f);
        floatValue_RoundingMode_helper(TRILLION, "CEILING", 1.00000006E12f);
        floatValue_RoundingMode_helper(TRILLION, "DOWN", 1.0E12f);
        floatValue_RoundingMode_helper(TRILLION, "UP", 1.00000006E12f);
        floatValue_RoundingMode_helper(TRILLION, "HALF_DOWN", 1.0E12f);
        floatValue_RoundingMode_helper(TRILLION, "HALF_UP", 1.0E12f);
        floatValue_RoundingMode_helper(TRILLION, "HALF_EVEN", 1.0E12f);
        floatValue_RoundingMode_fail_helper(TRILLION, "UNNECESSARY");

        floatValue_RoundingMode_helper(PI_FLOAT, "FLOOR", 3.1415927f);
        floatValue_RoundingMode_helper(PI_FLOAT, "CEILING", 3.1415927f);
        floatValue_RoundingMode_helper(PI_FLOAT, "DOWN", 3.1415927f);
        floatValue_RoundingMode_helper(PI_FLOAT, "UP", 3.1415927f);
        floatValue_RoundingMode_helper(PI_FLOAT, "HALF_DOWN", 3.1415927f);
        floatValue_RoundingMode_helper(PI_FLOAT, "HALF_UP", 3.1415927f);
        floatValue_RoundingMode_helper(PI_FLOAT, "HALF_EVEN", 3.1415927f);
        floatValue_RoundingMode_helper(PI_FLOAT, "UNNECESSARY", 3.1415927f);

        floatValue_RoundingMode_helper(HALF_ABOVE_PI_FLOAT, "FLOOR", 3.1415927f);
        floatValue_RoundingMode_helper(HALF_ABOVE_PI_FLOAT, "CEILING", 3.141593f);
        floatValue_RoundingMode_helper(HALF_ABOVE_PI_FLOAT, "DOWN", 3.1415927f);
        floatValue_RoundingMode_helper(HALF_ABOVE_PI_FLOAT, "UP", 3.141593f);
        floatValue_RoundingMode_helper(HALF_ABOVE_PI_FLOAT, "HALF_DOWN", 3.1415927f);
        floatValue_RoundingMode_helper(HALF_ABOVE_PI_FLOAT, "HALF_UP", 3.141593f);
        floatValue_RoundingMode_helper(HALF_ABOVE_PI_FLOAT, "HALF_EVEN", 3.141593f);
        floatValue_RoundingMode_fail_helper(HALF_ABOVE_PI_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(HALF_BELOW_PI_FLOAT, "FLOOR", 3.1415925f);
        floatValue_RoundingMode_helper(HALF_BELOW_PI_FLOAT, "CEILING", 3.1415927f);
        floatValue_RoundingMode_helper(HALF_BELOW_PI_FLOAT, "DOWN", 3.1415925f);
        floatValue_RoundingMode_helper(HALF_BELOW_PI_FLOAT, "UP", 3.1415927f);
        floatValue_RoundingMode_helper(HALF_BELOW_PI_FLOAT, "HALF_DOWN", 3.1415925f);
        floatValue_RoundingMode_helper(HALF_BELOW_PI_FLOAT, "HALF_UP", 3.1415927f);
        floatValue_RoundingMode_helper(HALF_BELOW_PI_FLOAT, "HALF_EVEN", 3.1415925f);
        floatValue_RoundingMode_fail_helper(HALF_BELOW_PI_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(JUST_ABOVE_PI_FLOAT, "FLOOR", 3.1415927f);
        floatValue_RoundingMode_helper(JUST_ABOVE_PI_FLOAT, "CEILING", 3.141593f);
        floatValue_RoundingMode_helper(JUST_ABOVE_PI_FLOAT, "DOWN", 3.1415927f);
        floatValue_RoundingMode_helper(JUST_ABOVE_PI_FLOAT, "UP", 3.141593f);
        floatValue_RoundingMode_helper(JUST_ABOVE_PI_FLOAT, "HALF_DOWN", 3.1415927f);
        floatValue_RoundingMode_helper(JUST_ABOVE_PI_FLOAT, "HALF_UP", 3.1415927f);
        floatValue_RoundingMode_helper(JUST_ABOVE_PI_FLOAT, "HALF_EVEN", 3.1415927f);
        floatValue_RoundingMode_fail_helper(JUST_ABOVE_PI_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(JUST_BELOW_PI_FLOAT, "FLOOR", 3.1415925f);
        floatValue_RoundingMode_helper(JUST_BELOW_PI_FLOAT, "CEILING", 3.1415927f);
        floatValue_RoundingMode_helper(JUST_BELOW_PI_FLOAT, "DOWN", 3.1415925f);
        floatValue_RoundingMode_helper(JUST_BELOW_PI_FLOAT, "UP", 3.1415927f);
        floatValue_RoundingMode_helper(JUST_BELOW_PI_FLOAT, "HALF_DOWN", 3.1415927f);
        floatValue_RoundingMode_helper(JUST_BELOW_PI_FLOAT, "HALF_UP", 3.1415927f);
        floatValue_RoundingMode_helper(JUST_BELOW_PI_FLOAT, "HALF_EVEN", 3.1415927f);
        floatValue_RoundingMode_fail_helper(JUST_BELOW_PI_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(SUBNORMAL_FLOAT, "FLOOR", 1.0E-40f);
        floatValue_RoundingMode_helper(SUBNORMAL_FLOAT, "CEILING", 1.0E-40f);
        floatValue_RoundingMode_helper(SUBNORMAL_FLOAT, "DOWN", 1.0E-40f);
        floatValue_RoundingMode_helper(SUBNORMAL_FLOAT, "UP", 1.0E-40f);
        floatValue_RoundingMode_helper(SUBNORMAL_FLOAT, "HALF_DOWN", 1.0E-40f);
        floatValue_RoundingMode_helper(SUBNORMAL_FLOAT, "HALF_UP", 1.0E-40f);
        floatValue_RoundingMode_helper(SUBNORMAL_FLOAT, "HALF_EVEN", 1.0E-40f);
        floatValue_RoundingMode_helper(SUBNORMAL_FLOAT, "UNNECESSARY", 1.0E-40f);

        floatValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_FLOAT, "FLOOR", 1.0E-40f);
        floatValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_FLOAT, "CEILING", 1.00001E-40f);
        floatValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_FLOAT, "DOWN", 1.0E-40f);
        floatValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_FLOAT, "UP", 1.00001E-40f);
        floatValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_FLOAT, "HALF_DOWN", 1.0E-40f);
        floatValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_FLOAT, "HALF_UP", 1.00001E-40f);
        floatValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_FLOAT, "HALF_EVEN", 1.0E-40f);
        floatValue_RoundingMode_fail_helper(HALF_ABOVE_SUBNORMAL_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_FLOAT, "FLOOR", 9.9998E-41f);
        floatValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_FLOAT, "CEILING", 1.0E-40f);
        floatValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_FLOAT, "DOWN", 9.9998E-41f);
        floatValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_FLOAT, "UP", 1.0E-40f);
        floatValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_FLOAT, "HALF_DOWN", 9.9998E-41f);
        floatValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_FLOAT, "HALF_UP", 1.0E-40f);
        floatValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_FLOAT, "HALF_EVEN", 1.0E-40f);
        floatValue_RoundingMode_fail_helper(HALF_BELOW_SUBNORMAL_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_FLOAT, "FLOOR", 1.0E-40f);
        floatValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_FLOAT, "CEILING", 1.00001E-40f);
        floatValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_FLOAT, "DOWN", 1.0E-40f);
        floatValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_FLOAT, "UP", 1.00001E-40f);
        floatValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_FLOAT, "HALF_DOWN", 1.0E-40f);
        floatValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_FLOAT, "HALF_UP", 1.0E-40f);
        floatValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_FLOAT, "HALF_EVEN", 1.0E-40f);
        floatValue_RoundingMode_fail_helper(JUST_ABOVE_SUBNORMAL_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_FLOAT, "FLOOR", 9.9998E-41f);
        floatValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_FLOAT, "CEILING", 1.0E-40f);
        floatValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_FLOAT, "DOWN", 9.9998E-41f);
        floatValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_FLOAT, "UP", 1.0E-40f);
        floatValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_FLOAT, "HALF_DOWN", 1.0E-40f);
        floatValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_FLOAT, "HALF_UP", 1.0E-40f);
        floatValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_FLOAT, "HALF_EVEN", 1.0E-40f);
        floatValue_RoundingMode_fail_helper(JUST_BELOW_SUBNORMAL_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_FLOAT, "FLOOR", Float.NEGATIVE_INFINITY);
        floatValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_FLOAT, "CEILING", -3.4028235E38f);
        floatValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_FLOAT, "DOWN", -3.4028235E38f);
        floatValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_FLOAT, "UP", Float.NEGATIVE_INFINITY);
        floatValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_FLOAT, "HALF_DOWN", -3.4028235E38f);
        floatValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_FLOAT, "HALF_UP", Float.NEGATIVE_INFINITY);
        floatValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_FLOAT, "HALF_EVEN", Float.NEGATIVE_INFINITY);
        floatValue_RoundingMode_fail_helper(BELOW_NEGATIVE_MAX_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_FLOAT, "FLOOR", -3.4028235E38f);
        floatValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_FLOAT, "CEILING", -3.4028233E38f);
        floatValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_FLOAT, "DOWN", -3.4028233E38f);
        floatValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_FLOAT, "UP", -3.4028235E38f);
        floatValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_FLOAT, "HALF_DOWN", -3.4028233E38f);
        floatValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_FLOAT, "HALF_UP", -3.4028235E38f);
        floatValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_FLOAT, "HALF_EVEN", -3.4028233E38f);
        floatValue_RoundingMode_fail_helper(HALF_ABOVE_NEGATIVE_MAX_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_FLOAT, "FLOOR", -3.4028235E38f);
        floatValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_FLOAT, "CEILING", -3.4028233E38f);
        floatValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_FLOAT, "DOWN", -3.4028233E38f);
        floatValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_FLOAT, "UP", -3.4028235E38f);
        floatValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_FLOAT, "HALF_DOWN", -3.4028235E38f);
        floatValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_FLOAT, "HALF_UP", -3.4028235E38f);
        floatValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_FLOAT, "HALF_EVEN", -3.4028235E38f);
        floatValue_RoundingMode_fail_helper(JUST_ABOVE_NEGATIVE_MAX_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(ABOVE_MAX_FLOAT, "FLOOR", 3.4028235E38f);
        floatValue_RoundingMode_helper(ABOVE_MAX_FLOAT, "CEILING", Float.POSITIVE_INFINITY);
        floatValue_RoundingMode_helper(ABOVE_MAX_FLOAT, "DOWN", 3.4028235E38f);
        floatValue_RoundingMode_helper(ABOVE_MAX_FLOAT, "UP", Float.POSITIVE_INFINITY);
        floatValue_RoundingMode_helper(ABOVE_MAX_FLOAT, "HALF_DOWN", 3.4028235E38f);
        floatValue_RoundingMode_helper(ABOVE_MAX_FLOAT, "HALF_UP", Float.POSITIVE_INFINITY);
        floatValue_RoundingMode_helper(ABOVE_MAX_FLOAT, "HALF_EVEN", Float.POSITIVE_INFINITY);
        floatValue_RoundingMode_fail_helper(ABOVE_MAX_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(HALF_BELOW_MAX_FLOAT, "FLOOR", 3.4028233E38f);
        floatValue_RoundingMode_helper(HALF_BELOW_MAX_FLOAT, "CEILING", 3.4028235E38f);
        floatValue_RoundingMode_helper(HALF_BELOW_MAX_FLOAT, "DOWN", 3.4028233E38f);
        floatValue_RoundingMode_helper(HALF_BELOW_MAX_FLOAT, "UP", 3.4028235E38f);
        floatValue_RoundingMode_helper(HALF_BELOW_MAX_FLOAT, "HALF_DOWN", 3.4028233E38f);
        floatValue_RoundingMode_helper(HALF_BELOW_MAX_FLOAT, "HALF_UP", 3.4028235E38f);
        floatValue_RoundingMode_helper(HALF_BELOW_MAX_FLOAT, "HALF_EVEN", 3.4028233E38f);
        floatValue_RoundingMode_fail_helper(HALF_BELOW_MAX_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(JUST_BELOW_MAX_FLOAT, "FLOOR", 3.4028233E38f);
        floatValue_RoundingMode_helper(JUST_BELOW_MAX_FLOAT, "CEILING", 3.4028235E38f);
        floatValue_RoundingMode_helper(JUST_BELOW_MAX_FLOAT, "DOWN", 3.4028233E38f);
        floatValue_RoundingMode_helper(JUST_BELOW_MAX_FLOAT, "UP", 3.4028235E38f);
        floatValue_RoundingMode_helper(JUST_BELOW_MAX_FLOAT, "HALF_DOWN", 3.4028235E38f);
        floatValue_RoundingMode_helper(JUST_BELOW_MAX_FLOAT, "HALF_UP", 3.4028235E38f);
        floatValue_RoundingMode_helper(JUST_BELOW_MAX_FLOAT, "HALF_EVEN", 3.4028235E38f);
        floatValue_RoundingMode_fail_helper(JUST_BELOW_MAX_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(HALF_BELOW_ZERO_FLOAT, "FLOOR", -1.4E-45f);
        floatValue_RoundingMode_helper(HALF_BELOW_ZERO_FLOAT, "CEILING", -0.0f);
        floatValue_RoundingMode_helper(HALF_BELOW_ZERO_FLOAT, "DOWN", -0.0f);
        floatValue_RoundingMode_helper(HALF_BELOW_ZERO_FLOAT, "UP", -1.4E-45f);
        floatValue_RoundingMode_helper(HALF_BELOW_ZERO_FLOAT, "HALF_DOWN", -0.0f);
        floatValue_RoundingMode_helper(HALF_BELOW_ZERO_FLOAT, "HALF_UP", -1.4E-45f);
        floatValue_RoundingMode_helper(HALF_BELOW_ZERO_FLOAT, "HALF_EVEN", -0.0f);
        floatValue_RoundingMode_fail_helper(HALF_BELOW_ZERO_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(JUST_BELOW_ZERO_FLOAT, "FLOOR", -1.4E-45f);
        floatValue_RoundingMode_helper(JUST_BELOW_ZERO_FLOAT, "CEILING", -0.0f);
        floatValue_RoundingMode_helper(JUST_BELOW_ZERO_FLOAT, "DOWN", -0.0f);
        floatValue_RoundingMode_helper(JUST_BELOW_ZERO_FLOAT, "UP", -1.4E-45f);
        floatValue_RoundingMode_helper(JUST_BELOW_ZERO_FLOAT, "HALF_DOWN", -0.0f);
        floatValue_RoundingMode_helper(JUST_BELOW_ZERO_FLOAT, "HALF_UP", -0.0f);
        floatValue_RoundingMode_helper(JUST_BELOW_ZERO_FLOAT, "HALF_EVEN", -0.0f);
        floatValue_RoundingMode_fail_helper(JUST_BELOW_ZERO_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(HALF_ABOVE_ZERO_FLOAT, "FLOOR", 0.0f);
        floatValue_RoundingMode_helper(HALF_ABOVE_ZERO_FLOAT, "CEILING", 1.4E-45f);
        floatValue_RoundingMode_helper(HALF_ABOVE_ZERO_FLOAT, "DOWN", 0.0f);
        floatValue_RoundingMode_helper(HALF_ABOVE_ZERO_FLOAT, "UP", 1.4E-45f);
        floatValue_RoundingMode_helper(HALF_ABOVE_ZERO_FLOAT, "HALF_DOWN", 0.0f);
        floatValue_RoundingMode_helper(HALF_ABOVE_ZERO_FLOAT, "HALF_UP", 1.4E-45f);
        floatValue_RoundingMode_helper(HALF_ABOVE_ZERO_FLOAT, "HALF_EVEN", 0.0f);
        floatValue_RoundingMode_fail_helper(HALF_ABOVE_ZERO_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(JUST_ABOVE_ZERO_FLOAT, "FLOOR", 0.0f);
        floatValue_RoundingMode_helper(JUST_ABOVE_ZERO_FLOAT, "CEILING", 1.4E-45f);
        floatValue_RoundingMode_helper(JUST_ABOVE_ZERO_FLOAT, "DOWN", 0.0f);
        floatValue_RoundingMode_helper(JUST_ABOVE_ZERO_FLOAT, "UP", 1.4E-45f);
        floatValue_RoundingMode_helper(JUST_ABOVE_ZERO_FLOAT, "HALF_DOWN", 0.0f);
        floatValue_RoundingMode_helper(JUST_ABOVE_ZERO_FLOAT, "HALF_UP", 0.0f);
        floatValue_RoundingMode_helper(JUST_ABOVE_ZERO_FLOAT, "HALF_EVEN", 0.0f);
        floatValue_RoundingMode_fail_helper(JUST_ABOVE_ZERO_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(BOUNDARY_FLOAT, "FLOOR", 1.1754942E-38f);
        floatValue_RoundingMode_helper(BOUNDARY_FLOAT, "CEILING", 1.17549435E-38f);
        floatValue_RoundingMode_helper(BOUNDARY_FLOAT, "DOWN", 1.1754942E-38f);
        floatValue_RoundingMode_helper(BOUNDARY_FLOAT, "UP", 1.17549435E-38f);
        floatValue_RoundingMode_helper(BOUNDARY_FLOAT, "HALF_DOWN", 1.1754942E-38f);
        floatValue_RoundingMode_helper(BOUNDARY_FLOAT, "HALF_UP", 1.17549435E-38f);
        floatValue_RoundingMode_helper(BOUNDARY_FLOAT, "HALF_EVEN", 1.17549435E-38f);
        floatValue_RoundingMode_fail_helper(BOUNDARY_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_FLOAT, "FLOOR", 1.1754942E-38f);
        floatValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_FLOAT, "CEILING", 1.17549435E-38f);
        floatValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_FLOAT, "DOWN", 1.1754942E-38f);
        floatValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_FLOAT, "UP", 1.17549435E-38f);
        floatValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_FLOAT, "HALF_DOWN", 1.1754942E-38f);
        floatValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_FLOAT, "HALF_UP", 1.1754942E-38f);
        floatValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_FLOAT, "HALF_EVEN", 1.1754942E-38f);
        floatValue_RoundingMode_fail_helper(JUST_BELOW_BOUNDARY_FLOAT, "UNNECESSARY");

        floatValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_FLOAT, "FLOOR", 1.1754942E-38f);
        floatValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_FLOAT, "CEILING", 1.17549435E-38f);
        floatValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_FLOAT, "DOWN", 1.1754942E-38f);
        floatValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_FLOAT, "UP", 1.17549435E-38f);
        floatValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_FLOAT, "HALF_DOWN", 1.17549435E-38f);
        floatValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_FLOAT, "HALF_UP", 1.17549435E-38f);
        floatValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_FLOAT, "HALF_EVEN", 1.17549435E-38f);
        floatValue_RoundingMode_fail_helper(JUST_ABOVE_BOUNDARY_FLOAT, "UNNECESSARY");
    }

    private static void floatValue_helper(@NotNull Rational r, float output) {
        aeq(r.floatValue(), output);
    }

    private static void floatValue_helper(@NotNull String r, float output) {
        floatValue_helper(read(r).get(), output);
    }

    @Test
    public void testFloatValue() {
        floatValue_helper("0", 0.0f);
        floatValue_helper("1", 1.0f);
        floatValue_helper("1/2", 0.5f);
        floatValue_helper("1/3", 0.33333334f);
        floatValue_helper("-1/3", -0.33333334f);
        floatValue_helper(ALMOST_ONE, 1.0f);
        floatValue_helper(TRILLION, 1.0E12f);
        floatValue_helper(PI_FLOAT, 3.1415927f);
        floatValue_helper(HALF_ABOVE_PI_FLOAT, 3.141593f);
        floatValue_helper(HALF_BELOW_PI_FLOAT, 3.1415925f);
        floatValue_helper(JUST_ABOVE_PI_FLOAT, 3.1415927f);
        floatValue_helper(JUST_BELOW_PI_FLOAT, 3.1415927f);
        floatValue_helper(SUBNORMAL_FLOAT, 1.0E-40f);
        floatValue_helper(HALF_ABOVE_SUBNORMAL_FLOAT, 1.0E-40f);
        floatValue_helper(HALF_BELOW_SUBNORMAL_FLOAT, 1.0E-40f);
        floatValue_helper(JUST_ABOVE_SUBNORMAL_FLOAT, 1.0E-40f);
        floatValue_helper(JUST_BELOW_SUBNORMAL_FLOAT, 1.0E-40f);
        floatValue_helper(BELOW_NEGATIVE_MAX_FLOAT, Float.NEGATIVE_INFINITY);
        floatValue_helper(HALF_ABOVE_NEGATIVE_MAX_FLOAT, -3.4028233E38f);
        floatValue_helper(JUST_ABOVE_NEGATIVE_MAX_FLOAT, -3.4028235E38f);
        floatValue_helper(ABOVE_MAX_FLOAT, Float.POSITIVE_INFINITY);
        floatValue_helper(HALF_BELOW_MAX_FLOAT, 3.4028233E38f);
        floatValue_helper(JUST_BELOW_MAX_FLOAT, 3.4028235E38f);
        floatValue_helper(HALF_BELOW_ZERO_FLOAT, -0.0f);
        floatValue_helper(JUST_BELOW_ZERO_FLOAT, -0.0f);
        floatValue_helper(HALF_ABOVE_ZERO_FLOAT, 0.0f);
        floatValue_helper(JUST_ABOVE_ZERO_FLOAT, 0.0f);
        floatValue_helper(BOUNDARY_FLOAT, 1.17549435E-38f);
        floatValue_helper(JUST_BELOW_BOUNDARY_FLOAT, 1.1754942E-38f);
        floatValue_helper(JUST_ABOVE_BOUNDARY_FLOAT, 1.17549435E-38f);
    }

    private static void floatValueExact_helper(@NotNull Rational r, float output) {
        aeq(r.floatValueExact(), output);
    }

    private static void floatValueExact_helper(@NotNull String r, float output) {
        floatValueExact_helper(read(r).get(), output);
    }

    private static void floatValueExact_fail_helper(@NotNull Rational r) {
        try {
            r.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    private static void floatValueExact_fail_helper(@NotNull String r) {
        floatValueExact_fail_helper(read(r).get());
    }

    @Test
    public void testFloatValueExact() {
        floatValueExact_helper("0", 0.0f);
        floatValueExact_helper("1", 1.0f);
        floatValueExact_helper("1/2", 0.5f);
        floatValueExact_fail_helper("1/3");
        floatValueExact_fail_helper("-1/3");
        floatValueExact_fail_helper(ALMOST_ONE);
        floatValueExact_fail_helper(TRILLION);
        floatValueExact_helper(PI_FLOAT, 3.1415927f);
        floatValueExact_fail_helper(HALF_ABOVE_PI_FLOAT);
        floatValueExact_fail_helper(HALF_BELOW_PI_FLOAT);
        floatValueExact_fail_helper(JUST_ABOVE_PI_FLOAT);
        floatValueExact_fail_helper(JUST_BELOW_PI_FLOAT);
        floatValueExact_helper(SUBNORMAL_FLOAT, 1.0E-40f);
        floatValueExact_fail_helper(HALF_ABOVE_SUBNORMAL_FLOAT);
        floatValueExact_fail_helper(HALF_BELOW_SUBNORMAL_FLOAT);
        floatValueExact_fail_helper(JUST_ABOVE_SUBNORMAL_FLOAT);
        floatValueExact_fail_helper(JUST_BELOW_SUBNORMAL_FLOAT);
        floatValueExact_fail_helper(BELOW_NEGATIVE_MAX_FLOAT);
        floatValueExact_fail_helper(HALF_ABOVE_NEGATIVE_MAX_FLOAT);
        floatValueExact_fail_helper(JUST_ABOVE_NEGATIVE_MAX_FLOAT);
        floatValueExact_fail_helper(ABOVE_MAX_FLOAT);
        floatValueExact_fail_helper(HALF_BELOW_MAX_FLOAT);
        floatValueExact_fail_helper(JUST_BELOW_MAX_FLOAT);
        floatValueExact_fail_helper(HALF_BELOW_ZERO_FLOAT);
        floatValueExact_fail_helper(JUST_BELOW_ZERO_FLOAT);
        floatValueExact_fail_helper(HALF_ABOVE_ZERO_FLOAT);
        floatValueExact_fail_helper(JUST_ABOVE_ZERO_FLOAT);
        floatValueExact_fail_helper(BOUNDARY_FLOAT);
        floatValueExact_fail_helper(JUST_BELOW_BOUNDARY_FLOAT);
        floatValueExact_fail_helper(JUST_ABOVE_BOUNDARY_FLOAT);
    }

    private static void doubleValue_RoundingMode_helper(
            @NotNull Rational r,
            @NotNull String roundingMode,
            double output
    ) {
        aeq(r.doubleValue(Readers.readRoundingMode(roundingMode).get()), output);
    }

    private static void doubleValue_RoundingMode_helper(
            @NotNull String r,
            @NotNull String roundingMode,
            double output
    ) {
        doubleValue_RoundingMode_helper(read(r).get(), roundingMode, output);
    }

    private static void doubleValue_RoundingMode_fail_helper(@NotNull Rational r, @NotNull String roundingMode) {
        try {
            r.doubleValue(Readers.readRoundingMode(roundingMode).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    private static void doubleValue_RoundingMode_fail_helper(@NotNull String r, @NotNull String roundingMode) {
        doubleValue_RoundingMode_fail_helper(read(r).get(), roundingMode);
    }

    @Test
    public void testDoubleValue_RoundingMode() {
        doubleValue_RoundingMode_helper("0", "FLOOR", 0.0);
        doubleValue_RoundingMode_helper("0", "CEILING", 0.0);
        doubleValue_RoundingMode_helper("0", "DOWN", 0.0);
        doubleValue_RoundingMode_helper("0", "UP", 0.0);
        doubleValue_RoundingMode_helper("0", "HALF_DOWN", 0.0);
        doubleValue_RoundingMode_helper("0", "HALF_UP", 0.0);
        doubleValue_RoundingMode_helper("0", "HALF_EVEN", 0.0);
        doubleValue_RoundingMode_helper("0", "UNNECESSARY", 0.0);

        doubleValue_RoundingMode_helper("1", "FLOOR", 1.0);
        doubleValue_RoundingMode_helper("1", "CEILING", 1.0);
        doubleValue_RoundingMode_helper("1", "DOWN", 1.0);
        doubleValue_RoundingMode_helper("1", "UP", 1.0);
        doubleValue_RoundingMode_helper("1", "HALF_DOWN", 1.0);
        doubleValue_RoundingMode_helper("1", "HALF_UP", 1.0);
        doubleValue_RoundingMode_helper("1", "HALF_EVEN", 1.0);
        doubleValue_RoundingMode_helper("1", "UNNECESSARY", 1.0);

        doubleValue_RoundingMode_helper("1/2", "FLOOR", 0.5);
        doubleValue_RoundingMode_helper("1/2", "CEILING", 0.5);
        doubleValue_RoundingMode_helper("1/2", "DOWN", 0.5);
        doubleValue_RoundingMode_helper("1/2", "UP", 0.5);
        doubleValue_RoundingMode_helper("1/2", "HALF_DOWN", 0.5);
        doubleValue_RoundingMode_helper("1/2", "HALF_UP", 0.5);
        doubleValue_RoundingMode_helper("1/2", "HALF_EVEN", 0.5);
        doubleValue_RoundingMode_helper("1/2", "UNNECESSARY", 0.5);

        doubleValue_RoundingMode_helper("1/3", "FLOOR", 0.3333333333333333);
        doubleValue_RoundingMode_helper("1/3", "CEILING", 0.33333333333333337);
        doubleValue_RoundingMode_helper("1/3", "DOWN", 0.3333333333333333);
        doubleValue_RoundingMode_helper("1/3", "UP", 0.33333333333333337);
        doubleValue_RoundingMode_helper("1/3", "HALF_DOWN", 0.3333333333333333);
        doubleValue_RoundingMode_helper("1/3", "HALF_UP", 0.3333333333333333);
        doubleValue_RoundingMode_helper("1/3", "HALF_EVEN", 0.3333333333333333);
        doubleValue_RoundingMode_fail_helper("1/3", "UNNECESSARY");

        doubleValue_RoundingMode_helper("-1/3", "FLOOR", -0.33333333333333337);
        doubleValue_RoundingMode_helper("-1/3", "CEILING", -0.3333333333333333);
        doubleValue_RoundingMode_helper("-1/3", "DOWN", -0.3333333333333333);
        doubleValue_RoundingMode_helper("-1/3", "UP", -0.33333333333333337);
        doubleValue_RoundingMode_helper("-1/3", "HALF_DOWN", -0.3333333333333333);
        doubleValue_RoundingMode_helper("-1/3", "HALF_UP", -0.3333333333333333);
        doubleValue_RoundingMode_helper("-1/3", "HALF_EVEN", -0.3333333333333333);
        doubleValue_RoundingMode_fail_helper("-1/3", "UNNECESSARY");

        doubleValue_RoundingMode_helper(ALMOST_ONE, "FLOOR", 0.9999999999999999);
        doubleValue_RoundingMode_helper(ALMOST_ONE, "CEILING", 1.0);
        doubleValue_RoundingMode_helper(ALMOST_ONE, "DOWN", 0.9999999999999999);
        doubleValue_RoundingMode_helper(ALMOST_ONE, "UP", 1.0);
        doubleValue_RoundingMode_helper(ALMOST_ONE, "HALF_DOWN", 1.0);
        doubleValue_RoundingMode_helper(ALMOST_ONE, "HALF_UP", 1.0);
        doubleValue_RoundingMode_helper(ALMOST_ONE, "HALF_EVEN", 1.0);
        doubleValue_RoundingMode_fail_helper(ALMOST_ONE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(GOOGOL, "FLOOR", 9.999999999999998E99);
        doubleValue_RoundingMode_helper(GOOGOL, "CEILING", 1.0E100);
        doubleValue_RoundingMode_helper(GOOGOL, "DOWN", 9.999999999999998E99);
        doubleValue_RoundingMode_helper(GOOGOL, "UP", 1.0E100);
        doubleValue_RoundingMode_helper(GOOGOL, "HALF_DOWN", 1.0E100);
        doubleValue_RoundingMode_helper(GOOGOL, "HALF_UP", 1.0E100);
        doubleValue_RoundingMode_helper(GOOGOL, "HALF_EVEN", 1.0E100);
        doubleValue_RoundingMode_fail_helper(GOOGOL, "UNNECESSARY");

        doubleValue_RoundingMode_helper(PI_DOUBLE, "FLOOR", 3.141592653589793);
        doubleValue_RoundingMode_helper(PI_DOUBLE, "CEILING", 3.141592653589793);
        doubleValue_RoundingMode_helper(PI_DOUBLE, "DOWN", 3.141592653589793);
        doubleValue_RoundingMode_helper(PI_DOUBLE, "UP", 3.141592653589793);
        doubleValue_RoundingMode_helper(PI_DOUBLE, "HALF_DOWN",3.141592653589793);
        doubleValue_RoundingMode_helper(PI_DOUBLE, "HALF_UP", 3.141592653589793);
        doubleValue_RoundingMode_helper(PI_DOUBLE, "HALF_EVEN", 3.141592653589793);
        doubleValue_RoundingMode_helper(PI_DOUBLE, "UNNECESSARY", 3.141592653589793);

        doubleValue_RoundingMode_helper(HALF_ABOVE_PI_DOUBLE, "FLOOR", 3.141592653589793);
        doubleValue_RoundingMode_helper(HALF_ABOVE_PI_DOUBLE, "CEILING", 3.1415926535897936);
        doubleValue_RoundingMode_helper(HALF_ABOVE_PI_DOUBLE, "DOWN", 3.141592653589793);
        doubleValue_RoundingMode_helper(HALF_ABOVE_PI_DOUBLE, "UP", 3.1415926535897936);
        doubleValue_RoundingMode_helper(HALF_ABOVE_PI_DOUBLE, "HALF_DOWN", 3.141592653589793);
        doubleValue_RoundingMode_helper(HALF_ABOVE_PI_DOUBLE, "HALF_UP", 3.1415926535897936);
        doubleValue_RoundingMode_helper(HALF_ABOVE_PI_DOUBLE, "HALF_EVEN", 3.141592653589793);
        doubleValue_RoundingMode_fail_helper(HALF_ABOVE_PI_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(HALF_BELOW_PI_DOUBLE, "FLOOR", 3.1415926535897927);
        doubleValue_RoundingMode_helper(HALF_BELOW_PI_DOUBLE, "CEILING", 3.141592653589793);
        doubleValue_RoundingMode_helper(HALF_BELOW_PI_DOUBLE, "DOWN", 3.1415926535897927);
        doubleValue_RoundingMode_helper(HALF_BELOW_PI_DOUBLE, "UP", 3.141592653589793);
        doubleValue_RoundingMode_helper(HALF_BELOW_PI_DOUBLE, "HALF_DOWN", 3.1415926535897927);
        doubleValue_RoundingMode_helper(HALF_BELOW_PI_DOUBLE, "HALF_UP", 3.141592653589793);
        doubleValue_RoundingMode_helper(HALF_BELOW_PI_DOUBLE, "HALF_EVEN", 3.141592653589793);
        doubleValue_RoundingMode_fail_helper(HALF_BELOW_PI_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(JUST_ABOVE_PI_DOUBLE, "FLOOR", 3.141592653589793);
        doubleValue_RoundingMode_helper(JUST_ABOVE_PI_DOUBLE, "CEILING", 3.1415926535897936);
        doubleValue_RoundingMode_helper(JUST_ABOVE_PI_DOUBLE, "DOWN", 3.141592653589793);
        doubleValue_RoundingMode_helper(JUST_ABOVE_PI_DOUBLE, "UP", 3.1415926535897936);
        doubleValue_RoundingMode_helper(JUST_ABOVE_PI_DOUBLE, "HALF_DOWN", 3.141592653589793);
        doubleValue_RoundingMode_helper(JUST_ABOVE_PI_DOUBLE, "HALF_UP", 3.141592653589793);
        doubleValue_RoundingMode_helper(JUST_ABOVE_PI_DOUBLE, "HALF_EVEN", 3.141592653589793);
        doubleValue_RoundingMode_fail_helper(JUST_ABOVE_PI_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(JUST_BELOW_PI_DOUBLE, "FLOOR", 3.1415926535897927);
        doubleValue_RoundingMode_helper(JUST_BELOW_PI_DOUBLE, "CEILING", 3.141592653589793);
        doubleValue_RoundingMode_helper(JUST_BELOW_PI_DOUBLE, "DOWN", 3.1415926535897927);
        doubleValue_RoundingMode_helper(JUST_BELOW_PI_DOUBLE, "UP", 3.141592653589793);
        doubleValue_RoundingMode_helper(JUST_BELOW_PI_DOUBLE, "HALF_DOWN", 3.141592653589793);
        doubleValue_RoundingMode_helper(JUST_BELOW_PI_DOUBLE, "HALF_UP", 3.141592653589793);
        doubleValue_RoundingMode_helper(JUST_BELOW_PI_DOUBLE, "HALF_EVEN", 3.141592653589793);
        doubleValue_RoundingMode_fail_helper(JUST_BELOW_PI_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(SUBNORMAL_DOUBLE, "FLOOR", 1.0E-310);
        doubleValue_RoundingMode_helper(SUBNORMAL_DOUBLE, "CEILING", 1.0E-310);
        doubleValue_RoundingMode_helper(SUBNORMAL_DOUBLE, "DOWN", 1.0E-310);
        doubleValue_RoundingMode_helper(SUBNORMAL_DOUBLE, "UP", 1.0E-310);
        doubleValue_RoundingMode_helper(SUBNORMAL_DOUBLE, "HALF_DOWN", 1.0E-310);
        doubleValue_RoundingMode_helper(SUBNORMAL_DOUBLE, "HALF_UP", 1.0E-310);
        doubleValue_RoundingMode_helper(SUBNORMAL_DOUBLE, "HALF_EVEN", 1.0E-310);
        doubleValue_RoundingMode_helper(SUBNORMAL_DOUBLE, "UNNECESSARY", 1.0E-310);

        doubleValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_DOUBLE, "FLOOR", 1.0E-310);
        doubleValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_DOUBLE, "CEILING", 1.00000000000005E-310);
        doubleValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_DOUBLE, "DOWN", 1.0E-310);
        doubleValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_DOUBLE, "UP", 1.00000000000005E-310);
        doubleValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_DOUBLE, "HALF_DOWN", 1.0E-310);
        doubleValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_DOUBLE, "HALF_UP", 1.00000000000005E-310);
        doubleValue_RoundingMode_helper(HALF_ABOVE_SUBNORMAL_DOUBLE, "HALF_EVEN", 1.00000000000005E-310);
        doubleValue_RoundingMode_fail_helper(HALF_ABOVE_SUBNORMAL_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_DOUBLE, "FLOOR", 9.9999999999995E-311);
        doubleValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_DOUBLE, "CEILING", 1.0E-310);
        doubleValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_DOUBLE, "DOWN", 9.9999999999995E-311);
        doubleValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_DOUBLE, "UP", 1.0E-310);
        doubleValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_DOUBLE, "HALF_DOWN", 9.9999999999995E-311);
        doubleValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_DOUBLE, "HALF_UP", 1.0E-310);
        doubleValue_RoundingMode_helper(HALF_BELOW_SUBNORMAL_DOUBLE, "HALF_EVEN", 9.9999999999995E-311);
        doubleValue_RoundingMode_fail_helper(HALF_BELOW_SUBNORMAL_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_DOUBLE, "FLOOR", 1.0E-310);
        doubleValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_DOUBLE, "CEILING", 1.00000000000005E-310);
        doubleValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_DOUBLE, "DOWN", 1.0E-310);
        doubleValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_DOUBLE, "UP", 1.00000000000005E-310);
        doubleValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_DOUBLE, "HALF_DOWN", 1.0E-310);
        doubleValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_DOUBLE, "HALF_UP", 1.0E-310);
        doubleValue_RoundingMode_helper(JUST_ABOVE_SUBNORMAL_DOUBLE, "HALF_EVEN", 1.0E-310);
        doubleValue_RoundingMode_fail_helper(JUST_ABOVE_SUBNORMAL_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_DOUBLE, "FLOOR", 9.9999999999995E-311);
        doubleValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_DOUBLE, "CEILING", 1.0E-310);
        doubleValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_DOUBLE, "DOWN", 9.9999999999995E-311);
        doubleValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_DOUBLE, "UP", 1.0E-310);
        doubleValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_DOUBLE, "HALF_DOWN", 1.0E-310);
        doubleValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_DOUBLE, "HALF_UP", 1.0E-310);
        doubleValue_RoundingMode_helper(JUST_BELOW_SUBNORMAL_DOUBLE, "HALF_EVEN", 1.0E-310);
        doubleValue_RoundingMode_fail_helper(JUST_BELOW_SUBNORMAL_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_DOUBLE, "FLOOR", Double.NEGATIVE_INFINITY);
        doubleValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_DOUBLE, "CEILING", -1.7976931348623157E308);
        doubleValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_DOUBLE, "DOWN", -1.7976931348623157E308);
        doubleValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_DOUBLE, "UP", Double.NEGATIVE_INFINITY);
        doubleValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_DOUBLE, "HALF_DOWN", -1.7976931348623157E308);
        doubleValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_DOUBLE, "HALF_UP", Double.NEGATIVE_INFINITY);
        doubleValue_RoundingMode_helper(BELOW_NEGATIVE_MAX_DOUBLE, "HALF_EVEN", Double.NEGATIVE_INFINITY);
        doubleValue_RoundingMode_fail_helper(BELOW_NEGATIVE_MAX_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_DOUBLE, "FLOOR", -1.7976931348623157E308);
        doubleValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_DOUBLE, "CEILING", -1.7976931348623155E308);
        doubleValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_DOUBLE, "DOWN", -1.7976931348623155E308);
        doubleValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_DOUBLE, "UP", -1.7976931348623157E308);
        doubleValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_DOUBLE, "HALF_DOWN", -1.7976931348623155E308);
        doubleValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_DOUBLE, "HALF_UP", -1.7976931348623157E308);
        doubleValue_RoundingMode_helper(HALF_ABOVE_NEGATIVE_MAX_DOUBLE, "HALF_EVEN", -1.7976931348623155E308);
        doubleValue_RoundingMode_fail_helper(HALF_ABOVE_NEGATIVE_MAX_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_DOUBLE, "FLOOR", -1.7976931348623157E308);
        doubleValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_DOUBLE, "CEILING", -1.7976931348623155E308);
        doubleValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_DOUBLE, "DOWN", -1.7976931348623155E308);
        doubleValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_DOUBLE, "UP", -1.7976931348623157E308);
        doubleValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_DOUBLE, "HALF_DOWN", -1.7976931348623157E308);
        doubleValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_DOUBLE, "HALF_UP", -1.7976931348623157E308);
        doubleValue_RoundingMode_helper(JUST_ABOVE_NEGATIVE_MAX_DOUBLE, "HALF_EVEN", -1.7976931348623157E308);
        doubleValue_RoundingMode_fail_helper(JUST_ABOVE_NEGATIVE_MAX_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(ABOVE_MAX_DOUBLE, "FLOOR", 1.7976931348623157E308);
        doubleValue_RoundingMode_helper(ABOVE_MAX_DOUBLE, "CEILING", Double.POSITIVE_INFINITY);
        doubleValue_RoundingMode_helper(ABOVE_MAX_DOUBLE, "DOWN", 1.7976931348623157E308);
        doubleValue_RoundingMode_helper(ABOVE_MAX_DOUBLE, "UP", Double.POSITIVE_INFINITY);
        doubleValue_RoundingMode_helper(ABOVE_MAX_DOUBLE, "HALF_DOWN", 1.7976931348623157E308);
        doubleValue_RoundingMode_helper(ABOVE_MAX_DOUBLE, "HALF_UP", Double.POSITIVE_INFINITY);
        doubleValue_RoundingMode_helper(ABOVE_MAX_DOUBLE, "HALF_EVEN", Double.POSITIVE_INFINITY);
        doubleValue_RoundingMode_fail_helper(ABOVE_MAX_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(HALF_BELOW_MAX_DOUBLE, "FLOOR", 1.7976931348623155E308);
        doubleValue_RoundingMode_helper(HALF_BELOW_MAX_DOUBLE, "CEILING", 1.7976931348623157E308);
        doubleValue_RoundingMode_helper(HALF_BELOW_MAX_DOUBLE, "DOWN", 1.7976931348623155E308);
        doubleValue_RoundingMode_helper(HALF_BELOW_MAX_DOUBLE, "UP", 1.7976931348623157E308);
        doubleValue_RoundingMode_helper(HALF_BELOW_MAX_DOUBLE, "HALF_DOWN", 1.7976931348623155E308);
        doubleValue_RoundingMode_helper(HALF_BELOW_MAX_DOUBLE, "HALF_UP", 1.7976931348623157E308);
        doubleValue_RoundingMode_helper(HALF_BELOW_MAX_DOUBLE, "HALF_EVEN", 1.7976931348623155E308);
        doubleValue_RoundingMode_fail_helper(HALF_BELOW_MAX_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(JUST_BELOW_MAX_DOUBLE, "FLOOR", 1.7976931348623155E308);
        doubleValue_RoundingMode_helper(JUST_BELOW_MAX_DOUBLE, "CEILING", 1.7976931348623157E308);
        doubleValue_RoundingMode_helper(JUST_BELOW_MAX_DOUBLE, "DOWN", 1.7976931348623155E308);
        doubleValue_RoundingMode_helper(JUST_BELOW_MAX_DOUBLE, "UP", 1.7976931348623157E308);
        doubleValue_RoundingMode_helper(JUST_BELOW_MAX_DOUBLE, "HALF_DOWN", 1.7976931348623157E308);
        doubleValue_RoundingMode_helper(JUST_BELOW_MAX_DOUBLE, "HALF_UP", 1.7976931348623157E308);
        doubleValue_RoundingMode_helper(JUST_BELOW_MAX_DOUBLE, "HALF_EVEN", 1.7976931348623157E308);
        doubleValue_RoundingMode_fail_helper(JUST_BELOW_MAX_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(HALF_BELOW_ZERO_DOUBLE, "FLOOR", -4.9E-324);
        doubleValue_RoundingMode_helper(HALF_BELOW_ZERO_DOUBLE, "CEILING", -0.0);
        doubleValue_RoundingMode_helper(HALF_BELOW_ZERO_DOUBLE, "DOWN", -0.0);
        doubleValue_RoundingMode_helper(HALF_BELOW_ZERO_DOUBLE, "UP", -4.9E-324);
        doubleValue_RoundingMode_helper(HALF_BELOW_ZERO_DOUBLE, "HALF_DOWN", -0.0);
        doubleValue_RoundingMode_helper(HALF_BELOW_ZERO_DOUBLE, "HALF_UP", -4.9E-324);
        doubleValue_RoundingMode_helper(HALF_BELOW_ZERO_DOUBLE, "HALF_EVEN", -0.0);
        doubleValue_RoundingMode_fail_helper(HALF_BELOW_ZERO_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(JUST_BELOW_ZERO_DOUBLE, "FLOOR", -4.9E-324);
        doubleValue_RoundingMode_helper(JUST_BELOW_ZERO_DOUBLE, "CEILING", -0.0);
        doubleValue_RoundingMode_helper(JUST_BELOW_ZERO_DOUBLE, "DOWN", -0.0);
        doubleValue_RoundingMode_helper(JUST_BELOW_ZERO_DOUBLE, "UP", -4.9E-324);
        doubleValue_RoundingMode_helper(JUST_BELOW_ZERO_DOUBLE, "HALF_DOWN", -0.0);
        doubleValue_RoundingMode_helper(JUST_BELOW_ZERO_DOUBLE, "HALF_UP", -0.0);
        doubleValue_RoundingMode_helper(JUST_BELOW_ZERO_DOUBLE, "HALF_EVEN", -0.0);
        doubleValue_RoundingMode_fail_helper(JUST_BELOW_ZERO_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(HALF_ABOVE_ZERO_DOUBLE, "FLOOR", 0.0);
        doubleValue_RoundingMode_helper(HALF_ABOVE_ZERO_DOUBLE, "CEILING", 4.9E-324);
        doubleValue_RoundingMode_helper(HALF_ABOVE_ZERO_DOUBLE, "DOWN", 0.0);
        doubleValue_RoundingMode_helper(HALF_ABOVE_ZERO_DOUBLE, "UP", 4.9E-324);
        doubleValue_RoundingMode_helper(HALF_ABOVE_ZERO_DOUBLE, "HALF_DOWN", 0.0);
        doubleValue_RoundingMode_helper(HALF_ABOVE_ZERO_DOUBLE, "HALF_UP", 4.9E-324);
        doubleValue_RoundingMode_helper(HALF_ABOVE_ZERO_DOUBLE, "HALF_EVEN", 0.0);
        doubleValue_RoundingMode_fail_helper(HALF_ABOVE_ZERO_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(JUST_ABOVE_ZERO_DOUBLE, "FLOOR", 0.0);
        doubleValue_RoundingMode_helper(JUST_ABOVE_ZERO_DOUBLE, "CEILING", 4.9E-324);
        doubleValue_RoundingMode_helper(JUST_ABOVE_ZERO_DOUBLE, "DOWN", 0.0);
        doubleValue_RoundingMode_helper(JUST_ABOVE_ZERO_DOUBLE, "UP", 4.9E-324);
        doubleValue_RoundingMode_helper(JUST_ABOVE_ZERO_DOUBLE, "HALF_DOWN", 0.0);
        doubleValue_RoundingMode_helper(JUST_ABOVE_ZERO_DOUBLE, "HALF_UP", 0.0);
        doubleValue_RoundingMode_helper(JUST_ABOVE_ZERO_DOUBLE, "HALF_EVEN", 0.0);
        doubleValue_RoundingMode_fail_helper(JUST_ABOVE_ZERO_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(BOUNDARY_DOUBLE, "FLOOR", 2.225073858507201E-308);
        doubleValue_RoundingMode_helper(BOUNDARY_DOUBLE, "CEILING", 2.2250738585072014E-308);
        doubleValue_RoundingMode_helper(BOUNDARY_DOUBLE, "DOWN", 2.225073858507201E-308);
        doubleValue_RoundingMode_helper(BOUNDARY_DOUBLE, "UP", 2.2250738585072014E-308);
        doubleValue_RoundingMode_helper(BOUNDARY_DOUBLE, "HALF_DOWN", 2.225073858507201E-308);
        doubleValue_RoundingMode_helper(BOUNDARY_DOUBLE, "HALF_UP", 2.2250738585072014E-308);
        doubleValue_RoundingMode_helper(BOUNDARY_DOUBLE, "HALF_EVEN", 2.2250738585072014E-308);
        doubleValue_RoundingMode_fail_helper(BOUNDARY_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_DOUBLE, "FLOOR", 2.225073858507201E-308);
        doubleValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_DOUBLE, "CEILING", 2.2250738585072014E-308);
        doubleValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_DOUBLE, "DOWN", 2.225073858507201E-308);
        doubleValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_DOUBLE, "UP", 2.2250738585072014E-308);
        doubleValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_DOUBLE, "HALF_DOWN", 2.225073858507201E-308);
        doubleValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_DOUBLE, "HALF_UP", 2.225073858507201E-308);
        doubleValue_RoundingMode_helper(JUST_BELOW_BOUNDARY_DOUBLE, "HALF_EVEN", 2.225073858507201E-308);
        doubleValue_RoundingMode_fail_helper(JUST_BELOW_BOUNDARY_DOUBLE, "UNNECESSARY");

        doubleValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_DOUBLE, "FLOOR", 2.225073858507201E-308);
        doubleValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_DOUBLE, "CEILING", 2.2250738585072014E-308);
        doubleValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_DOUBLE, "DOWN", 2.225073858507201E-308);
        doubleValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_DOUBLE, "UP", 2.2250738585072014E-308);
        doubleValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_DOUBLE, "HALF_DOWN", 2.2250738585072014E-308);
        doubleValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_DOUBLE, "HALF_UP", 2.2250738585072014E-308);
        doubleValue_RoundingMode_helper(JUST_ABOVE_BOUNDARY_DOUBLE, "HALF_EVEN", 2.2250738585072014E-308);
        doubleValue_RoundingMode_fail_helper(JUST_ABOVE_BOUNDARY_DOUBLE, "UNNECESSARY");
    }

    private static void doubleValue_helper(@NotNull Rational r, double output) {
        aeq(r.doubleValue(), output);
    }

    private static void doubleValue_helper(@NotNull String r, double output) {
        doubleValue_helper(read(r).get(), output);
    }

    @Test
    public void testDoubleValue() {
        doubleValue_helper("0", 0.0);
        doubleValue_helper("1", 1.0);
        doubleValue_helper("1/2", 0.5);
        doubleValue_helper("1/3", 0.3333333333333333);
        doubleValue_helper("-1/3", -0.3333333333333333);
        doubleValue_helper(ALMOST_ONE, 1.0);
        doubleValue_helper(GOOGOL, 1.0E100);
        doubleValue_helper(PI_DOUBLE, 3.141592653589793);
        doubleValue_helper(HALF_ABOVE_PI_DOUBLE, 3.141592653589793);
        doubleValue_helper(HALF_BELOW_PI_DOUBLE, 3.141592653589793);
        doubleValue_helper(JUST_ABOVE_PI_DOUBLE, 3.141592653589793);
        doubleValue_helper(JUST_BELOW_PI_DOUBLE, 3.141592653589793);
        doubleValue_helper(SUBNORMAL_DOUBLE, 1.0E-310);
        doubleValue_helper(HALF_ABOVE_SUBNORMAL_DOUBLE, 1.00000000000005E-310);
        doubleValue_helper(HALF_BELOW_SUBNORMAL_DOUBLE, 9.9999999999995E-311);
        doubleValue_helper(JUST_ABOVE_SUBNORMAL_DOUBLE, 1.0E-310);
        doubleValue_helper(JUST_BELOW_SUBNORMAL_DOUBLE, 1.0E-310);
        doubleValue_helper(BELOW_NEGATIVE_MAX_DOUBLE, Double.NEGATIVE_INFINITY);
        doubleValue_helper(HALF_ABOVE_NEGATIVE_MAX_DOUBLE, -1.7976931348623155E308);
        doubleValue_helper(JUST_ABOVE_NEGATIVE_MAX_DOUBLE, -1.7976931348623157E308);
        doubleValue_helper(ABOVE_MAX_DOUBLE, Double.POSITIVE_INFINITY);
        doubleValue_helper(HALF_BELOW_MAX_DOUBLE, 1.7976931348623155E308);
        doubleValue_helper(JUST_BELOW_MAX_DOUBLE, 1.7976931348623157E308);
        doubleValue_helper(HALF_BELOW_ZERO_DOUBLE, -0.0);
        doubleValue_helper(JUST_BELOW_ZERO_DOUBLE, -0.0);
        doubleValue_helper(HALF_ABOVE_ZERO_DOUBLE, 0.0);
        doubleValue_helper(JUST_ABOVE_ZERO_DOUBLE, 0.0);
        doubleValue_helper(BOUNDARY_DOUBLE, 2.2250738585072014E-308);
        doubleValue_helper(JUST_BELOW_BOUNDARY_DOUBLE, 2.225073858507201E-308);
        doubleValue_helper(JUST_ABOVE_BOUNDARY_DOUBLE, 2.2250738585072014E-308);
    }

    private static void doubleValueExact_helper(@NotNull Rational r, double output) {
        aeq(r.doubleValueExact(), output);
    }

    private static void doubleValueExact_helper(@NotNull String r, double output) {
        doubleValueExact_helper(read(r).get(), output);
    }

    private static void doubleValueExact_fail_helper(@NotNull Rational r) {
        try {
            r.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    private static void doubleValueExact_fail_helper(@NotNull String r) {
        doubleValueExact_fail_helper(read(r).get());
    }

    @Test
    public void testDoubleValueExact() {
        doubleValueExact_helper("0", 0.0);
        doubleValueExact_helper("1", 1.0);
        doubleValueExact_helper("1/2", 0.5);
        doubleValueExact_fail_helper("1/3");
        doubleValueExact_fail_helper("-1/3");
        doubleValueExact_fail_helper(ALMOST_ONE);
        doubleValueExact_fail_helper(GOOGOL);
        doubleValueExact_helper(PI_DOUBLE, 3.141592653589793);
        doubleValueExact_fail_helper(HALF_ABOVE_PI_DOUBLE);
        doubleValueExact_fail_helper(HALF_BELOW_PI_DOUBLE);
        doubleValueExact_fail_helper(JUST_ABOVE_PI_DOUBLE);
        doubleValueExact_fail_helper(JUST_BELOW_PI_DOUBLE);
        doubleValueExact_helper(SUBNORMAL_DOUBLE, 1.0E-310);
        doubleValueExact_fail_helper(HALF_ABOVE_SUBNORMAL_DOUBLE);
        doubleValueExact_fail_helper(HALF_BELOW_SUBNORMAL_DOUBLE);
        doubleValueExact_fail_helper(JUST_ABOVE_SUBNORMAL_DOUBLE);
        doubleValueExact_fail_helper(JUST_BELOW_SUBNORMAL_DOUBLE);
        doubleValueExact_fail_helper(BELOW_NEGATIVE_MAX_DOUBLE);
        doubleValueExact_fail_helper(HALF_ABOVE_NEGATIVE_MAX_DOUBLE);
        doubleValueExact_fail_helper(JUST_ABOVE_NEGATIVE_MAX_DOUBLE);
        doubleValueExact_fail_helper(ABOVE_MAX_DOUBLE);
        doubleValueExact_fail_helper(HALF_BELOW_MAX_DOUBLE);
        doubleValueExact_fail_helper(JUST_BELOW_MAX_DOUBLE);
        doubleValueExact_fail_helper(HALF_BELOW_ZERO_DOUBLE);
        doubleValueExact_fail_helper(JUST_BELOW_ZERO_DOUBLE);
        doubleValueExact_fail_helper(HALF_ABOVE_ZERO_DOUBLE);
        doubleValueExact_fail_helper(JUST_ABOVE_ZERO_DOUBLE);
        doubleValueExact_fail_helper(BOUNDARY_DOUBLE);
        doubleValueExact_fail_helper(JUST_BELOW_BOUNDARY_DOUBLE);
        doubleValueExact_fail_helper(JUST_ABOVE_BOUNDARY_DOUBLE);
    }

    private static void hasTerminatingBaseExpansion_helper(@NotNull String r, @NotNull String base, boolean output) {
        aeq(read(r).get().hasTerminatingBaseExpansion(Readers.readBigInteger(base).get()), output);
    }

    private static void hasTerminatingBaseExpansion_fail_helper(@NotNull String r, @NotNull String base) {
        try {
            read(r).get().hasTerminatingBaseExpansion(Readers.readBigInteger(base).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testHasTerminatingBaseExpansion() {
        hasTerminatingBaseExpansion_helper("0", "2", true);
        hasTerminatingBaseExpansion_helper("1", "2", true);
        hasTerminatingBaseExpansion_helper("60", "2", true);
        hasTerminatingBaseExpansion_helper("1/2", "2", true);
        hasTerminatingBaseExpansion_helper("1/5", "2", false);
        hasTerminatingBaseExpansion_helper("-7/100", "2", false);
        hasTerminatingBaseExpansion_helper("-3/640", "2", false);
        hasTerminatingBaseExpansion_helper("1/3", "2", false);
        hasTerminatingBaseExpansion_helper("-1/15", "2", false);
        hasTerminatingBaseExpansion_helper("-2/9", "2", false);

        hasTerminatingBaseExpansion_helper("0", "3", true);
        hasTerminatingBaseExpansion_helper("1", "3", true);
        hasTerminatingBaseExpansion_helper("60", "3", true);
        hasTerminatingBaseExpansion_helper("1/2", "3", false);
        hasTerminatingBaseExpansion_helper("1/5", "3", false);
        hasTerminatingBaseExpansion_helper("-7/100", "3", false);
        hasTerminatingBaseExpansion_helper("-3/640", "3", false);
        hasTerminatingBaseExpansion_helper("1/3", "3", true);
        hasTerminatingBaseExpansion_helper("-1/15", "3", false);
        hasTerminatingBaseExpansion_helper("-2/9", "3", true);

        hasTerminatingBaseExpansion_helper("0", "4", true);
        hasTerminatingBaseExpansion_helper("1", "4", true);
        hasTerminatingBaseExpansion_helper("60", "4", true);
        hasTerminatingBaseExpansion_helper("1/2", "4", true);
        hasTerminatingBaseExpansion_helper("1/5", "4", false);
        hasTerminatingBaseExpansion_helper("-7/100", "4", false);
        hasTerminatingBaseExpansion_helper("-3/640", "4", false);
        hasTerminatingBaseExpansion_helper("1/3", "4", false);
        hasTerminatingBaseExpansion_helper("-1/15", "4", false);
        hasTerminatingBaseExpansion_helper("-2/9", "4", false);

        hasTerminatingBaseExpansion_helper("0", "16", true);
        hasTerminatingBaseExpansion_helper("1", "16", true);
        hasTerminatingBaseExpansion_helper("60", "16", true);
        hasTerminatingBaseExpansion_helper("1/2", "16", true);
        hasTerminatingBaseExpansion_helper("1/5", "16", false);
        hasTerminatingBaseExpansion_helper("-7/100", "16", false);
        hasTerminatingBaseExpansion_helper("-3/640", "16", false);
        hasTerminatingBaseExpansion_helper("1/3", "16", false);
        hasTerminatingBaseExpansion_helper("-1/15", "16", false);
        hasTerminatingBaseExpansion_helper("-2/9", "16", false);

        hasTerminatingBaseExpansion_helper("0", "83", true);
        hasTerminatingBaseExpansion_helper("1", "83", true);
        hasTerminatingBaseExpansion_helper("60", "83", true);
        hasTerminatingBaseExpansion_helper("1/2", "83", false);
        hasTerminatingBaseExpansion_helper("1/5", "83", false);
        hasTerminatingBaseExpansion_helper("-7/100", "83", false);
        hasTerminatingBaseExpansion_helper("-3/640", "83", false);
        hasTerminatingBaseExpansion_helper("1/3", "83", false);
        hasTerminatingBaseExpansion_helper("-1/15", "83", false);
        hasTerminatingBaseExpansion_helper("-2/9", "83", false);

        hasTerminatingBaseExpansion_helper("0", "100", true);
        hasTerminatingBaseExpansion_helper("1", "100", true);
        hasTerminatingBaseExpansion_helper("60", "100", true);
        hasTerminatingBaseExpansion_helper("1/2", "100", true);
        hasTerminatingBaseExpansion_helper("1/5", "100", true);
        hasTerminatingBaseExpansion_helper("-7/100", "100", true);
        hasTerminatingBaseExpansion_helper("-3/640", "100", true);
        hasTerminatingBaseExpansion_helper("1/3", "100", false);
        hasTerminatingBaseExpansion_helper("-1/15", "100", false);
        hasTerminatingBaseExpansion_helper("-2/9", "100", false);

        hasTerminatingBaseExpansion_fail_helper("1/2", "1");
        hasTerminatingBaseExpansion_fail_helper("1/2", "0");
        hasTerminatingBaseExpansion_fail_helper("1/2", "-1");
    }

    private static void bigDecimalValueByPrecision_int_RoundingMode_helper(
            @NotNull String r,
            int precision,
            @NotNull String roundingMode,
            @NotNull String output
    ) {
        aeq(read(r).get().bigDecimalValueByPrecision(precision, Readers.readRoundingMode(roundingMode).get()), output);
    }

    private static void bigDecimalValueByPrecision_int_RoundingMode_fail_helper(
            @NotNull String r,
            int precision,
            @NotNull String roundingMode
    ) {
        try {
            read(r).get().bigDecimalValueByPrecision(precision, Readers.readRoundingMode(roundingMode).get());
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigDecimalValueByPrecision_int_RoundingMode() {
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 0, "FLOOR", "0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 0, "CEILING", "0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 0, "DOWN", "0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 0, "UP", "0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 0, "HALF_DOWN", "0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 0, "HALF_UP", "0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 0, "HALF_EVEN", "0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 0, "UNNECESSARY", "0");

        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 4, "FLOOR", "0.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 4, "CEILING", "0.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 4, "DOWN", "0.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 4, "UP", "0.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 4, "HALF_DOWN", "0.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 4, "HALF_UP", "0.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 4, "HALF_EVEN", "0.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("0", 4, "UNNECESSARY", "0.000");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 0, "FLOOR", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 0, "CEILING", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 0, "DOWN", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 0, "UP", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 0, "HALF_DOWN", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 0, "HALF_UP", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 0, "HALF_EVEN", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 0, "UNNECESSARY", "1");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 4, "FLOOR", "1.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 4, "CEILING", "1.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 4, "DOWN", "1.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 4, "UP", "1.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 4, "HALF_DOWN", "1.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 4, "HALF_UP", "1.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 4, "HALF_EVEN", "1.000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1", 4, "UNNECESSARY", "1.000");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 0, "FLOOR", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 0, "CEILING", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 0, "DOWN", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 0, "UP", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 0, "HALF_DOWN", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 0, "HALF_UP", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 0, "HALF_EVEN", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 0, "UNNECESSARY", "0.5");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 1, "FLOOR", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 1, "CEILING", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 1, "DOWN", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 1, "UP", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 1, "HALF_DOWN", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 1, "HALF_UP", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 1, "HALF_EVEN", "0.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 1, "UNNECESSARY", "0.5");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 4, "FLOOR", "0.5000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 4, "CEILING", "0.5000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 4, "DOWN", "0.5000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 4, "UP", "0.5000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 4, "HALF_DOWN", "0.5000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 4, "HALF_UP", "0.5000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 4, "HALF_EVEN", "0.5000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/2", 4, "UNNECESSARY", "0.5000");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 0, "FLOOR", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 0, "CEILING", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 0, "DOWN", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 0, "UP", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 0, "HALF_DOWN", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 0, "HALF_UP", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 0, "HALF_EVEN", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 0, "UNNECESSARY", "0.015625");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 1, "FLOOR", "0.01");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 1, "CEILING", "0.02");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 1, "DOWN", "0.01");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 1, "UP", "0.02");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 1, "HALF_DOWN", "0.02");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 1, "HALF_UP", "0.02");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 1, "HALF_EVEN", "0.02");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("1/64", 1, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "FLOOR", "0.015");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "CEILING", "0.016");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "DOWN", "0.015");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "UP", "0.016");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "HALF_DOWN", "0.016");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "HALF_UP", "0.016");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "HALF_EVEN", "0.016");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("1/64", 2, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "FLOOR", "0.0156");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "CEILING", "0.0157");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "DOWN", "0.0156");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "UP", "0.0157");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "HALF_DOWN", "0.0156");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "HALF_UP", "0.0156");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "HALF_EVEN", "0.0156");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("1/64", 3, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "FLOOR", "0.01562");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "CEILING", "0.01563");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "DOWN", "0.01562");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "UP", "0.01563");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "HALF_DOWN", "0.01562");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "HALF_UP", "0.01563");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "HALF_EVEN", "0.01562");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("1/64", 4, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 5, "FLOOR", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 5, "CEILING", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 5, "DOWN", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 5, "UP", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 5, "HALF_DOWN", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 5, "HALF_UP", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 5, "HALF_EVEN", "0.015625");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 5, "UNNECESSARY", "0.015625");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 6, "FLOOR", "0.0156250");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 6, "CEILING", "0.0156250");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 6, "DOWN", "0.0156250");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 6, "UP", "0.0156250");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 6, "HALF_DOWN", "0.0156250");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 6, "HALF_UP", "0.0156250");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 6, "HALF_EVEN", "0.0156250");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 6, "UNNECESSARY", "0.0156250");

        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "FLOOR");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "CEILING");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "DOWN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "UP");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "HALF_DOWN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "HALF_UP");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "HALF_EVEN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "FLOOR", "-0.4");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "CEILING", "-0.3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "DOWN", "-0.3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "UP", "-0.4");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "HALF_DOWN", "-0.3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "HALF_UP", "-0.3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "HALF_EVEN", "-0.3");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 1, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "FLOOR", "-0.34");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "CEILING", "-0.33");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "DOWN", "-0.33");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "UP", "-0.34");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "HALF_DOWN", "-0.33");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "HALF_UP", "-0.33");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "HALF_EVEN", "-0.33");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 2, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "FLOOR", "-0.334");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "CEILING", "-0.333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "DOWN", "-0.333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "UP", "-0.334");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "HALF_DOWN", "-0.333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "HALF_UP", "-0.333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "HALF_EVEN", "-0.333");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 3, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "FLOOR", "-0.3334");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "CEILING", "-0.3333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "DOWN", "-0.3333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "UP", "-0.3334");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "HALF_DOWN", "-0.3333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "HALF_UP", "-0.3333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "HALF_EVEN", "-0.3333");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 4, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 0, "FLOOR", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 0, "CEILING", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 0, "DOWN", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 0, "UP", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 0, "HALF_DOWN", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 0, "HALF_UP", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 0, "HALF_EVEN", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 0, "UNNECESSARY", "6789");

        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 1, "FLOOR", "6E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 1, "CEILING", "7E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 1, "DOWN", "6E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 1, "UP", "7E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 1, "HALF_DOWN", "7E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 1, "HALF_UP", "7E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 1, "HALF_EVEN", "7E+3");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("6789", 1, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "FLOOR", "6.7E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "CEILING", "6.8E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "DOWN", "6.7E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "UP", "6.8E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "HALF_DOWN", "6.8E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "HALF_UP", "6.8E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "HALF_EVEN", "6.8E+3");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("6789", 2, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "FLOOR", "6.78E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "CEILING", "6.79E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "DOWN", "6.78E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "UP", "6.79E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "HALF_DOWN", "6.79E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "HALF_UP", "6.79E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "HALF_EVEN", "6.79E+3");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("6789", 3, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 4, "FLOOR", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 4, "CEILING", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 4, "DOWN", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 4, "UP", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 4, "HALF_DOWN", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 4, "HALF_UP", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 4, "HALF_EVEN", "6789");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 4, "UNNECESSARY", "6789");

        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 5, "FLOOR", "6789.0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 5, "CEILING", "6789.0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 5, "DOWN", "6789.0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 5, "UP", "6789.0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 5, "HALF_DOWN", "6789.0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 5, "HALF_UP", "6789.0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 5, "HALF_EVEN", "6789.0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 5, "UNNECESSARY", "6789.0");

        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 0, "FLOOR", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 0, "CEILING", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 0, "DOWN", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 0, "UP", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 0, "HALF_DOWN", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 0, "HALF_UP", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 0, "HALF_EVEN", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 0, "UNNECESSARY", "0.95");

        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 1, "FLOOR", "0.9");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 1, "CEILING", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 1, "DOWN", "0.9");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 1, "UP", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 1, "HALF_DOWN", "0.9");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 1, "HALF_UP", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 1, "HALF_EVEN", "1");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("19/20", 1, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 2, "FLOOR", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 2, "CEILING", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 2, "DOWN", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 2, "UP", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 2, "HALF_DOWN", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 2, "HALF_UP", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 2, "HALF_EVEN", "0.95");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 2, "UNNECESSARY", "0.95");

        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 3, "FLOOR", "0.950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 3, "CEILING", "0.950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 3, "DOWN", "0.950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 3, "UP", "0.950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 3, "HALF_DOWN", "0.950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 3, "HALF_UP", "0.950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 3, "HALF_EVEN", "0.950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 3, "UNNECESSARY", "0.950");

        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 4, "FLOOR", "0.9500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 4, "CEILING", "0.9500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 4, "DOWN", "0.9500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 4, "UP", "0.9500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 4, "HALF_DOWN", "0.9500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 4, "HALF_UP", "0.9500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 4, "HALF_EVEN", "0.9500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 4, "UNNECESSARY", "0.9500");

        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 5, "FLOOR", "0.95000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 5, "CEILING", "0.95000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 5, "DOWN", "0.95000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 5, "UP", "0.95000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 5, "HALF_DOWN", "0.95000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 5, "HALF_UP", "0.95000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 5, "HALF_EVEN", "0.95000");
        bigDecimalValueByPrecision_int_RoundingMode_helper("19/20", 5, "UNNECESSARY", "0.95000");

        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 0, "FLOOR", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 0, "CEILING", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 0, "DOWN", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 0, "UP", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 0, "HALF_DOWN", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 0, "HALF_UP", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 0, "HALF_EVEN", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 0, "UNNECESSARY", "0.995");

        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 1, "FLOOR", "0.9");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 1, "CEILING", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 1, "DOWN", "0.9");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 1, "UP", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 1, "HALF_DOWN", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 1, "HALF_UP", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 1, "HALF_EVEN", "1");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("199/200", 1, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "FLOOR", "0.99");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "CEILING", "1.0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "DOWN", "0.99");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "UP", "1.0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "HALF_DOWN", "0.99");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "HALF_UP", "1.0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "HALF_EVEN", "1.0");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("199/200", 2, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 3, "FLOOR", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 3, "CEILING", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 3, "DOWN", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 3, "UP", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 3, "HALF_DOWN", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 3, "HALF_UP", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 3, "HALF_EVEN", "0.995");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 3, "UNNECESSARY", "0.995");

        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 4, "FLOOR", "0.9950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 4, "CEILING", "0.9950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 4, "DOWN", "0.9950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 4, "UP", "0.9950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 4, "HALF_DOWN", "0.9950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 4, "HALF_UP", "0.9950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 4, "HALF_EVEN", "0.9950");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 4, "UNNECESSARY", "0.9950");

        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 5, "FLOOR", "0.99500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 5, "CEILING", "0.99500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 5, "DOWN", "0.99500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 5, "UP", "0.99500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 5, "HALF_DOWN", "0.99500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 5, "HALF_UP", "0.99500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 5, "HALF_EVEN", "0.99500");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 5, "UNNECESSARY", "0.99500");

        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "FLOOR");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "CEILING");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "DOWN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "UP");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "HALF_DOWN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "HALF_UP");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "HALF_EVEN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "UNNECESSARY");
    }

    private static void bigDecimalValueByScale_int_RoundingMode_helper(
            @NotNull String r,
            int scale,
            @NotNull String roundingMode,
            @NotNull String output
    ) {
        aeq(read(r).get().bigDecimalValueByScale(scale, Readers.readRoundingMode(roundingMode).get()), output);
    }

    private static void bigDecimalValueByScale_int_RoundingMode_fail_helper(
            @NotNull String r,
            int scale,
            @NotNull String roundingMode
    ) {
        try {
            read(r).get().bigDecimalValueByScale(scale, Readers.readRoundingMode(roundingMode).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBigDecimalValueByScale_int_RoundingMode() {
        bigDecimalValueByScale_int_RoundingMode_helper("0", 0, "FLOOR", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 0, "CEILING", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 0, "DOWN", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 0, "UP", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 0, "HALF_DOWN", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 0, "HALF_UP", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 0, "HALF_EVEN", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 0, "UNNECESSARY", "0");

        bigDecimalValueByScale_int_RoundingMode_helper("0", 3, "FLOOR", "0.000");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 3, "CEILING", "0.000");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 3, "DOWN", "0.000");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 3, "UP", "0.000");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 3, "HALF_DOWN", "0.000");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 3, "HALF_UP", "0.000");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 3, "HALF_EVEN", "0.000");
        bigDecimalValueByScale_int_RoundingMode_helper("0", 3, "UNNECESSARY", "0.000");

        bigDecimalValueByScale_int_RoundingMode_helper("0", -3, "FLOOR", "0E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("0", -3, "CEILING", "0E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("0", -3, "DOWN", "0E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("0", -3, "UP", "0E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("0", -3, "HALF_DOWN", "0E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("0", -3, "HALF_UP", "0E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("0", -3, "HALF_EVEN", "0E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("0", -3, "UNNECESSARY", "0E+3");

        bigDecimalValueByScale_int_RoundingMode_helper("1", 0, "FLOOR", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 0, "CEILING", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 0, "DOWN", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 0, "UP", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 0, "HALF_DOWN", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 0, "HALF_UP", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 0, "HALF_EVEN", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 0, "UNNECESSARY", "1");

        bigDecimalValueByScale_int_RoundingMode_helper("1", 3, "FLOOR", "1.000");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 3, "CEILING", "1.000");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 3, "DOWN", "1.000");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 3, "UP", "1.000");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 3, "HALF_DOWN", "1.000");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 3, "HALF_UP", "1.000");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 3, "HALF_EVEN", "1.000");
        bigDecimalValueByScale_int_RoundingMode_helper("1", 3, "UNNECESSARY", "1.000");

        bigDecimalValueByScale_int_RoundingMode_helper("1", -3, "FLOOR", "0E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("1", -3, "CEILING", "1E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("1", -3, "DOWN", "0E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("1", -3, "UP", "1E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("1", -3, "HALF_DOWN", "0E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("1", -3, "HALF_UP", "0E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("1", -3, "HALF_EVEN", "0E+3");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1", -3, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "FLOOR", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "CEILING", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "DOWN", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "UP", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "HALF_DOWN", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "HALF_UP", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "HALF_EVEN", "0");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/2", 0, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 1, "FLOOR", "0.5");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 1, "CEILING", "0.5");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 1, "DOWN", "0.5");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 1, "UP", "0.5");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 1, "HALF_DOWN", "0.5");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 1, "HALF_UP", "0.5");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 1, "HALF_EVEN", "0.5");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 1, "UNNECESSARY", "0.5");

        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 4, "FLOOR", "0.5000");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 4, "CEILING", "0.5000");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 4, "DOWN", "0.5000");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 4, "UP", "0.5000");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 4, "HALF_DOWN", "0.5000");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 4, "HALF_UP", "0.5000");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 4, "HALF_EVEN", "0.5000");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 4, "UNNECESSARY", "0.5000");

        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 0, "FLOOR", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 0, "CEILING", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 0, "DOWN", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 0, "UP", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 0, "HALF_DOWN", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 0, "HALF_UP", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 0, "HALF_EVEN", "0");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/64", 0, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "FLOOR", "0.0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "CEILING", "0.1");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "DOWN", "0.0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "UP", "0.1");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "HALF_DOWN", "0.0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "HALF_UP", "0.0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "HALF_EVEN", "0.0");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/64", 1, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "FLOOR", "0.01");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "CEILING", "0.02");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "DOWN", "0.01");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "UP", "0.02");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "HALF_DOWN", "0.02");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "HALF_UP", "0.02");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "HALF_EVEN", "0.02");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/64", 2, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "FLOOR", "0.015");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "CEILING", "0.016");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "DOWN", "0.015");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "UP", "0.016");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "HALF_DOWN", "0.016");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "HALF_UP", "0.016");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "HALF_EVEN", "0.016");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/64", 3, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "FLOOR", "0.0156");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "CEILING", "0.0157");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "DOWN", "0.0156");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "UP", "0.0157");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "HALF_DOWN", "0.0156");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "HALF_UP", "0.0156");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "HALF_EVEN", "0.0156");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/64", 4, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "FLOOR", "0.01562");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "CEILING", "0.01563");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "DOWN", "0.01562");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "UP", "0.01563");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "HALF_DOWN", "0.01562");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "HALF_UP", "0.01563");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "HALF_EVEN", "0.01562");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/64", 5, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 6, "FLOOR", "0.015625");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 6, "CEILING", "0.015625");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 6, "DOWN", "0.015625");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 6, "UP", "0.015625");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 6, "HALF_DOWN", "0.015625");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 6, "HALF_UP", "0.015625");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 6, "HALF_EVEN", "0.015625");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 6, "UNNECESSARY", "0.015625");

        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 0, "FLOOR", "-1");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 0, "CEILING", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 0, "DOWN", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 0, "UP", "-1");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 0, "HALF_DOWN", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 0, "HALF_UP", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 0, "HALF_EVEN", "0");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("-1/3", 0, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "FLOOR", "-0.4");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "CEILING", "-0.3");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "DOWN", "-0.3");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "UP", "-0.4");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "HALF_DOWN", "-0.3");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "HALF_UP", "-0.3");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "HALF_EVEN", "-0.3");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("-1/3", 1, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "FLOOR", "-0.34");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "CEILING", "-0.33");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "DOWN", "-0.33");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "UP", "-0.34");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "HALF_DOWN", "-0.33");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "HALF_UP", "-0.33");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "HALF_EVEN", "-0.33");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("-1/3", 2, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "FLOOR", "-0.334");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "CEILING", "-0.333");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "DOWN", "-0.333");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "UP", "-0.334");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "HALF_DOWN", "-0.333");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "HALF_UP", "-0.333");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "HALF_EVEN", "-0.333");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("-1/3", 3, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("6789", 0, "FLOOR", "6789");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", 0, "CEILING", "6789");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", 0, "DOWN", "6789");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", 0, "UP", "6789");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", 0, "HALF_DOWN", "6789");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", 0, "HALF_UP", "6789");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", 0, "HALF_EVEN", "6789");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", 0, "UNNECESSARY", "6789");

        bigDecimalValueByScale_int_RoundingMode_helper("6789", -1, "FLOOR", "6.78E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -1, "CEILING", "6.79E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -1, "DOWN", "6.78E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -1, "UP", "6.79E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -1, "HALF_DOWN", "6.79E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -1, "HALF_UP", "6.79E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -1, "HALF_EVEN", "6.79E+3");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("6789", -1, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "FLOOR", "6.7E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "CEILING", "6.8E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "DOWN", "6.7E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "UP", "6.8E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "HALF_DOWN", "6.8E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "HALF_UP", "6.8E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "HALF_EVEN", "6.8E+3");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("6789", -2, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "FLOOR", "6E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "CEILING", "7E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "DOWN", "6E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "UP", "7E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "HALF_DOWN", "7E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "HALF_UP", "7E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "HALF_EVEN", "7E+3");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("6789", -3, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "FLOOR", "0E+4");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "CEILING", "1E+4");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "DOWN", "0E+4");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "UP", "1E+4");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "HALF_DOWN", "1E+4");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "HALF_UP", "1E+4");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "HALF_EVEN", "1E+4");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("6789", -4, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "FLOOR", "0E+5");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "CEILING", "1E+5");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "DOWN", "0E+5");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "UP", "1E+5");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "HALF_DOWN", "0E+5");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "HALF_UP", "0E+5");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "HALF_EVEN", "0E+5");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("6789", -5, "UNNECESSARY");
    }

    private static void bigDecimalValueByPrecision_int_helper(
            @NotNull String r,
            int precision,
            @NotNull String output
    ) {
        aeq(read(r).get().bigDecimalValueByPrecision(precision), output);
    }

    private static void bigDecimalValueByPrecision_int_fail_helper(@NotNull String r, int precision) {
        try {
            read(r).get().bigDecimalValueByPrecision(precision);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigDecimalValueByPrecision_int() {
        bigDecimalValueByPrecision_int_helper("0", 0, "0");
        bigDecimalValueByPrecision_int_helper("0", 4, "0.000");

        bigDecimalValueByPrecision_int_helper("1", 0, "1");
        bigDecimalValueByPrecision_int_helper("1", 4, "1.000");

        bigDecimalValueByPrecision_int_helper("1/2", 0, "0.5");
        bigDecimalValueByPrecision_int_helper("1/2", 1, "0.5");
        bigDecimalValueByPrecision_int_helper("1/2", 4, "0.5000");

        bigDecimalValueByPrecision_int_helper("1/64", 0, "0.015625");
        bigDecimalValueByPrecision_int_helper("1/64", 1, "0.02");
        bigDecimalValueByPrecision_int_helper("1/64", 2, "0.016");
        bigDecimalValueByPrecision_int_helper("1/64", 3, "0.0156");
        bigDecimalValueByPrecision_int_helper("1/64", 4, "0.01562");
        bigDecimalValueByPrecision_int_helper("1/64", 5, "0.015625");
        bigDecimalValueByPrecision_int_helper("1/64", 6, "0.0156250");

        bigDecimalValueByPrecision_int_fail_helper("-1/3", 0);
        bigDecimalValueByPrecision_int_helper("-1/3", 1, "-0.3");
        bigDecimalValueByPrecision_int_helper("-1/3", 2, "-0.33");
        bigDecimalValueByPrecision_int_helper("-1/3", 3, "-0.333");
        bigDecimalValueByPrecision_int_helper("-1/3", 4, "-0.3333");

        bigDecimalValueByPrecision_int_helper("6789", 0, "6789");
        bigDecimalValueByPrecision_int_helper("6789", 1, "7E+3");
        bigDecimalValueByPrecision_int_helper("6789", 2, "6.8E+3");
        bigDecimalValueByPrecision_int_helper("6789", 3, "6.79E+3");
        bigDecimalValueByPrecision_int_helper("6789", 4, "6789");
        bigDecimalValueByPrecision_int_helper("6789", 5, "6789.0");

        bigDecimalValueByPrecision_int_helper("19/20", 0, "0.95");
        bigDecimalValueByPrecision_int_helper("19/20", 1, "1");
        bigDecimalValueByPrecision_int_helper("19/20", 2, "0.95");
        bigDecimalValueByPrecision_int_helper("19/20", 3, "0.950");
        bigDecimalValueByPrecision_int_helper("19/20", 4, "0.9500");
        bigDecimalValueByPrecision_int_helper("19/20", 5, "0.95000");

        bigDecimalValueByPrecision_int_helper("199/200", 0, "0.995");
        bigDecimalValueByPrecision_int_helper("199/200", 1, "1");
        bigDecimalValueByPrecision_int_helper("199/200", 2, "1.0");
        bigDecimalValueByPrecision_int_helper("199/200", 3, "0.995");
        bigDecimalValueByPrecision_int_helper("199/200", 4, "0.9950");
        bigDecimalValueByPrecision_int_helper("199/200", 5, "0.99500");

        bigDecimalValueByPrecision_int_fail_helper("5", -1);
    }

    private static void bigDecimalValueByScale_int_helper(@NotNull String r, int scale, @NotNull String output) {
        aeq(read(r).get().bigDecimalValueByScale(scale), output);
    }

    @Test
    public void testBigDecimalValueByScale_int() {
        bigDecimalValueByScale_int_helper("0", 0, "0");
        bigDecimalValueByScale_int_helper("0", 3, "0.000");
        bigDecimalValueByScale_int_helper("0", -3, "0E+3");

        bigDecimalValueByScale_int_helper("1", 0, "1");
        bigDecimalValueByScale_int_helper("1", 3, "1.000");
        bigDecimalValueByScale_int_helper("1", -3, "0E+3");

        bigDecimalValueByScale_int_helper("1/2", 0, "0");
        bigDecimalValueByScale_int_helper("1/2", 1, "0.5");
        bigDecimalValueByScale_int_helper("1/2", 4, "0.5000");

        bigDecimalValueByScale_int_helper("1/64", 0, "0");
        bigDecimalValueByScale_int_helper("1/64", 1, "0.0");
        bigDecimalValueByScale_int_helper("1/64", 2, "0.02");
        bigDecimalValueByScale_int_helper("1/64", 3, "0.016");
        bigDecimalValueByScale_int_helper("1/64", 4, "0.0156");
        bigDecimalValueByScale_int_helper("1/64", 5, "0.01562");
        bigDecimalValueByScale_int_helper("1/64", 6, "0.015625");

        bigDecimalValueByScale_int_helper("-1/3", 0, "0");
        bigDecimalValueByScale_int_helper("-1/3", 1, "-0.3");
        bigDecimalValueByScale_int_helper("-1/3", 2, "-0.33");
        bigDecimalValueByScale_int_helper("-1/3", 3, "-0.333");

        bigDecimalValueByScale_int_helper("6789", 0, "6789");
        bigDecimalValueByScale_int_helper("6789", -1, "6.79E+3");
        bigDecimalValueByScale_int_helper("6789", -2, "6.8E+3");
        bigDecimalValueByScale_int_helper("6789", -3, "7E+3");
        bigDecimalValueByScale_int_helper("6789", -4, "1E+4");
        bigDecimalValueByScale_int_helper("6789", -5, "0E+5");
    }

    private static void bigDecimalValueExact_helper(@NotNull Rational input, @NotNull String output) {
        aeq(input.bigDecimalValueExact(), output);
    }

    private static void bigDecimalValueExact_helper(@NotNull String input, @NotNull String output) {
        bigDecimalValueExact_helper(read(input).get(), output);
    }

    private static void bigDecimalValueExact_fail_helper(@NotNull String input) {
        try {
            read(input).get().bigDecimalValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBigDecimalValueExact() {
        bigDecimalValueExact_helper("0", "0");
        bigDecimalValueExact_helper("1", "1");
        bigDecimalValueExact_helper("-23", "-23");
        bigDecimalValueExact_helper("4/5", "0.8");
        bigDecimalValueExact_helper("1/64", "0.015625");
        bigDecimalValueExact_helper("1234", "1234");
        bigDecimalValueExact_helper("19/20", "0.95");
        bigDecimalValueExact_helper("199/200", "0.995");
        bigDecimalValueExact_helper(SMALLEST_FLOAT,
                "1.4012984643248170709237295832899161312802619418765157717570682838897910826858606014866381883621215" +
                "8203125E-45");
        bigDecimalValueExact_helper(LARGEST_FLOAT, "340282346638528859811704183484516925440");
        bigDecimalValueExact_helper(SMALLEST_DOUBLE,
                "4.9406564584124654417656879286822137236505980261432476442558568250067550727020875186529983636163599" +
                "237979656469544571773092665671035593979639877479601078187812630071319031140452784581716784898210368" +
                "871863605699873072305000638740915356498438731247339727316961514003171538539807412623856559117102665" +
                "855668676818703956031062493194527159149245532930545654440112748012970999954193198940908041656332452" +
                "475714786901472678015935523861155013480352649347201937902681071074917033322268447533357208324319360" +
                "923828934583680601060115061698097530783422773183292479049825247307763759272478746560847782037344696" +
                "995336470179726777175851256605511991315048911014510378627381672509558373897335989936648099411642057" +
                "02637090279242767544565229087538682506419718265533447265625E-324");
        bigDecimalValueExact_helper(LARGEST_DOUBLE,
                "179769313486231570814527423731704356798070567525844996598917476803157260780028538760589558632766878" +
                "171540458953514382464234321326889464182768467546703537516986049910576551282076245490090389328944075" +
                "868508455133942304583236903222948165808559332123348274797826204144723168738177180919299881250404026" +
                "184124858368");
        bigDecimalValueExact_fail_helper("1/3");
    }

    private static void bitLength_helper(@NotNull String x, int output) {
        aeq(read(x).get().bitLength(), output);
    }

    @Test
    public void testBitLength() {
        bitLength_helper("0", 1);
        bitLength_helper("1", 2);
        bitLength_helper("2", 3);
        bitLength_helper("-2", 3);
        bitLength_helper("5/3", 5);
        bitLength_helper("-5/3", 5);
    }

    private static void add_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().add(read(b).get()), output);
    }

    @Test
    public void testAdd() {
        add_helper("1/2", "1/3", "5/6");
        add_helper("1/2", "-1/3", "1/6");
        add_helper("-1/2", "1/3", "-1/6");
        add_helper("-1/2", "-1/3", "-5/6");
        add_helper("2", "1/5", "11/5");
        add_helper("2", "-1/5", "9/5");
        add_helper("-2", "1/5", "-9/5");
        add_helper("-2", "-1/5", "-11/5");
        add_helper("2", "5", "7");
        add_helper("2", "-5", "-3");
        add_helper("-2", "5", "3");
        add_helper("-2", "-5", "-7");
        add_helper("6/7", "1/7", "1");
        add_helper("6/7", "-6/7", "0");
        add_helper("1/2", "0", "1/2");
        add_helper("-1/2", "0", "-1/2");
        add_helper("1/2", "1", "3/2");
        add_helper("-1/2", "1", "1/2");
        add_helper("0", "0", "0");
        add_helper("0", "1", "1");
        add_helper("1", "0", "1");
        add_helper("1", "1", "2");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().negate(), read(output).get());
    }

    @Test
    public void testNegate() {
        negate_helper("2/3", "-2/3");
        negate_helper("-2/3", "2/3");
        negate_helper("4", "-4");
        negate_helper("-4", "4");
        negate_helper("0", "0");
        negate_helper("1", "-1");
        negate_helper("-1", "1");
    }

    private static void abs_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().abs(), read(output).get());
    }

    @Test
    public void testAbs() {
        abs_helper("2/3", "2/3");
        abs_helper("-2/3", "2/3");
        abs_helper("4", "4");
        abs_helper("-4", "4");
        abs_helper("0", "0");
        abs_helper("1", "1");
        abs_helper("-1", "1");
    }

    private static void signum_helper(@NotNull String input, int output) {
        aeq(read(input).get().signum(), output);
    }

    @Test
    public void testSignum() {
        signum_helper("2/3", 1);
        signum_helper("-2/3", -1);
        signum_helper("4", 1);
        signum_helper("-4", -1);
        signum_helper("0", 0);
        signum_helper("1", 1);
        signum_helper("-1", -1);
    }

    private static void subtract_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().subtract(read(b).get()), output);
    }

    @Test
    public void testSubtract() {
        subtract_helper("1/2", "1/3", "1/6");
        subtract_helper("1/2", "-1/3", "5/6");
        subtract_helper("-1/2", "1/3", "-5/6");
        subtract_helper("-1/2", "-1/3", "-1/6");
        subtract_helper("2", "1/5", "9/5");
        subtract_helper("2", "-1/5", "11/5");
        subtract_helper("-2", "1/5", "-11/5");
        subtract_helper("-2", "-1/5", "-9/5");
        subtract_helper("2", "5", "-3");
        subtract_helper("2", "-5", "7");
        subtract_helper("-2", "5", "-7");
        subtract_helper("-2", "-5", "3");
        subtract_helper("8/7", "1/7", "1");
        subtract_helper("6/7", "6/7", "0");
        subtract_helper("1/2", "0", "1/2");
        subtract_helper("-1/2", "0", "-1/2");
        subtract_helper("1/2", "1", "-1/2");
        subtract_helper("-1/2", "1", "-3/2");
        subtract_helper("0", "0", "0");
        subtract_helper("0", "1", "-1");
        subtract_helper("1", "0", "1");
        subtract_helper("1", "1", "0");
    }

    private static void multiply_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().multiply(read(b).get()), output);
    }

    @Test
    public void testMultiply_Rational() {
        multiply_Rational_helper("2/3", "6/7", "4/7");
        multiply_Rational_helper("2/3", "-6/7", "-4/7");
        multiply_Rational_helper("-2/3", "6/7", "-4/7");
        multiply_Rational_helper("-2/3", "-6/7", "4/7");
        multiply_Rational_helper("2/3", "4", "8/3");
        multiply_Rational_helper("2/3", "-4", "-8/3");
        multiply_Rational_helper("-2/3", "4", "-8/3");
        multiply_Rational_helper("-2/3", "-4", "8/3");
        multiply_Rational_helper("3", "5", "15");
        multiply_Rational_helper("3", "-5", "-15");
        multiply_Rational_helper("-3", "5", "-15");
        multiply_Rational_helper("-3", "-5", "15");
        multiply_Rational_helper("1/4", "4", "1");
        multiply_Rational_helper("-1/4", "-4", "1");
        multiply_Rational_helper("2/3", "0", "0");
        multiply_Rational_helper("-2/3", "0", "0");
        multiply_Rational_helper("2/3", "1", "2/3");
        multiply_Rational_helper("-2/3", "1", "-2/3");
        multiply_Rational_helper("0", "0", "0");
        multiply_Rational_helper("0", "1", "0");
        multiply_Rational_helper("1", "0", "0");
        multiply_Rational_helper("1", "1", "1");
    }

    private static void multiply_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().multiply(Readers.readBigInteger(b).get()), output);
    }

    @Test
    public void testMultiply_BigInteger() {
        multiply_BigInteger_helper("2/3", "4", "8/3");
        multiply_BigInteger_helper("2/3", "-4", "-8/3");
        multiply_BigInteger_helper("-2/3", "4", "-8/3");
        multiply_BigInteger_helper("-2/3", "-4", "8/3");
        multiply_BigInteger_helper("2/3", "3", "2");
        multiply_BigInteger_helper("2/3", "-3", "-2");
        multiply_BigInteger_helper("-2/3", "3", "-2");
        multiply_BigInteger_helper("-2/3", "-3", "2");
        multiply_BigInteger_helper("2/3", "0", "0");
        multiply_BigInteger_helper("1/3", "3", "1");
        multiply_BigInteger_helper("-1/3", "-3", "1");
        multiply_BigInteger_helper("2/3", "1", "2/3");
    }

    private static void multiply_int_helper(@NotNull String a, int b, @NotNull String output) {
        aeq(read(a).get().multiply(b), output);
    }

    @Test
    public void testMultiply_int() {
        multiply_int_helper("2/3", 4, "8/3");
        multiply_int_helper("2/3", -4, "-8/3");
        multiply_int_helper("-2/3", 4, "-8/3");
        multiply_int_helper("-2/3", -4, "8/3");
        multiply_int_helper("2/3", 3, "2");
        multiply_int_helper("2/3", -3, "-2");
        multiply_int_helper("-2/3", 3, "-2");
        multiply_int_helper("-2/3", -3, "2");
        multiply_int_helper("2/3", 0, "0");
        multiply_int_helper("1/3", 3, "1");
        multiply_int_helper("-1/3", -3, "1");
        multiply_int_helper("2/3", 1, "2/3");
    }

    private static void invert_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().invert(), output);
    }

    private static void invert_fail_helper(@NotNull String input) {
        try {
            read(input).get().invert();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testInvert() {
        invert_helper("2/3", "3/2");
        invert_helper("-2/3", "-3/2");
        invert_helper("4", "1/4");
        invert_helper("1/4", "4");
        invert_helper("1", "1");
        invert_helper("-1", "-1");
        invert_fail_helper("0");
    }

    private static void divide_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().divide(read(b).get()), output);
    }

    private static void divide_Rational_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().divide(read(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_Rational() {
        divide_Rational_helper("2/3", "6/7", "7/9");
        divide_Rational_helper("2/3", "-6/7", "-7/9");
        divide_Rational_helper("-2/3", "6/7", "-7/9");
        divide_Rational_helper("-2/3", "-6/7", "7/9");
        divide_Rational_helper("2/3", "4", "1/6");
        divide_Rational_helper("2/3", "-4", "-1/6");
        divide_Rational_helper("-2/3", "4", "-1/6");
        divide_Rational_helper("-2/3", "-4", "1/6");
        divide_Rational_helper("3", "5", "3/5");
        divide_Rational_helper("3", "-5", "-3/5");
        divide_Rational_helper("-3", "5", "-3/5");
        divide_Rational_helper("-3", "-5", "3/5");
        divide_Rational_helper("1/4", "4", "1/16");
        divide_Rational_helper("2/3", "2/3", "1");
        divide_Rational_helper("-2/3", "-2/3", "1");
        divide_Rational_helper("2/3", "1", "2/3");
        divide_Rational_helper("-2/3", "1", "-2/3");
        divide_Rational_helper("0", "1", "0");
        divide_Rational_helper("1", "1", "1");
        divide_Rational_fail_helper("2/3", "0");
        divide_Rational_fail_helper("3", "0");
        divide_Rational_fail_helper("1", "0");
        divide_Rational_fail_helper("0", "0");
    }

    private static void divide_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().divide(Readers.readBigInteger(b).get()), output);
    }

    private static void divide_BigInteger_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().divide(Readers.readBigInteger(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void divide_BigInteger() {
        divide_BigInteger_helper("2/3", "4", "1/6");
        divide_BigInteger_helper("2/3", "-4", "-1/6");
        divide_BigInteger_helper("-2/3", "4", "-1/6");
        divide_BigInteger_helper("-2/3", "-4", "1/6");
        divide_BigInteger_helper("2/3", "3", "2/9");
        divide_BigInteger_helper("2/3", "-3", "-2/9");
        divide_BigInteger_helper("-2/3", "3", "-2/9");
        divide_BigInteger_helper("-2/3", "-3", "2/9");
        divide_BigInteger_helper("2/3", "1", "2/3");
        divide_BigInteger_helper("0", "3", "0");
        divide_BigInteger_helper("5", "5", "1");
        divide_BigInteger_helper("-5", "-5", "1");
        divide_BigInteger_fail_helper("2/3", "0");
    }

    private static void divide_int_helper(@NotNull String a, int b, @NotNull String output) {
        aeq(read(a).get().divide(b), output);
    }

    private static void divide_int_fail_helper(@NotNull String a, int b) {
        try {
            read(a).get().divide(b);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void divide_int() {
        divide_int_helper("2/3", 4, "1/6");
        divide_int_helper("2/3", -4, "-1/6");
        divide_int_helper("-2/3", 4, "-1/6");
        divide_int_helper("-2/3", -4, "1/6");
        divide_int_helper("2/3", 3, "2/9");
        divide_int_helper("2/3", -3, "-2/9");
        divide_int_helper("-2/3", 3, "-2/9");
        divide_int_helper("-2/3", -3, "2/9");
        divide_int_helper("2/3", 1, "2/3");
        divide_int_helper("0", 3, "0");
        divide_int_helper("5", 5, "1");
        divide_int_helper("-5", -5, "1");
        divide_int_fail_helper("2/3", 0);
    }

    private static void shiftLeft_helper(@NotNull String r, int bits, @NotNull String output) {
        aeq(read(r).get().shiftLeft(bits), output);
    }

    @Test
    public void testShiftLeft() {
        shiftLeft_helper("7/12", 0, "7/12");
        shiftLeft_helper("7/12", 1, "7/6");
        shiftLeft_helper("7/12", 2, "7/3");
        shiftLeft_helper("7/12", 3, "14/3");
        shiftLeft_helper("7/12", 4, "28/3");
        shiftLeft_helper("7/12", -1, "7/24");
        shiftLeft_helper("7/12", -2, "7/48");
        shiftLeft_helper("7/12", -3, "7/96");
        shiftLeft_helper("7/12", -4, "7/192");

        shiftLeft_helper("4/5", 0, "4/5");
        shiftLeft_helper("4/5", 1, "8/5");
        shiftLeft_helper("4/5", 2, "16/5");
        shiftLeft_helper("4/5", 3, "32/5");
        shiftLeft_helper("4/5", 4, "64/5");
        shiftLeft_helper("4/5", -1, "2/5");
        shiftLeft_helper("4/5", -2, "1/5");
        shiftLeft_helper("4/5", -3, "1/10");
        shiftLeft_helper("4/5", -4, "1/20");

        shiftLeft_helper("0", 4, "0");
        shiftLeft_helper("1", 0, "1");

        shiftLeft_helper("1", 1, "2");
        shiftLeft_helper("1", 2, "4");
        shiftLeft_helper("1", 3, "8");
        shiftLeft_helper("1", 4, "16");
        shiftLeft_helper("1", -1, "1/2");
        shiftLeft_helper("1", -2, "1/4");
        shiftLeft_helper("1", -3, "1/8");
        shiftLeft_helper("1", -4, "1/16");

        shiftLeft_helper("-4/5", 0, "-4/5");
        shiftLeft_helper("-4/5", 1, "-8/5");
        shiftLeft_helper("-4/5", 2, "-16/5");
        shiftLeft_helper("-4/5", 3, "-32/5");
        shiftLeft_helper("-4/5", 4, "-64/5");
        shiftLeft_helper("-4/5", -1, "-2/5");
        shiftLeft_helper("-4/5", -2, "-1/5");
        shiftLeft_helper("-4/5", -3, "-1/10");
        shiftLeft_helper("-4/5", -4, "-1/20");

        shiftLeft_helper("-1", 0, "-1");
        shiftLeft_helper("-1", 1, "-2");
        shiftLeft_helper("-1", 2, "-4");
        shiftLeft_helper("-1", 3, "-8");
        shiftLeft_helper("-1", 4, "-16");
        shiftLeft_helper("-1", -1, "-1/2");
        shiftLeft_helper("-1", -2, "-1/4");
        shiftLeft_helper("-1", -3, "-1/8");
        shiftLeft_helper("-1", -4, "-1/16");
    }

    private static void shiftRight_helper(@NotNull String r, int bits, @NotNull String output) {
        aeq(read(r).get().shiftRight(bits), output);
    }

    @Test
    public void testShiftRight() {
        shiftRight_helper("7/12", 0, "7/12");
        shiftRight_helper("7/12", 1, "7/24");
        shiftRight_helper("7/12", 2, "7/48");
        shiftRight_helper("7/12", 3, "7/96");
        shiftRight_helper("7/12", 4, "7/192");
        shiftRight_helper("7/12", -1, "7/6");
        shiftRight_helper("7/12", -2, "7/3");
        shiftRight_helper("7/12", -3, "14/3");
        shiftRight_helper("7/12", -4, "28/3");

        shiftRight_helper("4/5", 0, "4/5");
        shiftRight_helper("4/5", 1, "2/5");
        shiftRight_helper("4/5", 2, "1/5");
        shiftRight_helper("4/5", 3, "1/10");
        shiftRight_helper("4/5", 4, "1/20");
        shiftRight_helper("4/5", -1, "8/5");
        shiftRight_helper("4/5", -2, "16/5");
        shiftRight_helper("4/5", -3, "32/5");
        shiftRight_helper("4/5", -4, "64/5");

        shiftRight_helper("0", 4, "0");
        shiftRight_helper("1", 0, "1");

        shiftRight_helper("1", 1, "1/2");
        shiftRight_helper("1", 2, "1/4");
        shiftRight_helper("1", 3, "1/8");
        shiftRight_helper("1", 4, "1/16");
        shiftRight_helper("1", -1, "2");
        shiftRight_helper("1", -2, "4");
        shiftRight_helper("1", -3, "8");
        shiftRight_helper("1", -4, "16");

        shiftRight_helper("-4/5", 0, "-4/5");
        shiftRight_helper("-4/5", 1, "-2/5");
        shiftRight_helper("-4/5", 2, "-1/5");
        shiftRight_helper("-4/5", 3, "-1/10");
        shiftRight_helper("-4/5", 4, "-1/20");
        shiftRight_helper("-4/5", -1, "-8/5");
        shiftRight_helper("-4/5", -2, "-16/5");
        shiftRight_helper("-4/5", -3, "-32/5");
        shiftRight_helper("-4/5", -4, "-64/5");

        shiftRight_helper("-1", 0, "-1");
        shiftRight_helper("-1", 1, "-1/2");
        shiftRight_helper("-1", 2, "-1/4");
        shiftRight_helper("-1", 3, "-1/8");
        shiftRight_helper("-1", 4, "-1/16");
        shiftRight_helper("-1", -1, "-2");
        shiftRight_helper("-1", -2, "-4");
        shiftRight_helper("-1", -3, "-8");
        shiftRight_helper("-1", -4, "-16");
    }

    private static void sum_helper(@NotNull String input, @NotNull String output) {
        aeq(sum(readRationalList(input)), output);
    }

    private static void sum_fail_helper(@NotNull String input) {
        try {
            sum(readRationalListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testSum() {
        sum_helper("[]", "0");
        sum_helper("[1]", "1");
        sum_helper("[-4/5]", "-4/5");
        sum_helper("[10, 21/2, 11]", "63/2");
        sum_helper("[-4, 6, -8]", "-6");
        sum_fail_helper("[10, null, 11]");
    }

    private static void product_helper(@NotNull String input, @NotNull String output) {
        aeq(product(readRationalList(input)), output);
    }

    private static void product_fail_helper(@NotNull String input) {
        try {
            product(readRationalListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProduct() {
        product_helper("[]", "1");
        product_helper("[0]", "0");
        product_helper("[-4/5]", "-4/5");
        product_helper("[10, 21/2, 11]", "1155");
        product_helper("[-4, 6, -8]", "192");
        product_fail_helper("[10, null, 11]");
    }

    private static void delta_helper(@NotNull Iterable<Rational> input, @NotNull String output) {
        aeqitLimit(TINY_LIMIT, delta(input), output);
    }

    private static void delta_helper(@NotNull String input, @NotNull String output) {
        delta_helper(readRationalList(input), output);
    }

    private static void delta_fail_helper(@NotNull String input) {
        try {
            toList(delta(readRationalListWithNulls(input)));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testDelta() {
        delta_helper("[3]", "[]");
        delta_helper("[31/10, 41/10, 59/10, 23/10]", "[1, 9/5, -18/5]");
        delta_helper(map(i -> of(i).invert(), rangeUp(1)),
                "[-1/2, -1/6, -1/12, -1/20, -1/30, -1/42, -1/56, -1/72, -1/90, -1/110, -1/132, -1/156, -1/182," +
                " -1/210, -1/240, -1/272, -1/306, -1/342, -1/380, -1/420, ...]");
        delta_fail_helper("[]");
        delta_fail_helper("[10, null, 12]");
    }

    private static void harmonicNumber_helper(int input, @NotNull String output) {
        aeq(harmonicNumber(input), output);
    }

    private static void harmonicNumber_fail_helper(int input) {
        try {
            harmonicNumber(input);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testHarmonicNumber() {
        harmonicNumber_helper(1, "1");
        harmonicNumber_helper(5, "137/60");
        harmonicNumber_helper(10, "7381/2520");
        harmonicNumber_fail_helper(0);
        harmonicNumber_fail_helper(-5);
    }

    private static void pow_helper(@NotNull String r, int p, @NotNull String output) {
        aeq(read(r).get().pow(p), output);
    }

    private static void pow_fail_helper(@NotNull String r, int p) {
        try {
            read(r).get().pow(p);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPow() {
        pow_helper("2/3", 0, "1");
        pow_helper("2/3", 1, "2/3");
        pow_helper("2/3", 2, "4/9");
        pow_helper("2/3", 3, "8/27");
        pow_helper("2/3", -1, "3/2");
        pow_helper("2/3", -2, "9/4");
        pow_helper("2/3", -3, "27/8");

        pow_helper("-2/3", 0, "1");
        pow_helper("-2/3", 1, "-2/3");
        pow_helper("-2/3", 2, "4/9");
        pow_helper("-2/3", 3, "-8/27");
        pow_helper("-2/3", -1, "-3/2");
        pow_helper("-2/3", -2, "9/4");
        pow_helper("-2/3", -3, "-27/8");

        pow_helper("2", 0, "1");
        pow_helper("2", 1, "2");
        pow_helper("2", 2, "4");
        pow_helper("2", 3, "8");
        pow_helper("2", -1, "1/2");
        pow_helper("2", -2, "1/4");
        pow_helper("2", -3, "1/8");

        pow_helper("-2", 0, "1");
        pow_helper("-2", 1, "-2");
        pow_helper("-2", 2, "4");
        pow_helper("-2", 3, "-8");
        pow_helper("-2", -1, "-1/2");
        pow_helper("-2", -2, "1/4");
        pow_helper("-2", -3, "-1/8");

        pow_helper("0", 0, "1");
        pow_helper("0", 1, "0");
        pow_helper("0", 2, "0");
        pow_helper("0", 3, "0");

        pow_helper("1", 0, "1");
        pow_helper("1", 1, "1");
        pow_helper("1", 2, "1");
        pow_helper("1", 3, "1");
        pow_helper("1", -1, "1");
        pow_helper("1", -2, "1");
        pow_helper("1", -3, "1");

        pow_fail_helper("0", -1);
        pow_fail_helper("0", -2);
        pow_fail_helper("0", -3);
    }

    private static void fractionalPart_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().fractionalPart(), output);
    }

    @Test
    public void testFractionalPart() {
        fractionalPart_helper("7/3", "1/3");
        fractionalPart_helper("5/3", "2/3");
        fractionalPart_helper("2/3", "2/3");
        fractionalPart_helper("-1/3", "2/3");
        fractionalPart_helper("-4/3", "2/3");
        fractionalPart_helper("4", "0");
        fractionalPart_helper("-2", "0");
        fractionalPart_helper("0", "0");
        fractionalPart_helper("1", "0");
    }

    private static void roundToDenominator_helper(
            @NotNull Rational r,
            @NotNull String denominator,
            @NotNull String roundingMode,
            @NotNull String output
    ) {
        aeq(
                r.roundToDenominator(
                        Readers.readBigInteger(denominator).get(),
                        Readers.readRoundingMode(roundingMode).get()
                ),
                output
        );
    }

    private static void roundToDenominator_helper(
            @NotNull String r,
            @NotNull String denominator,
            @NotNull String roundingMode,
            @NotNull String output
    ) {
        roundToDenominator_helper(read(r).get(), denominator, roundingMode, output);
    }

    private static void roundToDenominator_fail_helper(
            @NotNull Rational r,
            @NotNull String denominator,
            @NotNull String roundingMode
    ) {
        try {
            r.roundToDenominator(
                    Readers.readBigInteger(denominator).get(),
                    Readers.readRoundingMode(roundingMode).get()
            );
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRoundToDenominator() {
        roundToDenominator_helper(PI_DOUBLE, "1", "HALF_EVEN", "3");
        roundToDenominator_helper(PI_DOUBLE, "2", "HALF_EVEN", "3");
        roundToDenominator_helper(PI_DOUBLE, "3", "HALF_EVEN", "3");
        roundToDenominator_helper(PI_DOUBLE, "4", "HALF_EVEN", "13/4");
        roundToDenominator_helper(PI_DOUBLE, "5", "HALF_EVEN", "16/5");
        roundToDenominator_helper(PI_DOUBLE, "6", "HALF_EVEN", "19/6");
        roundToDenominator_helper(PI_DOUBLE, "7", "HALF_EVEN", "22/7");
        roundToDenominator_helper(PI_DOUBLE, "8", "HALF_EVEN", "25/8");
        roundToDenominator_helper(PI_DOUBLE, "9", "HALF_EVEN", "28/9");
        roundToDenominator_helper(PI_DOUBLE, "10", "HALF_EVEN", "31/10");
        roundToDenominator_helper(PI_DOUBLE, "100", "HALF_EVEN", "157/50");
        roundToDenominator_helper(PI_DOUBLE, "1000", "HALF_EVEN", "1571/500");
        roundToDenominator_helper("3/10", "30", "UNNECESSARY", "3/10");
        roundToDenominator_fail_helper(PI_DOUBLE, "0", "HALF_EVEN");
        roundToDenominator_fail_helper(PI_DOUBLE, "-1", "HALF_EVEN");
        roundToDenominator_fail_helper(PI_DOUBLE, "7", "UNNECESSARY");
    }

    private static void continuedFraction_helper(@NotNull Rational input, @NotNull String output) {
        aeqit(input.continuedFraction(), output);
    }

    private static void continuedFraction_helper(@NotNull String input, @NotNull String output) {
        continuedFraction_helper(read(input).get(), output);
    }

    @Test
    public void testContinuedFraction() {
        continuedFraction_helper("0", "[0]");
        continuedFraction_helper("1", "[1]");
        continuedFraction_helper("5", "[5]");
        continuedFraction_helper("-5", "[-5]");
        continuedFraction_helper("1/2", "[0, 2]");
        continuedFraction_helper("-1/2", "[-1, 2]");
        continuedFraction_helper("415/93", "[4, 2, 6, 7]");
        continuedFraction_helper("-415/93", "[-5, 1, 1, 6, 7]");
        continuedFraction_helper(ofExact(Math.sqrt(2)).get(),
                "[1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 2, 7, 1, 2, 33, 2, 7, 5," +
                " 2, 1, 1, 16, 2]");
        continuedFraction_helper(ofExact(-Math.sqrt(2)).get(),
                "[-2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 2, 7, 1, 2, 33, 2, 7," +
                " 5, 2, 1, 1, 16, 2]");
        continuedFraction_helper(PI_DOUBLE,
                "[3, 7, 15, 1, 292, 1, 1, 1, 2, 1, 3, 1, 14, 3, 3, 2, 1, 3, 3, 7, 2, 1, 1, 3, 2, 42, 2]");
        continuedFraction_helper(PI_DOUBLE.negate(),
                "[-4, 1, 6, 15, 1, 292, 1, 1, 1, 2, 1, 3, 1, 14, 3, 3, 2, 1, 3, 3, 7, 2, 1, 1, 3, 2, 42, 2]");
        continuedFraction_helper(ofExact(Math.E).get(),
                "[2, 1, 2, 1, 1, 4, 1, 1, 6, 1, 1, 8, 1, 1, 10, 1, 1, 12, 1, 1, 11, 1, 1, 1, 11, 5, 1, 1, 2, 1, 4," +
                " 2, 1, 1, 9, 17, 3]");
        continuedFraction_helper(ofExact(-Math.E).get(),
                "[-3, 3, 1, 1, 4, 1, 1, 6, 1, 1, 8, 1, 1, 10, 1, 1, 12, 1, 1, 11, 1, 1, 1, 11, 5, 1, 1, 2, 1, 4, 2," +
                " 1, 1, 9, 17, 3]");
    }

    private static void fromContinuedFraction_helper(@NotNull String input, @NotNull String output) {
        aeq(fromContinuedFraction(readBigIntegerList(input)), output);
    }

    private static void fromContinuedFraction_fail_helper(@NotNull String input) {
        try {
            fromContinuedFraction(readBigIntegerList(input));
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromContinuedFraction() {
        fromContinuedFraction_helper("[1]", "1");
        fromContinuedFraction_helper("[0, 2]", "1/2");
        fromContinuedFraction_helper("[-1, 2]", "-1/2");
        fromContinuedFraction_helper("[4, 2, 6, 7]", "415/93");
        fromContinuedFraction_helper("[-5, 1, 1, 6, 7]", "-415/93");
        aeqf(fromContinuedFraction(readBigIntegerList("[0, 1, 2, 3, 4, 5, 6, 7, 8]")).floatValue(), 0.69777465f);
        fromContinuedFraction_fail_helper("[]");
    }

    private static void convergentsHelper(@NotNull String x, @NotNull String output) {
        aeqit(read(x).get().convergents(), output);
    }

    private static void convergentsHelper(double x, @NotNull String output) {
        aeqit(ofExact(x).get().convergents(), output);
    }

    @Test
    public void testConvergents() {
        convergentsHelper("0", "[0]");
        convergentsHelper("1", "[1]");
        convergentsHelper("5", "[5]");
        convergentsHelper("-5", "[-5]");
        convergentsHelper("1/2", "[0, 1/2]");
        convergentsHelper("-1/2", "[-1, -1/2]");
        convergentsHelper("415/93", "[4, 9/2, 58/13, 415/93]");
        convergentsHelper("-415/93", "[-5, -4, -9/2, -58/13, -415/93]");
        convergentsHelper(Math.sqrt(2),
                "[1, 3/2, 7/5, 17/12, 41/29, 99/70, 239/169, 577/408, 1393/985, 3363/2378, 8119/5741, 19601/13860," +
                " 47321/33461, 114243/80782, 275807/195025, 665857/470832, 1607521/1136689, 3880899/2744210," +
                " 9369319/6625109, 22619537/15994428, 54608393/38613965, 77227930/54608393, 131836323/93222358," +
                " 209064253/147830751, 549964829/388883860, 4058818056/2870017771, 4608782885/3258901631," +
                " 13276383826/9387821033, 442729449143/313056995720, 898735282112/635501812473," +
                " 6733876423927/4761569683031, 34568117401747/24443350227628, 75870111227421/53648270138287," +
                " 110438228629168/78091620365915, 186308339856589/131739890504202," +
                " 3091371666334592/2185929868433147, 6369051672525773/4503599627370496]");
        convergentsHelper(-Math.sqrt(2),
                "[-2, -1, -3/2, -7/5, -17/12, -41/29, -99/70, -239/169, -577/408, -1393/985, -3363/2378, -8119/5741," +
                " -19601/13860, -47321/33461, -114243/80782, -275807/195025, -665857/470832, -1607521/1136689," +
                " -3880899/2744210, -9369319/6625109, -22619537/15994428, -54608393/38613965, -77227930/54608393," +
                " -131836323/93222358, -209064253/147830751, -549964829/388883860, -4058818056/2870017771," +
                " -4608782885/3258901631, -13276383826/9387821033, -442729449143/313056995720," +
                " -898735282112/635501812473, -6733876423927/4761569683031, -34568117401747/24443350227628," +
                " -75870111227421/53648270138287, -110438228629168/78091620365915, -186308339856589/131739890504202," +
                " -3091371666334592/2185929868433147, -6369051672525773/4503599627370496]");
        convergentsHelper(Math.PI,
                "[3, 22/7, 333/106, 355/113, 103993/33102, 104348/33215, 208341/66317, 312689/99532, 833719/265381," +
                " 1146408/364913, 4272943/1360120, 5419351/1725033, 80143857/25510582, 245850922/78256779," +
                " 817696623/260280919, 1881244168/598818617, 2698940791/859099536, 9978066541/3176117225," +
                " 32633140414/10387451211, 238410049439/75888275702, 509453239292/162164002615," +
                " 747863288731/238052278317, 1257316528023/400216280932, 4519812872800/1438701121113," +
                " 10296942273623/3277618523158, 436991388364966/139098679093749, 884279719003555/281474976710656]");
        convergentsHelper(-Math.PI,
                "[-4, -3, -22/7, -333/106, -355/113, -103993/33102, -104348/33215, -208341/66317, -312689/99532," +
                " -833719/265381, -1146408/364913, -4272943/1360120, -5419351/1725033, -80143857/25510582," +
                " -245850922/78256779, -817696623/260280919, -1881244168/598818617, -2698940791/859099536," +
                " -9978066541/3176117225, -32633140414/10387451211, -238410049439/75888275702," +
                " -509453239292/162164002615, -747863288731/238052278317, -1257316528023/400216280932," +
                " -4519812872800/1438701121113, -10296942273623/3277618523158, -436991388364966/139098679093749," +
                " -884279719003555/281474976710656]");
        convergentsHelper(Math.E,
                "[2, 3, 8/3, 11/4, 19/7, 87/32, 106/39, 193/71, 1264/465, 1457/536, 2721/1001, 23225/8544," +
                " 25946/9545, 49171/18089, 517656/190435, 566827/208524, 1084483/398959, 13580623/4996032," +
                " 14665106/5394991, 28245729/10391023, 325368125/119696244, 353613854/130087267," +
                " 678981979/249783511, 1032595833/379870778, 12037536142/4428362069, 61220276543/22521681123," +
                " 73257812685/26950043192, 134478089228/49471724315, 342213991141/125893491822," +
                " 476692080369/175365216137, 2248982312617/827354356370, 4974656705603/1830073928877," +
                " 7223639018220/2657428285247, 12198295723823/4487502214124, 117008300532627/43044948212363," +
                " 2001339404778482/736251621824295, 6121026514868073/2251799813685248]");
        convergentsHelper(-Math.E,
                "[-3, -8/3, -11/4, -19/7, -87/32, -106/39, -193/71, -1264/465, -1457/536, -2721/1001, -23225/8544," +
                " -25946/9545, -49171/18089, -517656/190435, -566827/208524, -1084483/398959, -13580623/4996032," +
                " -14665106/5394991, -28245729/10391023, -325368125/119696244, -353613854/130087267," +
                " -678981979/249783511, -1032595833/379870778, -12037536142/4428362069, -61220276543/22521681123," +
                " -73257812685/26950043192, -134478089228/49471724315, -342213991141/125893491822," +
                " -476692080369/175365216137, -2248982312617/827354356370, -4974656705603/1830073928877," +
                " -7223639018220/2657428285247, -12198295723823/4487502214124, -117008300532627/43044948212363," +
                " -2001339404778482/736251621824295, -6121026514868073/2251799813685248]");
    }

    private static void positionalNotation_helper(
            @NotNull Rational r,
            @NotNull String base,
            @NotNull String beforeDecimalPoint,
            @NotNull String nonrepeating,
            @NotNull String repeating
    ) {
        Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>> result =
                r.positionalNotation(Readers.readBigInteger(base).get());
        aeq(result.a, beforeDecimalPoint);
        aeq(result.b, nonrepeating);
        aeq(result.c, repeating);
    }

    private static void positionalNotation_helper(
            @NotNull String r,
            @NotNull String base,
            @NotNull String beforeDecimalPoint,
            @NotNull String nonrepeating,
            @NotNull String repeating
    ) {
        positionalNotation_helper(read(r).get(), base, beforeDecimalPoint, nonrepeating, repeating);
    }

    private static void positionalNotation_fail_helper(@NotNull String r, @NotNull String base) {
        try {
            read(r).get().positionalNotation(Readers.readBigInteger(base).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPositionalNotation() {
        positionalNotation_helper("0", "2", "[]", "[]", "[0]");
        positionalNotation_helper("0", "3", "[]", "[]", "[0]");
        positionalNotation_helper("0", "4", "[]", "[]", "[0]");
        positionalNotation_helper("0", "10", "[]", "[]", "[0]");
        positionalNotation_helper("0", "16", "[]", "[]", "[0]");
        positionalNotation_helper("0", "83", "[]", "[]", "[0]");
        positionalNotation_helper("0", "100", "[]", "[]", "[0]");

        positionalNotation_helper("1", "2", "[1]", "[]", "[0]");
        positionalNotation_helper("1", "3", "[1]", "[]", "[0]");
        positionalNotation_helper("1", "4", "[1]", "[]", "[0]");
        positionalNotation_helper("1", "10", "[1]", "[]", "[0]");
        positionalNotation_helper("1", "16", "[1]", "[]", "[0]");
        positionalNotation_helper("1", "83", "[1]", "[]", "[0]");
        positionalNotation_helper("1", "100", "[1]", "[]", "[0]");

        positionalNotation_helper("1/2", "2", "[]", "[1]", "[0]");
        positionalNotation_helper("1/2", "3", "[]", "[]", "[1]");
        positionalNotation_helper("1/2", "4", "[]", "[2]", "[0]");
        positionalNotation_helper("1/2", "10", "[]", "[5]", "[0]");
        positionalNotation_helper("1/2", "16", "[]", "[8]", "[0]");
        positionalNotation_helper("1/2", "83", "[]", "[]", "[41]");
        positionalNotation_helper("1/2", "100", "[]", "[50]", "[0]");

        positionalNotation_helper("1/3", "2", "[]", "[]", "[0, 1]");
        positionalNotation_helper("1/3", "3", "[]", "[1]", "[0]");
        positionalNotation_helper("1/3", "4", "[]", "[]", "[1]");
        positionalNotation_helper("1/3", "10", "[]", "[]", "[3]");
        positionalNotation_helper("1/3", "16", "[]", "[]", "[5]");
        positionalNotation_helper("1/3", "83", "[]", "[]", "[27, 55]");
        positionalNotation_helper("1/3", "100", "[]", "[]", "[33]");

        positionalNotation_helper("1/7", "2", "[]", "[]", "[0, 0, 1]");
        positionalNotation_helper("1/7", "3", "[]", "[]", "[0, 1, 0, 2, 1, 2]");
        positionalNotation_helper("1/7", "4", "[]", "[]", "[0, 2, 1]");
        positionalNotation_helper("1/7", "10", "[]", "[]", "[1, 4, 2, 8, 5, 7]");
        positionalNotation_helper("1/7", "16", "[]", "[]", "[2, 4, 9]");
        positionalNotation_helper("1/7", "83", "[]", "[]", "[11, 71]");
        positionalNotation_helper("1/7", "100", "[]", "[]", "[14, 28, 57]");

        positionalNotation_helper("415/93", "2", "[1, 0, 0]", "[]", "[0, 1, 1, 1, 0, 1, 1, 0, 0, 1]");
        positionalNotation_helper("415/93", "3", "[1, 1]", "[1]",
                "[1, 0, 1, 1, 1, 0, 0, 1, 2, 0, 2, 0, 0, 0, 2, 1, 2, 1, 1, 1, 2, 2, 1, 0, 2, 0, 2, 2, 2, 0]");
        positionalNotation_helper("415/93", "4", "[1, 0]", "[]", "[1, 3, 1, 2, 1]");
        positionalNotation_helper("415/93", "10", "[4]", "[]", "[4, 6, 2, 3, 6, 5, 5, 9, 1, 3, 9, 7, 8, 4, 9]");
        positionalNotation_helper("415/93", "16", "[4]", "[]", "[7, 6, 5, 13, 9]");
        positionalNotation_helper("415/93", "83", "[4]", "[]",
                "[38, 31, 19, 52, 54, 36, 49, 7, 11, 49, 81, 17, 70, 41, 78, 44, 51, 63, 30, 28, 46, 33, 75, 71, 33," +
                " 1, 65, 12, 41, 4]");
        positionalNotation_helper("415/93", "100", "[4]", "[]",
                "[46, 23, 65, 59, 13, 97, 84, 94, 62, 36, 55, 91, 39, 78, 49]");

        positionalNotation_helper(
                PI_DOUBLE,
                "2",
                "[1, 1]",
                "[0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1," +
                " 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1]",
                "[0]"
        );
        positionalNotation_helper(
                PI_DOUBLE,
                "4",
                "[3]",
                "[0, 2, 1, 0, 0, 3, 3, 3, 1, 2, 2, 2, 2, 0, 2, 0, 2, 0, 1, 1, 2, 2, 0, 3]",
                "[0]"
        );
        positionalNotation_helper(
                PI_DOUBLE,
                "10",
                "[3]",
                "[1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 1, 1, 5, 9, 9, 7, 9, 6, 3, 4, 6, 8, 5, 4, 4, 1, 8, 5," +
                " 1, 6, 1, 5, 9, 0, 5, 7, 6, 1, 7, 1, 8, 7, 5]",
                "[0]"
        );
        positionalNotation_helper(PI_DOUBLE, "16", "[3]", "[2, 4, 3, 15, 6, 10, 8, 8, 8, 5, 10, 3]", "[0]");
        positionalNotation_helper(
                PI_DOUBLE,
                "100",
                "[3]",
                "[14, 15, 92, 65, 35, 89, 79, 31, 15, 99, 79, 63, 46, 85, 44, 18, 51, 61, 59, 5, 76, 17, 18, 75]",
                "[0]"
        );

        positionalNotation_helper("299/56", "2", "[1, 0, 1]", "[0, 1, 0]", "[1, 0, 1]");
        positionalNotation_helper("405/26", "3", "[1, 2, 0]", "[]", "[1, 2, 0]");
        positionalNotation_helper("15613/576", "4", "[1, 2, 3]", "[0, 1, 2]", "[3, 0, 1]");
        positionalNotation_helper("41111111/333000", "10", "[1, 2, 3]", "[4, 5, 6]", "[7, 8, 9]");
        positionalNotation_helper("1628508433/5591040", "16", "[1, 2, 3]", "[4, 5, 6]", "[7, 8, 9]");
        positionalNotation_helper("1153778558235787/163469900791", "83", "[1, 2, 3]", "[4, 5, 6]", "[7, 8, 9]");
        positionalNotation_helper("3401010101010101/333333000000", "100", "[1, 2, 3]", "[4, 5, 6]", "[7, 8, 9]");

        positionalNotation_fail_helper("1/2", "1");
        positionalNotation_fail_helper("1/2", "0");
        positionalNotation_fail_helper("-1/2", "2");
    }

    private static void fromPositionalNotation_helper(
            @NotNull String base,
            @NotNull String beforeDecimalPoint,
            @NotNull String nonrepeating,
            @NotNull String repeating,
            @NotNull Object output
    ) {
        aeq(
                fromPositionalNotation(
                        Readers.readBigInteger(base).get(),
                        readBigIntegerList(beforeDecimalPoint),
                        readBigIntegerList(nonrepeating),
                        readBigIntegerList(repeating)
                ),
                output
        );
    }

    private static void fromPositionalNotation_fail_helper(
            @NotNull String base,
            @NotNull String beforeDecimalPoint,
            @NotNull String nonrepeating,
            @NotNull String repeating
    ) {
        try {
            fromPositionalNotation(
                    Readers.readBigInteger(base).get(),
                    readBigIntegerList(beforeDecimalPoint),
                    readBigIntegerList(nonrepeating),
                    readBigIntegerList(repeating)
            );
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromPositionalNotation() {
        fromPositionalNotation_helper("2", "[]", "[]", "[0]", "0");
        fromPositionalNotation_helper("3", "[]", "[]", "[0]", "0");
        fromPositionalNotation_helper("4", "[]", "[]", "[0]", "0");
        fromPositionalNotation_helper("10", "[]", "[]", "[0]", "0");
        fromPositionalNotation_helper("16", "[]", "[]", "[0]", "0");
        fromPositionalNotation_helper("83", "[]", "[]", "[0]", "0");
        fromPositionalNotation_helper("100", "[]", "[]", "[0]", "0");

        fromPositionalNotation_helper("2", "[1]", "[]", "[0]", "1");
        fromPositionalNotation_helper("3", "[1]", "[]", "[0]", "1");
        fromPositionalNotation_helper("4", "[1]", "[]", "[0]", "1");
        fromPositionalNotation_helper("10", "[1]", "[]", "[0]", "1");
        fromPositionalNotation_helper("16", "[1]", "[]", "[0]", "1");
        fromPositionalNotation_helper("83", "[1]", "[]", "[0]", "1");
        fromPositionalNotation_helper("100", "[1]", "[]", "[0]", "1");

        fromPositionalNotation_helper("2", "[]", "[1]", "[0]", "1/2");
        fromPositionalNotation_helper("3", "[]", "[]", "[1]", "1/2");
        fromPositionalNotation_helper("4", "[]", "[2]", "[0]", "1/2");
        fromPositionalNotation_helper("10", "[]", "[5]", "[0]", "1/2");
        fromPositionalNotation_helper("16", "[]", "[8]", "[0]", "1/2");
        fromPositionalNotation_helper("83", "[]", "[]", "[41]", "1/2");
        fromPositionalNotation_helper("100", "[]", "[50]", "[0]", "1/2");

        fromPositionalNotation_helper("2", "[]", "[]", "[0, 1]", "1/3");
        fromPositionalNotation_helper("3", "[]", "[1]", "[0]", "1/3");
        fromPositionalNotation_helper("4", "[]", "[]", "[1]", "1/3");
        fromPositionalNotation_helper("10", "[]", "[]", "[3]", "1/3");
        fromPositionalNotation_helper("16", "[]", "[]", "[5]", "1/3");
        fromPositionalNotation_helper("83", "[]", "[]", "[27, 55]", "1/3");
        fromPositionalNotation_helper("100", "[]", "[]", "[33]", "1/3");

        fromPositionalNotation_helper("2", "[]", "[]", "[0, 0, 1]", "1/7");
        fromPositionalNotation_helper("3", "[]", "[]", "[0, 1, 0, 2, 1, 2]", "1/7");
        fromPositionalNotation_helper("4", "[]", "[]", "[0, 2, 1]", "1/7");
        fromPositionalNotation_helper("10", "[]", "[]", "[1, 4, 2, 8, 5, 7]", "1/7");
        fromPositionalNotation_helper("16", "[]", "[]", "[2, 4, 9]", "1/7");
        fromPositionalNotation_helper("83", "[]", "[]", "[11, 71]", "1/7");
        fromPositionalNotation_helper("100", "[]", "[]", "[14, 28, 57]", "1/7");

        fromPositionalNotation_helper("2", "[1, 0, 0]", "[]", "[0, 1, 1, 1, 0, 1, 1, 0, 0, 1]", "415/93");
        fromPositionalNotation_helper(
                "3",
                "[1, 1]",
                "[1]",
                "[1, 0, 1, 1, 1, 0, 0, 1, 2, 0, 2, 0, 0, 0, 2, 1, 2, 1, 1, 1, 2, 2, 1, 0, 2, 0, 2, 2, 2, 0]",
                "415/93"
        );
        fromPositionalNotation_helper("4", "[1, 0]", "[]", "[1, 3, 1, 2, 1]", "415/93");
        fromPositionalNotation_helper("10", "[4]", "[]", "[4, 6, 2, 3, 6, 5, 5, 9, 1, 3, 9, 7, 8, 4, 9]", "415/93");
        fromPositionalNotation_helper("16", "[4]", "[]", "[7, 6, 5, 13, 9]", "415/93");
        fromPositionalNotation_helper(
                "83",
                "[4]",
                "[]",
                "[38, 31, 19, 52, 54, 36, 49, 7, 11, 49, 81, 17, 70, 41, 78, 44, 51, 63, 30, 28, 46, 33, 75, 71, 33," +
                " 1, 65, 12, 41, 4]",
                "415/93"
        );
        fromPositionalNotation_helper(
                "100",
                "[4]",
                "[]",
                "[46, 23, 65, 59, 13, 97, 84, 94, 62, 36, 55, 91, 39, 78, 49]",
                "415/93"
        );

        fromPositionalNotation_helper(
                "2",
                "[1, 1]",
                "[0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1," +
                " 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 1]",
                "[0]",
                PI_DOUBLE
        );
        fromPositionalNotation_helper(
                "4",
                "[3]",
                "[0, 2, 1, 0, 0, 3, 3, 3, 1, 2, 2, 2, 2, 0, 2, 0, 2, 0, 1, 1, 2, 2, 0, 3]",
                "[0]",
                PI_DOUBLE
        );
        fromPositionalNotation_helper(
                "10",
                "[3]",
                "[1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 1, 1, 5, 9, 9, 7, 9, 6, 3, 4, 6, 8, 5, 4, 4, 1, 8, 5," +
                " 1, 6, 1, 5, 9, 0, 5, 7, 6, 1, 7, 1, 8, 7, 5]",
                "[0]",
                PI_DOUBLE
        );
        fromPositionalNotation_helper("16", "[3]", "[2, 4, 3, 15, 6, 10, 8, 8, 8, 5, 10, 3]", "[0]", PI_DOUBLE);
        fromPositionalNotation_helper(
                "100",
                "[3]",
                "[14, 15, 92, 65, 35, 89, 79, 31, 15, 99, 79, 63, 46, 85, 44, 18, 51, 61, 59, 5, 76, 17, 18, 75]",
                "[0]",
                PI_DOUBLE
        );

        fromPositionalNotation_helper("2", "[1, 0, 1]", "[0, 1, 0]", "[1, 0, 1]", "299/56");
        fromPositionalNotation_helper("3", "[1, 2, 0]", "[]", "[1, 2, 0]", "405/26");
        fromPositionalNotation_helper("4", "[1, 2, 3]", "[0, 1, 2]", "[3, 0, 1]", "15613/576");
        fromPositionalNotation_helper("10", "[1, 2, 3]", "[4, 5, 6]", "[7, 8, 9]", "41111111/333000");
        fromPositionalNotation_helper("16", "[1, 2, 3]", "[4, 5, 6]", "[7, 8, 9]", "1628508433/5591040");
        fromPositionalNotation_helper("83", "[1, 2, 3]", "[4, 5, 6]", "[7, 8, 9]", "1153778558235787/163469900791");
        fromPositionalNotation_helper("100", "[1, 2, 3]", "[4, 5, 6]", "[7, 8, 9]", "3401010101010101/333333000000");

        fromPositionalNotation_fail_helper("1", "[1, 0, 1]", "[0, 1, 0]", "[1, 0, 1]");
        fromPositionalNotation_fail_helper("0", "[1, 0, 1]", "[0, 1, 0]", "[1, 0, 1]");
        fromPositionalNotation_fail_helper("-1", "[1, 0, 1]", "[0, 1, 0]", "[1, 0, 1]");
        fromPositionalNotation_fail_helper("2", "[-1, 0, 1]", "[0, 1, 0]", "[1, 0, 1]");
        fromPositionalNotation_fail_helper("2", "[2, 0, 1]", "[0, 1, 0]", "[1, 0, 1]");
        fromPositionalNotation_fail_helper("2", "[1, 0, 1]", "[0, -1, 0]", "[1, 0, 1]");
        fromPositionalNotation_fail_helper("2", "[1, 0, 1]", "[0, 2, 0]", "[1, 0, 1]");
        fromPositionalNotation_fail_helper("2", "[1, 0, 1]", "[0, 1, 0]", "[-1, 0, 1]");
        fromPositionalNotation_fail_helper("2", "[1, 0, 1]", "[0, 1, 0]", "[2, 0, 1]");
        fromPositionalNotation_fail_helper("2", "[1, 0, 1]", "[0, 1, 0]", "[]");
    }

    private static void digitsHelper(
            @NotNull Rational r,
            @NotNull String base,
            @NotNull String beforeDecimal,
            @NotNull String afterDecimal
    ) {
        Pair<List<BigInteger>, Iterable<BigInteger>> digits = r.digits(Readers.readBigInteger(base).get());
        aeq(digits.a, beforeDecimal);
        aeqitLimit(TINY_LIMIT, digits.b, afterDecimal);
    }

    private static void digitsHelper(
            @NotNull String r,
            @NotNull String base,
            @NotNull String beforeDecimal,
            @NotNull String afterDecimal
    ) {
        digitsHelper(read(r).get(), base, beforeDecimal, afterDecimal);
    }

    private static void digitsFail(@NotNull String r, @NotNull String base) {
        try {
            read(r).get().digits(Readers.readBigInteger(base).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testDigits() {
        digitsHelper("0", "2", "[]", "[]");
        digitsHelper("0", "3", "[]", "[]");
        digitsHelper("0", "4", "[]", "[]");
        digitsHelper("0", "10", "[]", "[]");
        digitsHelper("0", "16", "[]", "[]");
        digitsHelper("0", "83", "[]", "[]");
        digitsHelper("0", "100", "[]", "[]");

        digitsHelper("1", "2", "[1]", "[]");
        digitsHelper("1", "3", "[1]", "[]");
        digitsHelper("1", "4", "[1]", "[]");
        digitsHelper("1", "10", "[1]", "[]");
        digitsHelper("1", "16", "[1]", "[]");
        digitsHelper("1", "83", "[1]", "[]");
        digitsHelper("1", "100", "[1]", "[]");

        digitsHelper("1/2", "2", "[]", "[1]");
        digitsHelper("1/2", "3", "[]", "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]");
        digitsHelper("1/2", "4", "[]", "[2]");
        digitsHelper("1/2", "10", "[]", "[5]");
        digitsHelper("1/2", "16", "[]", "[8]");
        digitsHelper(
                "1/2",
                "83",
                "[]",
                "[41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, ...]"
        );

        digitsHelper("1/2", "100", "[]", "[50]");
        digitsHelper("1/3", "2", "[]", "[0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, ...]");
        digitsHelper("1/3", "3", "[]", "[1]");
        digitsHelper("1/3", "4", "[]", "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]");
        digitsHelper("1/3", "10", "[]", "[3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, ...]");
        digitsHelper("1/3", "16", "[]", "[5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, ...]");
        digitsHelper(
                "1/3",
                "83",
                "[]",
                "[27, 55, 27, 55, 27, 55, 27, 55, 27, 55, 27, 55, 27, 55, 27, 55, 27, 55, 27, 55, ...]"
        );
        digitsHelper(
                "1/3",
                "100",
                "[]",
                "[33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, ...]"
        );

        digitsHelper("1/7", "2", "[]", "[0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, ...]");
        digitsHelper("1/7", "3", "[]", "[0, 1, 0, 2, 1, 2, 0, 1, 0, 2, 1, 2, 0, 1, 0, 2, 1, 2, 0, 1, ...]");
        digitsHelper("1/7", "4", "[]", "[0, 2, 1, 0, 2, 1, 0, 2, 1, 0, 2, 1, 0, 2, 1, 0, 2, 1, 0, 2, ...]");
        digitsHelper("1/7", "10", "[]", "[1, 4, 2, 8, 5, 7, 1, 4, 2, 8, 5, 7, 1, 4, 2, 8, 5, 7, 1, 4, ...]");
        digitsHelper("1/7", "16", "[]", "[2, 4, 9, 2, 4, 9, 2, 4, 9, 2, 4, 9, 2, 4, 9, 2, 4, 9, 2, 4, ...]");
        digitsHelper(
                "1/7",
                "83",
                "[]",
                "[11, 71, 11, 71, 11, 71, 11, 71, 11, 71, 11, 71, 11, 71, 11, 71, 11, 71, 11, 71, ...]"
        );
        digitsHelper(
                "1/7",
                "100",
                "[]",
                "[14, 28, 57, 14, 28, 57, 14, 28, 57, 14, 28, 57, 14, 28, 57, 14, 28, 57, 14, 28, ...]"
        );

        digitsHelper("415/93", "2", "[1, 0, 0]", "[0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, ...]");
        digitsHelper("415/93", "3", "[1, 1]", "[1, 1, 0, 1, 1, 1, 0, 0, 1, 2, 0, 2, 0, 0, 0, 2, 1, 2, 1, 1, ...]");
        digitsHelper("415/93", "4", "[1, 0]", "[1, 3, 1, 2, 1, 1, 3, 1, 2, 1, 1, 3, 1, 2, 1, 1, 3, 1, 2, 1, ...]");
        digitsHelper("415/93", "10", "[4]", "[4, 6, 2, 3, 6, 5, 5, 9, 1, 3, 9, 7, 8, 4, 9, 4, 6, 2, 3, 6, ...]");
        digitsHelper("415/93", "16", "[4]", "[7, 6, 5, 13, 9, 7, 6, 5, 13, 9, 7, 6, 5, 13, 9, 7, 6, 5, 13, 9, ...]");
        digitsHelper(
                "415/93",
                "83",
                "[4]",
                "[38, 31, 19, 52, 54, 36, 49, 7, 11, 49, 81, 17, 70, 41, 78, 44, 51, 63, 30, 28, ...]"
        );
        digitsHelper(
                "415/93",
                "100",
                "[4]",
                "[46, 23, 65, 59, 13, 97, 84, 94, 62, 36, 55, 91, 39, 78, 49, 46, 23, 65, 59, 13, ...]"
        );

        digitsHelper(PI_DOUBLE, "2", "[1, 1]", "[0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, ...]");
        digitsHelper(PI_DOUBLE, "3", "[1, 0]", "[0, 1, 0, 2, 1, 1, 0, 1, 2, 2, 2, 2, 0, 1, 0, 2, 1, 1, 0, 0, ...]");
        digitsHelper(PI_DOUBLE, "4", "[3]", "[0, 2, 1, 0, 0, 3, 3, 3, 1, 2, 2, 2, 2, 0, 2, 0, 2, 0, 1, 1, ...]");
        digitsHelper(PI_DOUBLE, "10", "[3]", "[1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 1, 1, 5, 9, 9, ...]");
        digitsHelper(PI_DOUBLE, "16", "[3]", "[2, 4, 3, 15, 6, 10, 8, 8, 8, 5, 10, 3]");
        digitsHelper(
                PI_DOUBLE,
                "83",
                "[3]",
                "[11, 62, 35, 69, 50, 19, 79, 18, 11, 8, 60, 35, 10, 62, 20, 58, 42, 14, 31, 34, ...]"
        );
        digitsHelper(
                PI_DOUBLE,
                "100",
                "[3]",
                "[14, 15, 92, 65, 35, 89, 79, 31, 15, 99, 79, 63, 46, 85, 44, 18, 51, 61, 59, 5, ...]"
        );

        digitsHelper("299/56", "2", "[1, 0, 1]", "[0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, ...]");
        digitsHelper("405/26", "3", "[1, 2, 0]", "[1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, ...]");
        digitsHelper(
                "15613/576",
                "4",
                "[1, 2, 3]",
                "[0, 1, 2, 3, 0, 1, 3, 0, 1, 3, 0, 1, 3, 0, 1, 3, 0, 1, 3, 0, ...]"
        );
        digitsHelper(
                "41111111/333000",
                "10",
                "[1, 2, 3]",
                "[4, 5, 6, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, ...]"
        );
        digitsHelper(
                "1628508433/5591040",
                "16",
                "[1, 2, 3]",
                "[4, 5, 6, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, ...]"
        );
        digitsHelper(
                "1153778558235787/163469900791",
                "83",
                "[1, 2, 3]",
                "[4, 5, 6, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, ...]"
        );
        digitsHelper(
                "3401010101010101/333333000000",
                "100",
                "[1, 2, 3]",
                "[4, 5, 6, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, 9, 7, 8, ...]"
        );

        digitsFail("-1/2", "2");
        digitsFail("1/2", "1");
        digitsFail("1/2", "0");
        digitsFail("1/2", "-1");
    }

    private static void toStringBase_BigInteger_helper(
            @NotNull Rational r,
            @NotNull String base,
            @NotNull String output
    ) {
        aeq(r.toStringBase(Readers.readBigInteger(base).get()), output);
    }

    private static void toStringBase_BigInteger_helper(
            @NotNull String r,
            @NotNull String base,
            @NotNull String output
    ) {
        toStringBase_BigInteger_helper(read(r).get(), base, output);
    }

    private static void toStringBase_BigInteger_fail_helper(@NotNull String r, @NotNull String base) {
        try {
            read(r).get().toStringBase(Readers.readBigInteger(base).get());
            fail();
        } catch (IllegalArgumentException | ArithmeticException ignored) {}
    }

    @Test
    public void testToStringBase_BigInteger() {
        toStringBase_BigInteger_helper("0", "2", "0");
        toStringBase_BigInteger_helper("0", "3", "0");
        toStringBase_BigInteger_helper("0", "4", "0");
        toStringBase_BigInteger_helper("0", "10", "0");
        toStringBase_BigInteger_helper("0", "16", "0");
        toStringBase_BigInteger_helper("0", "83", "(0)");
        toStringBase_BigInteger_helper("0", "100", "(0)");

        toStringBase_BigInteger_helper("1", "2", "1");
        toStringBase_BigInteger_helper("1", "3", "1");
        toStringBase_BigInteger_helper("1", "4", "1");
        toStringBase_BigInteger_helper("1", "10", "1");
        toStringBase_BigInteger_helper("1", "16", "1");
        toStringBase_BigInteger_helper("1", "83", "(1)");
        toStringBase_BigInteger_helper("1", "100", "(1)");

        toStringBase_BigInteger_helper("-1/2", "2", "-0.1");
        toStringBase_BigInteger_helper("-1/2", "4", "-0.2");
        toStringBase_BigInteger_helper("-1/2", "10", "-0.5");
        toStringBase_BigInteger_helper("-1/2", "16", "-0.8");
        toStringBase_BigInteger_helper("-1/2", "100", "-(0).(50)");

        toStringBase_BigInteger_helper("1/3", "3", "0.1");

        toStringBase_BigInteger_helper(PI_DOUBLE, "2", "11.001001000011111101101010100010001000010110100011");
        toStringBase_BigInteger_helper(PI_DOUBLE, "4", "3.021003331222202020112203");
        toStringBase_BigInteger_helper(PI_DOUBLE, "10", "3.141592653589793115997963468544185161590576171875");
        toStringBase_BigInteger_helper(PI_DOUBLE, "16", "3.243F6A8885A3");
        toStringBase_BigInteger_helper(PI_DOUBLE, "100",
                "(3).(14)(15)(92)(65)(35)(89)(79)(31)(15)(99)(79)(63)(46)(85)(44)(18)(51)(61)(59)(5)(76)(17)(18)(75)");

        toStringBase_BigInteger_fail_helper("-1/2", "1");
        toStringBase_BigInteger_fail_helper("-1/2", "0");
        toStringBase_BigInteger_fail_helper("-1/2", "-1");
        toStringBase_BigInteger_fail_helper("1/3", "10");
    }

    private static void toStringBase_BigInteger_int_helper(
            @NotNull Rational r,
            @NotNull String base,
            int scale,
            @NotNull String output
    ) {
        aeq(r.toStringBase(Readers.readBigInteger(base).get(), scale), output);
    }

    private static void toStringBase_BigInteger_int_helper(
            @NotNull String r,
            @NotNull String base,
            int scale,
            @NotNull String output
    ) {
        toStringBase_BigInteger_int_helper(read(r).get(), base, scale, output);
    }

    private static void toStringBase_BigInteger_int_fail_helper(@NotNull String r, @NotNull String base, int scale) {
        try {
            read(r).get().toStringBase(Readers.readBigInteger(base).get(), scale);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testToStringBase_BigInteger_int() {
        toStringBase_BigInteger_int_helper("0", "10", 0, "0");
        toStringBase_BigInteger_int_helper("0", "10", -1, "0");
        toStringBase_BigInteger_int_helper("0", "10", 1, "0");
        toStringBase_BigInteger_int_helper("0", "83", 0, "(0)");
        toStringBase_BigInteger_int_helper("0", "83", -1, "(0)");
        toStringBase_BigInteger_int_helper("0", "83", 1, "(0)");

        toStringBase_BigInteger_int_helper("1", "10", 0, "1");
        toStringBase_BigInteger_int_helper("1", "10", -1, "0");
        toStringBase_BigInteger_int_helper("1", "10", 1, "1");
        toStringBase_BigInteger_int_helper("1", "83", 0, "(1)");
        toStringBase_BigInteger_int_helper("1", "83", -1, "(0)");
        toStringBase_BigInteger_int_helper("1", "83", 1, "(1)");

        toStringBase_BigInteger_int_helper("198", "10", 0, "198");
        toStringBase_BigInteger_int_helper("198", "10", 1, "198");
        toStringBase_BigInteger_int_helper("198", "10", -1, "190");
        toStringBase_BigInteger_int_helper("198", "10", -2, "100");
        toStringBase_BigInteger_int_helper("198", "10", -3, "0");
        toStringBase_BigInteger_int_helper("198", "83", 0, "(2)(32)");
        toStringBase_BigInteger_int_helper("198", "83", 1, "(2)(32)");
        toStringBase_BigInteger_int_helper("198", "83", -1, "(2)(0)");
        toStringBase_BigInteger_int_helper("198", "83", -2, "(0)");

        toStringBase_BigInteger_int_helper("-1/7", "10", -1, "0");
        toStringBase_BigInteger_int_helper("-1/7", "10", 0, "0");
        toStringBase_BigInteger_int_helper("-1/7", "10", 5, "-0.14285...");
        toStringBase_BigInteger_int_helper("-1/7", "10", 20, "-0.14285714285714285714...");
        toStringBase_BigInteger_int_helper("-1/7", "83", -1, "(0)");
        toStringBase_BigInteger_int_helper("-1/7", "83", 0, "(0)");
        toStringBase_BigInteger_int_helper("-1/7", "83", 5, "-(0).(11)(71)(11)(71)(11)...");
        toStringBase_BigInteger_int_helper("-1/7", "83", 20,
                "-(0).(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)...");

        toStringBase_BigInteger_int_helper(PI_DOUBLE, "10", -1, "0");
        toStringBase_BigInteger_int_helper(PI_DOUBLE, "10", 0, "3");
        toStringBase_BigInteger_int_helper(PI_DOUBLE, "10", 5, "3.14159...");
        toStringBase_BigInteger_int_helper(PI_DOUBLE, "10", 20, "3.14159265358979311599...");
        toStringBase_BigInteger_int_helper(PI_DOUBLE, "83", -1, "(0)");
        toStringBase_BigInteger_int_helper(PI_DOUBLE, "83", 0, "(3)");
        toStringBase_BigInteger_int_helper(PI_DOUBLE, "83", 5, "(3).(11)(62)(35)(69)(50)...");
        toStringBase_BigInteger_int_helper(PI_DOUBLE, "83", 20,
                "(3).(11)(62)(35)(69)(50)(19)(79)(18)(11)(8)(60)(35)(10)(62)(20)(58)(42)(14)(31)(34)...");

        toStringBase_BigInteger_int_helper("1/1000", "10", 0, "0");
        toStringBase_BigInteger_int_helper("1/1000", "10", 1, "0.0...");
        toStringBase_BigInteger_int_helper("1/1000", "10", 2, "0.00...");
        toStringBase_BigInteger_int_helper("1/1000", "10", 3, "0.001");
        toStringBase_BigInteger_int_helper("1/1000", "10", 4, "0.001");

        toStringBase_BigInteger_int_helper("1001/10000", "10", 0, "0");
        toStringBase_BigInteger_int_helper("1001/10000", "10", 1, "0.1...");
        toStringBase_BigInteger_int_helper("1001/10000", "10", 2, "0.10...");
        toStringBase_BigInteger_int_helper("1001/10000", "10", 3, "0.100...");
        toStringBase_BigInteger_int_helper("1001/10000", "10", 4, "0.1001");
        toStringBase_BigInteger_int_helper("1001/10000", "10", 5, "0.1001");

        toStringBase_BigInteger_int_helper("1/1000000", "100", 0, "(0)");
        toStringBase_BigInteger_int_helper("1/1000000", "100", 1, "(0).(0)...");
        toStringBase_BigInteger_int_helper("1/1000000", "100", 2, "(0).(0)(0)...");
        toStringBase_BigInteger_int_helper("1/1000000", "100", 3, "(0).(0)(0)(1)");
        toStringBase_BigInteger_int_helper("1/1000000", "100", 4, "(0).(0)(0)(1)");

        toStringBase_BigInteger_int_helper("1000001/10000000", "100", 0, "(0)");
        toStringBase_BigInteger_int_helper("1000001/10000000", "100", 1, "(0).(10)...");
        toStringBase_BigInteger_int_helper("1000001/10000000", "100", 2, "(0).(10)(0)...");
        toStringBase_BigInteger_int_helper("1000001/10000000", "100", 3, "(0).(10)(0)(0)...");
        toStringBase_BigInteger_int_helper("1000001/10000000", "100", 4, "(0).(10)(0)(0)(10)");
        toStringBase_BigInteger_int_helper("1000001/10000000", "100", 5, "(0).(10)(0)(0)(10)");

        toStringBase_BigInteger_int_fail_helper("-1/2", "1", 5);
        toStringBase_BigInteger_int_fail_helper("-1/2", "0", 5);
        toStringBase_BigInteger_int_fail_helper("-1/2", "-1", 5);
    }

    private static void fromStringBase_helper(@NotNull String s, @NotNull String base, @NotNull Object output) {
        aeq(fromStringBase(s, Readers.readBigInteger(base).get()), output);
    }

    private static void fromStringBase_fail_helper(@NotNull String s, @NotNull String base) {
        try {
            fromStringBase(s, Readers.readBigInteger(base).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromStringBase() {
        fromStringBase_helper("", "2", "0");
        fromStringBase_helper("", "3", "0");
        fromStringBase_helper("", "4", "0");
        fromStringBase_helper("", "10", "0");
        fromStringBase_helper("", "16", "0");
        fromStringBase_helper("", "83", "0");
        fromStringBase_helper("", "100", "0");

        fromStringBase_helper("0", "2", "0");
        fromStringBase_helper("0", "3", "0");
        fromStringBase_helper("0", "4", "0");
        fromStringBase_helper("0", "10", "0");
        fromStringBase_helper("0", "16", "0");
        fromStringBase_helper("(0)", "83", "0");
        fromStringBase_helper("(0)", "100", "0");

        fromStringBase_helper("1", "2", "1");
        fromStringBase_helper("1", "3", "1");
        fromStringBase_helper("1", "4", "1");
        fromStringBase_helper("1", "10", "1");
        fromStringBase_helper("1", "16", "1");
        fromStringBase_helper("(1)", "83", "1");
        fromStringBase_helper("(1)", "100", "1");

        fromStringBase_helper("-0.1", "2", "-1/2");
        fromStringBase_helper("-0.2", "4", "-1/2");
        fromStringBase_helper("-0.5", "10", "-1/2");
        fromStringBase_helper("-0.8", "16", "-1/2");
        fromStringBase_helper("-(0).(50)", "100", "-1/2");

        fromStringBase_helper("-0.1", "3", "-1/3");
        fromStringBase_helper("11.001001000011111101101010100010001000010110100011", "2", PI_DOUBLE);
        fromStringBase_helper("3.021003331222202020112203", "4", PI_DOUBLE);
        fromStringBase_helper("3.141592653589793115997963468544185161590576171875", "10", PI_DOUBLE);
        fromStringBase_helper("3.243F6A8885A3", "16", PI_DOUBLE);
        fromStringBase_helper(
                "(3).(14)(15)(92)(65)(35)(89)(79)(31)(15)(99)(79)(63)(46)(85)(44)(18)(51)(61)(59)(5)(76)(17)(18)(75)",
                "100",
                PI_DOUBLE
        );

        fromStringBase_helper("00", "10", 0);
        fromStringBase_helper("0.", "10", 0);
        fromStringBase_helper(".0", "10", 0);
        fromStringBase_helper("0.0", "10", 0);
        fromStringBase_helper("(0)(0)", "100", 0);
        fromStringBase_helper("(0).", "100", 0);
        fromStringBase_helper(".(0)", "100", 0);
        fromStringBase_helper("(0).(0)", "100", 0);

        fromStringBase_fail_helper("0", "1");
        fromStringBase_fail_helper("0", "0");
        fromStringBase_fail_helper("0", "-1");
        fromStringBase_fail_helper("a", "10");
        fromStringBase_fail_helper("[10]", "100");
        fromStringBase_fail_helper("(.10)", "100");
        fromStringBase_fail_helper("5-", "10");
        fromStringBase_fail_helper("-", "10");
        fromStringBase_fail_helper("(5)-", "100");
        fromStringBase_fail_helper("2-3", "10");
        fromStringBase_fail_helper("(2)-(3)", "100");
        fromStringBase_fail_helper("1e5", "10");
        fromStringBase_fail_helper("1E5", "10");
        fromStringBase_fail_helper("(2)(3)", "10");
        fromStringBase_fail_helper("23", "100");
        fromStringBase_fail_helper("-0.1...", "10");
        fromStringBase_fail_helper("-5...", "10");
        fromStringBase_fail_helper("()", "100");
        fromStringBase_fail_helper("()()", "100");
        fromStringBase_fail_helper("(00)", "100");
        fromStringBase_fail_helper("(02)", "100");
        fromStringBase_fail_helper(".", "10");
        fromStringBase_fail_helper("-.", "10");
    }

    @Test
    public void testCancelDenominators() {
        aeq(cancelDenominators(readRationalList("[]")), "[]");
        aeq(cancelDenominators(readRationalList("[0]")), "[0]");
        aeq(cancelDenominators(readRationalList("[0, 0]")), "[0, 0]");
        aeq(cancelDenominators(readRationalList("[2/3]")), "[1]");
        aeq(cancelDenominators(readRationalList("[-2/3]")), "[-1]");
        aeq(cancelDenominators(readRationalList("[1, -2/3]")), "[3, -2]");
        aeq(cancelDenominators(readRationalList("[4, -4, 5/12, 0, 1]")), "[48, -48, 5, 0, 12]");
        aeq(cancelDenominators(readRationalList("[1, 1/2, 1/3, 1/4, 1/5]")), "[60, 30, 20, 15, 12]");
        try {
            cancelDenominators(readRationalListWithNulls("[1, null, 0]"));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readRationalList("[0, 1, 4, -4, 5/12, -5/12]"),
                readRationalList("[0, 1, 4, -4, 5/12, -5/12]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("0", 1);
        hashCode_helper("1", 32);
        hashCode_helper("4", 125);
        hashCode_helper("-4", -123);
        hashCode_helper("5/12", 167);
        hashCode_helper("-5/12", -143);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(readRationalList("[-4, -5/12, 0, 5/12, 1, 4]"));
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

    private static @NotNull List<BigInteger> readBigIntegerList(@NotNull String s) {
        return Readers.readList(Readers::readBigInteger).apply(s).get();
    }

    private static @NotNull List<Rational> readRationalList(@NotNull String s) {
        return Readers.readList(Rational::read).apply(s).get();
    }

    private static @NotNull List<Rational> readRationalListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Rational::read).apply(s).get();
    }
}
