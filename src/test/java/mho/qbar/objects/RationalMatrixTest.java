package mho.qbar.objects;

import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.RationalMatrix.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
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

    private static @NotNull List<RationalVector> readRationalVectorList(@NotNull String s) {
        return Readers.readList(RationalVector::read).apply(s).get();
    }

    private static @NotNull List<RationalVector> readRationalVectorListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(RationalVector::read).apply(s).get();
    }
}
