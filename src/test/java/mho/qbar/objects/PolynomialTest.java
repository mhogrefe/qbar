package mho.qbar.objects;

import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.Polynomial.*;
import static mho.wheels.iterables.IterableUtils.toList;
import static mho.wheels.testing.Testing.aeq;
import static mho.wheels.testing.Testing.aeqit;
import static org.junit.Assert.*;

public class PolynomialTest {
    @Test
    public void testConstants() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
        aeq(X, "x");
    }

    private static void iteratorHelper(@NotNull String x, @NotNull String output) {
        aeq(toList(read(x).get()), output);
    }

    @Test
    public void testIterator() {
        iteratorHelper("0", "[]");
        iteratorHelper("1", "[1]");
        iteratorHelper("x", "[0, 1]");
        iteratorHelper("-17", "[-17]");
        iteratorHelper("x^2-4*x+7", "[7, -4, 1]");
        iteratorHelper("x^3-1", "[-1, 0, 0, 1]");
        iteratorHelper("3*x^10", "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3]");
    }

    @Test
    public void testApply_BigInteger() {
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
    public void testApply_Rational() {
        aeq(ZERO.apply(Rational.ZERO), 0);
        aeq(ZERO.apply(Rational.ONE), 0);
        aeq(ZERO.apply(Rational.of(-1)), 0);
        aeq(ZERO.apply(Rational.of(4, 5)), 0);
        aeq(ZERO.apply(Rational.of(100)), 0);
        aeq(ONE.apply(Rational.ZERO), 1);
        aeq(ONE.apply(Rational.ONE), 1);
        aeq(ONE.apply(Rational.of(-1)), 1);
        aeq(ONE.apply(Rational.of(4, 5)), 1);
        aeq(ONE.apply(Rational.of(100)), 1);
        aeq(X.apply(Rational.ZERO), 0);
        aeq(X.apply(Rational.ONE), 1);
        aeq(X.apply(Rational.of(-1)), -1);
        aeq(X.apply(Rational.of(4, 5)), "4/5");
        aeq(X.apply(Rational.of(100)), 100);
        aeq(read("-17").get().apply(Rational.ZERO), "-17");
        aeq(read("-17").get().apply(Rational.ONE), "-17");
        aeq(read("-17").get().apply(Rational.of(-1)), "-17");
        aeq(read("-17").get().apply(Rational.of(4, 5)), "-17");
        aeq(read("-17").get().apply(Rational.of(100)), "-17");
        aeq(read("x^2-4*x+7").get().apply(Rational.ZERO), "7");
        aeq(read("x^2-4*x+7").get().apply(Rational.ONE), "4");
        aeq(read("x^2-4*x+7").get().apply(Rational.of(-1)), "12");
        aeq(read("x^2-4*x+7").get().apply(Rational.of(4, 5)), "111/25");
        aeq(read("x^2-4*x+7").get().apply(Rational.of(100)), "9607");
        aeq(read("x^3-1").get().apply(Rational.ZERO), -1);
        aeq(read("x^3-1").get().apply(Rational.ONE), 0);
        aeq(read("x^3-1").get().apply(Rational.of(-1)), -2);
        aeq(read("x^3-1").get().apply(Rational.of(4, 5)), "-61/125");
        aeq(read("x^3-1").get().apply(Rational.of(100)), 999999);
        aeq(read("3*x^10").get().apply(Rational.ZERO), 0);
        aeq(read("3*x^10").get().apply(Rational.ONE), 3);
        aeq(read("3*x^10").get().apply(Rational.of(-1)), 3);
        aeq(read("3*x^10").get().apply(Rational.of(4, 5)), "3145728/9765625");
        aeq(read("3*x^10").get().apply(Rational.of(100)), "300000000000000000000");
    }

    @Test
    public void testToRationalPolynomial() {
        assertTrue(ZERO.toRationalPolynomial() == RationalPolynomial.ZERO);
        assertTrue(ONE.toRationalPolynomial() == RationalPolynomial.ONE);
        aeq(X.toRationalPolynomial(), RationalPolynomial.X);
        aeq(read("-17").get().toRationalPolynomial(), "-17");
        aeq(read("x^2-4*x+7").get().toRationalPolynomial(), "x^2-4*x+7");
        aeq(read("x^3-1").get().toRationalPolynomial(), "x^3-1");
        aeq(read("3*x^10").get().toRationalPolynomial(), "3*x^10");
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
        assertTrue(of(readBigIntegerList("[]")) == ZERO);
        assertTrue(of(readBigIntegerList("[0]")) == ZERO);
        assertTrue(of(readBigIntegerList("[0, 0, 0]")) == ZERO);
        assertTrue(of(readBigIntegerList("[1]")) == ONE);
        assertTrue(of(readBigIntegerList("[1, 0, 0]")) == ONE);
        aeq(of(readBigIntegerList("[0, 1]")), X);
        aeq(of(readBigIntegerList("[-17]")), "-17");
        aeq(of(readBigIntegerList("[7, -4, 1]")), "x^2-4*x+7");
        aeq(of(readBigIntegerList("[7, -4, 1, 0, 0]")), "x^2-4*x+7");
        aeq(of(readBigIntegerList("[-1, 0, 0, 1]")), "x^3-1");
        aeq(of(readBigIntegerList("[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3]")), "3*x^10");
        try {
            of(readBigIntegerListWithNulls("[7, null, 1]"));
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
    public void testShiftLeft() {
        assertTrue(ZERO.shiftLeft(0) == ZERO);
        assertTrue(ZERO.shiftLeft(1) == ZERO);
        assertTrue(ZERO.shiftLeft(2) == ZERO);
        assertTrue(ZERO.shiftLeft(3) == ZERO);
        assertTrue(ZERO.shiftLeft(4) == ZERO);
        assertTrue(ONE.shiftLeft(0) == ONE);
        aeq(ONE.shiftLeft(1), "2");
        aeq(ONE.shiftLeft(2), "4");
        aeq(ONE.shiftLeft(3), "8");
        aeq(ONE.shiftLeft(4), "16");
        aeq(X.shiftLeft(0), X);
        aeq(X.shiftLeft(1), "2*x");
        aeq(X.shiftLeft(2), "4*x");
        aeq(X.shiftLeft(3), "8*x");
        aeq(X.shiftLeft(4), "16*x");
        aeq(read("-17").get().shiftLeft(0), "-17");
        aeq(read("-17").get().shiftLeft(1), "-34");
        aeq(read("-17").get().shiftLeft(2), "-68");
        aeq(read("-17").get().shiftLeft(3), "-136");
        aeq(read("-17").get().shiftLeft(4), "-272");
        aeq(read("x^2-4*x+7").get().shiftLeft(0), "x^2-4*x+7");
        aeq(read("x^2-4*x+7").get().shiftLeft(1), "2*x^2-8*x+14");
        aeq(read("x^2-4*x+7").get().shiftLeft(2), "4*x^2-16*x+28");
        aeq(read("x^2-4*x+7").get().shiftLeft(3), "8*x^2-32*x+56");
        aeq(read("x^2-4*x+7").get().shiftLeft(4), "16*x^2-64*x+112");
        aeq(read("-x^3-1").get().shiftLeft(0), "-x^3-1");
        aeq(read("-x^3-1").get().shiftLeft(1), "-2*x^3-2");
        aeq(read("-x^3-1").get().shiftLeft(2), "-4*x^3-4");
        aeq(read("-x^3-1").get().shiftLeft(3), "-8*x^3-8");
        aeq(read("-x^3-1").get().shiftLeft(4), "-16*x^3-16");
        aeq(read("3*x^10").get().shiftLeft(0), "3*x^10");
        aeq(read("3*x^10").get().shiftLeft(1), "6*x^10");
        aeq(read("3*x^10").get().shiftLeft(2), "12*x^10");
        aeq(read("3*x^10").get().shiftLeft(3), "24*x^10");
        aeq(read("3*x^10").get().shiftLeft(4), "48*x^10");
        try {
            ZERO.shiftLeft(-1);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            ONE.shiftLeft(-1);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("-17").get().shiftLeft(-1);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSum() {
        assertTrue(sum(readPolynomialList("[]")) == ZERO);
        assertTrue(sum(readPolynomialList("[1]")) == ONE);
        aeq(sum(readPolynomialList("[-17]")), "-17");
        aeq(sum(readPolynomialList("[-17, x^2-4*x+7, -x^3-1, 3*x^10]")), "3*x^10-x^3+x^2-4*x-11");
        try {
            sum(readPolynomialListWithNulls("[-17, null, -x^3-1, 3*x^10]"));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProduct() {
        assertTrue(product(readPolynomialList("[]")) == ONE);
        assertTrue(product(readPolynomialList("[0]")) == ZERO);
        aeq(product(readPolynomialList("[-17]")), "-17");
        aeq(
                product(readPolynomialList("[-17, x^2-4*x+7, -x^3-1, 3*x^10]")),
                "51*x^15-204*x^14+357*x^13+51*x^12-204*x^11+357*x^10"
        );
        try {
            product(readPolynomialListWithNulls("[-17, null, -x^3-1, 3*x^10]"));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testDelta() {
        aeqit(delta(readPolynomialList("[-17]")), "[]");
        aeqit(delta(readPolynomialList("[-17, x^2-4*x+7]")), "[x^2-4*x+24]");
        aeqit(
                delta(readPolynomialList("[-17, x^2-4*x+7, -x^3-1, 3*x^10]")),
                "[x^2-4*x+24, -x^3-x^2+4*x-8, 3*x^10+x^3+1]"
        );
        try {
            delta(readPolynomialList("[]"));
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(delta(readPolynomialListWithNulls("[-17, null, -x^3-1, 3*x^10]")));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testPow() {
        aeq(read("x+1").get().pow(0), "1");
        aeq(read("x+1").get().pow(1), "x+1");
        aeq(read("x+1").get().pow(2), "x^2+2*x+1");
        aeq(read("x+1").get().pow(3), "x^3+3*x^2+3*x+1");
        aeq(read("x+1").get().pow(4), "x^4+4*x^3+6*x^2+4*x+1");
        aeq(read("x+1").get().pow(10), "x^10+10*x^9+45*x^8+120*x^7+210*x^6+252*x^5+210*x^4+120*x^3+45*x^2+10*x+1");
        assertTrue(ZERO.pow(0) == ONE);
        assertTrue(ZERO.pow(1) == ZERO);
        assertTrue(ZERO.pow(2) == ZERO);
        assertTrue(ZERO.pow(3) == ZERO);
        assertTrue(ONE.pow(0) == ONE);
        assertTrue(ONE.pow(1) == ONE);
        assertTrue(ONE.pow(2) == ONE);
        assertTrue(ONE.pow(3) == ONE);
        assertTrue(read("-17").get().pow(0) == ONE);
        aeq(read("-17").get().pow(1), "-17");
        aeq(read("-17").get().pow(2), "289");
        aeq(read("-17").get().pow(3), "-4913");
        assertTrue(read("x^2-4*x+7").get().pow(0) == ONE);
        aeq(read("x^2-4*x+7").get().pow(1), "x^2-4*x+7");
        aeq(read("x^2-4*x+7").get().pow(2), "x^4-8*x^3+30*x^2-56*x+49");
        aeq(read("x^2-4*x+7").get().pow(3), "x^6-12*x^5+69*x^4-232*x^3+483*x^2-588*x+343");
        assertTrue(read("-x^3-1").get().pow(0) == ONE);
        aeq(read("-x^3-1").get().pow(1), "-x^3-1");
        aeq(read("-x^3-1").get().pow(2), "x^6+2*x^3+1");
        aeq(read("-x^3-1").get().pow(3), "-x^9-3*x^6-3*x^3-1");
        assertTrue(read("3*x^10").get().pow(0) == ONE);
        aeq(read("3*x^10").get().pow(1), "3*x^10");
        aeq(read("3*x^10").get().pow(2), "9*x^20");
        aeq(read("3*x^10").get().pow(3), "27*x^30");
    }

    @Test
    public void testSubstitute() {
        assertTrue(ZERO.substitute(ZERO) == ZERO);
        assertTrue(ZERO.substitute(ONE) == ZERO);
        assertTrue(ZERO.substitute(X) == ZERO);
        assertTrue(ZERO.substitute(read("-17").get()) == ZERO);
        assertTrue(ZERO.substitute(read("x^2-4*x+7").get()) == ZERO);
        assertTrue(ZERO.substitute(read("-x^3-1").get()) == ZERO);
        assertTrue(ZERO.substitute(read("3*x^10").get()) == ZERO);
        assertTrue(ONE.substitute(ZERO) == ONE);
        assertTrue(ONE.substitute(ONE) == ONE);
        assertTrue(ONE.substitute(X) == ONE);
        assertTrue(ONE.substitute(read("-17").get()) == ONE);
        assertTrue(ONE.substitute(read("x^2-4*x+7").get()) == ONE);
        assertTrue(ONE.substitute(read("-x^3-1").get()) == ONE);
        assertTrue(ONE.substitute(read("3*x^10").get()) == ONE);
        assertTrue(X.substitute(ZERO) == ZERO);
        assertTrue(X.substitute(ONE) == ONE);
        aeq(X.substitute(X), "x");
        aeq(X.substitute(read("-17").get()), "-17");
        aeq(X.substitute(read("x^2-4*x+7").get()), "x^2-4*x+7");
        aeq(X.substitute(read("-x^3-1").get()), "-x^3-1");
        aeq(X.substitute(read("3*x^10").get()), "3*x^10");
        aeq(read("-17").get().substitute(ZERO), "-17");
        aeq(read("-17").get().substitute(ONE), "-17");
        aeq(read("-17").get().substitute(X), "-17");
        aeq(read("-17").get().substitute(read("-17").get()), "-17");
        aeq(read("-17").get().substitute(read("x^2-4*x+7").get()), "-17");
        aeq(read("-17").get().substitute(read("-x^3-1").get()), "-17");
        aeq(read("-17").get().substitute(read("3*x^10").get()), "-17");
        aeq(read("x^2-4*x+7").get().substitute(ZERO), "7");
        aeq(read("x^2-4*x+7").get().substitute(ONE), "4");
        aeq(read("x^2-4*x+7").get().substitute(X), "x^2-4*x+7");
        aeq(read("x^2-4*x+7").get().substitute(read("-17").get()), "364");
        aeq(read("x^2-4*x+7").get().substitute(read("x^2-4*x+7").get()), "x^4-8*x^3+26*x^2-40*x+28");
        aeq(read("x^2-4*x+7").get().substitute(read("-x^3-1").get()), "x^6+6*x^3+12");
        aeq(read("x^2-4*x+7").get().substitute(read("3*x^10").get()), "9*x^20-12*x^10+7");
        aeq(read("-x^3-1").get().substitute(ZERO), "-1");
        aeq(read("-x^3-1").get().substitute(ONE), "-2");
        aeq(read("-x^3-1").get().substitute(X), "-x^3-1");
        aeq(read("-x^3-1").get().substitute(read("-17").get()), "4912");
        aeq(read("-x^3-1").get().substitute(read("x^2-4*x+7").get()), "-x^6+12*x^5-69*x^4+232*x^3-483*x^2+588*x-344");
        aeq(read("-x^3-1").get().substitute(read("-x^3-1").get()), "x^9+3*x^6+3*x^3");
        aeq(read("-x^3-1").get().substitute(read("3*x^10").get()), "-27*x^30-1");
        assertTrue(read("3*x^10").get().substitute(ZERO) == ZERO);
        aeq(read("3*x^10").get().substitute(ONE), "3");
        aeq(read("3*x^10").get().substitute(X), "3*x^10");
        aeq(read("3*x^10").get().substitute(read("-17").get()), "6047981701347");
        aeq(read("3*x^10").get().substitute(read("x^2-4*x+7").get()),
                "3*x^20-120*x^19+2370*x^18-30600*x^17+288855*x^16-2114784*x^15+12441240*x^14-60158880*x^13" +
                "+242643510*x^12-823956240*x^11+2367787980*x^10-5767693680*x^9+11889531990*x^8-20634495840*x^7" +
                "+29871417240*x^6-35543174688*x^5+33983501895*x^4-25200415800*x^3+13662578370*x^2-4842432840*x" +
                "+847425747");
        aeq(read("3*x^10").get().substitute(read("-x^3-1").get()),
                "3*x^30+30*x^27+135*x^24+360*x^21+630*x^18+756*x^15+630*x^12+360*x^9+135*x^6+30*x^3+3");
        aeq(read("3*x^10").get().substitute(read("3*x^10").get()), "177147*x^100");
    }

    private static void differentiateHelper(@NotNull String x, @NotNull String output) {
        Polynomial derivative = read(x).get().differentiate();
        derivative.validate();
        aeq(derivative, output);
    }

    @Test
    public void testDifferentiate() {
        differentiateHelper("0", "0");
        differentiateHelper("1", "0");
        differentiateHelper("-17", "0");
        differentiateHelper("x", "1");
        differentiateHelper("x^2-4*x+7", "2*x-4");
        differentiateHelper("3*x^10", "30*x^9");
    }

    @Test
    public void testIsMonic() {
        assertFalse(ZERO.isMonic());
        assertTrue(ONE.isMonic());
        assertTrue(X.isMonic());
        assertFalse(read("-17").get().isMonic());
        assertTrue(read("x^2-4*x+7").get().isMonic());
        assertFalse(read("-x^3-1").get().isMonic());
        assertFalse(read("3*x^10").get().isMonic());
    }

    @Test
    public void testIsPrimitive() {
        assertFalse(ZERO.isPrimitive());
        assertTrue(ONE.isPrimitive());
        assertTrue(X.isPrimitive());
        assertFalse(read("-17").get().isPrimitive());
        assertTrue(read("x^2-4*x+7").get().isPrimitive());
        assertFalse(read("6*x^2-4*x+8").get().isPrimitive());
        assertFalse(read("-x^3-1").get().isPrimitive());
        assertFalse(read("3*x^10").get().isPrimitive());
    }

    @Test
    public void testContentAndPrimitive() {
        aeq(ONE.contentAndPrimitive(), "(1, 1)");
        aeq(X.contentAndPrimitive(), "(1, x)");
        aeq(read("-17").get().contentAndPrimitive(), "(-17, 1)");
        aeq(read("x^2-4*x+7").get().contentAndPrimitive(), "(1, x^2-4*x+7)");
        aeq(read("6*x^2-4*x+8").get().contentAndPrimitive(), "(2, 3*x^2-2*x+4)");
        aeq(read("-x^3-1").get().contentAndPrimitive(), "(-1, x^3+1)");
        aeq(read("3*x^10").get().contentAndPrimitive(), "(3, x^10)");
        try {
            ZERO.contentAndPrimitive();
            fail();
        } catch (ArithmeticException ignored) {}
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
        aeq(of(Collections.singletonList(BigInteger.valueOf(-17))), "-17");
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

    private static @NotNull List<BigInteger> readBigIntegerList(@NotNull String s) {
        return Readers.readList(Readers::readBigInteger).apply(s).get();
    }

    private static @NotNull List<BigInteger> readBigIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Readers::readBigInteger).apply(s).get();
    }

    private static @NotNull List<Polynomial> readPolynomialList(@NotNull String s) {
        return Readers.readList(Polynomial::read).apply(s).get();
    }

    private static @NotNull List<Polynomial> readPolynomialListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Polynomial::read).apply(s).get();
    }
}
