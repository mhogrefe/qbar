package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.RationalMatrix.*;
import static mho.wheels.iterables.IterableUtils.toList;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class RationalMatrixTest {
    private static void rows_helper(@NotNull String input, @NotNull String output) {
        aeq(toList(readStrict(input).get().rows()), output);
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
        aeq(toList(readStrict(input).get().columns()), output);
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
        aeq(readStrict(input).get().row(i), output);
    }

    private static void row_fail_helper(@NotNull String input, int i) {
        try {
            readStrict(input).get().row(i);
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
        aeq(readStrict(input).get().column(j), output);
    }

    private static void column_fail_helper(@NotNull String input, int j) {
        try {
            readStrict(input).get().column(j);
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

    private static void onlyHasIntegralElements_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().onlyHasIntegralElements(), output);
    }

    @Test
    public void testHasIntegralElements() {
        onlyHasIntegralElements_helper("[]#0", true);
        onlyHasIntegralElements_helper("[]#1", true);
        onlyHasIntegralElements_helper("[]#3", true);
        onlyHasIntegralElements_helper("[[]]", true);
        onlyHasIntegralElements_helper("[[], [], []]", true);
        onlyHasIntegralElements_helper("[[-2/3]]", false);
        onlyHasIntegralElements_helper("[[-2/3, -8], [0, 5/7]]", false);
        onlyHasIntegralElements_helper("[[1, 9, -13], [20, 5, -6]]", true);
    }

    private static void toMatrix_helper(@NotNull String input) {
        aeq(readStrict(input).get().toMatrix(), input);
    }

    private static void toMatrix_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().toMatrix();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testToMatrix() {
        toMatrix_helper("[]#0");
        toMatrix_helper("[]#1");
        toMatrix_helper("[]#3");
        toMatrix_helper("[[]]");
        toMatrix_helper("[[], [], []]");
        toMatrix_helper("[[1, 9, -13], [20, 5, -6]]");
        toMatrix_fail_helper("[[-2/3]]");
        toMatrix_fail_helper("[[-2/3, -8], [0, 5/7]]");
    }

    private static void get_helper(@NotNull String input, int i, int j, @NotNull String output) {
        aeq(readStrict(input).get().get(i, j), output);
    }

    private static void get_fail_helper(@NotNull String input, int i, int j) {
        try {
            readStrict(input).get().get(i, j);
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

    private static void maxElementBitLength_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().maxElementBitLength(), output);
    }

    @Test
    public void testMaxElementBitLength() {
        maxElementBitLength_helper("[]#0", 0);
        maxElementBitLength_helper("[]#1", 0);
        maxElementBitLength_helper("[]#3", 0);
        maxElementBitLength_helper("[[]]", 0);
        maxElementBitLength_helper("[[], [], []]", 0);
        maxElementBitLength_helper("[[-2/3]]", 4);
        maxElementBitLength_helper("[[-2/3, -8], [0, 5/7]]", 6);
        maxElementBitLength_helper("[[1, 9, -13], [20, 5, -6]]", 6);
    }

    private static void height_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().height(), output);
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
        aeq(readStrict(input).get().width(), output);
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
        aeq(readStrict(input).get().isSquare(), output);
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
        aeq(readStrict(input).get().isZero(), output);
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
        aeq(readStrict(input).get().isIdentity(), output);
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
        identity_helper(0, "[]#0");
        identity_helper(1, "[[1]]");
        identity_helper(3, "[[1, 0, 0], [0, 1, 0], [0, 0, 1]]");
        identity_helper(5, "[[1, 0, 0, 0, 0], [0, 1, 0, 0, 0], [0, 0, 1, 0, 0], [0, 0, 0, 1, 0], [0, 0, 0, 0, 1]]");
        identity_fail_helper(-1);
    }

    private static void trace_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().trace(), output);
    }

    private static void trace_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().trace();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testTrace() {
        trace_helper("[]#0", "0");
        trace_helper("[[-2/3]]", "-2/3");
        trace_helper("[[-2/3, -8], [0, 5/7]]", "1/21");

        trace_fail_helper("[]#1");
        trace_fail_helper("[]#3");
        trace_fail_helper("[[]]");
        trace_fail_helper("[[], [], []]");
        trace_fail_helper("[[1, 9, -13], [20, 5, -6]]");
    }

    private static void submatrix_helper(
            @NotNull String m,
            @NotNull String rowIndices,
            @NotNull String columnIndices,
            @NotNull String output
    ) {
        aeq(readStrict(m).get().submatrix(readIntegerList(rowIndices), readIntegerList(columnIndices)), output);
    }

    private static void submatrix_fail_helper(
            @NotNull String m,
            @NotNull String rowIndices,
            @NotNull String columnIndices
    ) {
        try {
            readStrict(m).get()
                    .submatrix(readIntegerListWithNulls(rowIndices), readIntegerListWithNulls(columnIndices));
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
        aeq(readStrict(input).get().transpose(), output);
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
        aeq(readStrict(a).get().concat(readStrict(b).get()), output);
    }

    private static void concat_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().concat(readStrict(b).get());
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
        aeq(readStrict(a).get().augment(readStrict(b).get()), output);
    }

    private static void augment_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().augment(readStrict(b).get());
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
        aeq(readStrict(a).get().add(readStrict(b).get()), output);
    }

    private static void add_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().add(readStrict(b).get());
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
        aeq(readStrict(input).get().negate(), output);
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

    private static void subtract_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().subtract(readStrict(b).get()), output);
    }

    private static void subtract_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().subtract(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSubtract() {
        subtract_helper("[]#0", "[]#0", "[]#0");
        subtract_helper("[]#1", "[]#1", "[]#1");
        subtract_helper("[]#3", "[]#3", "[]#3");
        subtract_helper("[[]]", "[[]]", "[[]]");
        subtract_helper("[[], [], []]", "[[], [], []]", "[[], [], []]");
        subtract_helper("[[2/3]]", "[[4/5]]", "[[-2/15]]");
        subtract_helper("[[1, 3], [1, 0], [1, 2]]", "[[0, 0], [7, 5], [2, 1]]", "[[1, 3], [-6, -5], [-1, 1]]");
        subtract_fail_helper("[]#0", "[]#1");
        subtract_fail_helper("[]#0", "[[]]");
        subtract_fail_helper("[[2/3]]", "[[2/3], [4/5]]");
    }

    private static void multiply_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().multiply(Rational.readStrict(b).get()), output);
    }

    @Test
    public void testMultiply_Rational() {
        multiply_Rational_helper("[]#0", "0", "[]#0");
        multiply_Rational_helper("[]#0", "1", "[]#0");
        multiply_Rational_helper("[]#0", "-3/2", "[]#0");

        multiply_Rational_helper("[]#3", "0", "[]#3");
        multiply_Rational_helper("[]#3", "1", "[]#3");
        multiply_Rational_helper("[]#3", "-3/2", "[]#3");

        multiply_Rational_helper("[[], [], []]", "0", "[[], [], []]");
        multiply_Rational_helper("[[], [], []]", "1", "[[], [], []]");
        multiply_Rational_helper("[[], [], []]", "-3/2", "[[], [], []]");

        multiply_Rational_helper("[[-2/3]]", "0", "[[0]]");
        multiply_Rational_helper("[[-2/3]]", "1", "[[-2/3]]");
        multiply_Rational_helper("[[-2/3]]", "-3/2", "[[1]]");

        multiply_Rational_helper("[[-2/3, -8], [0, 5/7]]", "0", "[[0, 0], [0, 0]]");
        multiply_Rational_helper("[[-2/3, -8], [0, 5/7]]", "1", "[[-2/3, -8], [0, 5/7]]");
        multiply_Rational_helper("[[-2/3, -8], [0, 5/7]]", "-3/2", "[[1, 12], [0, -15/14]]");
    }

    private static void multiply_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().multiply(Readers.readBigIntegerStrict(b).get()), output);
    }

    @Test
    public void testMultiply_BigInteger() {
        multiply_BigInteger_helper("[]#0", "0", "[]#0");
        multiply_BigInteger_helper("[]#0", "1", "[]#0");
        multiply_BigInteger_helper("[]#0", "5", "[]#0");

        multiply_BigInteger_helper("[]#3", "0", "[]#3");
        multiply_BigInteger_helper("[]#3", "1", "[]#3");
        multiply_BigInteger_helper("[]#3", "5", "[]#3");

        multiply_BigInteger_helper("[[], [], []]", "0", "[[], [], []]");
        multiply_BigInteger_helper("[[], [], []]", "1", "[[], [], []]");
        multiply_BigInteger_helper("[[], [], []]", "5", "[[], [], []]");

        multiply_BigInteger_helper("[[-2/3]]", "0", "[[0]]");
        multiply_BigInteger_helper("[[-2/3]]", "1", "[[-2/3]]");
        multiply_BigInteger_helper("[[-2/3]]", "5", "[[-10/3]]");

        multiply_BigInteger_helper("[[-2/3, -8], [0, 5/7]]", "0", "[[0, 0], [0, 0]]");
        multiply_BigInteger_helper("[[-2/3, -8], [0, 5/7]]", "1", "[[-2/3, -8], [0, 5/7]]");
        multiply_BigInteger_helper("[[-2/3, -8], [0, 5/7]]", "5", "[[-10/3, -40], [0, 25/7]]");
    }

    private static void multiply_int_helper(@NotNull String a, int b, @NotNull String output) {
        aeq(readStrict(a).get().multiply(b), output);
    }

    @Test
    public void testMultiply_int() {
        multiply_int_helper("[]#0", 0, "[]#0");
        multiply_int_helper("[]#0", 1, "[]#0");
        multiply_int_helper("[]#0", 5, "[]#0");

        multiply_int_helper("[]#3", 0, "[]#3");
        multiply_int_helper("[]#3", 1, "[]#3");
        multiply_int_helper("[]#3", 5, "[]#3");

        multiply_int_helper("[[], [], []]", 0, "[[], [], []]");
        multiply_int_helper("[[], [], []]", 1, "[[], [], []]");
        multiply_int_helper("[[], [], []]", 5, "[[], [], []]");

        multiply_int_helper("[[-2/3]]", 0, "[[0]]");
        multiply_int_helper("[[-2/3]]", 1, "[[-2/3]]");
        multiply_int_helper("[[-2/3]]", 5, "[[-10/3]]");

        multiply_int_helper("[[-2/3, -8], [0, 5/7]]", 0, "[[0, 0], [0, 0]]");
        multiply_int_helper("[[-2/3, -8], [0, 5/7]]", 1, "[[-2/3, -8], [0, 5/7]]");
        multiply_int_helper("[[-2/3, -8], [0, 5/7]]", 5, "[[-10/3, -40], [0, 25/7]]");
    }

    private static void divide_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().divide(Rational.readStrict(b).get()), output);
    }

    private static void divide_Rational_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().divide(Rational.readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_Rational() {
        divide_Rational_helper("[]#0", "1", "[]#0");
        divide_Rational_helper("[]#0", "-3/2", "[]#0");

        divide_Rational_helper("[]#3", "1", "[]#3");
        divide_Rational_helper("[]#3", "-3/2", "[]#3");

        divide_Rational_helper("[[], [], []]", "1", "[[], [], []]");
        divide_Rational_helper("[[], [], []]", "-3/2", "[[], [], []]");

        divide_Rational_helper("[[-2/3]]", "1", "[[-2/3]]");
        divide_Rational_helper("[[-2/3]]", "-3/2", "[[4/9]]");

        divide_Rational_helper("[[-2/3, -8], [0, 5/7]]", "1", "[[-2/3, -8], [0, 5/7]]");
        divide_Rational_helper("[[-2/3, -8], [0, 5/7]]", "-3/2", "[[4/9, 16/3], [0, -10/21]]");

        divide_Rational_fail_helper("[]#0", "0");
        divide_Rational_fail_helper("[]#3", "0");
        divide_Rational_fail_helper("[[], [], []]", "0");
        divide_Rational_fail_helper("[[-2/3]]", "0");
        divide_Rational_fail_helper("[[-2/3, -8], [0, 5/7]]", "0");
    }

    private static void divide_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().divide(Readers.readBigIntegerStrict(b).get()), output);
    }

    private static void divide_BigInteger_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().divide(Readers.readBigIntegerStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_BigInteger() {
        divide_BigInteger_helper("[]#0", "1", "[]#0");
        divide_BigInteger_helper("[]#0", "5", "[]#0");

        divide_BigInteger_helper("[]#3", "1", "[]#3");
        divide_BigInteger_helper("[]#3", "5", "[]#3");

        divide_BigInteger_helper("[[], [], []]", "1", "[[], [], []]");
        divide_BigInteger_helper("[[], [], []]", "5", "[[], [], []]");

        divide_BigInteger_helper("[[-2/3]]", "1", "[[-2/3]]");
        divide_BigInteger_helper("[[-2/3]]", "5", "[[-2/15]]");

        divide_BigInteger_helper("[[-2/3, -8], [0, 5/7]]", "1", "[[-2/3, -8], [0, 5/7]]");
        divide_BigInteger_helper("[[-2/3, -8], [0, 5/7]]", "5", "[[-2/15, -8/5], [0, 1/7]]");

        divide_BigInteger_fail_helper("[]#0", "0");
        divide_BigInteger_fail_helper("[]#3", "0");
        divide_BigInteger_fail_helper("[[], [], []]", "0");
        divide_BigInteger_fail_helper("[[-2/3]]", "0");
        divide_BigInteger_fail_helper("[[-2/3, -8], [0, 5/7]]", "0");
    }

    private static void divide_int_helper(@NotNull String a, int b, @NotNull String output) {
        aeq(readStrict(a).get().divide(b), output);
    }

    private static void divide_int_fail_helper(@NotNull String a, int b) {
        try {
            readStrict(a).get().divide(b);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_int() {
        divide_int_helper("[]#0", 1, "[]#0");
        divide_int_helper("[]#0", 5, "[]#0");

        divide_int_helper("[]#3", 1, "[]#3");
        divide_int_helper("[]#3", 5, "[]#3");

        divide_int_helper("[[], [], []]", 1, "[[], [], []]");
        divide_int_helper("[[], [], []]", 5, "[[], [], []]");

        divide_int_helper("[[-2/3]]", 1, "[[-2/3]]");
        divide_int_helper("[[-2/3]]", 5, "[[-2/15]]");

        divide_int_helper("[[-2/3, -8], [0, 5/7]]", 1, "[[-2/3, -8], [0, 5/7]]");
        divide_int_helper("[[-2/3, -8], [0, 5/7]]", 5, "[[-2/15, -8/5], [0, 1/7]]");

        divide_int_fail_helper("[]#0", 0);
        divide_int_fail_helper("[]#3", 0);
        divide_int_fail_helper("[[], [], []]", 0);
        divide_int_fail_helper("[[-2/3]]", 0);
        divide_int_fail_helper("[[-2/3, -8], [0, 5/7]]", 0);
    }

    private static void multiply_RationalVector_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().multiply(RationalVector.readStrict(b).get()), output);
    }

    private static void multiply_RationalVector_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().multiply(RationalVector.readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testMultiply_RationalVector() {
        multiply_RationalVector_helper("[]#0", "[]", "[]");
        multiply_RationalVector_helper("[]#1", "[4/3]", "[]");
        multiply_RationalVector_helper("[]#3", "[4/3, 0, -3]", "[]");
        multiply_RationalVector_helper("[[]]", "[]", "[0]");
        multiply_RationalVector_helper("[[], [], []]", "[]", "[0, 0, 0]");
        multiply_RationalVector_helper("[[5]]", "[-1/3]", "[-5/3]");
        multiply_RationalVector_helper("[[0, 0], [0, 0]]", "[3, 2]", "[0, 0]");
        multiply_RationalVector_helper("[[1, 0], [0, 1]]", "[3, 2]", "[3, 2]");
        multiply_RationalVector_helper("[[1, -1, 2], [0, -3, 1]]", "[2, 1, 0]", "[1, -3]");

        multiply_RationalVector_fail_helper("[]#0", "[0]");
        multiply_RationalVector_fail_helper("[]#3", "[1, 2]");
        multiply_RationalVector_fail_helper("[[1, 0], [0, 1]]", "[1, 2, 3]");
    }

    private static void multiply_RationalMatrix_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().multiply(readStrict(b).get()), output);
    }

    private static void multiply_RationalMatrix_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().multiply(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testMultiply_RationalMatrix() {
        multiply_RationalMatrix_helper("[]#0", "[]#0", "[]#0");
        multiply_RationalMatrix_helper("[]#1", "[[2/3, 4]]", "[]#2");
        multiply_RationalMatrix_helper("[[], [], []]", "[]#5", "[[0, 0, 0, 0, 0], [0, 0, 0, 0, 0], [0, 0, 0, 0, 0]]");
        multiply_RationalMatrix_helper("[[1], [2], [3], [4]]", "[[]]", "[[], [], [], []]");
        multiply_RationalMatrix_helper("[[5/3, 4, 0]]", "[[-2], [1], [3]]", "[[2/3]]");
        multiply_RationalMatrix_helper("[[-2/3, -8], [0, 5/7]]", "[[0, 0], [0, 0]]", "[[0, 0], [0, 0]]");
        multiply_RationalMatrix_helper("[[-2/3, -8], [0, 5/7]]", "[[1, 0], [0, 1]]", "[[-2/3, -8], [0, 5/7]]");

        multiply_RationalMatrix_helper("[[1, 3], [-1, 2]]", "[[3], [4]]", "[[15], [5]]");
        multiply_RationalMatrix_helper(
                "[[1, 2], [3, 4], [5, 6]]",
                "[[1, 2, 3, 4], [5, 6, 7, 8]]",
                "[[11, 14, 17, 20], [23, 30, 37, 44], [35, 46, 57, 68]]"
        );

        multiply_RationalMatrix_fail_helper("[]#0", "[[]]");
        multiply_RationalMatrix_fail_helper("[[1, 2, 3, 4], [5, 6, 7, 8]]", "[[1, 2], [3, 4], [5, 6]]");
    }

    private static void shiftLeft_helper(@NotNull String a, int bits, @NotNull String output) {
        aeq(readStrict(a).get().shiftLeft(bits), output);
    }

    @Test
    public void testShiftLeft() {
        shiftLeft_helper("[]#0", 0, "[]#0");
        shiftLeft_helper("[]#0", 1, "[]#0");
        shiftLeft_helper("[]#0", 2, "[]#0");
        shiftLeft_helper("[]#0", 3, "[]#0");
        shiftLeft_helper("[]#0", 4, "[]#0");
        shiftLeft_helper("[]#0", -1, "[]#0");
        shiftLeft_helper("[]#0", -2, "[]#0");
        shiftLeft_helper("[]#0", -3, "[]#0");
        shiftLeft_helper("[]#0", -4, "[]#0");

        shiftLeft_helper("[]#3", 0, "[]#3");
        shiftLeft_helper("[]#3", 1, "[]#3");
        shiftLeft_helper("[]#3", 2, "[]#3");
        shiftLeft_helper("[]#3", 3, "[]#3");
        shiftLeft_helper("[]#3", 4, "[]#3");
        shiftLeft_helper("[]#3", -1, "[]#3");
        shiftLeft_helper("[]#3", -2, "[]#3");
        shiftLeft_helper("[]#3", -3, "[]#3");
        shiftLeft_helper("[]#3", -4, "[]#3");

        shiftLeft_helper("[[], [], []]", 0, "[[], [], []]");
        shiftLeft_helper("[[], [], []]", 1, "[[], [], []]");
        shiftLeft_helper("[[], [], []]", 2, "[[], [], []]");
        shiftLeft_helper("[[], [], []]", 3, "[[], [], []]");
        shiftLeft_helper("[[], [], []]", 4, "[[], [], []]");
        shiftLeft_helper("[[], [], []]", -1, "[[], [], []]");
        shiftLeft_helper("[[], [], []]", -2, "[[], [], []]");
        shiftLeft_helper("[[], [], []]", -3, "[[], [], []]");
        shiftLeft_helper("[[], [], []]", -4, "[[], [], []]");

        shiftLeft_helper("[[2/3]]", 0, "[[2/3]]");
        shiftLeft_helper("[[2/3]]", 1, "[[4/3]]");
        shiftLeft_helper("[[2/3]]", 2, "[[8/3]]");
        shiftLeft_helper("[[2/3]]", 3, "[[16/3]]");
        shiftLeft_helper("[[2/3]]", 4, "[[32/3]]");
        shiftLeft_helper("[[2/3]]", -1, "[[1/3]]");
        shiftLeft_helper("[[2/3]]", -2, "[[1/6]]");
        shiftLeft_helper("[[2/3]]", -3, "[[1/12]]");
        shiftLeft_helper("[[2/3]]", -4, "[[1/24]]");

        shiftLeft_helper("[[-2/3, -8], [0, 5/7]]", 0, "[[-2/3, -8], [0, 5/7]]");
        shiftLeft_helper("[[-2/3, -8], [0, 5/7]]", 1, "[[-4/3, -16], [0, 10/7]]");
        shiftLeft_helper("[[-2/3, -8], [0, 5/7]]", 2, "[[-8/3, -32], [0, 20/7]]");
        shiftLeft_helper("[[-2/3, -8], [0, 5/7]]", 3, "[[-16/3, -64], [0, 40/7]]");
        shiftLeft_helper("[[-2/3, -8], [0, 5/7]]", 4, "[[-32/3, -128], [0, 80/7]]");
        shiftLeft_helper("[[-2/3, -8], [0, 5/7]]", -1, "[[-1/3, -4], [0, 5/14]]");
        shiftLeft_helper("[[-2/3, -8], [0, 5/7]]", -2, "[[-1/6, -2], [0, 5/28]]");
        shiftLeft_helper("[[-2/3, -8], [0, 5/7]]", -3, "[[-1/12, -1], [0, 5/56]]");
        shiftLeft_helper("[[-2/3, -8], [0, 5/7]]", -4, "[[-1/24, -1/2], [0, 5/112]]");
    }

    private static void shiftRight_helper(@NotNull String a, int bits, @NotNull String output) {
        aeq(readStrict(a).get().shiftRight(bits), output);
    }

    @Test
    public void testShiftRight() {
        shiftRight_helper("[]#0", 0, "[]#0");
        shiftRight_helper("[]#0", 1, "[]#0");
        shiftRight_helper("[]#0", 2, "[]#0");
        shiftRight_helper("[]#0", 3, "[]#0");
        shiftRight_helper("[]#0", 4, "[]#0");
        shiftRight_helper("[]#0", -1, "[]#0");
        shiftRight_helper("[]#0", -2, "[]#0");
        shiftRight_helper("[]#0", -3, "[]#0");
        shiftRight_helper("[]#0", -4, "[]#0");

        shiftRight_helper("[]#3", 0, "[]#3");
        shiftRight_helper("[]#3", 1, "[]#3");
        shiftRight_helper("[]#3", 2, "[]#3");
        shiftRight_helper("[]#3", 3, "[]#3");
        shiftRight_helper("[]#3", 4, "[]#3");
        shiftRight_helper("[]#3", -1, "[]#3");
        shiftRight_helper("[]#3", -2, "[]#3");
        shiftRight_helper("[]#3", -3, "[]#3");
        shiftRight_helper("[]#3", -4, "[]#3");

        shiftRight_helper("[[], [], []]", 0, "[[], [], []]");
        shiftRight_helper("[[], [], []]", 1, "[[], [], []]");
        shiftRight_helper("[[], [], []]", 2, "[[], [], []]");
        shiftRight_helper("[[], [], []]", 3, "[[], [], []]");
        shiftRight_helper("[[], [], []]", 4, "[[], [], []]");
        shiftRight_helper("[[], [], []]", -1, "[[], [], []]");
        shiftRight_helper("[[], [], []]", -2, "[[], [], []]");
        shiftRight_helper("[[], [], []]", -3, "[[], [], []]");
        shiftRight_helper("[[], [], []]", -4, "[[], [], []]");

        shiftRight_helper("[[2/3]]", 0, "[[2/3]]");
        shiftRight_helper("[[2/3]]", 1, "[[1/3]]");
        shiftRight_helper("[[2/3]]", 2, "[[1/6]]");
        shiftRight_helper("[[2/3]]", 3, "[[1/12]]");
        shiftRight_helper("[[2/3]]", 4, "[[1/24]]");
        shiftRight_helper("[[2/3]]", -1, "[[4/3]]");
        shiftRight_helper("[[2/3]]", -2, "[[8/3]]");
        shiftRight_helper("[[2/3]]", -3, "[[16/3]]");
        shiftRight_helper("[[2/3]]", -4, "[[32/3]]");

        shiftRight_helper("[[-2/3, -8], [0, 5/7]]", 0, "[[-2/3, -8], [0, 5/7]]");
        shiftRight_helper("[[-2/3, -8], [0, 5/7]]", 1, "[[-1/3, -4], [0, 5/14]]");
        shiftRight_helper("[[-2/3, -8], [0, 5/7]]", 2, "[[-1/6, -2], [0, 5/28]]");
        shiftRight_helper("[[-2/3, -8], [0, 5/7]]", 3, "[[-1/12, -1], [0, 5/56]]");
        shiftRight_helper("[[-2/3, -8], [0, 5/7]]", 4, "[[-1/24, -1/2], [0, 5/112]]");
        shiftRight_helper("[[-2/3, -8], [0, 5/7]]", -1, "[[-4/3, -16], [0, 10/7]]");
        shiftRight_helper("[[-2/3, -8], [0, 5/7]]", -2, "[[-8/3, -32], [0, 20/7]]");
        shiftRight_helper("[[-2/3, -8], [0, 5/7]]", -3, "[[-16/3, -64], [0, 40/7]]");
        shiftRight_helper("[[-2/3, -8], [0, 5/7]]", -4, "[[-32/3, -128], [0, 80/7]]");
    }

    private static void isInRowEchelonForm_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isInRowEchelonForm(), output);
    }

    @Test
    public void testIsInRowEchelonForm() {
        isInRowEchelonForm_helper("[]#0", true);
        isInRowEchelonForm_helper("[]#3", true);
        isInRowEchelonForm_helper("[[], [], []]", true);
        isInRowEchelonForm_helper("[[-2/3]]", false);
        isInRowEchelonForm_helper("[[-1]]", false);
        isInRowEchelonForm_helper("[[1]]", true);
        isInRowEchelonForm_helper("[[-2/3, -8], [0, 5/7]]", false);
        isInRowEchelonForm_helper("[[1, 9, -13], [20, 5, -6]]", false);
        isInRowEchelonForm_helper("[[1, 9, -13], [0, 1, -6]]", true);

        isInRowEchelonForm_helper(
                "[[0, -3, -6, 4, 9], [-1, -2, -1, 3, 1], [-2, -3, 0, 3, -1], [1, 4, 5, -9, -7]]",
                false
        );
        isInRowEchelonForm_helper(
                "[[1, 2, 1, -3, -1], [0, 1, 2, -4/3, -3], [0, 0, 0, 1, 0], [0, 0, 0, 0, 0]]",
                true
        );
    }

    private static void rowEchelonForm_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().rowEchelonForm(), output);
    }

    @Test
    public void testRowEchelonForm() {
        rowEchelonForm_helper("[]#0", "[]#0");
        rowEchelonForm_helper("[]#3", "[]#3");
        rowEchelonForm_helper("[[], [], []]", "[[], [], []]");
        rowEchelonForm_helper("[[-2/3]]", "[[1]]");
        rowEchelonForm_helper("[[-2/3, -8], [0, 5/7]]", "[[1, 12], [0, 1]]");
        rowEchelonForm_helper("[[1, 9, -13], [20, 5, -6]]", "[[1, 9, -13], [0, 1, -254/175]]");

        rowEchelonForm_helper(
                "[[0, -3, -6, 4, 9], [-1, -2, -1, 3, 1], [-2, -3, 0, 3, -1], [1, 4, 5, -9, -7]]",
                "[[1, 2, 1, -3, -1], [0, 1, 2, -4/3, -3], [0, 0, 0, 1, 0], [0, 0, 0, 0, 0]]"
        );
    }

    private static void rank_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().rank(), output);
    }

    @Test
    public void testRank() {
        rank_helper("[]#0", 0);
        rank_helper("[]#1", 0);
        rank_helper("[]#3", 0);
        rank_helper("[[]]", 0);
        rank_helper("[[], [], []]", 0);
        rank_helper("[[-2/3]]", 1);
        rank_helper("[[-2/3, -8], [0, 5/7]]", 2);
        rank_helper("[[1, 9, -13], [20, 5, -6]]", 2);

        rank_helper("[[1, 2, 1], [-2, -3, 1], [3, 5, 0]]", 2);
        rank_helper("[[1, 1, 0, 2], [-1, -1, 0, -2]]", 1);
        rank_helper("[[1, -1], [1, -1], [0, 0], [2, -2]]", 1);
    }

    private static void isInvertible_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isInvertible(), output);
    }

    private static void isInvertible_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().isInvertible();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testIsInvertible() {
        isInvertible_helper("[]#0", true);
        isInvertible_helper("[[-2/3]]", true);
        isInvertible_helper("[[-2/3, -8], [0, 5/7]]", true);
        isInvertible_helper("[[1, 2, 1], [-2, -3, 1], [3, 5, 0]]", false);
        isInvertible_helper("[[1, 1], [-1, -1]]", false);

        isInvertible_fail_helper("[]#1");
        isInvertible_fail_helper("[]#3");
        isInvertible_fail_helper("[[]]");
        isInvertible_fail_helper("[[], [], []]");
        isInvertible_fail_helper("[[1, 9, -13], [20, 5, -6]]");
    }

    private static void isInReducedRowEchelonForm_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isInReducedRowEchelonForm(), output);
    }

    @Test
    public void testIsInReducedRowEchelonForm() {
        isInReducedRowEchelonForm_helper("[]#0", true);
        isInReducedRowEchelonForm_helper("[]#3", true);
        isInReducedRowEchelonForm_helper("[[], [], []]", true);
        isInReducedRowEchelonForm_helper("[[-2/3]]", false);
        isInReducedRowEchelonForm_helper("[[-1]]", false);
        isInReducedRowEchelonForm_helper("[[1]]", true);
        isInReducedRowEchelonForm_helper("[[-2/3, -8], [0, 5/7]]", false);
        isInReducedRowEchelonForm_helper("[[1, 9, -13], [20, 5, -6]]", false);
        isInReducedRowEchelonForm_helper("[[1, 9, -13], [0, 1, -6]]", false);
        isInReducedRowEchelonForm_helper("[[1, 0, -13], [0, 1, -6]]", true);

        isInReducedRowEchelonForm_helper(
                "[[0, -3, -6, 4, 9], [-1, -2, -1, 3, 1], [-2, -3, 0, 3, -1], [1, 4, 5, -9, -7]]",
                false
        );
        isInReducedRowEchelonForm_helper(
                "[[1, 2, 1, -3, -1], [0, 1, 2, -4/3, -3], [0, 0, 0, 1, 0], [0, 0, 0, 0, 0]]",
                false
        );
        isInReducedRowEchelonForm_helper(
                "[[1, 0, -3, 0, 5], [0, 1, 2, 0, -3], [0, 0, 0, 1, 0], [0, 0, 0, 0, 0]]",
                true
        );
    }

    private static void reducedRowEchelonForm_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().reducedRowEchelonForm(), output);
    }

    @Test
    public void testReducedRowEchelonForm() {
        reducedRowEchelonForm_helper("[]#0", "[]#0");
        reducedRowEchelonForm_helper("[]#3", "[]#3");
        reducedRowEchelonForm_helper("[[], [], []]", "[[], [], []]");
        reducedRowEchelonForm_helper("[[-2/3]]", "[[1]]");
        reducedRowEchelonForm_helper("[[-2/3, -8], [0, 5/7]]", "[[1, 0], [0, 1]]");
        reducedRowEchelonForm_helper("[[1, 9, -13], [20, 5, -6]]", "[[1, 0, 11/175], [0, 1, -254/175]]");

        reducedRowEchelonForm_helper(
                "[[0, -3, -6, 4, 9], [-1, -2, -1, 3, 1], [-2, -3, 0, 3, -1], [1, 4, 5, -9, -7]]",
                "[[1, 0, -3, 0, 5], [0, 1, 2, 0, -3], [0, 0, 0, 1, 0], [0, 0, 0, 0, 0]]"
        );
    }

    private static void solveLinearSystem_helper(@NotNull String m, @NotNull String v, @NotNull String output) {
        aeq(readStrict(m).get().solveLinearSystem(RationalVector.readStrict(v).get()), output);
    }

    private static void solveLinearSystem_fail_helper(@NotNull String m, @NotNull String v) {
        try {
            readStrict(m).get().solveLinearSystem(RationalVector.readStrict(v).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testSolveLinearSystem() {
        solveLinearSystem_helper("[]#0", "[]", "Optional[[]]");
        solveLinearSystem_helper("[]#3", "[]", "Optional.empty");
        solveLinearSystem_helper("[[], [], []]", "[0, 0, 0]", "Optional[[]]");
        solveLinearSystem_helper("[[], [], []]", "[0, 3, 0]", "Optional.empty");
        solveLinearSystem_helper("[[-2/3]]", "[0]", "Optional[[0]]");
        solveLinearSystem_helper("[[-2/3]]", "[-2/3]", "Optional[[1]]");
        solveLinearSystem_helper("[[-2/3]]", "[5/7]", "Optional[[-15/14]]");

        solveLinearSystem_helper("[[1, 1], [1, -1]]", "[10, 5]", "Optional[[15/2, 5/2]]");
        solveLinearSystem_helper("[[1, 1], [1, -1], [2, 2]]", "[10, 5, 20]", "Optional[[15/2, 5/2]]");
        solveLinearSystem_helper("[[1, 1], [1, -1], [2, 2]]", "[10, 5, 19]", "Optional.empty");
        solveLinearSystem_helper("[[2, 3], [4, 9]]", "[6, 15]", "Optional[[3/2, 1]]");
        solveLinearSystem_helper("[[3, 2, -1], [2, -2, 4], [-1, 1/2, -1]]", "[1, -2, 0]", "Optional[[1, -2, -2]]");

        solveLinearSystem_fail_helper("[]#0", "[0]");
        solveLinearSystem_fail_helper("[[2, 3], [4, 9]]", "[6, 15, 3]");
    }

    private static void invert_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().invert(), output);
    }

    private static void invert_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().invert();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testInvert() {
        invert_helper("[]#0", "Optional[[]#0]");
        invert_helper("[[1]]", "Optional[[[1]]]");
        invert_helper("[[-1]]", "Optional[[[-1]]]");
        invert_helper("[[-2/3]]", "Optional[[[-3/2]]]");
        invert_helper("[[1, 0], [0, 1]]", "Optional[[[1, 0], [0, 1]]]");
        invert_helper("[[0, 1], [1, 0]]", "Optional[[[0, 1], [1, 0]]]");
        invert_helper("[[-2/3, -8], [0, 5/7]]", "Optional[[[-3/2, -84/5], [0, 7/5]]]");
        invert_helper("[[1, 3, 3], [1, 4, 3], [1, 3, 4]]", "Optional[[[7, -3, -3], [-1, 1, 0], [-1, 0, 1]]]");

        invert_fail_helper("[]#3");
        invert_fail_helper("[[], [], []]");
        invert_fail_helper("[[1, 9, -13], [20, 5, -6]]");
    }

    private static void determinant_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().determinant(), output);
    }

    private static void determinant_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().determinant();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testDeterminant() {
        determinant_helper("[]#0", "1");
        determinant_helper("[[1]]", "1");
        determinant_helper("[[-1]]", "-1");
        determinant_helper("[[-2/3]]", "-2/3");
        determinant_helper("[[1, 0], [0, 1]]", "1");
        determinant_helper("[[0, 1], [1, 0]]", "-1");
        determinant_helper("[[-2/3, -8], [0, 5/7]]", "-10/21");
        determinant_helper("[[-2, 2, -3], [-1, 1, 3], [2, 0, -1]]", "18");

        determinant_fail_helper("[]#3");
        determinant_fail_helper("[[], [], []]");
        determinant_fail_helper("[[1, 9, -13], [20, 5, -6]]");
    }

    private static void characteristicPolynomial_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().characteristicPolynomial(), output);
    }

    private static void characteristicPolynomial_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().characteristicPolynomial();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testCharacteristicPolynomial() {
        characteristicPolynomial_helper("[]#0", "1");
        characteristicPolynomial_helper("[[1]]", "x-1");
        characteristicPolynomial_helper("[[-1]]", "x+1");
        characteristicPolynomial_helper("[[-2/3]]", "x+2/3");
        characteristicPolynomial_helper("[[1, 0], [0, 1]]", "x^2-2*x+1");
        characteristicPolynomial_helper("[[0, 1], [1, 0]]", "x^2-1");
        characteristicPolynomial_helper("[[2, 1], [-1, 0]]", "x^2-2*x+1");
        characteristicPolynomial_helper("[[-2/3, -8], [0, 5/7]]", "x^2-1/21*x-10/21");
        characteristicPolynomial_helper("[[-2, 2, -3], [-1, 1, 3], [2, 0, -1]]", "x^3+2*x^2+7*x-18");

        characteristicPolynomial_fail_helper("[]#3");
        characteristicPolynomial_fail_helper("[[], [], []]");
        characteristicPolynomial_fail_helper("[[1, 9, -13], [20, 5, -6]]");
    }

    private static void kroneckerMultiply_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().kroneckerMultiply(readStrict(b).get()), output);
    }

    @Test
    public void testKroneckerMultiply() {
        kroneckerMultiply_helper("[]#0", "[]#0", "[]#0");
        kroneckerMultiply_helper("[]#0", "[[]]", "[]#0");
        kroneckerMultiply_helper("[[]]", "[]#0", "[]#0");
        kroneckerMultiply_helper("[]#1", "[[1/2, 4]]", "[]#2");
        kroneckerMultiply_helper("[[1/2, 4]]", "[]#1", "[]#2");
        kroneckerMultiply_helper("[[], [], []]", "[]#5", "[]#0");
        kroneckerMultiply_helper("[]#5", "[[], [], []]", "[]#0");
        kroneckerMultiply_helper("[[1], [2/3], [3], [4]]", "[[]]", "[[], [], [], []]");
        kroneckerMultiply_helper("[[1, 2, 3/5, 4], [5, 6, 7, 8]]", "[[1, 2], [3, 4], [5, 6]]",
                "[[1, 2, 2, 4, 3/5, 6/5, 4, 8], [3, 4, 6, 8, 9/5, 12/5, 12, 16], [5, 6, 10, 12, 3, 18/5, 20, 24]," +
                " [5, 10, 6, 12, 7, 14, 8, 16], [15, 20, 18, 24, 21, 28, 24, 32], [25, 30, 30, 36, 35, 42, 40, 48]]");
        kroneckerMultiply_helper("[[3, 4, 0]]", "[[-2], [1], [3]]", "[[-6, -8, 0], [3, 4, 0], [9, 12, 0]]");
        kroneckerMultiply_helper("[[-3, -8], [0, 1/7]]", "[[0, 0], [0, 0]]",
                "[[0, 0, 0, 0], [0, 0, 0, 0], [0, 0, 0, 0], [0, 0, 0, 0]]");
        kroneckerMultiply_helper("[[-3, -8], [0, 1/7]]", "[[1, 0], [0, 1]]",
                "[[-3, 0, -8, 0], [0, -3, 0, -8], [0, 0, 1/7, 0], [0, 0, 0, 1/7]]");
    }

    private static void kroneckerAdd_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().kroneckerAdd(readStrict(b).get()), output);
    }

    private static void kroneckerAdd_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().kroneckerAdd(readStrict(b).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testKroneckerAdd() {
        kroneckerAdd_helper("[]#0", "[]#0", "[]#0");
        kroneckerAdd_helper("[]#0", "[[0, 0, 0], [0, 0, 0], [0, 0, 0]]", "[]#0");
        kroneckerAdd_helper("[[0, 0, 0], [0, 0, 0], [0, 0, 0]]", "[]#0", "[]#0");
        kroneckerAdd_helper("[[1, 2/3], [3, 4]]", "[[-3]]", "[[-2, 2/3], [3, 1]]");
        kroneckerAdd_helper("[[1, 2/3], [3, 4]]", "[[7, 4], [2, 0]]",
                "[[8, 4, 2/3, 0], [2, 1, 0, 2/3], [3, 0, 11, 4], [0, 3, 2, 4]]");
        kroneckerAdd_helper("[[1, 2], [3, 4]]", "[[0, 0], [0, 0]]",
                "[[1, 0, 2, 0], [0, 1, 0, 2], [3, 0, 4, 0], [0, 3, 0, 4]]");
        kroneckerAdd_helper("[[1, 2], [3, 4]]", "[[1, 0], [0, 1]]",
                "[[2, 0, 2, 0], [0, 2, 0, 2], [3, 0, 5, 0], [0, 3, 0, 5]]");

        kroneckerAdd_fail_helper("[]#0", "[[], [], []]");
        kroneckerAdd_fail_helper("[[], [], []]", "[]#0");
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
        aeq(readStrict(input).get().hashCode(), hashCode);
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

    private static void readStrict_helper(@NotNull String input) {
        aeq(readStrict(input).get(), input);
    }

    private static void readStrict_fail_helper(@NotNull String input) {
        assertFalse(readStrict(input).isPresent());
    }

    @Test
    public void testReadStrict() {
        readStrict_helper("[]#0");
        readStrict_helper("[]#1");
        readStrict_helper("[]#3");
        readStrict_helper("[[]]");
        readStrict_helper("[[], [], []]");
        readStrict_helper("[[-2/3]]");
        readStrict_helper("[[-2/3, -8], [0, 5/7]]");
        readStrict_helper("[[1, 9, -13], [20, 5, -6]]");
        readStrict_fail_helper("");
        readStrict_fail_helper("[]");
        readStrict_fail_helper("[]#");
        readStrict_fail_helper("[]#-1");
        readStrict_fail_helper("[[]]#1");
        readStrict_fail_helper("[[4]]#1");
        readStrict_fail_helper("[2]");
        readStrict_fail_helper("[[ ]]");
        readStrict_fail_helper("[[],]");
        readStrict_fail_helper("[[1/0]]");
        readStrict_fail_helper("[[2/4]]");
        readStrict_fail_helper("[[1/3], null]");
        readStrict_fail_helper("[[1/3], [2/3, 5/3]]");
        readStrict_fail_helper("[[]]]");
        readStrict_fail_helper("[[[]]");
        readStrict_fail_helper("hello");
        readStrict_fail_helper("vdfvfmsl;dfbv");
    }

    private static @NotNull List<Integer> readIntegerList(@NotNull String s) {
        return Readers.readListStrict(Readers::readIntegerStrict).apply(s).get();
    }

    private static @NotNull List<Integer> readIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Readers::readIntegerStrict).apply(s).get();
    }

    private static @NotNull List<RationalVector> readRationalVectorList(@NotNull String s) {
        return Readers.readListStrict(RationalVector::readStrict).apply(s).get();
    }

    private static @NotNull List<RationalVector> readRationalVectorListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(RationalVector::readStrict).apply(s).get();
    }

    private static @NotNull List<RationalMatrix> readRationalMatrixList(@NotNull String s) {
        return Readers.readListStrict(RationalMatrix::readStrict).apply(s).get();
    }
}
