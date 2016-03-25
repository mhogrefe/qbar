package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.MonomialOrder.*;
import static mho.wheels.testing.Testing.aeq;
import static org.junit.Assert.assertFalse;

public class MonomialOrderTest {
    private static void compare_helper(@NotNull MonomialOrder order, @NotNull String input) {
        List<ExponentVector> evs = readExponentVectorList(input);
        for (int i = 0; i < evs.size(); i++) {
            ExponentVector evi = evs.get(i);
            for (int j = 0; j < evs.size(); j++) {
                ExponentVector evj = evs.get(j);
                aeq(new Pair<>(evi, evj), Integer.compare(i, j), order.compare(evi, evj));
            }
        }
    }

    @Test
    public void testCompare() {
        compare_helper(LEX, "[1, ooo, z^2, y^2, x*y, x*y^2, x*y^2*z, x^2, x^2*z^2, x^2*y, x^3, b, a, a*b*c*d]");
        compare_helper(GRLEX, "[1, ooo, b, a, z^2, y^2, x*y, x^2, x*y^2, x^2*y, x^3, x*y^2*z, x^2*z^2, a*b*c*d]");
        compare_helper(GREVLEX, "[1, ooo, b, a, z^2, y^2, x*y, x^2, x*y^2, x^2*y, x^3, x^2*z^2, x*y^2*z, a*b*c*d]");
    }

    private static void read_helper(@NotNull String input) {
        aeq(read(input).get(), input);
    }

    private static void read_fail_helper(@NotNull String input) {
        assertFalse(read(input).isPresent());
    }

    @Test
    public void testRead() {
        read_helper("LEX");
        read_helper("GRLEX");
        read_helper("GREVLEX");
        read_fail_helper("");
        read_fail_helper("1");
        read_fail_helper(" ");
        read_fail_helper("ab");
    }

    private static void findIn_helper(@NotNull String input, @NotNull String output, int index) {
        Pair<MonomialOrder, Integer> result = findIn(input).get();
        aeq(result.a, output);
        aeq(result.b, index);
    }

    private static void findIn_fail_helper(@NotNull String input) {
        assertFalse(findIn(input).isPresent());
    }

    @Test
    public void testFindIn() {
        findIn_helper("ROLEX", "LEX", 2);
        findIn_helper("09rfj0woGREVLEXMOIHU)O", "GREVLEX", 8);
        findIn_helper("GR LEX", "LEX", 3);
        findIn_fail_helper("");
        findIn_fail_helper("abc");
        findIn_fail_helper("grlex");
    }

    private static @NotNull List<ExponentVector> readExponentVectorList(@NotNull String s) {
        return Readers.readList(ExponentVector::read).apply(s).get();
    }
}
