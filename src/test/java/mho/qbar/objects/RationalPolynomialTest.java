package mho.qbar.objects;

import mho.wheels.iterables.IterableUtils;
import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
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
        aeq(read("-4/3").get().apply(Rational.ZERO), "-4/3");
        aeq(read("-4/3").get().apply(Rational.ONE), "-4/3");
        aeq(read("-4/3").get().apply(Rational.of(-1)), "-4/3");
        aeq(read("-4/3").get().apply(Rational.of(4, 5)), "-4/3");
        aeq(read("-4/3").get().apply(Rational.of(100)), "-4/3");
        aeq(read("x^2-7/4*x+1/3").get().apply(Rational.ZERO), "1/3");
        aeq(read("x^2-7/4*x+1/3").get().apply(Rational.ONE), "-5/12");
        aeq(read("x^2-7/4*x+1/3").get().apply(Rational.of(-1)), "37/12");
        aeq(read("x^2-7/4*x+1/3").get().apply(Rational.of(4, 5)), "-32/75");
        aeq(read("x^2-7/4*x+1/3").get().apply(Rational.of(100)), "29476/3");
        aeq(read("x^3-1").get().apply(Rational.ZERO), -1);
        aeq(read("x^3-1").get().apply(Rational.ONE), 0);
        aeq(read("x^3-1").get().apply(Rational.of(-1)), -2);
        aeq(read("x^3-1").get().apply(Rational.of(4, 5)), "-61/125");
        aeq(read("x^3-1").get().apply(Rational.of(100)), 999999);
        aeq(read("1/2*x^10").get().apply(Rational.ZERO), 0);
        aeq(read("1/2*x^10").get().apply(Rational.ONE), "1/2");
        aeq(read("1/2*x^10").get().apply(Rational.of(-1)), "1/2");
        aeq(read("1/2*x^10").get().apply(Rational.of(4, 5)), "524288/9765625");
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
    public void testMultiply_Rational() {
        assertTrue(ZERO.multiply(Rational.ZERO) == ZERO);
        assertTrue(ZERO.multiply(Rational.ONE) == ZERO);
        assertTrue(ZERO.multiply(Rational.read("-3").get()) == ZERO);
        assertTrue(ZERO.multiply(Rational.read("4/5").get()) == ZERO);
        assertTrue(ONE.multiply(Rational.ZERO) == ZERO);
        assertTrue(ONE.multiply(Rational.ONE) == ONE);
        aeq(ONE.multiply(Rational.read("-3").get()), "-3");
        aeq(ONE.multiply(Rational.read("4/5").get()), "4/5");
        assertTrue(X.multiply(Rational.ZERO) == ZERO);
        aeq(X.multiply(Rational.ONE), "x");
        aeq(X.multiply(Rational.read("-3").get()), "-3*x");
        aeq(X.multiply(Rational.read("4/5").get()), "4/5*x");
        assertTrue(read("-4/3").get().multiply(Rational.ZERO) == ZERO);
        aeq(read("-4/3").get().multiply(Rational.ONE), "-4/3");
        aeq(read("-4/3").get().multiply(Rational.read("-3").get()), "4");
        aeq(read("-4/3").get().multiply(Rational.read("4/5").get()), "-16/15");
        assertTrue(read("x^2-7/4*x+1/3").get().multiply(Rational.ZERO) == ZERO);
        aeq(read("x^2-7/4*x+1/3").get().multiply(Rational.ONE), "x^2-7/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().multiply(Rational.read("-3").get()), "-3*x^2+21/4*x-1");
        aeq(read("x^2-7/4*x+1/3").get().multiply(Rational.read("4/5").get()), "4/5*x^2-7/5*x+4/15");
        assertTrue(read("-x^3-1").get().multiply(Rational.ZERO) == ZERO);
        aeq(read("-x^3-1").get().multiply(Rational.ONE), "-x^3-1");
        aeq(read("-x^3-1").get().multiply(Rational.read("-3").get()), "3*x^3+3");
        aeq(read("-x^3-1").get().multiply(Rational.read("4/5").get()), "-4/5*x^3-4/5");
        assertTrue(read("1/2*x^10").get().multiply(Rational.ZERO) == ZERO);
        aeq(read("1/2*x^10").get().multiply(Rational.ONE), "1/2*x^10");
        aeq(read("1/2*x^10").get().multiply(Rational.read("-3").get()), "-3/2*x^10");
        aeq(read("1/2*x^10").get().multiply(Rational.read("4/5").get()), "2/5*x^10");
        assertTrue(read("5/4").get().multiply(Rational.read("4/5").get()) == ONE);
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
        assertTrue(read("-4/3").get().multiply(BigInteger.ZERO) == ZERO);
        aeq(read("-4/3").get().multiply(BigInteger.ONE), "-4/3");
        aeq(read("-4/3").get().multiply(BigInteger.valueOf(-3)), "4");
        aeq(read("-4/3").get().multiply(BigInteger.valueOf(4)), "-16/3");
        assertTrue(read("x^2-7/4*x+1/3").get().multiply(BigInteger.ZERO) == ZERO);
        aeq(read("x^2-7/4*x+1/3").get().multiply(BigInteger.ONE), "x^2-7/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().multiply(BigInteger.valueOf(-3)), "-3*x^2+21/4*x-1");
        aeq(read("x^2-7/4*x+1/3").get().multiply(BigInteger.valueOf(4)), "4*x^2-7*x+4/3");
        assertTrue(read("-x^3-1").get().multiply(BigInteger.ZERO) == ZERO);
        aeq(read("-x^3-1").get().multiply(BigInteger.ONE), "-x^3-1");
        aeq(read("-x^3-1").get().multiply(BigInteger.valueOf(-3)), "3*x^3+3");
        aeq(read("-x^3-1").get().multiply(BigInteger.valueOf(4)), "-4*x^3-4");
        assertTrue(read("1/2*x^10").get().multiply(BigInteger.ZERO) == ZERO);
        aeq(read("1/2*x^10").get().multiply(BigInteger.ONE), "1/2*x^10");
        aeq(read("1/2*x^10").get().multiply(BigInteger.valueOf(-3)), "-3/2*x^10");
        aeq(read("1/2*x^10").get().multiply(BigInteger.valueOf(4)), "2*x^10");
        assertTrue(read("1/4").get().multiply(BigInteger.valueOf(4)) == ONE);
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
        assertTrue(read("-4/3").get().multiply(0) == ZERO);
        aeq(read("-4/3").get().multiply(1), "-4/3");
        aeq(read("-4/3").get().multiply(-3), "4");
        aeq(read("-4/3").get().multiply(4), "-16/3");
        assertTrue(read("x^2-7/4*x+1/3").get().multiply(0) == ZERO);
        aeq(read("x^2-7/4*x+1/3").get().multiply(1), "x^2-7/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().multiply(-3), "-3*x^2+21/4*x-1");
        aeq(read("x^2-7/4*x+1/3").get().multiply(4), "4*x^2-7*x+4/3");
        assertTrue(read("-x^3-1").get().multiply(0) == ZERO);
        aeq(read("-x^3-1").get().multiply(1), "-x^3-1");
        aeq(read("-x^3-1").get().multiply(-3), "3*x^3+3");
        aeq(read("-x^3-1").get().multiply(4), "-4*x^3-4");
        assertTrue(read("1/2*x^10").get().multiply(0) == ZERO);
        aeq(read("1/2*x^10").get().multiply(1), "1/2*x^10");
        aeq(read("1/2*x^10").get().multiply(-3), "-3/2*x^10");
        aeq(read("1/2*x^10").get().multiply(4), "2*x^10");
        assertTrue(read("1/4").get().multiply(4) == ONE);
    }

    @Test
    public void testDivide_Rational() {
        assertTrue(ZERO.divide(Rational.ONE) == ZERO);
        assertTrue(ZERO.divide(Rational.read("-3").get()) == ZERO);
        assertTrue(ZERO.divide(Rational.read("4/5").get()) == ZERO);
        assertTrue(ONE.divide(Rational.ONE) == ONE);
        aeq(ONE.divide(Rational.read("-3").get()), "-1/3");
        aeq(ONE.divide(Rational.read("4/5").get()), "5/4");
        aeq(X.divide(Rational.ONE), "x");
        aeq(X.divide(Rational.read("-3").get()), "-1/3*x");
        aeq(X.divide(Rational.read("4/5").get()), "5/4*x");
        aeq(read("-4/3").get().divide(Rational.ONE), "-4/3");
        aeq(read("-4/3").get().divide(Rational.read("-3").get()), "4/9");
        aeq(read("-4/3").get().divide(Rational.read("4/5").get()), "-5/3");
        aeq(read("x^2-7/4*x+1/3").get().divide(Rational.ONE), "x^2-7/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().divide(Rational.read("-3").get()), "-1/3*x^2+7/12*x-1/9");
        aeq(read("x^2-7/4*x+1/3").get().divide(Rational.read("4/5").get()), "5/4*x^2-35/16*x+5/12");
        aeq(read("-x^3-1").get().divide(Rational.ONE), "-x^3-1");
        aeq(read("-x^3-1").get().divide(Rational.read("-3").get()), "1/3*x^3+1/3");
        aeq(read("-x^3-1").get().divide(Rational.read("4/5").get()), "-5/4*x^3-5/4");
        aeq(read("1/2*x^10").get().divide(Rational.ONE), "1/2*x^10");
        aeq(read("1/2*x^10").get().divide(Rational.read("-3").get()), "-1/6*x^10");
        aeq(read("1/2*x^10").get().divide(Rational.read("4/5").get()), "5/8*x^10");
        assertTrue(read("5/4").get().divide(Rational.read("5/4").get()) == ONE);
        try {
            ZERO.divide(Rational.ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("-4/3").get().divide(Rational.ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_BigInteger() {
        assertTrue(ZERO.divide(BigInteger.ONE) == ZERO);
        assertTrue(ZERO.divide(BigInteger.valueOf(-3)) == ZERO);
        assertTrue(ZERO.divide(BigInteger.valueOf(4)) == ZERO);
        assertTrue(ONE.divide(BigInteger.ONE) == ONE);
        aeq(ONE.divide(BigInteger.valueOf(-3)), "-1/3");
        aeq(ONE.divide(BigInteger.valueOf(4)), "1/4");
        aeq(X.divide(BigInteger.ONE), "x");
        aeq(X.divide(BigInteger.valueOf(-3)), "-1/3*x");
        aeq(X.divide(BigInteger.valueOf(4)), "1/4*x");
        aeq(read("-4/3").get().divide(BigInteger.ONE), "-4/3");
        aeq(read("-4/3").get().divide(BigInteger.valueOf(-3)), "4/9");
        aeq(read("-4/3").get().divide(BigInteger.valueOf(4)), "-1/3");
        aeq(read("x^2-7/4*x+1/3").get().divide(BigInteger.ONE), "x^2-7/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().divide(BigInteger.valueOf(-3)), "-1/3*x^2+7/12*x-1/9");
        aeq(read("x^2-7/4*x+1/3").get().divide(BigInteger.valueOf(4)), "1/4*x^2-7/16*x+1/12");
        aeq(read("-x^3-1").get().divide(BigInteger.ONE), "-x^3-1");
        aeq(read("-x^3-1").get().divide(BigInteger.valueOf(-3)), "1/3*x^3+1/3");
        aeq(read("-x^3-1").get().divide(BigInteger.valueOf(4)), "-1/4*x^3-1/4");
        aeq(read("1/2*x^10").get().divide(BigInteger.ONE), "1/2*x^10");
        aeq(read("1/2*x^10").get().divide(BigInteger.valueOf(-3)), "-1/6*x^10");
        aeq(read("1/2*x^10").get().divide(BigInteger.valueOf(4)), "1/8*x^10");
        assertTrue(read("5").get().divide(BigInteger.valueOf(5)) == ONE);
        try {
            ZERO.divide(BigInteger.ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            ONE.divide(BigInteger.ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_int() {
        assertTrue(ZERO.divide(1) == ZERO);
        assertTrue(ZERO.divide(-3) == ZERO);
        assertTrue(ZERO.divide(4) == ZERO);
        assertTrue(ONE.divide(1) == ONE);
        aeq(ONE.divide(-3), "-1/3");
        aeq(ONE.divide(4), "1/4");
        aeq(X.divide(1), "x");
        aeq(X.divide(-3), "-1/3*x");
        aeq(X.divide(4), "1/4*x");
        aeq(read("-4/3").get().divide(1), "-4/3");
        aeq(read("-4/3").get().divide(-3), "4/9");
        aeq(read("-4/3").get().divide(4), "-1/3");
        aeq(read("x^2-7/4*x+1/3").get().divide(1), "x^2-7/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().divide(-3), "-1/3*x^2+7/12*x-1/9");
        aeq(read("x^2-7/4*x+1/3").get().divide(4), "1/4*x^2-7/16*x+1/12");
        aeq(read("-x^3-1").get().divide(1), "-x^3-1");
        aeq(read("-x^3-1").get().divide(-3), "1/3*x^3+1/3");
        aeq(read("-x^3-1").get().divide(4), "-1/4*x^3-1/4");
        aeq(read("1/2*x^10").get().divide(1), "1/2*x^10");
        aeq(read("1/2*x^10").get().divide(-3), "-1/6*x^10");
        aeq(read("1/2*x^10").get().divide(4), "1/8*x^10");
        assertTrue(read("5").get().divide(5) == ONE);
        try {
            ZERO.divide(0);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            ONE.divide(0);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShiftLeft() {
        assertTrue(ZERO.shiftLeft(0) == ZERO);
        assertTrue(ZERO.shiftLeft(1) == ZERO);
        assertTrue(ZERO.shiftLeft(2) == ZERO);
        assertTrue(ZERO.shiftLeft(3) == ZERO);
        assertTrue(ZERO.shiftLeft(4) == ZERO);
        assertTrue(ZERO.shiftLeft(-1) == ZERO);
        assertTrue(ZERO.shiftLeft(-2) == ZERO);
        assertTrue(ZERO.shiftLeft(-3) == ZERO);
        assertTrue(ZERO.shiftLeft(-4) == ZERO);
        assertTrue(ONE.shiftLeft(0) == ONE);
        aeq(ONE.shiftLeft(1), "2");
        aeq(ONE.shiftLeft(2), "4");
        aeq(ONE.shiftLeft(3), "8");
        aeq(ONE.shiftLeft(4), "16");
        aeq(ONE.shiftLeft(-1), "1/2");
        aeq(ONE.shiftLeft(-2), "1/4");
        aeq(ONE.shiftLeft(-3), "1/8");
        aeq(ONE.shiftLeft(-4), "1/16");
        aeq(X.shiftLeft(0), X);
        aeq(X.shiftLeft(1), "2*x");
        aeq(X.shiftLeft(2), "4*x");
        aeq(X.shiftLeft(3), "8*x");
        aeq(X.shiftLeft(4), "16*x");
        aeq(X.shiftLeft(-1), "1/2*x");
        aeq(X.shiftLeft(-2), "1/4*x");
        aeq(X.shiftLeft(-3), "1/8*x");
        aeq(X.shiftLeft(-4), "1/16*x");
        aeq(read("-4/3").get().shiftLeft(0), "-4/3");
        aeq(read("-4/3").get().shiftLeft(1), "-8/3");
        aeq(read("-4/3").get().shiftLeft(2), "-16/3");
        aeq(read("-4/3").get().shiftLeft(3), "-32/3");
        aeq(read("-4/3").get().shiftLeft(4), "-64/3");
        aeq(read("-4/3").get().shiftLeft(-1), "-2/3");
        aeq(read("-4/3").get().shiftLeft(-2), "-1/3");
        aeq(read("-4/3").get().shiftLeft(-3), "-1/6");
        aeq(read("-4/3").get().shiftLeft(-4), "-1/12");
        aeq(read("x^2-7/4*x+1/3").get().shiftLeft(0), "x^2-7/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().shiftLeft(1), "2*x^2-7/2*x+2/3");
        aeq(read("x^2-7/4*x+1/3").get().shiftLeft(2), "4*x^2-7*x+4/3");
        aeq(read("x^2-7/4*x+1/3").get().shiftLeft(3), "8*x^2-14*x+8/3");
        aeq(read("x^2-7/4*x+1/3").get().shiftLeft(4), "16*x^2-28*x+16/3");
        aeq(read("x^2-7/4*x+1/3").get().shiftLeft(-1), "1/2*x^2-7/8*x+1/6");
        aeq(read("x^2-7/4*x+1/3").get().shiftLeft(-2), "1/4*x^2-7/16*x+1/12");
        aeq(read("x^2-7/4*x+1/3").get().shiftLeft(-3), "1/8*x^2-7/32*x+1/24");
        aeq(read("x^2-7/4*x+1/3").get().shiftLeft(-4), "1/16*x^2-7/64*x+1/48");
        aeq(read("-x^3-1").get().shiftLeft(0), "-x^3-1");
        aeq(read("-x^3-1").get().shiftLeft(1), "-2*x^3-2");
        aeq(read("-x^3-1").get().shiftLeft(2), "-4*x^3-4");
        aeq(read("-x^3-1").get().shiftLeft(3), "-8*x^3-8");
        aeq(read("-x^3-1").get().shiftLeft(4), "-16*x^3-16");
        aeq(read("-x^3-1").get().shiftLeft(-1), "-1/2*x^3-1/2");
        aeq(read("-x^3-1").get().shiftLeft(-2), "-1/4*x^3-1/4");
        aeq(read("-x^3-1").get().shiftLeft(-3), "-1/8*x^3-1/8");
        aeq(read("-x^3-1").get().shiftLeft(-4), "-1/16*x^3-1/16");
        aeq(read("1/2*x^10").get().shiftLeft(0), "1/2*x^10");
        aeq(read("1/2*x^10").get().shiftLeft(1), "x^10");
        aeq(read("1/2*x^10").get().shiftLeft(2), "2*x^10");
        aeq(read("1/2*x^10").get().shiftLeft(3), "4*x^10");
        aeq(read("1/2*x^10").get().shiftLeft(4), "8*x^10");
        aeq(read("1/2*x^10").get().shiftLeft(-1), "1/4*x^10");
        aeq(read("1/2*x^10").get().shiftLeft(-2), "1/8*x^10");
        aeq(read("1/2*x^10").get().shiftLeft(-3), "1/16*x^10");
        aeq(read("1/2*x^10").get().shiftLeft(-4), "1/32*x^10");
    }

    @Test
    public void testShiftRight() {
        assertTrue(ZERO.shiftRight(0) == ZERO);
        assertTrue(ZERO.shiftRight(1) == ZERO);
        assertTrue(ZERO.shiftRight(2) == ZERO);
        assertTrue(ZERO.shiftRight(3) == ZERO);
        assertTrue(ZERO.shiftRight(4) == ZERO);
        assertTrue(ZERO.shiftRight(-1) == ZERO);
        assertTrue(ZERO.shiftRight(-2) == ZERO);
        assertTrue(ZERO.shiftRight(-3) == ZERO);
        assertTrue(ZERO.shiftRight(-4) == ZERO);
        assertTrue(ONE.shiftRight(0) == ONE);
        aeq(ONE.shiftRight(1), "1/2");
        aeq(ONE.shiftRight(2), "1/4");
        aeq(ONE.shiftRight(3), "1/8");
        aeq(ONE.shiftRight(4), "1/16");
        aeq(ONE.shiftRight(-1), "2");
        aeq(ONE.shiftRight(-2), "4");
        aeq(ONE.shiftRight(-3), "8");
        aeq(ONE.shiftRight(-4), "16");
        aeq(X.shiftRight(0), X);
        aeq(X.shiftRight(1), "1/2*x");
        aeq(X.shiftRight(2), "1/4*x");
        aeq(X.shiftRight(3), "1/8*x");
        aeq(X.shiftRight(4), "1/16*x");
        aeq(X.shiftRight(-1), "2*x");
        aeq(X.shiftRight(-2), "4*x");
        aeq(X.shiftRight(-3), "8*x");
        aeq(X.shiftRight(-4), "16*x");
        aeq(read("-4/3").get().shiftRight(0), "-4/3");
        aeq(read("-4/3").get().shiftRight(1), "-2/3");
        aeq(read("-4/3").get().shiftRight(2), "-1/3");
        aeq(read("-4/3").get().shiftRight(3), "-1/6");
        aeq(read("-4/3").get().shiftRight(4), "-1/12");
        aeq(read("-4/3").get().shiftRight(-1), "-8/3");
        aeq(read("-4/3").get().shiftRight(-2), "-16/3");
        aeq(read("-4/3").get().shiftRight(-3), "-32/3");
        aeq(read("-4/3").get().shiftRight(-4), "-64/3");
        aeq(read("x^2-7/4*x+1/3").get().shiftRight(0), "x^2-7/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().shiftRight(1), "1/2*x^2-7/8*x+1/6");
        aeq(read("x^2-7/4*x+1/3").get().shiftRight(2), "1/4*x^2-7/16*x+1/12");
        aeq(read("x^2-7/4*x+1/3").get().shiftRight(3), "1/8*x^2-7/32*x+1/24");
        aeq(read("x^2-7/4*x+1/3").get().shiftRight(4), "1/16*x^2-7/64*x+1/48");
        aeq(read("x^2-7/4*x+1/3").get().shiftRight(-1), "2*x^2-7/2*x+2/3");
        aeq(read("x^2-7/4*x+1/3").get().shiftRight(-2), "4*x^2-7*x+4/3");
        aeq(read("x^2-7/4*x+1/3").get().shiftRight(-3), "8*x^2-14*x+8/3");
        aeq(read("x^2-7/4*x+1/3").get().shiftRight(-4), "16*x^2-28*x+16/3");
        aeq(read("-x^3-1").get().shiftRight(0), "-x^3-1");
        aeq(read("-x^3-1").get().shiftRight(1), "-1/2*x^3-1/2");
        aeq(read("-x^3-1").get().shiftRight(2), "-1/4*x^3-1/4");
        aeq(read("-x^3-1").get().shiftRight(3), "-1/8*x^3-1/8");
        aeq(read("-x^3-1").get().shiftRight(4), "-1/16*x^3-1/16");
        aeq(read("-x^3-1").get().shiftRight(-1), "-2*x^3-2");
        aeq(read("-x^3-1").get().shiftRight(-2), "-4*x^3-4");
        aeq(read("-x^3-1").get().shiftRight(-3), "-8*x^3-8");
        aeq(read("-x^3-1").get().shiftRight(-4), "-16*x^3-16");
        aeq(read("1/2*x^10").get().shiftRight(0), "1/2*x^10");
        aeq(read("1/2*x^10").get().shiftRight(1), "1/4*x^10");
        aeq(read("1/2*x^10").get().shiftRight(2), "1/8*x^10");
        aeq(read("1/2*x^10").get().shiftRight(3), "1/16*x^10");
        aeq(read("1/2*x^10").get().shiftRight(4), "1/32*x^10");
        aeq(read("1/2*x^10").get().shiftRight(-1), "x^10");
        aeq(read("1/2*x^10").get().shiftRight(-2), "2*x^10");
        aeq(read("1/2*x^10").get().shiftRight(-3), "4*x^10");
        aeq(read("1/2*x^10").get().shiftRight(-4), "8*x^10");
    }

    @Test
    public void testSum() {
        assertTrue(sum(readRationalPolynomialList("[]").get()) == ZERO);
        assertTrue(sum(readRationalPolynomialList("[1]").get()) == ONE);
        aeq(sum(readRationalPolynomialList("[-4/3]").get()), "-4/3");
        aeq(
                sum(readRationalPolynomialList("[-4/3, x^2-7/4*x+1/3, -x^3-1, 1/2*x^10]").get()),
                "1/2*x^10-x^3+x^2-7/4*x-2"
        );
        try {
            sum(readRationalPolynomialListWithNulls("[-4/3, null, -x^3-1, 1/2*x^10]").get());
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProduct() {
        assertTrue(product(readRationalPolynomialList("[]").get()) == ONE);
        assertTrue(product(readRationalPolynomialList("[0]").get()) == ZERO);
        aeq(product(readRationalPolynomialList("[-4/3]").get()), "-4/3");
        aeq(
                product(readRationalPolynomialList("[-4/3, x^2-7/4*x+1/3, -x^3-1, 1/2*x^10]").get()),
                "2/3*x^15-7/6*x^14+2/9*x^13+2/3*x^12-7/6*x^11+2/9*x^10"
        );
        try {
            product(readRationalPolynomialListWithNulls("[-4/3, null, -x^3-1, 1/2*x^10]").get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testDelta() {
        aeqit(delta(readRationalPolynomialList("[-4/3]").get()), "[]");
        aeqit(delta(readRationalPolynomialList("[-4/3, x^2-7/4*x+1/3]").get()), "[x^2-7/4*x+5/3]");
        aeqit(
                delta(readRationalPolynomialList("[-4/3, x^2-7/4*x+1/3, -x^3-1, 1/2*x^10]").get()),
                "[x^2-7/4*x+5/3, -x^3-x^2+7/4*x-4/3, 1/2*x^10+x^3+1]"
        );
        try {
            delta(readRationalPolynomialList("[]").get());
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(delta(readRationalPolynomialListWithNulls("[-4/3, null, -x^3-1, 1/2*x^10]").get()));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testIsMonic() {
        assertFalse(ZERO.isMonic());
        assertTrue(ONE.isMonic());
        assertTrue(X.isMonic());
        assertFalse(read("-4/3").get().isMonic());
        assertTrue(read("x^2-7/4*x+1/3").get().isMonic());
        assertFalse(read("-x^3-1").get().isMonic());
        assertFalse(read("1/2*x^10").get().isMonic());
    }

    @Test
    public void testMakeMonic() {
        assertTrue(ONE.makeMonic() == ONE);
        aeq(X.makeMonic(), "x");
        assertTrue(read("-4/3").get().makeMonic() == ONE);
        aeq(read("x^2-7/4*x+1/3").get().makeMonic(), "x^2-7/4*x+1/3");
        aeq(read("3*x^2-7/4*x+1/3").get().makeMonic(), "x^2-7/12*x+1/9");
        aeq(read("-x^3-1").get().makeMonic(), "x^3+1");
        aeq(read("1/2*x^10").get().makeMonic(), "x^10");
        try {
            ZERO.makeMonic();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testContentAndPrimitive() {
        aeq(ONE.contentAndPrimitive(), "(1, 1)");
        aeq(X.contentAndPrimitive(), "(1, x)");
        aeq(read("-4/3").get().contentAndPrimitive(), "(-4/3, 1)");
        aeq(read("x^2-4*x+7").get().contentAndPrimitive(), "(1, x^2-4*x+7)");
        aeq(read("6*x^2-4*x+8").get().contentAndPrimitive(), "(2, 3*x^2-2*x+4)");
        aeq(read("x^2-7/4*x+1/3").get().contentAndPrimitive(), "(1/12, 12*x^2-21*x+4)");
        aeq(read("-x^3-1").get().contentAndPrimitive(), "(-1, x^3+1)");
        aeq(read("1/2*x^10").get().contentAndPrimitive(), "(1/2, x^10)");
        try {
            ZERO.contentAndPrimitive();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_RationalPolynomial() {
        aeq(read("x^3-2*x^2-4").get().divide(read("x-3").get()), "(x^2+x+3, 5)");
        aeq(read("x^3-12*x^2-42").get().divide(read("x^2-2*x+1").get()), "(x-10, -21*x-32)");
        aeq(ZERO.divide(ONE), "(0, 0)");
        aeq(ZERO.divide(X), "(0, 0)");
        aeq(ZERO.divide(read("-4/3").get()), "(0, 0)");
        aeq(ZERO.divide(read("x^2-7/4*x+1/3").get()), "(0, 0)");
        aeq(ZERO.divide(read("-x^3-1").get()), "(0, 0)");
        aeq(ZERO.divide(read("1/2*x^10").get()), "(0, 0)");
        aeq(ONE.divide(ONE), "(1, 0)");
        aeq(ONE.divide(X), "(0, 1)");
        aeq(ONE.divide(read("-4/3").get()), "(-3/4, 0)");
        aeq(ONE.divide(read("x^2-7/4*x+1/3").get()), "(0, 1)");
        aeq(ONE.divide(read("-x^3-1").get()), "(0, 1)");
        aeq(ONE.divide(read("1/2*x^10").get()), "(0, 1)");
        aeq(X.divide(ONE), "(x, 0)");
        aeq(X.divide(X), "(1, 0)");
        aeq(X.divide(read("-4/3").get()), "(-3/4*x, 0)");
        aeq(X.divide(read("x^2-7/4*x+1/3").get()), "(0, x)");
        aeq(X.divide(read("-x^3-1").get()), "(0, x)");
        aeq(X.divide(read("1/2*x^10").get()), "(0, x)");
        aeq(read("-4/3").get().divide(ONE), "(-4/3, 0)");
        aeq(read("-4/3").get().divide(X), "(0, -4/3)");
        aeq(read("-4/3").get().divide(read("-4/3").get()), "(1, 0)");
        aeq(read("-4/3").get().divide(read("x^2-7/4*x+1/3").get()), "(0, -4/3)");
        aeq(read("-4/3").get().divide(read("-x^3-1").get()), "(0, -4/3)");
        aeq(read("-4/3").get().divide(read("1/2*x^10").get()), "(0, -4/3)");
        aeq(read("x^2-7/4*x+1/3").get().divide(ONE), "(x^2-7/4*x+1/3, 0)");
        aeq(read("x^2-7/4*x+1/3").get().divide(X), "(x-7/4, 1/3)");
        aeq(read("x^2-7/4*x+1/3").get().divide(read("-4/3").get()), "(-3/4*x^2+21/16*x-1/4, 0)");
        aeq(read("x^2-7/4*x+1/3").get().divide(read("x^2-7/4*x+1/3").get()), "(1, 0)");
        aeq(read("x^2-7/4*x+1/3").get().divide(read("-x^3-1").get()), "(0, x^2-7/4*x+1/3)");
        aeq(read("x^2-7/4*x+1/3").get().divide(read("1/2*x^10").get()), "(0, x^2-7/4*x+1/3)");
        aeq(read("-x^3-1").get().divide(ONE), "(-x^3-1, 0)");
        aeq(read("-x^3-1").get().divide(X), "(-x^2, -1)");
        aeq(read("-x^3-1").get().divide(read("-4/3").get()), "(3/4*x^3+3/4, 0)");
        aeq(read("-x^3-1").get().divide(read("x^2-7/4*x+1/3").get()), "(-x-7/4, -131/48*x-5/12)");
        aeq(read("-x^3-1").get().divide(read("-x^3-1").get()), "(1, 0)");
        aeq(read("-x^3-1").get().divide(read("1/2*x^10").get()), "(0, -x^3-1)");
        aeq(read("1/2*x^10").get().divide(ONE), "(1/2*x^10, 0)");
        aeq(read("1/2*x^10").get().divide(X), "(1/2*x^9, 0)");
        aeq(read("1/2*x^10").get().divide(read("-4/3").get()), "(-3/8*x^10, 0)");
        aeq(
                read("1/2*x^10").get().divide(read("x^2-7/4*x+1/3").get()),
                "(1/2*x^8+7/8*x^7+131/96*x^6+805/384*x^5+14809/4608*x^4+10087/2048*x^3+1669499/221184*x^2" +
                "+10233965/884736*x+188201281/10616832," +
                " 1153665527/42467328*x-188201281/31850496)"
        );
        aeq(read("1/2*x^10").get().divide(read("-x^3-1").get()), "(-1/2*x^7+1/2*x^4-1/2*x, -1/2*x)");
        aeq(read("1/2*x^10").get().divide(read("1/2*x^10").get()), "(1, 0)");
        try {
            ZERO.divide(ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("x^2-7/4*x+1/3").get().divide(ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
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
        assertTrue(read("-4/3").get().pow(0) == ONE);
        aeq(read("-4/3").get().pow(1), "-4/3");
        aeq(read("-4/3").get().pow(2), "16/9");
        aeq(read("-4/3").get().pow(3), "-64/27");
        assertTrue(read("x^2-7/4*x+1/3").get().pow(0) == ONE);
        aeq(read("x^2-7/4*x+1/3").get().pow(1), "x^2-7/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().pow(2), "x^4-7/2*x^3+179/48*x^2-7/6*x+1/9");
        aeq(read("x^2-7/4*x+1/3").get().pow(3), "x^6-21/4*x^5+163/16*x^4-567/64*x^3+163/48*x^2-7/12*x+1/27");
        assertTrue(read("-x^3-1").get().pow(0) == ONE);
        aeq(read("-x^3-1").get().pow(1), "-x^3-1");
        aeq(read("-x^3-1").get().pow(2), "x^6+2*x^3+1");
        aeq(read("-x^3-1").get().pow(3), "-x^9-3*x^6-3*x^3-1");
        assertTrue(read("1/2*x^10").get().pow(0) == ONE);
        aeq(read("1/2*x^10").get().pow(1), "1/2*x^10");
        aeq(read("1/2*x^10").get().pow(2), "1/4*x^20");
        aeq(read("1/2*x^10").get().pow(3), "1/8*x^30");
    }

    @Test
    public void testSubstitute() {
        assertTrue(ZERO.substitute(ZERO) == ZERO);
        assertTrue(ZERO.substitute(ONE) == ZERO);
        assertTrue(ZERO.substitute(X) == ZERO);
        assertTrue(ZERO.substitute(read("-4/3").get()) == ZERO);
        assertTrue(ZERO.substitute(read("x^2-7/4*x+1/3").get()) == ZERO);
        assertTrue(ZERO.substitute(read("-x^3-1").get()) == ZERO);
        assertTrue(ZERO.substitute(read("1/2*x^10").get()) == ZERO);
        assertTrue(ONE.substitute(ZERO) == ONE);
        assertTrue(ONE.substitute(ONE) == ONE);
        assertTrue(ONE.substitute(X) == ONE);
        assertTrue(ONE.substitute(read("-4/3").get()) == ONE);
        assertTrue(ONE.substitute(read("x^2-7/4*x+1/3").get()) == ONE);
        assertTrue(ONE.substitute(read("-x^3-1").get()) == ONE);
        assertTrue(ONE.substitute(read("1/2*x^10").get()) == ONE);
        assertTrue(X.substitute(ZERO) == ZERO);
        assertTrue(X.substitute(ONE) == ONE);
        aeq(X.substitute(X), "x");
        aeq(X.substitute(read("-4/3").get()), "-4/3");
        aeq(X.substitute(read("x^2-7/4*x+1/3").get()), "x^2-7/4*x+1/3");
        aeq(X.substitute(read("-x^3-1").get()), "-x^3-1");
        aeq(X.substitute(read("1/2*x^10").get()), "1/2*x^10");
        aeq(read("-4/3").get().substitute(ZERO), "-4/3");
        aeq(read("-4/3").get().substitute(ONE), "-4/3");
        aeq(read("-4/3").get().substitute(X), "-4/3");
        aeq(read("-4/3").get().substitute(read("-4/3").get()), "-4/3");
        aeq(read("-4/3").get().substitute(read("x^2-4*x+7").get()), "-4/3");
        aeq(read("-4/3").get().substitute(read("-x^3-1").get()), "-4/3");
        aeq(read("-4/3").get().substitute(read("1/2*x^10").get()), "-4/3");
        aeq(read("x^2-7/4*x+1/3").get().substitute(ZERO), "1/3");
        aeq(read("x^2-7/4*x+1/3").get().substitute(ONE), "-5/12");
        aeq(read("x^2-7/4*x+1/3").get().substitute(X), "x^2-7/4*x+1/3");
        aeq(read("x^2-7/4*x+1/3").get().substitute(read("-4/3").get()), "40/9");
        aeq(read("x^2-7/4*x+1/3").get().substitute(read("x^2-7/4*x+1/3").get()), "x^4-7/2*x^3+95/48*x^2+91/48*x-5/36");
        aeq(read("x^2-7/4*x+1/3").get().substitute(read("-x^3-1").get()), "x^6+15/4*x^3+37/12");
        aeq(read("x^2-7/4*x+1/3").get().substitute(read("1/2*x^10").get()), "1/4*x^20-7/8*x^10+1/3");
        aeq(read("-x^3-1").get().substitute(ZERO), "-1");
        aeq(read("-x^3-1").get().substitute(ONE), "-2");
        aeq(read("-x^3-1").get().substitute(X), "-x^3-1");
        aeq(read("-x^3-1").get().substitute(read("-4/3").get()), "37/27");
        aeq(read("-x^3-1").get().substitute(read("x^2-7/4*x+1/3").get()),
                "-x^6+21/4*x^5-163/16*x^4+567/64*x^3-163/48*x^2+7/12*x-28/27");
        aeq(read("-x^3-1").get().substitute(read("-x^3-1").get()), "x^9+3*x^6+3*x^3");
        aeq(read("-x^3-1").get().substitute(read("1/2*x^10").get()), "-1/8*x^30-1");
        assertTrue(read("1/2*x^10").get().substitute(ZERO) == ZERO);
        aeq(read("1/2*x^10").get().substitute(ONE), "1/2");
        aeq(read("1/2*x^10").get().substitute(X), "1/2*x^10");
        aeq(read("1/2*x^10").get().substitute(read("-4/3").get()), "524288/59049");
        aeq(read("1/2*x^10").get().substitute(read("x^2-4*x+7").get()),
                "1/2*x^20-20*x^19+395*x^18-5100*x^17+96285/2*x^16-352464*x^15+2073540*x^14-10026480*x^13" +
                "+40440585*x^12-137326040*x^11+394631330*x^10-961282280*x^9+1981588665*x^8-3439082640*x^7" +
                "+4978569540*x^6-5923862448*x^5+11327833965/2*x^4-4200069300*x^3+2277096395*x^2-807072140*x" +
                "+282475249/2");
        aeq(read("1/2*x^10").get().substitute(read("-x^3-1").get()),
                "1/2*x^30+5*x^27+45/2*x^24+60*x^21+105*x^18+126*x^15+105*x^12+60*x^9+45/2*x^6+5*x^3+1/2");
        aeq(read("1/2*x^10").get().substitute(read("1/2*x^10").get()), "1/2048*x^100");
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

    private static void aeqit(Iterable<?> a, Object b) {
        assertEquals(IterableUtils.toString(a), b.toString());
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

    private static @NotNull Optional<List<RationalPolynomial>> readRationalPolynomialList(@NotNull String s) {
        return Readers.readList(RationalPolynomial::read).apply(s);
    }

    private static @NotNull Optional<List<RationalPolynomial>> readRationalPolynomialListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(RationalPolynomial::read).apply(s);
    }
}