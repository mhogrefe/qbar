package mho.qbar.objects;

import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.Algebraic.*;
import static mho.wheels.testing.Testing.aeq;
import static mho.wheels.testing.Testing.testCompareToHelper;
import static mho.wheels.testing.Testing.testEqualsHelper;
import static org.junit.Assert.assertFalse;

public class AlgebraicTest {
    @Test
    public void testConstants() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
        aeq(TEN, "10");
        aeq(TWO, "2");
        aeq(NEGATIVE_ONE, "-1");
        aeq(ONE_HALF, "1/2");
        aeq(SQRT_TWO, "sqrt(2)");
        aeq(PHI, "(1+sqrt(5))/2");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readAlgebraicList("[-sqrt(2), -4/3, 0, 1/2, 1, root 0 of x^5-x-1, sqrt(2), (1+sqrt(5))/2, sqrt(3)]"),
                readAlgebraicList("[-sqrt(2), -4/3, 0, 1/2, 1, root 0 of x^5-x-1, sqrt(2), (1+sqrt(5))/2, sqrt(3)]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("0", 32);
        hashCode_helper("1", 63);
        hashCode_helper("1/2", 64);
        hashCode_helper("-4/3", -90);
        hashCode_helper("sqrt(2)", 27901);
        hashCode_helper("-sqrt(2)", 27870);
        hashCode_helper("(1+sqrt(5))/2", 28831);
        hashCode_helper("root 0 of x^5-x-1", 857951010);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(
                readAlgebraicList("[-sqrt(2), -4/3, 0, 1/2, 1, root 0 of x^5-x-1, sqrt(2), (1+sqrt(5))/2, sqrt(3)]")
        );
    }

    private static void read_String_helper(@NotNull String input) {
        aeq(read(input).get(), input);
    }

    private static void read_String_fail_helper(@NotNull String input) {
        assertFalse(read(input).isPresent());
    }

    @Test
    public void testRead_String() {
        read_String_helper("0");
        read_String_helper("1");
        read_String_helper("1/2");
        read_String_helper("-4/3");
        read_String_helper("sqrt(2)");
        read_String_helper("-sqrt(2)");
        read_String_helper("(1+sqrt(5))/2");
        read_String_helper("root 0 of x^5-x-1");

        read_String_fail_helper("");
        read_String_fail_helper(" ");
        read_String_fail_helper("x");
        read_String_fail_helper("x^2+3");
        read_String_fail_helper("2/4");
        read_String_fail_helper("-");
        read_String_fail_helper("00");
        read_String_fail_helper("01");
        read_String_fail_helper("-0");
        read_String_fail_helper("+0");
        read_String_fail_helper("+2");
        read_String_fail_helper("sqrt(4)");
        read_String_fail_helper("sqrt(-1)");
        read_String_fail_helper("sqrt(0)");
        read_String_fail_helper("0*sqrt(2)");
        read_String_fail_helper("1*sqrt(2)");
        read_String_fail_helper("-1*sqrt(2)");
        read_String_fail_helper("0+sqrt(2)");
        read_String_fail_helper("0-sqrt(2)");
        read_String_fail_helper("(sqrt(2))");
        read_String_fail_helper("*sqrt(2)");
        read_String_fail_helper("+sqrt(2)");
        read_String_fail_helper("(sqrt(2))/2");
        read_String_fail_helper("sqrt(2)/1");
        read_String_fail_helper("sqrt(2)/0");
        read_String_fail_helper("sqrt(2)/-1");
        read_String_fail_helper("(2+2*sqrt(2))/2");
        read_String_fail_helper("root -1 of x^5-x-1");
        read_String_fail_helper("root 1 of x^5-x-1");
        read_String_fail_helper("root 0 of x^2-1");
        read_String_fail_helper("root 0 of x^10");
        read_String_fail_helper("roof 0 of x^5-x-1");
        read_String_fail_helper("root 0 on x^5-x-1");
    }

    private static @NotNull List<Algebraic> readAlgebraicList(@NotNull String s) {
        return Readers.readList(Algebraic::read).apply(s).get();
    }

    private static @NotNull List<Algebraic> readAlgebraicListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Algebraic::read).apply(s).get();
    }
}
