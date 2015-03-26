package mho.qbar.objects;

import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
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
    public void testApply() {
        aeq(ZERO.apply(Rational.ZERO), 0);
        aeq(ZERO.apply(Rational.ONE), 0);
        aeq(ZERO.apply(Rational.of(-1)), 0);
        aeq(ZERO.apply(Rational.of(5)), 0);
        aeq(ZERO.apply(Rational.of(100)), 0);
        aeq(ONE.apply(Rational.ZERO), 1);
        aeq(ONE.apply(Rational.ONE), 1);
        aeq(ONE.apply(Rational.of(-1)), 1);
        aeq(ONE.apply(Rational.of(5)), 1);
        aeq(ONE.apply(Rational.of(100)), 1);
        aeq(X.apply(Rational.ZERO), 0);
        aeq(X.apply(Rational.ONE), 1);
        aeq(X.apply(Rational.of(-1)), -1);
        aeq(X.apply(Rational.of(5)), 5);
        aeq(X.apply(Rational.of(100)), 100);
        aeq(read("-4/3").get().apply(Rational.ZERO), "-4/3");
        aeq(read("-4/3").get().apply(Rational.ONE), "-4/3");
        aeq(read("-4/3").get().apply(Rational.of(-1)), "-4/3");
        aeq(read("-4/3").get().apply(Rational.of(5)), "-4/3");
        aeq(read("-4/3").get().apply(Rational.of(100)), "-4/3");
        aeq(read("x^2-7/4*x+1/3").get().apply(Rational.ZERO), "1/3");
        aeq(read("x^2-7/4*x+1/3").get().apply(Rational.ONE), "-5/12");
        aeq(read("x^2-7/4*x+1/3").get().apply(Rational.of(-1)), "37/12");
        aeq(read("x^2-7/4*x+1/3").get().apply(Rational.of(5)), "199/12");
        aeq(read("x^2-7/4*x+1/3").get().apply(Rational.of(100)), "29476/3");
        aeq(read("x^3-1").get().apply(Rational.ZERO), -1);
        aeq(read("x^3-1").get().apply(Rational.ONE), 0);
        aeq(read("x^3-1").get().apply(Rational.of(-1)), -2);
        aeq(read("x^3-1").get().apply(Rational.of(5)), 124);
        aeq(read("x^3-1").get().apply(Rational.of(100)), 999999);
        aeq(read("1/2*x^10").get().apply(Rational.ZERO), 0);
        aeq(read("1/2*x^10").get().apply(Rational.ONE), "1/2");
        aeq(read("1/2*x^10").get().apply(Rational.of(-1)), "1/2");
        aeq(read("1/2*x^10").get().apply(Rational.of(5)), "9765625/2");
        aeq(read("1/2*x^10").get().apply(Rational.of(100)), "50000000000000000000");
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
    public void testAdd() {
        assertTrue(ZERO.add(ZERO) == ZERO);
        assertTrue(ZERO.add(ONE) == ONE);
        aeq(ZERO.add(X), "x");
        aeq(ZERO.add(read("-4/3").get()), "-4/3");
        aeq(ZERO.add(read("x^2-7/4*x+1/3").get()), "x^2-7/4*x+1/3");
        aeq(ZERO.add(read("-x^3-1").get()), "-x^3-1");
        aeq(ZERO.add(read("1/2*x^10").get()), "1/2*x^10");
        assertTrue(ONE.add(ZERO) == ONE);
        aeq(ONE.add(ONE), "2");
        aeq(ONE.add(X), "x+1");
        aeq(ONE.add(read("-4/3").get()), "-1/3");
        aeq(ONE.add(read("x^2-7/4*x+1/3").get()), "x^2-7/4*x+4/3");
        aeq(ONE.add(read("-x^3-1").get()), "-x^3");
        aeq(ONE.add(read("1/2*x^10").get()), "1/2*x^10+1");
        aeq(X.add(ZERO), "x");
        aeq(X.add(ONE), "x+1");
        aeq(X.add(X), "2*x");
        aeq(X.add(read("-4/3").get()), "x-4/3");
        aeq(X.add(read("x^2-7/4*x+1/3").get()), "x^2-3/4*x+1/3");
        aeq(X.add(read("-x^3-1").get()), "-x^3+x-1");
        aeq(X.add(read("1/2*x^10").get()), "1/2*x^10+x");
        aeq(read("-4/3").get().add(ZERO), "-4/3");
        aeq(read("-4/3").get().add(ONE), "-1/3");
        aeq(read("-4/3").get().add(X), "x-4/3");
        aeq(read("-4/3").get().add(read("-4/3").get()), "-8/3");
        aeq(read("-4/3").get().add(read("x^2-7/4*x+1/3").get()), "x^2-7/4*x-1");
        aeq(read("-4/3").get().add(read("-x^3-1").get()), "-x^3-7/3");
        aeq(read("-4/3").get().add(read("1/2*x^10").get()), "1/2*x^10-4/3");
        aeq(read("x^2-7/4*x+1/3").get().add(ZERO), "x^2-7/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().add(ONE), "x^2-7/4*x+4/3");
        aeq(read("x^2-7/4*x+1/3").get().add(X), "x^2-3/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().add(read("-4/3").get()), "x^2-7/4*x-1");
        aeq(read("x^2-7/4*x+1/3").get().add(read("x^2-7/4*x+1/3").get()), "2*x^2-7/2*x+2/3");
        aeq(read("x^2-7/4*x+1/3").get().add(read("-x^3-1").get()), "-x^3+x^2-7/4*x-2/3");
        aeq(read("x^2-7/4*x+1/3").get().add(read("1/2*x^10").get()), "1/2*x^10+x^2-7/4*x+1/3");
        aeq(read("-x^3-1").get().add(ZERO), "-x^3-1");
        aeq(read("-x^3-1").get().add(ONE), "-x^3");
        aeq(read("-x^3-1").get().add(X), "-x^3+x-1");
        aeq(read("-x^3-1").get().add(read("-4/3").get()), "-x^3-7/3");
        aeq(read("-x^3-1").get().add(read("x^2-7/4*x+1/3").get()), "-x^3+x^2-7/4*x-2/3");
        aeq(read("-x^3-1").get().add(read("-x^3-1").get()), "-2*x^3-2");
        aeq(read("-x^3-1").get().add(read("1/2*x^10").get()), "1/2*x^10-x^3-1");
        aeq(read("1/2*x^10").get().add(ZERO), "1/2*x^10");
        aeq(read("1/2*x^10").get().add(ONE), "1/2*x^10+1");
        aeq(read("1/2*x^10").get().add(X), "1/2*x^10+x");
        aeq(read("1/2*x^10").get().add(read("-4/3").get()), "1/2*x^10-4/3");
        aeq(read("1/2*x^10").get().add(read("x^2-7/4*x+1/3").get()), "1/2*x^10+x^2-7/4*x+1/3");
        aeq(read("1/2*x^10").get().add(read("-x^3-1").get()), "1/2*x^10-x^3-1");
        aeq(read("1/2*x^10").get().add(read("1/2*x^10").get()), "x^10");
        assertTrue(read("x^2-4*x+7").get().add(read("-x^2+4*x-7").get()) == ZERO);
        assertTrue(read("x+3/4").get().add(read("-x+1/4").get()) == ONE);
    }

    @Test
    public void testNegate() {
        assertTrue(ZERO.negate() == ZERO);
        aeq(ONE.negate(), "-1");
        aeq(X.negate(), "-x");
        aeq(read("-4/3").get().negate(), "4/3");
        aeq(read("x^2-7/4*x+1/3").get().negate(), "-x^2+7/4*x-1/3");
        aeq(read("-x^3-1").get().negate(), "x^3+1");
        aeq(read("1/2*x^10").get().negate(), "-1/2*x^10");
    }

    @Test
    public void testAbs() {
        assertTrue(ZERO.abs() == ZERO);
        assertTrue(ONE.abs() == ONE);
        aeq(X.abs(), "x");
        aeq(read("-4/3").get().abs(), "4/3");
        aeq(read("x^2-7/4*x+1/3").get().abs(), "x^2-7/4*x+1/3");
        aeq(read("-x^3-1").get().abs(), "x^3+1");
        aeq(read("1/2*x^10").get().abs(), "1/2*x^10");
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

    @Test
    public void testSubtract() {
        assertTrue(ZERO.subtract(ZERO) == ZERO);
        aeq(ZERO.subtract(ONE), "-1");
        aeq(ZERO.subtract(X), "-x");
        aeq(ZERO.subtract(read("-4/3").get()), "4/3");
        aeq(ZERO.subtract(read("x^2-7/4*x+1/3").get()), "-x^2+7/4*x-1/3");
        aeq(ZERO.subtract(read("-x^3-1").get()), "x^3+1");
        aeq(ZERO.subtract(read("1/2*x^10").get()), "-1/2*x^10");
        assertTrue(ONE.subtract(ZERO) == ONE);
        assertTrue(ONE.subtract(ONE) == ZERO);
        aeq(ONE.subtract(X), "-x+1");
        aeq(ONE.subtract(read("-4/3").get()), "7/3");
        aeq(ONE.subtract(read("x^2-7/4*x+1/3").get()), "-x^2+7/4*x+2/3");
        aeq(ONE.subtract(read("-x^3-1").get()), "x^3+2");
        aeq(ONE.subtract(read("1/2*x^10").get()), "-1/2*x^10+1");
        aeq(X.subtract(ZERO), "x");
        aeq(X.subtract(ONE), "x-1");
        assertTrue(X.subtract(X) == ZERO);
        aeq(X.subtract(read("-4/3").get()), "x+4/3");
        aeq(X.subtract(read("x^2-7/4*x+1/3").get()), "-x^2+11/4*x-1/3");
        aeq(X.subtract(read("-x^3-1").get()), "x^3+x+1");
        aeq(X.subtract(read("1/2*x^10").get()), "-1/2*x^10+x");
        aeq(read("-4/3").get().subtract(ZERO), "-4/3");
        aeq(read("-4/3").get().subtract(ONE), "-7/3");
        aeq(read("-4/3").get().subtract(X), "-x-4/3");
        assertTrue(read("-4/3").get().subtract(read("-4/3").get()) == ZERO);
        aeq(read("-4/3").get().subtract(read("x^2-7/4*x+1/3").get()), "-x^2+7/4*x-5/3");
        aeq(read("-4/3").get().subtract(read("-x^3-1").get()), "x^3-1/3");
        aeq(read("-4/3").get().subtract(read("1/2*x^10").get()), "-1/2*x^10-4/3");
        aeq(read("x^2-7/4*x+1/3").get().subtract(ZERO), "x^2-7/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().subtract(ONE), "x^2-7/4*x-2/3");
        aeq(read("x^2-7/4*x+1/3").get().subtract(X), "x^2-11/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().subtract(read("-4/3").get()), "x^2-7/4*x+5/3");
        assertTrue(read("x^2-7/4*x+1/3").get().subtract(read("x^2-7/4*x+1/3").get()) == ZERO);
        aeq(read("x^2-7/4*x+1/3").get().subtract(read("-x^3-1").get()), "x^3+x^2-7/4*x+4/3");
        aeq(read("x^2-7/4*x+1/3").get().subtract(read("1/2*x^10").get()), "-1/2*x^10+x^2-7/4*x+1/3");
        aeq(read("-x^3-1").get().subtract(ZERO), "-x^3-1");
        aeq(read("-x^3-1").get().subtract(ONE), "-x^3-2");
        aeq(read("-x^3-1").get().subtract(X), "-x^3-x-1");
        aeq(read("-x^3-1").get().subtract(read("-4/3").get()), "-x^3+1/3");
        aeq(read("-x^3-1").get().subtract(read("x^2-7/4*x+1/3").get()), "-x^3-x^2+7/4*x-4/3");
        assertTrue(read("-x^3-1").get().subtract(read("-x^3-1").get()) == ZERO);
        aeq(read("-x^3-1").get().subtract(read("1/2*x^10").get()), "-1/2*x^10-x^3-1");
        aeq(read("1/2*x^10").get().subtract(ZERO), "1/2*x^10");
        aeq(read("1/2*x^10").get().subtract(ONE), "1/2*x^10-1");
        aeq(read("1/2*x^10").get().subtract(X), "1/2*x^10-x");
        aeq(read("1/2*x^10").get().subtract(read("-4/3").get()), "1/2*x^10+4/3");
        aeq(read("1/2*x^10").get().subtract(read("x^2-7/4*x+1/3").get()), "1/2*x^10-x^2+7/4*x-1/3");
        aeq(read("1/2*x^10").get().subtract(read("-x^3-1").get()), "1/2*x^10+x^3+1");
        assertTrue(read("1/2*x^10").get().subtract(read("1/2*x^10").get()) == ZERO);
    }

    @Test
    public void testMultiply_RationalPolynomial() {
        assertTrue(ZERO.multiply(ZERO) == ZERO);
        assertTrue(ZERO.multiply(ONE) == ZERO);
        assertTrue(ZERO.multiply(X) == ZERO);
        assertTrue(ZERO.multiply(read("-4/3").get()) == ZERO);
        assertTrue(ZERO.multiply(read("x^2-7/4*x+1/3").get()) == ZERO);
        assertTrue(ZERO.multiply(read("-x^3-1").get()) == ZERO);
        assertTrue(ZERO.multiply(read("1/2*x^10").get()) == ZERO);
        assertTrue(ONE.multiply(ZERO) == ZERO);
        assertTrue(ONE.multiply(ONE) == ONE);
        aeq(ONE.multiply(X), "x");
        aeq(ONE.multiply(read("-4/3").get()), "-4/3");
        aeq(ONE.multiply(read("x^2-7/4*x+1/3").get()), "x^2-7/4*x+1/3");
        aeq(ONE.multiply(read("-x^3-1").get()), "-x^3-1");
        aeq(ONE.multiply(read("1/2*x^10").get()), "1/2*x^10");
        assertTrue(X.multiply(ZERO) == ZERO);
        aeq(X.multiply(ONE), "x");
        aeq(X.multiply(X), "x^2");
        aeq(X.multiply(read("-4/3").get()), "-4/3*x");
        aeq(X.multiply(read("x^2-7/4*x+1/3").get()), "x^3-7/4*x^2+1/3*x");
        aeq(X.multiply(read("-x^3-1").get()), "-x^4-x");
        aeq(X.multiply(read("1/2*x^10").get()), "1/2*x^11");
        assertTrue(read("-4/3").get().multiply(ZERO) == ZERO);
        aeq(read("-4/3").get().multiply(ONE), "-4/3");
        aeq(read("-4/3").get().multiply(X), "-4/3*x");
        aeq(read("-4/3").get().multiply(read("-4/3").get()), "16/9");
        aeq(read("-4/3").get().multiply(read("x^2-7/4*x+1/3").get()), "-4/3*x^2+7/3*x-4/9");
        aeq(read("-4/3").get().multiply(read("-x^3-1").get()), "4/3*x^3+4/3");
        aeq(read("-4/3").get().multiply(read("1/2*x^10").get()), "-2/3*x^10");
        assertTrue(read("x^2-7/4*x+1/3").get().multiply(ZERO) == ZERO);
        aeq(read("x^2-7/4*x+1/3").get().multiply(ONE), "x^2-7/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().multiply(X), "x^3-7/4*x^2+1/3*x");
        aeq(read("x^2-7/4*x+1/3").get().multiply(read("-4/3").get()), "-4/3*x^2+7/3*x-4/9");
        aeq(read("x^2-7/4*x+1/3").get().multiply(read("x^2-7/4*x+1/3").get()), "x^4-7/2*x^3+179/48*x^2-7/6*x+1/9");
        aeq(read("x^2-7/4*x+1/3").get().multiply(read("-x^3-1").get()), "-x^5+7/4*x^4-1/3*x^3-x^2+7/4*x-1/3");
        aeq(read("x^2-7/4*x+1/3").get().multiply(read("1/2*x^10").get()), "1/2*x^12-7/8*x^11+1/6*x^10");
        assertTrue(read("-x^3-1").get().multiply(ZERO) == ZERO);
        aeq(read("-x^3-1").get().multiply(ONE), "-x^3-1");
        aeq(read("-x^3-1").get().multiply(X), "-x^4-x");
        aeq(read("-x^3-1").get().multiply(read("-4/3").get()), "4/3*x^3+4/3");
        aeq(read("-x^3-1").get().multiply(read("x^2-7/4*x+1/3").get()), "-x^5+7/4*x^4-1/3*x^3-x^2+7/4*x-1/3");
        aeq(read("-x^3-1").get().multiply(read("-x^3-1").get()), "x^6+2*x^3+1");
        aeq(read("-x^3-1").get().multiply(read("1/2*x^10").get()), "-1/2*x^13-1/2*x^10");
        assertTrue(read("3*x^10").get().multiply(ZERO) == ZERO);
        aeq(read("1/2*x^10").get().multiply(ONE), "1/2*x^10");
        aeq(read("1/2*x^10").get().multiply(X), "1/2*x^11");
        aeq(read("1/2*x^10").get().multiply(read("-4/3").get()), "-2/3*x^10");
        aeq(read("1/2*x^10").get().multiply(read("x^2-7/4*x+1/3").get()), "1/2*x^12-7/8*x^11+1/6*x^10");
        aeq(read("1/2*x^10").get().multiply(read("-x^3-1").get()), "-1/2*x^13-1/2*x^10");
        aeq(read("1/2*x^10").get().multiply(read("1/2*x^10").get()), "1/4*x^20");
        assertTrue(read("1/2").get().multiply(read("2").get()) == ONE);
    }

    @Test
    public void testEquals() {
        //noinspection EqualsWithItself
        assertTrue(ZERO.equals(ZERO));
        //noinspection EqualsWithItself
        assertTrue(ONE.equals(ONE));
        //noinspection EqualsWithItself
        assertTrue(X.equals(X));
        assertTrue(read("-4/3").get().equals(read("-4/3").get()));
        assertTrue(read("x^2-7/4*x+1/3").get().equals(read("x^2-7/4*x+1/3").get()));
        assertTrue(read("-x^3-1").get().equals(read("-x^3-1").get()));
        assertTrue(read("1/2*x^10").get().equals(read("1/2*x^10").get()));
        assertFalse(ZERO.equals(ONE));
        assertFalse(ZERO.equals(X));
        assertFalse(ONE.equals(ZERO));
        assertFalse(ONE.equals(X));
        assertFalse(X.equals(ZERO));
        assertFalse(X.equals(ONE));
        assertFalse(ZERO.equals(read("-4/3").get()));
        assertFalse(ZERO.equals(read("x^2-7/4*x+1/3").get()));
        assertFalse(ZERO.equals(read("-x^3-1").get()));
        assertFalse(ZERO.equals(read("1/2*x^10").get()));
        assertFalse(ONE.equals(read("-4/3").get()));
        assertFalse(ONE.equals(read("x^2-7/4*x+1/3").get()));
        assertFalse(ONE.equals(read("-x^3-1").get()));
        assertFalse(ONE.equals(read("1/2*x^10").get()));
        assertFalse(X.equals(read("-4/3").get()));
        assertFalse(X.equals(read("x^2-7/4*x+1/3").get()));
        assertFalse(X.equals(read("-x^3-1").get()));
        assertFalse(X.equals(read("1/2*x^10").get()));
        assertFalse(read("-4/3").get().equals(ZERO));
        assertFalse(read("x^2-7/4*x+1/3").get().equals(ZERO));
        assertFalse(read("-x^3-1").get().equals(ZERO));
        assertFalse(read("1/2*x^10").get().equals(ZERO));
        assertFalse(read("-4/3").get().equals(ONE));
        assertFalse(read("x^2-7/4*x+1/3").get().equals(ONE));
        assertFalse(read("-x^3-1").get().equals(ONE));
        assertFalse(read("1/2*x^10").get().equals(ONE));
        assertFalse(read("-4/3").get().equals(X));
        assertFalse(read("x^2-7/4*x+1/3").get().equals(X));
        assertFalse(read("-x^3-1").get().equals(X));
        assertFalse(read("1/2*x^10").get().equals(X));
        assertFalse(read("-4/3").equals(read("x^2-7/4*x+1/3")));
        assertFalse(read("-4/3").equals(read("-x^3-1")));
        assertFalse(read("-4/3").equals(read("1/2*x^10")));
        assertFalse(read("x^2-7/4*x+1/3").equals(read("-4/3")));
        assertFalse(read("x^2-7/4*x+1/3").equals(read("-x^3-1")));
        assertFalse(read("x^2-7/4*x+1/3").equals(read("1/2*x^10")));
        assertFalse(read("-x^3-1").equals(read("-4/3")));
        assertFalse(read("-x^3-1").equals(read("x^2-7/4*x+1/3")));
        assertFalse(read("-x^3-1").equals(read("1/2*x^10")));
        assertFalse(read("1/2*x^10").equals(read("-4/3")));
        assertFalse(read("1/2*x^10").equals(read("x^2-7/4*x+1/3")));
        assertFalse(read("1/2*x^10").equals(read("-x^3-1")));
    }

    @Test
    public void testHashCode() {
        aeq(ZERO.hashCode(), 1);
        aeq(ONE.hashCode(), 63);
        aeq(X.hashCode(), 1024);
        aeq(read("-4/3").get().hashCode(), -90);
        aeq(read("x^2-7/4*x+1/3").get().hashCode(), 55894);
        aeq(read("-x^3-1").get().hashCode(), 30753);
        aeq(read("1/2*x^10").get().hashCode(), -1011939104);
    }

    @Test
    public void testCompareTo() {
        aeq(ZERO.compareTo(ZERO), 0);
        aeq(ZERO.compareTo(ONE), -1);
        aeq(ZERO.compareTo(X), -1);
        aeq(ZERO.compareTo(read("-4/3").get()), 1);
        aeq(ZERO.compareTo(read("x^2-7/4*x+1/3").get()), -1);
        aeq(ZERO.compareTo(read("-x^3-1").get()), 1);
        aeq(ZERO.compareTo(read("1/2*x^10").get()), -1);
        aeq(ONE.compareTo(ZERO), 1);
        aeq(ONE.compareTo(ONE), 0);
        aeq(ONE.compareTo(X), -1);
        aeq(ONE.compareTo(read("-4/3").get()), 1);
        aeq(ONE.compareTo(read("x^2-7/4*x+1/3").get()), -1);
        aeq(ONE.compareTo(read("-x^3-1").get()), 1);
        aeq(ONE.compareTo(read("1/2*x^10").get()), -1);
        aeq(X.compareTo(ZERO), 1);
        aeq(X.compareTo(ONE), 1);
        aeq(X.compareTo(X), 0);
        aeq(X.compareTo(read("-4/3").get()), 1);
        aeq(X.compareTo(read("x^2-7/4*x+1/3").get()), -1);
        aeq(X.compareTo(read("-x^3-1").get()), 1);
        aeq(X.compareTo(read("1/2*x^10").get()), -1);
        aeq(read("-4/3").get().compareTo(ZERO), -1);
        aeq(read("-4/3").get().compareTo(ONE), -1);
        aeq(read("-4/3").get().compareTo(X), -1);
        aeq(read("-4/3").get().compareTo(read("-4/3").get()), 0);
        aeq(read("-4/3").get().compareTo(read("x^2-7/4*x+1/3").get()), -1);
        aeq(read("-4/3").get().compareTo(read("-x^3-1").get()), 1);
        aeq(read("-4/3").get().compareTo(read("1/2*x^10").get()), -1);
        aeq(read("x^2-7/4*x+1/3").get().compareTo(ZERO), 1);
        aeq(read("x^2-7/4*x+1/3").get().compareTo(ONE), 1);
        aeq(read("x^2-7/4*x+1/3").get().compareTo(X), 1);
        aeq(read("x^2-7/4*x+1/3").get().compareTo(read("-4/3").get()), 1);
        aeq(read("x^2-7/4*x+1/3").get().compareTo(read("x^2-7/4*x+1/3").get()), 0);
        aeq(read("x^2-7/4*x+1/3").get().compareTo(read("-x^3-1").get()), 1);
        aeq(read("x^2-7/4*x+1/3").get().compareTo(read("1/2*x^10").get()), -1);
        aeq(read("-x^3-1").get().compareTo(ZERO), -1);
        aeq(read("-x^3-1").get().compareTo(ONE), -1);
        aeq(read("-x^3-1").get().compareTo(X), -1);
        aeq(read("-x^3-1").get().compareTo(read("-4/3").get()), -1);
        aeq(read("-x^3-1").get().compareTo(read("x^2-7/4*x+1/3").get()), -1);
        aeq(read("-x^3-1").get().compareTo(read("-x^3-1").get()), 0);
        aeq(read("-x^3-1").get().compareTo(read("1/2*x^10").get()), -1);
        aeq(read("1/2*x^10").get().compareTo(ZERO), 1);
        aeq(read("1/2*x^10").get().compareTo(ONE), 1);
        aeq(read("1/2*x^10").get().compareTo(X), 1);
        aeq(read("1/2*x^10").get().compareTo(read("-4/3").get()), 1);
        aeq(read("1/2*x^10").get().compareTo(read("x^2-7/4*x+1/3").get()), 1);
        aeq(read("1/2*x^10").get().compareTo(read("-x^3-1").get()), 1);
        aeq(read("1/2*x^10").get().compareTo(read("1/2*x^10").get()), 0);
    }

    @Test
    public void testRead() {
        assertTrue(read("0").get() == ZERO);
        assertTrue(read("1").get() == ONE);
        aeq(read("x").get(), X);
        aeq(read("2").get(), "2");
        aeq(read("-2").get(), "-2");
        aeq(read("4/3").get(), "4/3");
        aeq(read("-4/3").get(), "-4/3");
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
        aeq(read("x^2-7/4*x+1/3").get(), "x^2-7/4*x+1/3");
        aeq(read("1/2*x^10").get(), "1/2*x^10");
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
        assertFalse(read("1/2x").isPresent());
        assertFalse(read("x^2+1+x").isPresent());
        assertFalse(read("x^2+3*x^2").isPresent());
        assertFalse(read("2^x").isPresent());
        assertFalse(read("abc").isPresent());
        assertFalse(read("x+y").isPresent());
        assertFalse(read("y").isPresent());
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
        aeq(findIn("2/4*x").get(), "(2, 0)");
        aeq(findIn("x/2").get(), "(x, 0)");
        assertFalse(findIn("").isPresent());
        assertFalse(findIn("o").isPresent());
        assertFalse(findIn("hello").isPresent());
    }

    @Test
    public void testToString() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
        aeq(X, "x");
        aeq(of(Arrays.asList(Rational.of(-4, 3))), "-4/3");
        aeq(of(Arrays.asList(Rational.ZERO, Rational.ONE)), "x");
        aeq(of(Arrays.asList(Rational.ZERO, Rational.of(-1))), "-x");
        aeq(of(Arrays.asList(Rational.ZERO, Rational.of(2))), "2*x");
        aeq(of(Arrays.asList(Rational.ZERO, Rational.of(-2))), "-2*x");
        aeq(of(Arrays.asList(Rational.ZERO, Rational.ZERO, Rational.ONE)), "x^2");
        aeq(of(Arrays.asList(Rational.ZERO, Rational.ZERO, Rational.of(-1))), "-x^2");
        aeq(of(Arrays.asList(Rational.ZERO, Rational.ZERO, Rational.of(2))), "2*x^2");
        aeq(of(Arrays.asList(Rational.ZERO, Rational.ZERO, Rational.of(-2))), "-2*x^2");
        aeq(of(Arrays.asList(Rational.of(1, 3), Rational.of(-7, 4), Rational.ONE)), "x^2-7/4*x+1/3");
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