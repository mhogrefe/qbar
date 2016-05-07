package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.NullableOptional;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.qbar.objects.RationalPolynomial.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RationalPolynomialTest {
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
        iterator_helper("-4/3", "[-4/3]");
        iterator_helper("x^2-7/4*x+1/3", "[1/3, -7/4, 1]");
        iterator_helper("x^3-1", "[-1, 0, 0, 1]");
        iterator_helper("1/2*x^10", "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1/2]");
    }

    private static void apply_helper(@NotNull String p, @NotNull String x, @NotNull String output) {
        aeq(readStrict(p).get().apply(Rational.readStrict(x).get()), output);
    }

    @Test
    public void testApply() {
        apply_helper("0", "0", "0");
        apply_helper("0", "1", "0");
        apply_helper("0", "-1", "0");
        apply_helper("0", "4/5", "0");
        apply_helper("0", "100", "0");

        apply_helper("1", "0", "1");
        apply_helper("1", "1", "1");
        apply_helper("1", "-1", "1");
        apply_helper("1", "4/5", "1");
        apply_helper("1", "100", "1");

        apply_helper("x", "0", "0");
        apply_helper("x", "1", "1");
        apply_helper("x", "-1", "-1");
        apply_helper("x", "4/5", "4/5");
        apply_helper("x", "100", "100");

        apply_helper("-4/3", "0", "-4/3");
        apply_helper("-4/3", "1", "-4/3");
        apply_helper("-4/3", "-1", "-4/3");
        apply_helper("-4/3", "4/5", "-4/3");
        apply_helper("-4/3", "100", "-4/3");

        apply_helper("x^2-7/4*x+1/3", "0", "1/3");
        apply_helper("x^2-7/4*x+1/3", "1", "-5/12");
        apply_helper("x^2-7/4*x+1/3", "-1", "37/12");
        apply_helper("x^2-7/4*x+1/3", "4/5", "-32/75");
        apply_helper("x^2-7/4*x+1/3", "100", "29476/3");

        apply_helper("x^3-1", "0", "-1");
        apply_helper("x^3-1", "1", "0");
        apply_helper("x^3-1", "-1", "-2");
        apply_helper("x^3-1", "4/5", "-61/125");
        apply_helper("x^3-1", "100", "999999");

        apply_helper("1/2*x^10", "0", "0");
        apply_helper("1/2*x^10", "1", "1/2");
        apply_helper("1/2*x^10", "-1", "1/2");
        apply_helper("1/2*x^10", "4/5", "524288/9765625");
        apply_helper("1/2*x^10", "100", "50000000000000000000");
    }

    private static void onlyHasIntegralCoefficients_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().onlyHasIntegralCoefficients(), output);
    }

    @Test
    public void testOnlyHasIntegralCoefficients() {
        onlyHasIntegralCoefficients_helper("0", true);
        onlyHasIntegralCoefficients_helper("1", true);
        onlyHasIntegralCoefficients_helper("x", true);
        onlyHasIntegralCoefficients_helper("-17", true);
        onlyHasIntegralCoefficients_helper("x^2-4*x+7", true);
        onlyHasIntegralCoefficients_helper("x^3-1", true);
        onlyHasIntegralCoefficients_helper("3*x^10", true);
        onlyHasIntegralCoefficients_helper("-4/3", false);
        onlyHasIntegralCoefficients_helper("x^2-7/4*x+1/3", false);
        onlyHasIntegralCoefficients_helper("1/2*x^10", false);
    }

    private static void toPolynomial_helper(@NotNull String input) {
        aeq(readStrict(input).get().toPolynomial(), input);
    }

    private static void toPolynomial_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().toPolynomial();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testToPolynomial() {
        toPolynomial_helper("0");
        toPolynomial_helper("1");
        toPolynomial_helper("x");
        toPolynomial_helper("-17");
        toPolynomial_helper("x^2-4*x+7");
        toPolynomial_helper("x^3-1");
        toPolynomial_helper("3*x^10");
        toPolynomial_fail_helper("-4/3");
        toPolynomial_fail_helper("x^2-7/4*x+1/3");
        toPolynomial_fail_helper("1/2*x^10");
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
        coefficient_helper("x^2-7/4*x+1/3", 0, "1/3");
        coefficient_helper("x^2-7/4*x+1/3", 1, "-7/4");
        coefficient_helper("x^2-7/4*x+1/3", 2, "1");
        coefficient_helper("x^2-7/4*x+1/3", 3, "0");
        coefficient_helper("x^3-1", 0, "-1");
        coefficient_helper("x^3-1", 1, "0");
        coefficient_helper("x^3-1", 2, "0");
        coefficient_helper("x^3-1", 3, "1");
        coefficient_helper("x^3-1", 4, "0");
        coefficient_fail_helper("x^3-1", -1);
    }

    private static void of_List_Rational_helper(@NotNull String input, @NotNull String output) {
        aeq(of(readRationalList(input)), output);
    }

    private static void of_List_Rational_fail_helper(@NotNull String input) {
        try {
            of(readRationalListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testOf_List_Rational() {
        of_List_Rational_helper("[]", "0");
        of_List_Rational_helper("[0]", "0");
        of_List_Rational_helper("[0, 0, 0]", "0");
        of_List_Rational_helper("[1]", "1");
        of_List_Rational_helper("[1, 0, 0]", "1");
        of_List_Rational_helper("[0, 1]", "x");
        of_List_Rational_helper("[-4/3]", "-4/3");
        of_List_Rational_helper("[1/3, -7/4, 1]", "x^2-7/4*x+1/3");
        of_List_Rational_helper("[1/3, -7/4, 1, 0]", "x^2-7/4*x+1/3");
        of_List_Rational_helper("[-1, 0, 0, 1]", "x^3-1");
        of_List_Rational_helper("[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1/2]", "1/2*x^10");
        of_List_Rational_fail_helper("[1/3, null, 1]");
    }

    private static void of_Rational_helper(@NotNull String input) {
        aeq(of(Rational.readStrict(input).get()), input);
    }

    @Test
    public void testOf_Rational() {
        of_Rational_helper("0");
        of_Rational_helper("1");
        of_Rational_helper("5/3");
        of_Rational_helper("-1/7");
    }

    private static void of_Rational_int_helper(@NotNull String input, int i, @NotNull String output) {
        aeq(of(Rational.readStrict(input).get(), i), output);
    }

    private static void of_Rational_int_fail_helper(@NotNull String input, int i) {
        try {
            of(Rational.readStrict(input).get(), i);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testOf_Rational_int() {
        of_Rational_int_helper("0", 0, "0");
        of_Rational_int_helper("0", 1, "0");
        of_Rational_int_helper("0", 2, "0");
        of_Rational_int_helper("0", 3, "0");

        of_Rational_int_helper("1", 0, "1");
        of_Rational_int_helper("1", 1, "x");
        of_Rational_int_helper("1", 2, "x^2");
        of_Rational_int_helper("1", 3, "x^3");

        of_Rational_int_helper("-1", 0, "-1");
        of_Rational_int_helper("-1", 1, "-x");
        of_Rational_int_helper("-1", 2, "-x^2");
        of_Rational_int_helper("-1", 3, "-x^3");

        of_Rational_int_helper("3/2", 0, "3/2");
        of_Rational_int_helper("3/2", 1, "3/2*x");
        of_Rational_int_helper("3/2", 2, "3/2*x^2");
        of_Rational_int_helper("3/2", 3, "3/2*x^3");

        of_Rational_int_helper("-5/7", 0, "-5/7");
        of_Rational_int_helper("-5/7", 1, "-5/7*x");
        of_Rational_int_helper("-5/7", 2, "-5/7*x^2");
        of_Rational_int_helper("-5/7", 3, "-5/7*x^3");
        of_Rational_int_fail_helper("-5/7", -1);
    }

    private static void maxCoefficientBitLength_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().maxCoefficientBitLength(), output);
    }

    @Test
    public void testMaxCoefficientBitLength() {
        maxCoefficientBitLength_helper("0", 0);
        maxCoefficientBitLength_helper("1", 2);
        maxCoefficientBitLength_helper("x", 2);
        maxCoefficientBitLength_helper("-4/3", 5);
        maxCoefficientBitLength_helper("x^2-7/4*x+1/3", 6);
        maxCoefficientBitLength_helper("x^3-1", 2);
        maxCoefficientBitLength_helper("1/2*x^10", 3);
    }

    private static void degree_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().degree(), output);
    }

    @Test
    public void testDegree() {
        degree_helper("0", -1);
        degree_helper("1", 0);
        degree_helper("x", 1);
        degree_helper("-4/3", 0);
        degree_helper("x^2-7/4*x+1/3", 2);
        degree_helper("x^3-1", 3);
        degree_helper("1/2*x^10", 10);
    }

    private static void leading_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().leading().get(), output);
    }

    private static void leading_empty_helper(@NotNull String input) {
        assertFalse(readStrict(input).get().leading().isPresent());
    }

    @Test
    public void testLeading() {
        leading_helper("-4/3", "-4/3");
        leading_helper("x^2-7/4*x+1/3", "1");
        leading_helper("-x^3-1", "-1");
        leading_helper("1/2*x^10", "1/2");
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

        multiplyByPowerOfX_helper("-4/3", 0, "-4/3");
        multiplyByPowerOfX_helper("-4/3", 1, "-4/3*x");
        multiplyByPowerOfX_helper("-4/3", 2, "-4/3*x^2");
        multiplyByPowerOfX_helper("-4/3", 3, "-4/3*x^3");

        multiplyByPowerOfX_helper("x^2-7/4*x+1/3", 0, "x^2-7/4*x+1/3");
        multiplyByPowerOfX_helper("x^2-7/4*x+1/3", 1, "x^3-7/4*x^2+1/3*x");
        multiplyByPowerOfX_helper("x^2-7/4*x+1/3", 2, "x^4-7/4*x^3+1/3*x^2");
        multiplyByPowerOfX_helper("x^2-7/4*x+1/3", 3, "x^5-7/4*x^4+1/3*x^3");

        multiplyByPowerOfX_helper("-x^3-1", 0, "-x^3-1");
        multiplyByPowerOfX_helper("-x^3-1", 1, "-x^4-x");
        multiplyByPowerOfX_helper("-x^3-1", 2, "-x^5-x^2");
        multiplyByPowerOfX_helper("-x^3-1", 3, "-x^6-x^3");

        multiplyByPowerOfX_helper("1/2*x^10", 0, "1/2*x^10");
        multiplyByPowerOfX_helper("1/2*x^10", 1, "1/2*x^11");
        multiplyByPowerOfX_helper("1/2*x^10", 2, "1/2*x^12");
        multiplyByPowerOfX_helper("1/2*x^10", 3, "1/2*x^13");

        multiplyByPowerOfX_fail_helper("1/2*x^10", -1);
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
        add_helper("0", "-4/3", "-4/3");
        add_helper("0", "x^2-7/4*x+1/3", "x^2-7/4*x+1/3");
        add_helper("0", "-x^3-1", "-x^3-1");
        add_helper("0", "1/2*x^10", "1/2*x^10");

        add_helper("1", "0", "1");
        add_helper("1", "1", "2");
        add_helper("1", "x", "x+1");
        add_helper("1", "-4/3", "-1/3");
        add_helper("1", "x^2-7/4*x+1/3", "x^2-7/4*x+4/3");
        add_helper("1", "-x^3-1", "-x^3");
        add_helper("1", "1/2*x^10", "1/2*x^10+1");

        add_helper("x", "0", "x");
        add_helper("x", "1", "x+1");
        add_helper("x", "x", "2*x");
        add_helper("x", "-4/3", "x-4/3");
        add_helper("x", "x^2-7/4*x+1/3", "x^2-3/4*x+1/3");
        add_helper("x", "-x^3-1", "-x^3+x-1");
        add_helper("x", "1/2*x^10", "1/2*x^10+x");

        add_helper("-4/3", "0", "-4/3");
        add_helper("-4/3", "1", "-1/3");
        add_helper("-4/3", "x", "x-4/3");
        add_helper("-4/3", "-4/3", "-8/3");
        add_helper("-4/3", "x^2-7/4*x+1/3", "x^2-7/4*x-1");
        add_helper("-4/3", "-x^3-1", "-x^3-7/3");
        add_helper("-4/3", "1/2*x^10", "1/2*x^10-4/3");

        add_helper("x^2-7/4*x+1/3", "0", "x^2-7/4*x+1/3");
        add_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+4/3");
        add_helper("x^2-7/4*x+1/3", "x", "x^2-3/4*x+1/3");
        add_helper("x^2-7/4*x+1/3", "-4/3", "x^2-7/4*x-1");
        add_helper("x^2-7/4*x+1/3", "x^2-7/4*x+1/3", "2*x^2-7/2*x+2/3");
        add_helper("x^2-7/4*x+1/3", "-x^3-1", "-x^3+x^2-7/4*x-2/3");
        add_helper("x^2-7/4*x+1/3", "1/2*x^10", "1/2*x^10+x^2-7/4*x+1/3");

        add_helper("-x^3-1", "0", "-x^3-1");
        add_helper("-x^3-1", "1", "-x^3");
        add_helper("-x^3-1", "x", "-x^3+x-1");
        add_helper("-x^3-1", "-4/3", "-x^3-7/3");
        add_helper("-x^3-1", "x^2-7/4*x+1/3", "-x^3+x^2-7/4*x-2/3");
        add_helper("-x^3-1", "-x^3-1", "-2*x^3-2");
        add_helper("-x^3-1", "1/2*x^10", "1/2*x^10-x^3-1");

        add_helper("1/2*x^10", "0", "1/2*x^10");
        add_helper("1/2*x^10", "1", "1/2*x^10+1");
        add_helper("1/2*x^10", "x", "1/2*x^10+x");
        add_helper("1/2*x^10", "-4/3", "1/2*x^10-4/3");
        add_helper("1/2*x^10", "x^2-7/4*x+1/3", "1/2*x^10+x^2-7/4*x+1/3");
        add_helper("1/2*x^10", "-x^3-1", "1/2*x^10-x^3-1");
        add_helper("1/2*x^10", "1/2*x^10", "x^10");

        add_helper("x^2-4*x+7", "-x^2+4*x-7", "0");
        add_helper("x+3/4", "-x+1/4", "1");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().negate(), output);
    }

    @Test
    public void testNegate() {
        negate_helper("0", "0");
        negate_helper("1", "-1");
        negate_helper("x", "-x");
        negate_helper("-4/3", "4/3");
        negate_helper("x^2-7/4*x+1/3", "-x^2+7/4*x-1/3");
        negate_helper("-x^3-1", "x^3+1");
        negate_helper("1/2*x^10", "-1/2*x^10");
    }

    private static void abs_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().abs(), output);
    }

    @Test
    public void testAbs() {
        abs_helper("0", "0");
        abs_helper("1", "1");
        abs_helper("x", "x");
        abs_helper("-4/3", "4/3");
        abs_helper("x^2-7/4*x+1/3", "x^2-7/4*x+1/3");
        abs_helper("-x^3-1", "x^3+1");
        abs_helper("1/2*x^10", "1/2*x^10");
    }

    private static void signum_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().signum(), output);
    }

    @Test
    public void testSignum() {
        signum_helper("0", 0);
        signum_helper("1", 1);
        signum_helper("x", 1);
        signum_helper("-4/3", -1);
        signum_helper("x^2-7/4*x+1/3", 1);
        signum_helper("-x^3-1", -1);
        signum_helper("1/2*x^10", 1);
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

        signum_Rational_helper("-4/3", "0", -1);
        signum_Rational_helper("-4/3", "1", -1);
        signum_Rational_helper("-4/3", "-1", -1);
        signum_Rational_helper("-4/3", "4/5", -1);
        signum_Rational_helper("-4/3", "100", -1);

        signum_Rational_helper("x^2-7/4*x+1/3", "0", 1);
        signum_Rational_helper("x^2-7/4*x+1/3", "1", -1);
        signum_Rational_helper("x^2-7/4*x+1/3", "-1", 1);
        signum_Rational_helper("x^2-7/4*x+1/3", "4/5", -1);
        signum_Rational_helper("x^2-7/4*x+1/3", "100", 1);

        signum_Rational_helper("x^3-1", "0", -1);
        signum_Rational_helper("x^3-1", "1", 0);
        signum_Rational_helper("x^3-1", "-1", -1);
        signum_Rational_helper("x^3-1", "4/5", -1);
        signum_Rational_helper("x^3-1", "100", 1);

        signum_Rational_helper("1/2*x^10", "0", 0);
        signum_Rational_helper("1/2*x^10", "1", 1);
        signum_Rational_helper("1/2*x^10", "-1", 1);
        signum_Rational_helper("1/2*x^10", "4/5", 1);
        signum_Rational_helper("1/2*x^10", "100", 1);
    }

    private static void subtract_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().subtract(readStrict(b).get()), output);
    }

    @Test
    public void testSubtract() {
        subtract_helper("0", "0", "0");
        subtract_helper("0", "1", "-1");
        subtract_helper("0", "x", "-x");
        subtract_helper("0", "-4/3", "4/3");
        subtract_helper("0", "x^2-7/4*x+1/3", "-x^2+7/4*x-1/3");
        subtract_helper("0", "-x^3-1", "x^3+1");
        subtract_helper("0", "1/2*x^10", "-1/2*x^10");

        subtract_helper("1", "0", "1");
        subtract_helper("1", "1", "0");
        subtract_helper("1", "x", "-x+1");
        subtract_helper("1", "-4/3", "7/3");
        subtract_helper("1", "x^2-7/4*x+1/3", "-x^2+7/4*x+2/3");
        subtract_helper("1", "-x^3-1", "x^3+2");
        subtract_helper("1", "1/2*x^10", "-1/2*x^10+1");

        subtract_helper("x", "0", "x");
        subtract_helper("x", "1", "x-1");
        subtract_helper("x", "x", "0");
        subtract_helper("x", "-4/3", "x+4/3");
        subtract_helper("x", "x^2-7/4*x+1/3", "-x^2+11/4*x-1/3");
        subtract_helper("x", "-x^3-1", "x^3+x+1");
        subtract_helper("x", "1/2*x^10", "-1/2*x^10+x");

        subtract_helper("-4/3", "0", "-4/3");
        subtract_helper("-4/3", "1", "-7/3");
        subtract_helper("-4/3", "x", "-x-4/3");
        subtract_helper("-4/3", "-4/3", "0");
        subtract_helper("-4/3", "x^2-7/4*x+1/3", "-x^2+7/4*x-5/3");
        subtract_helper("-4/3", "-x^3-1", "x^3-1/3");
        subtract_helper("-4/3", "1/2*x^10", "-1/2*x^10-4/3");

        subtract_helper("x^2-7/4*x+1/3", "0", "x^2-7/4*x+1/3");
        subtract_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x-2/3");
        subtract_helper("x^2-7/4*x+1/3", "x", "x^2-11/4*x+1/3");
        subtract_helper("x^2-7/4*x+1/3", "-4/3", "x^2-7/4*x+5/3");
        subtract_helper("x^2-7/4*x+1/3", "x^2-7/4*x+1/3", "0");
        subtract_helper("x^2-7/4*x+1/3", "-x^3-1", "x^3+x^2-7/4*x+4/3");
        subtract_helper("x^2-7/4*x+1/3", "1/2*x^10", "-1/2*x^10+x^2-7/4*x+1/3");

        subtract_helper("-x^3-1", "0", "-x^3-1");
        subtract_helper("-x^3-1", "1", "-x^3-2");
        subtract_helper("-x^3-1", "x", "-x^3-x-1");
        subtract_helper("-x^3-1", "-4/3", "-x^3+1/3");
        subtract_helper("-x^3-1", "x^2-7/4*x+1/3", "-x^3-x^2+7/4*x-4/3");
        subtract_helper("-x^3-1", "-x^3-1", "0");
        subtract_helper("-x^3-1", "1/2*x^10", "-1/2*x^10-x^3-1");

        subtract_helper("1/2*x^10", "0", "1/2*x^10");
        subtract_helper("1/2*x^10", "1", "1/2*x^10-1");
        subtract_helper("1/2*x^10", "x", "1/2*x^10-x");
        subtract_helper("1/2*x^10", "-4/3", "1/2*x^10+4/3");
        subtract_helper("1/2*x^10", "x^2-7/4*x+1/3", "1/2*x^10-x^2+7/4*x-1/3");
        subtract_helper("1/2*x^10", "-x^3-1", "1/2*x^10+x^3+1");
        subtract_helper("1/2*x^10", "1/2*x^10", "0");
    }

    private static void multiply_Polynomial_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().multiply(readStrict(b).get()), output);
    }

    @Test
    public void testMultiply_RationalPolynomial() {
        multiply_Polynomial_helper("0", "0", "0");
        multiply_Polynomial_helper("0", "1", "0");
        multiply_Polynomial_helper("0", "x", "0");
        multiply_Polynomial_helper("0", "-4/3", "0");
        multiply_Polynomial_helper("0", "x^2-7/4*x+1/3", "0");
        multiply_Polynomial_helper("0", "-x^3-1", "0");
        multiply_Polynomial_helper("0", "1/2*x^10", "0");

        multiply_Polynomial_helper("1", "0", "0");
        multiply_Polynomial_helper("1", "1", "1");
        multiply_Polynomial_helper("1", "x", "x");
        multiply_Polynomial_helper("1", "-4/3", "-4/3");
        multiply_Polynomial_helper("1", "x^2-7/4*x+1/3", "x^2-7/4*x+1/3");
        multiply_Polynomial_helper("1", "-x^3-1", "-x^3-1");
        multiply_Polynomial_helper("1", "1/2*x^10", "1/2*x^10");

        multiply_Polynomial_helper("x", "0", "0");
        multiply_Polynomial_helper("x", "1", "x");
        multiply_Polynomial_helper("x", "x", "x^2");
        multiply_Polynomial_helper("x", "-4/3", "-4/3*x");
        multiply_Polynomial_helper("x", "x^2-7/4*x+1/3", "x^3-7/4*x^2+1/3*x");
        multiply_Polynomial_helper("x", "-x^3-1", "-x^4-x");
        multiply_Polynomial_helper("x", "1/2*x^10", "1/2*x^11");

        multiply_Polynomial_helper("-4/3", "0", "0");
        multiply_Polynomial_helper("-4/3", "1", "-4/3");
        multiply_Polynomial_helper("-4/3", "x", "-4/3*x");
        multiply_Polynomial_helper("-4/3", "-4/3", "16/9");
        multiply_Polynomial_helper("-4/3", "x^2-7/4*x+1/3", "-4/3*x^2+7/3*x-4/9");
        multiply_Polynomial_helper("-4/3", "-x^3-1", "4/3*x^3+4/3");
        multiply_Polynomial_helper("-4/3", "1/2*x^10", "-2/3*x^10");

        multiply_Polynomial_helper("x^2-7/4*x+1/3", "0", "0");
        multiply_Polynomial_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+1/3");
        multiply_Polynomial_helper("x^2-7/4*x+1/3", "x", "x^3-7/4*x^2+1/3*x");
        multiply_Polynomial_helper("x^2-7/4*x+1/3", "-4/3", "-4/3*x^2+7/3*x-4/9");
        multiply_Polynomial_helper("x^2-7/4*x+1/3", "x^2-7/4*x+1/3", "x^4-7/2*x^3+179/48*x^2-7/6*x+1/9");
        multiply_Polynomial_helper("x^2-7/4*x+1/3", "-x^3-1", "-x^5+7/4*x^4-1/3*x^3-x^2+7/4*x-1/3");
        multiply_Polynomial_helper("x^2-7/4*x+1/3", "1/2*x^10", "1/2*x^12-7/8*x^11+1/6*x^10");

        multiply_Polynomial_helper("-x^3-1", "0", "0");
        multiply_Polynomial_helper("-x^3-1", "1", "-x^3-1");
        multiply_Polynomial_helper("-x^3-1", "x", "-x^4-x");
        multiply_Polynomial_helper("-x^3-1", "-4/3", "4/3*x^3+4/3");
        multiply_Polynomial_helper("-x^3-1", "x^2-7/4*x+1/3", "-x^5+7/4*x^4-1/3*x^3-x^2+7/4*x-1/3");
        multiply_Polynomial_helper("-x^3-1", "-x^3-1", "x^6+2*x^3+1");
        multiply_Polynomial_helper("-x^3-1", "1/2*x^10", "-1/2*x^13-1/2*x^10");

        multiply_Polynomial_helper("1/2*x^10", "0", "0");
        multiply_Polynomial_helper("1/2*x^10", "1", "1/2*x^10");
        multiply_Polynomial_helper("1/2*x^10", "x", "1/2*x^11");
        multiply_Polynomial_helper("1/2*x^10", "-4/3", "-2/3*x^10");
        multiply_Polynomial_helper("1/2*x^10", "x^2-7/4*x+1/3", "1/2*x^12-7/8*x^11+1/6*x^10");
        multiply_Polynomial_helper("1/2*x^10", "-x^3-1", "-1/2*x^13-1/2*x^10");
        multiply_Polynomial_helper("1/2*x^10", "1/2*x^10", "1/4*x^20");
    }

    private static void multiply_Rational_helper(@NotNull String p, @NotNull String r, @NotNull String output) {
        aeq(readStrict(p).get().multiply(Rational.readStrict(r).get()), output);
    }

    @Test
    public void testMultiply_Rational() {
        multiply_Rational_helper("0", "0", "0");
        multiply_Rational_helper("0", "1", "0");
        multiply_Rational_helper("0", "-3", "0");
        multiply_Rational_helper("0", "4/5", "0");

        multiply_Rational_helper("1", "0", "0");
        multiply_Rational_helper("1", "1", "1");
        multiply_Rational_helper("1", "-3", "-3");
        multiply_Rational_helper("1", "4/5", "4/5");

        multiply_Rational_helper("x", "0", "0");
        multiply_Rational_helper("x", "1", "x");
        multiply_Rational_helper("x", "-3", "-3*x");
        multiply_Rational_helper("x", "4/5", "4/5*x");

        multiply_Rational_helper("-4/3", "0", "0");
        multiply_Rational_helper("-4/3", "1", "-4/3");
        multiply_Rational_helper("-4/3", "-3", "4");
        multiply_Rational_helper("-4/3", "4/5", "-16/15");

        multiply_Rational_helper("x^2-7/4*x+1/3", "0", "0");
        multiply_Rational_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+1/3");
        multiply_Rational_helper("x^2-7/4*x+1/3", "-3", "-3*x^2+21/4*x-1");
        multiply_Rational_helper("x^2-7/4*x+1/3", "4/5", "4/5*x^2-7/5*x+4/15");

        multiply_Rational_helper("-x^3-1", "0", "0");
        multiply_Rational_helper("-x^3-1", "1", "-x^3-1");
        multiply_Rational_helper("-x^3-1", "-3", "3*x^3+3");
        multiply_Rational_helper("-x^3-1", "4/5", "-4/5*x^3-4/5");

        multiply_Rational_helper("1/2*x^10", "0", "0");
        multiply_Rational_helper("1/2*x^10", "1", "1/2*x^10");
        multiply_Rational_helper("1/2*x^10", "-3", "-3/2*x^10");
        multiply_Rational_helper("1/2*x^10", "4/5", "2/5*x^10");

        multiply_Rational_helper("5/4", "4/5", "1");
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

        multiply_BigInteger_helper("-4/3", "0", "0");
        multiply_BigInteger_helper("-4/3", "1", "-4/3");
        multiply_BigInteger_helper("-4/3", "-3", "4");
        multiply_BigInteger_helper("-4/3", "4", "-16/3");

        multiply_BigInteger_helper("x^2-7/4*x+1/3", "0", "0");
        multiply_BigInteger_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+1/3");
        multiply_BigInteger_helper("x^2-7/4*x+1/3", "-3", "-3*x^2+21/4*x-1");
        multiply_BigInteger_helper("x^2-7/4*x+1/3", "4", "4*x^2-7*x+4/3");

        multiply_BigInteger_helper("-x^3-1", "0", "0");
        multiply_BigInteger_helper("-x^3-1", "1", "-x^3-1");
        multiply_BigInteger_helper("-x^3-1", "-3", "3*x^3+3");
        multiply_BigInteger_helper("-x^3-1", "4", "-4*x^3-4");

        multiply_BigInteger_helper("1/2*x^10", "0", "0");
        multiply_BigInteger_helper("1/2*x^10", "1", "1/2*x^10");
        multiply_BigInteger_helper("1/2*x^10", "-3", "-3/2*x^10");
        multiply_BigInteger_helper("1/2*x^10", "4", "2*x^10");

        multiply_BigInteger_helper("1/4", "4", "1");
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

        multiply_int_helper("-4/3", 0, "0");
        multiply_int_helper("-4/3", 1, "-4/3");
        multiply_int_helper("-4/3", -3, "4");
        multiply_int_helper("-4/3", 4, "-16/3");

        multiply_int_helper("x^2-7/4*x+1/3", 0, "0");
        multiply_int_helper("x^2-7/4*x+1/3", 1, "x^2-7/4*x+1/3");
        multiply_int_helper("x^2-7/4*x+1/3", -3, "-3*x^2+21/4*x-1");
        multiply_int_helper("x^2-7/4*x+1/3", 4, "4*x^2-7*x+4/3");

        multiply_int_helper("-x^3-1", 0, "0");
        multiply_int_helper("-x^3-1", 1, "-x^3-1");
        multiply_int_helper("-x^3-1", -3, "3*x^3+3");
        multiply_int_helper("-x^3-1", 4, "-4*x^3-4");

        multiply_int_helper("1/2*x^10", 0, "0");
        multiply_int_helper("1/2*x^10", 1, "1/2*x^10");
        multiply_int_helper("1/2*x^10", -3, "-3/2*x^10");
        multiply_int_helper("1/2*x^10", 4, "2*x^10");

        multiply_int_helper("1/4", 4, "1");
    }

    private static void divide_Rational_helper(@NotNull String p, @NotNull String r, @NotNull String output) {
        aeq(readStrict(p).get().divide(Rational.readStrict(r).get()), output);
    }

    private static void divide_Rational_fail_helper(@NotNull String p, @NotNull String r) {
        try {
            readStrict(p).get().divide(Rational.readStrict(r).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_Rational() {
        divide_Rational_helper("0", "1", "0");
        divide_Rational_helper("0", "-3", "0");
        divide_Rational_helper("0", "4/5", "0");

        divide_Rational_helper("1", "1", "1");
        divide_Rational_helper("1", "-3", "-1/3");
        divide_Rational_helper("1", "4/5", "5/4");

        divide_Rational_helper("x", "1", "x");
        divide_Rational_helper("x", "-3", "-1/3*x");
        divide_Rational_helper("x", "4/5", "5/4*x");

        divide_Rational_helper("-4/3", "1", "-4/3");
        divide_Rational_helper("-4/3", "-3", "4/9");
        divide_Rational_helper("-4/3", "4/5", "-5/3");

        divide_Rational_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+1/3");
        divide_Rational_helper("x^2-7/4*x+1/3", "-3", "-1/3*x^2+7/12*x-1/9");
        divide_Rational_helper("x^2-7/4*x+1/3", "4/5", "5/4*x^2-35/16*x+5/12");

        divide_Rational_helper("-x^3-1", "1", "-x^3-1");
        divide_Rational_helper("-x^3-1", "-3", "1/3*x^3+1/3");
        divide_Rational_helper("-x^3-1", "4/5", "-5/4*x^3-5/4");

        divide_Rational_helper("1/2*x^10", "1", "1/2*x^10");
        divide_Rational_helper("1/2*x^10", "-3", "-1/6*x^10");
        divide_Rational_helper("1/2*x^10", "4/5", "5/8*x^10");

        divide_Rational_helper("5/4", "5/4", "1");

        divide_Rational_fail_helper("0", "0");
        divide_Rational_fail_helper("-4/3", "0");
    }

    private static void divide_BigInteger_helper(@NotNull String p, @NotNull String i, @NotNull String output) {
        aeq(readStrict(p).get().divide(Readers.readBigIntegerStrict(i).get()), output);
    }

    private static void divide_BigInteger_fail_helper(@NotNull String p, @NotNull String i) {
        try {
            readStrict(p).get().divide(Readers.readBigIntegerStrict(i).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_BigInteger() {
        divide_BigInteger_helper("0", "1", "0");
        divide_BigInteger_helper("0", "-3", "0");
        divide_BigInteger_helper("0", "4", "0");

        divide_BigInteger_helper("1", "1", "1");
        divide_BigInteger_helper("1", "-3", "-1/3");
        divide_BigInteger_helper("1", "4", "1/4");

        divide_BigInteger_helper("x", "1", "x");
        divide_BigInteger_helper("x", "-3", "-1/3*x");
        divide_BigInteger_helper("x", "4", "1/4*x");

        divide_BigInteger_helper("-4/3", "1", "-4/3");
        divide_BigInteger_helper("-4/3", "-3", "4/9");
        divide_BigInteger_helper("-4/3", "4", "-1/3");

        divide_BigInteger_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+1/3");
        divide_BigInteger_helper("x^2-7/4*x+1/3", "-3", "-1/3*x^2+7/12*x-1/9");
        divide_BigInteger_helper("x^2-7/4*x+1/3", "4", "1/4*x^2-7/16*x+1/12");

        divide_BigInteger_helper("-x^3-1", "1", "-x^3-1");
        divide_BigInteger_helper("-x^3-1", "-3", "1/3*x^3+1/3");
        divide_BigInteger_helper("-x^3-1", "4", "-1/4*x^3-1/4");

        divide_BigInteger_helper("1/2*x^10", "1", "1/2*x^10");
        divide_BigInteger_helper("1/2*x^10", "-3", "-1/6*x^10");
        divide_BigInteger_helper("1/2*x^10", "4", "1/8*x^10");

        divide_BigInteger_helper("5", "5", "1");

        divide_BigInteger_fail_helper("0", "0");
        divide_BigInteger_fail_helper("1", "0");
    }

    private static void divide_int_helper(@NotNull String p, int i, @NotNull String output) {
        aeq(readStrict(p).get().divide(i), output);
    }

    private static void divide_int_fail_helper(@NotNull String p, int i) {
        try {
            readStrict(p).get().divide(i);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_int() {
        divide_int_helper("0", 1, "0");
        divide_int_helper("0", -3, "0");
        divide_int_helper("0", 4, "0");

        divide_int_helper("1", 1, "1");
        divide_int_helper("1", -3, "-1/3");
        divide_int_helper("1", 4, "1/4");

        divide_int_helper("x", 1, "x");
        divide_int_helper("x", -3, "-1/3*x");
        divide_int_helper("x", 4, "1/4*x");

        divide_int_helper("-4/3", 1, "-4/3");
        divide_int_helper("-4/3", -3, "4/9");
        divide_int_helper("-4/3", 4, "-1/3");

        divide_int_helper("x^2-7/4*x+1/3", 1, "x^2-7/4*x+1/3");
        divide_int_helper("x^2-7/4*x+1/3", -3, "-1/3*x^2+7/12*x-1/9");
        divide_int_helper("x^2-7/4*x+1/3", 4, "1/4*x^2-7/16*x+1/12");

        divide_int_helper("-x^3-1", 1, "-x^3-1");
        divide_int_helper("-x^3-1", -3, "1/3*x^3+1/3");
        divide_int_helper("-x^3-1", 4, "-1/4*x^3-1/4");

        divide_int_helper("1/2*x^10", 1, "1/2*x^10");
        divide_int_helper("1/2*x^10", -3, "-1/6*x^10");
        divide_int_helper("1/2*x^10", 4, "1/8*x^10");

        divide_int_helper("5", 5, "1");

        divide_int_fail_helper("0", 0);
        divide_int_fail_helper("1", 0);
    }

    private static void shiftLeft_helper(@NotNull String p, int bits, @NotNull String output) {
        aeq(readStrict(p).get().shiftLeft(bits), output);
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

        shiftLeft_helper("x", 0, "x");
        shiftLeft_helper("x", 1, "2*x");
        shiftLeft_helper("x", 2, "4*x");
        shiftLeft_helper("x", 3, "8*x");
        shiftLeft_helper("x", 4, "16*x");
        shiftLeft_helper("x", -1, "1/2*x");
        shiftLeft_helper("x", -2, "1/4*x");
        shiftLeft_helper("x", -3, "1/8*x");
        shiftLeft_helper("x", -4, "1/16*x");

        shiftLeft_helper("-4/3", 0, "-4/3");
        shiftLeft_helper("-4/3", 1, "-8/3");
        shiftLeft_helper("-4/3", 2, "-16/3");
        shiftLeft_helper("-4/3", 3, "-32/3");
        shiftLeft_helper("-4/3", 4, "-64/3");
        shiftLeft_helper("-4/3", -1, "-2/3");
        shiftLeft_helper("-4/3", -2, "-1/3");
        shiftLeft_helper("-4/3", -3, "-1/6");
        shiftLeft_helper("-4/3", -4, "-1/12");

        shiftLeft_helper("x^2-7/4*x+1/3", 0, "x^2-7/4*x+1/3");
        shiftLeft_helper("x^2-7/4*x+1/3", 1, "2*x^2-7/2*x+2/3");
        shiftLeft_helper("x^2-7/4*x+1/3", 2, "4*x^2-7*x+4/3");
        shiftLeft_helper("x^2-7/4*x+1/3", 3, "8*x^2-14*x+8/3");
        shiftLeft_helper("x^2-7/4*x+1/3", 4, "16*x^2-28*x+16/3");
        shiftLeft_helper("x^2-7/4*x+1/3", -1, "1/2*x^2-7/8*x+1/6");
        shiftLeft_helper("x^2-7/4*x+1/3", -2, "1/4*x^2-7/16*x+1/12");
        shiftLeft_helper("x^2-7/4*x+1/3", -3, "1/8*x^2-7/32*x+1/24");
        shiftLeft_helper("x^2-7/4*x+1/3", -4, "1/16*x^2-7/64*x+1/48");

        shiftLeft_helper("-x^3-1", 0, "-x^3-1");
        shiftLeft_helper("-x^3-1", 1, "-2*x^3-2");
        shiftLeft_helper("-x^3-1", 2, "-4*x^3-4");
        shiftLeft_helper("-x^3-1", 3, "-8*x^3-8");
        shiftLeft_helper("-x^3-1", 4, "-16*x^3-16");
        shiftLeft_helper("-x^3-1", -1, "-1/2*x^3-1/2");
        shiftLeft_helper("-x^3-1", -2, "-1/4*x^3-1/4");
        shiftLeft_helper("-x^3-1", -3, "-1/8*x^3-1/8");
        shiftLeft_helper("-x^3-1", -4, "-1/16*x^3-1/16");

        shiftLeft_helper("1/2*x^10", 0, "1/2*x^10");
        shiftLeft_helper("1/2*x^10", 1, "x^10");
        shiftLeft_helper("1/2*x^10", 2, "2*x^10");
        shiftLeft_helper("1/2*x^10", 3, "4*x^10");
        shiftLeft_helper("1/2*x^10", 4, "8*x^10");
        shiftLeft_helper("1/2*x^10", -1, "1/4*x^10");
        shiftLeft_helper("1/2*x^10", -2, "1/8*x^10");
        shiftLeft_helper("1/2*x^10", -3, "1/16*x^10");
        shiftLeft_helper("1/2*x^10", -4, "1/32*x^10");
    }

    private static void shiftRight_helper(@NotNull String p, int bits, @NotNull String output) {
        aeq(readStrict(p).get().shiftRight(bits), output);
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

        shiftRight_helper("x", 0, "x");
        shiftRight_helper("x", 1, "1/2*x");
        shiftRight_helper("x", 2, "1/4*x");
        shiftRight_helper("x", 3, "1/8*x");
        shiftRight_helper("x", 4, "1/16*x");
        shiftRight_helper("x", -1, "2*x");
        shiftRight_helper("x", -2, "4*x");
        shiftRight_helper("x", -3, "8*x");
        shiftRight_helper("x", -4, "16*x");

        shiftRight_helper("-4/3", 0, "-4/3");
        shiftRight_helper("-4/3", 1, "-2/3");
        shiftRight_helper("-4/3", 2, "-1/3");
        shiftRight_helper("-4/3", 3, "-1/6");
        shiftRight_helper("-4/3", 4, "-1/12");
        shiftRight_helper("-4/3", -1, "-8/3");
        shiftRight_helper("-4/3", -2, "-16/3");
        shiftRight_helper("-4/3", -3, "-32/3");
        shiftRight_helper("-4/3", -4, "-64/3");

        shiftRight_helper("x^2-7/4*x+1/3", 0, "x^2-7/4*x+1/3");
        shiftRight_helper("x^2-7/4*x+1/3", 1, "1/2*x^2-7/8*x+1/6");
        shiftRight_helper("x^2-7/4*x+1/3", 2, "1/4*x^2-7/16*x+1/12");
        shiftRight_helper("x^2-7/4*x+1/3", 3, "1/8*x^2-7/32*x+1/24");
        shiftRight_helper("x^2-7/4*x+1/3", 4, "1/16*x^2-7/64*x+1/48");
        shiftRight_helper("x^2-7/4*x+1/3", -1, "2*x^2-7/2*x+2/3");
        shiftRight_helper("x^2-7/4*x+1/3", -2, "4*x^2-7*x+4/3");
        shiftRight_helper("x^2-7/4*x+1/3", -3, "8*x^2-14*x+8/3");
        shiftRight_helper("x^2-7/4*x+1/3", -4, "16*x^2-28*x+16/3");

        shiftRight_helper("-x^3-1", 0, "-x^3-1");
        shiftRight_helper("-x^3-1", 1, "-1/2*x^3-1/2");
        shiftRight_helper("-x^3-1", 2, "-1/4*x^3-1/4");
        shiftRight_helper("-x^3-1", 3, "-1/8*x^3-1/8");
        shiftRight_helper("-x^3-1", 4, "-1/16*x^3-1/16");
        shiftRight_helper("-x^3-1", -1, "-2*x^3-2");
        shiftRight_helper("-x^3-1", -2, "-4*x^3-4");
        shiftRight_helper("-x^3-1", -3, "-8*x^3-8");
        shiftRight_helper("-x^3-1", -4, "-16*x^3-16");

        shiftRight_helper("1/2*x^10", 0, "1/2*x^10");
        shiftRight_helper("1/2*x^10", 1, "1/4*x^10");
        shiftRight_helper("1/2*x^10", 2, "1/8*x^10");
        shiftRight_helper("1/2*x^10", 3, "1/16*x^10");
        shiftRight_helper("1/2*x^10", 4, "1/32*x^10");
        shiftRight_helper("1/2*x^10", -1, "x^10");
        shiftRight_helper("1/2*x^10", -2, "2*x^10");
        shiftRight_helper("1/2*x^10", -3, "4*x^10");
        shiftRight_helper("1/2*x^10", -4, "8*x^10");
    }

    private static void sum_helper(@NotNull String input, @NotNull String output) {
        aeq(sum(readRationalPolynomialList(input)), output);
    }

    private static void sum_fail_helper(@NotNull String input) {
        try {
            sum(readRationalPolynomialListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testSum() {
        sum_helper("[]", "0");
        sum_helper("[1]", "1");
        sum_helper("[-4/3]", "-4/3");
        sum_helper("[-4/3, x^2-7/4*x+1/3, -x^3-1, 1/2*x^10]", "1/2*x^10-x^3+x^2-7/4*x-2");
        sum_fail_helper("[-4/3, null, -x^3-1, 1/2*x^10]");
    }

    @Test
    public void testProduct() {
        assertTrue(product(readRationalPolynomialList("[]")) == ONE);
        assertTrue(product(readRationalPolynomialList("[0]")) == ZERO);
        aeq(product(readRationalPolynomialList("[-4/3]")), "-4/3");
        aeq(
                product(readRationalPolynomialList("[-4/3, x^2-7/4*x+1/3, -x^3-1, 1/2*x^10]")),
                "2/3*x^15-7/6*x^14+2/9*x^13+2/3*x^12-7/6*x^11+2/9*x^10"
        );
        try {
            product(readRationalPolynomialListWithNulls("[-4/3, null, -x^3-1, 1/2*x^10]"));
            fail();
        } catch (NullPointerException ignored) {}
    }

    private static void delta_helper(@NotNull Iterable<RationalPolynomial> input, @NotNull String output) {
        aeqitLimit(TINY_LIMIT, delta(input), output);
    }

    private static void delta_helper(@NotNull String input, @NotNull String output) {
        delta_helper(readRationalPolynomialList(input), output);
    }

    private static void delta_fail_helper(@NotNull String input) {
        try {
            toList(delta(readRationalPolynomialListWithNulls(input)));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testDelta() {
        delta_helper("[-4/3]", "[]");
        delta_helper("[-4/3, x^2-7/4*x+1/3]", "[x^2-7/4*x+5/3]");
        delta_helper("[-4/3, x^2-7/4*x+1/3, -x^3-1, 1/2*x^10]", "[x^2-7/4*x+5/3, -x^3-x^2+7/4*x-4/3, 1/2*x^10+x^3+1]");
        RationalPolynomial seed = readStrict("x+1/2").get();
        delta_helper(map(seed::pow, rangeUp(0)),
                "[x-1/2, x^2-1/4, x^3+1/2*x^2-1/4*x-1/8, x^4+x^3-1/4*x-1/16," +
                " x^5+3/2*x^4+1/2*x^3-1/4*x^2-3/16*x-1/32, x^6+2*x^5+5/4*x^4-5/16*x^2-1/8*x-1/64," +
                " x^7+5/2*x^6+9/4*x^5+5/8*x^4-5/16*x^3-9/32*x^2-5/64*x-1/128," +
                " x^8+3*x^7+7/2*x^6+7/4*x^5-7/16*x^3-7/32*x^2-3/64*x-1/256," +
                " x^9+7/2*x^8+5*x^7+7/2*x^6+7/8*x^5-7/16*x^4-7/16*x^3-5/32*x^2-7/256*x-1/512," +
                " x^10+4*x^9+27/4*x^8+6*x^7+21/8*x^6-21/32*x^4-3/8*x^3-27/256*x^2-1/64*x-1/1024," +
                " x^11+9/2*x^10+35/4*x^9+75/8*x^8+45/8*x^7+21/16*x^6-21/32*x^5-45/64*x^4-75/256*x^3-35/512*x^2-" +
                "9/1024*x-1/2048," +
                " x^12+5*x^11+11*x^10+55/4*x^9+165/16*x^8+33/8*x^7-33/32*x^5-165/256*x^4-55/256*x^3-11/256*x^2-" +
                "5/1024*x-1/4096," +
                " x^13+11/2*x^12+27/2*x^11+77/4*x^10+275/16*x^9+297/32*x^8+33/16*x^7-33/32*x^6-297/256*x^5-" +
                "275/512*x^4-77/512*x^3-27/1024*x^2-11/4096*x-1/8192," +
                " x^14+6*x^13+65/4*x^12+26*x^11+429/16*x^10+143/8*x^9+429/64*x^8-429/256*x^6-143/128*x^5-" +
                "429/1024*x^4-13/128*x^3-65/4096*x^2-3/2048*x-1/16384," +
                " x^15+13/2*x^14+77/4*x^13+273/8*x^12+637/16*x^11+1001/32*x^10+1001/64*x^9+429/128*x^8-429/256*x^7-" +
                "1001/512*x^6-1001/1024*x^5-637/2048*x^4-273/4096*x^3-77/8192*x^2-13/16384*x-1/32768," +
                " x^16+7*x^15+45/2*x^14+175/4*x^13+455/8*x^12+819/16*x^11+1001/32*x^10+715/64*x^9-715/256*x^7-" +
                "1001/512*x^6-819/1024*x^5-455/2048*x^4-175/4096*x^3-45/8192*x^2-7/16384*x-1/65536," +
                " x^17+15/2*x^16+26*x^15+55*x^14+315/4*x^13+637/8*x^12+455/8*x^11+429/16*x^10+715/128*x^9-" +
                "715/256*x^8-429/128*x^7-455/256*x^6-637/1024*x^5-315/2048*x^4-55/2048*x^3-13/4096*x^2-15/65536*x-" +
                "1/131072," +
                " x^18+8*x^17+119/4*x^16+68*x^15+425/4*x^14+119*x^13+1547/16*x^12+221/4*x^11+2431/128*x^10-" +
                "2431/512*x^8-221/64*x^7-1547/1024*x^6-119/256*x^5-425/4096*x^4-17/1024*x^3-119/65536*x^2-1/8192*x-" +
                "1/262144," +
                " x^19+17/2*x^18+135/4*x^17+663/8*x^16+561/4*x^15+1377/8*x^14+2499/16*x^13+3315/32*x^12+" +
                "5967/128*x^11+2431/256*x^10-2431/512*x^9-5967/1024*x^8-3315/1024*x^7-2499/2048*x^6-1377/4096*x^5-" +
                "561/8192*x^4-663/65536*x^3-135/131072*x^2-17/262144*x-1/524288, x^20+9*x^19+38*x^18+399/4*x^17+" +
                "2907/16*x^16+969/4*x^15+969/4*x^14+2907/16*x^13+12597/128*x^12+4199/128*x^11-4199/512*x^9-" +
                "12597/2048*x^8-2907/1024*x^7-969/1024*x^6-969/4096*x^5-2907/65536*x^4-399/65536*x^3-19/32768*x^2-" +
                "9/262144*x-1/1048576, ...]");
        delta_fail_helper("[]");
        delta_fail_helper("[-4/3, null, -x^3-1, 1/2*x^10]");
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

        pow_helper("-4/3", 0, "1");
        pow_helper("-4/3", 1, "-4/3");
        pow_helper("-4/3", 2, "16/9");
        pow_helper("-4/3", 3, "-64/27");

        pow_helper("x^2-7/4*x+1/3", 0, "1");
        pow_helper("x^2-7/4*x+1/3", 1, "x^2-7/4*x+1/3");
        pow_helper("x^2-7/4*x+1/3", 2, "x^4-7/2*x^3+179/48*x^2-7/6*x+1/9");
        pow_helper("x^2-7/4*x+1/3", 3, "x^6-21/4*x^5+163/16*x^4-567/64*x^3+163/48*x^2-7/12*x+1/27");

        pow_helper("-x^3-1", 0, "1");
        pow_helper("-x^3-1", 1, "-x^3-1");
        pow_helper("-x^3-1", 2, "x^6+2*x^3+1");
        pow_helper("-x^3-1", 3, "-x^9-3*x^6-3*x^3-1");

        pow_helper("1/2*x^10", 0, "1");
        pow_helper("1/2*x^10", 1, "1/2*x^10");
        pow_helper("1/2*x^10", 2, "1/4*x^20");
        pow_helper("1/2*x^10", 3, "1/8*x^30");

        pow_fail_helper("1/2*x^10", -1);
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
        substitute_helper("0", "-4/3", "0");
        substitute_helper("0", "x^2-7/4*x+1/3", "0");
        substitute_helper("0", "-x^3-1", "0");
        substitute_helper("0", "1/2*x^10", "0");

        substitute_helper("1", "0", "1");
        substitute_helper("1", "1", "1");
        substitute_helper("1", "x", "1");
        substitute_helper("1", "-4/3", "1");
        substitute_helper("1", "x^2-7/4*x+1/3", "1");
        substitute_helper("1", "-x^3-1", "1");
        substitute_helper("1", "1/2*x^10", "1");

        substitute_helper("x", "0", "0");
        substitute_helper("x", "1", "1");
        substitute_helper("x", "x", "x");
        substitute_helper("x", "-4/3", "-4/3");
        substitute_helper("x", "x^2-7/4*x+1/3", "x^2-7/4*x+1/3");
        substitute_helper("x", "-x^3-1", "-x^3-1");
        substitute_helper("x", "1/2*x^10", "1/2*x^10");

        substitute_helper("-4/3", "0", "-4/3");
        substitute_helper("-4/3", "1", "-4/3");
        substitute_helper("-4/3", "x", "-4/3");
        substitute_helper("-4/3", "-4/3", "-4/3");
        substitute_helper("-4/3", "x^2-4*x+7", "-4/3");
        substitute_helper("-4/3", "-x^3-1", "-4/3");
        substitute_helper("-4/3", "1/2*x^10", "-4/3");

        substitute_helper("x^2-7/4*x+1/3", "0", "1/3");
        substitute_helper("x^2-7/4*x+1/3", "1", "-5/12");
        substitute_helper("x^2-7/4*x+1/3", "x", "x^2-7/4*x+1/3");
        substitute_helper("x^2-7/4*x+1/3", "-4/3", "40/9");
        substitute_helper("x^2-7/4*x+1/3", "x^2-7/4*x+1/3", "x^4-7/2*x^3+95/48*x^2+91/48*x-5/36");
        substitute_helper("x^2-7/4*x+1/3", "-x^3-1", "x^6+15/4*x^3+37/12");
        substitute_helper("x^2-7/4*x+1/3", "1/2*x^10", "1/4*x^20-7/8*x^10+1/3");

        substitute_helper("-x^3-1", "0", "-1");
        substitute_helper("-x^3-1", "1", "-2");
        substitute_helper("-x^3-1", "x", "-x^3-1");
        substitute_helper("-x^3-1", "-4/3", "37/27");
        substitute_helper("-x^3-1", "x^2-7/4*x+1/3", "-x^6+21/4*x^5-163/16*x^4+567/64*x^3-163/48*x^2+7/12*x-28/27");
        substitute_helper("-x^3-1", "-x^3-1", "x^9+3*x^6+3*x^3");
        substitute_helper("-x^3-1", "1/2*x^10", "-1/8*x^30-1");

        substitute_helper("1/2*x^10", "0", "0");
        substitute_helper("1/2*x^10", "1", "1/2");
        substitute_helper("1/2*x^10", "x", "1/2*x^10");
        substitute_helper("1/2*x^10", "-4/3", "524288/59049");
        substitute_helper("1/2*x^10", "x^2-4*x+7",
                "1/2*x^20-20*x^19+395*x^18-5100*x^17+96285/2*x^16-352464*x^15+2073540*x^14-10026480*x^13" +
                "+40440585*x^12-137326040*x^11+394631330*x^10-961282280*x^9+1981588665*x^8-3439082640*x^7" +
                "+4978569540*x^6-5923862448*x^5+11327833965/2*x^4-4200069300*x^3+2277096395*x^2-807072140*x" +
                "+282475249/2");
        substitute_helper("1/2*x^10", "-x^3-1",
                "1/2*x^30+5*x^27+45/2*x^24+60*x^21+105*x^18+126*x^15+105*x^12+60*x^9+45/2*x^6+5*x^3+1/2");
        substitute_helper("1/2*x^10", "1/2*x^10", "1/2048*x^100");
    }

    private static void differentiate_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().differentiate(), output);
    }

    @Test
    public void testDifferentiate() {
        differentiate_helper("0", "0");
        differentiate_helper("1", "0");
        differentiate_helper("x", "1");
        differentiate_helper("-4/3", "0");
        differentiate_helper("x^2-7/4*x+1/3", "2*x-7/4");
        differentiate_helper("1/2*x^10", "5*x^9");
    }

    private static void isMonic_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isMonic(), output);
    }

    @Test
    public void testIsMonic() {
        isMonic_helper("0", false);
        isMonic_helper("1", true);
        isMonic_helper("x", true);
        isMonic_helper("-4/3", false);
        isMonic_helper("x^2-7/4*x+1/3", true);
        isMonic_helper("-x^3-1", false);
        isMonic_helper("1/2*x^10", false);
    }

    private static void makeMonic_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().makeMonic(), output);
    }

    private static void makeMonic_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().makeMonic();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testMakeMonic() {
        makeMonic_helper("1", "1");
        makeMonic_helper("x", "x");
        makeMonic_helper("-4/3", "1");
        makeMonic_helper("x^2-7/4*x+1/3", "x^2-7/4*x+1/3");
        makeMonic_helper("3*x^2-7/4*x+1/3", "x^2-7/12*x+1/9");
        makeMonic_helper("-x^3-1", "x^3+1");
        makeMonic_helper("1/2*x^10", "x^10");
        makeMonic_fail_helper("0");
    }

    private static void constantFactor_helper(
            @NotNull String input,
            @NotNull String constant,
            @NotNull String polynomial
    ) {
        Pair<Rational, Polynomial> result = readStrict(input).get().constantFactor();
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
        constantFactor_helper("-4/3", "-4/3", "1");
        constantFactor_helper("x^2-4*x+7", "1", "x^2-4*x+7");
        constantFactor_helper("6*x^2-4*x+8", "2", "3*x^2-2*x+4");
        constantFactor_helper("x^2-7/4*x+1/3", "1/12", "12*x^2-21*x+4");
        constantFactor_helper("-x^3-1", "-1", "x^3+1");
        constantFactor_helper("1/2*x^10", "1/2", "x^10");
        constantFactor_fail_helper("0");
    }

    private static void divide_RationalPolynomial_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String quotient,
            @NotNull String remainder
    ) {
        Pair<RationalPolynomial, RationalPolynomial> result = readStrict(a).get().divide(readStrict(b).get());
        aeq(result.a, quotient);
        aeq(result.b, remainder);
    }

    private static void divide_RationalPolynomial_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().divide(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_RationalPolynomial() {
        divide_RationalPolynomial_helper("0", "1", "0", "0");
        divide_RationalPolynomial_helper("0", "x", "0", "0");
        divide_RationalPolynomial_helper("0", "-4/3", "0", "0");
        divide_RationalPolynomial_helper("0", "x^2-7/4*x+1/3", "0", "0");
        divide_RationalPolynomial_helper("0", "-x^3-1", "0", "0");
        divide_RationalPolynomial_helper("0", "1/2*x^10", "0", "0");

        divide_RationalPolynomial_helper("1", "1", "1", "0");
        divide_RationalPolynomial_helper("1", "x", "0", "1");
        divide_RationalPolynomial_helper("1", "-4/3", "-3/4", "0");
        divide_RationalPolynomial_helper("1", "x^2-7/4*x+1/3", "0", "1");
        divide_RationalPolynomial_helper("1", "-x^3-1", "0", "1");
        divide_RationalPolynomial_helper("1", "1/2*x^10", "0", "1");

        divide_RationalPolynomial_helper("x", "1", "x", "0");
        divide_RationalPolynomial_helper("x", "x", "1", "0");
        divide_RationalPolynomial_helper("x", "-4/3", "-3/4*x", "0");
        divide_RationalPolynomial_helper("x", "x^2-7/4*x+1/3", "0", "x");
        divide_RationalPolynomial_helper("x", "-x^3-1", "0", "x");
        divide_RationalPolynomial_helper("x", "1/2*x^10", "0", "x");

        divide_RationalPolynomial_helper("-4/3", "1", "-4/3", "0");
        divide_RationalPolynomial_helper("-4/3", "x", "0", "-4/3");
        divide_RationalPolynomial_helper("-4/3", "-4/3", "1", "0");
        divide_RationalPolynomial_helper("-4/3", "x^2-7/4*x+1/3", "0", "-4/3");
        divide_RationalPolynomial_helper("-4/3", "-x^3-1", "0", "-4/3");
        divide_RationalPolynomial_helper("-4/3", "1/2*x^10", "0", "-4/3");

        divide_RationalPolynomial_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+1/3", "0");
        divide_RationalPolynomial_helper("x^2-7/4*x+1/3", "x", "x-7/4", "1/3");
        divide_RationalPolynomial_helper("x^2-7/4*x+1/3", "-4/3", "-3/4*x^2+21/16*x-1/4", "0");
        divide_RationalPolynomial_helper("x^2-7/4*x+1/3", "x^2-7/4*x+1/3", "1", "0");
        divide_RationalPolynomial_helper("x^2-7/4*x+1/3", "-x^3-1", "0", "x^2-7/4*x+1/3");
        divide_RationalPolynomial_helper("x^2-7/4*x+1/3", "1/2*x^10", "0", "x^2-7/4*x+1/3");

        divide_RationalPolynomial_helper("-x^3-1", "1", "-x^3-1", "0");
        divide_RationalPolynomial_helper("-x^3-1", "x", "-x^2", "-1");
        divide_RationalPolynomial_helper("-x^3-1", "-4/3", "3/4*x^3+3/4", "0");
        divide_RationalPolynomial_helper("-x^3-1", "x^2-7/4*x+1/3", "-x-7/4", "-131/48*x-5/12");
        divide_RationalPolynomial_helper("-x^3-1", "-x^3-1", "1", "0");
        divide_RationalPolynomial_helper("-x^3-1", "1/2*x^10", "0", "-x^3-1");

        divide_RationalPolynomial_helper("1/2*x^10", "1", "1/2*x^10", "0");
        divide_RationalPolynomial_helper("1/2*x^10", "x", "1/2*x^9", "0");
        divide_RationalPolynomial_helper("1/2*x^10", "-4/3", "-3/8*x^10", "0");
        divide_RationalPolynomial_helper(
                "1/2*x^10",
                "x^2-7/4*x+1/3",
                "1/2*x^8+7/8*x^7+131/96*x^6+805/384*x^5+14809/4608*x^4+10087/2048*x^3+1669499/221184*x^2+" +
                        "10233965/884736*x+188201281/10616832",
                "1153665527/42467328*x-188201281/31850496"
        );
        divide_RationalPolynomial_helper("1/2*x^10", "-x^3-1", "-1/2*x^7+1/2*x^4-1/2*x", "-1/2*x");
        divide_RationalPolynomial_helper("1/2*x^10", "1/2*x^10", "1", "0");

        divide_RationalPolynomial_helper("x^3-2*x^2-4", "x-3", "x^2+x+3", "5");
        divide_RationalPolynomial_helper("x^3-12*x^2-42", "x^2-2*x+1", "x-10", "-21*x-32");

        divide_RationalPolynomial_fail_helper("0", "0");
        divide_RationalPolynomial_fail_helper("x^2-7/4*x+1/3", "0");
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
        divisibleBy_helper("1", "5/2", true);
        divisibleBy_helper("3", "5/2", true);
        divisibleBy_helper("0", "1", true);
        divisibleBy_helper("0", "5/2", true);
        divisibleBy_helper("x", "1", true);
        divisibleBy_helper("x", "5/2", true);
        divisibleBy_helper("x", "-5/2", true);
        divisibleBy_helper("-x", "5/2", true);
        divisibleBy_helper("-x", "-5/2", true);
        divisibleBy_helper("5/2", "x", false);
        divisibleBy_helper("5/2", "-x", false);
        divisibleBy_helper("-5/2", "x", false);
        divisibleBy_helper("-5/2", "-x", false);
        divisibleBy_helper("x^2-1", "x+1", true);
        divisibleBy_helper("x^2-1", "x-1", true);
        divisibleBy_helper("x^2-1", "1/3*x+1/3", true);
        divisibleBy_helper("-2*x^2+1", "3*x+3", false);
        divisibleBy_helper("x^3", "x^2", true);
        divisibleBy_helper("x^2", "x^3", false);
        divisibleBy_helper("-2*x^3", "5/2*x^2", true);
        divisibleBy_helper("-x^5+7/4*x^4-1/3*x^3-x^2+7/4*x-1/3", "-x^3-1", true);
        divisibleBy_helper("-x^5+7/4*x^4-1/3*x^3-x^2+7/4*x-1/3", "x^2-7/4*x+1/3", true);

        divisibleBy_fail_helper("0", "0");
        divisibleBy_fail_helper("-5/2", "0");
        divisibleBy_fail_helper("x^2", "0");
    }

    private static void remainderSequence_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().remainderSequence(readStrict(b).get()), output);
    }

    private static void remainderSequence_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().remainderSequence(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRemainderSequence() {
        remainderSequence_helper("0", "1", "[0, 1]");
        remainderSequence_helper("0", "x", "[0, x]");
        remainderSequence_helper("0", "-4/3", "[0, -4/3]");
        remainderSequence_helper("0", "x^2-7/4*x+1/3", "[0, x^2-7/4*x+1/3]");
        remainderSequence_helper("0", "-x^3-1", "[0, -x^3-1]");
        remainderSequence_helper("0", "1/2*x^10", "[0, 1/2*x^10]");

        remainderSequence_helper("1", "0", "[1]");
        remainderSequence_helper("1", "1", "[1, 1]");
        remainderSequence_helper("1", "x", "[1, x, 1]");
        remainderSequence_helper("1", "-4/3", "[1, -4/3]");
        remainderSequence_helper("1", "x^2-7/4*x+1/3", "[1, x^2-7/4*x+1/3, 1]");
        remainderSequence_helper("1", "-x^3-1", "[1, -x^3-1, 1]");
        remainderSequence_helper("1", "1/2*x^10", "[1, 1/2*x^10, 1]");

        remainderSequence_helper("x", "0", "[x]");
        remainderSequence_helper("x", "1", "[x, 1]");
        remainderSequence_helper("x", "x", "[x, x]");
        remainderSequence_helper("x", "-4/3", "[x, -4/3]");
        remainderSequence_helper("x", "x^2-7/4*x+1/3", "[x, x^2-7/4*x+1/3, x, 1/3]");
        remainderSequence_helper("x", "-x^3-1", "[x, -x^3-1, x, -1]");
        remainderSequence_helper("x", "1/2*x^10", "[x, 1/2*x^10, x]");

        remainderSequence_helper("-4/3", "0", "[-4/3]");
        remainderSequence_helper("-4/3", "1", "[-4/3, 1]");
        remainderSequence_helper("-4/3", "x", "[-4/3, x, -4/3]");
        remainderSequence_helper("-4/3", "-4/3", "[-4/3, -4/3]");
        remainderSequence_helper("-4/3", "x^2-7/4*x+1/3", "[-4/3, x^2-7/4*x+1/3, -4/3]");
        remainderSequence_helper("-4/3", "-x^3-1", "[-4/3, -x^3-1, -4/3]");
        remainderSequence_helper("-4/3", "1/2*x^10", "[-4/3, 1/2*x^10, -4/3]");

        remainderSequence_helper("x^2-7/4*x+1/3", "0", "[x^2-7/4*x+1/3]");
        remainderSequence_helper("x^2-7/4*x+1/3", "1", "[x^2-7/4*x+1/3, 1]");
        remainderSequence_helper("x^2-7/4*x+1/3", "x", "[x^2-7/4*x+1/3, x, 1/3]");
        remainderSequence_helper("x^2-7/4*x+1/3", "-4/3", "[x^2-7/4*x+1/3, -4/3]");
        remainderSequence_helper("x^2-7/4*x+1/3", "x^2-7/4*x+1/3", "[x^2-7/4*x+1/3, x^2-7/4*x+1/3]");
        remainderSequence_helper("x^2-7/4*x+1/3", "-x^3-1",
                "[x^2-7/4*x+1/3, -x^3-1, x^2-7/4*x+1/3, -131/48*x-5/12, 32116/51483]");
        remainderSequence_helper("x^2-7/4*x+1/3", "1/2*x^10",
                "[x^2-7/4*x+1/3, 1/2*x^10, x^2-7/4*x+1/3, 1153665527/42467328*x-188201281/31850496," +
                " 68719476736/11978497333693689561]");

        remainderSequence_helper("-x^3-1", "0", "[-x^3-1]");
        remainderSequence_helper("-x^3-1", "1", "[-x^3-1, 1]");
        remainderSequence_helper("-x^3-1", "x", "[-x^3-1, x, -1]");
        remainderSequence_helper("-x^3-1", "-4/3", "[-x^3-1, -4/3]");
        remainderSequence_helper("-x^3-1", "x^2-7/4*x+1/3", "[-x^3-1, x^2-7/4*x+1/3, -131/48*x-5/12, 32116/51483]");
        remainderSequence_helper("-x^3-1", "-x^3-1", "[-x^3-1, -x^3-1]");
        remainderSequence_helper("-x^3-1", "1/2*x^10", "[-x^3-1, 1/2*x^10, -x^3-1, -1/2*x, -1]");

        remainderSequence_helper("1/2*x^10", "0", "[1/2*x^10]");
        remainderSequence_helper("1/2*x^10", "1", "[1/2*x^10, 1]");
        remainderSequence_helper("1/2*x^10", "x", "[1/2*x^10, x]");
        remainderSequence_helper("1/2*x^10", "-4/3", "[1/2*x^10, -4/3]");
        remainderSequence_helper("1/2*x^10", "x^2-7/4*x+1/3",
                "[1/2*x^10, x^2-7/4*x+1/3, 1153665527/42467328*x-188201281/31850496," +
                " 68719476736/11978497333693689561]");
        remainderSequence_helper("1/2*x^10", "-x^3-1", "[1/2*x^10, -x^3-1, -1/2*x, -1]");
        remainderSequence_helper("1/2*x^10", "1/2*x^10", "[1/2*x^10, 1/2*x^10]");

        remainderSequence_helper("x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5", "3*x^6+5*x^4-4*x^2-9*x+21",
                "[x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5, 3*x^6+5*x^4-4*x^2-9*x+21, -5/9*x^4+1/9*x^2-1/3," +
                " -117/25*x^2-9*x+441/25, 233150/19773*x-102500/6591, -1288744821/543589225]");

        remainderSequence_fail_helper("0", "0");
    }

    private static void signedRemainderSequence_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().signedRemainderSequence(readStrict(b).get()), output);
    }

    private static void signedRemainderSequence_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().signedRemainderSequence(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSignedRemainderSequence() {
        signedRemainderSequence_helper("0", "1", "[0, 1]");
        signedRemainderSequence_helper("0", "x", "[0, x]");
        signedRemainderSequence_helper("0", "-4/3", "[0, -4/3]");
        signedRemainderSequence_helper("0", "x^2-7/4*x+1/3", "[0, x^2-7/4*x+1/3]");
        signedRemainderSequence_helper("0", "-x^3-1", "[0, -x^3-1]");
        signedRemainderSequence_helper("0", "1/2*x^10", "[0, 1/2*x^10]");

        signedRemainderSequence_helper("1", "0", "[1]");
        signedRemainderSequence_helper("1", "1", "[1, 1]");
        signedRemainderSequence_helper("1", "x", "[1, x, -1]");
        signedRemainderSequence_helper("1", "-4/3", "[1, -4/3]");
        signedRemainderSequence_helper("1", "x^2-7/4*x+1/3", "[1, x^2-7/4*x+1/3, -1]");
        signedRemainderSequence_helper("1", "-x^3-1", "[1, -x^3-1, -1]");
        signedRemainderSequence_helper("1", "1/2*x^10", "[1, 1/2*x^10, -1]");

        signedRemainderSequence_helper("x", "0", "[x]");
        signedRemainderSequence_helper("x", "1", "[x, 1]");
        signedRemainderSequence_helper("x", "x", "[x, x]");
        signedRemainderSequence_helper("x", "-4/3", "[x, -4/3]");
        signedRemainderSequence_helper("x", "x^2-7/4*x+1/3", "[x, x^2-7/4*x+1/3, -x, -1/3]");
        signedRemainderSequence_helper("x", "-x^3-1", "[x, -x^3-1, -x, 1]");
        signedRemainderSequence_helper("x", "1/2*x^10", "[x, 1/2*x^10, -x]");

        signedRemainderSequence_helper("-4/3", "0", "[-4/3]");
        signedRemainderSequence_helper("-4/3", "1", "[-4/3, 1]");
        signedRemainderSequence_helper("-4/3", "x", "[-4/3, x, 4/3]");
        signedRemainderSequence_helper("-4/3", "-4/3", "[-4/3, -4/3]");
        signedRemainderSequence_helper("-4/3", "x^2-7/4*x+1/3", "[-4/3, x^2-7/4*x+1/3, 4/3]");
        signedRemainderSequence_helper("-4/3", "-x^3-1", "[-4/3, -x^3-1, 4/3]");
        signedRemainderSequence_helper("-4/3", "1/2*x^10", "[-4/3, 1/2*x^10, 4/3]");

        signedRemainderSequence_helper("x^2-7/4*x+1/3", "0", "[x^2-7/4*x+1/3]");
        signedRemainderSequence_helper("x^2-7/4*x+1/3", "1", "[x^2-7/4*x+1/3, 1]");
        signedRemainderSequence_helper("x^2-7/4*x+1/3", "x", "[x^2-7/4*x+1/3, x, -1/3]");
        signedRemainderSequence_helper("x^2-7/4*x+1/3", "-4/3", "[x^2-7/4*x+1/3, -4/3]");
        signedRemainderSequence_helper("x^2-7/4*x+1/3", "x^2-7/4*x+1/3", "[x^2-7/4*x+1/3, x^2-7/4*x+1/3]");
        signedRemainderSequence_helper("x^2-7/4*x+1/3", "-x^3-1",
                "[x^2-7/4*x+1/3, -x^3-1, -x^2+7/4*x-1/3, 131/48*x+5/12, 32116/51483]");
        signedRemainderSequence_helper("x^2-7/4*x+1/3", "1/2*x^10",
                "[x^2-7/4*x+1/3, 1/2*x^10, -x^2+7/4*x-1/3, -1153665527/42467328*x+188201281/31850496," +
                " 68719476736/11978497333693689561]");

        signedRemainderSequence_helper("-x^3-1", "0", "[-x^3-1]");
        signedRemainderSequence_helper("-x^3-1", "1", "[-x^3-1, 1]");
        signedRemainderSequence_helper("-x^3-1", "x", "[-x^3-1, x, 1]");
        signedRemainderSequence_helper("-x^3-1", "-4/3", "[-x^3-1, -4/3]");
        signedRemainderSequence_helper("-x^3-1", "x^2-7/4*x+1/3",
                "[-x^3-1, x^2-7/4*x+1/3, 131/48*x+5/12, -32116/51483]");
        signedRemainderSequence_helper("-x^3-1", "-x^3-1", "[-x^3-1, -x^3-1]");
        signedRemainderSequence_helper("-x^3-1", "1/2*x^10", "[-x^3-1, 1/2*x^10, x^3+1, 1/2*x, -1]");

        signedRemainderSequence_helper("1/2*x^10", "0", "[1/2*x^10]");
        signedRemainderSequence_helper("1/2*x^10", "1", "[1/2*x^10, 1]");
        signedRemainderSequence_helper("1/2*x^10", "x", "[1/2*x^10, x]");
        signedRemainderSequence_helper("1/2*x^10", "-4/3", "[1/2*x^10, -4/3]");
        signedRemainderSequence_helper("1/2*x^10", "x^2-7/4*x+1/3",
                "[1/2*x^10, x^2-7/4*x+1/3, -1153665527/42467328*x+188201281/31850496," +
                " -68719476736/11978497333693689561]");
        signedRemainderSequence_helper("1/2*x^10", "-x^3-1", "[1/2*x^10, -x^3-1, 1/2*x, 1]");
        signedRemainderSequence_helper("1/2*x^10", "1/2*x^10", "[1/2*x^10, 1/2*x^10]");

        signedRemainderSequence_helper("x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5", "3*x^6+5*x^4-4*x^2-9*x+21",
                "[x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5, 3*x^6+5*x^4-4*x^2-9*x+21, 5/9*x^4-1/9*x^2+1/3," +
                " 117/25*x^2+9*x-441/25, 233150/19773*x-102500/6591, -1288744821/543589225]");
        signedRemainderSequence_helper(
                "9*x^13-18*x^11-33*x^10+102*x^8+7*x^7-36*x^6-122*x^5+49*x^4+93*x^3-42*x^2-18*x+9",
                "117*x^12-198*x^10-330*x^9+816*x^7+49*x^6-216*x^5-610*x^4+196*x^3+279*x^2-84*x-18",
                "[9*x^13-18*x^11-33*x^10+102*x^8+7*x^7-36*x^6-122*x^5+49*x^4+93*x^3-42*x^2-18*x+9," +
                " 117*x^12-198*x^10-330*x^9+816*x^7+49*x^6-216*x^5-610*x^4+196*x^3+279*x^2-84*x-18," +
                " 36/13*x^11+99/13*x^10-510/13*x^8-42/13*x^7+252/13*x^6+976/13*x^5-441/13*x^4-930/13*x^3+462/13*x^2+" +
                "216/13*x-9," +
                " -10989/16*x^10-2655/2*x^9+35373/8*x^8+3027/8*x^7+3483/4*x^6-39761/4*x^5+24463/16*x^4+76939/8*x^3-" +
                "29649/8*x^2-8907/4*x+17019/16," +
                " -2228672/165649*x^9+11497792/496947*x^8-758720/496947*x^7+8858368/496947*x^6-72291808/1490841*x^5-" +
                "14747008/1490841*x^4+81689728/1490841*x^3-7130848/496947*x^2-6742336/496947*x+910304/165649," +
                " -900202097355/4850565316*x^8+4790758416807/19402261264*x^7-871080009261/38804522528*x^6+" +
                "15288527907631/38804522528*x^5-11178436305883/19402261264*x^4-5169096757231/38804522528*x^3+" +
                "13117087511715/38804522528*x^2-871080009261/38804522528*x-1515244576329/38804522528," +
                " -3841677139249510908/543561530761725025*x^7+6180347358405238902/543561530761725025*x^6-" +
                "2388192201565258842/543561530761725025*x^5+8963913324915525452/543561530761725025*x^4-" +
                "14420810502945557438/543561530761725025*x^3+346154266213885278/108712306152345005*x^2+" +
                "6180347358405238902/543561530761725025*x-2388192201565258842/543561530761725025," +
                " -6648854900739944448789496725/676140352527579535315696712*x^6+" +
                "4693072116514804907890170825/676140352527579535315696712*x^5+" +
                "15513994768393203713842159025/676140352527579535315696712*x^3-" +
                "10950501605201211451743731925/676140352527579535315696712*x^2-" +
                "6648854900739944448789496725/676140352527579535315696712*x+" +
                "4693072116514804907890170825/676140352527579535315696712," +
                " -200117670554781699308164692478544184/1807309302290980501324553958871415645*x^5+" +
                "66705890184927233102721564159514728/258187043184425785903507708410202235*x^2-" +
                "200117670554781699308164692478544184/1807309302290980501324553958871415645]"
        );

        signedRemainderSequence_helper("x^11-x^10+1", "11*x^10-10*x^9",
                "[x^11-x^10+1, 11*x^10-10*x^9, 10/121*x^9-1, -1331/10*x+121, 275311670611/285311670611]");

        signedRemainderSequence_fail_helper("0", "0");
    }

    private static void powerSums_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().powerSums(), output);
    }

    private static void powerSums_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().powerSums();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPowerSums() {
        powerSums_helper("1", "[0]");
        powerSums_helper("x", "[1, 0]");
        powerSums_helper("x^2-7/4*x+1/3", "[2, 7/4, 115/48]");
        powerSums_helper("x^3-1", "[3, 0, 0, 3]");
        powerSums_helper("x^10", "[10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]");

        powerSums_helper("x^2-2", "[2, 0, 4]");
        powerSums_helper("x^3-3/2*x^2+1/2*x", "[3, 3/2, 5/4, 9/8]");
        powerSums_helper("x^5-4*x^4+13/2*x^3-16/3*x^2+53/24*x-11/30", "[5, 4, 3, 2, 1, 0]");
        powerSums_helper("x^5+4*x^4+19/2*x^3+52/3*x^2+641/24*x+1091/30", "[5, -4, -3, -2, -1, 0]");

        powerSums_fail_helper("0");
        powerSums_fail_helper("2");
        powerSums_fail_helper("-1");
        powerSums_fail_helper("1/2*x^2");
    }

    private static void fromPowerSums_helper(@NotNull String input, @NotNull String output) {
        aeq(fromPowerSums(readRationalList(input)), output);
    }

    private static void fromPowerSums_fail_helper(@NotNull String input) {
        try {
            fromPowerSums(readRationalListWithNulls(input));
            fail();
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromPowerSums() {
        fromPowerSums_helper("[0]", "1");
        fromPowerSums_helper("[1, 0]", "x");
        fromPowerSums_helper("[2, 7/4, 115/48]", "x^2-7/4*x+1/3");
        fromPowerSums_helper("[3, 0, 0, 3]", "x^3-1");
        fromPowerSums_helper("[10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]", "x^10");

        fromPowerSums_helper("[2, 0, 4]", "x^2-2");
        fromPowerSums_helper("[3, 3/2, 5/4, 9/8]", "x^3-3/2*x^2+1/2*x");
        fromPowerSums_helper("[5, 4, 3, 2, 1, 0]", "x^5-4*x^4+13/2*x^3-16/3*x^2+53/24*x-11/30");
        fromPowerSums_helper("[5, -4, -3, -2, -1, 0]", "x^5+4*x^4+19/2*x^3+52/3*x^2+641/24*x+1091/30");

        fromPowerSums_fail_helper("[]");
        fromPowerSums_fail_helper("[1, 0, 1]");
        fromPowerSums_fail_helper("[2, 0, null]");
    }

    private static void interpolate_helper(@NotNull String input, @NotNull String output) {
        aeq(interpolate(readRationalPairList(input)), output);
    }

    private static void interpolate_fail_helper(@NotNull String input) {
        try {
            interpolate(readRationalPairListWithNulls(input));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testInterpolate() {
        interpolate_helper("[]", "0");
        interpolate_helper("[(2/3, 1/5)]", "1/5");
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
        companionMatrix_helper("x^2-7/4*x+1/3", "[[0, -1/3], [1, 7/4]]");
        companionMatrix_helper("x^3-1", "[[0, 0, 1], [1, 0, 0], [0, 1, 0]]");
        companionMatrix_helper("x^10",
                "[[0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [1, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 1, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 1, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 1, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 1, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 1, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 1, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 1, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 1, 0]]");

        companionMatrix_fail_helper("0");
        companionMatrix_fail_helper("2");
        companionMatrix_fail_helper("-1");
        companionMatrix_fail_helper("1/2*x^2");
    }

    private static void coefficientMatrix_helper(@NotNull String input, @NotNull String output) {
        aeq(coefficientMatrix(readRationalPolynomialList(input)), output);
    }

    private static void coefficientMatrix_fail_helper(@NotNull String input) {
        try {
            coefficientMatrix(readRationalPolynomialList(input));
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testCoefficientMatrix() {
        coefficientMatrix_helper("[]", "[]#0");
        coefficientMatrix_helper("[0, 1/2*x]", "[[0, 0], [1/2, 0]]");
        coefficientMatrix_helper("[1]", "[[1]]");
        coefficientMatrix_helper("[1, x, x^3-4/3]", "[[0, 0, 0, 1], [0, 0, 1, 0], [1, 0, 0, -4/3]]");
        coefficientMatrix_helper("[x^2-7/4*x+1/3, 1/2*x^10, 4]",
                "[[0, 0, 0, 0, 0, 0, 0, 0, 1, -7/4, 1/3], [1/2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]," +
                " [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4]]");

        coefficientMatrix_fail_helper("[0]");
        coefficientMatrix_fail_helper("[1, 1/2*x, -4/3]");
    }

    private static void reflect_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().reflect(), output);
    }

    @Test
    public void testReflect() {
        reflect_helper("0", "0");
        reflect_helper("1", "1");
        reflect_helper("1/2*x", "1/2*x");
        reflect_helper("-4/3", "-4/3");
        reflect_helper("x^2-4*x+7", "x^2+4*x+7");
        reflect_helper("x^2-7/4*x+1/3", "x^2+7/4*x+1/3");
        reflect_helper("1/2*x^10", "1/2*x^10");
    }

    private static void translate_helper(@NotNull String p, @NotNull String t, @NotNull String output) {
        aeq(readStrict(p).get().translate(Rational.readStrict(t).get()), output);
    }

    @Test
    public void testTranslate() {
        translate_helper("0", "0", "0");
        translate_helper("0", "1", "0");
        translate_helper("0", "-1", "0");
        translate_helper("0", "100/3", "0");
        translate_helper("0", "1/100", "0");

        translate_helper("1", "0", "1");
        translate_helper("1", "1", "1");
        translate_helper("1", "-1", "1");
        translate_helper("1", "100/3", "1");
        translate_helper("1", "1/100", "1");

        translate_helper("-4/3", "0", "-4/3");
        translate_helper("-4/3", "1", "-4/3");
        translate_helper("-4/3", "-1", "-4/3");
        translate_helper("-4/3", "100/3", "-4/3");
        translate_helper("-4/3", "1/100", "-4/3");

        translate_helper("x^2-7/4*x+1/3", "0", "x^2-7/4*x+1/3");
        translate_helper("x^2-7/4*x+1/3", "1", "x^2-15/4*x+37/12");
        translate_helper("x^2-7/4*x+1/3", "-1", "x^2+1/4*x-5/12");
        translate_helper("x^2-7/4*x+1/3", "100/3", "x^2-821/12*x+10528/9");
        translate_helper("x^2-7/4*x+1/3", "1/100", "x^2-177/100*x+658/1875");

        translate_helper("-x^3-1", "0", "-x^3-1");
        translate_helper("-x^3-1", "1", "-x^3+3*x^2-3*x");
        translate_helper("-x^3-1", "-1", "-x^3-3*x^2-3*x-2");
        translate_helper("-x^3-1", "100/3", "-x^3+100*x^2-10000/3*x+999973/27");
        translate_helper("-x^3-1", "1/100", "-x^3+3/100*x^2-3/10000*x-999999/1000000");

        translate_helper("1/2*x^10", "0", "1/2*x^10");
        translate_helper("1/2*x^10", "1",
                "1/2*x^10-5*x^9+45/2*x^8-60*x^7+105*x^6-126*x^5+105*x^4-60*x^3+45/2*x^2-5*x+1/2");
        translate_helper("1/2*x^10", "-1",
                "1/2*x^10+5*x^9+45/2*x^8+60*x^7+105*x^6+126*x^5+105*x^4+60*x^3+45/2*x^2+5*x+1/2");
        translate_helper("1/2*x^10", "100/3",
                "1/2*x^10-500/3*x^9+25000*x^8-20000000/9*x^7+3500000000/27*x^6-140000000000/27*x^5+" +
                "35000000000000/243*x^4-2000000000000000/729*x^3+25000000000000000/729*x^2-" +
                "5000000000000000000/19683*x+50000000000000000000/59049");
        translate_helper("1/2*x^10", "1/100",
                "1/2*x^10-1/20*x^9+9/4000*x^8-3/50000*x^7+21/20000000*x^6-63/5000000000*x^5+21/200000000000*x^4-" +
                "3/5000000000000*x^3+9/4000000000000000*x^2-1/200000000000000000*x+1/200000000000000000000");

        translate_helper("x+1/2", "1/2", "x");
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

        stretch_helper("-4/3", "1", "-4/3");
        stretch_helper("-4/3", "2", "-4/3");
        stretch_helper("-4/3", "1/2", "-4/3");
        stretch_helper("-4/3", "100/3", "-4/3");
        stretch_helper("-4/3", "1/100", "-4/3");

        stretch_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+1/3");
        stretch_helper("x^2-7/4*x+1/3", "2", "1/4*x^2-7/8*x+1/3");
        stretch_helper("x^2-7/4*x+1/3", "1/2", "4*x^2-7/2*x+1/3");
        stretch_helper("x^2-7/4*x+1/3", "100/3", "9/10000*x^2-21/400*x+1/3");
        stretch_helper("x^2-7/4*x+1/3", "1/100", "10000*x^2-175*x+1/3");

        stretch_helper("-x^3-1", "1", "-x^3-1");
        stretch_helper("-x^3-1", "2", "-1/8*x^3-1");
        stretch_helper("-x^3-1", "1/2", "-8*x^3-1");
        stretch_helper("-x^3-1", "100/3", "-27/1000000*x^3-1");
        stretch_helper("-x^3-1", "1/100", "-1000000*x^3-1");

        stretch_helper("1/2*x^10", "1", "1/2*x^10");
        stretch_helper("1/2*x^10", "2", "1/2048*x^10");
        stretch_helper("1/2*x^10", "1/2", "512*x^10");
        stretch_helper("1/2*x^10", "100/3", "59049/200000000000000000000*x^10");
        stretch_helper("1/2*x^10", "1/100", "50000000000000000000*x^10");

        stretch_helper("2*x-1", "2", "x-1");
        stretch_helper("x-2", "1/2", "2*x-2");

        stretch_fail_helper("x^2-7/4*x+1/3", "0");
        stretch_fail_helper("x^2-7/4*x+1/3", "-1");
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

        powerTable_helper("2*x^2-1", 0, "[1]");
        powerTable_helper("2*x^2-1", 1, "[1, x]");
        powerTable_helper("2*x^2-1", 2, "[1, x, 1/2]");
        powerTable_helper("2*x^2-1", 10, "[1, x, 1/2, 1/2*x, 1/4, 1/4*x, 1/8, 1/8*x, 1/16, 1/16*x, 1/32]");

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

        powerTable_helper("x^2-7/4*x+1/3", 10,
                "[1, x, 7/4*x-1/3, 131/48*x-7/12, 805/192*x-131/144, 14809/2304*x-805/576, 10087/1024*x-14809/6912," +
                " 1669499/110592*x-10087/3072, 10233965/442368*x-1669499/331776," +
                " 188201281/5308416*x-10233965/1327104, 1153665527/21233664*x-188201281/15925248]");

        powerTable_fail_helper("0", 10);
        powerTable_fail_helper("1", 10);
        powerTable_fail_helper("-1", 10);
        powerTable_fail_helper("3", 10);
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

        rootPower_helper("2*x^2-1", 0, "1");
        rootPower_helper("2*x^2-1", 1, "x");
        rootPower_helper("2*x^2-1", 2, "1/2");
        rootPower_helper("2*x^2-1", 10, "1/32");
        rootPower_helper("2*x^2-1", 100, "1/1125899906842624");
        rootPower_helper("2*x^2-1", 1000,
                "1/3273390607896141870013189696827599152216642046043064789483291368096133796404674554883270092325904" +
                "157150886684127560071009217256545885393053328527589376");

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

        rootPower_helper("1/3*x^3-1", 0, "1");
        rootPower_helper("1/3*x^3-1", 1, "x");
        rootPower_helper("1/3*x^3-1", 2, "x^2");
        rootPower_helper("1/3*x^3-1", 10, "27*x");
        rootPower_helper("1/3*x^3-1", 100, "5559060566555523*x");
        rootPower_helper("1/3*x^3-1", 1000,
                "760988023132059809720425867265032780727896356372077865117010037035791631439306199613044145649378522" +
                "557935351570949952010001833769302566531786879537190794573523*x");

        rootPower_helper("x^2-7/4*x+1/3", 0, "1");
        rootPower_helper("x^2-7/4*x+1/3", 1, "x");
        rootPower_helper("x^2-7/4*x+1/3", 2, "7/4*x-1/3");
        rootPower_helper("x^2-7/4*x+1/3", 10, "1153665527/21233664*x-188201281/15925248");
        rootPower_helper("x^2-7/4*x+1/3", 100,
                "253335604960232163661529033672005926060645690497368545703044739494288172222527726920254554148359185" +
                "125/96134799026584189281511108847917610543368311432460842820081486288285762013261463552*x-413274776" +
                "93394557489314212198784350059001650889191759379301296093721625989365583945551616524356374211/721010" +
                "99269938141961133331635938207907526233574345632115061114716214321509946097664");

        rootPower_fail_helper("0", 10);
        rootPower_fail_helper("1", 10);
        rootPower_fail_helper("-1", 10);
        rootPower_fail_helper("3", 10);
        rootPower_fail_helper("x^2-2", -1);
    }

    private static void realRoots_helper(@NotNull String polynomial, @NotNull String output) {
        List<Algebraic> xs = RationalPolynomial.readStrict(polynomial).get().realRoots();
        xs.forEach(Algebraic::validate);
        aeq(xs, output);
    }

    private static void realRoots_fail_helper(@NotNull String polynomial) {
        try {
            RationalPolynomial.readStrict(polynomial).get().realRoots();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRealRoots() {
        realRoots_helper("1", "[]");
        realRoots_helper("x", "[0]");
        realRoots_helper("x-1", "[1]");
        realRoots_helper("2*x-1", "[1/2]");
        realRoots_helper("x^2-2*x+1", "[1]");
        realRoots_helper("x^2-2", "[-sqrt(2), sqrt(2)]");
        realRoots_helper("x^3-x^2-2*x+2", "[-sqrt(2), 1, sqrt(2)]");
        realRoots_helper("x^2-4", "[-2, 2]");
        realRoots_helper("x^2-x-1", "[(1-sqrt(5))/2, (1+sqrt(5))/2]");
        realRoots_helper("x^5-x-1", "[root 0 of x^5-x-1]");
        realRoots_helper("x^10", "[0]");

        realRoots_helper("x-1/2", "[1/2]");
        realRoots_helper("x^2-7/4*x+1/3", "[(21-sqrt(249))/24, (21+sqrt(249))/24]");

        realRoots_fail_helper("0");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readRationalPolynomialList("[0, 1, x, -4/3, x^2-7/4*x+1/3, -x^3-1, 1/2*x^10]"),
                readRationalPolynomialList("[0, 1, x, -4/3, x^2-7/4*x+1/3, -x^3-1, 1/2*x^10]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(readStrict(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("0", 1);
        hashCode_helper("1", 63);
        hashCode_helper("x", 1024);
        hashCode_helper("-4/3", -90);
        hashCode_helper("x^2-7/4*x+1/3", 55894);
        hashCode_helper("-x^3-1", 30753);
        hashCode_helper("1/2*x^10", -1011939104);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(readRationalPolynomialList("[-x^3-1, -4/3, 0, 1, x, x^2-7/4*x+1/3, 1/2*x^10]"));
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
        readStrict_String_helper("4/3");
        readStrict_String_helper("-4/3");
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
        readStrict_String_helper("x^2-7/4*x+1/3");
        readStrict_String_helper("1/2*x^10");
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
        readStrict_String_fail_helper("1/2x");
        readStrict_String_fail_helper("x^2+1+x");
        readStrict_String_fail_helper("x^2+3*x^2");
        readStrict_String_fail_helper("2^x");
        readStrict_String_fail_helper("abc");
        readStrict_String_fail_helper("x+y");
        readStrict_String_fail_helper("y");
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
        readStrict_int_String_helper(1, "4/3");
        readStrict_int_String_helper(1, "-4/3");
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
        readStrict_int_String_helper(2, "x^2-7/4*x+1/3");
        readStrict_int_String_helper(10, "1/2*x^10");
        readStrict_int_String_fail_helper(1, "x^2");
        readStrict_int_String_fail_helper(1, "-x^2");
        readStrict_int_String_fail_helper(1, "2*x^2");
        readStrict_int_String_fail_helper(1, "-2*x^2");
        readStrict_int_String_fail_helper(1, "x^2-1");
        readStrict_int_String_fail_helper(1, "x^2-7/4*x+1/3");
        readStrict_int_String_fail_helper(9, "1/2*x^10");
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
        readStrict_int_String_fail_helper(10, "1/2x");
        readStrict_int_String_fail_helper(10, "x^2+1+x");
        readStrict_int_String_fail_helper(10, "x^2+3*x^2");
        readStrict_int_String_fail_helper(10, "2^x");
        readStrict_int_String_fail_helper(10, "abc");
        readStrict_int_String_fail_helper(10, "x+y");
        readStrict_int_String_fail_helper(10, "y");
        readStrict_int_String_fail_helper(10, "x/2");
        readStrict_int_String_bad_maxExponent_fail_helper(0, "1");
        readStrict_int_String_bad_maxExponent_fail_helper(-1, "0");
    }

    private static @NotNull List<Rational> readRationalList(@NotNull String s) {
        return Readers.readListStrict(Rational::readStrict).apply(s).get();
    }

    private static @NotNull List<Rational> readRationalListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Rational::readStrict).apply(s).get();
    }

    private static @NotNull List<RationalPolynomial> readRationalPolynomialList(@NotNull String s) {
        return Readers.readListStrict(RationalPolynomial::readStrict).apply(s).get();
    }

    private static @NotNull List<RationalPolynomial> readRationalPolynomialListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(RationalPolynomial::readStrict).apply(s).get();
    }

    private static @NotNull List<Pair<Rational, Rational>> readRationalPairList(@NotNull String s) {
        return Readers.readListStrict(
                t -> Pair.read(
                        t,
                        r -> NullableOptional.fromOptional(Rational.readStrict(r)),
                        r -> NullableOptional.fromOptional(Rational.readStrict(r))
                )
        ).apply(s).get();
    }

    private static @NotNull List<Pair<Rational, Rational>> readRationalPairListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(
                t -> Pair.read(
                        t,
                        Readers.readWithNullsStrict(Rational::readStrict),
                        Readers.readWithNullsStrict(Rational::readStrict)
                )
        ).apply(s).get();
    }
}
