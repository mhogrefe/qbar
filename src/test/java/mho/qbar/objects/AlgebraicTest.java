package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.math.BinaryFraction;
import mho.wheels.structures.Pair;
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
        Algebraic x = of(Polynomial.read(polynomial).get(), rootIndex);
        x.validate();
        aeq(x, output);
    }

    private static void of_Polynomial_int_fail_helper(@NotNull String polynomial, int rootIndex) {
        try {
            of(Polynomial.read(polynomial).get(), rootIndex);
            fail();
        } catch (IllegalArgumentException ignored) {}
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

        of_Polynomial_int_fail_helper("0", 0);
        of_Polynomial_int_fail_helper("1", 0);
        of_Polynomial_int_fail_helper("x", 1);
        of_Polynomial_int_fail_helper("x", -1);
        of_Polynomial_int_fail_helper("x^2+1", 0);
        of_Polynomial_int_fail_helper("x^2-1", 2);
    }

    private static void of_Rational_helper(@NotNull String input) {
        Algebraic x = of(Rational.read(input).get());
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
        Algebraic x = of(Readers.readBigInteger(input).get());
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
        Algebraic x = of(BinaryFraction.read(input).get());
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
        Algebraic x = of(Readers.readBigDecimal(input).get());
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
        aeq(read(input).get().isInteger(), output);
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
        aeq(read(x).get().bigIntegerValue(Readers.readRoundingMode(roundingMode).get()), output);
    }

    private static void bigIntegerValue_RoundingMode_fail_helper(@NotNull String x, @NotNull String roundingMode) {
        try {
            read(x).get().bigIntegerValue(Readers.readRoundingMode(roundingMode).get());
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
        aeq(read(x).get().bigIntegerValue(), output);
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
        aeq(read(x).get().floor(), output);
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
        aeq(read(x).get().ceiling(), output);
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
        aeq(read(x).get().bigIntegerValueExact(), output);
    }

    private static void bigIntegerValueExact_fail_helper(@NotNull String x) {
        try {
            read(x).get().bigIntegerValueExact();
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
        aeq(read(r).get().byteValueExact(), r);
    }

    private static void byteValueExact_fail_helper(@NotNull String r) {
        try {
            read(r).get().byteValueExact();
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
        aeq(read(r).get().shortValueExact(), r);
    }

    private static void shortValueExact_fail_helper(@NotNull String r) {
        try {
            read(r).get().shortValueExact();
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
        aeq(read(r).get().intValueExact(), r);
    }

    private static void intValueExact_fail_helper(@NotNull String r) {
        try {
            read(r).get().intValueExact();
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
        aeq(read(r).get().longValueExact(), r);
    }

    private static void longValueExact_fail_helper(@NotNull String r) {
        try {
            read(r).get().longValueExact();
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
        aeq(read(input).get().isIntegerPowerOfTwo(), output);
    }

    private static void isIntegerPowerOfTwo_fail_helper(@NotNull String input) {
        try {
            read(input).get().isIntegerPowerOfTwo();
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
        aeq(read(input).get().roundUpToIntegerPowerOfTwo(), output);
    }

    private static void roundUpToIntegerPowerOfTwo_fail_helper(@NotNull String input) {
        try {
            read(input).get().roundUpToIntegerPowerOfTwo();
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
        aeq(read(input).get().isBinaryFraction(), output);
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
        aeq(read(input).get().binaryFractionValueExact(), output);
    }

    private static void binaryFractionValueExact_fail_helper(@NotNull String input) {
        try {
            read(input).get().binaryFractionValueExact();
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
        aeq(read(input).get().isRational(), output);
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
        aeq(read(input).get().isAlgebraicInteger(), output);
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
        aeq(read(input).get().rationalValueExact(), input);
    }

    private static void rationalValueExact_fail_helper(@NotNull String input) {
        try {
            read(input).get().rationalValueExact();
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
        aeq(read(input).get().realValue(), output);
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

    private static void isEqualToFloat_helper(@NotNull String r, boolean output) {
        aeq(read(r).get().isEqualToFloat(), output);
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

    private static void isEqualToDouble_helper(@NotNull String r, boolean output) {
        aeq(read(r).get().isEqualToDouble(), output);
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
        aeq(read(x).get().floatValue(Readers.readRoundingMode(roundingMode).get()), output);
    }

    private static void floatValue_RoundingMode_fail_helper(@NotNull String x, @NotNull String roundingMode) {
        try {
            read(x).get().floatValue(Readers.readRoundingMode(roundingMode).get());
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

    private static void floatValue_helper(@NotNull String x, float output) {
        aeq(read(x).get().floatValue(), output);
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

    private static void floatValueExact_helper(@NotNull String x, float output) {
        aeq(read(x).get().floatValueExact(), output);
    }

    private static void floatValueExact_fail_helper(@NotNull String x) {
        try {
            read(x).get().floatValueExact();
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
        aeq(read(x).get().doubleValue(Readers.readRoundingMode(roundingMode).get()), output);
    }

    private static void doubleValue_RoundingMode_fail_helper(@NotNull String x, @NotNull String roundingMode) {
        try {
            read(x).get().doubleValue(Readers.readRoundingMode(roundingMode).get());
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

    private static void doubleValue_helper(@NotNull String x, double output) {
        aeq(read(x).get().doubleValue(), output);
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

    private static void doubleValueExact_helper(@NotNull String x, double output) {
        aeq(read(x).get().doubleValueExact(), output);
    }

    private static void doubleValueExact_fail_helper(@NotNull String x) {
        try {
            read(x).get().doubleValueExact();
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

    @Test
    public void testEquals() {
        testEqualsHelper(
                readAlgebraicList("[-sqrt(2), -4/3, 0, 1/2, 1, root 0 of x^5-x-1, sqrt(2), (1+sqrt(5))/2, sqrt(3)]"),
                readAlgebraicList("[-sqrt(2), -4/3, 0, 1/2, 1, root 0 of x^5-x-1, sqrt(2), (1+sqrt(5))/2, sqrt(3)]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
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

    private static void read_String_helper(@NotNull String input) {
        Algebraic x = read(input).get();
        x.validate();
        aeq(x, input);
    }

    private static void read_String_fail_helper(@NotNull String input) {
        assertFalse(read(input).isPresent());
    }

    @Test
    public void testRead_String() {
        read_String_helper("0");
        read_String_helper("1");
        read_String_helper("1/2");
        read_String_helper("-4/3");
        read_String_helper("sqrt(2)");
        read_String_helper("-sqrt(2)");
        read_String_helper("(1+sqrt(5))/2");
        read_String_helper("root 0 of x^5-x-1");
        read_String_helper("root 1 of x^2-10000000000000");

        read_String_fail_helper("");
        read_String_fail_helper(" ");
        read_String_fail_helper("x");
        read_String_fail_helper("x^2+3");
        read_String_fail_helper("2/4");
        read_String_fail_helper("-");
        read_String_fail_helper("00");
        read_String_fail_helper("01");
        read_String_fail_helper("-0");
        read_String_fail_helper("+0");
        read_String_fail_helper("+2");
        read_String_fail_helper("sqrt(a)");
        read_String_fail_helper("sqrt(4)");
        read_String_fail_helper("sqrt(-1)");
        read_String_fail_helper("sqrt(0)");
        read_String_fail_helper("0*sqrt(2)");
        read_String_fail_helper("a*sqrt(2)");
        read_String_fail_helper("1*sqrt(2)");
        read_String_fail_helper("-1*sqrt(2)");
        read_String_fail_helper("a+sqrt(2)");
        read_String_fail_helper("0+sqrt(2)");
        read_String_fail_helper("0-sqrt(2)");
        read_String_fail_helper("(sqrt(2))");
        read_String_fail_helper("*sqrt(2)");
        read_String_fail_helper("+sqrt(2)");
        read_String_fail_helper("(sqrt(2))/2");
        read_String_fail_helper("sqrt(2)/1");
        read_String_fail_helper("sqrt(2)/0");
        read_String_fail_helper("sqrt(2)/-1");
        read_String_fail_helper("sqrt(2)+1");
        read_String_fail_helper("sqrt(2)+sqrt(3)");
        read_String_fail_helper("(2+2*sqrt(2))/2");
        read_String_fail_helper("root -1 of x^5-x-1");
        read_String_fail_helper("root 1 of x^5-x-1");
        read_String_fail_helper("root 0 of x^2-1");
        read_String_fail_helper("root 0 of x^10");
        read_String_fail_helper("roof 0 of x^5-x-1");
        read_String_fail_helper("root 0 on x^5-x-1");
        read_String_fail_helper("root 0 of 0");
        read_String_fail_helper("root 0 of 1");
        read_String_fail_helper("root 0 of x^2-2");
        read_String_fail_helper("root 0 of x-2");
    }

    private static void read_int_String_helper(int maxDegree, @NotNull String input) {
        Algebraic x = read(maxDegree, input).get();
        x.validate();
        aeq(x, input);
    }

    private static void read_int_String_fail_helper(int maxDegree, @NotNull String input) {
        assertFalse(read(maxDegree, input).isPresent());
    }

    private static void read_int_String_bad_maxExponent_fail_helper(int maxDegree, @NotNull String input) {
        try {
            read(maxDegree, input);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRead_int_String() {
        read_int_String_helper(2, "0");
        read_int_String_helper(2, "1");
        read_int_String_helper(2, "1/2");
        read_int_String_helper(2, "-4/3");
        read_int_String_helper(2, "sqrt(2)");
        read_int_String_helper(2, "-sqrt(2)");
        read_int_String_helper(2, "(1+sqrt(5))/2");
        read_int_String_helper(5, "root 0 of x^5-x-1");
        read_int_String_helper(2, "root 1 of x^2-10000000000000");

        read_int_String_fail_helper(4, "root 0 of x^5-x-1");
        read_int_String_fail_helper(10, "");
        read_int_String_fail_helper(10, " ");
        read_int_String_fail_helper(10, "x");
        read_int_String_fail_helper(10, "x^2+3");
        read_int_String_fail_helper(10, "2/4");
        read_int_String_fail_helper(10, "-");
        read_int_String_fail_helper(10, "00");
        read_int_String_fail_helper(10, "01");
        read_int_String_fail_helper(10, "-0");
        read_int_String_fail_helper(10, "+0");
        read_int_String_fail_helper(10, "+2");
        read_int_String_fail_helper(10, "sqrt(a)");
        read_int_String_fail_helper(10, "sqrt(4)");
        read_int_String_fail_helper(10, "sqrt(-1)");
        read_int_String_fail_helper(10, "sqrt(0)");
        read_int_String_fail_helper(10, "0*sqrt(2)");
        read_int_String_fail_helper(10, "a*sqrt(2)");
        read_int_String_fail_helper(10, "1*sqrt(2)");
        read_int_String_fail_helper(10, "-1*sqrt(2)");
        read_int_String_fail_helper(10, "a+sqrt(2)");
        read_int_String_fail_helper(10, "0+sqrt(2)");
        read_int_String_fail_helper(10, "0-sqrt(2)");
        read_int_String_fail_helper(10, "(sqrt(2))");
        read_int_String_fail_helper(10, "*sqrt(2)");
        read_int_String_fail_helper(10, "+sqrt(2)");
        read_int_String_fail_helper(10, "(sqrt(2))/2");
        read_int_String_fail_helper(10, "sqrt(2)/1");
        read_int_String_fail_helper(10, "sqrt(2)/0");
        read_int_String_fail_helper(10, "sqrt(2)/-1");
        read_int_String_fail_helper(10, "sqrt(2)+1");
        read_int_String_fail_helper(10, "sqrt(2)+sqrt(3)");
        read_int_String_fail_helper(10, "(2+2*sqrt(2))/2");
        read_int_String_fail_helper(10, "root -1 of x^5-x-1");
        read_int_String_fail_helper(10, "root 1 of x^5-x-1");
        read_int_String_fail_helper(10, "root 0 of x^2-1");
        read_int_String_fail_helper(10, "root 0 of x^10");
        read_int_String_fail_helper(10, "roof 0 of x^5-x-1");
        read_int_String_fail_helper(10, "root 0 on x^5-x-1");
        read_int_String_fail_helper(10, "root 0 of 0");
        read_int_String_fail_helper(10, "root 0 of 1");
        read_int_String_fail_helper(10, "root 0 of x^2-2");
        read_int_String_fail_helper(10, "root 0 of x-2");
        read_int_String_bad_maxExponent_fail_helper(1, "sqrt(2)");
        read_int_String_bad_maxExponent_fail_helper(0, "sqrt(2)");
        read_int_String_bad_maxExponent_fail_helper(-1, "sqrt(2)");
    }

    private static void findIn_String_helper(@NotNull String input, @NotNull String output, int index) {
        Pair<Algebraic, Integer> result = findIn(input).get();
        result.a.validate();
        aeq(result.a, output);
        aeq(result.b, index);
    }

    private static void findIn_String_fail_helper(@NotNull String input) {
        assertFalse(findIn(input).isPresent());
    }

    @Test
    public void testFindIn_String() {
        findIn_String_helper("sqrt(2)", "sqrt(2)", 0);
        findIn_String_helper("sqrt(-2)", "-2", 5);
        findIn_String_helper("03+sqrt(2)", "0", 0);
        findIn_String_helper("x3+sqrt(2)", "3+sqrt(2)", 1);
        findIn_String_helper("root 2 of x^2-2", "2", 5);
        findIn_String_helper("(2+4*sqrt(2))/2", "2+4*sqrt(2)", 1);
        findIn_String_helper("root 0 of 2*x^3-12", "root 0 of 2*x^3-1", 0);

        findIn_String_fail_helper("");
        findIn_String_fail_helper("o");
        findIn_String_fail_helper("hello");
    }

    private static void findIn_int_String_helper(
            int maxExponent,
            @NotNull String input,
            @NotNull String output,
            int index
    ) {
        Pair<Algebraic, Integer> result = findIn(maxExponent, input).get();
        result.a.validate();
        aeq(result.a, output);
        aeq(result.b, index);
    }

    private static void findIn_int_String_fail_helper(int maxExponent, @NotNull String input) {
        assertFalse(findIn(maxExponent, input).isPresent());
    }

    private static void findIn_int_String_bad_maxExponent_fail_helper(int maxExponent, @NotNull String input) {
        try {
            findIn(maxExponent, input);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFindIn_int_String() {
        findIn_int_String_helper(2, "sqrt(2)", "sqrt(2)", 0);
        findIn_int_String_helper(2, "sqrt(-2)", "-2", 5);
        findIn_int_String_helper(2, "03+sqrt(2)", "0", 0);
        findIn_int_String_helper(2, "x3+sqrt(2)", "3+sqrt(2)", 1);
        findIn_int_String_helper(2, "root 2 of x^2-2", "2", 5);
        findIn_int_String_helper(2, "(2+4*sqrt(2))/2", "2+4*sqrt(2)", 1);
        findIn_int_String_helper(3, "root 0 of 2*x^3-12", "root 0 of 2*x^3-1", 0);

        findIn_int_String_fail_helper(2, "");
        findIn_int_String_fail_helper(2, "o");
        findIn_int_String_fail_helper(2, "hello");
        findIn_int_String_bad_maxExponent_fail_helper(1, "sqrt(2)");
        findIn_int_String_bad_maxExponent_fail_helper(0, "sqrt(2)");
        findIn_int_String_bad_maxExponent_fail_helper(-1, "sqrt(2)");
    }

    private static @NotNull List<Algebraic> readAlgebraicList(@NotNull String s) {
        return Readers.readList(Algebraic::read).apply(s).get();
    }

    private static @NotNull List<Algebraic> readAlgebraicListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Algebraic::read).apply(s).get();
    }
}
