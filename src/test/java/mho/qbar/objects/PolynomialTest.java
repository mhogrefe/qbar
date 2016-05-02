package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.NullableOptional;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static mho.qbar.objects.Polynomial.*;
import static mho.qbar.objects.Polynomial.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PolynomialTest {
    @Test
    public void testConstants() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
        aeq(X, "x");
    }

    private static void iterator_helper(@NotNull String x, @NotNull String output) {
        aeq(toList(readStrict(x).get()), output);
    }

    @Test
    public void testIterator() {
        iterator_helper("0", "[]");
        iterator_helper("1", "[1]");
        iterator_helper("x", "[0, 1]");
        iterator_helper("-17", "[-17]");
        iterator_helper("x^2-4*x+7", "[7, -4, 1]");
        iterator_helper("x^3-1", "[-1, 0, 0, 1]");
        iterator_helper("3*x^10", "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3]");
    }

    private static void apply_BigInteger_helper(@NotNull String p, @NotNull String x, @NotNull String output) {
        aeq(readStrict(p).get().apply(Readers.readBigIntegerStrict(x).get()), output);
    }

    @Test
    public void testApply_BigInteger() {
        apply_BigInteger_helper("0", "0", "0");
        apply_BigInteger_helper("0", "1", "0");
        apply_BigInteger_helper("0", "-1", "0");
        apply_BigInteger_helper("0", "5", "0");
        apply_BigInteger_helper("0", "100", "0");

        apply_BigInteger_helper("1", "0", "1");
        apply_BigInteger_helper("1", "1", "1");
        apply_BigInteger_helper("1", "-1", "1");
        apply_BigInteger_helper("1", "5", "1");
        apply_BigInteger_helper("1", "100", "1");

        apply_BigInteger_helper("x", "0", "0");
        apply_BigInteger_helper("x", "1", "1");
        apply_BigInteger_helper("x", "-1", "-1");
        apply_BigInteger_helper("x", "5", "5");
        apply_BigInteger_helper("x", "100", "100");

        apply_BigInteger_helper("-17", "0", "-17");
        apply_BigInteger_helper("-17", "1", "-17");
        apply_BigInteger_helper("-17", "-1", "-17");
        apply_BigInteger_helper("-17", "5", "-17");
        apply_BigInteger_helper("-17", "100", "-17");

        apply_BigInteger_helper("x^2-4*x+7", "0", "7");
        apply_BigInteger_helper("x^2-4*x+7", "1", "4");
        apply_BigInteger_helper("x^2-4*x+7", "-1", "12");
        apply_BigInteger_helper("x^2-4*x+7", "5", "12");
        apply_BigInteger_helper("x^2-4*x+7", "100", "9607");

        apply_BigInteger_helper("x^3-1", "0", "-1");
        apply_BigInteger_helper("x^3-1", "1", "0");
        apply_BigInteger_helper("x^3-1", "-1", "-2");
        apply_BigInteger_helper("x^3-1", "5", "124");
        apply_BigInteger_helper("x^3-1", "100", "999999");

        apply_BigInteger_helper("3*x^10", "0", "0");
        apply_BigInteger_helper("3*x^10", "1", "3");
        apply_BigInteger_helper("3*x^10", "-1", "3");
        apply_BigInteger_helper("3*x^10", "5", "29296875");
        apply_BigInteger_helper("3*x^10", "100", "300000000000000000000");
    }

    private static void apply_Rational_helper(@NotNull String p, @NotNull String x, @NotNull String output) {
        aeq(readStrict(p).get().apply(Rational.readStrict(x).get()), output);
    }

    @Test
    public void testApply_Rational() {
        apply_Rational_helper("0", "0", "0");
        apply_Rational_helper("0", "1", "0");
        apply_Rational_helper("0", "-1", "0");
        apply_Rational_helper("0", "4/5", "0");
        apply_Rational_helper("0", "100", "0");

        apply_Rational_helper("1", "0", "1");
        apply_Rational_helper("1", "1", "1");
        apply_Rational_helper("1", "-1", "1");
        apply_Rational_helper("1", "4/5", "1");
        apply_Rational_helper("1", "100", "1");

        apply_Rational_helper("x", "0", "0");
        apply_Rational_helper("x", "1", "1");
        apply_Rational_helper("x", "-1", "-1");
        apply_Rational_helper("x", "4/5", "4/5");
        apply_Rational_helper("x", "100", "100");

        apply_Rational_helper("-17", "0", "-17");
        apply_Rational_helper("-17", "1", "-17");
        apply_Rational_helper("-17", "-1", "-17");
        apply_Rational_helper("-17", "4/5", "-17");
        apply_Rational_helper("-17", "100", "-17");

        apply_Rational_helper("x^2-4*x+7", "0", "7");
        apply_Rational_helper("x^2-4*x+7", "1", "4");
        apply_Rational_helper("x^2-4*x+7", "-1", "12");
        apply_Rational_helper("x^2-4*x+7", "4/5", "111/25");
        apply_Rational_helper("x^2-4*x+7", "100", "9607");

        apply_Rational_helper("x^3-1", "0", "-1");
        apply_Rational_helper("x^3-1", "1", "0");
        apply_Rational_helper("x^3-1", "-1", "-2");
        apply_Rational_helper("x^3-1", "4/5", "-61/125");
        apply_Rational_helper("x^3-1", "100", "999999");

        apply_Rational_helper("3*x^10", "0", "0");
        apply_Rational_helper("3*x^10", "1", "3");
        apply_Rational_helper("3*x^10", "-1", "3");
        apply_Rational_helper("3*x^10", "4/5", "3145728/9765625");
        apply_Rational_helper("3*x^10", "100", "300000000000000000000");
    }

    private static void specialApply_helper(@NotNull String p, @NotNull String x, @NotNull String output) {
        aeq(readStrict(p).get().specialApply(Rational.readStrict(x).get()), output);
    }

    @Test
    public void testSpecialApply_Rational() {
        specialApply_helper("0", "0", "0");
        specialApply_helper("0", "1", "0");
        specialApply_helper("0", "-1", "0");
        specialApply_helper("0", "4/5", "0");
        specialApply_helper("0", "100", "0");

        specialApply_helper("1", "0", "1");
        specialApply_helper("1", "1", "1");
        specialApply_helper("1", "-1", "1");
        specialApply_helper("1", "4/5", "1");
        specialApply_helper("1", "100", "1");

        specialApply_helper("x", "0", "0");
        specialApply_helper("x", "1", "1");
        specialApply_helper("x", "-1", "-1");
        specialApply_helper("x", "4/5", "4");
        specialApply_helper("x", "100", "100");

        specialApply_helper("-17", "0", "-17");
        specialApply_helper("-17", "1", "-17");
        specialApply_helper("-17", "-1", "-17");
        specialApply_helper("-17", "4/5", "-17");
        specialApply_helper("-17", "100", "-17");

        specialApply_helper("x^2-4*x+7", "0", "7");
        specialApply_helper("x^2-4*x+7", "1", "4");
        specialApply_helper("x^2-4*x+7", "-1", "12");
        specialApply_helper("x^2-4*x+7", "4/5", "111");
        specialApply_helper("x^2-4*x+7", "100", "9607");

        specialApply_helper("x^3-1", "0", "-1");
        specialApply_helper("x^3-1", "1", "0");
        specialApply_helper("x^3-1", "-1", "-2");
        specialApply_helper("x^3-1", "4/5", "-61");
        specialApply_helper("x^3-1", "100", "999999");

        specialApply_helper("3*x^10", "0", "0");
        specialApply_helper("3*x^10", "1", "3");
        specialApply_helper("3*x^10", "-1", "3");
        specialApply_helper("3*x^10", "4/5", "3145728");
        specialApply_helper("3*x^10", "100", "300000000000000000000");
    }

    private static void toRationalPolynomial_helper(@NotNull String input) {
        aeq(readStrict(input).get().toRationalPolynomial(), input);
    }

    @Test
    public void testToRationalPolynomial() {
        toRationalPolynomial_helper("0");
        toRationalPolynomial_helper("1");
        toRationalPolynomial_helper("x");
        toRationalPolynomial_helper("-17");
        toRationalPolynomial_helper("x^2-4*x+7");
        toRationalPolynomial_helper("x^3-1");
        toRationalPolynomial_helper("3*x^10");
    }

    private static void coefficient_helper(@NotNull String p, int i, @NotNull String output) {
        aeq(readStrict(p).get().coefficient(i), output);
    }

    private static void coefficient_fail_helper(@NotNull String p, int i) {
        try {
            readStrict(p).get().coefficient(i);
            fail();
        } catch (ArrayIndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testCoefficient() {
        coefficient_helper("0", 0, "0");
        coefficient_helper("0", 5, "0");
        coefficient_helper("1", 0, "1");
        coefficient_helper("1", 5, "0");
        coefficient_helper("x", 0, "0");
        coefficient_helper("x", 1, "1");
        coefficient_helper("x", 5, "0");
        coefficient_helper("x^2-4*x+7", 0, "7");
        coefficient_helper("x^2-4*x+7", 1, "-4");
        coefficient_helper("x^2-4*x+7", 2, "1");
        coefficient_helper("x^2-4*x+7", 3, "0");
        coefficient_helper("x^3-1", 0, "-1");
        coefficient_helper("x^3-1", 1, "0");
        coefficient_helper("x^3-1", 2, "0");
        coefficient_helper("x^3-1", 3, "1");
        coefficient_helper("x^3-1", 4, "0");
        coefficient_fail_helper("x^3-1", -1);
    }

    private static void of_List_BigInteger_helper(@NotNull String input, @NotNull String output) {
        aeq(of(readBigIntegerList(input)), output);
    }

    private static void of_List_BigInteger_fail_helper(@NotNull String input) {
        try {
            of(readBigIntegerListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testOf_List_BigInteger() {
        of_List_BigInteger_helper("[]", "0");
        of_List_BigInteger_helper("[0]", "0");
        of_List_BigInteger_helper("[0, 0, 0]", "0");
        of_List_BigInteger_helper("[1]", "1");
        of_List_BigInteger_helper("[1, 0, 0]", "1");
        of_List_BigInteger_helper("[0, 1]", "x");
        of_List_BigInteger_helper("[-17]", "-17");
        of_List_BigInteger_helper("[7, -4, 1]", "x^2-4*x+7");
        of_List_BigInteger_helper("[7, -4, 1, 0, 0]", "x^2-4*x+7");
        of_List_BigInteger_helper("[-1, 0, 0, 1]", "x^3-1");
        of_List_BigInteger_helper("[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3]", "3*x^10");
        of_List_BigInteger_fail_helper("[7, null, 1]");
    }

    private static void of_BigInteger_helper(@NotNull String input) {
        aeq(of(Readers.readBigIntegerStrict(input).get()), input);
    }

    @Test
    public void testOf_BigInteger() {
        of_BigInteger_helper("0");
        of_BigInteger_helper("1");
        of_BigInteger_helper("5");
        of_BigInteger_helper("-7");
    }

    private static void of_BigInteger_int_helper(@NotNull String input, int i, @NotNull String output) {
        aeq(of(Readers.readBigIntegerStrict(input).get(), i), output);
    }

    private static void of_BigInteger_int_fail_helper(@NotNull String input, int i) {
        try {
            of(Readers.readBigIntegerStrict(input).get(), i);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testOf_BigInteger_int() {
        of_BigInteger_int_helper("0", 0, "0");
        of_BigInteger_int_helper("0", 1, "0");
        of_BigInteger_int_helper("0", 2, "0");
        of_BigInteger_int_helper("0", 3, "0");

        of_BigInteger_int_helper("1", 0, "1");
        of_BigInteger_int_helper("1", 1, "x");
        of_BigInteger_int_helper("1", 2, "x^2");
        of_BigInteger_int_helper("1", 3, "x^3");

        of_BigInteger_int_helper("-1", 0, "-1");
        of_BigInteger_int_helper("-1", 1, "-x");
        of_BigInteger_int_helper("-1", 2, "-x^2");
        of_BigInteger_int_helper("-1", 3, "-x^3");

        of_BigInteger_int_helper("3", 0, "3");
        of_BigInteger_int_helper("3", 1, "3*x");
        of_BigInteger_int_helper("3", 2, "3*x^2");
        of_BigInteger_int_helper("3", 3, "3*x^3");

        of_BigInteger_int_helper("-5", 0, "-5");
        of_BigInteger_int_helper("-5", 1, "-5*x");
        of_BigInteger_int_helper("-5", 2, "-5*x^2");
        of_BigInteger_int_helper("-5", 3, "-5*x^3");

        of_BigInteger_int_fail_helper("0", -1);
        of_BigInteger_int_fail_helper("3", -1);
    }

    private static void fromRoot_BigInteger_helper(@NotNull String input, @NotNull String output) {
        aeq(fromRoot(Readers.readBigIntegerStrict(input).get()), output);
    }

    @Test
    public void testFromRoot_BigInteger() {
        fromRoot_BigInteger_helper("0", "x");
        fromRoot_BigInteger_helper("1", "x-1");
        fromRoot_BigInteger_helper("5", "x-5");
        fromRoot_BigInteger_helper("-7", "x+7");
    }

    private static void fromRoot_Rational_helper(@NotNull String input, @NotNull String output) {
        aeq(fromRoot(Rational.readStrict(input).get()), output);
    }

    @Test
    public void testFromRoot_Rational() {
        fromRoot_Rational_helper("0", "x");
        fromRoot_Rational_helper("1/2", "2*x-1");
        fromRoot_Rational_helper("-4/3", "3*x+4");
        fromRoot_Rational_helper("-7", "x+7");
    }

    private static void maxCoefficientBitLength_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().maxCoefficientBitLength(), output);
    }

    @Test
    public void testMaxCoefficientBitLength() {
        maxCoefficientBitLength_helper("0", 0);
        maxCoefficientBitLength_helper("1", 1);
        maxCoefficientBitLength_helper("x", 1);
        maxCoefficientBitLength_helper("-17", 5);
        maxCoefficientBitLength_helper("x^2-4*x+7", 3);
        maxCoefficientBitLength_helper("x^3-1", 1);
        maxCoefficientBitLength_helper("3*x^10", 2);
    }

    private static void degree_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().degree(), output);
    }

    @Test
    public void testDegree() {
        degree_helper("0", -1);
        degree_helper("1", 0);
        degree_helper("x", 1);
        degree_helper("-17", 0);
        degree_helper("x^2-4*x+7", 2);
        degree_helper("x^3-1", 3);
        degree_helper("3*x^10", 10);
    }

    private static void leading_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().leading().get(), output);
    }

    private static void leading_empty_helper(@NotNull String input) {
        assertFalse(readStrict(input).get().leading().isPresent());
    }

    @Test
    public void testLeading() {
        leading_helper("1", "1");
        leading_helper("x", "1");
        leading_helper("-17", "-17");
        leading_helper("x^2-4*x+7", "1");
        leading_helper("-x^3-1", "-1");
        leading_helper("3*x^10", "3");
        leading_empty_helper("0");
    }

    private static void multiplyByPowerOfX_helper(@NotNull String a, int p, @NotNull String output) {
        aeq(readStrict(a).get().multiplyByPowerOfX(p), output);
    }

    private static void multiplyByPowerOfX_fail_helper(@NotNull String a, int p) {
        try {
            readStrict(a).get().multiplyByPowerOfX(p);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testMultiplyByPowerOfX() {
        multiplyByPowerOfX_helper("0", 0, "0");
        multiplyByPowerOfX_helper("0", 1, "0");
        multiplyByPowerOfX_helper("0", 2, "0");
        multiplyByPowerOfX_helper("0", 3, "0");

        multiplyByPowerOfX_helper("1", 0, "1");
        multiplyByPowerOfX_helper("1", 1, "x");
        multiplyByPowerOfX_helper("1", 2, "x^2");
        multiplyByPowerOfX_helper("1", 3, "x^3");

        multiplyByPowerOfX_helper("-17", 0, "-17");
        multiplyByPowerOfX_helper("-17", 1, "-17*x");
        multiplyByPowerOfX_helper("-17", 2, "-17*x^2");
        multiplyByPowerOfX_helper("-17", 3, "-17*x^3");

        multiplyByPowerOfX_helper("x^2-4*x+7", 0, "x^2-4*x+7");
        multiplyByPowerOfX_helper("x^2-4*x+7", 1, "x^3-4*x^2+7*x");
        multiplyByPowerOfX_helper("x^2-4*x+7", 2, "x^4-4*x^3+7*x^2");
        multiplyByPowerOfX_helper("x^2-4*x+7", 3, "x^5-4*x^4+7*x^3");

        multiplyByPowerOfX_helper("-x^3-1", 0, "-x^3-1");
        multiplyByPowerOfX_helper("-x^3-1", 1, "-x^4-x");
        multiplyByPowerOfX_helper("-x^3-1", 2, "-x^5-x^2");
        multiplyByPowerOfX_helper("-x^3-1", 3, "-x^6-x^3");

        multiplyByPowerOfX_helper("3*x^10", 0, "3*x^10");
        multiplyByPowerOfX_helper("3*x^10", 1, "3*x^11");
        multiplyByPowerOfX_helper("3*x^10", 2, "3*x^12");
        multiplyByPowerOfX_helper("3*x^10", 3, "3*x^13");

        multiplyByPowerOfX_fail_helper("3*x^10", -1);
        multiplyByPowerOfX_fail_helper("0", -1);
        multiplyByPowerOfX_fail_helper("1", -1);
    }

    private static void add_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().add(readStrict(b).get()), output);
    }

    @Test
    public void testAdd() {
        add_helper("0", "0", "0");
        add_helper("0", "1", "1");
        add_helper("0", "x", "x");
        add_helper("0", "-17", "-17");
        add_helper("0", "x^2-4*x+7", "x^2-4*x+7");
        add_helper("0", "-x^3-1", "-x^3-1");
        add_helper("0", "3*x^10", "3*x^10");

        add_helper("1", "0", "1");
        add_helper("1", "1", "2");
        add_helper("1", "x", "x+1");
        add_helper("1", "-17", "-16");
        add_helper("1", "x^2-4*x+7", "x^2-4*x+8");
        add_helper("1", "-x^3-1", "-x^3");
        add_helper("1", "3*x^10", "3*x^10+1");

        add_helper("x", "0", "x");
        add_helper("x", "1", "x+1");
        add_helper("x", "x", "2*x");
        add_helper("x", "-17", "x-17");
        add_helper("x", "x^2-4*x+7", "x^2-3*x+7");
        add_helper("x", "-x^3-1", "-x^3+x-1");
        add_helper("x", "3*x^10", "3*x^10+x");

        add_helper("-17", "0", "-17");
        add_helper("-17", "1", "-16");
        add_helper("-17", "x", "x-17");
        add_helper("-17", "-17", "-34");
        add_helper("-17", "x^2-4*x+7", "x^2-4*x-10");
        add_helper("-17", "-x^3-1", "-x^3-18");
        add_helper("-17", "3*x^10", "3*x^10-17");

        add_helper("x^2-4*x+7", "0", "x^2-4*x+7");
        add_helper("x^2-4*x+7", "1", "x^2-4*x+8");
        add_helper("x^2-4*x+7", "x", "x^2-3*x+7");
        add_helper("x^2-4*x+7", "-17", "x^2-4*x-10");
        add_helper("x^2-4*x+7", "x^2-4*x+7", "2*x^2-8*x+14");
        add_helper("x^2-4*x+7", "-x^3-1", "-x^3+x^2-4*x+6");
        add_helper("x^2-4*x+7", "3*x^10", "3*x^10+x^2-4*x+7");

        add_helper("-x^3-1", "0", "-x^3-1");
        add_helper("-x^3-1", "1", "-x^3");
        add_helper("-x^3-1", "x", "-x^3+x-1");
        add_helper("-x^3-1", "-17", "-x^3-18");
        add_helper("-x^3-1", "x^2-4*x+7", "-x^3+x^2-4*x+6");
        add_helper("-x^3-1", "-x^3-1", "-2*x^3-2");
        add_helper("-x^3-1", "3*x^10", "3*x^10-x^3-1");

        add_helper("3*x^10", "0", "3*x^10");
        add_helper("3*x^10", "1", "3*x^10+1");
        add_helper("3*x^10", "x", "3*x^10+x");
        add_helper("3*x^10", "-17", "3*x^10-17");
        add_helper("3*x^10", "x^2-4*x+7", "3*x^10+x^2-4*x+7");
        add_helper("3*x^10", "-x^3-1", "3*x^10-x^3-1");
        add_helper("3*x^10", "3*x^10", "6*x^10");

        add_helper("x^2-4*x+7", "-x^2+4*x-7", "0");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().negate(), output);
    }

    @Test
    public void testNegate() {
        negate_helper("0", "0");
        negate_helper("1", "-1");
        negate_helper("x", "-x");
        negate_helper("-17", "17");
        negate_helper("x^2-4*x+7", "-x^2+4*x-7");
        negate_helper("-x^3-1", "x^3+1");
        negate_helper("3*x^10", "-3*x^10");
    }

    private static void abs_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().abs(), output);
    }

    @Test
    public void testAbs() {
        abs_helper("0", "0");
        abs_helper("1", "1");
        abs_helper("x", "x");
        abs_helper("-17", "17");
        abs_helper("x^2-4*x+7", "x^2-4*x+7");
        abs_helper("-x^3-1", "x^3+1");
        abs_helper("3*x^10", "3*x^10");
    }

    private static void signum_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().signum(), output);
    }

    @Test
    public void testSignum() {
        signum_helper("0", 0);
        signum_helper("1", 1);
        signum_helper("x", 1);
        signum_helper("-17", -1);
        signum_helper("x^2-4*x+7", 1);
        signum_helper("-x^3-1", -1);
        signum_helper("3*x^10", 1);
    }

    private static void signum_BigInteger_helper(@NotNull String p, @NotNull String x, int output) {
        aeq(readStrict(p).get().signum(Readers.readBigIntegerStrict(x).get()), output);
    }

    @Test
    public void testSignum_BigInteger() {
        signum_BigInteger_helper("0", "0", 0);
        signum_BigInteger_helper("0", "1", 0);
        signum_BigInteger_helper("0", "-1", 0);
        signum_BigInteger_helper("0", "5", 0);
        signum_BigInteger_helper("0", "100", 0);

        signum_BigInteger_helper("1", "0", 1);
        signum_BigInteger_helper("1", "1", 1);
        signum_BigInteger_helper("1", "-1", 1);
        signum_BigInteger_helper("1", "5", 1);
        signum_BigInteger_helper("1", "100", 1);

        signum_BigInteger_helper("x", "0", 0);
        signum_BigInteger_helper("x", "1", 1);
        signum_BigInteger_helper("x", "-1", -1);
        signum_BigInteger_helper("x", "5", 1);
        signum_BigInteger_helper("x", "100", 1);

        signum_BigInteger_helper("-17", "0", -1);
        signum_BigInteger_helper("-17", "1", -1);
        signum_BigInteger_helper("-17", "-1", -1);
        signum_BigInteger_helper("-17", "5", -1);
        signum_BigInteger_helper("-17", "100", -1);

        signum_BigInteger_helper("x^2-4*x+7", "0", 1);
        signum_BigInteger_helper("x^2-4*x+7", "1", 1);
        signum_BigInteger_helper("x^2-4*x+7", "-1", 1);
        signum_BigInteger_helper("x^2-4*x+7", "5", 1);
        signum_BigInteger_helper("x^2-4*x+7", "100", 1);

        signum_BigInteger_helper("x^3-1", "0", -1);
        signum_BigInteger_helper("x^3-1", "1", 0);
        signum_BigInteger_helper("x^3-1", "-1", -1);
        signum_BigInteger_helper("x^3-1", "5", 1);
        signum_BigInteger_helper("x^3-1", "100", 1);

        signum_BigInteger_helper("3*x^10", "0", 0);
        signum_BigInteger_helper("3*x^10", "1", 1);
        signum_BigInteger_helper("3*x^10", "-1", 1);
        signum_BigInteger_helper("3*x^10", "5", 1);
        signum_BigInteger_helper("3*x^10", "100", 1);
    }

    private static void signum_Rational_helper(@NotNull String p, @NotNull String x, int output) {
        aeq(readStrict(p).get().signum(Rational.readStrict(x).get()), output);
    }

    @Test
    public void testSignum_Rational() {
        signum_Rational_helper("0", "0", 0);
        signum_Rational_helper("0", "1", 0);
        signum_Rational_helper("0", "-1", 0);
        signum_Rational_helper("0", "4/5", 0);
        signum_Rational_helper("0", "100", 0);

        signum_Rational_helper("1", "0", 1);
        signum_Rational_helper("1", "1", 1);
        signum_Rational_helper("1", "-1", 1);
        signum_Rational_helper("1", "4/5", 1);
        signum_Rational_helper("1", "100", 1);

        signum_Rational_helper("x", "0", 0);
        signum_Rational_helper("x", "1", 1);
        signum_Rational_helper("x", "-1", -1);
        signum_Rational_helper("x", "4/5", 1);
        signum_Rational_helper("x", "100", 1);

        signum_Rational_helper("-17", "0", -1);
        signum_Rational_helper("-17", "1", -1);
        signum_Rational_helper("-17", "-1", -1);
        signum_Rational_helper("-17", "4/5", -1);
        signum_Rational_helper("-17", "100", -1);

        signum_Rational_helper("x^2-4*x+7", "0", 1);
        signum_Rational_helper("x^2-4*x+7", "1", 1);
        signum_Rational_helper("x^2-4*x+7", "-1", 1);
        signum_Rational_helper("x^2-4*x+7", "4/5", 1);
        signum_Rational_helper("x^2-4*x+7", "100", 1);

        signum_Rational_helper("x^3-1", "0", -1);
        signum_Rational_helper("x^3-1", "1", 0);
        signum_Rational_helper("x^3-1", "-1", -1);
        signum_Rational_helper("x^3-1", "4/5", -1);
        signum_Rational_helper("x^3-1", "100", 1);

        signum_Rational_helper("3*x^10", "0", 0);
        signum_Rational_helper("3*x^10", "1", 1);
        signum_Rational_helper("3*x^10", "-1", 1);
        signum_Rational_helper("3*x^10", "4/5", 1);
        signum_Rational_helper("3*x^10", "100", 1);

        signum_Rational_helper("x^5-8*x^3+10*x+6", "3/2", 1);
        signum_Rational_helper("x^5-8*x^3+10*x+6", "7/4", -1);
    }

    private static void subtract_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().subtract(readStrict(b).get()), output);
    }

    @Test
    public void testSubtract() {
        subtract_helper("0", "0", "0");
        subtract_helper("0", "1", "-1");
        subtract_helper("0", "x", "-x");
        subtract_helper("0", "-17", "17");
        subtract_helper("0", "x^2-4*x+7", "-x^2+4*x-7");
        subtract_helper("0", "-x^3-1", "x^3+1");
        subtract_helper("0", "3*x^10", "-3*x^10");

        subtract_helper("1", "0", "1");
        subtract_helper("1", "1", "0");
        subtract_helper("1", "x", "-x+1");
        subtract_helper("1", "-17", "18");
        subtract_helper("1", "x^2-4*x+7", "-x^2+4*x-6");
        subtract_helper("1", "-x^3-1", "x^3+2");
        subtract_helper("1", "3*x^10", "-3*x^10+1");

        subtract_helper("x", "0", "x");
        subtract_helper("x", "1", "x-1");
        subtract_helper("x", "x", "0");
        subtract_helper("x", "-17", "x+17");
        subtract_helper("x", "x^2-4*x+7", "-x^2+5*x-7");
        subtract_helper("x", "-x^3-1", "x^3+x+1");
        subtract_helper("x", "3*x^10", "-3*x^10+x");

        subtract_helper("-17", "0", "-17");
        subtract_helper("-17", "1", "-18");
        subtract_helper("-17", "x", "-x-17");
        subtract_helper("-17", "-17", "0");
        subtract_helper("-17", "x^2-4*x+7", "-x^2+4*x-24");
        subtract_helper("-17", "-x^3-1", "x^3-16");
        subtract_helper("-17", "3*x^10", "-3*x^10-17");

        subtract_helper("x^2-4*x+7", "0", "x^2-4*x+7");
        subtract_helper("x^2-4*x+7", "1", "x^2-4*x+6");
        subtract_helper("x^2-4*x+7", "x", "x^2-5*x+7");
        subtract_helper("x^2-4*x+7", "-17", "x^2-4*x+24");
        subtract_helper("x^2-4*x+7", "x^2-4*x+7", "0");
        subtract_helper("x^2-4*x+7", "-x^3-1", "x^3+x^2-4*x+8");
        subtract_helper("x^2-4*x+7", "3*x^10", "-3*x^10+x^2-4*x+7");

        subtract_helper("-x^3-1", "0", "-x^3-1");
        subtract_helper("-x^3-1", "1", "-x^3-2");
        subtract_helper("-x^3-1", "x", "-x^3-x-1");
        subtract_helper("-x^3-1", "-17", "-x^3+16");
        subtract_helper("-x^3-1", "x^2-4*x+7", "-x^3-x^2+4*x-8");
        subtract_helper("-x^3-1", "-x^3-1", "0");
        subtract_helper("-x^3-1", "3*x^10", "-3*x^10-x^3-1");

        subtract_helper("3*x^10", "0", "3*x^10");
        subtract_helper("3*x^10", "1", "3*x^10-1");
        subtract_helper("3*x^10", "x", "3*x^10-x");
        subtract_helper("3*x^10", "-17", "3*x^10+17");
        subtract_helper("3*x^10", "x^2-4*x+7", "3*x^10-x^2+4*x-7");
        subtract_helper("3*x^10", "-x^3-1", "3*x^10+x^3+1");
        subtract_helper("3*x^10", "3*x^10", "0");
    }

    private static void multiply_Polynomial_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().multiply(readStrict(b).get()), output);
    }

    @Test
    public void testMultiply_Polynomial() {
        multiply_Polynomial_helper("0", "0", "0");
        multiply_Polynomial_helper("0", "1", "0");
        multiply_Polynomial_helper("0", "x", "0");
        multiply_Polynomial_helper("0", "-17", "0");
        multiply_Polynomial_helper("0", "x^2-4*x+7", "0");
        multiply_Polynomial_helper("0", "-x^3-1", "0");
        multiply_Polynomial_helper("0", "3*x^10", "0");

        multiply_Polynomial_helper("1", "0", "0");
        multiply_Polynomial_helper("1", "1", "1");
        multiply_Polynomial_helper("1", "x", "x");
        multiply_Polynomial_helper("1", "-17", "-17");
        multiply_Polynomial_helper("1", "x^2-4*x+7", "x^2-4*x+7");
        multiply_Polynomial_helper("1", "-x^3-1", "-x^3-1");
        multiply_Polynomial_helper("1", "3*x^10", "3*x^10");

        multiply_Polynomial_helper("x", "0", "0");
        multiply_Polynomial_helper("x", "1", "x");
        multiply_Polynomial_helper("x", "x", "x^2");
        multiply_Polynomial_helper("x", "-17", "-17*x");
        multiply_Polynomial_helper("x", "x^2-4*x+7", "x^3-4*x^2+7*x");
        multiply_Polynomial_helper("x", "-x^3-1", "-x^4-x");
        multiply_Polynomial_helper("x", "3*x^10", "3*x^11");

        multiply_Polynomial_helper("-17", "0", "0");
        multiply_Polynomial_helper("-17", "1", "-17");
        multiply_Polynomial_helper("-17", "x", "-17*x");
        multiply_Polynomial_helper("-17", "-17", "289");
        multiply_Polynomial_helper("-17", "x^2-4*x+7", "-17*x^2+68*x-119");
        multiply_Polynomial_helper("-17", "-x^3-1", "17*x^3+17");
        multiply_Polynomial_helper("-17", "3*x^10", "-51*x^10");

        multiply_Polynomial_helper("x^2-4*x+7", "0", "0");
        multiply_Polynomial_helper("x^2-4*x+7", "1", "x^2-4*x+7");
        multiply_Polynomial_helper("x^2-4*x+7", "x", "x^3-4*x^2+7*x");
        multiply_Polynomial_helper("x^2-4*x+7", "-17", "-17*x^2+68*x-119");
        multiply_Polynomial_helper("x^2-4*x+7", "x^2-4*x+7", "x^4-8*x^3+30*x^2-56*x+49");
        multiply_Polynomial_helper("x^2-4*x+7", "-x^3-1", "-x^5+4*x^4-7*x^3-x^2+4*x-7");
        multiply_Polynomial_helper("x^2-4*x+7", "3*x^10", "3*x^12-12*x^11+21*x^10");

        multiply_Polynomial_helper("-x^3-1", "0", "0");
        multiply_Polynomial_helper("-x^3-1", "1", "-x^3-1");
        multiply_Polynomial_helper("-x^3-1", "x", "-x^4-x");
        multiply_Polynomial_helper("-x^3-1", "-17", "17*x^3+17");
        multiply_Polynomial_helper("-x^3-1", "x^2-4*x+7", "-x^5+4*x^4-7*x^3-x^2+4*x-7");
        multiply_Polynomial_helper("-x^3-1", "-x^3-1", "x^6+2*x^3+1");
        multiply_Polynomial_helper("-x^3-1", "3*x^10", "-3*x^13-3*x^10");

        multiply_Polynomial_helper("3*x^10", "0", "0");
        multiply_Polynomial_helper("3*x^10", "1", "3*x^10");
        multiply_Polynomial_helper("3*x^10", "x", "3*x^11");
        multiply_Polynomial_helper("3*x^10", "-17", "-51*x^10");
        multiply_Polynomial_helper("3*x^10", "x^2-4*x+7", "3*x^12-12*x^11+21*x^10");
        multiply_Polynomial_helper("3*x^10", "-x^3-1", "-3*x^13-3*x^10");
        multiply_Polynomial_helper("3*x^10", "3*x^10", "9*x^20");
    }

    private static void multiply_BigInteger_helper(@NotNull String p, @NotNull String i, @NotNull String output) {
        aeq(readStrict(p).get().multiply(Readers.readBigIntegerStrict(i).get()), output);
    }

    @Test
    public void testMultiply_BigInteger() {
        multiply_BigInteger_helper("0", "0", "0");
        multiply_BigInteger_helper("0", "1", "0");
        multiply_BigInteger_helper("0", "-3", "0");
        multiply_BigInteger_helper("0", "4", "0");

        multiply_BigInteger_helper("1", "0", "0");
        multiply_BigInteger_helper("1", "1", "1");
        multiply_BigInteger_helper("1", "-3", "-3");
        multiply_BigInteger_helper("1", "4", "4");

        multiply_BigInteger_helper("x", "0", "0");
        multiply_BigInteger_helper("x", "1", "x");
        multiply_BigInteger_helper("x", "-3", "-3*x");
        multiply_BigInteger_helper("x", "4", "4*x");

        multiply_BigInteger_helper("-17", "0", "0");
        multiply_BigInteger_helper("-17", "1", "-17");
        multiply_BigInteger_helper("-17", "-3", "51");
        multiply_BigInteger_helper("-17", "4", "-68");

        multiply_BigInteger_helper("x^2-4*x+7", "0", "0");
        multiply_BigInteger_helper("x^2-4*x+7", "1", "x^2-4*x+7");
        multiply_BigInteger_helper("x^2-4*x+7", "-3", "-3*x^2+12*x-21");
        multiply_BigInteger_helper("x^2-4*x+7", "4", "4*x^2-16*x+28");

        multiply_BigInteger_helper("-x^3-1", "0", "0");
        multiply_BigInteger_helper("-x^3-1", "1", "-x^3-1");
        multiply_BigInteger_helper("-x^3-1", "-3", "3*x^3+3");
        multiply_BigInteger_helper("-x^3-1", "4", "-4*x^3-4");

        multiply_BigInteger_helper("3*x^10", "0", "0");
        multiply_BigInteger_helper("3*x^10", "1", "3*x^10");
        multiply_BigInteger_helper("3*x^10", "-3", "-9*x^10");
        multiply_BigInteger_helper("3*x^10", "4", "12*x^10");

        multiply_BigInteger_helper("-1", "-1", "1");
    }

    private static void multiply_int_helper(@NotNull String p, int i, @NotNull String output) {
        aeq(readStrict(p).get().multiply(i), output);
    }

    @Test
    public void testMultiply_int() {
        multiply_int_helper("0", 0, "0");
        multiply_int_helper("0", 1, "0");
        multiply_int_helper("0", -3, "0");
        multiply_int_helper("0", 4, "0");

        multiply_int_helper("1", 0, "0");
        multiply_int_helper("1", 1, "1");
        multiply_int_helper("1", -3, "-3");
        multiply_int_helper("1", 4, "4");

        multiply_int_helper("x", 0, "0");
        multiply_int_helper("x", 1, "x");
        multiply_int_helper("x", -3, "-3*x");
        multiply_int_helper("x", 4, "4*x");

        multiply_int_helper("-17", 0, "0");
        multiply_int_helper("-17", 1, "-17");
        multiply_int_helper("-17", -3, "51");
        multiply_int_helper("-17", 4, "-68");

        multiply_int_helper("x^2-4*x+7", 0, "0");
        multiply_int_helper("x^2-4*x+7", 1, "x^2-4*x+7");
        multiply_int_helper("x^2-4*x+7", -3, "-3*x^2+12*x-21");
        multiply_int_helper("x^2-4*x+7", 4, "4*x^2-16*x+28");

        multiply_int_helper("-x^3-1", 0, "0");
        multiply_int_helper("-x^3-1", 1, "-x^3-1");
        multiply_int_helper("-x^3-1", -3, "3*x^3+3");
        multiply_int_helper("-x^3-1", 4, "-4*x^3-4");

        multiply_int_helper("3*x^10", 0, "0");
        multiply_int_helper("3*x^10", 1, "3*x^10");
        multiply_int_helper("3*x^10", -3, "-9*x^10");
        multiply_int_helper("3*x^10", 4, "12*x^10");

        multiply_int_helper("-1", -1, "1");
    }

    private static void divideExact_BigInteger_helper(@NotNull String p, @NotNull String i, @NotNull String output) {
        aeq(readStrict(p).get().divideExact(Readers.readBigIntegerStrict(i).get()), output);
    }

    private static void divideExact_BigInteger_fail_helper(@NotNull String p, @NotNull String i) {
        try {
            readStrict(p).get().divideExact(Readers.readBigIntegerStrict(i).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivideExact_BigInteger() {
        divideExact_BigInteger_helper("0", "1", "0");
        divideExact_BigInteger_helper("0", "-3", "0");
        divideExact_BigInteger_helper("0", "4", "0");
        divideExact_BigInteger_helper("1", "1", "1");
        divideExact_BigInteger_helper("x", "1", "x");
        divideExact_BigInteger_helper("-17", "1", "-17");
        divideExact_BigInteger_helper("x^2-4*x+7", "1", "x^2-4*x+7");
        divideExact_BigInteger_helper("-x^3-1", "1", "-x^3-1");
        divideExact_BigInteger_helper("3*x^10", "1", "3*x^10");
        divideExact_BigInteger_helper("3*x^10", "-3", "-x^10");
        divideExact_BigInteger_helper("-1", "-1", "1");

        divideExact_BigInteger_fail_helper("0", "0");
        divideExact_BigInteger_fail_helper("1", "0");
        divideExact_BigInteger_fail_helper("x", "0");
    }

    private static void divideExact_int_helper(@NotNull String p, int i, @NotNull String output) {
        aeq(readStrict(p).get().divideExact(i), output);
    }

    private static void divideExact_int_fail_helper(@NotNull String p, int i) {
        try {
            readStrict(p).get().divideExact(i);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivideExact_int() {
        divideExact_int_helper("0", 1, "0");
        divideExact_int_helper("0", -3, "0");
        divideExact_int_helper("0", 4, "0");
        divideExact_int_helper("1", 1, "1");
        divideExact_int_helper("x", 1, "x");
        divideExact_int_helper("-17", 1, "-17");
        divideExact_int_helper("x^2-4*x+7", 1, "x^2-4*x+7");
        divideExact_int_helper("-x^3-1", 1, "-x^3-1");
        divideExact_int_helper("3*x^10", 1, "3*x^10");
        divideExact_int_helper("3*x^10", -3, "-x^10");
        divideExact_int_helper("-1", -1, "1");

        divideExact_int_fail_helper("0", 0);
        divideExact_int_fail_helper("1", 0);
        divideExact_int_fail_helper("x", 0);
    }

    private static void shiftLeft_helper(@NotNull String p, int bits, @NotNull String output) {
        aeq(readStrict(p).get().shiftLeft(bits), output);
    }

    private static void shiftLeft_fail_helper(@NotNull String p, int bits) {
        try {
            readStrict(p).get().shiftLeft(bits);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShiftLeft() {
        shiftLeft_helper("0", 0, "0");
        shiftLeft_helper("0", 1, "0");
        shiftLeft_helper("0", 2, "0");
        shiftLeft_helper("0", 3, "0");
        shiftLeft_helper("0", 4, "0");

        shiftLeft_helper("1", 0, "1");
        shiftLeft_helper("1", 1, "2");
        shiftLeft_helper("1", 2, "4");
        shiftLeft_helper("1", 3, "8");
        shiftLeft_helper("1", 4, "16");

        shiftLeft_helper("x", 0, "x");
        shiftLeft_helper("x", 1, "2*x");
        shiftLeft_helper("x", 2, "4*x");
        shiftLeft_helper("x", 3, "8*x");
        shiftLeft_helper("x", 4, "16*x");

        shiftLeft_helper("-17", 0, "-17");
        shiftLeft_helper("-17", 1, "-34");
        shiftLeft_helper("-17", 2, "-68");
        shiftLeft_helper("-17", 3, "-136");
        shiftLeft_helper("-17", 4, "-272");

        shiftLeft_helper("x^2-4*x+7", 0, "x^2-4*x+7");
        shiftLeft_helper("x^2-4*x+7", 1, "2*x^2-8*x+14");
        shiftLeft_helper("x^2-4*x+7", 2, "4*x^2-16*x+28");
        shiftLeft_helper("x^2-4*x+7", 3, "8*x^2-32*x+56");
        shiftLeft_helper("x^2-4*x+7", 4, "16*x^2-64*x+112");

        shiftLeft_helper("-x^3-1", 0, "-x^3-1");
        shiftLeft_helper("-x^3-1", 1, "-2*x^3-2");
        shiftLeft_helper("-x^3-1", 2, "-4*x^3-4");
        shiftLeft_helper("-x^3-1", 3, "-8*x^3-8");
        shiftLeft_helper("-x^3-1", 4, "-16*x^3-16");

        shiftLeft_helper("3*x^10", 0, "3*x^10");
        shiftLeft_helper("3*x^10", 1, "6*x^10");
        shiftLeft_helper("3*x^10", 2, "12*x^10");
        shiftLeft_helper("3*x^10", 3, "24*x^10");
        shiftLeft_helper("3*x^10", 4, "48*x^10");

        shiftLeft_fail_helper("0", -1);
        shiftLeft_fail_helper("1", -1);
        shiftLeft_fail_helper("-17", -1);
    }

    private static void sum_helper(@NotNull String input, @NotNull String output) {
        aeq(sum(readPolynomialList(input)), output);
    }

    private static void sum_fail_helper(@NotNull String input) {
        try {
            sum(readPolynomialListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testSum() {
        sum_helper("[]", "0");
        sum_helper("[1]", "1");
        sum_helper("[-17]", "-17");
        sum_helper("[-17, x^2-4*x+7, -x^3-1, 3*x^10]", "3*x^10-x^3+x^2-4*x-11");
        sum_fail_helper("[-17, null, -x^3-1, 3*x^10]");
    }

    private static void product_helper(@NotNull String input, @NotNull String output) {
        aeq(product(readPolynomialList(input)), output);
    }

    private static void product_fail_helper(@NotNull String input) {
        try {
            product(readPolynomialListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProduct() {
        product_helper("[]", "1");
        product_helper("[0]", "0");
        product_helper("[-17]", "-17");
        product_helper("[-17, x^2-4*x+7, -x^3-1, 3*x^10]", "51*x^15-204*x^14+357*x^13+51*x^12-204*x^11+357*x^10");
        product_fail_helper("[-17, null, -x^3-1, 3*x^10]");
    }

    private static void delta_helper(@NotNull Iterable<Polynomial> input, @NotNull String output) {
        aeqitLimit(TINY_LIMIT, delta(input), output);
    }

    private static void delta_helper(@NotNull String input, @NotNull String output) {
        delta_helper(readPolynomialList(input), output);
    }

    private static void delta_fail_helper(@NotNull String input) {
        try {
            toList(delta(readPolynomialListWithNulls(input)));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testDelta() {
        delta_helper("[-17]", "[]");
        delta_helper("[-17, x^2-4*x+7]", "[x^2-4*x+24]");
        delta_helper("[-17, x^2-4*x+7, -x^3-1, 3*x^10]", "[x^2-4*x+24, -x^3-x^2+4*x-8, 3*x^10+x^3+1]");
        Polynomial seed = readStrict("x+1").get();
        delta_helper(map(seed::pow, rangeUp(0)),
                "[x, x^2+x, x^3+2*x^2+x, x^4+3*x^3+3*x^2+x, x^5+4*x^4+6*x^3+4*x^2+x," +
                " x^6+5*x^5+10*x^4+10*x^3+5*x^2+x, x^7+6*x^6+15*x^5+20*x^4+15*x^3+6*x^2+x," +
                " x^8+7*x^7+21*x^6+35*x^5+35*x^4+21*x^3+7*x^2+x," +
                " x^9+8*x^8+28*x^7+56*x^6+70*x^5+56*x^4+28*x^3+8*x^2+x," +
                " x^10+9*x^9+36*x^8+84*x^7+126*x^6+126*x^5+84*x^4+36*x^3+9*x^2+x," +
                " x^11+10*x^10+45*x^9+120*x^8+210*x^7+252*x^6+210*x^5+120*x^4+45*x^3+10*x^2+x," +
                " x^12+11*x^11+55*x^10+165*x^9+330*x^8+462*x^7+462*x^6+330*x^5+165*x^4+55*x^3+11*x^2+x," +
                " x^13+12*x^12+66*x^11+220*x^10+495*x^9+792*x^8+924*x^7+792*x^6+495*x^5+220*x^4+66*x^3+12*x^2+x," +
                " x^14+13*x^13+78*x^12+286*x^11+715*x^10+1287*x^9+1716*x^8+1716*x^7+1287*x^6+715*x^5+286*x^4+78*x^3+" +
                "13*x^2+x, " +
                "x^15+14*x^14+91*x^13+364*x^12+1001*x^11+2002*x^10+3003*x^9+3432*x^8+3003*x^7+2002*x^6+1001*x^5+" +
                "364*x^4+91*x^3+14*x^2+x," +
                " x^16+15*x^15+105*x^14+455*x^13+1365*x^12+3003*x^11+5005*x^10+6435*x^9+6435*x^8+5005*x^7+3003*x^6+" +
                "1365*x^5+455*x^4+105*x^3+15*x^2+x," +
                " x^17+16*x^16+120*x^15+560*x^14+1820*x^13+4368*x^12+8008*x^11+11440*x^10+12870*x^9+11440*x^8+" +
                "8008*x^7+4368*x^6+1820*x^5+560*x^4+120*x^3+16*x^2+x," +
                " x^18+17*x^17+136*x^16+680*x^15+2380*x^14+6188*x^13+12376*x^12+19448*x^11+24310*x^10+24310*x^9+" +
                "19448*x^8+12376*x^7+6188*x^6+2380*x^5+680*x^4+136*x^3+17*x^2+x," +
                " x^19+18*x^18+153*x^17+816*x^16+3060*x^15+8568*x^14+18564*x^13+31824*x^12+43758*x^11+48620*x^10+" +
                "43758*x^9+31824*x^8+18564*x^7+8568*x^6+3060*x^5+816*x^4+153*x^3+18*x^2+x," +
                " x^20+19*x^19+171*x^18+969*x^17+3876*x^16+11628*x^15+27132*x^14+50388*x^13+75582*x^12+92378*x^11+" +
                "92378*x^10+75582*x^9+50388*x^8+27132*x^7+11628*x^6+3876*x^5+969*x^4+171*x^3+19*x^2+x, ...]");
        delta_fail_helper("[]");
        delta_fail_helper("[-17, null, -x^3-1, 3*x^10]");
    }

    private static void pow_helper(@NotNull String p, int exponent, @NotNull String output) {
        aeq(readStrict(p).get().pow(exponent), output);
    }

    private static void pow_fail_helper(@NotNull String p, int exponent) {
        try {
            readStrict(p).get().pow(exponent);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPow() {
        pow_helper("x+1", 0, "1");
        pow_helper("x+1", 1, "x+1");
        pow_helper("x+1", 2, "x^2+2*x+1");
        pow_helper("x+1", 3, "x^3+3*x^2+3*x+1");
        pow_helper("x+1", 4, "x^4+4*x^3+6*x^2+4*x+1");
        pow_helper("x+1", 10, "x^10+10*x^9+45*x^8+120*x^7+210*x^6+252*x^5+210*x^4+120*x^3+45*x^2+10*x+1");

        pow_helper("0", 0, "1");
        pow_helper("0", 1, "0");
        pow_helper("0", 2, "0");
        pow_helper("0", 3, "0");

        pow_helper("1", 0, "1");
        pow_helper("1", 1, "1");
        pow_helper("1", 2, "1");
        pow_helper("1", 3, "1");

        pow_helper("-17", 0, "1");
        pow_helper("-17", 1, "-17");
        pow_helper("-17", 2, "289");
        pow_helper("-17", 3, "-4913");

        pow_helper("x^2-4*x+7", 0, "1");
        pow_helper("x^2-4*x+7", 1, "x^2-4*x+7");
        pow_helper("x^2-4*x+7", 2, "x^4-8*x^3+30*x^2-56*x+49");
        pow_helper("x^2-4*x+7", 3, "x^6-12*x^5+69*x^4-232*x^3+483*x^2-588*x+343");

        pow_helper("-x^3-1", 0, "1");
        pow_helper("-x^3-1", 1, "-x^3-1");
        pow_helper("-x^3-1", 2, "x^6+2*x^3+1");
        pow_helper("-x^3-1", 3, "-x^9-3*x^6-3*x^3-1");

        pow_helper("3*x^10", 0, "1");
        pow_helper("3*x^10", 1, "3*x^10");
        pow_helper("3*x^10", 2, "9*x^20");
        pow_helper("3*x^10", 3, "27*x^30");

        pow_fail_helper("3*x^10", -1);
        pow_fail_helper("0", -1);
        pow_fail_helper("1", -1);
    }

    private static void substitute_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().substitute(readStrict(b).get()), output);
    }

    @Test
    public void testSubstitute() {
        substitute_helper("0", "0", "0");
        substitute_helper("0", "1", "0");
        substitute_helper("0", "x", "0");
        substitute_helper("0", "-17", "0");
        substitute_helper("0", "x^2-4*x+7", "0");
        substitute_helper("0", "-x^3-1", "0");
        substitute_helper("0", "3*x^10", "0");

        substitute_helper("1", "0", "1");
        substitute_helper("1", "1", "1");
        substitute_helper("1", "x", "1");
        substitute_helper("1", "-17", "1");
        substitute_helper("1", "x^2-4*x+7", "1");
        substitute_helper("1", "-x^3-1", "1");
        substitute_helper("1", "3*x^10", "1");

        substitute_helper("x", "0", "0");
        substitute_helper("x", "1", "1");
        substitute_helper("x", "x", "x");
        substitute_helper("x", "-17", "-17");
        substitute_helper("x", "x^2-4*x+7", "x^2-4*x+7");
        substitute_helper("x", "-x^3-1", "-x^3-1");
        substitute_helper("x", "3*x^10", "3*x^10");

        substitute_helper("-17", "0", "-17");
        substitute_helper("-17", "1", "-17");
        substitute_helper("-17", "x", "-17");
        substitute_helper("-17", "-17", "-17");
        substitute_helper("-17", "x^2-4*x+7", "-17");
        substitute_helper("-17", "-x^3-1", "-17");
        substitute_helper("-17", "3*x^10", "-17");

        substitute_helper("x^2-4*x+7", "0", "7");
        substitute_helper("x^2-4*x+7", "1", "4");
        substitute_helper("x^2-4*x+7", "x", "x^2-4*x+7");
        substitute_helper("x^2-4*x+7", "-17", "364");
        substitute_helper("x^2-4*x+7", "x^2-4*x+7", "x^4-8*x^3+26*x^2-40*x+28");
        substitute_helper("x^2-4*x+7", "-x^3-1", "x^6+6*x^3+12");
        substitute_helper("x^2-4*x+7", "3*x^10", "9*x^20-12*x^10+7");

        substitute_helper("-x^3-1", "0", "-1");
        substitute_helper("-x^3-1", "1", "-2");
        substitute_helper("-x^3-1", "x", "-x^3-1");
        substitute_helper("-x^3-1", "-17", "4912");
        substitute_helper("-x^3-1", "x^2-4*x+7", "-x^6+12*x^5-69*x^4+232*x^3-483*x^2+588*x-344");
        substitute_helper("-x^3-1", "-x^3-1", "x^9+3*x^6+3*x^3");
        substitute_helper("-x^3-1", "3*x^10", "-27*x^30-1");

        substitute_helper("3*x^10", "0", "0");
        substitute_helper("3*x^10", "1", "3");
        substitute_helper("3*x^10", "x", "3*x^10");
        substitute_helper("3*x^10", "-17", "6047981701347");
        substitute_helper("3*x^10", "x^2-4*x+7",
                "3*x^20-120*x^19+2370*x^18-30600*x^17+288855*x^16-2114784*x^15+12441240*x^14-60158880*x^13" +
                "+242643510*x^12-823956240*x^11+2367787980*x^10-5767693680*x^9+11889531990*x^8-20634495840*x^7" +
                "+29871417240*x^6-35543174688*x^5+33983501895*x^4-25200415800*x^3+13662578370*x^2-4842432840*x" +
                "+847425747");
        substitute_helper("3*x^10", "-x^3-1",
                "3*x^30+30*x^27+135*x^24+360*x^21+630*x^18+756*x^15+630*x^12+360*x^9+135*x^6+30*x^3+3");
        substitute_helper("3*x^10", "3*x^10", "177147*x^100");
    }

    private static void differentiate_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().differentiate(), output);
    }

    @Test
    public void testDifferentiate() {
        differentiate_helper("0", "0");
        differentiate_helper("1", "0");
        differentiate_helper("-17", "0");
        differentiate_helper("x", "1");
        differentiate_helper("x^2-4*x+7", "2*x-4");
        differentiate_helper("3*x^10", "30*x^9");
    }

    private static void isMonic_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isMonic(), output);
    }

    @Test
    public void testIsMonic() {
        isMonic_helper("0", false);
        isMonic_helper("1", true);
        isMonic_helper("x", true);
        isMonic_helper("-17", false);
        isMonic_helper("x^2-4*x+7", true);
        isMonic_helper("-x^3-1", false);
        isMonic_helper("3*x^10", false);
    }

    private static void isPrimitive_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isPrimitive(), output);
    }

    @Test
    public void testIsPrimitive() {
        isPrimitive_helper("0", false);
        isPrimitive_helper("1", true);
        isPrimitive_helper("x", true);
        isPrimitive_helper("-17", false);
        isPrimitive_helper("x^2-4*x+7", true);
        isPrimitive_helper("6*x^2-4*x+8", false);
        isPrimitive_helper("-x^3-1", true);
        isPrimitive_helper("3*x^10", false);
    }

    private static void contentAndPrimitive_helper(
            @NotNull String input,
            @NotNull String constant,
            @NotNull String polynomial
    ) {
        Pair<BigInteger, Polynomial> result = readStrict(input).get().contentAndPrimitive();
        aeq(result.a, constant);
        aeq(result.b, polynomial);
    }

    private static void contentAndPrimitive_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().contentAndPrimitive();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testContentAndPrimitive() {
        contentAndPrimitive_helper("1", "1", "1");
        contentAndPrimitive_helper("x", "1", "x");
        contentAndPrimitive_helper("-17", "17", "-1");
        contentAndPrimitive_helper("x^2-4*x+7", "1", "x^2-4*x+7");
        contentAndPrimitive_helper("6*x^2-4*x+8", "2", "3*x^2-2*x+4");
        contentAndPrimitive_helper("-x^3-1", "1", "-x^3-1");
        contentAndPrimitive_helper("3*x^10", "3", "x^10");
        contentAndPrimitive_fail_helper("0");
    }

    private static void constantFactor_helper(
            @NotNull String input,
            @NotNull String constant,
            @NotNull String polynomial
    ) {
        Pair<BigInteger, Polynomial> result = readStrict(input).get().constantFactor();
        aeq(result.a, constant);
        aeq(result.b, polynomial);
    }

    private static void constantFactor_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().constantFactor();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testConstantFactor() {
        constantFactor_helper("1", "1", "1");
        constantFactor_helper("x", "1", "x");
        constantFactor_helper("-17", "-17", "1");
        constantFactor_helper("x^2-4*x+7", "1", "x^2-4*x+7");
        constantFactor_helper("6*x^2-4*x+8", "2", "3*x^2-2*x+4");
        constantFactor_helper("-x^3-1", "-1", "x^3+1");
        constantFactor_helper("3*x^10", "3", "x^10");
        constantFactor_fail_helper("0");
    }

    private static void pseudoDivide_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String pseudoQuotient,
            @NotNull String pseudoRemainder
    ) {
        Pair<Polynomial, Polynomial> result = readStrict(a).get().pseudoDivide(readStrict(b).get());
        aeq(result.a, pseudoQuotient);
        aeq(result.b, pseudoRemainder);
    }

    private static void pseudoDivide_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().pseudoDivide(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPseudoDivide() {
        pseudoDivide_helper("1", "1", "1", "0");
        pseudoDivide_helper("1", "-17", "1", "0");

        pseudoDivide_helper("x", "1", "x", "0");
        pseudoDivide_helper("x", "x", "1", "0");
        pseudoDivide_helper("x", "-17", "-17*x", "0");

        pseudoDivide_helper("-17", "1", "-17", "0");
        pseudoDivide_helper("-17", "-17", "-17", "0");

        pseudoDivide_helper("x^2-4*x+7", "1", "x^2-4*x+7", "0");
        pseudoDivide_helper("x^2-4*x+7", "x", "x-4", "7");
        pseudoDivide_helper("x^2-4*x+7", "-17", "289*x^2-1156*x+2023", "0");
        pseudoDivide_helper("x^2-4*x+7", "x^2-4*x+7", "1", "0");

        pseudoDivide_helper("-x^3-1", "1", "-x^3-1", "0");
        pseudoDivide_helper("-x^3-1", "x", "-x^2", "-1");
        pseudoDivide_helper("-x^3-1", "-17", "4913*x^3+4913", "0");
        pseudoDivide_helper("-x^3-1", "x^2-4*x+7", "-x-4", "-9*x+27");
        pseudoDivide_helper("-x^3-1", "-x^3-1", "-1", "0");

        pseudoDivide_helper("3*x^10", "1", "3*x^10", "0");
        pseudoDivide_helper("3*x^10", "x", "3*x^9", "0");
        pseudoDivide_helper("3*x^10", "-17", "6047981701347*x^10", "0");
        pseudoDivide_helper(
                "3*x^10",
                "x^2-4*x+7",
                "3*x^8+12*x^7+27*x^6+24*x^5-93*x^4-540*x^3-1509*x^2-2256*x+1539",
                "21948*x-10773"
        );
        pseudoDivide_helper("3*x^10", "-x^3-1", "-3*x^7+3*x^4-3*x", "-3*x");
        pseudoDivide_helper("3*x^10", "3*x^10", "3", "0");

        pseudoDivide_helper(
                "x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5",
                "3*x^6+5*x^4-4*x^2-9*x+21",
                "9*x^2-6",
                "-15*x^4+3*x^2-9"
        );
        pseudoDivide_helper("x^3+x+1", "3*x^2+x+1", "3*x-1", "7*x+10");
        pseudoDivide_helper("x+1", "x-1", "1", "2");
        pseudoDivide_helper("x", "x+1", "1", "-1");
        pseudoDivide_helper("2*x+1", "x", "2", "1");

        pseudoDivide_fail_helper("x", "0");
        pseudoDivide_fail_helper("0", "x");
        pseudoDivide_fail_helper("x^2", "x^3");
    }

    private static void pseudoRemainder_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        aeq(readStrict(a).get().pseudoRemainder(readStrict(b).get()), output);
    }

    private static void pseudoRemainder_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().pseudoRemainder(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPseudoRemainder() {
        pseudoRemainder_helper("1", "1", "0");
        pseudoRemainder_helper("1", "-17", "0");

        pseudoRemainder_helper("x", "1", "0");
        pseudoRemainder_helper("x", "x", "0");
        pseudoRemainder_helper("x", "-17", "0");

        pseudoRemainder_helper("-17", "1", "0");
        pseudoRemainder_helper("-17", "-17", "0");

        pseudoRemainder_helper("x^2-4*x+7", "1", "0");
        pseudoRemainder_helper("x^2-4*x+7", "x", "7");
        pseudoRemainder_helper("x^2-4*x+7", "-17", "0");
        pseudoRemainder_helper("x^2-4*x+7", "x^2-4*x+7", "0");

        pseudoRemainder_helper("-x^3-1", "1", "0");
        pseudoRemainder_helper("-x^3-1", "x", "-1");
        pseudoRemainder_helper("-x^3-1", "-17", "0");
        pseudoRemainder_helper("-x^3-1", "x^2-4*x+7", "-9*x+27");
        pseudoRemainder_helper("-x^3-1", "-x^3-1", "0");

        pseudoRemainder_helper("3*x^10", "1", "0");
        pseudoRemainder_helper("3*x^10", "x", "0");
        pseudoRemainder_helper("3*x^10", "-17", "0");
        pseudoRemainder_helper("3*x^10", "x^2-4*x+7", "21948*x-10773");
        pseudoRemainder_helper("3*x^10", "-x^3-1", "-3*x");
        pseudoRemainder_helper("3*x^10", "3*x^10", "0");

        pseudoRemainder_helper("x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5", "3*x^6+5*x^4-4*x^2-9*x+21", "-15*x^4+3*x^2-9");
        pseudoRemainder_helper("x^3+x+1", "3*x^2+x+1", "7*x+10");
        pseudoRemainder_helper("x+1", "x-1", "2");
        pseudoRemainder_helper("x", "x+1", "-1");
        pseudoRemainder_helper("2*x+1", "x", "1");

        pseudoRemainder_fail_helper("x", "0");
        pseudoRemainder_fail_helper("0", "x");
        pseudoRemainder_fail_helper("x^2", "x^3");
    }

    private static void absolutePseudoDivide_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String pseudoQuotient,
            @NotNull String pseudoRemainder
    ) {
        Pair<Polynomial, Polynomial> result = readStrict(a).get().absolutePseudoDivide(readStrict(b).get());
        aeq(result.a, pseudoQuotient);
        aeq(result.b, pseudoRemainder);
    }

    private static void absolutePseudoDivide_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().absolutePseudoDivide(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testAbsolutePseudoDivide() {
        absolutePseudoDivide_helper("1", "1", "1", "0");
        absolutePseudoDivide_helper("1", "-17", "-1", "0");

        absolutePseudoDivide_helper("x", "1", "x", "0");
        absolutePseudoDivide_helper("x", "x", "1", "0");
        absolutePseudoDivide_helper("x", "-17", "-17*x", "0");

        absolutePseudoDivide_helper("-17", "1", "-17", "0");
        absolutePseudoDivide_helper("-17", "-17", "17", "0");

        absolutePseudoDivide_helper("x^2-4*x+7", "1", "x^2-4*x+7", "0");
        absolutePseudoDivide_helper("x^2-4*x+7", "x", "x-4", "7");
        absolutePseudoDivide_helper("x^2-4*x+7", "-17", "-289*x^2+1156*x-2023", "0");
        absolutePseudoDivide_helper("x^2-4*x+7", "x^2-4*x+7", "1", "0");

        absolutePseudoDivide_helper("-x^3-1", "1", "-x^3-1", "0");
        absolutePseudoDivide_helper("-x^3-1", "x", "-x^2", "-1");
        absolutePseudoDivide_helper("-x^3-1", "-17", "4913*x^3+4913", "0");
        absolutePseudoDivide_helper("-x^3-1", "x^2-4*x+7", "-x-4", "-9*x+27");
        absolutePseudoDivide_helper("-x^3-1", "-x^3-1", "1", "0");

        absolutePseudoDivide_helper("3*x^10", "1", "3*x^10", "0");
        absolutePseudoDivide_helper("3*x^10", "x", "3*x^9", "0");
        absolutePseudoDivide_helper("3*x^10", "-17", "-6047981701347*x^10", "0");
        absolutePseudoDivide_helper(
                "3*x^10",
                "x^2-4*x+7",
                "3*x^8+12*x^7+27*x^6+24*x^5-93*x^4-540*x^3-1509*x^2-2256*x+1539",
                "21948*x-10773"
        );
        absolutePseudoDivide_helper("3*x^10", "-x^3-1", "-3*x^7+3*x^4-3*x", "-3*x");
        absolutePseudoDivide_helper("3*x^10", "3*x^10", "3", "0");

        absolutePseudoDivide_helper(
                "x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5",
                "3*x^6+5*x^4-4*x^2-9*x+21",
                "9*x^2-6",
                "-15*x^4+3*x^2-9"
        );
        absolutePseudoDivide_helper("x^3+x+1", "3*x^2+x+1", "3*x-1", "7*x+10");
        absolutePseudoDivide_helper("x+1", "x-1", "1", "2");
        absolutePseudoDivide_helper("x", "x+1", "1", "-1");
        absolutePseudoDivide_helper("2*x+1", "x", "2", "1");

        absolutePseudoDivide_fail_helper("x", "0");
        absolutePseudoDivide_fail_helper("0", "x");
        absolutePseudoDivide_fail_helper("x^2", "x^3");
    }

    private static void absolutePseudoRemainder_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        aeq(readStrict(a).get().absolutePseudoRemainder(readStrict(b).get()), output);
    }

    private static void absolutePseudoRemainder_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().absolutePseudoRemainder(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testAbsolutePseudoRemainder() {
        absolutePseudoRemainder_helper("1", "1", "0");
        absolutePseudoRemainder_helper("1", "-17", "0");

        absolutePseudoRemainder_helper("x", "1", "0");
        absolutePseudoRemainder_helper("x", "x", "0");
        absolutePseudoRemainder_helper("x", "-17", "0");

        absolutePseudoRemainder_helper("-17", "1", "0");
        absolutePseudoRemainder_helper("-17", "-17", "0");

        absolutePseudoRemainder_helper("x^2-4*x+7", "1", "0");
        absolutePseudoRemainder_helper("x^2-4*x+7", "x", "7");
        absolutePseudoRemainder_helper("x^2-4*x+7", "-17", "0");
        absolutePseudoRemainder_helper("x^2-4*x+7", "x^2-4*x+7", "0");

        absolutePseudoRemainder_helper("-x^3-1", "1", "0");
        absolutePseudoRemainder_helper("-x^3-1", "x", "-1");
        absolutePseudoRemainder_helper("-x^3-1", "-17", "0");
        absolutePseudoRemainder_helper("-x^3-1", "x^2-4*x+7", "-9*x+27");
        absolutePseudoRemainder_helper("-x^3-1", "-x^3-1", "0");

        absolutePseudoRemainder_helper("3*x^10", "1", "0");
        absolutePseudoRemainder_helper("3*x^10", "x", "0");
        absolutePseudoRemainder_helper("3*x^10", "-17", "0");
        absolutePseudoRemainder_helper("3*x^10", "x^2-4*x+7", "21948*x-10773");
        absolutePseudoRemainder_helper("3*x^10", "-x^3-1", "-3*x");
        absolutePseudoRemainder_helper("3*x^10", "3*x^10", "0");

        absolutePseudoRemainder_helper(
                "x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5",
                "3*x^6+5*x^4-4*x^2-9*x+21",
                "-15*x^4+3*x^2-9"
        );
        absolutePseudoRemainder_helper("x^3+x+1", "3*x^2+x+1", "7*x+10");
        absolutePseudoRemainder_helper("x+1", "x-1", "2");
        absolutePseudoRemainder_helper("x", "x+1", "-1");
        absolutePseudoRemainder_helper("2*x+1", "x", "1");

        absolutePseudoRemainder_fail_helper("x", "0");
        absolutePseudoRemainder_fail_helper("0", "x");
        absolutePseudoRemainder_fail_helper("x^2", "x^3");
    }

    private static void evenPseudoDivide_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String pseudoQuotient,
            @NotNull String pseudoRemainder
    ) {
        Pair<Polynomial, Polynomial> result = readStrict(a).get().evenPseudoDivide(readStrict(b).get());
        aeq(result.a, pseudoQuotient);
        aeq(result.b, pseudoRemainder);
    }

    private static void evenPseudoDivide_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().evenPseudoDivide(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testEvenPseudoDivide() {
        evenPseudoDivide_helper("1", "1", "1", "0");
        evenPseudoDivide_helper("1", "-17", "-17", "0");

        evenPseudoDivide_helper("x", "1", "x", "0");
        evenPseudoDivide_helper("x", "x", "1", "0");
        evenPseudoDivide_helper("x", "-17", "-17*x", "0");

        evenPseudoDivide_helper("-17", "1", "-17", "0");
        evenPseudoDivide_helper("-17", "-17", "289", "0");

        evenPseudoDivide_helper("x^2-4*x+7", "1", "x^2-4*x+7", "0");
        evenPseudoDivide_helper("x^2-4*x+7", "x", "x-4", "7");
        evenPseudoDivide_helper("x^2-4*x+7", "-17", "-4913*x^2+19652*x-34391", "0");
        evenPseudoDivide_helper("x^2-4*x+7", "x^2-4*x+7", "1", "0");

        evenPseudoDivide_helper("-x^3-1", "1", "-x^3-1", "0");
        evenPseudoDivide_helper("-x^3-1", "x", "-x^2", "-1");
        evenPseudoDivide_helper("-x^3-1", "-17", "4913*x^3+4913", "0");
        evenPseudoDivide_helper("-x^3-1", "x^2-4*x+7", "-x-4", "-9*x+27");
        evenPseudoDivide_helper("-x^3-1", "-x^3-1", "1", "0");

        evenPseudoDivide_helper("3*x^10", "1", "3*x^10", "0");
        evenPseudoDivide_helper("3*x^10", "x", "3*x^9", "0");
        evenPseudoDivide_helper("3*x^10", "-17", "-102815688922899*x^10", "0");
        evenPseudoDivide_helper(
                "3*x^10",
                "x^2-4*x+7",
                "3*x^8+12*x^7+27*x^6+24*x^5-93*x^4-540*x^3-1509*x^2-2256*x+1539",
                "21948*x-10773"
        );
        evenPseudoDivide_helper("3*x^10", "-x^3-1", "-3*x^7+3*x^4-3*x", "-3*x");
        evenPseudoDivide_helper("3*x^10", "3*x^10", "9", "0");

        evenPseudoDivide_helper(
                "x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5",
                "3*x^6+5*x^4-4*x^2-9*x+21",
                "27*x^2-18",
                "-45*x^4+9*x^2-27"
        );
        evenPseudoDivide_helper("x^3+x+1", "3*x^2+x+1", "3*x-1", "7*x+10");
        evenPseudoDivide_helper("x+1", "x-1", "1", "2");
        evenPseudoDivide_helper("x", "x+1", "1", "-1");
        evenPseudoDivide_helper("2*x+1", "x", "2", "1");

        evenPseudoDivide_fail_helper("x", "0");
        evenPseudoDivide_fail_helper("0", "x");
        evenPseudoDivide_fail_helper("x^2", "x^3");
    }

    private static void evenPseudoRemainder_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        aeq(readStrict(a).get().evenPseudoRemainder(readStrict(b).get()), output);
    }

    private static void evenPseudoRemainder_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().evenPseudoRemainder(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testEvenPseudoRemainder() {
        evenPseudoRemainder_helper("1", "1", "0");
        evenPseudoRemainder_helper("1", "-17", "0");

        evenPseudoRemainder_helper("x", "1", "0");
        evenPseudoRemainder_helper("x", "x", "0");
        evenPseudoRemainder_helper("x", "-17", "0");

        evenPseudoRemainder_helper("-17", "1", "0");
        evenPseudoRemainder_helper("-17", "-17", "0");

        evenPseudoRemainder_helper("x^2-4*x+7", "1", "0");
        evenPseudoRemainder_helper("x^2-4*x+7", "x", "7");
        evenPseudoRemainder_helper("x^2-4*x+7", "-17", "0");
        evenPseudoRemainder_helper("x^2-4*x+7", "x^2-4*x+7", "0");

        evenPseudoRemainder_helper("-x^3-1", "1", "0");
        evenPseudoRemainder_helper("-x^3-1", "x", "-1");
        evenPseudoRemainder_helper("-x^3-1", "-17", "0");
        evenPseudoRemainder_helper("-x^3-1", "x^2-4*x+7", "-9*x+27");
        evenPseudoRemainder_helper("-x^3-1", "-x^3-1", "0");

        evenPseudoRemainder_helper("3*x^10", "1", "0");
        evenPseudoRemainder_helper("3*x^10", "x", "0");
        evenPseudoRemainder_helper("3*x^10", "-17", "0");
        evenPseudoRemainder_helper("3*x^10", "x^2-4*x+7", "21948*x-10773");
        evenPseudoRemainder_helper("3*x^10", "-x^3-1", "-3*x");
        evenPseudoRemainder_helper("3*x^10", "3*x^10", "0");

        evenPseudoRemainder_helper(
                "x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5",
                "3*x^6+5*x^4-4*x^2-9*x+21",
                "-45*x^4+9*x^2-27"
        );
        evenPseudoRemainder_helper("x^3+x+1", "3*x^2+x+1", "7*x+10");
        evenPseudoRemainder_helper("x+1", "x-1", "2");
        evenPseudoRemainder_helper("x", "x+1", "-1");
        evenPseudoRemainder_helper("2*x+1", "x", "1");

        evenPseudoRemainder_fail_helper("x", "0");
        evenPseudoRemainder_fail_helper("0", "x");
        evenPseudoRemainder_fail_helper("x^2", "x^3");
    }

    private static void divisibleBy_helper(@NotNull String a, @NotNull String b, boolean output) {
        aeq(readStrict(a).get().isDivisibleBy(readStrict(b).get()), output);
    }

    private static void divisibleBy_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().isDivisibleBy(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivisibleBy() {
        divisibleBy_helper("1", "1", true);
        divisibleBy_helper("1", "5", true);
        divisibleBy_helper("3", "5", true);
        divisibleBy_helper("0", "1", true);
        divisibleBy_helper("0", "5", true);
        divisibleBy_helper("x", "1", true);
        divisibleBy_helper("x", "5", true);
        divisibleBy_helper("x", "-5", true);
        divisibleBy_helper("-x", "5", true);
        divisibleBy_helper("-x", "-5", true);
        divisibleBy_helper("5", "x", false);
        divisibleBy_helper("5", "-x", false);
        divisibleBy_helper("-5", "x", false);
        divisibleBy_helper("-5", "-x", false);
        divisibleBy_helper("x^2-1", "x+1", true);
        divisibleBy_helper("x^2-1", "x-1", true);
        divisibleBy_helper("x^2-1", "3*x+3", true);
        divisibleBy_helper("-2*x^2+1", "3*x+3", false);
        divisibleBy_helper("x^3", "x^2", true);
        divisibleBy_helper("x^2", "x^3", false);
        divisibleBy_helper("-2*x^3", "5*x^2", true);
        divisibleBy_helper("-x^5+4*x^4-7*x^3-x^2+4*x-7", "-x^3-1", true);
        divisibleBy_helper("-x^5+4*x^4-7*x^3-x^2+4*x-7", "x^2-4*x+7", true);

        divisibleBy_fail_helper("0", "0");
        divisibleBy_fail_helper("-5", "0");
        divisibleBy_fail_helper("x^2", "0");
    }

    private static void divideExact_Polynomial_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().divideExact(readStrict(b).get()), output);
    }

    private static void divideExact_Polynomial_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().divideExact(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivideExact_Polynomial() {
        divideExact_Polynomial_helper("0", "1", "0");
        divideExact_Polynomial_helper("0", "x^2", "0");
        divideExact_Polynomial_helper("6", "3", "2");
        divideExact_Polynomial_helper("6*x-3", "3", "2*x-1");
        divideExact_Polynomial_helper("x^2-1", "x+1", "x-1");
        divideExact_Polynomial_helper("x^2-1", "x-1", "x+1");
        divideExact_Polynomial_helper("6*x^10", "-2*x^3", "-3*x^7");
        divideExact_Polynomial_helper("-x^5+4*x^4-7*x^3-x^2+4*x-7", "x^2-4*x+7", "-x^3-1");
        divideExact_Polynomial_helper("-x^5+4*x^4-7*x^3-x^2+4*x-7", "-x^3-1", "x^2-4*x+7");

        divideExact_Polynomial_fail_helper("0", "0");
        divideExact_Polynomial_fail_helper("1", "0");
        divideExact_Polynomial_fail_helper("2", "3");
        divideExact_Polynomial_fail_helper("6*x-3", "4");
        divideExact_Polynomial_fail_helper("x^5", "x+1");
        divideExact_Polynomial_fail_helper("x^2+2*x+1", "x-1");
    }

    private static void remainderExact_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().remainderExact(readStrict(b).get()), output);
    }

    private static void remainderExact_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().remainderExact(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRemainderExact() {
        remainderExact_helper("0", "1", "0");
        remainderExact_helper("0", "x", "0");
        remainderExact_helper("0", "-17", "0");
        remainderExact_helper("0", "x^2-4*x+7", "0");
        remainderExact_helper("0", "-x^3-1", "0");
        remainderExact_helper("0", "3*x^10", "0");

        remainderExact_helper("1", "1", "0");
        remainderExact_helper("1", "x", "1");
        remainderExact_helper("1", "x^2-4*x+7", "1");
        remainderExact_helper("1", "-x^3-1", "1");
        remainderExact_helper("1", "3*x^10", "1");

        remainderExact_helper("x", "1", "0");
        remainderExact_helper("x", "x", "0");
        remainderExact_helper("x", "x^2-4*x+7", "x");
        remainderExact_helper("x", "-x^3-1", "x");
        remainderExact_helper("x", "3*x^10", "x");

        remainderExact_helper("-17", "1", "0");
        remainderExact_helper("-17", "x", "-17");
        remainderExact_helper("-17", "-17", "0");
        remainderExact_helper("-17", "x^2-4*x+7", "-17");
        remainderExact_helper("-17", "-x^3-1", "-17");
        remainderExact_helper("-17", "3*x^10", "-17");

        remainderExact_helper("x^2-4*x+7", "1", "0");
        remainderExact_helper("x^2-4*x+7", "x", "7");
        remainderExact_helper("x^2-4*x+7", "x^2-4*x+7", "0");
        remainderExact_helper("x^2-4*x+7", "-x^3-1", "x^2-4*x+7");
        remainderExact_helper("x^2-4*x+7", "3*x^10", "x^2-4*x+7");

        remainderExact_helper("-x^3-1", "1", "0");
        remainderExact_helper("-x^3-1", "x", "-1");
        remainderExact_helper("-x^3-1", "x^2-4*x+7", "-9*x+27");
        remainderExact_helper("-x^3-1", "-x^3-1", "0");
        remainderExact_helper("-x^3-1", "3*x^10", "-x^3-1");

        remainderExact_helper("3*x^10", "1", "0");
        remainderExact_helper("3*x^10", "x", "0");
        remainderExact_helper("3*x^10", "x^2-4*x+7", "21948*x-10773");
        remainderExact_helper("3*x^10", "-x^3-1", "-3*x");
        remainderExact_helper("3*x^10", "3*x^10", "0");

        remainderExact_helper("x^5", "x+1", "-1");
        remainderExact_helper("x^2+2*x+1", "x-1", "4");

        remainderExact_fail_helper("0", "0");
        remainderExact_fail_helper("1", "0");
        remainderExact_fail_helper("2", "3");
        remainderExact_fail_helper("6*x-3", "4");
    }

    private static void trivialPseudoRemainderSequence_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        aeq(readStrict(a).get().trivialPseudoRemainderSequence(readStrict(b).get()), output);
    }

    private static void trivialPseudoRemainderSequence_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().trivialPseudoRemainderSequence(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testTrivialPseudoRemainderSequence() {
        trivialPseudoRemainderSequence_helper("1", "0", "[1]");
        trivialPseudoRemainderSequence_helper("1", "1", "[1, 1]");
        trivialPseudoRemainderSequence_helper("1", "-17", "[1, -17]");

        trivialPseudoRemainderSequence_helper("x", "0", "[x]");
        trivialPseudoRemainderSequence_helper("x", "1", "[x, 1]");
        trivialPseudoRemainderSequence_helper("x", "x", "[x, x]");
        trivialPseudoRemainderSequence_helper("x", "-17", "[x, -17]");

        trivialPseudoRemainderSequence_helper("-17", "0", "[-17]");
        trivialPseudoRemainderSequence_helper("-17", "1", "[-17, 1]");
        trivialPseudoRemainderSequence_helper("-17", "-17", "[-17, -17]");

        trivialPseudoRemainderSequence_helper("x^2-4*x+7", "0", "[x^2-4*x+7]");
        trivialPseudoRemainderSequence_helper("x^2-4*x+7", "1", "[x^2-4*x+7, 1]");
        trivialPseudoRemainderSequence_helper("x^2-4*x+7", "x", "[x^2-4*x+7, x, 7]");
        trivialPseudoRemainderSequence_helper("x^2-4*x+7", "-17", "[x^2-4*x+7, -17]");
        trivialPseudoRemainderSequence_helper("x^2-4*x+7", "x^2-4*x+7", "[x^2-4*x+7, x^2-4*x+7]");

        trivialPseudoRemainderSequence_helper("-x^3-1", "0", "[-x^3-1]");
        trivialPseudoRemainderSequence_helper("-x^3-1", "1", "[-x^3-1, 1]");
        trivialPseudoRemainderSequence_helper("-x^3-1", "x", "[-x^3-1, x, -1]");
        trivialPseudoRemainderSequence_helper("-x^3-1", "-17", "[-x^3-1, -17]");
        trivialPseudoRemainderSequence_helper("-x^3-1", "x^2-4*x+7", "[-x^3-1, x^2-4*x+7, -9*x+27, 324]");
        trivialPseudoRemainderSequence_helper("-x^3-1", "-x^3-1", "[-x^3-1, -x^3-1]");

        trivialPseudoRemainderSequence_helper("3*x^10", "0", "[3*x^10]");
        trivialPseudoRemainderSequence_helper("3*x^10", "1", "[3*x^10, 1]");
        trivialPseudoRemainderSequence_helper("3*x^10", "x", "[3*x^10, x]");
        trivialPseudoRemainderSequence_helper("3*x^10", "-17", "[3*x^10, -17]");
        trivialPseudoRemainderSequence_helper("3*x^10", "x^2-4*x+7", "[3*x^10, x^2-4*x+7, 21948*x-10773, 2542277241]");
        trivialPseudoRemainderSequence_helper("3*x^10", "-x^3-1", "[3*x^10, -x^3-1, -3*x, 27]");
        trivialPseudoRemainderSequence_helper("3*x^10", "3*x^10", "[3*x^10, 3*x^10]");

        trivialPseudoRemainderSequence_helper("x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5", "3*x^6+5*x^4-4*x^2-9*x+21",
                "[x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5, 3*x^6+5*x^4-4*x^2-9*x+21, -15*x^4+3*x^2-9," +
                " 15795*x^2+30375*x-59535, 1254542875143750*x-1654608338437500, 12593338795500743100931141992187500]");

        trivialPseudoRemainderSequence_fail_helper("0", "0");
        trivialPseudoRemainderSequence_fail_helper("0", "x");
        trivialPseudoRemainderSequence_fail_helper("x^2", "x^3");
    }

    private static void primitivePseudoRemainderSequence_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        aeq(readStrict(a).get().primitivePseudoRemainderSequence(readStrict(b).get()), output);
    }

    private static void primitivePseudoRemainderSequence_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().primitivePseudoRemainderSequence(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPrimitivePseudoRemainderSequence() {
        primitivePseudoRemainderSequence_helper("1", "0", "[1]");
        primitivePseudoRemainderSequence_helper("1", "1", "[1, 1]");
        primitivePseudoRemainderSequence_helper("1", "-17", "[1, -1]");

        primitivePseudoRemainderSequence_helper("x", "0", "[x]");
        primitivePseudoRemainderSequence_helper("x", "1", "[x, 1]");
        primitivePseudoRemainderSequence_helper("x", "x", "[x, x]");
        primitivePseudoRemainderSequence_helper("x", "-17", "[x, -1]");

        primitivePseudoRemainderSequence_helper("-17", "0", "[-1]");
        primitivePseudoRemainderSequence_helper("-17", "1", "[-1, 1]");
        primitivePseudoRemainderSequence_helper("-17", "-17", "[-1, -1]");

        primitivePseudoRemainderSequence_helper("x^2-4*x+7", "0", "[x^2-4*x+7]");
        primitivePseudoRemainderSequence_helper("x^2-4*x+7", "1", "[x^2-4*x+7, 1]");
        primitivePseudoRemainderSequence_helper("x^2-4*x+7", "x", "[x^2-4*x+7, x, 1]");
        primitivePseudoRemainderSequence_helper("x^2-4*x+7", "-17", "[x^2-4*x+7, -1]");
        primitivePseudoRemainderSequence_helper("x^2-4*x+7", "x^2-4*x+7", "[x^2-4*x+7, x^2-4*x+7]");

        primitivePseudoRemainderSequence_helper("-x^3-1", "0", "[-x^3-1]");
        primitivePseudoRemainderSequence_helper("-x^3-1", "1", "[-x^3-1, 1]");
        primitivePseudoRemainderSequence_helper("-x^3-1", "x", "[-x^3-1, x, -1]");
        primitivePseudoRemainderSequence_helper("-x^3-1", "-17", "[-x^3-1, -1]");
        primitivePseudoRemainderSequence_helper("-x^3-1", "x^2-4*x+7", "[-x^3-1, x^2-4*x+7, -x+3, 1]");
        primitivePseudoRemainderSequence_helper("-x^3-1", "-x^3-1", "[-x^3-1, -x^3-1]");

        primitivePseudoRemainderSequence_helper("3*x^10", "0", "[x^10]");
        primitivePseudoRemainderSequence_helper("3*x^10", "1", "[x^10, 1]");
        primitivePseudoRemainderSequence_helper("3*x^10", "x", "[x^10, x]");
        primitivePseudoRemainderSequence_helper("3*x^10", "-17", "[x^10, -1]");
        primitivePseudoRemainderSequence_helper("3*x^10", "x^2-4*x+7", "[x^10, x^2-4*x+7, 7316*x-3591, 1]");
        primitivePseudoRemainderSequence_helper("3*x^10", "-x^3-1", "[x^10, -x^3-1, -x, 1]");
        primitivePseudoRemainderSequence_helper("3*x^10", "3*x^10", "[x^10, x^10]");

        primitivePseudoRemainderSequence_helper("x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5", "3*x^6+5*x^4-4*x^2-9*x+21",
                "[x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5, 3*x^6+5*x^4-4*x^2-9*x+21, -5*x^4+x^2-3, 13*x^2+25*x-49," +
                " 4663*x-6150, 1]");

        primitivePseudoRemainderSequence_fail_helper("0", "0");
        primitivePseudoRemainderSequence_fail_helper("0", "x");
        primitivePseudoRemainderSequence_fail_helper("x^2", "x^3");
    }

    private static void subresultantSequence_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        aeq(readStrict(a).get().subresultantSequence(readStrict(b).get()), output);
    }

    private static void subresultantSequence_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().subresultantSequence(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSubresultantSequence() {
        subresultantSequence_helper("1", "0", "[1]");
        subresultantSequence_helper("1", "1", "[1, 1]");
        subresultantSequence_helper("1", "-17", "[1, -17]");

        subresultantSequence_helper("x", "0", "[x]");
        subresultantSequence_helper("x", "1", "[x, 1]");
        subresultantSequence_helper("x", "x", "[x, x]");
        subresultantSequence_helper("x", "-17", "[x, -17]");

        subresultantSequence_helper("-17", "0", "[-17]");
        subresultantSequence_helper("-17", "1", "[-17, 1]");
        subresultantSequence_helper("-17", "-17", "[-17, -17]");

        subresultantSequence_helper("x^2-4*x+7", "0", "[x^2-4*x+7]");
        subresultantSequence_helper("x^2-4*x+7", "1", "[x^2-4*x+7, 1]");
        subresultantSequence_helper("x^2-4*x+7", "x", "[x^2-4*x+7, x, 7]");
        subresultantSequence_helper("x^2-4*x+7", "-17", "[x^2-4*x+7, -17]");
        subresultantSequence_helper("x^2-4*x+7", "x^2-4*x+7", "[x^2-4*x+7, x^2-4*x+7]");

        subresultantSequence_helper("-x^3-1", "0", "[-x^3-1]");
        subresultantSequence_helper("-x^3-1", "1", "[-x^3-1, 1]");
        subresultantSequence_helper("-x^3-1", "x", "[-x^3-1, x, -1]");
        subresultantSequence_helper("-x^3-1", "-17", "[-x^3-1, -17]");
        subresultantSequence_helper("-x^3-1", "x^2-4*x+7", "[-x^3-1, x^2-4*x+7, -9*x+27, 324]");
        subresultantSequence_helper("-x^3-1", "-x^3-1", "[-x^3-1, -x^3-1]");

        subresultantSequence_helper("3*x^10", "0", "[3*x^10]");
        subresultantSequence_helper("3*x^10", "1", "[3*x^10, 1]");
        subresultantSequence_helper("3*x^10", "x", "[3*x^10, x]");
        subresultantSequence_helper("3*x^10", "-17", "[3*x^10, -17]");
        subresultantSequence_helper("3*x^10", "x^2-4*x+7", "[3*x^10, x^2-4*x+7, 21948*x-10773, 2542277241]");
        subresultantSequence_helper("3*x^10", "-x^3-1", "[3*x^10, -x^3-1, -3*x, -27]");
        subresultantSequence_helper("3*x^10", "3*x^10", "[3*x^10, 3*x^10]");

        subresultantSequence_helper("x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5", "3*x^6+5*x^4-4*x^2-9*x+21",
                "[x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5, 3*x^6+5*x^4-4*x^2-9*x+21, -15*x^4+3*x^2-9, 65*x^2+125*x-245," +
                " -9326*x+12300, 260708]");

        subresultantSequence_fail_helper("0", "0");
        subresultantSequence_fail_helper("0", "x");
        subresultantSequence_fail_helper("x^2", "x^3");
    }

    private static void gcd_Polynomial_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().gcd(readStrict(b).get()), output);
    }

    private static void gcd_Polynomial_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().gcd(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testGcd_Polynomial() {
        gcd_Polynomial_helper("0", "1", "1");
        gcd_Polynomial_helper("0", "x", "x");
        gcd_Polynomial_helper("0", "-17", "1");
        gcd_Polynomial_helper("0", "x^2-4*x+7", "x^2-4*x+7");
        gcd_Polynomial_helper("0", "-x^3-1", "x^3+1");
        gcd_Polynomial_helper("0", "3*x^10", "x^10");

        gcd_Polynomial_helper("1", "0", "1");
        gcd_Polynomial_helper("1", "1", "1");
        gcd_Polynomial_helper("1", "x", "1");
        gcd_Polynomial_helper("1", "-17", "1");
        gcd_Polynomial_helper("1", "x^2-4*x+7", "1");
        gcd_Polynomial_helper("1", "-x^3-1", "1");
        gcd_Polynomial_helper("1", "3*x^10", "1");

        gcd_Polynomial_helper("x", "0", "x");
        gcd_Polynomial_helper("x", "1", "1");
        gcd_Polynomial_helper("x", "x", "x");
        gcd_Polynomial_helper("x", "-17", "1");
        gcd_Polynomial_helper("x", "x^2-4*x+7", "1");
        gcd_Polynomial_helper("x", "-x^3-1", "1");
        gcd_Polynomial_helper("x", "3*x^10", "x");

        gcd_Polynomial_helper("-17", "0", "1");
        gcd_Polynomial_helper("-17", "1", "1");
        gcd_Polynomial_helper("-17", "x", "1");
        gcd_Polynomial_helper("-17", "-17", "1");
        gcd_Polynomial_helper("-17", "x^2-4*x+7", "1");
        gcd_Polynomial_helper("-17", "-x^3-1", "1");
        gcd_Polynomial_helper("-17", "3*x^10", "1");

        gcd_Polynomial_helper("x^2-4*x+7", "0", "x^2-4*x+7");
        gcd_Polynomial_helper("x^2-4*x+7", "1", "1");
        gcd_Polynomial_helper("x^2-4*x+7", "x", "1");
        gcd_Polynomial_helper("x^2-4*x+7", "-17", "1");
        gcd_Polynomial_helper("x^2-4*x+7", "x^2-4*x+7", "x^2-4*x+7");
        gcd_Polynomial_helper("x^2-4*x+7", "-x^3-1", "1");
        gcd_Polynomial_helper("x^2-4*x+7", "3*x^10", "1");

        gcd_Polynomial_helper("-x^3-1", "0", "x^3+1");
        gcd_Polynomial_helper("-x^3-1", "1", "1");
        gcd_Polynomial_helper("-x^3-1", "x", "1");
        gcd_Polynomial_helper("-x^3-1", "-17", "1");
        gcd_Polynomial_helper("-x^3-1", "x^2-4*x+7", "1");
        gcd_Polynomial_helper("-x^3-1", "-x^3-1", "x^3+1");
        gcd_Polynomial_helper("-x^3-1", "3*x^10", "1");

        gcd_Polynomial_helper("3*x^10", "0", "x^10");
        gcd_Polynomial_helper("3*x^10", "1", "1");
        gcd_Polynomial_helper("3*x^10", "x", "x");
        gcd_Polynomial_helper("3*x^10", "-17", "1");
        gcd_Polynomial_helper("3*x^10", "x^2-4*x+7", "1");
        gcd_Polynomial_helper("3*x^10", "-x^3-1", "1");
        gcd_Polynomial_helper("3*x^10", "3*x^10", "x^10");

        gcd_Polynomial_helper("x^2+7*x+6", "x^2-5*x-6", "x+1");

        gcd_Polynomial_fail_helper("0", "0");
    }

    private static void gcd_List_Polynomial_helper(@NotNull String input, @NotNull String output) {
        aeq(gcd(readPolynomialList(input)), output);
    }

    private static void gcd_List_Polynomial_fail_helper(@NotNull String input) {
        try {
            gcd(readPolynomialListWithNulls(input));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testGcd_List_Polynomial() {
        gcd_List_Polynomial_helper("[1]", "1");
        gcd_List_Polynomial_helper("[-17]", "1");
        gcd_List_Polynomial_helper("[-17, x^2-4*x+7, -x^3-1, 3*x^10]", "1");
        gcd_List_Polynomial_helper("[x+1, x^2+2*x+1, 3*x+3]", "x+1");

        gcd_List_Polynomial_fail_helper("[-17, null, -x^3-1, 3*x^10]");
        gcd_List_Polynomial_fail_helper("[1, null]");
    }

    private static void lcm_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().lcm(readStrict(b).get()), output);
    }

    @Test
    public void testLcm() {
        lcm_helper("0", "0", "0");
        lcm_helper("0", "1", "0");
        lcm_helper("0", "x", "0");
        lcm_helper("0", "-17", "0");
        lcm_helper("0", "x^2-4*x+7", "0");
        lcm_helper("0", "-x^3-1", "0");
        lcm_helper("0", "3*x^10", "0");

        lcm_helper("1", "0", "0");
        lcm_helper("1", "1", "1");
        lcm_helper("1", "x", "x");
        lcm_helper("1", "-17", "1");
        lcm_helper("1", "x^2-4*x+7", "x^2-4*x+7");
        lcm_helper("1", "-x^3-1", "x^3+1");
        lcm_helper("1", "3*x^10", "x^10");

        lcm_helper("x", "0", "0");
        lcm_helper("x", "1", "x");
        lcm_helper("x", "x", "x");
        lcm_helper("x", "-17", "x");
        lcm_helper("x", "x^2-4*x+7", "x^3-4*x^2+7*x");
        lcm_helper("x", "-x^3-1", "x^4+x");
        lcm_helper("x", "3*x^10", "x^10");

        lcm_helper("-17", "0", "0");
        lcm_helper("-17", "1", "1");
        lcm_helper("-17", "x", "x");
        lcm_helper("-17", "-17", "1");
        lcm_helper("-17", "x^2-4*x+7", "x^2-4*x+7");
        lcm_helper("-17", "-x^3-1", "x^3+1");
        lcm_helper("-17", "3*x^10", "x^10");

        lcm_helper("x^2-4*x+7", "0", "0");
        lcm_helper("x^2-4*x+7", "1", "x^2-4*x+7");
        lcm_helper("x^2-4*x+7", "x", "x^3-4*x^2+7*x");
        lcm_helper("x^2-4*x+7", "-17", "x^2-4*x+7");
        lcm_helper("x^2-4*x+7", "x^2-4*x+7", "x^2-4*x+7");
        lcm_helper("x^2-4*x+7", "-x^3-1", "x^5-4*x^4+7*x^3+x^2-4*x+7");
        lcm_helper("x^2-4*x+7", "3*x^10", "x^12-4*x^11+7*x^10");

        lcm_helper("-x^3-1", "0", "0");
        lcm_helper("-x^3-1", "1", "x^3+1");
        lcm_helper("-x^3-1", "x", "x^4+x");
        lcm_helper("-x^3-1", "-17", "x^3+1");
        lcm_helper("-x^3-1", "x^2-4*x+7", "x^5-4*x^4+7*x^3+x^2-4*x+7");
        lcm_helper("-x^3-1", "-x^3-1", "x^3+1");
        lcm_helper("-x^3-1", "3*x^10", "x^13+x^10");

        lcm_helper("3*x^10", "0", "0");
        lcm_helper("3*x^10", "1", "x^10");
        lcm_helper("3*x^10", "x", "x^10");
        lcm_helper("3*x^10", "-17", "x^10");
        lcm_helper("3*x^10", "x^2-4*x+7", "x^12-4*x^11+7*x^10");
        lcm_helper("3*x^10", "-x^3-1", "x^13+x^10");
        lcm_helper("3*x^10", "3*x^10", "x^10");

        lcm_helper("x^2+7*x+6", "x^2-5*x-6", "x^3+x^2-36*x-36");
    }

    private static void isRelativelyPrimeTo_helper(@NotNull String a, @NotNull String b, boolean output) {
        aeq(readStrict(a).get().isRelativelyPrimeTo(readStrict(b).get()), output);
    }

    private static void isRelativelyPrimeTo_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().isRelativelyPrimeTo(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIsRelativelyPrimeTo() {
        isRelativelyPrimeTo_helper("0", "1", true);
        isRelativelyPrimeTo_helper("0", "x", false);
        isRelativelyPrimeTo_helper("0", "-17", true);
        isRelativelyPrimeTo_helper("0", "x^2-4*x+7", false);
        isRelativelyPrimeTo_helper("0", "-x^3-1", false);
        isRelativelyPrimeTo_helper("0", "3*x^10", false);

        isRelativelyPrimeTo_helper("1", "0", true);
        isRelativelyPrimeTo_helper("1", "1", true);
        isRelativelyPrimeTo_helper("1", "x", true);
        isRelativelyPrimeTo_helper("1", "-17", true);
        isRelativelyPrimeTo_helper("1", "x^2-4*x+7", true);
        isRelativelyPrimeTo_helper("1", "-x^3-1", true);
        isRelativelyPrimeTo_helper("1", "3*x^10", true);

        isRelativelyPrimeTo_helper("x", "0", false);
        isRelativelyPrimeTo_helper("x", "1", true);
        isRelativelyPrimeTo_helper("x", "x", false);
        isRelativelyPrimeTo_helper("x", "-17", true);
        isRelativelyPrimeTo_helper("x", "x^2-4*x+7", true);
        isRelativelyPrimeTo_helper("x", "-x^3-1", true);
        isRelativelyPrimeTo_helper("x", "3*x^10", false);

        isRelativelyPrimeTo_helper("-17", "0", true);
        isRelativelyPrimeTo_helper("-17", "1", true);
        isRelativelyPrimeTo_helper("-17", "x", true);
        isRelativelyPrimeTo_helper("-17", "-17", true);
        isRelativelyPrimeTo_helper("-17", "x^2-4*x+7", true);
        isRelativelyPrimeTo_helper("-17", "-x^3-1", true);
        isRelativelyPrimeTo_helper("-17", "3*x^10", true);

        isRelativelyPrimeTo_helper("x^2-4*x+7", "0", false);
        isRelativelyPrimeTo_helper("x^2-4*x+7", "1", true);
        isRelativelyPrimeTo_helper("x^2-4*x+7", "x", true);
        isRelativelyPrimeTo_helper("x^2-4*x+7", "-17", true);
        isRelativelyPrimeTo_helper("x^2-4*x+7", "x^2-4*x+7", false);
        isRelativelyPrimeTo_helper("x^2-4*x+7", "-x^3-1", true);
        isRelativelyPrimeTo_helper("x^2-4*x+7", "3*x^10", true);

        isRelativelyPrimeTo_helper("-x^3-1", "0", false);
        isRelativelyPrimeTo_helper("-x^3-1", "1", true);
        isRelativelyPrimeTo_helper("-x^3-1", "x", true);
        isRelativelyPrimeTo_helper("-x^3-1", "-17", true);
        isRelativelyPrimeTo_helper("-x^3-1", "x^2-4*x+7", true);
        isRelativelyPrimeTo_helper("-x^3-1", "-x^3-1", false);
        isRelativelyPrimeTo_helper("-x^3-1", "3*x^10", true);

        isRelativelyPrimeTo_helper("3*x^10", "0", false);
        isRelativelyPrimeTo_helper("3*x^10", "1", true);
        isRelativelyPrimeTo_helper("3*x^10", "x", false);
        isRelativelyPrimeTo_helper("3*x^10", "-17", true);
        isRelativelyPrimeTo_helper("3*x^10", "x^2-4*x+7", true);
        isRelativelyPrimeTo_helper("3*x^10", "-x^3-1", true);
        isRelativelyPrimeTo_helper("3*x^10", "3*x^10", false);

        isRelativelyPrimeTo_helper("x^2+7*x+6", "x^2-5*x-6", false);

        isRelativelyPrimeTo_fail_helper("0", "0");
    }

    private static void isSquareFree_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isSquareFree(), output);
    }

    @Test
    public void testIsSquareFree() {
        isSquareFree_helper("0", false);
        isSquareFree_helper("1", true);
        isSquareFree_helper("x", true);
        isSquareFree_helper("-17", true);
        isSquareFree_helper("x^2-4*x+7", true);
        isSquareFree_helper("-x^3-1", true);
        isSquareFree_helper("3*x^10", false);

        isSquareFree_helper("x^2+2*x+1", false);
    }

    private static void squareFreePart_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().squareFreePart(), output);
    }

    private static void squareFreePart_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().squareFreePart();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSquareFreePart() {
        squareFreePart_helper("1", "1");
        squareFreePart_helper("x", "x");
        squareFreePart_helper("-17", "1");
        squareFreePart_helper("x^2-4*x+7", "x^2-4*x+7");
        squareFreePart_helper("-x^3-1", "x^3+1");
        squareFreePart_helper("3*x^10", "x");

        squareFreePart_helper("x^2+2*x+1", "x+1");

        squareFreePart_fail_helper("0");
    }

    private static void squareFreeFactor_helper(@NotNull String input, @NotNull String output) {
        assertTrue(readStrict(input).get().isPrimitive());
        aeq(readStrict(input).get().squareFreeFactor(), output);
    }

    @Test
    public void testSquareFreeFactor() {
        squareFreeFactor_helper("1", "[]");
        squareFreeFactor_helper("x", "[x]");
        squareFreeFactor_helper("x^2-4*x+7", "[x^2-4*x+7]");
        squareFreeFactor_helper("x^3+1", "[x^3+1]");
        squareFreeFactor_helper("x^10", "[x, x, x, x, x, x, x, x, x, x]");
        squareFreeFactor_helper("x^2+2*x+1", "[x+1, x+1]");
    }

    private static void factor_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().factor(), output);
    }

    private static void factor_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().factor();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testFactor() {
        factor_helper("1", "[]");
        factor_helper("-1", "[-1]");
        factor_helper("x", "[x]");
        factor_helper("-17", "[-17]");
        factor_helper("x^2-4*x+7", "[x^2-4*x+7]");
        factor_helper("x^3-1", "[x-1, x^2+x+1]");
        factor_helper("3*x^10", "[3, x, x, x, x, x, x, x, x, x, x]");

        factor_helper("6", "[6]");
        factor_helper("-x", "[-1, x]");
        factor_helper("3*x", "[3, x]");
        factor_helper("x^10", "[x, x, x, x, x, x, x, x, x, x]");
        factor_helper("x^2-1", "[x-1, x+1]");
        factor_helper("3*x^6+24*x+2", "[3*x^6+24*x+2]");
        factor_helper("3*x^6+24*x+12", "[3, x^6+8*x+4]");
        factor_helper("x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5", "[x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5]");
        factor_helper("x^5+x^4+x^2+x+2", "[x^2+x+1, x^3-x+2]");
        factor_helper(
                "x^6-41*x^5+652*x^4-5102*x^3+20581*x^2-40361*x+30031",
                "[x^6-41*x^5+652*x^4-5102*x^3+20581*x^2-40361*x+30031]"
        );
        factor_helper(
                "3*x^8+25*x^7+12*x^6-24*x^5+86*x^4-3*x^3-3*x^2+35*x+12",
                "[x^4+7*x^3-4*x^2+5*x+4, 3*x^4+4*x^3-4*x^2+5*x+3]"
        );
        factor_helper(
                "114041041*x^8+75072771976*x^7+14078766732040*x^6+620936816292544*x^5+8987291129562544*x^4-" +
                "12309633851062016*x^3-1181581086409765760*x^2-4560536778732921344*x+24667056244891853056",
                "[10679*x^4+3055132*x^3+67216932*x^2-139102448*x-7673059216," +
                " 10679*x^4+3974812*x^3+113997732*x^2+652636432*x-3214761616]"
        );
        factor_helper(
                "65536*x^72-2621440*x^70+1048576*x^69+47972352*x^68-38535168*x^67-525139968*x^66+645922816*x^65+" +
                "3752198144*x^64-6507790336*x^63-17625923584*x^62+43573182464*x^61+48623316992*x^60-" +
                "201052356608*x^59-21592313856*x^58+630585999360*x^57-459350970368*x^56-1192397180928*x^55+" +
                "2265417959424*x^54+410196217856*x^53-5724183067136*x^52+5285853506560*x^51+7473679587840*x^50-" +
                "18629447326720*x^49+2044069048384*x^48+32661373259776*x^47-32480258869952*x^46-23274174418432*x^45+" +
                "73889897585728*x^44-36917063556736*x^43-79660313695104*x^42+131308121971584*x^41-" +
                "2531471576720*x^40-166963370333920*x^39+140326535864464*x^38+56931043778176*x^37-" +
                "192191445581264*x^36+127348114793552*x^35+69536352863328*x^34-187066595647440*x^33+" +
                "104667809105817*x^32+53907642497700*x^31-126633132192912*x^30+88104329874684*x^29+" +
                "2496586906368*x^28-61647822912952*x^27+56851517573884*x^26-26192073465540*x^25-207802230696*x^24+" +
                "15501451719492*x^23-17905145192578*x^22+14637241748190*x^21-9783804452778*x^20+3753647987988*x^19+" +
                "276759138184*x^18-1703416482184*x^17+1741593353124*x^16-1076237434256*x^15+506022665406*x^14-" +
                "217962361777*x^13+45152941216*x^12+25163688232*x^11-34238911848*x^10+23261035580*x^9-" +
                "7927272793*x^8+494427179*x^7+771191070*x^6-470115239*x^5+133329032*x^4-13719648*x^3-552480*x^2+" +
                "235008*x-19440",
                "[65536*x^72-2621440*x^70+1048576*x^69+47972352*x^68-38535168*x^67-525139968*x^66+645922816*x^65+" +
                "3752198144*x^64-6507790336*x^63-17625923584*x^62+43573182464*x^61+48623316992*x^60-" +
                "201052356608*x^59-21592313856*x^58+630585999360*x^57-459350970368*x^56-1192397180928*x^55+" +
                "2265417959424*x^54+410196217856*x^53-5724183067136*x^52+5285853506560*x^51+7473679587840*x^50-" +
                "18629447326720*x^49+2044069048384*x^48+32661373259776*x^47-32480258869952*x^46-23274174418432*x^45+" +
                "73889897585728*x^44-36917063556736*x^43-79660313695104*x^42+131308121971584*x^41-" +
                "2531471576720*x^40-166963370333920*x^39+140326535864464*x^38+56931043778176*x^37-" +
                "192191445581264*x^36+127348114793552*x^35+69536352863328*x^34-187066595647440*x^33+" +
                "104667809105817*x^32+53907642497700*x^31-126633132192912*x^30+88104329874684*x^29+" +
                "2496586906368*x^28-61647822912952*x^27+56851517573884*x^26-26192073465540*x^25-207802230696*x^24+" +
                "15501451719492*x^23-17905145192578*x^22+14637241748190*x^21-9783804452778*x^20+3753647987988*x^19+" +
                "276759138184*x^18-1703416482184*x^17+1741593353124*x^16-1076237434256*x^15+506022665406*x^14-" +
                "217962361777*x^13+45152941216*x^12+25163688232*x^11-34238911848*x^10+23261035580*x^9-" +
                "7927272793*x^8+494427179*x^7+771191070*x^6-470115239*x^5+133329032*x^4-13719648*x^3-552480*x^2+" +
                "235008*x-19440]"
        );
        factor_helper(
                "x^27+6569368281923092251642*x^26+11176098125781590669940099356083790227234*x^25-" +
                "166190811410600729989152748758535912028540364955056686010*x^24+" +
                "757820279535930209156381099667374243256537767796106107049884361980215722*x^23-" +
                "1086023145885836892265068601506444260457337574906712420308773422527517298648192967177586*x^22+" +
                "59402992909390943784617268646277306239225847553773209265104525581137494964050218538430833847698147" +
                "0*x^21+" +
                "660316076081327747848284317303270028789035756663431159880501410665139420377934379110312867054473923" +
                "4313730142*x^20+" +
                "224918113953890083743490124442639415793362062486727790238942994392550771520075553654752838222703054" +
                "48611568030143284812*x^19+" +
                "335151872274849894827574899936331578782508922243130300493867621068215160219697272027483418384507309" +
                "52863826198615239216498092362*x^18+" +
                "234616582306902657792237950182631553539364561659680246331055190899699711510476421162090110289890605" +
                "84256489796023705103653804028411986074*x^17+" +
                "716426130709077789336772429605733289313052020491932198682055723866062562715837961073963163317208816" +
                "0990517328705727487476053402874718347911678706*x^16+" +
                "733418163197524888821532715475080327391767700297719056832642301084130678948958320361418553676538760" +
                "539233907583184852453652519484057860828952920770552230*x^15+" +
                "201385083915207079137923105874240304072490873360409612449955465586446301346671015540944134765684477" +
                "05871926437367678866845873704464321668491094182695705974094522*x^14+" +
                "154839660667196312447368879978142223978724535594376818106099399193254490588385183533533260092063826" +
                "665756000450990109617918812680859786288983748738658765236031238448586*x^13-" +
                "444157016021894639858454188210942791130056663964016573599962094806381291532455870857120548835434795" +
                "32389502348521908412971345543034202320797823287305855611391021911147915318*x^12-" +
                "713906314142053049326656613992590225059756354996311810211037956437968063660445272647741384721872369" +
                "53133275249142334184749344618860708898296215567136971468710546881428013551631869*x^11+" +
                "680841620911562539390128643729675288141305302548892693593579947182730092976119263234994394955565899" +
                "5220550720516704723467764277221013837717709828856934276512167363980913102566890868084*x^10+" +
                "806439898764039295376661830500180074715683537536355695796606929207021250748275315784659431827356201" +
                "6988605406147110672900283658493370514571281276656109549762895230124641090732032883321608476*x^9+" +
                "458066991527868050833681665335245853436019451941922126080694214051444736905619848825861029646914097" +
                "40459268165154495773771440637428461614485230093979740361232686581225148904349530477007153219408" +
                "0*x^8-" +
                "100811189795572593667400245882954277530656141358896283564379320348224789367300154122628610694996827" +
                "16083031951424465736992419103318425928994222199891768084263449417023656251553112007436908106388896*" +
                "x^7+" +
                "146700953799923261110247905810888137921535950695090003136869916053180722051515864725802944485866168" +
                "80837012122363638420271889391261459164987219755595411045448130218997681622414706947757699613550976*" +
                "x^6+" +
                "356510401973442983753507574316067972448623018212169119590771491256074096089672925887472887451137580" +
                "946720614692605263899169999183901602978191844031224748266871408473694853224276738215714048913548160" +
                "*x^5-" +
                "213162909754291257576911200085875816751166951434326083034210906765635235370082339665161116301869433" +
                "754746247549945797628801569955829575062629121199287302837976350073713518639776628308875259943001600" +
                "*x^4-" +
                "450371665601589211837950972273969451967719230731976741729209925081809448885234606394816182384288387" +
                "109790182100155790764950950919450941578825827301308825453967354243762951924015263194952869060803200" +
                "0*x^3-" +
                "514692806698150445714370856292092648410193457301708753124138355636138110184346058543884402539515166" +
                "073440647635293501711529329737286265881770574998802293056147220699824377572959402689451731813568000" +
                "0*x^2+" +
                "415728736495760740642373985065935413525183262044004604112268131462323508994935393570851523263162014" +
                "165574713415041907779640612141135708788331949042228150688833991204744644305741315749753688521920000" +
                "0*x+" +
                "534496281436426871214255221955068594209040009063097938479902678370817532358351064059951536192811786" +
                "088814667546405157994427973819603270755700942115535444919329902078348511524333769368212287142400000" +
                "0",
                "[x-7587332257843715, x-4090879484415307, x-3102559791329530, x-558154516330, x-627711, x-464901," +
                " x-18, x-6, x-6, x-1, x+1, x+2, x+3, x+3, x+62097, x+330680, x+478974, x+14186599, x+22807870," +
                " x+120682062, x+506979513, x+927695299, x+1265424985, x+1796838449, x+6309745387," +
                " x+1716469927821277666, x+6567666593313994939577]"
        );

        factor_fail_helper("0");
    }

    private static void isIrreducible_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isIrreducible(), output);
    }

    private static void isIrreducible_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().isIrreducible();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIsIrreducible() {
        isIrreducible_helper("1", true);
        isIrreducible_helper("-1", false);
        isIrreducible_helper("x", true);
        isIrreducible_helper("-17", false);
        isIrreducible_helper("x^2-4*x+7", true);
        isIrreducible_helper("x^3-1", false);
        isIrreducible_helper("3*x^10", false);

        isIrreducible_helper("6", false);
        isIrreducible_helper("-x", false);
        isIrreducible_helper("3*x", false);
        isIrreducible_helper("x^10", false);
        isIrreducible_helper("x^2-1", false);
        isIrreducible_helper("3*x^6+24*x+2", true);
        isIrreducible_helper("3*x^6+24*x+12", false);
        isIrreducible_helper("x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5", true);
        isIrreducible_helper("x^5+x^4+x^2+x+2", false);
        isIrreducible_helper("x^6-41*x^5+652*x^4-5102*x^3+20581*x^2-40361*x+30031", true);
        isIrreducible_helper("3*x^8+25*x^7+12*x^6-24*x^5+86*x^4-3*x^3-3*x^2+35*x+12", false);
        isIrreducible_helper(
                "114041041*x^8+75072771976*x^7+14078766732040*x^6+620936816292544*x^5+8987291129562544*x^4-" +
                "12309633851062016*x^3-1181581086409765760*x^2-4560536778732921344*x+24667056244891853056",
                false
        );
        isIrreducible_helper(
                "65536*x^72-2621440*x^70+1048576*x^69+47972352*x^68-38535168*x^67-525139968*x^66+645922816*x^65+" +
                "3752198144*x^64-6507790336*x^63-17625923584*x^62+43573182464*x^61+48623316992*x^60-" +
                "201052356608*x^59-21592313856*x^58+630585999360*x^57-459350970368*x^56-1192397180928*x^55+" +
                "2265417959424*x^54+410196217856*x^53-5724183067136*x^52+5285853506560*x^51+7473679587840*x^50-" +
                "18629447326720*x^49+2044069048384*x^48+32661373259776*x^47-32480258869952*x^46-23274174418432*x^45+" +
                "73889897585728*x^44-36917063556736*x^43-79660313695104*x^42+131308121971584*x^41-" +
                "2531471576720*x^40-166963370333920*x^39+140326535864464*x^38+56931043778176*x^37-" +
                "192191445581264*x^36+127348114793552*x^35+69536352863328*x^34-187066595647440*x^33+" +
                "104667809105817*x^32+53907642497700*x^31-126633132192912*x^30+88104329874684*x^29+" +
                "2496586906368*x^28-61647822912952*x^27+56851517573884*x^26-26192073465540*x^25-207802230696*x^24+" +
                "15501451719492*x^23-17905145192578*x^22+14637241748190*x^21-9783804452778*x^20+3753647987988*x^19+" +
                "276759138184*x^18-1703416482184*x^17+1741593353124*x^16-1076237434256*x^15+506022665406*x^14-" +
                "217962361777*x^13+45152941216*x^12+25163688232*x^11-34238911848*x^10+23261035580*x^9-" +
                "7927272793*x^8+494427179*x^7+771191070*x^6-470115239*x^5+133329032*x^4-13719648*x^3-552480*x^2+" +
                "235008*x-19440",
                true
        );

        isIrreducible_fail_helper("0");
    }

    private static void interpolate_helper(@NotNull String input, @NotNull String output) {
        aeq(interpolate(readBigIntegerPairList(input)), output);
    }

    private static void interpolate_fail_helper(@NotNull String input) {
        try {
            interpolate(readBigIntegerPairListWithNulls(input));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testInterpolate() {
        interpolate_helper("[]", "0");
        interpolate_helper("[(3, 5)]", "5");
        interpolate_helper("[(1, 2), (10, 5)]", "1/3*x+5/3");
        interpolate_helper("[(1, 1), (2, 4), (3, 9), (4, 16), (5, 25)]", "x^2");
        interpolate_helper("[(1, 2), (2, 3), (3, 5), (4, 7), (5, 11)]", "1/8*x^4-17/12*x^3+47/8*x^2-103/12*x+6");

        interpolate_fail_helper("[(1, 1), (1, 2)]");
        interpolate_fail_helper("[(1, 1), null]");
        interpolate_fail_helper("[(1, 1), (2, null)]");
        interpolate_fail_helper("[(1, 1), (null, 3)]");
    }

    private static void companionMatrix_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().companionMatrix(), output);
    }

    private static void companionMatrix_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().companionMatrix();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testCompanionMatrix() {
        companionMatrix_helper("1", "[]#0");
        companionMatrix_helper("x", "[[0]]");
        companionMatrix_helper("x^2-4*x+7", "[[0, -7], [1, 4]]");
        companionMatrix_helper("x^3-1", "[[0, 0, 1], [1, 0, 0], [0, 1, 0]]");
        companionMatrix_helper("x^10",
                "[[0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [1, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 1, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 1, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 1, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 1, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 1, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 1, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 1, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 1, 0]]");

        companionMatrix_fail_helper("0");
        companionMatrix_fail_helper("2");
        companionMatrix_fail_helper("-1");
        companionMatrix_fail_helper("2*x^2");
    }

    private static void coefficientMatrix_helper(@NotNull String input, @NotNull String output) {
        aeq(coefficientMatrix(readPolynomialList(input)), output);
    }

    private static void coefficientMatrix_fail_helper(@NotNull String input) {
        try {
            coefficientMatrix(readPolynomialList(input));
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testCoefficientMatrix() {
        coefficientMatrix_helper("[]", "[]#0");
        coefficientMatrix_helper("[0, x]", "[[0, 0], [1, 0]]");
        coefficientMatrix_helper("[1]", "[[1]]");
        coefficientMatrix_helper("[x, x]", "[[1, 0], [1, 0]]");
        coefficientMatrix_helper("[1, 2*x^2-5*x+2, x^3-17]", "[[0, 0, 0, 1], [0, 2, -5, 2], [1, 0, 0, -17]]");
        coefficientMatrix_helper("[x^2-4*x+7, x^3, 4]", "[[0, 1, -4, 7], [1, 0, 0, 0], [0, 0, 0, 4]]");
        coefficientMatrix_helper("[x^2-4*x+7, x^10, 4]",
                "[[0, 0, 0, 0, 0, 0, 0, 0, 1, -4, 7], [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4]]");

        coefficientMatrix_fail_helper("[0]");
        coefficientMatrix_fail_helper("[1, x, -17]");
    }

    private static void augmentedCoefficientMatrix_helper(@NotNull String input, @NotNull String output) {
        aeq(augmentedCoefficientMatrix(readPolynomialList(input)), output);
    }

    private static void augmentedCoefficientMatrix_fail_helper(@NotNull String input) {
        try {
            augmentedCoefficientMatrix(readPolynomialList(input));
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testAugmentedCoefficientMatrix() {
        augmentedCoefficientMatrix_helper("[]", "[]#0");
        augmentedCoefficientMatrix_helper("[0, x]", "[[0, 0], [1, x]]");
        augmentedCoefficientMatrix_helper("[1]", "[[1]]");
        augmentedCoefficientMatrix_helper("[x, x]", "[[1, x], [1, x]]");
        augmentedCoefficientMatrix_helper("[1, 2*x^2-5*x+2, x^3-17]",
                "[[0, 0, 1], [0, 2, 2*x^2-5*x+2], [1, 0, x^3-17]]");
        augmentedCoefficientMatrix_helper("[x^2-4*x+7, x^3, 4]", "[[0, 1, x^2-4*x+7], [1, 0, x^3], [0, 0, 4]]");
        augmentedCoefficientMatrix_helper("[x^2-4*x+7, x^10, 4]", "[[0, 0, x^2-4*x+7], [1, 0, x^10], [0, 0, 4]]");

        augmentedCoefficientMatrix_fail_helper("[0]");
        augmentedCoefficientMatrix_fail_helper("[1, x, -17]");
    }

    private static void determinant_helper(@NotNull String input, @NotNull String output) {
        aeq(determinant(readPolynomialList(input)), output);
    }

    private static void determinant_fail_helper(@NotNull String input) {
        try {
            determinant(readPolynomialList(input));
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testDeterminant() {
        determinant_helper("[]", "1");
        determinant_helper("[0, x]", "0");
        determinant_helper("[1]", "1");
        determinant_helper("[x, x]", "0");
        determinant_helper("[1, 2*x^2-5*x+2, x^3-17]", "-2");
        determinant_helper("[x^2-4*x+7, x^3, 4]", "-4");
        determinant_helper("[x^2-4*x+7, x^10, 4]", "0");

        determinant_fail_helper("[0]");
        determinant_fail_helper("[1, x, -17]");
    }

    private static void sylvesterMatrix_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().sylvesterMatrix(readStrict(b).get()), output);
    }

    private static void sylvesterMatrix_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().sylvesterMatrix(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSylvesterMatrix() {
        sylvesterMatrix_helper("1", "1", "[]#0");
        sylvesterMatrix_helper("1", "x", "[[1]]");
        sylvesterMatrix_helper("1", "-17", "[]#0");
        sylvesterMatrix_helper("1", "x^2-4*x+7", "[[1, 0], [0, 1]]");
        sylvesterMatrix_helper("1", "-x^3-1", "[[1, 0, 0], [0, 1, 0], [0, 0, 1]]");
        sylvesterMatrix_helper("1", "3*x^10",
                "[[1, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 1, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 1, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 1, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 1, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 1, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 1, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 1, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 1, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 0, 1]]");

        sylvesterMatrix_helper("x", "1", "[[1]]");
        sylvesterMatrix_helper("x", "x", "[[1, 0], [1, 0]]");
        sylvesterMatrix_helper("x", "-17", "[[-17]]");
        sylvesterMatrix_helper("x", "x^2-4*x+7", "[[1, 0, 0], [0, 1, 0], [1, -4, 7]]");
        sylvesterMatrix_helper("x", "-x^3-1", "[[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [-1, 0, 0, -1]]");
        sylvesterMatrix_helper("x", "3*x^10",
                "[[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0]," +
                " [3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]]");

        sylvesterMatrix_helper("-17", "1", "[]#0");
        sylvesterMatrix_helper("-17", "x", "[[-17]]");
        sylvesterMatrix_helper("-17", "-17", "[]#0");
        sylvesterMatrix_helper("-17", "x^2-4*x+7", "[[-17, 0], [0, -17]]");
        sylvesterMatrix_helper("-17", "-x^3-1", "[[-17, 0, 0], [0, -17, 0], [0, 0, -17]]");
        sylvesterMatrix_helper("-17", "3*x^10",
                "[[-17, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, -17, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, -17, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, -17, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, -17, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, -17, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, -17, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, -17, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, -17, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, -17]]");

        sylvesterMatrix_helper("x^2-4*x+7", "1", "[[1, 0], [0, 1]]");
        sylvesterMatrix_helper("x^2-4*x+7", "x", "[[1, -4, 7], [1, 0, 0], [0, 1, 0]]");
        sylvesterMatrix_helper("x^2-4*x+7", "-17", "[[-17, 0], [0, -17]]");
        sylvesterMatrix_helper("x^2-4*x+7", "x^2-4*x+7",
                "[[1, -4, 7, 0], [0, 1, -4, 7], [1, -4, 7, 0], [0, 1, -4, 7]]");
        sylvesterMatrix_helper("x^2-4*x+7", "-x^3-1",
                "[[1, -4, 7, 0, 0], [0, 1, -4, 7, 0], [0, 0, 1, -4, 7], [-1, 0, 0, -1, 0], [0, -1, 0, 0, -1]]");
        sylvesterMatrix_helper("x^2-4*x+7", "3*x^10",
                "[[1, -4, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 1, -4, 7, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 1, -4, 7, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 1, -4, 7, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 1, -4, 7, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 1, -4, 7, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 1, -4, 7, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 1, -4, 7, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 1, -4, 7, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -4, 7]," +
                " [3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]]");

        sylvesterMatrix_helper("-x^3-1", "1", "[[1, 0, 0], [0, 1, 0], [0, 0, 1]]");
        sylvesterMatrix_helper("-x^3-1", "x", "[[-1, 0, 0, -1], [1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0]]");
        sylvesterMatrix_helper("-x^3-1", "-17", "[[-17, 0, 0], [0, -17, 0], [0, 0, -17]]");
        sylvesterMatrix_helper("-x^3-1", "x^2-4*x+7",
                "[[-1, 0, 0, -1, 0], [0, -1, 0, 0, -1], [1, -4, 7, 0, 0], [0, 1, -4, 7, 0], [0, 0, 1, -4, 7]]");
        sylvesterMatrix_helper("-x^3-1", "-x^3-1",
                "[[-1, 0, 0, -1, 0, 0], [0, -1, 0, 0, -1, 0], [0, 0, -1, 0, 0, -1], [-1, 0, 0, -1, 0, 0]," +
                " [0, -1, 0, 0, -1, 0], [0, 0, -1, 0, 0, -1]]");
        sylvesterMatrix_helper("-x^3-1", "3*x^10",
                "[[-1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1]," +
                " [3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]]");

        sylvesterMatrix_helper("3*x^10", "1",
                "[[1, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 1, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 1, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 1, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 1, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 1, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 1, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 1, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 1, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 0, 1]]");
        sylvesterMatrix_helper("3*x^10", "x",
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0]]");
        sylvesterMatrix_helper("3*x^10", "-17",
                "[[-17, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, -17, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, -17, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, -17, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, -17, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, -17, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, -17, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, -17, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, -17, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, -17]]");
        sylvesterMatrix_helper("3*x^10", "x^2-4*x+7",
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [1, -4, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 1, -4, 7, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 1, -4, 7, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 1, -4, 7, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 1, -4, 7, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 1, -4, 7, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 1, -4, 7, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 1, -4, 7, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 1, -4, 7, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -4, 7]]");
        sylvesterMatrix_helper("3*x^10", "-x^3-1",
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [-1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1]]");
        sylvesterMatrix_helper("3*x^10", "3*x^10",
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]]");

        sylvesterMatrix_fail_helper("0", "x");
        sylvesterMatrix_fail_helper("x", "0");
    }

    private static void resultant_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().resultant(readStrict(b).get()), output);
    }

    private static void resultant_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().resultant(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testResultant() {
        resultant_helper("1", "1", "1");
        resultant_helper("1", "x", "1");
        resultant_helper("1", "-17", "1");
        resultant_helper("1", "x^2-4*x+7", "1");
        resultant_helper("1", "-x^3-1", "1");
        resultant_helper("1", "3*x^10", "1");

        resultant_helper("x", "1", "1");
        resultant_helper("x", "x", "0");
        resultant_helper("x", "-17", "-17");
        resultant_helper("x", "x^2-4*x+7", "7");
        resultant_helper("x", "-x^3-1", "-1");
        resultant_helper("x", "3*x^10", "0");

        resultant_helper("-17", "1", "1");
        resultant_helper("-17", "x", "-17");
        resultant_helper("-17", "-17", "1");
        resultant_helper("-17", "x^2-4*x+7", "289");
        resultant_helper("-17", "-x^3-1", "-4913");
        resultant_helper("-17", "3*x^10", "2015993900449");

        resultant_helper("x^2-4*x+7", "1", "1");
        resultant_helper("x^2-4*x+7", "x", "7");
        resultant_helper("x^2-4*x+7", "-17", "289");
        resultant_helper("x^2-4*x+7", "x^2-4*x+7", "0");
        resultant_helper("x^2-4*x+7", "-x^3-1", "324");
        resultant_helper("x^2-4*x+7", "3*x^10", "2542277241");

        resultant_helper("-x^3-1", "1", "1");
        resultant_helper("-x^3-1", "x", "1");
        resultant_helper("-x^3-1", "-17", "-4913");
        resultant_helper("-x^3-1", "x^2-4*x+7", "324");
        resultant_helper("-x^3-1", "-x^3-1", "0");
        resultant_helper("-x^3-1", "3*x^10", "27");

        resultant_helper("3*x^10", "1", "1");
        resultant_helper("3*x^10", "x", "0");
        resultant_helper("3*x^10", "-17", "2015993900449");
        resultant_helper("3*x^10", "x^2-4*x+7", "2542277241");
        resultant_helper("3*x^10", "-x^3-1", "27");
        resultant_helper("3*x^10", "3*x^10", "0");

        resultant_fail_helper("0", "x");
        resultant_fail_helper("x", "0");
    }

    private static void sylvesterHabichtMatrix_helper(
            @NotNull String p,
            @NotNull String q,
            int j,
            @NotNull String output
    ) {
        aeq(readStrict(p).get().sylvesterHabichtMatrix(readStrict(q).get(), j), output);
    }

    private static void sylvesterHabichtMatrix_fail_helper(@NotNull String p, @NotNull String q, int j) {
        try {
            readStrict(p).get().sylvesterHabichtMatrix(readStrict(q).get(), j);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testSylvesterHabichtMatrix() {
        sylvesterHabichtMatrix_helper("x", "1", 0, "[[1]]");
        sylvesterHabichtMatrix_helper("x", "-17", 0, "[[-17]]");

        sylvesterHabichtMatrix_helper("x^2-4*x+7", "1", 0, "[[0, 1], [1, 0]]");
        sylvesterHabichtMatrix_helper("x^2-4*x+7", "x", 0, "[[1, -4, 7], [0, 1, 0], [1, 0, 0]]");
        sylvesterHabichtMatrix_helper("x^2-4*x+7", "x", 1, "[[1, 0]]");
        sylvesterHabichtMatrix_helper("x^2-4*x+7", "-17", 0, "[[0, -17], [-17, 0]]");

        sylvesterHabichtMatrix_helper("-x^3-1", "1", 0, "[[0, 0, 1], [0, 1, 0], [1, 0, 0]]");
        sylvesterHabichtMatrix_helper("-x^3-1", "x", 0, "[[-1, 0, 0, -1], [0, 0, 1, 0], [0, 1, 0, 0], [1, 0, 0, 0]]");
        sylvesterHabichtMatrix_helper("-x^3-1", "x", 1, "[[0, 1, 0], [1, 0, 0]]");
        sylvesterHabichtMatrix_helper("-x^3-1", "-17", 0, "[[0, 0, -17], [0, -17, 0], [-17, 0, 0]]");
        sylvesterHabichtMatrix_helper("-x^3-1", "x^2-4*x+7", 0,
                "[[-1, 0, 0, -1, 0], [0, -1, 0, 0, -1], [0, 0, 1, -4, 7], [0, 1, -4, 7, 0], [1, -4, 7, 0, 0]]");
        sylvesterHabichtMatrix_helper("-x^3-1", "x^2-4*x+7", 1, "[[-1, 0, 0, -1], [0, 1, -4, 7], [1, -4, 7, 0]]");
        sylvesterHabichtMatrix_helper("-x^3-1", "x^2-4*x+7", 2, "[[1, -4, 7]]");

        sylvesterHabichtMatrix_helper("3*x^10", "1", 0,
                "[[0, 0, 0, 0, 0, 0, 0, 0, 0, 1], [0, 0, 0, 0, 0, 0, 0, 0, 1, 0], [0, 0, 0, 0, 0, 0, 0, 1, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 1, 0, 0, 0], [0, 0, 0, 0, 0, 1, 0, 0, 0, 0], [0, 0, 0, 0, 1, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 1, 0, 0, 0, 0, 0, 0], [0, 0, 1, 0, 0, 0, 0, 0, 0, 0], [0, 1, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [1, 0, 0, 0, 0, 0, 0, 0, 0, 0]]");
        sylvesterHabichtMatrix_helper("3*x^10", "x", 0,
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0], [0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0], [0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0], [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0], [0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]]");
        sylvesterHabichtMatrix_helper("3*x^10", "x", 1,
                "[[0, 0, 0, 0, 0, 0, 0, 0, 1, 0], [0, 0, 0, 0, 0, 0, 0, 1, 0, 0], [0, 0, 0, 0, 0, 0, 1, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 1, 0, 0, 0, 0], [0, 0, 0, 0, 1, 0, 0, 0, 0, 0], [0, 0, 0, 1, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 1, 0, 0, 0, 0, 0, 0, 0], [0, 1, 0, 0, 0, 0, 0, 0, 0, 0], [1, 0, 0, 0, 0, 0, 0, 0, 0, 0]]");
        sylvesterHabichtMatrix_helper("3*x^10", "-17", 0,
                "[[0, 0, 0, 0, 0, 0, 0, 0, 0, -17], [0, 0, 0, 0, 0, 0, 0, 0, -17, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, -17, 0, 0], [0, 0, 0, 0, 0, 0, -17, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, -17, 0, 0, 0, 0], [0, 0, 0, 0, -17, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, -17, 0, 0, 0, 0, 0, 0], [0, 0, -17, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, -17, 0, 0, 0, 0, 0, 0, 0, 0], [-17, 0, 0, 0, 0, 0, 0, 0, 0, 0]]");
        sylvesterHabichtMatrix_helper("3*x^10", "x^2-4*x+7", 0,
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -4, 7], [0, 0, 0, 0, 0, 0, 0, 0, 1, -4, 7, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 1, -4, 7, 0, 0], [0, 0, 0, 0, 0, 0, 1, -4, 7, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 1, -4, 7, 0, 0, 0, 0], [0, 0, 0, 0, 1, -4, 7, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 1, -4, 7, 0, 0, 0, 0, 0, 0], [0, 0, 1, -4, 7, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 1, -4, 7, 0, 0, 0, 0, 0, 0, 0, 0], [1, -4, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0]]");
        sylvesterHabichtMatrix_helper("3*x^10", "x^2-4*x+7", 1,
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 1, -4, 7]," +
                " [0, 0, 0, 0, 0, 0, 0, 1, -4, 7, 0], [0, 0, 0, 0, 0, 0, 1, -4, 7, 0, 0]," +
                " [0, 0, 0, 0, 0, 1, -4, 7, 0, 0, 0], [0, 0, 0, 0, 1, -4, 7, 0, 0, 0, 0]," +
                " [0, 0, 0, 1, -4, 7, 0, 0, 0, 0, 0], [0, 0, 1, -4, 7, 0, 0, 0, 0, 0, 0]," +
                " [0, 1, -4, 7, 0, 0, 0, 0, 0, 0, 0], [1, -4, 7, 0, 0, 0, 0, 0, 0, 0, 0]]");
        sylvesterHabichtMatrix_helper("3*x^10", "x^2-4*x+7", 2,
                "[[0, 0, 0, 0, 0, 0, 0, 1, -4, 7], [0, 0, 0, 0, 0, 0, 1, -4, 7, 0], [0, 0, 0, 0, 0, 1, -4, 7, 0, 0]," +
                " [0, 0, 0, 0, 1, -4, 7, 0, 0, 0], [0, 0, 0, 1, -4, 7, 0, 0, 0, 0], [0, 0, 1, -4, 7, 0, 0, 0, 0, 0]," +
                " [0, 1, -4, 7, 0, 0, 0, 0, 0, 0], [1, -4, 7, 0, 0, 0, 0, 0, 0, 0]]");
        sylvesterHabichtMatrix_helper("3*x^10", "-x^3-1", 0,
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0], [0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0], [0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0], [0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0], [0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [-1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0]]");
        sylvesterHabichtMatrix_helper("3*x^10", "-x^3-1", 1,
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1], [0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0]," +
                " [0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0], [0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0]," +
                " [0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0], [0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0]," +
                " [0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0], [0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0]," +
                " [-1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0]]");
        sylvesterHabichtMatrix_helper("3*x^10", "-x^3-1", 2,
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1]," +
                " [0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0], [0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0]," +
                " [0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0], [0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0]," +
                " [0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0], [0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0]," +
                " [-1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0]]");
        sylvesterHabichtMatrix_helper("3*x^10", "-x^3-1", 3,
                "[[0, 0, 0, 0, 0, 0, -1, 0, 0, -1], [0, 0, 0, 0, 0, -1, 0, 0, -1, 0]," +
                " [0, 0, 0, 0, -1, 0, 0, -1, 0, 0], [0, 0, 0, -1, 0, 0, -1, 0, 0, 0]," +
                " [0, 0, -1, 0, 0, -1, 0, 0, 0, 0], [0, -1, 0, 0, -1, 0, 0, 0, 0, 0]," +
                " [-1, 0, 0, -1, 0, 0, 0, 0, 0, 0]]");

        sylvesterHabichtMatrix_fail_helper("0", "x", 0);
        sylvesterHabichtMatrix_fail_helper("x", "0", 0);
        sylvesterHabichtMatrix_fail_helper("x", "x", 0);
        sylvesterHabichtMatrix_fail_helper("x", "x^2", 0);
        sylvesterHabichtMatrix_fail_helper("x^2", "x", -1);
        sylvesterHabichtMatrix_fail_helper("x^2", "x", 2);
    }

    private static void signedSubresultantCoefficient_helper(
            @NotNull String p,
            @NotNull String q,
            int j,
            @NotNull String output
    ) {
        aeq(readStrict(p).get().signedSubresultantCoefficient(readStrict(q).get(), j), output);
    }

    private static void signedSubresultantCoefficient_fail_helper(@NotNull String p, @NotNull String q, int j) {
        try {
            readStrict(p).get().signedSubresultantCoefficient(readStrict(q).get(), j);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testSignedSubresultantCoefficient() {
        signedSubresultantCoefficient_helper("x", "1", 0, "1");
        signedSubresultantCoefficient_helper("x", "1", 1, "1");
        signedSubresultantCoefficient_helper("x", "-17", 0, "-17");
        signedSubresultantCoefficient_helper("x", "-17", 1, "1");

        signedSubresultantCoefficient_helper("x^2-4*x+7", "1", 0, "-1");
        signedSubresultantCoefficient_helper("x^2-4*x+7", "1", 1, "1");
        signedSubresultantCoefficient_helper("x^2-4*x+7", "1", 2, "1");
        signedSubresultantCoefficient_helper("x^2-4*x+7", "x", 0, "-7");
        signedSubresultantCoefficient_helper("x^2-4*x+7", "x", 1, "1");
        signedSubresultantCoefficient_helper("x^2-4*x+7", "x", 2, "1");
        signedSubresultantCoefficient_helper("x^2-4*x+7", "-17", 0, "-289");
        signedSubresultantCoefficient_helper("x^2-4*x+7", "-17", 1, "-17");
        signedSubresultantCoefficient_helper("x^2-4*x+7", "-17", 2, "1");

        signedSubresultantCoefficient_helper("-x^3-1", "1", 0, "-1");
        signedSubresultantCoefficient_helper("-x^3-1", "1", 1, "0");
        signedSubresultantCoefficient_helper("-x^3-1", "1", 2, "1");
        signedSubresultantCoefficient_helper("-x^3-1", "1", 3, "-1");
        signedSubresultantCoefficient_helper("-x^3-1", "x", 0, "-1");
        signedSubresultantCoefficient_helper("-x^3-1", "x", 1, "-1");
        signedSubresultantCoefficient_helper("-x^3-1", "x", 2, "1");
        signedSubresultantCoefficient_helper("-x^3-1", "x", 3, "-1");
        signedSubresultantCoefficient_helper("-x^3-1", "-17", 0, "4913");
        signedSubresultantCoefficient_helper("-x^3-1", "-17", 1, "0");
        signedSubresultantCoefficient_helper("-x^3-1", "-17", 2, "-17");
        signedSubresultantCoefficient_helper("-x^3-1", "-17", 3, "-1");
        signedSubresultantCoefficient_helper("-x^3-1", "x^2-4*x+7", 0, "-324");
        signedSubresultantCoefficient_helper("-x^3-1", "x^2-4*x+7", 1, "9");
        signedSubresultantCoefficient_helper("-x^3-1", "x^2-4*x+7", 2, "1");
        signedSubresultantCoefficient_helper("-x^3-1", "x^2-4*x+7", 3, "-1");

        signedSubresultantCoefficient_helper("3*x^10", "1", 0, "-1");
        signedSubresultantCoefficient_helper("3*x^10", "x", 0, "0");
        signedSubresultantCoefficient_helper("3*x^10", "x", 1, "1");
        signedSubresultantCoefficient_helper("3*x^10", "-17", 0, "-2015993900449");
        signedSubresultantCoefficient_helper("3*x^10", "x^2-4*x+7", 0, "-2542277241");
        signedSubresultantCoefficient_helper("3*x^10", "x^2-4*x+7", 1, "-21948");
        signedSubresultantCoefficient_helper("3*x^10", "x^2-4*x+7", 2, "1");
        signedSubresultantCoefficient_helper("3*x^10", "-x^3-1", 0, "-27");
        signedSubresultantCoefficient_helper("3*x^10", "-x^3-1", 1, "-9");
        signedSubresultantCoefficient_helper("3*x^10", "-x^3-1", 2, "0");
        signedSubresultantCoefficient_helper("3*x^10", "-x^3-1", 3, "1");

        signedSubresultantCoefficient_fail_helper("0", "x", 0);
        signedSubresultantCoefficient_fail_helper("x", "0", 0);
        signedSubresultantCoefficient_fail_helper("x", "x", 0);
        signedSubresultantCoefficient_fail_helper("x", "x^2", 0);
        signedSubresultantCoefficient_fail_helper("x^2", "x", -1);
        signedSubresultantCoefficient_fail_helper("x^2", "x", 3);
    }

    private static void sylvesterHabichtPolynomialMatrix_helper(
            @NotNull String p,
            @NotNull String q,
            int j,
            @NotNull String output
    ) {
        aeq(readStrict(p).get().sylvesterHabichtPolynomialMatrix(readStrict(q).get(), j), output);
    }

    private static void sylvesterHabichtPolynomialMatrix_fail_helper(@NotNull String p, @NotNull String q, int j) {
        try {
            readStrict(p).get().sylvesterHabichtPolynomialMatrix(readStrict(q).get(), j);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testSylvesterHabichtPolynomialMatrix() {
        sylvesterHabichtPolynomialMatrix_helper("x", "1", 0, "[[1]]");
        sylvesterHabichtPolynomialMatrix_helper("x", "-17", 0, "[[-17]]");

        sylvesterHabichtPolynomialMatrix_helper("x^2-4*x+7", "1", 0, "[[0, 1], [1, x]]");
        sylvesterHabichtPolynomialMatrix_helper("x^2-4*x+7", "x", 0, "[[1, -4, x^2-4*x+7], [0, 1, x], [1, 0, x^2]]");
        sylvesterHabichtPolynomialMatrix_helper("x^2-4*x+7", "x", 1, "[[x]]");
        sylvesterHabichtPolynomialMatrix_helper("x^2-4*x+7", "-17", 0, "[[0, -17], [-17, -17*x]]");

        sylvesterHabichtPolynomialMatrix_helper("-x^3-1", "1", 0, "[[0, 0, 1], [0, 1, x], [1, 0, x^2]]");
        sylvesterHabichtPolynomialMatrix_helper("-x^3-1", "x", 0,
                "[[-1, 0, 0, -x^3-1], [0, 0, 1, x], [0, 1, 0, x^2], [1, 0, 0, x^3]]");
        sylvesterHabichtPolynomialMatrix_helper("-x^3-1", "x", 1, "[[0, x], [1, x^2]]");
        sylvesterHabichtPolynomialMatrix_helper("-x^3-1", "-17", 0,
                "[[0, 0, -17], [0, -17, -17*x], [-17, 0, -17*x^2]]");
        sylvesterHabichtPolynomialMatrix_helper("-x^3-1", "x^2-4*x+7", 0,
                "[[-1, 0, 0, -1, -x^4-x], [0, -1, 0, 0, -x^3-1], [0, 0, 1, -4, x^2-4*x+7]," +
                " [0, 1, -4, 7, x^3-4*x^2+7*x], [1, -4, 7, 0, x^4-4*x^3+7*x^2]]");
        sylvesterHabichtPolynomialMatrix_helper("-x^3-1", "x^2-4*x+7", 1,
                "[[-1, 0, -x^3-1], [0, 1, x^2-4*x+7], [1, -4, x^3-4*x^2+7*x]]");
        sylvesterHabichtPolynomialMatrix_helper("-x^3-1", "x^2-4*x+7", 2, "[[x^2-4*x+7]]");

        sylvesterHabichtPolynomialMatrix_helper("3*x^10", "1", 0,
                "[[0, 0, 0, 0, 0, 0, 0, 0, 0, 1], [0, 0, 0, 0, 0, 0, 0, 0, 1, x], [0, 0, 0, 0, 0, 0, 0, 1, 0, x^2]," +
                " [0, 0, 0, 0, 0, 0, 1, 0, 0, x^3], [0, 0, 0, 0, 0, 1, 0, 0, 0, x^4]," +
                " [0, 0, 0, 0, 1, 0, 0, 0, 0, x^5], [0, 0, 0, 1, 0, 0, 0, 0, 0, x^6]," +
                " [0, 0, 1, 0, 0, 0, 0, 0, 0, x^7], [0, 1, 0, 0, 0, 0, 0, 0, 0, x^8]," +
                " [1, 0, 0, 0, 0, 0, 0, 0, 0, x^9]]");
        sylvesterHabichtPolynomialMatrix_helper("3*x^10", "x", 0,
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3*x^10], [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, x]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 1, 0, x^2], [0, 0, 0, 0, 0, 0, 0, 1, 0, 0, x^3]," +
                " [0, 0, 0, 0, 0, 0, 1, 0, 0, 0, x^4], [0, 0, 0, 0, 0, 1, 0, 0, 0, 0, x^5]," +
                " [0, 0, 0, 0, 1, 0, 0, 0, 0, 0, x^6], [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, x^7]," +
                " [0, 0, 1, 0, 0, 0, 0, 0, 0, 0, x^8], [0, 1, 0, 0, 0, 0, 0, 0, 0, 0, x^9]," +
                " [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, x^10]]");
        sylvesterHabichtPolynomialMatrix_helper("3*x^10", "x", 1,
                "[[0, 0, 0, 0, 0, 0, 0, 0, x], [0, 0, 0, 0, 0, 0, 0, 1, x^2], [0, 0, 0, 0, 0, 0, 1, 0, x^3]," +
                " [0, 0, 0, 0, 0, 1, 0, 0, x^4], [0, 0, 0, 0, 1, 0, 0, 0, x^5], [0, 0, 0, 1, 0, 0, 0, 0, x^6]," +
                " [0, 0, 1, 0, 0, 0, 0, 0, x^7], [0, 1, 0, 0, 0, 0, 0, 0, x^8], [1, 0, 0, 0, 0, 0, 0, 0, x^9]]");
        sylvesterHabichtPolynomialMatrix_helper("3*x^10", "-17", 0,
                "[[0, 0, 0, 0, 0, 0, 0, 0, 0, -17], [0, 0, 0, 0, 0, 0, 0, 0, -17, -17*x]," +
                " [0, 0, 0, 0, 0, 0, 0, -17, 0, -17*x^2], [0, 0, 0, 0, 0, 0, -17, 0, 0, -17*x^3]," +
                " [0, 0, 0, 0, 0, -17, 0, 0, 0, -17*x^4], [0, 0, 0, 0, -17, 0, 0, 0, 0, -17*x^5]," +
                " [0, 0, 0, -17, 0, 0, 0, 0, 0, -17*x^6], [0, 0, -17, 0, 0, 0, 0, 0, 0, -17*x^7]," +
                " [0, -17, 0, 0, 0, 0, 0, 0, 0, -17*x^8], [-17, 0, 0, 0, 0, 0, 0, 0, 0, -17*x^9]]");
        sylvesterHabichtPolynomialMatrix_helper("3*x^10", "x^2-4*x+7", 0,
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3*x^11], [0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3*x^10]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -4, x^2-4*x+7], [0, 0, 0, 0, 0, 0, 0, 0, 1, -4, 7, x^3-4*x^2+7*x]," +
                " [0, 0, 0, 0, 0, 0, 0, 1, -4, 7, 0, x^4-4*x^3+7*x^2]," +
                " [0, 0, 0, 0, 0, 0, 1, -4, 7, 0, 0, x^5-4*x^4+7*x^3]," +
                " [0, 0, 0, 0, 0, 1, -4, 7, 0, 0, 0, x^6-4*x^5+7*x^4]," +
                " [0, 0, 0, 0, 1, -4, 7, 0, 0, 0, 0, x^7-4*x^6+7*x^5]," +
                " [0, 0, 0, 1, -4, 7, 0, 0, 0, 0, 0, x^8-4*x^7+7*x^6]," +
                " [0, 0, 1, -4, 7, 0, 0, 0, 0, 0, 0, x^9-4*x^8+7*x^7]," +
                " [0, 1, -4, 7, 0, 0, 0, 0, 0, 0, 0, x^10-4*x^9+7*x^8]," +
                " [1, -4, 7, 0, 0, 0, 0, 0, 0, 0, 0, x^11-4*x^10+7*x^9]]");
        sylvesterHabichtPolynomialMatrix_helper("3*x^10", "x^2-4*x+7", 1,
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 3*x^10], [0, 0, 0, 0, 0, 0, 0, 0, 1, x^2-4*x+7]," +
                " [0, 0, 0, 0, 0, 0, 0, 1, -4, x^3-4*x^2+7*x], [0, 0, 0, 0, 0, 0, 1, -4, 7, x^4-4*x^3+7*x^2]," +
                " [0, 0, 0, 0, 0, 1, -4, 7, 0, x^5-4*x^4+7*x^3], [0, 0, 0, 0, 1, -4, 7, 0, 0, x^6-4*x^5+7*x^4]," +
                " [0, 0, 0, 1, -4, 7, 0, 0, 0, x^7-4*x^6+7*x^5], [0, 0, 1, -4, 7, 0, 0, 0, 0, x^8-4*x^7+7*x^6]," +
                " [0, 1, -4, 7, 0, 0, 0, 0, 0, x^9-4*x^8+7*x^7], [1, -4, 7, 0, 0, 0, 0, 0, 0, x^10-4*x^9+7*x^8]]");
        sylvesterHabichtPolynomialMatrix_helper("3*x^10", "x^2-4*x+7", 2,
                "[[0, 0, 0, 0, 0, 0, 0, x^2-4*x+7], [0, 0, 0, 0, 0, 0, 1, x^3-4*x^2+7*x]," +
                " [0, 0, 0, 0, 0, 1, -4, x^4-4*x^3+7*x^2], [0, 0, 0, 0, 1, -4, 7, x^5-4*x^4+7*x^3]," +
                " [0, 0, 0, 1, -4, 7, 0, x^6-4*x^5+7*x^4], [0, 0, 1, -4, 7, 0, 0, x^7-4*x^6+7*x^5]," +
                " [0, 1, -4, 7, 0, 0, 0, x^8-4*x^7+7*x^6], [1, -4, 7, 0, 0, 0, 0, x^9-4*x^8+7*x^7]]");
        sylvesterHabichtPolynomialMatrix_helper("3*x^10", "-x^3-1", 0,
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3*x^12], [0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3*x^11]," +
                " [0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3*x^10], [0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -x^3-1]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1, -x^4-x], [0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0, -x^5-x^2]," +
                " [0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, -x^6-x^3]," +
                " [0, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, -x^7-x^4]," +
                " [0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0, -x^8-x^5]," +
                " [0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0, -x^9-x^6]," +
                " [0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, -x^10-x^7]," +
                " [0, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, -x^11-x^8]," +
                " [-1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, -x^12-x^9]]");
        sylvesterHabichtPolynomialMatrix_helper("3*x^10", "-x^3-1", 1,
                "[[3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3*x^11], [0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3*x^10]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, -1, 0, -x^3-1], [0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -x^4-x]," +
                " [0, 0, 0, 0, 0, 0, -1, 0, 0, -1, -x^5-x^2], [0, 0, 0, 0, 0, -1, 0, 0, -1, 0, -x^6-x^3]," +
                " [0, 0, 0, 0, -1, 0, 0, -1, 0, 0, -x^7-x^4], [0, 0, 0, -1, 0, 0, -1, 0, 0, 0, -x^8-x^5]," +
                " [0, 0, -1, 0, 0, -1, 0, 0, 0, 0, -x^9-x^6], [0, -1, 0, 0, -1, 0, 0, 0, 0, 0, -x^10-x^7]," +
                " [-1, 0, 0, -1, 0, 0, 0, 0, 0, 0, -x^11-x^8]]");
        sylvesterHabichtPolynomialMatrix_helper("3*x^10", "-x^3-1", 2,
                "[[3, 0, 0, 0, 0, 0, 0, 0, 3*x^10], [0, 0, 0, 0, 0, 0, 0, -1, -x^3-1]," +
                " [0, 0, 0, 0, 0, 0, -1, 0, -x^4-x], [0, 0, 0, 0, 0, -1, 0, 0, -x^5-x^2]," +
                " [0, 0, 0, 0, -1, 0, 0, -1, -x^6-x^3], [0, 0, 0, -1, 0, 0, -1, 0, -x^7-x^4]," +
                " [0, 0, -1, 0, 0, -1, 0, 0, -x^8-x^5], [0, -1, 0, 0, -1, 0, 0, 0, -x^9-x^6]," +
                " [-1, 0, 0, -1, 0, 0, 0, 0, -x^10-x^7]]");
        sylvesterHabichtPolynomialMatrix_helper("3*x^10", "-x^3-1", 3,
                "[[0, 0, 0, 0, 0, 0, -x^3-1], [0, 0, 0, 0, 0, -1, -x^4-x], [0, 0, 0, 0, -1, 0, -x^5-x^2]," +
                " [0, 0, 0, -1, 0, 0, -x^6-x^3], [0, 0, -1, 0, 0, -1, -x^7-x^4], [0, -1, 0, 0, -1, 0, -x^8-x^5]," +
                " [-1, 0, 0, -1, 0, 0, -x^9-x^6]]");

        sylvesterHabichtPolynomialMatrix_fail_helper("0", "x", 0);
        sylvesterHabichtPolynomialMatrix_fail_helper("x", "0", 0);
        sylvesterHabichtPolynomialMatrix_fail_helper("x", "x", 0);
        sylvesterHabichtPolynomialMatrix_fail_helper("x", "x^2", 0);
        sylvesterHabichtPolynomialMatrix_fail_helper("x^2", "x", -1);
        sylvesterHabichtPolynomialMatrix_fail_helper("x^2", "x", 2);
    }

    private static void signedSubresultant_helper(
            @NotNull String p,
            @NotNull String q,
            int j,
            @NotNull String output
    ) {
        aeq(readStrict(p).get().signedSubresultant(readStrict(q).get(), j), output);
    }

    private static void signedSubresultant_fail_helper(@NotNull String p, @NotNull String q, int j) {
        try {
            readStrict(p).get().signedSubresultant(readStrict(q).get(), j);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testSignedSubresultant() {
        signedSubresultant_helper("x", "1", 0, "1");
        signedSubresultant_helper("x", "1", 1, "x");
        signedSubresultant_helper("x", "-17", 0, "-17");
        signedSubresultant_helper("x", "-17", 1, "x");

        signedSubresultant_helper("x^2-4*x+7", "1", 0, "-1");
        signedSubresultant_helper("x^2-4*x+7", "1", 1, "1");
        signedSubresultant_helper("x^2-4*x+7", "1", 2, "x^2-4*x+7");
        signedSubresultant_helper("x^2-4*x+7", "x", 0, "-7");
        signedSubresultant_helper("x^2-4*x+7", "x", 1, "x");
        signedSubresultant_helper("x^2-4*x+7", "x", 2, "x^2-4*x+7");
        signedSubresultant_helper("x^2-4*x+7", "-17", 0, "-289");
        signedSubresultant_helper("x^2-4*x+7", "-17", 1, "-17");
        signedSubresultant_helper("x^2-4*x+7", "-17", 2, "x^2-4*x+7");

        signedSubresultant_helper("-x^3-1", "1", 0, "-1");
        signedSubresultant_helper("-x^3-1", "1", 1, "0");
        signedSubresultant_helper("-x^3-1", "1", 2, "1");
        signedSubresultant_helper("-x^3-1", "1", 3, "-x^3-1");
        signedSubresultant_helper("-x^3-1", "x", 0, "-1");
        signedSubresultant_helper("-x^3-1", "x", 1, "-x");
        signedSubresultant_helper("-x^3-1", "x", 2, "x");
        signedSubresultant_helper("-x^3-1", "x", 3, "-x^3-1");
        signedSubresultant_helper("-x^3-1", "-17", 0, "4913");
        signedSubresultant_helper("-x^3-1", "-17", 1, "0");
        signedSubresultant_helper("-x^3-1", "-17", 2, "-17");
        signedSubresultant_helper("-x^3-1", "-17", 3, "-x^3-1");
        signedSubresultant_helper("-x^3-1", "x^2-4*x+7", 0, "-324");
        signedSubresultant_helper("-x^3-1", "x^2-4*x+7", 1, "9*x-27");
        signedSubresultant_helper("-x^3-1", "x^2-4*x+7", 2, "x^2-4*x+7");
        signedSubresultant_helper("-x^3-1", "x^2-4*x+7", 3, "-x^3-1");

        signedSubresultant_helper("3*x^10", "1", 0, "-1");
        signedSubresultant_helper("3*x^10", "x", 0, "0");
        signedSubresultant_helper("3*x^10", "x", 1, "x");
        signedSubresultant_helper("3*x^10", "-17", 0, "-2015993900449");
        signedSubresultant_helper("3*x^10", "x^2-4*x+7", 0, "-2542277241");
        signedSubresultant_helper("3*x^10", "x^2-4*x+7", 1, "-21948*x+10773");
        signedSubresultant_helper("3*x^10", "x^2-4*x+7", 2, "x^2-4*x+7");
        signedSubresultant_helper("3*x^10", "-x^3-1", 0, "-27");
        signedSubresultant_helper("3*x^10", "-x^3-1", 1, "-9*x");
        signedSubresultant_helper("3*x^10", "-x^3-1", 2, "-3*x");
        signedSubresultant_helper("3*x^10", "-x^3-1", 3, "x^3+1");

        signedSubresultant_fail_helper("0", "x", 0);
        signedSubresultant_fail_helper("x", "0", 0);
        signedSubresultant_fail_helper("x", "x", 0);
        signedSubresultant_fail_helper("x", "x^2", 0);
        signedSubresultant_fail_helper("x^2", "x", -1);
        signedSubresultant_fail_helper("x^2", "x", 3);
    }

    private static void signedSubresultantSequence_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        aeq(readStrict(a).get().signedSubresultantSequence(readStrict(b).get()), output);
    }

    private static void signedSubresultantSequence_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().signedSubresultantSequence(readStrict(b).get());
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testSignedSubresultantSequence() {
        signedSubresultantSequence_helper("x", "1", "[x, 1]");
        signedSubresultantSequence_helper("x", "-17", "[x, -17]");

        signedSubresultantSequence_helper("x^2-4*x+7", "1", "[x^2-4*x+7, 1, -1]");
        signedSubresultantSequence_helper("x^2-4*x+7", "x", "[x^2-4*x+7, x, -7]");
        signedSubresultantSequence_helper("x^2-4*x+7", "-17", "[x^2-4*x+7, -17, -289]");

        signedSubresultantSequence_helper("-x^3-1", "1", "[-x^3-1, 1, 0, -1]");
        signedSubresultantSequence_helper("-x^3-1", "x", "[-x^3-1, x, -x, -1]");
        signedSubresultantSequence_helper("-x^3-1", "-17", "[-x^3-1, -17, 0, 4913]");
        signedSubresultantSequence_helper("-x^3-1", "x^2-4*x+7", "[-x^3-1, x^2-4*x+7, 9*x-27, -324]");

        signedSubresultantSequence_helper("3*x^10", "1", "[3*x^10, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1]");
        signedSubresultantSequence_helper("3*x^10", "x", "[3*x^10, x, 0, 0, 0, 0, 0, 0, 0, x, 0]");
        signedSubresultantSequence_helper("3*x^10", "-17", "[3*x^10, -17, 0, 0, 0, 0, 0, 0, 0, 0, -2015993900449]");
        signedSubresultantSequence_helper("3*x^10", "x^2-4*x+7",
                "[3*x^10, x^2-4*x+7, 0, 0, 0, 0, 0, 0, x^2-4*x+7, -21948*x+10773, -2542277241]");
        signedSubresultantSequence_helper("3*x^10", "-x^3-1",
                "[3*x^10, -x^3-1, 0, 0, 0, 0, 0, x^3+1, -3*x, -9*x, -27]");

        signedSubresultantSequence_helper("x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5", "3*x^6+5*x^4-4*x^2-9*x+21",
                "[x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5, 3*x^6+5*x^4-4*x^2-9*x+21, -9*x^6-15*x^4+12*x^2+27*x-63," +
                " -15*x^4+3*x^2-9, 25*x^4-5*x^2+15, 65*x^2+125*x-245, -169*x^2-325*x+637, -9326*x+12300, 260708]");

        signedSubresultantSequence_helper("x^11-x^10+1", "11*x^10-10*x^9",
                "[x^11-x^10+1, 11*x^10-10*x^9, 10*x^9-121, -110*x+100, 0, 0, 0, 0, 0, 0, 2143588810*x-1948717100," +
                " -275311670611]");

        signedSubresultantSequence_helper("x^2", "1", "[x^2, 1, -1]");

        signedSubresultantSequence_fail_helper("0", "0");
        signedSubresultantSequence_fail_helper("0", "x");
        signedSubresultantSequence_fail_helper("x", "0");
        signedSubresultantSequence_fail_helper("x^2", "x^3");
        signedSubresultantSequence_fail_helper("x^2", "x^2");
    }

    private static void primitiveSignedPseudoRemainderSequence_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        aeq(readStrict(a).get().primitiveSignedPseudoRemainderSequence(readStrict(b).get()), output);
    }

    private static void primitiveSignedPseudoRemainderSequence_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().primitiveSignedPseudoRemainderSequence(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPrimitiveSignedPseudoRemainderSequence() {
        primitiveSignedPseudoRemainderSequence_helper("1", "0", "[1]");
        primitiveSignedPseudoRemainderSequence_helper("1", "1", "[1, 1]");
        primitiveSignedPseudoRemainderSequence_helper("1", "-17", "[1, -1]");

        primitiveSignedPseudoRemainderSequence_helper("x", "0", "[x]");
        primitiveSignedPseudoRemainderSequence_helper("x", "1", "[x, 1]");
        primitiveSignedPseudoRemainderSequence_helper("x", "x", "[x, x]");
        primitiveSignedPseudoRemainderSequence_helper("x", "-17", "[x, -1]");

        primitiveSignedPseudoRemainderSequence_helper("-17", "0", "[-1]");
        primitiveSignedPseudoRemainderSequence_helper("-17", "1", "[-1, 1]");
        primitiveSignedPseudoRemainderSequence_helper("-17", "-17", "[-1, -1]");

        primitiveSignedPseudoRemainderSequence_helper("x^2-4*x+7", "0", "[x^2-4*x+7]");
        primitiveSignedPseudoRemainderSequence_helper("x^2-4*x+7", "1", "[x^2-4*x+7, 1]");
        primitiveSignedPseudoRemainderSequence_helper("x^2-4*x+7", "x", "[x^2-4*x+7, x, -1]");
        primitiveSignedPseudoRemainderSequence_helper("x^2-4*x+7", "-17", "[x^2-4*x+7, -1]");
        primitiveSignedPseudoRemainderSequence_helper("x^2-4*x+7", "x^2-4*x+7", "[x^2-4*x+7, x^2-4*x+7]");

        primitiveSignedPseudoRemainderSequence_helper("-x^3-1", "0", "[-x^3-1]");
        primitiveSignedPseudoRemainderSequence_helper("-x^3-1", "1", "[-x^3-1, 1]");
        primitiveSignedPseudoRemainderSequence_helper("-x^3-1", "x", "[-x^3-1, x, 1]");
        primitiveSignedPseudoRemainderSequence_helper("-x^3-1", "-17", "[-x^3-1, -1]");
        primitiveSignedPseudoRemainderSequence_helper("-x^3-1", "x^2-4*x+7", "[-x^3-1, x^2-4*x+7, x-3, -1]");
        primitiveSignedPseudoRemainderSequence_helper("-x^3-1", "-x^3-1", "[-x^3-1, -x^3-1]");

        primitiveSignedPseudoRemainderSequence_helper("3*x^10", "0", "[x^10]");
        primitiveSignedPseudoRemainderSequence_helper("3*x^10", "1", "[x^10, 1]");
        primitiveSignedPseudoRemainderSequence_helper("3*x^10", "x", "[x^10, x]");
        primitiveSignedPseudoRemainderSequence_helper("3*x^10", "-17", "[x^10, -1]");
        primitiveSignedPseudoRemainderSequence_helper("3*x^10", "x^2-4*x+7", "[x^10, x^2-4*x+7, -7316*x+3591, -1]");
        primitiveSignedPseudoRemainderSequence_helper("3*x^10", "-x^3-1", "[x^10, -x^3-1, x, 1]");
        primitiveSignedPseudoRemainderSequence_helper("3*x^10", "3*x^10", "[x^10, x^10]");

        primitiveSignedPseudoRemainderSequence_helper("x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5", "3*x^6+5*x^4-4*x^2-9*x+21",
                "[x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5, 3*x^6+5*x^4-4*x^2-9*x+21, 5*x^4-x^2+3, 13*x^2+25*x-49," +
                " 4663*x-6150, -1]");
        primitiveSignedPseudoRemainderSequence_helper("x^11-x^10+1", "11*x^10-10*x^9",
                "[x^11-x^10+1, 11*x^10-10*x^9, 10*x^9-121, -11*x+10, 1]");
        primitiveSignedPseudoRemainderSequence_helper("x^2", "1", "[x^2, 1]");

        primitiveSignedPseudoRemainderSequence_fail_helper("0", "0");
        primitiveSignedPseudoRemainderSequence_fail_helper("0", "x");
        primitiveSignedPseudoRemainderSequence_fail_helper("x^2", "x^3");
    }

    private static void abbreviatedSignedSubresultantSequence_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        aeq(readStrict(a).get().abbreviatedSignedSubresultantSequence(readStrict(b).get()), output);
    }

    private static void abbreviatedSignedSubresultantSequence_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().abbreviatedSignedSubresultantSequence(readStrict(b).get());
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testAbbreviatedSignedSubresultantSequence() {
        abbreviatedSignedSubresultantSequence_helper("x", "1", "[x, 1]");
        abbreviatedSignedSubresultantSequence_helper("x", "-17", "[x, -17]");

        abbreviatedSignedSubresultantSequence_helper("x^2-4*x+7", "1", "[x^2-4*x+7]");
        abbreviatedSignedSubresultantSequence_helper("x^2-4*x+7", "x", "[x^2-4*x+7, x, -7]");
        abbreviatedSignedSubresultantSequence_helper("x^2-4*x+7", "-17", "[x^2-4*x+7, -17]");

        abbreviatedSignedSubresultantSequence_helper("-x^3-1", "1", "[-x^3-1]");
        abbreviatedSignedSubresultantSequence_helper("-x^3-1", "x", "[-x^3-1, -1]");
        abbreviatedSignedSubresultantSequence_helper("-x^3-1", "-17", "[-x^3-1]");
        abbreviatedSignedSubresultantSequence_helper("-x^3-1", "x^2-4*x+7", "[-x^3-1, x^2-4*x+7, 9*x-27, -324]");

        abbreviatedSignedSubresultantSequence_helper("3*x^10", "1", "[3*x^10]");
        abbreviatedSignedSubresultantSequence_helper("3*x^10", "x", "[3*x^10, x]");
        abbreviatedSignedSubresultantSequence_helper("3*x^10", "-17", "[3*x^10, -17]");
        abbreviatedSignedSubresultantSequence_helper("3*x^10", "x^2-4*x+7",
                "[3*x^10, x^2-4*x+7, -21948*x+10773, -2542277241]");
        abbreviatedSignedSubresultantSequence_helper("3*x^10", "-x^3-1", "[3*x^10, -3*x, -27]");

        abbreviatedSignedSubresultantSequence_helper("x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5", "3*x^6+5*x^4-4*x^2-9*x+21",
                "[x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5, -9326*x+12300, 260708]");

        abbreviatedSignedSubresultantSequence_helper("x^11-x^10+1", "11*x^10-10*x^9",
                "[x^11-x^10+1, 11*x^10-10*x^9, 10*x^9-121, -275311670611]");

        abbreviatedSignedSubresultantSequence_helper("x^2", "1", "[x^2]");

        abbreviatedSignedSubresultantSequence_fail_helper("0", "0");
        abbreviatedSignedSubresultantSequence_fail_helper("0", "x");
        abbreviatedSignedSubresultantSequence_fail_helper("x", "0");
        abbreviatedSignedSubresultantSequence_fail_helper("x^2", "x^3");
        abbreviatedSignedSubresultantSequence_fail_helper("x^2", "x^2");
    }

    private static void rootBound_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().rootBound(), output);
    }

    private static void rootBound_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().rootBound();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRootBound() {
        rootBound_helper("1", "[0, 0]");
        rootBound_helper("x", "[-1, 1]");
        rootBound_helper("-17", "[0, 0]");
        rootBound_helper("x^2-4*x+7", "[-8, 8]");
        rootBound_helper("-x^3-1", "[-2, 2]");
        rootBound_helper("3*x^10", "[-1, 1]");

        rootBound_helper("x^2-2", "[-3, 3]");
        rootBound_helper("x^2-3", "[-4, 4]");
        rootBound_helper("x^2-x-1", "[-2, 2]");
        rootBound_helper("x^5-x+1", "[-2, 2]");
        rootBound_helper("-x^4+x", "[-2, 2]");

        rootBound_fail_helper("0");
    }

    private static void powerOfTwoRootBound_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().powerOfTwoRootBound(), output);
    }

    private static void powerOfTwoRootBound_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().powerOfTwoRootBound();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPowerOfTwoRootBound() {
        powerOfTwoRootBound_helper("1", "[-1, 1]");
        powerOfTwoRootBound_helper("x", "[-1, 1]");
        powerOfTwoRootBound_helper("-17", "[-1, 1]");
        powerOfTwoRootBound_helper("x^2-4*x+7", "[-8, 8]");
        powerOfTwoRootBound_helper("-x^3-1", "[-2, 2]");
        powerOfTwoRootBound_helper("3*x^10", "[-1, 1]");

        powerOfTwoRootBound_helper("x^2-2", "[-4, 4]");
        powerOfTwoRootBound_helper("x^2-3", "[-4, 4]");
        powerOfTwoRootBound_helper("x^2-x-1", "[-2, 2]");
        powerOfTwoRootBound_helper("x^5-x+1", "[-2, 2]");
        powerOfTwoRootBound_helper("-x^4+x", "[-2, 2]");

        powerOfTwoRootBound_fail_helper("0");
    }

    private static void rootCount_Interval_helper(@NotNull String p, @NotNull String i, int output) {
        assertTrue(readStrict(p).get().isSquareFree());
        aeq(readStrict(p).get().rootCount(Interval.readStrict(i).get()), output);
    }

    private static void rootCount_Interval_fail_helper(@NotNull String p, @NotNull String i) {
        try {
            readStrict(p).get().rootCount(Interval.readStrict(i).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRootCount_Interval() {
        rootCount_Interval_helper("1", "(-Infinity, Infinity)", 0);
        rootCount_Interval_helper("1", "[0, 0]", 0);
        rootCount_Interval_helper("1", "[0, 10]", 0);

        rootCount_Interval_helper("x", "(-Infinity, Infinity)", 1);
        rootCount_Interval_helper("x", "[0, 0]", 1);
        rootCount_Interval_helper("x", "[0, 10]", 1);
        rootCount_Interval_helper("x", "[1, 10]", 0);

        rootCount_Interval_helper("x^2-2", "(-Infinity, Infinity)", 2);
        rootCount_Interval_helper("x^2-2", "[0, Infinity)", 1);
        rootCount_Interval_helper("x^2-2", "[7/5, 8/5]", 1);

        rootCount_Interval_helper("x^5-x+1", "(-Infinity, Infinity)", 1);
        rootCount_Interval_helper("x^5-x+1", "[0, 0]", 0);
        rootCount_Interval_helper("x^5-x+1", "[-5/4, 9/8]", 1);

        rootCount_Interval_fail_helper("0", "[0, 1]");
    }

    private static void rootCount_helper(@NotNull Polynomial input, int output) {
        assertTrue(input.isSquareFree());
        aeq(input.rootCount(), output);
    }

    private static void rootCount_helper(@NotNull String input, int output) {
        rootCount_helper(readStrict(input).get(), output);
    }

    private static void rootCount_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().rootCount();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRootCount() {
        rootCount_helper("1", 0);
        rootCount_helper("x", 1);
        rootCount_helper("-17", 0);
        rootCount_helper("x^2-4*x+7", 0);
        rootCount_helper("-x^3-1", 1);

        rootCount_helper("x^2-2", 2);
        rootCount_helper("x^2-3", 2);
        rootCount_helper("x^2-x-1", 2);
        rootCount_helper("x^5-x+1", 1);
        rootCount_helper("-x^4+x", 2);

        Polynomial wilkinsonsPolynomial = ONE;
        for (int i = 1; i <= 20; i++) {
            wilkinsonsPolynomial = wilkinsonsPolynomial.multiply(
                    of(Arrays.asList(BigInteger.valueOf(-i), BigInteger.ONE))
            );
        }
        rootCount_helper(wilkinsonsPolynomial, 20);

        List<Rational> coefficients = toList(wilkinsonsPolynomial.toRationalPolynomial());
        coefficients.set(19, coefficients.get(19).subtract(Rational.ONE.shiftRight(23)));
        Polynomial perturbedWilkinsonsPolynomial = RationalPolynomial.of(coefficients).constantFactor().b;
        rootCount_helper(perturbedWilkinsonsPolynomial, 10);

        rootCount_fail_helper("0");
    }

    private static void isolatingInterval_helper(@NotNull String p, int rootIndex, @NotNull String output) {
        aeq(readStrict(p).get().isolatingInterval(rootIndex), output);
    }

    private static void isolatingInterval_fail_helper(@NotNull String p, int rootIndex) {
        try {
            readStrict(p).get().isolatingInterval(rootIndex);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIsolatingInterval() {
        isolatingInterval_helper("x", 0, "[-1, 1]");
        isolatingInterval_helper("-x^3-1", 0, "[-2, 2]");
        isolatingInterval_helper("x^2-2", 0, "[-3, 0]");
        isolatingInterval_helper("x^2-2", 1, "[0, 3]");
        isolatingInterval_helper("x^2-3", 0, "[-4, 0]");
        isolatingInterval_helper("x^2-3", 1, "[0, 4]");
        isolatingInterval_helper("x^2-x-1", 0, "[-2, 0]");
        isolatingInterval_helper("x^2-x-1", 1, "[0, 2]");
        isolatingInterval_helper("x^5-x+1", 0, "[-2, 2]");
        isolatingInterval_helper("-x^4+x", 0, "[-2, 0]");
        isolatingInterval_helper("-x^4+x", 1, "[1/2, 1]");

        isolatingInterval_fail_helper("0", 0);
        isolatingInterval_fail_helper("1", 0);
        isolatingInterval_fail_helper("x", 1);
        isolatingInterval_fail_helper("x^2-2", 2);
        isolatingInterval_fail_helper("x^2-2", -1);
    }

    private static void powerOfTwoIsolatingInterval_helper(@NotNull String p, int rootIndex, @NotNull String output) {
        aeq(readStrict(p).get().powerOfTwoIsolatingInterval(rootIndex), output);
    }

    private static void powerOfTwoIsolatingInterval_fail_helper(@NotNull String p, int rootIndex) {
        try {
            readStrict(p).get().powerOfTwoIsolatingInterval(rootIndex);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPowerOfTwoIsolatingInterval() {
        powerOfTwoIsolatingInterval_helper("x", 0, "[-1, 1]");
        powerOfTwoIsolatingInterval_helper("-x^3-1", 0, "[-2, 2]");
        powerOfTwoIsolatingInterval_helper("x^2-2", 0, "[-4, 0]");
        powerOfTwoIsolatingInterval_helper("x^2-2", 1, "[0, 4]");
        powerOfTwoIsolatingInterval_helper("x^2-3", 0, "[-4, 0]");
        powerOfTwoIsolatingInterval_helper("x^2-3", 1, "[0, 4]");
        powerOfTwoIsolatingInterval_helper("x^2-x-1", 0, "[-2, 0]");
        powerOfTwoIsolatingInterval_helper("x^2-x-1", 1, "[0, 2]");
        powerOfTwoIsolatingInterval_helper("x^5-x+1", 0, "[-2, 2]");
        powerOfTwoIsolatingInterval_helper("-x^4+x", 0, "[-2, 0]");
        powerOfTwoIsolatingInterval_helper("-x^4+x", 1, "[1/2, 1]");

        powerOfTwoIsolatingInterval_fail_helper("0", 0);
        powerOfTwoIsolatingInterval_fail_helper("1", 0);
        powerOfTwoIsolatingInterval_fail_helper("x", 1);
        powerOfTwoIsolatingInterval_fail_helper("x^2-2", 2);
        powerOfTwoIsolatingInterval_fail_helper("x^2-2", -1);
    }

    private static void isolatingIntervals_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().isolatingIntervals(), output);
    }

    private static void isolatingIntervals_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().isolatingIntervals();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIsolatingIntervals() {
        isolatingIntervals_helper("1", "[]");
        isolatingIntervals_helper("x", "[[-1, 1]]");
        isolatingIntervals_helper("-17", "[]");
        isolatingIntervals_helper("x^2-4*x+7", "[]");
        isolatingIntervals_helper("-x^3-1", "[[-2, 2]]");
        isolatingIntervals_helper("3*x^10", "[[-1, 1]]");

        isolatingIntervals_helper("x^2-2", "[[-3, 0], [0, 3]]");
        isolatingIntervals_helper("x^2-3", "[[-4, 0], [0, 4]]");
        isolatingIntervals_helper("x^2-x-1", "[[-2, 0], [0, 2]]");
        isolatingIntervals_helper("x^5-x+1", "[[-2, 2]]");
        isolatingIntervals_helper("-x^4+x", "[[-2, 0], [1/2, 1]]");

        isolatingIntervals_fail_helper("0");
    }

    private static void powerOfTwoIsolatingIntervals_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().powerOfTwoIsolatingIntervals(), output);
    }

    private static void powerOfTwoIsolatingIntervals_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().powerOfTwoIsolatingIntervals();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPowerOfTwoIsolatingIntervals() {
        powerOfTwoIsolatingIntervals_helper("1", "[]");
        powerOfTwoIsolatingIntervals_helper("x", "[[-1, 1]]");
        powerOfTwoIsolatingIntervals_helper("-17", "[]");
        powerOfTwoIsolatingIntervals_helper("x^2-4*x+7", "[]");
        powerOfTwoIsolatingIntervals_helper("-x^3-1", "[[-2, 2]]");
        powerOfTwoIsolatingIntervals_helper("3*x^10", "[[-1, 1]]");

        powerOfTwoIsolatingIntervals_helper("x^2-2", "[[-4, 0], [0, 4]]");
        powerOfTwoIsolatingIntervals_helper("x^2-3", "[[-4, 0], [0, 4]]");
        powerOfTwoIsolatingIntervals_helper("x^2-x-1", "[[-2, 0], [0, 2]]");
        powerOfTwoIsolatingIntervals_helper("x^5-x+1", "[[-2, 2]]");
        powerOfTwoIsolatingIntervals_helper("-x^4+x", "[[-2, 0], [1/2, 1]]");

        powerOfTwoIsolatingIntervals_fail_helper("0");
    }

    private static void reflect_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().reflect(), output);
    }

    @Test
    public void testReflect() {
        reflect_helper("0", "0");
        reflect_helper("1", "1");
        reflect_helper("x", "x");
        reflect_helper("-17", "-17");
        reflect_helper("x^2-4*x+7", "x^2+4*x+7");
        reflect_helper("-x^3-1", "-x^3+1");
        reflect_helper("3*x^10", "3*x^10");
    }

    private static void translate_helper(@NotNull String p, @NotNull String t, @NotNull String output) {
        aeq(readStrict(p).get().translate(Readers.readBigIntegerStrict(t).get()), output);
    }

    @Test
    public void testTranslate() {
        translate_helper("0", "0", "0");
        translate_helper("0", "1", "0");
        translate_helper("0", "-1", "0");
        translate_helper("0", "100", "0");

        translate_helper("1", "0", "1");
        translate_helper("1", "1", "1");
        translate_helper("1", "-1", "1");
        translate_helper("1", "100", "1");

        translate_helper("-17", "0", "-17");
        translate_helper("-17", "1", "-17");
        translate_helper("-17", "-1", "-17");
        translate_helper("-17", "100", "-17");

        translate_helper("x^2-4*x+7", "0", "x^2-4*x+7");
        translate_helper("x^2-4*x+7", "1", "x^2-6*x+12");
        translate_helper("x^2-4*x+7", "-1", "x^2-2*x+4");
        translate_helper("x^2-4*x+7", "100", "x^2-204*x+10407");

        translate_helper("-x^3-1", "0", "-x^3-1");
        translate_helper("-x^3-1", "1", "-x^3+3*x^2-3*x");
        translate_helper("-x^3-1", "-1", "-x^3-3*x^2-3*x-2");
        translate_helper("-x^3-1", "100", "-x^3+300*x^2-30000*x+999999");

        translate_helper("3*x^10", "0", "3*x^10");
        translate_helper("3*x^10", "1",
                "3*x^10-30*x^9+135*x^8-360*x^7+630*x^6-756*x^5+630*x^4-360*x^3+135*x^2-30*x+3");
        translate_helper("3*x^10", "-1",
                "3*x^10+30*x^9+135*x^8+360*x^7+630*x^6+756*x^5+630*x^4+360*x^3+135*x^2+30*x+3");
        translate_helper("3*x^10", "100",
                "3*x^10-3000*x^9+1350000*x^8-360000000*x^7+63000000000*x^6-7560000000000*x^5+630000000000000*x^4-" +
                "36000000000000000*x^3+1350000000000000000*x^2-30000000000000000000*x+300000000000000000000");

        translate_helper("x+1", "1", "x");
    }

    private static void specialTranslate_helper(@NotNull String p, @NotNull String t, @NotNull String output) {
        aeq(readStrict(p).get().specialTranslate(Rational.readStrict(t).get()), output);
    }

    @Test
    public void testSpecialTranslate() {
        specialTranslate_helper("0", "0", "0");
        specialTranslate_helper("0", "1", "0");
        specialTranslate_helper("0", "-1", "0");
        specialTranslate_helper("0", "100/3", "0");
        specialTranslate_helper("0", "1/100", "0");

        specialTranslate_helper("1", "0", "1");
        specialTranslate_helper("1", "1", "1");
        specialTranslate_helper("1", "-1", "1");
        specialTranslate_helper("1", "100/3", "1");
        specialTranslate_helper("1", "1/100", "1");

        specialTranslate_helper("-17", "0", "-17");
        specialTranslate_helper("-17", "1", "-17");
        specialTranslate_helper("-17", "-1", "-17");
        specialTranslate_helper("-17", "100/3", "-17");
        specialTranslate_helper("-17", "1/100", "-17");

        specialTranslate_helper("x^2-4*x+7", "0", "x^2-4*x+7");
        specialTranslate_helper("x^2-4*x+7", "1", "x^2-6*x+12");
        specialTranslate_helper("x^2-4*x+7", "-1", "x^2-2*x+4");
        specialTranslate_helper("x^2-4*x+7", "100/3", "9*x^2-636*x+11263");
        specialTranslate_helper("x^2-4*x+7", "1/100", "10000*x^2-40200*x+70401");

        specialTranslate_helper("-x^3-1", "0", "-x^3-1");
        specialTranslate_helper("-x^3-1", "1", "-x^3+3*x^2-3*x");
        specialTranslate_helper("-x^3-1", "-1", "-x^3-3*x^2-3*x-2");
        specialTranslate_helper("-x^3-1", "100/3", "-27*x^3+2700*x^2-90000*x+999973");
        specialTranslate_helper("-x^3-1", "1/100", "-1000000*x^3+30000*x^2-300*x-999999");

        specialTranslate_helper("3*x^10", "0", "3*x^10");
        specialTranslate_helper("3*x^10", "1",
                "3*x^10-30*x^9+135*x^8-360*x^7+630*x^6-756*x^5+630*x^4-360*x^3+135*x^2-30*x+3");
        specialTranslate_helper("3*x^10", "-1",
                "3*x^10+30*x^9+135*x^8+360*x^7+630*x^6+756*x^5+630*x^4+360*x^3+135*x^2+30*x+3");
        specialTranslate_helper("3*x^10", "100/3",
                "177147*x^10-59049000*x^9+8857350000*x^8-787320000000*x^7+45927000000000*x^6-1837080000000000*x^5+" +
                "51030000000000000*x^4-972000000000000000*x^3+12150000000000000000*x^2-90000000000000000000*x+" +
                "300000000000000000000");
        specialTranslate_helper("3*x^10", "1/100",
                "300000000000000000000*x^10-30000000000000000000*x^9+1350000000000000000*x^8-36000000000000000*x^7+" +
                "630000000000000*x^6-7560000000000*x^5+63000000000*x^4-360000000*x^3+1350000*x^2-3000*x+3");

        specialTranslate_helper("2*x+1", "1/2", "4*x");
    }

    private static void positivePrimitiveTranslate_helper(
            @NotNull String p,
            @NotNull String t,
            @NotNull String output
    ) {
        aeq(readStrict(p).get().positivePrimitiveTranslate(Rational.readStrict(t).get()), output);
    }

    @Test
    public void testPositivePrimitiveTranslate() {
        positivePrimitiveTranslate_helper("0", "0", "0");
        positivePrimitiveTranslate_helper("0", "1", "0");
        positivePrimitiveTranslate_helper("0", "-1", "0");
        positivePrimitiveTranslate_helper("0", "100/3", "0");
        positivePrimitiveTranslate_helper("0", "1/100", "0");

        positivePrimitiveTranslate_helper("1", "0", "1");
        positivePrimitiveTranslate_helper("1", "1", "1");
        positivePrimitiveTranslate_helper("1", "-1", "1");
        positivePrimitiveTranslate_helper("1", "100/3", "1");
        positivePrimitiveTranslate_helper("1", "1/100", "1");

        positivePrimitiveTranslate_helper("-17", "0", "1");
        positivePrimitiveTranslate_helper("-17", "1", "1");
        positivePrimitiveTranslate_helper("-17", "-1", "1");
        positivePrimitiveTranslate_helper("-17", "100/3", "1");
        positivePrimitiveTranslate_helper("-17", "1/100", "1");

        positivePrimitiveTranslate_helper("x^2-4*x+7", "0", "x^2-4*x+7");
        positivePrimitiveTranslate_helper("x^2-4*x+7", "1", "x^2-6*x+12");
        positivePrimitiveTranslate_helper("x^2-4*x+7", "-1", "x^2-2*x+4");
        positivePrimitiveTranslate_helper("x^2-4*x+7", "100/3", "9*x^2-636*x+11263");
        positivePrimitiveTranslate_helper("x^2-4*x+7", "1/100", "10000*x^2-40200*x+70401");

        positivePrimitiveTranslate_helper("-x^3-1", "0", "x^3+1");
        positivePrimitiveTranslate_helper("-x^3-1", "1", "x^3-3*x^2+3*x");
        positivePrimitiveTranslate_helper("-x^3-1", "-1", "x^3+3*x^2+3*x+2");
        positivePrimitiveTranslate_helper("-x^3-1", "100/3", "27*x^3-2700*x^2+90000*x-999973");
        positivePrimitiveTranslate_helper("-x^3-1", "1/100", "1000000*x^3-30000*x^2+300*x+999999");

        positivePrimitiveTranslate_helper("3*x^10", "0", "x^10");
        positivePrimitiveTranslate_helper("3*x^10", "1",
                "x^10-10*x^9+45*x^8-120*x^7+210*x^6-252*x^5+210*x^4-120*x^3+45*x^2-10*x+1");
        positivePrimitiveTranslate_helper("3*x^10", "-1",
                "x^10+10*x^9+45*x^8+120*x^7+210*x^6+252*x^5+210*x^4+120*x^3+45*x^2+10*x+1");
        positivePrimitiveTranslate_helper("3*x^10", "100/3",
                "59049*x^10-19683000*x^9+2952450000*x^8-262440000000*x^7+15309000000000*x^6-612360000000000*x^5+" +
                "17010000000000000*x^4-324000000000000000*x^3+4050000000000000000*x^2-30000000000000000000*x+" +
                "100000000000000000000");
        positivePrimitiveTranslate_helper("3*x^10", "1/100",
                "100000000000000000000*x^10-10000000000000000000*x^9+450000000000000000*x^8-12000000000000000*x^7+" +
                "210000000000000*x^6-2520000000000*x^5+21000000000*x^4-120000000*x^3+450000*x^2-1000*x+1");

        positivePrimitiveTranslate_helper("2*x+1", "1/2", "x");
    }

    private static void stretch_helper(@NotNull String p, @NotNull String f, @NotNull String output) {
        aeq(readStrict(p).get().stretch(Rational.readStrict(f).get()), output);
    }

    private static void stretch_fail_helper(@NotNull String p, @NotNull String f) {
        try {
            readStrict(p).get().stretch(Rational.readStrict(f).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testStretch() {
        stretch_helper("0", "1", "0");
        stretch_helper("0", "2", "0");
        stretch_helper("0", "1/2", "0");
        stretch_helper("0", "100/3", "0");
        stretch_helper("0", "1/100", "0");

        stretch_helper("1", "1", "1");
        stretch_helper("1", "2", "1");
        stretch_helper("1", "1/2", "1");
        stretch_helper("1", "100/3", "1");
        stretch_helper("1", "1/100", "1");

        stretch_helper("-17", "1", "-17");
        stretch_helper("-17", "2", "-17");
        stretch_helper("-17", "1/2", "-17");
        stretch_helper("-17", "100/3", "-17");
        stretch_helper("-17", "1/100", "-17");

        stretch_helper("x^2-4*x+7", "1", "x^2-4*x+7");
        stretch_helper("x^2-4*x+7", "2", "x^2-8*x+28");
        stretch_helper("x^2-4*x+7", "1/2", "4*x^2-8*x+7");
        stretch_helper("x^2-4*x+7", "100/3", "9*x^2-1200*x+70000");
        stretch_helper("x^2-4*x+7", "1/100", "10000*x^2-400*x+7");

        stretch_helper("-x^3-1", "1", "-x^3-1");
        stretch_helper("-x^3-1", "2", "-x^3-8");
        stretch_helper("-x^3-1", "1/2", "-8*x^3-1");
        stretch_helper("-x^3-1", "100/3", "-27*x^3-1000000");
        stretch_helper("-x^3-1", "1/100", "-1000000*x^3-1");

        stretch_helper("3*x^10", "1", "3*x^10");
        stretch_helper("3*x^10", "2", "3*x^10");
        stretch_helper("3*x^10", "1/2", "3072*x^10");
        stretch_helper("3*x^10", "100/3", "177147*x^10");
        stretch_helper("3*x^10", "1/100", "300000000000000000000*x^10");

        stretch_helper("2*x-1", "2", "2*x-2");
        stretch_helper("x-2", "1/2", "2*x-2");

        stretch_fail_helper("x^2-4*x+7", "0");
        stretch_fail_helper("x^2-4*x+7", "-1");
    }

    private static void positivePrimitiveStretch_helper(@NotNull String p, @NotNull String f, @NotNull String output) {
        aeq(readStrict(p).get().positivePrimitiveStretch(Rational.readStrict(f).get()), output);
    }

    private static void positivePrimitiveStretch_fail_helper(@NotNull String p, @NotNull String f) {
        try {
            readStrict(p).get().positivePrimitiveStretch(Rational.readStrict(f).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPositivePrimitiveStretch() {
        positivePrimitiveStretch_helper("0", "1", "0");
        positivePrimitiveStretch_helper("0", "2", "0");
        positivePrimitiveStretch_helper("0", "1/2", "0");
        positivePrimitiveStretch_helper("0", "100/3", "0");
        positivePrimitiveStretch_helper("0", "1/100", "0");

        positivePrimitiveStretch_helper("1", "1", "1");
        positivePrimitiveStretch_helper("1", "2", "1");
        positivePrimitiveStretch_helper("1", "1/2", "1");
        positivePrimitiveStretch_helper("1", "100/3", "1");
        positivePrimitiveStretch_helper("1", "1/100", "1");

        positivePrimitiveStretch_helper("-17", "1", "1");
        positivePrimitiveStretch_helper("-17", "2", "1");
        positivePrimitiveStretch_helper("-17", "1/2", "1");
        positivePrimitiveStretch_helper("-17", "100/3", "1");
        positivePrimitiveStretch_helper("-17", "1/100", "1");

        positivePrimitiveStretch_helper("x^2-4*x+7", "1", "x^2-4*x+7");
        positivePrimitiveStretch_helper("x^2-4*x+7", "2", "x^2-8*x+28");
        positivePrimitiveStretch_helper("x^2-4*x+7", "1/2", "4*x^2-8*x+7");
        positivePrimitiveStretch_helper("x^2-4*x+7", "100/3", "9*x^2-1200*x+70000");
        positivePrimitiveStretch_helper("x^2-4*x+7", "1/100", "10000*x^2-400*x+7");

        positivePrimitiveStretch_helper("-x^3-1", "1", "x^3+1");
        positivePrimitiveStretch_helper("-x^3-1", "2", "x^3+8");
        positivePrimitiveStretch_helper("-x^3-1", "1/2", "8*x^3+1");
        positivePrimitiveStretch_helper("-x^3-1", "100/3", "27*x^3+1000000");
        positivePrimitiveStretch_helper("-x^3-1", "1/100", "1000000*x^3+1");

        positivePrimitiveStretch_helper("3*x^10", "1", "x^10");
        positivePrimitiveStretch_helper("3*x^10", "2", "x^10");
        positivePrimitiveStretch_helper("3*x^10", "1/2", "x^10");
        positivePrimitiveStretch_helper("3*x^10", "100/3", "x^10");
        positivePrimitiveStretch_helper("3*x^10", "1/100", "x^10");

        positivePrimitiveStretch_helper("2*x-1", "2", "x-1");
        positivePrimitiveStretch_helper("x-2", "1/2", "x-1");

        positivePrimitiveStretch_fail_helper("x^2-4*x+7", "0");
        positivePrimitiveStretch_fail_helper("x^2-4*x+7", "-1");
    }

    private static void shiftRootsLeft_helper(@NotNull String p, int bits, @NotNull String output) {
        aeq(readStrict(p).get().shiftRootsLeft(bits), output);
    }

    private static void shiftRootsLeft_fail_helper(@NotNull String p, int bits) {
        try {
            readStrict(p).get().shiftRootsLeft(bits);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testShiftRootLeft() {
        shiftRootsLeft_helper("0", 0, "0");
        shiftRootsLeft_helper("0", 1, "0");
        shiftRootsLeft_helper("0", 2, "0");

        shiftRootsLeft_helper("1", 0, "1");
        shiftRootsLeft_helper("1", 1, "1");
        shiftRootsLeft_helper("1", 2, "1");

        shiftRootsLeft_helper("-17", 0, "-17");
        shiftRootsLeft_helper("-17", 1, "-17");
        shiftRootsLeft_helper("-17", 2, "-17");

        shiftRootsLeft_helper("x^2-4*x+7", 0, "x^2-4*x+7");
        shiftRootsLeft_helper("x^2-4*x+7", 1, "x^2-8*x+28");
        shiftRootsLeft_helper("x^2-4*x+7", 2, "x^2-16*x+112");

        shiftRootsLeft_helper("-x^3-1", 0, "-x^3-1");
        shiftRootsLeft_helper("-x^3-1", 1, "-x^3-8");
        shiftRootsLeft_helper("-x^3-1", 2, "-x^3-64");

        shiftRootsLeft_helper("3*x^10", 0, "3*x^10");
        shiftRootsLeft_helper("3*x^10", 1, "3*x^10");
        shiftRootsLeft_helper("3*x^10", 2, "3*x^10");

        shiftRootsLeft_helper("2*x-1", 1, "2*x-2");

        shiftRootsLeft_fail_helper("0", -1);
        shiftRootsLeft_fail_helper("x", -1);
    }

    private static void shiftRootsRight_helper(@NotNull String p, int bits, @NotNull String output) {
        aeq(readStrict(p).get().shiftRootsRight(bits), output);
    }

    private static void shiftRootsRight_fail_helper(@NotNull String p, int bits) {
        try {
            readStrict(p).get().shiftRootsRight(bits);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testShiftRootRight() {
        shiftRootsRight_helper("0", 0, "0");
        shiftRootsRight_helper("0", 1, "0");
        shiftRootsRight_helper("0", 2, "0");

        shiftRootsRight_helper("1", 0, "1");
        shiftRootsRight_helper("1", 1, "1");
        shiftRootsRight_helper("1", 2, "1");

        shiftRootsRight_helper("-17", 0, "-17");
        shiftRootsRight_helper("-17", 1, "-17");
        shiftRootsRight_helper("-17", 2, "-17");

        shiftRootsRight_helper("x^2-4*x+7", 0, "x^2-4*x+7");
        shiftRootsRight_helper("x^2-4*x+7", 1, "4*x^2-8*x+7");
        shiftRootsRight_helper("x^2-4*x+7", 2, "16*x^2-16*x+7");

        shiftRootsRight_helper("-x^3-1", 0, "-x^3-1");
        shiftRootsRight_helper("-x^3-1", 1, "-8*x^3-1");
        shiftRootsRight_helper("-x^3-1", 2, "-64*x^3-1");

        shiftRootsRight_helper("3*x^10", 0, "3*x^10");
        shiftRootsRight_helper("3*x^10", 1, "3072*x^10");
        shiftRootsRight_helper("3*x^10", 2, "3145728*x^10");

        shiftRootsRight_helper("x-2", 1, "2*x-2");

        shiftRootsRight_fail_helper("0", -1);
        shiftRootsRight_fail_helper("x", -1);
    }

    private static void positivePrimitiveShiftRootsLeft_helper(@NotNull String p, int bits, @NotNull String output) {
        aeq(readStrict(p).get().positivePrimitiveShiftRootsLeft(bits), output);
    }

    private static void positivePrimitiveShiftRootsLeft_fail_helper(@NotNull String p, int bits) {
        try {
            readStrict(p).get().positivePrimitiveShiftRootsLeft(bits);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPositivePrimitiveShiftRootLeft() {
        positivePrimitiveShiftRootsLeft_helper("0", 0, "0");
        positivePrimitiveShiftRootsLeft_helper("0", 1, "0");
        positivePrimitiveShiftRootsLeft_helper("0", 2, "0");

        positivePrimitiveShiftRootsLeft_helper("1", 0, "1");
        positivePrimitiveShiftRootsLeft_helper("1", 1, "1");
        positivePrimitiveShiftRootsLeft_helper("1", 2, "1");

        positivePrimitiveShiftRootsLeft_helper("-17", 0, "1");
        positivePrimitiveShiftRootsLeft_helper("-17", 1, "1");
        positivePrimitiveShiftRootsLeft_helper("-17", 2, "1");

        positivePrimitiveShiftRootsLeft_helper("x^2-4*x+7", 0, "x^2-4*x+7");
        positivePrimitiveShiftRootsLeft_helper("x^2-4*x+7", 1, "x^2-8*x+28");
        positivePrimitiveShiftRootsLeft_helper("x^2-4*x+7", 2, "x^2-16*x+112");

        positivePrimitiveShiftRootsLeft_helper("-x^3-1", 0, "x^3+1");
        positivePrimitiveShiftRootsLeft_helper("-x^3-1", 1, "x^3+8");
        positivePrimitiveShiftRootsLeft_helper("-x^3-1", 2, "x^3+64");

        positivePrimitiveShiftRootsLeft_helper("3*x^10", 0, "x^10");
        positivePrimitiveShiftRootsLeft_helper("3*x^10", 1, "x^10");
        positivePrimitiveShiftRootsLeft_helper("3*x^10", 2, "x^10");

        positivePrimitiveShiftRootsLeft_helper("2*x-1", 1, "x-1");

        positivePrimitiveShiftRootsLeft_fail_helper("0", -1);
        positivePrimitiveShiftRootsLeft_fail_helper("x", -1);
    }

    private static void positivePrimitiveShiftRootsRight_helper(@NotNull String p, int bits, @NotNull String output) {
        aeq(readStrict(p).get().positivePrimitiveShiftRootsRight(bits), output);
    }

    private static void positivePrimitiveShiftRootsRight_fail_helper(@NotNull String p, int bits) {
        try {
            readStrict(p).get().positivePrimitiveShiftRootsRight(bits);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPositivePrimitiveShiftRootRight() {
        positivePrimitiveShiftRootsRight_helper("0", 0, "0");
        positivePrimitiveShiftRootsRight_helper("0", 1, "0");
        positivePrimitiveShiftRootsRight_helper("0", 2, "0");

        positivePrimitiveShiftRootsRight_helper("1", 0, "1");
        positivePrimitiveShiftRootsRight_helper("1", 1, "1");
        positivePrimitiveShiftRootsRight_helper("1", 2, "1");

        positivePrimitiveShiftRootsRight_helper("-17", 0, "1");
        positivePrimitiveShiftRootsRight_helper("-17", 1, "1");
        positivePrimitiveShiftRootsRight_helper("-17", 2, "1");

        positivePrimitiveShiftRootsRight_helper("x^2-4*x+7", 0, "x^2-4*x+7");
        positivePrimitiveShiftRootsRight_helper("x^2-4*x+7", 1, "4*x^2-8*x+7");
        positivePrimitiveShiftRootsRight_helper("x^2-4*x+7", 2, "16*x^2-16*x+7");

        positivePrimitiveShiftRootsRight_helper("-x^3-1", 0, "x^3+1");
        positivePrimitiveShiftRootsRight_helper("-x^3-1", 1, "8*x^3+1");
        positivePrimitiveShiftRootsRight_helper("-x^3-1", 2, "64*x^3+1");

        positivePrimitiveShiftRootsRight_helper("3*x^10", 0, "x^10");
        positivePrimitiveShiftRootsRight_helper("3*x^10", 1, "x^10");
        positivePrimitiveShiftRootsRight_helper("3*x^10", 2, "x^10");

        positivePrimitiveShiftRootsRight_helper("x-2", 1, "x-1");

        positivePrimitiveShiftRootsRight_fail_helper("0", -1);
        positivePrimitiveShiftRootsRight_fail_helper("x", -1);
    }

    private static void invertRoots_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().invertRoots(), output);
    }

    @Test
    public void testInvertRoots() {
        invertRoots_helper("0", "0");
        invertRoots_helper("1", "1");
        invertRoots_helper("x", "1");
        invertRoots_helper("-17", "17");
        invertRoots_helper("x^2-4*x+7", "7*x^2-4*x+1");
        invertRoots_helper("-x^3-1", "x^3+1");
        invertRoots_helper("3*x^10", "3");
    }

    private static void addRoots_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().addRoots(readStrict(b).get()), output);
    }

    @Test
    public void testAddRoots() {
        addRoots_helper("0", "0", "1");
        addRoots_helper("1", "0", "1");
        addRoots_helper("x", "0", "1");
        addRoots_helper("x^2-2", "0", "1");
        addRoots_helper("x^2-3", "0", "1");
        addRoots_helper("x^2-x-1", "0", "1");
        addRoots_helper("x^5-x+1", "0", "1");

        addRoots_helper("1", "x", "1");
        addRoots_helper("x", "x", "x");
        addRoots_helper("x^2-2", "x", "x^2-2");
        addRoots_helper("x^2-3", "x", "x^2-3");
        addRoots_helper("x^2-x-1", "x", "x^2-x-1");
        addRoots_helper("x^5-x+1", "x", "x^5-x+1");

        addRoots_helper("x^2-2", "x^2-2", "x^4-8*x^2");
        addRoots_helper("x^2-3", "x^2-2", "x^4-10*x^2+1");
        addRoots_helper("x^2-x-1", "x^2-2", "x^4-2*x^3-5*x^2+6*x-1");
        addRoots_helper("x^5-x+1", "x^2-2", "x^10-10*x^8+38*x^6+2*x^5-100*x^4+40*x^3+121*x^2+38*x-17");

        addRoots_helper("x^2-3", "x^2-3", "x^4-12*x^2");
        addRoots_helper("x^2-x-1", "x^2-3", "x^4-2*x^3-7*x^2+8*x+1");
        addRoots_helper("x^5-x+1", "x^2-3", "x^10-15*x^8+88*x^6+2*x^5-300*x^4+60*x^3+496*x^2+88*x-191");

        addRoots_helper("x^2-x-1", "x^2-x-1", "x^4-4*x^3+x^2+6*x-4");
        addRoots_helper("x^5-x+1", "x^2-x-1", "x^10-5*x^9+5*x^8+10*x^7-17*x^6-3*x^5-10*x^4+70*x^3-49*x^2+18*x-4");

        addRoots_helper("x^5-x+1", "x^5-x+1",
                "x^25-10*x^21+10*x^20-95*x^17+470*x^16-585*x^15-40*x^13+1280*x^12-4190*x^11+3830*x^10+400*x^9-" +
                "1760*x^8+760*x^7+2280*x^6+449*x^5+640*x^3+640*x^2+240*x+32");
    }

    private static void multiplyRoots_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().multiplyRoots(readStrict(b).get()), output);
    }

    @Test
    public void testMultiplyRoots() {
        multiplyRoots_helper("0", "0", "1");
        multiplyRoots_helper("1", "0", "1");
        multiplyRoots_helper("x", "0", "1");
        multiplyRoots_helper("x^2-2", "0", "1");
        multiplyRoots_helper("x^2-3", "0", "1");
        multiplyRoots_helper("x^2-x-1", "0", "1");
        multiplyRoots_helper("x^5-x+1", "0", "1");

        multiplyRoots_helper("1", "x", "1");
        multiplyRoots_helper("x", "x", "x");
        multiplyRoots_helper("x^2-2", "x", "x^2");
        multiplyRoots_helper("x^2-3", "x", "x^2");
        multiplyRoots_helper("x^2-x-1", "x", "x^2");
        multiplyRoots_helper("x^5-x+1", "x", "x^5");

        multiplyRoots_helper("x^2-2", "x^2-2", "x^4-8*x^2+16");
        multiplyRoots_helper("x^2-3", "x^2-2", "x^4-12*x^2+36");
        multiplyRoots_helper("x^2-x-1", "x^2-2", "x^4-6*x^2+4");
        multiplyRoots_helper("x^5-x+1", "x^2-2", "x^10-8*x^6+16*x^2-32");

        multiplyRoots_helper("x^2-3", "x^2-3", "x^4-18*x^2+81");
        multiplyRoots_helper("x^2-x-1", "x^2-3", "x^4-9*x^2+9");
        multiplyRoots_helper("x^5-x+1", "x^2-3", "x^10-18*x^6+81*x^2-243");

        multiplyRoots_helper("x^2-x-1", "x^2-x-1", "x^4-x^3-4*x^2-x+1");
        multiplyRoots_helper("x^5-x+1", "x^2-x-1", "x^10-7*x^6+11*x^5+x^2-x-1");

        multiplyRoots_helper("x^5-x+1", "x^5-x+1",
                "x^25-4*x^21-5*x^20+6*x^17+11*x^16+10*x^15-4*x^13-7*x^12-9*x^11-10*x^10+x^9+x^8+x^7+x^6+3*x^5+x-1");

        multiplyRoots_helper("x^2+x+1", "2*x^3+1", "4*x^6+4*x^3+1");
    }

    private static void powerTable_helper(@NotNull String p, int maxPower, @NotNull String output) {
        aeq(readStrict(p).get().powerTable(maxPower), output);
    }

    private static void powerTable_fail_helper(@NotNull String p, int maxPower) {
        try {
            readStrict(p).get().powerTable(maxPower);
            fail();
        } catch (UnsupportedOperationException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testPowerTable() {
        powerTable_helper("x^2-2", 0, "[1]");
        powerTable_helper("x^2-2", 1, "[1, x]");
        powerTable_helper("x^2-2", 2, "[1, x, 2]");
        powerTable_helper("x^2-2", 10, "[1, x, 2, 2*x, 4, 4*x, 8, 8*x, 16, 16*x, 32]");

        powerTable_helper("x", 10, "[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]");
        powerTable_helper("x+1", 10, "[1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1]");
        powerTable_helper("x-1", 10, "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]");
        powerTable_helper("x^2+2", 10, "[1, x, -2, -2*x, 4, 4*x, -8, -8*x, 16, 16*x, -32]");
        powerTable_helper("x^2-x-1", 10, "[1, x, x+1, 2*x+1, 3*x+2, 5*x+3, 8*x+5, 13*x+8, 21*x+13, 34*x+21, 55*x+34]");
        powerTable_helper("x^2+x+1", 10, "[1, x, -x-1, 1, x, -x-1, 1, x, -x-1, 1, x]");
        powerTable_helper("x^3-1", 10, "[1, x, x^2, 1, x, x^2, 1, x, x^2, 1, x]");
        powerTable_helper("x^5-x-1", 20,
                "[1, x, x^2, x^3, x^4, x+1, x^2+x, x^3+x^2, x^4+x^3, x^4+x+1, x^2+2*x+1, x^3+2*x^2+x, x^4+2*x^3+x^2," +
                " 2*x^4+x^3+x+1, x^4+x^2+3*x+2, x^3+3*x^2+3*x+1, x^4+3*x^3+3*x^2+x, 3*x^4+3*x^3+x^2+x+1," +
                " 3*x^4+x^3+x^2+4*x+3, x^4+x^3+4*x^2+6*x+3, x^4+4*x^3+6*x^2+4*x+1]");

        powerTable_fail_helper("0", 10);
        powerTable_fail_helper("1", 10);
        powerTable_fail_helper("-1", 10);
        powerTable_fail_helper("3", 10);
        powerTable_fail_helper("2*x+1", 10);
        powerTable_fail_helper("5*x^2", 10);
        powerTable_fail_helper("x^2-2", -1);
    }

    private static void rootPower_helper(@NotNull String x, int p, @NotNull String output) {
        aeq(readStrict(x).get().rootPower(p), output);
    }

    private static void rootPower_fail_helper(@NotNull String x, int p) {
        try {
            readStrict(x).get().rootPower(p);
            fail();
        } catch (UnsupportedOperationException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testRootPower() {
        rootPower_helper("x", 0, "1");
        rootPower_helper("x", 1, "0");
        rootPower_helper("x", 2, "0");
        rootPower_helper("x", 10, "0");
        rootPower_helper("x", 100, "0");
        rootPower_helper("x", 1000, "0");

        rootPower_helper("x+1", 0, "1");
        rootPower_helper("x+1", 1, "-1");
        rootPower_helper("x+1", 2, "1");
        rootPower_helper("x+1", 10, "1");
        rootPower_helper("x+1", 100, "1");
        rootPower_helper("x+1", 1000, "1");

        rootPower_helper("x-1", 0, "1");
        rootPower_helper("x-1", 1, "1");
        rootPower_helper("x-1", 2, "1");
        rootPower_helper("x-1", 10, "1");
        rootPower_helper("x-1", 100, "1");
        rootPower_helper("x-1", 1000, "1");

        rootPower_helper("x^2-2", 0, "1");
        rootPower_helper("x^2-2", 1, "x");
        rootPower_helper("x^2-2", 2, "2");
        rootPower_helper("x^2-2", 10, "32");
        rootPower_helper("x^2-2", 100, "1125899906842624");
        rootPower_helper("x^2-2", 1000,
                "327339060789614187001318969682759915221664204604306478948329136809613379640467455488327009232590415" +
                "7150886684127560071009217256545885393053328527589376");

        rootPower_helper("x^2+2", 0, "1");
        rootPower_helper("x^2+2", 1, "x");
        rootPower_helper("x^2+2", 2, "-2");
        rootPower_helper("x^2+2", 10, "-32");
        rootPower_helper("x^2+2", 100, "1125899906842624");
        rootPower_helper("x^2+2", 1000,
                "327339060789614187001318969682759915221664204604306478948329136809613379640467455488327009232590415" +
                "7150886684127560071009217256545885393053328527589376");

        rootPower_helper("x^2-x-1", 0, "1");
        rootPower_helper("x^2-x-1", 1, "x");
        rootPower_helper("x^2-x-1", 2, "x+1");
        rootPower_helper("x^2-x-1", 10, "55*x+34");
        rootPower_helper("x^2-x-1", 100, "354224848179261915075*x+218922995834555169026");

        rootPower_helper("x^2+x+1", 0, "1");
        rootPower_helper("x^2+x+1", 1, "x");
        rootPower_helper("x^2+x+1", 2, "-x-1");
        rootPower_helper("x^2+x+1", 10, "x");
        rootPower_helper("x^2+x+1", 100, "x");
        rootPower_helper("x^2+x+1", 1000, "x");

        rootPower_helper("x^3-1", 0, "1");
        rootPower_helper("x^3-1", 1, "x");
        rootPower_helper("x^3-1", 2, "x^2");
        rootPower_helper("x^3-1", 10, "x");
        rootPower_helper("x^3-1", 100, "x");
        rootPower_helper("x^3-1", 1000, "x");

        rootPower_helper("x^5-x-1", 0, "1");
        rootPower_helper("x^5-x-1", 1, "x");
        rootPower_helper("x^5-x-1", 2, "x^2");
        rootPower_helper("x^5-x-1", 10, "x^2+2*x+1");
        rootPower_helper("x^5-x-1", 100, "627401*x^4+735723*x^3+864339*x^2+1006897*x+540536");

        rootPower_fail_helper("0", 10);
        rootPower_fail_helper("1", 10);
        rootPower_fail_helper("-1", 10);
        rootPower_fail_helper("3", 10);
        rootPower_fail_helper("2*x+1", 10);
        rootPower_fail_helper("5*x^2", 10);
        rootPower_fail_helper("x^2-2", -1);
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readPolynomialList("[0, 1, x, -17, x^2-4*x+7, -x^3-1, 3*x^10]"),
                readPolynomialList("[0, 1, x, -17, x^2-4*x+7, -x^3-1, 3*x^10]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(readStrict(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("0", 1);
        hashCode_helper("1", 32);
        hashCode_helper("x", 962);
        hashCode_helper("-17", 14);
        hashCode_helper("x^2-4*x+7", 36395);
        hashCode_helper("-x^3-1", 893729);
        hashCode_helper("3*x^10", 129082722);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(readPolynomialList("[-x^3-1, -17, 0, 1, x, x^2-4*x+7, 3*x^10]"));
    }

    private static void readStrict_String_helper(@NotNull String input) {
        aeq(readStrict(input).get(), input);
    }

    private static void readStrict_String_fail_helper(@NotNull String input) {
        assertFalse(readStrict(input).isPresent());
    }

    @Test
    public void testReadStrict_String() {
        readStrict_String_helper("0");
        readStrict_String_helper("1");
        readStrict_String_helper("x");
        readStrict_String_helper("2");
        readStrict_String_helper("-2");
        readStrict_String_helper("-x");
        readStrict_String_helper("3*x");
        readStrict_String_helper("-3*x");
        readStrict_String_helper("x^2");
        readStrict_String_helper("-x^2");
        readStrict_String_helper("2*x^2");
        readStrict_String_helper("-2*x^2");
        readStrict_String_helper("x-1");
        readStrict_String_helper("-x-1");
        readStrict_String_helper("x^2-1");
        readStrict_String_helper("x^2-4*x+7");
        readStrict_String_helper("3*x^10");
        readStrict_String_fail_helper("");
        readStrict_String_fail_helper("+");
        readStrict_String_fail_helper("-");
        readStrict_String_fail_helper("-0");
        readStrict_String_fail_helper("+0");
        readStrict_String_fail_helper("--");
        readStrict_String_fail_helper("+1");
        readStrict_String_fail_helper("+x");
        readStrict_String_fail_helper("+x^2");
        readStrict_String_fail_helper("+x^2-x");
        readStrict_String_fail_helper("x^1000000000000");
        readStrict_String_fail_helper(" x");
        readStrict_String_fail_helper("x ");
        readStrict_String_fail_helper("X");
        readStrict_String_fail_helper("x + 1");
        readStrict_String_fail_helper("x^0");
        readStrict_String_fail_helper("x^-1");
        readStrict_String_fail_helper("x^1");
        readStrict_String_fail_helper("1-2");
        readStrict_String_fail_helper("1*x");
        readStrict_String_fail_helper("-1*x");
        readStrict_String_fail_helper("1*x^2");
        readStrict_String_fail_helper("-1*x^2");
        readStrict_String_fail_helper("x+x");
        readStrict_String_fail_helper("x+x^2");
        readStrict_String_fail_helper("1+x");
        readStrict_String_fail_helper("x+0");
        readStrict_String_fail_helper("0*x");
        readStrict_String_fail_helper("-0*x");
        readStrict_String_fail_helper("+0*x");
        readStrict_String_fail_helper("2x");
        readStrict_String_fail_helper("x^2+1+x");
        readStrict_String_fail_helper("x^2+3*x^2");
        readStrict_String_fail_helper("2^x");
        readStrict_String_fail_helper("abc");
        readStrict_String_fail_helper("x+y");
        readStrict_String_fail_helper("y");
        readStrict_String_fail_helper("1/2");
        readStrict_String_fail_helper("x/2");
    }

    private static void readStrict_int_String_helper(int maxExponent, @NotNull String input) {
        aeq(readStrict(maxExponent, input).get(), input);
    }

    private static void readStrict_int_String_fail_helper(int maxExponent, @NotNull String input) {
        assertFalse(readStrict(maxExponent, input).isPresent());
    }

    private static void readStrict_int_String_bad_maxExponent_fail_helper(int maxExponent, @NotNull String input) {
        try {
            readStrict(maxExponent, input);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testReadStrict_int_String() {
        readStrict_int_String_helper(1, "0");
        readStrict_int_String_helper(1, "1");
        readStrict_int_String_helper(1, "x");
        readStrict_int_String_helper(1, "2");
        readStrict_int_String_helper(1, "-2");
        readStrict_int_String_helper(1, "-x");
        readStrict_int_String_helper(1, "3*x");
        readStrict_int_String_helper(1, "-3*x");
        readStrict_int_String_helper(2, "x^2");
        readStrict_int_String_helper(2, "-x^2");
        readStrict_int_String_helper(2, "2*x^2");
        readStrict_int_String_helper(2, "-2*x^2");
        readStrict_int_String_helper(1, "x-1");
        readStrict_int_String_helper(1, "-x-1");
        readStrict_int_String_helper(2, "x^2-1");
        readStrict_int_String_helper(2, "x^2-4*x+7");
        readStrict_int_String_helper(10, "3*x^10");
        readStrict_int_String_fail_helper(1, "x^2");
        readStrict_int_String_fail_helper(1, "-x^2");
        readStrict_int_String_fail_helper(1, "2*x^2");
        readStrict_int_String_fail_helper(1, "-2*x^2");
        readStrict_int_String_fail_helper(1, "x^2-1");
        readStrict_int_String_fail_helper(1, "x^2-4*x+7");
        readStrict_int_String_fail_helper(9, "3*x^10");
        readStrict_int_String_fail_helper(10, "");
        readStrict_int_String_fail_helper(10, "+");
        readStrict_int_String_fail_helper(10, "-");
        readStrict_int_String_fail_helper(10, "-0");
        readStrict_int_String_fail_helper(10, "+0");
        readStrict_int_String_fail_helper(10, "--");
        readStrict_int_String_fail_helper(10, "+1");
        readStrict_int_String_fail_helper(10, "+x");
        readStrict_int_String_fail_helper(10, "+x^2");
        readStrict_int_String_fail_helper(10, "+x^2-x");
        readStrict_int_String_fail_helper(10, "x^1000000000000");
        readStrict_int_String_fail_helper(10, " x");
        readStrict_int_String_fail_helper(10, "x ");
        readStrict_int_String_fail_helper(10, "X");
        readStrict_int_String_fail_helper(10, "x + 1");
        readStrict_int_String_fail_helper(10, "x^0");
        readStrict_int_String_fail_helper(10, "x^-1");
        readStrict_int_String_fail_helper(10, "x^1");
        readStrict_int_String_fail_helper(10, "1-2");
        readStrict_int_String_fail_helper(10, "1*x");
        readStrict_int_String_fail_helper(10, "-1*x");
        readStrict_int_String_fail_helper(10, "1*x^2");
        readStrict_int_String_fail_helper(10, "-1*x^2");
        readStrict_int_String_fail_helper(10, "x+x");
        readStrict_int_String_fail_helper(10, "x+x^2");
        readStrict_int_String_fail_helper(10, "1+x");
        readStrict_int_String_fail_helper(10, "x+0");
        readStrict_int_String_fail_helper(10, "0*x");
        readStrict_int_String_fail_helper(10, "-0*x");
        readStrict_int_String_fail_helper(10, "+0*x");
        readStrict_int_String_fail_helper(10, "2x");
        readStrict_int_String_fail_helper(10, "x^2+1+x");
        readStrict_int_String_fail_helper(10, "x^2+3*x^2");
        readStrict_int_String_fail_helper(10, "2^x");
        readStrict_int_String_fail_helper(10, "abc");
        readStrict_int_String_fail_helper(10, "x+y");
        readStrict_int_String_fail_helper(10, "y");
        readStrict_int_String_fail_helper(10, "1/2");
        readStrict_int_String_fail_helper(10, "x/2");
        readStrict_int_String_bad_maxExponent_fail_helper(0, "1");
        readStrict_int_String_bad_maxExponent_fail_helper(-1, "0");
    }

    private static @NotNull List<BigInteger> readBigIntegerList(@NotNull String s) {
        return Readers.readListStrict(Readers::readBigIntegerStrict).apply(s).get();
    }

    private static @NotNull List<BigInteger> readBigIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Readers::readBigIntegerStrict).apply(s).get();
    }

    private static @NotNull List<Polynomial> readPolynomialList(@NotNull String s) {
        return Readers.readListStrict(Polynomial::readStrict).apply(s).get();
    }

    private static @NotNull List<Polynomial> readPolynomialListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Polynomial::readStrict).apply(s).get();
    }

    private static @NotNull List<Pair<BigInteger, BigInteger>> readBigIntegerPairList(@NotNull String s) {
        return Readers.readListStrict(
                t -> Pair.read(
                        t,
                        i -> NullableOptional.fromOptional(Readers.readBigIntegerStrict(i)),
                        i -> NullableOptional.fromOptional(Readers.readBigIntegerStrict(i))
                )
        ).apply(s).get();
    }

    private static @NotNull List<Pair<BigInteger, BigInteger>> readBigIntegerPairListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(
                t -> Pair.read(
                        t,
                        Readers.readWithNullsStrict(Readers::readBigIntegerStrict),
                        Readers.readWithNullsStrict(Readers::readBigIntegerStrict)
                )
        ).apply(s).get();
    }
}
