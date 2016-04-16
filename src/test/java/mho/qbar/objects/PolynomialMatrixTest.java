package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.PolynomialMatrix.*;
import static mho.wheels.iterables.IterableUtils.toList;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class PolynomialMatrixTest {
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
        rows_helper("[[-x]]", "[[-x]]");
        rows_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "[[x-3, -8*x^2+x], [0, 7*x-1]]");
        rows_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]");
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
        columns_helper("[[-x]]", "[[-x]]");
        columns_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "[[x-3, 0], [-8*x^2+x, 7*x-1]]");
        columns_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[[1, x^3-x^2+1], [x, 5], [x^10, 2*x+4]]");
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
        aeq(readStrict(input).get().toRationalPolynomialMatrix(), input);
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
        aeq(readStrict(input).get().maxElementBitLength(), output);
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
        aeq(readStrict(input).get().height(), output);
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
        aeq(readStrict(input).get().width(), output);
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
        aeq(readStrict(input).get().isSquare(), output);
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
        aeq(readStrict(input).get().isIdentity(), output);
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
        trace_helper("[[-3]]", "-3");
        trace_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "8*x-4");

        trace_fail_helper("[]#1");
        trace_fail_helper("[]#3");
        trace_fail_helper("[[]]");
        trace_fail_helper("[[], [], []]");
        trace_fail_helper("[[x-3, -8*x^2+x, x], [0, 7*x-1, 4]]");
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
        submatrix_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[]", "[]", "[]#0");
        submatrix_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[]", "[1, 2]", "[]#2");
        submatrix_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[0, 1]", "[]", "[[], []]");
        submatrix_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[1]", "[0, 2]", "[[x^3-x^2+1, 2*x+4]]");
        submatrix_fail_helper("[[0]]", "[null]", "[]");
        submatrix_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[1]", "[0, null]");
        submatrix_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[1]", "[2, 0]");
        submatrix_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[1]", "[0, 0, 2]");
        submatrix_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[1, 0]", "[0, 2]");
        submatrix_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[1, 1]", "[0, 2]");
        submatrix_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[-1]", "[0, 2]");
        submatrix_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[1]", "[-1, 2]");
        submatrix_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[2]", "[0, 2]");
        submatrix_fail_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[1]", "[0, 3]");
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
        transpose_helper("[[-x]]", "[[-x]]");
        transpose_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "[[x-3, 0], [-8*x^2+x, 7*x-1]]");
        transpose_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[[1, x^3-x^2+1], [x, 5], [x^10, 2*x+4]]");
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
        concat_helper("[[-x]]", "[[3], [4]]", "[[-x], [3], [4]]");
        concat_helper(
                "[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]",
                "[[4, 3, 1]]",
                "[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4], [4, 3, 1]]"
        );
        concat_fail_helper("[]#0", "[]#1");
        concat_fail_helper("[]#3", "[]#4");
        concat_fail_helper("[[]]", "[[2]]");
        concat_fail_helper("[[2]]", "[[]]");
        concat_fail_helper("[[2]]", "[[3, 4]]");
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
        augment_helper("[[-x]]", "[[3, 4]]", "[[-x, 3, 4]]");
        augment_helper(
                "[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]",
                "[[4], [3]]",
                "[[1, x, x^10, 4], [x^3-x^2+1, 5, 2*x+4, 3]]"
        );
        augment_fail_helper("[]#0", "[[]]");
        augment_fail_helper("[[]]", "[[], []]");
        augment_fail_helper("[[2]]", "[[3], [4]]");
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
        add_helper("[[-x]]", "[[5]]", "[[-x+5]]");
        add_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "[[x^5-x, -x+3], [2*x+1, 4]]",
                "[[x^5-3, -8*x^2+3], [2*x+1, 7*x+3]]");
        add_fail_helper("[]#0", "[]#1");
        add_fail_helper("[]#0", "[[]]");
        add_fail_helper("[[-x]]", "[[3], [5]]");
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
        negate_helper("[[-x]]", "[[x]]");
        negate_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "[[-x+3, 8*x^2-x], [0, -7*x+1]]");
        negate_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[[-1, -x, -x^10], [-x^3+x^2-1, -5, -2*x-4]]");
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
        subtract_helper("[[-x]]", "[[5]]", "[[-x-5]]");
        subtract_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "[[x^5-x, -x+3], [2*x+1, 4]]",
                "[[-x^5+2*x-3, -8*x^2+2*x-3], [-2*x-1, 7*x-5]]");
        subtract_fail_helper("[]#0", "[]#1");
        subtract_fail_helper("[]#0", "[[]]");
        subtract_fail_helper("[[-x]]", "[[3], [5]]");
    }

    private static void multiply_Polynomial_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().multiply(Polynomial.readStrict(b).get()), output);
    }

    @Test
    public void testMultiply_Polynomial() {
        multiply_Polynomial_helper("[]#0", "0", "[]#0");
        multiply_Polynomial_helper("[]#0", "1", "[]#0");
        multiply_Polynomial_helper("[]#0", "x", "[]#0");
        multiply_Polynomial_helper("[]#0", "x^2-4*x+7", "[]#0");

        multiply_Polynomial_helper("[]#3", "0", "[]#3");
        multiply_Polynomial_helper("[]#3", "1", "[]#3");
        multiply_Polynomial_helper("[]#3", "x", "[]#3");
        multiply_Polynomial_helper("[]#3", "x^2-4*x+7", "[]#3");

        multiply_Polynomial_helper("[[], [], []]", "0", "[[], [], []]");
        multiply_Polynomial_helper("[[], [], []]", "1", "[[], [], []]");
        multiply_Polynomial_helper("[[], [], []]", "x", "[[], [], []]");
        multiply_Polynomial_helper("[[], [], []]", "x^2-4*x+7", "[[], [], []]");

        multiply_Polynomial_helper("[[-x]]", "0", "[[0]]");
        multiply_Polynomial_helper("[[-x]]", "1", "[[-x]]");
        multiply_Polynomial_helper("[[-x]]", "x", "[[-x^2]]");
        multiply_Polynomial_helper("[[-x]]", "x^2-4*x+7", "[[-x^3+4*x^2-7*x]]");

        multiply_Polynomial_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "0", "[[0, 0], [0, 0]]");
        multiply_Polynomial_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "1", "[[x-3, -8*x^2+x], [0, 7*x-1]]");
        multiply_Polynomial_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "x", "[[x^2-3*x, -8*x^3+x^2], [0, 7*x^2-x]]");
        multiply_Polynomial_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "x^2-4*x+7",
                "[[x^3-7*x^2+19*x-21, -8*x^4+33*x^3-60*x^2+7*x], [0, 7*x^3-29*x^2+53*x-7]]");
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

        multiply_BigInteger_helper("[[-x]]", "0", "[[0]]");
        multiply_BigInteger_helper("[[-x]]", "1", "[[-x]]");
        multiply_BigInteger_helper("[[-x]]", "5", "[[-5*x]]");

        multiply_BigInteger_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "0", "[[0, 0], [0, 0]]");
        multiply_BigInteger_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "1", "[[x-3, -8*x^2+x], [0, 7*x-1]]");
        multiply_BigInteger_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "5", "[[5*x-15, -40*x^2+5*x], [0, 35*x-5]]");
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

        multiply_int_helper("[[-x]]", 0, "[[0]]");
        multiply_int_helper("[[-x]]", 1, "[[-x]]");
        multiply_int_helper("[[-x]]", 5, "[[-5*x]]");

        multiply_int_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", 0, "[[0, 0], [0, 0]]");
        multiply_int_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", 1, "[[x-3, -8*x^2+x], [0, 7*x-1]]");
        multiply_int_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", 5, "[[5*x-15, -40*x^2+5*x], [0, 35*x-5]]");
    }

    private static void multiply_PolynomialVector_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        aeq(readStrict(a).get().multiply(PolynomialVector.readStrict(b).get()), output);
    }

    private static void multiply_PolynomialVector_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().multiply(PolynomialVector.readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testMultiply_PolynomialVector() {
        multiply_PolynomialVector_helper("[]#0", "[]", "[]");
        multiply_PolynomialVector_helper("[]#1", "[3]", "[]");
        multiply_PolynomialVector_helper("[]#3", "[3, 0, -3]", "[]");
        multiply_PolynomialVector_helper("[[]]", "[]", "[0]");
        multiply_PolynomialVector_helper("[[], [], []]", "[]", "[0, 0, 0]");
        multiply_PolynomialVector_helper("[[-x]]", "[-3]", "[3*x]");
        multiply_PolynomialVector_helper("[[0, 0], [0, 0]]", "[3, 2]", "[0, 0]");
        multiply_PolynomialVector_helper("[[1, 0], [0, 1]]", "[3, 2]", "[3, 2]");
        multiply_PolynomialVector_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]", "[1, x, x^10]",
                "[x^20+x^2+1, 2*x^11+4*x^10+x^3-x^2+5*x+1]");

        multiply_PolynomialVector_fail_helper("[]#0", "[0]");
        multiply_PolynomialVector_fail_helper("[]#3", "[1, 2]");
        multiply_PolynomialVector_fail_helper("[[1, 0], [0, 1]]", "[1, 2, 3]");
    }

    private static void multiply_PolynomialMatrix_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        aeq(readStrict(a).get().multiply(readStrict(b).get()), output);
    }

    private static void multiply_PolynomialMatrix_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().multiply(readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testMultiply_PolynomialMatrix() {
        multiply_PolynomialMatrix_helper("[]#0", "[]#0", "[]#0");
        multiply_PolynomialMatrix_helper("[]#1", "[[3, 4]]", "[]#2");
        multiply_PolynomialMatrix_helper("[[], [], []]", "[]#5",
                "[[0, 0, 0, 0, 0], [0, 0, 0, 0, 0], [0, 0, 0, 0, 0]]");
        multiply_PolynomialMatrix_helper("[[1], [x], [x^2], [x^4]]", "[[]]", "[[], [], [], []]");
        multiply_PolynomialMatrix_helper("[[1, x, x^10]]", "[[-2*x], [3*x-1], [3]]", "[[3*x^10+3*x^2-3*x]]");
        multiply_PolynomialMatrix_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "[[0, 0], [0, 0]]", "[[0, 0], [0, 0]]");
        multiply_PolynomialMatrix_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "[[1, 0], [0, 1]]",
                "[[x-3, -8*x^2+x], [0, 7*x-1]]");

        multiply_PolynomialMatrix_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "[[x^5], [40*x]]",
                "[[x^6-3*x^5-320*x^3+40*x^2], [280*x^2-40*x]]");
        multiply_PolynomialMatrix_helper(
                "[[1, 2], [3, 4], [5, 6*x^2]]",
                "[[1, x-2, 3, 4], [5, 6, 7, 8]]",
                "[[11, x+10, 17, 20], [23, 3*x+18, 37, 44], [30*x^2+5, 36*x^2+5*x-10, 42*x^2+15, 48*x^2+20]]"
        );

        multiply_PolynomialMatrix_fail_helper("[]#0", "[[]]");
        multiply_PolynomialMatrix_fail_helper("[[1, 2, 3, 4], [5, 6, 7, 8]]", "[[1, 2], [3, 4], [5, 6]]");
    }

    private static void shiftLeft_helper(@NotNull String a, int bits, @NotNull String output) {
        aeq(readStrict(a).get().shiftLeft(bits), output);
    }

    private static void shiftLeft_fail_helper(@NotNull String a, int bits) {
        try {
            readStrict(a).get().shiftLeft(bits);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShiftLeft() {
        shiftLeft_helper("[]#0", 0, "[]#0");
        shiftLeft_helper("[]#0", 1, "[]#0");
        shiftLeft_helper("[]#0", 2, "[]#0");
        shiftLeft_helper("[]#0", 3, "[]#0");
        shiftLeft_helper("[]#0", 4, "[]#0");

        shiftLeft_helper("[]#3", 0, "[]#3");
        shiftLeft_helper("[]#3", 1, "[]#3");
        shiftLeft_helper("[]#3", 2, "[]#3");
        shiftLeft_helper("[]#3", 3, "[]#3");
        shiftLeft_helper("[]#3", 4, "[]#3");

        shiftLeft_helper("[[], [], []]", 0, "[[], [], []]");
        shiftLeft_helper("[[], [], []]", 1, "[[], [], []]");
        shiftLeft_helper("[[], [], []]", 2, "[[], [], []]");
        shiftLeft_helper("[[], [], []]", 3, "[[], [], []]");
        shiftLeft_helper("[[], [], []]", 4, "[[], [], []]");

        shiftLeft_helper("[[-x]]", 0, "[[-x]]");
        shiftLeft_helper("[[-x]]", 1, "[[-2*x]]");
        shiftLeft_helper("[[-x]]", 2, "[[-4*x]]");
        shiftLeft_helper("[[-x]]", 3, "[[-8*x]]");
        shiftLeft_helper("[[-x]]", 4, "[[-16*x]]");

        shiftLeft_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", 0, "[[x-3, -8*x^2+x], [0, 7*x-1]]");
        shiftLeft_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", 1, "[[2*x-6, -16*x^2+2*x], [0, 14*x-2]]");
        shiftLeft_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", 2, "[[4*x-12, -32*x^2+4*x], [0, 28*x-4]]");
        shiftLeft_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", 3, "[[8*x-24, -64*x^2+8*x], [0, 56*x-8]]");
        shiftLeft_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", 4, "[[16*x-48, -128*x^2+16*x], [0, 112*x-16]]");

        shiftLeft_fail_helper("[]#0", -1);
        shiftLeft_fail_helper("[]#3", -1);
        shiftLeft_fail_helper("[[], [], []]", -1);
        shiftLeft_fail_helper("[[-x]]", -1);
        shiftLeft_fail_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", -1);
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
        determinant_helper("[[-x]]", "-x");
        determinant_helper("[[1, 0], [0, 1]]", "1");
        determinant_helper("[[0, 1], [1, 0]]", "-1");
        determinant_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]", "7*x^2-22*x+3");
        determinant_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4], [2*x, 3*x-1, 201]]",
                "3*x^14-4*x^13+x^12-7*x^11-x^10-201*x^4+205*x^3+2*x^2-211*x+1009");

        determinant_fail_helper("[]#3");
        determinant_fail_helper("[[], [], []]");
        determinant_fail_helper("[[1, 9, -13], [20, 5, -6]]");
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
        aeq(readStrict(input).get().hashCode(), hashCode);
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
        readStrict_helper("[[-x]]");
        readStrict_helper("[[x-3, -8*x^2+x], [0, 7*x-1]]");
        readStrict_helper("[[1, x, x^10], [x^3-x^2+1, 5, 2*x+4]]");
        readStrict_fail_helper("");
        readStrict_fail_helper("[]");
        readStrict_fail_helper("[]#");
        readStrict_fail_helper("[]#-1");
        readStrict_fail_helper("[[]]#1");
        readStrict_fail_helper("[[4]]#1");
        readStrict_fail_helper("[2]");
        readStrict_fail_helper("[[ ]]");
        readStrict_fail_helper("[[],]");
        readStrict_fail_helper("[[2/3]]");
        readStrict_fail_helper("[[2/3*x]]");
        readStrict_fail_helper("[[3], null]");
        readStrict_fail_helper("[[3], [3, 3]]");
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

    private static @NotNull List<PolynomialVector> readPolynomialVectorList(@NotNull String s) {
        return Readers.readListStrict(PolynomialVector::readStrict).apply(s).get();
    }

    private static @NotNull List<PolynomialVector> readPolynomialVectorListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(PolynomialVector::readStrict).apply(s).get();
    }

    private static @NotNull List<PolynomialMatrix> readPolynomialMatrixList(@NotNull String s) {
        return Readers.readListStrict(PolynomialMatrix::readStrict).apply(s).get();
    }
}
