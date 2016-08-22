package mho.qbar.objects;

import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.Variable.of;
import static mho.qbar.objects.Variable.readStrict;
import static mho.wheels.testing.Testing.*;

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
        Variable v = of(input);
        v.validate();
        aeq(v, output);
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

    private static void readStrict_helper(@NotNull String input, @NotNull String output) {
        Optional<Variable> ov = readStrict(input);
        if (ov.isPresent()) {
            ov.get().validate();
        }
        aeq(ov, output);
    }

    @Test
    public void testReadStrict() {
        readStrict_helper("a", "Optional[a]");
        readStrict_helper("b", "Optional[b]");
        readStrict_helper("c", "Optional[c]");
        readStrict_helper("x", "Optional[x]");
        readStrict_helper("y", "Optional[y]");
        readStrict_helper("z", "Optional[z]");
        readStrict_helper("ooo", "Optional[ooo]");

        readStrict_helper("", "Optional.empty");
        readStrict_helper("1", "Optional.empty");
        readStrict_helper(" ", "Optional.empty");
        readStrict_helper("ab", "Optional.empty");
    }

    private static @NotNull List<Variable> readVariableList(@NotNull String s) {
        return Readers.readListStrict(Variable::readStrict).apply(s).get();
    }
}
