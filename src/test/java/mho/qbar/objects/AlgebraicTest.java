package mho.qbar.objects;

import mho.wheels.io.Readers;
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
        aeq(read(input).get(), input);
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
        aeq(read(maxDegree, input).get(), input);
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
