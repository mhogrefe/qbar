package mho.qbar.objects;

import org.junit.Test;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.assertEquals;
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

    private static void aeq(Object a, Object b) {
        assertEquals(a.toString(), b.toString());
    }
}