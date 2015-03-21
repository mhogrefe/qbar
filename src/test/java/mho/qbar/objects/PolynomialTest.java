package mho.qbar.objects;

import org.junit.Test;

import static mho.qbar.objects.Polynomial.*;
import static org.junit.Assert.assertEquals;

public class PolynomialTest {
    @Test
    public void testConstants() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
        aeq(X, "x");
    }

    private static void aeq(Object a, Object b) {
        assertEquals(a.toString(), b.toString());
    }
}
