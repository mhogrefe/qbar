package mho.qbar.objects;

import mho.wheels.math.MathUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static mho.qbar.objects.ExponentVector.*;
import static mho.wheels.testing.Testing.aeq;
import static mho.wheels.testing.Testing.aeqit;
import static org.junit.Assert.fail;

public class ExponentVectorTest {
    @Test
    public void testConstants() {
        aeq(ONE, "1");
    }

    private static void exponent_helper(@NotNull String ev, @NotNull String v, int output) {
        aeq(read(ev).get().exponent(MathUtils.stringToVariableIndex(v).get()), output);
    }

    private static void exponent_fail_helper(@NotNull String ev, int i) {
        try {
            read(ev).get().exponent(i);
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testExponent() {
        exponent_helper("1", "a", 0);
        exponent_helper("1", "z", 0);
        exponent_helper("a", "a", 1);
        exponent_helper("a", "z", 0);
        exponent_helper("a^2", "a", 2);
        exponent_helper("a^2", "z", 0);
        exponent_helper("a^3", "a", 3);
        exponent_helper("a^3", "z", 0);
        exponent_helper("x^2*y*z^3", "a", 0);
        exponent_helper("x^2*y*z^3", "x", 2);
        exponent_helper("x^2*y*z^3", "y", 1);
        exponent_helper("x^2*y*z^3", "z", 3);
        exponent_fail_helper("1", -1);
        exponent_fail_helper("a", -1);
        exponent_fail_helper("x^2*y*z^3", -1);
    }

    private static void terms_helper(@NotNull String input, @NotNull String output) {
        aeqit(read(input).get().terms(), output);
    }

    @Test
    public void testTerms() {
        terms_helper("1", "[]");
        terms_helper("a", "[(0, 1)]");
        terms_helper("a^2", "[(0, 2)]");
        terms_helper("a^3", "[(0, 3)]");
        terms_helper("x^2*y*z^3", "[(23, 2), (24, 1), (25, 3)]");
    }
}
