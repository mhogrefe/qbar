package mho.qbar.objects;

import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static mho.qbar.objects.RationalVector.*;
import static mho.qbar.objects.RationalVector.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RationalVectorTest {
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
        iterator_helper("[1/2]");
        iterator_helper("[5/3, -1/4, 23]");
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
        get_helper("[1/2]", 0, "1/2");
        get_helper("[5/3, -1/4, 23]", 0, "5/3");
        get_helper("[5/3, -1/4, 23]", 1, "-1/4");
        get_helper("[5/3, -1/4, 23]", 2, "23");
        get_fail_helper("[5/3, -1/4, 23]", 4);
        get_fail_helper("[1/2]", 3);
        get_fail_helper("[]", 0);
    }

    private static void of_List_Rational_helper(@NotNull String input) {
        aeq(of(readRationalList(input)), input);
    }

    private static void of_List_Rational_fail_helper(@NotNull String input) {
        try {
            of(readRationalListWithNulls(input));
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testOf_List_Rational() {
        of_List_Rational_helper("[]");
        of_List_Rational_helper("[1/2]");
        of_List_Rational_helper("[5/3, -1/4, 23]");
        of_List_Rational_fail_helper("[5/3, null, 23]");
    }

    private static void of_Rational_helper(@NotNull String input, @NotNull String output) {
        aeq(of(Rational.read(input).get()), output);
    }

    @Test
    public void testOf_Rational() {
        of_Rational_helper("1/2", "[1/2]");
        of_Rational_helper("-5", "[-5]");
        of_Rational_helper("0", "[0]");
    }

    private static void dimension_helper(@NotNull String input, int output) {
        aeq(read(input).get().dimension(), output);
    }

    @Test
    public void testDimension() {
        dimension_helper("[]", 0);
        dimension_helper("[1/2]", 1);
        dimension_helper("[5/3, -1/4, 23]", 3);
    }

    private static void testIsZero(@NotNull String input, boolean output) {
        aeq(read(input).get().isZero(), output);
    }

    @Test
    public void testIsZero() {
        testIsZero("[]", true);
        testIsZero("[0]", true);
        testIsZero("[0, 0, 0]", true);
        testIsZero("[5]", false);
        testIsZero("[0, 0, 3]", false);
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
        add_helper("[2]", "[3]", "[5]");
        add_helper("[5/3, 4, 0]", "[-2, 1, 3]", "[-1/3, 5, 3]");
        add_helper("[5/3, 4, 0]", "[0, 0, 0]", "[5/3, 4, 0]");
        add_helper("[5/3, 4, 0]", "[-5/3, -4, 0]", "[0, 0, 0]");
        add_fail_helper("[]", "[1/2]");
        add_fail_helper("[1/2]", "[]");
        add_fail_helper("[1/2, 4, -4]", "[5/6, 2/3]");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().negate(), output);
    }

    @Test
    public void testNegate() {
        negate_helper("[]", "[]");
        negate_helper("[1/2]", "[-1/2]");
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
        subtract_helper("[2]", "[3]", "[-1]");
        subtract_helper("[5/3, 4, 0]", "[-2, 1, 3]", "[11/3, 3, -3]");
        subtract_helper("[5/3, 4, 0]", "[0, 0, 0]", "[5/3, 4, 0]");
        subtract_helper("[5/3, 4, 0]", "[5/3, 4, 0]", "[0, 0, 0]");
        subtract_helper("[0, 0, 0]", "[5/3, 4, 0]", "[-5/3, -4, 0]");
        subtract_fail_helper("[]", "[1/2]");
        subtract_fail_helper("[1/2]", "[]");
        subtract_fail_helper("[1/2, 4, -4]", "[5/6, 2/3]");
    }

    private static void multiply_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().multiply(Rational.read(b).get()), output);
    }

    @Test
    public void testMultiply_Rational() {
        multiply_Rational_helper("[]", "0", "[]");
        multiply_Rational_helper("[]", "1", "[]");
        multiply_Rational_helper("[]", "-3/2", "[]");
        multiply_Rational_helper("[2]", "0", "[0]");
        multiply_Rational_helper("[2]", "1", "[2]");
        multiply_Rational_helper("[2]", "-3/2", "[-3]");
        multiply_Rational_helper("[5/3, 4, 0]", "0", "[0, 0, 0]");
        multiply_Rational_helper("[5/3, 4, 0]", "1", "[5/3, 4, 0]");
        multiply_Rational_helper("[5/3, 4, 0]", "-3/2", "[-5/2, -6, 0]");
    }

    private static void multiply_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().multiply(Readers.readBigInteger(b).get()), output);
    }

    @Test
    public void testMultiply_BigInteger() {
        multiply_BigInteger_helper("[]", "0", "[]");
        multiply_BigInteger_helper("[]", "1", "[]");
        multiply_BigInteger_helper("[]", "5", "[]");
        multiply_BigInteger_helper("[2]", "0", "[0]");
        multiply_BigInteger_helper("[2]", "1", "[2]");
        multiply_BigInteger_helper("[2]", "5", "[10]");
        multiply_BigInteger_helper("[5/3, 4, 0]", "0", "[0, 0, 0]");
        multiply_BigInteger_helper("[5/3, 4, 0]", "1", "[5/3, 4, 0]");
        multiply_BigInteger_helper("[5/3, 4, 0]", "5", "[25/3, 20, 0]");
    }

    private static void multiply_int_helper(@NotNull String a, int b, @NotNull String output) {
        aeq(read(a).get().multiply(b), output);
    }

    @Test
    public void testMultiply_int() {
        multiply_int_helper("[]", 0, "[]");
        multiply_int_helper("[]", 1, "[]");
        multiply_int_helper("[]", 5, "[]");
        multiply_int_helper("[2]", 0, "[0]");
        multiply_int_helper("[2]", 1, "[2]");
        multiply_int_helper("[2]", 5, "[10]");
        multiply_int_helper("[5/3, 4, 0]", 0, "[0, 0, 0]");
        multiply_int_helper("[5/3, 4, 0]", 1, "[5/3, 4, 0]");
        multiply_int_helper("[5/3, 4, 0]", 5, "[25/3, 20, 0]");
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
        divide_Rational_helper("[2]", "1", "[2]");
        divide_Rational_helper("[2]", "-3/2", "[-4/3]");
        divide_Rational_helper("[5/3, 4, 0]", "1", "[5/3, 4, 0]");
        divide_Rational_helper("[5/3, 4, 0]", "-3/2", "[-10/9, -8/3, 0]");
        divide_Rational_fail_helper("[]", "0");
        divide_Rational_fail_helper("[5/3, 4, 0]", "0");
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
        divide_BigInteger_helper("[2]", "1", "[2]");
        divide_BigInteger_helper("[2]", "5", "[2/5]");
        divide_BigInteger_helper("[5/3, 4, 0]", "1", "[5/3, 4, 0]");
        divide_BigInteger_helper("[5/3, 4, 0]", "5", "[1/3, 4/5, 0]");
        divide_BigInteger_fail_helper("[]", "0");
        divide_BigInteger_fail_helper("[5/3, 4, 0]", "0");
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
        divide_int_helper("[2]", 1, "[2]");
        divide_int_helper("[2]", 5, "[2/5]");
        divide_int_helper("[5/3, 4, 0]", 1, "[5/3, 4, 0]");
        divide_int_helper("[5/3, 4, 0]", 5, "[1/3, 4/5, 0]");
        divide_int_fail_helper("[]", 0);
        divide_int_fail_helper("[5/3, 4, 0]", 0);
    }

    private static void shiftLeft_helper(@NotNull String a, int bits, @NotNull String output) {
        aeq(read(a).get().shiftLeft(bits), output);
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
        shiftLeft_helper("[1/2]", 0, "[1/2]");
        shiftLeft_helper("[1/2]", 1, "[1]");
        shiftLeft_helper("[1/2]", 2, "[2]");
        shiftLeft_helper("[1/2]", 3, "[4]");
        shiftLeft_helper("[1/2]", 4, "[8]");
        shiftLeft_helper("[1/2]", -1, "[1/4]");
        shiftLeft_helper("[1/2]", -2, "[1/8]");
        shiftLeft_helper("[1/2]", -3, "[1/16]");
        shiftLeft_helper("[1/2]", -4, "[1/32]");
        shiftLeft_helper("[5/3, -1/4, 23]", 0, "[5/3, -1/4, 23]");
        shiftLeft_helper("[5/3, -1/4, 23]", 1, "[10/3, -1/2, 46]");
        shiftLeft_helper("[5/3, -1/4, 23]", 2, "[20/3, -1, 92]");
        shiftLeft_helper("[5/3, -1/4, 23]", 3, "[40/3, -2, 184]");
        shiftLeft_helper("[5/3, -1/4, 23]", 4, "[80/3, -4, 368]");
        shiftLeft_helper("[5/3, -1/4, 23]", -1, "[5/6, -1/8, 23/2]");
        shiftLeft_helper("[5/3, -1/4, 23]", -2, "[5/12, -1/16, 23/4]");
        shiftLeft_helper("[5/3, -1/4, 23]", -3, "[5/24, -1/32, 23/8]");
        shiftLeft_helper("[5/3, -1/4, 23]", -4, "[5/48, -1/64, 23/16]");
    }

    private static void shiftRight_helper(@NotNull String a, int bits, @NotNull String output) {
        aeq(read(a).get().shiftRight(bits), output);
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
        shiftRight_helper("[1/2]", 0, "[1/2]");
        shiftRight_helper("[1/2]", 1, "[1/4]");
        shiftRight_helper("[1/2]", 2, "[1/8]");
        shiftRight_helper("[1/2]", 3, "[1/16]");
        shiftRight_helper("[1/2]", 4, "[1/32]");
        shiftRight_helper("[1/2]", -1, "[1]");
        shiftRight_helper("[1/2]", -2, "[2]");
        shiftRight_helper("[1/2]", -3, "[4]");
        shiftRight_helper("[1/2]", -4, "[8]");
        shiftRight_helper("[5/3, -1/4, 23]", 0, "[5/3, -1/4, 23]");
        shiftRight_helper("[5/3, -1/4, 23]", 1, "[5/6, -1/8, 23/2]");
        shiftRight_helper("[5/3, -1/4, 23]", 2, "[5/12, -1/16, 23/4]");
        shiftRight_helper("[5/3, -1/4, 23]", 3, "[5/24, -1/32, 23/8]");
        shiftRight_helper("[5/3, -1/4, 23]", 4, "[5/48, -1/64, 23/16]");
        shiftRight_helper("[5/3, -1/4, 23]", -1, "[10/3, -1/2, 46]");
        shiftRight_helper("[5/3, -1/4, 23]", -2, "[20/3, -1, 92]");
        shiftRight_helper("[5/3, -1/4, 23]", -3, "[40/3, -2, 184]");
        shiftRight_helper("[5/3, -1/4, 23]", -4, "[80/3, -4, 368]");
    }

    private static void sum_helper(@NotNull String input, @NotNull String output) {
        aeq(sum(readRationalVectorList(input)), output);
    }

    private static void sum_fail_helper(@NotNull String input) {
        try {
            sum(readRationalVectorListWithNulls(input));
            fail();
        } catch (IllegalArgumentException | ArithmeticException | NullPointerException ignored) {}
    }

    @Test
    public void testSum() {
        sum_helper("[[]]", "[]");
        sum_helper("[[], [], []]", "[]");
        sum_helper("[[5/3, 1/4, 23]]", "[5/3, 1/4, 23]");
        sum_helper("[[5/3, 1/4, 23], [0, 2/3, -1/8], [32, -45/2, 9]]", "[101/3, -259/12, 255/8]");
        sum_helper("[[1/2], [2/3], [3/4]]", "[23/12]");
        sum_fail_helper("[]");
        sum_fail_helper("[[1/2, 3], null]");
        sum_fail_helper("[[1/2], [3, 4]]");
    }

    private static void delta_helper(@NotNull Iterable<RationalVector> input, @NotNull String output) {
        aeqitLimit(TINY_LIMIT, delta(input), output);
    }

    private static void delta_helper(@NotNull String input, @NotNull String output) {
        delta_helper(readRationalVectorList(input), output);
    }

    private static void delta_fail_helper(@NotNull String input) {
        try {
            toList(delta(readRationalVectorListWithNulls(input)));
            fail();
        } catch (IllegalArgumentException | ArithmeticException | NullPointerException ignored) {}
    }

    @Test
    public void testDelta() {
        delta_helper("[[]]", "[]");
        delta_helper("[[], [], []]", "[[], []]");
        delta_helper("[[5/3, 1/4, 23]]", "[]");
        delta_helper("[[5/3, 1/4, 23], [0, 2/3, -1/8], [32, -45/2, 9]]", "[[-5/3, 5/12, -185/8], [32, -139/6, 73/8]]");
        delta_helper("[[1/2], [2/3], [3/4]]", "[[1/6], [1/12]]");
        delta_helper(
                map(
                        i -> {
                            Rational r = Rational.of(i);
                            return of(Arrays.asList(r, r.pow(2), r.pow(3)));
                        },
                        rangeUp(1)
                ),
                "[[1, 3, 7], [1, 5, 19], [1, 7, 37], [1, 9, 61], [1, 11, 91], [1, 13, 127], [1, 15, 169]," +
                " [1, 17, 217], [1, 19, 271], [1, 21, 331], [1, 23, 397], [1, 25, 469], [1, 27, 547], [1, 29, 631]," +
                " [1, 31, 721], [1, 33, 817], [1, 35, 919], [1, 37, 1027], [1, 39, 1141], [1, 41, 1261], ...]"
        );
        delta_fail_helper("[]");
        delta_fail_helper("[[1/2, 3], null]");
        delta_fail_helper("[[1/2], [3, 4]]");
    }

    @Test
    public void testDot() {
        assertTrue(ZERO_DIMENSIONAL.dot(ZERO_DIMENSIONAL) == Rational.ZERO);
        aeq(read("[2]").get().dot(read("[3]").get()), "6");
        aeq(read("[5/3, 4, 0]").get().dot(read("[-2, 1, 3]").get()), "2/3");
        aeq(read("[5/3, 4, 0]").get().dot(read("[0, 0, 0]").get()), "0");
        aeq(read("[5/3, 4, 0]").get().dot(read("[-5/3, -4, 0]").get()), "-169/9");
        try {
            ZERO_DIMENSIONAL.dot(read("[1/2]").get());
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2]").get().dot(ZERO_DIMENSIONAL);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2, 4, -4]").get().dot(read("[5/6, 2/3]").get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRightAngleCompare() {
        aeq(ZERO_DIMENSIONAL.rightAngleCompare(ZERO_DIMENSIONAL), "EQ");
        aeq(read("[2]").get().rightAngleCompare(read("[3]").get()), "LT");
        aeq(read("[2]").get().rightAngleCompare(read("[-3]").get()), "GT");
        aeq(read("[1, 0]").get().rightAngleCompare(read("[1, 1]").get()), "LT");
        aeq(read("[1, -1]").get().rightAngleCompare(read("[1, 1]").get()), "EQ");
        aeq(read("[0, -1]").get().rightAngleCompare(read("[1, 1]").get()), "GT");
        aeq(read("[5/3, 4, 0]").get().rightAngleCompare(read("[-2, 1, 3]").get()), "LT");
        aeq(read("[5/3, 4, 0]").get().rightAngleCompare(read("[0, 0, 0]").get()), "EQ");
        aeq(read("[5/3, 4, 0]").get().rightAngleCompare(read("[-5/3, -4, 0]").get()), "GT");
        try {
            ZERO_DIMENSIONAL.rightAngleCompare(read("[1/2]").get());
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2]").get().rightAngleCompare(ZERO_DIMENSIONAL);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2, 4, -4]").get().rightAngleCompare(read("[5/6, 2/3]").get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSquaredLength() {
        aeq(ZERO_DIMENSIONAL.squaredLength(), "0");
        aeq(read("[2]").get().squaredLength(), "4");
        aeq(read("[1, 0]").get().squaredLength(), "1");
        aeq(read("[1, 1]").get().squaredLength(), "2");
        aeq(read("[5/3, 4, 0]").get().squaredLength(), "169/9");
        aeq(read("[1/2, 4, -4]").get().squaredLength(), "129/4");
    }

    @Test
    public void testCancelDenominators() {
        assertTrue(ZERO_DIMENSIONAL.cancelDenominators() == ZERO_DIMENSIONAL);
        aeq(read("[0]").get().cancelDenominators(), "[0]");
        aeq(read("[0, 0]").get().cancelDenominators(), "[0, 0]");
        aeq(read("[2/3]").get().cancelDenominators(), "[1]");
        aeq(read("[-2/3]").get().cancelDenominators(), "[-1]");
        aeq(read("[1, -2/3]").get().cancelDenominators(), "[3, -2]");
        aeq(read("[4, -4, 5/12, 0, 1]").get().cancelDenominators(), "[48, -48, 5, 0, 12]");
        aeq(read("[1, 1/2, 1/3, 1/4, 1/5]").get().cancelDenominators(), "[60, 30, 20, 15, 12]");
    }

    @Test
    public void testPivot() {
        assertFalse(ZERO_DIMENSIONAL.pivot().isPresent());
        assertFalse(read("[0]").get().pivot().isPresent());
        assertFalse(read("[0, 0, 0]").get().pivot().isPresent());
        aeq(read("[2/3]").get().pivot().get(), "2/3");
        aeq(read("[1, -2/3]").get().pivot().get(), "1");
        aeq(read("[0, 1, -2/3]").get().pivot().get(), "1");
        aeq(read("[0, 0, -2/3]").get().pivot().get(), "-2/3");
    }

    @Test
    public void testReduce() {
        aeq(ZERO_DIMENSIONAL.reduce(), "[]");
        aeq(read("[0]").get().reduce(), "[0]");
        aeq(read("[0, 0, 0]").get().reduce(), "[0, 0, 0]");
        aeq(read("[2/3]").get().reduce(), "[1]");
        aeq(read("[1, -2/3]").get().reduce(), "[1, -2/3]");
        aeq(read("[-2/3, 1]").get().reduce(), "[1, -3/2]");
        aeq(read("[0, 1, -2/3]").get().reduce(), "[0, 1, -2/3]");
        aeq(read("[0, 0, -2/3]").get().reduce(), "[0, 0, 1]");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readRationalVectorList("[[], [1/2], [5/3, -1/4, 23], [5/3, 1/4, 23]]"),
                readRationalVectorList("[[], [1/2], [5/3, -1/4, 23], [5/3, 1/4, 23]]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("[]", 1);
        hashCode_helper("[1/2]", 64);
        hashCode_helper("[5/3, -1/4, 23]", 181506);
        hashCode_helper("[5/3, 1/4, 23]", 183428);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(readRationalVectorList("[[], [1/2], [5/3, -1/4, 23], [5/3, 1/4, 23]]"));
    }

    @Test
    public void testRead() {
        assertTrue(read("[]").get() == ZERO_DIMENSIONAL);
        aeq(read("[1/2]").get(), "[1/2]");
        aeq(read("[0, -23/4, 7/8]").get(), "[0, -23/4, 7/8]");
        assertFalse(read("").isPresent());
        assertFalse(read("[ 1/2]").isPresent());
        assertFalse(read("[1/3, 2/4]").isPresent());
        assertFalse(read("[1/3, 2/0]").isPresent());
        assertFalse(read("hello").isPresent());
        assertFalse(read("][").isPresent());
        assertFalse(read("1/2, 2/3").isPresent());
        assertFalse(read("vfbdb ds").isPresent());
    }

    @Test
    public void testFindIn() {
        aeq(findIn("fr24rev[]evfre").get(), "([], 7)");
        assertTrue(findIn("fr24rev[]evfre").get().a == ZERO_DIMENSIONAL);
        aeq(findIn("]]][[3/4, 45/7][]dsvdf").get(), "([3/4, 45/7], 4)");
        aeq(findIn("]]][[3/4, 45/0][]dsvdf").get(), "([], 15)");
        aeq(findIn("]]][[3/4, 45/3][]dsvdf").get(), "([], 15)");
        assertFalse(findIn("").isPresent());
        assertFalse(findIn("]]][[3/4, 45/0]dsvdf").isPresent());
        assertFalse(findIn("]]][[3/4, 2/4]dsvdf").isPresent());
        assertFalse(findIn("hello").isPresent());
    }

    private static @NotNull List<Rational> readRationalList(@NotNull String s) {
        return Readers.readList(Rational::read).apply(s).get();
    }

    private static @NotNull List<Rational> readRationalListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Rational::read).apply(s).get();
    }

    private static @NotNull List<RationalVector> readRationalVectorList(@NotNull String s) {
        return Readers.readList(RationalVector::read).apply(s).get();
    }

    private static @NotNull List<RationalVector> readRationalVectorListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(RationalVector::read).apply(s).get();
    }
}