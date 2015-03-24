package mho.qbar.objects;

import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.Polynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.*;

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
        try {
            of(BigInteger.valueOf(-5), -1);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testDegree() {
        aeq(ZERO.degree(), -1);
        aeq(ONE.degree(), 0);
        aeq(X.degree(), 1);
        aeq(read("-17").get().degree(), 0);
        aeq(read("x^2-4*x+7").get().degree(), 2);
        aeq(read("x^3-1").get().degree(), 3);
        aeq(read("3*x^10").get().degree(), 10);
    }

    @Test
    public void testLeading() {
        assertFalse(ZERO.leading().isPresent());
        aeq(ONE.leading().get(), 1);
        aeq(X.leading().get(), 1);
        aeq(read("-17").get().leading().get(), -17);
        aeq(read("x^2-4*x+7").get().leading().get(), 1);
        aeq(read("-x^3-1").get().leading().get(), -1);
        aeq(read("3*x^10").get().leading().get(), 3);
    }

    @Test
    public void testNegate() {
        assertTrue(ZERO.negate() == ZERO);
        aeq(ONE.negate(), "-1");
        aeq(X.negate(), "-x");
        aeq(read("-17").get().negate(), "17");
        aeq(read("x^2-4*x+7").get().negate(), "-x^2+4*x-7");
        aeq(read("-x^3-1").get().negate(), "x^3+1");
        aeq(read("3*x^10").get().negate(), "-3*x^10");
    }

    @Test
    public void testAbs() {
        assertTrue(ZERO.abs() == ZERO);
        assertTrue(ONE.abs() == ONE);
        aeq(X.abs(), "x");
        aeq(read("-17").get().abs(), "17");
        aeq(read("x^2-4*x+7").get().abs(), "x^2-4*x+7");
        aeq(read("-x^3-1").get().abs(), "x^3+1");
        aeq(read("3*x^10").get().abs(), "3*x^10");
    }

    @Test
    public void testSignum() {
        aeq(ZERO.signum(), 0);
        aeq(ONE.signum(), 1);
        aeq(X.signum(), 1);
        aeq(read("-17").get().signum(), -1);
        aeq(read("x^2-4*x+7").get().signum(), 1);
        aeq(read("-x^3-1").get().signum(), -1);
        aeq(read("3*x^10").get().signum(), 1);
    }

    @Test
    public void testEquals() {
        //noinspection EqualsWithItself
        assertTrue(ZERO.equals(ZERO));
        //noinspection EqualsWithItself
        assertTrue(ONE.equals(ONE));
        //noinspection EqualsWithItself
        assertTrue(X.equals(X));
        assertTrue(read("-17").get().equals(read("-17").get()));
        assertTrue(read("x^2-4*x+7").get().equals(read("x^2-4*x+7").get()));
        assertTrue(read("-x^3-1").get().equals(read("-x^3-1").get()));
        assertTrue(read("3*x^10").get().equals(read("3*x^10").get()));
        assertFalse(ZERO.equals(ONE));
        assertFalse(ZERO.equals(X));
        assertFalse(ONE.equals(ZERO));
        assertFalse(ONE.equals(X));
        assertFalse(X.equals(ZERO));
        assertFalse(X.equals(ONE));
        assertFalse(ZERO.equals(read("-17").get()));
        assertFalse(ZERO.equals(read("x^2-4*x+7").get()));
        assertFalse(ZERO.equals(read("-x^3-1").get()));
        assertFalse(ZERO.equals(read("3*x^10").get()));
        assertFalse(ONE.equals(read("-17").get()));
        assertFalse(ONE.equals(read("x^2-4*x+7").get()));
        assertFalse(ONE.equals(read("-x^3-1").get()));
        assertFalse(ONE.equals(read("3*x^10").get()));
        assertFalse(X.equals(read("-17").get()));
        assertFalse(X.equals(read("x^2-4*x+7").get()));
        assertFalse(X.equals(read("-x^3-1").get()));
        assertFalse(X.equals(read("3*x^10").get()));
        assertFalse(read("-17").get().equals(ZERO));
        assertFalse(read("x^2-4*x+7").get().equals(ZERO));
        assertFalse(read("-x^3-1").get().equals(ZERO));
        assertFalse(read("3*x^10").get().equals(ZERO));
        assertFalse(read("-17").get().equals(ONE));
        assertFalse(read("x^2-4*x+7").get().equals(ONE));
        assertFalse(read("-x^3-1").get().equals(ONE));
        assertFalse(read("3*x^10").get().equals(ONE));
        assertFalse(read("-17").get().equals(X));
        assertFalse(read("x^2-4*x+7").get().equals(X));
        assertFalse(read("-x^3-1").get().equals(X));
        assertFalse(read("3*x^10").get().equals(X));
        assertFalse(read("-17").equals(read("x^2-4*x+7")));
        assertFalse(read("-17").equals(read("-x^3-1")));
        assertFalse(read("-17").equals(read("3*x^10")));
        assertFalse(read("x^2-4*x+7").equals(read("-17")));
        assertFalse(read("x^2-4*x+7").equals(read("-x^3-1")));
        assertFalse(read("x^2-4*x+7").equals(read("3*x^10")));
        assertFalse(read("-x^3-1").equals(read("-17")));
        assertFalse(read("-x^3-1").equals(read("x^2-4*x+7")));
        assertFalse(read("-x^3-1").equals(read("3*x^10")));
        assertFalse(read("3*x^10").equals(read("-17")));
        assertFalse(read("3*x^10").equals(read("x^2-4*x+7")));
        assertFalse(read("3*x^10").equals(read("-x^3-1")));
    }

    @Test
    public void testHashCode() {
        aeq(ZERO.hashCode(), 1);
        aeq(ONE.hashCode(), 32);
        aeq(X.hashCode(), 962);
        aeq(read("-17").get().hashCode(), 14);
        aeq(read("x^2-4*x+7").get().hashCode(), 36395);
        aeq(read("-x^3-1").get().hashCode(), 893729);
        aeq(read("3*x^10").get().hashCode(), 129082722);
    }

    @Test
    public void testCompareTo() {
        aeq(ZERO.compareTo(ZERO), 0);
        aeq(ZERO.compareTo(ONE), -1);
        aeq(ZERO.compareTo(X), -1);
        aeq(ZERO.compareTo(read("-17").get()), 1);
        aeq(ZERO.compareTo(read("x^2-4*x+7").get()), -1);
        aeq(ZERO.compareTo(read("-x^3-1").get()), 1);
        aeq(ZERO.compareTo(read("3*x^10").get()), -1);
        aeq(ONE.compareTo(ZERO), 1);
        aeq(ONE.compareTo(ONE), 0);
        aeq(ONE.compareTo(X), -1);
        aeq(ONE.compareTo(read("-17").get()), 1);
        aeq(ONE.compareTo(read("x^2-4*x+7").get()), -1);
        aeq(ONE.compareTo(read("-x^3-1").get()), 1);
        aeq(ONE.compareTo(read("3*x^10").get()), -1);
        aeq(X.compareTo(ZERO), 1);
        aeq(X.compareTo(ONE), 1);
        aeq(X.compareTo(X), 0);
        aeq(X.compareTo(read("-17").get()), 1);
        aeq(X.compareTo(read("x^2-4*x+7").get()), -1);
        aeq(X.compareTo(read("-x^3-1").get()), 1);
        aeq(X.compareTo(read("3*x^10").get()), -1);
        aeq(read("-17").get().compareTo(ZERO), -1);
        aeq(read("-17").get().compareTo(ONE), -1);
        aeq(read("-17").get().compareTo(X), -1);
        aeq(read("-17").get().compareTo(read("-17").get()), 0);
        aeq(read("-17").get().compareTo(read("x^2-4*x+7").get()), -1);
        aeq(read("-17").get().compareTo(read("-x^3-1").get()), 1);
        aeq(read("-17").get().compareTo(read("3*x^10").get()), -1);
        aeq(read("x^2-4*x+7").get().compareTo(ZERO), 1);
        aeq(read("x^2-4*x+7").get().compareTo(ONE), 1);
        aeq(read("x^2-4*x+7").get().compareTo(X), 1);
        aeq(read("x^2-4*x+7").get().compareTo(read("-17").get()), 1);
        aeq(read("x^2-4*x+7").get().compareTo(read("x^2-4*x+7").get()), 0);
        aeq(read("x^2-4*x+7").get().compareTo(read("-x^3-1").get()), 1);
        aeq(read("x^2-4*x+7").get().compareTo(read("3*x^10").get()), -1);
        aeq(read("-x^3-1").get().compareTo(ZERO), -1);
        aeq(read("-x^3-1").get().compareTo(ONE), -1);
        aeq(read("-x^3-1").get().compareTo(X), -1);
        aeq(read("-x^3-1").get().compareTo(read("-17").get()), -1);
        aeq(read("-x^3-1").get().compareTo(read("x^2-4*x+7").get()), -1);
        aeq(read("-x^3-1").get().compareTo(read("-x^3-1").get()), 0);
        aeq(read("-x^3-1").get().compareTo(read("3*x^10").get()), -1);
        aeq(read("3*x^10").get().compareTo(ZERO), 1);
        aeq(read("3*x^10").get().compareTo(ONE), 1);
        aeq(read("3*x^10").get().compareTo(X), 1);
        aeq(read("3*x^10").get().compareTo(read("-17").get()), 1);
        aeq(read("3*x^10").get().compareTo(read("x^2-4*x+7").get()), 1);
        aeq(read("3*x^10").get().compareTo(read("-x^3-1").get()), 1);
        aeq(read("3*x^10").get().compareTo(read("3*x^10").get()), 0);
    }

    @Test
    public void testRead() {
        assertTrue(read("0").get() == ZERO);
        assertTrue(read("1").get() == ONE);
        aeq(read("x").get(), X);
        aeq(read("2").get(), "2");
        aeq(read("-2").get(), "-2");
        aeq(read("-x").get(), "-x");
        aeq(read("3*x").get(), "3*x");
        aeq(read("-3*x").get(), "-3*x");
        aeq(read("x^2").get(), "x^2");
        aeq(read("-x^2").get(), "-x^2");
        aeq(read("2*x^2").get(), "2*x^2");
        aeq(read("-2*x^2").get(), "-2*x^2");
        aeq(read("x-1").get(), "x-1");
        aeq(read("-x-1").get(), "-x-1");
        aeq(read("x^2-1").get(), "x^2-1");
        aeq(read("x^2-4*x+7").get(), "x^2-4*x+7");
        aeq(read("3*x^10").get(), "3*x^10");
        assertFalse(read("").isPresent());
        assertFalse(read("+").isPresent());
        assertFalse(read("-").isPresent());
        assertFalse(read("-0").isPresent());
        assertFalse(read("+0").isPresent());
        assertFalse(read("--").isPresent());
        assertFalse(read("+1").isPresent());
        assertFalse(read("+x").isPresent());
        assertFalse(read("+x^2").isPresent());
        assertFalse(read("+x^2-x").isPresent());
        assertFalse(read("x^1000000000000").isPresent());
        assertFalse(read(" x").isPresent());
        assertFalse(read("x ").isPresent());
        assertFalse(read("X").isPresent());
        assertFalse(read("x + 1").isPresent());
        assertFalse(read("x^0").isPresent());
        assertFalse(read("x^-1").isPresent());
        assertFalse(read("x^1").isPresent());
        assertFalse(read("1-2").isPresent());
        assertFalse(read("1*x").isPresent());
        assertFalse(read("-1*x").isPresent());
        assertFalse(read("1*x^2").isPresent());
        assertFalse(read("-1*x^2").isPresent());
        assertFalse(read("x+x").isPresent());
        assertFalse(read("x+x^2").isPresent());
        assertFalse(read("1+x").isPresent());
        assertFalse(read("x+0").isPresent());
        assertFalse(read("0*x").isPresent());
        assertFalse(read("-0*x").isPresent());
        assertFalse(read("+0*x").isPresent());
        assertFalse(read("2x").isPresent());
        assertFalse(read("x^2+1+x").isPresent());
        assertFalse(read("x^2+3*x^2").isPresent());
        assertFalse(read("2^x").isPresent());
        assertFalse(read("abc").isPresent());
        assertFalse(read("x+y").isPresent());
        assertFalse(read("y").isPresent());
        assertFalse(read("1/2").isPresent());
        assertFalse(read("x/2").isPresent());
    }

    @Test
    public void testFindIn() {
        aeq(findIn("0123").get(), "(0, 0)");
        aeq(findIn("yxy").get(), "(x, 1)");
        aeq(findIn("ax+12b").get(), "(x+12, 1)");
        aeq(findIn("------x------").get(), "(-x, 5)");
        aeq(findIn("3*x^2z").get(), "(3*x^2, 0)");
        aeq(findIn("1+x+x^2").get(), "(1, 0)");
        aeq(findIn("+1").get(), "(1, 1)");
        aeq(findIn("y^12").get(), "(12, 2)");
        aeq(findIn("52*x^-10").get(), "(52*x, 0)");
        assertFalse(findIn("").isPresent());
        assertFalse(findIn("o").isPresent());
        assertFalse(findIn("hello").isPresent());
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
