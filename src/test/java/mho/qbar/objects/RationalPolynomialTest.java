package mho.qbar.objects;

import mho.wheels.io.Readers;
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
        aeq(toList(read(x).get()), output);
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
        aeq(read(p).get().apply(Rational.read(x).get()), output);
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

    private static void hasIntegralCoefficients_helper(@NotNull String input, boolean output) {
        aeq(read(input).get().hasIntegralCoefficients(), output);
    }

    @Test
    public void testHasIntegralCoefficients() {
        hasIntegralCoefficients_helper("0", true);
        hasIntegralCoefficients_helper("1", true);
        hasIntegralCoefficients_helper("x", true);
        hasIntegralCoefficients_helper("-17", true);
        hasIntegralCoefficients_helper("x^2-4*x+7", true);
        hasIntegralCoefficients_helper("x^3-1", true);
        hasIntegralCoefficients_helper("3*x^10", true);
        hasIntegralCoefficients_helper("-4/3", false);
        hasIntegralCoefficients_helper("x^2-7/4*x+1/3", false);
        hasIntegralCoefficients_helper("1/2*x^10", false);
    }

    private static void toPolynomial_helper(@NotNull String input) {
        aeq(read(input).get().toPolynomial(), input);
    }

    private static void toPolynomial_fail_helper(@NotNull String input) {
        try {
            read(input).get().toPolynomial();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testToRationalPolynomial() {
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
        aeq(of(Rational.read(input).get()), input);
    }

    @Test
    public void testOf_Rational() {
        of_Rational_helper("0");
        of_Rational_helper("1");
        of_Rational_helper("5/3");
        of_Rational_helper("-1/7");
    }

    private static void of_Rational_int_helper(@NotNull String input, int i, @NotNull String output) {
        aeq(of(Rational.read(input).get(), i), output);
    }

    private static void of_Rational_int_fail_helper(@NotNull String input, int i) {
        try {
            of(Rational.read(input).get(), i);
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

    private static void degree_helper(@NotNull String input, int output) {
        aeq(read(input).get().degree(), output);
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
        aeq(read(input).get().leading().get(), output);
    }

    private static void leading_empty_helper(@NotNull String input) {
        assertFalse(read(input).get().leading().isPresent());
    }

    @Test
    public void testLeading() {
        leading_helper("-4/3", "-4/3");
        leading_helper("x^2-7/4*x+1/3", "1");
        leading_helper("-x^3-1", "-1");
        leading_helper("1/2*x^10", "1/2");
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
        aeq(read(input).get().negate(), output);
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
        aeq(read(input).get().abs(), output);
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
        aeq(read(input).get().signum(), output);
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

    private static void subtract_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().subtract(read(b).get()), output);
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
        aeq(read(a).get().multiply(read(b).get()), output);
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
        aeq(read(p).get().multiply(Rational.read(r).get()), output);
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
        aeq(read(p).get().divide(Rational.read(r).get()), output);
    }

    private static void divide_Rational_fail_helper(@NotNull String p, @NotNull String r) {
        try {
            read(p).get().divide(Rational.read(r).get());
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
        aeq(read(p).get().divide(Readers.readBigInteger(i).get()), output);
    }

    private static void divide_BigInteger_fail_helper(@NotNull String p, @NotNull String i) {
        try {
            read(p).get().divide(Readers.readBigInteger(i).get());
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
        aeq(read(p).get().divide(i), output);
    }

    private static void divide_int_fail_helper(@NotNull String p, int i) {
        try {
            read(p).get().divide(i);
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
        aeq(read(p).get().shiftLeft(bits), output);
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
        aeq(read(p).get().shiftRight(bits), output);
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
        RationalPolynomial seed = read("x+1/2").get();
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
        aeq(read(a).get().substitute(read(b).get()), output);
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
        aeq(read(input).get().differentiate(), output);
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
        aeq(read(input).get().isMonic(), output);
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
        aeq(read(input).get().makeMonic(), output);
    }

    private static void makeMonic_fail_helper(@NotNull String input) {
        try {
            read(input).get().makeMonic();
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
        Pair<Rational, Polynomial> result = read(input).get().constantFactor();
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
        Pair<RationalPolynomial, RationalPolynomial> result = read(a).get().divide(read(b).get());
        aeq(result.a, quotient);
        aeq(result.b, remainder);
    }

    private static void divide_RationalPolynomial_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().divide(read(b).get());
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
        aeq(read(a).get().remainderSequence(read(b).get()), output);
    }

    private static void remainderSequence_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().remainderSequence(read(b).get());
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
        aeq(read(a).get().signedRemainderSequence(read(b).get()), output);
    }

    private static void signedRemainderSequence_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().signedRemainderSequence(read(b).get());
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

        signedRemainderSequence_fail_helper("0", "0");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readRationalPolynomialList("[0, 1, x, -4/3, x^2-7/4*x+1/3, -x^3-1, 1/2*x^10]"),
                readRationalPolynomialList("[0, 1, x, -4/3, x^2-7/4*x+1/3, -x^3-1, 1/2*x^10]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
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
        read_String_helper("4/3");
        read_String_helper("-4/3");
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
        read_String_helper("x^2-7/4*x+1/3");
        read_String_helper("1/2*x^10");
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
        read_String_fail_helper("1/2x");
        read_String_fail_helper("x^2+1+x");
        read_String_fail_helper("x^2+3*x^2");
        read_String_fail_helper("2^x");
        read_String_fail_helper("abc");
        read_String_fail_helper("x+y");
        read_String_fail_helper("y");
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
        read_int_String_helper(1, "4/3");
        read_int_String_helper(1, "-4/3");
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
        read_int_String_helper(2, "x^2-7/4*x+1/3");
        read_int_String_helper(10, "1/2*x^10");
        read_int_String_fail_helper(1, "x^2");
        read_int_String_fail_helper(1, "-x^2");
        read_int_String_fail_helper(1, "2*x^2");
        read_int_String_fail_helper(1, "-2*x^2");
        read_int_String_fail_helper(1, "x^2-1");
        read_int_String_fail_helper(1, "x^2-7/4*x+1/3");
        read_int_String_fail_helper(9, "1/2*x^10");
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
        read_int_String_fail_helper(10, "1/2x");
        read_int_String_fail_helper(10, "x^2+1+x");
        read_int_String_fail_helper(10, "x^2+3*x^2");
        read_int_String_fail_helper(10, "2^x");
        read_int_String_fail_helper(10, "abc");
        read_int_String_fail_helper(10, "x+y");
        read_int_String_fail_helper(10, "y");
        read_int_String_fail_helper(10, "x/2");
        read_int_String_bad_maxExponent_fail_helper(0, "1");
        read_int_String_bad_maxExponent_fail_helper(-1, "0");
    }

    private static void findIn_String_helper(@NotNull String input, @NotNull String output, int index) {
        Pair<RationalPolynomial, Integer> result = findIn(input).get();
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
        findIn_String_helper("2/4*x", "2", 0);
        findIn_String_helper("x/2", "x", 0);
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
        Pair<RationalPolynomial, Integer> result = findIn(maxExponent, input).get();
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
        findIn_int_String_helper(2, "2/4*x", "2", 0);
        findIn_int_String_helper(2, "x/2", "x", 0);
        findIn_int_String_fail_helper(2, "");
        findIn_int_String_fail_helper(2, "o");
        findIn_int_String_fail_helper(2, "hello");
        findIn_int_String_bad_maxExponent_fail_helper(0, "1");
        findIn_int_String_bad_maxExponent_fail_helper(-1, "0");
    }

    private static @NotNull List<Rational> readRationalList(@NotNull String s) {
        return Readers.readList(Rational::read).apply(s).get();
    }

    private static @NotNull List<Rational> readRationalListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Rational::read).apply(s).get();
    }

    private static @NotNull List<RationalPolynomial> readRationalPolynomialList(@NotNull String s) {
        return Readers.readList(RationalPolynomial::read).apply(s).get();
    }

    private static @NotNull List<RationalPolynomial> readRationalPolynomialListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(RationalPolynomial::read).apply(s).get();
    }
}
