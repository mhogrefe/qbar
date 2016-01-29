package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.Vector.*;
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

    private static void dimension_helper(@NotNull String input, int output) {
        aeq(read(input).get().dimension(), output);
    }

    @Test
    public void testDimension() {
        dimension_helper("[]", 0);
        dimension_helper("[2]", 1);
        dimension_helper("[5, -4, 23]", 3);
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
