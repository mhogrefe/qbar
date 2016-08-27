package mho.qbar.objects;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static mho.qbar.objects.Real.*;
import static mho.wheels.testing.Testing.aeq;

public class RealTest {
    private static void constant_helper(@NotNull Real input, @NotNull String output) {
        input.validate();
        aeq(input, output);
    }

    @Test
    public void testConstants() {
        constant_helper(ZERO, "0");
        constant_helper(ONE, "1");
        constant_helper(TEN, "10");
        constant_helper(TWO, "2");
        constant_helper(NEGATIVE_ONE, "-1");
        constant_helper(ONE_HALF, "0.5");
        constant_helper(SQRT_TWO, "1.41421356237309504880...");
        constant_helper(PHI, "1.61803398874989484820...");
        constant_helper(E, "2.71828182845904523536...");
        //todo fix constant_helper(PI, "3.14159265358979323846...");
    }
}
