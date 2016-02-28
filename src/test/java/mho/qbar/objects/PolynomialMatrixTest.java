package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.PolynomialMatrix.*;
import static mho.wheels.iterables.IterableUtils.toList;
import static mho.wheels.testing.Testing.aeq;
import static mho.wheels.testing.Testing.testCompareToHelper;
import static mho.wheels.testing.Testing.testEqualsHelper;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class PolynomialMatrixTest {
    private static void rows_helper(@NotNull String input, @NotNull String output) {
        aeq(toList(read(input).get().rows()), output);
    }

    @Test
    public void testRows() {
        rows_helper("[]#0", "[]");
        rows_helper("[]#1", "[]");
        rows_helper("[]#3", "[]");
        rows_helper("[[]]", "[[]]");
        rows_helper("[[], [], []]", "[[], [], []]");
        rows_helper("[[-x]]", "[[-x]]");
        rows_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "[[x-3, -8*x^2+x], [0, 7*x-1]]");
        rows_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]");
    }

    private static void columns_helper(@NotNull String input, @NotNull String output) {
        aeq(toList(read(input).get().columns()), output);
    }

    @Test
    public void testColumns() {
        columns_helper("[]#0", "[]");
        columns_helper("[]#1", "[[]]");
        columns_helper("[]#3", "[[], [], []]");
        columns_helper("[[]]", "[]");
        columns_helper("[[], [], []]", "[]");
        columns_helper("[[-x]]", "[[-x]]");
        columns_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "[[x-3, 0], [-8*x^2+x, 7*x-1]]");
        columns_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[[1, x^3-x^2+1], [x, 5], [x^10, 2*x+4]]");
    }

    private static void row_helper(@NotNull String input, int i, @NotNull String output) {
        aeq(read(input).get().row(i), output);
    }

    private static void row_fail_helper(@NotNull String input, int i) {
        try {
            read(input).get().row(i);
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testRow() {
        row_helper("[[]]", 0, "[]");
        row_helper("[[], [], []]", 0, "[]");
        row_helper("[[], [], []]", 2, "[]");
        row_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 0, "[1, x, x^10]");
        row_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 1, "[x^3-x^2+1, 5, 2*x+4]");
        row_fail_helper("[]#0", 0);
        row_fail_helper("[]#3", 0);
        row_fail_helper("[[]]", 1);
        row_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 2);
        row_fail_helper("[]#0", -1);
        row_fail_helper("[]#1", -1);
        row_fail_helper("[[]]", -1);
        row_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", -1);
    }

    private static void column_helper(@NotNull String input, int j, @NotNull String output) {
        aeq(read(input).get().column(j), output);
    }

    private static void column_fail_helper(@NotNull String input, int j) {
        try {
            read(input).get().column(j);
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testColumn() {
        column_helper("[]#3", 0, "[]");
        column_helper("[]#3", 2, "[]");
        column_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 0, "[1, x^3-x^2+1]");
        column_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 1, "[x, 5]");
        column_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 2, "[x^10, 2*x+4]");
        column_fail_helper("[]#0", 0);
        column_fail_helper("[[]]", 0);
        column_fail_helper("[[], [], []]", 0);
        column_fail_helper("[[]]", 1);
        column_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 3);
        column_fail_helper("[]#0", -1);
        column_fail_helper("[]#1", -1);
        column_fail_helper("[[]]", -1);
        column_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", -1);
    }

    private static void toRationalPolynomialMatrix_helper(@NotNull String input) {
        aeq(read(input).get().toRationalPolynomialMatrix(), input);
    }

    @Test
    public void testToRationalPolynomialMatrix() {
        toRationalPolynomialMatrix_helper("[]#0");
        toRationalPolynomialMatrix_helper("[]#1");
        toRationalPolynomialMatrix_helper("[]#3");
        toRationalPolynomialMatrix_helper("[[]]");
        toRationalPolynomialMatrix_helper("[[], [], []]");
        toRationalPolynomialMatrix_helper("[[-3]]");
        toRationalPolynomialMatrix_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]");
        toRationalPolynomialMatrix_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]");
    }

    private static void get_helper(@NotNull String input, int i, int j, @NotNull String output) {
        aeq(read(input).get().get(i, j), output);
    }

    private static void get_fail_helper(@NotNull String input, int i, int j) {
        try {
            read(input).get().get(i, j);
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testGet() {
        get_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 0, 0, "1");
        get_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 0, 2, "x^10");
        get_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 1, 0, "x^3-x^2+1");
        get_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 1, 2, "2*x+4");
        get_fail_helper("[]#0", 0, 0);
        get_fail_helper("[]#1", 0, 0);
        get_fail_helper("[[]]", 0, 0);
        get_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 2, 0);
        get_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 0, 3);
    }

    private static void fromRows_helper(@NotNull String input, @NotNull String output) {
        aeq(fromRows(readPolynomialVectorList(input)), output);
    }

    private static void fromRows_fail_helper(@NotNull String input) {
        try {
            fromRows(readPolynomialVectorListWithNulls(input));
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromRows() {
        fromRows_helper("[]", "[]#0");
        fromRows_helper("[[]]", "[[]]");
        fromRows_helper("[[-x]]", "[[-x]]");
        fromRows_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "[[x-3, -8*x^2+x], [0, 7*x-1]]");
        fromRows_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]");
        fromRows_fail_helper("[[x-3, -8*x^2+x], null, [0, 7*x-1]]");
        fromRows_fail_helper("[[x-3, -8*x^2+x], [0], [0, 7*x-1]]");
    }

    private static void fromColumns_helper(@NotNull String input, @NotNull String output) {
        aeq(fromColumns(readPolynomialVectorList(input)), output);
    }

    private static void fromColumns_fail_helper(@NotNull String input) {
        try {
            fromColumns(readPolynomialVectorListWithNulls(input));
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromColumns() {
        fromColumns_helper("[]", "[]#0");
        fromColumns_helper("[[]]", "[]#1");
        fromColumns_helper("[[-x]]", "[[-x]]");
        fromColumns_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "[[x-3, 0], [-8*x^2+x, 7*x-1]]");
        fromColumns_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[[1, x^3-x^2+1], [x, 5], [x^10, 2*x+4]]");
        fromColumns_fail_helper("[[x-3, -8*x^2+x], null, [0, 7*x-1]]");
        fromColumns_fail_helper("[[x-3, -8*x^2+x], [0], [0, 7*x-1]]");
    }

    private static void maxElementBitLength_helper(@NotNull String input, int output) {
        aeq(read(input).get().maxElementBitLength(), output);
    }

    @Test
    public void testMaxElementBitLength() {
        maxElementBitLength_helper("[]#0", 0);
        maxElementBitLength_helper("[]#1", 0);
        maxElementBitLength_helper("[]#3", 0);
        maxElementBitLength_helper("[[]]", 0);
        maxElementBitLength_helper("[[], [], []]", 0);
        maxElementBitLength_helper("[[-x]]", 1);
        maxElementBitLength_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", 4);
        maxElementBitLength_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 3);
    }

    private static void height_helper(@NotNull String input, int output) {
        aeq(read(input).get().height(), output);
    }

    @Test
    public void testHeight() {
        height_helper("[]#0", 0);
        height_helper("[]#1", 0);
        height_helper("[]#3", 0);
        height_helper("[[]]", 1);
        height_helper("[[], [], []]", 3);
        height_helper("[[-x]]", 1);
        height_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", 2);
        height_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 2);
    }

    private static void width_helper(@NotNull String input, int output) {
        aeq(read(input).get().width(), output);
    }

    @Test
    public void testWidth() {
        width_helper("[]#0", 0);
        width_helper("[]#1", 1);
        width_helper("[]#3", 3);
        width_helper("[[]]", 0);
        width_helper("[[], [], []]", 0);
        width_helper("[[-x]]", 1);
        width_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", 2);
        width_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", 3);
    }

    private static void isSquare_helper(@NotNull String input, boolean output) {
        aeq(read(input).get().isSquare(), output);
    }

    @Test
    public void testIsSquare() {
        isSquare_helper("[]#0", true);
        isSquare_helper("[]#1", false);
        isSquare_helper("[]#3", false);
        isSquare_helper("[[]]", false);
        isSquare_helper("[[], [], []]", false);
        isSquare_helper("[[-x]]", true);
        isSquare_helper("[[0, 0, 0], [0, 0, x], [0, 0, 0]]", true);
        isSquare_helper("[[0, 0, 0], [0, 0, x]]", false);
        isSquare_helper("[[0, 0], [0, x], [0, 0]]", false);
    }

    private static void isZero_helper(@NotNull String input, boolean output) {
        aeq(read(input).get().isZero(), output);
    }

    @Test
    public void testIsZero() {
        isZero_helper("[]#0", true);
        isZero_helper("[]#1", true);
        isZero_helper("[]#3", true);
        isZero_helper("[[]]", true);
        isZero_helper("[[0]]", true);
        isZero_helper("[[], [], []]", true);
        isZero_helper("[[0, 0, 0], [0, 0, 0], [0, 0, 0]]", true);
        isZero_helper("[[0, 0, 0], [0, 0, x], [0, 0, 0]]", false);
        isZero_helper("[[-x]]", false);
        isZero_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", false);
        isZero_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", false);
    }

    private static void zero_helper(int height, int width, @NotNull String output) {
        aeq(zero(height, width), output);
    }

    private static void zero_fail_helper(int height, int width) {
        try {
            zero(height, width);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testZero() {
        zero_helper(0, 0, "[]#0");
        zero_helper(0, 3, "[]#3");
        zero_helper(3, 0, "[[], [], []]");
        zero_helper(1, 1, "[[0]]");
        zero_helper(2, 2, "[[0, 0], [0, 0]]");
        zero_helper(3, 4, "[[0, 0, 0, 0], [0, 0, 0, 0], [0, 0, 0, 0]]");
        zero_fail_helper(-1, 0);
        zero_fail_helper(-1, 3);
        zero_fail_helper(0, -1);
        zero_fail_helper(3, -1);
        zero_fail_helper(-1, -1);
    }

    private static void isIdentity_helper(@NotNull String input, boolean output) {
        aeq(read(input).get().isIdentity(), output);
    }

    @Test
    public void testIsIdentity() {
        isIdentity_helper("[]#0", true);
        isIdentity_helper("[]#1", false);
        isIdentity_helper("[]#3", false);
        isIdentity_helper("[[]]", false);
        isIdentity_helper("[[], [], []]", false);
        isIdentity_helper("[[-x]]", false);
        isIdentity_helper("[[0]]", false);
        isIdentity_helper("[[1]]", true);
        isIdentity_helper("[[1], [2]]", false);
        isIdentity_helper("[[1, 0], [0, 1]]", true);
        isIdentity_helper("[[1, x], [0, 1]]", false);
        isIdentity_helper("[[0, 1], [1, 0]]", false);
        isIdentity_helper("[[1, 1], [1, 1]]", false);
        isIdentity_helper("[[1, 0, 0], [0, 1, 0], [0, 0, 1]]", true);
    }

    private static void identity_helper(int dimension, @NotNull String output) {
        aeq(identity(dimension), output);
    }

    private static void identity_fail_helper(int dimension) {
        try {
            identity(dimension);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testIdentity() {
        identity_helper(0, "[]#0");
        identity_helper(1, "[[1]]");
        identity_helper(3, "[[1, 0, 0], [0, 1, 0], [0, 0, 1]]");
        identity_helper(5, "[[1, 0, 0, 0, 0], [0, 1, 0, 0, 0], [0, 0, 1, 0, 0], [0, 0, 0, 1, 0], [0, 0, 0, 0, 1]]");
        identity_fail_helper(-1);
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readPolynomialMatrixList("[[]#0, []#1, []#3, [[]], [[], [], []], [[-x]]," +
                        " [[x-3, -8*x^2+x], [0, 7*x-1]], [[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]]"),
                readPolynomialMatrixList("[[]#0, []#1, []#3, [[]], [[], [], []], [[-x]]," +
                        " [[x-3, -8*x^2+x], [0, 7*x-1]], [[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("[]#0", 31);
        hashCode_helper("[]#1", 32);
        hashCode_helper("[]#3", 34);
        hashCode_helper("[[]]", 992);
        hashCode_helper("[[], [], []]", 954304);
        hashCode_helper("[[-x]]", 31683);
        hashCode_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", 55552746);
        hashCode_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", -2083242301);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(readPolynomialMatrixList("[[]#0, []#1, []#3, [[]], [[-x]]," +
                " [[x-3, -8*x^2+x], [0, 7*x-1]], [[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]], [[], [], []]]"));
    }

    private static void read_helper(@NotNull String input) {
        aeq(read(input).get(), input);
    }

    private static void read_fail_helper(@NotNull String input) {
        assertFalse(read(input).isPresent());
    }

    @Test
    public void testRead() {
        read_helper("[]#0");
        read_helper("[]#1");
        read_helper("[]#3");
        read_helper("[[]]");
        read_helper("[[], [], []]");
        read_helper("[[-x]]");
        read_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]");
        read_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]");
        read_fail_helper("");
        read_fail_helper("[]");
        read_fail_helper("[]#");
        read_fail_helper("[]#-1");
        read_fail_helper("[[]]#1");
        read_fail_helper("[[4]]#1");
        read_fail_helper("[2]");
        read_fail_helper("[[ ]]");
        read_fail_helper("[[],]");
        read_fail_helper("[[2/3]]");
        read_fail_helper("[[2/3*x]]");
        read_fail_helper("[[3], null]");
        read_fail_helper("[[3], [3, 3]]");
        read_fail_helper("[[]]]");
        read_fail_helper("[[[]]");
        read_fail_helper("hello");
        read_fail_helper("vdfvfmsl;dfbv");
    }

    private static void findIn_helper(@NotNull String input, @NotNull String output, int index) {
        Pair<PolynomialMatrix, Integer> result = findIn(input).get();
        aeq(result.a, output);
        aeq(result.b, index);
    }

    private static void findIn_fail_helper(@NotNull String input) {
        assertFalse(findIn(input).isPresent());
    }

    @Test
    public void testFindIn() {
        findIn_helper("fr24rev[[]]evfre", "[[]]", 7);
        findIn_helper(
                "]]][[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]][[]]dsvdf",
                "[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]",
                4
        );
        findIn_helper("]]][[[1, x, x^10], [x^3-x^2+1, 5/2, 2*x+4]][[]]dsvdf", "[[]]", 43);
        findIn_helper("]]][[][]#02[]dsvdf", "[]#0", 6);
        findIn_fail_helper("");
        findIn_fail_helper("]]][[[1, x, x^10], [x^3-x^2+1, 5/3, 2*x+4]]dsvdf");
        findIn_fail_helper("hello");
    }

    private static @NotNull List<Integer> readIntegerList(@NotNull String s) {
        return Readers.readList(Readers::readInteger).apply(s).get();
    }

    private static @NotNull List<Integer> readIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Readers::readInteger).apply(s).get();
    }

    private static @NotNull List<PolynomialVector> readPolynomialVectorList(@NotNull String s) {
        return Readers.readList(PolynomialVector::read).apply(s).get();
    }

    private static @NotNull List<PolynomialVector> readPolynomialVectorListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(PolynomialVector::read).apply(s).get();
    }

    private static @NotNull List<PolynomialMatrix> readPolynomialMatrixList(@NotNull String s) {
        return Readers.readList(PolynomialMatrix::read).apply(s).get();
    }
}
