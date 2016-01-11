package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.RationalMatrix.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class RationalMatrixTest {
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
        rows_helper("[[-2/3]]", "[[-2/3]]");
        rows_helper("[[-2/3, -8], [0, 5/7]]", "[[-2/3, -8], [0, 5/7]]");
        rows_helper("[[1, 9, -13], [20, 5, -6]]", "[[1, 9, -13], [20, 5, -6]]");
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
        columns_helper("[[-2/3]]", "[[-2/3]]");
        columns_helper("[[-2/3, -8], [0, 5/7]]", "[[-2/3, 0], [-8, 5/7]]");
        columns_helper("[[1, 9, -13], [20, 5, -6]]", "[[1, 20], [9, 5], [-13, -6]]");
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
        row_helper("[[1, 9, -13], [20, 5, -6]]", 0, "[1, 9, -13]");
        row_helper("[[1, 9, -13], [20, 5, -6]]", 1, "[20, 5, -6]");
        row_fail_helper("[]#0", 0);
        row_fail_helper("[]#3", 0);
        row_fail_helper("[[]]", 1);
        row_fail_helper("[[1, 9, -13], [20, 5, -6]]", 2);
        row_fail_helper("[]#0", -1);
        row_fail_helper("[]#1", -1);
        row_fail_helper("[[]]", -1);
        row_fail_helper("[[1, 9, -13], [20, 5, -6]]", -1);
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
        column_helper("[[1, 9, -13], [20, 5, -6]]", 0, "[1, 20]");
        column_helper("[[1, 9, -13], [20, 5, -6]]", 1, "[9, 5]");
        column_helper("[[1, 9, -13], [20, 5, -6]]", 2, "[-13, -6]");
        column_fail_helper("[]#0", 0);
        column_fail_helper("[[]]", 0);
        column_fail_helper("[[], [], []]", 0);
        column_fail_helper("[[]]", 1);
        column_fail_helper("[[1, 9, -13], [20, 5, -6]]", 3);
        column_fail_helper("[]#0", -1);
        column_fail_helper("[]#1", -1);
        column_fail_helper("[[]]", -1);
        column_fail_helper("[[1, 9, -13], [20, 5, -6]]", -1);
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
        get_helper("[[1, 9, -13], [20, 5, -6]]", 0, 0, "1");
        get_helper("[[1, 9, -13], [20, 5, -6]]", 0, 2, "-13");
        get_helper("[[1, 9, -13], [20, 5, -6]]", 1, 0, "20");
        get_helper("[[1, 9, -13], [20, 5, -6]]", 1, 2, "-6");
        get_fail_helper("[]#0", 0, 0);
        get_fail_helper("[]#1", 0, 0);
        get_fail_helper("[[]]", 0, 0);
        get_fail_helper("[[1, 9, -13], [20, 5, -6]]", 2, 0);
        get_fail_helper("[[1, 9, -13], [20, 5, -6]]", 0, 3);
    }

    private static void fromRows_helper(@NotNull String input, @NotNull String output) {
        aeq(fromRows(readRationalVectorList(input)), output);
    }

    private static void fromRows_fail_helper(@NotNull String input) {
        try {
            fromRows(readRationalVectorListWithNulls(input));
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromRows() {
        fromRows_helper("[]", "[]#0");
        fromRows_helper("[[]]", "[[]]");
        fromRows_helper("[[-2/3]]", "[[-2/3]]");
        fromRows_helper("[[-2/3, -8], [0, 5/7]]", "[[-2/3, -8], [0, 5/7]]");
        fromRows_helper("[[1, 9, -13], [20, 5, -6]]", "[[1, 9, -13], [20, 5, -6]]");
        fromRows_fail_helper("[[-2/3, -8], null, [0, 5/7]]");
        fromRows_fail_helper("[[-2/3, -8], [0], [0, 5/7]]");
    }

    private static void fromColumns_helper(@NotNull String input, @NotNull String output) {
        aeq(fromColumns(readRationalVectorList(input)), output);
    }

    private static void fromColumns_fail_helper(@NotNull String input) {
        try {
            fromColumns(readRationalVectorListWithNulls(input));
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromColumns() {
        fromColumns_helper("[]", "[]#0");
        fromColumns_helper("[[]]", "[]#1");
        fromColumns_helper("[[-2/3]]", "[[-2/3]]");
        fromColumns_helper("[[-2/3, -8], [0, 5/7]]", "[[-2/3, 0], [-8, 5/7]]");
        fromColumns_helper("[[1, 9, -13], [20, 5, -6]]", "[[1, 20], [9, 5], [-13, -6]]");
        fromColumns_fail_helper("[[-2/3, -8], null, [0, 5/7]]");
        fromColumns_fail_helper("[[-2/3, -8], [0], [0, 5/7]]");
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
        height_helper("[[-2/3]]", 1);
        height_helper("[[-2/3, -8], [0, 5/7]]", 2);
        height_helper("[[1, 9, -13], [20, 5, -6]]", 2);
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
        width_helper("[[-2/3]]", 1);
        width_helper("[[-2/3, -8], [0, 5/7]]", 2);
        width_helper("[[1, 9, -13], [20, 5, -6]]", 3);
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
        isSquare_helper("[[-2/3]]", true);
        isSquare_helper("[[0, 0, 0], [0, 0, 1], [0, 0, 0]]", true);
        isSquare_helper("[[0, 0, 0], [0, 0, 1]]", false);
        isSquare_helper("[[0, 0], [0, 1], [0, 0]]", false);
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
        isZero_helper("[[0, 0, 0], [0, 0, 1], [0, 0, 0]]", false);
        isZero_helper("[[-2/3]]", false);
        isZero_helper("[[-2/3, -8], [0, 5/7]]", false);
        isZero_helper("[[1, 9, -13], [20, 5, -6]]", false);
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
        isIdentity_helper("[[-2/3]]", false);
        isIdentity_helper("[[0]]", false);
        isIdentity_helper("[[1]]", true);
        isIdentity_helper("[[1], [2]]", false);
        isIdentity_helper("[[1, 0], [0, 1]]", true);
        isIdentity_helper("[[1, 1/5], [0, 1]]", false);
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
        identity_helper(1, "[[1]]");
        identity_helper(3, "[[1, 0, 0], [0, 1, 0], [0, 0, 1]]");
        identity_helper(5, "[[1, 0, 0, 0, 0], [0, 1, 0, 0, 0], [0, 0, 1, 0, 0], [0, 0, 0, 1, 0], [0, 0, 0, 0, 1]]");
        identity_fail_helper(0);
        identity_fail_helper(-1);
    }

    private static void submatrix_helper(
            @NotNull String m,
            @NotNull String rowIndices,
            @NotNull String columnIndices,
            @NotNull String output
    ) {
        aeq(read(m).get().submatrix(readIntegerList(rowIndices), readIntegerList(columnIndices)), output);
    }

    private static void submatrix_fail_helper(
            @NotNull String m,
            @NotNull String rowIndices,
            @NotNull String columnIndices
    ) {
        try {
            read(m).get().submatrix(readIntegerListWithNulls(rowIndices), readIntegerListWithNulls(columnIndices));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testSubmatrix() {
        submatrix_helper("[]#0", "[]", "[]", "[]#0");
        submatrix_helper("[]#3", "[]", "[]", "[]#0");
        submatrix_helper("[]#3", "[]", "[0, 2]", "[]#2");
        submatrix_helper("[]#3", "[]", "[0, 1, 2]", "[]#3");
        submatrix_helper("[[], [], []]", "[]", "[]", "[]#0");
        submatrix_helper("[[], [], []]", "[0, 2]", "[]", "[[], []]");
        submatrix_helper("[[], [], []]", "[0, 1, 2]", "[]", "[[], [], []]");
        submatrix_helper("[[1, 9, -13], [20, 5, -6]]", "[]", "[]", "[]#0");
        submatrix_helper("[[1, 9, -13], [20, 5, -6]]", "[]", "[1, 2]", "[]#2");
        submatrix_helper("[[1, 9, -13], [20, 5, -6]]", "[0, 1]", "[]", "[[], []]");
        submatrix_helper("[[1, 9, -13], [20, 5, -6]]", "[1]", "[0, 2]", "[[20, -6]]");
        submatrix_fail_helper("[[0]]", "[null]", "[]");
        submatrix_fail_helper("[[1, 9, -13], [20, 5, -6]]", "[1]", "[0, null]");
        submatrix_fail_helper("[[1, 9, -13], [20, 5, -6]]", "[1]", "[2, 0]");
        submatrix_fail_helper("[[1, 9, -13], [20, 5, -6]]", "[1]", "[0, 0, 2]");
        submatrix_fail_helper("[[1, 9, -13], [20, 5, -6]]", "[1, 0]", "[0, 2]");
        submatrix_fail_helper("[[1, 9, -13], [20, 5, -6]]", "[1, 1]", "[0, 2]");
        submatrix_fail_helper("[[1, 9, -13], [20, 5, -6]]", "[-1]", "[0, 2]");
        submatrix_fail_helper("[[1, 9, -13], [20, 5, -6]]", "[1]", "[-1, 2]");
        submatrix_fail_helper("[[1, 9, -13], [20, 5, -6]]", "[2]", "[0, 2]");
        submatrix_fail_helper("[[1, 9, -13], [20, 5, -6]]", "[1]", "[0, 3]");
    }

    private static void transpose_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().transpose(), output);
    }

    @Test
    public void testTranspose() {
        transpose_helper("[]#0", "[]#0");
        transpose_helper("[]#1", "[[]]");
        transpose_helper("[]#3", "[[], [], []]");
        transpose_helper("[[]]", "[]#1");
        transpose_helper("[[], [], []]", "[]#3");
        transpose_helper("[[-2/3]]", "[[-2/3]]");
        transpose_helper("[[-2/3, -8], [0, 5/7]]", "[[-2/3, 0], [-8, 5/7]]");
        transpose_helper("[[1, 9, -13], [20, 5, -6]]", "[[1, 20], [9, 5], [-13, -6]]");
    }

    private static void concat_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().concat(read(b).get()), output);
    }

    private static void concat_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().concat(read(b).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testConcat() {
        concat_helper("[]#0", "[]#0", "[]#0");
        concat_helper("[[]]", "[[], []]", "[[], [], []]");
        concat_helper("[[1/2]]", "[[1/3], [1/4]]", "[[1/2], [1/3], [1/4]]");
        concat_helper(
                "[[1, 3, 2], [2, 0, 1], [5, 2, 2]]",
                "[[4, 3, 1]]",
                "[[1, 3, 2], [2, 0, 1], [5, 2, 2], [4, 3, 1]]"
        );
        concat_fail_helper("[]#0", "[]#1");
        concat_fail_helper("[]#3", "[]#4");
        concat_fail_helper("[[]]", "[[2]]");
        concat_fail_helper("[[2]]", "[[]]");
        concat_fail_helper("[[1/2]]", "[[1/3, 1/4]]");
    }

    private static void augment_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().augment(read(b).get()), output);
    }

    private static void augment_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            read(a).get().augment(read(b).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testAugment() {
        augment_helper("[]#0", "[]#0", "[]#0");
        augment_helper("[]#3", "[]#4", "[]#7");
        augment_helper("[[]]", "[[2]]", "[[2]]");
        augment_helper("[[2]]", "[[]]", "[[2]]");
        augment_helper("[[1/2]]", "[[1/3, 1/4]]", "[[1/2, 1/3, 1/4]]");
        augment_helper(
                "[[1, 3, 2], [2, 0, 1], [5, 2, 2]]",
                "[[4], [3], [1]]",
                "[[1, 3, 2, 4], [2, 0, 1, 3], [5, 2, 2, 1]]"
        );
        augment_fail_helper("[]#0", "[[]]");
        augment_fail_helper("[[]]", "[[], []]");
        augment_fail_helper("[[1/2]]", "[[1/3], [1/4]]");
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
        add_helper("[]#0", "[]#0", "[]#0");
        add_helper("[]#1", "[]#1", "[]#1");
        add_helper("[]#3", "[]#3", "[]#3");
        add_helper("[[]]", "[[]]", "[[]]");
        add_helper("[[], [], []]", "[[], [], []]", "[[], [], []]");
        add_helper("[[2/3]]", "[[4/5]]", "[[22/15]]");
        add_helper("[[1, 3], [1, 0], [1, 2]]", "[[0, 0], [7, 5], [2, 1]]", "[[1, 3], [8, 5], [3, 3]]");
        add_fail_helper("[]#0", "[]#1");
        add_fail_helper("[]#0", "[[]]");
        add_fail_helper("[[2/3]]", "[[2/3], [4/5]]");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().negate(), output);
    }

    @Test
    public void testNegate() {
        negate_helper("[]#0", "[]#0");
        negate_helper("[]#1", "[]#1");
        negate_helper("[]#3", "[]#3");
        negate_helper("[[]]", "[[]]");
        negate_helper("[[], [], []]", "[[], [], []]");
        negate_helper("[[-2/3]]", "[[2/3]]");
        negate_helper("[[-2/3, -8], [0, 5/7]]", "[[2/3, 8], [0, -5/7]]");
        negate_helper("[[1, 9, -13], [20, 5, -6]]", "[[-1, -9, 13], [-20, -5, 6]]");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readRationalMatrixList("[[]#0, []#1, []#3, [[]], [[], [], []], [[-2/3]], [[-2/3, -8], [0, 5/7]]," +
                        " [[1, 9, -13], [20, 5, -6]]]"),
                readRationalMatrixList("[[]#0, []#1, []#3, [[]], [[], [], []], [[-2/3]], [[-2/3, -8], [0, 5/7]]," +
                        " [[1, 9, -13], [20, 5, -6]]]")
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
        hashCode_helper("[[-2/3]]", 94);
        hashCode_helper("[[-2/3, -8], [0, 5/7]]", -1005948);
        hashCode_helper("[[1, 9, -13], [20, 5, -6]]", 85734688);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(readRationalMatrixList("[[]#0, []#1, []#3, [[]], [[-2/3]], [[-2/3, -8], [0, 5/7]]," +
                " [[1, 9, -13], [20, 5, -6]], [[], [], []]]"));
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
        read_helper("[[-2/3]]");
        read_helper("[[-2/3, -8], [0, 5/7]]");
        read_helper("[[1, 9, -13], [20, 5, -6]]");
        read_fail_helper("");
        read_fail_helper("[]");
        read_fail_helper("[]#");
        read_fail_helper("[]#-1");
        read_fail_helper("[[]]#1");
        read_fail_helper("[[4]]#1");
        read_fail_helper("[2]");
        read_fail_helper("[[ ]]");
        read_fail_helper("[[],]");
        read_fail_helper("[[1/0]]");
        read_fail_helper("[[2/4]]");
        read_fail_helper("[[1/3], null]");
        read_fail_helper("[[1/3], [2/3, 5/3]]");
        read_fail_helper("[[]]]");
        read_fail_helper("[[[]]");
        read_fail_helper("hello");
        read_fail_helper("vdfvfmsl;dfbv");
    }

    private static void findIn_helper(@NotNull String input, @NotNull String output, int index) {
        Pair<RationalMatrix, Integer> result = findIn(input).get();
        aeq(result.a, output);
        aeq(result.b, index);
    }

    private static void findIn_fail_helper(@NotNull String input) {
        assertFalse(findIn(input).isPresent());
    }

    @Test
    public void testFindIn() {
        findIn_helper("fr24rev[[]]evfre", "[[]]", 7);
        findIn_helper("]]][[[1, 9, -13], [20, 5, -6]][[]]dsvdf", "[[1, 9, -13], [20, 5, -6]]", 4);
        findIn_helper("]]][[[1, 9, -13], [20, 5/5, -6]][[]]dsvdf", "[[]]", 32);
        findIn_helper("]]][[[1, 9, -13], [20, 2/4, -6]][[]]dsvdf", "[[]]", 32);
        findIn_helper("]]][[][]#02[]dsvdf", "[]#0", 6);
        findIn_fail_helper("");
        findIn_fail_helper("]]][[[1, 9, -13], [20, 5/5, -6]]dsvdf");
        findIn_fail_helper("hello");
    }

    private static @NotNull List<Integer> readIntegerList(@NotNull String s) {
        return Readers.readList(Readers::readInteger).apply(s).get();
    }

    private static @NotNull List<Integer> readIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Readers::readInteger).apply(s).get();
    }

    private static @NotNull List<RationalVector> readRationalVectorList(@NotNull String s) {
        return Readers.readList(RationalVector::read).apply(s).get();
    }

    private static @NotNull List<RationalVector> readRationalVectorListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(RationalVector::read).apply(s).get();
    }

    private static @NotNull List<RationalMatrix> readRationalMatrixList(@NotNull String s) {
        return Readers.readList(RationalMatrix::read).apply(s).get();
    }
}
