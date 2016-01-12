package mho.qbar.objects;

import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
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

    private static void iteratorHelper(@NotNull String x, @NotNull String output) {
        aeq(toList(read(x).get()), output);
    }

    @Test
    public void testIterator() {
        iteratorHelper("0", "[]");
        iteratorHelper("1", "[1]");
        iteratorHelper("x", "[0, 1]");
        iteratorHelper("-17", "[-17]");
        iteratorHelper("x^2-4*x+7", "[7, -4, 1]");
        iteratorHelper("x^3-1", "[-1, 0, 0, 1]");
        iteratorHelper("3*x^10", "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3]");
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

    @Test
    public void testPow() {
        aeq(read("x+1").get().pow(0), "1");
        aeq(read("x+1").get().pow(1), "x+1");
        aeq(read("x+1").get().pow(2), "x^2+2*x+1");
        aeq(read("x+1").get().pow(3), "x^3+3*x^2+3*x+1");
        aeq(read("x+1").get().pow(4), "x^4+4*x^3+6*x^2+4*x+1");
        aeq(read("x+1").get().pow(10), "x^10+10*x^9+45*x^8+120*x^7+210*x^6+252*x^5+210*x^4+120*x^3+45*x^2+10*x+1");
        assertTrue(ZERO.pow(0) == ONE);
        assertTrue(ZERO.pow(1) == ZERO);
        assertTrue(ZERO.pow(2) == ZERO);
        assertTrue(ZERO.pow(3) == ZERO);
        assertTrue(ONE.pow(0) == ONE);
        assertTrue(ONE.pow(1) == ONE);
        assertTrue(ONE.pow(2) == ONE);
        assertTrue(ONE.pow(3) == ONE);
        assertTrue(read("-17").get().pow(0) == ONE);
        aeq(read("-17").get().pow(1), "-17");
        aeq(read("-17").get().pow(2), "289");
        aeq(read("-17").get().pow(3), "-4913");
        assertTrue(read("x^2-4*x+7").get().pow(0) == ONE);
        aeq(read("x^2-4*x+7").get().pow(1), "x^2-4*x+7");
        aeq(read("x^2-4*x+7").get().pow(2), "x^4-8*x^3+30*x^2-56*x+49");
        aeq(read("x^2-4*x+7").get().pow(3), "x^6-12*x^5+69*x^4-232*x^3+483*x^2-588*x+343");
        assertTrue(read("-x^3-1").get().pow(0) == ONE);
        aeq(read("-x^3-1").get().pow(1), "-x^3-1");
        aeq(read("-x^3-1").get().pow(2), "x^6+2*x^3+1");
        aeq(read("-x^3-1").get().pow(3), "-x^9-3*x^6-3*x^3-1");
        assertTrue(read("3*x^10").get().pow(0) == ONE);
        aeq(read("3*x^10").get().pow(1), "3*x^10");
        aeq(read("3*x^10").get().pow(2), "9*x^20");
        aeq(read("3*x^10").get().pow(3), "27*x^30");
    }

    @Test
    public void testSubstitute() {
        assertTrue(ZERO.substitute(ZERO) == ZERO);
        assertTrue(ZERO.substitute(ONE) == ZERO);
        assertTrue(ZERO.substitute(X) == ZERO);
        assertTrue(ZERO.substitute(read("-17").get()) == ZERO);
        assertTrue(ZERO.substitute(read("x^2-4*x+7").get()) == ZERO);
        assertTrue(ZERO.substitute(read("-x^3-1").get()) == ZERO);
        assertTrue(ZERO.substitute(read("3*x^10").get()) == ZERO);
        assertTrue(ONE.substitute(ZERO) == ONE);
        assertTrue(ONE.substitute(ONE) == ONE);
        assertTrue(ONE.substitute(X) == ONE);
        assertTrue(ONE.substitute(read("-17").get()) == ONE);
        assertTrue(ONE.substitute(read("x^2-4*x+7").get()) == ONE);
        assertTrue(ONE.substitute(read("-x^3-1").get()) == ONE);
        assertTrue(ONE.substitute(read("3*x^10").get()) == ONE);
        assertTrue(X.substitute(ZERO) == ZERO);
        assertTrue(X.substitute(ONE) == ONE);
        aeq(X.substitute(X), "x");
        aeq(X.substitute(read("-17").get()), "-17");
        aeq(X.substitute(read("x^2-4*x+7").get()), "x^2-4*x+7");
        aeq(X.substitute(read("-x^3-1").get()), "-x^3-1");
        aeq(X.substitute(read("3*x^10").get()), "3*x^10");
        aeq(read("-17").get().substitute(ZERO), "-17");
        aeq(read("-17").get().substitute(ONE), "-17");
        aeq(read("-17").get().substitute(X), "-17");
        aeq(read("-17").get().substitute(read("-17").get()), "-17");
        aeq(read("-17").get().substitute(read("x^2-4*x+7").get()), "-17");
        aeq(read("-17").get().substitute(read("-x^3-1").get()), "-17");
        aeq(read("-17").get().substitute(read("3*x^10").get()), "-17");
        aeq(read("x^2-4*x+7").get().substitute(ZERO), "7");
        aeq(read("x^2-4*x+7").get().substitute(ONE), "4");
        aeq(read("x^2-4*x+7").get().substitute(X), "x^2-4*x+7");
        aeq(read("x^2-4*x+7").get().substitute(read("-17").get()), "364");
        aeq(read("x^2-4*x+7").get().substitute(read("x^2-4*x+7").get()), "x^4-8*x^3+26*x^2-40*x+28");
        aeq(read("x^2-4*x+7").get().substitute(read("-x^3-1").get()), "x^6+6*x^3+12");
        aeq(read("x^2-4*x+7").get().substitute(read("3*x^10").get()), "9*x^20-12*x^10+7");
        aeq(read("-x^3-1").get().substitute(ZERO), "-1");
        aeq(read("-x^3-1").get().substitute(ONE), "-2");
        aeq(read("-x^3-1").get().substitute(X), "-x^3-1");
        aeq(read("-x^3-1").get().substitute(read("-17").get()), "4912");
        aeq(read("-x^3-1").get().substitute(read("x^2-4*x+7").get()), "-x^6+12*x^5-69*x^4+232*x^3-483*x^2+588*x-344");
        aeq(read("-x^3-1").get().substitute(read("-x^3-1").get()), "x^9+3*x^6+3*x^3");
        aeq(read("-x^3-1").get().substitute(read("3*x^10").get()), "-27*x^30-1");
        assertTrue(read("3*x^10").get().substitute(ZERO) == ZERO);
        aeq(read("3*x^10").get().substitute(ONE), "3");
        aeq(read("3*x^10").get().substitute(X), "3*x^10");
        aeq(read("3*x^10").get().substitute(read("-17").get()), "6047981701347");
        aeq(read("3*x^10").get().substitute(read("x^2-4*x+7").get()),
                "3*x^20-120*x^19+2370*x^18-30600*x^17+288855*x^16-2114784*x^15+12441240*x^14-60158880*x^13" +
                "+242643510*x^12-823956240*x^11+2367787980*x^10-5767693680*x^9+11889531990*x^8-20634495840*x^7" +
                "+29871417240*x^6-35543174688*x^5+33983501895*x^4-25200415800*x^3+13662578370*x^2-4842432840*x" +
                "+847425747");
        aeq(read("3*x^10").get().substitute(read("-x^3-1").get()),
                "3*x^30+30*x^27+135*x^24+360*x^21+630*x^18+756*x^15+630*x^12+360*x^9+135*x^6+30*x^3+3");
        aeq(read("3*x^10").get().substitute(read("3*x^10").get()), "177147*x^100");
    }

    private static void differentiateHelper(@NotNull String x, @NotNull String output) {
        Polynomial derivative = read(x).get().differentiate();
        derivative.validate();
        aeq(derivative, output);
    }

    @Test
    public void testDifferentiate() {
        differentiateHelper("0", "0");
        differentiateHelper("1", "0");
        differentiateHelper("-17", "0");
        differentiateHelper("x", "1");
        differentiateHelper("x^2-4*x+7", "2*x-4");
        differentiateHelper("3*x^10", "30*x^9");
    }

    @Test
    public void testIsMonic() {
        assertFalse(ZERO.isMonic());
        assertTrue(ONE.isMonic());
        assertTrue(X.isMonic());
        assertFalse(read("-17").get().isMonic());
        assertTrue(read("x^2-4*x+7").get().isMonic());
        assertFalse(read("-x^3-1").get().isMonic());
        assertFalse(read("3*x^10").get().isMonic());
    }

    @Test
    public void testIsPrimitive() {
        assertFalse(ZERO.isPrimitive());
        assertTrue(ONE.isPrimitive());
        assertTrue(X.isPrimitive());
        assertFalse(read("-17").get().isPrimitive());
        assertTrue(read("x^2-4*x+7").get().isPrimitive());
        assertFalse(read("6*x^2-4*x+8").get().isPrimitive());
        assertFalse(read("-x^3-1").get().isPrimitive());
        assertFalse(read("3*x^10").get().isPrimitive());
    }

    @Test
    public void testContentAndPrimitive() {
        aeq(ONE.contentAndPrimitive(), "(1, 1)");
        aeq(X.contentAndPrimitive(), "(1, x)");
        aeq(read("-17").get().contentAndPrimitive(), "(-17, 1)");
        aeq(read("x^2-4*x+7").get().contentAndPrimitive(), "(1, x^2-4*x+7)");
        aeq(read("6*x^2-4*x+8").get().contentAndPrimitive(), "(2, 3*x^2-2*x+4)");
        aeq(read("-x^3-1").get().contentAndPrimitive(), "(-1, x^3+1)");
        aeq(read("3*x^10").get().contentAndPrimitive(), "(3, x^10)");
        try {
            ZERO.contentAndPrimitive();
            fail();
        } catch (ArithmeticException ignored) {}
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

    @Test
    public void testRead() {
        assertTrue(read("0").get() == ZERO);
        assertTrue(read("1").get() == ONE);
        aeq(read("x").get(), X);
        aeq(read("2").get(), "2");
        aeq(read("-2").get(), "-2");
        aeq(read("-x").get(), "-x");
        aeq(read("3*x").get(), "3*x");
        aeq(read("-3*x").get(), "-3*x");
        aeq(read("x^2").get(), "x^2");
        aeq(read("-x^2").get(), "-x^2");
        aeq(read("2*x^2").get(), "2*x^2");
        aeq(read("-2*x^2").get(), "-2*x^2");
        aeq(read("x-1").get(), "x-1");
        aeq(read("-x-1").get(), "-x-1");
        aeq(read("x^2-1").get(), "x^2-1");
        aeq(read("x^2-4*x+7").get(), "x^2-4*x+7");
        aeq(read("3*x^10").get(), "3*x^10");
        assertFalse(read("").isPresent());
        assertFalse(read("+").isPresent());
        assertFalse(read("-").isPresent());
        assertFalse(read("-0").isPresent());
        assertFalse(read("+0").isPresent());
        assertFalse(read("--").isPresent());
        assertFalse(read("+1").isPresent());
        assertFalse(read("+x").isPresent());
        assertFalse(read("+x^2").isPresent());
        assertFalse(read("+x^2-x").isPresent());
        assertFalse(read("x^1000000000000").isPresent());
        assertFalse(read(" x").isPresent());
        assertFalse(read("x ").isPresent());
        assertFalse(read("X").isPresent());
        assertFalse(read("x + 1").isPresent());
        assertFalse(read("x^0").isPresent());
        assertFalse(read("x^-1").isPresent());
        assertFalse(read("x^1").isPresent());
        assertFalse(read("1-2").isPresent());
        assertFalse(read("1*x").isPresent());
        assertFalse(read("-1*x").isPresent());
        assertFalse(read("1*x^2").isPresent());
        assertFalse(read("-1*x^2").isPresent());
        assertFalse(read("x+x").isPresent());
        assertFalse(read("x+x^2").isPresent());
        assertFalse(read("1+x").isPresent());
        assertFalse(read("x+0").isPresent());
        assertFalse(read("0*x").isPresent());
        assertFalse(read("-0*x").isPresent());
        assertFalse(read("+0*x").isPresent());
        assertFalse(read("2x").isPresent());
        assertFalse(read("x^2+1+x").isPresent());
        assertFalse(read("x^2+3*x^2").isPresent());
        assertFalse(read("2^x").isPresent());
        assertFalse(read("abc").isPresent());
        assertFalse(read("x+y").isPresent());
        assertFalse(read("y").isPresent());
        assertFalse(read("1/2").isPresent());
        assertFalse(read("x/2").isPresent());
    }

    @Test
    public void testFindIn() {
        aeq(findIn("0123").get(), "(0, 0)");
        aeq(findIn("yxy").get(), "(x, 1)");
        aeq(findIn("ax+12b").get(), "(x+12, 1)");
        aeq(findIn("------x------").get(), "(-x, 5)");
        aeq(findIn("3*x^2z").get(), "(3*x^2, 0)");
        aeq(findIn("1+x+x^2").get(), "(1, 0)");
        aeq(findIn("+1").get(), "(1, 1)");
        aeq(findIn("y^12").get(), "(12, 2)");
        aeq(findIn("52*x^-10").get(), "(52*x, 0)");
        assertFalse(findIn("").isPresent());
        assertFalse(findIn("o").isPresent());
        assertFalse(findIn("hello").isPresent());
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
