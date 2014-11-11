package mho.qbar.iterableProviders;

import mho.wheels.iterables.IterableUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.assertEquals;

public class QBarExhaustiveProviderTest {
    private static final @NotNull QBarExhaustiveProvider P = new QBarExhaustiveProvider();

    @Test
    public void testRationals() {
        aeq(take(50, P.rationals()),
                "[0, 1, 1/2, 1/3, 1/4, -1, -1/2, 2, -1/3, -1/4, 2/3, 1/5, 1/6, 1/7, 1/8, -1/5, -1/6, 2/5, -1/7," +
                " -1/8, 2/7, -2, 3, 3/2, -2/3, 3/4, -3, -3/2, 4, -3/4, 4/3, -2/5, 3/5, -2/7, 3/7, 3/8, -3/5, 4/5," +
                " -3/7, -3/8, 4/7, 1/9, 1/10, 1/11, 1/12, -1/9, -1/10, 2/9, -1/11, -1/12]");
    }

    @Test
    public void testNonNegativeRationals() {
        aeq(take(50, P.nonNegativeRationals()),
                "[0, 1, 1/2, 1/3, 1/4, 2, 3, 3/2, 2/3, 3/4, 1/5, 1/6, 1/7, 1/8, 2/5, 3/5, 2/7, 3/7, 3/8, 4, 5, 5/2," +
                " 4/3, 5/3, 5/4, 6, 7, 7/2, 7/3, 7/4, 4/5, 5/6, 4/7, 5/7, 5/8, 6/5, 7/5, 7/6, 6/7, 7/8, 1/9, 1/10," +
                " 1/11, 1/12, 2/9, 3/10, 2/11, 3/11, 1/13, 1/14]");
    }

    @Test
    public void testPositiveRationals() {
        aeq(take(50, P.positiveRationals()),
                "[1, 1/2, 2, 1/3, 1/4, 2/3, 3, 3/2, 4, 3/4, 4/3, 1/5, 1/6, 2/5, 1/7, 1/8, 2/7, 3/5, 4/5, 3/7, 3/8," +
                " 4/7, 5, 5/2, 6, 5/3, 5/4, 7, 7/2, 8, 7/3, 7/4, 8/3, 5/6, 6/5, 5/7, 5/8, 6/7, 7/5, 7/6, 8/5, 7/8," +
                " 8/7, 1/9, 1/10, 2/9, 1/11, 1/12, 2/11, 3/10]");
    }

    @Test
    public void testNegativeRationals() {
        aeq(take(50, P.negativeRationals()),
                "[-1, -1/2, -2, -1/3, -1/4, -2/3, -3, -3/2, -4, -3/4, -4/3, -1/5, -1/6, -2/5, -1/7, -1/8, -2/7," +
                " -3/5, -4/5, -3/7, -3/8, -4/7, -5, -5/2, -6, -5/3, -5/4, -7, -7/2, -8, -7/3, -7/4, -8/3, -5/6," +
                " -6/5, -5/7, -5/8, -6/7, -7/5, -7/6, -8/5, -7/8, -8/7, -1/9, -1/10, -2/9, -1/11, -1/12, -2/11," +
                " -3/10]");
    }

    @Test
    public void testNonNegativeRationalsLessThanOne() {
        aeq(take(50, P.nonNegativeRationalsLessThanOne()),
                "[0, 1/2, 1/3, 1/4, 2/3, 3/4, 1/5, 1/6, 1/7, 1/8, 2/5, 3/5, 2/7, 3/7, 3/8, 4/5, 5/6, 4/7, 5/7, 5/8," +
                " 6/7, 7/8, 1/9, 1/10, 1/11, 1/12, 2/9, 3/10, 2/11, 3/11, 1/13, 1/14, 1/15, 1/16, 2/13, 3/13, 3/14," +
                " 2/15, 3/16, 4/9, 5/9, 4/11, 5/11, 5/12, 7/9, 7/10, 6/11, 7/11, 7/12, 4/13]");
    }

    @Test
    public void testFinitelyBoundedIntervals() {
        aeq(take(50, P.finitelyBoundedIntervals()),
                "[[0, 0], [0, 1], [1, 1], [0, 1/2], [0, 1/3], [1/2, 1], [1/3, 1], [1/2, 1/2], [1/3, 1/2]," +
                " [1/3, 1/3], [0, 1/4], [0, 2], [1, 2], [1/2, 2], [1/3, 2], [1/4, 1], [-1, 0], [-1, 1], [1/4, 1/2]," +
                " [1/4, 1/3], [-1, 1/2], [-1, 1/3], [-1/2, 0], [-1/2, 1], [-1/2, 1/2], [-1/2, 1/3], [1/4, 1/4]," +
                " [-1, 1/4], [-1, -1], [1/4, 2], [-1, -1/2], [-1, 2], [-1/2, 1/4], [-1/2, -1/2], [-1/2, 2], [2, 2]," +
                " [0, 2/3], [0, 1/5], [1/2, 2/3], [1/3, 2/3], [0, 1/6], [0, 1/7], [0, 1/8], [-1, -1/3], [-1, -1/4]," +
                " [1/4, 2/3], [-1, 2/3], [-1, 1/5], [-1/2, -1/3], [-1/2, -1/4]]");
    }

    @Test
    public void testIntervals() {
        aeq(take(50, P.intervals()),
                "[(-Infinity, Infinity), (-Infinity, 0], [0, Infinity), [0, 0], (-Infinity, 1], (-Infinity, 1/2]," +
                " [0, 1], [0, 1/2], [1, Infinity), [1/2, Infinity), [1, 1], [1/2, 1], [1/2, 1/2], (-Infinity, 1/3]," +
                " (-Infinity, 1/4], [0, 1/3], [0, 1/4], (-Infinity, -1], (-Infinity, -1/2], [1/3, Infinity)," +
                " [1/4, Infinity), [1/3, 1], [1/3, 1/2], [1/4, 1], [1/4, 1/2], [-1, Infinity), [-1, 0]," +
                " [-1/2, Infinity), [-1/2, 0], [-1, 1], [-1, 1/2], [-1/2, 1], [-1/2, 1/2], [1/3, 1/3], [1/4, 1/3]," +
                " [1/4, 1/4], [-1, 1/3], [-1, 1/4], [-1/2, 1/3], [-1/2, 1/4], [-1, -1], [-1, -1/2], [-1/2, -1/2]," +
                " (-Infinity, 2], (-Infinity, -1/3], [0, 2], (-Infinity, -1/4], (-Infinity, 2/3], [0, 2/3], [1, 2]]");
    }

    private static void aeq(Iterable<?> a, Object b) {
        assertEquals(IterableUtils.toString(a), b.toString());
    }
}