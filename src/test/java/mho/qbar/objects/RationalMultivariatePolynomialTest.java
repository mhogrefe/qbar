package mho.qbar.objects;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static mho.qbar.objects.RationalMultivariatePolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

public class RationalMultivariatePolynomialTest {
    @Test
    public void testConstants() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
    }

    private static void iterable_helper(@NotNull String p, @NotNull String mo, @NotNull String output) {
        aeq(toList(readStrict(p).get().iterable(MonomialOrder.readStrict(mo).get())), output);
    }

    @Test
    public void testIterable() {
        iterable_helper("0", "LEX", "[]");
        iterable_helper("0", "GRLEX", "[]");
        iterable_helper("0", "GREVLEX", "[]");

        iterable_helper("1", "LEX", "[(1, 1)]");
        iterable_helper("1", "GRLEX", "[(1, 1)]");
        iterable_helper("1", "GREVLEX", "[(1, 1)]");

        iterable_helper("-4/3", "LEX", "[(1, -4/3)]");
        iterable_helper("-4/3", "GRLEX", "[(1, -4/3)]");
        iterable_helper("-4/3", "GREVLEX", "[(1, -4/3)]");

        iterable_helper("ooo", "LEX", "[(ooo, 1)]");
        iterable_helper("ooo", "GRLEX", "[(ooo, 1)]");
        iterable_helper("ooo", "GREVLEX", "[(ooo, 1)]");

        iterable_helper("a*b*c", "LEX", "[(a*b*c, 1)]");
        iterable_helper("a*b*c", "GRLEX", "[(a*b*c, 1)]");
        iterable_helper("a*b*c", "GREVLEX", "[(a*b*c, 1)]");

        iterable_helper("x^2-7/4*x+1/3", "LEX", "[(1, 1/3), (x, -7/4), (x^2, 1)]");
        iterable_helper("x^2-7/4*x+1/3", "GRLEX", "[(1, 1/3), (x, -7/4), (x^2, 1)]");
        iterable_helper("x^2-7/4*x+1/3", "GREVLEX", "[(1, 1/3), (x, -7/4), (x^2, 1)]");

        iterable_helper("x^2+1/2*x*y+y^2", "LEX", "[(y^2, 1), (x*y, 1/2), (x^2, 1)]");
        iterable_helper("x^2+1/2*x*y+y^2", "GRLEX", "[(y^2, 1), (x*y, 1/2), (x^2, 1)]");
        iterable_helper("x^2+1/2*x*y+y^2", "GREVLEX", "[(y^2, 1), (x*y, 1/2), (x^2, 1)]");

        iterable_helper("a+b+c+d+e+f", "LEX", "[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]");
        iterable_helper("a+b+c+d+e+f", "GRLEX", "[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]");
        iterable_helper("a+b+c+d+e+f", "GREVLEX", "[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]");

        iterable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "LEX", "[(z^2, 22/7), (x*y^2*z, 1), (x^2*z^2, 1), (x^3, 1)]");
        iterable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "GRLEX",
                "[(z^2, 22/7), (x^3, 1), (x*y^2*z, 1), (x^2*z^2, 1)]");
        iterable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "GREVLEX",
                "[(z^2, 22/7), (x^3, 1), (x^2*z^2, 1), (x*y^2*z, 1)]");
    }

    private static void iterator_helper(@NotNull String x, @NotNull String output) {
        aeq(toList(readStrict(x).get()), output);
    }

    @Test
    public void testIterator() {
        iterator_helper("0", "[]");
        iterator_helper("1", "[(1, 1)]");
        iterator_helper("-4/3", "[(1, -4/3)]");
        iterator_helper("ooo", "[(ooo, 1)]");
        iterator_helper("a*b*c", "[(a*b*c, 1)]");
        iterator_helper("x^2-7/4*x+1/3", "[(1, 1/3), (x, -7/4), (x^2, 1)]");
        iterator_helper("x^2+1/2*x*y+y^2", "[(y^2, 1), (x*y, 1/2), (x^2, 1)]");
        iterator_helper("a+b+c+d+e+f", "[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]");
        iterator_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[(z^2, 22/7), (x^3, 1), (x^2*z^2, 1), (x*y^2*z, 1)]");
    }
}
