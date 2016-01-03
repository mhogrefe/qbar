package mho.qbar.objects;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static mho.qbar.objects.RationalMatrix.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

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
}
