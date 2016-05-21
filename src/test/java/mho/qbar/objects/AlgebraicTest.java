package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.math.BinaryFraction;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.Algebraic.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class AlgebraicTest {
    @Test
    public void testConstants() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
        aeq(TEN, "10");
        aeq(TWO, "2");
        aeq(NEGATIVE_ONE, "-1");
        aeq(ONE_HALF, "1/2");
        aeq(SQRT_TWO, "sqrt(2)");
        aeq(PHI, "(1+sqrt(5))/2");
    }

    private static void of_Polynomial_int_helper(@NotNull String polynomial, int rootIndex, @NotNull String output) {
        Algebraic x = of(Polynomial.readStrict(polynomial).get(), rootIndex);
        x.validate();
        aeq(x, output);
    }

    private static void of_Polynomial_int_fail_helper(@NotNull String polynomial, int rootIndex) {
        try {
            of(Polynomial.readStrict(polynomial).get(), rootIndex);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testOf_Polynomial_int() {
        of_Polynomial_int_helper("x", 0, "0");
        of_Polynomial_int_helper("x-1", 0, "1");
        of_Polynomial_int_helper("2*x-1", 0, "1/2");
        of_Polynomial_int_helper("x^2-2*x+1", 0, "1");
        of_Polynomial_int_helper("x^2-2", 0, "-sqrt(2)");
        of_Polynomial_int_helper("x^2-2", 1, "sqrt(2)");
        of_Polynomial_int_helper("x^2-4", 0, "-2");
        of_Polynomial_int_helper("x^2-4", 1, "2");
        of_Polynomial_int_helper("x^2-x-1", 0, "(1-sqrt(5))/2");
        of_Polynomial_int_helper("x^2-x-1", 1, "(1+sqrt(5))/2");
        of_Polynomial_int_helper("x^10", 0, "0");
        of_Polynomial_int_helper("x^5-x-1", 0, "root 0 of x^5-x-1");
        of_Polynomial_int_helper("x^3-x^2-2*x+2", 0, "-sqrt(2)");
        of_Polynomial_int_helper("x^3-x^2-2*x+2", 1, "1");
        of_Polynomial_int_helper("x^3-x^2-2*x+2", 2, "sqrt(2)");

        of_Polynomial_int_fail_helper("0", 0);
        of_Polynomial_int_fail_helper("1", 0);
        of_Polynomial_int_fail_helper("x", 1);
        of_Polynomial_int_fail_helper("x", -1);
        of_Polynomial_int_fail_helper("x^2+1", 0);
        of_Polynomial_int_fail_helper("x^2-1", 2);
    }

    private static void of_Rational_helper(@NotNull String input) {
        Algebraic x = of(Rational.readStrict(input).get());
        x.validate();
        aeq(x, input);
    }

    @Test
    public void testOf_Rational() {
        of_Rational_helper("0");
        of_Rational_helper("1");
        of_Rational_helper("2");
        of_Rational_helper("-2");
        of_Rational_helper("5/3");
        of_Rational_helper("-5/3");
    }

    private static void of_BigInteger_helper(@NotNull String input) {
        Algebraic x = of(Readers.readBigIntegerStrict(input).get());
        x.validate();
        aeq(x, input);
    }

    @Test
    public void testOf_BigInteger() {
        of_BigInteger_helper("0");
        of_BigInteger_helper("1");
        of_BigInteger_helper("-1");
        of_BigInteger_helper("23");
        of_BigInteger_helper("-23");
    }

    private static void of_long_helper(long input) {
        Algebraic x = of(input);
        x.validate();
        aeq(x, input);
    }

    @Test
    public void testOf_long() {
        of_long_helper(0L);
        of_long_helper(1L);
        of_long_helper(-1L);
        of_long_helper(23L);
        of_long_helper(-23L);
    }

    private static void of_int_helper(int input) {
        Algebraic x = of(input);
        x.validate();
        aeq(x, input);
    }

    @Test
    public void testOf_int() {
        of_int_helper(0);
        of_int_helper(1);
        of_int_helper(-1);
        of_int_helper(23);
        of_int_helper(-23);
    }

    private static void of_BinaryFraction_helper(@NotNull String input, @NotNull String output) {
        Algebraic x = of(BinaryFraction.readStrict(input).get());
        x.validate();
        aeq(x, output);
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
        Algebraic x = of(f).get();
        x.validate();
        aeq(x, output);
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
        Algebraic x = of(d).get();
        x.validate();
        aeq(x, output);
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
        Algebraic x = ofExact(f).get();
        x.validate();
        aeq(x, output);
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
        ofExact_float_helper(Float.MIN_VALUE, Rational.SMALLEST_FLOAT);
        ofExact_float_helper(-Float.MIN_VALUE, Rational.SMALLEST_FLOAT.negate());
        ofExact_float_helper(Float.MIN_NORMAL, Rational.SMALLEST_NORMAL_FLOAT);
        ofExact_float_helper(-Float.MIN_NORMAL, Rational.SMALLEST_NORMAL_FLOAT.negate());
        ofExact_float_helper(Float.MAX_VALUE, Rational.LARGEST_FLOAT);
        ofExact_float_helper(-Float.MAX_VALUE, Rational.LARGEST_FLOAT.negate());

        ofExact_float_empty_helper(Float.POSITIVE_INFINITY);
        ofExact_float_empty_helper(Float.NEGATIVE_INFINITY);
        ofExact_float_empty_helper(Float.NaN);
    }

    private static void ofExact_double_helper(double d, @NotNull Object output) {
        Algebraic x = ofExact(d).get();
        x.validate();
        aeq(x, output);
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
        ofExact_double_helper(Double.MIN_VALUE, Rational.SMALLEST_DOUBLE);
        ofExact_double_helper(-Double.MIN_VALUE, Rational.SMALLEST_DOUBLE.negate());
        ofExact_double_helper(Double.MIN_NORMAL, Rational.SMALLEST_NORMAL_DOUBLE);
        ofExact_double_helper(-Double.MIN_NORMAL, Rational.SMALLEST_NORMAL_DOUBLE.negate());
        ofExact_double_helper(Double.MAX_VALUE, Rational.LARGEST_DOUBLE);
        ofExact_double_helper(-Double.MAX_VALUE, Rational.LARGEST_DOUBLE.negate());
        ofExact_double_empty_helper(Double.POSITIVE_INFINITY);
        ofExact_double_empty_helper(Double.NEGATIVE_INFINITY);
        ofExact_double_empty_helper(Double.NaN);
    }

    private static void of_BigDecimal_helper(@NotNull String input, @NotNull String output) {
        Algebraic x = of(Readers.readBigDecimalStrict(input).get());
        x.validate();
        aeq(x, output);
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

    private static void isInteger_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isInteger(), output);
    }

    @Test
    public void testIsInteger() {
        isInteger_helper("0", true);
        isInteger_helper("1", true);
        isInteger_helper("-1", true);
        isInteger_helper("10", true);
        isInteger_helper("1/2", false);
        isInteger_helper("-4/3", false);
        isInteger_helper("sqrt(2)", false);
        isInteger_helper("-sqrt(2)", false);
        isInteger_helper("(1+sqrt(5))/2", false);
        isInteger_helper("root 0 of x^5-x-1", false);
    }

    private static void bigIntegerValue_RoundingMode_helper(
            @NotNull String x,
            @NotNull String roundingMode,
            @NotNull String output
    ) {
        aeq(readStrict(x).get().bigIntegerValue(Readers.readRoundingModeStrict(roundingMode).get()), output);
    }

    private static void bigIntegerValue_RoundingMode_fail_helper(@NotNull String x, @NotNull String roundingMode) {
        try {
            readStrict(x).get().bigIntegerValue(Readers.readRoundingModeStrict(roundingMode).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBigIntegerValue_RoundingMode() {
        bigIntegerValue_RoundingMode_helper("0", "UP", "0");
        bigIntegerValue_RoundingMode_helper("1", "UP", "1");
        bigIntegerValue_RoundingMode_helper("1/2", "UP", "1");
        bigIntegerValue_RoundingMode_helper("-4/3", "UP", "-2");
        bigIntegerValue_RoundingMode_helper("sqrt(2)", "UP", "2");
        bigIntegerValue_RoundingMode_helper("-sqrt(2)", "UP", "-2");
        bigIntegerValue_RoundingMode_helper("(1+sqrt(5))/2", "UP", "2");
        bigIntegerValue_RoundingMode_helper("root 0 of x^5-x-1", "UP", "2");

        bigIntegerValue_RoundingMode_helper("0", "DOWN", "0");
        bigIntegerValue_RoundingMode_helper("1", "DOWN", "1");
        bigIntegerValue_RoundingMode_helper("1/2", "DOWN", "0");
        bigIntegerValue_RoundingMode_helper("-4/3", "DOWN", "-1");
        bigIntegerValue_RoundingMode_helper("sqrt(2)", "DOWN", "1");
        bigIntegerValue_RoundingMode_helper("-sqrt(2)", "DOWN", "-1");
        bigIntegerValue_RoundingMode_helper("(1+sqrt(5))/2", "DOWN", "1");
        bigIntegerValue_RoundingMode_helper("root 0 of x^5-x-1", "DOWN", "1");

        bigIntegerValue_RoundingMode_helper("0", "CEILING", "0");
        bigIntegerValue_RoundingMode_helper("1", "CEILING", "1");
        bigIntegerValue_RoundingMode_helper("1/2", "CEILING", "1");
        bigIntegerValue_RoundingMode_helper("-4/3", "CEILING", "-1");
        bigIntegerValue_RoundingMode_helper("sqrt(2)", "CEILING", "2");
        bigIntegerValue_RoundingMode_helper("-sqrt(2)", "CEILING", "-1");
        bigIntegerValue_RoundingMode_helper("(1+sqrt(5))/2", "CEILING", "2");
        bigIntegerValue_RoundingMode_helper("root 0 of x^5-x-1", "CEILING", "2");

        bigIntegerValue_RoundingMode_helper("0", "FLOOR", "0");
        bigIntegerValue_RoundingMode_helper("1", "FLOOR", "1");
        bigIntegerValue_RoundingMode_helper("1/2", "FLOOR", "0");
        bigIntegerValue_RoundingMode_helper("-4/3", "FLOOR", "-2");
        bigIntegerValue_RoundingMode_helper("sqrt(2)", "FLOOR", "1");
        bigIntegerValue_RoundingMode_helper("-sqrt(2)", "FLOOR", "-2");
        bigIntegerValue_RoundingMode_helper("(1+sqrt(5))/2", "FLOOR", "1");
        bigIntegerValue_RoundingMode_helper("root 0 of x^5-x-1", "FLOOR", "1");

        bigIntegerValue_RoundingMode_helper("0", "HALF_UP", "0");
        bigIntegerValue_RoundingMode_helper("1", "HALF_UP", "1");
        bigIntegerValue_RoundingMode_helper("1/2", "HALF_UP", "1");
        bigIntegerValue_RoundingMode_helper("-4/3", "HALF_UP", "-1");
        bigIntegerValue_RoundingMode_helper("sqrt(2)", "HALF_UP", "1");
        bigIntegerValue_RoundingMode_helper("-sqrt(2)", "HALF_UP", "-1");
        bigIntegerValue_RoundingMode_helper("(1+sqrt(5))/2", "HALF_UP", "2");
        bigIntegerValue_RoundingMode_helper("root 0 of x^5-x-1", "HALF_UP", "1");

        bigIntegerValue_RoundingMode_helper("0", "HALF_DOWN", "0");
        bigIntegerValue_RoundingMode_helper("1", "HALF_DOWN", "1");
        bigIntegerValue_RoundingMode_helper("1/2", "HALF_DOWN", "0");
        bigIntegerValue_RoundingMode_helper("-4/3", "HALF_DOWN", "-1");
        bigIntegerValue_RoundingMode_helper("sqrt(2)", "HALF_DOWN", "1");
        bigIntegerValue_RoundingMode_helper("-sqrt(2)", "HALF_DOWN", "-1");
        bigIntegerValue_RoundingMode_helper("(1+sqrt(5))/2", "HALF_DOWN", "2");
        bigIntegerValue_RoundingMode_helper("root 0 of x^5-x-1", "HALF_DOWN", "1");

        bigIntegerValue_RoundingMode_helper("0", "HALF_EVEN", "0");
        bigIntegerValue_RoundingMode_helper("1", "HALF_EVEN", "1");
        bigIntegerValue_RoundingMode_helper("1/2", "HALF_EVEN", "0");
        bigIntegerValue_RoundingMode_helper("-4/3", "HALF_EVEN", "-1");
        bigIntegerValue_RoundingMode_helper("sqrt(2)", "HALF_EVEN", "1");
        bigIntegerValue_RoundingMode_helper("-sqrt(2)", "HALF_EVEN", "-1");
        bigIntegerValue_RoundingMode_helper("(1+sqrt(5))/2", "HALF_EVEN", "2");
        bigIntegerValue_RoundingMode_helper("root 0 of x^5-x-1", "HALF_EVEN", "1");

        bigIntegerValue_RoundingMode_helper("0", "UNNECESSARY", "0");
        bigIntegerValue_RoundingMode_helper("1", "UNNECESSARY", "1");

        bigIntegerValue_RoundingMode_fail_helper("1/2", "UNNECESSARY");
        bigIntegerValue_RoundingMode_fail_helper("-4/3", "UNNECESSARY");
        bigIntegerValue_RoundingMode_fail_helper("sqrt(2)", "UNNECESSARY");
        bigIntegerValue_RoundingMode_fail_helper("-sqrt(2)", "UNNECESSARY");
        bigIntegerValue_RoundingMode_fail_helper("(1+sqrt(5))/2", "UNNECESSARY");
        bigIntegerValue_RoundingMode_fail_helper("root 0 of x^5-x-1", "UNNECESSARY");
    }

    private static void bigIntegerValue_helper(@NotNull String x, @NotNull String output) {
        aeq(readStrict(x).get().bigIntegerValue(), output);
    }

    @Test
    public void testBigIntegerValue() {
        bigIntegerValue_helper("0", "0");
        bigIntegerValue_helper("1", "1");
        bigIntegerValue_helper("1/2", "0");
        bigIntegerValue_helper("-4/3", "-1");
        bigIntegerValue_helper("sqrt(2)", "1");
        bigIntegerValue_helper("-sqrt(2)", "-1");
        bigIntegerValue_helper("(1+sqrt(5))/2", "2");
        bigIntegerValue_helper("root 0 of x^5-x-1", "1");
    }

    private static void floor_helper(@NotNull String x, @NotNull String output) {
        aeq(readStrict(x).get().floor(), output);
    }

    @Test
    public void testFloor() {
        floor_helper("0", "0");
        floor_helper("1", "1");
        floor_helper("1/2", "0");
        floor_helper("-4/3", "-2");
        floor_helper("sqrt(2)", "1");
        floor_helper("-sqrt(2)", "-2");
        floor_helper("(1+sqrt(5))/2", "1");
        floor_helper("root 0 of x^5-x-1", "1");
    }

    private static void ceiling_helper(@NotNull String x, @NotNull String output) {
        aeq(readStrict(x).get().ceiling(), output);
    }

    @Test
    public void testCeiling() {
        ceiling_helper("0", "0");
        ceiling_helper("1", "1");
        ceiling_helper("1/2", "1");
        ceiling_helper("-4/3", "-1");
        ceiling_helper("sqrt(2)", "2");
        ceiling_helper("-sqrt(2)", "-1");
        ceiling_helper("(1+sqrt(5))/2", "2");
        ceiling_helper("root 0 of x^5-x-1", "2");
    }

    private static void bigIntegerValueExact_helper(@NotNull String x, @NotNull String output) {
        aeq(readStrict(x).get().bigIntegerValueExact(), output);
    }

    private static void bigIntegerValueExact_fail_helper(@NotNull String x) {
        try {
            readStrict(x).get().bigIntegerValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBigIntegerValueExact() {
        bigIntegerValueExact_helper("0", "0");
        bigIntegerValueExact_helper("1", "1");

        bigIntegerValueExact_fail_helper("1/2");
        bigIntegerValueExact_fail_helper("-4/3");
        bigIntegerValueExact_fail_helper("sqrt(2)");
        bigIntegerValueExact_fail_helper("-sqrt(2)");
        bigIntegerValueExact_fail_helper("(1+sqrt(5))/2");
        bigIntegerValueExact_fail_helper("root 0 of x^5-x-1");
    }

    private static void byteValueExact_helper(@NotNull String r) {
        aeq(readStrict(r).get().byteValueExact(), r);
    }

    private static void byteValueExact_fail_helper(@NotNull String r) {
        try {
            readStrict(r).get().byteValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testByteValueExact() {
        byteValueExact_helper("1");
        byteValueExact_helper("0");
        byteValueExact_helper("-1");
        byteValueExact_helper("23");
        byteValueExact_helper("8");

        byteValueExact_fail_helper("11/2");
        byteValueExact_fail_helper("sqrt(2)");
        byteValueExact_fail_helper("1000");
    }

    private static void shortValueExact_helper(@NotNull String r) {
        aeq(readStrict(r).get().shortValueExact(), r);
    }

    private static void shortValueExact_fail_helper(@NotNull String r) {
        try {
            readStrict(r).get().shortValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShortValueExact() {
        shortValueExact_helper("1");
        shortValueExact_helper("0");
        shortValueExact_helper("-1");
        shortValueExact_helper("23");
        shortValueExact_helper("8");

        shortValueExact_fail_helper("11/2");
        shortValueExact_fail_helper("sqrt(2)");
        shortValueExact_fail_helper("100000");
    }

    private static void intValueExact_helper(@NotNull String r) {
        aeq(readStrict(r).get().intValueExact(), r);
    }

    private static void intValueExact_fail_helper(@NotNull String r) {
        try {
            readStrict(r).get().intValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIntValueExact() {
        intValueExact_helper("1");
        intValueExact_helper("0");
        intValueExact_helper("-1");
        intValueExact_helper("23");
        intValueExact_helper("8");

        intValueExact_fail_helper("11/2");
        intValueExact_fail_helper("sqrt(2)");
        intValueExact_fail_helper("10000000000");
    }

    private static void longValueExact_helper(@NotNull String r) {
        aeq(readStrict(r).get().longValueExact(), r);
    }

    private static void longValueExact_fail_helper(@NotNull String r) {
        try {
            readStrict(r).get().longValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testLongValueExact() {
        longValueExact_helper("1");
        longValueExact_helper("0");
        longValueExact_helper("-1");
        longValueExact_helper("23");
        longValueExact_helper("8");

        longValueExact_fail_helper("11/2");
        longValueExact_fail_helper("sqrt(2)");
        longValueExact_fail_helper("10000000000000000000");
    }

    private static void isIntegerPowerOfTwo_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isIntegerPowerOfTwo(), output);
    }

    private static void isIntegerPowerOfTwo_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().isIntegerPowerOfTwo();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIsIntegerPowerOfTwo() {
        isIntegerPowerOfTwo_helper("1", true);
        isIntegerPowerOfTwo_helper("1/2", true);
        isIntegerPowerOfTwo_helper("8", true);
        isIntegerPowerOfTwo_helper("10", false);
        isIntegerPowerOfTwo_helper("sqrt(2)", false);
        isIntegerPowerOfTwo_helper("(1+sqrt(5))/2", false);
        isIntegerPowerOfTwo_helper("root 0 of x^5-x-1", false);

        isIntegerPowerOfTwo_fail_helper("0");
        isIntegerPowerOfTwo_fail_helper("-1");
        isIntegerPowerOfTwo_fail_helper("(1-sqrt(5))/2");
    }

    private static void roundUpToIntegerPowerOfTwo_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().roundUpToIntegerPowerOfTwo(), output);
    }

    private static void roundUpToIntegerPowerOfTwo_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().roundUpToIntegerPowerOfTwo();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRoundUpToIntegerPowerOfTwo() {
        roundUpToIntegerPowerOfTwo_helper("1", "1");
        roundUpToIntegerPowerOfTwo_helper("1/2", "1/2");
        roundUpToIntegerPowerOfTwo_helper("10", "16");
        roundUpToIntegerPowerOfTwo_helper("sqrt(2)", "2");
        roundUpToIntegerPowerOfTwo_helper("(1+sqrt(5))/2", "2");
        roundUpToIntegerPowerOfTwo_helper("root 0 of x^5-x-1", "2");

        roundUpToIntegerPowerOfTwo_fail_helper("0");
        roundUpToIntegerPowerOfTwo_fail_helper("-1");
        roundUpToIntegerPowerOfTwo_fail_helper("(1-sqrt(5))/2");
    }

    private static void isBinaryFraction_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isBinaryFraction(), output);
    }

    @Test
    public void testIsBinaryFraction() {
        isBinaryFraction_helper("0", true);
        isBinaryFraction_helper("1", true);
        isBinaryFraction_helper("-1", true);
        isBinaryFraction_helper("1/2", true);
        isBinaryFraction_helper("8", true);
        isBinaryFraction_helper("10", true);
        isBinaryFraction_helper("1/3", false);
        isBinaryFraction_helper("sqrt(2)", false);
        isBinaryFraction_helper("(1-sqrt(5))/2", false);
        isBinaryFraction_helper("(1+sqrt(5))/2", false);
        isBinaryFraction_helper("root 0 of x^5-x-1", false);
    }

    private static void binaryFractionValueExact_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().binaryFractionValueExact(), output);
    }

    private static void binaryFractionValueExact_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().binaryFractionValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBinaryFractionValueExact() {
        binaryFractionValueExact_helper("0", "0");
        binaryFractionValueExact_helper("1", "1");
        binaryFractionValueExact_helper("-1", "-1");
        binaryFractionValueExact_helper("1/2", "1 >> 1");
        binaryFractionValueExact_helper("10", "5 << 1");

        binaryFractionValueExact_fail_helper("1/3");
        binaryFractionValueExact_fail_helper("sqrt(2)");
        binaryFractionValueExact_fail_helper("root 0 of x^5-x-1");
    }

    private static void isRational_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isRational(), output);
    }

    @Test
    public void testIsRational() {
        isRational_helper("0", true);
        isRational_helper("1", true);
        isRational_helper("-1", true);
        isRational_helper("1/2", true);
        isRational_helper("8", true);
        isRational_helper("10", true);
        isRational_helper("1/3", true);
        isRational_helper("sqrt(2)", false);
        isRational_helper("(1-sqrt(5))/2", false);
        isRational_helper("(1+sqrt(5))/2", false);
        isRational_helper("root 0 of x^5-x-1", false);
    }

    private static void isAlgebraicInteger_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isAlgebraicInteger(), output);
    }

    @Test
    public void testIsAlgebraicInteger() {
        isAlgebraicInteger_helper("0", true);
        isAlgebraicInteger_helper("1", true);
        isAlgebraicInteger_helper("-1", true);
        isAlgebraicInteger_helper("1/2", false);
        isAlgebraicInteger_helper("10", true);
        isAlgebraicInteger_helper("1/3", false);
        isAlgebraicInteger_helper("sqrt(2)", true);
        isAlgebraicInteger_helper("(1-sqrt(5))/2", true);
        isAlgebraicInteger_helper("(1+sqrt(5))/2", true);
        isAlgebraicInteger_helper("root 0 of x^5-x-1", true);
        isAlgebraicInteger_helper("sqrt(2)/2", false);
    }

    private static void rationalValueExact_helper(@NotNull String input) {
        aeq(readStrict(input).get().rationalValueExact(), input);
    }

    private static void rationalValueExact_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().rationalValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRationalValueExact() {
        rationalValueExact_helper("0");
        rationalValueExact_helper("1");
        rationalValueExact_helper("-1");
        rationalValueExact_helper("1/2");
        rationalValueExact_helper("10");
        rationalValueExact_helper("4/3");

        rationalValueExact_fail_helper("sqrt(2)");
        rationalValueExact_fail_helper("root 0 of x^5-x-1");
    }

    private static void realValue_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().realValue(), output);
    }

    @Test
    public void testRealValue() {
        realValue_helper("0", "0");
        realValue_helper("1", "1");
        realValue_helper("-1", "-1");
        realValue_helper("1/2", "0.5");
        realValue_helper("10", "10");
        realValue_helper("1/3", "0.33333333333333333333...");
        realValue_helper("sqrt(2)", "1.41421356237309504880...");
        realValue_helper("(1-sqrt(5))/2", "-0.61803398874989484820...");
        realValue_helper("(1+sqrt(5))/2", "1.61803398874989484820...");
        realValue_helper("sqrt(2)/2", "0.70710678118654752440...");
        realValue_helper("root 0 of x^5-x-1", "1.16730397826141868425...");
    }

    private static void binaryExponent_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().binaryExponent(), output);
    }

    private static void binaryExponent_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().binaryExponent();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testBinaryExponent() {
        binaryExponent_helper("1", 0);
        binaryExponent_helper("1/2", -1);
        binaryExponent_helper("10", 3);
        binaryExponent_helper("1/3", -2);
        binaryExponent_helper("sqrt(2)", 0);
        binaryExponent_helper("(1+sqrt(5))/2", 0);
        binaryExponent_helper("sqrt(2)/2", -1);
        binaryExponent_helper("root 0 of x^5-x-1", 0);

        binaryExponent_fail_helper("0");
        binaryExponent_fail_helper("-1");
        binaryExponent_fail_helper("-sqrt(2)");
    }

    private static void isEqualToFloat_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isEqualToFloat(), output);
    }

    @Test
    public void testIsEqualToFloat() {
        isEqualToFloat_helper("0", true);
        isEqualToFloat_helper("1", true);
        isEqualToFloat_helper("1/2", true);
        isEqualToFloat_helper("-1/2", true);
        isEqualToFloat_helper("1/3", false);
        isEqualToFloat_helper("-1/3", false);
        isEqualToFloat_helper("1/10", false);
        isEqualToFloat_helper("-1/10", false);
        isEqualToFloat_helper("sqrt(2)", false);
        isEqualToFloat_helper("-sqrt(2)", false);
        isEqualToFloat_helper("root 0 of x^5-x-1", false);
    }

    private static void isEqualToDouble_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isEqualToDouble(), output);
    }

    @Test
    public void testIsEqualToDouble() {
        isEqualToDouble_helper("0", true);
        isEqualToDouble_helper("1", true);
        isEqualToDouble_helper("1/2", true);
        isEqualToDouble_helper("-1/2", true);
        isEqualToDouble_helper("1/3", false);
        isEqualToDouble_helper("-1/3", false);
        isEqualToDouble_helper("1/10", false);
        isEqualToDouble_helper("-1/10", false);
        isEqualToDouble_helper("sqrt(2)", false);
        isEqualToDouble_helper("-sqrt(2)", false);
        isEqualToDouble_helper("root 0 of x^5-x-1", false);
    }

    private static void floatValue_RoundingMode_helper(@NotNull String x, @NotNull String roundingMode, float output) {
        aeq(readStrict(x).get().floatValue(Readers.readRoundingModeStrict(roundingMode).get()), output);
    }

    private static void floatValue_RoundingMode_fail_helper(@NotNull String x, @NotNull String roundingMode) {
        try {
            readStrict(x).get().floatValue(Readers.readRoundingModeStrict(roundingMode).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testFloatValue_RoundingMode() {
        floatValue_RoundingMode_helper("0", "UP", 0.0f);
        floatValue_RoundingMode_helper("1", "UP", 1.0f);
        floatValue_RoundingMode_helper("1/2", "UP", 0.5f);
        floatValue_RoundingMode_helper("-4/3", "UP", -1.3333334f);
        floatValue_RoundingMode_helper("sqrt(2)", "UP", 1.4142137f);
        floatValue_RoundingMode_helper("-sqrt(2)", "UP", -1.4142137f);
        floatValue_RoundingMode_helper("(1+sqrt(5))/2", "UP", 1.618034f);
        floatValue_RoundingMode_helper("root 0 of x^5-x-1", "UP", 1.167304f);

        floatValue_RoundingMode_helper("0", "DOWN", 0.0f);
        floatValue_RoundingMode_helper("1", "DOWN", 1.0f);
        floatValue_RoundingMode_helper("1/2", "DOWN", 0.5f);
        floatValue_RoundingMode_helper("-4/3", "DOWN", -1.3333333f);
        floatValue_RoundingMode_helper("sqrt(2)", "DOWN", 1.4142135f);
        floatValue_RoundingMode_helper("-sqrt(2)", "DOWN", -1.4142135f);
        floatValue_RoundingMode_helper("(1+sqrt(5))/2", "DOWN", 1.6180339f);
        floatValue_RoundingMode_helper("root 0 of x^5-x-1", "DOWN", 1.1673039f);

        floatValue_RoundingMode_helper("0", "CEILING", 0.0f);
        floatValue_RoundingMode_helper("1", "CEILING", 1.0f);
        floatValue_RoundingMode_helper("1/2", "CEILING", 0.5f);
        floatValue_RoundingMode_helper("-4/3", "CEILING", -1.3333333f);
        floatValue_RoundingMode_helper("sqrt(2)", "CEILING", 1.4142137f);
        floatValue_RoundingMode_helper("-sqrt(2)", "CEILING", -1.4142135f);
        floatValue_RoundingMode_helper("(1+sqrt(5))/2", "CEILING", 1.618034f);
        floatValue_RoundingMode_helper("root 0 of x^5-x-1", "CEILING", 1.167304f);

        floatValue_RoundingMode_helper("0", "FLOOR", 0.0f);
        floatValue_RoundingMode_helper("1", "FLOOR", 1.0f);
        floatValue_RoundingMode_helper("1/2", "FLOOR", 0.5f);
        floatValue_RoundingMode_helper("-4/3", "FLOOR", -1.3333334f);
        floatValue_RoundingMode_helper("sqrt(2)", "FLOOR", 1.4142135f);
        floatValue_RoundingMode_helper("-sqrt(2)", "FLOOR", -1.4142137f);
        floatValue_RoundingMode_helper("(1+sqrt(5))/2", "FLOOR", 1.6180339f);
        floatValue_RoundingMode_helper("root 0 of x^5-x-1", "FLOOR", 1.1673039f);

        floatValue_RoundingMode_helper("0", "HALF_UP", 0.0f);
        floatValue_RoundingMode_helper("1", "HALF_UP", 1.0f);
        floatValue_RoundingMode_helper("1/2", "HALF_UP", 0.5f);
        floatValue_RoundingMode_helper("-4/3", "HALF_UP", -1.3333334f);
        floatValue_RoundingMode_helper("sqrt(2)", "HALF_UP", 1.4142135f);
        floatValue_RoundingMode_helper("-sqrt(2)", "HALF_UP", -1.4142135f);
        floatValue_RoundingMode_helper("(1+sqrt(5))/2", "HALF_UP", 1.618034f);
        floatValue_RoundingMode_helper("root 0 of x^5-x-1", "HALF_UP", 1.1673039f);

        floatValue_RoundingMode_helper("0", "HALF_DOWN", 0.0f);
        floatValue_RoundingMode_helper("1", "HALF_DOWN", 1.0f);
        floatValue_RoundingMode_helper("1/2", "HALF_DOWN", 0.5f);
        floatValue_RoundingMode_helper("-4/3", "HALF_DOWN", -1.3333334f);
        floatValue_RoundingMode_helper("sqrt(2)", "HALF_DOWN", 1.4142135f);
        floatValue_RoundingMode_helper("-sqrt(2)", "HALF_DOWN", -1.4142135f);
        floatValue_RoundingMode_helper("(1+sqrt(5))/2", "HALF_DOWN", 1.618034f);
        floatValue_RoundingMode_helper("root 0 of x^5-x-1", "HALF_DOWN", 1.1673039f);

        floatValue_RoundingMode_helper("0", "HALF_EVEN", 0.0f);
        floatValue_RoundingMode_helper("1", "HALF_EVEN", 1.0f);
        floatValue_RoundingMode_helper("1/2", "HALF_EVEN", 0.5f);
        floatValue_RoundingMode_helper("-4/3", "HALF_EVEN", -1.3333334f);
        floatValue_RoundingMode_helper("sqrt(2)", "HALF_EVEN", 1.4142135f);
        floatValue_RoundingMode_helper("-sqrt(2)", "HALF_EVEN", -1.4142135f);
        floatValue_RoundingMode_helper("(1+sqrt(5))/2", "HALF_EVEN", 1.618034f);
        floatValue_RoundingMode_helper("root 0 of x^5-x-1", "HALF_EVEN", 1.1673039f);

        floatValue_RoundingMode_helper("0", "UNNECESSARY", 0.0f);
        floatValue_RoundingMode_helper("1", "UNNECESSARY", 1.0f);
        floatValue_RoundingMode_helper("1/2", "UNNECESSARY", 0.5f);

        floatValue_RoundingMode_fail_helper("-4/3", "UNNECESSARY");
        floatValue_RoundingMode_fail_helper("sqrt(2)", "UNNECESSARY");
        floatValue_RoundingMode_fail_helper("-sqrt(2)", "UNNECESSARY");
        floatValue_RoundingMode_fail_helper("(1+sqrt(5))/2", "UNNECESSARY");
        floatValue_RoundingMode_fail_helper("root 0 of x^5-x-1", "UNNECESSARY");
    }

    private static void floatValue_helper(@NotNull String input, float output) {
        aeq(readStrict(input).get().floatValue(), output);
    }

    @Test
    public void testFloatValue() {
        floatValue_helper("0", 0.0f);
        floatValue_helper("1", 1.0f);
        floatValue_helper("1/2", 0.5f);
        floatValue_helper("-4/3", -1.3333334f);
        floatValue_helper("sqrt(2)", 1.4142135f);
        floatValue_helper("-sqrt(2)", -1.4142135f);
        floatValue_helper("(1+sqrt(5))/2", 1.618034f);
        floatValue_helper("root 0 of x^5-x-1", 1.1673039f);
    }

    private static void floatValueExact_helper(@NotNull String input, float output) {
        aeq(readStrict(input).get().floatValueExact(), output);
    }

    private static void floatValueExact_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testFloatValueExact() {
        floatValueExact_helper("0", 0.0f);
        floatValueExact_helper("1", 1.0f);
        floatValueExact_helper("1/2", 0.5f);

        floatValueExact_fail_helper("-4/3");
        floatValueExact_fail_helper("sqrt(2)");
        floatValueExact_fail_helper("-sqrt(2)");
        floatValueExact_fail_helper("(1+sqrt(5))/2");
        floatValueExact_fail_helper("root 0 of x^5-x-1");
    }

    private static void doubleValue_RoundingMode_helper(
            @NotNull String x,
            @NotNull String roundingMode,
            double output
    ) {
        aeq(readStrict(x).get().doubleValue(Readers.readRoundingModeStrict(roundingMode).get()), output);
    }

    private static void doubleValue_RoundingMode_fail_helper(@NotNull String x, @NotNull String roundingMode) {
        try {
            readStrict(x).get().doubleValue(Readers.readRoundingModeStrict(roundingMode).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDoubleValue_RoundingMode() {
        doubleValue_RoundingMode_helper("0", "UP", 0.0);
        doubleValue_RoundingMode_helper("1", "UP", 1.0);
        doubleValue_RoundingMode_helper("1/2", "UP", 0.5);
        doubleValue_RoundingMode_helper("-4/3", "UP", -1.3333333333333335);
        doubleValue_RoundingMode_helper("sqrt(2)", "UP", 1.4142135623730951);
        doubleValue_RoundingMode_helper("-sqrt(2)", "UP", -1.4142135623730951);
        doubleValue_RoundingMode_helper("(1+sqrt(5))/2", "UP", 1.618033988749895);
        doubleValue_RoundingMode_helper("root 0 of x^5-x-1", "UP", 1.1673039782614187);

        doubleValue_RoundingMode_helper("0", "DOWN", 0.0);
        doubleValue_RoundingMode_helper("1", "DOWN", 1.0);
        doubleValue_RoundingMode_helper("1/2", "DOWN", 0.5);
        doubleValue_RoundingMode_helper("-4/3", "DOWN", -1.3333333333333333);
        doubleValue_RoundingMode_helper("sqrt(2)", "DOWN", 1.414213562373095);
        doubleValue_RoundingMode_helper("-sqrt(2)", "DOWN", -1.414213562373095);
        doubleValue_RoundingMode_helper("(1+sqrt(5))/2", "DOWN", 1.6180339887498947);
        doubleValue_RoundingMode_helper("root 0 of x^5-x-1", "DOWN", 1.1673039782614185);

        doubleValue_RoundingMode_helper("0", "CEILING", 0.0);
        doubleValue_RoundingMode_helper("1", "CEILING", 1.0);
        doubleValue_RoundingMode_helper("1/2", "CEILING", 0.5);
        doubleValue_RoundingMode_helper("-4/3", "CEILING", -1.3333333333333333);
        doubleValue_RoundingMode_helper("sqrt(2)", "CEILING", 1.4142135623730951);
        doubleValue_RoundingMode_helper("-sqrt(2)", "CEILING", -1.414213562373095);
        doubleValue_RoundingMode_helper("(1+sqrt(5))/2", "CEILING", 1.618033988749895);
        doubleValue_RoundingMode_helper("root 0 of x^5-x-1", "CEILING", 1.1673039782614187);

        doubleValue_RoundingMode_helper("0", "FLOOR", 0.0);
        doubleValue_RoundingMode_helper("1", "FLOOR", 1.0);
        doubleValue_RoundingMode_helper("1/2", "FLOOR", 0.5);
        doubleValue_RoundingMode_helper("-4/3", "FLOOR", -1.3333333333333335);
        doubleValue_RoundingMode_helper("sqrt(2)", "FLOOR", 1.414213562373095);
        doubleValue_RoundingMode_helper("-sqrt(2)", "FLOOR", -1.4142135623730951);
        doubleValue_RoundingMode_helper("(1+sqrt(5))/2", "FLOOR", 1.6180339887498947);
        doubleValue_RoundingMode_helper("root 0 of x^5-x-1", "FLOOR", 1.1673039782614185);

        doubleValue_RoundingMode_helper("0", "HALF_UP", 0.0);
        doubleValue_RoundingMode_helper("1", "HALF_UP", 1.0);
        doubleValue_RoundingMode_helper("1/2", "HALF_UP", 0.5);
        doubleValue_RoundingMode_helper("-4/3", "HALF_UP", -1.3333333333333333);
        doubleValue_RoundingMode_helper("sqrt(2)", "HALF_UP", 1.4142135623730951);
        doubleValue_RoundingMode_helper("-sqrt(2)", "HALF_UP", -1.4142135623730951);
        doubleValue_RoundingMode_helper("(1+sqrt(5))/2", "HALF_UP", 1.618033988749895);
        doubleValue_RoundingMode_helper("root 0 of x^5-x-1", "HALF_UP", 1.1673039782614187);

        doubleValue_RoundingMode_helper("0", "HALF_DOWN", 0.0);
        doubleValue_RoundingMode_helper("1", "HALF_DOWN", 1.0);
        doubleValue_RoundingMode_helper("1/2", "HALF_DOWN", 0.5);
        doubleValue_RoundingMode_helper("-4/3", "HALF_DOWN", -1.3333333333333333);
        doubleValue_RoundingMode_helper("sqrt(2)", "HALF_DOWN", 1.4142135623730951);
        doubleValue_RoundingMode_helper("-sqrt(2)", "HALF_DOWN", -1.4142135623730951);
        doubleValue_RoundingMode_helper("(1+sqrt(5))/2", "HALF_DOWN", 1.618033988749895);
        doubleValue_RoundingMode_helper("root 0 of x^5-x-1", "HALF_DOWN", 1.1673039782614187);

        doubleValue_RoundingMode_helper("0", "HALF_EVEN", 0.0);
        doubleValue_RoundingMode_helper("1", "HALF_EVEN", 1.0);
        doubleValue_RoundingMode_helper("1/2", "HALF_EVEN", 0.5);
        doubleValue_RoundingMode_helper("-4/3", "HALF_EVEN", -1.3333333333333333);
        doubleValue_RoundingMode_helper("sqrt(2)", "HALF_EVEN", 1.4142135623730951);
        doubleValue_RoundingMode_helper("-sqrt(2)", "HALF_EVEN", -1.4142135623730951);
        doubleValue_RoundingMode_helper("(1+sqrt(5))/2", "HALF_EVEN", 1.618033988749895);
        doubleValue_RoundingMode_helper("root 0 of x^5-x-1", "HALF_EVEN", 1.1673039782614187);

        doubleValue_RoundingMode_helper("0", "UNNECESSARY", 0.0f);
        doubleValue_RoundingMode_helper("1", "UNNECESSARY", 1.0f);
        doubleValue_RoundingMode_helper("1/2", "UNNECESSARY", 0.5f);

        doubleValue_RoundingMode_fail_helper("-4/3", "UNNECESSARY");
        doubleValue_RoundingMode_fail_helper("sqrt(2)", "UNNECESSARY");
        doubleValue_RoundingMode_fail_helper("-sqrt(2)", "UNNECESSARY");
        doubleValue_RoundingMode_fail_helper("(1+sqrt(5))/2", "UNNECESSARY");
        doubleValue_RoundingMode_fail_helper("root 0 of x^5-x-1", "UNNECESSARY");
    }

    private static void doubleValue_helper(@NotNull String input, double output) {
        aeq(readStrict(input).get().doubleValue(), output);
    }

    @Test
    public void testDoubleValue() {
        doubleValue_helper("0", 0.0);
        doubleValue_helper("1", 1.0);
        doubleValue_helper("1/2", 0.5);
        doubleValue_helper("-4/3", -1.3333333333333333);
        doubleValue_helper("sqrt(2)", 1.4142135623730951);
        doubleValue_helper("-sqrt(2)", -1.4142135623730951);
        doubleValue_helper("(1+sqrt(5))/2", 1.618033988749895);
        doubleValue_helper("root 0 of x^5-x-1", 1.1673039782614187);
    }

    private static void doubleValueExact_helper(@NotNull String input, double output) {
        aeq(readStrict(input).get().doubleValueExact(), output);
    }

    private static void doubleValueExact_fail_helper(@NotNull String x) {
        try {
            readStrict(x).get().doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDoubleValueExact() {
        doubleValueExact_helper("0", 0.0);
        doubleValueExact_helper("1", 1.0);
        doubleValueExact_helper("1/2", 0.5);

        doubleValueExact_fail_helper("-4/3");
        doubleValueExact_fail_helper("sqrt(2)");
        doubleValueExact_fail_helper("-sqrt(2)");
        doubleValueExact_fail_helper("(1+sqrt(5))/2");
        doubleValueExact_fail_helper("root 0 of x^5-x-1");
    }

    private static void hasTerminatingBaseExpansion_helper(@NotNull String r, @NotNull String base, boolean output) {
        aeq(readStrict(r).get().hasTerminatingBaseExpansion(Readers.readBigIntegerStrict(base).get()), output);
    }

    private static void hasTerminatingBaseExpansion_fail_helper(@NotNull String r, @NotNull String base) {
        try {
            readStrict(r).get().hasTerminatingBaseExpansion(Readers.readBigIntegerStrict(base).get());
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
        hasTerminatingBaseExpansion_helper("sqrt(2)", "2", false);

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
        hasTerminatingBaseExpansion_helper("sqrt(2)", "3", false);

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
        hasTerminatingBaseExpansion_helper("sqrt(2)", "4", false);

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
        hasTerminatingBaseExpansion_helper("sqrt(2)", "16", false);

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
        hasTerminatingBaseExpansion_helper("sqrt(2)", "83", false);

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
        hasTerminatingBaseExpansion_helper("sqrt(2)", "100", false);

        hasTerminatingBaseExpansion_fail_helper("1/2", "1");
        hasTerminatingBaseExpansion_fail_helper("1/2", "0");
        hasTerminatingBaseExpansion_fail_helper("1/2", "-1");
        hasTerminatingBaseExpansion_fail_helper("sqrt(2)", "1");
        hasTerminatingBaseExpansion_fail_helper("sqrt(2)", "0");
        hasTerminatingBaseExpansion_fail_helper("sqrt(2)", "-1");
    }

    private static void bigDecimalValueByPrecision_int_RoundingMode_helper(
            @NotNull String r,
            int precision,
            @NotNull String roundingMode,
            @NotNull String output
    ) {
        aeq(
                readStrict(r).get()
                        .bigDecimalValueByPrecision(precision, Readers.readRoundingModeStrict(roundingMode).get()),
                output
        );
    }

    private static void bigDecimalValueByPrecision_int_RoundingMode_fail_helper(
            @NotNull String r,
            int precision,
            @NotNull String roundingMode
    ) {
        try {
            readStrict(r).get()
                    .bigDecimalValueByPrecision(precision, Readers.readRoundingModeStrict(roundingMode).get());
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

        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "FLOOR", "0.015");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "CEILING", "0.016");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "DOWN", "0.015");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "UP", "0.016");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "HALF_DOWN", "0.016");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "HALF_UP", "0.016");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 2, "HALF_EVEN", "0.016");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "FLOOR", "0.0156");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "CEILING", "0.0157");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "DOWN", "0.0156");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "UP", "0.0157");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "HALF_DOWN", "0.0156");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "HALF_UP", "0.0156");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 3, "HALF_EVEN", "0.0156");

        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "FLOOR", "0.01562");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "CEILING", "0.01563");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "DOWN", "0.01562");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "UP", "0.01563");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "HALF_DOWN", "0.01562");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "HALF_UP", "0.01563");
        bigDecimalValueByPrecision_int_RoundingMode_helper("1/64", 4, "HALF_EVEN", "0.01562");

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

        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "FLOOR", "-0.4");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "CEILING", "-0.3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "DOWN", "-0.3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "UP", "-0.4");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "HALF_DOWN", "-0.3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "HALF_UP", "-0.3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 1, "HALF_EVEN", "-0.3");

        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "FLOOR", "-0.34");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "CEILING", "-0.33");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "DOWN", "-0.33");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "UP", "-0.34");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "HALF_DOWN", "-0.33");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "HALF_UP", "-0.33");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 2, "HALF_EVEN", "-0.33");

        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "FLOOR", "-0.334");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "CEILING", "-0.333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "DOWN", "-0.333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "UP", "-0.334");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "HALF_DOWN", "-0.333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "HALF_UP", "-0.333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 3, "HALF_EVEN", "-0.333");

        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "FLOOR", "-0.3334");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "CEILING", "-0.3333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "DOWN", "-0.3333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "UP", "-0.3334");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "HALF_DOWN", "-0.3333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "HALF_UP", "-0.3333");
        bigDecimalValueByPrecision_int_RoundingMode_helper("-1/3", 4, "HALF_EVEN", "-0.3333");

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

        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "FLOOR", "6.7E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "CEILING", "6.8E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "DOWN", "6.7E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "UP", "6.8E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "HALF_DOWN", "6.8E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "HALF_UP", "6.8E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 2, "HALF_EVEN", "6.8E+3");

        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "FLOOR", "6.78E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "CEILING", "6.79E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "DOWN", "6.78E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "UP", "6.79E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "HALF_DOWN", "6.79E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "HALF_UP", "6.79E+3");
        bigDecimalValueByPrecision_int_RoundingMode_helper("6789", 3, "HALF_EVEN", "6.79E+3");

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

        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "FLOOR", "0.99");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "CEILING", "1.0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "DOWN", "0.99");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "UP", "1.0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "HALF_DOWN", "0.99");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "HALF_UP", "1.0");
        bigDecimalValueByPrecision_int_RoundingMode_helper("199/200", 2, "HALF_EVEN", "1.0");

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

        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 1, "FLOOR", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 1, "CEILING", "2");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 1, "DOWN", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 1, "UP", "2");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 1, "HALF_DOWN", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 1, "HALF_UP", "1");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 1, "HALF_EVEN", "1");

        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 2, "FLOOR", "1.4");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 2, "CEILING", "1.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 2, "DOWN", "1.4");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 2, "UP", "1.5");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 2, "HALF_DOWN", "1.4");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 2, "HALF_UP", "1.4");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 2, "HALF_EVEN", "1.4");

        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 3, "FLOOR", "1.41");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 3, "CEILING", "1.42");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 3, "DOWN", "1.41");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 3, "UP", "1.42");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 3, "HALF_DOWN", "1.41");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 3, "HALF_UP", "1.41");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 3, "HALF_EVEN", "1.41");

        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 4, "FLOOR", "1.414");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 4, "CEILING", "1.415");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 4, "DOWN", "1.414");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 4, "UP", "1.415");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 4, "HALF_DOWN", "1.414");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 4, "HALF_UP", "1.414");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 4, "HALF_EVEN", "1.414");

        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 5, "FLOOR", "1.4142");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 5, "CEILING", "1.4143");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 5, "DOWN", "1.4142");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 5, "UP", "1.4143");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 5, "HALF_DOWN", "1.4142");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 5, "HALF_UP", "1.4142");
        bigDecimalValueByPrecision_int_RoundingMode_helper("sqrt(2)", 5, "HALF_EVEN", "1.4142");

        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("1/64", 1, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("1/64", 2, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("1/64", 3, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("1/64", 4, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "FLOOR");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "CEILING");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "DOWN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "UP");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "HALF_DOWN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "HALF_UP");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "HALF_EVEN");

        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 0, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 1, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 2, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 3, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("-1/3", 4, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("6789", 1, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("6789", 2, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("6789", 3, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("199/200", 1, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("199/200", 2, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", 0, "FLOOR");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", 0, "CEILING");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", 0, "DOWN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", 0, "UP");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", 0, "HALF_DOWN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", 0, "HALF_UP");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", 0, "HALF_EVEN");

        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", 0, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", 1, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", 2, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", 3, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", 4, "UNNECESSARY");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", 5, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "FLOOR");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "CEILING");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "DOWN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "UP");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "HALF_DOWN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "HALF_UP");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "HALF_EVEN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("5", -1, "UNNECESSARY");

        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", -1, "FLOOR");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", -1, "CEILING");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", -1, "DOWN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", -1, "UP");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", -1, "HALF_DOWN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", -1, "HALF_UP");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", -1, "HALF_EVEN");
        bigDecimalValueByPrecision_int_RoundingMode_fail_helper("sqrt(2)", -1, "UNNECESSARY");
    }

    private static void bigDecimalValueByScale_int_RoundingMode_helper(
            @NotNull String r,
            int scale,
            @NotNull String roundingMode,
            @NotNull String output
    ) {
        aeq(
                readStrict(r).get().bigDecimalValueByScale(scale, Readers.readRoundingModeStrict(roundingMode).get()),
                output
        );
    }

    private static void bigDecimalValueByScale_int_RoundingMode_fail_helper(
            @NotNull String r,
            int scale,
            @NotNull String roundingMode
    ) {
        try {
            readStrict(r).get().bigDecimalValueByScale(scale, Readers.readRoundingModeStrict(roundingMode).get());
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
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

        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "FLOOR", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "CEILING", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "DOWN", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "UP", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "HALF_DOWN", "0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "HALF_UP", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("1/2", 0, "HALF_EVEN", "0");

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

        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "FLOOR", "0.0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "CEILING", "0.1");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "DOWN", "0.0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "UP", "0.1");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "HALF_DOWN", "0.0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "HALF_UP", "0.0");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 1, "HALF_EVEN", "0.0");

        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "FLOOR", "0.01");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "CEILING", "0.02");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "DOWN", "0.01");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "UP", "0.02");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "HALF_DOWN", "0.02");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "HALF_UP", "0.02");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 2, "HALF_EVEN", "0.02");

        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "FLOOR", "0.015");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "CEILING", "0.016");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "DOWN", "0.015");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "UP", "0.016");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "HALF_DOWN", "0.016");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "HALF_UP", "0.016");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 3, "HALF_EVEN", "0.016");

        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "FLOOR", "0.0156");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "CEILING", "0.0157");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "DOWN", "0.0156");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "UP", "0.0157");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "HALF_DOWN", "0.0156");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "HALF_UP", "0.0156");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 4, "HALF_EVEN", "0.0156");

        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "FLOOR", "0.01562");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "CEILING", "0.01563");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "DOWN", "0.01562");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "UP", "0.01563");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "HALF_DOWN", "0.01562");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "HALF_UP", "0.01563");
        bigDecimalValueByScale_int_RoundingMode_helper("1/64", 5, "HALF_EVEN", "0.01562");

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

        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "FLOOR", "-0.4");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "CEILING", "-0.3");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "DOWN", "-0.3");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "UP", "-0.4");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "HALF_DOWN", "-0.3");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "HALF_UP", "-0.3");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 1, "HALF_EVEN", "-0.3");

        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "FLOOR", "-0.34");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "CEILING", "-0.33");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "DOWN", "-0.33");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "UP", "-0.34");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "HALF_DOWN", "-0.33");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "HALF_UP", "-0.33");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 2, "HALF_EVEN", "-0.33");

        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "FLOOR", "-0.334");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "CEILING", "-0.333");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "DOWN", "-0.333");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "UP", "-0.334");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "HALF_DOWN", "-0.333");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "HALF_UP", "-0.333");
        bigDecimalValueByScale_int_RoundingMode_helper("-1/3", 3, "HALF_EVEN", "-0.333");

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

        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "FLOOR", "6.7E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "CEILING", "6.8E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "DOWN", "6.7E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "UP", "6.8E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "HALF_DOWN", "6.8E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "HALF_UP", "6.8E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -2, "HALF_EVEN", "6.8E+3");

        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "FLOOR", "6E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "CEILING", "7E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "DOWN", "6E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "UP", "7E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "HALF_DOWN", "7E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "HALF_UP", "7E+3");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -3, "HALF_EVEN", "7E+3");

        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "FLOOR", "0E+4");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "CEILING", "1E+4");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "DOWN", "0E+4");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "UP", "1E+4");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "HALF_DOWN", "1E+4");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "HALF_UP", "1E+4");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -4, "HALF_EVEN", "1E+4");

        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "FLOOR", "0E+5");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "CEILING", "1E+5");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "DOWN", "0E+5");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "UP", "1E+5");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "HALF_DOWN", "0E+5");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "HALF_UP", "0E+5");
        bigDecimalValueByScale_int_RoundingMode_helper("6789", -5, "HALF_EVEN", "0E+5");

        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 0, "FLOOR", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 0, "CEILING", "2");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 0, "DOWN", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 0, "UP", "2");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 0, "HALF_DOWN", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 0, "HALF_UP", "1");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 0, "HALF_EVEN", "1");

        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 1, "FLOOR", "1.4");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 1, "CEILING", "1.5");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 1, "DOWN", "1.4");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 1, "UP", "1.5");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 1, "HALF_DOWN", "1.4");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 1, "HALF_UP", "1.4");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 1, "HALF_EVEN", "1.4");

        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 2, "FLOOR", "1.41");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 2, "CEILING", "1.42");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 2, "DOWN", "1.41");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 2, "UP", "1.42");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 2, "HALF_DOWN", "1.41");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 2, "HALF_UP", "1.41");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 2, "HALF_EVEN", "1.41");

        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 3, "FLOOR", "1.414");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 3, "CEILING", "1.415");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 3, "DOWN", "1.414");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 3, "UP", "1.415");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 3, "HALF_DOWN", "1.414");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 3, "HALF_UP", "1.414");
        bigDecimalValueByScale_int_RoundingMode_helper("sqrt(2)", 3, "HALF_EVEN", "1.414");

        bigDecimalValueByScale_int_RoundingMode_fail_helper("1", -3, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/2", 0, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/64", 0, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/64", 1, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/64", 2, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/64", 3, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/64", 4, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("1/64", 5, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_fail_helper("-1/3", 0, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("-1/3", 1, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("-1/3", 2, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("-1/3", 3, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_fail_helper("6789", -1, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("6789", -2, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("6789", -3, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("6789", -4, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("6789", -5, "UNNECESSARY");

        bigDecimalValueByScale_int_RoundingMode_fail_helper("sqrt(2)", 0, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("sqrt(2)", 1, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("sqrt(2)", 2, "UNNECESSARY");
        bigDecimalValueByScale_int_RoundingMode_fail_helper("sqrt(2)", 3, "UNNECESSARY");
    }

    private static void bigDecimalValueByPrecision_int_helper(
            @NotNull String r,
            int precision,
            @NotNull String output
    ) {
        aeq(readStrict(r).get().bigDecimalValueByPrecision(precision), output);
    }

    private static void bigDecimalValueByPrecision_int_fail_helper(@NotNull String r, int precision) {
        try {
            readStrict(r).get().bigDecimalValueByPrecision(precision);
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

        bigDecimalValueByPrecision_int_helper("sqrt(2)", 1, "1");
        bigDecimalValueByPrecision_int_helper("sqrt(2)", 2, "1.4");
        bigDecimalValueByPrecision_int_helper("sqrt(2)", 3, "1.41");
        bigDecimalValueByPrecision_int_helper("sqrt(2)", 4, "1.414");
        bigDecimalValueByPrecision_int_helper("sqrt(2)", 5, "1.4142");
        bigDecimalValueByPrecision_int_helper("sqrt(2)", 6, "1.41421");

        bigDecimalValueByPrecision_int_fail_helper("-1/3", 0);
        bigDecimalValueByPrecision_int_fail_helper("sqrt(2)", 0);
        bigDecimalValueByPrecision_int_fail_helper("5", -1);
        bigDecimalValueByPrecision_int_fail_helper("sqrt(2)", -1);
    }

    private static void bigDecimalValueByScale_int_helper(@NotNull String r, int scale, @NotNull String output) {
        aeq(readStrict(r).get().bigDecimalValueByScale(scale), output);
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

        bigDecimalValueByScale_int_helper("sqrt(2)", 0, "1");
        bigDecimalValueByScale_int_helper("sqrt(2)", 1, "1.4");
        bigDecimalValueByScale_int_helper("sqrt(2)", 2, "1.41");
        bigDecimalValueByScale_int_helper("sqrt(2)", 3, "1.414");
        bigDecimalValueByScale_int_helper("sqrt(2)", 4, "1.4142");
        bigDecimalValueByScale_int_helper("sqrt(2)", 5, "1.41421");
        bigDecimalValueByScale_int_helper("sqrt(2)", 6, "1.414214");
    }

    private static void bigDecimalValueExact_helper(@NotNull Algebraic input, @NotNull String output) {
        aeq(input.bigDecimalValueExact(), output);
    }

    private static void bigDecimalValueExact_helper(@NotNull String input, @NotNull String output) {
        bigDecimalValueExact_helper(readStrict(input).get(), output);
    }

    private static void bigDecimalValueExact_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().bigDecimalValueExact();
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

        bigDecimalValueExact_helper(of(Rational.SMALLEST_FLOAT),
                "1.4012984643248170709237295832899161312802619418765157717570682838897910826858606014866381883621215" +
                "8203125E-45");
        bigDecimalValueExact_helper(of(Rational.LARGEST_FLOAT), "340282346638528859811704183484516925440");
        bigDecimalValueExact_helper(of(Rational.SMALLEST_DOUBLE),
                "4.9406564584124654417656879286822137236505980261432476442558568250067550727020875186529983636163599" +
                "237979656469544571773092665671035593979639877479601078187812630071319031140452784581716784898210368" +
                "871863605699873072305000638740915356498438731247339727316961514003171538539807412623856559117102665" +
                "855668676818703956031062493194527159149245532930545654440112748012970999954193198940908041656332452" +
                "475714786901472678015935523861155013480352649347201937902681071074917033322268447533357208324319360" +
                "923828934583680601060115061698097530783422773183292479049825247307763759272478746560847782037344696" +
                "995336470179726777175851256605511991315048911014510378627381672509558373897335989936648099411642057" +
                "02637090279242767544565229087538682506419718265533447265625E-324");
        bigDecimalValueExact_helper(of(Rational.LARGEST_DOUBLE),
                "179769313486231570814527423731704356798070567525844996598917476803157260780028538760589558632766878" +
                "171540458953514382464234321326889464182768467546703537516986049910576551282076245490090389328944075" +
                "868508455133942304583236903222948165808559332123348274797826204144723168738177180919299881250404026" +
                "184124858368");

        bigDecimalValueExact_fail_helper("1/3");
        bigDecimalValueExact_fail_helper("sqrt(2)");
    }

    private static void minimalPolynomial_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().minimalPolynomial(), output);
    }

    @Test
    public void testMinimalPolynomial() {
        minimalPolynomial_helper("0", "x");
        minimalPolynomial_helper("1", "x-1");
        minimalPolynomial_helper("1/2", "2*x-1");
        minimalPolynomial_helper("-4/3", "3*x+4");
        minimalPolynomial_helper("sqrt(2)", "x^2-2");
        minimalPolynomial_helper("-sqrt(2)", "x^2-2");
        minimalPolynomial_helper("(1+sqrt(5))/2", "x^2-x-1");
        minimalPolynomial_helper("root 0 of x^5-x-1", "x^5-x-1");
    }

    private static void rootIndex_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().rootIndex(), output);
    }

    @Test
    public void testRootIndex() {
        rootIndex_helper("0", 0);
        rootIndex_helper("1", 0);
        rootIndex_helper("1/2", 0);
        rootIndex_helper("-4/3", 0);
        rootIndex_helper("sqrt(2)", 1);
        rootIndex_helper("-sqrt(2)", 0);
        rootIndex_helper("(1+sqrt(5))/2", 1);
        rootIndex_helper("root 0 of x^5-x-1", 0);
    }

    private static void degree_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().degree(), output);
    }

    @Test
    public void testDegree() {
        degree_helper("0", 1);
        degree_helper("1", 1);
        degree_helper("1/2", 1);
        degree_helper("-4/3", 1);
        degree_helper("sqrt(2)", 2);
        degree_helper("-sqrt(2)", 2);
        degree_helper("(1+sqrt(5))/2", 2);
        degree_helper("root 0 of x^5-x-1", 5);
    }

    private static void isolatingInterval_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().isolatingInterval(), output);
    }

    @Test
    public void testIsolatingInterval() {
        isolatingInterval_helper("0", "[0, 0]");
        isolatingInterval_helper("1", "[1, 1]");
        isolatingInterval_helper("1/2", "[1/2, 1/2]");
        isolatingInterval_helper("-4/3", "[-4/3, -4/3]");
        isolatingInterval_helper("sqrt(2)", "[0, 4]");
        isolatingInterval_helper("-sqrt(2)", "[-4, 0]");
        isolatingInterval_helper("(1+sqrt(5))/2", "[0, 2]");
        isolatingInterval_helper("root 0 of x^5-x-1", "[-2, 2]");
    }

    private static void minimalPolynomialRootCount_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().minimalPolynomialRootCount(), output);
    }

    @Test
    public void testMinimalPolynomialRootCount() {
        minimalPolynomialRootCount_helper("0", 1);
        minimalPolynomialRootCount_helper("1", 1);
        minimalPolynomialRootCount_helper("1/2", 1);
        minimalPolynomialRootCount_helper("-4/3", 1);
        minimalPolynomialRootCount_helper("sqrt(2)", 2);
        minimalPolynomialRootCount_helper("-sqrt(2)", 2);
        minimalPolynomialRootCount_helper("(1+sqrt(5))/2", 2);
        minimalPolynomialRootCount_helper("root 0 of x^5-x-1", 1);
    }

    private static void intervalExtension_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(intervalExtension(readStrict(a).get(), readStrict(b).get()), output);
    }

    private static void intervalExtension_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            intervalExtension(readStrict(a).get(), readStrict(b).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testIntervalExtension() {
        intervalExtension_helper("-sqrt(2)", "-4/3", "[-23/16, -4/3]");
        intervalExtension_helper("-sqrt(2)", "0", "[-2, 0]");
        intervalExtension_helper("-sqrt(2)", "1/2", "[-2, 1/2]");
        intervalExtension_helper("-sqrt(2)", "1", "[-2, 1]");
        intervalExtension_helper("-sqrt(2)", "root 0 of x^5-x-1", "[-2, 2]");
        intervalExtension_helper("-sqrt(2)", "sqrt(2)", "[-2, 2]");
        intervalExtension_helper("-sqrt(2)", "(1+sqrt(5))/2", "[-2, 2]");

        intervalExtension_helper("-4/3", "0", "[-4/3, 0]");
        intervalExtension_helper("-4/3", "1/2", "[-4/3, 1/2]");
        intervalExtension_helper("-4/3", "1", "[-4/3, 1]");
        intervalExtension_helper("-4/3", "root 0 of x^5-x-1", "[-4/3, 2]");
        intervalExtension_helper("-4/3", "sqrt(2)", "[-4/3, 2]");
        intervalExtension_helper("-4/3", "(1+sqrt(5))/2", "[-4/3, 2]");

        intervalExtension_helper("0", "1/2", "[0, 1/2]");
        intervalExtension_helper("0", "1", "[0, 1]");
        intervalExtension_helper("0", "root 0 of x^5-x-1", "[0, 2]");
        intervalExtension_helper("0", "sqrt(2)", "[0, 2]");
        intervalExtension_helper("0", "(1+sqrt(5))/2", "[0, 2]");

        intervalExtension_helper("1/2", "1", "[1/2, 1]");
        intervalExtension_helper("1/2", "root 0 of x^5-x-1", "[1/2, 3/2]");
        intervalExtension_helper("1/2", "sqrt(2)", "[1/2, 3/2]");
        intervalExtension_helper("1/2", "(1+sqrt(5))/2", "[1/2, 2]");

        intervalExtension_helper("1", "root 0 of x^5-x-1", "[1, 5/4]");
        intervalExtension_helper("1", "sqrt(2)", "[1, 3/2]");
        intervalExtension_helper("1", "(1+sqrt(5))/2", "[1, 2]");

        intervalExtension_helper("root 0 of x^5-x-1", "sqrt(2)", "[9/8, 23/16]");
        intervalExtension_helper("root 0 of x^5-x-1", "(1+sqrt(5))/2", "[9/8, 13/8]");

        intervalExtension_helper("sqrt(2)", "(1+sqrt(5))/2", "[11/8, 13/8]");

        intervalExtension_fail_helper("0", "0");
        intervalExtension_fail_helper("sqrt(2)", "sqrt(2)");
        intervalExtension_fail_helper("sqrt(2)", "0");
        intervalExtension_fail_helper("sqrt(2)", "-sqrt(2)");
        intervalExtension_fail_helper("1", "0");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        Algebraic x = readStrict(input).get().negate();
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testNegate() {
        negate_helper("0", "0");
        negate_helper("1", "-1");
        negate_helper("1/2", "-1/2");
        negate_helper("-4/3", "4/3");
        negate_helper("sqrt(2)", "-sqrt(2)");
        negate_helper("-sqrt(2)", "sqrt(2)");
        negate_helper("(1+sqrt(5))/2", "(-1-sqrt(5))/2");
        negate_helper("root 0 of x^5-x-1", "root 0 of x^5-x+1");
    }

    private static void abs_helper(@NotNull String input, @NotNull String output) {
        Algebraic x = readStrict(input).get().abs();
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testAbs() {
        abs_helper("0", "0");
        abs_helper("1", "1");
        abs_helper("1/2", "1/2");
        abs_helper("-4/3", "4/3");
        abs_helper("sqrt(2)", "sqrt(2)");
        abs_helper("-sqrt(2)", "sqrt(2)");
        abs_helper("(1+sqrt(5))/2", "(1+sqrt(5))/2");
        abs_helper("root 0 of x^5-x-1", "root 0 of x^5-x-1");
    }

    private static void signum_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().signum(), output);
    }

    @Test
    public void testSignum() {
        signum_helper("0", 0);
        signum_helper("1", 1);
        signum_helper("1/2", 1);
        signum_helper("-4/3", -1);
        signum_helper("sqrt(2)", 1);
        signum_helper("-sqrt(2)", -1);
        signum_helper("(1+sqrt(5))/2", 1);
        signum_helper("root 0 of x^5-x-1", 1);
    }

    private static void add_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        Algebraic x = readStrict(a).get().add(Readers.readBigIntegerStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testAdd_BigInteger() {
        add_BigInteger_helper("0", "0", "0");
        add_BigInteger_helper("0", "1", "1");
        add_BigInteger_helper("0", "-1", "-1");
        add_BigInteger_helper("0", "100", "100");

        add_BigInteger_helper("1", "0", "1");
        add_BigInteger_helper("1", "1", "2");
        add_BigInteger_helper("1", "-1", "0");
        add_BigInteger_helper("1", "100", "101");

        add_BigInteger_helper("-1", "0", "-1");
        add_BigInteger_helper("-1", "1", "0");
        add_BigInteger_helper("-1", "-1", "-2");
        add_BigInteger_helper("-1", "100", "99");

        add_BigInteger_helper("1/2", "0", "1/2");
        add_BigInteger_helper("1/2", "1", "3/2");
        add_BigInteger_helper("1/2", "-1", "-1/2");
        add_BigInteger_helper("1/2", "100", "201/2");

        add_BigInteger_helper("-4/3", "0", "-4/3");
        add_BigInteger_helper("-4/3", "1", "-1/3");
        add_BigInteger_helper("-4/3", "-1", "-7/3");
        add_BigInteger_helper("-4/3", "100", "296/3");

        add_BigInteger_helper("sqrt(2)", "0", "sqrt(2)");
        add_BigInteger_helper("sqrt(2)", "1", "1+sqrt(2)");
        add_BigInteger_helper("sqrt(2)", "-1", "-1+sqrt(2)");
        add_BigInteger_helper("sqrt(2)", "100", "100+sqrt(2)");

        add_BigInteger_helper("-sqrt(2)", "0", "-sqrt(2)");
        add_BigInteger_helper("-sqrt(2)", "1", "1-sqrt(2)");
        add_BigInteger_helper("-sqrt(2)", "-1", "-1-sqrt(2)");
        add_BigInteger_helper("-sqrt(2)", "100", "100-sqrt(2)");

        add_BigInteger_helper("(1+sqrt(5))/2", "0", "(1+sqrt(5))/2");
        add_BigInteger_helper("(1+sqrt(5))/2", "1", "(3+sqrt(5))/2");
        add_BigInteger_helper("(1+sqrt(5))/2", "-1", "(-1+sqrt(5))/2");
        add_BigInteger_helper("(1+sqrt(5))/2", "100", "(201+sqrt(5))/2");

        add_BigInteger_helper("root 0 of x^5-x-1", "0", "root 0 of x^5-x-1");
        add_BigInteger_helper("root 0 of x^5-x-1", "1", "root 0 of x^5-5*x^4+10*x^3-10*x^2+4*x-1");
        add_BigInteger_helper("root 0 of x^5-x-1", "-1", "root 0 of x^5+5*x^4+10*x^3+10*x^2+4*x-1");
        add_BigInteger_helper("root 0 of x^5-x-1", "100",
                "root 0 of x^5-500*x^4+100000*x^3-10000000*x^2+499999999*x-9999999901");
    }

    private static void add_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        Algebraic x = readStrict(a).get().add(Rational.readStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testAdd_Rational() {
        add_Rational_helper("0", "0", "0");
        add_Rational_helper("0", "1", "1");
        add_Rational_helper("0", "-1", "-1");
        add_Rational_helper("0", "100/3", "100/3");
        add_Rational_helper("0", "1/100", "1/100");

        add_Rational_helper("1", "0", "1");
        add_Rational_helper("1", "1", "2");
        add_Rational_helper("1", "-1", "0");
        add_Rational_helper("1", "100/3", "103/3");
        add_Rational_helper("1", "1/100", "101/100");

        add_Rational_helper("-1", "0", "-1");
        add_Rational_helper("-1", "1", "0");
        add_Rational_helper("-1", "-1", "-2");
        add_Rational_helper("-1", "100/3", "97/3");
        add_Rational_helper("-1", "1/100", "-99/100");

        add_Rational_helper("1/2", "0", "1/2");
        add_Rational_helper("1/2", "1", "3/2");
        add_Rational_helper("1/2", "-1", "-1/2");
        add_Rational_helper("1/2", "100/3", "203/6");
        add_Rational_helper("1/2", "1/100", "51/100");

        add_Rational_helper("-4/3", "0", "-4/3");
        add_Rational_helper("-4/3", "1", "-1/3");
        add_Rational_helper("-4/3", "-1", "-7/3");
        add_Rational_helper("-4/3", "100/3", "32");
        add_Rational_helper("-4/3", "1/100", "-397/300");

        add_Rational_helper("sqrt(2)", "0", "sqrt(2)");
        add_Rational_helper("sqrt(2)", "1", "1+sqrt(2)");
        add_Rational_helper("sqrt(2)", "-1", "-1+sqrt(2)");
        add_Rational_helper("sqrt(2)", "100/3", "(100+3*sqrt(2))/3");
        add_Rational_helper("sqrt(2)", "1/100", "(1+100*sqrt(2))/100");

        add_Rational_helper("-sqrt(2)", "0", "-sqrt(2)");
        add_Rational_helper("-sqrt(2)", "1", "1-sqrt(2)");
        add_Rational_helper("-sqrt(2)", "-1", "-1-sqrt(2)");
        add_Rational_helper("-sqrt(2)", "100/3", "(100-3*sqrt(2))/3");
        add_Rational_helper("-sqrt(2)", "1/100", "(1-100*sqrt(2))/100");

        add_Rational_helper("(1+sqrt(5))/2", "0", "(1+sqrt(5))/2");
        add_Rational_helper("(1+sqrt(5))/2", "1", "(3+sqrt(5))/2");
        add_Rational_helper("(1+sqrt(5))/2", "-1", "(-1+sqrt(5))/2");
        add_Rational_helper("(1+sqrt(5))/2", "100/3", "(203+3*sqrt(5))/6");
        add_Rational_helper("(1+sqrt(5))/2", "1/100", "(51+50*sqrt(5))/100");

        add_Rational_helper("root 0 of x^5-x-1", "0", "root 0 of x^5-x-1");
        add_Rational_helper("root 0 of x^5-x-1", "1", "root 0 of x^5-5*x^4+10*x^3-10*x^2+4*x-1");
        add_Rational_helper("root 0 of x^5-x-1", "-1", "root 0 of x^5+5*x^4+10*x^3+10*x^2+4*x-1");
        add_Rational_helper("root 0 of x^5-x-1", "100/3",
                "root 0 of 243*x^5-40500*x^4+2700000*x^3-90000000*x^2+1499999757*x-9999992143");
        add_Rational_helper("root 0 of x^5-x-1", "1/100",
                "root 0 of 10000000000*x^5-500000000*x^4+10000000*x^3-100000*x^2-9999999500*x-9900000001");
    }

    private static void add_Algebraic_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        Algebraic x = readStrict(a).get().add(readStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testAdd_Algebraic() {
        add_Algebraic_helper("0", "0", "0");
        add_Algebraic_helper("0", "1", "1");
        add_Algebraic_helper("0", "-1", "-1");
        add_Algebraic_helper("0", "1/2", "1/2");
        add_Algebraic_helper("0", "-4/3", "-4/3");
        add_Algebraic_helper("0", "sqrt(2)", "sqrt(2)");
        add_Algebraic_helper("0", "-sqrt(2)", "-sqrt(2)");
        add_Algebraic_helper("0", "(1+sqrt(5))/2", "(1+sqrt(5))/2");
        add_Algebraic_helper("0", "root 0 of x^5-x-1", "root 0 of x^5-x-1");

        add_Algebraic_helper("1", "0", "1");
        add_Algebraic_helper("1", "1", "2");
        add_Algebraic_helper("1", "-1", "0");
        add_Algebraic_helper("1", "1/2", "3/2");
        add_Algebraic_helper("1", "-4/3", "-1/3");
        add_Algebraic_helper("1", "sqrt(2)", "1+sqrt(2)");
        add_Algebraic_helper("1", "-sqrt(2)", "1-sqrt(2)");
        add_Algebraic_helper("1", "(1+sqrt(5))/2", "(3+sqrt(5))/2");
        add_Algebraic_helper("1", "root 0 of x^5-x-1", "root 0 of x^5-5*x^4+10*x^3-10*x^2+4*x-1");

        add_Algebraic_helper("1/2", "0", "1/2");
        add_Algebraic_helper("1/2", "1", "3/2");
        add_Algebraic_helper("1/2", "-1", "-1/2");
        add_Algebraic_helper("1/2", "1/2", "1");
        add_Algebraic_helper("1/2", "-4/3", "-5/6");
        add_Algebraic_helper("1/2", "sqrt(2)", "(1+2*sqrt(2))/2");
        add_Algebraic_helper("1/2", "-sqrt(2)", "(1-2*sqrt(2))/2");
        add_Algebraic_helper("1/2", "(1+sqrt(5))/2", "(2+sqrt(5))/2");
        add_Algebraic_helper("1/2", "root 0 of x^5-x-1", "root 0 of 32*x^5-80*x^4+80*x^3-40*x^2-22*x-17");

        add_Algebraic_helper("-4/3", "0", "-4/3");
        add_Algebraic_helper("-4/3", "1", "-1/3");
        add_Algebraic_helper("-4/3", "-1", "-7/3");
        add_Algebraic_helper("-4/3", "1/2", "-5/6");
        add_Algebraic_helper("-4/3", "-4/3", "-8/3");
        add_Algebraic_helper("-4/3", "sqrt(2)", "(-4+3*sqrt(2))/3");
        add_Algebraic_helper("-4/3", "-sqrt(2)", "(-4-3*sqrt(2))/3");
        add_Algebraic_helper("-4/3", "(1+sqrt(5))/2", "(-5+3*sqrt(5))/6");
        add_Algebraic_helper("-4/3", "root 0 of x^5-x-1", "root 0 of 243*x^5+1620*x^4+4320*x^3+5760*x^2+3597*x+457");

        add_Algebraic_helper("sqrt(2)", "0", "sqrt(2)");
        add_Algebraic_helper("sqrt(2)", "1", "1+sqrt(2)");
        add_Algebraic_helper("sqrt(2)", "-1", "-1+sqrt(2)");
        add_Algebraic_helper("sqrt(2)", "1/2", "(1+2*sqrt(2))/2");
        add_Algebraic_helper("sqrt(2)", "-4/3", "(-4+3*sqrt(2))/3");
        add_Algebraic_helper("sqrt(2)", "sqrt(2)", "2*sqrt(2)");
        add_Algebraic_helper("sqrt(2)", "-sqrt(2)", "0");
        add_Algebraic_helper("sqrt(2)", "(1+sqrt(5))/2", "root 3 of x^4-2*x^3-5*x^2+6*x-1");
        add_Algebraic_helper("sqrt(2)", "root 0 of x^5-x-1",
                "root 1 of x^10-10*x^8+38*x^6-2*x^5-100*x^4-40*x^3+121*x^2-38*x-17");

        add_Algebraic_helper("-sqrt(2)", "0", "-sqrt(2)");
        add_Algebraic_helper("-sqrt(2)", "1", "1-sqrt(2)");
        add_Algebraic_helper("-sqrt(2)", "-1", "-1-sqrt(2)");
        add_Algebraic_helper("-sqrt(2)", "1/2", "(1-2*sqrt(2))/2");
        add_Algebraic_helper("-sqrt(2)", "-4/3", "(-4-3*sqrt(2))/3");
        add_Algebraic_helper("-sqrt(2)", "sqrt(2)", "0");
        add_Algebraic_helper("-sqrt(2)", "-sqrt(2)", "-2*sqrt(2)");
        add_Algebraic_helper("-sqrt(2)", "(1+sqrt(5))/2", "root 1 of x^4-2*x^3-5*x^2+6*x-1");
        add_Algebraic_helper("-sqrt(2)", "root 0 of x^5-x-1",
                "root 0 of x^10-10*x^8+38*x^6-2*x^5-100*x^4-40*x^3+121*x^2-38*x-17");

        add_Algebraic_helper("(1+sqrt(5))/2", "0", "(1+sqrt(5))/2");
        add_Algebraic_helper("(1+sqrt(5))/2", "1", "(3+sqrt(5))/2");
        add_Algebraic_helper("(1+sqrt(5))/2", "-1", "(-1+sqrt(5))/2");
        add_Algebraic_helper("(1+sqrt(5))/2", "1/2", "(2+sqrt(5))/2");
        add_Algebraic_helper("(1+sqrt(5))/2", "-4/3", "(-5+3*sqrt(5))/6");
        add_Algebraic_helper("(1+sqrt(5))/2", "sqrt(2)", "root 3 of x^4-2*x^3-5*x^2+6*x-1");
        add_Algebraic_helper("(1+sqrt(5))/2", "-sqrt(2)", "root 1 of x^4-2*x^3-5*x^2+6*x-1");
        add_Algebraic_helper("(1+sqrt(5))/2", "(1+sqrt(5))/2", "1+sqrt(5)");
        add_Algebraic_helper("(1+sqrt(5))/2", "root 0 of x^5-x-1",
                "root 1 of x^10-5*x^9+5*x^8+10*x^7-17*x^6-7*x^5+10*x^3+31*x^2-48*x+16");

        add_Algebraic_helper("root 0 of x^5-x-1", "0", "root 0 of x^5-x-1");
        add_Algebraic_helper("root 0 of x^5-x-1", "1", "root 0 of x^5-5*x^4+10*x^3-10*x^2+4*x-1");
        add_Algebraic_helper("root 0 of x^5-x-1", "-1", "root 0 of x^5+5*x^4+10*x^3+10*x^2+4*x-1");
        add_Algebraic_helper("root 0 of x^5-x-1", "1/2", "root 0 of 32*x^5-80*x^4+80*x^3-40*x^2-22*x-17");
        add_Algebraic_helper("root 0 of x^5-x-1", "-4/3", "root 0 of 243*x^5+1620*x^4+4320*x^3+5760*x^2+3597*x+457");
        add_Algebraic_helper("root 0 of x^5-x-1", "sqrt(2)",
                "root 1 of x^10-10*x^8+38*x^6-2*x^5-100*x^4-40*x^3+121*x^2-38*x-17");
        add_Algebraic_helper("root 0 of x^5-x-1", "-sqrt(2)",
                "root 0 of x^10-10*x^8+38*x^6-2*x^5-100*x^4-40*x^3+121*x^2-38*x-17");
        add_Algebraic_helper("root 0 of x^5-x-1", "(1+sqrt(5))/2",
                "root 1 of x^10-5*x^9+5*x^8+10*x^7-17*x^6-7*x^5+10*x^3+31*x^2-48*x+16");
        add_Algebraic_helper("root 0 of x^5-x-1", "root 0 of x^5-x-1", "root 0 of x^5-16*x-32");

        add_Algebraic_helper("sqrt(2)", "sqrt(3)", "root 3 of x^4-10*x^2+1");
        add_Algebraic_helper("sqrt(2)", "1-sqrt(2)", "1");
    }

    private static void subtract_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        Algebraic x = readStrict(a).get().subtract(Readers.readBigIntegerStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testSubtract_BigInteger() {
        subtract_BigInteger_helper("0", "0", "0");
        subtract_BigInteger_helper("0", "1", "-1");
        subtract_BigInteger_helper("0", "-1", "1");
        subtract_BigInteger_helper("0", "100", "-100");

        subtract_BigInteger_helper("1", "0", "1");
        subtract_BigInteger_helper("1", "1", "0");
        subtract_BigInteger_helper("1", "-1", "2");
        subtract_BigInteger_helper("1", "100", "-99");

        subtract_BigInteger_helper("-1", "0", "-1");
        subtract_BigInteger_helper("-1", "1", "-2");
        subtract_BigInteger_helper("-1", "-1", "0");
        subtract_BigInteger_helper("-1", "100", "-101");

        subtract_BigInteger_helper("1/2", "0", "1/2");
        subtract_BigInteger_helper("1/2", "1", "-1/2");
        subtract_BigInteger_helper("1/2", "-1", "3/2");
        subtract_BigInteger_helper("1/2", "100", "-199/2");

        subtract_BigInteger_helper("-4/3", "0", "-4/3");
        subtract_BigInteger_helper("-4/3", "1", "-7/3");
        subtract_BigInteger_helper("-4/3", "-1", "-1/3");
        subtract_BigInteger_helper("-4/3", "100", "-304/3");

        subtract_BigInteger_helper("sqrt(2)", "0", "sqrt(2)");
        subtract_BigInteger_helper("sqrt(2)", "1", "-1+sqrt(2)");
        subtract_BigInteger_helper("sqrt(2)", "-1", "1+sqrt(2)");
        subtract_BigInteger_helper("sqrt(2)", "100", "-100+sqrt(2)");

        subtract_BigInteger_helper("-sqrt(2)", "0", "-sqrt(2)");
        subtract_BigInteger_helper("-sqrt(2)", "1", "-1-sqrt(2)");
        subtract_BigInteger_helper("-sqrt(2)", "-1", "1-sqrt(2)");
        subtract_BigInteger_helper("-sqrt(2)", "100", "-100-sqrt(2)");

        subtract_BigInteger_helper("(1+sqrt(5))/2", "0", "(1+sqrt(5))/2");
        subtract_BigInteger_helper("(1+sqrt(5))/2", "1", "(-1+sqrt(5))/2");
        subtract_BigInteger_helper("(1+sqrt(5))/2", "-1", "(3+sqrt(5))/2");
        subtract_BigInteger_helper("(1+sqrt(5))/2", "100", "(-199+sqrt(5))/2");

        subtract_BigInteger_helper("root 0 of x^5-x-1", "0", "root 0 of x^5-x-1");
        subtract_BigInteger_helper("root 0 of x^5-x-1", "1", "root 0 of x^5+5*x^4+10*x^3+10*x^2+4*x-1");
        subtract_BigInteger_helper("root 0 of x^5-x-1", "-1", "root 0 of x^5-5*x^4+10*x^3-10*x^2+4*x-1");
        subtract_BigInteger_helper("root 0 of x^5-x-1", "100",
                "root 0 of x^5+500*x^4+100000*x^3+10000000*x^2+499999999*x+9999999899");
    }

    private static void subtract_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        Algebraic x = readStrict(a).get().subtract(Rational.readStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testSubtract_Rational() {
        subtract_Rational_helper("0", "0", "0");
        subtract_Rational_helper("0", "1", "-1");
        subtract_Rational_helper("0", "-1", "1");
        subtract_Rational_helper("0", "100/3", "-100/3");
        subtract_Rational_helper("0", "1/100", "-1/100");

        subtract_Rational_helper("1", "0", "1");
        subtract_Rational_helper("1", "1", "0");
        subtract_Rational_helper("1", "-1", "2");
        subtract_Rational_helper("1", "100/3", "-97/3");
        subtract_Rational_helper("1", "1/100", "99/100");

        subtract_Rational_helper("-1", "0", "-1");
        subtract_Rational_helper("-1", "1", "-2");
        subtract_Rational_helper("-1", "-1", "0");
        subtract_Rational_helper("-1", "100/3", "-103/3");
        subtract_Rational_helper("-1", "1/100", "-101/100");

        subtract_Rational_helper("1/2", "0", "1/2");
        subtract_Rational_helper("1/2", "1", "-1/2");
        subtract_Rational_helper("1/2", "-1", "3/2");
        subtract_Rational_helper("1/2", "100/3", "-197/6");
        subtract_Rational_helper("1/2", "1/100", "49/100");

        subtract_Rational_helper("-4/3", "0", "-4/3");
        subtract_Rational_helper("-4/3", "1", "-7/3");
        subtract_Rational_helper("-4/3", "-1", "-1/3");
        subtract_Rational_helper("-4/3", "100/3", "-104/3");
        subtract_Rational_helper("-4/3", "1/100", "-403/300");

        subtract_Rational_helper("sqrt(2)", "0", "sqrt(2)");
        subtract_Rational_helper("sqrt(2)", "1", "-1+sqrt(2)");
        subtract_Rational_helper("sqrt(2)", "-1", "1+sqrt(2)");
        subtract_Rational_helper("sqrt(2)", "100/3", "(-100+3*sqrt(2))/3");
        subtract_Rational_helper("sqrt(2)", "1/100", "(-1+100*sqrt(2))/100");

        subtract_Rational_helper("-sqrt(2)", "0", "-sqrt(2)");
        subtract_Rational_helper("-sqrt(2)", "1", "-1-sqrt(2)");
        subtract_Rational_helper("-sqrt(2)", "-1", "1-sqrt(2)");
        subtract_Rational_helper("-sqrt(2)", "100/3", "(-100-3*sqrt(2))/3");
        subtract_Rational_helper("-sqrt(2)", "1/100", "(-1-100*sqrt(2))/100");

        subtract_Rational_helper("(1+sqrt(5))/2", "0", "(1+sqrt(5))/2");
        subtract_Rational_helper("(1+sqrt(5))/2", "1", "(-1+sqrt(5))/2");
        subtract_Rational_helper("(1+sqrt(5))/2", "-1", "(3+sqrt(5))/2");
        subtract_Rational_helper("(1+sqrt(5))/2", "100/3", "(-197+3*sqrt(5))/6");
        subtract_Rational_helper("(1+sqrt(5))/2", "1/100", "(49+50*sqrt(5))/100");

        subtract_Rational_helper("root 0 of x^5-x-1", "0", "root 0 of x^5-x-1");
        subtract_Rational_helper("root 0 of x^5-x-1", "1", "root 0 of x^5+5*x^4+10*x^3+10*x^2+4*x-1");
        subtract_Rational_helper("root 0 of x^5-x-1", "-1", "root 0 of x^5-5*x^4+10*x^3-10*x^2+4*x-1");
        subtract_Rational_helper("root 0 of x^5-x-1", "100/3",
                "root 0 of 243*x^5+40500*x^4+2700000*x^3+90000000*x^2+1499999757*x+9999991657");
        subtract_Rational_helper("root 0 of x^5-x-1", "1/100",
                "root 0 of 10000000000*x^5+500000000*x^4+10000000*x^3+100000*x^2-9999999500*x-10099999999");
    }

    private static void subtract_Algebraic_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        Algebraic x = readStrict(a).get().subtract(readStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testSubtract_Algebraic() {
        subtract_Algebraic_helper("0", "0", "0");
        subtract_Algebraic_helper("0", "1", "-1");
        subtract_Algebraic_helper("0", "-1", "1");
        subtract_Algebraic_helper("0", "1/2", "-1/2");
        subtract_Algebraic_helper("0", "-4/3", "4/3");
        subtract_Algebraic_helper("0", "sqrt(2)", "-sqrt(2)");
        subtract_Algebraic_helper("0", "-sqrt(2)", "sqrt(2)");
        subtract_Algebraic_helper("0", "(1+sqrt(5))/2", "(-1-sqrt(5))/2");
        subtract_Algebraic_helper("0", "root 0 of x^5-x-1", "root 0 of x^5-x+1");

        subtract_Algebraic_helper("1", "0", "1");
        subtract_Algebraic_helper("1", "1", "0");
        subtract_Algebraic_helper("1", "-1", "2");
        subtract_Algebraic_helper("1", "1/2", "1/2");
        subtract_Algebraic_helper("1", "-4/3", "7/3");
        subtract_Algebraic_helper("1", "sqrt(2)", "1-sqrt(2)");
        subtract_Algebraic_helper("1", "-sqrt(2)", "1+sqrt(2)");
        subtract_Algebraic_helper("1", "(1+sqrt(5))/2", "(1-sqrt(5))/2");
        subtract_Algebraic_helper("1", "root 0 of x^5-x-1", "root 0 of x^5-5*x^4+10*x^3-10*x^2+4*x+1");

        subtract_Algebraic_helper("1/2", "0", "1/2");
        subtract_Algebraic_helper("1/2", "1", "-1/2");
        subtract_Algebraic_helper("1/2", "-1", "3/2");
        subtract_Algebraic_helper("1/2", "1/2", "0");
        subtract_Algebraic_helper("1/2", "-4/3", "11/6");
        subtract_Algebraic_helper("1/2", "sqrt(2)", "(1-2*sqrt(2))/2");
        subtract_Algebraic_helper("1/2", "-sqrt(2)", "(1+2*sqrt(2))/2");
        subtract_Algebraic_helper("1/2", "(1+sqrt(5))/2", "-sqrt(5)/2");
        subtract_Algebraic_helper("1/2", "root 0 of x^5-x-1", "root 0 of 32*x^5-80*x^4+80*x^3-40*x^2-22*x+47");

        subtract_Algebraic_helper("-4/3", "0", "-4/3");
        subtract_Algebraic_helper("-4/3", "1", "-7/3");
        subtract_Algebraic_helper("-4/3", "-1", "-1/3");
        subtract_Algebraic_helper("-4/3", "1/2", "-11/6");
        subtract_Algebraic_helper("-4/3", "-4/3", "0");
        subtract_Algebraic_helper("-4/3", "sqrt(2)", "(-4-3*sqrt(2))/3");
        subtract_Algebraic_helper("-4/3", "-sqrt(2)", "(-4+3*sqrt(2))/3");
        subtract_Algebraic_helper("-4/3", "(1+sqrt(5))/2", "(-11-3*sqrt(5))/6");
        subtract_Algebraic_helper("-4/3", "root 0 of x^5-x-1",
                "root 0 of 243*x^5+1620*x^4+4320*x^3+5760*x^2+3597*x+943");

        subtract_Algebraic_helper("sqrt(2)", "0", "sqrt(2)");
        subtract_Algebraic_helper("sqrt(2)", "1", "-1+sqrt(2)");
        subtract_Algebraic_helper("sqrt(2)", "-1", "1+sqrt(2)");
        subtract_Algebraic_helper("sqrt(2)", "1/2", "(-1+2*sqrt(2))/2");
        subtract_Algebraic_helper("sqrt(2)", "-4/3", "(4+3*sqrt(2))/3");
        subtract_Algebraic_helper("sqrt(2)", "sqrt(2)", "0");
        subtract_Algebraic_helper("sqrt(2)", "-sqrt(2)", "2*sqrt(2)");
        subtract_Algebraic_helper("sqrt(2)", "(1+sqrt(5))/2", "root 2 of x^4+2*x^3-5*x^2-6*x-1");
        subtract_Algebraic_helper("sqrt(2)", "root 0 of x^5-x-1",
                "root 1 of x^10-10*x^8+38*x^6+2*x^5-100*x^4+40*x^3+121*x^2+38*x-17");

        subtract_Algebraic_helper("-sqrt(2)", "0", "-sqrt(2)");
        subtract_Algebraic_helper("-sqrt(2)", "1", "-1-sqrt(2)");
        subtract_Algebraic_helper("-sqrt(2)", "-1", "1-sqrt(2)");
        subtract_Algebraic_helper("-sqrt(2)", "1/2", "(-1-2*sqrt(2))/2");
        subtract_Algebraic_helper("-sqrt(2)", "-4/3", "(4-3*sqrt(2))/3");
        subtract_Algebraic_helper("-sqrt(2)", "sqrt(2)", "-2*sqrt(2)");
        subtract_Algebraic_helper("-sqrt(2)", "-sqrt(2)", "0");
        subtract_Algebraic_helper("-sqrt(2)", "(1+sqrt(5))/2", "root 0 of x^4+2*x^3-5*x^2-6*x-1");
        subtract_Algebraic_helper("-sqrt(2)", "root 0 of x^5-x-1",
                "root 0 of x^10-10*x^8+38*x^6+2*x^5-100*x^4+40*x^3+121*x^2+38*x-17");

        subtract_Algebraic_helper("(1+sqrt(5))/2", "0", "(1+sqrt(5))/2");
        subtract_Algebraic_helper("(1+sqrt(5))/2", "1", "(-1+sqrt(5))/2");
        subtract_Algebraic_helper("(1+sqrt(5))/2", "-1", "(3+sqrt(5))/2");
        subtract_Algebraic_helper("(1+sqrt(5))/2", "1/2", "sqrt(5)/2");
        subtract_Algebraic_helper("(1+sqrt(5))/2", "-4/3", "(11+3*sqrt(5))/6");
        subtract_Algebraic_helper("(1+sqrt(5))/2", "sqrt(2)", "root 1 of x^4-2*x^3-5*x^2+6*x-1");
        subtract_Algebraic_helper("(1+sqrt(5))/2", "-sqrt(2)", "root 3 of x^4-2*x^3-5*x^2+6*x-1");
        subtract_Algebraic_helper("(1+sqrt(5))/2", "(1+sqrt(5))/2", "0");
        subtract_Algebraic_helper("(1+sqrt(5))/2", "root 0 of x^5-x-1",
                "root 1 of x^10-5*x^9+5*x^8+10*x^7-17*x^6-3*x^5-10*x^4+70*x^3-49*x^2+18*x-4");

        subtract_Algebraic_helper("root 0 of x^5-x-1", "0", "root 0 of x^5-x-1");
        subtract_Algebraic_helper("root 0 of x^5-x-1", "1", "root 0 of x^5+5*x^4+10*x^3+10*x^2+4*x-1");
        subtract_Algebraic_helper("root 0 of x^5-x-1", "-1", "root 0 of x^5-5*x^4+10*x^3-10*x^2+4*x-1");
        subtract_Algebraic_helper("root 0 of x^5-x-1", "1/2", "root 0 of 32*x^5+80*x^4+80*x^3+40*x^2-22*x-47");
        subtract_Algebraic_helper("root 0 of x^5-x-1", "-4/3",
                "root 0 of 243*x^5-1620*x^4+4320*x^3-5760*x^2+3597*x-943");
        subtract_Algebraic_helper("root 0 of x^5-x-1", "sqrt(2)",
                "root 0 of x^10-10*x^8+38*x^6-2*x^5-100*x^4-40*x^3+121*x^2-38*x-17");
        subtract_Algebraic_helper("root 0 of x^5-x-1", "-sqrt(2)",
                "root 1 of x^10-10*x^8+38*x^6-2*x^5-100*x^4-40*x^3+121*x^2-38*x-17");
        subtract_Algebraic_helper("root 0 of x^5-x-1", "(1+sqrt(5))/2",
                "root 0 of x^10+5*x^9+5*x^8-10*x^7-17*x^6+3*x^5-10*x^4-70*x^3-49*x^2-18*x-4");
        subtract_Algebraic_helper("root 0 of x^5-x-1", "root 0 of x^5-x-1", "0");

        subtract_Algebraic_helper("sqrt(2)", "sqrt(3)", "root 1 of x^4-10*x^2+1");
        subtract_Algebraic_helper("1+sqrt(2)", "sqrt(2)", "1");
    }

    private static void multiply_int_helper(@NotNull String a, int b, @NotNull String output) {
        Algebraic x = readStrict(a).get().multiply(b);
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testMultiply_int() {
        multiply_int_helper("0", 0, "0");
        multiply_int_helper("0", 1, "0");
        multiply_int_helper("0", -1, "0");
        multiply_int_helper("0", 100, "0");

        multiply_int_helper("1", 0, "0");
        multiply_int_helper("1", 1, "1");
        multiply_int_helper("1", -1, "-1");
        multiply_int_helper("1", 100, "100");

        multiply_int_helper("-1", 0, "0");
        multiply_int_helper("-1", 1, "-1");
        multiply_int_helper("-1", -1, "1");
        multiply_int_helper("-1", 100, "-100");

        multiply_int_helper("1/2", 0, "0");
        multiply_int_helper("1/2", 1, "1/2");
        multiply_int_helper("1/2", -1, "-1/2");
        multiply_int_helper("1/2", 100, "50");

        multiply_int_helper("-4/3", 0, "0");
        multiply_int_helper("-4/3", 1, "-4/3");
        multiply_int_helper("-4/3", -1, "4/3");
        multiply_int_helper("-4/3", 100, "-400/3");

        multiply_int_helper("sqrt(2)", 0, "0");
        multiply_int_helper("sqrt(2)", 1, "sqrt(2)");
        multiply_int_helper("sqrt(2)", -1, "-sqrt(2)");
        multiply_int_helper("sqrt(2)", 100, "100*sqrt(2)");

        multiply_int_helper("-sqrt(2)", 0, "0");
        multiply_int_helper("-sqrt(2)", 1, "-sqrt(2)");
        multiply_int_helper("-sqrt(2)", -1, "sqrt(2)");
        multiply_int_helper("-sqrt(2)", 100, "-100*sqrt(2)");

        multiply_int_helper("(1+sqrt(5))/2", 0, "0");
        multiply_int_helper("(1+sqrt(5))/2", 1, "(1+sqrt(5))/2");
        multiply_int_helper("(1+sqrt(5))/2", -1, "(-1-sqrt(5))/2");
        multiply_int_helper("(1+sqrt(5))/2", 100, "50+50*sqrt(5)");

        multiply_int_helper("root 0 of x^5-x-1", 0, "0");
        multiply_int_helper("root 0 of x^5-x-1", 1, "root 0 of x^5-x-1");
        multiply_int_helper("root 0 of x^5-x-1", -1, "root 0 of x^5-x+1");
        multiply_int_helper("root 0 of x^5-x-1", 100, "root 0 of x^5-100000000*x-10000000000");
    }

    private static void multiply_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        Algebraic x = readStrict(a).get().multiply(Readers.readBigIntegerStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testMultiply_BigInteger() {
        multiply_BigInteger_helper("0", "0", "0");
        multiply_BigInteger_helper("0", "1", "0");
        multiply_BigInteger_helper("0", "-1", "0");
        multiply_BigInteger_helper("0", "100", "0");

        multiply_BigInteger_helper("1", "0", "0");
        multiply_BigInteger_helper("1", "1", "1");
        multiply_BigInteger_helper("1", "-1", "-1");
        multiply_BigInteger_helper("1", "100", "100");

        multiply_BigInteger_helper("-1", "0", "0");
        multiply_BigInteger_helper("-1", "1", "-1");
        multiply_BigInteger_helper("-1", "-1", "1");
        multiply_BigInteger_helper("-1", "100", "-100");

        multiply_BigInteger_helper("1/2", "0", "0");
        multiply_BigInteger_helper("1/2", "1", "1/2");
        multiply_BigInteger_helper("1/2", "-1", "-1/2");
        multiply_BigInteger_helper("1/2", "100", "50");

        multiply_BigInteger_helper("-4/3", "0", "0");
        multiply_BigInteger_helper("-4/3", "1", "-4/3");
        multiply_BigInteger_helper("-4/3", "-1", "4/3");
        multiply_BigInteger_helper("-4/3", "100", "-400/3");

        multiply_BigInteger_helper("sqrt(2)", "0", "0");
        multiply_BigInteger_helper("sqrt(2)", "1", "sqrt(2)");
        multiply_BigInteger_helper("sqrt(2)", "-1", "-sqrt(2)");
        multiply_BigInteger_helper("sqrt(2)", "100", "100*sqrt(2)");

        multiply_BigInteger_helper("-sqrt(2)", "0", "0");
        multiply_BigInteger_helper("-sqrt(2)", "1", "-sqrt(2)");
        multiply_BigInteger_helper("-sqrt(2)", "-1", "sqrt(2)");
        multiply_BigInteger_helper("-sqrt(2)", "100", "-100*sqrt(2)");

        multiply_BigInteger_helper("(1+sqrt(5))/2", "0", "0");
        multiply_BigInteger_helper("(1+sqrt(5))/2", "1", "(1+sqrt(5))/2");
        multiply_BigInteger_helper("(1+sqrt(5))/2", "-1", "(-1-sqrt(5))/2");
        multiply_BigInteger_helper("(1+sqrt(5))/2", "100", "50+50*sqrt(5)");

        multiply_BigInteger_helper("root 0 of x^5-x-1", "0", "0");
        multiply_BigInteger_helper("root 0 of x^5-x-1", "1", "root 0 of x^5-x-1");
        multiply_BigInteger_helper("root 0 of x^5-x-1", "-1", "root 0 of x^5-x+1");
        multiply_BigInteger_helper("root 0 of x^5-x-1", "100", "root 0 of x^5-100000000*x-10000000000");
    }

    private static void multiply_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        Algebraic x = readStrict(a).get().multiply(Rational.readStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testMultiply_Rational() {
        multiply_Rational_helper("0", "0", "0");
        multiply_Rational_helper("0", "1", "0");
        multiply_Rational_helper("0", "-1", "0");
        multiply_Rational_helper("0", "100/3", "0");
        multiply_Rational_helper("0", "1/100", "0");

        multiply_Rational_helper("1", "0", "0");
        multiply_Rational_helper("1", "1", "1");
        multiply_Rational_helper("1", "-1", "-1");
        multiply_Rational_helper("1", "100/3", "100/3");
        multiply_Rational_helper("1", "1/100", "1/100");

        multiply_Rational_helper("-1", "0", "0");
        multiply_Rational_helper("-1", "1", "-1");
        multiply_Rational_helper("-1", "-1", "1");
        multiply_Rational_helper("-1", "100/3", "-100/3");
        multiply_Rational_helper("-1", "1/100", "-1/100");

        multiply_Rational_helper("1/2", "0", "0");
        multiply_Rational_helper("1/2", "1", "1/2");
        multiply_Rational_helper("1/2", "-1", "-1/2");
        multiply_Rational_helper("1/2", "100/3", "50/3");
        multiply_Rational_helper("1/2", "1/100", "1/200");

        multiply_Rational_helper("-4/3", "0", "0");
        multiply_Rational_helper("-4/3", "1", "-4/3");
        multiply_Rational_helper("-4/3", "-1", "4/3");
        multiply_Rational_helper("-4/3", "100/3", "-400/9");
        multiply_Rational_helper("-4/3", "1/100", "-1/75");

        multiply_Rational_helper("sqrt(2)", "0", "0");
        multiply_Rational_helper("sqrt(2)", "1", "sqrt(2)");
        multiply_Rational_helper("sqrt(2)", "-1", "-sqrt(2)");
        multiply_Rational_helper("sqrt(2)", "100/3", "100*sqrt(2)/3");
        multiply_Rational_helper("sqrt(2)", "1/100", "sqrt(2)/100");

        multiply_Rational_helper("-sqrt(2)", "0", "0");
        multiply_Rational_helper("-sqrt(2)", "1", "-sqrt(2)");
        multiply_Rational_helper("-sqrt(2)", "-1", "sqrt(2)");
        multiply_Rational_helper("-sqrt(2)", "100/3", "-100*sqrt(2)/3");
        multiply_Rational_helper("-sqrt(2)", "1/100", "-sqrt(2)/100");

        multiply_Rational_helper("(1+sqrt(5))/2", "0", "0");
        multiply_Rational_helper("(1+sqrt(5))/2", "1", "(1+sqrt(5))/2");
        multiply_Rational_helper("(1+sqrt(5))/2", "-1", "(-1-sqrt(5))/2");
        multiply_Rational_helper("(1+sqrt(5))/2", "100/3", "(50+50*sqrt(5))/3");
        multiply_Rational_helper("(1+sqrt(5))/2", "1/100", "(1+sqrt(5))/200");

        multiply_Rational_helper("root 0 of x^5-x-1", "0", "0");
        multiply_Rational_helper("root 0 of x^5-x-1", "1", "root 0 of x^5-x-1");
        multiply_Rational_helper("root 0 of x^5-x-1", "-1", "root 0 of x^5-x+1");
        multiply_Rational_helper("root 0 of x^5-x-1", "100/3", "root 0 of 243*x^5-300000000*x-10000000000");
        multiply_Rational_helper("root 0 of x^5-x-1", "1/100", "root 0 of 10000000000*x^5-100*x-1");
    }

    private static void multiply_Algebraic_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        Algebraic x = readStrict(a).get().multiply(readStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testMultiply_Algebraic() {
        multiply_Algebraic_helper("0", "0", "0");
        multiply_Algebraic_helper("0", "1", "0");
        multiply_Algebraic_helper("0", "-1", "0");
        multiply_Algebraic_helper("0", "1/2", "0");
        multiply_Algebraic_helper("0", "-4/3", "0");
        multiply_Algebraic_helper("0", "sqrt(2)", "0");
        multiply_Algebraic_helper("0", "-sqrt(2)", "0");
        multiply_Algebraic_helper("0", "(1+sqrt(5))/2", "0");
        multiply_Algebraic_helper("0", "root 0 of x^5-x-1", "0");

        multiply_Algebraic_helper("1", "0", "0");
        multiply_Algebraic_helper("1", "1", "1");
        multiply_Algebraic_helper("1", "-1", "-1");
        multiply_Algebraic_helper("1", "1/2", "1/2");
        multiply_Algebraic_helper("1", "-4/3", "-4/3");
        multiply_Algebraic_helper("1", "sqrt(2)", "sqrt(2)");
        multiply_Algebraic_helper("1", "-sqrt(2)", "-sqrt(2)");
        multiply_Algebraic_helper("1", "(1+sqrt(5))/2", "(1+sqrt(5))/2");
        multiply_Algebraic_helper("1", "root 0 of x^5-x-1", "root 0 of x^5-x-1");

        multiply_Algebraic_helper("1/2", "0", "0");
        multiply_Algebraic_helper("1/2", "1", "1/2");
        multiply_Algebraic_helper("1/2", "-1", "-1/2");
        multiply_Algebraic_helper("1/2", "1/2", "1/4");
        multiply_Algebraic_helper("1/2", "-4/3", "-2/3");
        multiply_Algebraic_helper("1/2", "sqrt(2)", "sqrt(2)/2");
        multiply_Algebraic_helper("1/2", "-sqrt(2)", "-sqrt(2)/2");
        multiply_Algebraic_helper("1/2", "(1+sqrt(5))/2", "(1+sqrt(5))/4");
        multiply_Algebraic_helper("1/2", "root 0 of x^5-x-1", "root 0 of 32*x^5-2*x-1");

        multiply_Algebraic_helper("-4/3", "0", "0");
        multiply_Algebraic_helper("-4/3", "1", "-4/3");
        multiply_Algebraic_helper("-4/3", "-1", "4/3");
        multiply_Algebraic_helper("-4/3", "1/2", "-2/3");
        multiply_Algebraic_helper("-4/3", "-4/3", "16/9");
        multiply_Algebraic_helper("-4/3", "sqrt(2)", "-4*sqrt(2)/3");
        multiply_Algebraic_helper("-4/3", "-sqrt(2)", "4*sqrt(2)/3");
        multiply_Algebraic_helper("-4/3", "(1+sqrt(5))/2", "(-2-2*sqrt(5))/3");
        multiply_Algebraic_helper("-4/3", "root 0 of x^5-x-1", "root 0 of 243*x^5-768*x+1024");

        multiply_Algebraic_helper("sqrt(2)", "0", "0");
        multiply_Algebraic_helper("sqrt(2)", "1", "sqrt(2)");
        multiply_Algebraic_helper("sqrt(2)", "-1", "-sqrt(2)");
        multiply_Algebraic_helper("sqrt(2)", "1/2", "sqrt(2)/2");
        multiply_Algebraic_helper("sqrt(2)", "-4/3", "-4*sqrt(2)/3");
        multiply_Algebraic_helper("sqrt(2)", "sqrt(2)", "2");
        multiply_Algebraic_helper("sqrt(2)", "-sqrt(2)", "-2");
        multiply_Algebraic_helper("sqrt(2)", "(1+sqrt(5))/2", "root 3 of x^4-6*x^2+4");
        multiply_Algebraic_helper("sqrt(2)", "root 0 of x^5-x-1", "root 1 of x^10-8*x^6+16*x^2-32");

        multiply_Algebraic_helper("-sqrt(2)", "0", "0");
        multiply_Algebraic_helper("-sqrt(2)", "1", "-sqrt(2)");
        multiply_Algebraic_helper("-sqrt(2)", "-1", "sqrt(2)");
        multiply_Algebraic_helper("-sqrt(2)", "1/2", "-sqrt(2)/2");
        multiply_Algebraic_helper("-sqrt(2)", "-4/3", "4*sqrt(2)/3");
        multiply_Algebraic_helper("-sqrt(2)", "sqrt(2)", "-2");
        multiply_Algebraic_helper("-sqrt(2)", "-sqrt(2)", "2");
        multiply_Algebraic_helper("-sqrt(2)", "(1+sqrt(5))/2", "root 0 of x^4-6*x^2+4");
        multiply_Algebraic_helper("-sqrt(2)", "root 0 of x^5-x-1", "root 0 of x^10-8*x^6+16*x^2-32");

        multiply_Algebraic_helper("(1+sqrt(5))/2", "0", "0");
        multiply_Algebraic_helper("(1+sqrt(5))/2", "1", "(1+sqrt(5))/2");
        multiply_Algebraic_helper("(1+sqrt(5))/2", "-1", "(-1-sqrt(5))/2");
        multiply_Algebraic_helper("(1+sqrt(5))/2", "1/2", "(1+sqrt(5))/4");
        multiply_Algebraic_helper("(1+sqrt(5))/2", "-4/3", "(-2-2*sqrt(5))/3");
        multiply_Algebraic_helper("(1+sqrt(5))/2", "sqrt(2)", "root 3 of x^4-6*x^2+4");
        multiply_Algebraic_helper("(1+sqrt(5))/2", "-sqrt(2)", "root 0 of x^4-6*x^2+4");
        multiply_Algebraic_helper("(1+sqrt(5))/2", "(1+sqrt(5))/2", "(3+sqrt(5))/2");
        multiply_Algebraic_helper("(1+sqrt(5))/2", "root 0 of x^5-x-1", "root 1 of x^10-7*x^6-11*x^5+x^2+x-1");

        multiply_Algebraic_helper("root 0 of x^5-x-1", "0", "0");
        multiply_Algebraic_helper("root 0 of x^5-x-1", "1", "root 0 of x^5-x-1");
        multiply_Algebraic_helper("root 0 of x^5-x-1", "-1", "root 0 of x^5-x+1");
        multiply_Algebraic_helper("root 0 of x^5-x-1", "1/2", "root 0 of 32*x^5-2*x-1");
        multiply_Algebraic_helper("root 0 of x^5-x-1", "-4/3", "root 0 of 243*x^5-768*x+1024");
        multiply_Algebraic_helper("root 0 of x^5-x-1", "sqrt(2)", "root 1 of x^10-8*x^6+16*x^2-32");
        multiply_Algebraic_helper("root 0 of x^5-x-1", "-sqrt(2)", "root 0 of x^10-8*x^6+16*x^2-32");
        multiply_Algebraic_helper("root 0 of x^5-x-1", "(1+sqrt(5))/2", "root 1 of x^10-7*x^6-11*x^5+x^2+x-1");
        multiply_Algebraic_helper("root 0 of x^5-x-1", "root 0 of x^5-x-1", "root 0 of x^5-2*x^3+x-1");

        multiply_Algebraic_helper("sqrt(2)", "sqrt(3)", "sqrt(6)");
        multiply_Algebraic_helper("sqrt(2)", "sqrt(2)/2", "1");
    }

    private static void invert_helper(@NotNull String input, @NotNull String output) {
        Algebraic x = readStrict(input).get().invert();
        x.validate();
        aeq(x, output);
    }

    private static void invert_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().invert();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testInvert() {
        invert_helper("1", "1");
        invert_helper("1/2", "2");
        invert_helper("-4/3", "-3/4");
        invert_helper("sqrt(2)", "sqrt(2)/2");
        invert_helper("-sqrt(2)", "-sqrt(2)/2");
        invert_helper("(1+sqrt(5))/2", "(-1+sqrt(5))/2");
        invert_helper("root 0 of x^5-x-1", "root 0 of x^5+x^4-1");

        invert_fail_helper("0");
    }

    private static void divide_int_helper(@NotNull String a, int b, @NotNull String output) {
        Algebraic x = readStrict(a).get().divide(b);
        x.validate();
        aeq(x, output);
    }

    private static void divide_int_fail_helper(@NotNull String a, int b) {
        try {
            readStrict(a).get().divide(b);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_int() {
        divide_int_helper("0", 1, "0");
        divide_int_helper("0", -1, "0");
        divide_int_helper("0", 100, "0");

        divide_int_helper("1", 1, "1");
        divide_int_helper("1", -1, "-1");
        divide_int_helper("1", 100, "1/100");

        divide_int_helper("-1", 1, "-1");
        divide_int_helper("-1", -1, "1");
        divide_int_helper("-1", 100, "-1/100");

        divide_int_helper("1/2", 1, "1/2");
        divide_int_helper("1/2", -1, "-1/2");
        divide_int_helper("1/2", 100, "1/200");

        divide_int_helper("-4/3", 1, "-4/3");
        divide_int_helper("-4/3", -1, "4/3");
        divide_int_helper("-4/3", 100, "-1/75");

        divide_int_helper("sqrt(2)", 1, "sqrt(2)");
        divide_int_helper("sqrt(2)", -1, "-sqrt(2)");
        divide_int_helper("sqrt(2)", 100, "sqrt(2)/100");

        divide_int_helper("-sqrt(2)", 1, "-sqrt(2)");
        divide_int_helper("-sqrt(2)", -1, "sqrt(2)");
        divide_int_helper("-sqrt(2)", 100, "-sqrt(2)/100");

        divide_int_helper("(1+sqrt(5))/2", 1, "(1+sqrt(5))/2");
        divide_int_helper("(1+sqrt(5))/2", -1, "(-1-sqrt(5))/2");
        divide_int_helper("(1+sqrt(5))/2", 100, "(1+sqrt(5))/200");

        divide_int_helper("root 0 of x^5-x-1", 1, "root 0 of x^5-x-1");
        divide_int_helper("root 0 of x^5-x-1", -1, "root 0 of x^5-x+1");
        divide_int_helper("root 0 of x^5-x-1", 100, "root 0 of 10000000000*x^5-100*x-1");

        divide_int_fail_helper("0", 0);
        divide_int_fail_helper("1", 0);
        divide_int_fail_helper("-4/3", 0);
        divide_int_fail_helper("sqrt(2)", 0);
    }

    private static void divide_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        Algebraic x = readStrict(a).get().divide(Readers.readBigIntegerStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    private static void divide_BigInteger_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().divide(Readers.readBigIntegerStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_BigInteger() {
        divide_BigInteger_helper("0", "1", "0");
        divide_BigInteger_helper("0", "-1", "0");
        divide_BigInteger_helper("0", "100", "0");

        divide_BigInteger_helper("1", "1", "1");
        divide_BigInteger_helper("1", "-1", "-1");
        divide_BigInteger_helper("1", "100", "1/100");

        divide_BigInteger_helper("-1", "1", "-1");
        divide_BigInteger_helper("-1", "-1", "1");
        divide_BigInteger_helper("-1", "100", "-1/100");

        divide_BigInteger_helper("1/2", "1", "1/2");
        divide_BigInteger_helper("1/2", "-1", "-1/2");
        divide_BigInteger_helper("1/2", "100", "1/200");

        divide_BigInteger_helper("-4/3", "1", "-4/3");
        divide_BigInteger_helper("-4/3", "-1", "4/3");
        divide_BigInteger_helper("-4/3", "100", "-1/75");

        divide_BigInteger_helper("sqrt(2)", "1", "sqrt(2)");
        divide_BigInteger_helper("sqrt(2)", "-1", "-sqrt(2)");
        divide_BigInteger_helper("sqrt(2)", "100", "sqrt(2)/100");

        divide_BigInteger_helper("-sqrt(2)", "1", "-sqrt(2)");
        divide_BigInteger_helper("-sqrt(2)", "-1", "sqrt(2)");
        divide_BigInteger_helper("-sqrt(2)", "100", "-sqrt(2)/100");

        divide_BigInteger_helper("(1+sqrt(5))/2", "1", "(1+sqrt(5))/2");
        divide_BigInteger_helper("(1+sqrt(5))/2", "-1", "(-1-sqrt(5))/2");
        divide_BigInteger_helper("(1+sqrt(5))/2", "100", "(1+sqrt(5))/200");

        divide_BigInteger_helper("root 0 of x^5-x-1", "1", "root 0 of x^5-x-1");
        divide_BigInteger_helper("root 0 of x^5-x-1", "-1", "root 0 of x^5-x+1");
        divide_BigInteger_helper("root 0 of x^5-x-1", "100", "root 0 of 10000000000*x^5-100*x-1");

        divide_BigInteger_fail_helper("0", "0");
        divide_BigInteger_fail_helper("1", "0");
        divide_BigInteger_fail_helper("-4/3", "0");
        divide_BigInteger_fail_helper("sqrt(2)", "0");
    }

    private static void divide_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        Algebraic x = readStrict(a).get().divide(Rational.readStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    private static void divide_Rational_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().divide(Rational.readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_Rational() {
        divide_Rational_helper("0", "1", "0");
        divide_Rational_helper("0", "-1", "0");
        divide_Rational_helper("0", "100/3", "0");
        divide_Rational_helper("0", "1/100", "0");

        divide_Rational_helper("1", "1", "1");
        divide_Rational_helper("1", "-1", "-1");
        divide_Rational_helper("1", "100/3", "3/100");
        divide_Rational_helper("1", "1/100", "100");

        divide_Rational_helper("-1", "1", "-1");
        divide_Rational_helper("-1", "-1", "1");
        divide_Rational_helper("-1", "100/3", "-3/100");
        divide_Rational_helper("-1", "1/100", "-100");

        divide_Rational_helper("1/2", "1", "1/2");
        divide_Rational_helper("1/2", "-1", "-1/2");
        divide_Rational_helper("1/2", "100/3", "3/200");
        divide_Rational_helper("1/2", "1/100", "50");

        divide_Rational_helper("-4/3", "1", "-4/3");
        divide_Rational_helper("-4/3", "-1", "4/3");
        divide_Rational_helper("-4/3", "100/3", "-1/25");
        divide_Rational_helper("-4/3", "1/100", "-400/3");

        divide_Rational_helper("sqrt(2)", "1", "sqrt(2)");
        divide_Rational_helper("sqrt(2)", "-1", "-sqrt(2)");
        divide_Rational_helper("sqrt(2)", "100/3", "3*sqrt(2)/100");
        divide_Rational_helper("sqrt(2)", "1/100", "100*sqrt(2)");

        divide_Rational_helper("-sqrt(2)", "1", "-sqrt(2)");
        divide_Rational_helper("-sqrt(2)", "-1", "sqrt(2)");
        divide_Rational_helper("-sqrt(2)", "100/3", "-3*sqrt(2)/100");
        divide_Rational_helper("-sqrt(2)", "1/100", "-100*sqrt(2)");

        divide_Rational_helper("(1+sqrt(5))/2", "1", "(1+sqrt(5))/2");
        divide_Rational_helper("(1+sqrt(5))/2", "-1", "(-1-sqrt(5))/2");
        divide_Rational_helper("(1+sqrt(5))/2", "100/3", "(3+3*sqrt(5))/200");
        divide_Rational_helper("(1+sqrt(5))/2", "1/100", "50+50*sqrt(5)");

        divide_Rational_helper("root 0 of x^5-x-1", "1", "root 0 of x^5-x-1");
        divide_Rational_helper("root 0 of x^5-x-1", "-1", "root 0 of x^5-x+1");
        divide_Rational_helper("root 0 of x^5-x-1", "100/3", "root 0 of 10000000000*x^5-8100*x-243");
        divide_Rational_helper("root 0 of x^5-x-1", "1/100", "root 0 of x^5-100000000*x-10000000000");

        divide_Rational_fail_helper("0", "0");
        divide_Rational_fail_helper("1", "0");
        divide_Rational_fail_helper("-4/3", "0");
        divide_Rational_fail_helper("sqrt(2)", "0");
    }

    private static void divide_Algebraic_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        Algebraic x = readStrict(a).get().divide(readStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    private static void divide_Algebraic_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().divide(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_Algebraic() {
        divide_Algebraic_helper("0", "1", "0");
        divide_Algebraic_helper("0", "-1", "0");
        divide_Algebraic_helper("0", "1/2", "0");
        divide_Algebraic_helper("0", "-4/3", "0");
        divide_Algebraic_helper("0", "sqrt(2)", "0");
        divide_Algebraic_helper("0", "-sqrt(2)", "0");
        divide_Algebraic_helper("0", "(1+sqrt(5))/2", "0");
        divide_Algebraic_helper("0", "root 0 of x^5-x-1", "0");

        divide_Algebraic_helper("1", "1", "1");
        divide_Algebraic_helper("1", "-1", "-1");
        divide_Algebraic_helper("1", "1/2", "2");
        divide_Algebraic_helper("1", "-4/3", "-3/4");
        divide_Algebraic_helper("1", "sqrt(2)", "sqrt(2)/2");
        divide_Algebraic_helper("1", "-sqrt(2)", "-sqrt(2)/2");
        divide_Algebraic_helper("1", "(1+sqrt(5))/2", "(-1+sqrt(5))/2");
        divide_Algebraic_helper("1", "root 0 of x^5-x-1", "root 0 of x^5+x^4-1");

        divide_Algebraic_helper("1/2", "1", "1/2");
        divide_Algebraic_helper("1/2", "-1", "-1/2");
        divide_Algebraic_helper("1/2", "1/2", "1");
        divide_Algebraic_helper("1/2", "-4/3", "-3/8");
        divide_Algebraic_helper("1/2", "sqrt(2)", "sqrt(2)/4");
        divide_Algebraic_helper("1/2", "-sqrt(2)", "-sqrt(2)/4");
        divide_Algebraic_helper("1/2", "(1+sqrt(5))/2", "(-1+sqrt(5))/4");
        divide_Algebraic_helper("1/2", "root 0 of x^5-x-1", "root 0 of 32*x^5+16*x^4-1");

        divide_Algebraic_helper("-4/3", "1", "-4/3");
        divide_Algebraic_helper("-4/3", "-1", "4/3");
        divide_Algebraic_helper("-4/3", "1/2", "-8/3");
        divide_Algebraic_helper("-4/3", "-4/3", "1");
        divide_Algebraic_helper("-4/3", "sqrt(2)", "-2*sqrt(2)/3");
        divide_Algebraic_helper("-4/3", "-sqrt(2)", "2*sqrt(2)/3");
        divide_Algebraic_helper("-4/3", "(1+sqrt(5))/2", "(2-2*sqrt(5))/3");
        divide_Algebraic_helper("-4/3", "root 0 of x^5-x-1", "root 0 of 243*x^5-324*x^4+1024");

        divide_Algebraic_helper("sqrt(2)", "1", "sqrt(2)");
        divide_Algebraic_helper("sqrt(2)", "-1", "-sqrt(2)");
        divide_Algebraic_helper("sqrt(2)", "1/2", "2*sqrt(2)");
        divide_Algebraic_helper("sqrt(2)", "-4/3", "-3*sqrt(2)/4");
        divide_Algebraic_helper("sqrt(2)", "sqrt(2)", "1");
        divide_Algebraic_helper("sqrt(2)", "-sqrt(2)", "-1");
        divide_Algebraic_helper("sqrt(2)", "(1+sqrt(5))/2", "root 2 of x^4-6*x^2+4");
        divide_Algebraic_helper("sqrt(2)", "root 0 of x^5-x-1", "root 1 of x^10-2*x^8+16*x^4-32");

        divide_Algebraic_helper("-sqrt(2)", "1", "-sqrt(2)");
        divide_Algebraic_helper("-sqrt(2)", "-1", "sqrt(2)");
        divide_Algebraic_helper("-sqrt(2)", "1/2", "-2*sqrt(2)");
        divide_Algebraic_helper("-sqrt(2)", "-4/3", "3*sqrt(2)/4");
        divide_Algebraic_helper("-sqrt(2)", "sqrt(2)", "-1");
        divide_Algebraic_helper("-sqrt(2)", "-sqrt(2)", "1");
        divide_Algebraic_helper("-sqrt(2)", "(1+sqrt(5))/2", "root 1 of x^4-6*x^2+4");
        divide_Algebraic_helper("-sqrt(2)", "root 0 of x^5-x-1", "root 0 of x^10-2*x^8+16*x^4-32");

        divide_Algebraic_helper("(1+sqrt(5))/2", "1", "(1+sqrt(5))/2");
        divide_Algebraic_helper("(1+sqrt(5))/2", "-1", "(-1-sqrt(5))/2");
        divide_Algebraic_helper("(1+sqrt(5))/2", "1/2", "1+sqrt(5)");
        divide_Algebraic_helper("(1+sqrt(5))/2", "-4/3", "(-3-3*sqrt(5))/8");
        divide_Algebraic_helper("(1+sqrt(5))/2", "sqrt(2)", "root 3 of 4*x^4-6*x^2+1");
        divide_Algebraic_helper("(1+sqrt(5))/2", "-sqrt(2)", "root 0 of 4*x^4-6*x^2+1");
        divide_Algebraic_helper("(1+sqrt(5))/2", "(1+sqrt(5))/2", "1");
        divide_Algebraic_helper("(1+sqrt(5))/2", "root 0 of x^5-x-1", "root 1 of x^10+x^9-x^8-11*x^5+7*x^4-1");

        divide_Algebraic_helper("root 0 of x^5-x-1", "1", "root 0 of x^5-x-1");
        divide_Algebraic_helper("root 0 of x^5-x-1", "-1", "root 0 of x^5-x+1");
        divide_Algebraic_helper("root 0 of x^5-x-1", "1/2", "root 0 of x^5-16*x-32");
        divide_Algebraic_helper("root 0 of x^5-x-1", "-4/3", "root 0 of 1024*x^5-324*x+243");
        divide_Algebraic_helper("root 0 of x^5-x-1", "sqrt(2)", "root 1 of 32*x^10-16*x^6+2*x^2-1");
        divide_Algebraic_helper("root 0 of x^5-x-1", "-sqrt(2)", "root 0 of 32*x^10-16*x^6+2*x^2-1");
        divide_Algebraic_helper("root 0 of x^5-x-1", "(1+sqrt(5))/2", "root 1 of x^10-7*x^6+11*x^5+x^2-x-1");
        divide_Algebraic_helper("root 0 of x^5-x-1", "root 0 of x^5-x-1", "1");

        divide_Algebraic_helper("sqrt(2)", "sqrt(3)", "sqrt(6)/3");
        divide_Algebraic_helper("sqrt(6)", "sqrt(3)", "sqrt(2)");

        divide_Algebraic_fail_helper("0", "0");
        divide_Algebraic_fail_helper("1", "0");
        divide_Algebraic_fail_helper("-4/3", "0");
        divide_Algebraic_fail_helper("sqrt(2)", "0");
    }

    private static void shiftLeft_helper(@NotNull String r, int bits, @NotNull String output) {
        Algebraic x = readStrict(r).get().shiftLeft(bits);
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testShiftLeft() {
        shiftLeft_helper("0", 0, "0");
        shiftLeft_helper("0", 1, "0");
        shiftLeft_helper("0", 2, "0");
        shiftLeft_helper("0", 3, "0");
        shiftLeft_helper("0", 4, "0");
        shiftLeft_helper("0", -1, "0");
        shiftLeft_helper("0", -2, "0");
        shiftLeft_helper("0", -3, "0");
        shiftLeft_helper("0", -4, "0");

        shiftLeft_helper("1", 0, "1");
        shiftLeft_helper("1", 1, "2");
        shiftLeft_helper("1", 2, "4");
        shiftLeft_helper("1", 3, "8");
        shiftLeft_helper("1", 4, "16");
        shiftLeft_helper("1", -1, "1/2");
        shiftLeft_helper("1", -2, "1/4");
        shiftLeft_helper("1", -3, "1/8");
        shiftLeft_helper("1", -4, "1/16");

        shiftLeft_helper("1/2", 0, "1/2");
        shiftLeft_helper("1/2", 1, "1");
        shiftLeft_helper("1/2", 2, "2");
        shiftLeft_helper("1/2", 3, "4");
        shiftLeft_helper("1/2", 4, "8");
        shiftLeft_helper("1/2", -1, "1/4");
        shiftLeft_helper("1/2", -2, "1/8");
        shiftLeft_helper("1/2", -3, "1/16");
        shiftLeft_helper("1/2", -4, "1/32");

        shiftLeft_helper("-4/3", 0, "-4/3");
        shiftLeft_helper("-4/3", 1, "-8/3");
        shiftLeft_helper("-4/3", 2, "-16/3");
        shiftLeft_helper("-4/3", 3, "-32/3");
        shiftLeft_helper("-4/3", 4, "-64/3");
        shiftLeft_helper("-4/3", -1, "-2/3");
        shiftLeft_helper("-4/3", -2, "-1/3");
        shiftLeft_helper("-4/3", -3, "-1/6");
        shiftLeft_helper("-4/3", -4, "-1/12");

        shiftLeft_helper("sqrt(2)", 0, "sqrt(2)");
        shiftLeft_helper("sqrt(2)", 1, "2*sqrt(2)");
        shiftLeft_helper("sqrt(2)", 2, "4*sqrt(2)");
        shiftLeft_helper("sqrt(2)", 3, "8*sqrt(2)");
        shiftLeft_helper("sqrt(2)", 4, "16*sqrt(2)");
        shiftLeft_helper("sqrt(2)", -1, "sqrt(2)/2");
        shiftLeft_helper("sqrt(2)", -2, "sqrt(2)/4");
        shiftLeft_helper("sqrt(2)", -3, "sqrt(2)/8");
        shiftLeft_helper("sqrt(2)", -4, "sqrt(2)/16");

        shiftLeft_helper("-sqrt(2)", 0, "-sqrt(2)");
        shiftLeft_helper("-sqrt(2)", 1, "-2*sqrt(2)");
        shiftLeft_helper("-sqrt(2)", 2, "-4*sqrt(2)");
        shiftLeft_helper("-sqrt(2)", 3, "-8*sqrt(2)");
        shiftLeft_helper("-sqrt(2)", 4, "-16*sqrt(2)");
        shiftLeft_helper("-sqrt(2)", -1, "-sqrt(2)/2");
        shiftLeft_helper("-sqrt(2)", -2, "-sqrt(2)/4");
        shiftLeft_helper("-sqrt(2)", -3, "-sqrt(2)/8");
        shiftLeft_helper("-sqrt(2)", -4, "-sqrt(2)/16");

        shiftLeft_helper("(1+sqrt(5))/2", 0, "(1+sqrt(5))/2");
        shiftLeft_helper("(1+sqrt(5))/2", 1, "1+sqrt(5)");
        shiftLeft_helper("(1+sqrt(5))/2", 2, "2+2*sqrt(5)");
        shiftLeft_helper("(1+sqrt(5))/2", 3, "4+4*sqrt(5)");
        shiftLeft_helper("(1+sqrt(5))/2", 4, "8+8*sqrt(5)");
        shiftLeft_helper("(1+sqrt(5))/2", -1, "(1+sqrt(5))/4");
        shiftLeft_helper("(1+sqrt(5))/2", -2, "(1+sqrt(5))/8");
        shiftLeft_helper("(1+sqrt(5))/2", -3, "(1+sqrt(5))/16");
        shiftLeft_helper("(1+sqrt(5))/2", -4, "(1+sqrt(5))/32");

        shiftLeft_helper("root 0 of x^5-x-1", 0, "root 0 of x^5-x-1");
        shiftLeft_helper("root 0 of x^5-x-1", 1, "root 0 of x^5-16*x-32");
        shiftLeft_helper("root 0 of x^5-x-1", 2, "root 0 of x^5-256*x-1024");
        shiftLeft_helper("root 0 of x^5-x-1", 3, "root 0 of x^5-4096*x-32768");
        shiftLeft_helper("root 0 of x^5-x-1", 4, "root 0 of x^5-65536*x-1048576");
        shiftLeft_helper("root 0 of x^5-x-1", -1, "root 0 of 32*x^5-2*x-1");
        shiftLeft_helper("root 0 of x^5-x-1", -2, "root 0 of 1024*x^5-4*x-1");
        shiftLeft_helper("root 0 of x^5-x-1", -3, "root 0 of 32768*x^5-8*x-1");
        shiftLeft_helper("root 0 of x^5-x-1", -4, "root 0 of 1048576*x^5-16*x-1");
    }

    private static void shiftRight_helper(@NotNull String r, int bits, @NotNull String output) {
        Algebraic x = readStrict(r).get().shiftRight(bits);
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testShiftRight() {
        shiftRight_helper("0", 0, "0");
        shiftRight_helper("0", 1, "0");
        shiftRight_helper("0", 2, "0");
        shiftRight_helper("0", 3, "0");
        shiftRight_helper("0", 4, "0");
        shiftRight_helper("0", -1, "0");
        shiftRight_helper("0", -2, "0");
        shiftRight_helper("0", -3, "0");
        shiftRight_helper("0", -4, "0");

        shiftRight_helper("1", 0, "1");
        shiftRight_helper("1", 1, "1/2");
        shiftRight_helper("1", 2, "1/4");
        shiftRight_helper("1", 3, "1/8");
        shiftRight_helper("1", 4, "1/16");
        shiftRight_helper("1", -1, "2");
        shiftRight_helper("1", -2, "4");
        shiftRight_helper("1", -3, "8");
        shiftRight_helper("1", -4, "16");

        shiftRight_helper("1/2", 0, "1/2");
        shiftRight_helper("1/2", 1, "1/4");
        shiftRight_helper("1/2", 2, "1/8");
        shiftRight_helper("1/2", 3, "1/16");
        shiftRight_helper("1/2", 4, "1/32");
        shiftRight_helper("1/2", -1, "1");
        shiftRight_helper("1/2", -2, "2");
        shiftRight_helper("1/2", -3, "4");
        shiftRight_helper("1/2", -4, "8");

        shiftRight_helper("-4/3", 0, "-4/3");
        shiftRight_helper("-4/3", 1, "-2/3");
        shiftRight_helper("-4/3", 2, "-1/3");
        shiftRight_helper("-4/3", 3, "-1/6");
        shiftRight_helper("-4/3", 4, "-1/12");
        shiftRight_helper("-4/3", -1, "-8/3");
        shiftRight_helper("-4/3", -2, "-16/3");
        shiftRight_helper("-4/3", -3, "-32/3");
        shiftRight_helper("-4/3", -4, "-64/3");

        shiftRight_helper("sqrt(2)", 0, "sqrt(2)");
        shiftRight_helper("sqrt(2)", 1, "sqrt(2)/2");
        shiftRight_helper("sqrt(2)", 2, "sqrt(2)/4");
        shiftRight_helper("sqrt(2)", 3, "sqrt(2)/8");
        shiftRight_helper("sqrt(2)", 4, "sqrt(2)/16");
        shiftRight_helper("sqrt(2)", -1, "2*sqrt(2)");
        shiftRight_helper("sqrt(2)", -2, "4*sqrt(2)");
        shiftRight_helper("sqrt(2)", -3, "8*sqrt(2)");
        shiftRight_helper("sqrt(2)", -4, "16*sqrt(2)");

        shiftRight_helper("-sqrt(2)", 0, "-sqrt(2)");
        shiftRight_helper("-sqrt(2)", 1, "-sqrt(2)/2");
        shiftRight_helper("-sqrt(2)", 2, "-sqrt(2)/4");
        shiftRight_helper("-sqrt(2)", 3, "-sqrt(2)/8");
        shiftRight_helper("-sqrt(2)", 4, "-sqrt(2)/16");
        shiftRight_helper("-sqrt(2)", -1, "-2*sqrt(2)");
        shiftRight_helper("-sqrt(2)", -2, "-4*sqrt(2)");
        shiftRight_helper("-sqrt(2)", -3, "-8*sqrt(2)");
        shiftRight_helper("-sqrt(2)", -4, "-16*sqrt(2)");

        shiftRight_helper("(1+sqrt(5))/2", 0, "(1+sqrt(5))/2");
        shiftRight_helper("(1+sqrt(5))/2", 1, "(1+sqrt(5))/4");
        shiftRight_helper("(1+sqrt(5))/2", 2, "(1+sqrt(5))/8");
        shiftRight_helper("(1+sqrt(5))/2", 3, "(1+sqrt(5))/16");
        shiftRight_helper("(1+sqrt(5))/2", 4, "(1+sqrt(5))/32");
        shiftRight_helper("(1+sqrt(5))/2", -1, "1+sqrt(5)");
        shiftRight_helper("(1+sqrt(5))/2", -2, "2+2*sqrt(5)");
        shiftRight_helper("(1+sqrt(5))/2", -3, "4+4*sqrt(5)");
        shiftRight_helper("(1+sqrt(5))/2", -4, "8+8*sqrt(5)");

        shiftRight_helper("root 0 of x^5-x-1", 0, "root 0 of x^5-x-1");
        shiftRight_helper("root 0 of x^5-x-1", 1, "root 0 of 32*x^5-2*x-1");
        shiftRight_helper("root 0 of x^5-x-1", 2, "root 0 of 1024*x^5-4*x-1");
        shiftRight_helper("root 0 of x^5-x-1", 3, "root 0 of 32768*x^5-8*x-1");
        shiftRight_helper("root 0 of x^5-x-1", 4, "root 0 of 1048576*x^5-16*x-1");
        shiftRight_helper("root 0 of x^5-x-1", -1, "root 0 of x^5-16*x-32");
        shiftRight_helper("root 0 of x^5-x-1", -2, "root 0 of x^5-256*x-1024");
        shiftRight_helper("root 0 of x^5-x-1", -3, "root 0 of x^5-4096*x-32768");
        shiftRight_helper("root 0 of x^5-x-1", -4, "root 0 of x^5-65536*x-1048576");
    }

    private static void pow_helper(@NotNull String r, int p, @NotNull String output) {
        Algebraic x = readStrict(r).get().pow(p);
        x.validate();
        aeq(x, output);
    }

    private static void pow_fail_helper(@NotNull String r, int p) {
        try {
            readStrict(r).get().pow(p);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPow() {
        pow_helper("0", 0, "1");
        pow_helper("0", 1, "0");
        pow_helper("0", 2, "0");
        pow_helper("0", 3, "0");
        pow_helper("0", 100, "0");

        pow_helper("1", 0, "1");
        pow_helper("1", 1, "1");
        pow_helper("1", 2, "1");
        pow_helper("1", 3, "1");
        pow_helper("1", 100, "1");
        pow_helper("1", -1, "1");
        pow_helper("1", -2, "1");
        pow_helper("1", -3, "1");
        pow_helper("1", -100, "1");

        pow_helper("1/2", 0, "1");
        pow_helper("1/2", 1, "1/2");
        pow_helper("1/2", 2, "1/4");
        pow_helper("1/2", 3, "1/8");
        pow_helper("1/2", 100, "1/1267650600228229401496703205376");
        pow_helper("1/2", -1, "2");
        pow_helper("1/2", -2, "4");
        pow_helper("1/2", -3, "8");
        pow_helper("1/2", -100, "1267650600228229401496703205376");

        pow_helper("-4/3", 0, "1");
        pow_helper("-4/3", 1, "-4/3");
        pow_helper("-4/3", 2, "16/9");
        pow_helper("-4/3", 3, "-64/27");
        pow_helper("-4/3", 100,
                "1606938044258990275541962092341162602522202993782792835301376/5153775207320113310364611297656212727" +
                "02107522001");
        pow_helper("-4/3", -1, "-3/4");
        pow_helper("-4/3", -2, "9/16");
        pow_helper("-4/3", -3, "-27/64");
        pow_helper("-4/3", -100,
                "515377520732011331036461129765621272702107522001/16069380442589902755419620923411626025222029937827" +
                "92835301376");

        pow_helper("sqrt(2)", 0, "1");
        pow_helper("sqrt(2)", 1, "sqrt(2)");
        pow_helper("sqrt(2)", 2, "2");
        pow_helper("sqrt(2)", 3, "2*sqrt(2)");
        pow_helper("sqrt(2)", 100, "1125899906842624");
        pow_helper("sqrt(2)", -1, "sqrt(2)/2");
        pow_helper("sqrt(2)", -2, "1/2");
        pow_helper("sqrt(2)", -3, "sqrt(2)/4");
        pow_helper("sqrt(2)", -100, "1/1125899906842624");

        pow_helper("-sqrt(2)", 0, "1");
        pow_helper("-sqrt(2)", 1, "-sqrt(2)");
        pow_helper("-sqrt(2)", 2, "2");
        pow_helper("-sqrt(2)", 3, "-2*sqrt(2)");
        pow_helper("-sqrt(2)", 100, "1125899906842624");
        pow_helper("-sqrt(2)", -1, "-sqrt(2)/2");
        pow_helper("-sqrt(2)", -2, "1/2");
        pow_helper("-sqrt(2)", -3, "-sqrt(2)/4");
        pow_helper("-sqrt(2)", -100, "1/1125899906842624");

        pow_helper("(1+sqrt(5))/2", 0, "1");
        pow_helper("(1+sqrt(5))/2", 1, "(1+sqrt(5))/2");
        pow_helper("(1+sqrt(5))/2", 2, "(3+sqrt(5))/2");
        pow_helper("(1+sqrt(5))/2", 3, "2+sqrt(5)");
        pow_helper("(1+sqrt(5))/2", 100, "root 1 of x^2-792070839848372253127*x+1");
        pow_helper("(1+sqrt(5))/2", -1, "(-1+sqrt(5))/2");
        pow_helper("(1+sqrt(5))/2", -2, "(3-sqrt(5))/2");
        pow_helper("(1+sqrt(5))/2", -3, "-2+sqrt(5)");
        pow_helper("(1+sqrt(5))/2", -100, "root 0 of x^2-792070839848372253127*x+1");

        pow_helper("root 0 of x^5-x-1", 0, "1");
        pow_helper("root 0 of x^5-x-1", 1, "root 0 of x^5-x-1");
        pow_helper("root 0 of x^5-x-1", 2, "root 0 of x^5-2*x^3+x-1");
        pow_helper("root 0 of x^5-x-1", 3, "root 0 of x^5-3*x^2-x-1");
        pow_helper("root 0 of x^5-x-1", 100,
                "root 0 of x^5-5212284*x^4-85855281194*x^3-827865826315124*x^2+40049481*x-1");
        pow_helper("root 0 of x^5-x-1", -1, "root 0 of x^5+x^4-1");
        pow_helper("root 0 of x^5-x-1", -2, "root 0 of x^5-x^4+2*x^2-1");
        pow_helper("root 0 of x^5-x-1", -3, "root 0 of x^5+x^4+3*x^3-1");
        pow_helper("root 0 of x^5-x-1", -100,
                "root 0 of x^5-40049481*x^4+827865826315124*x^3+85855281194*x^2+5212284*x-1");

        pow_fail_helper("0", -1);
        pow_fail_helper("0", -2);
        pow_fail_helper("0", -3);
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readAlgebraicList("[-sqrt(2), -4/3, 0, 1/2, 1, root 0 of x^5-x-1, sqrt(2), (1+sqrt(5))/2, sqrt(3)]"),
                readAlgebraicList("[-sqrt(2), -4/3, 0, 1/2, 1, root 0 of x^5-x-1, sqrt(2), (1+sqrt(5))/2, sqrt(3)]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(readStrict(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("0", 32);
        hashCode_helper("1", 63);
        hashCode_helper("1/2", 64);
        hashCode_helper("-4/3", -90);
        hashCode_helper("sqrt(2)", 27901);
        hashCode_helper("-sqrt(2)", 27870);
        hashCode_helper("(1+sqrt(5))/2", 28831);
        hashCode_helper("root 0 of x^5-x-1", 857951010);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(
                readAlgebraicList("[-sqrt(2), -4/3, 0, 1/2, 1, root 0 of x^5-x-1, sqrt(2), (1+sqrt(5))/2, sqrt(3)]")
        );
    }

    private static void readStrict_String_helper(@NotNull String input) {
        Algebraic x = readStrict(input).get();
        x.validate();
        aeq(x, input);
    }

    private static void readStrict_String_fail_helper(@NotNull String input) {
        assertFalse(readStrict(input).isPresent());
    }

    @Test
    public void testReadStrict_String() {
        readStrict_String_helper("0");
        readStrict_String_helper("1");
        readStrict_String_helper("1/2");
        readStrict_String_helper("-4/3");
        readStrict_String_helper("sqrt(2)");
        readStrict_String_helper("-sqrt(2)");
        readStrict_String_helper("(1+sqrt(5))/2");
        readStrict_String_helper("root 0 of x^5-x-1");
        readStrict_String_helper("root 1 of x^2-10000000000000");

        readStrict_String_fail_helper("");
        readStrict_String_fail_helper(" ");
        readStrict_String_fail_helper("x");
        readStrict_String_fail_helper("x^2+3");
        readStrict_String_fail_helper("2/4");
        readStrict_String_fail_helper("-");
        readStrict_String_fail_helper("00");
        readStrict_String_fail_helper("01");
        readStrict_String_fail_helper("-0");
        readStrict_String_fail_helper("+0");
        readStrict_String_fail_helper("+2");
        readStrict_String_fail_helper("sqrt(a)");
        readStrict_String_fail_helper("sqrt(4)");
        readStrict_String_fail_helper("sqrt(-1)");
        readStrict_String_fail_helper("sqrt(0)");
        readStrict_String_fail_helper("0*sqrt(2)");
        readStrict_String_fail_helper("a*sqrt(2)");
        readStrict_String_fail_helper("1*sqrt(2)");
        readStrict_String_fail_helper("-1*sqrt(2)");
        readStrict_String_fail_helper("a+sqrt(2)");
        readStrict_String_fail_helper("0+sqrt(2)");
        readStrict_String_fail_helper("0-sqrt(2)");
        readStrict_String_fail_helper("(sqrt(2))");
        readStrict_String_fail_helper("*sqrt(2)");
        readStrict_String_fail_helper("+sqrt(2)");
        readStrict_String_fail_helper("(sqrt(2))/2");
        readStrict_String_fail_helper("sqrt(2)/1");
        readStrict_String_fail_helper("sqrt(2)/0");
        readStrict_String_fail_helper("sqrt(2)/-1");
        readStrict_String_fail_helper("sqrt(2)+1");
        readStrict_String_fail_helper("sqrt(2)+sqrt(3)");
        readStrict_String_fail_helper("(2+2*sqrt(2))/2");
        readStrict_String_fail_helper("root -1 of x^5-x-1");
        readStrict_String_fail_helper("root 1 of x^5-x-1");
        readStrict_String_fail_helper("root 0 of x^2-1");
        readStrict_String_fail_helper("root 0 of x^10");
        readStrict_String_fail_helper("roof 0 of x^5-x-1");
        readStrict_String_fail_helper("root 0 on x^5-x-1");
        readStrict_String_fail_helper("root 0 of 0");
        readStrict_String_fail_helper("root 0 of 1");
        readStrict_String_fail_helper("root 0 of x^2-2");
        readStrict_String_fail_helper("root 0 of x-2");
    }

    private static void readStrict_int_String_helper(int maxDegree, @NotNull String input) {
        Algebraic x = readStrict(maxDegree, input).get();
        x.validate();
        aeq(x, input);
    }

    private static void readStrict_int_String_fail_helper(int maxDegree, @NotNull String input) {
        assertFalse(readStrict(maxDegree, input).isPresent());
    }

    private static void readStrict_int_String_bad_maxExponent_fail_helper(int maxDegree, @NotNull String input) {
        try {
            readStrict(maxDegree, input);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testReadStrict_int_String() {
        readStrict_int_String_helper(2, "0");
        readStrict_int_String_helper(2, "1");
        readStrict_int_String_helper(2, "1/2");
        readStrict_int_String_helper(2, "-4/3");
        readStrict_int_String_helper(2, "sqrt(2)");
        readStrict_int_String_helper(2, "-sqrt(2)");
        readStrict_int_String_helper(2, "(1+sqrt(5))/2");
        readStrict_int_String_helper(5, "root 0 of x^5-x-1");
        readStrict_int_String_helper(2, "root 1 of x^2-10000000000000");

        readStrict_int_String_fail_helper(4, "root 0 of x^5-x-1");
        readStrict_int_String_fail_helper(10, "");
        readStrict_int_String_fail_helper(10, " ");
        readStrict_int_String_fail_helper(10, "x");
        readStrict_int_String_fail_helper(10, "x^2+3");
        readStrict_int_String_fail_helper(10, "2/4");
        readStrict_int_String_fail_helper(10, "-");
        readStrict_int_String_fail_helper(10, "00");
        readStrict_int_String_fail_helper(10, "01");
        readStrict_int_String_fail_helper(10, "-0");
        readStrict_int_String_fail_helper(10, "+0");
        readStrict_int_String_fail_helper(10, "+2");
        readStrict_int_String_fail_helper(10, "sqrt(a)");
        readStrict_int_String_fail_helper(10, "sqrt(4)");
        readStrict_int_String_fail_helper(10, "sqrt(-1)");
        readStrict_int_String_fail_helper(10, "sqrt(0)");
        readStrict_int_String_fail_helper(10, "0*sqrt(2)");
        readStrict_int_String_fail_helper(10, "a*sqrt(2)");
        readStrict_int_String_fail_helper(10, "1*sqrt(2)");
        readStrict_int_String_fail_helper(10, "-1*sqrt(2)");
        readStrict_int_String_fail_helper(10, "a+sqrt(2)");
        readStrict_int_String_fail_helper(10, "0+sqrt(2)");
        readStrict_int_String_fail_helper(10, "0-sqrt(2)");
        readStrict_int_String_fail_helper(10, "(sqrt(2))");
        readStrict_int_String_fail_helper(10, "*sqrt(2)");
        readStrict_int_String_fail_helper(10, "+sqrt(2)");
        readStrict_int_String_fail_helper(10, "(sqrt(2))/2");
        readStrict_int_String_fail_helper(10, "sqrt(2)/1");
        readStrict_int_String_fail_helper(10, "sqrt(2)/0");
        readStrict_int_String_fail_helper(10, "sqrt(2)/-1");
        readStrict_int_String_fail_helper(10, "sqrt(2)+1");
        readStrict_int_String_fail_helper(10, "sqrt(2)+sqrt(3)");
        readStrict_int_String_fail_helper(10, "(2+2*sqrt(2))/2");
        readStrict_int_String_fail_helper(10, "root -1 of x^5-x-1");
        readStrict_int_String_fail_helper(10, "root 1 of x^5-x-1");
        readStrict_int_String_fail_helper(10, "root 0 of x^2-1");
        readStrict_int_String_fail_helper(10, "root 0 of x^10");
        readStrict_int_String_fail_helper(10, "roof 0 of x^5-x-1");
        readStrict_int_String_fail_helper(10, "root 0 on x^5-x-1");
        readStrict_int_String_fail_helper(10, "root 0 of 0");
        readStrict_int_String_fail_helper(10, "root 0 of 1");
        readStrict_int_String_fail_helper(10, "root 0 of x^2-2");
        readStrict_int_String_fail_helper(10, "root 0 of x-2");

        readStrict_int_String_bad_maxExponent_fail_helper(1, "sqrt(2)");
        readStrict_int_String_bad_maxExponent_fail_helper(0, "sqrt(2)");
        readStrict_int_String_bad_maxExponent_fail_helper(-1, "sqrt(2)");
    }

    private static @NotNull List<Algebraic> readAlgebraicList(@NotNull String s) {
        return Readers.readListStrict(Algebraic::readStrict).apply(s).get();
    }

    private static @NotNull List<Algebraic> readAlgebraicListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Algebraic::readStrict).apply(s).get();
    }
}
