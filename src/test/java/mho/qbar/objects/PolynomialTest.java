package mho.qbar.objects;

import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.Polynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PolynomialTest {
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
        aeq(toList(read("-17").get()), "[-17]");
        aeq(toList(read("x^2-4*x+7").get()), "[7, -4, 1]");
        aeq(toList(read("x^3-1").get()), "[-1, 0, 0, 1]");
        aeq(toList(read("3*x^10").get()), "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3]");
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
        aeq(read("x^2-4*x+7").get().coefficient(0), 7);
        aeq(read("x^2-4*x+7").get().coefficient(1), -4);
        aeq(read("x^2-4*x+7").get().coefficient(2), 1);
        aeq(read("x^2-4*x+7").get().coefficient(3), 0);
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
    public void testOf_List_BigInteger() {
        assertTrue(of(readBigIntegerList("[]").get()) == ZERO);
        assertTrue(of(readBigIntegerList("[0]").get()) == ZERO);
        assertTrue(of(readBigIntegerList("[0, 0, 0]").get()) == ZERO);
        assertTrue(of(readBigIntegerList("[1]").get()) == ONE);
        assertTrue(of(readBigIntegerList("[1, 0, 0]").get()) == ONE);
        aeq(of(readBigIntegerList("[0, 1]").get()), X);
        aeq(of(readBigIntegerList("[-17]").get()), "-17");
        aeq(of(readBigIntegerList("[7, -4, 1]").get()), "x^2-4*x+7");
        aeq(of(readBigIntegerList("[7, -4, 1, 0, 0]").get()), "x^2-4*x+7");
        aeq(of(readBigIntegerList("[-1, 0, 0, 1]").get()), "x^3-1");
        aeq(of(readBigIntegerList("[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3]").get()), "3*x^10");
        try {
            of(readBigIntegerListWithNulls("[7, null, 1]").get());
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testOf_BigInteger() {
        assertTrue(of(BigInteger.ZERO) == ZERO);
        assertTrue(of(BigInteger.ONE) == ONE);
        aeq(of(BigInteger.valueOf(5)), 5);
        aeq(of(BigInteger.valueOf(-7)), -7);
    }

    @Test
    public void testOf_BigInteger_int() {
        assertTrue(of(BigInteger.ZERO, 0) == ZERO);
        assertTrue(of(BigInteger.ZERO, 1) == ZERO);
        assertTrue(of(BigInteger.ZERO, 2) == ZERO);
        assertTrue(of(BigInteger.ZERO, 3) == ZERO);
        assertTrue(of(BigInteger.ONE, 0) == ONE);
        aeq(of(BigInteger.ONE, 1), "x");
        aeq(of(BigInteger.ONE, 2), "x^2");
        aeq(of(BigInteger.ONE, 3), "x^3");
        aeq(of(BigInteger.valueOf(-1), 0), "-1");
        aeq(of(BigInteger.valueOf(-1), 1), "-x");
        aeq(of(BigInteger.valueOf(-1), 2), "-x^2");
        aeq(of(BigInteger.valueOf(-1), 3), "-x^3");
        aeq(of(BigInteger.valueOf(3), 0), "3");
        aeq(of(BigInteger.valueOf(3), 1), "3*x");
        aeq(of(BigInteger.valueOf(3), 2), "3*x^2");
        aeq(of(BigInteger.valueOf(3), 3), "3*x^3");
        aeq(of(BigInteger.valueOf(-5), 0), "-5");
        aeq(of(BigInteger.valueOf(-5), 1), "-5*x");
        aeq(of(BigInteger.valueOf(-5), 2), "-5*x^2");
        aeq(of(BigInteger.valueOf(-5), 3), "-5*x^3");
    }

    private static void aeq(Object a, Object b) {
        assertEquals(a.toString(), b.toString());
    }

    private static @NotNull Optional<List<BigInteger>> readBigIntegerList(@NotNull String s) {
        return Readers.readList(Readers::readBigInteger).apply(s);
    }

    private static @NotNull Optional<List<BigInteger>> readBigIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Readers::readBigInteger).apply(s);
    }
}
