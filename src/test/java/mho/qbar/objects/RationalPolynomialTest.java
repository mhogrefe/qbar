package mho.qbar.objects;

import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.*;

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

    @Test
    public void testOf_Rational() {
        assertTrue(of(Rational.ZERO) == ZERO);
        assertTrue(of(Rational.ONE) == ONE);
        aeq(of(Rational.read("5/3").get()), "5/3");
        aeq(of(Rational.read("-1/7").get()), "-1/7");
    }

    @Test
    public void testOf_Rational_int() {
        assertTrue(of(Rational.ZERO, 0) == ZERO);
        assertTrue(of(Rational.ZERO, 1) == ZERO);
        assertTrue(of(Rational.ZERO, 2) == ZERO);
        assertTrue(of(Rational.ZERO, 3) == ZERO);
        assertTrue(of(Rational.ONE, 0) == ONE);
        aeq(of(Rational.ONE, 1), "x");
        aeq(of(Rational.ONE, 2), "x^2");
        aeq(of(Rational.ONE, 3), "x^3");
        aeq(of(Rational.read("-1").get(), 0), "-1");
        aeq(of(Rational.read("-1").get(), 1), "-x");
        aeq(of(Rational.read("-1").get(), 2), "-x^2");
        aeq(of(Rational.read("-1").get(), 3), "-x^3");
        aeq(of(Rational.read("3/2").get(), 0), "3/2");
        aeq(of(Rational.read("3/2").get(), 1), "3/2*x");
        aeq(of(Rational.read("3/2").get(), 2), "3/2*x^2");
        aeq(of(Rational.read("3/2").get(), 3), "3/2*x^3");
        aeq(of(Rational.read("-5/7").get(), 0), "-5/7");
        aeq(of(Rational.read("-5/7").get(), 1), "-5/7*x");
        aeq(of(Rational.read("-5/7").get(), 2), "-5/7*x^2");
        aeq(of(Rational.read("-5/7").get(), 3), "-5/7*x^3");
        try {
            of(Rational.read("-5/7").get(), -1);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testDegree() {
        aeq(ZERO.degree(), -1);
        aeq(ONE.degree(), 0);
        aeq(X.degree(), 1);
        aeq(read("-4/3").get().degree(), 0);
        aeq(read("x^2-7/4*x+1/3").get().degree(), 2);
        aeq(read("x^3-1").get().degree(), 3);
        aeq(read("1/2*x^10").get().degree(), 10);
    }

    @Test
    public void testLeading() {
        assertFalse(ZERO.leading().isPresent());
        aeq(ONE.leading().get(), 1);
        aeq(X.leading().get(), 1);
        aeq(read("-4/3").get().leading().get(), "-4/3");
        aeq(read("x^2-7/4*x+1/3").get().leading().get(), 1);
        aeq(read("-x^3-1").get().leading().get(), -1);
        aeq(read("1/2*x^10").get().leading().get(), "1/2");
    }

    @Test
    public void testSignum() {
        aeq(ZERO.signum(), 0);
        aeq(ONE.signum(), 1);
        aeq(X.signum(), 1);
        aeq(read("-4/3").get().signum(), -1);
        aeq(read("x^2-7/4*x+1/3").get().signum(), 1);
        aeq(read("-x^3-1").get().signum(), -1);
        aeq(read("1/2*x^10").get().signum(), 1);
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