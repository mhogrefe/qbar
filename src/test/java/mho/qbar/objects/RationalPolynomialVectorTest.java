package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.RationalPolynomialVector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.aeq;
import static mho.wheels.testing.Testing.testCompareToHelper;
import static mho.wheels.testing.Testing.testEqualsHelper;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class RationalPolynomialVectorTest {
    @Test
    public void testConstants() {
        aeq(ZERO_DIMENSIONAL, "[]");
    }

    private static void iterator_helper(@NotNull String input) {
        aeq(toList(read(input).get()), input);
    }

    @Test
    public void testIterator() {
        iterator_helper("[]");
        iterator_helper("[1/2*x]");
        iterator_helper("[5/3, -1/4*x+3, 23*x^5]");
    }

    private static void hasIntegralCoordinates_helper(@NotNull String input, boolean output) {
        aeq(read(input).get().hasIntegralCoordinates(), output);
    }

    @Test
    public void testHasIntegralCoordinates() {
        hasIntegralCoordinates_helper("[]", true);
        hasIntegralCoordinates_helper("[5*x]", true);
        hasIntegralCoordinates_helper("[12, -4, 0, x+1]", true);
        hasIntegralCoordinates_helper("[1/2*x]", false);
        hasIntegralCoordinates_helper("[5/3, -1/4*x+3, 23*x^5]", false);
    }

    private static void toPolynomialVector_helper(@NotNull String input) {
        aeq(read(input).get().toPolynomialVector(), input);
    }

    private static void toPolynomialVector_fail_helper(@NotNull String input) {
        try {
            read(input).get().toPolynomialVector();
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
        aeq(read(input).get().get(i), output);
    }

    private static void get_fail_helper(@NotNull String input, int i) {
        try {
            read(input).get().get(i);
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
        aeq(of(readRationalPolynomialList(input)), input);
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
        aeq(of(RationalPolynomial.read(input).get()), output);
    }

    @Test
    public void testOf_Rational() {
        of_RationalPolynomial_helper("1/2*x", "[1/2*x]");
        of_RationalPolynomial_helper("-5", "[-5]");
        of_RationalPolynomial_helper("0", "[0]");
    }

    private static void maxCoordinateBitLength_helper(@NotNull String input, int output) {
        aeq(read(input).get().maxCoordinateBitLength(), output);
    }

    @Test
    public void testMaxCoordinateBitLength() {
        maxCoordinateBitLength_helper("[]", 0);
        maxCoordinateBitLength_helper("[1/2*x]", 3);
        maxCoordinateBitLength_helper("[5/3, -1/4*x+3, 23*x^5]", 6);
    }

    private static void dimension_helper(@NotNull String input, int output) {
        aeq(read(input).get().dimension(), output);
    }

    @Test
    public void testDimension() {
        dimension_helper("[]", 0);
        dimension_helper("[1/2*x]", 1);
        dimension_helper("[5/3, -1/4*x+3, 23*x^5]", 3);
    }

    private static void testIsZero(@NotNull String input, boolean output) {
        aeq(read(input).get().isZero(), output);
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
        aeq(zero(dimension), output);
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
        aeq(standard(dimension, i), output);
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
        aeq(read(a).get().add(read(b).get()), output);
    }

    private static void add_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().add(read(b).get());
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
        aeq(read(input).get().negate(), output);
    }

    @Test
    public void testNegate() {
        negate_helper("[]", "[]");
        negate_helper("[1/2*x]", "[-1/2*x]");
        negate_helper("[5/3, -1/4, 23]", "[-5/3, 1/4, -23]");
    }

    private static void subtract_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().subtract(read(b).get()), output);
    }

    private static void subtract_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().subtract(read(b).get());
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
        aeq(read(a).get().multiply(RationalPolynomial.read(b).get()), output);
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
        aeq(read(a).get().multiply(Rational.read(b).get()), output);
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
        aeq(read(a).get().multiply(Readers.readBigInteger(b).get()), output);
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
        aeq(read(a).get().multiply(b), output);
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
        aeq(read(a).get().divide(Rational.read(b).get()), output);
    }

    private static void divide_Rational_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().divide(Rational.read(b).get());
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
        aeq(read(a).get().divide(Readers.readBigInteger(b).get()), output);
    }

    private static void divide_BigInteger_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().divide(Readers.readBigInteger(b).get());
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
        aeq(read(a).get().divide(b), output);
    }

    private static void divide_int_fail_helper(@NotNull String a, int b) {
        try {
            read(a).get().divide(b);
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

    @Test
    public void testEquals() {
        testEqualsHelper(
                readRationalPolynomialVectorList("[[], [1/2*x], [5/3, -1/4*x+3, 23*x^5], [5/3, 1/4*x+3, 23*x^5]]"),
                readRationalPolynomialVectorList("[[], [1/2*x], [5/3, -1/4*x+3, 23*x^5], [5/3, 1/4*x+3, 23*x^5]]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
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

    private static void read_helper(@NotNull String input) {
        aeq(read(input).get(), input);
    }

    private static void read_fail_helper(@NotNull String input) {
        assertFalse(read(input).isPresent());
    }

    @Test
    public void testRead() {
        read_helper("[]");
        read_helper("[x]");
        read_helper("[5/3, -1/4*x+3, 23*x^5]");
        read_fail_helper("");
        read_fail_helper("[ 1]");
        read_fail_helper("[1/3*x, 2/4]");
        read_fail_helper("hello");
        read_fail_helper("][");
        read_fail_helper("1, 3");
        read_fail_helper("vfbdb ds");
    }

    private static void findIn_helper(@NotNull String input, @NotNull String output, int index) {
        Pair<RationalPolynomialVector, Integer> result = findIn(input).get();
        aeq(result.a, output);
        aeq(result.b, index);
    }

    private static void findIn_fail_helper(@NotNull String input) {
        assertFalse(findIn(input).isPresent());
    }

    @Test
    public void testFindIn() {
        findIn_helper("fr24rev[]evfre", "[]", 7);
        findIn_helper("]]][[x, 45][]dsvdf", "[x, 45]", 4);
        findIn_helper("]]][[4,  45][]dsvdf", "[]", 12);
        findIn_helper("]]][[3/4, 45][]dsvdf", "[3/4, 45]", 4);
        findIn_fail_helper("");
        findIn_fail_helper("]]][[x,  45]dsvdf");
        findIn_fail_helper("]]][[3/4*x,  45]dsvdf");
        findIn_fail_helper("hello");
    }

    private static @NotNull List<RationalPolynomial> readRationalPolynomialList(@NotNull String s) {
        return Readers.readList(RationalPolynomial::read).apply(s).get();
    }

    private static @NotNull List<RationalPolynomial> readRationalPolynomialListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(RationalPolynomial::read).apply(s).get();
    }

    private static @NotNull List<RationalPolynomialVector> readRationalPolynomialVectorList(@NotNull String s) {
        return Readers.readList(RationalPolynomialVector::read).apply(s).get();
    }

    private static @NotNull List<RationalPolynomialVector> readRationalPolynomialVectorListWithNulls(
            @NotNull String s
    ) {
        return Readers.readListWithNulls(RationalPolynomialVector::read).apply(s).get();
    }
}
