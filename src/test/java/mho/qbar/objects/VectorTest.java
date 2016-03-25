package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static mho.qbar.objects.Vector.*;
import static mho.qbar.objects.Vector.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class VectorTest {
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
        iterator_helper("[2]");
        iterator_helper("[5, -4, 23]");
    }

    private static void toRationalVector_helper(@NotNull String input) {
        aeq(read(input).get().toRationalVector(), input);
    }

    @Test
    public void testToRationalVector() {
        toRationalVector_helper("[]");
        toRationalVector_helper("[2]");
        toRationalVector_helper("[5, -4, 23]");
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
        get_helper("[2]", 0, "2");
        get_helper("[5, -4, 23]", 0, "5");
        get_helper("[5, -4, 23]", 1, "-4");
        get_helper("[5, -4, 23]", 2, "23");
        get_fail_helper("[5, -4, 23]", 4);
        get_fail_helper("[2]", 3);
        get_fail_helper("[]", 0);
    }

    private static void of_List_BigInteger_helper(@NotNull String input) {
        aeq(of(readBigIntegerList(input)), input);
    }

    private static void of_List_BigInteger_fail_helper(@NotNull String input) {
        try {
            of(readBigIntegerListWithNulls(input));
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testOf_List_BigInteger() {
        of_List_BigInteger_helper("[]");
        of_List_BigInteger_helper("[2]");
        of_List_BigInteger_helper("[5, -4, 23]");
        of_List_BigInteger_fail_helper("[3, null, 23]");
    }

    private static void of_BigInteger_helper(@NotNull String input, @NotNull String output) {
        aeq(of(Readers.readBigInteger(input).get()), output);
    }

    @Test
    public void testOf_BigInteger() {
        of_BigInteger_helper("2", "[2]");
        of_BigInteger_helper("-5", "[-5]");
        of_BigInteger_helper("0", "[0]");
    }

    private static void maxCoordinateBitLength_helper(@NotNull String input, int output) {
        aeq(read(input).get().maxCoordinateBitLength(), output);
    }

    @Test
    public void testMaxCoordinateBitLength() {
        maxCoordinateBitLength_helper("[]", 0);
        maxCoordinateBitLength_helper("[2]", 2);
        maxCoordinateBitLength_helper("[-2]", 2);
        maxCoordinateBitLength_helper("[5, -4, 23]", 5);
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
        add_helper("[5, 4, 0]", "[-2, 1, 3]", "[3, 5, 3]");
        add_helper("[5, 4, 0]", "[0, 0, 0]", "[5, 4, 0]");
        add_helper("[5, 4, 0]", "[-5, -4, 0]", "[0, 0, 0]");
        add_fail_helper("[]", "[2]");
        add_fail_helper("[2]", "[]");
        add_fail_helper("[2, 4, -4]", "[6, 3]");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().negate(), output);
    }

    @Test
    public void testNegate() {
        negate_helper("[]", "[]");
        negate_helper("[2]", "[-2]");
        negate_helper("[5, -4, 23]", "[-5, 4, -23]");
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
        subtract_helper("[5, 4, 0]", "[-2, 1, 3]", "[7, 3, -3]");
        subtract_helper("[5, 4, 0]", "[0, 0, 0]", "[5, 4, 0]");
        subtract_helper("[5, 4, 0]", "[5, 4, 0]", "[0, 0, 0]");
        subtract_helper("[0, 0, 0]", "[5, 4, 0]", "[-5, -4, 0]");
        subtract_fail_helper("[]", "[2]");
        subtract_fail_helper("[2]", "[]");
        subtract_fail_helper("[2, 4, -4]", "[6, 3]");
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

        multiply_BigInteger_helper("[5, 4, 0]", "0", "[0, 0, 0]");
        multiply_BigInteger_helper("[5, 4, 0]", "1", "[5, 4, 0]");
        multiply_BigInteger_helper("[5, 4, 0]", "5", "[25, 20, 0]");
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

        multiply_int_helper("[5, 4, 0]", 0, "[0, 0, 0]");
        multiply_int_helper("[5, 4, 0]", 1, "[5, 4, 0]");
        multiply_int_helper("[5, 4, 0]", 5, "[25, 20, 0]");
    }

    private static void shiftLeft_helper(@NotNull String a, int bits, @NotNull String output) {
        aeq(read(a).get().shiftLeft(bits), output);
    }

    private static void shiftLeft_fail_helper(@NotNull String a, int bits) {
        try {
            read(a).get().shiftLeft(bits);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShiftLeft() {
        shiftLeft_helper("[]", 0, "[]");
        shiftLeft_helper("[]", 1, "[]");
        shiftLeft_helper("[]", 2, "[]");
        shiftLeft_helper("[]", 3, "[]");
        shiftLeft_helper("[]", 4, "[]");

        shiftLeft_helper("[2]", 0, "[2]");
        shiftLeft_helper("[2]", 1, "[4]");
        shiftLeft_helper("[2]", 2, "[8]");
        shiftLeft_helper("[2]", 3, "[16]");
        shiftLeft_helper("[2]", 4, "[32]");

        shiftLeft_helper("[5, -4, 23]", 0, "[5, -4, 23]");
        shiftLeft_helper("[5, -4, 23]", 1, "[10, -8, 46]");
        shiftLeft_helper("[5, -4, 23]", 2, "[20, -16, 92]");
        shiftLeft_helper("[5, -4, 23]", 3, "[40, -32, 184]");
        shiftLeft_helper("[5, -4, 23]", 4, "[80, -64, 368]");

        shiftLeft_fail_helper("[]", -1);
        shiftLeft_fail_helper("[5, -4, 23]", -1);
    }

    private static void sum_helper(@NotNull String input, @NotNull String output) {
        aeq(sum(readVectorList(input)), output);
    }

    private static void sum_fail_helper(@NotNull String input) {
        try {
            sum(readVectorListWithNulls(input));
            fail();
        } catch (IllegalArgumentException | ArithmeticException | NullPointerException ignored) {}
    }

    @Test
    public void testSum() {
        sum_helper("[[]]", "[]");
        sum_helper("[[], [], []]", "[]");
        sum_helper("[[5, 4, 23]]", "[5, 4, 23]");
        sum_helper("[[5, 4, 23], [0, 3, -8], [32, -45, 9]]", "[37, -38, 24]");
        sum_helper("[[2], [3], [4]]", "[9]");
        sum_fail_helper("[]");
        sum_fail_helper("[[2, 3], null]");
        sum_fail_helper("[[2], [3, 4]]");
    }

    private static void delta_helper(@NotNull Iterable<Vector> input, @NotNull String output) {
        aeqitLimit(TINY_LIMIT, delta(input), output);
    }

    private static void delta_helper(@NotNull String input, @NotNull String output) {
        delta_helper(readVectorList(input), output);
    }

    private static void delta_fail_helper(@NotNull String input) {
        try {
            toList(delta(readVectorListWithNulls(input)));
            fail();
        } catch (IllegalArgumentException | ArithmeticException | NullPointerException ignored) {}
    }

    @Test
    public void testDelta() {
        delta_helper("[[]]", "[]");
        delta_helper("[[], [], []]", "[[], []]");
        delta_helper("[[5, 4, 23]]", "[]");
        delta_helper("[[5, 4, 23], [0, 3, -8], [32, -45, 9]]", "[[-5, -1, -31], [32, -48, 17]]");
        delta_helper("[[2], [3], [4]]", "[[1], [1]]");
        delta_helper(map(i -> of(Arrays.asList(i, i.pow(2), i.pow(3))), rangeUp(BigInteger.ONE)),
                "[[1, 3, 7], [1, 5, 19], [1, 7, 37], [1, 9, 61], [1, 11, 91], [1, 13, 127], [1, 15, 169]," +
                " [1, 17, 217], [1, 19, 271], [1, 21, 331], [1, 23, 397], [1, 25, 469], [1, 27, 547], [1, 29, 631]," +
                " [1, 31, 721], [1, 33, 817], [1, 35, 919], [1, 37, 1027], [1, 39, 1141], [1, 41, 1261], ...]"
        );
        delta_fail_helper("[]");
        delta_fail_helper("[[2, 3], null]");
        delta_fail_helper("[[2], [3, 4]]");
    }

    private static void dot_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().dot(read(b).get()), output);
    }

    private static void dot_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().dot(read(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDot() {
        dot_helper("[]", "[]", "0");
        dot_helper("[2]", "[3]", "6");
        dot_helper("[5, 4, 0]", "[-2, 1, 3]", "-6");
        dot_helper("[5, 4, 0]", "[0, 0, 0]", "0");
        dot_helper("[5, 4, 0]", "[-5, -4, 0]", "-41");
        dot_fail_helper("[]", "[2]");
        dot_fail_helper("[2]", "[]");
        dot_fail_helper("[2, 4, -4]", "[6, 3]");
    }

    private static void rightAngleCompare_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().rightAngleCompare(read(b).get()), output);
    }

    private static void rightAngleCompare_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().rightAngleCompare(read(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRightAngleCompare() {
        rightAngleCompare_helper("[]", "[]", "EQ");
        rightAngleCompare_helper("[2]", "[3]", "LT");
        rightAngleCompare_helper("[2]", "[-3]", "GT");
        rightAngleCompare_helper("[1, 0]", "[1, 1]", "LT");
        rightAngleCompare_helper("[1, -1]", "[1, 1]", "EQ");
        rightAngleCompare_helper("[0, -1]", "[1, 1]", "GT");
        rightAngleCompare_helper("[5, 4, 0]", "[-2, 1, 3]", "GT");
        rightAngleCompare_helper("[5, 4, 0]", "[0, 0, 0]", "EQ");
        rightAngleCompare_helper("[5, 4, 0]", "[-5, -4, 0]", "GT");
        rightAngleCompare_fail_helper("[]", "[2]");
        rightAngleCompare_fail_helper("[2]", "[]");
        rightAngleCompare_fail_helper("[2, 4, -4]", "[6, 3]");
    }

    private static void squaredLength_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().squaredLength(), output);
    }

    @Test
    public void testSquaredLength() {
        squaredLength_helper("[]", "0");
        squaredLength_helper("[2]", "4");
        squaredLength_helper("[1, 0]", "1");
        squaredLength_helper("[1, 1]", "2");
        squaredLength_helper("[5, 4, 0]", "41");
        squaredLength_helper("[2, 4, -4]", "36");
    }

    private static void pivot_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().pivot().get(), output);
    }

    private static void pivot_empty_helper(@NotNull String input) {
        assertFalse(read(input).get().pivot().isPresent());
    }

    @Test
    public void testPivot() {
        pivot_helper("[3]", "3");
        pivot_helper("[1, -3]", "1");
        pivot_helper("[0, 1, -3]", "1");
        pivot_helper("[0, 0, -3]", "-3");
        pivot_empty_helper("[]");
        pivot_empty_helper("[0]");
        pivot_empty_helper("[0, 0, 0]");
    }

    private static void isReduced_helper(@NotNull String input, boolean output) {
        aeq(read(input).get().isReduced(), output);
    }

    @Test
    public void testIsReduced() {
        isReduced_helper("[]", true);
        isReduced_helper("[0]", true);
        isReduced_helper("[0, 0, 0]", true);
        isReduced_helper("[3]", false);
        isReduced_helper("[1, -3]", true);
        isReduced_helper("[-3, 1]", false);
        isReduced_helper("[0, 1, -3]", true);
        isReduced_helper("[0, 0, -3]", false);
    }

    private static void isPrimitive_helper(@NotNull String input, boolean output) {
        aeq(read(input).get().isPrimitive(), output);
    }

    @Test
    public void testIsPrimitive() {
        isPrimitive_helper("[]", true);
        isPrimitive_helper("[0]", true);
        isPrimitive_helper("[0, 0, 0]", true);
        isPrimitive_helper("[3]", false);
        isPrimitive_helper("[1, -3]", true);
        isPrimitive_helper("[-3, 1]", true);
        isPrimitive_helper("[2, 4]", false);
        isPrimitive_helper("[0, 1, -3]", true);
        isPrimitive_helper("[0, 0, -3]", false);
        isPrimitive_helper("[0, 0, 3]", false);
        isPrimitive_helper("[0, 0, 1]", true);
        isPrimitive_helper("[0, 0, -1]", true);
    }

    private static void makePrimitive_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().makePrimitive(), output);
    }

    @Test
    public void testMakePrimitive() {
        makePrimitive_helper("[]", "[]");
        makePrimitive_helper("[0]", "[0]");
        makePrimitive_helper("[0, 0, 0]", "[0, 0, 0]");
        makePrimitive_helper("[3]", "[1]");
        makePrimitive_helper("[1, -3]", "[1, -3]");
        makePrimitive_helper("[-3, 1]", "[-3, 1]");
        makePrimitive_helper("[2, 4]", "[1, 2]");
        makePrimitive_helper("[0, 1, -3]", "[0, 1, -3]");
        makePrimitive_helper("[0, 0, -3]", "[0, 0, -1]");
        makePrimitive_helper("[0, 0, 3]", "[0, 0, 1]");
        makePrimitive_helper("[0, 0, 1]", "[0, 0, 1]");
        makePrimitive_helper("[0, 0, -1]", "[0, 0, -1]");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readVectorList("[[], [2], [5, -4, 23], [5, 4, 23]]"),
                readVectorList("[[], [2], [5, -4, 23], [5, 4, 23]]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("[]", 1);
        hashCode_helper("[2]", 33);
        hashCode_helper("[5, -4, 23]", 34495);
        hashCode_helper("[5, 4, 23]", 34743);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(readVectorList("[[], [2], [5, -4, 23], [5, 4, 23]]"));
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
        read_helper("[2]");
        read_helper("[0, -23, 7]");
        read_fail_helper("");
        read_fail_helper("[ 1]");
        read_fail_helper("[1/3, 1/2]");
        read_fail_helper("[4, -5/3]");
        read_fail_helper("hello");
        read_fail_helper("][");
        read_fail_helper("1, 3");
        read_fail_helper("vfbdb ds");
    }

    private static void findIn_helper(@NotNull String input, @NotNull String output, int index) {
        Pair<Vector, Integer> result = findIn(input).get();
        aeq(result.a, output);
        aeq(result.b, index);
    }

    private static void findIn_fail_helper(@NotNull String input) {
        assertFalse(findIn(input).isPresent());
    }

    @Test
    public void testFindIn() {
        findIn_helper("fr24rev[]evfre", "[]", 7);
        findIn_helper("]]][[4, 45][]dsvdf", "[4, 45]", 4);
        findIn_helper("]]][[4,  45][]dsvdf", "[]", 12);
        findIn_helper("]]][[3/4, 45][]dsvdf", "[]", 13);
        findIn_fail_helper("");
        findIn_fail_helper("]]][[4,  45]dsvdf");
        findIn_fail_helper("]]][[3/4, 45]dsvdf");
        findIn_fail_helper("hello");
    }

    private static @NotNull List<BigInteger> readBigIntegerList(@NotNull String s) {
        return Readers.readList(Readers::readBigInteger).apply(s).get();
    }

    private static @NotNull List<BigInteger> readBigIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Readers::readBigInteger).apply(s).get();
    }

    private static @NotNull List<Vector> readVectorList(@NotNull String s) {
        return Readers.readList(Vector::read).apply(s).get();
    }

    private static @NotNull List<Vector> readVectorListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Vector::read).apply(s).get();
    }
}
