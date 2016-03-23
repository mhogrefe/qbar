package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.Variable.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;

public class VariableTest {
    private static void getIndex_helper(@NotNull String x, int output) {
        aeq(read(x).get().getIndex(), output);
    }

    @Test
    public void testGetIndex() {
        getIndex_helper("a", 0);
        getIndex_helper("b", 1);
        getIndex_helper("c", 2);
        getIndex_helper("x", 23);
        getIndex_helper("y", 24);
        getIndex_helper("z", 25);
        getIndex_helper("ooo", 66);
    }

    private static void of_helper(int input, @NotNull String output) {
        aeq(of(input), output);
    }

    @Test
    public void testOf() {
        of_helper(0, "a");
        of_helper(1, "b");
        of_helper(2, "c");
        of_helper(23, "x");
        of_helper(24, "y");
        of_helper(25, "z");
        of_helper(66, "ooo");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(readVariableList("[a, b, c, x, y, z, ooo]"), readVariableList("[a, b, c, x, y, z, ooo]"));
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("a", 0);
        hashCode_helper("b", 1);
        hashCode_helper("c", 2);
        hashCode_helper("x", 23);
        hashCode_helper("y", 24);
        hashCode_helper("z", 25);
        hashCode_helper("ooo", 66);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(readVariableList("[a, b, c, x, y, z, ooo]"));
    }

    private static void read_helper(@NotNull String input) {
        aeq(read(input).get(), input);
    }

    private static void read_fail_helper(@NotNull String input) {
        assertFalse(read(input).isPresent());
    }

    @Test
    public void testRead() {
        read_helper("a");
        read_helper("b");
        read_helper("c");
        read_helper("x");
        read_helper("y");
        read_helper("z");
        read_helper("ooo");
        read_fail_helper("");
        read_fail_helper("1");
        read_fail_helper(" ");
        read_fail_helper("ab");
    }

    private static void findIn_helper(@NotNull String input, @NotNull String output, int index) {
        Pair<Variable, Integer> result = findIn(input).get();
        aeq(result.a, output);
        aeq(result.b, index);
    }

    private static void findIn_fail_helper(@NotNull String input) {
        assertFalse(findIn(input).isPresent());
    }

    @Test
    public void testFindIn() {
        findIn_helper("abcd1234xyz", "a", 0);
        findIn_helper("hello", "h", 0);
        findIn_helper("llama", "ll", 0);
        findIn_helper("123aabbcc", "aa", 3);
        findIn_fail_helper("");
        findIn_fail_helper("123");
        findIn_fail_helper("1, 2, 3");
    }

    private static @NotNull List<Variable> readVariableList(@NotNull String s) {
        return Readers.readList(Variable::read).apply(s).get();
    }
}
