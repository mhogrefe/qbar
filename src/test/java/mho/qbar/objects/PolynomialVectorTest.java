package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.PolynomialVector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.aeq;
import static mho.wheels.testing.Testing.testCompareToHelper;
import static mho.wheels.testing.Testing.testEqualsHelper;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class PolynomialVectorTest {
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
        iterator_helper("[x]");
        iterator_helper("[5, -4*x+3, 23*x^5]");
    }

    private static void toRationalPolynomialVector_helper(@NotNull String input) {
        aeq(read(input).get().toRationalPolynomialVector(), input);
    }

    @Test
    public void testToRationalPolynomialVector() {
        toRationalPolynomialVector_helper("[]");
        toRationalPolynomialVector_helper("[x]");
        toRationalPolynomialVector_helper("[5, -4*x+3, 23*x^5]");
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
        get_helper("[x]", 0, "x");
        get_helper("[5, -4*x+3, 23*x^5]", 0, "5");
        get_helper("[5, -4*x+3, 23*x^5]", 1, "-4*x+3");
        get_helper("[5, -4*x+3, 23*x^5]", 2, "23*x^5");
        get_fail_helper("[5, -4*x+3, 23*x^5]", 4);
        get_fail_helper("[x]", 3);
        get_fail_helper("[]", 0);
    }

    private static void of_List_Polynomial_helper(@NotNull String input) {
        aeq(of(readPolynomialList(input)), input);
    }

    private static void of_List_Polynomial_fail_helper(@NotNull String input) {
        try {
            of(readPolynomialListWithNulls(input));
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testOf_List_Polynomial() {
        of_List_Polynomial_helper("[]");
        of_List_Polynomial_helper("[x]");
        of_List_Polynomial_helper("[5, -4*x+3, 23*x^5]");
        of_List_Polynomial_fail_helper("[5, null, 23*x^5]");
    }

    private static void of_Polynomial_helper(@NotNull String input, @NotNull String output) {
        aeq(of(Polynomial.read(input).get()), output);
    }

    @Test
    public void testOf_Polynomial() {
        of_Polynomial_helper("2", "[2]");
        of_Polynomial_helper("x", "[x]");
        of_Polynomial_helper("0", "[0]");
    }

    private static void maxCoordinateBitLength_helper(@NotNull String input, int output) {
        aeq(read(input).get().maxCoordinateBitLength(), output);
    }

    @Test
    public void testMaxCoordinateBitLength() {
        maxCoordinateBitLength_helper("[]", 0);
        maxCoordinateBitLength_helper("[x]", 1);
        maxCoordinateBitLength_helper("[-x]", 1);
        maxCoordinateBitLength_helper("[5, -4*x+3, 23*x^5]", 5);
    }

    private static void dimension_helper(@NotNull String input, int output) {
        aeq(read(input).get().dimension(), output);
    }

    @Test
    public void testDimension() {
        dimension_helper("[]", 0);
        dimension_helper("[2]", 1);
        dimension_helper("[5, -4, 23]", 3);
    }

    private static void testIsZero(@NotNull String input, boolean output) {
        aeq(read(input).get().isZero(), output);
    }

    @Test
    public void testIsZero() {
        testIsZero("[]", true);
        testIsZero("[0]", true);
        testIsZero("[0, 0, 0]", true);
        testIsZero("[x]", false);
        testIsZero("[0, 0, -4*x+3]", false);
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
        add_helper("[x]", "[x^2]", "[x^2+x]");
        add_helper("[5, -4*x+3, 23*x^5]", "[-2, 1, 3]", "[3, -4*x+4, 23*x^5+3]");
        add_helper("[5, -4*x+3, 23*x^5]", "[0, 0, 0]", "[5, -4*x+3, 23*x^5]");
        add_helper("[5, -4*x+3, 23*x^5]", "[-5, 4*x-3, -23*x^5]", "[0, 0, 0]");
        add_fail_helper("[]", "[x]");
        add_fail_helper("[x]", "[]");
        add_fail_helper("[5, -4*x+3, 23*x^5]", "[6, 3]");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().negate(), output);
    }

    @Test
    public void testNegate() {
        negate_helper("[]", "[]");
        negate_helper("[x]", "[-x]");
        negate_helper("[5, -4*x+3, 23*x^5]", "[-5, 4*x-3, -23*x^5]");
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
        subtract_helper("[x]", "[x^2]", "[-x^2+x]");
        subtract_helper("[5, -4*x+3, 23*x^5]", "[-2, 1, 3]", "[7, -4*x+2, 23*x^5-3]");
        subtract_helper("[5, -4*x+3, 23*x^5]", "[0, 0, 0]", "[5, -4*x+3, 23*x^5]");
        subtract_helper("[5, -4*x+3, 23*x^5]", "[5, -4*x+3, 23*x^5]", "[0, 0, 0]");
        subtract_helper("[0, 0, 0]", "[5, -4*x+3, 23*x^5]", "[-5, 4*x-3, -23*x^5]");
        subtract_fail_helper("[]", "[x]");
        subtract_fail_helper("[x]", "[]");
        subtract_fail_helper("[5, -4*x+3, 23*x^5]", "[6, 3]");
    }

    private static void multiply_Polynomial_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().multiply(Polynomial.read(b).get()), output);
    }

    @Test
    public void testMultiply_Polynomial() {
        multiply_Polynomial_helper("[]", "0", "[]");
        multiply_Polynomial_helper("[]", "1", "[]");
        multiply_Polynomial_helper("[]", "x", "[]");
        multiply_Polynomial_helper("[]", "23*x^5", "[]");

        multiply_Polynomial_helper("[x]", "0", "[0]");
        multiply_Polynomial_helper("[x]", "1", "[x]");
        multiply_Polynomial_helper("[x]", "x", "[x^2]");
        multiply_Polynomial_helper("[x]", "23*x^5", "[23*x^6]");

        multiply_Polynomial_helper("[5, -4*x+3, 23*x^5]", "0", "[0, 0, 0]");
        multiply_Polynomial_helper("[5, -4*x+3, 23*x^5]", "1", "[5, -4*x+3, 23*x^5]");
        multiply_Polynomial_helper("[5, -4*x+3, 23*x^5]", "x", "[5*x, -4*x^2+3*x, 23*x^6]");
        multiply_Polynomial_helper("[5, -4*x+3, 23*x^5]", "23*x^5", "[115*x^5, -92*x^6+69*x^5, 529*x^10]");
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

        multiply_BigInteger_helper("[5, -4*x+3, 23*x^5]", "0", "[0, 0, 0]");
        multiply_BigInteger_helper("[5, -4*x+3, 23*x^5]", "1", "[5, -4*x+3, 23*x^5]");
        multiply_BigInteger_helper("[5, -4*x+3, 23*x^5]", "5", "[25, -20*x+15, 115*x^5]");
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

        multiply_int_helper("[5, -4*x+3, 23*x^5]", 0, "[0, 0, 0]");
        multiply_int_helper("[5, -4*x+3, 23*x^5]", 1, "[5, -4*x+3, 23*x^5]");
        multiply_int_helper("[5, -4*x+3, 23*x^5]", 5, "[25, -20*x+15, 115*x^5]");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readPolynomialVectorList("[[], [x], [5, -4*x+3, 23*x^5], [5, 4*x+3, 23*x^5]]"),
                readPolynomialVectorList("[[], [x], [5, -4*x+3, 23*x^5], [5, 4*x+3, 23*x^5]]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("[]", 1);
        hashCode_helper("[x]", 993);
        hashCode_helper("[5, -4*x+3, 23*x^5]", 887600641);
        hashCode_helper("[5, 4*x+3, 23*x^5]", 887600889);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(readPolynomialVectorList("[[], [x], [5, -4*x+3, 23*x^5], [5, 4*x+3, 23*x^5]]"));
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
        read_helper("[5, 4*x+3, 23*x^5]");
        read_fail_helper("");
        read_fail_helper("[ 1]");
        read_fail_helper("[1/3*x, 1/2]");
        read_fail_helper("[4, -5/3*x^2-1]");
        read_fail_helper("hello");
        read_fail_helper("][");
        read_fail_helper("1, 3");
        read_fail_helper("vfbdb ds");
    }

    private static void findIn_helper(@NotNull String input, @NotNull String output, int index) {
        Pair<PolynomialVector, Integer> result = findIn(input).get();
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
        findIn_helper("]]][[3/4, 45][]dsvdf", "[]", 13);
        findIn_fail_helper("");
        findIn_fail_helper("]]][[x,  45]dsvdf");
        findIn_fail_helper("]]][[3/4*x, 45]dsvdf");
        findIn_fail_helper("hello");
    }

    private static @NotNull List<Polynomial> readPolynomialList(@NotNull String s) {
        return Readers.readList(Polynomial::read).apply(s).get();
    }

    private static @NotNull List<Polynomial> readPolynomialListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Polynomial::read).apply(s).get();
    }

    private static @NotNull List<PolynomialVector> readPolynomialVectorList(@NotNull String s) {
        return Readers.readList(PolynomialVector::read).apply(s).get();
    }

    private static @NotNull List<PolynomialVector> readPolynomialVectorListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(PolynomialVector::read).apply(s).get();
    }
}