package mho.qbar.objects;

import org.junit.Test;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.assertEquals;

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

    private static void aeq(Object a, Object b) {
        assertEquals(a.toString(), b.toString());
    }
}