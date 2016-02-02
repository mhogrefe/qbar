package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.Polynomial.*;
import static mho.qbar.objects.Polynomial.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class PolynomialTest {
    @Test
    public void testConstants() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
        aeq(X, "x");
    }

    private static void iterator_helper(@NotNull String x, @NotNull String output) {
        aeq(toList(read(x).get()), output);
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
        aeq(read(p).get().apply(Readers.readBigInteger(x).get()), output);
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
        aeq(read(p).get().apply(Rational.read(x).get()), output);
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
        aeq(read(p).get().specialApply(Rational.read(x).get()), output);
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
        aeq(read(input).get().toRationalPolynomial(), input);
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
        aeq(read(p).get().coefficient(i), output);
    }

    private static void coefficient_fail_helper(@NotNull String p, int i) {
        try {
            read(p).get().coefficient(i);
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
        aeq(of(Readers.readBigInteger(input).get()), input);
    }

    @Test
    public void testOf_BigInteger() {
        of_BigInteger_helper("0");
        of_BigInteger_helper("1");
        of_BigInteger_helper("5");
        of_BigInteger_helper("-7");
    }

    private static void of_BigInteger_int_helper(@NotNull String input, int i, @NotNull String output) {
        aeq(of(Readers.readBigInteger(input).get(), i), output);
    }

    private static void of_BigInteger_int_fail_helper(@NotNull String input, int i) {
        try {
            of(Readers.readBigInteger(input).get(), i);
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

    private static void maxCoefficientBitLength_helper(@NotNull String input, int output) {
        aeq(read(input).get().maxCoefficientBitLength(), output);
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
        aeq(read(input).get().degree(), output);
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
        aeq(read(input).get().leading().get(), output);
    }

    private static void leading_empty_helper(@NotNull String input) {
        assertFalse(read(input).get().leading().isPresent());
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

    private static void add_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().add(read(b).get()), output);
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
        aeq(read(input).get().negate(), output);
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
        aeq(read(input).get().abs(), output);
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
        aeq(read(input).get().signum(), output);
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
        aeq(read(p).get().signum(Readers.readBigInteger(x).get()), output);
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
        aeq(read(p).get().signum(Rational.read(x).get()), output);
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
        aeq(read(a).get().subtract(read(b).get()), output);
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
        aeq(read(a).get().multiply(read(b).get()), output);
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
        aeq(read(p).get().multiply(Readers.readBigInteger(i).get()), output);
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
        aeq(read(p).get().multiply(i), output);
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

    private static void shiftLeft_helper(@NotNull String p, int bits, @NotNull String output) {
        aeq(read(p).get().shiftLeft(bits), output);
    }

    private static void shiftLeft_fail_helper(@NotNull String p, int bits) {
        try {
            read(p).get().shiftLeft(bits);
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
        Polynomial seed = read("x+1").get();
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
        aeq(read(p).get().pow(exponent), output);
    }

    private static void pow_fail_helper(@NotNull String p, int exponent) {
        try {
            read(p).get().pow(exponent);
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
        aeq(read(a).get().substitute(read(b).get()), output);
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
        aeq(read(input).get().differentiate(), output);
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
        aeq(read(input).get().isMonic(), output);
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
        aeq(read(input).get().isPrimitive(), output);
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
        Pair<BigInteger, Polynomial> result = read(input).get().contentAndPrimitive();
        aeq(result.a, constant);
        aeq(result.b, polynomial);
    }

    private static void contentAndPrimitive_fail_helper(@NotNull String input) {
        try {
            read(input).get().contentAndPrimitive();
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
        Pair<BigInteger, Polynomial> result = read(input).get().constantFactor();
        aeq(result.a, constant);
        aeq(result.b, polynomial);
    }

    private static void constantFactor_fail_helper(@NotNull String input) {
        try {
            read(input).get().constantFactor();
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
        Pair<Polynomial, Polynomial> result = read(a).get().pseudoDivide(read(b).get());
        aeq(result.a, pseudoQuotient);
        aeq(result.b, pseudoRemainder);
    }

    private static void pseudoDivide_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().pseudoDivide(read(b).get());
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
        aeq(read(a).get().pseudoRemainder(read(b).get()), output);
    }

    private static void pseudoRemainder_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().pseudoRemainder(read(b).get());
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

    private static void divisibleBy_helper(@NotNull String a, @NotNull String b, boolean output) {
        aeq(read(a).get().isDivisibleBy(read(b).get()), output);
    }

    private static void divisibleBy_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().isDivisibleBy(read(b).get());
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

    private static void divideExact_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().divideExact(read(b).get()), output);
    }

    private static void divideExact_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().divideExact(read(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivideExact() {
        divideExact_helper("0", "1", "0");
        divideExact_helper("0", "x^2", "0");
        divideExact_helper("6", "3", "2");
        divideExact_helper("6*x-3", "3", "2*x-1");
        divideExact_helper("x^2-1", "x+1", "x-1");
        divideExact_helper("x^2-1", "x-1", "x+1");
        divideExact_helper("6*x^10", "-2*x^3", "-3*x^7");
        divideExact_helper("-x^5+4*x^4-7*x^3-x^2+4*x-7", "x^2-4*x+7", "-x^3-1");
        divideExact_helper("-x^5+4*x^4-7*x^3-x^2+4*x-7", "-x^3-1", "x^2-4*x+7");

        divideExact_fail_helper("0", "0");
        divideExact_fail_helper("1", "0");
        divideExact_fail_helper("2", "3");
        divideExact_fail_helper("6*x-3", "4");
        divideExact_fail_helper("x^5", "x+1");
        divideExact_fail_helper("x^2+2*x+1", "x-1");
    }

    private static void remainderExact_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().remainderExact(read(b).get()), output);
    }

    private static void remainderExact_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().remainderExact(read(b).get());
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
        aeq(read(a).get().trivialPseudoRemainderSequence(read(b).get()), output);
    }

    private static void trivialPseudoRemainderSequence_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().trivialPseudoRemainderSequence(read(b).get());
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
        aeq(read(a).get().primitivePseudoRemainderSequence(read(b).get()), output);
    }

    private static void primitivePseudoRemainderSequence_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().primitivePseudoRemainderSequence(read(b).get());
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
        aeq(read(a).get().subresultantSequence(read(b).get()), output);
    }

    private static void subresultantSequence_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().subresultantSequence(read(b).get());
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

    private static void gcd_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().gcd(read(b).get()), output);
    }

    private static void gcd_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().gcd(read(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testGcd() {
        gcd_helper("0", "1", "1");
        gcd_helper("0", "x", "x");
        gcd_helper("0", "-17", "1");
        gcd_helper("0", "x^2-4*x+7", "x^2-4*x+7");
        gcd_helper("0", "-x^3-1", "x^3+1");
        gcd_helper("0", "3*x^10", "x^10");

        gcd_helper("1", "0", "1");
        gcd_helper("1", "1", "1");
        gcd_helper("1", "x", "1");
        gcd_helper("1", "-17", "1");
        gcd_helper("1", "x^2-4*x+7", "1");
        gcd_helper("1", "-x^3-1", "1");
        gcd_helper("1", "3*x^10", "1");

        gcd_helper("x", "0", "x");
        gcd_helper("x", "1", "1");
        gcd_helper("x", "x", "x");
        gcd_helper("x", "-17", "1");
        gcd_helper("x", "x^2-4*x+7", "1");
        gcd_helper("x", "-x^3-1", "1");
        gcd_helper("x", "3*x^10", "x");

        gcd_helper("-17", "0", "1");
        gcd_helper("-17", "1", "1");
        gcd_helper("-17", "x", "1");
        gcd_helper("-17", "-17", "1");
        gcd_helper("-17", "x^2-4*x+7", "1");
        gcd_helper("-17", "-x^3-1", "1");
        gcd_helper("-17", "3*x^10", "1");

        gcd_helper("x^2-4*x+7", "0", "x^2-4*x+7");
        gcd_helper("x^2-4*x+7", "1", "1");
        gcd_helper("x^2-4*x+7", "x", "1");
        gcd_helper("x^2-4*x+7", "-17", "1");
        gcd_helper("x^2-4*x+7", "x^2-4*x+7", "x^2-4*x+7");
        gcd_helper("x^2-4*x+7", "-x^3-1", "1");
        gcd_helper("x^2-4*x+7", "3*x^10", "1");

        gcd_helper("-x^3-1", "0", "x^3+1");
        gcd_helper("-x^3-1", "1", "1");
        gcd_helper("-x^3-1", "x", "1");
        gcd_helper("-x^3-1", "-17", "1");
        gcd_helper("-x^3-1", "x^2-4*x+7", "1");
        gcd_helper("-x^3-1", "-x^3-1", "x^3+1");
        gcd_helper("-x^3-1", "3*x^10", "1");

        gcd_helper("3*x^10", "0", "x^10");
        gcd_helper("3*x^10", "1", "1");
        gcd_helper("3*x^10", "x", "x");
        gcd_helper("3*x^10", "-17", "1");
        gcd_helper("3*x^10", "x^2-4*x+7", "1");
        gcd_helper("3*x^10", "-x^3-1", "1");
        gcd_helper("3*x^10", "3*x^10", "x^10");

        gcd_helper("x^2+7*x+6", "x^2-5*x-6", "x+1");
        gcd_fail_helper("0", "0");
    }

    private static void factor_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().factor(), output);
    }

    private static void factor_fail_helper(@NotNull String input) {
        try {
            read(input).get().factor();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testFactor() {
        factor_helper("1", "[1]");
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

        factor_fail_helper("0");
    }

    private static void isIrreducible_helper(@NotNull String input, boolean output) {
        aeq(read(input).get().isIrreducible(), output);
    }

    private static void isIrreducible_fail_helper(@NotNull String input) {
        try {
            read(input).get().isIrreducible();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIsIrreducible() {
        isIrreducible_helper("1", true);
        isIrreducible_helper("-1", true);
        isIrreducible_helper("x", true);
        isIrreducible_helper("-17", true);
        isIrreducible_helper("x^2-4*x+7", true);
        isIrreducible_helper("x^3-1", false);
        isIrreducible_helper("3*x^10", false);

        isIrreducible_helper("6", true);
        isIrreducible_helper("-x", false);
        isIrreducible_helper("3*x", false);
        isIrreducible_helper("x^10", false);
        isIrreducible_helper("x^2-1", false);
        isIrreducible_helper("3*x^6+24*x+2", true);
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

    @Test
    public void testEquals() {
        testEqualsHelper(
                readPolynomialList("[0, 1, x, -17, x^2-4*x+7, -x^3-1, 3*x^10]"),
                readPolynomialList("[0, 1, x, -17, x^2-4*x+7, -x^3-1, 3*x^10]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
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
        read_String_helper("x");
        read_String_helper("2");
        read_String_helper("-2");
        read_String_helper("-x");
        read_String_helper("3*x");
        read_String_helper("-3*x");
        read_String_helper("x^2");
        read_String_helper("-x^2");
        read_String_helper("2*x^2");
        read_String_helper("-2*x^2");
        read_String_helper("x-1");
        read_String_helper("-x-1");
        read_String_helper("x^2-1");
        read_String_helper("x^2-4*x+7");
        read_String_helper("3*x^10");
        read_String_fail_helper("");
        read_String_fail_helper("+");
        read_String_fail_helper("-");
        read_String_fail_helper("-0");
        read_String_fail_helper("+0");
        read_String_fail_helper("--");
        read_String_fail_helper("+1");
        read_String_fail_helper("+x");
        read_String_fail_helper("+x^2");
        read_String_fail_helper("+x^2-x");
        read_String_fail_helper("x^1000000000000");
        read_String_fail_helper(" x");
        read_String_fail_helper("x ");
        read_String_fail_helper("X");
        read_String_fail_helper("x + 1");
        read_String_fail_helper("x^0");
        read_String_fail_helper("x^-1");
        read_String_fail_helper("x^1");
        read_String_fail_helper("1-2");
        read_String_fail_helper("1*x");
        read_String_fail_helper("-1*x");
        read_String_fail_helper("1*x^2");
        read_String_fail_helper("-1*x^2");
        read_String_fail_helper("x+x");
        read_String_fail_helper("x+x^2");
        read_String_fail_helper("1+x");
        read_String_fail_helper("x+0");
        read_String_fail_helper("0*x");
        read_String_fail_helper("-0*x");
        read_String_fail_helper("+0*x");
        read_String_fail_helper("2x");
        read_String_fail_helper("x^2+1+x");
        read_String_fail_helper("x^2+3*x^2");
        read_String_fail_helper("2^x");
        read_String_fail_helper("abc");
        read_String_fail_helper("x+y");
        read_String_fail_helper("y");
        read_String_fail_helper("1/2");
        read_String_fail_helper("x/2");
    }

    private static void read_int_String_helper(int maxExponent, @NotNull String input) {
        aeq(read(maxExponent, input).get(), input);
    }

    private static void read_int_String_fail_helper(int maxExponent, @NotNull String input) {
        assertFalse(read(maxExponent, input).isPresent());
    }

    private static void read_int_String_bad_maxExponent_fail_helper(int maxExponent, @NotNull String input) {
        try {
            read(maxExponent, input);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRead_int_String() {
        read_int_String_helper(1, "0");
        read_int_String_helper(1, "1");
        read_int_String_helper(1, "x");
        read_int_String_helper(1, "2");
        read_int_String_helper(1, "-2");
        read_int_String_helper(1, "-x");
        read_int_String_helper(1, "3*x");
        read_int_String_helper(1, "-3*x");
        read_int_String_helper(2, "x^2");
        read_int_String_helper(2, "-x^2");
        read_int_String_helper(2, "2*x^2");
        read_int_String_helper(2, "-2*x^2");
        read_int_String_helper(1, "x-1");
        read_int_String_helper(1, "-x-1");
        read_int_String_helper(2, "x^2-1");
        read_int_String_helper(2, "x^2-4*x+7");
        read_int_String_helper(10, "3*x^10");
        read_int_String_fail_helper(1, "x^2");
        read_int_String_fail_helper(1, "-x^2");
        read_int_String_fail_helper(1, "2*x^2");
        read_int_String_fail_helper(1, "-2*x^2");
        read_int_String_fail_helper(1, "x^2-1");
        read_int_String_fail_helper(1, "x^2-4*x+7");
        read_int_String_fail_helper(9, "3*x^10");
        read_int_String_fail_helper(10, "");
        read_int_String_fail_helper(10, "+");
        read_int_String_fail_helper(10, "-");
        read_int_String_fail_helper(10, "-0");
        read_int_String_fail_helper(10, "+0");
        read_int_String_fail_helper(10, "--");
        read_int_String_fail_helper(10, "+1");
        read_int_String_fail_helper(10, "+x");
        read_int_String_fail_helper(10, "+x^2");
        read_int_String_fail_helper(10, "+x^2-x");
        read_int_String_fail_helper(10, "x^1000000000000");
        read_int_String_fail_helper(10, " x");
        read_int_String_fail_helper(10, "x ");
        read_int_String_fail_helper(10, "X");
        read_int_String_fail_helper(10, "x + 1");
        read_int_String_fail_helper(10, "x^0");
        read_int_String_fail_helper(10, "x^-1");
        read_int_String_fail_helper(10, "x^1");
        read_int_String_fail_helper(10, "1-2");
        read_int_String_fail_helper(10, "1*x");
        read_int_String_fail_helper(10, "-1*x");
        read_int_String_fail_helper(10, "1*x^2");
        read_int_String_fail_helper(10, "-1*x^2");
        read_int_String_fail_helper(10, "x+x");
        read_int_String_fail_helper(10, "x+x^2");
        read_int_String_fail_helper(10, "1+x");
        read_int_String_fail_helper(10, "x+0");
        read_int_String_fail_helper(10, "0*x");
        read_int_String_fail_helper(10, "-0*x");
        read_int_String_fail_helper(10, "+0*x");
        read_int_String_fail_helper(10, "2x");
        read_int_String_fail_helper(10, "x^2+1+x");
        read_int_String_fail_helper(10, "x^2+3*x^2");
        read_int_String_fail_helper(10, "2^x");
        read_int_String_fail_helper(10, "abc");
        read_int_String_fail_helper(10, "x+y");
        read_int_String_fail_helper(10, "y");
        read_int_String_fail_helper(10, "1/2");
        read_int_String_fail_helper(10, "x/2");
        read_int_String_bad_maxExponent_fail_helper(0, "1");
        read_int_String_bad_maxExponent_fail_helper(-1, "0");
    }

    private static void findIn_String_helper(@NotNull String input, @NotNull String output, int index) {
        Pair<Polynomial, Integer> result = findIn(input).get();
        aeq(result.a, output);
        aeq(result.b, index);
    }

    private static void findIn_String_fail_helper(@NotNull String input) {
        assertFalse(findIn(input).isPresent());
    }

    @Test
    public void testFindIn_String() {
        findIn_String_helper("0123", "0", 0);
        findIn_String_helper("yxy", "x", 1);
        findIn_String_helper("ax+12b", "x+12", 1);
        findIn_String_helper("------x------", "-x", 5);
        findIn_String_helper("3*x^2z", "3*x^2", 0);
        findIn_String_helper("1+x+x^2", "1", 0);
        findIn_String_helper("+1", "1", 1);
        findIn_String_helper("y^12", "12", 2);
        findIn_String_helper("52*x^-10", "52*x", 0);
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
        Pair<Polynomial, Integer> result = findIn(maxExponent, input).get();
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
        findIn_int_String_helper(2, "0123", "0", 0);
        findIn_int_String_helper(2, "yxy", "x", 1);
        findIn_int_String_helper(2, "ax+12b", "x+12", 1);
        findIn_int_String_helper(2, "------x------", "-x", 5);
        findIn_int_String_helper(2, "3*x^2z", "3*x^2", 0);
        findIn_int_String_helper(1, "3*x^2z", "3*x", 0);
        findIn_int_String_helper(2, "1+x+x^2", "1", 0);
        findIn_int_String_helper(2, "+1", "1", 1);
        findIn_int_String_helper(2, "y^12", "12", 2);
        findIn_int_String_helper(2, "52*x^-10", "52*x", 0);
        findIn_int_String_fail_helper(2, "");
        findIn_int_String_fail_helper(2, "o");
        findIn_int_String_fail_helper(2, "hello");
        findIn_int_String_bad_maxExponent_fail_helper(0, "1");
        findIn_int_String_bad_maxExponent_fail_helper(-1, "0");
    }

    private static @NotNull List<BigInteger> readBigIntegerList(@NotNull String s) {
        return Readers.readList(Readers::readBigInteger).apply(s).get();
    }

    private static @NotNull List<BigInteger> readBigIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Readers::readBigInteger).apply(s).get();
    }

    private static @NotNull List<Polynomial> readPolynomialList(@NotNull String s) {
        return Readers.readList(Polynomial::read).apply(s).get();
    }

    private static @NotNull List<Polynomial> readPolynomialListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Polynomial::read).apply(s).get();
    }
}
