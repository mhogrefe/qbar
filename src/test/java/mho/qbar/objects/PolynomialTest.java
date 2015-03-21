package mho.qbar.objects;

import org.junit.Test;

import static mho.qbar.objects.Polynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.assertEquals;
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

    private static void aeq(Object a, Object b) {
        assertEquals(a.toString(), b.toString());
    }
}
