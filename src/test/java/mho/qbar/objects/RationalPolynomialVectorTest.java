package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.ExhaustiveProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.RationalPolynomialVector.*;
import static mho.qbar.objects.RationalPolynomialVector.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.fail;

public class RationalPolynomialVectorTest {
    private static void constant_helper(@NotNull RationalPolynomialVector input, @NotNull String output) {
        input.validate();
        aeq(input, output);
    }

    @Test
    public void testConstants() {
        constant_helper(ZERO_DIMENSIONAL, "[]");
    }

    private static void iterator_helper(@NotNull String input) {
        aeq(toList(readStrict(input).get()), input);
    }

    @Test
    public void testIterator() {
        iterator_helper("[]");
        iterator_helper("[1/2*x]");
        iterator_helper("[5/3, -1/4*x+3, 23*x^5]");
    }

    private static void onlyHasIntegralCoordinates_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().onlyHasIntegralCoordinates(), output);
    }

    @Test
    public void testHasIntegralCoordinates() {
        onlyHasIntegralCoordinates_helper("[]", true);
        onlyHasIntegralCoordinates_helper("[5*x]", true);
        onlyHasIntegralCoordinates_helper("[12, -4, 0, x+1]", true);
        onlyHasIntegralCoordinates_helper("[1/2*x]", false);
        onlyHasIntegralCoordinates_helper("[5/3, -1/4*x+3, 23*x^5]", false);
    }

    private static void toPolynomialVector_helper(@NotNull String input) {
        aeq(readStrict(input).get().toPolynomialVector(), input);
    }

    private static void toPolynomialVector_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().toPolynomialVector();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testToVector() {
        toPolynomialVector_helper("[]");
        toPolynomialVector_helper("[5*x]");
        toPolynomialVector_helper("[12, -4, 0, x+1]");

        toPolynomialVector_fail_helper("[1/2*x]");
        toPolynomialVector_fail_helper("[5/3, -1/4*x+3, 23*x^5]");
    }

    private static void get_helper(@NotNull String input, int i, @NotNull String output) {
        aeq(readStrict(input).get().get(i), output);
    }

    private static void get_fail_helper(@NotNull String input, int i) {
        try {
            readStrict(input).get().get(i);
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testGet() {
        get_helper("[1/2*x]", 0, "1/2*x");
        get_helper("[5/3, -1/4*x+3, 23*x^5]", 0, "5/3");
        get_helper("[5/3, -1/4*x+3, 23*x^5]", 1, "-1/4*x+3");
        get_helper("[5/3, -1/4*x+3, 23*x^5]", 2, "23*x^5");

        get_fail_helper("[5/3, -1/4, 23]", 4);
        get_fail_helper("[1/2]", 3);
        get_fail_helper("[]", 0);
    }

    private static void of_List_RationalPolynomial_helper(@NotNull String input) {
        RationalPolynomialVector v = of(readRationalPolynomialList(input));
        v.validate();
        aeq(v, input);
    }

    private static void of_List_RationalPolynomial_fail_helper(@NotNull String input) {
        try {
            of(readRationalPolynomialListWithNulls(input));
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testOf_List_Rational() {
        of_List_RationalPolynomial_helper("[]");
        of_List_RationalPolynomial_helper("[1/2*x]");
        of_List_RationalPolynomial_helper("[5/3, -1/4*x+3, 23*x^5]");

        of_List_RationalPolynomial_fail_helper("[5/3, null, 23*x^5]");
    }

    private static void of_RationalPolynomial_helper(@NotNull String input, @NotNull String output) {
        RationalPolynomialVector v = of(RationalPolynomial.readStrict(input).get());
        v.validate();
        aeq(v, output);
    }

    @Test
    public void testOf_RationalPolynomial() {
        of_RationalPolynomial_helper("1/2*x", "[1/2*x]");
        of_RationalPolynomial_helper("-5", "[-5]");
        of_RationalPolynomial_helper("0", "[0]");
    }

    private static void of_RationalVector_helper(@NotNull String input) {
        RationalPolynomialVector v = of(RationalVector.readStrict(input).get());
        v.validate();
        aeq(v, input);
    }

    @Test
    public void testOf_RationalVector() {
        of_RationalVector_helper("[]");
        of_RationalVector_helper("[1/2]");
        of_RationalVector_helper("[5/3, -1/4, 23]");
    }

    private static void maxCoordinateBitLength_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().maxCoordinateBitLength(), output);
    }

    @Test
    public void testMaxCoordinateBitLength() {
        maxCoordinateBitLength_helper("[]", 0);
        maxCoordinateBitLength_helper("[1/2*x]", 3);
        maxCoordinateBitLength_helper("[5/3, -1/4*x+3, 23*x^5]", 6);
    }

    private static void dimension_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().dimension(), output);
    }

    @Test
    public void testDimension() {
        dimension_helper("[]", 0);
        dimension_helper("[1/2*x]", 1);
        dimension_helper("[5/3, -1/4*x+3, 23*x^5]", 3);
    }

    private static void testIsZero(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isZero(), output);
    }

    @Test
    public void testIsZero() {
        testIsZero("[]", true);
        testIsZero("[0]", true);
        testIsZero("[0, 0, 0]", true);
        testIsZero("[1/2*x]", false);
        testIsZero("[0, 0, -1/4*x+3]", false);
    }

    private static void zero_helper(int dimension, @NotNull String output) {
        RationalPolynomialVector v = zero(dimension);
        v.validate();
        aeq(v, output);
    }

    private static void zero_fail_helper(int dimension) {
        try {
            zero(dimension);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testZero() {
        zero_helper(0, "[]");
        zero_helper(1, "[0]");
        zero_helper(3, "[0, 0, 0]");
        zero_helper(10, "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]");

        zero_fail_helper(-1);
    }

    private void standard_helper(int dimension, int i, @NotNull String output) {
        RationalPolynomialVector v = standard(dimension, i);
        v.validate();
        aeq(v, output);
    }

    private void standard_fail_helper(int dimension, int i) {
        try {
            standard(dimension, i);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testStandard() {
        standard_helper(1, 0, "[1]");
        standard_helper(3, 0, "[1, 0, 0]");
        standard_helper(3, 1, "[0, 1, 0]");
        standard_helper(3, 2, "[0, 0, 1]");
        standard_helper(10, 6, "[0, 0, 0, 0, 0, 0, 1, 0, 0, 0]");

        standard_fail_helper(2, -4);
        standard_fail_helper(-3, -4);
        standard_fail_helper(2, 3);
        standard_fail_helper(0, 0);
    }

    private static void add_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        RationalPolynomialVector v = readStrict(a).get().add(readStrict(b).get());
        v.validate();
        aeq(v, output);
    }

    private static void add_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().add(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testAdd() {
        add_helper("[]", "[]", "[]");
        add_helper("[1/2*x]", "[x^2]", "[x^2+1/2*x]");
        add_helper("[5/3, -1/4*x+3, 23*x^5]", "[-2, 1, 3]", "[-1/3, -1/4*x+4, 23*x^5+3]");
        add_helper("[5/3, -1/4*x+3, 23*x^5]", "[0, 0, 0]", "[5/3, -1/4*x+3, 23*x^5]");
        add_helper("[5/3, -1/4*x+3, 23*x^5]", "[-5/3, 1/4*x-3, -23*x^5]", "[0, 0, 0]");

        add_fail_helper("[]", "[1/2*x]");
        add_fail_helper("[1/2*x]", "[]");
        add_fail_helper("[5/3, -1/4*x+3, 23*x^5]", "[5/6, 2/3]");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        RationalPolynomialVector v = readStrict(input).get().negate();
        v.validate();
        aeq(v, output);
    }

    @Test
    public void testNegate() {
        negate_helper("[]", "[]");
        negate_helper("[1/2*x]", "[-1/2*x]");
        negate_helper("[5/3, -1/4, 23]", "[-5/3, 1/4, -23]");
    }

    private static void subtract_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        RationalPolynomialVector v = readStrict(a).get().subtract(readStrict(b).get());
        v.validate();
        aeq(v, output);
    }

    private static void subtract_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().subtract(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSubtract() {
        subtract_helper("[]", "[]", "[]");
        subtract_helper("[1/2*x]", "[x^2]", "[-x^2+1/2*x]");
        subtract_helper("[5/3, -1/4*x+3, 23*x^5]", "[-2, 1, 3]", "[11/3, -1/4*x+2, 23*x^5-3]");
        subtract_helper("[5/3, -1/4*x+3, 23*x^5]", "[0, 0, 0]", "[5/3, -1/4*x+3, 23*x^5]");
        subtract_helper("[5/3, -1/4*x+3, 23*x^5]", "[5/3, -1/4*x+3, 23*x^5]", "[0, 0, 0]");
        subtract_helper("[0, 0, 0]", "[5/3, -1/4*x+3, 23*x^5]", "[-5/3, 1/4*x-3, -23*x^5]");

        subtract_fail_helper("[]", "[1/2*x]");
        subtract_fail_helper("[1/2*x]", "[]");
        subtract_fail_helper("[5/3, -1/4*x+3, 23*x^5]", "[5/6, 2/3]");
    }

    private static void multiply_RationalPolynomial_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        RationalPolynomialVector v = readStrict(a).get().multiply(RationalPolynomial.readStrict(b).get());
        v.validate();
        aeq(v, output);
    }

    @Test
    public void testMultiply_RationalPolynomial() {
        multiply_RationalPolynomial_helper("[]", "0", "[]");
        multiply_RationalPolynomial_helper("[]", "1", "[]");
        multiply_RationalPolynomial_helper("[]", "x", "[]");
        multiply_RationalPolynomial_helper("[]", "-1/4*x+3", "[]");

        multiply_RationalPolynomial_helper("[x]", "0", "[0]");
        multiply_RationalPolynomial_helper("[x]", "1", "[x]");
        multiply_RationalPolynomial_helper("[x]", "x", "[x^2]");
        multiply_RationalPolynomial_helper("[x]", "-1/4*x+3", "[-1/4*x^2+3*x]");

        multiply_RationalPolynomial_helper("[5/3, -1/4*x+3, 23*x^5]", "0", "[0, 0, 0]");
        multiply_RationalPolynomial_helper("[5/3, -1/4*x+3, 23*x^5]", "1", "[5/3, -1/4*x+3, 23*x^5]");
        multiply_RationalPolynomial_helper("[5/3, -1/4*x+3, 23*x^5]", "x", "[5/3*x, -1/4*x^2+3*x, 23*x^6]");
        multiply_RationalPolynomial_helper("[5/3, -1/4*x+3, 23*x^5]", "-1/4*x+3",
                "[-5/12*x+5, 1/16*x^2-3/2*x+9, -23/4*x^6+69*x^5]");
    }

    private static void multiply_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        RationalPolynomialVector v = readStrict(a).get().multiply(Rational.readStrict(b).get());
        v.validate();
        aeq(v, output);
    }

    @Test
    public void testMultiply_Rational() {
        multiply_Rational_helper("[]", "0", "[]");
        multiply_Rational_helper("[]", "1", "[]");
        multiply_Rational_helper("[]", "-3/2", "[]");

        multiply_Rational_helper("[x]", "0", "[0]");
        multiply_Rational_helper("[x]", "1", "[x]");
        multiply_Rational_helper("[x]", "-3/2", "[-3/2*x]");

        multiply_Rational_helper("[5/3, -1/4*x+3, 23*x^5]", "0", "[0, 0, 0]");
        multiply_Rational_helper("[5/3, -1/4*x+3, 23*x^5]", "1", "[5/3, -1/4*x+3, 23*x^5]");
        multiply_Rational_helper("[5/3, -1/4*x+3, 23*x^5]", "-3/2", "[-5/2, 3/8*x-9/2, -69/2*x^5]");
    }

    private static void multiply_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        RationalPolynomialVector v = readStrict(a).get().multiply(Readers.readBigIntegerStrict(b).get());
        v.validate();
        aeq(v, output);
    }

    @Test
    public void testMultiply_BigInteger() {
        multiply_BigInteger_helper("[]", "0", "[]");
        multiply_BigInteger_helper("[]", "1", "[]");
        multiply_BigInteger_helper("[]", "5", "[]");

        multiply_BigInteger_helper("[x]", "0", "[0]");
        multiply_BigInteger_helper("[x]", "1", "[x]");
        multiply_BigInteger_helper("[x]", "5", "[5*x]");

        multiply_BigInteger_helper("[5/3, -1/4*x+3, 23*x^5]", "0", "[0, 0, 0]");
        multiply_BigInteger_helper("[5/3, -1/4*x+3, 23*x^5]", "1", "[5/3, -1/4*x+3, 23*x^5]");
        multiply_BigInteger_helper("[5/3, -1/4*x+3, 23*x^5]", "5", "[25/3, -5/4*x+15, 115*x^5]");
    }

    private static void multiply_int_helper(@NotNull String a, int b, @NotNull String output) {
        RationalPolynomialVector v = readStrict(a).get().multiply(b);
        v.validate();
        aeq(v, output);
    }

    @Test
    public void testMultiply_int() {
        multiply_int_helper("[]", 0, "[]");
        multiply_int_helper("[]", 1, "[]");
        multiply_int_helper("[]", 5, "[]");

        multiply_int_helper("[x]", 0, "[0]");
        multiply_int_helper("[x]", 1, "[x]");
        multiply_int_helper("[x]", 5, "[5*x]");

        multiply_int_helper("[5/3, -1/4*x+3, 23*x^5]", 0, "[0, 0, 0]");
        multiply_int_helper("[5/3, -1/4*x+3, 23*x^5]", 1, "[5/3, -1/4*x+3, 23*x^5]");
        multiply_int_helper("[5/3, -1/4*x+3, 23*x^5]", 5, "[25/3, -5/4*x+15, 115*x^5]");
    }

    private static void divide_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        RationalPolynomialVector v = readStrict(a).get().divide(Rational.readStrict(b).get());
        v.validate();
        aeq(v, output);
    }

    private static void divide_Rational_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().divide(Rational.readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_Rational() {
        divide_Rational_helper("[]", "1", "[]");
        divide_Rational_helper("[]", "-3/2", "[]");

        divide_Rational_helper("[x]", "1", "[x]");
        divide_Rational_helper("[x]", "-3/2", "[-2/3*x]");

        divide_Rational_helper("[5/3, -1/4*x+3, 23*x^5]", "1", "[5/3, -1/4*x+3, 23*x^5]");
        divide_Rational_helper("[5/3, -1/4*x+3, 23*x^5]", "-3/2", "[-10/9, 1/6*x-2, -46/3*x^5]");

        divide_Rational_fail_helper("[]", "0");
        divide_Rational_fail_helper("[5/3, -1/4*x+3, 23*x^5]", "0");
    }

    private static void divide_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        RationalPolynomialVector v = readStrict(a).get().divide(Readers.readBigIntegerStrict(b).get());
        v.validate();
        aeq(v, output);
    }

    private static void divide_BigInteger_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().divide(Readers.readBigIntegerStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_BigInteger() {
        divide_BigInteger_helper("[]", "1", "[]");
        divide_BigInteger_helper("[]", "5", "[]");

        divide_BigInteger_helper("[x]", "1", "[x]");
        divide_BigInteger_helper("[x]", "5", "[1/5*x]");

        divide_BigInteger_helper("[5/3, -1/4*x+3, 23*x^5]", "1", "[5/3, -1/4*x+3, 23*x^5]");
        divide_BigInteger_helper("[5/3, -1/4*x+3, 23*x^5]", "5", "[1/3, -1/20*x+3/5, 23/5*x^5]");

        divide_BigInteger_fail_helper("[]", "0");
        divide_BigInteger_fail_helper("[5/3, -1/4*x+3, 23*x^5]", "0");
    }

    private static void divide_int_helper(@NotNull String a, int b, @NotNull String output) {
        RationalPolynomialVector v = readStrict(a).get().divide(b);
        v.validate();
        aeq(v, output);
    }

    private static void divide_int_fail_helper(@NotNull String a, int b) {
        try {
            readStrict(a).get().divide(b);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_int() {
        divide_int_helper("[]", 1, "[]");
        divide_int_helper("[]", 5, "[]");

        divide_int_helper("[x]", 1, "[x]");
        divide_int_helper("[x]", 5, "[1/5*x]");

        divide_int_helper("[5/3, -1/4*x+3, 23*x^5]", 1, "[5/3, -1/4*x+3, 23*x^5]");
        divide_int_helper("[5/3, -1/4*x+3, 23*x^5]", 5, "[1/3, -1/20*x+3/5, 23/5*x^5]");

        divide_int_fail_helper("[]", 0);
        divide_int_fail_helper("[5/3, -1/4*x+3, 23*x^5]", 0);
    }

    private static void shiftLeft_helper(@NotNull String a, int bits, @NotNull String output) {
        RationalPolynomialVector v = readStrict(a).get().shiftLeft(bits);
        v.validate();
        aeq(v, output);
    }

    @Test
    public void testShiftLeft() {
        shiftLeft_helper("[]", 0, "[]");
        shiftLeft_helper("[]", 1, "[]");
        shiftLeft_helper("[]", 2, "[]");
        shiftLeft_helper("[]", 3, "[]");
        shiftLeft_helper("[]", 4, "[]");
        shiftLeft_helper("[]", -1, "[]");
        shiftLeft_helper("[]", -2, "[]");
        shiftLeft_helper("[]", -3, "[]");
        shiftLeft_helper("[]", -4, "[]");

        shiftLeft_helper("[1/2*x]", 0, "[1/2*x]");
        shiftLeft_helper("[1/2*x]", 1, "[x]");
        shiftLeft_helper("[1/2*x]", 2, "[2*x]");
        shiftLeft_helper("[1/2*x]", 3, "[4*x]");
        shiftLeft_helper("[1/2*x]", 4, "[8*x]");
        shiftLeft_helper("[1/2*x]", -1, "[1/4*x]");
        shiftLeft_helper("[1/2*x]", -2, "[1/8*x]");
        shiftLeft_helper("[1/2*x]", -3, "[1/16*x]");
        shiftLeft_helper("[1/2*x]", -4, "[1/32*x]");

        shiftLeft_helper("[5/3, -1/4*x+3, 23*x^5]", 0, "[5/3, -1/4*x+3, 23*x^5]");
        shiftLeft_helper("[5/3, -1/4*x+3, 23*x^5]", 1, "[10/3, -1/2*x+6, 46*x^5]");
        shiftLeft_helper("[5/3, -1/4*x+3, 23*x^5]", 2, "[20/3, -x+12, 92*x^5]");
        shiftLeft_helper("[5/3, -1/4*x+3, 23*x^5]", 3, "[40/3, -2*x+24, 184*x^5]");
        shiftLeft_helper("[5/3, -1/4*x+3, 23*x^5]", 4, "[80/3, -4*x+48, 368*x^5]");
        shiftLeft_helper("[5/3, -1/4*x+3, 23*x^5]", -1, "[5/6, -1/8*x+3/2, 23/2*x^5]");
        shiftLeft_helper("[5/3, -1/4*x+3, 23*x^5]", -2, "[5/12, -1/16*x+3/4, 23/4*x^5]");
        shiftLeft_helper("[5/3, -1/4*x+3, 23*x^5]", -3, "[5/24, -1/32*x+3/8, 23/8*x^5]");
        shiftLeft_helper("[5/3, -1/4*x+3, 23*x^5]", -4, "[5/48, -1/64*x+3/16, 23/16*x^5]");
    }

    private static void shiftRight_helper(@NotNull String a, int bits, @NotNull String output) {
        RationalPolynomialVector v = readStrict(a).get().shiftRight(bits);
        v.validate();
        aeq(v, output);
    }

    @Test
    public void testShiftRight() {
        shiftRight_helper("[]", 0, "[]");
        shiftRight_helper("[]", 1, "[]");
        shiftRight_helper("[]", 2, "[]");
        shiftRight_helper("[]", 3, "[]");
        shiftRight_helper("[]", 4, "[]");
        shiftRight_helper("[]", -1, "[]");
        shiftRight_helper("[]", -2, "[]");
        shiftRight_helper("[]", -3, "[]");
        shiftRight_helper("[]", -4, "[]");

        shiftRight_helper("[1/2*x]", 0, "[1/2*x]");
        shiftRight_helper("[1/2*x]", 1, "[1/4*x]");
        shiftRight_helper("[1/2*x]", 2, "[1/8*x]");
        shiftRight_helper("[1/2*x]", 3, "[1/16*x]");
        shiftRight_helper("[1/2*x]", 4, "[1/32*x]");
        shiftRight_helper("[1/2*x]", -1, "[x]");
        shiftRight_helper("[1/2*x]", -2, "[2*x]");
        shiftRight_helper("[1/2*x]", -3, "[4*x]");
        shiftRight_helper("[1/2*x]", -4, "[8*x]");

        shiftRight_helper("[5/3, -1/4*x+3, 23*x^5]", 0, "[5/3, -1/4*x+3, 23*x^5]");
        shiftRight_helper("[5/3, -1/4*x+3, 23*x^5]", 1, "[5/6, -1/8*x+3/2, 23/2*x^5]");
        shiftRight_helper("[5/3, -1/4*x+3, 23*x^5]", 2, "[5/12, -1/16*x+3/4, 23/4*x^5]");
        shiftRight_helper("[5/3, -1/4*x+3, 23*x^5]", 3, "[5/24, -1/32*x+3/8, 23/8*x^5]");
        shiftRight_helper("[5/3, -1/4*x+3, 23*x^5]", 4, "[5/48, -1/64*x+3/16, 23/16*x^5]");
        shiftRight_helper("[5/3, -1/4*x+3, 23*x^5]", -1, "[10/3, -1/2*x+6, 46*x^5]");
        shiftRight_helper("[5/3, -1/4*x+3, 23*x^5]", -2, "[20/3, -x+12, 92*x^5]");
        shiftRight_helper("[5/3, -1/4*x+3, 23*x^5]", -3, "[40/3, -2*x+24, 184*x^5]");
        shiftRight_helper("[5/3, -1/4*x+3, 23*x^5]", -4, "[80/3, -4*x+48, 368*x^5]");
    }

    private static void sum_helper(@NotNull String input, @NotNull String output) {
        RationalPolynomialVector v = sum(readRationalPolynomialVectorList(input));
        v.validate();
        aeq(v, output);
    }

    private static void sum_fail_helper(@NotNull String input) {
        try {
            sum(readRationalPolynomialVectorListWithNulls(input));
            fail();
        } catch (IllegalArgumentException | ArithmeticException | NullPointerException ignored) {}
    }

    @Test
    public void testSum() {
        sum_helper("[[]]", "[]");
        sum_helper("[[], [], []]", "[]");
        sum_helper("[[5/3, -1/4*x+3, 23*x^5]]", "[5/3, -1/4*x+3, 23*x^5]");
        sum_helper("[[5/3, -1/4*x+3, 23*x^5], [0, 1/2*x^10, x-1/3], [x^2-1, 2, 4/3]]",
                "[x^2+2/3, 1/2*x^10-1/4*x+5, 23*x^5+x+1]");
        sum_helper("[[x], [1/2*x], [x-1/4]]", "[5/2*x-1/4]");

        sum_fail_helper("[]");
        sum_fail_helper("[[x], [1/2*x], null]");
        sum_fail_helper("[[1/2*x], [3, 4]]");
    }

    private static void delta_helper(@NotNull Iterable<RationalPolynomialVector> input, @NotNull String output) {
        Iterable<RationalPolynomialVector> vs = delta(input);
        take(TINY_LIMIT, vs).forEach(RationalPolynomialVector::validate);
        aeqitLimit(TINY_LIMIT, vs, output);
    }

    private static void delta_helper(@NotNull String input, @NotNull String output) {
        delta_helper(readRationalPolynomialVectorList(input), output);
    }

    private static void delta_fail_helper(@NotNull String input) {
        try {
            toList(delta(readRationalPolynomialVectorListWithNulls(input)));
            fail();
        } catch (IllegalArgumentException | ArithmeticException | NullPointerException ignored) {}
    }

    @Test
    public void testDelta() {
        delta_helper("[[]]", "[]");
        delta_helper("[[], [], []]", "[[], []]");
        delta_helper("[[5/3, -1/4*x+3, 23*x^5]]", "[]");
        delta_helper("[[5/3, -1/4*x+3, 23*x^5], [0, 1/2*x^10, x-1/3], [x^2-1, 2, 4/3]]",
                "[[-5/3, 1/2*x^10+1/4*x-3, -23*x^5+x-1/3], [x^2-1, -1/2*x^10+2, -x+5/3]]");
        delta_helper("[[x], [1/2*x], [x-1/4]]", "[[-1/2*x], [1/2*x-1/4]]");
        delta_helper(
                map(
                        i -> {
                            RationalPolynomial p = RationalPolynomial.of(
                                    Arrays.asList(Rational.of(1, -i), Rational.ONE)
                            );
                            return of(Arrays.asList(p, p.pow(2), p.pow(3)));
                        },
                        ExhaustiveProvider.INSTANCE.positiveIntegers()
                ),
                "[[1/2, x-3/4, 3/2*x^2-9/4*x+7/8], [1/6, 1/3*x-5/36, 1/2*x^2-5/12*x+19/216]," +
                " [1/12, 1/6*x-7/144, 1/4*x^2-7/48*x+37/1728], [1/20, 1/10*x-9/400, 3/20*x^2-27/400*x+61/8000]," +
                " [1/30, 1/15*x-11/900, 1/10*x^2-11/300*x+91/27000]," +
                " [1/42, 1/21*x-13/1764, 1/14*x^2-13/588*x+127/74088]," +
                " [1/56, 1/28*x-15/3136, 3/56*x^2-45/3136*x+169/175616]," +
                " [1/72, 1/36*x-17/5184, 1/24*x^2-17/1728*x+217/373248]," +
                " [1/90, 1/45*x-19/8100, 1/30*x^2-19/2700*x+271/729000]," +
                " [1/110, 1/55*x-21/12100, 3/110*x^2-63/12100*x+331/1331000]," +
                " [1/132, 1/66*x-23/17424, 1/44*x^2-23/5808*x+397/2299968]," +
                " [1/156, 1/78*x-25/24336, 1/52*x^2-25/8112*x+469/3796416]," +
                " [1/182, 1/91*x-27/33124, 3/182*x^2-81/33124*x+547/6028568]," +
                " [1/210, 1/105*x-29/44100, 1/70*x^2-29/14700*x+631/9261000]," +
                " [1/240, 1/120*x-31/57600, 1/80*x^2-31/19200*x+721/13824000]," +
                " [1/272, 1/136*x-33/73984, 3/272*x^2-99/73984*x+817/20123648]," +
                " [1/306, 1/153*x-35/93636, 1/102*x^2-35/31212*x+919/28652616]," +
                " [1/342, 1/171*x-37/116964, 1/114*x^2-37/38988*x+1027/40001688]," +
                " [1/380, 1/190*x-39/144400, 3/380*x^2-117/144400*x+1141/54872000]," +
                " [1/420, 1/210*x-41/176400, 1/140*x^2-41/58800*x+1261/74088000], ...]"
        );

        delta_fail_helper("[]");
        delta_fail_helper("[[x], [1/2*x], null]");
        delta_fail_helper("[[1/2*x], [3, 4]]");
    }

    private static void dot_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().dot(readStrict(b).get()), output);
    }

    private static void dot_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().dot(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDot() {
        dot_helper("[]", "[]", "0");
        dot_helper("[1/2*x]", "[3]", "3/2*x");
        dot_helper("[5/3, -1/4*x+3, 23*x^5]", "[0, 1/2*x^10, x-1/3]", "-1/8*x^11+3/2*x^10+23*x^6-23/3*x^5");
        dot_helper("[5/3, -1/4*x+3, 23*x^5]", "[0, 0, 0]", "0");
        dot_helper("[5/3, -1/4*x+3, 23*x^5]", "[-5/3, 1/4*x-3, -23*x^5]", "-529*x^10-1/16*x^2+3/2*x-106/9");

        dot_fail_helper("[]", "[1/2*x]");
        dot_fail_helper("[1/2*x]", "[]");
        dot_fail_helper("[0, 1/2*x^10, x-1/3]", "[5/6, 2/3]");
    }

    private static void squaredLength_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().squaredLength(), output);
    }

    @Test
    public void testSquaredLength() {
        squaredLength_helper("[]", "0");
        squaredLength_helper("[1/2*x]", "1/4*x^2");
        squaredLength_helper("[1, 0]", "1");
        squaredLength_helper("[1, 1]", "2");
        squaredLength_helper("[5/3, -1/4*x+3, 23*x^5]", "529*x^10+1/16*x^2-3/2*x+106/9");
        squaredLength_helper("[0, 1/2*x^10, x-1/3]", "1/4*x^20+x^2-2/3*x+1/9");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readRationalPolynomialVectorList("[[], [1/2*x], [5/3, -1/4*x+3, 23*x^5], [5/3, 1/4*x+3, 23*x^5]]"),
                readRationalPolynomialVectorList("[[], [1/2*x], [5/3, -1/4*x+3, 23*x^5], [5/3, 1/4*x+3, 23*x^5]]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(readStrict(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("[]", 1);
        hashCode_helper("[1/2*x]", 1056);
        hashCode_helper("[5/3, -1/4*x+3, 23*x^5]", 917418558);
        hashCode_helper("[5/3, 1/4*x+3, 23*x^5]", 917420480);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(
                readRationalPolynomialVectorList("[[], [1/2*x], [5/3, -1/4*x+3, 23*x^5], [5/3, 1/4*x+3, 23*x^5]]")
        );
    }

    private static void readStrict_helper(@NotNull String input, @NotNull String output) {
        Optional<RationalPolynomialVector> ov = readStrict(input);
        ov.ifPresent(RationalPolynomialVector::validate);
        aeq(ov, output);
    }

    @Test
    public void testReadStrict() {
        readStrict_helper("[]", "Optional[[]]");
        readStrict_helper("[x]", "Optional[[x]]");
        readStrict_helper("[5/3, -1/4*x+3, 23*x^5]", "Optional[[5/3, -1/4*x+3, 23*x^5]]");

        readStrict_helper("", "Optional.empty");
        readStrict_helper("[ 1]", "Optional.empty");
        readStrict_helper("[1/3*x, 2/4]", "Optional.empty");
        readStrict_helper("hello", "Optional.empty");
        readStrict_helper("][", "Optional.empty");
        readStrict_helper("1, 3", "Optional.empty");
        readStrict_helper("vfbdb ds", "Optional.empty");
    }

    private static @NotNull List<RationalPolynomial> readRationalPolynomialList(@NotNull String s) {
        return Readers.readListStrict(RationalPolynomial::readStrict).apply(s).get();
    }

    private static @NotNull List<RationalPolynomial> readRationalPolynomialListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(RationalPolynomial::readStrict).apply(s).get();
    }

    private static @NotNull List<RationalPolynomialVector> readRationalPolynomialVectorList(@NotNull String s) {
        return Readers.readListStrict(RationalPolynomialVector::readStrict).apply(s).get();
    }

    private static @NotNull List<RationalPolynomialVector> readRationalPolynomialVectorListWithNulls(
            @NotNull String s
    ) {
        return Readers.readListWithNullsStrict(RationalPolynomialVector::readStrict).apply(s).get();
    }
}
