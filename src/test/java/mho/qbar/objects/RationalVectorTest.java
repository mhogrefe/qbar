package mho.qbar.objects;

import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
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
    private static final int TINY_LIMIT = 20;

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

    @Test
    public void testAdd() {
        assertTrue(ZERO_DIMENSIONAL.add(ZERO_DIMENSIONAL) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().add(read("[3]").get()), "[5]");
        aeq(read("[5/3, 4, 0]").get().add(read("[-2, 1, 3]").get()), "[-1/3, 5, 3]");
        aeq(read("[5/3, 4, 0]").get().add(read("[0, 0, 0]").get()), "[5/3, 4, 0]");
        aeq(read("[5/3, 4, 0]").get().add(read("[-5/3, -4, 0]").get()), "[0, 0, 0]");
        try {
            ZERO_DIMENSIONAL.add(read("[1/2]").get());
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2]").get().add(ZERO_DIMENSIONAL);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2, 4, -4]").get().add(read("[5/6, 2/3]").get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testNegate() {
        aeq(ZERO_DIMENSIONAL.negate(), "[]");
        aeq(read("[1/2]").get().negate(), "[-1/2]");
        aeq(read("[5/3, -1/4, 23]").get().negate(), "[-5/3, 1/4, -23]");
    }

    @Test
    public void testSubtract() {
        assertTrue(ZERO_DIMENSIONAL.subtract(ZERO_DIMENSIONAL) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().subtract(read("[3]").get()), "[-1]");
        aeq(read("[5/3, 4, 0]").get().subtract(read("[-2, 1, 3]").get()), "[11/3, 3, -3]");
        aeq(read("[5/3, 4, 0]").get().subtract(read("[0, 0, 0]").get()), "[5/3, 4, 0]");
        aeq(read("[5/3, 4, 0]").get().subtract(read("[5/3, 4, 0]").get()), "[0, 0, 0]");
        aeq(read("[0, 0, 0]").get().subtract(read("[5/3, 4, 0]").get()), "[-5/3, -4, 0]");
        try {
            ZERO_DIMENSIONAL.subtract(read("[1/2]").get());
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2]").get().subtract(ZERO_DIMENSIONAL);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2, 4, -4]").get().subtract(read("[5/6, 2/3]").get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testMultiply_Rational() {
        assertTrue(ZERO_DIMENSIONAL.multiply(Rational.ZERO) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.multiply(Rational.read("-3/2").get()) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().multiply(Rational.ZERO), "[0]");
        aeq(read("[2]").get().multiply(Rational.read("-3/2").get()), "[-3]");
        aeq(read("[5/3, 4, 0]").get().multiply(Rational.ZERO), "[0, 0, 0]");
        aeq(read("[5/3, 4, 0]").get().multiply(Rational.read("-3/2").get()), "[-5/2, -6, 0]");
    }

    @Test
    public void testMultiply_BigInteger() {
        assertTrue(ZERO_DIMENSIONAL.multiply(BigInteger.ZERO) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.multiply(BigInteger.valueOf(5)) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().multiply(BigInteger.ZERO), "[0]");
        aeq(read("[2]").get().multiply(BigInteger.valueOf(5)), "[10]");
        aeq(read("[5/3, 4, 0]").get().multiply(BigInteger.ZERO), "[0, 0, 0]");
        aeq(read("[5/3, 4, 0]").get().multiply(BigInteger.valueOf(5)), "[25/3, 20, 0]");
    }

    @Test
    public void testMultiply_int() {
        assertTrue(ZERO_DIMENSIONAL.multiply(0) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.multiply(5) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().multiply(0), "[0]");
        aeq(read("[2]").get().multiply(5), "[10]");
        aeq(read("[5/3, 4, 0]").get().multiply(0), "[0, 0, 0]");
        aeq(read("[5/3, 4, 0]").get().multiply(5), "[25/3, 20, 0]");
    }

    @Test
    public void testDivide_Rational() {
        assertTrue(ZERO_DIMENSIONAL.divide(Rational.ONE) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.divide(Rational.read("-3/2").get()) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().divide(Rational.ONE), "[2]");
        aeq(read("[2]").get().divide(Rational.read("-3/2").get()), "[-4/3]");
        aeq(read("[5/3, 4, 0]").get().divide(Rational.ONE), "[5/3, 4, 0]");
        aeq(read("[5/3, 4, 0]").get().divide(Rational.read("-3/2").get()), "[-10/9, -8/3, 0]");
        try {
            ZERO_DIMENSIONAL.divide(Rational.ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            aeq(read("[5/3, 4, 0]").get().divide(Rational.ZERO), "[-10/9, -8/3, 0]");
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_BigInteger() {
        assertTrue(ZERO_DIMENSIONAL.divide(BigInteger.ONE) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.divide(BigInteger.valueOf(5)) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().divide(BigInteger.ONE), "[2]");
        aeq(read("[2]").get().divide(BigInteger.valueOf(5)), "[2/5]");
        aeq(read("[5/3, 4, 0]").get().divide(BigInteger.ONE), "[5/3, 4, 0]");
        aeq(read("[5/3, 4, 0]").get().divide(BigInteger.valueOf(5)), "[1/3, 4/5, 0]");
        try {
            ZERO_DIMENSIONAL.divide(BigInteger.ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            aeq(read("[5/3, 4, 0]").get().divide(BigInteger.ZERO), "[-10/9, -8/3, 0]");
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_int() {
        assertTrue(ZERO_DIMENSIONAL.divide(1) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.divide(5) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().divide(1), "[2]");
        aeq(read("[2]").get().divide(5), "[2/5]");
        aeq(read("[5/3, 4, 0]").get().divide(1), "[5/3, 4, 0]");
        aeq(read("[5/3, 4, 0]").get().divide(5), "[1/3, 4/5, 0]");
        try {
            ZERO_DIMENSIONAL.divide(0);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            aeq(read("[5/3, 4, 0]").get().divide(0), "[-10/9, -8/3, 0]");
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShiftLeft() {
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(0) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(1) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(2) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(3) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(4) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(-1) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(-2) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(-3) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(-4) == ZERO_DIMENSIONAL);
        aeq(read("[1/2]").get().shiftLeft(0), "[1/2]");
        aeq(read("[1/2]").get().shiftLeft(1), "[1]");
        aeq(read("[1/2]").get().shiftLeft(2), "[2]");
        aeq(read("[1/2]").get().shiftLeft(3), "[4]");
        aeq(read("[1/2]").get().shiftLeft(4), "[8]");
        aeq(read("[1/2]").get().shiftLeft(-1), "[1/4]");
        aeq(read("[1/2]").get().shiftLeft(-2), "[1/8]");
        aeq(read("[1/2]").get().shiftLeft(-3), "[1/16]");
        aeq(read("[1/2]").get().shiftLeft(-4), "[1/32]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(0), "[5/3, -1/4, 23]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(1), "[10/3, -1/2, 46]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(2), "[20/3, -1, 92]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(3), "[40/3, -2, 184]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(4), "[80/3, -4, 368]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(-1), "[5/6, -1/8, 23/2]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(-2), "[5/12, -1/16, 23/4]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(-3), "[5/24, -1/32, 23/8]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(-4), "[5/48, -1/64, 23/16]");
    }

    @Test
    public void testShiftRight() {
        assertTrue(ZERO_DIMENSIONAL.shiftRight(0) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(1) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(2) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(3) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(4) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(-1) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(-2) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(-3) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(-4) == ZERO_DIMENSIONAL);
        aeq(read("[1/2]").get().shiftRight(0), "[1/2]");
        aeq(read("[1/2]").get().shiftRight(1), "[1/4]");
        aeq(read("[1/2]").get().shiftRight(2), "[1/8]");
        aeq(read("[1/2]").get().shiftRight(3), "[1/16]");
        aeq(read("[1/2]").get().shiftRight(4), "[1/32]");
        aeq(read("[1/2]").get().shiftRight(-1), "[1]");
        aeq(read("[1/2]").get().shiftRight(-2), "[2]");
        aeq(read("[1/2]").get().shiftRight(-3), "[4]");
        aeq(read("[1/2]").get().shiftRight(-4), "[8]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(0), "[5/3, -1/4, 23]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(1), "[5/6, -1/8, 23/2]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(2), "[5/12, -1/16, 23/4]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(3), "[5/24, -1/32, 23/8]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(4), "[5/48, -1/64, 23/16]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(-1), "[10/3, -1/2, 46]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(-2), "[20/3, -1, 92]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(-3), "[40/3, -2, 184]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(-4), "[80/3, -4, 368]");
    }

    @Test
    public void testSum() {
        assertTrue(sum(readRationalVectorList("[[]]")) == ZERO_DIMENSIONAL);
        assertTrue(sum(readRationalVectorList("[[], [], []]")) == ZERO_DIMENSIONAL);
        aeq(sum(readRationalVectorList("[[5/3, 1/4, 23]]")), "[5/3, 1/4, 23]");
        aeq(
                sum(readRationalVectorList("[[5/3, 1/4, 23], [0, 2/3, -1/8], [32, -45/2, 9]]")),
                "[101/3, -259/12, 255/8]"
        );
        aeq(sum(readRationalVectorList("[[1/2], [2/3], [3/4]]")), "[23/12]");
        try {
            sum(readRationalVectorList("[]"));
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            sum(readRationalVectorListWithNulls("[[1/2, 3], null]"));
            fail();
        } catch (NullPointerException ignored) {}
        try {
            sum(readRationalVectorList("[[1/2], [3, 4]]"));
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDelta() {
        aeqit(delta(readRationalVectorList("[[]]")), "[]");
        aeqit(delta(readRationalVectorList("[[], [], []]")), "[[], []]");
        aeqit(delta(readRationalVectorList("[[5/3, 1/4, 23]]")), "[]");
        aeqit(
                delta(readRationalVectorList("[[5/3, 1/4, 23], [0, 2/3, -1/8], [32, -45/2, 9]]")),
                "[[-5/3, 5/12, -185/8], [32, -139/6, 73/8]]"
        );
        aeqit(delta(readRationalVectorList("[[1/2], [2/3], [3/4]]")), "[[1/6], [1/12]]");
        aeqitLimit(
                TINY_LIMIT,
                delta(
                        map(
                                i -> {
                                    Rational r = Rational.of(i);
                                    return of(Arrays.asList(r, r.pow(2), r.pow(3)));
                                },
                                rangeUp(1)
                        )
                ),
                "[[1, 3, 7], [1, 5, 19], [1, 7, 37], [1, 9, 61], [1, 11, 91], [1, 13, 127], [1, 15, 169]," +
                " [1, 17, 217], [1, 19, 271], [1, 21, 331], [1, 23, 397], [1, 25, 469], [1, 27, 547]," +
                " [1, 29, 631], [1, 31, 721], [1, 33, 817], [1, 35, 919], [1, 37, 1027], [1, 39, 1141]," +
                " [1, 41, 1261], ...]"
        );
        try {
            delta(readRationalVectorList("[]"));
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(delta(readRationalVectorListWithNulls("[[1/2, 3], null]")));
            fail();
        } catch (NullPointerException ignored) {}
        try {
            toList(delta(readRationalVectorList("[[1/2], [3, 4]]")));
            fail();
        } catch (ArithmeticException ignored) {}
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