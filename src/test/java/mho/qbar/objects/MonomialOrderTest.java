package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.MonomialOrder.*;
import static mho.wheels.testing.Testing.aeq;

public class MonomialOrderTest {
    private static void compare_helper(@NotNull MonomialOrder order, @NotNull String input) {
        List<Monomial> evs = readMonomialList(input);
        for (int i = 0; i < evs.size(); i++) {
            Monomial evi = evs.get(i);
            for (int j = 0; j < evs.size(); j++) {
                Monomial evj = evs.get(j);
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

    private static void readStrict_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input), output);
    }

    @Test
    public void testReadStrict() {
        readStrict_helper("LEX", "Optional[LEX]");
        readStrict_helper("GRLEX", "Optional[GRLEX]");
        readStrict_helper("GREVLEX", "Optional[GREVLEX]");

        readStrict_helper("", "Optional.empty");
        readStrict_helper("1", "Optional.empty");
        readStrict_helper(" ", "Optional.empty");
        readStrict_helper("ab", "Optional.empty");
    }

    private static @NotNull List<Monomial> readMonomialList(@NotNull String s) {
        return Readers.readListStrict(Monomial::readStrict).apply(s).get();
    }
}
