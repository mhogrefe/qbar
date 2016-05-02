package mho.qbar.objects;

import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.Variable.of;
import static mho.qbar.objects.Variable.readStrict;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;

public class VariableTest {
    private static void getIndex_helper(@NotNull String x, int output) {
        aeq(readStrict(x).get().getIndex(), output);
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
        aeq(readStrict(input).get().hashCode(), hashCode);
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

    private static void readStrict_helper(@NotNull String input) {
        aeq(readStrict(input).get(), input);
    }

    private static void readStrict_fail_helper(@NotNull String input) {
        assertFalse(readStrict(input).isPresent());
    }

    @Test
    public void testReadStrict() {
        readStrict_helper("a");
        readStrict_helper("b");
        readStrict_helper("c");
        readStrict_helper("x");
        readStrict_helper("y");
        readStrict_helper("z");
        readStrict_helper("ooo");
        readStrict_fail_helper("");
        readStrict_fail_helper("1");
        readStrict_fail_helper(" ");
        readStrict_fail_helper("ab");
    }

    private static @NotNull List<Variable> readVariableList(@NotNull String s) {
        return Readers.readListStrict(Variable::readStrict).apply(s).get();
    }
}
