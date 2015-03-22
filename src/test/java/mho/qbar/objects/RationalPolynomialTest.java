package mho.qbar.objects;

import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RationalPolynomialTest {
    @Test
    public void testConstants() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
        aeq(X, "x");
    }

    @Test
    public void testIterator() {
        aeq(toList(ZERO), "[]");
        aeq(toList(ONE), "[1]");
        aeq(toList(X), "[0, 1]");
        aeq(toList(read("-4/3").get()), "[-4/3]");
        aeq(toList(read("x^2-7/4*x+1/3").get()), "[1/3, -7/4, 1]");
        aeq(toList(read("x^3-1").get()), "[-1, 0, 0, 1]");
        aeq(toList(read("1/2*x^10").get()), "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1/2]");
    }

    @Test
    public void testCoefficient() {
        aeq(ZERO.coefficient(0), 0);
        aeq(ZERO.coefficient(5), 0);
        aeq(ONE.coefficient(0), 1);
        aeq(ONE.coefficient(5), 0);
        aeq(X.coefficient(0), 0);
        aeq(X.coefficient(1), 1);
        aeq(X.coefficient(5), 0);
        aeq(read("x^2-7/4*x+1/3").get().coefficient(0), "1/3");
        aeq(read("x^2-7/4*x+1/3").get().coefficient(1), "-7/4");
        aeq(read("x^2-7/4*x+1/3").get().coefficient(2), 1);
        aeq(read("x^2-7/4*x+1/3").get().coefficient(3), 0);
        aeq(read("x^3-1").get().coefficient(0), -1);
        aeq(read("x^3-1").get().coefficient(1), 0);
        aeq(read("x^3-1").get().coefficient(2), 0);
        aeq(read("x^3-1").get().coefficient(3), 1);
        aeq(read("x^3-1").get().coefficient(4), 0);
        try {
            read("x^3-1").get().coefficient(-1);
            fail();
        } catch (ArrayIndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testOf_List_Rational() {
        assertTrue(of(readRationalList("[]").get()) == ZERO);
        assertTrue(of(readRationalList("[0]").get()) == ZERO);
        assertTrue(of(readRationalList("[0, 0, 0]").get()) == ZERO);
        assertTrue(of(readRationalList("[1]").get()) == ONE);
        assertTrue(of(readRationalList("[1, 0, 0]").get()) == ONE);
        aeq(of(readRationalList("[0, 1]").get()), X);
        aeq(of(readRationalList("[-4/3]").get()), "-4/3");
        aeq(of(readRationalList("[1/3, -7/4, 1]").get()), "x^2-7/4*x+1/3");
        aeq(of(readRationalList("[1/3, -7/4, 1, 0]").get()), "x^2-7/4*x+1/3");
        aeq(of(readRationalList("[-1, 0, 0, 1]").get()), "x^3-1");
        aeq(of(readRationalList("[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1/2]").get()), "1/2*x^10");
        try {
            of(readRationalListWithNulls("[1/3, null, 1]").get());
            fail();
        } catch (NullPointerException ignored) {}
    }

    private static void aeq(Object a, Object b) {
        assertEquals(a.toString(), b.toString());
    }

    private static @NotNull Optional<List<Rational>> readRationalList(@NotNull String s) {
        return Readers.readList(Rational::read).apply(s);
    }

    private static @NotNull Optional<List<Rational>> readRationalListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Rational::read).apply(s);
    }
}