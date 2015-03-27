package mho.qbar.objects;

import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
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
    public void testApply() {
        aeq(ZERO.apply(BigInteger.ZERO), 0);
        aeq(ZERO.apply(BigInteger.ONE), 0);
        aeq(ZERO.apply(BigInteger.valueOf(-1)), 0);
        aeq(ZERO.apply(BigInteger.valueOf(5)), 0);
        aeq(ZERO.apply(BigInteger.valueOf(100)), 0);
        aeq(ONE.apply(BigInteger.ZERO), 1);
        aeq(ONE.apply(BigInteger.ONE), 1);
        aeq(ONE.apply(BigInteger.valueOf(-1)), 1);
        aeq(ONE.apply(BigInteger.valueOf(5)), 1);
        aeq(ONE.apply(BigInteger.valueOf(100)), 1);
        aeq(X.apply(BigInteger.ZERO), 0);
        aeq(X.apply(BigInteger.ONE), 1);
        aeq(X.apply(BigInteger.valueOf(-1)), -1);
        aeq(X.apply(BigInteger.valueOf(5)), 5);
        aeq(X.apply(BigInteger.valueOf(100)), 100);
        aeq(read("-17").get().apply(BigInteger.ZERO), -17);
        aeq(read("-17").get().apply(BigInteger.ONE), -17);
        aeq(read("-17").get().apply(BigInteger.valueOf(-1)), -17);
        aeq(read("-17").get().apply(BigInteger.valueOf(5)), -17);
        aeq(read("-17").get().apply(BigInteger.valueOf(100)), -17);
        aeq(read("x^2-4*x+7").get().apply(BigInteger.ZERO), 7);
        aeq(read("x^2-4*x+7").get().apply(BigInteger.ONE), 4);
        aeq(read("x^2-4*x+7").get().apply(BigInteger.valueOf(-1)), 12);
        aeq(read("x^2-4*x+7").get().apply(BigInteger.valueOf(5)), 12);
        aeq(read("x^2-4*x+7").get().apply(BigInteger.valueOf(100)), 9607);
        aeq(read("x^3-1").get().apply(BigInteger.ZERO), -1);
        aeq(read("x^3-1").get().apply(BigInteger.ONE), 0);
        aeq(read("x^3-1").get().apply(BigInteger.valueOf(-1)), -2);
        aeq(read("x^3-1").get().apply(BigInteger.valueOf(5)), 124);
        aeq(read("x^3-1").get().apply(BigInteger.valueOf(100)), 999999);
        aeq(read("3*x^10").get().apply(BigInteger.ZERO), 0);
        aeq(read("3*x^10").get().apply(BigInteger.ONE), 3);
        aeq(read("3*x^10").get().apply(BigInteger.valueOf(-1)), 3);
        aeq(read("3*x^10").get().apply(BigInteger.valueOf(5)), 29296875);
        aeq(read("3*x^10").get().apply(BigInteger.valueOf(100)), "300000000000000000000");
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
    public void testAdd() {
        assertTrue(ZERO.add(ZERO) == ZERO);
        assertTrue(ZERO.add(ONE) == ONE);
        aeq(ZERO.add(X), "x");
        aeq(ZERO.add(read("-17").get()), "-17");
        aeq(ZERO.add(read("x^2-4*x+7").get()), "x^2-4*x+7");
        aeq(ZERO.add(read("-x^3-1").get()), "-x^3-1");
        aeq(ZERO.add(read("3*x^10").get()), "3*x^10");
        assertTrue(ONE.add(ZERO) == ONE);
        aeq(ONE.add(ONE), "2");
        aeq(ONE.add(X), "x+1");
        aeq(ONE.add(read("-17").get()), "-16");
        aeq(ONE.add(read("x^2-4*x+7").get()), "x^2-4*x+8");
        aeq(ONE.add(read("-x^3-1").get()), "-x^3");
        aeq(ONE.add(read("3*x^10").get()), "3*x^10+1");
        aeq(X.add(ZERO), "x");
        aeq(X.add(ONE), "x+1");
        aeq(X.add(X), "2*x");
        aeq(X.add(read("-17").get()), "x-17");
        aeq(X.add(read("x^2-4*x+7").get()), "x^2-3*x+7");
        aeq(X.add(read("-x^3-1").get()), "-x^3+x-1");
        aeq(X.add(read("3*x^10").get()), "3*x^10+x");
        aeq(read("-17").get().add(ZERO), "-17");
        aeq(read("-17").get().add(ONE), "-16");
        aeq(read("-17").get().add(X), "x-17");
        aeq(read("-17").get().add(read("-17").get()), "-34");
        aeq(read("-17").get().add(read("x^2-4*x+7").get()), "x^2-4*x-10");
        aeq(read("-17").get().add(read("-x^3-1").get()), "-x^3-18");
        aeq(read("-17").get().add(read("3*x^10").get()), "3*x^10-17");
        aeq(read("x^2-4*x+7").get().add(ZERO), "x^2-4*x+7");
        aeq(read("x^2-4*x+7").get().add(ONE), "x^2-4*x+8");
        aeq(read("x^2-4*x+7").get().add(X), "x^2-3*x+7");
        aeq(read("x^2-4*x+7").get().add(read("-17").get()), "x^2-4*x-10");
        aeq(read("x^2-4*x+7").get().add(read("x^2-4*x+7").get()), "2*x^2-8*x+14");
        aeq(read("x^2-4*x+7").get().add(read("-x^3-1").get()), "-x^3+x^2-4*x+6");
        aeq(read("x^2-4*x+7").get().add(read("3*x^10").get()), "3*x^10+x^2-4*x+7");
        aeq(read("-x^3-1").get().add(ZERO), "-x^3-1");
        aeq(read("-x^3-1").get().add(ONE), "-x^3");
        aeq(read("-x^3-1").get().add(X), "-x^3+x-1");
        aeq(read("-x^3-1").get().add(read("-17").get()), "-x^3-18");
        aeq(read("-x^3-1").get().add(read("x^2-4*x+7").get()), "-x^3+x^2-4*x+6");
        aeq(read("-x^3-1").get().add(read("-x^3-1").get()), "-2*x^3-2");
        aeq(read("-x^3-1").get().add(read("3*x^10").get()), "3*x^10-x^3-1");
        aeq(read("3*x^10").get().add(ZERO), "3*x^10");
        aeq(read("3*x^10").get().add(ONE), "3*x^10+1");
        aeq(read("3*x^10").get().add(X), "3*x^10+x");
        aeq(read("3*x^10").get().add(read("-17").get()), "3*x^10-17");
        aeq(read("3*x^10").get().add(read("x^2-4*x+7").get()), "3*x^10+x^2-4*x+7");
        aeq(read("3*x^10").get().add(read("-x^3-1").get()), "3*x^10-x^3-1");
        aeq(read("3*x^10").get().add(read("3*x^10").get()), "6*x^10");
        assertTrue(read("x^2-4*x+7").get().add(read("-x^2+4*x-7").get()) == ZERO);
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
    public void testSubtract() {
        assertTrue(ZERO.subtract(ZERO) == ZERO);
        aeq(ZERO.subtract(ONE), "-1");
        aeq(ZERO.subtract(X), "-x");
        aeq(ZERO.subtract(read("-17").get()), "17");
        aeq(ZERO.subtract(read("x^2-4*x+7").get()), "-x^2+4*x-7");
        aeq(ZERO.subtract(read("-x^3-1").get()), "x^3+1");
        aeq(ZERO.subtract(read("3*x^10").get()), "-3*x^10");
        assertTrue(ONE.subtract(ZERO) == ONE);
        assertTrue(ONE.subtract(ONE) == ZERO);
        aeq(ONE.subtract(X), "-x+1");
        aeq(ONE.subtract(read("-17").get()), "18");
        aeq(ONE.subtract(read("x^2-4*x+7").get()), "-x^2+4*x-6");
        aeq(ONE.subtract(read("-x^3-1").get()), "x^3+2");
        aeq(ONE.subtract(read("3*x^10").get()), "-3*x^10+1");
        aeq(X.subtract(ZERO), "x");
        aeq(X.subtract(ONE), "x-1");
        assertTrue(X.subtract(X) == ZERO);
        aeq(X.subtract(read("-17").get()), "x+17");
        aeq(X.subtract(read("x^2-4*x+7").get()), "-x^2+5*x-7");
        aeq(X.subtract(read("-x^3-1").get()), "x^3+x+1");
        aeq(X.subtract(read("3*x^10").get()), "-3*x^10+x");
        aeq(read("-17").get().subtract(ZERO), "-17");
        aeq(read("-17").get().subtract(ONE), "-18");
        aeq(read("-17").get().subtract(X), "-x-17");
        assertTrue(read("-17").get().subtract(read("-17").get()) == ZERO);
        aeq(read("-17").get().subtract(read("x^2-4*x+7").get()), "-x^2+4*x-24");
        aeq(read("-17").get().subtract(read("-x^3-1").get()), "x^3-16");
        aeq(read("-17").get().subtract(read("3*x^10").get()), "-3*x^10-17");
        aeq(read("x^2-4*x+7").get().subtract(ZERO), "x^2-4*x+7");
        aeq(read("x^2-4*x+7").get().subtract(ONE), "x^2-4*x+6");
        aeq(read("x^2-4*x+7").get().subtract(X), "x^2-5*x+7");
        aeq(read("x^2-4*x+7").get().subtract(read("-17").get()), "x^2-4*x+24");
        assertTrue(read("x^2-4*x+7").get().subtract(read("x^2-4*x+7").get()) == ZERO);
        aeq(read("x^2-4*x+7").get().subtract(read("-x^3-1").get()), "x^3+x^2-4*x+8");
        aeq(read("x^2-4*x+7").get().subtract(read("3*x^10").get()), "-3*x^10+x^2-4*x+7");
        aeq(read("-x^3-1").get().subtract(ZERO), "-x^3-1");
        aeq(read("-x^3-1").get().subtract(ONE), "-x^3-2");
        aeq(read("-x^3-1").get().subtract(X), "-x^3-x-1");
        aeq(read("-x^3-1").get().subtract(read("-17").get()), "-x^3+16");
        aeq(read("-x^3-1").get().subtract(read("x^2-4*x+7").get()), "-x^3-x^2+4*x-8");
        assertTrue(read("-x^3-1").get().subtract(read("-x^3-1").get()) == ZERO);
        aeq(read("-x^3-1").get().subtract(read("3*x^10").get()), "-3*x^10-x^3-1");
        aeq(read("3*x^10").get().subtract(ZERO), "3*x^10");
        aeq(read("3*x^10").get().subtract(ONE), "3*x^10-1");
        aeq(read("3*x^10").get().subtract(X), "3*x^10-x");
        aeq(read("3*x^10").get().subtract(read("-17").get()), "3*x^10+17");
        aeq(read("3*x^10").get().subtract(read("x^2-4*x+7").get()), "3*x^10-x^2+4*x-7");
        aeq(read("3*x^10").get().subtract(read("-x^3-1").get()), "3*x^10+x^3+1");
        assertTrue(read("3*x^10").get().subtract(read("3*x^10").get()) == ZERO);
    }

    @Test
    public void testMultiply_Polynomial() {
        assertTrue(ZERO.multiply(ZERO) == ZERO);
        assertTrue(ZERO.multiply(ONE) == ZERO);
        assertTrue(ZERO.multiply(X) == ZERO);
        assertTrue(ZERO.multiply(read("-17").get()) == ZERO);
        assertTrue(ZERO.multiply(read("x^2-4*x+7").get()) == ZERO);
        assertTrue(ZERO.multiply(read("-x^3-1").get()) == ZERO);
        assertTrue(ZERO.multiply(read("3*x^10").get()) == ZERO);
        assertTrue(ONE.multiply(ZERO) == ZERO);
        assertTrue(ONE.multiply(ONE) == ONE);
        aeq(ONE.multiply(X), "x");
        aeq(ONE.multiply(read("-17").get()), "-17");
        aeq(ONE.multiply(read("x^2-4*x+7").get()), "x^2-4*x+7");
        aeq(ONE.multiply(read("-x^3-1").get()), "-x^3-1");
        aeq(ONE.multiply(read("3*x^10").get()), "3*x^10");
        assertTrue(X.multiply(ZERO) == ZERO);
        aeq(X.multiply(ONE), "x");
        aeq(X.multiply(X), "x^2");
        aeq(X.multiply(read("-17").get()), "-17*x");
        aeq(X.multiply(read("x^2-4*x+7").get()), "x^3-4*x^2+7*x");
        aeq(X.multiply(read("-x^3-1").get()), "-x^4-x");
        aeq(X.multiply(read("3*x^10").get()), "3*x^11");
        assertTrue(read("-17").get().multiply(ZERO) == ZERO);
        aeq(read("-17").get().multiply(ONE), "-17");
        aeq(read("-17").get().multiply(X), "-17*x");
        aeq(read("-17").get().multiply(read("-17").get()), "289");
        aeq(read("-17").get().multiply(read("x^2-4*x+7").get()), "-17*x^2+68*x-119");
        aeq(read("-17").get().multiply(read("-x^3-1").get()), "17*x^3+17");
        aeq(read("-17").get().multiply(read("3*x^10").get()), "-51*x^10");
        assertTrue(read("x^2-4*x+7").get().multiply(ZERO) == ZERO);
        aeq(read("x^2-4*x+7").get().multiply(ONE), "x^2-4*x+7");
        aeq(read("x^2-4*x+7").get().multiply(X), "x^3-4*x^2+7*x");
        aeq(read("x^2-4*x+7").get().multiply(read("-17").get()), "-17*x^2+68*x-119");
        aeq(read("x^2-4*x+7").get().multiply(read("x^2-4*x+7").get()), "x^4-8*x^3+30*x^2-56*x+49");
        aeq(read("x^2-4*x+7").get().multiply(read("-x^3-1").get()), "-x^5+4*x^4-7*x^3-x^2+4*x-7");
        aeq(read("x^2-4*x+7").get().multiply(read("3*x^10").get()), "3*x^12-12*x^11+21*x^10");
        assertTrue(read("-x^3-1").get().multiply(ZERO) == ZERO);
        aeq(read("-x^3-1").get().multiply(ONE), "-x^3-1");
        aeq(read("-x^3-1").get().multiply(X), "-x^4-x");
        aeq(read("-x^3-1").get().multiply(read("-17").get()), "17*x^3+17");
        aeq(read("-x^3-1").get().multiply(read("x^2-4*x+7").get()), "-x^5+4*x^4-7*x^3-x^2+4*x-7");
        aeq(read("-x^3-1").get().multiply(read("-x^3-1").get()), "x^6+2*x^3+1");
        aeq(read("-x^3-1").get().multiply(read("3*x^10").get()), "-3*x^13-3*x^10");
        assertTrue(read("3*x^10").get().multiply(ZERO) == ZERO);
        aeq(read("3*x^10").get().multiply(ONE), "3*x^10");
        aeq(read("3*x^10").get().multiply(X), "3*x^11");
        aeq(read("3*x^10").get().multiply(read("-17").get()), "-51*x^10");
        aeq(read("3*x^10").get().multiply(read("x^2-4*x+7").get()), "3*x^12-12*x^11+21*x^10");
        aeq(read("3*x^10").get().multiply(read("-x^3-1").get()), "-3*x^13-3*x^10");
        aeq(read("3*x^10").get().multiply(read("3*x^10").get()), "9*x^20");
        assertTrue(read("-1").get().multiply(read("-1").get()) == ONE);
    }

    @Test
    public void testMultiply_BigInteger() {
        assertTrue(ZERO.multiply(BigInteger.ZERO) == ZERO);
        assertTrue(ZERO.multiply(BigInteger.ONE) == ZERO);
        assertTrue(ZERO.multiply(BigInteger.valueOf(-3)) == ZERO);
        assertTrue(ZERO.multiply(BigInteger.valueOf(4)) == ZERO);
        assertTrue(ONE.multiply(BigInteger.ZERO) == ZERO);
        assertTrue(ONE.multiply(BigInteger.ONE) == ONE);
        aeq(ONE.multiply(BigInteger.valueOf(-3)), "-3");
        aeq(ONE.multiply(BigInteger.valueOf(4)), "4");
        assertTrue(X.multiply(BigInteger.ZERO) == ZERO);
        aeq(X.multiply(BigInteger.ONE), "x");
        aeq(X.multiply(BigInteger.valueOf(-3)), "-3*x");
        aeq(X.multiply(BigInteger.valueOf(4)), "4*x");
        assertTrue(read("-17").get().multiply(BigInteger.ZERO) == ZERO);
        aeq(read("-17").get().multiply(BigInteger.ONE), "-17");
        aeq(read("-17").get().multiply(BigInteger.valueOf(-3)), "51");
        aeq(read("-17").get().multiply(BigInteger.valueOf(4)), "-68");
        assertTrue(read("x^2-4*x+7").get().multiply(BigInteger.ZERO) == ZERO);
        aeq(read("x^2-4*x+7").get().multiply(BigInteger.ONE), "x^2-4*x+7");
        aeq(read("x^2-4*x+7").get().multiply(BigInteger.valueOf(-3)), "-3*x^2+12*x-21");
        aeq(read("x^2-4*x+7").get().multiply(BigInteger.valueOf(4)), "4*x^2-16*x+28");
        assertTrue(read("-x^3-1").get().multiply(BigInteger.ZERO) == ZERO);
        aeq(read("-x^3-1").get().multiply(BigInteger.ONE), "-x^3-1");
        aeq(read("-x^3-1").get().multiply(BigInteger.valueOf(-3)), "3*x^3+3");
        aeq(read("-x^3-1").get().multiply(BigInteger.valueOf(4)), "-4*x^3-4");
        assertTrue(read("3*x^10").get().multiply(BigInteger.ZERO) == ZERO);
        aeq(read("3*x^10").get().multiply(BigInteger.ONE), "3*x^10");
        aeq(read("3*x^10").get().multiply(BigInteger.valueOf(-3)), "-9*x^10");
        aeq(read("3*x^10").get().multiply(BigInteger.valueOf(4)), "12*x^10");
        assertTrue(read("-1").get().multiply(BigInteger.valueOf(-1)) == ONE);
    }

    @Test
    public void testMultiply_int() {
        assertTrue(ZERO.multiply(0) == ZERO);
        assertTrue(ZERO.multiply(1) == ZERO);
        assertTrue(ZERO.multiply(-3) == ZERO);
        assertTrue(ZERO.multiply(4) == ZERO);
        assertTrue(ONE.multiply(0) == ZERO);
        assertTrue(ONE.multiply(1) == ONE);
        aeq(ONE.multiply(-3), "-3");
        aeq(ONE.multiply(4), "4");
        assertTrue(X.multiply(0) == ZERO);
        aeq(X.multiply(1), "x");
        aeq(X.multiply(-3), "-3*x");
        aeq(X.multiply(4), "4*x");
        assertTrue(read("-17").get().multiply(0) == ZERO);
        aeq(read("-17").get().multiply(1), "-17");
        aeq(read("-17").get().multiply(-3), "51");
        aeq(read("-17").get().multiply(4), "-68");
        assertTrue(read("x^2-4*x+7").get().multiply(0) == ZERO);
        aeq(read("x^2-4*x+7").get().multiply(1), "x^2-4*x+7");
        aeq(read("x^2-4*x+7").get().multiply(-3), "-3*x^2+12*x-21");
        aeq(read("x^2-4*x+7").get().multiply(4), "4*x^2-16*x+28");
        assertTrue(read("-x^3-1").get().multiply(0) == ZERO);
        aeq(read("-x^3-1").get().multiply(1), "-x^3-1");
        aeq(read("-x^3-1").get().multiply(-3), "3*x^3+3");
        aeq(read("-x^3-1").get().multiply(4), "-4*x^3-4");
        assertTrue(read("3*x^10").get().multiply(0) == ZERO);
        aeq(read("3*x^10").get().multiply(1), "3*x^10");
        aeq(read("3*x^10").get().multiply(-3), "-9*x^10");
        aeq(read("3*x^10").get().multiply(4), "12*x^10");
        assertTrue(read("-1").get().multiply(-1) == ONE);
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

    @Test
    public void testToString() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
        aeq(X, "x");
        aeq(of(Arrays.asList(BigInteger.valueOf(-17))), "-17");
        aeq(of(Arrays.asList(BigInteger.ZERO, BigInteger.ONE)), "x");
        aeq(of(Arrays.asList(BigInteger.ZERO, BigInteger.valueOf(-1))), "-x");
        aeq(of(Arrays.asList(BigInteger.ZERO, BigInteger.valueOf(2))), "2*x");
        aeq(of(Arrays.asList(BigInteger.ZERO, BigInteger.valueOf(-2))), "-2*x");
        aeq(of(Arrays.asList(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ONE)), "x^2");
        aeq(of(Arrays.asList(BigInteger.ZERO, BigInteger.ZERO, BigInteger.valueOf(-1))), "-x^2");
        aeq(of(Arrays.asList(BigInteger.ZERO, BigInteger.ZERO, BigInteger.valueOf(2))), "2*x^2");
        aeq(of(Arrays.asList(BigInteger.ZERO, BigInteger.ZERO, BigInteger.valueOf(-2))), "-2*x^2");
        aeq(of(Arrays.asList(BigInteger.valueOf(7), BigInteger.valueOf(-4), BigInteger.ONE)), "x^2-4*x+7");
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
